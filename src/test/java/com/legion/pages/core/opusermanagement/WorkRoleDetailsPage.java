package com.legion.pages.core.opusermanagement;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class WorkRoleDetailsPage extends BasePage {
    public WorkRoleDetailsPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a.ng-binding")
    private WebElement backButton;
    @FindBy(css = "div.lg-page-heading>h1")//'New Work Role'
    private WebElement pageHeading;

    @FindBy(css = "div.wrc.ng-scope h2")//' Work Role Details'
    private WebElement contentBoxTitle;
    @FindBy(css = "input[placeholder='Work role display name']")
    private WebElement name;
    @FindBy(css = "div.center-details>div")
    private WebElement color;
    //select a color
    @FindBy(css = "span.settings-create-work-role-color-circle")
    private List<WebElement> colors;

    @FindBy(css = "input-field[label='Work Role Class'] select")
    private WebElement workRoleClass;
    @FindBy(css = "input-field[label='Hourly rate']>ng-form>input")
    private WebElement hourlyRate;

    //assignmentRules
    @FindBy(css = "span.settings-work-role-details-edit-add-icon.dropdown")
    private WebElement assignmentRuleAddButton;
    @FindBy(id = "workRoleConstraintDropDown")
    private WebElement teamMemberTitleButton;
    @FindBy(css = "div.dropdown-menu.dropdown-menu-left>ul>li:nth-child(3)>a")
    //District Manager/Manager/MGR/Market Director-Ops/Senior Ambassador/
    private WebElement teamMemberTitleOption;
    @FindBy(id = "timeConstraintDropDown")
    private WebElement timeRuleAppliesButton;
    @FindBy(css = "div.dropdown.settings-dropdown.open>ul>li:nth-child(3)>a")
    //During Opening Operation Hours/During Opening Hours/During Peak Hours/At all Hours
    private WebElement timeRuleOption;
    @FindBy(id = "limitConstraintDropDown")
    private WebElement conditionButton;
    @FindBy(css = "div.dropdown.settings-dropdown-170.fl.mr-10>ul>li:nth-child(3)>a")//Up to/At least/Exactly/
    private WebElement conditionOption;
    @FindBy(css = "div.settings-display-inline.ml-10.mt-33.fl>input")
    private WebElement numberOfShitsOrHours;
    @FindBy(id = "unitConstraintDropDown")
    private WebElement type;
    @FindBy(css = "#unitConstraintDropDown+ul>li:nth-child(2)>a")//Hours/Shift
    private WebElement typeOption;
    @FindBy(id = "intervalConstraintDropDown")
    private WebElement frequency;
    @FindBy(xpath = "//ul[@aria-labelledby ='intervalConstraintDropDown']/li")
    private WebElement week;
    @FindBy(css = "div.settings-display-inline.ml-10.fl>div.row input")
    private WebElement priority;
    @FindBy(css = "lg-button-group>div>div:first-child")
    private WebElement noBadge;
    @FindBy(css = "lg-button-group>div>div:nth-child(2)")
    private WebElement badgeRequired;
    @FindBy(css = "lg-search input")
    private WebElement badgeSearchBox;
    @FindBy(css = "div.lg-search-icon.ng-scope")
    private WebElement badgeSearchButton;
    @FindBy(css = "div.badges-list-scroll input")
    private WebElement badgeCheckBox;
    //save and delete
    @FindBy(css = "div.settings-work-rule-delete-icon")
    private WebElement deleteAssignRuleIcon;
    @FindBy(css = "div.settings-work-rule-save-icon")
    private WebElement saveAssignRuleIcon;

    //save and cancel work role
    @FindBy(css = "lg-button[label='Cancel']")
    private WebElement cancelButton;
    @FindBy(css = "lg-button[label='Save']")
    private WebElement saveButton;

    public void editWorkRoleDetails(String workRoleName, int colorIndex, String roleClass, String hRate) {
        waitForSeconds(2);
        name.clear();
        name.sendKeys(workRoleName);
        color.click();
        colors.get(colorIndex).click();
        Select select = new Select(workRoleClass);
        select.selectByVisibleText(roleClass);
//        hourlyRate.clear();
//        hourlyRate.sendKeys(hRate);
    }

    public void addAssignmentRule(String num, String pri, String badgeText) {
        assignmentRuleAddButton.click();
        teamMemberTitleButton.click();
        teamMemberTitleOption.click();
        timeRuleAppliesButton.click();
        timeRuleOption.click();
        conditionButton.click();
        conditionOption.click();
        numberOfShitsOrHours.sendKeys(num);
        type.click();
        typeOption.click();
        frequency.click();
        week.click();
        priority.clear();
        priority.sendKeys(pri);
        setBadge(badgeText);//2098
    }

    public void saveAssignRule() {
        scrollToBottom();
        saveAssignRuleIcon.click();
    }

    public void cancelAssignRule() {
        deleteAssignRuleIcon.click();
    }

    public void setBadge(String badgeText) {
        badgeRequired.click();
        badgeSearchBox.sendKeys(badgeText);
        badgeSearchButton.click();
        badgeCheckBox.click();
    }

    public void submit() {
        waitForSeconds(2);
        saveButton.click();
    }

    public void cancel() {
        cancelButton.click();
    }

    //check jon title in work role assignment rule
    @FindBy(css = "input[placeholder = 'Please enter characters to filter']")
    private WebElement teamMemberSearchBox;
    @FindBy(css = "a.ng-binding")
    private WebElement teamMemberSearchResult;

    public void goToTeamMemberSearchBox() throws Exception{
        assignmentRuleAddButton.click();
        teamMemberTitleButton.click();
    }

    public void searchTeamMember(String jobTitleName) throws Exception{
        teamMemberSearchBox.clear();
        teamMemberSearchBox.sendKeys(jobTitleName);
        if(isElementLoaded(teamMemberSearchResult,5)){
            SimpleUtils.pass("Job title search result successfully");
        }else
            SimpleUtils.fail("Job title loaded failed",false);
    }
}
