package com.yebisu.medusa.service;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.service.dto.VehicleState;
import reactor.core.publisher.Mono;

public interface VehicleService {
    Mono<VehicleState> getState(String vehicleId);

    Mono<Void> moveVehicleTo(String vehicleId, Point dto);
}
