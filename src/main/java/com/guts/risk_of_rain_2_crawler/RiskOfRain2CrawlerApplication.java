package com.guts.risk_of_rain_2_crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@ComponentScan
@EnableScheduling
public class RiskOfRain2CrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskOfRain2CrawlerApplication.class, args);
	}

}
