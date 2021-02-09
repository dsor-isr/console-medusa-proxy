package com.yebisu.medusa.controller;

import com.yebisu.medusa.exception.CustomException;
import com.yebisu.medusa.exception.ResourceNotFoundException;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.service.dto.VehicleState;
import com.yebisu.medusa.util.API;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping(API.VEHICLE_BASE_API)
@RequiredArgsConstructor
@Slf4j
public class RosMessageController {

    private final VehicleService vehicleService;

    @GetMapping(value = "/{id}/state")
    public Mono<VehicleState> getState(@PathVariable("id") final String vehicleId) {
        log.debug("GET: Vehicle state: {}", vehicleId);
        return vehicleService.getState(vehicleId)
                .timeout(Duration.ofSeconds(1))
                .log();
    }

}
