package com.legion.tests.core;

import com.legion.api.abSwitch.ABSwitchAPI;
import com.legion.api.abSwitch.AbSwitches;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import static com.legion.utils.MyThreadLocal.*;


public class FTSERelevantTest extends TestBase {

    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, String> schedulePolicyData = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SchedulingPoliciesData.json");
    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
    private static HashMap<String, Object[][]> kendraScott2TeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("KendraScott2TeamMembers.json");
    private static HashMap<String, Object[][]> cinemarkWkdyTeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("CinemarkWkdyTeamMembers.json");
    private static String opWorkRole = scheduleWorkRoles.get("RETAIL_ASSOCIATE");
    private static String controlWorkRole = scheduleWorkRoles.get("RETAIL_RENTAL_MGMT");
    private static String opEnterprice = "CinemarkWkdy_Enterprise";

    public enum weekCount {
        Zero(0),
        One(1),
        Two(2),
        Three(3),
        Four(4),
        Five(5);
        private final int value;

        weekCount(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public enum filtersIndex {
        Zero(0),
        One(1),
        Two(2),
        Three(3),
        Four(4),
        Five(5);
        private final int value;

        filtersIndex(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }


    public enum dayCount{
        Seven(7);
        private final int value;
        dayCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }

    public enum schedulePlanningWindow{
        Eight(8);
        private final int value;
        schedulePlanningWindow(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }

    public enum sliderShiftCount {
        SliderShiftStartCount(2),
        SliderShiftEndTimeCount(10),
        SliderShiftEndTimeCount2(14),
        SliderShiftEndTimeCount3(40);
        private final int value;
        sliderShiftCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }

    public enum staffingOption{
        OpenShift("Auto"),
        ManualShift("Manual"),
        AssignTeamMemberShift("Assign Team Member");
        private final String value;
        staffingOption(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum overviewWeeksStatus{
        NotAvailable("Not Available"),
        Draft("Draft"),
        Guidance("Guidance"),
        Published("Published"),
        Finalized("Finalized");

        private final String value;
        overviewWeeksStatus(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }


    public enum SchedulePageSubTabText {
        Overview("OVERVIEW"),
        Forecast("FORECAST"),
        ProjectedSales("PROJECTED SALES"),
        StaffingGuidance("STAFFING GUIDANCE"),
        Schedule("SCHEDULE"),
        MySchedule("MY SCHEDULE"),
        TeamSchedule("TEAM SCHEDULE"),
        ProjectedTraffic("PROJECTED TRAFFIC");
        private final String value;

        SchedulePageSubTabText(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum weekViewType {
        Next("Next"),
        Previous("Previous");
        private final String value;

        weekViewType(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum shiftSliderDroppable{
        StartPoint("Start"),
        EndPoint("End");
        private final String value;
        shiftSliderDroppable(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum scheduleHoursAndWagesData{
        scheduledHours("scheduledHours"),
        budgetedHours("budgetedHours"),
        otherHours("otherHours"),
        wagesBudgetedCount("wagesBudgetedCount"),
        wagesScheduledCount("wagesScheduledCount");
        private final String value;
        scheduleHoursAndWagesData(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum scheduleGroupByFilterOptions{
        groupbyAll("Group by All"),
        groupbyWorkRole("Group by Work Role"),
        groupbyTM("Group by TM"),
        groupbyJobTitle("Group by Job Title"),
        groupbyLocation("Group by Location"),
        groupbyDayParts("Group by Day Parts");
        private final String value;
        scheduleGroupByFilterOptions(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum dayWeekOrPayPeriodViewType{
        Next("Next"),
        Previous("Previous");
        private final String value;
        dayWeekOrPayPeriodViewType(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum schedulingPoliciesShiftIntervalTime{
        FifteenMinutes("15 minutes"),
        ThirtyMinutes("30 minutes");
        private final String value;
        schedulingPoliciesShiftIntervalTime(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum usersAndRolesSubTabs{
        AllUsers("Users"),
        AccessByJobTitles("Access Roles"),
        Badges("Badges");
        private final String value;
        usersAndRolesSubTabs(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum tasksAndWorkRolesSubTab{
        WorkRoles("Work Roles"),
        LaborCalculator("Labor Calculator");
        private final String value;
        tasksAndWorkRolesSubTab(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum dayWeekOrPayPeriodCount{
        Zero(0),
        One(1),
        Two(2),
        Three(3),
        Four(4),
        Five(5);
        private final int value;
        dayWeekOrPayPeriodCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }

    public enum schedulingPolicyGroupsTabs{
        FullTimeSalariedExempt("Full Time Salaried Exempt"),
        FullTimeSalariedNonExempt("Full Time Salaried Non Exempt"),
        FullTimeHourlyNonExempt("Full Time Hourly Non Exempt"),
        PartTimeHourlyNonExempt("Part Time Hourly Non Exempt");
        private final String value;
        schedulingPolicyGroupsTabs(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum DayOfWeek {
        Mon,
        Tue,
        Wed,
        Thu,
        Fri,
        Sat,
        Sun;
    }

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) {
        try {
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            // ToggleAPI.updateToggle(Toggles.EnableMultiWorkRolePerShiftSCH.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1), false);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ignore the OT violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnoreTheDayOTAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration){
//                //Go to compliance page and set day OT violation
//                workRole = "Training";
//                controlsNewUIPage.clickOnControlsConsoleMenu();
//                controlsNewUIPage.clickOnControlsComplianceSection();
//                compliancePage.turnOnOrTurnOffDayOTToggle(true);
//                compliancePage.editDayOTSetting("8 hours", "single work day", true);
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToSubLocationsInLocationsPage();
                locationsPage.searchLocation(location);
                ;
                SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
                locationsPage.clickOnLocationInLocationResult(location);
                locationsPage.clickOnConfigurationTabOfLocation();
                HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                Thread.sleep(3000);
                compliancePage.turnOnOrTurnOffDayOTToggle(true);
                compliancePage.editDayOTSetting("8 hours", "single work day", true);
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }

            //Go to the schedule page
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create a new shift and assign it to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any WeekOT violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
            SimpleUtils.assertOnFail("The DayOT violation message is showing!", !(complianceMessage.contains("daily overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ignore the OT violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnoreTheWeekOTAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration){
//                //Go to compliance page and set week OT violation
//                workRole = "Training";
//                controlsNewUIPage.clickOnControlsConsoleMenu();
//                controlsNewUIPage.clickOnControlsComplianceSection();
//                compliancePage.turnOnOrTurnOffWeeklyOTToggle(true);
//                compliancePage.editWeeklyOTSetting("40 hours");
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToSubLocationsInLocationsPage();
                locationsPage.searchLocation(location);
                ;
                SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
                locationsPage.clickOnLocationInLocationResult(location);
                locationsPage.clickOnConfigurationTabOfLocation();
                HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                Thread.sleep(3000);
                compliancePage.turnOnOrTurnOffWeeklyOTToggle(true);
                compliancePage.editWeeklyOTSetting("40");
                configurationPage.publishNowTheTemplate();
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }

            //Go to the schedule view table
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create a new shift and assign it to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.inputTeamMember(tmPartialName);
            newShiftPage.clickOnBackButton();
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any WeekOT violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(5));
            SimpleUtils.assertOnFail("The weekOT violation message is showing!", !(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ignore the OT violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnoreThe7thConsecutiveOTAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration){
//                //Go to the compliance page and set the 7th Consecutive OT
//                workRole = "Training";
//                controlsNewUIPage.clickOnControlsConsoleMenu();
//                controlsNewUIPage.clickOnControlsComplianceSection();
//                compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
//                compliancePage.editConsecutiveOTSetting("7th","always", true);
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToSubLocationsInLocationsPage();
                locationsPage.searchLocation(location);
                ;
                SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
                locationsPage.clickOnLocationInLocationResult(location);
                locationsPage.clickOnConfigurationTabOfLocation();
                HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                Thread.sleep(3000);
                compliancePage.turnOnOrTurnOff7thConsecutiveOTToggle(true);
                compliancePage.editConsecutiveOTSetting("7th", "always", true);
                configurationPage.publishNowTheTemplate();
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }

            //Go to the schedule view table
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.inputTeamMember(tmPartialName);
            newShiftPage.clickOnBackButton();
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            newShiftPage.clickOnOkButtonOnErrorDialog();
            scheduleMainPage.saveSchedule();

            //Verify no any 7th consecutive violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(6));
            SimpleUtils.assertOnFail("The The7th Consecutive OT message is showing!", !(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ignore the OT violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnoreThe24HrsOTAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;

            //Go to the compliance page and set the 24Hrs OT
//            if (isLocationUsingControlsConfiguration){
//                workRole = "Training";
//                controlsNewUIPage.clickOnControlsConsoleMenu();
//                controlsNewUIPage.clickOnControlsComplianceSection();
//                compliancePage.turnOnOrTurnOffDayOTToggle(true);
//                compliancePage.editDayOTSetting("8 hours","24 hour period",true);
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToSubLocationsInLocationsPage();
                locationsPage.searchLocation(location);
                ;
                SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
                locationsPage.clickOnLocationInLocationResult(location);
                locationsPage.clickOnConfigurationTabOfLocation();
                HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                Thread.sleep(3000);
                compliancePage.turnOnOrTurnOffDayOTToggle(true);
                compliancePage.editDayOTSetting("8 hours", "24 hour period", true);
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }

            //Go to the schedule view table
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE T.");
            scheduleMainPage.saveSchedule();

            //Create a new shift and assign it to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("9pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any WeekOT violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
            SimpleUtils.assertOnFail("The DayOT violation message is showing!", !(complianceMessage.contains("daily overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees support other violations except OT")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeeSupportClopeningViolationAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Go to the compliance page and set the clopening
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;

            //Set the clopening violation
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//                controlsNewUIPage.clickOnControlsConsoleMenu();
//                controlsNewUIPage.clickOnControlsSchedulingPolicies();
//                SimpleUtils.assertOnFail("Scheduling Policies Page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
//                controlsNewUIPage.selectClopeningHours(12);
//
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
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
                controlsNewUIPage.selectClopeningHoursOP("12");
                Thread.sleep(60000);
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }

            //Go to the schedule view table
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("8:00AM", "11:00PM");

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE T.");
            scheduleMainPage.saveSchedule();

            //Create a new shift and assign it to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any dayOT violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
            SimpleUtils.assertOnFail("The Clopening violation message is not showing!", complianceMessage.contains("Clopening"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees support other violations except OT")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeeSupportMaxShiftViolationAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//            }
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the same FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            newShiftPage.clickOnOkButtonOnErrorDialog();
            scheduleMainPage.saveSchedule();

            //Verify Max Shifts violation displays after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(6));
            SimpleUtils.assertOnFail("The Max Shifts violation message is not showing!",
                    complianceMessage.contains("Max shift per week violation") || complianceMessage.contains("Max days per week"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees support other violations except OT")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeeSupportRoleViolationAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Rental Service Tech";
//            }else {
                workRole = "2ND JOB–ASSISTANT MANAGER";
//            }

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create one shift and assign it to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify role violation displays after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
            SimpleUtils.assertOnFail("The role violation message is not showing!", complianceMessage.contains("Role Violation"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ingnore Schedule Agreement Policy overtimes violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnore4X10AgreementAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//            }
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("8:00AM", "11:00PM");

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any Schedule Agreement Policy overtimes violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(5));
            SimpleUtils.assertOnFail("The Schedule Agreement Policy overtimes violation is showing!", !(complianceMessage.contains("daily overtime"))&&!(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ingnore Schedule Agreement Policy overtimes violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnore5X8AgreementAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester2";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//            }
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE T.");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any Schedule Agreement Policy overtimes violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(6));
            SimpleUtils.assertOnFail("The Schedule Agreement Policy overtimes violation is showing!", !(complianceMessage.contains("daily overtime"))&&!(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ingnore Schedule Agreement Policy overtimes violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnore40X8AgreementAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester3";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//            }
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE T.");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("9pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any Schedule Agreement Policy overtimes violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(6));
            SimpleUtils.assertOnFail("The Schedule Agreement Policy overtimes violation is showing!", !(complianceMessage.contains("daily overtime"))&&!(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ingnore Schedule Agreement Policy overtimes violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnore40hrsAgreementAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester4";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//            }
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any Schedule Agreement Policy overtimes violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(6));
            SimpleUtils.assertOnFail("The Schedule Agreement Policy overtimes violation is showing!", !(complianceMessage.contains("daily overtime"))&&!(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ingnore Schedule Agreement Policy overtimes violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnoreNoOverTimeAgreementAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester5";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//            }

            //Go to the schedule view table
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("8:00AM", "11:00PM");

            //Delete all auto-generated shifts for the FTSE employee
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any Schedule Agreement Policy overtimes violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(6));
            SimpleUtils.assertOnFail("The Schedule Agreement Policy overtimes violation is showing!", !(complianceMessage.contains("daily overtime"))&&!(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees ingnore Schedule Agreement Policy overtimes violations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesIgnore50hrsPerWeekAgreementAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester6";
            String workRole;

            //Go to the schedule view table
//            if (isLocationUsingControlsConfiguration) {
//                workRole = "Training";
//            }else {
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//            }
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("8:00AM", "11:00PM");

            //Delete all auto-generated shifts for the FTSE employee
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("FTSE");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts and assign them to the FTSE employee
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify no any Schedule Agreement Policy overtimes violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(6));
            SimpleUtils.assertOnFail("The Schedule Agreement Policy overtimes violation is showing!", !(complianceMessage.contains("weekly overtime")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees support other violations except OT")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesSupportSplitShiftsViolationAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set split shift violation in the Compliance page
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;

            //Set the split shifts violation
//            if (isLocationUsingControlsConfiguration){
//                //Go to Controls page and set split shift violation
//                workRole = "Training";
//                controlsNewUIPage.clickOnControlsConsoleMenu();
//                controlsNewUIPage.clickOnControlsComplianceSection();
//                controlsNewUIPage.turnOnOrTurnOffSplitShiftToggle(true);
//                controlsNewUIPage.editSplitShiftPremium("1", "60", true);
//            }else {
                //Go to OP page
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToSubLocationsInLocationsPage();
                locationsPage.searchLocation(location);
                ;
                SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
                locationsPage.clickOnLocationInLocationResult(location);
                locationsPage.clickOnConfigurationTabOfLocation();
                HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                Thread.sleep(3000);
                controlsNewUIPage.turnOnOrTurnOffSplitShiftToggle(true);
                controlsNewUIPage.editSplitShiftPremium("1", "60", true);
                configurationPage.publishNowTheTemplate();

                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }
//            }

            //Go to the schedule view table
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all auto-generated shifts for the FTSE employee
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.saveSchedule();

            //Create one shift and assign it to the FTSE employee
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            Thread.sleep(3000);
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Add second shift to the same FTSE employee and check the violation on the assign page
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("9pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("1pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickClearAssignmentsLink();
            String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
            SimpleUtils.assertOnFail("Should get split shift warning message!", shiftWarningMessage.toLowerCase().contains("split shift"), false);
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            //Verify the split shift violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(1));
            SimpleUtils.assertOnFail("The split shift violation is not showing!", complianceMessage.contains("Split Shift"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify FTSE employees support other violations except OT")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFTSEEmployeesSupportSpreadOfHoursViolationAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set spread of hours violation in the Compliance page
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration){
//                //Go to Controls page
//                workRole = "Training";
//                controlsNewUIPage.clickOnControlsConsoleMenu();
//                controlsNewUIPage.clickOnControlsComplianceSection();
//                controlsNewUIPage.turnOnOrTurnOffSpreadOfHoursToggle(true);
//                controlsNewUIPage.editSpreadOfHoursPremium("1", "10", true);
//            } else {
                //Go to OP page
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToSubLocationsInLocationsPage();
                locationsPage.searchLocation(location);
                ;
                SimpleUtils.assertOnFail("Locations not searched out Successfully!", locationsPage.verifyUpdateLocationResult(location), false);
                locationsPage.clickOnLocationInLocationResult(location);
                locationsPage.clickOnConfigurationTabOfLocation();
                HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Compliance");
                configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Compliance"), "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                Thread.sleep(3000);
                controlsNewUIPage.turnOnOrTurnOffSpreadOfHoursToggle(true);
                controlsNewUIPage.editSpreadOfHoursPremium("1", "10", true);
                configurationPage.publishNowTheTemplate();

                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }
//            }

            //Go to the schedule view table
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "10:00PM");

            //Delete all auto-generated shifts for the FTSE employee
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.saveSchedule();

            //Create one shift and assign it to the FTSE employee
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("10pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickClearAssignmentsLink();
            SimpleUtils.assertOnFail("The spread of hour violation fail to display in Status column! The actual message is: " + shiftOperatePage.getTheMessageOfTMScheduledStatus(),
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().toLowerCase().contains("spread of hours"), false);
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Verify the spread of hours violation after saving.
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(tmPartialName);
            List<String> complianceMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(scheduleShiftTablePage.getTheShiftByIndex(0));
            SimpleUtils.assertOnFail("The spread of hours violation is not showing!", complianceMessage.contains("Spread of hours"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the short shifts displayed correctly with the improved UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyShiftInfoInDayViewAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set different role for different enterprises
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            String tmPartialName = "Tester1";
            String workRole= "TEAM MEMBER CORPORATE-THEATRE";

            //Go to the schedule view table
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");

            //Switch to the DayView and delete all existed shifts.
            scheduleCommonPage.clickOnDayView();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
            scheduleMainPage.saveSchedule();

            //Create the particular shift and verify the face bubble is displayed
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(8000);
            SimpleUtils.assertOnFail("The shift's info doesn't include face bubble!", scheduleShiftTablePage.isInfoIconLoaded(0)
                    &&scheduleShiftTablePage.isProfileIconLoaded(0), false);


            //Modify the shift's box to cover the shift length & shift duration
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            String inputStartTime = "08:00 AM";
            String inputEndTime = "11:45 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("The shift's info doesn't include shift length or shift duration!",
                    scheduleShiftTablePage.isInfoIconLoaded(0)&&scheduleShiftTablePage.isProfileIconLoaded(0)
                            &&scheduleShiftTablePage.isShiftLengthLoaded(0)&&scheduleShiftTablePage.isShiftDurationInBoxLoaded(0), false);

            //Modify the shift's box to cover the total shift length
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            inputStartTime = "08:00 AM";
            inputEndTime = "12:00 PM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("The shift's info doesn't include total shift length!",
                    scheduleShiftTablePage.isInfoIconLoaded(0)&&scheduleShiftTablePage.isProfileIconLoaded(0)
                            &&scheduleShiftTablePage.isShiftLengthLoaded(0)&&scheduleShiftTablePage.isShiftDurationInBoxLoaded(0)
                            &&scheduleShiftTablePage.isShiftTotalLengthLoaded(0), false);

            //Modify the shift's box to cover the profile name, work role and job title
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            inputStartTime = "08:00 AM";
            inputEndTime = "02:00 PM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            SimpleUtils.assertOnFail("The shift's info doesn't include TM name,work role and job title!",
                    scheduleShiftTablePage.isInfoIconLoaded(0)&&scheduleShiftTablePage.isProfileIconLoaded(0)
                            &&scheduleShiftTablePage.isShiftLengthLoaded(0)&&scheduleShiftTablePage.isShiftDurationInBoxLoaded(0)
                            &&scheduleShiftTablePage.isShiftTotalLengthLoaded(0)&&scheduleShiftTablePage.isProfileNameAndWorkRoleLoaded(0)
                            &&scheduleShiftTablePage.isShiftJobTitleLoaded(0), false);
            scheduleMainPage.saveSchedule();

            //Modify the shift's box to the minimum, verify the info icon is displayed
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShiftInDayView(0);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            inputStartTime = "08:00 AM";
            inputEndTime = "08:15 AM";
            editShiftPage.inputStartOrEndTime(inputStartTime, true);
            editShiftPage.inputStartOrEndTime(inputEndTime, false);
            editShiftPage.clickOnUpdateButton();
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("The shift's info doesn't include TM's info icon!",
                    scheduleShiftTablePage.isInfoIconLoaded(0), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the short shifts displayed correctly with the improved UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyShiftInfoInViewProfileAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set different roles for different enterprises
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration){
//                //Go to Controls page
//                workRole = "Training";
//            } else {
                //Go to OP page
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }
//            }

            //Go to the schedule view table
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");

            //Switch to the WeekView and delete all existed shifts.
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            Thread.sleep(1000);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts to cover different info
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();

            newShiftPage.selectDaysByIndex(0,0,0);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("08:15am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1,1,1);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("12:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2,2,2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("11:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.publishOrRepublishSchedule();

            //Open the TM's View Profile and verify the shifts under the Availability section
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnViewProfile();

            //Verify the short shift include info icon
//            SimpleUtils.assertOnFail("The shift's info doesn't include info icon!",
//                    scheduleShiftTablePage.isShiftInfoIconInAvailabilityLoaded(0)&&!(scheduleShiftTablePage.isShiftJobTitleInAvailabilityLoaded(0))
//                            &&!(scheduleShiftTablePage.isShiftLocationInAvailabilityLoaded(0)), false);

            //Verify the medium shift include shift duration & job title
            SimpleUtils.assertOnFail("The shift's info doesn't include shift duration & job title!",
                    !(scheduleShiftTablePage.isShiftInfoIconInAvailabilityLoaded(1))&&scheduleShiftTablePage.isShiftJobTitleMediumInAvailabilityLoaded(1)
                            &&!(scheduleShiftTablePage.isShiftLocationInAvailabilityLoaded(1)), false);

            //Verify the long shift include shift location
            SimpleUtils.assertOnFail("The shift's info doesn't include total shift location!",
                    !(scheduleShiftTablePage.isShiftInfoIconInAvailabilityLoaded(2))&&scheduleShiftTablePage.isShiftJobTitleLargeInAvailabilityLoaded(2)
                            &&scheduleShiftTablePage.isShiftLocationInAvailabilityLoaded(2), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the short shifts displayed correctly with the improved UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyShiftInfoInPersonalMyScheduleAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set different roles for different enterprises
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration){
//                //Go to Controls page
//                workRole = "Training";
//            } else {
                //Go to OP page
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }
//            }

            //Go to the schedule view table
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");

            //Delete all auto-generated shifts for the FTSE employee
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts to cover different info
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(0,0,0);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("08:15am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1,1,1);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("10:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2,2,2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("12:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(3,3,3);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("02:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.publishOrRepublishSchedule();

            //Login the TM and verify the shifts on the MySchedule page
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            Thread.sleep(3000);
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
            Thread.sleep(3000);

//            SimpleUtils.assertOnFail("The first shift displays incorrectly!",
//                    scheduleShiftTablePage.isInfoIconLoaded(0)&&scheduleShiftTablePage.isProfileIconLoaded(0)
//                            &&!(scheduleShiftTablePage.isMyScheduleShiftLocationLoaded(0))&&!(scheduleShiftTablePage.isShiftLengthLoaded(0))
//                            &&!(scheduleShiftTablePage.isShiftDurationInBoxLoaded(0))&&scheduleShiftTablePage.isMyScheduleProfileNameLoaded(0)
//                            &&!(scheduleShiftTablePage.isMyScheduleShiftWorkRoleLoaded(0)), false);
//
//            SimpleUtils.assertOnFail("The second shift displays incorrectly!",
//                    scheduleShiftTablePage.isInfoIconLoaded(1)&&scheduleShiftTablePage.isProfileIconLoaded(0)
//                            &&!(scheduleShiftTablePage.isMyScheduleShiftLocationLoaded(1))&&!(scheduleShiftTablePage.isShiftLengthLoaded(1))
//                            &&scheduleShiftTablePage.isShiftDurationInBoxLoaded(1) &&!(scheduleShiftTablePage.isMyScheduleProfileNameLoaded(1))
//                            &&!(scheduleShiftTablePage.isMyScheduleShiftWorkRoleLoaded(1)), false);
//
//            SimpleUtils.assertOnFail("The third shift displays incorrectly!",
//                    scheduleShiftTablePage.isInfoIconLoaded(2)&&(scheduleShiftTablePage.isProfileIconLoaded(2))
//                            &&scheduleShiftTablePage.isMyScheduleShiftLocationLoaded(2)&&scheduleShiftTablePage.isShiftLengthLoaded(2)
//                            &&scheduleShiftTablePage.isShiftDurationInBoxLoaded(2) &&!(scheduleShiftTablePage.isMyScheduleProfileNameLoaded(2))
//                            &&!(scheduleShiftTablePage.isMyScheduleShiftWorkRoleLoaded(2)), false);
//
//            SimpleUtils.assertOnFail("The fourth shift displays incorrectly!",
//                    scheduleShiftTablePage.isInfoIconLoaded(3)&&scheduleShiftTablePage.isProfileIconLoaded(3)
//                            &&scheduleShiftTablePage.isMyScheduleShiftLocationLoaded(3)&&scheduleShiftTablePage.isShiftLengthLoaded(3)
//                            &&scheduleShiftTablePage.isShiftDurationInBoxLoaded(3) &&scheduleShiftTablePage.isMyScheduleProfileNameLoaded(3)
//                            &&scheduleShiftTablePage.isMyScheduleShiftWorkRoleLoaded(3), false);

            SimpleUtils.assertOnFail("The first shift displays incorrectly!",
                    scheduleShiftTablePage.isInfoIconLoaded(0)&&scheduleShiftTablePage.isProfileIconLoaded(0), false);

            SimpleUtils.assertOnFail("The second shift displays incorrectly!",
                    scheduleShiftTablePage.isInfoIconLoaded(1)&&scheduleShiftTablePage.isProfileIconLoaded(1), false);

            SimpleUtils.assertOnFail("The third shift displays incorrectly!",
                    scheduleShiftTablePage.isInfoIconLoaded(2)&&(scheduleShiftTablePage.isProfileIconLoaded(2))
                            &&scheduleShiftTablePage.isMyScheduleShiftLocationLoaded(2)&&scheduleShiftTablePage.isShiftLengthLoaded(2)
                            &&scheduleShiftTablePage.isShiftDurationInBoxLoaded(2), false);

            SimpleUtils.assertOnFail("The fourth shift displays incorrectly!",
                    scheduleShiftTablePage.isInfoIconLoaded(3)&&scheduleShiftTablePage.isProfileIconLoaded(3)
                            &&scheduleShiftTablePage.isMyScheduleShiftLocationLoaded(3)&&scheduleShiftTablePage.isShiftLengthLoaded(3)
                            &&scheduleShiftTablePage.isShiftDurationInBoxLoaded(3) &&scheduleShiftTablePage.isMyScheduleProfileNameLoaded(3)
                            &&scheduleShiftTablePage.isMyScheduleShiftWorkRoleLoaded(3), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the short shifts displayed correctly with the improved UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyShiftInfoInPersonalMyPreferenceAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set different roles for different enterprises
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            String workRole = "TEAM MEMBER CORPORATE-THEATRE";
            String tmPartialName = "Tester1";
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
//            String tmPartialName = "Tester1";
//            String workRole;

//            if (isLocationUsingControlsConfiguration){
//                //Go to Controls page
//                workRole = "Training";
//            } else {

//                //Go to OP page
//                workRole = "TEAM MEMBER CORPORATE-THEATRE";
//                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
//                    //Back to the console page
//                    switchToConsoleWindow();
//                }

//            }

            //Go to the schedule view table
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");

            //Delete all auto-generated shifts for the FTSE employee
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.saveSchedule();

            //Create multiple shifts to cover different info
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();

            newShiftPage.selectDaysByIndex(0,0,0);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("08:15am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1,1,1);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("12:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2,2,2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("06:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("08:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.publishOrRepublishSchedule();

            //Login the TM and verify the shifts on the MySchedule page
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            Thread.sleep(3000);
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //verify the shifts under the Availability section on the MyPreference page
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.getNickNameFromProfile();
            String myWorkPreferencesLabel = "My Work Preferences";
            profileNewUIPage.selectProfilePageSubSectionByLabel(myWorkPreferencesLabel);

            SimpleUtils.assertOnFail("The shift's info doesn't include info icon!",
                    !(scheduleShiftTablePage.isShiftInfoIconInAvailabilityLoaded(0))&&!(scheduleShiftTablePage.isShiftJobTitleMediumInAvailabilityLoaded(0))
                            &&!(scheduleShiftTablePage.isShiftLocationInAvailabilityLoaded(0)), false);

            //Verify the medium shift include shift duration & job title
            SimpleUtils.assertOnFail("The shift's info doesn't include shift duration & job title!",
                    !(scheduleShiftTablePage.isShiftInfoIconInAvailabilityLoaded(1))&&scheduleShiftTablePage.isShiftJobTitleMediumInAvailabilityLoaded(1)
                            &&!(scheduleShiftTablePage.isShiftLocationInAvailabilityLoaded(1)), false);

            //Verify the long shift include shift location
            SimpleUtils.assertOnFail("The shift's info doesn't include total shift location!",
                    !(scheduleShiftTablePage.isShiftInfoIconInAvailabilityLoaded(2))&&scheduleShiftTablePage.isShiftJobTitleLargeInAvailabilityLoaded(2)
                            &&scheduleShiftTablePage.isShiftLocationInAvailabilityLoaded(2), false);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the short shifts displayed correctly with the improved UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMaxShiftDurationInfoInDayViewAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set different roles for different enterprises
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration){
//                //Go to Controls page
//                workRole = "Training";
//            } else {
                //Go to OP page
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }
//            }

            //Go to the schedule view table
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Editing the operating day and save all actions.
//            List<String> weekDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
//            scheduleMainPage.goToEditOperatingHoursView();
//            scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "12:00AM","12:00AM");
//            scheduleMainPage.clickSaveBtnOnEditOpeHoursPage();
//            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Delete all existed shifts, then switch to the Day View.
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.saveSchedule();
            scheduleCommonPage.clickOnDayView();

            //Create the particular shift and verify relevant info is displayed in the shift box
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("9pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(true);
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(tmPartialName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            Thread.sleep(3000);
            SimpleUtils.assertOnFail("The shift's info doesn't include all relevant info!", scheduleShiftTablePage.isInfoIconLoaded(0)
                    &&scheduleShiftTablePage.isProfileIconLoaded(0)&&scheduleShiftTablePage.isShiftLengthLoaded(0)
                    &&scheduleShiftTablePage.isShiftDurationInBoxLoaded(0)&&scheduleShiftTablePage.isShiftTotalLengthLoaded(0)
                    &&scheduleShiftTablePage.isProfileNameAndWorkRoleLoaded(0)&&scheduleShiftTablePage.isShiftJobTitleLoaded(0), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the functionality of green/grey available")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheFunctionalityOfGreenOrGreyAvailableAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            //Set different roles for different enterprises
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            TeamPage consoleTeamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//            Boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String tmPartialName = "Tester1";
            String workRole;
//            if (isLocationUsingControlsConfiguration) {
//                //Go to Controls page
//                workRole = "Training";
//            } else {
                //Go to OP page
                workRole = "TEAM MEMBER CORPORATE-THEATRE";
                if (getDriver().getCurrentUrl().toLowerCase().contains(propertyMap.get(opEnterprice).toLowerCase())) {
                    //Back to the console page
                    switchToConsoleWindow();
                }
//            }

            //Go to the schedule view table and un-generate the schedule
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);

            //Go to the Team page and set work preference for the specific TM
            consoleTeamPage.goToTeam();
            consoleTeamPage.searchAndSelectTeamMemberByName(tmPartialName);
            consoleTeamPage.navigateToWorkPreferencesTab();
            consoleTeamPage.editOrUnLockAvailability();
            String hoursType = "When I prefer to work";
            String availabilityChangesRepeat = "repeat forward";
            profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
            profileNewUIPage.updateSpecificPreferredOrBusyHoursToAllWeek(hoursType);
            profileNewUIPage.saveMyAvailabilityEditMode(availabilityChangesRepeat);

            //Go to the schedule view table
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");

            //Delete all relevant TM's shifts
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String ftseTMShifts = "FTSE";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(ftseTMShifts);
            scheduleMainPage.saveSchedule();

            //Create a new shift and assign to the TM
            String greenIcon = "green";
            String greyIcon = "grey";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.isAddNewDayViewShiftButtonLoaded();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(false);
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(tmPartialName);
            newShiftPage.clickClearAssignmentsLink();
            Boolean colourMatch1 = greyIcon.equalsIgnoreCase(newShiftPage.getTMAvailableColourForAssignedShift());
            SimpleUtils.assertOnFail("The available icon's colour is incorrect!", colourMatch1,false);
            Thread.sleep(3000);

            newShiftPage.clickOnBackButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("5pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(false);
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(tmPartialName);
            newShiftPage.clickClearAssignmentsLink();
            Boolean colourMatch2 = greyIcon.equals(newShiftPage.getTMAvailableColourForAssignedShift());
            SimpleUtils.assertOnFail("The available icon's colour is incorrect!", colourMatch2,false);
            Thread.sleep(3000);

            newShiftPage.clickOnBackButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("12pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(false);
            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(tmPartialName);
            newShiftPage.clickClearAssignmentsLink();
            Boolean colourMatch3 = greenIcon.equalsIgnoreCase(newShiftPage.getTMAvailableColourForAssignedShift());
            SimpleUtils.assertOnFail("The available icon's colour is incorrect!", colourMatch3, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

}