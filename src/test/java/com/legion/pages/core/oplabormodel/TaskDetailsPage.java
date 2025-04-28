package com.legion.pages.core.oplabormodel;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class TaskDetailsPage extends BasePage {
    public TaskDetailsPage() {
        PageFactory.initElements(getDriver(), this);
    }

    //added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a.ng-binding")
    private WebElement backButton;
    @FindBy(css = "sub-content-box[box-title='Task Details']>h2")//Task Details
    private WebElement subTitle;
    @FindBy(css = "input-field[label='Name'] input")
    private WebElement name;
    @FindBy(css = "input-field[label='Description'] input")
    private WebElement description;
    @FindBy(css = "input-field[label='Type'] select")//Labor Meal Rest Filler
    private WebElement type;
    ////icon part
    @FindBy(css = "input-field[label='Icon']")
    private WebElement icon;
    @FindBy(css = "div.lg-picker-input__wrapper.lg-ng-animate lg-search input")
    private WebElement iconSearchBox;
    @FindBy(css = "div.lg-search-icon")
    private WebElement iconSearchButton;
    @FindBy(css = "div.lg-task-icons__wrapper.ng-scope>div")//25
    private List<WebElement> iconOption;

    @FindBy(css = "input-field[label='Demand-Based'] input")
    private WebElement demandBasedCheckBox;
    @FindBy(css = "input-field[label='Flexible Task'] input")
    private WebElement flexibleTaskCheckBox;

    //label part
    @FindBy(css = "lg-button[label='Add Label']>button")
    private WebElement addLabel;
    @FindBy(css = "input-field[placeholder='Enter a label'] input")
    private WebElement labelInputBox;
    @FindBy(css = "div.label-container>div>div:last-child img")
    private WebElement labelAddIcon;

    //add rule
    @FindBy(css = "lg-button[label='Add Rule']>button")
    private WebElement addRuleButton;
    @FindBy(css = "lg-button[ng-click=\"$ctrl.addTeamRule($event)\"]>button")
    private WebElement addTeamMemberRuleButton;
    @FindBy(css = "h1.lg-modal__title")//When should the task be done?
    private WebElement modalTitle;
    @FindBy(css = "div.lg-button-group>div:nth-child(1)>span")
    private WebElement standardTab;
    @FindBy(css = "div.lg-button-group>div:nth-child(2)>span")
    private WebElement customTab;
    @FindBy(css = "input-field[options=\"$ctrl.demandDriverTypeChoices\"] select")
    private WebElement driverType;
    @FindBy(css = "input-field[options=\"$ctrl.sourceTypeChoices\"] select")
    private WebElement sourceType;
    @FindBy(css = "input-field[options=\"$ctrl.categoryChoices\"] select")
    private WebElement categoryType;
    @FindBy(css = "input-field[value=\"$ctrl.capacity\"] input")
    private WebElement capacityInput;
    @FindBy(css = "div.lg-toast--error>p")
    private WebElement warnigPopUp;
    @FindBy(css = "tr[ng-repeat=\"tr in $ctrl.task.teamMemberRules\"]")
    private List<WebElement> teamMemberRules;
    //
    @FindBy(css = "div.mt-20>span.dib.ml- select")
    private WebElement startEnd;
    @FindBy(css = "div.mt-20>span.dib.ng-scope select")
    private WebElement atBeforeAfter;
    @FindBy(css = "div.mt-20>span.dib:nth-child(4) select")
    private WebElement atWhatSituation;
    @FindBy(css = "div.mt-20>span.dib:nth-child(6) select")
    private WebElement duringWhatTime;
    @FindBy(css = "div.col-sm-3.ng-scope>input-field")//8 index:0~7
    private List<WebElement> selectDay;
    @FindBy(css = "modal lg-button[label='Cancel']>button")
    private WebElement cancelRuleEditing;
    @FindBy(css = "modal lg-button[label='Add Rule']>button")
    private WebElement saveRuleEditing;


    @FindBy(css = "lg-button[label='Cancel']>button")
    private WebElement cancelButton;
    @FindBy(css = "lg-button[label='Add Task']>button")
    private WebElement addTaskButton; // displayed when adding a new task
    @FindBy(css = "lg-button[label='Save']>button")
    private WebElement saveButton; // displayed when editing an existing task

    public void setIcon(String searchText, int optionIndex) {
        icon.click();
        waitForSeconds(3);
        iconSearchBox.sendKeys(searchText);//cart
        iconSearchButton.click();
        iconOption.get(optionIndex).click();//(full:25; search:7)
    }

    public void addNewLabel(String label) {
        addLabel.click();
        labelInputBox.sendKeys(label);
        labelAddIcon.click();
    }

    public void cancel() {
        cancelButton.click();
    }

    //used when adding a new task
    public void saveAdding() {
        scrollToBottom();
        addTaskButton.click();
    }

    //used when editing an existing task
    public void saveEditing() {
        scrollToBottom();
        saveButton.click();
    }

    //rule part
    public void addRules() {
        addRuleButton.click();
        standardTab.click();
        Select select1 = new Select(startEnd);
        select1.selectByVisibleText("Start");
        Select select2 = new Select(atBeforeAfter);
        select2.selectByVisibleText("at");
        Select select3 = new Select(atWhatSituation);
        select3.selectByVisibleText("Opening");
        Select select4 = new Select(duringWhatTime);
        select4.selectByVisibleText("Business Hours");
        selectDay.get(1).click();
        selectDay.get(2).click();
        selectDay.get(3).click();
    }

    public void saveRule() {
        saveRuleEditing.click();
    }

    public void cancelRule() {
        cancelRuleEditing.click();
    }


    public void editTask(String taskName, String desc, String tp, String iconSearch, int iconIndex) {
        name.clear();
        name.sendKeys(taskName);
        description.clear();
        description.sendKeys(desc);
        Select tpSelect = new Select(type);
        getDriver().findElement(By.xpath("//ng-transclude/sub-content-box/ng-transclude/div[1]/div[3]/input-field/ng-form/div[2]")).click();
        tpSelect.selectByVisibleText(tp);//Labor Meal Rest Filler
        setIcon(iconSearch, iconIndex);//cart buy 0~2
    }


    public void setDemandBased() {
        if (!demandBasedCheckBox.isSelected()) {
            demandBasedCheckBox.click();
        }
    }

    public void setFlexibleTaskCheckBox() {
        if (demandBasedCheckBox.isSelected()) {
            demandBasedCheckBox.click();
            flexibleTaskCheckBox.click();
        }
    }

    public void addRulesForDemandTask(HashMap<String, String> rulesInformation) {
        WebElement addTeamMemberRuleButton = getDriver().findElement(By.cssSelector("lg-button[ng-click=\"$ctrl.addTeamRule($event)\"]>button"));
        addTeamMemberRuleButton.click();
        standardTab.click();
        capacityInput.clear();
        capacityInput.sendKeys(rulesInformation.get("capacity"));

        Select driverTypeSelect = new Select(driverType);
        Select sourceTypeSelect = new Select(sourceType);
        Select categoryTypeSelect = new Select(categoryType);
        driverTypeSelect.selectByVisibleText(rulesInformation.get("driverType"));
        sourceTypeSelect.selectByVisibleText(rulesInformation.get("sourceType"));
        categoryTypeSelect.selectByVisibleText(rulesInformation.get("categoryType"));
    }

    public String getWarningMessage() throws Exception {
        String messageInfo = "";

        if (isElementLoaded(warnigPopUp, 5)){
            messageInfo = warnigPopUp.getAttribute("innerText");
        }else{
            SimpleUtils.report("No warning message pop up!");
        }
        return messageInfo;
    }

    public void removeRule(int index) {
        if(areListElementVisible(teamMemberRules, 2)){
            WebElement removeBtn = teamMemberRules.get(index).findElement(By.cssSelector("td lg-button[label=\"Remove\"]"));
            if(removeBtn != null)
                removeBtn.click();
            else
                SimpleUtils.fail("No remove button found!", false);
        }else{
            SimpleUtils.fail("No team member rules found!", false);
        }
    }
}
