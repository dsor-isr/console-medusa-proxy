package com.yebisu.medusa.service;

import com.yebisu.medusa.service.dto.VehicleState;
import reactor.core.publisher.Mono;

public interface VehicleService {
    Mono<VehicleState> getState(String vehicleId);
}
