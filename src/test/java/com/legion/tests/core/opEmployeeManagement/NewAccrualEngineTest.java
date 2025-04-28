package com.legion.tests.core.opEmployeeManagement;

import com.legion.pages.DashboardPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.TeamPage;
import com.legion.pages.core.OpCommons.ConsoleNavigationPage;
import com.legion.pages.core.OpCommons.OpsCommonComponents;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.OpCommons.RightHeaderBarPage;
import com.legion.pages.core.opemployeemanagement.AbsentManagePage;
import com.legion.pages.core.opemployeemanagement.EmployeeManagementPanelPage;
import com.legion.pages.core.opemployeemanagement.TimeOffPage;
import com.legion.pages.core.opemployeemanagement.TimeOffReasonConfigurationPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

public class NewAccrualEngineTest extends TestBase {
    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        this.createDriver((String) params[0], "83", "Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Accrual Engine Distribution Types")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAccrualEngineWorksWellAfterEditingAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        //search and go to the target location
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DistributionMethod");
        String workerId = user.get("id");
        String timeZone = user.get("timeZone");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));//hireDate:2023/1/10
        timeOffPage.switchToTimeOffTab();

        //get session id via login
        String sessionId = getSession();
        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        //Delete the worker's accrual balance
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //expected accrual
        HashMap<String, String> expectedBalance = new HashMap<>();
        expectedBalance.put("Annual Leave", "2");// HireDate~HireDate/Monthly /hire month/begin

        //run engine to a specified date
        String date1 = "2023-2-1%2000:00:00";
        timeOffPage.runAccrualJobToSimulateDate(workerId, date1, sessionId, timeZone);
        //and verify the result in UI
        refreshPage();
        timeOffPage.switchToTimeOffTab();
        HashMap<String, String> actualTOB = timeOffPage.getSpecificTimeOffBalance(expectedBalance.keySet());
        timeOffPage.validateTheAccrualResults(expectedBalance, actualTOB);

        //Edit the employee's balance
        expectedBalance.put("Annual Leave", "3");
        timeOffPage.editTimeOff(expectedBalance);
        expectedBalance.put("Annual Leave", "5");
        //run engine to a specified date and verify that accrued based on the editing balance.
        String date2 = "2023-11-10%2000:00:00";
        timeOffPage.runAccrualJobToSimulateDate(workerId, date2, sessionId, timeZone);
        //and verify the result in UI
        refreshPage();
        timeOffPage.switchToTimeOffTab();
        actualTOB = timeOffPage.getSpecificTimeOffBalance(expectedBalance.keySet());
        timeOffPage.validateTheAccrualResults(expectedBalance, actualTOB);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify monthly distribution method")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMonthlyDistributionMethodAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        //search and go to the target location
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DistributionMethod");
        String workerId = user.get("id");
        String timeZone = user.get("timeZone");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));//hireDate:2023/1/10
        timeOffPage.switchToTimeOffTab();

        //get session id via login
        String sessionId = getSession();
        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        //Delete the worker's accrual balance
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //expected accrual
        HashMap<String, ArrayList> expectedBalance = new HashMap<>();
        //blocked by TA-10276
        expectedBalance.put("Annual Leave", new ArrayList<>(Arrays.asList("2", "2", "4", "4", "6")));// HireDate~HireDate/Monthly /hire month/begin
        expectedBalance.put("Annual Leave1", new ArrayList<>(Arrays.asList("0", "2", "2", "4", "6")));//Specified~Specified/Monthly /calendar month/end
        expectedBalance.put("Annual Leave2", new ArrayList<>(Arrays.asList("0", "2", "2", "4", "6")));// HireDate~Specified/Monthly /hire month/end
        HashMap<String, String> expectedBalanceNew = new HashMap<>();
        //run engine to a specified date
        ArrayList<String> date = new ArrayList<>();
        date.add("2023-1-11%2000:00:00");
        date.add("2023-02-01%2000:00:00");
        date.add("2023-02-11%2000:00:00");
        date.add("2023-03-01%2000:00:00");
        date.add("2023-04-01%2000:00:00");
        HashMap<String, String> actualBalance;
        for (int i = 0; i < date.size(); i++) {
            //expected accrual
            expectedBalanceNew.put("Annual Leave", expectedBalance.get("Annual Leave").get(i).toString());
            expectedBalanceNew.put("Annual Leave1", expectedBalance.get("Annual Leave1").get(i).toString());
            expectedBalanceNew.put("Annual Leave2", expectedBalance.get("Annual Leave2").get(i).toString());
            timeOffPage.runAccrualJobToSimulateDate(workerId, date.get(i), sessionId, timeZone);
            //and verify the result in UI
            refreshPage();
            timeOffPage.switchToTimeOffTab();
            actualBalance = timeOffPage.getSpecificTimeOffBalance(expectedBalanceNew.keySet());
            timeOffPage.validateTheAccrualResults(actualBalance, expectedBalanceNew);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify weekly distribution method")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWeeklyDistributionMethodAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        //search and go to the target location
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DistributionMethod");
        String workerId = user.get("id");
        String timeZone = user.get("timeZone");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));//hireDate:2023/1/10
        timeOffPage.switchToTimeOffTab();

        //get session id via login
        String sessionId = getSession();
        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        //Delete the worker's accrual balance
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //expected accrual
        HashMap<String, ArrayList> expectedBalance = new HashMap<>();
        //Blocked by TA-10289
        expectedBalance.put("Annual Leave3", new ArrayList<>(Arrays.asList("0", "1", "2", "3")));// HireDate~HireDate/Weekly
        expectedBalance.put("Annual Leave4", new ArrayList<>(Arrays.asList("0", "0", "1", "2")));//Specified~Specified/Weekly
        HashMap<String, String> expectedBalanceNew = new HashMap<>();
        //run engine to a specified date
        ArrayList<String> date = new ArrayList<>();
        date.add("2023-01-10%2000:00:00");
        date.add("2023-01-18%2000:00:00");
        date.add("2023-01-25%2000:00:00");
        date.add("2023-02-02%2000:00:00");
        HashMap<String, String> actualBalance;
        for (int i = 0; i < date.size(); i++) {
            //expected accrual
            expectedBalanceNew.put("Annual Leave3", expectedBalance.get("Annual Leave3").get(i).toString());
            expectedBalanceNew.put("Annual Leave4", expectedBalance.get("Annual Leave4").get(i).toString());
            timeOffPage.runAccrualJobToSimulateDate(workerId, date.get(i), sessionId, timeZone);
            //and verify the result in UI
            refreshPage();
            timeOffPage.switchToTimeOffTab();
            actualBalance = timeOffPage.getSpecificTimeOffBalance(expectedBalanceNew.keySet());
            timeOffPage.validateTheAccrualResults(actualBalance, expectedBalanceNew);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify lump sum distribution method")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLumpSumDistributionMethodAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        //search and go to the target location
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DistributionMethod");
        String workerId = user.get("id");
        String timeZone = user.get("timeZone");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));//hireDate:2023/1/10
        timeOffPage.switchToTimeOffTab();

        //get session id via login
        String sessionId = getSession();
        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        //Delete the worker's accrual balance
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //expected accrual
        HashMap<String, ArrayList> expectedBalance = new HashMap<>();
        expectedBalance.put("Bereavement1", new ArrayList<>(Arrays.asList("10", "10", "17", "17")));// HireDate~HireDate/Lump sum
        expectedBalance.put("Bereavement2", new ArrayList<>(Arrays.asList("0", "0", "11", "6")));//Specified~Specified/Lump sum/10 allowance in days(out of)
        expectedBalance.put("Bereavement3", new ArrayList<>(Arrays.asList("10", "5", "16", "6")));//HireDate~Specified/Weekly

        HashMap<String, String> expectedBalanceNew = new HashMap<>();
        //run engine to a specified date
        ArrayList<String> date = new ArrayList<>();
        date.add("2023-1-11%2000:00:00");
        date.add("2023-12-31%2000:00:00");
        date.add("2024-1-10%2000:00:00");
        date.add("2024-12-31%2000:00:00");
        HashMap<String, String> actualBalance;
        for (int i = 0; i < date.size(); i++) {
            //expected accrual
            expectedBalanceNew.put("Bereavement1", expectedBalance.get("Bereavement1").get(i).toString());
            expectedBalanceNew.put("Bereavement2", expectedBalance.get("Bereavement2").get(i).toString());
            expectedBalanceNew.put("Bereavement3", expectedBalance.get("Bereavement3").get(i).toString());
            timeOffPage.runAccrualJobToSimulateDate(workerId, date.get(i), sessionId, timeZone);
            //and verify the result in UI
            refreshPage();
            timeOffPage.switchToTimeOffTab();
            actualBalance = timeOffPage.getSpecificTimeOffBalance(expectedBalanceNew.keySet());
            timeOffPage.validateTheAccrualResults(actualBalance, expectedBalanceNew);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Payable hour types UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPayableHoursUIConfigurationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        RightHeaderBarPage modelSwitchPage = new RightHeaderBarPage();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        modelSwitchPage.switchToOpsPortal();
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        //verify that employee management is enabled.
        navigationPage.navigateToEmployeeManagement();
        SimpleUtils.pass("EmployeeManagement Module is enabled!");
        //go to the time off management page
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();
        absentManagePage.switchToSettings();
        //Get all time off reasons.
        ArrayList<String> timeOffConfiguredInGlobalSettings = absentManagePage.getTimeOffReasonsInGlobalSetting();
        absentManagePage.switchToTemplates();
        String templateName = "AccrualAuto-PayableHours(Don't touch!!!)";
        absentManagePage.search(templateName);
        SimpleUtils.assertOnFail("Failed the find the target template!", absentManagePage.getResult().equals(templateName), false);
        //configure: floating holiday
        absentManagePage.configureTemplate(templateName);
        absentManagePage.configureTimeOffRules("Floating holiday");//worked hours/total
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();
        configurationPage.setAccrualPeriod("Hire Date", "Hire Date", null, null, null, null);
        configurationPage.setDistributionMethod("Worked Hours");
        //1.verify there is payable hours displayed
        configurationPage.getPayableTitle();
        //2.verify that configure button is displayed
        configurationPage.isPayableConfigButtonDisplayed();
        //3.open the modal, and verify the title
        configurationPage.configurePayableHours();
        configurationPage.removeTheExistingHourType();
        //4.add more button is displayed.
        configurationPage.isAddMoreButtonDisplayed();
        configurationPage.addMore();
        //5.get all options
        ArrayList HoursTypeOptions = configurationPage.getHoursTypeOptions();
        System.out.println(HoursTypeOptions);
        SimpleUtils.pass("Succeeded in validating all the Payable Hour Types!");
        //6.Remove the existing hour types
        configurationPage.removeTheExistingHourType();
        //7.get time off options
        configurationPage.addHourTypes("Time Off Type", null, null);
        ArrayList<String> options = configurationPage.getTimeOffOptions();
        Assert.assertTrue(timeOffConfiguredInGlobalSettings.size() == options.size() && timeOffConfiguredInGlobalSettings.containsAll(options) && options.containsAll(timeOffConfiguredInGlobalSettings), "Failed to assert the time off options are the full list of Time offs in global settings!");
        SimpleUtils.pass("Succeeded in validating the time off options are the full list of Time offs in global settings!");
        configurationPage.removeTheExistingHourType();
        //8.add regular hour
        configurationPage.addHourTypes("Regular Hours", null, null);
        //9.add time off type
        configurationPage.addHourTypes("Time Off Type", "Sick", "PTO");//sick & PTO
        //10.add holiday
        configurationPage.addHourTypes("Holiday", "Fixed Hours", null);
        //11.Other pay type
        // configurationPage.addHourTypes("Other Pay Type", "OtherPay1", null);
        //12.compliance
        configurationPage.addHourTypes("Compliance", "Doubletime", null);
        OpsCommonComponents commonComponents = new OpsCommonComponents();
        commonComponents.okToActionInModal(true);
        //save configuration
        configurationPage.saveTimeOffConfiguration(true);
        SimpleUtils.pass("Succeeded in setting all the Payable Hour Types for Floating Holiday!");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Payable hour types accrual")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPayableHoursDistributionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        //search and go to the target location
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("WorkedHoursMethod");
        String workerId = user.get("id");
        String timeZone = user.get("timeZone");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));//hireDate:2023/1/10
        timeOffPage.switchToTimeOffTab();

        //get session id via login
        String sessionId = getSession();
        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        //Delete the worker's accrual balance
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //expected accrual
        HashMap<String, ArrayList> expectedBalance = new HashMap<>();
        expectedBalance.put("Floating Holiday", new ArrayList<>(Arrays.asList("0", "1", "3")));//OtherPay1+doubletime
        expectedBalance.put("PTO", new ArrayList<>(Arrays.asList("0", "0.7", "1.25")));//
        //expectedBalance.put("Sick", new ArrayList<>(Arrays.asList("1.25", "10")));//
        HashMap<String, String> expectedBalanceNew = new HashMap<>();
        //run engine to a specified date
        ArrayList<String> date = new ArrayList<>();
        date.add("2023-1-10%2000:00:00");
        date.add("2023-2-19%2000:00:00");
        date.add("2023-2-20%2000:00:00");
        //date.add("2023-2-21");
        HashMap<String, String> actualBalance;
        for (int i = 0; i < date.size(); i++) {
            //expected accrual
            expectedBalanceNew.put("Floating Holiday", expectedBalance.get("Floating Holiday").get(i).toString());
            timeOffPage.runAccrualJobToSimulateDate(workerId, date.get(i), sessionId, timeZone);
            //and verify the result in UI
            refreshPage();
            timeOffPage.switchToTimeOffTab();
            actualBalance = timeOffPage.getSpecificTimeOffBalance(expectedBalanceNew.keySet());
            timeOffPage.validateTheAccrualResults(actualBalance, expectedBalanceNew);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "OPS-3733 Usability updates to employee accrual history.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAccrualHistoryAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        //search and go to the target location
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DistributionMethod");
        String workerId = user.get("id");
        String timeZone = user.get("timeZone");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));//hireDate:2023/1/10
        timeOffPage.switchToTimeOffTab();
        //get session id via login
        String sessionId = getSession();
        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        //Delete the worker's accrual balance
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //expected timeOffType
        Set<String> timeOffType = new HashSet<>() ;
//        timeOffType.add("Annual Leave");
        timeOffType.add("Annual Leave1");
        //run engine to a specified date
        ArrayList<String> date = new ArrayList<>();
        date.add("2023-1-10%2000:00:00");
        date.add("2023-02-01%2000:00:00");
        date.add("2023-02-11%2000:00:00");
        date.add("2023-03-01%2000:00:00");
        for (int i = 0; i < date.size(); i++) {
            timeOffPage.runAccrualJobToSimulateDate(workerId, date.get(i), sessionId ,timeZone);
        }
        refreshPage();
        timeOffPage.switchToTimeOffTab();
        //Verify the accrual history
        //create a history map to store the expected history items
        HashMap<String, String> expectedAccrualHistory = new HashMap<>();
//        expectedAccrualHistory.put("Annual Leave Accrued + 5 hours", "Accrued hours on Mar 9, 2024");
        expectedAccrualHistory.put("Annual Leave1 Accrued + 2 hour", "Accrued hours on 28 Feb, 2023");
 //       expectedAccrualHistory.put("Annual Leave Deducted - 14 hours", "Deducted hours to max carryover on 31 Dec, 2023");
 //       expectedAccrualHistory.put("Annual Leave Accrued + 22 hours", "Accrued hours on Jan 9, 2024");
 //       expectedAccrualHistory.put("Annual Leave1 Accrued + 2 hour", "Accrued hours on 31 Jan, 2023");
 //       expectedAccrualHistory.put("Annual Leave Accrued + 2 hours", "Accrued hours on 9 Feb, 2023");
        HashMap<String, String> actualHis = timeOffPage.filterOutHistory(timeOffType,"","");
        timeOffPage.validateTheAccrualResults(actualHis, expectedAccrualHistory);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Import employee time off balance")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyAccrualEngineWorksWellAfterImportingAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        //search and go to the target location
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("WorkedHoursMethod");
        String workerId = user.get("id");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));
        timeOffPage.switchToTimeOffTab();
        //get session id via login
        String sessionId = getSession();
        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        //Delete the worker's accrual balance
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //import accrual balance via CSV file.
        String FileName = "AccrualLedger_auto.csv";
        timeOffPage.importAccrualBalance(sessionId, FileName);
        refreshPage();
        timeOffPage.switchToTimeOffTab();
        //expected timeOffType
        HashMap<String, String> expectedBalance = new HashMap<>();
        expectedBalance.put("Floating Holiday", "13");
        expectedBalance.put("PTO", "7");
        HashMap<String, String> actualBalance = timeOffPage.getSpecificTimeOffBalance(expectedBalance.keySet());
        timeOffPage.validateTheAccrualResults(actualBalance, expectedBalance);
        HashMap<String, String> expectedAccrualHistory = new HashMap<>();
        if (System.getProperty("env").contains("RC")) {
            expectedAccrualHistory.put("PTO Imported from 0 hours to 7 hours + 7 hours", "Balance hours Imported by Grace01 H on Apr 11, 2023");
            expectedAccrualHistory.put("Floating Holiday Imported from 0 hours to 13 hours + 13 hours", "Balance hours Imported by Grace01 H on Apr 11, 2023");
        } else {
            expectedAccrualHistory.put("PTO Imported from 0 hours to 7 hours + 7 hours", "Balance hours Imported by LisaA A on Apr 11, 2023");
            expectedAccrualHistory.put("Floating Holiday Imported from 0 hours to 13 hours + 13 hours", "Balance hours Imported by LisaA A on Apr 11, 2023");
        }
        HashMap<String, String> actualHis = timeOffPage.filterOutHistory(expectedBalance.keySet(), "", "");
        timeOffPage.validateTheAccrualResults(actualHis, expectedAccrualHistory);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "OPS-4799 Add ability to define accrual units")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDefineAccrualUnitsAbilityAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        //get session id via login
        String sessionId = getSession();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DayUnit");
        String workerId = user.get("id");
        String timeZone = user.get("timeZone");
        consoleNavigationPage.searchLocation(user.get("location"));
        timeOffPage.deleteAccrualByWorkerId(workerId, sessionId);
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(user.get("name"));
        timeOffPage.switchToTimeOffTab();

        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        HashMap<String, String> expectedBalance = new HashMap<>();
        expectedBalance.put("DayUnit", "10");
        expectedBalance.put("DayUnit1", "10");
        expectedBalance.put("DayUnit2", "10");
        timeOffPage.editTimeOff(expectedBalance);

        Set<String> expectedAccrualHistory = new HashSet<>();
        expectedAccrualHistory.add("DayUnit2 Edited from 0 day to + 10 day + 10 day");
        expectedAccrualHistory.add("DayUnit Edited from 0 day to + 10 day + 10 day");
        expectedAccrualHistory.add("DayUnit1 Edited from 0 day to + 10 day + 10 day");

        HashMap<String, String> actualHis = timeOffPage.filterOutHistory(expectedBalance.keySet(), "", "");
        timeOffPage.validateTheAccrualHistory(actualHis.keySet(), expectedAccrualHistory);

        //run engine to a specified date
        timeOffPage.runAccrualJobToSimulateDate(workerId, "2024-1-10%2000:00:00", sessionId, timeZone);

        refreshPage();
        timeOffPage.switchToTimeOffTab();
        Set<String> expectedAccrualHistory1 = new HashSet<>();
        expectedAccrualHistory1.add("DayUnit2 Edited from 0 day to + 10 day + 10 day");
        Set<String> timeoffReason = new HashSet<>();
        timeoffReason.add("DayUnit2");
        HashMap<String, String> actualHis1 = timeOffPage.filterOutHistory(timeoffReason,"","");
        timeOffPage.validateTheAccrualHistory(actualHis1.keySet(), expectedAccrualHistory1);
    }
}
