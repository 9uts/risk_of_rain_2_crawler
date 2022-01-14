package com.guts.risk_of_rain_2_crawler.util;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Created by yitian.luo on 2022/1/13.
 */
public class HttpUtil {
    
    private HttpUtil() {
        throw new IllegalStateException("Util class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static String getHtml(String urlString) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlString);
            CloseableHttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                LOGGER.info("Get html file success, which url is {}", urlString);
                return EntityUtils.toString(entity, "utf8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("Fail to get html file of url = {}", urlString);
        }
        return "";
    }

    public static class RiskOfRainUrl {
        private RiskOfRainUrl() {
            throw new IllegalStateException("Finial class : " + RiskOfRainUrl.class);
        }
        public static final String ITEM_LIST_URL = "https://riskofrain2.fandom.com/wiki/Items";

    }
}
