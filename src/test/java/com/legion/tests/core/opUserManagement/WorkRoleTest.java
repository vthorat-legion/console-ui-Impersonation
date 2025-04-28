package com.legion.tests.core.opUserManagement;

import com.legion.pages.core.OpCommons.RightHeaderBarPage;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.opusermanagement.*;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.Constants;
import com.legion.utils.HttpUtil;
import com.legion.utils.SimpleUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

public class WorkRoleTest extends TestBase {
    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        this.createDriver((String) params[0], "83", "Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield((String) params[1], (String) params[2], (String) params[3]);
        RightHeaderBarPage modelSwitchPage = new RightHeaderBarPage();
        modelSwitchPage.switchToOpsPortal();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify add and update work role")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddEditSearchAndDisableWorkRoleAsInternalAdminForUserManagement(String browser, String username, String password, String location) throws Exception {
        try {
            OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
            navigationPage.navigateToUserManagement();
            OpsPortalUserManagementPanelPage panelPage = new OpsPortalUserManagementPanelPage();
            panelPage.goToWorkRolesPage();
            OpsPortalWorkRolesPage workRolesPage = new OpsPortalWorkRolesPage();

            //add a new work role and save it
            workRolesPage.addNewWorkRole();
            WorkRoleDetailsPage workRoleDetailsPage = new WorkRoleDetailsPage();
            workRoleDetailsPage.editWorkRoleDetails("autoWorkRole001", 3, "Deployed", "3");
            workRoleDetailsPage.addAssignmentRule("3","2","2098");
            workRoleDetailsPage.saveAssignRule();
            workRoleDetailsPage.submit();
            workRolesPage.save();
            workRolesPage.searchByWorkRole("autoWorkRole001");
            Assert.assertEquals(workRolesPage.getTheFirstWorkRoleInTheList(), "autoWorkRole001", "Failed to add new work role!");

            //search by work role
            //testcase1: exact matching
            workRolesPage.searchByWorkRole("Ambassador");
            Assert.assertTrue(workRolesPage.getTheFirstWorkRoleInTheList().equalsIgnoreCase("Ambassador"));

            //testcase2: partial matching
            workRolesPage.searchByWorkRole("test");
            Assert.assertFalse(workRolesPage.getTheFirstWorkRoleInTheList().equalsIgnoreCase("test"));
            Assert.assertTrue(workRolesPage.getTheFirstWorkRoleInTheList().contains("test"));

            //testcase3: no matching item
            workRolesPage.searchByWorkRole("m*");
            Assert.assertTrue(workRolesPage.getNoResultNotice().contains("A Work Role defines a category of work that needs to be scheduled."));

            //cancel the creating of new work role
            workRolesPage.addNewWorkRole();
            workRoleDetailsPage.editWorkRoleDetails("testCancelCreating", 6, "Deployed", "2");
            workRoleDetailsPage.submit();
            workRolesPage.cancel();
            Assert.assertEquals(workRolesPage.getCancelDialogTitle(), "Cancel Editing?", "Failed to popup the cancel dialog. ");
            workRolesPage.cancelEditing();
            workRolesPage.searchByWorkRole("testCancelCreating");
            Assert.assertTrue(workRolesPage.getNoResultNotice().contains("A Work Role defines a category of work that needs to be scheduled."));

            //edit an existing work role and save the editing
            workRolesPage.editAnExistingWorkRole("autoWorkRole001");
            workRoleDetailsPage.editWorkRoleDetails("autoWorkRole001-edit", 2, "Deployed", "1.5");
            workRoleDetailsPage.submit();
            workRolesPage.save();
            workRolesPage.searchByWorkRole("autoWorkRole001-edit");
            Assert.assertEquals(workRolesPage.getTheFirstWorkRoleInTheList(), "autoWorkRole001-edit", "Failed to Edit new work role!");

            //edit an existing work role and cancel the editing
            workRolesPage.editAnExistingWorkRole("autoWorkRole001-edit");
            workRoleDetailsPage.editWorkRoleDetails("autoWorkRole-cancelEdit", 9, "Deployed", "2");
            workRoleDetailsPage.submit();
            workRolesPage.cancel();
            Assert.assertEquals(workRolesPage.getCancelDialogTitle(), "Cancel Editing?", "Cancel Dialog is not displayed");
            workRolesPage.cancelEditing();
            workRolesPage.searchByWorkRole("autoWorkRole-cancelEdit");
            Assert.assertTrue(workRolesPage.getNoResultNotice().contains("A Work Role defines a category of work that needs to be scheduled."));
            workRolesPage.searchByWorkRole("autoWorkRole001-edit");
            Assert.assertEquals(workRolesPage.getTheFirstWorkRoleInTheList(), "autoWorkRole001-edit", "Failed to cancel the editing!");

            //disable the work role added and it can't be searched out
            workRolesPage.disableAWorkRole("autoWorkRole001-edit");
            Assert.assertEquals(workRolesPage.getDisableDialogTitle(), "Disable Work Role", "The disable work role dialog is not displayed.");
            workRolesPage.okToDisableAction();
            workRolesPage.save();
            workRolesPage.searchByWorkRole("autoWorkRole001-edit");
            Assert.assertTrue(workRolesPage.getNoResultNotice().contains("A Work Role defines a category of work that needs to be scheduled."));

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private String[] copyWorkRole() {

        //body
        String payLoad = "{\"enterpriseName\":\"opauto\",\"userName\":\"stoneman@legion.co\",\"passwordPlainText\":\"admin11.a\",\"sourceSystem\":\"legion\"}";
        String sessionId = TestBase.getSessionId(payLoad);
        //set headers
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("sessionId", sessionId);

        return HttpUtil.httpPost0(Constants.copyWorkRole,header);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Copy work role")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCopyWorkRoleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String[] response = copyWorkRole();
            Assert.assertEquals(response[0], "200", "Failed to copy work role");
            if(!response[2].contains("workerRoles are copied from controls to OP")){
                SimpleUtils.fail("Failed to copy work role",false);
            }
        } catch (Exception e) {
            SimpleUtils.fail("Failed to copy work role",false);
        }
    }

    @AfterMethod
    public void tearDown() {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.logout();
    }

}
