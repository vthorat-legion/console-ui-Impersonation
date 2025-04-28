package com.legion.pages.pagefactories.mobile;

import com.legion.pages.core.ConsoleAnalyticsPage;
import com.legion.pages.core.ConsoleControlsPage;
import com.legion.pages.core.ConsoleLoginPage;
import com.legion.pages.core.ConsoleStaffingGuidancePage;
import com.legion.pages.core.ConsoleSalesForecastPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.pages.core.ConsoleScheduleOverviewPage;
import com.legion.pages.core.ConsoleTeamPage;
import com.legion.pages.core.ConsoleTimeSheetPage;
import com.legion.pages.core.ConsoleTrafficForecastPage;
import com.legion.pages.core.ConsoleUserAuthorizationPage;
import com.legion.pages.core.ConsoleDashboardPage;
import com.legion.pages.core.ConsoleLocationSelectorPage;
import com.legion.pages.core.mobile.MobileLoginPage;
import com.legion.pages.mobile.LoginPageAndroid;
import com.legion.pages.AnalyticsPage;
import com.legion.pages.ControlsPage;
import com.legion.pages.DashboardPage;
import com.legion.pages.LocationSelectorPage;
import com.legion.pages.LoginPage;
import com.legion.pages.SchedulePage;
import com.legion.pages.StaffingGuidancePage;
import com.legion.pages.SalesForecastPage;
import com.legion.pages.ScheduleOverviewPage;
import com.legion.pages.TeamPage;
import com.legion.pages.TimeSheetPage;
import com.legion.pages.TrafficForecastPage;
import com.legion.pages.UserAuthorizationPage;

import org.testng.Reporter;

public class MobileWebPageFactory implements MobilePageFactory {

	@Override
	public LoginPageAndroid createMobileLoginPage() {
		// TODO Auto-generated method stub
		return new MobileLoginPage();
	}
   
	

    
}
