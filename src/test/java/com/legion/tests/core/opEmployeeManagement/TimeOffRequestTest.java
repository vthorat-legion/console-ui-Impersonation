package com.legion.tests.core.opEmployeeManagement;

import com.legion.pages.ProfileNewUIPage;
import com.legion.pages.TeamPage;
import com.legion.pages.TimeSheetPage;
import com.legion.pages.core.OpCommons.*;
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
import com.legion.utils.DBConnection;
import com.legion.utils.SimpleUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TimeOffRequestTest extends TestBase {

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        this.createDriver((String) params[0], "83", "Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        RightHeaderBarPage modelSwitchPage = new RightHeaderBarPage();
        modelSwitchPage.switchToOpsPortal();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time Off Request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyEmployeeCanRequestAsInternalAdminOfTimeOffRequestTest(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();
        //search template
        String tempName = "AccrualAuto-Don't touch!!!";
        absentManagePage.configureTemplate(tempName);
        //template details page
        //configure time off reason1
        absentManagePage.removeTimeOffRules("Annual Leave");
        absentManagePage.configureTimeOffRules("Annual Leave");
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();
        //set value for request rules
        setTimeOffRRules(configurationPage, true, true, true, "10", "2", "6", "2", true, true, true, "8", false, "1095", "Days", "9999");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);


        //configure time off reason2
        absentManagePage.removeTimeOffRules("Floating Holiday");
        absentManagePage.configureTimeOffRules("Floating Holiday");
        //set value for request rules
        setTimeOffRRules(configurationPage, false, false, false, "7", "2", "8", "3", false, false, true, "8", false, "3", "Months", "4");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);


        //configure time off reason3
        absentManagePage.removeTimeOffRules("PTO");
        absentManagePage.configureTimeOffRules("PTO");
        //set value for request rules
        setTimeOffRRules(configurationPage, true, false, false, "20", "2", "7", "3", false, false, false, "8", true, "3", "Months", "10");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "25");
        configurationPage.saveTimeOffConfiguration(true);

        //configure time off reason4
        absentManagePage.removeTimeOffRules("Sick");
        absentManagePage.configureTimeOffRules("Sick");
        //set value for request rules
        setTimeOffRRules(configurationPage, false, true, true, "24", "2", "8", "3", true, false, false, "8", false, "3", "Months", "4");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);

        //associate
        String dynamicGroupName = "Newark-for auto";
        absentManagePage.switchToAssociation();
        OpsCommonComponents commonComponents = new OpsCommonComponents();
        commonComponents.associateWithDynamicGroups(dynamicGroupName);
        absentManagePage.switchToDetails();

        //can employee request yes
        absentManagePage.setTemplateLeverCanRequest(true);
        //weekly limit 40
        absentManagePage.setTemplateLeverWeeklyLimits("40");

        //publish
        absentManagePage.saveTemplateAs("Publish now");
        SimpleUtils.pass("Succeeded in creating template: " + tempName + " !");

        //switch to console.
        RightHeaderBarPage headerBarPage = new RightHeaderBarPage();
        headerBarPage.switchToNewTab();
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        consoleNavigationPage.searchLocation("Newark");
        consoleNavigationPage.navigateTo("Team");
        TimeOffPage timeOffPage = new TimeOffPage();
        timeOffPage.goToTeamMemberDetail("Adele Kutch");//Allene Mante

        //clear the history time off record.
        String WorkerId = timeOffPage.getWorkerId();
        deleteRequestedTimeOffDateByWorkerId(WorkerId);
        refreshPage();
        timeOffPage.switchToTimeOffTab();
        Assert.assertEquals(timeOffPage.getTimeOffRequestNum(), 0);
        SimpleUtils.pass("Succeeded in clearing history time off records!");

        //Edit annual leave balance
        timeOffPage.editTimeOffBalance("Annual Leave", "5");
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getTimeOffBalance().get("Annual Leave"), "5");
        SimpleUtils.pass("Succeeded in editing time off balance!");

        //request rules validation
        //annual Leave    --can request true   track accrual---true
        //floatingHoliday--can request false  track accrual---true
        //PTO            --can request true   track accrual---false
        //Sick           --can request false  track accrual---false
        //10 Does this time off reason track Accruals ?
        timeOffPage.getTimeOffTypes();
        Assert.assertTrue(timeOffPage.getTimeOffTypes().contains("Annual Leave") && timeOffPage.getTimeOffTypes().contains("Floating Holiday"));
        Assert.assertFalse(timeOffPage.getTimeOffTypes().contains("PTO") && timeOffPage.getTimeOffTypes().contains("Sick"));
        SimpleUtils.pass("Succeeded in validating time off rules: " + "Does this time off reason track Accruals ?");


        //1 Employee can request ?
        //Annual Leave - Bal
        //PTO
        timeOffPage.checkTimeOffSelect();
        Assert.assertTrue(timeOffPage.getTimeOffOptions().get(0).contains("Annual Leave - Bal") && timeOffPage.getTimeOffOptions().contains("PTO"));
        Assert.assertFalse(timeOffPage.getTimeOffOptions().contains("PTO - Bal"));
        Assert.assertFalse(timeOffPage.getTimeOffOptions().contains("floatingHoliday") && timeOffPage.getTimeOffOptions().contains("Sick"));
        SimpleUtils.pass("Succeeded in validating time off rules: " + "Employee can request ?");

        //2 Employee can request partial day ?
        //yes
        commonComponents.okToActionInModal(false);
        timeOffPage.selectTimeOff("Annual Leave");
        Assert.assertTrue(timeOffPage.isPartialDayEnabled(), "Failed to request partial day!!!");
        SimpleUtils.pass("Succeeded in validating time off rules: " + "Employee can request partial day!");
        //no
        commonComponents.okToActionInModal(false);
        timeOffPage.selectTimeOff("PTO");
        Assert.assertFalse(timeOffPage.isPartialDayEnabled(), "The partial day should not be displayed as we set PTO can't request partial day!");
        SimpleUtils.pass("Succeeded in validating time off rules: " + "Employee can't request partial day!");

        //13 Probation Period
        //in probation period
        commonComponents.okToActionInModal(false);
        timeOffPage.createOneDayTimeOff("Annual Leave", "March 2022",true, 3, 3);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Time off request cannot be submitted during probation period");
        SimpleUtils.pass("Succeeded in validating time off rules: " + "Employee can't request time off in probation period!");
        //out of probation period
        commonComponents.okToActionInModal(false);
        timeOffPage.createOneDayTimeOff("PTO", "March 2022", false, 1, 1);//-7hours
        commonComponents.okToActionInModal(true);
        timeOffPage.getAccrualHistory().keySet().contains("PTO taken - 7 hours");
        SimpleUtils.pass("Succeeded in validating Employee can request time off out probation period!");
        //all day time off default: 7
        SimpleUtils.pass("Succeeded in validating PTO all day time off default is 7!");
        SimpleUtils.pass("Succeeded in validating Employee can request time off in weekly limits!");

        //4 Weekly limits(hours)  request 2 days 2*7=14; 7hours have been requested last time,3*7=21 hours, more than weekly limit 20 hours;
        //can't request in one week
        timeOffPage.createManyDaysTimeOff("PTO", "March 2022",3, 4);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Above weekly max");
        SimpleUtils.pass("Succeeded in validating employee can't request time off which out of Weekly limits!");
        //But can request when acrossing the week
        commonComponents.okToActionInModal(false);
        timeOffPage.createManyDaysTimeOff("PTO", "March 2022",4, 5);
        commonComponents.okToActionInModal(true);
        timeOffPage.getAccrualHistory().keySet().contains("PTO taken - 14 hours");
        SimpleUtils.pass("Succeeded in validating employee can request time off across week!");
        SimpleUtils.pass("Succeeded in validating employee can request time off in available year limit!");

        //continue request one day off to test-- "Time off request exceeds available year limit"
        //3*7=21hrs already requested, available year limit: 25hrs, 21+7(now request)=28>25
        timeOffPage.createManyDaysTimeOff("PTO", "March 2022",17, 17);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Time off request exceeds available year limit");
        SimpleUtils.pass("Succeeded in validating employee can request time off out of available year limit!");
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time Off Request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyTimeOffRulesAsInternalAdminOfTimeOffRequestTest(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();
        //search template
        String tempName = "AccrualAuto-Don't touch!!!";
        absentManagePage.configureTemplate(tempName);
        //template details page
        //configure time off reason1
        absentManagePage.removeTimeOffRules("Annual Leave");
        absentManagePage.configureTimeOffRules("Annual Leave");
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();
        //set value for request rules
        setTimeOffRRules(configurationPage, true, true, true, "32", "2", "6", "3", true, true, true, "8", false, "90", "Days", "5");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);

        //configure time off reason2
        absentManagePage.removeTimeOffRules("Floating Holiday");
        absentManagePage.configureTimeOffRules("Floating Holiday");
        //set value for request rules
        setTimeOffRRules(configurationPage, true, false, false, "40", "2", "8", "3", true, false, true, "8", false, "3", "Months", "5");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);

        //configure time off reason3
        absentManagePage.removeTimeOffRules("PTO");
        absentManagePage.configureTimeOffRules("PTO");
        //set value for request rules
        setTimeOffRRules(configurationPage, true, false, false, "40", "2", "7", "3", false, false, true, "8", false, "3", "Months", "5");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "20");
        configurationPage.saveTimeOffConfiguration(true);

        //configure time off reason4
        absentManagePage.removeTimeOffRules("Sick");
        absentManagePage.configureTimeOffRules("Sick");
        //set value for request rules
        setTimeOffRRules(configurationPage, true, true, true, "24", "2", "8", "3", true, false, true, "8", false, "3", "Months", "4");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);
        SimpleUtils.pass("Succeeded in setting time off rules!");
        //associate
        String dynamicGroupName = "Newark-for auto";
        absentManagePage.switchToAssociation();
        OpsCommonComponents commonComponents = new OpsCommonComponents();
        commonComponents.associateWithDynamicGroups(dynamicGroupName);
        SimpleUtils.pass("Succeeded in associating to the target dynamic group!");
        absentManagePage.switchToDetails();

        //can employee request yes
        absentManagePage.setTemplateLeverCanRequest(true);
        //weekly limit 40
        absentManagePage.setTemplateLeverWeeklyLimits("40");

        //publish
        absentManagePage.saveTemplateAs("Publish now");
        SimpleUtils.pass("Succeeded in creating template: " + tempName + " !");

        //switch to console.
        RightHeaderBarPage headerBarPage = new RightHeaderBarPage();
        headerBarPage.switchToNewTab();
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        consoleNavigationPage.searchLocation("Newark");
        consoleNavigationPage.navigateTo("Team");
        TimeOffPage timeOffPage = new TimeOffPage();
        timeOffPage.goToTeamMemberDetail("Adele Kutch");

        //clear the history time off record.
        String WorkerId = timeOffPage.getWorkerId();
        deleteRequestedTimeOffDateByWorkerId(WorkerId);
        refreshPage();
        timeOffPage.switchToTimeOffTab();
        Assert.assertEquals(timeOffPage.getTimeOffRequestNum(), 0);
        SimpleUtils.pass("Succeeded in clearing history time off records!");
        //wait for the template taking effect.

        //Edit Floating Holiday balance to 10hrs
        timeOffPage.editTimeOffBalance("Annual Leave", "60");
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getTimeOffBalance().get("Annual Leave"), "60");
        SimpleUtils.pass("Succeeded in editing Annual Leave balance!");
        //7 Days an employee can request at one time： 3
        //days can request at one time:3 ---request 4 days.--failed to request
        timeOffPage.createOneDayTimeOff("Annual Leave",  "March 2022",false, 20, 23);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Annual Leave Exceeds days an employee can request in one time");
        SimpleUtils.pass("Succeeded in validating employee can't request time off more than max days at one time!");
        //request 3 days------can request successfully
        //enforce yearly limit: false, max available 15, 3days*6hrs=18hrs>15 ---can request successfully
        commonComponents.okToActionInModal(false);
        timeOffPage.createOneDayTimeOff("Annual Leave",  "March 2022",false, 20, 22);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getTimeOffBalance().get("Annual Leave"), "42");
        timeOffPage.getAccrualHistory().keySet().contains("Annual Leave taken - 18 hours");
        SimpleUtils.pass("Succeeded in validating employee can request 3 days at one time!");
        SimpleUtils.pass("Succeeded in validating employee can request more than max available hours when enforce yearly limit is false!");
        //2 request lined up more than 3 days---also can't request.
        timeOffPage.createOneDayTimeOff("Annual Leave",  "March 2022",false, 23, 23);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Annual Leave Exceeds days an employee can request in one time");
        SimpleUtils.pass("Succeeded in validating employee can't request when 2 requests add up and more than 3 days!");

        //14 Annual Use Limit
        //Annual Leave: 5 days, already use 3 days, ask for another 2 days,---<=5 days successfully
        commonComponents.okToActionInModal(false);
        timeOffPage.createOneDayTimeOff("Annual Leave",  "March 2022",false, 27, 28);
        commonComponents.okToActionInModal(true);
        timeOffPage.getAccrualHistory().keySet().contains("Annual Leave taken - 12 hours");
        SimpleUtils.pass("Succeeded in validating employee can request time off when time off hours used less than or equal to Annual earn limit!");
        //>5days---Failed
        timeOffPage.createOneDayTimeOff("Annual Leave",  "March 2022",false, 29, 29);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Time off request exceeds annual use limit");
        SimpleUtils.pass("Succeeded in validating employee can request time off when time off hours used less than or equal to Annual use limit!");

        //11 Max hours in advance of what you earn
        //Edit Floating Holiday balance to 10hrs
        commonComponents.okToActionInModal(false);
        timeOffPage.editTimeOffBalance("Floating Holiday", "7");
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getTimeOffBalance().get("Floating Holiday"), "7");
        SimpleUtils.pass("Succeeded in editing Floating Holiday balance!");
        //Edit PTO balance to 6hrs
        timeOffPage.editTimeOffBalance("PTO", "5");
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getTimeOffBalance().get("PTO"), "5");
        SimpleUtils.pass("Succeeded in editing PTO balance!");
        //auto reject: ture, max hours in advance: 8
        //balance 7hrs-request 1day(8hrs)&auto reject: ture(can't borrow from future)---failed to request
        timeOffPage.createOneDayTimeOff("Floating Holiday",  "March 2022",false, 29, 29);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Not enough accrued hours for Floating Holiday");
        SimpleUtils.pass("Succeeded in validating employee can't use max hours in advance when auto reject is false!");

        //auto reject: false, max hours in advance: 8----7(one day)
        //balance 5-7hrs=-2  still 6hrs left can be borrowed.  ---can request
        commonComponents.okToActionInModal(false);
        timeOffPage.createOneDayTimeOff("PTO",  "March 2022",false, 29, 29);
        commonComponents.okToActionInModal(true);
        timeOffPage.getAccrualHistory().keySet().contains("PTO - 7 hours");
        Assert.assertEquals(timeOffPage.getTimeOffBalance().get("PTO"), "-2");
        SimpleUtils.pass("Succeeded in validating employee can request time off in Max hours in advance!");
        //request another day, need 7hrs,but only 6 hrs left, ----failed
        timeOffPage.createOneDayTimeOff("PTO",  "March 2022",false, 30, 30);
        commonComponents.okToActionInModal(true);
        Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Not enough accrued hours for PTO");
        SimpleUtils.pass("Succeeded in validating employee can't request exceeding Max hours in advance!");
        commonComponents.okToActionInModal(false);
        //9 test cases
        //5 Days request must be made in advance
        //3 Manager can submit in timesheet ?

        /*timeOffPage.rejectTimeOffRequest();
        consoleNavigationPage.navigateTo("Logout");*/
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time Off Request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyDaysMustBeMadeInAdvanceAsInternalAdminOfTimeOffRequestTest(String browser, String username, String password, String location) throws Exception {

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time Off Request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyManagerCanSubmitInTimeSheetAsInternalAdminOfTimeOffRequestTest(String browser, String username, String password, String location) throws Exception {

    }



    //Set time off rules
    public void setTimeOffRRules(TimeOffReasonConfigurationPage configurationPage, Boolean canRequest, Boolean partialDay, Boolean submitInTimesheet, String weeklyLimits, String daysInAdvance, String allDayDefault, String daysCanRequestOneTime, Boolean autoReject, Boolean overTime, Boolean trackAccrual, String maxHorsInAdvance, Boolean enforceYearlyLimits, String probationPeriod, String probationUnit, String annualUseLimit) {
        configurationPage.setTimeOffRequestRuleAs("Employee can request ?", canRequest);
        configurationPage.setTimeOffRequestRuleAs("Employee can request partial day ?", partialDay);
        configurationPage.setTimeOffRequestRuleAs("Manager can submit in timesheet ?", submitInTimesheet);
        configurationPage.setValueForTimeOffRequestRules("Weekly limits(hours)", weeklyLimits);
        configurationPage.setValueForTimeOffRequestRules("Days request must be made in advance", daysInAdvance);
        configurationPage.setValueForTimeOffRequestRules("Configure all day time off default", allDayDefault);
        configurationPage.setValueForTimeOffRequestRules("Days an employee can request at one time", daysCanRequestOneTime);
        configurationPage.setTimeOffRequestRuleAs("Auto reject time off which exceed accrued hours ?", autoReject);
        configurationPage.setTimeOffRequestRuleAs("Allow Paid Time Off to compute to overtime ?", overTime);
        configurationPage.setTimeOffRequestRuleAs("Does this time off reason track Accruals ?", trackAccrual);
        configurationPage.setValueForTimeOffRequestRules("Max hours in advance of what you earn", maxHorsInAdvance);
        configurationPage.setTimeOffRequestRuleAs("Enforce Yearly Limits", enforceYearlyLimits);
        configurationPage.setValueForTimeOffRequestRules("Probation Period", probationPeriod);
        configurationPage.setProbationUnitAs(probationUnit);
        configurationPage.setValueForTimeOffRequestRules("Annual Use Limit", annualUseLimit);
    }

    //Delete the time off request just created before each test.
    public void deleteRequestedTimeOffDateByWorkerId(String workerId) {
        String enterpriseId = "aee2dfb5-387d-4b8b-b3f5-62e86d1a9d95";
        String DBName = "legionrc";
        if(System.getProperty("env").contains("ephemeral-ops"))
            DBName = "legiondb_ephemeral_ops";
        String sql1 = "delete from " + DBName + ".TimeOffRequest where workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'";
        System.out.println("sql1： " + sql1);
        String sql2 = "delete from " + DBName + ".TimeOffRequestHistory where workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'";
        System.out.println("sql2： " + sql2);
        String sql3 = "delete from " + DBName + ".WorkerAccrualHistory where workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'";
        System.out.println("sql3： " + sql3);
        String sql4 ="delete from " + DBName + ".TAWorkerPTO where workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'";
        System.out.println("sql4： " + sql4);
        DBConnection.updateDB(sql1);
        DBConnection.updateDB(sql2);
        DBConnection.updateDB(sql3);
        DBConnection.updateDB(sql4);
        String queryResult1 = DBConnection.queryDB(DBName + ".TimeOffRequest", "objectId", "workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'");
        Assert.assertEquals(queryResult1, "No item returned!", "Failed to clear the data just generated in DB!");
        String queryResult2 = DBConnection.queryDB(DBName + ".TimeOffRequestHistory", "objectId", "workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'");
        Assert.assertEquals(queryResult2, "No item returned!", "Failed to clear the data just generated in DB!");
        String queryResult3 = DBConnection.queryDB(DBName + ".WorkerAccrualHistory", "objectId", "workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'");
        Assert.assertEquals(queryResult3, "No item returned!", "Failed to clear the data just generated in DB!");
        String queryResult4 = DBConnection.queryDB(DBName + ".TAWorkerPTO", "id", "workerId='" + workerId + "' and enterpriseId='" + enterpriseId + "'");
        Assert.assertEquals(queryResult4, "No item returned!", "Failed to clear the data just generated in DB!");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "OPS-4060 Ability to enable/disable accrual deductions for customers who do not use Legion to track accruals")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyUseAccrualToggleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();

        AbsentManagePage absentManagePage = new AbsentManagePage();
        absentManagePage.switchToSettings();
        absentManagePage.setAccrualToggle(false);

        refreshCache("Template");

        switchToNewWindow();

        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        consoleNavigationPage.navigateTo("logout");
        Thread.sleep(480000);
        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("nancy.nan+admin@legion.co", "admin11.a","verifyMock");
        consoleNavigationPage.searchLocation("verifyMock");
        consoleNavigationPage.navigateTo("Team");

        TeamPage teamPage = pageFactory.createConsoleTeamPage();

        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMemberByName("Nancy NoContact");
        teamPage.navigateToTimeOffPage();

        deleteRequestedTimeOffDateByWorkerId("8aab92e1-8d15-4cad-b6fa-8c596e4d691c");

        //refreshPage();

        HashMap<String, String> expectedTOBalance = new HashMap<>();
        //expectedTOBalance.put("Annual Leave", "100");
        TimeOffPage timeOffPage = new TimeOffPage();
        OpsCommonComponents opsCommonComponents = new OpsCommonComponents();
        timeOffPage.createTimeOff("Annual Leave",false,28,28);
        opsCommonComponents.okToActionInModal(true);
        timeOffPage.createTimeOff("Annual Leave1",false,27,27);
        opsCommonComponents.okToActionInModal(true);
        expectedTOBalance.put("Annual Leave", "100");
        expectedTOBalance.put("Annual Leave1", "0");
        expectedTOBalance.put("Annual Leave2", "0");
        expectedTOBalance.put("Annual Leave3", "0");
        expectedTOBalance.put("Annual Leave4", "0");
        expectedTOBalance.put("Bereavement1", "0");
        expectedTOBalance.put("Bereavement2", "0");
        expectedTOBalance.put("Bereavement3", "0");
        expectedTOBalance.put("Bereavement4", "0");
        expectedTOBalance.put("Covid1", "0");
        expectedTOBalance.put("Covid2", "0");
        expectedTOBalance.put("Covid3", "0");
        expectedTOBalance.put("Covid4", "0");
        expectedTOBalance.put("Floating Holiday", "0");
        expectedTOBalance.put("Grandparents Day Off1", "0");

        HashMap<String, String> accrualBalance100 = timeOffPage.getTimeOffBalance();
        System.out.println(accrualBalance100);
        System.out.println(expectedTOBalance);
        Assert.assertTrue(expectedTOBalance.equals(accrualBalance100));

        switchToNewWindow();
        navigationPage.navigateToEmployeeManagement();
        panelPage.goToTimeOffManagementPage();
        absentManagePage.switchToSettings();
        absentManagePage.setAccrualToggle(true);

        refreshCache("Template");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time off activity")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOffActivityAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        // delete created time off
        deleteRequestedTimeOffDateByWorkerId("223e6893-07a4-4df0-96d2-d5e008a413e8");

        String createdBy = "7b856e05-dfcd-4911-81e0-f4c109840ef0";
        String DBName = "legionrc";

        if(System.getProperty("env").contains("ephemeral-ops")) {
            createdBy = "7b856e05-dfcd-4911-81e0-f4c109840ef0";
            DBName = "legiondb_ephemeral_ops";
        }

        // delete activity
        String sql = "delete from " + DBName + ".Activity where createdBy = '" + createdBy + "' and enterpriseId = 'aee2dfb5-387d-4b8b-b3f5-62e86d1a9d95'";
        DBConnection.updateDB(sql);

        String queryResult = DBConnection.queryDB(DBName + ".Activity", "objectId", "createdBy = '" + createdBy + "' and enterpriseId = 'aee2dfb5-387d-4b8b-b3f5-62e86d1a9d95'");
        Assert.assertEquals(queryResult, "No item returned!", "Failed to clear the data just generated in DB!");

        // Verify activity doesn't display for admin
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        ActivityPage activityPage = new ActivityPage();
        activityPage.switchToNewWindow();
        Assert.assertEquals(activityPage.verifyActivityDisplay(), false);

        //create approved time off for tm
        //ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        //consoleNavigationPage.searchLocation("verifyMock");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember("Activity");
        TimeOffPage timeOffPage = new TimeOffPage();
        timeOffPage.switchToTimeOffTab();
        OpsCommonComponents commonComponents = new OpsCommonComponents();
        timeOffPage.editTimeOffBalance("Annual Leave","1000");
        commonComponents.okToActionInModal(true);
        timeOffPage.createTimeOff("Annual Leave",false,27,27);
        commonComponents.okToActionInModal(true);

        navigationPage.logout();
        // Verify activity doesn't display for team member
        loginToLegionAndVerifyIsLoginDone("nancy.nan+activity@legion.co", "admin11.a","verifyMock");
        OpsPortalNavigationPage navigationPage1 = new OpsPortalNavigationPage();
        Assert.assertEquals(activityPage.verifyActivityDisplay(), false);
        //consoleNavigationPage.searchLocation("verifyMock");

        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember("Activity");

        timeOffPage.switchToTimeOffTab();
        timeOffPage.createTimeOff("Annual Leave",false,28,28);
        commonComponents.okToActionInModal(true);
        timeOffPage.createTimeOff("Annual Leave",false,29,29);
        commonComponents.okToActionInModal(true);
        timeOffPage.createTimeOff("Annual Leave",false,30,30);
        commonComponents.okToActionInModal(true);
        RightHeaderBarPage rightHeaderBarPage = new RightHeaderBarPage();
        rightHeaderBarPage.navigateToTimeOff();
        timeOffPage.cancelCreatedTimeOffRequest();
        navigationPage1.logout();
        // Verify activity  display for store member
        loginToLegionAndVerifyIsLoginDone("nancy.nan+sm@legion.co", "admin11.a","verifyMock");
        Assert.assertEquals(activityPage.verifyActivityDisplay(), true);

        //search and go to the target location
        //consoleNavigationPage.searchLocation("verifyMock");

        // click activity
        Assert.assertEquals(activityPage.verifyActivityBoxDisplay(), true);
        activityPage.clickTimeOff();
        activityPage.clickTimeOffDetail();

        //verify approve is clickable
        activityPage.verifyApproveIsClickable();

        //verify reject is clickable
        activityPage.verifyRejectIsClickable();

        //verify activity title
        activityPage.verifyActivityTitle();

        //verify time off status
        activityPage.verifyActivityTimeOffStatus();

        //verify first activity is cancelled
       // activityPage.verifyCancel();

        //approve second activity and verify it's approved
        activityPage.approveActivityTimeOff();
        activityPage.verifyApprove();

        //reject third activity and verify it's rejected
        activityPage.rejectActivityTimeOff();
      //  activityPage.verifyReject();

        refreshPage();
//        navigationPage1.logout();
//
//        //tm login to check its time off status have changed
//        loginToLegionAndVerifyIsLoginDone("nancy.nan+activity@legion.co", "admin11.a","verifyMock");
//        //consoleNavigationPage.searchLocation("verifyMock");
//        rightHeaderBarPage.navigateToTimeOff();
//        timeOffPage.verifyTimeOffStatus();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "TA-9631 Nike ER | Coaches need to be able to add TO reasons to TMs shifts that TMs have no visibility")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOffAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DistributionMethod");
        AbsentManagePage absentManagePage = new AbsentManagePage();
        //search template
        String tempName = user.get("template");
        String timeOff = "Personal Emergency";
        absentManagePage.search(tempName);
        absentManagePage.clickInDetails();
        absentManagePage.viewTimeOffConfigure(timeOff);
        HashMap<String, String> requestRules = new HashMap<String, String>();
        requestRules.put("Employee can request?", "No");
        requestRules.put("Manager can request ?", "Yes");
        absentManagePage.verifyRequestRules(requestRules);
        //switch to console.
        RightHeaderBarPage headerBarPage = new RightHeaderBarPage();
        headerBarPage.switchToNewTab();
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        consoleNavigationPage.searchLocation(user.get("location"));
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.searchAndSelectTeamMember(user.get("name"));
        timeOffPage.switchToTimeOffTab();
        timeOffPage.checkTimeOffSelect();
        timeOffPage.verifyIsTimeOffDisplayedInSelectList(timeOff, true);
        OpsCommonComponents commonComponents = new OpsCommonComponents();
        commonComponents.okToActionInModal(false);
        navigationPage.logout();
        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("yang.fang+0021@legion.co", "Hydsoft123", "locationAutoCreateForYang");
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Time Off");
        timeOffPage.checkTimeOffSelect();
        timeOffPage.verifyIsTimeOffDisplayedInSelectList(timeOff, false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "OPS-6593 Canada Sick Leave Support")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCanadaSickLeaveAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        RightHeaderBarPage headerBarPage = new RightHeaderBarPage();
        headerBarPage.switchToNewTab();
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        TimeOffPage timeOffPage = new TimeOffPage();
        //get session id via login
        String sessionId = getSession();
        HashMap<String, String> user = timeOffPage.getAccrualUser("DayUnit");
        String workerId = user.get("id");
        String name = user.get("name");
        consoleNavigationPage.searchLocation(user.get("location"));
        //go to team member details and switch to the time off tab.
        consoleNavigationPage.navigateTo("Team");
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(name);
        timeOffPage.switchToTimeOffTab();

        //confirm template
        timeOffPage.getUserTemplate(workerId, sessionId, user.get("template"));
        HashMap<String, String> expectedBalance = new HashMap<>();
        expectedBalance.put("DayUnit3", "30");
        timeOffPage.editTimeOff(expectedBalance);
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
        String time = dateTime.format(formatter);
        int today = Integer.parseInt(time.substring(0,time.indexOf("-")))-1;
        timeOffPage.createTimeOff("DayUnit3", false, today, today);
        OpsCommonComponents opsCommonComponents = new OpsCommonComponents();
        opsCommonComponents.okToActionInModal(true);
        consoleNavigationPage.navigateTo("Timesheet");
        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
        timeSheetPage.reaggregateTimesheet();
        timeSheetPage.seachAndSelectWorkerByName(name);
        timeSheetPage.verifyPTOOfEmployee(name,"8");
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMember(name);
        timeOffPage.switchToTimeOffTab();
        timeOffPage.rejectTimeOffRequest();
    }

}
