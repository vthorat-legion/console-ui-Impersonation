package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.CompliancePage;
import com.legion.pages.DashboardPage;
import com.legion.pages.LocationSelectorPage;
import com.legion.pages.ProfileNewUIPage;
import com.legion.pages.TimeSheetPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class DMViewTest extends TestBase {

    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Dashboard functionality in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDashboardFunctionalityInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            // Validate the title
            dashboardPage.verifyHeaderOnDashboard();
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(districtName);
            locationSelectorPage.isDMView();

            // Validate the presence of district
            dashboardPage.validateThePresenceOfDistrict();

            // Validate the visibility of week
            dashboardPage.validateTheVisibilityOfWeek();

            // Validate welcome content
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String nickName = profileNewUIPage.getNickNameFromProfile();
            dashboardPage.verifyTheWelcomeMessageOfDM(nickName);

            // Validate changing districts on Dashboard
            locationSelectorPage.changeAnotherDistrictInDMView();
            districtName = dashboardPage.getCurrentDistrict();
            districtName = districtName.contains("\n")? districtName.split("\n")[0]:districtName;
            String districtOnDashboard = dashboardPage.getUpperfieldNameOnDashboard();
            if (districtName.equals(districtOnDashboard))
                SimpleUtils.pass("Dashboard Page: When the user selects a different district from the DM view, the data updates to reflect the selected district");
            else
                SimpleUtils.fail("When the user selects a different district from the DM view, the data doesn't update to reflect the selected district",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Refresh feature on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnDashboardInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            // Validate the presence of Refresh button
            dashboardPage.validateThePresenceOfRefreshButton();

            // Validate Refresh timestamp
            dashboardPage.validateRefreshTimestamp();

            // Validate Refresh when navigation back
            dashboardPage.validateRefreshWhenNavigationBack("Dashboard");

            // Validate Refresh function
            dashboardPage.validateRefreshFunction();

            // Validate Refresh performance
            dashboardPage.validateRefreshPerformance();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Projected Compliance widget on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyProjectedComplianceWidgetOnDashboardInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            SimpleUtils.assertOnFail("Project Compliance widget not loaded successfully",
                    dashboardPage.isProjectedComplianceWidgetDisplay(), false);
            //Validate the content of Projected Compliance widget
            dashboardPage.verifyTheContentInProjectedComplianceWidget();

            //Validate the data of Projected Compliance widget
            String totalViolationHrsFromProjectedComplianceWidget =
                    dashboardPage.getTheTotalViolationHrsFromProjectedComplianceWidget();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            dashboardPage.clickOnViewViolationsLink();
            String totalViolationHrsFromCompliancePage =
                    compliancePage.getTheTotalViolationHrsFromSmartCard().split(" ")[0];
            SimpleUtils.assertOnFail("Project Compliance widget not loaded successfully",
                    totalViolationHrsFromProjectedComplianceWidget.equals(totalViolationHrsFromCompliancePage), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Timesheet Approval Rate widget on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimesheetApprovalRateWidgetOnDashboardInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!",
                    dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            SimpleUtils.assertOnFail("Timesheet Approval Rate widget not loaded successfully",
                    dashboardPage.isTimesheetApprovalRateWidgetDisplay(), false);

            // Validate the content on Timesheet Approval Rate widget on TA env
            dashboardPage.validateTheContentOnTimesheetApprovalRateWidgetInUpperfieldView();

            // Validate status value of Timesheet Approval Rate widget on TA env
            dashboardPage.validateStatusValueOfTimesheetApprovalRateWidget();
            // todo due to SCH-2636

            // Validate data on Timesheet Approval Rate widget on TA env
            List<String> timesheetApprovalRateOnDMViewDashboard = dashboardPage.getTimesheetApprovalRateOnDMViewWidget();
            dashboardPage.clickOnViewTimesheets();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            List<String> timesheetApprovalRateFromSmartCardOnDMViewTimesheet = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard();
            dashboardPage.validateDataOnTimesheetApprovalRateWidget(timesheetApprovalRateOnDMViewDashboard, timesheetApprovalRateFromSmartCardOnDMViewTimesheet);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Compliance Violations widget on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComplianceViolationsWidgetOnDashboardInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            SimpleUtils.assertOnFail("Compliance Violations widget not loaded successfully", dashboardPage.isComplianceViolationsWidgetDisplay(), false);

            // Validate the content on Compliance Violations widget on TA env
            dashboardPage.validateTheContentOnComplianceViolationsWidgetInUpperfield();

            // Validate the data in Compliance Violations widget with TA between dashboard and compliance
            List<String> complianceViolationsOnDMViewDashboard = dashboardPage.getComplianceViolationsOnDashboard();
            dashboardPage.clickOnViewViolationsLink();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            List<String> complianceViolationsFromOnDMViewCompliance = compliancePage.getComplianceViolationsOnSmartCard();
            dashboardPage.validateDataOnComplianceViolationsWidget(complianceViolationsOnDMViewDashboard, complianceViolationsFromOnDMViewCompliance);
            //todo SCH-1906： [Dashboard] Compliance violation widget -> the numbers of violation is incorrect

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Payroll Projection widget on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPayrollProjectionWidgetOnDashboardInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //  Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            dashboardPage.clickOnDashboardConsoleMenu();
            SimpleUtils.assertOnFail("Payroll Projection widget loaded fail! ", dashboardPage.isPayrollProjectionWidgetDisplay(), false);

            // Validate the content on Payroll Projection widget on TA env
            dashboardPage.validateTheContentOnPayrollProjectionWidget(true);

            // Validate the date on Payroll Projection widget on TA env
            String weekOnPayrollProjectionWidget = dashboardPage.getWeekOnPayrollProjectionWidget();
            String forecastKPIOnPayrollProjectionWidget = dashboardPage.getBudgetComparisonOnPayrollProjectionWidget();
            dashboardPage.clickOnViewSchedulesOnPayrollProjectWidget();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            String currentWeekInDMViewSchedule = scheduleDMViewPage.getCurrentWeekInDMView();
            String forecastKPIInDMViewSchedule = scheduleDMViewPage.getBudgetComparisonInDMView();
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Payroll Projection widget not loaded successfully", dashboardPage.isPayrollProjectionWidgetDisplay(), false);
            dashboardPage.validateWeekOnPayrollProjectionWidget(weekOnPayrollProjectionWidget, currentWeekInDMViewSchedule);
            dashboardPage.validateBudgetComparisonOnPayrollProjectionWidget(forecastKPIOnPayrollProjectionWidget, forecastKPIInDMViewSchedule);

            // Validate today's time line on Payroll Projection widget on TA env
            dashboardPage.validateTodayAtTimeOnPayrollProjectionWidget();

            // Validate the future Budget Comparison on Payroll Projection widget on TA env
            dashboardPage.validateTheFutureBudgetComparisonOnPayrollProjectionWidget();

            // Validate hours tooltips of Payroll Projection widget on TA env
            dashboardPage.validateHoursTooltipsOfPayrollProjectionWidget();

            // Validate the data in Payroll Projection widget with TA
            HashMap<String, Integer> theSumOfValues = dashboardPage.getTheSumOfValuesOnPayrollProjectionWidget();
            List<String> dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            if ((Integer.valueOf(dataOnLocationSummaryWidget.get(0)) - theSumOfValues.get("Budgeted") < 14)
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1)))
                    && theSumOfValues.get("Projected").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(2))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in Location Summary widget for Budgeted, Scheduled and Projected");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in Location Summary widget for Budgeted, Scheduled and Projected",false);

            // Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            dashboardPage.clickOnDashboardConsoleMenu();
            SimpleUtils.assertOnFail("Payroll Projection widget loaded fail! ", dashboardPage.isPayrollProjectionWidgetDisplay(), false);

            // Validate the content in Payroll Projection widget with TA
            dashboardPage.validateTheContentOnPayrollProjectionWidget(false);

            // Validate the data in Payroll Projection widget with TA
            theSumOfValues = dashboardPage.getTheSumOfValuesOnPayrollProjectionWidget();
            if (Integer.valueOf(dataOnLocationSummaryWidget.get(0)) - theSumOfValues.get("Budgeted") < 14
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1)))
                    && theSumOfValues.get("Projected").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(2))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in Location Summary widget for Guidance, Scheduled and Projected");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in Location Summary widget for Guidance, Scheduled and Projected",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Refresh feature on Schedule in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnScheduleInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);

            // Validate the presence of Refresh button
            scheduleDMViewPage.validateThePresenceOfRefreshButton();

            // Validate Refresh timestamp
            scheduleDMViewPage.validateRefreshTimestamp();

            // Validate Refresh when navigation back
            scheduleDMViewPage.validateRefreshWhenNavigationBack();

            // Validate Refresh function
            scheduleDMViewPage.validateRefreshFunction();

            // Validate Refresh performance
            scheduleDMViewPage.validateRefreshPerformance();

            // Validate Refresh function for past weeks
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.validateRefreshTimestamp();
            scheduleDMViewPage.clickOnRefreshButton();
            scheduleDMViewPage.validateRefreshFunction();

            // Validate Refresh function for current/future weeks
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.validateRefreshTimestamp();
            scheduleDMViewPage.clickOnRefreshButton();
            scheduleDMViewPage.validateRefreshFunction();

            // Validate Refresh reflects schedule change
            while (!scheduleDMViewPage.isNotStartedScheduleDisplay()) {
                scheduleCommonPage.navigateToNextWeek();
            }
            if (scheduleDMViewPage.isNotStartedScheduleDisplay()) {
                String notStartedLocation = scheduleDMViewPage.getLocationsWithNotStartedSchedules().get(0);
                scheduleDMViewPage.clickOnLocationNameInDMView(notStartedLocation);
                SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
                boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
                if (!isWeekGenerated) {
                    createSchedulePage.createScheduleForNonDGFlowNewUI();
                }
                locationSelectorPage.reSelectDistrict(districtName);
                scheduleDMViewPage.clickOnRefreshButton();
                String scheduleStatus = scheduleDMViewPage.getScheduleStatusForGivenLocation(notStartedLocation);
                if (scheduleStatus.equals("In Progress"))
                    SimpleUtils.pass("Schedule Page: After the first refreshing, it is \"In Progress\" status");
                else
                    SimpleUtils.fail("Schedule Page: After the first refreshing, it isn't \"In Progress\" status", false);
                scheduleDMViewPage.clickOnLocationNameInDMView(notStartedLocation);
                SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
                createSchedulePage.publishActiveSchedule();
                locationSelectorPage.reSelectDistrict(districtName);
                scheduleDMViewPage.clickOnRefreshButton();
                scheduleStatus = scheduleDMViewPage.getScheduleStatusForGivenLocation(notStartedLocation);
                if (scheduleStatus.equals("Published"))
                    SimpleUtils.pass("Schedule Page: After the second refreshing, it is \"Published\" status");
                else
                    SimpleUtils.fail("Schedule Page: After the second refreshing, it isn't \"Published\" status", false);
            } else
                SimpleUtils.report("Schedule Page: There are no Not Started schedules in the current and upcoming weeks");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Refresh feature on Timesheet in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnTimesheetInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimeSheetPageLoaded(), false);

            // Validate the presence of Refresh button
            timeSheetPage.validateThePresenceOfRefreshButton();

            // Validate Refresh timestamp
            timeSheetPage.validateRefreshTimestamp();

            // Validate Refresh when navigation back
            timeSheetPage.validateRefreshWhenNavigationBack();

            // Validate Refresh function
            timeSheetPage.validateRefreshFunction();

            // Validate Refresh performance
            timeSheetPage.validateRefreshPerformance();

            // Validate Refresh function for past weeks
            timeSheetPage.navigateToPreviousWeek();
            timeSheetPage.validateRefreshTimestamp();
            timeSheetPage.clickOnRefreshButton();
            timeSheetPage.validateRefreshFunction();

            // Validate Refresh reflects timesheet change
            String rateWithin24OnSmartCardBeforeChange = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard().get(0);
            String rateInAnalyticsTableBeforeChange = timeSheetPage.getTimesheetApprovalForGivenLocationInDMView(location);
            timeSheetPage.clickOnGivenLocation(location);
            if (!timeSheetPage.isWorkerDisplayInTimesheetTable()) {
                timeSheetPage.navigateToSchedule();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
                if (!isWeekGenerated) {
                    createSchedulePage.createScheduleForNonDGFlowNewUI();
                }
                createSchedulePage.publishActiveSchedule();
                timeSheetPage.clickOnTimeSheetConsoleMenu();
                SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimeSheetPageLoaded(), false);
            }
            timeSheetPage.approveAnyTimesheet();
            String rateInTimesheetDueAfterApprove = timeSheetPage.getApprovalRateFromTIMESHEETDUESmartCard();
            dashboardPage.navigateToDashboard();
            locationSelectorPage.reSelectDistrict(districtName);
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimeSheetPageLoaded(), false);
            timeSheetPage.clickOnRefreshButton();
            String rateWithin24OnSmartCardAfterChange = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard().get(0);
            String rateInAnalyticsTableAfterChange = timeSheetPage.getTimesheetApprovalForGivenLocationInDMView(location);
            if (rateInTimesheetDueAfterApprove.equals(rateInAnalyticsTableAfterChange)
                    && !rateWithin24OnSmartCardBeforeChange.equals(rateWithin24OnSmartCardAfterChange)
            && !rateInAnalyticsTableBeforeChange.equals(rateInAnalyticsTableAfterChange)) {
                SimpleUtils.report("The rate <24 Hrs on smart card before change is " + rateWithin24OnSmartCardBeforeChange);
                SimpleUtils.report("The rate <24 Hrs on smart card after change is " + rateWithin24OnSmartCardAfterChange);
                SimpleUtils.report("The rate in analytics table for given location before change is " + rateInAnalyticsTableBeforeChange);
                SimpleUtils.report("The rate in analytics table for given location after change is " + rateInAnalyticsTableAfterChange);
                SimpleUtils.report("The rate in SM view for given location after updating is " + rateInTimesheetDueAfterApprove);
                SimpleUtils.pass("Timesheet Page: The timesheet approval rate on smart card and in analytics table gets updating according to the new change");
                        } else
                // SimpleUtils.fail("Timesheet Page: The timesheet approval rate on smart card and in analytics table doesn't get updating according to the new change",false);
                SimpleUtils.warn("TA-5015: Approved percentage cannot get updating after approving on TIMESHEET DUE smart card");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Refresh feature on Compliance in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnComplianceInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        String districtName = dashboardPage.getCurrentDistrict();
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.reSelectDistrict(districtName);

        CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        compliancePage.clickOnComplianceConsoleMenu();
        SimpleUtils.assertOnFail("Compliance page not loaded successfully", compliancePage.isCompliancePageLoaded(), false);

        // Validate the presence of Refresh button
        compliancePage.validateThePresenceOfRefreshButton();

        // Validate Refresh timestamp
        compliancePage.validateRefreshTimestamp();

        // Validate Refresh when navigation back
        compliancePage.validateRefreshWhenNavigationBack();

        // Validate Refresh function
        compliancePage.validateRefreshFunction();

        // Validate Refresh performance
        compliancePage.validateRefreshPerformance();

        // Validate Refresh function for past weeks
        compliancePage.navigateToPreviousWeek();
        compliancePage.validateRefreshTimestamp();
        compliancePage.clickOnRefreshButton();
        compliancePage.validateRefreshFunction();

        // Validate Refresh function for future weeks
        compliancePage.navigateToNextWeek();
        compliancePage.navigateToNextWeek();
        compliancePage.validateRefreshTimestamp();
        compliancePage.clickOnRefreshButton();
        compliancePage.validateRefreshFunction();

        // Validate Refresh reflects timesheet change
        compliancePage.navigateToPreviousWeek();
        String totalViolationHrsBeforeChange = compliancePage.getTheTotalViolationHrsFromSmartCard();
        List<String> dataFromComplianceTableBeforeChange = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimeSheetPageLoaded(), false);
        timeSheetPage.clickOnGivenLocation(location);
        if (!timeSheetPage.isWorkerDisplayInTimesheetTable()) {
            timeSheetPage.navigateToSchedule();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            createSchedulePage.publishActiveSchedule();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimeSheetPageLoaded(), false);
        }
//        timeSheetPage.clickOnDayView();
//        SimpleUtils.pass("Timesheet Day View: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
//        timeSheetPage.navigateDayWeekOrPayPeriodToPastOrFuture(TimeSheetTest.dayWeekOrPayPeriodViewType.Previous.getValue()
//                , TimeSheetTest.dayWeekOrPayPeriodCount.One.getValue());
//        SimpleUtils.pass("Timesheet Day View: '"+ timeSheetPage.getActiveDayWeekOrPayPeriod() +"' loaded.");
        timeSheetPage.openFirstPendingTimeSheet();
        timeSheetPage.addTimeClockCheckInOutOnDetailWithDefaultValue(location);
        timeSheetPage.saveTimeSheetDetail();
        timeSheetPage.clickOnTimeSheetDetailBackBtn();
//        timeSheetPage.reaggregateTimesheet();

        dashboardPage.navigateToDashboard();
        locationSelectorPage.reSelectDistrict(districtName);
        dashboardPage.clickOnRefreshButton();
        SimpleUtils.assertOnFail("Compliance Violations widget not loaded successfully", dashboardPage.isComplianceViolationsWidgetDisplay(), false);
        compliancePage.clickOnComplianceConsoleMenu();
        compliancePage.clickOnRefreshButton();
        String totalViolationHrsAfterChange = compliancePage.getTheTotalViolationHrsFromSmartCard();
        List<String> dataFromComplianceTableAfterChange = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);

        SimpleUtils.report("The total violation hours before change is " + totalViolationHrsBeforeChange);
        SimpleUtils.report("The total violation hours after change is " + totalViolationHrsAfterChange);
        SimpleUtils.report("The total violation hours for the given location before change is " + dataFromComplianceTableBeforeChange.get(0));
        SimpleUtils.report("The total violation hours for the given location after change is " + dataFromComplianceTableAfterChange.get(0));

        if (Integer.valueOf(totalViolationHrsAfterChange.split(" ")[0]) != Integer.valueOf(totalViolationHrsBeforeChange.split(" ")[0])
                    && !dataFromComplianceTableBeforeChange.containsAll(dataFromComplianceTableAfterChange)
                    && !dataFromComplianceTableAfterChange.containsAll(dataFromComplianceTableBeforeChange))
            SimpleUtils.pass("Compliance Page: The violation number or hours on smart card and in analytics table should get updating according to the new change");
        else
            SimpleUtils.fail("Compliance Page: The timesheet approval rate on smart card and in analytics table doesn't get updating according to the new change",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify the availability of location list and sub location on Timesheet in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationListAndSubLocationOnTimesheetInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimeSheetPageLoaded(), false);

            // Validate the location list
            // todo: Blocked By https://legiontech.atlassian.net/browse/SCH-2522

            // Validate click one location
            timeSheetPage.clickOnGivenLocation(location);

            // Validate go back from selected location in current week
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            locationSelectorPage.reSelectDistrict(districtName);
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimesheetDMView(), false);

            // Validate the data for selected location
            List<String> dataFromTimesheetTable = timeSheetPage.getDataFromTimesheetTableForGivenLocationInDMView(location);

            timeSheetPage.clickOnGivenLocation(location);
            List<String> alertsDataFromSmartCard = timeSheetPage.getAlertsDataFromSmartCard();
            int totalAlert = 0;
            for (String alert: alertsDataFromSmartCard)
                totalAlert += Integer.valueOf(alert.replace("\n", " ").split(" ")[0]);
            String approvalRateFromTIMESHEETDUESmartCard = timeSheetPage.getApprovalRateFromTIMESHEETDUESmartCard();
            int totalTimesheetsInSMView = timeSheetPage.getTotalTimesheetInSMView();

            SimpleUtils.report("Unplanned Clocks for location \'" + location + "\' in DM View is " + dataFromTimesheetTable.get(0));
            SimpleUtils.report("Total Timesheets for location \'" + location + "\' in DM View is " + dataFromTimesheetTable.get(1));
            SimpleUtils.report("Timesheet Approval for location \'" + location + "\' in DM View is " + dataFromTimesheetTable.get(2));
            SimpleUtils.report("Unplanned Clocks for location \'" + location + "\' in SM View is " + totalAlert);
            SimpleUtils.report("Total Timesheets for location \'" + location + "\' in SM View is " + totalTimesheetsInSMView);
            SimpleUtils.report("Timesheet Approval for location \'" + location + "\' in SM View is " + approvalRateFromTIMESHEETDUESmartCard);

            if (dataFromTimesheetTable.get(0).equals(totalAlert) && dataFromTimesheetTable.get(1).equals(totalTimesheetsInSMView)
            && dataFromTimesheetTable.get(2).equals(approvalRateFromTIMESHEETDUESmartCard))
                SimpleUtils.pass("Timesheet Page: They are consistent between the rows in the table in DM view and in SM view");
            else
                //SimpleUtils.fail("Timesheet Page: They are inconsistent between the rows in the table in DM view and in SM view",false);
                SimpleUtils.warn("LEG-12321: [DM View] Timesheet is inconsistent");

            // Validate click given location and given week
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            locationSelectorPage.reSelectDistrict(districtName);
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            timeSheetPage.navigateToPreviousWeek();
            String weekInfo = timeSheetPage.getActiveDayWeekOrPayPeriod();
            timeSheetPage.clickOnGivenLocation(location);
            SimpleUtils.assertOnFail("It didn't navigate to the Timesheet page of the location in that week", weekInfo.equals(timeSheetPage.getActiveDayWeekOrPayPeriod()), false);

            // Validate click other district in past week
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            locationSelectorPage.changeAnotherDistrictInDMView();
            String anotherDistrictName = dashboardPage.getCurrentDistrict();
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(anotherDistrictName);
            locationSelectorPage.reSelectDistrict(districtName);
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            timeSheetPage.navigateToPreviousWeek();
            List<String> timesheetApprovalRateForOneDistrict = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard();
            locationSelectorPage.reSelectDistrict(anotherDistrictName);
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimesheetDMView(), false);
            List<String> timesheetApprovalRateForAnotherDistrict = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard();
            SimpleUtils.assertOnFail("Timesheet page: It didn't navigate to the Timesheet page of DM view with that district",!timesheetApprovalRateForOneDistrict.containsAll(timesheetApprovalRateForAnotherDistrict),false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify the availability of location list and sub location on Compliance in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationListAndSubLocationOnComplianceInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully", compliancePage.isCompliancePageLoaded(), false);

            // Validate the location list
            // todo: Blocked By https://legiontech.atlassian.net/browse/SCH-2522

            // Validate click one location
            SimpleUtils.assertOnFail("Compliance Page: The location is clickable unexpectedly", !compliancePage.isLocationInCompliancePageClickable(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify analytics table on Compliance in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnComplianceInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully", compliancePage.isCompliancePageLoaded(), false);

            // Validate the field names in analytics table
            compliancePage.verifyFieldNamesInAnalyticsTable("Location");

            // Validate the field columns can be ordered
            compliancePage.verifySortByColForLocationsInDMView(1);
            compliancePage.verifySortByColForLocationsInDMView(2);
            compliancePage.verifySortByColForLocationsInDMView(3);
            compliancePage.verifySortByColForLocationsInDMView(4);
            compliancePage.verifySortByColForLocationsInDMView(5);
            compliancePage.verifySortByColForLocationsInDMView(6);
            compliancePage.verifySortByColForLocationsInDMView(7);
            compliancePage.verifySortByColForLocationsInDMView(8);

            // Validate the data of analytics table for past week.
            compliancePage.navigateToPreviousWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for past week successfully",compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInDMForPast = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String totalExtraHoursInDMView = dataInDMForPast.get(0);

            dashboardPage.navigateToDashboard();
            locationSelectorPage.changeLocation(location);
            liquidDashboardPage.clickOnCarouselOnWidget("compliance violation","left");
            List<String> dataInSMForPast = liquidDashboardPage.getDataOnComplianceViolationWidget();
            String totalHrsInSMForPast = dataInSMForPast.get(3);
            SimpleUtils.report("Total Extra Hours In DM View for past week is "+totalExtraHoursInDMView);
            SimpleUtils.report("Total Extra Hours In SM View for past week is "+totalHrsInSMForPast);
            if(totalHrsInSMForPast.equals(String.valueOf(Math.round(Float.parseFloat(totalExtraHoursInDMView)))))
                SimpleUtils.pass("Compliance Page: Analytics table matches the past week's data");
            else
                SimpleUtils.fail("Compliance Page: Analytics table doesn't match the past week's data",false);

            // Validate the data of analytics table for current week.
            liquidDashboardPage.clickOnCarouselOnWidget("compliance violation","right");
            List<String> dataInSMForCurrent  = liquidDashboardPage.getDataOnComplianceViolationWidget();
            String totalHrsInSMForCurrent = dataInSMForCurrent.get(6);
            locationSelectorPage.reSelectDistrict(districtName);
            compliancePage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for current week successfully",compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInDMForCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String totalExtraHoursInDMViewForCurrent = dataInDMForCurrent.get(0);
            SimpleUtils.report("Total Extra Hours In DM View for current week is " + totalExtraHoursInDMViewForCurrent);
            SimpleUtils.report("Total Extra Hours In SM View for current week is " + totalHrsInSMForCurrent);
            if(totalHrsInSMForCurrent.equals(String.valueOf(Math.round(Float.parseFloat((totalExtraHoursInDMViewForCurrent))))))
                SimpleUtils.pass("Compliance Page: Analytics table matches the current week's data");
            else
                SimpleUtils.fail("Compliance Page: Analytics table doesn't match the current week's data",false);

            // Validate the data of analytics table for future week
            compliancePage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for future week successfully",compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInDMForFuture = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String totalExtraHoursInDMViewForFuture = dataInDMForFuture.get(0);
            SimpleUtils.report("Total Extra Hours In DM View for future week is " + totalExtraHoursInDMViewForFuture);
            if(totalExtraHoursInDMViewForFuture.equals("0.0"))
                SimpleUtils.pass("Compliance Page: Analytics table matches the future week's data");
            else
                SimpleUtils.fail("Compliance Page: Analytics table doesn't match the future week's data",false);
            compliancePage.navigateToPreviousWeek();

            // Validate Late Schedule is Yes
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            Thread.sleep(2000);
            controlsNewUIPage.updateDaysInAdvancePublishSchedulesInSchedulingPolicies("7");


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated)
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            createSchedulePage.publishActiveSchedule();
            locationSelectorPage.reSelectDistrict(districtName);

            compliancePage.clickOnComplianceConsoleMenu();
            List<String>  dataCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String lateScheduleYes = dataCurrent.get(dataCurrent.size()-1);
            if (lateScheduleYes.equals("Yes"))
                SimpleUtils.pass("Compliance Page: Late Schedule is Yes as expected");
            else
                SimpleUtils.fail("Compliance Page: Late Schedule is not Yes",false);

            // Validate Late Schedule is No
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated)
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            createSchedulePage.publishActiveSchedule();
            locationSelectorPage.reSelectDistrict(districtName);

            compliancePage.clickOnComplianceConsoleMenu();
            compliancePage.navigateToNextWeek();
            compliancePage.navigateToNextWeek();
            List<String>  dataNext = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String lateScheduleNo = dataNext.get(dataNext.size()-1);
            if (lateScheduleNo.equals("No"))
                SimpleUtils.pass("Compliance Page: Late Schedule is No as expected");
            else
                SimpleUtils.fail("Compliance Page: Late Schedule is not No",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Julie")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify TIMESHEET APPROVAL RATE on Timesheet in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTIMESHEETAPPROVALRATEOnTimesheetInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            String timesheetDueDate = timeSheetPage.verifyTimesheetSmartCard();
            dashboardPage.navigateToDashboard();
            locationSelectorPage.reSelectDistrict(districtName);
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            // Validate the content on TIMESHEET APPROVAL RATE smart card
            timeSheetPage.validateTheContentOnTIMESHEETAPPROVALRATESmartCard(timesheetDueDate);

            // Validate the data on TIMESHEET APPROVAL RATE smart card
            //todo due to https://legiontech.atlassian.net/browse/LEG-12321

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule Publish Status widget on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchedulePublishStatusWidgetOnDashboardInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Validate the Schedule Publish Status widget display on DM dashboard
            SimpleUtils.assertOnFail("Schedule Publish Status widget not loaded successfully",
                    dashboardPage.isSchedulePublishStatusWidgetDisplay(), false);
            //Validate the content of Schedule Publish Status widget
            dashboardPage.verifyTheContentInSchedulePublishStatusWidget();

            //Validate the data of Schedule Publish Status widget
            Map<String, Integer> scheduleStatusFromSchedulePublishStatusWidget = dashboardPage.getAllScheduleStatusFromSchedulePublishStatusWidget();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            Map<String, Integer> scheduleStatusFromScheduleDMViewPage = scheduleDMViewPage.getThreeWeeksScheduleStatusFromScheduleDMViewPage();
            SimpleUtils.assertOnFail("Schedule status on Schedule Publish Status widget and Schedule DM view page are different! ",
                    scheduleStatusFromSchedulePublishStatusWidget.equals(scheduleStatusFromScheduleDMViewPage), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Open Shifts widget on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentOfOpenShiftsForDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            // Create open shift in schedule so that we can verify the content on Open_Shifts Widget
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);

            if (createSchedulePage.isWeekGenerated() && !createSchedulePage.isWeekPublished()){
                createSchedulePage.publishActiveSchedule();
            }
            int openShiftsNumForLoc1 = scheduleShiftTablePage.getShiftsNumberByName("open");
            int shiftsNumForLoc1 = scheduleShiftTablePage.getShiftsNumberByName("");
            dashboardPage.navigateToDashboard();
            locationSelectorPage.changeLocation("NY CENTRAL");
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
            if (createSchedulePage.isWeekGenerated() && !createSchedulePage.isWeekPublished()){
                createSchedulePage.publishActiveSchedule();
            }
            int openShiftsNumForLoc2 = scheduleShiftTablePage.getShiftsNumberByName("open");
            int shiftsNumForLoc2 = scheduleShiftTablePage.getShiftsNumberByName("");
            int openRateExpected = 0;
            if ((shiftsNumForLoc1+shiftsNumForLoc2)!=0){
                openRateExpected = Math.round((float)(openShiftsNumForLoc1+openShiftsNumForLoc2)*100/(shiftsNumForLoc1+shiftsNumForLoc2));
            }
            //int assignedRateExpected = 100-openRateExpected;
            int assignedRateExpected = Math.round((float)(shiftsNumForLoc1+shiftsNumForLoc2-openShiftsNumForLoc1-openShiftsNumForLoc2)*100/(shiftsNumForLoc1+shiftsNumForLoc2));

            // refresh shifts offer KPI.
            dashboardPage.navigateToDashboard();
            locationSelectorPage.changeLocation(location);
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.gotoAnalyticsPage();
            analyticsPage.switchAllLocationsOrSingleLocation(false);
            analyticsPage.mouseHoverAndRefreshByName("Shift Offer KPI");

            dashboardPage.navigateToDashboard();
            locationSelectorPage.reSelectDistrict(districtName);

            SimpleUtils.assertOnFail("Open Shifts widget not loaded successfully", dashboardPage.isOpenShiftsWidgetDisplay(), false);
            String currentWeek = dashboardPage.getWeekInfoFromUpperfieldView();

            //Get values on open shifts widget and verify the info on Open_Shifts Widget
            HashMap<String, Integer> valuesOnOpenShiftsWidget = dashboardPage.verifyContentOfOpenShiftsWidgetForDMView();

            // Verify navigation to schedule page by "View Schedules" button on Open_Shifts Widget
            dashboardPage.clickViewSchedulesLinkOnOpenShiftsWidget();
            SimpleUtils.assertOnFail("Schedule page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);
            String[] weekInfoInDMView = MyThreadLocal.getDriver().findElement(By.cssSelector(".day-week-picker-period-active")).getText().toLowerCase().split("\n");
            String weekInfoExpected = scheduleCommonPage.convertDateStringFormat(weekInfoInDMView[weekInfoInDMView.length-1]);
            if (currentWeek.toLowerCase().contains(weekInfoExpected.toLowerCase())) {
                SimpleUtils.pass("Open Shifts: \"View Schedules\" button is to navigate to current week schedule page");
            } else {
                SimpleUtils.fail("Open Shifts: \"View Schedules\" button failed to navigate to current week schedule page", false);
            }

            // Verify the data on Open_Shifts Widget
            if (openRateExpected == valuesOnOpenShiftsWidget.get("open") && assignedRateExpected == valuesOnOpenShiftsWidget.get("assigned")){
                SimpleUtils.pass("Data is correct!");
            } else {
                SimpleUtils.fail("Data is incorrect!",false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule functionality in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleFunctionalityForDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Go to the Schedule page in DM view. And to verify the title.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule DM view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(districtName);
            locationSelectorPage.isLocationSelected("All Locations");
            List<String> locationInDistrict1 =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            System.out.println(scheduleCommonPage.getActiveWeekText());
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);
            scheduleDMViewPage.verifySearchLocationInScheduleDMView(location);
            //Change to another district.
            dashboardPage.navigateToDashboard();
            locationSelectorPage.changeAnotherDistrict();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            String anotherDistrictName = dashboardPage.getCurrentDistrict();
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(anotherDistrictName);
            List<String> locationInDistrict2 =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();
            SimpleUtils.assertOnFail("Schedule DM view page fail to update!", !locationInDistrict1.containsAll(locationInDistrict2), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the availablity of location list and sub location on Schedule in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationListAndSublocationInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Go to the Schedule page in DM view.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule DM view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            //Click on a location name and go to the schedule page.
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            //Validate go back from one location---function changed: cannot go back to DM View in schedule page
            //locationSelectorPage.reSelectDistrictInDMView(districtName);
            //SimpleUtils.assertOnFail("Schedule DM view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            //Validate click given location and given week
            dashboardPage.navigateToDashboard();
            locationSelectorPage.reSelectDistrict(districtName);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            SimpleUtils.assertOnFail("Didn't go to the right week!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Timesheet functionality on Timesheet in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimesheetFunctionalityInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Go to the Timesheet page in DM view and verify the title of the page.
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);

            //Verify district selected and displayed with "All locations".
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(districtName);
            locationSelectorPage.isLocationSelected("All Locations");
            List<String> locationInDistrict1 =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();

            //Validate changing district.
            locationSelectorPage.changeAnotherDistrictInDMView();
            String anotherDistrictName = dashboardPage.getCurrentDistrict();
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(anotherDistrictName);
            List<String> locationInDistrict2 =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();
            SimpleUtils.assertOnFail("TimeSheet DM view page fail to update!", !locationInDistrict1.containsAll(locationInDistrict2), false);

            //Validate the clickability of backward button.
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToPreviousWeek();

            //Validate the clickability of forward button.
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

            //Validate search function.
            locationSelectorPage.reSelectDistrict(districtName);
            scheduleDMViewPage.verifySearchLocationInScheduleDMView(location);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify LOCATION SUMMARY on Schedule in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationSummaryInScheduleDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Go to the Schedule page in DM view.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule DM view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            //Validate the content of LOCATION SUMMARY smart card for current/future weeks.
            HashMap<String, Float> valuesFromLocationSummaryCard =  scheduleDMViewPage.getValuesAndVerifyInfoForLocationSummaryInDMView("location","current");

            //Validate the data LOCATION SUMMARY smart card for current/future weeks.
            SimpleUtils.assertOnFail("Location counts in title are inconsistent!", Math.round(valuesFromLocationSummaryCard.get("NumOfLocations")) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            SimpleUtils.assertOnFail("Location counts from projected info are inconsistent!", (Math.round(valuesFromLocationSummaryCard.get("NumOfProjectedWithin")) + Math.round(valuesFromLocationSummaryCard.get("NumOfProjectedOver"))) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            //verify budgeted hours.
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Budgeted Hours")));
            float budgetedHrsFromTable = 0;
            for (Float f: data){
                budgetedHrsFromTable = budgetedHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Budgeted hours are inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("Budgeted Hrs")) - budgetedHrsFromTable) == 0, false);
            //verify scheduled hours
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Scheduled Hours")));
            float scheduledHrsFromTable = 0;
            for (Float f: data){
                scheduledHrsFromTable = scheduledHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Published hours are inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("Published Hrs")) - scheduledHrsFromTable) == 0, false);

            //Verify difference value between budgeted and projected.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Projected Hours")));
            float projectedHours = 0;
            for (Float f: data){
                projectedHours = projectedHours + f;
            }
            if ((valuesFromLocationSummaryCard.get("Budgeted Hrs") - projectedHours)>0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("▼")) - (valuesFromLocationSummaryCard.get("Budgeted Hrs") - projectedHours)) == 0, false);
            }
            if ((valuesFromLocationSummaryCard.get("Budgeted Hrs") - projectedHours)<0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("▲")) - (valuesFromLocationSummaryCard.get("Budgeted Hrs") - projectedHours)) == 0, false);
            }

            //Verify currect week Projected Hours displays.
            scheduleDMViewPage.verifyClockedOrProjectedInDMViewTable("Projected Hours");

            //Navigate to the past week to verify the info and data.
            scheduleCommonPage.navigateToPreviousWeek();
            valuesFromLocationSummaryCard =  scheduleDMViewPage.getValuesAndVerifyInfoForLocationSummaryInDMView("location", "past");

            //Validate the data LOCATION SUMMARY smart card for the past weeks.
            SimpleUtils.assertOnFail("Location counts in title are inconsistent!", Math.round(valuesFromLocationSummaryCard.get("NumOfLocations")) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            SimpleUtils.assertOnFail("Location counts from projected info are inconsistent!", (Math.round(valuesFromLocationSummaryCard.get("NumOfProjectedWithin")) + Math.round(valuesFromLocationSummaryCard.get("NumOfProjectedOver"))) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            //verify budgeted hours.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Budgeted Hours")));
            budgetedHrsFromTable = 0;
            for (Float f: data){
                budgetedHrsFromTable = budgetedHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Budgeted hours are inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("Budgeted Hrs")) - budgetedHrsFromTable) == 0, false);
            //verify scheduled hours.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Scheduled Hours")));
            scheduledHrsFromTable = 0;
            for (Float f: data){
                scheduledHrsFromTable = scheduledHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Published hours are inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("Published Hrs")) - scheduledHrsFromTable) == 0, false);
            //Verify difference value between budgeted and projected.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clocked Hours")));
            projectedHours = 0;
            for (Float f: data){
                projectedHours = projectedHours + f;
            }
            if ((valuesFromLocationSummaryCard.get("Budgeted Hrs") - projectedHours)>=0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("▼")) - (valuesFromLocationSummaryCard.get("Budgeted Hrs") - projectedHours)) == 0, false);
            } else {
                SimpleUtils.assertOnFail("Difference hours is inconsistent!", (Math.abs(valuesFromLocationSummaryCard.get("▲")) - (valuesFromLocationSummaryCard.get("Budgeted Hrs") - projectedHours)) == 0, false);

            }
            //Verify past week Clocked Hours displays.
            scheduleDMViewPage.verifyClockedOrProjectedInDMViewTable("Clocked Hours");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Unplanned Clocks on Timesheet in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUnplannedClocksForTimesheetInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Validate the content on Unplanned Clocks summary card.
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            HashMap<String, Integer> valuesFromUnplannedClocksSummaryCard = scheduleDMViewPage.getValueOnUnplannedClocksSummaryCardAndVerifyInfo();
            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Unplanned Clocks");
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            int unplannedClocks = 0;
            for (Float f: data){
                unplannedClocks = unplannedClocks + Math.round(f);
            }
            SimpleUtils.assertOnFail("Unplanned clocks from summary card and analytic table are inconsistent!", valuesFromUnplannedClocksSummaryCard.get("unplanned clocks")==unplannedClocks, false);

            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Total Timesheets");
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            int totalTimesheets = 0;
            for (Float f: data){
                totalTimesheets = totalTimesheets + Math.round(f);
            }
            SimpleUtils.assertOnFail("Total Timesheets from summary card and analytic table are inconsistent!", valuesFromUnplannedClocksSummaryCard.get("total timesheets")==totalTimesheets, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify UNPLANNED CLOCKS smart card on Timesheet in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUnplannedClocksSmartCardForTimesheetInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Validate the content on Unplanned Clocks summary smart card.
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            HashMap<String, Integer> valuesFromUnplannedClocksSummaryCard = scheduleDMViewPage.getValueOnUnplannedClocksSmartCardAndVerifyInfo();
            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Unplanned Clocks");
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            int unplannedClocks = 0;
            for (Float f: data){
                unplannedClocks = unplannedClocks + Math.round(f);
            }
            SimpleUtils.assertOnFail("Unplanned clocks from summary card and analytic table are inconsistent!", valuesFromUnplannedClocksSummaryCard.get("No Show")==unplannedClocks, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Compliance functionality on Compliance in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComplianceFunctionalityForComplianceInDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Validate the title and info of Compliance page.
            timeSheetPage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance Page not loaded Successfully!",compliancePage.isCompliancePageLoaded() , false);

            //Verify district selected and displayed with "All locations".
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(districtName);
            locationSelectorPage.isLocationSelected("All Locations");
            List<String> locationInDistrict1 =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();

            //Validate changing district.
            locationSelectorPage.changeAnotherDistrictInDMView();
            String anotherDistrictName = dashboardPage.getCurrentDistrict();
            locationSelectorPage.verifyTheDisplayDistrictWithSelectedDistrictConsistent(anotherDistrictName);
            List<String> locationInDistrict2 =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();
            SimpleUtils.assertOnFail("Compliance DM view page fail to update!", !locationInDistrict1.containsAll(locationInDistrict2), false);

            //Validate search function.
            locationSelectorPage.reSelectDistrictInDMView(districtName);
            scheduleDMViewPage.verifySearchLocationInScheduleDMView(location);

            //Validate the clickability of backward button.
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToPreviousWeek();

            //Validate the clickability of forward button.
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify TOTAL VIOLATION HRS on Compliance in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTotalViolationHrsInComplianceDMViewAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Navigate to Compliance page.
            timeSheetPage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance Page not loaded Successfully!",compliancePage.isCompliancePageLoaded() , false);

            //Validate the content of Toatal Violation smart card for current week.
            HashMap<String, Float> valuesFromToatalViolationCard =  compliancePage.getValuesAndVerifyInfoForTotalViolationSmartCardInDMView();

            //Validate the data Toatal Violation smart card for current week.
            //Verify total violation hours for current week.
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Total Extra Hours")));
            float totalViolationHrsFromTable = 0;
            for (Float f: data){
                totalViolationHrsFromTable = totalViolationHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Total violation hours are inconsistent with analytic table!", (Math.abs(valuesFromToatalViolationCard.get("vioHrsCurrentWeek")) - totalViolationHrsFromTable) == 0, false);

            //Verify diff hours flag.
            if ((valuesFromToatalViolationCard.get("vioHrsCurrentWeek") - valuesFromToatalViolationCard.get("vioHrsPastWeek"))>0){
                compliancePage.verifyDiffFlag("down");
            } else {
                compliancePage.verifyDiffFlag("up");
            }

            //Verify total violation hours for last week.
            scheduleCommonPage.navigateToPreviousWeek();
            HashMap<String, Float> valuesFromToatalViolationCardForLastWeek =  compliancePage.getValuesAndVerifyInfoForTotalViolationSmartCardInDMView();
            SimpleUtils.assertOnFail("Violation hours for last week are inconsistent!", (Math.abs(valuesFromToatalViolationCard.get("vioHrsPastWeek")) - valuesFromToatalViolationCardForLastWeek.get("vioHrsCurrentWeek")) == 0, false);

            //Verify diff hours.
            SimpleUtils.assertOnFail("Diff hours with last week is incorrect!", (Math.abs(Math.abs(valuesFromToatalViolationCard.get("vioHrsCurrentWeek")) - valuesFromToatalViolationCard.get("vioHrsPastWeek"))-valuesFromToatalViolationCard.get("diffHrs")) == 0, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Locations with Violations on Compliance in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTotalLocationsWithViolationCardInComplianceDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Navigate to Compliance page.
            timeSheetPage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance Page not loaded Successfully!",compliancePage.isCompliancePageLoaded() , false);

            //Validate the content on Locations with violation card.
            HashMap<String, Integer> valuesFromLocationsWithViolationCard = compliancePage.getValueOnLocationsWithViolationCardAndVerifyInfo("Location");
            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Extra Hours");
            List<Float> extraHours = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Late Schedule");
            List<String> publishStatus = scheduleDMViewPage.getListByColInTimesheetDMView(index);
            SimpleUtils.assertOnFail("The extra hour count should consistent with publish status count! ", extraHours.size() == publishStatus.size(), false);
            int totalLocationWithViolation = 0;

            for (int i = 0; i < extraHours.size(); i++){
                if (extraHours.get(i) >0 || publishStatus.get(i).equals("Yes")){
                    totalLocationWithViolation ++;
                }
            }

            SimpleUtils.assertOnFail("Locations With Violation Card and analytic table are inconsistent!", valuesFromLocationsWithViolationCard.get("UpperFieldsWithViolations") == totalLocationWithViolation, false);
            SimpleUtils.assertOnFail("Locations With Violation Card and analytic table are inconsistent!", valuesFromLocationsWithViolationCard.get("TotalUpperFields") == extraHours.size(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify TOP x VIOLATIONS (HRS) on Compliance in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTopViolationsCardInComplianceDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        String districtName = dashboardPage.getCurrentDistrict();
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
        ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
        locationSelectorPage.reSelectDistrict(districtName);

        //Navigate to Compliance page.
        timeSheetPage.clickOnComplianceConsoleMenu();
        SimpleUtils.assertOnFail("Compliance Page not loaded Successfully!",compliancePage.isCompliancePageLoaded() , false);

        float topViolationInOvertimeCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Overtime"))));
        float topViolationInClopeningCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clopening"))));
        float topViolationInMissedMealCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Missed Meal"))));
        float topViolationInScheduleChangedCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Schedule Changed"))));
        float topViolationInDoubletimeCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Double time"))));


        if ((topViolationInOvertimeCol+topViolationInClopeningCol+topViolationInMissedMealCol+topViolationInScheduleChangedCol+topViolationInDoubletimeCol) != 0.0){
            HashMap<String, Float> valuesFromLocationsWithViolationCard = compliancePage.getViolationHrsFromTop1ViolationCardAndVerifyInfo();

            if (valuesFromLocationsWithViolationCard.containsKey("Overtime (Hrs)")){
                SimpleUtils.assertOnFail("Overtime (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Overtime (Hrs)")-topViolationInOvertimeCol)==0, false);
            }
            if (valuesFromLocationsWithViolationCard.containsKey("Clopening (Hrs)")){
                SimpleUtils.assertOnFail("Clopening (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Clopening (Hrs)")-topViolationInClopeningCol)==0, false);
            }
            if (valuesFromLocationsWithViolationCard.containsKey("Overtime (Hrs)")){
                SimpleUtils.assertOnFail("Missed Meal on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Missed Meal")-topViolationInMissedMealCol)==0, false);
            }
            if (valuesFromLocationsWithViolationCard.containsKey("Schedule Changed")){
                SimpleUtils.assertOnFail("Schedule Changed on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Schedule Changed")-topViolationInScheduleChangedCol)==0, false);
            }
            if (valuesFromLocationsWithViolationCard.containsKey("Doubletime (Hrs)")){
                SimpleUtils.assertOnFail("Doubletime (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Doubletime (Hrs)")-topViolationInDoubletimeCol)==0, false);
            }
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "DM View Navigation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDMViewNavigationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            //Validate user has access to 1 district.
            locationSelectorPage.reSelectDistrict(districtName);
            locationSelectorPage.isDMView();
            String currentWeek = dashboardPage.getWeekInfoFromUpperfieldView();

            //Validate drilling into a store location.
            locationSelectorPage.changeLocation(location);

            //Validate navigating back to district view.
            //Validate default date.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            locationSelectorPage.reSelectDistrict(districtName);
            SimpleUtils.assertOnFail("Schedule DM view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);
            if (currentWeek.toLowerCase().contains(MyThreadLocal.getDriver().findElement(By.cssSelector(".day-week-picker-period-active")).getText().toLowerCase().split("\n")[MyThreadLocal.getDriver().findElement(By.cssSelector(".day-week-picker-period-active")).getText().toLowerCase().split("\n").length-1])) {
                SimpleUtils.pass("Open Shifts: \"View Schedules\" button is to navigate to current week schedule page");
            } else {
                SimpleUtils.fail("Open Shifts: \"View Schedules\" button failed to navigate to current week schedule page", false);
            }

            //Validate changing date and location.
            dashboardPage.navigateToDashboard();
            locationSelectorPage.changeLocation(location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab("Schedule");
            scheduleCommonPage.navigateToNextWeek();
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            locationSelectorPage.reSelectDistrict(districtName);
            SimpleUtils.assertOnFail("Dates are inconsistent after changing date and location!", scheduleCommonPage.getActiveWeekText().equalsIgnoreCase(weekInfo), false);

            //Changing district and navigate to Timesheet page(Checked in other script).
            //Verify no timesheet tab in non-TA env.
            SimpleUtils.assertOnFail("Timesheet tab should not be loaded!", !timeSheetPage.isTimeSheetConsoleMenuTabLoaded(), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify analytics table on Timesheet in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticTableInTimesheetDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Validate fields name in analytic table.
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            String field1 = "Location";
            String field2 = "Unplanned Clocks";
            String field3 = "Total Timesheets";
            String field4 = "Timesheet Approval";
            String field5 = "Within 24 Hrs";
            String field6 = "Within 48 Hrs";
            String field7 = "Beyond 48 Hrs";
            String field8 = "Avg. Approval Time";
            SimpleUtils.assertOnFail(field1 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field1) > 0, false);
            SimpleUtils.assertOnFail(field2 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field2) > 0, false);
            SimpleUtils.assertOnFail(field3 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field3) > 0, false);
            SimpleUtils.assertOnFail(field4 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field4) > 0, false);
            SimpleUtils.assertOnFail(field5 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field5) > 0, false);
            SimpleUtils.assertOnFail(field6 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field6) > 0, false);
            SimpleUtils.assertOnFail(field7 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field7) > 0, false);
            SimpleUtils.assertOnFail(field8 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field8) > 0, false);

            //Validate the field columns can be ordered.
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(2);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(4);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(5);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(6);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(7);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(8);

            //Validate the data of analytics table for past week.
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(location);
            SimpleUtils.assertOnFail("This is not the Timesheet SM view page for past week!",timeSheetPage.isTimeSheetPageLoaded(), false);
            locationSelectorPage.reSelectDistrict(districtName);

            //Validate the data of analytics table for current week.
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(location);
            SimpleUtils.assertOnFail("This is not the Timesheet SM view page for current!",timeSheetPage.isTimeSheetPageLoaded(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Controls in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyControlsInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Validate Controls existing from DM view.
            //Validate navigate to Controls from DM view.
            timeSheetPage.clickOnComplianceConsoleMenu();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page is not loaded!", controlsNewUIPage.isControlsPageLoaded(), false);
            locationSelectorPage.isLocationSelected("All Locations");

            //Validate week navigation in DM view getting updated based on schedule planning window settings.
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            String s = controlsNewUIPage.getAdvanceScheduleWeekCountToCreate().replace(" weeks","").replace(" week","");
            int weekCount = 0;
            if (SimpleUtils.isNumeric(s)){
                weekCount = Integer.parseInt(s);
            } else {
                SimpleUtils.fail("Advance schedule week count isn't expected!", false);
            }
            timeSheetPage.clickOnComplianceConsoleMenu();
            for (int i = 0; i < weekCount; i++ ){//There should be weekCount next weeks we can navigate to.
                scheduleCommonPage.navigateToNextWeek();
            }
            SimpleUtils.assertOnFail("There shouldn't be another next week can access to!", !scheduleDMViewPage.hasNextWeek(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule vs. Guidance by Day widget on Dashboard in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchedulesGuidanceByDayWidgetOnDashboardInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Set 'Apply labor budget to schedules?' to No
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            dashboardPage.clickOnDashboardConsoleMenu();

            //Try to refresh the Guidance message
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            dashboardPage.clickOnDashboardConsoleMenu();
            //Validate the Schedule Vs Guidance By Day widget is loaded on dashboard‘
            SimpleUtils.assertOnFail("Schedule Vs Guidance By Day widget loaded fail! ",
                    dashboardPage.isScheduleVsGuidanceByDayWidgetDisplay(), false);

            //Validate the content on Schedule Vs Guidance By Day widget display correctly
            dashboardPage.verifyTheContentOnScheduleVsGuidanceByDayWidget();

            //Validate the hours Under or Cover budget is consistent with the hours on schedule page
            dashboardPage.verifyTheHrsUnderOrCoverBudgetOnScheduleVsGuidanceByDayWidget();

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Location Summary widget on Dashboard in DM View on non TA env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationSummaryWidgetOnDashboardInDMViewOnNonTAEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            verifyLocationSummaryWidgetOnDashboardInDMView(false);
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Location Summary widget on Dashboard in DM View on TA env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationSummaryWidgetOnDashboardInDMViewOnTAEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            verifyLocationSummaryWidgetOnDashboardInDMView(true);
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    private void verifyLocationSummaryWidgetOnDashboardInDMView(boolean isTAEnv) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        controlsNewUIPage.clickOnGlobalLocationButton();
        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
        dashboardPage.clickOnDashboardConsoleMenu();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        dashboardPage.clickOnDashboardConsoleMenu();
        String districtName = dashboardPage.getCurrentDistrict();
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.reSelectDistrict(districtName);

        //Validate the Location Summary widget is loaded on dashboard
        SimpleUtils.assertOnFail("Location Summary widget loaded fail! ",
                dashboardPage.isLocationSummaryWidgetDisplay(), false);

        //Validate the content on Location Summary widget display correctly
        dashboardPage.verifyTheContentOnOrgSummaryWidget(false, true);

        //Validate the Hrs Over Or Under Budget On Location Summary Widget
        dashboardPage.verifyTheHrsOverOrUnderBudgetOnLocationSummaryWidget();

        //Validate the hours on Location Summary widget is consistent with the hours on schedule page
        List<String> dataFromLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
        List<Float> totalBudgetedScheduledProjectedHour= scheduleDMViewPage.getTheTotalBudgetedScheduledProjectedHourOfScheduleInDMView();
        List<String> locationNumbersFromLocationSummarySmartCard= scheduleDMViewPage.getLocationNumbersFromLocationSummarySmartCard();
        List<String> textOnTheChartInLocationSummarySmartCard= scheduleDMViewPage.getTextFromTheChartInLocationSummarySmartCard();

        BigDecimal b1 = new BigDecimal(totalBudgetedScheduledProjectedHour.get(0));
        BigDecimal b2 = new BigDecimal(totalBudgetedScheduledProjectedHour.get(1));
        boolean isBudgetedHrsCorrect = Float.parseFloat(dataFromLocationSummaryWidget.get(0))
                - b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() == 0;
        boolean isScheduledHrsCorrect = Float.parseFloat(dataFromLocationSummaryWidget.get(1)) -
                b2.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() == 0;
        boolean isProjectedWithinBudgetLocationsCorrect = dataFromLocationSummaryWidget.get(3).equals(locationNumbersFromLocationSummarySmartCard.get(0));
        boolean isProjectedOverBudgetLocationsCorrect = dataFromLocationSummaryWidget.get(4).equals(locationNumbersFromLocationSummarySmartCard.get(1));
        boolean isHrsOfUnderOrCoverBudgetCorrect = false;
        if(isTAEnv){
            isHrsOfUnderOrCoverBudgetCorrect = dataFromLocationSummaryWidget.get(5).split(" ")[0].
                    equals(textOnTheChartInLocationSummarySmartCard.get(6).split(" ")[0]);
        } else
            isHrsOfUnderOrCoverBudgetCorrect = dataFromLocationSummaryWidget.get(5).split(" ")[0].
                    equals(textOnTheChartInLocationSummarySmartCard.get(4).split(" ")[0]);

        SimpleUtils.assertOnFail("The hours on Location Summary widget is inconsistent with the hours on schedule page! ",
                isBudgetedHrsCorrect && isScheduledHrsCorrect && isProjectedWithinBudgetLocationsCorrect
                && isProjectedOverBudgetLocationsCorrect&&isHrsOfUnderOrCoverBudgetCorrect, false);

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the content between weeks on Schedule in DM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentBetweenWeeksOnScheduleInDMViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);

            //Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            dashboardPage.clickOnDashboardConsoleMenu();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Validate the smart card and schedule table header for previous week
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, false);
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,false);
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, true);
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,true);

            //Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            dashboardPage.clickOnDashboardConsoleMenu();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Validate the smart card and schedule table header for current week
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, false);
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false, false);
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, true);
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false, true);

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }


//    @Automated(automated = "Automated")
//    @Owner(owner = "Mary")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate analytics table on Schedule in DM View")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyScheduleStatusAndHoursOnScheduleDMViewOnNonTAEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        try{
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//            String districtName = dashboardPage.getCurrentDistrict();
//            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//            locationSelectorPage.reSelectDistrict(districtName);
//            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
//            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//
//            //Validate the schedule status and hours on schedule list
//            if (createSchedulePage.isWeekGenerated()){
//                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//            }
//            createSchedulePage.createScheduleForNonDGFlowNewUI();
////            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Not Started", "Current Week");       //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "In Progress", "Current Week");
////            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Published", "Current Week");   //comment it because bug: https://legiontech.atlassian.net/browse/SCH-2654
//
//            //Validate the schedule status and hours on schedule list
//            scheduleCommonPage.navigateToNextWeek();
//            if (createSchedulePage.isWeekGenerated()){
//                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//            }
//            createSchedulePage.createScheduleForNonDGFlowNewUI();
////            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Not Started", "Next Week");      //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "In Progress", "Next Week");
////            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Published", "Next Week");        //comment it because bug: https://legiontech.atlassian.net/browse/SCH-2654
//
//            //Validate the schedule status and hours on schedule list
//            scheduleCommonPage.navigateToPreviousWeek();
//            scheduleCommonPage.navigateToPreviousWeek();
//            if (createSchedulePage.isWeekGenerated()){
//                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//            }
//            createSchedulePage.createScheduleForNonDGFlowNewUI();
////            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Not Started", "Previous Week");      //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "In Progress", "Previous Week");      //comment it because bug: https://legiontech.atlassian.net/browse/SCH-2654
////            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Published", "Previous Week");        //comment it because bug: https://legiontech.atlassian.net/browse/SCH-2654
//
//            //Validate the numbers on Schedule Status Cards
//            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();
//        } catch (Exception e) {
//            SimpleUtils.fail(e.toString(),false);
//        }
//    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule Status on Schedule in DM View for Not Started schedule on Non TA Env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleStatusAndHoursOnScheduleDMViewForNotStartedScheduleOnNonTAEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Validate the schedule status and hours on schedule list
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToNextWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            locationSelectorPage.reSelectDistrict(districtName);
            dashboardPage.clickOnDashboardConsoleMenu();
            dashboardPage.clickOnRefreshButton();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Not Started", "Current Week");

            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Not Started", "Next Week");

            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Not Started", "Previous Week");      //comment it because bug: https://legiontech.atlassian.net/browse/SCH-2654

            //Validate the numbers on Schedule Status Cards
            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule Status on Schedule in DM View for In progress schedule on Non TA Env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleStatusAndHoursOnScheduleDMViewForInProgressScheduleOnNonTAEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Validate the schedule status and hours on schedule list
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToNextWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            locationSelectorPage.reSelectDistrict(districtName);
            dashboardPage.clickOnDashboardConsoleMenu();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "In Progress", "Current Week");

            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "In Progress", "Next Week");

            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "In Progress", "Previous Week");      //comment it because bug: https://legiontech.atlassian.net/browse/SCH-2654

            //Validate the numbers on Schedule Status Cards
            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule Status on Schedule in DM View for Published schedule on Non TA Env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleStatusAndHoursOnScheduleDMViewForPublishedScheduleOnNonTAEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
            controlsNewUIPage.clickOnScheduleCollaborationOpenShiftAdvanceBtn();

            //Set 'Automatically convert unassigned shifts to open shifts when generating the schedule?' set as Yes, all unassigned shifts
            controlsNewUIPage.updateConvertUnassignedShiftsToOpenSettingOption("Yes, all unassigned shifts");

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Validate the schedule status and hours on schedule list
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            }
            createSchedulePage.publishActiveSchedule();

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToNextWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            }
            createSchedulePage.publishActiveSchedule();

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            }
            createSchedulePage.publishActiveSchedule();

            locationSelectorPage.reSelectDistrict(districtName);
            dashboardPage.clickOnDashboardConsoleMenu();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Published", "Current Week");

            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Published", "Next Week");

            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,false, false, "Published", "Previous Week");      //comment it because bug: https://legiontech.atlassian.net/browse/SCH-2654

            //Validate the numbers on Schedule Status Cards
            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Schedule Status on Schedule in DM View on TA non DG env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleStatusAndHoursOnScheduleDMViewOnTANonDGEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();

            //Validate the schedule status and hours on schedule list
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "Published", "Current Week");
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "Not Started", "Current Week");            //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "In Progress", "Current Week");

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "Published", "Next Week");
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "Not Started", "Next Week");//comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "In Progress", "Next Week");

            //Validate the schedule status and hours on schedule list
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "Published", "Previous Week");   //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1482
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "Not Started", "Previous Week");//comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, false, "In Progress", "Previous Week"); //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1482

            //Validate the numbers on Schedule Status Cards
            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "DGStage_Enterprise")
    @TestName(description = "Verify Schedule Status on Schedule in DM View on TA DG env")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleStatusAndHoursOnScheduleDMViewOnTADGEnvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String districtName = dashboardPage.getCurrentDistrict();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.reSelectDistrict(districtName);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();

            //Validate the schedule status and hours on schedule list
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "Published", "Current Week");
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "Not Started", "Current Week");            //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "In Progress", "Current Week");       // comment it because bug: https://legiontech.atlassian.net/browse/SCH-1653

            //Validate the schedule status and hours on schedule list
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "Published", "Next Week");
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "Not Started", "Next Week");//comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "In Progress", "Next Week");      //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1653

            //Validate the schedule status and hours on schedule list
//            scheduleCommonPage.navigateToPreviousWeek();
//            scheduleCommonPage.navigateToPreviousWeek();
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "Published", "Previous Week");   //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1482
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "Not Started", "Previous Week");//comment it because bug: https://legiontech.atlassian.net/browse/SCH-1874
//            scheduleDMViewPage.verifyScheduleStatusAndHoursInScheduleList(location,true, true, "In Progress", "Previous Week"); //comment it because bug: https://legiontech.atlassian.net/browse/SCH-1482

            //Validate the numbers on Schedule Status Cards
            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }
}
