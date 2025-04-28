package com.legion.utils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.apache.xpath.operations.Bool;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class MyThreadLocal {

	public static final ThreadLocal<EventFiringWebDriver> driver = new ThreadLocal<>();
	public static final ThreadLocal<File> file = new ThreadLocal<>();
	public static final ThreadLocal<String> loc = new ThreadLocal<>();
	public static final ThreadLocal<Integer> screenNum = new ThreadLocal<>();
	public static final ThreadLocal<HashMap<ITestResult, List<Throwable>>> verificationFailuresMap = new ThreadLocal<>();
	public static final ThreadLocal<String> driver_type = new ThreadLocal<>();
	public static final ThreadLocal<String> environment = new ThreadLocal<>();
	public static final ThreadLocal<String> url = new ThreadLocal<>();
	public static final ThreadLocal<File> ieDriver = new ThreadLocal<>();
	public static final ThreadLocal<File> chromeDriver = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> browserNeeded = new ThreadLocal<>();
	public static final ThreadLocal<String> currentTestMethod = new ThreadLocal<>();
	public static final ThreadLocal<HashMap<String, String>> method = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> beforeInvocationCalled = new ThreadLocal<>();
	public static final ThreadLocal<String> enterprise = new ThreadLocal<>();
	public static final ThreadLocal<Method> currentMethod = new ThreadLocal<>();
	public static final ThreadLocal<Integer> totalSuiteTestCases = new ThreadLocal<>();
	public static final ThreadLocal<Integer> totalSuiteTestCounter = new ThreadLocal<>();
	public static final ThreadLocal<String> sessionID = new ThreadLocal<>();
	public static final ThreadLocal<String> sessionTimestamp = new ThreadLocal<>();
	public static final ThreadLocal<String> version = new ThreadLocal<>();
	public static final ThreadLocal<String> os = new ThreadLocal<>();
	public static final ThreadLocal<String> testMethodName = new ThreadLocal<>();
	public static void setCurrentTestMethodName(String value) { testMethodName.set(value); }
	public static String getCurrentTestMethodName() { return testMethodName.get(); }
	public static ThreadLocal<String> consoleName = new ThreadLocal<>();
	public static final ThreadLocal<String> scheduleHoursStartTime = new ThreadLocal<>();
	public static final ThreadLocal<String> scheduleHoursEndTime = new ThreadLocal<>();
	public static ThreadLocal<Integer> testCaseId = new ThreadLocal<>();
	public static ThreadLocal<Integer> testRailRunId = new ThreadLocal<>();
	public static ThreadLocal<List<String>> testName = new ThreadLocal<>();
	public static ThreadLocal<List<String>> commentSection = new ThreadLocal<>();
	public static ThreadLocal<String> comment = new ThreadLocal<>();
	public static ThreadLocal<List<String>> failedComment = new ThreadLocal<>();
	public static ThreadLocal<List<Integer>> testRailRun = new ThreadLocal<>();
	public static final ThreadLocal<String> teamMemberName = new ThreadLocal<>();
	public static final ThreadLocal<String> workerRole = new ThreadLocal<>();
	public static final ThreadLocal<String> workerStatus = new ThreadLocal<>();
	public static final ThreadLocal<String> workerStatusDes = new ThreadLocal<>();
	public static final ThreadLocal<String> workerLocation = new ThreadLocal<>();
	public static final ThreadLocal<String> workerShiftTime = new ThreadLocal<>();
	public static final ThreadLocal<String> workerShiftDuration = new ThreadLocal<>();
	public static final ThreadLocal<String> screenshotLoc = new ThreadLocal<>();
	public static ThreadLocal<String> screenShotURL = new ThreadLocal<>();
	public static final ThreadLocal<String> timeOffStartTime = new ThreadLocal<>();
	public static final ThreadLocal<String> timeOffEndTime = new ThreadLocal<>();
	public static final ThreadLocal<String> currentComplianceTemplate = new ThreadLocal<>();
	public static final ThreadLocal<String> currentOperatingTemplate = new ThreadLocal<>();
	public static final ThreadLocal<String> testSuiteID = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> ifAddNewTestRun = new ThreadLocal<>();
	public static final ThreadLocal<String> testRailRunName = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> testCaseExistsFlag = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> testResultFlag = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> testSkippedFlag = new ThreadLocal<>();
	public static final ThreadLocal<List<Integer>> CurrentTestCaseIDList = new ThreadLocal<>();
	public static final ThreadLocal<String> testSuiteName = new ThreadLocal<>();
	public static final ThreadLocal<String> timeDuration = new ThreadLocal<>();
	public static final ThreadLocal<String> moduleName = new ThreadLocal<>();
	public static final ThreadLocal<Integer> sectionID = new ThreadLocal<>();
	public static final ThreadLocal<Integer> budgetTolerance = new ThreadLocal<>();
	public static final ThreadLocal<String> testRailReporting = new ThreadLocal<>();
	public static final ThreadLocal<String> location = new ThreadLocal<>();
	public static final ThreadLocal<HashMap<String, String>> templateTypeName = new ThreadLocal<>();
	public static final ThreadLocal<String> LGMSLocationName = new ThreadLocal<>();
	public static final ThreadLocal<String> LGMSChildLocationName = new ThreadLocal<>();
	public static final ThreadLocal<String> LGPTPLocationName = new ThreadLocal<>();
	public static final ThreadLocal<String> LGPTPChildLocationName = new ThreadLocal<>();
	public static final ThreadLocal<String> LGMSNsoLocationName = new ThreadLocal<>();
	public static final ThreadLocal<String> LGPTPNsoLocationName = new ThreadLocal<>();
	public static final ThreadLocal<String> job = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> isNeedUpdateOperatingHours = new ThreadLocal<>();
	//added by Estelle to catch up test rail login user info
	//public static final ThreadLocal<String> testRailURL = new ThreadLocal<>();
	//public static final ThreadLocal<String> testRailUser = new ThreadLocal<>();
	//public static final ThreadLocal<String> testRailPassword = new ThreadLocal<>();
	public static final ThreadLocal<String> testRailProjectID = new ThreadLocal<>();
	public static final ThreadLocal<String> emailAccount = new ThreadLocal<>();
	public static final ThreadLocal<String> firstName = new ThreadLocal<>();
	public static final ThreadLocal<String> lastName = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> isCompanyPolicySet = new ThreadLocal<>();
	public static final ThreadLocal<String> phone = new ThreadLocal<>();
	public static final ThreadLocal<String> employeeId = new ThreadLocal<>();
	public static final ThreadLocal<String> consoleHandle = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> isWFSEnabled = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> isAssignTM = new ThreadLocal<>();
	public static final ThreadLocal<String> messageOfTMScheduledStatus = new ThreadLocal<>();
	public static final ThreadLocal<HashMap<String, Integer>> weekDaysNDates = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> isNewCreateShiftUIEnabled = new ThreadLocal<>();

	public static void setIsNeedEditingOperatingHours(Boolean value) { isNeedUpdateOperatingHours.set(value); }

	public static Boolean getIsNeedEditingOperatingHours() { return isNeedUpdateOperatingHours.get(); }

	public static void setScreenshotLocation(String value) { screenshotLoc.set(value); }

	public static String getScreenshotLocation() { return screenshotLoc.get(); }

	public static void setTestRailReporting(String value) { testRailReporting.set(value); }

	public static String getTestRailReporting() { return testRailReporting.get(); }

	public static void setscreenShotURL(String value) { screenShotURL.set(value); }
	public static String getscreenShotURL() { return screenShotURL.get(); }

	public static void setTeamMemberName(String value) { teamMemberName.set(value); }

	public static String getTeamMemberName() { return teamMemberName.get(); }

	public static void setWorkerRole(String value) { workerRole.set(value); }

	public static String getWorkerRole() { return workerRole.get(); }

	public static void setWorkerLocation(String value) { workerLocation.set(value); }

	public static String getWorkerLocation() { return workerLocation.get(); }

	public static void setWorkerStatus(String value) { workerStatus.set(value); }

	public static String getWorkerStatus() { return workerStatus.get(); }

	public static void setWorkerStatusDes(String value) { workerStatusDes.set(value); }

	public static String getWorkerStatusDes() { return workerStatusDes.get(); }

	public static void setWorkerShiftTime(String value) { workerShiftTime.set(value); }

	public static String getWorkerShiftTime() { return workerShiftTime.get(); }

	public static void setWorkerShiftDuration(String value) { workerShiftDuration.set(value); }

	public static String getWorkerShiftDuration() { return workerShiftDuration.get(); }

	public static void setTimeOffStartTime(String value) { timeOffStartTime.set(value); }

	public static String getTimeOffStartTime() { return timeOffStartTime.get(); }

	public static void setModuleName(String value) { moduleName.set(value); }

	public static String getModuleName() { return moduleName.get(); }

	public static void setTimeOffEndTime(String value) { timeOffEndTime.set(value); }

	public static String getTimeOffEndTime() { return timeOffEndTime.get(); }

	public static void setCurrentComplianceTemplate(String value) { currentComplianceTemplate.set(value); }

	public static String getCurrentComplianceTemplate() { return currentComplianceTemplate.get(); }

	public static void setCurrentOperatingTemplate(String value) { currentOperatingTemplate.set(value); }

	public static String getCurrentOperatingTemplate() { return currentOperatingTemplate.get(); }

	public static void setTestSuiteID(String value) { testSuiteID.set(value); }

	public static String getTestSuiteID() { return testSuiteID.get(); }

	public static void setIfAddNewTestRun(boolean value) { ifAddNewTestRun.set(value); }

	public static boolean getIfAddNewTestRun() { return ifAddNewTestRun.get(); }

	public static void setTestRailRunName(String value) { testRailRunName.set(value); }

	public static String getTestRailRunName() { return testRailRunName.get(); }

	public static void setTestCaseExistsFlag(boolean value) { testCaseExistsFlag.set(value); }

	public static boolean getTestCaseExistsFlag() {
		if (testCaseExistsFlag != null) {
			return testCaseExistsFlag.get();
		} else {
			return false;
		}
	}

	public static void setTestResultFlag(boolean value) { testResultFlag.set(value); }

	public static boolean getTestResultFlag() { return testResultFlag.get(); }

	public static void setTestSkippedFlag(boolean value) { testSkippedFlag.set(value); }

	public static boolean getTestSkippedFlag() {
		if (testSkippedFlag != null) {
			return testSkippedFlag.get();
		} else {
			return false;
		}
	}

	public static void setCurrentTestCaseIDList(List<Integer> value) { CurrentTestCaseIDList.set(value); }

	public static List<Integer> getCurrentTestCaseIDList() { return CurrentTestCaseIDList.get(); }

	public static void setTestSuiteName(String value) { testSuiteName.set(value); }

	public static String getTestSuiteName() { return testSuiteName.get(); }

	public static void setScheduleHoursStartTime(String value) { scheduleHoursStartTime.set(value); }

	public static String getScheduleHoursStartTime() { return scheduleHoursStartTime.get(); }
	public static void setScheduleHoursEndTime(String value) { scheduleHoursEndTime.set(value); }
	public static String getScheduleHoursEndTime() { return scheduleHoursEndTime.get(); }

	public static void setScheduleHoursTimeDuration(String value) { timeDuration.set(value); }
	public static String getScheduleHoursTimeDuration() { return timeDuration.get(); }

	public static void setTestCaseId(Integer value) {
		testCaseId.set(value);
	}

	public static Integer getTestCaseId() {
		return testCaseId.get();
	}


	public static void setSectionID(Integer value) {
		sectionID.set(value);
	}

	public static Integer getSectionID() {
		return sectionID.get();
	}

	public static void setBudgetTolerance(Integer value) {
		budgetTolerance.set(value);
	}

	public static Integer getBudgetTolerance() {
		return budgetTolerance.get();
	}
	public static void setTestRailRunId(Integer value) {
		testRailRunId.set(value);
	}

	public static Integer getTestRailRunId() {
		return testRailRunId.get();
	}

	public static void setTestName(List<String> value) {
		testName.set(value);
	}

	public static List<String> getTestName() {
		return testName.get();
	}

	public static void setCommentSection(List<String> value) {
		commentSection.set(value);
	}

	public static List<String> getCommentSection() {
		return commentSection.get();
	}
	public static void setComment(String value) {
		comment.set(value);
	}

	public static String getComment() {
		return comment.get();
	}

	public static void setFailedComment(List<String> value) {
		failedComment.set(value);
	}

	public static List<String> getFailedComment() {
		return failedComment.get();
	}

	public static void setTestRailRun(List<Integer> value) {
		testRailRun.set(value);
	}

	public static List<Integer> getTestRailRun() {
		return testRailRun.get();
	}
	public static final ThreadLocal<AndroidDriver<MobileElement>> android_driver = new ThreadLocal<>();

	public static final ThreadLocal<AndroidDriver<MobileElement>> android_driver_web = new ThreadLocal<>();
	public static ThreadLocal<String> platformname = new ThreadLocal<>();

	public static void setTotalSuiteTestCases(Integer value) {
		totalSuiteTestCases.set(value);
	}

	public static Integer getTotalSuiteTestCases() {
		return totalSuiteTestCases.get();
	}

	public static void setTotalSuiteTestCounter(Integer value) {
		totalSuiteTestCounter.set(value);
	}

	public static Integer getTotalSuiteTestCounter() {
		return totalSuiteTestCounter.get();
	}

	public static void setCurrentMethod(Method value) {
		currentMethod.set(value);
	}

	public static Method getCurrentMethod() {
		return currentMethod.get();
	}

	public static void setBeforeInvocationCalled(Boolean value) {
		beforeInvocationCalled.set(value);
	}

	public static Boolean getBeforeInvocationCalled() {
		return beforeInvocationCalled.get();
	}

	public static void setMethod(HashMap<String, String> methodMap) {
		method.set(methodMap);
	}

	public static HashMap<String, String> getMethod() {
		return method.get();
	}

	public static void setCurrentTestMethod(String value) {
		currentTestMethod.set(value);
	}

	public static String getCurrentTestMethod() {
		return currentTestMethod.get();
	}

	public static void setBrowserNeeded(boolean _browserNeeded) {
		browserNeeded.set(_browserNeeded);
	}

	public static boolean getBrowserNeeded() {
		return browserNeeded.get();
	}

	public static void setEnvironment(String _environment) {
		environment.set(_environment);
	}

	public static String getEnvironment() {
		return environment.get();
	}

	public static void setChromeDriver(File _file) {
		chromeDriver.set(_file);
	}

	public static File getChromeDriver() {
		return chromeDriver.get();
	}

	public static void setIEDriver(File _file) {
		ieDriver.set(_file);
	}

	public static File getIEDriver() {
		return ieDriver.get();
	}

	public static void setURL(String _url) {
		url.set(_url);
	}

	public static String getURL() {
		return url.get();
	}

	public static void setLoc(String _loc) {
		loc.set(_loc);
	}

	public static String getLoc() {
		return loc.get();
	}

	public static void setDriverType(String driverType) {
		driver_type.set(driverType);
		MyThreadLocal.setVerificationMap(new HashMap<ITestResult, List<Throwable>>());
	}

	public static String getDriverType() {
		return driver_type.get();
	}


	public static void setVerificationMap(HashMap<ITestResult, List<Throwable>> vMap) {
		verificationFailuresMap.set(vMap);
	}

	public static HashMap<ITestResult, List<Throwable>> getVerificationMap() {
		return verificationFailuresMap.get();
	}

	public static void setFile(File _file) {
		file.set(_file);
	}

	public static File getFile() {
		return file.get();
	}

	public static void setScreenNum(int _screenNum) {
		screenNum.set(_screenNum);
	}

	public static int getScreenNum() {
		return screenNum.get();
	}

	public static void setDriver(EventFiringWebDriver _driver) {
		driver.set(_driver);
	}

	public static void setDriver(WebDriver _driver) {
		driver.set(new EventFiringWebDriver(_driver));
	}

	public static EventFiringWebDriver getDriver() {
		return driver.get();
	}

	public static void setAndroidDriver(AndroidDriver<MobileElement> _driver) {
		android_driver.set(_driver);
	}

	public static AndroidDriver<MobileElement> getAndroidDriver() {
		return android_driver.get();
	}

	public static void setAndroidWebDriver(AndroidDriver<MobileElement> _driver) {
		android_driver_web.set(_driver);
	}

	public static AndroidDriver<MobileElement> getAndroidWebDriver() {
		return android_driver_web.get();
	}

	public static void setEnterprise(String _enterprise) {
		enterprise.set(_enterprise);
	}

	public static String getEnterprise() {
		return enterprise.get();
	}

	public static void setSessionID(String _sessionID) {
		sessionID.set(_sessionID);
	}

	public static String getSessionID() {
		return sessionID.get();
	}

	public static void setSessionTimestamp(String _sessionTimestamp) {
		sessionTimestamp.set(_sessionTimestamp);
	}

	public static String getSessionTimestamp() {
		return sessionTimestamp.get();
	}

	public static void setVersion(String _version) {
		version.set(_version);
	}

	public static String getVersion() {
		return version.get();
	}

	public static void setOS(String _os) {
		os.set(_os);
	}

	public static String getOS() {
		return os.get();
	}


	//set screenshot Console name
	public static void setScreenshotConsoleName(String screenshotFolder){
		consoleName.set(screenshotFolder);
	}

	//get screenshot Console name
	public static String getScreenshotConsoleName(){
		return consoleName.get();
	}


	//set screenshot Console name
	public static void setPlatformName(String value){
		platformname.set(value);
	}

	//get screenshot Console name
	public static String getPlatformName(){
		return platformname.get();
	}

	//set/get location info
	public static void setLocationName(String locationName) { location.set(locationName); }

	public static String getLocationName() { return location.get(); }

	public static void setTemplateTypeAndName(HashMap<String, String> templateTypeAndName) {templateTypeName.set(templateTypeAndName); }
	public static HashMap<String, String> getTemplateTypeAndName() { return templateTypeName.get(); }

	public static void setLGMSLocationName(String value) { LGMSLocationName.set(value); }
	public static void setLGMSChildLocationName(String value) { LGMSChildLocationName.set(value); }

	public static String getLGMSLocationName() { return LGMSLocationName.get(); }
	public static String getLGMSChildLocationName() { return LGMSChildLocationName.get(); }

	public static void setLGPTPLocationName(String value) { LGPTPLocationName.set(value); }
	public static void setLGPTPChildLocationName(String value) { LGPTPChildLocationName.set(value); }
	public static String getLGPTPLocationName() { return LGPTPLocationName.get(); }
	public static String getLGPTPChildLocationName() { return LGPTPChildLocationName.get(); }
	//NSO location info
	public static void setLGMSNsoLocationName(String value) { LGMSNsoLocationName.set(value); }

	public static String getLGMSNsoLocationName() { return LGMSNsoLocationName.get(); }

	public static void setLGPTPNsoLocationName(String value) { LGPTPNsoLocationName.set(value); }

	public static String getLGPTPNsoLocationName() { return LGPTPNsoLocationName.get(); }

	//added by Estelle for jobs
	public static void setJobName(String jobName) { job.set(jobName); }

	public static String getJobName() { return job.get(); }

	//public static void setTestRailURL(String value) { testRailURL.set(value); }
	//public static String getTestRailURL() { return testRailURL.get(); }
	//public static void setTestRailUser(String value){ testRailUser.set(value); }
	//public static String getTestRailUser() { return testRailUser.get(); }
	//public static void setTestRailPassword(String value){ testRailPassword.set(value); }
	//public static String getTestRailPassword() { return testRailPassword.get(); }
	public static void setTestRailProjectID(String value){ testRailProjectID.set(value); }
	public static String getTestRailProjectID() { return testRailProjectID.get(); }
	public static void setEmailAccount(String value) { emailAccount.set(value);}
	public static String getEmailAccount() { return emailAccount.get();}
	public static void setFirstNameForNewHire(String value) { firstName.set(value);}
	public static String getFirstNameForNewHire() { return firstName.get();}
	public static void setLastNameForNewHire(String value) { lastName.set(value);}
	public static String getLastNameForNewHire() { return lastName.get();}
	public static void setCompanyPolicy(Boolean value) { isCompanyPolicySet.set(value);}
	public static Boolean getCompanyPolicy() { return  isCompanyPolicySet.get();}
	public static void setPhoneForNewHire(String value) { phone.set(value);}
	public static String getPhoneForNewHire() { return  phone.get();}
	public static void setEmployeeIdForNewHire(String value) { employeeId.set(value);}
	public static String getEmployeeIdForNewHire() { return  employeeId.get();}
	public static void setConsoleWindowHandle(String value) { consoleHandle.set(value);}
	public static String getConsoleWindowHandle() { return consoleHandle.get();}
	public static void setWFSStatus(Boolean value) { isWFSEnabled.set(value);}
	public static Boolean getWFSStatus() { return  isWFSEnabled.get();}
	//True- assign TM, False - offer TM
	public static void setAssignTMStatus(Boolean value) { isAssignTM.set(value);}
	public static Boolean getAssignTMStatus() { return  isAssignTM.get();}
	public static void setMessageOfTMScheduledStatus(String value) { messageOfTMScheduledStatus.set(value);}
	public static String getMessageOfTMScheduledStatus() { return messageOfTMScheduledStatus.get();}
	public static void setWeekDaysNDates(HashMap<String, Integer> value) {weekDaysNDates.set(value);}
	public static HashMap<String, Integer> getWeekDaysNDates() { return weekDaysNDates.get();}
	public static void setNewCreateShiftUIStatus(Boolean value) { isNewCreateShiftUIEnabled.set(value);}
	public static Boolean getNewCreateShiftUIStatus() { return  isNewCreateShiftUIEnabled.get();}
}
