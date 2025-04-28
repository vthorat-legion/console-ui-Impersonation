package com.legion.api.schedule.cost;

import com.google.gson.JsonObject;
import com.jayway.restassured.response.Response;
import com.legion.api.login.LoginAPI;
import com.legion.utils.SimpleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class ScheduleCostAPI {

    private static Response response;

    public static ScheduleCostContainer getScheduleCost(String username, String password, int weekStartDayOfTheYear, String businessId, String year) {
        ScheduleCostContainer scheduleCostContainer = null;
        try {

            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);
            response = given().log().all().header("sessionId",sessionId)
                    .param("businessId", businessId)
                    .param("year", year)
                    .param("weekStartDayOfTheYear", weekStartDayOfTheYear)
                    .when().get(System.getProperty("env") + "legion/schedule/cost").then().log().all().extract().response();

            HashMap<String, String> weekData = response.jsonPath().get("weekData");
            List<HashMap<String, String>> dayData = response.jsonPath().getList("dayData");
            List<String> scheduleCostData = response.jsonPath().getList("scheduleCostData");
            List<ScheduleCostData> scheduleCostDataList = new ArrayList<>();
            if (scheduleCostData != null && scheduleCostData.size() > 0) {
                for (int i = 0; i < scheduleCostData.size(); i++) {
                    HashMap<String, Integer> currentDayMinutes = response.jsonPath()
                            .get("scheduleCostData[" + i + "].scheduleCostDataRecord.workMinutes.currentDayMinutes");
                    String totalWagePerTM = response.jsonPath()
                            .get("scheduleCostData[" + i + "].scheduleCostDataRecord.dataContainer.wages.totalWages").toString();
                    List<HashMap<String, String>> wagesBreakDownList = response.jsonPath()
                            .getList("scheduleCostData[" + i + "].scheduleCostDataRecord.dataContainer.wages.wageBreakDownList");
                    List<HashMap<String, String>> premiumsList = response.jsonPath()
                            .getList("scheduleCostData[" + i + "].scheduleCostDataRecord.dataContainer.workedLocations[0].premiums");
                    int dayOfTheYear = response.jsonPath()
                            .get("scheduleCostData[" + i + "].timesheetKey.yearDayOfTheYear.dayOfTheYear");
                    scheduleCostDataList.add(new ScheduleCostData(currentDayMinutes, totalWagePerTM, wagesBreakDownList,
                            premiumsList, dayOfTheYear));
                }
            }
            scheduleCostContainer = new ScheduleCostContainer(weekData, dayData, scheduleCostDataList);
        } catch (Exception e) {
            SimpleUtils.fail("Got exception when getting the response data: " + e.getMessage(), false);
        }
        return scheduleCostContainer;
    }
}
