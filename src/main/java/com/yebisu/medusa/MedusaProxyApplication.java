package com.yebisu.medusa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableMongoAuditing
@EnableTransactionManagement
public class MedusaProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedusaProxyApplication.class, args);
    }

}
