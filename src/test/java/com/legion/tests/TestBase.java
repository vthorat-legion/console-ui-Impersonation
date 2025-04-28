package com.legion.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableMap;
import com.legion.pages.*;
import com.legion.pages.core.ConsoleAdminPage;
import com.legion.pages.pagefactories.ConsoleWebPageFactory;
import com.legion.pages.pagefactories.PageFactory;
import com.legion.pages.pagefactories.mobile.MobilePageFactory;
import com.legion.pages.pagefactories.mobile.MobileWebPageFactory;
import com.legion.test.testrail.APIException;
import com.legion.test.testrail.TestRailOperation;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.tests.testframework.ExtentReportManager;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.tests.testframework.LegionWebDriverEventListener;
import com.legion.tests.testframework.ScreenshotManager;
import com.legion.utils.*;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.json.JSONException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.legion.utils.MyThreadLocal.*;
import static java.lang.Thread.sleep;

//import org.apache.log4j.Logger;


/**
 * DataProvider for multiple browser combinations.
 * Using SimpleUtils by default since we are not using any remote Selenium server
 * @author Yanming Tang
 *
 */

public abstract class TestBase {

    protected PageFactory pageFactory = null;
    protected MobilePageFactory mobilePageFactory = null;
    String TestID = null;
    //  public static HashMap<String, String> propertyMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
    public static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
    public static Map<String, String> districtsMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/UpperfieldsForDifferentEnterprises.json");
    private static ExtentReports extent = ExtentReportManager.getInstance();
    static HashMap<String,String> testRailCfgSch = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg_SCH.json");
    static HashMap<String,String> testRailCfgOp = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg_OP.json");
    static HashMap<String,String> testRailCfgElm = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg_ELM.json");
    public static AndroidDriver<MobileElement> driver;
    public static String versionString;
    public static int version;
    public static  int flagForTestRun = 0;
    public String enterpriseName;
    public static String pth=System.getProperty("user.dir");
    public static String reportFilePath=pth+"/Reports/";
    public static String screenshotFilePath=pth+"/screenshots/";
    public static String excelData=pth+"/TestData/";
    public static String apkpath=pth+"/Resources";
    public static AppiumDriverLocalService service;
    private static AppiumServiceBuilder builder;
    public static final int TEST_CASE_PASSED_STATUS = 1;
    public static final int TEST_CASE_FAILED_STATUS = 5;
    public static String testSuiteID = null;
    public static String finalTestRailRunName = null;
    public static boolean ifAddNewTestRun = true;
    public static List<Integer> AllTestCaseIDList = null;
    public static String testRailReportingFlag = null;
    public static Integer testRailRunId = null;
    public static String testRailProjectID = null;

    public enum AccessRoles {
        InternalAdmin("InternalAdmin"),
        StoreManager("StoreManager"),
        StoreManager2("StoreManager2"),
        StoreManagerOtherLocation1("StoreManagerOtherLocation1"),
        TeamLead("TeamLead"),
        TeamLead2("TeamLead2"),
        TeamMember("TeamMember"),
        TeamMemberOtherLocation1("TeamMemberOtherLocation1"),
        TeamMember2("TeamMember2"),
        TeamMember3("TeamMember3"),
        TeamMember4("TeamMember4"),
        TeamMember5("TeamMember5"),
        TeamMember6("TeamMember6"),
        StoreManagerLG("StoreManagerLG"),
        DistrictManager("DistrictManager"),
        DistrictManager2("DistrictManager2"),
        CustomerAdmin("CustomerAdmin"),
        CustomerAdmin2("CustomerAdmin2"),
        AreaManager("AreaManager"),
        GeneralManager("GeneralManager");
        private final String role;
        AccessRoles(final String accessRole) {
            role = accessRole;
        }
        public String getValue() {
            return role;
        }
    }

    @Parameters({ "platform", "executionon", "runMode","testRail","testSuiteName","testRailRunName"})
    @BeforeSuite
    public void startServer(@Optional String platform, @Optional String executionon,
                            @Optional String runMode, @Optional String testRail, @Optional String testSuiteName, @Optional String testRailRunName, ITestContext context) throws Exception {

        if (AllTestCaseIDList==null){
            AllTestCaseIDList = new ArrayList<Integer>();
        }
//        startServer();
//        mobilePageFactory = createMobilePageFactory();
        //For mobile.
        if(platform!= null && executionon!= null && runMode!= null){
            if (platform.equalsIgnoreCase("android") && executionon.equalsIgnoreCase("realdevice")
                    && runMode.equalsIgnoreCase("mobile") ){
                startServer();
                mobilePageFactory = createMobilePageFactory();
            } else{
                Reporter.log("Script will be executing only for Web");
            }
        }else{
            Reporter.log("Script will be executing only for Web");
        }

        if(System.getProperty("testRail") != null && System.getProperty("testRail").equalsIgnoreCase("Yes")){
            testRailReportingFlag = "Y";
            if (System.getProperty("module") != null && (System.getProperty("module").equalsIgnoreCase("op"))) {
                testSuiteID = testRailCfgOp.get("TEST_RAIL_SUITE_ID");
                testRailProjectID = testRailCfgOp.get("TEST_RAIL_PROJECT_ID");
            } else if (System.getProperty("module") != null && (System.getProperty("module").equalsIgnoreCase("sch"))) {
                testSuiteID = testRailCfgSch.get("TEST_RAIL_SUITE_ID");
                testRailProjectID = testRailCfgSch.get("TEST_RAIL_PROJECT_ID");
            } else if (System.getProperty("module") != null && (System.getProperty("module").equalsIgnoreCase("elm"))){
                testSuiteID = testRailCfgElm.get("TEST_RAIL_SUITE_ID");
                testRailProjectID = testRailCfgElm.get("TEST_RAIL_PROJECT_ID");
            }
            finalTestRailRunName = testRailRunName;

            if (AllTestCaseIDList==null){
                AllTestCaseIDList = new ArrayList<Integer>();
            }
            TestRailOperation.addTestRun();
        }
    }

    // Set the Desired Capabilities to launch the app in Andriod mobile
    public static void launchMobileApp() throws Exception{
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", propertyMap.get("deviceName"));
        caps.setCapability("platformName", "Android");
//        caps.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
//        caps.setCapability(CapabilityType.VERSION, "108");
        caps.setCapability("noReset",true);
        caps.setCapability("platformVersion", propertyMap.get("platformVersion"));
        caps.setCapability("autoAcceptAlerts", true);
        caps.setCapability("appPackage", "co.legion.client.staging");
//        caps.setCapability("appActivity", "activities.LegionSplashActivity");
        caps.setCapability("appActivity", "co.legion.client.activities.newloginsplash.InitialScreenActivity");
        caps.setCapability("newCommandTimeout", "360");
        setAndroidDriver( new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps));
        getAndroidDriver().manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
        sleep(10000);
        ExtentTestManager.getTest().log(Status.PASS, "Launched Mobile Application Successfully!");
//        Map<String, Object> cap = getAndroidDriver().getSessionDetails();
//        System.out.println("cap is");

    }

    public static void launchMobileWebApp() throws Exception{
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", propertyMap.get("deviceName"));
        caps.setCapability("platformName", "Android");
        caps.setCapability("chromedriverExecutable","D:\\drivers\\chromedriver.exe");
        caps.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
        caps.setCapability("platformVersion", propertyMap.get("platformVersion"));
        caps.setCapability("noReset",true);
//        caps.setCapability("appium:chromeoptions", ImmutableMap("w3c",false));
        caps.setCapability("newCommandTimeout", "360");
        caps.setCapability("appium:chromeOptions",ImmutableMap.of("w3c",false));
        setAndroidWebDriver( new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), caps));
        getAndroidWebDriver().manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
        sleep(10000);
//        getAndroidWebDriver().get("https://www.makemytrip.com");
        ExtentTestManager.getTest().log(Status.PASS, "Launched Mobile Application Successfully!");
//        Map<String, Object> cap = getAndroidDriver().getSessionDetails();
//        System.out.println("cap is");

    }


//    @BeforeClass
//    protected void init () {
//        ScreenshotManager.createScreenshotDirIfNotExist();
//    }

    @BeforeMethod(alwaysRun = true)
    protected void initTestFramework(Method method, ITestContext context) throws AWTException, IOException, APIException, JSONException {
        try {
            Date date = new Date();
            String testName = ExtentTestManager.getTestName(method);
            String ownerName = ExtentTestManager.getOwnerName(method);
            String automatedName = ExtentTestManager.getAutomatedName(method);
            enterpriseName = SimpleUtils.getEnterprise(method);
            String platformName = ExtentTestManager.getMobilePlatformName(method);
            List<String> categories = new ArrayList<String>();
            categories.add(getClass().getSimpleName());
            List<String> enterprises = new ArrayList<String>();
            enterprises.add(enterpriseName);
            ExtentTestManager.createTest(getClass().getSimpleName() + " - "
                    + " " + method.getName() + " : " + testName + ""
                    + " [" + ownerName + "/" + automatedName + "/" + platformName + "]", "", categories);
            extent.setSystemInfo(method.getName(), enterpriseName.toString());
            if (testRailRunId == null) {
                testRailRunId = 0;
            }
            if (testRailReportingFlag != null) {
                TestRailOperation.addNUpdateTestCaseIntoTestRail(testName, context);
                MyThreadLocal.setTestResultFlag(false);
                MyThreadLocal.setTestSkippedFlag(false);
            }
            setCurrentMethod(method);
            setBrowserNeeded(true);
            setCurrentTestMethodName(method.getName());
            setSessionTimestamp(date.toString().replace(":", "_").replace(" ", "_"));
        } catch (Exception e) {
            SimpleUtils.fail("Error encountered when initing framework: " + e.getMessage(), false);
        }
    }


    /**
     * upload file with the input element
     * @param fileName file name with relative path xxx/xxx.png
     */
    public static void uploadFiles(WebElement ele, String fileName) throws Exception {
        Actions actions = new Actions(getDriver());
        // if linux system
        if (System.getProperty("os.name").contains("Linux")) {
            String filePath = null;
            // change the inputBy element as block
            filePath = new File(fileName).getAbsolutePath();
            ele.sendKeys(filePath);
            actions.sendKeys(Keys.ENTER).build().perform();
        }
        else {
            //run at local
            String absolutePath = new File(fileName).getCanonicalPath();
            ele.sendKeys(absolutePath);
            //return
            actions.sendKeys(Keys.ENTER).build().perform();
        }
    }

    protected void createDriver (String browser, String version, String os) throws Exception {
        if (getBrowserNeeded() && browser != null) {
            setDriverType(browser);
            setVersion(version);
            setOS(os);
            createDriver();
        }
    }

    protected void createDriver()
            throws MalformedURLException, UnexpectedException {

        //todo replace Chrome driver initializaton with what Manideep has
        DesiredCapabilities capabilities = null;
        String url = "";
        url = SimpleUtils.getURL();
        // Initialize browser
        if (propertyMap.get("isGridEnabled").equalsIgnoreCase("false")) {
            capabilities = SimpleUtils.initCapabilities(getDriverType(), getVersion(), getOS());
            if (getDriverType().equalsIgnoreCase(propertyMap.get("INTERNET_EXPLORER"))) {
                InternetExplorerOptions options = new InternetExplorerOptions()
                        .requireWindowFocus()
                        .ignoreZoomSettings()
                        .introduceFlakinessByIgnoringSecurityDomains();
                options.setCapability("silent", true);
                options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                setDriver(new InternetExplorerDriver(options));

            }
            if (getDriverType().equalsIgnoreCase(propertyMap.get("CHROME"))) {
                System.setProperty("webdriver.chrome.driver", propertyMap.get("CHROME_DRIVER_PATH"));
                ChromeOptions options = new ChromeOptions();
                if (propertyMap.get("isHeadlessBrowser").equalsIgnoreCase("true")) {
//                    options.addArguments("headless");
                    options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--disable-setuid-sandbox", "--disable-dev-shm-usage");
                    options.addArguments("window-size=1200x600");
                    runScriptOnHeadlessOrBrowser(options);
                } else {
                    runScriptOnHeadlessOrBrowser(options);
                }

            }
            if (getDriverType().equalsIgnoreCase(propertyMap.get("FIREFOX"))) {
                System.setProperty("webdriver.gecko.driver", propertyMap.get("FIREFOX_DRIVER_PATH"));
                FirefoxProfile profile = new FirefoxProfile();
                profile.setAcceptUntrustedCertificates(true);
                FirefoxOptions options = new FirefoxOptions();
                options.setProfile(profile);
                setDriver(new FirefoxDriver(options));
            }

            pageFactory = createPageFactory();
            LegionWebDriverEventListener webDriverEventListener = new LegionWebDriverEventListener();
            getDriver().register(webDriverEventListener);

        } else {
            // Launch remote browser and set it as the current thread
            createRemoteChrome(url);
        }
    }


    private void createRemoteChrome(String url){
        MyThreadLocal myThreadLocal = new MyThreadLocal();
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "chrome");
//        caps.setCapability("version", "5.4.0-1029-aws");
        caps.setCapability("platform", "LINUX");
        caps.setCapability("idleTimeout", 150);
        caps.setCapability("network", true);
        caps.setCapability("visual", true);
        caps.setCapability("video", true);
        caps.setCapability("console", true);
        caps.setCapability("name", ExtentTestManager.getTestName(myThreadLocal.getCurrentMethod()));

        Assert.assertNotNull(url,"Error grid url is not configured, please review it in envCFg.json file and add it.");
        try {
            setDriver(new RemoteWebDriver(new URL(url),caps));
            pageFactory = createPageFactory();
            LegionWebDriverEventListener webDriverEventListener = new LegionWebDriverEventListener();
            getDriver().register(webDriverEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PageFactory createPageFactory() {
        return new ConsoleWebPageFactory();
    }

    private MobilePageFactory createMobilePageFactory() {
        return new MobileWebPageFactory();
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown(Method method,ITestResult result) throws IOException {
        ExtentTestManager.getTest().info("tearDown started");
        TestRailOperation.addResultForTest();
        if (Boolean.parseBoolean(propertyMap.get("close_browser"))) {
            try {
                if (getDriver() != null) {
                    getDriver().manage().deleteAllCookies();
                    getDriver().quit();
                }
            } catch (Exception exp) {
                Reporter.log("Error closing browser");
            } finally {
                if (getDriver() != null) {
                    getDriver().quit();
                }
            }
        }

        if (getVerificationMap() != null) {
            getVerificationMap().clear();
        }
        ExtentTestManager.getTest().info("tearDown finished");
        extent.flush();

    }

    @AfterSuite
    public void afterSuiteWorker() throws IOException{
        if(testRailReportingFlag!=null){
            List<Integer> testRunList = new ArrayList<Integer>();
            testRunList.add(testRailRunId);
            if (testRailRunId!=null && TestRailOperation.isTestRunEmpty(testRailRunId)){
                TestRailOperation.deleteTestRail(testRunList);
            }
        }
        stopServer();
    }


    public static void visitPage(Method testMethod){

        System.out.println("-------------------Start running test: " + testMethod.getName() + "-------------------");
        setEnvironment(propertyMap.get("ENVIRONMENT"));
        Enterprise e = testMethod.getAnnotation(Enterprise.class);
        String enterpriseName = null;
        if (System.getProperty("enterprise")!=null && !System.getProperty("enterprise").isEmpty()) {
            enterpriseName = System.getProperty("enterprise");
        }else if(e != null ){
            enterpriseName = SimpleUtils.getEnterprise(e.name());
        }else{
            enterpriseName = SimpleUtils.getDefaultEnterprise();
        }
        setEnterprise(enterpriseName);
        switch (getEnvironment()){
            case "QA":
                if (System.getProperty("env")!=null) {
                    setURL(System.getProperty("env"));
                }else {
                    setURL(propertyMap.get("QAURL"));
                }
                loadURL();
                break;
            case "DEV":
                setURL(propertyMap.get("DEVURL"));
                loadURL();
                break;
            default:
                ExtentTestManager.getTest().log(Status.FAIL,"Unable to set the URL");
        }
    }


    public static void loadURL() {
        try {
            getDriver().get(getURL() + "legion/?enterprise=" + getEnterprise() + " ");
            getDriver().manage().window().maximize();

        } catch (TimeoutException te) {
            try {
                getDriver().navigate().refresh();
            } catch (TimeoutException te1) {
                SimpleUtils.fail("Page failed to load", false);
            }
        } catch (WebDriverException we) {
            try {
                getDriver().navigate().refresh();
            } catch (TimeoutException te1) {
                SimpleUtils.fail("Page failed to load", false);
            }
        }
    }

    /*
     * Login to Legion With Credential and assert on failure
     */
    public synchronized void loginToLegionAndVerifyIsLoginDone(String username, String Password, String location) throws Exception
    {
        MyThreadLocal.setLocationName(location);
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        SimpleUtils.report(getDriver().getCurrentUrl());
        loginPage.loginToLegionWithCredential(username, Password);
        loginPage.verifyLegionTermsOfService();
        loginPage.closeIntroductionMode();
        SimpleUtils.assertOnFail("Failed to login to the application!", loginPage.isLoginSuccess(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
        loginPage.closeIntroductionMode();
//        changeUpperFieldsAccordingToEnterprise(locationSelectorPage);
//        locationSelectorPage.changeLocation(location);
        boolean isLoginDone = loginPage.isLoginDone();
        SimpleUtils.assertOnFail("Not able to Login to Legion Application Successfully!", isLoginDone, false);
        setConsoleWindowHandle(getDriver().getWindowHandle());
        //loginPage.verifyLoginDone(isLoginDone, location);
    }

    public synchronized void loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield(String username, String Password, String location) throws Exception
    {
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        SimpleUtils.report(getDriver().getCurrentUrl());
        loginPage.loginToLegionWithCredential(username, Password);
        loginPage.verifyLegionTermsOfService();
        boolean isLoginSuccess = loginPage.isLoginSuccess();
        if (isLoginSuccess) {
            SimpleUtils.pass("Login legion without update upperfield successfully");
        }else
            SimpleUtils.fail("Login legion  failed",false);
    }
    private void changeUpperFieldsAccordingToEnterprise(LocationSelectorPage locationSelectorPage) throws Exception {
        if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
            locationSelectorPage.changeUpperFields(districtsMap.get("Coffee_Enterprise"));
        }
        if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
            locationSelectorPage.changeUpperFields(districtsMap.get("KendraScott2_Enterprise"));
        }
        if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
            locationSelectorPage.changeUpperFields(districtsMap.get("Op_Enterprise"));
        }
        if (getDriver().getCurrentUrl().contains(propertyMap.get("DGStage_Enterprise"))) {
            locationSelectorPage.changeUpperFields(districtsMap.get("DGStage_Enterprise"));
        }
        if (getDriver().getCurrentUrl().contains(propertyMap.get("CinemarkWkdy_Enterprise"))) {
            locationSelectorPage.changeUpperFields(districtsMap.get("CinemarkWkdy_Enterprise"));
        }
        if (getDriver().getCurrentUrl().contains(propertyMap.get("Vailqacn_Enterprise"))) {
            locationSelectorPage.changeUpperFields(districtsMap.get("Vailqacn_Enterprise"));
        }
    }

    public void loginAsDifferentRole(String roleName) throws Exception {
        try {
            Object[][] credentials = null;
            StackTraceElement[] stacks = (new Throwable()).getStackTrace();
            String simpleClassName = stacks[1].getFileName().replace(".java", "");
            String fileName = "UsersCredentials.json";
            if (System.getProperty("env")!=null && System.getProperty("env").toLowerCase().contains("rel")){
                fileName = "Release"+MyThreadLocal.getEnterprise()+fileName;
            } else {
                fileName = MyThreadLocal.getEnterprise() + fileName;
            }
            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
            if (userCredentials.containsKey(roleName + "Of" + simpleClassName)) {
                credentials = userCredentials.get(roleName + "Of" + simpleClassName);
            } else {
                credentials = userCredentials.get(roleName);
            }
            loginToLegionAndVerifyIsLoginDone(String.valueOf(credentials[0][0]), String.valueOf(credentials[0][1])
                    , String.valueOf(credentials[0][2]));
        } catch (Exception e) {
            SimpleUtils.fail("Login as: " + roleName + " failed!", false);
        }
    }

    protected void goToSchedulePageScheduleTab() throws Exception {
        // Go to Schedule page, Schedule tab
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
    }

    protected List<String> createShiftsWithSpecificValues(String workRole, String shiftName, String location, String startTime,
        String endTime, int shiftPerDay, List<Integer> workDays, String assignment, String shiftNotes, String tmName) throws Exception {
        List<String> selectedTMs = new ArrayList<>();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();

        newShiftPage.clickOnDayViewAddNewShiftButton();
        Thread.sleep(5000);
//        SimpleUtils.assertOnFail("New create shift page is not display! ",
//                newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
        // Select work role
        newShiftPage.selectWorkRole(workRole);
        // Set shift name
        if (shiftName != null && !shiftName.isEmpty()) {
            newShiftPage.setShiftNameOnNewCreateShiftPage(shiftName);
        }
        // Select location
        if (location != null && !location.isEmpty()) {
            newShiftPage.selectChildLocInCreateShiftWindow(location);
        }
        // Set end time
        if (endTime != null && !endTime.isEmpty()) {
            newShiftPage.moveSliderAtCertainPoint(endTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        }
        // Set start time
        if (startTime != null && !startTime.isEmpty()) {
            newShiftPage.moveSliderAtCertainPoint(startTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        }
        // Set shift per day
        newShiftPage.setShiftPerDayOnNewCreateShiftPage(shiftPerDay);
        // Select work day
        if (workDays.size() == 1) {
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectMultipleOrSpecificWorkDay(workDays.get(0), true);
        } else if (workDays.size() > 1) {
            newShiftPage.clearAllSelectedDays();
            for (int i : workDays) {
                newShiftPage.selectMultipleOrSpecificWorkDay(workDays.get(i), true);
            }
        }
        // Select the assignment
        newShiftPage.clickRadioBtnStaffingOption(assignment);
        // Set shift notes
        if (shiftNotes != null && !shiftNotes.isEmpty()) {
            newShiftPage.setShiftNotesOnNewCreateShiftPage(shiftNotes);
        }
        newShiftPage.clickOnCreateOrNextBtn();
        if (assignment.equals(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue())
                || assignment.equals(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue()) ) {
            if (tmName != null && !tmName.isEmpty()) {
                newShiftPage.searchTeamMemberByName(tmName);
            } else {
                shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
                for (int i = 0; i < shiftPerDay; i++) {
                    selectedTMs.add(newShiftPage.selectTeamMembers());
                }
            }
            newShiftPage.clickOnCreateOrNextBtn();
        }
        return selectedTMs;
    }

    public String getCrendentialInfo(String roleName) throws Exception {
        Object[][] credentials = null;
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        String simpleClassName = stacks[1].getFileName().replace(".java", "");
        String fileName = "UsersCredentials.json";
        fileName = MyThreadLocal.getEnterprise() + fileName;
        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
        if (userCredentials.containsKey(roleName + "Of" + simpleClassName)) {
            credentials = userCredentials.get(roleName + "Of" + simpleClassName);
        } else {
            credentials = userCredentials.get(roleName);
        }
        return String.valueOf(credentials[0][0]);
    }

    public HashMap<String, Object[][]> getSwapCoverUserCredentials(String locationName) throws Exception {
        HashMap<String, Object[][]> swapCoverCredentials = new HashMap<>();
        try {
            String fileName = "UserCredentialsForComparableSwapShifts.json";
            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
            for (Map.Entry<String, Object[][]> entry : userCredentials.entrySet()) {
                if (String.valueOf(entry.getValue()[0][2]).contains(locationName)) {
                    swapCoverCredentials.put(entry.getKey(), entry.getValue());
                    SimpleUtils.pass("Get Swap/Cover User Credential:" + entry.getKey());
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail("Failed to get the swap/cover name list for Location: " + locationName, false);
        }
        return swapCoverCredentials;
    }

    public abstract void firstTest(Method testMethod, Object[] params) throws Exception;

    // TODO Auto-generated method stub


    // Method for Start the appium server and arguments should be appium installation path upto node.exe and appium.js
    public static void appiumServerStart(String appiumServerPath, String appiumJSPath){
        service=AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File(appiumServerPath))
                .withAppiumJS(new File(appiumJSPath)));
    }

    //Start appium programatically
    public static void startServer() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("noReset", "true");
        cap.setCapability("autoGrantPermissions", true);
        //Build the Appium service
        builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingPort(4723);
        builder.withCapabilities(cap);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
        //Start the server with the builder
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
    }

    //Stop appium programatically
    public void stopServer() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("taskkill /F /IM node.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void runScriptOnHeadlessOrBrowser(ChromeOptions options){
        options.addArguments("disable-infobars");
        options.addArguments("test-type", "new-window", "disable-extensions","start-maximized");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-logging", "silent", "ignore-certificate-errors");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);
        options.setCapability("chrome.switches", Arrays.asList("--disable-extensions", "--disable-logging",
                "--ignore-certificate-errors", "--log-level=0", "--silent"));
        options.setCapability("silent", true);
        System.setProperty("webdriver.chrome.silentOutput", "true");
        setDriver(new ChromeDriver(options));
    }

    public  static void switchToNewWindow() {
        String winHandleBefore =getDriver().getWindowHandle();
        for(String winHandle : getDriver().getWindowHandles()) {
            if (winHandle.equals(winHandleBefore)) {
                //getDriver().close();
                continue;
            }
            getDriver().switchTo().window(winHandle);
            break;
        }
    }

    public static void closeCurrentWindow() {
        getDriver().close();
    }

    public static void switchToConsoleWindow() {
        try {
            Set<String> winHandles = getDriver().getWindowHandles();
            for (String handle : winHandles) {
                if (handle.equals(getConsoleWindowHandle())) {
                    getDriver().switchTo().window(handle);
                    SimpleUtils.pass("Switch to Console window successfully!");
                    break;
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail("Failed to switch to Console window!", false);
        }
    }

    public static String getCurrentTime() {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTime =  dfs.format(new Date());
        return currentTime;
    }

    public String getCurrentClassName() {
        String className = "";
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        className = stacks[1].getFileName().replace(".java", "");
        return className;
    }

    public static void refreshPage() {
        getDriver().navigate().refresh();
    }

    public static String getUrl() {
      return getDriver().getCurrentUrl();
    }

    public static String getSession() {
        Object session;
        session = getDriver().executeScript("return localStorage.getItem('legion.authToken');");
        return session.toString().replace("\"", "");
    }

    public static String getSessionId(String payLoad){
        //header
        HashMap<String, String> loginHeader = new HashMap<String, String>();
        //post request
        String[] postResponse = HttpUtil.httpPost(Constants.loginUrlRC, loginHeader, payLoad);
        Assert.assertEquals(postResponse[0], "200", "Failed to login!");
        String sessionId = postResponse[1];
        return sessionId;
    }

    public static void refreshCache(String cacheType) throws Exception{
        String sessionId = getSessionId("{\"enterpriseName\":\"opauto\",\"userName\":\"stoneman@legion.co\",\"passwordPlainText\":\"admin11.a\",\"sourceSystem\":\"legion\"}");
        //url
        String toggleUrl = Constants.refreshCache;
        Map<String, String> togglePara = new HashMap<>();
        togglePara.put("cacheType", cacheType);
        String[] response = HttpUtil.httpGet(toggleUrl, sessionId, togglePara);
    }

    public static List<String> getUserNameNPwdForCallingAPI() {
        List<String> usernameNPwd = new ArrayList<>();
        HashMap<String, String> credentials = JsonUtil.getPropertiesFromJsonFile("src/test/resources/credentialsForCallingAPI.json");
        if (credentials != null && credentials.size() > 0) {
            if (credentials.containsKey(getEnterprise())) {
                String[] values = credentials.get(getEnterprise()).split("/");
                usernameNPwd.add(values[0]);
                usernameNPwd.add(values[1]);
            }
        }
        return usernameNPwd;
    }

    public static void refreshCachesAfterChangeTemplate() throws Exception {
        AdminPage adminPage = new ConsoleAdminPage();
        adminPage.clickOnConsoleAdminMenu();
        adminPage.clickOnInspectorTab();
        adminPage.clickOnCacheTab();
        adminPage.refreshCacheStatus(ConsoleAdminPage.CacheNames.TemplateAssociation.getValue());
        adminPage.refreshCacheStatus(ConsoleAdminPage.CacheNames.Template.getValue());
        adminPage.refreshCacheStatus(ConsoleAdminPage.CacheNames.TemplateUserAssociation.getValue());
        adminPage.refreshCacheStatus(ConsoleAdminPage.CacheNames.LocationBrokerContainer.getValue());
    }

    public void deleteAllUnassignedShifts() throws Exception {
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        if (smartCardPage.isRequiredActionSmartCardLoaded()) {
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
        }
    }
    //added by Mary. 09:00AM-->9:00am
    public String changeTimeFormat(String time) throws Exception{
        String result = time;
        if (result.indexOf("0")==0){
            result = result.substring(1).toLowerCase();
        }
        return result;
    }

    public int getCharactersCount (String str, String key) {
        int count = 0;
        int index = 0;
        while((index = str.indexOf(key))!=-1){
            str = str.substring(index+key.length());
            count++;
        }
        return count;
    }

    public static void refreshCachesAfterChangeToggleOrABSwitch() throws Exception {
        AdminPage adminPage = new ConsoleAdminPage();
        adminPage.clickOnConsoleAdminMenu();
        adminPage.clickOnInspectorTab();
        adminPage.clickOnCacheTab();
        adminPage.refreshCacheStatus(ConsoleAdminPage.CacheNames.ABSwitch.getValue());
    }
}
