//package com.legion.tests.core;
//
//import com.legion.pages.*;
//import com.legion.tests.annotations.*;
//import java.util.Map;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import com.aventstack.extentreports.Status;
//import com.legion.tests.TestBase;
//import com.legion.utils.SimpleUtils;
//
//import java.lang.reflect.Method;
//
//import com.legion.tests.data.CredentialDataProviderSource;
//import com.legion.tests.testframework.ExtentTestManager;
//
//
///**
// * Manideep
// */
//
//public class NavigationTest extends TestBase {
//    private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
//
//    @Override
//	@BeforeMethod()
//	public void firstTest(Method testMethod, Object[] params) throws Exception{
//	  this.createDriver((String)params[0],"69","Window");
//      visitPage(testMethod);
//        loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
//	}
//
//    @Automated(automated = "Automated")
//	@Owner(owner = "Naval")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Verify all the console navigations for Legion web application at high level")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void legionConsoleNavigationFlowStoreManager(String username, String password, String browser, String location)
//
//            throws Exception {
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        dashboardPage.verifyDashboardPageLoadedProperly();
//        SchedulePage schedulePage = dashboardPage.goToToday();
//        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//        scheduleCommonPage.goToSchedulePage();
//        schedulePage.goToProjectedSales();
//        schedulePage.goToStaffingGuidance();
//        scheduleCommonPage.goToSchedule();
//        ExtentTestManager.getTest().log(Status.PASS,"Schedule Page - Navigation sales, guidance and schedule finish Successfully!");
//    }
//
//
//    @Automated(automated = "Automated")
//	@Owner(owner = "Naval")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Verify all the console navigations for Legion web application at high level")
//    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//    public void legionConsoleNavigationFlowInternalAdmin(String username, String password, String browser, String location)
//
//            throws Exception {
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        dashboardPage.verifyDashboardPageLoadedProperly();
//        SchedulePage schedulePage = dashboardPage.goToToday();
//        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//        scheduleCommonPage.goToSchedulePage();
//        schedulePage.goToProjectedSales();
//        schedulePage.goToStaffingGuidance();
//        scheduleCommonPage.goToSchedule();
//        ExtentTestManager.getTest().log(Status.PASS,"Schedule Page - Navigation sales, guidance and schedule finish Successfully!");
//    }
//
//    @Automated(automated = "Automated")
//   	@Owner(owner = "Gunjan")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "TP-144 : Validate navigation to below tabs and loading of data[No spinning icon]")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void legionAppNavigationAllTabsStoreManager(String username, String password, String browser, String location) throws Exception {
//       DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//       dashboardPage.verifyDashboardPageLoadedProperly();
//       TeamPage teamPage = pageFactory.createConsoleTeamPage();
//       teamPage.loadTeamTab();
//       ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//       scheduleOverviewPage.loadScheduleOverview();
//       SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
//       salesForecastPage.loadSalesForecast();
//       StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
//       staffingGuidancePage.loadStaffingGuidance();
//
//       schedulePage.loadSchedule();
//       AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//       analyticsPage.loadAnalyticsTab();
//
//    }
//
//    @SanitySuite(sanity =  "Sanity")
//    @UseAsTestRailSectionId(testRailSectionId = 96)
//    @Automated(automated = "Automated")
//    @Owner(owner = "Gunjan")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate navigation and data loading in Day/Week view for Schedule Tab[No Spinning icon or Blank screen]")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void scheduleSubTabNavigationStoreManager(String username, String password, String browser, String location) throws Exception {
//        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//        scheduleOverviewPage.loadScheduleOverview();
//
//        schedulePage.navigateScheduleDayWeekView(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
//
//    }
//
//    @SanitySuite(sanity =  "Sanity")
//    @UseAsTestRailSectionId(testRailSectionId = 96)
//    @Automated(automated = "Automated")
//    @Owner(owner = "Gunjan")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate navigation and data loading in Day/Week view for Projected Traffic Tab[No Spinning icon or Blank screen]")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void projectedTrafficSubTabNavigationStoreManager(String username, String password, String browser, String location) throws Exception {
//        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//        scheduleOverviewPage.loadScheduleOverview();
//        SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
//        salesForecastPage.loadSalesForecastforCurrentNFutureWeek(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Six.getValue());
//    }
//
//    @SanitySuite(sanity =  "Sanity")
//    @UseAsTestRailSectionId(testRailSectionId = 96)
//    @Automated(automated = "Automated")
//    @Owner(owner = "Gunjan")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate navigation and data loading in Day/Week view for Staffing Guidance Tab[No Spinning icon or Blank screen]")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void staffingGuidanceSubTabNavigationStoreManager(String username, String password, String browser, String location) throws Exception {
//        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//        scheduleOverviewPage.loadScheduleOverview();
//        StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
//        staffingGuidancePage.navigateStaffingGuidance(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
//    }
//
//
//    @Automated(automated = "Manual")
//    @Owner(owner = "Gunjan")
//    @Enterprise(name = "Coffee2_Enterprise")
//    @TestName(description = "LEG-5112:LocationGroup forecast, guidance and dashboard not loading on 10.09 master build for Carmel Club on LegionCoffee2")
//    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//    public void DataNotLoadingForCarmelClubLocation(String username, String password, String browser, String location)
//           throws Exception
//    {
//        SimpleUtils.pass("Login to LegionCoffee2 Successfully");
//        SimpleUtils.pass("Navigate to Carmel Club location");
//        SimpleUtils.pass("assert navigation for carmel club location should load successfully ");
//
//    }
//
//
//}