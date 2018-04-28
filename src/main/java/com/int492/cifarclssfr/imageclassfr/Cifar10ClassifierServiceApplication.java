package com.int492.cifarclssfr.imageclassfr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Cifar10ClassifierServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(Cifar10ClassifierServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Cifar10ClassifierServiceApplication.class, args);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForLocation("http://127.0.0.1:8081/init", null);
		logger.info("Initiated...");
	}
}
