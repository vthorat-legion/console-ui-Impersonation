package com.legion.tests.core;

import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.opemployeemanagement.AbsentManagePage;
import com.legion.pages.core.opemployeemanagement.EmployeeManagementPanelPage;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ScheduleAccrualTimeTest extends TestBase {

	private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");

	@Override
	@BeforeMethod()
	public void firstTest(Method testMethod, Object[] params) {
		try {
			this.createDriver((String) params[0], "69", "Window");
			visitPage(testMethod);
			//ToggleAPI.updateToggle(Toggles.UseAbsenceMgmtConfiguration.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), true);
			//ToggleAPI.updateToggle(Toggles.UseLegionAccrual.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), true);
			//ToggleAPI.updateToggle(Toggles.EnableScheduleOnAccrualSCH.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), true);
			loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the configurable of override rule")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAccrualLimitOverrideConfigsAsInternalAdmin(String username, String password, String browser, String location)
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

			//Edit the accrual time override toggle as Yes, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAccrualTimeOverrideLoaded();
			controlsNewUIPage.updateAccrualTimeOverrideToggle("Yes");
			configurationPage.publishNowTheTemplate();
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			Thread.sleep(10000);
			String activeBtnLabel = controlsNewUIPage.getAccrualTimeOverrideToggleActiveBtnLabel();
			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel.equalsIgnoreCase("Yes"),false);

			//Edit the accrual time override toggle as No, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(3000);
			controlsNewUIPage.isAccrualTimeOverrideLoaded();
			controlsNewUIPage.updateAccrualTimeOverrideToggle("No");
			configurationPage.publishNowTheTemplate();
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			Thread.sleep(10000);
			activeBtnLabel = controlsNewUIPage.getAccrualTimeOverrideToggleActiveBtnLabel();
			SimpleUtils.assertOnFail("The selected button is not expected!", activeBtnLabel.equalsIgnoreCase("No"),false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the configurable of time off reason")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyAccrualLimitTimeOffReasonConfigsAsInternalAdmin(String username, String password, String browser, String location)
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

			//Time off reason, delete button & add button won't be shown on read-only mode
			Thread.sleep(10000);
			SimpleUtils.assertOnFail("Schedule Restriction is not loaded!",controlsNewUIPage.isScheduleRestrictionLoaded(),false);
			SimpleUtils.assertOnFail("Schedule Restriction section is not on read-only mode!",!(controlsNewUIPage.isAddTimeOffBtnLoaded())
					&& !(controlsNewUIPage.isDeleteTimeOffBtnLoaded()),false);

			//Add one time off reason under Schedule Restriction, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(15000);
			SimpleUtils.assertOnFail("Schedule Restriction is not loaded!",controlsNewUIPage.isScheduleRestrictionLoaded(),false);
			if(controlsNewUIPage.isDeleteTimeOffBtnLoaded()){
				controlsNewUIPage.clickDeleteTimeOffBtn();
			}
			controlsNewUIPage.addTimeOffReasonOnSchedulingPolicy("Holiday","40");
			SimpleUtils.assertOnFail("System support multiple time off reasons!",!(controlsNewUIPage.isAddTimeOffBtnClickable())
					&&controlsNewUIPage.isDeleteTimeOffBtnLoaded(),false);
			configurationPage.publishNowTheTemplate();

			//Edit exists time off reason under Schedule Restriction, save the change
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			Thread.sleep(5000);
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(30000);
			SimpleUtils.assertOnFail("No existing Time off reason!",!(controlsNewUIPage.isAddTimeOffBtnClickable())
					&&controlsNewUIPage.isDeleteTimeOffBtnLoaded(),false);
			controlsNewUIPage.modifyTimeOffReasonOnSchedulingPolicy("Vacation","35");
			configurationPage.publishNowTheTemplate();

			//Delete exists time off reason under Schedule Restriction, save the change
			configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
			Thread.sleep(5000);
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(15000);
			controlsNewUIPage.clickDeleteTimeOffBtn();
			SimpleUtils.assertOnFail("Time off reason still displays!",controlsNewUIPage.isAddTimeOffBtnClickable()
					&&!(controlsNewUIPage.isDeleteTimeOffBtnLoaded()),false);
			configurationPage.publishNowTheTemplate();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Cosimo")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Validate the sync of time off reason between scheduling policy and OP")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTimeOffReasonListSyncAsInternalAdmin(String username, String password, String browser, String location)
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
			Thread.sleep(10000);

			//Add one time off reason under Schedule Restriction, save the change
			configurationPage.clickOnEditButtonOnTemplateDetailsPage();
			Thread.sleep(30000);
			SimpleUtils.assertOnFail("Schedule Restriction is not loaded!",controlsNewUIPage.isScheduleRestrictionLoaded(),false);
			if(controlsNewUIPage.isAddTimeOffBtnClickable()){
				controlsNewUIPage.clickAddTimeOffBtn();
			}

			List<String> timeOffReasonOnSchedule = controlsNewUIPage.getTimeOffReasonsOnSchedulingPolicy();

			//Go to OP time off reason settings page, compare the time pff reason list
			OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
			navigationPage.navigateToEmployeeManagement();
			EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
			panelPage.goToTimeOffManagementPage();

			AbsentManagePage absentManagePage = new AbsentManagePage();
			absentManagePage.switchToSettings();
			List<String> timeOffReasonOnOP = absentManagePage.getAllTheTimeOffReasons();
			for (int i = 0; i < timeOffReasonOnSchedule.size(); i++){
				for(int j = 0; j < timeOffReasonOnOP.size(); j++){
					if(timeOffReasonOnSchedule.get(i).trim().equals(timeOffReasonOnOP.get(j).trim())){
						SimpleUtils.report("Time off reason matches between Schedule & OP!");
						break;
					}
					if(j == timeOffReasonOnOP.size()-1){
						SimpleUtils.fail("Time off reason between Schedule & OP is not same!", false);
					}
				}
			}

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

}