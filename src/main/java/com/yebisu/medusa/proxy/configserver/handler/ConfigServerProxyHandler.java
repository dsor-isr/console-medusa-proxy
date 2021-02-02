package com.yebisu.medusa.proxy.configserver.handler;

import com.yebisu.medusa.proxy.configserver.ConfigServerProxy;
import com.yebisu.medusa.proxy.configserver.dto.VehicleConfigurationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ConfigServerProxyHandler implements ConfigServerProxy {

    @Value("${config-server-vehicle-config-base-uri}")
    private String vehicleConfigUrl;

    @Override
    public Mono<VehicleConfigurationDTO> getVehicleConfigById(final String id) {
        WebClient webClient = WebClient.create(vehicleConfigUrl);
        return webClient.get()
                .uri("/configuration/" + id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(VehicleConfigurationDTO.class));
    }
}
