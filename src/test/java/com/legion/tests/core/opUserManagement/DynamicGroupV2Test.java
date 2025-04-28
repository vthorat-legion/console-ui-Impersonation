package com.legion.tests.core.opUserManagement;

import com.legion.api.common.EnterpriseId;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.OpsPortaPageFactories.*;
import com.legion.pages.ReportPage;
import com.legion.pages.core.OpCommons.ConsoleNavigationPage;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.OpCommons.RightHeaderBarPage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.pages.core.OpsPortal.OpsPortalSettingsAndAssociationPage;
import com.legion.pages.core.oplabormodel.LaborModelPanelPage;
import com.legion.pages.core.oplabormodel.LaborModelRepositoryPage;
import com.legion.tests.TestBase;
import com.legion.utils.DBConnection;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import org.testng.annotations.BeforeMethod;


import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getEnterprise;

public class DynamicGroupV2Test extends TestBase {

    @BeforeClass
    public void enableDynamicGroupV2Toggle() throws Exception{
        //ToggleAPI.enableToggle(Toggles.DynamicGroupV2.getValue(), "stoneman@legion.co", "admin11.a");
        //ToggleAPI.updateToggle(Toggles.MinorRulesTemplate.getValue(), "stoneman@legion.co", "admin11.a", true);
    }


    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        this.createDriver((String) params[0], "69", "Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        RightHeaderBarPage modelSwitchPage = new RightHeaderBarPage();
        modelSwitchPage.switchToOpsPortal();
    }
/*
    @AfterClass
    public void tearDown() throws Exception {
        //ToggleAPI.disableToggle(Toggles.DynamicGroupV2.getValue(), "stoneman@legion.co", "admin11.a");
        //ToggleAPI.disableToggle(Toggles.MinorRulesTemplate.getValue(), "stoneman@legion.co", "admin11.a");
    }
*/
    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate the name for dynamic user group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNameForDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        String templateName = "AutoTest"+System.currentTimeMillis();

        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        settingsAndAssociationPage.verifyTitleOnTheSettingsPage("Employee");
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //Create minor rules template
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue(), "IN", "15");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
        //Edit the template, check the title
        configurationPage.clickOnTemplateName(templateName);
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.verifyTitleOnTheSAssociationPage("Employee");
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.verifyTitleOnTheSAssociationPage("Employee");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate the name for dynamic location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheNameForDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Compliance.getValue());
        //Verify Dynamic group name on the setting page.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        settingsAndAssociationPage.verifyTitleOnTheSettingsPage("Location");
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //Verify Dynamic group name on the association page.
        configurationPage.clickOnTemplateName("");
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.verifyTitleOnTheSAssociationPage("Location");
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Verify Dynamic group name on the association page---edit mode.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.verifyTitleOnTheSAssociationPage("Location");

        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //check dynamic group item
        locationsPage.iCanSeeDynamicGroupItemInLocationsTab();
        //go to dynamic group
        locationsPage.goToDynamicGroup();
        locationsPage.clickOnAddBtnForSharingDynamicLocationGroup();
        locationsPage.verifyTitleForWorkforceSharingLocationGroup();
        locationsPage.clickOnCancelBtnOnSharingDynamicLocationGroupWindow();

        //Verify Dynamic group name on the Job page.
        JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
        jobsPage.iCanEnterJobsTab();
        jobsPage.iCanEnterCreateNewJobPage();
        jobsPage.selectJobType("Release Schedule");
        jobsPage.selectWeekForJobToTakePlace();
        jobsPage.clickOkBtnInCreateNewJobPage();
        jobsPage.addLocationBtnIsClickable();
        jobsPage.verifyDynamicGroupName();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate the Dynamic Group tile is removed from User Management")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheDynamicEmployeeGroupTileIsRemovedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
        userManagementPage.clickOnUserManagementTab();
        SimpleUtils.assertOnFail("Dynamic User Group tile should not be showing up!", !userManagementPage.iCanSeeDynamicGroupItemTileInUserManagementTab(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate creation for \"Dynamic Employee Group\"")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheCreationForDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());

        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Minor");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");

        //Create new template.
        String templateName = "AutoTest"+String.valueOf(System.currentTimeMillis());
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and save it.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue(), "IN", "15");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Go into that template again to update the group.
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue(), "IN", "14");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate the Association page for Dynamic employee group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheAssociationPageForDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());

        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();
        //Go to the settings tab to select a field.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Minor");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");

        //==========verify there is only one dynamic group displaying, add button should be disabled after adding one============
        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and save it.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue(), "IN", "15");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Add button should be disabled now!", !settingsAndAssociationPage.isAddGroupBtnEnabled(), false);
        settingsAndAssociationPage.clickOnRemoveBtnToRemoveDynamicGroupOnAssociationPage();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate creation for \"Dynamic Location Group\"")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheCreationForDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());

        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");

        //Create new template.
        String templateName = "AutoTest"+String.valueOf(System.currentTimeMillis());
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and save it.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Go into that template again to update the group.
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "Japan");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate the Assocition page for Dynamic location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheAssociationPageForDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());

        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();
        //Go to the settings tab to select a field.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");

        //==========verify there is only one dynamic group displaying, add button should be disabled after adding one============
        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and save it.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Add button should be disabled now!", !settingsAndAssociationPage.isAddGroupBtnEnabled(), false);
        settingsAndAssociationPage.clickOnRemoveBtnToRemoveDynamicGroupOnAssociationPage();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName);
    }



//--------------Tests for Settings tab------------ Begin-------------
    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate settings tab fields")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheSettingsFieldsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        //======verify Setting tab for dynamic employee group========
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Work Role");
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        ExpectedFieldsFromSettingsTab.add("City");
        ExpectedFieldsFromSettingsTab.add("Location Name");
        ExpectedFieldsFromSettingsTab.add("Location Id");
        ExpectedFieldsFromSettingsTab.add("Employment Type");
        ExpectedFieldsFromSettingsTab.add("Employment Status");
        ExpectedFieldsFromSettingsTab.add("Minor");
        ExpectedFieldsFromSettingsTab.add("Exempt");
        ExpectedFieldsFromSettingsTab.add("Badge");
        ExpectedFieldsFromSettingsTab.add("Job Title");
        List<String> ActualFieldsFromSettingsTab = settingsAndAssociationPage.getFieldListFromSettingsTab();
        SimpleUtils.assertOnFail("Fields are not all expected!", ExpectedFieldsFromSettingsTab.containsAll(ActualFieldsFromSettingsTab), false);
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);

        //======verify Setting tab for dynamic location group========
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Compliance.getValue());
        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        ExpectedFieldsFromSettingsTab.clear();
        ExpectedFieldsFromSettingsTab.add("Config Type");
        ExpectedFieldsFromSettingsTab.add("District");
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        ExpectedFieldsFromSettingsTab.add("City");
        ExpectedFieldsFromSettingsTab.add("Location Name");
        ExpectedFieldsFromSettingsTab.add("Location Id");
        ExpectedFieldsFromSettingsTab.add("Location Type");
        ExpectedFieldsFromSettingsTab.add("UpperField");

        ActualFieldsFromSettingsTab = settingsAndAssociationPage.getFieldListFromSettingsTab();
        SimpleUtils.assertOnFail("Fields are not all expected!", ExpectedFieldsFromSettingsTab.containsAll(ActualFieldsFromSettingsTab), false);
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate criteria selection and usage for dynamic employee group criteria")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCriteriaSelectionAndUsageForDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();

        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        ExpectedFieldsFromSettingsTab.add("City");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+String.valueOf(System.currentTimeMillis());
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        List<String> ActualFieldsFromTheAssociationPage = settingsAndAssociationPage.getCriteriaListFromTheAssociationPage();
        SimpleUtils.assertOnFail("Criteria on the Association page are not all expected!", ExpectedFieldsFromSettingsTab.containsAll(ActualFieldsFromTheAssociationPage), false);
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.City.getValue(), "IN", "ANY");
        String testResult = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        SimpleUtils.assertOnFail("Test result is not coming up!", testResult != null, false);
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName);

        //Go to another type of template.
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());

        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTabForAnotherTemplate = new ArrayList<>();
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Minor");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTabForAnotherTemplate);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName2 = "AutoTest"+String.valueOf(System.currentTimeMillis());
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        List<String> ActualFieldsFromTheAssociationPageForAnotherTemplate = settingsAndAssociationPage.getCriteriaListFromTheAssociationPage();
        SimpleUtils.assertOnFail("Criteria on the Association page are not all expected!", ExpectedFieldsFromSettingsTabForAnotherTemplate.containsAll(ActualFieldsFromTheAssociationPageForAnotherTemplate), false);
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName2);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate criteria selection and usage for dynamic location group criteria")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCriteriaSelectionAndUsageForDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        ExpectedFieldsFromSettingsTab.add("City");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+String.valueOf(System.currentTimeMillis());
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        List<String> ActualFieldsFromTheAssociationPage = settingsAndAssociationPage.getCriteriaListFromTheAssociationPage();
        SimpleUtils.assertOnFail("Criteria on the Association page are not all expected!", ExpectedFieldsFromSettingsTab.containsAll(ActualFieldsFromTheAssociationPage), false);
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.City.getValue(), "IN", "ANY");
        String testResult = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        SimpleUtils.assertOnFail("Test result is not coming up!", testResult != null, false);
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName);

        //Go to another type of template.
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Compliance.getValue());

        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTabForAnotherTemplate = new ArrayList<>();
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Location Type");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTabForAnotherTemplate);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");

        //Create new template.
        String templateName2 = "AutoTest"+String.valueOf(System.currentTimeMillis());
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        List<String> ActualFieldsFromTheAssociationPageForAnotherTemplate = settingsAndAssociationPage.getCriteriaListFromTheAssociationPage();
        SimpleUtils.assertOnFail("Criteria on the Association page are not all expected!", ExpectedFieldsFromSettingsTabForAnotherTemplate.containsAll(ActualFieldsFromTheAssociationPageForAnotherTemplate), false);
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
        //delete the template.
        configurationPage.archiveOrDeleteTemplate(templateName2);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate permission for settings tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCriteriaSelectionAndUsageForDynamicLocationGroupAsStoreManager(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        //======verify Setting tab for dynamic location group========
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Config Type");
        ExpectedFieldsFromSettingsTab.add("District");
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        ExpectedFieldsFromSettingsTab.add("City");
        ExpectedFieldsFromSettingsTab.add("Location Name");
        ExpectedFieldsFromSettingsTab.add("Location Id");
        ExpectedFieldsFromSettingsTab.add("Location Type");
        ExpectedFieldsFromSettingsTab.add("UpperField");
        List<String> ActualFieldsFromSettingsTab = settingsAndAssociationPage.getFieldListFromSettingsTab();
        SimpleUtils.assertOnFail("Fields are not all expected!", ExpectedFieldsFromSettingsTab.containsAll(ActualFieldsFromSettingsTab), false);
        SimpleUtils.assertOnFail("There should not be fields enabled!", !settingsAndAssociationPage.areFieldsCheckInputEnabled(), false);

        //======verify Setting tab for dynamic employee group========
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());

        //Go to the Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTabForAnotherTemplate = new ArrayList<>();
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Work Role");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Country");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("State");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("City");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Location Name");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Location Id");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Employment Type");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Employment Status");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Minor");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Exempt");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Badge");
        ExpectedFieldsFromSettingsTabForAnotherTemplate.add("Job Title");
        ActualFieldsFromSettingsTab = settingsAndAssociationPage.getFieldListFromSettingsTab();
        SimpleUtils.assertOnFail("Fields are not all expected!", ExpectedFieldsFromSettingsTabForAnotherTemplate.containsAll(ActualFieldsFromSettingsTab), false);
        SimpleUtils.assertOnFail("There should not be fields enabled!", !settingsAndAssociationPage.areFieldsCheckInputEnabled(), false);
    }
//--------------Tests for Settings tab------------ End-------------


    //--------------Tests for Conflict Detection------------ Begin-------------

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is conflict among Dynamic location group---create new Dynamic group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDetectionForCreatingNewDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        settingsAndAssociationPage.verifyConflictDetectionInfo();
        settingsAndAssociationPage.clickOnButtonOnTheConflictDetectedWindow("save");
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("publish now");
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is no conflict among Dynamic location group---create new Dynamic group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoConflictDetectedForCreatingNewDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is conflict among Dynamic location group---edit Dynamic group for draft version template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDetectionForEditingExistedDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("save");


        //Edit the second template again.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        settingsAndAssociationPage.verifyConflictDetectionInfo();
        settingsAndAssociationPage.clickOnButtonOnTheConflictDetectedWindow("save");
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("publish now");
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is no conflict among Dynamic location group---edit Dynamic group for draft version template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoConflictDetectedForEditingExistedDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("save");

        //Edit the second template.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Florida");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is conflict among Dynamic location group---edit Dynamic group for a published template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDetectionForEditingPublishedDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();


        //Edit the second template again.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should show up. And save button should be disabled!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP() && !settingsAndAssociationPage.isSaveBtnEnabledOnConflictDetectedWindow(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is no conflict among Dynamic location group---edit Dynamic group for a published template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoConflictDetectedForEditingPublishedDynamicLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.Communications.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Edit the second template.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Florida");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
    }

    //===========================================================================
    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is conflict among Dynamic employee group---create new Dynamic group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDetectionForCreatingNewDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        settingsAndAssociationPage.verifyConflictDetectionInfo();
        settingsAndAssociationPage.clickOnButtonOnTheConflictDetectedWindow("save");
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("publish now");
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is no conflict among Dynamic employee group---create new Dynamic group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoConflictDetectedForCreatingNewDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is conflict among Dynamic employee group---edit Dynamic group for draft version template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDetectionForEditingExistedDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("save");


        //Edit the second template again.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        settingsAndAssociationPage.verifyConflictDetectionInfo();
        settingsAndAssociationPage.clickOnButtonOnTheConflictDetectedWindow("save");
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("publish now");
        SimpleUtils.assertOnFail("Conflict detection window should show up!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is no conflict among Dynamic employee group---edit Dynamic group for draft version template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoConflictDetectedForEditingExistedDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("save");

        //Edit the second template.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Florida");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is conflict among Dynamic employee group---edit Dynamic group for a published template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDetectionForEditingPublishedDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();


        //Edit the second template again.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should show up. And save button should be disabled!", settingsAndAssociationPage.ifConflictDetectedWindowShowUP() && !settingsAndAssociationPage.isSaveBtnEnabledOnConflictDetectedWindow(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate detection when there is no conflict among Dynamic employee group---edit Dynamic group for a published template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoConflictDetectedForEditingPublishedDynamicEmployeeGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MinorsRules.getValue());
        //Go to the Settings tab and set required fields.
        settingsAndAssociationPage.goToTemplateListOrSettings("setting");
        List<String> ExpectedFieldsFromSettingsTab = new ArrayList<>();
        ExpectedFieldsFromSettingsTab.add("Country");
        ExpectedFieldsFromSettingsTab.add("State");
        settingsAndAssociationPage.setupRequiredFields(ExpectedFieldsFromSettingsTab);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //delete all template.
        configurationPage.archiveOrDeleteAllTemplates();

        //Create new template.
        String templateName = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Texas");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Create new template.
        String templateName2 = "AutoTest"+System.currentTimeMillis();
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName2);
        settingsAndAssociationPage.deleteAllCriteriaOnTheAssociationPageIfExist();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Washington");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        //Edit the second template.
        configurationPage.clickOnTemplateName(templateName2);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        //Go to the Association page to create a dynamic employee group and check the fields.
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnEditBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Florida");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        SimpleUtils.assertOnFail("Conflict detection window should not show up!", !settingsAndAssociationPage.ifConflictDetectedWindowShowUP(), false);
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
    }

    //--------------Tests for Conflict Detection------------ End---------------
    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate there are warning icons to indicate the template for conflicts among dynamic location groups")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWarningIconsForConflictingDynamicLocationGroupsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        String templateName = "AutoTemp1" + System.currentTimeMillis();
        String templateName2 = "AutoTemp2"+ System.currentTimeMillis();

        //Go to Time and Attendance template
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.TimeAttendance.getValue());
        //-------------go to settings page, check Country and State, uncheck other options-----------
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        List<String> requiredFields = new ArrayList<>();
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue());
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue());
        settingsAndAssociationPage.setupRequiredFields(requiredFields);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //-------------create two templates. make them conflict-------------
        configurationPage.archiveOrDeleteAllTemplates();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage("AutoGroup"+ System.currentTimeMillis());
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "Any");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue(), "IN", "Abu Dhabi");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        configurationPage.createNewTemplate(templateName2);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "Any");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue(), "IN", "Abu Dhabi");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.clickOnTheSaveBtnOnConflictDetectedWindow();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //return to the template list, verify there are waring icons.
        String expectedWarningMsg = "This template conflicts with other templates, please resolve.";
        SimpleUtils.assertOnFail("Warning icons and message should correctly show up!", configurationPage.verifyWarningIconsDisplay(templateName2, expectedWarningMsg), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate there are warning icons to indicate the template for conflicts among dynamic employee groups")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWarningIconsForConflictingDynamicEmployeeGroupsAsInternalAdmin(String browser, String userName, String password, String location) throws Exception{
        String templateName = "AutoTemp1" + System.currentTimeMillis();
        String templateName1 = "AutoTemp2" + System.currentTimeMillis();
        List<String> requiredFields = new ArrayList<>();

        //-------------go to settings page, check Employment Status and Minor, uncheck other options-----------
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.EmploymentStatus.getValue());
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue());
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        settingsAndAssociationPage.setupRequiredFields(requiredFields);
        settingsAndAssociationPage.goToTemplateListOrSettings("Template List");

        //-------------create two templates. make them conflict-------------
        configurationPage.archiveOrDeleteAllTemplates();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.EmploymentStatus.getValue(), "IN", "FullTime");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue(), "IN", "Any");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();

        configurationPage.createNewTemplate(templateName1);
        configurationPage.clickOnTemplateName(templateName1);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName1);
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.EmploymentStatus.getValue(), "IN", "FullTime");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue(), "IN", "<14");
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.clickOnTheSaveBtnOnConflictDetectedWindow();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //return to the template list, verify there are waring icons.
        String WarningMsg = "This template conflicts with other templates, please resolve.";
        SimpleUtils.assertOnFail("Warning icons and message should correctly show up!", configurationPage.verifyWarningIconsDisplay(templateName1, WarningMsg), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate there are warning icons to indicate changes of required criteria")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifyWarningIconsForRequiredCriteriaChangedAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        String templateName = "AutoTemp1" + System.currentTimeMillis();
        String templateName1 = "AutoTemp2" + System.currentTimeMillis();
        String WarningMsg = "Required fields changed, the associated Dynamic Group needs to be updated.";
        String WarningMsg1 = "Required fields changed, the associated Dynamic Group needs to be updated.";

        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.TimeAttendance.getValue());
        //--------------Dynamic Location Groups: Start-----------------------------
        //Create The first template
        configurationPage.archiveOrDeleteAllTemplates();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);
        settingsAndAssociationPage.selectFirstOptionForCriteria();
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //Change the required fields in Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        settingsAndAssociationPage.changeCriteriaInSettingsTab();
        settingsAndAssociationPage.goToTemplateListOrSettings("Template List");
        refreshPage();

        //Check warning icons for the newly created template
        SimpleUtils.assertOnFail("Dynamic Location Group Required fields changed, Warning icons and message should correctly show up!", configurationPage.verifyWarningIconsDisplay(templateName, WarningMsg), false);
        //--------------Dynamic Location Groups: End-----------------------------

        //--------------Dynamic Employee Groups: Start-----------------------------
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());

        //Create the second template
        configurationPage.createNewTemplate(templateName1);
        configurationPage.clickOnTemplateName(templateName1);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName1);
        settingsAndAssociationPage.selectFirstOptionForCriteria();
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

        //Change the required fields in Settings tab.
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        settingsAndAssociationPage.changeCriteriaInSettingsTab();
        settingsAndAssociationPage.goToTemplateListOrSettings("Template List");
        refreshPage();

        //Check warning icons for the newly created template
        SimpleUtils.assertOnFail("Dynamic Employee Group Required fields changed, Warning icons and message should correctly show up!", configurationPage.verifyWarningIconsDisplay(templateName1, WarningMsg1), false);
        //--------------Dynamic Employee Groups: End-----------------------------
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate creation window for Dynamic Employee Group on the Association page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreationWindowForDynamicEmployeeGroupOnAssociationPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        String templateName = "AutoTemp" + System.currentTimeMillis();
        List<String> requiredFields = new ArrayList<>();
        List<String> valuesToCheck = new ArrayList<>();

        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());
        //select one field in settings tab and create new template
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue());
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue());
        settingsAndAssociationPage.setupRequiredFields(requiredFields);
        settingsAndAssociationPage.goToTemplateListOrSettings("Template List");
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);

        //Check if there is in and not in for operators
        valuesToCheck.add("IN");
        valuesToCheck.add("NOT IN");
        SimpleUtils.assertOnFail("Checking Operator value in and not in failed!", settingsAndAssociationPage.ifOperatorsCanBeSelected(valuesToCheck), false);;

        //using not in for one criteria, and in for another criteria
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "NOT IN", "United Kingdom");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Minor.getValue(), "IN", "Any");

        //Click "test" and check the match number.
        String resultString = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        String matchNumber = resultString.split(" ")[0];
        System.out.println("After click test button, the match number is: " + matchNumber);

        String countryCode = "GB";
        String sqlStatement = "select w.firstName, w.lastname, b.displayName from legiondb.Worker w \n" +
                "join legiondb.Engagement en on w.objectId = en.workerId\n" +
                "join legiondb.ExternalEmployee ex on ex.objectId = en.externalEmployeeId\n" +
                "join legiondb.Business b on b.objectId = en.businessId and w.enterpriseId = '" +
                EnterpriseId.op.getValue() + "' and (b.country != '" + countryCode + "' or b.country is null)";
        int resultNumber = DBConnection.queryMultipleTableAndGetNumber(sqlStatement);
        System.out.println("after query DB, the expected resultNumber is: " + resultNumber);
        if(Integer.parseInt(matchNumber) == resultNumber){
            SimpleUtils.pass("test match result number is correct!");
        }else{
            SimpleUtils.fail("test match result number is NOT correct!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate creation window for Dynamic Location Group on the Association page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreationWindowForDynamicLocationGroupOnAssociationPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        String templateName = "AutoTemp" + System.currentTimeMillis();
        List<String> valuesToCheck = new ArrayList<>();
        String countryCode = "GB";
        String enterpriseId = EnterpriseId.op.getValue();
        String querySqlStatement = "(locationType = \"Real\" or locationType = \"LocationGroup\") and integrationStatus = \"ENABLED\" and country!='" + countryCode +  "' and enterpriseId='" + enterpriseId + "'";

        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.TimeAttendance.getValue());

        //select one field in settings tab and create new template
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        List<String> requiredFields = new ArrayList<>();
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue());
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue());
        settingsAndAssociationPage.setupRequiredFields(requiredFields);
        settingsAndAssociationPage.goToTemplateListOrSettings("Template List");

        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage(templateName);

        //Check if there is in and not in for operators
        valuesToCheck.add("IN");
        valuesToCheck.add("NOT IN");
        String checkItem = "Operator";
        SimpleUtils.assertOnFail("Checking Operator value in and not in failed!", settingsAndAssociationPage.ifOperatorsCanBeSelected(valuesToCheck), false);;

        //using not in for one criteria, and in for another criteria
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.Country.getValue(), "NOT IN", "United Kingdom");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForEmployeeGroup.State.getValue(), "IN", "Any");

        //Click "test" and check the match number.
        String resultString = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        String matchNumber = resultString.split(" ")[0];
        System.out.println("After click test button, the match number is: " + matchNumber);

        int resultNumber = DBConnection.getQueryResultNumber("legiondb.Business", "objectId", querySqlStatement);
        System.out.println("after query DB, the expected resultNumber is: " + resultNumber);
        if(Integer.parseInt(matchNumber) == resultNumber){
            SimpleUtils.pass("test match result number is correct!");
        }else{
            SimpleUtils.fail("test match result number is NOT correct!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate creation window for Dynamic Location Group in Location tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreationWindowForDynamicLocationGroupOnWorkforceSharingPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        String groupName = "Auto" + System.currentTimeMillis();
        String countryCode = "UAE";
        String stateCode = "AK";
        String enterpriseId = EnterpriseId.op.getValue();
        String querySqlStatement = "(locationType = \"Real\" or locationType = \"LocationGroup\") and integrationStatus = \"ENABLED\" and country!='" + countryCode + "' and state='" + stateCode + "' and enterpriseId='" + enterpriseId + "'";

        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //go to dynamic group, create dynamic groups
        locationsPage.iCanSeeDynamicGroupItemInLocationsTab();
        locationsPage.goToDynamicGroup();
        locationsPage.clickOnAddBtnForSharingDynamicLocationGroup();
        locationsPage.inputGroupNameForDynamicGroupOnWorkforceSharingPage(groupName);
        locationsPage.selectAnOptionForCriteria("Country", "NOT IN", "United Arab Emirates");
        locationsPage.clickAddMoreBtnOnWFSharing();
        locationsPage.selectAnOptionForCriteria("State", "IN", "Alaska");

        //Click "test" and check the match number.
        String resultString = locationsPage.clickOnTestBtnAndGetResultString();
        String matchNumber = SimpleUtils.isNumeric(resultString.split(" ")[0])? resultString.split(" ")[0] : "0";
        System.out.println("After click test button, the match number is: " + matchNumber);

        int resultNumber = DBConnection.getQueryResultNumber("legiondb.Business", "objectId", querySqlStatement);
        System.out.println("after query DB, the expected resultNumber is: " + resultNumber);
        if(Integer.parseInt(matchNumber) == resultNumber){
            SimpleUtils.pass("test match result number is correct!");
        }else{
            SimpleUtils.fail("test match result number is NOT correct!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate ability to download Health Check report")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifyAbilityToDownloadHealthCheckReportAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        ReportPage reportPage = pageFactory.createConsoleReportPage();
        RightHeaderBarPage modelSwitchPage = new RightHeaderBarPage();
        modelSwitchPage.switchToConsole();
        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
        consoleNavigationPage.searchLocation("test");
        reportPage.clickOnConsoleReportMenu();

        //find OP Health Check report and export
        SimpleUtils.assertOnFail("export failed!", reportPage.exportReportForAllLocations("health check"), false);

        //verify the exported file
        String FileExtensionExpected = "xlsx";
        SimpleUtils.assertOnFail("The reported file is not expected!", reportPage.verifyFileExtension(FileExtensionExpected), false); ;

        //open the file, and check the sheet
        List<String> expectedSheets = new ArrayList<>();
        expectedSheets.add("Location-Template Binding");
        expectedSheets.add("Employee-Template Binding");
        List<String> actualSheets = reportPage.verifyExcelSheet();
        SimpleUtils.assertOnFail("Actual sheet name are not expected!", expectedSheets.containsAll(actualSheets), false);

        //check column name for sheet "Location-Template Binding"
        List<String> expectedColumnNames = new ArrayList<>();
        expectedColumnNames.add("Location ID");
        expectedColumnNames.add("Location Name");
        expectedColumnNames.add("Template Type");
        expectedColumnNames.add("Template");
        expectedColumnNames.add("Version");
        expectedColumnNames.add("Last Modified Date");
        expectedColumnNames.add("Last Modified By");
        System.out.println("");
        SimpleUtils.assertOnFail("Column names are not expected for sheet: Location-Template Binding!", reportPage.verifyColumnNameOnSheet(expectedSheets.get(0) ,expectedColumnNames), false);

        //check column name for sheet "Employee-Template Binding"
        List<String> expectedColumnNames1 = new ArrayList<>();
        expectedColumnNames1.add("Employee ID");
        expectedColumnNames1.add("First Name");
        expectedColumnNames1.add("Last Name");
        expectedColumnNames1.add("Template Type");
        expectedColumnNames1.add("Template");
        expectedColumnNames1.add("Version");
        expectedColumnNames1.add("Last Modified Date");
        expectedColumnNames1.add("Last Modified By");
        SimpleUtils.assertOnFail("Column names are not expected for sheet: Employee-Template Binding!", reportPage.verifyColumnNameOnSheet(expectedSheets.get(1) ,expectedColumnNames1), false);

        //check Info for OP Health Check downloaded file.
        List<String> templateTypesForLocation = new ArrayList<>();
        List<String> templateTypesForEmployee = new ArrayList<>();
        templateTypesForLocation.add("SchedulingPolicy");
        templateTypesForLocation.add("ScheduleCompliance");
        templateTypesForLocation.add("ScheduleCollaboration");
        templateTypesForLocation.add("TimeAndAttendance");
        templateTypesForLocation.add("DemandDriver");
        templateTypesForLocation.add("StaffingRule");
        templateTypesForLocation.add("LaborModel");
        templateTypesForLocation.add("OperatingHours");
        templateTypesForLocation.add("CommunicationsPolicy");
        templateTypesForLocation.add("DifferentialPay");
        templateTypesForLocation.add("RealTimeAttendance");

        templateTypesForEmployee.add("Accruals");
        templateTypesForEmployee.add("Minor");
        templateTypesForEmployee.add("MealRestBreak");

        Map<String, List<String>> dynamicGroupMap = new HashMap<>();
        dynamicGroupMap.put(expectedSheets.get(0), templateTypesForLocation);
        dynamicGroupMap.put(expectedSheets.get(1), templateTypesForEmployee);
        SimpleUtils.assertOnFail("The content", reportPage.verifyInfoForOPHealthCheckExportedFile(dynamicGroupMap), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate smart card for unassigned locations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifySmartCardForUnassignedLocationsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        String templateName = "AutoTemp" + System.currentTimeMillis();
        String templateName1 = "AutoTemp1" + System.currentTimeMillis();
        List<String> requiredFields = new ArrayList<>();

        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.TimeAttendance.getValue());
        //-------------go to settings page, check Country and State, uncheck other options-----------
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.ConfigType.getValue());
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue());
        requiredFields.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue());
        settingsAndAssociationPage.setupRequiredFields(requiredFields);
        settingsAndAssociationPage.goToTemplateListOrSettings("template list");
        //-------------create two templates. one is matched with all locations-------------
        //create template with 'in any' criteria, and get the total match number.
        configurationPage.archiveOrDeleteAllTemplates();
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage("AutoGroup"+ System.currentTimeMillis());
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.ConfigType.getValue(), "IN", "Any");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "Any");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue(), "IN", "Any");

        String resultString = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        String matchNumber = resultString.split(" ")[0];
        System.out.println("After click test button, the match number for all location is: " + matchNumber);
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.saveADraftTemplate();

        //create template with certain criteria, and get the total match number.
        configurationPage.createNewTemplate(templateName1);
        configurationPage.clickOnTemplateName(templateName1);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage("AutoGroup"+ System.currentTimeMillis());
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.ConfigType.getValue(), "IN", "Any");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue(), "IN", "Any");

        String resultString1 = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        String matchNumber1 = resultString1.split(" ")[0];
        System.out.println("After click test button, the match number is: " + matchNumber1);
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        configurationPage.clickOnTemplateDetailTab();
        configurationPage.publishNowTemplate();
        int differenceNumber = Integer.parseInt(matchNumber) - Integer.parseInt(matchNumber1);
        SimpleUtils.assertOnFail("the unassigned number is not expected!", configurationPage.getUnassignedNumber() == differenceNumber, false);

        String exportFileName = "UnassignedLocations_" + "TimeAndAttendance";
        Map<String, String> criteriaAndValue = new HashMap<String, String>();
        criteriaAndValue.put(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.ConfigType.getValue(), "Any");
        criteriaAndValue.put(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "United States");
        SimpleUtils.assertOnFail("the data meets current criteria, should not show up in the file!", configurationPage.verifyUnassignedSmartCardDownloadFile(exportFileName, criteriaAndValue), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate location attributes on the Settings page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifyLocationAttributesOnTheSettingsPagesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
        String label = "External Attributes";
        String currentTime = dfs.format(new Date()).trim();
        String attributeName = "AutoAttribute" + currentTime;
        Random random = new Random();
        String attributeValue = Integer.toString(random.nextInt(20));
        String attributeDescription = attributeName;
        List<String> expectedAttributeFields = new ArrayList<>();
        HashMap<String, List<String>> externalAttributeInfo = new HashMap<>();

        LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        //Go to labor model and create external attribute
        laborModelPage.clickOnLaborModelTab();
        laborModelPage.goToLaborStandardRepositoryTile();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        laborModelPage.clickOnEditButton();
        laborModelPage.clickOnAddAttributeButton();
        laborModelPage.createNewAttribute(attributeName, attributeValue, attributeDescription);
        laborModelPage.clickOnSaveButton();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        externalAttributeInfo = laborModelPage.getValueAndDescriptionForEachAttributeAtGlobalLevel();
        laborModelPage.clickOnLaborModelTab();

        //Go to Configuration tab and time and attendance template
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.TimeAttendance.getValue());
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
        //Verify if the external attributes in settings page are same with in labor model
        for(Map.Entry<String, List<String>> entry : externalAttributeInfo.entrySet()){
            expectedAttributeFields.add(entry.getKey());
        }
        SimpleUtils.assertOnFail("Fields are not all expected!", expectedAttributeFields.containsAll(settingsAndAssociationPage.getExternalAttributesInSettingsPage()), false);
        SimpleUtils.assertOnFail("Fail to find the location attribute you search!", settingsAndAssociationPage.searchLocationAttributeInSettingsPage(attributeName), false);

        //Delete the location attribute
        laborModelPage.clickOnLaborModelTab();
        laborModelPage.goToLaborStandardRepositoryTile();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        laborModelPage.clickOnEditButton();
        laborModelPage.clickOnDeleteAttributeButton(attributeName);
        laborModelPage.clickOkBtnOnDeleteAttributeDialog();
        laborModelPage.clickOnSaveButton();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @TestName(description = "Validate location attributes on the Association page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifyLocationAttributesOnTheAssociationPagesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHH");
        String currentTime = dfs.format(new Date()).trim();
        Random random = new Random();
        int randomValue = random.nextInt(20);
        String templateName = "AutoTemp" + currentTime;
        String location1 = "AttributeTest-001";
        String location2 = "AttributeTest-002";
        String attributeName = "AutoAttribute" + currentTime;
        String label = "External Attributes";
        String attributeValue = Integer.toString(randomValue);
        String attributeValueUpdated = Integer.toString(randomValue + 2);
        String attributeDescription = attributeName;
        List<String> fieldsToSet = new ArrayList<>();
        HashMap<String, List<String>> externalAttributeInfo = new HashMap<>();
        List<String> fromValues = new ArrayList<String>(Arrays.asList("-1", "11.2", "test", "a"));

        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        //Go to labor model and create external attribute
        laborModelPage.clickOnLaborModelTab();
        laborModelPage.goToLaborStandardRepositoryTile();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        laborModelPage.clickOnEditButton();
        laborModelPage.clickOnAddAttributeButton();
        laborModelPage.createNewAttribute(attributeName, attributeValue, attributeDescription);
        laborModelPage.clickOnSaveButton();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        externalAttributeInfo = laborModelPage.getValueAndDescriptionForEachAttributeAtGlobalLevel();
        laborModelPage.clickOnLaborModelTab();

        //Select two locations, one keep default value, change values for another. Both are override
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.goToLocationDetailsPage(location1);
        locationsPage.clickOnConfigurationTabOfLocation();
        locationsPage.clickActionsForTemplate("Labor Model", "Edit");
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        locationsPage.updateLocationLevelExternalAttributes(attributeName, attributeValue, attributeDescription);
        locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");
        locationsPage.goBack();
        locationsPage.goToLocationDetailsPage(location2);
        locationsPage.clickOnConfigurationTabOfLocation();
        locationsPage.clickActionsForTemplate("Labor Model", "Edit");
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        locationsPage.updateLocationLevelExternalAttributes(attributeName, attributeValueUpdated, attributeDescription);
        locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");
        //Go to "time and attendance" template
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.TimeAttendance.getValue());
        settingsAndAssociationPage.goToTemplateListOrSettings("Settings");

        //Location attribute values to check
        HashMap<String, String> defaultLocationAttributes = new HashMap<String, String>();
        defaultLocationAttributes.put(attributeName, externalAttributeInfo.get(attributeName).get(0));
        defaultLocationAttributes.put("testByJane", externalAttributeInfo.get("testByJane").get(0));

        HashMap<String, List<String>> invalidLocationAttributes = new HashMap<String, List<String>>();
        List<String> invalidValues1 = new ArrayList<>();
        List<String> invalidValues2 = new ArrayList<>();
        List<String> invalidValues3 = new ArrayList<>();
        invalidValues1.add("-1");
        invalidValues1.add("3.2");
        invalidValues2.add("a");
        invalidValues2.add("test");
        invalidLocationAttributes.put(attributeName, invalidValues1);
        invalidLocationAttributes.put("testByJane", invalidValues2);

        HashMap<String, List<String>> validLocationAttributes = new HashMap<String, List<String>>();
        List<String> validValues1 = new ArrayList<>();
        List<String> validValues2 = new ArrayList<>();
        validValues1.add(attributeValueUpdated);
        validValues1.add(attributeValueUpdated);
        validValues2.add(externalAttributeInfo.get("testByJane").get(0));
        validValues2.add(externalAttributeInfo.get("testByJane").get(0));
        validLocationAttributes.put(attributeName, validValues1);
        validLocationAttributes.put("testByJane", validValues2);

        //Set required fields in settings page
        fieldsToSet.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue());
        fieldsToSet.add(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue());
        settingsAndAssociationPage.setupRequiredFields(fieldsToSet);
        settingsAndAssociationPage.clearUpSelectedLocationAttributes();
        for (String key : defaultLocationAttributes.keySet()){
            settingsAndAssociationPage.setupLocationAttributes(key);
        }

        settingsAndAssociationPage.goToTemplateListOrSettings("Templates");
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnTemplateName(templateName);
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
        settingsAndAssociationPage.clickOnAddBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.inputGroupNameForDynamicGroupOnAssociationPage("AutoGroup"+ System.currentTimeMillis());
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.Country.getValue(), "IN", "United States");
        settingsAndAssociationPage.selectAnOptionForCriteria(OpsPortalSettingsAndAssociationPage.requiredFieldsForLocationGroup.State.getValue(), "IN", "Any");
        settingsAndAssociationPage.verifyLocationAttributesInAssociation(defaultLocationAttributes);
        //Default attributes value can work well
        String resultString = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        SimpleUtils.assertOnFail("Test result is not coming up!", resultString != null, false);
        String matchNumber = resultString.split(" ")[0];
        SimpleUtils.assertOnFail("There should be at least 1 locations matched!", Integer.parseInt(matchNumber) > 0, false);
        //Check for invalid values
        settingsAndAssociationPage.fillInValuesForLocationAttributes(invalidLocationAttributes);
        SimpleUtils.assertOnFail("Wrong handling for the attribute values on association page", settingsAndAssociationPage.verifyAttributeValuesInAssociationPage(), false);
        invalidLocationAttributes.clear();
        invalidValues3.add("11");
        invalidValues3.add("10");
        invalidLocationAttributes.put("testByJane", invalidValues3);
        settingsAndAssociationPage.fillInValuesForLocationAttributes(invalidLocationAttributes);
        SimpleUtils.assertOnFail("Wrong handling for the attribute values on association page", settingsAndAssociationPage.verifyAttributeValuesInAssociationPage(), false);

        //Check for valid values
        settingsAndAssociationPage.fillInValuesForLocationAttributes(validLocationAttributes);
        SimpleUtils.assertOnFail("Wrong handling for the attribute values on association page", settingsAndAssociationPage.verifyAttributeValuesInAssociationPage(), false);
        String resultString1 = settingsAndAssociationPage.clickOnTestBtnAndGetResultString();
        SimpleUtils.assertOnFail("Test result is not coming up!", resultString != null, false);
        String matchNumber1 = resultString.split(" ")[0];
        SimpleUtils.assertOnFail("There should be at least 1 locations matched!", Integer.parseInt(matchNumber) > 0, false);
        settingsAndAssociationPage.clickOnDoneBtnForDynamicGroupOnAssociationPage();
        settingsAndAssociationPage.goToTemplateListOrSettings("Template");
        configurationPage.publishNowTemplate();
        configurationPage.archiveOrDeleteTemplate(templateName);

        //Delete the location attribute
        laborModelPage.clickOnLaborModelTab();
        laborModelPage.goToLaborStandardRepositoryTile();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        laborModelPage.clickOnEditButton();
        laborModelPage.clickOnDeleteAttributeButton(attributeName);
        laborModelPage.clickOkBtnOnDeleteAttributeDialog();
        laborModelPage.clickOnSaveButton();
    }

}
