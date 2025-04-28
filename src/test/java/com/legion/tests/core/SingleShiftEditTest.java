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
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

import static com.legion.utils.MyThreadLocal.firstName;
import static com.legion.utils.MyThreadLocal.getDriver;

public class SingleShiftEditTest extends TestBase {
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
    private BasePage basePage;
    private SmartCardPage smartCardPage;
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
            basePage = new BasePage();
            smartCardPage = pageFactory.createSmartCardPage();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content on Edit Single Shifts window for regular location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOnSingleEditShiftsWindowForRegularLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
            String workRole = shiftOperatePage.getRandomWorkRole();

            BasePage basePage = new BasePage();
            String activeWeek = basePage.getActiveWeekText();
            String startOfWeek = activeWeek.split(" ")[3] + " " + activeWeek.split(" ")[4];
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select single shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 1;
            int index = 1;
            HashSet<Integer> set = new HashSet<>();
            set.add(index);
            List<String> shiftInfo= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            List<String> selectedDays = scheduleShiftTablePage.getSelectedWorkDays(set);
            // Verify action menu will pop up when right clicking on anywhere of the selected shifts
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            // Verify Edit action is visible when right clicking the selected shift in week view
            // Verify the functionality of Edit button in week view
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly
            editShiftPage.verifyShiftInfoCard(shiftInfo);
            // Verify the Location Name shows correctly
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section
            editShiftPage.verifyTheContentOfOptionsSectionIsNotLoaded();
            // Verify the visibility of Clear Edited Fields button
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the three columns show on Shift Details section
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section
            editShiftPage.verifyEditableTypesShowOnSingleEditShiftDetail();
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
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window in day view
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly in day view
            editShiftPage.verifyShiftInfoCard(shiftInfo);
            // Verify the Location Name shows correctly in day view
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons in day view
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section in day view
            editShiftPage.verifyTheContentOfOptionsSectionIsNotLoaded();
            // Verify the visibility of Clear Edited Fields button in day view
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the three columns show on Shift Details section in day view
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section in day view
            editShiftPage.verifyEditableTypesShowOnSingleEditShiftDetail();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of Current and Edit column when selecting single shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheCurrentColumnOnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            createSchedulePage.publishActiveSchedule();
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
            ArrayList<HashMap<String,String>> workRoles = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            String workRole = workRoles.get(0).get("optionName");
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());
            String activeWeek = basePage.getActiveWeekText();
            String startOfWeek =  SimpleUtils.getFullWeekDayName(activeWeek.split(" ")[0]);
            String startOfWeekDate = activeWeek.split(" ")[3] + " " + activeWeek.split(" ")[4];
            String shiftName = "Shift Name 1";
            String shiftNotes = "Shift Notes 1";
            String shiftStartTime = "9:00 AM";
            String shiftEndTime = "12:00 PM";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Create 2 shifts with all different
            createShiftsWithSpecificValues(workRole, shiftName, "", shiftStartTime, shiftEndTime,
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), shiftNotes, "");

            int index = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon().iterator().next();
            mySchedulePage.clickOnShiftByIndex(index);
            shiftOperatePage.clickOnEditMeaLBreakTime();
            Thread.sleep(5000);
            List<String> breakTimes = shiftOperatePage.verifyEditBreaks();
            shiftOperatePage.clickOnUpdateEditShiftTimeButton();
            String mealBreakTime = breakTimes.get(0).replace("am", "AM").replace("pm", "PM");
            String restBreakTime = breakTimes.get(1).replace("am", "AM").replace("pm", "PM");
            HashSet<Integer> set = new HashSet<>();
            set.add(index);
            List<String> shiftInfo= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            String firstName = shiftInfo.get(0);
            String lastName = shiftInfo.get(5);
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the original work role will show when selecting the single shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.WorkRole.getType(), workRole);
            // Verify the original shift name will show when selecting single shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.ShiftName.getType(), shiftName);
            // Verify original shift start time will show when selecting single shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.StartTime.getType(), shiftStartTime);
            // Verify original shift end time will show when selecting single shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.EndTime.getType(), shiftEndTime);
            // Verify original Breaks will show when selecting single shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.Breaks.getType(), mealBreakTime+" and "+restBreakTime);
            // Verify original Date will show when selecting single shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.Date.getType(), startOfWeek+ " - "+startOfWeekDate);
            // Verify avatars will show consistent with the selected shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.Assignment.getType(), firstName+ " "+lastName);
            // Verify the original shift note will show when selecting single shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.ShiftNotes.getType(), shiftNotes);

            //update work role
            editShiftPage.clickOnWorkRoleSelect();
            List<String> actualWorkRoleList2 = editShiftPage.getOptionsFromSpecificSelect();
            editShiftPage.selectSpecificOptionByText(actualWorkRoleList2.get(0));
            //update shift name
            editShiftPage.inputShiftName(shiftName+ " updated");
            //update shift start and end time
            editShiftPage.inputStartOrEndTime("10:00 AM", true);
            editShiftPage.inputStartOrEndTime("2:00 PM", false);
            //remove breaks
            editShiftPage.removeAllMealBreaks();
            editShiftPage.removeAllRestBreaks();
            //update date
            editShiftPage.clickOnDateSelect();
            List<String> dates = editShiftPage.getOptionsFromSpecificSelect();
            // Verify can select the date
            editShiftPage.selectSpecificOptionByText(dates.get(2));
            //update shift notes
            editShiftPage.inputShiftNotes(shiftNotes+ " updated");

            //Verify the original work role name still show when selecting new work role
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.WorkRole.getType(), workRole);
            // Verify the original shift name will show when input new shift name
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.ShiftName.getType(), shiftName);
            // Verify original shift start time still show when input new shift start time
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.StartTime.getType(), shiftStartTime);
            // Verify original shift end time still show when input new shift end time
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.EndTime.getType(), shiftEndTime);
            // Verify original Breaks still show when remove all breaks
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.Breaks.getType(), mealBreakTime+" and "+restBreakTime);
            // Verify original Date still show when selecting new Date
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.Date.getType(), startOfWeek+ " - "+startOfWeekDate);
            // Verify avatars will show consistent with the selected shift
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.Assignment.getType(), firstName+ " "+lastName);
            // Verify the original shift note will show when input new shift note
            editShiftPage.verifyTheTextInCurrentColumnOnSingleEditShiftPage(ConsoleEditShiftPage.sectionType.ShiftNotes.getType(), shiftNotes);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing work role on single edit shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingWorkRoleOnSingleEditShiftWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            setOverRideAssignmentRule(false, location, false);
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
            newShiftPage.clickOnDayViewAddNewShiftButton();
            List<String> workRoleList = newShiftPage.getWorkRoleList();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Create 2 shifts with all different
            List<String> names = createShiftsWithSpecificValues(workRole1, "", "", "9:00am", "12:00pm",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");
            scheduleMainPage.saveSchedule();
            Thread.sleep(10000);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();

            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify only elegible work roles will show when clicking on Change Shift Role and override assignment rule is set to No
            editShiftPage.clickOnWorkRoleSelect();
            List<String> actualWorkRoleList = editShiftPage.getOptionsFromSpecificSelect();
            // Verify only elegible work roles will show on bulk edit shift dialog when override assignment rule is set to No
            if (workRoleList.containsAll(actualWorkRoleList)){
//                    && actualWorkRoleList.containsAll(workRoleList)) {
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
            editShiftPage.clickOnUpdateAnywayButton();
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            SimpleUtils.assertOnFail("Work role is not updated!", actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo1.get(4)), false);
            // Verify work role is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            SimpleUtils.assertOnFail("Work role is not updated!", actualWorkRoleList.get(0).equalsIgnoreCase(shiftInfo1.get(4)), false);
            // Verify there is no role violation
            SimpleUtils.assertOnFail("Role violation should not show!",
                    !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(
                            Integer.parseInt(shiftIndexes.toArray()[0].toString())
                    )).contains("Role Violation"), false);
            // Verify all available work roles will show when clicking on Change Shift Role and override assignment rule is set to Yes
            setOverRideAssignmentRule(false, location, true);

            goToSchedulePageScheduleTab();
//            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            shiftOperatePage.clickOnProfileIconByIndex(0);
//            shiftOperatePage.clickOnChangeRole();
//            List<String> workRoleList2 = shiftOperatePage.getWorkRoleListFromChangeShiftRoleOption();
//            scheduleMainPage.clickOnCancelButtonOnEditMode();
//            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());
//            SimpleUtils.assertOnFail("Not all work role listed!", workRoleList2.size() <= workRoleList.size(), false);

            // Verify all available work roles will show on bulk edit shift dialog when override assignment rule is set to Yes
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnWorkRoleSelect();
            List<String> actualWorkRoleList2 = editShiftPage.getOptionsFromSpecificSelect();
            if (workRoleList.containsAll(actualWorkRoleList2)) {
//                    && actualWorkRoleList2.containsAll(workRoleList2)) {
                SimpleUtils.pass("Work role list shows correctly");
            } else {
                SimpleUtils.fail("Work role list is incorrect when override assignment rule is set to Yes!", false);
            }
            // Verify work role is upated
            actualWorkRoleList2.remove(actualWorkRoleList.get(0));
            editShiftPage.selectSpecificOptionByText(actualWorkRoleList2.get(0));
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            SimpleUtils.assertOnFail("Work role is not updated! the expected is:"+actualWorkRoleList2.get(0)
                    +" the actual is:"+shiftInfo1.get(4), actualWorkRoleList2.get(0).equalsIgnoreCase(shiftInfo1.get(4))
                    ||actualWorkRoleList2.get(0).trim().replace(" ", "").equalsIgnoreCase(shiftInfo1.get(4).trim().replace(" ", "")), false);
            // Verify work role is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            SimpleUtils.assertOnFail("Work role is not updated! the expected is:"+actualWorkRoleList2.get(0)
                    +" the actual is:"+shiftInfo1.get(4), actualWorkRoleList2.get(0).equalsIgnoreCase(shiftInfo1.get(4))
                    || actualWorkRoleList2.get(0).trim().replace(" ", "").equalsIgnoreCase(shiftInfo1.get(4).trim().replace(" ", "")), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        } finally {
//            newShiftPage.closeNewCreateShiftPage();
////            boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//            setOverRideAssignmentRule(false, location, true);
        }
    }


    private void setOverRideAssignmentRule(boolean isLocationUsingControlsConfiguration, String location, boolean yesOrNo) throws Exception {
        if (isLocationUsingControlsConfiguration) {
            //Go to Controls page
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling Policies Page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.enableOverRideAssignmentRuleAsNo();
            Thread.sleep(10000);
        } else {
            //Go to OP page
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);
            SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Scheduling Policies");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Scheduling Policies"), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            Thread.sleep(3000);
            if (yesOrNo) {
                controlsNewUIPage.enableOverRideAssignmentRuleAsYesForOP();
            } else
                controlsNewUIPage.enableOverRideAssignmentRuleAsNoForOP();
            configurationPage.publishNowTheTemplate();
//                Thread.sleep(240000);
            switchToConsoleWindow();
            refreshCachesAfterChangeTemplate();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the functionality of changing shift name on single edit shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingShiftNameOnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can update the shift name without selecting 2 options
            String shiftName = "This is the shift name";
            editShiftPage.inputShiftName(shiftName);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            // Verify the shift name can show on the info popup
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            SimpleUtils.assertOnFail("Shift Name is not updated!", shiftName.equalsIgnoreCase(shiftInfo1.get(9)), false);
            // Verify the shift name is saved successfully
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString()));
            SimpleUtils.assertOnFail("Shift Name is not updated!", shiftName.equalsIgnoreCase(shiftInfo1.get(9)), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing Start Time on single edit shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingStartTimeOnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

            HashSet<Integer> indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            Iterator<Integer> iterator = indexes.iterator();
            List<Integer> indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can change the start time without checking options
            String inputStartTime = "10:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");

            // Verify the start time of the shifts is updated
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            String startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) , false);

            // Verify the start time is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) , false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            inputStartTime = "4:00 AM";
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify error message will pop up when changing the start time will cause violation
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            mySchedulePage.verifyThePopupMessageOnTop("Error! Could not edit 1 shift");

            //Verify the start time of the shifts is not updated
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", !inputStartTime.equalsIgnoreCase(startTime1) , false);

            // Verify the start time is saved
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", !inputStartTime.equalsIgnoreCase(startTime1), false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Verify the start time shows correctly using early offset
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.checkUseOffset(true, true);
            editShiftPage.verifyTheFunctionalityOfOffsetTime("1", null, "Early", true);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            inputStartTime = "9:00 am";
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1), false);
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
            editShiftPage.clickOnUpdateAnywayButton();
            inputStartTime = "3:00 pm";
            indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            iterator = indexes.iterator();
            indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            System.out.println(startTime1);
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1) , false);

            // Verify the start time can be saved successfully
            scheduleMainPage.saveSchedule();
            shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            startTime1 = shiftInfo1.get(6).split("-")[0].trim();
            SimpleUtils.assertOnFail("Start time is not updated!", inputStartTime.equalsIgnoreCase(startTime1), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing End Time on single edit shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingEndTimeOnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            editShiftPage.clickOnUpdateAnywayButton();

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
            editShiftPage.clickOnUpdateAnywayButton();
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
            editShiftPage.clickOnUpdateAnywayButton();
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
            editShiftPage.clickOnUpdateAnywayButton();

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
            editShiftPage.clickOnUpdateAnywayButton();

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
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing Date on single edit shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingDateOnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            String workRole = shiftOperatePage.getRandomWorkRole();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> assignedNames = createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

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
            editShiftPage.clickOnUpdateAnywayButton();
            HashSet<Integer> newIndexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            System.out.println(scheduleShiftTablePage.getOneDayShiftByName(1, assignedNames.get(0)).size());
            SimpleUtils.assertOnFail("Shifts are not moved to the next day!", !indexes.equals(newIndexes), false);

            // Verify the error will pop up when changing the date without selecting the two options
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", assignedNames.get(0));
            Thread.sleep(2000);
            scheduleShiftTablePage.selectSpecificShifts(newIndexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(newIndexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnDateSelect();
            editShiftPage.selectSpecificOptionByText(dates.get(0));
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
//            mySchedulePage.verifyThePopupMessageOnTop("Success");

            // Verify the shifts are moved to the selected day
            String firstName1 = assignedNames.get(0).contains(" ") ? assignedNames.get(0).split(" ")[0] : assignedNames.get(0);
            SimpleUtils.assertOnFail("Shift with name: " + firstName1 + " is not moved to the selected date!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstName1).size() >= 1, false);

            // Verify the changes can be saved successfully
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("Shift with name: " + firstName1 + " is not moved to the selected date!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstName1).size() >= 1, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing Assignment on single edit shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingAssignmentOnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.publishOrRepublishSchedule();
            String workRole = shiftOperatePage.getRandomWorkRole();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> assignedNames = createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

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
            editShiftPage.clickOnUpdateAnywayButton();
//            mySchedulePage.verifyThePopupMessageOnTop("Success");

            // Verify the shifts are converted to open shifts
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnAssignmentSelect();
            editShiftPage.selectSpecificOptionByText(ConsoleEditShiftPage.assignmentOptions.OpenShift.getOption());
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
//            mySchedulePage.verifyThePopupMessageOnTop("Success");
            SimpleUtils.assertOnFail("The previous assigned shifts are not converted to open shifts!",
                    scheduleShiftTablePage.getOneDayShiftByName(0, "Open").size() >= 1, false);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Verify the option "Assign or Offer to Specific TM's" is enabled
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnAssignmentSelect();
            List<String> assignments = editShiftPage.getOptionsFromSpecificSelect();
            String assignOrOfferOption = ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption();
            if (assignments.get(2).equalsIgnoreCase(assignOrOfferOption)) {
                SimpleUtils.pass("Assign or Offer to Specific TM's is enabled!");
            } else {
                SimpleUtils.fail("Assign or Offer to Specific TM's is not enabled! The expected is "+assignOrOfferOption
                        +" The actual is: "+assignments.get(2), false);
            }

            // Verify Search Team Members page will show when selecting "Assign or Offer to Specific TM's
            editShiftPage.selectSpecificOptionByText(assignOrOfferOption);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();

            // Verify can assign or offer to new team members
            newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift(assignedNames.get(0), true);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            int index = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon().iterator().next();
            // Verify the offers are in draft status after saving the schedule
            shiftOperatePage.clickOnProfileIconByIndex(index);
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(assignedNames.get(0), "Draft Offer");
            shiftOperatePage.closeViewStatusContainer();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing shift notes on single edit shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingShiftNotesOnSingleEditShiftWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.selectShiftTypeFilterByText("Assigned");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            HashSet<Integer> indexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
            Iterator<Integer> iterator = indexes.iterator();
            List<Integer> indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            Thread.sleep(3000);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify can update the shift notes without selecting 2 options
            String note = "Test Shift Notes";
            editShiftPage.inputShiftNotes(note);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();

            // Verify the shift notes can show on the info popup
            List<String> infoList1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            SimpleUtils.assertOnFail("Failed to update the shift notes!", note.equalsIgnoreCase(infoList1.get(10)), false);

            scheduleMainPage.saveSchedule();

            // Verify the shift notes is saved successfully
            infoList1 = scheduleShiftTablePage.getTheShiftInfoByIndex(indexList.get(0));
            SimpleUtils.assertOnFail("Failed to update the shift notes!", note.equalsIgnoreCase(infoList1.get(10)), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content of Breaks sections")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOfBreaksSectionsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }else
                createSchedulePage.publishActiveSchedule();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.selectShiftTypeFilterByText("Assigned");
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "04:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", "");

            HashSet<Integer> indexes = scheduleShiftTablePage.getAddedShiftsIndexesByPlusIcon();
            int index = indexes.iterator().next();
//            mySchedulePage.clickOnShiftByIndex(index);
//            shiftOperatePage.clickOnEditMeaLBreakTime();
//            List<String> breakTimes = shiftOperatePage.verifyEditBreaks();
//            shiftOperatePage.clickOnUpdateEditShiftTimeButton();
//            String mealBreakTime = breakTimes.get(0).replace(" am", "am").replace(" pm", "pm");
//            String restBreakTime = breakTimes.get(1).replace(" am", "am").replace(" pm", "pm");
//            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
//            String action = "Edit";
//            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
//            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
//
//            //Verify the meal and rest breaks time are display correctly in Edit column
//            Map<String, String> breakTimesOnSingleEditPage = editShiftPage.getMealBreakTimes().get(0);
//            String breakStartTime = changeTimeFormat(breakTimesOnSingleEditPage.get("mealStartTime")).replace(" ", "");
//            String breakEndTime = changeTimeFormat(breakTimesOnSingleEditPage.get("mealEndTime")).replace(" ", "");
//            SimpleUtils.assertOnFail("The expected break time is: "+mealBreakTime
//                            + ". The actual is: "+ (breakStartTime+ " "+breakEndTime),
//                    mealBreakTime.toLowerCase().contains(breakStartTime.toLowerCase())
//                            && mealBreakTime.toLowerCase().contains(breakEndTime.toLowerCase()), false);
//
//            Map<String, String> restTimesOnSingleEditPage = editShiftPage.getRestBreakTimes().get(0);
//            String restStartTime = changeTimeFormat(restTimesOnSingleEditPage.get("restStartTime")).replace(" ", "");
//            String restEndTime = changeTimeFormat(restTimesOnSingleEditPage.get("restEndTime")).replace(" ", "");
//            SimpleUtils.assertOnFail("The expected rest time is: "+restBreakTime
//                            + ". The actual is: "+ (restStartTime+ " "+restEndTime),
//                    restBreakTime.toLowerCase().contains(restStartTime.toLowerCase())
//                            && restBreakTime.toLowerCase().contains(restEndTime.toLowerCase()), false);
            //Verify the two Add buttons in Edit column display correctly
            String action = "Edit";
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            editShiftPage.removeAllRestBreaks();
            editShiftPage.removeAllMealBreaks();
            editShiftPage.clickOnAddMealBreakButton();
            editShiftPage.clickOnAddRestBreakButton();
            //Verify the breaks time cannot out of shift time
            editShiftPage.inputMealBreakTimes("8am", "8:20am", 0);
            editShiftPage.inputRestBreakTimes("4pm", "4:30pm", 0);
            editShiftPage.clickOnUpdateButton();
            String expectedBreakMessage = "Break cannot go beyond the shift";
            List<String> mealBreakWarningMessages = editShiftPage.getMealBreakWarningMessage();
            List<String> restBreakWarningMessages = editShiftPage.getRestBreakWarningMessage();
            if (mealBreakWarningMessages.size()==0 || restBreakWarningMessages.size() ==0) {
                SimpleUtils.fail("Get break warning message fail! The actual warning message count is: " +
                        ""+mealBreakWarningMessages.size()+ " and "+restBreakWarningMessages.size(), false);
            }
            String mealBreakWarningMessage = mealBreakWarningMessages.get(0);
            String restBreakWarningMessage = restBreakWarningMessages.get(0);
            SimpleUtils.assertOnFail("The expected break message: "+expectedBreakMessage+
                            ". The actual is: "+mealBreakWarningMessage,
                    mealBreakWarningMessage.equals(expectedBreakMessage), false);
            SimpleUtils.assertOnFail("The expected break message: "+expectedBreakMessage+
                            ". The actual is: "+restBreakWarningMessage,
                    restBreakWarningMessage.equals(expectedBreakMessage), false);
            //Verify the breaks time cannot have overlapping with other breaks time
            editShiftPage.inputMealBreakTimes("10:00am", "10:30am", 0);
            editShiftPage.inputRestBreakTimes("10:00am", "10:30am", 0);
            editShiftPage.clickOnUpdateButton();
            expectedBreakMessage = "Break is overlapping with another one";
            restBreakWarningMessages = editShiftPage.getRestBreakWarningMessage();
            if (restBreakWarningMessages.size() ==0) {
                SimpleUtils.fail("Get break warning message fail! The actual warning message count is: " +
                        ""+restBreakWarningMessages.size(), false);
            }
            restBreakWarningMessage = restBreakWarningMessages.get(0);
            SimpleUtils.assertOnFail("The expected break message: "+expectedBreakMessage+
                            ". The actual is: "+restBreakWarningMessage,
                    restBreakWarningMessage.equals(expectedBreakMessage), false);
            //Verify break start time cannot after end time
            editShiftPage.inputMealBreakTimes("11:00am", "9:30am", 0);
            editShiftPage.inputRestBreakTimes("11:00am", "10:30am", 0);
            editShiftPage.clickOnUpdateButton();
            expectedBreakMessage = "Start time must be before end time";
            mealBreakWarningMessages = editShiftPage.getMealBreakWarningMessage();
            restBreakWarningMessages = editShiftPage.getRestBreakWarningMessage();
            if (mealBreakWarningMessages.size()==0 || restBreakWarningMessages.size() ==0) {
                SimpleUtils.fail("Get break warning message fail! The actual warning message count is: " +
                        ""+mealBreakWarningMessages.size()+ " and "+restBreakWarningMessages.size(), false);
            }
            mealBreakWarningMessage = mealBreakWarningMessages.get(0);
            restBreakWarningMessage = restBreakWarningMessages.get(0);
            SimpleUtils.assertOnFail("The expected break message: "+expectedBreakMessage+
                            ". The actual is: "+mealBreakWarningMessage,
                    mealBreakWarningMessage.equals(expectedBreakMessage), false);
            SimpleUtils.assertOnFail("The expected break message: "+expectedBreakMessage+
                            ". The actual is: "+restBreakWarningMessage,
                    restBreakWarningMessage.equals(expectedBreakMessage), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the functionality of edit meal breaks on single edit shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheFunctionalityOfEditMealBreaksOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            createSchedulePage.publishActiveSchedule();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.selectShiftTypeFilterByText("Assigned");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(0);
            int index = indexes.iterator().next();
//            mySchedulePage.clickOnShiftByIndex(index);
//            shiftOperatePage.clickOnEditMeaLBreakTime();
//            shiftOperatePage.verifyEditBreaks();
//            shiftOperatePage.clickOnUpdateEditShiftTimeButton();
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            //Delete all the meal and rest breaks
            editShiftPage.removeAllRestBreaks();
            editShiftPage.removeAllMealBreaks();
            //Click Update button
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Edit the shift again and check the breaks
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            //All breaks been delete successfully
            int mealBreakCount = editShiftPage.getMealBreakCount();
            int restBreakCount = editShiftPage.getRestBreakCount();
            SimpleUtils.assertOnFail("All breaks should been deleted, but actual there are "+mealBreakCount+" meal breaks and "+restBreakCount+" rest breaks", mealBreakCount == 0
                    && restBreakCount==0, false);

            //Add break and rest breaks and Set times for the new added breaks without warning message
            editShiftPage.clickOnAddMealBreakButton();
            editShiftPage.clickOnAddRestBreakButton();
            String mealBreakStartTime = "10:00 AM";
            String mealBreakEndTime = "10:20 AM";
            String restBreakStartTime = "11:00 AM";
            String restBreakEndTime = "11:30 AM";
            editShiftPage.inputMealBreakTimes(mealBreakStartTime, mealBreakEndTime, 0);
            editShiftPage.inputRestBreakTimes(restBreakStartTime, restBreakEndTime, 0);
            List<String> mealBreakWarningMessages = editShiftPage.getMealBreakWarningMessage();
            List<String> restBreakWarningMessages = editShiftPage.getRestBreakWarningMessage();
            if (mealBreakWarningMessages.size()> 0 || restBreakWarningMessages.size() >0) {
                SimpleUtils.fail("Get break warning message fail! The actual warning message count is: " +
                        ""+mealBreakWarningMessages.size()+ " and "+restBreakWarningMessages.size(), false);
            }
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            //Edit the shift again and check the breaks
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            //All breaks been added successfully
            mealBreakCount = editShiftPage.getMealBreakCount();
            restBreakCount = editShiftPage.getRestBreakCount();
            SimpleUtils.assertOnFail("All breaks should been added, but actual there are "+mealBreakCount+" meal breaks and "+restBreakCount+" rest breaks", mealBreakCount == 1
                    && restBreakCount==1, false);

            //Verify the meal and rest breaks time are display correctly in Edit column
            Map<String, String> breakTimesOnSingleEditPage = editShiftPage.getMealBreakTimes().get(0);
            String breakStartTime = breakTimesOnSingleEditPage.get("mealStartTime");
            String breakEndTime = breakTimesOnSingleEditPage.get("mealEndTime");
            SimpleUtils.assertOnFail("The expected break time is: "+mealBreakStartTime+ " "+ mealBreakEndTime
                            + ". The actual is: "+ (breakStartTime+ " "+breakEndTime),
                    mealBreakStartTime.equals(breakStartTime)
                            && mealBreakEndTime.equals(breakEndTime), false);

            Map<String, String> restTimesOnSingleEditPage = editShiftPage.getRestBreakTimes().get(0);
            String restStartTime = restTimesOnSingleEditPage.get("restStartTime");
            String restEndTime = restTimesOnSingleEditPage.get("restEndTime");
            SimpleUtils.assertOnFail("The expected rest time is: "+restBreakEndTime+ " "+ restBreakStartTime
                            + ". The actual is: "+ (restStartTime+ " "+restEndTime),
                    restBreakStartTime.equals(restStartTime)
                            && restBreakEndTime.equals(restEndTime), false);

            mealBreakStartTime = "11:00 AM";
            mealBreakEndTime = "11:20 AM";
            restBreakStartTime = "12:00 PM";
            restBreakEndTime = "12:30 PM";
            editShiftPage.inputMealBreakTimes(mealBreakStartTime, mealBreakEndTime, 0);
            editShiftPage.inputRestBreakTimes(restBreakStartTime, restBreakEndTime, 0);
            mealBreakWarningMessages = editShiftPage.getMealBreakWarningMessage();
            restBreakWarningMessages = editShiftPage.getRestBreakWarningMessage();
            if (mealBreakWarningMessages.size()> 0 || restBreakWarningMessages.size() >0) {
                SimpleUtils.fail("Get break warning message fail! The actual warning message count is: " +
                        ""+mealBreakWarningMessages.size()+ " and "+restBreakWarningMessages.size(), false);
            }
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            //Edit the shift again and check the breaks
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            //All breaks been added successfully
            mealBreakCount = editShiftPage.getMealBreakCount();
            restBreakCount = editShiftPage.getRestBreakCount();
            SimpleUtils.assertOnFail("All breaks should been added, but actual there are "+mealBreakCount+" meal breaks and "+restBreakCount+" rest breaks", mealBreakCount == 1
                    && restBreakCount==1, false);

            //Verify the meal and rest breaks time are display correctly in Edit column
            breakTimesOnSingleEditPage = editShiftPage.getMealBreakTimes().get(0);
            breakStartTime = breakTimesOnSingleEditPage.get("mealStartTime");
            breakEndTime = breakTimesOnSingleEditPage.get("mealEndTime");
            SimpleUtils.assertOnFail("The expected break time is: "+mealBreakStartTime+ " "+ mealBreakEndTime
                            + ". The actual is: "+ (breakStartTime+ " "+breakEndTime),
                    mealBreakStartTime.equals(breakStartTime)
                            && mealBreakEndTime.equals(breakEndTime), false);

            restTimesOnSingleEditPage = editShiftPage.getRestBreakTimes().get(0);
            restStartTime = restTimesOnSingleEditPage.get("restStartTime");
            restEndTime = restTimesOnSingleEditPage.get("restEndTime");
            SimpleUtils.assertOnFail("The expected rest time is: "+restBreakEndTime+ " "+ restBreakStartTime
                            + ". The actual is: "+ (restStartTime+ " "+restEndTime),
                    restBreakStartTime.equals(restStartTime)
                            && restBreakEndTime.equals(restEndTime), false);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            scheduleMainPage.saveSchedule();
            scheduleCommonPage.clickOnDayView();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            indexes.clear();
            indexes.add(0);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            //Delete all the meal and rest breaks
            editShiftPage.removeAllRestBreaks();
            editShiftPage.removeAllMealBreaks();
            //Click Update button
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            //Edit the shift again and check the breaks
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            //All breaks been delete successfully
            mealBreakCount = editShiftPage.getMealBreakCount();
            restBreakCount = editShiftPage.getRestBreakCount();
            SimpleUtils.assertOnFail("All breaks should been deleted, but actual there are "+mealBreakCount+" meal breaks and "+restBreakCount+" rest breaks", mealBreakCount == 0
                    && restBreakCount==0, false);

            //Add break and rest breaks and Set times for the new added breaks without warning message
            editShiftPage.clickOnAddMealBreakButton();
            editShiftPage.clickOnAddRestBreakButton();
            mealBreakStartTime = "10:00 AM";
            mealBreakEndTime = "10:20 AM";
            restBreakStartTime = "11:00 AM";
            restBreakEndTime = "11:30 AM";
            editShiftPage.inputMealBreakTimes(mealBreakStartTime, mealBreakEndTime, 0);
            editShiftPage.inputRestBreakTimes(restBreakStartTime, restBreakEndTime, 0);
            mealBreakWarningMessages = editShiftPage.getMealBreakWarningMessage();
            restBreakWarningMessages = editShiftPage.getRestBreakWarningMessage();
            if (mealBreakWarningMessages.size()> 0 || restBreakWarningMessages.size() >0) {
                SimpleUtils.fail("Get break warning message fail! The actual warning message count is: " +
                        ""+mealBreakWarningMessages.size()+ " and "+restBreakWarningMessages.size(), false);
            }
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            //Edit the shift again and check the breaks
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            //All breaks been added successfully
            mealBreakCount = editShiftPage.getMealBreakCount();
            restBreakCount = editShiftPage.getRestBreakCount();
            SimpleUtils.assertOnFail("All breaks should been added, but actual there are "+mealBreakCount+" meal breaks and "+restBreakCount+" rest breaks", mealBreakCount == 1
                    && restBreakCount==1, false);

            //Verify the meal and rest breaks time are display correctly in Edit column
            breakTimesOnSingleEditPage = editShiftPage.getMealBreakTimes().get(0);
            breakStartTime = breakTimesOnSingleEditPage.get("mealStartTime");
            breakEndTime = breakTimesOnSingleEditPage.get("mealEndTime");
            SimpleUtils.assertOnFail("The expected break time is: "+mealBreakStartTime+ " "+ mealBreakEndTime
                            + ". The actual is: "+ (breakStartTime+ " "+breakEndTime),
                    mealBreakStartTime.equals(breakStartTime)
                            && mealBreakEndTime.equals(breakEndTime), false);

            restTimesOnSingleEditPage = editShiftPage.getRestBreakTimes().get(0);
            restStartTime = restTimesOnSingleEditPage.get("restStartTime");
            restEndTime = restTimesOnSingleEditPage.get("restEndTime");
            SimpleUtils.assertOnFail("The expected rest time is: "+restBreakEndTime+ " "+ restBreakStartTime
                            + ". The actual is: "+ (restStartTime+ " "+restEndTime),
                    restBreakStartTime.equals(restStartTime)
                            && restBreakEndTime.equals(restEndTime), false);

            mealBreakStartTime = "11:00 AM";
            mealBreakEndTime = "11:20 AM";
            restBreakStartTime = "12:00 PM";
            restBreakEndTime = "12:30 PM";
            editShiftPage.inputMealBreakTimes(mealBreakStartTime, mealBreakEndTime, 0);
            editShiftPage.inputRestBreakTimes(restBreakStartTime, restBreakEndTime, 0);
            mealBreakWarningMessages = editShiftPage.getMealBreakWarningMessage();
            restBreakWarningMessages = editShiftPage.getRestBreakWarningMessage();
            if (mealBreakWarningMessages.size()> 0 || restBreakWarningMessages.size() >0) {
                SimpleUtils.fail("Get break warning message fail! The actual warning message count is: " +
                        ""+mealBreakWarningMessages.size()+ " and "+restBreakWarningMessages.size(), false);
            }
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            //Edit the shift again and check the breaks
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            //All breaks been added successfully
            mealBreakCount = editShiftPage.getMealBreakCount();
            restBreakCount = editShiftPage.getRestBreakCount();
            SimpleUtils.assertOnFail("All breaks should been added, but actual there are "+mealBreakCount+" meal breaks and "+restBreakCount+" rest breaks", mealBreakCount == 1
                    && restBreakCount==1, false);

            //Verify the meal and rest breaks time are display correctly in Edit column
            breakTimesOnSingleEditPage = editShiftPage.getMealBreakTimes().get(0);
            breakStartTime = breakTimesOnSingleEditPage.get("mealStartTime");
            breakEndTime = breakTimesOnSingleEditPage.get("mealEndTime");
            SimpleUtils.assertOnFail("The expected break time is: "+mealBreakStartTime+ " "+ mealBreakEndTime
                            + ". The actual is: "+ (breakStartTime+ " "+breakEndTime),
                    mealBreakStartTime.equals(breakStartTime)
                            && mealBreakEndTime.equals(breakEndTime), false);

            restTimesOnSingleEditPage = editShiftPage.getRestBreakTimes().get(0);
            restStartTime = restTimesOnSingleEditPage.get("restStartTime");
            restEndTime = restTimesOnSingleEditPage.get("restEndTime");
            SimpleUtils.assertOnFail("The expected rest time is: "+restBreakEndTime+ " "+ restBreakStartTime
                            + ". The actual is: "+ (restStartTime+ " "+restEndTime),
                    restBreakStartTime.equals(restStartTime)
                            && restBreakEndTime.equals(restEndTime), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the Role Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheRoleViolationsOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();

            //Validate the Role Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnWorkRoleSelect();
            List<String> actualWorkRoleList = editShiftPage.getOptionsFromSpecificSelect();
            editShiftPage.selectSpecificOptionByText(actualWorkRoleList.get((new Random()).nextInt(actualWorkRoleList.size())));
            editShiftPage.clickOnUpdateButton();
            boolean hasRoleViolation = false;
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("Role Violation")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                    hasRoleViolation = true;
                } else {
                    SimpleUtils.report("There is no role violation warning message display");
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            }
            scheduleMainPage.saveSchedule();
            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getTheShiftByIndex(Integer.parseInt(shiftIndexes.toArray()[0].toString())));
            if (hasRoleViolation){
                SimpleUtils.assertOnFail("The Role Violation should display! ",
                        complianceMessages.contains("Role Violation"), false);
            } else {
                SimpleUtils.assertOnFail("The Role Violation should not display! ",
                        !complianceMessages.contains("Role Violation"), false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the daily OT and DT Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheDailyOTAndDTViolationsOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Validate the daily OT Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            String inputStartTime = "10:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            String inputEndTime = "10:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.checkOrUnCheckNextDayOnBulkEditPage(false);
            editShiftPage.clickOnUpdateButton();
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("4.0 hrs daily overtime")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no daily OT violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for daily OT violation! ", false);
            scheduleMainPage.saveSchedule();
            int index = scheduleShiftTablePage.getTheIndexOfEditedShift();
            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getTheShiftByIndex(index));
            SimpleUtils.assertOnFail("The Role Violation should display! The actual message is: "+complianceMessages,
                    complianceMessages.contains("3.5 hrs daily overtime") || complianceMessages.contains("4.0 hrs daily overtime"), false);
            createSchedulePage.publishActiveSchedule();

            //Validate the daily DT Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            inputStartTime = "06:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            inputEndTime = "10:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.checkOrUnCheckNextDayOnBulkEditPage(false);
            editShiftPage.clickOnUpdateButton();
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("4.0 hrs daily doubletime")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no daily OT violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for daily OT violation! ", false);
            scheduleMainPage.saveSchedule();
            index = scheduleShiftTablePage.getTheIndexOfEditedShift();
            complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getTheShiftByIndex(index));
            SimpleUtils.assertOnFail("The Role Violation should display! The actual message is: "+complianceMessages,
                    complianceMessages.contains("4.0 hrs daily double overtime")
                            || complianceMessages.contains("3.5 hrs daily double overtime"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the weekly OT and DT Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheWeeklyOTAndDTViolationsOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            }
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int shiftCount = 0;
            while ((firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1 = shiftInfo.get(0);
                shiftCount++;
            }
            String workRole1= shiftInfo.get(4);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole1, "", "", "8:00am", "04:00pm",
                    1, Arrays.asList(0, 1, 2, 3, 4), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1);

            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            //Validate the weekly OT Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            String inputStartTime = "10:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            String inputEndTime = "10:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("will incur 2 hours of weekly overtime")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no weekly OT violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for daily OT violation! ", false);
            scheduleMainPage.saveSchedule();
            int index = scheduleShiftTablePage.getTheIndexOfEditedShift();
            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getTheShiftByIndex(index));
            SimpleUtils.assertOnFail("The weekly OT Violation should display! ",
                    complianceMessages.contains("2 hrs weekly overtime"), false);
            scheduleMainPage.clickOnCloseSearchBoxButton();

            //Validate the weekly DT Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole1, "", "", "8:00am", "04:00pm",
                    1, Arrays.asList(0, 1, 2, 3, 4, 5), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1);

            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            //Validate the weekly OT Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShifts(1);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            inputStartTime = "10:00 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            inputEndTime = "10:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("will incur 2 hours of weekly overtime")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no weekly OT violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for daily OT violation! ", false);
            scheduleMainPage.saveSchedule();
            index = scheduleShiftTablePage.getTheIndexOfEditedShift();
            complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getTheShiftByIndex(index));
            SimpleUtils.assertOnFail("The weekly OT Violation should display! ",
                    complianceMessages.contains("2 hrs weekly overtime"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the Clopening Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheClopeningViolationsOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            }
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int shiftCount = 0;
            while ((firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1 = shiftInfo.get(0);
                shiftCount++;
            }
            String workRole1= shiftInfo.get(4);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            //Validate the weekly DT Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole1, "", "", "06:00am", "8:00am",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1);
            createShiftsWithSpecificValues(workRole1, "", "", "06:00am", "11:00am",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1);

            scheduleMainPage.saveSchedule();
//            String firstNameOfTM1 = "Stephan";
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            //Validate the Clopening Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShiftsOnOneDay(1, 0);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            String inputStartTime = "06:00 PM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            String inputEndTime = "10:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("Clopening")){
                    SimpleUtils.pass("Clopening warning message displays! ");
                } else {
                    SimpleUtils.fail("There is no Cloepning violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for Clopening violation! ", false);
            scheduleMainPage.saveSchedule();

            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).get(0));
            SimpleUtils.assertOnFail("The Clopening Violation should display! ",
                    complianceMessages.contains("Clopening"), false);complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).get(0));
            SimpleUtils.assertOnFail("The Clopening Violation should display! ",
                    complianceMessages.contains("Clopening"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the Split Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheSplitViolationsOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            }
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int shiftCount = 0;
            while ((firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1 = shiftInfo.get(0);
                shiftCount++;
            }
            String workRole1= shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole1, "", "", "08:00am", "12:00pm",
                    1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1+" "+lastName);
            createShiftsWithSpecificValues(workRole1, "", "", "1:00pm", "5:00pm",
                    1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1+" "+lastName);

            scheduleMainPage.saveSchedule();
//            String firstNameOfTM1 = "Alvera";
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1.split(" ")[0]);
            //Validate the Split Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShiftsOnOneDay(1, 0);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            String inputStartTime = "7:00 PM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            String inputEndTime = "9:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.removeAllRestBreaks();
            editShiftPage.removeAllMealBreaks();
            editShiftPage.clickOnUpdateButton();
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("Split Shift")){
                    SimpleUtils.pass("Split warning message displays! ");
                } else {
                    SimpleUtils.fail("There is no Split violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for Split violation! ", false);
            scheduleMainPage.saveSchedule();
            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).get(1));
            SimpleUtils.assertOnFail("The Split Shift Violation should display! ",
                    complianceMessages.contains("Split Shift"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the Spread Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheSpreadViolationsOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            }
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int shiftCount = 0;
            while ((firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1 = shiftInfo.get(0);
                shiftCount++;
            }
            String workRole1= shiftInfo.get(4);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            //Validate the Spread Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole1, "", "", "6:00am", "7:00am",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1);
            createShiftsWithSpecificValues(workRole1, "", "", "8:00am", "11:00am",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1
            );

            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1.split(" ")[0]);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> shiftIndexes = scheduleShiftTablePage.verifyCanSelectMultipleShiftsOnOneDay(1, 0);
            scheduleShiftTablePage.rightClickOnSelectedShifts(shiftIndexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            String inputStartTime = "7:00 PM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            String inputEndTime = "11:00 PM";
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("Spread of hours")){
                    SimpleUtils.pass("Spread warning message displays! ");
                } else {
                    SimpleUtils.fail("There is no Spread violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for Spread violation! ", false);
            scheduleMainPage.saveSchedule();
            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).get(1));
            SimpleUtils.assertOnFail("The Spread Violation should display! ",
                    complianceMessages.contains("Spread of hours"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the 7th consecutive day OT Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyThe7thDayOTOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            }
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int shiftCount = 0;
            while ((firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1 = shiftInfo.get(0);
                shiftCount++;
            }
            String workRole1= shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            //Validate the 7th consecutive OT Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole1, "", "", "8:00am", "3:00pm",
                    1, Arrays.asList(0,1,2,3,4,5,6), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1+ " "+lastName);

            scheduleMainPage.saveSchedule();
//            String firstNameOfTM1 = "Jessica";
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1.split(" ")[0]);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(6);
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
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("7th day overtime")){
                    SimpleUtils.pass("Spread warning message displays! ");
                } else {
                    SimpleUtils.fail("There is no 7th day overtime violation warning message display, the actual message is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("The warning mode should display for 7th day overtime violation! ", false);
            scheduleMainPage.saveSchedule();
            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1).get(0));
            SimpleUtils.assertOnFail("The 7th day overtime Violation should display! ",
                    complianceMessages.contains("11.5 Hrs 7th day overtime"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the Meal Miss Violation on edit single shift page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheMealMissViolationOnSingleEditShiftPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6am", "6am");
            }
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            int shiftCount = 0;
            while ((firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) && shiftCount < 100) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1 = shiftInfo.get(0);
                shiftCount++;
            }
            String workRole1= shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            //Validate the Meal Miss Violation on edit single shift page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole1, "", "", "8:00am", "4:00pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstNameOfTM1+ " "+lastName);

            scheduleMainPage.saveSchedule();
//            String firstNameOfTM1 = "Jessica";
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1.split(" ")[0]);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(0);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!",
                    editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.removeAllMealBreaks();
            editShiftPage.removeAllRestBreaks();
            editShiftPage.clickOnUpdateButton();
            //https://legiontech.atlassian.net/browse/SCH-8413
//            if(newShiftPage.ifWarningModeDisplay()){
//                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
//                if (warningMessage.contains("Missed Meal Break")){
//                    SimpleUtils.pass("Missed Meal Break warning message displays! ");
//                } else {
//                    SimpleUtils.fail("There is no Missed Meal Break violation warning message display, the actual message is:"+warningMessage, false);
//                }
//                shiftOperatePage.clickOnAssignAnywayButton();
//            } else
//                SimpleUtils.fail("The warning mode should display for Missed Meal Break violation! ", false);
            scheduleMainPage.saveSchedule();
            List<String> complianceMessages = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                    (scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).get(0));
            SimpleUtils.assertOnFail("The Missed Meal Break Violation should display! ",
                    complianceMessages.contains("Missed Meal Break"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
