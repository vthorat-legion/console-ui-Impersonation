package com.legion.api.toggle;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.legion.api.login.LoginAPI;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;

import java.util.*;

import static com.jayway.restassured.RestAssured.given;

public class ToggleAPI {

    public static void updateToggle(String toggleName, String username, String password, boolean isTurnOn) {
        try {
            String enterpriseName = System.getProperty("enterprise");
            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);

            List<HashMap> rules = new ArrayList<>();
            // Get the current enterprise names which have specific toggle turned on
            List<String> enterpriseNames = new ArrayList<>(getCurrentEnabledEnterprises(toggleName));
            if (isTurnOn) {
                if (!enterpriseNames.contains(enterpriseName.toLowerCase())) {
                    enterpriseNames.add(enterpriseName);
                }
            } else {
                if (enterpriseNames.contains(enterpriseName.toLowerCase())) {
                    for (int i = 0; i < enterpriseNames.size(); i++) {
                        if (enterpriseNames.size() > 1 && enterpriseNames.get(i).equalsIgnoreCase(enterpriseName)) {
                            enterpriseNames.remove(i);
                            break;
                        }
                        if (enterpriseNames.size() == 1 && enterpriseNames.get(i).equalsIgnoreCase(enterpriseName)) {
                            enterpriseNames = new ArrayList<>();
                            break;
                        }
                    }
                }
            }
            if (enterpriseNames != null && enterpriseNames.size() > 0) {
                String enterpriseNameForAPI = "";
                for (int i = 0; i < enterpriseNames.size(); i++) {
                    if (i != enterpriseNames.size() - 1) {
                        enterpriseNameForAPI = enterpriseNameForAPI + enterpriseNames.get(i) + "\",\"";
                    } else {
                        enterpriseNameForAPI = enterpriseNameForAPI + enterpriseNames.get(i);
                    }
                }
                HashMap<String, Object> rulesValue = new HashMap<>();
                rulesValue.put("enterpriseName", enterpriseNameForAPI);
                rules.add(rulesValue);
            } else {
                rules = new ArrayList<>();
            }

            HashMap<String, Object> recordContext = new HashMap<>();
            recordContext.put("name", toggleName);
            recordContext.put("defaultValue", false);
            recordContext.put("rules", rules);
            HashMap<String, Object> jsonAsMap = new HashMap<>();
            jsonAsMap.put("level", "Enterprise");
            jsonAsMap.put("valid", true);
            jsonAsMap.put("record", recordContext);

            Response responseAfterDisable = given().log().all().headers("sessionId", sessionId).contentType(ContentType.JSON).body(jsonAsMap)
                    .when().post(System.getProperty("env") + "legion/toggles").then().log().all().extract().response();
            responseAfterDisable.then().statusCode(200);
        } catch (Exception e) {
            SimpleUtils.report("Failed to enable toggle: " + toggleName);
        }
    }

    private static List<String> getCurrentEnabledEnterprises(String toggleName) {
        List<String> enterpriseNames = new ArrayList<>();
        String fileName = "Toggles.json";
        try {
            String env = System.getProperty("env");
            if (env != null && !env.isEmpty()) {
                if (env.toLowerCase().contains("rc") || env.toLowerCase().contains("ephemeral")) {
                    fileName = "RC" + fileName;
                } else if (env.toLowerCase().contains("rel")) {
                    fileName = "Release" + fileName;
                }
                HashMap<String, String> toggleNEnterprises = JsonUtil.getPropertiesFromJsonFile("src/test/java/com/legion/api/" + fileName);
                String enterprises = toggleNEnterprises.get(toggleName);
                if (enterprises != null && !enterprises.isEmpty()) {
                    if (enterprises.contains(",")) {
                        String[] nameList = enterprises.split(",");
                        Collections.addAll(enterpriseNames, nameList);
                    } else {
                        enterpriseNames.add(enterprises);
                    }
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
        return enterpriseNames;
    }
}
