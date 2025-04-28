package com.legion.pages.pagefactories;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.*;
import com.legion.pages.opConfiguration.MealAndRestPage;

/**
 * Yanming
 */
public interface PageFactory {
    LoginPage createConsoleLoginPage();

    DashboardPage createConsoleDashboardPage();
    
    /* 
     * Aug 03- Zorang Added Below code 
     * */
    TeamPage createConsoleTeamPage();
    
    UserAuthorizationPage createConsoleUserAuthorizationPage();
    
    AnalyticsPage createConsoleAnalyticsPage();
    
    ControlsPage createConsoleControlsPage();
    
    SalesForecastPage createSalesForecastPage();
    
    StaffingGuidancePage createStaffingGuidancePage();
    
    LocationSelectorPage createLocationSelectorPage();
    
    ScheduleOverviewPage createScheduleOverviewPage();
    
    SchedulePage createConsoleScheduleNewUIPage();
    
    TrafficForecastPage createTrafficForecastPage();
    
    TimeSheetPage createTimeSheetPage(); 
    
    ControlsNewUIPage createControlsNewUIPage();

	ProjectedSalesPage createProjectedSalesPage(); 
	
	ProfileNewUIPage createProfileNewUIPage();

    ForecastPage createForecastPage();

    GmailPage createConsoleGmailPage();

    ActivityPage createConsoleActivityPage();

    LocationsPage createOpsPortalLocationsPage();
    PlanPage createConsolePlanPage();

    LiquidDashboardPage createConsoleLiquidDashboardPage();

    JobsPage createOpsPortalJobsPage();
    ScheduleDMViewPage createScheduleDMViewPage();

    InboxPage createConsoleInboxPage();

    CinemarkMinorPage createConsoleCinemarkMinorPage();

    AdminPage createConsoleAdminPage();

    ConfigurationPage createOpsPortalConfigurationPage();
    CompliancePage createConsoleCompliancePage();

    IntegrationPage createIntegrationPage();

    OnboardingPage createOnboardingPage();

    NewsPage createNewsPage();

    ReportPage createConsoleReportPage();

    InsightPage createConsoleInsightPage();

    NewsPage createConsoleNewsPage();

    IntegrationPage createConsoleIntegrationPage();

    UserManagementPage createOpsPortalUserManagementPage();

    LaborModelPage createOpsPortalLaborModelPage();

    ScheduleCommonPage createScheduleCommonPage();

    AnalyzePage createAnalyzePage();

    CreateSchedulePage createCreateSchedulePage();

    DragAndDropPage createDragAndDropPage();

    MySchedulePage createMySchedulePage();

    ScheduleMainPage createScheduleMainPage();

    ScheduleShiftTablePage createScheduleShiftTablePage();

    ShiftOperatePage createShiftOperatePage();

    SmartCardPage createSmartCardPage();

    ToggleSummaryPage createToggleSummaryPage();

    NewShiftPage createNewShiftPage();

    SettingsAndAssociationPage createSettingsAndAssociationPage();
    MealAndRestPage createMealAndRestPage();

    EditShiftPage createEditShiftPage();

    ShiftPatternPage createConsoleShiftPatternPage();

    NewShiftPatternBiddingPage createNewShiftPatternBiddingPage();

    BidShiftPatternBiddingPage createBidShiftPatternBiddingPage();

    RulePage createRulePage();
}