package com.legion.tests.core;

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
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import static com.legion.utils.MyThreadLocal.*;


public class ScheduleSeniorityTest extends TestBase {

	private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");

	@Override
	@BeforeMethod()
	public void firstTest(Method testMethod, Object[] params) {
		try {
			this.createDriver((String) params[0], "69", "Window");
			visitPage(testMethod);
			//ToggleAPI.updateToggle(Toggles.EnableSenioritySchedule.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), true);
			loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the configurable of seniority toggle")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySeniorityConfigsOnSchedulingPolicyPageAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
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
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Seniority by enterprise, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
			configurationPage.publishNowTheTemplate();
//			Thread.sleep(10000);
//			String activeBtnLabel = controlsNewUIPage.getSeniorityToggleActiveBtnLabel();
//			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel.equalsIgnoreCase("Yes"),false);

			//Edit the seniority toggle as No seniority, save the change
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("No Seniority");
			configurationPage.publishNowTheTemplate();
//			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
//			Thread.sleep(10000);
//			activeBtnLabel = controlsNewUIPage.getSeniorityToggleActiveBtnLabel();
//			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel.equalsIgnoreCase("No"),false);

			//Edit the seniority toggle as Seniority by work role, save the change
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("Seniority by work role");
			configurationPage.publishNowTheTemplate();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the configurable of seniority sort")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySeniorityOrderOnSchedulingPolicyPageAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
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
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority sort as Ascending, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.selectSortOfSeniority("Seniority by enterprise","Ascending");
			configurationPage.publishNowTheTemplate();
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			Thread.sleep(10000);
			String senioritySort = controlsNewUIPage.getSenioritySort();
			SimpleUtils.assertOnFail("The seniority sort is not expected!",senioritySort.equalsIgnoreCase("Ascending"),false);

			//Edit the seniority sort as Descending, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.selectSortOfSeniority("Seniority by enterprise","Descending");
			configurationPage.publishNowTheTemplate();
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			Thread.sleep(10000);
			senioritySort = controlsNewUIPage.getSenioritySort();
			SimpleUtils.assertOnFail("The seniority sort is not expected!",senioritySort.equalsIgnoreCase("Descending"),false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Seniority on TM searching dialog when toggle is off")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySeniorityNotDisplayWhenToggleTurnOffForSearchingAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
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
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as No Seniority, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("No Seniority");
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);
			refreshCachesAfterChangeTemplate();

			//Back to Schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
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
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("3pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();

			EditShiftPage editShiftPage =pageFactory.createEditShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
			String action = "Edit";
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
			editShiftPage.clickOnAssignmentSelect();
			editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption());
			editShiftPage.clickOnUpdateButton();

			newShiftPage.searchEmployee(firstNameOfTM);
			SimpleUtils.assertOnFail("The Seniority Column is displayed on searching dialog!", !(shiftOperatePage.isSeniorityColumnLoaded()), false);
			newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift(firstNameOfTM, false);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			//Create a new shift which using the TM name & role above, check the Seniority Column
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchEmployee(firstNameOfTM);
			SimpleUtils.assertOnFail("The Seniority Column is displayed on searching dialog!", !(shiftOperatePage.isSeniorityColumnLoaded()), false);
			newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift(firstNameOfTM, false);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up the created shift, assign it to other TM, check the Seniority Column
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
			editShiftPage.clickOnAssignmentSelect();
			editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption());
			editShiftPage.clickOnUpdateButton();
			newShiftPage.searchEmployee(firstNameOfTM);
			SimpleUtils.assertOnFail("The Seniority Column is displayed on searching dialog!", !(shiftOperatePage.isSeniorityColumnLoaded()), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Seniority on TM recommend dialog when toggle is off")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySeniorityNotDisplayWhenToggleTurnOffForRecommendAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
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
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as No Seniority, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("No Seniority");
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(200000);

			//Back to Schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
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
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("3pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();

			EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
			String action = "Edit";
			scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
			editShiftPage.clickOnAssignmentSelect();
			editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption());
			editShiftPage.clickOnUpdateButton();

//			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
//			shiftOperatePage.clickOnOfferTMOption();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			shiftOperatePage.verifyRecommendedTableHasTM();
			SimpleUtils.assertOnFail("The Seniority Column is displayed on searching dialog!", !(shiftOperatePage.isSeniorityColumnLoaded()), false);
			newShiftPage.clickCloseBtnForOfferShift();
//			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			//Create a new shift which using the TM name & role above, check the Seniority Column
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			SimpleUtils.assertOnFail("The Seniority Column is displayed on searching dialog!", !(shiftOperatePage.isSeniorityColumnLoaded()), false);
			newShiftPage.clickCloseBtnForCreateShift();

			//Pick up the created shift, assign it to other TM, check the Seniority Column
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.clickOnOpenSearchBoxButton();
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
			SimpleUtils.assertOnFail("The Seniority Column is displayed on searching dialog!", !(shiftOperatePage.isSeniorityColumnLoaded()), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Seniority on TM searching dialog when toggle is on")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySeniorityDisplayWhenToggleTurnOnForSearchingAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.searchLocation(location);
			SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
			locationsPage.clickOnLocationInLocationResult(location);
			locationsPage.clickOnConfigurationTabOfLocation();
			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Seniority by enterprise, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
			configurationPage.publishNowTheTemplate();
//			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
//			Thread.sleep(10000);
//			String activeBtnLabel = controlsNewUIPage.getSeniorityToggleActiveBtnLabel();
//			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel.equalsIgnoreCase("Yes"),false);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);
			refreshCachesAfterChangeTemplate();

			//Back to Schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
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
			if (isActiveWeekGenerated) {
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
			}

			//Create an open shift, then offer it to the TMs
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("3pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.publishOrRepublishSchedule();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchText(firstNameOfTM);
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			//Create a new shift which using the TM name & role above, check the Seniority Column
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickClearAssignmentsLink();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up the created shift, assign it to other TM, check the Seniority Column
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			newShiftPage.searchText(firstNameOfTM);
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the visibility of seniority column under searching tab")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySeniorityAlwaysDisplayOnSearchingDialogAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			locationsPage.clickOnLocationsTab();
			locationsPage.goToSubLocationsInLocationsPage();
			locationsPage.searchLocation(location);
			SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
			locationsPage.clickOnLocationInLocationResult(location);
			locationsPage.clickOnConfigurationTabOfLocation();
			HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Seniority by enterprise, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
			configurationPage.publishNowTheTemplate();
//			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
//			Thread.sleep(10000);
//			String activeBtnLabel = controlsNewUIPage.getSeniorityToggleActiveBtnLabel();
//			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel.equalsIgnoreCase("Yes"), false);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);
			refreshCachesAfterChangeTemplate();
			Thread.sleep(20000);
			refreshPage();

			//Back to Schedule page
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
			if (isActiveWeekGenerated) {
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
			}

			//Create an open shift, then assign it to the TMs
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.publishOrRepublishSchedule();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			newShiftPage.searchText(firstNameOfTM);
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.emptySearchBox();
			newShiftPage.clickSearchIcon();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			//Create a new shift which using the TM name & role above, check the Seniority Column
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickClearAssignmentsLink();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.emptySearchBox();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up the created shift, assign it to other TM, check the Seniority Column
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			newShiftPage.searchText(firstNameOfTM);
			newShiftPage.clickClearAssignmentsLink();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.emptySearchBox();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on searching dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Seniority on TM recommend dialog when toggle is on")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifySeniorityDisplayWhenToggleTurnOnForRecommendAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
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
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Seniority by enterprise, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isSenioritySectionLoaded();
			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
			configurationPage.publishNowTheTemplate();
//			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
//			Thread.sleep(10000);
//			String activeBtnLabel = controlsNewUIPage.getSeniorityToggleActiveBtnLabel();
//			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel.equalsIgnoreCase("Yes"),false);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(200000);

			//Back to Schedule page
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
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
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
			scheduleMainPage.saveSchedule();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("3pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			scheduleMainPage.saveSchedule();
			scheduleMainPage.publishOrRepublishSchedule();

			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage("Open");
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickOnOfferTMOption();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on recommend dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.clickCloseBtnForOfferShift();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
			scheduleMainPage.saveSchedule();

			//Create a new shift which using the TM name & role above, check the Seniority Column
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on recommend dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);
			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			SimpleUtils.assertOnFail("The Seniority Column is not displayed on recommend dialog!", shiftOperatePage.isSeniorityColumnLoaded(), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Ascending of Seniority on TM searching dialog")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAscendingOfSeniorityForSearchingAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
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
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Yes set sort as Ascending, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(10000);
//			controlsNewUIPage.isSenioritySectionLoaded();
//			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
//			controlsNewUIPage.updateSeniorityToggle("Yes");
			controlsNewUIPage.selectSortOfSeniority("Seniority by enterprise","Ascending");
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(200000);
			refreshCachesAfterChangeTemplate();
			Thread.sleep(100000);
			refreshPage();

			//Create a schedule if there is no any
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Catch up on random shift for further shift creation
			String firstNameOfTM = null;
			String workRole = null;
			if (isActiveWeekGenerated) {
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
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
			scheduleMainPage.saveSchedule();

			//Create a new shift, assign it to the TM
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName("C");
			ArrayList <Integer> seniorityValuesForOpenAssign = shiftOperatePage.getTMSeniorityValues();
			for (int i = 0; i < seniorityValuesForOpenAssign.size() - 1; i++){
				if (seniorityValuesForOpenAssign.get(i) > seniorityValuesForOpenAssign.get(i+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Ascending!", false);
				}
			}
			newShiftPage.clickClearAssignmentsLink();

			newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift("C", true);

			ArrayList <Integer> seniorityValuesForOpenOffer = shiftOperatePage.getTMSeniorityValues();
			for (int j = 0; j < seniorityValuesForOpenOffer.size() - 1; j++){
				if (seniorityValuesForOpenOffer.get(j) > seniorityValuesForOpenOffer.get(j+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Ascending!", false);
				}
			}
			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up a exist shift and assign it to other TMs
			scheduleMainPage.clickOnOpenSearchBoxButton();
			scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			newShiftPage.searchText("C");

			ArrayList <Integer> seniorityValuesForExistAssign = shiftOperatePage.getTMSeniorityValues();
			for (int h = 0; h < seniorityValuesForExistAssign.size() - 1; h++){
				if (seniorityValuesForExistAssign.get(h) > seniorityValuesForExistAssign.get(h+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Ascending!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Ascending of Seniority on TM recommend dialog")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAscendingOfSeniorityForRecommendAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
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
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Seniority by enterprise and set sort as Ascending, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(10000);
//			controlsNewUIPage.isSenioritySectionLoaded();
//			controlsNewUIPage.updateSeniorityToggle("Yes");
//			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
			controlsNewUIPage.selectSortOfSeniority("Seniority by enterprise","Ascending");
			configurationPage.publishNowTheTemplate();
			Thread.sleep(5000);
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(180000);
			refreshCachesAfterChangeTemplate();

			//Create a schedule if there is no any
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
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
			shiftOperatePage.deleteAllShiftsInWeekView();
			scheduleMainPage.saveSchedule();

			//Create a new shift, check the seniority column
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
			ArrayList <Integer> seniorityValuesForOpen = shiftOperatePage.getTMSeniorityValues();
			for (int i = 0; i < seniorityValuesForOpen.size() - 1; i++){
				if (seniorityValuesForOpen.get(i) > seniorityValuesForOpen.get(i+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Ascending!", false);
				}
			}

			//Assign it to one TM, check the seniority column for rest TMs
			MyThreadLocal.setAssignTMStatus(true);
			newShiftPage.selectTeamMembers();
			ArrayList <Integer> seniorityValuesForOpenAssign = shiftOperatePage.getTMSeniorityValues();
			for (int j = 0; j < seniorityValuesForOpenAssign.size() - 1; j++){
				if (seniorityValuesForOpenAssign.get(j) > seniorityValuesForOpenAssign.get(j+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Ascending!", false);
				}
			}
			newShiftPage.clickClearAssignmentsLink();

			//Offer it to one TM, check the seniority column for rest TMs
			MyThreadLocal.setAssignTMStatus(false);
			newShiftPage.selectTeamMembers();
			ArrayList <Integer> seniorityValuesForOpenOffer = shiftOperatePage.getTMSeniorityValues();
			for (int h = 0; h < seniorityValuesForOpenOffer.size() - 1; h++){
				if (seniorityValuesForOpenOffer.get(h) > seniorityValuesForOpenOffer.get(h+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Ascending!", false);
				}
			}

			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up a exist shift and assign it to other TMs
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			newShiftPage.searchText("C");

			ArrayList <Integer> seniorityValuesForExistAssign = shiftOperatePage.getTMSeniorityValues();
			for (int k = 0; k < seniorityValuesForExistAssign.size() - 1; k++){
				if (seniorityValuesForExistAssign.get(k) > seniorityValuesForExistAssign.get(k+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Ascending!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Descending of Seniority on TM searching dialog")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyDescendingOfSeniorityForSearchingAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
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
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Seniority by enterprise and set sort as Descending, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
//			controlsNewUIPage.isSenioritySectionLoaded();
//			controlsNewUIPage.updateSeniorityToggle("Yes");
//			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
			controlsNewUIPage.selectSortOfSeniority("Seniority by enterprise","Descending");
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(100000);
			refreshCachesAfterChangeTemplate();
			Thread.sleep(100000);
			refreshCachesAfterChangeTemplate();
			Thread.sleep(20000);

			//Create a schedule if there is no any
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
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

			//Create a new shift, assign it to the TM
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleCommonPage.clickOnDayView();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName("C");
			newShiftPage.clickClearAssignmentsLink();

			ArrayList <Integer> seniorityValuesForOpenAssign = shiftOperatePage.getTMSeniorityValues();
			for (int i = 0; i < seniorityValuesForOpenAssign.size() - 1; i++){
				if (seniorityValuesForOpenAssign.get(i) < seniorityValuesForOpenAssign.get(i+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Descending!", false);
				}
			}

			newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift("C", true);

			ArrayList <Integer> seniorityValuesForOpenOffer = shiftOperatePage.getTMSeniorityValues();
			for (int j = 0; j < seniorityValuesForOpenOffer.size() - 1; j++){
				if (seniorityValuesForOpenOffer.get(j) < seniorityValuesForOpenOffer.get(j+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Descending!", false);
				}
			}

			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up a exist shift and assign it to other TMs
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			newShiftPage.searchText("C");

			ArrayList <Integer> seniorityValuesForExistAssign = shiftOperatePage.getTMSeniorityValues();
			for (int h = 0; h < seniorityValuesForExistAssign.size() - 1; h++){
				if (seniorityValuesForExistAssign.get(h) < seniorityValuesForExistAssign.get(h+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Descending!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the Descending of Seniority on TM recommend dialog")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyDescendingOfSeniorityForRecommendAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		try {
			//Go to the Scheduling Policy page
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
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
			ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
			configurationPage.goToConfigurationPage();
			configurationPage.clickOnConfigurationCrad("Scheduling Policies");
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");

			//Edit the seniority toggle as Yes set sort as Descending, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
//			controlsNewUIPage.isSenioritySectionLoaded();
//			controlsNewUIPage.updateSeniorityToggle("Yes");
//			controlsNewUIPage.selectSeniorityType("Seniority by enterprise");
			controlsNewUIPage.selectSortOfSeniority("Seniority by enterprise","Descending");
			configurationPage.publishNowTheTemplate();
			switchToConsoleWindow();
			refreshCachesAfterChangeTemplate();
			Thread.sleep(200000);
			refreshCachesAfterChangeTemplate();

			//Create a schedule if there is no any
			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
			ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
			scheduleCommonPage.clickOnWeekView();
			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
			if(isActiveWeekGenerated){
				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
			}
			Thread.sleep(5000);
			createSchedulePage.createScheduleForNonDGFlowNewUI();

			//Catch up on random shift for further shift creation
			String firstNameOfTM = null;
			String workRole = null;
			if (isActiveWeekGenerated) {
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
			}
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			shiftOperatePage.deleteAllShiftsInWeekView();
			scheduleMainPage.saveSchedule();

			//Create a new shift, switch to the Recommend tab, check the seniority column
			NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			newShiftPage.clickOnDayViewAddNewShiftButton();
			newShiftPage.customizeNewShiftPage();
			newShiftPage.selectWorkRole(workRole);
			newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
			newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
			newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
			newShiftPage.clickOnCreateOrNextBtn();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();

			ArrayList <Integer> seniorityValuesForOpen = shiftOperatePage.getTMSeniorityValues();
			for (int i = 0; i < seniorityValuesForOpen.size() - 1; i++){
				if (seniorityValuesForOpen.get(i) < seniorityValuesForOpen.get(i+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Descending!", false);
				}
			}

			//Assign it to one TM, check the seniority column for rest TMs
			MyThreadLocal.setAssignTMStatus(true);
			newShiftPage.selectTeamMembers();

			ArrayList <Integer> seniorityValuesForOpenOffer = shiftOperatePage.getTMSeniorityValues();
			for (int j = 0; j < seniorityValuesForOpenOffer.size() - 1; j++){
				if (seniorityValuesForOpenOffer.get(j) < seniorityValuesForOpenOffer.get(j+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Descending!", false);
				}
			}

			newShiftPage.clickClearAssignmentsLink();

			//Offer it to one TM, check the seniority column for rest TMs
			MyThreadLocal.setAssignTMStatus(false);
			newShiftPage.selectTeamMembers();

			ArrayList <Integer> seniorityValuesForOpenAssign = shiftOperatePage.getTMSeniorityValues();
			for (int h = 0; h < seniorityValuesForOpenAssign.size() - 1; h++){
				if (seniorityValuesForOpenAssign.get(h) < seniorityValuesForOpenAssign.get(h+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Descending!", false);
				}
			}

			newShiftPage.clickOnBackButton();
			newShiftPage.clickOnCreateOrNextBtn();
			newShiftPage.searchTeamMemberByName(firstNameOfTM);
			newShiftPage.clickOnOfferOrAssignBtn();
			scheduleMainPage.saveSchedule();

			//Pick up a exist shift and assign it to other TMs
			scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
			scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
			shiftOperatePage.clickonAssignTM();
			shiftOperatePage.switchSearchTMAndRecommendedTMsTab();

			ArrayList <Integer> seniorityValuesForExistAssign = shiftOperatePage.getTMSeniorityValues();
			for (int k = 0; k < seniorityValuesForExistAssign.size() - 1; k++){
				if (seniorityValuesForExistAssign.get(k) < seniorityValuesForExistAssign.get(k+1)){
					SimpleUtils.fail("The Seniority column is not sorted as Descending!", false);
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

}