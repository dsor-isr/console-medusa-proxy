package com.yebisu.medusa.controller;

import com.yebisu.medusa.controller.dto.VehicleConfigurationDTO;
import com.yebisu.medusa.controller.mapper.VehicleMapper;
import com.yebisu.medusa.domain.VehicleConfiguration;
import com.yebisu.medusa.service.VehicleService;
import com.yebisu.medusa.util.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(API.VEHICLE_BASE_API)
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleMapper vehicleMapper;
    private final VehicleService vehicleService;

    @PostMapping("/configuration")
    public Mono<VehicleConfigurationDTO> createConfiguration(@RequestBody @Valid final VehicleConfigurationDTO vehicleConfigurationDTO) {
        VehicleConfiguration vehicleConfiguration = vehicleMapper.mapTo(vehicleConfigurationDTO);
        return vehicleService.create(vehicleConfiguration)
                .map(this.vehicleMapper::mapTo)
                .log();
    }

    @GetMapping("/configuration/{id}")
    public Mono<VehicleConfigurationDTO> findById(@PathVariable final String id) {
        return vehicleService.findById(id)
                .map(vehicleMapper::mapTo)
                .log();
    }

    @GetMapping("/configuration")
    public Flux<VehicleConfiguration> findAll() {
      return vehicleService.findAll()
              .log();
    }

    @DeleteMapping("/configuration/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> deleteById(@PathVariable final String id){
        return vehicleService.deleteById(id)
                .log();
    }

    @DeleteMapping("/configuration")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> deleteAll(){
       return vehicleService.deleteAll()
               .log();
    }
}
