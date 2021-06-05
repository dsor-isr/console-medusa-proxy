package com.yebisu.medusa.controller;

import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.service.MedusaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class MedusaRestController {

    private final MedusaService medusaService;

    @PostMapping(value = "mission/{missionId}/execute")
    public Mono<Void> executeMission(@PathVariable String missionId,
                                     @RequestBody @Valid List<VehicleDetails> vehicleDetails) {
        log.info("/mission/{missionId}/execute");
        return medusaService.executeMission(missionId, vehicleDetails);
    }
}
