package com.legion.utils;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class LegionRestAPI {

	public static JSONObject getLegionAPIResponse(String requestURL, Map<String, Object> requestParams)
	{
		JSONObject legionResponse = new JSONObject();
		try {
			RestAssured.baseURI = requestURL;
			RequestSpecification request = RestAssured.given();
			request.queryParams(requestParams);
			Response response = request.get();
			int statusCode = response.getStatusCode();
			SimpleUtils.assertOnFail( response.body().asString() , (statusCode == 200), true);
			JSONParser parser = new JSONParser();
			legionResponse = (JSONObject) parser.parse(response.body().asString());
		}
		catch (ParseException parseException) {
			System.err.println(parseException.getMessage());
			SimpleUtils.fail(parseException.getMessage(), true);
		}
		return legionResponse;
	}
	
}
