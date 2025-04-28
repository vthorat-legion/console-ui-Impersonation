package com.legion.tests.core;

import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
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
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class CopyScheduleWithoutAssignmentsTest extends TestBase {

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
    @TestName(description = "Verify the resulting schedules should all be created as Open Shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheResultingSchedulesShouldAllBeCreatedAsOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            changeConvertToOpenShiftsSettings("Yes, all unassigned shifts", location);
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();

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
            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            createSchedulePage.publishActiveSchedule();
            HashMap<String, Float> scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            ArrayList<WebElement> allShiftsInFirstSchedule = scheduleShiftTablePage.getAllAvailableShiftsInWeekView();
            String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
            if (firstWeekInfo.length() > 11) {
                firstWeekInfo = firstWeekInfo.trim().substring(10);
            }
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();

            //"Only copy shifts" toggle is available
            SimpleUtils.assertOnFail("The Only copy shifts switch should display! ",
                    createSchedulePage.checkOnlyCopyShiftsSwitchDisplayOrNot(), false);
            //"Only copy shifts" toggle should be off
            SimpleUtils.assertOnFail("The Only copy shifts switch should be off! ",
                    !createSchedulePage.checkOnlyCopyPartialAssignmentSwitchEnableOrNot(), false);
            //Toggle "Only copy shifts" will not show
            createSchedulePage.selectWhichWeekToCopyFrom("SUGGESTED");
            SimpleUtils.assertOnFail("The Only copy shifts switch should not display! ",
                    !createSchedulePage.checkOnlyCopyShiftsSwitchDisplayOrNot(), false);
            //"Only copy shifts" toggle should be on
            createSchedulePage.selectWhichWeekToCopyFrom(firstWeekInfo);
            createSchedulePage.turnOnOrTurnOffOnlyCopyShiftsSwitch(true);
            SimpleUtils.assertOnFail("The Only copy shifts switch should be on! ",
                    createSchedulePage.checkOnlyCopyShiftsSwitchEnableOrNot(), false);
            createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();

            HashMap<String, Float> scheduleHoursForSecondSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            ArrayList<WebElement> allShiftsInSecondSchedule = scheduleShiftTablePage.getAllAvailableShiftsInWeekView();
            List<WebElement> allOpenShifts = scheduleShiftTablePage.getAllShiftsOfOneTM("open");

            //All the shifts should be open shifts
            SimpleUtils.assertOnFail("All shifts in first schedule should been copied to second schedule! ",
                    allShiftsInFirstSchedule.size() == allShiftsInSecondSchedule.size(), false);
            SimpleUtils.assertOnFail("All shifts in second schedule are open shifts! ",
                    allShiftsInSecondSchedule.size() == allOpenShifts.size(), false);
            //Scheduled hour should be consistent with the copied schedule
            SimpleUtils.assertOnFail("The scheduled hour should be consistent with the copied schedule! " +
                            "The origin schedule hour is: "+ scheduleHoursForFirstSchedule.get("scheduledHours")
                            +" The copy schedule hour is: "+scheduleHoursForSecondSchedule.get("scheduledHours"),
                    scheduleHoursForFirstSchedule.get("scheduledHours").equals(scheduleHoursForSecondSchedule.get("scheduledHours")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the resulting schedules should all be created as Open Shifts when setting is 'No, keep as unassigned'")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheResultingSchedulesWhenSettingIsKeepAsUnassignedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            changeConvertToOpenShiftsSettings("No, keep as unassigned", location);
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();

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
            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            createSchedulePage.publishActiveSchedule();
            HashMap<String, Float> scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            ArrayList<WebElement> allShiftsInFirstSchedule = scheduleShiftTablePage.getAllAvailableShiftsInWeekView();
            String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
            if (firstWeekInfo.length() > 11) {
                firstWeekInfo = firstWeekInfo.trim().substring(10);
            }
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();

            //"Only copy shifts" toggle should be on
            createSchedulePage.selectWhichWeekToCopyFrom(firstWeekInfo);
            createSchedulePage.turnOnOrTurnOffOnlyCopyShiftsSwitch(true);
            SimpleUtils.assertOnFail("The Only copy shifts switch should be on! ",
                    createSchedulePage.checkOnlyCopyShiftsSwitchEnableOrNot(), false);
            createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();

            HashMap<String, Float> scheduleHoursForSecondSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            ArrayList<WebElement> allShiftsInSecondSchedule = scheduleShiftTablePage.getAllAvailableShiftsInWeekView();
            List<WebElement> allOpenShifts = scheduleShiftTablePage.getAllShiftsOfOneTM("open");

            //All the shifts should be open shifts
            SimpleUtils.assertOnFail("All shifts in first schedule should been copied to second schedule! ",
                    allShiftsInFirstSchedule.size() == allShiftsInSecondSchedule.size(), false);
            SimpleUtils.assertOnFail("All shifts in second schedule are open shifts! ",
                    allShiftsInSecondSchedule.size() == allOpenShifts.size(), false);
            //Scheduled hour should be consistent with the copied schedule
            SimpleUtils.assertOnFail("The scheduled hour should be consistent with the copied schedule! " +
                            "The origin schedule hour is: "+ scheduleHoursForFirstSchedule.get("scheduledHours")
                            +" The copy schedule hour is: "+scheduleHoursForSecondSchedule.get("scheduledHours"),
                    scheduleHoursForFirstSchedule.get("scheduledHours").equals(scheduleHoursForSecondSchedule.get("scheduledHours")), false);
            changeConvertToOpenShiftsSettings("Yes, all unassigned shifts", location);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }
    private void changeConvertToOpenShiftsSettings(String option, String location) throws Exception {
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
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        configurationPage.updateConvertUnassignedShiftsToOpenWhenCreatingScheduleSettingOption(option);
        configurationPage.updateConvertUnassignedShiftsToOpenWhenCopyingScheduleSettingOption(option);
        configurationPage.publishNowTheTemplate();
        Thread.sleep(3000);
        switchToConsoleWindow();
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify 'Outside Operating hours' violation should show if the day is closed")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheResultingSchedulesWhenDayIsClosedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();

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
            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            createSchedulePage.publishActiveSchedule();
            HashMap<String, Float> scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            ArrayList<WebElement> allShiftsInFirstSchedule = scheduleShiftTablePage.getAllAvailableShiftsInWeekView();
            String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
            if (firstWeekInfo.length() > 11) {
                firstWeekInfo = firstWeekInfo.trim().substring(10);
                if (firstWeekInfo.contains("-")) {
                    String[] temp = firstWeekInfo.split("-");
                    if (temp.length == 2 && temp[0].contains(" ") && temp[1].contains(" ")) {
                        firstWeekInfo = temp[0].trim().split(" ")[0] + " " + (temp[0].trim().split(" ")[1].length() == 1 ? "0" + temp[0].trim().split(" ")[1] : temp[0].trim().split(" ")[1])
                                + " - " + temp[1].trim().split(" ")[0] + " " + (temp[1].trim().split(" ")[1].length() == 1 ? "0" + temp[1].trim().split(" ")[1] : temp[1].trim().split(" ")[1]);
                    }
                }
            }
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.clickCreateScheduleBtn();
            List<String> toCloseDays = new ArrayList<>();
            toCloseDays.add("Sunday");
//            createSchedulePage.editOperatingHoursWithGivingPrameters("Sunday", "08:00AM", "08:00PM");
//            createSchedulePage.editOperatingHoursWithGivingPrameters("Monday", "08:00AM", "08:00PM");
//            createSchedulePage.editOperatingHoursWithGivingPrameters("Tuesday", "08:00AM", "08:00PM");
//            createSchedulePage.editOperatingHoursWithGivingPrameters("Wednesday", "08:00AM", "08:00PM");
//            createSchedulePage.editOperatingHoursWithGivingPrameters("Thursday", "08:00AM", "08:00PM");
//            createSchedulePage.editOperatingHoursWithGivingPrameters("Friday", "08:00AM", "08:00PM");
            createSchedulePage.editTheOperatingHoursForLGInPopupWinodw(toCloseDays);
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();

            //"Only copy shifts" toggle should be on
            createSchedulePage.selectWhichWeekToCopyFrom(firstWeekInfo);
            createSchedulePage.turnOnOrTurnOffOnlyCopyShiftsSwitch(true);
            SimpleUtils.assertOnFail("The Only copy shifts switch should be on! ",
                    createSchedulePage.checkOnlyCopyShiftsSwitchEnableOrNot(), false);
            createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();

            HashMap<String, Float> scheduleHoursForSecondSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            ArrayList<WebElement> allShiftsInSecondSchedule = scheduleShiftTablePage.getAllAvailableShiftsInWeekView();
            List<WebElement> allOpenShifts = scheduleShiftTablePage.getAllShiftsOfOneTM("open");

            //All the shifts should be open shifts
            SimpleUtils.assertOnFail("All shifts in first schedule should been copied to second schedule! ",
                    allShiftsInFirstSchedule.size() == allShiftsInSecondSchedule.size(), false);
            SimpleUtils.assertOnFail("All shifts in second schedule are open shifts! ",
                    allShiftsInSecondSchedule.size() == allOpenShifts.size(), false);
            //Scheduled hour should be consistent with the copied schedule
            SimpleUtils.assertOnFail("The scheduled hour should be consistent with the copied schedule! " +
                            "The origin schedule hour is: "+ scheduleHoursForFirstSchedule.get("scheduledHours")
                            +" The copy schedule hour is: "+scheduleHoursForSecondSchedule.get("scheduledHours"),
                    scheduleHoursForFirstSchedule.get("scheduledHours").equals(scheduleHoursForSecondSchedule.get("scheduledHours")), false);
            // "Outside Operating hours" violation should show on the shifts on the closed day
            int i;
            for (i = 0; i< 7; i++) {
                String weekDay = scheduleShiftTablePage.getWeekDayTextByIndex(i);
                if (weekDay.equalsIgnoreCase("Sun")){
                    break;
                }
            }

            List<WebElement> shiftsOnClosed = scheduleShiftTablePage.getOneDayShiftByName(i, "open");
            for (WebElement shift: shiftsOnClosed) {
                List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shift);
                SimpleUtils.assertOnFail("The unassigned violation message display incorrectly in i icon popup! ",
                        complianceMessage.contains("Outside Operating hours"), false);
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }
}
