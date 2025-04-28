package com.legion.tests.core.OpsPortal;

import com.alibaba.fastjson.JSONObject;
import com.legion.api.login.LoginAPI;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.core.ConsoleLocationSelectorPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.HttpUtil;
import com.legion.utils.SimpleUtils;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.legion.utils.MyThreadLocal.getDriver;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class BasicConfigurationTest extends TestBase {

    public enum modelSwitchOperation {
        Console("Console"),
        OperationPortal("Control Cent");

        private final String value;

        modelSwitchOperation(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    @BeforeMethod(alwaysRun = true)
    public void firstTest(Method testMethod, Object[] params) throws Exception {


        this.createDriver((String) params[0], "83", "Window");
//        ToggleAPI.updateToggle(Toggles.DynamicGroupV2.getValue(), "jane.meng+006@legion.co", "P@ssword123", false);
//        ToggleAPI.updateToggle(Toggles.EnableDemandDriverTemplate.getValue(), "jane.meng+006@legion.co", "P@ssword123", true);
//        ToggleAPI.updateToggle(Toggles.MixedModeDemandDriverSwitch.getValue(), "jane.meng+006@legion.co", "P@ssword123", true);
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.verifyNewTermsOfServicePopUp();
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Operating Hours")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOperatiingHoursAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String OHtemplate = "Operating Hours";
            String mode = "edit";
            String templateName = "OHTemplateCreationCheck";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OHtemplate);
            //check the operating hours list
            configurationPage.OHListPageCheck();
            //create a OH template and check create page
            configurationPage.createOHTemplateUICheck(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Add country field to holidays")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddCountryFieldToHolidayAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String OHtemplate = "Operating Hours";
            //scheduling rules is not included as some exception, will added later
            String mode = "edit";
            String templateName = "LizzyUsingDynamicCheckNoDelete";
            String customerHolidayName = "LZautoTestHoliday";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OHtemplate);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //check the Holidays pops up
            configurationPage.holidaysDataCheckAndSelect(customerHolidayName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify open each type template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUserCanOpenEachTypeTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            List<String> templateTypeList = new ArrayList<String>() {{
                add("Operating Hours");
                add("Scheduling Policies");
                add("Schedule Collaboration");
                add("Time & Attendance");
                add("Compliance");
                add("Scheduling Rules");
//                add("Communications");
            }};

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            for (String templateType : templateTypeList) {
                configurationPage.goToConfigurationPage();
                configurationPage.goToTemplateDetailsPage(templateType);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //requirement from ops-3151
    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void ceateMultipleHistortForOHTempInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String mode = "edit";
            //OH templaye  3153 on RC OPauto
            String templateName = "3153";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            int now = configurationPage.historyRecordLimitCheck(templateName);
            configurationPage.closeTemplateHistoryPanel();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            if (now < 101) {
                SimpleUtils.pass("History records displayed successfully!");
                //create another 100 records
                for (int i = 0; i < 101 - now; i++) {
                    configurationPage.searchTemplate(templateName);
                    configurationPage.clickOnTemplateName(templateName);
                    configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                    configurationPage.changeOHtemp();
                }
            } else {
                SimpleUtils.fail("History records excedded 100!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Create all type template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateAllTypeTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {

            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String templateName = "AutoCreate" + currentTime;

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.addAllTypeOfTemplate(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Multiple version regression")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMultipleVersionRegressionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Scheduling Policies";
            String templateName = "FionaMultipleTemp";
            String dynamicGpName = "ForMultipleAutoRegression";
            String button = "publish at different time";
            int date = 14;
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            //create Operating Hour template and published it
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.publishNewTemplate(templateName, dynamicGpName, "Custom", "AutoCreatedDynamic---Format Script");
            configurationPage.archivePublishedOrDeleteDraftTemplate(templateName, "Archive");
            //create Operating Hour template and save as draft
            configurationPage.createNewTemplate(templateName);
            configurationPage.archivePublishedOrDeleteDraftTemplate(templateName, "Delete");
            //create Operating Hour template and publish at different time
            configurationPage.publishAtDifferentTimeTemplate(templateName, dynamicGpName, "Custom", "AutoCreatedDynamic---Format Script", button, date);
            configurationPage.archivePublishedOrDeleteDraftTemplate(templateName, "Archive");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Create future published version template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMultipleVersionTemplateCreationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
//            String templateType = "Operating Hours";
            String templateType ="Scheduling Policies";
            String templateName = "MultipleTemplate" + currentTime;
            String dynamicGpName = "MultipleTemplate" + currentTime;
            String formula = "AutoCreatedDynamic---Format Script" + currentTime;
            String button1 = "publish at different time";
            String button2 = "save as draft";
            int date = 14;
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            //create Operating Hour template and published it
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Create one current published version template
            configurationPage.publishNewTemplate(templateName, dynamicGpName, "Custom", formula);
            //Create future publish version based on current published version
            configurationPage.createFutureTemplateBasedOnExistingTemplate(templateName, button1, date, "edit");
            //Verify user can create draft template for each published version template
            configurationPage.createDraftForEachPublishInMultipleTemplate(templateName, button2, "edit");


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify menu list for multiple version template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMenuListForMultipleVersionTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String templateName = "MultipleTemplateMenuListCheckingAutoUsing";
            List<String> current = new ArrayList<>(Arrays.asList("Save as draft", "Publish now", "Publish at different time"));
            List<String> future = new ArrayList<>(Arrays.asList("Save as draft", "replacing", "Publish at different time"));
            List<String> current1 = new ArrayList<String>();
            List<String> future1 = new ArrayList<String>();
            HashMap<String, List<String>> allMenuInfo = new HashMap<>();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();

            //get menu list for current template and future template
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            allMenuInfo = configurationPage.verifyMenuListForMultipleTemplate(templateName);
            current1 = allMenuInfo.get("current");
            future1 = allMenuInfo.get("future");
            //Verify menu list of current template
            for (String menuName : current1) {
                for (String menu : current) {
                    if (menuName.contains(menu)) {
                        SimpleUtils.pass(menuName + " is showing for current template");
                        break;
                    }
                }
            }
            //Verify menu list of future template
            for (String menuName : future1) {
                for (String menu : future) {
                    if (menuName.contains(menu)) {
                        SimpleUtils.pass(menuName + " is showing for future template");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify buttons showing for multiple template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyButtonsShowingForMultipleTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String templateName = "MultipleTemplateMenuListCheckingAutoUsing";
            //Go to OH template list page
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Expand multiple version template
            configurationPage.expandMultipleVersionTemplate(templateName);
            //Verify buttons on published template details page
            configurationPage.verifyButtonsShowingOnPublishedTemplateDetailsPage();
            //Expand multiple version template
            configurationPage.expandMultipleVersionTemplate(templateName);
            //Verify buttons on draft template details page
            configurationPage.verifyButtonsShowingOnDraftTemplateDetailsPage();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "User can create/archive multiple template for all template type")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUserCanCreateMultipleTemplateForAllTemplateTypeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String templateName = "MultipleTemplate" + currentTime;
            String dynamicGpName = "MultipleTemplate" + currentTime;
            String button = "publish at different time";
            String criteriaType = "Custom";
            String criteriaValue = "AutoCreatedDynamic---Format Script" + currentTime;
            int date = 14;
            String editOrViewMode = "Edit";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.createMultipleTemplateForAllTypeOfTemplate(templateName, dynamicGpName, criteriaType, criteriaValue, button, date, editOrViewMode);
//            configurationPage.archiveMultipleTemplate(templateName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Multiple Template E2E flow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMultipleTemplateE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String templateName = "MultipleTemplateE2EUsing";
            String button = "publish at different time";
            int date = 14;
            String locationName = "MultipleTemplateE2EUsing";
            HashMap<String, String> activeDayAndOperatingHrs = new HashMap<>();
            String currentOperatingHour = "7AM-12AM";
            String futureOperatingHour = "7:30 AM-12AM";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //get the future template's effective date, such as Jun 15, 2022
            String[] effectiveDate = configurationPage.updateEffectiveDateOfFutureTemplate(templateName, button, date).split(",");
            Collections.reverse(Arrays.asList(effectiveDate));
            //2022 Jun 15
            String effectiveDate1 = String.join(" ", effectiveDate).trim();
            String day = effectiveDate1.split(" ")[2];

            // go to console side to check the result
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
            //go to schedule function
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to the specified week
            scheduleCommonPage.goToSpecificWeekByDateNew(effectiveDate1);
            scheduleCommonPage.clickOnFirstWeekInWeekPicker();

            // Ungenerate and create the schedule
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            ToggleSummaryPage consoleToggleSummaryPage = pageFactory.createToggleSummaryPage();
            //Check future template can show well or not
            if (!scheduleCommonPage.isSpecifyDayEqualWithFirstDayOfActivateWeek(day)) {
                //go to next week
                scheduleCommonPage.navigateToNextWeek();
            }
            activeDayAndOperatingHrs = consoleToggleSummaryPage.getOperatingHrsValue("Sun");
            String futureOH = activeDayAndOperatingHrs.get("ScheduleOperatingHrs");
            if (futureOH.contains(futureOperatingHour)) {
                SimpleUtils.pass("Future template can work well");
            } else {
                SimpleUtils.fail("Future template can NOT work well", false);
            }
            //Check current template can show well or not
            scheduleCommonPage.navigateToPreviousWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            activeDayAndOperatingHrs = consoleToggleSummaryPage.getOperatingHrsValue("Sun");
            String currentOH = activeDayAndOperatingHrs.get("ScheduleOperatingHrs");
            if (currentOH.contains(currentOperatingHour)) {
                SimpleUtils.pass("Current template can work well");
            } else {
                SimpleUtils.fail("Current template can NOT work well", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Location Level override")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationLevelOverriddenAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String templateName = "MultipleTemplateE2EUsing";
            String locationName = "MultipleTemplateLocationLevelChecking";
            HashMap<String, String> activeDayAndOperatingHrs = new HashMap<>();
            String overriddenOperatingHour = "8AM-12AM";
            List<String> effectiveDatesOfMultipleTemplates = new ArrayList<String>();

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //get the future template's effective date, such as Jun 15, 2022
            effectiveDatesOfMultipleTemplates = configurationPage.getEffectiveDateForTemplate(templateName);
            String[] effectiveDate = effectiveDatesOfMultipleTemplates.get(effectiveDatesOfMultipleTemplates.size() - 1).split(",");
            Collections.reverse(Arrays.asList(effectiveDate));
            //2022 Jun 15
            String effectiveDate1 = String.join(" ", effectiveDate).trim();
            String day = effectiveDate1.split(" ")[2];

            // go to console side to check the result
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
            //go to schedule function
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to the specified week
            scheduleCommonPage.goToSpecificWeekByDateNew(effectiveDate1);
            scheduleCommonPage.clickOnFirstWeekInWeekPicker();

            // Ungenerate and create the schedule
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            ToggleSummaryPage consoleToggleSummaryPage = pageFactory.createToggleSummaryPage();
            //Check future template can show well or not
            if (!scheduleCommonPage.isSpecifyDayEqualWithFirstDayOfActivateWeek(day)) {
                //go to next week
                scheduleCommonPage.navigateToNextWeek();
            }
            activeDayAndOperatingHrs = consoleToggleSummaryPage.getOperatingHrsValue("Sun");
            String futureOH = activeDayAndOperatingHrs.get("ScheduleOperatingHrs");
            if (futureOH.contains(overriddenOperatingHour)) {
                SimpleUtils.pass("Future week's OH is showing with overridden value.");
            } else {
                SimpleUtils.fail("Future week's OH isn't showing with overridden value.", false);
            }
            //Check current template can show well or not
            scheduleCommonPage.navigateToPreviousWeek();
            activeDayAndOperatingHrs = consoleToggleSummaryPage.getOperatingHrsValue("Sun");
            String currentOH = activeDayAndOperatingHrs.get("ScheduleOperatingHrs");
            if (currentOH.contains(overriddenOperatingHour)) {
                SimpleUtils.pass("Current week's OH is showing with overridden value.");
            } else {
                SimpleUtils.fail("Current week's OH isn't showing with overridden value.", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Audit Log History Button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void auditLogVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();

            configurationPage.goToConfigurationPage();

            configurationPage.goToItemInConfiguration("Time & Attendance");
            configurationPage.searchTemplate("AuditLog");
            configurationPage.clickOnTemplateName("AuditLog");
            configurationPage.verifyHistoryButtonDisplay();
            configurationPage.verifyHistoryButtonIsClickable();
            configurationPage.verifyCloseIconNotDisplayDefault();
            configurationPage.clickHistoryAndClose();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

//            configurationPage.goToItemInConfiguration("Demand Drivers");
//            configurationPage.searchTemplate("AuditLog");
//            configurationPage.clickOnTemplateName("AuditLog");
//            configurationPage.verifyHistoryButtonDisplay();
//            configurationPage.verifyHistoryButtonIsClickable();
//            configurationPage.verifyCloseIconNotDisplayDefault();
//            configurationPage.clickHistoryAndClose();
//            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
//            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

            configurationPage.goToItemInConfiguration("Operating Hours");
            configurationPage.searchTemplate("AuditLog");
            configurationPage.clickOnTemplateName("AuditLog");
            configurationPage.verifyHistoryButtonDisplay();
            configurationPage.verifyHistoryButtonIsClickable();
            configurationPage.verifyCloseIconNotDisplayDefault();
            configurationPage.clickHistoryAndClose();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

            configurationPage.goToItemInConfiguration("Scheduling Policies");
            configurationPage.searchTemplate("AuditLog");
            configurationPage.clickOnTemplateName("AuditLog");
            configurationPage.verifyHistoryButtonDisplay();
            configurationPage.verifyHistoryButtonIsClickable();
            configurationPage.verifyCloseIconNotDisplayDefault();
            configurationPage.clickHistoryAndClose();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

            configurationPage.goToItemInConfiguration("Schedule Collaboration");
            configurationPage.searchTemplate("AuditLog");
            configurationPage.clickOnTemplateName("AuditLog");
            configurationPage.verifyHistoryButtonDisplay();
            configurationPage.verifyHistoryButtonIsClickable();
            configurationPage.verifyCloseIconNotDisplayDefault();
            configurationPage.clickHistoryAndClose();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

            configurationPage.goToItemInConfiguration("Compliance");
            configurationPage.searchTemplate("AuditLog");
            configurationPage.clickOnTemplateName("AuditLog");
            configurationPage.verifyHistoryButtonDisplay();
            configurationPage.verifyHistoryButtonIsClickable();
            configurationPage.verifyCloseIconNotDisplayDefault();
            configurationPage.clickHistoryAndClose();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

            configurationPage.goToItemInConfiguration("Scheduling Rules");
            configurationPage.searchTemplate("AuditLog");
            configurationPage.clickOnTemplateName("AuditLog");
            configurationPage.verifyHistoryButtonDisplay();
            configurationPage.verifyHistoryButtonIsClickable();
            configurationPage.verifyCloseIconNotDisplayDefault();
            configurationPage.clickHistoryAndClose();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

//            configurationPage.goToItemInConfiguration("Minors Rules");
//            configurationPage.searchTemplate("AuditLog");
//            configurationPage.clickOnTemplateName("AuditLog");
//            configurationPage.verifyHistoryButtonDisplay();
//            configurationPage.verifyHistoryButtonIsClickable();
//            configurationPage.verifyCloseIconNotDisplayDefault();
//            configurationPage.clickHistoryAndClose();
//            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
//            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

//            configurationPage.goToItemInConfiguration("Additional Pay Rules");
//            configurationPage.searchTemplate("AuditLog");
//            configurationPage.clickOnTemplateName("AuditLog");
//            configurationPage.verifyHistoryButtonDisplay();
//            configurationPage.verifyHistoryButtonIsClickable();
//            configurationPage.verifyCloseIconNotDisplayDefault();
//            configurationPage.clickHistoryAndClose();
//            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
//            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();

            configurationPage.verifyHistoryButtonNotDisplay();
            locationsPage.goBack();

            locationsPage.goToUpperFieldsPage();
            configurationPage.verifyHistoryButtonNotDisplay();
            locationsPage.goBack();

            locationsPage.getEnterpriseLogoAndDefaultLocationInfo();
            configurationPage.verifyHistoryButtonNotDisplay();
            locationsPage.goBack();

            locationsPage.goToGlobalConfigurationInLocations();
            configurationPage.verifyHistoryButtonNotDisplay();
            locationsPage.goBack();

            locationsPage.goToDynamicGroup();
            configurationPage.verifyHistoryButtonNotDisplay();
            locationsPage.goBack();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Audit Log UI Checking")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void auditLogUIVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String mode = "view";
            String templateName = "AuditLogAutoUsing";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickHistoryButton();
            configurationPage.verifyTemplateHistoryUI();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Audit Log Button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void auditLogHistoryButtonVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String mode = "view";
            String templateName = "AuditLogAutoUsing";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickHistoryButton();
            configurationPage.verifyRecordIsClickable();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Template History Content Check")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void auditLogContentVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Operating Hours";
            String mode = "view";
            String templateName = "AuditLogAutoUsing";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickHistoryButton();
            configurationPage.verifyTemplateHistoryContent();
            configurationPage.verifyOrderOfTheTemplateHistory();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Audit Log E2E")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void auditLogE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            SimpleDateFormat dfs1 = new SimpleDateFormat("hh:mm:ss a','MM/dd/yyyy");
            String templateName = "AuditLog" + currentTime;
            String dynamicGpName = "AuditLog" + currentTime;
            String criteriaType = "Custom";


            String criteriaValue = "AutoCreatedDynamic---Format Script" + currentTime;
            String viewMode = "view";
            String editMode = "edit";
            String templateType = "Scheduling Policies";
            String createOption = "Created";
            String publishOption = "Published";
            String publishFutureOption = "set to publish on";
            String archiveOption = "Archived";
            String deleteOption = "Deleted";
            String button = "publish at different time";
            int date = 14;
            Random random1 = new Random();
            int number1 = random1.nextInt(90) + 10;
            String count1 = String.valueOf(number1);
            Random random2 = new Random();
            int number2 = random2.nextInt(90) + 10;
            String count2 = String.valueOf(number2);

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String userFullName = profileNewUIPage.getUserProfileName().get("fullName");

            //check create new template and save as draft template history
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.createNewTemplate(templateName);
            String time1 = dfs1.format(new Date()).trim();
            configurationPage.clickOnSpecifyTemplateName(templateName, viewMode);
            configurationPage.clickHistoryButton();
            configurationPage.verifyNewCreatedTemplateHistoryContent(createOption, userFullName, time1);
            configurationPage.verifyRecordIsClickable();
            configurationPage.verifyUpdatedSchedulePolicyTemplateFirstFieldCorrectOrNot("35");

            //check publish template history can show correct and verify data in template via template history
            configurationPage.closeTemplateHistoryPanel();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnSpecifyTemplateName(templateName, editMode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.updateSchedulePolicyTemplateFirstField(count1);
            configurationPage.createDynamicGroup(dynamicGpName, criteriaType, criteriaValue);
            configurationPage.selectOneDynamicGroup(dynamicGpName);
            configurationPage.clickOnTemplateDetailTab();
            configurationPage.publishNowTemplate();
            configurationPage.clickOnSpecifyTemplateName(templateName, viewMode);
            configurationPage.clickHistoryButton();
            configurationPage.verifyPublishTemplateHistoryContent(publishOption, userFullName);
            configurationPage.verifyRecordIsClickable();
            configurationPage.verifyUpdatedSchedulePolicyTemplateFirstFieldCorrectOrNot(count1);

            //Check publish future template history and data showing in each version
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnSpecifyTemplateName(templateName, editMode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.updateSchedulePolicyTemplateFirstField(count2);
            configurationPage.publishAtDifferentTimeForTemplate(button, date);
            configurationPage.openCurrentPublishInMultipleTemplate(templateName);
            configurationPage.clickHistoryButton();
            configurationPage.verifyPublishFutureTemplateHistoryContent(publishFutureOption, userFullName);
            configurationPage.verifyRecordIsClickable();
            configurationPage.verifyUpdatedSchedulePolicyTemplateFirstFieldCorrectOrNot(count2);

            //check archive template history
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.openCurrentPublishInMultipleTemplate(templateName);
            configurationPage.clickOnArchiveButton();
            configurationPage.clickOnSpecifyTemplateName(templateName, editMode);
            configurationPage.clickHistoryButton();
            configurationPage.verifyArchiveTemplateHistoryContent(archiveOption, userFullName);

            //Check delete template history
            configurationPage.closeTemplateHistoryPanel();
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
            configurationPage.clickOnSpecifyTemplateName(templateName, editMode);
            configurationPage.clickOnDeleteButtonOnTemplateDetailsPage();
            configurationPage.clickOnSpecifyTemplateName(templateName, editMode);
            configurationPage.clickHistoryButton();
            configurationPage.verifyDeleteTemplateHistoryContent(deleteOption, userFullName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Each Template type have audit log")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAuditLogForEachTemplateTypeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            //check create new template and save as draft template history
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.verifyAllTemplateTypeHasAuditLog();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Location Level Audit Log Checking")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void auditLogAtLocationLevelTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "AuditLogAutoLocation";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Assignment Rules", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Policies", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Compliance", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Schedule Collaboration", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Time and Attendance", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Demand Drivers", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Rules", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Labor Model", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Operating Hours", "View");
            configurationPage.verifyLocationLevelTemplateNoHistoryButton();
            locationsPage.backToConfigurationTabInLocationLevel();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate the layout of the new template page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void ValidateLayoutOfNewTemplatePageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            List<String> templateTypes = new ArrayList<String>() {{
                add("Operating Hours");
                add("Scheduling Policies");
                add("Scheduling Rules");
            }};
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            for (String templateType : templateTypes) {
                String templateName = templateType + currentTime;
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad(templateType);
                configurationPage.createNewTemplate(templateName);
                configurationPage.searchTemplate(templateName);
                configurationPage.clickOnTemplateName(templateName);
                configurationPage.verifyTheLayoutOfTemplateDetailsPage();
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                configurationPage.verifyTheLayoutOfTemplateAssociationPage();
                configurationPage.verifyCriteriaTypeOfDynamicGroup();
                configurationPage.clickOnBackBtnOnTheTemplateDetailAndListPage();
                configurationPage.searchTemplate(templateName);
                configurationPage.archivePublishedOrDeleteDraftTemplate(templateName, "Delete");
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify default value of override via integration button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void ValidateDefaultValueOfOverrideViaIntegrationButtonAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {

            String templateType = "Operating Hours";
            String templateName = "FionaUsingUpdateLocationLevelOH";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.searchTemplate(templateName);
            configurationPage.clickOnTemplateName(templateName);
            configurationPage.verifyDefaultValueOfOverrideViaIntegrationButton();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Update Reset location level OH when the button is off")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenOperatingHoursInLocationLevelAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "updateOHViaIntegration";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            String openString = "09:00AM";
            String closeString = "08:30PM";

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            if (locationsPage.verifyIsOverrideStatusAtLocationLevel("Operating Hours")) {
                locationsPage.clickActionsForTemplate("Operating Hours", "Reset");
            }
            //user can view location level OH template
            locationsPage.clickActionsForTemplate("Operating Hours", "View");
            locationsPage.backToConfigurationTabInLocationLevel();
            //user can edit location level OH template
            locationsPage.clickActionsForTemplate("Operating Hours", "Edit");
            locationsPage.editBtnIsClickableInBusinessHours();
            locationsPage.updateOpenCloseHourForOHTemplate(openString, closeString);
            locationsPage.selectDayInWorkingHoursPopUpWin(6);
            configurationPage.saveBtnIsClickable();
            configurationPage.saveBtnIsClickable();
            if (locationsPage.verifyIsOverrideStatusAtLocationLevel("Operating Hours")) {
                SimpleUtils.pass("User can update location level template successfully");
            } else {
                SimpleUtils.fail("User failed to update location level template", false);
            }

            //reset
            locationsPage.clickActionsForTemplate("Operating Hours", "Reset");
            if (!locationsPage.verifyIsOverrideStatusAtLocationLevel("Operating Hours")) {
                SimpleUtils.pass("User can reset successfully");
            } else {
                SimpleUtils.fail("User failed to reset location level template", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "User can only view location level OH when the button is on")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUserOnlyCanViewOperatingHoursInLocationLevelAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "updateOHViaInteTest";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();

            //Verify user can only see view button in location level
            List<String> actions = new ArrayList<>();
            actions = locationsPage.actionsForTemplateInLocationLevel("Operating Hours");
            if (actions.size() == 1 && actions.get(0).equalsIgnoreCase("View")) {
                SimpleUtils.pass("User can only view location level Operating Hours when Override via integration is on");
            } else {
                SimpleUtils.fail("User can do other actions and not only view for location level OH when Override via integration is on", false);
            }
            //user can view location level OH template
            locationsPage.clickActionsForTemplate("Operating Hours", "View");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    public void importBusinessHours(String sessionId) {
        String filePath = "src/test/resources/uploadFile/LocationLevelWithoutDayparts.csv";
        String url = "https://staging-enterprise.dev.legion.work/legion/integration/testUploadBusinessHoursData?isTest=false&fileName=" + filePath + "&encrypted=false";
        String responseInfo = HttpUtil.fileUploadByHttpPost(url, sessionId, filePath);
        if (StringUtils.isNotBlank(responseInfo)) {
            JSONObject json = JSONObject.parseObject(responseInfo);
            if (!json.isEmpty()) {
                String value = json.getString("responseStatus");
                System.out.println(value);
            }
        }
    }

    //blocked by API error
    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user can update location level operating hours via integration")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyUserCanUpdateLocationOHViaIntegrationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            String locationName = "updateOHViaInteTest";
            String templateName = "updateOHViaInteTest";
            String sessionId = LoginAPI.getSessionIdFromLoginAPI("fiona+99@legion.co", "admin11.a");
            importBusinessHours(sessionId);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            //verify location level OH is overridden or not?
            if (locationsPage.isOverrideStatusAtLocationLevel("Operating Hours")) {
                SimpleUtils.pass("User can update location level OH via integration");
            } else {
                SimpleUtils.fail("User can NOT update location level OH via integration", false);
            }
            //Go to configuration and edit this template's override button to false then reset location level OH
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.turnOnOffOverrideViaIntegrationButton();
            configurationPage.publishNowTemplate();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Operating Hours", "Reset");
            //back to template details page to turn on the button
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.clickOnSpecifyTemplateName(templateName, "edit");
            configurationPage.turnOnOffOverrideViaIntegrationButton();
            configurationPage.publishNowTemplate();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Create Each Template with Dynamic Group Association ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateEachTemplateWithDynamicGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String templateNameVerify = "LizzyUsingToCreateTempTest" + currentTime;
            String[] tempType = {"Operating Hours", "Schedule Collaboration"};
            String dynamicGpNameTempTest = "LZautoTestDyGpName";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            //create other types of templates
            for (String type : tempType) {
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad(type);
                configurationPage.createTmpAndPublishAndArchive(type, templateNameVerify, dynamicGpNameTempTest);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Work Roles hourly rate regression test")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class,enabled = false)
    public void workRolesHourlyRateRegressionTestAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            String hourlyRate = "15";
            String locationName = "AutoADVDynamicGroup02";
            String workRoleName = "AMBASSADOR";
            //Turn off WorkRoleSettingsTemplateOP toggle
            ToggleAPI.updateToggle(Toggles.WorkRoleSettingsTemplateOP.getValue(), "fiona+99@legion.co", "admin11.a", false);

            String winHandleBefore = getDriver().getWindowHandle();
            Set<String> winHandles = getDriver().getWindowHandles();
            getDriver().close();

            Iterator<String> it = winHandles.iterator();
            while (it.hasNext()) {
                String win = it.next();
                if (!win.equals(winHandleBefore)) {
                    getDriver().switchTo().window(win);
                    break;
                }
            }

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            //Login as admin
            loginAsDifferentRole("InternalAdmin");

            //Verify user can update hourly rate in work role details page
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            LocationsPage locationsPage=pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());

            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();
            userManagementPage.updateWorkRoleHourlyRate(hourlyRate);
            //Verify location level hourly rate is read only
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Assignment Rules", "Edit");
            userManagementPage.verifySearchWorkRole(workRoleName);
            userManagementPage.goToWorkRolesDetails(workRoleName);
            //userManagementPage.verifyLocationLevelHourlyRateIsReadOnly();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Work role hourly rate is NOT showing on work role details page when toggle is on")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void workRolesHourlyRateIsNOTShowingWhenToggleIsOnAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            String locationName = "AutoADVDynamicGroup02";
            String workRoleName = "AMBASSADOR";
            //Turn on WorkRoleSettingsTemplateOP toggle
//            ToggleAPI.updateToggle(Toggles.WorkRoleSettingsTemplateOP.getValue(), "fiona+99@legion.co", "admin11.a", true);

            //Verify user can update hourly rate in work role details page
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
//            userManagementPage.clickOnUserManagementTab();
//            userManagementPage.goToUserAndRoles();
//            userManagementPage.verifyBackBtnIsClickable();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//            locationsPage.clickOnLocationsTab();

            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();
            userManagementPage.goToWorkRolesDetails(workRoleName);
            userManagementPage.hourlyRateFieldIsNotShowing();

            //Verify location level hourly rate is read only
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Assignment Rules", "Edit");
            userManagementPage.verifySearchWorkRole(workRoleName);
            userManagementPage.goToWorkRolesDetails(workRoleName);
            userManagementPage.hourlyRateFieldIsNotShowing();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Work role template common checking")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void workRolesTemplateCommonCheckingAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            String templateName = "Default";
            String mode = "edit";
            //Turn on WorkRoleSettingsTemplateOP toggle
//            ToggleAPI.updateToggle(Toggles.WorkRoleSettingsTemplateOP.getValue(), "fiona+99@legion.co", "admin11.a", true);

//            String winHandleBefore = getDriver().getWindowHandle();
//            Set<String> winHandles = getDriver().getWindowHandles();
//            getDriver().close();
//
//            Iterator<String> it = winHandles.iterator();
//            while (it.hasNext()) {
//                String win = it.next();
//                if (!win.equals(winHandleBefore)) {
//                    getDriver().switchTo().window(win);
//                    break;
//                }
//            }

//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//            //Login as admin
//            loginAsDifferentRole("InternalAdmin");
//            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());

            //go to configuration tab
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.goToWorkRoleSettingsTile();
            configurationPage.verifyWorkRoleSettingsTemplateListUIAndDetailsUI(templateName, mode);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Create work role setting template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void createWorkRolesSettingTemplateAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            String templateName = "Default";
            String mode = "Edit";
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String templateName1 = "AutoCreate" + currentTime;
            String dynamicGpName = "AutoCreateDynamicGp" + currentTime;
            HashMap<String, String> workRoleHourlyRate = new HashMap<String, String>();
            workRoleHourlyRate.put("WRSAuto1", "12");
            workRoleHourlyRate.put("WRSAuto2", "15");
            workRoleHourlyRate.put("WRSAuto3", "16");
            HashMap<String, String> workRoleHourlyRateInTemplate = new HashMap<String, String>();
            String workRole = "WRSAuto3";
            String updateValue = "22";
            int date = 14;

            //Turn on WorkRoleSettingsTemplateOP toggle
//            ToggleAPI.updateToggle(Toggles.WorkRoleSettingsTemplateOP.getValue(), "fiona+99@legion.co", "admin11.a", true);


            //go to user management -> work roles page to check the count of the work roles
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();
            int workRoleCount = userManagementPage.getTotalWorkRoleCount();

//            //go to WRS template details page to check the count is equal with above count or not
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.goToWorkRoleSettingsTile();
            configurationPage.verifyWorkRoleSettingsTemplateListUIAndDetailsUI(templateName, mode);
            configurationPage.checkWorkRoleListShowingWell(workRoleCount);

            //Create new WRS template
            configurationPage.goToConfigurationPage();
            configurationPage.goToWorkRoleSettingsTile();
            configurationPage.publishNewTemplate(templateName1, dynamicGpName, "Custom", "AutoCreatedDynamic---Format Script" + currentTime);

            //Go to the new created template to check the default hourly rate
            configurationPage.clickOnSpecifyTemplateName(templateName1, mode);
            List<String> workRoles = workRoleHourlyRate.keySet().stream().collect(Collectors.toList());
            workRoleHourlyRateInTemplate = configurationPage.getDefaultHourlyRate(workRoles);
            if (workRoleHourlyRateInTemplate.equals(workRoleHourlyRate)) {
                SimpleUtils.pass("The default value is correct");
            } else {
                SimpleUtils.fail("The default value is NOT correct", false);
            }

            //update the hourly rate
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.updateWorkRoleHourlyRate(workRole, updateValue);
            configurationPage.chooseSaveOrPublishBtnAndClickOnTheBtn("publish now");

            //create work role settings template and publish at different time
            configurationPage.createFutureWRSTemplateBasedOnExistingTemplate(templateName1, "publish at different time", date, "edit");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "update and reset location level work role settings template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenLocationLevelWorkRoleSettingsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String locationName = "FionaWorkRoles";
            String workRole = "WRSAuto3";
            Random random = new Random();
            int number = random.nextInt(90) + 10;
            String updateValue = String.valueOf(number);

            //Turn on WorkRoleSettingsTemplateOP toggle
//            ToggleAPI.updateToggle(Toggles.WorkRoleSettingsTemplateOP.getValue(), "fiona+99@legion.co", "admin11.a", true);

            //go to user management -> work roles page to check the count of the work roles
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Work Role Settings", "View");
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Work Role Settings", "Edit");

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.updateWorkRoleHourlyRate(workRole, updateValue);
            configurationPage.saveBtnIsClickable();
            locationsPage.verifyOverrideStatusAtLocationLevel("Work Role Settings", "Yes");
            // there is no reset button for location level work role setting template
//            locationsPage.clickActionsForTemplate("Work Role Settings", "Reset");
//            locationsPage.verifyOverrideStatusAtLocationLevel("Work Role Settings", "No");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}