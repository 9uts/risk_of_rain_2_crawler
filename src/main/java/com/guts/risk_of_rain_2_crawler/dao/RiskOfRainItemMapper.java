package com.guts.risk_of_rain_2_crawler.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guts.risk_of_rain_2_crawler.entity.RiskOfRainItem;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil.RiskOfRainUrl;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil.TranslateUrl;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
        String moeItemsHtml = HttpUtil.httpGet(RiskOfRainUrl.ITEM_LIST_URL);
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
        // 翻译产生中文版描述
        translateEachItem(items);
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

    public static void main(String[] args) {
        RiskOfRainItemMapper rorMapper = new RiskOfRainItemMapper();
        rorMapper.translateEachItem(rorMapper.getFullItemsFromWiki());
    }

    public void translateEachItem(List<RiskOfRainItem> items) {
        items.forEach(item -> {
            String q = item.getDescription();
            String salt = String.valueOf(System.currentTimeMillis());
            Map<String, String> params = new HashMap<>();
            params.put("from", "EN");
            params.put("to", "CH");
            params.put("signType", "v3");
            String curtime = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("curtime", curtime);
            String signStr = TranslateUrl.APP_ID + truncate(q) + salt + curtime + TranslateUrl.APP_SECRET;
            String sign = getDigest(signStr);
            params.put("appKey", TranslateUrl.APP_ID);
            params.put("q", q);
            params.put("salt", salt);
            params.put("sign", sign);
            JSONObject responseBody = JSON.parseObject(HttpUtil.httpPost(TranslateUrl.TRANSLATE_URL, params));
            JSONArray translationArray = responseBody.getJSONArray("translation");
            if (translationArray.size() == 1) {
                item.setDescriptionCh(translationArray.getString(0));
                LOGGER.info("Translate {} success, chinese description: {}", item.getName(), item.getDescriptionCh());
            } else {
                LOGGER.info("Translate {} error: {}", item.getName(), item.getDescription());
            }
        });
    }

    private static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    private static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
