package com.yebisu.medusa.service;

import com.yebisu.medusa.domain.VehicleConfiguration;
import com.yebisu.medusa.service.dto.VehicleState;
import reactor.core.publisher.Mono;

public interface VehicleService {
    Mono<VehicleConfiguration> saveConfiguration(VehicleConfiguration vehicleConfiguration);
    VehicleState getState(String ip);
}
