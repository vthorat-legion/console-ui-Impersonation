package com.legion.tests.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.legion.pages.ScheduleCommonPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.legion.pages.DashboardPage;
import com.legion.pages.SchedulePage;
import com.legion.pages.StaffingGuidancePage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.SimpleUtils;

public class StaffingGuidanceTestKendraScott2 extends TestBase{
	
	@Override
	  @BeforeMethod()
	  public void firstTest(Method testMethod, Object[] params) throws Exception{
		  this.createDriver((String)params[0],"69","Window");
	      visitPage(testMethod);
	      loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
	  }
	
	@Automated(automated = "Automated")
	@Owner(owner = "Naval")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "LEG-2423: As a store manager, can view Staffing Guidance data for current week")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void staffingGuidanceDataAsStoreManagerTestKendraScott2(String username, String password, String browser, String location)
            throws Exception
    {
    	//To Do Should be separate Test from Schedule test
//    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        dashboardPage.goToToday();
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        SimpleUtils.assertOnFail( "Schedule Page not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()),false);
        StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
        staffingGuidancePage.navigateToStaffingGuidanceTab();
        SimpleUtils.assertOnFail( "Staffing Guidance tab not loaded successfully!", staffingGuidancePage.isStaffingGuidanceTabActive(),false);
        
        /*
         * Staffing Guidance for Day View
         */
        staffingGuidancePage.navigateToStaffingGuidanceTabDayView();
        SimpleUtils.assertOnFail( "Staffing Guidance Day View not loaded successfully!", staffingGuidancePage.isStaffingGuidanceForecastTabDayViewActive(),false);
        List<String> staffingGuidanceDayViewTimeDurationLabelsdata = staffingGuidancePage.getStaffingGuidanceForecastDayViewTimeDuration();
        List<Integer> staffingGuidanceDayViewItemsLabelsdata = staffingGuidancePage.getStaffingGuidanceForecastDayViewItemsCount();
        List<Integer> staffingGuidanceDayViewTeamMembersLabelsdata = staffingGuidancePage.getStaffingGuidanceForecastDayViewTeamMembersCount();
        String dayViewTimeDurationAsString = "";
        for(String timeDuration : staffingGuidanceDayViewTimeDurationLabelsdata)
        {
        	dayViewTimeDurationAsString = dayViewTimeDurationAsString + "|" + timeDuration;
        }
        String staffingGuidanceDayViewItemsCountAsString = "";
        int staffingGuidanceDayViewItemsTotalCount = 0;
        for(int itemsCount : staffingGuidanceDayViewItemsLabelsdata)
        {
        	staffingGuidanceDayViewItemsTotalCount = staffingGuidanceDayViewItemsTotalCount + itemsCount;
        	staffingGuidanceDayViewItemsCountAsString = staffingGuidanceDayViewItemsCountAsString + "|" + itemsCount;
        }
        String staffingGuidanceDayViewTeamMembersCountAsString = "";
        int staffingGuidanceDayViewTeamMembersTotalCount = 0;
        for(int itemsCount : staffingGuidanceDayViewTeamMembersLabelsdata)
        {
        	staffingGuidanceDayViewTeamMembersTotalCount = staffingGuidanceDayViewTeamMembersTotalCount + itemsCount;
        	staffingGuidanceDayViewTeamMembersCountAsString = staffingGuidanceDayViewTeamMembersCountAsString + "|" + itemsCount;
        }
        ExtentTestManager.getTest().log(Status.INFO,"Staffing Guidance for Day View");
        ExtentTestManager.getTest().log(Status.INFO,dayViewTimeDurationAsString);
        ExtentTestManager.getTest().log(Status.INFO,staffingGuidanceDayViewItemsCountAsString);
        ExtentTestManager.getTest().log(Status.INFO,staffingGuidanceDayViewTeamMembersCountAsString);
        SimpleUtils.assertOnFail( "Staffing Guidance Day View items Count is Zero!", (staffingGuidanceDayViewItemsTotalCount != 0),true);
        SimpleUtils.assertOnFail( "Staffing Guidance Day View Team Memners Count is Zero!", (staffingGuidanceDayViewTeamMembersTotalCount != 0),true);
        
        /*
         * Staffing Guidance for Week View
         */
        staffingGuidancePage.navigateToStaffingGuidanceTabWeekView();
        SimpleUtils.assertOnFail( "Staffing Guidance Week View not loaded successfully!", staffingGuidancePage.isStaffingGuidanceForecastTabWeekViewActive(),false);
        List<String> staffingGuidanceWeekViewDurationLabels = staffingGuidancePage.getStaffingGuidanceDayDateMonthLabelsForWeekView();
        List<Float> staffingGuidanceWeekViewDaysHours = staffingGuidancePage.getStaffingGuidanceHoursCountForWeekView();
        String weekViewDurationDataAsString = "";
        for(String weekViewDuration : staffingGuidanceWeekViewDurationLabels)
        {
        	weekViewDurationDataAsString = weekViewDurationDataAsString + "|" + weekViewDuration;
        }
        String staffingGuidanceWeekViewDaysHoursAsString = "";
        Float staffingGuidanceWeekViewHoursTotalCount = (float) 0;
        for(Float staffingGuidanceWeekViewDayHours : staffingGuidanceWeekViewDaysHours)
        {
        	staffingGuidanceWeekViewHoursTotalCount = staffingGuidanceWeekViewHoursTotalCount + staffingGuidanceWeekViewDayHours;
        	staffingGuidanceWeekViewDaysHoursAsString = staffingGuidanceWeekViewDaysHoursAsString + "|" + staffingGuidanceWeekViewDayHours;
        }
        ExtentTestManager.getTest().log(Status.INFO,"Staffing Guidance for Week View");
        ExtentTestManager.getTest().log(Status.INFO,weekViewDurationDataAsString);
        ExtentTestManager.getTest().log(Status.INFO,staffingGuidanceWeekViewDaysHoursAsString);
        SimpleUtils.assertOnFail( "Staffing Guidance Week View Hours Count is Zero!", (staffingGuidanceWeekViewHoursTotalCount != 0),true);
        staffingGuidancePage.clickOnStaffingGuidanceAnalyzeButton();
        List<HashMap<String, String>> analyzePopupStaffingGuidanceData = staffingGuidancePage.getAnalyzePopupStaffingGuidanceAndLatestVersionData();
        SimpleUtils.report("Staffing Guidance Data Start..");
        for(HashMap<String, String> analyzePopupData : analyzePopupStaffingGuidanceData)
        {
        	for(Map.Entry<String, String> entry : analyzePopupData.entrySet())
            {
        		SimpleUtils.report(entry.getKey() +" : "+entry.getValue());
            }
        }
        SimpleUtils.report("Staffing Guidance Data End..");
        
    }
	
	@Automated(automated = "Manual")
	@Owner(owner = "Manideep")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-2423: Weekly Guidance Hours Should match the sum of individual day working hours (Failed with Jira Ticket#4923)")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void weeklyGuidanceHoursShouldMatchTheSumOfEachDay(String username, String password, String browser, String location)
            throws Exception
    {
		SimpleUtils.pass("Login as Store Manager Successfully");
		SimpleUtils.pass("Successfully opened the Schedule app");
		SimpleUtils.pass("Open a Staffing Guidance of any Week (Not necessarily the current week) in Week view ");
        SimpleUtils.pass("Staffing Guidance hours not matching with the sum of individual day working hours"); 
    }
	
	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-2423: Weekly Guidance Hours Should match the sum of Work Roles Enabled")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void weeklyGuidanceHoursShouldMatchTheSumOfWorkRolesEnabled(String username, String password, String browser, String location)
            throws Exception
    {

		SimpleUtils.pass("Login as Store Manager Successfully");
		SimpleUtils.pass("Successfully opened the Schedule app");
		SimpleUtils.pass("Open a Staffing Guidance of any Week (Not necessarily the current week) in Week view ");
		SimpleUtils.pass("Select Work Roles from dropdown and it should match with the Weekly Guidance hours"); 
		SimpleUtils.pass("Select Work Roles from dropdown and assert value of Work roles which are not enabled should be zero"); 
    }
	
	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-5005: Refresh Guidance in LegionTech shows different guidance hours")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void staffingGuidanceShowsDiffGuidanceHour(String username, String password, String browser, String location)
            throws Exception
    {

		SimpleUtils.pass("Login to Legiontech Successfully");
		SimpleUtils.pass("Successfully opened the Schedule app");
		SimpleUtils.pass("Open a Staffing Guidance of 09/23 Week view ");
		SimpleUtils.pass("Data in Staffing Guidance table is same as yesterday"); 
    }
	
	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-5037: Staffing guidance page gets blank on doing a refresh")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void staffingGuidanceShouldNotBeBlankOnRefresh(String username, String password, String browser, String location)
            throws Exception
    {
		SimpleUtils.pass("Login to reverted environement Successfully");
		SimpleUtils.pass("Successfully opened the Schedule app");
		SimpleUtils.pass("Open a Staffing Guidance of any Week (Not necessarily the current week) in Week view ");
		SimpleUtils.pass("Click Refresh button"); 
		SimpleUtils.pass("Data in Staffing Guidance table is not getting disappear"); 
    }
	

	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-5062 : Items section of Day View on Staffing Guidance tab has no data on LegionCoffee env")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void itemsOnstaffingGuidanceIsBlank(String username, String password, String browser, String location)
            throws Exception
    {

		SimpleUtils.pass("Login to LegionCoffee environment Successfully");
		SimpleUtils.pass("Successfully opened the Schedule app");
		SimpleUtils.pass("Open a day view in Staffing Guidance of any Week");
		SimpleUtils.fail("assert Items section should not be empty.",false);
    }

	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-5063 : For Bay Area location, Analyze section is showing Polo Alto by default even for Bay Area under Schedule History of Staffing Guidance")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void analyzeShowsDifferentLocationInScheduleHistoryOfBayArea(String username, String password, String browser, String location)
            throws Exception
    {

		SimpleUtils.pass("Login to LegionCoffee environment Successfully");
		SimpleUtils.pass("Successfully opened the Schedule app");
		SimpleUtils.pass("Open a day view in Staffing Guidance of any Week");
		SimpleUtils.pass("Click Analyze button");
		SimpleUtils.pass("location is configured to show data from three different locations");
    }

	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-5108:Wages showing as zero for certain work roles having non-0 staffing guidance hour in LegionCoffee")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
	public void wagesAreZeroForGuidanceHourValue(String username, String password, String browser, String location)
	          throws Exception
	{
	       SimpleUtils.pass("Login to LegionCoffee Successfully");
	       SimpleUtils.pass("Navigate to Staffing Guidance tab under Schedule tab");
	       SimpleUtils.pass("Open Guidance for Oct1-Oct7");
	       SimpleUtils.pass("Select Key Manager in all work role filter");
	       SimpleUtils.pass("assert for Non-0 Guidance hour schedule wages should be Non-0 ");
	}

	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Kendrascott2_Enterprise")
	@TestName(description = "LEG-4923:After adding individual Guidance Hrs of each Day present in the week is not equals to Total Guidance Hrs of the week")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
	public void sumOfGuidanceHourNotEqualToTotalGuidanceHour(String username, String password, String browser, String location)
	          throws Exception
	{
	       SimpleUtils.pass("Login to environment Successfully");
	       SimpleUtils.pass("Navigate to Staffing Guidance tab open any week");
	       SimpleUtils.pass("assert sum of individual guidance hour should be equal to total guidance hour ");

	}
	

}
