package com.legion.pages.core.oplabormodel;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class LaborModelRepositoryPage extends BasePage {

    public LaborModelRepositoryPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a.ng-binding")
    private WebElement backButton;
    @FindBy(css = "lg-button[label='Edit']>button")
    private WebElement editButton;
    @FindBy(css = "nav.lg-tabs__nav>div:first-child")
    private WebElement taskTab;
    @FindBy(css = "nav.lg-tabs__nav>div:nth-child(2)")
    private WebElement externalAttriTab;
    @FindBy(xpath = "//input[@placeholder=\"You can search task name or label name.\"]")
    private WebElement searchBoxOfTask;
    @FindBy(css = "div.lg-search-icon")
    private WebElement searchButton;
    @FindBy(css = "table.lg-table.ng-scope tr:nth-child(2)>td:nth-child(2)>lg-button")
    private WebElement theFirstTaskInTheList;
    @FindBy(css = "table.lg-table.ng-scope tr:nth-child(2)>td:nth-child(3)")
    private WebElement theFirstLabelInTheList;
    @FindBy(css = "div.no-record.ng-binding.ng-scope")
    private WebElement noMatchMessage;

    //disable
    @FindBy(css = "td>lg-button[label='Disable']")
    private WebElement theFirstDisableInTheList;
    @FindBy(css = "h1.lg-modal__title>div")
    private WebElement disableModalTitle;
    @FindBy(css = "modal lg-button[label='Cancel']>button")
    private WebElement cancelButtonOfDisable;
    @FindBy(css = "modal lg-button[label='OK']>button")
    private WebElement okButtonOfDisable;

    //add task
    @FindBy(css = "div.button-container lg-button[label='Add Task']>button")
    private WebElement addTaskButton;

    //cancel
    @FindBy(css = "div.button-container lg-button[label='Cancel']>button")
    private WebElement cancelButton;
    @FindBy(css = "div.modal-content h1")
    private WebElement cancelModal;
    @FindBy(css = "lg-button[label='Keep editing']>button")
    private WebElement keepEditing;
    @FindBy(css = "lg-button[label='Yes']>button")
    private WebElement yesToCancelButton;

    //save
    @FindBy(css = "div.button-container lg-button[label='Save']>button")
    private WebElement saveButton;

    //action
    @FindBy(css = "table.lg-table.ng-scope tr:nth-child(1)>th:nth-child(4)")
    private WebElement colAction;

    public void back() {
        backButton.click();
    }

    public String getEditButton() {
        return editButton.getText();
    }

    public void edit() {
        editButton.click();
    }


    public void addNewTask() {
        addTaskButton.click();
    }

    public void editAnExistingTask(String oldTaskName) throws Exception {
        searchByTaskORLabel(oldTaskName);
        edit();
        clickInToDetails();
    }

    //cancel part
    public void cancel() {
        cancelButton.click();
    }

    public String getModalTitle() {
        return cancelModal.getText();
    }

    public void keepEditing() {
        keepEditing.click();
    }

    public void cancelEditing() {
        yesToCancelButton.click();
    }

    //save
    public void save() {
        saveButton.click();
        waitForSeconds(3);
    }

    //search a task or label
    public void searchByTaskORLabel(String taskOrLabelName) throws Exception {
        waitForSeconds(3);
        if (isElementLoaded(searchBoxOfTask, 15) && isElementLoaded(searchButton, 10)) {
            clickTheElement(searchBoxOfTask);
            waitForSeconds(1);
            searchBoxOfTask.clear();
            searchBoxOfTask.sendKeys(taskOrLabelName);
            searchButton.click();
        } else {
            SimpleUtils.fail("Labor Standard Repository: Search Box and Search Icon failed to load!", false);
        }
    }

    public void clickInToDetails() {
        theFirstTaskInTheList.click();
    }

    public String getTheSearchedTaskName() {
        return theFirstTaskInTheList.getText();
    }

    public String getTheSearchedLabel() {
        return theFirstLabelInTheList.getText();
    }

    public String getNoMatchMessage() {
        return noMatchMessage.getText();
    }


    //disable a task
    public String getActionColumnLabel() {
        return colAction.getText();
    }

    public String getActionColumnValue() {
        return theFirstDisableInTheList.getText();
    }

    public void disable() {
        theFirstDisableInTheList.click();
    }

    public String getDisableModalTitle() {
        return disableModalTitle.getText();
    }

    public void cancelDisableAction() {
        cancelButtonOfDisable.click();
    }

    public void saveDisableAction() {
        okButtonOfDisable.click();
    }

}
