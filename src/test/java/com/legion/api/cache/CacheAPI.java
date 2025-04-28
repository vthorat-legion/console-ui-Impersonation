package com.legion.api.cache;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.legion.api.login.LoginAPI;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;

public class CacheAPI {

    public static void refreshTemplateCache(String username, String password) {
        try {
            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);
            Response cacheResponse = given().log().all().header("sessionId", sessionId).param("cacheType", "Template").when().get(System.getProperty("env") + "legion/cache/refreshCache").then().log().all().extract().response();
            cacheResponse.then().statusCode(204);
        } catch (Exception e) {
            SimpleUtils.report("Failed to refresh the cache!");
        }
    }

    public static String getEmailVerificationURL(String username, String password) {
        Response emailVerificationResponse = null;
        try {
            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);
            emailVerificationResponse = given().log().all().header("sessionId", sessionId).when().get(System.getProperty("env")+ "legion/verificationmeta/").then().log().all().extract().response();
            emailVerificationResponse.then().statusCode(202);
           System.out.println("email verification is " + emailVerificationResponse.asString()) ;
        } catch (Exception e) {
            SimpleUtils.report("Failed to launch Email validation Api!");
        }
        return emailVerificationResponse.asString();
    }
}
