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

public class ImpersonationTest extends TestBase {

////    //The location that has same BU and  different region with the default location
//    private static String location2 = "5751200 - Ticket POS";
//    private static String Location = "Index32";
//    private static String District = "District";
//    private static String Region = "Region";
//    private static String BusinessUnit = "Business Unit";
//    private static String hQ = "HQ";

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
    @TestName(description = "Unable to impersonate not accepted Term of Service (ToS) user")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void A_VerifyUnableToImpersonateNotAcceptedToSUserAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ImpersonationPage CCImpersonation = pageFactory.ConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser("Bea Mine");
            CCImpersonation.endImpersonationSession () ;
            CCImpersonation.checkAcceptedToSAndImpersonateUser("Jack1 Keron");
            CCImpersonation.endImpersonationSession ();
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
            ImpersonationPage CCImpersonation = pageFactory.ConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser("Dimphy Cakir");
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
            ImpersonationPage CCImpersonation = pageFactory.ConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser("ed0209bd-9589-431f-9999-4f906b5a6fcc");
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
            ImpersonationPage CCImpersonation = pageFactory.ConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser("Dimphy Cakir");
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
            ImpersonationPage CCImpersonation = pageFactory.ConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser("Dimphy Cakir");
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
            ImpersonationPage CCImpersonation = pageFactory.ConsoleImpersonationPage ();
            CCImpersonation.gotoControlsPage();
            CCImpersonation.checkAcceptedToSAndImpersonateUser("Dimphy Cakir");
            LocationSelectorPage locationsPage = pageFactory.createLocationSelectorPage();
            locationsPage.searchSpecificLocationAndNavigateTo("Loc-01");
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
}
