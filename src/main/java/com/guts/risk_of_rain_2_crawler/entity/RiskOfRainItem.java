package com.guts.risk_of_rain_2_crawler.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * @author Created by yitian.luo on 2022/1/13.
 */
@Data
@ToString
@Accessors(chain = true)
public class RiskOfRainItem {
    @Id
    private final String name;
    private final String description;
    private String descriptionCh;

    public RiskOfRainItem(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
