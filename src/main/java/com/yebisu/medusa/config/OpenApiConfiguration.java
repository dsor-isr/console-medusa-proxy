package com.yebisu.medusa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Medusa Proxy API")
                .description("Medusa proxy is the component which stores and manages the vehicle as well as stablishing the\n" +
                        "communication between web app (clients) and ist-legacy-back-end")
                .termsOfService("https://github.com/istyebisu");
        return new OpenAPI()
                .info(info);
    }
}
