package com.legion.tests.core.oplabormodel;

import com.legion.pages.core.oplabormodel.*;
import com.legion.pages.core.OpCommons.RightHeaderBarPage;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class LaborModelRepositoryTest extends TestBase {
    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        this.createDriver((String) params[0], "83", "Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        RightHeaderBarPage modelSwitchPage = new RightHeaderBarPage();
        modelSwitchPage.switchToOpsPortal();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Labor Standard Repository")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddEditSearchAndDisableTasksAsInternalAdminForLaborModel(String browser, String username, String password, String location) throws Exception {
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
            taskDetailsPage.editTask("autoTask001", "auto", "Labor", "cart", 2);
            taskDetailsPage.saveAdding();
            repositoryPage.save();

            //search by task name or label name
            //exact matching
            //verify the new task
            repositoryPage.searchByTaskORLabel("autoTask001");
            Assert.assertTrue(repositoryPage.getTheSearchedTaskName().equalsIgnoreCase("autoTask001") || repositoryPage.getTheSearchedLabel().equalsIgnoreCase("autoTask001"), "Failed to add new task and search by task name or label!");
            //partial matching 'by label'
            repositoryPage.searchByTaskORLabel("Clear");
            Assert.assertTrue(repositoryPage.getTheSearchedTaskName().contains("Clear") || repositoryPage.getTheSearchedLabel().contains("Clear"), "Failed to search by task name or label!");
            //no match
            repositoryPage.searchByTaskORLabel("M*");
            Assert.assertEquals("There are no tasks that match your criteria.",repositoryPage.getNoMatchMessage(),"Failed to assert no match!");


            //select the task new created in Labor Model
            repositoryPage.back();
            panelPage.goToLaborModel();
            LaborModelPage laborModelPage = new LaborModelPage();
            laborModelPage.searchTemplate("autoTestCreatedBySophia");
            laborModelPage.clickIntoDetails();
            LaborModelTemplateDetailPage templateDetailPage = new LaborModelTemplateDetailPage();
            templateDetailPage.edit();
            templateDetailPage.okInModal();
            templateDetailPage.selectTasks("autoTask001");
            templateDetailPage.selectWorKRole();
            templateDetailPage.save("Save as draft");
            laborModelPage.back();
            panelPage.goToLaborModelRepositoryPage();

            //edit a task and save
            repositoryPage.editAnExistingTask("autoTask001");
            taskDetailsPage.editTask("autoTaskEdit", "testEdit", "Rest", "cart", 1);
            taskDetailsPage.saveEditing();
            repositoryPage.save();
            repositoryPage.searchByTaskORLabel("autoTaskEdit");
            Assert.assertTrue(repositoryPage.getTheSearchedTaskName().equalsIgnoreCase("autoTaskEdit"), "Failed to edit the task!");

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
            templateDetailPage.searchTasksInModal("autoTask001");
            templateDetailPage.searchTasksInModal("autoTaskEdit");
            templateDetailPage.cancelInModal();
            templateDetailPage.cancel();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @AfterMethod
    public void tearDown() {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.logout();
    }

}
