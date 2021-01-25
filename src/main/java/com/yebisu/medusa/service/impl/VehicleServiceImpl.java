package com.yebisu.medusa.service.impl;

import com.yebisu.medusa.domain.VehicleConfiguration;
import com.yebisu.medusa.exception.ResourceNotFoundException;
import com.yebisu.medusa.proxy.ROSMessageProxy;
import com.yebisu.medusa.proxy.model.Content;
import com.yebisu.medusa.repository.VehicleRepository;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.service.dto.VehicleState;
import com.yebisu.medusa.service.mapper.VehicleStateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    public static final String NOT_FOUND_BY_ID = "Couldn't find any vehicle config with id: %s";
    private final ROSMessageProxy rosMessageProxy;
    private final VehicleStateMapper vehicleStateMapper;
    private final VehicleRepository vehicleRepository;

    @Override
    public Mono<VehicleConfiguration> create(VehicleConfiguration vehicleConfiguration) {
        return vehicleRepository.save(vehicleConfiguration);
    }

    @Override
    public Mono<VehicleConfiguration> findById(String id) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID, id))));
    }

    @Override
    public Flux<VehicleConfiguration> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public VehicleState getState(final String ip) {
        Content content = rosMessageProxy.pingForROSMessageState(ip);
        return vehicleStateMapper.mapFom(content);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID, id))))
                .doOnSuccess(existingVehicle -> log.info("Deleting the Vehicle configuration: {}", existingVehicle))
                .flatMap(existingVehicleConfig -> vehicleRepository.deleteById(id));
    }

    @Override
    public Mono<Void> deleteAll() {
        return vehicleRepository.deleteAll();
    }

    @Override
    public Mono<VehicleConfiguration> update(final String id, final VehicleConfiguration vehicleConfiguration) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID, id))))
                .doOnSuccess(existingVehicle -> log.info("Updating the vehicle which old value is : {}, and new value is {}", existingVehicle, vehicleConfiguration))
                .flatMap(existingVehicleConfig -> {
                    vehicleConfiguration.setId(existingVehicleConfig.getId());
                    return vehicleRepository.save(vehicleConfiguration);
                });
    }
}
