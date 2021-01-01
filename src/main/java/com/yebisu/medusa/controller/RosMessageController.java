package com.yebisu.medusa.controller;

import com.yebisu.medusa.controller.dto.VehicleStateInputDto;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.service.dto.VehicleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
@Slf4j
public class RosMessageController {

    private final VehicleService vehicleService;

    @PostMapping(value = "/state")
    public Flux<VehicleState> getState(@RequestBody @Valid final VehicleStateInputDto vehicleStateInput) {
        log.info("GET: Vehicle state: {}", vehicleStateInput);
        return Flux.just(vehicleService.getState(vehicleStateInput.getVehicleIP()))
                .log();
    }
}
