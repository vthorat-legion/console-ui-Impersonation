package com.legion.tests.testframework;

import com.legion.utils.JsonUtil;

import java.util.HashMap;

public class PropertyMap {

    private static HashMap<String, String> propertyMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");

    public static String get(String key){
        return propertyMap.get(key);
    }
}
