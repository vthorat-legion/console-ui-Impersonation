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
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class DeactivateTerminateOrTransferTMTest extends TestBase {


    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify setting \"Move existing shifts to Open when transfers occur within the Workforce Sharing Group.\" is available")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheExistingShiftsWhenTransferSettingIsAvailableAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            String districtName = locationSelectorPage.getSelectedUpperFields().get("District");
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            //Click on Configuration tab -> Schedule collaboration tile
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
            configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
//            //Click on one template to view
//            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Schedule Collaboration"), "edit");
//
//            //Observe the setting under Open Shifts section is available
//            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
//            configurationPage.setWFS("Yes");
//            configurationPage.selectWFSGroup(districtName);
//            configurationPage.setMoveExistingShiftWhenTransfer("Yes");
//            configurationPage.publishNowTheTemplate();
//            Thread.sleep(3000);

            //Publish the template, click on the template again to check the setting
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Schedule Collaboration"), "edit");
            SimpleUtils.assertOnFail("The 'Move existing shifts to Open when transfers occur within the Workforce Sharing Group' settings not been setted successfully! ",
                    configurationPage.isMoveExistingShiftWhenTransferSettingEnabled(), false);

            //Click on Configuration tab -> Schedule collaboration tile
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
            String newTemplateName = "TemplateForAutoDeleting";
            configurationPage.deleteTemplate(newTemplateName);
            configurationPage.clearSearchTemplateBox();
            Thread.sleep(3000);

            //Verify the default value of setting "Move existing shifts to Open when transfers occur within the Workforce Sharing Group." is Yes for new template
            configurationPage.createNewTemplate(newTemplateName);
            configurationPage.clickOnSpecifyTemplateName(newTemplateName, "edit");
            SimpleUtils.assertOnFail("The 'Move existing shifts to Open when transfers occur within the Workforce Sharing Group' settings not been setted successfully! ",
                    configurationPage.isMoveExistingShiftWhenTransferSettingEnabled(), false);
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
            configurationPage.deleteTemplate(newTemplateName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify shifts are converted to Open when 'Move Shifts When tranfer' setting is Yes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsAreConvertedToOpenWhenMoveShiftsSettingIsYesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            //Click on Configuration tab -> Schedule collaboration tile
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
            configurationPage.clickOnConfigurationCrad("Schedule Collaboration");

            //Click on the template which is associated to the location to view
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Schedule Collaboration"), "edit");
            //Edit the template
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Click on Yes besides the setting "Move existing shifts to Open when transfers occur within the Workforce Sharing Group"
            configurationPage.setMoveExistingShiftWhenTransfer("Yes");

            //Publish the template, click on the template again to check the setting
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            Thread.sleep(300000);
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            //Get the transfer day
            String activeWeek = scheduleCommonPage.getActiveWeekText();
            List<String> year = scheduleCommonPage.getYearsFromCalendarMonthYearText();
            String[] items = activeWeek.split(" ");
            String transferFromDate = year.get(0)+ " " + items[3] + " " + items[4];

            //Go to schedule page, create the schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            createSchedulePage.publishActiveSchedule();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

            //Select one TM that has shifts
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int i=0;
            while (i< 20 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
                i++;
            }
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            String transferLocation = "";
            int j = 0;
            while (j <20 && (transferLocation.equals("") || transferLocation.equalsIgnoreCase(location))) {
                transferLocation = locationSelectorPage.getOneRandomNameFromUpperFieldDropdownList("Location").split("\n")[0];
                j++;
            }

            //Go to Team page, search this TM, click on it to view profile
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            teamPage.cancelTransfer();
            teamPage.transferTheTeamMemberOnSpecificDay(transferLocation, transferFromDate);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            //Go to Schedule page, check this TM's shifts, This TM's shifts are converted to open start from the transfer date
            SimpleUtils.assertOnFail("The transfered TM: "+ firstNameOfTM1+"'s shifts not been conver to open successfully! ",
                    scheduleShiftTablePage.getShiftsNumberByName(firstNameOfTM1) == 0, false);
            Thread.sleep(5000);
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            teamPage.cancelTransfer();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify shifts are converted to Open when setting is No")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsAreConvertedToOpenWhenMoveShiftsSettingIsNoAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            //Click on Configuration tab -> Schedule collaboration tile
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
            configurationPage.clickOnConfigurationCrad("Schedule Collaboration");

            //Click on the template which is associated to the location to view
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Schedule Collaboration"), "edit");
            //Edit the template
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Click on Yes besides the setting "Move existing shifts to Open when transfers occur within the Workforce Sharing Group"
            configurationPage.setMoveExistingShiftWhenTransfer("No");

            //Publish the template, click on the template again to check the setting
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            Thread.sleep(300000);
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            //Get the transfer day
            String activeWeek = scheduleCommonPage.getActiveWeekText();
            List<String> year = scheduleCommonPage.getYearsFromCalendarMonthYearText();
            String[] items = activeWeek.split(" ");
            String transferFromDate = year.get(0)+ " " + items[3] + " " + items[4];

            //Go to schedule page, create the schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            createSchedulePage.publishActiveSchedule();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

            //Select one TM that has shifts
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int i=0;
            while (i< 20 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeAnotherDistrict();
            String transferLocation = "";
            int j = 0;
            while (j <20 && (transferLocation == "" || transferLocation.equalsIgnoreCase(location))) {
                transferLocation = locationSelectorPage.getOneRandomNameFromUpperFieldDropdownList("Location").split("\n")[0];
            }

            //Go to Team page, search this TM, click on it to view profile
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            teamPage.cancelTransfer();
            teamPage.transferTheTeamMemberOnSpecificDay(transferLocation, transferFromDate);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(5000);
            //Go to Schedule page, check this TM's shifts, This TM's shifts are converted to open start from the transfer date
            SimpleUtils.assertOnFail("The transfered TM: "+firstNameOfTM1 + "'s shifts should been convert to open successfully! ",
                    scheduleShiftTablePage.getShiftsNumberByName(firstNameOfTM1) == 0, false);

            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            teamPage.cancelTransfer();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify shifts are keeping assigned when setting is No")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsAreKeepingAssignedWhenMoveShiftsSettingIsNoAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            //Click on Configuration tab -> Schedule collaboration tile
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
            configurationPage.clickOnConfigurationCrad("Schedule Collaboration");

            //Click on the template which is associated to the location to view
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Schedule Collaboration"), "edit");
            //Edit the template
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Click on No besides the setting "Move existing shifts to Open when transfers occur within the Workforce Sharing Group"
            configurationPage.setMoveExistingShiftWhenTransfer("No");

            //Publish the template, click on the template again to check the setting
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            //Get the transfer day
            String activeWeek = scheduleCommonPage.getActiveWeekText();
            List<String> year = scheduleCommonPage.getYearsFromCalendarMonthYearText();
            String[] items = activeWeek.split(" ");
            String transferFromDate = year.get(0)+ " " + items[3] + " " + items[4];

            //Go to schedule page, create the schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            createSchedulePage.publishActiveSchedule();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

            //Select one TM that has shifts
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int i=0;
            while (i< 20 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            String transferLocation = "";
            int j = 0;
            while (j <20 && (transferLocation == "" || transferLocation.equalsIgnoreCase(location))) {
                transferLocation = locationSelectorPage.getOneRandomNameFromUpperFieldDropdownList("Location").split("\n")[0];
            }

            //Go to Team page, search this TM, click on it to view profile
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            teamPage.cancelTransfer();
            teamPage.transferTheTeamMemberOnSpecificDay(transferLocation, transferFromDate);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(5000);
            //Go to Schedule page, check this TM's shifts, This TM's shifts are converted to open start from the transfer date
            SimpleUtils.assertOnFail("The transfered TM: "+ firstNameOfTM1+"'s shifts should not been converted to open successfully! ",
                    scheduleShiftTablePage.getShiftsNumberByName(firstNameOfTM1) > 0, false);

            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            teamPage.cancelTransfer();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
