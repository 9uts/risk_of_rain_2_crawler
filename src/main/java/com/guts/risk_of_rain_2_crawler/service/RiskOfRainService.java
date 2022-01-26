package com.guts.risk_of_rain_2_crawler.service;

import com.guts.risk_of_rain_2_crawler.dao.RiskOfRainItemMapper;
import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem.RoRField;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public List<RiskOfRainItem> selectAll() {
        return rorMapper.findAll();
    }

    public RiskOfRainItem updateChName(String name, String chName) {
        RiskOfRainItem item = rorMapper.selectById(name);
        item.setChName(chName);
        return rorMapper.updateItem(item);
    }

    public RiskOfRainItem findItemByName(String name) {
        return rorMapper.selectById(name);
    }

    public void cleanAllItems() {
        rorMapper.cleanRorDb();
    }

    public List<RiskOfRainItem> findItemByNameIfContains(String name) {
        if (containsChinese(name)) {
            return rorMapper.searchByKeyVal(RoRField.CH_NAME, name);
        } else {
            return rorMapper.searchByKeyVal(RoRField.NAME, name);
        }
    }

    private boolean containsChinese(String str) {
        if (!StringUtils.hasText(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
