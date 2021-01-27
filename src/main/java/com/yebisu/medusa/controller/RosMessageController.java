package com.yebisu.medusa.controller;

import com.yebisu.medusa.controller.dto.VehicleStateInputDTO;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.service.dto.VehicleState;
import com.yebisu.medusa.util.API;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(API.VEHICLE_BASE_API)
@RequiredArgsConstructor
@Slf4j
public class RosMessageController {

    private final VehicleService vehicleService;


    @PostMapping(value = "/state")
    public Mono<VehicleState> getState(@RequestBody @Valid final VehicleStateInputDTO vehicleStateInput) {
        log.debug("GET: Vehicle state: {}", vehicleStateInput);
        return Mono.just(vehicleService.getState(vehicleStateInput.getVehicleIP()))
                .log();
    }

}
