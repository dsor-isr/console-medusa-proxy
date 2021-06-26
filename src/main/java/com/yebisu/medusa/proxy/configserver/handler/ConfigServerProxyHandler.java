package com.yebisu.medusa.proxy.configserver.handler;

import com.yebisu.medusa.proxy.configserver.ConfigServerProxy;
import com.yebisu.medusa.proxy.configserver.dto.MissionDTO;
import com.yebisu.medusa.proxy.configserver.dto.VehicleConfigurationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConfigServerProxyHandler implements ConfigServerProxy {

    private static final WebClient WEB_CLIENT = WebClient.create();

    @Value("${config-server-vehicle-config-base-uri}")
    private String vehicleConfigUrl;
    @Value("${configserver.mission.base.uri}")
    private String missionBaseUrl;

    @Override
    public Mono<VehicleConfigurationDTO> getVehicleConfigById(final String id) {
        log.debug("find vehicle configuration by id: {}", id);
        return WEB_CLIENT.get()
                .uri(vehicleConfigUrl + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleOnError)
                .onStatus(HttpStatus::is5xxServerError, this::handleOnError)
                .bodyToMono(VehicleConfigurationDTO.class);
    }

    @Override
    public Mono<MissionDTO> getMissionById(String missionId) {
        log.info("looking for mission with id: {}", missionId);
        return WEB_CLIENT.get()
                .uri(missionBaseUrl + "/" + missionId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleOnError)
                .onStatus(HttpStatus::is5xxServerError, this::handleOnError)
                .onStatus(HttpStatus::isError, this::handleOnError)
                .bodyToMono(MissionDTO.class)
                .log();
    }

    private Mono<Exception> handleOnError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .log()
                .flatMap(errorMessage -> Mono.error(new RuntimeException(errorMessage)));
    }
}
