package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getEnterprise;

public class DragAndDropTest extends TestBase {

    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, Object[][]> kendraScott2TeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("KendraScott2TeamMembers.json");
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
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the box interaction color and message for TM status: Scheduled at home location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyWarningMessageForAlreadyScheduledAtHomeLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Select one team member to view profile
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            String userName = teamPage.selectATeamMemberToViewProfile();
            String firstName = userName.contains(" ") ? userName.split(" ")[0] : userName;

            // Go to Schedule page, Schedule tab

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Delete all the shifts that are assigned to the team member on Step #1
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            String workRole = shiftOperatePage.getRandomWorkRole();

            // Create new shift for this TM on Monday and Tuesday
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            List<Integer> dayIndexes = newShiftPage.selectDaysByCountAndCannotSelectedDate(2, "");
            //newShiftPage.selectWorkRole("MOD");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName);
            newShiftPage.clickOnOfferOrAssignBtn();

            // Save the Schedule
            scheduleMainPage.saveSchedule();
            List<Integer> shiftIndexes = scheduleShiftTablePage.getAddedShiftIndexes(firstName);
            SimpleUtils.assertOnFail("Failed to add two shifts!", shiftIndexes != null && shiftIndexes.size() == 2, false);
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(shiftIndexes.get(1));

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Drag the TM's avatar on Monday to another TM's shift on Tuesday
            scheduleShiftTablePage.dragOneAvatarToAnother(dayIndexes.get(0), firstName, dayIndexes.get(1));

            String weekday = scheduleShiftTablePage.getWeekDayTextByIndex(Integer.parseInt(shiftInfo.get(1)));
            String fullWeekDay = SimpleUtils.getFullWeekDayName(weekday);
            String expectedMessage = shiftInfo.get(0) + " is scheduled " + shiftInfo.get(6).toUpperCase() + " on " + fullWeekDay
                    + ". This shift will be converted to an open shift";
            scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(expectedMessage, "swap");
            scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(expectedMessage, "assign");
            List<String> swapData = scheduleShiftTablePage.getShiftSwapDataFromConfirmPage("swap");
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            mySchedulePage.verifyShiftsAreSwapped(swapData);

            // Delete the shifts for this TM
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);

            // Prepare the shift for this TM again
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            dayIndexes = newShiftPage.selectDaysByCountAndCannotSelectedDate(2, "");
            //newShiftPage.selectWorkRole("MOD");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName);
            newShiftPage.clickOnOfferOrAssignBtn();

            // Save the Schedule
            scheduleMainPage.saveSchedule();
            shiftIndexes = scheduleShiftTablePage.getAddedShiftIndexes(firstName);
            SimpleUtils.assertOnFail("Failed to add the shifts!", shiftIndexes != null && shiftIndexes.size() > 0, false);
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(shiftIndexes.get(1));

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Drag the TM's shift on Monday to another TM's shift on Tuesday
            scheduleShiftTablePage.dragOneShiftToAnotherDay(dayIndexes.get(0), firstName, dayIndexes.get(1));

            // Verify the warning model pops up
            expectedMessage = firstName + " is scheduled " + shiftInfo.get(6) + " on " + fullWeekDay
                    + ". This shift will be converted to an open shift";

            scheduleShiftTablePage.verifyMessageOnCopyMoveConfirmPage(expectedMessage,expectedMessage);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("Move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            // Verify if Confirm Store opening closing hour window pops up
            scheduleShiftTablePage.verifyConfirmStoreOpenCloseHours();

            if (scheduleShiftTablePage.ifMoveAnywayDialogDisplay()) {
                scheduleShiftTablePage.moveAnywayWhenChangeShift();
            } else {
                SimpleUtils.fail("MOVE ANYWAY dialog failed to load!", false);
            }
            scheduleMainPage.saveSchedule();
            scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(dayIndexes.get(0), firstName, dayIndexes.get(1));
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the box interaction color and message when TM is from another store and is already scheduled at this store")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyWarningMsgForTMFromAnotherStoreScheduledAtCurrentLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Controls Scheduling Policies page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsYes();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);

            // Set "Can a manager add another locations' employee in schedule before the employee's home location has published the schedule?" to "Yes, anytime"
            configurationPage.goToConfigurationPage();
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
            controlsNewUIPage.updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule("Yes, anytime");
            controlsNewUIPage.clickOnGlobalLocationButton();

            configurationPage.setWFS("Yes");
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();

            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String anotherLocation = "";
            if (getEnterprise().equalsIgnoreCase("KendraScott2")) {
                 anotherLocation = "AUSTIN DOWNTOWN";
            } else {
                anotherLocation = "7500216 - Can-Ski Village";
            }
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(anotherLocation);
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Select one team member to view profile
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            String userName = teamPage.selectATeamMemberToViewProfile();
            String firstName = userName.contains(" ") ? userName.split(" ")[0] : userName;

            // Go to Schedule page, Schedule tab
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            // Go to Dashboard page
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Change the location to the original location
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Delete all the shifts that are assigned to the team member on Step #1
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            String workRole = shiftOperatePage.getRandomWorkRole();

            // Create new shift for this TM on Monday and Tuesday
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            List<Integer> dayIndexes = newShiftPage.selectDaysByCountAndCannotSelectedDate(2, "");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");

            // Save the Schedule
            scheduleMainPage.saveSchedule();
            List<Integer> shiftIndexes = scheduleShiftTablePage.getAddedShiftIndexes(firstName);
            SimpleUtils.assertOnFail("Failed to add two shifts!", shiftIndexes != null && shiftIndexes.size() > 0, false);
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(shiftIndexes.get(1));

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Drag the TM's avatar on Monday to another TM's shift on Tuesday
            scheduleShiftTablePage.dragOneAvatarToAnother(dayIndexes.get(0), firstName, dayIndexes.get(1));

            String weekday = scheduleShiftTablePage.getWeekDayTextByIndex(Integer.parseInt(shiftInfo.get(1)));
            String fullWeekDay = SimpleUtils.getFullWeekDayName(weekday);
            String expectedMessage = shiftInfo.get(0) + " is scheduled " + shiftInfo.get(6).toUpperCase() + " on " + fullWeekDay
                    + ". This shift will be converted to an open shift";
            scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(expectedMessage, "swap");
            scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(expectedMessage, "assign");
            List<String> swapData = scheduleShiftTablePage.getShiftSwapDataFromConfirmPage("swap");
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            mySchedulePage.verifyShiftsAreSwapped(swapData);
            scheduleMainPage.clickOnCancelButtonOnEditMode();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the box interaction color and message for TM status: Time Off")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyWarningModelForTimeOffAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            String userName = teamPage.selectATeamMemberToViewProfile();
            String firstName = userName.contains(" ") ? userName.split(" ")[0] : userName;
            String lastName = userName.contains(" ") ? userName.split(" ")[1] : userName;
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String myTimeOffLabel = "Time Off";
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            teamPage.rejectAllTheTimeOffRequests();
            profileNewUIPage.clickOnCreateTimeOffBtn();
            SimpleUtils.assertOnFail("New time off request window not loaded Successfully!", profileNewUIPage.isNewTimeOffWindowLoaded(), false);
            String timeOffReasonLabel = "JURY DUTY";
            profileNewUIPage.selectTimeOffReason(timeOffReasonLabel);
            String timeOffDate = profileNewUIPage.selectStartAndEndDateAtSameDay();
            profileNewUIPage.clickOnSaveTimeOffRequestBtn();


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Navigate to the week that contains the date that provided
            scheduleCommonPage.goToSpecificWeekByDate(timeOffDate);

            // Ungenerate and create the schedule
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            // Edit schedule to create the new shift for new TM
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            String workRole = shiftOperatePage.getRandomWorkRole();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            List<Integer> indexes = newShiftPage.selectDaysByCountAndCannotSelectedDate(1, timeOffDate);
            //newShiftPage.selectWorkRole("MOD");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstName + " " + lastName.substring(0, 1));
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            // Drag the shift to the time off day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int endIndex = scheduleShiftTablePage.getTheIndexOfTheDayInWeekView(timeOffDate.substring(timeOffDate.length() - 2));
            scheduleShiftTablePage.dragOneAvatarToAnother(indexes.get(0), firstName, endIndex);

            // Verify the warning model pops up and Click on OK button
            shiftOperatePage.verifyWarningModelForAssignTMOnTimeOff(firstName);

            // Drag the TM's shift to the day that he/she has time off
            scheduleShiftTablePage.dragOneShiftToAnotherDay(indexes.get(0), firstName, endIndex);

            // Verify the Warning model pops up with the message
            String expectedMsg = firstName + " is approved for Time Off";
            scheduleShiftTablePage.verifyMessageOnCopyMoveConfirmPage(expectedMsg, expectedMsg);
            String copyOption = "Copy";
            String moveOption = "Move";
            scheduleShiftTablePage.verifyConfirmBtnIsDisabledForSpecificOption(copyOption);
            scheduleShiftTablePage.verifyConfirmBtnIsDisabledForSpecificOption(moveOption);
            shiftOperatePage.clickOnCancelEditShiftTimeButton();

            // Verify nothing happens after clicking CANCEL button
            if (scheduleShiftTablePage.verifyDayHasShiftByName(indexes.get(0), firstName) == 1 && scheduleShiftTablePage.verifyDayHasShiftByName(endIndex, firstName) == 0)
                SimpleUtils.pass("Nothing happens as expected after clicking OK button");
            else
                SimpleUtils.fail("The TM's shift may be assigned unexpected", false);
            scheduleMainPage.saveSchedule();

            // Clean up data
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.searchAndSelectTeamMemberByName(firstName);
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            teamPage.rejectAllTheTimeOffRequests();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the box interaction color and message when TM status is overtime")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySwapWarningModelForOvertimeAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            String userName1 = teamPage.selectATeamMemberToViewProfile();
            String TM1 = userName1.contains(" ") ? userName1.split(" ")[0] : userName1;
            teamPage.goToTeam();
            String userName2 = teamPage.selectATeamMemberToViewProfile();
            String TM2 = userName2.contains(" ") ? userName2.split(" ")[0] : userName2;


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // recreate the schedule
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            // Edit schedule to create the new shifts for new TM1 and TM2
            //String TM1 = "John";
            //String TM2 = "Pat";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(TM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(TM2);
            String workRole = shiftOperatePage.getRandomWorkRole();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            //newShiftPage.selectWorkRole("EVENT MANAGER");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(2);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(TM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            //newShiftPage.selectWorkRole("EVENT MANAGER");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(TM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(1,TM1,0,TM2);
            List<String> swapData = scheduleShiftTablePage.getShiftSwapDataFromConfirmPage("swap");
            String expectedMessage = TM1+" will incur 1 hours of overtime";
            SimpleUtils.report(expectedMessage);
            scheduleShiftTablePage.verifyMessageInConfirmPage(expectedMessage, expectedMessage);
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            mySchedulePage.verifyShiftsAreSwapped(swapData);

            //verify cancel button
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(1,TM2,0,TM1);
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(1,TM1,0,TM2);
            scheduleShiftTablePage.clickCancelBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,TM1)==1 && scheduleShiftTablePage.verifyDayHasShiftByName(1,TM1)==1){
                SimpleUtils.pass("cancel successfully!");
            }

            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(1,TM1,0,TM2);
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,TM1)==2 && scheduleShiftTablePage.verifyDayHasShiftByName(1,TM1)==1){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(TM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(TM2);
            scheduleMainPage.saveSchedule();

            //verify change shift
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            //newShiftPage.selectWorkRole("EVENT MANAGER");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clearAllSelectedDays();
            //newShiftPage.selectDaysByIndex(0, 0, 2);
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(TM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            //newShiftPage.selectWorkRole("EVENT MANAGER");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1, 1, 1);
            //newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(TM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneShiftToAnotherDay(1,TM1,0);
            if (scheduleShiftTablePage.ifMoveAnywayDialogDisplay()){
                scheduleShiftTablePage.moveAnywayWhenChangeShift();
            }
            scheduleMainPage.saveSchedule();
            expectedMessage = "1 hrs daily overtime";
            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, TM1);
            SimpleUtils.assertOnFail("Get "+TM1+"'s shift failed",shiftsOfFirstDay.size()>0, false);
            String actualMessage=null;
            for (String s:scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(shiftsOfFirstDay.size()-1))){
                actualMessage = actualMessage+s;
            }
            SimpleUtils.assertOnFail("overtime comliance message display failed",
                    actualMessage.toString().contains(expectedMessage), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the box interaction color and message when SM tries to assign TM to an open shift that overlaps a time TM is already assigned to")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyWarningMessageWhenAssignTMToOpenShiftThatTMIsAlreadyAssignedToAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Select one team member to view profile
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            String userName = teamPage.selectATeamMemberToViewProfile();
            String firstName = userName.contains(" ") ? userName.split(" ")[0] : userName;

            // Go to Schedule page, Schedule tab, navigate to next week

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("05:00AM","11:00PM");
            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Delete all the shifts that are assigned to the team member on Step #1
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            String workRole = shiftOperatePage.getRandomWorkRole();

            // Create 2 new shifts for this TM on Monday and Tuesday
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            List<Integer> dayIndexes = newShiftPage.selectDaysByCountAndCannotSelectedDate(2, "");
            //newShiftPage.selectWorkRole("MOD");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(userName);
            newShiftPage.clickOnOfferOrAssignBtn();

            // Create an open shift on Tuesday
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            if (dayIndexes.size() == 2)
                newShiftPage.selectWorkingDaysOnNewShiftPageByIndex(dayIndexes.get(1));
            else
                newShiftPage.selectWorkingDaysOnNewShiftPageByIndex(1);
            //newShiftPage.selectWorkRole("MOD");
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();

            // Save the Schedule
            scheduleMainPage.saveSchedule();
            List<Integer> shiftIndexes = scheduleShiftTablePage.getAddedShiftIndexes(firstName);
            SimpleUtils.assertOnFail("Failed to add two shifts for the TM!", shiftIndexes != null && shiftIndexes.size() == 2, false);
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(shiftIndexes.get(1));
            List<Integer> openShiftIndexes = scheduleShiftTablePage.getAddedShiftIndexes("Open");
            SimpleUtils.assertOnFail("Failed to add an open shift!", openShiftIndexes != null && openShiftIndexes.size() == 1, false);
            List<String> openShiftInfo = scheduleShiftTablePage.getOpenShiftInfoByIndex(openShiftIndexes.get(0));

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Drag the TM's avatar on Monday to the open shift on Tuesday
            String weekday = scheduleShiftTablePage.getWeekDayTextByIndex(Integer.parseInt(shiftInfo.get(1)));
            String fullWeekDay = SimpleUtils.getFullWeekDayName(weekday);
            String expectedMessage = shiftInfo.get(0) + " is scheduled " + shiftInfo.get(6).toUpperCase() + " on " + fullWeekDay
                    + ". This shift will be converted to an open shift";
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstName,1,"Open");
            scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(expectedMessage,"assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            // Verify the shift is assigned
            List<String> shiftInfoAfterDrag = scheduleShiftTablePage.getTheShiftInfoByIndex(shiftIndexes.get(1));
            List<String> openShiftInfoAfterDrag = scheduleShiftTablePage.getOpenShiftInfoByIndex(openShiftIndexes.get(0));

            if (shiftInfoAfterDrag.get(2).replaceAll(" ", "").equals(openShiftInfo.get(0).replaceAll(" ", ""))
                    && shiftInfoAfterDrag.get(4).equals(openShiftInfo.get(1)) && openShiftInfoAfterDrag.get(0).replaceAll(" ", "").equals(shiftInfo.get(2).replaceAll(" ", "")))
                SimpleUtils.pass("Assign a TM to an open shift that overlaps a time TM is already assigned to successfully!");
            else
                SimpleUtils.fail("Failed to assign a TM to an open shift that overlaps a time TM is already assigned to",false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the box interaction color and message when TM will incur clopening")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWarningModelForClopeningAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            //turn on clopening toggle and set hours
            controlsNewUIPage.selectClopeningHours(12);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();

            // Go to Schedule page, Schedule tab

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "09:00AM", "11:00PM");
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRoleOfTM1 = shiftInfo.get(4);
            while (shiftInfo.get(0).equalsIgnoreCase(firstNameOfTM1) || shiftInfo.get(0).equalsIgnoreCase("open")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            }
            String firstNameOfTM2 = shiftInfo.get(0);
            String workRoleOfTM2 = shiftInfo.get(4);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            Thread.sleep(5000);
            teamPage.searchAndSelectTeamMemberByName(firstNameOfTM1);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String myTimeOffLabel = "Time Off";
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            teamPage.rejectAllTheTimeOffRequests();
            Thread.sleep(5000);
            teamPage.goToTeam();
            Thread.sleep(5000);
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
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Create new shift for TM1 on Monday and Wednesday
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(0, 0, 2);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            Thread.sleep(5000);
            //Create new shift for TM2 on Tuesday
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("8:30pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1, 1, 1);
            newShiftPage.selectWorkRole(workRoleOfTM2);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();

            // Save the Schedule
            scheduleMainPage.saveSchedule();

            // Edit the Schedule and try to drag TM1 on Monday to TM2 on Tuesday
            String clopeningWarningMessage = " will incur clopening";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int i=0;
            while (!scheduleShiftTablePage.isDragAndDropConfirmPageLoaded() && i<5){
                scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM1,1,firstNameOfTM2);
                i++;
                Thread.sleep(2000);
            }
            SimpleUtils.assertOnFail("Clopening message display incorrectly on swap section! The expected is: "+firstNameOfTM1 + clopeningWarningMessage,
                    scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(firstNameOfTM1 + clopeningWarningMessage, "swap"), false);
            SimpleUtils.assertOnFail("Clopening message display incorrectly on assign section! The expected is: "+firstNameOfTM1 + clopeningWarningMessage,
                    scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(firstNameOfTM1 + clopeningWarningMessage, "assign"), false);

            // Swap TM1 and TM2, check the TMs been swapped successfully
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM1);
            scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM2);
            scheduleMainPage.saveSchedule();
            //check compliance smart card display
            SimpleUtils.assertOnFail("Compliance smart card display successfully!",
                    smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
            smartCardPage.clickViewShift();

            //check the violation on the info popup
            List<WebElement> shiftsOfTuesday = scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1);
            SimpleUtils.assertOnFail("Get compliance shift failed",shiftsOfTuesday.size()>0, false);

            List<WebElement> shiftsOfWednesday = scheduleShiftTablePage.getOneDayShiftByName(2, firstNameOfTM1);
            SimpleUtils.assertOnFail("Get compliance shift failed",shiftsOfWednesday.size()>0, false);

            SimpleUtils.assertOnFail("Clopening compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfTuesday.get(shiftsOfTuesday.size()-1)).contains("Clopening"), false);

            SimpleUtils.assertOnFail("Clopening compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfWednesday.get(0)).contains("Clopening"), false);

            // Swap TM1 and TM2 back, check the TMs been swapped successfully
            smartCardPage.clickViewShift();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            i=0;
            while (!scheduleShiftTablePage.isDragAndDropConfirmPageLoaded() && i<5){
                scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM2,1,firstNameOfTM1);
                i++;
                Thread.sleep(2000);
            }

            SimpleUtils.assertOnFail("Clopening message is not display because there should no clopening !",
                    !scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(firstNameOfTM1 + clopeningWarningMessage, "swap"), false);
            SimpleUtils.assertOnFail("Clopening message is not display because there should no clopening !",
                    !scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(firstNameOfTM1 + clopeningWarningMessage, "assign"), false);

            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM1);
            scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM2);
            scheduleMainPage.saveSchedule();
            // Edit the Schedule and try to drag TM1 on Monday to TM2 on Tuesday again
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            i=0;
            while (!scheduleShiftTablePage.isDragAndDropConfirmPageLoaded() && i<5){
                scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM1,1,firstNameOfTM2);
                i++;
                Thread.sleep(2000);
            }
            SimpleUtils.assertOnFail("Clopening message display successfully on swap section!",
                    scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(firstNameOfTM1 + clopeningWarningMessage, "swap"), false);
            SimpleUtils.assertOnFail("Clopening message display successfully on assign section!",
                    scheduleShiftTablePage.verifySwapAndAssignWarningMessageInConfirmPage(firstNameOfTM1 + clopeningWarningMessage, "assign"), false);

            // Swap TM1 and TM2, check the TMs been swapped successfully
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM1);
            scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM2);
            scheduleMainPage.saveSchedule();

            //check compliance smart card display
            SimpleUtils.assertOnFail("Compliance smart card display successfully!",
                    smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
            smartCardPage.clickViewShift();

            //check the violation on the info popup
            shiftsOfTuesday = scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1);
            SimpleUtils.assertOnFail("Get compliance shift failed",shiftsOfTuesday.size()>0, false);

            shiftsOfWednesday = scheduleShiftTablePage.getOneDayShiftByName(2, firstNameOfTM1);
            SimpleUtils.assertOnFail("Get compliance shift failed",shiftsOfWednesday.size()>0, false);

            SimpleUtils.assertOnFail("Clopening compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfTuesday.get(shiftsOfTuesday.size()-1)).contains("Clopening"), false);

            SimpleUtils.assertOnFail("Clopening compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfWednesday.get(0)).contains("Clopening"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the box interaction color and message when TM is already scheduled during this time at another location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWarningModeWhenTMIsScheduledAtAnotherLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            String anotherLocation = "New York Central Park";
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeLocation(anotherLocation);
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
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            List<String> shiftInfo = new ArrayList<>();
            while (shiftInfo.size() ==0) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            }
//        List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            String workRoleOfTM1 = shiftInfo.get(4);
            // Delete all the shifts that are assigned to the team member
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);

            // Create new shift for TM on first day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            String timeInfo = newShiftPage.getTimeDurationWhenCreateNewShift();
            String dayInfo = newShiftPage.getSelectedDayInfoFromCreateShiftPage().get(0);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

//        DashboardPage dashboardPage2 = pageFactory.createConsoleDashboardPage();
            dashboardPage.navigateToDashboard();
            locationSelectorPage.changeLocation("AUSTIN DOWNTOWN");
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
//        SchedulePage schedulePage2 = pageFactory.createConsoleScheduleNewUIPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            boolean isWeekGenerated2 = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated2){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);

            // Create new shift for TM on first day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1, 1, 1);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneShiftToAnotherDay(1, firstNameOfTM1, 0);
            String expectedWarningMesage = firstNameOfTM1+" is already scheduled to work at" +
                    anotherLocation + " at " + timeInfo + " " + dayInfo.replace("\n",", ") +
                    "\n" + "Please contact " +anotherLocation+" to change assignment.";
            String actualwarningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
            expectedWarningMesage.equalsIgnoreCase(actualwarningMessage);
            scheduleShiftTablePage.clickOnOkButtonInWarningMode();
            List<WebElement> shiftsOfFirstDay= scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1);
            SimpleUtils.assertOnFail("Get compliance shift failed",shiftsOfFirstDay.size()==0, false);

            List<WebElement> shiftsOfSecondDay = scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1);
            SimpleUtils.assertOnFail("Get compliance shift failed",shiftsOfSecondDay.size()>0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate Assignment rule violation with Yes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySwapWarningModelForRoleViolationConfigYesAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            //controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsYes();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();
            refreshCachesAfterChangeTemplate();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            //edit schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int i = 0;
            List<String> shiftInfo = new ArrayList<>();
            while (i< 50 &&shiftInfo.size() == 0) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
                i++;
            }
            String firstNameOfTM1 = shiftInfo.get(0);
            String workRoleOfTM1 = shiftInfo.get(4);
            List<String> shiftInfo2 = new ArrayList<>();
            i = 0;
            while (i < 50 && (shiftInfo2.size() == 0 || workRoleOfTM1.equals(shiftInfo2.get(4)))) {
                shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                i++;
            }
            String firstNameOfTM2 = shiftInfo2.get(0);
            String workRoleOfTM2 = shiftInfo2.get(4);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(0, 0, 0);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            SimpleUtils.report("teammember1: "+ firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRoleOfTM2);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1, 1, 1);
//            newShiftPage.moveSliderAtSomePoint("8", 0, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            SimpleUtils.report("teammember2: "+ firstNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0, firstNameOfTM1, 1, firstNameOfTM2);
            scheduleShiftTablePage.verifyConfirmStoreOpenCloseHours();
            String expectedViolationMessage = firstNameOfTM1+" should not take a "+workRoleOfTM2+" shift";
            scheduleShiftTablePage.verifyMessageInConfirmPage(expectedViolationMessage,expectedViolationMessage);
            List<String> swapData = scheduleShiftTablePage.getShiftSwapDataFromConfirmPage("swap");
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            mySchedulePage.verifyShiftsAreSwapped(swapData);

            //assign option
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(0, 0, 0);
//            newShiftPage.moveSliderAtSomePoint("8", 0, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRoleOfTM2);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1, 1, 1);
//            newShiftPage.moveSliderAtSomePoint("8", 0, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(2000);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0, firstNameOfTM1, 1, firstNameOfTM2);
            scheduleShiftTablePage.verifyConfirmStoreOpenCloseHours();
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM1)==1 && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM1)==1){
                SimpleUtils.pass("assign successfully!");
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate Assignment rule violation with No")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySwapWarningModelForRoleViolationConfigNoAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            //controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsNo();


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            //edit schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> shiftInfo = new ArrayList<>();
            while (shiftInfo.size() == 0) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
            }
            String firstNameOfTM1 = shiftInfo.get(0);
            String workRoleOfTM1 = shiftInfo.get(4);
            List<String> shiftInfo2 = new ArrayList<>();
            while (shiftInfo2.size() == 0 || workRoleOfTM1.equals(shiftInfo2.get(4))) {
                shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            }
            String firstNameOfTM2 = shiftInfo2.get(0);
            String workRoleOfTM2 = shiftInfo2.get(4);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRoleOfTM2);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1, 1, 1);
//            newShiftPage.moveSliderAtSomePoint("8", 0, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0, firstNameOfTM1, 1, firstNameOfTM2);
            String expectedViolationMessage ="This assignment will trigger a role violation.\n" +
                    firstNameOfTM1+" "+shiftInfo.get(5)+" can not take a "+workRoleOfTM2+" shift\n";
            String actualwarningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
            if (expectedViolationMessage.equalsIgnoreCase(actualwarningMessage)){
                SimpleUtils.pass("violation warning message is expected!");
            } else {
                SimpleUtils.fail("violation warning message is not expected! the actual is: " + actualwarningMessage+" expected: "+ expectedViolationMessage,true);
            }
            scheduleShiftTablePage.clickOnOkButtonInWarningMode();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM1)==1 && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM2)==1){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();

            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), true);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), true);
            //controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
            controlsNewUIPage.enableOverRideAssignmentRuleAsYes();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the middle days of the week to copy shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToAMiddleDayToCopyShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        List<String> shiftInfo = new ArrayList<>();
        while (shiftInfo.size() == 0) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        }
        String firstNameOfTM1 = shiftInfo.get(0);
        //String workRoleOfTM1 = shiftInfo.get(4);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 3 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,3);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,3);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the middle days of the week to copy shift(blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToABlankMiddleDayToCopyShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleCommonPage.navigateDayViewWithIndex(3);
        scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        List<String> shiftInfo = new ArrayList<>();
        while (shiftInfo.size() == 0) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        }
        String firstNameOfTM1 = shiftInfo.get(0);
        //String workRoleOfTM1 = shiftInfo.get(4);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 3 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,3);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,3);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the middle days of the week to move shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToAMiddleDayToMoveShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        List<String> shiftInfo = new ArrayList<>();
        while (shiftInfo.size() == 0) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        }
        String firstNameOfTM1 = shiftInfo.get(0);
        //String workRoleOfTM1 = shiftInfo.get(4);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 3 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,3);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,3);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the middle days of the week to move shift(blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToABlankMiddleDayToMoveShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleCommonPage.navigateDayViewWithIndex(3);
        scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        List<String> shiftInfo = new ArrayList<>();
        while (shiftInfo.size() == 0) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        }
        String firstNameOfTM1 = shiftInfo.get(0);
        //String workRoleOfTM1 = shiftInfo.get(4);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 3 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,3);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,3);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the first day of the week to copy shift(not blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheFirstDayToCopyShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(6);;
        scheduleShiftTablePage.dragOneShiftToAnotherDay(6, firstNameOfTM1, 0 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(6,firstNameOfTM1,0);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(6,firstNameOfTM1,0);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the first day of the week to copy shift(blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheBlankFirstDayToCopyShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleCommonPage.navigateDayViewWithIndex(0);
        scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(6);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(6, firstNameOfTM1, 0 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(6,firstNameOfTM1,0);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(6,firstNameOfTM1,0);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the first day of the week to move shift(not blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheFirstDayToMoveShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(6);;
        scheduleShiftTablePage.dragOneShiftToAnotherDay(6, firstNameOfTM1, 0 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(6,firstNameOfTM1,0);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(6,firstNameOfTM1,0);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the first day of the week to move shift(blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheBlankFirstDayToMoveShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleCommonPage.navigateDayViewWithIndex(0);
        scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(6);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(6, firstNameOfTM1, 0 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(6,firstNameOfTM1,0);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(6,firstNameOfTM1,0);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the last day of the week to copy shift(not blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheLastDayToCopyShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(0);;
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 6 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,6);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,6);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the last day of the week to copy shift(blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheBlankLastDayToCopyShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleCommonPage.navigateDayViewWithIndex(6);
        scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(0);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 6 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,6);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,6);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the last day of the week to move shift(not blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheLastDayToMoveShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(0);;
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 6 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,6);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,6);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the last day of the week to move shift(blank day)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToTheBlankLastDayToMoveShiftAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleCommonPage.navigateDayViewWithIndex(6);
        scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        String firstNameOfTM1 = scheduleShiftTablePage.getNameOfTheFirstShiftInADay(0);
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 6 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,6);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(0,firstNameOfTM1,6);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the another day of the week to copy shift will occur overlapping shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToCopyGetOverlappingShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        List<String> shiftInfo = new ArrayList<>();
        while (shiftInfo.size() == 0) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        }
        String firstNameOfTM1 = shiftInfo.get(0);
        String weekday = scheduleShiftTablePage.getWeekDayTextByIndex(1);
        String fullWeekDay = SimpleUtils.getFullWeekDayName(weekday);
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleCommonPage.navigateDayViewWithIndex(1);
        scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 1 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 1 );
        scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
        // Verify the warning model pops up
        String expectedMessage = firstNameOfTM1 + " is scheduled " + shiftInfo.get(6) + " on " + fullWeekDay
                + ". This shift will be converted to an open shift";
        scheduleShiftTablePage.verifyMessageOnCopyMoveConfirmPage(expectedMessage,expectedMessage);
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
        //Check copy anyway button and result.
        if (scheduleShiftTablePage.ifMoveAnywayDialogDisplay()) {
            scheduleShiftTablePage.moveAnywayWhenChangeShift();
        } else {
            SimpleUtils.fail("Copy ANYWAY dialog failed to load!", false);
        }

        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,1);
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,"open",1);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM1,1);
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,"open",1);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "validate drag and drop to the same day of the week to copy shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragDropToToTheSameDayAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        List<String> shiftInfo = new ArrayList<>();
        while (shiftInfo.size() == 0) {
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
        }
        String firstNameOfTM1 = shiftInfo.get(0);
        //edit schedule
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Action Required");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("open");
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.dragOneShiftToAnotherDay(0, firstNameOfTM1, 0 );

        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,"open",0);
        scheduleMainPage.saveSchedule();
        scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,"open",0);
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the items on bulk drag and drop confirm change popup")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheItemsOnBulkDragAndDropConfirmPopupAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(1);
            Map<String, String> dayInfo = scheduleCommonPage.getActiveDayInfo();
            String fullWeekDay = SimpleUtils.getFullWeekDayName(dayInfo.get("weekDay"));
            String fullMonth = mySchedulePage.getFullMonthName(dayInfo.get("month"));
            String day = dayInfo.get("day");
            scheduleCommonPage.clickOnWeekView();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            List<WebElement> selectedShifts = scheduleShiftTablePage.selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 0);
            //Drag the selected shifts to next day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            //Verify the confirm change info display correctly
            String expectedInfo = "Would you like to move or copy the "+selectedShiftCount+" Selected shifts to "+fullWeekDay+", "+fullMonth+" "+ day+ "?";
            String actualInfo = scheduleShiftTablePage.getBulkDragAndDropConfirmChangeInfo();
            SimpleUtils.assertOnFail("The expected message is: "+expectedInfo
                            + " The actual message is: "+actualInfo,
                    expectedInfo.equals(actualInfo), false);
            //Verify the Move shifts and Copy Shifts radio buttons display and clickable
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");

            //Verify the the compliance and conflict switches should display and could enable or disable
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(false);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(false);

            //Verify the Cancel and Confirm buttons are display and clickable
            scheduleShiftTablePage.clickCancelBtnOnDragAndDropConfirmPage();
            SimpleUtils.assertOnFail("The bulk drag and drop confirm change modal should not display! ",
                    !scheduleShiftTablePage.checkIfBulkDragAndDropConfirmChangeModalDisplay(), false);
            selectedShifts = scheduleShiftTablePage.selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 0);
            //Drag the selected shifts to next day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            SimpleUtils.assertOnFail("The bulk drag and drop confirm change modal should not display! ",
                    !scheduleShiftTablePage.checkIfBulkDragAndDropConfirmChangeModalDisplay(), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate open shifts will been created when drag and drop shifts to same day and same location on non-LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkDragAndDropShiftsToSameDayAndSameLocationOnNonLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);
            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, false);

            //Two open shifts been created
            List<WebElement> newAddedShifts = scheduleShiftTablePage.getOneDayShiftByName(1, "open");
            SimpleUtils.assertOnFail("The expected new added shifts count is "+selectedShiftCount
                            + " The actual new added shift count is:"+newAddedShifts.size(),
                    newAddedShifts.size()==2, false);

            scheduleMainPage.saveSchedule();
            newAddedShifts = scheduleShiftTablePage.getOneDayShiftByName(1, "open");
            SimpleUtils.assertOnFail("The expected new added shifts count is "+selectedShiftCount
                            + " The actual new added shift count is:"+newAddedShifts.size(),
                    newAddedShifts.size()==2, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate shifts can be moved to another day and same location on non-LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyMoveShiftsToAnotherDayAndSameLocationOnNonLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int firstDayShiftCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            int secondDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 3;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);

            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Drag the selected shifts to another day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

            //Select move option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("move");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            int secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+allShiftsCountBefore
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == allShiftsCountBefore
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ (firstDayShiftCountBefore-selectedShiftCount)
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , (firstDayShiftCountBefore-selectedShiftCount) == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);

            }
            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+allShiftsCountBefore
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == allShiftsCountBefore
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ (firstDayShiftCountBefore-selectedShiftCount)
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , (firstDayShiftCountBefore-selectedShiftCount) == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);

            }
            //Verify changes can be published
            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+allShiftsCountBefore
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == allShiftsCountBefore
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ (firstDayShiftCountBefore-selectedShiftCount)
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , (firstDayShiftCountBefore-selectedShiftCount) == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);

            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate shifts can be copied to another day and same location on non-LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCopyShiftsToAnotherDayAndSameLocationOnNonLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int firstDayShiftCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            int secondDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 3;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);

            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Drag the selected shifts to another day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

            //Select move option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("copy");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            int secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+(allShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == (allShiftsCountBefore+selectedShiftCount)
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ firstDayShiftCountBefore
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , firstDayShiftCountBefore == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size() >0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() >0, false);

            }
            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+(allShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == (allShiftsCountBefore+selectedShiftCount)
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ firstDayShiftCountBefore
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , firstDayShiftCountBefore == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size() >0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() >0, false);

            }
            //Verify changes can be published
            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+(allShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == (allShiftsCountBefore+selectedShiftCount)
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ firstDayShiftCountBefore
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , firstDayShiftCountBefore == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size() >0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() >0, false);

            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the shifts cannot be moved or copied to close day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsCannotBeMovedOrCopiedToCloseDayAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            List<String> weekDaysToClose = new ArrayList<>();
            weekDaysToClose.add("Sunday");
            createSchedulePage.createScheduleForNonDGByWeekInfo("SUGGESTED", weekDaysToClose, null);
            int targetDayIndex = teamPage.getWeekDayIndexByTitle("Sun");
            int fromDayIndex;
            if (targetDayIndex ==6) {
                fromDayIndex = targetDayIndex-1;
            } else
                fromDayIndex = targetDayIndex+1;

            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();

            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 3;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, fromDayIndex);

            //Drag the selected shifts to another day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, targetDayIndex, false);

            //Check the close day popup will display
            SimpleUtils.assertOnFail("The warning modal should be loaded! ",
                    newShiftPage.ifWarningModeDisplay(), false);

            newShiftPage.clickOnOkButtonOnWarningModal();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+(allShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == allShiftsCountBefore
                    , false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate move multiple shifts on different day and same location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyMoveMultipleShiftsOnDifferentDayAndSomeLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int targetDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Press ctrl and click multiple shifts with different TMs at the different day and same location
            int selectedShiftCount = 3;
            int targetDayIndex = 6;
            List<WebElement> selectedShifts = scheduleShiftTablePage.selectMultipleDifferentAssignmentShifts(selectedShiftCount);
            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Move them to one day and same location
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, targetDayIndex, true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shifts on target day
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int targetDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            SimpleUtils.assertOnFail("The all shifts count should not change after move shift. The expected is:"+allShiftsCountBefore
                    + " The actual is: "+allShiftsCountAfter, allShiftsCountBefore == allShiftsCountAfter, false);

            SimpleUtils.assertOnFail("The target shifts count should be:"+(targetDayShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+targetDayShiftsCountAfter, (targetDayShiftsCountBefore+selectedShiftCount) == targetDayShiftsCountAfter, false);

            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(6, shiftNames.get(i)).size() >0, false);
            }

            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            targetDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            SimpleUtils.assertOnFail("The all shifts count should not change after move shift. The expected is:"+allShiftsCountBefore
                    + " The actual is: "+allShiftsCountAfter, allShiftsCountBefore == allShiftsCountAfter, false);

            SimpleUtils.assertOnFail("The target shifts count should be:"+(targetDayShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+targetDayShiftsCountAfter, (targetDayShiftsCountBefore+selectedShiftCount) == targetDayShiftsCountAfter, false);

            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(6, shiftNames.get(i)).size() >0, false);
            }

            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            targetDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            SimpleUtils.assertOnFail("The all shifts count should not change after move shift. The expected is:"+allShiftsCountBefore
                    + " The actual is: "+allShiftsCountAfter, allShiftsCountBefore == allShiftsCountAfter, false);

            SimpleUtils.assertOnFail("The target shifts count should be:"+(targetDayShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+targetDayShiftsCountAfter, (targetDayShiftsCountBefore+selectedShiftCount) == targetDayShiftsCountAfter, false);

            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(6, shiftNames.get(i)).size() >0, false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate copy multiple shifts on different day and same location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCopyMultipleShiftsOnDifferentDayAndSomeLocationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int targetDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Press ctrl and click multiple shifts with different TMs at the different day and same location
            int selectedShiftCount = 3;
            int targetDayIndex = 6;
            List<WebElement> selectedShifts = scheduleShiftTablePage.selectMultipleDifferentAssignmentShifts(selectedShiftCount);
            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Move them to one day and same location
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, targetDayIndex, true);
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("Copy");
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shifts on target day
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int targetDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            SimpleUtils.assertOnFail("The all shifts count be:"+(allShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+allShiftsCountAfter, (allShiftsCountBefore+selectedShiftCount) == allShiftsCountAfter, false);

            SimpleUtils.assertOnFail("The target shifts count should be:"+(targetDayShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+targetDayShiftsCountAfter, (targetDayShiftsCountBefore+selectedShiftCount) == targetDayShiftsCountAfter, false);

            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(6, shiftNames.get(i)).size() >0, false);
            }

            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            targetDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            SimpleUtils.assertOnFail("The all shifts count be:"+(allShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+allShiftsCountAfter, (allShiftsCountBefore+selectedShiftCount) == allShiftsCountAfter, false);

            SimpleUtils.assertOnFail("The target shifts count should be:"+(targetDayShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+targetDayShiftsCountAfter, (targetDayShiftsCountBefore+selectedShiftCount) == targetDayShiftsCountAfter, false);

            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(6, shiftNames.get(i)).size() >0, false);
            }

            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            targetDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(6);
            SimpleUtils.assertOnFail("The all shifts count be:"+(allShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+allShiftsCountAfter, (allShiftsCountBefore+selectedShiftCount) == allShiftsCountAfter, false);

            SimpleUtils.assertOnFail("The target shifts count should be:"+(targetDayShiftsCountBefore+selectedShiftCount)
                    + " The actual is: "+targetDayShiftsCountAfter, (targetDayShiftsCountBefore+selectedShiftCount) == targetDayShiftsCountAfter, false);

            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(6, shiftNames.get(i)).size() >0, false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the shifts with same TMs and overlapping shift time cannot bulk drag and drop")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheShiftsWithSameTMsAndOverlappingShiftTimeCannotBulkDragAndDropAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM = shiftInfo.get(0);
            while (firstNameOfTM.equalsIgnoreCase("open")
                    || firstNameOfTM.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM  = shiftInfo.get(0);
            }
            String workRole= shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            //Fill the required option
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 2;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(2, firstNameOfTM);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, false);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            dayCount = 1;
            newShiftPage.selectSpecificWorkDay(dayCount);
            String shiftStartTime = "11:00am";
            String shiftEndTime = "2:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            dayCount = 1;
            newShiftPage.selectSpecificWorkDay(dayCount);
            shiftStartTime = "4:00pm";
            shiftEndTime = "9:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(2, firstNameOfTM);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            SimpleUtils.assertOnFail("The "+firstNameOfTM+"'s shift should not display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM+"'s shift should not display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM+"'s shift should display on third day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(2, firstNameOfTM).size()==2, false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate shifts will convert to open if it will incur conflict after move")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsWillConvertToOpenIfItWillIncurConflictAfterMoveAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRole1= shiftInfo.get(4);
            String firstNameOfTM2 = shiftInfo.get(0);
            while (firstNameOfTM2.equalsIgnoreCase("open")
                    || firstNameOfTM2.equalsIgnoreCase("unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM2  = shiftInfo.get(0);
            }
            String workRole2= shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();

            //Fill the required option
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 2;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 2;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(2, 0);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should not display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should not display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);

            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should not display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should not display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The open shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, "open").size()==2, false);
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should not display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should not display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The open shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, "open").size()==2, false);
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should not display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should not display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The open shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, "open").size()==2, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate shifts will convert to open if it will incur conflict after copy")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsWillConvertToOpenIfItWillIncurConflictAfterCopyAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRole1= shiftInfo.get(4);
            String firstNameOfTM2 = shiftInfo.get(0);
            while (firstNameOfTM2.equalsIgnoreCase("open")
                    || firstNameOfTM2.equalsIgnoreCase("unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM2  = shiftInfo.get(0);
            }
            String workRole2= shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();

            //Fill the required option
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 2;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 2;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(2, 0);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("Copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);

            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("Copy");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The open shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, "open").size()==2, false);
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The open shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, "open").size()==2, false);
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM2).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1).size()==1, false);
            SimpleUtils.assertOnFail("The open shift should display on second day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, "open").size()==2, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate bulk move the shifts that will incur OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkMoveShiftsThatWillIncurOTViolationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
//        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRole1= shiftInfo.get(4);
            String lastNameOfTM1 = shiftInfo.get(5);
            String firstNameOfTM2 = shiftInfo.get(0);
            while (firstNameOfTM2.equalsIgnoreCase("open")
                    || firstNameOfTM2.equalsIgnoreCase("unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM2  = shiftInfo.get(0);
            }
            String workRole2= shiftInfo.get(4);
            String lastNameOfTM2 = shiftInfo.get(5);
            String firstNameOfTM3 = shiftInfo.get(0);
            while (firstNameOfTM3.equalsIgnoreCase("open")
                    || firstNameOfTM3.equalsIgnoreCase("unassigned")
                    || firstNameOfTM3.equalsIgnoreCase(firstNameOfTM1)
                    || firstNameOfTM3.equalsIgnoreCase(firstNameOfTM2)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM3  = shiftInfo.get(0);
            }
            String workRole3= shiftInfo.get(4);
            String lastNameOfTM3 = shiftInfo.get(5);
            String firstNameOfTM4 = shiftInfo.get(0);
            while (firstNameOfTM4.equalsIgnoreCase("open")
                    || firstNameOfTM4.equalsIgnoreCase("unassigned")
                    || firstNameOfTM4.equalsIgnoreCase(firstNameOfTM1)
                    || firstNameOfTM4.equalsIgnoreCase(firstNameOfTM2)
                    || firstNameOfTM4.equalsIgnoreCase(firstNameOfTM3)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM4  = shiftInfo.get(0);
            }
            String workRole4= shiftInfo.get(4);
            String lastNameOfTM4 = shiftInfo.get(5);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM3);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM4);
            scheduleMainPage.saveSchedule();

            //Create shifts on first day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 4;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(5,5,5);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1+" "+lastNameOfTM1);
            newShiftPage.searchTeamMemberByName(firstNameOfTM2+" "+lastNameOfTM2);
            newShiftPage.searchTeamMemberByName(firstNameOfTM3+" "+lastNameOfTM3);
            newShiftPage.searchTeamMemberByName(firstNameOfTM4+" "+lastNameOfTM4);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create OT shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            String shiftStartTime = "11:30am";
            String shiftEndTime = "4:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(4,4,4);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1+" "+lastNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create split shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole2);
            shiftStartTime = "1:00pm";
            shiftEndTime = "3:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(4,4,4);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2+" "+lastNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            //Create spread shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole3);
            shiftStartTime = "4:00pm";
            shiftEndTime = "6:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(4,4,4);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM3+" "+lastNameOfTM3);
            newShiftPage.clickOnOfferOrAssignBtn();
            //Create clopening shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole4);
            shiftStartTime = "6:00pm";
            shiftEndTime = "11:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(3,3,3);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM4+" "+lastNameOfTM4);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(4, 5);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 4, true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            List<WebElement> firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            List<WebElement> secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            List<WebElement> thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            List<WebElement> forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            List<WebElement> firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            List<WebElement> secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            List<WebElement> thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            List<WebElement> forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()>=1, false);

            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 4, true);
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
//        scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"), false);
            SimpleUtils.assertOnFail("Split compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(secondTMShiftsOnTargetDay.get(secondTMShiftsOnTargetDay.size()-1)).contains("Split Shift"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(thirdTMShiftsOnTargetDay.get(thirdTMShiftsOnTargetDay.size()-1)).contains("Spread of hours"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(forthTMShiftsOnTargetDay.get(forthTMShiftsOnTargetDay.size()-1)).contains("Clopening"), false);

            scheduleMainPage.saveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"), false);
            SimpleUtils.assertOnFail("Split compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(secondTMShiftsOnTargetDay.get(secondTMShiftsOnTargetDay.size()-1)).contains("Split Shift"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(thirdTMShiftsOnTargetDay.get(thirdTMShiftsOnTargetDay.size()-1)).contains("Spread of hours"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(forthTMShiftsOnTargetDay.get(forthTMShiftsOnTargetDay.size()-1)).contains("Clopening"), false);

            createSchedulePage.publishActiveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"), false);
            SimpleUtils.assertOnFail("Split compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(secondTMShiftsOnTargetDay.get(secondTMShiftsOnTargetDay.size()-1)).contains("Split Shift"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(thirdTMShiftsOnTargetDay.get(thirdTMShiftsOnTargetDay.size()-1)).contains("Spread of hours"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(forthTMShiftsOnTargetDay.get(forthTMShiftsOnTargetDay.size()-1)).contains("Clopening"), false);

//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate bulk copy the shifts will incur OT violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkCopyShiftsThatWillIncurOTViolationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRole1= shiftInfo.get(4);
            String firstNameOfTM2 = shiftInfo.get(0);
            while (firstNameOfTM2.equalsIgnoreCase("open")
                    || firstNameOfTM2.equalsIgnoreCase("unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM1)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM2  = shiftInfo.get(0);
            }
            String workRole2= shiftInfo.get(4);
            String firstNameOfTM3 = shiftInfo.get(0);
            while (firstNameOfTM3.equalsIgnoreCase("open")
                    || firstNameOfTM3.equalsIgnoreCase("unassigned")
                    || firstNameOfTM3.equalsIgnoreCase(firstNameOfTM1)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM3  = shiftInfo.get(0);
            }
            String workRole3= shiftInfo.get(4);
            String firstNameOfTM4 = shiftInfo.get(0);
            while (firstNameOfTM4.equalsIgnoreCase("open")
                    || firstNameOfTM4.equalsIgnoreCase("unassigned")
                    || firstNameOfTM4.equalsIgnoreCase(firstNameOfTM1)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM4  = shiftInfo.get(0);
            }
            String workRole4= shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM3);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM4);
            scheduleMainPage.saveSchedule();

            //Create shifts on first day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 2;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(5,5,5);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            newShiftPage.searchTeamMemberByName(firstNameOfTM3);
            newShiftPage.searchTeamMemberByName(firstNameOfTM4);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create OT shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            String shiftStartTime = "3:00pm";
            String shiftEndTime = "8:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(4,4,4);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create split shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole2);
            shiftStartTime = "12:00pm";
            shiftEndTime = "3:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(4,4,4);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
            newShiftPage.clickOnOfferOrAssignBtn();
            //Create spread shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole3);
            shiftStartTime = "4:00pm";
            shiftEndTime = "6:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(4,4,4);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM3);
            newShiftPage.clickOnOfferOrAssignBtn();
            //Create clopening shift on second day
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole4);
            shiftStartTime = "6:00pm";
            shiftEndTime = "11:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(3,3,3);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM4);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(4, 5);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 4, true);
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("Copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            List<WebElement> firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            List<WebElement> secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            List<WebElement> thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            List<WebElement> forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            List<WebElement> firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            List<WebElement> secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            List<WebElement> thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            List<WebElement> forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()==1, false);

            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 4, true);
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("Copy");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
//        scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"), false);
            SimpleUtils.assertOnFail("Split compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(secondTMShiftsOnTargetDay.get(secondTMShiftsOnTargetDay.size()-1)).contains("Split Shift"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(thirdTMShiftsOnTargetDay.get(thirdTMShiftsOnTargetDay.size()-1)).contains("Spread of hours"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(forthTMShiftsOnTargetDay.get(forthTMShiftsOnTargetDay.size()-1)).contains("Clopening"), false);

            scheduleMainPage.saveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"), false);
            SimpleUtils.assertOnFail("Split compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(secondTMShiftsOnTargetDay.get(secondTMShiftsOnTargetDay.size()-1)).contains("Split Shift"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(thirdTMShiftsOnTargetDay.get(thirdTMShiftsOnTargetDay.size()-1)).contains("Spread of hours"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(forthTMShiftsOnTargetDay.get(forthTMShiftsOnTargetDay.size()-1)).contains("Clopening"), false);

            createSchedulePage.publishActiveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            secondTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2);
            thirdTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM3);
            forthTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM4);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM1);
            secondTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2);
            thirdTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM3);
            forthTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM4);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM2+"'s shift should display on first day! ",
                    secondTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM3+"'s shift should display on first day! ",
                    thirdTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM4+"'s shift should display on first day! ",
                    forthTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    secondTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    thirdTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    forthTMShiftsOnTargetDay.size()==2, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"), false);
            SimpleUtils.assertOnFail("Split compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(secondTMShiftsOnTargetDay.get(secondTMShiftsOnTargetDay.size()-1)).contains("Split Shift"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(thirdTMShiftsOnTargetDay.get(thirdTMShiftsOnTargetDay.size()-1)).contains("Spread of hours"), false);
            SimpleUtils.assertOnFail("Spread compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(forthTMShiftsOnTargetDay.get(forthTMShiftsOnTargetDay.size()-1)).contains("Clopening"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate bulk move the shifts that will incur 7th day violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkMoveShiftsThatWillIncur7thDayViolationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRole1= shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            //Create shifts on first 6 day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(5,5,5);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(2, 5);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 6, true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            List<WebElement> firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            List<WebElement> firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==0, false);

            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 6, true);
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
//        scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                            (firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"),
                    false);

            scheduleMainPage.saveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                            (firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"),
                    false);

            createSchedulePage.publishActiveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==0, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                            (firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"),
                    false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate bulk copy the shifts that will incur 7th day violation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkCopyShiftsThatWillIncur7thDayViolationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            String workRole1= shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            //Create shifts on first 6 day
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole1);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(2, 5);
            //drag them to one day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 6, true);
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("Copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            List<WebElement> firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            List<WebElement> firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==0, false);

            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 4, true);
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("Copy");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
//        scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                            (firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"),
                    false);

            scheduleMainPage.saveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                            (firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"),
                    false);

            createSchedulePage.publishActiveSchedule();
            firstTMShiftsOnOriginDay = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1);
            firstTMShiftsOnTargetDay = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on first day! ",
                    firstTMShiftsOnOriginDay.size()==1, false);
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should display on second day! ",
                    firstTMShiftsOnTargetDay.size()==1, false);
            SimpleUtils.assertOnFail("OT compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup
                            (firstTMShiftsOnTargetDay.get(firstTMShiftsOnTargetDay.size()-1)).contains("hrs daily overtime"),
                    false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate bulk drag and drop by different access roles")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkDragAndDropByDifferentAccessRolesAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            int count = (int)(Math.random()*4+1);
            String accessRole = "";
            switch(count) {
                case 1: accessRole = AccessRoles.StoreManager.getValue(); break;
                case 2: accessRole = AccessRoles.TeamLead.getValue();break;
                case 3: accessRole = AccessRoles.DistrictManager.getValue();break;
                case 4: accessRole = AccessRoles.CustomerAdmin.getValue();break;
            }
            //Verify the shifts can be created by new UI by original SM access role
            SimpleUtils.pass("Will login as: "+ accessRole);
            loginAsDifferentRole(accessRole);
            bulkDragAndDropByDifferentAccessRoles();
//            loginPage.logOut();

//            //Verify the shifts can be created by new UI by original TL access role
//            loginAsDifferentRole(AccessRoles.TeamLead.getValue());
//            bulkDragAndDropByDifferentAccessRoles();
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by original DM access role
//            loginAsDifferentRole(AccessRoles.DistrictManager.getValue());
//            bulkDragAndDropByDifferentAccessRoles();
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by original CA access role
//            loginAsDifferentRole(AccessRoles.CustomerAdmin.getValue());
//            bulkDragAndDropByDifferentAccessRoles();
//            loginPage.logOut();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    private void bulkDragAndDropByDifferentAccessRoles() throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

        // Navigate to next week
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule.
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
        }
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        //Get shift count before drag and drop
        int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
        int firstDayShiftCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
        int secondDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
        // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
        int selectedShiftCount = 3;
        List<WebElement> selectedShifts = scheduleShiftTablePage.
                selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);

        List<String> shiftNames = new ArrayList<>();
        for (int i=0; i< selectedShiftCount;i++) {
            int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
            shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
        }
        //Drag the selected shifts to another day
        scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

        //Select move option
        scheduleShiftTablePage.selectMoveOrCopyBulkShifts("move");
        scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
        scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        //Check the shift count after drag and drop
        int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
        int firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
        int secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
        SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+allShiftsCountBefore
                        + ", the actual count is:"+allShiftsCountAfter
                , allShiftsCountAfter == allShiftsCountBefore
                , false);
        SimpleUtils.assertOnFail("The first day shifts count should be: "+ (firstDayShiftCountBefore-selectedShiftCount)
                        + ", the actual count is:"+firstDayShiftCountAfter
                , (firstDayShiftCountBefore-selectedShiftCount) == firstDayShiftCountAfter
                , false);
        SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                        + ", the actual count is:"+secondDayShiftsCountAfter
                , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                , false);
        for (int i=0; i< selectedShiftCount;i++) {
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                    scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);

        }

        //Verify changes can be saved
        scheduleMainPage.saveSchedule();
        allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
        firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
        secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
        SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+allShiftsCountBefore
                        + ", the actual count is:"+allShiftsCountAfter
                , allShiftsCountAfter == allShiftsCountBefore
                , false);
        SimpleUtils.assertOnFail("The first day shifts count should be: "+ (firstDayShiftCountBefore-selectedShiftCount)
                        + ", the actual count is:"+firstDayShiftCountAfter
                , (firstDayShiftCountBefore-selectedShiftCount) == firstDayShiftCountAfter
                , false);
        SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                        + ", the actual count is:"+secondDayShiftsCountAfter
                , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                , false);
        for (int i=0; i< selectedShiftCount;i++) {
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                    scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);

        }

        isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
        }
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        //Get shift count before drag and drop
        allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
        firstDayShiftCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
        secondDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
        // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
        selectedShiftCount = 3;
        selectedShifts = scheduleShiftTablePage.
                selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);

       shiftNames = new ArrayList<>();
        for (int i=0; i< selectedShiftCount;i++) {
            int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
            shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
        }
        //Drag the selected shifts to another day
        scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

        //Select move option
        scheduleShiftTablePage.selectMoveOrCopyBulkShifts("copy");
        scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
        scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
        scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

        //Check the shift count after drag and drop
        allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
        firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
        secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
        SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+(allShiftsCountBefore+selectedShiftCount)
                        + ", the actual count is:"+allShiftsCountAfter
                , allShiftsCountAfter == (allShiftsCountBefore+selectedShiftCount)
                , false);
        SimpleUtils.assertOnFail("The first day shifts count should be: "+ firstDayShiftCountBefore
                        + ", the actual count is:"+firstDayShiftCountAfter
                , firstDayShiftCountBefore == firstDayShiftCountAfter
                , false);
        SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                        + ", the actual count is:"+secondDayShiftsCountAfter
                , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                , false);
        for (int i=0; i< selectedShiftCount;i++) {
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                    scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size() >0, false);
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() >0, false);

        }
        //Verify changes can be saved
        scheduleMainPage.saveSchedule();
        allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
        firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
        secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
        SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+(allShiftsCountBefore+selectedShiftCount)
                        + ", the actual count is:"+allShiftsCountAfter
                , allShiftsCountAfter == (allShiftsCountBefore+selectedShiftCount)
                , false);
        SimpleUtils.assertOnFail("The first day shifts count should be: "+ firstDayShiftCountBefore
                        + ", the actual count is:"+firstDayShiftCountAfter
                , firstDayShiftCountBefore == firstDayShiftCountAfter
                , false);
        SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                        + ", the actual count is:"+secondDayShiftsCountAfter
                , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                , false);
        for (int i=0; i< selectedShiftCount;i++) {
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                    scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size() >0, false);
            SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                    scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() >0, false);

        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate shifts can be drag&drop several times without save")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsCanBeDragAndDropSeveralTimesWithOutSaveAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int firstDayShiftCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            int secondDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            int thirdDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(3);
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 3;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);

            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Drag the selected shifts to another day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

            //Select move option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("move");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            int secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+allShiftsCountBefore
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == allShiftsCountBefore
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ (firstDayShiftCountBefore-selectedShiftCount)
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , (firstDayShiftCountBefore-selectedShiftCount) == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);

            }
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 2);

            shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Drag the selected shifts to another day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 3, true);

            //Select copy option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("copy");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            firstDayShiftCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            secondDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(3);
            SimpleUtils.assertOnFail("The all shifts count should not change, the expect count is: "+(allShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+allShiftsCountAfter
                    , allShiftsCountAfter == (allShiftsCountBefore+selectedShiftCount)
                    , false);
            SimpleUtils.assertOnFail("The first day shifts count should be: "+ firstDayShiftCountBefore
                            + ", the actual count is:"+firstDayShiftCountAfter
                    , firstDayShiftCountBefore == firstDayShiftCountAfter
                    , false);
            SimpleUtils.assertOnFail("The second day shifts count should be: "+ (secondDayShiftsCountBefore+selectedShiftCount)
                            + ", the actual count is:"+secondDayShiftsCountAfter
                    , (secondDayShiftsCountBefore+selectedShiftCount) == secondDayShiftsCountAfter
                    , false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(3, shiftNames.get(i)).size() >0, false);
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size() >0, false);

            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the swap and assign button display correctly on drag and drop confirm change modal")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySwapAndAssignButtonOnDragAndDropConfirmChangeModalAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
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

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (firstNameOfTM1.equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
            }
            int dayIndex1 = Integer.parseInt(shiftInfo.get(1));
            while (shiftInfo.get(0).equalsIgnoreCase(firstNameOfTM1)
                    || shiftInfo.get(0).equalsIgnoreCase("open")
                    || firstNameOfTM1.equalsIgnoreCase("unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            }
            String firstNameOfTM2 = shiftInfo.get(0);
            int dayIndex2 = Integer.parseInt(shiftInfo.get(1));
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int i=0;
            while (!scheduleShiftTablePage.isDragAndDropConfirmPageLoaded() && i<5){
                scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(dayIndex1,firstNameOfTM1,dayIndex2,firstNameOfTM2);
                i++;
                Thread.sleep(2000);
            }
            Thread.sleep(5000);
            //The Confirm button is display and disabled
            scheduleShiftTablePage.verifyConfirmBtnIsDisabledOnDragAndDropConfirmPage();
            //The Confirm button will change to Swap button and clickable
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.verifySwapBtnIsEnabledOnDragAndDropConfirmPage();
            //The button will change to Assign button and clickable
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.verifyAssignBtnIsEnabledOnDragAndDropConfirmPage();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate shifts can be drag&drop when group by TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsCanBeDragAndDropWhenGroupByTMAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Assigned");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Change group by TM
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyTM.getValue());
            int selectedShiftCount = 1;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);

            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Drag the selected shifts to another day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

            //Select move option
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("Move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if(scheduleShiftTablePage.ifMoveAnywayDialogDisplay()){
                scheduleShiftTablePage.moveAnywayWhenChangeShift();
            }
            //Verify the shift been move to other day
            scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(1,shiftNames.get(0), 2);
            //Verify the shift been move to other day after save
            scheduleMainPage.saveSchedule();
            scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(1,shiftNames.get(0), 2);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 2);

            shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }
            //Drag the selected shifts to another day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 3, true);

            //Select copy option
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("Copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            //Verify the shift been copy to other day
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(1,shiftNames.get(0), 2);
            //Verify the shift been copy to other day after save
            scheduleMainPage.saveSchedule();
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(1,shiftNames.get(0), 2);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate shifts avatar can be drag&drop when group by TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftsAvatarCanBeDragAndDropWhenGroupByTMAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Assigned");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Change group by TM
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyTM.getValue());
            int i = 0;
            List<String> shiftInfo = new ArrayList<>();
            while (i< 50 &&shiftInfo.size() == 0) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
                i++;
            }
            String firstNameOfTM1 = shiftInfo.get(0);
            String workRoleOfTM1 = shiftInfo.get(4);
            int dayIndex1 = Integer.parseInt(shiftInfo.get(1));
            i = 0;
            List<String> shiftInfo2 = new ArrayList<>();
            while (i < 50 && (shiftInfo2.size() == 0 || firstNameOfTM1.equals(shiftInfo2.get(0)))) {
                shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                i++;
            }
            String firstNameOfTM2 = shiftInfo2.get(0);
            String workRoleOfTM2 = shiftInfo2.get(4);
            int dayIndex2 = Integer.parseInt(shiftInfo2.get(1));
            Thread.sleep(3000);
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(dayIndex1, firstNameOfTM1, dayIndex2, firstNameOfTM2);
            List<String> swapData = scheduleShiftTablePage.getShiftSwapDataFromConfirmPage("swap");
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            mySchedulePage.verifyShiftsAreSwapped(swapData);
            scheduleMainPage.saveSchedule();
            mySchedulePage.verifyShiftsAreSwapped(swapData);


            i = 0;
            shiftInfo = new ArrayList<>();
            while (i< 50 &&shiftInfo.size() == 0) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
                i++;
            }
            firstNameOfTM1 = shiftInfo.get(0);
            workRoleOfTM1 = shiftInfo.get(4);
            dayIndex1 = Integer.parseInt(shiftInfo.get(1));
            i = 0;
            shiftInfo2 = new ArrayList<>();
            while (i < 50 && (shiftInfo2.size() == 0 || firstNameOfTM1.equals(shiftInfo2.get(0)))) {
                shiftInfo2 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                i++;
            }
            firstNameOfTM2 = shiftInfo2.get(0);
            workRoleOfTM2 = shiftInfo2.get(4);
            dayIndex2 = Integer.parseInt(shiftInfo2.get(1));
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            Thread.sleep(5000);
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(dayIndex1, firstNameOfTM1, dayIndex2, firstNameOfTM2);
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(dayIndex1,firstNameOfTM1)==1 && scheduleShiftTablePage.verifyDayHasShiftByName(dayIndex2,firstNameOfTM1)>=1){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(dayIndex1,firstNameOfTM1)==1 && scheduleShiftTablePage.verifyDayHasShiftByName(dayIndex2,firstNameOfTM1)>=1){
                SimpleUtils.pass("assign successfully!");
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
