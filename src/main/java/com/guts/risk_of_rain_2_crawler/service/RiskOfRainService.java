package com.guts.risk_of_rain_2_crawler.service;

import com.guts.risk_of_rain_2_crawler.dao.RiskOfRainItemMapper;
import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Created by yitian.luo on 2022/1/14.
 */
@Service
public class RiskOfRainService {

    @Autowired
    private RiskOfRainItemMapper rorMapper;

    public List<RiskOfRainItem> updateRorDb() {
        List<RiskOfRainItem> items = rorMapper.getFullItemsFromWiki();
        return rorMapper.updateAllItem(items);
    }

    public RiskOfRainItem findItemByName(String name) {
        return rorMapper.selectById(name);
    }

    public void cleanAllItems() {
        rorMapper.cleanRorDb();
    }

    public List<RiskOfRainItem> findItemByNameIfContains(String name) {
        return rorMapper.selectByIdIfContains(name);
    }
}
