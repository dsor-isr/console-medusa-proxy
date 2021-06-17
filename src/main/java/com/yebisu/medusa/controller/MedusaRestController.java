package com.yebisu.medusa.controller;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.service.MedusaService;
import com.yebisu.medusa.service.dto.VehicleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class MedusaRestController {

    private final MedusaService medusaService;

    @PostMapping(value = "/mission/{missionId}/execute")
    public Mono<Void> executeMission(@PathVariable String missionId,
                                     @RequestBody @Valid List<VehicleDetails> vehicleDetails) {
        return medusaService.executeMission(missionId, vehicleDetails);
    }

    @GetMapping(value = "/vehicle/{id}/state")
    public Mono<VehicleState> getState(@PathVariable("id") final String vehicleId) {
        log.debug("GET: Vehicle state: {}", vehicleId);
        return medusaService.getState(vehicleId)
                .timeout(Duration.ofMillis(800));
    }

    @PostMapping(value = "/vehicle/{id}/move")
    public Mono<ResponseEntity<Void>> moveVehicleTo(@PathVariable("id") final String vehicleId, @RequestBody @Valid final Point point) {
        log.info("Moving the vehicle {} to coordinates: {}",vehicleId, point);
        return medusaService.moveVehicleTo(vehicleId, point)
                .timeout(Duration.ofSeconds(5))
                .log();
    }
}
