package com.legion.pages.core.opusermanagement;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class DynamicEmployeePage extends BasePage {
    public DynamicEmployeePage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a")
    private WebElement backButton;
    //add
    @FindBy(css = "div.col-sm-2.templateAssociation_action.gray>lg-button>button")
    private WebElement addGroupButton;

    //Manage Dynamic Group Modal
    @FindBy(css = "div.lg-modal__title-icon.ng-binding")
    private WebElement dynamicGroupModalTitle;
    @FindBy(css = "input-field[label='Group Name'] input")
    private WebElement groupName;
    @FindBy(css = "div.fl-left.groupDescription input")
    private WebElement groupDesc;
    @FindBy(css = "input-field[label='Labels']>ng-form")
    private WebElement labels;
    @FindBy(css = "lg-search[placeholder='Search Label'] input")
    private WebElement labelSearchBox;
    @FindBy(css = "div.lg-search-icon.ng-scope")
    private WebElement searchButton;
    @FindBy(css = "div.mt-10.new-label")
    private WebElement newLabel;
    @FindBy(css = ".item .input-label")
    private WebElement existingLabel;
    //Work Role/Exempt/Minor/Employment Type/Employment Status/Badge/Custom
    @FindBy(css = "input-field[placeholder='Select one']")
    private WebElement criteria;
    @FindBy(css = ".lg-search-options__option")
    private List<WebElement> criteriaOptions;
    @FindBy(css = "[on-open=\"$ctrl.options && $ctrl.onOpenOptionList()\"]")
    private WebElement valueButton;
    @FindBy(css = ".select-list-item input-field")//need to get attribute label
    private List<WebElement> valueList;
    @FindBy(css = ".select-list-item:nth-child(1) input")
    private WebElement theFirstCriteriaOption;
    @FindBy(css = "div.ml-10.fl-left.addGroupBtn>lg-button>button")
    private WebElement addMoreButton;
    @FindBy(id = "omjob")
    private WebElement textarea;
    @FindBy(css = "lg-button[label='Test']>button")
    private WebElement test;
    @FindBy(css = "lg-button[label='Cancel']>button")
    private WebElement cancelButtonInModal;
    @FindBy(css = "lg-button[label='OK']>button")
    private WebElement okButtonInModal;

    //search a group
    @FindBy(css = "div.lg-tab-toolbar__search>lg-search input")
    private WebElement searchBox;
    //edit
    @FindBy(css = "td.tr lg-button:nth-child(1)>button")
    private WebElement editButton;
    //remove
    @FindBy(css = "td.tr lg-button:nth-child(2)>button")
    private WebElement removeButton;
    @FindBy(css = "p.lg-modal__content.lg-modal__text")//Are you sure you want to remove this dynamic group?
    private WebElement removeModalContent;
    @FindBy(css = "modal lg-button[label='Cancel']>button")
    private WebElement cancelInRemoveModal;
    @FindBy(css = "modal lg-button[label='Remove']>button")
    private WebElement removeInRemoveModal;
    @FindBy(css = ".mappingLocation [ng-if*='isUserDynamicGroup']")
    private WebElement userMatching;

    public void back() {
        backButton.click();
    }

    public void addGroup() {
        addGroupButton.click();
    }

    public String getModalTitle() throws Exception{
        if (isElementLoaded(dynamicGroupModalTitle, 10)) {
            return dynamicGroupModalTitle.getText();
        } else {
            return null;
        }
    }

    public List<String> getCriteriaValues(String criteriaName) throws Exception {
        if (isElementLoaded(criteria, 5)) {
            clickTheElement(criteria);
            if (areListElementVisible(criteriaOptions, 5)) {
                for (WebElement option : criteriaOptions) {
                    if (option.getText().equalsIgnoreCase(criteriaName)) {
                        clickTheElement(option);
                        break;
                    }
                }
            }
        }
        valueButton.click();
        ArrayList<String> value = new ArrayList<String>();
        if (areListElementVisible(valueList, 10)) {
            for (WebElement val : valueList) {
                value.add(val.getAttribute("label"));
            }
        }
        return value;
    }

    public void editEmployeeGroup(String name, String desc, String lab, String criName) throws Exception {
        groupName.clear();
        groupName.sendKeys(name);
        groupDesc.clear();
        groupDesc.sendKeys(desc);
        setLabel(lab);
        setCriteria(criName);
        test.click();
        waitUntilElementIsVisible(userMatching);
    }

    public void setLabel(String lab) {
        labels.click();
        labelSearchBox.clear();
        labelSearchBox.sendKeys(lab);
        try {
            if (isElementLoaded(newLabel, 5)) {
                newLabel.click();
            } else if (isElementLoaded(existingLabel, 5)) {
                existingLabel.click();
            }
        } catch (Exception e) {
            // Do nothing
        }
        labels.click();
    }

    public void setCriteria(String criteriaName) throws Exception {
        if (isElementLoaded(criteria, 5)) {
            clickTheElement(criteria);
            if (areListElementVisible(criteriaOptions, 5)) {
                for (WebElement option : criteriaOptions) {
                    if (option.getText().equalsIgnoreCase(criteriaName)) {
                        clickTheElement(option);
                        break;
                    }
                }
            }
            valueButton.click();
            if (isElementLoaded(theFirstCriteriaOption, 5)) {
                theFirstCriteriaOption.click();
            }
            valueButton.click();
        }
    }

    public void searchGroup(String nameLabelOrDesc) {
        searchBox.clear();
        searchBox.sendKeys(nameLabelOrDesc);
    }

    public void edit() throws Exception {
        if (isElementLoaded(editButton, 10)) {
            clickTheElement(editButton);
        }
    }

    //remove
    public void remove() {
        removeButton.click();
    }

    public String getContentOfRemoveModal() {
        return removeModalContent.getText();
    }

    public void removeTheGroup() {
        removeInRemoveModal.click();
    }

    public void cancelRemove() throws Exception {
        if (isElementLoaded(cancelInRemoveModal, 5)) {
            clickTheElement(cancelInRemoveModal);
        }
    }

    //create
    public void cancelCreating() throws Exception {
        if (isElementLoaded(cancelButtonInModal, 5)) {
            clickTheElement(cancelButtonInModal);
        }
    }

    public void saveCreating() {
        waitForSeconds(3);
        clickTheElement(okButtonInModal);
    }

    @FindBy(css = "[ng-repeat=\"group in filterdynamicGroups\"]")
    private List<WebElement> dynamicGroups;
    public void removeSpecificGroup(String groupName) {
        String removeWarningMsg = "Are you sure you want to remove this dynamic employee group?";
        searchGroup(groupName);
        if (areListElementVisible(dynamicGroups, 5)) {
            for (WebElement group : dynamicGroups){
                clickTheElement(group.findElement(By.cssSelector("td.tr lg-button:nth-child(2)>button")));
                Assert.assertEquals(getContentOfRemoveModal(), removeWarningMsg, "Failed to open the remove modal!");
                removeTheGroup();
            }
        } else
            SimpleUtils.report("The group: " +groupName +" is not exists! ");
    }

    @FindBy(css = "div.lg-filter input-field[placeholder='Labels'] ng-form")
    private WebElement labelInput;
    @FindBy(css = "div.lg-filter__category-items div input-field")
    private List<WebElement> labelList;

    public void searchGroupWithLabel(String labelName) {
        labelInput.click();
        for (WebElement label : labelList) {
            if (label.findElement(By.cssSelector("label.input-label")).getText().trim().contains(labelName)) {
                label.click();
                break;
            }
        }
    }

    @FindBy(css = "tr[ng-repeat='group in filterdynamicGroups']")
    private List<WebElement> groupList;

    public void verifyGroupIsSearched(String groupName) {
        boolean flag = false;
        for (WebElement group : groupList) {
            if (group.findElement(By.cssSelector("td:nth-child(1)")).getText().trim().contains(groupName)) {
                SimpleUtils.pass("The group: " + groupName + " is exists! ");
                flag = true;
                break;
            }
        }
        if (!flag) {
            SimpleUtils.fail("The group: " + groupName + " is exists! ", true);
        }
    }
}
