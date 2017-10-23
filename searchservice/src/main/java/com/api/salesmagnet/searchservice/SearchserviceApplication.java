package com.api.salesmagnet.searchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class SearchserviceApplication {
	
	@RequestMapping("search/hello")
    String hello() {
        return "Hello search service!!";
    }

	public static void main(String[] args) {
		SpringApplication.run(SearchserviceApplication.class, args);
	}
}
