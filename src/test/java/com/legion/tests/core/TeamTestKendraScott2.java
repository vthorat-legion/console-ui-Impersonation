package com.legion.tests.core;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import com.legion.pages.*;

import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.pages.core.ConsoleGmailPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.utils.Constants;
import com.legion.utils.MyThreadLocal;
import cucumber.api.java.hu.Ha;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.formula.ptg.ControlPtg;
import org.apache.xpath.operations.Bool;
import org.openqa.selenium.WebElement;
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
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;

import static com.legion.utils.MyThreadLocal.*;

public class TeamTestKendraScott2 extends TestBase{
	
	public enum timeOffRequestAction{
	  Approve("APPROVE"),
	  Reject("REJECT");
		private final String value;
		timeOffRequestAction(final String newValue) {
		    value = newValue;
		}
		public String getValue() { return value; }
	}
	
	public enum timeOffRequestStatus{
		  Approved("APPROVED"),
		  Pending("PENDING"),
		  Cancelled("CANCELLED"),
		  Rejected("REJECTED");
			private final String value;
			timeOffRequestStatus(final String newValue) {
			    value = newValue;
			}
			public String getValue() { return value; }
	}
	
	private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
	private static Map<String, String> searchDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/searchDetails.json");
	private static Map<String, String> newTMDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/AddANewTeamMember.json");
	private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
	private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static HashMap<String, String> imageFilePath = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ProfileImageFilePath.json");
	//	private static HashMap<String, Object[][]> kendraScott2TeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("KendraScott2TeamMembers.json");
	private static HashMap<String, Object[][]> controlTeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("VailqacnTeamMembers.json");
	private static HashMap<String, Object[][]> opTeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("CinemarkWkdyTeamMembers.json");
	private static String controlEnterprice = "Vailqacn_Enterprise";


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

	@Automated(automated = "Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate Team Search and Coverage in Team Tab")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class, enabled = false)
	public void validateTeamTabAsStoreManager(String username, String password, String browser, String location)
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
		teamPage.coverageViewToPastOrFuture(TeamTest.weekViewType.Next.getValue(), TeamTest.weekCount.Six.getValue());
	}
	
	
	@Automated(automated = "Automated")
	@Owner(owner = "Naval")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "TP-157: Team Tab :- Verify whether Manager is able to approve Time Off request")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class, enabled = false)
	public void VerifyWhetherManagerCanApproveTimeOffRequestAsTeamMember(String browser, String username, String password, String location)
			throws Exception
	{
		// Login with Team Member Credentials
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        profileNewUIPage.clickOnProfileConsoleMenu();
        SimpleUtils.assertOnFail("Profile Page not loaded.", profileNewUIPage.isProfilePageLoaded(), false);
        String myTimeOffSectionLabel = "My Time Off";
        profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffSectionLabel);
        String expectedRequestStatus = "PENDING";
        String timeOffReasonLabel = "VACATION";
        String timeOffExplanationText = "Sample Explanation Text";
        profileNewUIPage.createNewTimeOffRequest(timeOffReasonLabel, timeOffExplanationText);
        String requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel
        	, timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime());
        if(requestStatus.toLowerCase().contains(expectedRequestStatus.toLowerCase()))
        	SimpleUtils.pass("Profile Page: New Time Off Request status is '"+requestStatus+"'.");
        else
        	SimpleUtils.fail("Profile Page: New Time Off Request status is  not '"+expectedRequestStatus
        			+"', status found as '"+requestStatus+"'.", false);
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
		loginPage.logOut();

        // Login as Store Manager
        String fileName = "UsersCredentials.json";
        fileName = SimpleUtils.getEnterprise(controlEnterprice)+fileName;
        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
        Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
        loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
        		, String.valueOf(storeManagerCredentials[0][2]));
        dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
		teamPage.goToTeam();
		teamPage.openToDoPopupWindow();
		teamPage.approveOrRejectTimeOffRequestFromToDoList(username, getTimeOffStartTime(), getTimeOffEndTime(), timeOffRequestAction.Approve.getValue());
		teamPage.closeToDoPopupWindow();
		teamPage.searchAndSelectTeamMemberByName(username);
		String TeamMemberProfileSubSectionLabel = "Time Off";
        profileNewUIPage.selectProfilePageSubSectionByLabel(TeamMemberProfileSubSectionLabel);
        requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel,
        		timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime());
        if(requestStatus.toLowerCase().contains(timeOffRequestStatus.Approved.getValue().toLowerCase()))
        	SimpleUtils.pass("Team Page: Time Off request Approved By Store Manager reflected on Team Page.");
        else
        	SimpleUtils.fail("Team Page: Time Off request Approved By Store Manager not reflected on Team Page.", false);

        loginPage.logOut();

        // Login as Team Member Again
        loginToLegionAndVerifyIsLoginDone(username, password, location);
        profileNewUIPage.clickOnProfileConsoleMenu();
        SimpleUtils.assertOnFail("Profile Page not loaded.", profileNewUIPage.isProfilePageLoaded(), false);
        profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffSectionLabel);
        requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel
            	, timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime()

		);
        if(requestStatus.toLowerCase().contains(timeOffRequestStatus.Approved.getValue().toLowerCase()))
         	SimpleUtils.pass("Profile Page: New Time Off Request status is '"+requestStatus
         			+"' after Store Manager Approved the request.");
        else
          	SimpleUtils.fail("Profile Page: New Time Off Request status is '"+requestStatus
          			+"' after Store Manager Approved the request.", false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Naval")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "TP-157: Team Tab :- Verify whether Manager is able to approve Time Off request")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class, enabled = false)
	public void VerifyWhetherManagerCanRejectTimeOffRequestAsTeamMember(String browser, String username, String password, String location)
			throws Exception
	{
		// Login with Team Member Credentials
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        profileNewUIPage.clickOnProfileConsoleMenu();
        SimpleUtils.assertOnFail("Profile Page not loaded.", profileNewUIPage.isProfilePageLoaded(), false);
        String myTimeOffSectionLabel = "My Time Off";
        profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffSectionLabel);
        String expectedRequestStatus = "PENDING";
        String timeOffReasonLabel = "VACATION";
        String timeOffExplanationText = "Sample Explanation Text";
        profileNewUIPage.createNewTimeOffRequest(timeOffReasonLabel, timeOffExplanationText);
        String requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel
        	, timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime());
        if(requestStatus.toLowerCase().contains(expectedRequestStatus.toLowerCase()))
        	SimpleUtils.pass("Profile Page: New Time Off Request status is '"+requestStatus+"'.");
        else
        	SimpleUtils.fail("Profile Page: New Time Off Request status is  not '"+expectedRequestStatus
        			+"', status found as '"+requestStatus+"'.", false);
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
		loginPage.logOut();

        // Login as Store Manager
        String fileName = "UsersCredentials.json";
        fileName = SimpleUtils.getEnterprise(controlEnterprice)+fileName;
        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
        Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
        loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
        		, String.valueOf(storeManagerCredentials[0][2]));
        dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
		teamPage.goToTeam();
		teamPage.openToDoPopupWindow();
		teamPage.approveOrRejectTimeOffRequestFromToDoList(username, getTimeOffStartTime(), getTimeOffEndTime(), timeOffRequestAction.Reject.getValue());
		teamPage.closeToDoPopupWindow();
		teamPage.searchAndSelectTeamMemberByName(username);
		String TeamMemberProfileSubSectionLabel = "Time Off";
        profileNewUIPage.selectProfilePageSubSectionByLabel(TeamMemberProfileSubSectionLabel);
        requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel,
        		timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime());
        if(requestStatus.toLowerCase().contains(timeOffRequestStatus.Rejected.getValue().toLowerCase()))
        	SimpleUtils.pass("Team Page: Time Off request Rejected By Store Manager reflected on Team Page.");
        else
        	SimpleUtils.fail("Team Page: Time Off request Rejected By Store Manager not reflected on Team Page.", false);

        loginPage.logOut();

        // Login as Team Member Again
        loginToLegionAndVerifyIsLoginDone(username, password, location);
        profileNewUIPage.clickOnProfileConsoleMenu();
        SimpleUtils.assertOnFail("Profile Page not loaded.", profileNewUIPage.isProfilePageLoaded(), false);
        profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffSectionLabel);
        requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel
            	, timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime());
        if(requestStatus.toLowerCase().contains(timeOffRequestStatus.Rejected.getValue().toLowerCase()))
         	SimpleUtils.pass("Profile Page: New Time Off Request status is '"+requestStatus
         			+"' after Store Manager Rejected the request.");
        else
          	SimpleUtils.fail("Profile Page: New Time Off Request status is '"+requestStatus
          			+"' after Store Manager Rejected the request.", false);
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team Functionality > In Update Info")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInUpdateInfoAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			dashboardPage.verifyDashboardPageLoadedProperly();
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.isControlsPageLoaded();
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			controlsNewUIPage.isControlsScheduleCollaborationLoaded();
			String mandatoryField = controlsNewUIPage.getOnBoardOptionFromScheduleCollaboration();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.verifyTheFunctionOfAddNewTeamMemberButton();
			teamPage.checkAddATMMandatoryFieldsAreLoaded(mandatoryField);
			String firstName = teamPage.fillInMandatoryFieldsOnNewTMPage(newTMDetails, mandatoryField);
			teamPage.isSaveButtonOnNewTMPageEnabled();
			teamPage.saveTheNewTeamMember();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchTheNewTMAndUpdateInfo(firstName);
			teamPage.isProfilePageLoaded();
			teamPage.isEmailOrPhoneNumberEmptyAndUpdate(newTMDetails, mandatoryField);
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.searchTheTMAndCheckUpdateInfoNotShow(firstName);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team functionality>In Roster")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInRosterAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.isControlsPageLoaded();
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			controlsNewUIPage.isControlsScheduleCollaborationLoaded();
			String mandatoryField = controlsNewUIPage.getOnBoardOptionFromScheduleCollaboration();
			String currentDate = getTimeZoneFromControlsAndGetDate();
			// Verify TM Count is correct from roster
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.verifyTMCountIsCorrectOnRoster();
			// Verify Search Team Members is working correctly
			List<String> testStrings = new ArrayList<>(Arrays.asList("jam", "boris", "Retail"));
			teamPage.verifyTheFunctionOfSearchTMBar(testStrings);
			// Verify + button to add TM is working
			teamPage.verifyTheFunctionOfAddNewTeamMemberButton();
			// Verify Cancel button is enabled by default
			teamPage.verifyCancelButtonOnAddTMIsEnabled();
			// Verify Contact number accept in numeric digits only
			List<String> contactNumbers = new ArrayList<>(Arrays.asList("123456", "abc123cfg566", "@#$%%", "1234567890"));
			teamPage.verifyContactNumberFormatOnNewTMPage(contactNumbers);
			// Verify Mandatory field can not leave empty
			teamPage.checkAddATMMandatoryFieldsAreLoaded(mandatoryField);
			teamPage.verifyTheMandatoryFieldsCannotEmpty();
			// Verify Date Hired calendar is open for current month only and current date should be in Red color
			teamPage.verifyTheMonthAndCurrentDayOnCalendar(currentDate);
			// Verify Calendar can be Navigate to Previous/future
			teamPage.verifyTheCalendarCanNavToPreviousAndFuture();
			// Verify Save button is enabled only when all the mandatory details are filled
			teamPage.fillInMandatoryFieldsOnNewTMPage(newTMDetails, mandatoryField);
			teamPage.isSaveButtonOnNewTMPageEnabled();
			// Verify click on Cancel button, it will return to the Roster page
			teamPage.clickCancelButton();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora&Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the team functionality in Roster - Sort")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInRosterForSortAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			// Check whether the location is location group or not
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			boolean isLocationGroup = toggleSummaryPage.isLocationGroup();

			// Verify TM Count is correct from roster
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.verifyTMCountIsCorrectOnRoster();
			// Verify Search Team Members is working correctly
			List<String> testStrings = new ArrayList<>(Arrays.asList("jam", "boris", "Retail Manager", "abc"));
			teamPage.verifyTheFunctionOfSearchTMBar(testStrings);
			// Verify the column in roster page
			teamPage.verifyTheColumnInRosterPage(isLocationGroup);
			// Verify NAME column can be sorted in ascending or descending order
			teamPage.verifyTheSortFunctionInRosterByColumnName("NAME");
			// Verify EMPLOYEE ID column can be sorted in ascending or descending order
			teamPage.verifyTheSortFunctionInRosterByColumnName("EMPLOYEE ID");
			// Verify JOB TITLE column can be sorted in ascending or descending order
			teamPage.verifyTheSortFunctionInRosterByColumnName("JOB TITLE");
			// Verify EMPLOYMENT column can be sorted in ascending or descending order
			teamPage.verifyTheSortFunctionInRosterByColumnName("EMPLOYMENT");
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team functionality>In Transfer")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInTransferAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			String currentDate = getTimeZoneFromControlsAndGetDate();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			// Verify any new home location can be selected
			String teamMember = teamPage.selectATeamMemberToTransfer();
			String selectedLocation = teamPage.verifyHomeLocationCanBeSelected();
			// Verify Temp transfer button is working
			teamPage.verifyClickOnTemporaryTransferButton();
			// Verify when click on temp Transfer button,Start date and End date calendar is opening
			teamPage.verifyTwoCalendarsForCurrentMonthAreShown(currentDate);
			// Verify current date is by default selected, Other Dates can be selected
			teamPage.verifyTheCurrentDateAndSelectOtherDateOnTransfer();
			// Verify while transferring to a new location, old location shift is converting into open shift
			teamPage.isApplyButtonEnabled();
			teamPage.verifyClickOnApplyButtonOnTransfer();
			teamPage.verifyTheMessageOnPopupWindow(location, selectedLocation, teamMember);
			// Verify Confirm button is working
			teamPage.verifyTheFunctionOfConfirmTransferButton();
			// Verify home location is not updating to new location
			teamPage.verifyTheHomeStoreLocationOnProfilePage(location, selectedLocation);
			// Verify cancel Transfer button is working,After click on Cancel Transfer-one pop-up is coming
			teamPage.isCancelTransferButtonLoadedAndClick();
			teamPage.verifyCancelTransferWindowPopup();
			// Verify After click on Cancel Transfer button, Transfer button is enabled again
			teamPage.verifyTransferButtonEnabledAfterCancelingTransfer();
			// Verify After cancelling the transfer, Home location should get updated as the previous one
			teamPage.verifyHomeLocationAfterCancelingTransfer(location);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team functionality > In Badges")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInBadgesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			dashboardPage.isDashboardPageLoaded();
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
			// Verify Badges is working and is able to edit into any new one
			String badgeID = teamPage.verifyTheFunctionOfEditBadges();
			teamPage.goToTeam();
			// Verify Badges is visible on Team roster.
			teamPage.verifyTheVisibleOfBadgesOnTeamRoster(firstName, badgeID);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team Functionality > Invite Team Member")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInInviteTeamMemberAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			dashboardPage.isDashboardPageLoaded();
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			controlsPage.gotoControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			controlsNewUIPage.isControlsPageLoaded();
			controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
			controlsNewUIPage.isControlsScheduleCollaborationLoaded();
			// Set the on boarded option as Email while inviting
			controlsNewUIPage.setOnBoardOptionAsEmailWhileInviting();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.verifyTheFunctionOfAddNewTeamMemberButton();
			String firstName = teamPage.addANewTeamMemberToInvite(newTMDetails);
			// Verify Team member is invited and visible on todos
			teamPage.openToDoPopupWindow();
			teamPage.verifyTMIsVisibleAndInvitedOnTODO(firstName);
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(firstName);
			teamPage.isProfilePageLoaded();
			// Verify To Invite Team Member for Onboarding, Invite/Re-Invite button should be available
			teamPage.verifyInviteAndReInviteButtonThenInvite();
			// Verify To use the legion, when click on invite button, Invite Team Member page should be opened.
			teamPage.isInviteTeamMemberWindowLoaded();
			// Verify Contact Information, Enter Committed Availability and Personalize Welcome Message Field should be available
			teamPage.verifyThereSectionsAreLoadedOnInviteWindow();
			// Verify in Email, only correct domain is accepted.
			List<String> testEmails = new ArrayList<>(Arrays.asList("123456", "@#$%%", "nora@legion.co"));
			teamPage.verifyTheEmailFormatOnInviteWindow(testEmails);
			// Verify Send/Cancel button should be available on Invite Team Member
			teamPage.isSendAndCancelLoadedAndEnabledOnInvite();
			teamPage.sendTheInviteViaEmail(newTMDetails.get("EMAIL"));
			// Verify Invitation code should be available on Email id
			GmailPage gmailPage = pageFactory.createConsoleGmailPage();
			gmailPage.loginToGmailWithCredential();
			gmailPage.waitUntilInvitationEmailLoaded();
			gmailPage.verifyInvitationCodeIsAvailableOnEmailID();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify the Team functionality > Work Preferences")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInWorkPreferencesAsStoreManager(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			dashboardPage.isDashboardPageLoaded();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.selectATeamMemberToViewProfile();
			teamPage.isProfilePageLoaded();
			teamPage.navigateToWorkPreferencesTab();
			// Verify shift preferences- can be Edit by clicking on pencil icon Changes are being Cancel by clicking on cancel button
			List<String> previousPreferences = teamPage.getShiftPreferences();
			teamPage.clickOnEditShiftPreference();
			SimpleUtils.assertOnFail("Edit Shift Preferences layout failed to load!", teamPage.isEditShiftPreferLayoutLoaded(), true);
			teamPage.setSliderForShiftPreferences();
			teamPage.changeShiftPreferencesStatus();
			teamPage.clickCancelEditShiftPrefBtn();
			List<String> currentPreferences = teamPage.getShiftPreferences();
			if (previousPreferences.containsAll(currentPreferences) && currentPreferences.containsAll(previousPreferences)) {
				SimpleUtils.pass("Shift preferences don't change after cancelling!");
			} else {
				SimpleUtils.fail("Shift preferences are changed after cancelling!", true);
			}
			// Verify shift preferences- can be Edit by clicking on pencil icon Changes are being Saved by clicking on Save button
			teamPage.clickOnEditShiftPreference();
			SimpleUtils.assertOnFail("Edit Shift Preferences layout failed to load!", teamPage.isEditShiftPreferLayoutLoaded(), true);
			List<String> changedShiftPrefs = teamPage.setSliderForShiftPreferences();
			List<String> status = teamPage.changeShiftPreferencesStatus();
			teamPage.clickSaveShiftPrefBtn();
			currentPreferences = teamPage.getShiftPreferences();
			teamPage.verifyCurrentShiftPrefIsConsistentWithTheChanged(currentPreferences, changedShiftPrefs, status);
			// Verify Availability Graph [Edited by manager/Admin]:Weekly Availability/Unavailability is showing by green/Red color
			teamPage.editOrUnLockAvailability();
			SimpleUtils.assertOnFail("Edit Availability layout failed to load!", teamPage.areCancelAndSaveAvailabilityBtnLoaded(), true);
			teamPage.changePreferredHours();
			teamPage.changeBusyHours();
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Team Functionality > Profile section")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheTeamFunctionalityInProfileSectionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			// Login with Internal Admin Credentials
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.selectATeamMemberToViewProfile();
			teamPage.isProfilePageLoaded();
			// Verify Profile picture is updating
			String filePath = imageFilePath.get("FilePath");
			teamPage.updateProfilePicture(filePath);
			// Verify Phone number and Email id are updating
			String phoneNumber = "1234455678";
			String emailID = "nora@legion.co";
			teamPage.updatePhoneNumberAndEmailID(phoneNumber, emailID);
			// Verify Engagement details are updating
			teamPage.updateEngagementDetails(newTMDetails);
			// Verify Badges is updating
			teamPage.clickOnEditBadgeButton();
			List<String> selectedBadgeIDs = teamPage.updateTheSelectedBadges();
			List<String> badgeIDs = teamPage.getCurrentBadgesOnEngagement();
			if (badgeIDs.containsAll(selectedBadgeIDs) && selectedBadgeIDs.containsAll(badgeIDs)) {
				SimpleUtils.pass("Badges updated Successfully!");
			} else {
				SimpleUtils.fail("Badges not updated successfully!", true);
			}
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	public String getTimeZoneFromControlsAndGetDate() throws Exception {
		String timeZone = "";
		String currentDate = "";
		ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
		controlsPage.gotoControlsPage();
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
		if (controlsNewUIPage.isControlsPageLoaded()){
			controlsNewUIPage.clickOnControlsLocationProfileSection();
			if (controlsNewUIPage.isControlsLocationProfileLoaded()){
				timeZone = controlsNewUIPage.getTimeZoneFromLocationDetailsPage();
				if (timeZone != null && !timeZone.isEmpty()){
					SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy dd");
					currentDate = SimpleUtils.getCurrentDateMonthYearWithTimeZone(timeZone, format);
				}
			}
		}
		return currentDate;
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the profile invitation code in Profile UI")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheProfileInvitationCodeInProfileUIAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			// Login with Internal Admin Credentials
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			// Check invite to legion button and invitation code are not exists on the onboarded user profile page
			teamPage.selectARandomOnboardedOrNotTeamMemberToViewProfile(true);
			teamPage.isProfilePageLoaded();
			SimpleUtils.assertOnFail("Invite buttons should not loaded on the onboarded TM profile page! ",
					!profileNewUIPage.isInviteToLegionButtonLoaded()
							&& !profileNewUIPage.isInvitationCodeLoaded()
							&& !profileNewUIPage.isShowOrHideInvitationCodeButtonLoaded(), false);

			teamPage.goToTeam();
			teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
			teamPage.selectARandomOnboardedOrNotTeamMemberToViewProfile(false);
			teamPage.isProfilePageLoaded();
			// Click Invite to Legion button
			profileNewUIPage.userProfileInviteTeamMember();
			// Check the tooltip of
			String tooltipMessage = "This code has been sent to the team member";
			SimpleUtils.assertOnFail("Show Or Hide Invitation Code Button tooltip is incorrectly",
					tooltipMessage.equals(profileNewUIPage.getShowOrHideInvitationCodeButtonTooltip()), false);
			// Click Show Invitation Code button
			profileNewUIPage.clickOnShowOrHideInvitationCodeButton(true);
			//Check invitation code is loaded
			SimpleUtils.assertOnFail("Invitation code loaded fail! ", profileNewUIPage.isInvitationCodeLoaded(), false);
			// Get invitation code
			String invitationCode = profileNewUIPage.getInvitationCode();
			String fullName = profileNewUIPage.getUserProfileName().get("fullName");
			String lastName = fullName.substring(fullName.indexOf(" "));
			// Click Hide Invitation Code button
			profileNewUIPage.clickOnShowOrHideInvitationCodeButton(false);
			SimpleUtils.assertOnFail("Invitation code should not loaded! ", !profileNewUIPage.isInvitationCodeLoaded(), false);

			//Logout
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			loginPage.verifyLoginPageIsLoaded();

			//Check Create Account message display correctly
			loginPage.verifyCreateAccountMessageDisplayCorrectly();

			//Click Sign Up button
			loginPage.clickSignUpLink();
			SimpleUtils.assertOnFail("Verify last name and invitation code page fail to loaded! ", loginPage.isVerifyLastNameAndInvitationCodePageLoaded(), false);

			//Input the incorrect invitation code
			loginPage.verifyLastNameAndInvitationCode(lastName, "123456");
			SimpleUtils.assertOnFail("Error toast failed to loaded", loginPage.isErrorToastLoaded(), false);

			//Input the correct invitation code
			loginPage.verifyLastNameAndInvitationCode(lastName, invitationCode);
			SimpleUtils.assertOnFail("Create Account page fail to loaded! ", loginPage.isCreateAccountPageLoaded(), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Remove access to Employee Profile in Team Schedule view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyRemoveAccessToEmployeeProfileInTeamScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
		ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		// Create schedule and publish it.
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);

		if (createSchedulePage.isWeekGenerated()){
			createSchedulePage.unGenerateActiveScheduleScheduleWeek();
		}
		createSchedulePage.createScheduleForNonDGFlowNewUI();
		scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
		scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
		scheduleMainPage.saveSchedule();
		createSchedulePage.publishActiveSchedule();
		LoginPage loginPage = pageFactory.createConsoleLoginPage();
		loginPage.logOut();

		// Login as Store Manager
		loginAsDifferentRole(AccessRoles.StoreManager.getValue());
		ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
		profileNewUIPage.clickOnUserProfileImage();//.getNickNameFromProfile();
		dashboardPage.clickOnSwitchToEmployeeView();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab("Team Schedule");
		SimpleUtils.assertOnFail("SM shouldn't be able to view profile info in employee view", !scheduleShiftTablePage.isProfileIconsClickable(), false);
		loginPage.logOut();


		// Login as Team Member
		loginAsDifferentRole(AccessRoles.TeamMember.getValue());
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab("Team Schedule");
		SimpleUtils.assertOnFail("SM shouldn't be able to view profile info in employee view", !scheduleShiftTablePage.isProfileIconsClickable(), false);
	}



	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "KendraScott2_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the SM, DM, TL can or cannot see the invite button on TM list and profile page when grant or ungrant Invite Employee permission to them")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyAbilityToHideInviteButtonAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Grant Invite Employee permission for DM SM and TL
			ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			String accessRoleTab = "Access Roles";
			String rolePermissionForDM = "Area Manager";
			String rolePermissionForSM = "Store Manager";
			String rolePermissionForTL = "Team Lead";
			String section = "Team";
			String permission = "Invite Employee";
			String actionOff = "off";
			String actionOn = "on";
			OpsPortalLocationsPage opsPortalLocationsPage = (OpsPortalLocationsPage) pageFactory.createOpsPortalLocationsPage();
			opsPortalLocationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToUserManagementPage();
			controlsNewUIPage.clickOnControlsUsersAndRolesSection();
			controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
			controlsNewUIPage.turnOnOrOffSpecificPermissionForDifferentRole(rolePermissionForDM, section, permission, actionOn);
			controlsNewUIPage.turnOnOrOffSpecificPermissionForDifferentRole(rolePermissionForSM, section, permission, actionOn);
			controlsNewUIPage.turnOnOrOffSpecificPermissionForDifferentRole(rolePermissionForTL, section, permission, actionOn);
			switchToConsoleWindow();


			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			//Login as DM
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.DistrictManager.getValue());
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to team page and check the invite button
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			teamPage.goToTeam();
			HashMap<String, Object[][]> teamMembers = null;
			if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))){
				teamMembers = controlTeamMembers;
			} else {
				teamMembers = opTeamMembers;
			}

			String tm = teamMembers.get("TeamMember1")[0][0].toString();

			SimpleUtils.assertOnFail("The invite buttons fail to load on Roster page! ", teamPage.checkIsInviteButtonExists(), false);
			teamPage.searchAndSelectTeamMemberByName(tm);
			SimpleUtils.assertOnFail("The invite buttons fail to load on profile page! ", profileNewUIPage.isInviteToLegionButtonLoaded(), false);
			loginPage.logOut();
			//Login as SM
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to team page and check the invite button
			teamPage.goToTeam();
			SimpleUtils.assertOnFail("The invite buttons fail to load on Roster page! ", teamPage.checkIsInviteButtonExists(), false);
			teamPage.searchAndSelectTeamMemberByName(tm);
			SimpleUtils.assertOnFail("The invite buttons fail to load on profile page! ", profileNewUIPage.isInviteToLegionButtonLoaded(), false);
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.TeamLead.getValue());
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to team page and check the invite button
			teamPage.goToTeam();
			SimpleUtils.assertOnFail("The invite buttons fail to load on Roster page! ", teamPage.checkIsInviteButtonExists(), false);
			teamPage.searchAndSelectTeamMemberByName(tm);
			SimpleUtils.assertOnFail("The invite buttons fail to load on profile page! ", profileNewUIPage.isInviteToLegionButtonLoaded(), false);
			loginPage.logOut();
			//Login as admin
			Thread.sleep(3000);
			loginToLegionAndVerifyIsLoginDone(username, password, location);
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			controlsPage.gotoControlsPage();
			controlsNewUIPage.isControlsPageLoaded();
			controlsNewUIPage.clickOnControlsUsersAndRolesSection();
			controlsNewUIPage.verifyUsersAreLoaded();
			controlsPage.clickGlobalSettings();
			controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
			controlsNewUIPage.turnOnOrOffSpecificPermissionForDifferentRole(rolePermissionForDM, section, permission, actionOff);
			controlsNewUIPage.turnOnOrOffSpecificPermissionForDifferentRole(rolePermissionForSM, section, permission, actionOff);
			controlsNewUIPage.turnOnOrOffSpecificPermissionForDifferentRole(rolePermissionForTL, section, permission, actionOff);
			loginPage.logOut();

			//Login as DM
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.DistrictManager.getValue());
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to team page and check the invite button
			teamPage.goToTeam();
			SimpleUtils.assertOnFail("The invite buttons fail to load on Roster page! ", !teamPage.checkIsInviteButtonExists(), false);
			teamPage.searchAndSelectTeamMemberByName(tm);
			SimpleUtils.assertOnFail("The invite buttons fail to load on profile page! ", !profileNewUIPage.isInviteToLegionButtonLoaded(), false);
			loginPage.logOut();
			//Login as SM
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to team page and check the invite button
			teamPage.goToTeam();
			SimpleUtils.assertOnFail("The invite buttons fail to load on Roster page! ", !teamPage.checkIsInviteButtonExists(), false);
			teamPage.searchAndSelectTeamMemberByName(tm);
			SimpleUtils.assertOnFail("The invite buttons fail to load on profile page! ", !profileNewUIPage.isInviteToLegionButtonLoaded(), false);
			loginPage.logOut();
			//Login as TL
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.TeamLead.getValue());
			dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

			//Go to team page and check the invite button
			teamPage.goToTeam();
			SimpleUtils.assertOnFail("The invite buttons fail to load on Roster page! ", !teamPage.checkIsInviteButtonExists(), false);
			teamPage.searchAndSelectTeamMemberByName(tm);
			SimpleUtils.assertOnFail("The invite buttons fail to load on profile page! ", !profileNewUIPage.isInviteToLegionButtonLoaded(), false);


		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify upcoming shifts when TM has access to multiple locations")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyUpcomingShiftsWhenTMHasAccessToMultipleLocationsAsTeamMember (String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Get two locations of TM
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			String tmName = profileNewUIPage.getNickNameFromProfile();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			String location1 = location;
			String location2 = "";
			int i = 0;
			while (i <20 && (location2 == "" || location2.equalsIgnoreCase(location))) {
				location2 = locationSelectorPage.getOneRandomNameFromUpperFieldDropdownList("Location").split("\n")[0];
			}

			// Team Member logout
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			// Login as Admin
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();

			//Ungenerate the first week schedule
			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			//Go to next week and create schedule
			scheduleCommonPage.navigateToNextWeek();
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}

			//Delete all the shifts of TM
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmName);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");

			//Create shift for TM on first day
			String workRole = shiftOperatePage.getRandomWorkRole();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("4pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			scheduleCommonPage.clickOnDayView();
			scheduleCommonPage.navigateDayViewWithIndex(0);
			String weekDay1 = scheduleCommonPage.getScheduleWeekStartDayMonthDate();

			//Select another location
			locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location1);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			//Ungenerate the first week schedule
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			//Go to next week and create schedule
			scheduleCommonPage.navigateToNextWeek();
			isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmName);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");

			//Create shift for TM on second day
			workRole = shiftOperatePage.getRandomWorkRole();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(1, 1, 1);
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("3pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			//Get second shift info
			scheduleCommonPage.clickOnDayView();
			scheduleCommonPage.navigateDayViewWithIndex(1);
			String weekDay2 = scheduleCommonPage.getScheduleWeekStartDayMonthDate();
			scheduleCommonPage.clickOnWeekView();

			// All the shifts that are assigned to this TM should show, if it is created in another location, these shifts should be gray
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyTM.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(tmName);
			List<WebElement> shifts = scheduleShiftTablePage.getOneDayShiftByName(0, tmName);
			SimpleUtils.assertOnFail("The shift display correctly on the first day!",
					shifts.size() == 1 && shifts.get(0).getAttribute("class").contains("no-drag"), false);

			shifts = scheduleShiftTablePage.getOneDayShiftByName(1, tmName);
			SimpleUtils.assertOnFail("The shift dislay correctly on the first day!",
					shifts.size() == 1 && !shifts.get(0).getAttribute("class").contains("no-drag"), false);
			loginPage.logOut();

			//Login as TM, select the first location, observe the upcoming shifts
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			List<HashMap<String, String>> shiftsInfo1= dashboardPage.getAllUpComingShiftsInfo();
			String shift1Time = "11:00am - 4:00pm";
			String shift2Time = "10:00am - 3:00pm";
			//Upcoming shifts are shown
			SimpleUtils.assertOnFail("The TM's upcoming shifts should equal or more than 2! ", shiftsInfo1.size() > 1, false);
			//Shift info, shift location should be correct
			SimpleUtils.assertOnFail("The first upcoming shift's location name should be: "+location2 +" , but actual is: " +shiftsInfo1.get(0).get("locationName"),
					shiftsInfo1.get(0).get("locationName").equalsIgnoreCase(location2), false);
			SimpleUtils.assertOnFail("The first upcoming shift's shift info should be: "+weekDay1 + " "+ shift1Time +" , but actual is: " +shiftsInfo1.get(0).get("shiftInfo").replace(",", ""),
					shiftsInfo1.get(0).get("shiftInfo").replace(",", "").equalsIgnoreCase(weekDay1 + " "+ shift1Time), false);
			SimpleUtils.assertOnFail("The second upcoming shift's location name should be: "+location1 +" , but actual is: " +shiftsInfo1.get(1).get("locationName"),
					shiftsInfo1.get(1).get("locationName").equalsIgnoreCase(location1), false);
			SimpleUtils.assertOnFail("The second upcoming shift's shift info should be: "+weekDay2 + " "+ shift2Time +" , but actual is: " +shiftsInfo1.get(1).get("shiftInfo").replace(",", ""),
					shiftsInfo1.get(1).get("shiftInfo").replace(",", "").equalsIgnoreCase(weekDay2 + " "+ shift2Time), false);
			//All the shifts assigned to this TM should show here
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.navigateToNextWeek();
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			SimpleUtils.assertOnFail("All the shifts assigned to this TM should show here! ",
					scheduleShiftTablePage.getAvailableShiftsInDayView().size() == 2, false);
			//select the second location, the upcoming shifts should be consistent with the first location
			dashboardPage.clickOnDashboardConsoleMenu();
			locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
			List<HashMap<String, String>> shiftsInfo2= dashboardPage.getAllUpComingShiftsInfo();
			SimpleUtils.assertOnFail("", shiftsInfo2.equals(shiftsInfo1), false);
			//All the shifts assigned to this TM should show here
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.navigateToNextWeek();
			SimpleUtils.assertOnFail("All the shifts assigned to this TM should show here! ",
					scheduleShiftTablePage.getAvailableShiftsInDayView().size() == 2, false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify can update the config in integration")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyCanUpdateTheHRConfigInIntegrationAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

		dashboardPage.clickOnIntegrationConsoleMenu();
		dashboardPage.verifyIntegrationPageIsLoaded();
		IntegrationPage integrationPage = pageFactory.createIntegrationPage();
		if (integrationPage.checkIsConfigExists(Constants.Ftp, Constants.Hr)) {
			integrationPage.clickOnEditButtonByChannelAndApplication(Constants.Ftp, Constants.Hr);
			SimpleUtils.assertOnFail("Edit Config page failed to load!", integrationPage.isEditConfigPageLoaded(), false);
			HashMap<String, String> disable = new HashMap<>();
			disable.put("status", Constants.Disabled);
			integrationPage.editTheConfigByName(disable);
			dashboardPage.verifyIntegrationPageIsLoaded();
			SimpleUtils.assertOnFail("FTP HR still enabled!", !integrationPage.checkIsConfigExists(Constants.Ftp, Constants.Hr), false);
		}
		if(!integrationPage.checkIsConfigExists(Constants.Custom, Constants.Hr)){
			Map<String, String> configInfo = new HashMap<>();
			configInfo.put("channel", Constants.Custom);
			configInfo.put("applicationType", Constants.Hr);
			configInfo.put("status", Constants.Enabled);
			configInfo.put("timeZoneOption", Constants.Utc);

			integrationPage.createConfig(configInfo);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Nora")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Verify the buttons for employee in different status")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyTheButtonsForEmployeeInDifferentStatusAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

		TeamPage teamPage = pageFactory.createConsoleTeamPage();
		LoginPage loginPage = pageFactory.createConsoleLoginPage();
		ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
		teamPage.goToTeam();
		teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

		List<String> expectedButtons = new ArrayList<>();

		// Verify Terminate, Transfer, Manual Onboard buttons should show for new employee
		teamPage.searchAndSelectTeamMemberByName("New");
		SimpleUtils.assertOnFail("Profile page not loaded successfully!", teamPage.isProfilePageLoaded(), false);
		expectedButtons.add(Constants.Terminate);
		expectedButtons.add(Constants.Transfer);
		expectedButtons.add(Constants.ManualOnboard);
		teamPage.verifyTheButtonsInActions(expectedButtons);

		teamPage.goToTeam();
		teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

		// Verify Activate, Terminate, Transfer buttons should show in Actions for onboarded employee
		teamPage.searchAndSelectTeamMemberByName("Onboarded");
		SimpleUtils.assertOnFail("Profile page not loaded successfully!", teamPage.isProfilePageLoaded(), false);
		expectedButtons = new ArrayList<>();
		expectedButtons.add(Constants.Terminate);
		expectedButtons.add(Constants.Transfer);
		expectedButtons.add(Constants.Activate);
		teamPage.verifyTheButtonsInActions(expectedButtons);

		loginPage.logOut();
		loginAsDifferentRole(AccessRoles.TeamMember.getValue());
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

		String firstName = profileNewUIPage.getNickNameFromProfile();
		profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
		SimpleUtils.assertOnFail("Profile page not loaded successfully!", teamPage.isProfilePageLoaded(), false);

		// Verify Change Password button should show in Actions in TM view
		expectedButtons = new ArrayList<>();
		expectedButtons.add(Constants.ChangePassword);
		teamPage.verifyTheButtonsInActions(expectedButtons);

		loginPage.logOut();

		loginToLegionAndVerifyIsLoginDone(username, password, location);
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

		teamPage.goToTeam();
		teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

		// Verify Deactivate, Terminate, Transfer, Send Username, Reset Password buttons should show in Actions for active employee
		teamPage.searchAndSelectTeamMemberByName(firstName);
		SimpleUtils.assertOnFail("Profile page not loaded successfully!", teamPage.isProfilePageLoaded(), false);
		expectedButtons = new ArrayList<>();
		expectedButtons.add(Constants.Terminate);
		expectedButtons.add(Constants.Transfer);
		expectedButtons.add(Constants.Deactivate);
		expectedButtons.add(Constants.SendUsername);
		expectedButtons.add(Constants.ResetPassword);
		teamPage.verifyTheButtonsInActions(expectedButtons);
	}


	@Automated(automated ="Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate there is no error warning when no overlap exists when SM edit the availabilities for TM")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateErrorMessageWhenThereIsNoRecurringPendingAvailabilityRequestAsStoreManager(String browser, String username, String password, String location) throws Exception {
		// Login with Store Manager Credentials
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		// Set availability policy
		ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
		controlsPage.gotoControlsPage();
		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
		SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

		dashboardPage.navigateToDashboard();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
		controlsPage.gotoControlsPage();
		SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

		controlsNewUIPage.clickOnControlsSchedulingPolicies();
		SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
		controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Required for all changes";
		controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredTest.AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
		LoginPage loginPage = pageFactory.createConsoleLoginPage();
		loginPage.logOut();

		//Login as Team Member to change availability
		loginAsDifferentRole(AccessRoles.TeamMember.getValue());
		ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
		String requestUserName = profileNewUIPage.getNickNameFromProfile();
		String myWorkPreferencesLabel = "My Work Preferences";
		profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
		//cancel all availability change requests firstly.
		profileNewUIPage.cancelAllPendingAvailabilityRequest();
		//Update Preferred Hours
		while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
			profileNewUIPage.clickNextWeek();
		}
		int sliderIndex = 1;
		double hours = 0.5;//move 1 metric 0.5h right----increase
		String leftOrRightDuration = "Right";
		String hoursType = "When I prefer to work";
		String repeatChanges = "This week only";
		profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
				hours, repeatChanges);
		loginPage.logOut();

		//log in as SM, go to the Roster page, search out the TM.
		loginToLegionAndVerifyIsLoginDone(username, password, location);
		TeamPage teamPage = pageFactory.createConsoleTeamPage();
		teamPage.goToTeam();
		teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
		teamPage.searchAndSelectTeamMemberByName(requestUserName);
		String workPreferencesLabel = "Work Preferences";
		profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
		profileNewUIPage.clickAvailabilityEditButton();
		SimpleUtils.assertOnFail("Error message shouldn't show up!", !profileNewUIPage.verifyErrorMessageForEditAvailabilityShowsUpOrNot(), false);
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the available left hrs display as 0 when delete all Availabilities")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheAvailableLeftHrsDisplayAs0WhenDeleteAllAvailabilitiesAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstNameOfTM = tmFullName.split(" ")[0];
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(5000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String timeOffLabel = "Time Off";
			profileNewUIPage.selectProfilePageSubSectionByLabel(timeOffLabel);
			profileNewUIPage.cancelAllTimeOff();
			profileNewUIPage.rejectAllTimeOff();
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.cancelAllPendingAvailabilityRequest();
			profileNewUIPage.clickNextWeek();
			profileNewUIPage.clickAvailabilityEditButton();
			profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String totalHoursValue = availabilityData.get("totalHoursValue").toString();
			String remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 0, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("0.0")&&remainingHoursValue.equals("0.0"), false);
			profileNewUIPage.saveMyAvailabilityEditMode("This week only");
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 0, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("0.0")&&remainingHoursValue.equals("0.0"), false);

			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(5000);
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 0, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("0.0")&&remainingHoursValue.equals("0.0"), false);
		} catch (Exception e){
		SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the available left hrs display as 168 when add full Availabilities")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheAvailableLeftHrsDisplayAs168WhenAddFullAvailabilitiesAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}

			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String timeOffLabel = "Time Off";
			profileNewUIPage.selectProfilePageSubSectionByLabel(timeOffLabel);
			profileNewUIPage.cancelAllTimeOff();
			profileNewUIPage.rejectAllTimeOff();
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.cancelAllPendingAvailabilityRequest();
			Thread.sleep(3000);
			profileNewUIPage.clickNextWeek();
			profileNewUIPage.clickAvailabilityEditButton();
			for (int i=0; i<7;i++) {
				profileNewUIPage.updatePreferredOrBusyHoursToAllDay(i, "When I prefer to work");
			}
//			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
//			String totalHoursValue = availabilityData.get("totalHoursValue").toString();
//			String remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
//			SimpleUtils.assertOnFail("The total and remaining hrs should be 168, but the actual are: "
//							+ totalHoursValue +" and "+ remainingHoursValue,
//					totalHoursValue.equals("168.0")&&remainingHoursValue.equals("168.0"), false);
			Thread.sleep(3000);
			profileNewUIPage.saveMyAvailabilityEditMode("This week only");
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			locationSelectorPage.refreshTheBrowser();
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.clickNextWeek();
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String totalHoursValue = availabilityData.get("totalHoursValue").toString();
			String remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 168, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("168.0")&&remainingHoursValue.equals("168.0"), false);

			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 168, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("168.0")&&remainingHoursValue.equals("168.0"), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the available left hrs will display correctly when update Availabilities")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheAvailableLeftHrsDisplayCorrectlyWhenUpdateAvailabilitiesAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}

			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String timeOffLabel = "Time Off";
			profileNewUIPage.selectProfilePageSubSectionByLabel(timeOffLabel);
			profileNewUIPage.cancelAllTimeOff();
			profileNewUIPage.rejectAllTimeOff();
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.cancelAllPendingAvailabilityRequest();
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String totalHoursValueBeforeChange = availabilityData.get("totalHoursValue").toString();
			String remainingHoursValueBeforeChange = availabilityData.get("remainingHoursValue").toString();
			int sliderIndex = 0;
			double hours = -0.5;
			String repeatChanges = "This week only";
			String leftOrRightDuration = "Right";
			String hoursType = "When I prefer to work";
			profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
					hours, repeatChanges);
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			String totalHoursValueAfterChange = availabilityData.get("totalHoursValue").toString();
			String remainingHoursValueAfterChange = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total hrs should been changed, the actual is: " +totalHoursValueBeforeChange,
					!totalHoursValueBeforeChange.equals(totalHoursValueAfterChange), false);
			SimpleUtils.assertOnFail("The remaining hrs should been changed, the actual is: " +remainingHoursValueAfterChange,
					!remainingHoursValueBeforeChange.equals(remainingHoursValueAfterChange), false);

			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValueAfterChange = availabilityData.get("totalHoursValue").toString();
			remainingHoursValueAfterChange = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total hrs should been changed, the actual is: " +totalHoursValueBeforeChange,
					!totalHoursValueBeforeChange.equals(totalHoursValueAfterChange), false);
			SimpleUtils.assertOnFail("The remaining hrs should been changed, the actual is: " +remainingHoursValueAfterChange,
					!remainingHoursValueBeforeChange.equals(remainingHoursValueAfterChange), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}


	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the available left hrs when TM has shifts on the week")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheAvailableLeftHrsWhenTMHasShiftsOnTheWeekAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstNameOfTM = tmFullName.split(" ")[0];
			String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
			String timeOffLabel = "Time Off";
			profileNewUIPage.selectProfilePageSubSectionByLabel(timeOffLabel);
			profileNewUIPage.cancelAllTimeOff();
			profileNewUIPage.rejectAllTimeOff();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");

			//Create one shift for TM1 with shift time as 8am-2pm
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.selectJobTitleFilterByText(jobTitle);
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(1);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmFullName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			Thread.sleep(5000);
			//Create all day's avalabilities on the day that has shift scheduled
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.cancelAllPendingAvailabilityRequest();
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			profileNewUIPage.clickAvailabilityEditButton();
			profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
			profileNewUIPage.saveMyAvailabilityEditMode("This week only");
			profileNewUIPage.clickAvailabilityEditButton();
			profileNewUIPage.updatePreferredOrBusyHoursToAllDay(0, "When I prefer to work");
			Thread.sleep(5000);
			profileNewUIPage.saveMyAvailabilityEditMode("This week only");
//			locationSelectorPage.refreshTheBrowser();
//			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.clickNextWeek();
			profileNewUIPage.clickPreviousWeek();
			Thread.sleep(5000);
			//The message should display as '18 of 24 Available hrs left'
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String totalHoursValue = availabilityData.get("totalHoursValue").toString();
			String remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 24 and 18, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("24.0")&&remainingHoursValue.equals("18.0"), false);

			//Remove the availabilities
			profileNewUIPage.clickAvailabilityEditButton();
			profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
			profileNewUIPage.saveMyAvailabilityEditMode("This week only");
			//The message should display as '0 of 0 Available hrs left'
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 0 and 0, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("0.0")&&remainingHoursValue.equals("0.0"), false);

			//Add the availabilities again
			profileNewUIPage.clickAvailabilityEditButton();
			profileNewUIPage.updatePreferredOrBusyHoursToAllDay(0, "When I prefer to work");
			profileNewUIPage.saveMyAvailabilityEditMode("This week only");
//			locationSelectorPage.refreshTheBrowser();
//			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.clickNextWeek();
			profileNewUIPage.clickPreviousWeek();
			Thread.sleep(5000);
			//The message should display as '18 of 24 Available hrs left'
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 24 and 18, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("24.0")&&remainingHoursValue.equals("18.0"), false);

			//Go to the schedule and remove the TM's shift
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			//Go to Roster, search and go to TM1's preference page, Check the Available left hrs
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			Thread.sleep(10000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(5000);
			//The message should display as '24 of 24 Available hrs left'
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 24 and 24, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("24.0")&&remainingHoursValue.equals("24.0"), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}



	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the time off on availability table")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTimeOffOnAvailabilityTableAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.getTimeOffsLengthOnAvailabilityTable();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstNameOfTM = tmFullName.split(" ")[0];
			String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}

			//Go to Roster, search and go to TM1's preference page
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String workPreferencesLabel = "Work Preferences";
			String timeoffLabel = "Time Off";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.cancelAllPendingAvailabilityRequest();
			profileNewUIPage.clickNextWeek();
			Thread.sleep(5000);
			if (profileNewUIPage.getTimeOffsLengthOnAvailabilityTable().size() != 0) {
				profileNewUIPage.selectProfilePageSubSectionByLabel(timeoffLabel);
				profileNewUIPage.cancelAllTimeOff();
				profileNewUIPage.rejectAllTimeOff();
				profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			}
			//Create full availabilities on the week
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String totalHoursValue = availabilityData.get("totalHoursValue").toString();
			String remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			if (!totalHoursValue.equals("168.0") || !remainingHoursValue.equals("168.0")) {
				profileNewUIPage.clickAvailabilityEditButton();
				for (int i=0; i<7;i++) {
					profileNewUIPage.updatePreferredOrBusyHoursToAllDay(i, "When I prefer to work");
				}
				profileNewUIPage.saveMyAvailabilityEditMode("This week only");
				availabilityData = profileNewUIPage.getMyAvailabilityData();
				totalHoursValue = availabilityData.get("totalHoursValue").toString();
				remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
				SimpleUtils.assertOnFail("The total and remaining hrs should be 168, but the actual are: "
								+ totalHoursValue +" and "+ remainingHoursValue,
						totalHoursValue.equals("168.0")&&remainingHoursValue.equals("168.0"), false);
			}
			String currentYear = getCurrentTime().substring(0, 4);
			String fromDate1 = currentYear + " " +profileNewUIPage.getAvailabilityWeek().split("-")[0];
			String fromDate2 = currentYear + " " +profileNewUIPage.getAvailabilityWeek().split("-")[1];

			//Go to Time Off tab and create time off on the same week
			profileNewUIPage.selectProfilePageSubSectionByLabel(timeoffLabel);
			String timeOffExplanationText = "Sample Explanation Text";
			profileNewUIPage.clickOnCreateTimeOffBtn();
			Thread.sleep(5000);
			if (profileNewUIPage.isReasonLoad(ActivityTest.timeOffReasonType.JuryDuty.getValue())) {
				profileNewUIPage.createTimeOffOnSpecificDays(ActivityTest.timeOffReasonType.JuryDuty.getValue(), timeOffExplanationText, fromDate1, 0);
			} else if (profileNewUIPage.isReasonLoad(ActivityTest.timeOffReasonType.Vacation.getValue())) {
				profileNewUIPage.createTimeOffOnSpecificDays(ActivityTest.timeOffReasonType.Vacation.getValue(), timeOffExplanationText, fromDate1, 0);
			}
//			String timeOffReasonLabel = "VACATION";
//			profileNewUIPage.createTimeOffOnSpecificDays(ActivityTest.timeOffReasonType.JuryDuty.getValue(), timeOffExplanationText, fromDate1, 0);

			//Go the Preference and check the time off will display
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.clickNextWeek();
			profileNewUIPage.clickPreviousWeek();
			List<String> timeOffsLength = profileNewUIPage.getTimeOffsLengthOnAvailabilityTable();
			SimpleUtils.assertOnFail("The time offs fail to load on availability table" ,
					timeOffsLength.size() == 1
							&& timeOffsLength.get(0).equalsIgnoreCase("24.0 hrs"), false);
			//Edit the steps for SCH-657
			profileNewUIPage.clickAvailabilityEditButton();
			timeOffsLength = profileNewUIPage.getTimeOffsLengthOnAvailabilityTable();
			SimpleUtils.assertOnFail("The time offs fail to load on availability table" ,
					timeOffsLength.size() == 1
							&& timeOffsLength.get(0).equalsIgnoreCase("24.0 hrs"), false);
			controlsNewUIPage.clickOnCancelBtn();
			//Check the available left hrs, the hrs will not change
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 144, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("144.0")&&remainingHoursValue.equals("144.0"), false);
			//Go to Time Off tab and reject the time off
			profileNewUIPage.selectProfilePageSubSectionByLabel(timeoffLabel);
			profileNewUIPage.rejectAllTimeOff();
			//Go the Preference and check the time off, time off will been removed
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.clickNextWeek();
			profileNewUIPage.clickPreviousWeek();
			SimpleUtils.assertOnFail("It should has no time off on availability table",
					profileNewUIPage.getTimeOffsLengthOnAvailabilityTable().size() == 0, false);
			//Check the available left hrs, the hrs will not change
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			totalHoursValue = availabilityData.get("totalHoursValue").toString();
			remainingHoursValue = availabilityData.get("remainingHoursValue").toString();
			SimpleUtils.assertOnFail("The total and remaining hrs should be 168, but the actual are: "
							+ totalHoursValue +" and "+ remainingHoursValue,
					totalHoursValue.equals("168.0")&&remainingHoursValue.equals("168.0"), false);

		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}
	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the scheduled shifts and hrs on Availability table when there are shift been scheduled")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheScheduledShiftAndHrsOnAvailabilityTableWithScheduledShiftAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstNameOfTM = tmFullName.split(" ")[0];
			String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");

			//Create one shift with shift time as 8am-2pm for TM1 per days
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.selectJobTitleFilterByText(jobTitle);
			String workRole = shiftOperatePage.getRandomWorkRole();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
			scheduleMainPage.clickOnFilterBtn();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectSpecificWorkDay(7);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmFullName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			//Go to Roster, search and go to TM1's preference page
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			locationSelectorPage.refreshTheBrowser();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.cancelAllPendingAvailabilityRequest();
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(2000);
			profileNewUIPage.clickAvailabilityEditButton();
			profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
			profileNewUIPage.updatePreferredOrBusyHoursToAllDay(0, "When I prefer to work");
			profileNewUIPage.saveMyAvailabilityEditMode("This week only");
			locationSelectorPage.refreshTheBrowser();
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			profileNewUIPage.clickNextWeek();
			//There are 7 shifts display and scheduled hrs display as 42
			List<String> availableShiftsOnAvailabilityTable = profileNewUIPage.getAvailableShiftsOnAvailabilityTable();
			SimpleUtils.assertOnFail("It should have 7 shifts in availability table, but actual is: "+availableShiftsOnAvailabilityTable.size(),
					availableShiftsOnAvailabilityTable.size() == 7, false);
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String scheduleHoursValue = availabilityData.get("scheduleHoursValue").toString();
			SimpleUtils.assertOnFail("The scheduled hrs should be 38.5 in availability table, but actual is: "+scheduleHoursValue,
					(scheduleHoursValue.equals("38.5") || scheduleHoursValue.equals("42.0")), false);

			//Login as TM1
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(2000);
			availableShiftsOnAvailabilityTable = profileNewUIPage.getAvailableShiftsOnAvailabilityTable();
			SimpleUtils.assertOnFail("It should have 7 shifts in availability table, but actual is: "+availableShiftsOnAvailabilityTable.size(),
					availableShiftsOnAvailabilityTable.size() == 7, false);
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			scheduleHoursValue = availabilityData.get("scheduleHoursValue").toString();
			SimpleUtils.assertOnFail("The scheduled hrs should be 42 in availability table, but actual is: "+scheduleHoursValue,
					(scheduleHoursValue.equals("38.5") || scheduleHoursValue.equals("42.0")), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}


	}


	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the scheduled shifts and hrs will change on Availability table when update the scheduled shifts")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheScheduledShiftAndHrsOnAvailabilityTableWillChangedAccordinglyAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstNameOfTM = tmFullName.split(" ")[0];
			String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(!isActiveWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
				//Create one shift with shift time as 8am-2pm for TM1 per days
				ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
				ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
				ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
				scheduleMainPage.clickOnFilterBtn();
				scheduleMainPage.selectJobTitleFilterByText(jobTitle);
				String workRole = shiftOperatePage.getRandomWorkRole();
				scheduleMainPage.clickOnFilterBtn();
				scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
				scheduleMainPage.clickOnFilterBtn();
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
				scheduleMainPage.saveSchedule();
				scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
				NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
				newShiftPage.clickOnDayViewAddNewShiftButton();
				newShiftPage.customizeNewShiftPage();
				newShiftPage.clearAllSelectedDays();
				newShiftPage.selectSpecificWorkDay(7);
				newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
				newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
				newShiftPage.selectWorkRole(workRole);
				newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
				newShiftPage.clickOnCreateOrNextBtn();
				newShiftPage.searchTeamMemberByName(tmFullName);
				newShiftPage.clickOnOfferOrAssignBtn();
				scheduleMainPage.saveSchedule();
				createSchedulePage.publishActiveSchedule();
			}
			//Update one shift of TM1
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			WebElement shiftsOfTM = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM).get(0);
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.editTheShiftTimeForSpecificShift(shiftsOfTM, "8am", "8pm");
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();

			//Go to Roster, search and go to TM1's preference page
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(2000);
			//There are 7 shifts display and scheduled hrs display as 42
			List<String> availableShiftsOnAvailabilityTable = profileNewUIPage.getAvailableShiftsOnAvailabilityTable();
			SimpleUtils.assertOnFail("It should have 7 shifts in availability table, but actual is: "+availableShiftsOnAvailabilityTable.size(),
					availableShiftsOnAvailabilityTable.size() == 7, false);
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String scheduleHoursValue = availabilityData.get("scheduleHoursValue").toString();
			SimpleUtils.assertOnFail("The scheduled hrs should be 44.5 in availability table, but actual is: "+scheduleHoursValue,
					(scheduleHoursValue.equals("44.5") || (scheduleHoursValue.equals("45.0")) || scheduleHoursValue.equals("48.0")), false);

			//Login as TM1
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(2000);
			availableShiftsOnAvailabilityTable = profileNewUIPage.getAvailableShiftsOnAvailabilityTable();
			SimpleUtils.assertOnFail("It should have 7 shifts in availability table, but actual is: "+availableShiftsOnAvailabilityTable.size(),
					availableShiftsOnAvailabilityTable.size() == 7, false);
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			scheduleHoursValue = availabilityData.get("scheduleHoursValue").toString();
			SimpleUtils.assertOnFail("The scheduled hrs should be 44.5 in availability table, but actual is: "+scheduleHoursValue,
					(scheduleHoursValue.equals("44.5") || (scheduleHoursValue.equals("45.0")) || scheduleHoursValue.equals("48.0")), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}


	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "Vailqacn_Enterprise")
//	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the scheduled shifts and hrs on Availability table when there is no shift been scheduled")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheScheduledShiftAndHrsOnAvailabilityTablWhenThereIsNoShiftBeenScheduledAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstNameOfTM = tmFullName.split(" ")[0];
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(3000);
			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(!isActiveWeekGenerated){
				createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
			}
			//Remove all shifts for TM1
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();
			createSchedulePage.publishActiveSchedule();
			//Go to Roster, search and go to TM1's preference page
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(tmFullName);
			String workPreferencesLabel = "Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(2000);
			//There are 7 shifts display and scheduled hrs display as 42
			List<String> availableShiftsOnAvailabilityTable = profileNewUIPage.getAvailableShiftsOnAvailabilityTable();
			SimpleUtils.assertOnFail("It should have 7 shifts in availability table, but actual is: "+availableShiftsOnAvailabilityTable.size(),
					availableShiftsOnAvailabilityTable.size() == 0, false);
			HashMap<String, Object> availabilityData = profileNewUIPage.getMyAvailabilityData();
			String scheduleHoursValue = availabilityData.get("scheduleHoursValue").toString();
			SimpleUtils.assertOnFail("The scheduled hrs should be 0 in availability table, but actual is: "+scheduleHoursValue,
					scheduleHoursValue.equals("0.0"), false);

			//Login as TM1
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
			Thread.sleep(5000);
			profileNewUIPage.clickNextWeek();
			Thread.sleep(2000);
			availableShiftsOnAvailabilityTable = profileNewUIPage.getAvailableShiftsOnAvailabilityTable();
			SimpleUtils.assertOnFail("It should have 7 shifts in availability table, but actual is: "+availableShiftsOnAvailabilityTable.size(),
					availableShiftsOnAvailabilityTable.size() == 0, false);
			availabilityData = profileNewUIPage.getMyAvailabilityData();
			scheduleHoursValue = availabilityData.get("scheduleHoursValue").toString();
			SimpleUtils.assertOnFail("The scheduled hrs should be 0 in availability table, but actual is: "+scheduleHoursValue,
					scheduleHoursValue.equals("0.0"), false);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated ="Automated")
	@Owner(owner = "Mary")
//	@Enterprise(name = "Vailqacn_Enterprise")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Validate the approved or rejected availabilities request should not be able to operate")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void validateTheApprovedOrRejectedAvailabilitiesRequestShouldNotBeAbleToOperateAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try{
			ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
			TeamPage teamPage = pageFactory.createConsoleTeamPage();
			profileNewUIPage.clickOnUserProfileImage();
			profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
			String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
			String firstNameOfTM = tmFullName.split(" ")[0];
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			String preferencesLabel = "My Work Preferences";
			profileNewUIPage.selectProfilePageSubSectionByLabel(preferencesLabel);
			profileNewUIPage.cancelAllPendingAvailabilityRequest();
			while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
				profileNewUIPage.clickNextWeek();
			}

			String firstWeek = profileNewUIPage.getAvailabilityWeek();
			String repeatChanges = "This week only";
			String leftOrRightDuration = "Right";
			String hoursType = "When I prefer to work";
			HashMap<String, Object> myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();
//			if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
				int sliderIndex = 1;
				double hours = -0.5;//move 1 metric 0.5h left
				profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
						hours, repeatChanges);
//			} else {
//				profileNewUIPage.clickAvailabilityEditButton();
//				profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
//				profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
//			}

			profileNewUIPage.clickNextWeek();
			String secondWeek = profileNewUIPage.getAvailabilityWeek();
			myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();
//			if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
				sliderIndex = 1;
				hours = -0.5;//move 1 metric 0.5h left
				profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
						hours, repeatChanges);
//			} else {
//				profileNewUIPage.clickAvailabilityEditButton();
//				profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
//				profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
//			}
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
					dashboardPage.isDashboardPageLoaded() , false);
			teamPage.goToTeam();
			teamPage.searchAndSelectTeamMemberByName(firstNameOfTM);
			profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
			profileNewUIPage.approveOrRejectSpecificPendingAvailabilityRequest(firstWeek, "approve");
			profileNewUIPage.verifyTheApprovedOrRejectedAvailabilityRequestCannotBeOperated(firstWeek);
			profileNewUIPage.approveOrRejectSpecificPendingAvailabilityRequest(secondWeek, "reject");
			profileNewUIPage.verifyTheApprovedOrRejectedAvailabilityRequestCannotBeOperated(secondWeek);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate managers can convert to Open Shift on current day and past days")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateManagersCanConvertToOpenShiftAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			String accessRoleTab = "Access Roles";
			String permissionSection = "Schedule";
			String permission = "Edit Past Schedule";
			String actionOn = "on";
			//Go to the configuration page and set the labor budget and By Location
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
				userManagementPage.clickOnUserManagementTab();
				SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
				userManagementPage.goToUserAndRoles();
				controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);

				//Add the Edit Past Schedule permission for SM & DM
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
				String role = "CinemarkStoreManager";
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(permissionSection,role,permission,actionOn);
				role = "CinemarkDistrictManager";
				controlsNewUIPage.turnOnOrOffSpecificPermissionForSpecificRoles(permissionSection,role,permission,actionOn);
				cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
				switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(240000);

			//Go to schedule page and re-generate the schedule for current & past weeks
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			scheduleCommonPage.clickOnWeekView();
			scheduleCommonPage.navigateToPreviousWeek();
			boolean isPreviousActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if (isPreviousActiveWeekGenerated) {
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Login as SM, generate the schedule
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(240000);
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());

			//Switch to the DayView and convert one shift to the Open Shift.
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			scheduleCommonPage.clickOnDayView();
			String action = "Edit";
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.convertAllShiftsToOpenInDayView(action);
			scheduleMainPage.saveSchedule();

			//Switch the previous week and convert one past shift to the Open Shift.
			scheduleCommonPage.clickOnWeekView();
			scheduleCommonPage.navigateToPreviousWeek();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.convertAllShiftsToOpenInDayView(action);
			scheduleMainPage.saveSchedule();

			//Login as DM, generate the schedule
			loginPage.logOut();
			Thread.sleep(60000);
			loginAsDifferentRole(AccessRoles.DistrictManager.getValue());
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());

			//Switch to the DayView and convert one shift to the Open Shift.
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.convertAllShiftsToOpenInDayView(action);
			scheduleMainPage.saveSchedule();

			//Switch the previous week and convert one past shift to the Open Shift.
			scheduleCommonPage.clickOnWeekView();
			scheduleCommonPage.navigateToPreviousWeek();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.convertAllShiftsToOpenInDayView(action);
			scheduleMainPage.saveSchedule();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}
}
