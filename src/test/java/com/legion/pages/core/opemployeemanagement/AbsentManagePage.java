package com.legion.pages.core.opemployeemanagement;

import com.legion.pages.BasePage;
import com.legion.utils.Constants;
import com.legion.utils.HttpUtil;
import com.legion.utils.SimpleUtils;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.legion.utils.MyThreadLocal.driver;
import static com.legion.utils.MyThreadLocal.getDriver;

public class AbsentManagePage extends BasePage {
    public AbsentManagePage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading>div>a")
    private WebElement backButton;

    //templates
    @FindBy(css = "div.lg-tabs>nav>div:nth-child(1)")
    private WebElement templatesTab;
    //settings
    @FindBy(css = "div.lg-tabs>nav>div:nth-child(2)")
    private WebElement settingsTab;

    //new template
    @FindBy(css = "lg-button[label='New Template']>button")
    private WebElement newTemplateButton;
    @FindBy(css = "div.lg-modal>h1.lg-modal__title")
    private WebElement newTemplateModalTitle;
    @FindBy(css = "input-field[label='Name this template'] input")
    private WebElement templateName;
    @FindBy(css = "input-field[label='Description'] textarea")
    private WebElement templateDesc;
    @FindBy(css = "lg-button[label='Cancel']>button")
    private WebElement cancelCreatingTemp;
    @FindBy(css = "lg-button[label='Continue']>button")
    private WebElement continueCreatingTemp;
    @FindBy(css = "div.lg-templates-table-improved__grid-row--header>div span")
    private List<WebElement> templateTableHeaders;
    @FindBy(css = "lg-search>input-field input")
    private WebElement templateSearchBox;
    @FindBy(css = "div.lg-search-icon.ng-scope")
    private WebElement searchIcon;
    @FindBy(css = "div.lg-templates-table-improved__grid-row.ng-scope>div>lg-button.name")
    private List<WebElement> templateNameOfSearchResult;
    @FindBy(css = "table.lg-table.ng-scope>tbody>tr:first-child>td:nth-child(4)")
    private List<WebElement> creatorOfSearchResult;
    @FindBy(css = "div.default.ng-binding.ng-scope")
    private WebElement defaultLabel;
    //welcome modal
    @FindBy(css = "div.wm-close-button.walkme-x-button")
    private WebElement welComeModalCloseButton;

    //smart card
    @FindBy(css = "div.card-carousel-container card-carousel-card div.card-carousel-card-title")
    private List<WebElement> smartCardTitle;
    @FindBy(css = "span.card-carousel-link.ng-binding.ng-scope")
    private List<WebElement> smartCardLink;
    @FindBy(css = "lg-eg-status")
    private List<WebElement> templateStatus;

    @FindBy(css = "i.fa.fa-caret-right")
    private WebElement caretRight;
    @FindBy(css = "i.fa.fa-caret-down")
    private WebElement caretDown;
    @FindBy(css = "div.lg-templates-table-improved__grid-row.child-row>div>lg-button")
    private WebElement theChildTemplate;

    @FindBy(css = "div.lg-paged-search h4")
    private WebElement noMatchMessage;

    //template details
    @FindBy(css = "lg-button[label='History']>button")
    private WebElement historyButton;
    //div.lg-slider-pop>h1
    @FindBy(css = "div.lg-slider-pop>h1>div:nth-child(2)")
    private WebElement historyCloseButton;
    @FindBy(css = "div.lg-slider-pop>div.lg-slider-pop__content li:last-child")
    private WebElement createdRecordInHistory;
    @FindBy(css = "form-buttons lg-button[label='Archive']>button")
    private WebElement archiveButton;
    @FindBy(css = "lg-button[label='Delete']>button")
    private WebElement deleteButton;
    @FindBy(css = "lg-search[placeholder='You can search by time off reason name']>input-field input")
    private WebElement timeOffSearchBox;
    @FindBy(css = "lg-paged-search[placeholder=\"Search by time off reason name\"]>div>div>lg-pagination>div>div:nth-child(3)")
    private WebElement TimeOffReasonRightArrow;


    //delete modal
    @FindBy(css = "modal div.model-content")
    private WebElement deleteConfirmMessage;

    @FindBy(css = "lg-button[label='Edit']>button")
    private WebElement editTemplate;
    //edit template modal
    @FindBy(css = "div.templateName>input-field input")
    private WebElement tempName;
    @FindBy(css = "div.mt-10.description>input-field input")
    private WebElement tempDesc;
    @FindBy(css = "input-field[label='Edit Notes'] textarea")
    private WebElement tempNotes;
    @FindBy(css = "form-buttons lg-button[label='OK']>button")
    private WebElement okToEditing;
    @FindBy(css = "form-buttons lg-button[label='Cancel']>button")
    private WebElement cancelEditing;
    @FindBy(css = "div.title-breadcrumbs>h1")
    private WebElement titleBreadCrumb;


    @FindBy(css = "div.lg-tabs>nav>div:nth-child(1)")
    private WebElement detailsTab;
    @FindBy(css = "div.lg-tabs>nav>div:nth-child(2)")
    private WebElement associationTab;
    @FindBy(css = "input-field[label='Mark this as default template'] input")
    private WebElement markAsDefaultTemplate;
    @FindBy(css = "div.model-content.ng-scope")
    private WebElement markAsDefaultTempConfirmMes;

    //template lever
    @FindBy(css = "question-input[question-title='Can employees request time off ?'] h3")
    private WebElement canEmployeeRequestLabel;
    @FindBy(css = "yes-no lg-button-group>div>div.lg-button-group-first")
    private WebElement templateLeverCanRequestYes;
    @FindBy(css = "yes-no lg-button-group>div>div.lg-button-group-last")
    private WebElement templateLeverCanRequestNo;
    @FindBy(css = "question-input[question-title='Weekly limit (Time Off + Hours Worked)'] h3")
    private WebElement weeklyLimitLabel;
    @FindBy(css = "question-input[question-title='Weekly limit (Time Off + Hours Worked)'] input-field input")
    private WebElement templateLeverWeeklyLimitInput;
    @FindBy(css = "question-input[question-title='Weekly limit (Time Off + Hours Worked)'] div.input-faked")
    private WebElement weeklyLimitHrs;

    @FindBy(css = "table.lg-table.ng-scope>tbody>tr>td:nth-child(1)")
    private List<WebElement> timeOffReasonsInTemplateDetail;
    @FindBy(css = "tbody lg-button[label='View']")
    private WebElement view;
    @FindBy(css = "tbody lg-button[label='Edit']")
    private WebElement edit;
    @FindBy(css = "tbody lg-button[label='Remove']")
    private WebElement remove;
    @FindBy(css = "tbody lg-button[label='Configure']")
    private WebElement configure;
    @FindBy(css = "tbody lg-button[label='Not Configured']>button")
    private WebElement notConfigured;
    @FindBy(css = "lg-button[label='Cancel']>button")
    private WebElement cancelEditingTemplate;

    //template save
    @FindBy(css = "button.saveas-drop")
    private WebElement saveAsDrop;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(1)")
    private WebElement saveAsDraft;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(2)")
    private WebElement publishNow;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(3)")
    private WebElement publishLater;
    //modal
    @FindBy(css = "div.lg-modal")
    private WebElement dateOfPublishModal;
    @FindBy(css = "div.modal-dialog ng-form.input-form.ng-pristine")
    private WebElement dateSelect;
    @FindBy(css = "div.lg-single-calendar-toolbar>a:last-child")
    private WebElement nextMonthArrow;
    @FindBy(css = "div.lg-single-calendar-date-wrapper>div:nth-child(20)")
    private WebElement dayChosen;
    //
    @FindBy(css = "button.pre-saveas")
    private WebElement saveTemplate;

    //template association
    @FindBy(css = "div.templateAssociation_title>span")
    private WebElement templateAssociationTitle;
    @FindBy(css = "table.lg-table.templateAssociation_table.ng-scope>tbody>tr:nth-child(2)>td>input")
    private WebElement theFirstAssociateGroup;
    @FindBy(css = "lg-button[label='Save']")
    private WebElement saveAssociate;
    @FindBy(css = "lg-search[placeholder='You can search by name, label and description']>input-field input")
    private WebElement associateSearch;
    @FindBy(css = "lg-templates-table h3")
    private WebElement templateListLabel;//Review and configure the list of time off management templates. Add and take an action.
    @FindBy(css = "lg-tab[tab-title='Association'] div.lg-tab-toolbar__content div.lg-pagination__arrow--left")
    private WebElement previousPage;
    @FindBy(css = "lg-tab[tab-title='Association'] div.lg-tab-toolbar__content div.lg-pagination__arrow--right")
    private WebElement nextPage;
    @FindBy(css = "lg-tab[tab-title='Association'] div.lg-tab-toolbar__content div.lg-pagination__pages select>option[selected='selected']")
    private WebElement currentPage;
    @FindBy(css = "table.lg-table.templateAssociation_table tr:nth-child(2)>td:nth-child(2)")
    private WebElement dynamicEmployeeGroupName;
    @FindBy(css = "table.lg-table.templateAssociation_table tr:nth-child(2)>td:nth-child(3)")
    private WebElement dynamicEmployeeGroupDesc;
    @FindBy(css = "table.lg-table.templateAssociation_table tr>td:nth-child(4)")
    private List<WebElement> dynamicEmployeeGroupLabel;
    @FindBy(css = "table.lg-table.templateAssociation_table tr:nth-child(2)>td:nth-child(5) lg-button[label='View'] button")
    private WebElement viewBtnInAssociate;
    @FindBy(css = "modal[modal-title='Manage Dynamic Employee Group'] h1")
    private WebElement viewModalTitle;
    @FindBy(css = "div.mappingLocation>lg-button[label='Test']>button")
    private WebElement viewModalTestButton;
    @FindBy(css = "div.mappingLocation>span")
    private WebElement viewModalTestResult;
    @FindBy(css = "lg-tab[tab-title='Association'] lg-button[label='Cancel']>button")
    private WebElement cancelBtnInAssociate;
    @FindBy(css = "modal[modal-title='Cancel Editing?'] lg-button[label='Yes']>button")
    private WebElement yesToCancel;
    @FindBy(css = "lg-tab[tab-title='Association'] lg-button[label='Save']>button")
    private WebElement saveBtnInAssociate;
    @FindBy(css = "lg-button[label='Leave this page']>button")
    private WebElement leavePageBtn;

    //setting page
    @FindBy(css = "div.basic-setting-info>question-input[question-title='Do time off reasons use accruals?']")
    private WebElement useAccrual;
    @FindBy(css = "div.basic-setting-info>question-input:nth-child(1)>div lg-switch span")
    private WebElement useAccrualToggle;
    @FindBy(css = "div.basic-setting-info>question-input:nth-child(2)>div lg-switch span")
    private WebElement timesheetRequiredToBeApprovedToggle;
    @FindBy(css = "div.basic-setting-info>question-input:nth-child(3)>div input-field input")
    private WebElement lookBackDays;
    @FindBy(css = "lg-switch>label.switch span")
    private WebElement toggleSlide;
    @FindBy(css = "lg-button[ng-click=\"$ctrl.createTimeOffReason('create')\"]>button")
    private WebElement addTimeOffButton;
    @FindBy(css = "div.lg-modal>h1")
    private WebElement CreateNewTimeOffModalTitle;
    @FindBy(css = "input-field[label='Reason name'] input")
    private WebElement reasonName;
    @FindBy(css = "input-field[label='Reason name']>ng-form>lg-input-error span")
    private WebElement errorMes;
    @FindBy(css = "input-field[label='Accrual Code'] input")
    private WebElement accrualCode;
    @FindBy(css = "lg-button[label='Cancel']>button")
    private WebElement cancelCreating;
    @FindBy(css = "lg-button[label='OK']>button")
    private WebElement okCreating;

    @FindBy(css = "lg-accrual-setting table tr>td:nth-child(1)")//lg-accrual-setting>div.time-off-reason-setting>table tr>td:nth-child(1)
    private List<WebElement> timeOffReasonNames;
    @FindBy(css = "div.time-off-reason-setting table tr:last-child>td:first-child")
    private WebElement timeOffReasonAdded;
    @FindBy(css = "[ng-repeat=\"item in $ctrl.timeOffReasonSortedRows\"]>td:nth-child(3) > lg-button:nth-child(1)")
    private WebElement editTimeOff;
    @FindBy(css = "[ng-repeat=\"item in $ctrl.timeOffReasonSortedRows\"]>td:nth-child(3) > lg-button:nth-child(2)")
    private WebElement removeButton;
    @FindBy(css = "modal form p.lg-modal__content.lg-modal__text")
    private WebElement removeConfirmMes;

    //Promotion part
    @FindBy(css = "lg-accrual-setting>div:nth-child(3) h1")
    private WebElement promotionTitle;
    @FindBy(css = "lg-button[ng-click=\"$ctrl.openPromotionModal('create')\"]>button")
    private WebElement promotionRuleAddButton;
    @FindBy(css = "modal[modal-title='Create New Accrual Promotion'] h1")
    private WebElement promotionModalTitle;
    @FindBy(css = "input-field[label='Promotion Name'] input")
    private WebElement promotionNameInput;
    @FindBy(css = "lg-select[label='Criteria'] ng-form")
    private WebElement promotionCriteriaType;
    @FindBy(css = "lg-picker-input[label='Criteria'] div[title='Job Title']")
    private WebElement criteriaByJobTitle;
    @FindBy(css = "lg-picker-input[label='Criteria'] div[title='Engagement Status']")
    private WebElement criteriaByEngagementStatus;
    @FindBy(css = "lg-multiple-select[label='Before'] lg-picker-input")
    private WebElement criteriaBefore;
    @FindBy(css = "input-field[label='Before'] ng-form div")
    private WebElement multiSelectPlaceHolder;
    @FindBy(css = "lg-multiple-select lg-search input")
    private WebElement criteriaBeforeSearchInput;
    @FindBy(css = "lg-select[label='After'] lg-search input")
    private WebElement criteriaAfterSearchInput;
    @FindBy(css = "div.select-list-item input-field[label='Senior Ambassador']>ng-form")
    private WebElement jobTitleAsSeniorAmbassador;
    @FindBy(css = "div.select-list-item input-field[label='WA Ambassador']>ng-form")
    private WebElement jobTitleAsWAAmbassador;
    @FindBy(css = "lg-select[label='After'] lg-picker-input")
    private WebElement criteriaAfter;
    @FindBy(css = "lg-picker-input[label='After'] div.lg-search-options__scroller>div:nth-child(1)")
    private WebElement jobTitleAfterPromotion;
    @FindBy(css = "inline-input+div>lg-button[label='Add More']>button")
    private WebElement criteriaAddMoreButton;
    @FindBy(css = "div.promotion-action+div>lg-button[label='Add More']>button")
    private WebElement transferAddMoreButton;
    @FindBy(css = "lg-picker-input [label='Before']")
    private WebElement engagementBefore;
    @FindBy(css = "lg-picker-input[label='Before'] div[title='PartTime']")
    private WebElement partTimeBefore;
    @FindBy(css = "lg-picker-input [label='After']")
    private WebElement engagementAfter;
    @FindBy(css = "lg-picker-input[label='After'] div[title='FullTime']")
    private WebElement fullTimeAfter;
    @FindBy(css = "input-field[label='Balance before promotion']")
    private WebElement balanceBeforePromotion;
    @FindBy(css = "input-field[label='Balance after promotion']")
    private WebElement balanceAfterPromotion;
    @FindBy(css = "lg-select[label='Balance before promotion'] lg-search input")
    private WebElement balanceSearchInputB;
    @FindBy(css = "lg-select[label='Balance before promotion'] div.lg-search-options__scroller>div>div")
    private WebElement balanceSearchResultB;
    @FindBy(css = "lg-select[label='Balance after promotion'] lg-search input")
    private WebElement balanceSearchInputA;
    @FindBy(css = "lg-select[label='Balance after promotion'] div.lg-search-options__scroller>div>div")
    private WebElement balanceSearchResultA;
    @FindBy(css = "lg-paged-search[placeholder=\"Search by promotion name\"] table tr>td:nth-child(1)")
    private List<WebElement> promotionRuleNames;
    @FindBy(css = "lg-paged-search[placeholder=\"Search by promotion name\"] table>tbody>tr:nth-child(2) lg-button[label='Edit']")
    private WebElement promotionEditButton;
    @FindBy(css = "lg-paged-search[placeholder=\"Search by promotion name\"] table>tbody>tr:nth-child(2) lg-button[label='Remove']")
    private WebElement promotionRemoveButton;
    @FindBy(css = "modal[modal-title='Remove Accrual Promotion'] h1.lg-modal__title")
    private WebElement removeModalTitle;
    @FindBy(css = "modal[modal-title='Remove Accrual Promotion'] general-form p")
    private WebElement removeModalContent;

    @FindBy(css = "div.time-off-reason-setting table tr>td.one-line-overflow")
    private List<WebElement> tOffReasonsInGlobalSettingList;

    @FindBy(css = "lg-picker-input[label='Balance before promotion'] div.lg-search-options__scroller div>div")
    private List<WebElement> tOffInPromotionSelect;
    @FindBy(xpath = "(//lg-pagination/div/div[2])[4]")
    private WebElement pagination;
    //home page methods
    public boolean isBackButtonDisplayed(){
        scrollToTop();
        return isElementDisplayed(backButton);
    }
    public void back() {
        backButton.click();
        if(isElementDisplayed(leavePageBtn)){
            toLeavePage();
        }
        waitForSeconds(3);
    }

    public boolean isTemplateTabDisplayed() {
        return templatesTab.isDisplayed();
    }

    public boolean isSettingsTabDisplayed() {
        return templatesTab.isDisplayed();
    }

    public void switchToTemplates() {
        templatesTab.click();
    }

    public void switchToSettings() {
        settingsTab.click();
    }

    public boolean isNewTemplateButtonDisplayedAndEnabled() {
        try {
            if (isElementLoaded(newTemplateButton, 20) && isElementEnabled(newTemplateButton, 20)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void createANewTemplate(String tempName, String tempDesc) throws Exception {
        if (isElementLoaded(newTemplateButton, 20)) {
            newTemplateButton.click();
            if (isElementLoaded(templateName, 10) && isElementLoaded(templateDesc, 10)) {
                templateName.clear();
                templateName.sendKeys(tempName);
                templateDesc.clear();
                templateDesc.sendKeys(tempDesc);
            } else {
                SimpleUtils.fail("Absence Management Page: Click on New Template button failed!", false);
            }
        } else {
            SimpleUtils.fail("Absence Management Page: New Template button failed to load!", false);
        }
    }

    public void cancel() {
        cancelCreatingTemp.click();
    }

    @FindBy(css = ".walkme-action-destroy-1.wm-close-link")
    private WebElement closeWakeme;

    public void submit() throws Exception {
        continueCreatingTemp.click();
        if (isElementLoaded(closeWakeme, 10)) {
            click(closeWakeme);
        }
        waitForSeconds(3);
    }

    public void closeWelcomeModal() {
        welComeModalCloseButton.click();
    }

    public String getTemplateSearchBoxPlaceHolder() {
        return templateSearchBox.getAttribute("placeholder");
    }

    public ArrayList<String> getTemplateTableHeaders() {
        return getWebElementsLabels(templateTableHeaders);
    }

    public void search(String searchText) {
        templateSearchBox.clear();
        templateSearchBox.sendKeys(searchText);
        //searchIcon.click();
        waitForSeconds(3);
    }

    public void searchTimeOff(String timeOff){
        timeOffSearchBox.clear();
        timeOffSearchBox.sendKeys(timeOff);
        waitForSeconds(2);
    }

    public String noMatch() {
        return noMatchMessage.getText();
    }

    public boolean isNoMatchMessageDisplayed() {
        return isElementDisplayed(noMatchMessage);
    }

    public int templateNumber() {
        return templateNameOfSearchResult.size();
    }

    public String getResult() {
        String name = "";
        if (areListElementVisible(templateNameOfSearchResult, 10)) {
            name = templateNameOfSearchResult.get(0).getText();
        }
        return name;
    }

    public ArrayList getWebElementsLabels(List<WebElement> webElementsList) {
        ArrayList<String> labelList = new ArrayList<>();
        webElementsList.forEach((e) -> {
            labelList.add(e.getText());
        });
        return labelList;
    }

    public boolean isRelated(String text) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> list1 = new ArrayList<String>();
        boolean contain = true;
        list = getWebElementsLabels(creatorOfSearchResult);
        list1 = getWebElementsLabels(templateNameOfSearchResult);
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i) + "&" + list1.get(i);
            if (!str.toLowerCase().contains(text.toLowerCase())) {
                contain = false;
                break;
            }
        }
        return contain;
    }

    public Boolean smartCardFilter() {
        Boolean isItFiltered = true;
        try {
            if (getWebElementsLabels(smartCardTitle).get(0).equals("UPCOMING DUES")) {
                smartCardLink.get(0).click();
                for (String status : getTemplateStatus()) {
                    if (!status.equals("Pending")) {
                        isItFiltered = false;
                        break;
                    }
                }
            } else if (getWebElementsLabels(smartCardTitle).get(0).equals("PUBLISHED")) {
                smartCardLink.get(0).click();
                for (String status : getTemplateStatus()) {
                    if (!status.equals("Published")) {
                        isItFiltered = false;
                        break;
                    }
                }
            }
            waitForSeconds(3);
            smartCardLink.get(0).click();
        } catch (Exception NoSuchElementException) {
            System.out.println("No published template!");
        }
        return isItFiltered;
    }

    public ArrayList<String> getTemplateStatus() {
        ArrayList<String> status = new ArrayList<>();
        templateStatus.forEach((e) -> {
            status.add(e.getAttribute("type"));
        });
        return status;
    }

    public void caretDown() {
        caretRight.click();
    }

    public void clickInDetails() {//into edit mode
        if (isElementDisplayed(caretRight)) {
            caretDown();
            theChildTemplate.click();
        } else if (isElementDisplayed(caretDown)) {
            theChildTemplate.click();
        } else {
            templateNameOfSearchResult.get(0).click();
        }
    }

    //template details page methods
    public boolean isHistoryButtonDisplayed() {
        return isElementDisplayed(historyButton);
    }

    public void openTemplateHistory() {
        historyButton.click();
    }

    public void closeHistory() {
        historyCloseButton.click();
    }

    public String getCreatedRecord() {
        return createdRecordInHistory.getText();
    }

    public boolean isArchiveButtonDisplayed() {
        return isElementDisplayed(archiveButton);
    }

    public void archivePublishedTemplate() {
        archiveButton.click();
    }

    public boolean isDeleteButtonDisplayed() {
        return isElementDisplayed(deleteButton);
    }

    public boolean isEditButtonDisplayed() {
        return isElementDisplayed(editTemplate);
    }

    public void switchToDetails() {
        detailsTab.click();
    }

    public void switchToAssociation() {
        waitForSeconds(5);
        associationTab.click();
    }

    public void associateTemplate(String groupName) {
        switchToAssociation();
        searchDynamicGroup(groupName);
        theFirstAssociateGroup.click();
    }

    public void searchDynamicGroup(String groupName){
        associateSearch.clear();
        associateSearch.sendKeys(groupName);
        waitForSeconds(2);
    }

    public String getCanEmployeeRequestLabel() {
        return canEmployeeRequestLabel.getText();
    }

    public String getTemplateAssociationTitle() {
        return templateAssociationTitle.getText();
    }


    public void editTemplateInfo(String tpName, String tpDesc, String tpNotes) {
        editTemplate.click();
        tempName.clear();
        tempName.sendKeys(tpName);
        if (tpDesc != null) {
            tempDesc.clear();
            tempDesc.sendKeys(tpDesc);
        }
        tempNotes.clear();
        tempNotes.sendKeys(tpNotes);
    }

    public String getTemplateTitle() {
        return titleBreadCrumb.getText();
    }

    public void configureTemplate(String templateName){
        waitForSeconds(3);
        search(templateName);
        clickInDetails();
        waitForSeconds(5);
        editTemplate.click();
        okToActionInModal(true);
        waitForSeconds(3);
    }

    public void markAsDefaultTemplate() {
        markAsDefaultTemplate.click();
        okToActionInModal(true);
    }

    public String getDefaultLabel() {
        return defaultLabel.getText();
    }


    public String deleteTheTemplate() {
        deleteButton.click();
        String mes = deleteConfirmMessage.getText();
        return mes;
    }

    public void setTemplateLeverCanRequest(Boolean canRequest) {
        if (canRequest) {
            templateLeverCanRequestYes.click();

        } else {
            templateLeverCanRequestNo.click();
        }
    }

    public boolean isToggleAsSetted(Boolean toggleStatus) {
        String bgColor = "";
        String toggleOn = "rgba(244, 246, 248, 1)";
        if (toggleStatus) {
            bgColor = templateLeverCanRequestYes.getCssValue("background-color");
        } else {
            bgColor = templateLeverCanRequestNo.getCssValue("background-color");
        }
        return bgColor.equals(toggleOn);
    }


    public void setTemplateLeverWeeklyLimits(String hours) {
        templateLeverWeeklyLimitInput.clear();
        templateLeverWeeklyLimitInput.sendKeys(hours);
    }

    public String getWeeklyLimitHrs() {
        return weeklyLimitHrs.getText();
    }

    public void viewTimeOffConfigure(String timeOff) throws Exception {
        searchTimeOff(timeOff);
        isButtonClickable(view);
        System.out.println("View button is shown and clickable!");
        view.click();
    }

    public void configureTimeOffRules(String timeOff){
        searchTimeOff(timeOff);
        waitForSeconds(5);
        if (isButtonClickable(configure)) {
            System.out.println("Configure button is shown and clickable!");
            configure.click();
        } else if (isButtonClickable(edit)) {
            System.out.println("Edit button is shown and clickable!");
            edit.click();
        } else {
            System.out.println("Something is wrong with the time off UI in the template detail page!");
        }
    }

    public boolean isButtonClickable(WebElement button) {
        Boolean shown = null;
        Boolean status = null;
        try {
            shown = button.isDisplayed();
            status = button.isEnabled();
            return shown && status;
        } catch (Exception NoSuchElement) {
            return false;
        }
    }

    public void removeTimeOffRules(String timeOff) throws Exception {
        searchTimeOff(timeOff);
        if (isButtonClickable(remove)) {
            System.out.println("Remove button is shown and clickable!");
            remove.click();
        } else {
            System.out.println("Time off rule can't be removed before configured!");
        }
    }

    public String getNotConfigured() {
        return notConfigured.getText();
    }

    public void saveTemplateAs(String method) {
        scrollToBottom();
        waitForSeconds(2);
        saveAsDrop.click();
        switch (method) {
            case "Save as draft":
                saveAsDraft.click();
                break;
            case "Publish now":
                publishNow.click();
                break;
            case "Publish later":
                publishLater.click();
                break;
        }
        saveTemplate.click();
        try {
            if (dateOfPublishModal.isDisplayed()) {
                dateSelect.click();
                nextMonthArrow.click();
                dayChosen.click();
                okToActionInModal(true);
            }
        } catch (Exception NoSuchElementException) {
            System.out.println("Doesn't use publish later");
        }
        waitForSeconds(5);
    }


    //settings
    public String getQuestionTitle() {
        return useAccrual.getAttribute("question-title");
    }

    public void setAccrualToggle(Boolean tureOrFalse) {
        if (tureOrFalse != isAccrualToggleOn()) {
            toggleSlide.click();
        }
    }

    public Boolean isAccrualToggleOn() {
        String toggleColor = toggleSlide.getCssValue("background-color");
        String toggleOn = "rgba(49, 61, 146, 1)";
        if (toggleColor.equals(toggleOn)) {
            System.out.println("Accrual toggle is on!");
            return true;
        } else {
            System.out.println("Accrual toggle is off!");
            return false;
        }
    }

    public void addTimeOff(String reaName) {
        addTimeOffButton.click();
        reasonName.clear();
        reasonName.sendKeys(reaName);
    }

    public void okCreatingTimeOff() {
        scrollToElement(okCreating);
        okCreating.click();
        waitForSeconds(3);
    }

    public void cancelCreatingTimeOff() {
        scrollToElement(cancelCreating);
        cancelCreating.click();
    }

    public void nxetTimeOffReasonPage() {
        scrollToElement(TimeOffReasonRightArrow);
        TimeOffReasonRightArrow.click();
    }

    public String getErrorMessage() {
        return errorMes.getText();
    }

    public Boolean isOKButtonEnabled() {//time off reason name is required
        addTimeOffButton.click();
        reasonName.clear();
        return okCreating.isEnabled();
    }

    public ArrayList<String> getAllTheTimeOffReasons() {
        return getWebElementsLabels(timeOffReasonNames);
        // use contains() to judge if the new time off is in the ArrayList
    }

    public Boolean isTimeOffReasonDisplayed(String timeOff) {
        return getAllTheTimeOffReasons().contains(timeOff);
    }


    public void editTimeOffReason(String reaName) throws Exception {
        if (isElementLoaded(editTimeOff, 10)) {
            clickTheElement(editTimeOff);
            if (isElementLoaded(reasonName, 5)) {
                reasonName.clear();
                reasonName.sendKeys(reaName);
            }
        } else
            SimpleUtils.fail("Edit button or reason name input box fail to load! ", false);
    }

    public String removeTimeOffInSettings() {
        scrollToElement(removeButton);
        removeButton.click();
        return removeConfirmMes.getText();
    }
    //ok to action in modal and get reason names , not contain now.

    public void okToActionInModal(boolean okToAction) {
        if (okToAction) {
            scrollToElement(okToEditing);
            okToEditing.click();
        } else {
            scrollToElement(cancelCreating);
            cancelEditing.click();
        }
        waitForSeconds(3);
    }


    @FindBy(css = ".time-off-reason-setting tr.ng-scope")
    private List<WebElement> timeOffReasons;

    public void removeTimeOffReasons(String timeOffReasonName) {
        if (areListElementVisible(timeOffReasons, 10) && timeOffReasons.size() > 0) {
            for (WebElement reason : timeOffReasons) {
                if (reason.findElement(By.cssSelector(".one-line-overflow")).getText().equalsIgnoreCase(timeOffReasonName)) {
                    clickTheElement(reason.findElement(By.cssSelector("[label=\"Remove\"]")));
                    okCreatingTimeOff();
                    System.out.println("Delete the time off reason successfull!");
                    break;
                }
            }
        } else
            System.out.println("There is no time off reason been listed!");
    }

    //promotion part
    public String getPromotionSettingTitle() {
        scrollToBottom();
        return promotionTitle.getText();
    }

    public boolean isAddButtonDisplayedAndClickable() {
        return isElementDisplayed(promotionRuleAddButton) && isButtonClickable(promotionRuleAddButton);
    }

    public void addPromotionRule() {
        promotionRuleAddButton.click();
        waitForSeconds(3);
    }

    public String getPromotionModalTitle() {
        waitForSeconds(5);
        return promotionModalTitle.getText();
    }

    public void setPromotionName(String promotionName) {
        promotionNameInput.clear();
        promotionNameInput.sendKeys(promotionName);
    }

    public void addCriteriaByJobTitle() {//Job Title & Engagement Status
        promotionCriteriaType.click();
        criteriaByJobTitle.click();
        criteriaBefore.click();
        criteriaBeforeSearchInput.clear();
        criteriaBeforeSearchInput.sendKeys("Senior Ambassador");
        jobTitleAsSeniorAmbassador.click();
        criteriaBeforeSearchInput.clear();
        criteriaBeforeSearchInput.sendKeys("WA Ambassador");
        waitForSeconds(3);
        jobTitleAsWAAmbassador.click();
        criteriaAfter.click();
        criteriaAfterSearchInput.clear();
        criteriaAfterSearchInput.sendKeys("General Manager");
        jobTitleAfterPromotion.click();
    }

    public void addCriteriaByEngagementStatus() {
        promotionCriteriaType.click();
        criteriaByEngagementStatus.click();
        engagementBefore.click();
        partTimeBefore.click();
        engagementAfter.click();
        fullTimeAfter.click();
    }

    public String getJobTitleSelectedBeforePromotion() {
        return multiSelectPlaceHolder.getText();
    }

    public boolean verifyJobTitleSelectedBeforePromotionShouldBeDisabledAfterPromotion(String jobTitleSelectBefore) {
        Boolean isDisabled = false;
        criteriaAfter.click();
        criteriaAfterSearchInput.clear();
        criteriaAfterSearchInput.sendKeys(jobTitleSelectBefore);
        try {
            jobTitleAfterPromotion.click();
        } catch (ElementNotInteractableException exception ) {
            isDisabled = true;
        }
        return isDisabled;
    }

    public void setPromotionAction(String balanceB, String balanceA) {
        //before
        balanceBeforePromotion.click();
        balanceSearchInputB.clear();
        balanceSearchInputB.sendKeys(balanceB);
        balanceSearchResultB.click();
        //after
        balanceAfterPromotion.click();
        balanceSearchInputA.clear();
        balanceSearchInputA.sendKeys(balanceA);
        balanceSearchResultA.click();
    }

    public ArrayList getPromotionRuleName() {
        return getWebElementsLabels(promotionRuleNames);
    }

    public void EditPromotionRule() {
        promotionEditButton.click();
    }

    public void removePromotionRule() {
        promotionRemoveButton.click();
    }

    public String getRemovePromotionRuleModalTitle() {
        return removeModalTitle.getText();
    }

    public String getRemovePromotionRuleModalContent() {
        return removeModalContent.getText();
    }

    public boolean isTherePromotionRule(){
        Boolean hasPromotionRule=true;
        if(!isElementDisplayed(promotionEditButton)){
            hasPromotionRule=false;
        }
     return hasPromotionRule;
    }

    public ArrayList getTimeOffReasonsInGlobalSetting() {
        ArrayList<String> timeOffConfiguredInGlobalSettings = new ArrayList<>();
        ArrayList<String> PromotionNames = getPromotionRuleName();
        int page = new Integer(pagination.getText().trim().split(" ")[1]);
        for(int i=0;i<page;i++){
            ArrayList<String> timeOffConfiguredInGlobalSettings1 = getAllTheTimeOffReasons();
            int n = timeOffConfiguredInGlobalSettings1.size()-PromotionNames.size();
            List<String> TimeOffConfigs = timeOffConfiguredInGlobalSettings1.subList(0,n);
            timeOffConfiguredInGlobalSettings.addAll(TimeOffConfigs);
            nxetTimeOffReasonPage();
        }
        return timeOffConfiguredInGlobalSettings;
    }

    public ArrayList getTimeOffOptions() {
        balanceBeforePromotion.click();
        ArrayList<String> optList = new ArrayList<>();
        tOffInPromotionSelect.forEach((e) -> {
            optList.add(e.getAttribute("title"));
        });
        balanceBeforePromotion.click();
        return optList;
    }

    public String getTemplateListLabel() {
        return templateListLabel.getText();
    }

    public void goToPreviousPage() {
        previousPage.click();
    }

    public void goToNextPage() {
        nextPage.click();
    }

    public String getCurrentPage() {
        waitForSeconds(3);
        return currentPage.getText();
    }

    public String getDynamicEmployeeGroupName() {
        return dynamicEmployeeGroupName.getText();
    }

    public String getDynamicEmployeeGroupDesc() {
        return dynamicEmployeeGroupDesc.getText();
    }

    public ArrayList getDynamicEmployeeGroupLabs() {
        return getWebElementsText(dynamicEmployeeGroupLabel);
    }

    public void viewEmployeeGroup() {
        viewBtnInAssociate.click();
        waitForSeconds(3);
    }

    public static void exportTimeOffBalance(Map TimeOffBalance, String accessToken, Object expectedPageNum) {
        String getTimeOffBalanceUrl = Constants.getTimeOffBalance;
        RestAssured.given().log().all().queryParams(TimeOffBalance).header("accessToken", accessToken).when().get(getTimeOffBalanceUrl)
                .then().log().all().statusCode(200).body("numberOfPages", Matchers.equalTo(expectedPageNum));
        SimpleUtils.pass("Succeeded in exporting time off balance!");
    }

    public String getViewModalTitle() {
        return viewModalTitle.getText();
    }

    public void cancelAssociation() {
        cancelBtnInAssociate.click();
        waitForSeconds(2);
        yesToCancel.click();
        if (isElementDisplayed(leavePageBtn)) {
            toLeavePage();
        }
        waitForSeconds(2);
    }

    public void saveAssociation() {
        saveAssociate.click();
        waitForSeconds(2);
    }

    public void toLeavePage() {
        leavePageBtn.click();
    }

    public void deleteOrArchiveTemplate(String temp) {
        search(temp);
        clickInDetails();
        waitForSeconds(3);
        if (isDeleteButtonDisplayed()) {
            deleteButton.click();
        } else {
            archivePublishedTemplate();
        }
        okToActionInModal(true);
    }

    public String getAssociations() {
        waitForSeconds(5);
        viewModalTestButton.click();
        return viewModalTestResult.getText();
    }

    @FindBy(css = "div.text-danger.text-invalid-range.ng-binding")
    private WebElement noEnoughMessage;

    public void verifyNotEnoughMessageDisplay(){
        if(noEnoughMessage.isDisplayed()){
            SimpleUtils.pass("Error message display");
        }else
            SimpleUtils.fail("Error message doesn't display",false);
    }

    @FindBy(css = "img[src = 'img/legion/add.png']")
    private WebElement addIcon;
    @FindBy(css = "select[aria-label = 'Accrual Units']")
    private WebElement unitSelect;

    public void addTimeOffReasonWithDayUnit() {
        click(addIcon);
        reasonName.sendKeys("Aaaa");
        unitSelect.sendKeys("Days");
        scrollToElement(okCreating);
        click(okCreating);
    }

    public void editTimeOffReason() {
        scrollToElement(editTemplate);
        click(editTemplate);
        System.out.println(unitSelect.getAttribute("disabled"));
        if(unitSelect.getAttribute("disabled").equals("true")){
            SimpleUtils.pass("Unit can't be edited");
        }else
            SimpleUtils.fail("Unit can be edited",false);
        scrollToElement(okCreating);
        click(okCreating);
        click(remove);
        click(okCreating);
    }

    @FindBy(css = "td lg-button[label='Edit']")
    private WebElement editTimeOffReason;
    @FindBy(css = "div.lg-templates-table-improved__grid-column > lg-button > button")
    private WebElement firstPublishTemplate;
    @FindBy(css = "div.lg-templates-table-improved__grid-row.ng-scope")
    private WebElement firstTemplate;
    @FindBy(css = "div.toggle")
    private WebElement toggle;
    @FindBy(css = "div.lg-templates-table-improved__grid-column.lg-templates-table-improved__grid-column--left.ml-25 > lg-button > button")
    private WebElement firstDraftTemplate;

    public void goToTemplate(){
        if(firstTemplate.getAttribute("class").contains("hasChildren")){
            click(toggle);
            click(firstDraftTemplate);
        }else
            click(firstPublishTemplate);
    }

    public void otherDistributionMethodisDiabled() {
        goToTemplate();
        click(editTemplate);
        click(okCreating);
        scrollToElement(editTimeOffReason);
        click(editTimeOffReason);

        verifyWorkRoleOnlyDisplayForScheduleHour("Worked Hours");
        verifyWorkRoleOnlyDisplayForScheduleHour("Scheduled Hours");
        verifyWorkRoleOnlyDisplayForScheduleHour("Lump Sum");
        verifyWorkRoleOnlyDisplayForScheduleHour("None");

        if(getDriver().findElement(By.cssSelector("option[label='Monthly']")).getAttribute("disabled").equals("true")
                && getDriver().findElement(By.cssSelector("option[label='Weekly']")).getAttribute("disabled").equals("true")
                && getDriver().findElement(By.cssSelector("option[label='Worked Hours']")).getAttribute("disabled") == null
                && getDriver().findElement(By.cssSelector("option[label='Scheduled Hours']")).getAttribute("disabled") == null
                && getDriver().findElement(By.cssSelector("option:nth-child(6)")).getAttribute("disabled").equals("true")
                && getDriver().findElement(By.cssSelector("option[label='None']")).getAttribute("disabled") == null){
            SimpleUtils.pass("Lump Sum/Scheduled Hours/Worked Hours/None are enabled, others are disabled");
        }else
            SimpleUtils.fail("Lump Sum/Scheduled Hours/Worked Hours/None are diabled or others are enabled",false);
    }

    @FindBy(css = "input-field[placeholder = 'All Work Roles']>ng-form>input")
    private WebElement workRoleInput;
    @FindBy(css = "question-input[question-title = 'Distribution Method'] > div > div > ng-transclude > input-field > ng-form > div > select" )
    private WebElement distributioonMethodSelect;

    private void verifyWorkRoleOnlyDisplayForScheduleHour(String type){
        distributioonMethodSelect.sendKeys(type);
        if(!getDriver().findElement(By.cssSelector("option[label='Scheduled Hours']")).isSelected()){
            if(!isElementEnabled(workRoleInput)){
                SimpleUtils.pass("Work role doesn't display for other distribution type");
            }else
                SimpleUtils.fail("Work role display for other distribution type",false);
        }else{
            if(isElementEnabled(workRoleInput)){
                SimpleUtils.pass("Work role display for Scheduled Hours");
            }else
                SimpleUtils.fail("Work role doesn't display for Scheduled Hours",false);
        }
    }

    public void verifyWorkRoleStatus(){
        click(backButton);
        click(leavePageBtn);
        search("Floating Holiday");
        //scrollToElement(editTemplate);
        click(getDriver().findElement(By.cssSelector(" tr > td:nth-child(2) > lg-button:nth-child(1) > button")));

        verifyWorkRoleOnlyDisplayForScheduleHour("Monthly");
        verifyWorkRoleOnlyDisplayForScheduleHour("Weekly");
        verifyWorkRoleOnlyDisplayForScheduleHour("Worked Hours");
        verifyWorkRoleOnlyDisplayForScheduleHour("Scheduled Hours");
        verifyWorkRoleOnlyDisplayForScheduleHour("Lump Sum");
        verifyWorkRoleOnlyDisplayForScheduleHour("None");

        click(backButton);
        click(leavePageBtn);
        click(saveAsDrop);
        scrollToElement(publishNow);
        click(publishNow);
        click(saveTemplate);
    }

    public void configureGlobalSettings() {
        switchToSettings();
        String toggleIsOn = "rgba(49, 61, 146, 1)";
        String bgColor1 = useAccrualToggle.getCssValue("background-color");
        if (!bgColor1.equals(toggleIsOn)) {
            useAccrualToggle.click();
        }
        String bgColor2 = timesheetRequiredToBeApprovedToggle.getCssValue("background-color");
        if (!bgColor2.equals(toggleIsOn)) {
            timesheetRequiredToBeApprovedToggle.click();
        }
        lookBackDays.clear();
        lookBackDays.sendKeys("5");
        waitForSeconds(3);
        switchToTemplates();
    }

    @FindBy(css = "input[type = 'checkbox']")
    private WebElement firstWorkRole;
    @FindBy(css = "div.select-list-item>input-field>label")
    private List<WebElement> workRoleList;

    public ArrayList<String> searchAndSelectWorkRole(){
        verifyWorkRoleOnlyDisplayForScheduleHour("Scheduled Hours");
        scrollToElement(workRoleInput);
        clickTheElement(workRoleInput);

        ArrayList<String> workRoles = getWebElementsText(workRoleList);

        templateSearchBox.clear();
        templateSearchBox.sendKeys("AMBASSADOR");
        click(firstWorkRole);

        templateSearchBox.clear();
        templateSearchBox.sendKeys("Key Carrier");
        click(firstWorkRole);

        if(getDriver().findElement(By.cssSelector("input-field[placeholder = 'All Work Roles']>ng-form>div")).getAttribute("innerText").contains("2 Work Roles Selected")){
            SimpleUtils.pass("2 work roles selected successfully");
        }else
            SimpleUtils.fail("2 work roles selected faied",false);
        return workRoles;
    }

    @FindBy(css = "input[placeholder = 'Search by time off reason name']")
    private WebElement timeOffReasonSearchBox;
    @FindBy(css = "input[placeholder = 'Search by promotion name']")
    private WebElement promotionSearchBox;
    @FindBy(css = "div:nth-child(2) > lg-paged-search > div > ng-transclude > table > tbody > tr:nth-child(2)")
    private WebElement firstTimeOffReason;
    @FindBy(css = "div:nth-child(2) > lg-paged-search > div > ng-transclude > div")
    //@FindBy(linkText = "No matching time off reason found.")
    //@FindBy(xpath = "//div[2]/lg-paged-search/div/ng-transclude/div")
    private WebElement noMatchTimeOffReason;
    @FindBy(css = "tr[ng-repeat = 'item in $ctrl.timeOffReasonSortedRows']>td>lg-button[label = 'Edit']>button")
    private WebElement timeOffReasonEdit;
    @FindBy(css = "div:nth-child(3) > lg-paged-search > div > ng-transclude > table > tbody > tr:nth-child(2);")
    private WebElement firstPromotion;
    @FindBy(css = "div:nth-child(3) > lg-paged-search > div > ng-transclude > div")
    private WebElement noMatchPromotion;
    @FindBy(css = "div:nth-child(2) > lg-paged-search > div > ng-transclude > table > tbody > tr[ng-repeat='item in $ctrl.timeOffReasonSortedRows']")
    private List<WebElement> timeOffResonRows;
    @FindBy(css = "div:nth-child(3) > lg-paged-search > div > ng-transclude > table > tbody > tr[ng-repeat='item in $ctrl.timeOffReasonSortedRows']")
    private List<WebElement> promotionRows;
    @FindBy(css = "tr[ng-repeat = 'item in $ctrl.promotionSortedRows']>td>lg-button[label = 'Edit']>button")
    private WebElement promotionEdit;

    public Boolean searchTimeOffReason(String timeOffReasonName) throws Exception{
        if(isElementLoaded(timeOffReasonSearchBox,5)){
            timeOffReasonSearchBox.clear();
            timeOffReasonSearchBox.sendKeys(timeOffReasonName);
            waitForSeconds(3);
//            if(!noMatchTimeOffReason.getAttribute("class").contains("no-record"))
//                return true;
//            else
//                return false;
            if(isElementLoaded(noMatchTimeOffReason,5))
                return false;
            else
                return true;
        }else{
            SimpleUtils.fail("Time off reason search box loaded failed",false);
            return false;
        }
    }

    public Boolean searchPromotion(String promotionName) throws Exception{
        if(isElementLoaded(promotionSearchBox,5)){
            promotionSearchBox.clear();
            promotionSearchBox.sendKeys(promotionName);
            if(isElementLoaded(firstPromotion,5))
                return true;
            else
                return false;
        }else{
            SimpleUtils.fail("Promotion search box loaded failed",false);
            return false;
        }
    }

    @FindBy(css = "div:nth-child(2) > lg-paged-search > div > ng-transclude > table")
    private WebElement timeOffPage;
    @FindBy(css = "div:nth-child(2) > lg-paged-search > div > lg-tab-toolbar > div > div.lg-tab-toolbar__content > lg-pagination > div > div.lg-pagination__arrow.lg-pagination__arrow--right")
    private WebElement timeOffNextPage;
    @FindBy(css = "div:nth-child(2) > lg-paged-search > div > lg-tab-toolbar > div > div.lg-tab-toolbar__content > lg-pagination > div > div.lg-pagination__arrow.lg-pagination__arrow--left")
    private WebElement timeOffPreviousPage;
    @FindBy(css = "div:nth-child(3)")
    private WebElement promotionPage;
    @FindBy(css = "div:nth-child(3) > lg-paged-search > div > lg-tab-toolbar > div > div.lg-tab-toolbar__content > lg-pagination > div > div.lg-pagination__arrow.lg-pagination__arrow--left")
    private WebElement promotionPreviousPage;
    @FindBy(css = "div:nth-child(3) > lg-paged-search > div > lg-tab-toolbar > div > div.lg-tab-toolbar__content > lg-pagination > div > div.lg-pagination__arrow.lg-pagination__arrow--right")
    private WebElement promotionNextPage;

    public void verifyTimeOffPage() throws Exception{
        if(isElementDisplayed(timeOffPage)){
            if(isElementLoaded(timeOffPreviousPage,5) && isElementLoaded(timeOffNextPage,5) && isElementEnabled(timeOffNextPage)) {
                if(timeOffPreviousPage.getAttribute("class").contains("lg-pagination__arrow--disabled"))
                    SimpleUtils.pass("Time off reason previous page is disable default");
                else
                    SimpleUtils.fail("Time off reason previous page is not disable default",false);
                click(timeOffNextPage);
                SimpleUtils.pass("Time off reason page function work well");
            }
            else
                SimpleUtils.fail("Time off reason page function can't work",false);
        }else
            SimpleUtils.fail("Time off page doesn't display",false);
    }

    public void verifyPromotionPage() throws Exception{
        scrollToBottom();
        if(isElementLoaded(promotionPage,5)){
            if(isElementDisplayed(promotionPreviousPage) && isElementDisplayed(promotionNextPage) && isElementEnabled(promotionNextPage)) {
                if(promotionPreviousPage.getAttribute("class").contains("lg-pagination__arrow--disabled"))
                    SimpleUtils.pass("Promotion previous page is disable default");
                else
                    SimpleUtils.fail("Promotion previous page is not disable default",false);
                click(promotionNextPage);
                SimpleUtils.pass("Promotion page function work well");
            }
            else
                SimpleUtils.fail("Promotion page function can't work",false);
        }else
            SimpleUtils.fail("Promotion page doesn't display",false);
    }

    @FindBy(css = "input-field[label = 'Country'] > ng-form")
    private WebElement countryDropDownList;
    @FindBy(css = "input-field[label = 'State'] > ng-form")
    private WebElement stateDropDownList;
    @FindBy(css = "input-field[label = 'City'] > ng-form")
    private WebElement cityDropDownList;

    public void verifyGeographicDisplay() throws Exception{
        click(promotionRuleAddButton);
        if(isElementLoaded(countryDropDownList,5) && isElementLoaded(stateDropDownList) && isElementLoaded(cityDropDownList)){
            SimpleUtils.pass("All Geographic fields display");
        }else
            SimpleUtils.fail("All Geographic fields don't display",false);
    }

    @FindBy(css = "input-field[label = 'Country'] > ng-form > div")
    private WebElement countryValue;
    @FindBy(css = "input-field[label = 'State'] > ng-form > div")
    private WebElement stateValue;
    @FindBy(css = "input-field[label = 'City'] > ng-form > div")
    private WebElement cityValue;

    public void verifyGeographicDefaultValue() throws Exception{
        if(countryValue.getAttribute("innerText").contains("Any") && stateValue.getAttribute("innerText").contains("Any") && cityValue.getAttribute("innerText").contains("Any")){
            SimpleUtils.pass("All Geographic default value are Any");
        }else
            SimpleUtils.fail("All Geographic default value aren't Any",false);
    }

    @FindBy(css = "input[placeholder = 'Search']")
    private WebElement searchBox;
    @FindBy(css = "lg-picker-input[label = 'Country']>div>div>ng-transclude>lg-search-options>div>div>div")
    private List<WebElement> countryList;

    public void verifyCountrySearch() throws Exception{
        click(countryDropDownList);

        if(countryList.size() == 15){
            SimpleUtils.pass("Country list display successfully");

            searchBox.sendKeys("Ja");

            if(countryList.size() == 1){
                SimpleUtils.pass("Country search function work well");
                click(countryList.get(0));
            }else
                SimpleUtils.fail("Country search function work failed",false);

        }else
            SimpleUtils.fail("Country list display failed",false);
    }

    @FindBy(css = "input[type = 'checkbox']")
    private WebElement checkBox;
    @FindBy(css = "lg-picker-input[label = 'State'] > div > div > ng-transclude > lg-search > input-field > ng-form > input")
    private WebElement stateSearchBox;

    public void verifyStateSearch() throws Exception{
        click(stateDropDownList);
        stateSearchBox.sendKeys("Gi");
        click(checkBox);
        click(checkBox);


    }

    @FindBy(css = "input-field[label = 'City'] > ng-form > input")
    private WebElement cityInput;
    @FindBy(css = "lg-picker-input[label = 'City'] > div > div > ng-transclude > lg-search > input-field > ng-form > input")
    private WebElement citySearchBox;
    @FindBy(css = "lg-picker-input[label = 'City'] > div > div > ng-transclude > div > div > input-field > ng-form > input")
    private WebElement cityCheckBox;

    public void verifyCity() throws Exception{
        cityInput.clear();
        click(cityInput);
        cityInput.sendKeys("Tokyo");
        cityInput.clear();
        click(cityDropDownList);

        citySearchBox.sendKeys("Seattle");
        highlightElement(cityCheckBox);
        click(cityCheckBox);
        click(cityCheckBox);
    }

    public void verifyRequestRules(HashMap<String, String> requestRules) throws Exception {
        for (String key : requestRules.keySet()) {
            if (isElementLoaded(getDriver().findElement(By.xpath("//*[contains(text(),'" + key + "')]")))) {
                WebElement status = getDriver().findElement(By.xpath("//*[contains(text(),'" + key + "')]/parent::div//div[contains(@class,\"lg-button-group-selected\")]/span"));
                if (status.getText().trim().equalsIgnoreCase(requestRules.get(key))) {
                    SimpleUtils.fail("The time off rule is correct", true);
                } else {
                    SimpleUtils.fail("The time off rule status is correct", false);
                }
            } else {
                SimpleUtils.fail("The time off rule does not exist", false);
            }
        }
    }
}
