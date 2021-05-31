package com.yebisu.medusa.proxy.configserver.handler;

import com.yebisu.medusa.exception.CustomException;
import com.yebisu.medusa.exception.ResourceNotFoundException;
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

    private final WebClient webClient;
    @Value("${config-server-vehicle-config-base-uri}")
    private String vehicleConfigUrl;
    @Value("${configserver.mission.base.uri}")
    private String missionBaseUrl;

    @Override
    public Mono<VehicleConfigurationDTO> getVehicleConfigById(final String id) {
        log.debug("find vehicle configuration by id: {}", id);
        return webClient.get()
                .uri(vehicleConfigUrl + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new ResourceNotFoundException("Client exception while invoking configServer")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CustomException("Error while Invoking the configServer")))
                .bodyToMono(VehicleConfigurationDTO.class);
    }

    @Override
    public Mono<MissionDTO> getMissionById(String missionId) {
        return webClient.get()
                .uri(missionBaseUrl + "/", missionId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleOnError)
                .onStatus(HttpStatus::is5xxServerError, this::handleOnError)
                .onStatus(HttpStatus::isError, this::handleOnError)
                .bodyToMono(MissionDTO.class)
                .log();
    }

    private Mono<? extends Throwable> handleOnError(ClientResponse clientResponse) {
        Mono<String> errorMessageMono = clientResponse.bodyToMono(String.class);
        return errorMessageMono.flatMap(errorMessage -> {
            log.error("Error on webclient GET MISSION BY ID with message: {}", errorMessage);
            return Mono.error(new RuntimeException(errorMessage));
        });
    }
}
