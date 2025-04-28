package com.legion.pages.core.opusermanagement;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class OpsPortalWorkRolesPage extends BasePage {

    public OpsPortalWorkRolesPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a.ng-binding")
    private WebElement backButton;
    @FindBy(css = "lg-search>input-field.ng-isolate-scope>ng-form>input")
    private WebElement searchBox;
    @FindBy(css = "div.lg-search-icon")
    private WebElement searchButton;
    @FindBy(css = "table.lg-table.ng-scope>tbody>tr>td:first-child>lg-button")
    private List<WebElement> workRoleList;
    @FindBy(css = "table.lg-table.ng-scope>tbody>tr:nth-child(2)>td:first-child>lg-button")
    private WebElement theFirstWorkRoleInTheList;
    @FindBy(css = "div.lg-work-roles__placeholder-content.ng-binding")
    private WebElement noResultNotice;
    @FindBy(css = "lg-button[label='Edit']>button")
    private WebElement editButton;
    @FindBy(css = "lg-button[label='Add Work Role']>button")
    private WebElement addWorkRoleButton;

    @FindBy(css = "div.button-container lg-button[label='Cancel']>button")
    private WebElement cancelButton;
    // Cancel Dialog
    @FindBy(css = "h1.lg-modal__title>div")
    private WebElement cancelDialog;
    @FindBy(css = "lg-button[label='Keep editing']")
    private WebElement keepEditing;
    @FindBy(css = "lg-button[label='Yes']")
    private WebElement yesToCancelEditing;

    @FindBy(css = "div.button-container lg-button[label='Save']>button")
    private WebElement saveButton;

    @FindBy(css = "lg-button[label='Disable']")
    private WebElement disableButton;
    // Disable Dialog
    @FindBy(css = "div.lg-modal__title-icon.ng-binding")
    private WebElement disableDialog;
    @FindBy(css = "div.modal-content lg-button[label='Cancel']")
    private WebElement cancelDisableAction;
    @FindBy(css = "div.modal-content lg-button[label='OK']")
    private WebElement okToDisableAction;
    @FindBy(css = "div.lg-toast")
    private WebElement popUpMsg;

    @FindBy(css = ".lg-paged-search tr th")
    private List<WebElement> columns;
    @FindBy(css = "[label='Display Order'] > label")
    private WebElement displayOrderLabel;
    @FindBy(css = "[label='Display Order'] input")
    private WebElement displayOrderInput;
    @FindBy(css = "[ng-repeat*=workRole] td:nth-child(2)")
    private List<WebElement> displayOrders;
    @FindBy(css = "[ng-repeat*=workRole] td:nth-child(1)")
    private List<WebElement> workRoleNames;
    @FindBy(css = "[type=Submit]")
    private WebElement saveBtnOnDetails;
    @FindBy(css = ".lg-template-work-roles__drag-handle")
    private List<WebElement> dragHandles;
    @FindBy(css = "input[placeholder=\"Work role display name\"]")
    private WebElement workRoleNameInput;
    @FindBy(css = "[aria-label=\"Work Role Class\"]")
    private WebElement workRoleClassSelect;
    @FindBy(css = "[aria-label*=\"Hourly rate\"]")
    private WebElement hourlyRateInput;
    @FindBy(css = ".lg-pagination__arrow--right")
    private WebElement rightArrowBtn;

    public void goBack() {
        scrollToElement(backButton);
        backButton.click();
    }

    public void searchByWorkRole(String workRole) throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(searchBox, 15)) {
            searchBox.clear();
            searchBox.sendKeys(workRole);
            searchButton.click();
        } else {
            SimpleUtils.fail("Work Role Page: Search box failed to load!", false);
        }
    }

    public String getTheFirstWorkRoleInTheList() throws Exception {
        String workRoleLabel = "";
        if (isElementLoaded(theFirstWorkRoleInTheList, 15)) {
            workRoleLabel = theFirstWorkRoleInTheList.getAttribute("label");
        } else {
            SimpleUtils.fail("Work Role Page: The first work role failed to load in the list!", false);
        }
        return workRoleLabel;
    }

    public String getNoResultNotice() {
        String notice = noResultNotice.getText();
        return notice;
    }

    public void goToWorkRoleDetails() throws Exception {
        if (isElementLoaded(theFirstWorkRoleInTheList, 15)) {
            clickTheElement(theFirstWorkRoleInTheList);
        } else {
            SimpleUtils.fail("Work Role Page: The first work role failed to load in the list!", false);
        }
    }

    public void clickEditButton() throws Exception {
        if (isElementLoaded(editButton, 5)) {
            clickTheElement(editButton);
        }
    }

    public void clickDisableButton() {
        if (disableButton.isDisplayed() && disableButton.isEnabled()) {
            disableButton.click();
        }
    }

    public void editAnExistingWorkRole(String oldName) throws Exception {
        waitForSeconds(2);
        clickEditButton();
        searchByWorkRole(oldName);
        goToWorkRoleDetails();//go to work role details page
    }

    public void addNewWorkRole() throws Exception {
        waitForSeconds(2);
        clickEditButton();
        addWorkRoleButton.click();//go to work role details page
    }

    public void save() throws Exception {
        if(isElementDisplayed(saveButton)&&isClickable(saveButton,5)){
            saveButton.click();
            SimpleUtils.pass("Work Role Page: Click on Save button successfully!");
        }else{
            SimpleUtils.fail("Work Role Page: Save button failed to load!", false);
        }
    }

    //cancel editing
    public void cancel() {
        waitForSeconds(2);
        cancelButton.click();
    }

    public String getCancelDialogTitle() {
        return cancelDialog.getText();
    }

    public void keepEditing() {
        keepEditing.click();
    }

    public void cancelEditing() {
        waitForSeconds(2);
        yesToCancelEditing.click();
    }


    //disable action
    public void disableAWorkRole(String name) throws Exception {
        waitForSeconds(2);
        clickEditButton();
        searchByWorkRole(name);
        clickDisableButton();
    }

    public String getDisableDialogTitle() {
        return disableDialog.getText();// Disable Work Role
    }

    public void cancelDisableAction() {
        cancelDisableAction.click();
    }

    public void okToDisableAction() {
        waitForSeconds(2);
        okToDisableAction.click();
    }

    @FindBy (css = ".lg-pagination__arrow--right")
    private WebElement pageRightBtn;
    public List<String> getWorkRoleList() throws Exception {
        ArrayList<String> wr = new ArrayList<String>();
        for(WebElement workRole:workRoleList){
            wr.add(workRole.getAttribute("label"));
        }
        while (isElementLoaded(pageRightBtn, 5)
                && !pageRightBtn.getAttribute("class").contains("disabled")){
            clickTheElement(pageRightBtn);
            for(WebElement workRole:workRoleList){
                wr.add(workRole.getAttribute("label"));
            }
        }
        return wr;
    }

    public boolean isColumnVisibleOnWorkRoles(String columnName) throws Exception {
        boolean isVisible = false;
        if (areListElementVisible(columns, 5)) {
            for (WebElement column : columns) {
                if (column.getText().trim().equalsIgnoreCase(columnName)) {
                    isVisible = true;
                    break;
                }
            }
        }
        return isVisible;
    }

    public boolean isDisplayOrderInputShowInDetailPage() throws Exception {
        boolean isShow = false;
        try {
            if (isElementLoaded(displayOrderLabel, 5) && isElementLoaded(displayOrderInput, 5) &&
            displayOrderLabel.getText().equalsIgnoreCase("Display Order*")) {
                isShow = true;
            }
        } catch (Exception e) {
            // Do nothing
        }
        return isShow;
    }

    public boolean isSpecificValueOfDisplayOrderAllowed(String value) throws Exception {
        boolean isAllowed = false;
        displayOrderInput.clear();
        displayOrderInput.sendKeys(value);
        if (displayOrderInput.getAttribute("class").contains("ng-invalid")) {
            SimpleUtils.report(value + "is not be allowed!");
        } else if (displayOrderInput.getAttribute("class").contains("ng-valid")) {
            SimpleUtils.report(value + " is allowed!");
            isAllowed = true;
        }
        return isAllowed;
    }

    public void updateDisplayOrder(int order) throws Exception {
        displayOrderInput.clear();
        displayOrderInput.sendKeys(String.valueOf(order));
        saveBtnOnDetails.click();
    }

    public void sortByDisplayOrder(boolean isAsc) throws Exception {
        if (areListElementVisible(columns, 5)) {
            for (WebElement column: columns) {
                if (column.getText().trim().equalsIgnoreCase("Display Order")) {
                    WebElement displayOrder = column.findElement(By.tagName("div"));
                    if (isAsc && !displayOrder.getAttribute("dir").equalsIgnoreCase("asc")) {
                        clickTheElement(displayOrder);
                    }
                    if (!isAsc && !displayOrder.getAttribute("dir").equalsIgnoreCase("desc")) {
                        clickTheElement(displayOrder);
                    }
                }
            }
        } else {
            SimpleUtils.fail("Columns failed to load on Work Roles page!", false);
        }
    }

    public List<Integer> getDisplayOrders() throws Exception {
        List<Integer> displayOrderList = new ArrayList<>();
        if (areListElementVisible(displayOrders, 5)) {
            for (WebElement order : displayOrders) {
                displayOrderList.add(Integer.parseInt(order.getText()));
            }
        }
        return displayOrderList;
    }

    public List<String> getWorkRoleNames() throws Exception {
        List<String> workRoleList = new ArrayList<>();
        if (areListElementVisible(workRoleNames, 5)) {
            for (WebElement workRole : workRoleNames) {
                workRoleList.add(workRole.getText());
            }
        }
        return workRoleList;
    }

    public void getWholeWorkRoleNDisplayOrderMap(HashMap<String, Integer> workRoleNOrders) throws Exception {
        if (areListElementVisible(workRoleNames, 5) && areListElementVisible(displayOrders, 5)) {
            for (int i = 0; i < workRoleNames.size(); i++) {
                workRoleNOrders.put(workRoleNames.get(i).findElement(By.cssSelector("span.ng-binding")).getText().toLowerCase()
                        .replaceAll(" ", ""), Integer.valueOf(displayOrders.get(i).getText()));
            }
            if (isElementLoaded(rightArrowBtn, 3) && !rightArrowBtn.getAttribute("class").contains("lg-pagination__arrow--disabled")) {
                clickTheElement(rightArrowBtn);
                waitForSeconds(1);
                getWholeWorkRoleNDisplayOrderMap(workRoleNOrders);
            }
        }
    }

    public void inputWorkRoleDetail(String workRole, int displayOrder) throws Exception {
        if (isElementLoaded(workRoleNameInput, 3) && isElementLoaded(workRoleClassSelect, 3)
                && isElementLoaded(displayOrderInput, 3)) {
            workRoleNameInput.clear();
            workRoleNameInput.sendKeys(workRole);
            selectByVisibleText(workRoleClassSelect, "Deployed");
            try {
                if (isElementLoaded(hourlyRateInput, 3)) {
                    hourlyRateInput.clear();
                    hourlyRateInput.sendKeys("1");
                }
            } catch (Exception e) {
                // Do nothing
            }
            displayOrderInput.clear();
            displayOrderInput.sendKeys(String.valueOf(displayOrder));
            clickTheElement(saveBtnOnDetails);
        } else {
            SimpleUtils.fail("New Work Role page failed to load!", false);
        }
    }
}
