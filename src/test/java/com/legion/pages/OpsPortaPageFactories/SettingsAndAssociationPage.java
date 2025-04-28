package com.legion.pages.OpsPortaPageFactories;

import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;

public interface SettingsAndAssociationPage {
    public void goToTemplateListOrSettings(String tab) throws Exception;
    public void verifyTitleOnTheSettingsPage(String groupType) throws Exception;
    public void verifyTitleOnTheSAssociationPage(String groupType) throws Exception;
    public void clickOnEditBtnForDynamicGroupOnAssociationPage() throws Exception;
    public void clickOnRemoveBtnToRemoveDynamicGroupOnAssociationPage() throws Exception;
    public void clickOnDoneBtnForDynamicGroupOnAssociationPage() throws Exception;
    public void clickOnAddBtnForDynamicGroupOnAssociationPage() throws Exception;
    public boolean isAddGroupBtnEnabled() throws Exception;
    public void inputGroupNameForDynamicGroupOnAssociationPage(String groupName) throws Exception;
    public void selectAnOptionForCriteria(String criteria, String operator, String option) throws Exception;
    public void deleteAllCriteriaOnTheAssociationPageIfExist() throws Exception;
    public void goToAssociationTabOnTemplateDetailsPage() throws Exception;
    public boolean ifConflictDetectedWindowShowUP() throws Exception;
    public void clickOnTheSaveBtnOnConflictDetectedWindow() throws Exception;
    public boolean isSaveBtnEnabledOnConflictDetectedWindow() throws Exception;
    public List<String> getFieldListFromSettingsTab() throws Exception;
    public void setupRequiredFields(List<String> fields) throws Exception;
    public List<String> getCriteriaListFromTheAssociationPage() throws Exception;
    public String clickOnTestBtnAndGetResultString() throws Exception;
    public boolean areFieldsCheckInputEnabled() throws Exception;
    public void verifyConflictDetectionInfo() throws Exception;
    public void clickOnButtonOnTheConflictDetectedWindow(String cancelOrSave) throws Exception;
    public List<String> createNewChannelOrCategory(String type, String displayName, String... otherInfo) throws Exception;
    public WebElement searchSettingsForDemandDriver(String verifyType, String Name) throws Exception;
    public void clickOnEditBtnInSettings(String verifyType, String Name, String NewName) throws Exception;
    public void clickOnRemoveBtnInSettings(String verifyType, String Name) throws Exception;
    public void createInputStream(HashMap<String, String> inputStreamSpecificInfo) throws Exception;
    public  void clickOnEditBtnForInputStream(HashMap<String, String> inputStream, HashMap<String, String> inputStreamUpdated)  throws Exception;
    public void verifyInputStreamInList(HashMap<String, String> inputStreamInfo, WebElement searchResultElement) throws Exception;
    public List<String> getStreamNamesInList(String streamType) throws Exception;
    public int getTotalNumberForChannelOrCategory(String streamType) throws Exception;

    public List<String> getAllChannelsOrCategories(String settingType) throws Exception;
    public void changeCriteriaInSettingsTab() throws Exception;
    public void selectFirstOptionForCriteria() throws Exception;
    public boolean ifOperatorsCanBeSelected(List<String> valuesToCheck) throws Exception;
    public List<String>  getExternalAttributesInSettingsPage() throws Exception;
    public boolean  searchLocationAttributeInSettingsPage(String attributeName) throws Exception;
    public void setupLocationAttributes(String fields) throws Exception;
    public void verifyLocationAttributesInAssociation(HashMap<String, String> locationAttributesToCheck) throws Exception;
    public void clearUpSelectedLocationAttributes() throws Exception;
    public void fillInValuesForLocationAttributes(HashMap<String, List<String>> locationAttributes) throws Exception;
    public boolean verifyAttributeValuesInAssociationPage() throws Exception;
    public void clickEditBtn(String inputStreamName) throws Exception;
    public String getGranularityForCertainInputStream() throws Exception;
    public void updateGranularityForCertainInputStream(String granularityValue) throws Exception;
    public void validateWarningMessage(String warningMsgToVerify) throws Exception;
    public void clickCancelBtn() throws Exception;
    public void removeInputStream(String name) throws Exception;
    public void verifyIfAllBaseStreamsInListForAggregatedInputStream(HashMap<String, String> aggregatedInputStream) throws Exception;
}
