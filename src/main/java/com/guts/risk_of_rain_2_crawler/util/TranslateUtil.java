package com.guts.risk_of_rain_2_crawler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guts.risk_of_rain_2_crawler.util.HttpUtil.TranslateUrl;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Created by yitian.luo on 2022/1/24.
 */
public class TranslateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TranslateUtil.class);

    private TranslateUtil(){}

    public static String translate(String str) {
        String salt = String.valueOf(System.currentTimeMillis());
        Map<String, String> params = new HashMap<>();
        params.put("from", "EN");
        params.put("to", "CH");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = TranslateUrl.APP_ID + truncate(str) + salt + curtime + TranslateUrl.APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", TranslateUrl.APP_ID);
        params.put("q", str);
        params.put("salt", salt);
        params.put("sign", sign);
        JSONObject responseBody = JSON.parseObject(HttpUtil.httpPost(TranslateUrl.TRANSLATE_URL, params));
        JSONArray translationArray = responseBody.getJSONArray("translation");
        if (translationArray.size() == 1) {
            String trans = translationArray.getString(0).trim();
            LOGGER.info("Translate success, translate to: {}", trans);
            return trans;
        }
        LOGGER.info("Translate error, original text: {}", str);
        return "";
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
