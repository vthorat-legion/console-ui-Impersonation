package com.legion.tests.core;

import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
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
import java.util.ArrayList;

public class ScheduleCopyTest extends TestBase {
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
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content on create schedule page in week view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentOnCreateSchedulePageInWeekViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
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

            //Verify day/week button should be disabled
            SimpleUtils.assertOnFail("The day and week view button should not enabled! ",
                    scheduleCommonPage.checkIfDayAndWeekViewButtonEnabled(), false);

            //Verify weeks are loaded in week view
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToPreviousWeek();

            //Verify Create Schedule button is loaded in week view
            SimpleUtils.assertOnFail("The Create shcedule button should loaded! ",
                    createSchedulePage.isGenerateButtonLoaded(), false);

            //Verify OPERATING HOURS section is loaded in week view
            toggleSummaryPage.verifyTheContentInOperatingHoursSection();
            //Verify STAFF section is loaded in week view
            toggleSummaryPage.verifyTheContentInStaffSection();
            //Verify Roster Updates section is loaded in week view
            toggleSummaryPage.verifyTheContentInRosterUpdatesSection();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the enhanced copy modal for non-dg flow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheEnhancedCopyModalForNonDgFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            String isBudgetEnabled = "Yes";
            //Check the budget is enabled or not
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToGlobalConfigurationInLocations();
                Thread.sleep(10000);
                isBudgetEnabled = controlsNewUIPage.getApplyLaborBudgetToSchedulesActiveBtnLabel();
                switchToConsoleWindow();
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
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00PM");
            if (smartCardPage.isRequiredActionSmartCardLoaded()){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(5000);
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            //Click on "Create Schedule"
            String weekInfo = scheduleCommonPage.getActiveWeekText().substring(10);
            createSchedulePage.clickCreateScheduleButton();
            //Verify the content on Confirm Operating Hours window
            createSchedulePage.verifyTheContentOnConfirmOperatingHoursWindow(weekInfo, location);
            //Verify the functioning of Edit button on Confirm Operating Hours window
            //Verify Start and End time can be updated
            createSchedulePage.editTheOperatingHours(new ArrayList<>());
            //Verify the functionality of Cancel button on Comfirm Operating Hours window
            createSchedulePage.clickExitBtnToExitCreateScheduleWindow();
            Thread.sleep(3000);
            SimpleUtils.assertOnFail("The create schedule window should not loaded! ",
                    !createSchedulePage.checkIfCreateScheduleWindowLoad(), false);
            //Verify the functionality of Next button on Confirm Operating Hours window
            createSchedulePage.clickCreateScheduleButton();
            createSchedulePage.clickNextButtonOnCreateScheduleWindow();
            float budgetHrs = -1;
            if (isBudgetEnabled.equalsIgnoreCase("yes")){
                //Verify the content on Enter Budget window
                createSchedulePage.verifyTheContentOnEnterBudgetWindow(weekInfo, location);
                //Verify the functioning of Edit button on Enter Budget window
                //Verify budget hours can be updated
                budgetHrs = createSchedulePage.checkEnterBudgetWindowLoadedForNonDG();
            }
            //Verify the content on Copy Schedule window
            createSchedulePage.verifyTheContentOnCopyScheduleWindow(weekInfo, location, budgetHrs, 0);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of copying a Suggested Schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheFunctionalityOfCopyingASuggestedScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

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
            if (smartCardPage.isRequiredActionSmartCardLoaded()){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            String weekInfo = scheduleCommonPage.getActiveWeekText().substring(10);
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextButtonOnCreateScheduleWindow();
            //Verify the content on Enter Budget window
            createSchedulePage.clickNextButtonOnCreateScheduleWindow();
            createSchedulePage.selectWhichWeekToCopyFrom("SUGGESTED");
            // Click on Next button successfully, schedule will be created
            createSchedulePage.clickNextButtonOnCreateScheduleWindow();
            createSchedulePage.verifyTheScheduleSuccessMessage(weekInfo);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the count of compliance shifts should be consistent with compliance smart card")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheCountOfComplianceShiftsShouldBeConsistentWithComplianceSmartCardAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

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
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00PM");
            int complianceCount = smartCardPage.getComplianceShiftCountFromSmartCard("COMPLIANCE");
            if (complianceCount== 0) {
                String workRole = shiftOperatePage.getRandomWorkRole();
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
                newShiftPage.clickOnDayViewAddNewShiftButton();
                newShiftPage.customizeNewShiftPage();
                newShiftPage.clearAllSelectedDays();
                newShiftPage.selectSpecificWorkDay(7);
                newShiftPage.selectWorkRole(workRole);
                newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
                newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
                newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
                newShiftPage.clickOnCreateOrNextBtn();
                newShiftPage.searchTeamMemberByName(location.length()>7?location.substring(0, 7):location);
                newShiftPage.clickOnOfferOrAssignBtn();
                Thread.sleep(3000);
                scheduleMainPage.saveSchedule();
            }

            complianceCount = smartCardPage.getComplianceShiftCountFromSmartCard("COMPLIANCE");
            if (complianceCount == 0){
                SimpleUtils.fail("The compliance count in Compliance smart card should not be 0! ", false);
            } else
                SimpleUtils.pass("There are "+complianceCount+ " compliance shifts in the schedule! ");
            if (smartCardPage.isRequiredActionSmartCardLoaded()){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
            if (firstWeekInfo.length() > 11) {
                firstWeekInfo = firstWeekInfo.trim().substring(10);
            }
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            String weekInfo = scheduleCommonPage.getActiveWeekText().substring(10);
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextButtonOnCreateScheduleWindow();
            //Verify the content on Enter Budget window
            createSchedulePage.clickNextButtonOnCreateScheduleWindow();
            createSchedulePage.selectWhichWeekToCopyFrom(firstWeekInfo);
            // Click on Next button successfully, schedule will be created
            createSchedulePage.clickNextButtonOnCreateScheduleWindow();
            String needComplianceReviewMessage1 = complianceCount+" Shift";
            String needComplianceReviewMessage2 = "Need compliance review";
            SimpleUtils.assertOnFail("The shift need compliance review message display incorrectly, the expected is:"
                            +needComplianceReviewMessage1 + needComplianceReviewMessage2 +" the actual is:"+createSchedulePage.getComplianceShiftsMessageOnScheduleSuccessModal(),
                    createSchedulePage.getComplianceShiftsMessageOnScheduleSuccessModal().contains(needComplianceReviewMessage1) &&
                            createSchedulePage.getComplianceShiftsMessageOnScheduleSuccessModal().contains(needComplianceReviewMessage2), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }
}
