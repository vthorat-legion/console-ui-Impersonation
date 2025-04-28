package com.legion.tests.core;

import java.lang.reflect.Method;
import java.util.*;

import com.legion.pages.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;

public class ScheduleTest extends TestBase{
	  private static HashMap<String, String> propertyMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
	  private static HashMap<String, String> propertyBudgetValue = JsonUtil.getPropertiesFromJsonFile("src/test/resources/Budget.json");
	  private HashMap<String, Object[][]> swapCoverCredentials = null;
	  private List<String> swapCoverNames = null;
	  private String workRoleName = "";

	  @Override
	  @BeforeMethod()
	  public void firstTest(Method testMethod, Object[] params) throws Exception{
	  	try {
			this.createDriver((String) params[0], "69", "Window");
			visitPage(testMethod);
			loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	  }
	  public enum weekCount{
			Zero(0),
			One(1),
			Two(2),
			Three(3),
			Four(4),
			Five(5),
			Six(6);
			private final int value;
			weekCount(final int newValue) {
	            value = newValue;
	        }
	        public int getValue() { return value; }
		}

	  public enum overviewWeeksStatus{
		  NotAvailable("Not Available"),
		  Draft("Draft"),
		  Guidance("Guidance"),
		  Finalized("Finalized"),
		  Published("Published");

		  private final String value;
		  overviewWeeksStatus(final String newValue) {
            value = newValue;
          }
        public String getValue() { return value; }
		}


	  public enum SchedulePageSubTabText{
		  Overview("OVERVIEW"),
		  ProjectedSales("PROJECTED SALES"),
		  StaffingGuidance("STAFFING GUIDANCE"),
		  Schedule("SCHEDULE");
			private final String value;
			SchedulePageSubTabText(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
		}

	  public enum weekViewType{
		  Next("Next"),
		  Previous("Previous");
			private final String value;
			weekViewType(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
		}

	  public enum scheduleHoursAndWagesData{
		  scheduledHours("scheduledHours"),
		  budgetedHours("budgetedHours"),
		  otherHours("otherHours"),
		  wagesBudgetedCount("wagesBudgetedCount"),
		  wagesScheduledCount("wagesScheduledCount");
			private final String value;
			scheduleHoursAndWagesData(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
		}


//		@Automated(automated = "Automated")
//		@Owner(owner = "Naval")
//		@Enterprise(name = "Coffee2_Enterprise")
//	    @TestName(description = "TP-33: Hours and Wage calculation on Console-UI")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void hoursAndWagesCalculationOnSchedulePage(String username, String password, String browser, String location)
//	    		throws Exception {
////	        loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//	        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//	        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//	        SchedulePage schedulePage = dashboardPage.goToToday();
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//	        SimpleUtils.assertOnFail("Today's Schedule not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()) , true);
//	        //get Week view Hours & Wages
//			scheduleCommonPage.clickOnWeekView();
//	        HashMap<String, Float> scheduleWeekViewLabelData = smartCardPage.getScheduleLabelHoursAndWages();
//	        Float scheduleWeekScheduledHours = scheduleWeekViewLabelData.get(scheduleHoursAndWagesData.scheduledHours.getValue());
//	        Float scheduleWeekBudgetedHours = scheduleWeekViewLabelData.get(scheduleHoursAndWagesData.budgetedHours.getValue());
//	        Float scheduleWeekOtherHours = scheduleWeekViewLabelData.get(scheduleHoursAndWagesData.otherHours.getValue());
//	        Float scheduleWeekWagesBudgetedCount = scheduleWeekViewLabelData.get(scheduleHoursAndWagesData.wagesBudgetedCount.getValue());
//	        Float scheduleWeekWagesScheduledCount = scheduleWeekViewLabelData.get(scheduleHoursAndWagesData.wagesScheduledCount.getValue());
//
//	        //get days hours & Wages for current week
//			scheduleCommonPage.clickOnDayView();
//	        List<HashMap<String, Float>>  scheduleDaysViewLabelDataForWeekDays = smartCardPage.getScheduleLabelHoursAndWagesDataForEveryDayInCurrentWeek();
//	        Float scheduleDaysScheduledHoursTotal = (float) 0;
//	        Float scheduleDaysBudgetedHoursTotal = (float) 0;
//	        Float scheduleDaysOtherHoursTotal = (float) 0;
//	        Float scheduleDaysWagesBudgetedCountTotal = (float) 0;
//	        Float scheduleDaysWagesScheduledCountTotal = (float) 0;
//	        for(HashMap<String, Float> scheduleDaysViewLabelDataForWeekDay : scheduleDaysViewLabelDataForWeekDays)
//	        {
//	        	scheduleDaysScheduledHoursTotal = scheduleDaysScheduledHoursTotal + scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.scheduledHours.getValue());
//	        	scheduleDaysBudgetedHoursTotal = scheduleDaysBudgetedHoursTotal + scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.budgetedHours.getValue());
//	        	scheduleDaysOtherHoursTotal = scheduleDaysOtherHoursTotal + scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.otherHours.getValue());
//	        	scheduleDaysWagesBudgetedCountTotal = scheduleDaysWagesBudgetedCountTotal + scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.wagesBudgetedCount.getValue());
//	        	scheduleDaysWagesScheduledCountTotal = scheduleDaysWagesScheduledCountTotal + scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.wagesScheduledCount.getValue());
//	        }
//
//	        // Week Summary = Sum of Day Summary
//
//	        // Comparing Week Scheduled Hours and Sum of Days Scheduled Hours
//
//
//	       /* if(scheduleWeekScheduledHours.equals(scheduleDaysScheduledHoursTotal)) {
//	        	SimpleUtils.pass("Week Scheduled Hours are matched with Sum of Days Scheduled Hours ("+scheduleWeekScheduledHours+"/"
//	        			+scheduleDaysScheduledHoursTotal+")");
//	        }
//	        else {
//		        SimpleUtils.assertOnFail("Week Scheduled Hours not matched with Sum of Days Scheduled Hours (" +scheduleWeekScheduledHours+"/"
//		        		+scheduleDaysScheduledHoursTotal+ ")", scheduleWeekScheduledHours.equals(scheduleDaysScheduledHoursTotal), true);
//	        }
//
//	        if(scheduleWeekBudgetedHours.equals(scheduleDaysBudgetedHoursTotal)) {
//	        	SimpleUtils.pass("Week Scheduled Hours are matched with Sum of Days Scheduled Hours ("+scheduleWeekBudgetedHours+"/"
//	        			+scheduleDaysBudgetedHoursTotal);
//	        }
//	        else {
//		        SimpleUtils.assertOnFail("Week Budgeted Hours not matched with Sum of Days Budgeted Hours (" +scheduleWeekScheduledHours+ "/"
//		        		+ scheduleDaysBudgetedHoursTotal + ")", scheduleWeekBudgetedHours.equals(scheduleDaysBudgetedHoursTotal), true);
//	        }*/
//
//	        if(scheduleWeekScheduledHours != null && scheduleDaysScheduledHoursTotal != null)
//	           {
//	        	   if(scheduleWeekScheduledHours.equals(scheduleDaysScheduledHoursTotal)) {
//	   	        	SimpleUtils.pass("Schedule Page: Week Scheduled Hours matched with Sum of Days Scheduled Hours ("+scheduleWeekScheduledHours+"/"
//	   	        			+scheduleDaysScheduledHoursTotal+")");
//	        	   }
//	        	   else {
//	   		        SimpleUtils.assertOnFail("Schedule Page: Week Scheduled Hours not matched with Sum of Days Scheduled Hours (" +scheduleWeekScheduledHours+"/"
//	   		        		+scheduleDaysScheduledHoursTotal+ ")", scheduleWeekScheduledHours.equals(scheduleDaysScheduledHoursTotal), true);
//	        	   }
//	           }
//
//	           if(scheduleWeekBudgetedHours != null && scheduleDaysBudgetedHoursTotal != null)
//	           {
//	        	   if(scheduleWeekBudgetedHours.equals(scheduleDaysBudgetedHoursTotal)) {
//	   	        	SimpleUtils.pass("Schedule Page: Week Budgeted Hours matched with Sum of Days Budgeted Hours ("+scheduleWeekBudgetedHours+"/"
//	   	        			+scheduleDaysBudgetedHoursTotal);
//		   	        }
//		   	        else {
////		   		        SimpleUtils.assertOnFail("Schedule Page: Week Budgeted Hours not matched with Sum of Days Budgeted Hours (" +scheduleWeekBudgetedHours+ "/"
////		   		        		+ scheduleDaysBudgetedHoursTotal + ")", scheduleWeekBudgetedHours.equals(scheduleDaysBudgetedHoursTotal), true);
//		   		        SimpleUtils.report("Schedule Page: Week Budgeted Hours not matched with Sum of Days Budgeted Hours (" +scheduleWeekBudgetedHours+ "/"
//		   		        		+ scheduleDaysBudgetedHoursTotal + ")");
//		   	        }
//	           }
//
//	           if(scheduleWeekOtherHours != null && scheduleDaysOtherHoursTotal != null)
//	           {
//	        	   if(scheduleWeekOtherHours.equals(scheduleDaysOtherHoursTotal)) {
//	   	        	SimpleUtils.pass("Schedule Page: Week Other Hours matched with Sum of Days Other Hours ("+scheduleWeekOtherHours+"/"
//	   	        			+scheduleDaysOtherHoursTotal+")");
//	        	   }
//	        	   else {
//	   		        SimpleUtils.assertOnFail("Schedule Page: Week Other Hours not matched with Sum of Days Other Hours (" +scheduleWeekOtherHours+"/"
//	   		        		+scheduleDaysOtherHoursTotal+ ")", scheduleWeekOtherHours.equals(scheduleDaysOtherHoursTotal), true);
//	        	   }
//	           }
//
//	           if(scheduleWeekWagesBudgetedCount != null && scheduleDaysWagesBudgetedCountTotal != null)
//	           {
//	        	   if(scheduleWeekWagesBudgetedCount.equals(scheduleDaysWagesBudgetedCountTotal)) {
//	   	        	SimpleUtils.pass("Schedule Page: Week Budgeted Wages matched with Sum of Days Budgeted Wages ("+scheduleWeekWagesBudgetedCount+"/"
//	   	        			+scheduleDaysWagesBudgetedCountTotal);
//		   	        }
//		   	        else {
////		   		        SimpleUtils.assertOnFail("Schedule Page: Week Budgeted Wages not matched with Sum of Days Budgeted Wages (" +scheduleWeekWagesBudgetedCount+ "/"
////		   		        		+ scheduleDaysWagesBudgetedCountTotal + ")", scheduleWeekWagesBudgetedCount.equals(scheduleDaysWagesBudgetedCountTotal), true);
//		   		        SimpleUtils.report("Schedule Page: Week Budgeted Wages not matched with Sum of Days Budgeted Wages (" +scheduleWeekWagesBudgetedCount+ "/"
//		   		        		+ scheduleDaysWagesBudgetedCountTotal + ")");
//		   	        }
//	           }
//
//	           if(scheduleWeekWagesScheduledCount != null && scheduleDaysWagesScheduledCountTotal != null)
//	           {
//	        	   if(scheduleWeekWagesScheduledCount.equals(scheduleDaysWagesScheduledCountTotal)) {
//	   	        	SimpleUtils.pass("Schedule Page: Week Scheduled Wages matched with Sum of Days Scheduled Wages ("+scheduleWeekWagesScheduledCount+"/"
//	   	        			+scheduleDaysWagesScheduledCountTotal);
//		   	        }
//		   	        else {
//		   		        SimpleUtils.assertOnFail("Schedule Page: Week Scheduled Wages not matched with Sum of Days Scheduled Wages (" +scheduleWeekWagesScheduledCount+ "/"
//		   		        		+ scheduleDaysWagesScheduledCountTotal + ")", scheduleWeekWagesScheduledCount.equals(scheduleDaysWagesScheduledCountTotal), true);
//		   	        }
//	           }
//
//	  }
//
//	    @Automated(automated =  "Automated")
//		@Owner(owner = "Naval")
//	    @Enterprise(name = "Coffee2_Enterprise")
//	    @TestName(description = "LEG-2424: As a store manager, should be able to review past week's schedule and generate this week or next week's schedule")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void reviewPastGenerateCurrentAndFutureWeekSchedule(String username, String password, String browser, String location)
//	    		throws Exception {
//	    	int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//	        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//	        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//	        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//	        scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//	        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()) , true);
//	        //Schedule overview should show 5 week's schedule
//	        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//	        List<String> scheduleOverviewWeeksStatus = scheduleOverviewPage.getScheduleWeeksStatus();
//	        int overviewWeeksStatusCount = scheduleOverviewWeeksStatus.size();
//	        SimpleUtils.assertOnFail("Schedule overview Page not displaying upcoming 5 weeks",(overviewWeeksStatusCount == overviewTotalWeekCount) , true);
//	        System.out.println("overviewWeeksStatusCount: "+overviewWeeksStatusCount);
//	        for(String overviewWeeksStatusText: scheduleOverviewWeeksStatus)
//	        {
//	        	int index = scheduleOverviewWeeksStatus.indexOf(overviewWeeksStatusText);
////		        SimpleUtils.assertOnFail("Schedule overview Page upcoming week on index '"+index+"' is 'Not Available'",(! overviewWeeksStatusText.contains(overviewWeeksStatus.NotAvailable.getValue())) , true);
//		        SimpleUtils.report("Schedule overview Page upcoming week on index '"+index+"' is 'Not Available'");
//
//	        	System.out.println("overviewWeeksStatus: "+overviewWeeksStatusText);
//	        }
//
//
//	        //Must have at least "Past Week" schedule published
//	        scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
//			scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(weekViewType.Previous.getValue(), weekCount.One.getValue());
//	        SimpleUtils.assertOnFail("Schedule Page: Past week not generated!",createSchedulePage.isWeekGenerated() , true);
//	        SimpleUtils.assertOnFail("Schedule Page: Past week not Published!",createSchedulePage.isWeekPublished() , true);
//
//	        //The schedules that are already published should remain unchanged
//			scheduleCommonPage.clickOnDayView();
//	        scheduleMainPage.clickOnEditButton();
//	        SimpleUtils.assertOnFail("User can add new shift for past week", (! scheduleMainPage.isAddNewDayViewShiftButtonLoaded()) , true);
//	        scheduleMainPage.clickOnCancelButtonOnEditMode();
//
//	        // No generate button for Past Week
//	        SimpleUtils.assertOnFail("Generate Button displaying for Past week", (! createSchedulePage.isGenerateButtonLoaded()) , true);
//
//
//	        //there are at least one week in the future where schedule has not yet been published
//			scheduleCommonPage.clickOnWeekView();
//	        scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(weekViewType.Next.getValue(), weekCount.One.getValue());
//
//	        // to do -
//	        for(int index = 1; index < weekCount.values().length; index++)
//	        {
//	        	scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(weekViewType.Next.getValue(), weekCount.One.getValue());
//	        	if(! createSchedulePage.isWeekGenerated()){
//        			ExtentTestManager.getTest().log(Status.INFO, "Schedule Page: Future week '"+ scheduleCommonPage.getScheduleWeekStartDayMonthDate()+"' not Generated!");
//	        	}
//	        	else {
//	        		if(! createSchedulePage.isWeekPublished()){
//	        			ExtentTestManager.getTest().log(Status.INFO, "Schedule Page: Future week '"+ scheduleCommonPage.getScheduleWeekStartDayMonthDate()+"' not Published!");
//	        		}
//	        	}
//	        }
//	    }

//	    @Automated(automated ="Automated")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "KendraScott2_Enterprise")
//		@TestName(description = "FOR-596:Budget modal header should display the week instead of UNDEFINED")
//	    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	    public void enterBudgetPopUpHeaderStoreManager(String username, String password, String browser, String location) throws Throwable {
//
//			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//	    	scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//	    	schedulePage.validateBudgetPopUpHeader(weekViewType.Next.getValue(), weekCount.Two.getValue());
//	    }
//
//
//	    @Automated(automated ="Automated")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "KendraScott2_Enterprise")
//		@TestName(description = "TP-100: FOR-620: Budget smartcard shows budget hrs when no budget was entered ,if navigate from a week with budget")
//	    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	    public void noBudgetHourDisplayWhenBudgetNotEnteredStoreManager(String username, String password, String browser, String location) throws Throwable {
//
//			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//	    	scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//	    	schedulePage.noBudgetDisplayWhenBudgetNotEntered(weekViewType.Next.getValue(), weekCount.Two.getValue());
//	    }
//
//	    @Automated(automated ="Automated")
//  		@Owner(owner = "Gunjan")
//  		@Enterprise(name = "KendraScott2_Enterprise")
//  		@TestName(description = "Validate calculation of budget values for budget and schedule smartcard when budget is by hours or wages")
//  	    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//  	    public void budgetInScheduleNBudgetSmartCardStoreManager(String username, String password, String browser, String location) throws Throwable {
//
//			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//  	    	scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//  	    	int tolerance = Integer.parseInt(propertyBudgetValue.get("Tolerance"));
//			List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//			schedulePage.budgetInScheduleNBudgetSmartCard(weekViewType.Next.getValue(), weekCount.Two.getValue(), tolerance);
//  	    }
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-102: LEG 5500 : Budget Hours shown in budget modal 715 hrs does not match the budgeted hours shown in schedule 1287 hrs")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void budgetIntScheduleNBudgetSmartCardStoreManager(String username, String password, String browser, String location) throws Throwable {
//
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		int tolerance = Integer.parseInt(propertyBudgetValue.get("Tolerance"));
//		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//		schedulePage.budgetInScheduleNBudgetSmartCard(weekViewType.Next.getValue(), weekCount.Two.getValue(), tolerance);
//	}
//
//
////	@Automated(automated ="Automated")
////	@Owner(owner = "Gunjan")
////	@Enterprise(name = "KendraScott2_Enterprise")
////	@TestName(description = "Validate the budget calculation when budget is modified for any schedule week")
////	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
////	public void updateBudgetInScheduleNBudgetSmartCardStoreManager(String username, String password, String browser, String location) throws Throwable {
////
////		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
////		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
////		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
////		schedulePage.updatebudgetInScheduleNBudgetSmartCard(weekViewType.Next.getValue(), weekCount.One.getValue());
////	}
//
////	@Automated(automated ="Automated")
////	@Owner(owner = "Gunjan")
////	@Enterprise(name = "KendraScott2_Enterprise")
////	@TestName(description = "TP-102: LEG 5500 : Budget Wages shown in budget modal 715 hrs does not match the budgeted hours shown in schedule 1287 hrs")
////	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
////	public void budgetWagesInScheduleNBudgetSmartCardStoreManager(String username, String password, String browser, String location) throws Throwable {
////
////		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
////		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
////		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
////		schedulePage.budgetHourByWagesInScheduleNBudgetedSmartCard(weekViewType.Next.getValue(), weekCount.Two.getValue());
////	}
//
//	    @Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//	    @Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-4977: Republish Button is missing for finalized week")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterpriseP", dataProviderClass=CredentialDataProviderSource.class)
//	    public void shouldRepublishButtonDisplyedForFinalizedWeek(String username, String password, String browser, String location)
//				throws Exception
//	    {
//			SimpleUtils.pass("Login to leginTech Successfully");
//			SimpleUtils.pass("Successfully opened the Schedule app");
//			SimpleUtils.pass("Select date which is finalized week present in Schedule Overview");
//			SimpleUtils.pass("Republish button is visible");
//
//	    }
//
//	    @Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//	    @Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-5064: On click refresh, Publish/Republish button disappears")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void onRefreshPublishButtonDisappears(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login to leginCoffee Successfully");
//			SimpleUtils.pass("Successfully opened the Schedule app");
//			SimpleUtils.pass("Navigate to Oct15-Oct21 in Schedule tab");
//			SimpleUtils.pass("Click Refresh");
//			SimpleUtils.pass("assert on click refresh publish/republish button should not disappear");
//
//	    }
//
//	    @Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//	    @Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-4845: Changes for Schedule wages are not getting reflected after adding new shift in Day view")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void scheduleWagesDoesNotGetUpdatedForAdminShift(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login to LeginCoffee/LegionCoffee2 Successfully");
//			SimpleUtils.pass("Successfully opened the Schedule app");
//			SimpleUtils.pass("Add a Admin Shift Manual/Auto");
//			SimpleUtils.pass("assert schedule wages should get increased for Admin shift");
//
//	    }
//
//	    @Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//	    @Enterprise(name = "Tech_Enterprise")
//		@TestName(description = "TP-43: should be able to convert to open shift for Current date")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void shouldConvertToOpenShiftOption(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login to leginTech Successfully");
//			SimpleUtils.pass("Successfully opened the Schedule app");
//			SimpleUtils.pass("Successfully Opened a Schedule of Present day in Day view");
//			SimpleUtils.pass("Successfully created shift using Assign team member option");
//			SimpleUtils.pass("Click on edit button");
//			SimpleUtils.pass("Convert to Open shift option is coming for the shift created in previous step");
//
//	    }
//
//	    @Automated(automated = "Manual")
//	    @Owner(owner = "Gunjan")
//	    @Enterprise(name = "Tech_Enterprise")
//	    @TestName(description = "LEG-4845:Changes for Schedule wages are not getting reflected after adding new shift in Day view in LegionTech")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void scheduleWagesDoesNotChangeForNewAddedShift(String username, String password, String browser, String location)
//	           throws Exception
//	    {
//	        SimpleUtils.pass("Login to LegionTech Successfully");
//	        SimpleUtils.pass("Navigate to Schedule tab and Add a new");
//	        SimpleUtils.pass("Observe the change in Schedule wages");
//	        SimpleUtils.pass("assert schedule wages should have some value according to admin working hour ");
//
//	    }
//
//
//	    @Automated(automated = "Manual")
//	    @Owner(owner = "Gunjan")
//	    @Enterprise(name = "Coffee2_Enterprise")
//	    @TestName(description = "LEG-5110:Facing issue while deleting Shift using close icon in all the environments")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void scheduleDeletionNotWorking(String username, String password, String browser, String location)
//	           throws Exception
//	    {
//	        SimpleUtils.pass("Login to Environment Successfully");
//	        SimpleUtils.pass("Navigate to Schedule tab and Add a new shift by editing the schedule");
//	        SimpleUtils.pass("Try deleting any shift by clicking over the desired schedule");
//	        SimpleUtils.pass("assert on click a red cross icon should appear and it should be clickable ");
//
//	    }
//
//	    @Automated(automated = "Manual")
//	    @Owner(owner = "Gunjan")
//	    @Enterprise(name = "Coffee2_Enterprise")
//	    @TestName(description = "LEG-5111:Projected sales and Staffing Guidance data are showing as 0 on generated schedule page in LegionCoffee2")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void projectedSalesAndStaffingGuidanceAreZeroOnGenerateSchedulePage(String username, String password, String browser, String location)
//	           throws Exception
//	    {
//	        SimpleUtils.pass("Login to LegionCoffee2 Successfully");
//	        SimpleUtils.pass("Navigate to Schedule>Schedule (Mountain view location) and look for the week with schedule not generated");
//	        SimpleUtils.pass("Open week Oct15-Oct21");
//	        SimpleUtils.pass("assert for Projected sales and Staffing Guidance data should be non-0 once the schedule is generated ");
//
//	    }
//
//
//	    @Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//	    @Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-5148:Budgeted Hrs and  Guidance Hrs  are different for the week ( Oct 07 - Oct 13) in LegionTech env")
//		@Test(dataProvider = "legionTeamCredentialsByEnterpriseP", dataProviderClass=CredentialDataProviderSource.class)
//		public void budgetAndGuidanceHourNotEqual(String username, String password, String browser, String location)
//		          throws Exception
//		{
//		       SimpleUtils.pass("Login to environment Successfully");
//		       SimpleUtils.pass("Navigate to schedule page");
//		       SimpleUtils.pass("click on (Oct 07 - Oct 13) week present in Overview Page ");
//		       SimpleUtils.pass("In Week view click on Analyze button");
//		       SimpleUtils.pass("Guidance Hrs should be equals to Budgeted Hrs");
//
//		}
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-5147:On click edit shifts under Compliance Review filter disappears")
//		@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//		public void complianceReviewShiftsDisappear(String username, String password, String browser, String location)
//		          throws Exception
//		{
//		       SimpleUtils.pass("Login to environment Successfully");
//		       SimpleUtils.pass("Navigate to schedule page");
//		       SimpleUtils.pass("Open any week schedule");
//		       SimpleUtils.pass("Select Compliance Review in All Shift Type filters");
//		       SimpleUtils.pass("Click Edit button");
//		       SimpleUtils.fail("assert on click edit button shifts should not disappear",false);
//		}
//
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-5183:Not able to select a member for Assign Team Member shift in LegionCoffee envirnment")
//		@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//		public void shouldBeAddShiftUsingAssignTeamMember(String username, String password, String browser, String location)
//		          throws Exception
//		{
//		       SimpleUtils.pass("Login to environment Successfully");
//		       SimpleUtils.pass("Navigate to schedule page");
//		       SimpleUtils.pass("Open any future week schedule");
//		       SimpleUtils.pass("Click Edit button");
//		       SimpleUtils.pass("Try adding a shift in day view");
//		       SimpleUtils.pass("Try adding a shift in day view");
//		       SimpleUtils.pass("Select Assign Team Member and try selecting a member from Search TM pop-up");
//		       SimpleUtils.pass("Able to select a member for Assign Team Member shift");
//		}
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-5195: Schedule shifts are not aligned for Nov-12 when we select environment as LegionCoffee and location as Carmel Club")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void scheduleShiftsNotAligned(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login into LegionCoffee application successfully");
//			SimpleUtils.pass("Change location to Carmel Club");
//			SimpleUtils.pass("Click on Schedule button");
//			SimpleUtils.pass("Click on Schedule and navigate till Nov -12 to Nov-18 week");
//			SimpleUtils.pass("Click on day view and select Nov -12");
//			SimpleUtils.pass("Click on Next week arrow");
//			SimpleUtils.pass("Click on previous week arrow and select Nov-12");
//			SimpleUtils.pass("assert schedule shifts should be aligned");
//	    }
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee_Enterprise")
//		@TestName(description = "LEG-5197: Schedules Hours in Schedule tab are not displaying for each locations if user selects any locations from All locations filter")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void scheduledHrsNotChangingOnAllLocationFilter(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login into LegionCoffee application successfully");
//			SimpleUtils.pass("Select location as Bay area");
//			SimpleUtils.pass("Click on Schedule button");
//			SimpleUtils.pass("Click on Schedule Sub tab");
//			SimpleUtils.pass("Select Carmal Club from All locations filter");
//			SimpleUtils.pass("Click on Day view and select day as current date");
//			SimpleUtils.pass("assert Schedule hours should display for each locations");
//	    }
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee_Enterprise")
//		@TestName(description = "LEG-5198: Not able to edit Budget as Budget popup is blank in LegionCoffee and LegionCoffee2 Environment")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void shouldBeAbleToEditOnStaffingGuidance(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login into LegionCoffee application successfully");
//			SimpleUtils.pass("Select location as Carmel Club");
//			SimpleUtils.pass("Click on Schedule button");
//			SimpleUtils.pass("Click on Schedule Sub tab");
//			SimpleUtils.pass("Select toggle summary view");
//			SimpleUtils.pass("Open a Guidance week from Schedule Overview");
//			SimpleUtils.fail("assert Budget popup should not be blank",false);
//	    }
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee_Enterprise")
//		@TestName(description = "LEG-5232: Data for Schedule does not get loaded when user clicks on next day without waiting data for highlighted day gets loaded")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void shouldBeAbleToLoadScheduleDataOnDayView(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login into application successfully");
//			SimpleUtils.pass("Click on Schedule button");
//			SimpleUtils.pass("Click on Schedule Sub tab");
//			SimpleUtils.pass("Click on Day view");
//			SimpleUtils.pass("Click on Next week arrow");
//			SimpleUtils.fail("assert Click on day which is not highlighted and make sure Highlighted day does not get loaded before user clicks on other day.",false);
//	    }
//
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee_Enterprise")
//		@TestName(description = "LEG-5232: Data for Schedule does not get loaded when user clicks on next day without waiting data for highlighted day gets loaded")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void groupByLocationFilterShouldBeSelected(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login into https://enterprise-stage.legion.work/legion/?enterprise=Coffee#/");
//			SimpleUtils.pass("Select location as Bay Area");
//			SimpleUtils.pass("Navigate to Schedule page");
//			SimpleUtils.pass("assert GroupByLocation should be selected by Default on Schedule Page");
//	    }
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee_Enterprise")
//		@TestName(description = "LEG-5230: Group By selection filter is blank on navigating back from different tabs")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void groupByAllShouldNotBeBlank(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login into application successfully");
//			SimpleUtils.pass("Navigate to Schedule page");
//			SimpleUtils.pass("Select “Group By WorkRole” in the filter");
//			SimpleUtils.pass("Navigate to overview tab and then navigate back to schedule tab");
//			SimpleUtils.pass("assert Group By Workrole filter should be Sticky and should not be blank");
//	    }
//
//
//		@Automated(automated = "Manual")
//		@Owner(owner = "Gunjan")
//		@Enterprise(name = "Coffee2_Enterprise")
//		@TestName(description = "LEG-5333: Other Hrs is displaying as Zero in Schedule tab whereas Other hrs is displaying as 10.5 Hrs in Dashboard")
//	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	    public void shouldBeNonZeroOtherHours(String username, String password, String browser, String location)
//	            throws Exception
//	    {
//			SimpleUtils.pass("Login into application successfully");
//			SimpleUtils.pass("Change location to Legion Coffee Moch Store");
//			SimpleUtils.pass("Verify the value of Other Hrs in dashboard Page");
//			SimpleUtils.pass("Navigate to Schedule page");
//			SimpleUtils.pass("assert alue of Other Hrs in Schedule tab should be same as Dashboard page");
//	    }
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Nora")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Prepare the data for swap")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
//	public void prepareTheSwapShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//	  	try {
//	  		swapCoverNames = new ArrayList<>();
//			swapCoverCredentials = getSwapCoverUserCredentials(location);
//			for (Map.Entry<String, Object[][]> entry : swapCoverCredentials.entrySet()) {
//				swapCoverNames.add(entry.getKey());
//			}
//			workRoleName = String.valueOf(swapCoverCredentials.get(swapCoverNames.get(0))[0][3]);
//
//			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
//					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
//					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//
//			scheduleCommonPage.navigateToNextWeek();
//			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
//			if (isWeekGenerated) {
//				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//			}
//			createSchedulePage.createScheduleForNonDGFlowNewUI();
//			// Deleting the existing shifts for swap team members
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(swapCoverNames.get(0));
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(swapCoverNames.get(1));
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
//			scheduleMainPage.saveSchedule();
//			// Add the new shifts for swap team members
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			newShiftPage.addNewShiftsByNames(swapCoverNames, workRoleName);
//			scheduleMainPage.saveSchedule();
//			createSchedulePage.publishActiveSchedule();
//		} catch (Exception e){
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nora")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Verify the Team Member view Swap")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyTheTeamMemberViewSwapAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception
//	{
//		try {
//			prepareTheSwapShiftsAsInternalAdmin(browser, username, password, location);
//			LoginPage loginPage = pageFactory.createConsoleLoginPage();
//			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
//			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//			loginPage.logOut();
//
//			Object[][] credential = swapCoverCredentials.get(swapCoverNames.get(0));
//			loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//					, String.valueOf(credential[0][2]));
//			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			dashboardPage.clickOnProfileIconOnDashboard();
//			if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//				dashboardPage.clickOnSwitchToEmployeeView();
//			}
//			// Verify Shift should be shown as starting Tomorrow/Today
//			if (dashboardPage.getUpComingShifts().size() > 0) {
//				SimpleUtils.pass("Shifts are shown Successfully!");
//			} else {
//				SimpleUtils.fail("Shifts not loaded successfully!", true);
//			}
//			// Verify View my Schedule button should be present and clickable
//			dashboardPage.isViewMySchedulePresentAndClickable();
//			// Verify After click on the View My Schedule, page should navigate to My Schedule page
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			scheduleCommonPage.navigateToNextWeek();
//			// Verify On My Schedule page, Schedule shifts should be present on Schedule table
//			SimpleUtils.assertOnFail("Schedule Shifts are not present!", scheduleShiftTablePage.areShiftsPresent(), true);
//			// Verify After click on any shift from Schedule table, 2 Button should be available for : 1. Request to Swap shift 2. Request to cover shift
//			mySchedulePage.verifyClickOnAnyShift();
//			List<String> requests = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
//			SimpleUtils.assertOnFail("Requests on pop-up shows incorrectly!", mySchedulePage.verifyShiftRequestButtonOnPopup(requests), true);
//			// Verify After click on the Request to swap shift, Find Shifts to Swap page should be opened.
//			String swapRequest = "Request to Swap Shift";
//			String swapTitle = "Find Shifts to Swap";
//			mySchedulePage.clickTheShiftRequestByName(swapRequest);
//			SimpleUtils.assertOnFail(swapTitle + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(swapTitle), true);
//			// Verify On Find Shifts to Swap page should have comparable shifts that you can ask to trade
//			mySchedulePage.verifyComparableShiftsAreLoaded();
//			// Verify After requesting for Swap shift, Request should go to the another TM
//			mySchedulePage.selectOneTeamMemberToSwap();
//		} catch (Exception e){
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nora")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Verify the Team Member view Cover")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyTheTeamMemberViewCoverAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception
//	{
//		try {
//			prepareTheSwapShiftsAsInternalAdmin(browser, username, password, location);
//			LoginPage loginPage = pageFactory.createConsoleLoginPage();
//			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
//			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//			loginPage.logOut();
//
//			Object[][] credential = swapCoverCredentials.get(swapCoverNames.get(0));
//			loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//					, String.valueOf(credential[0][2]));
//			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			dashboardPage.clickOnProfileIconOnDashboard();
//			if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//				dashboardPage.clickOnSwitchToEmployeeView();
//			}
//			// Verify Shift should be shown as starting Tomorrow/Today
//			if (dashboardPage.getUpComingShifts().size() > 0) {
//				SimpleUtils.pass("Shifts are shown Successfully!");
//			} else {
//				SimpleUtils.fail("Shifts not loaded successfully!", true);
//			}
//			// Verify View my Schedule button should be present and clickable
//			dashboardPage.isViewMySchedulePresentAndClickable();
//			// Verify After click on the View My Schedule, page should navigate to My Schedule page
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			scheduleCommonPage.navigateToNextWeek();
//			// Verify On My Schedule page, Schedule shifts should be present on Schedule table
//			SimpleUtils.assertOnFail("Schedule Shifts are not present!", scheduleShiftTablePage.areShiftsPresent(), true);
//			// Verify After click on any shift from Schedule table, 2 Button should be available for : 1. Request to Swap shift 2. Request to cover shift
//			int index = mySchedulePage.verifyClickOnAnyShift();
//			// Verify After Click on the Request to Cover shift, Submit Cover Request should be opened.
//			String request = "Request to Cover Shift";
//			String title = "Submit Cover Request";
//			mySchedulePage.clickTheShiftRequestByName(request);
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//			// Verify Submit Cover Request should have Message Text box, Submit and cancel button
//			mySchedulePage.verifyComponentsOnSubmitCoverRequest();
//			// Verify After Click on the Submit button, confirmation pop-up should be opened.
//			mySchedulePage.verifyClickOnSubmitButton();
//			// Verify After requesting for cover request, View Cover Request status should be shown
//			mySchedulePage.clickOnShiftByIndex(index);
//			List<String> requests = new ArrayList<>(Arrays.asList("View Cover Request Status"));
//			SimpleUtils.assertOnFail("Requests on pop-up shows incorrectly!", mySchedulePage.verifyShiftRequestButtonOnPopup(requests), false);
//			// Verify After Click on the View Cover Request , Cover Request status page should be opened
//			mySchedulePage.clickTheShiftRequestByName(requests.get(0));
//			title = "Cover Request Status";
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//			// Validate the cancellation of cover request
//			mySchedulePage.verifyClickCancelSwapOrCoverRequest();
//			// Validate the Submit swap/cover request pop-up keep Showing
//			mySchedulePage.clickOnShiftByIndex(index);
//			requests = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
//			SimpleUtils.assertOnFail("Requests on pop-up shows incorrectly!", mySchedulePage.verifyShiftRequestButtonOnPopup(requests), false);
//		} catch (Exception e){
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nora")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the functionality of Team schedule option")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyTheTeamScheduleOptionAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//	  	try {
//			// Login as Internal Admin first to make sure that the schedule is published for this week, and get the whole week schedule
//			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
//			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
//					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
//					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//
//			scheduleCommonPage.navigateToNextWeek();
//			//make publish schedule activity
//			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//			if (isActiveWeekGenerated) {
//				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//			}
//			createSchedulePage.createScheduleForNonDGFlowNewUI();
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
//			newShiftPage.clickOnDayViewAddNewShiftButton();
//			newShiftPage.customizeNewShiftPage();
//			newShiftPage.selectWorkRole("Event Manager");
//			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
//			newShiftPage.clickOnCreateOrNextBtn();
//			scheduleMainPage.saveSchedule();
//			createSchedulePage.publishActiveSchedule();
//			List<String> weekSchedule = mySchedulePage.getWholeWeekSchedule();
//			LoginPage loginPage = pageFactory.createConsoleLoginPage();
//			loginPage.logOut();
//
//			// Login as Team Member
//			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
//			dashboardPage = pageFactory.createConsoleDashboardPage();
//			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			myscheduleCommonPage.goToSchedulePageAsTeamMember();
//			scheduleCommonPage.navigateToNextWeek();
//			String mySelectedWeek = mySchedulePage.getSelectedWeek();
//			// Validate the clickability of Team schedule option
//			String subTitle = "Team Schedule";
//			myscheduleCommonPage.goToScheduleSubTabByText(subTitle);
//			// Validate that Team schedule is in view mode for TM
//			mySchedulePage.verifyTeamScheduleInViewMode();
//			// Validate the availability of team schedule for whole week
//			List<String> weekScheduleTMView = mySchedulePage.getWholeWeekSchedule();
//			if (weekSchedule.containsAll(weekScheduleTMView) && weekScheduleTMView.containsAll(weekSchedule)) {
//				SimpleUtils.pass("Whole week schedule is consistent with admin view!");
//			} else {
//				SimpleUtils.fail("Whole week schedule is incorrect!", false);
//			}
//			// Validate the value of selected week in Team Schedule
//			String teamSelectedWeek = mySchedulePage.getSelectedWeek();
//			if (teamSelectedWeek.equalsIgnoreCase(mySelectedWeek)) {
//				SimpleUtils.pass("Team Schedule selected week is same as what we selected in My Schedule!");
//			} else {
//				SimpleUtils.fail("Team Schedule selected week isn't same as what we selected in My Schedule!", false);
//			}
//			// TODO: Validate the visibility of info icon - refer to meal break, skip it now
//			// Validate the number of shifts in open shift smartcard
//			String cardName = "OPEN SHIFTS";
//			SimpleUtils.assertOnFail("Open shift smart card not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
//			int openShiftCount = smartCardPage.getCountFromSmartCardByName(cardName);
//			String linkName = "View Shifts";
//			smartCardPage.clickLinkOnSmartCardByName(linkName);
//			int openShiftCountWeekView = scheduleShiftTablePage.getShiftsCount();
//			if (openShiftCount == openShiftCountWeekView) {
//				SimpleUtils.pass("The number of open shift in smart card is correct!");
//			} else {
//				SimpleUtils.fail("The number of open shift in smart card is: " + openShiftCount + ", but actual count is: " + openShiftCountWeekView, false);
//			}
//			// Validate the filters results for team schedule
//			scheduleMainPage.filterScheduleByShiftTypeAsTeamMember(true);
//			// Validate the availbity of Print icon
//			SimpleUtils.assertOnFail("Print Icon not loaded Successfully!", scheduleMainPage.isPrintIconLoaded(), false);
//			// Validate the print feature
//			scheduleMainPage.verifyThePrintFunction();
//			// Validate team schedule by selecting another week
//			mySchedulePage.verifySelectOtherWeeks();
//		} catch (Exception e){
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nora")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the functionality of Swap and Cover request options")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyTheFunctionalityOfSwapAndCoverAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//	  	try {
//			prepareTheSwapShiftsAsInternalAdmin(browser, username, password, location);
//			LoginPage loginPage = pageFactory.createConsoleLoginPage();
//			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
//			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//			loginPage.logOut();
//
//			Object[][] credential = swapCoverCredentials.get(swapCoverNames.get(0));
//			loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//					, String.valueOf(credential[0][2]));
//			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//			dashboardPage.clickOnProfileIconOnDashboard();
//			if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//				dashboardPage.clickOnSwitchToEmployeeView();
//			}
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			scheduleCommonPage.navigateToNextWeek();
//			// Validate the availibility of Swap and Cover request options
//			int index = mySchedulePage.verifyClickOnAnyShift();
//			// Verify the availability of Request to cover shift option
//			String request = "Request to Cover Shift";
//			String title = "Submit Cover Request";
//			mySchedulePage.clickTheShiftRequestByName(request);
//			// Validate the clickabilityof option of Request to Cover
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//			// Validate the data of Submit Cover Request popup
//			mySchedulePage.verifyComponentsOnSubmitCoverRequest();
//			// Validate the content of Add Message text box on Submit Cover Request pop-up
//			mySchedulePage.verifyTheContentOfMessageOnSubmitCover();
//			// Validate the functionality of Cancel button of Submit Cover Request popup
//			scheduleCommonPage.clickCancelButtonOnPopupWindow();
//			// Validate the functionality of Submit button of Submit Cover Request popup
//			mySchedulePage.clickOnShiftByIndex(index);
//			mySchedulePage.clickTheShiftRequestByName(request);
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//			mySchedulePage.verifyComponentsOnSubmitCoverRequest();
//			mySchedulePage.verifyClickOnSubmitButton();
//			// Validate the unavailibity of Request to Swap and Request to Cover post submission of Cover request
//			mySchedulePage.clickOnShiftByIndex(index);
//			List<String> swapCoverRequsts = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
//			if (!mySchedulePage.verifyShiftRequestButtonOnPopup(swapCoverRequsts)) {
//				SimpleUtils.pass("Request to Swap and Request to Cover options are disappear");
//			} else {
//				SimpleUtils.fail("Request to Swap and Request to Cover options are still shown!", false);
//			}
//			// Validate the Shift after requesting for cover
//			List<String> viewCoverRequests = new ArrayList<>(Arrays.asList("View Cover Request Status"));
//			SimpleUtils.assertOnFail("Requests on pop-up shows incorrectly!", mySchedulePage.verifyShiftRequestButtonOnPopup(viewCoverRequests), true);
//			mySchedulePage.clickTheShiftRequestByName(viewCoverRequests.get(0));
//			title = "Cover Request Status";
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//			String status = "Pending";
//			mySchedulePage.verifyShiftRequestStatus(status);
//			mySchedulePage.verifyClickCancelSwapOrCoverRequest();
//
//			// For Swap Feature
//			index = mySchedulePage.verifyClickOnAnyShift();
//			// Validate the clickability of Cover and Swap request options
//			request = "Request to Swap Shift";
//			title = "Find Shifts to Swap";
//			mySchedulePage.clickTheShiftRequestByName(request);
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//			// Validate the Request to Swap feature
//			mySchedulePage.verifyComparableShiftsAreLoaded();
//			// Validate the data of the available TMs to swap the shift
//			mySchedulePage.verifyTheDataOfComparableShifts();
//			// Validate the sum of available swap shifts
//			mySchedulePage.verifyTheSumOfSwapShifts();
//			// Validate the Next button functionality in swap popup
//			mySchedulePage.verifyNextButtonIsLoadedAndDisabledByDefault();
//			// Validate the selection feature of shifts to send a swap request
//			mySchedulePage.verifySelectOneShiftNVerifyNextButtonEnabled();
//			// Validate the clickability and functionality of cancel button
//			scheduleCommonPage.clickCancelButtonOnPopupWindow();
//			// Validate the redirection of Next button to Submit Swap Request popup
//			mySchedulePage.clickOnShiftByIndex(index);
//			mySchedulePage.clickTheShiftRequestByName(request);
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//			mySchedulePage.verifyClickOnNextButtonOnSwap();
//			title = "Submit Swap Request";
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//			// Validate the availability of Back and Submit buttons in Submit Swap Request popup
//			mySchedulePage.verifyBackNSubmitBtnLoaded();
//			// Validate the redirection of Back button
//			mySchedulePage.verifyTheRedirectionOfBackButton();
//			// Validate the multiselection feature of Swap request
//			mySchedulePage.verifySelectMultipleSwapShifts();
//			// Validate the Submit button feature
//			mySchedulePage.verifyClickOnNextButtonOnSwap();
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//			mySchedulePage.verifyClickOnSubmitButton();
//			// Validate the disappearence of Request to Swap and Request to Cover option
//			mySchedulePage.clickOnShiftByIndex(index);
//			if (!mySchedulePage.verifyShiftRequestButtonOnPopup(swapCoverRequsts)) {
//				SimpleUtils.pass("Request to Swap and Request to Cover options are disappear");
//			} else {
//				SimpleUtils.fail("Request to Swap and Request to Cover options are still shown!", false);
//			}
//
//			loginPage.logOut();
//
//			credential = swapCoverCredentials.get(swapCoverNames.get(1));
//			loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//					, String.valueOf(credential[0][2]));
//			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			dashboardPage.clickOnProfileIconOnDashboard();
//			if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//				dashboardPage.clickOnSwitchToEmployeeView();
//			}
//			dashboardPage.goToTodayForNewUI();
//			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			scheduleCommonPage.navigateToNextWeek();
//			// Validate that swap request smartcard is available to recipient team member
//			String smartCard = "SWAP REQUESTS";
//			smartCardPage.isSmartCardAvailableByLabel(smartCard);
//			// Validate the availability of all swap request shifts in schedule table
//			String linkName = "View All";
//			smartCardPage.clickLinkOnSmartCardByName(linkName);
//			mySchedulePage.verifySwapRequestShiftsLoaded();
//			// Validate that recipient can claim the swap request shift.
//			mySchedulePage.verifyClickAcceptSwapButton();
//
//			loginPage.logOut();
//			credential = swapCoverCredentials.get(swapCoverNames.get(0));
//			loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//					, String.valueOf(credential[0][2]));
//			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			dashboardPage.clickOnProfileIconOnDashboard();
//			if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//				dashboardPage.clickOnSwitchToEmployeeView();
//			}
//			dashboardPage.goToTodayForNewUI();
//			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			scheduleCommonPage.navigateToNextWeek();
//
//			mySchedulePage.clickOnShiftByIndex(index);
//			List<String> viewSwapRequest = new ArrayList<>(Arrays.asList("View Swap Request Status"));
//			if (!mySchedulePage.verifyShiftRequestButtonOnPopup(viewSwapRequest)) {
//				mySchedulePage.clickOnShiftByIndex(index);
//				// Create the Swap request again, so that it can be cancelled
//				createTheSwapRequest(index);
//				mySchedulePage.clickOnShiftByIndex(index);
//			}
//			mySchedulePage.clickTheShiftRequestByName(viewSwapRequest.get(0));
//			title = "Swap Request Status";
//			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//			// Validate the cancellation of Swap request
//			mySchedulePage.verifyClickCancelSwapOrCoverRequest();
//			// Validate that user can resend the swap request after cancelling it first
//			createTheSwapRequest(index);
//			// Validate the unavailibity of Request to Swap and Request to Cover forPast shifts
//			mySchedulePage.clickOnShiftByIndex(index);
//			if (!mySchedulePage.verifyShiftRequestButtonOnPopup(swapCoverRequsts)) {
//				SimpleUtils.pass("Request to Swap and Request to Cover options are disappear");
//			} else {
//				SimpleUtils.fail("Request to Swap and Request to Cover options are still shown!", false);
//			}
//		} catch (Exception e){
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}

	@Automated(automated = "Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the feature of filter")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheFeatureOfFilterAsInternalAdmin(String browser, String username, String password, String location)
			throws Exception {
	  	try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			String workRole = shiftOperatePage.getRandomWorkRole();
			// Deleting the existing shifts for swap team members
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
			newShiftPage.addOpenShiftWithLastDay(workRole);
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			// Login as Team Member
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());

			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			profileNewUIPage.clickOnUserProfileImage();
			if (dashboardPage.isSwitchToEmployeeViewPresent()) {
				dashboardPage.clickOnSwitchToEmployeeView();
			}

			mySchedulePage.goToSchedulePageAsTeamMember();
			scheduleCommonPage.navigateToNextWeek();
			String subTitle = "Team Schedule";
			mySchedulePage.gotoScheduleSubTabByText(subTitle);
			// Validate the feature of filter
			mySchedulePage.verifyScheduledNOpenFilterLoaded();
			// Validate the filter - Schedule and Open
			scheduleMainPage.checkAndUnCheckTheFilters();
			// Validate the filter results by applying scheduled filter
			// Validate the filter results by applying Open filter
			scheduleMainPage.filterScheduleByShiftTypeAsTeamMember(true);
			// Validate the filter results by applying both filters and none of them
			scheduleMainPage.filterScheduleByBothAndNone();
			// Validate the filter value by moving to other weeks
			String selectedFilter = scheduleMainPage.selectOneFilter();
			scheduleMainPage.verifySelectedFilterPersistsWhenSelectingOtherWeeks(selectedFilter);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nora")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Verify the availibility and functionality of claiming open shift popup")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyTheFunctionalityOfClaimOpenShiftAsTeamLead(String browser, String username, String password, String location)
//			throws Exception {
//	  	try {
//			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
//			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//			String tmName = profileNewUIPage.getNickNameFromProfile();
//			LoginPage loginPage = pageFactory.createConsoleLoginPage();
//			loginPage.logOut();
//
//			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
//			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//			// Checking configuration in controls
//			String option = "Always";
//			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//			controlsNewUIPage.clickOnControlsConsoleMenu();
//			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
//			boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
//			SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
//			controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
//
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);
//			scheduleCommonPage.navigateToNextWeek();
//
//			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//			if (isActiveWeekGenerated) {
//				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//			}
//			createSchedulePage.createScheduleForNonDGFlowNewUI();
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmName);
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
//			String workRole = shiftOperatePage.getRandomWorkRole();
//			scheduleMainPage.saveSchedule();
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			newShiftPage.clickOnDayViewAddNewShiftButton();
//			newShiftPage.customizeNewShiftPage();
//			newShiftPage.selectWorkRole(workRole);
//			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
//			newShiftPage.clickOnCreateOrNextBtn();
//			newShiftPage.searchTeamMemberByName(tmName);
//			newShiftPage.clickOnOfferOrAssignBtn();
//			scheduleMainPage.saveSchedule();
//			createSchedulePage.publishActiveSchedule();
//
//			loginPage.logOut();
//
//			loginToLegionAndVerifyIsLoginDone(username, password, location);
//			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			scheduleCommonPage.navigateToNextWeek();
//
//			// Validate the clickability of claim open text in popup
//			String cardName = "WANT MORE HOURS?";
//			SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
//			String linkName = "View Shifts";
//			smartCardPage.clickLinkOnSmartCardByName(linkName);
//			SimpleUtils.assertOnFail("Open shifts not loaed Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
//			List<String> shiftHours = mySchedulePage.getShiftHoursFromInfoLayout();
//			List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
//			int index = mySchedulePage.selectOneShiftIsClaimShift(claimShift);
//			String weekDay = mySchedulePage.getSpecificShiftWeekDay(index);
//			// Validate the availability of Claim Shift Request popup
//			mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
//			// Validate the availability of Decline and Accept buttons in popup
//			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
//			// Validate the date and time of Claim Shift Request popup
//			mySchedulePage.verifyTheShiftHourOnPopupWithScheduleTable(shiftHours.get(index), weekDay);
//			// Validate the clickability of Cancel button
//			// schedulePage.verifyClickCancelBtnOnClaimShiftOffer();
//			// Validate the clickability of I Agree button
//			// mySchedulePage.clickOnShiftByIndex(index);
//			// mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
//			mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();
//			// Validate the status of Claim request
//			mySchedulePage.clickOnShiftByIndex(index);
//			List<String> claimStatus = new ArrayList<>(Arrays.asList("Claim Shift Approval Pending", "Cancel Claim Request"));
//			mySchedulePage.verifyShiftRequestButtonOnPopup(claimStatus);
//			// Validate the availability of Cancel Claim Request option.
//			mySchedulePage.verifyTheColorOfCancelClaimRequest(claimStatus.get(1));
//			// Validate that Cancel claim request is clickable and popup is displaying by clicking on it to reconfirm the cancellation
//			mySchedulePage.clickTheShiftRequestByName(claimStatus.get(1));
//			mySchedulePage.verifyReConfirmDialogPopup();
//			// Validate that Claim request remains in Pending state after clicking on No button
//			mySchedulePage.verifyClickNoButton();
//			mySchedulePage.clickOnShiftByIndex(index);
//			mySchedulePage.verifyShiftRequestButtonOnPopup(claimStatus);
//			// Validate the Cancellation of Claim request by clicking  on Yes
//			mySchedulePage.clickTheShiftRequestByName(claimStatus.get(1));
//			mySchedulePage.verifyReConfirmDialogPopup();
//			mySchedulePage.verifyClickOnYesButton();
//			mySchedulePage.clickOnShiftByIndex(index);
//			mySchedulePage.verifyShiftRequestButtonOnPopup(claimShift);
//			// Validate the functionality of clear filter in Open shift smart card
//			mySchedulePage.verifyTheFunctionalityOfClearFilter();
//		} catch (Exception e){
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}
//
//	public void createTheSwapRequest(int index) throws Exception {
//
//		MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
//		mySchedulePage.clickOnShiftByIndex(index);
//		String request = "Request to Swap Shift";
//		String title = "Find Shifts to Swap";
//		mySchedulePage.clickTheShiftRequestByName(request);
//		SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//		mySchedulePage.verifySelectOneShiftNVerifyNextButtonEnabled();
//		mySchedulePage.verifyClickOnNextButtonOnSwap();
//		title = "Submit Swap Request";
//		SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//		mySchedulePage.verifyClickOnSubmitButton();
//	}
}
