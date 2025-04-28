package com.legion.tests.core.OpsPortal;

import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LaborModelPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.oplabormodel.LaborModelPanelPage;
import com.legion.pages.core.oplabormodel.LaborModelRepositoryPage;
import com.legion.pages.core.oplabormodel.LaborModelTemplateDetailPage;
import com.legion.pages.core.oplabormodel.TaskDetailsPage;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.apache.commons.collections.ListUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;


public class LaborModelTest extends TestBase {

    public enum modelSwitchOperation{
        Console("Console"),
        OperationPortal("Control Cent");

        private final String value;
        modelSwitchOperation(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{


        this.createDriver((String)params[0],"83","Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify create delete and publish labor model template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUserCanCreateDeleteAndPublishLaborModelTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            SimpleDateFormat dfs=new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime=dfs.format(new Date()).trim();
            String templateName="AutoCreate"+currentTime;
            String dynamicGroupName ="AutoDynamic"+currentTime;
            String dynamicGroupCriteria =  "Custom";
            String dynamicGroupFormula = dynamicGroupName;
            String action = "Archive";

            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.addNewLaborModelTemplate(templateName);
            laborModelPage.deleteDraftLaborModelTemplate(templateName);
            laborModelPage.publishNewLaborModelTemplate(templateName,dynamicGroupName,dynamicGroupCriteria,dynamicGroupFormula);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.archivePublishedOrDeleteDraftTemplate(templateName,action);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Global External Attributes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateUpdateAndDeleteNewAttributeFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            SimpleDateFormat dfs=new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime=dfs.format(new Date()).trim();
            String attributeName="AutoCreate"+currentTime;
            String attributeValue = "33";
            String attributeDescription = attributeName;
            String attributeValueUpdate = "34";
            String attributeDescriptionUpdate = "Update";
            String label = "External Attributes";

            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborStandardRepositoryTile();
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            laborModelPage.clickOnEditButton();
            laborModelPage.clickOnAddAttributeButton();
            //Cancel create attributes
            laborModelPage.cancelCreateNewAttribute(attributeName,attributeValue,attributeDescription);
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            if(!laborModelPage.isSpecifyAttributeExisting(attributeName)){
                SimpleUtils.pass("User can cancel create attribute successfully!");
            }else {
                SimpleUtils.fail("User failed to cancel create attribute!",false);
            }

            //Create attributes
            laborModelPage.clickOnEditButton();
            laborModelPage.clickOnAddAttributeButton();
            laborModelPage.createNewAttribute(attributeName,attributeValue,attributeDescription);
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            if(laborModelPage.isSpecifyAttributeExisting(attributeName)){
                SimpleUtils.pass("User created attribute successfully!");
            }else {
                SimpleUtils.fail("User failed to created attribute!",false);
            }

            //Update attribute
            laborModelPage.clickOnEditButton();
            List<String> updatedVal = laborModelPage.clickOnPencilButtonAndUpdateAttribute(attributeName,attributeValueUpdate,attributeDescriptionUpdate);
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            if(updatedVal.get(0).equals(attributeValueUpdate) && updatedVal.get(1).equals(attributeDescriptionUpdate)){
                SimpleUtils.pass("User can update attribute value and description successfully!");
            }else {
                SimpleUtils.fail("User failed to update attribute value and description!",false);
            }

            //Delete attribute
            laborModelPage.clickOnEditButton();
            laborModelPage.checkDeleteAttributeButtonForEachAttribute();
            laborModelPage.clickOnDeleteAttributeButton(attributeName);
            laborModelPage.clickCancelBtnOnDeleteAttributeDialog();
            laborModelPage.clickOnDeleteAttributeButton(attributeName);
            laborModelPage.clickOkBtnOnDeleteAttributeDialog();
            laborModelPage.clickOnSaveButton();
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            if(!laborModelPage.isSpecifyAttributeExisting(attributeName)){
                SimpleUtils.pass("User can delete attribute successfully!");
            }else {
                SimpleUtils.fail("User failed to delete attribute!",false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Template level external attributes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTemplateLevelAttributeFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            SimpleDateFormat dfs=new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime=dfs.format(new Date()).trim();
            String label = "External Attributes";
            HashMap<String,List<String>> attributesInfoInGlobal = new HashMap<>();
            HashMap<String,List<String>> attributesInfoInTemplate = new HashMap<>();
            HashMap<String,List<String>> attributesUpdatedInfoInTemplate = new HashMap<>();
            String attributeName ="AutoUsingAttribute";
            Random random=new Random();
            int number=random.nextInt(90)+10;
            String attributeValueUpdate=String.valueOf(number);
            String templateName = "AutoCreated" + currentTime;
            String mode = "edit";


            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            //Check are the default value of attributes at template level correct or not?
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborStandardRepositoryTile();
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            attributesInfoInGlobal = laborModelPage.getValueAndDescriptionForEachAttributeAtGlobalLevel();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.createNewTemplatePageWithoutSaving(templateName);
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            attributesInfoInTemplate = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevel();
            //Compare two map info
            for(String key:attributesInfoInTemplate.keySet()){
                for(String key1:attributesInfoInGlobal.keySet()){
                    if(key.equals(key1)){
                        List<String> valuesInGlobal = attributesInfoInGlobal.get(key1);
                        List<String> valuesInTemplate = attributesInfoInTemplate.get(key);

                        if(ListUtils.isEqualList(valuesInGlobal,valuesInTemplate)){
                            SimpleUtils.pass("The attribute " + key + " in template level is correct.");
                            break;
                        }else{
                            SimpleUtils.fail("The attribute " + key + " in template level is NOT correct.",false);
                        }
                    }
                }
            }

            //update template attribute value
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateName,mode);
            laborModelPage.clickOnEditButtonOnTemplateDetailsPage();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            laborModelPage.updateAttributeValueInTemplate(attributeName,attributeValueUpdate);
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("Details");
            laborModelPage.saveAsDraftTemplate();
            //go to template details page check updated or not?
            laborModelPage.clickOnSpecifyTemplateName(templateName,mode);
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            attributesUpdatedInfoInTemplate = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevel();
            for(String key2:attributesUpdatedInfoInTemplate.keySet()){
                if(key2.equals(attributeName)){
                    if(attributesUpdatedInfoInTemplate.get(key2).get(0).equals(attributeValueUpdate)){
                        SimpleUtils.pass("User can update attribute value successfully in labor model template.");
                        break;
                    }else {
                        SimpleUtils.fail("User can NOT update attribute value successfully in labor model template.",false);
                    }
                }
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Custom formula configuration in task detail")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyColorCodingInTaskDetailAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            String taskName="CodeColorTest";
            String label = "Tasks";
            //navigate to labor model
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            //go to labor model repository
            laborModelPage.goToLaborStandardRepositoryTile();
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            LaborModelRepositoryPage repositoryPage = new LaborModelRepositoryPage();
            //check the test task
            repositoryPage.searchByTaskORLabel(taskName);
            Boolean searched=repositoryPage.getTheSearchedTaskName().equalsIgnoreCase(taskName) || repositoryPage.getTheSearchedLabel().equalsIgnoreCase(taskName);
            Assert.assertTrue(searched, "Failed to load the task that search by task name or label!");
            if(searched){
                SimpleUtils.pass("Find the searched task!");
                //enter the task detail
                laborModelPage.goToTaskDetail(taskName);
                //check custom color coding
                laborModelPage.checkCustomFormulaCoding("p_OffsetAndStart");}
            else
                SimpleUtils.fail("Not find the searched task, please add it!",false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Add update disable tasks")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddEditSearchAndDisableTasksAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
            navigationPage.navigateToLaborModelPage();
            LaborModelPanelPage panelPage = new LaborModelPanelPage();
            panelPage.goToLaborModelRepositoryPage();

            //add a new task
            LaborModelRepositoryPage repositoryPage = new LaborModelRepositoryPage();
            //verify edit button
            Assert.assertTrue(repositoryPage.getEditButton().equals("Edit"), "The edit button does not display!");
            repositoryPage.edit();
            repositoryPage.addNewTask();
            TaskDetailsPage taskDetailsPage = new TaskDetailsPage();
            Random random=new Random();
            String taskName="autoTask"+random.nextInt(1000);
            taskDetailsPage.editTask(taskName, "auto", "Meal", "cart", 2);
            taskDetailsPage.saveAdding();
            repositoryPage.save();

            //search by task name or label name
            //exact matching
            //verify the new task
            repositoryPage.searchByTaskORLabel(taskName);
            Assert.assertTrue(repositoryPage.getTheSearchedTaskName().equalsIgnoreCase(taskName) || repositoryPage.getTheSearchedLabel().equalsIgnoreCase(taskName), "Failed to add new task and search by task name or label!");
            //partial matching 'by label'
            repositoryPage.searchByTaskORLabel("Clear");
            Assert.assertTrue(repositoryPage.getTheSearchedTaskName().contains("Clear") || repositoryPage.getTheSearchedLabel().contains("Clear"), "Failed to search by task name or label!");
            //no match
            repositoryPage.searchByTaskORLabel("M*");
            Assert.assertEquals("There are no tasks that match your criteria.",repositoryPage.getNoMatchMessage(),"Failed to assert no match!");


            //select the task new created in Labor Model
            repositoryPage.back();
            panelPage.goToLaborModel();
            com.legion.pages.core.oplabormodel.LaborModelPage laborModelPage = new com.legion.pages.core.oplabormodel.LaborModelPage();
            laborModelPage.searchTemplate("autoTestCreatedBySophia");
            laborModelPage.clickIntoDetails();
            LaborModelTemplateDetailPage templateDetailPage = new LaborModelTemplateDetailPage();
            templateDetailPage.edit();
            templateDetailPage.okInModal();
            templateDetailPage.selectTasks(taskName);
            templateDetailPage.selectWorKRole();
            templateDetailPage.save("Save as draft");
            laborModelPage.back();
            panelPage.goToLaborModelRepositoryPage();

            //edit a task and save
            repositoryPage.editAnExistingTask(taskName);
            taskDetailsPage.editTask("autoTaskEdit", "testEdit", "Rest", "cart", 1);
            taskDetailsPage.saveEditing();
            repositoryPage.save();
            repositoryPage.searchByTaskORLabel("autoTaskEdit");
//            Assert.assertTrue(repositoryPage.getTheSearchedTaskName().equalsIgnoreCase("autoTaskEdit"), "Failed to edit the task!");

            //cancel edit
            repositoryPage.editAnExistingTask("autoTaskEdit");
            taskDetailsPage.editTask("autoTaskCancel", "testCancel", "Filler", "cart", 0);
            taskDetailsPage.saveEditing();
            repositoryPage.cancel();
            Assert.assertEquals(repositoryPage.getModalTitle(), "Cancel Editing?", "The cancel modal dose not display!");
            repositoryPage.cancelEditing();
            //assert editing no been saved
            repositoryPage.searchByTaskORLabel("autoTaskCancel");
            Assert.assertEquals("There are no tasks that match your criteria.",repositoryPage.getNoMatchMessage(),"Failed to assert no match!");
            //assert the original task still there.
            repositoryPage.searchByTaskORLabel("autoTaskEdit");
            Assert.assertTrue(repositoryPage.getTheSearchedTaskName().equalsIgnoreCase("autoTaskEdit"), "Failed to cancel the editing!");

            //disable a task
            repositoryPage.searchByTaskORLabel("autoTaskEdit");
            repositoryPage.edit();
            //verify 'Action'
            Assert.assertTrue(repositoryPage.getActionColumnLabel().equals("Action"), "The Action column does not display!");
            //verify 'Disable'
            Assert.assertTrue(repositoryPage.getActionColumnValue().equals("Disable"), "The Action value is not disable!");
            repositoryPage.disable();
            Assert.assertEquals(repositoryPage.getDisableModalTitle(), "Disable Work Task", "The disable work task modal dose not display!");
            repositoryPage.saveDisableAction();
            repositoryPage.save();
            repositoryPage.searchByTaskORLabel("autoTaskEdit");
            Assert.assertEquals("There are no tasks that match your criteria.",repositoryPage.getNoMatchMessage(),"Failed to assert no match!");

            //select the task new created in Labor Model
            repositoryPage.back();
            panelPage.goToLaborModel();
            laborModelPage.searchTemplate("autoTestCreatedBySophia");
            laborModelPage.clickIntoDetails();
            templateDetailPage.edit();
            templateDetailPage.okInModal();
            templateDetailPage.toSelectATask();
            templateDetailPage.searchTasksInModal(taskName);
            templateDetailPage.searchTasksInModal("autoTaskEdit");
            templateDetailPage.cancelInModal();
         //   templateDetailPage.cancel();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Description of External Attributes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDescriptionOfExternalAttributesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            SimpleDateFormat dfs=new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime=dfs.format(new Date()).trim();
            String attributeName="AutoCreate"+currentTime;
            Random random=new Random();
            int number=random.nextInt(90)+10;
            String attributeValue = String.valueOf(number);
            String attributeDescription = attributeName + "Des";
            String attributeValueUpdate = String.valueOf(number+1);
            String attributeDescriptionUpdate = "Update123@";
            String label = "External Attributes";
            HashMap<String, List<String>> attributesInfoInGlobal = new HashMap<>();
            String templateName = "AutoUsingByFiona";
            String mode = "View";
            HashMap<String, List<String>> attributesInfoInTemplate = new HashMap<>();
            String locationName = "FionaUsingLocation";
            HashMap<String, List<String>> attributesInfoInLocation = new HashMap<>();

            //go to LaborStandardRepository - External Attributes
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborStandardRepositoryTile();
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);

            //Create attributes
            laborModelPage.clickOnEditButton();
            laborModelPage.clickOnAddAttributeButton();
            laborModelPage.createNewAttribute(attributeName,attributeValue,attributeDescription);
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            attributesInfoInGlobal = laborModelPage.getValueAndDescriptionForEachAttributeAtGlobalLevel();
            for(String key:attributesInfoInGlobal.keySet()) {
                if (key.equals(attributeName)) {
                    List<String> valuesInGlobal = attributesInfoInGlobal.get(key);
                    if (valuesInGlobal.get(0).equals(attributeValue) && valuesInGlobal.get(1).equals(attributeDescription)) {
                        SimpleUtils.pass("The attribute " + key + " added successfully!");
                        break;
                    } else {
                        SimpleUtils.fail("The attribute " + key + " is not correct or added failed", false);
                    }
                }
            }

            //Update existing attribute description in global level
            laborModelPage.clickOnEditButton();
            List<String> updatedVal = laborModelPage.clickOnPencilButtonAndUpdateAttribute(attributeName,attributeValueUpdate,attributeDescriptionUpdate);
            laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
            if(updatedVal.get(0).equals(attributeValueUpdate) && updatedVal.get(1).equals(attributeDescriptionUpdate)){
                SimpleUtils.pass("User can update attribute value and description successfully!");
            }else {
                SimpleUtils.fail("User failed to update attribute value and description!",false);
            }

            //Go to template level check the update attribute show well or not?
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateName,mode);
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            attributesInfoInTemplate = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevel();
            for(String key:attributesInfoInTemplate.keySet()) {
                if (key.equals(attributeName)) {
                    List<String> valuesInTemplate = attributesInfoInTemplate.get(key);
                    if (valuesInTemplate.get(0).equals(attributeValueUpdate) && valuesInTemplate.get(1).equals(attributeDescriptionUpdate)) {
                        SimpleUtils.pass("The attribute " + key + " in template level is aligned with global level.");
                        break;
                    } else {
                        SimpleUtils.fail("The attribute " + key + " in template level is not aligned with global level.", false);
                    }
                }
            }

            //Go to location level check the update attribute show well or not?
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String,String>>  templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
//            if (templateInfo.get(7).get("Overridden").equalsIgnoreCase("No")) {
//                SimpleUtils.pass("Labor model template is not overridden at location level");
//                locationsPage.actionsForEachTypeOfTemplate(templateInfo.get(7).get("Template Type"),"View");
//            } else{
//                SimpleUtils.pass("Labor model template is already overridden at location level");
//                locationsPage.editLocationBtnIsClickableInLocationDetails();
//                locationsPage.actionsForEachTypeOfTemplate(templateInfo.get(7).get("Template Type"),"Reset");
//                locationsPage.actionsForEachTypeOfTemplate(templateInfo.get(7).get("Template Type"),"View");
//            }
            if(locationsPage.isOverrideStatusAtLocationLevel("Labor Model")){
                SimpleUtils.pass("Labor model template is already overridden at location level");
                locationsPage.clickActionsForTemplate("Labor Model", "Reset");
                locationsPage.clickActionsForTemplate("Labor Model", "View");
            }else {
                SimpleUtils.pass("Labor model template is not overridden at location level");
                locationsPage.clickActionsForTemplate("Labor Model", "View");
            }
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            attributesInfoInLocation = locationsPage.getValueAndDescriptionForEachAttributeAtLocationLevel();
            for(String key:attributesInfoInLocation.keySet()) {
                if (key.equals(attributeName)) {
                    List<String> valuesInLocation = attributesInfoInLocation.get(key);
                    if (valuesInLocation.get(0).equals(attributeValueUpdate) && valuesInLocation.get(1).equals(attributeDescriptionUpdate)) {
                        SimpleUtils.pass("The attribute " + key + " in location level is aligned with global level.");
                        break;
                    } else {
                        SimpleUtils.fail("The attribute " + key + " in location level is not aligned with global level.", false);
                    }
                }
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify the entry of override location level work role by csv")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheEntryOfOverrideLocationLevelWorkRoleByCsvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateName="AutoUsingByFiona";
            String mode="edit";
            //go to specify template detail page - check the labor model subscription entry
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateName,mode);
            laborModelPage.clickOnEditButtonOnTemplateDetailsPage();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("Association");
            laborModelPage.verifyEntryOfLaborModelSubscription();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify export Location Subscription of labor model template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyExportLocationSubscriptionOfLaborModelTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateName="AutoUsingByFiona";
            String mode="edit";
            //go to specify template detail page - check the labor model subscription entry
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateName,mode);
            laborModelPage.clickOnEditButtonOnTemplateDetailsPage();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("Association");
            laborModelPage.verifyEntryOfLaborModelSubscription();
            laborModelPage.exportLaborModelSubscriptionCsv();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify import Location Subscription of labor model template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyImportLocationSubscriptionOfLaborModelTemplateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String templateName="AutoUsingByFiona";
            String mode="edit";
            //go to specify template detail page - check the labor model subscription entry
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateName,mode);
            laborModelPage.clickOnEditButtonOnTemplateDetailsPage();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("Association");
            laborModelPage.verifyEntryOfLaborModelSubscription();
//            laborModelPage.verifyImportLocationLevelWorkRoleSubscription();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Update Work Role subscription via csv")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUpdateWorkRoleSubscriptionViaCsvAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName="AutoUsingForLaborBudget";
            String templateName="AutoUsingByLocationLevelWorkRole";
            String workRole="Mgr on Duty";
            String mode="edit";
            boolean flag;
            boolean flag1;
            //go to location level to check the work role status
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            locationsPage.actionsForEachTypeOfTemplate(templateInfo.get(7).get("Template Type"), "View");
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            flag = laborModelPage.verifyWorkRoleStatusInLocationLevel(workRole);
            //go to labor model template to check upload csv to update location level work role status
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateName,mode);
            laborModelPage.clickOnEditButtonOnTemplateDetailsPage();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("Association");
            laborModelPage.verifyEntryOfLaborModelSubscription();
//            if(flag){
//                laborModelPage.disableLocationLevelWorkRoleSubscriptionInLaborModelTemplate();
//            }else {
//                laborModelPage.enableLocationLevelWorkRoleSubscriptionInLaborModelTemplate();
//            }
            laborModelPage.clickOnSaveButton();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("Details");
            laborModelPage.publishNowTemplate();
            //go to location to check location level work role again
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo1 = locationsPage.getLocationTemplateInfoInLocationLevel();
            locationsPage.actionsForEachTypeOfTemplate(templateInfo1.get(7).get("Template Type"), "View");
            flag1 = laborModelPage.verifyWorkRoleStatusInLocationLevel(workRole);
//            if(flag != flag1){
//                SimpleUtils.pass("User update work role by upload csv successfully!");
//            }else {
//                SimpleUtils.fail("User update work role by upload csv failed!",true);
//            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user can view and update location level labor model template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenLaborModelInLocationLevelAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "OMLocation16";
            String workRoleName = "ForAutomation";
            int index = 0;
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Labor Model", "View");
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Labor Model", "Edit");
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.overriddenLaborModelRuleInLocationLevel(index);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.saveBtnIsClickable();
            if(locationsPage.isOverrideStatusAtLocationLevel("Labor Model")) {
                SimpleUtils.pass("Overridden Labor Model successfully");
            } else
                SimpleUtils.fail("Overridden Labor Model failed", false);

            //reset
            locationsPage.clickActionsForTemplate("Labor Model", "Reset");
            if (!locationsPage.isOverrideStatusAtLocationLevel("Labor Model")) {
                SimpleUtils.pass("Reset Labor Model successfully");
            } else
                SimpleUtils.fail("Reset Labor Model failed", false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Only one driver type is allowed under task's team member rules")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOnlyOneDriverTypeAllowedUnderTaskTeamMemberRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String warningMsgToVerify = "Only allowing 1 single demand driver type under task's team member rules";
            int index  = 2;
            HashMap<String, String> firstRules = new HashMap<String, String>(){
                {
                    put("capacity", "1");
                    put("driverType", "Items");
                    put("sourceType", "EDW");
                    put("categoryType", "Enrollments");
                }
            };
            HashMap<String, String> secondRules = new HashMap<String, String>(){
                {
                    put("capacity", "3");
                    put("driverType", "Items");
                    put("sourceType", "Channel01");
                    put("categoryType", "Verifications");
                }
            };
            HashMap<String, String> thirdRules = new HashMap<String, String>(){
                {
                    put("capacity", "4");
                    put("driverType", "Transactions");
                    put("sourceType", "EDW");
                    put("categoryType", "Verifications");
                }
            };
            OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
            navigationPage.navigateToLaborModelPage();
            LaborModelPanelPage panelPage = new LaborModelPanelPage();
            panelPage.goToLaborModelRepositoryPage();

            //add a new task
            LaborModelRepositoryPage repositoryPage = new LaborModelRepositoryPage();
            //Add two rules with same driver type
            Assert.assertTrue(repositoryPage.getEditButton().equals("Edit"), "The edit button does not display!");
            repositoryPage.edit();
            repositoryPage.addNewTask();
            TaskDetailsPage taskDetailsPage = new TaskDetailsPage();
            Random random=new Random();
            String taskName="autoTask"+random.nextInt(1000);
            taskDetailsPage.editTask(taskName, "auto", "Labor", "cart", 3);
            taskDetailsPage.addRulesForDemandTask(firstRules);
            taskDetailsPage.saveRule();
            taskDetailsPage.addRulesForDemandTask(secondRules);
            taskDetailsPage.saveRule();
            taskDetailsPage.saveAdding();
            SimpleUtils.assertOnFail("Should have no warning message", !taskDetailsPage.getWarningMessage().contains(warningMsgToVerify), false);
            //Add a rule with different driver type
            repositoryPage.searchByTaskORLabel(taskName);
            repositoryPage.clickInToDetails();
            taskDetailsPage.addRulesForDemandTask(thirdRules);
            taskDetailsPage.saveRule();
            taskDetailsPage.saveEditing();
            if (!taskDetailsPage.getWarningMessage().contains(warningMsgToVerify))
                SimpleUtils.fail("Warning Message is not as expected!", false);
            else
                SimpleUtils.pass("Correct Warning message" + taskDetailsPage.getWarningMessage());

            //Remove the rule with different driver type
            taskDetailsPage.removeRule(index);
            taskDetailsPage.saveEditing();
            repositoryPage.save();
            SimpleUtils.assertOnFail("Should have no warning message", !taskDetailsPage.getWarningMessage().contains(warningMsgToVerify), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

        }
