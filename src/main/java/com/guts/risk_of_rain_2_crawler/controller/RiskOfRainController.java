package com.guts.risk_of_rain_2_crawler.controller;

import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import com.guts.risk_of_rain_2_crawler.service.RiskOfRainService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by yitian.luo on 2022/1/14.
 */
@RestController
public class RiskOfRainController {

    @Autowired
    private RiskOfRainService rorService;

    @GetMapping("/itemDescription")
    public String getDescription(String name) {
        return rorService.findItemByName(name).getDescription();
    }

    @GetMapping("/items")
    public String getDescriptionsWhichContains(String name) {
        List<RiskOfRainItem> items = rorService.findItemByNameIfContains(name);
        StringBuilder builder = new StringBuilder("道具"+name+"搜索结果:\n");
        if (items.isEmpty()) {
            return "未搜索到道具。";
        }
        builder.append("------------------\n");
        for (RiskOfRainItem item : items) {
            builder.append("道具名称: ")
                    .append(item.getName())
                    .append("\n> ")
                    .append(item.getDescriptionCh())
                    .append("\n> ")
                    .append(item.getDescription())
                    .append("\n------------------\n");
        }
        return builder.toString();
    }
}
