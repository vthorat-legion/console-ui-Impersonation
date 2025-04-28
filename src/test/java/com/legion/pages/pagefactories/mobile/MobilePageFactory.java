package com.legion.pages.pagefactories.mobile;
import com.legion.pages.AnalyticsPage;
import com.legion.pages.ControlsPage;
import com.legion.pages.LoginPage;
import com.legion.pages.SchedulePage;
import com.legion.pages.StaffingGuidancePage;
import com.legion.pages.SalesForecastPage;
import com.legion.pages.ScheduleOverviewPage;
import com.legion.pages.TeamPage;
import com.legion.pages.TimeSheetPage;
import com.legion.pages.TrafficForecastPage;
import com.legion.pages.UserAuthorizationPage;
import com.legion.pages.DashboardPage;
import com.legion.pages.LocationSelectorPage;
import com.legion.pages.mobile.LoginPageAndroid;
/**
 * Yanming
 */
public interface MobilePageFactory {
    LoginPageAndroid createMobileLoginPage();
   
   
}