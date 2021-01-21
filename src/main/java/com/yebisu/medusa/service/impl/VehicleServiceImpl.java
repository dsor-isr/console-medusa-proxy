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
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format("Couldn't find any vehicle config with id: %s", id))));
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
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(String.format("Couldn't find any vehicle config with id: %s", id))))
                .doOnNext(vehicle -> vehicleRepository.deleteById(id))
                .doOnSuccess(vehicleConfiguration -> log.info("Vehicle configuration with id {} has been deleted",id))
                .then();

    }

    @Override
    public Mono<Void> deleteAll() {
        return vehicleRepository.deleteAll();
    }
}
