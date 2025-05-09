package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.core.schedule.ConsoleCreateSchedulePage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.legion.utils.SimpleUtils.getImpersonationData;

public class ImpersonationTest extends TestBase {
    String userToSNotAccepted = ((Map<String, String>) getImpersonationData().get("users")).get("notAcceptedToS");
    String userToSAccepted = ((Map<String, String>) getImpersonationData().get("users")).get("acceptedToS");
    String timesheetUser = ((Map<String, String>) getImpersonationData().get("users")).get("timesheetUser");
    String manager= ((Map<String, String>) getImpersonationData().get("users")).get("Manager");
    String teamMember = ((Map<String, String>) getImpersonationData().get("users")).get("TeamMember");
    String location1 = ((Map<String, String>) getImpersonationData().get("locations")).get("location1");
    String location2 = ((Map<String, String>) getImpersonationData().get("locations")).get("location2");
    String upperfield = ((Map<String, String>) getImpersonationData().get("locations")).get("upperfieldLocation");
    String timesheetTime = ((Map<String, String>) getImpersonationData().get("timesheet")).get("time");
    String templateName = ((Map<String, String>) getImpersonationData().get("template")).get("templateName");
    String workerId = ((Map<String, String>) getImpersonationData().get("workerIds")).get("worker1");

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

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Verify that the Impersonate button is enabled once the user has accepted the Legion terms of service (ToS)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void A_VerifyUnableToImpersonateNotAcceptedToSUserAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(userToSNotAccepted);
            CCImpersonation.endImpersonationSession () ;
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonate user who has accepted Term of Service")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void B_VerifyImpersonationSessionAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(userToSAccepted);
            CCImpersonation.endImpersonationSession();
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonate User through WorkerId")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void C_VerifyImpersonationSessionAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(workerId);
            CCImpersonation.endImpersonationSession();
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonator End Session from control center")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void D_VerifySessionEndFromCCAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(userToSAccepted);
            CCImpersonation.gotoControlsPage();
            CCImpersonation.endImpersonationSession();

        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonator not able to edit Legion Profile of impersonated user ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void E_VerifyImpersonatorNotAbleToEditLegionProfileAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(userToSAccepted);
            CCImpersonation.goToLegionProfile();
            CCImpersonation.endImpersonationSession();
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonator details shows in Schedule History")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void F_VerifyScheduleHistoryAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.goToSchedule();
           CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(userToSAccepted);
            LocationSelectorPage locationsPage = pageFactory.createLocationSelectorPage();
            locationsPage.searchSpecificLocationAndNavigateTo(location1);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.goToSchedule();
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdate();
            createSchedulePage.publishActiveSchedule();
            createSchedulePage.checkScheduleHistory();
            CCImpersonation.endImpersonationSession();
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonator details shows in Timesheet History")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void G_VerifyTimesheetHistoryAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(userToSAccepted);
            LocationSelectorPage locationsPage = pageFactory.createLocationSelectorPage();
            locationsPage.searchSpecificLocationAndNavigateTo(location1);
            TimeSheetPage timesheet = pageFactory.createTimeSheetPage();
            timesheet.clickOnTimeSheetConsoleMenu();
            timesheet.addTimeClock(timesheetUser, timesheetTime);
            timesheet.checkTimesheetHistory();
            CCImpersonation.endImpersonationSession();
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonator details shows in Template History")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void H_VerifyTemplateHistoryAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(userToSAccepted);
            CCImpersonation.gotoControlsPage();
            CCImpersonation.createNewTemplate(templateName);
            CCImpersonation.checkTemplateHistory(templateName);
            CCImpersonation.endImpersonationSession();
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonator Session End From Manager and Employee View Page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void I_VerifySuccessfullyEndSessionFromManagerEmployeeViewPageAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(manager);
            CCImpersonation.switchToEmployeeView();
            CCImpersonation.endImpersonationSession();
            CCImpersonation.confirmSessionEnds();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(manager);
            CCImpersonation.switchBackToManagerView();
            CCImpersonation.endImpersonationSession();
            CCImpersonation.confirmSessionEnds();

        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Vikas")
    @Enterprise(name = "Forac_Enterprise")
    @TestName(description = "Impersonate User With Location Switch Condition")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void J_VerifySuccessfullyEndSessionWithSwitchLocationAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            LocationSelectorPage locationsPage = pageFactory.createLocationSelectorPage();
//            locationsPage.searchSpecificLocationAndNavigateTo(location2);
            ImpersonationPage CCImpersonation = pageFactory.createConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser(manager);
            CCImpersonation.endImpersonationSession();
//            locationsPage.searchSpecificLocationAndNavigateTo(upperfield);
//            CCImpersonation.gotoControlsPage();
//            CCImpersonation.checkAcceptedToSAndImpersonateUser(teamMember);
//            CCImpersonation.endImpersonationSession();
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
