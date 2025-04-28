package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
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
import java.util.*;

public class ApproveActivitiesByHomeOrNonHomeManagerTest extends TestBase {


    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static String opWorkRole = scheduleWorkRoles.get("TEAM_MEMBER_CORPORATE_THEATRE");
    private static String controlWorkRole = scheduleWorkRoles.get("RETAIL_BOOTFITTER");
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
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the manager need to approve the claimed open shift when enable the claim open shift in home location setting on OP env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyManagerNeedToApproveShiftWhenEnableShiftInHomeLocationSettingOnOPAsTeamMember (String browser, String username, String password, String location) throws Exception {
        try {
            Boolean isOP = true;
            String option = "Yes";
            verifyManagerNeedToApproveShift(isOP, option, location);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the manager doesn't need to approve the claimed open shift when disable the claim open shift in home location setting on OP env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyManagerDoesNotNeedToApproveShiftWhenDisableShiftInHomeLocationSettingOnOPAsTeamMember (String browser, String username, String password, String location) throws Exception {
        try {
            Boolean isOP = true;
            String option = "No";
            verifyManagerDoesNotNeedToApproveShift(isOP, option, location);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the manager and non-home manager need to approve the claimed open shift when enable the claim open shift in non-home location setting on OP env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyNonHomeManagerNeedToApproveShiftWhenEnableShiftInNonHomeLocationSettingOnOPAsTeamMember (String browser, String username, String password, String location) throws Exception {
        try {

            Boolean isOP = true;
            String option = "Yes";
            verifyNonHomeManagerNeedToApproveShift(isOP, option, location);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the managers doesn't need to approve the claimed open shift when disable the claim open shift in non-home location setting on OP env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyNonHomeManagerNotNeedToApproveShiftWhenDisableShiftInNonHomeLocationSettingOnOPAsTeamMember (String browser, String username, String password, String location) throws Exception {
        try {
            Boolean isOP = true;
            String option = "No";
            verifyNonHomeManagerDoesNotNeedToApproveShift(isOP, option, location);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the manager doesn't need to approve the claimed open shift of home location when set the claim open shift to Non-home location only on Control env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyActivityOfClaimOpenShiftNoNeedApprovalForHomeManagerWhenSetOpenShiftSettingToNonHomeLocationOnlyAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try{
            Boolean isOP = false;
            String option = "Non-home location only";
            verifyManagerDoesNotNeedToApproveShift(isOP, option, location);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the notification message when TM claim open shift automatically")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyActivityOfClaimOpenShiftNoApprovalAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try{
            Boolean isOP = false;
            String option = "Non-home location only";
            verifyNonHomeManagerNeedToApproveShift(isOP, option, location);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the manager and non-home manager need to approve the claimed open shift of non-home location when set the claim open shift to Always on Control env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyActivityOfClaimOpenShiftNeedApprovalWhenSetOpenShiftSettingToAlwaysAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try{
            Boolean isOP = false;
            String option = "Always";
            verifyNonHomeManagerNeedToApproveShift(isOP, option, location);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void verifyManagerNeedToApproveShift (Boolean isOP, String option, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String teamMemberName = profileNewUIPage.getNickNameFromProfile();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

        if (isOP) {
            // 1. Go to OP-> Schedule Collaboration -> Open Shifts -> enable -- Is approval required by Manager when an employee claims an Open Shift in a home location?
            OpsPortalLocationsPage opsPortalLocationsPage = (OpsPortalLocationsPage) pageFactory.createOpsPortalLocationsPage();
            opsPortalLocationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.goToTemplateDetailsPage("Schedule Collaboration");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.enableOrDisableApproveShiftInHomeLocationSetting(option);
            configurationPage.publishNowTheTemplate();
            switchToConsoleWindow();
        }

        // 2.admin create one manual open shift and assign to specific TM

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        //to generate schedule  if current week is not generated
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(isActiveWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        if (isOP)
            newShiftPage.selectWorkRole(opWorkRole);
        else
            newShiftPage.selectWorkRole(controlWorkRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(teamMemberName);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        loginPage.logOut();

        // 3.Login with the TM to claim the shift
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        dashboardPage.goToTodayForNewUI();
        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        String cardName = "WANT MORE HOURS?";
        SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
        String linkName = "View Shifts";
        smartCardPage.clickLinkOnSmartCardByName(linkName);
        SimpleUtils.assertOnFail("Open shifts not loaed Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
        List<String> claimShift = new ArrayList<>(Arrays.asList("Claim Shift"));
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
        loginPage.logOut();

        // 4.Login with SM to check activity
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
        activityPage.verifyActivityOfShiftOffer(teamMemberName,location);
        activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());
    }


    private void verifyNonHomeManagerDoesNotNeedToApproveShift (Boolean isOP, String option, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String teamMemberName = profileNewUIPage.getNickNameFromProfile();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        String fileName = "UsersCredentials.json";
        fileName = MyThreadLocal.getEnterprise() + fileName;
        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
        Object[][] credentials = userCredentials.get(AccessRoles.StoreManagerOtherLocation1.getValue());
        String nonHomeLocation = String.valueOf(credentials[0][2]);
        if (isOP) {
            // 1. Go to OP-> Schedule Collaboration -> Open Shifts -> enable -- Is approval required by Manager when an employee claims an Open Shift in a home location?
            OpsPortalLocationsPage opsPortalLocationsPage = (OpsPortalLocationsPage) pageFactory.createOpsPortalLocationsPage();
            opsPortalLocationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.goToTemplateDetailsPage("Schedule Collaboration");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.enableOrDisableApproveShiftInHomeLocationSetting(option);
            configurationPage.publishNowTheTemplate();
            switchToConsoleWindow();
        } else {

        }

        // 2.admin create one manual open shift and assign to specific TM
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        //Go to TM's home location, to generate and publish schedule if current week is not generated
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(!isActiveWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
        createSchedulePage.publishActiveSchedule();

        //Go to TM's non-home location, to generate and publish schedule if current week is not generated
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(nonHomeLocation);
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        //Go to TM's home location, to generate and publish schedule if current week is not generated
        scheduleCommonPage.navigateToNextWeek();
        isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(!isActiveWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
        scheduleMainPage.saveSchedule();
        String workRole = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(1);
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(teamMemberName);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        loginPage.logOut();

        // 3.Login with the TM to claim the shift
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        dashboardPage.goToTodayForNewUI();
        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        String cardName = "WANT MORE HOURS?";
        SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
        String linkName = "View Shifts";
        smartCardPage.clickLinkOnSmartCardByName(linkName);
        SimpleUtils.assertOnFail("Open shifts not loaed Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
        List<String> claimShift = new ArrayList<>(Arrays.asList("Claim Shift"));
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);

        SimpleUtils.assertOnFail("", scheduleShiftTablePage.getAvailableShiftsInDayView().size()==0, false);
        loginPage.logOut();

        // 4.Login with SM to check activity
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
        activityPage.verifyActivityOfShiftOffer(teamMemberName,location);
        activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());

        //Go to schedule and check the shift
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();

        //Check the shift is not assigned TM
        List<WebElement> shiftsOfTuesday = scheduleShiftTablePage.getOneDayShiftByName(0, teamMemberName);
        SimpleUtils.assertOnFail("The shift should not been assigned! ",shiftsOfTuesday.size()==0, false);

        loginPage.logOut();

        // 4.Login with SM to check activity
        loginAsDifferentRole(AccessRoles.StoreManagerOtherLocation1.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
        activityPage.verifyActivityOfShiftOffer(teamMemberName,nonHomeLocation);
        activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());

        //Go to schedule and check the shift
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();

        //Check the shift is assigned TM
        shiftsOfTuesday = scheduleShiftTablePage.getOneDayShiftByName(0, teamMemberName);
        SimpleUtils.assertOnFail("The shift should not been assigned! ",shiftsOfTuesday.size()> 0, false);
    }



    private void verifyManagerDoesNotNeedToApproveShift (Boolean isOP, String option, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String teamMemberName = profileNewUIPage.getNickNameFromProfile();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

        if (isOP) {
            // 1. Go to OP-> Schedule Collaboration -> Open Shifts -> enable -- Is approval required by Manager when an employee claims an Open Shift in a home location?
            OpsPortalLocationsPage opsPortalLocationsPage = (OpsPortalLocationsPage) pageFactory.createOpsPortalLocationsPage();
            opsPortalLocationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.goToTemplateDetailsPage("Schedule Collaboration");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.enableOrDisableApproveShiftInHomeLocationSetting(option);
            configurationPage.publishNowTheTemplate();
            switchToConsoleWindow();
        } else {
            // 1.Checking configuration in controls
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
            //String selectedOption = controlsNewUIPage.getIsApprovalByManagerRequiredWhenEmployeeClaimsOpenShiftSelectedOption();
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
        }

        // 2.admin create one manual open shift and assign to specific TM

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        //to generate schedule  if current week is not generated
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(isActiveWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        if (isOP)
            newShiftPage.selectWorkRole(opWorkRole);
        else
            newShiftPage.selectWorkRole(controlWorkRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(teamMemberName);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        loginPage.logOut();

        // 3.Login with the TM to claim the shift
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        int i=0;
        while (!scheduleShiftTablePage.areShiftsPresent() && i < 5) {
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            String cardName = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
            String linkName = "View Shifts";
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            Thread.sleep(10000);
            i++;
        }
        SimpleUtils.assertOnFail("Open shifts not load Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
        List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
        Thread.sleep(5000);
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimSuccessMessage);
        loginPage.logOut();

        // 4.Login with SM to check activity
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
        activityPage.verifyActivityOfShiftOffer(teamMemberName,location);
        SimpleUtils.assertOnFail("Shouldn't load Approval and Reject buttons!", !activityPage.isApproveRejectBtnsLoaded(0), false);
    }

    private void verifyNonHomeManagerNeedToApproveShift(Boolean isOP, String option, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String teamMemberName = profileNewUIPage.getNickNameFromProfile();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

        //Get non-home location name
        String fileName = "UsersCredentials.json";
        Object[][] credentials = null;
        fileName = MyThreadLocal.getEnterprise() + fileName;
        String className = getCurrentClassName();
        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
        String roleName = AccessRoles.StoreManagerOtherLocation1.getValue();
        if (userCredentials.containsKey(roleName + "Of" + className)) {
            credentials = userCredentials.get(roleName + "Of" + className);
        } else {
            credentials = userCredentials.get(roleName);
        }
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        String nonHomeLocationName = String.valueOf(credentials[0][2]);

        if (isOP){
            // 1. Go to OP-> Schedule Collaboration -> Open Shifts -> enable -- Is approval required by Manager when an employee claims an Open Shift in a home location?
            OpsPortalLocationsPage opsPortalLocationsPage = (OpsPortalLocationsPage) pageFactory.createOpsPortalLocationsPage();
            opsPortalLocationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.goToTemplateDetailsPage("Schedule Collaboration");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.enableOrDisableApproveShiftInNonHomeLocationSetting(option);
            configurationPage.publishNowTheTemplate();
            switchToConsoleWindow();
        } else {
            //Set the setting to "Non-home location only" for the SM
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(nonHomeLocationName);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
            SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
            controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
        }

        //Go to TM's home location, to generate and publish schedule if current week is not generated
        dashboardPage.clickOnDashboardConsoleMenu();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(!isActiveWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();

        // Admin create one manual open shift and assign to the TM from other location
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(nonHomeLocationName);
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();
        isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(isActiveWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(1);
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        if (isOP)
            newShiftPage.selectWorkRole(opWorkRole);
        else
            newShiftPage.selectWorkRole(controlWorkRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(teamMemberName);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        Thread.sleep(10000);
        loginPage.logOut();

        // 3.Login with the TM to claim the shift
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        int i=0;
        while (!scheduleShiftTablePage.areShiftsPresent() && i < 5) {
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            String cardName = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
            String linkName = "View Shifts";
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            Thread.sleep(10000);
            i++;
        }
        SimpleUtils.assertOnFail("Open shifts not load Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
        Thread.sleep(10000);
        List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
        loginPage.logOut();

        // 4.Login with TM's home location SM to check activity
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
        activityPage.verifyActivityOfShiftOffer(teamMemberName,nonHomeLocationName);
        activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());
        activityPage.verifyClickOnActivityCloseButton();

        //Go to schedule and check the shift
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();
        //Check the shift is not assigned TM
        Thread.sleep(5000);
        List<WebElement> shiftsOfTM = scheduleShiftTablePage.getOneDayShiftByName(0, teamMemberName);
        SimpleUtils.assertOnFail("The shift should not been assigned! ",shiftsOfTM.size()==0, false);
        loginPage.logOut();

        // 4.Login with TM's non-home SM to check activity
        loginAsDifferentRole(AccessRoles.StoreManagerOtherLocation1.getValue());
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
        activityPage.verifyActivityOfShiftOffer(teamMemberName,nonHomeLocationName);
        activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());
        activityPage.verifyClickOnActivityCloseButton();
        //Go to schedule and check the shift
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();

        //Check the shift is assigned TM
        Thread.sleep(5000);
        shiftsOfTM = scheduleShiftTablePage.getOneDayShiftByName(0, teamMemberName);
        SimpleUtils.assertOnFail("The shift should not been assigned! ",shiftsOfTM.size()> 0, false);
    }
}
