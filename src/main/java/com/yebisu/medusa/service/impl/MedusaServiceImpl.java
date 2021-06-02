package com.yebisu.medusa.service.impl;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.exception.CustomException;
import com.yebisu.medusa.exception.ResourceNotFoundException;
import com.yebisu.medusa.proxy.configserver.ConfigServerProxy;
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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedusaServiceImpl implements MedusaService {
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

        return configServerProxy.getMissionById(missionId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format("Couldn't find any mission with id: %s id", missionId))))
                .flatMap(missionDTO -> medusaRestProxy.executeMission(missionDTO.getId(), vehicleDetails));
    }
}
