package com.yebisu.medusa.proxy.rosmessage.handler;

import com.yebisu.medusa.controller.dto.Point;
import com.yebisu.medusa.proxy.rosmessage.ROSMessageProxy;
import com.yebisu.medusa.proxy.rosmessage.dto.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
    public Mono<ResponseEntity<Void>> moveVehicleTo(final String vehicleIP, final Point point) {
        final String pointStamped = "{point:{\"x\":%s,\"y\":%s}}";
        //String baseUri = "http://" + vehicleIP + vehicleMoveUri + String.format(pointStamped, point.getX(), point.getY());
        String baseUri = "http://192.168.1.248:7080/RSETWPRefgeometry_msgs/PointStamped{\"point\":{\"x\":492261.6167762757,\"y\":4290026.3945034975}}";

//        WebClient webClient = WebClient.create("http://"+vehicleIP+":7080/RSETWPRefgeometry_msgs");

        String allEncoded = UriUtils.encode("http://" + vehicleIP + ":7080/RSETWPRefgeometry_msgs/PointStamped{\"point\":{\"x\":" + point.getX() + ",\"y\":" + point.getY() + "}}",
                "UTF-8");
        String notEncoded = "http://" + vehicleIP + ":7080/RSET WPRef geometry_msgs/PointStamped {\"point\":{\"x\":" + point.getX() + ",\"y\":" + point.getY() + "}}";
        String extraUrlNoEncoding = "/PointStamped{\"point\":{\"x\":" + point.getX() + ",\"y\":" + point.getY() + "}}";
        String extraUrl = UriUtils.encode("/PointStamped{\"point\":{\"x\":" + point.getX() + ",\"y\":" + point.getY() + "}}", "UTF-8");
        String extra1Url = UriUtils.encode("/PointStamped{\"point\":{\"x\":" + point.getX(), "UTF-8");
        String extra2Url = UriUtils.encode(",\"y\":" + point.getY() + "}}", "UTF-8");
//        String extraUrl = extra1Url + extra2Url;
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
//
//http://192.168.1.249:7080/RSETWPRefgeometry_msgs%252FPointStamped%257B%2522point%2522%253A%257B%2522x%2522%253A492175.21594885114%252C%2522y%2522%253A4290642.706802326%257D%257D
//http://192.168.1.249:7080/RSETWPRefgeometry_msgs%2FPointStamped%7B%22point%22%3A%7B%22x%22%3A492463.7815146467%2C%22y%22%3A4290517.83549251%7D%7D
//http://192.168.1.249:7080/RSETWPRefgeometry_msgs%2FPointStamped%7B%22point%22%3A%7B%22x%22%3A492175.21594885114%2C%22y%22%3A4290642.706802326%7D%7D

        WebClient webClient = WebClient.builder()
                .baseUrl("http://" + vehicleIP + ":7080/RSETWPRefgeometry_msgs")
//                .uriBuilderFactory(factory)
                .filter(logRequest())
                .build();


        log.info("{}", URI.create(allEncoded));
//        HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create("http://" + vehicleIP + ":7080/RSETWPRefgeometry_msgs/PointStamped{\"point\":{\"x\":" + point.getX() + ",\"y\":" + point.getY() + "}}"))
//                    .GET()
//                    .build();
//            HttpClient.newBuilder()
//                    .build()
//                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        //http://192.168.1.249:7080/RSET WPRef geometry_msgs/PointStamped {"point":{"x":492345.0841737075,"y":4290457.33631503}}
        //http://192.168.1.249:7080/RSET WPRef geometry_msgs/PointStamped {"point":{"x":492447.83664018963,"y":4290384.839702747}}
        try {
            URL url = new URL(notEncoded);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            log.info("{}", url);
            log.info("{}", con.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }


//        return webClient.get()
//                .uri(s -> s
//                        .path("{shittyUrl}")
//                        .build(extraUrlNoEncoding))
//                .retrieve()
//                .toBodilessEntity();

        return Mono.empty();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
}
