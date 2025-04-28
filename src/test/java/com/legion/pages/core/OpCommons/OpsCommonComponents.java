package com.legion.pages.core.OpCommons;

import com.legion.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class OpsCommonComponents extends BasePage {

    public OpsCommonComponents() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    //back
    @FindBy(css = "div.lg-page-heading>div>a")
    private WebElement backButton;

    //action in modal
    @FindBy(css = "form-buttons lg-button[label='OK']>button")
    private WebElement okToEditing;
    @FindBy(css = "form-buttons lg-button[label='Cancel']>button")
    private WebElement cancelEditing;

    //template save
    @FindBy(css = "button.saveas-drop")
    private WebElement saveAsDrop;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(1)")
    private WebElement saveAsDraft;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(2)")
    private WebElement publishNow;
    @FindBy(css = "div.saveas-list.ng-scope>h3:nth-child(3)")
    private WebElement publishLater;
    //publish later modal
    @FindBy(css = "div.lg-modal")
    private WebElement dateOfPublishModal;
    @FindBy(css = "div.modal-dialog ng-form.input-form.ng-pristine")
    private WebElement dateSelect;
    @FindBy(css = "div.lg-single-calendar-toolbar>a:last-child")
    private WebElement nextMonthArrow;
    @FindBy(css = "div.lg-single-calendar-date-wrapper>div:nth-child(20)")
    private WebElement dayChosen;
    //save
    @FindBy(css = "button.pre-saveas")
    private WebElement saveTemplate;

    //Associate
    @FindBy(css = "table.lg-table.templateAssociation_table.ng-scope>tbody>tr:nth-child(2)>td>input")
    private WebElement theFirstAssociateGroup;
    @FindBy(css = "lg-button[label='Save']")
    private WebElement saveAssociate;

    //Search
    @FindBy(css = "lg-search>input-field input[placeholder='You can search by name, label and description']")
    private WebElement searchBox;
    @FindBy(css = "div.lg-search-icon.ng-scope")
    private WebElement searchIcon;


    public void back() {
        backButton.click();
    }

    public void okToActionInModal(boolean okToAction) {
        if (okToAction) {
            clickTheElement(okToEditing);
        } else {
            clickTheElement(cancelEditing);
        }
        waitForSeconds(3);
    }

    public void saveTemplateAs(String method) {
        scrollToBottom();
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

    public void associateWithDynamicGroups(String groupName) {
        searchBox.clear();
        searchBox.sendKeys(groupName);
        if (!theFirstAssociateGroup.isSelected()) {
            theFirstAssociateGroup.click();
        }
        scrollToBottom();
        saveAssociate.click();
    }

    public void search(String searchText) {
        searchBox.clear();
        searchBox.sendKeys(searchText);
        searchIcon.click();
    }

    @FindBy(css = "lg-button[label= 'Leave this page']>button")
    private WebElement leaveThisPageButton;

    public void leaveThisPage(){
        leaveThisPageButton.click();
    }

    @FindBy(css = "lg-button[label='Delete']>button")
    private WebElement deleteButton;

    public void deleteConfirm(){
        deleteButton.click();
    }
}
