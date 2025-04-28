package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.*;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class InboxTest extends TestBase {

    private static HashMap<String, String> parameterMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
    private static HashMap<String, String> propertyLocationTimeZone = JsonUtil.getPropertiesFromJsonFile("src/test/resources/LocationTimeZone.json");

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

    //Added by Nora
    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "verify reports will be available for export")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyGFEReportsAreAbleToExportAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Controls -> Compliance, turn on the GFE
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded Successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Controls: Compliance page not loaded Successfully!", controlsNewUIPage.isControlsComplianceLoaded(), false);
            controlsNewUIPage.turnGFEToggleOnOrOff(true);

            // Go to Analytics page
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            SimpleUtils.assertOnFail("Analytics Page not loaded Successfully!", analyticsPage.isReportsPageLoaded(), false);

            analyticsPage.switchAllLocationsOrSingleLocation(false);
            String gfe = "Good Faith Estimate";
            if (analyticsPage.isSpecificReportLoaded(gfe)) {
                analyticsPage.mouseHoverAndExportReportByName(gfe);
                // Verify whether the file is downloaded or not
                Thread.sleep(5000);
                String downloadPath = parameterMap.get("Download_File_Default_Dir");
                SimpleUtils.assertOnFail("Failed to download the Good Faith Estimate Report!", FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "GoodFaithEstimate"), false);
            } else {
                SimpleUtils.fail("Analytics: " + gfe + " not loaded Successfully!", false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "verify the column and team member details and count of the report")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheColumnAndCountOfGFEReportAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            int rosterCount = teamPage.verifyTMCountIsCorrectOnRoster();
            List<String> tmNames = teamPage.getTMNameList();

            // Go to Controls -> Compliance, turn on the GFE
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded Successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Controls: Compliance page not loaded Successfully!", controlsNewUIPage.isControlsComplianceLoaded(), false);
            controlsNewUIPage.turnGFEToggleOnOrOff(true);

            // Go to Analytics page
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            SimpleUtils.assertOnFail("Analytics Page not loaded Successfully!", analyticsPage.isReportsPageLoaded(), false);

            analyticsPage.switchAllLocationsOrSingleLocation(false);
            String gfe = "Good Faith Estimate";
            String downloadPath = parameterMap.get("Download_File_Default_Dir");
            if (analyticsPage.isSpecificReportLoaded(gfe)) {
                analyticsPage.mouseHoverAndExportReportByName(gfe);
                // Verify whether the file is downloaded or not
                Thread.sleep(5000);
                SimpleUtils.assertOnFail("Failed to download the Good Faith Estimate Report!", FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "GoodFaithEstimate"), false);
            } else {
                SimpleUtils.fail("Analytics: " + gfe + " not loaded Successfully!", false);
            }
            File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadPath);
            String fileName = latestFile.getName();
            SimpleUtils.pass("KPI Report exported successfully with file name '" + fileName + "'.");
            ArrayList<String> actualHeader = CsvUtils.getHeaderFromCSVFileByPath(downloadPath + "/" + fileName);
            ArrayList<String> expectedHeader = new ArrayList<>(Arrays.asList("TM first name",
                    "TM last name",
                    "TM Employee ID",
                    "TM Job Title",
                    "Scheduling policy group",
                    "Date & time sent",
                    "Date & time read",
                    "Date & time acknowledged",
                    "Location",
                    "Location ID",
                    "Estimated working Days",
                    "Estimated working hours",
                    "Average hours per week",
                    "Minimum # of shifts per week"));
            if (actualHeader.containsAll(expectedHeader) && expectedHeader.containsAll(actualHeader)) {
                SimpleUtils.pass("GFE Report columns are correct!");
            } else {
                SimpleUtils.fail("GFE report columns are not correct!", true);
            }
            ArrayList<HashMap<String, String>> gfeDetails = CsvUtils.getDataFromCSVFileWithHeader(downloadPath + "/" + fileName);
            if (gfeDetails != null && gfeDetails.size() > 0 && rosterCount == gfeDetails.size()) {
                SimpleUtils.pass("Verified the count of TMs in the GFE report is consistent with Roster!");
                boolean isConsistent = true;
                for (HashMap<String, String> gfeDetail : gfeDetails) {
                    String name = gfeDetail.get("TM first name") + " " + gfeDetail.get("TM last name");
                    if (!tmNames.contains(name)) {
                        SimpleUtils.fail(name + " is not exist in the Team Roster!", false);
                        isConsistent = false;
                        break;
                    }
                }
                if (isConsistent) {
                    SimpleUtils.pass("Team members are consistent with the Roster!");
                }
            } else {
                SimpleUtils.fail("The count of TMs in the GFE report is inconsistent with Roster", false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "verify reports show the most recent GFE that was provided for each employee")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheMostRecentGFEIsInReportAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            //turn on GFE toggle
            controlsNewUIPage.turnGFEToggleOnOrOff(true);
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            //login as TM to get nickName
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String nickNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            //go to Inbox page create GFE announcement.
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            InboxPage inboxPage = pageFactory.createConsoleInboxPage();
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            inboxPage.sendToTM(nickNameOfTM);

            //change operating hours and working day
            String dayToClose = "MON";
            String estimatedWorkingDays = "Sunday, Tuesday, Wednesday, Thursday, Friday, Saturday";
            String dayToChangeHrs = "SUN";
            String startTime = "08:00AM";
            String endTime = "06:00PM";
            inboxPage.chooseOneDayToClose(dayToClose);
            inboxPage.changeOperatingHrsOfDay(dayToChangeHrs, startTime, endTime);
            List<String> selectedOperatingHours = inboxPage.getSelectedOperatingHours();
            //change week summary info
            String minimumShifts = "6";
            String averageHrs = "38";
            inboxPage.changeWeekSummaryInfo(minimumShifts, averageHrs);
            inboxPage.clickSendBtn();
            loginPage.logOut();

            //login as TM to check the gfe
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.clickFirstGFEInList();
            inboxPage.clickAcknowledgeBtn();
            String jsonTimeZone = propertyLocationTimeZone.get(location);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String acknowledgeTime = SimpleUtils.getCurrentDateMonthYearWithTimeZone(jsonTimeZone, sdf);
            loginPage.logOut();

            //login as SM to export the report.
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("Dashboard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            SimpleUtils.assertOnFail("Analytics Page not loaded Successfully!", analyticsPage.isReportsPageLoaded(), false);

            String gfe = "Good Faith Estimate";
            String downloadPath = parameterMap.get("Download_File_Default_Dir");
            if (analyticsPage.isSpecificReportLoaded(gfe)) {
                analyticsPage.mouseHoverAndExportReportByName(gfe);
                // Verify whether the file is downloaded or not
                Thread.sleep(5000);
                SimpleUtils.assertOnFail("Failed to download the Good Faith Estimate Report!", FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "GoodFaithEstimate"), false);
            } else {
                SimpleUtils.fail("Analytics: " + gfe + " not loaded Successfully!", false);
            }
            File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadPath);
            String gfeFileName = latestFile.getName();
            SimpleUtils.pass("KPI Report exported successfully with file name '" + gfeFileName + "'.");
            ArrayList<String> actualHeader = CsvUtils.getHeaderFromCSVFileByPath(downloadPath + "/" + gfeFileName);
            ArrayList<String> expectedHeader = new ArrayList<>(Arrays.asList("TM first name",
                    "TM last name",
                    "TM Employee ID",
                    "TM Job Title",
                    "Scheduling policy group",
                    "Date & time sent",
                    "Date & time read",
                    "Date & time acknowledged",
                    "Location",
                    "Location ID",
                    "Estimated working Days",
                    "Estimated working hours",
                    "Average hours per week",
                    "Minimum # of shifts per week"));
            if (actualHeader.containsAll(expectedHeader) && expectedHeader.containsAll(actualHeader)) {
                SimpleUtils.pass("GFE Report columns are correct!");
            } else {
                SimpleUtils.fail("GFE report columns are not correct!", true);
            }
            ArrayList<HashMap<String, String>> gfeDetails = CsvUtils.getDataFromCSVFileWithHeader(downloadPath + "/" + gfeFileName);
            if (gfeDetails != null && gfeDetails.size() > 0) {
                boolean isConsistent = false;
                for (HashMap<String, String> gfeDetail : gfeDetails) {
                    String firstName = gfeDetail.get("TM first name");
                    if (nickNameOfTM.equalsIgnoreCase(firstName)) {
                        String actualEstimatedWorkingDays = gfeDetail.get("Estimated working Days");
                        if (actualEstimatedWorkingDays.startsWith("\"") && actualEstimatedWorkingDays.endsWith("\"")) {
                            actualEstimatedWorkingDays = actualEstimatedWorkingDays.substring(1, actualEstimatedWorkingDays.length() - 1);
                        }
                        if (estimatedWorkingDays.equals(actualEstimatedWorkingDays)) {
                            SimpleUtils.pass("Analytics Report: Verified \"Estimated working Days\" is consistent: " + estimatedWorkingDays);
                        } else {
                            SimpleUtils.fail("Analytics Report: \"Estimated working Days\" is inconsistent, Expected: " + estimatedWorkingDays
                                    + ", the value in report is: " + actualEstimatedWorkingDays, true);
                        }
                        String actualWorkingHours = gfeDetail.get("Estimated working hours");
                        if (actualWorkingHours.startsWith("\"") && actualWorkingHours.endsWith("\"")) {
                            actualWorkingHours = actualWorkingHours.substring(1, actualWorkingHours.length() - 1);
                        }
                        String expectedOperatingHours = selectedOperatingHours.toString();
                        if (expectedOperatingHours.startsWith("[") && expectedOperatingHours.endsWith("]")) {
                            expectedOperatingHours = expectedOperatingHours.substring(1, expectedOperatingHours.length() - 1);
                        }
                        if (expectedOperatingHours.equals(actualWorkingHours)) {
                            SimpleUtils.pass("Analytics Report: Verified \"Estimated working hours\" is consistent: " + actualWorkingHours);
                        } else {
                            SimpleUtils.fail("Analytics Report: \"Estimated working hours\" is inconsistent, Expected: " + expectedOperatingHours
                                    + ", the value in report is: " + actualWorkingHours, true);
                        }
                        String actualAverageHour = gfeDetail.get("Average hours per week");
                        if (Float.parseFloat(averageHrs) == Float.parseFloat(actualAverageHour)) {
                            SimpleUtils.pass("Analytics Report: Verified \"Average hours per week\" is consistent: " + averageHrs);
                        } else {
                            SimpleUtils.fail("Analytics Report: \"Average hours per week\" is inconsistent, Expected: " + averageHrs
                                    + ", the value in report is: " + actualAverageHour, true);
                        }
                        String actualMinShifts = gfeDetail.get("Minimum # of shifts per week");
                        if (minimumShifts.equals(actualMinShifts)) {
                            SimpleUtils.pass("Analytics Report: Verified \"Minimum # of shifts per week\" is consistent: " + minimumShifts);
                        } else {
                            SimpleUtils.fail("Analytics Report: \"Minimum # of shifts per week\" is inconsistent, Expected: " + minimumShifts
                                    + ", the value in report is: " + actualMinShifts, true);
                        }
                        // Judge the difference of two acknowledge times should be less than 1 minute
                        String actualAcknowledgeTime = gfeDetail.get("Date & time acknowledged");
                        if ((sdf.parse(acknowledgeTime).getTime() - sdf.parse(actualAcknowledgeTime).getTime()) < 60 * 1000) {
                            SimpleUtils.pass("Analytics Report: Verified \"Date & time acknowledged\" is consistent: " + acknowledgeTime +
                                    "! The difference between the two times should be less than 1 minute!");
                        } else {
                            SimpleUtils.fail("Analytics Report: \"Date & time acknowledged\" is inconsistent, Expected: " + acknowledgeTime
                                    + ", the value in report is: " + actualAcknowledgeTime, false);
                        }
                        isConsistent = true;
                        break;
                    }
                }
                if (!isConsistent) {
                    SimpleUtils.fail("Analytics Report: the most recent GFE in report is incorrect!", false);
                }
            } else {
                SimpleUtils.fail("The count of TMs in the GFE report is inconsistent with Roster", false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //Added by Julie
    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the content of operating hours and the first day of week are correct")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentOfOperationHrsAndTheFirstDayOfWeekAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            InboxPage inboxPage = pageFactory.createConsoleInboxPage();

            // Make sure that GFE is turned on
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls Page failed to load", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Compliance Card failed to load", controlsNewUIPage.isCompliancePageLoaded(), false);
            controlsNewUIPage.turnGFEToggleOnOrOff(true);

            // Get Regular hours from Controls-> Working hours -> Regular
            String workingHoursType = "Regular";
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page failed to load", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsWorkingHoursCard();
            SimpleUtils.assertOnFail("Working Hours Card failed to load", controlsNewUIPage.isControlsWorkingHoursLoaded(), false);
            controlsNewUIPage.clickOnWorkHoursTypeByText(workingHoursType);
            LinkedHashMap<String, List<String>> regularHoursFromControls = controlsNewUIPage.getRegularWorkingHours();

            // Get the first day of week that schedule begins from Controls -> Scheduling Policies -> Schedules
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page failed to load", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Schedule Policy Card failed to load", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            String firstDayOfWeekFromControls = controlsNewUIPage.getSchedulingPoliciesFirstDayOfWeek();

            // Create a GFE announcement to verify its content of operation hours and the first day of week
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            String theFirstDayOfWeekFromGFE = inboxPage.getGFEFirstDayOfWeek();
            LinkedHashMap<String, List<String>> GFEWorkingHours = inboxPage.getGFEWorkingHours();

            // Compare the first day of week
            SimpleUtils.report("The first day of week from controls is: " + firstDayOfWeekFromControls);
            SimpleUtils.report("The first day of week from GFE is: " + theFirstDayOfWeekFromGFE);
            if (firstDayOfWeekFromControls.toUpperCase().contains(theFirstDayOfWeekFromGFE))
                SimpleUtils.pass("Inbox page: The first day of the week in GFE is consistent with the setting in Control -> Scheduling Policies -> What day of the week does your schedule begin?");
            else
                SimpleUtils.fail("Inbox page: The first day of the week in GFE is inconsistent with the setting in Control -> Scheduling Policies -> What day of the week does your schedule begin?",false);

            // Compare the operation days and hours
            if (inboxPage.compareGFEWorkingHrsWithRegularWorkingHrs(GFEWorkingHours, regularHoursFromControls))
                SimpleUtils.pass("Inbox page: Operation days and hours in GFE is consistent with the setting in Controls -> Working Hours -> Regular");
            else
                SimpleUtils.fail("Inbox page: Operation days and hours in GFE is inconsistent with the setting in Controls -> Working Hours -> Regular",false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify VSL info shows or not")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyVSLInfoShowsOrNotAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            InboxPage inboxPage = pageFactory.createConsoleInboxPage();

            // Make sure that GFE is turned on
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls Page failed to load", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Compliance Card failed to load", controlsNewUIPage.isCompliancePageLoaded(), false);

            // Turn on VSL to verify VSL info
            controlsNewUIPage.turnVSLToggleOnOrOff(true);
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            inboxPage.verifyVSLInfo(true);

            // Turn off VSL to verify VSL info
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls Page failed to load", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Compliance Card failed to load", controlsNewUIPage.isCompliancePageLoaded(), false);
            controlsNewUIPage.turnVSLToggleOnOrOff(false);
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            inboxPage.verifyVSLInfo(false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the content of week summary when selecting different tm")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentOfWeekSummaryForDifferentTMAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            InboxPage inboxPage = pageFactory.createConsoleInboxPage();

            // Make sure that GFE is turned on
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls Page failed to load", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Compliance Card failed to load", controlsNewUIPage.isCompliancePageLoaded(), false);
            controlsNewUIPage.turnGFEToggleOnOrOff(true);

            // Get 1 TM from Controls-> Users and Roles and get their data from scheduling policy group setting
            String nickName1 = "";
            String nickName1_Location = "";
            String nickName1_schedulingPolicyGroup = "";
            controlsPage.gotoControlsPage();
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            HashMap<String, List<String>> TM1 = controlsNewUIPage.getRandomUserNLocationNSchedulingPolicyGroup();
            Iterator itTM1 = TM1.keySet().iterator();
            while (itTM1.hasNext()) {
                nickName1 = itTM1.next().toString();
                List<String> nickName1_Value = TM1.get(nickName1);
                if (nickName1_Value != null && nickName1_Value.size() == 2) {
                    nickName1_Location = nickName1_Value.get(0);
                    if (nickName1_Location.equals("All Locations"))
                        nickName1_Location = location;
                    nickName1_schedulingPolicyGroup = nickName1_Value.get(1);
                    break;
                }
            }
            if (nickName1.contains(" "))
                nickName1 = nickName1.split(" ")[0] + " " + nickName1.split(" ")[1].substring(0, 1) + ".";
            controlsPage.gotoControlsPage();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling Policies Card failed to load", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.selectSchdulingPolicyGroupsTabByLabel(nickName1_schedulingPolicyGroup);
            HashMap<String, List<String>> schedulingPolicyGroupData_TM1 = controlsNewUIPage.getDataFromSchedulingPolicyGroups();

            // Get another TM from Controls-> Users and Roles and get their data from scheduling policy group setting
            String nickName2 = "";
            String nickName2_Location = "";
            String nickName2_schedulingPolicyGroup = "";
            controlsPage.gotoControlsPage();
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();

            HashMap<String, List<String>> TM2 = controlsNewUIPage.getRandomUserNLocationNSchedulingPolicyGroup();
            Iterator itTM2 = TM2.keySet().iterator();
            while (itTM2.hasNext()) {
                nickName2 = itTM2.next().toString();
                List<String> nickName2_Value = TM2.get(nickName2);
                if (nickName2_Value != null && nickName2_Value.size() == 2) {
                    nickName2_Location = nickName2_Value.get(0);
                    if (nickName2_Location.equals("All Locations"))
                        nickName2_Location = location;
                    nickName2_schedulingPolicyGroup = nickName2_Value.get(1);
                    break;
                }
            }
            if (nickName2.contains(" "))
                nickName2 = nickName2.split(" ")[0] + " " + nickName2.split(" ")[1].substring(0, 1) + ".";
            controlsPage.gotoControlsPage();
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling Policies Card failed to load", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.selectSchdulingPolicyGroupsTabByLabel(nickName2_schedulingPolicyGroup);
            HashMap<String, List<String>> schedulingPolicyGroupData_TM2 = controlsNewUIPage.getDataFromSchedulingPolicyGroups();

            // Get the address of TM
            controlsPage.gotoControlsPage();
            controlsNewUIPage.clickOnControlsLocationProfileSection();
            String nickName1_LocationDetailInfo = controlsNewUIPage.getLocationInfoStringFromDetailPage();
            String nickName2_LocationDetailInfo = controlsNewUIPage.getLocationInfoStringFromDetailPage();

            // Create GFE Announcement and select 2 TMs to get their week summary
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            inboxPage.sendToTM(nickName1);
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            inboxPage.sendToTM(nickName1);
            HashMap<String, String> contentOfWeekSummary_TM1 = inboxPage.getTheContentOfWeekSummaryInGFE();
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            inboxPage.sendToTM(nickName2);
            HashMap<String, String> contentOfWeekSummary_TM2 = inboxPage.getTheContentOfWeekSummaryInGFE();

            // Compare the data to verify the content of week summary when selecting different tm
            SimpleUtils.report("Inbox: Compare a TM with the data from controls");
            if (inboxPage.compareDataInGFEWeekSummary(contentOfWeekSummary_TM1, schedulingPolicyGroupData_TM1) && contentOfWeekSummary_TM1.get("Location").contains(nickName1_LocationDetailInfo))
                SimpleUtils.pass("Inbox: The content of week summary is consistent with the data from controls when selecting a tm");
            else
                SimpleUtils.fail("Inbox: The content of week summary is inconsistent with the data from controls when selecting a tm",false);

            SimpleUtils.report("Inbox: Compare another TM with the data from controls");
            if (inboxPage.compareDataInGFEWeekSummary(contentOfWeekSummary_TM2, schedulingPolicyGroupData_TM2) && contentOfWeekSummary_TM2.get("Location").contains(nickName2_LocationDetailInfo))
                SimpleUtils.pass("Inbox: The content of week summary is consistent with the data from controls when selecting another tm");
            else
                SimpleUtils.fail("Inbox: The content of week summary is inconsistent with the data from controls when selecting another tm",false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    //Added by Marym
    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify turn off GFE ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTurnOffGFEAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully!", controlsNewUIPage.isCompliancePageLoaded(), false);

            controlsNewUIPage.turnGFEToggleOnOrOff(false);

            InboxPage inboxPage = pageFactory.createConsoleInboxPage();
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.checkCreateAnnouncementPageWithGFETurnOnOrTurnOff(false);

            // Go to Analytics page
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            SimpleUtils.assertOnFail("Analytics Page not loaded Successfully!", analyticsPage.isReportsPageLoaded(), false);

            String gfe = "Good Faith Estimate";
            if (!analyticsPage.isSpecificReportLoaded(gfe)){
                SimpleUtils.pass("Analytics: GFE report is not displayed in all locations tab, because GFE is turned off");
            } else{
                SimpleUtils.fail("Analytics: GFE report should not display in all locations tab, because GFE is turned off", true);
            }
            analyticsPage.switchAllLocationsOrSingleLocation(false);
            if (!analyticsPage.isSpecificReportLoaded(gfe)){
                SimpleUtils.pass("Analytics: GFE report is not displayed in location: " + location +" tab, because GFE is turned off");
            } else{
                SimpleUtils.fail("Analytics: GFE report should not display in location: " + location +" tab, because GFE is turned off", true);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }


    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify turn on GFE ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTurnOnGFEAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully!", controlsNewUIPage.isCompliancePageLoaded(), false);

            controlsNewUIPage.turnGFEToggleOnOrOff(true);

            InboxPage inboxPage = pageFactory.createConsoleInboxPage();
            inboxPage.clickOnInboxConsoleMenuItem();

            inboxPage.checkCreateAnnouncementPageWithGFETurnOnOrTurnOff(true);

            // Go to Analytics page
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            SimpleUtils.assertOnFail("Analytics Page not loaded Successfully!", analyticsPage.isReportsPageLoaded(), false);

            String gfe = "Good Faith Estimate";
            if (analyticsPage.isSpecificReportLoaded(gfe)){
                SimpleUtils.pass("Analytics: GFE report loaded successfully in all location tab");
            } else{
                SimpleUtils.fail("Analytics: Analytics: GFE report failed to load in all location tab", true);
            }
            if (analyticsPage.isSpecificReportLoaded(gfe)){
                SimpleUtils.pass("Analytics: GFE report loaded successfully in location: " +location+ " tab");
            } else{
                SimpleUtils.fail("Analytics: Analytics: GFE report failed to load in location: " + location+ "tab", true);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the template loaded when selecting GFE ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheTemplateLoadedWhenSelectingGFEAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsNewUIPage.clickOnControlsConsoleMenu();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("Compliance page not loaded successfully!", controlsNewUIPage.isCompliancePageLoaded(), false);

            controlsNewUIPage.turnGFEToggleOnOrOff(true);

            InboxPage inboxPage = pageFactory.createConsoleInboxPage();
            inboxPage.clickOnInboxConsoleMenuItem();

            inboxPage.createGFEAnnouncement();
            inboxPage.checkCreateGFEPage();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //Added by Haya
    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the content of GFE is consistent between SM and TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyContentOfGFEAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        controlsPage.gotoControlsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
        controlsNewUIPage.clickOnControlsComplianceSection();
        //turn on GFE toggle
        controlsNewUIPage.turnGFEToggleOnOrOff(true);
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //login as TM to get nickName
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String nickNameOfTM = profileNewUIPage.getNickNameFromProfile();
        loginPage.logOut();

        //go to Inbox page create GFE announcement.
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        String nickNameOfSM = profileNewUIPage.getNickNameFromProfile();
        InboxPage inboxPage = pageFactory.createConsoleInboxPage();
        inboxPage.clickOnInboxConsoleMenuItem();
        inboxPage.createGFEAnnouncement();
        inboxPage.sendToTM(nickNameOfTM);

        //Update the message
        String message = "Hi,\nThis is a test message!";
        inboxPage.changeTheMessage(message);
        //change operating hours and working day
        String dayToClose = "MON";
        String dayToChangeHrs = "SUN";
        String startTime = "08:00AM";
        String endTime = "06:00PM";
        inboxPage.chooseOneDayToClose(dayToClose);
        inboxPage.changeOperatingHrsOfDay(dayToChangeHrs, startTime, endTime);
        //change week summary info
        String minimumShifts = "6";
        String averageHrs = "38";
        inboxPage.changeWeekSummaryInfo(minimumShifts, averageHrs);
        inboxPage.clickSendBtn();
        String commentFromSM = "test from SM";
        inboxPage.addComment(commentFromSM);
        loginPage.logOut();

        //login as TM to check the gfe
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        inboxPage.clickOnInboxConsoleMenuItem();
        inboxPage.clickFirstGFEInList();
        inboxPage.verifyMessageIsExpected(message);
        inboxPage.verifyDayIsClosed(dayToClose);
        inboxPage.verifyOperatingHrsOfDay(dayToChangeHrs, startTime, endTime);
        inboxPage.clickAcknowledgeBtn();
        inboxPage.verifyComment(commentFromSM,nickNameOfSM);
        String commentFromTM = "test from TM";
        inboxPage.addComment(commentFromTM);
        loginPage.logOut();

        //login as SM to check the comment TM sent.
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        inboxPage.clickOnInboxConsoleMenuItem();
        inboxPage.clickFirstGFEInList();
        inboxPage.verifyComment(commentFromTM, nickNameOfTM);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "verify TM can see the VSL info")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyVSLInfoAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsComplianceSection();
            //turn on GFE toggle
            controlsNewUIPage.turnVSLToggleOnOrOff(true);
            controlsNewUIPage.turnGFEToggleOnOrOff(true);
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            //login as TM to get nickName
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String nickNameOfTM = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            //go to Inbox page create GFE announcement.
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            String nickNameOfSM = profileNewUIPage.getNickNameFromProfile();
            InboxPage inboxPage = pageFactory.createConsoleInboxPage();
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.createGFEAnnouncement();
            inboxPage.sendToTM(nickNameOfTM);
            inboxPage.clickSendBtn();
            loginPage.logOut();

            //login as TM to check the gfe
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            inboxPage.clickOnInboxConsoleMenuItem();
            inboxPage.clickFirstGFEInList();
            inboxPage.verifyVSLTooltip();
            inboxPage.clickAcknowledgeBtn();
            inboxPage.verifyVSLTooltip();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
