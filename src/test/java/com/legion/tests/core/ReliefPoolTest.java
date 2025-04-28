package com.legion.tests.core;

import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.pages.core.schedule.ConsoleEditShiftPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ReliefPoolTest extends TestBase {

	private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");

	@Override
	@BeforeMethod()
	public void firstTest(Method testMethod, Object[] params) {
		try {
			this.createDriver((String) params[0], "69", "Window");
			visitPage(testMethod);
//			ToggleAPI.updateToggle(Toggles.ReliefPool.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), true);
//			ToggleAPI.updateToggle(Toggles.EnableForce24hTimeFormat.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), false);
//			ToggleAPI.updateToggle(Toggles.EnableSchTmView24hTimeFormat.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), false);
			loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the configurable of auto group toggle")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAutoGroupConfigsAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Collaboration page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
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
			String templateName = templateTypeAndName.get("Schedule Collaboration");
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");

			//Open the auto group toggle, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("Yes");
			configurationPage.publishNowTheTemplate();
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			Thread.sleep(10000);
			String activeBtnLabel1 = controlsNewUIPage.getAutoGroupToggleActiveBtnLabel();
			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel1.equalsIgnoreCase("Yes"),false);

			//Close the auto group toggle, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("No");
			configurationPage.publishNowTheTemplate();
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			Thread.sleep(10000);
			String activeBtnLabel2 = controlsNewUIPage.getAutoGroupToggleActiveBtnLabel();
			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel2.equalsIgnoreCase("No"),false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the configurable rules of open shift group")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyOpenShiftGroupRulesConfigsAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Collaboration page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
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
			String templateType = "Schedule Collaboration";
			String templateName = templateTypeAndName.get(templateType);
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad(templateType);
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");

			//Select different rules for open shift group, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("Yes");
			ArrayList<String> selectOptions = new ArrayList<>();
			selectOptions.add("ShiftPatternAssignment()");
			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
			configurationPage.publishNowTheTemplate();

			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			selectOptions.add("ScheduleConflictCheck()");
			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
			configurationPage.publishNowTheTemplate();

			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			selectOptions.add("AccrualBalanceCheck()");
			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
			configurationPage.publishNowTheTemplate();

			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			selectOptions.add("AssignmentRuleCheck()");
			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
			configurationPage.publishNowTheTemplate();

			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			selectOptions.add("TimeOffCheck()");
			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
			configurationPage.publishNowTheTemplate();

			//Select multiple rules, check the text in the input box
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			String text = "5 What criteria are used when evaluating employees for open shift offers? Selected";
			controlsNewUIPage.verifyTextInOpenShiftGroupRuleInputBox(text);

			//Group rules section disappears after close auto group toggle
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("No");
			SimpleUtils.assertOnFail("Group rules section still displays!", controlsNewUIPage.isOpenShiftGroupRuleSectionLoaded() == false,false);
			configurationPage.publishNowTheTemplate();

			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			SimpleUtils.assertOnFail("Group rules section still displays!", controlsNewUIPage.isOpenShiftGroupRuleSectionLoaded() == false,false);

			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("Yes");
			SimpleUtils.assertOnFail("Group rules section not displays!", controlsNewUIPage.isOpenShiftGroupRuleSectionLoaded() == true,false);
			configurationPage.publishNowTheTemplate();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the configurable advanced hours of auto approval")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyConfigOfAdvancedHoursAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Collaboration page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.searchLocation(location);
			SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
			locationsPage.clickOnLocationInLocationResult(location);
			locationsPage.clickOnConfigurationTabOfLocation();
			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
			String templateType = "Schedule Collaboration";
			String templateName = templateTypeAndName.get(templateType);
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad(templateType);
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");

			//Input advanced hours, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);

			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("Yes");
			controlsNewUIPage.isAutoApprovalSectionLoaded();
			String inputAdvancedHours = "48";
			controlsNewUIPage.setAutoApprovalAdvancedHours(inputAdvancedHours);
			configurationPage.publishNowTheTemplate();
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			Thread.sleep(10000);
			String advancedHours = controlsNewUIPage.getAutoApprovalAdvancedHours();
			SimpleUtils.assertOnFail("The advanced hours value is not expected!", advancedHours.equalsIgnoreCase(inputAdvancedHours),false);

			//Advance hours section disappears after close auto group toggle
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("No");
			SimpleUtils.assertOnFail("Advance hours section still displays!", controlsNewUIPage.isAutoApprovalSectionLoaded() == false,false);
			configurationPage.publishNowTheTemplate();

			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("Yes");
			SimpleUtils.assertOnFail("Advance hours section not displays!", controlsNewUIPage.isAutoApprovalSectionLoaded() == true,false);
			configurationPage.publishNowTheTemplate();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Group by Pattern filter in the schedule")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyGroupByPatternAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.goToLocationDetailsPage("panregularlocation2");
			locationsPage.goToConfigurationTabInLocationLevel();
			String workRole = "Master";
			String template = "Scheduling Rules";
			locationsPage.clickActionsForTemplate(template,"Edit");
			configurationPage.DeleteStaffingRulesForParticularRule(workRole);
			configurationPage.saveBtnIsClickable();

			//Add new master shift pattern
			ShiftPatternPage shiftPatternPage = pageFactory.createConsoleShiftPatternPage();
			locationsPage.clickActionsForTemplate(template,"Edit");
			configurationPage.selectWorkRoleToEdit(workRole);
			configurationPage.checkTheEntryOfAddShiftPatternRule();
			String patternName = "Master";
			String patternDescription = "Description";
			String patternInstances = "1";
			shiftPatternPage.inputNameDescriptionNInstances(patternName, patternDescription, patternInstances);
			shiftPatternPage.selectTheCurrentWeek();
			// Verify the work role should in role input
			shiftPatternPage.selectAddOnOrOffWeek(true);
			shiftPatternPage.clickOnAddShiftButton();
			shiftPatternPage.verifyWorkRoleNameShows(workRole);
			// Verify can create the shift pattern rule
			List<String> workDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"));
			shiftPatternPage.selectWorkDays(workDays);
			shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
			shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
			shiftPatternPage.clickOnCreateButton();
			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
//			configurationPage.clickOnSaveButtonOnScheduleRulesListPage();
			locationsPage.clickOnSaveButton();
			locationsPage.clickOnSaveButton();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(200000);


			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");

			//Check Group by Pattern filter in the group by list
			ArrayList<String> shiftTitles = new ArrayList<>();
			shiftTitles.add("MASTER");
			scheduleCommonPage.clickOnWeekView();
			scheduleMainPage.clickOnSuggestedButton();
			scheduleMainPage.validateGroupBySelectorSchedulePage(false);
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleShiftTablePage.verifyGroupCanbeCollapsedNExpanded();
			scheduleMainPage.isShiftTitleExist(shiftTitles);

			scheduleMainPage.clickOnManagerButton();
			scheduleMainPage.validateGroupBySelectorSchedulePage(false);
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleShiftTablePage.verifyGroupCanbeCollapsedNExpanded();
			scheduleMainPage.isShiftTitleExist(shiftTitles);

			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnSuggestedButton();
			scheduleMainPage.validateGroupBySelectorSchedulePage(false);
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleMainPage.isShiftTitleExist(shiftTitles);

			scheduleMainPage.clickOnManagerButton();
			scheduleMainPage.validateGroupBySelectorSchedulePage(false);
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleMainPage.isShiftTitleExist(shiftTitles);

			scheduleCommonPage.clickOnMultiWeekView();
			scheduleMainPage.validateGroupBySelectorSchedulePage(false);
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the function of open shift group")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyFunctionOfOpenShiftGroupAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Prepare master shift pattern at location level
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.goToLocationDetailsPage("panregularlocation2");
			locationsPage.goToConfigurationTabInLocationLevel();
			String patternName1 = "Master";
			String patternName2 = "Deckhand";
			String patternDescription = "Description";
			String patternInstances = "1";
			String template = "Scheduling Rules";
			locationsPage.clickActionsForTemplate(template,"Edit");
			configurationPage.selectWorkRoleToEdit(patternName1);
			configurationPage.deleteScheduleRules();
			locationsPage.clickOnSaveButton();
			configurationPage.selectWorkRoleToEdit(patternName2);
			configurationPage.deleteScheduleRules();
			locationsPage.clickOnSaveButton();

			//Add new Master shift pattern
			ShiftPatternPage shiftPatternPage = pageFactory.createConsoleShiftPatternPage();
			configurationPage.selectWorkRoleToEdit(patternName1);
			configurationPage.checkTheEntryOfAddShiftPatternRule();

			shiftPatternPage.inputNameDescriptionNInstances(patternName1, patternDescription, patternInstances);
			shiftPatternPage.selectTheCurrentWeek();

			shiftPatternPage.selectAddOnOrOffWeek(true);
			shiftPatternPage.clickOnAddShiftButton();

			List<String> workDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"));
			shiftPatternPage.selectWorkDays(workDays);
			shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
			shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
			shiftPatternPage.clickOnCreateButton();
			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
			locationsPage.clickOnSaveButton();

			//Add new Deckhand shift pattern
			configurationPage.selectWorkRoleToEdit(patternName2);
			configurationPage.checkTheEntryOfAddShiftPatternRule();
			shiftPatternPage.inputNameDescriptionNInstances(patternName2, patternDescription, patternInstances);
			shiftPatternPage.selectTheCurrentWeek();

			// Verify the work role should in role input
			shiftPatternPage.selectAddOnOrOffWeek(true);
			shiftPatternPage.clickOnAddShiftButton();

			// Verify can create the shift pattern rule
			shiftPatternPage.selectWorkDays(workDays);
			shiftPatternPage.inputStartOrEndTime("7", "0", "a", true);
			shiftPatternPage.inputStartOrEndTime("7", "0", "p", false);
			shiftPatternPage.clickOnCreateButton();
			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
			locationsPage.clickOnSaveButton();
			locationsPage.clickOnSaveButton();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(200000);

			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
			createSchedulePage.publishActiveSchedule();

			//TM can receive open shift group
			scheduleCommonPage.clickOnWeekView();
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(patternName1);

			EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
			String action = "Edit";
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
			editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption());
			editShiftPage.clickOnUpdateButton();

//			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
//			shiftOperatePage.clickOnOfferTMOption();
			String tmName = "Sebastian";
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();

			scheduleMainPage.searchShiftOnSchedulePage(patternName2);
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();

			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnDayView();
			ArrayList<String> expectedWorkDays = mySchedulePage.getAllWeekDays();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();

			//Login as TM, check the open shift group
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");
//			SimpleUtils.assertOnFail("Didn't get open shift group offer!", scheduleShiftTablePage.getShiftsCount() == 14, false);
//			SimpleUtils.assertOnFail("The shift hours were incorrect!", scheduleShiftTablePage.getShiftTextByIndex(0).replaceAll(" ", "").contains("6:00am-4:00pm"), false );

			int shiftCount = 7;
			String locationName = "SeaspanRegularLocation2";
			String shiftTimePeriod1 = "9:00am-5:00pm";
			String shiftTimePeriod2 = "7:00am-7:00pm";
			List<String> expectedRequest = Arrays.asList("View Group Offer(" + shiftCount + ")");
			ArrayList<String> weekDays = new ArrayList<String>(Arrays.asList("Sun","Mon","Tue","Wed","Thu","Fri","Sat"));
			mySchedulePage.checkOpenShiftGroup(shiftCount, shiftTimePeriod1, patternName1, weekDays, expectedWorkDays,locationName);
			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
			mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();

			//Decline the open shift group offer
			mySchedulePage.checkOpenShiftGroup(shiftCount, shiftTimePeriod2, patternName2, weekDays, expectedWorkDays,locationName);
			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
			mySchedulePage.clickOnDeclineButton();

			//Cancel claim the open shift group offer
			mySchedulePage.cancelClaimOpenShiftGroupRequest(expectedRequest,patternName1);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the layout of open shift group in Manager Activity")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyRequestInActivityAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Set up manager approval process
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
			String templateName = templateTypeAndName.get("Schedule Collaboration");
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			configurationPage.enableOrDisableApproveShiftInHomeLocationSetting("yes");
			configurationPage.enableOrDisableApproveShiftInNonHomeLocationSetting("yes");
			controlsNewUIPage.setAutoApprovalAdvancedHours("0");
			configurationPage.publishNowTheTemplate();

			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.goToLocationDetailsPage("panregularlocation2");
			locationsPage.goToConfigurationTabInLocationLevel();
			String patternName = "Master";
			String patternDescription = "Description";
			String patternInstances = "1";
			String patternTemplate = "Scheduling Rules";
			locationsPage.clickActionsForTemplate(patternTemplate,"Edit");
			configurationPage.selectWorkRoleToEdit(patternName);
			configurationPage.deleteScheduleRules();
			locationsPage.clickOnSaveButton();

			//Add new Master shift pattern
			ShiftPatternPage shiftPatternPage = pageFactory.createConsoleShiftPatternPage();
			configurationPage.selectWorkRoleToEdit(patternName);
			configurationPage.checkTheEntryOfAddShiftPatternRule();

			shiftPatternPage.inputNameDescriptionNInstances(patternName, patternDescription, patternInstances);
			shiftPatternPage.selectTheCurrentWeek();

			shiftPatternPage.selectAddOnOrOffWeek(true);
			shiftPatternPage.clickOnAddShiftButton();
			shiftPatternPage.verifyWorkRoleNameShows(patternName);

			List<String> workDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"));
			shiftPatternPage.selectWorkDays(workDays);
			shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
			shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
			shiftPatternPage.clickOnCreateButton();
			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
			locationsPage.clickOnSaveButton();
			locationsPage.clickOnSaveButton();

			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(30000);


			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
			createSchedulePage.publishActiveSchedule();

			//TM can receive open shift group
			String tmName = "Sebastian";
			scheduleCommonPage.clickOnWeekView();
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(patternName);
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();

			//Login as TM, check the open shift group
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnDayView();
			ArrayList<String> expectedWorkDays = mySchedulePage.getAllWeekDays();
//			System.out.print(expectedWorkDays.get(0) + " - " + expectedWorkDays.get(expectedWorkDays.size()-1));
			ArrayList<String> shiftTimePeriodInActivity = mySchedulePage.getAllWeekDaysForActivityDetailVerification();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(60000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");

			int shiftCount = 7;
			String locationName = "SeaspanRegularLocation2";
			String shiftTimePeriod = "9:00am-5:00pm";
			ArrayList<String> weekDays = new ArrayList<String>(Arrays.asList("Sun","Mon","Tue","Wed","Thu","Fri","Sat"));
			mySchedulePage.checkOpenShiftGroup(shiftCount, shiftTimePeriod, patternName, weekDays, expectedWorkDays,locationName);
			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
			mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();

			//Login as SM, check folded open shift group request
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			ActivityPage activityPage = pageFactory.createConsoleActivityPage();
			activityPage.verifyActivityBellIconLoaded();
			activityPage.verifyClickOnActivityIcon();
			activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(),
					ActivityTest.indexOfActivityType.ShiftOffer.name());
			String openShiftGroupPeriod = shiftTimePeriodInActivity.get(0) + " - " + shiftTimePeriodInActivity.get(shiftTimePeriodInActivity.size()-1);
			activityPage.verifyNewClaimOpenShiftGroupCardShowsOnActivity(0,tmName, patternName, openShiftGroupPeriod, locationName);
			SimpleUtils.assertOnFail("Should load Approval and Reject buttons!",
					activityPage.isApproveRejectBtnsLoaded(0), false);

			ArrayList<String> shiftsDateInActivityRequest = new ArrayList<String>();
			String template = null;
			for(int i = 0; i<shiftTimePeriodInActivity.size(); i++){
				template = weekDays.get(i) + shiftTimePeriodInActivity.get(i).split(" ")[1];
				shiftsDateInActivityRequest.add(template);
			}
			boolean isGroupFolded = activityPage.isOpenShiftGroupRequestFolded();
			SimpleUtils.assertOnFail("Open Shift Group request is not folded!", isGroupFolded == true,false);
			activityPage.clickDetailLinksInActivitiesByIndex(0);
			isGroupFolded = activityPage.isOpenShiftGroupRequestFolded();
			SimpleUtils.assertOnFail("Open Shift Group request is folded!", isGroupFolded == false,false);

			String shiftGroupTitle = "Open Shift Group";
			String shiftExpiryDate = "Offer expires at " + shiftTimePeriodInActivity.get(0) + ", 9:00 AM";
			activityPage.verifyContentOfOpenShiftGroupRequestOnActivity(shiftGroupTitle, shiftExpiryDate, shiftsDateInActivityRequest,shiftTimePeriod,patternName,locationName);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the behaviour of manager in activity")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyManagerApprovalAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Set up manager approval process
//			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
//			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
//			locationsPage.clickOnLocationsTab();
//			locationsPage.goToSubLocationsInLocationsPage();
//			locationsPage.searchLocation(location);
//			SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
//			locationsPage.clickOnLocationInLocationResult(location);
//			locationsPage.clickOnConfigurationTabOfLocation();
//			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
//			String templateName = templateTypeAndName.get("Schedule Collaboration");
//			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
//			configurationPage.goToConfigurationPage();
//			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
//			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
//			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
//			Thread.sleep(3000);
//			configurationPage.enableOrDisableApproveShiftInHomeLocationSetting("yes");
//			configurationPage.enableOrDisableApproveShiftInNonHomeLocationSetting("yes");
//			controlsNewUIPage.setAutoApprovalAdvancedHours("0");
//			controlsNewUIPage.isAutoGroupSectionLoaded();
//			controlsNewUIPage.updateAutoGroupToggle("Yes");
//			ArrayList<String> selectOptions = new ArrayList<>();
//			selectOptions.add("ShiftPatternAssignment()");
//			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
//			configurationPage.publishNowTheTemplate();
//
//			locationsPage.clickOnLocationsTab();
//			locationsPage.goToSubLocationsInLocationsPage();
//			locationsPage.goToLocationDetailsPage("panregularlocation2");
//			locationsPage.goToConfigurationTabInLocationLevel();
			String patternName1 = "Master";
			String patternName2 = "Deckhand";
			String patternDescription = "Description";
			String patternInstances = "1";
			String template = "Scheduling Rules";
//			locationsPage.clickActionsForTemplate(patternTemplate,"Edit");
//			configurationPage.selectWorkRoleToEdit(patternName);
//			configurationPage.deleteScheduleRules();
//			locationsPage.clickOnSaveButton();


			//Prepare shift patterns at location level
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
//			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
//			SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
//			locationsPage.clickOnLocationsTab();
//			locationsPage.goToSubLocationsInLocationsPage();
//			locationsPage.goToLocationDetailsPage("panregularlocation2");
//			locationsPage.goToConfigurationTabInLocationLevel();
//			locationsPage.clickActionsForTemplate(template,"Edit");
//			configurationPage.selectWorkRoleToEdit(patternName1);
//			configurationPage.deleteScheduleRules();
//			locationsPage.clickOnSaveButton();
//			configurationPage.selectWorkRoleToEdit(patternName2);
//			configurationPage.deleteScheduleRules();
//			locationsPage.clickOnSaveButton();

			//Add new Master shift pattern
//			ShiftPatternPage shiftPatternPage = pageFactory.createConsoleShiftPatternPage();
//			configurationPage.selectWorkRoleToEdit(patternName1);
//			configurationPage.checkTheEntryOfAddShiftPatternRule();
//
//			shiftPatternPage.inputNameDescriptionNInstances(patternName1, patternDescription, patternInstances);
//			shiftPatternPage.selectTheCurrentWeek();
//
//			shiftPatternPage.selectAddOnOrOffWeek(true);
//			shiftPatternPage.clickOnAddShiftButton();
//			shiftPatternPage.verifyWorkRoleNameShows(patternName1);
//
//			List<String> workDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"));
//			shiftPatternPage.selectWorkDays(workDays);
//			shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
//			shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
//			shiftPatternPage.clickOnCreateButton();
//			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
//			locationsPage.clickOnSaveButton();

			//Add new Deckhand shift pattern
//			configurationPage.selectWorkRoleToEdit(patternName2);
//			configurationPage.checkTheEntryOfAddShiftPatternRule();
//
//			shiftPatternPage.inputNameDescriptionNInstances(patternName2, patternDescription, patternInstances);
//			shiftPatternPage.selectTheCurrentWeek();
//			// Verify the work role should in role input
//			shiftPatternPage.selectAddOnOrOffWeek(true);
//			shiftPatternPage.clickOnAddShiftButton();
//			shiftPatternPage.verifyWorkRoleNameShows(patternName2);
//			// Verify can create the shift pattern rule
//			shiftPatternPage.selectWorkDays(workDays);
//			shiftPatternPage.inputStartOrEndTime("7", "0", "a", true);
//			shiftPatternPage.inputStartOrEndTime("7", "0", "p", false);
//			shiftPatternPage.clickOnCreateButton();
//			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
//			locationsPage.clickOnSaveButton();
//			locationsPage.clickOnSaveButton();
//			switchToConsoleWindow();
//			refreshCachesAfterChangeTemplate();
//			Thread.sleep(200000);

			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
			createSchedulePage.publishActiveSchedule();

			//TM can receive open shift group
			String tmName = "Sebastian";
			scheduleCommonPage.clickOnWeekView();
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(patternName1);
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();

			scheduleMainPage.searchShiftOnSchedulePage(patternName2);
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();

			//Login as TM, check the open shift group
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnDayView();
			ArrayList<String> expectedWorkDays = mySchedulePage.getAllWeekDays();
//			System.out.print(expectedWorkDays.get(0) + " - " + expectedWorkDays.get(expectedWorkDays.size()-1));
			ArrayList<String> shiftTimePeriodInActivity = mySchedulePage.getAllWeekDaysForActivityDetailVerification();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(60000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");

			int shiftCount = 7;
			String locationName = "SeaspanRegularLocation2";
			String shiftTimePeriod1 = "9:00am-5:00pm";
			String shiftTimePeriod2 = "7:00am-7:00pm";
			ArrayList<String> weekDays = new ArrayList<String>(Arrays.asList("Sun","Mon","Tue","Wed","Thu","Fri","Sat"));
			mySchedulePage.checkOpenShiftGroup(shiftCount, shiftTimePeriod1, patternName1, weekDays, expectedWorkDays,locationName);
			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
			mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();

			mySchedulePage.checkOpenShiftGroup(shiftCount, shiftTimePeriod2, patternName2, weekDays, expectedWorkDays,locationName);
			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
			mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();

			//Login as SM, check folded open shift group request
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			ActivityPage activityPage = pageFactory.createConsoleActivityPage();
			activityPage.verifyActivityBellIconLoaded();
			activityPage.verifyClickOnActivityIcon();
			activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(),
					ActivityTest.indexOfActivityType.ShiftOffer.name());
			String openShiftGroupPeriod = shiftTimePeriodInActivity.get(0) + " - " + shiftTimePeriodInActivity.get(shiftTimePeriodInActivity.size()-1);
			activityPage.verifyNewClaimOpenShiftGroupCardShowsOnActivity(1,tmName, patternName1, openShiftGroupPeriod, locationName);
			SimpleUtils.assertOnFail("Should load Approval and Reject buttons!",
					activityPage.isApproveRejectBtnsLoaded(1), false);
			activityPage.approveOrRejectOpenShiftGroupRequestOnActivity(1,"Approve");

			activityPage.verifyNewClaimOpenShiftGroupCardShowsOnActivity(1,tmName, patternName2, openShiftGroupPeriod, locationName);
			SimpleUtils.assertOnFail("Should load Approval and Reject buttons!",
					activityPage.isApproveRejectBtnsLoaded(1), false);
			activityPage.approveOrRejectOpenShiftGroupRequestOnActivity(1,"Reject");

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the manager can not approve or reject request in activity")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyDisableManagerApprovalAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Set up manager approval process
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
			String templateName = templateTypeAndName.get("Schedule Collaboration");
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			configurationPage.enableOrDisableApproveShiftInHomeLocationSetting("yes");
			configurationPage.enableOrDisableApproveShiftInNonHomeLocationSetting("yes");
			controlsNewUIPage.setAutoApprovalAdvancedHours("48");
			configurationPage.publishNowTheTemplate();

//			locationsPage.clickOnLocationsTab();
//			locationsPage.goToSubLocationsInLocationsPage();
//			locationsPage.goToLocationDetailsPage("panregularlocation2");
//			locationsPage.goToConfigurationTabInLocationLevel();
			String patternName = "Master";
			String patternDescription = "Description";
			String patternInstances = "1";
//			String patternTemplate = "Scheduling Rules";
//			locationsPage.clickActionsForTemplate(patternTemplate,"Edit");
//			configurationPage.selectWorkRoleToEdit(patternName);
//			configurationPage.deleteScheduleRules();
//			locationsPage.clickOnSaveButton();

			//Add new Master shift pattern
//			ShiftPatternPage shiftPatternPage = pageFactory.createConsoleShiftPatternPage();
//			configurationPage.selectWorkRoleToEdit(patternName);
//			configurationPage.checkTheEntryOfAddShiftPatternRule();
//
//			shiftPatternPage.inputNameDescriptionNInstances(patternName, patternDescription, patternInstances);
//			shiftPatternPage.selectTheCurrentWeek();
//
//			shiftPatternPage.selectAddOnOrOffWeek(true);
//			shiftPatternPage.clickOnAddShiftButton();
//			shiftPatternPage.verifyWorkRoleNameShows(patternName);
//
//			List<String> workDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"));
//			shiftPatternPage.selectWorkDays(workDays);
//			shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
//			shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
//			shiftPatternPage.clickOnCreateButton();
//			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
//			locationsPage.clickOnSaveButton();
//			locationsPage.clickOnSaveButton();

			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(30000);


			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
			createSchedulePage.publishActiveSchedule();

			//TM can receive open shift group
			String tmName = "Sebastian";
			scheduleCommonPage.clickOnWeekView();
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(patternName);
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();

			//Login as TM, claim the open shift group
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnDayView();
			ArrayList<String> expectedWorkDays = mySchedulePage.getAllWeekDays();
			ArrayList<String> shiftTimePeriodInActivity = mySchedulePage.getAllWeekDaysForActivityDetailVerification();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(60000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");

			int shiftCount = 7;
			String locationName = "SeaspanRegularLocation2";
			String shiftTimePeriod = "9:00am-5:00pm";
			ArrayList<String> weekDays = new ArrayList<String>(Arrays.asList("Sun","Mon","Tue","Wed","Thu","Fri","Sat"));
			mySchedulePage.checkOpenShiftGroup(shiftCount, shiftTimePeriod, patternName, weekDays, expectedWorkDays,locationName);
			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
			mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();

			//Login as SM, check the request's approve & reject buttons
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			ActivityPage activityPage = pageFactory.createConsoleActivityPage();
			activityPage.verifyActivityBellIconLoaded();
			activityPage.verifyClickOnActivityIcon();
			activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(),
					ActivityTest.indexOfActivityType.ShiftOffer.name());
			String openShiftGroupPeriod = shiftTimePeriodInActivity.get(0) + " - " + shiftTimePeriodInActivity.get(shiftTimePeriodInActivity.size()-1);
			activityPage.verifyNewClaimOpenShiftGroupCardShowsOnActivity(0,tmName, patternName, openShiftGroupPeriod, locationName);
			SimpleUtils.assertOnFail("Should not load Approval and Reject buttons!",
					!activityPage.isApproveRejectBtnsLoaded(0), false);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the usable of manager activity")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyManagerApprovalAndRejectionAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			//Set up manager approval process
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
			String templateName = templateTypeAndName.get("Schedule Collaboration");
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			configurationPage.enableOrDisableApproveShiftInHomeLocationSetting("yes");
			configurationPage.enableOrDisableApproveShiftInNonHomeLocationSetting("yes");
			controlsNewUIPage.setAutoApprovalAdvancedHours("48");
			configurationPage.publishNowTheTemplate();

//			locationsPage.clickOnLocationsTab();
//			locationsPage.goToSubLocationsInLocationsPage();
//			locationsPage.goToLocationDetailsPage("panregularlocation2");
//			locationsPage.goToConfigurationTabInLocationLevel();
			String patternName = "Master";
			String patternDescription = "Description";
			String patternInstances = "1";
//			String patternTemplate = "Scheduling Rules";
//			locationsPage.clickActionsForTemplate(patternTemplate,"Edit");
//			configurationPage.selectWorkRoleToEdit(patternName);
//			configurationPage.deleteScheduleRules();
//			locationsPage.clickOnSaveButton();

			//Add new Master shift pattern
//			ShiftPatternPage shiftPatternPage = pageFactory.createConsoleShiftPatternPage();
//			configurationPage.selectWorkRoleToEdit(patternName);
//			configurationPage.checkTheEntryOfAddShiftPatternRule();
//
//			shiftPatternPage.inputNameDescriptionNInstances(patternName, patternDescription, patternInstances);
//			shiftPatternPage.selectTheCurrentWeek();
//
//			shiftPatternPage.selectAddOnOrOffWeek(true);
//			shiftPatternPage.clickOnAddShiftButton();
//			shiftPatternPage.verifyWorkRoleNameShows(patternName);
//
//			List<String> workDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"));
//			shiftPatternPage.selectWorkDays(workDays);
//			shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
//			shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
//			shiftPatternPage.clickOnCreateButton();
//			configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
//			locationsPage.clickOnSaveButton();
//			locationsPage.clickOnSaveButton();

			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(30000);


			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
			createSchedulePage.publishActiveSchedule();

			//TM can receive open shift group
			String tmName = "Sebastian";
			scheduleCommonPage.clickOnWeekView();
			scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(patternName);
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();

			//Login as TM, claim the open shift group
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnDayView();
			ArrayList<String> expectedWorkDays = mySchedulePage.getAllWeekDays();
			ArrayList<String> shiftTimePeriodInActivity = mySchedulePage.getAllWeekDaysForActivityDetailVerification();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(60000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");

			int shiftCount = 7;
			String locationName = "SeaspanRegularLocation2";
			String shiftTimePeriod = "9:00am-5:00pm";
			ArrayList<String> weekDays = new ArrayList<String>(Arrays.asList("Sun","Mon","Tue","Wed","Thu","Fri","Sat"));
			mySchedulePage.checkOpenShiftGroup(shiftCount, shiftTimePeriod, patternName, weekDays, expectedWorkDays,locationName);
			mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
			mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();

			//Login as SM, check the request's approve & reject buttons
			loginPage.logOut();
			loginAsDifferentRole(AccessRoles.StoreManager.getValue());
			ActivityPage activityPage = pageFactory.createConsoleActivityPage();
			activityPage.verifyActivityBellIconLoaded();
			activityPage.verifyClickOnActivityIcon();
			activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(),
					ActivityTest.indexOfActivityType.ShiftOffer.name());
			String openShiftGroupPeriod = shiftTimePeriodInActivity.get(0) + " - " + shiftTimePeriodInActivity.get(shiftTimePeriodInActivity.size()-1);
			activityPage.verifyNewClaimOpenShiftGroupCardShowsOnActivity(0,tmName, patternName, openShiftGroupPeriod, locationName);
			SimpleUtils.assertOnFail("Should not load Approval and Reject buttons!",
					!activityPage.isApproveRejectBtnsLoaded(0), false);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate open shift groups of other work roles won't offer to employee")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyRoleViolationOfReliefPoolAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
//			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
//			locationsPage.clickOnLocationsTab();
//			locationsPage.goToSubLocationsInLocationsPage();
//			locationsPage.searchLocation(location);
//			SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
//			locationsPage.clickOnLocationInLocationResult(location);
//			locationsPage.clickOnConfigurationTabOfLocation();
//			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
//			String templateName = templateTypeAndName.get("Schedule Collaboration");
//			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
//			configurationPage.goToConfigurationPage();
//			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
//			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
//			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
//			Thread.sleep(3000);
//
//			controlsNewUIPage.isAutoGroupSectionLoaded();
//			controlsNewUIPage.updateAutoGroupToggle("Yes");
//			ArrayList<String> selectOptions = new ArrayList<>();
//			selectOptions.add("AssignmentRuleCheck()");
//			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
//			configurationPage.publishNowTheTemplate();
//			switchToConsoleWindow();
//			refreshCachesAfterChangeTemplate();
//			Thread.sleep(30000);


//			Go to Schedule page, create a schedule
//			goToSchedulePageScheduleTab();
//			scheduleCommonPage.navigateToNextWeek();
//			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//			if(isActiveWeekGenerated){
//				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//			}
//			Thread.sleep(5000);
//			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
//			createSchedulePage.publishActiveSchedule();


			//Login as TM, check the open shift group
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
//			Thread.sleep(120000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");
			int shiftCount = 7;
			String availableRule = "Master";
			String unavailableRule = "Deckhand";
			SimpleUtils.assertOnFail("Shift assignment rule of open shift group is not working!",
					mySchedulePage.isOpenShiftGroupDisplayed(shiftCount,availableRule) & !mySchedulePage.isOpenShiftGroupDisplayed(shiftCount,unavailableRule),false);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate open shift groups of other work roles won't offer to employee")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduleConflictWhenTimePeriodOverlappingOfReliefPoolAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
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
			String templateName = templateTypeAndName.get("Schedule Collaboration");
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);

			controlsNewUIPage.isAutoGroupSectionLoaded();
			controlsNewUIPage.updateAutoGroupToggle("Yes");
			ArrayList<String> selectOptions = new ArrayList<>();
			selectOptions.add("ScheduleConflictCheck()");
			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(30000);


			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");

			//Create multiple open shifts and assign to employee to incur conflicts
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			String tmName = "Sebastian";
			String shiftWorkRole1 = "Master";
			String shiftWorkRole2 = "Mate";
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(2,4,4);
			newShiftPage.selectWorkRole(shiftWorkRole1);
			newShiftPage.moveSliderAtCertainPoint("05:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("09:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.publishOrRepublishSchedule();

			//Login as TM, check the open shift group
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnDayView();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(120000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");
			SimpleUtils.assertOnFail("Shift assignment rule of open shift group is not working!",
					!mySchedulePage.isOpenShiftGroupDisplayed(7,shiftWorkRole1) & !mySchedulePage.isOpenShiftGroupDisplayed(5,shiftWorkRole2),false);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate open shift groups can be offered to employee if time period not overlapping")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyScheduleConflictNotTriggerWhenTimePeriodNotIncludedOfReliefPoolAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
//			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
//			locationsPage.clickOnLocationsTab();
//			locationsPage.goToSubLocationsInLocationsPage();
//			locationsPage.searchLocation(location);
//			SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
//			locationsPage.clickOnLocationInLocationResult(location);
//			locationsPage.clickOnConfigurationTabOfLocation();
//			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
//			String templateName = templateTypeAndName.get("Schedule Collaboration");
//			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
//			configurationPage.goToConfigurationPage();
//			configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
//			configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
//			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
//			Thread.sleep(3000);
//
//			controlsNewUIPage.isAutoGroupSectionLoaded();
//			controlsNewUIPage.updateAutoGroupToggle("Yes");
//			ArrayList<String> selectOptions = new ArrayList<>();
//			selectOptions.add("ScheduleConflictCheck()");
//			controlsNewUIPage.selectOpenShiftGroupRule(selectOptions);
//			configurationPage.publishNowTheTemplate();
//			switchToConsoleWindow();
//			refreshCachesAfterChangeTemplate();
//			Thread.sleep(30000);


			// Go to Schedule page, create a schedule
			goToSchedulePageScheduleTab();
			scheduleCommonPage.navigateToNextWeek();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");

			//Create multiple open shifts and assign to employee to incur conflicts
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			String tmName = "Sebastian";
			String shiftWorkRole1 = "Master";
			String shiftWorkRole2 = "Deckhand";
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.clearAllSelectedDays();
			newShiftPage.selectDaysByIndex(0,6,6);
			newShiftPage.selectWorkRole(shiftWorkRole1);
			newShiftPage.moveSliderAtCertainPoint("08:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("01:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(tmName);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.publishOrRepublishSchedule();

			//Login as TM, check the open shift group
			MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
			scheduleCommonPage.clickOnDayView();
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			loginPage.logOut();
			Thread.sleep(120000);
			loginAsDifferentRole(AccessRoles.TeamMember.getValue());
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
			Thread.sleep(3000);
			scheduleCommonPage.navigateToNextWeek();
			smartCardPage.clickLinkOnSmartCardByName("View Shifts");
			SimpleUtils.assertOnFail("Open shift group should be offered to TM when not overlapping with regular shifts!",
					mySchedulePage.isOpenShiftGroupDisplayed(5,shiftWorkRole2),false);


		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

}