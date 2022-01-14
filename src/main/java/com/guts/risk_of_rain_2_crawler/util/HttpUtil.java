package com.guts.risk_of_rain_2_crawler.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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

    public static String httpGet(String urlString) {
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

    public static String httpPost(String urlString, Map<String, String> postBody) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(urlString);
            if(postBody.isEmpty()){
                return "";
            }
            List<NameValuePair> paramsList = new ArrayList<>();
            for (Entry<String, String> en : postBody.entrySet()) {
                String key = en.getKey();
                String value = en.getValue();
                paramsList.add(new BasicNameValuePair(key, value));
            }
            post.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String result = null;
            if(entity != null){
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
            return result;
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

    public static class TranslateUrl {
        private TranslateUrl() {
            throw new IllegalStateException("Final class : " + TranslateUrl.class);
        }

        public static final String TRANSLATE_URL = "https://openapi.youdao.com/api";

        public static final String APP_ID = "1c194f2157f1f5d5";
        public static final String APP_SECRET = "3eSTzWIV4FDQEcU1lPonx9zwQLA2567o";
    }
}
