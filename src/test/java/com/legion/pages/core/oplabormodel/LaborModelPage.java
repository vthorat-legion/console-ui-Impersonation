package com.legion.pages.core.oplabormodel;

import com.legion.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;

import static com.legion.utils.MyThreadLocal.getDriver;

public class LaborModelPage extends BasePage {
    public LaborModelPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a.ng-binding")
    private WebElement backButton;

    //new template
    @FindBy(css = "lg-button[label='New Template']>button")
    private WebElement newTemplateButton;
    @FindBy(css = "div.lg-modal>h1")//Creating Labor Model Template
    private WebElement newTemplateModalTitle;
    @FindBy(css = "div.lg-modal input-field[label='Name this template'] input")
    private WebElement newTempName;
    @FindBy(css = "div.lg-modal input-field[label='Description'] textarea")
    private WebElement newTempDesc;
    @FindBy(css = "div.lg-modal lg-button[label='Cancel']>button")
    private WebElement cancelCreating;
    @FindBy(css = "div.lg-modal lg-button[label='Continue']>button")
    private WebElement continueCreating;

    //search
    @FindBy(css = "div.lg-tab-toolbar__search input")
    private WebElement searchBox;
    @FindBy(css = "div.lg-search-icon")
    private WebElement searchIcon;
    @FindBy(css = "div.lg-table>div.lg-templates-table-improved__grid-row:nth-child(2)>div.lg-templates-table-improved__grid-column:nth-child(2)>lg-button")//label
    private WebElement firstTemplateNameOfList;
    @FindBy(css = "table.lg-table>tbody:nth-child(2)>tr>td:nth-child(3)>lg-eg-status") //type
    private WebElement firstTemplateStatusOfList;
    @FindBy(css = "table.lg-table>tbody:nth-child(2)>tr>td:nth-child(4)")
    private WebElement firstTemplateCreatorOfList;


    public void back() {
        scrollToTop();
        backButton.click();
    }

    public void newTemplate(String templateName, String templateDesc) {
        newTemplateButton.click();
        newTempName.sendKeys(templateName);
        newTempDesc.sendKeys(templateDesc);
    }

    public void cancelCreating() {
        cancelCreating.click();
    }

    public void continueCreating() {
        continueCreating.click();
    }

    public void searchTemplate(String searchText) { //search text can be template name creator and status
        searchBox.clear();
        searchBox.sendKeys(searchText);
        searchIcon.click();
    }

    public ArrayList<String> getSearchResult() {
        ArrayList<String> results = new ArrayList<String>();
        results.add(firstTemplateNameOfList.getAttribute("label"));
        results.add(firstTemplateStatusOfList.getAttribute("type"));
        results.add(firstTemplateCreatorOfList.getText());
        return results;
    }

    public void clickIntoDetails() {
        firstTemplateNameOfList.click();
    }

}
