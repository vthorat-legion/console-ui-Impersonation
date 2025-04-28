package com.legion.tests.core;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import com.legion.api.abSwitch.ABSwitchAPI;
import com.legion.api.abSwitch.AbSwitches;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.pages.core.schedule.ConsoleEditShiftPage;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.utils.JsonUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.SwitchToWindow;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;

import static com.legion.utils.MyThreadLocal.*;


public class ScheduleTestKendraScott2 extends TestBase {

	private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
	private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
	private static HashMap<String, String> schedulePolicyData = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SchedulingPoliciesData.json");
	private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
	private static HashMap<String, Object[][]> kendraScott2TeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("KendraScott2TeamMembers.json");
	private static HashMap<String, Object[][]> cinemarkWkdyTeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("CinemarkWkdyTeamMembers.json");
	private static String opWorkRole = scheduleWorkRoles.get("RETAIL_ASSOCIATE");
	private static String controlWorkRole = scheduleWorkRoles.get("RETAIL_RENTAL_MGMT");

	public enum weekCount {
		Zero(0),
		One(1),
		Two(2),
		Three(3),
		Four(4),
		Five(5);
		private final int value;

		weekCount(final int newValue) {
			value = newValue;
		}

		public int getValue() {
			return value;
		}
	}

	public enum filtersIndex {
		Zero(0),
		One(1),
		Two(2),
		Three(3),
		Four(4),
		Five(5);
		private final int value;

		filtersIndex(final int newValue) {
			value = newValue;
		}

		public int getValue() {
			return value;
		}
	}


	public enum dayCount{
		Seven(7);
		private final int value;
		dayCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
	}

	public enum schedulePlanningWindow{
		Eight(8);
		private final int value;
		schedulePlanningWindow(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
	}

	public enum sliderShiftCount {
		SliderShiftStartCount(2),
		SliderShiftEndTimeCount(10),
		SliderShiftEndTimeCount2(14),
		SliderShiftEndTimeCount3(40);
		private final int value;
		sliderShiftCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
	}

	public enum staffingOption{
		OpenShift("Auto"),
		ManualShift("Manual"),
		AssignTeamMemberShift("Assign Team Member");
		private final String value;
		staffingOption(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
	}

	  public enum overviewWeeksStatus{
		  NotAvailable("Not Available"),
		  Draft("Draft"),
		  Guidance("Guidance"),
		  Published("Published"),
		  Finalized("Finalized");

		  private final String value;
		  overviewWeeksStatus(final String newValue) {
            value = newValue;
          }
          public String getValue() { return value; }
		}


	public enum SchedulePageSubTabText {
		Overview("OVERVIEW"),
		Forecast("FORECAST"),
		ProjectedSales("PROJECTED SALES"),
		StaffingGuidance("STAFFING GUIDANCE"),
		Schedule("SCHEDULE"),
		MySchedule("MY SCHEDULE"),
		TeamSchedule("TEAM SCHEDULE"),
		ProjectedTraffic("PROJECTED TRAFFIC");
		private final String value;

		SchedulePageSubTabText(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	public enum weekViewType {
		Next("Next"),
		Previous("Previous");
		private final String value;

		weekViewType(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	  public enum shiftSliderDroppable{
		  StartPoint("Start"),
		  EndPoint("End");
			private final String value;
			shiftSliderDroppable(final String newValue) {
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

	  public enum scheduleGroupByFilterOptions{
		  groupbyAll("Group by All"),
		  groupbyWorkRole("Group by Work Role"),
		  groupbyTM("Group by TM"),
		  groupbyJobTitle("Group by Job Title"),
		  groupbyLocation("Group by Location"),
		  groupbyDayParts("Group by Day Parts");
			private final String value;
			scheduleGroupByFilterOptions(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
		}

	public enum dayWeekOrPayPeriodViewType{
		  Next("Next"),
		  Previous("Previous");
			private final String value;
			dayWeekOrPayPeriodViewType(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
	}

	public enum schedulingPoliciesShiftIntervalTime{
		  FifteenMinutes("15 minutes"),
		  ThirtyMinutes("30 minutes");
			private final String value;
			schedulingPoliciesShiftIntervalTime(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
	}

	public enum usersAndRolesSubTabs{
		AllUsers("Users"),
		AccessByJobTitles("Access Roles"),
		Badges("Badges");
		private final String value;
		usersAndRolesSubTabs(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
	}

	public enum tasksAndWorkRolesSubTab{
		WorkRoles("Work Roles"),
		LaborCalculator("Labor Calculator");
		private final String value;
		tasksAndWorkRolesSubTab(final String newValue) {
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

	public enum schedulingPolicyGroupsTabs{
		  FullTimeSalariedExempt("Full Time Salaried Exempt"),
		  FullTimeSalariedNonExempt("Full Time Salaried Non Exempt"),
		  FullTimeHourlyNonExempt("Full Time Hourly Non Exempt"),
          PartTimeHourlyNonExempt("Part Time Hourly Non Exempt");
			private final String value;
			schedulingPolicyGroupsTabs(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
	}

    public enum DayOfWeek {
        Mon,
        Tue,
        Wed,
        Thu,
        Fri,
        Sat,
        Sun;
    }

	@Override
	@BeforeMethod()
	public void firstTest(Method testMethod, Object[] params) {
		try {
			this.createDriver((String) params[0], "69", "Window");
			visitPage(testMethod);
			loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Estelle")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Schedule functionality > Legion")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduleLegionFunctionality(String username, String password, String browser, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		dashboardPage.goToTodayForNewUI();
		scheduleCommonPage.navigateToNextWeek();
		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
		if(!isActiveWeekGenerated){
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		scheduleMainPage.legionButtonIsClickableAndHasNoEditButton();
		scheduleMainPage.legionIsDisplayingTheSchedul();
	}


	@Automated(automated = "Auto")
	@Owner(owner = "Estelle")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Schedule functionality > Schedule")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduleFunctionalitySchedule(String username, String password, String browser, String location)
			throws Exception {
		boolean isWeekView = false;
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		AnalyzePage analyzePage = pageFactory.createAnalyzePage();
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		scheduleCommonPage.goToScheduleNewUI();
		//Current week is getting open by default
		scheduleCommonPage.currentWeekIsGettingOpenByDefault(location);
		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
		if(!isActiveWeekGenerated){
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//Weather week smartcard is displayed correctly-Current week[Sun-Sat] Next week will show which days are past day for current week: Eg: Sun for current week
		smartCardPage.weatherWeekSmartCardIsDisplayedForAWeek();
		//If selecting one week then data is getting updating on Schedule page according to corresponding week
		scheduleShiftTablePage.scheduleUpdateAccordingToSelectWeek();
		//Print button is clickable
		String handle = getDriver().getWindowHandle();
		scheduleMainPage.printButtonIsClickable();
		getDriver().switchTo().window(handle);

		//In week View > Clicking on Print button  it should give option to print in both Landscape or Portrait mode Both should work.
		//schedulePage.landscapePortraitModeShowWellInWeekView();
		//In Week view should be able to change the mode between Landscape and Portrait
		//schedulePage.landscapeModeWorkWellInWeekView();
		//schedulePage.portraitModeWorkWellInWeekView();
		//Day-week picker section navigating correctly
		//Todo:Run failed by LEG-10221
		scheduleCommonPage.dayWeekPickerSectionNavigatingCorrectly();
		//In Day view  Clicking on Print button it should give option to print in Landscape mode only
//		schedulePage.landscapeModeOnlyInDayView();
		scheduleCommonPage.clickOnWeekView();
		//Filter is working correctly if we select any one or more filters then schedule table data is updating according to that
		//Todo:Run failed by  LEG-10210
		scheduleMainPage.filterScheduleByWorkRoleAndShiftType(isWeekView);
		//to do button is clickable
		//schedulePage.todoButtonIsClickable();
		//Todo:Run failed by Swap Cover Requested function error
		//schedulePage.verifyShiftSwapCoverRequestedIsDisplayInTo();
		//Analyze button is clickable:Staffing Guidance Schedule History-Scrollbar is working correctly version x details and close button is working
		analyzePage.verifyAnalyzeBtnFunctionAndScheduleHistoryScroll();


	}

		@Automated(automated = "Automated")
		@Owner(owner = "Estelle")
		@Enterprise(name = "KendraScott2_Enterprise")
		@TestName(description = "Verify the Schedule functionality > Compliance smartcard")
		@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
		public void verifyComplianceSmartCardFunctionalityAsInternalAdmin(String username, String password, String browser, String location)
				throws Exception {
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.goToScheduleNewUI();
			if(smartCardPage.verifyComplianceShiftsSmartCardShowing() && smartCardPage.verifyRedFlagIsVisible()){
				smartCardPage.verifyComplianceFilterIsSelectedAftClickingViewShift();
				smartCardPage.verifyComplianceShiftsShowingInGrid();
				smartCardPage.verifyClearFilterFunction();
			}else
				SimpleUtils.report("There is no compliance and no red flag");
		}


        @Automated(automated = "Automated")
		@Owner(owner = "Estelle")
		@Enterprise(name = "Coffee_Enterprise")
		@TestName(description = "Verify the Schedule functionality > Schedule smartcard")
		@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
		public void verifyScheduleFunctionalityScheduleSmartCard(String username, String password, String browser, String location)
				throws Exception {
			ArrayList<LinkedHashMap<String, Float>> scheduleOverviewAllWeekHours = new ArrayList<LinkedHashMap<String, Float>>();
			HashMap<String, Float> scheduleSmartCardHoursWages = new HashMap<>();
			HashMap<String, Float> overviewData = new HashMap<>();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.goToScheduleNewUI();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(!isActiveWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleSmartCardHoursWages = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
			SimpleUtils.report("scheduleSmartCardHoursWages :"+scheduleSmartCardHoursWages);
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
			scheduleOverviewPage.clickOverviewTab();
			List<WebElement> scheduleOverViewWeeks =  scheduleOverviewPage.getOverviewScheduleWeeks();
			overviewData = scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(0));
			SimpleUtils.report("overview data :"+scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(0)));
			if (Math.abs(overviewData.get("guidanceHours") - (scheduleSmartCardHoursWages.get("budgetedHours"))) <= 0.05
					&& Math.abs(overviewData.get("scheduledHours") - (scheduleSmartCardHoursWages.get("scheduledHours"))) <= 0.05
					&& Math.abs(overviewData.get("otherHours") - (scheduleSmartCardHoursWages.get("otherHours"))) <= 0.05) {
				SimpleUtils.pass("Schedule/Budgeted smartcard-is showing the values in Hours and wages, it is displaying the same data as overview page have for the current week .");
			}else {
				SimpleUtils.fail("Scheduled Hours and Overview Schedule Hours not same, hours on smart card for budget, scheduled and other are: " +
						scheduleSmartCardHoursWages.get("budgetedHours") + ", " + scheduleSmartCardHoursWages.get("scheduledHours") + ", " + scheduleSmartCardHoursWages.get("otherHours")
						+ ". But hours on Overview page are: " + overviewData.get("guidanceHours") + ", " + overviewData.get("scheduledHours") + ", " + overviewData.get("otherHours"),false);
			}
		}

	
	@Automated(automated = "Automated")
	@Owner(owner = "Mary/Estelle")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify the Schedule functionality - Week View - Context Menu")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduleFunctionalityWeekViewAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		scheduleCommonPage.navigateToNextWeek();

		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
		if(isActiveWeekGenerated){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		Thread.sleep(5000);
		createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("05:00AM", "11:00PM");
		//In week view, Group by All filter have 4 filters:1.Group by all  2. Group by work role  3. Group by TM 4.Group by job title
		scheduleMainPage.validateGroupBySelectorSchedulePage(false);
		//Selecting any of them, check the schedule table
		scheduleMainPage.validateScheduleTableWhenSelectAnyOfGroupByOptions(false);

		//Edit button should be clickable
		//While click on edit button,if Schedule is finalized then prompt is available and Prompt is in proper alignment and correct msg info.
		//Edit anyway and cancel button is clickable
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

		//click on the context of any TM, 1. View profile 2. Change shift role  3.Assign TM 4.  Convert to open shift is enabled for current and future week day 5.Edit meal break time 6. Delete shift
		scheduleCommonPage.navigateToNextWeek();
		isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
		if(isActiveWeekGenerated){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		Thread.sleep(5000);
		createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("05:00AM", "11:00PM");
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("assigned");
		String workRole = shiftOperatePage.getRandomWorkRole();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();

		createSchedulePage.publishActiveSchedule();
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		SimpleUtils.assertOnFail(" context of any TM display doesn't show well" , shiftOperatePage.verifyContextOfTMDisplay(), false);

		//"After Click on view profile,then particular TM profile is displayed :1. Personal details 2. Work Preferences 3. Availability
		shiftOperatePage.clickOnViewProfile();
		shiftOperatePage.verifyPersonalDetailsDisplayed();
		shiftOperatePage.verifyWorkPreferenceDisplayed();
		shiftOperatePage.verifyAvailabilityDisplayed();
		shiftOperatePage.closeViewProfileContainer();

		//After Click on Assign TM-Select TMs window is opened,Recommended and search TM tab is enabled
		shiftOperatePage.clickOnProfileIcon();
		shiftOperatePage.clickonAssignTM();
		shiftOperatePage.verifyRecommendedAndSearchTMEnabled();

		//Search and select any TM,Click on the assign: new Tm is updated on the schedule table
		//Select new TM from Search Team Member tab
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectWorkRoleFilterByText(workRole, true);
		WebElement selectedShift = null;
		selectedShift = shiftOperatePage.clickOnProfileIcon();
		String selectedShiftId= selectedShift.getAttribute("id").toString();
		shiftOperatePage.clickonAssignTM();
		String firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
		newShiftPage.clickOnOfferOrAssignBtn();
		SimpleUtils.assertOnFail(" New selected TM doesn't display in scheduled table" ,
				firstNameOfSelectedTM.equals(scheduleShiftTablePage.getShiftById(selectedShiftId).findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText().split(" ")[0].trim()), false);
		//Select new TM from Recommended TMs tab
		String firstNameOfSelectedTM2 = "";
		String selectedShiftId2 = "";
		int i = 0;
		while (firstNameOfSelectedTM2.equals("") && i<10) {
			selectedShift = shiftOperatePage.clickOnProfileIcon();
			selectedShiftId2  = selectedShift.getAttribute("id").toString();
			shiftOperatePage.clickonAssignTM();
			Thread.sleep(2000);
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			firstNameOfSelectedTM2 = newShiftPage.selectTeamMembers().split(" ")[0];
			i++;
		}
		if (firstNameOfSelectedTM2.equals("")) {
			SimpleUtils.fail("Cannot found TMs in recommended TMs tab! ", false);
		}
		newShiftPage.clickOnOfferOrAssignBtn();
		Thread.sleep(5000);
		SimpleUtils.assertOnFail(" New selected TM doesn't display in scheduled table" ,
				firstNameOfSelectedTM2.equals(scheduleShiftTablePage.getShiftById(selectedShiftId2).findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText().split(" ")[0].trim()), false);
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleMainPage.clickOnFilterBtn();

		//Click on the Convert to open shift, checkbox is available to offer the shift to any specific TM[optional] Cancel /yes
		//if checkbox is unselected then, shift is convert to open
		selectedShift = shiftOperatePage.clickOnProfileIcon();
		String tmFirstName = selectedShift.findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText().split(" ")[0];
		shiftOperatePage.clickOnConvertToOpenShift();
		if (shiftOperatePage.verifyConvertToOpenPopUpDisplay(tmFirstName)) {
			shiftOperatePage.convertToOpenShiftDirectly();
		}
        //if checkbox is select then select team member page will display
		selectedShift = shiftOperatePage.clickOnProfileIcon();
		tmFirstName = selectedShift.findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText().split(" ")[0];
		shiftOperatePage.clickOnConvertToOpenShift();
		if (shiftOperatePage.verifyConvertToOpenPopUpDisplay(tmFirstName)) {
			shiftOperatePage.convertToOpenShiftAndOfferToSpecificTMs();
		}

		//After click on Edit Shift Time, the Edit Shift window will display
		shiftOperatePage.clickOnProfileIcon();
		shiftOperatePage.clickOnEditShiftTime();
		shiftOperatePage.verifyEditShiftTimePopUpDisplay();
		shiftOperatePage.clickOnCancelEditShiftTimeButton();
		//Edit shift time and click update button
		shiftOperatePage.editAndVerifyShiftTime(true);
		//Edit shift time and click Cancel button
		shiftOperatePage.editAndVerifyShiftTime(false);

		//Verify Edit/View Meal Break
		if (shiftOperatePage.isEditMealBreakEnabled()){
			//After click on Edit Meal Break Time, the Edit Meal Break window will display
			shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(true);
			//Verify Delete Meal Break
			shiftOperatePage.verifyDeleteMealBreakFunctionality();
			//Edit meal break time and click update button
			shiftOperatePage.verifyEditMealBreakTimeFunctionality(true);
			//Edit meal break time and click cancel button
			shiftOperatePage.verifyEditMealBreakTimeFunctionality(false);
		} else
			shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(false);

		//verify cancel button
		shiftOperatePage.verifyDeleteShiftCancelButton();

		//verify delete shift
		shiftOperatePage.verifyDeleteShift();

		//"After Click on the Change shift role, one prompt is enabled:various work role any one of them can be selected"
		shiftOperatePage.clickOnProfileIcon();
		shiftOperatePage.clickOnChangeRole();
		shiftOperatePage.verifyChangeRoleFunctionality();
		//check the work role by click Apply button
		shiftOperatePage.changeWorkRoleInPrompt(true);
		//check the work role by click Cancel button
		shiftOperatePage.changeWorkRoleInPrompt(false);
	}

//	@Automated(automated = "Automated")
//	@Owner(owner = "Estelle")
//	@Enterprise(name = "Vailqacn_Enterprise")
//	@TestName(description = "Verify the Schedule functionality > Day View")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void verifyScheduleFunctionalityDayViewAsInternalAdmin(String username, String password, String browser, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		// Select one team member to view profile
//		TeamPage teamPage = pageFactory.createConsoleTeamPage();
//		teamPage.goToTeam();
//		teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
//		String userName = teamPage.selectATeamMemberToViewProfile();
//		String firstName = userName.contains(" ") ? userName.split(" ")[0] : userName;
//
//		//Overtime hours = shift total hours  - 8h
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		//Current week and day is selected by default
//		scheduleCommonPage.currentWeekIsGettingOpenByDefault(location);
//		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//		if(isActiveWeekGenerated){
//			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//		}
//		createSchedulePage.createScheduleForNonDGFlowNewUI();
//		//click on Edit button to add new shift
//		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//		scheduleCommonPage.clickOnDayView();
//		scheduleCommonPage.navigateToNextDayIfStoreClosedForActiveDay();
//		shiftOperatePage.deleteAllShiftsInDayView();
//		scheduleMainPage.saveSchedule();
//		String workRole = shiftOperatePage.getRandomWorkRole();
//		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//		SimpleUtils.assertOnFail("Add new shift button is not loaded", !scheduleMainPage.isAddNewDayViewShiftButtonLoaded() , true);
//
//		//"while selecting Open shift:Auto,create button is enabled one open shift will created and system will offer shift automatically
//		newShiftPage.clickOnDayViewAddNewShiftButton();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.selectWorkRole(workRole);
//		//newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
///*		if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get(workRole));
//		} else if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole_BARISTA"));
//		}
//*/		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//
////		"While selecting Open shift:Manual,Next button is enabled,After Click on Next Select Tms window is enabled and after selecting N number of TMs, offer will send to them"
//		newShiftPage.clickOnDayViewAddNewShiftButton();
//		newShiftPage.customizeNewShiftPage();
//		String defaultTimeDuration = newShiftPage.getTimeDurationWhenCreateNewShift();
//		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		//newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//		String defaultTimeDurationAftDrag = newShiftPage.getTimeDurationWhenCreateNewShift();
//		if (!defaultTimeDurationAftDrag.equals(defaultTimeDuration)) {
//			SimpleUtils.pass("A shift time and duration can be changed by dragging it");
//		}else
//			SimpleUtils.report("there is no change for time duration");
///*		if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get("MOD"));
//		} else if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole_BARISTA"));
//		}*/
//		newShiftPage.selectWorkRole(workRole);
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.verifySelectTeamMembersOption();
//		newShiftPage.clickOnOfferOrAssignBtn();
//
////		While selecting Assign TM,Next button is enabled, After Click on Next, Select Tm window is enabled and only one TM can be selected, and shift will assign to him/her
//		newShiftPage.clickOnDayViewAddNewShiftButton();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		//newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
///*		if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get("MOD"));
//		} else if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole_BARISTA"));
//		}*/
//		newShiftPage.selectWorkRole(workRole);
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		newShiftPage.verifySelectTeamMembersOption();
//		newShiftPage.clickOnOfferOrAssignBtn();
//        //While click on any shift from Schedule grid , X red button is enabled to delete the shift
//		scheduleShiftTablePage.validateXButtonForEachShift();
//
//		//If a shift is more than 8 hours (defined in Controls) then Daily OT hours(Daily OT should be enabled in Controls) should show
//		//if make X hour overtime, the Daily OT will be show
//		int otFlagCount = scheduleShiftTablePage.getOTShiftCount();
//		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//		newShiftPage.clickOnDayViewAddNewShiftButton();
//		newShiftPage.customizeNewShiftPage();
//		//newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_END_TIME"), dragIncreasePoint, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		//newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
///*		if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get("MOD"));
//		} else if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
//			newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole_BARISTA"));
//		}*/
//		newShiftPage.selectWorkRole(workRole);
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		newShiftPage.verifySelectTeamMembersOption();
//		newShiftPage.clickOnOfferOrAssignBtn();
//		scheduleMainPage.saveSchedule();
//		int otFlagCountAftAddNewShift = scheduleShiftTablePage.getOTShiftCount();
//		if (otFlagCountAftAddNewShift > otFlagCount) {
//			SimpleUtils.pass("OT shit add successfully");
//		}else
//			SimpleUtils.fail("add OT new shift failed" , false);
//
//
//		//If a TM has more than 40 working hours in a week (As defined in Controls) then Week OT hours should show (Week OT should be enabled in Controls)
//		int workWeekOverTime = Integer.valueOf(schedulePolicyData.get("single_workweek_overtime"));
//		int dayCountInOneWeek = Integer.valueOf(propertyCustomizeMap.get("WORKDAY_COUNT"));
//		scheduleCommonPage.clickOnWeekView();
//		float shiftHoursInWeekForTM = scheduleShiftTablePage.getShiftHoursByTMInWeekView(firstName);
//		scheduleCommonPage.clickOnDayView();
//		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
//		if (shiftHoursInWeekForTM == 0) {
//			newShiftPage.clickOnDayViewAddNewShiftButton();
//			newShiftPage.customizeNewShiftPage();
//			newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//			//newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//			/*if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
//				newShiftPage.selectWorkRole(scheduleWorkRoles.get("MOD"));
//			} else if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
//				newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole_BARISTA"));
//			}*/
//			newShiftPage.selectWorkRole(workRole);
//			newShiftPage.selectSpecificWorkDay(dayCountInOneWeek);
//			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//			newShiftPage.clickOnCreateOrNextBtn();
//			newShiftPage.searchTeamMemberByName(firstName);
//			newShiftPage.clickOnOfferOrAssignBtn();
//			scheduleMainPage.saveSchedule();
//			scheduleCommonPage.clickOnWeekView();
//			float shiftHoursInWeekForTMAftAddNewShift = scheduleShiftTablePage.getShiftHoursByTMInWeekView(firstName);
//
//			if ((shiftHoursInWeekForTMAftAddNewShift -shiftHoursInWeekForTM)>workWeekOverTime) {
//				scheduleShiftTablePage.verifyWeeklyOverTimeAndFlag(firstName);
//			}
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
//			scheduleMainPage.saveSchedule();
//		}else{
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
//			newShiftPage.clickOnDayViewAddNewShiftButton();
//			newShiftPage.customizeNewShiftPage();
//			newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//			//newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//			/*if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
//				newShiftPage.selectWorkRole(scheduleWorkRoles.get("MOD"));
//			} else if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
//				newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole_BARISTA"));
//			}*/
//			newShiftPage.selectWorkRole(workRole);
//			newShiftPage.selectSpecificWorkDay(dayCountInOneWeek);
//			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//			newShiftPage.clickOnCreateOrNextBtn();
//			newShiftPage.searchTeamMemberByName(firstName);
//			newShiftPage.clickOnOfferOrAssignBtn();
//			scheduleMainPage.saveSchedule();
//			scheduleCommonPage.clickOnWeekView();
//			float shiftHoursInWeekForTMAftAddNewShift = scheduleShiftTablePage.getShiftHoursByTMInWeekView(firstName);
//
//			if (shiftHoursInWeekForTMAftAddNewShift > workWeekOverTime) {
//				scheduleShiftTablePage.verifyWeeklyOverTimeAndFlag(firstName);
//			}
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
//			scheduleMainPage.saveSchedule();
//		}
//	}

	@Automated(automated = "Automated")
	@Owner(owner = "Estelle")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Verify the Schedule functionality > Job Title Filter Functionality > Week View")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void viewAndFilterScheduleWithGroupByJobTitleInWeekViewAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());

		/*
		 *  Navigate to Schedule Week view
		 */
		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
		if(!isActiveWeekGenerated){
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		boolean isWeekView = true;
		scheduleCommonPage.clickOnWeekView();
		scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
		scheduleMainPage.filterScheduleByJobTitle(isWeekView);
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.filterScheduleByJobTitle(isWeekView);
		scheduleMainPage.clickOnCancelButtonOnEditMode();
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Estelle")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Verify the Schedule functionality > Job Title Filter Functionality > Day View")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void viewAndFilterScheduleWithGroupByJobTitleInDayViewAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());

		/*
		 *  Navigate to Schedule day view
		 */
		boolean isWeekView = false;
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated){
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
		scheduleMainPage.filterScheduleByJobTitle(isWeekView);
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.filterScheduleByJobTitle(isWeekView);
		scheduleMainPage.clickOnCancelButtonOnEditMode();
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Estelle")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Verify the Schedule functionality > Job Title Filter Functionality > Combination")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void viewAndFilterScheduleWithGroupByJobTitleFilterCombinationInWeekViewAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		/*
		 *  Navigate to Schedule week view
		 */
		boolean isWeekView = true;
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated){
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}

		scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
		scheduleMainPage.filterScheduleByWorkRoleAndJobTitle(isWeekView);
		scheduleMainPage.filterScheduleByShiftTypeAndJobTitle(isWeekView);
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.filterScheduleByJobTitle(isWeekView);
		scheduleMainPage.clickOnCancelButtonOnEditMode();
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verification of My Schedule Page")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verificationOfMySchedulePageAsTeamMember(String browser, String username, String password, String location) throws Exception {
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();

		//T1838603 Validate the availability of schedule table.
		ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
		String nickName = profileNewUIPage.getNickNameFromProfile();
		mySchedulePage.validateTheAvailabilityOfScheduleTable(nickName);

		//T1838604 Validate the disability of location selector on Schedule page.
		mySchedulePage.validateTheDisabilityOfLocationSelectorOnSchedulePage();

		//T1838605 Validate the availability of profile menu.
		mySchedulePage.validateTheAvailabilityOfScheduleMenu();

		//T1838606 Validate the focus of schedule.
		mySchedulePage.validateTheFocusOfSchedule();

		//T1838607 Validate the default filter is selected as Scheduled.
		mySchedulePage.validateTheDefaultFilterIsSelectedAsScheduled();

		//T1838608 Validate the focus of week.
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		dashboardPage.navigateToDashboard();
		String currentDate = dashboardPage.getCurrentDateFromDashboard();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		mySchedulePage.validateTheFocusOfWeek(currentDate);

		//T1838609 Validate the selection of previous and upcoming week.
		mySchedulePage.verifySelectOtherWeeks();
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verification of To and Fro navigation of week picker")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verificationOfToAndFroNavigationOfWeekPickerAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			String nickName = profileNewUIPage.getNickNameFromProfile();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			///Log in as admin to get the operation hours
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab("Schedule");
			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
			scheduleMainPage.saveSchedule();
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(nickName);
//			if(newShiftPage.displayAlertPopUp())
//				newShiftPage.displayAlertPopUpForRoleViolation();
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			if (!toggleSummaryPage.isSummaryViewLoaded())
				toggleSummaryPage.toggleSummaryView();
			loginPage.logOut();

			///Log in as team member again to compare the operation hours
			loginToLegionAndVerifyIsLoginDone(username, password, location);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.navigateToNextWeek();

			//T1838613 Validate that hours and date is visible of shifts.
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			mySchedulePage.validateThatHoursAndDateIsVisibleOfShifts();

			//T1838610 Validate the click ability of forward and backward button.
			scheduleCommonPage.validateForwardAndBackwardButtonClickable();

			//T1838611 Validate the data according to the selected week.
			mySchedulePage.validateTheDataAccordingToTheSelectedWeek();

			//T1838612 Validate the seven days - Sunday to Saturday is available in schedule table.
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			mySchedulePage.validateTheSevenDaysIsAvailableInScheduleTable();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Profile picture functionality")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyProfilePictureFunctionalityAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			//T1838614 Validate the clickability of Profile picture in a shift.
			mySchedulePage.validateProfilePictureInAShiftClickable();

			//T1838615 Validate the data of profile popup in a shift.
			//schedulePage.validateTheDataOfProfilePopupInAShift();
			// todo: <Here is an incident LEG-10929>
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Info icon functionality")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyInfoIconFunctionalityAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try {

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();

			//T1838616 Validate the availability of info icon.
			mySchedulePage.validateTheAvailabilityOfInfoIcon();

			//T1838617 Validate the clickability of info icon.
			mySchedulePage.validateInfoIconClickable();

			//T1838618 Validate the availability of Meal break as per the control settings.
			//schedulePage.validateMealBreakPerControlSettings();
			// todo: <Meal break is a postpone feature>
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verification of Open Shift Schedule Smart Card")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyOpenShiftScheduleSmartCardAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try {

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			//T1838619 Validate the availability of Open shift Smartcard.
			smartCardPage.validateTheAvailabilityOfOpenShiftSmartcard();

			//T1838620 Validate the clickability of View shifts in Open shift smartcard.
			mySchedulePage.validateViewShiftsClickable();

			//T1838621 Validate the number of open shifts in smartcard and schedule table.
			mySchedulePage.validateTheNumberOfOpenShifts();

			//T1838622 Verify the availability of claim open shift popup.
			mySchedulePage.verifyTheAvailabilityOfClaimOpenShiftPopup();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Verify the content of copy schedule for non dg flow")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheContentOfCopyScheduleForNonDGFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			// Get the hours and the count of the tms for each day, ex: "37.5 Hrs 5TMs"
			HashMap<String, String> hoursNTMsCountFirstWeek = scheduleShiftTablePage.getTheHoursNTheCountOfTMsForEachWeekDays();
			HashMap<String, List<String>> shiftsForEachDayFirstWeek = scheduleShiftTablePage.getTheContentOfShiftsForEachWeekDay();
			HashMap<String, String> budgetNScheduledHoursFirstWeek = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
			String cardName = "COMPLIANCE";
			boolean isComplianceCardLoadedFirstWeek = smartCardPage.isSpecificSmartCardLoaded(cardName);
			int complianceShiftCountFirstWeek = 0;
			if (isComplianceCardLoadedFirstWeek) {
				complianceShiftCountFirstWeek = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
			}
			String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
			if (firstWeekInfo.length() > 11) {
				firstWeekInfo = firstWeekInfo.trim().substring(10);
				if (firstWeekInfo.contains("-")) {
					String[] temp = firstWeekInfo.split("-");
					if (temp.length == 2 && temp[0].contains(" ") && temp[1].contains(" ")) {
						firstWeekInfo = temp[0].trim().split(" ")[0] + " " + (temp[0].trim().split(" ")[1].length() == 1 ? "0" + temp[0].trim().split(" ")[1] : temp[0].trim().split(" ")[1])
								+ " - " + temp[1].trim().split(" ")[0] + " " + (temp[1].trim().split(" ")[1].length() == 1 ? "0" + temp[1].trim().split(" ")[1] : temp[1].trim().split(" ")[1]);
					}
				}
			}

			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			List<String> weekDaysToClose = new ArrayList<>();
			createSchedulePage.createScheduleForNonDGByWeekInfo(firstWeekInfo, weekDaysToClose, null);

			HashMap<String, String> hoursNTMsCountSecondWeek = scheduleShiftTablePage.getTheHoursNTheCountOfTMsForEachWeekDays();
			HashMap<String, List<String>> shiftsForEachDaySecondWeek = scheduleShiftTablePage.getTheContentOfShiftsForEachWeekDay();
			HashMap<String, String> budgetNScheduledHoursSecondWeek = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
			boolean isComplianceCardLoadedSecondWeek = smartCardPage.isSpecificSmartCardLoaded(cardName);
			int complianceShiftCountSecondWeek = 0;
			if (isComplianceCardLoadedFirstWeek) {
				complianceShiftCountSecondWeek = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
			}

			if (hoursNTMsCountFirstWeek.equals(hoursNTMsCountSecondWeek)) {
				SimpleUtils.pass("Verified the scheduled hour and TMs of each week day are consistent with the copied schedule!");
			} else {
				SimpleUtils.fail("Verified the scheduled hour and TMs of each week day are inconsistent with the copied schedule", true);
			}
			if (SimpleUtils.compareHashMapByEntrySet(shiftsForEachDayFirstWeek, shiftsForEachDaySecondWeek)) {
				SimpleUtils.pass("Verified the shifts of each week day are consistent with the copied schedule!");
			} else {
				SimpleUtils.fail("Verified the shifts of each week day are inconsistent with the copied schedule!", true);
			}
			if (budgetNScheduledHoursFirstWeek.get("Scheduled").equals(budgetNScheduledHoursSecondWeek.get("Scheduled"))) {
				SimpleUtils.pass("The Scheduled hour is consistent with the copied scheudle: " + budgetNScheduledHoursFirstWeek.get("Scheduled"));
			} else {
				SimpleUtils.fail("The Scheduled hour is inconsistent, the first week is: " + budgetNScheduledHoursFirstWeek.get("Scheduled")
						+ ", but second week is: " + budgetNScheduledHoursSecondWeek.get("Scheduled"), true);
			}
			if ((isComplianceCardLoadedFirstWeek == isComplianceCardLoadedSecondWeek) && (complianceShiftCountFirstWeek == complianceShiftCountSecondWeek)) {
				SimpleUtils.pass("Verified Compliance is consistent with the copied schedule");
			} else {
				SimpleUtils.fail("Verified Compliance is inconsistent with the copied schedule!", true);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the content of closed week day schedule for non dg flow ")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheContentOfClosedWeekDayScheduleForNonDGFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			List<String> weekDaysToClose = new ArrayList<>(Arrays.asList("Sunday", "Tuesday"));
			createSchedulePage.createScheduleForNonDGByWeekInfo("SUGGESTED", weekDaysToClose, null);

			// Verify that the closed week day should not have any shifts
			scheduleShiftTablePage.verifyNoShiftsForSpecificWeekDay(weekDaysToClose);
			// Go to day view, check the closed week day should show "Store is Closed"
			scheduleCommonPage.clickOnDayView();
			scheduleShiftTablePage.verifyStoreIsClosedForSpecificWeekDay(weekDaysToClose);
			// Toggle Summary view, verify that the specific week days shows Closed
			toggleSummaryPage.toggleSummaryView();
			toggleSummaryPage.verifyClosedDaysInToggleSummaryView(weekDaysToClose);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the content after changing Budget Hours, and the budget and scheduled hours in smart card for non dg flow")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyContentOfBudgetHoursForNonDGFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.goToConsoleScheduleAndScheduleSubMenu();
			String weekInfo = toggleSummaryPage.getWeekInfoBeforeCreateSchedule();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			HashMap<String,String> budgetNScheduledFromGraph = createSchedulePage.verifyNGetBudgetNScheduleWhileCreateScheduleForNonDGFlowNewUI(weekInfo, location);
			String budgetFromGraph = budgetNScheduledFromGraph.get("Budget");
			String scheduledFromGraph = budgetNScheduledFromGraph.get("Scheduled");
			if (!toggleSummaryPage.isSummaryViewLoaded())
				toggleSummaryPage.toggleSummaryView();
			List<String> budgetsOnSTAFF = toggleSummaryPage.getBudgetedHoursOnSTAFF();
			String budgetOnWeeklyBudget = toggleSummaryPage.getBudgetOnWeeklyBudget();
			HashMap<String, String> budgetNScheduledHoursFromSmartCard = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
			String budgetFromSmartCard = budgetNScheduledHoursFromSmartCard.get("Budget");
			String scheduledFromSmartCard = budgetNScheduledHoursFromSmartCard.get("Scheduled");
			System.out.println("budgetOnWeeklyBudget is: "+budgetOnWeeklyBudget);
			String totalBudgetOnSTAFF = "";
			if (budgetsOnSTAFF.size() > 1) {
				totalBudgetOnSTAFF = budgetsOnSTAFF.get(budgetsOnSTAFF.size() - 1);
				System.out.println("totalBudgetOnSTAFF is: "+totalBudgetOnSTAFF);
			}
			if (budgetOnWeeklyBudget.equals(budgetFromGraph) && budgetFromSmartCard.equals(budgetOnWeeklyBudget) && totalBudgetOnSTAFF.equals(budgetFromGraph))
				SimpleUtils.pass("The budget hours is consistent with the saved value both in STAFF and smart cards (including Weekly)");
			else
				SimpleUtils.warn("The budget hours is inconsistent with the saved value both in STAFF and smart cards (including Weekly) since there are bugs https://legiontech.atlassian.net/browse/SF-1054 and (https://legiontech.atlassian.net/browse/SF-1113");
			if (scheduledFromGraph.equals(scheduledFromSmartCard))
				SimpleUtils.pass("The scheduled hours is consistent with the saved value both in STAFF and smart card");
			else
				SimpleUtils.fail("The scheduled hours is inconsistent with the saved value both in STAFF and smart card",false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the content after changing Operating Hours for non dg flow ")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyContentAfterChangingOperatingHrsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		try {
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab("Schedule");
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			if (createSchedulePage.isWeekGenerated()){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			String day = "Sunday";
			String startTime = "07:00AM";
			String endTime = "06:00PM";
			//set operating hours: Sunday -> start time: 07:00AM, end time: 06:00PM
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingParameters(day, startTime, endTime);
			scheduleShiftTablePage.verifyDayHasShifts(day);
			scheduleShiftTablePage.verifyDayHasShifts("Monday");
			scheduleShiftTablePage.verifyDayHasShifts("Tuesday");
			scheduleShiftTablePage.verifyDayHasShifts("Wednesday");
			scheduleShiftTablePage.verifyDayHasShifts("Thursday");
			scheduleShiftTablePage.verifyDayHasShifts("Friday");
			scheduleShiftTablePage.verifyDayHasShifts("Saturday");
			scheduleMainPage.goToToggleSummaryView();
			//verify the operating hours in Toggle Summary View
			toggleSummaryPage.verifyOperatingHrsInToggleSummary(day, startTime, endTime);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the content after changing Operating Hours for non dg flow - next day")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyContentAfterChangingOperatingHrsNextDayAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab("Schedule");
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			if (createSchedulePage.isWeekGenerated()){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			String day = "Sunday";
			String startTime = "11:00AM";
			String endTime = "02:00AM";
			//set operating hours: Sunday -> start time: 11:00AM, end time: 02:00AM
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingParameters(day, startTime, endTime);
			//verify the day we set a next day time has shifts.
			scheduleShiftTablePage.verifyDayHasShifts(day);
			//verify the operating hours in Toggle Summary View
			scheduleMainPage.goToToggleSummaryView();
			toggleSummaryPage.verifyOperatingHrsInToggleSummary(day, startTime, endTime);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Verify the budget hour in DM view schedule page for non dg flow")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyBudgetHourInDMViewSchedulePageForNonDGFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try{
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			String budgetHours = smartCardPage.getBudgetNScheduledHoursFromSmartCard().get("Budget");
			String guidanceHours = smartCardPage.getBudgetNScheduledHoursFromSmartCard().get("Guidance");
			float budgetHoursInSchedule = 0;
			if (budgetHours != null) {
				budgetHoursInSchedule = Float.parseFloat(budgetHours.replace(",",""));
			} else if (guidanceHours != null) {
				budgetHoursInSchedule = Float.parseFloat(guidanceHours.replace(",",""));
			} else
				SimpleUtils.fail("The budget and guidance hour fail to load! ", false);

			dashboardPage.clickOnDashboardConsoleMenu();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			locationSelectorPage.selectCurrentUpperFieldAgain("District");
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();

			ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
			BigDecimal round = new BigDecimal(scheduleDMViewPage.getBudgetedHourOfScheduleInDMViewByLocation(location));
			float budgetedHoursInDMViewSchedule = round.setScale(1,   BigDecimal.ROUND_HALF_UP).floatValue();
			budgetHoursInSchedule  = (new BigDecimal(budgetHoursInSchedule)).setScale(1 ,BigDecimal.ROUND_HALF_UP).floatValue();

			if (budgetHoursInSchedule != 0 && budgetHoursInSchedule == budgetedHoursInDMViewSchedule) {
				SimpleUtils.pass("Verified the budget hour in DM view schedule page is consistent with the value saved in create schedule page!");
			} else {
				SimpleUtils.fail("Verified the budget hour in DM view schedule page is consistent with the value saved in create schedule page! The budget hour in DM view schedule page is " +
						budgetedHoursInDMViewSchedule + ". The value saved in create schedule page is " + budgetHoursInSchedule, false);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}


	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify smart card for schedule not publish(include past weeks)")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySmartCardForScheduleNotPublishAsInternalAdmin(String browser, String username, String password, String location) throws Exception{

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab("Schedule");
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		if (createSchedulePage.isWeekGenerated()){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		createSchedulePage.createScheduleForNonDGFlowNewUI();
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		//make edits
		newShiftPage.clickOnDayViewAddNewShiftButton();
		Thread.sleep(5000);
		newShiftPage.customizeNewShiftPage();
		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		newShiftPage.selectWorkRole("");
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		scheduleMainPage.saveSchedule();
		//generate and save, should not display number of changes, we set it as 0.
		int changesNotPublished = 0;
		//Verify changes not publish smart card.
		SimpleUtils.assertOnFail("Changes not publish smart card is not loaded!",smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"),false);
		smartCardPage.verifyChangesNotPublishSmartCard(changesNotPublished);
		createSchedulePage.verifyLabelOfPublishBtn("Publish");
		String activeWeek = scheduleCommonPage.getActiveWeekText();
		scheduleCommonPage.clickOnScheduleSubTab("Overview");
		List<String> resultListInOverview = scheduleOverviewPage.getOverviewData();
		for (String s : resultListInOverview){
			String a = s.substring(1,7);
			if (activeWeek.toLowerCase().contains(a.toLowerCase())){
				if (s.contains("Unpublished Edits")){
					SimpleUtils.pass("Warning message in overview page is correct!");
				} else {
					SimpleUtils.fail("Warning message is not expected: "+ s.split(",")[4],false);
				}
			}
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify smart card for schedule not publish(include past weeks) - republish")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyNumberOnSmartCardForScheduleNotPublishAsInternalAdmin(String browser, String username, String password, String location) {
		try {

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab("Schedule");
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			if (createSchedulePage.isWeekGenerated()){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.selectShiftTypeFilterByText("Action Required");
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			//make edits and publish
			Thread.sleep(3000);
			newShiftPage.clickOnDayViewAddNewShiftButton();
			Thread.sleep(3000);
			newShiftPage.customizeNewShiftPage();
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.selectWorkRole("");
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			//make edits and save
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.selectWorkRole("");
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();

			int changesNotPublished = 1;
			//Verify changes not publish smart card.
			SimpleUtils.assertOnFail("Changes not publish smart card is not loaded!",smartCardPage.isSpecificSmartCardLoaded("UNPUBLISHED CHANGES"),false);
			smartCardPage.verifyChangesNotPublishSmartCard(changesNotPublished);
			createSchedulePage.verifyLabelOfPublishBtn("Republish");
			String activeWeek = scheduleCommonPage.getActiveWeekText();
			scheduleCommonPage.clickOnScheduleSubTab("Overview");
			List<String> resultListInOverview = scheduleOverviewPage.getOverviewData();
			for (String s : resultListInOverview){
				String a = s.substring(1,7);
				if (activeWeek.toLowerCase().contains(a.toLowerCase())){
					if (s.contains("Unpublished Edits")){
						SimpleUtils.pass("Warning message in overview page is correct!");
					} else {
						SimpleUtils.fail("Warning message is not expected: "+ s.split(",")[4],false);
					}
				}
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	// Add the new test cases for "Schedule Not Published"
	@Automated(automated = "Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "verify smart card for compliance violation")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyComplianceViolationWhenScheduleIsNotPublishedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			String cardName = "COMPLIANCE";
			int originalComplianceCount = 0;
			if (smartCardPage.isSpecificSmartCardLoaded(cardName)) {
				originalComplianceCount = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
				scheduleShiftTablePage.dragOneShiftToMakeItOverTime();
			} else {
				//if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
				shiftOperatePage.clickOnProfileIcon();
				shiftOperatePage.clickOnEditShiftTime();
				shiftOperatePage.verifyEditShiftTimePopUpDisplay();
				shiftOperatePage.editShiftTimeToTheLargest();
				shiftOperatePage.clickOnUpdateEditShiftTimeButton();
			}
			scheduleMainPage.saveSchedule();
			scheduleCommonPage.clickOnWeekView();
			int currentComplianceCount = 0;
			if (smartCardPage.isSpecificSmartCardLoaded(cardName)) {
				currentComplianceCount = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
				if (currentComplianceCount == originalComplianceCount + 1) {
					SimpleUtils.pass("Schedule Week View: Compliance Count is correct after updating a new overtime shift!");
				} else {
					SimpleUtils.fail("Schedule Week View: Compliance Count is incorrect, original is: " + originalComplianceCount + ", current is: "
							+ currentComplianceCount + ", the difference between two numbers should equal to 1!", false);
				}
			} else {
				SimpleUtils.fail("Schedule Week View: Compliance smart card failed to show!", false);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "verify smart card for compliance violation -republish")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyComplianceViolationWhenScheduleHasPublishedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			String cardName = "COMPLIANCE";
			int originalComplianceCount = 0;
			if (smartCardPage.isSpecificSmartCardLoaded(cardName)) {
				originalComplianceCount = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))) {
				scheduleShiftTablePage.dragOneShiftToMakeItOverTime();
			} else {
				//if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
				shiftOperatePage.clickOnProfileIcon();
				shiftOperatePage.clickOnEditShiftTime();
				shiftOperatePage.verifyEditShiftTimePopUpDisplay();
				shiftOperatePage.editShiftTimeToTheLargest();
				shiftOperatePage.clickOnUpdateEditShiftTimeButton();
			}
			scheduleMainPage.saveSchedule();
			scheduleCommonPage.clickOnWeekView();
			int currentComplianceCount = 0;
			if (smartCardPage.isSpecificSmartCardLoaded(cardName)) {
				currentComplianceCount = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
				if (currentComplianceCount > originalComplianceCount) {
					SimpleUtils.pass("Schedule Week View: Compliance Count is correct after updating a new overtime shift!");
				} else {
					SimpleUtils.fail("Schedule Week View: Compliance Count is incorrect, original is: " + originalComplianceCount + ", current is: "
							+ currentComplianceCount + ", the difference between two numbers should equal or larger than 1!", false);
				}
			} else {
				SimpleUtils.report("Schedule Week View: Compliance smart card failed to show! Please check if the TM is exempt!");
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}
	
	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify tooltip for delete shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTooltipForDeleteShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {

			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.goToConsoleScheduleAndScheduleSubMenu();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated)
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			String workRole = shiftOperatePage.getRandomWorkRole();
			newShiftPage.addOpenShiftWithLastDay(workRole);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.addOpenShiftWithFirstDay(workRole);
			shiftOperatePage.deleteLatestOpenShift();
			scheduleMainPage.saveSchedule();

			// Check the number on changes not publish smart card
			if (smartCardPage.getChangesOnActionRequired().contains("2 Changes"))
				SimpleUtils.pass("ACTION REQUIRED Smart Card: The number on changes not publish smart card is 2");
			else
				SimpleUtils.fail("ACTION REQUIRED Smart Card: The number on changes not publish smart card is incorrect",true);

			// Filter unpublished changes to check the shifts and tooltip
			scheduleMainPage.selectShiftTypeFilterByText("Unpublished changes");
			if (scheduleShiftTablePage.getShiftsCount() == 1)
				SimpleUtils.pass("ACTION REQUIRED Smart Card: There is only one shift as expected");
			else
				SimpleUtils.fail("ACTION REQUIRED Smart Card: There is not only one shift unexpectedly",true);
			if (smartCardPage.getTooltipOfUnpublishedDeleted().contains("1 shift deleted"))
				SimpleUtils.pass("ACTION REQUIRED Smart Card: \"1 shift deleted\" tooltip shows up on smart card");
			else
				SimpleUtils.fail("ACTION REQUIRED Smart Card: \"1 shift deleted\" tooltip doesn't show up on smart card",false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify assign TM warning: TM status is inactive")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMWarningWhenTMIsInactiveAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			// Prepare a TM who is inactive
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			controlsNewUIPage.clickOnControlsConsoleMenu();
			controlsNewUIPage.clickOnControlsUsersAndRolesSection();
			String inactiveUser = controlsNewUIPage.selectAnyActiveTM();
			String date = controlsNewUIPage.deactivateActiveTM();

			// Assign to the TM to verify the message and warning

			scheduleCommonPage.goToConsoleScheduleAndScheduleSubMenu();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.selectAShiftToAssignTM(inactiveUser);
			shiftOperatePage.verifyInactiveMessageNWarning(inactiveUser,date);

			// Restore the TM to be active
			controlsNewUIPage.clickOnControlsConsoleMenu();
			controlsNewUIPage.clickOnControlsUsersAndRolesSection();
			controlsNewUIPage.searchAndSelectTeamMemberByName(inactiveUser);
			controlsNewUIPage.activateInactiveTM();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "verify the Unpublished Edits on dashboard and overview page")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyUnpublishedEditsTextOnDashboardAndOverviewPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try{
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.addOpenShiftWithLastDay(workRole);
			scheduleMainPage.saveSchedule();

			//Verify the Unpublished Edits text on overview page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
			List<WebElement> schedulesInOverviewPage = scheduleOverviewPage.getOverviewScheduleWeeks();
			if (schedulesInOverviewPage != null && schedulesInOverviewPage.size()>0){
				WebElement warningTextOfCurrentScheduleWeek = schedulesInOverviewPage.get(0).findElement(By.cssSelector("div.text-small.ng-binding"));
				if (warningTextOfCurrentScheduleWeek != null){
					String warningText = warningTextOfCurrentScheduleWeek.getText();
					if (warningText !=null && warningText.equals("Unpublished Edits")){
						SimpleUtils.pass("Verified the Unpublished Edits on Overview page display correctly. ");
					} else{
						SimpleUtils.fail("Verified the Unpublished Edits on Overview page display incorrectly. The actual warning text is " + warningText +".", true);
					}
				}
			} else{
				SimpleUtils.fail("Overview Page: Schedule weeks not found!" , true);
			}

			//Verify the Unpublished Edits text on dashboard page
			dashboardPage.navigateToDashboard();
			dashboardPage.clickOnRefreshButton();
			List<WebElement> dashboardScheduleWeeks = dashboardPage.getDashboardScheduleWeeks();
			if (dashboardScheduleWeeks != null && dashboardScheduleWeeks.size()>0){
				WebElement warningTextOfCurrentScheduleWeek = dashboardScheduleWeeks.get(1).findElement(By.cssSelector("div.text-small.ng-binding"));
				if (warningTextOfCurrentScheduleWeek != null){
					String warningText = warningTextOfCurrentScheduleWeek.getText();
					if (warningText !=null && warningText.equals("Unpublished Edits")){
						SimpleUtils.pass("Verified the Unpublished Edits text on Dashboard page display correctly. ");
					} else{
						SimpleUtils.fail("Verified the Unpublished Edits text on Dashboard page display incorrectly. The actual warning text is " + warningText +".", false);
					}
				}
			} else{
				SimpleUtils.fail("Dashboard Page: Schedule weeks not found!" , false);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "verify Assign TM warning: TM status is on time off")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMWarningForTMIsOnTimeOffAsStoreManager(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();

			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "08:00pm");
			}
			//Get the date info of the week for create time off
			String activeWeek = scheduleCommonPage.getActiveWeekText();
			List<String> year = scheduleCommonPage.getYearsFromCalendarMonthYearText();
			String[] items = activeWeek.split(" ");
			String fromDate = year.get(0)+ " " + items[3] + " " + items[4];
			String endDate = year.get(0)+ " " + items[6] + " " + items[7];
//			//To avoid one issue -- the schedule cannot be generated when directly go to Team tab
//			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
//					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
//					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
			String nickNameFromProfile = profileNewUIPage.getNickNameFromProfile();
			String myProfileLabel = "My Profile";
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
			String myTimeOffLabel = "My Time Off";
			profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
			String timeOffReasonLabel = "JURY DUTY";
			String timeOffExplanationText = "Sample Explanation Text";
			String timeOffStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel, timeOffExplanationText, fromDate, endDate);
			if (!timeOffStatus.equalsIgnoreCase("approved")) {
				profileNewUIPage.cancelAllTimeOff();
				//Go to team page and create time off for tm
				profileNewUIPage.createTimeOffOnSpecificDays(timeOffReasonLabel, timeOffExplanationText, fromDate, 6);
				teamPage.approvePendingTimeOffRequest();
			}

			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			String workRole = shiftOperatePage.getRandomWorkRole();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("11am", shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(nickNameFromProfile);
			shiftOperatePage.verifyMessageIsExpected("time off");
			shiftOperatePage.verifyWarningModelForAssignTMOnTimeOff(nickNameFromProfile);
			scheduleMainPage.clickOnCancelButtonOnEditMode();

		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
		finally{
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			//go to cancel the time off.
			String myProfileLabel = "My Profile";
			String myTimeOffLabel = "My Time Off";
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.getNickNameFromProfile();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
			profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
			profileNewUIPage.cancelAllTimeOff();
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify assign TM warning: Assignment rule violation with conf is yes")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMWarningForTMHasRoleViolationWithConfYesAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			//Go to control to set the override assignment rule as Yes.
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			controlsNewUIPage.clickOnGlobalLocationButton();
			controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
			controlsNewUIPage.enableOverRideAssignmentRuleAsYes();


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			String workRole = shiftOperatePage.getRandomWorkRole();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName("Keanu");
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Assign TM warning: TM status is already Scheduled at Home location")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMWarningForTMIsAlreadyScheduledAsStoreManager(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			List<String> firstShiftInfo = new ArrayList<>();
			while(firstShiftInfo.size() == 0 || firstShiftInfo.get(0).equalsIgnoreCase("open")
					|| firstShiftInfo.get(0).equalsIgnoreCase("unassigned")){
				firstShiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(firstShiftInfo.get(4));
			String shiftEndTime = "";
			String shiftStartTime = "";
			if (firstShiftInfo.get(2).split("-")[1].contains(":")) {
				shiftEndTime = firstShiftInfo.get(2).split("-")[1].split(":")[0] + firstShiftInfo.get(2).split("-")[1].substring(firstShiftInfo.get(2).split("-")[1].length() - 2);
				SimpleUtils.report("Get the shift end time: " + shiftEndTime);
			}
			if (firstShiftInfo.get(2).split("-")[0].contains(":")) {
				shiftStartTime = firstShiftInfo.get(2).split("-")[0].split(":")[0] + firstShiftInfo.get(2).split("-")[0].substring(firstShiftInfo.get(2).split("-")[0].length() - 2);
				SimpleUtils.report("Get the shift start time: " + shiftStartTime);
			}
			newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkingDaysOnNewShiftPageByIndex(Integer.parseInt(firstShiftInfo.get(1)));
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.verifyScheduledWarningWhenAssigning(firstShiftInfo.get(0), firstShiftInfo.get(2));
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Assign TM warning: TM is from another store and is already scheduled at this store")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMWarningForTMIsAlreadyScheduledAtAnotherStoreAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			String nearByLocation = getCrendentialInfo("NearByLocationInfo");
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();

			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
			controlsNewUIPage.clickOnGlobalLocationButton();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.setWFS("Yes");

			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
			List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
			if (!createSchedulePage.isWeekPublished()) {
				createSchedulePage.publishActiveSchedule();
			}

			// Navigate to the near by location to create the shift for this TM from AUSTIN DOWNTOWN
			dashboardPage.navigateToDashboard();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(nearByLocation);
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			scheduleCommonPage.navigateToNextWeek();
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(shiftInfo.get(4));
			newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.selectWorkingDaysOnNewShiftPageByIndex(Integer.parseInt(shiftInfo.get(1)));
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.verifyScheduledWarningWhenAssigning(shiftInfo.get(0), shiftInfo.get(2));
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify version number in Analyze page and edits persist when navigating to Suggested and back to Manager")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyVersionNumberAndEditsAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			AnalyzePage analyzePage = pageFactory.createAnalyzePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//verify version number in analyze page

			analyzePage.clickOnAnalyzeBtn("history");
			String version0 = "0.0";
			String version1 = "0.1";
			String version2 = "1.1";
			analyzePage.verifyScheduleVersion(version0);
			analyzePage.closeAnalyzeWindow();
			//make edits and save
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.verifyVersionInSaveMessage(version1);
			//suggested tab
			scheduleMainPage.clickOnSuggestedButton();
			SimpleUtils.assertOnFail("Changes not publish smart card is loaded in suggested page!",!smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"),false);
			scheduleMainPage.clickOnManagerButton();
			SimpleUtils.assertOnFail("Changes not publish smart card is not loaded in Manager page!",smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"),false);
			analyzePage.clickOnAnalyzeBtn("history");
			analyzePage.verifyScheduleVersion(version1);
			analyzePage.closeAnalyzeWindow();
			createSchedulePage.publishActiveSchedule();
			analyzePage.clickOnAnalyzeBtn("history");
			analyzePage.verifyScheduleVersion(version2);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify the schedule version and work role shows correctly")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduleVersionAndWorkRoleShowsCorrectlyAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		AnalyzePage analyzePage = pageFactory.createAnalyzePage();
		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
		SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (isWeekGenerated){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		createSchedulePage.createScheduleForNonDGFlowNewUI();

		//verify version number in analyze page

		HashMap<String, String> valuesOnTheSmartCard = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
		analyzePage.clickOnAnalyzeBtn("labor");
		String resultTotalHrs = analyzePage.getPieChartTotalHrsFromLaborGuidanceTab();
		SimpleUtils.assertOnFail("Budget hours are inconsistent!", resultTotalHrs.contains(valuesOnTheSmartCard.get("Budget")), false);
		analyzePage.closeAnalyzeWindow();
		analyzePage.clickOnAnalyzeBtn("history");
		String versionInfo = analyzePage.getPieChartHeadersFromHistoryTab("schedule");
		String version0 = "0.0";
		SimpleUtils.assertOnFail("Version info is inconsistent!", versionInfo.contains(version0), false);
		SimpleUtils.assertOnFail("Labor guidance is inconsistent!", resultTotalHrs.equalsIgnoreCase(analyzePage.getPieChartTotalHrsFromHistoryTab("guidance")), false);
		analyzePage.closeAnalyzeWindow();

		String version1 = "0.1";
		String version2 = "1.0";
		//make edits and save
		String workRole = shiftOperatePage.getRandomWorkRole();
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.customizeNewShiftPage();
		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		newShiftPage.selectWorkRole(workRole);
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		scheduleMainPage.saveSchedule();

		analyzePage.clickOnAnalyzeBtn("history");
		versionInfo = analyzePage.getPieChartHeadersFromHistoryTab("schedule");
		SimpleUtils.assertOnFail("Version info is inconsistent!", versionInfo.contains(version1), false);
		SimpleUtils.assertOnFail("Labor guidance is inconsistent!", resultTotalHrs.equalsIgnoreCase(analyzePage.getPieChartTotalHrsFromHistoryTab("guidance")), false);
		analyzePage.closeAnalyzeWindow();

		createSchedulePage.publishActiveSchedule();
		analyzePage.clickOnAnalyzeBtn("history");
		versionInfo = analyzePage.getPieChartHeadersFromHistoryTab("schedule");
		SimpleUtils.assertOnFail("Version info is inconsistent!", versionInfo.contains(version2), false);
		SimpleUtils.assertOnFail("Labor guidance is inconsistent!", resultTotalHrs.equalsIgnoreCase(analyzePage.getPieChartTotalHrsFromHistoryTab("guidance")), false);

	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify offers generated for open shift")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyOffersGeneratedForOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
		SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (isWeekGenerated){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		createSchedulePage.createScheduleForNonDGFlowNewUI();

		//delete unassigned shifts and open shifts.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		//scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
		//Delete all shifts are action required.
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Open");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Compliance Review");
		shiftOperatePage.deleteAllShiftsInWeekView();
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		HashSet<Integer> indexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
		scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
		String action = "Edit";
		scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
		editShiftPage.clickOnAssignmentSelect();
		List<String> assignments = editShiftPage.getOptionsFromSpecificSelect();
		String assignOrOfferOption = ConsoleEditShiftPage.assignmentOptions.OpenShift.getOption();
		editShiftPage.selectSpecificOptionByText(assignOrOfferOption);
		editShiftPage.clickOnUpdateButton();
		editShiftPage.clickOnUpdateAnywayButton();

		scheduleMainPage.saveSchedule();
		createSchedulePage.publishActiveSchedule();
		int i = 0;
		boolean hasOffers = false;
		while (i < 10 && !hasOffers) {
			BasePage.waitForSeconds(10);
			shiftOperatePage.clickOnProfileIconOfOpenShift();
			scheduleShiftTablePage.clickViewStatusBtn();
			hasOffers = shiftOperatePage.checkIfOfferListHasOffers();
			shiftOperatePage.closeViewStatusContainer();
			i++;
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify x Compliance warnings in publish confirm modal")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyComplianceWarningInPublishConfirmModalAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.clickOnProfileIcon();
			shiftOperatePage.clickOnEditShiftTime();
			shiftOperatePage.editShiftTimeToTheLargest();
			shiftOperatePage.clickOnUpdateEditShiftTimeButton();
			scheduleMainPage.saveSchedule();

			String smardCard ="COMPLIANCE";
			int complianceShiftCount= smartCardPage.getComplianceShiftCountFromSmartCard(smardCard);
			createSchedulePage.publishActiveSchedule();
			String complianceMsg = createSchedulePage.getMessageForComplianceWarningInPublishConfirmModal();
			if (complianceMsg != null && !complianceMsg.equals("") && SimpleUtils.isNumeric(complianceMsg.split(" ")[0])){
				SimpleUtils.assertOnFail("Compliance count is not correct!", Integer.parseInt(complianceMsg.split(" ")[0]) == complianceShiftCount, false);
			} else {
				SimpleUtils.fail("Compliance warning has issue with count, please check!", false);
			}

			createSchedulePage.clickConfirmBtnOnPublishConfirmModal();

			//verify compliance count on smart card is consistent with before.
			SimpleUtils.assertOnFail("Compliance count on smart card is inconsistent with before", complianceShiftCount == smartCardPage.getComplianceShiftCountFromSmartCard(smardCard), false);

			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.selectShiftTypeFilterByText("Compliance Review");
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
			scheduleMainPage.saveSchedule();

			SimpleUtils.assertOnFail("Comliance smart card should not load!", !smartCardPage.isSpecificSmartCardLoaded(smardCard), false);
			createSchedulePage.publishActiveSchedule();
			SimpleUtils.assertOnFail("Compliance warning message shouldn't show up on publish confirm modal!",
					!createSchedulePage.isComplianceWarningMsgLoad(), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify x Hours over budget in publish confirm modal on non DG")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyHrsOverBudgetInPublishConfirmModalForNonDGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.selectShiftTypeFilterByText("Action Required");
			while (scheduleShiftTablePage.getShiftsCount() != 0){
				scheduleShiftTablePage.clickOnProfileOfUnassignedShift();
				shiftOperatePage.clickOnConvertToOpenShift();
				shiftOperatePage.convertToOpenShiftDirectly();
			}
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			SimpleUtils.assertOnFail("Over budget warning message shouldn't show up on publish confirm modal!", createSchedulePage.isComplianceWarningMsgLoad() , false);
			scheduleCommonPage.clickCancelButtonOnPopupWindow();
			HashMap<String, Float> values = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
			float value3 = values.get("scheduledHours");
			float value4 = values.get("budgetedHours");
			if (value4>=value3) {
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				int i = Math.round((value4 - value3) / 7) + 1;
				for (int j = 0; j < i + 1; j++) {
					newShiftPage.clickOnDayViewAddNewShiftButton();
					newShiftPage.selectWorkRole(workRoleOfTM);
					newShiftPage.clearAllSelectedDays();
					Random r = new Random();
					int index = r.nextInt(6); // 生成[0,6]区间的整数
					newShiftPage.selectDaysByIndex(index, index, index);
					//newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
					newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
					newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
					newShiftPage.clickOnCreateOrNextBtn();
				}
				scheduleMainPage.saveSchedule();
			}

			values = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
			value3 = values.get("scheduledHours");
			value4 = values.get("budgetedHours");
			createSchedulePage.publishActiveSchedule();
			String warningMsg = createSchedulePage.getMessageForComplianceWarningInPublishConfirmModal();
			SimpleUtils.assertOnFail("Over budget warning message shouldn't show up on publish confirm modal!", warningMsg.contains(String.valueOf(Math.abs(value3-value4)).replace(".0", "")) && warningMsg.contains("Hrs over guidance") , false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "KendraScott2_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify search bar on schedule page")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySearchBarOnSchedulePageInWeekViewAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
		try{
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
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
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//click search button
			scheduleMainPage.clickOnOpenSearchBoxButton();

			//Check the ghost text inside the Search bar
			scheduleMainPage.verifyGhostTextInSearchBox();

			//Get the info of first shift
			List<String> shiftInfo = new ArrayList<>();
			String firstNameOfTM = "";
			while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
					|| firstNameOfTM.equalsIgnoreCase("Unassigned")) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				//Search shift by TM names: first name and last name
				firstNameOfTM = shiftInfo.get(0);
			}

			List<WebElement> searchResultOfFirstName = scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			scheduleShiftTablePage.verifySearchResult(firstNameOfTM, null, null, null, searchResultOfFirstName);

			String lastNameOfTM = shiftInfo.get(5);
			List<WebElement> searchResultOfLastName = scheduleMainPage.searchShiftOnSchedulePage(lastNameOfTM);
			scheduleShiftTablePage.verifySearchResult(null, lastNameOfTM, null, null, searchResultOfLastName);

			//Search shift by work role
			String workRole = shiftInfo.get(4);
			List<WebElement> searchResultOfWorkRole = scheduleMainPage.searchShiftOnSchedulePage(workRole);
			scheduleShiftTablePage.verifySearchResult(null, null, workRole, null, searchResultOfWorkRole);

			//Search shift by job title
			String jobTitle = shiftInfo.get(3);
			List<WebElement> searchResultOfJobTitle = scheduleMainPage.searchShiftOnSchedulePage(jobTitle);
			scheduleShiftTablePage.verifySearchResult(null, null, null, jobTitle, searchResultOfJobTitle);

			//Click X button to close search box
			scheduleMainPage.clickOnCloseSearchBoxButton();
			Thread.sleep(3000);
			//Go to edit mode
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//click search button
			scheduleMainPage.clickOnOpenSearchBoxButton();
			//Click X button to close search box
			scheduleMainPage.clickOnCloseSearchBoxButton();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}


	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify assign TM message: If SM wants to schedule a TM from another location and schedule hasn’t been generated or published yet")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
	public void verifyAssignTMMessageWhenScheduleTMToAnotherLocationWithHomeLocationScheduleNotBeenGeneratedOrPublishedAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
		try{
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsPage.gotoControlsPage();
			SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
			controlsNewUIPage.updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule("Yes, anytime");
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.publishNowTheTemplate();
			Thread.sleep(3000);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();

			dashboardPage.navigateToDashboard();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			String nyLocation = getCrendentialInfo("NearByLocationInfo");
			locationSelectorPage.changeUpperFieldDirect("Location", nyLocation);
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			// Select one team member to view profile
			Thread.sleep(3000);
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			List<String> tmList = teamPage.getTMNameList();
			String firstName = tmList.get(0);
			teamPage.activeTMAndRejectOrApproveAllAvailabilityAndTimeOff(firstName);

			//Go to schedule page

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			// Navigate to next week
			scheduleCommonPage.navigateToNextWeek();

			// create the schedule if not created
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}

			//Select AUSTIN DOWNTOWN location
			dashboardPage.navigateToDashboard();
			locationSelectorPage.changeUpperFieldDirect("Location", location);
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.navigateToNextWeek();

			boolean isWeekGenerated2 = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated2) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			// Edit the Schedule
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			// Delete all the shifts that are assigned to the team member on Step #1
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
			String workRole = shiftOperatePage.getRandomWorkRole();

			// Create new shift for this schedule
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchWithOutSelectTM(firstName);
			String scheduleStatus = shiftOperatePage.getTheMessageOfTMScheduledStatus();
			SimpleUtils.assertOnFail("TM scheduled status message display failed",
					scheduleStatus.contains("Schedule not published") ||
							scheduleStatus.contains("Schedule Not Created"), false);

			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleShiftTablePage.verifyDayHasShiftByName(0, firstName.split(" ")[0]);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify assign TM warning: If SM wants to schedule a TM from another location and schedule hasn’t been published")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMWarningForScheduleTMFromAnotherLocAndScheduleNotPublishedAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsPage.gotoControlsPage();
			SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();

			SimpleUtils.assertOnFail("collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
			controlsNewUIPage.updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule("No, home location must publish schedule first");
			dashboardPage.navigateToDashboard();
			String anotherLocation = "NY CENTRAL";
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			locationSelectorPage.changeLocation(anotherLocation);
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
			// Navigate to a week
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			// create the schedule if not created
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			List<String> shiftInfo = new ArrayList<>();
			while (shiftInfo.size() == 0 || shiftInfo.get(0).equalsIgnoreCase("Open")) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
			}
			String firstNameOfTM1 = shiftInfo.get(0);
			String workRoleOfTM1 = shiftInfo.get(4);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			scheduleMainPage.saveSchedule();
			dashboardPage.navigateToDashboard();
			locationSelectorPage.changeLocation(location);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
			// Navigate to a week
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			// create the schedule if not created
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.selectWorkRole(workRoleOfTM1);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM1);
			shiftOperatePage.verifyMessageIsExpected("schedule not published");
			shiftOperatePage.verifyWarningModelMessageAssignTMInAnotherLocWhenScheduleNotPublished();
			shiftOperatePage.verifyTMNotSelected();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "verify shifts display normally after switch to day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyShiftsDisplayNormallyInDayViewAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
			// Navigate to a week
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			// create the schedule if not created
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "09:00AM", "08:00PM");
			scheduleShiftTablePage.verifyDayHasShifts("Sunday");
			scheduleShiftTablePage.verifyDayHasShifts("Monday");
			scheduleShiftTablePage.verifyDayHasShifts("Tuesday");
			scheduleShiftTablePage.verifyDayHasShifts("Wednesday");
			scheduleShiftTablePage.verifyDayHasShifts("Thursday");
			scheduleShiftTablePage.verifyDayHasShifts("Friday");
			scheduleShiftTablePage.verifyDayHasShifts("Saturday");
			scheduleCommonPage.clickOnDayView();
			List<WebElement> shiftsInDayView = scheduleShiftTablePage.getAvailableShiftsInDayView();
			SimpleUtils.assertOnFail("Day view shifts don't diaplay successfully!", !shiftsInDayView.isEmpty(), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the functionality for Schedule Copy Restrictions")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheFunctionalityForScheduleCopyRestrictionsAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			controlsPage.clickGlobalSettings();

			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			controlsNewUIPage.enableOrDisableScheduleCopyRestriction("yes");
			controlsNewUIPage.setViolationLimit("2");
			controlsNewUIPage.setBudgetOverageLimit("0");
			controlsNewUIPage.enableOrDisableScheduleCopyRestriction("no");
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the functionality of Violation limit and Budget overage limit")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyViolationLimitAndBudgetOverageLimitAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			controlsPage.gotoControlsPage();
			controlsPage.clickGlobalSettings();

			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			controlsNewUIPage.enableOrDisableScheduleCopyRestriction("yes");
			controlsNewUIPage.setViolationLimit("2");
			controlsNewUIPage.setBudgetOverageLimit("0");

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			//Make current week 3 or more violation
			String pastWeekInfo1 = scheduleCommonPage.getActiveWeekText().substring(10);
			pastWeekInfo1 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo1);
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			List<String> shiftInfo = new ArrayList<>();
			while (shiftInfo.size() == 0) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
			}
			String firstNameOfTM1 = shiftInfo.get(0);
			String workRoleOfTM1 = shiftInfo.get(4);
			List<String> shiftInfo2 = new ArrayList<>();
			while (shiftInfo2.size() == 0) {
				shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
			}
			String firstNameOfTM2 = shiftInfo2.get(0);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.selectWorkRole(workRoleOfTM1);
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(1, 2, 3);
			newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			//newShiftPage.moveSliderAtSomePoint("8", 0, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			//Make next week 2 or less violation
			scheduleCommonPage.navigateToNextWeek();
			String pastWeekInfo2 = scheduleCommonPage.getActiveWeekText().substring(10);
			pastWeekInfo2 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo2);
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.selectWorkRole(workRoleOfTM1);
			newShiftPage.clearAllSelectedDays();
			//create 2 overtime violation
			newShiftPage.selectDaysByIndex(1, 1, 1);
			newShiftPage.selectDaysByIndex(2, 2, 2);
			newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			//newShiftPage.moveSliderAtSomePoint("8", 0, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();


			//Make another next week 0 violation
			scheduleCommonPage.navigateToNextWeek();
			String pastWeekInfo3 = scheduleCommonPage.getActiveWeekText().substring(10);
			pastWeekInfo3 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo3);
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			createSchedulePage.publishActiveSchedule();

			//Go to another next week to check copy restriction.
			scheduleCommonPage.navigateToNextWeek();

			// Ungenerate the schedule if it has generated
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.clickCreateScheduleBtn();
			createSchedulePage.clickNextBtnOnCreateScheduleWindow();
			createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo1, false);
			createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo2, true);
			createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo3, true);
			createSchedulePage.clickBackBtnAndExitCreateScheduleWindow();

			controlsPage.gotoControlsPage();
			controlsPage.clickGlobalSettings();
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			controlsNewUIPage.enableOrDisableScheduleCopyRestriction("no");

			//Verify budget overage limit
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			//Regenerate current week schedule
			pastWeekInfo1 = scheduleCommonPage.getActiveWeekText().substring(10);
			pastWeekInfo1 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo1);
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			float value1 = toggleSummaryPage.getStaffingGuidanceHrs();
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			createSchedulePage.publishActiveSchedule();
			//Make next week has higher schedule hours than budget hours
			scheduleCommonPage.navigateToNextWeek();
			pastWeekInfo2 = scheduleCommonPage.getActiveWeekText().substring(10);
			pastWeekInfo2 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo2);
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			float value2 = toggleSummaryPage.getStaffingGuidanceHrs();
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			shiftInfo = new ArrayList<>();
			while (shiftInfo.size() == 0) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
			}
			workRoleOfTM1 = shiftInfo.get(4);
			createSchedulePage.publishActiveSchedule();


			//Regenerate another next week schedule
			scheduleCommonPage.navigateToNextWeek();
			pastWeekInfo3 = scheduleCommonPage.getActiveWeekText().substring(10);
			pastWeekInfo3 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo3);
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			float value3 = toggleSummaryPage.getStaffingGuidanceHrs();
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			createSchedulePage.publishActiveSchedule();

			//Go to another next week to check copy restriction.
			scheduleCommonPage.navigateToNextWeek();

			// Ungenerate the schedule if it has generated
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			float value4 = toggleSummaryPage.getStaffingGuidanceHrs();
			if (value4>=value3){
				scheduleCommonPage.clickOnScheduleConsoleMenuItem();
				SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
						scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
				scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
				SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
						scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

				scheduleCommonPage.navigateToNextWeek();
				scheduleCommonPage.navigateToNextWeek();
				int i = Math.round((value4-value3)/7) + 1;
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				for (int j = 0; j < i + 1; j++) {
					newShiftPage.clickOnDayViewAddNewShiftButton();
					newShiftPage.selectWorkRole(workRoleOfTM1);
					newShiftPage.clearAllSelectedDays();
					Random r = new Random();
					int index = r.nextInt(6); // 生成[0,6]区间的整数
					newShiftPage.selectDaysByIndex(index, index, index);
					//create shifts has 7 hours.
					newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
					newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
					newShiftPage.clickOnCreateOrNextBtn();
				}
			}
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			scheduleCommonPage.navigateToNextWeek();
			createSchedulePage.clickCreateScheduleBtn();
			createSchedulePage.clickNextBtnOnCreateScheduleWindow();

			createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo3, false);
			createSchedulePage.verifyTooltipForCopyScheduleWeek(pastWeekInfo3);
			if (value4>=value2){
				createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo2,  true);
			} else {
				createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo2,  false);
			}
			if (value4>=value2){
				createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo1, true);
			} else {
				createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo1, false);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify turn off the Schedule Copy Restriction")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTurnOffCopyRestrictionAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			controlsPage.gotoControlsPage();
			controlsPage.clickGlobalSettings();

			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			controlsNewUIPage.enableOrDisableScheduleCopyRestriction("no");


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			//Make current week 3 or more violation
			String pastWeekInfo1 = scheduleCommonPage.getActiveWeekText().substring(10);
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			List<String> shiftInfo = new ArrayList<>();
			while (shiftInfo.size() == 0) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
			}
			String firstNameOfTM1 = shiftInfo.get(0);
			String workRoleOfTM1 = shiftInfo.get(4);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.selectWorkRole(workRoleOfTM1);
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(1, 2, 3);
			newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			Random r = new Random();
			int index = r.nextInt(10);
			List<String> randomShiftInfoFromCopiedWeek = scheduleShiftTablePage.getTheShiftInfoByIndex(index);

			//Go to next week to check copy restriction.
			scheduleCommonPage.navigateToNextWeek();

			// Ungenerate the schedule if it has generated
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.clickCreateScheduleBtn();
			createSchedulePage.clickNextBtnOnCreateScheduleWindow();
			pastWeekInfo1 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo1);
			createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo1, true);
			createSchedulePage.selectWhichWeekToCopyFrom(pastWeekInfo1);
			createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();
			scheduleShiftTablePage.verifyDayHasShifts("Sunday");
			scheduleShiftTablePage.verifyDayHasShifts("Monday");
			scheduleShiftTablePage.verifyDayHasShifts("Tuesday");
			scheduleShiftTablePage.verifyDayHasShifts("Wednesday");
			scheduleShiftTablePage.verifyDayHasShifts("Thursday");
			scheduleShiftTablePage.verifyDayHasShifts("Friday");
			scheduleShiftTablePage.verifyDayHasShifts("Saturday");
			List<String> sameIndexShiftInfoFromThisWeek = scheduleShiftTablePage.getTheShiftInfoByIndex(index);

			if (randomShiftInfoFromCopiedWeek.equals(sameIndexShiftInfoFromThisWeek)){
				SimpleUtils.pass("The 2 week have the same shifts!");
			} else {
				SimpleUtils.fail("Shifts are not the same as copied week!", false);
			}

			controlsPage.gotoControlsPage();
			controlsPage.clickGlobalSettings();
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			controlsNewUIPage.enableOrDisableScheduleCopyRestriction("yes");
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify turn on Schedule Copy Restriction and copy schedule")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTurnOnCopyRestrictionAndCheckCopyResultAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();

			controlsPage.gotoControlsPage();
			controlsPage.clickGlobalSettings();

			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			controlsNewUIPage.enableOrDisableScheduleCopyRestriction("yes");
			controlsNewUIPage.setViolationLimit("2");
			controlsNewUIPage.setBudgetOverageLimit("0");


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			String pastWeekInfo1 = scheduleCommonPage.getActiveWeekText().substring(10);
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			List<String> shiftInfo = new ArrayList<>();
			while (shiftInfo.size() == 0) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
			}
			String firstNameOfTM1 = shiftInfo.get(0);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			//Go to next week.
			scheduleCommonPage.navigateToNextWeek();
			// Ungenerate the schedule if it has generated
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.clickCreateScheduleBtn();
			createSchedulePage.editOperatingHoursWithGivingPrameters("Sunday", "10:00AM", "9:00PM");
			createSchedulePage.clickNextBtnOnCreateScheduleWindow();
			pastWeekInfo1 = scheduleCommonPage.convertDateStringFormat(pastWeekInfo1);
			createSchedulePage.verifyPreviousWeekWhenCreateAndCopySchedule(pastWeekInfo1, true);
			createSchedulePage.selectWhichWeekToCopyFrom(pastWeekInfo1);
			createSchedulePage.verifyDifferentOperatingHours(pastWeekInfo1);
			createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();
			List<String> shiftsInfos = scheduleShiftTablePage.getDayShifts("0");
			String startTime = null;
			String endTime = null;
			if (shiftsInfos!=null){
				startTime = shiftsInfos.get(0).split("\n")[0].split(" - ")[0];
				endTime = shiftsInfos.get(shiftsInfos.size()-1).split("\n")[0].split(" - ")[1];
			}
			//compare startTime to 10:00AM
			String items[] = startTime.substring(0,startTime.length()-2).split(":");
			if (startTime.contains("pm") ){
				SimpleUtils.pass("Shift start time is within time range!");
			} else if (items.length > 0 && SimpleUtils.isNumeric(items[0]) && Integer.parseInt(items[0])>=10){
				SimpleUtils.pass("Shift start time is within time range!");
			} else {
				SimpleUtils.fail("Shift start time is out of time range!", false);
			}
			//compare endTime to 9:00PM
			String items2[] = endTime.substring(0,endTime.length()-2).split(":");
			if (endTime.contains("am") ){
				SimpleUtils.pass("Shift end time is within time range!");
			} else if (items2.length > 0 && SimpleUtils.isNumeric(items2[0]) && Integer.parseInt(items2[0])<=9){
				SimpleUtils.pass("Shift end time is within time range!");
			} else {
				SimpleUtils.fail("Shift end time is out of time range!", false);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify internal admin can see employee home location on shift menu")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyCanSeeEmployeeHomeLocationAsInternalAdmin(String browser, String username, String password, String location) {
		try {

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
			scheduleMainPage.saveSchedule();

			//Click on one of the profile icons to get the home location and related info.
			shiftOperatePage.clickOnProfileIcon();
			Map<String, String> workerInfo = scheduleShiftTablePage.getHomeLocationInfo();

			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(workerInfo.get("worker name"));

			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			ArrayList<String> badges = profileNewUIPage.getUserBadgesDetailsFromProfilePage();
			profileNewUIPage.verifyHRProfileInformationSectionIsLoaded();
			Map<String, String> result = profileNewUIPage.getHRProfileInfo();

			SimpleUtils.assertOnFail("Employment status is not consistent!", result.get("EMPLOYMENT STATUS").contains(workerInfo.get("PTorFT").substring(0,1)), false);
			SimpleUtils.assertOnFail("Home location is not consistent!", result.get("HOME STORE").contains(workerInfo.get("homeLocation").substring(0,1)), false);
			SimpleUtils.assertOnFail("Badge info is not consistent!", workerInfo.get("badgeSum").contains(String.valueOf(badges.size())), false);
			SimpleUtils.assertOnFail("Job title is not consistent!", result.get("JOB TITLE").contains(workerInfo.get("job title").substring(0,1)), false);

		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify store manager can see employee home location on shift menu")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyCanSeeEmployeeHomeLocationAsStoreManager(String browser, String username, String password, String location) {
		try {

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
			scheduleMainPage.saveSchedule();

			//Click on one of the profile icons to get the home location and related info.
			shiftOperatePage.clickOnProfileIcon();
			Map<String, String> workerInfo = scheduleShiftTablePage.getHomeLocationInfo();

			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(workerInfo.get("worker name"));

			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			ArrayList<String> badges = profileNewUIPage.getUserBadgesDetailsFromProfilePage();
			profileNewUIPage.verifyHRProfileInformationSectionIsLoaded();
			Map<String, String> result = profileNewUIPage.getHRProfileInfo();

			SimpleUtils.assertOnFail("Employment status is not consistent!", result.get("EMPLOYMENT STATUS").contains(workerInfo.get("PTorFT").substring(0,1)), false);
			SimpleUtils.assertOnFail("Home location is not consistent!", result.get("HOME STORE").contains(workerInfo.get("homeLocation").substring(0,1)), false);
			SimpleUtils.assertOnFail("Badge info is not consistent!", workerInfo.get("badgeSum").contains(String.valueOf(badges.size())), false);
			SimpleUtils.assertOnFail("Job title is not consistent!", result.get("JOB TITLE").contains(workerInfo.get("job title").substring(0,1)), false);

		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify area manager can see employee home location on shift menu")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyCanSeeEmployeeHomeLocationAsDistrictManager(String browser, String username, String password, String location) {
		try {

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
			scheduleMainPage.saveSchedule();

			//Click on one of the profile icons to get the home location and related info.
			shiftOperatePage.clickOnProfileIcon();
			Map<String, String> workerInfo = scheduleShiftTablePage.getHomeLocationInfo();

			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(workerInfo.get("worker name"));

			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			ArrayList<String> badges = profileNewUIPage.getUserBadgesDetailsFromProfilePage();
			profileNewUIPage.verifyHRProfileInformationSectionIsLoaded();
			Map<String, String> result = profileNewUIPage.getHRProfileInfo();

			SimpleUtils.assertOnFail("Employment status is not consistent!", result.get("EMPLOYMENT STATUS").contains(workerInfo.get("PTorFT").substring(0,1)), false);
			SimpleUtils.assertOnFail("Home location is not consistent!", result.get("HOME STORE").contains(workerInfo.get("homeLocation").substring(0,1)), false);
			SimpleUtils.assertOnFail("Badge info is not consistent!", workerInfo.get("badgeSum").contains(String.valueOf(badges.size())), false);
			SimpleUtils.assertOnFail("Job title is not consistent!", result.get("JOB TITLE").contains(workerInfo.get("job title").substring(0,1)), false);

		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Group by Day Parts can be collapsed/expanded")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByDayPartsCanBeCollapsedNExpandedAsStoreManager(String browser, String username, String password, String location) {
		try {
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyDayParts.getValue());
			scheduleShiftTablePage.verifyGroupCanbeCollapsedNExpanded();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Group by Work Role can be collapsed/expanded")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByWorkRoleCanBeCollapsedNExpandedAsStoreManager(String browser, String username, String password, String location) throws Exception{
		try {
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
			scheduleShiftTablePage.verifyGroupCanbeCollapsedNExpanded();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Group by Job Title can be collapsed/expanded")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByJobTitleCanBeCollapsedNExpandedAsStoreManager(String browser, String username, String password, String location) throws Exception{
		try {
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
			scheduleShiftTablePage.verifyGroupCanbeCollapsedNExpanded();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Group by Location can be collapsed/expanded")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByLocationCanBeCollapsedNExpandedAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		try {
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(getCrendentialInfo("LGInfo"));
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
			scheduleShiftTablePage.verifyGroupCanbeCollapsedNExpanded();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate there is option to edit notes for assigned shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditNotesOptionIsAvailableAsInternalAdmin(String browser, String username, String password, String location) throws Exception{

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		int i = 0;
		int index = 0;
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(i);
		while (shiftInfo.size() == 0 && shiftInfo.get(0).toLowerCase().contains("open")) {
			i++;
			if (i>10 && i>= scheduleShiftTablePage.getShiftsCount()){
				break;
			}
			shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(i);
			index = i;
		}
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		String shiftInfoOnDialog = shiftOperatePage.getShiftInfoInEditShiftDialog();
		//add shift notes.
		shiftOperatePage.addShiftNotesToTextarea("new shift notes");
		scheduleMainPage.saveSchedule();
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		shiftOperatePage.verifyShiftNotesContent("new shift notes");
		if (shiftInfoOnDialog!=null && !shiftInfoOnDialog.equalsIgnoreCase("") && shiftInfoOnDialog.contains(shiftInfo.get(2))&& shiftInfoOnDialog.contains(shiftInfo.get(3))
				&& shiftInfoOnDialog.contains(shiftInfo.get(0))){
			SimpleUtils.pass("Shift info is consistent!");
		} else {
			SimpleUtils.fail("Shift info on edit shift notes dialog is not correct!", false);
		}

	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate there is option to edit notes for open shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditNotesOptionIsAvailableForOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		String workRole = shiftOperatePage.getRandomWorkRole();
		//create an open shifts.
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.customizeNewShiftPage();
		//newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		newShiftPage.moveSliderAtCertainPoint("8","8");
		newShiftPage.selectWorkRole(workRole);
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("open");
		int index = 0;
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		String shiftInfoOnDialog = shiftOperatePage.getShiftInfoInEditShiftDialog();
		//add shift notes.
		shiftOperatePage.addShiftNotesToTextarea("new shift notes for open shift");
		scheduleMainPage.saveSchedule();
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
		shiftOperatePage.clickOnEditShiftNotesOption();
		shiftOperatePage.verifyShiftNotesContent("new shift notes for open shift");
		if (shiftInfoOnDialog!=null && !shiftInfoOnDialog.equalsIgnoreCase("") && shiftInfoOnDialog.contains(shiftInfo.get(2))&& shiftInfoOnDialog.contains(shiftInfo.get(3))){
			SimpleUtils.pass("Shift info is consistent!");
		} else {
			SimpleUtils.fail("Shift info on edit shift notes dialog is not correct!", false);
		}

	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "validate update notes for assigned shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyUpdateShiftNotesForAssignedShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		int i = 0;
		int index = 0;
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(i);
		while (shiftInfo.size() == 0 && shiftInfo.get(0).toLowerCase().contains("open")) {
			i++;
			if (i>10 && i>= scheduleShiftTablePage.getShiftsCount()){
				break;
			}
			shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(i);
			index = i;
		}
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		//add shift notes.
		shiftOperatePage.addShiftNotesToTextarea("shift notes version1");
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		//update shift notes.
		shiftOperatePage.addShiftNotesToTextarea("shift notes version2");
		scheduleMainPage.saveSchedule();
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		shiftOperatePage.verifyShiftNotesContent("shift notes version2");
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "validate update notes for open shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyUpdateEditNotesForOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		String workRole = shiftOperatePage.getRandomWorkRole();
		//create an open shifts.
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.customizeNewShiftPage();
		newShiftPage.moveSliderAtCertainPoint("8","8");
		newShiftPage.selectWorkRole(workRole);
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("open");
		int index = 0;
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		//add shift notes.
		shiftOperatePage.addShiftNotesToTextarea("shift notes version1");
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		//update shift notes.
		shiftOperatePage.addShiftNotesToTextarea("shift notes version2");
		scheduleMainPage.saveSchedule();
		scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
		shiftOperatePage.clickOnEditShiftNotesOption();
		shiftOperatePage.verifyShiftNotesContent("shift notes version2");
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate the those employees scheduled in other locations show up when group by TM")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEmployeesScheduledInOtherLocationShowUpInGroupByTMAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		String nyLocation = getCrendentialInfo("NearByLocationInfo");
		locationSelectorPage.changeLocation(nyLocation);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.saveSchedule();
		createSchedulePage.publishActiveSchedule();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String employeeName = shiftInfo.get(0);

		//change location back.
		locationSelectorPage.changeLocation(location);
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(employeeName);
		String workRole = shiftOperatePage.getRandomWorkRole();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.customizeNewShiftPage();
		newShiftPage.moveSliderAtCertainPoint("8","8");
		newShiftPage.selectWorkRole(workRole);
		newShiftPage.clickRadioBtnStaffingOption(staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		newShiftPage.searchTeamMemberByNameNLocation(employeeName, nyLocation);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(employeeName);
		List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(0);

		//change to nearby location.
		locationSelectorPage.changeLocation(nyLocation);
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyTM.getValue());
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(employeeName);
		List<String> shiftInfo3 = scheduleShiftTablePage.getTheGreyedShiftInfoByIndex(0);
		shiftInfo2.remove(7);
		shiftInfo3.remove(7);
		shiftInfo2.remove(4);
		shiftInfo3.remove(4);
		SimpleUtils.assertOnFail("ShiftInfo should be consistent!", shiftInfo2.containsAll(shiftInfo3), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate the those employees scheduled come from other locations show up when group by TM")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEmployeesFromOtherLocationShowUpInGroupByTMAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		String nyLocation = getCrendentialInfo("NearByLocationInfo");
		locationSelectorPage.changeLocation(nyLocation);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.saveSchedule();
		createSchedulePage.publishActiveSchedule();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String employeeName = shiftInfo.get(0);

		//change location back.
		locationSelectorPage.changeLocation(location);
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(employeeName);
		String workRole = shiftOperatePage.getRandomWorkRole();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.customizeNewShiftPage();
		newShiftPage.moveSliderAtCertainPoint("8","8");
		newShiftPage.selectWorkRole(workRole);
		newShiftPage.clickRadioBtnStaffingOption(staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		//newShiftPage.searchTeamMemberByName(employeeName);
		newShiftPage.searchTeamMemberByNameNLocation(employeeName, nyLocation);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(employeeName);
		List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(0);

		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyTM.getValue());
		List<String> shiftInfo3 = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		SimpleUtils.assertOnFail("Total hrs info should be consistent!", shiftInfo3.get(shiftInfo3.size()-1).contains(scheduleShiftTablePage.getTotalHrsFromRightStripCellByIndex(0)), false);
		SimpleUtils.assertOnFail("ShiftInfo should be consistent!", shiftInfo2.containsAll(shiftInfo3), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate location info in the profile popup for those shifts that are assigned to employees from other location")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEmployeesLocationInfoFromOtherLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		String nyLocation = getCrendentialInfo("NearByLocationInfo");
		locationSelectorPage.changeLocation(nyLocation);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.saveSchedule();
		createSchedulePage.publishActiveSchedule();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String employeeName = shiftInfo.get(0);

		//change location back.
		locationSelectorPage.changeLocation(location);
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(employeeName);
		String workRole = shiftOperatePage.getRandomWorkRole();
		//create an open shifts.
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.customizeNewShiftPage();
		//newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		newShiftPage.moveSliderAtCertainPoint("8","8");
		newShiftPage.selectWorkRole(workRole);
		newShiftPage.clickRadioBtnStaffingOption(staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		//newShiftPage.searchTeamMemberByName(employeeName);
		newShiftPage.searchTeamMemberByNameNLocation(employeeName, nyLocation);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(employeeName);

		shiftOperatePage.clickOnProfileIcon();
		Map <String, String> locationInfo = scheduleShiftTablePage.getHomeLocationInfo();
		SimpleUtils.assertOnFail("Home location info is incorrect!", nyLocation.contains(locationInfo.get("homeLocation").replace("...", "")), false);

		//change to nearby location.
		locationSelectorPage.changeLocation(nyLocation);
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyTM.getValue());
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(employeeName);
		List<String> shiftInfo3 = scheduleShiftTablePage.getTheGreyedShiftInfoByIndex(0);
		SimpleUtils.assertOnFail("Work location info is incorrect!", shiftInfo3.get(4).contains(location), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate the total hours in the 'information' popup")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheTotalHrsOfEmployeesScheduledInOtherLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
		String nyLocation = getCrendentialInfo("NearByLocationInfo");
		locationSelectorPage.changeLocation(nyLocation);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.saveSchedule();
		createSchedulePage.publishActiveSchedule();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String employeeName = shiftInfo.get(0);

		//change location back.
		locationSelectorPage.changeLocation(location);
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//edit schedule
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(employeeName);
		String workRole = shiftOperatePage.getRandomWorkRole();
		//create an open shifts.
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.customizeNewShiftPage();
		//newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		newShiftPage.moveSliderAtCertainPoint("8","8");
		newShiftPage.selectWorkRole(workRole);
		newShiftPage.clickRadioBtnStaffingOption(staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		//newShiftPage.searchTeamMemberByName(employeeName);
		newShiftPage.searchTeamMemberByNameNLocation(employeeName, nyLocation);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(employeeName);
		List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(0);

		//change to nearby location.
		locationSelectorPage.changeLocation(nyLocation);
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyTM.getValue());
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(employeeName);
		List<String> shiftInfo3 = scheduleShiftTablePage.getTheGreyedShiftInfoByIndex(0);
		SimpleUtils.assertOnFail("Total hrs info should be consistent!", shiftInfo3.get(shiftInfo3.size()-1).contains(scheduleShiftTablePage.getTotalHrsFromRightStripCellByIndex(0)), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "validate group by XXX results order")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByResultsOrderedAlphabetilyAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//Group by work role and check the results.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesOrder();
		//Group by job title and check the results.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesOrder();
		//Group by TM and check the results.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyTM.getValue());
		scheduleShiftTablePage.verifyGroupByTMOrderResults();
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "validate group by all and group by day parts results order")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByAllResultsOrderedByStartTimeAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//Group by work role and check the results.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyAll.getValue());
		scheduleShiftTablePage.verifyShiftsOrderByStartTime();
		//Group by day parts and check the results.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyDayParts.getValue());
		scheduleShiftTablePage.expandOnlyOneGroup("UNSPECIFIED");
		scheduleShiftTablePage.verifyShiftsOrderByStartTime();
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify automatically expand when clicking group by on regular location")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAutomaticallyExpandWhenGroupByInRegularLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

		scheduleCommonPage.navigateToNextWeek();
		boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (!isWeekGenerated) {
			createSchedulePage.createScheduleForNonDGFlowNewUI();
		}
		//Group by work role and check the group.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
		//Group by job title and check the group.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
		//Group by day parts and check the group.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyDayParts.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();

		//Edit-mode
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		//Group by work role and check the group.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
		//Group by job title and check the group.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
		//Group by day parts and check the group.
		scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyDayParts.getValue());
		scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify settings for excess of x hours in a 24 hour period.")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySettingsExcessOf8HrsInA24HrsPeriodAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		// Checking configuration in controls
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
		controlsNewUIPage.clickOnControlsConsoleMenu();
		controlsNewUIPage.clickOnControlsComplianceSection();
		controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(true);
		controlsNewUIPage.editDailyOT("12 hours", "24 hour period", true);
		controlsNewUIPage.editDailyOT("10 hours", "24 hour period", true);
		controlsNewUIPage.editDailyOT("8 hours", "24 hour period", true);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify excess of 8 hours in a 24 hour period.")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyExcessOf8HrsInA24HrsPeriodAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		// set configuration in controls
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
		controlsNewUIPage.clickOnControlsConsoleMenu();
		controlsNewUIPage.clickOnControlsComplianceSection();
		controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(true);
		controlsNewUIPage.editDailyOT("8 hours", "24 hour period", true);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
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
		String startTime = "08:00PM";
		String endTime = "06:00AM";
		createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange(startTime, endTime);

		//delete unassigned shifts.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String firstNameOfTM = shiftInfo.get(0);
		String workRoleOfTM = shiftInfo.get(4);
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
		scheduleMainPage.saveSchedule();

		//add new shift and assign TM.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.selectWorkRole(workRoleOfTM);
		if (newShiftPage.checkIfNewCreateShiftPageDisplay()){
			newShiftPage.moveSliderAtCertainPoint("5:00am",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("7:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(true);
		} else {
			newShiftPage.moveSliderAtCertainPoint("5",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("7",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
		}
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		newShiftPage.searchTeamMemberByName(firstNameOfTM);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();

		//verify daily OT violation after saving.
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
		List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
		SimpleUtils.assertOnFail("Daily OT violation is not showing!", complianceMessage.contains("1.5 hrs daily overtime"), false);

		// set configuration in controls
		controlsNewUIPage.clickOnControlsConsoleMenu();
		controlsNewUIPage.clickOnControlsComplianceSection();
		controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(true);
		controlsNewUIPage.editDailyOT("8 hours", "single work day", true);

		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
		scheduleCommonPage.navigateToNextWeek();
		isWeekGenerated = createSchedulePage.isWeekGenerated();
		if (isWeekGenerated) {
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		createSchedulePage.createScheduleForNonDGFlowNewUI();

		//delete unassigned shifts.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
		scheduleMainPage.saveSchedule();

		//add new shift and assign TM.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.selectWorkRole(workRoleOfTM);
		newShiftPage.moveSliderAtCertainPoint("5pm",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		newShiftPage.moveSliderAtCertainPoint("7am",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		newShiftPage.searchTeamMemberByName(firstNameOfTM);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();

		//verify daily OT violation after saving.
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
		complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
		SimpleUtils.assertOnFail("Daily OT violation is not showing!", complianceMessage.contains("1.5 hrs daily overtime"), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify excess of 10 hours in a 24 hour period.")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyExcessOf10HrsInA24HrsPeriodAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		// set configuration in controls
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
		controlsNewUIPage.clickOnControlsConsoleMenu();
		controlsNewUIPage.clickOnControlsComplianceSection();
		controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(true);
		controlsNewUIPage.editDailyOT("10 hours", "24 hour period", true);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
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
		createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("8:00PM", "8:00AM");
		createSchedulePage.switchToManagerViewToCheckForSecondGenerate();

		//delete unassigned shifts.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String firstNameOfTM = shiftInfo.get(0);
		String workRoleOfTM = shiftInfo.get(4);
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
		scheduleMainPage.saveSchedule();

		//add new shift and assign TM.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.selectWorkRole(workRoleOfTM);
		if (newShiftPage.checkIfNewCreateShiftPageDisplay()) {
			newShiftPage.moveSliderAtCertainPoint("5:00am",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("6:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(true);
		} else {
			newShiftPage.moveSliderAtCertainPoint("5",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("6",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
		}
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		newShiftPage.searchTeamMemberByName(firstNameOfTM);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();

		//verify daily OT violation after saving.
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
		List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
		SimpleUtils.assertOnFail("Daily OT violation is not showing!", complianceMessage.contains("0.5 hrs daily overtime"), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify excess of 12 hours in a 24 hour period.")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyExcessOf12HrsInA24HrsPeriodAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		// set configuration in controls
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
		controlsNewUIPage.clickOnControlsConsoleMenu();
		controlsNewUIPage.clickOnControlsComplianceSection();
		controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(true);
		controlsNewUIPage.editDailyOT("12 hours", "24 hour period", true);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();

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
		createSchedulePage.clickCreateScheduleBtn();
		createSchedulePage.editOperatingHoursWithGivingPrameters("8:00PM", "8:00AM");
		createSchedulePage.clickNextBtnOnCreateScheduleWindow();
		createSchedulePage.selectWhichWeekToCopyFrom("SUGGESTED");
		createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();
		createSchedulePage.switchToManagerViewToCheckForSecondGenerate();

		//delete unassigned shifts.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String firstNameOfTM = shiftInfo.get(0);
		String workRoleOfTM = shiftInfo.get(4);
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
		scheduleMainPage.saveSchedule();

		//add new shift and assign TM.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.selectWorkRole(workRoleOfTM);
		newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		if (newShiftPage.checkIfNewCreateShiftPageDisplay()) {
			newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(true);
		}
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		newShiftPage.searchTeamMemberByName(firstNameOfTM);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();

		//verify daily OT violation after saving.
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
		List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
		SimpleUtils.assertOnFail("Daily OT violation is not showing!", complianceMessage.contains("1.5 hrs daily overtime"), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify excess of 8 hours in a single work day.")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyExcessOf8HrsInASingleWorkDayAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		// set configuration in controls
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
		controlsNewUIPage.clickOnControlsConsoleMenu();
		controlsNewUIPage.clickOnControlsComplianceSection();
		controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(true);
		controlsNewUIPage.editDailyOT("8 hours", "single work day", true);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
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
		createSchedulePage.clickCreateScheduleBtn();
		createSchedulePage.editOperatingHoursWithGivingPrameters("8:00PM", "6:00AM");
		createSchedulePage.clickNextBtnOnCreateScheduleWindow();
		createSchedulePage.selectWhichWeekToCopyFrom("SUGGESTED");
		createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();
		createSchedulePage.switchToManagerViewToCheckForSecondGenerate();

		//delete unassigned shifts.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Action Required");
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
		scheduleMainPage.clickOnFilterBtn();
		scheduleMainPage.selectShiftTypeFilterByText("Assigned");
		List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
		String firstNameOfTM = shiftInfo.get(0);
		String workRoleOfTM = shiftInfo.get(4);
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
		scheduleMainPage.saveSchedule();

		//add new shift and assign TM.
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		newShiftPage.clickOnDayViewAddNewShiftButton();
		newShiftPage.selectWorkRole(workRoleOfTM);
		newShiftPage.moveSliderAtCertainPoint("5",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
		newShiftPage.moveSliderAtCertainPoint("7",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
		newShiftPage.clickOnCreateOrNextBtn();
		newShiftPage.searchTeamMemberByName(firstNameOfTM);
		newShiftPage.clickOnOfferOrAssignBtn();
		scheduleMainPage.saveSchedule();

		//verify daily OT violation after saving.
		scheduleMainPage.clickOnOpenSearchBoxButton();
		scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
		List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
		SimpleUtils.assertOnFail("Daily OT violation is not showing!", !complianceMessage.contains("1.5 hrs daily overtime"), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify assign TM warning: If SM wants to schedule a TM from another location and schedule hasn’t been generated")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMMessageWhenScheduleTMFromAnotherLocationWhereScheduleNotBeenGeneratedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			// Set this setting "Can a manager add another locations' employee in schedule before the employee's home location has published the schedule?" to "No, home location must publish schedule first"
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsPage.gotoControlsPage();
			SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
			controlsNewUIPage.updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule("No, home location must publish schedule first");

			// Change the location to "NY CENTRAL"
			dashboardPage.navigateToDashboard();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			String nyLocation = "NY CENTRAL (Previously New York Central Park)";
			locationSelectorPage.changeLocation(nyLocation);
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			// Select one team member to view profile
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			String userName = teamPage.searchAndSelectTeamMemberByName("Active");
			String firstName = userName.contains(" ") ? userName.split(" ")[0] : userName;
			String lastName = userName.contains(" ") ? userName.split(" ")[1] : userName;

			// Go to schedule page, schedule tab

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			// Navigate to a week
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();

			// Ungenerate the schedule if it has generated
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}

			// Select AUSTIN DOWNTOWN location
			dashboardPage.navigateToDashboard();
			locationSelectorPage.changeLocation("AUSTIN DOWNTOWN");
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			// Go to Schedule page, Schedule tab
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			// Navigate to a week
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();

			// Create the schedule if it is not created
			boolean isWeekGenerated2 = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated2){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}

			// Edit the Schedule
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			String workRole = shiftOperatePage.getRandomWorkRole();

			// Create new shift for TM
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();

			// Search TM and verify the message
			newShiftPage.searchText(firstName + " " + lastName.substring(0,1));
			if (shiftOperatePage.getTheMessageOfTMScheduledStatus().equalsIgnoreCase("Schedule Not Created"))
				SimpleUtils.pass("TM scheduled status message display correctly");
			else
				SimpleUtils.fail("TM scheduled status message failed to display or displays incorrectly",true);

			// Select the team member and verify the pop-up warning message
			newShiftPage.searchTeamMemberByName(firstName + " " + lastName.substring(0,1));
			String expectedMessage = firstName + " cannot be assigned because the schedule has not been published yet at the home location, " + nyLocation;
			shiftOperatePage.verifyAlertMessageIsExpected(expectedMessage);

			// Click on OK button and verify that TM is not selected
			scheduleShiftTablePage.clickOnOkButtonInWarningMode();
			shiftOperatePage.verifyTMNotSelected();
			newShiftPage.closeCustomizeNewShiftWindow();
			scheduleMainPage.saveSchedule();

			// Revert the setting
			controlsPage.gotoControlsPage();
			SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
			controlsNewUIPage.updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule("Yes, anytime");
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the buffer hours display in schedule")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheBufferHoursDisplayInScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();

			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			if (!newShiftPage.checkIfNewCreateShiftPageDisplay()) {
				List<String> allOperatingHrsOnCreateShiftPage = newShiftPage.getAllOperatingHrsOnCreateShiftPage();
				SimpleUtils.assertOnFail("The operating hours on create shift page display incorrectly! ",
						allOperatingHrsOnCreateShiftPage.get(0).equalsIgnoreCase("6am")
								&& allOperatingHrsOnCreateShiftPage.get(allOperatingHrsOnCreateShiftPage.size()-1).equalsIgnoreCase("11pm"),false);
			}
			newShiftPage.closeCustomizeNewShiftWindow();

			//Profile option been disabled
//			shiftOperatePage.clickOnProfileIcon();
//			shiftOperatePage.clickOnEditShiftTime();
//			Thread.sleep(5000);
//			shiftOperatePage.verifyEditShiftTimePopUpDisplay();
//			if (!shiftOperatePage.isEditShiftTimeNewUIDisplay()) {
//				List<String> startAndEndHrsOnEditShiftPage = shiftOperatePage.getStartAndEndOperatingHrsOnEditShiftPage();
//				SimpleUtils.assertOnFail("The operating hours on create shift page display incorrectly! ",
//						startAndEndHrsOnEditShiftPage.get(0).equalsIgnoreCase("6")
//								&& startAndEndHrsOnEditShiftPage.get(1).equalsIgnoreCase("11"),false);
//			}
//			shiftOperatePage.clickOnCancelEditShiftTimeButton();
//			Thread.sleep(5000);
//			scheduleMainPage.clickOnCancelButtonOnEditMode();

			scheduleCommonPage.clickOnDayView();
			List<String> gridHeaderTimes = new ArrayList();
			gridHeaderTimes = scheduleShiftTablePage.getScheduleDayViewGridTimeDuration();
			SimpleUtils.assertOnFail("The grid header time should start as 6 AM, the actual time is: " +
					gridHeaderTimes.get(0), gridHeaderTimes.get(0).contains("6 AM"), false);
			SimpleUtils.assertOnFail("The grid header time should end with 10 PM, the actual time is: " +
					gridHeaderTimes.get(gridHeaderTimes.size() - 1), gridHeaderTimes.get(gridHeaderTimes.size() - 1).contains("10 PM"), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	//@Enterprise(name = "KendraScott2_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate search bar on schedule page in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySearchBarOnSchedulePageInDayViewAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
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

		//Go to day view
		scheduleCommonPage.clickOnDayView();

		//click search button
		scheduleMainPage.clickOnOpenSearchBoxButton();

		//Check the ghost text inside the Search bar
		scheduleMainPage.verifyGhostTextInSearchBox();

		//Get the info of a random shift
		List<String> shiftInfo = new ArrayList<>();
		String firstNameOfTM = "";
		int i = 0;
		while (i < 50 && (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
				|| firstNameOfTM.equalsIgnoreCase("Unassigned"))) {
			shiftInfo = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			//Search shift by TM names: first name and last name
			firstNameOfTM = shiftInfo.get(0);
			i++;
		}
		List<WebElement> searchResultOfFirstName = scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
		scheduleShiftTablePage.verifySearchResult(firstNameOfTM, null, null, null, searchResultOfFirstName);

		String lastNameOfTM = shiftInfo.get(5);
		List<WebElement> searchResultOfLastName = scheduleMainPage.searchShiftOnSchedulePage(lastNameOfTM);
		scheduleShiftTablePage.verifySearchResult(null, lastNameOfTM, null, null, searchResultOfLastName);

		//Search shift by work role
		String workRole = shiftInfo.get(4);
		List<WebElement> searchResultOfWorkRole = scheduleMainPage.searchShiftOnSchedulePage(workRole);
		scheduleShiftTablePage.verifySearchResult(null, null, workRole, null, searchResultOfWorkRole);

		//Search shift by job title
		String jobTitle = shiftInfo.get(3);
		List<WebElement> searchResultOfJobTitle = scheduleMainPage.searchShiftOnSchedulePage(jobTitle);
		scheduleShiftTablePage.verifySearchResult(null, null, null, jobTitle, searchResultOfJobTitle);
		Thread.sleep(5000);
		//Click X button to close search box
		scheduleMainPage.clickOnCloseSearchBoxButton();

		//Go to edit mode
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

		//click search button
		scheduleMainPage.clickOnOpenSearchBoxButton();
		//Click X button to close search box
		scheduleMainPage.clickOnCloseSearchBoxButton();
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate the first name and last name are all display on Search TM , Recommended TMs and View profile page")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTMFullNameDisplayOnSearchTMRecommendedAndViewProfilePageAsInternalAdmin(String username, String password, String browser, String location) {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.clickOnControlsConsoleMenu();
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			controlsNewUIPage.clickOnGlobalLocationButton();
			OpsPortalConfigurationPage opsPortalConfigurationPage = (OpsPortalConfigurationPage) pageFactory.createOpsPortalConfigurationPage();
			opsPortalConfigurationPage.setWFS("No");
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.publishNowTheTemplate();
			Thread.sleep(3000);
			switchToConsoleWindow();

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();

			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//Select new TM from Search Team Member tab
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(controlWorkRole);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			String nameOfSelectedTM = newShiftPage.selectTeamMembers();
			newShiftPage.clickOnOfferOrAssignBtn();

			//Select new TM from Recommended TMs tab
			String nameOfSelectedTM2 = "";
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(controlWorkRole);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			nameOfSelectedTM2 = newShiftPage.selectTeamMembers();
			newShiftPage.clickOnOfferOrAssignBtn();

			//Get TM full name from view profile page
			List<String> shiftInfo = new ArrayList<>();
			String nameOfSelectedTM3 = "";
			int i = 0;
			while (i < 50 && (nameOfSelectedTM3.equals("") || nameOfSelectedTM3.equalsIgnoreCase("Open")
					|| nameOfSelectedTM3.equalsIgnoreCase("Unassigned"))) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				//Search shift by TM names: first name and last name
				nameOfSelectedTM3 = shiftInfo.get(0);
				i++;
			}
			nameOfSelectedTM3 = shiftInfo.get(0) +" " + shiftInfo.get(5);
			scheduleMainPage.saveSchedule();
			Thread.sleep(3000);
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();

			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(nameOfSelectedTM);
			String nameOnProfilePage = profileNewUIPage.getUserProfileName().get("fullName");
			SimpleUtils.assertOnFail("Name on profile page display incorrectly! The expected is: "+ nameOfSelectedTM +
					", The actual is: " + nameOnProfilePage, nameOfSelectedTM.equalsIgnoreCase(nameOnProfilePage), false);

			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(nameOfSelectedTM2);
			nameOnProfilePage = profileNewUIPage.getUserProfileName().get("fullName");
			SimpleUtils.assertOnFail("Name on profile page display incorrectly! The expected is: "+ nameOfSelectedTM2 +
					", The actual is: " + nameOnProfilePage, nameOfSelectedTM2.equalsIgnoreCase(nameOnProfilePage), false);

			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(nameOfSelectedTM3);
			nameOnProfilePage = profileNewUIPage.getUserProfileName().get("fullName");
			SimpleUtils.assertOnFail("Name on profile page display incorrectly! The expected is: "+ nameOfSelectedTM3 +
					", The actual is: " + nameOnProfilePage, nameOfSelectedTM3.equalsIgnoreCase(nameOnProfilePage), false);

			controlsNewUIPage.clickOnControlsConsoleMenu();
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			controlsNewUIPage.clickOnGlobalLocationButton();
			opsPortalConfigurationPage.setWFS("Yes");
		} catch (Exception e) {
		SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify Action Required smart card display correctly when the schedule created by copy partial")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyActionRequiredSmartCardDisplayCorrectlyWhenScheduleCreatedByCopyPartialAsInternalAdmin(String browser, String username, String password, String location) throws Exception
	{
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab("Schedule");
		scheduleCommonPage.navigateToNextWeek();
		if (createSchedulePage.isWeekGenerated()){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "05:00PM");
		shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
		createSchedulePage.publishActiveSchedule();

		String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
		if (firstWeekInfo.length() > 11) {
			firstWeekInfo = firstWeekInfo.trim().substring(10);
		}
		scheduleCommonPage.navigateToNextWeek();
		if (createSchedulePage.isWeekGenerated()){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		createSchedulePage.clickCreateScheduleBtn();
		createSchedulePage.editOperatingHoursWithGivingPrameters("Sunday", "11:00AM", "05:00PM");
		createSchedulePage.editOperatingHoursWithGivingPrameters("Monday", "11:00AM", "05:00PM");
		createSchedulePage.editOperatingHoursWithGivingPrameters("Tuesday", "11:00AM", "05:00PM");
		createSchedulePage.editOperatingHoursWithGivingPrameters("Wednesday", "11:00AM", "05:00PM");
		createSchedulePage.editOperatingHoursWithGivingPrameters("Thursday", "11:00AM", "05:00PM");
		createSchedulePage.editOperatingHoursWithGivingPrameters("Friday", "11:00AM", "05:00PM");
		createSchedulePage.editOperatingHoursWithGivingPrameters("Saturday", "11:00AM", "05:00PM");
		createSchedulePage.clickNextBtnOnCreateScheduleWindow();
		createSchedulePage.selectWhichWeekToCopyFrom(firstWeekInfo);
		createSchedulePage.copyAllPartialSchedule();
		createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();

		//Check the Action required smart card is display
		SimpleUtils.assertOnFail("Action Required smart card should be loaded! ",
				smartCardPage.isRequiredActionSmartCardLoaded(), false);
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the employee name on shift in day and week view when enable ScheduleShowFullNames toggle")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheFullNamesOnShiftInDayAndWeekViewWhenEnableScheduleShowFullNamesToggleAsTeamMember(String username, String password, String browser, String location) throws Exception {
		try {
			ToggleAPI.updateToggle(Toggles.ScheduleShowFullNames.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), true);
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			//Login as admin
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			//Enable ScheduleShowFullNames toggle
			refreshCachesAfterChangeToggleOrABSwitch();
			Thread.sleep(3000);
			loginPage.logOut();
			//Login as admin
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			//Go to one schedule page week view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			//Delete all unassigned shifts and tm's shifts
			shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmFullName.split(" ")[0]);
			scheduleMainPage.saveSchedule();

			//Add shifts for TM
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("4pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmFullName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(tmFullName);

			//Get employee full name in week view
			String fullNameInWeekView = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in week view, the expected full name is: "+ tmFullName +" The actual full name is: " + fullNameInWeekView,
					fullNameInWeekView.equalsIgnoreCase(tmFullName), false);

			//Get employee full name in day view
			scheduleCommonPage.clickOnDayView();
			scheduleCommonPage.navigateDayViewWithIndex(0);
		    String fullNameInDayView = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in day view, the expected full name is: "+ tmFullName +" The actual full name is: " + fullNameInDayView,
					fullNameInDayView.equalsIgnoreCase(tmFullName), false);
		    loginPage.logOut();
		    //Login as TM
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			//Get employee full name on TM schedule view
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			String fullNameInMySchedulePage = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in my schedule page, the expected full name is: "+ tmFullName +" The actual full name is: " + fullNameInMySchedulePage,
					fullNameInMySchedulePage.equalsIgnoreCase(tmFullName), false);

			//Try to create swap request for one shift and check the shift on swap page
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			mySchedulePage.verifyClickOnAnyShift();
			String request = "Request to Swap Shift";
			String title = "Find Shifts to Swap";
			mySchedulePage.clickTheShiftRequestByName(request);
			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
			String fullNameInFindShiftsToSwapPage = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in swap page, the expected full name is: "+ tmFullName +" The actual full name is: " + fullNameInFindShiftsToSwapPage,
					fullNameInFindShiftsToSwapPage.equalsIgnoreCase(tmFullName), false);

			//Try to create cover request for one shift and check the shift on cover page
			mySchedulePage.clickCloseDialogButton();
			Thread.sleep(3000);
			mySchedulePage.verifyClickOnAnyShift();
			request = "Request to Cover Shift";
			title = "Submit Cover Request";
			mySchedulePage.clickTheShiftRequestByName(request);
			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
			String fullNameInFindShiftsToCoverPage = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in cover page, the expected full name is: "+ tmFullName +" The actual full name is: " + fullNameInFindShiftsToCoverPage,
					fullNameInFindShiftsToCoverPage.equalsIgnoreCase(tmFullName), false);
			mySchedulePage.clickCloseDialogButton();

			String subTitle = "Team Schedule";
			mySchedulePage.gotoScheduleSubTabByText(subTitle);
			int indexInWeekView = scheduleShiftTablePage.getAddedShiftIndexes(tmFullName.split(" ")[0]).get(0);
			//Get employee full name in week
			fullNameInWeekView = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(indexInWeekView);
			SimpleUtils.assertOnFail("The full name display incorrectly in team schedule page, the expected full name is: "+ tmFullName +" The actual full name is: " + fullNameInWeekView,
					fullNameInWeekView.equalsIgnoreCase(tmFullName), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the employee name on shift in day and week view when disable ScheduleShowFullNames toggle")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheFullNamesOnShiftInDayAndWeekViewWhenDisableScheduleShowFullNamesToggleAsTeamMember(String username, String password, String browser, String location) throws Exception {
		try {
			ToggleAPI.updateToggle(Toggles.ScheduleShowFullNames.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), false);
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstAndInitialSecondName = tmFullName.split(" ")[0] + " " + tmFullName.split(" ")[1].substring(0, 1) + ".";
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			//Login as admin
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			//Enable ScheduleShowFullNames toggle
			refreshCachesAfterChangeToggleOrABSwitch();
			Thread.sleep(3000);
			loginPage.logOut();
			//Login as admin
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			//Enable ScheduleShowFullNames toggle
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Go to one schedule page week view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}

			//Delete all unassigned shifts and tm's shifts
			shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmFullName.split(" ")[0]);
			scheduleMainPage.saveSchedule();

			//Add shifts for TM
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			Thread.sleep(3000);
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(0,0,1);
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("4pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmFullName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			Thread.sleep(5000);
			createSchedulePage.publishActiveSchedule();
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(tmFullName);

			//Get employee full name in week view
			String fullNameInWeekView = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in week view, the expected full name is: "+ firstAndInitialSecondName +" The actual full name is: " + fullNameInWeekView,
					fullNameInWeekView.equalsIgnoreCase(firstAndInitialSecondName), false);

			//Get employee full name in day view
			scheduleCommonPage.clickOnDayView();
			scheduleCommonPage.navigateDayViewWithIndex(0);
			String fullNameInDayView = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in day view, the expected full name is: "+ firstAndInitialSecondName +" The actual full name is: " + fullNameInDayView,
					fullNameInDayView.equalsIgnoreCase(firstAndInitialSecondName), false);
			loginPage.logOut();
			//Login as TM
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			//Get employee full name on TM schedule view
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.navigateToNextWeek();
			String fullNameInMySchedulePage = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in my schedule page, the expected full name is: "+ firstAndInitialSecondName +" The actual full name is: " + fullNameInMySchedulePage,
					fullNameInMySchedulePage.equalsIgnoreCase(firstAndInitialSecondName), false);

			//Try to create swap request for one shift and check the shift on swap page
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			mySchedulePage.verifyClickOnAnyShift();
			String request = "Request to Swap Shift";
			String title = "Find Shifts to Swap";
			mySchedulePage.clickTheShiftRequestByName(request);
			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
			String fullNameInFindShiftsToSwapPage = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in swap page, the expected full name is: "+ firstAndInitialSecondName +" The actual full name is: " + fullNameInFindShiftsToSwapPage,
					fullNameInFindShiftsToSwapPage.equalsIgnoreCase(firstAndInitialSecondName), false);

			//Try to create cover request for one shift and check the shift on cover page
			mySchedulePage.clickCloseDialogButton();
			Thread.sleep(3000);
			mySchedulePage.verifyClickOnAnyShift();
			request = "Request to Cover Shift";
			title = "Submit Cover Request";
			mySchedulePage.clickTheShiftRequestByName(request);
			SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
			String fullNameInFindShiftsToCoverPage = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(0);
			SimpleUtils.assertOnFail("The full name display incorrectly in cover page, the expected full name is: "+ firstAndInitialSecondName +" The actual full name is: " + fullNameInFindShiftsToCoverPage,
					fullNameInFindShiftsToCoverPage.equalsIgnoreCase(firstAndInitialSecondName), false);
			mySchedulePage.clickCloseDialogButton();

			String subTitle = "Team Schedule";
			mySchedulePage.gotoScheduleSubTabByText(subTitle);
			int indexInWeekView = scheduleShiftTablePage.getAddedShiftIndexes(tmFullName.split(" ")[0]).get(0);
			//Get employee full name in week
			fullNameInWeekView = scheduleShiftTablePage.getFullNameOfOneShiftByIndex(indexInWeekView);
			SimpleUtils.assertOnFail("The full name display incorrectly in team schedule page, the expected full name is: "+ firstAndInitialSecondName +" The actual full name is: " + fullNameInWeekView,
					fullNameInWeekView.equalsIgnoreCase(firstAndInitialSecondName), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Owner(owner = "Nora")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify whole day PTO requests should be recognized in schedule")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyPTORequestInScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
		try {
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();

			int advancedDays = 2;
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			// Login as Team Member to create time off
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			String requestUserName = profileNewUIPage.getNickNameFromProfile();
			String myTimeOffLabel = "My Time Off";
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myTimeOffLabel);
			profileNewUIPage.cancelAllTimeOff();
			profileNewUIPage.clickOnCreateTimeOffBtn();
			SimpleUtils.assertOnFail("New time off request window not loaded Successfully!", profileNewUIPage.isNewTimeOffWindowLoaded(), false);
			// select time off reason
			if (profileNewUIPage.isReasonLoad(ActivityTest.timeOffReasonType.FamilyEmergency.getValue())){
				profileNewUIPage.selectTimeOffReason(ActivityTest.timeOffReasonType.FamilyEmergency.getValue());
			} else if (profileNewUIPage.isReasonLoad(ActivityTest.timeOffReasonType.PersonalEmergency.getValue())){
				profileNewUIPage.selectTimeOffReason(ActivityTest.timeOffReasonType.PersonalEmergency.getValue());
			} else if (profileNewUIPage.isReasonLoad(ActivityTest.timeOffReasonType.JuryDuty.getValue())){
				profileNewUIPage.selectTimeOffReason(ActivityTest.timeOffReasonType.JuryDuty.getValue());
			} else if (profileNewUIPage.isReasonLoad(ActivityTest.timeOffReasonType.Sick.getValue())){
				profileNewUIPage.selectTimeOffReason(ActivityTest.timeOffReasonType.Sick.getValue());
			} else if (profileNewUIPage.isReasonLoad(ActivityTest.timeOffReasonType.Vacation.getValue())){
				profileNewUIPage.selectTimeOffReason(ActivityTest.timeOffReasonType.Vacation.getValue());
			}
			List<String> timeOffDates = profileNewUIPage.selectStartAndEndDate(advancedDays, 7, 7);
			profileNewUIPage.clickOnSaveTimeOffRequestBtn();
			loginPage.logOut();

			// Login as Store Manager again to check message and approve
			String RequestTimeOff = "requested";
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			String respondUserName = profileNewUIPage.getNickNameFromProfile();
			ActivityPage activityPage = pageFactory.createConsoleActivityPage();
			activityPage.verifyClickOnActivityIcon();
			activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.TimeOff.getValue(), ActivityTest.indexOfActivityType.TimeOff.name());
			activityPage.verifyTheNotificationForReqestTimeOff(requestUserName, getTimeOffStartTime(),getTimeOffEndTime(), RequestTimeOff);
			activityPage.approveOrRejectTTimeOffRequestOnActivity(requestUserName,respondUserName, ActivityTest.approveRejectAction.Approve.getValue());
			activityPage.closeActivityWindow();

			// Go to Schedule page, Schedule tab

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

			// Navigate to the week that contains the date that provided
			scheduleCommonPage.goToSpecificWeekByDate(timeOffDates.get(0));

			// Create schedule if it is not created
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleShiftTablePage.getWeekDayAndDate();

			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnOpenSearchBoxButton();
			List<WebElement> shifts = scheduleMainPage.searchShiftOnSchedulePage(requestUserName);

			if (shifts == null || (shifts != null && shifts.size() == 0)) {
				// Edit schedule to create the new shift for new TM
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				newShiftPage.clickOnDayViewAddNewShiftButton();
				newShiftPage.customizeNewShiftPage();
				newShiftPage.clearAllSelectedDays();
				newShiftPage.selectDaysByCountAndCannotSelectedDate(1, timeOffDates.get(0));
				newShiftPage.selectWorkRole(workRole);
				newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
				newShiftPage.clickOnCreateOrNextBtn();
				newShiftPage.searchTeamMemberByName(requestUserName);
				newShiftPage.clickOnOfferOrAssignBtn();
				scheduleMainPage.saveSchedule();
				scheduleMainPage.searchShiftOnSchedulePage(requestUserName);
			}
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyTM.getValue());
			// Verify Time Off card will show when group by TM
			int index = scheduleShiftTablePage.getTheIndexOfTheDayInWeekView(timeOffDates.get(0).substring(timeOffDates.get(0).length() - 2));
			scheduleShiftTablePage.verifyTimeOffCardShowInCorrectDay(index);

			// Clear the time off request
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName(requestUserName);
			SimpleUtils.assertOnFail("Profile page failed to load!", teamPage.isProfilePageLoaded(), false);
			profileNewUIPage.selectProfilePageSubSectionByLabel("Time Off");
			profileNewUIPage.rejectAllTimeOff();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}
	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Group by All filter in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByAllFilterInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			scheduleCommonPage.clickOnDayView();
			//In day view, Group by All filter have 4 filters:1.Group by all  2. Group by work role  3. Group by TM 4.Group by job title 5. Group by Day Parts
			scheduleMainPage.validateGroupBySelectorSchedulePage(false);
			//Group by work role and check the results.
			scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
			scheduleShiftTablePage.verifyGroupByTitlesOrder();
			//Group by job title and check the results.
			scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
			scheduleShiftTablePage.verifyGroupByTitlesOrder();
			//Group by TM and check the results.
			List<WebElement> shiftsInDayView = scheduleShiftTablePage.getAvailableShiftsInDayView();
			scheduleMainPage.selectGroupByFilter(scheduleGroupByFilterOptions.groupbyTM.getValue());
			SimpleUtils.assertOnFail("The shifts display incorrectly when group by TM in day view! ",
					shiftsInDayView.containsAll(scheduleShiftTablePage.getAvailableShiftsInDayView()), false);
			//Group by day parts and check the group.
			scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyDayParts.getValue());
			scheduleShiftTablePage.verifyGroupByTitlesOrder();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the View Profile functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyViewProfileInContextMenuInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage= pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleCommonPage.clickOnDayView();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//			SimpleUtils.assertOnFail(" context of any TM display doesn't show well" , shiftOperatePage.verifyContextOfTMDisplay(), false);

			//"After Click on view profile,then particular TM profile is displayed :1. Personal details 2. Work Preferences 3. Availability
			scheduleShiftTablePage.clickOnProfileIconOfShiftInDayView("no");
			shiftOperatePage.verifyPersonalDetailsDisplayed();
			shiftOperatePage.verifyWorkPreferenceDisplayed();
			shiftOperatePage.verifyAvailabilityDisplayed();
			shiftOperatePage.closeViewProfileContainer();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Change Shift Role functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyChangeShiftRoleInContextMenuInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SimpleUtils.assertOnFail(" context of any TM display doesn't show well" , shiftOperatePage.verifyContextOfTMDisplay(), false);

//			"After Click on view profile,then particular TM profile is displayed :1. Personal details 2. Work Preferences 3. Availability
			shiftOperatePage.clickOnChangeRole();
			shiftOperatePage.verifyChangeRoleFunctionality();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			//check the work role by click Apply button
			shiftOperatePage.changeWorkRoleInPrompt(true);
			//check the work role by click Cancel button
			shiftOperatePage.changeWorkRoleInPrompt(false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Assign Team Member functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAssignTMInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
			scheduleMainPage.saveSchedule();
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			SimpleUtils.assertOnFail(" context of any TM display doesn't show well" , shiftOperatePage.verifyContextOfTMDisplay(), false);

			shiftOperatePage.clickonAssignTM();
			shiftOperatePage.verifyRecommendedAndSearchTMEnabled();

			//Search and select any TM,Click on the assign: new Tm is updated on the schedule table
			//Select new TM from Search Team Member tab
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.selectWorkRoleFilterByText(workRole, true);
			WebElement selectedShift = null;
			selectedShift = shiftOperatePage.clickOnProfileIcon();
			String selectedShiftId= selectedShift.getAttribute("id").toString();
			shiftOperatePage.clickonAssignTM();
			String firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
			newShiftPage.clickOnOfferOrAssignBtn();
			SimpleUtils.assertOnFail(" New selected TM doesn't display in scheduled table" ,
					firstNameOfSelectedTM.equals(scheduleShiftTablePage.getShiftById(selectedShiftId).findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0].trim()), false);
			//Select new TM from Recommended TMs tab
			String firstNameOfSelectedTM2 = "";
			String selectedShiftId2 = "";
			int i = 0;
			while (firstNameOfSelectedTM2.equals("") && i<10) {
				selectedShift = shiftOperatePage.clickOnProfileIcon();
				selectedShiftId2  = selectedShift.getAttribute("id").toString();
				shiftOperatePage.clickonAssignTM();
				shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
				firstNameOfSelectedTM2 = newShiftPage.selectTeamMembers().split(" ")[0];
				i++;
			}
			if (firstNameOfSelectedTM2.equals("")) {
				SimpleUtils.fail("Cannot found TMs in recommended TMs tab! ", false);
			}
			newShiftPage.clickOnOfferOrAssignBtn();
			SimpleUtils.assertOnFail(" New selected TM doesn't display in scheduled table" ,
					firstNameOfSelectedTM2.equals(scheduleShiftTablePage.getShiftById(selectedShiftId2).findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0].trim()), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Convert to Open Shift functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyConvertToOpenShiftInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
			scheduleMainPage.saveSchedule();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//Click on the Convert to open shift, checkbox is available to offer the shift to any specific TM[optional] Cancel /yes
			//if checkbox is unselected then, shift is convert to open
			WebElement selectedShift = shiftOperatePage.clickOnProfileIcon();
			String tmFirstName = selectedShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0];
			int i = 0;
			while (i<10 && (tmFirstName.equalsIgnoreCase("open")||tmFirstName.equalsIgnoreCase("unassigned"))) {
				selectedShift = shiftOperatePage.clickOnProfileIcon();
				tmFirstName = selectedShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0];
				i++;
			}
			shiftOperatePage.clickOnConvertToOpenShift();
			if (shiftOperatePage.verifyConvertToOpenPopUpDisplay(tmFirstName)) {
				shiftOperatePage.convertToOpenShiftDirectly();
			}
			//if checkbox is select then select team member page will display
			selectedShift = shiftOperatePage.clickOnProfileIcon();
			tmFirstName = selectedShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0];
			i = 0;
			while (i<20 && (tmFirstName.equalsIgnoreCase("open")||tmFirstName.equalsIgnoreCase("unassigned"))) {
				selectedShift = shiftOperatePage.clickOnProfileIcon();
				tmFirstName = selectedShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0];
				i++;
			}
			shiftOperatePage.clickOnConvertToOpenShift();
			if (shiftOperatePage.verifyConvertToOpenPopUpDisplay(tmFirstName)) {
				shiftOperatePage.convertToOpenShiftAndOfferToSpecificTMs();
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Edit Shift Notes functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditShiftNotesInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//Click on the Convert to open shift, checkbox is available to offer the shift to any specific TM[optional] Cancel /yes
			//if checkbox is unselected then, shift is convert to open
			WebElement selectedShift = shiftOperatePage.clickOnProfileIcon();
			int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShift);
			shiftOperatePage.clickOnEditShiftNotesOption();
			//add shift notes.
			String shiftNote = "new shift notes";
			shiftOperatePage.addShiftNotesToTextarea(shiftNote);
			scheduleMainPage.saveSchedule();
			selectedShift = scheduleShiftTablePage.getTheShiftByIndex(index);
			SimpleUtils.assertOnFail("The new shift notes fail to display! ",
					scheduleShiftTablePage.getIIconTextInfo(selectedShift).contains(shiftNote), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Edit Shift Time functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditShiftTimeInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//After click on Edit Shift Time, the Edit Shift window will display
			int index = scheduleShiftTablePage.getAvailableShiftsInDayView().size()-1;
			WebElement shift = scheduleShiftTablePage.getTheShiftByIndex(index);
			scheduleShiftTablePage.clickOnShiftInDayView(scheduleShiftTablePage.getAvailableShiftsInDayView().get(index));
			String id = shift.getAttribute("id");
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);

			//The Edit Shift window is opened and check the content
			shiftOperatePage.clickOnEditShiftTime();
			shiftOperatePage.verifyEditShiftTimePopUpDisplay();
			shiftOperatePage.clickOnCancelEditShiftTimeButton();

			//Get shift position before edit
			String[] style = shift.getAttribute("style").split(";");
			String shiftLengthBeforeEdit = style[style.length-1].split(":")[1].replace("%", "").trim();
			shiftOperatePage.editTheShiftTimeForSpecificShift(shift, "6am", "5pm");

			//Get shift position after edit
			Thread.sleep(2000);
			style = scheduleShiftTablePage.getShiftById(id).getAttribute("style").split(";");
			String shiftLengthAfterEdit = style[style.length-1].split(":")[1].replace("%", "").trim();
			SimpleUtils.assertOnFail("The shift container length should be changed! ",
					!shiftLengthAfterEdit.equals(shiftLengthBeforeEdit), false);
			List<String> shiftTimes = scheduleShiftTablePage.getShiftTimeInDayViewPopUp();
			shiftTimes.get(0).equalsIgnoreCase("6:00 am");
			shiftTimes.get(1).equalsIgnoreCase("5:00 pm");
			String shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(scheduleShiftTablePage.getShiftIndexById(id));
			SimpleUtils.assertOnFail("The shift time display incorrectly in shift container in day view! ",
					shiftInfo.contains("6:00am-5:00pm"), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}



	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Edit Meal Break Time functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditMealBreakTimeInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//Verify Edit/View Meal Break
			if (shiftOperatePage.isEditMealBreakEnabled()){
				//After click on Edit Meal Break Time, the Edit Meal Break window will display
				shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(true);
				//Verify Delete Meal Break
				shiftOperatePage.verifyDeleteMealBreakFunctionality();
				//Edit meal break time and click update button
				shiftOperatePage.verifyEditMealBreakTimeFunctionality(true);
				//Edit meal break time and click cancel button
				shiftOperatePage.verifyEditMealBreakTimeFunctionality(false);
			} else
				shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the Delete Shift functionality in Context Menu in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyDeleteShiftInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//verify cancel button
			shiftOperatePage.verifyDeleteShiftCancelButton();

			//verify delete shift
			shiftOperatePage.verifyDeleteShift();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the items after click the shift in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheItemsAfterClickTheShiftInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//Select one assigned shift and click shift, the delete button and two shift time popups will display after click one shift
			int index = scheduleShiftTablePage.getRandomIndexOfShift();
			scheduleShiftTablePage.clickOnShiftInDayView(scheduleShiftTablePage.
					getAvailableShiftsInDayView().get(index));

			//the shift start time and end time are display correctly in the popup
			String shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index).replace("\n", " ");
			List<String> shiftTimeInDayViewPopUp = scheduleShiftTablePage.getShiftTimeInDayViewPopUp();
			SimpleUtils.assertOnFail("The shift start time and end time are display incorrect in the popup",
					shiftInfo.contains(shiftTimeInDayViewPopUp.get(0))
							&&shiftInfo.contains(shiftTimeInDayViewPopUp.get(1)), false);
			//Delete the shift by click the X button
			HashSet<Integer> needDelete = new HashSet<>();
			needDelete.add(index);
			scheduleShiftTablePage.rightClickOnSelectedShifts(needDelete);
			String option = "Delete";
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(option);
			SimpleUtils.assertOnFail("The shift should been marked as deleted! ",
					scheduleShiftTablePage.checkIfShiftInDayViewBeenMarkAsDeletedByIndex(index), false);;

			//Click the delete img in the end of the shift row, the shift will go back
			scheduleShiftTablePage.clickTheDeleteImgForSpecifyShiftByIndex(index);

			//Delete the shift by click the X button and save the schedule, the shift can be deleted
			int shiftCountBeforeDelete = scheduleShiftTablePage.getAvailableShiftsInDayView().size();
			scheduleShiftTablePage.clickOnShiftInDayView(scheduleShiftTablePage.
					getAvailableShiftsInDayView().get(index));
			scheduleShiftTablePage.rightClickOnSelectedShifts(needDelete);
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(option);
			scheduleMainPage.saveSchedule();
			SimpleUtils.assertOnFail("The shift not been deleted successfully! ",
					scheduleShiftTablePage.getAvailableShiftsInDayView().size()+1 == shiftCountBeforeDelete, false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify shifts can be dragged in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyShiftsCanBeDraggedInDayViewAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isActiveWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

			//Drag one shift in day view and check the shift time, save the schedule
			int index = scheduleShiftTablePage.getAvailableShiftsInDayView().size()-1;
			String shiftInfoBeforeEdit = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index).replace("\n", " ");
			String id = scheduleShiftTablePage.getTheShiftByIndex(index).getAttribute("id");
			scheduleShiftTablePage.moveShiftByIndexInDayView(index, true);
			scheduleMainPage.saveSchedule();

			index = scheduleShiftTablePage.getShiftIndexById(id);
			String shiftInfoAfterEdit = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index).replace("\n", " ");
			SimpleUtils.assertOnFail("The shift info should be changed after change! ",
					!shiftInfoBeforeEdit.equals(shiftInfoAfterEdit), false);

			//Drag one shift in day view, the edit shift img in the end of the row will display
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			index = scheduleShiftTablePage.getAvailableShiftsInDayView().size()-1;
			id = scheduleShiftTablePage.getTheShiftByIndex(index).getAttribute("id");
			shiftInfoBeforeEdit = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index).replace("\n", " ");
			scheduleShiftTablePage.moveShiftByIndexInDayView(index, true);
			scheduleShiftTablePage.verifyTheEditedImgDisplayForShiftInDayByIndex(index);

			//Click the edit img and change will go back
			scheduleShiftTablePage.clickOnEditedOrDeletedImgForShiftInDayViewByIndex(index);
			Thread.sleep(2000);
			shiftInfoAfterEdit = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index).replace("\n", " ");
			SimpleUtils.assertOnFail("The shift time should not been changed! ",
					shiftInfoAfterEdit.contains(shiftInfoBeforeEdit), false);

			//Save the schedule, the shift will not been edited
			scheduleMainPage.saveSchedule();
			shiftInfoAfterEdit  = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index).replace("\n", " ");
			SimpleUtils.assertOnFail("The shift time should not been changed! ",
					shiftInfoAfterEdit.contains(shiftInfoBeforeEdit), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the overnight shift can be drag to other day")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyOvernightShiftsCanBeDraggedToOtherDayAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");
			int i = 0;
			List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM1 = shiftInfo.get(0);
			while (i< 50 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM1  = shiftInfo.get(0);
				i++;
			}
			String workRole = shiftInfo.get(4);

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			scheduleMainPage.saveSchedule();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(2,2,2);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			scheduleCommonPage.clickOnDayView();
			scheduleCommonPage.navigateDayViewWithIndex(2);
			//Verify overnight shift can be created
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			int index = scheduleShiftTablePage.
					getTheIndexOfShift(scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).get(0));
			scheduleShiftTablePage.moveShiftByIndexInDayView(index, false);
			scheduleMainPage.saveSchedule();
			//Verify the overnight shift can display on next day
			scheduleCommonPage.navigateDayViewWithIndex(3);
			SimpleUtils.assertOnFail("The overnight shift also display on the next day! ",
					scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).size()>0, false);
			//Verify the overnight shift can be dragged to other days
			scheduleCommonPage.clickOnWeekView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.dragOneShiftToAnotherDay(2, firstNameOfTM1, 1);
			scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
			scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
			Thread.sleep(3000);
			scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(2,firstNameOfTM1,1);
			scheduleMainPage.saveSchedule();
			scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(2,firstNameOfTM1,1);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate ScheduleEditShiftTimeNew abswitch")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduleEditShiftTimeNewAbswitchAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			//Disable the ScheduleEditShiftTimeNew
			ToggleAPI.updateToggle(Toggles.ScheduleEditShiftTimeNew.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), false);
			refreshCachesAfterChangeToggleOrABSwitch();
			Thread.sleep(3000);
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			int i = 0;
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			shiftOperatePage.clickOnProfileIcon();
			shiftOperatePage.clickOnEditShiftTime();
			i =0;
			while (i<5 && shiftOperatePage.isEditShiftTimeNewUIDisplay()) {
				Thread.sleep(60000);
				shiftOperatePage.clickOnCancelEditShiftTimeButton();
				shiftOperatePage.clickOnProfileIcon();
				shiftOperatePage.clickOnEditShiftTime();
				i++;
			}
			SimpleUtils.assertOnFail("The new edit shift time page should not display! ",
					!shiftOperatePage.isEditShiftTimeNewUIDisplay(), false);
			shiftOperatePage.clickOnCancelEditShiftTimeButton();
			ToggleAPI.updateToggle(Toggles.ScheduleEditShiftTimeNew.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), true);
			refreshCachesAfterChangeToggleOrABSwitch();
			Thread.sleep(3000);
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			//Go to one schedule page day view
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			Thread.sleep(3000);
			isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.clickOnProfileIcon();
			shiftOperatePage.clickOnEditShiftTime();
			SimpleUtils.assertOnFail("The new edit shift time page should display! ",
					shiftOperatePage.isEditShiftTimeNewUIDisplay(), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		} finally{
			ToggleAPI.updateToggle(Toggles.ScheduleEditShiftTimeNew.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), true);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the shift times always consistent on edit shift time page")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheShiftTimesConsistentOnInputAndShiftCardAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			//Enable the ScheduleEditShiftTimeNew
			ToggleAPI.updateToggle(Toggles.ScheduleEditShiftTimeNew.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), true);
			refreshCachesAfterChangeToggleOrABSwitch();
			Thread.sleep(3000);
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			//Login as admin
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			//Go to one schedule page day view
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			WebElement shift = shiftOperatePage.clickOnProfileIcon();
			String id = shift.getAttribute("id");
			shiftOperatePage.clickOnEditShiftTime();
			Thread.sleep(5000);
			String shiftTime = "08:00am-09:00pm";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], true);
			HashMap<String, String> shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			String shiftTimeOnShiftCard = shiftInfo.get("shiftTime");
			SimpleUtils.assertOnFail("The shift times on inputs and shift card should be consistent, the time in inputs: "
					+ shiftTime + " the time on shift card: "+ shiftTimeOnShiftCard, "8:00am-9:00pm".equals(shiftTimeOnShiftCard.replace(" ","")), false);

			shiftTime = "08:45am-11:45am";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], false);
			shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			shiftTimeOnShiftCard = shiftInfo.get("shiftTime");
			SimpleUtils.assertOnFail("The shift times on inputs and shift card should be consistent, the time in inputs: "
					+ shiftTime + " the time on shift card: "+ shiftTimeOnShiftCard, "8:45am-11:45am".equals(shiftTimeOnShiftCard.replace(" ", "")), false);

			shiftTime = "01:00pm-10:00pm";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], true);
			shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			shiftTimeOnShiftCard = shiftInfo.get("shiftTime");
			SimpleUtils.assertOnFail("The shift times on inputs and shift card should be consistent, the time in inputs: "
					+ shiftTime + " the time on shift card: "+ shiftTimeOnShiftCard, "1:00pm-10:00pm".equals(shiftTimeOnShiftCard.replace(" ","")), false);

			shiftTime = "10:00pm-06:00am";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], false);
			shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			shiftTimeOnShiftCard = shiftInfo.get("shiftTime");
			SimpleUtils.assertOnFail("The shift times on inputs and shift card should be consistent, the time in inputs: "
					+ shiftTime + " the time on shift card: "+ shiftTimeOnShiftCard, "10:00pm-6:00am".equals(shiftTimeOnShiftCard.replace(" ", "")), false);

			shiftTime = "11:00am-02:00pm";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], false);
			shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			shiftTimeOnShiftCard = shiftInfo.get("shiftTime");
			SimpleUtils.assertOnFail("The shift times on inputs and shift card should consistent, the time in inputs: "
					+ shiftTime + " the time on shift card: "+ shiftTimeOnShiftCard, "11:00am-2:00pm".equals(shiftTimeOnShiftCard.replace(" ", "")), false);
			Thread.sleep(5000);
			shiftOperatePage.clickOnUpdateEditShiftTimeButton();
			String shiftInfoOnIIcon = scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getShiftById(id)).replace(" ", "");
			SimpleUtils.assertOnFail("The shift times on edit shift page and i icon should consistent, the time on edit shift time page: "
							+shiftTime + " the time on i icon: "+ shiftInfoOnIIcon,
					shiftInfoOnIIcon.contains("11:00am-2:00pm") , false);
			scheduleMainPage.saveSchedule();
			shiftInfoOnIIcon = scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getShiftById(id)).replace(" ", "");
			SimpleUtils.assertOnFail("The shift times on edit shift page and i icon should consistent, the time on edit shift time page: "
							+shiftTime + " the time on i icon: "+ shiftInfoOnIIcon,
					shiftInfoOnIIcon.contains("11:00am-2:00pm") , false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the shift times can be set more than 24 hrs by shift time input")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheShiftTimeCanBeSetMoreThan24HrsThatSetByShiftTimeInputAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			//Enable the ScheduleEditShiftTimeNew
			ToggleAPI.updateToggle(Toggles.ScheduleEditShiftTimeNew.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), true);
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			WebElement shift = shiftOperatePage.clickOnProfileIcon();
			String id = shift.getAttribute("id");
			shiftOperatePage.clickOnEditShiftTime();
			String shiftTimeForInput = "08:00am-09:00am";
			String shiftTime = "8:00 am-9:00 am";
			Thread.sleep(3000);
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTimeForInput.split("-")[0], shiftTimeForInput.split("-")[1], true);

			Thread.sleep(5000);
			shiftOperatePage.clickOnUpdateEditShiftTimeButton();
			String shiftInfoOnIIcon = scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getShiftById(id));
			SimpleUtils.assertOnFail("The shift times on edit shift page and i icon should consistent, the time on edit shift time page: "
							+shiftTime + " the time on i icon: "+ shiftInfoOnIIcon,
					shiftInfoOnIIcon.contains(shiftTime) , false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate shift time cannot be earlier or later than the operating hours")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheShiftTimeCannotBeEarlierOrLaterThanOperatingHoursAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			//Enable the ScheduleEditShiftTimeNew
			ToggleAPI.updateToggle(Toggles.ScheduleEditShiftTimeNew.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), true);
			refreshCachesAfterChangeToggleOrABSwitch();
			Thread.sleep(3000);
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			//Login as admin
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "06:00PM");
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			shiftOperatePage.clickOnProfileIcon();
			shiftOperatePage.clickOnEditShiftTime();
			String shiftTime = "03:00 am-09:00 am";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], false);
			HashMap<String, String> shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			String shiftTimeOnShiftCard = shiftInfo.get("shiftTime");
			SimpleUtils.assertOnFail("The shift times on inputs and shift card should be consistent, the time in inputs: "
					+ shiftTime + " the time on shift card: "+ shiftTimeOnShiftCard, "3:00 am-9:00 am".equals(shiftTimeOnShiftCard), false);
			String compliance = "Shift starts too early (min: 6:00 AM)";
			String complianceFromEditShiftTimePage = shiftOperatePage.getEditShiftTimeCompliance();
			SimpleUtils.assertOnFail("The compliance on edit shift time page display incorrectly. The expected: "
					+ compliance + " the actual: "+complianceFromEditShiftTimePage,
					complianceFromEditShiftTimePage.contains(compliance), false);
			SimpleUtils.assertOnFail("The update button should disable! ",
					!shiftOperatePage.checkIfUpdateButtonEnabled(),false);


			shiftTime = "08:00 am-11:00 pm";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], false);
			shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			shiftTimeOnShiftCard = shiftInfo.get("shiftTime");
			SimpleUtils.assertOnFail("The shift times on inputs and shift card should be consistent, the time in inputs: "
					+ shiftTime + " the time on shift card: "+ shiftTimeOnShiftCard, "8:00 am-11:00 pm".equals(shiftTimeOnShiftCard), false);
			compliance = "Shift ends too late (max: 9:00 PM)";
			complianceFromEditShiftTimePage = shiftOperatePage.getEditShiftTimeCompliance();
			SimpleUtils.assertOnFail("The compliance on edit shift time page display incorrectly. The expected: "
							+ compliance + " the actual: "+complianceFromEditShiftTimePage,
					complianceFromEditShiftTimePage.contains(compliance), false);
			SimpleUtils.assertOnFail("The update button should disable! ",
					!shiftOperatePage.checkIfUpdateButtonEnabled(),false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the Next day checkbox on edit shift time page")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheNextDayCheckboxOnEditShiftTimePageAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
		try {
			//Enable the ScheduleEditShiftTimeNew
			ToggleAPI.updateToggle(Toggles.ScheduleEditShiftTimeNew.getValue(), getUserNameNPwdForCallingAPI().get(0),
					getUserNameNPwdForCallingAPI().get(1), true);
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			shiftOperatePage.clickOnProfileIcon();
			shiftOperatePage.clickOnEditShiftTime();
			String shiftTime = "8:00am-2:00pm";
			shiftOperatePage.setShiftTimesOnEditShiftTimePage(shiftTime.split("-")[0], shiftTime.split("-")[1], false);
			HashMap<String, String> shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			String shiftHrsBeforeCheckNextDay = shiftInfo.get("workCurrentShiftHrs").split(" ")[0];
			shiftOperatePage.checkOrUnCheckNextDayOnEditShiftTimePage(true);
			shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			String shiftHrsAfterCheckNextDay = shiftInfo.get("workCurrentShiftHrs").split(" ")[0];
			SimpleUtils.assertOnFail("The shift hrs display incorrectly on edit shift time page, the expected is:"
							+(Float.parseFloat(shiftHrsBeforeCheckNextDay)+24) + " the actual is: "+Float.parseFloat(shiftHrsAfterCheckNextDay),
					(Float.parseFloat(shiftHrsBeforeCheckNextDay)+23.5 == Float.parseFloat(shiftHrsAfterCheckNextDay))||
							(Float.parseFloat(shiftHrsBeforeCheckNextDay)+24 == Float.parseFloat(shiftHrsAfterCheckNextDay)),false);

			shiftOperatePage.checkOrUnCheckNextDayOnEditShiftTimePage(false);
			shiftInfo = shiftOperatePage.getInfoFromCardOnEditShiftTimePage();
			shiftHrsAfterCheckNextDay = shiftInfo.get("workCurrentShiftHrs").split(" ")[0];
			SimpleUtils.assertOnFail("The shift hrs display incorrectly on edit shift time page, the expected is:"
							+shiftHrsBeforeCheckNextDay+ " the actual is: "+ shiftHrsAfterCheckNextDay,
					shiftHrsBeforeCheckNextDay.equals(shiftHrsAfterCheckNextDay),false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify open shifts will show when grouping by job title")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyOpenShiftsDisplayWhenGroupingByJobTitleAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//			if (isLocationUsingControlsConfiguration) {
//				workRole = "Training";
//			}else{
//				workRole = "AM SERVER";
//			}

			//Go to the schedule view table
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Catch up on random shift for further shift creation
			String firstNameOfTM = null;
			String workRole = null;
			List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			firstNameOfTM = shiftInfo.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM = shiftInfo.get(0);
				shiftCount1++;
			}
			workRole = shiftInfo.get(4);

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();

			//In week view, Group by All filter have 4 filters:1.Group by all  2. Group by work role  3. Group by TM 4.Group by job title
			scheduleMainPage.validateGroupBySelectorSchedulePage(false);
			//Selecting any of them, check the schedule table
			scheduleMainPage.validateScheduleTableWhenSelectAnyOfGroupByOptions(false);

			//Create a new open shift
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.clickOnOkButtonOnErrorDialog();
			scheduleMainPage.saveSchedule();

			//Check the Open Shift in the WeekView
			scheduleMainPage.selectGroupByFilter("Group by Job Title");
			List<String> weekShiftTitles = scheduleShiftTablePage.getWeekScheduleShiftTitles();
			if (weekShiftTitles.contains("OPEN SHIFT")) {
				SimpleUtils.pass("Schedule page: The Schedule WeekView includes 'OPEN SHIFT'.");
			} else {
				SimpleUtils.fail("Schedule page: The Schedule WeekView doesn't includes 'OPEN SHIFT'!", false);
			}

			//Check the Open Shift in the DayView
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.selectGroupByFilter("Group by Job Title");
			List<String> dayShiftTitles = scheduleShiftTablePage.getDayScheduleGroupLabels();
			if (dayShiftTitles.contains("OPEN SHIFT")) {
				SimpleUtils.pass("Schedule page: The Schedule DayView includes 'OPEN SHIFT'.");
			} else {
				SimpleUtils.fail("Schedule page: The Schedule DayView doesn't includes 'OPEN SHIFT'!", false);
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

//	@Automated(automated = "Automated")
//	@Owner(owner = "Cosimo")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Verify the budget hours when inputting by Location")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void verifyTheSumOfEachWorkRoleBudgetConsistentWithTotalBudgetOnSTAFFCardAsInternalAdmin(String username, String password, String browser, String location)
//			throws Exception {
//		try {
//			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//			ForecastPage forecastPage = pageFactory.createForecastPage();
//			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//
//			//Go to the configuration page and set the labor budget and By Location
//			if (isLocationUsingControlsConfiguration){
//				controlsNewUIPage.clickOnControlsConsoleMenu();
//				controlsNewUIPage.clickOnControlsSchedulingPolicies();
//				controlsNewUIPage.clickOnGlobalLocationButton();
//				controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
//				controlsNewUIPage.selectBudgetGroupNonOP("By Location");
//			}else {
//				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
//				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
//				locationsPage.clickOnLocationsTab();
//				locationsPage.goToGlobalConfigurationInLocations();
//				locationsPage.editLaborBudgetSettingContent();
//				locationsPage.turnOnOrTurnOffLaborBudgetToggle(true);
//				locationsPage.selectBudgetGroup("By Location");
//				locationsPage.saveTheGlobalConfiguration();
//				if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
//					//Back to the console page
//					switchToConsoleWindow();
//				}
//			}
//
//			//Go to the forecast labor tab
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Forecast.getValue());
//			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Forecast.getValue()), false);
//
//			//Edit the budget
//			forecastPage.goToForecastLaborWeek();
//			forecastPage.editLaborBudgetOnSummarySmartCard();
//			Float laborBudget = Float.parseFloat((forecastPage.getLaborBudgetOnSummarySmartCard().trim()));
//
//			//Go to WeekView tab and un-generate the schedule
//			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
//			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Schedule.getValue()), false);
//			scheduleCommonPage.clickOnWeekView();
//			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//			if (isActiveWeekGenerated) {
//				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//			}
//			Thread.sleep(5000);
//
//			//Check the budget value in the staff card
//			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//			Float budgetValue = scheduleShiftTablePage.getTotalBudgetFromSTAFFSmartCard();
//			float totalBudget = 0;
//			for(int i = 0; i < budgetValues.size()-1; i++){
//				totalBudget = totalBudget + budgetValues.get(i);
//			}
//			SimpleUtils.assertOnFail("The budget on Staff Card is not consisting with the edited value on Forecast page!",laborBudget == totalBudget,false);
//
//		} catch (Exception e) {
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the budget hours when inputting by Location")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheBudgetHoursWhenByLocationAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();

			//Go to the configuration page and set the labor budget and By Location
			if (isLocationUsingControlsConfiguration){
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsSchedulingPolicies();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
				controlsNewUIPage.selectBudgetGroupNonOP("By Location");
				Thread.sleep(240000);
			}else {
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToGlobalConfigurationInLocations();
				locationsPage.editLaborBudgetSettingContent();
				locationsPage.turnOnOrTurnOffLaborBudgetToggle(true);
				locationsPage.selectBudgetGroup("By Location");
				locationsPage.saveTheGlobalConfiguration();
				Thread.sleep(240000);
				//Back to the console page
				switchToConsoleWindow();
			}

			//Go to the forecast labor tab
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Forecast.getValue()), false);

			//Edit the budget
			forecastPage.goToForecastLaborWeek();
			forecastPage.editLaborBudgetOnSummarySmartCard();
			String laborBudget = forecastPage.getLaborBudgetOnSummarySmartCard().trim();
			String laborBudgetExpend = forecastPage.getLaborBudgetOnSummarySmartCard().trim() + " Hours";
			forecastPage.verifyRefreshBtnInLaborWeekView();

			//Go to WeekView tab and un-generate the schedule
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);

			//Check the budget value in the smartcard
			smartCardPage.isSpecificSmartCardLoaded("Weekly Budget");
			String weeklyBudget = smartCardPage.getBudgetValueFromWeeklyBudgetSmartCard("Weekly Budget");
			SimpleUtils.assertOnFail("The budget on weekly view is not consisting with the edited value on Forecast page!",laborBudgetExpend.equals(weeklyBudget),false);

			//Check the budget value in the staff card
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			String staffBudget = scheduleShiftTablePage.getTotalBudgetFromSTAFFSmartCard();
			SimpleUtils.assertOnFail("The budget on Staff Card is not consisting with the edited value on Forecast page!",laborBudget.equals(staffBudget),false);

			//Generate a new schedule and check the budget value in the schedule smart card
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			scheduleCommonPage.clickOnWeekView();
			String smartCardBudget = smartCardPage.getBudgetValueFromScheduleBudgetSmartCard();
			SimpleUtils.assertOnFail("The budget on Schedule Smart Card is not consisting with the edited value on Forecast page!",laborBudget.equals(smartCardBudget),false);

			//Go to Overview tab and check the current week's budget
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), false);
			String overviewBudget = scheduleOverviewPage.getCurrentWeekBudgetHours().trim();
			SimpleUtils.assertOnFail("The budget on Schedule Smart Card is not consisting with the edited value on Forecast page!",laborBudget.equals(overviewBudget),false);

			//Go to DM overview tab and check the current week's budget
			String districtName = dashboardPage.getCurrentDistrict();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			locationSelectorPage.reSelectDistrict(districtName);
			ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
			String DMViewBudget = String.valueOf(scheduleDMViewPage.getBudgetedHourOfScheduleInDMViewByLocation(location)).trim();
			SimpleUtils.assertOnFail("The budget on Schedule Smart Card is not consisting with the budget on DM view page!",laborBudget.equals(DMViewBudget),false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the functionality of Create Schedule button when SM doesn't have permission \"Manage Budget\"")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheNoBudgetPermissionForSMAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			String accessRoleTab = "Access Roles";
			String permissionSection = "Schedule";
			String permission = "Manage Budget";
			String actionOff = "off";
			String actionOn = "on";
			//Go to the configuration page and set the labor budget and By Location
			if (isLocationUsingControlsConfiguration){
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsSchedulingPolicies();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
				controlsNewUIPage.selectBudgetGroupNonOP("By Location");

				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Budget permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection,permission,actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(240000);

			}else {
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToGlobalConfigurationInLocations();
				locationsPage.editLaborBudgetSettingContent();
				locationsPage.turnOnOrTurnOffLaborBudgetToggle(true);
				locationsPage.selectBudgetGroup("By Location");
				locationsPage.saveTheGlobalConfiguration();

				//Go to Users and Roles page and switch to the Access Roles sub tab
				ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Budget permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection,permission,actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(240000);

				//Back to the console page
				switchToConsoleWindow();
			}

			//Go to forecast page, clear the budget value
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Forecast.getValue()), false);
			forecastPage.goToForecastLaborWeek();
			forecastPage.clearLaborBudgetOnSummarySmartCard();
			forecastPage.verifyRefreshBtnInLaborWeekView();

			//Un-generate the schedule
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);

			//Login as SM, check the Create Schedule button is disabled
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(120000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			createSchedulePage.isGenerateButtonNotClickable();

			//Verify the tooltips when mouse hovering the Create Schedule button
			createSchedulePage.verifyTooltipForUnclickableCreateScheduleBtn();

			//Login as Internal Admin, add Manage Budget permission back to the SM
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			Boolean isLocationUsingControlsConfigurationTwice = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfigurationTwice){
				//Add permission on Controls
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection,permission,actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(240000);

			}else{
				//Add permission on OP
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection,permission,actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(240000);
				//Back to the console page
				switchToConsoleWindow();

				//Login as SM, check the Create Schedule button is enabled
				loginPage.logOut();
				Thread.sleep(120000);
				loginAsDifferentRole(AccessRoles.StoreManager.getValue());
				SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
				scheduleCommonPage.clickOnScheduleConsoleMenuItem();
				scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Assign TM warning: TM status is overtime")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTMOverTimeStatusAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Set daily overtime in the Compliance page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Controls page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsComplianceSection();
				compliancePage.turnOnOrTurnOffDayOTToggle(true);
				compliancePage.editDayOTSetting("8 hours", "single work day", true);
				Thread.sleep(240000);
			} else {
				//Go to OP page
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToSubLocationsInLocationsPage();
				locationsPage.searchLocation(location);
				SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
				locationsPage.clickOnLocationInLocationResult(location);
				locationsPage.clickOnConfigurationTabOfLocation();
				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
				configurationPage.goToConfigurationPage();
				configurationPage.clickOnConfigurationCrad("Compliance");
				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
				Thread.sleep(3000);
				compliancePage.turnOnOrTurnOffDayOTToggle(true);
				compliancePage.editDayOTSetting("8 hours", "single work day", true);
				configurationPage.publishNowTheTemplate();
				Thread.sleep(240000);
				//Back to the console page
				switchToConsoleWindow();
			}

			//Go to the schedule view table
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");

			//Delete all specific shifts
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.selectWorkRoleFilterByText(workRole, true);

			List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM1 = shiftInfo.get(0);
			int shiftCount = 0;
			while ((firstNameOfTM1.equalsIgnoreCase("open")
					|| firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM1 = shiftInfo.get(0);
				shiftCount++;
			}
			String workRole1= shiftInfo.get(4);

			int shiftCount2 = 0;
			String firstNameOfTM2 = shiftInfo.get(0);
			while ((firstNameOfTM2.equalsIgnoreCase("open")
					|| firstNameOfTM2.equalsIgnoreCase("unassigned")
					|| firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1)) && shiftCount2 < 100) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM2 = shiftInfo.get(0);
				shiftCount2++;
			}

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
			scheduleMainPage.saveSchedule();

			//Create one OT shift and assign it to the particular TM
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole1);
			newShiftPage.moveSliderAtCertainPoint("10pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			//Assign the shift to other TM, check the OT
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			Boolean assignTMPage = shiftOperatePage.isAssignTeamMemberShowWell();
			SimpleUtils.assertOnFail("The Assign TM page is not displayed correctly!", assignTMPage, false);
			String OTMessage = "Will trigger 3.5Hrs daily OT";
			shiftOperatePage.searchTMOnAssignPage(firstNameOfTM2);
			String realOTMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
			SimpleUtils.assertOnFail("The Assign TM page is not displayed correctly!", realOTMessage.contains(OTMessage), false);

			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM2);
			String alertMessage = firstNameOfTM2 + " will incur 3.5 hours of daily overtime";
			String realAlertMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
			SimpleUtils.assertOnFail("The Pop up warning message is not expected!", realAlertMessage.contains(alertMessage), false);
			shiftOperatePage.clickOnAssignAnywayButton();
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Verify the daily violation after saving.
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM2);
			List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
			for(int i = 0; i< complianceMessage.size(); i++){
				if (complianceMessage.get(i).contains("daily overtime")) {
					SimpleUtils.pass("The daily OT message is showing correctly!");
					break;
				}
				if ((i == complianceMessage.size() - 1) && !(complianceMessage.get(i).contains("daily overtime"))){
					SimpleUtils.fail("The daily OT message is missing!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify assign TM message: TM will incur clopening")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTMClopeningStatusAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Set clopening in the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration) {
				//Go to Controls page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsSchedulingPolicies();
				SimpleUtils.assertOnFail("Scheduling Policies Page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectClopeningHours(12);
				Thread.sleep(240000);
			} else {
				//Go to OP page
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToSubLocationsInLocationsPage();
				locationsPage.searchLocation(location);
				SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
				locationsPage.clickOnLocationInLocationResult(location);
				locationsPage.clickOnConfigurationTabOfLocation();
				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
				configurationPage.goToConfigurationPage();
				configurationPage.clickOnConfigurationCrad("Scheduling Policies");
				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
				Thread.sleep(3000);
				controlsNewUIPage.selectClopeningHoursOP("12");
				configurationPage.publishNowTheTemplate();
				Thread.sleep(240000);
				//Back to the console page
				switchToConsoleWindow();
			}

			//Go to the schedule view table
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");


			//Delete all specific shifts
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.selectWorkRoleFilterByText(workRole, true);

			List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM1 = shiftInfo.get(0);
			int shiftCount = 0;
			while ((firstNameOfTM1.equalsIgnoreCase("open")
					|| firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM1 = shiftInfo.get(0);
				shiftCount++;
			}

			int shiftCount2 = 0;
			String firstNameOfTM2 = shiftInfo.get(0);
			while ((firstNameOfTM2.equalsIgnoreCase("open")
					|| firstNameOfTM2.equalsIgnoreCase("unassigned")
					|| firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1)) && shiftCount2 < 100) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM2 = shiftInfo.get(0);
				shiftCount2++;
			}

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
			scheduleMainPage.saveSchedule();

			//Create two shifts and assign them to the different TMs
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectMultipleOrSpecificWorkDay(6,true);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();

			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectMultipleOrSpecificWorkDay(5,true);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM2);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Assign one shift to other TM, check the clopening message
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			Boolean assignTMPage = shiftOperatePage.isAssignTeamMemberShowWell();
			SimpleUtils.assertOnFail("The Assign TM page is not displayed correctly!", assignTMPage, false);
			String cloMessage = "Will trigger clopening shift";
			shiftOperatePage.searchTMOnAssignPage(firstNameOfTM2);
			String realCloMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
			SimpleUtils.assertOnFail("The warning message on the Assign page is not expected!", realCloMessage.contains(cloMessage), false);


			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM2);
			String alertMessage = firstNameOfTM2 + " will incur clopening";
			String realAlertMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
			SimpleUtils.assertOnFail("The Pop up warning message is not expected!", realAlertMessage.contains(alertMessage), false);
			shiftOperatePage.clickOnAssignAnywayButton();
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Verify the Clopening violation after saving.
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM2);
			List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
			for(int i = 0; i< complianceMessage.size(); i++){
				if (complianceMessage.get(i).contains("Clopening")) {
					SimpleUtils.pass("The Clopening message is showing correctly!");
					break;
				}
				if ((i == complianceMessage.size() - 1) && !(complianceMessage.get(i).contains("Clopening"))){
					SimpleUtils.fail("The Clopening message is missing!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify assign TM warning: Assignment rule violation - Yes")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyOverrideAssignmentRulesToYesAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Set override rule in the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration) {
				//Go to Controls page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsSchedulingPolicies();
				SimpleUtils.assertOnFail("Scheduling Policies Page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.enableOverRideAssignmentRuleAsYes();
				Thread.sleep(10000);
			} else {
				//Go to OP page
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToSubLocationsInLocationsPage();
				locationsPage.searchLocation(location);
				SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
				locationsPage.clickOnLocationInLocationResult(location);
				locationsPage.clickOnConfigurationTabOfLocation();
				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
				configurationPage.goToConfigurationPage();
				configurationPage.clickOnConfigurationCrad("Scheduling Policies");
				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
				Thread.sleep(3000);
				controlsNewUIPage.enableOverRideAssignmentRuleAsYesForOP();
				configurationPage.publishNowTheTemplate();
				switchToConsoleWindow();
			}
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);
			refreshCachesAfterChangeTemplate();
			Thread.sleep(20000);

			//Go to the schedule view table
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");


			//Pick up two shifts with different roles randomly, delete all relevant shifts
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

//			List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//			String firstNameOfTM1 = shiftInfo1.get(0);
//			String lastNameOfTM1 = shiftInfo1.get(5);
//			int shiftCount1 = 0;
//			while ((firstNameOfTM1.equalsIgnoreCase("open")
//					|| firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
//				shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//				firstNameOfTM1  = shiftInfo1.get(0);
//				lastNameOfTM1  = shiftInfo1.get(5);
//				shiftCount1++;
//			}
//			String workRole1 =  shiftInfo1.get(4);
//
//			List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//			int shiftCount2 = 0;
//			String firstNameOfTM2 = shiftInfo2.get(0);
//			String lastNameOfTM2 = shiftInfo2.get(5);
//			String workRole2 = shiftInfo2.get(4);
//			while ((firstNameOfTM2.equalsIgnoreCase("open")
//					|| firstNameOfTM2.equalsIgnoreCase("unassigned")
//					|| firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1) || workRole1.equalsIgnoreCase(workRole2))
//					&& shiftCount2 < 150) {
//				shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//				firstNameOfTM2  = shiftInfo2.get(0);
//				lastNameOfTM2  = shiftInfo2.get(5);
//				workRole2 = shiftInfo2.get(4);
//				shiftCount2++;
//			}

			String nameOfTM1= "Taylor Tang";
			String nameOfTM2= "Ava Kautzer";
			String firstNameOfTM1= "Taylor";
			String firstNameOfTM2= "Ava";
			String workRole = "MOD";
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM1);
			shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM2);
			scheduleMainPage.saveSchedule();

			//Create one shift and assign it to the TM1
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(nameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Assign the specific shift to another TM whose role is not match
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(nameOfTM1);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			Boolean assignTMPage = shiftOperatePage.isAssignTeamMemberShowWell();
			SimpleUtils.assertOnFail("The Assign TM page is not displayed correctly!", assignTMPage, false);
			String roleVioMessage = "Role Violation";
			shiftOperatePage.searchTMOnAssignPage(nameOfTM2);
			String realRoleVioMessage = shiftOperatePage.getTheMessageOfAssignedShiftToTM();
			SimpleUtils.assertOnFail("The Role Violation message on the Assign page is not expected!", realRoleVioMessage.contains(roleVioMessage), false);
			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(nameOfTM2);
			String alertMessage = "This assignment will trigger a role violation.";
			String realAlertMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
			SimpleUtils.assertOnFail("The Pop up Role Violation message is not expected!", realAlertMessage.contains(alertMessage), false);
			shiftOperatePage.clickOnAssignAnywayButton();
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Verify the Role Violation after saving.
			scheduleMainPage.searchShiftOnSchedulePage(nameOfTM2);
			List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
			for(int i = 0; i< complianceMessage.size(); i++){
				if (complianceMessage.get(i).contains("Role Violation")) {
					SimpleUtils.pass("The Role Violation message is showing correctly!");
					break;
				}
				if ((i == complianceMessage.size() - 1) && !(complianceMessage.get(i).contains("Role Violation"))){
					SimpleUtils.fail("The Role Violation message is missing!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify assign TM warning: Assignment rule violation - No")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyOverrideAssignmentRulesToNoAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Set override rule in the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration) {
				//Go to Controls page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsSchedulingPolicies();
				SimpleUtils.assertOnFail("Scheduling Policies Page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.enableOverRideAssignmentRuleAsNo();
				Thread.sleep(5000);
			} else {
				//Go to OP page
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToSubLocationsInLocationsPage();
				locationsPage.searchLocation(location);
				SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
				locationsPage.clickOnLocationInLocationResult(location);
				locationsPage.clickOnConfigurationTabOfLocation();
				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
				configurationPage.goToConfigurationPage();
				configurationPage.clickOnConfigurationCrad("Scheduling Policies");
				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
				Thread.sleep(10000);
				controlsNewUIPage.enableOverRideAssignmentRuleAsNoForOP();
				configurationPage.publishNowTheTemplate();
				switchToConsoleWindow();
			}
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);
			refreshCachesAfterChangeTemplate();
			Thread.sleep(20000);

			//Go to the schedule view table
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(6000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("09:00AM", "09:00PM");

			//Delete all specific shifts
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//			List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//			String firstNameOfTM1 = shiftInfo1.get(0);
//			String lastNameOfTM1 = shiftInfo1.get(5);
//			int shiftCount1 = 0;
//			while ((firstNameOfTM1.equalsIgnoreCase("open")
//					|| firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
//				shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//				firstNameOfTM1 = shiftInfo1.get(0);
//				lastNameOfTM1 = shiftInfo1.get(5);
//				shiftCount1++;
//			}
//			String workRole1 = shiftInfo1.get(4);
//
//			int shiftCount2 = 0;
//			List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//			String firstNameOfTM2 = shiftInfo2.get(0);
//			String lastNameOfTM2 = shiftInfo2.get(5);
//			String workRole2 = shiftInfo2.get(4);
//			while ((firstNameOfTM2.equalsIgnoreCase("open")
//					|| firstNameOfTM2.equalsIgnoreCase("unassigned")
//					|| firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1) || workRole1.equalsIgnoreCase(workRole2)) && shiftCount2 < 150) {
//				shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//				firstNameOfTM2 = shiftInfo2.get(0);
//				lastNameOfTM2 = shiftInfo2.get(5);
//				workRole2 = shiftInfo2.get(4);
//				shiftCount2++;
//			}
//
			String nameOfTM1= "Taylor Tang";
			String nameOfTM2= "Ava Kautzer";
			String firstNameOfTM1= "Taylor";
			String firstNameOfTM2= "Ava";
			String workRole = "MOD";
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM1);
			shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM2);
			scheduleMainPage.saveSchedule();

			//Create two shifts and assign them to the different TMs
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRoleCaseSensitive(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(nameOfTM1);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Assign the specific shift to another TM whose role is not match
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(nameOfTM1);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			Boolean assignTMPage = shiftOperatePage.isAssignTeamMemberShowWell();
			SimpleUtils.assertOnFail("The Assign TM page is not displayed correctly!", assignTMPage, false);
			String roleVioMessage = "Role Violation";
			shiftOperatePage.searchTMOnAssignPage(nameOfTM2);
			String realRoleVioMessage = shiftOperatePage.getTheMessageOfAssignedShiftToTM();
			SimpleUtils.assertOnFail("The Role Violation message on the Assign page is not expected!", realRoleVioMessage.contains(roleVioMessage), false);
			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(nameOfTM2);
			String alertMessage1 = "This assignment will trigger a role violation";
			String alertMessage2 = " can not take a " + workRole + " shift";
			String realAlertMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
			boolean isCorrect = realAlertMessage.contains(alertMessage1) && realAlertMessage.contains(alertMessage2);
			SimpleUtils.assertOnFail("The Pop up Role Violation message is not expected!", isCorrect, false);
			scheduleShiftTablePage.clickOnOkButtonInWarningMode();
//			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(nameOfTM2);
//			SimpleUtils.assertOnFail("The Pop up Role Violation message is not expected!", isCorrect, false);
//			scheduleShiftTablePage.clickOnOkButtonInWarningMode();
//			boolean okBtnLoad = scheduleShiftTablePage.isOkButtonInWarningModeLoaded();
//			int count1 = 0;
//			while(okBtnLoad && count1 < 3){
//				scheduleShiftTablePage.clickOnOkButtonInWarningMode();
//				Thread.sleep(3000);
//				okBtnLoad = scheduleShiftTablePage.isOkButtonInWarningModeLoaded();
//				count1++;
//				continue;
//			}
//			Thread.sleep(3000);
			shiftOperatePage.clickOnCloseBtnOfAssignDialog();
//
			boolean closeBtnLoad = shiftOperatePage.isCloseBtnOfAssignDialogLoaded();
			int count2 = 0;
			while(closeBtnLoad && count2 < 3){
				shiftOperatePage.clickOnCloseBtnOfAssignDialog();
				Thread.sleep(3000);
				closeBtnLoad = shiftOperatePage.isCloseBtnOfAssignDialogLoaded();
				count2++;
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		} finally {
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration) {
				//Go to Controls page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsSchedulingPolicies();
				SimpleUtils.assertOnFail("Scheduling Policies Page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.enableOverRideAssignmentRuleAsYes();
				Thread.sleep(10000);
			} else {
				//Go to OP page
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToSubLocationsInLocationsPage();
				locationsPage.searchLocation(location);
				SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
				locationsPage.clickOnLocationInLocationResult(location);
				locationsPage.clickOnConfigurationTabOfLocation();
				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
				configurationPage.goToConfigurationPage();
				configurationPage.clickOnConfigurationCrad("Scheduling Policies");
				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
				Thread.sleep(3000);
				controlsNewUIPage.enableOverRideAssignmentRuleAsYesForOP();
				configurationPage.publishNowTheTemplate();
				Thread.sleep(240000);
				//Back to the console page
				switchToConsoleWindow();
			}
		}
	}



	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the functionality of Minimum time between shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheFunctionalityOfMinTimeBetweenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//			boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//			if (!isLocationUsingControlsConfiguration) {
//				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
//				locationsPage.clickOnLocationsTab();
//				locationsPage.goToSubLocationsInLocationsPage();
//				locationsPage.searchLocation(location);
//				SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
//				locationsPage.clickOnLocationInLocationResult(location);
//				locationsPage.clickOnConfigurationTabOfLocation();
//				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
//				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
//				configurationPage.goToConfigurationPage();
//				configurationPage.clickOnConfigurationCrad("Scheduling Policies");
//				//Click on the template which is associated to the location to view
//				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
//				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
//				int maximumNumber = 3;
//				int minimumTime = 30;
//				configurationPage.updateMaximumNumberOfShiftsPerDay(maximumNumber);
//				configurationPage.updateMinimumTimeBetweenShifts(minimumTime);
//				configurationPage.publishNowTheTemplate();
//				switchToConsoleWindow();
//
//				loginPage.logOut();
//				loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
//				int i = 0;
//				while (i<5) {
//					Thread.sleep(60000);
//					scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//					i++;
//				}
//			}

			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM = shiftInfo1.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM  = shiftInfo1.get(0);
				shiftCount1++;
			}
			String lastName = shiftInfo1.get(5);
			String workRole =  shiftInfo1.get(4);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("6:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM + " "+ lastName);
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("10:15am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8:15am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchWithOutSelectTM(firstNameOfTM+ " "+ lastName);
			//https://legiontech.atlassian.net/browse/SCH-7682
			String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
			String expectedWaningMessage= "Minimum time between shifts";
			SimpleUtils.assertOnFail(expectedWaningMessage+ " message fail to load! The actual message is:"+shiftWarningMessage,
					shiftWarningMessage.contains(expectedWaningMessage), false);
			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
//			expectedWaningMessage = firstNameOfTM+ " does not have minimum time between shifts";
			if(newShiftPage.ifWarningModeDisplay()){
				String warningMessage = newShiftPage.getWarningMessageFromWarningModal();

				if (warningMessage.toLowerCase().contains(expectedWaningMessage.toLowerCase())){
					SimpleUtils.pass(expectedWaningMessage+" message displays");
				} else {
					SimpleUtils.fail("There is no "+expectedWaningMessage+" warning message displaying", false);
				}
				shiftOperatePage.clickOnAssignAnywayButton();
			} else {
				SimpleUtils.fail("There is no '"+expectedWaningMessage+"' warning modal displaying!",false);
			}
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
			//https://legiontech.atlassian.net/browse/SCH-7196
			expectedWaningMessage = "Minimum time between shifts";
			String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(0)).toString();
			SimpleUtils.assertOnFail("'"+expectedWaningMessage+"' compliance message display failed, the actual message is:"+actualMessage,
					actualMessage.contains(expectedWaningMessage) , false);
			actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(1)).toString();
			SimpleUtils.assertOnFail("'"+expectedWaningMessage+"' compliance message display failed, the actual message is:"+actualMessage,
					actualMessage.contains(expectedWaningMessage) , false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify the functionality of max shifts per day")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheFunctionalityOfMaxShiftsPerDayAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(!isActiveWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM = shiftInfo1.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM  = shiftInfo1.get(0);
				shiftCount1++;
			}
			String workRole =  shiftInfo1.get(4);
			String lastNameOfTM = shiftInfo1.get(5);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("6:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM+" "+lastNameOfTM);
			newShiftPage.clickOnCreateOrNextBtn();

			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("9:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM+" "+lastNameOfTM);
			newShiftPage.clickOnCreateOrNextBtn();

			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("2:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("12:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM+" "+lastNameOfTM);
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("5:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("3:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM+" "+lastNameOfTM);
			//https://legiontech.atlassian.net/browse/SCH-7963
//			newShiftPage.searchWithOutSelectTM(firstNameOfTM);
//			String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
//			SimpleUtils.assertOnFail("Max shift per day violation message fail to load!",
//					shiftWarningMessage.contains("Max shift per day violation"), false);
//			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
//			if(newShiftPage.ifWarningModeDisplay()){
//				String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
//				if (warningMessage.contains("Max shift per day violation")){
//					SimpleUtils.pass("Max shift per day violation message displays");
//				} else {
//					SimpleUtils.fail("There is no 'Max shift per day violation' warning message displaying, the actual is:"+warningMessage, false);
//				}
//				shiftOperatePage.clickOnAssignAnywayButton();
//			} else {
//				SimpleUtils.fail("There is no 'Max shift per day violation' warning modal displaying!",false);
//			}
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
			String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(3)).toString();
			SimpleUtils.assertOnFail("'Max shift per day violation' compliance message display failed",
					actualMessage.contains("Max shift per day") , false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify the functionality of daily OT")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheFunctionalityOfDailyOTAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.searchLocation(location);               ;
			SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
			locationsPage.clickOnLocationInLocationResult(location);
			locationsPage.clickOnConfigurationTabOfLocation();
			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Compliance");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(true);
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM = shiftInfo1.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM  = shiftInfo1.get(0);
				shiftCount1++;
			}
			String workRole =  shiftInfo1.get(4);
			String lastNameOfTM = shiftInfo1.get(5);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			Thread.sleep(3000);
			scheduleMainPage.saveSchedule();
			Thread.sleep(3000);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			Thread.sleep(8000);
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("11:30am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("6:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+ lastNameOfTM);
			newShiftPage.clickOnCreateOrNextBtn();
			Thread.sleep(3000);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			Thread.sleep(5000);
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("4:30pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("12:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchWithOutSelectTM(firstNameOfTM+ " "+ lastNameOfTM);
			String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
			String expectedMessage = "Will trigger 1.5Hrs daily OT";
			SimpleUtils.assertOnFail(expectedMessage+ " message fail to load! The actual message is:"+shiftWarningMessage,
					shiftWarningMessage.contains(expectedMessage), false);
			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
			expectedMessage = firstNameOfTM + " will incur 1.5 hours of daily overtime";
			if(newShiftPage.ifWarningModeDisplay()){
				String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
				if (warningMessage.contains(expectedMessage)){
					SimpleUtils.pass(expectedMessage+ " message displays");
				} else {
					SimpleUtils.fail("There is no 'Will trigger 1Hrs daily OT' warning message displaying, the actual message is:"+warningMessage, false);
				}
				shiftOperatePage.clickOnAssignAnywayButton();
			} else {
				SimpleUtils.fail("There is no 'Will trigger 1Hrs daily OT' warning modal displaying!",false);
			}
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM+ " "+lastNameOfTM);
			List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
			String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(1)).toString();
			expectedMessage = "hrs daily overtime";
			SimpleUtils.assertOnFail(expectedMessage+ " compliance message display failed, then actual message is: "+actualMessage,
					actualMessage.contains(expectedMessage) , false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the published schedule should not change to Draft after Auto-fill open shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyThePublishedScheduleShouldNotChangeToDraftAfterAutoFillOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab("Schedule");
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(!isActiveWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			if (smartCardPage.isRequiredActionSmartCardLoaded()){
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
				scheduleMainPage.saveSchedule();
			}
			scheduleMainPage.publishOrRepublishSchedule();
			SimpleUtils.assertOnFail("The Publish and Republish buttons should not loaded! ",
					!createSchedulePage.isPublishButtonLoaded()
							&& !createSchedulePage.isRepublishButtonLoadedOnSchedulePage(), false);
			String successMessage = toggleSummaryPage.autoFillOpenShifts();
			int fillShiftCount = Integer.parseInt(successMessage.split(" ")[0]);
			if (fillShiftCount == 0){
				SimpleUtils.assertOnFail("The Publish and Republish buttons should not loaded! ",
						!createSchedulePage.isPublishButtonLoaded()
								&& !createSchedulePage.isRepublishButtonLoadedOnSchedulePage(), false);
				SimpleUtils.assertOnFail("The UNPUBLISHED CHANGES should not load! ",
						!smartCardPage.isSpecificSmartCardLoaded("UNPUBLISHED CHANGES"), false);

			} else {
				SimpleUtils.assertOnFail("The Publish should not loaded but the Republish buttons should load! ",
						!createSchedulePage.isPublishButtonLoaded()
								&& createSchedulePage.isRepublishButtonLoadedOnSchedulePage(), false);
				SimpleUtils.assertOnFail("The UNPUBLISHED CHANGES should load! ",
						smartCardPage.isSpecificSmartCardLoaded("UNPUBLISHED CHANGES"), false);
			}

			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			String firstWeekScheduleStatus = scheduleOverviewPage.getScheduleWeeksStatus().get(0);
			SimpleUtils.assertOnFail("The expected schedule status is: Published or Finalized, the actual status is:"+firstWeekScheduleStatus,
					firstWeekScheduleStatus.equalsIgnoreCase("Published")
					||firstWeekScheduleStatus.equalsIgnoreCase("Finalized"), false);
			String firstWeekScheduleStatusWarningMessage = scheduleOverviewPage.getScheduleWeeksStatusWarningMessage().get(0);
			if (fillShiftCount == 0){
				SimpleUtils.assertOnFail("The expected schedule status is: Finalized on, the actual status is:"+firstWeekScheduleStatusWarningMessage,
						firstWeekScheduleStatusWarningMessage.contains("Finalized on"), false);

			} else {
				SimpleUtils.assertOnFail("The expected schedule status is: Unpublished Edits, the actual status is:"+firstWeekScheduleStatusWarningMessage,
						firstWeekScheduleStatusWarningMessage.equalsIgnoreCase("Unpublished Edits"), false);
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify scheduled hours and number of shifts in offers when the TM is scheduled in 2 locations")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduledHoursAndNumberOfShiftsInOffersWhenTheTMIsScheduledIn2LocationsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "09:00pm");
			List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM = shiftInfo1.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM  = shiftInfo1.get(0);
				shiftCount1++;
			}
			String workRole =  shiftInfo1.get(4);
			String lastName =  shiftInfo1.get(5);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+ lastName);
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
			if (smartCardPage.isRequiredActionSmartCardLoaded()) {
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
				scheduleMainPage.saveSchedule();
			}
			createSchedulePage.publishActiveSchedule();
			String nearByLocation = getCrendentialInfo("NearByLocationInfo");
			locationSelectorPage.changeLocation(nearByLocation);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(!isActiveWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("5:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("2:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
			if (smartCardPage.isRequiredActionSmartCardLoaded()) {
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
				scheduleMainPage.saveSchedule();
			}
			createSchedulePage.publishActiveSchedule();
			locationSelectorPage.changeLocation(location);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyTM.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
			SimpleUtils.assertOnFail("The expected shift count is 2, the actual is :"+shiftsOfFirstDay.size(),
					shiftsOfFirstDay.size()==2, false);
			String textOnIIconPopUp = scheduleShiftTablePage.getIIconTextInfo(shiftsOfFirstDay.get(0));
			String weekTotalHrsText = "6 Hrs this week";
			SimpleUtils.assertOnFail(weekTotalHrsText+ " message display failed, the actual is:"+textOnIIconPopUp,
					textOnIIconPopUp.contains(weekTotalHrsText), false);
			scheduleMainPage.clickOnCloseSearchBoxButton();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(1, 1, 1);
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(staffingOption.ManualShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchWithOutSelectTM(firstNameOfTM+ " "+lastName);
			Thread.sleep(5000);
			HashMap<String, Float> totalShiftHrsAndShiftCountThisWeek = shiftOperatePage.getTotalShiftHrsAndShiftCountThisWeek();
			SimpleUtils.assertOnFail("The expected total shift hrs this week is 6, the actual is "
							+totalShiftHrsAndShiftCountThisWeek.get("shiftHrs"),
					totalShiftHrsAndShiftCountThisWeek.get("shiftHrs") == 6, false);
			SimpleUtils.assertOnFail("The expected total shift count this week is 6, the actual is "
							+totalShiftHrsAndShiftCountThisWeek.get("shiftCount"),
					totalShiftHrsAndShiftCountThisWeek.get("shiftCount") == 2, false);
//			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM+" "+lastName);
			newShiftPage.selectTeamMembers();
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyAll.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage("Open");
			List<Integer> openShifts = scheduleShiftTablePage.getAddedShiftIndexes("Open");
			SimpleUtils.assertOnFail("It should has one open shift in the schedule, actual has "+openShifts.size(),
					openShifts.size()>=1, false);
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(openShifts.get(0));
			scheduleShiftTablePage.clickViewStatusBtn();
			String offerTMInfo = shiftOperatePage.getOfferStatusFromOpenShiftStatusList(firstNameOfTM);
			String totalShiftHrsMessage = "6 Scheduled Hrs";
			String totalShiftCountMessage = "2 Shift";
			SimpleUtils.assertOnFail(totalShiftHrsMessage+ " message display failed, the actual is:"+offerTMInfo,
					offerTMInfo.contains(totalShiftHrsMessage.toLowerCase()), false);
			SimpleUtils.assertOnFail(totalShiftCountMessage+ " message display failed, the actual is:"+offerTMInfo,
					offerTMInfo.contains(totalShiftCountMessage.toLowerCase()), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify \"Edit operating hours\" option is available")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditOperatingHoursUnderMoreActionsAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

			//Go to the schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();

			//Check the edit operating hours page
			scheduleMainPage.goToEditOperatingHoursView();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify \"Edit operating hours\" option is disabled in edit mode")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditOpeHoursDisabledAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

			//Go to the schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Check the edit operating hours page is disable
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			SimpleUtils.assertOnFail("The More Actions button is clickable!", !(scheduleMainPage.isMoreActionsBtnClickable()), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the functionality of \"Edit Operating hours\" option")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheFunctionalityOfEditOpeHoursAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

			//Go to the schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Check the content of week days on operating hours page, it matches with toggle summary view
			scheduleMainPage.goToToggleSummaryView();
			scheduleMainPage.checkOperatingHoursOnToggleSummary();
			scheduleMainPage.goToEditOperatingHoursView();
			scheduleMainPage.checkOperatingHoursOnEditDialog();
			scheduleMainPage.clickCancelBtnOnEditOpeHoursPage();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the functionality of SAVE button on \"Edit operating hours\" window")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheSaveFunctionOnEditOpeHoursDialogAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

			//Go to the schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("07:00am", "09:00pm");

			//Edit the operating day and cancel all actions.
			scheduleMainPage.goToToggleSummaryView();
			scheduleMainPage.goToEditOperatingHoursView();
			List<String> weekDay = new ArrayList<>(Arrays.asList("Sunday"));
			scheduleMainPage.closeTheParticularOperatingDay(weekDay);
			scheduleMainPage.openTheParticularOperatingDay(weekDay);
			scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDay, "10:00AM","10:00PM");
			scheduleMainPage.clickCancelBtnOnEditOpeHoursPage();
			scheduleMainPage.goToEditOperatingHoursView();

			//Editing the operating day and save all actions.
//			List<String> weekDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
			List<String> weekDays = new ArrayList<>(Arrays.asList("Friday", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"));
			scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "10:00AM","10:00PM");
			scheduleMainPage.clickCancelBtnOnEditOpeHoursPage();
			scheduleMainPage.checkOperatingHoursOnToggleSummary();
			scheduleMainPage.goToEditOperatingHoursView();
			scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "10:00AM","10:00PM");
			scheduleMainPage.clickSaveBtnOnEditOpeHoursPage();
			scheduleMainPage.checkOpeHrsOfParticualrDayOnToggleSummary(weekDays, "10AM-10PM");

			//Check the time duration on the day view
			scheduleMainPage.goToToggleSummaryView();
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdate();
			scheduleCommonPage.clickOnDayView();
			ArrayList<String> timeDurations = scheduleShiftTablePage.getScheduleDayViewGridTimeDuration();
			String timeDuration = timeDurations.get(0) + "-" + timeDurations.get(timeDurations.size()-1);
			SimpleUtils.assertOnFail("The time duration is not matched between day view and toggle summary view!", timeDuration.equalsIgnoreCase("8 AM-12 AM"), false);

			//Close operating days, regenerate the schedule.
			scheduleMainPage.goToToggleSummaryView();
			scheduleMainPage.goToEditOperatingHoursView();
			scheduleMainPage.closeTheParticularOperatingDay(weekDays);
			scheduleMainPage.clickSaveBtnOnEditOpeHoursPage();
			scheduleMainPage.checkClosedDayOnToggleSummary(weekDays);
			scheduleMainPage.goToToggleSummaryView();
			Thread.sleep(3000);

			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdate();

			//Check the closed operating day.
			scheduleCommonPage.clickOnWeekView();
			int shiftCount = scheduleShiftTablePage.getShiftsCount();
			SimpleUtils.assertOnFail("The schedule is not empty!", shiftCount == 0, false);
			scheduleCommonPage.clickOnDayView();
			SimpleUtils.assertOnFail("The current day is not closed!", scheduleCommonPage.isStoreClosedForActiveWeek(), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		} finally{
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			goToSchedulePageScheduleTab();
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			scheduleMainPage.goToEditOperatingHoursView();
			List<String> weekDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
			scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "07:00AM","09:00PM");
			scheduleMainPage.clickSaveBtnOnEditOpeHoursPage();
			scheduleMainPage.checkOpeHrsOfParticualrDayOnToggleSummary(weekDays, "7AM-9PM");
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify shifts should have \"Outside Operating hours\" violation when reducing the operating hours")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheOutOpeHrsViolationAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

			//Go to the schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Edit the operating hours for the current week.
			scheduleMainPage.goToToggleSummaryView();
			scheduleMainPage.goToEditOperatingHoursView();
			List<String> weekDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
			scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "06:00AM","08:00AM");
			scheduleMainPage.clickSaveBtnOnEditOpeHoursPage();
			scheduleMainPage.checkOpeHrsOfParticualrDayOnToggleSummary(weekDays, "6AM-8AM");

			//Refresh the schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();

			//Check the Outside Operating Hours violation on the shifts
			List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
			SimpleUtils.assertOnFail("The Outside Operating Hours violation is not shown!", complianceMessage.contains("Outside Operating hours"), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		} finally{
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00am", "06:00am");
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Weekly OT violation when config is turned on")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTurnOnWeekOTViolationAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			String tmPartialName = "WMTester";
			String workRole;
//			if (isLocationUsingControlsConfiguration){
//				//Go to compliance page and set week OT violation
//				workRole = "Retail Associate";
//				controlsNewUIPage.clickOnControlsConsoleMenu();
//				controlsNewUIPage.clickOnControlsComplianceSection();
//				compliancePage.turnOnOrTurnOffWeeklyOTToggle(true);
//				compliancePage.editWeeklyOTSetting("40 hours");
//			}else {
				workRole = "TEAM MEMBER CORPORATE-THEATRE";
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToSubLocationsInLocationsPage();
				locationsPage.searchLocation(location);
//				SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
				locationsPage.clickOnLocationInLocationResult(location);
				locationsPage.clickOnConfigurationTabOfLocation();
				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
				configurationPage.goToConfigurationPage();
				configurationPage.clickOnConfigurationCrad("Compliance");
				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
				Thread.sleep(10000);
				compliancePage.turnOnOrTurnOffWeeklyOTToggle(true);
				compliancePage.editWeeklyOTSetting("40");
				configurationPage.publishNowTheTemplate();
				switchToConsoleWindow();
//			}
			refreshCachesAfterChangeTemplate();
			Thread.sleep(60000);

			//Go to the schedule view table
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Delete all auto-generated shifts for the TM
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("WMTester C.");
			scheduleMainPage.saveSchedule();

			//Create multiple shifts and assign it to the same TM
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(5);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("5:30pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("9:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmPartialName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Create a new shift and assign it to the same TM to trigger the WeeklyOT
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(6,6,6);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("9:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmPartialName);
			newShiftPage.clickClearAssignmentsLink();
			String standardAssignAnyway = "weekly overtime";

			String statusMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
			SimpleUtils.assertOnFail("The Violation message on the Assign dialog is not expected!",
					statusMessage.contains(standardAssignAnyway), false);

			shiftOperatePage.clickAssignBtnOnCreateShiftDialog(tmPartialName);
//			String standardAssignAnyway1 = tmPartialName + " will incur";
//			String standardAssignAnyway2 = "hours of weekly overtime";
			String assignAnywayMessage = newShiftPage.getWarningMessageFromWarningModal();
			SimpleUtils.assertOnFail("The Violation message on the AssignAnyway page is not expected!", assignAnywayMessage.contains(standardAssignAnyway), false);
			shiftOperatePage.clickOnAssignAnywayButton();
			newShiftPage.clickOnOfferOrAssignBtn();
			newShiftPage.clickOnOkButtonOnErrorDialog();
			scheduleMainPage.saveSchedule();

			//Verify the WeekOT violation after saving.
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
			List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(5));
			for(int i = 0; i< complianceMessage.size(); i++){
				if (complianceMessage.get(i).contains("weekly overtime")) {
					SimpleUtils.pass("The Weekly OT message is showing correctly!");
					break;
				}if (i == complianceMessage.size()-1){
					SimpleUtils.fail("The Weekly OT message is not shown!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Weekly OT violation when config is turned off")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTurnOffWeekOTViolationAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			String tmPartialName = "WMTester";
			String workRole;
//			if (isLocationUsingControlsConfiguration){
//				//Go to compliance page and set week OT violation
//				workRole = "Retail Associate";
//				controlsNewUIPage.clickOnControlsConsoleMenu();
//				controlsNewUIPage.clickOnControlsComplianceSection();
//				compliancePage.turnOnOrTurnOffWeeklyOTToggle(false);
//				compliancePage.editWeeklyOTSetting("40 hours");
//			}else {
				workRole = "TEAM MEMBER CORPORATE-THEATRE";
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				locationsPage.clickOnLocationsTab();
				locationsPage.goToSubLocationsInLocationsPage();
				locationsPage.searchLocation(location);
//				SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
				locationsPage.clickOnLocationInLocationResult(location);
				locationsPage.clickOnConfigurationTabOfLocation();
				HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
				ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
				configurationPage.goToConfigurationPage();
				configurationPage.clickOnConfigurationCrad("Compliance");
				configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
				configurationPage.clickOnEditButtonOnTemplateDetailsPage();
				Thread.sleep(3000);
				compliancePage.turnOnOrTurnOffWeeklyOTToggle(false);
				configurationPage.publishNowTheTemplate();
				switchToConsoleWindow();
//			}
			refreshCachesAfterChangeTemplate();
			Thread.sleep(60000);

			//Go to the schedule view table
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Delete all auto-generated shifts for the TM
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("WMTester C.");
			scheduleMainPage.saveSchedule();

			//Create multiple shifts and assign it to the same TM
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(5);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("5:30pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("9:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.inputTeamMember(tmPartialName);
			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmPartialName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Create a new shift and assign it to the same TM to trigger the WeeklyOT
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(6,6,6);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("9:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.inputTeamMember(tmPartialName);
			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmPartialName);
			newShiftPage.clickClearAssignmentsLink();

			String statusMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
			SimpleUtils.assertOnFail("The Violation message on the Assign dialog is not expected!",
					!(statusMessage.contains("Will trigger 1Hrs weekly OT")), false);

			newShiftPage.searchTeamMemberByName(tmPartialName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Verify the WeekOT violation after saving.
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
			List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(5));
			for(int i = 0; i< complianceMessage.size(); i++){
				if (!(complianceMessage.get(i).contains("weekly overtime"))) {
//					SimpleUtils.pass("The Weekly OT message is not showing!");
					continue;
				}
				else{
					SimpleUtils.fail("The Weekly OT is showing!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the View Team Schedule permission displays")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyTheViewTeamSchedulePermissionDisplayAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOn = "on";
			String actionOff = "off";
//			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//			if (isLocationUsingControlsConfiguration){
//				//Go to Users and Roles page
//				controlsNewUIPage.clickOnControlsConsoleMenu();
//				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
//				controlsNewUIPage.clickOnGlobalLocationButton();
//				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
//
//				//Check the View Team Schedule permission for SM and TM
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
//
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOff);
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
//
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
//
//			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Check the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section,permission,actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());

				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section,permission,actionOff);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());

				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section,permission,actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				switchToConsoleWindow();
//			}
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		} finally {
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOn = "on";
//			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//			if (isLocationUsingControlsConfiguration){
//				//Go to Users and Roles page
//				controlsNewUIPage.clickOnControlsConsoleMenu();
//				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
//				controlsNewUIPage.clickOnGlobalLocationButton();
//				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
//
//				//Add the View Team Schedule permission for SM and TM
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
//				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
//				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
//			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section,permission,actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
//			}
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the View Team Schedule permission after been turned off")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyTheViewTeamSchedulePermissionTurnOffAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOff = "off";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Remove the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOff);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());

			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Remove the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section,permission,actionOff);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				switchToConsoleWindow();
			}
			refreshCachesAfterChangeTemplate();

			///Log in as store manager, check the Team Schedule sub tab
			loginPage.logOut();
			Thread.sleep(300000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			dashboardPage.clickOnProfileIconOnDashboard();
			dashboardPage.clickOnSwitchToEmployeeView();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("The View Team Schedule tab is displayed!", !(scheduleCommonPage.verifyActivatedSubTab("Team Schedule")), false);

			///Log in as TM, check the Team Schedule sub tab
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("The View Team Schedule tab is displayed!", !(scheduleCommonPage.verifyActivatedSubTab("Team Schedule")), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		} finally {
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOn = "on";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());

			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
			}
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the View Team Schedule permission after been turned on")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyTheViewTeamSchedulePermissionTurnOnAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOn = "on";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				switchToConsoleWindow();
			}
			refreshCachesAfterChangeTemplate();
			Thread.sleep(100000);
			refreshCachesAfterChangeTemplate();

			///Log in as store manager, check the Team Schedule sub tab
			String subTab = "Team Schedule";
			loginPage.logOut();
			Thread.sleep(200000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			dashboardPage.clickOnProfileIconOnDashboard();
			dashboardPage.clickOnSwitchToEmployeeView();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(subTab);
			SimpleUtils.assertOnFail("The View Team Schedule tab is not displayed!", scheduleCommonPage.verifyActivatedSubTab(subTab), false);

			///Log in as TM, check the Team Schedule sub tab
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(subTab);
			SimpleUtils.assertOnFail("The View Team Schedule tab is not displayed!", scheduleCommonPage.verifyActivatedSubTab(subTab), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the users won't affect each other with different View Team Schedule permission")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyTheUsersWithDifferentViewTeamSchedulePermissionAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOn = "on";
			String actionOff = "off";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Remove the View Team Schedule permission for TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(300000);
			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Remove the View Team Schedule permission for TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(300000);
				closeCurrentWindow();
				switchToConsoleWindow();
			}

			///Log in as Store Manager, check the Team Schedule sub tab
			String subTab = "Team Schedule";
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			dashboardPage.clickOnProfileIconOnDashboard();
			dashboardPage.clickOnSwitchToEmployeeView();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(subTab);
			SimpleUtils.assertOnFail("The View Team Schedule tab is displayed!", !(scheduleCommonPage.verifyActivatedSubTab(subTab)), false);

			///Log in as Team Member, check the Team Schedule sub tab
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			dashboardPage.clickOnProfileIconOnDashboard();
			dashboardPage.clickOnSwitchToEmployeeView();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(subTab);
			SimpleUtils.assertOnFail("The View Team Schedule tab is displayed!", !(scheduleCommonPage.verifyActivatedSubTab(subTab)), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}finally {
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOn = "on";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(10000);

			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission permission for SM and TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(10000);
			}
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Swap/Cover works fine when turn off View Team Schedule permission")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyTheCoverSwapWorkingWithoutViewTeamPermissionAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOff = "off";
			String teamMember = null;
			String workRole = null;
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				teamMember = "Danyka Walsh";
				workRole = "Retail Associate";
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Remove the View Team Schedule permission for TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(300000);
			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				teamMember = "Brandon Tackett";
				workRole = "TEAM MEMBER CORPORATE-THEATRE";
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Remove the Manage Budget permission for TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(300000);

				switchToConsoleWindow();
			}

			//Create a new schedule
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Delete all auto-generated shifts for the particular TM
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMember);
			scheduleMainPage.saveSchedule();

			//Create a new shift and assign it to the particular TM
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(teamMember);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			///Re-login as TM, check the cover/swap functions
			String subTab = "Team Schedule";
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			SimpleUtils.assertOnFail("The View Team Schedule tab is displayed!", !(scheduleCommonPage.verifyActivatedSubTab(subTab)), false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			Thread.sleep(3000);

			// For Swap Feature
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			List<String> swapCoverRequests = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
			mySchedulePage.clickOnShiftByIndex(0);
			String swapTitle = "Find Shifts to Swap";
			mySchedulePage.clickTheShiftRequestByName(swapCoverRequests.get(0));
			SimpleUtils.assertOnFail(swapTitle + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(swapTitle), false);
			mySchedulePage.verifyClickCancelSwapOrCoverRequest();

			// For Cover Feature
			mySchedulePage.clickOnShiftByIndex(0);
			String coverTitle = "Submit Cover Request";
			mySchedulePage.clickTheShiftRequestByName(swapCoverRequests.get(1));
			SimpleUtils.assertOnFail(coverTitle + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(coverTitle), false);
			mySchedulePage.verifyClickCancelSwapOrCoverRequest();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}finally {
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();

			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			String accessRoleTab = "Access Roles";
			String section = "Schedule";
			String permission = "View Team Schedule";
			String role = "Team Member";
			String actionOn = "on";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(10000);
			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the View Team Schedule permission for TM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(section, role, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				Thread.sleep(10000);
			}
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the TM shown under Recommend tab")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyRecommendTabHasTMsAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Create a schedule if there isn't
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Catch one random shift
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			String firstNameOfTM = null;
			String workRole = null;
			List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			firstNameOfTM = shiftInfo.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM = shiftInfo.get(0);
				shiftCount1++;
				}
			workRole = shiftInfo.get(4);

			//Create an open shift, then offer it to the TMs
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			shiftOperatePage.deleteAllShiftsInWeekView();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
			scheduleMainPage.saveSchedule();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
//			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
//			scheduleMainPage.publishOrRepublishSchedule();

			EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage("Open");
			scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
			String action = "Edit";
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
			editShiftPage.clickOnAssignmentSelect();
			editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption());
			editShiftPage.clickOnUpdateButton();
//			scheduleMainPage.saveSchedule();
//
//			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
//			shiftOperatePage.clickOnOfferTMOption();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			SimpleUtils.assertOnFail("Recommended tab is empty!", newShiftPage.isRecommendedTabHasTMs(), false);
			newShiftPage.clickCloseBtnForOfferShift();
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//			shiftOperatePage.deleteAllShiftsInWeekView();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			//Create a new shift which using the TM name & role above, check the recommend tab
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			SimpleUtils.assertOnFail("Recommended tab is empty!", newShiftPage.isRecommendedTabHasTMs(), false);
			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up one exist shift and assign it to other TM, check the recommend tab
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
			editShiftPage.clickOnAssignmentSelect();
			editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption());
			editShiftPage.clickOnUpdateButton();
//			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
//			shiftOperatePage.clickonAssignTM();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			SimpleUtils.assertOnFail("Recommended tab is empty!", newShiftPage.isRecommendedTabHasTMs(), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}



	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify 'Please confirm' messages on warning modal when there are more than one warnings")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyPleaseConfirmMessageOnWarningModalAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			String firstNameOfTM = shiftInfo1.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM  = shiftInfo1.get(0);
				shiftCount1++;
			}
			String lastName = shiftInfo1.get(5);
			String workRole =  shiftInfo1.get(4);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			Thread.sleep(3000);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			createShiftsWithSpecificValues(workRole, "", "", "6am", "2pm",
					1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
					"", firstNameOfTM+ " "+lastName);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("2:15pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchWithOutSelectTM(firstNameOfTM+ " "+ lastName);
			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
			String expectedMessage = "Please confirm";
			if(newShiftPage.ifWarningModeDisplay()){
				String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
				int count = getCharactersCount(warningMessage, expectedMessage);
				if (count==1){
					SimpleUtils.pass(expectedMessage+ " message displays");
				} else {
					SimpleUtils.fail("The warning message display incorrectly, the actual message is:"+warningMessage, false);
				}
				shiftOperatePage.clickOnAssignAnywayButton();
			} else {
				SimpleUtils.fail("There is no warning modal displaying!",false);
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the currency signs align with country United Kingdom")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheUKCurrencySignDisplayAlignWithCountryAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			String option = "Wages";
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			Thread.sleep(15000);
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToGlobalConfigurationInLocations();
			scheduleMainPage.clickOnEditButton();
			configurationPage.updateLaborPreferencesForForecastSummarySmartcardSettingDropdownOption(option);
			Thread.sleep(3000);
			controlsNewUIPage.clickOnSaveBtn();

			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.goToLocationDetailsPage(location);
			controlsNewUIPage.clickOnLocationProfileEditLocationBtn();

			//Change location's country to England
			locationsPage.modifyLocationCountry("United Kingdom","England","England");
			locationsPage.clickOnSaveButton();
			Thread.sleep(5000);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);

			//Go to schedule and un-generate the schedule
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);

			//Go to the forecast labor tab
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Forecast.getValue()), false);

			//Edit the budget, check the currency signs
			refreshPage();
			forecastPage.goToForecastLaborWeek();
			forecastPage.editLaborBudgetOnSummarySmartCard();
			ArrayList <String> wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("£")){
					SimpleUtils.report("The currency sign of wage is £!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not £!",false);
				}
			}

			//Check the currency sign under day tab of Forecast
			forecastPage.goToForecastLaborDay();
			wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("£")){
					SimpleUtils.report("The currency sign of wage is £!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not £!",false);
				}
			}

			//Go to schedule page, check the wages
			goToSchedulePageScheduleTab();
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("£")){
					SimpleUtils.report("The currency sign of wage is £!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not £!",false);
				}
			}

			scheduleCommonPage.clickOnDayView();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("£")){
					SimpleUtils.report("The currency sign of wage is £!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not £!",false);
				}
			}

			//Go to Team profile page, check the currency sign of Hourly Rate
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName("Brandon Tackett");
			teamPage.isProfilePageLoaded();
			String hourlyRate = teamPage.getTextOfHourlyRate();
			SimpleUtils.assertOnFail("Hourly Rate doesn't include currency sign £!",hourlyRate.contains("£"),false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the currency signs align with country Canada")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheCanadaCurrencySignDisplayAlignWithCountryAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			String option = "Wages";
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToGlobalConfigurationInLocations();
			scheduleMainPage.clickOnEditButton();
			configurationPage.updateLaborPreferencesForForecastSummarySmartcardSettingDropdownOption(option);
			Thread.sleep(3000);
			controlsNewUIPage.clickOnSaveBtn();

			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.goToLocationDetailsPage(location);
			controlsNewUIPage.clickOnLocationProfileEditLocationBtn();

			//Change location's country to England
			locationsPage.modifyLocationCountry("Canada","Alberta", "Alberta");
			locationsPage.clickOnSaveButton();
			Thread.sleep(5000);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);

			//Go to schedule and un-generate the schedule
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);

			//Go to the forecast labor tab
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Forecast.getValue()), false);

			//Edit the budget, check the currency signs
			refreshPage();
			forecastPage.goToForecastLaborWeek();
			forecastPage.editLaborBudgetOnSummarySmartCard();
			ArrayList <String> wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("$")){
					SimpleUtils.report("The currency sign of wage is $!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not $!",false);
				}
			}

			//Check the currency sign under day tab of Forecast
			forecastPage.goToForecastLaborDay();
			wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("$")){
					SimpleUtils.report("The currency sign of wage is $!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not $!",false);
				}
			}

			//Go to schedule page, check the wages
			goToSchedulePageScheduleTab();
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("$")){
					SimpleUtils.report("The currency sign of wage is $!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not $!",false);
				}
			}

			scheduleCommonPage.clickOnDayView();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("$")){
					SimpleUtils.report("The currency sign of wage is $!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not $!",false);
				}
			}

			//Go to Team profile page, check the currency sign of Hourly Rate
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName("Brandon Tackett");
			teamPage.isProfilePageLoaded();
			String hourlyRate = teamPage.getTextOfHourlyRate();
			SimpleUtils.assertOnFail("Hourly Rate doesn't include currency sign $!",hourlyRate.contains("$"),false);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the currency signs align with country Ireland")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheIrelandCurrencySignDisplayAlignWithCountryAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			String option = "Wages";
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToGlobalConfigurationInLocations();
			scheduleMainPage.clickOnEditButton();
			configurationPage.updateLaborPreferencesForForecastSummarySmartcardSettingDropdownOption(option);
			Thread.sleep(3000);
			controlsNewUIPage.clickOnSaveBtn();

			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.goToLocationDetailsPage(location);
			controlsNewUIPage.clickOnLocationProfileEditLocationBtn();

			//Change location's country to England
			locationsPage.modifyLocationCountry("Ireland","Ulster","Ulster");
			locationsPage.clickOnSaveButton();
			Thread.sleep(5000);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);

			//Go to schedule and un-generate the schedule
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			goToSchedulePageScheduleTab();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);

			//Go to the forecast labor tab
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Forecast.getValue()), false);

			//Edit the budget, check the currency signs
			refreshPage();
			forecastPage.goToForecastLaborWeek();
			forecastPage.editLaborBudgetOnSummarySmartCard();
			ArrayList <String> wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("€")){
					SimpleUtils.report("The currency sign of wage is €!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not €!",false);
				}
			}

			//Check the currency sign under day tab of Forecast
			forecastPage.goToForecastLaborDay();
			wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("€")){
					SimpleUtils.report("The currency sign of wage is €!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not €!",false);
				}
			}

			//Go to schedule page, check the wages
			goToSchedulePageScheduleTab();
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("€")){
					SimpleUtils.report("The currency sign of wage is €!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not €!",false);
				}
			}

			scheduleCommonPage.clickOnDayView();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("€")){
					SimpleUtils.report("The currency sign of wage is €!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not €!",false);
				}
			}

			//Go to Team profile page, check the currency sign of Hourly Rate
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName("Brandon Tackett");
			teamPage.isProfilePageLoaded();
			String hourlyRate = teamPage.getTextOfHourlyRate();
			SimpleUtils.assertOnFail("Hourly Rate doesn't include currency sign €!",hourlyRate.contains("€"),false);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the currency signs align with country South Africa")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheSouthAfricaCurrencySignDisplayAlignWithCountryAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			String option = "Wages";
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToGlobalConfigurationInLocations();
			scheduleMainPage.clickOnEditButton();
			configurationPage.updateLaborPreferencesForForecastSummarySmartcardSettingDropdownOption(option);
			Thread.sleep(3000);
			controlsNewUIPage.clickOnSaveBtn();

			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.goToLocationDetailsPage(location);
			controlsNewUIPage.clickOnLocationProfileEditLocationBtn();

			//Change location's country to England
			locationsPage.modifyLocationCountry("South Africa","Eastern Cape","Eastern Cape");
			locationsPage.clickOnSaveButton();
			Thread.sleep(5000);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);

			//Go to schedule and un-generate the schedule
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);

			//Go to the forecast labor tab
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Forecast.getValue()), false);

			//Edit the budget, check the currency signs
			refreshPage();
			forecastPage.goToForecastLaborWeek();
			forecastPage.editLaborBudgetOnSummarySmartCard();
			ArrayList <String> wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("R")){
					SimpleUtils.report("The currency sign of wage is R!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not R!",false);
				}
			}

			//Check the currency sign under day tab of Forecast
			forecastPage.goToForecastLaborDay();
			wages = forecastPage.getTextOfLaborWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("R")){
					SimpleUtils.report("The currency sign of wage is R!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not R!",false);
				}
			}

			//Go to schedule page, check the wages
			goToSchedulePageScheduleTab();
			createSchedulePage.createScheduleForNonDGFlowNewUI();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("R")){
					SimpleUtils.report("The currency sign of wage is R!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not R!",false);
				}
			}

			scheduleCommonPage.clickOnDayView();
			wages = forecastPage.getTextOfScheduleWages();
			for (int i = 0; i < wages.size(); i++){
				if (wages.get(i).contains("R")){
					SimpleUtils.report("The currency sign of wage is R!");
				}else{
					SimpleUtils.fail("The currency sign of wage is not R!",false);
				}
			}

			//Go to Team profile page, check the currency sign of Hourly Rate
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName("Brandon Tackett");
			teamPage.isProfilePageLoaded();
			String hourlyRate = teamPage.getTextOfHourlyRate();
			SimpleUtils.assertOnFail("Hourly Rate doesn't include currency sign R!",hourlyRate.contains("R"),false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the message of schedule version won't change for unmodified Save")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyTheScheduleVersionUnchangedWithUnmodifiedSaveAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			AnalyzePage analyzePage = pageFactory.createAnalyzePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Go to OP page
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.searchLocation(location);               ;
			SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
			locationsPage.clickOnLocationInLocationResult(location);
			locationsPage.clickOnConfigurationTabOfLocation();
			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();

			String option = "Yes, all unassigned shifts";
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Schedule Collaboration"));
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			configurationPage.updateConvertUnassignedShiftsToOpenWhenCreatingScheduleSettingOption(option);
			configurationPage.updateConvertUnassignedShiftsToOpenWhenCopyingScheduleSettingOption(option);
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);


			//Go to schedule page, create a new schedule, modify the schedule and publish
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Catch up on random shift for further shift creation
			String firstNameOfTM = null;
			List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
			firstNameOfTM = shiftInfo.get(0);
			int shiftCount1 = 0;
			while ((firstNameOfTM.equalsIgnoreCase("open")
					|| firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
				shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
				firstNameOfTM = shiftInfo.get(0);
				shiftCount1++;
			}

			//Publish the schedule
			scheduleMainPage.publishOrRepublishSchedule();
			Thread.sleep(3000);

			//Delete all relevant shifts of particular TM
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();

			//Check the schedule version
			String analyzeTab = "Schedule History";
			String scheduleVersion = "1.1";
			analyzePage.clickOnAnalyzeBtn(analyzeTab);
			analyzePage.verifyScheduleVersion(scheduleVersion);
			analyzePage.closeAnalyzeWindow();

			//Edit the schedule again and save without any changes, check the pop up message
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.saveScheduleWithoutChange();

			//Check the schedule version
			analyzePage.clickOnAnalyzeBtn(analyzeTab);
			analyzePage.verifyScheduleVersion(scheduleVersion);
			analyzePage.closeAnalyzeWindow();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Confirm Operating Hours dialog after turn on permission")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyConfirmOpeHrsDisplayWhenManageWorkingHrsPermissionTurnOnAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			String accessRoleTab = "Access Roles";
			String section = "Controls";
			String permission = "Manage Working Hours Settings";
			String actionOn = "on";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Working Hours Settings permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());

			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Working Hours Settings permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section,permission,actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				switchToConsoleWindow();
			}
			refreshCachesAfterChangeTemplate();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(60000);
			refreshCachesAfterChangeTemplate();

			///Log in as store manager, check the Confirm Operating Hours dialog during shift creation
			loginPage.logOut();
			Thread.sleep(300000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			createSchedulePage.clickCreateScheduleBtn();
			boolean isConfirmOpeHrsDialogShows = createSchedulePage.verifyTheConfirmOperatingHoursWindowShows(location);
			SimpleUtils.assertOnFail("Confirm Operating Hours dialog is not displayed!", isConfirmOpeHrsDialogShows, false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Confirm Operating Hours dialog after turn off permission")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void VerifyConfirmOpeHrsDisplayWhenManageWorkingHrsPermissionTurnOffAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			String accessRoleTab = "Access Roles";
			String section = "Controls";
			String permission = "Manage Working Hours Settings";
			String actionOff = "off";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Working Hours Settings permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());

			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Working Hours Settings permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section,permission,actionOff);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				switchToConsoleWindow();
			}

			refreshCachesAfterChangeTemplate();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(60000);
			refreshCachesAfterChangeTemplate();

			///Log in as store manager, check the Confirm Operating Hours dialog during shift creation
			loginPage.logOut();
			Thread.sleep(300000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			createSchedulePage.clickCreateScheduleBtn();
			boolean isConfirmOpeHrsDialogShows = createSchedulePage.verifyTheConfirmOperatingHoursWindowShows(location);
			SimpleUtils.assertOnFail("Confirm Operating Hours dialog is displayed!", !(isConfirmOpeHrsDialogShows), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		} finally {
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			refreshPage();
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			String accessRoleTab = "Access Roles";
			String section = "Controls";
			String permission = "Manage Working Hours Settings";
			String actionOn = "on";
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			if (isLocationUsingControlsConfiguration){
				//Go to Users and Roles page
				controlsNewUIPage.clickOnControlsConsoleMenu();
				controlsNewUIPage.clickOnControlsUsersAndRolesSection();
				controlsNewUIPage.clickOnGlobalLocationButton();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Working Hours Settings permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());

			}else {
				//Go to Users and Roles page and switch to the Access Roles sub tab
				LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
				locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
				SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Manage Working Hours Settings permission for SM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(section, permission, actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
			}
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the scheduled hrs on schedule smart card will changed accordingly by select filter in week view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateTheScheduledHrsOnScheduleSmartCardWillChangedAccordinglyBySelectFilterInWeekViewAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();

			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "09:00pm");
			//Get total scheduled hrs and wages

			HashMap<String, Float> scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
			float totalScheduledHrs = scheduleHoursForFirstSchedule.get("scheduledHours");

			//Select every shift type and get the scheduled hrs
			List<String> allShiftType = scheduleMainPage.getSpecificFilterNames("Shift Type");
			SimpleUtils.assertOnFail("Fail to get shift types! ", allShiftType.size()>0, false);
			float scheduledHrsForEveryOptions = 0;
			float scheduledHrsForOpenShifts = 0;
			for (String shiftType : allShiftType){
				scheduleMainPage.selectShiftTypeFilterByText(shiftType);
				if (shiftType.equalsIgnoreCase("Assigned")
						|| shiftType.equalsIgnoreCase("Open") ){
					if (shiftType.equalsIgnoreCase("Open")){
						scheduledHrsForOpenShifts = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
					}
					float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");

					SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
									+", the scheduled hrs for every shift type option is "+scheduledHrs,
							scheduledHrs <= totalScheduledHrs, false);
					scheduledHrsForEveryOptions = scheduledHrsForEveryOptions + scheduledHrs;
					SimpleUtils.pass("Get "+shiftType+"'s scheduled hrs successfully! ");
				} else{
					float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
					SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
									+", the scheduled hrs for every shift type option is "+scheduledHrs,
							scheduledHrs < totalScheduledHrs, false);
				}
			}
			SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
					+", the scheduled hrs for every shift type option is "+scheduledHrsForEveryOptions,
					scheduledHrsForEveryOptions ==totalScheduledHrs, false);
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			//Select every job title and get the scheduled hrs
			List<String> allJobTitles = scheduleMainPage.getSpecificFilterNames("JOB TITLE");
			SimpleUtils.assertOnFail("Fail to get JOB TITLEs! ", allJobTitles.size()>0, false);
			scheduledHrsForEveryOptions = 0;
			for (String jobTitle : allJobTitles){
				scheduleMainPage.selectJobTitleFilterByText(jobTitle);
				float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
				SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
								+", the scheduled hrs for every job title option is "+scheduledHrs,
						scheduledHrs < totalScheduledHrs, false);
				scheduledHrsForEveryOptions = scheduledHrsForEveryOptions + scheduledHrs;
				SimpleUtils.pass("Get "+jobTitle+"'s scheduled hrs successfully! ");
			}
			scheduledHrsForEveryOptions = scheduledHrsForEveryOptions+scheduledHrsForOpenShifts;
			SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
							+", the scheduled hrs for every job title option is "+scheduledHrsForEveryOptions,
					scheduledHrsForEveryOptions ==totalScheduledHrs, false);

			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			//Select every work role and get the scheduled hrs
			List<String> allWorkRoles = scheduleMainPage.getSpecificFilterNames("Work Role");
			SimpleUtils.assertOnFail("Fail to get Work Roles! ", allWorkRoles.size()>0, false);
			scheduledHrsForEveryOptions = 0;
			for (String workRole : allWorkRoles){
				scheduleMainPage.selectWorkRoleFilterByText(workRole, true);
				float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
				SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
								+", the scheduled hrs for every work role option is "+scheduledHrs,
						scheduledHrs < totalScheduledHrs, false);
				scheduledHrsForEveryOptions = scheduledHrsForEveryOptions + scheduledHrs;
				SimpleUtils.pass("Get "+workRole+"'s scheduled hrs successfully! ");
			}
			SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
							+", the scheduled hrs for every work role option is "+scheduledHrsForEveryOptions,
					scheduledHrsForEveryOptions ==totalScheduledHrs, false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the scheduled hrs on schedule smart card will changed accordingly by select filter in day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateTheScheduledHrsOnScheduleSmartCardWillChangedAccordinglyBySelectFilterInDayViewAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();

			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "09:00pm");
			scheduleCommonPage.clickOnDayView();
			//Get total scheduled hrs and wages

			HashMap<String, Float> scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
			float totalScheduledHrs = scheduleHoursForFirstSchedule.get("scheduledHours");

			//Select every shift type and get the scheduled hrs
			List<String> allShiftType = scheduleMainPage.getSpecificFilterNames("Shift Type");
			SimpleUtils.assertOnFail("Fail to get shift types! ", allShiftType.size()>0, false);
			float scheduledHrsForEveryOptions = 0;
			float scheduledHrsForOpenShifts = 0;
			for (String shiftType : allShiftType){
				scheduleMainPage.selectShiftTypeFilterByText(shiftType);
				if (shiftType.equalsIgnoreCase("Assigned")
						|| shiftType.equalsIgnoreCase("Open") ){
					if (shiftType.equalsIgnoreCase("Open")){
						scheduledHrsForOpenShifts = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
					}
					float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
					SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
									+", the scheduled hrs for every shift type option is "+scheduledHrs,
							scheduledHrs <= totalScheduledHrs, false);
					scheduledHrsForEveryOptions = scheduledHrsForEveryOptions + scheduledHrs;
					SimpleUtils.pass("Get "+shiftType+"'s scheduled hrs successfully! ");
				} else{
					float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
					SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
									+", the scheduled hrs for every shift type option is "+scheduledHrs,
							scheduledHrs < totalScheduledHrs, false);
				}
			}
			SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
							+", the scheduled hrs for every shift type option is "+scheduledHrsForEveryOptions,
					scheduledHrsForEveryOptions ==totalScheduledHrs, false);
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			//Select every job title and get the scheduled hrs
			List<String> allJobTitles = scheduleMainPage.getSpecificFilterNames("JOB TITLE");
			SimpleUtils.assertOnFail("Fail to get JOB TITLEs! ", allJobTitles.size()>0, false);
			scheduledHrsForEveryOptions = 0;
			for (String jobTitle : allJobTitles){
				scheduleMainPage.selectJobTitleFilterByText(jobTitle);
				float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
				SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
								+", the scheduled hrs for every job title option is "+scheduledHrs,
						scheduledHrs < totalScheduledHrs, false);
				scheduledHrsForEveryOptions = scheduledHrsForEveryOptions + scheduledHrs;
				SimpleUtils.pass("Get "+jobTitle+"'s scheduled hrs successfully! ");
			}
			scheduledHrsForEveryOptions = scheduledHrsForEveryOptions+scheduledHrsForOpenShifts;
			SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
							+", the scheduled hrs for every job title option is "+scheduledHrsForEveryOptions,
					scheduledHrsForEveryOptions ==totalScheduledHrs, false);

			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			//Select every work role and get the scheduled hrs
			List<String> allWorkRoles = scheduleMainPage.getSpecificFilterNames("Work Role");
			SimpleUtils.assertOnFail("Fail to get Work Roles! ", allWorkRoles.size()>0, false);
			scheduledHrsForEveryOptions = 0;
			for (String workRole : allWorkRoles){
				scheduleMainPage.selectWorkRoleFilterByText(workRole, true);
				float scheduledHrs = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard().get("scheduledHours");
				SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
								+", the scheduled hrs for every work role option is "+scheduledHrs,
						scheduledHrs < totalScheduledHrs, false);
				scheduledHrsForEveryOptions = scheduledHrsForEveryOptions + scheduledHrs;
				SimpleUtils.pass("Get "+workRole+"'s scheduled hrs successfully! ");
			}
			SimpleUtils.assertOnFail("The total scheduled hrs is "+totalScheduledHrs
							+", the scheduled hrs for every work role option is "+scheduledHrsForEveryOptions,
					scheduledHrsForEveryOptions ==totalScheduledHrs, false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate TM can view team schedule after generate and publish schedule")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateTMCanViewTeamScheduleAfterGenerateScheduleAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();

			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "09:00pm");
			createSchedulePage.publishActiveSchedule();
			int shiftCountInScheduleForAdmin = scheduleShiftTablePage.getShiftsCount();
			SimpleUtils.assertOnFail("There is no shifts display in the schedule for admin! ",
					shiftCountInScheduleForAdmin > 0,false);
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			Thread.sleep(3000);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.TeamSchedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			mySchedulePage.selectSchedulFilter("Scheduled");
			int shiftCountInScheduleForTM = scheduleShiftTablePage.getShiftsCount();
			mySchedulePage.selectSchedulFilter("Open");
			shiftCountInScheduleForTM = shiftCountInScheduleForTM+scheduleShiftTablePage.getShiftsCount();
			SimpleUtils.assertOnFail("There is no shifts display in the schedule for admin! ",
					shiftCountInScheduleForTM > 0,false);
			SimpleUtils.assertOnFail("The shift count in schedule for admin is "+shiftCountInScheduleForAdmin+
							" The shift count in schedule for TM is "+shiftCountInScheduleForTM,
					shiftCountInScheduleForTM == shiftCountInScheduleForAdmin, false);

		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate SM can view schedule in manager and employee view after generate and publish schedule")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateSMCanViewScheduleInManagerAndEmployeeViewAfterGenerateScheduleAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();

			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "09:00pm");
			createSchedulePage.publishActiveSchedule();
			int shiftCountInScheduleForSM = scheduleShiftTablePage.getShiftsCount();
			SimpleUtils.assertOnFail("There is no shifts display in the schedule for admin! ",
					shiftCountInScheduleForSM > 0,false);
			Thread.sleep(3000);
			dashboardPage.clickOnProfileIconOnDashboard();
			dashboardPage.clickOnSwitchToEmployeeView();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.TeamSchedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			mySchedulePage.selectSchedulFilter("Scheduled");
			int shiftCountInScheduleForTM = scheduleShiftTablePage.getShiftsCount();
			mySchedulePage.selectSchedulFilter("Open");
			shiftCountInScheduleForTM = shiftCountInScheduleForTM+scheduleShiftTablePage.getShiftsCount();
			SimpleUtils.assertOnFail("There is no shifts display in the schedule for admin! ",
					shiftCountInScheduleForTM > 0,false);
			SimpleUtils.assertOnFail("The shift count in schedule for admin is "+shiftCountInScheduleForSM+
							" The shift count in schedule for TM is "+shiftCountInScheduleForTM,
					shiftCountInScheduleForTM == shiftCountInScheduleForSM, false);

		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}
}