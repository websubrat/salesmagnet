package com.api.salesmagnet.appgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableZuulProxy
@RestController
public class AppgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppgatewayApplication.class, args);
	}
}
