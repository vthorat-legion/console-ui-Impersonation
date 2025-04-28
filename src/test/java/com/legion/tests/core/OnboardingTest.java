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
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.text.StyledEditorKit;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.legion.utils.MyThreadLocal.*;

public class OnboardingTest extends TestBase {

   int a =10;
    private static HashMap<String, String> testDataMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/OnboardingTestData.json");
    private String nonSSOEnterprise = propertyMap.get("KendraScott2_Enterprise");
    private String ssoEnterprise = propertyMap.get("Dgch_Enterprise");
    private String opEnabledEnterprise = propertyMap.get("CinemarkWkdy_Enterprise");
    private String currentLocation = "";
    private String invitationCode = "";
    private String newPassword = testDataMap.get("Password");
    private String continueLabel = "Continue";
    private String nextLabel = "Next";
    private boolean hasCompanyMobilePolicyURL = false;
    private boolean isSetActive = false;
    private String rcEnv = "rc-enterprise.dev.legion.work";
    private String firstName = "";

    private static Map<String, String> newTMDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/AddANewTeamMember.json");
    int c = 20;
    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            verifyLoginToTheSpecificLocation((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the onboarding flow for New hire and status changed to Active (Non-SSO)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheOnboardingFlowForNewHireAndStatusChangeToActiveAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            verifyOnboardingFlow("Yes", username, password);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the onboarding flow for New hire and status changed to Onboarded (Non-SSO)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheOnboardingFlowForNewHireAndStatusChangeToOnboardedAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            verifyOnboardingFlow("No", username, password);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the onboarding flow when the rehired user has an account and status change to Onboarded (Non-SSO)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheOnboardingFlowForRehireAndStatusChangeToOnboardedAsInternalAdmin(String browser, String username, String password, String location){
        try {
            verifyOnboardingFlow("No", username, password);
            verifyOnboardingFlowForRehire("No", username, password);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Verify the onboarding flow when the rehired user has an account and status change to Active (Non-SSO)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheOnboardingFlowForRehireAndStatusChangeToActiveAsInternalAdmin(String browser, String username, String password, String location){
        try {
            verifyOnboardingFlow("Yes", username, password);
            verifyOnboardingFlowForRehire("Yes", username, password);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the onboarding flow for New hire (Non-SSO & OP based)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheOnboardingFlowForNewHireOnOPEnabledEnvAsInternalAdmin(String browser, String username, String password, String location){
        try {
            verifyOnboardingFlowOnOPEnabled(username, password);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the onboarding flow for Rehire (Non-SSO & OP based)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheOnboardingFlowForReHireOnOPEnabledEnvAsInternalAdmin(String browser, String username, String password, String location){
        try {
            verifyOnboardingFlowOnOPEnabled(username, password);
            verifyOnboardingFlowForRehireOnOPEnabled(username, password);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void verifyOnboardingFlowOnOPEnabled(String username, String password) throws Exception {
        // Initial the pages
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();

        // Switch to OP side to get the setting for current location
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.searchLocation(currentLocation);               ;
        SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(currentLocation), false);
        locationsPage.clickOnLocationInLocationResult(currentLocation);
        locationsPage.clickOnConfigurationTabOfLocation();
        HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();

        configurationPage.goToConfigurationPage();
        controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
        cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Schedule Collaboration"));
        isSetActive = controlsNewUIPage.getTheSettingForAutomaticallySetOnboardedEmployeesToActive();
        configurationPage.goToConfigurationPage();
        controlsNewUIPage.clickOnControlsComplianceSection();
        cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Compliance"));
        hasCompanyMobilePolicyURL = controlsNewUIPage.hasCompanyMobilePolicyURLOrNot();
        switchToConsoleWindow();

        createNewUserAndInvite();
        verifyOnboardingFlowForNewOrReHire(false);

        String status = profileNewUIPage.getStatusOnProfilePage();

        if(isSetActive){
            SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Active, but actual display as "+ status,
                    status.equalsIgnoreCase("Active"), false);
        } else
            SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Onboarded, but actual display as "+ status,
                    status.equalsIgnoreCase("Onboarded"), false);

        // Logout and login as internal admin to terminate the user
        loginAsInternalAdminAndTerminateUser(username, password);
    }

    private void verifyOnboardingFlowForRehireOnOPEnabled(String username, String password) throws Exception {
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();

        createNewUserAndInvite();

        verifyOnboardingFlowForNewOrReHire(true);

        String status = profileNewUIPage.getStatusOnProfilePage();

        if(isSetActive){
            SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Active, but actual display as "+ status,
                    status.equalsIgnoreCase("Active"), false);
        } else
            SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Onboarded, but actual display as "+ status,
                    status.equalsIgnoreCase("Onboarded"), false);

        // Logout and login as internal admin to terminate the user
        loginAsInternalAdminAndTerminateUser(username, password);
    }

    private void verifyOnboardingFlow (String yesOrNo, String username, String password) throws Exception {
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        // Set "Automatically set onboarded employees to active?"
        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
        controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
        SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
        controlsNewUIPage.setAutomaticallySetOnboardedEmployeesToActive(yesOrNo);

        //Check if there is the url set under Controls -> Compliance -> Company Mobile Policy
        controlsPage.gotoControlsPage();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
        controlsNewUIPage.clickOnControlsComplianceSection();
        SimpleUtils.assertOnFail("Controls: Compliance page not loaded Successfully!", controlsNewUIPage.isControlsComplianceLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
        hasCompanyMobilePolicyURL = controlsNewUIPage.hasCompanyMobilePolicyURLOrNot();

        createNewUserAndInvite();

        verifyOnboardingFlowForNewOrReHire(false);
        String status = profileNewUIPage.getStatusOnProfilePage();

        if(yesOrNo.equalsIgnoreCase("Yes")){
        SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Active, but actual display as "+ status,
                status.equalsIgnoreCase("Active"), false);
        } else
            SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Onboarded, but actual display as "+ status,
                    status.equalsIgnoreCase("Onboarded"), false);

        // Logout and login as internal admin to terminate the user
        loginAsInternalAdminAndTerminateUser(username, password);
    }

    private void verifyOnboardingFlowForRehire(String yesOrNo, String username, String password) throws Exception {
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();

        createNewUserAndInvite();

        verifyOnboardingFlowForNewOrReHire(true);

        String status = profileNewUIPage.getStatusOnProfilePage();

        if(yesOrNo.equalsIgnoreCase("Yes")){
            SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Active, but actual display as "+ status,
                    status.equalsIgnoreCase("Active"), false);
        } else
            SimpleUtils.assertOnFail("The user status display incorrectly! It should display as: Onboarded, but actual display as "+ status,
                    status.equalsIgnoreCase("Onboarded"), false);

        // Logout and login as internal admin to terminate the user
        loginAsInternalAdminAndTerminateUser(username, password);
    }

    private void verifyOnboardingFlowForNewOrReHire(boolean isRehire) throws Exception {
        OnboardingPage onboardingPage = pageFactory.createOnboardingPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();

        onboardingPage.openOnboardingPage(invitationCode, firstName, isRehire, getEnterprise());
        if (isRehire) {
            // Verify the content of "Log in to your account" page
            onboardingPage.verifyTheContentOfLoginToYourAccountPage();
            // Verify the rehire can login to the previous legion credential successfully
            onboardingPage.verifyRehireLoginToPreviousCredential(getEmailAccount(), newPassword);
        } else {
            String lastName = MyThreadLocal.getLastNameForNewHire();
            onboardingPage.openOnboardingPage(invitationCode, firstName, false, "KendraScott2");
            onboardingPage.verifyTheContentOfCreateAccountPage(firstName, invitationCode);
            onboardingPage.verifyLastName(lastName);
            SimpleUtils.assertOnFail("Create Account page failed to load after verifying last name!", onboardingPage.isCreateAccountPageLoadedAfterVerifyingLastName(), false);
            onboardingPage.createAccountForNewHire(newPassword);
            // Verify "Is this email correct?" dialog pops up
            onboardingPage.verifyIsEmailCorrectDialogPopup();
            // Verify the functionality of YES button
            onboardingPage.clickYesBtnOnIsEmailCorrectDialog();
        }
        // Verify the content on Important Notice from your Employer page
        if (hasCompanyMobilePolicyURL) {
            onboardingPage.verifyImportantNoticeFromYourEmployerPageLoaded();
            onboardingPage.clickOnButtonByLabel(continueLabel);
        }
        // Verify the content on Verify Profile page
        onboardingPage.validateVerifyProfilePageLoaded();
        onboardingPage.clickOnButtonByLabel(nextLabel);

        //Verify the content on Set Availability page
        onboardingPage.verifySetAvailabilityPageLoaded();
        onboardingPage.clickOnNextButtonOnSetAvailabilityPage();
        onboardingPage.verifyThatsItPageLoaded();
        onboardingPage.clickOnDoneOnThatsItPage();
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.verifyNewTermsOfServicePopUp();

        //Check user status from profile page
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
    }

    private void createNewUserAndInvite() throws Exception {
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        TeamPage teamPage = pageFactory.createConsoleTeamPage();

        // Go to Team page, to create the new user for onboarding
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.verifyTheFunctionOfAddNewTeamMemberButton();
        teamPage.isProfilePageLoaded();
        firstName = teamPage.addANewTeamMemberToInvite(newTMDetails);
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();

        //If testing on rc, set "Preview User" for this user
        if (getDriver().getCurrentUrl().contains(rcEnv)){
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsUsersAndRolesSection();
            controlsNewUIPage.searchAndSelectTeamMemberByName(firstName);
            List<String> selectAccessRoles = new ArrayList<>();
            selectAccessRoles.add("Preview User");
            controlsNewUIPage.selectAccessRoles(selectAccessRoles);
        }

        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMemberByName(firstName);
        // Verify manager can invite the user to use Legion, click Re invite to Legion button
        profileNewUIPage.userProfileInviteTeamMember();
        //Get invitation code
        profileNewUIPage.clickOnShowOrHideInvitationCodeButton(true);
        invitationCode = profileNewUIPage.getInvitationCode();
    }

    private void loginAsInternalAdminAndTerminateUser(String username, String password) throws Exception {
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        TeamPage teamPage = pageFactory.createConsoleTeamPage();

        loginPage.logOut();
        verifyLoginToTheSpecificLocation(username, password, currentLocation);
        teamPage.goToTeam();
        teamPage.searchAndSelectTeamMemberByName(firstName);
        teamPage.terminateTheTeamMember(true);
    }

    private void verifyLoginToTheSpecificLocation(String username, String password, String location) throws Exception {
        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield(username, password, location);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        if (MyThreadLocal.getEnterprise().equalsIgnoreCase(nonSSOEnterprise)) {
            currentLocation = testDataMap.get("Non-SSO_Location");
            locationSelectorPage.changeUpperFields(testDataMap.get("Non-SSO_Upperfields"));
        } else if (MyThreadLocal.getEnterprise().equalsIgnoreCase(ssoEnterprise)) {
            currentLocation = testDataMap.get("SSO_Location");
            locationSelectorPage.changeUpperFields(testDataMap.get("SSO_Upperfields"));
        } else if (MyThreadLocal.getEnterprise().equalsIgnoreCase(opEnabledEnterprise)) {
            currentLocation = testDataMap.get("Non-SSO_Location");
            locationSelectorPage.changeUpperFields(testDataMap.get("OPEnabled_Upperfields"));
        }
        locationSelectorPage.changeLocation(currentLocation);
        boolean isLoginDone = loginPage.isLoginDone();
        loginPage.verifyLoginDone(isLoginDone, currentLocation);
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate TM onboarding with WFSG enabled and toggle on")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMOnboardingWithWFSGEnabledAndToggleOnAsInternalAdmin(String browser, String username, String password, String location){
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            verifyTMOnboardingWithWFSGEnabledAndToggleOnOrOff("Yes", true);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate TM onboarding with WFSG enabled and toggle off")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMOnboardingWithWFSGEnabledAndToggleOffAsInternalAdmin(String browser, String username, String password, String location){
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            verifyTMOnboardingWithWFSGEnabledAndToggleOnOrOff("Yes", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate TM onboarding with WFSG disabled")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMOnboardingWithWFSGDisabledAsInternalAdmin(String browser, String username, String password, String location){
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            verifyTMOnboardingWithWFSGEnabledAndToggleOnOrOff("No", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    private void verifyTMOnboardingWithWFSGEnabledAndToggleOnOrOff (String setWFS, boolean enableWorkLocationOrNot) throws Exception {
        //Verify WFSG can be enabled or disable
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
        controlsNewUIPage.clickOnGlobalLocationButton();
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        configurationPage.setWFS(setWFS);

        //Check if there is the url set under Controls -> Compliance -> Company Mobile Policy
        controlsNewUIPage.clickOnControlsConsoleMenu();
        SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
        controlsNewUIPage.clickOnControlsComplianceSection();
        SimpleUtils.assertOnFail("Controls: Compliance page not loaded Successfully!", controlsNewUIPage.isControlsComplianceLoaded(), false);
        controlsNewUIPage.clickOnGlobalLocationButton();
        hasCompanyMobilePolicyURL = controlsNewUIPage.hasCompanyMobilePolicyURLOrNot();

        OnboardingPage onboardingPage = pageFactory.createOnboardingPage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        //Verify TM can be invited
        createNewUserAndInvite();
        String lastName = MyThreadLocal.getLastNameForNewHire();
        onboardingPage.openOnboardingPage(invitationCode, firstName, false, "KendraScott2");
        onboardingPage.verifyTheContentOfCreateAccountPage(firstName, invitationCode);
        onboardingPage.verifyLastName(lastName);
        SimpleUtils.assertOnFail("Create Account page failed to load after verifying last name!",
                onboardingPage.isCreateAccountPageLoadedAfterVerifyingLastName(), false);
        onboardingPage.createAccountForNewHire(newPassword);
        // Verify "Is this email correct?" dialog pops up
        onboardingPage.verifyIsEmailCorrectDialogPopup();
        // Click the YES button On 'Is Email Correct' Dialog
        onboardingPage.clickYesBtnOnIsEmailCorrectDialog();
        // Click the Continue button on Important Notice from your Employer page
        if (hasCompanyMobilePolicyURL) {
            onboardingPage.clickOnButtonByLabel(continueLabel);
        }
        // Click the Next button on Verify Profile page
        onboardingPage.clickOnButtonByLabel(nextLabel);

        //Click the Next on Set Availability page
        onboardingPage.clickOnNextButtonOnSetAvailabilityPage();

        if (setWFS.equalsIgnoreCase("Yes")) {
            //Verify the content on Work Locations page
            onboardingPage.verifyWorkLocationsPageLoaded();
            if (!enableWorkLocationOrNot) {
                onboardingPage.setOtherPreferredLocationsToggleOnWorkLocationsPage("No");
            }

            //Verify the functionality of Back button
            onboardingPage.clickOnBackButtonOnWorkLocationsPage();
            onboardingPage.verifySetAvailabilityPageLoaded();
            onboardingPage.clickOnNextButtonOnSetAvailabilityPage();

            //Verify the functionality of Next button
            onboardingPage.clickOnNextButtonOnWorkLocationsPage();
        }

        onboardingPage.verifyThatsItPageLoaded();

        onboardingPage.clickOnDoneOnThatsItPage();
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.verifyNewTermsOfServicePopUp();

        //Check other preferred location from preferences page
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
        HashMap<String, String>  shiftPreferenceData = profileNewUIPage.getMyShiftPreferenceData();
        String otherPreferredLocationsStatus = shiftPreferenceData.get("otherPreferredLocations");
        if (enableWorkLocationOrNot) {
            SimpleUtils.assertOnFail("The other Preferred Locations should display as Yes, but it display as: " + otherPreferredLocationsStatus,
                    otherPreferredLocationsStatus.equalsIgnoreCase("yes"), false);
        } else
            SimpleUtils.assertOnFail("The other Preferred Locations should display as No, but it display as: " + otherPreferredLocationsStatus,
                    otherPreferredLocationsStatus.equalsIgnoreCase("no"), false);
    }




    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate TM onboarding Work Locations steps on OP site")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMOnboardingWorkLocationsStepsOnOPSiteAsInternalAdmin(String browser, String username, String password, String location){
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Switch to OP side to get the setting for current location
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(currentLocation);               ;
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(currentLocation), false);
            locationsPage.clickOnLocationInLocationResult(currentLocation);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            //Get WFSG status
            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
            cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Schedule Collaboration"));
            boolean wfsStatus = configurationPage.isWFSEnabled();
            //Check if there is the url set under Controls -> Compliance -> Company Mobile Policy
            configurationPage.goToConfigurationPage();
            controlsNewUIPage.clickOnControlsComplianceSection();
            cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Compliance"));
            hasCompanyMobilePolicyURL = configurationPage.hasCompanyMobilePolicyURLOrNotOnOP();
            switchToConsoleWindow();

            OnboardingPage onboardingPage = pageFactory.createOnboardingPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            //Verify TM can be invited
            createNewUserAndInvite();
            String lastName = MyThreadLocal.getLastNameForNewHire();
            onboardingPage.openOnboardingPage(invitationCode, firstName, false, "Cinemark-Wkdy");
            onboardingPage.verifyTheContentOfCreateAccountPage(firstName, invitationCode);
            onboardingPage.verifyLastName(lastName);
            SimpleUtils.assertOnFail("Create Account page failed to load after verifying last name!",
                    onboardingPage.isCreateAccountPageLoadedAfterVerifyingLastName(), false);
            onboardingPage.createAccountForNewHire(newPassword);
            // Verify "Is this email correct?" dialog pops up
            onboardingPage.verifyIsEmailCorrectDialogPopup();
            // Click the YES button On 'Is Email Correct' Dialog
            onboardingPage.clickYesBtnOnIsEmailCorrectDialog();
            // Click the Continue button on Important Notice from your Employer page
            if (hasCompanyMobilePolicyURL) {
                onboardingPage.clickOnButtonByLabel(continueLabel);
            }
            // Click the Next button on Verify Profile page
            onboardingPage.clickOnButtonByLabel(nextLabel);

            //Click the Next on Set Availability page
            onboardingPage.clickOnNextButtonOnSetAvailabilityPage();

            if (wfsStatus) {
                //Verify the content on Work Locations page
                onboardingPage.verifyWorkLocationsPageLoaded();

                //Verify the functionality of Back button
                onboardingPage.clickOnBackButtonOnWorkLocationsPage();
                onboardingPage.verifySetAvailabilityPageLoaded();
                onboardingPage.clickOnNextButtonOnSetAvailabilityPage();

                //Verify the functionality of Next button
                onboardingPage.clickOnNextButtonOnWorkLocationsPage();
            }

            onboardingPage.verifyThatsItPageLoaded();

            onboardingPage.clickOnDoneOnThatsItPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.verifyNewTermsOfServicePopUp();

            //Check other preferred location from preferences page
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Work Preferences");
            Thread.sleep(3000);
            HashMap<String, String>  shiftPreferenceData = profileNewUIPage.getMyShiftPreferenceData();
            String otherPreferredLocationsStatus = shiftPreferenceData.get("otherPreferredLocations");
            if (wfsStatus) {
                SimpleUtils.assertOnFail("The other Preferred Locations should display as Yes, but it display as: " + otherPreferredLocationsStatus,
                        otherPreferredLocationsStatus.equalsIgnoreCase("yes"), false);
            } else
                SimpleUtils.assertOnFail("The other Preferred Locations should display as No, but it display as: " + otherPreferredLocationsStatus,
                        otherPreferredLocationsStatus.equalsIgnoreCase("no"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}