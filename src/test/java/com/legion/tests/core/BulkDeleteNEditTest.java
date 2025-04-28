package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.pages.core.schedule.ConsoleEditShiftPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.ro.Si;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

public class BulkDeleteNEditTest extends TestBase {
    private DashboardPage dashboardPage;
    private CreateSchedulePage createSchedulePage;
    private ScheduleMainPage scheduleMainPage;
    private ScheduleShiftTablePage scheduleShiftTablePage;
    private ScheduleCommonPage scheduleCommonPage;
    private EditShiftPage editShiftPage;
    private NewShiftPage newShiftPage;
    private ShiftOperatePage shiftOperatePage;
    private ControlsPage controlsPage;
    private ControlsNewUIPage controlsNewUIPage;
    private MySchedulePage mySchedulePage;
    private String action = "Edit";
    private String success = "Success";
    private String error = "Error";

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
            editShiftPage = pageFactory.createEditShiftPage();
            newShiftPage = pageFactory.createNewShiftPage();
            shiftOperatePage = pageFactory.createShiftOperatePage();
            controlsPage = pageFactory.createConsoleControlsPage();
            controlsNewUIPage = pageFactory.createControlsNewUIPage();
            mySchedulePage = pageFactory.createMySchedulePage();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify can delete multiple shifts at a time in week view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDeleteMultipleShiftsInWeekViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            int previousShiftCount = scheduleShiftTablePage.getShiftsCount();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            HashSet<Integer> set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            // Verify action menu will pop up when right clicking on anywhere of the selected shifts
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            // Verify the content on pop up menu
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            // Verify the Delete button on Bulk Action Menu is clickable
            String action = "Delete";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            // Verify the shifts are marked as X after clicking on Delete button
            scheduleShiftTablePage.verifySelectedShiftsAreMarkedWithX(set);
            // Verify the shift which are marked "x" will not be deleted if canceling editing
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            if (scheduleShiftTablePage.getShiftsCount() == previousShiftCount) {
                SimpleUtils.pass("Shifts are not deleted when clicking on Cancel button!");
            } else {
                SimpleUtils.fail("Shifts are deleted when clicking on Cancel button!", false);
            }
            // Verify multiple shifts are deleted
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            scheduleShiftTablePage.verifySelectedShiftsAreMarkedWithX(set);
            scheduleMainPage.saveSchedule();
            Thread.sleep(10000);
            int currentShiftCount = scheduleShiftTablePage.getShiftsCount();
            if (currentShiftCount == previousShiftCount - selectedShiftCount) {
                SimpleUtils.pass("Selected shifts are deleted successfully!");
            } else {
                SimpleUtils.fail("Selected shifts are not deleted successfully!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify can delete multiple shifts at a time in day view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDeleteMultipleShiftsInDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleCommonPage.clickOnDayView();

            int previousShiftCount = scheduleShiftTablePage.getShiftsCount();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            HashSet<Integer> set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            // Verify action menu will pop up when right clicking on anywhere of the selected shifts
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            // Verify the content on pop up menu
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            // Verify the Delete button on Bulk Action Menu is clickable
            String action = "Delete";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            // Verify the shifts are marked as X after clicking on Delete button
            scheduleShiftTablePage.verifySelectedShiftsAreMarkedWithX(set);
            // Verify the shift which are marked "x" will not be deleted if canceling editing
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            if (scheduleShiftTablePage.getShiftsCount() == previousShiftCount) {
                SimpleUtils.pass("Shifts are not deleted when clicking on Cancel button!");
            } else {
                SimpleUtils.fail("Shifts are deleted when clicking on Cancel button!", false);
            }
            // Verify multiple shifts are deleted
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            scheduleShiftTablePage.verifySelectedShiftsAreMarkedWithX(set);
            scheduleMainPage.saveSchedule();
            int currentShiftCount = scheduleShiftTablePage.getShiftsCount();
            if (currentShiftCount == previousShiftCount - selectedShiftCount) {
                SimpleUtils.pass("Selected shifts are deleted successfully!");
            } else {
                SimpleUtils.fail("Selected shifts are not deleted successfully!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content on Multiple Edit Shifts window for regular location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            String workRole = shiftOperatePage.getRandomWorkRole();

            BasePage basePage = new BasePage();
            String activeWeek = basePage.getActiveWeekText();
            String startOfWeek = activeWeek.split(" ")[3] + " " + activeWeek.split(" ")[4];
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            HashSet<Integer> set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            List<String> selectedDays = scheduleShiftTablePage.getSelectedWorkDays(set);
            // Verify action menu will pop up when right clicking on anywhere of the selected shifts
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            // Verify Edit action is visible when right clicking the selected shifts in week view
            // Verify the functionality of Edit button in week view
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly
            editShiftPage.verifySelectedWorkDays(selectedShiftCount, selectedDays);
            // Verify the Location Name shows correctly
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section
            editShiftPage.verifyTheContentOfOptionsSection();
            // Verify the visibility of Clear Edited Fields button
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the two columns show on Shift Details section
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section
            editShiftPage.verifyEditableTypesShowOnShiftDetail();
            // Verify the functionality of x button
            editShiftPage.clickOnXButton();
            SimpleUtils.assertOnFail("Click on X button failed!", !editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the functionality of Cancel button
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnCancelButton();

            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleCommonPage.clickOnDayView();
            String weekDay = basePage.getActiveWeekText();
            String fullWeekDay = SimpleUtils.getFullWeekDayName(weekDay.split(" ")[0].trim());
            selectedDays = new ArrayList<>();
            selectedDays.add(fullWeekDay);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window in day view
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly in day view
            editShiftPage.verifySelectedWorkDays(selectedShiftCount, selectedDays);
            // Verify the Location Name shows correctly in day view
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons in day view
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section in day view
            editShiftPage.verifyTheContentOfOptionsSection();
            // Verify the visibility of Clear Edited Fields button in day view
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the three columns show on Shift Details section in day view
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section in day view
            editShiftPage.verifyEditableTypesShowOnShiftDetail();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of Current column when selecting multiple shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheCurrentColumnOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
            ArrayList<HashMap<String,String>> workRoles = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            String workRole1 = workRoles.get(0).get("optionName");
            String workRole2 = workRoles.get(1).get("optionName");
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());

            String shiftName1 = "Shift Name 1";
            String shiftName2 = "Shift Name 2";
            String shiftNotes1 = "Shift Notes 1";
            String shiftNotes2 = "Shift Notes 2";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Create 2 shifts with all different
            createShiftsWithSpecificValues(workRole1, shiftName1, "", "9:00am", "12:00pm",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.OpenShift.getValue(), shiftNotes1, "");
            createShiftsWithSpecificValues(workRole2, shiftName2, "", "1:00pm", "3:00pm",
                    1, Arrays.asList(2), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), shiftNotes2, "");

            Thread.sleep(3000);
            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();

            scheduleShiftTablePage.selectSpecificShifts(shiftIndexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            editShiftPage.clickOnDateSelect();
            List<String> dates = editShiftPage.getOptionsFromSpecificSelect();

            editShiftPage.clickOnCancelButton();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            createShiftsWithSpecificValues(workRole1, shiftName1, null, "9:00am", "12:00pm",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.OpenShift.getValue(), shiftNotes1, "");
            createShiftsWithSpecificValues(workRole1, shiftName1, null, "9:00am", "12:00pm",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.OpenShift.getValue(), shiftNotes1, "");
            String selectedDate = dates.get(1);

            shiftIndexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();

            scheduleShiftTablePage.selectSpecificShifts(shiftIndexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify work role name will show when selecting same work role
            editShiftPage.verifyTheTextInCurrentColumn(ConsoleEditShiftPage.sectionType.WorkRole.getType(), workRole1);
            // Verify shift name will show when selecting same shift name
            editShiftPage.verifyTheTextInCurrentColumn(ConsoleEditShiftPage.sectionType.ShiftName.getType(), shiftName1);
            // Verify shift start time will show when selecting the shifts with same start time
            editShiftPage.verifyTheTextInCurrentColumn(ConsoleEditShiftPage.sectionType.StartTime.getType(), "9:00 AM");
            // Verify shift end time will show when selecting the shifts with same start time
            editShiftPage.verifyTheTextInCurrentColumn(ConsoleEditShiftPage.sectionType.EndTime.getType(), "12:00 PM");
            // Verify date will show when selecting the shifts on the same day
            editShiftPage.verifyTheTextInCurrentColumn(ConsoleEditShiftPage.sectionType.Date.getType(), selectedDate);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of Edited column")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheEditedColumnOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
            ArrayList<HashMap<String,String>> workRoles = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            String workRole1 = workRoles.get(0).get("optionName");
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            createShiftsWithSpecificValues(workRole1, "", "", "9:00pm", "11:00pm",
                    2, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");
            HashSet<Integer> set = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            scheduleShiftTablePage.selectSpecificShifts(set);
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify the functionality of Work Role Select
            editShiftPage.clickOnWorkRoleSelect();
            editShiftPage.selectSpecificOptionByText(workRole1);
            String selectedWorkRole = editShiftPage.getSelectedWorkRole();
            SimpleUtils.assertOnFail("Failed to select the work role!", !selectedWorkRole.trim().equals(workRole1), false);
            // Verify the functionality of Clear Edited Fields button
            editShiftPage.clickOnClearEditedFieldsButton();
            selectedWorkRole = editShiftPage.getSelectedWorkRole();
            SimpleUtils.assertOnFail("Failed to clear the edited field!", !workRole1.equalsIgnoreCase(selectedWorkRole), false);
            // Verify the functionality of Shift Name input
            String shiftName = "This is the shift name";
            editShiftPage.inputShiftName(shiftName);
            // Verify the functionality of Update button
            editShiftPage.clickOnUpdateButton();

            Thread.sleep(2000);
            scheduleShiftTablePage.selectSpecificShifts(set);
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify the functionality of Start Time input
            editShiftPage.inputStartOrEndTime("09:00 AM", true);
            // Verify selecting Use Offset checkbox on Start Time section
            editShiftPage.checkUseOffset(true, true);
            // Verify the functionality of Mins input in Start Time section
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, "15", "Late", true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, "1", null, true);
            // Verify the functionality of Hours input in Start Time section
            editShiftPage.verifyTheFunctionalityOfOffsetTime("11", null, null, true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime("13", null, "Early", true);
            // Verify the functionality of Early/Late select in Start Time section
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, null, "Early", true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, null, "Late", true);
            // Verify un-selecting Use Offset checkbox on Start Time section
            editShiftPage.checkUseOffset(true, false);
            // Verify the functionality of End Time input
            editShiftPage.inputStartOrEndTime("12:00 PM", false);
            // Verify selecting Use Offset checkbox on End Time
            editShiftPage.checkUseOffset(false, true);
            // Verify the functionality of Mins input in End Time section
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, "15", null, false);
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, "1", null, false);
            // Verify the functionality of Hours input in End Time section
            editShiftPage.verifyTheFunctionalityOfOffsetTime("11", null, null, false);
            editShiftPage.verifyTheFunctionalityOfOffsetTime("13", null, null, false);
            // Verify the functionality of Early/Late select in End Time section
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, null, "Early", false);
            editShiftPage.verifyTheFunctionalityOfOffsetTime(null, null, "Late", false);
            // Verify un-selecting Use Offset checkbox on End Time section
            editShiftPage.checkUseOffset(false, false);
            // Verify the functionality of Next day check box
            editShiftPage.checkOrUnCheckNextDayOnBulkEditPage(true);

            // Verify the content in Date select
            editShiftPage.clickOnDateSelect();
            List<String> dates = editShiftPage.getOptionsFromSpecificSelect();
            for (String date : dates) {
                if (!date.contains("Sunday") && !date.contains("Monday") && !date.contains("Tuesday") && !date.contains("Wednesday")
                        && !date.contains("Thursday") && !date.contains("Friday") && !date.contains("Saturday")) {
                    SimpleUtils.fail("The content of dates are incorrect!", false);
                    break;
                }
            }
            // Verify can select the date
            editShiftPage.selectSpecificOptionByText(dates.get(0));
            SimpleUtils.assertOnFail("Failed to select the date: " + dates.get(0), editShiftPage.getSelectedDate()
                    .trim().contains(dates.get(0)), false);
            // Verify the content in Assignment Select
            editShiftPage.clickOnAssignmentSelect();
            List<String> assignments = editShiftPage.getOptionsFromSpecificSelect();
            List<String> expectedAssignments = new ArrayList<>();
            expectedAssignments.add("Do not change assignments");
            expectedAssignments.add("Open Shift: Auto Offer to TMs");
            expectedAssignments.add("Assign or Offer to Specific TM's");
            String info = "Only the shifts with same start time, end time, location and worker role can be bulk-assigned";
            if (assignments.get(0).equalsIgnoreCase(expectedAssignments.get(0)) && assignments.get(1).equalsIgnoreCase(
                    expectedAssignments.get(1)) && assignments.get(2).contains(expectedAssignments.get(2))) {
                SimpleUtils.pass("The content in Assignment list is correct!");
            } else {
                SimpleUtils.fail("The content in Assignment list is incorrect!", false);
            }
            // Verify can select the Assignment
            editShiftPage.selectSpecificOptionByText(assignments.get(0));
            editShiftPage.getSelectedAssignment();
            SimpleUtils.assertOnFail("Failed to select the assignment: " + assignments.get(0), editShiftPage
                    .getSelectedAssignment().trim().contains(assignments.get(0)), false);
            // Verify can input the shift notes
            editShiftPage.inputShiftNotes("notes");
            // Verify can select the two options
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowConflicts.getOption(), true);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowComplianceErrors.getOption(), false);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowConflicts.getOption(), true);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowComplianceErrors.getOption(), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing work role")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingWorkRoleOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsNo();

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
            ArrayList<HashMap<String,String>> workRoles = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            String workRole1 = workRoles.get(0).get("optionName");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftOperatePage.clickOnProfileIconByIndex(0);
            shiftOperatePage.clickOnChangeRole();
            List<String> workRoleList = shiftOperatePage.getWorkRoleListFromChangeShiftRoleOption();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Create 2 shifts with all different
            List<String> names = createShiftsWithSpecificValues(workRole1, "", "", "9:00am", "12:00pm",
                    2, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();

            scheduleShiftTablePage.selectSpecificShifts(shiftIndexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify only elegible work roles will show when clicking on Change Shift Role and override assignment rule is set to No
            editShiftPage.clickOnWorkRoleSelect();
            List<String> actualWorkRoleList = editShiftPage.getOptionsFromSpecificSelect();
            // Verify only elegible work roles will show on bulk edit shift dialog when override assignment rule is set to No
            if (workRoleList.containsAll(actualWorkRoleList) && actualWorkRoleList.containsAll(workRoleList)) {
                SimpleUtils.pass("Work role list shows correctly");
            } else {
                SimpleUtils.fail("Work role list is incorrect when override assignment rule is set to No!", false);
            }
            if (actualWorkRoleList.size() > 1) {
                for (int i = 0; i < actualWorkRoleList.size(); i++) {
                    if (actualWorkRoleList.get(i).equalsIgnoreCase(workRole1)) {
                        actualWorkRoleList.remove(i);
                        break;
                    }
                }
            }
            // Verify work role is updated
            editShiftPage.selectSpecificOptionByText(actualWorkRoleList.get(0));
            editShiftPage.clickOnUpdateButton();
            scheduleMainPage.saveSchedule();
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[1].toString()));
            SimpleUtils.assertOnFail("Work role is not updated!", actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo1.get(4))
                    && actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo2.get(4)), false);
            // Verify work role is saved
            //scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[1].toString()));
            SimpleUtils.assertOnFail("Work role is not updated!", actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo1.get(4))
                    && actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo2.get(4)), false);
            // Verify there is no role violation
            SimpleUtils.assertOnFail("Role violation should not show!",
                    !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(
                            Integer.parseInt(shiftIndexes.toArray()[0].toString())
                    )).contains("Role Violation"), false);
            SimpleUtils.assertOnFail("Role violation should not show!",
                    !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(
                            Integer.parseInt(shiftIndexes.toArray()[1].toString())
                    )).contains("Role Violation"), false);
            // Verify all available work roles will show when clicking on Change Shift Role and override assignment rule is set to Yes
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsYes();

            goToSchedulePageScheduleTab();
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftOperatePage.clickOnProfileIconByIndex(0);
            shiftOperatePage.clickOnChangeRole();
            List<String> workRoleList2 = shiftOperatePage.getWorkRoleListFromChangeShiftRoleOption();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());
            SimpleUtils.assertOnFail("Not all work role listed!", workRoleList.size() <= workRoleList2.size(), false);

            // Verify all available work roles will show on bulk edit shift dialog when override assignment rule is set to Yes
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.selectSpecificShifts(shiftIndexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnWorkRoleSelect();
            List<String> actualWorkRoleList2 = editShiftPage.getOptionsFromSpecificSelect();
            if (workRoleList2.containsAll(actualWorkRoleList2) && actualWorkRoleList2.containsAll(workRoleList2)) {
                SimpleUtils.pass("Work role list shows correctly");
            } else {
                SimpleUtils.fail("Work role list is incorrect when override assignment rule is set to Yes!", false);
            }
            // Verify work role is updated
            actualWorkRoleList2.remove(workRole1);
            editShiftPage.selectSpecificOptionByText(actualWorkRoleList2.get(0));
            editShiftPage.clickOnUpdateButton();
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[1].toString()));
            SimpleUtils.assertOnFail("Work role is not updated!", actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo1.get(4))
                    && actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo2.get(4)), false);
            // Verify work role is saved
            //scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[1].toString()));
            SimpleUtils.assertOnFail("Work role is not updated!", actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo1.get(4))
                    && actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo2.get(4)), false);

            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsYes();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing shift name")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingShiftNameOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(2);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can update the shift name without selecting 2 options
            String shiftName = "This is the shift name";
            editShiftPage.inputShiftName(shiftName);
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            // Verify the shift name can show on the info popup
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[1].toString()));
            SimpleUtils.assertOnFail("Shift Name is not updated!", shiftName.equalsIgnoreCase(shiftInfo1.get(9))
                    && shiftName.equalsIgnoreCase(shiftInfo2.get(9)), false);
            // Verify the shift name is saved successfully
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[1].toString()));
            SimpleUtils.assertOnFail("Shift Name is not updated!", shiftName.equalsIgnoreCase(shiftInfo1.get(9))
                    && shiftName.equalsIgnoreCase(shiftInfo2.get(9)), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing Start Time")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingStartTimeOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String workRole = shiftOperatePage.getRandomWorkRole();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> assignedNames = createShiftsWithSpecificValues(workRole, "", "", "9:00am", "05:00pm",
                    2, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

            HashSet<Integer> indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            Iterator<Integer> iterator = indexes.iterator();
            List<Integer> indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can change the start time without checking options
            String inputStartTime = "10:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");

            // Verify the start time of the shifts is updated
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            String startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            String startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);

            // Verify the start time is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            inputStartTime = "4:00 AM";
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify success will pop up when changing the start time will cause violation
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            scheduleMainPage.clickOnCancelButtonOnEditMode();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can change the start time which will cause violation with selecting the options
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowConflicts.getOption(), true);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowComplianceErrors.getOption(), true);
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }

            // Verify the start time of the shifts is updated
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);

            // Verify the start time is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Verify the start time shows correctly using early offset
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.checkUseOffset(true, true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime("1", null, "Early", true);
            editShiftPage.clickOnUpdateButton();
            inputStartTime = "3:00 am";
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Verify the start time shows correctly using later offset
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.checkUseOffset(true, true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime("6", null, "Late", true);
            editShiftPage.clickOnUpdateButton();
            inputStartTime = "9:00 am";
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            System.out.println(startTime1);
            System.out.println(startTime2);
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);

            // Verify the start time can be saved successfully
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing End Time")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingEndTimeOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String workRole = shiftOperatePage.getRandomWorkRole();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> assignedNames = createShiftsWithSpecificValues(workRole, "", "", "9:00am", "05:00pm",
                    2, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

            HashSet<Integer> indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            Iterator<Integer> iterator = indexes.iterator();
            List<Integer> indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can change the end time without checking options
            String inputEndTime = "4:00 pm";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();

            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }

            // Verify the start time of the shifts is updated
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            String startTime1 = shiftInfo1.get(6).split("-")[1].trim();
            String startTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("End time is not updated!", inputEndTime.equalsIgnoreCase(startTime1) &&
                    inputEndTime.equalsIgnoreCase(startTime2), false);

            // Verify the end time is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[1].trim();
            startTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("End time is not updated!", inputEndTime.equalsIgnoreCase(startTime1) &&
                    inputEndTime.equalsIgnoreCase(startTime2), false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            inputEndTime = "7:00 pm";
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify success will pop up when changing the End time will cause violation
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");

            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }

            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can change the end time which will cause violation with selecting the options
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowConflicts.getOption(), true);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowComplianceErrors.getOption(), true);
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");

            // Verify the end time of the shifts is updated
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[1].trim();
            startTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputEndTime.equalsIgnoreCase(startTime1) &&
                    inputEndTime.equalsIgnoreCase(startTime2), false);

            // Verify the end time is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[1].trim();
            startTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputEndTime.equalsIgnoreCase(startTime1) &&
                    inputEndTime.equalsIgnoreCase(startTime2), false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Verify the start time shows correctly using early offset
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.checkUseOffset(false, true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime("3", null, "Early", false);
            editShiftPage.clickOnUpdateButton();

            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }

            inputEndTime = "4:00 pm";
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[1].trim();
            startTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("End time is not updated!", inputEndTime.equalsIgnoreCase(startTime1) &&
                    inputEndTime.equalsIgnoreCase(startTime2), false);

            // Verify the end time shows correctly using later offset
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.checkUseOffset(false, true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime("1", null, "Late", false);
            editShiftPage.clickOnUpdateButton();

            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }

            inputEndTime = "5:00 pm";
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[1].trim();
            startTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("End time is not updated!", inputEndTime.equalsIgnoreCase(startTime1) &&
                    inputEndTime.equalsIgnoreCase(startTime2), false);

            // Verify the start time can be saved successfully
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[1].trim();
            startTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputEndTime.equalsIgnoreCase(startTime1) &&
                    inputEndTime.equalsIgnoreCase(startTime2), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing Date")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingDateOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String workRole = shiftOperatePage.getRandomWorkRole();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> assignedNames = createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    2, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

            HashSet<Integer> indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            editShiftPage.clickOnDateSelect();
            List<String> dates = editShiftPage.getOptionsFromSpecificSelect();

            // Verify can change the date without selecting the two options
            editShiftPage.selectSpecificOptionByText(dates.get(1));
            editShiftPage.clickOnUpdateButton();

            HashSet<Integer> newIndexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            System.out.println(scheduleShiftTablePage.getOneDayShiftByName(1, assignedNames.get(0)).size());
            SimpleUtils.assertOnFail("Shifts are not moved to the next day!", !indexes.equals(newIndexes), false);

            // Verify the error will pop up when changing the date without selecting the two options
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", assignedNames.get(0));
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", assignedNames.get(1));
            Thread.sleep(2000);
            scheduleShiftTablePage.selectSpecificShifts(newIndexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(newIndexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnDateSelect();
            editShiftPage.selectSpecificOptionByText(dates.get(0));
            editShiftPage.clickOnUpdateButton();

            mySchedulePage.verifyThePopupMessageOnTop("Error");

            // Verify can change the date with selecting the two options
            scheduleShiftTablePage.selectSpecificShifts(newIndexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(newIndexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnDateSelect();
            editShiftPage.selectSpecificOptionByText(dates.get(0));
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowConflicts.getOption(), true);
            editShiftPage.checkOrUncheckOptionsByName(ConsoleEditShiftPage.twoOptions.AllowComplianceErrors.getOption(), true);
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            SimpleUtils.assertOnFail("The previous assigned shifts are not converted to open shifts!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, "Open").size() >= 2, false);

            // Verify the shifts are moved to the selected day
            String firstName1 = assignedNames.get(0).contains(" ") ? assignedNames.get(0).split(" ")[0] : assignedNames.get(0);
            String firstName2 = assignedNames.get(1).contains(" ") ? assignedNames.get(1).split(" ")[0] : assignedNames.get(1);
            SimpleUtils.assertOnFail("Shift with name: " + firstName1 + " is not moved to the selected date!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstName1).size() == 1, false);
            SimpleUtils.assertOnFail("Shift with name: " + firstName2 + " is not moved to the selected date!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstName2).size() == 1, false);

            // Verify the changes can be saved successfully
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("Shift with name: " + firstName1 + " is not moved to the selected date!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstName1).size() == 1, false);
            SimpleUtils.assertOnFail("Shift with name: " + firstName2 + " is not moved to the selected date!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstName2).size() == 1, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing Assignment")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingAssignmentOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String workRole = shiftOperatePage.getRandomWorkRole();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String activeWeek = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithDayName(activeWeek.replace("\n", " ").substring(0,3));
            scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
            scheduleMainPage.saveSchedule();
            scheduleCommonPage.clickOnWeekView();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> assignedNames = createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    2, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

            HashSet<Integer> indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            editShiftPage.clickOnAssignmentSelect();

            // Verify the functionality of "Do not change assignments"
            editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.DoNotChangeAssignments.getOption());
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");

            // Verify the shifts are converted to open shifts
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnAssignmentSelect();
            editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.OpenShift.getOption());
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            SimpleUtils.assertOnFail("The previous assigned shifts are not converted to open shifts!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, "Open").size() >= 2, false);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Verify the option "Assign or Offer to Specific TM's" is enabled
            bulkEditShiftsByIndexes(indexes);
            editShiftPage.clickOnAssignmentSelect();
            List<String> assignments = editShiftPage.getOptionsFromSpecificSelect();
            String assignOrOfferOption = ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption();
            if (assignments.get(2).contains(assignOrOfferOption.toLowerCase())) {
                SimpleUtils.pass("Assign or Offer to Specific TM's is enabled!");
            } else {
                SimpleUtils.fail("Assign or Offer to Specific TM's is not enabled!", false);
            }

            // Verify Search Team Members page will show when selecting "Assign or Offer to Specific TM's
            editShiftPage.selectSpecificOptionByText(assignOrOfferOption);
            editShiftPage.clickOnUpdateButton();

            // Verify can assign or offer to new team members
            newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift(assignedNames.get(0), true);
            newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift(assignedNames.get(1), true);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            // Verify the offers are in draft status after saving the schedule
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(assignedNames.get(0), "Draft Offer");
            shiftOperatePage.verifyTMInTheOfferList(assignedNames.get(1), "Draft Offer");
            shiftOperatePage.closeViewStatusContainer();
            createSchedulePage.publishActiveSchedule();

            // Verify the option "Assign or Offer to Specific TM's" is disabled
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");
            createShiftsWithSpecificValues(workRole, "", "", "10:00am", "03:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            bulkEditShiftsByIndexes(indexes);
            editShiftPage.clickOnAssignmentSelect();
            assignments = editShiftPage.getOptionsFromSpecificSelect();
            assignOrOfferOption = ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption() +
                    "\nOnly the shifts with same start time, end time, location and worker role can be bulk-assigned";
            System.out.println(assignments.get(2));
            System.out.println(assignOrOfferOption);
            if (assignments.get(2).equals(assignOrOfferOption.toLowerCase())) {
                SimpleUtils.pass("Assign or Offer to Specific TM's is disabled!");
            } else {
                SimpleUtils.fail("Assign or Offer to Specific TM's is not disabled!", false);
            }
            editShiftPage.clickOnCancelButton();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing shift notes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingShiftNotesOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            scheduleMainPage.selectShiftTypeFilterByText("Assigned");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            HashSet<Integer> indexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(2);
            Iterator<Integer> iterator = indexes.iterator();
            List<Integer> indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can update the shift notes without selecting 2 options
            String note = "Test Shift Notes";
            editShiftPage.inputShiftNotes(note);
            editShiftPage.clickOnUpdateButton();

            // Verify the shift notes can show on the info popup
            List<String> infoList1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            SimpleUtils.assertOnFail("Failed to update the shift notes!", note.equalsIgnoreCase(infoList1.get(10)), false);
            List<String> infoList2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            SimpleUtils.assertOnFail("Failed to update the shift notes!", note.equalsIgnoreCase(infoList2.get(10)), false);

            scheduleMainPage.saveSchedule();

            // Verify the shift notes is saved successfully
            infoList1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            SimpleUtils.assertOnFail("Failed to update the shift notes!", note.equalsIgnoreCase(infoList1.get(10)), false);
            infoList2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            SimpleUtils.assertOnFail("Failed to update the shift notes!", note.equalsIgnoreCase(infoList2.get(10)), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void bulkEditShiftsByIndexes(HashSet<Integer> indexes) throws Exception {
        scheduleShiftTablePage.selectSpecificShifts(indexes);
        scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
        scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
        SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the error message of time period displays correctly when bulk editing shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyErrorMessageOfStartAndEndTimeOnBulkEditAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Go to the Scheduling Policy page
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

            //Create a schedule if there is no any
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Catch up on random shift for further shift creation
            String firstNameOfTM = null;
            String workRole = null;
            if (isActiveWeekGenerated) {
                List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM = shiftInfo.get(0);
                int shiftCount1 = 0;
                while ((firstNameOfTM.equalsIgnoreCase("open")
                        || firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
                    shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                    firstNameOfTM = shiftInfo.get(0);
                    shiftCount1++;
                }
                workRole = shiftInfo.get(4);
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            //Create multiple new shifts and assign them to the same TM
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "11:00am",
                    1, Arrays.asList(0,1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM);
            HashSet<Integer> indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            Iterator<Integer> iterator = indexes.iterator();
            List<Integer> indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }

            //Verify the start time of the shifts is updated
            String inputStartTime = "8:00 am";
            bulkEditShiftsByIndexes(indexes);
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.clickOnUpdateButton();
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            List<String> shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            String startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            String startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2), false);
            scheduleMainPage.saveSchedule();

            //Verify the end time of the shifts is updated
            String inputEndTime = "12:00 pm";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            bulkEditShiftsByIndexes(indexes);
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            String endTime1 = shiftInfo1.get(6).split("-")[1].trim();
            String endTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("End time is not updated!", inputEndTime.equalsIgnoreCase(endTime1) &&
                    inputEndTime.equalsIgnoreCase(endTime2), false);
            scheduleMainPage.saveSchedule();

            //Bulk edit the shifts, don't change start time & end time, update directly, check the shift's time duration
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            bulkEditShiftsByIndexes(indexes);
            //editShiftPage.inputStartOrEndTime("", true);
            //editShiftPage.inputStartOrEndTime("", false);
            editShiftPage.clickOnUpdateButton();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(1));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            startTime2 = shiftInfo2.get(6).split("-")[0].trim();
            endTime1 = shiftInfo1.get(6).split("-")[1].trim();
            endTime2 = shiftInfo2.get(6).split("-")[1].trim();
            SimpleUtils.assertOnFail("Shift's time duration is changed!", inputStartTime.equalsIgnoreCase(startTime1) &&
                    inputStartTime.equalsIgnoreCase(startTime2) && inputEndTime.equalsIgnoreCase(endTime1) &&
                    inputEndTime.equalsIgnoreCase(endTime2), false);
            scheduleMainPage.saveSchedule();

            //Bulk edit the shifts, change start time to occur the error message
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            bulkEditShiftsByIndexes(indexes);
            inputStartTime = "3:00 am";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.clickOnUpdateButton();
            String errorMessage = "Hours on";
            List<String> startErrorMessage = editShiftPage.getErrorMessageOfTime();
            SimpleUtils.assertOnFail("Error message of start time is not loaded correctly!", startErrorMessage.get(0).contains(errorMessage), false);
            shiftOperatePage.closeViewStatusContainer();

            //Bulk edit the shifts, change end time to occur the error message
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            bulkEditShiftsByIndexes(indexes);
            inputEndTime = "7:00 am";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            String defaultMessage = "Start time should be before End time";
            List<String> endErrorMessage = editShiftPage.getErrorMessageOfTime();
            SimpleUtils.assertOnFail("Error message of start time is not loaded correctly!", endErrorMessage.get(0).trim().equalsIgnoreCase(defaultMessage), false);
            shiftOperatePage.closeViewStatusContainer();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
