package com.guts.risk_of_rain_2_crawler.controller;

import com.guts.risk_of_rain_2_crawler.service.RiskOfRainService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by yitian.luo on 2022/1/14.
 */
@RestController
public class DatabaseUpdater {
    @Autowired
    private RiskOfRainService rorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUpdater.class);

    @Scheduled(cron = "0 0 6 * * ?")
    public void updateDatabase() {
        LOGGER.info("Update {} item(s) at {}", rorService.updateRorDb().size(), new Date());
    }

    @PostMapping("/gameDb/update")
    public String updateGameDb() {
        return "Update " + rorService.updateRorDb().size() + " item(s) at " + new Date();
    }
}
