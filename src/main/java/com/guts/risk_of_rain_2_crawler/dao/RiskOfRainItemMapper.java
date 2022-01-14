package com.guts.risk_of_rain_2_crawler.dao;

import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil.RiskOfRainUrl;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Created by yitian.luo on 2022/1/13.
 */
@Repository
public class RiskOfRainItemMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RiskOfRainItemMapper.class);

    private static final String ROR_COLLECTION = "risk_of_rain";

    @Autowired
    private MongoTemplate rorTemplate;

    public List<RiskOfRainItem> getFullItemsFromWiki() {
        String moeItemsHtml = HttpUtil.getHtml(RiskOfRainUrl.ITEM_LIST_URL);
        if (moeItemsHtml.isEmpty()) {
            LOGGER.info("Risk of rain items pages missing");
            return new LinkedList<>();
        }
        List<RiskOfRainItem> items = new LinkedList<>();
        Document doc = Jsoup.parse(moeItemsHtml);
        Elements tables = doc.getElementsByClass("article-table sortable firstcolumn-center-nowrap floatheader");
        if (tables.isEmpty()) {
            LOGGER.info("Table is Blank");
            return new LinkedList<>();
        }
        for (Element table : tables) {
            for (Element tbody : table.children()) {
                for (int i = 1; i < tbody.childrenSize(); i++) {
                    Element tr = tbody.child(i);
                    items.add(new RiskOfRainItem(tr.child(0).attributes().get("data-sort-value"), tr.child(1).text()));
                }
            }
        }
        return items;
    }

    public void updateItem(RiskOfRainItem item) {
        LOGGER.info("Update {}", rorTemplate.save(item, ROR_COLLECTION));
    }

    public List<RiskOfRainItem> updateAllItem(List<RiskOfRainItem> items) {
        for (RiskOfRainItem item : items) {
            updateItem(item);
        }
        return items;
    }

    public RiskOfRainItem selectById(String id) {
        RiskOfRainItem item = rorTemplate.findById(id, RiskOfRainItem.class, ROR_COLLECTION);
        LOGGER.info("Find {}", item);
        return item;
    }

    public List<RiskOfRainItem> selectByIdIfContains(String id) {
        Pattern pattern = Pattern.compile("^.*" + id + ".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("_id").regex(pattern));
        List<RiskOfRainItem> items = rorTemplate.find(query, RiskOfRainItem.class, ROR_COLLECTION);
        LOGGER.info("Find {} item(s)", items.size());
        if (items.size() < 10) {
            items.forEach(item -> LOGGER.info("Find: {}", item));
        }
        return items;
    }

    public void cleanRorDb() {
        rorTemplate.findAll(RiskOfRainItem.class, ROR_COLLECTION).forEach(item -> {
            Criteria criteria = Criteria.where("_id").is(item.getName());
            Query query = new Query(criteria);
            LOGGER.info("Delete {}: {}", item.getName(), rorTemplate.remove(query, ROR_COLLECTION).getDeletedCount() != 0);
        });
    }
}
