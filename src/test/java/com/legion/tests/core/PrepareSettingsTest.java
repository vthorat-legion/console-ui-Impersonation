package com.legion.tests.core;

import com.legion.api.cache.RemoveTemplateSnapShotForLocationsAPI;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.pages.core.opConfiguration.MealAndRestPage;
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
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getEnterprise;

public class PrepareSettingsTest extends TestBase {

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
        try {
//            ToggleAPI.disableToggle(Toggles.DynamicGroupV2.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1));
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Prepare the control settings First")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void prepareSettingsInControlsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
//            ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), getUserNameNPwdForCallingAPI().get(0),
//                    getUserNameNPwdForCallingAPI().get(1), false);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

//            // Go to Team page, reject all the time off request
//            TeamPage teamPage = pageFactory.createConsoleTeamPage();
//            teamPage.goToTeam();
//            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            teamPage.rejectAllTeamMembersTimeOffRequest(profileNewUIPage, 0);

            dashboardPage.clickOnIntegrationConsoleMenu();
            dashboardPage.verifyIntegrationPageIsLoaded();
            IntegrationPage integrationPage = pageFactory.createIntegrationPage();
            if(!integrationPage.checkIsConfigExists("custom", "HR")){
                Map<String, String> configInfo = new HashMap<>();
                configInfo.put("channel", "CUSTOM");
                configInfo.put("applicationType", "HR");
                configInfo.put("status", "ENABLED");
                configInfo.put("timeZoneOption", "UTC");

                integrationPage.createConfig(configInfo);
            }

            // Set time off policy
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
            controlsPage.clickGlobalSettings();
            //Set 'Automatically convert unassigned shifts to open shifts when generating the schedule?' set as Yes, all unassigned shifts
            controlsNewUIPage.clickOnScheduleCollaborationOpenShiftAdvanceBtn();
            controlsNewUIPage.updateConvertUnassignedShiftsToOpenSettingOption("Yes, all unassigned shifts");

            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsPage.clickGlobalSettings();
            controlsNewUIPage.updateCanWorkerRequestTimeOff("Yes");
            controlsNewUIPage.clickOnSchedulingPoliciesTimeOffAdvanceBtn();
            controlsNewUIPage.updateShowTimeOffReasons("Yes");

            controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsYes();

            controlsNewUIPage.clickOnSchedulingPoliciesSchedulesAdvanceBtn();
            List<WebElement> CentralizedScheduleReleaseSelector = controlsNewUIPage.getAvailableSelector();
            WebElement noItem = CentralizedScheduleReleaseSelector.get(1);
            controlsNewUIPage.updateCentralizedScheduleRelease(noItem);

            controlsPage.gotoControlsPage();
            controlsPage.clickGlobalSettings();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
            controlsNewUIPage.updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule("Yes, anytime");

            controlsPage.gotoControlsPage();
            controlsPage.clickGlobalSettings();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.enableOrDisableScheduleCopyRestriction("no");

            //Set buffer hours: before--2, after--3
            controlsPage.gotoControlsPage();
            controlsPage.clickGlobalSettings();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.clickOnSchedulingPoliciesSchedulesAdvanceBtn();
            HashMap<String, String> schedulingPoliciesData = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SchedulingPoliciesData.json");
            String beforeBufferCount = schedulingPoliciesData.get("Additional_Schedule_Hours_Before");
            String afterBufferCount = schedulingPoliciesData.get("Additional_Schedule_Hours_After");
            controlsNewUIPage.updateScheduleBufferHoursBefore(beforeBufferCount);
            controlsNewUIPage.updateScheduleBufferHoursAfter(afterBufferCount);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

//            MyThreadLocal.setIsNeedEditingOperatingHours(true);
//            createScheduleForThreeWeeks(schedulePage);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Prepare the op template settings First")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void prepareSettingsInOPTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
//            ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), getUserNameNPwdForCallingAPI().get(0),
//                    getUserNameNPwdForCallingAPI().get(1), false);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            dashboardPage.clickOnIntegrationConsoleMenu();
            dashboardPage.verifyIntegrationPageIsLoaded();
            IntegrationPage integrationPage = pageFactory.createIntegrationPage();
            if(!integrationPage.checkIsConfigExists("custom", "HR")){
                Map<String, String> configInfo = new HashMap<>();
                configInfo.put("channel", "CUSTOM");
                configInfo.put("applicationType", "HR");
                configInfo.put("status", "ENABLED");
                configInfo.put("timeZoneOption", "UTC");

                integrationPage.createConfig(configInfo);
            }

            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();

            //Go to OP page
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

            configurationPage.goToConfigurationPage();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Scheduling Policies"));
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.enableOrDisableRequiredEmployeeAcknowledgeSetting("Yes");
            configurationPage.publishNowTheTemplate();

            String option = "Yes, all unassigned shifts";
            configurationPage.goToConfigurationPage();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Schedule Collaboration"));
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.setWFS("No");
            configurationPage.updateConvertUnassignedShiftsToOpenWhenCreatingScheduleSettingOption(option);
            configurationPage.updateConvertUnassignedShiftsToOpenWhenCopyingScheduleSettingOption(option);
            configurationPage.updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule("Yes, anytime");
//            configurationPage.publishNowTheTemplate();

            String wfsName = "Lone Star Region";
//            cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Schedule Collaboration"));
//            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

//            configurationPage.selectWFSGroup(wfsName);
//            configurationPage.updateConvertUnassignedShiftsToOpenWhenCreatingScheduleSettingOption(option);
//            configurationPage.updateConvertUnassignedShiftsToOpenWhenCopyingScheduleSettingOption(option);
            configurationPage.publishNowTheTemplate();

            //Set buffer hours on OP: before--2, after--3
            configurationPage.goToConfigurationPage();
            controlsNewUIPage.clickOnControlsOperatingHoursSection();
            cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Operating Hours"));
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectOperatingBufferHours("BufferHour");
            configurationPage.setOpeningAndClosingBufferHours(2, 3);
            configurationPage.publishNowTheTemplate();

            //setStrictlyEnforceMinorViolationSetting
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Compliance.getValue());
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            // Click the Yes buttons of setting "Strictly enforce minor violations?"
            configurationPage.setStrictlyEnforceMinorViolations("No");
            SimpleUtils.assertOnFail("The 'Strictly enforce minor violations?' should be setting as No! ",
                        !configurationPage.isStrictlyEnforceMinorViolationSettingEnabled(), false);
            //Publish the template
            configurationPage.publishNowTheTemplate();

            RemoveTemplateSnapShotForLocationsAPI.removeTemplateSnapShotForLocationsAPI(username, password);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void createScheduleForThreeWeeks(SchedulePage schedulePage) throws Exception {
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleCommonPage.navigateToNextWeek();
        isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleCommonPage.navigateToNextWeek();
        isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Prepare the dynamic employee groups and meal rest templates")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCanPrepareDynamicEmployeeGroupsNTemplatesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
//            ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), getUserNameNPwdForCallingAPI().get(0),
//                    getUserNameNPwdForCallingAPI().get(1), false);
//            ToggleAPI.updateToggle(Toggles.MealAndRestTemplate.getValue(), getUserNameNPwdForCallingAPI().get(0),
//                    getUserNameNPwdForCallingAPI().get(1), true);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToUserManagementPage();
            configurationPage.goToDynamicEmployeeGroupPage();
            // Delete all dynamic employee group
            configurationPage.deleteSpecifyDynamicEmployeeGroupsInList("MealRest-ForAuto");
            // Create the dynamic employee group for different work roles
            List<String> groupCriteriaList = new ArrayList<>();
            groupCriteriaList.clear();
            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.WorkRole.getValue()+ "-"
                    +OpsPortalConfigurationPage.DynamicEmployeeGroupWorkRoleCriteria.EventManager.getValue());
            String eventManagerGroupTitle = "EventManager-MealRest-ForAuto";
            String eventManagerGroupDescription = "EventManager-ForAuto";
            configurationPage.createNewDynamicEmployeeGroup(eventManagerGroupTitle, eventManagerGroupDescription,
                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MealAndRest.getValue(), groupCriteriaList);
            String generalManagerGroupTitle = "GeneralManager-MealRest-ForAuto";
            String generalManagerGroupDescription = "GeneralManager-ForAuto";
            groupCriteriaList.clear();
            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.WorkRole.getValue()+ "-"
                    +OpsPortalConfigurationPage.DynamicEmployeeGroupWorkRoleCriteria.GeneralManager.getValue());
            configurationPage.createNewDynamicEmployeeGroup(generalManagerGroupTitle, generalManagerGroupDescription,
                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MealAndRest.getValue(), groupCriteriaList);
            String teamMemberGroupTitle = "TeamMember-MealRest-ForAuto";
            String teamMemberGroupDescription = "TeamMember-ForAuto";
            groupCriteriaList.clear();
            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.WorkRole.getValue()+ "&"
                    +OpsPortalConfigurationPage.DynamicEmployeeGroupWorkRoleCriteria.TeamMember.getValue());
            configurationPage.createNewDynamicEmployeeGroup(teamMemberGroupTitle, teamMemberGroupDescription,
                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MealAndRest.getValue(), groupCriteriaList);

            // Now Go to Meal And Rest Template to set the settings for different work roles
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());
            String eventManagerTemplate = "EventManager-ForAuto";
            String generalManagerTemplate = "GeneralManager-ForAuto";
            String teamMemberTemplate = "TeamMember-ForAuto";
            configurationPage.archiveOrDeleteTemplate(eventManagerTemplate);
            configurationPage.archiveOrDeleteTemplate(generalManagerTemplate);
            configurationPage.archiveOrDeleteTemplate(teamMemberTemplate);
            List<String> mealSettings = new ArrayList<>(Arrays.asList("1", "240", "721", "", "", "", "", "20", "", "", "", "120", "120"));
            List<String> restSettings = new ArrayList<>(Arrays.asList("180", "721", "1"));
            String restDuration = "15";
            createMealAndRestTemplates(eventManagerTemplate, mealSettings, restSettings, eventManagerGroupTitle, restDuration);
            mealSettings = new ArrayList<>(Arrays.asList("1", "240", "721", "", "", "", "", "25", "", "", "", "120", "120"));
            restSettings = new ArrayList<>(Arrays.asList("180", "721", "1"));
            restDuration = "20";
            createMealAndRestTemplates(generalManagerTemplate, mealSettings, restSettings, generalManagerGroupTitle, restDuration);
            mealSettings = new ArrayList<>(Arrays.asList("1", "240", "721", "", "", "", "", "30", "", "", "", "120", "120"));
            restSettings = new ArrayList<>(Arrays.asList("180", "721", "1"));
            restDuration = "25";
            createMealAndRestTemplates(teamMemberTemplate, mealSettings, restSettings, teamMemberGroupTitle, restDuration);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void createMealAndRestTemplates(String templateName, List<String> mealSettings, List<String> restSettings,
                                            String dynamicGroup, String restDuration) throws Exception {
        String meal = "Meal";
        String rest = "Rest";
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        MealAndRestPage mealAndRestPage = (MealAndRestPage) pageFactory.createMealAndRestPage();
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        mealAndRestPage.clickOnAddButtonOnMealOrRestSection(meal);
        mealAndRestPage.verifyCanSetTheValueForInputs(meal, mealSettings);
        mealAndRestPage.clickOnAddButtonOnMealOrRestSection(rest);
        mealAndRestPage.verifyCanSetTheValueForInputs(rest, restSettings);
        mealAndRestPage.setRestDuration(restDuration);
        configurationPage.selectOneDynamicGroup(dynamicGroup);
        configurationPage.clickOnTemplateDetailTab();
        cinemarkMinorPage.saveOrPublishTemplate(CinemarkMinorTest.templateAction.Publish_Now.getValue());
    }
}
