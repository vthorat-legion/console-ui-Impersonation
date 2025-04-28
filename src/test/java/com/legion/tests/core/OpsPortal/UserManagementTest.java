package com.legion.tests.core.OpsPortal;

import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.LoginPage;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LaborModelPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.TeamPage;
import com.legion.pages.core.ConsoleControlsNewUIPage;
import com.legion.pages.core.ConsoleControlsPage;
import com.legion.pages.core.OpCommons.ConsoleNavigationPage;
import com.legion.pages.core.OpCommons.OpsCommonComponents;
import com.legion.pages.core.OpCommons.OpsPortalNavigationPage;
import com.legion.pages.core.OpCommons.RightHeaderBarPage;
import com.legion.pages.core.opusermanagement.*;
import com.legion.pages.core.schedule.ConsoleScheduleCommonPage;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import com.legion.tests.core.opEmployeeManagement.AccrualEngineTest;


public class UserManagementTest extends TestBase {

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
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.verifyNewTermsOfServicePopUp();
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Common function for newsfeed group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDynamicGroupFunctionInUserManagementTabAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try{
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime =  dfs.format(new Date()).trim();
            String groupNameForNewsFeed =   "AutoNewsFeed" +currentTime;
            String groupNameForNewsFeed2 = "difName" +currentTime;
            String description = "AutoCreate" +currentTime;
            String criteria = "Work Role";
            String criteriaUpdate = "Minor";
            String searchText = "AutoNewsFeed";


            //go to User Management tab
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            //check dynamic group item
            if (userManagementPage.iCanSeeDynamicGroupItemTileInUserManagementTab()) {
                //go to dynamic group
                userManagementPage.goToDynamicGroup();
                if (userManagementPage.verifyLayoutOfDGDisplay()) {
                    List<HashMap<String,String>> groupRows =userManagementPage.getExistingGroups();
                    if (groupRows!=null) {
                        userManagementPage.searchNewsFeedDynamicGroup(searchText);
                        //remove existing dynamic group
                        userManagementPage.iCanDeleteExistingWFSDG();
                        userManagementPage.iCanGoToManageDynamicGroupPage();
                        //verifyEachField
                        userManagementPage.verifyNameInputField(groupNameForNewsFeed);
                        userManagementPage.verifyCriteriaList();
                        userManagementPage.testButtonIsClickable();
                        userManagementPage.addMoreButtonIsClickable();
                        userManagementPage.removeCriteriaBtnIsClickAble();
                        userManagementPage.criteriaDescriptionDisplay();
                        userManagementPage.cancelBtnIsClickable();
                        //create new workforce sharing dynamic group
                        String groupTestMess = userManagementPage.addNewsFeedGroupWithOneCriteria(groupNameForNewsFeed,description,criteria);

                        //verify add group with existing group name
                        userManagementPage.verifyAddNewsFeedGroupWithExistingGroupName(groupNameForNewsFeed,description);
                        //verify existing criteria ,but group name is not same
                        userManagementPage.verifyAddNewsFeedGroupWithDifNameSameCriterias(groupNameForNewsFeed2,description,criteria);

                        String groupTestMessAftUpdate = userManagementPage.updateNewsFeedDynamicGroup(groupNameForNewsFeed,criteriaUpdate);
                        if (!groupTestMessAftUpdate.equalsIgnoreCase(groupTestMess)) {
                            SimpleUtils.pass("Update News Feed dynamic group successfully");
                        }
                    }else
                        userManagementPage.verifyDefaultMessageIfThereIsNoGroup();
                }else
                    SimpleUtils.fail("Newsfeed group show wrong",false);
            }else
                SimpleUtils.report("Dynamic group for newsfeed is off");

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    //blocked by one bug
//    @Automated(automated = "Automated")
//    @Owner(owner = "Estelle")
//    @Enterprise(name = "Op_Enterprise")
//    @TestName(description = "Remove the condition from drop down list if it's selected")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyRemoveTheConditionFromDropDownListIfItSelectedAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
//
//        try{
//            //go to User Management tab
//            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
//            userManagementPage.clickOnUserManagementTab();
//            //check dynamic group item
//            userManagementPage.iCanSeeDynamicGroupItemTileInUserManagementTab();
//            //go to dynamic group
//            userManagementPage.goToDynamicGroup();
//            userManagementPage.verifyRemoveTheConditionFromDropDownListIfItSelected();
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//
//    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify manage item in User Management access role tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyManageItemInUserManagementAccessRoleTabAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try{
            //go to User Management tab
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            //go to dynamic group
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAccessRolesTab();
            userManagementPage.verifyManageItemInUserManagementAccessRoleTab();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify add and update work role")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false) //is blocked by OPS-6284
    public void verifyAddEditSearchAndDisableWorkRoleAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
            navigationPage.navigateToUserManagement();
            OpsPortalUserManagementPanelPage panelPage = new OpsPortalUserManagementPanelPage();
            panelPage.goToWorkRolesPage();
            OpsPortalWorkRolesPage workRolesPage = new OpsPortalWorkRolesPage();
            //add a new work role and save it
            workRolesPage.addNewWorkRole();
            WorkRoleDetailsPage workRoleDetailsPage = new WorkRoleDetailsPage();
            Random random = new Random();
            String workRoleName="autoWorkRole" + random.nextInt(1000);
            workRoleDetailsPage.editWorkRoleDetails(workRoleName, 3, "Deployed", "3");
            workRoleDetailsPage.addAssignmentRule("3","2","2098");
            workRoleDetailsPage.saveAssignRule();
            workRoleDetailsPage.submit();
            workRolesPage.save();
            workRolesPage.searchByWorkRole(workRoleName);
            Assert.assertEquals(workRolesPage.getTheFirstWorkRoleInTheList(), workRoleName, "Failed to add new work role!");

            //search by work role
            //testcase1: exact matching
            workRolesPage.searchByWorkRole("Ambassador");
            Assert.assertTrue(workRolesPage.getTheFirstWorkRoleInTheList().equalsIgnoreCase("Ambassador"));

            //testcase2: partial matching
            workRolesPage.searchByWorkRole("Key");
            Assert.assertFalse(workRolesPage.getTheFirstWorkRoleInTheList().equalsIgnoreCase("Key"));
            Assert.assertTrue(workRolesPage.getTheFirstWorkRoleInTheList().contains("Key"));

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
            workRolesPage.editAnExistingWorkRole(workRoleName);
            workRoleDetailsPage.editWorkRoleDetails(workRoleName+"-edit", 2, "Deployed", "1.5");
            workRoleDetailsPage.submit();
            workRolesPage.save();
            workRolesPage.searchByWorkRole(workRoleName+"-edit");
            Assert.assertEquals(workRolesPage.getTheFirstWorkRoleInTheList(), workRoleName+"-edit", "Failed to Edit new work role!");

            //edit an existing work role and cancel the editing
            workRolesPage.editAnExistingWorkRole(workRoleName+"-edit");
            workRoleDetailsPage.editWorkRoleDetails(workRoleName+"-cancelEdit", 9, "Deployed", "2");
            workRoleDetailsPage.submit();
            workRolesPage.cancel();
            Assert.assertEquals(workRolesPage.getCancelDialogTitle(), "Cancel Editing?", "Cancel Dialog is not displayed");
            workRolesPage.cancelEditing();
            workRolesPage.searchByWorkRole(workRoleName+"-cancelEdit");
            Assert.assertTrue(workRolesPage.getNoResultNotice().contains("A Work Role defines a category of work that needs to be scheduled."));
            workRolesPage.searchByWorkRole(workRoleName+"-edit");
            Assert.assertEquals(workRolesPage.getTheFirstWorkRoleInTheList(), workRoleName+"-edit", "Failed to cancel the editing!");

            //disable the work role added and it can't be searched out
            workRolesPage.disableAWorkRole(workRoleName+"-edit");
            Assert.assertEquals(workRolesPage.getDisableDialogTitle(), "Disable Work Role", "The disable work role dialog is not displayed.");
            workRolesPage.okToDisableAction();
            workRolesPage.save();
            workRolesPage.searchByWorkRole(workRoleName+"-edit");
            Assert.assertTrue(workRolesPage.getNoResultNotice().contains("A Work Role defines a category of work that needs to be scheduled."));

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Sophia")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Dynamic employee group validation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDynamicEmployeeGroupAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        String manageDynamicEmployeeGroup = "Manage Dynamic Employee Group";
        String removeWarningMsg = "Are you sure you want to remove this dynamic employee group?";
        OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
        navigationPage.navigateToUserManagement();
        OpsPortalUserManagementPanelPage panelPage = new OpsPortalUserManagementPanelPage();

        //get work role list
        panelPage.goToWorkRolesPage();
        OpsPortalWorkRolesPage workRolesPage = new OpsPortalWorkRolesPage();
        List<String> wrList1 = workRolesPage.getWorkRoleList();
        workRolesPage.goBack();

        //get badge list
        panelPage.goToUsersAndRoles();
        UsersAndRolesPage usersAndRolesPage = new UsersAndRolesPage();
        usersAndRolesPage.goToBadges();
        List<String> badgeList = usersAndRolesPage.getBadgeList();
        usersAndRolesPage.back();

        //verified dynamic groups
        panelPage.goToDynamicGroups();
        DynamicEmployeePage dynamicEmployeePage = new DynamicEmployeePage();
        dynamicEmployeePage.addGroup();
        Assert.assertEquals(dynamicEmployeePage.getModalTitle(), manageDynamicEmployeeGroup, "Failed to open manage dynamic group modal");

        //work role
        List<String> wrList2 = dynamicEmployeePage.getCriteriaValues("Work Role");
        Assert.assertTrue(wrList1.size() == wrList2.size() && wrList1.containsAll(wrList2), "Failed to assert that work role options in dynamic group are in accord with Work role list in work roles");

        //employment type  Hourly/Salary - Eligible for Overtime/Salary - No Overtime
        ArrayList<String> empType = new ArrayList<String>();
        empType.add("Hourly");
        empType.add("Salaried");
        /*empType.add("Salary - Eligible for Overtime");
        empType.add("Salary - No Overtime");*/
        List<String> empType1 = dynamicEmployeePage.getCriteriaValues("Employment Type");
        Assert.assertTrue(empType1.size() == empType.size() && empType1.containsAll(empType), "The Employment Type value validate failed!");

        //employment status FullTime/PartTime
        ArrayList<String> empStatus = new ArrayList<String>();
        empStatus.add("FullTime");
        empStatus.add("PartTime");
        List<String> empStatus1 = dynamicEmployeePage.getCriteriaValues("Employment Status");
        Assert.assertTrue(empStatus1.size() == empStatus.size() && empStatus1.containsAll(empStatus), "The Employment Status validate failed! ");

        //Exempt No/Yes
        ArrayList<String> status = new ArrayList<String>();
        status.add("No");
        status.add("Yes");
        List<String> exempt = dynamicEmployeePage.getCriteriaValues("Exempt");
        Assert.assertTrue(exempt.size() == status.size() && exempt.containsAll(status), "The Exempt value validate failed!");

        //Minor No/Yes
        List<String> minor = dynamicEmployeePage.getCriteriaValues("Minor");
        ArrayList<String> minorsExpected = new ArrayList<String>();
        minorsExpected.add(" <14");
        minorsExpected.add("14");
        minorsExpected.add("15");
        minorsExpected.add("16");
        minorsExpected.add("17");
        minorsExpected.add(">=18");
        Assert.assertTrue(minor.containsAll(minorsExpected), "The Minor value validate failed!");

        //Badge list
        List<String> badge = dynamicEmployeePage.getCriteriaValues("Badge");
        //Assert.assertTrue(badgeList.containsAll(badge), "The Badge value validate failed!");
        dynamicEmployeePage.cancelCreating();

        //create a new employee group
        dynamicEmployeePage.removeSpecificGroup("AutoTestCreating");
        dynamicEmployeePage.removeSpecificGroup("TestEdit");
        dynamicEmployeePage.addGroup();
        Assert.assertEquals(dynamicEmployeePage.getModalTitle(), manageDynamicEmployeeGroup, "Failed to open manage dynamic group modal!");
        dynamicEmployeePage.editEmployeeGroup("AutoTestCreating", "create a new group", "autoTest", "Work Role");
        dynamicEmployeePage.saveCreating();

        //cancel creating
        dynamicEmployeePage.addGroup();
        Assert.assertEquals(dynamicEmployeePage.getModalTitle(), manageDynamicEmployeeGroup, "Failed to open manage dynamic group modal!");
        dynamicEmployeePage.editEmployeeGroup("AutoTestCancel", "give up creating a new group", "cancel", "Employment Type");
        dynamicEmployeePage.cancelCreating();

        //search a group
        dynamicEmployeePage.searchGroup("AutoTestCreating");
        //edit an existing group
        dynamicEmployeePage.edit();
        Assert.assertEquals(dynamicEmployeePage.getModalTitle(), manageDynamicEmployeeGroup, "Failed to open manage dynamic group modal!");
        dynamicEmployeePage.editEmployeeGroup("TestEdit", "edit an existing group", "edit", "Employment Status");
        dynamicEmployeePage.saveCreating();

        //cancel editing
        dynamicEmployeePage.searchGroup("TestEdit");
        dynamicEmployeePage.edit();
        Assert.assertEquals(dynamicEmployeePage.getModalTitle(), manageDynamicEmployeeGroup, "Failed to open manage dynamic group modal!");
        dynamicEmployeePage.editEmployeeGroup("TestCancelEdit", "cancel edit", "cancel edit", "Minor");
        dynamicEmployeePage.cancelCreating();

        //cancel remove
        dynamicEmployeePage.remove();
        Assert.assertEquals(dynamicEmployeePage.getContentOfRemoveModal(), removeWarningMsg, "Failed to open the remove modal!");
        dynamicEmployeePage.cancelRemove();

        //remove an existing group
        dynamicEmployeePage.remove();
        Assert.assertEquals(dynamicEmployeePage.getContentOfRemoveModal(), removeWarningMsg, "Failed to open the remove modal!");
        dynamicEmployeePage.removeTheGroup();
        SimpleUtils.pass("Succeeded in removing dynamic group!");

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Plan permissions showing and default value")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPlanPermissionsShowingAndDefaultValueAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        List<String> rolesList = new ArrayList<>(Arrays.asList("Admin","Budget Planner"));
        List<String> rolesList1 = new ArrayList<>(Arrays.asList("Area Manager","Store Manager"));
        int index = 0;
        int index1 = 0;
        UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
        //go to user management tab -> Users and Roles -> Access Roles sub tab
        userManagementPage.clickOnUserManagementTab();
        userManagementPage.goToUserAndRoles();
        userManagementPage.goToAccessRolesTab();
        //Verify plan permission group showing
        userManagementPage.verifyPlanItemInUserManagementAccessRoleTab();
        //Verify Admin and planner should have all plan permissions by default
        for(String role:rolesList){
            index = userManagementPage.getIndexOfRolesInPermissionsTable(role);
//            Assert.assertTrue(userManagementPage.verifyPermissionIsCheckedOrNot(index),role + " has all plan permissions by default");
            if(userManagementPage.verifyPermissionIsCheckedOrNot(index)){
                SimpleUtils.pass(role + " has all plan permissions by default.");
            }else {
                SimpleUtils.fail(role + " Don't has all plan permissions by default.", false);
            }
        }
        for(String role:rolesList1){
            index1 = userManagementPage.getIndexOfRolesInPermissionsTable(role);
           if(!userManagementPage.verifyPermissionIsCheckedOrNot(index1)){
                SimpleUtils.pass(role + " DON'T have all plan permissions by default.");
            }else {
                SimpleUtils.fail(role + " has all plan permissions by default.", false);
            }
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Upload custom access role api")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyUploadCustomAccessRoleApiAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try{
            String storeManager = "Queen Stehr";
            //get session id via login
            String payLoad = "{\"enterpriseName\":\"opauto\",\"userName\":\"stoneman@legion.co\",\"passwordPlainText\":\"admin11.a\",\"sourceSystem\":\"legion\"}";
            String sessionId = TestBase.getSessionId(payLoad);
            //go to User Management tab
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            //go to user profile
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToUserDetailPage(storeManager);
            //check whether additional access roles are added, if added, delete them
            int flag;
            String reponse;
            flag = userManagementPage.verifyAccessRoleSelected();

            reponse = HttpUtil.fileUploadByHttpPost(Constants.uploadUserAccessRole+"?isImport=true&isAsync=false&encrypted=false",sessionId,"\\console-ui-selenium\\src\\test\\resources\\uploadFile\\userAccessRoleNotExist.csv");
            System.out.println("uploadAccessRoleFileReponse" + reponse);
            if (reponse.contains("Employee Id not exists") && reponse.contains("User Access Role not exists")){
                SimpleUtils.pass("upload not exist employee and access role response is as expected");
            }
            //Add additional access role
            if(flag != 1){
                reponse = HttpUtil.fileUploadByHttpPost(Constants.uploadUserAccessRole+"?isImport=true&isAsync=false&encrypted=false",sessionId,"\\console-ui-selenium\\src\\test\\resources\\uploadFile\\userAccessRole.csv");
                System.out.println("uploadAccessRoleReponse:  " + reponse);
                refreshPage();
                LoginPage loginPage = pageFactory.createConsoleLoginPage();
                loginPage.verifyNewTermsOfServicePopUp();

                flag = userManagementPage.verifyAccessRoleSelected();
                if(flag != 1){
                    SimpleUtils.fail("Add additional access role failed",false);
                }
            }
            //Delete additional access role
            if(flag != 2){
                reponse = HttpUtil.fileUploadByHttpPost(Constants.uploadUserAccessRole+"?isImport=true&isAsync=false&encrypted=false",sessionId,"\\console-ui-selenium\\src\\test\\resources\\uploadFile\\userAccessRoleBlank.csv");
                System.out.println("uploadBlankFileReponse:  " + reponse);
                refreshPage();
                LoginPage loginPage = pageFactory.createConsoleLoginPage();
                loginPage.verifyNewTermsOfServicePopUp();
                if(flag != 2){
                    SimpleUtils.fail("Delete additional access role failed",false);
                }
            }
        }catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Work Role - Assignment Rule - Job Title")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false) // is blocked by https://legiontech.atlassian.net/browse/OPS-6251
    public void verifyManageJobTitleAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            //go to User Management tab
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();

            //go to job title page
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToJobTitleAccess();

            //Add job title
            userManagementPage.clickAddJobTitle();
            userManagementPage.inputJobTitleName("op auto add");
            userManagementPage.selectAccessRole();
            userManagementPage.saveJobTitle();

            //Search job title
            userManagementPage.searchJobTitle("op auto add");

            //go back
            OpsPortalWorkRolesPage opsPortalWorkRolesPage = new OpsPortalWorkRolesPage();
            opsPortalWorkRolesPage.goBack();

            //go to work role
            OpsPortalUserManagementPanelPage panelPage = new OpsPortalUserManagementPanelPage();
            panelPage.goToWorkRolesPage();

            opsPortalWorkRolesPage.addNewWorkRole();
            // verify new added job title is display in op assignment rule list
            WorkRoleDetailsPage workRoleDetailsPage = new WorkRoleDetailsPage();
            workRoleDetailsPage.goToTeamMemberSearchBox();
            workRoleDetailsPage.searchTeamMember("op auto add");
            //logout
            OpsPortalNavigationPage opsPortalNavigationPage = new OpsPortalNavigationPage();
            opsPortalNavigationPage.logout();

            //log in with user has contorl manage job title permission
            loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("nancy.nan+admin@legion.co", "admin11.a","verifyMock");
            RightHeaderBarPage rightHeaderBarPage = new RightHeaderBarPage();
            ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
            consoleNavigationPage.searchLocation("ClearDistrict");
            consoleNavigationPage.navigateTo("Controls");

            //go to job title page
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToJobTitleAccess();

            //Add job title
            userManagementPage.clickAddJobTitle();
            userManagementPage.inputJobTitleName("control auto add");
            userManagementPage.selectAccessRole();
            userManagementPage.saveJobTitle();

            //Search job title
            userManagementPage.searchJobTitle("control auto add");

            opsPortalWorkRolesPage.goBack();

            ConsoleControlsPage consoleControlsPage = new ConsoleControlsPage();
            consoleControlsPage.goToTaskAndWorkRolePage();
            //go to work role detail
            consoleControlsPage.goToWorkRolePage();
            consoleControlsPage.goToFirstWorkRoleDetail();
            // verify op and control added job title is display in control assignment rule list
            consoleControlsPage.goToTeamMemberSearchBox();
            workRoleDetailsPage.searchTeamMember("op auto add");
            workRoleDetailsPage.searchTeamMember("control auto add");

            //go to control center
            rightHeaderBarPage.switchToOpsPortal();
            //go to usermanagement
            userManagementPage.clickOnUserManagementTab();
            //go to work role
            panelPage.goToWorkRolesPage();
            opsPortalWorkRolesPage.addNewWorkRole();
            //verify control added job title diaplay in op assignment rule
            workRoleDetailsPage.goToTeamMemberSearchBox();
            workRoleDetailsPage.searchTeamMember("control auto add");
            //go back
            opsPortalWorkRolesPage.goBack();
            OpsCommonComponents opsCommonComponents = new OpsCommonComponents();
            opsCommonComponents.leaveThisPage();
            opsPortalWorkRolesPage.goBack();
            //go to job title page
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToJobTitleAccess();
            //remove job title added in control
            userManagementPage.searchJobTitle("control auto add");
            userManagementPage.removeJobTitle();
            opsCommonComponents.deleteConfirm();
            //remove job title added in op
            userManagementPage.searchJobTitle("op auto add");
            userManagementPage.removeJobTitle();
            opsCommonComponents.deleteConfirm();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Hourly Rate Show Hide Logic")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyHourlyRateShowHideAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try{
            String users = "Nancy TM";
            //go to User Management tab
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            //go to user detail page
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToUserDetailPage(users);

            if(userManagementPage.getHourlyRateValue().contains("***")){
                SimpleUtils.pass("Hourly rate initial value is hide");
            }else
                SimpleUtils.fail("Hourly rate initial value is show",false);

            userManagementPage.clickShowRate();
            if(userManagementPage.getHourlyRateValue().contains("$15")){
                SimpleUtils.pass("Hourly rate value show successfully");
            }else
                SimpleUtils.fail("Hourly rate value show failed",false);

            userManagementPage.clickHideShowRate();
            if(userManagementPage.getHourlyRateValue().contains("***")){
                SimpleUtils.pass("Hourly rate value hide successfully");
            }else
                SimpleUtils.fail("Hourly rate value hide failed",false);

            //go to access role
            userManagementPage.goBack();
            userManagementPage.goToAccessRolesTab();

            //check view hourly rate permission
            userManagementPage.clickProfile();
            userManagementPage.verifyViewHourlyRate();

//            // go to control user management     remove console related script
//            RightHeaderBarPage rightHeaderBarPage = new RightHeaderBarPage();
//            rightHeaderBarPage.switchToConsole();
//            ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
//            consoleNavigationPage.searchLocation("verifyMock");
//            consoleNavigationPage.navigateTo("Controls");
//            ConsoleControlsNewUIPage consoleControlsNewUIPage = new ConsoleControlsNewUIPage();
//            consoleControlsNewUIPage.clickOnControlsUsersAndRolesSection();
//            consoleControlsNewUIPage.searchAndSelectTeamMemberByName(users);
//
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate initial value is hide");
//            }else
//                SimpleUtils.fail("Hourly rate initial value is show",false);
//
//            userManagementPage.clickShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("$15")){
//                SimpleUtils.pass("Hourly rate value show successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value show failed",false);
//
//            userManagementPage.clickHideShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate value hide successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value hide failed",false);
//
//            TeamPage teamPage = pageFactory.createConsoleTeamPage();
//            teamPage.goToTeam();
//            teamPage.searchAndSelectTeamMember("TM");
//
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate initial value is hide");
//            }else
//                SimpleUtils.fail("Hourly rate initial value is show",false);
//
//            userManagementPage.clickShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("$15")){
//                SimpleUtils.pass("Hourly rate value show successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value show failed",false);
//
//            userManagementPage.clickHideShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate value hide successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value hide failed",false);
//
//            //go to my profile
//            rightHeaderBarPage.switchToMyProfile();
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate initial value is hide");
//            }else
//                SimpleUtils.fail("Hourly rate initial value is show",false);
//
//            userManagementPage.clickShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("$0")){
//                SimpleUtils.pass("Hourly rate value show successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value show failed",false);
//
//            userManagementPage.clickHideShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate value hide successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value hide failed",false);
//
//            //logout
//            OpsPortalNavigationPage opsPortalNavigationPage = new OpsPortalNavigationPage();
//            opsPortalNavigationPage.logout();
//
//            //log in with user has no view hourly rate job title permission
//            loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("nancy.nan+customer@legion.co", "admin11.a","verifyMock");
//            //go to team
//            consoleNavigationPage.searchLocation("verifyMock");
//            consoleNavigationPage.navigateTo("Team");
//
//            teamPage.goToTeam();
//            teamPage.searchAndSelectTeamMember("TM");
//
//            boolean isHourlyRateDisplay;
//            isHourlyRateDisplay = userManagementPage.isHourlyRateExist();
//
//            if(isHourlyRateDisplay == false)
//                SimpleUtils.pass("Hourly rate doesn't display");
//            else
//                SimpleUtils.fail("Hourly rate display",false);
//
//            //go to controls
//            consoleNavigationPage.navigateTo("Controls");
//            consoleControlsNewUIPage.clickOnControlsUsersAndRolesSection();
//            consoleControlsNewUIPage.searchAndSelectTeamMemberByName(users);
//
//            isHourlyRateDisplay = userManagementPage.isHourlyRateExist();
//
//            if(isHourlyRateDisplay == false)
//                SimpleUtils.pass("Hourly rate doesn't display");
//            else
//                SimpleUtils.fail("Hourly rate display",false);
//
//            //go to my profile
//            rightHeaderBarPage.switchToMyProfile();
//
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate initial value is hide");
//            }else
//                SimpleUtils.fail("Hourly rate initial value is show",false);
//
//            userManagementPage.clickShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("$15")){
//                SimpleUtils.pass("Hourly rate value show successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value show failed",false);
//
//            userManagementPage.clickHideShowRate();
//            if(userManagementPage.getHourlyRateValue().contains("***")){
//                SimpleUtils.pass("Hourly rate value hide successfully");
//            }else
//                SimpleUtils.fail("Hourly rate value hide failed",false);
//
//            //go to control center
//            rightHeaderBarPage.switchToOpsPortal();
//            //go to user management
//            userManagementPage.clickOnUserManagementTab();
//            //go to user detail page
//            userManagementPage.goToUserAndRoles();
//            userManagementPage.goToUserDetailPage(users);
//
//            isHourlyRateDisplay = userManagementPage.isHourlyRateExist();
//
//            if(isHourlyRateDisplay == false)
//                SimpleUtils.pass("Hourly rate doesn't display");
//            else
//                SimpleUtils.fail("Hourly rate display",false);
        }catch(Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Permission to view employee contact info in profile")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyViewEmployeeContactInfoPermissionAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try{
            //go to User Management Access Role table
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();

            boolean profileViewPermision;
            Integer profilePermission;
            String user1 = "Nancy Profile";
            String user2 = "Test ProfilePermission";
            userManagementPage.goToUserDetailPage(user1);
            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 0){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            userManagementPage.goBack();
            userManagementPage.goToAccessRolesTab();

            //check view contact permission
            userManagementPage.clickProfile();
            profileViewPermision = userManagementPage.profileViewPermissionExist();

            if(profileViewPermision == true){
                SimpleUtils.pass("View profile permission is exist");
            }else
                SimpleUtils.fail("View profile permission is not exist",false);

            switchToNewWindow();

            ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
            consoleNavigationPage.searchLocation("verifyMock");
            consoleNavigationPage.navigateTo("Team");

            TeamPage teamPage = pageFactory.createConsoleTeamPage();

            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(user1);

            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 0){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            ConsoleControlsNewUIPage consoleControlsNewUIPage = new ConsoleControlsNewUIPage();

            consoleNavigationPage.navigateTo("Controls");
            consoleControlsNewUIPage.clickOnControlsUsersAndRolesSection();
            consoleControlsNewUIPage.searchAndSelectTeamMemberByName(user1);

            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 0){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            //logout
            OpsPortalNavigationPage opsPortalNavigationPage = new OpsPortalNavigationPage();
            opsPortalNavigationPage.logout();

            //log in with user has no view contact info job title permission
            loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("nancy.nan+nocontact@legion.co", "admin11.a","verifyMock");
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.verifyNewTermsOfServicePopUp();
            //go to team
            consoleNavigationPage.searchLocation("FionaUsingLocation");
            consoleNavigationPage.navigateTo("Team");

            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(user2);

            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 3){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            consoleNavigationPage.navigateTo("Controls");
            consoleControlsNewUIPage.clickOnControlsUsersAndRolesSection();
            consoleControlsNewUIPage.searchAndSelectTeamMemberByName(user2);

            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 3){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            RightHeaderBarPage rightHeaderBarPage = new RightHeaderBarPage();
            rightHeaderBarPage.switchToOpsPortal();
            loginPage.verifyNewTermsOfServicePopUp();

            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();

            userManagementPage.goToUserDetailPage(user2);
            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 3){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            //logout
            opsPortalNavigationPage.logout();

            //log in with user has no view contact info job title permission
            loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("nancy.nan+nocreate01@legion.co", "admin11.a","verifyMock");
            loginPage.verifyNewTermsOfServicePopUp();
            //go to team
            consoleNavigationPage.searchLocation("VerifyMock");
            consoleNavigationPage.navigateTo("Team");

            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(user1);

            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 3){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            consoleNavigationPage.navigateTo("Controls");
            consoleControlsNewUIPage.clickOnControlsUsersAndRolesSection();
            consoleControlsNewUIPage.searchAndSelectTeamMemberByName(user2);

            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 3){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);

            rightHeaderBarPage.switchToOpsPortal();
            loginPage.verifyNewTermsOfServicePopUp();

            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();

            userManagementPage.goToUserDetailPage(user2);
            profilePermission = userManagementPage.verifyProfilePermission();

            if(profilePermission == 3){
                SimpleUtils.pass("Profile permission is correct");
            }else
                SimpleUtils.fail("Profile permission is wrong",false);
        }catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Job title group tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyJobTitleGroupAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            //go to User Management Access Role table
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToJobTitleGroup();

            userManagementPage.verifyJobTitleGroupTabDisplay();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Badge Searching")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyBadgeSearchingAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try{
            String badgeName = "ForAuto";
            String badgeDescription = "Nancy";
            //go to User Management Access Role table
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();

            userManagementPage.verifyBadgesList("forBadge");
            userManagementPage.searchBadge(badgeName);
            userManagementPage.searchBadge(badgeDescription);

            userManagementPage.verifyBackBtnIsClickable();
            userManagementPage.clickLeaveThisPage();

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();

            userManagementPage.verifySearchWorkRole("forBadge");
            userManagementPage.verifyBadgeInWorkRole();
            userManagementPage.searchBadge(badgeName);
            userManagementPage.searchBadge(badgeDescription);
            userManagementPage.updateBadge();

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();

            locationsPage.searchLocation("NancyTest");
            locationsPage.goToAssignmentRuleOfSearchedLocation("NancyTest");

            userManagementPage.verifySearchWorkRole("forBadge");
            locationsPage.verifyBadgeInLocation();
            userManagementPage.searchBadge(badgeName);
            userManagementPage.searchBadge(badgeDescription);
            userManagementPage.updateBadge();

            switchToNewWindow();
            ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
            consoleNavigationPage.searchLocation("NancyTest");
            consoleNavigationPage.navigateTo("Schedule");

            ConsoleScheduleCommonPage consoleScheduleCommonPage = new ConsoleScheduleCommonPage();
            consoleScheduleCommonPage.goToConsoleScheduleAndScheduleSubMenu();
            consoleScheduleCommonPage.goToSchedule();

        }catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Work Role E2E")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWorkRoleE2EAndCopyWorkRoleAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "locationAutoCreateForYang";
            OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
            navigationPage.navigateToUserManagement();
            OpsPortalUserManagementPanelPage panelPage = new OpsPortalUserManagementPanelPage();
            panelPage.goToWorkRolesPage();
            OpsPortalWorkRolesPage workRolesPage = new OpsPortalWorkRolesPage();
            //add a new work role and save it
            workRolesPage.addNewWorkRole();
            WorkRoleDetailsPage workRoleDetailsPage = new WorkRoleDetailsPage();
            Random random = new Random();
            String workRoleName="autoWorkRole" + random.nextInt(1000);
            workRoleDetailsPage.editWorkRoleDetails(workRoleName, 3, "Deployed", "3");
            workRoleDetailsPage.addAssignmentRule("3","2","2098");
            workRoleDetailsPage.saveAssignRule();
            workRoleDetailsPage.submit();
            workRolesPage.save();
            workRolesPage.searchByWorkRole(workRoleName);
            Assert.assertEquals(workRolesPage.getTheFirstWorkRoleInTheList(), workRoleName, "Failed to add new work role!");
            //Validate work role list is disabled completely  in labor model template
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName("Default","edit");
            laborModelPage.clickOnEditButtonOnTemplateDetailsPage();
            laborModelPage.selectWorkRoles(workRoleName);
            laborModelPage.publishNowTemplate();
            //Validate work role list is disabled completely  in scheduling rules template
            //String workRoleName= "autoWorkRole558";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Rules", "Edit");
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.selectWorkRoleToEdit(workRoleName);
            ArrayList staffingRuleCondition = new ArrayList<>();
            staffingRuleCondition.add("A Minimum");
            staffingRuleCondition.add("1");
            staffingRuleCondition.add(workRoleName);
            staffingRuleCondition.add("Shifts");
            staffingRuleCondition.add("during");
            staffingRuleCondition.add("All Hours");
            staffingRuleCondition.add("Slot");
            locationsPage.addStaffingRulesForWorkRole(staffingRuleCondition);
            locationsPage.clickOnSaveButton();
//            Thread.sleep(600000);
//            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
//            LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
//            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
//            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
//            ForecastPage forecastPage  = pageFactory.createForecastPage();
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToPreviousWeek();
//            forecastPage.clickOnLabor();
//            forecastPage.verifyWorkRoleInList(workRoleName);
            //disable the work role added and it can't be searched out
//            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            navigationPage.navigateToUserManagement();
            panelPage.goToWorkRolesPage();
            workRolesPage.disableAWorkRole(workRoleName);
            Assert.assertEquals(workRolesPage.getDisableDialogTitle(), "Disable Work Role", "The disable work role dialog is not displayed.");
            workRolesPage.okToDisableAction();
            workRolesPage.save();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Add labels to dynamic user group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddLabelsToDynamicUserGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            Random random = new Random();
            String employeeGroupName = "AutoTestCreating" + random.nextInt(100);
            OpsPortalNavigationPage navigationPage = new OpsPortalNavigationPage();
            navigationPage.navigateToUserManagement();
            OpsPortalUserManagementPanelPage panelPage = new OpsPortalUserManagementPanelPage();
            panelPage.goToDynamicGroups();
            DynamicEmployeePage dynamicEmployeePage = new DynamicEmployeePage();
            dynamicEmployeePage.addGroup();
            dynamicEmployeePage.editEmployeeGroup(employeeGroupName, "create a new group", "autoTesNew", "Work Role");
            dynamicEmployeePage.saveCreating();
            dynamicEmployeePage.searchGroupWithLabel("autoTesNew");
            dynamicEmployeePage.verifyGroupIsSearched(employeeGroupName);
            dynamicEmployeePage.removeSpecificGroup(employeeGroupName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user can see template value via click template name if user only have template localization permission")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddLabelsToDynamicUserGroupAsSM(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "locationAutoCreateForYang";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.sMGoToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            //Assignment Rules
            String[] action = {"View"};
            locationsPage.verifyActionsForTemplate("Assignment Rules", action);
            //Scheduling Rules
            locationsPage.verifyActionsForTemplate("Scheduling Rules", action);
            //Labor Model
            locationsPage.verifyActionsForTemplate("Labor Model", action);
            //Operating Hours
            locationsPage.verifyActionsForTemplate("Operating Hours", action);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user can view template if user only have template localization permission +View template")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyUserPermissionpAsSMA(String browser, String username, String password, String location) throws Exception {
        try {
            String locationName = "locationAutoCreateForYang";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.sMGoToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();

            String[] action = {"View"};
            locationsPage.verifyActionsForTemplate("Assignment Rules", action);
            locationsPage.verifyActionsForTemplate("Scheduling Rules", action);
            locationsPage.verifyActionsForTemplate("Labor Model", action);
            locationsPage.verifyActionsForTemplate("Operating Hours", action);
            locationsPage.verifyActionsForTemplate("Compliance", action);
            locationsPage.verifyActionsForTemplate("Scheduling Policies", action);
            locationsPage.verifyActionsForTemplate("Schedule Collaboration", action);
            locationsPage.verifyActionsForTemplate("Time and Attendance", action);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user can create/edit template if user only have template localization permission+ create/edit template permission")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUserPermissionAsSMB(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime=dfs.format(new Date()).trim();
            String templateName="AutoCreate"+currentTime;
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.verifyNewTemplateIsClickable();
            configurationPage.inputTemplateName(templateName);
            configurationPage.goToBusinessHoursEditPage("sunday");
            configurationPage.verifyEachFieldsWithInvalidTexts();
            configurationPage.clickOnCancelButton();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.deleteTemplate(templateName);
            //get template level info of Scheduling rules
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Scheduling Rules");
            configurationPage.verifyNewTemplateIsClickable();

            //get template level info of Scheduling collaboration
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
            configurationPage.verifyNewTemplateIsClickable();

            //get template level info of TA
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Time and Attendance");
            configurationPage.verifyNewTemplateIsClickable();

            //get template level info of Schedule policy
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Scheduling Policies");
            configurationPage.verifyNewTemplateIsClickable();

            //get template level info of Compliance
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Compliance");
            configurationPage.verifyNewTemplateIsClickable();

            //go to labor model tab to get specific template value
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            configurationPage.verifyNewTemplateIsClickable();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Add Update Delete Job title groups")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddUpdateDeleteJobTitleGroupAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = sdf.format(new Date()).trim();
            String jobTitleGroupName ="JobTitleGroup" + currentTime;
            List<String> addHrJobTitles = new ArrayList<>(Arrays.asList("FionaAutoUsing1","FionaAutoUsing2"));
            List<String> updateHrJobTitles = new ArrayList<>(Arrays.asList("FionaAutoUsing3","FionaAutoUsing4"));
            Random random1 = new Random();
            int number1 = random1.nextInt(90)+10;
            String averageHourlyRate= String.valueOf(number1);
            Random random2 = new Random();
            int number2 = random2.nextInt(90) + 10;
            String updateAverageHourlyRate=String.valueOf(number2);
            Random random3 = new Random();
            int number3 = random3.nextInt(10) + 1;
            String allocationOrder=String.valueOf(number3);
            Random random4 = new Random();
            int number4 = random4.nextInt(10) + 1;
            String updateAllocationOrder=String.valueOf(number4);
            boolean isNonManagementGroup=false;
            boolean updateIsNonManagementGroup=true;

            //go to User Management Access Role table
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToJobTitleGroup();
            userManagementPage.verifyJobTitleGroupTabDisplay();
            userManagementPage.clickOnJobTitleGroupTab();
            userManagementPage.addNewJobTitleGroup(jobTitleGroupName,addHrJobTitles,averageHourlyRate,allocationOrder,isNonManagementGroup);
            userManagementPage.updateJobTitleGroup(jobTitleGroupName,updateHrJobTitles,updateAverageHourlyRate,updateAllocationOrder,updateIsNonManagementGroup);
            userManagementPage.deleteJobTitleGroup(jobTitleGroupName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Job Title Group UI checking")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUIOfJobTitleGroupAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            //go to User Management Job Title Groups
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToJobTitleGroup();
            userManagementPage.verifyJobTitleGroupTabDisplay();
            userManagementPage.clickOnJobTitleGroupTab();
            userManagementPage.verifyJobTitleGroupPageUI();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Job Title Group E2E checking")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyE2EOfJobTitleGroupAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            List<String> jobTitleGroups = new ArrayList<>();
            List<String> jobTitlesInAssignmentRule = new ArrayList<>();

            //go to User Management Job Title Groups
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToJobTitleGroup();
            userManagementPage.verifyJobTitleGroupTabDisplay();
            userManagementPage.clickOnJobTitleGroupTab();
            jobTitleGroups = userManagementPage.getAllJobTitleGroups();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();
            userManagementPage.verifyEditBtnIsClickable();
            userManagementPage.clickOnAddWorkRoleButton();
            jobTitlesInAssignmentRule = userManagementPage.getOptionListOfJobTitleInAssignmentRule();
            for(String jobTitle:jobTitleGroups){
                if(jobTitlesInAssignmentRule.contains(jobTitle)){
                    SimpleUtils.pass(jobTitle + " can show well in assignment rule");
                }else {
                    SimpleUtils.fail(jobTitle + " can't show in assignment rule",false);
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Announcement")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyAnnouncementAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
//            String sessionId = getSession();
//            AccrualEngineTest accrualEngineTest = new AccrualEngineTest();
//            if(!accrualEngineTest.isToggleEnabled(sessionId, "CommsAnnouncements")) {
//                String[] toggleResponse = accrualEngineTest.turnOnToggle(sessionId, "CommsAnnouncements");
//                System.out.println("Turn on toggle CommsAnnouncements");
//            }
//            if(!accrualEngineTest.isToggleEnabled(sessionId, "Announcements")) {
//                String[] toggleResponse = accrualEngineTest.turnOnToggle(sessionId, "Announcements");
//                System.out.println("Turn on toggle Announcements");
//            }
//            if(!accrualEngineTest.isToggleEnabled(sessionId, "NewAnnouncements")) {
//                String[] toggleResponse = accrualEngineTest.turnOnToggle(sessionId, "NewAnnouncements");
//                System.out.println("Turn on toggle NewAnnouncements");
//            }
//            if(!accrualEngineTest.isToggleEnabled(sessionId, "ShowAnnouncementGroupOP")) {
//                String[] toggleResponse = accrualEngineTest.turnOnToggle(sessionId, "ShowAnnouncementGroupOP");
//                System.out.println("Turn on toggle ShowAnnouncementGroupOP");
//            }
//            if(accrualEngineTest.isToggleEnabled(sessionId, "DynamicGroupV2")) {
//                String[] toggleResponse = accrualEngineTest.turnOffToggle(sessionId, "DynamicGroupV2");
//                System.out.println("Turn off toggle DynamicGroupV2");
//            }
//            ToggleAPI.enableToggleNew(Toggles.CommsAnnouncements.getValue());
//            ToggleAPI.enableToggleNew(Toggles.Announcements.getValue());
//            ToggleAPI.enableToggleNew(Toggles.NewAnnouncements.getValue());
//
//            ToggleAPI.disableToggleNew(Toggles.DynamicGroupV2.getValue());
//            ToggleAPI.enableToggleNew(Toggles.ShowAnnouncementGroupOP.getValue());

            BasePage.waitForSeconds(300);
            //go to User Management Dynamic Employee Groups
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.verifyDynamicEmployeeGroupContainAnnouncement();
            userManagementPage.goToDynamicEmployeeGroup();

            userManagementPage.verifyBothEmployeeAndAnnouncementDisplay();
            userManagementPage.verifyAnnouncementBlankInfo();

            SimpleDateFormat an = new SimpleDateFormat("yyyyMMddHHmmss");
            String announcementName = "AutoCreate" + an.format(new Date());

            userManagementPage.addAnnouncement(announcementName);
            userManagementPage.searchAccouncement(announcementName);
            userManagementPage.updateAccouncement();
            userManagementPage.deleteAnnouncement();

            //           ToggleAPI.enableToggleNew(Toggles.DynamicGroupV2.getValue());
//            if(!accrualEngineTest.isToggleEnabled(sessionId, "DynamicGroupV2")) {
//                String[] toggleResponse = accrualEngineTest.turnOnToggle(sessionId, "DynamicGroupV2");
//                System.out.println("Turn on toggle DynamicGroupV2");
//            }

            userManagementPage.verifyOnlyAnnouncementDisplay();

            userManagementPage.addAnnouncementForOnlyOneDisplay(announcementName);
            userManagementPage.searchAccouncement(announcementName);
            userManagementPage.updateAccouncement();
            userManagementPage.deleteAnnouncement();

//            ToggleAPI.disableToggleNew(Toggles.DynamicGroupV2.getValue());
//            ToggleAPI.disableToggleNew(Toggles.ShowAnnouncementGroupOP.getValue());
//            if(accrualEngineTest.isToggleEnabled(sessionId, "DynamicGroupV2")) {
//                String[] toggleResponse = accrualEngineTest.turnOffToggle(sessionId, "DynamicGroupV2");
//                System.out.println("Turn off toggle DynamicGroupV2");
//            }
//            if(accrualEngineTest.isToggleEnabled(sessionId, "ShowAnnouncementGroupOP")) {
//                String[] toggleResponse = accrualEngineTest.turnOffToggle(sessionId, "ShowAnnouncementGroupOP");
//                System.out.println("Turn off toggle ShowAnnouncementGroupOP");
//            }
            userManagementPage.verifyOnlyAnnouncementDisplay();
            //ToggleAPI.enableToggleNew(Toggles.DynamicGroupV2.getValue());
//            if(!accrualEngineTest.isToggleEnabled(sessionId, "DynamicGroupV2")) {
//                String[] toggleResponse = accrualEngineTest.turnOnToggle(sessionId, "DynamicGroupV2");
//                System.out.println("Turn on toggle DynamicGroupV2");
//            }

            userManagementPage.verifyDynamicSmartCartNotDispaly();

//            ToggleAPI.disableToggleNew(Toggles.DynamicGroupV2.getValue());
//            if(accrualEngineTest.isToggleEnabled(sessionId, "DynamicGroupV2")) {
//                String[] toggleResponse = accrualEngineTest.turnOffToggle(sessionId, "DynamicGroupV2");
//                System.out.println("Turn off toggle DynamicGroupV2");
//            }
            BasePage.waitForSeconds(180);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Upload/receive EmployeeAttributes via API")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUploadReceiveEmployeeAttributesViaAPIAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String attributeName = "autoAttributeName" + currentTime;
            String attributeType = "String";
            String attributeValue = "autoAttributeValue";
            String attributeDescription = "autoAttributeDescription";
            String EffectiveDate = "2022-12-16";
            String employeeExternalId = "e68ef136-996d-402f-a43e-409210863d4c";
            String employeeName = "Aglae Nolan";
            String accessToken = "a3c89fb99498d24f5fdf9223d519c0d467b54cc5";

            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAttribute();
            userManagementPage.addGlobalAttribute(attributeName, attributeType, attributeValue, attributeDescription);
            userManagementPage.searchGlobalAttribute(attributeName, 1);

            List<HashMap> employeeAttributes = new ArrayList<HashMap>();
            HashMap<String, Object> attribute = new HashMap<>();
            attribute.put("employeeId", "");
            attribute.put("employeeExternalId", employeeExternalId);
            attribute.put("attributeName", attributeName);
            attribute.put("description", attributeDescription);
            attribute.put("booleanValue", null);
            attribute.put("formula", null);
            attribute.put("stringValue", attributeValue);
            attribute.put("numericValue", null);
            attribute.put("asOfEffectiveDate", EffectiveDate);
            attribute.put("deleted", false);
            employeeAttributes.add(attribute);
 //           userManagementPage.uploadEmployeeAttributes(employeeAttributes, 200, accessToken);
//            userManagementPage.getEmployeeAttributes(employeeExternalId, 200, accessToken, attributeName);
//            userManagementPage.removeGlobalAttribute(attributeName);
            userManagementPage.searchGlobalAttribute(attributeName, 1);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify for Global Employee Attribute with String type")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalEmployeeAttributeWithStringTypeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmm");
            String currentTime = dfs.format(new Date()).trim();
            String attributeName = "StringAttribute" + currentTime;
            String attributeNameUpdate = "StringAttribute" + currentTime + "New";
            String attributeType = "String";
            String attributeInvalidValueUpdate = "autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTeueTestByJane123autalueTane123j2stByJane123autoStringValueTestByJane1232";
            String attributeValidValueUpdate = "autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueTestByJane123autoStringValueeueTestByJane123autalueTane123j2stByJane123autoStringValueTestByJane1232";
            String attributeDescription = "StringAttributeDescription";
            String attributeDescriptionUpdate = "NewStringAttributeDescription";

            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAttribute();
            //Create one String employee attribute
            userManagementPage.addGlobalAttribute(attributeName, attributeType, "", attributeDescription);
            userManagementPage.searchGlobalAttribute(attributeName, 1);
            //Update attribute, check error message when attribute value length exceeds 255.
            userManagementPage.updateGlobalAttribute(attributeNameUpdate, attributeType, attributeInvalidValueUpdate, attributeDescriptionUpdate);
            //Update attribute, check error message when attribute value length is 255.
            userManagementPage.updateGlobalAttribute(attributeNameUpdate, attributeType, attributeValidValueUpdate, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(attributeNameUpdate, attributeType, attributeValidValueUpdate);
            //Remove the attribute
            userManagementPage.removeGlobalAttribute(attributeNameUpdate);
            userManagementPage.searchGlobalAttribute(attributeNameUpdate, 0);
            //Create a new attribute with same name which is removed above
            userManagementPage.addGlobalAttribute(attributeNameUpdate, attributeType, attributeValidValueUpdate, attributeDescription);
            userManagementPage.verifyAttributeInformation(attributeNameUpdate, attributeType, attributeValidValueUpdate);
            //Remove the attribute
            userManagementPage.removeGlobalAttribute(attributeNameUpdate);
            userManagementPage.searchGlobalAttribute(attributeNameUpdate, 0);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify for Global Employee Attribute with Numeric type")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalEmployeeAttributeWithNumericTypeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmm");
            String currentTime = dfs.format(new Date()).trim();
            String attributeName = "numericAttribute" + currentTime;
            String attributeNameUpdate = "NewNumericAttribute" + currentTime;
            String attributeType = "Numeric";
            String negativeValueUpdate = "-3.78";
            String PositiveValueUpdate = "8.15";
            String attributeDescription = "NumericAttributeDescription";
            String attributeDescriptionUpdate = "NewNumericAttributeDescription";

            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAttribute();
            //Create one String employee attribute and verify creation is successful
            userManagementPage.addGlobalAttribute(attributeName, attributeType, "Default", attributeDescription);
            userManagementPage.verifyAttributeInformation(attributeName, attributeType, "Default");
            //Update attribute, fill in negative value
            userManagementPage.updateGlobalAttribute(attributeNameUpdate, attributeType, negativeValueUpdate, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(attributeNameUpdate, attributeType, negativeValueUpdate);
            //Update attribute, fill in positive value
            userManagementPage.updateGlobalAttribute(attributeNameUpdate, attributeType, PositiveValueUpdate, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(attributeNameUpdate, attributeType, PositiveValueUpdate);
            //Remove the attribute
            userManagementPage.removeGlobalAttribute(attributeNameUpdate);
            userManagementPage.searchGlobalAttribute(attributeNameUpdate, 0);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify for Global Employee Attribute with Boolean type")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalEmployeeAttributeWithBooleanTypeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmm");
            String currentTime = dfs.format(new Date()).trim();
            String attributeName = "booleanAttribute" + currentTime;
            String attributeNameUpdate = "NewBooleanAttribute" + currentTime;
            String attributeType = "Boolean";
            String valueUpdate = "Yes";
            String attributeDescription = "BooleanAttributeDescription";
            String attributeDescriptionUpdate = "NewBooleanAttributeDescription";

            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAttribute();
            //Create one String employee attribute and verify creation is successful
            userManagementPage.addGlobalAttribute(attributeName, attributeType, "Default", attributeDescription);
            userManagementPage.verifyAttributeInformation(attributeName, attributeType, "Default");
            //Update attribute value to Yes
            userManagementPage.updateGlobalAttribute(attributeNameUpdate, attributeType, valueUpdate, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(attributeNameUpdate, attributeType, valueUpdate);
            //Add an attribute with existing name.
            userManagementPage.addGlobalAttribute(attributeNameUpdate, attributeType, valueUpdate, attributeDescriptionUpdate);
            userManagementPage.verifyAttribute(attributeNameUpdate, valueUpdate);
            //Remove the attribute
            userManagementPage.removeGlobalAttribute(attributeNameUpdate);
            userManagementPage.removeGlobalAttribute(attributeNameUpdate);
            userManagementPage.searchGlobalAttribute(attributeNameUpdate, 0);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify for Global Employee Attribute with Formula type")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalEmployeeAttributeWithFormulaTypeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmm");
            String currentTime = dfs.format(new Date()).trim();
            String attributeName = "FormulaAttribute" + currentTime;
            String attributeNameUpdate = "NewFormulaAttribute" + currentTime;
            String attributeType = "Formula";
            String valueUpdate = "TestFormulaValueWith30LengthJa";
            String attributeDescription = "FormulaAttributeDescription";
            String attributeDescriptionUpdate = "NewFormulaAttributeDescription";

            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAttribute();
            //Create one String employee attribute and verify creation is successful
            userManagementPage.addGlobalAttribute(attributeName, attributeType, "Default", attributeDescription);
            userManagementPage.verifyAttributeInformation(attributeName, attributeType, "Default");
            //Update attribute value
            userManagementPage.updateGlobalAttribute(attributeNameUpdate, attributeType, valueUpdate, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(attributeNameUpdate, attributeType, valueUpdate);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify change type for Global Employee Attribute")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyChangeTypeForGlobalEmployeeAttributeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMdd");
            String currentTime = dfs.format(new Date()).trim();
            String stringAttributeName = "StringAttribute" + currentTime;
            String numericAttributeName = "NumericAttribute" + currentTime;
            String booleanAttributeName = "BooleanAttribute" + currentTime;
            String formulaAttributeName = "FormulaAttribute" + currentTime;
            String numericValue = "-9.6";
            String booleanValue = "Yes";
            String stringValue = "string000";
            String attributeDescriptionUpdate = "UpdateAttributeDescription";

            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAttribute();
            //Search String employee attribute created in current day and chane type to Numeric
            userManagementPage.addGlobalAttribute(stringAttributeName, "String", "test", "attributeDescription");
            userManagementPage.searchGlobalAttribute(stringAttributeName, 1);
            userManagementPage.updateGlobalAttribute(numericAttributeName, "Numeric", numericValue, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(numericAttributeName, "Numeric", numericValue);
            //Search above Numeric employee attribute and change type to Boolean
            userManagementPage.searchGlobalAttribute(numericAttributeName, 1);
            userManagementPage.updateGlobalAttribute(booleanAttributeName, "Boolean", booleanValue, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(booleanAttributeName, "Boolean", booleanValue);
            //Search Formula employee attribute and change type to String
            userManagementPage.addGlobalAttribute(formulaAttributeName, "Formula", "test", "attributeDescription");
            userManagementPage.searchGlobalAttribute(formulaAttributeName, 1);
            userManagementPage.updateGlobalAttribute(stringAttributeName, "String", stringValue, attributeDescriptionUpdate);
            userManagementPage.verifyAttributeInformation(stringAttributeName, "String", stringValue);
            //Remove the attribute
            userManagementPage.removeGlobalAttribute(booleanAttributeName);
            userManagementPage.removeGlobalAttribute(stringAttributeName);
            userManagementPage.searchGlobalAttribute(booleanAttributeName, 0);
            userManagementPage.searchGlobalAttribute(stringAttributeName, 0);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify employee attribute creation validate")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEmployeeAttributeCreationAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String validName1 = "_String123";
            String validName2 = "-String123";
            String invalidName1 = "String123&";
            String invalidName2 = "123String";
            String invalidName3 = "String123-testNameCantExceed60String123-testName-CantExceed60";
            String attributeType = "String";
            String attributeValue = "test";
            String attributeDescription = "This is description";

            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToUserAndRoles();
            userManagementPage.goToAttribute();
            //Create employee attribute with valid name1
            userManagementPage.addGlobalAttribute(validName1, attributeType, attributeValue, attributeDescription);
            userManagementPage.searchGlobalAttribute(validName1, 1);
            //Update employee attribute with valid name2
            userManagementPage.updateGlobalAttribute(validName2, attributeType, attributeValue, attributeDescription);
            userManagementPage.searchGlobalAttribute(validName2, 1);
            //Update employee attribute with invalid name1
            userManagementPage.updateGlobalAttribute(invalidName1, attributeType, attributeValue, attributeDescription);
            //Update employee attribute with invalid name2
            userManagementPage.updateGlobalAttribute(invalidName2, attributeType, attributeValue, attributeDescription);
            //Update employee attribute with invalid name3
            userManagementPage.updateGlobalAttribute(invalidName3, attributeType, attributeValue, attributeDescription);
            //Remove the attribute
            //userManagementPage.removeGlobalAttribute(invalidName3);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}