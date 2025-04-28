package com.legion.tests.core;

import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.junit.Ignore;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getEnterprise;


public class PlanTest extends TestBase {


    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
    private static Map<String, String> newTMDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/AddANewTeamMember.json");
    private static HashMap<String, String> imageFilePath = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ProfileImageFilePath.json");

    public enum modelSwitchOperation {

        Console("Console"),
        OperationPortal("Operation Portal");

        private final String value;

        modelSwitchOperation(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum planLevelSelection{
        NoSubPlans(""),
        levelBu("BU"),
        levelRegion("Region"),
        levelDistrict("District");
        private final String value;
        planLevelSelection(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    public enum statusCheck {

        NotStarted("Not Started"),
        Inprogress("In Progress"),
        Completed("Completed"),
        ForReview("Ready For Review"),
        Approved("Reviewed-Approved"),
        Rejected("Reviewed-Rejected");
        private final String value;
        statusCheck(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum AccessRoles {
        InternalAdmin("InternalAdmin"),
        DistrictManager("DistrictManager"),
        StoreManager("StoreManager"),
        Planner("Planner"),
        DMPlanner("DMPlanner"),
        SMPlanner("SMPlanner"),
        DMWithPlanPermissionPlanner("DMWithPlanPermissionPlanner"),
        SMWithPlanPermissionPlanner("SMWithPlanPermissionPlanner");
        private final String role;
        AccessRoles(final String accessRole) {
            role = accessRole;
        }
        public String getValue() {
            return role;
        }
    }

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            this.createDriver((String) params[0], "83", "Window");
            //Turn on EnableLongTermBudgetPlan
//            ToggleAPI.updateToggle(Toggles.EnableLongTermBudgetPlan.getValue(), "fiona+99@legion.co", "admin11.a", true);
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    public enum approveRejectAction {
        Approve("APPROVE"),
        Reject("REJECT");
        private final String value;

        approveRejectAction(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Plan Creation-Create plan Flow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateAPlanAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            String currentTime =  dfs.format(new Date()).trim();
            String planName = "AutomationCreatedPlanName" +currentTime;
            String regionName="RegionForPlan_Auto";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //create a centralized plan with existing name as fail
            planPage.createANewPlan("testPlan-Not Delete");
            //create a centralized plan
            planPage.createANewPlan(planName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Scenarios Creation flow ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateAScenarioPlanAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            String currentTime =  dfs.format(new Date()).trim();
            String planName = "AutomationCreatedPlanName";
            String scePlanName = "ScenarioPlanName-" +currentTime;
            String regionName="RegionForPlan_Auto";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //Assert create a scenario plan with existing name
            planPage.verifyScenarioPlanAutoCreated("testPlan-Not Delete","testPlan01 scenario-Not delete");
            //Assert the scenario plan created and scenario plan name
            planPage.verifyScenarioPlanAutoCreated(planName,scePlanName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Create plan button available")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreatePlanButtonAvailableAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String regionName="RegionForPlan_Auto";
            String districtName="DistrcitForPlan2";
            String loactionName="Loc1ForDistrict2";
            String BUName="BU-ForPlan";
            String HQName="HQ";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //check create button at region level
            boolean regionAvail=planPage.verifyCreatePlanButtonAvail(regionName);
            if(regionAvail)
                SimpleUtils.pass("Create plan button is available for navigation as Region.");
            else
                SimpleUtils.fail("Not see the create plan button at Region level.",false);
            //check create button at district level
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(districtName);
            boolean districtAvail=planPage.verifyCreatePlanButtonAvail(districtName);
            if(regionAvail)
                SimpleUtils.pass("Create plan button is available for navigation as district.");
            else
                SimpleUtils.fail("Not see the create plan button at district level.",false);
            //check create button at BU level
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(BUName);
            boolean BUAvail=planPage.verifyCreatePlanButtonAvail(BUName);
            if(BUAvail)
                SimpleUtils.pass("Create plan button is available for navigation as BU.");
            else
                SimpleUtils.fail("Not see the create plan button at BU level.",false);
            //check create button at HQ level
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(HQName);
            boolean HQAvail=planPage.verifyCreatePlanButtonAvail(HQName);
            if(HQAvail)
                SimpleUtils.pass("Create plan button is available for navigation as HQ.");
            else
                SimpleUtils.fail("Not see the create plan button at HQ level.",false);
            //check create button at location level
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(loactionName);
            boolean locaAvail=planPage.verifyCreatePlanButtonAvail(loactionName);
            if(!locaAvail)
                SimpleUtils.pass("Create plan button is not available for navigation as location.");
            else
                SimpleUtils.fail("Create plan button is available at location level.",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Plan Creation-Plan landing page UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyParentPlanLandingPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String keywords = "checkPlanCount";
            String regionName="RegionForPlan_Auto";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //check the plan landing page
            planPage.verifyCreatePlanLandingPage(keywords);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Plan Creation-Create plan dialog UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyParentPlanCreatePageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String keywords = "SpecialChars";
            String regionName="RegionForPlan_Auto";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //check the location in plan create dialgo
            if(planPage.getCurrentLocationsForCreatePlan().equals(regionName))
                SimpleUtils.pass("The location is show as current navigator successfully!");
            else
                SimpleUtils.fail("The location in plan creating page is not match to the current navigator",false);
            //check the Create plan dialog UI
            planPage.verifyCreatePlanDialog(keywords);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //it was blocked by https://legiontech.atlassian.net/browse/OPS-4153--specific parent plan issue,will open this case
    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Scenarios Creation-Scenario details page UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScenarioPlanCreateLandingDetailAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("MMddHH");
            String currentDate =  dfs.format(new Date()).trim();
            String planName = "testNew-NotDelete";
            String scPlanName = "Scenario Plan Not Delete";
            String regionName="RegionForPlan_Auto";
            String copiedPlanName="Test Copy Generate Plan" + currentDate;

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //check the created scenario plan detail UI
            planPage.verifyCreatePlanDetailUICheck(planName,scPlanName,copiedPlanName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "scenario plan detail")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScenarioDetailAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String planName = "testPlan-Not Delete";
            String scPlanName = "TestCompletePlan-not delete";
            String regionName="RegionForPlan_Auto";
            String scToTestArchiveInprogress="check archive-not delete";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //check the created scenario plan detail UI
            planPage.verifyPlanDetail(planName,scPlanName);
            //check user can archive an in-progress plan
            planPage.verifyScenarioPlanAutoCreated(planName, scToTestArchiveInprogress);
//            planPage.takeOperationToPlan(planName, scToTestArchiveInprogress, "Not Started");
//            boolean arch=planPage.archiveAPlan(planName,scToTestArchiveInprogress);
//            if(arch)
//                SimpleUtils.pass("User can archive a plan which is in progress status!");
//            else
//                SimpleUtils.fail("User failed to archive a plan which is in progress status!",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "scenario plan-run budget")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyRerunScenarioPlanAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String planName = "PlanUsedToCheckRunAction";
            String scPlanName = "TestRerunBudget"+currentTime;
            String regionName="RegionForPlan_Auto";
            String compleleForecastPlan="CompleteForecastPlan";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //create a new scenario plan
            planPage.verifyScenarioPlanAutoCreated(planName,scPlanName);
            //check the budget run ,stop,rerun link
            planPage.verifyRunBTNInPlanDetail(planName,scPlanName);
            //check a complete plan
            planPage.checkCompleteForecastPlan(planName,compleleForecastPlan);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "scenario plan edit")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEditScenarioPlanAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String planName = "checkPlanCount";
            String scPlanName = "TestScenarioPlanEdit";
            String regionName="RegionForPlan_Auto";
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            //navigate to some region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(regionName);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            //navigate to plan page
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            //create a new scenario plan
            planPage.verifyScenarioPlanAutoCreated(planName,scPlanName);
            //edit the scenario plan and archive it
            planPage.editAScenarioPlan(planName,scPlanName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify different user can see plan tab or not")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDifferentUserCanSeePlanTabOrNotAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        PlanPage planPage = pageFactory.createConsolePlanPage();
        if(planPage.verifyPlanConsoleTabShowing()){
            SimpleUtils.pass("Admin can see plan tab");
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo("DistrcitForPlan2");
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo("Loc1ForDistrict2");
            planPage.clickOnPlanConsoleMenuItem();
            Assert.assertFalse(planPage.verifyCreatePlanButtonShowing(),"There is no create plan button at Location level");
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo("DistrcitForPlan1");
            planPage.clickOnPlanConsoleMenuItem();
            Assert.assertTrue(planPage.verifyCreatePlanButtonShowing(),"There is create plan button at District level");
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("RegionForPlan_Auto");
            planPage.clickOnPlanConsoleMenuItem();
            Assert.assertTrue(planPage.verifyCreatePlanButtonShowing(),"There is create plan button at Region level");
        }else {
            SimpleUtils.fail("Admin can't see plan tab",false);
        }

        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();

        // Login as Planner
        loginAsDifferentRole(AccessRoles.Planner.getValue());
        dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        if(planPage.verifyPlanConsoleTabShowing()){
            SimpleUtils.pass("Planner can see plan tab");
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("RegionForPlan_Auto");
            locationSelectorPage.changeDistrict("DistrcitForPlan2");
            locationSelectorPage.changeLocation("Loc1ForDistrict2");
            planPage.clickOnPlanConsoleMenuItem();
            Assert.assertFalse(planPage.verifyCreatePlanButtonShowing(),"There is no create plan button at Location level");
            locationSelectorPage.changeDistrict("DistrcitForPlan1");
            planPage.clickOnPlanConsoleMenuItem();
            Assert.assertTrue(planPage.verifyCreatePlanButtonShowing(),"There is create plan button at District level");
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("RegionForPlan_Auto");
            planPage.clickOnPlanConsoleMenuItem();
            Assert.assertTrue(planPage.verifyCreatePlanButtonShowing(),"There is create plan button at Region level");
        }else {
            SimpleUtils.fail("Planner can't see plan tab",false);
        }

        loginPage.logOut();
        loginPage.refreshLoginPage();
        // Login as District Manager
        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("fiona+114@legion.co", "admin11.a","");
        dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        if(!planPage.verifyPlanConsoleTabShowing()){
            SimpleUtils.pass("District Manager can't see plan tab by default");
        }else {
            SimpleUtils.fail("District Manager can see plan tab by default",false);
        }

        loginPage.logOut();
        loginPage.refreshLoginPage();
        // Login as StoreManager
        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("fiona+200@legion.co", "Test1234@","");
        if(!planPage.verifyPlanConsoleTabShowing()){
            SimpleUtils.pass("Store Manager can't see plan tab by default");
        }else {
            SimpleUtils.fail("Store Manager can see plan tab by default",false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Store manager can see plan tab after assigned view plan permission")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyStoreManagerCanSeePlanTabAfterAssignedViewPlanPermissionAsSMWithPlanPermissionPlanner (String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        PlanPage planPage = pageFactory.createConsolePlanPage();
        Assert.assertTrue(planPage.verifyPlanConsoleTabShowing(),"There is create plan button for store manager who has assigned view plan permission.");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify new customer access role that assigned view plan permission can see plan tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNewCustomerRoleThatAssignedViewPlanPermissionCanSeePlanTabAsDMWithPlanPermissionPlanner (String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        PlanPage planPage = pageFactory.createConsolePlanPage();
        Assert.assertTrue(planPage.verifyPlanConsoleTabShowing(),"There is plan tab for customer access role that assigned view plan permission.");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Plan Status")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyPlanStatusAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("RegionForPlan_Auto");
        locationSelectorPage.changeDistrict("DistrcitForPlan2");

        PlanPage planPage = pageFactory.createConsolePlanPage();
        planPage.clickOnPlanConsoleMenuItem();

        List<String> plans = new ArrayList<>(Arrays.asList("AutoUsing-ReadyForReview","AutoUsing-Inprogress","AutoUsing-Approved",
                "AutoUsing-Rejected","AutoUsing-NotStart"));

        for(String plan:plans){
            planPage.verifyPlanStatus(plan);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Set In Effect Popup")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySetInEffectPopupAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("RegionForPlan_Auto");
        locationSelectorPage.changeDistrict("DistrcitForPlan2");

        PlanPage planPage = pageFactory.createConsolePlanPage();
        planPage.clickOnPlanConsoleMenuItem();
        String planName ="AutoUsing-CheckSetInEffectPopup";
        String scName ="AutoUsing-CheckSetInEffect";
        planPage.verifySetInEffectPopup(planName,scName);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Labor Budget Section is controlled by toggle")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLaborBudgetSectionIsControlledByToggleAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
//            locationsPage.clickOnLocationsTab();
//            locationsPage.goToGlobalConfigurationInLocations();
//            //Turn off EnableLongTermBudgetPlan toggle
//            ToggleAPI.updateToggle(Toggles.EnableLongTermBudgetPlan.getValue(), "fiona+99@legion.co", "admin11.a", false);
//            refreshPage();
//            if(!locationsPage.isBudgetPlanSectionShowing()){
//                SimpleUtils.pass("Budget plan section is Not showing when EnableLongTermBudgetPlan is off");
//            }else {
//                SimpleUtils.fail("Budget plan section is showing when EnableLongTermBudgetPlan is off",false);
//            }
//            //Turn on EnableLongTermBudgetPlan toggle
//            ToggleAPI.updateToggle(Toggles.EnableLongTermBudgetPlan.getValue(), "fiona+99@legion.co", "admin11.a", true);
//            refreshPage();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            if(locationsPage.isBudgetPlanSectionShowing()){
                SimpleUtils.pass("Budget plan section is showing when EnableLongTermBudgetPlan is on");
            }else {
                SimpleUtils.fail("Budget plan section is NOT showing when EnableLongTermBudgetPlan is on",false);
            }
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

}