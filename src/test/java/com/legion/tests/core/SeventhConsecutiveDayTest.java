package com.legion.tests.core;

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
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;
import static com.legion.utils.MyThreadLocal.loc;

public class SeventhConsecutiveDayTest extends TestBase {
    private static String controlEnterprice = "Vailqacn_Enterprise";
    private static String opEnterprice = "CinemarkWkdy_Enterprise";
    public enum consecutiveOTDaysCount{
        First("1st"),
        Second("2nd"),
        Third("rd"),
        Forth("4th"),
        Fifth("5th"),
        Sixth("6th"),
        Seventh("7th");
        private final String value;
        consecutiveOTDaysCount(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum consecutiveOTOptions{
        Always("always"),
        WorkweekLessThan40Hrs("if the workweek > 40 hrs");
        private final String value;
        consecutiveOTOptions(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

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
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the consecutive OT setting")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyConsecutiveOTSettingAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            // Checking configuration in controls

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
            configurationPage.clickOnConfigurationCrad("Compliance");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.turnOnOrTurnOffDailyOTToggle(false);
            compliancePage.turnOnOrTurnOffDailyDTToggle(false);
            compliancePage.turnOnOrTurnOffWeelyDTToggle(false);
            compliancePage.turnOnOrTurnOffConsecutiveDTToggle(false);
            compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
            compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount.Sixth.getValue(),
                    consecutiveOTOptions.Always.getValue(), true);
            compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount.Fifth.getValue(),
                    consecutiveOTOptions.WorkweekLessThan40Hrs.getValue(), false);
            compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount.Seventh.getValue(),
                    consecutiveOTOptions.Always.getValue(), true);
            configurationPage.publishNowTheTemplate();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the violation when set consecutive OT setting as always")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViolationWhenSetConsecutiveOTSettingAsAlwaysAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
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
            String startTime = "11am";
            String endTime = "2pm";
            String consecutiveOTViolation = "3 hrs daily overtime";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, true);
            startTime = "9am";
            endTime = "3pm";
            consecutiveOTViolation = "5.5 hrs daily overtime";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, true);
            startTime = "7am";
            endTime = "5pm";
            consecutiveOTViolation = "9.5 hrs daily overtime";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, true);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the violation set consecutive OT setting as if the workweek less than 40 hrs")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViolationWhenSetConsecutiveOTSettingAsWorkweekLessThan40HrsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.WorkweekLessThan40Hrs.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            String startTime = "11am";
            String endTime = "2pm";
            String consecutiveOTViolation = "";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, false);
            startTime = "9am";
            endTime = "4pm";
            consecutiveOTViolation = "6.5 hrs daily overtime";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, true);
            startTime = "7am";
            endTime = "5pm";
            consecutiveOTViolation = "26.5 hrs daily overtime";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, true);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }

    }


    private void createShiftsForConsecutiveOT (String startTime, String endTime, String consecutiveOTViolation, boolean ifLastDayHasViolation) throws Exception {
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }

        int i = 0;
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
        String firstName = shiftInfo.get(0);
        while (i< 50 && (firstName.equalsIgnoreCase("open") || firstName.equalsIgnoreCase("Unassigned"))) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            firstName  = shiftInfo.get(0);
            i++;
        }
        String workRole = shiftInfo.get(4);
        String lastName = shiftInfo.get(5);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        Thread.sleep(5000);
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(7);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint(endTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(startTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName+ " "+lastName);
        newShiftPage.clickOnOfferOrAssignBtn();
        Thread.sleep(5000);
        scheduleMainPage.saveSchedule();
        for (int j=0; j< 7; j++) {
            List<WebElement> shiftsOfSevenDays = scheduleShiftTablePage.getOneDayShiftByName(j, firstName);
            if (j==6) {
                if (ifLastDayHasViolation) {
                    SimpleUtils.assertOnFail("The consecutive OT Violation display incorrectly, the expected is: "+ consecutiveOTViolation
                                    + " The actual is: "+ scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfSevenDays.get(0)),
                            scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfSevenDays.get(0)).
                                    contains(consecutiveOTViolation), false);
                } else {
                    SimpleUtils.assertOnFail("The consecutive OT Violation display incorrectly, the expected is: "+ consecutiveOTViolation
                                    + " The actual is: "+ scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfSevenDays.get(0)),
                            !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfSevenDays.get(0)).
                                    contains("hrs daily overtime"), false);
                }

            } else {
                SimpleUtils.assertOnFail("The consecutive OT Violation display incorrectly, it should not contains: "+ consecutiveOTViolation,
                        !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfSevenDays.get(0)).
                                contains(consecutiveOTViolation), false);
            }

        }
    }

    private void setConsecutiveOTAndDTSettings (String consecutiveOTDaysCount, String consecutiveOTOptions, String location) throws Exception {
        CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();

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
        configurationPage.clickOnConfigurationCrad("Compliance");
        configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();

        String contentBeforeChange = compliancePage.getConsecutiveOTSettingContent();
        compliancePage.turnOnOrTurnOffDayOTToggle(true);
        compliancePage.turnOnOrTurnOffConsecutiveDTToggle(false);
        compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
        compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount, consecutiveOTOptions, true);
        String contentAfterChange = compliancePage.getConsecutiveOTSettingContent();
        configurationPage.publishNowTheTemplate();
        switchToConsoleWindow();
        if (!contentAfterChange.equals(contentBeforeChange)) {
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            for (int i=0; i< 10; i++) {
                Thread.sleep(60000);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            }
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM cannot receive the auto open shift offer that has consecutive OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMCannotReceiveAutoOpenShiftOfferWithConsecutiveOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            Thread.sleep(5000);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();

            //Create 6 shifts for TM1 on the first 6 consecutive days and save
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.selectWorkRole(workRole);
            Thread.sleep(2000);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create one auto open shift on the 7th day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(6,6,6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(5000);
            //TM will not receive the auto open shift offer on the 7th day
            int i=0;
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            mySchedulePage.selectSchedulFilter("Offered");
            SimpleUtils.assertOnFail("Open shifts should not loaded!",
                    !scheduleShiftTablePage.areShiftsPresent(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the SM cannot approve the auto open shift activity that has consecutive OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySMCannotApproveAutoOpenShiftActivityWithConsecutiveOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            //Get work role
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();

            //Create 7 auto open shifts on the 7 consecutive days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            //TM will not receive the auto open shift offer on the 7th day
            int i=0;
            while (!scheduleShiftTablePage.areShiftsPresent() && i < 5) {
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                String cardName = "WANT MORE HOURS?";
                SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!",
                        smartCardPage.isSpecificSmartCardLoaded(cardName), false);
                String linkName = "View Shifts";
                smartCardPage.clickLinkOnSmartCardByName(linkName);
                Thread.sleep(10000);
                i++;
            }
            SimpleUtils.assertOnFail("Open shifts should be loaded!",
                    scheduleShiftTablePage.areShiftsPresent(), false);
            i = 0;
            while (scheduleShiftTablePage.areShiftsPresent() && i <10) {
                List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
                mySchedulePage.selectOneShiftIsClaimShift(claimShift);
                mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
                mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
                i++;
            }


            // Verify Activity Icon is loaded and approve the cover shift request
            loginAsDifferentRole(AccessRoles.StoreManagerOtherLocation1.getValue());
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);

            i = 0;
            while (scheduleShiftTablePage.areShiftsPresent() && i <10) {
                activityPage.verifyActivityBellIconLoaded();
                activityPage.verifyClickOnActivityIcon();
                activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
                activityPage.verifyActivityOfShiftOffer(firstName,location);
                activityPage.approveOrRejectShiftOfferRequestOnActivity(firstName, ActivityTest.approveRejectAction.Approve.getValue());
                i++;
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM cannot accept the auto open shift offer that has consecutive OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMCannotAcceptAutoOpenShiftActivityWithConsecutiveOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();

            //Create 7 auto open shifts on the 7 consecutive days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            //TM will not receive the auto open shift offer on the 7th day
            int i=0;
            while (!scheduleShiftTablePage.areShiftsPresent() && i < 6) {
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                String cardName = "WANT MORE HOURS?";
                SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!",
                        smartCardPage.isSpecificSmartCardLoaded(cardName), false);
                String linkName = "View Shifts";
                smartCardPage.clickLinkOnSmartCardByName(linkName);
                Thread.sleep(10000);
                i++;
            }
            SimpleUtils.assertOnFail("Open shifts should be loaded!",
                    scheduleShiftTablePage.areShiftsPresent(), false);
            i = 0;
            while (scheduleShiftTablePage.areShiftsPresent() && i <6) {
                List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
                mySchedulePage.selectOneShiftIsClaimShift(claimShift);
                mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
                mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
                i++;
            }


            // Verify Activity Icon is loaded and approve the cover shift request
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);

            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
            activityPage.verifyActivityOfShiftOffer(firstName,location);
            activityPage.approveOrRejectMultipleShiftOfferRequestOnActivity(firstName,
                    ActivityTest.approveRejectAction.Approve.getValue(), 6);

            loginPage.logOut();
            //Login as TM1 again and try to claim the 7th shift offer
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            String cardName = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!",
                    smartCardPage.isSpecificSmartCardLoaded(cardName), false);
            String linkName = "View Shifts";
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            SimpleUtils.assertOnFail("Open shifts should be loaded!",
                    scheduleShiftTablePage.areShiftsPresent(), false);
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            //Error message should display as: We're sorry. You're not eligible to take this shift, as it will trigger overtime.
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.NoLongEligibleTakeShiftErrorMessage);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM cannot accept the manual open shift that has OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMCannotAcceptManualOpenShiftOfferWithConsecutiveOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName1 = tmFullName.split(" ")[0];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();

            //Create 6 shifts for TM1 on the first 6 consecutive days and save
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create one auto open shift on the 7th day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(6,6,6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            int i=0;
            while (!scheduleShiftTablePage.areShiftsPresent() && i < 6) {
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.navigateToNextWeek();
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                String cardName = "WANT MORE HOURS?";
                SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!",
                        smartCardPage.isSpecificSmartCardLoaded(cardName), false);
                String linkName = "View Shifts";
                smartCardPage.clickLinkOnSmartCardByName(linkName);
                Thread.sleep(10000);
                i++;
            }
            SimpleUtils.assertOnFail("Open shifts should be loaded!",
                    scheduleShiftTablePage.areShiftsPresent(), false);
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            //Error message should display as: We're sorry. You're not eligible to take this shift, as it will trigger overtime.
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.NoLongEligibleTakeShiftErrorMessage);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM will not display in the swap request list that has OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMWillNotDisplayInSwapRequestListWhenHasOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName1 = tmFullName.split(" ")[0];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName2 = tmFullName.split(" ")[0];
            String lastName2= tmFullName.split(" ")[1];
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName2);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            Thread.sleep(3000);
            scheduleMainPage.saveSchedule();

            //Create 7 shifts for TM1 on the 7 consecutive days and save
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create one shift for TM2 on the 7th day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(6,6,6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName2+ " "+ lastName2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage.clickOnProfileIconOnDashboard();
            dashboardPage.clickOnSwitchToEmployeeView();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();

            // Validate that swap request smartcard is available to recipient team member
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            mySchedulePage.verifyClickOnAnyShift();
            String request = "Request to Swap Shift";
            String title = "Find Shifts to Swap";
            mySchedulePage.clickTheShiftRequestByName(request);
            SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
            SimpleUtils.assertOnFail("The TM "+ firstName1 +" should not be listed! ",
                    !mySchedulePage.checkIfTMExitsInCoverOrSwapRequestList(firstName1), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM cannot accept the shift swap request that has OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMCannotAcceptTheShiftSwapRequestThatHasOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try {
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
        String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
        String firstName1 = tmFullName.split(" ")[0];
        String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
        tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
        String firstName2 = tmFullName.split(" ")[0];
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        scheduleCommonPage.navigateToNextWeek();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectJobTitleFilterByText(jobTitle);
        String workRole = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName1);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName2);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
        scheduleMainPage.saveSchedule();

        //Create 1 shift for TM1 and 1 for TM2 on the 7th day, confirm these two shifts have no overlapping and save them
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(6,6,6);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName1);
        newShiftPage.clickOnOfferOrAssignBtn();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(6,6,6);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName2);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();

        //Login as TM2, request to swap shift and select the shift of TM1 created on step2
        Thread.sleep(3000);
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        dashboardPage.clickOnProfileIconOnDashboard();
        dashboardPage.clickOnSwitchToEmployeeView();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        List<String> swapCoverRequsts = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
        int index = mySchedulePage.verifyClickOnAnyShift();
        String request = "Request to Swap Shift";
        String title = "Find Shifts to Swap";
        mySchedulePage.clickTheShiftRequestByName(request);
        SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
        mySchedulePage.verifyComparableShiftsAreLoaded();
        mySchedulePage.selectOneTeamMemberToSwap(firstName1);

        //Login as admin, create 6 shifts on the first 6 days for TM1 and save.
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        scheduleCommonPage.navigateToNextWeek();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(6);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();

        //Login as TM1, try to accept the swap request from TM2
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        String smartCard = "SWAP REQUESTS";
        smartCardPage.isSmartCardAvailableByLabel(smartCard);
        // Validate the availability of all swap request shifts in schedule table
        String linkName = "View All";
        smartCardPage.clickLinkOnSmartCardByName(linkName);
        mySchedulePage.verifySwapRequestShiftsLoaded();
        //Error message should display as: We're sorry. You're not eligible to take this shift, as it will trigger overtime.
        mySchedulePage.verifyClickAgreeBtnForSwapWithMessage(Constants.WillTriggerWeeklyOTErrorMessage);
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM will not display in the cover request list that has OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMWillNotDisplayInCoverRequestListWhenHasOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            Thread.sleep(3000);
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName1 = tmFullName.split(" ")[0];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            Thread.sleep(3000);
            tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName2 = tmFullName.split(" ")[0];
            String lastName2 = tmFullName.split(" ")[1];
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            Thread.sleep(3000);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName2);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();

            //Create 7 shifts for TM1 on the 7 consecutive days and save
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create one shift for TM2 on the 7th day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(6,6,6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName2+ " "+ lastName2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage.clickOnProfileIconOnDashboard();
            dashboardPage.clickOnSwitchToEmployeeView();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();

//			List<String> swapCoverRequsts = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            String request = "Request to Cover Shift";
            String title = "Submit Cover Request";
            mySchedulePage.verifyClickOnAnyShift();
            mySchedulePage.clickTheShiftRequestByName(request);
            SimpleUtils.assertOnFail(title + " page not loaded Successfully!",
                    mySchedulePage.isPopupWindowLoaded(title), true);
            mySchedulePage.verifyClickOnSubmitButton();
            int coverRequestsCount = 0;
            int i = 0;
            while (i<10 && coverRequestsCount==0) {
                Thread.sleep(30000);
                String requestName = "View Cover Request Status";
                mySchedulePage.clickTheShiftRequestToClaimShift(requestName, firstName2);
                coverRequestsCount = mySchedulePage.getCountOfCoverOrSwapRequestsInList();
                if (coverRequestsCount == 0) {
                    mySchedulePage.clickCloseDialogButton();
                }
                i++;
            }
            SimpleUtils.assertOnFail("The TM:" + firstName1 + " should not be listed! ",
                    !mySchedulePage.checkIfTMExitsInCoverOrSwapRequestList(firstName1), false);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM cannot accept the shift cover request that has OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMCannotAcceptTheShiftCoverRequestThatHasOTViolationAsTeamMember(String browser, String username, String password, String location) throws Exception {
		try {
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
        String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
        String firstName1 = tmFullName.split(" ")[0];
        String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
        tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
        String firstName2 = tmFullName.split(" ")[0];
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        setConsecutiveOTAndDTSettings(consecutiveOTDaysCount.Seventh.getValue(), consecutiveOTOptions.Always.getValue(), location);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        scheduleCommonPage.navigateToNextWeek();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectJobTitleFilterByText(jobTitle);
        String workRole = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName1);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName2);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
        scheduleMainPage.saveSchedule();

        //Create 1 shift for TM2 on the 7th day and save
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(6, 6, 6);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName2);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();

        //Login as TM2 and request to cover shift, confirm TM1 is display in the cover request list
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        dashboardPage.clickOnProfileIconOnDashboard();
        dashboardPage.clickOnSwitchToEmployeeView();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        String request = "Request to Cover Shift";
        String title = "Submit Cover Request";
        mySchedulePage.verifyClickOnAnyShift();
        mySchedulePage.clickTheShiftRequestByName(request);
        SimpleUtils.assertOnFail(title + " page not loaded Successfully!",
                mySchedulePage.isPopupWindowLoaded(title), true);
        mySchedulePage.verifyClickOnSubmitButton();
        int coverRequestsCount = 0;
        int i = 0;
        while (i<10 && coverRequestsCount==0) {
            Thread.sleep(30000);
            String requestName = "View Cover Request Status";
            mySchedulePage.clickTheShiftRequestToClaimShift(requestName, firstName2);
            coverRequestsCount = mySchedulePage.getCountOfCoverOrSwapRequestsInList();
            if (coverRequestsCount == 0) {
                mySchedulePage.clickCloseDialogButton();
            }
            i++;
        }
        SimpleUtils.assertOnFail("The TM:" + firstName1 + " should be listed! ",
                mySchedulePage.checkIfTMExitsInCoverOrSwapRequestList(firstName1), false);
        mySchedulePage.clickCloseDialogButton();

        //Login as admin, create 6 shifts on the first 6 days for TM1 and save.
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        scheduleCommonPage.navigateToNextWeek();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(6);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();

        //Login as TM1, try to claim the cover request from TM2
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        String smartCard = "WANT MORE HOURS?";
        SimpleUtils.assertOnFail("Smart Card: " + smartCard + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(smartCard), false);
        // Validate the availability of all cover request shifts in schedule table
        String linkName = "View Shifts";;
        smartCardPage.clickLinkOnSmartCardByName(linkName);
        SimpleUtils.assertOnFail("Open shifts not loaded Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
        // Validate the availability of Claim Shift Request popup
        List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
        // Validate the clickability of I Agree button, Error message should display as: We're sorry. You're not eligible to take this shift, as it will trigger overtime.
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.NoLongEligibleTakeShiftErrorMessage);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the 7th consecutive day violation will not display when disable the rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verify7thConsecutiveDayViolationWillNotDisplayWhenDisableTheRuleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
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
            compliancePage.turnOnOrTurnOffConsecutiveDTToggle(false);
            compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(false);
                configurationPage.publishNowTheTemplate();
                switchToConsoleWindow();
                ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
                for (int i=0; i< 10; i++) {
                    Thread.sleep(60000);
                    scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                }

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
            String startTime = "9am";
            String endTime = "4pm";
            String consecutiveOTViolation = "";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, false);
            startTime = "7am";
            endTime = "5pm";
            createShiftsForConsecutiveOT(startTime, endTime, consecutiveOTViolation, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the violation when the shift hour in the 7th consecutive day exceed the doubletime pay hours for 7th consecutive day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViolationWhen7thConsecutiveDayShiftHourExceedDTPayHrsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();

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
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            compliancePage.turnOnOrTurnOffDailyDTToggle(true);
            compliancePage.turnOnOrTurnOffConsecutiveDTToggle(true);
            compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
            compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount.Seventh.getValue(),
                    consecutiveOTOptions.WorkweekLessThan40Hrs.getValue(), true);
                configurationPage.publishNowTheTemplate();
                switchToConsoleWindow();
                ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
                for (int i=0; i< 10; i++) {
                    Thread.sleep(60000);
                    scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                }

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            String firstSixDaysTime = "8am-2pm";
            String seventhConsecutiveDayTime  = "8am-10pm";
            String consecutiveOTViolation1 = "8 hrs daily overtime";
            String consecutiveOTViolation2 = "5.5 hrs daily double overtime";
            List<String> consecutiveOTViolations = new ArrayList<>();
            consecutiveOTViolations.add(consecutiveOTViolation1);
            consecutiveOTViolations.add(consecutiveOTViolation2);
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            createShiftsForConsecutiveOTAndDT(firstSixDaysTime, seventhConsecutiveDayTime, consecutiveOTViolations);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the violation when the shift hour in the 7th consecutive day NOT exceed the doubletime pay hours for 7th consecutive day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViolationWhen7thConsecutiveDayShiftHourNotExceedDTPayHrsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
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
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            compliancePage.turnOnOrTurnOffDailyDTToggle(true);
            compliancePage.turnOnOrTurnOffConsecutiveDTToggle(true);
            compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
            compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount.Seventh.getValue(),
                    consecutiveOTOptions.WorkweekLessThan40Hrs.getValue(), true);
                configurationPage.publishNowTheTemplate();
                switchToConsoleWindow();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            String firstSixDaysTime = "8am-2pm";
            String seventhConsecutiveDayTime  = "8am-4pm";
            String consecutiveOTViolation1 = "7.5 hrs daily overtime";
            List<String> consecutiveOTViolations = new ArrayList<>();
            consecutiveOTViolations.add(consecutiveOTViolation1);
            createShiftsForConsecutiveOTAndDT(firstSixDaysTime, seventhConsecutiveDayTime, consecutiveOTViolations);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the violation of the 7th consecutive day when disable doubletime pay hours for 7th consecutive day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViolationWhenDisable7thConsecutiveDayDTSettingAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();

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
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            compliancePage.turnOnOrTurnOffDailyDTToggle(true);
            compliancePage.turnOnOrTurnOffConsecutiveDTToggle(false);
            compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
            compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount.Seventh.getValue(),
                    consecutiveOTOptions.WorkweekLessThan40Hrs.getValue(), true);
                configurationPage.publishNowTheTemplate();
                switchToConsoleWindow();
                ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
                for (int i=0; i< 10; i++) {
                    Thread.sleep(60000);
                    scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                }

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            String firstSixDaysTime = "8am-2pm";
            String seventhConsecutiveDayTime  = "8am-10pm";
            String consecutiveOTViolation1 = "13.5 hrs daily overtime";
            List<String> consecutiveOTViolations = new ArrayList<>();
            consecutiveOTViolations.add(consecutiveOTViolation1);
            createShiftsForConsecutiveOTAndDT(firstSixDaysTime, seventhConsecutiveDayTime, consecutiveOTViolations);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the violation when the week shifts hour exceed the doubletime pay hours for a single workweek")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViolationWhenShiftHoursExceedWeeklyDTHoursAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();

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
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //weekly OT
            compliancePage.turnOnOrTurnOffDailyDTToggle(true);
            compliancePage.turnOnOrTurnOffWeelyDTToggle(true);
            compliancePage.turnOnOrTurnOffConsecutiveDTToggle(true);
            compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
            compliancePage.editConsecutiveOTSetting(consecutiveOTDaysCount.Seventh.getValue(),
                    consecutiveOTOptions.WorkweekLessThan40Hrs.getValue(), true);
                configurationPage.publishNowTheTemplate();
                switchToConsoleWindow();
                ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
                for (int i=0; i< 10; i++) {
                    Thread.sleep(60000);
                    scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                }

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            String firstSixDaysTime = "8am-6pm";
            String seventhConsecutiveDayTime  = "8am-10pm";
            String consecutiveOTViolation1 = "8 hrs daily overtime";
            String consecutiveOTViolation2 = "5.5 hrs daily double overtime";
            String consecutiveOTViolation3 = "8 hrs weekly overtime";
            String consecutiveOTViolation4 = "9 hrs weekly double overtime";
            List<String> consecutiveOTViolations = new ArrayList<>();
            consecutiveOTViolations.add(consecutiveOTViolation1);
            consecutiveOTViolations.add(consecutiveOTViolation2);
            consecutiveOTViolations.add(consecutiveOTViolation3);
            consecutiveOTViolations.add(consecutiveOTViolation4);
            createShiftsForConsecutiveOTAndDT(firstSixDaysTime, seventhConsecutiveDayTime, consecutiveOTViolations);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    private void createShiftsForConsecutiveOTAndDT (String firstSixDaysTime, String seventhConsecutiveDayTime,
                                                    List<String> consecutiveOTViolations) throws Exception {
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }

        int i = 0;
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
        String firstName = shiftInfo.get(0);
        while (i< 50 && (firstName.equalsIgnoreCase("open") || firstName.equalsIgnoreCase("Unassigned"))) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            firstName  = shiftInfo.get(0);
            i++;
        }
        String workRole = shiftInfo.get(4);

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        Thread.sleep(5000);
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(6);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint(firstSixDaysTime.split("-")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(firstSixDaysTime.split("-")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(6,6,6);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.moveSliderAtCertainPoint(seventhConsecutiveDayTime.split("-")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(seventhConsecutiveDayTime.split("-")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstName);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();

        for (int j=0; j< 7; j++) {
            List<WebElement> shiftsOfSevenDays = scheduleShiftTablePage.getOneDayShiftByName(j, firstName);
            List<String> violations = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfSevenDays.get(0));
            if (j==6) {
                if (consecutiveOTViolations.size() == 1) {
                    SimpleUtils.assertOnFail("The consecutive OT Violation display incorrectly, the violation should not include double overtime violation! "
                                    + " The actual is: "+ violations,
                            !violations.contains("double"), false);
                }
                for (int k= 0; k< consecutiveOTViolations.size(); k++) {
                    SimpleUtils.assertOnFail("The consecutive OT Violation display incorrectly, the expected is: "+ consecutiveOTViolations
                                    + " The actual is: "+ violations,
                            violations.contains(consecutiveOTViolations.get(k)), false);
                }
            } else {
                for (int k= 0; k< consecutiveOTViolations.size(); k++) {
                    if (consecutiveOTViolations.get(k).contains("weekly")) {
                        SimpleUtils.assertOnFail("The consecutive OT Violation display incorrectly, it should not contains: "+ consecutiveOTViolations +" The actual message is: "+violations,
                                violations.contains(consecutiveOTViolations.get(k)), false);
                    } else
                        SimpleUtils.assertOnFail("The consecutive OT Violation display incorrectly, it should not contains: "+ consecutiveOTViolations +" The actual message is: "+violations,
                                !violations.contains(consecutiveOTViolations.get(k)), false);
                }
            }

        }
    }
}
