package com.legion.tests.core;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.legion.tests.annotations.*;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.legion.pages.BasePage;
import com.legion.pages.ControlsNewUIPage;
import com.legion.pages.DashboardPage;
import com.legion.pages.LocationSelectorPage;
import com.legion.pages.TimeSheetPage;
import com.legion.tests.TestBase;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.CsvUtils;
import com.legion.utils.JsonUtil;
import com.legion.utils.LegionRestAPI;
import com.legion.utils.SimpleUtils;
import com.legion.utils.SpreadSheetUtils;

import static com.legion.utils.MyThreadLocal.getModuleName;
import static com.legion.utils.MyThreadLocal.getSectionID;
import static com.legion.utils.MyThreadLocal.setSectionID;

public class TimeSheetTest extends TestBase{
	
	private static HashMap<String, String> addTimeClockDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/AddTimeClock.json");
	private static HashMap<String, String> updateTimeClockDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/UpdateTimeClock.json");


	public enum dayWeekOrPayPeriodViewType{
		  Next("Next"),
		  Previous("Previous");
			private final String value;
			dayWeekOrPayPeriodViewType(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
	}
	
	public enum dayWeekOrPayPeriodCount{
		Zero(0),
		One(1),
		Two(2),
		Three(3),
		Four(4),
		Five(5);		
		private final int value;
		dayWeekOrPayPeriodCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
	}
	
	@Override
	@BeforeMethod
	public void firstTest(Method method, Object[] params) throws Exception {
		  this.createDriver((String) params[0], "68", "Linux");
	      visitPage(method);
//	      loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
	}
	

	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP-119: Automation TA module : validate if it is possible to delete a clock in entry.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateAndDeleteATimeSheetClockAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
			
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        
        // Click on "Timesheet" option menu.
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        
        // Spot a timesheet with a clock in and out entered
        // Click on that time sheet to open the edit modal
        
        timeSheetPage.openATimeSheetWithClockInAndOut();
        int clickIndex = 0;
        
        // click in the edit button of a clock in
        timeSheetPage.clickOnEditTimesheetClock(clickIndex);
        
        //click in the delete button
        // Validate you are requested to confirm
        timeSheetPage.clickOnDeleteClockButton();
        
        //click on the clock mark to confirm
        ArrayList<String> clockmarksInfo = timeSheetPage.hoverOnClockIconAndGetInfo();
        for(String clockInfo : clockmarksInfo)
        {
        	SimpleUtils.report("Time Sheet Page: Clock Icon Information After Edit: '"+ clockInfo +"'");
        }
        
        timeSheetPage.closeTimeSheetDetailPopUp();
        
    }

	//Updated by Gunjan
	@MobilePlatform(platform = "Android")
	@Automated(automated =  "Automated")
	@Owner(owner = "Gunjan")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Validate functioning of Add Time Clock button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyNewTimesheetEntryAddedAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

        // Click on "Timesheet" option menu.
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        String timeClockLocation = addTimeClockDetails.get("Location");
        String timeClockDate = addTimeClockDetails.get("Date");
        String timeClockEmployee = addTimeClockDetails.get("Employee");
        String timeClockWorkRole = addTimeClockDetails.get("Work_Role");
        String timeClockStartTime = addTimeClockDetails.get("Shift_Start");
        String timeClockEndTime = addTimeClockDetails.get("Shift_End");
		String breakStartTime = addTimeClockDetails.get("Break_Start");
		String breakEndTime = addTimeClockDetails.get("Break_End");
        String timeClockAddNote = addTimeClockDetails.get("Add_Note");
        String DaysInPast = addTimeClockDetails.get("DaysFromTodayInPast");
        timeSheetPage.addNewTimeClock(timeClockLocation, timeClockEmployee,timeClockWorkRole, timeClockStartTime, timeClockEndTime, breakStartTime, breakEndTime, timeClockAddNote, DaysInPast);
        timeSheetPage.valiadteTimeClock(timeClockLocation,timeClockEmployee, timeClockWorkRole, timeClockStartTime, timeClockEndTime, breakStartTime, breakEndTime, timeClockAddNote, DaysInPast);
        timeSheetPage.closeTimeSheetDetailPopUp();
	}

	//Updated by Gunjan
	@MobilePlatform(platform = "Android")
	@Automated(automated =  "Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Validate the correctness of values getting displayed in Timesheet smartcard")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyValuesInTimesheetSmartCardAsStoreManager(String browser, String username, String password, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		locationSelectorPage.changeLocation(location);
		TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

		// Click on "Timesheet" option menu.
		timeSheetPage.clickOnTimeSheetConsoleMenu();
		SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
		timeSheetPage.timesheetSmartCard();
	}

	//Updated by Gunjan
	@Automated(automated =  "Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Validate functioning of editing existing time clock entry")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void updateExistingTimesheetEntryAsStoreManager(String browser, String username, String password, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		locationSelectorPage.changeLocation(location);
		TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

		// Click on "Timesheet" option menu.
		timeSheetPage.clickOnTimeSheetConsoleMenu();
		SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
		String timeClockLocation = updateTimeClockDetails.get("Location");
		String timeClockEmployee = updateTimeClockDetails.get("Employee");
		String timeClockStartTime = updateTimeClockDetails.get("Shift_Start");
		String timeClockEndTime = updateTimeClockDetails.get("Shift_End");
		String timeClockAddNote = updateTimeClockDetails.get("Add_Note");
		String DaysInPast = updateTimeClockDetails.get("DaysFromTodayInPast");

		timeSheetPage.updateTimeClock(timeClockLocation,timeClockEmployee, timeClockStartTime, timeClockEndTime, timeClockAddNote, DaysInPast);
		timeSheetPage.closeTimeSheetDetailPopUp();
	}

	//Added by Gunjan
	@Automated(automated =  "Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Validate functionality of TimeSheet auto approval")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void timesheetAutoApprovalAsStoreManager(String browser, String username, String password, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		locationSelectorPage.changeLocation(location);
		TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

		// Click on "Timesheet" option menu.
		timeSheetPage.clickOnTimeSheetConsoleMenu();
		SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
		String timeClockLocation = updateTimeClockDetails.get("Location");
		String timeClockEmployee = updateTimeClockDetails.get("Employee");
		String timeClockStartTime = updateTimeClockDetails.get("Shift_Start");
		String timeClockEndTime = updateTimeClockDetails.get("Shift_End");
		String timeClockAddNote = updateTimeClockDetails.get("Add_Note");
		String DaysInPast = updateTimeClockDetails.get("DaysFromTodayInPast");

		timeSheetPage.timesheetAutoApproval(timeClockLocation,timeClockEmployee, timeClockStartTime, timeClockEndTime, timeClockAddNote);
		timeSheetPage.closeTimeSheetDetailPopUp();
	}

	//Added by Gunjan
	@MobilePlatform(platform = "Android")
	@Automated(automated =  "Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Validate the details of TMs records in timesheet tab")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyDetailsOfTMsRecordInTimeSheetAsStoreManager(String browser, String username, String password, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		locationSelectorPage.changeLocation(location);
		TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

		// Click on "Timesheet" option menu.
		timeSheetPage.clickOnTimeSheetConsoleMenu();
		SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
		timeSheetPage.verifyTMsRecordInTimesheetTab();
	}
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP-113 : Automation TA Module : Validate the number of hours in for the TS of the TM in the REG column is 8 and OT is 2.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateNumberOfHoursAfterAddingTimeClockAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        
        // Click on "Timesheet" option menu.
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        String timeClockLocation = addTimeClockDetails.get("Location");
        String timeClockDate = addTimeClockDetails.get("Date");
        String timeClockEmployee = addTimeClockDetails.get("Employee");
        String timeClockWorkRole = addTimeClockDetails.get("Work_Role");
        String timeClockStartTime = "09:00AM";
        String timeClockEndTime = "07:00pm";
        String timeClockAddNote = addTimeClockDetails.get("Add_Note");
		String DaysInPast = addTimeClockDetails.get("DaysFromTodayInPast");
        
//        timeSheetPage.addNewTimeClock(timeClockLocation, timeClockDate, timeClockEmployee,timeClockWorkRole, timeClockStartTime, timeClockEndTime, timeClockAddNote);
        HashMap<String, Float> allHours = timeSheetPage.getTimeClockHoursByDate(DaysInPast, timeClockEmployee);
		float regHours = allHours.get("regHours");
		float totalHours = allHours.get("totalHours");
		float dTHours = allHours.get("dTHours");
		float oTHours = allHours.get("oTHours");
		float expectedRegHours = 8;
		float expectedOTHours = totalHours - (regHours + dTHours);
		float expectedDTHours = totalHours - (regHours + oTHours);
		
		if(dTHours > 0)
			SimpleUtils.pass("Timesheet Total hours for user'"+ timeClockEmployee +"' found '" + totalHours + "' hours");
		
		if(regHours == expectedRegHours)
			SimpleUtils.pass("Timesheet Regular hours for user'"+ timeClockEmployee +"' found '" + regHours + "' hours");
		else
			SimpleUtils.fail("Timesheet Regular hours found'"+ regHours +"', expected '" + expectedRegHours + "' hours", true);
		
		if(oTHours == expectedOTHours)
			SimpleUtils.pass("Timesheet Overtime hours for user'"+ timeClockEmployee +"' found '" + oTHours + "' hours");
		else
			SimpleUtils.fail("Timesheet Overtime hours found'"+ oTHours +"', expected '" + expectedOTHours + "' hours", true);
		
		if(dTHours == expectedDTHours)
			SimpleUtils.pass("Timesheet Double Time hours for user'"+ timeClockEmployee +"' found '" + dTHours + "' hours");
		else
			SimpleUtils.fail("Timesheet Double Time hours found'"+ dTHours +"', expected '" + expectedDTHours + "' hours", true);
	}
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP-114: Automation TA module : Verify Manager can review past payperiod and cannot approve pending status.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyPastPayPeriodAndCanNotApprovePastPendingStatusAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet PayPeriod duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        timeSheetPage.clickOnDayView();
        SimpleUtils.pass("Timesheet Day View: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        timeSheetPage.navigateDayWeekOrPayPeriodToPastOrFuture(dayWeekOrPayPeriodViewType.Previous.getValue()
        		, dayWeekOrPayPeriodCount.One.getValue());
        SimpleUtils.pass("Timesheet Day View: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        timeSheetPage.openFirstPendingTimeSheet();
        SimpleUtils.assertOnFail("Manager can approve TimeSheet of past date: '" + timeSheetPage.getActiveDayWeekOrPayPeriod() + "'", 
        		(! timeSheetPage.isTimeSheetPopupApproveButtonActive()), false);  
        SimpleUtils.pass("Manager can not approve TimeSheet of past date: '" + timeSheetPage.getActiveDayWeekOrPayPeriod() + "'");  
        timeSheetPage.closeTimeSheetDetailPopUp();
	}
	
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP-115: Automation TA module : Verify Admin/Manager are alerted when a TM doesn't clock in and it displays a no show alert.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyNoShowAlertWhenTMDoesNotClockInAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet PayPeriod duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        String timeClockEmployee = addTimeClockDetails.get("Employee");
        SimpleUtils.assertOnFail("TimeSheet worker: '"+ timeClockEmployee +"' not found.",
        		timeSheetPage.seachAndSelectWorkerByName(timeClockEmployee) , false);
        String expectedAlertMessage = "No show";
        String textToVerifyOnTimesheetPopup = "No shifts scheduled today";
        boolean isScheduleShiftWithOutClockInFound = false;
        for(WebElement workersDayRow : timeSheetPage.getTimeSheetDisplayedWorkersDayRows()) {
        	HashMap<String, Float> timesheetWorkerDaysHours = timeSheetPage.getTimesheetWorkerHoursByDay(workersDayRow);
        	String[] workersDayRowText = workersDayRow.getText().split("\n");
        	if(timesheetWorkerDaysHours.get("regHours") == 0)
        	{
        		timeSheetPage.openWorkerDayTimeSheetByElement(workersDayRow);
            	if(! timeSheetPage.isTimesheetPopupModelContainsKeyword(textToVerifyOnTimesheetPopup))
            	{
            		isScheduleShiftWithOutClockInFound = true;
            		timeSheetPage.closeTimeSheetDetailPopUp();
            		Thread.sleep(1000);
            		String workerTimeSheetAlert = timeSheetPage.getWorkerTimeSheetAlert(workersDayRow);
            		if(workerTimeSheetAlert.toLowerCase().contains(expectedAlertMessage.toLowerCase()))
            			SimpleUtils.pass("Manager alerted when a TM ('" + timeClockEmployee + 
            					"') doesn't clock in and it displays a no show alert for duration: '" + workersDayRowText[0] +"'.");
            		else
            			SimpleUtils.fail("Manager is not alerted with message '" + expectedAlertMessage + "' when a TM ('" + 
            					timeClockEmployee + "') doesn't clock in for duration: '" + workersDayRowText[0] +"'.", false);
            		break;
            	}
            	timeSheetPage.closeTimeSheetDetailPopUp();
        	}
        }
        if(! isScheduleShiftWithOutClockInFound)
        	SimpleUtils.report("No Schedule Shift without clock-in found.");
	}
	
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP-116: Automation TA module : Verify Admin/Manager are alerted when a TM clocks in and he hadn't got a shift and gets a unschedule.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyUnscheduledAlertWhenTMClockInWhileNotHavingShiftAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet PayPeriod duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        String timeClockEmployee = addTimeClockDetails.get("Employee");
        SimpleUtils.assertOnFail("TimeSheet worker: '"+ timeClockEmployee +"' not found.",
        		timeSheetPage.seachAndSelectWorkerByName(timeClockEmployee) , false);
        String textToVerifyOnTimesheetPopup_1 = "No shifts scheduled today";
        String textToVerifyOnTimesheetPopup_2 = "No Timeclocks to Display";
        String expectedAlertMessage = "unscheduled";
        boolean isunScheduleClockFound = false;
        
        for(WebElement workersDayRow : timeSheetPage.getTimeSheetDisplayedWorkersDayRows()) {
        	String[] workersDayRowText = workersDayRow.getText().split("\n");
        	timeSheetPage.openWorkerDayTimeSheetByElement(workersDayRow);
        	if(timeSheetPage.isTimesheetPopupModelContainsKeyword(textToVerifyOnTimesheetPopup_1) && 
        			! timeSheetPage.isTimesheetPopupModelContainsKeyword(textToVerifyOnTimesheetPopup_2))
        	{
        		isunScheduleClockFound = true;
        		timeSheetPage.closeTimeSheetDetailPopUp();
        		Thread.sleep(1000);
        		String workerTimeSheetAlert = timeSheetPage.getWorkerTimeSheetAlert(workersDayRow);
        		if(workerTimeSheetAlert.toLowerCase().contains(expectedAlertMessage.toLowerCase()))
        			SimpleUtils.pass("Manager alerted '"+ expectedAlertMessage +"' when a TM ('" + timeClockEmployee + 
        					"') clock in while not having schedule shift for duration: '" + workersDayRowText[0] +"'.");
        		else
        			SimpleUtils.fail("Manager is not alerted with message ' " + expectedAlertMessage + "' when a TM ('" + 
        					timeClockEmployee + "') clock in while not having shift for the duration: '" + workersDayRowText[0] +"'.", false);
        		
        		break;
        	}
        	timeSheetPage.closeTimeSheetDetailPopUp();
        }
        if(! isunScheduleClockFound)
        	SimpleUtils.report("No unSchedule Clock found.");
	}
	
	
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP-117 : Automation TA module : Verify Admin/Manager are alerted when a TM misses a mealbreak gets a missed meal-break alert.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyMealBreakAlertAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet PayPeriod duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        String timeClockEmployee = addTimeClockDetails.get("Employee");
        SimpleUtils.assertOnFail("TimeSheet worker: '"+ timeClockEmployee +"' not found.",
        		timeSheetPage.seachAndSelectWorkerByName(timeClockEmployee) , false);
        String textToVerifyOnTimesheetPopup_1 = "Break:";
        float verifyTotalHours = 6;
        String expectedAlertMessage = "Missed Meal";
        boolean isTimeClockFound = false;
        
        for(WebElement workersDayRow : timeSheetPage.getTimeSheetDisplayedWorkersDayRows()) {
        	String[] workersDayRowText = workersDayRow.getText().split("\n");
           	HashMap<String, Float> workerDayRowHours = timeSheetPage.getTimesheetWorkerHoursByDay(workersDayRow);
        	if(workerDayRowHours.size() != 0)
        	{ 
        		float totalHours = workerDayRowHours.get("totalHours");
        		if(totalHours > verifyTotalHours)
        		{
        			timeSheetPage.openWorkerDayTimeSheetByElement(workersDayRow);
        			boolean isWorkerTookBreak = timeSheetPage.isTimesheetPopupModelContainsKeyword(textToVerifyOnTimesheetPopup_1);
        			timeSheetPage.closeTimeSheetDetailPopUp();
        			Thread.sleep(1000);
        			isTimeClockFound = true;
        			String workerTimeSheetAlert = timeSheetPage.getWorkerTimeSheetAlert(workersDayRow);
        			
        			if(isWorkerTookBreak)
        				SimpleUtils.pass("Verify Meal Break Alert: Worker: ' "+ timeClockEmployee +"' Took a break for the duration: '" 
        						+ workersDayRowText[0] +"'.");
        			else if (workerTimeSheetAlert.toLowerCase().contains(expectedAlertMessage.toLowerCase()))
            			SimpleUtils.pass("Manager alerted '"+ expectedAlertMessage +"' when a TM ('" + timeClockEmployee + 
            					"') missed the meal break for duration: '" + workersDayRowText[0] +"'.");
            		else
            			SimpleUtils.fail("Manager is not alerted with message ' " + expectedAlertMessage + "' when a TM ('" + 
            					timeClockEmployee + "') missed the meal break for the duration: '" + workersDayRowText[0] +"'.", false);
            		
            		break;
        		}
        	}
        }
        if(! isTimeClockFound)
        	SimpleUtils.report("No Time clock with more than '"+ verifyTotalHours + " Hours' found for Worker: '"+ timeClockEmployee +"'.");
	}
	
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP-118: Automation TA module : Verify Manager can review detail of TM and approve it.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void reviewTimeClockAndApproveAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet Day duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        String timeClockEmployee = addTimeClockDetails.get("Employee");
        String date = addTimeClockDetails.get("Date"); 
    	String workRole = addTimeClockDetails.get("Work_Role");
    	String startTime = addTimeClockDetails.get("Shift_Start");
    	String endTime = addTimeClockDetails.get("Shift_End");
    	String notes = addTimeClockDetails.get("Add_Note");
    	
//        timeSheetPage.addNewTimeClock(location, date, timeClockEmployee, workRole, startTime, endTime, notes);
//        timeSheetPage.valiadteTimeClock(location, date, timeClockEmployee, workRole, startTime, endTime, notes);
        
        SimpleUtils.assertOnFail("Time Clock: approve button not active for '"+ timeClockEmployee 
					+"' and duration: '"+timeClockEmployee +"'.", timeSheetPage.isTimeSheetPopupApproveButtonActive(), false);
		
        timeSheetPage.clickOnApproveButton();
			
		SimpleUtils.assertOnFail("Time Clock: unable to approve timesheet for '"+ timeClockEmployee 
					+"' and duration: '"+timeClockEmployee +"'.", timeSheetPage.isTimeSheetApproved(), false);

		timeSheetPage.closeTimeSheetDetailPopUp();
 	}
	
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP- 120: Automation TA module : Validate Edit timesheet entry is saved in the story: Edit time is saved in history.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateEditTimeSheetEntryToBeSavedInHistoryAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet Pay Period duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        boolean isTimeClockFound = false;
        List<WebElement> allWorkersRow = timeSheetPage.getTimeSheetWorkersRow();
        for(WebElement workerRow: allWorkersRow)
    	{
    		String[] workerNameAndRole = timeSheetPage.getWorkerNameByWorkerRowElement(workerRow).split("\n");
    		BasePage basePage = new BasePage();
    		basePage.click(workerRow);
    		SimpleUtils.pass("Editing timeclock for the Worker :'"+ workerNameAndRole[0] +"' and duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"'.");
    		
    		List<WebElement> workerTimeClocks = timeSheetPage.getTimeSheetDisplayedWorkersDayRows();
    		String breakStartTime = "12:00";
    		String breakEndTime = "12:20";
    		if(workerTimeClocks.size() != 0)
    		{
    			isTimeClockFound = true;
    			timeSheetPage.openWorkerDayTimeSheetByElement(workerTimeClocks.get(0));
    			timeSheetPage.displayTimeClockHistory();
    			String timeClockHistoryBeforeModification = timeSheetPage.getTimeClockHistoryText();
    			timeSheetPage.closeTimeClockHistoryView();
    			
    			String noTimeClockText = "No Timeclocks to Display";
    			if(timeSheetPage.isTimesheetPopupModelContainsKeyword(noTimeClockText))
    				timeSheetPage.addTimeClockCheckInOnDetailPopupWithDefaultValue();
    			else
    				timeSheetPage.addBreakToOpenedTimeClock(breakStartTime, breakEndTime);
    			
    			timeSheetPage.displayTimeClockHistory();
    			String timeClockHistoryAfterModification = timeSheetPage.getTimeClockHistoryText();
    			timeSheetPage.closeTimeClockHistoryView();
    			
    			timeSheetPage.closeTimeSheetDetailPopUp();
    			if(timeClockHistoryBeforeModification.equals(timeClockHistoryAfterModification))
    				SimpleUtils.fail("Time clock History not updated after modification", false);
    			else
    				SimpleUtils.pass("Time clock History updated after modification.");
    		}
    		
    		if(isTimeClockFound)
    			break;
    	}
 	}
	
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP- 122: Automation TA module : Verify if auto approved shift is unapproved, shift with less than 5.5 hours.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyAutoApprovedShiftIsUnApprovedAfterDeletingClockOutAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet Pay Period duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        boolean isTimeClockFound = false;
        List<WebElement> allWorkersRow = timeSheetPage.getTimeSheetWorkersRow();
        for(WebElement workerRow: allWorkersRow)
    	{
    		String[] workerNameAndRole = timeSheetPage.getWorkerNameByWorkerRowElement(workerRow).split("\n");
    		BasePage basePage = new BasePage();
    		basePage.click(workerRow);
    		SimpleUtils.pass("Editing timeclock for the Worker :'"+ workerNameAndRole[0] +"' and duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"'.");
    		
    		List<WebElement> workerTimeClocks = timeSheetPage.getTimeSheetDisplayedWorkersDayRows();
    		for(WebElement workerTimeClock : workerTimeClocks)
    		{
    			HashMap<String, Float> workerDayRowHours = timeSheetPage.getTimesheetWorkerHoursByDay(workerTimeClock);
    			for(Map.Entry<String, Float> entry : workerDayRowHours.entrySet())
    			{
    				System.out.println(entry.getKey()+" : "+entry.getValue());
    			}
    			if(timeSheetPage.isTimeClockApproved(workerTimeClock))
    			{
    				String clockLabel = "Clock Out";
        			timeSheetPage.openWorkerDayTimeSheetByElement(workerTimeClock);
        			timeSheetPage.removeTimeClockEntryByLabel(clockLabel);
        			timeSheetPage.closeTimeSheetDetailPopUp();
        			break;
    			}
    		}
    		
    		/*
    		 * To Be complete ...
    		 */
    		
    		basePage.click(workerRow);
    		break;
    	}
 	}
	
	@UseAsTestRailId(testRailId = 4)
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP- 134: Validate the columns present in Time sheet.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyTimeSheetColumnsAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        //LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        //locationSelectorPage.changeLocation(location);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        SimpleUtils.pass("Timesheet Pay Period duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
    	List<WebElement> allWorkersRow = timeSheetPage.getTimeSheetWorkersRow();
    	
    	float sumOfDaysRegHours = 0;
    	float sumOfDaysOTHours = 0;
    	float sumOfDaysDTHours = 0;
    	float sumOfDaysHolHours = 0;
    	float sumOfDaysTotalHours = 0;
    	float sumOfDaysSchedHours = 0;
    	float sumOfDaysDiffHours = 0;
    	float sumOfDaysTipsHours = 0;
    	float sumOfDaysMealHours = 0;
    	
    	float totalRegHours = 0;
    	float totalOTHours = 0;
    	float totalDTHours = 0;
    	float totalHolHours = 0;
    	float totalTotalHours = 0;
    	float totalSchedHours = 0;
    	float totalDiffHours = 0;
    	float totalTipsHours = 0;
    	float totalMealHours = 0;
    	
    	for(WebElement workerRow: allWorkersRow)
    	{
    		
    		String[] workerNameAndRole = timeSheetPage.getWorkerNameByWorkerRowElement(workerRow).split("\n");
    		HashMap<String, Float> workerTotalHours = timeSheetPage.getWorkerTotalHours(workerRow);
    		totalRegHours = workerTotalHours.get("RegHours");
    		totalOTHours = workerTotalHours.get("OTHours");
    		totalDTHours = workerTotalHours.get("DTHours");
    		totalHolHours = workerTotalHours.get("HolHours");
    		totalTotalHours = workerTotalHours.get("TotalHours");
    		totalSchedHours = workerTotalHours.get("SchedHours");
    		totalDiffHours = workerTotalHours.get("DiffHours");
    		totalTipsHours = workerTotalHours.get("TipsHours");
    		totalMealHours = workerTotalHours.get("MealHours");
    		
    		BasePage basePage = new BasePage();
    		basePage.click(workerRow);
    		SimpleUtils.pass("Validating columns present in Time sheet for the Worker :'"+ 
    				workerNameAndRole[0] +"' and duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"'.");
    		
    		// Checkbox validation
    		if(timeSheetPage.isTimeSheetWorkerRowContainsCheckbox(workerRow))
    			SimpleUtils.pass("Checkbox displayed for worker('"+ workerNameAndRole[0] +"') in timesheet list.");
    		else
    			SimpleUtils.fail("Checkbox not displayed for worker('"+ workerNameAndRole[0] +"') timesheet.", true);
    		
    		for(WebElement workersDayRow : timeSheetPage.getTimeSheetDisplayedWorkersDayRows()) {
               	HashMap<String, Float> workerDayRowHours = timeSheetPage.getTimesheetWorkerHoursByDay(workersDayRow);               	
               	sumOfDaysRegHours = sumOfDaysRegHours + workerDayRowHours.get("regHours");
               	sumOfDaysOTHours = sumOfDaysOTHours + workerDayRowHours.get("oTHours");
               	sumOfDaysDTHours = sumOfDaysDTHours + workerDayRowHours.get("dTHours");
               	sumOfDaysHolHours = sumOfDaysHolHours + workerDayRowHours.get("holHours");
               	sumOfDaysTotalHours = sumOfDaysTotalHours + workerDayRowHours.get("totalHours");
               	sumOfDaysSchedHours = sumOfDaysSchedHours + workerDayRowHours.get("schedHours");
               	sumOfDaysDiffHours = sumOfDaysDiffHours + workerDayRowHours.get("diffHours");
               	sumOfDaysTipsHours = sumOfDaysTipsHours + workerDayRowHours.get("tipsHours");
               	sumOfDaysMealHours = sumOfDaysMealHours + workerDayRowHours.get("mealHours");
               	
               	// Location validation
               	timeSheetPage.vadidateWorkerTimesheetLocationsForAllTimeClocks(workersDayRow);
               	
    		}
    		break;
    	}
    	
    	
    	// Hours validation
    	if(totalRegHours == sumOfDaysRegHours)
    		SimpleUtils.pass("Timesheet: worker's Regular Hours equal to sum of days timeclock Regular Hours ("+totalRegHours+"/"+sumOfDaysRegHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Regular Hours not equal to sum of days timeclock Regular Hours ("+totalRegHours+"/"+sumOfDaysRegHours+").", true);
    	
    	if(totalOTHours == sumOfDaysOTHours)
    		SimpleUtils.pass("Timesheet: worker's Overtime Hours equal to sum of days timeclock Overtime Hours ("+totalOTHours+"/"+sumOfDaysOTHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Overtime Hours not equal to sum of days timeclock Overtime Hours ("+totalOTHours+"/"+sumOfDaysOTHours+").", true);
    	
    	if(totalDTHours == sumOfDaysDTHours)
    		SimpleUtils.pass("Timesheet: worker's Double Time Hours equal to sum of days timeclock Double Time Hours ("+totalDTHours+"/"+sumOfDaysDTHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Double Time Hours not equal to sum of days timeclock Double Time Hours ("+totalDTHours+"/"+sumOfDaysDTHours+").", true);
    	
    	if(totalHolHours == sumOfDaysHolHours)
    		SimpleUtils.pass("Timesheet: worker's Holiday Hours equal to sum of days timeclock Holiday Hours("+totalHolHours+"/"+sumOfDaysHolHours+"). ");
    	else
    		SimpleUtils.fail("Timesheet: worker's Holiday Hours not equal to sum of days timeclock Holiday Hours ("+totalHolHours+"/"+sumOfDaysHolHours+").", true);
    	
    	if(totalTotalHours == sumOfDaysTotalHours)
    		SimpleUtils.pass("Timesheet: worker's Total Hours equal to sum of days timeclock Total Hours ("+totalTotalHours+"/"+sumOfDaysTotalHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Total Hours not equal to sum of days timeclock Total Hours ("+totalTotalHours+"/"+sumOfDaysTotalHours+").", true);
    	
    	if(totalSchedHours == sumOfDaysSchedHours)
    		SimpleUtils.pass("Timesheet: worker's Scheduled Hours equal to sum of days timeclock Scheduled Hours ("+totalSchedHours+"/"+sumOfDaysSchedHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Scheduled Hours not equal to sum of days timeclock Scheduled Hours ("+totalSchedHours+"/"+sumOfDaysSchedHours+").", true);
    	
    	if(totalDiffHours == sumOfDaysDiffHours)
    		SimpleUtils.pass("Timesheet: worker's Difference Hours equal to sum of days timeclock Difference Hours ("+totalDiffHours+"/"+sumOfDaysDiffHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Difference Hours not equal to sum of days timeclock Difference Hours ("+totalDiffHours+"/"+sumOfDaysDiffHours+").", true);
    	
    	if(totalTipsHours == sumOfDaysTipsHours)
    		SimpleUtils.pass("Timesheet: worker's Tips Hours equal to sum of days timeclock Tips Hours ("+totalTipsHours+"/"+sumOfDaysTipsHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Tips Hours not equal to sum of days timeclock Tips Hours ("+totalTipsHours+"/"+sumOfDaysTipsHours+").", true);
    	
    	if(totalMealHours == sumOfDaysMealHours)
    		SimpleUtils.pass("Timesheet: worker's Meal Hours equal to sum of days timeclock Meal Hours ("+totalMealHours+"/"+sumOfDaysMealHours+").");
    	else
    		SimpleUtils.fail("Timesheet: worker's Meal Hours not equal to sum of days timeclock Meal Hours ("+totalMealHours+"/"+sumOfDaysMealHours+").", true);
   
 	}
	
	@UseAsTestRailId(testRailId = 4)
	@Automated(automated =  "Automated")
	@Owner(owner = "Nishant")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TestRail API: Add Test Cases.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void addTestRailTestCaseAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
		ArrayList<HashMap<String, String>> spreadSheetData = SpreadSheetUtils.readExcel("src/test/resources/TMViewTCs.xlsx", "Schedule");
		for(HashMap<String, String> spreadSheetRow : spreadSheetData)
		{
			 String defaultAction = "";
			 String scenario = spreadSheetRow.get("Scenario/Module");
			 String summary = spreadSheetRow.get("Summary");
			 String testSteps = spreadSheetRow.get("Test Steps");
			 String expectedResult = spreadSheetRow.get("Expected Result");
			 String actualResult = spreadSheetRow.get("Actual Result");
			 String testData = spreadSheetRow.get("Test Data");
			 String preconditions = spreadSheetRow.get("Preconditions");
			 String testCaseType = spreadSheetRow.get("Test Case Type");
			 String priority = spreadSheetRow.get("Priority/Severity");
			 String isAutomated = spreadSheetRow.get("Automated (Y/N)");
			 String result = spreadSheetRow.get("Result (Pass/Fail)");
			 String action = spreadSheetRow.get("Action");
			 SimpleUtils.addSectionId(scenario);
//			 int sectionID = Integer.valueOf(spreadSheetRow.get("Section_ID"));
			 
			 if(action != null && action.trim().length() > 0)
				 defaultAction = action.toLowerCase();
			 
			if(summary == null || summary.trim().length() == 0)
				summary = "Title is missing on SpreadSheet"; 

//			if(defaultAction.contains("add"))
				SimpleUtils.addTestCase(scenario , summary, testSteps, expectedResult, actualResult, testData, 
						preconditions, testCaseType, priority, isAutomated, result, action, getSectionID());
		}
	}
	
	@UseAsTestRailId(testRailId = 4)
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TestRail API: Update Test Cases.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void updateTestRailTestCaseAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
		ArrayList<HashMap<String, String>> spreadSheetData = SpreadSheetUtils.readExcel("src/test/resources/TCs_Legion.xlsx", "Schedule>Schedule");
		for(HashMap<String, String> spreadSheetRow : spreadSheetData)
		{
			String defaultAction = "";
			String scenario = spreadSheetRow.get("Scenario/Module");
			String summary = spreadSheetRow.get("Summary");
			String testSteps = spreadSheetRow.get("Test Steps");
			String expectedResult = spreadSheetRow.get("Expected Result");
			String actualResult = spreadSheetRow.get("Actual Result");
			String testData = spreadSheetRow.get("Test Data");
			String preconditions = spreadSheetRow.get("Preconditions");
			String testCaseType = spreadSheetRow.get("Test Case Type");
			String priority = spreadSheetRow.get("Priority/Severifty");
			String isAutomated = spreadSheetRow.get("Automated (Y/N)");
			String result = spreadSheetRow.get("Result (Pass/Fail)");
			String action = spreadSheetRow.get("Action");
			int sectionID = Integer.valueOf(spreadSheetRow.get("Section_ID"));

			if(action != null && action.trim().length() > 0)
				 defaultAction = action.toLowerCase();
			
			if(summary == null || summary.trim().length() == 0)
				summary = "Title is missing on SpreadSheet"; 


			if(defaultAction.contains("update"))
				SimpleUtils.updateTestCase(scenario , summary, testSteps, expectedResult, actualResult, testData, 
						preconditions, testCaseType, priority, isAutomated, result, action, sectionID);
		}
	}
	
	@UseAsTestRailId(testRailId = 4)
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TestRail API: Delete Test Cases.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void deleteTestRailTestCaseAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
		ArrayList<HashMap<String, String>> spreadSheetData = SpreadSheetUtils.readExcel("src/test/resources/TCs_Legion.xlsx", "Schedule>Schedule");
		for(HashMap<String, String> spreadSheetRow : spreadSheetData)
		{
			String defaultAction = "";
		    HashMap<String,String> testRailConfig = JsonUtil.getPropertiesFromJsonFile("src/test/resources/TestRailCfg.json");
			int projectId = Integer.valueOf(testRailConfig.get("TEST_RAIL_PROJECT_ID"));
			String title = spreadSheetRow.get("Summary");
			String action = spreadSheetRow.get("Action");
			int sectionID = Integer.valueOf(spreadSheetRow.get("Section_ID"));
			if(action != null && action.trim().length() > 0)
				 defaultAction = action.toLowerCase();
			
			if(defaultAction.contains("remove"))
				SimpleUtils.deleteTestCaseByTitle(title, projectId, sectionID);
		}
	}
	
	@UseAsTestRailId(testRailId = 4)
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TestRail API: Delete Test Cases.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void getResponseFromLegionRestAPIAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("enterpriseName", "KendraScott2"); // Cast
		requestParams.put("sourceSystem", "legion");
		requestParams.put("userName", "Ida.D");
		requestParams.put("passwordPlainText", "Ida.D");
		String requestURL = "https://enterprise-stage.legion.work/legion/authentication/login";
		JSONObject legionResponse = LegionRestAPI.getLegionAPIResponse(requestURL , requestParams);
		System.out.println("Response Time");
		System.out.println(legionResponse);
	}
	
	
	
	@UseAsTestRailId(testRailId = 4)
	@Automated(automated =  "Automated")
	@Owner(owner = "Naval")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "TP- 149: Timesheet :- Validate loading of Timesheet for any pay period[No spinning icon or blank screen].")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateLoadingOfTimeSheetForEveryPeriodsAsStoreManager(String browser, String username, String password, String location)
    		throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        timeSheetPage.clickOnDayView();
        SimpleUtils.assertOnFail("TimeSheet Details Table not loaded for duration type '"+ timeSheetPage.getTimeSheetActiveDurationType() 
        	+"'.",timeSheetPage.isTimeSheetDetailsTableLoaded() , true);
        
        timeSheetPage.clickOnWeekView();
        SimpleUtils.assertOnFail("TimeSheet Details Table not loaded for duration type '"+ timeSheetPage.getTimeSheetActiveDurationType() 
    	+"'.",timeSheetPage.isTimeSheetDetailsTableLoaded() , true);
        
        timeSheetPage.clickOnPayPeriodDuration();
        SimpleUtils.assertOnFail("TimeSheet Details Table not loaded for duration type '"+ timeSheetPage.getTimeSheetActiveDurationType() 
    	+"'.",timeSheetPage.isTimeSheetDetailsTableLoaded() , true);
	}
	
	@Automated(automated ="Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Validate Export timesheet feature")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTimeSheetExportFeatureAsInternalAdmin(String username, String password
			, String browser, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
	    SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
	    controlsNewUIPage.clickOnControlsConsoleMenu();
	    SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
	    controlsNewUIPage.clickOnControlsTimeAndAttendanceCard();
	    controlsNewUIPage.clickOnGlobalLocationButton();
	    controlsNewUIPage.clickOnControlsTimeAndAttendanceAdvanceBtn();
	    controlsNewUIPage.selectTimeSheetExportFormatByLabel("Standard");
	    TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
        timeSheetPage.clickOnWeekView();
        SimpleUtils.assertOnFail("TimeSheet Details Table not loaded for duration type '"+ timeSheetPage.getTimeSheetActiveDurationType() 
    	+"'.",timeSheetPage.isTimeSheetDetailsTableLoaded() , true);
		// Exporting Timesheet
	    String verifyFileExtention = "csv";
	    //analyticsPage.clickOnAnalyticsSubTab(analyticsReportSubTabLabel);
	    //String scheduleKPITitle = "Forecast, Schedule and Clock KPI Daily";
	    String downloadDirPath = propertyMap.get("Download_File_Default_Dir");
	    int fileCounts = SimpleUtils.getDirectoryFilesCount(downloadDirPath);	
	    float clockRegularHours = 0;	
	    float clockOvertimeHours = 0;
	    float clockDoubleTimeHours = 0;
		String activeDay = timeSheetPage.getActiveDayWeekOrPayPeriod();
	    timeSheetPage.exportTimesheet();
	    Thread.sleep(2000);
	    if(SimpleUtils.getDirectoryFilesCount(downloadDirPath) > fileCounts) {
		    File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
		    String fileName = latestFile.getName();
		    SimpleUtils.pass("Timesheet Exported successfully with name: '"+ fileName +"' for pay period " + activeDay.substring(10));
		    String downloadedFileExtention = fileName.split("\\.")[1];
		    if(downloadedFileExtention.equalsIgnoreCase(verifyFileExtention) 
		    		|| downloadedFileExtention.toLowerCase().contains(verifyFileExtention))
		    	SimpleUtils.pass("Timesheet Page: Export timesheet downloaded file extention('"
		    		+ downloadedFileExtention +"') matched with '"+verifyFileExtention+"'.");
		    else
		    	SimpleUtils.fail("Timesheet Page: Export timesheet downloaded file extention('"
			    		+ downloadedFileExtention +"') not matched with '"+verifyFileExtention+"'.", true);
		    ArrayList<HashMap<String,String>> timeSheetExportResponse = CsvUtils.getDataFromCSVFileWithHeader(downloadDirPath+"/"+fileName);
		    for(HashMap<String,String> timeSheetExportRow : timeSheetExportResponse)
		    {
		    	clockRegularHours += Float.valueOf(timeSheetExportRow.get("Regular Hours"));	
			    clockOvertimeHours += Float.valueOf(timeSheetExportRow.get("Overtime Hours"));	
			    clockDoubleTimeHours += Float.valueOf(timeSheetExportRow.get("Double Time Hours"));	
		    }
		
		    // Get Timesheet hours data
		    HashMap<String,Float> timeSheetCarouselCardsHours = timeSheetPage.getTotalTimeSheetCarouselCardsHours();
		    
		    // Validating TimeSheet regular hours with exported file regular hours
		    if(clockRegularHours == timeSheetCarouselCardsHours.get("regularHours"))
		    	SimpleUtils.pass("Analytics Carousel Card's Regular hours matched with Expoted TimeSheet Regular Hours ('"
		    			+clockRegularHours+"/"+timeSheetCarouselCardsHours.get("regularHours")+"').");
			else
			    SimpleUtils.fail("Analytics Carousel Card's Regular hours not matched with Expoted TimeSheet Regular Hours ('"
			    		+clockRegularHours+"/"+timeSheetCarouselCardsHours.get("regularHours")+"').", true);
		    
		 // Validating TimeSheet Overtime hours with exported file Overtime hours
		    if(clockOvertimeHours == timeSheetCarouselCardsHours.get("overtimeHours"))
		    	SimpleUtils.pass("Analytics Carousel Card's Overtime hours matched with Expoted TimeSheet Overtime Hours ('"
		    			+clockOvertimeHours+"/"+timeSheetCarouselCardsHours.get("overtimeHours")+"').");
			else
			    SimpleUtils.fail("Analytics Carousel Card's Overtime hours not matched with Expoted TimeSheet Overtime Hours ('"
			    		+clockOvertimeHours+"/"+timeSheetCarouselCardsHours.get("overtimeHours")+"').", true);
		    
		 // Validating TimeSheet regular hours with exported file regular hours
		    if(clockDoubleTimeHours == timeSheetCarouselCardsHours.get("doubleTimeHours"))
		    	SimpleUtils.pass("Analytics Carousel Card's DoubleTime hours matched with Expoted TimeSheet DoubleTime Hours ('"
		    			+clockDoubleTimeHours+"/"+timeSheetCarouselCardsHours.get("doubleTimeHours")+"').");
			else
			    SimpleUtils.fail("Analytics Carousel Card's DoubleTime hours not matched with Expoted TimeSheet DoubleTime Hours ('"
			    		+clockDoubleTimeHours+"/"+timeSheetCarouselCardsHours.get("doubleTimeHours")+"').", true);		 
	    }
	    else
		    SimpleUtils.fail("Timesheet Not Exported.", false);
	    
	    

	    // To Do
	    /*List<WebElement> allWorkersRow = timeSheetPage.getTimeSheetWorkersRow();
        for(WebElement workerRow: allWorkersRow)
    	{
    		String[] workerNameAndRole = timeSheetPage.getWorkerNameByWorkerRowElement(workerRow).split("\n");
    		BasePage basePage = new BasePage();
    		basePage.click(workerRow);
    		SimpleUtils.pass("Editing timeclock for the Worker :'"+ workerNameAndRole[0] +"' and duration: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"'.");
    		
    		//List<WebElement> workerTimeClocks = timeSheetPage.getTimeSheetDisplayedWorkersDayRows();
		    for(WebElement timeSheetRow : timeSheetPage.getTimeSheetDisplayedWorkersDayRows())
		    {
		    	System.out.println("\n****************************************************\n");
		    	System.out.println(timeSheetRow.getText());
		    }
		    basePage.click(workerRow);
    	}*/
	}


//    @Automated(automated =  "Automated")
//    @Owner(owner = "Nishant")
//    @SanitySuite(sanity =  "Sanity")
//    @Enterprise(name = "Coffee_Enterprise")
//    @TestName(description = "Validate Due Date SmartCard for Manager and PayrollAdmin")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void validateDueDateSmartCardAsStoreManager(String browser, String username, String password, String location)
//            throws Exception {
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
//        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//        controlsNewUIPage.clickOnControlsConsoleMenu();
//        SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//        controlsNewUIPage.clickOnControlsTimeAndAttendanceCard();
//        controlsNewUIPage.clickOnGlobalLocationButton();
//        String timesheetApprovalVal = controlsNewUIPage.getTimeSheetApprovalSelectedOption(false);
//        LocalDate now = LocalDate.now();
//        timeSheetPage.clickOnTimeSheetConsoleMenu();
//        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
//        timeSheetPage.clickOnPPWeeklyDuration();
//        String activeDay = timeSheetPage.getActiveDayWeekOrPayPeriod();
//        String endOfPayPeriod = activeDay.substring(activeDay.length()-2).trim();
//        String timesheetDueDate = timeSheetPage.verifyTimesheetDueHeader();
//        LocalDate wanted = LocalDate.now().plusDays(Integer.parseInt(timesheetDueDate));
//        String dateWanted = String.valueOf(wanted.getDayOfMonth());
//        String dueDate = timeSheetPage.verifyTimesheetSmartCard();
//        validateDueDate(dueDate, dateWanted, timesheetApprovalVal, activeDay);
//    }
//    public static void validateDueDate(String dueDate, String dateWanted, String timesheetApprovalVal, String activeDay){
//        if(dueDate.contains(dateWanted)){
//            SimpleUtils.pass("Timesheet Due Date value is " + timesheetApprovalVal + " after the end of pay period " + activeDay.substring(10) + " which is " + dueDate);
//		}else{
//            SimpleUtils.fail("Timesheet Due Date value is " + timesheetApprovalVal + " after the end of the " + activeDay.substring(10) + " which is " + dueDate,false);
//        }
//    }


	//added by Nishant

	@Automated(automated =  "Automated")
	@Owner(owner = "Nishant")
	@SanitySuite(sanity =  "Sanity")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Validate Location filter functionality works fine")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateLocationFilterAsInternalAdmin(String browser, String username, String password, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
		timeSheetPage.clickOnTimeSheetConsoleMenu();
		String locationFilterAllLocations = addTimeClockDetails.get("Location_Filter_All_Locations");
		String locationFilterDefaultLocations = addTimeClockDetails.get("Location_Filter_Default_Locations");
		String locationFilterSpecificLocations = addTimeClockDetails.get("Location_Filter_Specific_Location");
		SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
		timeSheetPage.clickOnWeekDuration();
		timeSheetPage.clickImmediatePastToCurrentActiveWeekInDayPicker();
		timeSheetPage.validateLocationFilterIfDefaultLocationSelected(locationFilterDefaultLocations);
		timeSheetPage.validateLocationFilterIfNoLocationSelected(locationFilterAllLocations);
		timeSheetPage.verifyTimesheetTableIfNoLocationSelected();
		timeSheetPage.validateLocationFilterIfSpecificLocationSelected(locationFilterSpecificLocations);
		List<WebElement> allWorkersRow = timeSheetPage.getTimeSheetWorkersRow();
		timeSheetPage.clickWorkerRow(allWorkersRow, locationFilterSpecificLocations);

	}

    @Automated(automated =  "Automated")
    @Owner(owner = "Nishant")
    @SanitySuite(sanity =  "Sanity")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Validate Due Date SmartCard for Manager and PayrollAdmin")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateDueDateSmartCardAsStoreManager(String browser, String username, String password, String location)
            throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
        controlsNewUIPage.clickOnControlsTimeAndAttendanceCard();
        controlsNewUIPage.clickOnGlobalLocationButton();
		String timesheetApprovalVal = controlsNewUIPage.getTimeSheetApprovalSelectedOption(true);
		LocalDate now = LocalDate.now();
		timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);
		timeSheetPage.clickOnPPWeeklyDuration();
		String activeDay = timeSheetPage.getActiveDayWeekOrPayPeriod();
		String endOfPayPeriod = activeDay.substring(activeDay.length()-2).trim();
		String timesheetDueDate = timeSheetPage.verifyTimesheetDueHeader();
		LocalDate wanted = LocalDate.now().plusDays(Integer.parseInt(timesheetDueDate));
		String dateWanted = String.valueOf(wanted.getDayOfMonth());
		String dueDate = timeSheetPage.verifyTimesheetSmartCard();
		validateDueDate(dueDate, dateWanted, timesheetApprovalVal, endOfPayPeriod);
    }

    public static void validateDueDate(String dueDate, String dateWanted, String timesheetApprovalVal, String endOfPayPeriod){
		if(dueDate.contains(dateWanted)){
			SimpleUtils.pass("Timesheet Due Date value is " + timesheetApprovalVal + " days after the end of the " + endOfPayPeriod + " which is " + dueDate);
		}else{
			SimpleUtils.fail("Timesheet Due Date value is not " + timesheetApprovalVal + " days after the end of the pay period which is incorrect behavior",false);
		}
	}

	@Automated(automated =  "Automated")
	@Owner(owner = "Nishant")
	@SanitySuite(sanity =  "Sanity")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Test Rail Id")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void getTestRailAsStoreManager(String browser, String username, String password, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.getTestRailId();
	}


}
