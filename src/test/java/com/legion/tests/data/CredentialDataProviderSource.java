package com.legion.tests.data;

import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;

public class CredentialDataProviderSource {

    @DataProvider(name = "legionTeamCredentialsByEnterprise", parallel = false)
    public static Object[][] firstCredentialsByEnterprise(Method testMethod) {
        String fileName = "UsersCredentials.json";
        if (System.getProperty("enterprise")!=null && !System.getProperty("enterprise").isEmpty()) {
            //for release.
            if (System.getProperty("env")!=null && System.getProperty("env").toLowerCase().contains("rel")){
                fileName = "Release"+System.getProperty("enterprise")+fileName;
            } else {
                fileName = System.getProperty("enterprise")+fileName;
            }
        }else {
            fileName = SimpleUtils.getEnterprise(testMethod) + fileName;
        }
        TreeMap<String, Object[][]> userCredentials = new TreeMap<>(); 
        userCredentials.putAll(SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName)); 
        for(Map.Entry<String, Object[][]> entry : userCredentials.entrySet())
        {
            //return entry.getValue();
        	return SimpleUtils.concatenateObjects(browserDataProvider(testMethod), entry.getValue()) ;
        }
        return new Object[0][];
        //return Constants.DefaultInternalAdminCredentials;
    }

     @DataProvider(name = "legionTeamCredentialsByEnterpriseP", parallel = true)
     public static Object[][] firstCredentialsByEnterpriseP (Method testMethod) {
         return firstCredentialsByEnterprise(testMethod);
     }
     
     @DataProvider(name = "legionTeamCredentialsByRoles", parallel = false)
     public static Object[][] credentialsByRoles (Method testMethod) {
         String fileName = "UsersCredentials.json";
         if (System.getProperty("enterprise")!=null && !System.getProperty("enterprise").isEmpty()) {
             //for release.
             if (System.getProperty("env")!=null && System.getProperty("env").toLowerCase().contains("rel")){
                 fileName = "Release" + System.getProperty("enterprise") + fileName;
             } else if (System.getProperty("env")!=null && System.getProperty("env").toLowerCase().contains("staging")) {
                 fileName = "Staging" + System.getProperty("enterprise").toLowerCase() + fileName;
             }else {
                 fileName = System.getProperty("enterprise")+fileName;
             }
         }else {
             fileName = SimpleUtils.getEnterprise(testMethod) + fileName;
         }
         HashMap<String, Object[][]> userCredentials = SimpleUtils
        		 .getEnvironmentBasedUserCredentialsFromJson(fileName);
        	 for(Map.Entry<String, Object[][]> entry : userCredentials.entrySet())
             {
                 String testFullName = testMethod.getName();
                 String simpleClassName = "Of" + testMethod.getDeclaringClass().getSimpleName();
                 String testNameNClassName = testFullName + simpleClassName;
                 if (entry.getKey().contains(simpleClassName)) {
                     if (testNameNClassName.contains(entry.getKey())) {
                         return SimpleUtils.concatenateObjects(browserDataProvider(testMethod), entry.getValue());
                     }
                 } else {
                     if (testFullName.contains(entry.getKey())) {
                         return SimpleUtils.concatenateObjects(browserDataProvider(testMethod), entry.getValue());
                     }
                 }
             }
         
         return new Object[0][];
        //return Constants.DefaultInternalAdminCredentials;
     }

     @DataProvider(name = "legionTeamCredentialsByRolesP", parallel = true)
     public static Object[][] credentialsByRolesP (Method testMethod) {
         return credentialsByRoles(testMethod);
     }
     
     @DataProvider(name = "browsers", parallel = true)
     public synchronized static Object[][] browserDataProvider(Method testMethod) {
         return JsonUtil.getArraysFromJsonFile("src/test/resources/browsersCfg.json");
     }
}
