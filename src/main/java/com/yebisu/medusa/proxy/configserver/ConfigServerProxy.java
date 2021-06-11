package com.yebisu.medusa.proxy.configserver;

import com.yebisu.medusa.proxy.configserver.dto.MissionDTO;
import com.yebisu.medusa.proxy.configserver.dto.VehicleConfigurationDTO;
import reactor.core.publisher.Mono;

public interface ConfigServerProxy {

    Mono<MissionDTO> getMissionById(String missionId);

    Mono<VehicleConfigurationDTO> getVehicleConfigById(String id);
}
