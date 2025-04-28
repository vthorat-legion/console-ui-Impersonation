package com.legion.tests.core.OpsPortal;

import com.alibaba.fastjson.JSONObject;
import com.legion.api.login.LoginAPI;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.SettingsAndAssociationPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.core.ConsoleLocationSelectorPage;
import com.legion.pages.core.OpCommons.OpsCommonComponents;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.pages.core.OpsPortal.OpsPortalSettingsAndAssociationPage;
import com.legion.pages.core.opemployeemanagement.TimeOffPage;
import com.legion.pages.core.schedule.ConsoleScheduleCommonPage;
import com.legion.pages.core.schedule.ConsoleToggleSummaryPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.Constants;
import com.legion.utils.HttpUtil;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.ro.Si;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.security.auth.login.Configuration;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static com.legion.utils.MyThreadLocal.getDriver;
import static com.legion.utils.MyThreadLocal.getEnterprise;


public class ConfigurationTest extends TestBase {

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
    @TestName(description = "Verify Dynamic Group Function->In Template Association")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDynamicGroupFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String OHtemplate = "Operating Hours";
            //scheduling rules is not included as some exception, will added later
            String mode = "edit";
            String templateName = "LizzyUsingDynamicCheckNoDelete";
            String dynamicGpName = "LZautoTest";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(OHtemplate);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            //check the default save options
            configurationPage.commitTypeCheck();
            //go to Association to check dynamic group dialog UI
            configurationPage.dynamicGroupDialogUICheck(dynamicGpName);
            //edit the dynamic group
            configurationPage.editADynamicGroup(dynamicGpName);
            //search the dynamic group to delete
            configurationPage.deleteOneDynamicGroup(dynamicGpName);
            //save draft template
            configurationPage.saveADraftTemplate();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "New advanced staffing rules page verify")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNewAdvancedStaffingRulePageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Days of Week check box validation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCheckBoxOfDaysOfWeekAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.verifyCheckBoxOfDaysOfWeekSection();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Days of Week formula validation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyInputFormulaForDaysOfWeekAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String formula = "IsDay(p_Truck_Date,-1)";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.inputFormulaInForDaysOfWeekSection(formula);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time of Day Start Section")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOfDayStartSectionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String offsetTime = "10";
            String startEventPoint = "before";
            List<String> dayPartsInGlobalConfig = new ArrayList<String>();
            List<String> ShiftStartTimeEventList = new ArrayList<String>();
            List<String> publicEventList = new ArrayList<String>() {{
                add("Opening Operating Hours");
                add("Closing Operating Hours");
                add("Opening Business Hours");
                add("Closing Business Hours");
            }};

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //go to Global Configuration tab
            locationsPage.goToGlobalConfigurationInLocations();
            dayPartsInGlobalConfig = locationsPage.getAllDayPartsFromGlobalConfiguration();
            publicEventList.addAll(dayPartsInGlobalConfig);

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.inputOffsetTimeForShiftStart(offsetTime, startEventPoint);
            configurationPage.validateShiftStartTimeUnitList();
            ShiftStartTimeEventList = configurationPage.getShiftStartTimeEventList();
            if (ListUtils.isEqualList(ShiftStartTimeEventList, publicEventList)) {
                SimpleUtils.pass("The list of start time event is correct");
            } else {
                SimpleUtils.fail("The list of start time event is NOT correct", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time of Day During Section")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOfDayDuringSectionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String duringTime = "10";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.verifyRadioButtonInTimeOfDayIsSingletonSelect();
            configurationPage.inputShiftDurationMinutes(duringTime);
            configurationPage.validateShiftDurationTimeUnit();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time of Day End Section")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOfDayEndSectionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String endOffsetTime = "10";
            String endEventPoint = "before";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.inputOffsetTimeForShiftEnd(endOffsetTime, endEventPoint);
            configurationPage.validateShiftEndTimeUnitList();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Time of Day Formula Section")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOfDayFormulaSectionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String formulaOfTimeOfDay = "123";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.tickOnCheckBoxOfTimeOfDay();
            configurationPage.inputFormulaInTextAreaOfTimeOfDay(formulaOfTimeOfDay);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Meal and Rest Breaks")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMealAndRestBreaksAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            List<String> mealBreakInfo = new ArrayList<String>() {{
                add("30");
                add("60");
            }};
            List<String> restBreakInfo = new ArrayList<String>() {{
                add("20");
                add("45");
            }};

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.addNewMealBreak(mealBreakInfo);
            configurationPage.addMultipleMealBreaks(mealBreakInfo);
            configurationPage.deleteMealBreak();
            configurationPage.addNewRestBreak(restBreakInfo);
            configurationPage.addMultipleRestBreaks(restBreakInfo);
            configurationPage.deleteRestBreak();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Number of Shifts")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNumberOfShiftsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String shiftsNumber = "6";
            String shiftsNumberFormula = "5";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.inputNumberOfShiftsField(shiftsNumber);
            configurationPage.validCheckBoxOfNumberOfShiftsIsClickable();
            configurationPage.inputFormulaInFormulaTextAreaOfNumberOfShifts(shiftsNumberFormula);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Badges")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyBadgesOfAdvanceStaffingRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.selectBadgesForAdvanceStaffingRules();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "X button and Check Mark button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCrossAndCheckMarkButtonOfAdvanceStaffingRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            List<String> days = new ArrayList<String>() {{
                add("Sunday");
                add("Friday");
            }};

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.selectDaysForDaysOfWeekSection(days);
            configurationPage.verifyCrossButtonOnAdvanceStaffingRulePage();
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.selectDaysForDaysOfWeekSection(days);
            configurationPage.verifyCheckMarkButtonOnAdvanceStaffingRulePage();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Cancel button and Save button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySaveAndCancelButtonOfAdvanceStaffingRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole1 = "AutoUsing2";
            String workRole2 = "Mgr on Duty";
            List<String> days = new ArrayList<String>() {{
                add("Sunday");
                add("Friday");
            }};

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.saveOneAdvanceStaffingRule(workRole1, days);
            configurationPage.cancelSaveOneAdvanceStaffingRule(workRole2, days);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Add Edit and Delete advance staffing rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddEditDeleteFunctionOfAdvanceStaffingRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String shiftsNumber = "7";
            List<String> days = new ArrayList<String>() {{
                add("Sunday");
                add("Friday");
            }};

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.addMultipleAdvanceStaffingRule(workRole, days);
            configurationPage.editAdvanceStaffingRule(shiftsNumber);
            configurationPage.deleteAdvanceStaffingRule();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate advance staffing rule should be shown correct")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAdvancedStaffingRulesShowWellAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String shiftsNumber = "7";
            List<String> days = new ArrayList<String>() {{
                add("Sunday");
                add("Friday");
            }};
            String startOffsetTime = "30";
            String startTimeUnit = "minutes";
            String startEventPoint = "after";
            String startEvent = "Opening Operating Hours";
            String endOffsetTime = "35";
            String endTimeUnit = "minutes";
            String endEventPoint = "before";
            String endEvent = "Closing Operating Hours";

            String shiftsNumber1 = "10";
            List<String> days1 = new ArrayList<String>() {{
                add("Tuesday");
                add("Saturday");
            }};
            String startOffsetTime1 = "40";
            String startTimeUnit1 = "minutes";
            String startEventPoint1 = "after";
            String startEvent1 = "Opening Business Hours";
            String endOffsetTime1 = "55";
            String endTimeUnit1 = "minutes";
            String endEventPoint1 = "before";
            String endEvent1 = "Closing Business Hours";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.validateAdvanceStaffingRuleShowing(startEvent, startOffsetTime, startEventPoint, startTimeUnit,
                    endEvent, endOffsetTime, endEventPoint, endTimeUnit, days, shiftsNumber);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.validateAdvanceStaffingRuleShowing(startEvent1, startOffsetTime1, startEventPoint1, startTimeUnit1,
                    endEvent1, endOffsetTime1, endEventPoint1, endTimeUnit1, days1, shiftsNumber1);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "E2E days of week validation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void daysOfWeekE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "4AutoUsingByFiona";
            List<String> days = new ArrayList<String>() {{
                add("Friday");
                add("Sunday");
            }};
            List<String> daysAbbr = new ArrayList<String>();
            List<String> daysHasShifts = new ArrayList<String>();

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            //Back to console
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
            //go to schedule function
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdateOH();
            daysHasShifts = scheduleShiftTablePage.verifyDaysHasShifts();

            for (String day : days) {
                String dayAbbr = day.substring(0, 3);
                daysAbbr.add(dayAbbr);
            }
            Collections.sort(daysHasShifts);
            Collections.sort(daysAbbr);
            if (days.size() == daysHasShifts.size()) {
                if (ListUtils.isEqualList(daysAbbr, daysHasShifts)) {
                    SimpleUtils.pass("User can create shifts correctly according to AVD staffing rule");
                } else {
                    SimpleUtils.fail("User can't create correct shifts according to AVD staffing rule", false);
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //https://legiontech.atlassian.net/browse/SCH-8338
    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "E2E Verify number of shift function in advance staffing rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void numberOfShiftsInADVRuleE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "AutoUsingByFiona1";
            String shiftsNumber = "3";
            List<String> days = new ArrayList<String>() {{
                add("Friday");
                add("Sunday");
            }};
            List<String> daysAbbr = new ArrayList<String>();
            HashMap<String, String> hoursNTeamMembersCount = new HashMap<>();

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            //Back to console to select one location
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);

            //go to schedule function
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdateOH();
            hoursNTeamMembersCount = scheduleShiftTablePage.getTheHoursNTheCountOfTMsForEachWeekDays();

            List<String> numbersOfShifts = new ArrayList<String>();
            //get abbr for each work day which have shifts
            for (String day : days) {
                String dayAbbr = day.substring(0, 3);
                daysAbbr.add(dayAbbr);
            }
            //get TMs number for each work day which have shifts
            for (String dayAbbr : daysAbbr) {
                String hoursAndTeamMembersCount = hoursNTeamMembersCount.get(dayAbbr);
                String tms = hoursAndTeamMembersCount.trim().split(" ")[1];
                String numberOfTM = tms.replaceAll("TMs", "");
                numbersOfShifts.add(numberOfTM);
            }
            for (String numbersOfShift : numbersOfShifts) {
                if (numbersOfShift.equals(shiftsNumber)) {
                    SimpleUtils.pass("Shifts number is aligned with advance staffing rule");
                } else {
                    SimpleUtils.fail("Shifts number is NOT aligned with advance staffing rule", false);
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "E2E -> Verify the time of day setting in advance staffing rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void timeOfDayInADVRuleE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "AutoUsingByFiona3";
            String shiftTime = "8:30 am - 4:00 pm";
            List<String> indexes = new ArrayList<String>();

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            //back to console mode
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);

            //go to schedule function
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdateOH();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Assigned");
            indexes = scheduleShiftTablePage.getIndexOfDaysHaveShifts();
            for (String index : indexes) {
                scheduleShiftTablePage.verifyShiftTimeInReadMode(index, shiftTime);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "E2E Verify meal and rest break function in advance staffing rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void mealAndRestBreakInADVRuleE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "AutoUsingByFiona3";
            String mealBreakTime = "1:30 pm - 2:15 pm";
            String restBreakTime = "2:30 pm - 3:15 pm";
            HashMap<String, String> mealRestBreaks = new HashMap<String, String>();

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            //back to console mode
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);

            //go to schedule function
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdateOH();
//          Click on edit button on week view
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftOperatePage.clickOnProfileIcon();
//            SimpleUtils.assertOnFail(" context of any TM display doesn't show well", shiftOperatePage.verifyContextOfTMDisplay(), false);
//          Click On Profile icon -> Breaks
            shiftOperatePage.clickOnEditMeaLBreakTime();
            mealRestBreaks = shiftOperatePage.getMealAndRestBreaksTime();

            if (mealRestBreaks.get("Meal Break").compareToIgnoreCase(mealBreakTime) == 0) {
                SimpleUtils.pass("The Meal Break info is correct");
            } else
                SimpleUtils.fail("The Meal Break info is incorrect", false);
            if (mealRestBreaks.get("Rest Break").compareToIgnoreCase(restBreakTime) == 0) {
                SimpleUtils.pass("The Rest Break info is correct");
            } else
                SimpleUtils.fail("The Rest Break info is correct", false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Location Level Advance Staffing Rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void locationLevelAdvanceStaffingRuleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String shiftsNumber = "3";
            List<String> days = new ArrayList<String>() {{
                add("Sunday");
                add("Friday");
            }};
            String startOffsetTime = "30";
            String startTimeUnit = "minutes";
            String startEventPoint = "after";
            String startEvent = "Opening Operating Hours";
            String endOffsetTime = "60";
            String endTimeUnit = "minutes";
            String endEventPoint = "before";
            String endEvent = "Closing Operating Hours";
            String locationName = "AutoUsingByFiona3";
            String workRole = "Auto Using";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Rules", "View");
            locationsPage.goToScheduleRulesListAtLocationLevel(workRole);
            configurationPage.validateAdvanceStaffingRuleShowingAtLocationLevel(startEvent, startOffsetTime, startEventPoint, startTimeUnit,
                    endEvent, endOffsetTime, endEventPoint, endTimeUnit, days, shiftsNumber);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify archive published template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyArchivePublishedTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String action = "Archive";
            String templateType = "Scheduling Policies";
            String templateName = "UsedByAuto";
            String mode = "view";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.archiveIsClickable();
            configurationPage.verifyArchivePopUpShowWellOrNot();
            configurationPage.cancelArchiveDeleteWorkWell(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Schedule Policy")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyTimeOffInSchedulePolicyAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String action = "Archive";
            String templateType = "Scheduling Policies";
            String templateName = "timeOffLimit";
            String mode = "edit";


            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickEdit();
            configurationPage.clickOK();
            configurationPage.verifyTimeOff();
            configurationPage.verifymaxNumEmployeesInput("0");
            configurationPage.verifymaxNumEmployeesInput("-1");
            configurationPage.verifymaxNumEmployeesInput("-1.0");
            configurationPage.verifymaxNumEmployeesInput("1.1");
            configurationPage.verifymaxNumEmployeesInput("1");
            configurationPage.publishNowTemplate();

            configurationPage.switchToControlWindow();

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("NancyTimeOff");

            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName("Nancy TimeOff01");

            TimeOffPage timeOffPage = new TimeOffPage();
            timeOffPage.switchToTimeOffTab();
            OpsCommonComponents commonComponents = new OpsCommonComponents();
            timeOffPage.createTimeOff("Annual Leave1", false, 10, 10);
            String Month = timeOffPage.getMonth();
            commonComponents.okToActionInModal(true);
            timeOffPage.cancelTimeOffRequest();

            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName("Nancy TimeOff02");
            timeOffPage.switchToTimeOffTab();

            timeOffPage.createTimeOff("Annual Leave1", false, 10, 10);
            commonComponents.okToActionInModal(true);
            Assert.assertEquals(timeOffPage.getRequestErrorMessage(), "Maximum numbers of workers on time off exceeded on day " + Month + " 11");
            commonComponents.okToActionInModal(false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify multiple version template UI and Order")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMultipleVersionTemplateCanShowWellInListAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {

            String templateType = "Operating Hours";
            String templateName = "ForMultipleAutoUITesting";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            configurationPage.verifyMultipleTemplateListUI(templateName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Basic staffing rules page verify")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyStaffingRulePageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify workforce sharing in dynamic group in schedule collabration")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleCollaborationOfDynamicGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Schedule Collaboration";
            String templateName = "testDynamicGroup";
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String groupNameForWFS = "AutoWFS" + currentTime;
            String description = "AutoCreate" + currentTime;
            String criteria = "Config Type";
            String criteriaUpdate = "Country";
            String searchText = "AutoCreate";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            SettingsAndAssociationPage settingsAndAssociationPage = pageFactory.createSettingsAndAssociationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);

            boolean flag = configurationPage.isTemplateListPageShow();
            if (flag) {
                SimpleUtils.pass("Template landing page shows well");
            } else
                SimpleUtils.fail("Template landing page loads failed", false);

            configurationPage.searchTemplate(templateName);

            configurationPage.clickOnTemplateName(templateName);

            configurationPage.clickEdit();
            configurationPage.clickOK();
            configurationPage.verifyWorkforceSharingGroup();

            configurationPage.clickOnAssociationTabOnTemplateDetailsPage();
            configurationPage.clickOnBackButton();

            OpsPortalLocationsPage opsPortalLocationsPage = new OpsPortalLocationsPage();
            opsPortalLocationsPage.clickOnLocationsTab();
            opsPortalLocationsPage.goToDynamicGroup();
            opsPortalLocationsPage.eidtExistingDGP();
            opsPortalLocationsPage.searchWFSDynamicGroup(searchText);
            opsPortalLocationsPage.removedSearchedWFSDG();

            opsPortalLocationsPage.addWorkforceSharingDGWithMutiplyCriteria();
            opsPortalLocationsPage.searchWFSDynamicGroup("AutoCreateMutiply");
            opsPortalLocationsPage.removedSearchedWFSDG();

            opsPortalLocationsPage.verifyCriteriaList();
            String locationNum = opsPortalLocationsPage.addWorkforceSharingDGWithOneCriteria(groupNameForWFS, description, criteria);
            String locationNumAftUpdate = opsPortalLocationsPage.editWFSDynamicGroup(groupNameForWFS, criteriaUpdate);

            if (!locationNumAftUpdate.equalsIgnoreCase(locationNum)) {
                SimpleUtils.pass("Update workforce sharing dynamic group successfully");
            }

            opsPortalLocationsPage.searchWFSDynamicGroup(groupNameForWFS);
            opsPortalLocationsPage.removedSearchedWFSDG();
            opsPortalLocationsPage.addWorkforceSharingDGWithOneCriteria(groupNameForWFS, "", criteria);
            opsPortalLocationsPage.searchWFSDynamicGroup(groupNameForWFS);
            opsPortalLocationsPage.removedSearchedWFSDG();

            opsPortalLocationsPage.addWorkforceSharingDGWithOneCriteria(groupNameForWFS + "Custom", description, "Custom");
            opsPortalLocationsPage.addWorkforceSharingDGWithOneCriteria(groupNameForWFS + "Custom", description, "Custom");
            opsPortalLocationsPage.verifyDuplicatedDGErrorMessage();
            opsPortalLocationsPage.searchWFSDynamicGroup(groupNameForWFS);
            opsPortalLocationsPage.removedSearchedWFSDG();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Basic Staffing Rule Fields Verification")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void basicStaffingRuleFieldsVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            List<String> workRoleList = new ArrayList<>();
            List<String> startTimeEventOptionsList = new ArrayList<>();
            List<String> TimeEventOptionsList = new ArrayList<>(Arrays.asList("All Hours", "Opening Business Hours", "Closing Business Hours", "Opening Operating Hours", "Closing Operating Hours", "Incoming Hours",
                    "Outgoing Hours", "Specified Hours", "Peak Hours", "non Peak Hours", "DP1-forAuto", "DP2-forAuto"));
            boolean flag = true;
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.verifyConditionAndNumberFiledCanShowWell();
            configurationPage.verifyNumberInputFieldOfBasicStaffingRule();
            workRoleList = configurationPage.verifyWorkRoleListOfBasicStaffingRule();
            if (workRoleList.size() == 2) {
                if (workRoleList.get(0).equals(workRole) && workRoleList.get(1).equals("Any")) {
                    SimpleUtils.pass("Work role option list in adding new basic staffing rule is correct");
                } else {
                    SimpleUtils.fail("Work role option list in adding new basic staffing rule is NOT correct", false);
                }
            }
            configurationPage.verifyUnitOptionsListOfBasicStaffingRule();
            configurationPage.verifyStartEndOffsetMinutesShowingByDefault();
            configurationPage.verifyStartEndEventPointOptionsList();
            startTimeEventOptionsList = configurationPage.verifyStartEndTimeEventOptionsList();
            for (String startTimeEventOptions : startTimeEventOptionsList) {
                for (String TimeEventOptions : TimeEventOptionsList) {
                    if (startTimeEventOptions.equalsIgnoreCase(TimeEventOptions)) {
                        flag = true;
                        SimpleUtils.pass(startTimeEventOptions + " is showing in Time Event Options List");
                        break;
                    } else {
                        flag = false;
                    }
                }
            }
            if (flag) {
                SimpleUtils.pass("Time Event Options List is correct!");
            } else {
                SimpleUtils.fail("Time Event Options List is NOT correct!", false);
            }
            configurationPage.verifyDaysListShowWell();
            configurationPage.verifyDefaultValueAndSelectDaysForBasicStaffingRule("Tue");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Basic Staffing Rule special fields validation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void basicStaffingRuleSpecialFieldsVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String start = "8:30 AM";
            String end = "9:30 PM";
            String dayParts1 = "DP1-forAuto";
            String dayParts2 = "DP2-forAuto";
            String startEventPoint = "before";
            String endEventPoint = "after";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.setSpecifiedHours(start, end);
            configurationPage.verifyBeforeAndAfterDayPartsShouldBeSameWhenSetAsDayParts(dayParts1, dayParts2, startEventPoint, endEventPoint);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Basic Staffing Rule Cross and Check button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void basicStaffingRuleCrossCheckVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.verifyCrossAndCheckButtonOfBasicStaffingRule();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Basic Staffing Rule badge section")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void basicStaffingRuleBadgeVerificationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String hasBadgeOrNot = "yes";
            String badgeName = "AutoUsing";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.defaultSelectedBadgeOption();
            configurationPage.selectBadgesOfBasicStaffingRule(hasBadgeOrNot, badgeName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Basic Staffing Rule can show well in rule list")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void basicStaffingRuleCanShowWellInListAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole = "AutoUsing2";
            String startTimeEvent = "Opening Operating Hours";
            String endTimeEvent = "Closing Operating Hours";
            String startEventPoint = "after";
            String endEventPoint = "before";
            String workRoleName = "AutoUsing2";
            String unit = "Shifts";
            String condition = "A Minimum";
            List<String> days = new ArrayList<>(Arrays.asList("Mon", "Thu"));
            String number = "2";
            String startOffset = "30";
            String endOffset = "45";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddBasicStaffingRule();
            configurationPage.verifyStaffingRulePageShowWell();
            configurationPage.createBasicStaffingRule(startTimeEvent, endTimeEvent, startEventPoint, endEventPoint,
                    workRoleName, unit, condition, days, number,
                    startOffset, endOffset);
            configurationPage.verifyBasicStaffingRuleIsCorrectInRuleList(startTimeEvent, endTimeEvent, startEventPoint, endEventPoint,
                    workRoleName, unit, condition, days, number,
                    startOffset, endOffset);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Skill Coverage")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void skillCoverageOfBasicStaffingRuleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "Fiona Auto Using";
            String workRole1 = "AutoUsing2";
            String workRole2 = "ForAutomation";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.verifySkillCoverageBasicStaffingRule(workRole1, workRole2);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Basic Staffing Rule E2E")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void basicStaffingRuleE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "BasicStaffingRuleE2E";
            String shiftsNumber = "2";
            String scheduleDayViewGridTimeDurationStart = "8 AM";
            String scheduleDayViewGridTimeDurationEnd = "5 PM";
            String workRoleName = "Auto Using";
            String shiftStartTime = "8:30 am";
            String shiftEndTime = "5:00 pm";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            //Back to console to select one location
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);

            //go to schedule function
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created and set as day view
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdateOH();
            scheduleCommonPage.clickOnDayView();

            //verify schedule table time duration and verify how many TMs in each slot
            if (scheduleCommonPage.isScheduleDayViewActive()) {
                List<String> scheduleDayViewGridTimeDuration = scheduleShiftTablePage.getScheduleDayViewGridTimeDuration();
                if (scheduleDayViewGridTimeDuration.get(0).equalsIgnoreCase(scheduleDayViewGridTimeDurationStart)
                        && scheduleDayViewGridTimeDuration.get(scheduleDayViewGridTimeDuration.size() - 1).equalsIgnoreCase(scheduleDayViewGridTimeDurationEnd)) {
                    SimpleUtils.pass("Schedule Day View Grid Time Duration is correct");
                } else {
                    SimpleUtils.fail("Schedule Day View Grid Time Duration is NOT correct", false);
                }

                List<String> scheduleDayViewBudgetedTeamMembersCount = scheduleShiftTablePage.getScheduleDayViewBudgetedTeamMembersCount();
                for (int i = 1; i < scheduleDayViewBudgetedTeamMembersCount.size() - 1; i++) {
                    if (scheduleDayViewBudgetedTeamMembersCount.get(i).equalsIgnoreCase(shiftsNumber)) {
                        SimpleUtils.pass("Number of shifts set in Basic staffing rule can work well");
                    } else {
                        SimpleUtils.fail("Number of shifts set in Basic staffing rule can work well", false);
                    }
                }

                List<WebElement> allShifts = scheduleShiftTablePage.getAvailableShiftsInDayView();
                List<String> shiftInfo = new ArrayList<>();
                int index = 0;
                List<String> shiftStartTimeList = new ArrayList<>();
                List<String> shiftEndTimeList = new ArrayList<>();
                List<String> workRoleNameList = new ArrayList<>();
                for (int i = 0; i < allShifts.size(); i++) {
                    shiftInfo = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(i);
                    workRoleNameList.add(shiftInfo.get(4));
                    String[] shiftDuration = shiftInfo.get(2).split("-");
                    shiftStartTimeList.add(shiftDuration[0]);
                    shiftEndTimeList.add(shiftDuration[1]);
                }

                //verify the work role is correct or not
                for (String workRole : workRoleNameList) {
                    if (workRole.equalsIgnoreCase(workRoleName)) {
                        SimpleUtils.pass("Work role set in basic staffing rule can work well");
                    } else {
                        SimpleUtils.fail("Work role set in basic staffing rule can work well", false);
                    }
                }

                //verify shift start time is correct or not
                if (shiftStartTimeList.get(0).equalsIgnoreCase(shiftStartTime)) {
                    SimpleUtils.pass("Shift start time set in basic staffing rule can work well");
                } else {
                    SimpleUtils.fail("Shift start time set in basic staffing rule can't work well", false);
                }

                //verify shift end time is correct or not
                if (shiftEndTimeList.get(shiftEndTimeList.size() - 1).equalsIgnoreCase(shiftEndTime)) {
                    SimpleUtils.pass("Shift end time set in basic staffing rule can work well");
                } else {
                    SimpleUtils.fail("Shift end time set in basic staffing rule can't work well", false);
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify other template don't have Override via integration button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOtherTemplateNoOverrideViaIntegrationButtonAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            String templateType = "Scheduling Policies";
            String templateName = "UsedByAuto";

            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.searchTemplate(templateName);
            configurationPage.clickOnSpecifyTemplateName(templateName, "view");
            if (!configurationPage.verifyOverrideViaIntegrationButtonShowingOrNot()) {
                SimpleUtils.pass("There is no Override via integration button for other template types");
            } else {
                SimpleUtils.fail("There is Override via integration button for other template types", false);
            }
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

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify the dynamic group should be shown in advance staffing rule page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyBasicFunctionOfDynamicGroupInADVAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "AutoADVDynamicGroup";
            String workRole = "AMBASSADOR";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.verifyAddButtonOfDynamicLocationGroupOfAdvancedStaffingRuleIsClickable();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify each field of dynamic location group of advance staffing rule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEachFieldsOfDynamicGroupInADVAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "AutoADVDynamicGroup";
            String workRole = "AMBASSADOR";
            String dynamicGpName = "FionaAutoTest";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.verifyAddButtonOfDynamicLocationGroupOfAdvancedStaffingRuleIsClickable();
            //check dynamic group dialog UI add dynamic group
            configurationPage.advanceStaffingRuleDynamicGroupDialogUICheck(dynamicGpName);
            //edit delete dynamic group
            configurationPage.advanceStaffingRuleEditDeleteADynamicGroup(dynamicGpName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Create dynamic group with specify criteria")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateDynamicGroupWithSpecifyCriteriaAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "AutoADVDynamicGroup";
            String workRole = "AMBASSADOR";
            String dynamicGpName = "FionaAutoTest123";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.verifyAddButtonOfDynamicLocationGroupOfAdvancedStaffingRuleIsClickable();
            //Create dynamic group with specify criteria
            configurationPage.createAdvanceStaffingRuleDynamicGroup(dynamicGpName);
            //edit delete dynamic group
            configurationPage.advanceStaffingRuleEditDeleteADynamicGroup(dynamicGpName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Dynamic group dropdown list checking and custom formula description checking")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDynamicGroupDropdownListCheckingAndCustomFormulaDescriptionCheckingAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "AutoADVDynamicGroup";
            String workRole = "AMBASSADOR";
            String dynamicGpName = "FionaAutoTest123";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectWorkRoleToEdit(workRole);
            configurationPage.checkTheEntryOfAddAdvancedStaffingRule();
            configurationPage.verifyAdvancedStaffingRulePageShowWell();
            configurationPage.verifyAddButtonOfDynamicLocationGroupOfAdvancedStaffingRuleIsClickable();
            //Create dynamic group with specify criteria
            configurationPage.advanceStaffingRuleDynamicGroupCriteriaListChecking(dynamicGpName);
            configurationPage.advanceStaffingRuleEditDeleteADynamicGroup(dynamicGpName);
            configurationPage.advanceStaffingRuleDynamicGroupCustomFormulaDescriptionChecking();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify dynamic group item in advanced staffing rules is optional")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDynamicGroupOfAdvanceStaffingRulesIsOptionalAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateType = "Scheduling Rules";
            String mode = "edit";
            String templateName = "AutoADVDynamicGroup";
            String workRole = "AMBASSADOR";
            List<String> days = new ArrayList<String>() {{
                add("Sunday");
                add("Friday");
            }};

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.verifyDynamicGroupOfAdvanceStaffingRuleIsOptional(workRole, days);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //https://legiontech.atlassian.net/browse/OPS-5643; https://legiontech.atlassian.net/browse/OPS-6254
    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Advance staffing rule dynamic group E2E flow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyAdvanceStaffingRulesDynamicGroupE2EAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName1 = "AutoADVDynamicGroup02";
            String locationName2 = "AutoADVDynamicGroup03";
            List<String> days1 = new ArrayList<String>() {{
                add("Sunday");
                add("Friday");
            }};
            List<String> days2 = new ArrayList<String>() {{
                add("Monday");
                add("Tuesday");
                add("Wednesday");
            }};
            List<String> daysAbbr1 = new ArrayList<String>();
            List<String> daysHasShifts1 = new ArrayList<String>();
            List<String> daysAbbr2 = new ArrayList<String>();
            List<String> daysHasShifts2 = new ArrayList<String>();

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            //Back to console
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName1);
            //go to schedule function
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdateOH();
            daysHasShifts1 = scheduleShiftTablePage.verifyDaysHasShifts();

            for (String day : days1) {
                String dayAbbr = day.substring(0, 3);
                daysAbbr1.add(dayAbbr);
            }
            Collections.sort(daysHasShifts1);
            Collections.sort(daysAbbr1);
            if (days1.size() == daysHasShifts1.size()) {
                if (ListUtils.isEqualList(daysAbbr1, daysHasShifts1)) {
                    SimpleUtils.pass("User can create shifts correctly according to AVD staffing rule");
                } else {
                    SimpleUtils.fail("User can't create correct shifts according to AVD staffing rule", false);
                }
            }
            //switch to another location to check the shifts
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName2);
            //go to schedule function
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            // Navigate to a week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule if not created
            boolean isWeekGenerated1 = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated1) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdateOH();
            daysHasShifts2 = scheduleShiftTablePage.verifyDaysHasShifts();

            for (String day : days2) {
                String dayAbbr = day.substring(0, 3);
                daysAbbr2.add(dayAbbr);
            }
            Collections.sort(daysHasShifts2);
            Collections.sort(daysAbbr2);
            if (days2.size() == daysHasShifts2.size()) {
                if (ListUtils.isEqualList(daysAbbr2, daysHasShifts2)) {
                    SimpleUtils.pass("User can create shifts correctly according to AVD staffing rule");
                } else {
                    SimpleUtils.fail("User can't create correct shifts according to AVD staffing rule", false);
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Work Role settings E2E")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class,enabled = false)
    public void workRoleSettingsE2EAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            String locationName = "WorkRoleSettings";
            String workRole ="WRSAuto3";
            float hourlyRate = 74;
            //go to console side
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            //go to schedule function
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());

            ForecastPage forecastPage = pageFactory.createForecastPage();
            forecastPage.clickOnLabor();
            forecastPage.verifyLaborForecastCanLoad();
            forecastPage.verifyRefreshBtnInLaborWeekView();
            forecastPage.selectWorkRoleFilterByText(workRole);
            HashMap<String, Float> hoursAndWages = forecastPage.getSummaryLaborHoursAndWages();
            float hours = hoursAndWages.get("ForecastHours");
            float wages = hoursAndWages.get("ForecastWages");
            if(hours * hourlyRate == wages){
                SimpleUtils.pass("The wages is correct!");
            }else {
                SimpleUtils.fail("The wages is NOT correct!",false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user can create new dynamic group with formula")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateTemplateWithFormulaAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime =dfs.format(new Date()).trim();
            String templateType="Scheduling Rules";
            String templateName = "FionaTest"+currentTime;
            String dynamicGpName = "FionaTestD"+currentTime;
            Random random=new Random();
            int number=random.nextInt(90)+10;
            String formula ="IsLocationAttribute(p_FionaUsing," + number + ")";

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            //create Operating Hour template and published it
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            //Create one current published version template
            configurationPage.publishNewTemplate(templateName,dynamicGpName,"Custom",formula);
            configurationPage.archivePublishedOrDeleteDraftTemplate(templateName,"Archive");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}