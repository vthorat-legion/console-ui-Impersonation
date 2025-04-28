package com.legion.test.testrail;

import com.legion.tests.TestBase;
import com.legion.tests.testframework.ScreenshotManager;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestRailOperation {
    static HashMap<String,String> testRailCfgSch = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg_SCH.json");
    static HashMap<String,String> testRailCfgOp = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg_OP.json");
    static HashMap<String,String> testRailCfgElm = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg_ELM.json");

    public static String getTestRailURL(){
        String url = "";
        if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("op")) {
            url = testRailCfgOp.get("TEST_RAIL_URL");
        } else if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("sch")) {
            url = testRailCfgSch.get("TEST_RAIL_URL");
        } else if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("elm")){
            url = testRailCfgElm.get("TEST_RAIL_URL");
        }
        return url;
    }
    public static String getTestRailUser(){
        if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("op")) {
            return testRailCfgOp.get("TEST_RAIL_USER");
        } else if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("sch")) {
            return testRailCfgSch.get("TEST_RAIL_USER");
        } else if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("elm")) {
            return testRailCfgElm.get("TEST_RAIL_USER");
        } else {
            return null;
        }
    }
    public static String getTestRailPassword(){
        if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("op")) {
            return testRailCfgOp.get("TEST_RAIL_PASSWORD");
        } else if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("sch")) {
            return testRailCfgSch.get("TEST_RAIL_PASSWORD");
        } else if (System.getProperty("module") != null && System.getProperty("module").equalsIgnoreCase("elm")) {
            return testRailCfgElm.get("TEST_RAIL_PASSWORD");
        } else {
            return null;
        }
    }

    public static void addTestRun()
    {
        String testRailURL = getTestRailURL();
        String testRailUser = getTestRailUser();
        String testRailPassword = getTestRailPassword();
        int suiteId = Integer.valueOf(TestBase.testSuiteID);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =null;
        String strDate = null;
        String addResultString = "";
        String name = "";
        addResultString = "add_run/" + TestBase.testRailProjectID;
        try {
            // Make a connection with TestRail Server
            APIClient client = new APIClient(testRailURL);
            client.setUser(testRailUser);
            client.setPassword(testRailPassword);

            Map<String, Object> data = new HashMap<String, Object>();
            try{
                date = format.parse(timestamp.toString());
                strDate = format.format(date);
            }catch(ParseException e){
                System.err.println(e.getMessage());
            }
            data.put("include_all", false);
            data.put("suite_id", suiteId);
            if (TestBase.finalTestRailRunName==null||TestBase.finalTestRailRunName.equals("")){
                name = "Automation - Regression " + strDate;
            } else {
                name = TestBase.finalTestRailRunName+ " " + strDate;
            }
            data.put("name", name);
            JSONObject jSONObject = (JSONObject) client.sendPost(addResultString, data);
            long longTestRailRunId = (Long) jSONObject.get("id");
            //set test rail run ID=================================
            TestBase.testRailRunId = (int) longTestRailRunId;
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        } catch (APIException aPIException) {
            System.err.println(aPIException.getMessage());
        }
    }

    public static List<Integer> addNUpdateTestCaseIntoTestRail(String testName, ITestContext context)
    {
        List<Integer> testCaseIDList = new ArrayList<>();
        List<Integer> testCasesToAdd = new ArrayList<>();
        String testRailURL = getTestRailURL();
        String testRailUser = getTestRailUser();
        String testRailPassword = getTestRailPassword();

        try {
            // Make a connection with TestRail Server
            APIClient client = new APIClient(testRailURL);
            client.setUser(testRailUser);
            client.setPassword(testRailPassword);
            testCaseIDList = TestBase.AllTestCaseIDList;
            testCasesToAdd = getTestCaseIDFromTitle(testName, Integer.parseInt(TestBase.testRailProjectID), client);
            MyThreadLocal.setCurrentTestCaseIDList(testCasesToAdd);
            if (testCasesToAdd.isEmpty()){
                MyThreadLocal.setTestCaseExistsFlag(false);
                System.out.println("-------------------Cannot find the test cases for: " + testName + "-------------------");
            } else {
                MyThreadLocal.setTestCaseExistsFlag(true);
                testCaseIDList.addAll(testCasesToAdd);
                TestBase.AllTestCaseIDList = testCaseIDList;
            }
            updateTestCaseIntoTestRunSample(testName,context,testCaseIDList);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        return testCasesToAdd;
    }

    public static List<Integer> getTestCaseIDFromTitle(String title, int projectID, APIClient client)
    {
        JSONArray testCasesList = new JSONArray();
        JSONObject result;
        JSONObject jsonSectionName;
        JSONArray sectionNameList = new JSONArray();
        String s = null;
        int suiteId = Integer.valueOf(TestBase.testSuiteID);
        int testCaseID = 0;
        List<Integer> testCaseIDList = new ArrayList<>();
        try {
            do {
                if (s == null){
                    result = (JSONObject)client.sendGet("get_sections/"+projectID+"/&suite_id="+suiteId);
                } else {
                    result = (JSONObject)client.sendGet(s.replace("/api/v2/",""));
                }
                sectionNameList.addAll((JSONArray) result.get("sections")) ;
                s = String.valueOf(((JSONObject)result.get("_links")).get("next"));
            } while (s != null && s.contains("/api/v2/"));


            for(Object sectionName : sectionNameList)
            {

                jsonSectionName = (JSONObject) sectionName;
                if(title.trim().toLowerCase().equals(jsonSectionName.get("name").toString().trim().toLowerCase()))
                {
                    long longSectionID = (Long) jsonSectionName.get("id");
                    int sectionID = (int)longSectionID;
                    do {
                        if (s == null || s.equalsIgnoreCase("null")){
                            result = (JSONObject)client.sendGet("get_cases/"+projectID+"/&suite_id="+suiteId+"&section_id="+sectionID);
                        } else {
                            result = (JSONObject)client.sendGet(s.replace("/api/v2/",""));
                        }
                        testCasesList.addAll((JSONArray) result.get("cases")) ;
                        s = String.valueOf(((JSONObject)result.get("_links")).get("next"));
                    } while (s != null && s.contains("/api/v2/"));


                    //testCasesList = (JSONArray) client.sendGet("get_cases/"+projectID+"/&suite_id="+suiteId+"&section_id="+sectionID);
                    for(Object testCaseList : testCasesList){
                        JSONObject testCaseId = (JSONObject) testCaseList;
                        long longTestCaseID = (Long) testCaseId.get("id");
                        testCaseID = (int)longTestCaseID;
                        testCaseIDList.add(testCaseID);
                    }
                    break;
                }
            }
        } catch (IOException | APIException | NullPointerException e) {
            SimpleUtils.fail(e.getMessage(), true);
        }
        return testCaseIDList;
    }

    public static int updateTestCaseIntoTestRunSample(String testName, ITestContext context, List<Integer> testCaseIDList)
    {
        String testRailURL = getTestRailURL();
        String testRailUser = getTestRailUser();
        String testRailPassword = getTestRailPassword();
        int suiteId = Integer.valueOf(TestBase.testSuiteID);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =null;
        String strDate = null;
        String addResultString = "";
        String name = "";
        addResultString = "update_run/" + TestBase.testRailRunId;
        try {
            // Make a connection with TestRail Server
            APIClient client = new APIClient(testRailURL);
            client.setUser(testRailUser);
            client.setPassword(testRailPassword);

            //set up data.
            Map<String, Object> data = new HashMap<String, Object>();
            try{
                date = format.parse(timestamp.toString());
                strDate = format.format(date);
            }catch(ParseException e){
                System.err.println(e.getMessage());
            }
            data.put("include_all", false);
            data.put("suite_id", suiteId);
            if (TestBase.finalTestRailRunName==null||TestBase.finalTestRailRunName.equals("")){
                name = "Automation - Regression " + strDate;
            } else {
                name = TestBase.finalTestRailRunName+ " " + strDate;
            }
            data.put("name", name);
            data.put("case_ids", testCaseIDList);
            client.sendPost(addResultString, data);
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        } catch (APIException aPIException) {
            System.err.println(aPIException.getMessage());
        }
        return TestBase.testRailRunId;
    }

    public static void addResultForTest() {
        if(TestBase.testRailReportingFlag!=null&&MyThreadLocal.getTestCaseExistsFlag()){
            if (!MyThreadLocal.getTestSkippedFlag()){
                if (MyThreadLocal.getTestResultFlag()){
                    addTestResultIntoTestRailN(1, "");
                } else {
                    addTestResultIntoTestRailN(5, "");
                }
            }
        }
    }

    public static void addTestResultIntoTestRailN(int statusID, String comment) {
        String testRailURL = getTestRailURL();
        String testRailUser = getTestRailUser();
        String testRailPassword = getTestRailPassword();
        int testRailRunId = TestBase.testRailRunId;
        String addResultString = "add_results_for_cases/" + testRailRunId;
        List<Integer> testIds = new ArrayList<>();
        try {
            // Make a connection with TestRail Server
            APIClient client = new APIClient(testRailURL);
            client.setUser(testRailUser);
            client.setPassword(testRailPassword);
            List cases = new ArrayList();
            testIds = MyThreadLocal.getCurrentTestCaseIDList();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("results", cases);
            for (int testId : testIds) {
                Map singleCase = new HashMap();
                singleCase.put("case_id", "" + testId);
                singleCase.put("status_id", statusID);
                singleCase.put("comment", comment);
                cases.add(singleCase);
            }
            JSONArray jSONArray =(JSONArray)  client.sendPost(addResultString, data);
            JSONObject jsonObject = (JSONObject) jSONArray.get(0);
            long result_id = (long)jsonObject.get("id") ;
            if (statusID == 5){
                addAttachmentToResult(result_id);
            }
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        } catch (APIException aPIException) {
            System.err.println(aPIException.getMessage());
        }
    }

    /*
    * Author: Haya
    * Add attachment to result.
    *
    */
    public static void addAttachmentToResult(long result_id){
        String testRailURL = getTestRailURL();
        String testRailUser = getTestRailUser();
        String testRailPassword = getTestRailPassword();
        String addResultString = "add_attachment_to_result/" + result_id;
        try {
            // Make a connection with TestRail Server
            APIClient client = new APIClient(testRailURL);
            client.setUser(testRailUser);
            client.setPassword(testRailPassword);
            if (MyThreadLocal.getDriver()!=null){
                ScreenshotManager.takeScreenShot();
            } else {
                System.out.println("Session is null!");
            }
            client.sendPost(addResultString,MyThreadLocal.getScreenshotLocation());
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        } catch (APIException aPIException) {
            System.err.println(aPIException.getMessage());
        }
    }

    //Add by Haya
    public static boolean isTestRunEmpty(long testRailRunId){
        boolean result = false;
        String testRailURL = getTestRailURL();
        String testRailUser = getTestRailUser();
        String testRailPassword = getTestRailPassword();
        try {
            // Make a connection with Testrail Server
            APIClient client = new APIClient(testRailURL);
            client.setUser(testRailUser);
            client.setPassword(testRailPassword);
            JSONObject jSONObject= (JSONObject) client.sendGet("get_run/"+testRailRunId);
            long testRunPassedCount = (long) jSONObject.get("passed_count");
            long testRunFailedCount = (long) jSONObject.get("failed_count");
            long testRunBlockedCount = (long) jSONObject.get("blocked_count");
            long testRunRetestCount = (long) jSONObject.get("retest_count");
            long testRunUntestedCount = (long) jSONObject.get("untested_count");
            if ((testRunPassedCount+testRunFailedCount+testRunBlockedCount+testRunRetestCount+testRunUntestedCount)==0){
                result = true;
            }
        }catch(IOException ioException){
            System.err.println(ioException.getMessage());
        } catch(APIException aPIException){
            System.err.println(aPIException.getMessage());
        }
        return result;
    }

    public static void deleteTestRail(List<Integer> testRailIdList)
    {
        String testRailURL = getTestRailURL();
        String testRailUser = getTestRailUser();
        String testRailPassword = getTestRailPassword();

        // Make a connection with Testrail Server
        for(int i=0; i<testRailIdList.size();i++) {
            String addResult = "delete_run/" + testRailIdList.get(i);
            try {
                // Make a connection with TestRail Server
                APIClient client = new APIClient(testRailURL);
                client.setUser(testRailUser);
                client.setPassword(testRailPassword);
                Map<String, Object> data = new HashMap<String, Object>();
                client.sendPost(addResult, data);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
