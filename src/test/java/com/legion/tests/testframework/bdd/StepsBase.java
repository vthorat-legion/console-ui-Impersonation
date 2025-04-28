package com.legion.tests.testframework.bdd;

import com.aventstack.extentreports.Status;
import com.legion.tests.TestBase;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.tests.testframework.PropertyMap;

import java.lang.reflect.Method;

import static com.legion.utils.MyThreadLocal.*;
import static com.legion.utils.MyThreadLocal.setURL;

public abstract class StepsBase extends TestBase {

    public static void goEnterprisePage(String companyName){
        setEnvironment(PropertyMap.get("ENVIRONMENT"));
        setEnterprise(companyName);
        switch (getEnvironment()){
            case "QA":
                setURL(PropertyMap.get("QAURL"));
                loadURL();
                break;
            case "DEV":
                setURL(PropertyMap.get("DEVURL"));
                loadURL();
                break;
            default:
                ExtentTestManager.getTest().log(Status.FAIL,"Unable to set the URL");
        }
    }


    @Override
    public void firstTest(Method testMethod, Object[] params) throws Exception {

    }
}
