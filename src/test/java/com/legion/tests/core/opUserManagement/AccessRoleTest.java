package com.legion.tests.core.opUserManagement;

import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.OpCommons.RightHeaderBarPage;
import com.legion.pages.core.opusermanagement.AccessRolePage;
import com.legion.pages.core.opusermanagement.OpsPortalUserManagementPanelPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.DBConnection;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public class AccessRoleTest extends TestBase {
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
    @TestName(description = "Custom access role")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAccessRoleAsInternalAdminOfAccessRoleTest(String browser, String username, String password, String location) {
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToUserManagement();
        OpsPortalUserManagementPanelPage panelPage = new OpsPortalUserManagementPanelPage();
        panelPage.goToUsersAndRoles();

        AccessRolePage accessRolePage = new AccessRolePage();
        accessRolePage.switchToAccessRole();
        //1.Verify this is a "Add new role" button
        Assert.assertTrue(accessRolePage.isAddNewRoleButtonDisplayed(), "'Add new role' button was displayed");
        accessRolePage.addNewRole();
        //go to New Access Role--role details page
        //2.Verify the layout of "add new role" page
        Assert.assertEquals(accessRolePage.getNewAccessRolePageTitle(), "New Access Role", "Failed to open 'New Access Role' page!");
        Assert.assertEquals(accessRolePage.getContentBoxSubTitle(), "Role Details", "Failed to open 'New Access Role' page!");
        Assert.assertTrue(accessRolePage.getRoleDetailLabels().equals(roleDetailsLabels()), "Incorrect labels on 'New Access Role' page!");
        //3.click back button on role details page to back to access role landing page.
        accessRolePage.back();
        Assert.assertTrue(accessRolePage.isAccessRoleTabDisplayed(), "Failed to back to access role landing page!");

        //4.click cancel button on role permission page to back to access role landing page.
        accessRolePage.addNewRole();
        accessRolePage.cancelNewAccessRole();
        Assert.assertTrue(accessRolePage.isAccessRoleTabDisplayed(), "Failed to back to access role landing page!");

        //5. verify the max length of Role Title is 50!
        accessRolePage.addNewRole();
        String invalidRoleTitle = "asdfghjklo0987654321,./;'=[-08``@#$%^&*(Aqwsxcdefv!";
        //length=51
        accessRolePage.setRoleTitle(invalidRoleTitle);
       // Assert.assertEquals(accessRolePage.getErrorMessage(), "Role Title requires a maximum length of 50", "Failed to assert the max length of Role Title is 50");
        //length=50
        //& allow alphabet, Number and Special characters
        String length50 = "asdfghjklo0987654321,./;'=[-08``@#$%^&*(Aqwsxcdefv";
        accessRolePage.setRoleTitle(length50);
   //     Assert.assertFalse(accessRolePage.isErrorMesgDisplayed(), "Failed to assert the max length of Role Title is 50");
        // Role Title is required
        accessRolePage.clearRoleTitle();
        Assert.assertEquals(accessRolePage.getErrorMessage(), "Role Title is required", "Failed to assert the Role Title is required");

        //6. 'Copy Role Permission From*' is required!
        Random random = new Random();
        String accessRoleTitle = "Auto-AccessRoleTitle_" + random.nextInt(1000);
        accessRolePage.setRoleTitle(accessRoleTitle);
        Assert.assertFalse(accessRolePage.IsNextButtonEnabled(), "Failed to assert 'Copy Role Permission From*' is required!");
        accessRolePage.copyRolePermissionFrom("Store Manager");
        Assert.assertTrue(accessRolePage.IsNextButtonEnabled(), "The next button is still disabled after all required fields have been filled!");
        //7. verify all the options
        accessRolePage.getOptionsOfRolePermission();
        Assert.assertTrue(accessRolePage.getOptionsOfRolePermission().containsAll(rolePermissionOptions()), "Failed to assert all the role permission listed!");

        //8. also proven that description is optional
        //length=101
        String desc101 = "Part 1 of 3: Adding a Single Sheet Open yo'+' button at the end of your sheet tabs.@#￥%……&*《》？：“」「|！~";
        accessRolePage.setDescription(desc101);
        Assert.assertEquals(accessRolePage.getErrorMessage(), "Description requires a maxmimum length of 100", "Failed to assert the max length of description is 100!");
        //length=100
        //& allow alphabet, Number and Special characters
        String desc100 = "Part 1 of 3: Adding a Single Sheet Open yo'+' button at the end of your sheet tabs.@#￥%……&*《》？：“」「|！";
        accessRolePage.setDescription(desc100);
        Assert.assertFalse(accessRolePage.isErrorMesgDisplayed(), "Failed to assert the max length of description is 100!");

        //go to New Access Role--role permission page
        //9.Verify the layout of "role permission" page
        accessRolePage.next();
        Assert.assertEquals(accessRolePage.getRolePermissionTitle(), "Role Permissions", "Failed to open 'New Access Role' page!");

        //10.Verify the permission list
        //Assert.assertTrue( accessRolePage.getAllRolePermission().size()==collapsibleTitle().size()&&accessRolePage.getAllRolePermission().containsAll(collapsibleTitle())&&collapsibleTitle().containsAll( accessRolePage.getAllRolePermission()));

        //11.click back button on role permission page to back to role details page
        accessRolePage.back();
        Assert.assertEquals(accessRolePage.getContentBoxSubTitle(), "Role Details", "Failed to back to 'Role Details' page!");

        //12.click cancel button on role permission page to back to access role landing page.
        accessRolePage.next();
        accessRolePage.cancelNewAccessRole();
        Assert.assertTrue(accessRolePage.isAccessRoleTabDisplayed(), "Failed to back to access role landing page!");

        //13/14.create a new AccessRole successfully UI check and happy flow check
        accessRolePage.addNewRole();
        String roleTitle = "AutoAccessRole" + random.nextInt(1000);
        accessRolePage.setRoleTitle(roleTitle);
        accessRolePage.copyRolePermissionFrom("Team Member");
        accessRolePage.setDescription("For auto test!");
        accessRolePage.next();
        accessRolePage.createNewAccessRole();
        accessRolePage.expandControls();
        Assert.assertTrue(accessRolePage.getRoleNames().contains(roleTitle), "Failed to display the new created access role in UI!");

        //15.error message for using an existing role title
        accessRolePage.addNewRole();
        accessRolePage.setRoleTitle(roleTitle);
        Assert.assertEquals(accessRolePage.getErrorMessage(), "The role title must be unique.", "Failed to assert the role title need to be unique!");

        //16.verify customer role can be shown in optional list
        Assert.assertTrue(accessRolePage.getOptionsOfRolePermission().contains(roleTitle), "Failed to assert customer Access role was in the optional list!");

        //delete the Access Role just create successfully.
        String sql="delete from legionrc.Role where name like \"AutoAccessRole%\" and enterpriseId=\"aee2dfb5-387d-4b8b-b3f5-62e86d1a9d95\"";
        DBConnection.updateDB(sql);
        String queryResult = DBConnection.queryDB("legionrc.Role", "name", "name='" + roleTitle + "'");
        Assert.assertEquals(queryResult, "No item returned!", "Failed to clear the data just generated in DB!");
    }

    public ArrayList<String> roleDetailsLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Role Title*");
        labels.add("Copy Role Permission From*");
        labels.add("Description");
        return labels;
    }

    public ArrayList<String> rolePermissionOptions() {
        ArrayList<String> rolePermissionOpt = new ArrayList<String>();
        rolePermissionOpt.add("Admin");
        rolePermissionOpt.add("Advanced Insights Creator");
        rolePermissionOpt.add("Advanced Insights Viewer");
        rolePermissionOpt.add("Area Manager");
        rolePermissionOpt.add("Store Manager");
        rolePermissionOpt.add("Team Lead");
        rolePermissionOpt.add("Team Member");
        rolePermissionOpt.add("Analyst");
        rolePermissionOpt.add("HR");
        rolePermissionOpt.add("Payroll Admin");
        rolePermissionOpt.add("Operations Manager");
        rolePermissionOpt.add("Communications");
        rolePermissionOpt.add("Operation Store Config");
        rolePermissionOpt.add("Budget Planner");
        return rolePermissionOpt;
    }

    public ArrayList<String> collapsibleTitle() {
        ArrayList<String> collapsT = new ArrayList<String>();
        collapsT.add("Controls");
        collapsT.add("Timesheet Controls");
        collapsT.add("Communication Compliance");
        collapsT.add("Newsfeed");
        collapsT.add("Timesheet");
        collapsT.add("Profile");
        collapsT.add("Chat");
        collapsT.add("Manage");
        collapsT.add("Schedule");
        collapsT.add("Team");
        collapsT.add("Dashboard");
        collapsT.add("Plan");
        collapsT.add("Announcements");
        collapsT.add("Advanced Insights");
        collapsT.add("Scorecard");
        collapsT.add("Analytics");
        return collapsT;
    }
}
