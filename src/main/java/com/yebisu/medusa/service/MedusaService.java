package com.yebisu.medusa.service;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.service.dto.VehicleState;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MedusaService {
    Mono<VehicleState> getState(String vehicleId);

    Mono<ResponseEntity<Void>> moveVehicleTo(String vehicleId, Point dto);

    Mono<Void> executeMission(String missionId, List<VehicleDetails> vehicleDetails);
}
