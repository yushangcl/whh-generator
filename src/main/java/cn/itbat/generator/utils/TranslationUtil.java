package cn.itbat.generator.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huahui.wu
 * @date 2020年11月19日 09:19:38
 */
@Slf4j
public class TranslationUtil {

    private static final String YOU_DAO_URL = "https://openapi.youdao.com/api";

    private static final String APP_KEY = "";

    private static final String APP_SECRET = "";

    public static String translation(String str) {
        // 处理结果
        try {
            return requestForHttp(YOU_DAO_URL, assemblyParameters(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Map<String, String> assemblyParameters(String str) {
        Map<String, String> params = new HashMap<String, String>();
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "源语言");
        params.put("to", "目标语言");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(str) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", str);
        params.put("salt", salt);
        params.put("sign", sign);
        return params;
    }

    public static String requestForHttp(String url, Map<String, String> params) throws IOException {

        // 创建HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // httpPost
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> en : params.entrySet()) {
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key, value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            log.info("Content-Type:" + contentType[0].getValue());
            if ("audio/mp3".equals(contentType[0].getValue())) {
                //如果响应是wav
                HttpEntity httpEntity = httpResponse.getEntity();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(baos);
                byte[] result = baos.toByteArray();
                EntityUtils.consume(httpEntity);
                //合成成功
                String file = "合成的音频存储路径" + System.currentTimeMillis() + ".mp3";
                byte2File(result, file);
                return file;
            } else {
                //响应不是音频流，直接显示结果
                HttpEntity httpEntity = httpResponse.getEntity();
                String json = EntityUtils.toString(httpEntity, "UTF-8");
                EntityUtils.consume(httpEntity);
                JSONObject jsonObject = JSONObject.parseObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("translation");
                System.out.println("翻译文字：" + params.get("q") + "；翻译结果：" + jsonObject.getJSONArray("translation"));
                return jsonArray.getString(0);
            }
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                log.info("## release resouce error ##" + e);
            }
        }
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
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

    /**
     * @param result 音频字节流
     * @param file   存储路径
     */
    private static void byte2File(byte[] result, String file) {
        File audioFile = new File(file);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        } catch (Exception e) {
            log.info(e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
