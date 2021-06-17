package com.yebisu.medusa.proxy.rosmessage.handler;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.proxy.rosmessage.MedusaRestProxy;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import com.yebisu.medusa.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
import java.time.LocalDateTime;
import java.util.Comparator;

@Component
@Slf4j
public class MedusaProxyHandler implements MedusaRestProxy {

    public static final String INTERNAL_SERVER_ERROR = "Internal Server error";


    @Value("${vehicle.state.uri}")
    private String urlROSMessageState;

    @Override
    public Content getVehicleState(final String ip) {
        return invokeROSMessage(ip);
    }

    private Content invokeROSMessage(String ip) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .version(HttpClient.Version.HTTP_2)
                .build();

        String uri = "http://" + ip + urlROSMessageState;
        log.debug("The URI: {}", uri);
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
            tempFile = Files.createTempFile(LocalDateTime.now().toString(), "xml");
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

    /**
     * This method is responsible to make a vehicle move to.
     * a specified place.
     *
     * @param vehicleIP the the vehicle IP address which can be provided by
     *                  configuration server microservice.
     *                  may not be {@code blank}
     * @param point     the which represents the coordinates X or Y
     *                  may not be {@code null}
     *                  <p>
     *                  In order to build the uri its more convenient to use
     *                  the MessageFormat instead of String.format but the
     *                  uri itself is not self described
     * @return Returns a string whose value is an OK or NOT OK from medusa
     */

    @Override
    public Mono<ResponseEntity<Void>> moveVehicleTo(final String vehicleIP, final Point point) {
        final var urlConnection = "http://" + vehicleIP + ":7080/RSET%20/controls/send_wp_standard%20geometry_msgs/PointStamped%20{\"point\":{\"x\":" + point.getX() + ",\"y\":" + point.getY() + "}}";
        return Mono.fromCallable(() -> HttpUtils.supplyWithHttpConnector(urlConnection))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(i -> Mono.empty());
    }

    @Override
    public Flux<Void> executeMission(final String coordinates, Flux<String> vehiclesIPs) {
        return Flux.from(vehiclesIPs)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .filter(vehicleIP -> !vehicleIP.isEmpty())
                .map(vehicleIP -> composeURL(vehicleIP, coordinates))
                .flatMap(composedURL -> invokeMedusaServer(composedURL)
                        .onErrorContinue((t, o) -> log.error("Skipped error: {}", t.getMessage())))
                .ordered(Comparator.comparing(ResponseEntity::getStatusCode))
                .flatMap(voidResponseEntity -> Flux.empty());
    }

    private String composeURL(String vehicleIP, String coordinates) {
        var composedURI = "http://" + vehicleIP + ":7080/" + coordinates;
        log.info("Invoking mission execute with URL: {}", composedURI);
        return composedURI;
    }

    private Mono<ResponseEntity<Void>> invokeMedusaServer(final String composedURI) {
        return Mono.fromCallable(() -> HttpUtils.supplyWithHttpConnector(composedURI))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(i -> Mono.empty());
    }


}
