package com.api.salesmagnet.orderbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class OrderbookApplication {
	
	@RequestMapping("order/hello")
    String hello() {
        return "Hello Orderbook!!";
    }

	public static void main(String[] args) {
		SpringApplication.run(OrderbookApplication.class, args);
	}
}
