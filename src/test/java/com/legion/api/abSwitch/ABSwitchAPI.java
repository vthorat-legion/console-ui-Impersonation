package com.legion.api.abSwitch;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.legion.api.common.EnterpriseId;
import com.legion.api.login.LoginAPI;
import com.legion.utils.SimpleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class ABSwitchAPI {

    public static void enableABSwitch(String abSwitchName, String username, String password) {
        try {

            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);
            //get abswitch to confirm the switch is on or off
            Response response2= given().log().all().header("sessionId",sessionId).param("switchName", abSwitchName).when().get("https://rc-enterprise.dev.legion.work/legion/business/queryABSwitch").then().log().all().extract().response();
            String enabled = response2.jsonPath().get("records.enabled").toString();

            if (enabled.contains("false")) {
                HashMap<String, Object> recordContext = new HashMap<>();
                recordContext.put( "name", abSwitchName);
                recordContext.put("resource", "Business");
                recordContext.put("controlValue", "");
                recordContext.put("value", EnterpriseId.valueOf(System.getProperty("enterprise").toLowerCase().replace("-", "")).getValue());
                recordContext.put("enabled", true);
                HashMap<String, Object>  jsonAsMap = new HashMap<>();
                jsonAsMap.put("level", "Enterprise");
                jsonAsMap.put("valid", true);
                jsonAsMap.put("record",recordContext);

                Response responseAfterDisable= given().log().all().headers("sessionId",sessionId).contentType(ContentType.JSON).body(jsonAsMap)
                        .when().post(System.getProperty("env") + "legion/business/updateABSwitch").then().log().all().extract().response();
                responseAfterDisable.then().statusCode(200);

            }
        } catch (Exception e) {
            SimpleUtils.report("Failed to enable ABswitch: " + abSwitchName);
        }
    }

    public static void disableABSwitch(String abSwitchName, String username, String password) {
        try {

            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);
            //get abswitch to confirm the switch is on or off
            Response response2= given().log().all().header("sessionId",sessionId).param("switchName", abSwitchName).when().get("https://rc-enterprise.dev.legion.work/legion/business/queryABSwitch").then().log().all().extract().response();
            String enabled = response2.jsonPath().get("records.enabled").toString();

            if (enabled.contains("true")) {
                HashMap<String, Object> recordContext = new HashMap<>();
                recordContext.put( "name", abSwitchName);
                recordContext.put("resource", "Business");
                recordContext.put("controlValue", "");
                recordContext.put("value", EnterpriseId.valueOf(System.getProperty("enterprise").toLowerCase().replace("-", "")).getValue());
                recordContext.put("enabled", false);
                HashMap<String, Object>  jsonAsMap = new HashMap<>();
                jsonAsMap.put("level", "Enterprise");
                jsonAsMap.put("valid", true);
                jsonAsMap.put("record",recordContext);

                Response responseAfterDisable= given().log().all().headers("sessionId",sessionId).contentType(ContentType.JSON).body(jsonAsMap)
                        .when().post("https://rc-enterprise.dev.legion.work/legion/business/updateABSwitch").then().log().all().extract().response();
                responseAfterDisable.then().statusCode(200);

            }
        } catch (Exception e) {
            SimpleUtils.report("Failed to disable ABswitch: " + abSwitchName);
        }
    }
}
