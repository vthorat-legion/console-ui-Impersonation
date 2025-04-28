package com.legion.tests.core.OpsPortal;

import com.alibaba.fastjson.JSONObject;
import com.legion.api.login.LoginAPI;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.SettingsAndAssociationPage;
import com.legion.pages.core.ConsoleLocationSelectorPage;
import com.legion.pages.core.OpCommons.OpsCommonComponents;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.pages.core.opemployeemanagement.TimeOffPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.HttpUtil;
import com.legion.utils.SimpleUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;


public class DemandDriverTest extends TestBase {

public enum modelSwitchOperation{
    Console("Console"),
    OperationPortal("Control Cent");

    private final String value;
    modelSwitchOperation(final String newValue) {
        value = newValue;
    }
    public String getValue() { return value; }
}

    @Override
    @BeforeMethod(alwaysRun = true)
    public void firstTest(Method testMethod, Object[] params) throws Exception{


        this.createDriver((String)params[0],"83","Window");
//        ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), "jane.meng+006@legion.co", "P@ssword123",false);
//        ToggleAPI.updateToggle(Toggles.EnableDemandDriverTemplate.getValue(), "jane.meng+006@legion.co", "P@ssword123",true);
//        ToggleAPI.updateToggle(Toggles.MixedModeDemandDriverSwitch.getValue(), "jane.meng+006@legion.co", "P@ssword123",true);
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.verifyNewTermsOfServicePopUp();
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify category configuration in settings")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyCategoryConfigurationInSettingsForDemandDriverTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String categoryName = "CategoryTest";
            String categoryEditName = "CategoryTest-Update";
            String description = "This is a test for Category configuration!";
            String verifyType = "category";
            String locationName = "AutoCreate20220227202919";

            //Go to Demand Driver template
//            ToggleAPI.updateToggle(Toggles.UseDemandDriverTemplateSwitch.getValue(), "jane.meng+006@legion.co", "P@ssword123",false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            //Add new category in settings.
            settingsAndAssociationPage.createNewChannelOrCategory(verifyType, categoryName, description);
            //Verify newly added category is in Forecast page
            switchToNewWindow();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            SimpleUtils.assertOnFail("The newly added category not exist in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", categoryName), false);

            //edit the category in settings
            switchToNewWindow();
            settingsAndAssociationPage.clickOnEditBtnInSettings(verifyType, categoryName, categoryEditName);
            //verify edited category is in Forecast page
            switchToNewWindow();
            SimpleUtils.assertOnFail("The edited category not exist in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", categoryEditName), false);

            //remove the category in settings
            switchToNewWindow();
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, categoryEditName);
            //verify the removed category not show up in forecast page.
            switchToNewWindow();
            SimpleUtils.assertOnFail("The removed edited category should not display in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", "categoryEditName"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify channel configuration in settings")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyChannelConfigurationInSettingsForDemandDriverTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String channelName = "ChannelTest";
            String channelEditName = "ChannelTest-Update";
            String description = "This is a test for channel configuration!";
            String verifyType = "channel";
            String locationName = "AutoCreate20220227202919";

            //Go to Demand Driver template
//            ToggleAPI.updateToggle(Toggles.UseDemandDriverTemplateSwitch.getValue(), "jane.meng+006@legion.co", "P@ssword123",false);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            //Add new channel in settings.
            settingsAndAssociationPage.createNewChannelOrCategory(verifyType, channelName, description);
            //Verify newly added channel is in Forecast page
            switchToNewWindow();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            refreshPage();
            SimpleUtils.assertOnFail("The newly added channel not exist in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage(verifyType, channelName), false);

            //edit the channel in settings
            switchToNewWindow();
            settingsAndAssociationPage.clickOnEditBtnInSettings(verifyType, channelName, channelEditName);
            //verify edited channel is in Forecast page
            switchToNewWindow();
            refreshPage();
            SimpleUtils.assertOnFail("The edited channel not exist in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage(verifyType, channelEditName), false);

            //remove the channel in settings
            switchToNewWindow();
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, channelEditName);
            //verify the removed channel not show up in forecast page.
            switchToNewWindow();
            refreshPage();
            SimpleUtils.assertOnFail("The removed edited channel should not display in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage(verifyType, channelEditName), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Input Streams configuration in settings")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyInputStreamsConfigurationInSettingsForDemandDriverTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String inputStreamName1 = "InputStream-Base";
            String inputStreamName2 = "InputStream-Aggregated";
            String verifyType = "input stream";

            //input stream specification information to add
            List<HashMap<String, String>> inputStreamInfoToAdd = new ArrayList<>();
            HashMap<String, String> inputStreamInfoToAdd1 = new HashMap<String, String>(){
                {
                    put("Name", inputStreamName1);
                    put("Type", "Base");
                    put("Tag", inputStreamName1);
                }
            };
            HashMap<String, String> inputStreamInfoToAdd2 = new HashMap<String, String>(){
                {
                    put("Name", inputStreamName2);
                    put("Type", "Aggregated");
                    put("Operator", "IN");
                    put("Streams", "All");
                    put("Tag", inputStreamName2);
                }
            };
            inputStreamInfoToAdd.add(inputStreamInfoToAdd1);
            inputStreamInfoToAdd.add(inputStreamInfoToAdd2);

            //input stream specification information to edit
            List<HashMap<String, String>> inputStreamInfoToEdit = new ArrayList<>();
            HashMap<String, String> inputStreamInfoToEdit1 = new HashMap<String, String>(){
                {
                    put("Name", inputStreamName1);
                    put("Type", "Base");
                    put("Tag", "Items:EDW:Enrollments-Updated");
                }
            };
            HashMap<String, String> inputStreamInfoToEdit2 = new HashMap<String, String>(){
                {
                    put("Name", inputStreamName2);
                    put("Type", "Aggregated");
                    put("Operator", "NOT IN");
                    put("Streams", inputStreamName1);
                    put("Tag", "Items:EDW:Aggregated-Updated");
                }
            };
            inputStreamInfoToEdit.add(inputStreamInfoToEdit1);
            inputStreamInfoToEdit.add(inputStreamInfoToEdit2);

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            //Add new input stream in settings
            for (HashMap<String, String> inputStreamToAdd : inputStreamInfoToAdd){
                settingsAndAssociationPage.createInputStream(inputStreamToAdd);
                if (inputStreamToAdd.get("Type").equals("Aggregated")){
                    settingsAndAssociationPage.verifyIfAllBaseStreamsInListForAggregatedInputStream(inputStreamToAdd);
                }
            }

            //edit the input stream in settings
            settingsAndAssociationPage.clickOnEditBtnForInputStream(inputStreamInfoToAdd1, inputStreamInfoToEdit1);
            settingsAndAssociationPage.clickOnEditBtnForInputStream(inputStreamInfoToAdd2, inputStreamInfoToEdit2);

            //remove the category in settings
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, inputStreamName2);
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, inputStreamName1);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Very creating demand drivers template successfully")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 0)
    public void verifyCreateDemandDriverTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testDemand-NotDelete";
            String noDriverWarningMsg = "At least one demand driver is required when publishing a template";
            String noAssociationWarningMsg = "At least one association when publish template";
            String addOrEdit = "Add";
            String locationToAssociate = "Auto_ForDemandDriver";
            HashMap<String, String> driverSpecificInfo = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Enrollments");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");
            //Add new demand driver template, warning message will show up when no driver created
            while(configurationPage.searchTemplate(templateName)){
                configurationPage.clickOnTemplateName(templateName);
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
                configurationPage.deleteOneDynamicGroup(templateName);
                configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
                configurationPage.setLeaveThisPageButton();
                configurationPage.archiveOrDeleteTemplate(templateName);
            }
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("Publish Now");
            SimpleUtils.assertOnFail("There should be a warning message for no drivers", configurationPage.verifyWarningInfoForDemandDriver(noDriverWarningMsg), false);

            //Add new demand driver, warning message will show up when no association
            configurationPage.clickAddOrEditForDriver(addOrEdit);
            configurationPage.addOrEditDemandDriverInTemplate(driverSpecificInfo);
            configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("Publish Now");
            SimpleUtils.assertOnFail("There should be a warning message for no association", configurationPage.verifyWarningInfoForDemandDriver(noAssociationWarningMsg), false);

            //Add association and save
            configurationPage.createDynamicGroup(templateName, "Location Name", locationToAssociate);
            configurationPage.selectOneDynamicGroup(templateName);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify demand driver template detail duplicated adding check")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 1)
    public void verifyDemandDriverTemplateDetailsDuplicatedAddingCheckAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateName = "testDemand-NotDelete";
            String templateType = "Demand Drivers";
            String addOrEdit = "Add";

            HashMap<String, String> driverWithExistingName = new HashMap<String, String>()
            {
                {
                    put("Name", "Items:EDW:Enrollments");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> driverWithExistingCombination = new HashMap<String, String>()
            {
                {
                    put("Name", "Items:EDW:New");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");

            //Choose an existing template, add a driver with existing name
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver(addOrEdit);
            configurationPage.addOrEditDemandDriverInTemplate(driverWithExistingName);

            //Choose an existing template, add a driver with existing Type+Channel+Category
            configurationPage.clickAddOrEditForDriver(addOrEdit);
            configurationPage.addOrEditDemandDriverInTemplate(driverWithExistingCombination);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Very demand drivers template as publish now")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 2)
    public void verifyDemandDriverTemplateAsPublishNowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateName = "testDemand-NotDelete";
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("dd, yyyy");
            Calendar calendar = Calendar.getInstance();
            dfs.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            Month month = LocalDate.now().getMonth();
            String shortMonth = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " ";
            String currentDate = (shortMonth + dfs.format(calendar.getTime())).replace(" 0", " ");

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");

            //Check the template with published and draft version
            configurationPage.verifyPublishedTemplateAfterEdit(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            //Get and verify effective date
            String effectiveDate = configurationPage.getEffectiveDateForTemplate(templateName).get(0);
            SimpleUtils.assertOnFail("the effective Date is not Today!", effectiveDate.equalsIgnoreCase(currentDate), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Very demand drivers template details editing")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 3)
    public void verifyDemandDriverTemplateDetailsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateName = "testDemand-NotDelete";
            String templateType = "Demand Drivers";
            HashMap<String, String> driverSpecificInfo = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };

            HashMap<String, String> driverSpecificInfoUpdated = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications-Updated");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "2");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");

            //Enter the template, add a driver
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(driverSpecificInfo);
            SimpleUtils.assertOnFail("Can not find the driver you search!", configurationPage.searchDriverInTemplateDetailsPage(driverSpecificInfo.get("Name")), false);

            //Update the added driver
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.addOrEditDemandDriverInTemplate(driverSpecificInfoUpdated);
            SimpleUtils.assertOnFail("Can not find the driver you search!", configurationPage.searchDriverInTemplateDetailsPage(driverSpecificInfoUpdated.get("Name")), false);

            //Remove the added driver
            configurationPage.clickRemove();
            SimpleUtils.assertOnFail("Failed to removed the driver!", !configurationPage.searchDriverInTemplateDetailsPage(driverSpecificInfoUpdated.get("Name")), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate Creation for Input Stream in Settings page.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 0)
    public void verifyCreationForInputStreamInSettingsPageCAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String verifyType = "input stream";
            String baseInputStreamName1 = "InputStreamTest-Base01";
            String aggregatedInputStreamName1 = "InputStreamTest-Aggregated01";
            String aggregatedInputStreamName2 = "InputStreamTest-Aggregated02";

            //input stream specification information to add
            HashMap<String, String> baseInputStreamInfoToAdd = new HashMap<String, String>(){
                {
                    put("Name", baseInputStreamName1);
                    put("Type", "Base");
                    put("Tag", "Items:EDW:Base02");
                }
            };
            HashMap<String, String> aggregatedInputStreamInfoToAdd1 = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName1);
                    put("Type", "Aggregated");
                    put("Operator", "IN");
                    put("Streams", "All");
                }
            };
            HashMap<String, String> aggregatedInputStreamInfoToAdd2 = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName2);
                    put("Type", "Aggregated");
                    put("Operator", "NOT IN");
                    put("Streams", baseInputStreamName1);
                }
            };

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");

            //Add new Base input stream in settings
            settingsAndAssociationPage.createInputStream(baseInputStreamInfoToAdd);
            WebElement baseSearchElement = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, baseInputStreamName1);
            settingsAndAssociationPage.verifyInputStreamInList(baseInputStreamInfoToAdd, baseSearchElement);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");

            //Add new Aggregated input stream with IN Operator in settings
            settingsAndAssociationPage.createInputStream(aggregatedInputStreamInfoToAdd1);
            WebElement aggregatedSearchElement = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, aggregatedInputStreamName1);
            settingsAndAssociationPage.verifyInputStreamInList(aggregatedInputStreamInfoToAdd1, aggregatedSearchElement);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");

            //Add an input stream with existing name
            settingsAndAssociationPage.createInputStream(baseInputStreamInfoToAdd);

            //Add new Aggregated input stream with NOT IN Operator in settings
            settingsAndAssociationPage.createInputStream(aggregatedInputStreamInfoToAdd2);
            WebElement aggregatedSearchElement2 = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, aggregatedInputStreamName2);
            settingsAndAssociationPage.verifyInputStreamInList(aggregatedInputStreamInfoToAdd2, aggregatedSearchElement2);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Edit input stream in settings.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 1)
    public void verifyEditForInputStreamInSettingsPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHH");
            String currentDate = sdf.format(new Date());
            String templateType = "Demand Drivers";
            String verifyType = "input stream";
            String baseInputStreamName1 = "InputStreamTest-Base01";
            String aggregatedInputStreamName1 = "InputStreamTest-Aggregated01";
            String aggregatedInputStreamName2 = "InputStreamTest-Aggregated02";

            //input stream specification information to edit
            HashMap<String, String> baseInputStreamInfo = new HashMap<String, String>(){
                {
                    put("Name", baseInputStreamName1);
                    put("Type", "Base");
                    put("Tag", "Items:EDW:Base01" + currentDate);
                }
            };
            HashMap<String, String> aggregatedInputStreamInfo1 = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName1);
                    put("Type", "Aggregated");
                    put("Operator", "IN");
                    put("Streams", "All");
                }
            };
            HashMap<String, String> aggregatedInputStreamInfo2 = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName2);
                    put("Type", "Aggregated");
                    put("Operator", "NOT IN");
                    put("Streams", baseInputStreamName1 + currentDate);
                }
            };

            //update input stream to below specification information
            HashMap<String, String> baseInputStreamInfoUpdated = new HashMap<String, String>(){
                {
                    put("Name", baseInputStreamName1);
                    put("Type", "Base");
                    put("Tag", "Items:EDW:Base01-Update" + currentDate);
                }
            };
            HashMap<String, String> aggregatedInputStreamInfoUpdated1 = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName1);
                    put("Type", "Aggregated");
                    put("Operator", "NOT IN");
                    put("Streams", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> aggregatedInputStreamInfoUpdated2 = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName2);
                    put("Type", "Aggregated");
                    put("Operator", "IN");
                    put("Streams", "All");
                }
            };

            //update input stream to below specification information
            HashMap<String, String> baseInputStreamToAggregated = new HashMap<String, String>(){
                {
                    put("Name", baseInputStreamName1);
                    put("Type", "Aggregated");
                    put("Operator", "IN");
                    put("Streams", "All");
                }
            };
            HashMap<String, String> aggregatedInputStreamToBase = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName1);
                    put("Type", "Base");
                    put("Tag", aggregatedInputStreamName1 + "-to-Base");
                }
            };
            HashMap<String, String> baseInputStreamToAggregatedWithNotIn = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName1);
                    put("Type", "Aggregated");
                    put("Operator", "NOT IN");
                    put("Streams", "Items:EDW:Verifications");
                }
            };

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");

            //Choose the Base input stream to edit, change the data tag
            settingsAndAssociationPage.clickOnEditBtnForInputStream(baseInputStreamInfo, baseInputStreamInfoUpdated);
            WebElement baseSearchElement = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, baseInputStreamName1);
            settingsAndAssociationPage.verifyInputStreamInList(baseInputStreamInfoUpdated, baseSearchElement);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");

            //Choose the Aggregated input stream to edit, change IN to NOT IN
            settingsAndAssociationPage.clickOnEditBtnForInputStream(aggregatedInputStreamInfo1, aggregatedInputStreamInfoUpdated1);
            WebElement aggregatedSearchElement = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, aggregatedInputStreamName1);
            settingsAndAssociationPage.verifyInputStreamInList(aggregatedInputStreamInfoUpdated1, aggregatedSearchElement);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");

            //Choose the Aggregated input stream to edit, change NOT IN to IN
            settingsAndAssociationPage.clickOnEditBtnForInputStream(aggregatedInputStreamInfo2, aggregatedInputStreamInfoUpdated2);
            WebElement aggregatedSearchElement01 = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, aggregatedInputStreamName2);
            settingsAndAssociationPage.verifyInputStreamInList(aggregatedInputStreamInfoUpdated2, aggregatedSearchElement01);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");

            //Choose the Base input stream to edit, change to Aggregated With IN
            settingsAndAssociationPage.clickOnEditBtnForInputStream(baseInputStreamInfoUpdated, baseInputStreamToAggregated);
            WebElement baseToAggregatedSearchElement = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, baseInputStreamName1);
            settingsAndAssociationPage.verifyInputStreamInList(baseInputStreamToAggregated, baseToAggregatedSearchElement);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");

            //Choose the Aggregated input stream to edit, change to Base
            settingsAndAssociationPage.clickOnEditBtnForInputStream(aggregatedInputStreamInfoUpdated1, aggregatedInputStreamToBase);
            WebElement aggregatedToBaseSearchElement = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, aggregatedInputStreamName1);
            settingsAndAssociationPage.verifyInputStreamInList(aggregatedInputStreamToBase, aggregatedToBaseSearchElement);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");

            //Choose the Base input stream to edit, change to Aggregated with NOT IN
            settingsAndAssociationPage.clickOnEditBtnForInputStream(aggregatedInputStreamToBase, baseInputStreamToAggregatedWithNotIn);
            WebElement baseToAggregatedWithNotInSearchElement = settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, aggregatedInputStreamName1);
            settingsAndAssociationPage.verifyInputStreamInList(baseInputStreamToAggregatedWithNotIn, baseToAggregatedWithNotInSearchElement);
            settingsAndAssociationPage.searchSettingsForDemandDriver(verifyType, "");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify remove input stream in settings.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 2)
    public void verifyRemoveInputStreamAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String verifyType = "input stream";
            String baseInputStreamName1 = "InputStreamTest-Base01";
            String aggregatedInputStreamName1 = "InputStreamTest-Aggregated01";
            String aggregatedInputStreamName2 = "InputStreamTest-Aggregated02";

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");

            //Remove
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, baseInputStreamName1);
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, aggregatedInputStreamName1);
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, aggregatedInputStreamName2);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify demand drivers template as draft")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 4)
    public void verifyDemandDriverTemplatesAsDraftAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testDemand-NotDelete";
            String driverNameToEdit = "Items:EDW:Enrollments";

            HashMap<String, String> driverSpecificInfoUpdated = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Enrollments-Updated");
                    put("Show in App", "No");
                }
            };
            HashMap<String, String> driverSpecificInfoToAdd = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications");
                    put("Type", "Amount");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");

            //Choose one demand driver, edit
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Search and edit a driver
            configurationPage.searchDriverInTemplateDetailsPage(driverNameToEdit);
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.addOrEditDemandDriverInTemplate(driverSpecificInfoUpdated);
            configurationPage.searchDriverInTemplateDetailsPage(driverSpecificInfoUpdated.get("Name"));

            //Add a driver
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(driverSpecificInfoToAdd);
            configurationPage.searchDriverInTemplateDetailsPage(driverSpecificInfoToAdd.get("Name"));
            configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("Save as Draft");

            //Check the template information and effective date
            configurationPage.verifyMultipleTemplateListUI(templateName);
           // SimpleUtils.assertOnFail("The effective date for the draft version should be empty!", configurationPage.getEffectiveDateForTemplate(templateName).get(1).equals(""), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify demand drivers template archive")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 5)
    public void verifyDemandDriverTemplatesToArchiveAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testDemand-NotDelete";

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");
            //Delete the association and Archive the template
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Check UI for Input Stream in Settings page.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUIForInputStreamsInSettingsPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            HashMap<String, String> baseInputStreamInfoToAdd = new HashMap<String, String>(){
                {
                    put("Name", "baseInputStream-test");
                    put("Type", "Base");
                    put("Tag", "Items:EDW:Base01");
                }
            };
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            //Archive the template
            settingsAndAssociationPage.createInputStream(baseInputStreamInfoToAdd);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Very demand drivers template landing page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDemandDriversTemplateLandingPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testDemand-NotDelete";

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            //Check information for the template
            configurationPage.verifyMultipleTemplateListUI(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify default input streams when enter a new enterprise.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 7)
    public void verifyTheDefaultInputStreamsWhenEnterANewEnterpriseAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            List<String> baseStreamNames = new ArrayList<>();
            List<String> aggregatedStreamNames = new ArrayList<>();
            String verifyType = "input stream";
            int streamsCount = 0;
            int channelsCount = 0;
            int categoriesCount = 0;

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            baseStreamNames = settingsAndAssociationPage.getStreamNamesInList("Base");
            aggregatedStreamNames = settingsAndAssociationPage.getStreamNamesInList("Aggregated");
            if (aggregatedStreamNames.size() != 0){
                for (String streamName : aggregatedStreamNames){
                    settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, streamName);
                }
            }
            if (baseStreamNames.size() != 0){
                for (String streamName : baseStreamNames){
                    settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, streamName);
                }
            }

            //After remove all the input streams, calculate the generated default input stream.
            streamsCount = settingsAndAssociationPage.getTotalNumberForChannelOrCategory(verifyType);
            channelsCount = settingsAndAssociationPage.getTotalNumberForChannelOrCategory("channel");
            categoriesCount = settingsAndAssociationPage.getTotalNumberForChannelOrCategory("category");

            if (streamsCount == (channelsCount * categoriesCount)){
                SimpleUtils.pass("Default input streams are generated correctly!");
            }else if(streamsCount == 0 ){
                SimpleUtils.fail("No default input stream generated! ", false);
            }else{
                SimpleUtils.fail("Total number of default input stream is not correct! ", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify default demand driver template when enter a new enterprise.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, priority = 6)
    public void verifyTheDefaultDemandDriverTemplateWhenEnterANewEnterpriseAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            //Go to template association page, delete all templates
            configurationPage.clickOnSpecifyTemplateName("Default", "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup("Default");
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();

            //Remove all the current templates
            configurationPage.removeAllDemandDriverTemplates();

            //Check if default template generated
            SimpleUtils.assertOnFail("There is no default template generated after delete All!", configurationPage.searchTemplate("Default"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify input stream in demand driver")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyInputStreamInDemandDriverTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "Default";
            List<String> streamNameList = new ArrayList<>();
            List<String> streamNameListForCertainGranularity = new ArrayList<>();
            List<String> streamNamesInDriverPage = new ArrayList<>();

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            //Go to settings page, get all the input streams
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            streamNameList = settingsAndAssociationPage.getStreamNamesInList("All");
            for (String streamName : streamNameList){
                settingsAndAssociationPage.clickEditBtn(streamName);
                String granularityValue = settingsAndAssociationPage.getGranularityForCertainInputStream();
                if (granularityValue.equals("Slot (30 min)"))
                    streamNameListForCertainGranularity.add(streamName);
            }

            //Go to driver details page, get all the input streams
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Add");
            streamNamesInDriverPage = configurationPage.getInputStreamInDrivers();

            Collections.sort(streamNameListForCertainGranularity);
            Collections.sort(streamNamesInDriverPage);
            if(streamNameListForCertainGranularity.size()==streamNamesInDriverPage.size()){
                if(ListUtils.isEqualList(streamNameListForCertainGranularity, streamNamesInDriverPage)){
                    SimpleUtils.pass("Input Streams in driver details page and Settings page are totally the same!");
                }else {
                    SimpleUtils.fail("Input Streams in driver details page and Settings page are NOT the same!",false);
                }
            }else {
                SimpleUtils.fail("Input Streams in driver details page and Settings page are NOT the same!",false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Forecast page when only default demand driver template exist")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false, priority = 7)
    public void verifyVisibilityOnForecastPageForDefaultDemandDriverTemplateOnlyAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "Default";
            List<String> channelNameList = new ArrayList<>();
            List<String> categoryNameList = new ArrayList<>();
            String locationToTest = "Boston";


            //Turn off UseDemandDriverTemplateSwitch toggle
//            ToggleAPI.updateToggle(Toggles.UseDemandDriverTemplateSwitch.getValue(), "jane.meng+006@legion.co", "P@ssword123", false);
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();

            //Remove all the current templates
            configurationPage.removeAllDemandDriverTemplates();
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            channelNameList = settingsAndAssociationPage.getAllChannelsOrCategories("Channel");
            categoryNameList = settingsAndAssociationPage.getAllChannelsOrCategories("Category");
            //Verify channel/category visibility in Forecast page
            switchToNewWindow();
            refreshPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationToTest);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            //Check visibility for each channel&category
            for (int i = 0; i < channelNameList.size(); i++){
                SimpleUtils.assertOnFail("The channel " + channelNameList.get(i) + " should show up in forecast page!",
                        salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", channelNameList.get(i)), false);
            }
            for (int i = 0; i < categoryNameList.size(); i++){
                SimpleUtils.assertOnFail("The category " + categoryNameList.get(i) + " should show up in forecast page!",
                        salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", categoryNameList.get(i)), false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Forecast page for one newly created demand driver template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyForecastPageForOneNewlyCreatedDemandDriverTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testVisibility";
            HashMap<String, String> visibledriverInfo = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Enrollments");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> invisibledriverInfo = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "2");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");
            //Add new demand driver template
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Add two drivers, set up visible and invisible
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(visibledriverInfo);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(invisibledriverInfo);
            //Add association and save
            configurationPage.createDynamicGroup(templateName, "Location Name", location);
            configurationPage.selectOneDynamicGroup(templateName);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Verify channel/category visibility in Forecast page
            refreshCache("Template");
            switchToNewWindow();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            SimpleUtils.assertOnFail("The channel EDW should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", visibledriverInfo.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Enrollments should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  visibledriverInfo.get("Category")), false);
            SimpleUtils.assertOnFail("The category Verifications should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", invisibledriverInfo.get("Category")), false);
            //Remove the associated locations and templates
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.deleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Forecast page for different newly created demand driver template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyForecastPageForDifferentNewlyCreatedDemandDriverTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName1 = "testVisibility_Diff1";
            String templateName2 = "testVisibility_Diff2";
            String locationName1 = "Boston";
            String locationName2 = "Cleveland";
            HashMap<String, String> invisibleInfo_location1 = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Enrollments");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> invisibleInfo_location2 = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");
            //Add first demand driver template
            configurationPage.createNewTemplate(templateName1);
            configurationPage.clickOnTemplateName(templateName1);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Add one driver, set up visibility
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(invisibleInfo_location1);
            //Add association and save
            configurationPage.createDynamicGroup(templateName1, "Location Name", locationName1);
            configurationPage.selectOneDynamicGroup(templateName1);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Add second demand driver template
            configurationPage.createNewTemplate(templateName2);
            configurationPage.clickOnTemplateName(templateName2);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Add one driver, set up visibility
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(invisibleInfo_location2);
            //Add association and save
            configurationPage.createDynamicGroup(templateName2, "Location Name", locationName2);
            configurationPage.selectOneDynamicGroup(templateName2);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Verify channel/category visibility in Forecast page
            refreshCache("Template");
            switchToNewWindow();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName1);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            //Check channel/category visibility for location1
            SimpleUtils.assertOnFail("The channel EDW should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", invisibleInfo_location1.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Verifications should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  invisibleInfo_location2.get("Category")), false);
            SimpleUtils.assertOnFail("The category Enrollments should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", invisibleInfo_location1.get("Category")), false);

            //Check channel/category visibility for location2
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName1);
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            SimpleUtils.assertOnFail("The channel EDW should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", invisibleInfo_location2.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Enrollments should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  invisibleInfo_location1.get("Category")), false);
            SimpleUtils.assertOnFail("The category Verifications should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", invisibleInfo_location2.get("Category")), false);
            switchToNewWindow();

            //Remove the associated locations and templates
            configurationPage.clickOnSpecifyTemplateName(templateName1, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName1);
            configurationPage.deleteOneDynamicGroup(templateName2);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.deleteTemplate(templateName1);
            configurationPage.deleteTemplate(templateName2);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Forecast page for one newly created and default demand driver template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyForecastPageForOneNewlyCreatedDemandDriverTemplateForDGV2AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testVisibility_V2";
            String locationName1 = "Boston_V2";
            String locationName2 = "Cleveland_V2";
            HashMap<String, String> invisibleInfo_location = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> visibleInfo_location = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Enrollments");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "2");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");

            //Add first demand driver template
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Add one driver, set up visibility
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(invisibleInfo_location);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(visibleInfo_location);
            //Add association and save
            configurationPage.createDynamicGroup(templateName, "Location Name", locationName1);
            configurationPage.selectOneDynamicGroup(templateName);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Verify channel/category visibility in Forecast page
            refreshCache("Template");
            switchToNewWindow();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName1);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            //Check channel/category visibility for location1
            SimpleUtils.assertOnFail("The channel EDW should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", visibleInfo_location.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Verifications should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  invisibleInfo_location.get("Category")), false);
            SimpleUtils.assertOnFail("The category Enrollments should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", visibleInfo_location.get("Category")), false);

            //Check channel/category visibility for location2
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName2);
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            SimpleUtils.assertOnFail("The channel EDW should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", visibleInfo_location.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Verifications should  show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  invisibleInfo_location.get("Category")), false);
            SimpleUtils.assertOnFail("The category Enrollments should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", visibleInfo_location.get("Category")), false);
            //Remove the associated locations and templates
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Forecast page for different newly created demand driver template for V2")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyForecastPageForDifferentNewlyCreatedDemandDriverTemplateForDGV2AsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName1 = "testVisibility1_V2";
            String templateName2= "testVisibility2_V2";
            String locationName1 = "Boston_V2";
            String locationName2 = "Cleveland_V2";
            HashMap<String, String> invisibleInfo_location1 = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Enrollments");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> visibleInfo_location1 = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "2");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> invisibleInfo_location2 = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Verifications");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> visibleInfo_location2 = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Enrollments");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "2");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            //Turn on DynamicGroupV2 toggle
//            ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), "jane.meng+006@legion.co", "P@ssword123",true);
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");
            //Add first demand driver template
            configurationPage.createNewTemplate(templateName1);
            configurationPage.searchTemplate("Default");
            configurationPage.archivePublishedOrDeleteDraftTemplate("Default", "Archive");
            configurationPage.searchTemplate(templateName1);
            configurationPage.clickOnTemplateName(templateName1);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Add one driver, set up visibility
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(invisibleInfo_location1);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(visibleInfo_location1);
            //Add association and save
            configurationPage.createDynamicGroup(templateName1, "Location Name", locationName1);
            configurationPage.selectOneDynamicGroup(templateName1);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Add second demand driver template
            configurationPage.createNewTemplate(templateName2);
            configurationPage.clickOnTemplateName(templateName2);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Add one driver, set up visibility
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(invisibleInfo_location2);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(visibleInfo_location2);
            //Add association and save
            configurationPage.createDynamicGroup(templateName2, "Location Name", locationName2);
            configurationPage.selectOneDynamicGroup(templateName2);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Verify channel/category visibility in Forecast page
            refreshCache("Template");
            switchToNewWindow();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName1);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            //Check channel/category visibility for location1
            SimpleUtils.assertOnFail("The channel EDW should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", visibleInfo_location1.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Enrollments should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  invisibleInfo_location1.get("Category")), false);
            SimpleUtils.assertOnFail("The category Verifications should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", visibleInfo_location1.get("Category")), false);

            //Check channel/category visibility for location2
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName1);
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            SimpleUtils.assertOnFail("The channel EDW should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", visibleInfo_location2.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Enrollments should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  invisibleInfo_location2.get("Category")), false);
            SimpleUtils.assertOnFail("The category Verifications should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", visibleInfo_location2.get("Category")), false);
            //Remove the associated locations and templates
            configurationPage.archiveOrDeleteAllTemplates();
            //Turn off DynamicGroupV2 toggle
//            ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), "jane.meng+006@legion.co", "P@ssword123",false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Forecast page for newly created demand drivers with new Channel/Category for V2")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyForecastPageForDemandDriversTemplateWithNewChannelAndCategoryAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testVisibility_NewChannelAndCategory";
            String channelName = "Channel_Gate";
            String categoryName = "Category_Coffee";
            String locationName = "Boston";
            HashMap<String, String> invisibleInfo = new HashMap<String, String>(){
                {
                    put("Name", "Items:EDW:Category_Coffee");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", categoryName);
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> visibleInfo = new HashMap<String, String>(){
                {
                    put("Name", "Items:Channel_Gate:Verifications");
                    put("Type", "Items");
                    put("Channel", channelName);
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "2");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            //Turn on DynamicGroupV2 toggle
//            ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), "jane.meng+006@legion.co", "P@ssword123",true);
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            //Add new category in settings.
            settingsAndAssociationPage.createNewChannelOrCategory("category", categoryName, "This is a test for new Category visibility!");
            settingsAndAssociationPage.createNewChannelOrCategory("channel", channelName, "This is a test for new Channel visibility!");
            //Go to Templates tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Templates");
            //Add first demand driver template
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Add two drivers, set up visibility
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(invisibleInfo);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(visibleInfo);
            //Add association and save
            configurationPage.createDynamicGroup(templateName, "Location Name", locationName);
            configurationPage.selectOneDynamicGroup(templateName);

            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Verify channel/category visibility in Forecast page
            refreshCache("Template");
            switchToNewWindow();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName);
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
            scheduleCommonPage.goToSchedulePage();
            forecastPage.clickForecast();
            salesForecastPage.navigateToSalesForecastTab();
            //Check channel/category visibility for location
            SimpleUtils.assertOnFail("The channel EDW should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", invisibleInfo.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Category_Coffee should not show up in forecast page!",
                    !salesForecastPage.verifyChannelOrCategoryExistInForecastPage("channel", invisibleInfo.get("Category")), false);
            SimpleUtils.assertOnFail("The channel Channel_Gate should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand",  visibleInfo.get("Channel")), false);
            SimpleUtils.assertOnFail("The category Verifications should show up in forecast page!",
                    salesForecastPage.verifyChannelOrCategoryExistInForecastPage("demand", visibleInfo.get("Category")), false);

            //Turn off DynamicGroupV2 toggle
//            ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), "jane.meng+006@legion.co", "P@ssword123",false);
            switchToNewWindow();
            //Remove the associated locations and templates
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.archiveOrDeleteTemplate(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Check UI for Distributed demand driver page.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyForDistributedDemandDriverUIAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testDistributed";
            String derivedType = "Distributed";

            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.verifyForDerivedDemandDriverUI(derivedType, null);
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Check UI for Remote demand driver page.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyForRemoteDemandDriverUIAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testRemote";
            String derivedType = "Remote";
            String remoteType = "Remote Location";
            String parentType = "Parent Location";

            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.verifyForDerivedDemandDriverUI(derivedType, remoteType);
            configurationPage.clickOnCancelButton();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.verifyForDerivedDemandDriverUI(derivedType, parentType);
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Check UI for Aggregated demand driver page.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyForAggregatedDemandDriverUIAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        try {
        String templateType = "Demand Drivers";
        String templateName = "testAggregated";
        String derivedType = "Aggregated";

        //Turn on EnableTahoeStorage toggle
//        ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//        refreshPage();
        //Go to Demand Driver template
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
        configurationPage.goToConfigurationPage();
        configurationPage.clickOnConfigurationCrad(templateType);
        //Go to Template tab
        settingsAndAssociationPage.goToTemplateListOrSettings("Template");
        configurationPage.createNewTemplate(templateName);
        configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
        configurationPage.clickOnEditButtonOnTemplateDetailsPage();
        configurationPage.clickAddOrEditForDriver("Add");
        configurationPage.verifyForDerivedDemandDriverUI(derivedType, null);
//        ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate Creation&Modification for Remote Demand Driver.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreationAndEditForRemoteDemandDriverAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm ");
            String currentTime =  dfs.format(new Date()).trim();
            String templateName = "testCreation_Remote" + currentTime;
            String warningMsg = "can not be set as remote location because it is associated with the same demand driver template";
            HashMap<String, String> parentLocationDriver = new HashMap<String, String>(){
                {
                    put("Name", "ParentLocationDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Remote");
                    put("Specify Location", "Parent Location");
                    put("Parent Level", "2");
                }
            };
            HashMap<String, String> remoteLocationDriver_inAssociation = new HashMap<String, String>(){
                {
                    put("Name", "RemoteLocationDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Remote");
                    put("Specify Location", "Remote Location");
                    put("Choose Remote Location", "Austin");
                }
            };
            HashMap<String, String> remoteLocationDriver = new HashMap<String, String>(){
                {
                    put("Name", "RemoteLocationDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Remote");
                    put("Specify Location", "Remote Location");
                    put("Choose Remote Location", "Boston");
                }
            };

            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Template tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Add a Remote:ParentLocation demand driver
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(parentLocationDriver);
            //Add a Remote:RemoteLocation demand driver
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(remoteLocationDriver_inAssociation);
            //Add association
            configurationPage.selectOneDynamicGroup("testDerived_NotDelete");
            //Should not be able to publish
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("publish now");
            SimpleUtils.assertOnFail("Please check if there's a warning message and the content!", configurationPage.verifyWarningForDemandDriver(warningMsg), false);
            SimpleUtils.assertOnFail("Can't find the driver you search!", configurationPage.searchDriverInTemplateDetailsPage(remoteLocationDriver_inAssociation.get("Name")), false);
            //Edit the remote location driver and publish once again
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.addOrEditDemandDriverInTemplate(remoteLocationDriver);
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();
            configurationPage.archiveOrDeleteTemplate(templateName);
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate Creation&Modification for Distributed Demand Driver.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreationAndEditForDistributedDemandDriverAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String templateName = "testCreation_Distributed" + currentTime;
            HashMap<String, String> distributedDriver = new HashMap<String, String>(){
                {
                    put("Name", "DistributedDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Distributed");
                    put("Source Demand Driver", "LegionMLDriver");
                    put("Distribution of Demand Driver", "ImportedDriver");
                }
            };
            HashMap<String, String> legionMLDriver = new HashMap<String, String>(){
                {
                    put("Name", "LegionMLDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> importedDriver = new HashMap<String, String>(){
                {
                    put("Name", "ImportedDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "2");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> distributedChangedToremoteDriver = new HashMap<String, String>(){
                {
                    put("Name", "RemoteLocationDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "3");
                    put("Forecast Source", "Remote");
                    put("Specify Location", "Remote Location");
                    put("Choose Remote Location", "Boston");
                }
            };
            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Template tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Add a distributed demand driver, can't save, cancel
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(distributedDriver);
            //Add only one basic driver first
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(legionMLDriver);
            //Add distributed driver, can't save, cancel
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(distributedDriver);
            //Add the second basic driver then the distributed driver
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(importedDriver);
            //Add distributed driver, and publish
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(distributedDriver);
            SimpleUtils.assertOnFail("Can't find the distributed driver you search!", configurationPage.searchDriverInTemplateDetailsPage(distributedDriver.get("Name")), false);
            configurationPage.selectOneDynamicGroup("testDerived_NotDelete");
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //distributed changed to remote driver
            configurationPage.clickOnSpecifyTemplateName(templateName, "Edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.searchDriverInTemplateDetailsPage(distributedDriver.get("Name"));
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.addOrEditDemandDriverInTemplate(distributedChangedToremoteDriver);

            SimpleUtils.assertOnFail("Can't find the remote driver you search!", configurationPage.searchDriverInTemplateDetailsPage(distributedChangedToremoteDriver.get("Name")), false);
            configurationPage.searchDriverInTemplateDetailsPage("");
            //Publish again after modify the driver
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();
            configurationPage.archiveOrDeleteTemplate(templateName);
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate Creation&Modification for Aggregated Demand Driver.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreationAndEditForAggregatedDemandDriverAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String templateName = "testCreation_Aggregated" + currentTime;
            HashMap<String, String> aggregatedDriver = new HashMap<String, String>(){
                {
                    put("Name", "AggregatedDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Aggregated");
                    put("Options", "LegionMLDriver,-1.78;ImportedDriver,3.21");
                }
            };
            HashMap<String, String> legionMLDriver = new HashMap<String, String>(){
                {
                    put("Name", "LegionMLDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "2");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> importedDriver = new HashMap<String, String>(){
                {
                    put("Name", "ImportedDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "3");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> aggregatedChangedToremoteDriver = new HashMap<String, String>(){
                {
                    put("Name", "DistributedDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "4");
                    put("Forecast Source", "Distributed");
                    put("Source Demand Driver", "LegionMLDriver");
                    put("Distribution of Demand Driver", "ImportedDriver");
                }
            };

            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Template tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Add a aggregated demand driver, can't save, cancel
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(aggregatedDriver);
            //Add only one basic driver first
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(legionMLDriver);
            //Add aggregated driver, can't save, cancel
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(aggregatedDriver);
            //Add the second basic driver then the aggregated driver
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(importedDriver);
            //Add aggregated driver, and publish
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(aggregatedDriver);
            SimpleUtils.assertOnFail("Can't find the aggregated driver you search!", configurationPage.searchDriverInTemplateDetailsPage(aggregatedDriver.get("Name")), false);
            configurationPage.selectOneDynamicGroup("testDerived_NotDelete");
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //aggregated changed to aggregated driver
            configurationPage.clickOnSpecifyTemplateName(templateName, "Edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.searchDriverInTemplateDetailsPage(aggregatedDriver.get("Name"));
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.addOrEditDemandDriverInTemplate(aggregatedChangedToremoteDriver);

            SimpleUtils.assertOnFail("Can't find the aggregated driver you search!", configurationPage.searchDriverInTemplateDetailsPage(aggregatedChangedToremoteDriver.get("Name")), false);
            configurationPage.searchDriverInTemplateDetailsPage("");
            //Publish again after modify the driver
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();
            configurationPage.archiveOrDeleteTemplate(templateName);
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate view for Aggregated Demand Driver.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAggregatedDemandDriverInReadOnlyModeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String templateName = "testView_Aggregated" + currentTime;
            HashMap<String, String> aggregatedDriver = new HashMap<String, String>(){
                {
                    put("Name", "AggregatedDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Aggregated");
                    put("Options", "LegionMLDriver,-1.78;ImportedDriver,3.21");
                }
            };
            HashMap<String, String> legionMLDriver = new HashMap<String, String>(){
                {
                    put("Name", "LegionMLDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "2");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> importedDriver = new HashMap<String, String>(){
                {
                    put("Name", "ImportedDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "3");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Template tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Add two basic drivers first
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(legionMLDriver);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(importedDriver);
            //Add aggregated driver, and publish
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(aggregatedDriver);
            configurationPage.selectOneDynamicGroup("testDerived_NotDelete");
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //check in view mode
            configurationPage.clickOnSpecifyTemplateName(templateName, "Edit");
            configurationPage.searchDriverInTemplateDetailsPage(aggregatedDriver.get("Name"));
            SimpleUtils.assertOnFail("Verify aggregated driver page failed in read only mode!", configurationPage.verifyDriverInViewMode(aggregatedDriver), false);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.archiveOrDeleteTemplate(templateName);
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate view for Distributed Demand Driver.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDistributedDemandDriverInReadOnlyModeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String templateName = "testView_Distributed" + currentTime;
            HashMap<String, String> distributedDriver = new HashMap<String, String>(){
                {
                    put("Name", "DistributedDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Distributed");
                    put("Source Demand Driver", "LegionMLDriver");
                    put("Distribution of Demand Driver", "ImportedDriver");
                }
            };
            HashMap<String, String> legionMLDriver = new HashMap<String, String>(){
                {
                    put("Name", "LegionMLDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "2");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> importedDriver = new HashMap<String, String>(){
                {
                    put("Name", "ImportedDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "3");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Template tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Add two basic drivers first
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(legionMLDriver);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(importedDriver);
            //Add distributed driver, and publish
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(distributedDriver);
            configurationPage.selectOneDynamicGroup("testDerived_NotDelete");
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //check in view mode
            configurationPage.clickOnSpecifyTemplateName(templateName, "Edit");
            configurationPage.searchDriverInTemplateDetailsPage(distributedDriver.get("Name"));
            SimpleUtils.assertOnFail("Verify distributed driver page failed in read only mode!", configurationPage.verifyDriverInViewMode(distributedDriver), false);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.archiveOrDeleteTemplate(templateName);
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate view for Remote Demand Driver.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRemoteDemandDriverInReadOnlyModeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String templateName = "testView_Remote" + currentTime;
            HashMap<String, String> parentLocationDriver = new HashMap<String, String>(){
                {
                    put("Name", "ParentLocationDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Remote");
                    put("Specify Location", "Parent Location");
                    put("Parent Level", "2");
                }
            };
            HashMap<String, String> remoteLocationDriver = new HashMap<String, String>(){
                {
                    put("Name", "RemoteLocationDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "2");
                    put("Forecast Source", "Remote");
                    put("Specify Location", "Remote Location");
                    put("Choose Remote Location", "Boston");
                }
            };
            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Template tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Add remote driver, and publish
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(parentLocationDriver);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(remoteLocationDriver);
            configurationPage.selectOneDynamicGroup("testDerived_NotDelete");
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //check in view mode for remote location driver
            configurationPage.clickOnSpecifyTemplateName(templateName, "Edit");
            configurationPage.searchDriverInTemplateDetailsPage(parentLocationDriver.get("Name"));
            SimpleUtils.assertOnFail("Verify remote:parent driver page failed in read only mode!", configurationPage.verifyDriverInViewMode(parentLocationDriver), false);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            //check in view mode for parent location driver
            configurationPage.searchDriverInTemplateDetailsPage(remoteLocationDriver.get("Name"));
            SimpleUtils.assertOnFail("Verify remote:remote driver page failed in read only mode!", configurationPage.verifyDriverInViewMode(remoteLocationDriver), false);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user not allow to remove channels and categories in settings")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyRemoveChannelsAndCategoriesNotAllowedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            //-------------------will make it enabled when bug OPS-4600 fixed--------------
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String templateName = "testRemove_ChannelAndCategory";
            String channelName = "Channel_testRemove";
            String channelDes = "This is a test for channel remove!";
            String categoryName = "Category_testRemove";
            String categoryDes = "This is a test for Category remove!";
            HashMap<String, String> driverSpecificInfo = new HashMap<String, String>(){
                {
                    put("Name", "DriverTest_RemoveChannelAndCategory");
                    put("Type", "Items");
                    put("Channel", channelName);
                    put("Category", categoryName);
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            //Add new channel in settings.
            settingsAndAssociationPage.createNewChannelOrCategory("channel", channelName, channelDes);
            //Add new category in settings.
            settingsAndAssociationPage.createNewChannelOrCategory("category", categoryName, categoryDes);
            //Go to Template tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Add legionMl driver, and publish
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(driverSpecificInfo);
            configurationPage.selectOneDynamicGroup("testDerived_NotDelete");
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();
            //Remove channel and category in Settings
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            settingsAndAssociationPage.clickOnRemoveBtnInSettings("channel", channelName);
            settingsAndAssociationPage.clickOnRemoveBtnInSettings("category", categoryName);
            SimpleUtils.assertOnFail("Channel used in published template should not be able to remove!",
                    settingsAndAssociationPage.searchSettingsForDemandDriver("channel", channelName) != null, false);
            SimpleUtils.assertOnFail("Category used in published template should not be able to remove!",
                    settingsAndAssociationPage.searchSettingsForDemandDriver("category", categoryName) != null, false);
            //Archive the template
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify time configuration changed to text input")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeConfigurationChangedToTextInputAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String OHTemplateType = "Operating Hours";
            String SchedulingRulesTemplateType = "Scheduling Rules";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String OHTemplate = "OH" + currentTime;
            String SchedulingRulesTemplate = "SchedulingRules" + currentTime;
            String dayParts = "DP1-forAuto";
            String workRole = "Auto";

            //Go to OH template and create one
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OHTemplateType);
            configurationPage.createNewTemplate(OHTemplate);
            //Check time configuration changed to text input
            configurationPage.clickOnSpecifyTemplateName(OHTemplate, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Check it for "None"
            configurationPage.selectDaypart(dayParts);
            configurationPage.goToBusinessHoursEditPage("sunday");
            configurationPage.checkOpenAndCloseTime();
            configurationPage.clickOnCancelButton();
            //Check it for "Open / Close"
            configurationPage.selectOperatingBufferHours("StartEnd");
            configurationPage.clickOpenCloseTimeLink();
            configurationPage.checkOpenAndCloseTime();
            configurationPage.clickOnCancelButton();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.archiveOrDeleteTemplate(OHTemplate);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

            //Go to Scheduling Rules template and create one
            configurationPage.clickOnConfigurationCrad(SchedulingRulesTemplateType);
            configurationPage.createNewTemplate(SchedulingRulesTemplate);
            //Check time configuration changed to text input
            configurationPage.clickOnSpecifyTemplateName(SchedulingRulesTemplate, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.selectStartTimeEvent("Specified Hours");
            configurationPage.clickOpenCloseTimeLink();
            configurationPage.checkOpenAndCloseTime();
            configurationPage.clickOnCancelButton();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.archiveOrDeleteTemplate(OHTemplate);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify time validation for Operating Hours")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeValidationForOperatingHoursAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String OHTemplateType = "Operating Hours";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String OHTemplate = "OH" + currentTime;
            String dayParts = "DP1-forAuto";
            String crossNextDay = "Yes";
            List<String> dayOfWeek = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();

            //Go to OH template and create one
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OHTemplateType);
            configurationPage.createNewTemplate(OHTemplate);
            configurationPage.clickOnSpecifyTemplateName(OHTemplate, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Check default time configuration could save successfully
            configurationPage.selectDaypart(dayParts);
            configurationPage.createDynamicGroup(OHTemplate, "Custom", "Auto--Custom script" + currentTime);
            configurationPage.selectOneDynamicGroup(OHTemplate);
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.publishNowTemplate();
            configurationPage.clickOnSpecifyTemplateName(OHTemplate, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.goToBusinessHoursEditPage("sunday");

            configurationPage.setOpenCloseTime("Open/Close", "6:00AM", "5:00AM", "No");
            configurationPage.setOpenCloseTime("Open/Close", "12:00AM", "1:00AM", crossNextDay);
            configurationPage.clickOnCancelButton();
            configurationPage.goToBusinessHoursEditPage("sunday");
            configurationPage.setOpenCloseTime("Open/Close", "6:00AM", "5:00AM", crossNextDay);
            configurationPage.setOpenCloseTime("Dayparts", "6:00AM", "5:00AM", "No");
            configurationPage.setOpenCloseTime("Dayparts", "12:00AM", "1:00AM", crossNextDay);
            configurationPage.setOpenCloseTime("Dayparts", "6:00AM", "12:00PM", "No");
            configurationPage.saveBtnIsClickable();

            for (String day : dayOfWeek){
                if (day.equalsIgnoreCase("sunday")){
                    SimpleUtils.assertOnFail(day + "'s start time and end time is not as expected!", configurationPage.verifyStartEndTimeForDays("6:00 AM", "5:00 AM", "sunday"), false);
                }else{
                    SimpleUtils.assertOnFail(day + "'s start time and end time is not as expected!", configurationPage.verifyStartEndTimeForDays("12:00 AM", "12:00 AM", day), false);
                }
            }
            configurationPage.goToBusinessHoursEditPage("sunday");
            configurationPage.selectDaysForOpenCloseTime(dayOfWeek);
            configurationPage.saveBtnIsClickable();

            for (String day : dayOfWeek){
                SimpleUtils.assertOnFail(day + "'s start time and end time is not as expected!", configurationPage.verifyStartEndTimeForDays("6:00 AM", "5:00 AM", day), false);
            }
            configurationPage.publishNowTemplate();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify time validation for Scheduling Rules")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeValidationForSchedulingRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String SchedulingRulesTemplateType = "Scheduling Rules";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String SchedulingRulesTemplate = "SchedulingRules" + currentTime;
            String workRole = "Auto";
            String crossNextDay = "Yes";

            //Go to Scheduling Rules template and create one
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(SchedulingRulesTemplateType);
            configurationPage.createNewTemplate(SchedulingRulesTemplate);
            //Default open and close time in scheduling rules template
            configurationPage.clickOnSpecifyTemplateName(SchedulingRulesTemplate, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.selectStartTimeEvent("Specified Hours");
            configurationPage.clickOnSaveButtonOnScheduleRulesListPage();
            configurationPage.createDynamicGroup(SchedulingRulesTemplate, "Custom", "Auto--Custom script" + currentTime);
            configurationPage.selectOneDynamicGroup(SchedulingRulesTemplate);
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.publishNowTemplate();
            //Set open and close time in scheduling rules template
            configurationPage.clickOnSpecifyTemplateName(SchedulingRulesTemplate, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.editBasicStaffingRules();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.clickOpenCloseTimeLink();
            configurationPage.setOpenCloseTime("Open/Close", "6:00AM", "5:00AM", "No");
            configurationPage.setOpenCloseTime("Open/Close", "12:00AM", "1:00AM", crossNextDay);
            configurationPage.setOpenCloseTime("Open/Close", "6:00AM", "5:00AM", crossNextDay);
            configurationPage.saveBtnIsClickable();
            configurationPage.clickOnSaveButtonOnScheduleRulesListPage();
            configurationPage.publishNowTemplate();
            //Archive or delete the template
            configurationPage.clickOnSpecifyTemplateName(SchedulingRulesTemplate, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickOnAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(SchedulingRulesTemplate);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.archiveOrDeleteTemplate(SchedulingRulesTemplate);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Demand Driver is visible for Legion Internal User")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyDemandDriverIsAccessibleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHHmm");
            String currentTime =  dfs.format(new Date()).trim();
            String demandDriverTemplate = "CheckDerivedDriver" + currentTime;
            List<String> derivedDriverType = new ArrayList<String>(Arrays.asList("Remote", "Aggregated", "Distributed"));

            //Go to Configuration tab, check if demand driver exist.
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            SimpleUtils.assertOnFail("Demand Driver card should be visible for legion internal user!", configurationPage.verifyTemplateCardExist(templateType), false);

            //EnableTahoeStorage is on, verify could view derived driver
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.createNewTemplate(demandDriverTemplate);
            configurationPage.clickOnTemplateName(demandDriverTemplate);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Add");
            SimpleUtils.assertOnFail("Derived forecast source type should be visible with EnableTahoeStorage turned on!", configurationPage.getAllForecastSourceType().containsAll(derivedDriverType), false);

            //EnableTahoeStorage is off, verify could not view derived driver
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
            refreshPage();
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Add");
            SimpleUtils.assertOnFail("Derived forecast source type should be invisible with EnableTahoeStorage turned off!", !configurationPage.getAllForecastSourceType().containsAll(derivedDriverType), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Demand Driver is invisible for non-legion internal user")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDemandDriverIsNotAccessibleAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String normalAdminUserName = "jane.meng+admin01@legion.co";
            String adminPassword = "P@ssword123";

            //Go to Configuration tab, check demand driver should not exist.
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            OpsPortalNavigationPage opsPortalNavigationPage = new OpsPortalNavigationPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            configurationPage.goToConfigurationPage();
            SimpleUtils.assertOnFail("Demand Driver card should be invisible for store manager!", !configurationPage.verifyTemplateCardExist(templateType), false);
            switchToNewWindow();
            //logout
            loginPage.logOut();

            //log in with normal admin, check demand driver should not exist
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            SimpleUtils.report(getDriver().getCurrentUrl());
            loginPage.loginToLegionWithCredential(normalAdminUserName, adminPassword);
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            configurationPage.goToConfigurationPage();
            SimpleUtils.assertOnFail("Demand Driver card should be invisible for normal admin!", !configurationPage.verifyTemplateCardExist(templateType), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Predictability score is only for LegionML Forecast Source driver.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPredictabilityScoreIsOnlyForLegionMLForecastSourceAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "testPredictabilityScore";
            HashMap<String, String> legionMLDriver = new HashMap<String, String>(){
                {
                    put("Name", "LegionMLDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> importedLDriver = new HashMap<String, String>(){
                {
                    put("Name", "ImportedDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "Yes");
                    put("Order", "2");
                    put("Forecast Source", "Imported");
                    put("Input Stream", "Items:EDW:Verifications");
                }
            };
            HashMap<String, String> remoteLDriver = new HashMap<String, String>(){
                {
                    put("Name", "RemoteDriver");
                    put("Type", "Amount");
                    put("Channel", "Channel01");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Remote");
                    put("Specify Location", "Parent Location");
                    put("Parent Level", "2");
                }
            };
            HashMap<String, String> aggregatedDriver = new HashMap<String, String>(){
                {
                    put("Name", "AggregatedDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Aggregated");
                    put("Options", "LegionMLDriver,-1.98;ImportedDriver,7.23");
                }
            };
            HashMap<String, String> distributedDriver = new HashMap<String, String>(){
                {
                    put("Name", "DistributedDriver");
                    put("Type", "Items");
                    put("Channel", "Channel01");
                    put("Category", "Enrollments");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Distributed");
                    put("Source Demand Driver", "LegionMLDriver");
                    put("Distribution of Demand Driver", "ImportedDriver");
                }
            };
            //Turn on EnableTahoeStorage toggle
//            ToggleAPI.enableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //Check Predictability Score is visible for Legion ML Driver
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(legionMLDriver);
            configurationPage.clickAddOrEditForDriver("Edit");
            SimpleUtils.assertOnFail("Predictability Score should show up for Legion ML forecast source driver!", configurationPage.verifyPredictabilityScoreExist(), false);
            //Check Predictability Score is invisible for Imported Driver
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(importedLDriver);
            configurationPage.searchDriverInTemplateDetailsPage(importedLDriver.get("Name"));
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.setLeaveThisPageButton();
            SimpleUtils.assertOnFail("Predictability Score should NOT show up for Imported forecast source driver!", !configurationPage.verifyPredictabilityScoreExist(), false);
            //Check Predictability Score is invisible for Remote Driver
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(remoteLDriver);
            configurationPage.searchDriverInTemplateDetailsPage(remoteLDriver.get("Name"));
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.setLeaveThisPageButton();
            SimpleUtils.assertOnFail("Predictability Score should NOT show up for Remote forecast source driver!", !configurationPage.verifyPredictabilityScoreExist(), false);
            //Check Predictability Score is invisible for Aggregated Driver
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(aggregatedDriver);
            configurationPage.searchDriverInTemplateDetailsPage(aggregatedDriver.get("Name"));
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.setLeaveThisPageButton();
            SimpleUtils.assertOnFail("Predictability Score should NOT show up for Aggregated forecast source driver!", !configurationPage.verifyPredictabilityScoreExist(), false);
            //Check Predictability Score is invisible for Distributed Driver
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(distributedDriver);
            configurationPage.searchDriverInTemplateDetailsPage(distributedDriver.get("Name"));
            configurationPage.clickAddOrEditForDriver("Edit");
            configurationPage.setLeaveThisPageButton();
            SimpleUtils.assertOnFail("Predictability Score should NOT show up for Distributed forecast source driver!", !configurationPage.verifyPredictabilityScoreExist(), false);
            configurationPage.clickOnBackButton();
            configurationPage.createDynamicGroup(templateName, "Custom", templateName + "test");
            configurationPage.selectOneDynamicGroup(templateName);
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();

            //Delete the association and archive the template
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.archiveOrDeleteTemplate(templateName);

            //Turn off EnableTahoeStorage toggle
//            ToggleAPI.disableToggle(Toggles.EnableTahoeStorage.getValue(), "jane.meng+006@legion.co", "P@ssword123");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Predictability score is only enabled for ever published template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPredictabilityScoreIsOnlyEnabledForEverPublishedTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            String templateName = "PredictabilityScoreTest";
            HashMap<String, String> legionMLDriver = new HashMap<String, String>(){
                {
                    put("Name", "LegionMLDriver");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();

            //Check Predictability Score is disabled for draft version template
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(legionMLDriver);
            configurationPage.saveADraftTemplate();
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Edit");
            if( configurationPage.isGetPredictabilityScoreEnabled() == false){
                SimpleUtils.pass("Get Predictability Score is Disabled as expected for the draft version template!");
            }else{
                SimpleUtils.fail("Get Predictability Score should not be Enabled for the draft version template!", false);
            }

            //Check Predictability Score is enabled for published version template
            configurationPage.clickOnBackButton();
            configurationPage.createDynamicGroup(templateName, "Custom", templateName + " test");
            configurationPage.selectOneDynamicGroup(templateName);
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.publishNowTemplate();
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Edit");
            if(configurationPage.isGetPredictabilityScoreEnabled() == true){
                SimpleUtils.pass("Get Predictability Score is Enabled as expected for the Published version template!");
            }else{
                SimpleUtils.fail("Get Predictability Score should not be Disabled for the Published version template!", false);
            }

            //Will uncomment below code when we can get forecast data back
//            configurationPage.clickGetPredictabilityScore();

            //Delete the association and archive the template
            configurationPage.clickOnBackButton();
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            configurationPage.deleteOneDynamicGroup(templateName);
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.setLeaveThisPageButton();
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify empty channel name is allowed")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEmptyChannelNameIsAllowedAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            SimpleDateFormat sdf = new SimpleDateFormat("MMddhh");
            String currentTime = sdf.format(new Date()).trim();
            String templateName = "TestEmptyChannelName" + currentTime;
            String channelDisplayName = "ChannelTest";
            String channelDisplayName1 = "ChannelTest1";
            String description = "This is a test for empty channel name!";
            String verifyType = "channel";
            String channelName = "";
            String channelDisplayNameToUpdate = "ChannelToUpdate";
            String expectedWarningMsg = "The channel source type is already exist.";
            String addOrEdit = "Add";

            HashMap<String, String> driver = new HashMap<String, String>()
            {
                {
                    put("Name", "Items:ChannelTest:Enrollments");
                    put("Type", "Items");
                    put("Channel", channelDisplayNameToUpdate);
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            //Turn on UseDemandDriverTemplateSwitch toggle
//            ToggleAPI.updateToggle(Toggles.UseDemandDriverTemplateSwitch.getValue(), "jane.meng+006@legion.co", "P@ssword123", true);
//            refreshPage();
            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            //Add new channel with empty channel name in settings.
            settingsAndAssociationPage.createNewChannelOrCategory(verifyType, channelDisplayName, description, channelName);
            //Update a channel to empty channel name
            settingsAndAssociationPage.clickOnEditBtnInSettings(verifyType, channelDisplayName, channelDisplayNameToUpdate);
            //Only one empty channel name is allowed
            SimpleUtils.assertOnFail("There should be a warning message shows empty channel name already exist!", settingsAndAssociationPage.createNewChannelOrCategory(verifyType, channelDisplayName1, description, channelName).get(0).contains(expectedWarningMsg), false);
            //The channel with empty name will display in the used driver
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver(addOrEdit);
            configurationPage.addOrEditDemandDriverInTemplate(driver);
            SimpleUtils.assertOnFail("Failed to add the driver!", configurationPage.searchDriverInTemplateDetailsPage(driver.get("Name")), false);
            configurationPage.clickOnBackButton();
            configurationPage.setLeaveThisPageButton();

            //Remove the channel
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            settingsAndAssociationPage.clickOnRemoveBtnInSettings(verifyType, channelDisplayNameToUpdate);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Granularity to Input Stream ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGranularityToInputStreamAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Demand Drivers";
            Random random = new Random();
            String templateName = "TestGranularity" + random.nextInt(1000);
            String baseInputStreamName = "TestBase" + random.nextInt(1000);
            String baseInputStreamName1 = "TestBase" + random.nextInt(1000);
            String aggregatedInputStreamName = "TestAggregated" + random.nextInt(1000);
            List<String> granularityOptions = new ArrayList<>(Arrays.asList("Slot (15 min)", "Slot (30 min)", "Slot (60 min)", "Day", "Week"));
            String expectedWarningMsg = "This base input stream is used in aggregated input stream";

            HashMap<String, String> baseInputStream = new HashMap<String, String>(){
                {
                    put("Name", baseInputStreamName);
                    put("Type", "Base");
                    put("Tag", baseInputStreamName);
                }
            };
            HashMap<String, String> baseInputStream1 = new HashMap<String, String>(){
                {
                    put("Name", baseInputStreamName1);
                    put("Type", "Base");
                    put("Tag", baseInputStreamName1);
                }
            };
            HashMap<String, String> aggregatedInputStream = new HashMap<String, String>(){
                {
                    put("Name", aggregatedInputStreamName);
                    put("Type", "Aggregated");
                    put("Operator", "NOT IN");
                    put("Streams", baseInputStream1.get("Name"));
                }
            };
            HashMap<String, String> driver = new HashMap<String, String>()
            {
                {
                    put("Name", "DriverToTestGranularity");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Enrollments");
                    put("Show in App", "Yes");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Input Stream", "Items:EDW:Enrollments");
                }
            };
            HashMap<String, String> driver1 = new HashMap<String, String>()
            {
                {
                    put("Name", "DriverToTestGranularity_Week");
                    put("Type", "Items");
                    put("Channel", "EDW");
                    put("Category", "Verifications");
                    put("Show in App", "No");
                    put("Order", "1");
                    put("Forecast Source", "Legion ML");
                    put("Granularity", "Week");
                    put("Input Stream", baseInputStreamName);
                }
            };

            //Go to Demand Driver template
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Go to Settings tab
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");

            //Check Granularity for a new base input stream.
            settingsAndAssociationPage.createInputStream(baseInputStream);
            settingsAndAssociationPage.createInputStream(baseInputStream1);
            settingsAndAssociationPage.clickEditBtn(baseInputStream.get("Name"));
            String baseValue = settingsAndAssociationPage.getGranularityForCertainInputStream();
            System.out.println("value: " + baseValue);
            SimpleUtils.assertOnFail("The default granularity for base is not correct!", baseValue.equalsIgnoreCase(granularityOptions.get(1)), false);

            //Check Granularity for a new aggregated input stream.
            settingsAndAssociationPage.createInputStream(aggregatedInputStream);
            settingsAndAssociationPage.clickEditBtn(aggregatedInputStream.get("Name"));
            String aggregatedValue = settingsAndAssociationPage.getGranularityForCertainInputStream();
            System.out.println("value: " + aggregatedValue);
            SimpleUtils.assertOnFail("The default granularity for aggregated is not correct!", aggregatedValue.equalsIgnoreCase(granularityOptions.get(1)), false);

            //Update the granularity to other options
            for (String option : granularityOptions){
                if (!option.equals(baseValue)){
                    settingsAndAssociationPage.clickEditBtn(baseInputStream.get("Name"));
                    settingsAndAssociationPage.updateGranularityForCertainInputStream(option);
                }
            }

            //Update granularity for base stream which is used in aggregated is not allowed
            settingsAndAssociationPage.clickEditBtn(baseInputStream1.get("Name"));
            settingsAndAssociationPage.updateGranularityForCertainInputStream(granularityOptions.get(0));
            String warningMsgToVerify = expectedWarningMsg + " [" + aggregatedInputStream.get("Name") + "]";
            settingsAndAssociationPage.validateWarningMessage(warningMsgToVerify);

            //Remove base stream which is used in aggregated is not allowed
            settingsAndAssociationPage.removeInputStream(baseInputStream1.get("Name"));
            settingsAndAssociationPage.validateWarningMessage(warningMsgToVerify);

            //Verify default granularity for newly created demand driver
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.createNewTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(driver);
            configurationPage.searchDriverInTemplateDetailsPage(driver.get("Name"));
            configurationPage.clickAddOrEditForDriver("Edit");
            String granularityValue = configurationPage.getGranularityForCertainDriver();
            System.out.println("granularityValue: " + granularityValue);
            SimpleUtils.assertOnFail("The default granularity for driver is not correct!", granularityValue.equalsIgnoreCase(granularityOptions.get(1)), false);
            configurationPage.clickAddOrEditForDriver("Add");
            configurationPage.addOrEditDemandDriverInTemplate(driver1);
            settingsAndAssociationPage.goToAssociationTabOnTemplateDetailsPage();
            //Add association and save
            configurationPage.createDynamicGroup(templateName, "Custom", "Auto test" + templateName);
            configurationPage.selectOneDynamicGroup(templateName);
            //Could publish normally
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();
            settingsAndAssociationPage.goToTemplateListOrSettings("Settings");
            settingsAndAssociationPage.removeInputStream(baseInputStreamName);
            String inputStreamUsedInDriverWarning = "This input stream is used in the template " + "[" + templateName + "]";
            settingsAndAssociationPage.validateWarningMessage(inputStreamUsedInDriverWarning);
            settingsAndAssociationPage.goToTemplateListOrSettings("Template");
            configurationPage.archiveOrDeleteTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

}