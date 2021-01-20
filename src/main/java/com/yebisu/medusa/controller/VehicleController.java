package com.yebisu.medusa.controller;

import com.yebisu.medusa.controller.dto.VehicleConfigurationDTO;
import com.yebisu.medusa.controller.mapper.VehicleMapper;
import com.yebisu.medusa.domain.VehicleConfiguration;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.util.API;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(API.VEHICLE_BASE_API)
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleMapper vehicleMapper;
    private final VehicleService vehicleService;

    @PostMapping("/configuration")
    public Mono<VehicleConfigurationDTO> createConfiguration(@RequestBody final VehicleConfigurationDTO vehicleConfigurationDTO) {
        VehicleConfiguration vehicleConfiguration = vehicleMapper.mapTo(vehicleConfigurationDTO);
        return vehicleService.saveConfiguration(vehicleConfiguration)
                .map(this.vehicleMapper::mapTo)
                .log();
    }
}
