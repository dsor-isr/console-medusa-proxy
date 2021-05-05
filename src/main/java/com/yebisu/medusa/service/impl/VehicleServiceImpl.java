package com.yebisu.medusa.service.impl;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.exception.ResourceNotFoundException;
import com.yebisu.medusa.proxy.configserver.ConfigServerProxy;
import com.yebisu.medusa.proxy.configserver.dto.VehicleConfigurationDTO;
import com.yebisu.medusa.proxy.rosmessage.ROSMessageProxy;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.service.dto.VehicleState;
import com.yebisu.medusa.service.mapper.VehicleStateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    private final ROSMessageProxy rosMessageProxy;
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
        Content content = rosMessageProxy.pingForROSMessageState(ipAddress);
        return Mono.just(content);
    }

    @Override
    public Mono<String> moveVehicleTo(final String vehicleId, final Point point) {
        return configServerProxy.getVehicleConfigById(vehicleId)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format("Couldn't find any vehicle with id: %s id", vehicleId))))
                .flatMap(vehicleIP -> moveVehicleByIP(point, vehicleIP.getIpAddress()))
                .log();
    }

    private Mono<String> moveVehicleByIP(Point point, String vehicleIP) {
        return rosMessageProxy.moveVehicleTo(vehicleIP, point);
    }


}