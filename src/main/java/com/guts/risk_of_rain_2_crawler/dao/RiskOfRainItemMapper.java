package com.guts.risk_of_rain_2_crawler.dao;

import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil.RiskOfRainUrl;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Created by yitian.luo on 2022/1/13.
 */
public class RiskOfRainItemMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RiskOfRainItemMapper.class);

    public List<RiskOfRainItem> getFullItemsFromMoeGirlWiki() {
        String moeItemsHtml = HttpUtil.getHtml(RiskOfRainUrl.getUrl(RiskOfRainUrl.ITEMS));
        if (moeItemsHtml.isEmpty()) {
            LOGGER.info("Risk of rain items pages missing");
            return new LinkedList<>();
        }
        List<RiskOfRainItem> items = new LinkedList<>();
        Document doc = Jsoup.parse(moeItemsHtml);
        Elements tables = doc.getElementsByClass("wikitable");
        if (tables.isEmpty()) {
            LOGGER.info("Table is Blank");
            return new LinkedList<>();
        }
        for (Element table : tables) {
            for (Element tbody : table.children()) {
                for (int i = 1; i < tbody.childrenSize(); i++) {
                    List<String> itemAttr = new LinkedList<>();
                    for (Element td : tbody.child(i).children()) {
                        itemAttr.add(td.text());
                    }
                    items.add(new RiskOfRainItem(itemAttr.get(1), itemAttr.get(2)));
                }
            }
        }
        return items;
    }
}
