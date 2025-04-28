package com.legion.pages.core.oplabormodel;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.xml.crypto.dsig.SignatureMethod;

import static com.legion.utils.MyThreadLocal.getDriver;

public class LaborModelTemplateDetailPage extends BasePage {
    public LaborModelTemplateDetailPage() {
        PageFactory.initElements(getDriver(), this);
    }

    //added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a.ng-binding")
    private WebElement backButton;
    @FindBy(css = "h1.ng-binding.ng-scope")
    private WebElement templateNameAndStatus;
    @FindBy(css = "div.button-container lg-button[label='History']>button")
    private WebElement historyButton;
    @FindBy(css = "div.button-container lg-button[label='Delete']>button")
    private WebElement deleteButton;
    @FindBy(css = "div.button-container lg-button[label='Edit']>button")
    private WebElement editButton;
    @FindBy(css = "lg-button[label='Close']>button")
    private WebElement closeButton;

    //select
    @FindBy(css = "table.lg-table.ng-scope>tbody:nth-child(2) td:nth-child(1)")
    private WebElement workRole;
    @FindBy(css = "table.lg-table.ng-scope>tbody:nth-child(2) td:nth-child(2)>lg-button>button")
    private WebElement selectTasksLink;
    @FindBy(css = "table.lg-table.ng-scope>tbody:nth-child(2) td:nth-child(3)>input-field")
    private WebElement selectWorkRolesCheckBox;

    //edit
    @FindBy(css = "div.lg-modal>h1")//Editing Template
    private WebElement editTemplateModalTitle;
    @FindBy(css = "div.lg-modal input-field[label='Template Name'] input")
    private WebElement tempName;
    @FindBy(css = "div.lg-modal input-field[label='Description'] input")
    private WebElement tempDesc;
    @FindBy(css = "div.lg-modal input-field[label='Edit Notes'] textarea")
    private WebElement tempNotes;
    @FindBy(css = "div.lg-modal lg-button[label='Cancel']>button")
    private WebElement cancelButtonInModal;
    @FindBy(css = "div.lg-modal lg-button[label='OK']>button")
    private WebElement okButtonInModal;

    //select tasks
    @FindBy(css = "h1.lg-modal__title>div")
    private WebElement selectTaskModalTitle;
    @FindBy(css = "div.lg-modal lg-search input")
    private WebElement searchBoxInModal;
    @FindBy(css = "div.lg-modal lg-search div.lg-search-icon")
    private WebElement searchIconInModal;
    @FindBy(css = "div.lg-modal tr.ng-scope input-field[type='checkbox'] input")
    private WebElement checkBox;

    //cancel or save
    @FindBy(css = "lg-button[label='Save as draft']>button.saveas-drop")
    private WebElement saveDropsButton;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(1)")
    private WebElement saveAsDraft;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(2)")
    private WebElement publishNow;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(3)")
    private WebElement publishLater;
    @FindBy(css = "button.pre-saveas")
    private WebElement preSaveAs;

    @FindBy(css = "lg-tab[tab-title='Details'] lg-button[label='Cancel']>button")
    private WebElement cancelButton;
    @FindBy(css = "modal[modal-title='Cancel Editing?'] lg-button[label='Keep editing']>button")
    private WebElement keepEditing;
    @FindBy(css = "modal[modal-title='Cancel Editing?'] lg-button[label='Yes']>button")
    private WebElement cancelEditing;

    public void back() {
        backButton.click();
    }

    public void edit() throws Exception {
        if (isElementLoaded(editButton, 5)) {
            clickTheElement(editButton);
        } else
            SimpleUtils.fail("Edit button fail to load! ", false);
    }

    public void close() {
        closeButton.click();
    }

    public void delete() {
        deleteButton.click();
    }


    public String getEditModalTitle() {
        return editTemplateModalTitle.getText();
    }

    public void editTemplate(String newTemplateName, String newDesc, String newNotes) {
        tempName.clear();
        tempName.sendKeys(newTemplateName);
        tempDesc.clear();
        tempDesc.sendKeys(newDesc);
        tempNotes.clear();
        tempNotes.sendKeys(newNotes);
    }

    public String getTemplateNameAndStatus() {
        return templateNameAndStatus.getText();
    }

    public void toSelectATask() {
        selectTasksLink.click();
    }


    public String getSelectModalTitle() {
        return selectTaskModalTitle.getText();
    }

    public void searchTasksInModal(String taskName) {
        searchBoxInModal.clear();
        searchBoxInModal.sendKeys(taskName);
        searchIconInModal.click();
        waitForSeconds(5);
    }

    public void checkTheTask() {
        checkBox.click();
    }

    public void cancelInModal() {
        cancelButtonInModal.click();
    }

    public void okInModal() {
        clickTheElement(okButtonInModal);
    }

    public void selectTasks(String taskName) {
        toSelectATask();
        searchTasksInModal(taskName);
        checkTheTask();
        okInModal();
    }

    public void selectWorKRole() {
        selectWorkRolesCheckBox.click();
    }

    public void save(String Patter) {
        scrollToElement(saveDropsButton);
        saveDropsButton.click();
        waitForSeconds(3);
        switch (Patter) {
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
        waitForSeconds(3);
        preSaveAs.click();
    }

    public void cancel() {
        clickTheElement(cancelButton);
        clickTheElement(cancelEditing);
    }

}
