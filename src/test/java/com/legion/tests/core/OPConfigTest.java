package com.legion.tests.core;

import com.legion.api.cache.RemoveTemplateSnapShotForLocationsAPI;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.pages.core.opConfiguration.MealAndRestPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OPConfigTest extends TestBase {

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

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Minors Rules template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorRulesTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            //check description and the items in the Minors Rules tile
            configurationPage.verifyMinorRulesTileIsLoaded();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate new tile for Minors Rules")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNewTileForMinorRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.verifyMinorRulesTileIsLoaded();
            configurationPage.clickOnConfigurationCrad("Minors Rules");
            configurationPage.isTemplateListPageShow();
            String templateName = "TemplateName-ForAuto";
            configurationPage.deleteTemplate(templateName);

            //create new template with the 'New Template' button, there are 2 tabs: Detail and Association
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");

            // Check 2 sections: Minor Schedule by Week and Minor Schedule by Day in detail page
            SimpleUtils.assertOnFail("The Minors Sections should load on minor template page! ",
                    configurationPage.checkIfMinorSectionsLoaded(), false);
            configurationPage.clickOnBackButton();
            configurationPage.deleteTemplate(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate no minors rules in Compliance template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoMinorRulesInComplianceTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Compliance");
            configurationPage.isTemplateListPageShow();
            String templateName = "TemplateName-ForAuto";
            configurationPage.deleteTemplate(templateName);
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            //There are no setting for minors rules on Compliance template page
            SimpleUtils.assertOnFail("The Minors Sections should not on Compliance template page! ",
                    !configurationPage.checkIfMinorSectionsLoaded(), false);
            configurationPage.clickOnBackButton();
            configurationPage.deleteTemplate(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify new tile for Meal and Rest")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMealAndRestTileAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            // Verify the title of the tile
            // Verify the description of the tile
            // Verify the sub description list of the tile
            List<String> expectedContent = new ArrayList<>();
            expectedContent.add("Meal and Rest Breaks");
            expectedContent.add("Number and length of meal breaks per shift");
            expectedContent.add("Number and length of rest breaks per shift");
            expectedContent.add("Paid / non paid meal break definitions");
            configurationPage.verifyTheContentOnSpecificCard(
                    OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue(), expectedContent);
            // Verify the "Meal and Rest" tile is clickable
            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());
            // Verify the content on Meal and Rest page
            configurationPage.isTemplateListPageShow();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality on Meal and Rest Details page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheMealAndRestTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String meal = "Meal";
            String rest = "Rest";
            String templateName = TestBase.getCurrentTime().substring(0, 8);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            MealAndRestPage mealAndRestPage = (MealAndRestPage) pageFactory.createMealAndRestPage();
            configurationPage.goToConfigurationPage();

            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());
            // Verify the content on Meal and Rest page
            configurationPage.isTemplateListPageShow();
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            // Verify the content on Meal Breaks section
            mealAndRestPage.verifyTheContentOnMealBreaksSection();
            // Verify Yes/No button is clickable on Meal Breaks section
            mealAndRestPage.selectYesOrNoOnMealOrRest(meal, "Yes");
            mealAndRestPage.selectYesOrNoOnMealOrRest(meal, "No");
            // Verify + Add button is clickable on Meal Breaks section
            mealAndRestPage.clickOnAddButtonOnMealOrRestSection(meal);
            // Verify the functionality of 7 input boxes on Meal Breaks section
            mealAndRestPage.verifyTheFunctionalityOfInputsInMealOrRest(meal);
            // Verify X button is clickable on Meal Breaks section
            mealAndRestPage.verifyXbuttonOnMealOrRest(meal);
            // Verify the content on Rest Breaks section
            mealAndRestPage.verifyTheContentOnRestBreaksSection();
            // Verify Yes/No button is clickable on Rest Breaks section
            mealAndRestPage.selectYesOrNoOnMealOrRest(rest, "Yes");
            mealAndRestPage.selectYesOrNoOnMealOrRest(rest, "No");
            // Verify + Add button is clickable on Rest Breaks section
            mealAndRestPage.clickOnAddButtonOnMealOrRestSection(rest);
            // Verify the functionality of 3 input boxes on Rest Breaks section
            mealAndRestPage.verifyTheFunctionalityOfInputsInMealOrRest(rest);
            // Verify X button is clickable on Rest Breaks section
            mealAndRestPage.verifyXbuttonOnMealOrRest(rest);
            // Clear data
            configurationPage.saveADraftTemplate();
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify can set the value for Meal and Rest template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCanSetValueForMealAndRestTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String meal = "Meal";
            String rest = "Rest";
            String dynamicEmployeeGroup = "MealAuto";
            String templateName = TestBase.getCurrentTime().substring(0, 8);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            MealAndRestPage mealAndRestPage = (MealAndRestPage) pageFactory.createMealAndRestPage();
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            configurationPage.goToConfigurationPage();

            configurationPage.clickOnConfigurationCrad(OpsPortalConfigurationPage.configurationLandingPageTemplateCards.MealAndRest.getValue());
            // Verify the content on Meal and Rest page
            configurationPage.isTemplateListPageShow();
            configurationPage.archiveOrDeleteTemplate(templateName);
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            // Verify can set the meal breaks
            List<String> mealSettings = new ArrayList<String>(Arrays.asList("1", "240", "480", "", "", "", "", "20", "", "", "", "120", "120"));
            mealAndRestPage.clickOnAddButtonOnMealOrRestSection(meal);
            mealAndRestPage.verifyCanSetTheValueForInputs(meal, mealSettings);
            // Verify can set the rest breaks
            List<String> restSettings = new ArrayList<>(Arrays.asList("180", "360", "1"));
            mealAndRestPage.clickOnAddButtonOnMealOrRestSection(rest);
            mealAndRestPage.verifyCanSetTheValueForInputs(rest, restSettings);
            // Verify can select the Association
            configurationPage.clickOnAssociationTabOnTemplateDetailsPage();
            configurationPage.selectOneDynamicGroup(dynamicEmployeeGroup);
            configurationPage.clickOnTemplateDetailTab();
            // Verify can publish the template successfully
            cinemarkMinorPage.saveOrPublishTemplate(CinemarkMinorTest.templateAction.Publish_Now.getValue());
            // Verify the value is saved successfully
            configurationPage.clickOnSpecifyTemplateName(templateName, "view");
            SimpleUtils.assertOnFail("Meal value are not Saved!", mealAndRestPage.verifyMealAndRestValueAreSaved(meal, mealSettings), false);
            SimpleUtils.assertOnFail("Rest value are not Saved!", mealAndRestPage.verifyMealAndRestValueAreSaved(rest, restSettings), false);
            // Verify the association is selected successfully
            configurationPage.clickOnAssociationTabOnTemplateDetailsPage();
            configurationPage.verifySpecificAssociationIsSaved(dynamicEmployeeGroup);
            // Clear data
            configurationPage.clickOnBackButton();
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify wages should not show when setting Labor Preferences to None")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWagesShouldNotShowsWhenSettingLaborPreferencesToNoneAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
//            RemoveTemplateSnapShotForLocationsAPI.removeTemplateSnapShotForLocationsAPI(getUserNameNPwdForCallingAPI().get(0),
//                    getUserNameNPwdForCallingAPI().get(1));
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            String option = "None";
            setLaborPreferencesForForecastSummarySmartcardSetting(option);
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
            refreshCachesAfterChangeTemplate();
//            Thread.sleep(5000);
//            loginPage.logOut();
//            refreshPage();
//            Thread.sleep(5000);
//            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
//            Thread.sleep(5000);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            forecastPage.clickOnLabor();
            String textOnLaborSmartCard = smartCardPage.getsmartCardTextByLabel("Summary");
            int i=0;
            while (i<15 && textOnLaborSmartCard.toLowerCase().contains("wages")) {
                Thread.sleep(60000);
                loginPage.logOut();
                Thread.sleep(5000);
                loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
                Thread.sleep(5000);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
                forecastPage.clickOnLabor();
                textOnLaborSmartCard = smartCardPage.getsmartCardTextByLabel("Summary");
                i++;
            }
            SimpleUtils.assertOnFail("The Wages row should not display on Forecast Labor smart card! the actual text is "+textOnLaborSmartCard,
                    !textOnLaborSmartCard.toLowerCase().contains("wages"), false);
            loginPage.logOut();
            Thread.sleep(5000);
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            Thread.sleep(5000);
            dashboardPage.clickOnDashboardConsoleMenu();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String textOnScheduleSmartCard = smartCardPage.getsmartCardTextByLabel("Schedule V");
            i=0;
            while (i<15 && textOnScheduleSmartCard.toLowerCase().contains("wages")) {
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
                Thread.sleep(5000);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                textOnScheduleSmartCard = smartCardPage.getsmartCardTextByLabel("Schedule V");
                i++;
            }
            textOnScheduleSmartCard = smartCardPage.getsmartCardTextByLabel("Schedule V");
            SimpleUtils.assertOnFail("The Wages row should not display on Schedule smart card! the actual text is "+textOnScheduleSmartCard,
                    !textOnScheduleSmartCard.toLowerCase().contains("wages"), false);

//            //Change setting for next case to save the waiting session time
//            setLaborPreferencesForForecastSummarySmartcardSetting("Wages");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify wages shows when setting Labor Preferences to Wages")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWagesShowsWhenSettingLaborPreferencesToWagesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            RemoveTemplateSnapShotForLocationsAPI.removeTemplateSnapShotForLocationsAPI(getUserNameNPwdForCallingAPI().get(0),
                    getUserNameNPwdForCallingAPI().get(1));
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            String option = "Wages";
            setLaborPreferencesForForecastSummarySmartcardSetting(option);
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
//            Thread.sleep(5000);
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            Thread.sleep(5000);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            forecastPage.clickOnLabor();
            String textOnLaborSmartCard = smartCardPage.getsmartCardTextByLabel("Summary");
            int i=0;
            while (i<15 && !textOnLaborSmartCard.toLowerCase().contains(option.toLowerCase())) {
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
                Thread.sleep(5000);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
                forecastPage.clickOnLabor();
                textOnLaborSmartCard = smartCardPage.getsmartCardTextByLabel("Summary");
                i++;
            }
            SimpleUtils.assertOnFail("The Wages row should display on Forecast Labor smart card! The actual text is "+textOnLaborSmartCard,
                    textOnLaborSmartCard.toLowerCase().contains(option.toLowerCase()), false);
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            Thread.sleep(5000);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String textOnScheduleSmartCard = smartCardPage.getsmartCardTextByLabel("Schedule V");
            i=0;
            while (i<15 && !textOnScheduleSmartCard.toLowerCase().contains(option.toLowerCase())) {
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
                Thread.sleep(5000);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
                textOnScheduleSmartCard = smartCardPage.getsmartCardTextByLabel("Schedule V");
                i++;
            }
            SimpleUtils.assertOnFail("The Wages row should display on Schedule smart card! The actual text is "+ textOnScheduleSmartCard,
                    textOnScheduleSmartCard.toLowerCase().contains(option.toLowerCase()), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    private void setLaborPreferencesForForecastSummarySmartcardSetting(String option) throws Exception {
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        Thread.sleep(7000);
        locationsPage.clickOnLocationsTab();
        locationsPage.goToGlobalConfigurationInLocations();
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        scheduleMainPage.clickOnEditButton();
        configurationPage.updateLaborPreferencesForForecastSummarySmartcardSettingDropdownOption(option);
        Thread.sleep(3000);
        controlsNewUIPage.clickOnSaveBtn();


    }
}
