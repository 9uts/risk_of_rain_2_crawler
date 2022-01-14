package com.guts.risk_of_rain_2_crawler;

import com.guts.risk_of_rain_2_crawler.dao.RiskOfRainItemMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RiskOfRain2CrawlerApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(RiskOfRain2CrawlerApplicationTests.class);

	@Autowired
	private RiskOfRainItemMapper mapper;

	@Test
	void contextLoads() {
	}

}
