package com.yebisu.medusa.controller;

import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.service.dto.VehicleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
@Slf4j
public class RosMessageController {

    private final VehicleService vehicleService;

    @GetMapping(value = "/state")
    public Flux<VehicleState> getState() {
        log.info("GET: Vehicle state");
        return Flux.just(vehicleService.getState())
                .log();
    }
}
