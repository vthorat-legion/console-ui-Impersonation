package com.legion.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Yanming
 */
public class JsonUtil {


        public static HashMap< String,String> getPropertiesFromJsonFile(String pathname) {
            HashMap< String,String> parameterList = null;

            ObjectMapper mapper = new ObjectMapper();

            try {
                parameterList = mapper.readValue(new File(pathname),
                        new TypeReference<HashMap< String,String>>() {
                        });

            } catch (JsonGenerationException e) {
                System.err.println("The json configuration file is not valid. Please verify the file.");

            } catch (JsonMappingException  e) {
                System.err.println("The json configuration file is not valid. Please verify the file.");

            } catch (IOException e) {
                System.err.println("No configuration file available. Cannot Find file: " + pathname);
            }
            return parameterList;

        }

    public static Object[][] getArraysFromJsonFile(String pathname) {

        Object[][] objects = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            objects = mapper.readValue(new File(pathname),
                    new TypeReference<Object[][]>() {
                    });

        } catch (JsonGenerationException e) {
            System.err.println("The json configuration file is not valid. Please verify the file.");

        } catch (JsonMappingException  e) {
            System.err.println("The json configuration file is not valid. Please verify the file.");

        } catch (IOException e) {
            System.err.println("No configuration file available. Cannot Find file: " + pathname);
        }
        return objects;
    }
    
    /*
     * Added by Naval
     */
    
    public static HashMap< String,Object[][]> getCredentialsFromJsonFile(String pathname) {
        HashMap< String,Object[][]> parameterList = new HashMap< String,Object[][]>();

        ObjectMapper mapper = new ObjectMapper();

        try {
            parameterList = mapper.readValue(new File(pathname),
                    new TypeReference<HashMap< String,Object[][]>>() {
                    });

        } catch (JsonGenerationException e) {
            System.err.println("The json configuration file is not valid. Please verify the file."+pathname);

        } catch (JsonMappingException  e) {
            System.err.println("The json configuration file is not valid. Please verify the file."+pathname);

        } catch (IOException e) {
            System.err.println("No configuration file available. Cannot Find file: " + pathname);
        }
        return parameterList;

    }
    
    public static ArrayList<HashMap< String,String>> getArrayOfMapFromJsonFile(String pathname) {
    	ArrayList<HashMap< String,String>> parameterList = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            parameterList = mapper.readValue(new File(pathname),
                    new TypeReference<ArrayList<HashMap< String,String>>>() {
                    });

        } catch (JsonGenerationException e) {
            System.err.println("The json configuration file is not valid. Please verify the file.");

        } catch (JsonMappingException  e) {
            System.err.println("The json configuration file is not valid. Please verify the file.");

        } catch (IOException e) {
            System.err.println("No configuration file available. Cannot Find file: " + pathname);
        }
        return parameterList;

    }

    public static String getJsonValue(String t, String key) {
        JSONObject jsonObject = JSONObject.parseObject(t);
        String jsonValue = jsonObject.getString(key);
        return jsonValue;
    }

    public static String getJsonObjectValue(String t, String objectName, String keyString) {
        JSONObject jsonObject = JSONObject.parseObject(t);
        JSONObject jsonData = jsonObject.getJSONObject(objectName);
        String jsonValue = jsonData.getString(keyString);
        return jsonValue;
    }

    public static String[] getJsonArrayValue(String t, String objectName, String arrayName) {
        JSONObject jsonObject = JSONObject.parseObject(t);
        JSONObject jsonData = jsonObject.getJSONObject(objectName);
        JSONArray jsonArray = jsonData.getJSONArray(arrayName);

        String[] jsonNewArray = new String[20];
        int j = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
            Iterator iterator = (Iterator) arrayJSONObject.keySet();
            String key;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                jsonNewArray[j] = arrayJSONObject.getString(key);
                j++;
            }
        }
        return jsonNewArray;
    }

}
