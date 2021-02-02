package com.yebisu.medusa.proxy.configserver;

import com.yebisu.medusa.proxy.configserver.dto.VehicleConfigurationDTO;
import reactor.core.publisher.Mono;

public interface ConfigServerProxy {

    Mono<VehicleConfigurationDTO> getVehicleConfigById(String id);
}
