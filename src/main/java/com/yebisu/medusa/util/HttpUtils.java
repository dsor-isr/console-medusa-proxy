package com.yebisu.medusa.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@UtilityClass
@Slf4j
public class HttpUtils {
    public static final String SPACE = "%20";
    public static final String LINE = "LINE";
    public static final String ARC = "ARC";
    public static final String MISSION_BASE_URI = "RSET%20Mission_String%20std_msgs/String%20%7Bdata:%20\"3%09%09491967.881%204290859.308%2029S%09%09";

    public static Object supplyWithHttpConnector(String composedURI) throws IOException {
        final var url = new URL(composedURI);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try {
            final int responseCode = con.getResponseCode();
            log.info("Invocation with params: has a response code : {} {}", composedURI, responseCode);
            return responseCode;
        } catch (Exception e) {
            log.error(String.format("Invocation with params: %s has completed with message %s {}", composedURI, e));
            return Mono.error(e);
        } finally {
            con.disconnect();
        }
    }
}
