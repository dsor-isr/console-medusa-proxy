package com.yebisu.medusa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class MedusaProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedusaProxyApplication.class, args);
	}

}
