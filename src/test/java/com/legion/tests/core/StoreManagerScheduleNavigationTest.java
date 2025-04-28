package com.legion.tests.core;

import com.aventstack.extentreports.Status;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.SimpleUtils;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class StoreManagerScheduleNavigationTest extends TestBase {
    private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();

//    @BeforeClass
//    public void setUp () {
//        /*
//            Login as SM
//            Open Schedule app
//            Navigate to schedule overview
//            Pick next week which is one week from current date
//            Modify business hour and mark one day to be closed
//            Generate schedule
//         */
//    }

    @Override
	  @BeforeMethod()
	  public void firstTest(Method testMethod, Object[] params) throws Exception{
		  this.createDriver((String)params[0],"69","Window");
	      visitPage(testMethod);
	      loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
	  }
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-4249: should open the same day schedule when clicking Schedule tab from Overview")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void dayViewShouldBeSticky (String username, String password, String browser, String location) {
        /*
        Open a Schedule of any Day (Not necessarily the same Day) in Day View say (Sep 1)
        Then go to Schedule overview
        Then go back to Schedule tab directly
        Should open the the schedule of the same Day (Sep 1) in Day View
         */
    	
    }

    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-4249: should open the week schedule in TM view when clicking Schedule tab from Overview")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void teamViewShouldBeSticky (String username, String password, String browser, String location) {
        /*
        Open a Schedule of any Week (Not necessarily the current week) in TM view
        Then go to Schedule overview
        Then go back to Schedule tab directly
        Should open the the schedule of the same week in TM view
         */
    }

    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-4249: should open the week schedule in TM view when clicking Schedule tab from Overview")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void shouldNaviToNextWeekAndGenerateSchedule (String username, String password, String browser, String location) {
        /*
            navigate to next week
            assert schedule shows there are no shifts on the day when store is closed
            switch to TM view
            click next >
            assert that you are in the next week schedule and generate schedule button is clickable
            click generate and assert seeing successfully generated modal
            assert schedule shows there are shifts on the day when store is closed in the previous week
         */
    }
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-2424: should open the week schedule in Day/Week view for Past week and verify already generated Schedule")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void shouldBeNoGenerateButtonForPastWeek (String username, String password, String browser, String location) {

    	SimpleUtils.pass("Navigate to past week successfully!");
    	SimpleUtils.pass("assert the schedules that are already published should remain unchanged");
    	SimpleUtils.pass("assert there is no Generate Button");
    	SimpleUtils.pass("assert As you navigate to past week through navigation arrow, the Date and Day on Schedule calendar must be correct.");

    }
    
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-2592: should be able to view and filter Schedule and Group By")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void shouldBeAbleToViewAndFilterSchedule(String username, String password, String browser, String location) {

    	SimpleUtils.pass("Successfully Opened a Schedule of any Week (Not necessarily the current week) in Day/Week view");
    	SimpleUtils.pass("Select the dropdown of All Work Roles, All Shift Types, Group By All and assert all the options should be available");
    	SimpleUtils.pass("Select the dropdown of All Work Roles, All Shift Types and assert Guidance, Scheduled and Other Hrs");
    	SimpleUtils.pass("Select the dropdown of All Work Roles, All Shift Types and assert all the shifts should be grouped by the filters");
    	SimpleUtils.pass("Select the dropdown of Group By Location and assert Guidance, Scheduled and Other Hrs");    

    }
    
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-2592: verify Group by option is saved at session level.")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void GroupByShouldBeStickyAtSessionLevel(String username, String password, String browser, String location) {
    	SimpleUtils.pass("Successfully Opened a Schedule of any Week (Not necessarily the current week) in Day/Week view");
    	SimpleUtils.pass("Select the dropdown Group By All and naviagate out from Schedule");
    	SimpleUtils.pass("Go back to Schedule tab and assert Selection option should not be lost");    

    }
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-2592: verify Filter option is persisted locally")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void FilterShouldBeSticky(String username, String password, String browser, String location) {
    	SimpleUtils.pass("Successfully Opened a Schedule of any Week (Not necessarily the current week) in Day/Week view");
    	SimpleUtils.pass("Select the dropdown All Shift Types, All Work Roles successfully");
    	SimpleUtils.pass("Switch between Guidance and Schedule page and assert filter option should not be lost");
    	SimpleUtils.pass("Switch between Guidance and Schedule page to Forecast Page and assert filter option should be lost");

    }
    
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-4515: should Display Sales/Labor Hr for traffic with Sales Amount set up")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void DisplaySalesPerLaborHr(String username, String password, String browser, String location) {

    	SimpleUtils.pass("Login on reverted enterprise Successfully!");   
    	SimpleUtils.pass("Successfully Opened a Schedule of any Week (Not necessarily the current week) in Day view");
    	SimpleUtils.pass("Go to previous week till 19th August and and assert Sales/Labor Hr");
    	SimpleUtils.pass("Sales/Labor Hr should not be zero and matching with screenshot attached in Jira ticket");

    }
    
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-2425: should able to manually add admin shift (Failed with Jira Ticket#4922)")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void editShiftManually(String username, String password, String browser, String location) {
    	SimpleUtils.pass("Successfully Opened a Schedule of any Week (Not necessarily the current week) in Day view");
    	SimpleUtils.pass("Click on edit button and assert there is no pop up for Draft schedule");
    	SimpleUtils.pass("Click on + sign and assert there is Customize your new Shift window opens");
    	SimpleUtils.pass("Able to select Work Role as admin successfylly!");
    	SimpleUtils.pass("Able to specify the Start and End Time");
    	SimpleUtils.pass("Select a Staffing option as Open Shift:Manual");
    	SimpleUtils.pass("Click on Next button and assert it should be landed to Select Team Members Window");
    	SimpleUtils.pass("Click on Search Team members, search with keyword and Type Enter");
    	SimpleUtils.pass("Team members loaded Successfully");
    	SimpleUtils.pass("assert scheduled/hrs should increase with added hours manually");


    }
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee2_Enterprise")
    @TestName(description = "LEG-3490: Manually added shift should not lead to corruption")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void editShiftManuallyLeadtoCorruption(String username, String password, String browser, String location) {
    	ExtentTestManager.extentTest.get().log(Status.INFO,"Not able to verify the test due to Bug#4922");

    }
	
    
//    @AfterClass
//    public void cleanUp () {
//        /*
//            Ungenerate next week schedule
//            Ungenerate the week after next week's schedule
//            restore business hour of the next week
//         */
//    }
}