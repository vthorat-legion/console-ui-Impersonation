package com.legion.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
    private static Logger logger = Logger.getLogger(HttpUtil.class);
    private static PoolingHttpClientConnectionManager connectionMgr;
    private static final int MAX_TIMEOUT = 7000;

    private static RequestConfig requestConfig;

    static {
        connectionMgr = new PoolingHttpClientConnectionManager();
        connectionMgr.setMaxTotal(100);
        connectionMgr.setDefaultMaxPerRoute(connectionMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        requestConfig = configBuilder.build();
    }

    /**
     * GET request
     *
     * @param url
     * @param session    header
     * @param parameters
     * @return
     */
    public static String[] httpGet(String url, String session, Map<String, String> parameters) {
        //拼接url
        Set<String> keys = parameters.keySet();
        int mark = 1;
        for (String para : keys) {
            if (mark == 1) {
                url = url + "?" + para + "=" + parameters.get(para);
            } else {
                url = url + "&" + para + "=" + parameters.get(para);
            }
            mark++;
        }

        System.out.println("url： " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        String[] res = new String[2];
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("sessionId", session);
            httpResponse = httpClient.execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            String responseStr = null;
            if (responseCode == HttpStatus.SC_OK) {
                System.out.println("The Get request executed successfully!!!");
                responseStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

                JSONObject responseJson = JSON.parseObject(responseStr);
                System.out.println("Response Json from API： " + responseJson);

                Header[] headers = httpResponse.getAllHeaders();
                HashMap<String, String> headerMap = new HashMap<>();
                for (Header hd : headers
                ) {
                    headerMap.put(hd.getName(), hd.getValue());
                }
                System.out.println("Response Headers： " + headerMap);

            } else {
                System.out.println("The Get request Failed!!!");
                System.out.println("Status code: " + responseCode);
            }
            res = new String[]{Integer.toString(responseCode), responseStr};
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * POST request
     *
     * @param url     request url
     * @param payLoad post payload
     * @return
     */
    public static String[] httpPost(String url, Map<String, String> headers, String payLoad) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String[] res = new String[3];
        try {
            HttpPost httpPost = new HttpPost(url);
            Set<String> keys = headers.keySet();
            if (keys.size() > 0) {
                for (String key : keys) {
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(payLoad);
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("statusCode:   " + statusCode);

            String sessionValue = null;
            if (response.containsHeader("sessionid")) {
                Header[] sessionId = response.getHeaders("sessionid");
                String sessionStr = Arrays.toString(sessionId);
                sessionValue = sessionStr.substring(sessionStr.lastIndexOf(" ") + 1, sessionStr.length() - 1);
                System.out.println("sessionId:   " + sessionValue);
            }

            HttpEntity responseEntity = response.getEntity();
            String entityString = EntityUtils.toString(responseEntity);
            System.out.println("entityString:   " + entityString);
            res = new String[]{Integer.toString(statusCode), sessionValue, entityString};

            response.close();
            httpClient.close();
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * upload image/file
     *
     * @param url
     * @param filePath
     * @return String
     */
    public static String fileUploadByHttpPost(String url, String session, String filePath) {
        String requestJson = "";
        File file = new File(filePath);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("sessionId", session);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Consts.UTF_8);
            builder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName());
            HttpEntity reqEntity = builder.build();
            httpPost.setEntity(reqEntity);

            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                byte[] json = EntityUtils.toByteArray(httpEntity);
                requestJson = new String(json, "UTF-8");
                EntityUtils.consume(httpEntity);
                return requestJson;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * GET request
     *
     * @param url
     * @param session    header
     * @param parameters
     * @return
     */
    public static String[] httpGet0(String url, String session, Map<String, String> parameters) {
        //拼接url
        if(parameters != null){
            Set<String> keys = parameters.keySet();
            int mark = 1;
            for (String para : keys) {
                if (mark == 1) {
                    url = url + "?" + para + "=" + parameters.get(para);
                } else {
                    url = url + "&" + para + "=" + parameters.get(para);
                }
                mark++;
            }
        }

        System.out.println("url： " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        String[] res = new String[2];
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("sessionId", session);
            httpResponse = httpClient.execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            String responseStr = null;
            if (responseCode == HttpStatus.SC_OK) {
                System.out.println("The Get request executed successfully!!!");
                responseStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                if(!url.contains("downloadTranslations")){
                    JSONObject responseJson = JSON.parseObject(responseStr);
                    System.out.println("Response Json from API： " + responseJson);
                }else{
                    System.out.println(responseStr);
                }

                Header[] headers = httpResponse.getAllHeaders();
                HashMap<String, String> headerMap = new HashMap<>();
                for (Header hd : headers
                ) {
                    headerMap.put(hd.getName(), hd.getValue());
                }
                System.out.println("Response Headers： " + headerMap);

            } else {
                System.out.println("The Get request Failed!!!");
                System.out.println("Status code: " + responseCode);
            }
            res = new String[]{Integer.toString(responseCode), responseStr};
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * POST request
     *
     * @param url     request url
     * @return
     */
    public static String[] httpPost0(String url, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String[] res = new String[3];
        try {
            HttpPost httpPost = new HttpPost(url);
            Set<String> keys = headers.keySet();
            if (keys.size() > 0) {
                for (String key : keys) {
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("statusCode:   " + statusCode);

            String sessionValue = null;
            if (response.containsHeader("sessionid")) {
                Header[] sessionId = response.getHeaders("sessionid");
                String sessionStr = Arrays.toString(sessionId);
                sessionValue = sessionStr.substring(sessionStr.lastIndexOf(" ") + 1, sessionStr.length() - 1);
                System.out.println("sessionId:   " + sessionValue);
            }

            HttpEntity responseEntity = response.getEntity();
            String entityString = EntityUtils.toString(responseEntity);
            System.out.println("entityString:   " + entityString);
            res = new String[]{Integer.toString(statusCode), sessionValue, entityString};

            response.close();
            httpClient.close();
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
}




