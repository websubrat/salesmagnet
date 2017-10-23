package com.api.salesmagnet.salesbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class SalesbookApplication {
	
	@RequestMapping("sales/hello")
    String hello() {
        return "Hello salesbook!!";
    }

	public static void main(String[] args) {
		SpringApplication.run(SalesbookApplication.class, args);
	}
}
