package com.legion.pages.core.opusermanagement;

import com.legion.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class AccessRolePage extends BasePage {
    public AccessRolePage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a")
    private WebElement backButton;
    //add
    @FindBy(css = "div.lg-tabs>nav>div:nth-child(2)")
    private WebElement accessRoleTab;

    @FindBy(css = "lg-button[label='Add New Role']>button")
    private WebElement addNewRoleButton;
    //New Access Role page
    @FindBy(css = "div.title-breadcrumbs h1")
    private WebElement title;
    @FindBy(css = "content-box h2")
    private WebElement roleDetailsSubTitle;
    //labels
    @FindBy(css = "input-field[label]>label")
    private List<WebElement> inputFieldLabels;
    @FindBy(css = "input-field[label='Role Title']>label")
    private WebElement roleTitleLabel;
    @FindBy(css = "input-field[label='Copy Role Permission From']>label")
    private WebElement permissionFormLabel;
    @FindBy(css = "input-field[label='Description']>label")
    private WebElement descriptionLabel;
    //input
    @FindBy(css = "input-field[label='Role Title']>ng-form>input")
    private WebElement roleTitleInput;
    @FindBy(css = "input-field[label='Copy Role Permission From'] select")//select
    private WebElement permissionSelect;
    @FindBy(css = "input-field[label='Description']>ng-form>input")
    private WebElement descriptionInput;
    @FindBy(css = "div.lg-input-error>span")
    private WebElement errorMes;
    //buttons
    @FindBy(css = "lg-button[label='Cancel']>button")
    private WebElement cancelButton;
    @FindBy(css = "lg-button[label='next']>button")
    private WebElement nextButton;

    //Role Permission page
    @FindBy(css = "content-box h2")
    private WebElement rolePermissionSubTitle;//Role Permissions
    @FindBy(css = "lg-button[label=\"create\"]>button")
    private WebElement createButton;
    @FindBy(css = "collapsible-title>i+span")
    private List<WebElement> rolePermission;

    //Verify permission role new added has been displayed
    //@FindBy(css = "div.group.ng-scope:nth-child(1)>div.group-header-row.dif>i")
    @FindBy(css = "lg-access-role-permissions > div > div.scroll-wrapper > div:nth-child(1) > div > i")
    private WebElement controlArrow;
    @FindBy(css = "div.table-row.header-row>div")
    private List<WebElement> roleNames;

    public void back() {
        backButton.click();
        waitForSeconds(1);
    }

    public void switchToAccessRole() {
        accessRoleTab.click();
    }

    public boolean isAccessRoleTabDisplayed() {
        return accessRoleTab.isDisplayed();
    }

    public boolean isAddNewRoleButtonDisplayed() {
        return addNewRoleButton.isDisplayed();
    }

    public void addNewRole() {
        if (isAddNewRoleButtonDisplayed()) {
            addNewRoleButton.click();
        }
    }

    public String getNewAccessRolePageTitle() {
        return title.getText();//New Access Role
    }

    public String getContentBoxSubTitle() {
        return roleDetailsSubTitle.getText();//Role Details
    }

    public void clearRoleTitle() {
        roleTitleInput.clear();
    }

    public void setRoleTitle(String RoleT) {
        clearRoleTitle();
        roleTitleInput.sendKeys(RoleT);
    }

    public String getErrorMessage() {
        return errorMes.getText();
    }

    public boolean isErrorMesgDisplayed() {
        return isElementDisplayed(errorMes);
    }

    public void copyRolePermissionFrom(String permission) {
        Select select = new Select(permissionSelect);
        select.selectByVisibleText(permission);
    }

    public void setDescription(String Desc) {
        descriptionInput.clear();
        descriptionInput.sendKeys(Desc);
    }

    public boolean IsNextButtonEnabled() {
        return nextButton.isEnabled();
    }

    public void cancelNewAccessRole() {
        scrollToBottom();
        cancelButton.click();
        waitForSeconds(1);
    }

    public void next() {
        if (IsNextButtonEnabled()) {
            nextButton.click();
        } else {
            System.out.println("The next button is not enabled!");
        }
    }

    public String getRolePermissionTitle() {
        return rolePermissionSubTitle.getText();
    }

    public void createNewAccessRole() {
        scrollToBottom();
        createButton.click();
        waitForSeconds(5);
    }

    public ArrayList<String> getOptionsOfRolePermission() {
        Select select = new Select(permissionSelect);
        List<WebElement> options = select.getOptions();
        return getWebElementsText(options);
    }

    public ArrayList<String> getAllRolePermission() {
        return getWebElementsText(rolePermission);
    }

    public ArrayList<String> getRoleDetailLabels() {
        return getWebElementsText(inputFieldLabels);
    }

    public ArrayList<String> getRoleNames() {
        return getWebElementsText(roleNames);
    }

    public void expandControls() {
        waitForSeconds(2);
        controlArrow.click();
    }


}
