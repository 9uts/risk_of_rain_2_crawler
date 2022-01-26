package com.guts.risk_of_rain_2_crawler.controller;

import com.alibaba.fastjson.JSONObject;
import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import com.guts.risk_of_rain_2_crawler.service.RiskOfRainService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by yitian.luo on 2022/1/14.
 */
@RestController
@RequestMapping("rorItem")
public class RiskOfRainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RiskOfRainController.class);

    @Autowired
    private RiskOfRainService rorService;

    @GetMapping("/items")
    public JSONObject getDescriptionsWhichContains(String name) {
        JSONObject response = new JSONObject();
        LOGGER.info("Start searching : {}", name);
        List<RiskOfRainItem> items = rorService.findItemByNameIfContains(name);
        final String status = "status";
        final String message = "message";
        final String data = "data";
        if (items.isEmpty()) {
            response.put(status, -1);
            response.put(message, "未查询到数据。");
            response.put(data, null);
            return response;
        } else if (items.size() > 10) {
            response.put(status, -2);
            response.put(message, "查询到的数据过多，请提供更多信息。");
            response.put(data, null);
            return response;
        }
        response.put(status, 1);
        response.put(message, "查询成功。");
        response.put(data, items);
        LOGGER.info("Response: {}", response);
        return response;
    }


}
