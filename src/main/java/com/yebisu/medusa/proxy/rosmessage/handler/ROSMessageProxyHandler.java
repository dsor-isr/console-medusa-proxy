package com.yebisu.medusa.proxy.rosmessage.handler;

import com.yebisu.medusa.exception.CustomException;
import com.yebisu.medusa.exception.ResourceNotFoundException;
import com.yebisu.medusa.proxy.configserver.dto.VehicleConfigurationDTO;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import com.yebisu.medusa.proxy.rosmessage.ROSMessageProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

@Component
@Slf4j
public class ROSMessageProxyHandler implements ROSMessageProxy {

    public static final String INTERNAL_SERVER_ERROR = "Internal Server error";

    @Value("${vehicle.state.uri}")
    private String urlROSMessageState;

    @Value("${vehicle.move.uri}")
    private String vehicleMoveUri;

    @Override
    public Content pingForROSMessageState(final String ip) {
        return invokeROSMessage(ip);
    }

    private Content invokeROSMessage(String ip) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .version(HttpClient.Version.HTTP_2)
                .build();

        String uri = "http://" + ip + urlROSMessageState;
        log.debug("The URI: {}",uri);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> httpResponse;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            log.debug(httpResponse.body());
        } catch (IOException | InterruptedException exception) {
            log.error("An error occurred while invoking the ROSMessage in the proxy. " +
                    "Please certify that vehicle IP is correct or if medusa launcher is running on VM");
            exception.printStackTrace();
            throw new IllegalStateException(INTERNAL_SERVER_ERROR);
        }

        Path tempFile;
        try {
            tempFile = Files.createTempFile("httpResponse", "xml");
            Files.write(tempFile, httpResponse.body().getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            log.error("An error occurred while trying to create a temp file");
            exception.printStackTrace();
            throw new IllegalStateException(INTERNAL_SERVER_ERROR);
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Content.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Content) jaxbUnmarshaller.unmarshal(tempFile.toFile());
        } catch (JAXBException jaxbException) {
            log.error("An error occurred while trying to create a temp file");
            jaxbException.printStackTrace();
            throw new IllegalStateException(INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public Mono<String> moveVehicleTo(String vehicleIP, String coordinates) {
        String baseUri = "http://" + vehicleIP + vehicleMoveUri.concat(coordinates);
              return  WebClient.create(baseUri)
                .get()
                .uri("/")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new ResourceNotFoundException("Client exception while invoking configServer")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new CustomException("Error while Invoking the configServer")))
                .bodyToMono(String.class);
    }
}
