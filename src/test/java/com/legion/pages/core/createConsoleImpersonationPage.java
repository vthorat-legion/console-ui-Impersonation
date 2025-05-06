package com.legion.pages.core;

import com.aventstack.extentreports.Status;
import com.legion.pages.BasePage;
import com.legion.pages.ImpersonationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.SchedulePage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

import static com.legion.utils.MyThreadLocal.*;
import static com.legion.utils.MyThreadLocal.getLocationName;
import static com.legion.utils.SimpleUtils.getImpersonationData;

public class createConsoleImpersonationPage extends BasePage implements ImpersonationPage {

    public createConsoleImpersonationPage() {
        PageFactory.initElements(getDriver(), this);
    }

    //Control Center Part

    @FindBy(css = "div.console-navigation-item-label.Controls")
    private WebElement controlsConsoleName;
    @FindBy(css = ".lg-new-location-chooser__global.ng-scope")
    private WebElement globalIconControls;
    @FindBy(css = ".center.ng-scope")
    private WebElement controlsPage;
    @FindBy(css = "div[title='User Management']")
    private WebElement userManagement;
    @FindBy(css = "lg-dashboard-card[title='Users and Roles']")
    private WebElement userAndRoles;
    @FindBy(xpath= "//input[@placeholder='You can search by name, job title, location, etc.']")
    private WebElement usersSearchBox;
    @FindBy(xpath = "//lg-button[@ui-sref='^.userdetails({id: user.worker.memberId})']")
    private WebElement userprofile;
    @FindBy(xpath = "//span[contains(text(),'Impersonate')]")
    private WebElement impersonationbtn;
    @FindBy(xpath = "//button[normalize-space()='Confirm']")
    private WebElement confirmPopup;
    @FindBy(xpath = "//a[normalize-space()='End Session']")
    private WebElement ImpersonateEndSession;
    @FindBy(css = "h2[class='ng-binding']")
    private WebElement ImpersonateBanner;
    @FindBy(css = "div[title='Configuration']")

    //Template
    private WebElement configuration;
    @FindBy(css = "lg-dashboard-card[title='Scheduling Policies'] p[class='ng-binding']")
    private WebElement schedulingPolicies;
    @FindBy(css = "button[id='legion_cc_new_template_btn'] span[class='ng-binding ng-scope']")
    private WebElement newTemplate;
    @FindBy(xpath = "//input[@placeholder='E.g. New Template']")
    private WebElement enterTempName;
    @FindBy(xpath = "//textarea[@placeholder='Some description about this template']")
    private WebElement enterTempDescription;
    @FindBy(xpath = "//span[contains(text(),'Continue')]")
    private WebElement continueCreateNewTemp;

    //Association
    @FindBy(css = "#legion_cc_template_association_tab")
    private WebElement associationTab;
    @FindBy(css = "button[id='legion_cc_association_edit_btn'] span[class='ng-binding ng-scope']")
    private WebElement editAssociationTab;
    @FindBy(xpath = "//button[@type='button']//img[@class='ng-scope']")
    private WebElement plusBtnAssociation;
    @FindBy(xpath = "//input[@placeholder='Eg. Groupy']")
    private WebElement dynamicGroupName;
    @FindBy(css= "lg-cascade-select[placeholder='Select one']")
    private WebElement criteria;
    @FindBy(xpath = "//div[@title='Location Name']")
    private WebElement criteriaByLocationName;
    @FindBy(xpath = "//ng-form[@class='input-form ng-pristine ng-invalid ng-invalid-required ng-valid-pattern ng-valid-maxlength']")
    private WebElement selectLocationTab;
    @FindBy(xpath = "(//input[@placeholder='Search'])[1]")
    private WebElement selectLocationSearchBox;
    @FindBy(xpath = "//input[@type='checkbox']")
    private WebElement checkbox;
    @FindBy(xpath = "//span[contains(text(),'OK')]")
    private WebElement checkboxOK;
    @FindBy(xpath = "//div[@class='lg-modal']")
    private WebElement leavePagePopup;
    @FindBy(xpath = "//span[contains(text(),'Leave this page')]")
    private WebElement selectLeaveThisPage;

    @FindBy(xpath = "//input[@placeholder='You can search by name and description']")
    private WebElement searchAssociationTab;
    @FindBy(xpath = "//td[@title='Impersonation']/preceding-sibling::td/input[@type='radio']")
    private WebElement selectAssociationRadioBtn;
    @FindBy(xpath = "//span[contains(text(),'Save')]")
    private WebElement saveAssociation;
    @FindBy(css = "#legion_cc_template_detail_back")
    private WebElement backToTemplate;

    @FindBy(xpath = "//div[@id='legion_cc_template_details_tab']")
    private WebElement detailsTab;
    @FindBy(xpath = "//button[@class='saveas-drop ng-scope']")
    private WebElement tempDropdownBtn;
    @FindBy(xpath = "//h3[@id='legion_cc_save_as_draft_btn']")
    private WebElement saveAsDraftH3;
    @FindBy(xpath = "//h3[@id='legion_cc_publish_now_btn']")
    private WebElement publishNowH3;
    @FindBy(xpath = "//span[contains(text(),'Save as draft')]")
    private WebElement SaveDraftFinal;
    @FindBy(xpath = "//span[contains(text(),'Publish now')]")
    private WebElement publishNowFinal;
    @FindBy(css = "#legion_cc_template_list_search")
    private WebElement searchTemplate;
    @FindBy(css = "lg-button[class='name ng-isolate-scope'] span[class='ng-binding ng-scope']")
    private WebElement selectTemplate;
    @FindBy(css = "button[id='legion_cc_template_history_btn'] span[class='ng-binding ng-scope']")
    private WebElement historyBtn;
    @FindBy(css = "#logContainer")
    private WebElement historyLog;
    @FindBy(css = ".lg-slider-pop__title-dismiss")
    private WebElement historyLogClose;
    @FindBy(css = ".session")
    private List<WebElement> commentHistory;
    @FindBy(css = ".templateInfo")
    private List<WebElement> templateInfoList;
    @FindBy(xpath = "//ul[@class='session']//li[@class='new-audit-log']//div[@class='logInfoContainer']//p[not(contains(@class, 'changes-summary'))]")
    private List<WebElement> logEntrytext;

    @FindBy(css = "button[id='legion_cc_template_delete_btn'] span[class='ng-binding ng-scope']")
    private WebElement deleteTemplateBtn;
    @FindBy(css = "lg-button[label='OK'] span[class='ng-binding ng-scope']")
    private WebElement deleteTemplateOKBtn;
    @FindBy(css = "#legion_cc_template_archive_btn")
    private WebElement archivePublishedTempBtn;
    @FindBy(css = "lg-button[label='Remove'] span[class='ng-binding ng-scope']")
    private WebElement removeAssociationBtn;
    @FindBy(css = "lg-button[ng-if='$ctrl.secondLabel'] span[class='ng-binding ng-scope']")
    private WebElement removeAssociationCnfrBtn;
    @FindBy(css = "button[id='legion_cc_template_edit_btn'] span[class='ng-binding ng-scope']")
    private WebElement editTemplateBtn;
    @FindBy(css = "lg-button[label='OK'] span[class='ng-binding ng-scope']")
    private WebElement editTemplateOKBtn;


    //Legion Profile
    @FindBy(css = "#legion_Profile_button")
    private WebElement avatar;
    @FindBy(css = "div[class='header-user-switch-menu-item-new ng-binding']")
    private WebElement legionProfile;
    @FindBy(css = ".sc-iidyiZ.hOWWNs")
    private WebElement legionProfileEditbtn;
    @FindBy(css = ".header-current-engagement-title.ng-binding")
    private WebElement switchToForac;
    @FindBy(xpath = "(//div[@class='sc-fBfsKi cKjuMB'])[1]")
    private WebElement impersonatedUsername;
    @FindBy(css = ".tooltip-inner")
    private WebElement impersonateTooltip;
    @FindBy(css = "lg-button[label='Impersonate']")
    private WebElement impersonateWrapper;

    //Published Template
    @FindBy(css = "lg-eg-status[type='Published']")
    private WebElement tempStatusPublished;
    @FindBy(css = ".fa.fa-caret-right")
    private WebElement publishDraftTempArrow;
    @FindBy(css = "lg-button[class='ml-10 name ng-isolate-scope'] span[class='ng-binding ng-scope']")
    private WebElement publishDraftTemp;

    //SwitchManagerEmployeeViewPage
    @FindBy(css = ".header-user-switch-menu-item-new.ng-binding.ng-scope[ng-click='switchView()']")
    private WebElement switchToView;
    @FindBy(css = "lg-button[class='ml-10 name ng-isolate-scope'] span[class='ng-binding ng-scope']")
    private WebElement backToManagerView;

    String associationLocation = ((Map<String, String>) getImpersonationData().get("template")).get("associationLocation");

        @Override
    public void createNewTemplate(String TemplateName) throws Exception {
        if (isElementEnabled(configuration, 5)) {
            click(configuration);
            click(schedulingPolicies);
            searchTemplate.sendKeys(TemplateName);
        } else {
            SimpleUtils.fail("Configuration element is not enabled", false);
        }
        if (isElementEnabled(selectTemplate, 15)) {
            handleAssociation(TemplateName);
            publishTemplate(TemplateName);
            createDraftTemplate(TemplateName);
            deleteDraftTempAndAssociation(TemplateName);
        } else {
            createTemplate(TemplateName);
            handleAssociation(TemplateName);
            publishTemplate(TemplateName);
            createDraftTemplate(TemplateName);
            deleteDraftTempAndAssociation(TemplateName);
        }
    }

    private void handleAssociation(String TemplateName) throws InterruptedException {
        if (isElementEnabled(associationTab, 5)) {
            click(associationTab);
            if (isElementEnabled(editAssociationTab, 5)) {
                click(editAssociationTab);
                searchAssociationTab.sendKeys(TemplateName);

                if (isElementEnabled(selectAssociationRadioBtn, 10)) {
                    click(selectAssociationRadioBtn);
                    click(saveAssociation);
                } else {
                    createNewAssociation(TemplateName);
                }
            } else {
                SimpleUtils.fail("Edit Association Tab is not enabled", false);
            }
        } else {
            SimpleUtils.fail("Association Tab is not enabled", false);
        }
    }

    private void createTemplate(String TemplateName) throws Exception {
        click(newTemplate);
        if (isElementLoaded(enterTempName)) {
            enterTempName.sendKeys(TemplateName);
            enterTempDescription.sendKeys(TemplateName);
            click(continueCreateNewTemp);
            click(backToTemplate);
            waitForSeconds(5);
            searchTemplateAndSelect(TemplateName);
        } else {
            SimpleUtils.fail("Template name input field is not loaded", false);
        }
    }

    private void createDraftTemplate(String TemplateName) throws Exception {
            searchTemplateAndSelect(TemplateName);
            click(editTemplateBtn);
            click(editTemplateOKBtn);
            click(tempDropdownBtn);
            click(saveAsDraftH3);
            click(SaveDraftFinal);
            waitForSeconds(5);
        if (isElementEnabled(searchTemplate, 10)) {
            searchTemplate.sendKeys(TemplateName);
            click(publishDraftTempArrow);
            click(publishDraftTemp);
        } else {
            SimpleUtils.fail("Template name input field is not loaded", false);
        }
    }

    private void deleteDraftTempAndAssociation(String TemplateName) throws Exception {
        if (isElementEnabled(deleteTemplateBtn, 5)) {
        click(deleteTemplateBtn);
        click(deleteTemplateOKBtn);
        searchTemplateAndSelect(TemplateName);
            click(associationTab);
            searchAssociationTab.sendKeys(TemplateName);
            click(editAssociationTab);
            click(removeAssociationBtn);
            click(removeAssociationCnfrBtn);
            click(detailsTab);
        } else {
            SimpleUtils.fail("Template name input field is not loaded", false);
        }
    }
    private void deletePublishedTemplate(String TemplateName) throws Exception {
        if (isElementEnabled(archivePublishedTempBtn, 10)) {
            click(archivePublishedTempBtn);
            click(deleteTemplateOKBtn);
            if (isElementEnabled(leavePagePopup, 10)) {
                click(selectLeaveThisPage);
            }
            else{
                System.out.println("Leave page popup not populated");
            }
            waitForSeconds(3);
        }else {
            SimpleUtils.fail("Template Archive Button Not Found", false);
        }

        }

    private void createNewAssociation(String TemplateName) throws InterruptedException {
        if (isElementEnabled(plusBtnAssociation, 10)) {
            click(plusBtnAssociation);
            waitForSeconds(3);
            dynamicGroupName.sendKeys(TemplateName);
            waitForSeconds(3);
            click(criteria);
            waitForSeconds(3);
            click(criteriaByLocationName);
            waitForSeconds(3);
            click(selectLocationTab);
            waitForSeconds(3);
            selectLocationSearchBox.sendKeys(associationLocation);
            click(checkbox);
            click(checkboxOK);
            click(selectAssociationRadioBtn);
            click(saveAssociation);
            click(backToTemplate);
            waitForSeconds(5);
            searchTemplateAndSelect(TemplateName);
        } else {
            SimpleUtils.fail("Plus button for creating association is not enabled", false);
        }
    }

    private void searchTemplateAndSelect(String TemplateName) throws InterruptedException {
        if (isElementEnabled(searchTemplate, 10)) {
            searchTemplate.sendKeys(TemplateName);
            click(selectTemplate);
        } else {
            SimpleUtils.fail("Template not found: " + TemplateName, false);
        }
    }

    private void publishTemplate(String TemplateName) throws InterruptedException {
        if (isElementEnabled(detailsTab, 10)) {
            click(detailsTab);
            if (isElementEnabled(editTemplateBtn, 10)) {
                click(editTemplateBtn);
                click(editTemplateOKBtn);
                waitForSeconds(5);
                click(tempDropdownBtn);
                click(publishNowH3);
                click(publishNowFinal);
            } else {
                SimpleUtils.fail("Edit Template button is not enabled", false);
            }
        } else {
            SimpleUtils.fail("Details Tab is not enabled", false);
        }
    }

    @Override
    public void checkTemplateHistory(String TemplateName) throws Exception {
//        click(selectTemplate);
        if (isElementLoaded(historyBtn, 10)) {
            click(historyBtn);
            waitForSeconds(5);
            boolean impersonatingFound = false;

            for (int i = 0; i < commentHistory.size(); i++) {
                WebElement historyEntry = commentHistory.get(i);
                List<WebElement> actionTypeElements = historyEntry.findElements(By.cssSelector(".templateInfo"));
                List<WebElement> paragraphElements = logEntrytext;

                for (int j = 0; j < actionTypeElements.size() && j < paragraphElements.size(); j++) {
                    String actionType = actionTypeElements.get(j).getText().trim();
                    String entryText = paragraphElements.get(j).getText().trim();
                    if (entryText.toLowerCase().contains("impersonating")) {
                        SimpleUtils.pass("Template History Shows Impersonator Details: "
                                + actionType + " - " + entryText);
                        impersonatingFound = true;
                    }
                }
            }
            if (!impersonatingFound) {
                SimpleUtils.fail("Template history does not show impersonator details.", true);
            }
            click(historyLogClose);
            deletePublishedTemplate(TemplateName);
        } else {
            SimpleUtils.fail("Template history not loaded.", false);
        }
    }

    @Override
    public void checkAcceptedToSAndImpersonateUser(String Username) throws Exception {
        click(userManagement);
        click(userAndRoles);
        waitForSeconds(10);
        if (isElementLoaded(usersSearchBox, 10)) {
            moveToElementClickandSend(usersSearchBox, Username);
            waitForSeconds(5);
            click(userprofile);
            waitForSeconds(5);
            scrollToElement(impersonateWrapper);
            mouseToElement(impersonateWrapper);
            boolean isButtonDisabled = isButtonVisiblyDisabled(impersonationbtn);
            if (isButtonDisabled) {
                String tooltipText = impersonateTooltip.getText();
                SimpleUtils.pass("The Impersonate Button Is Disabled For: "+ Username +" Tooltip: " + tooltipText);
            } else {
                SimpleUtils.pass("The Impersonate Button Is Visible And Enabled For: "+ Username );
                click(impersonationbtn);
                waitForSeconds(5);
                click(confirmPopup);
                waitForSeconds(10);
                String banner = ImpersonateBanner.getText();
                click(avatar);
                click(legionProfile);
                String ImpersonatedUsername = impersonatedUsername.getText().replace("Name", "").trim();
                String expected = "You are currently logged in as " + ImpersonatedUsername;
                click(switchToForac);
                waitForSeconds(2);
                if (banner.equals(expected)) {
                    SimpleUtils.pass("Successfully Impersonated User With Banner = <b>" + banner + "</b>");
                } else {
                    SimpleUtils.fail("Impersonation failed. Banner text does not match. Expected: '"
                            + expected + "' but got: '" + banner + "'", false);
                }
            }
        }
    }
    private boolean isButtonVisiblyDisabled(WebElement button) {
        try {
            String opacity = button.getCssValue("opacity");
            String pointerEvents = button.getCssValue("pointer-events");
            // Button is visually disabled if opacity is 0 or pointer-events is none
            return "0".equals(opacity) || "none".equals(pointerEvents);
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void goToLegionProfile() throws Exception {
        click(avatar);
        click(legionProfile);
            waitForSeconds(5);
        if (isElementLoaded(legionProfileEditbtn, 10)) {
            SimpleUtils.fail("Legion Profile Edit Button Is Visible", false);
        }
        else {
            click(switchToForac);
            SimpleUtils.pass("Legion Profile Edit Button Is Disabled");
        }
    }

    @Override
    public void switchToEmployeeView() throws Exception {
        try {
            click(avatar);
            click(switchToView);
            SimpleUtils.pass("Successfully switched to Employee View");
        } catch (Exception e) {
            SimpleUtils.fail("Failed to switch to Employee View: " + e.getMessage(), false);
            throw e;  // Rethrow exception after logging failure
        }
    }
    @Override
    public void switchBackToManagerView() throws Exception {
        try {
            click(avatar);
            click(switchToView);
            click(avatar);
            click(switchToView);
            SimpleUtils.pass("Successfully switched back to Manager View");
        } catch (Exception e) {
            SimpleUtils.fail("Failed to switch back to Manager View: " + e.getMessage(), false);
            throw e;
        }
    }

    @Override
    public void confirmSessionEnds() throws Exception {
        try {
            click(avatar);
            if (!isElementLoaded(switchToView, 10)) {
                SimpleUtils.pass("Successfully Ended Session For Manager and Impersonator Session Started.");
            } else {
                SimpleUtils.fail("Switch-to-view is still available. Session might not have ended properly", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail("Failed to confirm session end: " + e.getMessage(), false);
            throw e;
        }

    }

    @Override
    public void gotoControlsPage() throws Exception {
        LocationsPage locationsPage = new OpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.pass("Navigated OpsPortal/Control Center Page");
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

    }

    @Override
    public void endImpersonationSession () throws Exception {
        if (isElementLoaded(ImpersonateEndSession, 10)) {
            click(ImpersonateEndSession);
            waitForSeconds(20);
            SimpleUtils.pass("Successfully End session of Impersonated user");
        }
        else {
            SimpleUtils.pass("End Session Button Not Visible");
        }
    }
     
    public void moveToElementClickandSend(WebElement element, String username) {
        try {
            waitUntilElementIsVisible(element);
            Actions actions = new Actions(getDriver());
            actions.moveToElement(element).click().sendKeys(username).sendKeys(Keys.ENTER).perform();

        } catch (TimeoutException te) {
            ExtentTestManager.getTest().log(Status.WARNING, te);
        }
    }
}

