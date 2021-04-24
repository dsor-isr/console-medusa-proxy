package com.yebisu.medusa.proxy.rosmessage.handler;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.proxy.rosmessage.ROSMessageProxy;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    public Mono<String> moveVehicleTo(final String vehicleIP, final Point point) {
        final String pointStamped = "{point:{\"x\":%s,\"y\":%s}}";
        //String baseUri = "http://" + vehicleIP + vehicleMoveUri + String.format(pointStamped, point.getX(), point.getY());
        String baseUri = "http://192.168.1.248:7080/RSETWPRefgeometry_msgs/PointStamped{\"point\":{\"x\":492261.6167762757,\"y\":4290026.3945034975}}";
        log.info("Requesting medusa proxy with URL: {}", baseUri);

        WebClient webClient = WebClient.create("http://192.168.1.248:7080/RSETWPRefgeometry_msgs");

        return webClient.get()
                .uri(s -> s
                        .path("/PointStamped")
                        .queryParam("x", "{x}")
                        .queryParam("y", "{y}")
                        .build("point", "492261.6167762757", "4290026.3945034975"))
                .retrieve()
                .bodyToMono(String.class);
    }
}
