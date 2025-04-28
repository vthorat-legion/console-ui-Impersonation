package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class SmartCopyConfigsInOP extends TestBase {
    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //The template the location is using.
    public enum templateInUse{
        SchedulePolicy_TEMPLATE_NAME("CINEMARK GENERAL TEMPLATE");
        private final String value;
        templateInUse(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum templateAction{
        Save_As_Draft("saveAsDraft"),
        Publish_Now("publishNow"),
        Publish_Later("publishLater");
        private final String value;
        templateAction(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum buttonGroup{
        Cancel("Cancel"),
        Close("Close"),
        OKWhenEdit("OK"),
        OKWhenPublish("OK"),
        Delete("Delete"),
        Save("Save"),
        EditTemplate("Edit template"),
        Edit("Edit"),
        Yes("Yes"),
        No("No");
        private final String value;
        buttonGroup(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum ScheduleCopyConfigItems{
        Full("Full schedule copy"),
        Partial("Partial schedule copy"),
        CopyRestriction("Enable schedule copy restrictions"),
        ViolationLimit("Violation limit"),
        BudgetOverageLimit("Budget overage limit");
        private final String value;
        ScheduleCopyConfigItems(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Smart Copy Configs in OP working")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySmartCopyConfigsInOPAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();

            //Go to OP page
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            //go to Configuration
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();

            cinemarkMinorPage.findDefaultTemplate(SmartCopyConfigsInOP.templateInUse.SchedulePolicy_TEMPLATE_NAME.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.EditTemplate.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenEdit.getValue());

            controlsNewUIPage.setCopyConfig(true, ScheduleCopyConfigItems.Full.getValue());
            controlsNewUIPage.setCopyConfig(false, ScheduleCopyConfigItems.Partial.getValue());

            cinemarkMinorPage.saveOrPublishTemplate(SmartCopyConfigsInOP.templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenPublish.getValue());
            Thread.sleep(3000);

            //Back to Console
            switchToConsoleWindow();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

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
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();
            createSchedulePage.checkEnterBudgetWindowLoadedForNonDG();
            //SimpleUtils.assertOnFail("Partial copy option should not load!", !createSchedulePage.isPartialCopyOptionLoaded(), false);
            createSchedulePage.clickBackBtnAndExitCreateScheduleWindow();

            //Go to OP to turn on partial copy.
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            //go to Configuration
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();

            cinemarkMinorPage.findDefaultTemplate(SmartCopyConfigsInOP.templateInUse.SchedulePolicy_TEMPLATE_NAME.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.EditTemplate.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenEdit.getValue());

            controlsNewUIPage.setCopyConfig(true, ScheduleCopyConfigItems.Partial.getValue());

            cinemarkMinorPage.saveOrPublishTemplate(SmartCopyConfigsInOP.templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenPublish.getValue());
            Thread.sleep(3000);

            //Back to Console
            switchToConsoleWindow();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();
            createSchedulePage.checkEnterBudgetWindowLoadedForNonDG();
            SimpleUtils.assertOnFail("Partial copy option should load!", createSchedulePage.isPartialCopyOptionLoaded(), false);
            createSchedulePage.clickBackBtnAndExitCreateScheduleWindow();

            //Go to OP to turn off Full copy and Partial copy.
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            //go to Configuration
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();

            cinemarkMinorPage.findDefaultTemplate(SmartCopyConfigsInOP.templateInUse.SchedulePolicy_TEMPLATE_NAME.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.EditTemplate.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenEdit.getValue());

            controlsNewUIPage.setCopyConfig(false, ScheduleCopyConfigItems.Full.getValue());
            controlsNewUIPage.setCopyConfig(false, ScheduleCopyConfigItems.Partial.getValue());

            cinemarkMinorPage.saveOrPublishTemplate(SmartCopyConfigsInOP.templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenPublish.getValue());
            Thread.sleep(3000);

            //Back to Console
            switchToConsoleWindow();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();
            createSchedulePage.checkEnterBudgetWindowLoadedForNonDG();
            SimpleUtils.assertOnFail("Partial copy option should not load!", !createSchedulePage.isCopyScheduleWindow(), false);
            createSchedulePage.clickBackBtnAndExitCreateScheduleWindow();

            //Go to OP to turn on Full copy and turn off Partial copy.
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            //go to Configuration
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();

            cinemarkMinorPage.findDefaultTemplate(SmartCopyConfigsInOP.templateInUse.SchedulePolicy_TEMPLATE_NAME.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.EditTemplate.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenEdit.getValue());

            controlsNewUIPage.setCopyConfig(true, ScheduleCopyConfigItems.Full.getValue());
            controlsNewUIPage.setCopyConfig(false, ScheduleCopyConfigItems.Partial.getValue());

            cinemarkMinorPage.saveOrPublishTemplate(SmartCopyConfigsInOP.templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(SmartCopyConfigsInOP.buttonGroup.OKWhenPublish.getValue());
            Thread.sleep(3000);

            //Back to Console
            switchToConsoleWindow();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();
            createSchedulePage.checkEnterBudgetWindowLoadedForNonDG();
            SimpleUtils.assertOnFail("Partial copy option should not load!", !createSchedulePage.isCopyScheduleWindow(), false);
            createSchedulePage.clickBackBtnAndExitCreateScheduleWindow();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }
}
