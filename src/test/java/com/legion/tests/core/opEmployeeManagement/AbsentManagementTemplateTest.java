package com.legion.tests.core.opEmployeeManagement;

import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpCommons.OpsCommonComponents;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.OpCommons.RightHeaderBarPage;
import com.legion.pages.core.opemployeemanagement.AbsentManagePage;
import com.legion.pages.core.opemployeemanagement.EmployeeManagementPanelPage;
import com.legion.pages.core.opemployeemanagement.TimeOffReasonConfigurationPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public class AbsentManagementTemplateTest extends TestBase {
    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        this.createDriver((String) params[0], "83", "Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        RightHeaderBarPage modelSwitchPage = new RightHeaderBarPage();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        modelSwitchPage.switchToOpsPortal();
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Employee manage tab and absence management tile")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEmployeeManagementModuleAndDashboardAsInternalAdmin(String browser, String username, String password, String location) {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        //verify that employee management is enabled.
        navigationPage.navigateToEmployeeManagement();
        SimpleUtils.pass("EmployeeManagement Module is enabled!");
        //verify the absent management dashboard card content
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        String dashboardText = panelPage.getDashboardCardContent();
        Assert.assertEquals(dashboardText, "Time Off Management\n" +
                "Configure Time Offs\n" +
                "Time Off Reasons\n" +
                "Time Off Accrual Rules", "Invalid content on dashboard card!");
        SimpleUtils.pass("Succeeded in validating Absent Management dashboard card content!");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Templates list page validation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddEditSearchAndDisableTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        //verify that there are 2 tabs in absent manage
        AbsentManagePage absentManagePage = new AbsentManagePage();
        Assert.assertTrue(absentManagePage.isTemplateTabDisplayed(), "Template tab on absent manage page didn't show!");
        Assert.assertTrue(absentManagePage.isSettingsTabDisplayed(), "Settings tab on absent manage page didn't show!");
        SimpleUtils.pass("Template tab and settings tab can be displayed normally!");

        //Verify there is a new template button
        Assert.assertTrue(absentManagePage.isNewTemplateButtonDisplayedAndEnabled(), "New template button didn't show or it is disabled!");
        SimpleUtils.pass("Succeeded in validating new template button shown!");

        //verify cancel creating template
        absentManagePage.createANewTemplate("CancelCreating", "for test");
        absentManagePage.cancel();
        SimpleUtils.pass("Succeeded in canceling creation!");

        //verify new template works well
        absentManagePage.search("AutoTest01");
        if (!absentManagePage.getResult().equalsIgnoreCase("")) {
            absentManagePage.clickInDetails();
            SimpleUtils.pass("Succeeded in validating removing time off rules works well!");

            absentManagePage.deleteTheTemplate();
            absentManagePage.okToActionInModal(true);
        }

        absentManagePage.createANewTemplate("AutoTest01", "for test");
        absentManagePage.submit();
        absentManagePage.saveTemplateAs("Save as draft");
        SimpleUtils.pass("Succeeded in creating a template!");

        //verify search template UI
        Assert.assertEquals(absentManagePage.getTemplateSearchBoxPlaceHolder(), "You can search by template name, status and creator.", "Wrong place holder in template search box!");
        SimpleUtils.pass("Succeeded in validating place holder in template search box is correct!");
        //Verify search function
        //no match
        absentManagePage.search("CancelCreating");
        Assert.assertEquals(absentManagePage.noMatch(), "No matching Time Off Management Templates found.", "The template canceled should not be searched out!");
        SimpleUtils.pass("Succeeded in validating no match!");

        //Fuzzy matching
        absentManagePage.search("test");
        Assert.assertTrue(absentManagePage.isRelated("test"), "Failed to search out related items!");
        SimpleUtils.pass("Succeeded in validating Fuzzy matching!");

        //exactly matching
        absentManagePage.search("AutoTest01");
        Assert.assertEquals(absentManagePage.getResult(), "AutoTest01", "Failed to search out the template just created!");
        SimpleUtils.pass("Succeeded in searching template by name!");

        //verify edit template
        absentManagePage.clickInDetails();
        //cancel editing
        absentManagePage.editTemplateInfo("Auto-CancelEditing", "", "test");
        absentManagePage.okToActionInModal(false);
        //save editing
        absentManagePage.editTemplateInfo("Auto-SaveEditing", "test save editing", "test");
        absentManagePage.okToActionInModal(true);
        Assert.assertTrue(absentManagePage.getTemplateTitle().contains("Auto-SaveEditing"), "Failed to save the editing!");
        absentManagePage.saveTemplateAs("SaveAsDraft");
        absentManagePage.search("Auto-SaveEditing");
        Assert.assertEquals(absentManagePage.getResult(), "Auto-SaveEditing", "Failed to search out the template just created!");
        SimpleUtils.pass("Succeeded in editing template!");

        //verify delete a template
        absentManagePage.clickInDetails();
        String mes = absentManagePage.deleteTheTemplate();
        Assert.assertTrue(mes.contains("You will no longer recover this template."), "Failed to get delete confirm message in modal!");//Are you sure you want to Delete?

        SimpleUtils.pass("Succeeded in opening delete template modal!");
        absentManagePage.okToActionInModal(true);
        SimpleUtils.pass("Succeeded in deleting template!");

        absentManagePage.search("Auto-SaveEditing");
        Assert.assertEquals(absentManagePage.noMatch(), "No matching Time Off Management Templates found.", "The template canceled should not be searched out!");

        absentManagePage.search("");
        Assert.assertEquals(absentManagePage.getTemplateTableHeaders(), templateColumns(), "Incorrect template table headers!");
        SimpleUtils.pass("Succeeded in template table column validation!");

        Assert.assertTrue(absentManagePage.smartCardFilter(), "It doesn't filter correctly!");
        SimpleUtils.pass("Succeeded in smart card filter validation!");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Settings page validation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyActionsInSettingsTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();

        AbsentManagePage absentManagePage = new AbsentManagePage();
        absentManagePage.switchToSettings();
        //reason name is required when creating a new time off
        Assert.assertFalse(absentManagePage.isOKButtonEnabled(), "OK button should not be enabled while missing reason name!");
        SimpleUtils.pass("Succeeded in reason name required validation!");
        absentManagePage.cancelCreatingTimeOff();
        //add new reason not unique!
        absentManagePage.addTimeOff("Sick");
        absentManagePage.okCreatingTimeOff();
        Assert.assertEquals(absentManagePage.getErrorMessage(), "Time off reason name should be unique.", "Failed to add for Using an existing time off reason name!!!");
        SimpleUtils.pass("Succeeded in reason name unique validation!");
        absentManagePage.cancelCreatingTimeOff();
        //cancel adding new time off
        String timeOffName = "ZZ-vacation";
        //absentManagePage.removeTimeOffReasons(timeOffName);

        //Remove timeOffReason
        Boolean timeOffReasonFlag;
        timeOffReasonFlag = absentManagePage.searchTimeOffReason(timeOffName);

        if(timeOffReasonFlag == true){
            SimpleUtils.fail("Non exist time off reason search successfully", false);
            Assert.assertEquals(absentManagePage.removeTimeOffInSettings(), "Are you sure you want to remove this time off reason?", "Failed to get confirm message in remove modal!");
            absentManagePage.okToActionInModal(true);
        }else
            SimpleUtils.pass("Non exist time off reason search failed");

        absentManagePage.addTimeOff(timeOffName);
        absentManagePage.cancelCreatingTimeOff();
        Assert.assertFalse(absentManagePage.isTimeOffReasonDisplayed(timeOffName), "Failed to cancel adding!");
        SimpleUtils.pass("Succeeded in canceling new time off!");

        //add new time off
        absentManagePage.addTimeOff(timeOffName);
        absentManagePage.okCreatingTimeOff();
        Assert.assertTrue(absentManagePage.isTimeOffReasonDisplayed(timeOffName), "Failed to add new time off!");
        SimpleUtils.pass("Succeeded in creating new time off!");

        //cancel editing time off
        String editName = "ZZ-vacation-editing";
        absentManagePage.editTimeOffReason(editName);
        absentManagePage.okToActionInModal(false);
        Assert.assertFalse(absentManagePage.isTimeOffReasonDisplayed(editName), "Failed to cancel editing!");
        SimpleUtils.pass("Succeeded in canceling edit action!");


        //edit time off
        absentManagePage.editTimeOffReason(editName);
        absentManagePage.okToActionInModal(true);
        Assert.assertTrue(absentManagePage.isTimeOffReasonDisplayed(editName), "Failed to edit an existing time off!");
        SimpleUtils.pass("Succeeded in updating a time off reason!");


        //cancel removing
        Assert.assertEquals(absentManagePage.removeTimeOffInSettings(), "Are you sure you want to remove this time off reason?", "Failed to get confirm message in remove modal!");
        absentManagePage.okToActionInModal(false);
        Assert.assertTrue(absentManagePage.isTimeOffReasonDisplayed(editName), "Failed to cancel remove!");
        SimpleUtils.pass("Succeeded in canceling remove action!");

        //remove
        Assert.assertEquals(absentManagePage.removeTimeOffInSettings(), "Are you sure you want to remove this time off reason?", "Failed to get confirm message in remove modal!");
        absentManagePage.okToActionInModal(true);
        Assert.assertFalse(absentManagePage.isTimeOffReasonDisplayed(editName), "Failed to remove!");
        SimpleUtils.pass("Succeeded in removing time off!");

        //settings
        Assert.assertEquals(absentManagePage.getQuestionTitle(), "Do time off reasons use accruals?", "Failed to Switch to settings page!");
        SimpleUtils.pass("Succeeded in switching to settings!");


        //set accrual toggle as false
        absentManagePage.setAccrualToggle(false);
        absentManagePage.switchToTemplates();
        absentManagePage.configureTemplate("Accrual-usedForAutomation");
        absentManagePage.configureTimeOffRules("Sick");
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();
        Assert.assertFalse(configurationPage.isAccrualDistributionLabelDisplayed(), "The accrual distribution label should not be displayed as accrual toggle is off!");
        SimpleUtils.pass("Succeeded in turning off accrual toggle!");

        absentManagePage.back();
        absentManagePage.back();
        absentManagePage.switchToSettings();

        //set accrual toggle as ture
        absentManagePage.setAccrualToggle(true);
        absentManagePage.switchToTemplates();
        absentManagePage.configureTemplate("Accrual-usedForAutomation");
        absentManagePage.configureTimeOffRules("Sick");
        Assert.assertTrue(configurationPage.isAccrualDistributionLabelDisplayed(), "The accrual distribution label should be displayed as accrual toggle is on!");
        SimpleUtils.pass("Succeeded in turning on accrual toggle!");

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Template Details Page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false) //blocked by OPS-6707
    public void verifyTemplateDetailsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();
        absentManagePage.search("AutoTest_Accrual");
        int maxNumOfTemp=absentManagePage.templateNumber() * 2;
        for (int i = 0; i < maxNumOfTemp; i++) {
            if (!absentManagePage.isNoMatchMessageDisplayed()) {
                absentManagePage.clickInDetails();
                if (absentManagePage.isDeleteButtonDisplayed()) {
                    absentManagePage.deleteTheTemplate();
                    absentManagePage.okToActionInModal(true);
                } else {
                    absentManagePage.archivePublishedTemplate();
                    absentManagePage.okToActionInModal(true);
                }
                i++;
                absentManagePage.search("AutoTest_Accrual");
            }
        }

        Random random = new Random();
        String tempName = "AutoTest_Accrual" + random.nextInt(100);
        absentManagePage.createANewTemplate(tempName, "accrual test");
        absentManagePage.submit();
        //absentManagePage.closeWelcomeModal();
        absentManagePage.saveTemplateAs("Save as draft");
        SimpleUtils.pass("Succeeded in creating template: " + tempName + " !");

        absentManagePage.search(tempName);
        //click in details--update a template info--save draft base on draft mode
        absentManagePage.clickInDetails();

        //template name
        Assert.assertTrue(absentManagePage.getTemplateTitle().contains(tempName), "History button should be displayed!");
        SimpleUtils.pass("Succeeded in validating template name displayed well!");
        //buttons
        Assert.assertTrue(absentManagePage.isHistoryButtonDisplayed(), "History button should be displayed!");
        Assert.assertTrue(absentManagePage.isDeleteButtonDisplayed(), "History button should be displayed!");
        Assert.assertTrue(absentManagePage.isEditButtonDisplayed(), "History button should be displayed!");
        SimpleUtils.pass("Succeeded in validating buttons displayed well!");

        //switch between details and associations
        absentManagePage.switchToAssociation();
        Assert.assertEquals(absentManagePage.getTemplateAssociationTitle(), "Dynamic Employee Groups", "Failed to switch to association page!");
        absentManagePage.switchToDetails();
        Assert.assertEquals(absentManagePage.getCanEmployeeRequestLabel(), "Can employees request time off ?", "Failed to switch to details page!");
        SimpleUtils.pass("Succeeded in validating switch between details and association!");

        //open template history
        absentManagePage.openTemplateHistory();
        Assert.assertTrue(absentManagePage.getCreatedRecord().contains("Template Created"), "It should display template created info!");
        absentManagePage.closeHistory();

        //save as draft on draft version.
        absentManagePage.editTemplateInfo(tempName, "accrual", "1234");
        absentManagePage.okToActionInModal(true);
        absentManagePage.setTemplateLeverCanRequest(false);
        absentManagePage.saveTemplateAs("Save as draft");
        absentManagePage.search(tempName);
        absentManagePage.clickInDetails();
        Assert.assertTrue(absentManagePage.isToggleAsSetted(false), "Failed to turn off the template lever--can employee request! ");
        absentManagePage.back();
        //set as true
        absentManagePage.configureTemplate(tempName);
        absentManagePage.setTemplateLeverCanRequest(true);
        //template lever weekly limits
        absentManagePage.setTemplateLeverWeeklyLimits("40");
        //save as draft on a draft version
        absentManagePage.saveTemplateAs("Save as draft");
        absentManagePage.search(tempName);
        Assert.assertTrue(absentManagePage.getTemplateStatus().get(0).equals("Draft"), "Failed to save the template as draft!");
        SimpleUtils.pass("Succeeded in saving as draft!");

        //validate the toggle setting and weekly limit setting
        absentManagePage.clickInDetails();
        Assert.assertTrue(absentManagePage.isToggleAsSetted(true), "Failed to turn on the template lever--can employee request! ");
        //validate weekly limits
        /*Assert.assertEquals(absentManagePage.getWeeklyLimitHrs(), "40", "Failed to validate the number as set before!");*/
        SimpleUtils.pass("Succeeded in validating template lever--can employee request toggle!");
        absentManagePage.back();

        //publish after associating
        absentManagePage.configureTemplate(tempName);
        absentManagePage.associateTemplate("OML16ForAuto");
        absentManagePage.saveAssociation();
        SimpleUtils.pass("Succeeded in associating the template!");
        absentManagePage.switchToDetails();
        //configure a rule before publish
        absentManagePage.configureTimeOffRules("Annual Leave");
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();
        configurationPage.setTimeOffRequestRuleAs("Employee can request ?", true);
        configurationPage.setTimeOffRequestRuleAs("Employee can request partial day ?", true);
        configurationPage.setDistributionMethod("Monthly");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);
        absentManagePage.saveTemplateAs("Publish now");

        absentManagePage.search(tempName);
        Assert.assertTrue(absentManagePage.getTemplateStatus().get(0).equals("Published"), "Failed to save the template as publish later!");
        SimpleUtils.pass("Succeeded in saving as publish now!");

        //Publish at different time
        /*absentManagePage.configureTemplate(tempName);
        absentManagePage.saveTemplateAs("Publish later");
        absentManagePage.search(tempName);
        Assert.assertTrue(absentManagePage.getTemplateStatus().get(0).equals("Published"), "Failed to save the template as publish now!");
        SimpleUtils.pass("Succeeded in saving as publish now!");*/

        //validate there are 2 versions
        absentManagePage.configureTemplate(tempName);
        absentManagePage.saveTemplateAs("Save as draft");
        absentManagePage.search(tempName);
        absentManagePage.caretDown();
        Assert.assertTrue(absentManagePage.getTemplateStatus().size() == 2 && absentManagePage.getTemplateStatus().get(0).equals("Published") && absentManagePage.getTemplateStatus().get(1).equals("Draft"));
        SimpleUtils.pass("Succeeded in saving as draft on a published version!");

        //delete a draft template
        absentManagePage.clickInDetails();
        absentManagePage.deleteTheTemplate();
        absentManagePage.okToActionInModal(true);
        absentManagePage.search(tempName);
        Assert.assertTrue(absentManagePage.getTemplateStatus().size() == 1 && absentManagePage.getTemplateStatus().get(0).equals("Published"));
        SimpleUtils.pass("Succeeded in deleting a draft template!");


        //Mark template as default (published)
        absentManagePage.clickInDetails();
        absentManagePage.markAsDefaultTemplate();
        absentManagePage.back();
        absentManagePage.search(tempName);
        Assert.assertEquals(absentManagePage.getDefaultLabel(), "Default", "Failed to mark as default template! ");
        SimpleUtils.pass("Succeeded in marking as default template!");

        //archive
        //absentManagePage.search(tempName);
        absentManagePage.clickInDetails();
        //verify archive button displays well!
        Assert.assertTrue(absentManagePage.isArchiveButtonDisplayed(), "Archive button should display here!");
        SimpleUtils.pass("Succeeded in validating archive button!");
        //archive published template
        absentManagePage.archivePublishedTemplate();
        absentManagePage.okToActionInModal(true);
        absentManagePage.search(tempName);
        Assert.assertEquals(absentManagePage.noMatch(), "No matching Templates found.", "Failed to archive the template");
        SimpleUtils.pass("Succeeded in archiving published template!");

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Template Details Page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOffConfigurationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();

        //Create a new template
        Random random = new Random();
        String tempName = "AutoTest_Accrual" + random.nextInt(1000);
        absentManagePage.createANewTemplate(tempName, "accrual test");
        absentManagePage.submit();
        //absentManagePage.closeWelcomeModal();
        absentManagePage.saveTemplateAs("Save as draft");
        SimpleUtils.pass("Succeeded in creating template: " + tempName + " !");


        absentManagePage.configureTemplate(tempName);
        //configure
        String timeOffReason = "Sick";
        absentManagePage.configureTimeOffRules(timeOffReason);
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();

        //verify accrual started options
        Assert.assertTrue(configurationPage.getAccrualStartOptions().equals(accrualStarted()), "Failed to assert accrual started as expected!");
        SimpleUtils.pass("Succeeded in validating accrual started as expected!");

        //verify accrual end option
        Assert.assertTrue(configurationPage.getAccrualEndOptions().equals(accrualEnd()), "Failed to assert accrual end as expected!");
        SimpleUtils.pass("Succeeded in validating accrual end as expected!");

        //set the reinstatement months
        configurationPage.setReinstatementMonth("6");

        //verify distribution method options
        Assert.assertTrue(configurationPage.getDistributionOptions().equals(distributionMethod()), "Failed to assert Distribution methhods as expected!");
        SimpleUtils.pass("Succeeded in validating distribution method options!");

        //add service lever
        /*configurationPage.addServiceLever();
        configurationPage.addSecondServiceLever();*/
        configurationPage.setDistributionMethod("Monthly");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.addSpecifiedServiceLever(2, "24", "6", "30");

        configurationPage.saveTimeOffConfiguration(true);

        SimpleUtils.pass("Succeeded in validating configure button is clickable, adding service lever, and configure function works well!");

        //edit
        absentManagePage.configureTimeOffRules(timeOffReason);
        //assert reinstatement
        //
        configurationPage.removeServiceLever();
        configurationPage.saveTimeOffConfiguration(true);
        SimpleUtils.pass("Succeeded in validating edit button is clickable, remove service lever, and edit function works well!");
        absentManagePage.saveTemplateAs("Save As Draft");


        //verify it is readOnly in view mode
        absentManagePage.search(tempName);
        absentManagePage.clickInDetails();
        absentManagePage.viewTimeOffConfigure(timeOffReason);
        Assert.assertEquals(configurationPage.getTimeOffReasonName(), timeOffReason, "Failed to open time off reason configuration page!");
        Assert.assertTrue(configurationPage.isTimeOffConfigurationReadOnly(), "Failed to assert it is read-only in view mode!");
        SimpleUtils.pass("Succeeded in validating it is read-only in view mode!");
        Assert.assertEquals(configurationPage.getServiceLeverNum(), 1, "Failed to remove one of the service lever!");
        SimpleUtils.pass("Succeeded in validating remove service lever!");

        configurationPage.back();
        absentManagePage.back();
        //remove time off rules
        absentManagePage.configureTemplate(tempName);
        absentManagePage.removeTimeOffRules(timeOffReason);
        absentManagePage.saveTemplateAs("Save As Draft");
        absentManagePage.search(tempName);
        absentManagePage.clickInDetails();
        Assert.assertEquals(absentManagePage.getNotConfigured(), "Not Configured", "Failed to remove time off reason rules!");
        SimpleUtils.pass("Succeeded in validating removing time off rules works well!");

        absentManagePage.deleteTheTemplate();
        absentManagePage.okToActionInModal(true);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Template Details Page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOffRequestRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();

        //Create a new template
        Random random = new Random();
        String tempName = "AutoTest_Accrual" + random.nextInt(1000);
        absentManagePage.createANewTemplate(tempName, "accrual test");
        absentManagePage.submit();
        //absentManagePage.closeWelcomeModal();
        absentManagePage.saveTemplateAs("Save as draft");
        SimpleUtils.pass("Succeeded in creating template: " + tempName + " !");

        absentManagePage.configureTemplate(tempName);
        //configure
        String timeOffReason = "Sick";
        absentManagePage.configureTimeOffRules(timeOffReason);
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();

        //verify request rules labels
        //Assert.assertTrue(configurationPage.getRequestRuleLabels().equals(requestRules()), "Failed to assert request rules is shown as expected!");
        SimpleUtils.pass("Succeeded in validating all request rules!");
        //
        Assert.assertTrue(configurationPage.getProbationPeriodUnitOptions().equals(probationUnit()), "Failed to probation unit is shown as expected!");
        SimpleUtils.pass("Succeeded in validating probation options!");

        //set all rule toggles as yes
        //set value for rules
        configurationPage.setTimeOffRequestRuleAs("Employee can request?", true);
        configurationPage.setTimeOffRequestRuleAs("Employee can request partial day?", true);
        configurationPage.setTimeOffRequestRuleAs("Manager can submit in timesheet?", true);
        configurationPage.setValueForTimeOffRequestRules("Weekly limits (hours)", "32");
        configurationPage.setValueForTimeOffRequestRules("Days request must be made in advance", "2");
        configurationPage.setValueForTimeOffRequestRules("Configure all day time off default", "8");
        configurationPage.setValueForTimeOffRequestRules("Days an employee can request at one time", "3");
        configurationPage.setTimeOffRequestRuleAs("Auto reject time off that exceeds accrued hours ?", true);
        configurationPage.setTimeOffRequestRuleAs("Allow Paid Time Off to compute to overtime ?", true);
        configurationPage.setTimeOffRequestRuleAs("Does this time off reason track Accruals ?", true);
        configurationPage.setValueForTimeOffRequestRules("Max hours in advance of what you earn", "8");
        configurationPage.setTimeOffRequestRuleAs("Enforce Yearly Limits", true);
        configurationPage.setValueForTimeOffRequestRules("Probation Period", "3");
        configurationPage.setProbationUnitAsMonths();
        configurationPage.setValueForTimeOffRequestRules("Annual Use Limit", "5");

        configurationPage.setDistributionMethod("Monthly");
        configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);
        //verify results of above action
        absentManagePage.configureTimeOffRules(timeOffReason);
        Assert.assertTrue(configurationPage.isTimeOffRuleToggleTurnOn("Employee can request ?"), "Failed to turn on--Employee can request ?");
        Assert.assertTrue(configurationPage.isTimeOffRuleToggleTurnOn("Employee can request partial day ?"), "Failed to turn on--Employee can request partial day ?");
        Assert.assertTrue(configurationPage.isTimeOffRuleToggleTurnOn("Is this considered a paid time off ?"), "Failed to turn on--Manager can submit in timesheet ?");
        Assert.assertTrue(configurationPage.isTimeOffRuleToggleTurnOn("Auto reject time off which exceed accrued hours ?"), "Failed to turn on--Auto reject time off which exceed accrued hours ?");
        Assert.assertTrue(configurationPage.isTimeOffRuleToggleTurnOn("Allow Paid Time Off to compute to overtime ?"), "Failed to turn on--Allow Paid Time Off to compute to overtime ?");
        Assert.assertTrue(configurationPage.isTimeOffRuleToggleTurnOn("Does this time off reason track Accruals ?"), "Failed to turn on--Does this time off reason track Accruals ?");
        Assert.assertTrue(configurationPage.isTimeOffRuleToggleTurnOn("Enforce Yearly Limits"), "Failed to turn on--Enforce Yearly Limits");

        //verify values we set before
        Assert.assertEquals(configurationPage.getNumSetForTimeOffRequestRules("Weekly limits(hours)"), "32");
        Assert.assertEquals(configurationPage.getNumSetForTimeOffRequestRules("Days request must be made in advance"), "2");
        Assert.assertEquals(configurationPage.getNumSetForTimeOffRequestRules("Configure all day time off default"), "8");
        Assert.assertEquals(configurationPage.getNumSetForTimeOffRequestRules("Days an employee can request at one time"), "3");
        Assert.assertEquals(configurationPage.getNumSetForTimeOffRequestRules("Max hours in advance of what you earn"), "8");
        Assert.assertEquals(configurationPage.getNumSetForTimeOffRequestRules("Probation Period"), "3");
        Assert.assertEquals(configurationPage.getNumSetForTimeOffRequestRules("Annual Use Limit"), "5");
        SimpleUtils.pass("Succeeded in setting all request rules!");
        //set all rule toggles as no
        configurationPage.setTimeOffRequestRuleAs("Employee can request ?", false);
        configurationPage.setTimeOffRequestRuleAs("Employee can request partial day ?", false);
        configurationPage.setTimeOffRequestRuleAs("Is this considered a paid time off ?", false);
        configurationPage.setTimeOffRequestRuleAs("Auto reject time off which exceed accrued hours ?", false);
        configurationPage.setTimeOffRequestRuleAs("Allow Paid Time Off to compute to overtime ?", false);
        configurationPage.setTimeOffRequestRuleAs("Does this time off reason track Accruals ?", false);
        configurationPage.setTimeOffRequestRuleAs("Enforce Yearly Limits", false);
        //save edit
        configurationPage.saveTimeOffConfiguration(true);
        //verify results
        absentManagePage.configureTimeOffRules(timeOffReason);
        Assert.assertFalse(configurationPage.isTimeOffRuleToggleTurnOn("Employee can request ?"), "Failed to turn off--Employee can request ?");
        Assert.assertFalse(configurationPage.isTimeOffRuleToggleTurnOn("Employee can request partial day ?"), "Failed to turn off--Employee can request partial day ?");
        Assert.assertFalse(configurationPage.isTimeOffRuleToggleTurnOn("Is this considered a paid time off ?"), "Failed to turn off--Manager can submit in timesheet ?");
        Assert.assertFalse(configurationPage.isTimeOffRuleToggleTurnOn("Auto reject time off which exceed accrued hours ?"), "Failed to turn off--Auto reject time off which exceed accrued hours ?");
        Assert.assertFalse(configurationPage.isTimeOffRuleToggleTurnOn("Allow Paid Time Off to compute to overtime ?"), "Failed to turn off--Allow Paid Time Off to compute to overtime ?");
        Assert.assertFalse(configurationPage.isTimeOffRuleToggleTurnOn("Does this time off reason track Accruals ?"), "Failed to turn off--Does this time off reason track Accruals ?");
        Assert.assertFalse(configurationPage.isTimeOffRuleToggleTurnOn("Enforce Yearly Limits"), "Failed to turn off--Enforce Yearly Limits");

        //verify cancel button is clickable
        configurationPage.saveTimeOffConfiguration(false);
        SimpleUtils.pass("Succeeded in validating cancel button in time-off rules configuration page is clickable!");
        absentManagePage.saveTemplateAs("Save as draft");
        //clear test data
        absentManagePage.search(tempName);
        absentManagePage.clickInDetails();
        absentManagePage.deleteTheTemplate();
        absentManagePage.okToActionInModal(true);
    }


    public ArrayList<String> templateColumns() {
        ArrayList<String> col = new ArrayList<String>();
        col.add("");
        col.add("Template");
        col.add("Status");
        col.add("Editor");
        col.add("Effective Date");
        col.add("Last Modified Date");
        return col;
    }

    public ArrayList<String> accrualStarted() {
        ArrayList<String> accrualStarted = new ArrayList<String>();
        accrualStarted.add("Hire Date");
        //accrualStarted.add("Seniority Date"); this one has been disabled.
        accrualStarted.add("Specified Date");
        return accrualStarted;
    }

    public ArrayList<String> accrualEnd() {
        ArrayList<String> accrualEnd = new ArrayList<String>();
        accrualEnd.add("");
        accrualEnd.add("Hire Date");
        //accrualEnd.add("Seniority Date");
        accrualEnd.add("Specified Date");
        return accrualEnd;
    }

    public ArrayList<String> distributionMethod() {
        ArrayList<String> distribution = new ArrayList<String>();
        distribution.add("Monthly");
        distribution.add("Weekly");
        distribution.add("Worked Hours");
        distribution.add("Scheduled Hours");
        distribution.add("Lump Sum");
        distribution.add("Specified Date");
        distribution.add("None");
        return distribution;
    }

    public ArrayList<String> requestRules() {
        ArrayList<String> requestRules = new ArrayList<String>();
        requestRules.add("Employee can request?");
        requestRules.add("Manager can request ?");
        requestRules.add("Employee can request partial day?");
        requestRules.add("Manager can submit in timesheet?");
        requestRules.add("WeeWeekly limits (hours)");
        requestRules.add("Days request must be made in advance");
        requestRules.add("Select default day time type");
        requestRules.add("Configure all day time off default");
        requestRules.add("Define number of hours eligible for paid time off by using");
        requestRules.add("Days an employee can request at one time");
        requestRules.add("Auto reject time off which exceed accrued hours ?");
        requestRules.add("Allow Paid Time Off to compute to overtime ?");
        requestRules.add("Does this time off reason track Accruals ?");
        requestRules.add("Max hours in advance of what you earn");
        requestRules.add("Enforce Yearly Limits");
        requestRules.add("Probation Period");
        requestRules.add("Annual Use Limit");
        return requestRules;
    }

    public ArrayList<String> probationUnit() {
        ArrayList<String> proUnit = new ArrayList<String>();
        proUnit.add("Days");
        proUnit.add("Months");
        proUnit.add("Hours Worked");
        return proUnit;
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Accruals Template Association")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)//blocked by: https://legiontech.atlassian.net/browse/OPS-5396
    public void verifyAccrualTemplateAssociationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();
        //delete the failed template
        //search template and archive
        absentManagePage.search("AutoTest_Accrual");
        int num = absentManagePage.templateNumber();
        for (int i = 0; i < num; i++) {
            absentManagePage.deleteOrArchiveTemplate("AutoTest_Accrual");
        }
        SimpleUtils.pass("Succeeded in editing or archiving template!");
        //Create a new template
        Random random = new Random();
        String tempName = "AutoTest_Accrual" + random.nextInt(10000);
        absentManagePage.createANewTemplate(tempName, "accrual test");
        absentManagePage.submit();

        //1.associate page title:
        absentManagePage.switchToAssociation();
        String associationTitle = absentManagePage.getTemplateAssociationTitle();
        Assert.assertEquals(associationTitle, "Dynamic Employee Groups", "Failed to switch to associate page!");
        SimpleUtils.pass("Succeeded in switching to associate page and getting the association Title!");
        //2.paging/page turning
        Assert.assertEquals(absentManagePage.getCurrentPage(), "1", "Failed to assert now is in the first page！");
        SimpleUtils.pass("Succeeded in validating now is in the first page！");
        absentManagePage.goToNextPage();
        Assert.assertEquals(absentManagePage.getCurrentPage(), "2", "Failed to assert it go to the next page！");
        SimpleUtils.pass("Succeeded in going to the next page！");
        absentManagePage.goToPreviousPage();
        Assert.assertEquals(absentManagePage.getCurrentPage(), "1", "Failed to assert it go to the previous page！");
        SimpleUtils.pass("Succeeded in going to the previous page！");
        //3.search works well
        //3.1 search by partial name
        String groupName = "3980";
        absentManagePage.searchDynamicGroup(groupName);
        Assert.assertTrue(absentManagePage.getDynamicEmployeeGroupName().contains(groupName), "Failed to search by group name!");
        SimpleUtils.pass("Succeeded in validating search by group name！");

        //3.2 search by description
        String groupDesc = "Don't touch(AutoUsed )";
        absentManagePage.searchDynamicGroup(groupDesc);
        Assert.assertTrue(absentManagePage.getDynamicEmployeeGroupDesc().equalsIgnoreCase(groupDesc), "Failed to search by group description!");
        SimpleUtils.pass("Succeeded in validating search by group description！");
        //4.label filter works well
        String groupLabel = "accrualLabelTest";
        absentManagePage.searchDynamicGroup(groupLabel);
        Boolean flag = true;
        for (int i = 0; i < absentManagePage.getDynamicEmployeeGroupLabs().size(); i++) {
            if (absentManagePage.getDynamicEmployeeGroupLabs().get(i).toString().contains(groupLabel)) {
            } else {
                flag = false;
                break;
            }
        }
        Assert.assertTrue(flag, "Failed to search by group label!");
        SimpleUtils.pass("Succeeded in validating filter by group label！");
        //5.back button
        absentManagePage.isBackButtonDisplayed();
        Assert.assertTrue(absentManagePage.isBackButtonDisplayed(), "Failed to assert the back button displayed!");
        SimpleUtils.pass("Succeeded in validating the back button is displayed!");
        absentManagePage.back();
        Assert.assertEquals(absentManagePage.getTemplateListLabel(), "Review and configure the list of time off management templates. Add and take an action.", "Failed to back to template list page!");
        SimpleUtils.pass("Succeeded in validating click back button can back to the template list page!");
        //6.cancel button
        absentManagePage.configureTemplate(tempName);
        absentManagePage.associateTemplate("AutoAssociateTest");
        absentManagePage.cancelAssociation();
        Assert.assertEquals(absentManagePage.getTemplateListLabel(), "Review and configure the list of time off management templates. Add and take an action.", "Failed to back to template list page!");
        SimpleUtils.pass("Succeeded in validating click cancel button can back to the template list page!");
        //7.save
        absentManagePage.configureTemplate(tempName);
        absentManagePage.associateTemplate("AutoAssociateTest");
        absentManagePage.saveAssociation();
        //4.View button
        absentManagePage.viewEmployeeGroup();
        Assert.assertEquals(absentManagePage.getViewModalTitle(), "Manage Dynamic Employee Group", "Failed to view dynamic employee group details!");
        SimpleUtils.pass("Succeeded in validating view button displayed and it is clickable！");
        //2 users associated to the template
//        Assert.assertEquals(absentManagePage.getAssociations(), "16 User matching", "Failed to get the associated user number!");
//        SimpleUtils.pass("Succeeded in validating users were associated to the target template！");
        OpsCommonComponents components = new OpsCommonComponents();
        components.okToActionInModal(false);
        //switch to details page
        absentManagePage.switchToDetails();
        //configure
        absentManagePage.setTemplateLeverCanRequest(true);
        absentManagePage.setTemplateLeverWeeklyLimits("40");
        absentManagePage.configureTimeOffRules("Sick");
        TimeOffReasonConfigurationPage configurationPage = new TimeOffReasonConfigurationPage();
        configurationPage.setTimeOffRules(true, true, true, "10", "2", "6", "2", true, true, true, "8", false, "1095", "Days", "9999");
        //configurationPage.addSpecifiedServiceLever(0, "12", "3", "15");
        configurationPage.saveTimeOffConfiguration(true);
        absentManagePage.saveTemplateAs("Publish now");
        SimpleUtils.pass("Succeeded in publishing template: " + tempName + " !");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "OPS-6030 Add pagination and search support for Time Off Reasons and Accrual Promotions in timeoff managemant template setting")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTimeOffReasonPromotionSearchPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToEmployeeManagement();
        EmployeeManagementPanelPage panelPage = new EmployeeManagementPanelPage();
        panelPage.goToTimeOffManagementPage();
        AbsentManagePage absentManagePage = new AbsentManagePage();

        absentManagePage.switchToSettings();

        absentManagePage.verifyTimeOffPage();
        absentManagePage.verifyPromotionPage();

        Boolean timeOffReasonFlag, promotionFlag;
        timeOffReasonFlag = absentManagePage.searchTimeOffReason("we");
        if(timeOffReasonFlag == false){
            SimpleUtils.pass("Non exist time off reason search failed");
        }else
            SimpleUtils.fail("Non exist time off reason search successfully",false);
        timeOffReasonFlag = absentManagePage.searchTimeOffReason("Annual Leave");
        if(timeOffReasonFlag == true){
            SimpleUtils.pass("Exist time off reason search successfully");
        }else
            SimpleUtils.fail("Exist time off reason search failed",false);
        promotionFlag = absentManagePage.searchPromotion("we");
        if(promotionFlag == false){
            SimpleUtils.pass("Non exist promotion search failed");
        }else
            SimpleUtils.fail("Non exist promotion search successfully",false);
        promotionFlag = absentManagePage.searchPromotion("promotion");
        if(promotionFlag == false){
            SimpleUtils.pass("Exist promotion search successfully");
        }else
            SimpleUtils.fail("Exist promotion search failed",false);
    }
}
