package com.legion.tests.core;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import com.legion.pages.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;

import static com.legion.utils.MyThreadLocal.*;

public class TeamTest extends TestBase{
	
	private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
	private static HashMap<String, String> searchDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/searchDetails.json");
	private static Map<String, String> newTMDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/AddANewTeamMember.json");
	private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
	private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
	private static HashMap<String, String> propertyLocationTimeZone = JsonUtil.getPropertiesFromJsonFile("src/test/resources/LocationTimeZone.json");

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
	
	public enum weekViewType{
		  Next("Next"),
		  Previous("Previous");
			private final String value;
			weekViewType(final String newValue) {
	            value = newValue;
	        }
	        public String getValue() { return value; }
		}

	public enum TeamPageSubTabText {
		Roster("ROSTER"),
		Coverage("COVERAGE"),
		SchoolCalendars("SCHOOL CALENDARS");

		private final String value;

		TeamPageSubTabText(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}
	
	 @Automated(automated = "Manual")
	 @Owner(owner = "Gunjan")
	 @Enterprise(name = "Tech_Enterprise")
	 @TestName(description = "LEG-4978: In Team Page ,Coverage section is not displayed for LegionTech for Nov 4- Nov 10")
	 @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
	 public void coverageForTeamPageNotWorking(String username, String password, String browser, String location)
	          throws Exception
	 {
	       SimpleUtils.pass("Login to leginTech Successfully");
	       SimpleUtils.pass("Successfully opened the Team Page");
	       SimpleUtils.pass("Click on Coverage tab");
	       SimpleUtils.pass("assert coverage page should load and show data");

	  }
	 
	 @Automated(automated = "Automated")
	 @Owner(owner = "Gunjan")
		@Enterprise(name = "Coffee2_Enterprise")
		@TestName(description = "TP-81: Validate Team Search and Coverage in Team Tab")
	    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
	    public void validateTeamTab(String username, String password, String browser, String location)
	            throws Exception
	    {
	    	//To Do Should be separate Test from Schedule test
			//loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
	        TeamPage teamPage = pageFactory.createConsoleTeamPage();
	        teamPage.goToTeam();
	        String key=searchDetails.get("jobTitle");
	        List<String> list = new ArrayList<String>(Arrays.asList(key.split(",")));
	        teamPage.performSearchRoster(list);
	        teamPage.coverage();
	        teamPage.coverageViewToPastOrFuture(weekViewType.Next.getValue(), weekCount.Six.getValue());
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team functionality > In Activate")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInActivateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			String onBoarded = "Onboarded";
			String active = "Active";
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			dashboardPage.isDashboardPageLoaded();
			String currentDay = dashboardPage.getCurrentDateFromDashboard();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.verifyTheFunctionOfAddNewTeamMemberButton();
			teamPage.isProfilePageLoaded();
			String firstName = teamPage.addANewTeamMemberToInvite(newTMDetails);
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName(firstName);
			teamPage.isProfilePageLoaded();
			teamPage.isManualOnBoardButtonLoaded();
			teamPage.manualOnBoardTeamMember();
			teamPage.verifyTheStatusOfTeamMember(onBoarded);
			teamPage.isActivateButtonLoaded();
			//String onBoardedDate = teamPage.getOnBoardedDate();
			teamPage.clickOnActivateButton();
			teamPage.isActivateWindowLoaded();
			teamPage.selectADateOnCalendarAndActivate();
			// Verify While activating team Member, On boarded date is updating to new one and Deactivate & terminate button is enabled
			teamPage.verifyDeactivateAndTerminateEnabled();
			// TODO: To check whether the on boarded date should update or not
			//teamPage.isOnBoardedDateUpdated(onBoardedDate);
			// Verify Status will change into Activate status according to date
			teamPage.verifyTheStatusOfTeamMember(active);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.goToSchedulePage();
			scheduleCommonPage.isSchedulePage();
			scheduleCommonPage.goToSchedule();
			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			if (!createSchedulePage.isWeekGenerated()) {
				createSchedulePage.generateOrUpdateAndGenerateSchedule();
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButton();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectDaysFromCurrentDay(currentDay);
			newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleCommonPage.clickOnWeekView();
			// Verify Shifts will go to Auto Scheduling  after Activating any TM
			scheduleShiftTablePage.verifyNewShiftsAreShownOnSchedule(firstName);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team functionality > In Terminate")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInTerminateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			String timeZone = "";
			String active = "Active";
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			dashboardPage.isDashboardPageLoaded();
			String currentDay = dashboardPage.getCurrentDateFromDashboard();
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			if (controlsNewUIPage.isControlsPageLoaded()) {
				controlsNewUIPage.clickOnControlsLocationProfileSection();
				if (controlsNewUIPage.isControlsLocationProfileLoaded()) {
					timeZone = controlsNewUIPage.getTimeZoneFromLocationDetailsPage();
				}
			}
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			// Create a new team member
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.verifyTheFunctionOfAddNewTeamMemberButton();
			teamPage.isProfilePageLoaded();
			String firstName = teamPage.addANewTeamMemberToInvite(newTMDetails);
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName(firstName);
			teamPage.isProfilePageLoaded();
			teamPage.isManualOnBoardButtonLoaded();
			teamPage.manualOnBoardTeamMember();
			teamPage.isActivateButtonLoaded();
			teamPage.clickOnActivateButton();
			teamPage.isActivateWindowLoaded();
			teamPage.selectADateOnCalendarAndActivate();
			teamPage.verifyTheStatusOfTeamMember(active);

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			// Go to schedule page, assign shift to the new team member
			scheduleCommonPage.goToSchedulePage();
			scheduleCommonPage.isSchedulePage();
			scheduleCommonPage.goToSchedule();
			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			if (!createSchedulePage.isWeekGenerated()) {
				createSchedulePage.generateOrUpdateAndGenerateSchedule();
			}
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButton();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectDaysFromCurrentDay(currentDay);
			newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleCommonPage.clickOnWeekView();
			scheduleShiftTablePage.verifyNewShiftsAreShownOnSchedule(firstName);
			scheduleMainPage.clickSaveBtn();
			List<Integer> indexes = scheduleShiftTablePage.getAddedShiftIndexes(firstName);
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName(firstName);
			teamPage.isProfilePageLoaded();
			// Verify Cancel Termination button is working, TM will not removed from Roster after click on this button
			boolean isCancel = teamPage.isCancelTerminateButtonLoaded();
			if (!isCancel) {
				teamPage.isTerminateButtonLoaded();
				teamPage.terminateTheTeamMember(false);
			}
			teamPage.verifyTheFunctionOfCancelTerminate();
			String employeeID = teamPage.getEmployeeIDFromProfilePage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchTheTeamMemberByEmployeeIDFromRoster(employeeID, false);
			// Verify While Clicking on Terminate button, particular TM is able to removed from Team roster on the set date
			teamPage.searchAndSelectTeamMemberByName(firstName);
			teamPage.isProfilePageLoaded();
			teamPage.isTerminateButtonLoaded();
			teamPage.terminateTheTeamMember(true);
			String currentTime = SimpleUtils.getCurrentTimeWithTimeZone(timeZone);
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchTheTeamMemberByEmployeeIDFromRoster(employeeID, true);
			// Verify TM's assigned shift is converting to open shift
			scheduleCommonPage.goToSchedulePage();
			scheduleCommonPage.isSchedulePage();
			scheduleCommonPage.goToSchedule();
			scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleShiftTablePage.verifyShiftsChangeToOpenAfterTerminating(indexes, firstName, currentTime);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team Functionality > Time Off")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInTimeOffAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			// Login with Store Manager Credentials
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			// Get the current month, year and date
			String currentMonthYearDate = getTimeZoneFromControlsAndGetDate(location);
			// Set time off policy
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
			controlsNewUIPage.clickOnControlsSchedulingPolicies();
			SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
			controlsNewUIPage.updateCanWorkerRequestTimeOff("Yes");
			controlsNewUIPage.clickOnSchedulingPoliciesTimeOffAdvanceBtn();
			controlsNewUIPage.updateShowTimeOffReasons("Yes");
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			// Login as Team Member
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			String nickName = profileNewUIPage.getNickNameFromProfile();
			String myProfileLabel = "My Profile";
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
			SimpleUtils.assertOnFail("Profile page not loaded Successfully!", profileNewUIPage.isProfilePageLoaded(), false);
			String aboutMeLabel = "About Me";
			profileNewUIPage.selectProfilePageSubSectionByLabel(aboutMeLabel);
			String myTimeOffLabel = "My Time Off";
			profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
			String pendingLabel = "Pending";
			profileNewUIPage.cancelAllTimeOff();
			int previousPendingCount = profileNewUIPage.getTimeOffCountByStatusLabel(pendingLabel);
			// Verify Create Time off button is working
			profileNewUIPage.clickOnCreateTimeOffBtn();
			SimpleUtils.assertOnFail("New time off request window not loaded Successfully!", profileNewUIPage.isNewTimeOffWindowLoaded(), false);
			String timeOffReasonLabel = "JURY DUTY";
			// Verify Reason can be selected
			profileNewUIPage.selectTimeOffReason(timeOffReasonLabel);
			// Verify Calendar is present and date-time showing correct
			profileNewUIPage.verifyCalendarForCurrentAndNextMonthArePresent(currentMonthYearDate);
			// Verify Calendar start n end date and time is correct
			List<String> startNEndDates = profileNewUIPage.selectStartAndEndDate();
			profileNewUIPage.areAllDayCheckboxesLoaded();
			profileNewUIPage.deSelectAllDayCheckboxes();
			profileNewUIPage.verifyStartDateAndEndDateIsCorrect(getTimeOffStartTime(), getTimeOffEndTime());
			profileNewUIPage.verifyTimeIsCorrectAfterDeSelectAllDay();
			// Verify Alignment for AM/PM is correct in starts n end time after deselecting All day checkbox
			profileNewUIPage.verifyAlignmentOfAMAndPMAfterDeSelectAllDay();
			// Verify Time off request can be requested
			profileNewUIPage.clickOnSaveTimeOffRequestBtn();
			Thread.sleep(3000);
			// Verify count of Pending/approved/Rejected is being increased and decreased accordingly
			int currentPendingCount = profileNewUIPage.getTimeOffCountByStatusLabel(pendingLabel);
			if (currentPendingCount - previousPendingCount == 1) {
				SimpleUtils.pass("Pending count is increased!");
			} else {
				SimpleUtils.fail("Pending count doesn't increased 1!", true);
			}
			loginPage.logOut();

			// Login as Store Manager again
			loginToLegionAndVerifyIsLoginDone(username, password, location);
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.openToDoPopupWindow();
			// Verify Info come in TODO list
			int timeOffDays = teamPage.verifyTimeOffRequestShowsOnToDoList(nickName, getTimeOffStartTime(), getTimeOffEndTime());
			teamPage.closeToDoPopupWindow();
			// Verify impacting on Coverage
			teamPage.coverage();
			int previousCount = teamPage.getTimeOffCountByStartAndEndDate(startNEndDates);
			// Approve the time off request, then check the time off days on coverage
			teamPage.openToDoPopupWindow();
			teamPage.approveOrRejectTimeOffRequestFromToDoList(nickName, getTimeOffStartTime(), getTimeOffEndTime(), TeamTestKendraScott2.timeOffRequestAction.Approve.getValue());
			teamPage.goToTeam();
			teamPage.coverage();
			int currentCount = teamPage.getTimeOffCountByStartAndEndDate(startNEndDates);
			if (timeOffDays == (currentCount - previousCount)) {
				SimpleUtils.pass("Time Off days get updated on Coverage");
			} else {
				SimpleUtils.fail("Time Off days on coverage is incorrect, expected days: " + (timeOffDays + previousCount)
						+ ", but actual time off days are: " + currentCount, true);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team Functionality > Coverage")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInCoverageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			String workingHoursType = "Regular";
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			// Get Regular hours from
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsPage.gotoControlsPage();
			SimpleUtils.assertOnFail("Controls page not loaded Successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
			controlsNewUIPage.clickOnControlsWorkingHoursCard();
			SimpleUtils.assertOnFail("Working Hours Card not loaded Successfully!", controlsNewUIPage.isControlsWorkingHoursLoaded(), false);
			controlsNewUIPage.clickOnWorkHoursTypeByText(workingHoursType);
			LinkedHashMap<String, List<String>> regularHours = controlsNewUIPage.getRegularWorkingHours();

			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			HashMap<Integer, String> indexAndTimes = teamPage.generateIndexAndRelatedTimes(regularHours);
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.coverage();
			// Verify 'All Job Title' filter is working, if selecting one/more of those then timeoff is updaing according to that
			String jobTitle = teamPage.selectAJobTitleByRandom();
			SimpleUtils.report("Select the job title: " + jobTitle + " Successfully!");
			String subTab = "Time off";
			teamPage.navigateToSubTabOnCoverage(subTab);
			HashMap<Integer, List<String>> previousTimeOffs = teamPage.getTimeOffWeekTableOnCoverage(indexAndTimes, regularHours);
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName(jobTitle);
			teamPage.isProfilePageLoaded();
			teamPage.navigateToTimeOffPage();

			// Create the time off for the user
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnCreateTimeOffBtn();
			SimpleUtils.assertOnFail("New time off request window not loaded Successfully!", profileNewUIPage.isNewTimeOffWindowLoaded(), false);
			String timeOffReasonLabel = "JURY DUTY";
			profileNewUIPage.selectTimeOffReason(timeOffReasonLabel);
			HashMap<String, List<String>> selectedDateNTime = profileNewUIPage.selectCurrentDayAsStartNEndDate();
			profileNewUIPage.clickOnSaveTimeOffRequestBtn();
			String approvedLabel = "Approved";
			int approvedCount = profileNewUIPage.getTimeOffCountByStatusLabel(approvedLabel);
			if (approvedCount > 0) {
				SimpleUtils.pass("Create the time off request Successfully!");
			} else {
				SimpleUtils.fail("The time off request doesn't approved!", false);
			}

			// Go to coverage to check the time off table
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.coverage();
			teamPage.selectTheJobTitleByName(jobTitle);
			teamPage.navigateToSubTabOnCoverage(subTab);
			HashMap<Integer, List<String>> currentTimeOffs = teamPage.getTimeOffWeekTableOnCoverage(indexAndTimes, regularHours);
			HashMap<Integer, List<String>> expectedTimeOffs = teamPage.getTimeOffWeekTableByDateNTime(
					previousTimeOffs, selectedDateNTime, indexAndTimes);
			if (currentTimeOffs.equals(expectedTimeOffs)) {
				SimpleUtils.pass("Verified Time off table is updated Correctly!");
			} else {
				SimpleUtils.fail("Time Off table updated is incorrectly!", false);
			}

			// Clear Data, reject the time off
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName(jobTitle);
			teamPage.isProfilePageLoaded();
			teamPage.navigateToTimeOffPage();
			teamPage.rejectAllTheTimeOffRequests();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	public String getTimeZoneFromControlsAndGetDate(String location) throws Exception {
		String timeZone = "";
		String currentDate = "";
		timeZone = propertyLocationTimeZone.get(location);
		if (timeZone != null && !timeZone.isEmpty()){
			SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy dd");
			currentDate = SimpleUtils.getCurrentDateMonthYearWithTimeZone(timeZone, format);
		}
		return currentDate;
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the None option displays in Averaging Agreement")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyNoneOptionInAveragingAgreementAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Set split shift violation in the Compliance page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
			String tmName = null;
			if (isLocationUsingControlsConfiguration) {
				//Go to Controls page
				tmName = "Daniel Grant";
			} else {
				//Go to OP page
				tmName = "Aaron Mitchell";
			}

			//Go to the Team page
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchAndSelectTeamMemberByName(tmName);
			teamPage.isProfilePageLoaded();
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);

			//Go to My Work Preference tab, edit the tab, check the None option in the Average Agreement dropdown list
			teamPage.clickOnEditShiftPreference();
			teamPage.selectAverageAgreement("5 x 8");
			teamPage.clickSaveShiftPrefBtn();
			String averageText = "Averaging Agreement: 5 x 8";
			SimpleUtils.assertOnFail("The Averaging Agreement is not 5 x 8!",
					teamPage.getTextOfAverageAgreement().equalsIgnoreCase(averageText),false);

			teamPage.clickOnEditShiftPreference();
			teamPage.selectAverageAgreement("None");
			teamPage.clickSaveShiftPrefBtn();
			averageText = "Averaging Agreement: None";
			SimpleUtils.assertOnFail("The Averaging Agreement is not None!",
					teamPage.getTextOfAverageAgreement().equalsIgnoreCase(averageText),false);

			//Login the TM and verify the shifts on the MySchedule page
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//verify the Average Agreement value is None under the My Shift Preferences section on the MyPreference page
			profileNewUIPage.getNickNameFromProfile();
			String myWorkPreferencesLabel = "My Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(myWorkPreferencesLabel);
			SimpleUtils.assertOnFail("The Averaging Agreement is not None!",
					teamPage.getTextOfAverageAgreement().equalsIgnoreCase(averageText),false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}
}
