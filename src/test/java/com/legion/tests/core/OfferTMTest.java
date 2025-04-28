package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.Constants;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OfferTMTest extends TestBase {
    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, String> schedulePolicyData = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SchedulingPoliciesData.json");
    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");

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
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify \"Offer Team Members\" option is available for Open shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOfferTMOptionAvailableForOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
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
        createSchedulePage.createScheduleForNonDGFlowNewUI();

        //delete unassigned shifts and open shifts.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        //Delete all shifts are action required.
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Open");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();
        String workRoleOfTM = shiftOperatePage.getRandomWorkRole();

        //verify assigned shift in non-edit mode for week view and day view
        shiftOperatePage.clickOnProfileIcon();
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", !shiftOperatePage.isOfferTMOptionVisible(), false);
        scheduleCommonPage.clickOnDayView();
        scheduleShiftTablePage.clickOnProfileIconOfShiftInDayView("no");
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", !shiftOperatePage.isOfferTMOptionVisible(), false);
        scheduleCommonPage.clickOnWeekView();

        //verify assigned shift in edit mode for week view and day view
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        shiftOperatePage.clickOnProfileIcon();
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", !shiftOperatePage.isOfferTMOptionVisible(), false);
        scheduleCommonPage.clickOnDayView();
        scheduleShiftTablePage.clickOnProfileIconOfShiftInDayView("no");
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", !shiftOperatePage.isOfferTMOptionVisible(), false);
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        //create auto open shifts.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRoleOfTM);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();

        //verify auto open shift in edit mode.
        //--verify in day view
        scheduleShiftTablePage.clickOnProfileIconOfShiftInDayView("open");
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
        //--verify in week view
        scheduleCommonPage.clickOnWeekView();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
        scheduleMainPage.saveSchedule();
        //verify auto open shift in non-edit mode.
        //--verify in day view
        scheduleCommonPage.clickOnDayView();
        scheduleShiftTablePage.clickOnProfileIconOfShiftInDayView("open");
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled when schedule is never published!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
        //--verify in week view
        scheduleCommonPage.clickOnWeekView();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled when schedule is never published!", !shiftOperatePage.isOfferTMOptionEnabled(), false);

        //create manual open shifts.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
        scheduleCommonPage.clickOnDayView();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRoleOfTM);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.verifySelectTeamMembersOption();
        newShiftPage.clickOnOfferOrAssignBtn();
        //verify manual open shift in edit mode.
        //--verify in day view
        scheduleShiftTablePage.clickOnProfileIconOfShiftInDayView("open");
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
        //--verify in week view
        scheduleCommonPage.clickOnWeekView();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
        scheduleMainPage.saveSchedule();
        //verify manual open shifts in non-edit mode.
        //--verify in day view
        scheduleCommonPage.clickOnDayView();
        scheduleShiftTablePage.clickOnProfileIconOfShiftInDayView("open");
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled when schedule is never published!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
        //--verify in week view
        scheduleCommonPage.clickOnWeekView();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
        SimpleUtils.assertOnFail("Offer TMs option should be disabled when schedule is never published!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the functionality of \"Offer Team Members\" for Open Shift: Auto in non-Edit mode")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFunctionalityOfOfferTMForAutoOpenShiftsInNonEditModeAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            //click search button, to pick a work role won't get role violation.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnCloseSearchBoxButton();

            //create auto open shifts.
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //verify auto open shift in non-edit mode.
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            Thread.sleep(3000);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            shiftOperatePage.verifyRecommendedTableHasTM();
            Thread.sleep(3000);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();
            loginPage.logOut();

            //login with TM.
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            SimpleUtils.assertOnFail("Didn't get open shift offer!", scheduleShiftTablePage.getShiftsCount()==1, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the functionality of \"Offer Team Members\" for Open Shift: Auto in Edit Mode")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFunctionalityOfOfferTMForAutoOpenShiftsInEditModeAsTeamMember(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

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
        createSchedulePage.createScheduleForNonDGFlowNewUI();

        //delete unassigned shifts and open shifts.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        //Delete all shifts are action required.
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Open");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnFilterBtn();
        Thread.sleep(3000);
        scheduleMainPage.selectWorkRoleFilterByText(workRoleOfTM, false);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();

        //create auto open shifts.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        //newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8","8");
        newShiftPage.selectWorkRole(workRoleOfTM);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
        String selectedShiftId= selectedShift.getAttribute("id");
        int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);

        //verify auto open shift in edit mode.
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
        scheduleMainPage.saveSchedule();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
        shiftOperatePage.clickOnOfferTMOption();
        String shiftInfoInWindows = shiftOperatePage.getViewStatusShiftsInfo();
        shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
        shiftOperatePage.verifyRecommendedTableHasTM();
        shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
        newShiftPage.searchTeamMemberByName(firstNameOfTM);
        newShiftPage.clickOnOfferOrAssignBtn();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        scheduleShiftTablePage.clickViewStatusBtn();
        shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
        shiftOperatePage.closeViewStatusContainer();
        SimpleUtils.assertOnFail("shift time is not consistent!", shiftInfoInWindows.replaceAll(" ", "").toLowerCase().contains(shiftInfo.get(2).replaceAll(" ", "").toLowerCase()), false);
        SimpleUtils.assertOnFail("shift work role is not consistent!", shiftInfoInWindows.toLowerCase().contains(shiftInfo.get(4).toLowerCase()), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the functionality of \"Offer Team Members\" for Open Shift: Manual in non-Edit mode")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFunctionalityOfOfferTMForManualOpenShiftsInNonEditModeAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            //click search button, to pick a work role won't get role violation.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnCloseSearchBoxButton();


            //create manual open shifts.
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("5pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //verify manual open shift in non-edit mode.
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            Thread.sleep(3000);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            shiftOperatePage.verifyRecommendedTableHasTM();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();
            loginPage.logOut();

            //login with TM.
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            SimpleUtils.assertOnFail("Didn't get open shift offer!", scheduleShiftTablePage.getShiftsCount()==1, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the functionality of \"Offer Team Members\" for Open Shift: Manual in Edit Mode")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFunctionalityOfOfferTMForManualOpenShiftsInEditModeAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText(workRoleOfTM);
            //shiftOperatePage.deleteAllShiftsInWeekView();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();

            //create manual open shifts.
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8","8");
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            String selectedShiftId= selectedShift.getAttribute("id");
            int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);

            //verify manual open shift in edit mode.
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            scheduleMainPage.saveSchedule();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            String shiftInfoInWindows = shiftOperatePage.getViewStatusShiftsInfo();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            shiftOperatePage.verifyRecommendedTableHasTM();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();
            SimpleUtils.assertOnFail("shift time is not consistent!", shiftInfoInWindows.replaceAll(" ", "").toLowerCase().contains(shiftInfo.get(2).replaceAll(" ", "").toLowerCase()), false);
            SimpleUtils.assertOnFail("shift work role is not consistent!", shiftInfoInWindows.toLowerCase().contains(shiftInfo.get(4).toLowerCase()), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the functionality of \"Offer Team Members\" for converting assigned shift to open in non-Edit mode")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFunctionalityOfOfferTMForConvertToOpenShiftsInNonEditModeAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();

            //convert a shift to open shift
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectWorkRoleFilterByText(workRoleOfTM, true);
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnConvertToOpenShift();
            shiftOperatePage.convertToOpenShiftDirectly();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //verify the open shift in non-edit mode.
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();
            loginPage.logOut();

            //login with TM.
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            SimpleUtils.assertOnFail("Didn't get open shift offer!", scheduleShiftTablePage.getShiftsCount()==1, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the functionality of \"Offer Team Members\" for converting assigned shift to open in Edit mode")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFunctionalityOfOfferTMForConvertToOpenShiftsInEditModeAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();

            //convert a shift to open shift
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectWorkRoleFilterByText(workRoleOfTM, true);
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnConvertToOpenShift();
            shiftOperatePage.convertToOpenShiftDirectly();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            String selectedShiftId= selectedShift.getAttribute("id");
            int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);

            //verify assign TM in edit mode.
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            scheduleMainPage.saveSchedule();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            String shiftInfoInWindows = shiftOperatePage.getViewStatusShiftsInfo();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            shiftOperatePage.verifyRecommendedTableHasTM();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();
            SimpleUtils.assertOnFail("shift time is not consistent!", shiftInfoInWindows.replaceAll(" ", "").toLowerCase().contains(shiftInfo.get(2).replaceAll(" ", "").toLowerCase()), false);
            SimpleUtils.assertOnFail("shift work role is not consistent!", shiftInfoInWindows.toLowerCase().contains(shiftInfo.get(4).toLowerCase()), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the open shift is assigned successfully")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheOpenShiftIsAssignedSuccessfullyAsTeamMember(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
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
        createSchedulePage.createScheduleForNonDGFlowNewUI();

        //delete unassigned shifts and open shifts.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        //Delete all shifts are action required.
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Open");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.clickOnFilterBtn();
        //click search button, to pick a work role won't get role violation.
        scheduleMainPage.clickOnOpenSearchBoxButton();
        scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
        String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnCloseSearchBoxButton();

        //create manual open shifts.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRoleOfTM);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        scheduleMainPage.saveSchedule();
        WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
        String selectedShiftId= selectedShift.getAttribute("id");
        int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);


        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
        shiftOperatePage.clickOnOfferTMOption();
        newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM, location);
        newShiftPage.clickOnOfferOrAssignBtn();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        scheduleShiftTablePage.clickViewStatusBtn();
        shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
        shiftOperatePage.closeViewStatusContainer();
        loginPage.logOut();

        //login with TM.
        loginToLegionAndVerifyIsLoginDone(username, password, location);
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        smartCardPage.clickLinkOnSmartCardByName("View Shifts");
        SimpleUtils.assertOnFail("Didn't get open shift offer!", scheduleShiftTablePage.getShiftsCount()==1, false);
        List<String> shiftInfoFromTMView = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(0);
        SimpleUtils.assertOnFail("shift info is not consistent", shiftInfo.get(2).contains(shiftInfoFromTMView.get(2)) && shiftInfoFromTMView.get(4).contains(shiftInfo.get(4)), false);

        List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
        activityPage.verifyActivityOfShiftOffer(firstNameOfTM, location);
        activityPage.approveOrRejectShiftOfferRequestOnActivity(firstNameOfTM, ActivityTest.approveRejectAction.Approve.getValue());
        activityPage.closeActivityWindow();

        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        scheduleCommonPage.navigateToNextWeek();
        shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
        SimpleUtils.assertOnFail("Open shift is not assigned successfully!", shiftInfo.get(0).equalsIgnoreCase(firstNameOfTM), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the open shift is not assigned if SM rejected")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheOpenShiftIsNotAssignedIfSMRejectedAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            
            // 1.Checking configuration in controls
            String option = "Always";
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
            //===================================
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            //click search button, to pick a work role won't get role violation.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM);
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnCloseSearchBoxButton();

            //create auto open shifts.
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            String selectedShiftId= selectedShift.getAttribute("id");
            int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);


            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();
            loginPage.logOut();

            //login with TM.
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            SimpleUtils.assertOnFail("Didn't get open shift offer!", scheduleShiftTablePage.getShiftsCount()==1, false);
            List<String> shiftInfoFromTMView = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(0);
            SimpleUtils.assertOnFail("shift info is not consistent", shiftInfo.get(2).contains(shiftInfoFromTMView.get(2)) && shiftInfoFromTMView.get(4).contains(shiftInfo.get(4)), false);

            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
            activityPage.verifyActivityOfShiftOffer(firstNameOfTM, location);
            activityPage.approveOrRejectShiftOfferRequestOnActivity(firstNameOfTM, ActivityTest.approveRejectAction.Reject.getValue());
            activityPage.closeActivityWindow();
            loginPage.logOut();

            //log in as TM.
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            SimpleUtils.assertOnFail("shouldn't get any open shift offer!", scheduleShiftTablePage.getShiftsCount()==0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify 2 TMs claim the open shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verify2TMsClaimTheOpenShiftAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM1 = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            //log in as TM2 to get nickname.
            loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
            String firstNameOfTM2 = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();


            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            // 1.Checking configuration in controls
            String option = "Always";
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
            //===================================
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);

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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();

            //create manual open shifts.
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();


            shiftOperatePage.clickOnProfileIconOfOpenShift();
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM1, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM2, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            loginPage.logOut();

            //login with TM1 to claim.
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            List<String> claimShift = new ArrayList<>(Arrays.asList("Claim Shift"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
            loginPage.logOut();

            //login with TM2 to claim.
            loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            claimShift = new ArrayList<>(Arrays.asList("Claim Shift"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
            loginPage.logOut();

            //log in as SM to check.
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
            activityPage.verifyActivityOfShiftOffer(firstNameOfTM2, location);
            activityPage.approveOrRejectShiftOfferRequestOnActivity(firstNameOfTM1, ActivityTest.approveRejectAction.Approve.getValue());
            String expectedTopMessage = "Error! Failed to Approve";
            activityPage.verifyApproveShiftOfferRequestAndGetErrorOnActivity(firstNameOfTM1, expectedTopMessage);
            activityPage.closeActivityWindow();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the second TM cannot claim the open shift when never need SM approval")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySecondTMCannotClaimTheOpenShiftAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM1 = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
            String firstNameOfTM2 = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            // 1.Checking configuration in controls
            String option = "Never";
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
            //===================================

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
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            //click search button, to pick a work role won't get role violation.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnCloseSearchBoxButton();;

            //create manual open shifts.
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();


            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM1, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM2, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            loginPage.logOut();

            //login with TM1 to claim.
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimSuccessMessage);
            loginPage.logOut();

            //login with TM2 to claim.
            loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            SimpleUtils.assertOnFail("shouldn't get any open shift offer!", scheduleShiftTablePage.getShiftsCount()==0, false);
            loginPage.logOut();

            //log in as AM to set back the config.
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            option = "Always";
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), true);
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify TM can be offered if claiming successfully and converting the assigned shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMCanBeOfferedAsTeamMember(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LoginPage loginPage = pageFactory.createConsoleLoginPage();

        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String firstNameOfTM1 = profileNewUIPage.getNickNameFromProfile();
        loginPage.logOut();


        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        // 1.Checking configuration in controls
        String option = "Never";
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
        boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
        SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
        controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
        //===================================

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
        createSchedulePage.createScheduleForNonDGFlowNewUI();

        //delete unassigned shifts and open shifts.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        //Delete all shifts are action required.
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Open");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.clickOnFilterBtn();
        //click search button, to pick a work role won't get role violation.
        scheduleMainPage.clickOnOpenSearchBoxButton();
        scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
        String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnCloseSearchBoxButton();

        //create manual open shifts.
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRoleOfTM);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();

        WebElement openShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
        String selectedShiftId= openShift.getAttribute("id");
        int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
        scheduleShiftTablePage.clickViewStatusBtn();
        shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM1, "draft");
        shiftOperatePage.closeViewStatusContainer();
        createSchedulePage.publishActiveSchedule();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        scheduleShiftTablePage.clickViewStatusBtn();
        shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM1, "offered");
        shiftOperatePage.closeViewStatusContainer();
        loginPage.logOut();

        //login with TM to claim.
        loginToLegionAndVerifyIsLoginDone(username, password, location);
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        smartCardPage.clickLinkOnSmartCardByName("View Shifts");
        List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimSuccessMessage);
        loginPage.logOut();

        //log in as SM to check the shift
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

        scheduleCommonPage.navigateToNextWeek();
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
        SimpleUtils.assertOnFail("Open shift is not assigned successfully!", shiftInfo.get(0).equalsIgnoreCase(firstNameOfTM1), false);

        //convert to open shift.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
        shiftOperatePage.clickOnConvertToOpenShift();
        shiftOperatePage.convertToOpenShiftDirectly();
        scheduleMainPage.saveSchedule();
        shiftOperatePage.clickOnProfileIconOfOpenShift();
        scheduleShiftTablePage.clickViewStatusBtn();
/*        //=============SCH-3418=============
        shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM1, "accepted");
        shiftOperatePage.closeViewStatusContainer();
		shiftOperatePage.clickOnProfileIconOfOpenShift();
		shiftOperatePage.clickOnOfferTMOption();
		newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM1, location);
		newShiftPage.clickOnOfferOrAssignBtn();
		shiftOperatePage.clickOnProfileIconOfOpenShift();
		scheduleShiftTablePage.clickViewStatusBtn();
		shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM1, "offered");
*/		//===================================
        shiftOperatePage.closeViewStatusContainer();

        //Go to set back the config.
        option = "Always";
        controlsNewUIPage.clickOnControlsConsoleMenu();
        controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
        SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), true);
        controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Offer Team Members is disabled in past days")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOfferTMDisabledInPastDaysForOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToPreviousWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.saveSchedule();
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();

            //create auto open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            //create manual open shift.
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.clickOnOfferOrAssignBtn();
            //convert a shift to open shift.
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnConvertToOpenShift();
            shiftOperatePage.convertToOpenShiftDirectly();
            scheduleMainPage.saveSchedule();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();



            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToPreviousWeek();

            //verify Offer TM option is disabled.
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);
            SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
            SimpleUtils.assertOnFail("Offer TMs option should be disabled!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(0);

            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(1);
            SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
            SimpleUtils.assertOnFail("Offer TMs option should be disabled!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(1);

            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(2);
            SimpleUtils.assertOnFail("Offer TMs option should be visible!", shiftOperatePage.isOfferTMOptionVisible(), false);
            SimpleUtils.assertOnFail("Offer TMs option should be disabled!", !shiftOperatePage.isOfferTMOptionEnabled(), false);
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(2);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the count on WANT MORE HOURS? smart card when needing approval")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheShiftOfferCountWhenNeedingApprovalAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String firstName = profileNewUIPage.getNickNameFromProfile();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            SimpleUtils.assertOnFail("Profile page failed to load!", profileNewUIPage.isProfilePageLoaded(), false);
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // "Is approval by Manager required when an employee claims an Open Shift?" is set to "Always"
            String option = "Always";
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.selectGroupByFilter(jobTitle);
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.selectDaysByIndex(0,1,2);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName);
            newShiftPage.clickOnOfferOrAssignBtn();

            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            // Wait for 80 seconds when the shift offer is offered
            Thread.sleep(80000);
            loginPage.logOut();

            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'My Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();

            String smartCard = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + smartCard + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(smartCard), false);
            // Verify can get the shift offer count from smart card
            int originalCount = smartCardPage.getCountFromSmartCardByName(smartCard);
            scheduleMainPage.clickOnFilterBtn();
            // Verify can get the open shift count from filter
            int offeredCount = scheduleMainPage.getSpecificFiltersCount("Offered");
            String linkName = "View Shifts";
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            SimpleUtils.assertOnFail("Open shifts not loaded Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
            // Verify the count on smart card won't change if accepting the shift offer
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);

            int shiftOfferCount = smartCardPage.getCountFromSmartCardByName(smartCard);
            if (originalCount == shiftOfferCount) {
                SimpleUtils.pass("The count on smart card doesn't change when accepting the shift offer and need approval!");
            } else {
                SimpleUtils.fail("The count is changed when accepting the shift offer and need approval", false);
            }

            // Verify the count in filter won't change
            scheduleMainPage.clickOnFilterBtn();
            int currentOfferedCount = scheduleMainPage.getSpecificFiltersCount("Offered");
            if (offeredCount == currentOfferedCount) {
                SimpleUtils.pass("The count in filter doesn't change when accepting the shift offer and need approval!");
            } else {
                SimpleUtils.fail("The count is changed when accepting the shift offer and need approval", false);
            }

            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.clickOnDeclineButton();

            // Verify the count on smart card will decrease if declining the shift offer
            shiftOfferCount = smartCardPage.getCountFromSmartCardByName(smartCard);
            if (originalCount - shiftOfferCount == 1) {
                SimpleUtils.pass("The count on smart card decreased when declining the shift offer and need approval!");
            } else {
                SimpleUtils.fail("The count doesn't decreased when decling the shift offer and need approval", false);
            }

            // Verify the count in filter will decrease if declining the shift offer
            scheduleMainPage.clickOnFilterBtn();
            currentOfferedCount = scheduleMainPage.getSpecificFiltersCount("Offered");
            if (offeredCount - currentOfferedCount == 1) {
                SimpleUtils.pass("The count in filter decreased when declining the shift offer and need approval!");
            } else {
                SimpleUtils.fail("The count doesn't decrease when declining the shift offer and need approval", false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the count on WANT MORE HOURS? smart card when not needing approval")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheShiftOfferCountWhenNotNeedingApprovalAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String firstName = profileNewUIPage.getNickNameFromProfile();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            SimpleUtils.assertOnFail("Profile page failed to load!", profileNewUIPage.isProfilePageLoaded(), false);
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // "Is approval by Manager required when an employee claims an Open Shift?" is set to "Never"
            String option = "Never";
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.selectGroupByFilter(jobTitle);
            String workRoleOfTM = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.selectDaysByIndex(0,1,2);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName);
            newShiftPage.clickOnOfferOrAssignBtn();

            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            // Wait for 10 seconds when the shift offer is offered
            Thread.sleep(20000);
            loginPage.logOut();

            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'My Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();

            String smartCard = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + smartCard + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(smartCard), false);
            // Verify can get the shift offer count from smart card
            int originalCount = smartCardPage.getCountFromSmartCardByName(smartCard);
            scheduleMainPage.clickOnFilterBtn();
            // Verify can get the open shift count from filter
            int offeredCount = scheduleMainPage.getSpecificFiltersCount("Offered");
            String linkName = "View Shifts";;
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            SimpleUtils.assertOnFail("Open shifts not loaded Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
            // Verify the count on smart card will decrease if accepting the shift offer
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimSuccessMessage);

            int shiftOfferCount = smartCardPage.getCountFromSmartCardByName(smartCard);
            if (originalCount - shiftOfferCount == 1) {
                SimpleUtils.pass("The count on smart card decreased when accepting the shift offer and not need approval!");
            } else {
                SimpleUtils.fail("The count doesn't decrease when accepting the shift offer and not need approval", false);
            }

            // Verify the count in filter will decrease if declining the shift offer
            scheduleMainPage.clickOnFilterBtn();
            int currentOfferedCount = scheduleMainPage.getSpecificFiltersCount("Offered");
            if (offeredCount - currentOfferedCount == 1) {
                SimpleUtils.pass("The count in filter decreased when accepting the shift offer and not need approval!");
            } else {
                SimpleUtils.fail("The count doesn't decrease when accepting the shift offer and not need approval", false);
            }

            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.clickOnDeclineButton();

            // Verify the count on smart card will decrease if declining the shift offer
            shiftOfferCount = smartCardPage.getCountFromSmartCardByName(smartCard);
            if (originalCount - shiftOfferCount == 1) {
                SimpleUtils.pass("The count on smart card decreased when declining the shift offer and need approval!");
            } else {
                SimpleUtils.fail("The count doesn't decreased when decling the shift offer and need approval", false);
            }

            // Verify the count in filter will decrease if declining the shift offer
            scheduleMainPage.clickOnFilterBtn();
            currentOfferedCount = scheduleMainPage.getSpecificFiltersCount("Offered");
            if (offeredCount - currentOfferedCount == 1) {
                SimpleUtils.pass("The count in filter decreased when declining the shift offer and need approval!");
            } else {
                SimpleUtils.fail("The count doesn't decrease when declining the shift offer and need approval", false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Ting")
    @Enterprise(name = "")
    @TestName(description = "Should be able to receive one shift offer by TM while multiple shifts were offered at the same duration of time")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySingleShiftOfferReceivedByTMWithMultipleOpenShiftsOfferedAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

            // Start to check and generate target schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            // Delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            shiftOperatePage.deleteAllShiftsInWeekView();

            // Modify corresponding work role by enterprise
            String workRoleOfTM = "Retail Associate";
            String enterprise = System.getProperty("enterprise").toLowerCase();
            if (enterprise.equalsIgnoreCase("cinemark-wkdy")) {
                workRoleOfTM = "Team Member Corporate-Theatre";
            }

            // Create multiple open shifts at the same duration of hours in one day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("3pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.setShiftsPerDay(3);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectMultipleOrSpecificWorkDay(3, true);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            // Publish schedule
            createSchedulePage.publishActiveSchedule();

            // Offer TM in non-edit mode to make sure the TM will be offered with multiple shifts
            String shiftType = "Open";
            int openShiftCount = shiftOperatePage.countShiftsByUserName(shiftType);
            shiftOperatePage.offerTMByOpenShiftsCount(openShiftCount, firstNameOfTM, location);

            loginPage.logOut();

            //login with TM.
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            SimpleUtils.assertOnFail("Didn't get open shift offer!", scheduleShiftTablePage.getShiftsCount() == 1, false);
            SimpleUtils.assertOnFail("The shift hours were incorrect!", scheduleShiftTablePage.getShiftTextByIndex(0).replaceAll(" ", "").contains("9:00am-3:00pm"), false );
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Ting")
    @Enterprise(name = "")
    @TestName(description = "Should be able to receive two shifts offer by TM while multiple shifts were offered at the two different duration of time")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMultipleShiftOfferReceivedByTMWithMultipleOpenShiftsOfferedAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String firstNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

            // Start to check and generate target schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            // Delete all shifts in the week view list grid
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            shiftOperatePage.deleteAllShiftsInWeekView();

            // Modify corresponding work role by enterprise
            String workRoleOfTM = "Retail Associate";
            String enterprise = System.getProperty("enterprise").toLowerCase();
            if (enterprise.equalsIgnoreCase("cinemark-wkdy")) {
                workRoleOfTM = "Team Member Corporate-Theatre";
            }

            // Create multiple open shifts in one day first duration of hours
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.setShiftsPerDay(2);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectMultipleOrSpecificWorkDay(3, true);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            // Create multiple open shifts in one day second duration of hours
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRoleOfTM);
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("4pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.setShiftsPerDay(2);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectMultipleOrSpecificWorkDay(3, true);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            // Offer TM in non-edit mode to make sure the TM will be offered with multiple shifts
            String shiftType = "Open";
            int openShiftCount = shiftOperatePage.countShiftsByUserName(shiftType);
            shiftOperatePage.offerTMByOpenShiftsCount(openShiftCount, firstNameOfTM, location);

            // Publish schedule
            scheduleMainPage.publishOrRepublishSchedule();
            loginPage.logOut();

            //login with TM.
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            SimpleUtils.assertOnFail("Didn't get open shift offer!", scheduleShiftTablePage.getShiftsCount() == 2, false);
            SimpleUtils.assertOnFail("The shift hours were incorrect!", scheduleShiftTablePage.getShiftTextByIndex(0).replaceAll(" ", "").contains("8:00am-2:00pm"), false );
            SimpleUtils.assertOnFail("The shift hours were incorrect!", scheduleShiftTablePage.getShiftTextByIndex(1).replaceAll(" ", "").contains("10:00am-4:00pm"), false );
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
