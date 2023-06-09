package com.yebisu.medusa.proxy.rosmessage;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.proxy.configserver.dto.MissionDTO;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MedusaRestProxy {
    Content getVehicleState(String ip);

    Mono<ResponseEntity<Void>> moveVehicleTo(String vehicleIP, Point point);

    Flux<Void> executeMission(String coordinates, Flux<String> vehiclesIPs);
}
