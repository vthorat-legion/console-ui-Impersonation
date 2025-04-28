package com.legion.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public class importFile {
    public static String importFileAPI(String sessionId, String filePath, String url) {
        String responseInfo = HttpUtil.fileUploadByHttpPost(url, sessionId, filePath);
        String responseCode = null;
        if (StringUtils.isNotBlank(responseInfo)) {
            JSONObject json = JSONObject.parseObject(responseInfo);
            if (!json.isEmpty()) {
                responseCode = json.getString("responseStatus");
                System.out.println(responseCode);
            }
        }
        return responseCode;
    }

    public static void verifyResponseCode(String actualResponseCode, String ExpectedResponseCode) {
        if (actualResponseCode.equals(ExpectedResponseCode)){
            SimpleUtils.pass("request pass");
        }else{
            SimpleUtils.fail("request failed", true);
        }
    }
}
