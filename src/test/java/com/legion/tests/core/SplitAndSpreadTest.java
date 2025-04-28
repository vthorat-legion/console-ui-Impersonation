package com.legion.tests.core;

import com.legion.pages.ControlsNewUIPage;
import com.legion.pages.ControlsPage;
import com.legion.pages.DashboardPage;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.Constants;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.firstName;
import static com.legion.utils.MyThreadLocal.getDriver;
import java.util.ArrayList;
import java.util.List;

public class SplitAndSpreadTest extends TestBase {

    private static String controlEnterprice = "Vailqacn_Enterprise";
    private static String opEnterprice = "CinemarkWkdy_Enterprise";
    private static String releaseOpEnterprice = "Circlek_Enterprise";
    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static String opWorkRole = scheduleWorkRoles.get("TEAM_MEMBER_CORPORATE_THEATRE");
    private static String controlWorkRole = scheduleWorkRoles.get("RETAIL_BOOTFITTER");

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
    @Owner(owner = "Haya")
    //@Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Split Shift can be configured successfully")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySplitShiftCanBeConfiguredAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            //Go to OP page
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
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
            Thread.sleep(3000);
        controlsNewUIPage.turnOnOrTurnOffSplitShiftToggle(true);
        controlsNewUIPage.editSplitShiftPremium("1", "60", false);
        controlsNewUIPage.editSplitShiftPremium("1", "60", true);
        configurationPage.publishNowTheTemplate();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Will trigger Split Shift violation when search TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySplitShiftViolationWhenSearchTMAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        // Navigate to a week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule and pick up roles and employee.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Assigned");
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        String firstNameOfTM1 = shiftInfo.get(0);
        String workRoleOfTM1 = shiftInfo.get(4);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
        scheduleMainPage.saveSchedule();

        //add new shift and assign TM.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.moveSliderAtCertainPoint("10:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();


        //add new one shift and assign TM to check warning message.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("12:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1);
        String shiftWarningMessage = MyThreadLocal.getMessageOfTMScheduledStatus();
        SimpleUtils.assertOnFail("Should get split shift warning message!", shiftWarningMessage.toLowerCase().contains("split shift"), false);

        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        //verify split shift violation after saving.
        scheduleMainPage.clickOnOpenSearchBoxButton();
        scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
        List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("Split shift violation is not showing!", complianceMessage.contains("Split Shift"), false);

        //verify split shift violation after publishing.
        createSchedulePage.publishActiveSchedule();
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("Split shift violation is not showing!", complianceMessage.contains("Split Shift"), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify \"Split Shift\" violation will show on the second/third shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySplitShiftViolationWillShowOnTheSecondShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
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
        // create the schedule and pick up roles and employee.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Assigned");
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        String firstNameOfTM1 = shiftInfo.get(0);
        String lastNameOfTM1 = shiftInfo.get(5);
        String workRoleOfTM1 = shiftInfo.get(4);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
        scheduleMainPage.saveSchedule();

        //add 2 new shift and assign TM.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.moveSliderAtCertainPoint("10:00am",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8:00am",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();

        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.moveSliderAtCertainPoint("1:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("12:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();


        //add new one shift and assign TM to check warning message.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.moveSliderAtCertainPoint("4:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("3:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchWithOutSelectTM(firstNameOfTM1 + " " + lastNameOfTM1);
        String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
        SimpleUtils.assertOnFail("Should get split shift warning message!", shiftWarningMessage.toLowerCase().contains("split shift"), false);
        shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
        if(newShiftPage.ifWarningModeDisplay()){
            String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
            if (warningMessage.toLowerCase().contains("split shift")){
                SimpleUtils.pass("Split shifts warning message displays");
            } else {
                SimpleUtils.fail("There is no split shifts warning message displaying", false);
            }
            shiftOperatePage.clickOnAssignAnywayButton();
        } else {
            SimpleUtils.fail("There is no split shifts warning modal displaying!",false);
        }
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        //verify split shift violation on the second and the third shifts after saving.
        scheduleMainPage.clickOnOpenSearchBoxButton();
        scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
        List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
        SimpleUtils.assertOnFail("The first shift should not have the violation", !complianceMessage.contains("Split Shift"), false);
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("The second shift should have the violation", complianceMessage.contains("Split Shift"), false);
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(2));
        SimpleUtils.assertOnFail("The thirrd shift should have the violation", complianceMessage.contains("Split Shift"), false);

        //verify split shift on the second and the third shifts violation after publishing.
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
        SimpleUtils.assertOnFail("The first shift should not have the violation", !complianceMessage.contains("Split Shift"), false);
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("The second shift should have the violation", complianceMessage.contains("Split Shift"), false);
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(2));
        SimpleUtils.assertOnFail("The thirrd shift should have the violation", complianceMessage.contains("Split Shift"), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify \"This will trigger a split shift\" warning when dragging the avatar to change the assignment")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySplitShiftViolationWhenDragDropToChangeAssignmentAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
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
        // create the schedule and pick up roles and employee.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Assigned");
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        String firstNameOfTM1 = shiftInfo.get(0);
        String workRoleOfTM1 = shiftInfo.get(4);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        String firstNameOfTM2 = shiftInfo.get(0);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
        scheduleMainPage.saveSchedule();

        //add 2 new shift which interval time is more than 1 hour.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        //The first shift.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(0,0,0);
        newShiftPage.moveSliderAtCertainPoint("10",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();

        //The second shift(open shift).
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(0,0,0);
        newShiftPage.moveSliderAtCertainPoint("1",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("12",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM2);
        newShiftPage.clickOnOfferOrAssignBtn();


        //add new one shift in another day for the TM.
        //The third shift.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(1,1,1);
        newShiftPage.moveSliderAtCertainPoint("11",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();


        //Drag the third one and drop it the second one to change assignment.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(1, firstNameOfTM1, 0, firstNameOfTM2);
        String expectedViolationMessage ="split shift";
        SimpleUtils.assertOnFail("violation warning message is not expected!", scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(expectedViolationMessage, "swap"),true);
        List<String> swapData = scheduleShiftTablePage.getShiftSwapDataFromConfirmPage("swap");
        scheduleShiftTablePage.selectSwapOrAssignOption("swap");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        mySchedulePage.verifyShiftsAreSwapped(swapData);

        //verify split shift violation after saving.
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnOpenSearchBoxButton();
        scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
        List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("Split shift violation is not showing!", complianceMessage.contains("Split Shift"), false);

        //verify split shift violation after publishing.
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("Split shift violation is not showing!", complianceMessage.contains("Split Shift"), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify \"This will trigger a split shift\" warning when dragging the shift to another day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySplitShiftViolationWhenDragDropToAnotherDayAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
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
        // create the schedule and pick up roles and employee.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Assigned");
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        String firstNameOfTM1 = shiftInfo.get(0);
        String workRoleOfTM1 = shiftInfo.get(4);
        String lastNameOfTM1 = shiftInfo.get(5);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();

        //add 2 new shift which interval time is more than 1 hour.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        //The first shift.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(0,0,0);
        newShiftPage.moveSliderAtCertainPoint("10:00am",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8:00am",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();


        //add new one shift in another day for the TM.
        //The second shift.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM1);
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(1,1,1);
        newShiftPage.moveSliderAtCertainPoint("2:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("12:00pm",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();


        //Drag the third one and drop it the second one to change assignment.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        String weekday = scheduleShiftTablePage.getWeekDayTextByIndex(0);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(1, firstNameOfTM1, 0);
        String fullWeekDay = SimpleUtils.getFullWeekDayName(weekday.split(" ")[0]);
        String expectedViolationMessage = firstNameOfTM1 + " is scheduled 8:00 am - 10:00 am on "+fullWeekDay+". This will trigger a split shift.";
        scheduleShiftTablePage.verifyMessageOnCopyMoveConfirmPage(expectedViolationMessage,expectedViolationMessage);
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("Move");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        if (scheduleShiftTablePage.ifMoveAnywayDialogDisplay()) {
            scheduleShiftTablePage.moveAnywayWhenChangeShift();
        } else {
            SimpleUtils.fail("MOVE ANYWAY dialog failed to load!", false);
        }
        //verify split shift violation after saving.
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnOpenSearchBoxButton();
        scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
        List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("Split shift violation is not showing!", complianceMessage.contains("Split Shift"), false);

        //verify split shift violation after publishing.
        complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
        SimpleUtils.assertOnFail("Split shift violation is not showing!", complianceMessage.contains("Split Shift"), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify open shift with \"Split Shift\" cannot be claimed")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySplitShiftViolationOpenShiftCannotBeClaimedAsTeamMember(String browser, String username, String password, String location) throws Exception{
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
        String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
        String firstNameOfTM = tmFullName.split(" ")[0];
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
        // Navigate to a week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule and pick up roles and employee.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnOpenSearchBoxButton();
        scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
        String workRoleOfTM = "";
        if (scheduleShiftTablePage.getShiftsCount()!=0){
            workRoleOfTM = scheduleShiftTablePage.getTheShiftInfoByIndex(0).get(4);
        } else {
            scheduleMainPage.clickOnCloseSearchBoxButton();
            scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
            List<String> workRoles = scheduleShiftTablePage.verifyGroupByTitlesOrder();
            if (workRoles.size()>0){
                workRoleOfTM = workRoles.get(0);
            } else {
                SimpleUtils.fail("No work roles in the schedule!!", false);
            }
        }
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.saveSchedule();

        //add 2 new shift which interval time is more than 1 hour.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        //The first shift.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM);
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(0,0,0);
        newShiftPage.moveSliderAtCertainPoint("10",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM);
        newShiftPage.clickOnOfferOrAssignBtn();

        //The second shift(open shift).
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.selectWorkRole(workRoleOfTM);
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(0,0,0);
        newShiftPage.moveSliderAtCertainPoint("2",ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("12",ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();


        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        smartCardPage.clickLinkOnSmartCardByName("View Shifts");
        List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage("Error! We're sorry, you're no longer eligible to take this shift.");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Spread Of Hours can be configured")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySpreadOfHoursCanBeConfiguredAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
                //Go to OP page
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
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
                Thread.sleep(3000);

            controlsNewUIPage.turnOnOrTurnOffSpreadOfHoursToggle(false);
            controlsNewUIPage.turnOnOrTurnOffSpreadOfHoursToggle(true);
            controlsNewUIPage.editSpreadOfHoursPremium("1", "10", true);
            controlsNewUIPage.editSpreadOfHoursPremium("2", "12", false);
            controlsNewUIPage.verifyCloseSplitShiftPremiumDialogButton();

            configurationPage.publishNowTheTemplate();
            Thread.sleep(120000);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Will trigger Spread of hours violation when search TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySpreadOfHoursViolationWhenSearchTMAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");

            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            List<String> shiftInfo =  scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String tmFirstName = shiftInfo.get(0);
            int i=0;
            while (i< 200 && (tmFirstName.equalsIgnoreCase("open") || tmFirstName.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                tmFirstName  = shiftInfo.get(0);
                i++;
            }
            String tmLastName = shiftInfo.get(5);
            String workRole = shiftInfo.get(4);
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmFirstName);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.saveSchedule();

            //Create first shift for tm
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmFirstName+ " "+tmLastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            Thread.sleep(3000);
            scheduleMainPage.saveSchedule();

            //Create second shift for tm
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            Thread.sleep(3000);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("10:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("2:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchWithOutSelectTM(tmFirstName+ " "+tmLastName);
            //check the violation message in Status column
            Thread.sleep(3000);
            SimpleUtils.assertOnFail("The spread of hour violation fail to display in Status column! The actual message is: " + shiftOperatePage.getTheMessageOfTMScheduledStatus(),
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().toLowerCase().contains("spread of hours"), false);
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(tmFirstName);
            //check the message in warning mode
            Thread.sleep(5000);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = "spread of hours";
                String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
                if (warningMessage.toLowerCase().contains(warningMessage1.toLowerCase())){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! the expected is: "
                            + warningMessage1+ "The actual is: " + warningMessage, false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the compliance smart card
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("The compliance smart card display correctly! ",
                    smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
            //check the violation in i icon popup of new create shift
            List<WebElement> newAddedShift = scheduleShiftTablePage.getOneDayShiftByName(0, tmFirstName);
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The spread of hours violation message display incorrectly in i icon popup! ",
                        !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift.get(0)).contains("Spread of hours"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);

            List<String> messageFromInfoIconPopup = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift.get(1));
            SimpleUtils.assertOnFail("The spread of hours violation message display incorrectly in i icon popup! The actual message is: "+ messageFromInfoIconPopup,
                    messageFromInfoIconPopup.contains("Spread of hours"), false);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate spread of hours violation when edit shift time")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySpreadOfHoursViolationWhenEditShiftTimeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");
            }
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            int index = scheduleShiftTablePage.getRandomIndexOfShift();
            List<String> shiftInfo =  scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            String tmFirstName = shiftInfo.get(0);
            int i=0;
            while (i< 200 && (tmFirstName.equalsIgnoreCase("open") || tmFirstName.equalsIgnoreCase("Unassigned"))) {
                index = scheduleShiftTablePage.getRandomIndexOfShift();
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
                tmFirstName  = shiftInfo.get(0);
                i++;
            }
            String tmLastName = shiftInfo.get(5);
            String workRole = shiftInfo.get(4);
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmFirstName);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Create first shift for tm
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmFirstName +" "+tmLastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //check the violation in i icon popup of new create shift
            List<WebElement> newShifts = scheduleShiftTablePage.getOneDayShiftByName(0, tmFirstName);
            SimpleUtils.assertOnFail("The new shift fail to created! ",newShifts.size()>0, false);
            WebElement newAddedShift = newShifts.get(0);
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The spread of hours violation message display incorrectly in i icon popup! ",
                        !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Spread of hours"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            shiftOperatePage.editTheShiftTimeForSpecificShift(newAddedShift, "8am", "8pm");
            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(scheduleShiftTablePage.getShiftIndexById(newAddedShift.getAttribute("id").toString()));
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            String inputStartTime = "8:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            String inputEndTime = "8:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            shiftOperatePage.clickOnAssignAnywayButton();
            scheduleMainPage.saveSchedule();

            if (newAddedShift != null) {
                String warningMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).toString();
                SimpleUtils.assertOnFail("The spread of hours violation message display incorrectly in i icon popup! The actual is:"+ warningMessage,
                        warningMessage.contains("Spread of hours"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify 'This will trigger spread hours' warning when dragging the avatar to change the assignment")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySpreadOfHoursViolationWhenDraggingTheAvatarToChangeTheAssignmentAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");
            }

            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int i = 0;
            while (i < 50 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
                i++;
            }
            String workRoleOfTM1 = shiftInfo.get(4);
            String lastNameOfTM1 = shiftInfo.get(5);
            i = 0;
            while (i< 50 && (shiftInfo.get(0).equalsIgnoreCase(firstNameOfTM1)
                    || shiftInfo.get(0).equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                i++;
            }
            String firstNameOfTM2 = shiftInfo.get(0);
            String workRoleOfTM2 = shiftInfo.get(4);
            String lastNameOfTM2 = shiftInfo.get(5);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            Thread.sleep(3000);
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String myTimeOffLabel = "Time Off";
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            teamPage.rejectAllTheTimeOffRequests();
            teamPage.goToTeam();
            Thread.sleep(3000);
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM2);
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            teamPage.rejectAllTheTimeOffRequests();
            // Edit the Schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Delete all the shifts that are assigned to the team member
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);

            // Create new shift for TM1 on first day and second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(2);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create new shift for TM2 on First day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("9pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("1pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectWorkRole(workRoleOfTM2);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2 + " " + lastNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();

            // Save the Schedule
            scheduleMainPage.saveSchedule();

            // Edit the Schedule and try to drag TM1 on Monday to TM2 on Tuesday
            String warningMessage = "This will trigger spread hours.";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            i=0;
            while (!scheduleShiftTablePage.isDragAndDropConfirmPageLoaded() && i<5){
                scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(1,firstNameOfTM1,0,firstNameOfTM2);
                i++;
                Thread.sleep(2000);
            }
            SimpleUtils.assertOnFail("Spread of hours message display incorrectly on swap section!",
                    scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(warningMessage, "swap"), false);
            SimpleUtils.assertOnFail("Spread of hours  message display incorrectly on assign section!",
                    scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(warningMessage, "assign"), false);

            // Swap TM1 and TM2, check the TMs been swapped successfully
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("The TM: " + firstNameOfTM2 +" fail to swapped to second day",
                    scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM2) == 1, false);
            SimpleUtils.assertOnFail("The TM: " + firstNameOfTM1 +" fail to swapped to first day",
                    scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM1) == 2, false);

            //check compliance smart card display
            SimpleUtils.assertOnFail("Compliance smart card display successfully!",
                    smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
            smartCardPage.clickViewShift();

            //check the violation on the info popup
            List<WebElement> shiftsOnFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1);
            SimpleUtils.assertOnFail("Get compliance shift failed",shiftsOnFirstDay.size()>0, false);

            SimpleUtils.assertOnFail("Spread of hours compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOnFirstDay.get(shiftsOnFirstDay.size()-1)).contains("Spread of hours"), false);
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("Spread of hours compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOnFirstDay.get(shiftsOnFirstDay.size()-1)).contains("Spread of hours"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify 'This will trigger spread hours' warning when dragging the shift to another day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySpreadOfHoursViolationWhenDraggingTheShiftToAnotherDayAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");

            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRoleOfTM1 = shiftInfo.get(4);
            String lastNameOfTM1 = shiftInfo.get(5);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String myTimeOffLabel = "Time Off";
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            teamPage.rejectAllTheTimeOffRequests();
            // Edit the Schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Delete all the shifts that are assigned to the team member
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Create new shift for TM1 on first day and second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Mon");
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create new shift for TM1 on Second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("9pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("1pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName("Tue");
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            // Save the Schedule
            scheduleMainPage.saveSchedule();
            Thread.sleep(3000);
            List<Integer> shiftIndexes = scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1);
            SimpleUtils.assertOnFail("Failed to add two shifts!",
                    shiftIndexes != null && shiftIndexes.size() == 2, false);
//            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(shiftIndexes.get(0));
//            int dayIndexOfMon = Integer.parseInt(shiftInfo1.get(1));
            int dayIndexOfMon = 8;
            for (int i = 0; i<7; i++) {
                if (scheduleShiftTablePage.getWeekDayTextByIndex(i).contains("Mon")){
                    dayIndexOfMon = i;
                    break;
                }
            }
            // Edit the Schedule and try to drag TM1 on Tue day to Mon day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            Thread.sleep(5000);
            int i=0;
            while (!scheduleShiftTablePage.isDragAndDropConfirmPageLoaded() && i<5){
                scheduleShiftTablePage.dragOneShiftToAnotherDay(dayIndexOfMon+1,firstNameOfTM1,dayIndexOfMon);
                i++;
                Thread.sleep(2000);
            }
            String warningMessage = firstNameOfTM1 + " is scheduled 9:00 AM - 11:00 AM on Monday. This will trigger spread hours.";
            scheduleShiftTablePage.verifyMessageOnCopyMoveConfirmPage(warningMessage,warningMessage);

            // Swap TM1 and TM2, check the TMs been swapped successfully
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.copyAnywayWhenChangeShift();
            scheduleMainPage.saveSchedule();
            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            Thread.sleep(5000);
            //check compliance smart card display
            SimpleUtils.assertOnFail("Compliance smart card display successfully!",
                    smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
            smartCardPage.clickViewShift();

            //check the violation on the info popup
            WebElement shiftOnMon = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1).get(0));
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftOnMon);
            SimpleUtils.assertOnFail("Spread of hours compliance message display failed, the actual is:"+complianceMessage,
                    complianceMessage.contains("Spread of hours"), false);
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("Spread of hours compliance message display failed, the actual is:"+complianceMessage,
                    complianceMessage.contains("Spread of hours"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate open shift with 'Spread Of Hours' cannot be claimed")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOpenShiftWithSpreadOfHoursViolationCannotBeClaimedAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            Thread.sleep(3000);
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstNameOfTM = tmFullName.split(" ")[0];
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            // Go to Schedule page, Schedule tab
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");
            String workRoleOfTM1 = "";
            List<WebElement> shiftOfTM = scheduleShiftTablePage.getAllShiftsOfOneTM(firstNameOfTM);
            if (shiftOfTM.size() > 0) {
                List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM).get(0));
                workRoleOfTM1 = shiftInfo.get(4);
            } else {
                if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))){
                    workRoleOfTM1 = controlWorkRole;
                } else if (getDriver().getCurrentUrl().contains(propertyMap.get(opEnterprice))) {
                    workRoleOfTM1 = opWorkRole;
                }
            }
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM);
            String myTimeOffLabel = "Time Off";
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            teamPage.rejectAllTheTimeOffRequests();
            // Edit the Schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Delete all the shifts that are assigned to the team member
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Create new shift for TM1 on first day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmFullName);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create open shift on first day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("9pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("1pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();

            //Create open shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1 ,1, 1);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();

            // Save the Schedule
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            //  Click on the open shift -> Offer Team Members, click on Recommended TMs, check the TM list
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
            shiftOperatePage.clickOnOfferTMOption();
            Thread.sleep(5000);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            //TM should not show
            SimpleUtils.assertOnFail("TM should not show in recommended tab! ",
                    !shiftOperatePage.checkIfTMExistsInRecommendedTab(tmFullName), false);

            //Click on the open shift -> Offer Team Members, search the TM, assign it
            Thread.sleep(5000);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchTeamMemberByName(tmFullName);
            newShiftPage.clickOnOfferOrAssignBtn();

            //  Click on the open shift -> Offer Team Members, click on Recommended TMs, check the TM list
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(1);
            shiftOperatePage.clickOnOfferTMOption();
            Thread.sleep(5000);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            //TM should not show
            SimpleUtils.assertOnFail("TM should not show in recommended tab! ",
                    !shiftOperatePage.checkIfTMExistsInRecommendedTab(tmFullName), false);
            Thread.sleep(3000);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchTeamMemberByName(tmFullName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
//            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
//            createSchedulePage.publishActiveSchedule();

            //Check if the daily OT setting enabled
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
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Schedule Collaboration"), "edit");
//                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                Thread.sleep(3000);
            boolean isAllowEmployeeClaimOTOpenShift = controlsNewUIPage.checkIfEmployeeCanClaimOTOpenShift();

            if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())){
                switchToConsoleWindow();
            }
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // Validate that smartcard is available to recipient team member
            String smartCard = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + smartCard + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(smartCard), false);
            // Validate the availability of all cover request shifts in schedule table
            String linkName = "View Shifts";;
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            SimpleUtils.assertOnFail("Open shifts not loaded Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
            // Validate the availability of Claim Shift Request popup for the first shift
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));

            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(isAllowEmployeeClaimOTOpenShift?Constants.NoLongEligibleTakeShiftErrorMessage:Constants.WillTriggerDailyOTErrorMessage);
            // Validate the availability of Claim Shift Request popup for the second shift
            Thread.sleep(3000);
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(isAllowEmployeeClaimOTOpenShift?Constants.NoLongEligibleTakeShiftErrorMessage:Constants.WillTriggerDailyOTErrorMessage);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
