package com.legion.pages.core.OpsPortal;

import com.legion.pages.BasePage;
import com.legion.pages.OpsPortaPageFactories.SettingsAndAssociationPage;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.ro.Si;
import org.apache.commons.collections.ListUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

import static com.legion.tests.TestBase.refreshPage;
import static com.legion.utils.MyThreadLocal.getDriver;

public class OpsPortalSettingsAndAssociationPage extends BasePage implements SettingsAndAssociationPage {
    public OpsPortalSettingsAndAssociationPage() {
        PageFactory.initElements(getDriver(), this);
    }

    public enum requiredFieldsForLocationGroup {
        ConfigType("Config Type"),
        District("District"),
        Country("Country"),
        State("State"),
        City("City"),
        LocationName("Location Name"),
        LocationId("Location Id"),
        LocationType("Location Type"),
        UpperField("UpperField");
        private final String value;

        requiredFieldsForLocationGroup(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum requiredFieldsForEmployeeGroup {
        WorkRole("Work Role"),
        JobTitle("Job Title"),
        Country("Country"),
        State("State"),
        City("City"),
        EmploymentType("Employment Type"),
        EmploymentStatus("Employment Status"),
        Exempt("Exempt"),
        Minor("Minor"),
        Badge("Badge");
        private final String value;

        requiredFieldsForEmployeeGroup(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    @FindBy(css = ".lg-tabs__nav .lg-tabs__nav-item")
    private List<WebElement> tabsOnTheTemplateListPage;
    @Override
    public void goToTemplateListOrSettings(String tab) throws Exception {
        if (areListElementVisible(tabsOnTheTemplateListPage, 10)){
            if (tab.toLowerCase().contains("setting") && tabsOnTheTemplateListPage.size()==2){
                clickTheElement(tabsOnTheTemplateListPage.get(1));
                SimpleUtils.pass("Clicked on settings tab!");
            } else {
                clickTheElement(tabsOnTheTemplateListPage.get(0));
                SimpleUtils.pass("Clicked on template list tab!");
            }
        } else {
            SimpleUtils.fail("There is no tab showing!", false);
        }
    }

    @FindBy(css = "lg-tab[tab-title=\"Settings\"] .title")
    private WebElement titleForSettingTab;
    @Override
    public void verifyTitleOnTheSettingsPage(String groupType) throws Exception {
        if (isElementLoaded(titleForSettingTab, 10)){
            if (titleForSettingTab.getText().contains("Required fields for Dynamic " + groupType + " Group")){
                SimpleUtils.pass("Title is expected!");
            } else {
                SimpleUtils.fail("Title is not expected! actual is: " + titleForSettingTab.getText(), false);
            }
        } else {
            SimpleUtils.fail("There is no title!", false);
        }
    }

    @FindBy(css = ".templateAssociation_titleText")
    private WebElement titleForAssociationTab;
    @Override
    public void verifyTitleOnTheSAssociationPage(String groupType) throws Exception {
        if (isElementLoaded(titleForAssociationTab, 10)){
            if (titleForAssociationTab.getText().contains("Dynamic " + groupType + " Groups")){
                SimpleUtils.pass("Title is expected!");
            } else {
                SimpleUtils.fail("Title is not expected! actual is: " + titleForSettingTab.getText(), false);
            }
        } else {
            SimpleUtils.fail("There is no title!", false);
        }
    }

    @FindBy(css = "content-box[ng-if*=\"enableDynamicGroupForAssociation \"] lg-button[label=\"Edit\"]")
    private WebElement editDynamicGroupBtn;
    @FindBy(css = "content-box[ng-if*=\"enableDynamicGroupForAssociation \"] lg-button[label=\"Done\"] button")
    private WebElement doneDynamicGroupBtn;
    @FindBy(css = "content-box[ng-if*=\"enableDynamicGroupForAssociation \"] lg-button[label=\"Cancel\"]")
    private WebElement cancelDynamicGroupBtn;
    @FindBy(css = "content-box[ng-if*=\"enableDynamicGroupForAssociation \"] lg-button[label=\"Remove\"]")
    private WebElement removeDynamicGroupBtn;

    @Override
    public void clickOnEditBtnForDynamicGroupOnAssociationPage() throws Exception {
        if (isElementLoaded(editDynamicGroupBtn, 10)){
            clickTheElement(editDynamicGroupBtn);
            if (isElementLoaded(doneDynamicGroupBtn, 10)){
                SimpleUtils.pass("Edit mode!");
            } else {
                SimpleUtils.fail("Fail to go into edit mode!", false);
            }
        } else {
            SimpleUtils.fail("Edit dynamic group button fail to load!", false);
        }
    }

    @FindBy(css = "lg-button[label=\"Remove\"] button")
    private WebElement removeConfirmButton;
    @Override
    public void clickOnRemoveBtnToRemoveDynamicGroupOnAssociationPage() throws Exception {
        if (isElementLoaded(removeDynamicGroupBtn, 10)){
            clickTheElement(removeDynamicGroupBtn);
            if (isElementLoaded(removeConfirmButton, 10)){
                clickTheElement(removeConfirmButton);
                waitForSeconds(1);
            }
            if (isElementLoaded(addDynamicGroupBtn, 10)){
                SimpleUtils.pass("Deleted!");
            } else {
                SimpleUtils.fail("Fail to delete the group!", false);
            }
        } else {
            SimpleUtils.fail("Remove dynamic group button fail to load!", false);
        }
    }

    @Override
    public void clickOnDoneBtnForDynamicGroupOnAssociationPage() throws Exception {
        if (isElementLoaded(doneDynamicGroupBtn, 10)){
            clickTheElement(doneDynamicGroupBtn);
        } else {
            SimpleUtils.fail("Save dynamic group button fail to load!", false);
        }
    }

    @FindBy(css = ".modal-dialog [modal-title=\"Conflict Detected\"]")
    private WebElement conflictDetectedWindow;
    @FindBy(css = ".modal-dialog [modal-title=\"Conflict Detected\"] [label=\"Save\"]")
    private WebElement saveBtnOnConflictDetectedWindow;
    @Override
    public boolean ifConflictDetectedWindowShowUP() throws Exception {
        if (isElementLoaded(conflictDetectedWindow, 10)){
            return true;
        }
        return false;
    }

    @Override
    public void clickOnTheSaveBtnOnConflictDetectedWindow() throws Exception {
        if (isElementLoaded(saveBtnOnConflictDetectedWindow, 10)){
            clickTheElement(saveBtnOnConflictDetectedWindow);
            SimpleUtils.pass("Clicked on save button!");
        } else {
            SimpleUtils.fail("Fail to find Save button!", false);
        }
    }

    @Override
    public boolean isSaveBtnEnabledOnConflictDetectedWindow() throws Exception {
        if (isElementLoaded(saveBtnOnConflictDetectedWindow, 10)){
            return true;
        }
        return false;
    }

    @FindBy(css = "lg-button[ng-click*=\"addDynamicGroup\"]")
    private WebElement addDynamicGroupBtn;
    @Override
    public void clickOnAddBtnForDynamicGroupOnAssociationPage() throws Exception {
        if (isAddGroupBtnEnabled()){
            clickTheElement(addDynamicGroupBtn);
            SimpleUtils.pass("Create dynamic group button is clicked!");
        } else {
            SimpleUtils.fail("Create dynamic group button is not loaded!", false);
        }
    }

    @Override
    public boolean isAddGroupBtnEnabled() throws Exception {
        if (isElementLoaded(addDynamicGroupBtn, 10)){
            return true;
        }
        return false;
    }

    @FindBy(css = "input[aria-label=\"Group Name\"]")
    private WebElement groupNameInput;
    @Override
    public void inputGroupNameForDynamicGroupOnAssociationPage(String groupName) throws Exception {
        if (isElementLoaded(groupNameInput, 10)){
            groupNameInput.clear();
            groupNameInput.sendKeys(groupName);
            SimpleUtils.pass("Input group name!");
        } else {
            SimpleUtils.fail("Create dynamic group button is not loaded!", false);
        }
    }

    @FindBy(css = ".condition_line")
    private List<WebElement> criteriaOnTheAssociationPage;

    @Override
    public void selectAnOptionForCriteria(String criteria, String operator, String option) throws Exception {
        boolean flag1 = false;
        boolean flag2 = false;
        if (areListElementVisible(criteriaOnTheAssociationPage, 10)){
            for (WebElement criteriaLine: criteriaOnTheAssociationPage){
                if (criteriaLine.findElement(By.cssSelector("div[disabled=\"disabled\"]")).getAttribute("innerText").replace("\n", "").trim().equalsIgnoreCase(criteria)){
                    click(criteriaLine.findElement(By.cssSelector("lg-cascade-select[required=\"true\"] lg-select[ng-if*=\"operatorSelect\"] div.lg-select")));
                    List<WebElement> operators = criteriaLine.findElements(By.cssSelector("lg-cascade-select[required=\"true\"] lg-select[ng-if*=\"operatorSelect\"] div.lg-search-options__subLabel"));
                    for (WebElement optionValue: operators){
                        if (operator.equalsIgnoreCase(optionValue.getAttribute("innerText").replace("\n", "").trim())){
                            scrollToElement(optionValue);
                            click(optionValue);
                            flag1 = true;
                            break;
                        }
                    }
                    click(criteriaLine.findElement(By.cssSelector("lg-cascade-select[required=\"true\"] lg-cascade-select lg-multiple-select")));
                    List<WebElement> options = criteriaLine.findElements(By.cssSelector("lg-cascade-select[required=\"true\"] div.select-list-item"));
                    for (WebElement optionValue: options){
                        if (option.equalsIgnoreCase(optionValue.getAttribute("innerText").replace("\n", "").trim())){
                            scrollToElement(optionValue.findElement(By.cssSelector("input-field")));
                            click(optionValue.findElement(By.cssSelector("input-field")));
                            flag2 = true;
                            break;
                        }
                    }
                }
            }
            if (!flag1 && !flag2){
                SimpleUtils.fail("Didn't find the criteria and option you want!", false);
            }
        } else {
            SimpleUtils.fail("There is no criteria on the page!", false);
        }
    }

    @Override
    public List<String> getCriteriaListFromTheAssociationPage() throws Exception {
        List<String> resultList = new ArrayList<>();
        if (areListElementVisible(criteriaOnTheAssociationPage, 10)){
            for (WebElement criteriaLine: criteriaOnTheAssociationPage){
                resultList.add(criteriaLine.findElement(By.cssSelector("lg-select[class=\"ng-scope ng-isolate-scope\"] div.input-faked.ng-binding")).getAttribute("innerText").replace("\n","").trim());
            }
        }
        return resultList;
    }

    @FindBy(css = "i[ng-click*=\"deleteCondition\"]")
    private List<WebElement> deleteCriteriaBtnsOnTheAssociationPage;
    @Override
    public void deleteAllCriteriaOnTheAssociationPageIfExist() throws Exception {
        if (areListElementVisible(deleteCriteriaBtnsOnTheAssociationPage, 10)){
            for (int i = deleteCriteriaBtnsOnTheAssociationPage.size()-1; i>=0; i--){
                clickTheElement(deleteCriteriaBtnsOnTheAssociationPage.get(i));
            }
        }
    }

    @FindBy(css="nav.lg-tabs__nav>div:nth-last-child(2)")
    private WebElement templateAssociationBTN;
    @Override
    public void goToAssociationTabOnTemplateDetailsPage() throws Exception{
        if(isElementEnabled(templateAssociationBTN,10)){
            clickTheElement(templateAssociationBTN);
            SimpleUtils.pass("Clicked on the Association tab!");
        }else {
            SimpleUtils.fail("Failed to find Association Tab!",false);
        }
    }

    @FindBy(css = "[ng-repeat*=\"baseFieldList\"]")
    private List<WebElement> fieldListFromSettingsTab;

    @Override
    public List<String> getFieldListFromSettingsTab() throws Exception {
        List<String> resultList = new ArrayList<>();
        if (areListElementVisible(fieldListFromSettingsTab, 10)){
            for (WebElement fieldRow: fieldListFromSettingsTab){
                if (isElementLoaded(fieldRow.findElement(By.cssSelector("td.ng-binding")), 10)){
                    resultList.add(fieldRow.findElement(By.cssSelector("td.ng-binding")).getText());
                }
            }
        } else {
            SimpleUtils.fail("Fail to find fields!", false);
        }
        return resultList;
    }

    @Override
    public void setupRequiredFields(List<String> fields) throws Exception {
        if (areListElementVisible(fieldListFromSettingsTab, 10)){
            clearUpSelectedRequiredFields();
            for (WebElement fieldRow: fieldListFromSettingsTab){
                if (isElementLoaded(fieldRow.findElement(By.cssSelector("td.ng-binding"))) && fields.contains(fieldRow.findElement(By.cssSelector("td.ng-binding")).getText())){
                    if (isElementLoaded(fieldRow.findElement(By.tagName("input")), 10) && fieldRow.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")){
                        waitForSeconds(2);
                        clickTheElement(fieldRow.findElement(By.tagName("input")));
                    }
                }
            }
            SimpleUtils.assertOnFail("There is fields failed to be selected!", getDriver().findElements(By.cssSelector("lg-template-setting div.setting-box:first-of-type [ng-repeat*=\"baseFieldList\"] input[class*=\"not-empty\"]")).size() == fields.size(), false);
        } else {
            SimpleUtils.fail("Fail to find fields!", false);
        }
    }

    private void clearUpSelectedRequiredFields() throws Exception{
        if (areListElementVisible(fieldListFromSettingsTab, 10)){
            for (WebElement fieldRow: fieldListFromSettingsTab){
                if (isElementLoaded(fieldRow.findElement(By.tagName("input")), 10)
                    && fieldRow.findElement(By.tagName("input")).getAttribute("class").contains("not-empty")){
                    waitForSeconds(2);
                    clickTheElement(fieldRow.findElement(By.tagName("input")));
                }
            }
        } else {
            SimpleUtils.fail("Fail to find fields!", false);
        }
    }

    @FindBy(css = "lg-button[label=\"Test\"] button")
    private WebElement testButton;
    @FindBy(css = "[ng-if*=\"testMappingNum\"]")
    private WebElement testMappingResult;
    @Override
    public String clickOnTestBtnAndGetResultString() throws Exception {
        if (isElementLoaded(testButton, 10)){
            clickTheElement(testButton);
            SimpleUtils.pass("Clicked on test button!");
            if (isElementLoaded(testMappingResult, 10)){
                return testMappingResult.getText();
            }
        } else {
            SimpleUtils.fail("Test button is not on the page!", false);
        }
        return null;
    }

    @Override
    public boolean areFieldsCheckInputEnabled() throws Exception {
        if (areListElementVisible(fieldListFromSettingsTab, 10)){
            for (WebElement fieldRow: fieldListFromSettingsTab){
                if (!isElementLoaded(fieldRow.findElement(By.cssSelector("input[disabled]")), 10)){
                    return true;
                }
            }
        } else {
            SimpleUtils.fail("Fail to find fields!", false);
        }
        return false;
    }

    @Override
    public void verifyConflictDetectionInfo() throws Exception {
        if (isElementLoaded(conflictDetectedWindow.findElement(By.cssSelector(".lg-modal__title-icon")), 10)
                && conflictDetectedWindow.findElement(By.cssSelector(".lg-modal__title-icon")).getAttribute("innerText").equalsIgnoreCase("conflict detected")
                && conflictDetectedWindow.findElement(By.cssSelector(".dynamic-group-conflict-title")).getAttribute("innerText").trim().contains("The Dynamic Group is conflicting with the dynamic groups below")
                && conflictDetectedWindow.findElements(By.cssSelector(".dynamic-group-conflict-list")).size() > 0){
            SimpleUtils.pass("Title and text and content are expected!");
        } else {
            SimpleUtils.fail("Title and text and content are not expected!", false);
        }
    }

    @Override
    public void clickOnButtonOnTheConflictDetectedWindow(String cancelOrSave) throws Exception {
        if (cancelOrSave.toLowerCase().contains("save") && isElementLoaded(conflictDetectedWindow.findElement(By.cssSelector("lg-button[label=\"Save\"] button")))){
            clickTheElement(conflictDetectedWindow.findElement(By.cssSelector("lg-button[label=\"Save\"] button")));
            SimpleUtils.pass("Save button is clicked!");
        } else if (cancelOrSave.toLowerCase().contains("cancel") && isElementLoaded(conflictDetectedWindow.findElement(By.cssSelector("lg-button[label=\"Cancel\"] button")))){
            clickTheElement(conflictDetectedWindow.findElement(By.cssSelector("lg-button[label=\"Cancel\"] button")));
            SimpleUtils.pass("Cancel button is clicked!");
        } else {
            SimpleUtils.fail("Input string is not expected or buttons are not loaded!", false);
        }
    }

    @FindBy(css = "span[ng-if*=\"$error.customValidation\"]")
    private List<WebElement> warningMsgList;
    private List<String> validateChannelName(){
        List<String> warningInfo = new ArrayList<>();

        if (areListElementVisible(warningMsgList, 3)){
            for (WebElement msg : warningMsgList){
                warningInfo.add(msg.getText());
            }
        }else{
            SimpleUtils.report("No Warning Message show up!");
        }
        return  warningInfo;
    }

    @FindBy(css = "div.setting-group")
    private List<WebElement> settingsTypes;
    @FindBy(css = "div.modal-dialog")
    private WebElement popUpWindow;
    @FindBy(css = "div.modal-content input-field")
    private List<WebElement> fieldsInput;
    @FindBy(css = "lg-button[label=\"OK\"] button")
    private WebElement okBtnToSave;
    @FindBy(css = "label.use-in-reporting input")
    private WebElement useInReportCheckbox;
    @Override
    public List<String> createNewChannelOrCategory(String type, String displayName, String... otherInfo) throws Exception {
        WebElement displayNameInput;
        WebElement NameOrSourceTypeInput;
        boolean isExisting = false;
        List<String> warningInfo = null;

        if (areListElementVisible(settingsTypes, 5)) {
            for (WebElement settingsType : settingsTypes) {
                if (settingsType.findElement(By.cssSelector("lg-paged-search")).getAttribute("placeholder").contains(type)) {
                    clickTheElement(settingsType.findElement(By.cssSelector("div.header-add-icon button")));
                    if (isElementLoaded(popUpWindow, 8)) {
                        displayNameInput = fieldsInput.get(0).findElement(By.cssSelector("input[aria-label=\"Display Name\"]"));
                        NameOrSourceTypeInput = fieldsInput.get(1).findElement(By.cssSelector("input"));
                        displayNameInput.sendKeys(displayName);
                        if (NameOrSourceTypeInput.getText().equals(displayNameInput.getText())) {
                            fieldsInput.get(2).findElement(By.cssSelector("textarea[ng-if=\"$ctrl.type === 'textarea'\"]")).sendKeys(otherInfo[0]);
                            if (otherInfo.length > 1){
                                NameOrSourceTypeInput.clear();
                                NameOrSourceTypeInput.sendKeys(otherInfo[1]);
                            }
                            warningInfo = validateChannelName();
                            if (warningInfo.size() > 0){
                                clickTheElement(cancelBtn);
                                return warningInfo;
                            }
                            if ("Category".equalsIgnoreCase(type) && isElementLoaded(useInReportCheckbox) &&
                                    useInReportCheckbox.getAttribute("checked") == null) {
                                useInReportCheckbox.click();
                            }
                            clickTheElement(okBtnToSave);
                            isExisting = true;
                            break;
                        }else{
                            SimpleUtils.fail("Name/Source Type not equals to Display Name!", false);
                        }
                    } else {
                        SimpleUtils.fail("The creation window not show up after clicking add button!", false);
                    }
                }
            }
            if (!isExisting){
                SimpleUtils.fail("Can not find the setting type you want to add!", false);
            }
        } else{
            SimpleUtils.fail("No content in Settings page!", false);
        }
        return warningInfo;
    }

    @FindBy(css = "lg-paged-search")
    private List<WebElement> searchContents;
    @Override
    public WebElement searchSettingsForDemandDriver(String verifyType, String Name) throws Exception {
        WebElement searchResult = null;
        WebElement searchInput = null;
        List<WebElement> searchResultList = null;
        if (areListElementVisible(searchContents, 5)) {
            for (WebElement searchContent : searchContents) {
                if (searchContent.getAttribute("placeholder").contains(verifyType)) {
                    searchInput = searchContent.findElement(By.cssSelector("div.lg-paged-search input"));
                    searchInput.clear();
                    searchInput.sendKeys(Name);
                    searchInput.sendKeys(Keys.ENTER);
                    waitForSeconds(2);
                    searchResultList = searchContent.findElements(By.cssSelector("table.lg-table tr[ng-repeat*=\"item in $ctrl\"]"));
                    if (searchResultList.size() > 0){
                        searchResult = searchResultList.get(0);
                    } else {
                        searchInput.clear();
                        waitForSeconds(2);
                    }
                    break;
                }
            }
        }

        return searchResult;
    }

    @Override
    public void clickOnEditBtnInSettings(String verifyType, String Name, String NewName) throws Exception {
        WebElement searchResult = searchSettingsForDemandDriver(verifyType, Name);
        if(searchResult != null){
            clickTheElement(searchResult.findElement(By.cssSelector("lg-button[label=\"Edit\"] button")));
            if (isElementLoaded(popUpWindow) && popUpWindow.findElement(By.cssSelector("modal")).getAttribute("modal-title").toLowerCase().contains(verifyType)){
                fieldsInput.get(0).findElement(By.cssSelector("input[aria-label=\"Display Name\"]")).clear();
                fieldsInput.get(0).findElement(By.cssSelector("input[aria-label=\"Display Name\"]")).sendKeys(NewName);
                clickTheElement(okBtnToSave);
            }else {
                SimpleUtils.fail("The edit pop up window not show up!", false);
            }
        }else {
            SimpleUtils.fail("The item does not exist in the result list!", false);
        }
    }

    @Override
    public void clickOnRemoveBtnInSettings(String verifyType, String Name) throws Exception {
        int countBeforeRemove  = getTotalNumberForChannelOrCategory(verifyType);
        WebElement searchResult = searchSettingsForDemandDriver(verifyType, Name);
        if(searchResult != null){
            clickTheElement(searchResult.findElement(By.cssSelector("lg-button[label=\"Remove\"] button")));
            if (isElementLoaded(popUpWindow.findElement(By.cssSelector("modal[modal-title*=\"Remove\"]")), 10)){
                clickTheElement(popUpWindow.findElement(By.cssSelector("lg-button[label=\"OK\"] button")));
            }else{
                SimpleUtils.fail("There should pop up a confirmation window!", false);
            }
            if ("input stream".equalsIgnoreCase(verifyType) && countBeforeRemove == 1){
                searchSettingsForDemandDriver(verifyType, "");
                SimpleUtils.pass("No need to check as there will generate new record after remove all");
            } else if(searchSettingsForDemandDriver(verifyType, Name) == null){
                SimpleUtils.pass("The item is remove successfully!");
            }else {
                SimpleUtils.fail("The item is not removed failed!", false);
            }
        }else {
            SimpleUtils.fail("The item you want to remove does not exist in the result list!", false);
        }
    }

    public List<String> getStreamNamesInList(String streamType) throws Exception{
        List<String> streamNames = new ArrayList<>();

        for (WebElement inputStreamRow : inputStreamRows){
            if(streamType.equalsIgnoreCase(inputStreamRow.findElements(By.cssSelector("td")).get(1).getText())){
                streamNames.add(inputStreamRow.findElement(By.cssSelector("td:first-child span")).getText());
            }else if (streamType.equalsIgnoreCase("All")){
                streamNames.add(inputStreamRow.findElement(By.cssSelector("td:first-child span")).getText());
            }
        }
        while (isElementLoaded(settingsTypes.get(2).findElement(By.cssSelector(".lg-pagination__arrow--right")), 5)
                && !settingsTypes.get(2).findElement(By.cssSelector(".lg-pagination__arrow--right")).getAttribute("class").contains("disabled")) {
            clickTheElement(settingsTypes.get(2).findElement(By.cssSelector(".lg-pagination__arrow--right")));
            for (WebElement inputStreamRow : inputStreamRows){
                if(streamType.equalsIgnoreCase(inputStreamRow.findElements(By.cssSelector("td")).get(1).getText())){
                    streamNames.add(inputStreamRow.findElement(By.cssSelector("td:first-child span")).getText());
                }else if (streamType.equalsIgnoreCase("All")){
                    streamNames.add(inputStreamRow.findElement(By.cssSelector("td:first-child span")).getText());
                }
            }
        }
        //Navigate to the first page.
        while (isElementLoaded(settingsTypes.get(2).findElement(By.cssSelector(".lg-pagination__arrow--left")), 5)
                && !settingsTypes.get(2).findElement(By.cssSelector(".lg-pagination__arrow--left")).getAttribute("class").contains("disabled")) {
            clickTheElement(settingsTypes.get(2).findElement(By.cssSelector(".lg-pagination__arrow--left")));
        }
        return  streamNames;
    }

    @FindBy(css = "tr[ng-repeat=\"item in $ctrl.channelSortedRows\"]")
    private List<WebElement> channelRows;
    @FindBy(css = "tr[ng-repeat=\"item in $ctrl.categorySortedRows\"]")
    private List<WebElement> categoryRows;

    public int getTotalNumberForChannelOrCategory(String verifyType) throws Exception{
        int totalNumber = 0;
        List<WebElement> settingRows = new ArrayList<>();

        if (verifyType.equalsIgnoreCase("channel")){
            settingRows = channelRows;
        }else if (verifyType.equalsIgnoreCase("category")){
            settingRows = categoryRows;
        }else if (verifyType.equalsIgnoreCase("input stream")){
            settingRows = inputStreamRows;
        }else {
            SimpleUtils.fail("verifyType is not correct!", false);
        }

        if (verifyType.equalsIgnoreCase("input stream"))
            totalNumber =  calculateBaseInputStream();
        else
            totalNumber = settingRows.size();
        return  totalNumber;
    }

    @Override
    public List<String> getAllChannelsOrCategories(String settingType) throws Exception {
        List<String> nameList = new ArrayList<>();
        List<WebElement> settingRows = new ArrayList<>();
        if (settingType.equalsIgnoreCase("channel")){
            settingRows = channelRows;
        }else if (settingType.equalsIgnoreCase("category")) {
            settingRows = categoryRows;
        }else {
            SimpleUtils.fail("verifyType is not correct!", false);
        }
        for (WebElement settingRow : settingRows){
            nameList.add(settingRow.findElement(By.cssSelector("td")).getText());
            System.out.println("->" + settingRow.findElement(By.cssSelector("td")).getText());
        }
        return nameList;
    }

    public List<String> getStreamsInAggregated() throws Exception {
        List<String> baseInputStreamList = new ArrayList<>();
        for (WebElement streamOption : streamOptions) {
            baseInputStreamList.add(streamOption.findElement(By.cssSelector("input-field")).getAttribute("label"));
        }
        return baseInputStreamList;
    }
    public List<String> getCertainGranularityBaseInputStreams(String granularity) throws Exception{
        List<String> streamNameListForCertainGranularity = new ArrayList<>();
        List<String> baseInputStreamList = new ArrayList<>();
        baseInputStreamList = getStreamNamesInList("Base");

        if (granularity == null){
            granularity = "Slot (30 min)";
        }
        for (String baseStream : baseInputStreamList){
            clickEditBtn(baseStream);
            waitForSeconds(3);
            if (getGranularityForCertainInputStream().equals(granularity))
                streamNameListForCertainGranularity.add(baseStream);
        }
        return streamNameListForCertainGranularity;
    }

    public void verifyIfAllBaseStreamsInListForAggregatedInputStream(HashMap<String, String> aggregatedStream) throws Exception{
        List<String> streamNameListForCertainGranularity = new ArrayList<>();
        List<String> streamsInAggregated = new ArrayList<>();

       //Get all the base input streams with certain granularity in the input stream list.
        streamNameListForCertainGranularity = getCertainGranularityBaseInputStreams(aggregatedStream.get("Granularity"));
        //Enter aggregated input stream and get the stream list.
        clickEditBtn(aggregatedStream.get("Name"));
        clickTheElement(streamValueInput);
        streamsInAggregated = getStreamsInAggregated();
        clickCancelBtn();
        Collections.sort(streamNameListForCertainGranularity);
        Collections.sort(streamsInAggregated);

        if(ListUtils.isEqualList(streamNameListForCertainGranularity, streamsInAggregated)){
            SimpleUtils.pass("Base streams in aggregated are correct!");
        }else
            SimpleUtils.fail("Base streams in aggregated are NOT correct!", false);
    }

    @FindBy(css = "select[aria-label=\"Type\"]")
    private WebElement streamType;
    @FindBy(css = "select[aria-label=\"Streams\"]")
    private WebElement streamOperator;
    @FindBy(css = "lg-multiple-select[options=\"baseStreamOptions\"] input")
    private WebElement streamValueInput;
    @FindBy(css = "div.select-list-item.ng-scope")
    private List<WebElement> streamOptions;
    @FindBy(css = "lg-input-error.ng-scope span")
    private WebElement errorHint;
    @FindBy(css = "lg-button[label=\"Cancel\"] button")
    private WebElement cancelBtn;
    @Override
    public void createInputStream(HashMap<String, String> inputStreamSpecificInfo) throws Exception {
        WebElement NameInput;
        boolean isExisting = false;
        List<String> basicStreamNameList = null;
        List<String> allStreamNameList = null;
        String[] streamsToSet = null;

        //Get stream names
        allStreamNameList = getStreamNamesInList("All");
        if (areListElementVisible(settingsTypes, 5)) {
            for (WebElement settingsType : settingsTypes) {
                if (settingsType.findElement(By.cssSelector("lg-paged-search")).getAttribute("placeholder").contains("input stream")) {
                    isExisting = true;
                    scrollToElement(settingsType.findElement(By.cssSelector("lg-paged-search")));
                    waitForSeconds(3);
                    clickTheElement(settingsType.findElement(By.cssSelector("div.header-add-icon button")));
                    if (isElementLoaded(popUpWindow, 25)) {
                        NameInput = fieldsInput.get(0).findElement(By.xpath("//input[contains(@placeholder, 'Input Stream')]"));
                        NameInput.sendKeys(inputStreamSpecificInfo.get("Name"));
                        //Verify if the input name is existing
                        if (allStreamNameList.contains(inputStreamSpecificInfo.get("Name"))){
                            if (isElementLoaded(errorHint) && errorHint.getText().contains("input stream name is already exist")){
                                clickTheElement(cancelBtn);
                                SimpleUtils.pass("The error message is correct, cancel to create!");
                           }else{
                                SimpleUtils.fail("There should be an error message for existing input stream name!", false);
                            }
                            return;
                        }
                        if (inputStreamSpecificInfo.get("Tag") != null){
                            fieldsInput.get(3).findElement(By.cssSelector("input[aria-label=\"Data Tag\"]")).sendKeys(inputStreamSpecificInfo.get("Tag"));
                        }
                        if (!"Base".equalsIgnoreCase(inputStreamSpecificInfo.get("Type"))){
                            clickTheElement(streamType);
                            Select typeSelect = new Select(streamType);
                            typeSelect.selectByVisibleText(inputStreamSpecificInfo.get("Type"));
                            if (streamOperator.getAttribute("class").contains("ng-empty")) {
                                clickTheElement(streamOperator);
                                Select select = new Select(streamOperator);
                                select.selectByVisibleText(inputStreamSpecificInfo.get("Operator"));

                                if (streamValueInput.getAttribute("class").contains("ng-empty")) {
                                    clickTheElement(streamValueInput);

                                    for (WebElement streamOption : streamOptions) {
                                        if ("All".equalsIgnoreCase(inputStreamSpecificInfo.get("Streams"))) {
                                            clickTheElement(streamOption.findElement(By.cssSelector("input")));
                                            waitForSeconds(5);
                                        }
                                        else {
                                            streamsToSet = inputStreamSpecificInfo.get("Streams").split(",");
                                            for (String streamToSet : streamsToSet){
                                                if(streamToSet.equalsIgnoreCase(streamOption.getAttribute("innerText").trim()))
                                                    clickTheElement(streamOption.findElement(By.cssSelector("input")));
                                                waitForSeconds(5);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        clickTheElement(okBtnToSave);
                    }else {
                        SimpleUtils.fail("The creation window not show up after clicking add button!", false);
                    }
                }
            }
            if (!isExisting){
                SimpleUtils.fail("Can not find the setting type you want to add!", false);
            }
        }else {
            SimpleUtils.fail("No content in Settings page!", false);
        }
    }

    @FindBy(css = "input-field input[aria-label=\"Data Tag\"]")
    private WebElement tagInput;
    @Override
    public void clickOnEditBtnForInputStream(HashMap<String, String> inputStream, HashMap<String, String> inputStreamUpdated) throws Exception {
        WebElement searchResult = searchSettingsForDemandDriver("input stream", inputStream.get("Name"));
        if(searchResult != null){
            clickTheElement(searchResult.findElement(By.cssSelector("lg-button[label=\"Edit\"] button")));
            if (isElementLoaded(popUpWindow, 10) && popUpWindow.findElement(By.cssSelector("modal")).getAttribute("modal-title").toLowerCase().contains("input stream")){
                if ("true".equalsIgnoreCase(fieldsInput.get(0).findElement(By.xpath("//input[contains(@placeholder, 'Input Stream')]")).getAttribute("disabled"))){
                    SimpleUtils.pass("Input Stream name is read only in edit mode!");
                }else {
                    SimpleUtils.fail("Input Stream name should not be editable!", false);
                }
                if (!inputStream.get("Type").equals(inputStreamUpdated.get("Type"))){
                    clickTheElement(streamType);
                    Select typeSelect = new Select(streamType);
                    typeSelect.selectByVisibleText(inputStreamUpdated.get("Type"));
                }
                if (!"Base".equalsIgnoreCase(inputStreamUpdated.get("Type"))){
                    if (inputStream.get("Operator") == null ||  !inputStream.get("Operator").equalsIgnoreCase(inputStreamUpdated.get("Operator"))){
                        clickTheElement(streamOperator);
                        Select select = new Select(streamOperator);
                        select.selectByVisibleText(inputStreamUpdated.get("Operator"));
                    }
                    clickTheElement(streamValueInput);
                    for (WebElement streamOption : streamOptions) {
                        if (streamOption.findElement(By.cssSelector("input")).getAttribute("class").contains("ng-not-empty")){
                            scrollToElement(streamOption.findElement(By.cssSelector("input-field")));
                            click(streamOption.findElement(By.cssSelector("input-field")));
                            waitForSeconds(2);
                        }
                    }
                    for (WebElement streamOption : streamOptions){
                        if ("All".equalsIgnoreCase(inputStreamUpdated.get("Streams"))){
                            scrollToElement(streamOption.findElement(By.cssSelector("input-field")));
                            click(streamOption.findElement(By.cssSelector("input-field")));
                        }else if (streamOption.findElement(By.cssSelector("label.input-label")).getText().equalsIgnoreCase(inputStreamUpdated.get("Streams"))){
                            scrollToElement(streamOption.findElement(By.cssSelector("input-field")));
                            click(streamOption.findElement(By.cssSelector("input-field")));
                        }
                    }
                    clickTheElement(streamValueInput);
                }
                if("Base".equalsIgnoreCase(inputStreamUpdated.get("Type"))){
                    tagInput.clear();
                    tagInput.sendKeys(inputStreamUpdated.get("Tag"));
                }
                waitForSeconds(5);
                clickTheElement(okBtnToSave);
                waitForNotExists(popUpWindow, 8);
            }else {
                SimpleUtils.fail("The edit pop up window not show up!", false);
            }
        }else {
            SimpleUtils.fail("The item does not exist in the result list!", false);
        }
    }

    public int calculateBaseInputStream(){
        int baseCount = 0;
        int pageNum = 1;

        do {
            if(pageNum > 1){
                clickTheElement(rightArrow);
                waitForSeconds(3);
            }
            List<WebElement> inputStreamList = getDriver().findElements(By.cssSelector("tr[ng-repeat=\"item in $ctrl.inputStreamSortedRows\"]"));
            for (WebElement inputStreamRow : inputStreamList) {
                if ("Base".equalsIgnoreCase(inputStreamRow.findElements(By.cssSelector("td")).get(1).getText())) {
                    baseCount++;
                }
            }
            pageNum++;
        }while (!rightArrow.getAttribute("class").contains("disabled"));
        return baseCount;
    }

    @FindBy(css = "tr[ng-repeat=\"item in $ctrl.inputStreamSortedRows\"]")
    private List<WebElement> inputStreamRows;
    @FindBy(css = "lg-paged-search[placeholder=\"Search by input stream name\"] div.lg-pagination__arrow--right")
    private WebElement rightArrow;
    @FindBy(css = "input-field[placeholder*=\"input stream\"] input")
    private List<WebElement> inputStreamSearchInput;
    @Override
    public void verifyInputStreamInList(HashMap<String, String> inputStreamInfo, WebElement searchResultElement) throws Exception{
        boolean isSame = false;
        String resultName = "";
        String resultType = "";
        String resultSource = "";
        String resultTag = "";
        int baseCount = 0;

        if (searchResultElement != null) {
            resultName = searchResultElement.findElement(By.cssSelector("td:nth-child(1) span")).getText();
            resultType = searchResultElement.findElement(By.cssSelector("td:nth-child(2)")).getText();
            resultSource = searchResultElement.findElement(By.cssSelector("td:nth-child(3)")).getText();
            resultTag = searchResultElement.findElement(By.cssSelector("td:nth-child(4) span")).getText();
            if (inputStreamInfo.get("Name").equalsIgnoreCase(resultName)
                    && inputStreamInfo.get("Type").equalsIgnoreCase(resultType)) {
                if ("Base".equalsIgnoreCase(resultType) && resultSource.equals("")) {
                    isSame = true;
                } else if ("Aggregated".equalsIgnoreCase(resultType)) {
                    if ("All".equalsIgnoreCase(inputStreamInfo.get("Streams"))) {
                        searchSettingsForDemandDriver("input stream", "");
                        baseCount = getCertainGranularityBaseInputStreams(inputStreamInfo.get("Granularity")).size();
                        if (baseCount == Integer.parseInt(resultSource.split(" ")[0])){
                            isSame = true;
                        }
                    } else if (inputStreamInfo.get("Streams").split(",").length == Integer.parseInt(resultSource.split(" ")[0])) {
                        isSame = true;
                    }
                }
            }
            if (isSame) {
                SimpleUtils.pass("The search result is same SUCCESS!");
            } else {
                SimpleUtils.fail("The search result is not same FAIL!", false);
            }
        } else {
            SimpleUtils.fail("The search result is null!", false);
        }
    }

    @Override
    public void changeCriteriaInSettingsTab() throws Exception{
        if (areListElementVisible(fieldListFromSettingsTab, 10)){
            for (WebElement fieldRow: fieldListFromSettingsTab){
                if (isElementLoaded(fieldRow.findElement(By.tagName("input")), 10)
                        && fieldRow.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")){
                    waitForSeconds(2);
                    clickTheElement(fieldRow.findElement(By.tagName("input")));
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Fail to find fields!", false);
        }
    }

    @Override
    public void selectFirstOptionForCriteria() {
        int FirstOptionCheckedCount = 0;
        if (areListElementVisible(criteriaOnTheAssociationPage, 10)){
            for (WebElement criteriaLine : criteriaOnTheAssociationPage){
                click(criteriaLine.findElement(By.cssSelector("lg-cascade-select[required=\"true\"] lg-cascade-select lg-multiple-select")));
                List<WebElement> selectList = criteriaLine.findElements(By.cssSelector("lg-cascade-select[required=\"true\"] div.select-list-item"));
                click(selectList.get(1).findElement(By.cssSelector("input-field")));
                Actions actions = new Actions(getDriver());
                actions.moveByOffset(0, 0).click().build().perform();
                FirstOptionCheckedCount++;
                System.out.println(selectList.get(1).findElement(By.cssSelector("input-field")).getText() + " is checked!");
            }
        }

        if(FirstOptionCheckedCount == criteriaOnTheAssociationPage.size()){
            SimpleUtils.pass("First Option for the criteria are all checked!");
        }else {
            SimpleUtils.fail("Failed to check the first option!", false);
        }
    }

    @FindBy(css = "lg-select[ng-if=\"$ctrl.selectedOption.operatorSelect\"]")
    private List<WebElement> OperatorsInAssociation;
    @Override
    public boolean ifOperatorsCanBeSelected(List<String> valuesToCheck) {
        boolean flag = false;
        if (areListElementVisible(OperatorsInAssociation)){
            for (WebElement operator : OperatorsInAssociation){
                click(operator);
                List<WebElement> operatorValues = operator.findElements(By.cssSelector("div[class=\"lg-search-options__option ng-binding lg-search-options__subLabel\"]"));
                List<String> actualOperatorValues = new ArrayList<>();
                for (WebElement operatorValue : operatorValues){
                    actualOperatorValues.add(operatorValue.getText());
                    if (!isElementEnabled(operatorValue)){
                        break;
                    }
                }
                Actions actions = new Actions(getDriver());
                actions.moveByOffset(0, 0).click().build().perform();
                if (valuesToCheck.containsAll(actualOperatorValues)){
                    flag = true;
                }else{
                    SimpleUtils.fail("Actual Operators value are not expeted!", false);
                }
            }
        }else {
            SimpleUtils.fail("Can not find the operator element!", false);
        }

        return flag;
    }

    @FindBy(css = "lg-paged-search[ng-if*=\"$ctrl.attributeFieldList\"]")
    private WebElement externalAttributesSetting;
    @FindBy(css = "input-field input[placeholder*=\"location attribute\"]")
    private WebElement attributeSearchInput;
    @FindBy(css = "[ng-repeat*=\"$ctrl.sortedRows\"]")
    private List<WebElement> locationAttributesInSettings;
    public boolean verifyExternalAttributeExistInSettingsPage() throws Exception {
        boolean isExist = false;
        if (isElementLoaded(externalAttributesSetting, 5)
                && isElementLoaded(attributeSearchInput, 5)
                && areListElementVisible(locationAttributesInSettings, 5)){
            isExist = true;
        }
        return isExist;
    }

    @Override
    public List<String> getExternalAttributesInSettingsPage() throws Exception {
        List<String> resultList = new ArrayList<>();
        if (verifyExternalAttributeExistInSettingsPage()){
            for (WebElement fieldRow: locationAttributesInSettings){
                if (isElementLoaded(fieldRow.findElement(By.cssSelector("td.ng-binding")), 10)){
                    resultList.add(fieldRow.findElement(By.cssSelector("td.ng-binding")).getText());
                }
            }
        }else{
                SimpleUtils.fail("Fail to find attribute fields!", false);
        }
        return resultList;
    }
    @FindBy(css = "[ng-repeat*=\"$ctrl.sortedRows\"]")
    private List<WebElement> attributeSearchResults;
    @Override
    public boolean searchLocationAttributeInSettingsPage(String attributeName) throws Exception {
        boolean isFound = false;
        if (verifyExternalAttributeExistInSettingsPage()){
            scrollToElement(attributeSearchInput);
            attributeSearchInput.sendKeys(attributeName);
            waitForSeconds(3);
            if (getDriver().findElements(By.cssSelector("[ng-repeat*=\"$ctrl.sortedRows\"]")).size() > 0 &&
                    getDriver().findElements(By.cssSelector("[ng-repeat*=\"$ctrl.sortedRows\"]")).get(0).findElement(By.cssSelector("td.ng-binding")).getText().equalsIgnoreCase(attributeName)){
                isFound = true;
            }
        }else{
            SimpleUtils.fail("Fail to find attribute fields!", false);
        }
        return isFound;
    }

    public void clearUpSelectedLocationAttributes() throws Exception{
        if (areListElementVisible(locationAttributesInSettings, 10)){
            for (WebElement attributeField: locationAttributesInSettings){
                if (isElementLoaded(attributeField.findElement(By.tagName("input")), 10)
                        && attributeField.findElement(By.tagName("input")).getAttribute("class").contains("not-empty")){
                    waitForSeconds(2);
                    clickTheElement(attributeField.findElement(By.tagName("input")));
                }
            }
        } else {
            SimpleUtils.fail("Fail to find location attributes!", false);
        }
    }

    @Override
    public void setupLocationAttributes(String fields) throws Exception {
        if (areListElementVisible(locationAttributesInSettings, 10)){
            for (WebElement attributeField: locationAttributesInSettings){
                if (isElementLoaded(attributeField.findElement(By.cssSelector("td.ng-binding"))) && fields.equalsIgnoreCase(attributeField.findElement(By.cssSelector("td.ng-binding")).getText())){
                    if (isElementLoaded(attributeField.findElement(By.tagName("input")), 10) && attributeField.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")){
                        waitForSeconds(2);
                        clickTheElement(attributeField.findElement(By.tagName("input")));
                    }
                }
            }
        } else {
            SimpleUtils.fail("Fail to find fields!", false);
        }
    }

    @FindBy(xpath = "//div[contains(@ng-if, 'isAttribute')]/parent::div")
    private List<WebElement> attributesInAssociation;
    @Override
    public void verifyLocationAttributesInAssociation(HashMap<String, String> locationAttributesToCheck) throws Exception {
        HashMap<String, String> attributes = new HashMap<String, String>();
        String attributeName = "";
        String fromValue = "";
        String toValue = "";

        if (areListElementVisible(attributesInAssociation, 10)){
            System.out.println("---attributesInAssociation size is: " + attributesInAssociation.size());

            for (WebElement criteriaLine: attributesInAssociation) {
                List<WebElement> fromToLabel = criteriaLine.findElements(By.cssSelector("div[ng-if*=\"isAttribute\"] div.dilf"));
                if (fromToLabel.size() == 2) {
                    for (WebElement label : fromToLabel) {
                        if (label.findElement(By.tagName("span")).getText().toLowerCase().contains("from")){
                            fromValue = label.findElement(By.cssSelector("input-field[value*=\"from\"] div")).getAttribute("innerText").replace("\n", "").trim();
                        }
                        if (label.findElement(By.tagName("span")).getText().toLowerCase().contains("to")){
                            toValue = label.findElement(By.cssSelector("input-field[value*=\"to\"] div")).getAttribute("innerText").replace("\n", "").trim();
                        }
                    }
                    if (fromValue.equals(toValue)) {
                        attributeName = criteriaLine.findElement(By.cssSelector("div[disabled=\"disabled\"]")).getAttribute("innerText").replace("\n", "").trim();
                        attributes.put(attributeName, fromValue);
                    } else {
                        SimpleUtils.fail("From value should be equals to To Value for the attribute!", false);
                    }
                } else {
                    SimpleUtils.fail("Location Attribute should have From& To text box", false);
                }
            }
            if (locationAttributesToCheck.equals(attributes)){
                SimpleUtils.pass("Location Attribute items and values in association page are expected!");
            }else {
                SimpleUtils.fail("Location Attribute items and values in association page are not correct!", false);
            }
        } else {
            SimpleUtils.fail("No location attributes on the page!", false);
        }
    }

    @Override
    public void fillInValuesForLocationAttributes(HashMap<String, List<String>> locationAttributes) throws Exception {
        String attributeName = "";

        if (areListElementVisible(attributesInAssociation, 10)){
            for (WebElement criteriaLine: attributesInAssociation) {
                for (Map.Entry<String, List<String>> attributeEntry : locationAttributes.entrySet()){
                    attributeName = criteriaLine.findElement(By.cssSelector("div[disabled=\"disabled\"]")).getAttribute("innerText").replace("\n", "").trim();
                    if (attributeEntry.getKey().equals(attributeName)){
                        criteriaLine.findElement(By.cssSelector("input-field[value*=\"from\"] input")).clear();
                        criteriaLine.findElement(By.cssSelector("input-field[value*=\"from\"] input")).sendKeys(attributeEntry.getValue().get(0));
                        criteriaLine.findElement(By.cssSelector("input-field[value*=\"to\"] input")).clear();
                        criteriaLine.findElement(By.cssSelector("input-field[value*=\"to\"] input")).sendKeys(attributeEntry.getValue().get(1));
                    }
                }
            }
        }else {
            SimpleUtils.fail("No location attributes on the page!", false);
        }
    }

    @Override
    public boolean verifyAttributeValuesInAssociationPage() throws Exception {
        String regex ="^(0|[1-9][0-9]*)$";
        boolean flag1 = true;
        boolean flag2 = true;
        String fromValue = "";
        String toValue = "";

        if (areListElementVisible(attributesInAssociation, 10)){
            //Verify if the values are legal
            for (WebElement criteriaLine: attributesInAssociation) {
                fromValue = criteriaLine.findElement(By.cssSelector("input-field[value*=\"from\"] div")).getAttribute("innerText").replace("\n", "").trim();
                toValue = criteriaLine.findElement(By.cssSelector("input-field[value*=\"to\"] div")).getAttribute("innerText").replace("\n", "").trim();
                if (fromValue.isEmpty() || toValue.isEmpty()){
                    flag1 = false;
                    break;
                }
                if (!fromValue.matches(regex) || !toValue.matches(regex)){
                    flag1 = false;
                    break;
                }
                if (Integer.parseInt(fromValue) > Integer.parseInt(toValue)){
                    flag1 = false;
                    break;
                }
            }
            //Verify if the save button is clickable
            if (flag1 == false){
                if (!testButton.getAttribute("disabled").equals("true") || !doneDynamicGroupBtn.getAttribute("disabled").equals("true")){
                    flag2 = false;
                    SimpleUtils.fail("Button should be disabled as Illegal from&to value exist!", false);
                }
            }else{
                if (testButton.getAttribute("disabled") != null || doneDynamicGroupBtn.getAttribute("disabled") != null){
                    flag2 = false;
                    SimpleUtils.fail("Button should be enabled as all from&to values are legal!", false);
                }
            }
        }else {
            SimpleUtils.fail("No location attributes on the page!", false);
        }
        return flag2;
    }

    @FindBy(css = "input-field[label=\"Granularity\"] select")
    private WebElement granularityOption;
    @Override
    public void clickEditBtn(String inputStreamName) throws Exception {
        WebElement searchResult = searchSettingsForDemandDriver("input stream", inputStreamName);
        if(searchResult != null){
            clickTheElement(searchResult.findElement(By.cssSelector("lg-button[label=\"Edit\"] button")));
            if (isElementLoaded(popUpWindow, 10))
                SimpleUtils.pass("Enter the input stream edit page successfully.");
            else
                SimpleUtils.fail("Enter the input stream edit page failed!", false);
        }else {
            SimpleUtils.fail("No input stream found after search!", false);
        }
    }

    @Override
    public String getGranularityForCertainInputStream() throws Exception {
        String granularityValue = "";
        Select granularitySelect = new Select(granularityOption);
        granularityValue = granularitySelect.getFirstSelectedOption().getText();
        clickTheElement(cancelBtn);
        searchSettingsForDemandDriver("input stream", "");
        waitForSeconds(25);
        if (granularityValue.isEmpty()){
            SimpleUtils.fail("Failed to get granularity value!", false);
        }
        return granularityValue;
    }


    @Override
    public void updateGranularityForCertainInputStream(String granularityValue) throws Exception{
        Select granularitySelect = new Select(granularityOption);
        granularitySelect.selectByVisibleText(granularityValue);
        if (!granularitySelect.getFirstSelectedOption().getText().equals(granularityValue))
            SimpleUtils.fail("Failed to select the granularity option: " + granularityValue, false);
        else
            SimpleUtils.pass("The granularity value is selected correctly.");
        clickTheElement(okBtnToSave);
    }

    @FindBy(css = "div.lg-toast--error>p")
    private WebElement warningPopUp;
    @Override
    public void validateWarningMessage(String warningMsgToVerify) throws Exception {
        String warningMessage = "";
        if (isElementLoaded(warningPopUp, 8)){
            warningMessage = warningPopUp.getAttribute("innerText").trim();
        }

        if (warningMessage.contains(warningMsgToVerify)){
            if (isElementLoaded(popUpWindow))
                clickCancelBtn();
            SimpleUtils.pass("The warning message is " + ".");
        }else {
            SimpleUtils.fail("The warning message is not correct!", false);
        }
    }

    @Override
    public void clickCancelBtn() throws Exception {
        if (isElementLoaded(cancelBtn,10)){
            click(cancelBtn);
            SimpleUtils.pass("cancel edit input stream button is clicked!");
        } else {
            SimpleUtils.fail("cancel edit input stream button is not loaded!", false);
        }
    }

    @Override
    public void removeInputStream(String name) throws Exception {
        String type = "input stream";
        WebElement searchResult = searchSettingsForDemandDriver(type, name);
        if(searchResult != null){
            clickTheElement(searchResult.findElement(By.cssSelector("lg-button[label=\"Remove\"] button")));
            if (isElementLoaded(popUpWindow.findElement(By.cssSelector("modal[modal-title*=\"Remove\"]")), 15)){
                clickTheElement(popUpWindow.findElement(By.cssSelector("lg-button[label=\"OK\"] button")));
            }else{
                SimpleUtils.fail("There should pop up a confirmation window!", false);
            }
        }else {
            SimpleUtils.fail("The item you want to remove does not exist in the result list!", false);
        }
    }
}
