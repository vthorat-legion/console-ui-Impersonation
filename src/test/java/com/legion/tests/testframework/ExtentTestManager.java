package com.legion.tests.testframework;

import java.lang.reflect.Method;
import java.util.List;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.legion.tests.annotations.*;

public class ExtentTestManager {
	
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ExtentReports extent = ExtentReportManager.getInstance();

    public synchronized static ExtentTest getTest() {
        return extentTest.get();
    }

    public synchronized static ExtentTest createTest(String name, String description, List<String> categories) {
    	
    	ExtentTest test = extent.createTest(name, description);
        
        for (String category : categories) {
            test.assignCategory(category);
           
        }
      
        extentTest.set(test);
        return getTest();
    }
    
    public synchronized static ExtentTest createTest(String name, String description, List<String> categories, List<String> enterprises) {
    	
    	ExtentTest test = extent.createTest(name, description);
        
        for (String category : categories) {
            test.assignCategory(category);
        }
        for (String enterprise : enterprises) {
        	extent.setSystemInfo("Enterprise",enterprise);
        }
      
        extentTest.set(test);
        return getTest();
    }

    public synchronized static ExtentTest createTest(String name, String description) {
        return createTest(name, description, null,null);
    }

    public synchronized static ExtentTest createTest(String name) {
        return createTest(name, null);
    }

    public synchronized static void log(String message) {
        getTest().info(message);
    }
    
    public synchronized static void setEnterpriseInfo(String categories) {
    	 
    	extent.setSystemInfo("Environment",categories);
    }
    
    
    public synchronized static String getTestName(Method testMethod) {
		
        String testName = "";
        // check if there is a Test annotation and get the test name
        TestName testCaseDescription = testMethod.getAnnotation(TestName.class);
        if (testCaseDescription != null && testCaseDescription.description().length() > 0) {
            testName = testCaseDescription.description();
        }
        
        return testName;
    }
    
    public synchronized static String getOwnerName(Method testMethod) {
		
        String ownerName = "";
        // check if there is a Test annotation and get the test name
        Owner own = testMethod.getAnnotation(Owner.class);
        if (own != null &&  own.owner().length() > 0) {
        	ownerName =  own.owner();
        }
       
        return ownerName;
    }
    
    public synchronized static String getAutomatedName(Method testMethod) {
		
        String automatedName = "";
        // check if there is a Test annotation and get the test name
        Automated automated = testMethod.getAnnotation(Automated.class);
        if (automated != null && automated.automated().length() > 0) {
        	automatedName = automated.automated();
        }
       
        return automatedName;
    }
    
    public synchronized static String getMobilePlatformName(Method testMethod) {

        String platformName = "";
        // check if there is a Test annotation and get the test name
        MobilePlatform mobilePlatform = testMethod.getAnnotation(MobilePlatform.class);
        if (mobilePlatform != null && mobilePlatform.platform().length() > 0) {
        	platformName = mobilePlatform.platform();
        }

        return platformName;
    }


    public synchronized static int getTestRailId(Method testMethod) {

        int testRailId = 0;
        // check if there is a Test annotation and get the test name
        UseAsTestRailId useAsTestRailId = testMethod.getAnnotation(UseAsTestRailId.class);
        if(useAsTestRailId != null && useAsTestRailId.testRailId()> 0){
        	testRailId = useAsTestRailId.testRailId();
        }

        return testRailId;
    }

    public synchronized static int getTestRailSectionId(Method testMethod) {

        int testRailSectionId = 0;
        // check if there is a Test annotation and get the test rail section id
        UseAsTestRailSectionId useAsTestRailSectionId = testMethod.getAnnotation(UseAsTestRailSectionId.class);
        if(useAsTestRailSectionId != null && useAsTestRailSectionId.testRailSectionId()> 0){
            testRailSectionId = useAsTestRailSectionId.testRailSectionId();
        }

        return testRailSectionId;
    }

    public synchronized static String getTestRunPhase(Method testMethod) {

        String testRunPhaseName = "";
        // check if there is a Test annotation and get the test run phase name
        SanitySuite sanitySuite = testMethod.getAnnotation(SanitySuite.class);
        if (sanitySuite != null && sanitySuite.sanity().length() > 0) {
            testRunPhaseName = sanitySuite.sanity();
        }

        return testRunPhaseName;
    }


    public synchronized static int getTestCaseSectionId(Method testMethod) {

            int testCaseSectionId = 0;
            // check if there is a Test annotation and get the test rail section id
            UseAsTestCaseSectionId useAsTestCaseSectionId = testMethod.getAnnotation(UseAsTestCaseSectionId.class);
            if(useAsTestCaseSectionId != null && useAsTestCaseSectionId.testCaseSectionId()> 0){
                testCaseSectionId = useAsTestCaseSectionId.testCaseSectionId();
            }

        return testCaseSectionId;
    }
     
}
