package com.guts.risk_of_rain_2_crawler;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RiskOfRain2CrawlerApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(RiskOfRain2CrawlerApplicationTests.class);
	@Test
	void contextLoads() {
		LOGGER.info("This is a test class");
		assert true : "This is a test class";
	}

}
