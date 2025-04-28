package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.JobsPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteScheduleTest extends TestBase {

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify internal admin can delete a published schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDeletePublishedScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
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
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            } else {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            String deleteForWeekText = scheduleMainPage.getDeleteScheduleForWhichWeekText();
            String unPublishedMessage = "This action can’t be undone.";
            // Verify the visibility of Delete button
            SimpleUtils.assertOnFail("Schedule page: Delete button is not visible!", scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
            // Verify the functionality of Delete button
            scheduleMainPage.verifyClickOnDeleteScheduleButton();
            // Verify the content on Delete Schedule confirm window
            scheduleMainPage.verifyTheContentOnDeleteScheduleDialog(unPublishedMessage, deleteForWeekText);
            // Verify the Delete button is disabled by default
            scheduleMainPage.verifyDeleteBtnDisabledOnDeleteScheduleDialog();
            // Verify the Delete button is enabled when clicking the check box
            scheduleMainPage.verifyDeleteButtonEnabledWhenClickingCheckbox();
            // Verify the functionality of Cancel button
            scheduleMainPage.verifyClickOnCancelBtnOnDeleteScheduleDialog();

            // Delete the Unassigned shifts to unblock publishing
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();

            createSchedulePage.publishActiveSchedule();
            String publishedDeleteMessage = "This action can’t be undone. The schedule has been published, it will be withdrawn from team members";
            scheduleMainPage.verifyClickOnDeleteScheduleButton();
            // Verify the content of Delete Schedule window when schedule is published
            scheduleMainPage.verifyTheContentOnDeleteScheduleDialog(publishedDeleteMessage, deleteForWeekText);
            // Verify the Delete button is disabled by default when schedule is published
            scheduleMainPage.verifyDeleteBtnDisabledOnDeleteScheduleDialog();
            // Verify the Delete button is enabled when clicking the check box when schedule is published
            scheduleMainPage.verifyDeleteButtonEnabledWhenClickingCheckbox();
            // Verify the functionality of Cancel button when schedule is published
            scheduleMainPage.verifyClickOnCancelBtnOnDeleteScheduleDialog();
            // Verify the functionality of Delete button when schedule is published
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Store Manger can delete an unpublished schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDeleteUnPublishedScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            } else {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Login as SM
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());

            String deleteForWeekText = scheduleMainPage.getDeleteScheduleForWhichWeekText();
            String unPublishedMessage = "This action can’t be undone.";

            // Verify the visibility of Delete button
            SimpleUtils.assertOnFail("Schedule page: Delete button is not visible!", scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
            // Verify the functionality of Delete button
            scheduleMainPage.verifyClickOnDeleteScheduleButton();
            // Verify the content on Delete Schedule confirm window
            scheduleMainPage.verifyTheContentOnDeleteScheduleDialog(unPublishedMessage, deleteForWeekText);
            // Verify the Delete button is disabled by default
            scheduleMainPage.verifyDeleteBtnDisabledOnDeleteScheduleDialog();
            // Verify the Delete button is enabled when clicking the check box
            scheduleMainPage.verifyDeleteButtonEnabledWhenClickingCheckbox();
            // Verify the functionality of Cancel button
            scheduleMainPage.verifyClickOnCancelBtnOnDeleteScheduleDialog();
            // Verify the functionality of Delete button when schedule is unpublished
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Store Manger cannot see the Delete button if schedule is published")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySMCannotSeeDeleteButtonIfScheduleIsPublishedAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
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
            boolean isSchedulePublished = createSchedulePage.isCurrentScheduleWeekPublished();
            if (!isSchedulePublished) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
                createSchedulePage.publishActiveSchedule();
            }

            // Verify Store Manger cannot see the Delete button if schedule is published
            SimpleUtils.assertOnFail("Schedule page: Delete button should not show when the schedule is published!", !scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Store Manager cannot see the Delete button when schedule is not pulished")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class, enabled = false)
    public void verifySMCannotSeeDeleteButtonIfScheduleIsNotPublishedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Verify SM doesn't have "Schedule: Manage Schedule" permission
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            controlsNewUIPage.verifyUsersAreLoaded();
            controlsPage.clickGlobalSettings();
            String accessRoleTab = "Access Roles";
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
            String permissionSection = "Schedule";
            String permission = "Manage Schedule";
            String actionOff = "off";
            String actionOn = "on";
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission, actionOff);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isScheduleCreated = createSchedulePage.isWeekGenerated();
            if (isScheduleCreated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Login as SM
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Verify Store Manager cannot see the Delete button when schedule is not published
            SimpleUtils.assertOnFail("Schedule page: Delete button should not show when the schedule is not published!", !scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
            loginPage.logOut();

            // Login as Internal admin, add the permission back
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            controlsPage.gotoControlsPage();
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            controlsNewUIPage.verifyUsersAreLoaded();
            controlsPage.clickGlobalSettings();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission, actionOn);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Store Manger can publish schedule if schedule is not published")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class, enabled = false)
    public void verifySMCanPublishScheduleIfScheduleIsNotPublishedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Verify SM doesn't have "Schedule: Manage Schedule" permission
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            controlsNewUIPage.verifyUsersAreLoaded();
            controlsPage.clickGlobalSettings();
            String accessRoleTab = "Access Roles";
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
            String permissionSection = "Schedule";
            String permission1 = "Manage Schedule";
            String permission2 = "Publish Schedule";
            String actionOff = "off";
            String actionOn = "on";
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission1, actionOff);
            // Verify SM have "Schedule: Publish Schedule" permission
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission2, actionOn);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isScheduleCreated = createSchedulePage.isWeekGenerated();
            if (!isScheduleCreated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            boolean isSchedulePublished = createSchedulePage.isCurrentScheduleWeekPublished();
            if (isSchedulePublished) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Login as SM
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Verify Store Manger can publish schedule if schedule is not published
            createSchedulePage.publishActiveSchedule();
            loginPage.logOut();

            // Login as Internal admin, add the permission back
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            controlsPage.gotoControlsPage();
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            controlsNewUIPage.verifyUsersAreLoaded();
            controlsPage.clickGlobalSettings();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission1, actionOn);
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission2, actionOn);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify SM delete schedule should keep the system schedule when Centralized Schedule Release is Yes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySMDeleteScheduleShouldKeepSystemScheduleWhenCentralizedScheduleReleaseIsYesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            int index = 1;
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            // Enable Centralized Schedule Release
            controlsPage.gotoControlsPage();
            if (isLocationUsingControlsConfiguration) {
                controlsNewUIPage.clickOnControlsConsoleMenu();
                controlsNewUIPage.clickOnControlsSchedulingPolicies();
                controlsNewUIPage.clickOnGlobalLocationButton();
                Thread.sleep(10000);
                controlsNewUIPage.clickOnSchedulingPoliciesSchedulesAdvanceBtn();
                List<WebElement> CentralizedScheduleReleaseSelector = controlsNewUIPage.getAvailableSelector();
                WebElement yesItem = CentralizedScheduleReleaseSelector.get(0);
                WebElement noItem = CentralizedScheduleReleaseSelector.get(1);
                controlsNewUIPage.updateCentralizedScheduleRelease(yesItem);
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            } else {
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToGlobalConfigurationInLocations();
                scheduleMainPage.clickOnEditButton();
                Thread.sleep(10000);
                List<WebElement> CentralizedScheduleReleaseSelector = controlsNewUIPage.getAvailableSelector();
                WebElement yesItem = CentralizedScheduleReleaseSelector.get(0);
                WebElement noItem = CentralizedScheduleReleaseSelector.get(1);
                controlsNewUIPage.updateCentralizedScheduleRelease(yesItem);
                controlsNewUIPage.clickOnSaveBtn();
//                switchToConsoleWindow();
            }

            //Create Release Schedule job
            jobsPage.iCanEnterJobsTab();
            String jobTitle = "Release Schedule For Auto";
            String jobType = "Release Schedule";
            String releaseDay = "100";
            jobsPage.archiveOrStopSpecificJob(jobTitle);
            jobsPage.iCanEnterCreateNewJobPage();
            jobsPage.selectJobType(jobType);
            jobsPage.selectWeeksForJobToTakePlaceByIndex(index);
            jobsPage.clickOkBtnInCreateNewJobPage();
            jobsPage.inputJobTitle(jobTitle);
            jobsPage.inputJobComments(jobTitle);
            jobsPage.addLocationBtnIsClickable();
            jobsPage.iCanSelectLocationsByAddLocation(location,0);
            jobsPage.iCanClickOnCreatAndReleaseCheckBox();
            jobsPage.iCanSetUpDaysBeforeRelease(releaseDay);
            jobsPage.createBtnIsClickable();

            ArrayList<HashMap<String,String>> jobInfo = jobsPage.iCanGetJobInfo(jobTitle);
            String jobStatus = jobInfo.get(0).get("status");
            int i = 0;
            while(i<5 && !jobStatus.equalsIgnoreCase("Completed")) {
                Thread.sleep(60000);
                jobsPage.iCanEnterJobsTab();
                jobInfo = jobsPage.iCanGetJobInfo(jobTitle);
                jobStatus = jobInfo.get(0).get("status");
                i++;
            }
            SimpleUtils.assertOnFail("The job status should be Completed, but actual is:"+jobStatus,
                    jobStatus.equalsIgnoreCase("Completed"), false);

            switchToConsoleWindow();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
//            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            //Check the week that you have released, Observe the status of this week
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            String scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            //The status should be Not Started
            String expectedStatus = "Not Started";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus), false);
            //Click on the week that the status is Not Started
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            // Should go to schedule create page, stay on the Manager tab, Create Schedule button is loaded
            SimpleUtils.assertOnFail("The manager schedule view is not display！",
                    scheduleMainPage.isManagerViewSelected(), false);
            boolean isScheduleCreated = createSchedulePage.isWeekGenerated();
            SimpleUtils.assertOnFail("The Create schedule button fail to load！",
                    !isScheduleCreated, false);

            //Click on the Suggested tab, observe the shifts
            scheduleMainPage.clickOnSuggestedButton();
            //Suggested schedule is created
            SimpleUtils.assertOnFail("The schedule table is display correctly! ",
                    scheduleShiftTablePage.isScheduleTableDisplay(), false);
            // Click on Manager tab again, follow the Steps to created the schedule
            scheduleMainPage.clickOnManagerButton();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            //Go to Overview tab, check the status of the schedule

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //The status should be Draft
            scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            expectedStatus = "Draft";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus), false);
            //Click on Delete button
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            // Observe the content on this pop up window
            // Following content should be loaded:
            //- Delete Schedule
            //- This action can’t be undone.
            //- Delete Schedule For <Feb 28 - Mar 06, 2021>
            //- Check box
            //- Cancel button
            //- Delete button
            String deleteForWeekText = scheduleMainPage.getDeleteScheduleForWhichWeekText();
            String unPublishedMessage = "This action can’t be undone.";
            // Verify the visibility of Delete button
            SimpleUtils.assertOnFail("Schedule page: Delete button is not visible!", scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
            // Verify the functionality of Delete button
            scheduleMainPage.verifyClickOnDeleteScheduleButton();
            // Verify the content on Delete Schedule confirm window
            scheduleMainPage.verifyTheContentOnDeleteScheduleDialog(unPublishedMessage, deleteForWeekText);
            // Verify the Delete button is disabled by default
            scheduleMainPage.verifyDeleteBtnDisabledOnDeleteScheduleDialog();
            // Verify the functionality of Delete button
            createSchedulePage.confirmDeleteSchedule();

            //Observe the schedule, Schedule should be deleted successfully
            isScheduleCreated = createSchedulePage.isWeekGenerated();
            SimpleUtils.assertOnFail("The Create schedule button fail to load！",
                    !isScheduleCreated, false);
            //Stay on the Manager tab, Create Schedule button is shown
            SimpleUtils.assertOnFail("The manager schedule view is not display！",
                    scheduleMainPage.isManagerViewSelected(), false);
            // Click on Suggested tab, observe the suggested schedule, Suggested schedule should be kept
            scheduleMainPage.clickOnSuggestedButton();
            SimpleUtils.assertOnFail("The schedule table is display correctly! ",
                    scheduleShiftTablePage.isScheduleTableDisplay(), false);
            //Go to Overview tab, check the status of the schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //The status should be Not Started
            scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            expectedStatus = "Not Started";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus), false);
            //Click on this week again, verify that this week can be accessed
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            //Create the schedule again, publish the schedule
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            //Observe the delete button, Delete button should not show
            SimpleUtils.assertOnFail("The Delete button should not loaded! ",
                    !scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
            //Go to Overview page again, check the status of the schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //The status should be Published or Finalized
            scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            expectedStatus = "Published";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus)
                            ||scheduleStatusOnOverViewTable.equalsIgnoreCase("Finalized"), false);
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Internal Admin delete schedule should keep the system schedule when Centralized Schedule Release is Yes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAdminDeleteScheduleShouldKeepSystemScheduleWhenCentralizedScheduleReleaseIsYesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            int index = 1;
            //Check the week that you have released, Observe the status of this week
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isScheduleCreated = createSchedulePage.isWeekGenerated();
            if (isScheduleCreated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            refreshPage();
            //Select one week which status is Guidance, Click on this week
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            //Create the suggested schedule for this week
            createSchedulePage.createSuggestedSchedule();
            //Go to Overview page, check the status of this week
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            Thread.sleep(5000);
            String scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            //The status should be Not Started
            String expectedStatus = "Not Started";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus), false);
            //Click on the week that the status is Not Started
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            // Should go to schedule create page, stay on the Manager tab, Create Schedule button is loaded
            SimpleUtils.assertOnFail("The manager schedule view is not display！",
                    scheduleMainPage.isManagerViewSelected(), false);
            Thread.sleep(5000);
            isScheduleCreated = createSchedulePage.isWeekGenerated();
            SimpleUtils.assertOnFail("The Create schedule button fail to load！",
                    !isScheduleCreated, false);

            //Click on the Suggested tab, observe the shifts
            scheduleMainPage.clickOnSuggestedButton();
            //Suggested schedule is created
            SimpleUtils.assertOnFail("The schedule table is display correctly! ",
                    scheduleShiftTablePage.isScheduleTableDisplay(), false);
            // Click on Manager tab again, follow the Steps to created the schedule
            scheduleMainPage.clickOnManagerButton();
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            //Go to Overview tab, check the status of the schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //The status should be Draft
            scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            expectedStatus = "Draft";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus), false);
            //Click on Delete button
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            // Observe the content on this pop up window
            // Following content should be loaded:
            //- Delete Schedule
            //- This action can’t be undone.
            //- Delete Schedule For <Feb 28 - Mar 06, 2021>
            //- Check box
            //- Cancel button
            //- Delete button
            String deleteForWeekText = scheduleMainPage.getDeleteScheduleForWhichWeekText();
            String unPublishedMessage = "This action can’t be undone.";
            // Verify the visibility of Delete button
            SimpleUtils.assertOnFail("Schedule page: Delete button is not visible!", scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
            // Verify the functionality of Delete button
            scheduleMainPage.verifyClickOnDeleteScheduleButton();
            // Verify the content on Delete Schedule confirm window
            scheduleMainPage.verifyTheContentOnDeleteScheduleDialog(unPublishedMessage, deleteForWeekText);
            // Verify the functionality of Delete button
            createSchedulePage.confirmDeleteSchedule();

            //Observe the schedule, Schedule should be deleted successfully
            isScheduleCreated = createSchedulePage.isWeekGenerated();
            SimpleUtils.assertOnFail("The Create schedule button fail to load！",
                    !isScheduleCreated, false);
            //Stay on the Manager tab, Create Schedule button is shown
            SimpleUtils.assertOnFail("The manager schedule view is not display！",
                    scheduleMainPage.isManagerViewSelected(), false);
            // Click on Suggested tab, observe the suggested schedule, Suggested schedule should be kept
            scheduleMainPage.clickOnSuggestedButton();
            scheduleMainPage.clickOnSuggestedButton();
            SimpleUtils.assertOnFail("The schedule table is display correctly! ",
                    scheduleShiftTablePage.isScheduleTableDisplay(), false);
            //Go to Overview tab, check the status of the schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            refreshPage();
            Thread.sleep(5000);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //The status should be Not Started
            scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            expectedStatus = "Not Started";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus), false);
            //Click on this week again, verify that this week can be accessed
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            //Create the schedule again, publish the schedule
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            Thread.sleep(5000);
            //Go to Overview page again, check the status of the schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //The status should be Published or Finalized
            scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            expectedStatus = "Published";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus)
                            ||scheduleStatusOnOverViewTable.equalsIgnoreCase("Finalized"), false);

            //Click on Delete button
            scheduleOverviewPage.clickOnGuidanceBtnOnOverview(index);
            // Observe the content on this pop up window
            // Following content should be loaded:
            //- Delete Schedule
            //- This action can’t be undone.
            //- Delete Schedule For <Feb 28 - Mar 06, 2021>
            //- Check box
            //- Cancel button
            //- Delete button
            deleteForWeekText = scheduleMainPage.getDeleteScheduleForWhichWeekText();
            unPublishedMessage = "This action can’t be undone. The schedule has been published, it will be withdrawn from team members";
            // Verify the visibility of Delete button
            SimpleUtils.assertOnFail("Schedule page: Delete button is not visible!", scheduleMainPage.isDeleteScheduleButtonLoaded(), false);
            // Verify the functionality of Delete button
            scheduleMainPage.verifyClickOnDeleteScheduleButton();
            // Verify the content on Delete Schedule confirm window
            scheduleMainPage.verifyTheContentOnDeleteScheduleDialog(unPublishedMessage, deleteForWeekText);
            // Verify the Delete button is disabled by default
            scheduleMainPage.verifyDeleteBtnDisabledOnDeleteScheduleDialog();
            // Verify the Delete button is enabled when clicking the check box
//            scheduleMainPage.verifyDeleteButtonEnabledWhenClickingCheckbox();
            // Verify the functionality of Delete button
            createSchedulePage.confirmDeleteSchedule();

            //Observe the schedule, Schedule should be deleted successfully
            isScheduleCreated = createSchedulePage.isWeekGenerated();
            SimpleUtils.assertOnFail("The Create schedule button fail to load！",
                    !isScheduleCreated, false);
            //Stay on the Manager tab, Create Schedule button is shown
            SimpleUtils.assertOnFail("The manager schedule view is not display！",
                    scheduleMainPage.isManagerViewSelected(), false);
            // Click on Suggested tab, observe the suggested schedule, Suggested schedule should be kept
            scheduleMainPage.clickOnSuggestedButton();
            SimpleUtils.assertOnFail("The schedule table is display correctly! ",
                    scheduleShiftTablePage.isScheduleTableDisplay(), false);
            //Go to Overview tab, check the status of the schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //The status should be Not Started
            scheduleStatusOnOverViewTable = scheduleOverviewPage.getScheduleWeeksStatus().get(index);
            expectedStatus = "Not Started";
            SimpleUtils.assertOnFail("The expected status is "+expectedStatus+ ", the actual status is: "+scheduleStatusOnOverViewTable
                    , scheduleStatusOnOverViewTable.equalsIgnoreCase(expectedStatus), false);

            // Disable Centralized Schedule Release
            controlsPage.gotoControlsPage();
            if (isLocationUsingControlsConfiguration) {
                controlsNewUIPage.clickOnControlsConsoleMenu();
                controlsNewUIPage.clickOnControlsSchedulingPolicies();
                controlsNewUIPage.clickOnGlobalLocationButton();
                Thread.sleep(10000);
                controlsNewUIPage.clickOnSchedulingPoliciesSchedulesAdvanceBtn();
                List<WebElement> CentralizedScheduleReleaseSelector = controlsNewUIPage.getAvailableSelector();
                WebElement yesItem = CentralizedScheduleReleaseSelector.get(0);
                WebElement noItem = CentralizedScheduleReleaseSelector.get(1);
                controlsNewUIPage.updateCentralizedScheduleRelease(noItem);
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            } else {
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToGlobalConfigurationInLocations();
                scheduleMainPage.clickOnEditButton();
                Thread.sleep(10000);
                List<WebElement> CentralizedScheduleReleaseSelector = controlsNewUIPage.getAvailableSelector();
                WebElement yesItem = CentralizedScheduleReleaseSelector.get(0);
                WebElement noItem = CentralizedScheduleReleaseSelector.get(1);
                controlsNewUIPage.updateCentralizedScheduleRelease(noItem);
                controlsNewUIPage.clickOnSaveBtn();
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
