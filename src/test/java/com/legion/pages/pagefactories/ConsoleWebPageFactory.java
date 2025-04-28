package com.legion.pages.pagefactories;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.*;
import com.legion.pages.core.*;
import com.legion.pages.core.OpsPortal.*;
import com.legion.pages.core.schedule.*;
import com.legion.pages.opConfiguration.MealAndRestPage;
import org.testng.Reporter;

/**
 * Yanming
 */
public class ConsoleWebPageFactory implements PageFactory {
    @Override
    public LoginPage createConsoleLoginPage() {
    	return new ConsoleLoginPage();
    }

    @Override
    public DashboardPage createConsoleDashboardPage() {
    	Reporter.log("Logged-in Successfully!");
        return new ConsoleDashboardPage();
    }
    
    /* Aug 03- Zorang Added Below code */
    @Override
    public TeamPage createConsoleTeamPage() {
    	return new ConsoleTeamPage();
    }
	@Override
	public PlanPage createConsolePlanPage() {
		return new ConsolePlanPage();
	}
    @Override
    public AnalyticsPage createConsoleAnalyticsPage() {
    	return new ConsoleAnalyticsPage();
    }
    
    @Override  
    public ControlsPage createConsoleControlsPage() {
    	return new ConsoleControlsPage();
    }

	@Override
	public UserAuthorizationPage createConsoleUserAuthorizationPage() {
		return new ConsoleUserAuthorizationPage();
	}
	
	@Override
	public SalesForecastPage createSalesForecastPage() {
		return new ConsoleSalesForecastPage();
	}

	@Override
	public StaffingGuidancePage createStaffingGuidancePage() {
		return new ConsoleStaffingGuidancePage();
	}

	@Override
	public LocationSelectorPage createLocationSelectorPage() {
		return new ConsoleLocationSelectorPage();
	}

	@Override
	public ScheduleOverviewPage createScheduleOverviewPage() {
		return new ConsoleScheduleOverviewPage();
	}

	@Override
	public SchedulePage createConsoleScheduleNewUIPage() {
		return new ConsoleScheduleNewUIPage();
	}

	@Override
	public TrafficForecastPage createTrafficForecastPage() {
		return new ConsoleTrafficForecastPage();
	}

	@Override
	public TimeSheetPage createTimeSheetPage() {
		return new ConsoleTimeSheetPage();
	}
	
	@Override
	public ControlsNewUIPage createControlsNewUIPage() {
		return new ConsoleControlsNewUIPage();
	}
	
	@Override
	public ProjectedSalesPage createProjectedSalesPage() {
		return new ConsoleProjectedSalesPage();
	}

	@Override
	public ProfileNewUIPage createProfileNewUIPage() {
		return new ConsoleProfileNewUIPage();
	}

	@Override
	public ForecastPage createForecastPage() {
		return new ConsoleForecastPage();
	}

	@Override
	public GmailPage createConsoleGmailPage() { return new ConsoleGmailPage(); }

	@Override
	public ActivityPage createConsoleActivityPage() { return new ConsoleActivityPage(); }

	@Override
	public LocationsPage createOpsPortalLocationsPage() { return new OpsPortalLocationsPage();
	}

	@Override
	public LiquidDashboardPage createConsoleLiquidDashboardPage() { return new ConsoleLiquidDashboardPage(); }

	@Override
	public JobsPage createOpsPortalJobsPage() { return new OpsPortalJobsPage();}

	@Override
	public ScheduleDMViewPage createScheduleDMViewPage() { return new ConsoleScheduleDMViewPage(); }

	@Override
	public InboxPage createConsoleInboxPage() { return new ConsoleInboxPage(); }

	@Override
	public CinemarkMinorPage createConsoleCinemarkMinorPage() { return new ConsoleCinemarkMinorPage(); }

	@Override
	public AdminPage createConsoleAdminPage() { return new ConsoleAdminPage(); }

	@Override
	public ConfigurationPage createOpsPortalConfigurationPage() { return new OpsPortalConfigurationPage();}

	@Override
	public CompliancePage createConsoleCompliancePage() { return new ConsoleCompliancePage(); }

	@Override
	public IntegrationPage createIntegrationPage() { return new ConsoleIntegrationPage(); }

	@Override
	public OnboardingPage createOnboardingPage() { return new ConsoleOnboardingPage(); }

	@Override
	public NewsPage createNewsPage (){ return new ConsoleNewsPage() ;}
	@Override
	public ReportPage createConsoleReportPage() { return new ConsoleReportPage(); }
	@Override
	public InsightPage createConsoleInsightPage() { return new ConsoleInsightPage(); }
	@Override
	public NewsPage createConsoleNewsPage() { return new ConsoleNewsPage(); }

	@Override
	public IntegrationPage createConsoleIntegrationPage() { return new ConsoleIntegrationPage(); }

	@Override
	public UserManagementPage createOpsPortalUserManagementPage() {
		return new OpsPortalUserManagementPage();
	}

	@Override
	public LaborModelPage createOpsPortalLaborModelPage() {
		return new OpsPortalLaborModelPage();
	}

	@Override
	public AnalyzePage createAnalyzePage() { return new ConsoleAnalyzePage(); }

	@Override
	public CreateSchedulePage createCreateSchedulePage() { return new ConsoleCreateSchedulePage(); }

	@Override
	public DragAndDropPage createDragAndDropPage() { return new ConsoleDragAndDropPage(); }

	@Override
	public MySchedulePage createMySchedulePage() { return new ConsoleMySchedulePage(); }

	@Override
	public ScheduleCommonPage createScheduleCommonPage() { return new ConsoleScheduleCommonPage(); }

	@Override
	public ScheduleMainPage createScheduleMainPage() { return new ConsoleScheduleMainPage(); }

	@Override
	public ScheduleShiftTablePage createScheduleShiftTablePage() { return new ConsoleScheduleShiftTablePage(); }

	@Override
	public ShiftOperatePage createShiftOperatePage() { return new ConsoleShiftOperatePage(); }

	@Override
	public SmartCardPage createSmartCardPage() { return new ConsoleSmartCardPage(); }

	@Override
	public ToggleSummaryPage createToggleSummaryPage() { return new ConsoleToggleSummaryPage(); }

	@Override
	public NewShiftPage createNewShiftPage() { return new ConsoleNewShiftPage(); }

	@Override
	public SettingsAndAssociationPage createSettingsAndAssociationPage() {	return new OpsPortalSettingsAndAssociationPage(); }

	@Override
	public MealAndRestPage createMealAndRestPage() {
		return new com.legion.pages.core.opConfiguration.MealAndRestPage();
	}

	@Override
	public EditShiftPage createEditShiftPage() {return new ConsoleEditShiftPage(); }

	@Override
	public ShiftPatternPage createConsoleShiftPatternPage() {
		return new ConsoleShiftPatternPage();
	}

	@Override
	public NewShiftPatternBiddingPage createNewShiftPatternBiddingPage() {
		return new ConsoleNewShiftPatternBiddingPage();
	}

	@Override
	public BidShiftPatternBiddingPage createBidShiftPatternBiddingPage() {
		return new ConsoleBidShiftPatternBiddingPage();
	}

	@Override
	public RulePage createRulePage() {
		return new ConsoleRulePage();
	}
}
