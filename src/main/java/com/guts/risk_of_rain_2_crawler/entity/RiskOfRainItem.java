package com.guts.risk_of_rain_2_crawler.entity;

/**
 * @author Created by yitian.luo on 2022/1/13.
 */
public class RiskOfRainItem {
    private String name;
    private String description;

    public RiskOfRainItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
