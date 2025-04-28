package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class AvailabilityApprovalRequiredTest extends TestBase {

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

    public enum AvailabilityApprovalRequiredOptions{
        RequiredForAllChanged("Required for all changes"),
        NotRequired("Not required");
        private final String value;
        AvailabilityApprovalRequiredOptions(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate TM and SM can see the dotted lines to check the availability changes for This Week Only")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMAndSMCanSeeTheDottedLinesToCheckTheAvailabilityChangesForThisWeekOnlyAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String tmName = profileNewUIPage.getNickNameFromProfile();
            String myProfileLabel = "My Work Preferences";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);

            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            String weekInfo = profileNewUIPage.getAvailabilityWeek();
            Thread.sleep(3000);
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
            profileNewUIPage.saveMyAvailabilityEditMode("Repeat Forward");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());

            // Set availability policy
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.clickOnGlobalLocationButton();
//            String isApprovalRequired = "Required for all changes";
            controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(tmName);
            profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
            profileNewUIPage.approveAllPendingAvailabilityRequest();
            while (!weekInfo.equalsIgnoreCase(profileNewUIPage.getAvailabilityWeek())){
                profileNewUIPage.clickNextWeek();
            }

            //Delete all availabilities of the first two editable weeks
            Thread.sleep(3000);
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
            profileNewUIPage.saveMyAvailabilityEditMode("This week only");
            profileNewUIPage.clickNextWeek();
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
            profileNewUIPage.saveMyAvailabilityEditMode("This week only");
            loginPage.logOut();

            //Login back to TM and add availabilities
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            profileNewUIPage.getNickNameFromProfile();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(0, "When I prefer to work");
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(1, "When I prefer not to work");
            profileNewUIPage.saveMyAvailabilityEditMode("This week only");
            List<WebElement> changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            List<WebElement> changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                    changedPreferredAvailabilities.size()==1, false);
            SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                    changedBusyAvailabilities.size()==1, false);
            String availabilityWeek1 = profileNewUIPage.getAvailabilityWeek();

            profileNewUIPage.clickNextWeek();
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(0, "When I prefer to work");
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(1, "When I prefer not to work");
            profileNewUIPage.saveMyAvailabilityEditMode("This week only");

            //Check the dotted line
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                    changedPreferredAvailabilities.size()==1, false);
            SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                    changedBusyAvailabilities.size()==1, false);
            String availabilityWeek2 = profileNewUIPage.getAvailabilityWeek();
            loginPage.logOut();
            Thread.sleep(3000);
            //Login as SM and check the dotted line
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(tmName);
            profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
            while (!profileNewUIPage.getAvailabilityWeek().equalsIgnoreCase(availabilityWeek1)){
                profileNewUIPage.clickNextWeek();
            }
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                    changedPreferredAvailabilities.size()==1, false);
            SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                    changedBusyAvailabilities.size()==1, false);

            //Approve the availabilities, the dotted line will disappear
            profileNewUIPage.approveOrRejectSpecificPendingAvailabilityRequest(availabilityWeek1, "Approve");
            Thread.sleep(3000);
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                    changedPreferredAvailabilities.size()==0, false);
            SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                    changedBusyAvailabilities.size()==0, false);

            profileNewUIPage.clickNextWeek();
            Thread.sleep(3000);
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                    changedPreferredAvailabilities.size()==1, false);
            SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                    changedBusyAvailabilities.size()==1, false);

            //Reject the availabilities, the dotted line will disappear
            profileNewUIPage.approveOrRejectSpecificPendingAvailabilityRequest(availabilityWeek2, "Reject");
            Thread.sleep(3000);
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                    changedPreferredAvailabilities.size()==0, false);
            SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                    changedBusyAvailabilities.size()==0, false);
            loginPage.logOut();

            //Login back to TM and check the dotted lines are disappear
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());

            profileNewUIPage.getNickNameFromProfile();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                    changedPreferredAvailabilities.size()==0, false);
            SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                    changedBusyAvailabilities.size()==0, false);

            profileNewUIPage.clickNextWeek();
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                    changedPreferredAvailabilities.size()==0, false);
            SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                    changedBusyAvailabilities.size()==0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate TM and SM can see the dotted lines to check the availability changes for Repeat Forward and dotted will disappear after SM approve request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheDottedLinesOnAvailabilityForRepeatForwardAndApproveAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String action = "Approve";
            verifyTMAndSMCanSeeTheDottedLinesToCheckTheAvailabilityChangesForRepeatForward(action);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate TM and SM can see the dotted lines to check the availability changes for Repeat Forward and dotted will disappear after SM reject request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheDottedLinesOnAvailabilityForRepeatForwardAndRejectAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String action = "Reject";
            verifyTMAndSMCanSeeTheDottedLinesToCheckTheAvailabilityChangesForRepeatForward(action);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void verifyTMAndSMCanSeeTheDottedLinesToCheckTheAvailabilityChangesForRepeatForward(String action) throws Exception {
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Required for all changes";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as TM
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String tmName = profileNewUIPage.getNickNameFromProfile();
        String myProfileLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        //Delete all availabilities of the first editable week
        Thread.sleep(3000);
        profileNewUIPage.clickAvailabilityEditButton();
        profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
        profileNewUIPage.saveMyAvailabilityEditMode("Repeat Forward");
        loginPage.logOut();

        //Login as SM and approve all the pending request of the TM
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMemberByName(tmName);
        profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
        Thread.sleep(5000);
        profileNewUIPage.approveAllPendingAvailabilityRequest();
        loginPage.logOut();

        //Login back to TM and add availabilities
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        profileNewUIPage.getNickNameFromProfile();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
            Thread.sleep(2000);
        }
        profileNewUIPage.clickAvailabilityEditButton();
        profileNewUIPage.updatePreferredOrBusyHoursToAllDay(0, "When I prefer to work");
        profileNewUIPage.updatePreferredOrBusyHoursToAllDay(1, "When I prefer not to work");
        profileNewUIPage.saveMyAvailabilityEditMode("Repeat Forward");
        List<WebElement> changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        List<WebElement> changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                changedPreferredAvailabilities.size()==1, false);
        SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                changedBusyAvailabilities.size()==1, false);
        String availabilityWeek1 = profileNewUIPage.getAvailabilityWeek();
        //Check the dotted line on next week
        profileNewUIPage.clickNextWeek();
        changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                changedPreferredAvailabilities.size()==1, false);
        SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                changedBusyAvailabilities.size()==1, false);
        loginPage.logOut();

        //Login as SM and check the dotted line
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMemberByName(tmName);
        profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
        while (!profileNewUIPage.getAvailabilityWeek().equalsIgnoreCase(availabilityWeek1)){
            profileNewUIPage.clickNextWeek();
        }
        changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                changedPreferredAvailabilities.size()==1, false);
        SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                changedBusyAvailabilities.size()==1, false);
        profileNewUIPage.clickNextWeek();
        Thread.sleep(3000);
        changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                changedPreferredAvailabilities.size()==1, false);
        SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                changedBusyAvailabilities.size()==1, false);

        //Approve the availabilities, the dotted line will disappear
        String onwardWeekInfo = availabilityWeek1.split("-")[0]+ "-" + "ONWARD";
        profileNewUIPage.approveOrRejectSpecificPendingAvailabilityRequest(onwardWeekInfo, action);
        Thread.sleep(3000);
        changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                changedPreferredAvailabilities.size()==0, false);
        SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                changedBusyAvailabilities.size()==0, false);

        profileNewUIPage.clickPreviousWeek();
        changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                changedPreferredAvailabilities.size()==0, false);
        SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                changedBusyAvailabilities.size()==0, false);
        loginPage.logOut();
        //Login back to TM and check the dotted lines are disappear
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());

        profileNewUIPage.getNickNameFromProfile();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                changedPreferredAvailabilities.size()==0, false);
        SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                changedBusyAvailabilities.size()==0, false);

        profileNewUIPage.clickNextWeek();
        changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
        changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
        SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                changedPreferredAvailabilities.size()==0, false);
        SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                changedBusyAvailabilities.size()==0, false);
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the notification when TM updates availability from a week onwards with config Not required")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyNotificationForUpdateAvailabilityRepeatForwardWithConfNOAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            // Login with Store Manager Credentials
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            // Set availability policy
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.clickOnGlobalLocationButton();
//            String isApprovalRequired = "Not required";
            controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.NotRequired.getValue());
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            //Login as Team Member to change availability
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String requestUserName = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            // Login as Internal Admin again
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            // Go to Team Roster, search the team member
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.searchAndSelectTeamMemberByName(requestUserName);
            String workPreferencesLabel = "Work Preferences";
            profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
            profileNewUIPage.approveAllPendingAvailabilityRequest();
            loginPage.logOut();

            // Login as Team Member again
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            profileNewUIPage.getNickNameFromProfile();
            String myWorkPreferencesLabel = "My Work Preferences";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
            //Update Preferred And Busy Hours
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            String weekInfo = profileNewUIPage.getAvailabilityWeek();
            String repeatChanges = "This week only";
            String leftOrRightDuration = "Right";
            String hoursType = "When I prefer to work";
            HashMap<String, Object> myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();
            if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
                int sliderIndex = 0;
                double hours = -0.5;//move 1 metric 0.5h left
                profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                        hours, repeatChanges);
            } else {
                profileNewUIPage.clickAvailabilityEditButton();
                profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
                profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
            }
            loginPage.logOut();

            // Login as Store Manager again to check message
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
            String requestAwailabilityChangeLabel = "request";
            activityPage.verifyNotificationForUpdateAvailability(requestUserName,AvailabilityApprovalRequiredOptions.NotRequired.getValue(),
                    requestAwailabilityChangeLabel,weekInfo,repeatChanges);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the notification when TM updates availability for a specific week with config Not required")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyNotificationForUpdateAvailability4SpecificWeekWithConfNOAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            // Login with Store Manager Credentials
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            // Set availability policy
            Thread.sleep(5000);
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.clickOnGlobalLocationButton();
//            String isApprovalRequired = "Not required";
            controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.NotRequired.getValue());
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            //Login as Team Member to change availability
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String requestUserName = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            // Login as Internal Admin again
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            // Go to Team Roster, search the team member
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.searchAndSelectTeamMemberByName(requestUserName);
            String workPreferencesLabel = "Work Preferences";
            profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
            profileNewUIPage.approveAllPendingAvailabilityRequest();
            loginPage.logOut();

            // Login as Team Member again
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            profileNewUIPage.getNickNameFromProfile();
            String myWorkPreferencesLabel = "My Work Preferences";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
            //Update Preferred And Busy Hours
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            String weekInfo = profileNewUIPage.getAvailabilityWeek();
            String repeatChanges = "This week only";
            String leftOrRightDuration = "Right";
            String hoursType = "When I prefer to work";
            HashMap<String, Object> myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();
            if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
                int sliderIndex = 0;
                double hours = -0.5;//move 1 metric 0.5h left
                profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                        hours, repeatChanges);
            } else {
                profileNewUIPage.clickAvailabilityEditButton();
                profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
                profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
            }
            loginPage.logOut();

            // Login as Store Manager again to check message
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
            String requestAwailabilityChangeLabel = "request";
            activityPage.verifyNotificationForUpdateAvailability(requestUserName,AvailabilityApprovalRequiredOptions.NotRequired.getValue(),
                    requestAwailabilityChangeLabel,weekInfo,repeatChanges);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Haya&Lizzy")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the notification when TM requests availability for a specific week")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyNotificationForUpdateAvailability4SpecificWeekWithConfYesAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            // Login with Store Manager Credentials
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            // Set availability policy
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            controlsNewUIPage.clickOnGlobalLocationButton();
//            String isApprovalRequired = "Required for all changes";
            controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            //Login as Team Member to change availability
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String requestUserName = profileNewUIPage.getNickNameFromProfile();
            loginPage.logOut();

            // Login as Internal Admin again
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            // Go to Team Roster, search the team member
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.searchAndSelectTeamMemberByName(requestUserName);
            String workPreferencesLabel = "Work Preferences";
            profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
            profileNewUIPage.approveAllPendingAvailabilityRequest();
            loginPage.logOut();

            // Login as Team Member again
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            profileNewUIPage.getNickNameFromProfile();
            String myWorkPreferencesLabel = "My Work Preferences";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
            //Update Preferred And Busy Hours
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            String weekInfo = profileNewUIPage.getAvailabilityWeek();
            String repeatChanges = "This week only";
            String leftOrRightDuration = "Right";
            String hoursType = "When I prefer to work";
            HashMap<String, Object> myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();
            if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
                int sliderIndex = 1;
                double hours = -0.5;//move 1 metric 0.5h left
                profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                        hours, repeatChanges);
            } else {
                profileNewUIPage.clickAvailabilityEditButton();
                profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
                profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
            }
            loginPage.logOut();

            // Login as Store Manager again to check message
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            String respondUserName = profileNewUIPage.getNickNameFromProfile();
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyClickOnActivityIcon();
            //check and click the go to profile link
            activityPage.goToProfileLinkOnActivity();
            //check the week data
            profileNewUIPage.verifyAvailabilityWeek(weekInfo);
            //click the activity bell to view the profile update again
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
            String requestAwailabilityChangeLabel = "requested";
            activityPage.verifyNotificationForUpdateAvailability(requestUserName,
                    AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue(),
                    requestAwailabilityChangeLabel,weekInfo,repeatChanges);
            activityPage.approveOrRejectTTimeOffRequestOnActivity(requestUserName,respondUserName, ActivityTest.approveRejectAction.Approve.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Haya&Lizzy")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify the notification when TM requests availability from a week onwards")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyNotificationForUpdateAvailabilityRepeatForwardWithConfYesAsTeamMember(String browser, String username, String password, String location) throws Exception{
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String requestUserName = profileNewUIPage.getNickNameFromProfile();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            // Set availability policy

            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            String respondUserName = profileNewUIPage.getNickNameFromProfile();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            dashboardPage.navigateToDashboard();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);

            controlsNewUIPage.clickOnGlobalLocationButton();
//            String isApprovalRequired = "Required for all changes";
            controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());

            // Go to Team Roster, search the team member
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.searchAndSelectTeamMemberByName(requestUserName);
            String workPreferencesLabel = "Work Preferences";
            profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
            profileNewUIPage.approveAllPendingAvailabilityRequest();
            loginPage.logOut();

            // Login as Team Member again
            //Login as Team Member to change availability
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            profileNewUIPage.getNickNameFromProfile();
            String myWorkPreferencesLabel = "My Work Preferences";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
            //Update Preferred And Busy Hours
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            String weekInfo = profileNewUIPage.getAvailabilityWeek();
            String repeatChanges = "This week only";
            String leftOrRightDuration = "Right";
            String hoursType = "When I prefer to work";
            HashMap<String, Object> myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();
            if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
                int sliderIndex = 1;
                double hours = -0.5;//move 1 metric 0.5h left
                profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                        hours, repeatChanges);
            } else {
                profileNewUIPage.clickAvailabilityEditButton();
                profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
                profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
            }
            loginPage.logOut();
            // Login as Store Manager again to check message
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyClickOnActivityIcon();
            //check and click the go to profile link
            activityPage.goToProfileLinkOnActivity();
            //check the week data
            profileNewUIPage.verifyAvailabilityWeek(weekInfo);
            //click the activity bell to view the profile update again
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
            String requestAwailabilityChangeLabel = "requested";
            activityPage.verifyNotificationForUpdateAvailability(requestUserName,
                    AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue(),
                    requestAwailabilityChangeLabel,weekInfo,repeatChanges);
            activityPage.approveOrRejectTTimeOffRequestOnActivity(requestUserName,respondUserName, ActivityTest.approveRejectAction.Reject.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate create change availability request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateCreateChangeAvailabilityRequestAsStoreManager(String browser, String username, String password, String location) throws Exception {
        // Login with Store Manager Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        controlsPage.gotoControlsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        dashboardPage.navigateToDashboard();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Required for all changes";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        String weekInfo = profileNewUIPage.getAvailabilityWeek();
        int sliderIndex = 1;
        double hours = 0.5;//move 1 metric 0.5h right----increase
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        String repeatChanges = "This week only";
        profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        profileNewUIPage.verifyTheLatestAvailabilityRequestInfo(weekInfo, hours, repeatChanges);
        profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        profileNewUIPage.verifyTheLatestAvailabilityRequestInfo(weekInfo, hours*2, repeatChanges);

        //cancel all availability change requests again.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        hours = -0.5;//move 1 metric 0.5h left----decrease
        leftOrRightDuration = "Left";
        hoursType = "When I prefer to work";
        repeatChanges = "repeat forward";
        profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        profileNewUIPage.verifyTheLatestAvailabilityRequestInfo(weekInfo, hours, repeatChanges);
        profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        profileNewUIPage.verifyTheLatestAvailabilityRequestInfo(weekInfo, hours*2, repeatChanges);
        SimpleUtils.assertOnFail("Pending count should be 1.", "1".equalsIgnoreCase(profileNewUIPage.getCountForStatus("pending").trim()), false);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate cancel Change Availability Request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateCancelAvailabilityRequestAsStoreManager(String browser, String username, String password, String location) throws Exception {
        // Login with Store Manager Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        controlsPage.gotoControlsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        dashboardPage.navigateToDashboard();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Required for all changes";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        String weekInfo = profileNewUIPage.getAvailabilityWeek();
        int sliderIndex = 1;
        double hours = 0.5;//move 1 metric 0.5h right----increase
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        String repeatChanges = "This week only";
        profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        profileNewUIPage.verifyTheLatestAvailabilityRequestInfo(weekInfo, hours, repeatChanges);
        SimpleUtils.assertOnFail("Pending count should be 1.", "1".equalsIgnoreCase(profileNewUIPage.getCountForStatus("pending").trim()), false);
        //cancel availability change requests.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        SimpleUtils.assertOnFail("Pending count should be 0.", "0".equalsIgnoreCase(profileNewUIPage.getCountForStatus("pending").trim()), false);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate SM approve change availability request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateApproveAvailabilityRequestAsStoreManager(String browser, String username, String password, String location) throws Exception {
        // Login with Store Manager Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        //Go to OP page
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.searchLocation(location);               ;
        SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
        locationsPage.clickOnLocationInLocationResult(location);
        locationsPage.clickOnConfigurationTabOfLocation();
        HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();

        configurationPage.goToConfigurationPage();
        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Scheduling Policies"));
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        configurationPage.publishNowTheTemplate();
        Thread.sleep(3000);
        switchToConsoleWindow();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String requestUserName = profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        String weekInfo = profileNewUIPage.getAvailabilityWeek();
        String repeatChanges = "This week only";
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        HashMap<String, Object> myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();

        if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
            int sliderIndex = 1;
            double hours = -0.5;//move 1 metric 0.5h left
            profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                    hours, repeatChanges);
        } else {
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
            profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
        }
        String newAvailableHrs = profileNewUIPage.getAvailableHoursForSpecificWeek();
        loginPage.logOut();

        //Login as store manager to approve the request.
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(requestUserName);
        String workPreferencesLabel = "Work Preferences";
        profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
        String approvedCount = profileNewUIPage.getCountForStatus("approved");
        profileNewUIPage.approveOrRejectSpecificPendingAvailabilityRequest(weekInfo, "Approve");
        if (SimpleUtils.isNumeric(approvedCount)){
            int approvedNum = Integer.valueOf(approvedCount);
            SimpleUtils.assertOnFail("Approved count should be 1 more than before.", String.valueOf((approvedNum+1)).equalsIgnoreCase(profileNewUIPage.getCountForStatus("approved").trim()), false);
        } else {
            SimpleUtils.fail("Count is not numeric", false);
        }
        while (!weekInfo.equalsIgnoreCase(profileNewUIPage.getAvailabilityWeek())){
            profileNewUIPage.clickNextWeek();
        }
        SimpleUtils.assertOnFail("Available hours didn't change to the new version!", newAvailableHrs.equalsIgnoreCase(profileNewUIPage.getAvailableHoursForSpecificWeek()), false);
        SimpleUtils.assertOnFail("Pending count should be 0.", "0".equalsIgnoreCase(profileNewUIPage.getCountForStatus("pending").trim()), false);

        //SCH-4997
        //Go to reject a approved request.
//		profileNewUIPage.rejectSpecificApprovedAvailabilityRequest(weekInfo);
//		SimpleUtils.assertOnFail("Available hours should change back to the old version!", oldAvailableHrs.equalsIgnoreCase(profileNewUIPage.getAvailableHoursForSpecificWeek()), false);
//		SimpleUtils.assertOnFail("Approved count should be the same as before.", approvedCount.equalsIgnoreCase(profileNewUIPage.getCountForStatus("approved").trim()), false);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate cancelled/approved/rejected and dated request has no option when clicking the request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateCancelledAvailabilityHasNoOptionRequestAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        // Login with Store Manager Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        //Go to OP page
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.searchLocation(location);               ;
        SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
        locationsPage.clickOnLocationInLocationResult(location);
        locationsPage.clickOnConfigurationTabOfLocation();
        HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();

        configurationPage.goToConfigurationPage();
        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Scheduling Policies"));
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        configurationPage.publishNowTheTemplate();
        Thread.sleep(3000);
        switchToConsoleWindow();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String requestUserName = profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        String repeatChanges = "This week only";
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        HashMap<String, Object> myAvailabilityData =  profileNewUIPage.getMyAvailabilityData();
        if (Float.parseFloat(myAvailabilityData.get("totalHoursValue").toString()) != 0) {
            int sliderIndex = 1;
            double hours = -0.5;//move 1 metric 0.5h left
            profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                    hours, repeatChanges);
        } else {
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(3, hoursType);
            profileNewUIPage.saveMyAvailabilityEditMode(repeatChanges);
        }
        loginPage.logOut();

        //Login as store manager to check cancelled request.
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(requestUserName);
        String workPreferencesLabel = "Work Preferences";
        profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
        profileNewUIPage.verifyClickCancelledAvalabilityRequest();

    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate SM reject change availability request")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateRejectAvailabilityRequestAsStoreManager(String browser, String username, String password, String location) throws Exception {
        // Login with Store Manager Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        controlsPage.gotoControlsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        dashboardPage.navigateToDashboard();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Required for all changes";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String requestUserName = profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        String weekInfo = profileNewUIPage.getAvailabilityWeek();
        String oldAvailableHrs = profileNewUIPage.getAvailableHoursForSpecificWeek();
        int sliderIndex = 1;
        double hours = 0.5;//move 1 metric 0.5h left----decrease
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        String repeatChanges = "This week only";
        String newAvailableHrs = profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        loginPage.logOut();

        //Login as store manager to reject the request.
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(requestUserName);
        String workPreferencesLabel = "Work Preferences";
        profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
        String rejectedCount = profileNewUIPage.getCountForStatus("rejected");
        profileNewUIPage.approveOrRejectSpecificPendingAvailabilityRequest(weekInfo, "Reject");
        if (SimpleUtils.isNumeric(rejectedCount)){
            int rejectedNum = Integer.valueOf(rejectedCount);
            SimpleUtils.assertOnFail("Rejected count should be 1 more than before.", String.valueOf((rejectedNum+1)).equalsIgnoreCase(profileNewUIPage.getCountForStatus("rejected").trim()), false);
        } else {
            SimpleUtils.fail("Count is not numeric", false);
        }
        while (!weekInfo.equalsIgnoreCase(profileNewUIPage.getAvailabilityWeek())){
            profileNewUIPage.clickNextWeek();
        }
        SimpleUtils.assertOnFail("Available hours didn't change to the old version!", oldAvailableHrs.equalsIgnoreCase(profileNewUIPage.getAvailableHoursForSpecificWeek()), false);
        SimpleUtils.assertOnFail("Pending count should be 0.", "0".equalsIgnoreCase(profileNewUIPage.getCountForStatus("pending").trim()), false);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate create change Availability request without approval")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateCreateAvailabilityRequestWithoutApprovalAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        // Login with Internal Admin Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        controlsPage.gotoControlsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Not required";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.NotRequired.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String requestUserName = profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfilePageSubSectionByLabel(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        String weekInfo = profileNewUIPage.getAvailabilityWeek();
        int sliderIndex = 1;
        double hours = 0.5;//move 1 metric 0.5h left----decrease
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        String repeatChanges = "This week only";
        String newAvailableHrs = profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        SimpleUtils.assertOnFail("Available hours should be the new version.", newAvailableHrs.equalsIgnoreCase(profileNewUIPage.getAvailableHoursForSpecificWeek()), false);
        loginPage.logOut();

        //Login as admin to check and set back the setting.
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(requestUserName);
        String workPreferencesLabel = "Work Preferences";
        profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
        while (!profileNewUIPage.getAvailabilityWeek().equalsIgnoreCase(weekInfo)){
            profileNewUIPage.clickNextWeek();
        }
        SimpleUtils.assertOnFail("Available hours should be the new version.", newAvailableHrs.equalsIgnoreCase(profileNewUIPage.getAvailableHoursForSpecificWeek()), false);


        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        isApprovalRequired = "Required for all changes";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate there is error warning when recurring request exists when SM edit the availabilities for TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateErrorMessageWhenThereIsRecurringPendingAvailabilityRequestAsStoreManager(String browser, String username, String password, String location) throws Exception {
        // Login with Store Manager Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        controlsPage.gotoControlsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        dashboardPage.navigateToDashboard();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Required for all changes";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String requestUserName = profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        int sliderIndex = 1;
        double hours = 0.5;//move 1 metric 0.5h right----increase
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        String repeatChanges = "repeat forward";
        profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        loginPage.logOut();

        //log in as SM, go to the Roster page, search out the TM.
        loginToLegionAndVerifyIsLoginDone(username, password, location);
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(requestUserName);
        String workPreferencesLabel = "Work Preferences";
        profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
        profileNewUIPage.clickAvailabilityEditButton();
        SimpleUtils.assertOnFail("Error message should show up!", profileNewUIPage.verifyErrorMessageForEditAvailabilityShowsUpOrNot(), false);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate there is no error warning when no overlap exists when SM edit the availabilities for TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void validateErrorMessageWhenThereIsNoRecurringPendingAvailabilityRequestAsStoreManager(String browser, String username, String password, String location) throws Exception {
        // Login with Store Manager Credentials
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        // Set availability policy
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        controlsPage.gotoControlsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        dashboardPage.navigateToDashboard();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
//        String isApprovalRequired = "Required for all changes";
        controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(AvailabilityApprovalRequiredOptions.RequiredForAllChanged.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        //Login as Team Member to change availability
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String requestUserName = profileNewUIPage.getNickNameFromProfile();
        String myWorkPreferencesLabel = "My Work Preferences";
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
        //cancel all availability change requests firstly.
        profileNewUIPage.cancelAllPendingAvailabilityRequest();
        //Update Preferred Hours
        while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
            profileNewUIPage.clickNextWeek();
        }
        int sliderIndex = 1;
        double hours = 0.5;//move 1 metric 0.5h right----increase
        String leftOrRightDuration = "Right";
        String hoursType = "When I prefer to work";
        String repeatChanges = "This week only";
        profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
                hours, repeatChanges);
        loginPage.logOut();

        //log in as SM, go to the Roster page, search out the TM.
        loginToLegionAndVerifyIsLoginDone(username, password, location);
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(requestUserName);
        String workPreferencesLabel = "Work Preferences";
        profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
        profileNewUIPage.clickAvailabilityEditButton();
        SimpleUtils.assertOnFail("Error message shouldn't show up!", !profileNewUIPage.verifyErrorMessageForEditAvailabilityShowsUpOrNot(), false);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate the dotted lins will disappear for TM and SM after TM cancel the availability")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheDottedLinesWillDisappearForTMAndSMAfterTMCancelAvailabilityAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            //Login as TM
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String tmName = profileNewUIPage.getNickNameFromProfile();
            String myProfileLabel = "My Work Preferences";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            //Get the editable week info
            String availabilityWeek = profileNewUIPage.getAvailabilityWeek();
            profileNewUIPage.cancelAllPendingAvailabilityRequest();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            //Login as SM and approve all the pending request of the TM
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(tmName);
            profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
            profileNewUIPage.approveAllPendingAvailabilityRequest();
            while (!profileNewUIPage.getAvailabilityWeek().equalsIgnoreCase(availabilityWeek)){
                profileNewUIPage.clickNextWeek();
            }
            Thread.sleep(5000);
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.deleteAllAvailabilitiesForCurrentWeek();
            profileNewUIPage.saveMyAvailabilityEditMode("Repeat Forward");
            loginPage.logOut();

            //Login back to TM and add availabilities
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            profileNewUIPage.getNickNameFromProfile();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            profileNewUIPage.clickAvailabilityEditButton();
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(0, "When I prefer to work");
            profileNewUIPage.updatePreferredOrBusyHoursToAllDay(1, "When I prefer not to work");
            profileNewUIPage.saveMyAvailabilityEditMode("Repeat Forward");
            List<WebElement> changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            List<WebElement> changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                    changedPreferredAvailabilities.size()==1, false);
            SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                    changedBusyAvailabilities.size()==1, false);
            String availabilityWeek1 = profileNewUIPage.getAvailabilityWeek();
            loginPage.logOut();

            //Login as SM and check the dotted line
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(tmName);
            profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
            while (!profileNewUIPage.getAvailabilityWeek().equalsIgnoreCase(availabilityWeek1)){
                profileNewUIPage.clickNextWeek();
            }
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities fail to load! ",
                    changedPreferredAvailabilities.size()==1, false);
            SimpleUtils.assertOnFail("The changed busy availabilities fail to load! ",
                    changedBusyAvailabilities.size()==1, false);

            loginPage.logOut();
            //Login back to TM and check the dotted lines are disappear
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());

            profileNewUIPage.getNickNameFromProfile();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
                profileNewUIPage.clickNextWeek();
            }
            String onwardWeekInfo = availabilityWeek1.split("-")[0]+ "-" + "ONWARD";
            profileNewUIPage.cancelSpecificPendingAvailabilityRequest(onwardWeekInfo);
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            Thread.sleep(3000);
            SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                    changedPreferredAvailabilities.size()==0, false);
            SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                    changedBusyAvailabilities.size()==0, false);

            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(tmName);
            profileNewUIPage.selectProfilePageSubSectionByLabel("Work Preferences");
            while (!profileNewUIPage.getAvailabilityWeek().equalsIgnoreCase(availabilityWeek1)){
                profileNewUIPage.clickNextWeek();
            }
            changedPreferredAvailabilities = profileNewUIPage.getChangedPreferredAvailabilities();
            changedBusyAvailabilities = profileNewUIPage.getChangedBusyAvailabilities();
            SimpleUtils.assertOnFail("The changed preferred availabilities should not load! ",
                    changedPreferredAvailabilities.size()==0, false);
            SimpleUtils.assertOnFail("The changed busy availabilities should not load! ",
                    changedBusyAvailabilities.size()==0, false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
