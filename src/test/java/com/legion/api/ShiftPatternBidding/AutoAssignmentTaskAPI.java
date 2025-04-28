package com.legion.api.ShiftPatternBidding;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.legion.api.login.LoginAPI;
import com.legion.api.schedule.cost.ScheduleCostContainer;
import com.legion.api.schedule.cost.ScheduleCostData;
import com.legion.utils.SimpleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class AutoAssignmentTaskAPI {
    private static Response response;

    public static void runAutoAssignmentTaskAPI(String username, String password) {
        try {
            String sessionId = LoginAPI.getSessionIdFromLoginAPI(username, password);
            Response responseAfterDisable  = given().log().all().header("sessionId", sessionId).when().get(System.getProperty("env")+ "legion/schedule/bidding/runAutoAssignTask").then().log().all().extract().response();
            responseAfterDisable.then().statusCode(200);
        } catch (Exception e) {
            SimpleUtils.fail("Got exception when getting the response data: " + e.getMessage(), false);
        }
    }
}
