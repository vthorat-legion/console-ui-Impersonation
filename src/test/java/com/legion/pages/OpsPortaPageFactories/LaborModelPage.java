package com.legion.pages.OpsPortaPageFactories;

import java.util.HashMap;
import java.util.List;


public interface LaborModelPage {

    public void goToLaborModelTile() throws Exception;
    public void goToTaskDetail(String taksName) throws Exception;
    public void checkCustomFormulaCoding(String keyword) throws Exception;
    public void clickOnSpecifyTemplateName(String template_name, String edit) throws Exception;

    public void clickOnLaborModelTab() throws Exception;

    public List<HashMap<String, String>> getLaborModelInTemplateLevel();

    public void addNewLaborModelTemplate(String templateName) throws Exception;

    public void deleteDraftLaborModelTemplate(String templateName) throws Exception;

    public void publishNewLaborModelTemplate(String templateName,String dynamicGroupName,String dynamicGroupCriteria,String dynamicGroupFormula) throws Exception;

    public void selectLaborStandardRepositorySubTabByLabel(String label) throws Exception;

    public void goToLaborStandardRepositoryTile() throws Exception;

    public void clickOnEditButton() throws Exception;

    public void clickOnSaveButton() throws Exception;

    public void clickOnCancelButton() throws Exception;

    public void clickOnAddAttributeButton() throws Exception;

    public void createNewAttribute(String attributeName,String attributeValue,String attributeDescription) throws Exception;

    public void cancelCreateNewAttribute(String attributeName,String attributeValue,String attributeDescription) throws Exception;

    public boolean isSpecifyAttributeExisting(String attributeName) throws Exception;

    public void clickOnDeleteAttributeButton(String attributeName) throws Exception;

    public void clickOkBtnOnDeleteAttributeDialog() throws Exception;

    public void clickCancelBtnOnDeleteAttributeDialog() throws Exception;

    public boolean checkDeleteAttributeButtonForEachAttribute() throws Exception;

    public List<String> clickOnPencilButtonAndUpdateAttribute(String attributeName,String attributeValueUpdate,String attributeDescriptionUpdate) throws Exception;

    public HashMap<String, List<String>> getValueAndDescriptionForEachAttributeAtGlobalLevel() throws Exception;

    public void selectLaborModelTemplateDetailsPageSubTabByLabel(String label) throws Exception;

    public void overriddenLaborModelRuleInLocationLevel(int index);

    public void archivePublishedOrDeleteDraftTemplate(String templateName, String action) throws Exception;

    public void clickOnEditButtonOnTemplateDetailsPage() throws Exception;

    public void updateAttributeValueInTemplate(String attributeName,String attributeValueUpdate) throws Exception;

    public void saveAsDraftTemplate() throws Exception;

    public void createNewTemplatePageWithoutSaving(String templateName) throws Exception;

    public HashMap<String, List<String>> getValueAndDescriptionForEachAttributeAtTemplateLevel() throws Exception;

    public void publishNowTemplate() throws Exception;

    public void verifyEntryOfLaborModelSubscription() throws Exception;

    public void exportLaborModelSubscriptionCsv() throws Exception;

    public void verifyImportLocationLevelWorkRoleSubscription();

    public boolean verifyImportLocationWorkRolePageShow();

    public void disableLocationLevelWorkRoleSubscriptionInLaborModelTemplate();

    public void enableLocationLevelWorkRoleSubscriptionInLaborModelTemplate();

    public boolean verifyWorkRoleStatusInLocationLevel(String workRole);

    public void selectWorkRoles(String workRole) throws Exception;

    public HashMap<String, List<String>> getValueAndDescriptionForEachAttributeAtTemplateLevelInLocations() throws Exception;

    public void disableOrEnableWorkRoleInLocationLevel(String roleName, boolean isEnable) throws Exception;
}