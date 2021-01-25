package com.yebisu.medusa.service;

import com.yebisu.medusa.domain.VehicleConfiguration;
import com.yebisu.medusa.service.dto.VehicleState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleService {
    Mono<VehicleConfiguration> create(VehicleConfiguration vehicleConfiguration);

    Mono<VehicleConfiguration> findById(String id);

    VehicleState getState(String ip);

    Flux<VehicleConfiguration> findAll();

    Mono<VehicleConfiguration> update(String id, VehicleConfiguration vehicleConfiguration);

    Mono<Void> deleteAll();

    Mono<Void> deleteById(String id);
}
