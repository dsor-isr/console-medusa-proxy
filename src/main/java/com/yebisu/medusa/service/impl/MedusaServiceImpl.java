package com.yebisu.medusa.service.impl;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.exception.CustomException;
import com.yebisu.medusa.exception.ResourceNotFoundException;
import com.yebisu.medusa.proxy.configserver.ConfigServerProxy;
import com.yebisu.medusa.proxy.configserver.dto.ArcDTO;
import com.yebisu.medusa.proxy.configserver.dto.LineDTO;
import com.yebisu.medusa.proxy.configserver.dto.MissionDTO;
import com.yebisu.medusa.proxy.configserver.dto.VehicleConfigurationDTO;
import com.yebisu.medusa.proxy.rosmessage.MedusaRestProxy;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import com.yebisu.medusa.service.MedusaService;
import com.yebisu.medusa.service.dto.VehicleState;
import com.yebisu.medusa.service.mapper.VehicleStateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedusaServiceImpl implements MedusaService {
    private static final String PERCENT_TWENTY = "%20";
    private final MedusaRestProxy medusaRestProxy;
    private final ConfigServerProxy configServerProxy;
    private final VehicleStateMapper vehicleStateMapper;

    @Override
    public Mono<VehicleState> getState(final String vehicleId) {
        return configServerProxy.getVehicleConfigById(vehicleId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format("Couldn't find any vehicle with id: %s id", vehicleId))))
                .flatMap(this::getVehicleState)
                .map(vehicleStateMapper::mapFom);
    }

    private Mono<Content> getVehicleState(final VehicleConfigurationDTO vehicleConfigDTO) {
        final String ipAddress = vehicleConfigDTO.getIpAddress();
        Content content = medusaRestProxy.getVehicleState(ipAddress);
        return Mono.just(content);
    }

    @Override
    public Mono<ResponseEntity<Void>> moveVehicleTo(final String vehicleId, final Point point) {
        Objects.requireNonNull(vehicleId, "vehicle id cannot be null");
        Objects.requireNonNull(point, "point details must not be null in order to move the vehicle");
        return configServerProxy.getVehicleConfigById(vehicleId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format("Couldn't find any vehicle with id: %s id", vehicleId))))
                .flatMap(vehicleIP -> moveVehicleByIP(point, vehicleIP.getIpAddress()))
                .log();
    }

    private Mono<ResponseEntity<Void>> moveVehicleByIP(Point point, String vehicleIP) {
        return medusaRestProxy.moveVehicleTo(vehicleIP, point);
    }

    @Override
    public Mono<Void> executeMission(final String missionId, final List<VehicleDetails> vehicleDetails) {
        Objects.requireNonNull(missionId, "mission id cannot be null");
        Objects.requireNonNull(vehicleDetails, "vehicle details must not be null in order to execute mission");

        if (vehicleDetails.isEmpty()) {
            throw new CustomException("no vehicle details provided. The collection is empty");
        }

        Set<String> vehicleIds = vehicleDetails.stream().map(VehicleDetails::getVehicleId).collect(Collectors.toSet());
        Flux<String> ids = getVehicleById(vehicleIds).map(VehicleConfigurationDTO::getIpAddress);

        return configServerProxy.getMissionById(missionId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format("Couldn't find any mission with id: %s id", missionId))))
                .flatMap(missionDTO -> {
                    log.info(missionDTO.toString());
                    String missionToExecute = prepareMissionExecution(missionDTO, vehicleDetails);
                    Flux<Void> voidFlux = medusaRestProxy.executeMission(missionToExecute, ids);
                    voidFlux.subscribe(response ->log.info("yooo "+response.toString()));
                    return Mono.empty();
                });

    }

    private Flux<VehicleConfigurationDTO> getVehicleById(Set<String> vehicleIds) {
        return Flux.fromIterable(vehicleIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(configServerProxy::getVehicleConfigById)
                .ordered(Comparator.comparing(VehicleConfigurationDTO::getName));
    }

    private String prepareMissionExecution(MissionDTO mission, List<VehicleDetails> vehicleDetails) {
        var map = new LinkedHashMap<Integer, String>();
        vehicleDetails.forEach(vehicleDetail -> composeCoordinates(mission, map, vehicleDetail));

        var xRefPoint = "0.0 ";
        var yRefPoint = "0.0 ";
        var utmZone = "29S\t\t";

        //TODO: INVOKE for each vehicle, maybe delegate this logic to the service
        //TODO: ask figueira if we must add the vehicle ip instead of -1
        StringBuilder uri = new StringBuilder()
                .append("RSET%20Mission_String%20std_msgs/String%20")
                .append(" {data: \"3\t\t")
                .append(PERCENT_TWENTY)
                .append(xRefPoint)
                .append(PERCENT_TWENTY)
                .append(yRefPoint)
                .append(PERCENT_TWENTY)
                .append(utmZone)
                .append(PERCENT_TWENTY);

        for (int i = 0; i <= map.size(); i++) {
            uri.append(map.get(i)).append(PERCENT_TWENTY);
        }
        return uri.toString();
    }


    private void composeCoordinates(MissionDTO missionDTO, Map<Integer, String> map, VehicleDetails vehicleDetail) {
        missionDTO.getLines().forEach(lineDTO -> computeLine(map, vehicleDetail, lineDTO));
        missionDTO.getArcs().forEach(arcDTO -> computeArc(map, vehicleDetail, arcDTO));
    }

    private void computeArc(Map<Integer, String> map, VehicleDetails vehicleDetail, ArcDTO arcDTO) {
        final var arcs = new StringBuilder()
                .append(arcDTO.getCoordinates().get(0).getX())
                .append(arcDTO.getCoordinates().get(0).getY())
                .append(arcDTO.getCoordinates().get(1).getX())
                .append(arcDTO.getCoordinates().get(1).getY())
                .append(arcDTO.getCoordinates().get(2).getX())
                .append(arcDTO.getCoordinates().get(2).getY())
                .append(vehicleDetail.getVelocity())
                .append(arcDTO.getDirection())
                .append(arcDTO.getRadius())
                .append("-1").toString();
        map.put(arcDTO.getIndex(), arcs);
    }

    private void computeLine(Map<Integer, String> map, VehicleDetails vehicleDetail, LineDTO lineDTO) {
        final var lines = new StringBuilder()
                .append(lineDTO.getCoordinates().get(0).getX())
                .append(lineDTO.getCoordinates().get(0).getY())
                .append(lineDTO.getCoordinates().get(1).getX())
                .append(lineDTO.getCoordinates().get(1).getY())
                .append(vehicleDetail.getVelocity())
                .append("-1").toString();
        map.put(lineDTO.getIndex(), lines);
    }
}
