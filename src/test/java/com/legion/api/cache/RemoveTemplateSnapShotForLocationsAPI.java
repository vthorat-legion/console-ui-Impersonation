package com.legion.api.cache;

import com.jayway.restassured.response.Response;
import com.legion.api.login.LoginAPI;
import com.legion.tests.TestBase;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import static com.jayway.restassured.RestAssured.given;

public class RemoveTemplateSnapShotForLocationsAPI {

    public static void removeTemplateSnapShotForLocationsAPI(String username, String password) {
        try {
            HashMap<String, String> locationBusinessID = JsonUtil.getPropertiesFromJsonFile("src/test/resources/LocationBusinessID.json");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
//            Date date = sdf.parse("2001-01-10");
            Date date = new Date();
            cal.setTime(date);

            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayOfWeek -14);
            date = cal.getTime();

            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);
            Set<String> keySet = locationBusinessID.keySet();
            for (String key: keySet) {
                cal.setTime(date);
                String businessId = locationBusinessID.get(key);
                for (int i=0; i<4; i++) {
                    cal.add(Calendar.DATE, 7);
                    int weekStartDayOfTheYear = cal.get(Calendar.DAY_OF_YEAR);
                    int currentYear = Integer.parseInt(sdf.format(cal.getTime()).substring(0, 4));
                    Response cacheResponse = given().log().all()
                            .header("sessionId", sessionId)
                            .param("year", currentYear)
                            .param("weekStartDayOfTheYear",weekStartDayOfTheYear)
                            .param("businessId",businessId)
                            .when().get(System.getProperty("env") + "legion/configTemplate/removeTemplateSnapShotForLocations")
                            .then().log().all().extract().response();
                    cacheResponse.then().statusCode(200);
                }
            }
        } catch (Exception e) {
            SimpleUtils.report("Failed to refresh the cache!");
        }
    }
}
