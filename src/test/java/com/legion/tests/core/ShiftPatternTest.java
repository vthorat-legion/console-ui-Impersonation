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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShiftPatternTest extends TestBase {

    private DashboardPage dashboardPage;
    private CreateSchedulePage createSchedulePage;
    private ScheduleMainPage scheduleMainPage;
    private ScheduleShiftTablePage scheduleShiftTablePage;
    private ScheduleCommonPage scheduleCommonPage;
    private LocationsPage locationsPage;
    private NewShiftPage newShiftPage;
    private ConfigurationPage configurationPage;
    private ShiftPatternPage shiftPatternPage;

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
            dashboardPage = pageFactory.createConsoleDashboardPage();
            createSchedulePage = pageFactory.createCreateSchedulePage();
            scheduleMainPage = pageFactory.createScheduleMainPage();
            scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleCommonPage = pageFactory.createScheduleCommonPage();
            locationsPage = pageFactory.createOpsPortalLocationsPage();
            newShiftPage = pageFactory.createNewShiftPage();
            configurationPage = pageFactory.createOpsPortalConfigurationPage();
            shiftPatternPage = pageFactory.createConsoleShiftPatternPage();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify creating shift pattern rules from scheduling rules template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCreatingShiftPatternFromSchedulingRulesTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            // Go to Configurations page
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Scheduling Rules");
            configurationPage.isTemplateListPageShow();
            String templateName = "TemplateName-ForAuto";
            configurationPage.archiveOrDeleteTemplate(templateName);
            String workRole = "General Manager";

            // Create new template with the 'New Template' button, there are 2 tabs: Detail and Association
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            // Verify Shift Pattern option is available when click on + button
            //Verify Shift Pattern is clickable
            configurationPage.checkTheEntryOfAddShiftPatternRule();
            // Verify the content on Shift Pattern Details
            // Verify the default value of Role
            shiftPatternPage.verifyTheContentOnShiftPatternDetails(workRole);
            // Verify the mandatory fields
            shiftPatternPage.verifyTheMandatoryFields();
            // Verify can input the value in Name, Description and No. of instances* inputs
            String patternName = "test";
            String patternDescription = "description";
            String patternInstances = "2";
            shiftPatternPage.inputNameDescriptionNInstances(patternName, patternDescription, patternInstances);
            // Verify Select Start Date button is clickable
            // Verify can select one week successfully
            String selectedFirstDayOfWeek = shiftPatternPage.selectTheCurrentWeek();
            // Verify the functionality of + Add Week section
            // Verify the functionality of clicking on Off Week
            shiftPatternPage.selectAddOnOrOffWeek(false);
            // Verify the content on Off Week section
            shiftPatternPage.verifyTheContentOnOffWeekSection();
            // Verify the functionality of option "Auto schedule team members during off weeks"
            shiftPatternPage.checkOrUnCheckAutoSchedule(true);
            // Verify the functionality of each day checkbox
            shiftPatternPage.checkOrUnCheckSpecificDay(true, "Everyday");
            shiftPatternPage.checkOrUnCheckSpecificDay(false, "Everyday");
            shiftPatternPage.checkOrUnCheckSpecificDay(true, "Sunday");
            shiftPatternPage.checkOrUnCheckSpecificDay(false, "Sunday");
            shiftPatternPage.checkOrUnCheckAutoSchedule(false);
            // Verify the functionality of clicking on On Week
            shiftPatternPage.selectAddOnOrOffWeek(true);
            // Verify the content on On Week section
            shiftPatternPage.verifyTheContentOnOnWeekSection();
            // Verify the functionality of expand week icon
            shiftPatternPage.verifyTheFunctionalityOfExpandWeekIcon(2, false);
            shiftPatternPage.verifyTheFunctionalityOfExpandWeekIcon(2, true);
            // Verify the functionality of arrow icon
            shiftPatternPage.verifyTheFunctionalityOfArrowIcon(2, false);
            shiftPatternPage.verifyTheFunctionalityOfArrowIcon(2, true);
            // Verify the functionality of Delete week button
            int previousWeekCount = shiftPatternPage.getWeekCount();
            shiftPatternPage.deleteTheWeek(2);
            int currentWeekCount = shiftPatternPage.getWeekCount();
            if (previousWeekCount - currentWeekCount == 1) {
                SimpleUtils.pass("Delete the week successfully!");
            } else {
                SimpleUtils.fail("Failed to delete the week!", false);
            }
            // Verify the functionality of + Add Shift button
            shiftPatternPage.selectAddOnOrOffWeek(true);
            shiftPatternPage.clickOnAddShiftButton();
            // Verify the content on Create New Shift Window
            shiftPatternPage.verifyTheContentOnCreateNewShiftWindow();
            // Verify the functionality of Cancel button
            shiftPatternPage.clickOnCancelButton();
            // Verify the functionality of Create button without inputting anything
            shiftPatternPage.clickOnAddShiftButton();
            shiftPatternPage.clickOnCreateButton();
            List<String> warnings = shiftPatternPage.getWarningMessages();
            if (warnings.contains("At least one day should be selected") && warnings.contains("Field should not be empty")) {
                SimpleUtils.pass("Mandatory fields should be entered when clicking Create button!");
            } else {
                SimpleUtils.fail("There is no warning messages when nothing inputted!", false);
            }
            // Verify the work role shows on the Create New Shift window
            shiftPatternPage.verifyWorkRoleNameShows(workRole);
            // Verify the functionality of Shift Name/Description/Shift Notes inputs
            String shiftName = "Shift Name";
            String description = "Description";
            String shiftNotes = "Shift Notes";
            shiftPatternPage.inputShiftNameDescriptionNShiftNotes(shiftName, description, shiftNotes);
            // Verify the functionality of selecting work days
            List<String> workDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday"));
            List<String> selectedDays = shiftPatternPage.selectWorkDays(workDays);
            // Verify the functionality of Start Time and End Time inputs
            String startTime = "09:00 AM";
            String endTime = "05:00 PM";
            shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
            shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
            // Verify Start Time should be before End Time
            shiftPatternPage.inputStartOrEndTime("8", "0", "a", false);
            warnings = shiftPatternPage.getWarningMessages();
            if (warnings.contains("Start time should be before End time")) {
                SimpleUtils.pass("Start time should be before End time message shows!");
            } else {
                SimpleUtils.fail("Start time should be before End time failed to show!", false);
            }
            shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
            // Verify the functionality of Add meal break button
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(true);
            // Verify the shift offset and duration cannot be empty
            shiftPatternPage.clickOnCreateButton();
            warnings = shiftPatternPage.getBreakWarnings();
            if (warnings.contains("Start Offset should not be empty") && warnings.contains("Break Duration should not be empty")) {
                SimpleUtils.pass("Shift offset and break duration warning show correctly!");
            } else {
                SimpleUtils.fail("Shift offset and break duration warnings failed to show!", false);
            }
            // Verify the functionality of x button in meal break section
            shiftPatternPage.deleteTheBreakByNumber(true, 1);
            // Verify the functionality of Add rest break button
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(false);
            // Verify the shift offset and duration cannot be empty
            shiftPatternPage.clickOnCreateButton();
            warnings = shiftPatternPage.getBreakWarnings();
            if (warnings.contains("Start Offset should not be empty") && warnings.contains("Break Duration should not be empty")) {
                SimpleUtils.pass("Shift offset and break duration warning show correctly!");
            } else {
                SimpleUtils.fail("Shift offset and break duration warnings failed to show!", false);
            }
            // Verify the functionality of Add rest break button
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(false);
            // Verify the shift offset and duration cannot be empty
            shiftPatternPage.clickOnCreateButton();
            warnings = shiftPatternPage.getBreakWarnings();
            if (warnings.contains("Start Offset should not be empty") && warnings.contains("Break Duration should not be empty")) {
                SimpleUtils.pass("Shift offset and break duration warning show correctly!");
            } else {
                SimpleUtils.fail("Shift offset and break duration warnings failed to show!", false);
            }
            // Verify the functionality of x button in rest break section
            shiftPatternPage.deleteTheBreakByNumber(false, 1);
            // Verify can input the shift offset and break duration
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(false);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(5, 5, 1, true);
            // Verify the warning message "Start Offset must be in 5 minute increments" in meal break section
            shiftPatternPage.deleteTheBreakByNumber(true, 1);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(1, 1, 1, true);
            warnings = shiftPatternPage.getBreakWarnings();
            if (warnings.contains("Start Offset must be in 5 minute increments") && warnings.contains("Break Duration must be in 5 minute increments")) {
                SimpleUtils.pass("Start Offset and Break Duration warning shows!");
            } else {
                SimpleUtils.fail("Start Offset and break duration warnings failed to show!", false);
            }
            // Verify the warning message "Break cannot start after the end of the shift" in meal break section
            // Verify the warning message "Break cannot go beyond the shift" in meal break section
            shiftPatternPage.deleteTheBreakByNumber(true, 1);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(480, 10, 1, true);
            warnings = shiftPatternPage.getBreakWarnings();
            if (warnings.contains("Break cannot start after the end of the shift") && warnings.contains("Break cannot go beyond the shift")) {
                SimpleUtils.pass("Break cannot start after the end of the shift and cannot go beyond the shift warning shows!");
            } else {
                SimpleUtils.fail("Break cannot start after the end of the shift and cannot go beyond the shift failed to show!", false);
            }
            // Verify the warning message "Break cannot start after the end of the shift" in rest break section
            // Verify the warning message "Break cannot go beyond the shift" in rest break section
            shiftPatternPage.deleteTheBreakByNumber(false, 1);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(480, 10, 1, false);
            warnings = shiftPatternPage.getBreakWarnings();
            if (warnings.contains("Break cannot start after the end of the shift") && warnings.contains("Break cannot go beyond the shift")) {
                SimpleUtils.pass("Break cannot start after the end of the shift and cannot go beyond the shift warning shows!");
            } else {
                SimpleUtils.fail("Break cannot start after the end of the shift and cannot go beyond the shift warnings failed to show!", false);
            }
            // Verify the warning message "Break is overlapping with another one" will show in rest break section
            shiftPatternPage.deleteTheBreakByNumber(true, 1);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(60, 30, 1, true);
            shiftPatternPage.deleteTheBreakByNumber(false, 1);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(60, 15, 1, false);
            warnings = shiftPatternPage.getBreakWarnings();
            if (warnings.contains("Break is overlapping with another one")) {
                SimpleUtils.pass("Break is overlapping with another one warning shows!");
            } else {
                SimpleUtils.fail("Break is overlapping with another one warnings failed to show!", false);
            }
            // Verify no warning message shows when the setting is correct
            shiftPatternPage.deleteTheBreakByNumber(true, 1);
            int mealStartOffset = 60;
            int mealDuration = 30;
            int restStartOffset = 180;
            int restDuration = 15;
            shiftPatternPage.inputShiftOffsetAndBreakDuration(mealStartOffset, mealDuration, 1, true);
            shiftPatternPage.deleteTheBreakByNumber(false, 1);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(restStartOffset, restDuration, 1, false);
            warnings = shiftPatternPage.getBreakWarnings();
            SimpleUtils.assertOnFail("The warning message still shows!", warnings.size() == 0, false);
            // Verify the functionality of Create button
            shiftPatternPage.clickOnCreateButton();
            // Verify the content on week section after creating the shifts
            String expectedContent = "Shifts are from " + (startTime.startsWith("0") ? startTime.substring(1, startTime.length()) : startTime)
                    +" to " + (endTime.startsWith("0") ? endTime.substring(1, startTime.length()) : endTime) + " and repeat on " +
                    selectedDays.toString().substring(1, selectedDays.toString().length() - 1);
            System.out.println(expectedContent);
            System.out.println(shiftPatternPage.getOnWeekContentDetails());
            SimpleUtils.assertOnFail("The content is incorrect!", expectedContent.equalsIgnoreCase(shiftPatternPage.getOnWeekContentDetails()), false);
            // Verify the functionality of pencil icon
            shiftPatternPage.clickOnPencilIcon(2);
            SimpleUtils.assertOnFail("Create New Shift window is not loaded!", shiftPatternPage.isCreateNewShiftWindowLoaded(), false);
            // Verify the edits should persist
            shiftPatternPage.verifyTheEditedValuePersist(shiftName, description, startTime, endTime, workDays, mealStartOffset, mealDuration,
                    restStartOffset, restDuration, shiftNotes);
            // Verify the content in week section when selecting more days
            workDays.add("Friday");
            selectedDays = shiftPatternPage.selectWorkDays(workDays);
            shiftPatternPage.clickOnCreateButton();
            expectedContent = "Shifts are from " + (startTime.startsWith("0") ? startTime.substring(1, startTime.length()) : startTime)
                    +" to " + (endTime.startsWith("0") ? endTime.substring(1, startTime.length()) : endTime) + " and repeat on " +
                    selectedDays.toString().substring(1, selectedDays.toString().length() - 1);
            System.out.println(expectedContent);
            System.out.println(shiftPatternPage.getOnWeekContentDetails());
            SimpleUtils.assertOnFail("The content is incorrect!", expectedContent.equalsIgnoreCase(shiftPatternPage.getOnWeekContentDetails()), false);
            // Verify the count of the shifts increased correctly
            String numberOfShifts = shiftPatternPage.getTheNumberOfTheShifts(2);
            int count2 = Integer.valueOf(numberOfShifts.split(" ")[0]);
            SimpleUtils.assertOnFail("The number of the shifts is incorrect!", "6 Shifts".equalsIgnoreCase(numberOfShifts), false);
            // Verify the functionality of x button
            shiftPatternPage.clickOnDeleteBtnToDelCreatedShifts(2);
            shiftPatternPage.clickOnAddShiftButton();
            shiftPatternPage.inputShiftNameDescriptionNShiftNotes(shiftName, description, shiftNotes);
            selectedDays = shiftPatternPage.selectWorkDays(workDays);
            shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
            shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(true);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(mealStartOffset, mealDuration, 1, true);
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(false);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(restStartOffset, restDuration, 1, false);
            shiftPatternPage.clickOnCreateButton();
            // Verify the title of the staffing rule after clicking on check button
            configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
            List<String> rules = configurationPage.getStaffingRules();
            String expectedRule = patternName + ". " + workRole + ", 2 weeks, Off/On, " + patternInstances + " instances, start date " + selectedFirstDayOfWeek;
            SimpleUtils.assertOnFail("The title of the rule is incorrect!", rules.get(0).contains(expectedRule), false);
            // Verify the shift pattern rule is saved successfully
            configurationPage.clickOnSaveButtonOnScheduleRulesListPage();
            configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("Save As Draft");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify creating shift pattern rules from locaiton level template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCreatingShiftPatternFromLocationLevelTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);               ;
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            String workRole = "Event Manager";

            // Verify can click on Edit button from Scheduling Rules section
            locationsPage.clickActionsForTemplate("Scheduling Rules", "Edit");
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddShiftPatternRule();
            String patternName = "test";
            String patternDescription = "description";
            String patternInstances = "1";
            shiftPatternPage.inputNameDescriptionNInstances(patternName, patternDescription, patternInstances);
            String selectedFirstDayOfWeek = shiftPatternPage.selectTheCurrentWeek();
            // Verify the work role should in role input
            shiftPatternPage.selectAddOnOrOffWeek(true);
            shiftPatternPage.clickOnAddShiftButton();
            shiftPatternPage.verifyWorkRoleNameShows(workRole);
            // Verify can create the shift pattern rule
            List<String> workDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday"));
            List<String> selectedDays = shiftPatternPage.selectWorkDays(workDays);
            shiftPatternPage.inputStartOrEndTime("9", "0", "a", true);
            shiftPatternPage.inputStartOrEndTime("5", "0", "p", false);
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(true);
            int mealStartOffset = 60;
            int mealDuration = 30;
            int restStartOffset = 180;
            int restDuration = 15;
            shiftPatternPage.inputShiftOffsetAndBreakDuration(mealStartOffset, mealDuration, 1, true);
            shiftPatternPage.clickOnAddMealOrRestBreakBtn(false);
            shiftPatternPage.inputShiftOffsetAndBreakDuration(restStartOffset, restDuration, 1, false);
            shiftPatternPage.clickOnCreateButton();
            configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
            List<String> rules = configurationPage.getStaffingRules();
            String expectedRule = patternName + ". " + workRole + ", 1 weeks, On, " + patternInstances + " instances, start date " + selectedFirstDayOfWeek;
            System.out.println(rules.get(0));
            System.out.println(expectedRule);
            SimpleUtils.assertOnFail("The title of the rule is incorrect!", rules.get(0).contains(expectedRule), false);
            configurationPage.clickOnSaveButtonOnScheduleRulesListPage();
            // Verify can save the changes from location level
            locationsPage.clickOnSaveButton();
            // Verify the shift pattern rule is saved successfully
            locationsPage.clickActionsForTemplate("Scheduling Rules", "View");
            configurationPage.selectWorkRoleToEdit(workRole);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
