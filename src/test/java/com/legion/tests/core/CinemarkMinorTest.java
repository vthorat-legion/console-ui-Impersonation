package com.legion.tests.core;


import com.legion.api.cache.CacheAPI;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.DashboardPage;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.ProfileNewUIPage;
import com.legion.pages.TeamPage;
import com.legion.pages.*;
import com.legion.pages.core.ConsoleLoginPage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.*;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.Constants;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.sl.In;
import org.jsoup.Connection;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.internal.annotations.IAfterClass;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

import static java.time.LocalDate.*;

public class CinemarkMinorTest extends TestBase {

    private static HashMap<String, String> cinemarkSetting14N15 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/CinemarkMinorSettings.json");
    private static HashMap<String, String> cinemarkSetting16N17 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/CinemarkMinorSettings16N17.json");
    private static HashMap<String, String> cinemarkMinors = JsonUtil.getPropertiesFromJsonFile("src/test/resources/CinemarkMinorsData.json");
    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static String minorWorkRole = scheduleWorkRoles.get("TEAM_MEMBER_CORPORATE_THEATRE");

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            CacheAPI.refreshTemplateCache((String) params[1], (String) params[2]);
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
//            if (MyThreadLocal.getCurrentComplianceTemplate()==null || MyThreadLocal.getCurrentComplianceTemplate().equals("")){
//                getAndSetDefaultTemplate((String) params[3]);
//            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //The template the location is using.
    public enum templateInUse{
        TEMPLATE_NAME("New Hampshire Compliance"),
        TEMPLATE_OP("clement-997");
        private final String value;
        templateInUse(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum minorType{
        Minor14N15("14N15"),
        Minor16N17("16N17");
        private final String value;
        minorType(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum minorRuleWeekType{
        School_Week("School Week"),
        Non_School_Week("Non-School Week"),
        Summer_Week("Summer Week");
        private final String value;
        minorRuleWeekType(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum minorRuleDayType{
        SchoolToday_SchoolTomorrow("School today, school tomorrow"),
        SchoolToday_NoSchoolTomorrow("School today, no school tomorrow"),
        NoSchoolToday_NoSchoolTomorrow("No school today, no school tomorrow"),
        NoSchoolToday_SchoolTomorrow("No school today, school tomorrow"),
        Summer_Day("Summer day");
        private final String value;
        minorRuleDayType(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum templateAction{
        Save_As_Draft("saveAsDraft"),
        Publish_Now("publishNow"),
        Publish_Later("publishLater");
        private final String value;
        templateAction(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum buttonGroup{
        Cancel("Cancel"),
        Close("Close"),
        OKWhenEdit("OK"),
        OKWhenPublish("OK"),
        Delete("Delete"),
        Save("Save"),
        EditTemplate("Edit template"),
        Edit("Edit"),
        Yes("Yes"),
        No("No");
        private final String value;
        buttonGroup(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum minorNames{
        Minor13("Minor13"),
        Minor14("Minor14"),
        Minor15("Minor15"),
        Minor16("Minor16"),
        Minor17("Minor17");
        private final String value;
        minorNames(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Prepare the calendar for all the minors")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void prepareTheCalendarForAllMinorsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleCommonPage.navigateDayViewWithDayName("Sat");
            Map<String, String> dayInfo = scheduleCommonPage.getActiveDayInfo();

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();

            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);
            String calendarName = "Automation" + new Random().nextInt(100) + new Random().nextInt(100) + new Random().nextInt(100);

            teamPage.deleteCalendarByName("Automation");
            teamPage.clickOnCreateNewCalendarButton();
            teamPage.selectSchoolYear();
            teamPage.clickOnSchoolSessionStart();
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            if (month < 6) {
                //First half of the year
                teamPage.selectSchoolSessionStartAndEndDate((Integer.parseInt(dayInfo.get("year")) - 1) + " Aug 1",
                        (Integer.parseInt(dayInfo.get("year"))) + " " + dayInfo.get("month") + " " + dayInfo.get("day"));
            } else {
                //second half
                teamPage.selectSchoolSessionStartAndEndDate((Integer.parseInt(dayInfo.get("year"))) + " Jan 1",
                        (Integer.parseInt(dayInfo.get("year"))) + " " + dayInfo.get("month") + " " + dayInfo.get("day"));
            }

            teamPage.clickOnSaveSchoolSessionCalendarBtn();
            teamPage.setNonSchoolDaysForNonSchoolWeek();
            teamPage.inputCalendarName(calendarName);
            teamPage.clickOnSaveSchoolCalendarBtn();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            List<String> minorNames = new ArrayList<>();
            minorNames.add(cinemarkMinors.get("Minor13"));
            minorNames.add(cinemarkMinors.get("Minor14"));
            minorNames.add(cinemarkMinors.get("Minor15"));
            minorNames.add(cinemarkMinors.get("Minor16"));
            minorNames.add(cinemarkMinors.get("Minor17"));

            teamPage.setTheCalendarForMinors(minorNames, calendarName, profileNewUIPage);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify add dates for breaks")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddDatesForBreaksAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            teamPage.clickTheCalendarByRandom();
            teamPage.verifySchoolSessionPageLoaded();
            teamPage.clickOnEditCalendarButton();
            teamPage.verifyEditCalendarAlertModelPopsUp();
            teamPage.clickOnEditAnywayButton();
            SimpleUtils.assertOnFail("Edit Calendar page not loaded Successfully!", teamPage.isEditCalendarModeLoaded(), false);

            // Verify the clicked days are highlighted as "Non School Day" color
            teamPage.verifyClickedDayIsHighlighted();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify view details of  calendars and edit")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViewDetailsAndEditAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            // Verify the visibility of calendars on School Calendars page
            teamPage.verifyTheCalendarListLoaded();
            // Verify the content of each calendar
            teamPage.verifyTheContentOnEachCalendarList();
            // Verify the visibility of the detailed calendar page
            teamPage.clickTheCalendarByRandom();
            teamPage.verifySchoolSessionPageLoaded();
            // Verify the content on detailed calendar page
            teamPage.verifyTheContentOnDetailedCalendarPage();
            // Verify the functionality of Edit button
            teamPage.clickOnEditCalendarButton();
            teamPage.verifyEditCalendarAlertModelPopsUp();
            teamPage.clickOnEditAnywayButton();
            SimpleUtils.assertOnFail("Edit Calendar page not loaded Successfully!", teamPage.isEditCalendarModeLoaded(), false);

            // Verify the functionality of Save button
            teamPage.clickOnSaveSchoolCalendarBtn();
            // Verify the functionality of "School Schedules" button
            teamPage.clickOnSchoolSchedulesButton();
            teamPage.verifyTheCalendarListLoaded();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify delete calendar")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDeleteCalendarAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            // Verify the visibility of calendars on School Calendars page
            teamPage.verifyTheCalendarListLoaded();

            teamPage.clickTheCalendarByRandom();
            teamPage.verifySchoolSessionPageLoaded();

            // Verify the presence of DELETE button
            // Verify the functionality of DELETE button
            teamPage.clickOnDeleteCalendarButton();
            // Verify the functionality of CANCEL button
            teamPage.clickOnCancelButtonOnPopup();
            // Verify the functionality of DELETE ANYWAY button
            teamPage.clickOnDELETEANYWAYButton();
            teamPage.verifyTheCalendarListLoaded();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify SM will have ability to select a calendar for the minor from a dropdown menu within the profile")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySMCanSelectACalendarForMinorAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();

            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.selectATeamMemberToViewProfile();
            teamPage.isProfilePageLoaded();

            // Verify Minor filed is displayed on TM Profile
            if (profileNewUIPage.isMINORYesOrNo())
                profileNewUIPage.verifyMINORField(true);
            else
                profileNewUIPage.verifyMINORField(false);

            // Search out a TM who is a minor
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            String minorName = teamPage.searchAndSelectTeamMemberByName(cinemarkMinors.get("Minor14"));
            teamPage.isProfilePageLoaded();
            if (minorName != "")
                SimpleUtils.pass("Team Page: search out one minor to View Profile successfully");
            else
                SimpleUtils.fail("Team Page: Failed to search out one minor to View Profile",false);

            // Verify SM can select a calendar from a dropdown menu within the profile
            profileNewUIPage.verifySMCanSelectACalendarForMinor();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the default value of a minor without a calendar")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDefaultValueOfAMinorWithoutACalendarAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            AnalyzePage analyzePage = pageFactory.createAnalyzePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();

            // Get Cinemark minor settings from Jason file
            String schoolWeekMaxScheduleHrs = cinemarkSetting14N15.get(minorRuleWeekType.School_Week.getValue()).split(",")[1];
            String nonSchoolWeekMaxScheduleHrs = cinemarkSetting14N15.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[1];

            // Search out a TM who is a minor and get minor name to enter profile page
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.searchAndSelectTeamMemberByName(cinemarkMinors.get("Minor14"));
            teamPage.isProfilePageLoaded();

            // Edit, select "None" from the calendar dropdown menu, and save the profile
            profileNewUIPage.selectAGivenCalendarForMinor("None");

            // Go to Schedule page and navigate to a week

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            // Get the current holiday information if have
            String holidaySmartCard = "HOLIDAYS";
            List<String> holidays = null;
            if (smartCardPage.isSpecificSmartCardLoaded(holidaySmartCard)) {
                smartCardPage.navigateToTheRightestSmartCard();
                smartCardPage.clickLinkOnSmartCardByName("View All");
                holidays = smartCardPage.getHolidaysOfCurrentWeek();
                // Close popup window
                analyzePage.closeAnalyzeWindow();
            }

            // Ungenerate the schedule if it is created or published
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            // Create new shift for the minor at weekday, weekend and holiday if have
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "05:00AM", "11:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(cinemarkMinors.get("Minor14"));
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.moveSliderAtCertainPoint("10pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("6am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(cinemarkMinors.get("Minor14"));
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(cinemarkMinors.get("Minor14"));
            shiftOperatePage.clickOnAssignAnywayButton();
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            // Get holidays index if have
            ArrayList<Integer> holidayIndexes = new ArrayList<>();
            ArrayList<Integer> weekdayIndexes = new ArrayList<>();
            if (holidays!= null) {
                for (int index = 0; index < 5; index ++) {
                    for (String s: holidays) {
                        if (s.contains(scheduleShiftTablePage.getWeekDayTextByIndex(index)))
                            holidayIndexes.add(index);
                        else
                            weekdayIndexes.add(index);
                    }
                }
            } else
                weekdayIndexes.add(0);

            // Validate weekday should apply the settings of school day
            WebElement newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(cinemarkMinors.get("Minor14").split(" ")[0]).get(weekdayIndexes.get(0)));
            if (newAddedShift != null && scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max " + schoolWeekMaxScheduleHrs + " hrs"))
                SimpleUtils.pass("Schedule Page: Weekday applies the settings of non school day");
            else
                SimpleUtils.fail("Get new added shift failed", false);

            // Validate weekend should apply the settings of non school day
            newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(cinemarkMinors.get("Minor14").split(" ")[0]).get(5));
            if (newAddedShift != null && scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max " + schoolWeekMaxScheduleHrs + " hrs"))
                SimpleUtils.pass("Schedule Page: Weekday applies the settings of non school day");
            else
                SimpleUtils.fail("Get new added shift failed", false);


            // Validate holiday should apply the settings of non school day
            if (holidays != null) {
                newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(cinemarkMinors.get("Minor14").split(" ")[0]).get(holidayIndexes.get(0)));
                if (newAddedShift != null) {
                    if (holidayIndexes.size() == 5 && scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max " + nonSchoolWeekMaxScheduleHrs + " hrs"))
                        SimpleUtils.pass("Schedule Page: Holiday applies the settings of non school day");
                    else if (holidayIndexes.size() < 5 && scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max " + schoolWeekMaxScheduleHrs + " hrs"))
                        SimpleUtils.pass("Schedule Page: Holiday applies the settings of non school day");
                    else
                        SimpleUtils.fail("Schedule Page: Holiday does not apply the settings of non school day",false);
                } else
                    SimpleUtils.fail("Get new added shift failed", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify create calendar")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateCalendarAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            int randomDigits = (new Random()).nextInt(100);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            // Go to School Calendars sub tab
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            // Click on Create New Calendar button, verify the Cancel and Save button display correctly
            teamPage.clickOnCreateNewCalendarButton();

            // Verify the Session Start and Session End fields are mandatory fields
            teamPage.verifyCreateCalendarLoaded();
            teamPage.verifySessionStartNEndIsMandatory();

            // Click on School Session Start
            teamPage.clickOnSchoolSessionStart();

            // Select random start and end day and verify they display correctly
            String startDate = teamPage.selectRandomDayInSessionStart(); //08-25-2020
            String endDate = teamPage.selectRandomDayInSessionEnd(); //05-31-2021

            // Save after setting session start and end time
            teamPage.clickOnSaveSchoolSessionCalendarBtn();

            // Verify dates will be color coded by start and end time
            teamPage.verifyDatesInCalendar(startDate,endDate);

            // Input calendar name, and verify the calendar name can be edited and changed
            String calendarName = "Calendar" + randomDigits;
            teamPage.inputCalendarName(calendarName);

            // Verify calendar for the next year will show the same calendar name until enter the start and end date, the calendar is editable
            teamPage.checkNextYearInEditMode();

            // Verify the year display when going back to current calendar
            teamPage.clickOnPriorYearInEditMode();

            // Verify that cannot go to prior year
            teamPage.checkPriorYearInEditMode();

            // Verify the calendar can be saved
            teamPage.clickOnSaveCalendar();

            // Verify the new created calendar will list in the calendar list
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), true);
            if (teamPage.isCalendarDisplayedByName("Calendar" + randomDigits))
                SimpleUtils.pass("School Calendar: Calendar just created is in the list");
            else
                SimpleUtils.fail("School Calendar: A Calendar just created is not in the list",true);

            // Verify the calendar can be deleted
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), true);
            teamPage.deleteCalendarByName(calendarName);

            // Verify the the change for calendar will not been saved after click Cancel button
            teamPage.clickOnCreateNewCalendarButton();
            teamPage.clickOnSchoolSessionStart();
            teamPage.selectRandomDayInSessionStart();
            teamPage.selectRandomDayInSessionEnd();
            teamPage.clickOnSaveSchoolSessionCalendarBtn();
            teamPage.inputCalendarName("CancelledCalendar");
            teamPage.clickOnCancelEditCalendarBtn();
            if (!teamPage.isCalendarDisplayedByName("CancelledCalendar"))
                SimpleUtils.pass("School Calendar: Create action is cancelled, there will not be this calendar in the list");
            else
                SimpleUtils.fail("School Calendar: Create action failed to cancel",false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify school calendar list")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchoolCalendarListAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            int random1 = (new Random()).nextInt(1000);
            int random2 = (new Random()).nextInt(1000);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            // Create a new calendar via Admin
            teamPage.createNewCalendarByName("Calendar" + random1);

            // Create another new calendar via Admin
            teamPage.createNewCalendarByName("Calendar" + random2);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Login as Store Manager
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            int random3 = (new Random()).nextInt(1000);
            int random4 = (new Random()).nextInt(1000);

            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            // Create a new calendar via Store Manager
            teamPage.createNewCalendarByName("Calendar" + random3);

            // Create another new calendar via Store Manager
            teamPage.createNewCalendarByName("Calendar" + random4);

            System.out.println(random1);
            System.out.println(random2);
            System.out.println(random3);
            System.out.println(random4);

            // Check the School Calendars list
            if (teamPage.isCalendarDisplayedByName("Calendar" + random1) && teamPage.isCalendarDisplayedByName("Calendar" + random2)
                    && teamPage.isCalendarDisplayedByName("Calendar" + random3) && teamPage.isCalendarDisplayedByName("Calendar" + random4))
                SimpleUtils.pass("School Calendar: All the calendars have been created display in the list");
            else
                SimpleUtils.fail("School Calendar: All the calendars have been created don't display in the list",false);
            loginPage.logOut();

            // Login as Internal Admin to clean up data
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);
            teamPage.deleteCalendarByName("Calendar" + random1);
            teamPage.deleteCalendarByName("Calendar" + random2);
            teamPage.deleteCalendarByName("Calendar" + random3);
            teamPage.deleteCalendarByName("Calendar" + random4);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    //Haya
    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify turn off minor rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTurnOffMinorRuleAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
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
            MyThreadLocal.setCurrentComplianceTemplate(templateTypeAndName.get("Compliance"));

            //go to Configuration
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsComplianceSection();
            String templateName = "test"+String.valueOf(System.currentTimeMillis());
            cinemarkMinorPage.newTemplate(templateName);
            cinemarkMinorPage.verifyDefaultMinorRuleIsOff("14N15");
            cinemarkMinorPage.verifyDefaultMinorRuleIsOff("16N17");
            cinemarkMinorPage.saveOrPublishTemplate(templateAction.Save_As_Draft.getValue());
            cinemarkMinorPage.findDefaultTemplate(templateName);
            cinemarkMinorPage.clickOnBtn(buttonGroup.Delete.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenPublish.getValue());

            //cinemarkMinorPage.findDefaultTemplate(templateInUse.TEMPLATE_NAME.getValue());
            cinemarkMinorPage.findDefaultTemplate(MyThreadLocal.getCurrentComplianceTemplate());
            cinemarkMinorPage.clickOnBtn(buttonGroup.Edit.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenEdit.getValue());
            cinemarkMinorPage.minorRuleToggle("no","14N15");
            cinemarkMinorPage.minorRuleToggle("no","16N17");
            cinemarkMinorPage.saveOrPublishTemplate(templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenPublish.getValue());
            Thread.sleep(3000);

            //Back to Console
            switchToConsoleWindow();

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            SimpleUtils.assertOnFail("School Calendar tab should not be loaded when minor rule turned off", !teamPage.isCalendarTabLoad(), false);

            loginPage.logOut();

            // Login as Internal Admin
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsComplianceSection();
            cinemarkMinorPage.findDefaultTemplate(MyThreadLocal.getCurrentComplianceTemplate());
            cinemarkMinorPage.clickOnBtn(buttonGroup.Edit.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenEdit.getValue());
            cinemarkMinorPage.minorRuleToggle("yes","14N15");
            cinemarkMinorPage.minorRuleToggle("yes","16N17");
            cinemarkMinorPage.saveOrPublishTemplate(templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenPublish.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "verify turn on minor rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTurnOnAndSetMinorRuleEmptyAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            String districtName = dashboardPage.getCurrentDistrict();

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
            MyThreadLocal.setCurrentComplianceTemplate(templateTypeAndName.get("Compliance"));
            //go to Configuration
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsComplianceSection();

            //Find the template
            //cinemarkMinorPage.findDefaultTemplate(templateInUse.TEMPLATE_NAME.getValue());
            cinemarkMinorPage.findDefaultTemplate(MyThreadLocal.getCurrentComplianceTemplate());
            cinemarkMinorPage.clickOnBtn(buttonGroup.Edit.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenEdit.getValue());
            cinemarkMinorPage.minorRuleToggle("yes","14N15");

//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor16N17.getValue(), minorRuleWeekType.School_Week.getValue(),"","");
//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor16N17.getValue(), minorRuleWeekType.Non_School_Week.getValue(),"","");
//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor16N17.getValue(), minorRuleWeekType.Summer_Week.getValue(),"","");
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.SchoolToday_SchoolTomorrow.getValue(), "","","");
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue(), "","","");
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue(), "","","");
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue(), "","","");
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.Summer_Day.getValue(), "","","");

            cinemarkMinorPage.saveOrPublishTemplate(templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenPublish.getValue());

            //Back to Console
            switchToConsoleWindow();
//            locationSelectorPage.changeUpperFieldsByName("Business Unit", "BU1");
//            locationSelectorPage.changeUpperFieldsByName("Region", "Region1");
//            locationSelectorPage.changeDistrict(districtName);
//            String minorLocation = "Test For Minors";
//            locationSelectorPage.changeLocation(minorLocation);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(cinemarkMinors.get("Minor17"));
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(0,0,0);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("5:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(cinemarkMinors.get("Minor17"));
            SimpleUtils.assertOnFail("Minor warning should not work when setting is empty", !shiftOperatePage.getAllTheWarningMessageOfTMWhenAssign().contains("Minor"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Admin can configure the access to edit calendars")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAccessToEditCalendarsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            controlsPage.clickGlobalSettings();

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            String accessRoleTab = "Access Roles";
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
            String permissionSection = "Team";
            String permission1 = "Manage School Calendars";
            String permission2 = "View School Calendars";
            String actionOff = "off";
            String actionOn = "on";
            cinemarkMinorPage.clickOnBtn(buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission1, actionOff);
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission2, actionOff);
            cinemarkMinorPage.clickOnBtn(buttonGroup.Save.getValue());
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            //Log in as SM to check
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            SimpleUtils.assertOnFail("School Calendar tab should not be loaded when SM doesn't have the permission!", !teamPage.isCalendarTabLoad(), false);
            loginPage.logOut();

            //Log in as admin, grant the view calendar permission to SM.
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            controlsPage.gotoControlsPage();
            controlsPage.clickGlobalSettings();

            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
            cinemarkMinorPage.clickOnBtn(buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission2, actionOn);
            cinemarkMinorPage.clickOnBtn(buttonGroup.Save.getValue());
            loginPage.logOut();

            //Log in as SM to check
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            teamPage.goToTeam();
            String calendarTab = "School Calendars";
            teamPage.clickOnTeamSubTab(calendarTab);
            SimpleUtils.assertOnFail("School Calendar tab should show up!", teamPage.isCalendarTabLoad(), false);
            //SimpleUtils.assertOnFail("School Calendar Create New Calendar button should not load!", !teamPage.isCreateCalendarBtnLoaded(), true);
            if (teamPage.isCreateCalendarBtnLoaded()){
                SimpleUtils.warn("School Calendar Create New Calendar button should not load!");
            }

            loginPage.logOut();
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            controlsPage.gotoControlsPage();
            controlsPage.clickGlobalSettings();

            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(accessRoleTab);
            cinemarkMinorPage.clickOnBtn(buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission1, actionOn);
            cinemarkMinorPage.clickOnBtn(buttonGroup.Save.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify turn on minor rule and set rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTurnOnAndSetMinorRuleAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
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
            MyThreadLocal.setCurrentComplianceTemplate(templateTypeAndName.get("Compliance"));
            //go to Configuration
            cinemarkMinorPage.clickConfigurationTabInOP();
            controlsNewUIPage.clickOnControlsComplianceSection();

            //Find the template
            //cinemarkMinorPage.findDefaultTemplate(templateInUse.TEMPLATE_NAME.getValue());
            cinemarkMinorPage.findDefaultTemplate(MyThreadLocal.getCurrentComplianceTemplate());
            cinemarkMinorPage.clickOnBtn(buttonGroup.Edit.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenEdit.getValue());
            cinemarkMinorPage.minorRuleToggle("yes","14N15");

//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor14N15.getValue(), minorRuleWeekType.School_Week.getValue(),cinemarkSetting14N15.get(minorRuleWeekType.School_Week.getValue()).split(",")[0],cinemarkSetting14N15.get(minorRuleWeekType.School_Week.getValue()).split(",")[1]);
//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor14N15.getValue(), minorRuleWeekType.Non_School_Week.getValue(),cinemarkSetting14N15.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[0],cinemarkSetting14N15.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[1]);
//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor14N15.getValue(), minorRuleWeekType.Summer_Week.getValue(),cinemarkSetting14N15.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[0],cinemarkSetting14N15.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[1]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor14N15.getValue(), minorRuleDayType.SchoolToday_SchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor14N15.getValue(), minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor14N15.getValue(), minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor14N15.getValue(), minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor14N15.getValue(), minorRuleDayType.Summer_Day.getValue(), cinemarkSetting14N15.get(minorRuleDayType.Summer_Day.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.Summer_Day.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.Summer_Day.getValue()).split(",")[2]);
//            cinemarkMinorPage.minorRuleToggle("yes","16N17");
//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor16N17.getValue(), minorRuleWeekType.School_Week.getValue(),cinemarkSetting16N17.get(minorRuleWeekType.School_Week.getValue()).split(",")[0],cinemarkSetting16N17.get(minorRuleWeekType.School_Week.getValue()).split(",")[1]);
//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor16N17.getValue(), minorRuleWeekType.Non_School_Week.getValue(),cinemarkSetting16N17.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[0],cinemarkSetting16N17.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[1]);
//            cinemarkMinorPage.setMinorRuleByWeek(minorType.Minor16N17.getValue(), minorRuleWeekType.Summer_Week.getValue(),cinemarkSetting16N17.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[0],cinemarkSetting16N17.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[1]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.SchoolToday_SchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
//            cinemarkMinorPage.setMinorRuleByDay(minorType.Minor16N17.getValue(), minorRuleDayType.Summer_Day.getValue(), cinemarkSetting16N17.get(minorRuleDayType.Summer_Day.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.Summer_Day.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.Summer_Day.getValue()).split(",")[2]);

            cinemarkMinorPage.saveOrPublishTemplate(templateAction.Publish_Now.getValue());
            cinemarkMinorPage.clickOnBtn(buttonGroup.OKWhenPublish.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //added by Haya.
    public void getAndSetDefaultTemplate(String currentLocation) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        //Go to OP page
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.searchLocation(currentLocation);               ;
        SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(currentLocation), false);
        locationsPage.clickOnLocationInLocationResult(currentLocation);
        locationsPage.clickOnConfigurationTabOfLocation();
        HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
        MyThreadLocal.setCurrentComplianceTemplate(templateTypeAndName.get("Compliance"));
        //back to console.
        switchToConsoleWindow();
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the School today and school tomorrow settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSchoolTodayAndSchoolTomorrowSettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "7am,1pm";
            String shiftTime2 = "9am,4pm";
            String shiftTime3 = "8am,2pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "8am - 4pm";
            String scheduleMaxHours = "6";
            String selectWeekDayName = "Mon";
//            setStrictlyEnforceMinorViolationSetting("No");
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            dashboardPage.clickOnDashboardConsoleMenu();
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3,
                    workRole, scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the School today and school tomorrow settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSchoolTodayAndSchoolTomorrowSettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "8am,1pm";
            String shiftTime2 = "9am,4pm";
            String shiftTime3 = "9am,2pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "8:30am - 4pm";
            String scheduleMaxHours = "5";
            String selectWeekDayName = "Mon";
            if (minorName.contains("14")
                    ||minorName.contains("15"))  {
                shiftTime1 = "7am,1pm";
                shiftTime2 = "9am,4pm";
                shiftTime3 = "8am,2pm";
                workRole = minorWorkRole;
                scheduleFromToTime = "8am - 4pm";
                scheduleMaxHours = "6";
                selectWeekDayName = "Mon";
            }
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3, workRole,
                    scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the School today and no school tomorrow settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSchoolTodayAndNoSchoolTomorrowSettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "8am,3pm";
            String shiftTime2 = "9am,5pm";
            String shiftTime3 = "11am,4pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "8:30am - 5pm";
            String scheduleMaxHours = "5";
            String selectWeekDayName = "Fri";
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3,
                    workRole, scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the School today and no school tomorrow settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSchoolTodayAndNoSchoolTomorrowSettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "8am,1pm";
            String shiftTime2 = "9am,5pm";
            String shiftTime3 = "9am,2pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "9am - 5pm";
            String scheduleMaxHours = "6";
            String selectWeekDayName = "Fri";
            if (minorName.contains("14")
                    ||minorName.contains("15")) {
                shiftTime1 = "8am,3pm";
                shiftTime2 = "9am,5pm";
                shiftTime3 = "11am,4pm";
                workRole = minorWorkRole;
                scheduleFromToTime = "8:30am - 5pm";
                scheduleMaxHours = "5";
                selectWeekDayName = "Fri";
            }
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3, workRole,
                    scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the no School today and no school tomorrow settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNoSchoolTodayAndNoSchoolTomorrowSettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "8am,3pm";
            String shiftTime2 = "9am,6pm";
            String shiftTime3 = "9am,3pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "9am - 6pm";
            String scheduleMaxHours = "7";
            String selectWeekDayName = "Sat";
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3,
                    workRole, scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the no School today and no school tomorrow settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNoSchoolTodayAndNoSchoolTomorrowSettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "8am,1pm";
            String shiftTime2 = "10am,7pm";
            String shiftTime3 = "10am,2pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "9:30am - 7pm";
            String scheduleMaxHours = "7";
            String selectWeekDayName = "Sat";
            if (minorName.contains("14")
                    ||minorName.contains("15")) {
                shiftTime1 = "8am,3pm";
                shiftTime2 = "9am,6pm";
                shiftTime3 = "9am,3pm";
                workRole = minorWorkRole;
                scheduleFromToTime = "9am - 6pm";
                scheduleMaxHours = "7";
                selectWeekDayName = "Sat";
            }
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3, workRole,
                    scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the no School today and school tomorrow settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNoSchoolTodayAndSchoolTomorrowSettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "8am,3pm";
            String shiftTime2 = "10am,9pm";
            String shiftTime3 = "10am,4pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "9:30am - 9pm";
            String scheduleMaxHours = "9";
            String selectWeekDayName = "Sun";
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3, workRole, scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the no School today and school tomorrow settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNoSchoolTodayAndSchoolTomorrowSettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "8am,1pm";
            String shiftTime2 = "10am,8pm";
            String shiftTime3 = "10am,2pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "10am - 8pm";
            String scheduleMaxHours = "8";
            String selectWeekDayName = "Sun";
            if (minorName.contains("14")
                    ||minorName.contains("15")) {
                shiftTime1 = "8am,3pm";
                shiftTime2 = "10am,9pm";
                shiftTime3 = "10am,4pm";
                workRole = minorWorkRole;
                scheduleFromToTime = "9:30am - 9pm";
                scheduleMaxHours = "9";
                selectWeekDayName = "Sun";
            }
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3, workRole, scheduleFromToTime, scheduleMaxHours, false, selectWeekDayName);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
      }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the summer day settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSummerDaySettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "8am,3pm";
            String shiftTime2 = "10am,10pm";
            String shiftTime3 = "10am,4pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "10am - 10pm";
            String scheduleMaxHours = "10";
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3, workRole, scheduleFromToTime, scheduleMaxHours, true, null);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the summer day settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSummerDaySettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "8am,1pm";
            String shiftTime2 = "11am,10pm";
            String shiftTime3 = "11am,7pm";
            String workRole = minorWorkRole;
            String scheduleFromToTime = "10:30am - 10pm";
            String scheduleFromToTime2 = "10:30 AM - 10:00 PM";
            String scheduleMaxHours = "9";
            if (minorName.contains("14")
                    ||minorName.contains("15")) {
                shiftTime1 = "8am,3pm";
                shiftTime2 = "10am,10pm";
                shiftTime3 = "10am,4pm";
                workRole = minorWorkRole;
                scheduleFromToTime = "10am - 10pm";
                scheduleMaxHours = "10";
            }
            verifyDayOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, shiftTime3, workRole, scheduleFromToTime, scheduleMaxHours, true, null);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    public void verifyDayOvertimeViolationsForMinors(String minorName, String shiftTime1, String shiftTime2, String shiftTime3, String workRole,
                                                     String scheduleFromToTime, String scheduleMaxHours, boolean isSummerWeek, String selectWeekDayName) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        scheduleCommonPage.navigateToNextWeek();
        if (isSummerWeek){
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
        }
        Thread.sleep(5000);
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
//        if (isWeekGenerated){
//            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//        }
//        Thread.sleep(5000);
//        createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");
        if (!isWeekGenerated) {
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");
        }
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        String firstNameOfTM1 = cinemarkMinors.get(minorName);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1.split(" ")[0]);
        scheduleMainPage.saveSchedule();
        if(smartCardPage.isRequiredActionSmartCardLoaded()){
//            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            shiftOperatePage.deleteAllOOOHShiftInWeekView();
            scheduleMainPage.saveSchedule();
        }
        Thread.sleep(5000);
        createSchedulePage.publishActiveSchedule();

        //Create new shift with shift time is not during the minor setting for TM
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();

        //set shift time
        newShiftPage.moveSliderAtCertainPoint(shiftTime1.split(",")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(shiftTime1.split(",")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        if(!isSummerWeek){
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName(selectWeekDayName);
        }
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchWithOutSelectTM(firstNameOfTM1);

        //check the violation message in Status column
        String warningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
        SimpleUtils.assertOnFail("There should have minor warning message display as: Minor working hrs "+scheduleFromToTime+"! but actual is: "
                        +warningMessage,
                warningMessage.toLowerCase().contains(("Minor working hrs "+ scheduleFromToTime).toLowerCase()), false);
        Thread.sleep(5000);
        shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
        Thread.sleep(5000);
        //check the message in warning mode
        if(newShiftPage.ifWarningModeDisplay()){
//            String warningMessage1 = "As a minor, "+firstNameOfTM1.split(" ")[0]+" should be scheduled from "+ scheduleFromToTime;
            String warningMessage2 = "Please confirm that you want to make this change.";
            String messageInWarningMode = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
            if (messageInWarningMode.toLowerCase().contains(("Minor working hrs "+ scheduleFromToTime).toLowerCase())&& messageInWarningMode.contains(warningMessage2)){
                SimpleUtils.pass("The message in warning mode display correctly!");
            } else
                SimpleUtils.fail("The message in warning mode display incorrectly!  The expected message is :"+
                        ("Minor working hrs "+ scheduleFromToTime).toLowerCase() + warningMessage2+" The actual message is : " + messageInWarningMode, false);
            shiftOperatePage.clickOnAssignAnywayButton();
        } else
            SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);

        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        Thread.sleep(5000);
        //check the compliance smart card
        SimpleUtils.assertOnFail("The compliance smart card display correctly! ",
                smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
        smartCardPage.clickViewShift();
        //check the violation in i icon popup of new create shift
        WebElement newAddedShift = scheduleShiftTablePage.
                getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1.split(" ")[0]).get(0));
        String expectMessage = "Minor working hrs "+ scheduleFromToTime;
        String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).toString().replace(" AM", "am").replace(" PM", "pm").replace(":00", "")
                .replace(" 0", " ");
//                .replace(":30 ", ":30").toLowerCase();
        if (newAddedShift != null) {
            SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! the expect is: "+expectMessage
                            +" the actual is:"+actualMessage,
                    actualMessage.toLowerCase().contains(expectMessage.toLowerCase()), false);
        } else
            SimpleUtils.fail("Get new added shift failed! ", false);

        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.clickOnFilterBtn();
        //Create new shift with shift hours is more than minor setting for TM1
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1.split(" ")[0]);
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        Thread.sleep(3000);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        Thread.sleep(6000);
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();

        //set shift time
        newShiftPage.moveSliderAtCertainPoint(shiftTime2.split(",")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(shiftTime2.split(",")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        if(!isSummerWeek){
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName(selectWeekDayName);
        }
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchText(firstNameOfTM1);

        //check the message in warning mode
        if(newShiftPage.ifWarningModeDisplay()){
//            String warningMessage1 = "As a minor, "+firstNameOfTM1.split(" ")[0]+"'s daily schedule should not exceed "+ scheduleMaxHours +" hours";
            String warningMessage1 = "Minor daily max "+ scheduleMaxHours +" hrs";
            String warningMessage2 = "Please confirm that you want to make this change.";
            if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().replace(".0","").contains(warningMessage1)){
                SimpleUtils.pass("The message in warning mode display correctly! ");
            } else
                SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
            shiftOperatePage.clickOnAssignAnywayButton();
        } else
            SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);

        //check the violation message in Status column
        SimpleUtils.assertOnFail("There should have minor warning message display as: Minor daily max "+scheduleMaxHours+" hrs! ",
                shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor daily max "+scheduleMaxHours+" hrs"), false);

        newShiftPage.clickOnOfferOrAssignBtn();
        Thread.sleep(5000);
        scheduleMainPage.saveSchedule();
        //check the compliance smart card
        SimpleUtils.assertOnFail("The compliance smart card display correctly! ",
                smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
        smartCardPage.clickViewShift();
        //check the violation in i icon popup of new create shift
        newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1.split(" ")[0]).get(0));
        if (newAddedShift != null) {
            SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor daily max "+scheduleMaxHours+" hrs"), false);
        } else
            SimpleUtils.fail("Get new added shift failed", false);

        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        scheduleMainPage.clickOnFilterBtn();
        //Create new shift that not avoid the minor settings for TM1
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        Thread.sleep(3000);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1.split(" ")[0]);
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();

        //set shift time
        newShiftPage.moveSliderAtCertainPoint(shiftTime3.split(",")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(shiftTime3.split(",")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        if(!isSummerWeek){
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectWeekDaysByDayName(selectWeekDayName);
        }
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchWithOutSelectTM(firstNameOfTM1);
        SimpleUtils.assertOnFail("There should no minor warning message display when shift is not avoid the minor setting! ",
                !shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor"), false);
        shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
        if(newShiftPage.ifWarningModeDisplay()){
            warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
            if (!warningMessage.contains("Minor ")){
                SimpleUtils.pass("There is no minor warning message display on the warning mode when shift is not avoid the minor setting! ");
            } else
                SimpleUtils.fail("There should no minor warning message display warning mode when shift is not avoid the minor setting! ", false);
            shiftOperatePage.clickOnAssignAnywayButton();
        } else
            SimpleUtils.pass("There is no minor warning message display on search TM page shift is not avoid the minor setting! ");

        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();
        Thread.sleep(5000);
        //check the violation in i icon popup of new create shift
        newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1.split(" ")[0]).get(0));
        Thread.sleep(3000);
        if (newAddedShift != null) {
            SimpleUtils.assertOnFail("There should no minor warning message display on the i icon when shift is not avoid the minor setting! ",
                    !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor"), false);
        } else
            SimpleUtils.fail("Get new added shift failed! ", false);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the school week settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSchoolWeekSettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "10am,1pm";
            String shiftTime2 = "10am,4pm";
            int needCreateShiftsNumber1 = 4;
            int needCreateShiftsNumber2 = 2;
            String workRole = minorWorkRole;
            String maxOfDays = "4";
            String maxOfScheduleHours = "15";
            verifyWeekOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, workRole, needCreateShiftsNumber1,
                    needCreateShiftsNumber2, maxOfDays, maxOfScheduleHours, true, false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
     }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the non school week settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNonSchoolWeekSettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "11am,1pm";
            String shiftTime2 = "10am,4pm";
            int needCreateShiftsNumber1 = 5;
            int needCreateShiftsNumber2 = 2;
            String workRole = minorWorkRole;
            String maxOfDays = "5";
            String maxOfScheduleHours = "16";
            verifyWeekOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, workRole, needCreateShiftsNumber1,
                    needCreateShiftsNumber2, maxOfDays, maxOfScheduleHours, false, true);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the summer week settings for the Minors of Age 14 or 15")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSummerWeekSettingsForTheMinorsOfAge14Or15AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = "Minor14";
            String shiftTime1 = "11am,1pm";
            String shiftTime2 = "10am,5pm";
            int needCreateShiftsNumber1 = 6;
            int needCreateShiftsNumber2 = 2;
            String workRole = minorWorkRole;
            String maxOfDays = "6";
            String maxOfScheduleHours = "17";
            verifyWeekOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, workRole, needCreateShiftsNumber1,
                    needCreateShiftsNumber2, maxOfDays, maxOfScheduleHours, false, false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the school week settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSchoolWeekSettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "11am,1pm";
            String shiftTime2 = "11am,4pm";
            int needCreateShiftsNumber1 = 6;
            int needCreateShiftsNumber2 = 4;
            String workRole = minorWorkRole;
            String maxOfDays = "6";
            String maxOfScheduleHours = "18";
            if (minorName.contains("14")
                    || minorName.contains("15")) {
                shiftTime1 = "10am,1pm";
                shiftTime2 = "10am,4pm";
                needCreateShiftsNumber1 = 4;
                needCreateShiftsNumber2 = 2;
                workRole = minorWorkRole;
                maxOfDays = "4";
                maxOfScheduleHours = "15";
            }
            verifyWeekOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, workRole, needCreateShiftsNumber1,
                    needCreateShiftsNumber2, maxOfDays, maxOfScheduleHours, true, false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the non school week settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNonSchoolWeekSettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "11am,1pm";
            String shiftTime2 = "11am,4pm";
            int needCreateShiftsNumber1 = 4;
            int needCreateShiftsNumber2 = 3;
            String workRole = minorWorkRole;
            String maxOfDays = "4";
            String maxOfScheduleHours = "16";
            if (minorName.contains("14")
                    || minorName.contains("15")) {
                shiftTime1 = "11am,1pm";
                shiftTime2 = "10am,4pm";
                needCreateShiftsNumber1 = 5;
                needCreateShiftsNumber2 = 2;
                workRole = minorWorkRole;
                maxOfDays = "5";
                maxOfScheduleHours = "16";
            }
            verifyWeekOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, workRole, needCreateShiftsNumber1,
                    needCreateShiftsNumber2, maxOfDays, maxOfScheduleHours, false, true);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the summer week settings for the Minors of Age 16 or 17")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSummerWeekSettingsForTheMinorsOfAge16Or17AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String minorName = getMinorName();
            String shiftTime1 = "11am,1pm";
            String shiftTime2 = "11am,5pm";
            int needCreateShiftsNumber1 = 5;
            int needCreateShiftsNumber2 = 3;
            String workRole = minorWorkRole;
            String maxOfDays = "5";
            String maxOfScheduleHours = "17";
            if (minorName.contains("14")
                    || minorName.contains("15")) {
                shiftTime1 = "11am,1pm";
                shiftTime2 = "10am,5pm";
                needCreateShiftsNumber1 = 6;
                needCreateShiftsNumber2 = 2;
                workRole = minorWorkRole;
                maxOfDays = "6";
                maxOfScheduleHours = "17";
            }
            verifyWeekOvertimeViolationsForMinors(minorName, shiftTime1, shiftTime2, workRole, needCreateShiftsNumber1,
                    needCreateShiftsNumber2, maxOfDays, maxOfScheduleHours, false, false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    public void verifyWeekOvertimeViolationsForMinors(String minorName, String shiftTime1, String shiftTime2, String workRole,
                                                      int needCreateShiftsNumber1, int needCreateShiftsNumber2,
                                                      String maxOfDays, String maxOfScheduleHours,
                                                      boolean isSchoolWeek, boolean isNonSchoolWeek) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

        if (isSchoolWeek){
            scheduleCommonPage.navigateToNextWeek();
        } else if (isNonSchoolWeek){
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
        } else {
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
        }
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
//        if (isWeekGenerated){
//            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//        }
//        Thread.sleep(3000);
//        createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");
        if (!isWeekGenerated) {
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "06:00AM", "11:00PM");
        }
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        String firstNameOfTM1 = cinemarkMinors.get(minorName);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1.split(" ")[0]);
        scheduleMainPage.saveSchedule();

        //Create new shift with shift time is not during the minor setting for TM
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();

        //set shift time
        newShiftPage.moveSliderAtCertainPoint(shiftTime1.split(",")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(shiftTime1.split(",")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(needCreateShiftsNumber1);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();
        Thread.sleep(3000);
        scheduleMainPage.saveSchedule();
        Thread.sleep(3000);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint(shiftTime1.split(",")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(shiftTime1.split(",")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(needCreateShiftsNumber1, needCreateShiftsNumber1, needCreateShiftsNumber1);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.clickClearAssignmentsLink();
        newShiftPage.searchText(firstNameOfTM1);

        //check the message in warning mode
        if(newShiftPage.ifWarningModeDisplay()){
            String warningMessage1 = "Minor weekly max "+ maxOfDays +" days";
            String warningMessage2 = "Please confirm that you want to make this change.";
            String actualMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
            if (actualMessage.contains(warningMessage1)
                    && actualMessage.contains(warningMessage2)){
                SimpleUtils.pass("The message in warning mode display correctly! ");
            } else
                SimpleUtils.fail("The message in warning mode display incorrectly! "+ "the expect is:" +warningMessage1 +" and "+ warningMessage2
                        + " the actual is: "+ actualMessage, false);
            shiftOperatePage.clickOnAssignAnywayButton();
        } else
            SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);


        //check the violation message in Status column
        String message = shiftOperatePage.getTheMessageOfTMScheduledStatus();
        SimpleUtils.assertOnFail("There should have minor warning message display as: Minor weekly max "
                        +maxOfDays+"days! The actual is: "+message,
                message.contains("Minor weekly max "+ maxOfDays+ " days"), false);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();

        //check the compliance smart card
        SimpleUtils.assertOnFail("The compliance smart card display correctly! ",
                smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
        smartCardPage.clickViewShift();
        //check the violation in i icon popup of new create shift
        WebElement newAddedShift = scheduleShiftTablePage.getOneDayShiftByName(needCreateShiftsNumber1, firstNameOfTM1.split(" ")[0]).get(0);
        if (newAddedShift != null) {
            SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max "+ maxOfDays+ " days"), false);
        } else
            SimpleUtils.fail("Get new added shift failed! ", false);

        //to close the i icon popup
        createSchedulePage.publishActiveSchedule();
        smartCardPage.verifyClearFilterFunction();
        //Create new shift with shift hours is more than minor setting for TM1
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1.split(" ")[0]);
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint(shiftTime2.split(",")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(shiftTime2.split(",")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectSpecificWorkDay(needCreateShiftsNumber2);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByName(firstNameOfTM1);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();


        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint(shiftTime2.split(",")[1], ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.moveSliderAtCertainPoint(shiftTime2.split(",")[0], ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
        newShiftPage.clearAllSelectedDays();
        newShiftPage.selectDaysByIndex(needCreateShiftsNumber2, needCreateShiftsNumber2, needCreateShiftsNumber2);
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchText(firstNameOfTM1);

        //check the message in warning mode
        if(newShiftPage.ifWarningModeDisplay()){
            String warningMessage1 = "Minor weekly max "+ maxOfScheduleHours +" hrs";
            String warningMessage2 = "Please confirm that you want to make this change.";
            if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                    && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                SimpleUtils.pass("The message in warning mode display correctly! ");
            } else
                SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
            shiftOperatePage.clickOnAssignAnywayButton();
        } else
            SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);

        //check the violation message in Status column
        SimpleUtils.assertOnFail("There should have minor warning message display as: Minor weekly max "+maxOfScheduleHours+" hrs! ",
                shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor weekly max "+maxOfScheduleHours+" hrs"), false);

        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();

        //check the violation in i icon popup of new create shift
        SimpleUtils.assertOnFail("The compliance smart card display correctly! ",
                smartCardPage.verifyComplianceShiftsSmartCardShowing(), false);
        smartCardPage.clickViewShift();
        newAddedShift = scheduleShiftTablePage.getOneDayShiftByName(needCreateShiftsNumber2, firstNameOfTM1.split(" ")[0]).get(0);
        if (newAddedShift != null) {
            SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max "+maxOfScheduleHours+" hrs"), false);
        } else
            SimpleUtils.fail("Get new added shift failed", false);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "validate school calendar default month")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchoolCalendarDefaultMonthAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            int randomDigits = (new Random()).nextInt(100);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            // Go to School Calendars sub tab
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            // Click on Create New Calendar button, verify the Cancel and Save button display correctly
            teamPage.clickOnCreateNewCalendarButton();

            // Verify the Session Start and Session End fields are mandatory fields
            teamPage.verifyCreateCalendarLoaded();
            teamPage.verifySessionStartNEndIsMandatory();

            // Click on School Session Start
            teamPage.clickOnSchoolSessionStart();

            //Check the defaul start and end month in calendar
            List<String> calendarCurrentStartAndEndTime = teamPage.getCalendarCurrentStartAndEndTime();
            String startMonth = calendarCurrentStartAndEndTime.get(0).split(" ")[0];
            String endMonth = calendarCurrentStartAndEndTime.get(1).split(" ")[0];
            SimpleUtils.assertOnFail("The calendar default start month display incorrectly! The expected month is: August, the actual month is: "
                    + startMonth, startMonth.equalsIgnoreCase("August"), false);
            SimpleUtils.assertOnFail("The calendar default end month display incorrectly! The expected month is: May, the actual month is: "
                    + endMonth, endMonth.equalsIgnoreCase("May"), false);

            teamPage.clickOnCancelSchoolSessionCalendarBtn();
            teamPage.clickOnSchoolSessionEnd();
            calendarCurrentStartAndEndTime = teamPage.getCalendarCurrentStartAndEndTime();
            startMonth = calendarCurrentStartAndEndTime.get(0).split(" ")[0];
            endMonth = calendarCurrentStartAndEndTime.get(1).split(" ")[0];
            SimpleUtils.assertOnFail("The calendar default start month display incorrectly! The expected month is: August, the actual month is: "
                    + startMonth, startMonth.equalsIgnoreCase("August"), false);
            SimpleUtils.assertOnFail("The calendar default end month display incorrectly! The expected month is: August, the actual month is: "
                    + endMonth, endMonth.equalsIgnoreCase("May"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "validate the main calendar info on the page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheMainCalendarInfoOnThePageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

            // Go to School Calendars sub tab
            teamPage.clickOnTeamSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue());
            SimpleUtils.assertOnFail("Team page 'School Calendars' sub tab not loaded",
                    teamPage.verifyActivatedSubTab(TeamTest.TeamPageSubTabText.SchoolCalendars.getValue()), false);

            // Click on Create New Calendar button, verify the Cancel and Save button display correctly
            teamPage.clickOnCreateNewCalendarButton();

            // Verify the Session Start and Session End fields are mandatory fields
            teamPage.verifyCreateCalendarLoaded();
            teamPage.verifySessionStartNEndIsMandatory();

            // Click on School Session Start
            teamPage.clickOnSchoolSessionStart();

            //Select start: Jan, end Fed
            List<String> calendarCurrentStartAndEndTime = teamPage.getCalendarCurrentStartAndEndTime();
            String startYear = calendarCurrentStartAndEndTime.get(0).split(" ")[1];
            String endYear = calendarCurrentStartAndEndTime.get(1).split(" ")[1];
            String startDate = startYear + " Jan 1";
            String endDate = endYear + " Feb 1";
            teamPage.selectSchoolSessionStartAndEndDate(startDate, endDate);
            teamPage.clickOnSaveSchoolSessionCalendarBtn();

            //Check the main calendar page, the first month is Jan, the last month is Dec
            List<String> calendarMonthNames = teamPage.getAllCalendarMonthNames();
            SimpleUtils.assertOnFail("The first month display incorrectly, the expected first month is: January, the actual first month is: " +
                    calendarMonthNames.get(0).split(" ")[0], calendarMonthNames.get(0).split(" ")[0].equalsIgnoreCase("January"), false);
            SimpleUtils.assertOnFail("The last month display incorrectly, the expected last month is: December, the actual last month is: " +
                            calendarMonthNames.get(calendarMonthNames.size()-1).split(" ")[0],
                    calendarMonthNames.get(calendarMonthNames.size()-1).split(" ")[0].equalsIgnoreCase("December"), false);

            //Select start: Aug, end Oct
            teamPage.clickOnSchoolSessionStart();
            startDate = startYear + " Aug 1";
            endDate = endYear + " Oct 1";
            teamPage.selectSchoolSessionStartAndEndDate(startDate, endDate);
            teamPage.clickOnSaveSchoolSessionCalendarBtn();

            //Check the main calendar page, the first month is Jan, the last month is Dec
            calendarMonthNames = teamPage.getAllCalendarMonthNames();
            SimpleUtils.assertOnFail("The first month display incorrectly, the expected first month is: August , the actual first month is: " +
                    calendarMonthNames.get(0).split(" ")[0], calendarMonthNames.get(0).split(" ")[0].equalsIgnoreCase("August"), false);
            SimpleUtils.assertOnFail("The last month display incorrectly, the expected last month is: July, the actual last month is: " +
                            calendarMonthNames.get(calendarMonthNames.size()-1).split(" ")[0],
                    calendarMonthNames.get(calendarMonthNames.size()-1).split(" ")[0].equalsIgnoreCase("July"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Minor profile page when minors has been assigned minor rule template-OP")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorProfilePageWhenMinorsHasBeenAssignedMinorRuleTemplateOnOPAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
//            ToggleAPI.updateToggle(Toggles.MinorRulesTemplate.getValue(), getUserNameNPwdForCallingAPI().get(0),
//                    getUserNameNPwdForCallingAPI().get(1), true);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //SM or admin log in, go OP side
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToUserManagementPage();
            configurationPage.goToDynamicEmployeeGroupPage();
            //delete all dynamic employee group
            String minor16GroupTitle = "Minor16Group-ForAuto";
            configurationPage.deleteSpecifyDynamicEmployeeGroupsInList(minor16GroupTitle);
            //create a User Group for 13 year old minors
            List<String> groupCriteriaList = new ArrayList<>();
//            groupCriteriaList.clear();
//            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.Minor.getValue()+ "-"
//                    +OpsPortalConfigurationPage.DynamicEmployeeGroupMinorCriteria.LessThan14.getValue());
//            String minor13GroupTitle = "Minor13Group-ForAuto";
//            String minor13GroupDescription = "Minor13-Description-ForAuto";
//            configurationPage.createNewDynamicEmployeeGroup(minor13GroupTitle, minor13GroupDescription,
//                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MinorRule.getValue(), groupCriteriaList);
//            Thread.sleep(10000);
//            String minor14GroupTitle = "Minor14Group-ForAuto";
//            String minor14GroupDescription = "Minor14-Description-ForAuto";
//            groupCriteriaList.clear();
//            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.Minor.getValue()+ "-"
//                    +OpsPortalConfigurationPage.DynamicEmployeeGroupMinorCriteria.Equals14.getValue());
//            configurationPage.createNewDynamicEmployeeGroup(minor14GroupTitle, minor14GroupDescription,
//                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MinorRule.getValue(), groupCriteriaList);
//            Thread.sleep(10000);
//
//            String minor15GroupDescription = "Minor15-Description-ForAuto";
//            groupCriteriaList.clear();
//            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.Minor.getValue()+ "-"
//                    +OpsPortalConfigurationPage.DynamicEmployeeGroupMinorCriteria.Equals15.getValue());
//            configurationPage.createNewDynamicEmployeeGroup(minor15GroupTitle, minor15GroupDescription,
//                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MinorRule.getValue(), groupCriteriaList);
//            Thread.sleep(10000);
//            String minor16GroupTitle = "Minor16Group-ForAuto";
            String minor16GroupDescription = "Minor16-Description-ForAuto";
            groupCriteriaList.clear();
            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.Minor.getValue()+ "-"
                    +OpsPortalConfigurationPage.DynamicEmployeeGroupMinorCriteria.Equals16.getValue());
            configurationPage.createNewDynamicEmployeeGroup(minor16GroupTitle, minor16GroupDescription,
                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MinorRule.getValue(), groupCriteriaList);
//            Thread.sleep(10000);
//            String minor17GroupTitle = "Minor17Group-ForAuto";
//            String minor17GroupDescription = "Minor17-Description-ForAuto";
//            groupCriteriaList.clear();
//            groupCriteriaList.add(OpsPortalConfigurationPage.DynamicEmployeeGroupCriteria.Minor.getValue()+ "-"
//                    +OpsPortalConfigurationPage.DynamicEmployeeGroupMinorCriteria.Equals17.getValue());
//            configurationPage.createNewDynamicEmployeeGroup(minor17GroupTitle, minor17GroupDescription,
//                    OpsPortalConfigurationPage.DynamicEmployeeGroupLabels.MinorRule.getValue(), groupCriteriaList);
//            Thread.sleep(10000);
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
//            String minor15TemplateName = "Minor15Template-ForAuto";
//            configurationPage.archiveOrDeleteTemplate(minor15TemplateName);
//            String minor17TemplateName = "Minor17Template-ForAuto";
//            createMinor16N17TemplateAndSetMinorSettings(minor17TemplateName, minor17GroupTitle);
//            String minor14TemplateName = "Minor14Template-ForAuto";
//            createMinor13N14N15TemplateAndSetMinorSettings(minor14TemplateName, minor14GroupTitle);
//            String minor13TemplateName = "Minor13Template-ForAuto";
//            createMinor13N14N15TemplateAndSetMinorSettings(minor13TemplateName, minor13GroupTitle);

//            createMinor13N14N15TemplateAndSetMinorSettings(minor15TemplateName, minor15GroupTitle);
            String minor16TemplateName = "Minor16Template-ForAuto";
            configurationPage.archiveOrDeleteTemplate(minor16TemplateName);
            createMinor16N17TemplateAndSetMinorSettings(minor16TemplateName, minor16GroupTitle);

//            Thread.sleep(3000);
//            switchToConsoleWindow();
//            LoginPage loginPage = new ConsoleLoginPage();
//            loginPage.verifyNewTermsOfServicePopUp();
//            //wait for 5 mins for catch
//            TeamPage teamPage = pageFactory.createConsoleTeamPage();
//            for (int i = 0; i< 10; i++) {
//                teamPage.goToTeam();
//                teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
//                Thread.sleep(60000);
//            }
//            //refresh cache
//            CacheAPI.refreshTemplateCache("stoneman@legion.co", "admin11.a");
//            String minor13Name = cinemarkMinors.get("Minor13");
//            String minor14Name = cinemarkMinors.get("Minor14");
//            String minor15Name = cinemarkMinors.get("Minor15");
//            String minor16Name = cinemarkMinors.get("Minor16");
//            String minor17Name = cinemarkMinors.get("Minor17");
//            verifyTemplateNameOnProfilePage(minor17Name, minor17TemplateName);
//            verifyTemplateNameOnProfilePage(minor14Name, minor14TemplateName);
//            verifyTemplateNameOnProfilePage(minor13Name, minor13TemplateName);
//            verifyTemplateNameOnProfilePage(minor15Name, minor15TemplateName);
//            verifyTemplateNameOnProfilePage(minor16Name, minor16TemplateName);
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
            refreshCachesAfterChangeTemplate();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    private void createMinor13N14N15TemplateAndSetMinorSettings (String templateName, String dynamicGroup) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
        cinemarkMinorPage.setMinorRuleByWeek(minorRuleWeekType.School_Week.getValue(),cinemarkSetting14N15.get(minorRuleWeekType.School_Week.getValue()).split(",")[0],cinemarkSetting14N15.get(minorRuleWeekType.School_Week.getValue()).split(",")[1]);
        cinemarkMinorPage.setMinorRuleByWeek(minorRuleWeekType.Non_School_Week.getValue(),cinemarkSetting14N15.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[0],cinemarkSetting14N15.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[1]);
        cinemarkMinorPage.setMinorRuleByWeek(minorRuleWeekType.Summer_Week.getValue(),cinemarkSetting14N15.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[0],cinemarkSetting14N15.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[1]);
        Thread.sleep(5000);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue(), cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.Summer_Day.getValue(), cinemarkSetting14N15.get(minorRuleDayType.Summer_Day.getValue()).split(",")[0], cinemarkSetting14N15.get(minorRuleDayType.Summer_Day.getValue()).split(",")[1], cinemarkSetting14N15.get(minorRuleDayType.Summer_Day.getValue()).split(",")[2]);
        configurationPage.selectOneDynamicGroup(dynamicGroup);
        configurationPage.clickOnTemplateDetailTab();
        cinemarkMinorPage.saveOrPublishTemplate(templateAction.Publish_Now.getValue());
        Thread.sleep(3000);
    }

    private void createMinor16N17TemplateAndSetMinorSettings (String templateName, String dynamicGroup) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
        cinemarkMinorPage.setMinorRuleByWeek(minorRuleWeekType.School_Week.getValue(),cinemarkSetting16N17.get(minorRuleWeekType.School_Week.getValue()).split(",")[0],cinemarkSetting16N17.get(minorRuleWeekType.School_Week.getValue()).split(",")[1]);
        cinemarkMinorPage.setMinorRuleByWeek(minorRuleWeekType.Non_School_Week.getValue(),cinemarkSetting16N17.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[0],cinemarkSetting16N17.get(minorRuleWeekType.Non_School_Week.getValue()).split(",")[1]);
        cinemarkMinorPage.setMinorRuleByWeek(minorRuleWeekType.Summer_Week.getValue(),cinemarkSetting16N17.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[0],cinemarkSetting16N17.get(minorRuleWeekType.Summer_Week.getValue()).split(",")[1]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue(), cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",")[2]);
        cinemarkMinorPage.setMinorRuleByDay(minorRuleDayType.Summer_Day.getValue(), cinemarkSetting16N17.get(minorRuleDayType.Summer_Day.getValue()).split(",")[0], cinemarkSetting16N17.get(minorRuleDayType.Summer_Day.getValue()).split(",")[1], cinemarkSetting16N17.get(minorRuleDayType.Summer_Day.getValue()).split(",")[2]);
        configurationPage.selectOneDynamicGroup(dynamicGroup);
        configurationPage.clickOnTemplateDetailTab();
        cinemarkMinorPage.saveOrPublishTemplate(templateAction.Publish_Now.getValue());
        Thread.sleep(3000);
    }


    private void verifyTemplateNameOnProfilePage (String minorName, String templateName) throws Exception {
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        int i = 0;
        String name = "None";
        while (i< 20 && name.equals("None")) {
            Thread.sleep(60000);
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(minorName);
            name = profileNewUIPage.getMinorRuleTemplateName();
            CacheAPI.refreshTemplateCache(getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1));
            i++;
        }
        SimpleUtils.assertOnFail("The minor rule template name of "+templateName+" display incorrectly! The actual is: "+name,
                name.equalsIgnoreCase(templateName), false);

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Minor profile page when the TM does not have minor rule template associated")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorProfilePageWhenTheTMDoesNotHaveMinorRuleTemplateAssociatedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
//            ToggleAPI.updateToggle(Toggles.MinorRulesTemplate.getValue(), getUserNameNPwdForCallingAPI().get(0),
//                    getUserNameNPwdForCallingAPI().get(1), true);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //SM or admin log in, go OP side
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
            String minor13TemplateName = "Minor13Template-ForAuto";
            configurationPage.archiveOrDeleteTemplate(minor13TemplateName);
            switchToConsoleWindow();

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String minor13Name = "Minor13 Cinemark13";
            teamPage.searchAndSelectTeamMemberByName(minor13Name);
            SimpleUtils.assertOnFail("The minor rule template name of Minor 13 display incorrectly! ",
                    profileNewUIPage.getMinorRuleTemplateName().equals("None"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate auto scheduler will respect the Minors Rules")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAutoScheduleRespectTheMinorsRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

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
            scheduleCommonPage.clickOnDayView();
            String minor14 = cinemarkMinors.get("Minor14");
            String minor15 = cinemarkMinors.get("Minor15");
            String minor16 = cinemarkMinors.get("Minor16");
            String minor17 = cinemarkMinors.get("Minor17");
            List<String> minorNames = new ArrayList<>();
            minorNames.add(minor14);
            minorNames.add(minor15);
            minorNames.add(minor16);
            minorNames.add(minor17);
            for (int i = 0; i< 7;i++) {
                scheduleCommonPage.navigateDayViewWithIndex(i);
                for (String minorName : minorNames) {
                    List<WebElement> shifts = scheduleShiftTablePage.getShiftsByNameOnDayView(minorName.split(" ")[0]);
                    if (shifts.size() >0) {
                        switch (scheduleCommonPage.getActiveDayInfo().get("weekDay")) {
                            case "Fri":
                            case "Thu":
                                for (WebElement shift : shifts) {
                                    int index = scheduleShiftTablePage.getTheIndexOfShift(shift);
                                    String shiftTime = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index).get(2);
                                    String[] settingTimes;
                                    if (minorName.contains("14") || minorName.contains("15")) {
                                        settingTimes =  cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "") +"-" + settingTimes[1].replace(" ", "")),
                                                false);

                                    } else if (minorName.contains("16") || minorName.contains("17")) {
                                        settingTimes =  cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_NoSchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "") +"-" + settingTimes[1].replace(" ", "")),
                                                false);
                                    }
                                }
                                break;
                            case "Sat":
                                for (WebElement shift : shifts) {
                                    int index = scheduleShiftTablePage.getTheIndexOfShift(shift);
                                    String shiftTime = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index).get(2);
                                    String[] settingTimes;
                                    if (minorName.contains("14") || minorName.contains("15")) {
                                        settingTimes =  cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "") +"-" + settingTimes[1].replace(" ", "")),
                                                false);

                                    } else if (minorName.contains("16") || minorName.contains("17")) {
                                        settingTimes =  cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_NoSchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "") +"-" + settingTimes[1].replace(" ", "")),
                                                false);
                                    }
                                }
                                break;
                            case "Sun":
                                for (WebElement shift : shifts) {
                                    int index = scheduleShiftTablePage.getTheIndexOfShift(shift);
                                    String shiftTime = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index).get(2);
                                    String[] settingTimes;
                                    if (minorName.contains("14") || minorName.contains("15")) {
                                        settingTimes =  cinemarkSetting14N15.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "") +"-" + settingTimes[1].replace(" ", "")),
                                                false);

                                    } else if (minorName.contains("16") || minorName.contains("17")) {
                                        settingTimes =  cinemarkSetting16N17.get(minorRuleDayType.NoSchoolToday_SchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "") +"-" + settingTimes[1].replace(" ", "")),
                                                false);
                                    }
                                }
                                break;
                            default:
                                for (WebElement shift : shifts) {
                                    int index = scheduleShiftTablePage.getTheIndexOfShift(shift);
                                    String shiftTime = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index).get(2);
                                    String[] settingTimes;
                                    if (minorName.contains("14") || minorName.contains("15")) {
                                        settingTimes =  cinemarkSetting14N15.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "") +"-" + settingTimes[1].replace(" ", "")),
                                                false);

                                    } else if (minorName.contains("16") || minorName.contains("17")) {
                                        settingTimes =  cinemarkSetting16N17.get(minorRuleDayType.SchoolToday_SchoolTomorrow.getValue()).split(",");
                                        SimpleUtils.assertOnFail("The shift time should belong to the minor setting period time! ",
                                                BasePage.isBelongPeriodTime(shiftTime, settingTimes[0].replace(" ", "")+"-" + settingTimes[1].replace(" ", "")),
                                                false);
                                    }
                                }
                                break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "validate recommendation will respect the Minors Rules")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRecommendationRespectTheMinorsRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String minor14 = cinemarkMinors.get("Minor14");
            String minor15 = cinemarkMinors.get("Minor15");
            String minor16 = cinemarkMinors.get("Minor16");
            String minor17 = cinemarkMinors.get("Minor17");
            List<String> minorNames = new ArrayList<>();
            minorNames.add(minor14);
            minorNames.add(minor15);
            minorNames.add(minor16);
            minorNames.add(minor17);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(minor14.split(" ")[0]);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(minor15.split(" ")[0]);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(minor16.split(" ")[0]);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(minor17.split(" ")[0]);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(minorWorkRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());

            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            for (String minorName: minorNames) {
                SimpleUtils.assertOnFail("The minor: "+ minorName +" should list in recommendation list! ",
                        shiftOperatePage.checkIfTMExistsInRecommendedTab(minorName), false);
            }

            newShiftPage.clickOnBackButton();
            newShiftPage.moveSliderAtCertainPoint("7am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            for (String minorName: minorNames) {
                SimpleUtils.assertOnFail("The minor: "+ minorName +" should NOT list in recommendation list! ",
                        !shiftOperatePage.checkIfTMExistsInRecommendedTab(minorName), false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    private String getMinorName(){
        String minorName = "";
        LocalDate date = now();
        String dayOfWeek = date.getDayOfWeek().toString();
        if (dayOfWeek.equalsIgnoreCase("Monday")
                ||dayOfWeek.equalsIgnoreCase("Tuesday")
                ||dayOfWeek.equalsIgnoreCase("Wednesday") ) {
            minorName = minorNames.Minor15.getValue();
        } else
            minorName = minorNames.Minor17.getValue();
        SimpleUtils.pass("Get minor user: "+minorName+ " successfully! ");
        return minorName;
    }
}