package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.core.ConsoleLocationSelectorPage;
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

import javax.swing.plaf.basic.BasicButtonUI;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.legion.utils.MyThreadLocal.getDriver;

public class UpperfieldTest extends TestBase {

    private static String Location = "Location";
    private static String District = "District";
    private static String Region = "Region";
    private static String BusinessUnit = "Business Unit";

    String[] controlUpperFields2 = districtsMap.get("Vailqacn_Enterprise2").split(">");
    String[] controlUpperFields3 = districtsMap.get("Vailqacn_Enterprise3").split(">");
    String[] opUpperFields2 = districtsMap.get("CinemarkWkdy_Enterprise2").split(">");
    String[] opUpperFields3 = districtsMap.get("CinemarkWkdy_Enterprise3").split(">");
    private static String controlEnterprice = "Vailqacn_Enterprise";
    private static String opEnterprice = "CinemarkWkdy_Enterprise";
    private static HashMap<String, String> parameterMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");

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

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Dashboard functionality in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDashboardFunctionalityInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            // Validate the title
            dashboardPage.verifyHeaderOnDashboard();
            locationSelectorPage.verifyTheDisplayBUWithSelectedBUConsistent(selectedUpperFields.get(BusinessUnit));
            locationSelectorPage.isBUView();

            // Validate the presence of BU
            dashboardPage.validateThePresenceOfUpperfield();

            // Validate the visibility of week
            dashboardPage.validateTheVisibilityOfWeek();

            // Validate welcome content
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String nickName = profileNewUIPage.getNickNameFromProfile();
            dashboardPage.verifyTheWelcomeMessageForUpperfield(nickName);

            // Validate changing BUs on Dashboard
            locationSelectorPage.changeAnotherBUInBUView();
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            String buOnDashboard = dashboardPage.getUpperfieldNameOnDashboard();
            if (buName.equals(buOnDashboard))
                SimpleUtils.pass("Dashboard Page: When the user selects a different BU from the BU view, the data updates to reflect the selected BU");
            else
                SimpleUtils.fail("When the user selects a different BU from the BU view, the data doesn't update to reflect the selected BU",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Dashboard functionality in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDashboardFunctionalityInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            // Validate the title
            dashboardPage.verifyHeaderOnDashboard();
            locationSelectorPage.verifyTheDisplayRegionWithSelectedRegionConsistent(selectedUpperFields.get(Region));
            locationSelectorPage.isRegionView();

            // Validate the presence of region
            dashboardPage.validateThePresenceOfUpperfield();

            // Validate the visibility of week
            dashboardPage.validateTheVisibilityOfWeek();

            // Validate welcome content
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String nickName = profileNewUIPage.getNickNameFromProfile();
            dashboardPage.verifyTheWelcomeMessageForUpperfield(nickName);

            // Validate changing regions on Dashboard
            locationSelectorPage.changeAnotherRegionInRegionView();
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            String regionOnDashboard = dashboardPage.getUpperfieldNameOnDashboard();
            if (regionName.equals(regionOnDashboard))
                SimpleUtils.pass("Dashboard Page: When the user selects a different region from the region view, the data updates to reflect the selected region");
            else
                SimpleUtils.fail("When the user selects a different region from the region view, the data doesn't update to reflect the selected region",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Refresh feature on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            // Validate the presence of Refresh button
            dashboardPage.validateThePresenceOfRefreshButtonUpperfield();

            // Validate Refresh timestamp
            dashboardPage.validateRefreshTimestampUpperfield();

            // Validate Refresh when navigation back
            dashboardPage.validateRefreshWhenNavigationBackUpperfied();

            // Validate Refresh function
            dashboardPage.validateRefreshFunctionUpperfield();

            // Validate Refresh performance
            dashboardPage.validateRefreshPerformanceUpperfield();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Refresh feature on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            // Validate the presence of Refresh button
            dashboardPage.validateThePresenceOfRefreshButtonUpperfield();

            // Validate Refresh timestamp
            dashboardPage.validateRefreshTimestampUpperfield();

            // Validate Refresh when navigation back
            dashboardPage.validateRefreshWhenNavigationBackUpperfied();

            // Validate Refresh function
            dashboardPage.validateRefreshFunctionUpperfield();

            // Validate Refresh performance
            dashboardPage.validateRefreshPerformanceUpperfield();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Projected Compliance widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyProjectedComplianceWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            SimpleUtils.assertOnFail("Project Compliance widget not loaded successfully", dashboardPage.isProjectedComplianceWidgetDisplay(), false);

            // Validate the content in Projected Compliance widget without TA
            dashboardPage.verifyTheContentInProjectedComplianceWidget();

            // Validate the data in Projected Compliance widget without TA
            String totalViolationHrsFromProjectedComplianceWidget =
                    dashboardPage.getTheTotalViolationHrsFromProjectedComplianceWidget();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            dashboardPage.clickOnViewViolationsLink();
            String totalViolationHrsFromCompliancePage =
                    compliancePage.getTheTotalViolationHrsFromSmartCard().split(" ")[0];
            SimpleUtils.assertOnFail("Project Compliance widget not loaded successfully",
                    totalViolationHrsFromProjectedComplianceWidget.equals(totalViolationHrsFromCompliancePage), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Projected Compliance widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyProjectedComplianceWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            SimpleUtils.assertOnFail("Project Compliance widget not loaded successfully", dashboardPage.isProjectedComplianceWidgetDisplay(), false);

            // Validate the content in Projected Compliance widget without TA
            dashboardPage.verifyTheContentInProjectedComplianceWidget();

            // Validate the data in Projected Compliance widget without TA
            String totalViolationHrsFromProjectedComplianceWidget =
                    dashboardPage.getTheTotalViolationHrsFromProjectedComplianceWidget();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            dashboardPage.clickOnViewViolationsLink();
            String totalViolationHrsFromCompliancePage =
                    compliancePage.getTheTotalViolationHrsFromSmartCard().split(" ")[0];
            SimpleUtils.assertOnFail("Project Compliance widget not loaded successfully",
                    totalViolationHrsFromProjectedComplianceWidget.equals(totalViolationHrsFromCompliancePage), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Timesheet Approval Rate widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimesheetApprovalRateWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            SimpleUtils.assertOnFail("Timesheet Approval Rate widget not loaded successfully", dashboardPage.isTimesheetApprovalRateWidgetDisplay(), false);

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
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Timesheet Approval Rate widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimesheetApprovalRateWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            SimpleUtils.assertOnFail("Timesheet Approval Rate widget not loaded successfully", dashboardPage.isTimesheetApprovalRateWidgetDisplay(), false);

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
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule Publish Status widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchedulePublishStatusWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            SimpleUtils.assertOnFail("Schedule Publish Status widget not loaded successfully", dashboardPage.isSchedulePublishStatusWidgetDisplay(), false);

            // Validate the content in Schedule Publish Status widget without TA
            dashboardPage.verifyTheContentInSchedulePublishStatusWidget();

            // Validate data in Schedule Publish Status widget without TA
            Map<String, Integer> scheduleStatusFromSchedulePublishStatusWidget = dashboardPage.getAllScheduleStatusFromSchedulePublishStatusWidget();

            dashboardPage.clickOnViewSchedulesLinkInSchedulePublishStatusWidget();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            Map<String, Integer> scheduleStatusFromScheduleDMViewPage = scheduleDMViewPage.getThreeWeeksScheduleStatusFromScheduleDMViewPage();
            SimpleUtils.assertOnFail("Schedule status on Schedule Publish Status widget and Schedule region view page are different! ",
                    scheduleStatusFromSchedulePublishStatusWidget.equals(scheduleStatusFromScheduleDMViewPage), false);


            // Validate status value in Schedule Publish Status widget without TA
            dashboardPage.navigateToDashboard();
            dashboardPage.validateTooltipsOfSchedulePublishStatusWidget();

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule Publish Status widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchedulePublishStatusWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            SimpleUtils.assertOnFail("Schedule Publish Status widget not loaded successfully", dashboardPage.isSchedulePublishStatusWidgetDisplay(), false);

            // Validate the content in Schedule Publish Status widget without TA
            dashboardPage.verifyTheContentInSchedulePublishStatusWidget();

            // Validate data in Schedule Publish Status widget without TA
            Map<String, Integer> scheduleStatusFromSchedulePublishStatusWidget = dashboardPage.getAllScheduleStatusFromSchedulePublishStatusWidget();

            dashboardPage.clickOnViewSchedulesLinkInSchedulePublishStatusWidget();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            Map<String, Integer> scheduleStatusFromScheduleDMViewPage = scheduleDMViewPage.getThreeWeeksScheduleStatusFromScheduleDMViewPage();
            SimpleUtils.assertOnFail("Schedule status on Schedule Publish Status widget and Schedule region view page are different! ",
                    scheduleStatusFromSchedulePublishStatusWidget.equals(scheduleStatusFromScheduleDMViewPage), false);

            // Validate status value in Schedule Publish Status widget without TA
            dashboardPage.navigateToDashboard();
            dashboardPage.validateTooltipsOfSchedulePublishStatusWidget();

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule vs. Guidance by Day widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchedulesGuidanceByDayWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            // Set 'Apply labor budget to schedules?' to No
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
            SimpleUtils.assertOnFail("Schedule Vs Guidance By Day widget loaded fail! ",
                    dashboardPage.isScheduleVsGuidanceByDayWidgetDisplay(), false);

            //Validate the content on Schedule Vs Guidance By Day widget display correctly
            dashboardPage.verifyTheContentOnScheduleVsGuidanceByDayWidget();

            // Validate the Guidance comparison in Schedule vs. Guidance by Day widget without TA
            dashboardPage.verifyTheHrsUnderOrCoverBudgetOnScheduleVsGuidanceByDayWidget();

            // Validate the week information in Schedule vs. Guidance by Day widget without TA
            String weekOnScheduleVsGuidanceByDayWidget = dashboardPage.getWeekOnScheduleVsGuidanceByDayWidget();
            String weekInfoFromUpperfieldView = dashboardPage.getWeekInfoFromUpperfieldView();
            SimpleUtils.assertOnFail("The week in Schedule vs. Guidance by Day widget is inconsistent with the week of welcome section of Dashboard page",
                    weekOnScheduleVsGuidanceByDayWidget.equalsIgnoreCase(weekInfoFromUpperfieldView.substring(8)), false);
            dashboardPage.clickOnViewSchedulesOnScheduleVsGuidanceByDayWidget();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            String currentWeekInDMViewSchedule = scheduleDMViewPage.getCurrentWeekInDMView();
            dashboardPage.validateWeekOnScheduleVsGuidanceByDayWidget(weekOnScheduleVsGuidanceByDayWidget, currentWeekInDMViewSchedule);

            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Schedule Vs Guidance By Day widget loaded fail! ",
                    dashboardPage.isScheduleVsGuidanceByDayWidgetDisplay(), false);

            // Validate the data in Schedule vs. Guidance by Day without TA
            HashMap<String, Integer> theSumOfValues = dashboardPage.getTheSumOfValuesOnScheduleVsGuidanceByDayWidget();
            List<String> dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            if (theSumOfValues.get("Guidance").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(0)))
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in Region Summary widget for Guidance and Scheduled");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in Region Summary widget for Guidance and Scheduled",false);

            // Validate value on in Schedule vs. Guidance by Day widget without TA
            dashboardPage.validateValueInScheduleVsGuidanceByDayWidget();

            //  Set 'Apply labor budget to schedules?' to Yes
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
            SimpleUtils.assertOnFail("Schedule Vs Guidance By Day widget loaded fail! ",
                    dashboardPage.isScheduleVsGuidanceByDayWidgetDisplay(), false);

            // Validate the content in Schedule vs. Budget by Day widget without TA
            dashboardPage.verifyTheContentOnScheduleVsBudgetByDayWidget();

            // Validate the data in Schedule vs. Budget by Day widget without TA
            theSumOfValues = dashboardPage.getTheSumOfValuesOnScheduleVsGuidanceByDayWidget();
            dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            if (theSumOfValues.get("Guidance").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(0)))
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in Region Summary widget for Budgeted and Scheduled");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in Region Summary widget for Budgeted and Scheduled",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Schedule vs. Guidance by Day widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySchedulesGuidanceByDayWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            // Set 'Apply labor budget to schedules?' to No
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            SimpleUtils.assertOnFail("Schedule Vs Guidance By Day widget loaded fail! ",
                    dashboardPage.isScheduleVsGuidanceByDayWidgetDisplay(), false);

            //Validate the content on Schedule Vs Guidance By Day widget display correctly
            dashboardPage.verifyTheContentOnScheduleVsGuidanceByDayWidget();

            // Validate the Guidance comparison in Schedule vs. Guidance by Day widget without TA
            dashboardPage.verifyTheHrsUnderOrCoverBudgetOnScheduleVsGuidanceByDayWidget();

            // Validate the week information in Schedule vs. Guidance by Day widget without TA
            String weekOnScheduleVsGuidanceByDayWidget = dashboardPage.getWeekOnScheduleVsGuidanceByDayWidget();
            String weekInfoFromUpperfieldView = dashboardPage.getWeekInfoFromUpperfieldView();
            SimpleUtils.assertOnFail("The week in Schedule vs. Guidance by Day widget is inconsistent with the week of welcome section of Dashboard page",
                    weekOnScheduleVsGuidanceByDayWidget.equalsIgnoreCase(weekInfoFromUpperfieldView.substring(8)), false);
            dashboardPage.clickOnViewSchedulesOnScheduleVsGuidanceByDayWidget();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            String currentWeekInDMViewSchedule = scheduleDMViewPage.getCurrentWeekInDMView();
            dashboardPage.validateWeekOnScheduleVsGuidanceByDayWidget(weekOnScheduleVsGuidanceByDayWidget, currentWeekInDMViewSchedule);

            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Schedule Vs Guidance By Day widget loaded fail! ",
                    dashboardPage.isScheduleVsGuidanceByDayWidgetDisplay(), false);

            // Validate the data in Schedule vs. Guidance by Day without TA
            HashMap<String, Integer> theSumOfValues = dashboardPage.getTheSumOfValuesOnScheduleVsGuidanceByDayWidget();
            List<String> dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            if (theSumOfValues.get("Guidance").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(0)))
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in District Summary widget for Guidance and Scheduled");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in District Summary widget for Guidance and Scheduled",false);

            // Validate value on in Schedule vs. Guidance by Day widget without TA
            dashboardPage.validateValueInScheduleVsGuidanceByDayWidget();

            //  Set 'Apply labor budget to schedules?' to Yes
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            SimpleUtils.assertOnFail("Schedule Vs Guidance By Day widget loaded fail! ",
                    dashboardPage.isScheduleVsGuidanceByDayWidgetDisplay(), false);

            // Validate the content in Schedule vs. Budget by Day widget without TA
            dashboardPage.verifyTheContentOnScheduleVsBudgetByDayWidget();

            // Validate the data in Schedule vs. Budget by Day widget without TA
            theSumOfValues = dashboardPage.getTheSumOfValuesOnScheduleVsGuidanceByDayWidget();
            dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            if (theSumOfValues.get("Guidance").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(0)))
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in District Summary widget for Budgeted and Scheduled");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in District Summary widget for Budgeted and Scheduled",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.toString(),false);
        }
    }

    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Open Shifts widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOpenShiftsWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            SimpleUtils.assertOnFail("Open Shifts widget not loaded successfully", dashboardPage.isOpenShiftsWidgetPresent(), false);

            // Get values on open shifts widget and verify the info on Open_Shifts Widget
            if (dashboardPage.isRefreshButtonDisplay())
                dashboardPage.clickOnRefreshButton();
            HashMap<String, Integer> valuesOnOpenShiftsWidget = dashboardPage.verifyContentOfOpenShiftsWidgetForUpperfield();

            // Validate View Schedules link is clickable
            dashboardPage.clickViewSchedulesLinkOnOpenShiftsWidget();

            // Validate the data in Open Shifts widget without TA
           // todo: blocked by the bug https://legiontech.atlassian.net/browse/SCH-3224

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify Open Shifts widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOpenShiftsWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);


            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            SimpleUtils.assertOnFail("Open Shifts widget not loaded successfully", dashboardPage.isOpenShiftsWidgetPresent(), false);

            // Get values on open shifts widget and verify the info on Open_Shifts Widget
            if (dashboardPage.isRefreshButtonDisplay())
                dashboardPage.clickOnRefreshButton();
            HashMap<String, Integer> valuesOnOpenShiftsWidget = dashboardPage.verifyContentOfOpenShiftsWidgetForUpperfield();

            // Validate View Schedules link is clickable
            dashboardPage.clickOnViewSchedulesOnOpenShiftsWidget();

            // Validate the data in Open Shifts widget without TA
            // todo: blocked by the bug https://legiontech.atlassian.net/browse/SCH-3224

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Compliance Violations widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComplianceViolationsWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            SimpleUtils.assertOnFail("Compliance Violations widget not loaded successfully", dashboardPage.isComplianceViolationsWidgetDisplay(), false);

            // Validate the content on Compliance Violations widget on TA env
            dashboardPage.validateTheContentOnComplianceViolationsWidgetInUpperfield();

            // Validate the data in Compliance Violations widget with TA between dashboard and compliance
            if (dashboardPage.isRefreshButtonDisplay())
                dashboardPage.clickOnRefreshButton();
            List<String> complianceViolationsOnBUViewDashboard = dashboardPage.getComplianceViolationsOnDashboard();
            dashboardPage.clickOnViewViolationsLink();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            Thread.sleep(5000);
            if (compliancePage.isRefreshButtonDisplay())
                compliancePage.clickOnRefreshButton();
            List<String> complianceViolationsFromOnBUViewCompliance = compliancePage.getComplianceViolationsOnSmartCard();
            dashboardPage.validateDataOnComplianceViolationsWidget(complianceViolationsOnBUViewDashboard, complianceViolationsFromOnBUViewCompliance);

            // Validate the data in Compliance Violations widget with TA between BU view and region view
            dashboardPage.navigateToDashboard();
            List<String> regionList = locationSelectorPage.getOrgList();
            Float totalViolationHours = 0.00f;
            int totalViolations = 0;
            for (String region: regionList) {
                locationSelectorPage.changeUpperFieldDirect(Region, region);
                if (dashboardPage.isRefreshButtonDisplay())
                    dashboardPage.clickOnRefreshButton();
                List<String> complianceViolations = dashboardPage.getComplianceViolationsOnDashboard();
                totalViolationHours += Float.valueOf(complianceViolations.get(0).split(" ")[0]);
                totalViolations += Integer.valueOf(complianceViolations.get(1).split(" ")[0]);
            }
            if (totalViolationHours== Float.valueOf(complianceViolationsOnBUViewDashboard.get(0).split(" ")[0])
                    && totalViolations == Integer.valueOf(complianceViolationsOnBUViewDashboard.get(1).split(" ")[0]))
                SimpleUtils.pass("The data of Compliance Violations widget on dashboard is consistent with the summary of each region");
            else
                // SimpleUtils.fail("The data of Compliance Violations widget on dashboard is inconsistent with the summary of each region",false);
                SimpleUtils.warn("SCH-4937: The past week's Extra hours on Compliance BU view is inconsistent with the sum of the extra hours on Region view");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Verify Compliance Violations widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComplianceViolationsWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            SimpleUtils.assertOnFail("Compliance Violations widget not loaded successfully", dashboardPage.isComplianceViolationsWidgetDisplay(), false);

            // Validate the content on Compliance Violations widget on TA env
            dashboardPage.validateTheContentOnComplianceViolationsWidgetInUpperfield();

            // Validate the data in Compliance Violations widget with TA between dashboard and compliance
            if (dashboardPage.isRefreshButtonDisplay())
                dashboardPage.clickOnRefreshButton();
            List<String> complianceViolationsOnRegionViewDashboard = dashboardPage.getComplianceViolationsOnDashboard();
            dashboardPage.clickOnViewViolationsLink();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            Thread.sleep(5000);
            if (compliancePage.isRefreshButtonDisplay())
                compliancePage.clickOnRefreshButton();
            List<String> complianceViolationsFromOnRegionViewCompliance = compliancePage.getComplianceViolationsOnSmartCard();
            dashboardPage.validateDataOnComplianceViolationsWidget(complianceViolationsOnRegionViewDashboard, complianceViolationsFromOnRegionViewCompliance);

            // Validate the data in Compliance Violations widget with TA between BU view and region view
            dashboardPage.navigateToDashboard();
            List<String> districtList = locationSelectorPage.getOrgList();
            Float totalViolationHours = 0.00f;
            int totalViolations = 0;
            for (String district: districtList) {
                locationSelectorPage.changeDistrict(district);
                if (dashboardPage.isRefreshButtonDisplay())
                    dashboardPage.clickOnRefreshButton();
                List<String> complianceViolations = dashboardPage.getComplianceViolationsOnDashboard();
                totalViolationHours += Float.valueOf(complianceViolations.get(0).split(" ")[0]);
                totalViolations += Integer.valueOf(complianceViolations.get(1).split(" ")[0]);
            }
            if (totalViolationHours== Float.valueOf(complianceViolationsOnRegionViewDashboard.get(0).split(" ")[0])
                    && totalViolations == Integer.valueOf(complianceViolationsOnRegionViewDashboard.get(1).split(" ")[0]))
                SimpleUtils.pass("The data of Compliance Violations widget on dashboard is consistent with the summary of each region");
            else
                // SimpleUtils.fail("The data of Compliance Violations widget on dashboard is inconsistent with the summary of each region",false);
                SimpleUtils.warn("SCH-4937: The past week's Extra hours on Compliance BU view is inconsistent with the sum of the extra hours on Region view");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Payroll Projection widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPayrollProjectionWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            //  Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
            SimpleUtils.assertOnFail("Payroll Projection widget loaded fail! ", dashboardPage.isPayrollProjectionWidgetDisplay(), false);

            // Validate the content on Payroll Projection widget with TA
            dashboardPage.validateTheContentOnPayrollProjectionWidget(true);

            // Validate the week information in Payroll Projection widget with TA
            String weekOnPayrollProjectionWidget = dashboardPage.getWeekOnPayrollProjectionWidget();
            String weekOnWelcomeSection = dashboardPage.getWeekInfoFromUpperfieldView();
            dashboardPage.clickOnViewSchedulesOnPayrollProjectWidget();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);
            ScheduleDMViewPage scheduleBUViewPage = pageFactory.createScheduleDMViewPage();
            String currentWeekInBUViewSchedule = scheduleBUViewPage.getCurrentWeekInDMView();
            String forecastKPIInBUViewSchedule = scheduleBUViewPage.getBudgetComparisonInDMView();
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Payroll Projection widget not loaded successfully", dashboardPage.isPayrollProjectionWidgetDisplay(), false);
            SimpleUtils.assertOnFail("The week in Payroll Projection widget is inconsistent with the week of welcome section of Dashboard page", weekOnWelcomeSection.contains(weekOnPayrollProjectionWidget), false);
            dashboardPage.validateWeekOnPayrollProjectionWidget(weekOnPayrollProjectionWidget, currentWeekInBUViewSchedule);

            // Validate today's time line in Payroll Projection widget with TA
            dashboardPage.validateTodayAtTimeOnPayrollProjectionWidget();

            // Validate the budget comparison in Payroll Projection widget with TA
            dashboardPage.validateTheFutureBudgetComparisonOnPayrollProjectionWidget();
            String forecastKPIOnPayrollProjectionWidget = dashboardPage.getBudgetComparisonOnPayrollProjectionWidget();
            dashboardPage.validateBudgetComparisonOnPayrollProjectionWidget(forecastKPIOnPayrollProjectionWidget, forecastKPIInBUViewSchedule);

            // Validate hours tooltips of Payroll Projection widget with TA
            dashboardPage.validateHoursTooltipsOfPayrollProjectionWidget();

            // Validate the data in Payroll Projection widget with TA
            HashMap<String, Integer> theSumOfValues = dashboardPage.getTheSumOfValuesOnPayrollProjectionWidget();
            List<String> dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            if ((Integer.valueOf(dataOnLocationSummaryWidget.get(0)) - theSumOfValues.get("Budgeted") < 14)
                  && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1)))
                  && theSumOfValues.get("Projected").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(2))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in Region Summary widget for Budgeted, Scheduled and Projected");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in Region Summary widget for Budgeted, Scheduled and Projected",false);

            // Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
            SimpleUtils.assertOnFail("Payroll Projection widget loaded fail! ", dashboardPage.isPayrollProjectionWidgetDisplay(), false);

            // Validate the content in Payroll Projection widget with TA
            dashboardPage.validateTheContentOnPayrollProjectionWidget(false);

            // Validate the data in Payroll Projection widget with TA
            theSumOfValues = dashboardPage.getTheSumOfValuesOnPayrollProjectionWidget();
            if (Integer.valueOf(dataOnLocationSummaryWidget.get(0)) - theSumOfValues.get("Budgeted") < 14
                  && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1)))
                  && theSumOfValues.get("Projected").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(2))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in Region Summary widget for Guidance, Scheduled and Projected");
           else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in Region Summary widget for Guidance, Scheduled and Projected",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Payroll Projection widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPayrollProjectionWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            //  Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            SimpleUtils.assertOnFail("Payroll Projection widget loaded fail! ", dashboardPage.isPayrollProjectionWidgetDisplay(), false);

            // Validate the content on Payroll Projection widget with TA
            dashboardPage.validateTheContentOnPayrollProjectionWidget(true);

            // Validate the week information in Payroll Projection widget with TA
            String weekOnPayrollProjectionWidget = dashboardPage.getWeekOnPayrollProjectionWidget();
            String weekOnWelcomeSection = dashboardPage.getWeekInfoFromUpperfieldView();
            dashboardPage.clickOnViewSchedulesOnPayrollProjectWidget();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);
            ScheduleDMViewPage scheduleBUViewPage = pageFactory.createScheduleDMViewPage();
            String currentWeekInBUViewSchedule = scheduleBUViewPage.getCurrentWeekInDMView();
            String forecastKPIInBUViewSchedule = scheduleBUViewPage.getBudgetComparisonInDMView();
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("Payroll Projection widget not loaded successfully", dashboardPage.isPayrollProjectionWidgetDisplay(), false);
            SimpleUtils.assertOnFail("The week in Payroll Projection widget is inconsistent with the week of welcome section of Dashboard page", weekOnWelcomeSection.contains(weekOnPayrollProjectionWidget), false);
            dashboardPage.validateWeekOnPayrollProjectionWidget(weekOnPayrollProjectionWidget, currentWeekInBUViewSchedule);

            // Validate today's time line in Payroll Projection widget with TA
            dashboardPage.validateTodayAtTimeOnPayrollProjectionWidget();

            // Validate the budget comparison in Payroll Projection widget with TA
            dashboardPage.validateTheFutureBudgetComparisonOnPayrollProjectionWidget();
            String forecastKPIOnPayrollProjectionWidget = dashboardPage.getBudgetComparisonOnPayrollProjectionWidget();
            dashboardPage.validateBudgetComparisonOnPayrollProjectionWidget(forecastKPIOnPayrollProjectionWidget, forecastKPIInBUViewSchedule);

            // Validate hours tooltips of Payroll Projection widget with TA
            dashboardPage.validateHoursTooltipsOfPayrollProjectionWidget();

            // Validate the data in Payroll Projection widget with TA
            HashMap<String, Integer> theSumOfValues = dashboardPage.getTheSumOfValuesOnPayrollProjectionWidget();
            List<String> dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            if ((Integer.valueOf(dataOnLocationSummaryWidget.get(0)) - theSumOfValues.get("Budgeted") < 14)
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1)))
                    && theSumOfValues.get("Projected").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(2))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in District Summary widget for Budgeted, Scheduled and Projected");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in District Summary widget for Budgeted, Scheduled and Projected",false);

            // Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            SimpleUtils.assertOnFail("Payroll Projection widget loaded fail! ", dashboardPage.isPayrollProjectionWidgetDisplay(), false);

            // Validate the content in Payroll Projection widget with TA
            dashboardPage.validateTheContentOnPayrollProjectionWidget(false);

            // Validate the data in Payroll Projection widget with TA
            theSumOfValues = dashboardPage.getTheSumOfValuesOnPayrollProjectionWidget();
            if (Integer.valueOf(dataOnLocationSummaryWidget.get(0)) - theSumOfValues.get("Budgeted") < 14
                    && theSumOfValues.get("Scheduled").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(1)))
                    && theSumOfValues.get("Projected").equals(Integer.valueOf(dataOnLocationSummaryWidget.get(2))))
                SimpleUtils.pass("Dashboard Page: The sum of days matches the numbers in District Summary widget for Guidance, Scheduled and Projected");
            else
                SimpleUtils.fail("Dashboard Page: The sum of days doesn't match the numbers in District Summary widget for Guidance, Scheduled and Projected",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Region Summary widget on Dashboard in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRegionSummaryWidgetOnDashboardInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            //  Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
            SimpleUtils.assertOnFail("Regions Summary widget loaded fail!", dashboardPage.isLocationSummaryWidgetDisplay(), false);

            // Validate the content in Region Summary widget
            dashboardPage.verifyTheContentOnOrgSummaryWidget(true,true);

            // Validate region number in Region Summary widget
            List<String> regionList = locationSelectorPage.getOrgList();
            int regionCountInNavigatorBar = regionList.size();
            String title = dashboardPage.getTitleOnOrgSummaryWidget();
            title = title.contains(" ")? title.split(" ")[0]:title;
            SimpleUtils.assertOnFail("Region number in Regions Summary widget is incorrect", Integer.valueOf(title).equals(regionCountInNavigatorBar), false);

            // Validate as of time under Projected in Region Summary widget
            dashboardPage.validateAsOfTimeUnderProjectedOnOrgSummaryWidget();

            // Validate Projected Hrs match
            List<String> dataFromRegionSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();

            dashboardPage.clickOnViewSchedulesOnOrgSummaryWidget();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            List<Float> totalBudgetedScheduledProjectedHour= scheduleDMViewPage.getTheTotalBudgetedScheduledProjectedHourOfScheduleInDMView();
            DecimalFormat df1 = new DecimalFormat("###.#");
            boolean isProjectedHrsMatched = dataFromRegionSummaryWidget.get(2).equals(df1.format(totalBudgetedScheduledProjectedHour.get(2)));
            // SimpleUtils.assertOnFail("Projected hours in Regions Summary widget did not match", isProjectedHrsMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5057

            // Validate Projected Hrs match without TA
            if (MyThreadLocal.getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
                isProjectedHrsMatched = dataFromRegionSummaryWidget.get(2).equals("--");
                // SimpleUtils.assertOnFail("Projected hours in Regions Summary widget did not match", isProjectedHrsMatched, false);
                // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5057
            }

            // Validate Scheduled Hrs match
            boolean isScheduledHrsMatched = dataFromRegionSummaryWidget.get(1).equals(df1.format(totalBudgetedScheduledProjectedHour.get(1)));
            SimpleUtils.assertOnFail("Scheduled hours in Regions Summary widget did not match", isScheduledHrsMatched, false);

            // Validate the  ▲ ▼ caret and hours under Scheduled Hrs
            List<String> regionNumbersFromRegionSummarySmartCard = scheduleDMViewPage.getLocationNumbersFromLocationSummarySmartCard();
            List<String> textOnTheChartInRegionSummarySmartCard= scheduleDMViewPage.getTextFromTheChartInLocationSummarySmartCard();
            boolean isHrsOfUnderOrCoverBudgetMatched = false;
            isHrsOfUnderOrCoverBudgetMatched = dataFromRegionSummaryWidget.get(5).split(" ")[0].
                    equals(textOnTheChartInRegionSummarySmartCard.get(6).split(" ")[0]);
            // SimpleUtils.assertOnFail("The  ▲ ▼ caret and hours under Scheduled Hrs in Regions Summary widget did not match", isHrsOfUnderOrCoverBudgetMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5165

            // Validate Budgeted Hrs match
            boolean isBudgetedHrsMatched = dataFromRegionSummaryWidget.get(0).equals(df1.format(totalBudgetedScheduledProjectedHour.get(0)));
            // SimpleUtils.assertOnFail("Budgeted hours in Regions Summary widget did not match", isBudgetedHrsMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5165

            // Validate the data of Projected Within Budget
            boolean isProjectedWithinBudgetRegionsMatched = dataFromRegionSummaryWidget.get(3).equals(regionNumbersFromRegionSummarySmartCard.get(0));
            SimpleUtils.assertOnFail("Budgeted hours in Regions Summary widget did not match", isProjectedWithinBudgetRegionsMatched, false);

            // Validate the data of Projected Over Budget
            boolean isProjectedOverBudgetRegionsMatched = dataFromRegionSummaryWidget.get(4).equals(regionNumbersFromRegionSummarySmartCard.get(1));
            SimpleUtils.assertOnFail("Budgeted hours in Regions Summary widget did not match", isProjectedOverBudgetRegionsMatched, false);

            //  Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
            SimpleUtils.assertOnFail("Regions Summary widget loaded fail!", dashboardPage.isLocationSummaryWidgetDisplay(), false);

            // Validate the content in Region Summary widget
            dashboardPage.verifyTheContentOnOrgSummaryWidget(true,false);

            //  Validate Guidance Hrs match
            dataFromRegionSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            dashboardPage.clickOnViewSchedulesOnOrgSummaryWidget();
            totalBudgetedScheduledProjectedHour= scheduleDMViewPage.getTheTotalBudgetedScheduledProjectedHourOfScheduleInDMView();
            boolean isGuidanceHrsMatched = dataFromRegionSummaryWidget.get(0).equals(df1.format(totalBudgetedScheduledProjectedHour.get(0)));
            // SimpleUtils.assertOnFail("Budgeted hours in Regions Summary widget did not match", isGuidanceHrsMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5165

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify District Summary widget on Dashboard in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDistrictSummaryWidgetOnDashboardInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            //  Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            SimpleUtils.assertOnFail("Districts Summary widget loaded fail!", dashboardPage.isLocationSummaryWidgetDisplay(), false);

            // Validate the content in Region Summary widget
            dashboardPage.verifyTheContentOnOrgSummaryWidget(true, true);

            // Validate district number in Region Summary widget
            List<String> districtList = locationSelectorPage.getOrgList();
            int districtCountInNavigatorBar = districtList.size();
            String title = dashboardPage.getTitleOnOrgSummaryWidget();
            title = title.contains(" ")? title.split(" ")[0]:title;
            SimpleUtils.assertOnFail("District number in Districts Summary widget is incorrect", Integer.valueOf(title).equals(districtCountInNavigatorBar), false);

            // Validate as of time under Projected in Region Summary widget
            dashboardPage.validateAsOfTimeUnderProjectedOnOrgSummaryWidget();

            // Validate Projected Hrs match
            List<String> dataFromDistrictSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();

            dashboardPage.clickOnViewSchedulesOnOrgSummaryWidget();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            List<Float> totalBudgetedScheduledProjectedHour= scheduleDMViewPage.getTheTotalBudgetedScheduledProjectedHourOfScheduleInDMView();
            DecimalFormat df1 = new DecimalFormat("###.#");
            boolean isProjectedHrsMatched = dataFromDistrictSummaryWidget.get(2).equals(df1.format(totalBudgetedScheduledProjectedHour.get(2)));
            // SimpleUtils.assertOnFail("Projected hours in Districts Summary widget did not match", isProjectedHrsMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5057

            // Validate Projected Hrs match without TA
            if (MyThreadLocal.getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
                isProjectedHrsMatched = dataFromDistrictSummaryWidget.get(2).equals("--");
                // SimpleUtils.assertOnFail("Projected hours in Districts Summary widget did not match", isProjectedHrsMatched, false);
                // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5057
            }

            // Validate Scheduled Hrs match
            boolean isScheduledHrsMatched = dataFromDistrictSummaryWidget.get(1).equals(df1.format(totalBudgetedScheduledProjectedHour.get(1)));
            SimpleUtils.assertOnFail("Scheduled hours in Districts Summary widget did not match", isScheduledHrsMatched, false);

            // Validate the  ▲ ▼ caret and hours under Scheduled Hrs
            List<String> regionNumbersFromRegionSummarySmartCard = scheduleDMViewPage.getLocationNumbersFromLocationSummarySmartCard();
            List<String> textOnTheChartInRegionSummarySmartCard= scheduleDMViewPage.getTextFromTheChartInLocationSummarySmartCard();
            boolean isHrsOfUnderOrCoverBudgetMatched = false;
            isHrsOfUnderOrCoverBudgetMatched = dataFromDistrictSummaryWidget.get(5).split(" ")[0].
                    equals(textOnTheChartInRegionSummarySmartCard.get(6).split(" ")[0]);
            // SimpleUtils.assertOnFail("The  ▲ ▼ caret and hours under Scheduled Hrs in Districts Summary widget did not match", isHrsOfUnderOrCoverBudgetMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5165

            // Validate Budgeted Hrs match
            boolean isBudgetedHrsMatched = dataFromDistrictSummaryWidget.get(0).equals(df1.format(totalBudgetedScheduledProjectedHour.get(0)));
            // SimpleUtils.assertOnFail("Budgeted hours in Districts Summary widget did not match", isBudgetedHrsMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5165

            // Validate the data of Projected Within Budget
            boolean isProjectedWithinBudgetRegionsMatched = dataFromDistrictSummaryWidget.get(3).equals(regionNumbersFromRegionSummarySmartCard.get(0));
            SimpleUtils.assertOnFail("Budgeted hours in Districts Summary widget did not match", isProjectedWithinBudgetRegionsMatched, false);

            // Validate the data of Projected Over Budget
            boolean isProjectedOverBudgetRegionsMatched = dataFromDistrictSummaryWidget.get(4).equals(regionNumbersFromRegionSummarySmartCard.get(1));
            SimpleUtils.assertOnFail("Budgeted hours in Districts Summary widget did not match", isProjectedOverBudgetRegionsMatched, false);

            //  Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            dashboardPage.navigateToDashboard();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            SimpleUtils.assertOnFail("Districts Summary widget loaded fail!", dashboardPage.isLocationSummaryWidgetDisplay(), false);

            // Validate the content in Region Summary widget
            dashboardPage.verifyTheContentOnOrgSummaryWidget(true, false);

            //  Validate Guidance Hrs match
            dataFromDistrictSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            dashboardPage.clickOnViewSchedulesOnOrgSummaryWidget();
            totalBudgetedScheduledProjectedHour= scheduleDMViewPage.getTheTotalBudgetedScheduledProjectedHourOfScheduleInDMView();
            boolean isGuidanceHrsMatched = dataFromDistrictSummaryWidget.get(0).equals(df1.format(totalBudgetedScheduledProjectedHour.get(0)));
            // SimpleUtils.assertOnFail("Budgeted hours in Districts Summary widget did not match", isGuidanceHrsMatched, false);
            // todo: Failed due to https://legiontech.atlassian.net/browse/SCH-5165

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Schedule functionality in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleFunctionalityForBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String currentRegion = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            // Validate the title

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule Page in BU View not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);
            scheduleCommonPage.verifyHeaderOnSchedule();
            Map<String, String> selectedUpperFieldsInSchedule = locationSelectorPage.getSelectedUpperFields();
            if (selectedUpperFieldsInSchedule.get(BusinessUnit).equals(selectedUpperFields.get(BusinessUnit)) && locationSelectorPage.isRegionSelected("All Regions"))
                SimpleUtils.pass("Schedule Page in BU View: The title includes selected BU and All Regions");

            // Validate changing BUs on Schedule
            String currentBU = selectedUpperFieldsInSchedule.get(BusinessUnit);
            locationSelectorPage.changeAnotherBUInBUView();
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.verifyTheDisplayBUWithSelectedBUConsistent(selectedUpperFields.get(BusinessUnit));

            // The field columns can be ordered successfully
            List<String> regionsInScheduleBUViewRegionsTable =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);

            // Validate search function
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(currentBU);
            scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(currentRegion);

            // Validate the clickability of forward and backward button
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Schedule functionality in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleFunctionalityForRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String currentDistrict = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            // Validate the title
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule Page in Region View not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);
            scheduleCommonPage.verifyHeaderOnSchedule();
            Map<String, String> selectedUpperFieldsInSchedule = locationSelectorPage.getSelectedUpperFields();
            if (selectedUpperFieldsInSchedule.get(Region).equals(selectedUpperFields.get(Region)) && locationSelectorPage.isRegionSelected("All Districts"))
                SimpleUtils.pass("Schedule Page in Region View: The title includes selected region and All Districts");

            // Validate changing BUs on Schedule
            String currentRegion = selectedUpperFieldsInSchedule.get(Region);
            locationSelectorPage.changeAnotherRegionInRegionView();
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.verifyTheDisplayRegionWithSelectedRegionConsistent(selectedUpperFields.get(Region));

            // The field columns can be ordered successfully
            List<String> regionsInScheduleRegionViewRegionsTable =  scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable();
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);

            // Validate search function
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(currentRegion);
            scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(currentDistrict);

            // Validate the clickability of forward and backward button
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Refresh feature on Schedule in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnScheduleInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));


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
                String notStartedRegion = scheduleDMViewPage.getLocationsWithNotStartedSchedules().get(0);
                scheduleDMViewPage.clickOnLocationNameInDMView(notStartedRegion);
                List<String> notStartedDistricts = scheduleDMViewPage.getLocationsWithNotStartedSchedules();
                for (String notStartedDistrict: notStartedDistricts) {
                    scheduleDMViewPage.clickOnLocationNameInDMView(notStartedDistrict);
                    List<String> notStartedLocations = scheduleDMViewPage.getLocationsWithNotStartedSchedules();
                    for (String notStartedLocation: notStartedLocations) {
                        scheduleDMViewPage.clickOnLocationNameInDMView(notStartedLocation);
                        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
                        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
                        if (!isWeekGenerated) {
                            createSchedulePage.createScheduleForNonDGFlowNewUI();
                        }
                        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(notStartedDistrict);
                    }
                    locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(notStartedRegion);
                }
                locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
                scheduleDMViewPage.clickOnRefreshButton();
                String scheduleStatus = scheduleDMViewPage.getScheduleStatusForGivenLocation(notStartedRegion);
                if (scheduleStatus.equals("In Progress"))
                    SimpleUtils.pass("Schedule Page in BU View: After the first refreshing, it is \"In Progress\" status");
                else
                    SimpleUtils.fail("Schedule Page in BU View: After the first refreshing, it isn't \"In Progress\" status", false);
                scheduleDMViewPage.clickOnLocationNameInDMView(notStartedRegion);
                List<String> inProgressDistricts = scheduleDMViewPage.getLocationsWithInProgressSchedules();
                for (String inProgressDistrict: inProgressDistricts) {
                    scheduleDMViewPage.clickOnLocationNameInDMView(inProgressDistrict);
                    List<String> inProgressLocations = scheduleDMViewPage.getLocationsWithInProgressSchedules();
                    for (String inProgressLocation: inProgressLocations) {
                        scheduleDMViewPage.clickOnLocationNameInDMView(inProgressLocation);
                        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
                        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
                        if (!isWeekGenerated) {
                            createSchedulePage.createScheduleForNonDGFlowNewUI();
                        }
                        shiftOperatePage.deleteAllOOOHShiftInWeekView();
                        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                        if (createSchedulePage.isPublishButtonLoaded() || createSchedulePage.isRepublishButtonLoadedOnSchedulePage()) {
                            createSchedulePage.publishActiveSchedule();
                        }
                        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(inProgressDistrict);
                    }
                    locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(notStartedRegion);
                }
                locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
                scheduleDMViewPage.clickOnRefreshButton();
                scheduleStatus = scheduleDMViewPage.getScheduleStatusForGivenLocation(notStartedRegion);
                if (scheduleStatus.equals("Published"))
                    SimpleUtils.pass("Schedule Page in BU View: After the second refreshing, it is \"Published\" status");
                else
                    SimpleUtils.fail("Schedule Page in BU Viewe: After the second refreshing, it isn't \"Published\" status", false);
            } else
                SimpleUtils.report("Schedule Page in BU View: There are no Not Started schedules in the current and upcoming weeks");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify Refresh feature on Schedule in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnScheduleInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));


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
                String notStartedDistrict = scheduleDMViewPage.getLocationsWithNotStartedSchedules().get(0);
                scheduleDMViewPage.clickOnLocationNameInDMView(notStartedDistrict);
                List<String> notStartedLocations = scheduleDMViewPage.getLocationsWithNotStartedSchedules();
                for (String notStartedLocation: notStartedLocations) {
                    scheduleDMViewPage.clickOnLocationNameInDMView(notStartedLocation);
                    SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
                    boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
                    if (!isWeekGenerated) {
                        createSchedulePage.createScheduleForNonDGFlowNewUI();
                    }
                    locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(notStartedDistrict);
                }
                locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(Region));
                scheduleDMViewPage.clickOnRefreshButton();
                String scheduleStatus = scheduleDMViewPage.getScheduleStatusForGivenLocation(notStartedDistrict);
                if (scheduleStatus.equals("In Progress"))
                    SimpleUtils.pass("Schedule Page in Region View: After the first refreshing, it is \"In Progress\" status");
                else
                    SimpleUtils.fail("Schedule Page in Region View: After the first refreshing, it isn't \"In Progress\" status", false);
                scheduleDMViewPage.clickOnLocationNameInDMView(notStartedDistrict);
                List<String> inProgressLocations = scheduleDMViewPage.getLocationsWithInProgressSchedules();
                for (String inProgressLocation: inProgressLocations) {
                    scheduleDMViewPage.clickOnLocationNameInDMView(inProgressLocation);
                    SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
                    boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
                    if (!isWeekGenerated) {
                        createSchedulePage.createScheduleForNonDGFlowNewUI();
                    }
                    shiftOperatePage.deleteAllOOOHShiftInWeekView();
                    scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                    if (createSchedulePage.isPublishButtonLoaded() || createSchedulePage.isRepublishButtonLoadedOnSchedulePage()) {
                        createSchedulePage.publishActiveSchedule();
                    }
                    locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(notStartedDistrict);
                }
                locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(Region));
                scheduleDMViewPage.clickOnRefreshButton();
                scheduleStatus = scheduleDMViewPage.getScheduleStatusForGivenLocation(notStartedDistrict);
                if (scheduleStatus.equals("Published"))
                    SimpleUtils.pass("Schedule Page in Region View: After the second refreshing, it is \"Published\" status");
                else
                    SimpleUtils.fail("Schedule Page in Region Viewe: After the second refreshing, it isn't \"Published\" status", false);
            } else
                SimpleUtils.report("Schedule Page in Region View: There are no Not Started schedules in the current and upcoming weeks");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the content between weeks on Schedule in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentBetweenWeeksOnScheduleInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, selectedUpperFields.get(BusinessUnit));

            // Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);

            // Validate field names in analytics table for current/future weeks when budget is Yes
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, false);

            // Validate field names in analytics table for past weeks when budget is Yes
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, true);

            // Validate field names in analytics table for past weeks when budget is Yes
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,true);

            // Validate smart cards for current/future weeks when budget is Yes
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,false);

            // Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(selectedUpperFields.get(BusinessUnit));
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);

            // Validate field names in analytics table for past weeks when budget is No
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false,true);

            // Validate smart cards for current/future weeks when budget is No
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false,false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false,false);

            // Validate field names in analytics table for current/future weeks when budget is No
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, false);
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, false);

            // Validate field names in analytics table for past weeks when budget is No
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, true);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the content between weeks on Schedule in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentBetweenWeeksOnScheduleInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            // Set 'Apply labor budget to schedules?' to Yes
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);

            // Validate field names in analytics table for current/future weeks when budget is Yes
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, false);

            // Validate field names in analytics table for past weeks when budget is Yes
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(true, true);

            // Validate field names in analytics table for past weeks when budget is Yes
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,true);

            // Validate smart cards for current/future weeks when budget is Yes
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(true,false);

            // Set 'Apply labor budget to schedules?' to No
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("No");
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationSelectorPage.changeUpperFieldDirect(Region, selectedUpperFields.get(Region));
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);

            // Validate field names in analytics table for past weeks when budget is No
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false,true);

            // Validate smart cards for current/future weeks when budget is No
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false,false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.verifySmartCardsAreLoadedForPastOrFutureWeek(false,false);

            // Validate field names in analytics table for current/future weeks when budget is No
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, false);
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, false);

            // Validate field names in analytics table for past weeks when budget is No
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleDMViewPage.verifySchedulesTableHeaderNames(false, true);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Region View Navigation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRegionViewNavigationAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get(District);
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Validate user has see multiple regions in upperfield dropdown list
            List<String> upperFieldNames = locationSelectorPage.getAllUpperFieldNamesInUpperFieldDropdownList(Region);
            SimpleUtils.assertOnFail("The selected region should display in the search region dropdown list!",
                    upperFieldNames.contains(selectedUpperFields.get(Region)), false);

            //Validate drilling into a district
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            locationSelectorPage.isDMView();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();

            //Validate navigating back to region view
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Validate navigating back to region view
            SimpleUtils.assertOnFail("Schedule Region view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);

            SimpleUtils.assertOnFail("The district: " + districtName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName), false);
            //Validate changing regions
            String[] upperFields2 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            String regionName2 = upperFields2[upperFields2.length-2].trim();
            String districtName2 = upperFields2[upperFields2.length-1].trim();
            locationSelectorPage.changeUpperFieldDirect(Region, regionName2);
            SimpleUtils.assertOnFail("Schedule Region view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            SimpleUtils.assertOnFail("The district: " + districtName2+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName2), false);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            SimpleUtils.assertOnFail("The selected region display incorrectly! The expected region is: " + regionName2 +
                            " . The actual region name is " + selectedUpperFields.get(Region),
                    selectedUpperFields.get(Region).equalsIgnoreCase(regionName2), false);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Validate changing dates and district
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            locationSelectorPage.isDMView();
            scheduleCommonPage.navigateToNextWeek();
            String selectedWeekInfo = mySchedulePage.getSelectedWeek();
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("The selected week should not been changed after change to region view from DM view! ",
                    mySchedulePage.getSelectedWeek().equalsIgnoreCase(selectedWeekInfo), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "BU View Navigation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyBUViewNavigationAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            //Validate user has access to multiple BUs in upperfield dropdown list
            List<String> upperFieldNames = locationSelectorPage.getAllUpperFieldNamesInUpperFieldDropdownList(BusinessUnit);
            SimpleUtils.assertOnFail("The selected region should display in the search region dropdown list!",
                    upperFieldNames.contains(buName), false);

            //Validate drilling into a region
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();

            //Validate navigating back to BU view
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            locationSelectorPage.isBUView();

            SimpleUtils.assertOnFail("Schedule BU view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            SimpleUtils.assertOnFail("The region: " + regionName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(regionName), false);
            //Validate changing BUs
            String[] upperFields3 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields3 = controlUpperFields3;
            } else {
                upperFields3 = opUpperFields3;
            }
            String buName2 = upperFields3[upperFields3.length-3].trim();
            String regionName2 = upperFields3[upperFields3.length-2].trim();
            String districtName2 = upperFields3[upperFields3.length-1].trim();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName2);
            SimpleUtils.assertOnFail("Schedule BU view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            SimpleUtils.assertOnFail("The region: " + regionName2+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(regionName2), false);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            SimpleUtils.assertOnFail("The selected BU display incorrectly! The expected BU is: " + buName2 +
                            " . The actual BU name is " + selectedUpperFields.get(BusinessUnit),
                    selectedUpperFields.get(BusinessUnit).equalsIgnoreCase(buName2), false);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            //Validate changing dates and district
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            scheduleCommonPage.navigateToNextWeek();
            String selectedWeekInfo = mySchedulePage.getSelectedWeek();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            SimpleUtils.assertOnFail("The selected week should not been changed after change to BU view from region view! ",
                    mySchedulePage.getSelectedWeek().equalsIgnoreCase(selectedWeekInfo), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Controls in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyControlsInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            //Validate Controls existing from BU view
            SimpleUtils.assertOnFail("Controls menu tab should be available on BU view",
                    !dashboardPage.isConsoleNavigationBarIsGray("Controls"), false);

            //Validate navigate to Controls from BU view
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page fail to load! ",
                    controlsNewUIPage.isControlsPageLoaded(), false);
            //Title: Controls Global > All Locations
            controlsNewUIPage.getCurrentLocationInControls().equalsIgnoreCase("All Locations");

            //Validate week navigation in BU View getting updated based on schedule planning window settings
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateSchedulePlanningWindow("5 weeks", false, true);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            String weekInfo = "";
            String selectedWeekInfo = mySchedulePage.getSelectedWeek();
            int i = 0;
            while (!weekInfo.equalsIgnoreCase(selectedWeekInfo) && scheduleDMViewPage.hasNextWeek()){
                weekInfo = mySchedulePage.getSelectedWeek();
                scheduleCommonPage.navigateToNextWeek();
                selectedWeekInfo = mySchedulePage.getSelectedWeek();
                i++;
            }
            SimpleUtils.assertOnFail("The week navigation in BU View should get updated to " +
                    "5 weeks based on schedule planning window settings for Schedule, but the actual weeks are: " + i,
                    i==5, false);

            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();

            weekInfo = "";
            selectedWeekInfo = mySchedulePage.getSelectedWeek();
            i = 0;
            while (!weekInfo.equalsIgnoreCase(selectedWeekInfo)&& scheduleDMViewPage.hasNextWeek()){
                weekInfo = mySchedulePage.getSelectedWeek();
                scheduleCommonPage.navigateToNextWeek();
                selectedWeekInfo = mySchedulePage.getSelectedWeek();
                i++;
            }
            SimpleUtils.assertOnFail("The week navigation in BU View should get updated to be " +
                            "5 weeks based on schedule planning window settings for Compliance, but the actual weeks are: " + i,
                    i==5, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Controls in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyControlsInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Validate Controls existing from Region view
            SimpleUtils.assertOnFail("Controls menu tab should be available on Region view",
                    !dashboardPage.isConsoleNavigationBarIsGray("Controls"), false);

            //Validate navigate to Controls from Region view
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page fail to load! ",
                    controlsNewUIPage.isControlsPageLoaded(), false);
            //Title: Controls Global > All Locations
            controlsNewUIPage.getCurrentLocationInControls().equalsIgnoreCase("All Locations");

            //Validate week navigation in Region View getting updated based on schedule planning window settings
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.updateSchedulePlanningWindow("5 weeks", false, true);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            String weekInfo = "";
            String selectedWeekInfo = mySchedulePage.getSelectedWeek();
            int i = 0;
            while (!weekInfo.equalsIgnoreCase(selectedWeekInfo) && scheduleDMViewPage.hasNextWeek()){
                weekInfo = mySchedulePage.getSelectedWeek();
                scheduleCommonPage.navigateToNextWeek();
                selectedWeekInfo = mySchedulePage.getSelectedWeek();
                i++;
            }
            SimpleUtils.assertOnFail("The week navigation in Region View should get updated to " +
                            "5 weeks based on schedule planning window settings for Schedule, but the actual weeks are: " + i,
                    i==5, false);

            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();

            weekInfo = "";
            selectedWeekInfo = mySchedulePage.getSelectedWeek();
            i = 0;
            while (!weekInfo.equalsIgnoreCase(selectedWeekInfo)&& scheduleDMViewPage.hasNextWeek()){
                weekInfo = mySchedulePage.getSelectedWeek();
                scheduleCommonPage.navigateToNextWeek();
                selectedWeekInfo = mySchedulePage.getSelectedWeek();
                i++;
            }
            SimpleUtils.assertOnFail("The week navigation in Region View should get updated to be " +
                            "5 weeks based on schedule planning window settings for Compliance, but the actual weeks are: " + i,
                    i==5, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify analytics table on Compliance in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnComplianceInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully", compliancePage.isCompliancePageLoaded(), false);

            // Validate the field names in analytics table
            compliancePage.verifyFieldNamesInAnalyticsTable(Region);

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
            List<String> dataInBUViewForPast = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(regionName);
            String totalExtraHoursInBUView = dataInBUViewForPast.get(0);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            String totalHrsInRegionViewForPast = compliancePage.getTheTotalViolationHrsFromSmartCard().split(" ")[0];
            SimpleUtils.report("Total Extra Hours In BU View for past week is "+totalExtraHoursInBUView);
            SimpleUtils.report("Total Extra Hours In Region View for past week is "+totalHrsInRegionViewForPast);
//            SimpleUtils.assertOnFail("Compliance Page: Analytics table doesn't match the past week's data",   //Blocked by https://legiontech.atlassian.net/browse/SCH-4937
//                    totalHrsInRegionViewForPast.equals(String.valueOf(Math.round(Float.parseFloat(totalExtraHoursInBUView)))), false);

            // Validate the data of analytics table for current week.
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            compliancePage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for current week successfully",
                    compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInDMForCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(regionName);
            String totalExtraHoursInBUViewForCurrent = dataInDMForCurrent.get(0);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            String totalHrsInRegionForCurrent = compliancePage.getTheTotalViolationHrsFromSmartCard().split(" ")[0];

            SimpleUtils.report("Total Extra Hours In BU View for current week is " + totalExtraHoursInBUViewForCurrent);
            SimpleUtils.report("Total Extra Hours In Region View for current week is " + totalHrsInRegionForCurrent);
            SimpleUtils.assertOnFail("Compliance Page: Analytics table doesn't match the current week's data",
                    totalHrsInRegionForCurrent.equals(String.valueOf(Math.round(Float.parseFloat((totalExtraHoursInBUViewForCurrent))))), false);

            // Validate the data of analytics table for future week
            compliancePage.clickOnComplianceConsoleMenu();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            compliancePage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for future week successfully",compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInBUForFuture = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(regionName);
            String totalExtraHoursInBUViewForFuture = dataInBUForFuture.get(0);
            SimpleUtils.report("Total Extra Hours In DM View for future week is " + totalExtraHoursInBUViewForFuture);
            SimpleUtils.assertOnFail("Compliance Page: Analytics table doesn't match the future week's data",
                    totalExtraHoursInBUViewForFuture.equals("0"), false);


            // Validate Late Schedule is Yes or No
            compliancePage.navigateToPreviousWeek();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            Thread.sleep(2000);
            controlsNewUIPage.updateDaysInAdvancePublishSchedulesInSchedulingPolicies("7");

            compliancePage.clickOnComplianceConsoleMenu();
            List<String>  dataCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(regionName);
            String lateScheduleYes = dataCurrent.get(dataCurrent.size()-1);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            List<String> upperFieldNames = compliancePage.getAllUpperFieldNamesOnAnalyticsTable();
            List<String> schedulePublishedOnTime = new ArrayList<>();
            for (String upperFieldName: upperFieldNames){
                dataCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(upperFieldName);
                schedulePublishedOnTime.add(dataCurrent.get(dataCurrent.size()-1));
            }

            if (lateScheduleYes.equals("Yes"))
                SimpleUtils.assertOnFail("Compliance Page: Late Schedule is not Yes", !schedulePublishedOnTime.contains("No"), false);
            else
                SimpleUtils.assertOnFail("Compliance Page: Late Schedule is not contain No", schedulePublishedOnTime.contains("No"),false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify analytics table on Compliance in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnComplianceInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String[] upperFields2 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            String regionName = upperFields2[upperFields2.length-2].trim();
            String districtName = upperFields2[upperFields2.length-1].trim();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

//            LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully",
                    compliancePage.isCompliancePageLoaded(), false);

            // Validate the field names in analytics table
            compliancePage.verifyFieldNamesInAnalyticsTable(District);
            compliancePage.clickOnRefreshButton();

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
            List<String> dataInRegionViewForPast = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(districtName);
            String totalExtraHoursInRegionView = dataInRegionViewForPast.get(0);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            String totalHrsInDistrictViewForPast = compliancePage.getTheTotalViolationHrsFromSmartCard().split(" ")[0];
            SimpleUtils.report("Total Extra Hours In Region View for past week is "+totalExtraHoursInRegionView);
            SimpleUtils.report("Total Extra Hours In District View for past week is "+totalHrsInDistrictViewForPast);
//            SimpleUtils.assertOnFail("Compliance Page: Analytics table doesn't match the past week's data",                           //Blocked by https://legiontech.atlassian.net/browse/SCH-4937
//                    totalHrsInDistrictViewForPast.equals
//                            (String.valueOf(Math.round(Float.parseFloat(totalExtraHoursInRegionView.replace(",",""))))), false);

            // Validate the data of analytics table for current week.
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            compliancePage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for current week successfully",
                    compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInRegionForCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(districtName);
            String totalExtraHoursInRegionViewForCurrent = dataInRegionForCurrent.get(0);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            String totalHrsInDistrictForCurrent = compliancePage.getTheTotalViolationHrsFromSmartCard().split(" ")[0];

            SimpleUtils.report("Total Extra Hours In BU View for current week is " + totalExtraHoursInRegionViewForCurrent);
            SimpleUtils.report("Total Extra Hours In Region View for current week is " + totalHrsInDistrictForCurrent);
            SimpleUtils.assertOnFail("Compliance Page: Analytics table doesn't match the current week's data",
                    totalHrsInDistrictForCurrent.equals(String.valueOf(Math.round(Float.parseFloat((totalExtraHoursInRegionViewForCurrent))))), false);

            // Validate the data of analytics table for future week
            compliancePage.clickOnComplianceConsoleMenu();
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            compliancePage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for future week successfully",
                    compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInRegionForFuture = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(districtName);
            String totalExtraHoursInRegionViewForFuture = dataInRegionForFuture.get(0);
            SimpleUtils.report("Total Extra Hours In Region View for future week is " + totalExtraHoursInRegionViewForFuture);
            SimpleUtils.assertOnFail("Compliance Page: Analytics table doesn't match the future week's data",
                    totalExtraHoursInRegionViewForFuture.equals("0"), false);

            // Validate Late Schedule is Yes or No
            compliancePage.navigateToPreviousWeek();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            Thread.sleep(2000);
            controlsNewUIPage.updateDaysInAdvancePublishSchedulesInSchedulingPolicies("7");

            compliancePage.clickOnComplianceConsoleMenu();
            List<String>  dataCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(districtName);
            String lateScheduleYes = dataCurrent.get(dataCurrent.size()-1);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            List<String> upperFieldNames = compliancePage.getAllUpperFieldNamesOnAnalyticsTable();
            List<String> schedulePublishedOnTime = new ArrayList<>();
            for (String upperFieldName: upperFieldNames){
                dataCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(upperFieldName);
                schedulePublishedOnTime.add(dataCurrent.get(dataCurrent.size()-1));
            }

            if (lateScheduleYes.equals("Yes"))
                SimpleUtils.assertOnFail("Compliance Page: Late Schedule is not Yes", schedulePublishedOnTime.contains("Yes"), false);
            else
                SimpleUtils.assertOnFail("Compliance Page: Late Schedule is not contain No", !schedulePublishedOnTime.contains("Yes"),false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Compliance functionality on Compliance in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComplianceFunctionalityOnComplianceInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();

            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get(District);
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            //Validate the title and info of Compliance page.
            timeSheetPage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance Page not loaded Successfully!",compliancePage.isCompliancePageLoaded() , false);

            //Verify BU selected and displayed with "All Regions".
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            SimpleUtils.assertOnFail("The selected BU display incorrectly! ",
                    selectedUpperFields.get(BusinessUnit).equalsIgnoreCase(buName), false);
            SimpleUtils.assertOnFail("The 'All Regions' display incorrectly! ",
                    selectedUpperFields.get(Region).equalsIgnoreCase("All Regions"), false);

            //Validate search function.
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(districtName);

            //Validate the clickability of backward button.
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToPreviousWeek();

            //Validate the clickability of forward button.
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

            //Validate changing BUs on Compliance
            String[] upperFields3 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields3 = controlUpperFields3;
            } else {
                upperFields3 = opUpperFields3;
            }
            String buName2 = upperFields3[upperFields3.length-3].trim();
            String regionName2 = upperFields3[upperFields3.length-2].trim();
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName2);

            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(regionName2);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Compliance functionality on Compliance in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComplianceFunctionalityOnComplianceInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();

            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get(District);
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Validate the title and info of Compliance page.
            timeSheetPage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance Page not loaded Successfully!",compliancePage.isCompliancePageLoaded() , false);

            //Verify BU selected and displayed with "All Regions".
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            SimpleUtils.assertOnFail("The selected Region display incorrectly! ",
                    selectedUpperFields.get(Region).equalsIgnoreCase(regionName), false);
            SimpleUtils.assertOnFail("The 'All District' display incorrectly! ",
                    selectedUpperFields.get(District).equalsIgnoreCase("All Districts"), false);

            //Validate search function.
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(location);

            //Validate the click ability of backward button.
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToPreviousWeek();

            //Validate the click ability of forward button.
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

            //Validate changing Regions on Compliance
            String[] upperFields2 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            String regionName2 = upperFields2[upperFields2.length-2].trim();
            String districtName2 = upperFields2[upperFields2.length-1].trim();
            locationSelectorPage.changeUpperFieldDirect(Region, regionName2);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(districtName2);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Refresh feature on Compliance in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnComplianceInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            // Validate the presence of Refresh button
            scheduleDMViewPage.validateThePresenceOfRefreshButton();

            // Validate Refresh timestamp
            scheduleDMViewPage.validateRefreshTimestamp();

            // Validate Refresh when navigation back
            dashboardPage.validateRefreshWhenNavigationBack("Compliance");

            // Validate Refresh function
            scheduleDMViewPage.validateRefreshFunction();

            // Validate Refresh performance
            scheduleDMViewPage.validateRefreshPerformance();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Refresh feature on Compliance in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnComplianceInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            // Validate the presence of Refresh button
            scheduleDMViewPage.validateThePresenceOfRefreshButton();

            // Validate Refresh timestamp
            scheduleDMViewPage.validateRefreshTimestamp();

            // Validate Refresh when navigation back
            dashboardPage.validateRefreshWhenNavigationBack("Compliance");

            // Validate Refresh function
            scheduleDMViewPage.validateRefreshFunction();

            // Validate Refresh performance
            scheduleDMViewPage.validateRefreshPerformance();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the availability of region list and sub region on Compliance in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRegionListAndSubRegionOnComplianceInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            //Validate the region list
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            SimpleUtils.assertOnFail("The Regions on Compliance page in BU View display incorrectly! ", compliancePage.getAllUpperFieldNamesOnAnalyticsTable().size()>1, false);

            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(regionName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the availability of district list and sub district on Compliance in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDistrictListAndSubDistrictOnComplianceInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get(District);
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            //Validate the region list
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            SimpleUtils.assertOnFail("The Regions on Compliance page in BU View display incorrectly! ", compliancePage.getAllUpperFieldNamesOnAnalyticsTable().size()>1, false);

            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(districtName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TOTAL VIOLATION HRS on Compliance in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTotalViolationHrsOnComplianceInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            //Validate the content of Toatal Violation smart card for current week.
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            HashMap<String, Float> valuesFromToatalViolationCard =  compliancePage.getValuesAndVerifyInfoForTotalViolationSmartCardInDMView();

            //Validate the data Toatal Violation smart card for current week.
            //Verify total violation hours for current week.
            List<String> allUpperFields = compliancePage.getAllUpperFieldNames();
            float totalViolationHrsFromTable = 0;
            for (String upperField: allUpperFields){
                totalViolationHrsFromTable = totalViolationHrsFromTable +
                        Float.parseFloat(compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(upperField).get("totalExtraHours"));
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
            SimpleUtils.assertOnFail("Violation hours for last week are inconsistent!",
                    (Math.abs(valuesFromToatalViolationCard.get("vioHrsPastWeek")) - valuesFromToatalViolationCardForLastWeek.get("vioHrsCurrentWeek")) == 0,
                    false);

            //Verify diff hours.
            SimpleUtils.assertOnFail("Diff hours with last week is incorrect!",
                    (Math.abs(Math.abs(valuesFromToatalViolationCard.get("vioHrsCurrentWeek"))
                            - valuesFromToatalViolationCard.get("vioHrsPastWeek"))-valuesFromToatalViolationCard.get("diffHrs")) == 0,
                    false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TOTAL VIOLATION HRS on Compliance in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTotalViolationHrsOnComplianceInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            //Validate the content of Toatal Violation smart card for current week.
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            HashMap<String, Float> valuesFromToatalViolationCard =  compliancePage.getValuesAndVerifyInfoForTotalViolationSmartCardInDMView();

            //Validate the data Toatal Violation smart card for current week.
            //Verify total violation hours for current week.
            List<String> allUpperFields = compliancePage.getAllUpperFieldNames();
            float totalViolationHrsFromTable = 0;
            for (String upperField: allUpperFields){
                totalViolationHrsFromTable = totalViolationHrsFromTable +
                        Float.parseFloat(compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(upperField).get("totalExtraHours"));
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
            SimpleUtils.assertOnFail("Violation hours for last week are inconsistent!",
                    (Math.abs(valuesFromToatalViolationCard.get("vioHrsPastWeek")) - valuesFromToatalViolationCardForLastWeek.get("vioHrsCurrentWeek")) == 0,
                    false);

            //Verify diff hours.
            SimpleUtils.assertOnFail("Diff hours with last week is incorrect!",
                    (Math.abs(Math.abs(valuesFromToatalViolationCard.get("vioHrsCurrentWeek"))
                            - valuesFromToatalViolationCard.get("vioHrsPastWeek"))-valuesFromToatalViolationCard.get("diffHrs")) == 0,
                    false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Regions with Violations on Compliance in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTotalLocationsWithViolationCardInComplianceBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            //Validate the content on Locations with violation card.
            HashMap<String, Integer> valuesFromLocationsWithViolationCard = compliancePage.getValueOnLocationsWithViolationCardAndVerifyInfo(Region);

            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Extra Hours");
            List<Float> extraHours = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Late Schedule");
            List<String> publishStatus = scheduleDMViewPage.getListByColInTimesheetDMView(index);
            SimpleUtils.assertOnFail("The extra hour count should consistent with publish status count! ",
                    extraHours.size() == publishStatus.size(), false);
            int totalLocationWithViolation = 0;

            for (int i = 0; i < extraHours.size(); i++){
                if (
                        extraHours.get(i) > 0 ||
                                publishStatus.get(i).equals("Yes")){
                    totalLocationWithViolation ++;
                }
            }

            SimpleUtils.assertOnFail("Locations With Violation Card and analytic table are inconsistent!",
                    valuesFromLocationsWithViolationCard.get("UpperFieldsWithViolations") == totalLocationWithViolation, false);
            SimpleUtils.assertOnFail("Locations With Violation Card and analytic table are inconsistent!",
                    valuesFromLocationsWithViolationCard.get("TotalUpperFields") == extraHours.size(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Districts with Violations on Compliance in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTotalLocationsWithViolationCardInComplianceRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            //Validate the content on Locations with violation card.

            HashMap<String, Integer> valuesFromLocationsWithViolationCard = compliancePage.getValueOnLocationsWithViolationCardAndVerifyInfo(District);

            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Extra Hours");
            List<Float> extraHours = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Late Schedule");
            List<String> publishStatus = scheduleDMViewPage.getListByColInTimesheetDMView(index);
            SimpleUtils.assertOnFail("The extra hour count should consistent with publish status count! ",
                    extraHours.size() == publishStatus.size(), false);
            int totalLocationWithViolation = 0;

            for (int i = 0; i < extraHours.size(); i++){
                if (
                        extraHours.get(i) > 0 ||
                        publishStatus.get(i).equals("Yes")){
                    totalLocationWithViolation ++;
                }
            }

            SimpleUtils.assertOnFail("Locations With Violation Card and analytic table are inconsistent!",
                    valuesFromLocationsWithViolationCard.get("UpperFieldsWithViolations") == totalLocationWithViolation, false);
            SimpleUtils.assertOnFail("Locations With Violation Card and analytic table are inconsistent!",
                    valuesFromLocationsWithViolationCard.get("TotalUpperFields") == extraHours.size(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TOP x VIOLATIONS (HRS) on Compliance in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTopViolationsCardInComplianceBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();

            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();

            //Validate the content on Locations with violation card.
            float topViolationInOvertimeCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Overtime"))));
            float topViolationInClopeningCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clopening"))));
            float topViolationInMissedMealCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Missed Meal"))));
            float topViolationInScheduleChangedCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Schedule Changed"))));
            float topViolationInDoubletimeCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Double Time"))));

            if ((topViolationInOvertimeCol+topViolationInClopeningCol+topViolationInMissedMealCol+topViolationInScheduleChangedCol+topViolationInDoubletimeCol) != 0.0){
                HashMap<String, Float> valuesFromLocationsWithViolationCard = compliancePage.getViolationHrsFromTop1ViolationCardAndVerifyInfo();

                if (valuesFromLocationsWithViolationCard.containsKey("Overtime")){
                    SimpleUtils.assertOnFail("Overtime (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Overtime")-topViolationInOvertimeCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Clopening")){
                    SimpleUtils.assertOnFail("Clopening (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Clopening")-topViolationInClopeningCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Missed Meal")){
                    SimpleUtils.assertOnFail("Missed Meal on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Missed Meal")-topViolationInMissedMealCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Schedule Changed")){
                    SimpleUtils.assertOnFail("Schedule Changed on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Schedule Changed")-topViolationInScheduleChangedCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Doubletime")){
                    SimpleUtils.assertOnFail("Doubletime (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Doubletime")-topViolationInDoubletimeCol)==0, false);
                }
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TOP x VIOLATIONS (HRS) on Compliance in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTopViolationsCardInComplianceRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnComplianceConsoleMenu();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();


            //Validate the content on Locations with violation card.
            float topViolationInOvertimeCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Overtime"))));
            float topViolationInClopeningCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clopening"))));
            float topViolationInMissedMealCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Missed Meal"))));
            float topViolationInScheduleChangedCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Schedule Changed"))));
            float topViolationInDoubletimeCol = compliancePage.getTopOneViolationHrsOrNumOfACol(scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Double Time"))));

            if ((topViolationInOvertimeCol+topViolationInClopeningCol+topViolationInMissedMealCol+topViolationInScheduleChangedCol+topViolationInDoubletimeCol) != 0.0){
                HashMap<String, Float> valuesFromLocationsWithViolationCard = compliancePage.getViolationHrsFromTop1ViolationCardAndVerifyInfo();

                if (valuesFromLocationsWithViolationCard.containsKey("Overtime")){
                    SimpleUtils.assertOnFail("Overtime (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Overtime")-topViolationInOvertimeCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Clopening")){
                    SimpleUtils.assertOnFail("Clopening (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Clopening")-topViolationInClopeningCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Missed Meal")){
                    SimpleUtils.assertOnFail("Missed Meal on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Missed Meal")-topViolationInMissedMealCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Schedule Changed")){
                    SimpleUtils.assertOnFail("Schedule Changed on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Schedule Changed")-topViolationInScheduleChangedCol)==0, false);
                }
                if (valuesFromLocationsWithViolationCard.containsKey("Doubletime")){
                    SimpleUtils.assertOnFail("Doubletime (Hrs) on smart cart is not correct!", Math.abs(valuesFromLocationsWithViolationCard.get("Doubletime")-topViolationInDoubletimeCol)==0, false);
                }
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify analytics table on Timesheet in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticTableInTimesheetBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);



            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

            //Validate fields name in analytic table.
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            String field1 = "Region";
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
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(regionName);
            SimpleUtils.assertOnFail("This is not the Timesheet Region view page for past week!",timeSheetPage.isTimeSheetPageLoaded(), false);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            //Validate the data of analytics table for current week.
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(regionName);
            SimpleUtils.assertOnFail("This is not the Timesheet Region view page for current!",timeSheetPage.isTimeSheetPageLoaded(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify analytics table on Timesheet in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticTableInTimesheetRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            String districtName = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);


            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

            //Validate fields name in analytic table.
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            String field1 = "District";
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
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(districtName);
            SimpleUtils.assertOnFail("This is not the Timesheet District view page for past week!",timeSheetPage.isTimeSheetPageLoaded(), false);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Validate the data of analytics table for current week.
            scheduleCommonPage.navigateToNextWeek();
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(districtName);
            SimpleUtils.assertOnFail("This is not the Timesheet District view page for current!",timeSheetPage.isTimeSheetPageLoaded(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the availability of region list and sub region on Timesheet in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRegionListAndSubRegionOnTimesheetInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            // Validate the region list
            List<String> regionNamesOnTimeSheetPage = timeSheetPage.getLocationName();
            SimpleUtils.assertOnFail("The region names fail to load! ",
                    regionNamesOnTimeSheetPage.size()>1
                            && regionNamesOnTimeSheetPage.contains(regionName), false);

            // Validate click one region
            timeSheetPage.clickOnGivenLocation(regionName);

            // Validate go back from selected region in current week
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimesheetDMView(), false);

            // Validate the data for selected region
            List<String> dataFromTimesheetTableOnBUView = timeSheetPage.getDataFromTimesheetTableForGivenLocationInDMView(regionName);

            timeSheetPage.clickOnGivenLocation(regionName);
            int unplannedClockOnSmartCardOnRegionView = timeSheetPage.getUnplannedClockSmartCardOnDMView();
            int totalTimesheetOnSmartCardOnRegionView = timeSheetPage.getTotalTimesheetFromSmartCardOnDMView();

            SimpleUtils.assertOnFail("The unplanned Clocks display inconsistent on BU and Region view! ",
                    dataFromTimesheetTableOnBUView.get(1).equalsIgnoreCase(String.valueOf(unplannedClockOnSmartCardOnRegionView)),
                    false);

            SimpleUtils.assertOnFail("The Total Timesheets display inconsistent on BU and Region view! ",
                    dataFromTimesheetTableOnBUView.get(2).equalsIgnoreCase(String.valueOf(totalTimesheetOnSmartCardOnRegionView)),
                    false);

            // Validate click given region and given week
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            timeSheetPage.navigateToPreviousWeek();
            String weekInfo = timeSheetPage.getActiveDayWeekOrPayPeriod();
            timeSheetPage.clickOnGivenLocation(regionName);
            SimpleUtils.assertOnFail("It didn't navigate to the Timesheet page of the region in that week", weekInfo.equals(timeSheetPage.getActiveDayWeekOrPayPeriod()), false);

            // Validate click other BU in past week
            String[] upperFields2 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            String anotherBUName = upperFields2[1];
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, anotherBUName.trim());
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimesheetDMView(), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the availability of district list and sub district on Timesheet in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRegionListAndSubRegionOnTimesheetInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            String districtName = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            // Validate the district list
            List<String> districtNamesOnTimeSheetPage = timeSheetPage.getLocationName();
            SimpleUtils.assertOnFail("The district names fail to load! ",
                    districtNamesOnTimeSheetPage.size()>1
                            && districtNamesOnTimeSheetPage.contains(districtName), false);

            // Validate click one district
            timeSheetPage.clickOnGivenLocation(districtName);

            // Validate go back from selected district in current week
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimesheetDMView(), false);

            // Validate the data for selected region
            List<String> dataFromTimesheetTableOnBUView = timeSheetPage.getDataFromTimesheetTableForGivenLocationInDMView(districtName);

            timeSheetPage.clickOnGivenLocation(districtName);
            int unplannedClockOnSmartCardOnRegionView = timeSheetPage.getUnplannedClockSmartCardOnDMView();
            int totalTimesheetOnSmartCardOnRegionView = timeSheetPage.getTotalTimesheetFromSmartCardOnDMView();

            SimpleUtils.assertOnFail("The unplanned Clocks display inconsistent on BU and Region view! ",
                    dataFromTimesheetTableOnBUView.get(1).equalsIgnoreCase(String.valueOf(unplannedClockOnSmartCardOnRegionView)),
                    false);

            SimpleUtils.assertOnFail("The Total Timesheets display inconsistent on BU and Region view! ",
                    dataFromTimesheetTableOnBUView.get(2).equalsIgnoreCase(String.valueOf(totalTimesheetOnSmartCardOnRegionView)),
                    false);

            // Validate click given region and given week
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            timeSheetPage.navigateToPreviousWeek();
            String weekInfo = timeSheetPage.getActiveDayWeekOrPayPeriod();
            timeSheetPage.clickOnGivenLocation(districtName);
            SimpleUtils.assertOnFail("It didn't navigate to the Timesheet page of the district in that week",
                    weekInfo.equals(timeSheetPage.getActiveDayWeekOrPayPeriod()), false);

            // Validate click other BU in past week
            String[] upperFields2 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            String anotherRegionName = upperFields2[2];

            locationSelectorPage.changeUpperFieldDirect(Region, anotherRegionName.trim());
            SimpleUtils.assertOnFail("Timesheet page not loaded successfully", timeSheetPage.isTimesheetDMView(), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Timesheet functionality on Timesheet in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimesheetFunctionalityInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            Thread.sleep(5000);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            //Validate the title
            timeSheetPage.isTimeSheetPageLoaded();
            locationSelectorPage.verifyTheDisplayBUWithSelectedBUConsistent(buName);
            locationSelectorPage.isRegionSelected("All Regions");
            List<String> regionInBU1 = timeSheetPage.getLocationName();

            //Validate changing BUs on Timesheet
            locationSelectorPage.changeAnotherBUInBUView();
            String anotherBUName = locationSelectorPage.getSelectedUpperFields().get(BusinessUnit);
            locationSelectorPage.verifyTheDisplayBUWithSelectedBUConsistent(anotherBUName);
            List<String> regionInBU2 =  timeSheetPage.getLocationName();
            SimpleUtils.assertOnFail("TimeSheet BU view page fail to update!",
                    (regionInBU2.size() == 0) || (regionInBU2.size() != 0 && !regionInBU1.containsAll(regionInBU2)), false);

            //Validate the click ability of backward button.
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToPreviousWeek();

            //Validate the click ability of forward button.
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

            //Validate search function.
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            Map<String, String> regionInfo = compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(regionName);
            SimpleUtils.assertOnFail("Region cannot be searched out on timesheet BU view page! ", regionInfo.size()!=0, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Timesheet functionality on Timesheet in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimesheetFunctionalityInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            Thread.sleep(5000);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            String districtName = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            //Validate the title
            timeSheetPage.isTimeSheetPageLoaded();
            locationSelectorPage.verifyTheDisplayRegionWithSelectedRegionConsistent(regionName);
            locationSelectorPage.isRegionSelected("All Districts");
            List<String> districtInRegion1 = timeSheetPage.getLocationName();

            //Validate changing Regions on Timesheet
            locationSelectorPage.changeAnotherRegionInRegionView();
            String anotherRegionName = locationSelectorPage.getSelectedUpperFields().get(Region);
            locationSelectorPage.verifyTheDisplayRegionWithSelectedRegionConsistent(anotherRegionName);
            List<String> districtInRegion2 =  timeSheetPage.getLocationName();
            SimpleUtils.assertOnFail("TimeSheet Region view page fail to update!",
                    !districtInRegion1.containsAll(districtInRegion2), false);

            //Validate the click ability of backward button.
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            String weekInfo = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.navigateToPreviousWeek();

            //Validate the click ability of forward button.
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Week picker has issue!", weekInfo.equals(scheduleCommonPage.getActiveWeekText()), false);

            //Validate search function.
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            Map<String, String> districtInfo = compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(districtName);
            SimpleUtils.assertOnFail("District cannot be searched out on timesheet BU view page! ", districtInfo.size()!=0, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Refresh feature on Timesheet in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnTimesheetInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            // Validate the presence of Refresh button
            scheduleDMViewPage.validateThePresenceOfRefreshButton();

            // Validate Refresh timestamp
            scheduleDMViewPage.validateRefreshTimestamp();

            // Validate Refresh when navigation back
            dashboardPage.validateRefreshWhenNavigationBack("Timesheet");

            // Validate Refresh function
            scheduleDMViewPage.validateRefreshFunction();

            // Validate Refresh performance
            scheduleDMViewPage.validateRefreshPerformance();

            // Validate Refresh reflects timesheet change
            String rateWithin24OnSmartCardBeforeChange = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard().get(0);
            String rateInAnalyticsTableBeforeChange = timeSheetPage.getTimesheetApprovalForGivenLocationInDMView(regionName);
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
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
            timeSheetPage.clickOnWeekView();
            timeSheetPage.approveAnyTimesheet();
            String rateInTimesheetDueAfterApprove = timeSheetPage.getApprovalRateFromTIMESHEETDUESmartCard();
            dashboardPage.navigateToDashboard();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
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
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Refresh feature on Timesheet in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRefreshFeatureOnTimesheetInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            // Validate the presence of Refresh button
            scheduleDMViewPage.validateThePresenceOfRefreshButton();

            // Validate Refresh timestamp
            scheduleDMViewPage.validateRefreshTimestamp();

            // Validate Refresh when navigation back
            dashboardPage.validateRefreshWhenNavigationBack("Timesheet");

            // Validate Refresh function
            scheduleDMViewPage.validateRefreshFunction();

            // Validate Refresh performance
            scheduleDMViewPage.validateRefreshPerformance();

            // Validate Refresh reflects timesheet change
            String rateWithin24OnSmartCardBeforeChange = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard().get(0);
            String rateInAnalyticsTableBeforeChange = timeSheetPage.getTimesheetApprovalForGivenLocationInDMView(regionName);
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
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
            timeSheetPage.clickOnWeekView();
            timeSheetPage.approveAnyTimesheet();
            String rateInTimesheetDueAfterApprove = timeSheetPage.getApprovalRateFromTIMESHEETDUESmartCard();
            dashboardPage.navigateToDashboard();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
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
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TIMESHEET APPROVAL RATE on Timesheet in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTIMESHEETAPPROVALRATEOnTimesheetInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            timeSheetPage.validateLoadingOfTimeSheetSmartCard();

            //Get time sheet rate from smart card

            List<String> timesheetApprovalRateFromSmartCard = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard();

            //Get total approve rate
            float totalApprovalRateFromSmartCard = 0;
            for (int i = 0; i< timesheetApprovalRateFromSmartCard.size(); i++){
                totalApprovalRateFromSmartCard += Float.parseFloat(timesheetApprovalRateFromSmartCard.get(i).replace("%", ""));
            }
            SimpleUtils.assertOnFail("The total approval rate on TIMESHEET APPROVAL RATE smart card should be 100% ! ",
                    totalApprovalRateFromSmartCard == 100, false);

            float onlyApprovalRate = 0;
            for (int i = 0; i< timesheetApprovalRateFromSmartCard.size()-1; i++){
                onlyApprovalRate += Float.parseFloat(timesheetApprovalRateFromSmartCard.get(i).replace("%", ""));
            }
            //Get time sheet rate from table
            List<String> timesheetApprovalRate = scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Timesheet Approval"));
            float approvalRateOnTable = 0;
            for (int i = 0; i< timesheetApprovalRate.size()-1; i++){
                approvalRateOnTable += Float.parseFloat(timesheetApprovalRate.get(i).replace("%", ""));
            }
            //Check the approval rate on TIMESHEET APPROVAL RATE smart card and Timesheet analytics table
            SimpleUtils.assertOnFail("The approval rates are inconsistent on TIMESHEET APPROVAL RATE smart card and Timesheet analytics table! ",
                    onlyApprovalRate == approvalRateOnTable, false);

            //Get one region approval rate on Timesheet analytics table
            float timeSheetApprovalOfOneRegion = Float.parseFloat(timeSheetPage.
                    getDataFromTimesheetTableForGivenLocationInDMView(regionName).get(2).replace("%",""));
            timeSheetPage.clickOnGivenLocation(regionName);

            //Get time sheet rate from table
            List<String> timesheetApprovalRateOnRegionView = scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Timesheet Approval"));
            float approvalRateOnTableOnRegionView = 0;
            for (int i = 0; i< timesheetApprovalRate.size()-1; i++){
                approvalRateOnTableOnRegionView += Float.parseFloat(timesheetApprovalRateOnRegionView.get(i).replace("%", ""));
            }

            //Check the region approval rates are consistent on BU view and Region view
            SimpleUtils.assertOnFail("The region approval rates are inconsistent on BU view and Region view, the rate on BU view is "+ timeSheetApprovalOfOneRegion +
                            ", the rate on review is "+ approvalRateOnTableOnRegionView,
                    timeSheetApprovalOfOneRegion == approvalRateOnTableOnRegionView, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TIMESHEET APPROVAL RATE on Timesheet in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTIMESHEETAPPROVALRATEOnTimesheetInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            String districtName = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            //Get time sheet rate from smart card

            List<String> timesheetApprovalRateFromSmartCard = timeSheetPage.getTimesheetApprovalRateOnDMViewSmartCard();

            //Get total approve rate
            float totalApprovalRateFromSmartCard = 0;
            for (int i = 0; i< timesheetApprovalRateFromSmartCard.size(); i++){
                totalApprovalRateFromSmartCard += Float.parseFloat(timesheetApprovalRateFromSmartCard.get(i).replace("%", ""));
            }
            SimpleUtils.assertOnFail("The total approval rate on TIMESHEET APPROVAL RATE smart card should be 100% ! ",
                    totalApprovalRateFromSmartCard == 100, false);

            float onlyApprovalRate = 0;
            for (int i = 0; i< timesheetApprovalRateFromSmartCard.size()-1; i++){
                onlyApprovalRate += Float.parseFloat(timesheetApprovalRateFromSmartCard.get(i).replace("%", ""));
            }
            //Get time sheet rate from table
            List<String> timesheetApprovalRate = scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Timesheet Approval"));
            float approvalRateOnTable = 0;
            for (int i = 0; i< timesheetApprovalRate.size()-1; i++){
                approvalRateOnTable += Float.parseFloat(timesheetApprovalRate.get(i).replace("%", ""));
            }
            //Check the approval rate on TIMESHEET APPROVAL RATE smart card and Timesheet analytics table
            SimpleUtils.assertOnFail("The approval rates are inconsistent on TIMESHEET APPROVAL RATE smart card and Timesheet analytics table! ",
                    onlyApprovalRate == approvalRateOnTable, false);

            //Get one region approval rate on Timesheet analytics table
            float timeSheetApprovalOfOneRegion = Float.parseFloat(timeSheetPage.
                    getDataFromTimesheetTableForGivenLocationInDMView(districtName).get(2).replace("%",""));
            timeSheetPage.clickOnGivenLocation(districtName);

            //Get time sheet rate from table
            List<String> timesheetApprovalRateOnRegionView = scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Timesheet Approval"));
            float approvalRateOnTableOnRegionView = 0;
            for (int i = 0; i< timesheetApprovalRate.size()-1; i++){
                approvalRateOnTableOnRegionView += Float.parseFloat(timesheetApprovalRateOnRegionView.get(i).replace("%", ""));
            }

            //Check the region approval rates are consistent on BU view and Region view
            SimpleUtils.assertOnFail("The region approval rates are inconsistent on BU view and Region view, the rate on BU view is "+ timeSheetApprovalOfOneRegion +
                            ", the rate on review is "+ approvalRateOnTableOnRegionView,
                    timeSheetApprovalOfOneRegion == approvalRateOnTableOnRegionView, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Unplanned Clocks on Timesheet in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUnplannedClocksOnTimesheetInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            timeSheetPage.validateLoadingOfTimeSheetSmartCard();

            //Get Unplanned Clocks and Total Timesheets from smart card
            int unplannedClockFromSmartCard = timeSheetPage.getUnplannedClockSmartCardOnDMView();
            int timesheetFromSmartCard = timeSheetPage.getTotalTimesheetFromSmartCardOnDMView();

            //Get Unplanned Clocks and Total Timesheets from table
            int totalUnplannedClocksOnTblView = timeSheetPage.getUnplannedClocksOnDMView();
            int totalTimesheetsOnTblView = timeSheetPage.getTotalTimesheetsOnDMView();

            int totalUnplannedClocksOnDMViewSmartCardDetailSummary = timeSheetPage.getUnplannedClocksDetailSummaryValue();
            verifyUnplannedClockOnDMView(unplannedClockFromSmartCard, totalUnplannedClocksOnDMViewSmartCardDetailSummary,
                    totalUnplannedClocksOnTblView);
            verifyTimesheetOnDMView(timesheetFromSmartCard, totalTimesheetsOnTblView);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    public void verifyUnplannedClockOnDMView(int totalUnplannedClockSmartCardValOnDMView,
                                             int totalUnplannedClocksOnDMViewSmartCardDetailSummary,
                                             int totalUnplannedClocksOnTblView){
        if(totalUnplannedClockSmartCardValOnDMView == totalUnplannedClocksOnDMViewSmartCardDetailSummary){
            SimpleUtils.pass("Unplanned Clock from Smart Card " + totalUnplannedClockSmartCardValOnDMView + " matches " +
                    "with Unplanned Clock in Details Summary Card " + totalUnplannedClocksOnDMViewSmartCardDetailSummary + " on DM View");
        }else{
            SimpleUtils.fail("Unplanned Clock from Smart Card " + totalUnplannedClockSmartCardValOnDMView + " do not match " +
                    "with Unplanned Clock in Details Summary Card " + totalUnplannedClocksOnDMViewSmartCardDetailSummary + " on DM View",true);
        }
        if(totalUnplannedClockSmartCardValOnDMView == totalUnplannedClocksOnTblView){
            SimpleUtils.pass("Unplanned Clock from Smart Card " + totalUnplannedClockSmartCardValOnDMView + " matches " +
                    "with sum of Unplanned Clock per location in Timesheet table " + totalUnplannedClocksOnTblView + " on DM View");
        }else{
            SimpleUtils.fail("Unplanned Clock from Smart Card " + totalUnplannedClockSmartCardValOnDMView + " do not match " +
                    "with sum of Unplanned Clock per location in Timesheet table " + totalUnplannedClocksOnTblView + " on DM View",true);
        }
    }

    public void verifyTimesheetOnDMView(int totalTimesheetOnDMViewSmartCard,
                                        int totalTimesheetsOnTblView){
        if(totalTimesheetOnDMViewSmartCard == totalTimesheetsOnTblView){
            SimpleUtils.pass("Total Timesheet Count from Smart Card " + totalTimesheetOnDMViewSmartCard + " matches " +
                    "with sum of Timesheet entries per location in Timesheet table " + totalTimesheetsOnTblView + " on DM View");
        }else{
            SimpleUtils.fail("Total Timesheet Count from Smart Card " + totalTimesheetOnDMViewSmartCard + " do not match " +
                    "with sum of Timesheet entries per location in Timesheet table  " + totalTimesheetsOnTblView + " on DM View",true);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Unplanned Clocks on Timesheet in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUnplannedClocksOnTimesheetInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            String districtName = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            //Get Unplanned Clocks and Total Timesheets from smart card
            int unplannedClockFromSmartCard = timeSheetPage.getUnplannedClockSmartCardOnDMView();
            int timesheetFromSmartCard = timeSheetPage.getTotalTimesheetFromSmartCardOnDMView();

            //Get Unplanned Clocks and Total Timesheets from table
            int totalUnplannedClocksOnTblView = timeSheetPage.getUnplannedClocksOnDMView();
            int totalTimesheetsOnTblView = timeSheetPage.getTotalTimesheetsOnDMView();

            int totalUnplannedClocksOnDMViewSmartCardDetailSummary = timeSheetPage.getUnplannedClocksDetailSummaryValue();
            verifyUnplannedClockOnDMView(unplannedClockFromSmartCard, totalUnplannedClocksOnDMViewSmartCardDetailSummary,
                    totalUnplannedClocksOnTblView);
            verifyTimesheetOnDMView(timesheetFromSmartCard, totalTimesheetsOnTblView);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify UNPLANNED CLOCKS smart card on Timesheet in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUnplannedClocksSmartCardOnTimesheetInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            Thread.sleep(5000);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            timeSheetPage.validateLoadingOfTimeSheetSmartCard();

            //Validate the content on Unplanned Clocks summary smart card.

            timeSheetPage.clickOnTimeSheetConsoleMenu();
            HashMap<String, Integer> valuesFromUnplannedClocksSummaryCard = scheduleDMViewPage.getValueOnUnplannedClocksSmartCardAndVerifyInfo();
            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Unplanned Clocks");
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            int unplannedClocks = 0;
            for (Float f: data){
                unplannedClocks = unplannedClocks + Math.round(f);
            }
            //The sum of numbers on UNPLANNED CLOCKS smart card should match the unplanned clocks smartcard
            SimpleUtils.assertOnFail("Unplanned clocks from summary card and analytic table are inconsistent!",
                    valuesFromUnplannedClocksSummaryCard.get("No Show") +
                            valuesFromUnplannedClocksSummaryCard.get("Early Clocks") +
                            valuesFromUnplannedClocksSummaryCard.get("Late Clocks") +
                            valuesFromUnplannedClocksSummaryCard.get("Incomplete Clocks") +
                            valuesFromUnplannedClocksSummaryCard.get("Missed Meal") +
                            valuesFromUnplannedClocksSummaryCard.get("Unscheduled")==unplannedClocks, false);

            timeSheetPage.clickOnComplianceConsoleMenu();
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Missed Meal");
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            int missedMeal = 0;
            for (Float f: data){
                missedMeal = missedMeal + Math.round(f);
            }
            //Missed meal number on UNPLANNED CLOCKS smart card should match the sum of the missed meal column in the Compliance tab
            SimpleUtils.assertOnFail("Miss meal from UNPLANNED CLOCKS smart card and compliance analytic table are inconsistent!",
                    valuesFromUnplannedClocksSummaryCard.get("Missed Meal")==missedMeal, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify UNPLANNED CLOCKS smart card on Timesheet in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUnplannedClocksSmartCardOnTimesheetInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            String districtName = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();

            //Validate the content on Unplanned Clocks summary smart card.

            timeSheetPage.clickOnTimeSheetConsoleMenu();
            HashMap<String, Integer> valuesFromUnplannedClocksSummaryCard = scheduleDMViewPage.getValueOnUnplannedClocksSmartCardAndVerifyInfo();
            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Unplanned Clocks");
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            int unplannedClocks = 0;
            for (Float f: data){
                unplannedClocks = unplannedClocks + Math.round(f);
            }
            //The sum of numbers on UNPLANNED CLOCKS smart card should match the unplanned clocks smartcard
            SimpleUtils.assertOnFail("Unplanned clocks from summary card and analytic table are inconsistent!",
                    valuesFromUnplannedClocksSummaryCard.get("No Show") +
                            valuesFromUnplannedClocksSummaryCard.get("Early Clocks") +
                            valuesFromUnplannedClocksSummaryCard.get("Late Clocks") +
                            valuesFromUnplannedClocksSummaryCard.get("Incomplete Clocks") +
                            valuesFromUnplannedClocksSummaryCard.get("Missed Meal") +
                            valuesFromUnplannedClocksSummaryCard.get("Unscheduled")==unplannedClocks, false);

            timeSheetPage.clickOnComplianceConsoleMenu();
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Missed Meal");
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            int missedMeal = 0;
            for (Float f: data){
                missedMeal = missedMeal + Math.round(f);
            }
            //Missed meal number on UNPLANNED CLOCKS smart card should match the sum of the missed meal column in the Compliance tab
            SimpleUtils.assertOnFail("Miss meal from UNPLANNED CLOCKS smart card and compliance analytic table are inconsistent!",
                    valuesFromUnplannedClocksSummaryCard.get("Missed Meal")==missedMeal, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate analytics table on Schedule in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnScheduleInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            String field1 = "Region";
            String field2 = "Published Status";
            String field3 = "Budget Hrs";
            String field4 = "Published Hrs";
            String field5 = "Budget Variance";
            SimpleUtils.assertOnFail(field1 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field1) > 0, false);
            SimpleUtils.assertOnFail(field2 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field2) > 0, false);
            SimpleUtils.assertOnFail(field3 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs") > 0
                    || scheduleDMViewPage.getIndexOfColInDMViewTable("Guidance Hrs") > 0 , false);
            SimpleUtils.assertOnFail(field4 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field4) > 0, false);
            SimpleUtils.assertOnFail(field5 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field5) > 0, false);

            //Validate the field columns can be ordered.
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(2);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(4);

            //Validate the data of analytics table for current week.
            verifyAnalyticsTableOnBUView(regionName);

            //Validate the data of analytics table for past week.
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            scheduleCommonPage.navigateToPreviousWeek();
            verifyAnalyticsTableOnBUView(regionName);

            //Validate the data of analytics table for future week.
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            verifyAnalyticsTableOnBUView(regionName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    private void verifyAnalyticsTableOnBUView(String regionName) throws Exception {

        ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
        scheduleDMViewPage.clickOnRefreshButton();
        Map<String, String> regionInfoOnBUView = scheduleDMViewPage.getAllScheduleInfoFromScheduleInDMViewByLocation(regionName);
        scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(regionName);
        scheduleDMViewPage.clickOnRefreshButton();
        //Check publish status on BU and region view
        int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Published Status");
        List<String> publishedStatus = scheduleDMViewPage.getListByColInTimesheetDMView(index);
        if (publishedStatus.contains("Not Started")) {
            SimpleUtils.assertOnFail("The region published status display inconsistent on BU and Region reivew",
                    regionInfoOnBUView.get("publishedStatus").equalsIgnoreCase("Not Started"), false);
        } else if (publishedStatus.contains("In Progress")) {
            SimpleUtils.assertOnFail("The region published status display inconsistent on BU and Region reivew",
                    regionInfoOnBUView.get("publishedStatus").equalsIgnoreCase("In Progress"), false);
        } else {
            SimpleUtils.assertOnFail("The region published status display inconsistent on BU and Region reivew",
                    regionInfoOnBUView.get("publishedStatus").equalsIgnoreCase("Published"), false);
        }

        //Check budget hrs on BU and region view
        if (scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs") > 0)
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
        else
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Guidance Hrs");

        List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
        float budgetHrsOnReviewView = 0;
        for (float f: data){
            budgetHrsOnReviewView += f;
        }
        SimpleUtils.assertOnFail("The Budget hrs display inconsistent on BU and Region reivew! It is "+ Float.parseFloat(regionInfoOnBUView.get("budgetedHours"))
                        + " on BU view, and is "+ budgetHrsOnReviewView + "on Region view",
                Float.parseFloat(regionInfoOnBUView.get("budgetedHours")) == budgetHrsOnReviewView, false);

        //Check published hrs on BU and region view
        index = scheduleDMViewPage.getIndexOfColInDMViewTable("Published Hrs");
        data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
        float publishedHrsOnReviewView = 0;
        for (Float f: data){
            publishedHrsOnReviewView += f;
        }
        SimpleUtils.assertOnFail("The Published hrs display inconsistent on BU and Region reivew! It is "+ Float.parseFloat(regionInfoOnBUView.get("publishedHours"))
                        + " on BU view, and is "+ publishedHrsOnReviewView + "on Region view",
                Float.parseFloat(regionInfoOnBUView.get("publishedHours")) == (float)(Math.round(publishedHrsOnReviewView*100))/100, false);

        //Check Budget Variance on BU and region view
        index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Variance");
        data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
        float budgetVarianceOnReviewView = 0;
        for (Float f: data){
            budgetVarianceOnReviewView =+ f;
        }
//        SimpleUtils.assertOnFail("The Budget Variance display inconsistent on BU and Region reivew! It is "+ Float.parseFloat(regionInfoOnBUView.get("budgetVariance"))
//                        + " on BU view, and is "+ budgetVarianceOnReviewView + "on Region view",        // Blocked by https://legiontech.atlassian.net/browse/SCH-5185
//                Float.parseFloat(regionInfoOnBUView.get("budgetVariance")) == budgetVarianceOnReviewView, false);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate analytics table on Schedule in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnScheduleInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get(District);
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            String field1 = "District";
            String field2 = "Published Status";
            String field3 = "Budget Hrs";
            String field4 = "Published Hrs";
            String field5 = "Budget Variance";
            SimpleUtils.assertOnFail(field1 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field1) > 0, false);
            SimpleUtils.assertOnFail(field2 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field2) > 0, false);
            SimpleUtils.assertOnFail(field3 + " field doesn't show up!",
                    scheduleDMViewPage.getIndexOfColInDMViewTable(field3) > 0 ||
                            scheduleDMViewPage.getIndexOfColInDMViewTable("Guidance Hrs") > 0, false);
            SimpleUtils.assertOnFail(field4 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field4) > 0, false);
            SimpleUtils.assertOnFail(field5 + " field doesn't show up!", scheduleDMViewPage.getIndexOfColInDMViewTable(field5) > 0, false);

            //Validate the field columns can be ordered.
            scheduleDMViewPage.verifySortByColForLocationsInDMView(1);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(2);
//            scheduleDMViewPage.verifySortByColForLocationsInDMView(3);
            scheduleDMViewPage.verifySortByColForLocationsInDMView(4);

            //Validate the data of analytics table for current week.
            verifyAnalyticsTableOnBUView(districtName);

            //Validate the data of analytics table for past week.
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            scheduleCommonPage.navigateToPreviousWeek();
            verifyAnalyticsTableOnBUView(districtName);

            //Validate the data of analytics table for future week.
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(5000);
            verifyAnalyticsTableOnBUView(districtName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify REGION SUMMARY on Schedule in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRegionSummaryOnScheduleInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();

            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            controlsNewUIPage.clickOnGlobalLocationButton();
            controlsNewUIPage.updateApplyLaborBudgetToSchedules("Yes");
            dashboardPage.clickOnDashboardConsoleMenu();

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            //Go to the Schedule page in BU view.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule BU view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            //Validate the content of REGION SUMMARY smart card for current/future weeks.
            HashMap<String, Float> valuesFromRegionSummaryCard =  scheduleDMViewPage.getValuesAndVerifyInfoForLocationSummaryInDMView(Region, "current");

            //Validate the data REGION SUMMARY smart card for current/future weeks.
            SimpleUtils.assertOnFail("Region counts in title are inconsistent!",
                    Math.round(valuesFromRegionSummaryCard.get("NumOfLocations")) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            SimpleUtils.assertOnFail("Region counts from projected info are inconsistent!",
                    (Math.round(valuesFromRegionSummaryCard.get("NumOfProjectedWithin")) +
                            Math.round(valuesFromRegionSummaryCard.get("NumOfProjectedOver"))) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            //verify budgeted hours.
            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            if ( index == 0)
                index = scheduleDMViewPage.getIndexOfColInDMViewTable("Guidance Hrs");
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            float budgetedHrsFromTable = 0;
            for (Float f: data){
                budgetedHrsFromTable = budgetedHrsFromTable + f;
            }
            float budgetHourOnSummaryCard = 0;
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            if (index == 0)
                budgetHourOnSummaryCard = valuesFromRegionSummaryCard.get("Guidance Hrs");
            else
                budgetHourOnSummaryCard = valuesFromRegionSummaryCard.get("Budgeted Hrs");
            SimpleUtils.assertOnFail("Budgeted hours are inconsistent!",
                    (Math.abs(budgetHourOnSummaryCard) - budgetedHrsFromTable) == 0, false);
            //verify scheduled hours
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Published Hrs")));
            float scheduledHrsFromTable = 0;
            for (Float f: data){
                scheduledHrsFromTable = scheduledHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Published hours are inconsistent!",
                    (Math.abs(valuesFromRegionSummaryCard.get("Scheduled Hrs")) - scheduledHrsFromTable) == 0, false);

            //Verify difference value between budgeted and projected.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clocked Hrs")));
            float projectedHours = 0;
            for (Float f: data){
                projectedHours = projectedHours + f;
            }
            if ((budgetHourOnSummaryCard- projectedHours)>0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromRegionSummaryCard.get("▼")) - (float)(Math.round((budgetHourOnSummaryCard - projectedHours)*100))/100) == 0, false);
            } else if ((budgetHourOnSummaryCard - projectedHours)<0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromRegionSummaryCard.get("▲")) + (float)(Math.round((budgetHourOnSummaryCard - projectedHours)*100))/100) == 0, false);
            }

            //Verify current week Projected Hours displays.
            scheduleDMViewPage.verifyClockedOrProjectedInDMViewTable("Clocked Hrs");

            //Navigate to the past week to verify the info and data.
            scheduleCommonPage.navigateToPreviousWeek();
            valuesFromRegionSummaryCard =  scheduleDMViewPage.getValuesAndVerifyInfoForLocationSummaryInDMView(Region, "past");

            //Validate the data REGION SUMMARY smart card for the past weeks.
            SimpleUtils.assertOnFail("Region counts in title are inconsistent!",
                    Math.round(valuesFromRegionSummaryCard.get("NumOfLocations")) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            SimpleUtils.assertOnFail("Region counts from projected info are inconsistent!",
                    (Math.round(valuesFromRegionSummaryCard.get("NumOfProjectedWithin")) +
                            Math.round(valuesFromRegionSummaryCard.get("NumOfProjectedOver"))) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            //verify budgeted hours.
            if (scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs") > 0)
                index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            else
                index = scheduleDMViewPage.getIndexOfColInDMViewTable("Guidance Hrs");
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            budgetedHrsFromTable = 0;
            for (Float f: data){
                budgetedHrsFromTable = budgetedHrsFromTable + f;
            }
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            if (index == 0)
                budgetHourOnSummaryCard = valuesFromRegionSummaryCard.get("Guidance Hrs");
            else
                budgetHourOnSummaryCard = valuesFromRegionSummaryCard.get("Budgeted Hrs");
            SimpleUtils.assertOnFail("Budgeted hours are inconsistent! The budget hours on summary card is : "+ budgetHourOnSummaryCard
                            + " The budget hours on the table is: "+ budgetedHrsFromTable,
                    (Math.abs(budgetHourOnSummaryCard) - budgetedHrsFromTable) == 0, false);
            //verify scheduled hours.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Published Hrs")));
            scheduledHrsFromTable = 0;
            for (Float f: data){
                scheduledHrsFromTable = scheduledHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Published hours are inconsistent!",
                    (Math.abs(valuesFromRegionSummaryCard.get("Published Hrs")) - scheduledHrsFromTable) == 0, false);
            //Verify difference value between budgeted and projected.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clocked Hrs")));
            projectedHours = 0;
            for (Float f: data){
                projectedHours = projectedHours + f;
            }
            if ((budgetHourOnSummaryCard - projectedHours)>0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromRegionSummaryCard.get("▼")) - (budgetHourOnSummaryCard - projectedHours)) == 0, false);
            } else {
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromRegionSummaryCard.get("▲")) + (budgetHourOnSummaryCard - projectedHours)) == 0, false);

            }
            //Verify past week Clocked Hours displays.
            scheduleDMViewPage.verifyClockedOrProjectedInDMViewTable("Clocked Hrs");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify DISTRICT SUMMARY on Schedule in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRegionSummaryOnScheduleInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            //Go to the Schedule page in Region view.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule BU view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            //Validate the content of District SUMMARY smart card for current/future weeks.
            HashMap<String, Float> valuesFromDistrictSummaryCard =  scheduleDMViewPage.getValuesAndVerifyInfoForLocationSummaryInDMView(District, "current");

            //Validate the data DISTRICT SUMMARY smart card for current/future weeks.
            SimpleUtils.assertOnFail("District counts in title are inconsistent!",
                    Math.round(valuesFromDistrictSummaryCard.get("NumOfLocations")) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            SimpleUtils.assertOnFail("District counts from projected info are inconsistent!",
                    (Math.round(valuesFromDistrictSummaryCard.get("NumOfProjectedWithin")) +
                            Math.round(valuesFromDistrictSummaryCard.get("NumOfProjectedOver"))) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            //verify budgeted hours.
            int index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            if ( index == 0)
                index = scheduleDMViewPage.getIndexOfColInDMViewTable("Guidance Hrs");
            List<Float> data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            float budgetedHrsFromTable = 0;
            for (Float f: data){
                budgetedHrsFromTable = budgetedHrsFromTable + f;
            }
            float budgetHourOnSummaryCard = 0;
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            if (index == 0)
                budgetHourOnSummaryCard = valuesFromDistrictSummaryCard.get("Guidance Hrs");
            else
                budgetHourOnSummaryCard = valuesFromDistrictSummaryCard.get("Budgeted Hrs");
            SimpleUtils.assertOnFail("Budgeted hours are inconsistent!",
                    (Math.abs(budgetHourOnSummaryCard) - budgetedHrsFromTable) == 0, false);
            //verify scheduled hours
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Published Hrs")));
            float scheduledHrsFromTable = 0;
            for (Float f: data){
                scheduledHrsFromTable = scheduledHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Published hours are inconsistent!",
                    (Math.abs(valuesFromDistrictSummaryCard.get("Scheduled Hrs")) - scheduledHrsFromTable) == 0, false);

            //Verify difference value between budgeted and projected.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clocked Hrs")));
            float projectedHours = 0;
            for (Float f: data){
                projectedHours = projectedHours + f;
            }
            if ((budgetHourOnSummaryCard - projectedHours)>0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromDistrictSummaryCard.get("▼")) - (float)(Math.round((budgetHourOnSummaryCard - projectedHours)*100))/100) == 0, false);
            }
            if ((budgetHourOnSummaryCard - projectedHours)<0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromDistrictSummaryCard.get("▲")) + (float)(Math.round((budgetHourOnSummaryCard - projectedHours)*100))/100) == 0, false);
            }

            //Verify current week Projected Hours displays.
            scheduleDMViewPage.verifyClockedOrProjectedInDMViewTable("Clocked Hrs");

            //Navigate to the past week to verify the info and data.
            scheduleCommonPage.navigateToPreviousWeek();
            valuesFromDistrictSummaryCard =  scheduleDMViewPage.getValuesAndVerifyInfoForLocationSummaryInDMView(District, "past");

            //Validate the data DISTRICT SUMMARY smart card for the past weeks.
            SimpleUtils.assertOnFail("District counts in title are inconsistent!",
                    Math.round(valuesFromDistrictSummaryCard.get("NumOfLocations")) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            SimpleUtils.assertOnFail("District counts from projected info are inconsistent!",
                    (Math.round(valuesFromDistrictSummaryCard.get("NumOfProjectedWithin")) +
                            Math.round(valuesFromDistrictSummaryCard.get("NumOfProjectedOver"))) == scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().size(), false);
            //verify budgeted hours.
            if (scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs") > 0)
                index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            else
                index = scheduleDMViewPage.getIndexOfColInDMViewTable("Guidance Hrs");
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(index));
            budgetedHrsFromTable = 0;
            for (Float f: data){
                budgetedHrsFromTable = budgetedHrsFromTable + f;
            }
            index = scheduleDMViewPage.getIndexOfColInDMViewTable("Budget Hrs");
            if (index == 0)
                budgetHourOnSummaryCard = valuesFromDistrictSummaryCard.get("Guidance Hrs");
            else
                budgetHourOnSummaryCard = valuesFromDistrictSummaryCard.get("Budgeted Hrs");
            SimpleUtils.assertOnFail("Budgeted hours are inconsistent! The budget hours on summary card is : "+ budgetHourOnSummaryCard
                            + " The budget hours on the table is: "+ budgetedHrsFromTable,
                    (Math.abs(budgetHourOnSummaryCard) - budgetedHrsFromTable) == 0, false);
            //verify scheduled hours.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Published Hrs")));
            scheduledHrsFromTable = 0;
            for (Float f: data){
                scheduledHrsFromTable = scheduledHrsFromTable + f;
            }
            SimpleUtils.assertOnFail("Published hours are inconsistent!",
                    (Math.abs(valuesFromDistrictSummaryCard.get("Published Hrs")) - scheduledHrsFromTable) == 0, false);
            //Verify difference value between budgeted and projected.
            data = scheduleDMViewPage.transferStringToFloat(scheduleDMViewPage.getListByColInTimesheetDMView(scheduleDMViewPage.getIndexOfColInDMViewTable("Clocked Hrs")));
            projectedHours = 0;
            for (Float f: data){
                projectedHours = projectedHours + f;
            }
            if ((budgetHourOnSummaryCard - projectedHours)>0){
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromDistrictSummaryCard.get("▼")) - (budgetHourOnSummaryCard - projectedHours)) == 0, false);
            } else {
                SimpleUtils.assertOnFail("Difference hours is inconsistent!",
                        (Math.abs(valuesFromDistrictSummaryCard.get("▲")) + (budgetHourOnSummaryCard - projectedHours)) == 0, false);

            }
            //Verify past week Clocked Hours displays.
            scheduleDMViewPage.verifyClockedOrProjectedInDMViewTable("Clocked Hrs");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Schedule Status on Schedule in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleStatusOnScheduleInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            //Go to the Schedule page in BU view.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule BU view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            //Validate the content on Not Started Schedules/In Progress/Published smart cards
            scheduleDMViewPage.verifyTheContentOnScheduleStatusCards();

            //Validate the numbers on Schedule Status Cards
            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Schedule Status on Schedule in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleStatusOnScheduleInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            //Go to the Schedule page in Region view.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule Region view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            //Validate the content on Not Started Schedules/In Progress/Published smart cards
            scheduleDMViewPage.verifyTheContentOnScheduleStatusCards();

            //Validate the numbers on Schedule Status Cards
            scheduleDMViewPage.verifyTheScheduleStatusAccountOnScheduleStatusCards();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }




    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the availability of region list and sub region on Schedule in BU View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheAvailabilityOfRegionListAndSubRegionOnScheduleInBUViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = selectedUpperFields.get(BusinessUnit);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule Region view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            // Validate the region list
            SimpleUtils.assertOnFail("The region: " + regionName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(regionName), false);
            // Validate click one region
            scheduleDMViewPage.clickOnLocationNameInDMView(regionName);

            // Validate go back from selected region in current week
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", scheduleDMViewPage.isScheduleDMView(), false);

            // Validate click given region and given week
            scheduleCommonPage.navigateToPreviousWeek();
            String weekInfo = scheduleDMViewPage.getCurrentWeekInDMView();
            scheduleDMViewPage.clickOnLocationNameInDMView(regionName);
            SimpleUtils.assertOnFail("It didn't navigate to the Schedule page of the region in that week",
                    weekInfo.equals(scheduleDMViewPage.getCurrentWeekInDMView()), false);

            // Validate click other BU in past week
            String[] upperFields2 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            String anotherBUName = upperFields2[1];
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, anotherBUName.trim());
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", scheduleDMViewPage.isScheduleDMView(), false);

            // Validate click given region and given week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            weekInfo = scheduleDMViewPage.getCurrentWeekInDMView();
            scheduleDMViewPage.clickOnLocationNameInDMView(regionName);
            SimpleUtils.assertOnFail("It didn't navigate to the Schedule page of the region in that week",
                    weekInfo.equals(scheduleDMViewPage.getCurrentWeekInDMView()), false);

            // Validate click other BU in future week
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            anotherBUName = upperFields2[1];
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, anotherBUName.trim());
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", scheduleDMViewPage.isScheduleDMView(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the availability of district list and sub district on Schedule in Region View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheAvailabilityOfRegionListAndSubRegionOnScheduleInRegionViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get(District);
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule Region view page not loaded Successfully!", scheduleDMViewPage.isScheduleDMView(), false);

            // Validate the district list
            SimpleUtils.assertOnFail("The district: " + districtName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName), false);
            // Validate click one district
            scheduleDMViewPage.clickOnLocationNameInDMView(districtName);

            // Validate go back from selected district in current week
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", scheduleDMViewPage.isScheduleDMView(), false);

            // Validate click given district and given week
            scheduleCommonPage.navigateToPreviousWeek();
            String weekInfo = scheduleDMViewPage.getCurrentWeekInDMView();
            scheduleDMViewPage.clickOnLocationNameInDMView(districtName);
            SimpleUtils.assertOnFail("It didn't navigate to the Schedule page of the district in that week",
                    weekInfo.equals(scheduleDMViewPage.getCurrentWeekInDMView()), false);

            // Validate click other Region in past week
            String[] upperFields2 = null;
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            String anotherRegionName = upperFields2[2];
            String anotherDistrictName = upperFields2[3];
            locationSelectorPage.changeUpperFieldDirect(Region, anotherRegionName.trim());
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", scheduleDMViewPage.isScheduleDMView(), false);

            // Validate click given region and given week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            weekInfo = scheduleDMViewPage.getCurrentWeekInDMView();
            scheduleDMViewPage.clickOnLocationNameInDMView(anotherDistrictName.trim());
            SimpleUtils.assertOnFail("It didn't navigate to the Schedule page of the district in that week",
                    weekInfo.equals(scheduleDMViewPage.getCurrentWeekInDMView()), false);

            // Validate click other BU in future week
            if (getDriver().getCurrentUrl().contains(propertyMap.get(controlEnterprice))) {
                upperFields2 = controlUpperFields2;
            } else {
                upperFields2 = opUpperFields2;
            }
            anotherRegionName = upperFields2[2];
            locationSelectorPage.changeUpperFieldDirect(Region, anotherRegionName.trim());
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", scheduleDMViewPage.isScheduleDMView(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }
}
