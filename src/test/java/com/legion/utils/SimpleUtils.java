package com.legion.utils;

import com.aventstack.extentreports.Status;
import com.legion.test.testrail.APIClient;
import com.legion.test.testrail.APIException;
import com.legion.test.testrail.TestRailOperation;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.tests.testframework.ScreenshotManager;
import org.apache.commons.lang.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.util.Strings;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.legion.utils.MyThreadLocal.*;
import static com.legion.utils.MyThreadLocal.testRailRunId;
import static org.testng.AssertJUnit.assertTrue;
/**
 * Yanming
 */
public class SimpleUtils {

	static Map<String,String> parameterMap = getPropertiesFromJsonFileWithOverrides("src/test/resources/ciEnvCfg.json");
//		static Map<String,String> parameterMap = getPropertiesFromJsonFileWithOverrides("src/test/resources/envCfg.json");
	static HashMap<String,String> testRailConfig = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg.json");
	static HashMap<String,String> testRailCfgOp = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg_OP.json");
	static String chrome_driver_path = parameterMap.get("CHROME_DRIVER_PATH");
	public static String fileDownloadPath = parameterMap.get("Download_File_Default_Dir");

	private static HashMap< String,Object[][]> userCredentials = JsonUtil.getCredentialsFromJsonFile("src/test/resources/legionUsers.json");

	private static Map<String, String> getPropertiesFromJsonFileWithOverrides(String pathname) {
		String envFileLocation = System.getenv().getOrDefault("ENVCFG_FILE_LOCATION", pathname);
		Map<String, String> propMap = JsonUtil.getPropertiesFromJsonFile(envFileLocation);
		return propMap;
	}

	public static Map<String, String> getParameterMap() {
		return parameterMap;
	}

	public static DesiredCapabilities initCapabilities(String browser, String version, String os) {
		DesiredCapabilities caps = new DesiredCapabilities();
		if ("chrome".equals(browser)) {
			System.setProperty("webdriver.chrome.driver", chrome_driver_path);
		}
		return caps;
	}

	/*
	 * //todo will set up a remote selenium server on localhost. for now return null
	 */
	public static String getURL() {
		String uRL = parameterMap.get("URL");
		if (uRL.contains("{0}") && uRL.contains("{1}") && System.getProperty("seleniumGridPort") != null && !System.getProperty("seleniumGridPort").isEmpty()) {
			uRL = MessageFormat.format(uRL, System.getProperty("domainName"), System.getProperty("seleniumGridPort"));
		}
		return uRL;
	}

	public static void fail(String message, boolean continueExecution, String... severity) {
//		SimpleUtils.addTestResultIntoTestRail(5, message);
		if(TestBase.testRailReportingFlag!=null&&MyThreadLocal.getTestCaseExistsFlag()){
			MyThreadLocal.setTestResultFlag(false);
			TestRailOperation.addTestResultIntoTestRailN(5, message);
		}
		if (continueExecution) {
			try {
				assertTrue(false);
			} catch (Throwable e) {
				addVerificationFailure(e);
				ExtentTestManager.getTest().log(Status.ERROR, message);
			}
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, message);
			throw new AssertionError(message);
		}
	}

	private static void addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		getVerificationMap().put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}

	private static List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = getVerificationMap().get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<>() : verificationFailures;
	}

	public static String getCurrentUsersJobTitle(String userName)
	{
		Object[][] userDetails = JsonUtil.getArraysFromJsonFile("src/test/resources/legionUsersCredentials.json");
		String currentUserRole = "NA";
		for (Object[] user : userDetails) {
			String userNameFromJson = (String) user[0];
			String userTitleFromJson = (String) user[2];
			if(userNameFromJson.contains(userName))
				return userTitleFromJson;
		}
		return currentUserRole;
	}

	public synchronized static HashMap<String, String> getUserNameAndPwd()
	{
		Object[][] userDetails = JsonUtil.getArraysFromJsonFile("src/test/resources/UsersCredentials.json");
		String userNameFromJson= null;
		String userPwdFromJson= null;
		HashMap<String, String> userNameAndPwd = new HashMap<String, String>();
		for (Object[] user : userDetails) {
			userNameFromJson = (String) user[0];
			userPwdFromJson = (String) user[1];
			break;
		}
		userNameAndPwd.put("UserName",userNameFromJson);
		userNameAndPwd.put("UserPassword",userPwdFromJson);
		return userNameAndPwd;
	}

	public static String getListElementTextAsString(List<WebElement> listWebElements, String separator)
	{
		String listWebElementsText = "";
		for(WebElement listWebElement: listWebElements)
		{
			listWebElementsText = listWebElementsText + separator +listWebElement.getText();
		}
		return listWebElementsText;
	}


	// ToDo - Missing locator ID for SubTabs
	public static WebElement getSubTabElement(List<WebElement> listWebElements, String subTabText)
	{
		for(WebElement listWebElement : listWebElements)
		{
			if(listWebElement.getText().toLowerCase().contains(subTabText.toLowerCase()))
			{
				return listWebElement;
			}
		}
		return null;
	}

	public static void assertOnFail(String message, boolean isAssert, Boolean isExecutionContinue) {
		if (isExecutionContinue) {
			try {
				assertTrue(isAssert);
				MyThreadLocal.setTestResultFlag(true);
			} catch (Throwable e) {
				addVerificationFailure(e);
				ExtentTestManager.getTest().log(Status.ERROR, "<div class=\"row\" style=\"background-color:#FDB45C; color:white; padding: 7px 5px;\">" + message
						+ "</div>");
				if(TestBase.testRailReportingFlag!=null&&MyThreadLocal.getTestCaseExistsFlag()){
					MyThreadLocal.setTestResultFlag(false);
					TestRailOperation.addTestResultIntoTestRailN(5, message);
				}
			}
		} else {
			try {
				assertTrue(isAssert);
				MyThreadLocal.setTestResultFlag(true);
			} catch (Throwable e) {
				ExtentTestManager.getTest().log(Status.FAIL, message);
				if(TestBase.testRailReportingFlag!=null&&MyThreadLocal.getTestCaseExistsFlag()){
					MyThreadLocal.setTestResultFlag(false);
					TestRailOperation.addTestResultIntoTestRailN(5, message);
				}
				throw new AssertionError(message);
			}
		}
	}


	public static int getCurrentDateDayOfYear()
	{
		LocalDate currentDate = LocalDate.now();
		return currentDate.getDayOfYear();
	}

	public static int getCurrentISOYear()
	{
		LocalDate currentDate = LocalDate.now();
		return currentDate.getYear();
	}

	public static String getNextMonthAndYearFromCurrentMonth(String currentMonthYear) throws ParseException {
		String nextMonthAndYear = null;
		SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");
		Date date = format.parse(currentMonthYear);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		date = calendar.getTime();
		nextMonthAndYear = format.format(date);
		return nextMonthAndYear;
	}

	public static String getNewTimeByAddingMinutes(String currentTime, int minutes) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("hh:mma");
		Date date = format.parse(currentTime);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, (date.getMinutes() + minutes)*60);
		date = calendar.getTime();
		return format.format(date);
	}

	public static HashMap<String, String> getDayMonthDateFormatForCurrentPastAndFutureWeek(int dayOfYear, int isoYear) {
		LocalDate dateBasedOnGivenParameter = Year.of(isoYear).atDay(dayOfYear);
		LocalDate pastWeekDate = dateBasedOnGivenParameter.minusWeeks(1);
		LocalDate futureWeekDate = dateBasedOnGivenParameter.plusWeeks(1);
		HashMap<String, String> dateMonthOfCurrentPastAndFutureWeek = new HashMap<String, String>();
		dateMonthOfCurrentPastAndFutureWeek.put("currentWeekDate", getDayMonthDateFormat(dateBasedOnGivenParameter));
		dateMonthOfCurrentPastAndFutureWeek.put("pastWeekDate", getDayMonthDateFormat(pastWeekDate));
		dateMonthOfCurrentPastAndFutureWeek.put("futureWeekDate", getDayMonthDateFormat(futureWeekDate));
		return dateMonthOfCurrentPastAndFutureWeek;
	}

	public static LocalDate getCurrentLocalDateObject()
	{
		return Year.of(LocalDate.now().getYear()).atDay(LocalDate.now().getDayOfYear());
	}

	public static String getDayMonthDateFormat(LocalDate localDate) {
		String dayMonthDateFormat = null;
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		Month currentMonth = localDate.getMonth();
		int currentDate = localDate.getDayOfMonth();
		if(currentDate > 9)
		{
			dayMonthDateFormat = dayOfWeek.toString().substring(0, 3) + " " + currentMonth.toString().substring(0, 3) + " " +currentDate;
		}
		else
		{
			dayMonthDateFormat = dayOfWeek.toString().substring(0, 3) + " " + currentMonth.toString().substring(0, 3) + " 0" +currentDate;
		}

		return dayMonthDateFormat;
	}

	public static void pass(String message) {

		ExtentTestManager.getTest().log(Status.PASS,"<div class=\"row\" style=\"background-color:#44aa44; color:white; padding: 7px 5px;\">" + message
				+ "</div>");
		MyThreadLocal.setTestResultFlag(true);
/*		if(TestBase.testRailReportingFlag!=null&&MyThreadLocal.getTestCaseExistsFlag()){
			SimpleUtils.addTestResultIntoTestRailN(1, message);
		}
*/
	}

	public static void report(String message) {

		ExtentTestManager.getTest().log(Status.INFO,"<div class=\"row\" style=\"background-color:#0000FF; color:white; padding: 7px 5px;\">" + message
				+ "</div>");
//		if(getTestRailReporting()!=null&&MyThreadLocal.getTestCaseExistsFlag()){
//			SimpleUtils.addTestResultIntoTestRailN(6, message);
//		}
	}

	public static void warn(String message) {

		ExtentTestManager.getTest().log(Status.WARNING,"<div class=\"row\" style=\"background-color:#FFA500; color:white; padding: 7px 5px;\">" + message
				+ "</div>");
//		if(getTestRailReporting()!=null&&MyThreadLocal.getTestCaseExistsFlag()){
//			SimpleUtils.addTestResultIntoTestRailN(6, message);
//		}
	}

	public static HashMap<String, Object[][]> getEnvironmentBasedUserCredentialsFromJson(String fileName)
	{

		return JsonUtil.getCredentialsFromJsonFile("src/test/resources/"+fileName);

	}

	public static String getDefaultEnterprise () {
		return parameterMap.get("ENTERPRISE");
	}

	public static String getEnterprise (String enterpriseKey) {
		String result = null;
		if (!Strings.isNullOrEmpty(enterpriseKey)) {
			result = parameterMap.get(enterpriseKey);
		}
		return result != null ? result : getDefaultEnterprise();
	}

	public static String getEnterprise (Method testMethod) {
		Enterprise e = testMethod.getAnnotation(Enterprise.class);
		String enterpriseName = null;
		if (e != null ) {
			enterpriseName = SimpleUtils.getEnterprise(e.name());
		}
		else {
			enterpriseName = SimpleUtils.getDefaultEnterprise();
		}
		return enterpriseName;
	}
/*
	public static String getTestSuiteID (String testSuiteName) {
		String result = null;
		if (Strings.isNullOrEmpty(testSuiteName)) {
			result = testSuites.get(testSuiteName);
		}
		return result != null ? result : MyThreadLocal.getTestSuiteID();
	}

	public static String getTestSuiteID (Method testMethod) {
		TestSuiteName testSuiteName = testMethod.getAnnotation(TestSuiteName.class);
		String id = null;
		if (testSuiteName != null ) {
			id = SimpleUtils.getTestSuiteID(testSuiteName.testSuiteName());
		}
		else {
			id = SimpleUtils.getDefaultEnterprise();
		}
		return id;
	}
*/
	public static void sortHashMapbykey(HashMap<String, Object[][]> hashMap)
	{
		TreeMap<String, Object[][]> sorted = new TreeMap<>();
		sorted.putAll(hashMap);
	}

	public static Object[][] concatenateObjects(Object[][] browersData, Object[][] credentialsByRole)
	{
		Object[][] combinedresult = new Object[credentialsByRole.length * browersData.length][];
		int index = 0;
		for(Object[] credentialByRole: credentialsByRole)
		{
			Object[] result = new Object[credentialByRole.length + 1];
			for(Object[] browerData : browersData)
			{
				System.arraycopy(browerData, 0, result, 0, 1);
				System.arraycopy(credentialByRole, 0, result, 1, credentialByRole.length);
				combinedresult[index] = result;
				index = index + 1;
			}
		}
		return combinedresult;
	}

	public static int countDuplicates(ArrayList list)
	{
		int duplicates = 0;
		for (int i = 0; i < list.size()-1;i++) {
			boolean found = false;
			for (int j = i+1; !found && j < list.size(); j++)  {
				if (list.get(i).equals(list.get(j)))
				{
					System.out.println("list.get(i) vs (list.get(j): "+list.get(i)+" "+list.get(j));
					found = true;
					duplicates++;
				}

			}
		}
		return duplicates;
	}

	public static void verifyTeamCount(List<String> previousTeamCount, List<String> currentTeamCount) throws Exception {
		if(previousTeamCount.size() == currentTeamCount.size()){
			for(int i =0; i<currentTeamCount.size();i++){
				String currentCount = currentTeamCount.get(i);
				String previousCount = previousTeamCount.get(i);
				if(Integer.parseInt(currentCount) == Integer.parseInt(previousCount)+1){
					SimpleUtils.pass("Current Team Count is greater than Previous Team Count");
				}else{
					SimpleUtils.fail("Current Team Count is not greater than Previous Team Count",true);
				}
			}
		}else{
			SimpleUtils.fail("Size of Current Team Count should be equal to Previous Team Count",false);
		}
	}
	public static String getCurrentDateMonthYearWithTimeZone(String timeZone, SimpleDateFormat sdf)
	{
		String date = "";
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		date = sdf.format(new Date());
		return date;
	}

	public static String getCurrentTimeWithTimeZone(String timeZone)
	{
		String date = "";
		SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("hh:mm a");
		dateTimeInGMT.setTimeZone(TimeZone.getTimeZone(timeZone));
		date = dateTimeInGMT.format(new Date());
		return date;
	}

	public static boolean isDateFormatCorrect(String date, SimpleDateFormat format) {
		boolean convertSuccess = true;
		try {
			format.parse(date);
		}catch (ParseException e){
			convertSuccess = false;
		}
		return convertSuccess;
	}

	public enum weekDayNames {
		Mon("Monday"),
		Tue("Tuesday"),
		Wed("Wednesday"),
		Thu("Thursday"),
		Fri("Friday"),
		Sat("Saturday"),
		Sun("Sunday");
		private final String value;

		weekDayNames(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	public static String getFullWeekDayName(String shortName) {
		String fullName = "";
		weekDayNames[] shortNames = weekDayNames.values();
		for (int i = 0; i < shortNames.length; i++) {
			if (shortNames[i].name().equalsIgnoreCase(shortName)) {
				fullName = shortNames[i].value;
				SimpleUtils.report("Get the full name of " + shortName + ", is: " + fullName);
				break;
			}
		}
		return fullName;
	}

	public static boolean isTimeBetweenStartNEndTime(String nowStartDate, String nowEndDate, String startDate, String endDate) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("hh:mm a");

		Date nowStart = format.parse(nowStartDate);
		Date nowEnd = format.parse(nowEndDate);
		Date start = format.parse(startDate);
		Date end = format.parse(endDate);

		long nowStartTime = nowStart.getTime();
		long nowEndTime = nowEnd.getTime();
		long startTime = start.getTime();
		long endTime = end.getTime();

		return nowStartTime >= startTime && nowEndTime <= endTime;
	}

	public static int getHashMapKeyByValue(HashMap<Integer, String> hashMap, String value) {
		int expectedKey = 0;
		for (Integer key : hashMap.keySet()) {
			if (hashMap.get(key).equalsIgnoreCase(value)) {
				expectedKey = key;
				break;
			}
		}
		return expectedKey;
	}

	public static String dateWeekPickerDateComparision(String weekActiveDate) {
		int i = 0;
		List<String> listWeekActiveDate = new ArrayList();
		String dateRangeDayPicker = null;
		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher match = pattern.matcher(weekActiveDate);
		String[] dateRange = weekActiveDate.split("-");
		while (match.find()) {
			if (Integer.parseInt(match.group(1)) < 10) {
				String padded = String.format("%02d", Integer.parseInt(match.group(1)));
				listWeekActiveDate.add(dateRange[i].replace(match.group(1), padded));
			} else {
				listWeekActiveDate.add(dateRange[i]);
			}
			i++;
		}
		dateRangeDayPicker = listWeekActiveDate.get(0) + "-" + listWeekActiveDate.get(1);
		return dateRangeDayPicker;

	}

	// method for mobile test cases incase of failure

//	public static void fail(String message, boolean continueExecution, String platform) {
//		SimpleUtils.addTestResultIntoTestRail(5,message);
//		if (continueExecution) {
//			try {
//				assertTrue(false);
//			} catch (Throwable e) {
//				addVerificationFailure(e);
//				TestRailOperation.addAttachmentToResult(getTestRailTestResultId());
//				ExtentTestManager.getTest().log(Status.ERROR, message + " " + platform);
//			}
//		} else {
//			TestRailOperation.addAttachmentToResult(getTestRailTestResultId());
//			ExtentTestManager.getTest().log(Status.FAIL, message);
//			throw new AssertionError(message);
//		}
//	}

	// added code for TestRail connection

//	public static void addTestResult(int statusID, String comment)
//	{
//		/*
//		 * TestRail Status ID : Description
//		 * 1 : Passed
//		 * 2 : Blocked
//		 * 4 : Retest
//		 * 5 : Failed
//		 */
//
//		MyThreadLocal myThreadLocal = new MyThreadLocal();
//		String testCaseId = Integer.toString(ExtentTestManager.getTestRailId(myThreadLocal.getCurrentMethod()));
//		String testName = ExtentTestManager.getTestName(myThreadLocal.getCurrentMethod());
//		String addResultString = "add_result_for_case/1/"+testCaseId+"";
//		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
//		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
//		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//
//		if(testCaseId != null && Integer.valueOf(testCaseId) > 0)
//		{
//			try {
//				// Make a connection with Testrail Server
//				APIClient client = new APIClient(testRailURL);
//				client.setUser(testRailUser);
//				client.setPassword(testRailPassword);
//
//				JSONObject c = (JSONObject) client.sendGet("get_case/"+testCaseId);
//				String TestRailTitle = (String) c.get("title");
//				if(! TestRailTitle.equals(testName))
//				{
//					Map<String, Object> updateTestTitle = new HashMap<String, Object>();
//					updateTestTitle.put("title", testName);
//					client.sendPost("update_case/"+testCaseId, updateTestTitle);
//				}
//
//				Map<String, Object> data = new HashMap<String, Object>();
//				data.put("status_id", statusID);
//				data.put("comment", comment);
//				client.sendPost(addResultString,data );
//
//			} catch(IOException ioException) {
//				System.err.println(ioException.getMessage());
//			}
//			catch(APIException aPIException)
//			{
//				System.err.println(aPIException.getMessage());
//			}
//		}
//
//	}


	public static void addTestCase(String scenario, String summary, String testSteps, String expectedResult, String actualResult,
								   String testData, String preconditions, String testCaseType, String priority,
								   String isAutomated, String result, String actions, int sectionID)
	{
		MyThreadLocal myThreadLocal = new MyThreadLocal();
		String testCaseId = Integer.toString(ExtentTestManager.getTestRailId(myThreadLocal.getCurrentMethod()));
		String testName = ExtentTestManager.getTestName(myThreadLocal.getCurrentMethod());
		String addResultString = "add_case/"+sectionID;
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		try {
			// Make a connection with Testrail Server
			APIClient client = new APIClient(testRailURL);
			client.setUser(testRailUser);
			client.setPassword(testRailPassword);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("title", summary);
			data.put("priority_id", 1);
			data.put("custom_custom_testdata",testData) ;
			data.put("custom_steps", testSteps);
			data.put("custom_custom_automated",isAutomated) ;
			//data.put("custom_custom_useraccess",testCaseType);
			data.put("custom_expected", expectedResult);
			data.put("custom_preconds", preconditions);

			System.out.println(client.sendPost(addResultString,data ));
		}

		catch(IOException ioException)
		{
			System.err.println(ioException.getMessage());
		}
		catch(APIException aPIException)
		{
			System.err.println(aPIException.getMessage());
		}
	}


	//added by Nishant

	public static void addSectionId(String module)
	{
		MyThreadLocal myThreadLocal = new MyThreadLocal();
		String testCaseId = Integer.toString(ExtentTestManager.getTestRailId(myThreadLocal.getCurrentMethod()));
		String testName = ExtentTestManager.getTestName(myThreadLocal.getCurrentMethod());
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		int projectId = Integer.valueOf(testRailConfig.get("TEST_RAIL_PROJECT_ID"));
		int suiteId = Integer.valueOf(TestBase.testSuiteID);
		String addResultString = "add_section/"+projectId;

		try {
			// Make a connection with Testrail Server
			APIClient client = new APIClient(testRailURL);
			client.setUser(testRailUser);
			client.setPassword(testRailPassword);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("suite_id", suiteId);
			data.put("name", module);
			JSONObject sectionData = (JSONObject) client.sendPost(addResultString,data );
			long sectionId = (long) sectionData.get("id");
			System.out.println(sectionId);
			setModuleName(sectionData.get("name").toString());
			setSectionID((int)sectionId);
//			String addResultString1 = "add_section/"+projectId+"/&section_id="+sectionId;
//			JSONObject jSONObject = (JSONObject) client.sendGet("get_case/"+testCaseId);
//			Map<String, Object> data1 = new HashMap<String, Object>();
//			data1.put("suite_id", suiteId);
//			data1.put("name", module);
//			data1.put("parent_id", null);
//			JSONObject sectionData1 = (JSONObject) client.sendPost(addResultString1,data1);
//			long sectionId1 = (long) sectionData1.get("id");
//			System.out.println(sectionId1);
		}

		catch(IOException ioException)
		{
			System.err.println(ioException.getMessage());
		}
		catch(APIException aPIException)
		{
			System.err.println(aPIException.getMessage());
		}

	}




	public static void updateTestCase(String scenario, String summary, String testSteps, String expectedResult,
									  String actualResult, String testData, String preconditions, String testCaseType, String priority,
									  String isAutomated, String result, String actions, int sectionID)
	{
		MyThreadLocal myThreadLocal = new MyThreadLocal();
		//String testCaseId = Integer.toString(ExtentTestManager.getTestRailId(myThreadLocal.getCurrentMethod()));
		String testName = ExtentTestManager.getTestName(myThreadLocal.getCurrentMethod());
		String updateResultString = "update_case";
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		int projectId = Integer.valueOf(testRailConfig.get("TEST_RAIL_PROJECT_ID"));
		try {
			// Make a connection with Testrail Server
			APIClient client = new APIClient(testRailURL);
			client.setUser(testRailUser);
			client.setPassword(testRailPassword);

			int testCaseID = getTestCaseIDFromTitle(summary, projectId, client, sectionID);
			System.out.println("testCaseID : "+testCaseID);
			if(testCaseID > 0)
			{
				Map<String, Object> testCaseDataToUpdate = new HashMap<String, Object>();
				testCaseDataToUpdate.put("priority_id", getPriorityIntegerValue(priority));
				testCaseDataToUpdate.put("custom_custom_testdata",testData) ;
				testCaseDataToUpdate.put("custom_steps", testSteps);
				testCaseDataToUpdate.put("custom_custom_automated",isAutomated) ;
				//data.put("custom_custom_useraccess",testCaseType);
				testCaseDataToUpdate.put("custom_expected", expectedResult);
				testCaseDataToUpdate.put("custom_preconds", preconditions);

				JSONObject updateTestCaseResult = (JSONObject) client.sendPost(updateResultString + "/" + testCaseID, testCaseDataToUpdate);
				pass("Test Case with ID :'"+ testCaseID +"' Updated Successfully ('"+ updateTestCaseResult +"");
			}
			else {
				report("No Test Case found with the title :'"+ summary +"'.");
			}

		}

		catch(IOException ioException)
		{
			System.err.println(ioException.getMessage());
			fail(ioException.getMessage(), true);
		}
		catch(APIException aPIException)
		{
			System.err.println(aPIException.getMessage());
			fail(aPIException.getMessage(), true);
		}
	}


	public static int getPriorityIntegerValue(String priority)
	{
		priority = priority.toLowerCase();
		int integerPriority = 0;
		switch (priority) {
			case "highest":
				integerPriority = 4;
				break;
			case "high":
				integerPriority = 3;
				break;
			case "Medium":
				integerPriority = 2;
				break;
			default:
				integerPriority = 1;
				break;
		}

		return integerPriority;
	}

	public static int getTestCaseIDFromTitle(String title, int projectID, APIClient client, int sectionID)
	{
		JSONArray testCasesList;
		JSONObject jsonTestCase;
		int suiteId = Integer.valueOf(TestBase.testSuiteID);
		int testCaseID = 0;
		try {
			testCasesList = (JSONArray) client.sendGet("get_cases/"+projectID+"/&suite_id="+suiteId+"&section_id="+sectionID);
			for(Object testCase : testCasesList)
			{

				jsonTestCase = (JSONObject) testCase;
				if(title.trim().toLowerCase().equals(jsonTestCase.get("title").toString().trim().toLowerCase()))
				{
					testCaseID = Integer.valueOf(jsonTestCase.get("id").toString());
				}
			}
		} catch (IOException | APIException | NullPointerException e) {
			fail(e.getMessage(), true);
		}
		return testCaseID;
	}

	//added by Nishant

//	public static JSONArray getTestCasesIDFromTitle(String title, int projectID, APIClient client, int sectionID)
//	{
//		JSONArray testCasesList  = new JSONArray();
//		JSONObject jsonTestCase;
//		int suiteId = Integer.valueOf(TestBase.testSuiteID);
//		int testCaseID = 0;
//		try {
//			testCasesList = (JSONArray) client.sendGet("get_cases/"+projectID+"/&suite_id="+suiteId+"&section_id="+sectionID);
//		} catch (IOException | APIException | NullPointerException e) {
//			fail(e.getMessage(), true);
//		}
//		return testCasesList;
//	}


	public static void deleteTestCaseByID(int testCaseID)
	{
		String deleteResultString = "delete_case";
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");

		try {
			// Make a connection with Testrail Server
			APIClient client = new APIClient(testRailURL);
			client.setUser(testRailUser);
			client.setPassword(testRailPassword);

			Map<String, Object> testCaseDataToDelete = new HashMap<String, Object>();
			testCaseDataToDelete.put("id", testCaseID);
			JSONObject deleteTestCaseResult = (JSONObject) client.sendPost(deleteResultString+ "/" + testCaseID, testCaseDataToDelete)/* client.sendGet(deleteResultString + "/" + testCaseID)*/;

			pass("Test Case with ID :'"+ testCaseID +"' Deleted Successfully ('"+ deleteTestCaseResult +"').");
			System.out.println("Test Case with ID :'"+ testCaseID +"' Deleted Successfully ('"+ deleteTestCaseResult +"').");
		}

		catch(IOException | APIException exception)
		{
			System.err.println(exception.getMessage());
			fail(exception.getMessage(), true);
		}
	}

	public static void deleteTestCaseByTitle(String title, int projectId, int sectionID)
	{
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");

		// Make a connection with Testrail Server
		APIClient client = new APIClient(testRailURL);
		client.setUser(testRailUser);
		client.setPassword(testRailPassword);

		int testCaseID = getTestCaseIDFromTitle(title, projectId, client, sectionID);

		if(testCaseID > 0)
		{
			deleteTestCaseByID(testCaseID);
		}

	}

	public static Float convertDateIntotTwentyFourHrFormat(String startTime, String endTime) throws ParseException {
		SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
		int shiftHourcalculation =0;
		Float shiftMinutecalculation =0.0f;
		Float scheduleHoursDifference = 0.0f;
		Date startDateFormat = parseFormat.parse(startTime.substring(0,startTime.length()-2) + " " +startTime.substring(startTime.length()-2));
		Date endDateFormat = parseFormat.parse(endTime.substring(0,endTime.length()-2) + " " +endTime.substring(endTime.length()-2));
		String strEndDate = displayFormat.format(endDateFormat).toString();
		String strStartDate = displayFormat.format(startDateFormat).toString();
		String[] arrEndDate = strEndDate.split(":");
		String[] arrStartDate = strStartDate.split(":");
		if(endTime.contains("AM")){
			shiftHourcalculation = (24 + Integer.parseInt(arrEndDate[0]))-(Integer.parseInt(arrStartDate[0]));
			shiftMinutecalculation =  (Float.parseFloat(arrEndDate[1]) -  Float.parseFloat(arrEndDate[1]))/60;
			scheduleHoursDifference = shiftHourcalculation + shiftMinutecalculation ;
		}else{
			shiftHourcalculation = Integer.parseInt(arrEndDate[0])-Integer.parseInt(arrStartDate[0]);
			shiftMinutecalculation =  (Float.parseFloat(arrEndDate[1]) -  Float.parseFloat(arrStartDate[1]))/60;
			scheduleHoursDifference = shiftHourcalculation + shiftMinutecalculation ;
		}

		return scheduleHoursDifference;
	}

	//added by Nishant

//	public static int addNUpdateTestCaseIntoTestRail(String testName, int sectionID,ITestContext context)
//	{
//		int testCaseID = 0;
//
//		if(sectionID > 0){
//
////	    String testName = ExtentTestManager.getTestName(MyThreadLocal.getCurrentMethod());
//			String addResultString = "add_case/"+sectionID;
//			String testRailURL = testRailConfig.get("TEST_RAIL_URL");
//			String testRailUser = testRailConfig.get("TEST_RAIL_USER");
//			String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//			String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
//			//String testRailSuiteID = TestBase.testSuiteID;
//			try {
//				// Make a connection with TestRail Server
//				APIClient client = new APIClient(testRailURL);
//				client.setUser(testRailUser);
//				client.setPassword(testRailPassword);
//				testCaseID = getTestCaseIDFromTitle(testName, Integer.parseInt(testRailProjectID), client, sectionID);
////				JSONArray testCasesList = getTestCasesIDFromTitle(testName, Integer.parseInt(testRailProjectID), client, sectionID);
////				if(!testCasesList.isEmpty()){
////					addNUpdateTestCaseIntoTestRun(testName,sectionID,testCasesList);
////				}
//				if(testCaseID > 0){
////					addNUpdateTestCaseIntoTestRun(testName,sectionID,testCaseID);
////					addNUpdateTestCaseIntoTestRun1(testName,sectionID,testCaseID,context);
//					addNUpdateTestCaseIntoTestRun2(testName,sectionID,testCaseID,context);
//					return testCaseID;
//				}else{
//					Map<String, Object> data = new HashMap<String, Object>();
//					data.put("title", testName);
//					client.sendPost(addResultString,data );
//					testCaseID = getTestCaseIDFromTitle(testName, Integer.parseInt(testRailProjectID), client, sectionID);
//					addNUpdateTestCaseIntoTestRun(testName,sectionID,testCaseID);
////					addNUpdateTestCaseIntoTestRun1(testName,sectionID,testCaseID,context);
//					return testCaseID;
//				}
//
//
//			}catch(IOException ioException){
//				System.err.println(ioException.getMessage());
//			}
//			catch(APIException aPIException){
//				System.err.println(aPIException.getMessage());
//			}
//
//
//		}
//		return testCaseID;
//	}



//	public static List<Integer> addNUpdateTestCaseIntoTestRail(String testName,ITestContext context)
//	{
//		//int testCaseID = 0;
//		List<Integer> testCaseIDList = new ArrayList<>();
//		List<Integer> testCasesToAdd = new ArrayList<>();
////	    String testName = ExtentTestManager.getTestName(MyThreadLocal.getCurrentMethod());
//		//String addResultString = "add_case/"+sectionID;
//		String testRailURL =        "";
//		String testRailUser =       "";
//		String testRailPassword =   "";
//		//String testRailSuiteID =    "";
//		if (!System.getProperty("enterprise").equalsIgnoreCase("opauto")) {
//			testRailURL = testRailConfig.get("TEST_RAIL_URL");
//			//setTestRailURL(testRailURL);
//			testRailUser = testRailConfig.get("TEST_RAIL_USER");
//			//setTestRailUser(testRailUser);
//			testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//			//setTestRailPassword(testRailPassword);
//		}else {
//			testRailURL = testRailCfgOp.get("TEST_RAIL_URL");
//			//setTestRailURL(testRailURL);
//			testRailUser = testRailCfgOp.get("TEST_RAIL_USER");
//			//setTestRailUser(testRailUser);
//			testRailPassword = testRailCfgOp.get("TEST_RAIL_PASSWORD");
//			//setTestRailPassword(testRailPassword);
//			//testRailSuiteID = MyThreadLocal.getTestSuiteID();
//			//String testRailSuiteID = testRailConfig.get("TEST_RAIL_SUITE_ID");
//		}
//		try {
//			// Make a connection with TestRail Server
//			APIClient client = new APIClient(testRailURL);
//			client.setUser(testRailUser);
//			client.setPassword(testRailPassword);
//			testCaseIDList = TestBase.AllTestCaseIDList;
//			testCasesToAdd = getTestCaseIDFromTitle(testName, Integer.parseInt(TestBase.testRailProjectID), client);
//			if (testCasesToAdd.isEmpty()){
//				MyThreadLocal.setTestCaseExistsFlag(false);
//				System.out.println("-------------------Cannot find the test cases for: " + testName + "-------------------");
//			} else {
//				MyThreadLocal.setTestCaseExistsFlag(true);
//				testCaseIDList.addAll(testCasesToAdd);
//				TestBase.AllTestCaseIDList = testCaseIDList;
//			}
//			updateTestCaseIntoTestRunSample(testName,context,testCaseIDList);
//		}catch(Exception e){
//			System.err.println(e.getMessage());
//		}
//
//		return testCaseIDList;
//	}

//	public static List<Integer> getTestCaseIDFromTitle(String title, int projectID, APIClient client)
//	{
//		JSONArray testCasesList;
//		JSONObject jsonSectionName;
//		JSONArray sectionNameList;
////		JSONObject testCaseId;
//		int suiteId = Integer.valueOf(TestBase.testSuiteID);
//		int testCaseID = 0;
//		List<Integer> testCaseIDList = new ArrayList<>();
//		try {
//			sectionNameList = (JSONArray) client.sendGet("get_sections/"+projectID+"/&suite_id="+suiteId);
//			for(Object sectionName : sectionNameList)
//			{
//
//				jsonSectionName = (JSONObject) sectionName;
//				if(title.trim().toLowerCase().equals(jsonSectionName.get("name").toString().trim().toLowerCase()))
//				{
//					long longSectionID = (Long) jsonSectionName.get("id");
//					int sectionID = (int)longSectionID;
//					testCasesList = (JSONArray) client.sendGet("get_cases/"+projectID+"/&suite_id="+suiteId+"&section_id="+sectionID);
//					for(Object testCaseList : testCasesList){
//						JSONObject testCaseId = (JSONObject) testCaseList;
//						long longTestCaseID = (Long) testCaseId.get("id");
//						testCaseID = (int)longTestCaseID;
//						testCaseIDList.add(testCaseID);
//					}
//					break;
//				}
//			}
//		} catch (IOException | APIException | NullPointerException e) {
//			fail(e.getMessage(), true);
//		}
//		return testCaseIDList;
//	}


	//added by Nishant

//	public static void addTestResultIntoTestRail(int statusID, String comment)
//	{
//		/*
//		 * TestRail Status ID : Description
//		 * 1 : Passed
//		 * 2 : Blocked
//		 * 4 : Retest
//		 * 5 : Failed
//		 */
//		int testCaseId = MyThreadLocal.getTestCaseId();
//		String testName = ExtentTestManager.getTestName(MyThreadLocal.getCurrentMethod());
//		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
//		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
//		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//		int testRailRunId = TestBase.testRailRunId;
////		String addResultString = "add_result_for_case/"+testRailRunId+"/"+testCaseId+"";
//		String addResultString = "add_results_for_cases/"+testRailRunId;
//
//		if(testCaseId > 0)
//		{
//			try {
//				// Make a connection with Testrail Server
//				APIClient client = new APIClient(testRailURL);
//				client.setUser(testRailUser);
//				client.setPassword(testRailPassword);
//				JSONObject jSONObject = (JSONObject) client.sendGet("get_case/"+testCaseId);
//				if(statusID == 5) {
//					Map<String, Object> data = new HashMap<String, Object>();
//					takeScreenShotOnFailure();
//					String finalLink = getscreenShotURL();
//					data.put("status_id", statusID);
//					data.put("comment", comment +"\n" + "[Link To ScreenShot]" +"("+finalLink +")");
//					client.sendPost(addResultString, data);
//				}else{
//					Map<String, Object> data = new HashMap<String, Object>();
//					data.put("status_id", statusID);
//					data.put("comment", comment);
//					client.sendPost(addResultString, data);
//				}
//			}catch(IOException ioException){
//				System.err.println(ioException.getMessage());
//			} catch(APIException aPIException){
//				System.err.println(aPIException.getMessage());
//			}
//		}
//
//	}

	//added by Nishant

//	public static void addTestResultIntoTestRailN(int statusID, String comment) {
//
//		String testName = ExtentTestManager.getTestName(MyThreadLocal.getCurrentMethod());
//		String testRailURL = getTestRailURL();
//		String testRailUser = getTestRailUser();
//		String testRailPassword = getTestRailPassword();
//		String testRailProjectID = TestBase.testRailProjectID;
//		int testRailRunId = TestBase.testRailRunId;
////		String addResultString = "add_result_for_case/"+testRailRunId+"/"+testCaseId+"";
//		String addResultString = "add_results_for_cases/"+testRailRunId;
//		JSONArray response = null;
//		List<Integer> testIds = new ArrayList<>();
//		List<String> commentSection = new ArrayList<>();
//		List<String> failedCommentSection = new ArrayList<>();
//		try {
//			// Make a connection with Testrail Server
//			APIClient client = new APIClient(testRailURL);
//			client.setUser(testRailUser);
//			client.setPassword(testRailPassword);
//			List cases = new ArrayList();
//			testIds = getTestCaseIDFromTitle(testName, Integer.parseInt(testRailProjectID), client);
//			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("results",cases);
//			for ( int testId : testIds )
//			{
//				Map singleCase = new HashMap();
//				singleCase.put("case_id", "" + testId);
//				singleCase.put("status_id", statusID);
//				singleCase.put("comment", comment);
//				cases.add(singleCase);
//				if(statusID == 5){
//					if(getFailedComment()== null){
//						failedCommentSection.add(comment);
//						setFailedComment(failedCommentSection);
//					}
////					else{
////						getFailedComment().add(comment);
////						setFailedComment(getFailedComment());
////					}
//				}
//			}
//			String responseReq = JSONValue.toJSONString(data);
//			response  =  (JSONArray) client.sendPost(addResultString,data);
//
//		}catch(IOException ioException){
//			System.err.println(ioException.getMessage());
//		} catch(APIException aPIException){
//			System.err.println(aPIException.getMessage());
//		}
//		int count =0;
//		if(getComment() == null){
//			setComment(testName);
//			commentSection.add("Start " +testName);
////			setComment(comment);
//			commentSection.add(comment);
//			setCommentSection(commentSection);
//		}else{
//			for(int i =0; i<getCommentSection().size();i++){
//				commentSection.add(getCommentSection().get(i));
//			}
//			if(!getComment().equalsIgnoreCase(testName)){
//				setComment(testName);
//				commentSection.add("Start " +testName);
//			}
//			commentSection.add(comment);
//			setCommentSection(commentSection);
//		}
//
//	}

//	public static void addTestResultIntoTestRailN(int statusPassID, int statusFailID ,ITestContext context) {
//		String testName = ExtentTestManager.getTestName(MyThreadLocal.getCurrentMethod());
//		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
//		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
//		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
//		int testRailRunId = TestBase.testRailRunId;
////		String addResultString = "add_result_for_case/"+testRailRunId+"/"+testCaseId+"";
//		String addResultString = "add_results_for_cases/"+testRailRunId;
//		JSONArray response = null;
//		List<Integer> testIds = new ArrayList<>();
//		List<String> commentSection = new ArrayList<>();
//		List<Integer> commentSectionWithTestName = new ArrayList<>();
//		int statusId = statusPassID;
////		Object[] arrComment = getCommentSection().toArray();
////		String[] strComment = Arrays.copyOf(arrComment, arrComment.length,String[].class);
//		try {
//			// Make a connection with Testrail Server
//			APIClient client = new APIClient(testRailURL);
//			client.setUser(testRailUser);
//			client.setPassword(testRailPassword);
//			List<String> testNameList = new ArrayList<String>();
//			List<Integer> testRailIdList = new ArrayList<>();
//			testNameList = (List)context.getAttribute("TestName");
//			testRailIdList = (List) context.getAttribute("TestRailId");
//			Collections.reverse(testRailIdList);
//			List commentSectionIndex = new ArrayList<>();
//			int counter =0;
//			for(int i=0;i<testNameList.size();i++){
//				List cases = new ArrayList();
//				testIds = getTestCaseIDFromTitle(testNameList.get(i), Integer.parseInt(testRailProjectID), client);
//				//status validation
//				JSONObject jSONObject= (JSONObject) client.sendGet("get_run/"+testRailIdList.get(i));
//				long longTestRunPassStatus = (Long) jSONObject.get("passed_count");
//				int TestRunPassStatus = (int) longTestRunPassStatus;
//				List<String> listComments = new ArrayList<>();
//				List<String> listCommentSection = new ArrayList<>();
//				for(int j=0; j<getCommentSection().size(); j++){
//					if(getCommentSection().get(j).contains(testNameList.get(i).trim())){
//						for(int k=j+1; k< getCommentSection().size();k++){
//							if(getCommentSection().get(k).contains("Start")){
//								break;
//							}
//							listCommentSection.add(getCommentSection().get(k));
//						}
//					}
//				}
//				if(getFailedComment()!=null){
//					for(int j =0; j<getFailedComment().size();j++){
//						for(int k=0; k<listCommentSection.size();k++){
//							if(listCommentSection.get(k).equalsIgnoreCase(getFailedComment().get(j))){
//								commentSectionIndex.add(k);
//							}
//						}
//					}
//				}
//
////				if(TestRunPassStatus >0){
//				if(commentSectionIndex.isEmpty()){
//					Map<String, Object> data = new HashMap<String, Object>();
//					data.put("results",cases);
//					for ( int testId : testIds ) {
//						for( String commentSections : listCommentSection) {
//							Map singleCase = new HashMap();
//							singleCase.put("case_id", "" + testId);
//							singleCase.put("status_id", statusPassID);
//							singleCase.put("comment", commentSections);
//							cases.add(singleCase);
//						}
//					}
//					String responseReq = JSONValue.toJSONString(data);
//					response  =  (JSONArray) client.sendPost(addResultString,data);
//				}else{
//					Map<String, Object> data = new HashMap<String, Object>();
//					data.put("results",cases);
//					listCommentSection.add("Failed");
//					for ( int testId : testIds ) {
//						for( int j=0; j<listCommentSection.size();j++) {
//							for(int k=0; k<commentSectionIndex.size();k++){
//								if(j == Integer.parseInt(commentSectionIndex.get(k).toString())){
//									statusId = statusFailID;
//									break;
//								}else if(j==listCommentSection.size()-1){
//									statusId = statusFailID;
//									break;
//								}else{
//									statusId = statusPassID;
//								}
//							}
//							Map singleCase = new HashMap();
//							singleCase.put("case_id", "" + testId);
//							singleCase.put("status_id", statusId);
//							singleCase.put("comment", listCommentSection.get(j));
//							cases.add(singleCase);
//
//						}
//					}
//					String responseReq = JSONValue.toJSONString(data);
//					response  =  (JSONArray) client.sendPost(addResultString,data);
//					commentSectionIndex.clear();
//				}
//				listCommentSection.clear();
//			}
//
//			deleteTestRail(testRailIdList);
//		}catch(IOException ioException){
//			System.err.println(ioException.getMessage());
//		} catch(APIException aPIException){
//			System.err.println(aPIException.getMessage());
//		}
//	}

//	public static void addTestResultWithTestCaseLinkIntoTestRail(int statusID, String comment)
//	{
//		/*
//		 * TestRail Status ID : Description
//		 * 1 : Passed
//		 * 2 : Blocked
//		 * 4 : Retest
//		 * 5 : Failed
//		 */
//		int testCaseId = MyThreadLocal.getTestCaseId();
//		String testName = ExtentTestManager.getTestName(MyThreadLocal.getCurrentMethod());
//		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
//		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
//		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//		int testRailRunId = TestBase.testRailRunId;
//		int suiteTestCaseId = Integer.valueOf(testRailConfig.get("TEST_CASE_SUITE_ID"));
//		int sectionId = ExtentTestManager.getTestCaseSectionId(MyThreadLocal.getCurrentMethod());
//		String addResultString = "add_result_for_case/"+testRailRunId+"/"+testCaseId+"";
//
//		if(testCaseId > 0)
//		{
//			try {
//				// Make a connection with Testrail Server
//				APIClient client = new APIClient(testRailURL);
//				client.setUser(testRailUser);
//				client.setPassword(testRailPassword);
//				JSONObject jSONObject = (JSONObject) client.sendGet("get_case/"+testCaseId);
//				Map<String, Object> data = new HashMap<String, Object>();
//				data.put("status_id", statusID);
//				data.put("comment", comment + " " + "https://legiontech.testrail.io/index.php?/suites/view/" + suiteTestCaseId + "&group_by=cases:section_id&group_order=asc&group_id=" + sectionId);
//				client.sendPost(addResultString, data);
//
//			}catch(IOException ioException){
//				System.err.println(ioException.getMessage());
//			} catch(APIException aPIException){
//				System.err.println(aPIException.getMessage());
//			}
//		}
//
//	}




//	public static int addNUpdateTestCaseIntoTestRun(String testName, int sectionID, int testCaseId)
//	{
//		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
//		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
//		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
//		String testRailSuiteName = testRailConfig.get("TEST_RUN_SUITE_NAME");
//		int suiteId = Integer.valueOf(TestBase.testSuiteID);
////		int suiteId = Integer.valueOf(testRailConfig.get("TEST_CASE_SUITE_ID"));
//
//		int TestRailRunId = 0;
//		int count = 0;
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date =null;
//		String strDate = null;
//
//		String addResultString = "add_run/" + testRailProjectID;
//		try {
//			// Make a connection with TestRail Server
//			APIClient client = new APIClient(testRailURL);
//			client.setUser(testRailUser);
//			client.setPassword(testRailPassword);
//			List cases = new ArrayList();
//			cases.add(new Integer(testCaseId));
//
//			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("title", testName);
//			try{
//				date = format.parse(timestamp.toString());
//				String[] arrDate = format.format(date).split(" ");
//				strDate = arrDate[1];
//			}catch(ParseException e){
//				System.err.println(e.getMessage());
//			}
//			data.put("suite_id", suiteId);
//			data.put("name", testRailSuiteName +" " +strDate);
//			data.put("include_all", true);
//			data.put("case_ids", cases);
//			JSONObject jSONObject = (JSONObject) client.sendPost(addResultString, data);
//			long longTestRailRunId = (Long) jSONObject.get("id");
//			TestRailRunId = (int) longTestRailRunId;
//			TestBase.testRailRunId = TestRailRunId;
//		} catch (IOException ioException) {
//			System.err.println(ioException.getMessage());
//		} catch (APIException aPIException) {
//			System.err.println(aPIException.getMessage());
//		}
//
//		return TestRailRunId;
//
//	}


	//added by Nishant

//	public static int addNUpdateTestCaseIntoTestRun(String testName, int sectionID,JSONArray testCasesList)
//	{
//		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
//		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
//		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
//		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
//		int suiteId = Integer.valueOf(testRailConfig.get("TEST_RAIL_SUITE_ID"));
//
//		int TestRailRunId = 0;
//		int count = 0;
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date =null;
//		String strDate = null;
//
//		String addResultString = "add_run/" + testRailProjectID;
//		try {
//			// Make a connection with TestRail Server
//			APIClient client = new APIClient(testRailURL);
//			client.setUser(testRailUser);
//			client.setPassword(testRailPassword);
//			List cases = new ArrayList();
//
//			cases.add(new Integer(testCaseId));
//
//			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("title", testName);
//			try{
//				date = format.parse(timestamp.toString());
//				String[] arrDate = format.format(date).split(" ");
//				strDate = arrDate[1];
//			}catch(ParseException e){
//				System.err.println(e.getMessage());
//			}
//			data.put("suite_id", suiteId);
//			data.put("name", "Automation Smoke"+"" +strDate);
//			data.put("include_all", false);
////			data.put("case_ids", cases);
//			JSONObject c = (JSONObject) client.sendPost(addResultString, data);
////			JSONObject c = (JSONObject) client.sendGet("get_run/"+testCaseId);
//			long longTestRailRunId = (Long) c.get("id");
//			TestRailRunId = (int) longTestRailRunId;
//			System.out.println(TestRailRunId);
//			setTestRailRunId(TestRailRunId);
//		} catch (IOException ioException) {
//			System.err.println(ioException.getMessage());
//		} catch (APIException aPIException) {
//			System.err.println(aPIException.getMessage());
//		}
//
//
//		return TestRailRunId;
//
//	}


//	public static void takeScreenShotOnFailure(){
//		String targetFile = ScreenshotManager.takeScreenShot();
//		//String screenshotLoc = parameterMap.get("Screenshot_Path") + File.separator + targetFile;
//		String screenshotLoc = parameterMap.get("Screenshot_Path") + File.separator + targetFile;
//		//String screenShotURL = "file:///" + screenshotLoc;
//		String screenShotURL = screenshotLoc;
//		setscreenShotURL(screenShotURL);
//	}

	public static int getDirectoryFilesCount(String directoryPath) {
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		return files.length;
	}

	public static File getLatestFileFromDirectory(String directoryPath) {
		File dir = new File(directoryPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile;
	}


/*
	//added by Nishant

	public static int addNUpdateTestCaseIntoTestRun1(String testName, int sectionID, int testCaseId, ITestContext context)
	{
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
		int suiteId = Integer.valueOf(TestBase.testSuiteID);

		int TestRailRunId = 0;
		int count = 0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =null;
		String strDate = null;

		if((Integer) context.getAttribute("TestRailId")!=null){
			int testRailId = (Integer) context.getAttribute("TestRailId");
//			String addResultString = "update_run/" + getTestRailRunId();
			String addResultString = "update_run/" + testRailId;
			try {
				// Make a connection with TestRail Server
				APIClient client = new APIClient(testRailURL);
				client.setUser(testRailUser);
				client.setPassword(testRailPassword);
				List cases = new ArrayList();
				cases.add(new Integer(testCaseId));
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("title", testName);
//				try{
//					date = format.parse(timestamp.toString());
//					String[] arrDate = format.format(date).split(" ");
//					strDate = arrDate[1];
//					System.out.println(format.format(date));
//				}catch(ParseException e){
//					System.err.println(e.getMessage());
//				}
//
//				data.put("name", "Automation Suite Test Run"+"" +strDate);
				data.put("suite_id", suiteId);
				data.put("include_all", false);
				data.put("case_ids", cases);
				JSONObject c = (JSONObject) client.sendPost(addResultString, data);
//			JSONObject c = (JSONObject) client.sendGet("get_run/"+testCaseId);
				long longTestRailRunId = (Long) c.get("id");
				TestRailRunId = (int) longTestRailRunId;
				TestBase.testRailRunId = TestRailRunId;
			} catch (IOException ioException) {
				System.err.println(ioException.getMessage());
			} catch (APIException aPIException) {
				System.err.println(aPIException.getMessage());
			}
		}else {
			String addResultString = "add_run/" + testRailProjectID;
			try {
				// Make a connection with TestRail Server
				APIClient client = new APIClient(testRailURL);
				client.setUser(testRailUser);
				client.setPassword(testRailPassword);
				List cases = new ArrayList();
				cases.add(new Integer(testCaseId));

				Map<String, Object> data = new HashMap<String, Object>();
				data.put("title", testName);
				try{
					date = format.parse(timestamp.toString());
					String[] arrDate = format.format(date).split(" ");
					strDate = arrDate[1];
				}catch(ParseException e){
					System.err.println(e.getMessage());
				}
				data.put("suite_id", suiteId);
				data.put("name", "Automation - Regression " + strDate);
				data.put("include_all", false);
				data.put("case_ids", cases);
				JSONObject c = (JSONObject) client.sendPost(addResultString, data);
//			JSONObject c = (JSONObject) client.sendGet("get_run/"+testCaseId);
				long longTestRailRunId = (Long) c.get("id");
				TestRailRunId = (int) longTestRailRunId;
				TestBase.testRailRunId = TestRailRunId;
				context.setAttribute("TestRailId", TestBase.testRailRunId);
			} catch (IOException ioException) {
				System.err.println(ioException.getMessage());
			} catch (APIException aPIException) {
				System.err.println(aPIException.getMessage());
			}
		}

		return TestRailRunId;

	}


	public static int addNUpdateTestCaseIntoTestRun2(String testName, int sectionID, int testCaseId, ITestContext context)
	{
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
		int suiteId = Integer.valueOf(TestBase.testSuiteID);

		int TestRailRunId = 0;
		int count = 0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =null;
		String strDate = null;

		String addResultString = "add_run/" + testRailProjectID;
		try {
			// Make a connection with TestRail Server
			APIClient client = new APIClient(testRailURL);
			client.setUser(testRailUser);
			client.setPassword(testRailPassword);
			List cases = new ArrayList();
			cases.add(new Integer(testCaseId));

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("title", testName);
			try{
				date = format.parse(timestamp.toString());
				String[] arrDate = format.format(date).split(" ");
				strDate = arrDate[1];
			}catch(ParseException e){
				System.err.println(e.getMessage());
			}
			data.put("suite_id", suiteId);
			data.put("name", "Automation Smoke - Regression " + strDate);
			data.put("include_all", false);
			data.put("case_ids", cases);
			JSONObject c = (JSONObject) client.sendPost(addResultString, data);
//			JSONObject c = (JSONObject) client.sendGet("get_run/"+testCaseId);
			long longTestRailRunId = (Long) c.get("id");
			TestRailRunId = (int) longTestRailRunId;
			TestBase.testRailRunId = TestRailRunId;
			List<Integer> testRailId =  new ArrayList<Integer>();
			List<Integer> testRailIdMaster =  new ArrayList<Integer>();
			testRailId.add(TestRailRunId);

			if(context.getAttribute("TestRailId")!=null) {
				testRailIdMaster.add(TestRailRunId);
				String myList = context.getAttribute("TestRailId").toString()
						.replace("[","").replace("]","").replace(" ","");
				String[] arrMyList = myList.split(",");
				for(int i=0; i<arrMyList.length;i++){
					testRailIdMaster.add(Integer.parseInt(arrMyList[i]));
				}
//				testRailIdMaster.add( Integer.parseInt(context.getAttribute("TestRailId").toString().replace("[","").replace("]","")));
				//setTestRailRun(testRailIdMaster);
			}else{
				setTestRailRun(testRailId);
			}

//			context.setAttribute("TestRailId", getTestRailRunId());
			context.setAttribute("TestRailId", TestBase.testRailRunId);
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		} catch (APIException aPIException) {
			System.err.println(aPIException.getMessage());
		}

		return TestRailRunId;

	}




	public void addAttachments(int statusID, String comment){
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
		int suiteId = Integer.valueOf(TestBase.testSuiteID);
		String addResultString = "add_attachment_to_result_for_case/"+testCaseId+"";

		try {
			// Make a connection with Testrail Server
			APIClient client = new APIClient(testRailURL);
			client.setUser(testRailUser);
			client.setPassword(testRailPassword);
			JSONObject jSONObject = (JSONObject) client.sendGet("get_case/"+testCaseId);
			if(statusID == 5) {
				Map<String, Object> data = new HashMap<String, Object>();
				takeScreenShotOnFailure();
				String finalLink = getscreenShotURL();
				data.put("status_id", statusID);
				data.put("comment", comment +"\n" + "[Link To ScreenShot]" +"("+finalLink +")");
//					data.put("screen_shot", getscreenShotURL());
				client.sendPost(addResultString, data);
			}else{
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("status_id", statusID);
				data.put("comment", comment);
				client.sendPost(addResultString, data);
			}

		}catch(IOException ioException){
			System.err.println(ioException.getMessage());
		}
		catch(APIException aPIException){
			System.err.println(aPIException.getMessage());
		}

	}

*/



	public static boolean convertYesOrNoToTrueOrFalse(String yesOrNo) {
		if(yesOrNo.toLowerCase().contains("yes"))
			return true;
		return false;
	}


	public static String convertTimeIntoHHColonMM(String timeDuration){
		if(timeDuration.contains(":")){
			timeDuration = timeDuration;
		}else{
			String numericTimeValue = timeDuration.replaceAll("[^0-9]","");
			String stringValue = timeDuration.replaceAll("[0-9]","");
			timeDuration = numericTimeValue + ":00" + stringValue;
		}
		return timeDuration;
	}


/*
	public static void addTestCaseIntoTestRun(ITestContext context)
	{
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
		int suiteId = Integer.valueOf(TestBase.testSuiteID);

		String testRailId = context.getAttribute("TestRailId").toString()
				.replace("[","").replace("]","").replace(" ","");
		String[] arrTestRailId = testRailId.split(",");
		String addResultString = "add_run/" + testRailProjectID;
		Object id = null;
		Object testName = null;
		int TestRailRunId = 0;
		List<Integer> testCaseList = new ArrayList<>();
		List<String> titleNameList = new ArrayList<>();
		for(int i=0; i<arrTestRailId.length;i++) {
			String addResult = "get_tests/" + Integer.parseInt(arrTestRailId[i]);
			try {
				// Make a connection with TestRail Server
				APIClient client = new APIClient(testRailURL);
				client.setUser(testRailUser);
				client.setPassword(testRailPassword);
				JSONArray testCasesList = (JSONArray) client.sendGet(addResult);
				id = ((JSONObject) testCasesList.get(0)).get("case_id");
				testName = ((JSONObject) testCasesList.get(0)).get("title");
				testCaseList.add(((Long) id).intValue());
//				long longTestRailRunId = (Long) c.get("id");
//				int TestRailRunId = (int) id;
				titleNameList.add((String) testName);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

	}

	public static int updateTestCaseIntoTestRunSample(String testName, ITestContext context, List<Integer> testCaseIDList)
	{
		String testRailURL = getTestRailURL();
		String testRailUser = getTestRailUser();
		String testRailPassword = getTestRailPassword();
		int suiteId = Integer.valueOf(TestBase.testSuiteID);
//		int suiteId = Integer.valueOf(testRailConfig.get("TEST_CASE_SUITE_ID"));
		//int TestRailRunId = 0;
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
			List cases = new ArrayList();
//				cases.add(new Integer(4375));

			Map<String, Object> data = new HashMap<String, Object>();
			try{
				date = format.parse(timestamp.toString());
				//String[] arrDate = format.format(date).split(" ");
				//strDate = arrDate[1];
				strDate = format.format(date);
			}catch(ParseException e){
				System.err.println(e.getMessage());
			}
//				data.put("results",cases);
//				int[] testIds ={4375,4808};
//				for ( int testId : testIds )
//				{
//					Map singleCase = new HashMap();
//					singleCase.put("case_ids", "" + testId);
//					singleCase.put("suite_id", suiteId);
//					singleCase.put("name", "Sample Automation Suite");
//					singleCase.put("include_all", false);
//					singleCase.put("title", testName);
////					singleCase.put("comment", comment);
//					cases.add(singleCase);
//				}
//				data.put("title", testName);
			//data.put("suite_id", suiteId);
			data.put("include_all", false);
			data.put("suite_id", suiteId);
			if (TestBase.finalTestRailRunName==null||TestBase.finalTestRailRunName.equals("")){
				name = "Automation - Regression " + strDate;
			} else {
				name = TestBase.finalTestRailRunName+ " " + strDate;
			}
			data.put("name", name);
			data.put("case_ids", testCaseIDList);
			String responseReq = JSONValue.toJSONString(data);
			JSONObject jSONObject = (JSONObject) client.sendPost(addResultString, data);
			long longTestRailRunId = (Long) jSONObject.get("id");
			//add test rail run ID=================================
			TestBase.testRailRunId = (int) longTestRailRunId;
			List<Integer> testRailId =  new ArrayList<Integer>();
			List<Integer> testRailIdMaster =  new ArrayList<Integer>();
			testRailId.add(TestBase.testRailRunId);
			List<String> testNameList = new ArrayList<String>();
//			testNameList.add(testName);
//			context.setAttribute("TestName", testNameList);
			if(context.getAttribute("TestName")!=null) {
				if(context.getAttribute("TestName").toString().split(",").length == 1){
					String myList = context.getAttribute("TestName").toString()
							.replace("[","").replace("]","") + "\n" + testName;
					String[] arrMyList = myList.split("\n");
					for(int i=0; i<arrMyList.length;i++){
						testNameList.add(arrMyList[i]);
					}
					setTestName(testNameList);
				}else{
					String myList = context.getAttribute("TestName").toString().
							replace("[","").replace("]","") + "," + testName;
					String[] arrMyList = myList.split(",");
					for(int i=0; i<arrMyList.length;i++){
						testNameList.add(arrMyList[i]);
					}
					setTestName(testNameList);
				}

			}else{
				setTestName(Arrays.asList(testName));
			}
			context.setAttribute("TestName", getTestName());
			if(context.getAttribute("TestRailId")!=null) {
				testRailIdMaster.add(TestRailRunId);
				String myList = context.getAttribute("TestRailId").toString()
						.replace("[","").replace("]","").replace(" ","");
				String[] arrMyList = myList.split(",");
				for(int i=0; i<arrMyList.length;i++){
					testRailIdMaster.add(Integer.parseInt(arrMyList[i]));
				}
				//setTestRailRun(testRailIdMaster);
			}else{
				setTestRailRun(testRailId);
			}
			context.setAttribute("TestRailId", TestBase.testRailRunId);
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		} catch (APIException aPIException) {
			System.err.println(aPIException.getMessage());
		}
		return TestBase.testRailRunId;

	}

	public static int addNUpdateTestCaseIntoTestRun()
	{
		String testRailURL = testRailConfig.get("TEST_RAIL_URL");
		String testRailUser = testRailConfig.get("TEST_RAIL_USER");
		String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
		String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
		String testRailSuiteName = testRailConfig.get("TEST_RUN_SUITE_NAME");
		int suiteId = Integer.valueOf(TestBase.testSuiteID);
		//int suiteId = Integer.valueOf(testRailConfig.get("TEST_RAIL_SUITE_ID"));
//		int suiteId = Integer.valueOf(testRailConfig.get("TEST_CASE_SUITE_ID"));

		int TestRailRunId = 0;
		int count = 0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =null;
		String strDate = null;

		String addResultString = "add_run/" + testRailProjectID;
		try {
			// Make a connection with TestRail Server
			APIClient client = new APIClient(testRailURL);
			client.setUser(testRailUser);
			client.setPassword(testRailPassword);
			Map<String, Object> data = new HashMap<String, Object>();
			try{
				date = format.parse(timestamp.toString());
				String[] arrDate = format.format(date).split(" ");
				strDate = arrDate[1];
			}catch(ParseException e){
				System.err.println(e.getMessage());
			}
			data.put("suite_id", suiteId);
			data.put("name", testRailSuiteName +" " +strDate);
			data.put("include_all", true);
			JSONObject jSONObject = (JSONObject) client.sendPost(addResultString, data);
			long longTestRailRunId = (Long) jSONObject.get("id");
			TestRailRunId = (int) longTestRailRunId;
			System.out.println(TestRailRunId);
			TestBase.testRailRunId = TestRailRunId;
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		} catch (APIException aPIException) {
			System.err.println(aPIException.getMessage());
		}

		return TestRailRunId;

	}
*/

	//added by Nishant

	public static void deleteTestRail(List<Integer> testRailIdList)
	{
		String testRailURL = TestRailOperation.getTestRailURL();
		String testRailUser = TestRailOperation.getTestRailUser();
		String testRailPassword = TestRailOperation.getTestRailPassword();

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


	//added by Nishant

	public static int generateRandomNumbers(){
		Random rand = new Random();
		int rand_int = rand.nextInt(1000);
		return rand_int;
	}

	public static String getUserName(){
		String strUser = "nishant+" + generateRandomNumbers() + "@legion.co";
		return strUser;
	}

		//added by Nishant for getSpecificTestRailId

        public static void getTestRailId() {
            String testRailURL = testRailConfig.get("TEST_RAIL_URL");
            String testRailUser = testRailConfig.get("TEST_RAIL_USER");
            String testRailPassword = testRailConfig.get("TEST_RAIL_PASSWORD");
            String testRailProjectID = testRailConfig.get("TEST_RAIL_PROJECT_ID");
            String testRailSuiteName = testRailConfig.get("TEST_RUN_SUITE_NAME");
            int suiteId = Integer.valueOf(TestBase.testSuiteID);
            String addResultString = "get_runs/" + testRailProjectID;
            JSONObject jsonTestRailName;
            List<Integer> testCaseIDList = new ArrayList<>();
    //        JSONArray testRailIdList;

            try {
                // Make a connection with TestRail Server
                APIClient client = new APIClient(testRailURL);
                client.setUser(testRailUser);
                client.setPassword(testRailPassword);
                JSONArray testRailList= (JSONArray) client.sendGet(addResultString);
                for(Object testRailId : testRailList)
                {

                    jsonTestRailName = (JSONObject) testRailId;
                    if(jsonTestRailName.get("name").toString().contains("CORE"))
                    {
                        long longTestRailID = (Long) jsonTestRailName.get("id");
                        int testRailID = (int)longTestRailID;
                        testCaseIDList.add(testRailID);
                        }

                    }

                System.out.println("Print Test Rail Id");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            deleteTestRail(testCaseIDList);
    //		return testCaseIDList;

        }

	public static String getThisWeekTimeInterval(Date  date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

		String imptimeBegin = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());
		return imptimeBegin + "," + imptimeEnd;
	}

	public static boolean isDateInTimeDuration(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime()
				|| nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSameDayComparingTwoDays(String dateString1, String dateString2, SimpleDateFormat format1,
													SimpleDateFormat format2) throws ParseException {
		Date date1 = format1.parse(dateString1);
		Date date2 = format2.parse(dateString2);
		return DateUtils.isSameDay(date1, date2);
	}

	public static boolean isNumeric(String str){
		//Pattern pattern = Pattern.compile("^[0-9]*");
		Pattern pattern = Pattern.compile("^[0-9]+((\\.[0-9]{1,2})|[0-9]{0,2})$");
		Matcher isNum = pattern.matcher(str.trim());
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	public static int getMinutesFromTime(String time) {
		int minutes = 0;
		if (time.contains(":")) {
			String minute = time.split(":")[1].substring(0, time.split(":")[1].length()-2).trim();
			minutes = (Integer.parseInt(time.split(":")[0].trim())) * 60 + Integer.parseInt(minute);
		}else {
			minutes = Integer.parseInt(time.substring(0, time.length()-2)) * 60;
		}
		if (time.toLowerCase().endsWith("pm")) {
			minutes += 12 * 60;
		}
		return minutes;
	}

	public static String convertMinutesToTime(int minutes) {
		if (minutes == 720) {
			return "12:00pm";
		} else if (minutes < 720) {
			return minutes/60 + ":" + (minutes%60 == 0 ? "00" : minutes%60) + "am";
		} else {
			return (minutes - 720)/60 + ":" + ((minutes - 720)%60 == 0 ? "00" : (minutes - 720)%60) + "pm";
		}
	}

	public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
		if (n > (max - min + 1) || max < min) {
			return;
		}
		for (int i = 0; i < n; i++) {
			int num = (int) (Math.random() * (max - min)) + min;
			set.add(num);
		}
		int setSize = set.size();
		if (setSize < n) {
			randomSet(min, max, n - setSize, set);
		}
	}

	public static boolean compareHashMapByEntrySet(HashMap<String, List<String>> map1, HashMap<String, List<String>> map2){
		if(map1.size()!=map2.size()){
			return false;
		}
		List<String> tmp1;
		List<String> tmp2;
		boolean isSame = false;
		for(Map.Entry<String, List<String>> entry : map1.entrySet()){
			if(map2.containsKey(entry.getKey())){
				tmp1 = entry.getValue();
				tmp2 = map2.get(entry.getKey());
				if(tmp1 != null && tmp2 != null){
					if(tmp1.containsAll(tmp2) && tmp2.containsAll(tmp1)){
						isSame = true;
						continue;
					}else{
						isSame = false;
						break;
					}
				}else if(tmp1 == null && tmp2 == null){
					isSame = true;
					continue;
				}else{
					isSame = false;
					break;
				}
			}else{
				isSame = false;
				break;
			}
		}
		return isSame;
	}

	public static int getWeekStartDayOfTheYear(String wholeDate, String dateFormat) {
		int weekStartDayOfTheYear = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date date = sdf.parse(wholeDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayOfWeek);
			weekStartDayOfTheYear = cal.get(Calendar.DAY_OF_YEAR);
		} catch (ParseException pe) {
			SimpleUtils.fail("Failed to parse date: " + wholeDate + " to format: " + dateFormat, false);
		}
		return weekStartDayOfTheYear;
	}
}
