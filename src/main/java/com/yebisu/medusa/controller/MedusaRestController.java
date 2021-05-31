package com.yebisu.medusa.controller;

import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.service.MedusaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MedusaRestController {

    private final MedusaService medusaService;

    @PostMapping(value = "/mission/{missionId}/execute", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> executeMission(@PathVariable(value = "missionId") String missionId,
                                     @RequestBody @Valid List<VehicleDetails> vehicleDetails) {

        return medusaService.executeMission(missionId, vehicleDetails);
    }
}
