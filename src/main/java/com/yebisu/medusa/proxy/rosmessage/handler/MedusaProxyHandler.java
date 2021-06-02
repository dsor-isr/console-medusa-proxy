package com.yebisu.medusa.proxy.rosmessage.handler;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.controller.dto.VehicleDetails;
import com.yebisu.medusa.proxy.rosmessage.MedusaRestProxy;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class ROSMessageProxyHandler implements MedusaRestProxy {

    public static final String INTERNAL_SERVER_ERROR = "Internal Server error";

    @Value("${vehicle.state.uri}")
    private String urlROSMessageState;

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
    public Mono<ResponseEntity<Void>> moveVehicleTo(final String vehicleIP, final Point point) {
        return Mono
                .fromCallable(() -> {
                    URL url = new URL("http://" + vehicleIP + ":7080/RSET%20WPRef%20geometry_msgs/PointStamped%20{\"point\":{\"x\":" + point.getX() + ",\"y\":" + point.getY() + "}}");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    con.disconnect();
                    return responseCode;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(i -> Mono.empty());
    }

    @Override
    public Mono<Void> executeMission(String missionId, List<VehicleDetails> vehicleDetails) {
        return null;
    }
}
