package com.guts.risk_of_rain_2_crawler.controller;

import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import com.guts.risk_of_rain_2_crawler.service.RiskOfRainService;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by yitian.luo on 2022/1/14.
 */
@RestController
@RequestMapping("gameDb")
public class DatabaseManager {
    @Autowired
    private RiskOfRainService rorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);

    @Scheduled(cron = "0 0 6 * * ?")
    public void updateDatabase() {
        LOGGER.info("Update {} item(s) at {}", rorService.updateRorDb().size(), new Date());
    }

    @PostMapping("/updateAll")
    public String updateGameDb() {
        return "Update " + rorService.updateRorDb().size() + " item(s) at " + new Date();
    }

    @GetMapping("/allItems")
    public List<RiskOfRainItem> listAllItem() {
        return rorService.selectAll();
    }

    @PostMapping("/chName")
    public RiskOfRainItem updateChName(String name, String chName) {
        LOGGER.info("Update item: {} -> {}", name, chName);
        return rorService.updateChName(name, chName);
    }
}
