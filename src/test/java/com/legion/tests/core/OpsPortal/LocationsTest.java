package com.legion.tests.core.OpsPortal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.legion.api.common.EnterpriseId;
import com.legion.api.login.LoginAPI;
import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LaborModelPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.core.ConsoleLocationSelectorPage;
import com.legion.pages.core.ConsolePlanPage;
import com.legion.pages.core.OpCommons.ConsoleNavigationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.pages.core.opemployeemanagement.TimeOffPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.*;
import cucumber.api.java.ro.Si;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.opencv.ml.NormalBayesClassifier;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.*;
import static com.legion.utils.MyThreadLocal.getDriver;


public class LocationsTest extends TestBase {

    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");

    public enum modelSwitchOperation {

        Console("Console"),
        OperationPortal("Control Cent");

        private final String value;

        modelSwitchOperation(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum locationGroupSwitchOperation {

        MS("Parent Child"),
        PTP("Peer to Peer");

        private final String value;

        locationGroupSwitchOperation(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ohSliderDroppable {
        StartPoint("Start"),
        EndPoint("End");
        private final String value;

        ohSliderDroppable(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
//          AdminPage adminPage = pageFactory.createConsoleAdminPage();
//          adminPage.goToAdminTab();
//          adminPage.rebuildSearchIndex();
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            dashboardPage.navigateToDashboard();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = " create a Type Regular location with effective date as a past date")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateRegularLocationWithAllFieldsAndNavigateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            String currentTime = dfs.format(new Date());
            String locationName = "AutoCreate" + currentTime;
            int index = 0;
            String searchCharactor = "No touch";
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();
            //add new regular location
            locationsPage.addNewRegularLocationWithAllFields(locationName, searchCharactor, index);

            //search created location blocked by https://legiontech.atlassian.net/browse/OPS-2757
//            if (locationsPage.searchNewLocation(locationName)) {
//                SimpleUtils.pass("Create new location successfully: "+locationName);
//            }else
//                SimpleUtils.fail("Create new location failed or can't search created location",true);
//            ArrayList<HashMap<String, String>> locationInfoDetails =locationsPage.getLocationInfo(locationName);
//            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
//            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//            locationSelectorPage.changeDistrict(locationInfoDetails.get(0).get("locationDistrict"));
//            locationSelectorPage.changeLocation(locationInfoDetails.get(0).get("locationName"));
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()) , false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //mock location creation is blocked by https://legiontech.atlassian.net/browse/OPS-2503
    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "create a Type MOCK location that based on a ENABLED status regular location ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateMockLocationAndNavigateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            String currentTime = TestBase.getCurrentTime().substring(4);
            String locationName = "AutoCreate" + currentTime;
            int index = 0;
            String searchCharactor = "No touch";
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();
            //add one mock location，first create one new location and then to mock that -to avoid duplication
            locationsPage.addNewRegularLocationWithAllFields(locationName, searchCharactor, index);
            // Wait for some seconds so that the newly created location can be searched out
            locationsPage.searchNewLocation(locationName);
            locationsPage.addNewMockLocationWithAllFields(locationName, index);
            //search created location
            if (locationsPage.searchNewLocation(locationName + "-MOCK")) {
                SimpleUtils.pass("Create new mock location successfully");
                locationsPage.disableLocation(locationName + "-MOCK");
            } else
                SimpleUtils.fail("Create new location failed or can't search created location", true);
//            ArrayList<HashMap<String, String>> locationInfoDetails =locationsPage.getLocationInfo(locationName);
//            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
//            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//            locationSelectorPage.changeDistrict(locationInfoDetails.get(0).get("locationDistrict"));
//            locationSelectorPage.changeLocation(locationName+"-MOCK");
//           SchedulePage schedulePage = pageFactory.createConsoleScheduleNewUIPage();
//           schedulePage.clickOnScheduleConsoleMenuItem();
//           schedulePage.clickOnScheduleSubTab(ScheduleNewUITest.SchedulePageSubTabText.Overview.getValue());
//           SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",schedulePage.verifyActivatedSubTab(ScheduleNewUITest.SchedulePageSubTabText.Overview.getValue()) , true);
//           schedulePage.clickOnScheduleSubTab(ScheduleNewUITest.SchedulePageSubTabText.Forecast.getValue());
//           SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
//                   schedulePage.verifyActivatedSubTab(ScheduleNewUITest.SchedulePageSubTabText.Forecast.getValue()) , false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //     NSO location is blocked by https://legiontech.atlassian.net/browse/OPS-2757
    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Create a Type NSO location with below conditions successfully")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateNSOLocationAndNavigateAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            String currentTime = dfs.format(new Date());
            String locationName = "AutoCreateNSO" + currentTime;
            int index = 0;
            String searchCharactor = "No touch";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.addNewNSOLocation(locationName, searchCharactor, index);
            if (locationsPage.searchNewLocation(getLocationName())) {
                SimpleUtils.pass("Create new NSO location successfully");
            } else
                SimpleUtils.fail("Create new location failed or can't search created location", true);

            //go to console to and vigate to NSO
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify disable the Type Regular locations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDisableEnableLocationFunctionAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {
            String searchInputText = "status:Enabled";
            String disableLocationName = "";
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();
            disableLocationName = locationsPage.disableLocation(searchInputText);
            locationsPage.enableLocation(disableLocationName);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Cancel to export or import locations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCancelToExportOrImportLocationsAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        try {

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();

            locationsPage.clickOnImportBtn();
            locationsPage.cancelBtnOnImportExportPopUpWinsIsClickable();
            locationsPage.clickOnExportBtn();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Export all/specific location function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyExportLocationDistrictAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        int index = 0;
        String searchCharactor = "*";
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //check locations item
        locationsPage.validateItemsInLocations();
        //go to sub-locations
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.verifyExportAllLocationDistrict();
        locationsPage.verifyExportSpecificLocationDistrict(searchCharactor, index);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify UpperFields list page and search function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUpperFieldsListPageAndSearchFunctionAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            String[] searchInfo = {"BU1", "Level: District", "Level: Region", "status:enabled", "status: disabled"};
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-district  tab
            locationsPage.goToUpperFieldsPage();
            if (locationsPage.verifyUpperFieldListShowWellOrNot()) {
                locationsPage.verifyBackBtnFunction();
                locationsPage.goToUpperFieldsPage();
                locationsPage.verifyPageNavigationFunctionInDistrict();
            } else
                SimpleUtils.fail("UpperFields list page loading failed", false);

            locationsPage.verifySearchUpperFieldsFunction(searchInfo);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Add New Upperfields with different level")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddUpperFieldsWithDiffLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            String currentTime = TestBase.getCurrentTime().substring(4);
            String upperfieldsName = currentTime;
            String upperfieldsId = currentTime;
            String searchChara = "test";
            int index = 0;

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();

            //get organization hierarchy info
            locationsPage.goToGlobalConfigurationInLocations();
            ArrayList<HashMap<String, String>> organizationHierarchyInfo = locationsPage.getOrganizationHierarchyInfo();
            locationsPage.goBackToLocationsTab();
            //go to sub-upperfield  tab
            locationsPage.goToUpperFieldsPage();
            locationsPage.verifyBackBtnInCreateNewUpperfieldPage();
            locationsPage.addNewUpperfieldsWithoutParentAndChild(upperfieldsName, upperfieldsId, searchChara, index, organizationHierarchyInfo);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify disable and enable upperfield")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDisableEnableUpperFieldFunctionAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            String disableAction = "Disable";
            String enableAction = "Enable";
            String searchChara = "status:Disabled";
            int index = 0;

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-district  tab
            locationsPage.goToUpperFieldsPage();
            String upperfieldsName = "";
            ArrayList<HashMap<String, String>> upperfieldInfo = locationsPage.getUpperfieldsInfo(searchChara);
            for (int i = 0; i < upperfieldInfo.size(); i++) {
                if (upperfieldInfo.get(i).get("upperfieldStatus").equalsIgnoreCase("DISABLED") &&
                        upperfieldInfo.get(i).get("numOfLocations").equals("0")) {
                    upperfieldsName = upperfieldInfo.get(i).get("upperfieldName");
                    break;
                }
            }
            //disable and enable upperfield
            locationsPage.disableEnableUpperfield(upperfieldsName, enableAction);
            locationsPage.disableEnableUpperfield(upperfieldsName, disableAction);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify update upperfield")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUpdateUpperFieldFunctionAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        String upperfieldsName = "RegionNoTouch";
        String searchChara = "re";
        int index = 1;
        String districtLevel = "District";
        String regionLevel = "Region";

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //check locations item
        locationsPage.validateItemsInLocations();
        //go to sub-district  tab
        locationsPage.goToUpperFieldsPage();

        //update upperfield
        String updateUpperfield = locationsPage.updateUpperfield(upperfieldsName, upperfieldsName, searchChara, index, districtLevel);

        ArrayList<HashMap<String, String>> upperfieldInfo = locationsPage.getUpperfieldsInfo(updateUpperfield);
        if (upperfieldInfo.get(1).get("upperfieldLevel").equalsIgnoreCase("District")) {
            SimpleUtils.pass("Upperfield update successfully");
            locationsPage.updateUpperfield(updateUpperfield, updateUpperfield, searchChara, index, regionLevel);
        } else
            SimpleUtils.fail("Upperfield update failed", false);

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify cancel creating upperfield")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCancelCreatingUpperfieldFunctionAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String upperfieldsName = currentTime;
            String upperfieldsId = currentTime;
            String level = "District";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-district  tab
            locationsPage.goToUpperFieldsPage();

            //cancel creating upperfield
            locationsPage.cancelCreatingUpperfield(level, upperfieldsName, upperfieldsId);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify upperfield smartcard data")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUpperFieldSmartCardDataAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-district  tab
            locationsPage.goToUpperFieldsPage();

            //get upperfield smart card data

            HashMap<String, Integer> upperfieldSmartCardInfo = locationsPage.getUpperfieldsSmartCardInfo();
            locationsPage.searchUpperFields("Status:Enabled");
            int searchResultNum = locationsPage.getSearchResultNum();
            if (searchResultNum == upperfieldSmartCardInfo.get("Enabled")) {
                SimpleUtils.pass("Enabled data in smart card is correct");
            } else
                SimpleUtils.fail("Enabled data in smart card not equals upperfield list data", false);
            locationsPage.searchUpperFields("Status:Disabled");
            int searchResultNumforDisable = locationsPage.getSearchResultNum();
            if (searchResultNumforDisable == upperfieldSmartCardInfo.get("Disabled")) {
                SimpleUtils.pass("Disabled data in smart card is correct");
            } else
                SimpleUtils.fail("Disabled data in smart card not equals upperfield list data", false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }
//    @Automated(automated = "Automated")
//    @Owner(owner = "Estelle")
//    @Enterprise(name = "Op_Enterprise")
//    @TestName(description = "verify internal location picture")
//    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyInternalLocationPicFunction(String browser, String username, String password, String location) throws Exception {
//
//        try{
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
//            String currentTime =  dfs.format(new Date()).trim();
//            String locationName = "AutoCreate" +currentTime;
//            int index =0;
//            String searchCharactor = "No touch";
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
//            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
//
//            //go to locations tab
//            locationsPage.clickOnLocationsTab();
//            //check locations item
//            locationsPage.validateItemsInLocations();
//            //go to enterprise profile to get enterprise logo and default pic
//            HashMap<String, String> enterpriseInfo = locationsPage.getEnterpriseLogoAndDefaultLocationInfo();
//            locationsPage.verifyBackBtnFunction();
//            //go to sub-locations tab
//            locationsPage.goToSubLocationsInLocationsPage();
//
//            //add new regular location
//            locationsPage.addNewRegularLocationWithMandatoryFields(locationName);
//
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//
//    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Global dynamic group in Locations tab  ")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalDynamicGroupFunctionInLocationsTabAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dfs.format(new Date()).trim();
            String groupNameForWFS = "AutoWFS" + currentTime;
            String groupNameForCloIn = "AutoClockIn" + currentTime;
            String description = "AutoCreate" + currentTime;
            String criteria = "Location Name";
            String criteriaUpdate = "Country";
            String searchText = "AutoCreate";
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check dynamic group item
            locationsPage.iCanSeeDynamicGroupItemInLocationsTab();
            //go to dynamic group
            locationsPage.goToDynamicGroup();
            locationsPage.searchWFSDynamicGroup(searchText);
            //remove existing dynamic group
            locationsPage.iCanDeleteExistingWFSDG();
            //create new workforce sharing dynamic group
            String locationNum = locationsPage.addWorkforceSharingDGWithOneCriteria(groupNameForWFS, description, criteria);
            String locationNumAftUpdate = locationsPage.updateWFSDynamicGroup(groupNameForWFS, criteriaUpdate);
            if (!locationNumAftUpdate.equalsIgnoreCase(locationNum)) {
                SimpleUtils.pass("Update workforce sharing dynamic group successfully");
            }
            locationsPage.searchClockInDynamicGroup(searchText);
            locationsPage.iCanDeleteExistingClockInDG();
            String locationNumForClockIn = locationsPage.addClockInDGWithOneCriteria(groupNameForCloIn, description, criteria);
            String locationNumForClockInAftUpdate = locationsPage.updateClockInDynamicGroup(groupNameForCloIn, criteriaUpdate);
            if (!locationNumForClockInAftUpdate.equalsIgnoreCase(locationNumForClockIn)) {
                SimpleUtils.pass("Update clock in dynamic group successfully");
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Global dynamic group for Clock in")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalDynamicGroupInClockInFunctionAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            List<String> clockInGroup = new ArrayList<>();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check dynamic group item
            locationsPage.iCanSeeDynamicGroupItemInLocationsTab();
            //go to dynamic group
            locationsPage.goToDynamicGroup();
            clockInGroup = locationsPage.getClockInGroupFromGlobalConfig();
            String templateType = "Time & Attendance";
            String mode = "edit";
            String templateName = "UsedByAuto_NoTouchNoDelete";

            //go to configuration page
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateType);
            configurationPage.clickOnSpecifyTemplateName(templateName, mode);
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.verifyClockInDisplayAndSelect(clockInGroup);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify abnormal scenarios")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalDynamicGroupAbnormalScenariosAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            List<String> wfsGroup = new ArrayList<>();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check dynamic group item
            locationsPage.iCanSeeDynamicGroupItemInLocationsTab();
            //go to dynamic group
            locationsPage.goToDynamicGroup();
            wfsGroup = locationsPage.getWFSGroupFromGlobalConfig();
            //go to dynamic group
            locationsPage.verifyCreateExistingDGAndGroupNameIsNull(wfsGroup.get(0));


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Global dynamic group for Workforce Sharing")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalDynamicGroupInWFSAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        String templateType = "Schedule Collaboration";
        String mode = "edit";
        String templateName = "UsedByAuto_NoTouchNoDelete";
        String wfsMode = "Yes";
        String wfsName = "WFS";
        String locationName = "EstelleUsingLocation";
        String criteria = "Custom";

        List<String> wfsGroup = new ArrayList<>();
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isActiveWeekGenerated) {
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("AMBASSADOR"));
            } else if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("MGR ON DUTY"));
            }
            newShiftPage.setStartTimeAndEndTimeForShift("8","9");
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchWithOutSelectTM("FionaUsingLocation");
            boolean aa= shiftOperatePage.verifyWFSFunction();
            if (shiftOperatePage.verifyWFSFunction()) {
                SimpleUtils.pass("Workforce sharing function work well");
            } else{
                //to check WFS group exist or not
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

                //go to locations tab
                locationsPage.clickOnLocationsTab();
                //check dynamic group item
                locationsPage.iCanSeeDynamicGroupItemInLocationsTab();
                //go to dynamic group
                locationsPage.goToDynamicGroup();
                wfsGroup = locationsPage.getWFSGroupFromGlobalConfig();
                for (int i = 0; i < wfsGroup.size(); i++) {
                    if (wfsGroup.get(i).contains(wfsName)) {
                        SimpleUtils.report("Workforce sharing group for automation existing");
                        break;
                    } else
                        locationsPage.addWorkforceSharingDGWithOneCriteria(wfsName, "Used by auto", criteria);
                }

                //to check wfs is on or off in schedule collaboration configuration page
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad(templateType);
                configurationPage.clickOnSpecifyTemplateName(templateName, mode);
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                configurationPage.setWFS(wfsMode);
                configurationPage.selectWFSGroup(wfsName);
                configurationPage.publishNowTheTemplate();

                //go to schedule to generate schedule

                locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
                SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            }
        } else {
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("AMBASSADOR"));
            } else if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("MGR ON DUTY"));
            }
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName("Aglae");

            if (!shiftOperatePage.verifyWFSFunction()) {
                SimpleUtils.fail("Workforce sharing function work failed", false);
            } else
                SimpleUtils.pass("Workforce sharing function work well");
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Parent formula in Workforce Sharing")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyParentFormulaInWFSAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
        String templateType = "Schedule Collaboration";
        String mode = "edit";
        String templateName = "Clear Default";
        String wfsMode = "Yes";
        String wfsName = "Same District";
        String locationName = "EstelleUsingLocation";
        String criteria = "Custom";

        List<String> wfsGroup = new ArrayList<>();
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isActiveWeekGenerated) {
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("AMBASSADOR"));
            } else if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("MGR ON DUTY"));
            }
            newShiftPage.setStartTimeAndEndTimeForShift("8","9");
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchWithOutSelectTM("FionaUsingLocation");
            if (!shiftOperatePage.verifyWFSFunction()) {
                //to check WFS group exist or not
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

                //go to locations tab
                locationsPage.clickOnLocationsTab();
                //check dynamic group item
                locationsPage.iCanSeeDynamicGroupItemInLocationsTab();
                //go to dynamic group
                locationsPage.goToDynamicGroup();
                wfsGroup = locationsPage.getWFSGroupFromGlobalConfig();
                for (int i = 0; i < wfsGroup.size(); i++) {
                    if (wfsGroup.get(i).contains(wfsName)) {
                        SimpleUtils.report("Workforce sharing group for automation existing");
                        break;
                    } else
                        locationsPage.addWorkforceSharingDGWithOneCriteria(wfsName, "Used by auto", criteria);
                }

                //to check wfs is on or off in schedule collaboration configuration page
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad(templateType);
                configurationPage.clickOnSpecifyTemplateName(templateName, mode);
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                configurationPage.setWFS(wfsMode);
                configurationPage.selectWFSGroup(wfsName);
                configurationPage.publishNowTheTemplate();

                //go to schedule to generate schedule

                locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
                SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            } else
                SimpleUtils.pass("Workforce sharing function work well");

        } else {
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("AMBASSADOR"));
            } else if (getDriver().getCurrentUrl().contains(propertyMap.get("Op_Enterprise"))) {
                newShiftPage.selectWorkRole(scheduleWorkRoles.get("MGR ON DUTY"));
            }
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName("Alysha");
            if (!shiftOperatePage.verifyWFSFunction()) {
                SimpleUtils.fail("Workforce sharing function work failed", false);
            } else
                SimpleUtils.pass("Workforce sharing function work well");
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify default value of Organization Hierarchy")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDefaultOrganizationHierarchyShowAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.verifyDefaultOrganizationHierarchy();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate add edit remove organization hierarchy")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAddEditRemoveOrganizationHierarchyAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            List<String> hierarchyNames = new ArrayList<String>() {{
                add("AutoDistrict");
                add("AutoRegion");
                add("AutoBU");
            }};
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.addOrganizationHierarchy(hierarchyNames);
            locationsPage.deleteOrganizationHierarchy(hierarchyNames);
            locationsPage.updateOrganizationHierarchyDisplayName();
            locationsPage.updateEnableUpperfieldViewOfHierarchy();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "abnormal cases of hierarchy")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAbnormalCasesOfOrganizationHierarchyAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.abnormalCaseOfEmptyDisplayNameForHierarchy();
            locationsPage.abnormalCaseOfLongDisplayNameForHierarchy();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    //added by Estelle to verify overridden function in location level
    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify user can see template value via click template name in location level and compare")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUserCanSeeEachTypeOfTemViaClickingTemNameAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            String locationName = "OMLocation16";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            Map<String, HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevelNew();
            locationsPage.canGoToAssignmentRoleViaTemNameInLocationLevel();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Operating Hours","View");
            String contextInOHTemplate = locationsPage.getOHTemplateValueInLocationLevel();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Rules","View");
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Schedule Collaboration","View");
            String contextInScheCollTemplate = locationsPage.getScheCollTemplateValueInLocationLevel();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Time and Attendance","View");
            String contextInTATemplate = locationsPage.getTATemplateValueInLocationLevel();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Policies","View");
            String contextInSchedulingPoliciesTemplate = locationsPage.getSchedulingPoliciesTemplateValueInLocationLevel();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Compliance","View");
            String contextInComplianceTemplate = locationsPage.getComplianceTemplateValueInLocationLevel();
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Labor Model","View");
            locationsPage.backToConfigurationTabInLocationLevel();

            //go to configuration tab to check each template value in template level
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();
            List<HashMap<String, String>> workRolesListInGlobal = locationsPage.getAssignmentRolesInLocationLevel();
            //get template level info of Operation hours
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
                    configurationPage.clickOnConfigurationCrad(templateInfo.get("Operating Hours").get("Template Type"));
            configurationPage.clickOnSpecifyTemplateName(templateInfo.get("Operating Hours").get("Template Name"), "view");
            String specificOHInTemplateLevel = locationsPage.getOHTemplateValueInLocationLevel();

            //get template level info of Scheduling rules
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateInfo.get("Scheduling Rules").get("Template Type"));
            configurationPage.clickOnSpecifyTemplateName(templateInfo.get("Scheduling Rules").get("Template Name"), "view");
            List<HashMap<String, String>> specificSchRolesInTemplateLevel = locationsPage.getScheRulesTemplateValueInConfigurationLevel();

            //get template level info of Scheduling collaboration
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateInfo.get("Schedule Collaboration").get("Template Type"));
            configurationPage.clickOnSpecifyTemplateName(templateInfo.get("Schedule Collaboration").get("Template Name"), "view");
            String specificSchCollInTemplateLevel = locationsPage.getScheCollTemplateValueInLocationLevel();

            //get template level info of TA
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateInfo.get("Time and Attendance").get("Template Type"));
            configurationPage.clickOnSpecifyTemplateName(templateInfo.get("Time and Attendance").get("Template Name"), "view");
            String specificTAInTemplateLevel = locationsPage.getTATemplateValueInLocationLevel();

            //get template level info of Schedule policy
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateInfo.get("Scheduling Policies").get("Template Type"));
            configurationPage.clickOnSpecifyTemplateName(templateInfo.get("Scheduling Policies").get("Template Name"), "view");
            String specificSchPolicyInTemplateLevel = locationsPage.getSchedulingPoliciesTemplateValueInLocationLevel();

            //get template level info of Compliance
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad(templateInfo.get("Compliance").get("Template Type"));
            configurationPage.clickOnSpecifyTemplateName(templateInfo.get("Compliance").get("Template Name"), "view");
            String specificComplianceInTemplateLevel = locationsPage.getTATemplateValueInLocationLevel();

            //go to labor model tab to get specific template value
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateInfo.get("Labor Model").get("Template Name"), "view");
            List<HashMap<String, String>> workRolesListInLaborModelTemplateLevel = laborModelPage.getLaborModelInTemplateLevel();

            //compare location level value with template level
            if (contextInOHTemplate.equalsIgnoreCase(specificOHInTemplateLevel)) {
                SimpleUtils.pass("Operation Hours template value in location level equals to template level");
            } else
                SimpleUtils.fail("Operation Hours template value in location level doesn't equals to template level", false);

            if (contextInSchedulingPoliciesTemplate.contains(specificSchPolicyInTemplateLevel)) {
                SimpleUtils.pass("Schedule Policy value in location level equals to template level");
            } else
                SimpleUtils.fail("Schedule Policy value in location level doesn't equals to template level", false);

            if (contextInScheCollTemplate.contains(specificSchCollInTemplateLevel)) {
                SimpleUtils.pass("Schedule Collaboration template value in location level equals to template level");
            } else
                SimpleUtils.fail("Schedule Collaboration template value in location level doesn't equals to template level", false);

            if (contextInTATemplate.contains(specificTAInTemplateLevel)) {
                SimpleUtils.pass("Time Attendance value in location level equals to template level");
            } else
                SimpleUtils.fail("Time Attendance value in location level doesn't equals to template level", false);

            if (contextInComplianceTemplate.contains(specificComplianceInTemplateLevel)) {
                SimpleUtils.pass("Compliance template value in location level equals to template level");
            } else
                SimpleUtils.fail("Compliance template value in location level doesn't equals to template level", false);

//            String[] contextInScheRulesTemplateAft = contextInScheRulesTemplate.toArray(new String[]{});
//            String[] specificSchRolesInTemplateLevelAft = specificSchRolesInTemplateLevel.toArray(new String[]{});
//            if (contextInScheRulesTemplateAft.equals(specificSchRolesInTemplateLevelAft)) {
//                SimpleUtils.pass("Scheduling rules in location level equals to template level");
//            }else
//                SimpleUtils.fail("Scheduling rules in location level doesn't equals to template level",false);
//
//            //compare assignment rules in location level with work roles list in user management
//            String[] workRolesListInGlobalAft = workRolesListInGlobal.toArray(new String[]{});
//            String[] workRolesListInAssignmentRulesAft = workRolesListInAssignmentRules.toArray(new String[]{});
//            if (workRolesListInGlobalAft.equals(workRolesListInAssignmentRulesAft)) {
//                SimpleUtils.pass("Assignment Rules in location level equals to template level");
//            }else
//                SimpleUtils.fail("Assignment Rules in location level doesn't equals to template level",false);
//
//            String[] workRolesListInLaborModelTemplateLevelAft = workRolesListInLaborModelTemplateLevel.toArray(new String[]{});
//            String[] workRolesListInLaborModelAft = workRolesListInLaborModel.toArray(new String[]{});
//            if (workRolesListInLaborModelAft.equals(workRolesListInLaborModelTemplateLevelAft)) {
//                SimpleUtils.pass("Labor model in location level equals to template level");
//            }else
//                SimpleUtils.fail("Labor model in location level doesn't equals to template level",false);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "View template of Scheduling policy schedule collaboration TA and Compliance")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyViewFunctionOfSchedulingPolicyScheduleCollaborationTAComplianceInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "OMLocation16";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            //get template level info of Scheduling collaboration
            locationsPage.clickActionsForTemplate("Schedule Collaboration", "View");
            String specificSchCollInTemplateLevel = locationsPage.getScheCollTemplateValueInLocationLevel();
            if (!(specificSchCollInTemplateLevel == null)) {
                SimpleUtils.pass("Can view Scheduling collaboration successfully via view button in location level");
            } else
                SimpleUtils.fail("View Scheduling collaboration via view button in location level failed", false);

            locationsPage.backToConfigurationTabInLocationLevel();


            //get template level info of TA
            locationsPage.clickActionsForTemplate("Time and Attendance", "View");
            String contextInTATemplate = locationsPage.getTATemplateValueInLocationLevel();
            if (!(contextInTATemplate == null)) {
                SimpleUtils.pass("Can view Time Attendance successfully via view button in location level");
            } else
                SimpleUtils.fail("View Time Attendance via view button in location level failed", false);
            locationsPage.backToConfigurationTabInLocationLevel();

            //get template level info of Schedule policy
            locationsPage.clickActionsForTemplate("Scheduling Policies", "View");
            String contextInSchedulingPoliciesTemplate = locationsPage.getSchedulingPoliciesTemplateValueInLocationLevel();
            if (!(contextInSchedulingPoliciesTemplate == null)) {
                SimpleUtils.pass("Can view Scheduling Policies successfully via view button in location level");
            } else
                SimpleUtils.fail("View Scheduling Policies via view button in location level failed", false);
            locationsPage.backToConfigurationTabInLocationLevel();

            //get template level info of Compliance
            locationsPage.clickActionsForTemplate("Compliance", "View");
            String contextInComplianceTemplate = locationsPage.getComplianceTemplateValueInLocationLevel();
            if (!(contextInComplianceTemplate == null)) {
                SimpleUtils.pass("Can view Compliance successfully via view button in location level");
            } else
                SimpleUtils.fail("View Compliance via view button in location level failed", false);
            locationsPage.backToConfigurationTabInLocationLevel();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Overridden scheduling rules template in location level")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenSchedulingRulesInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "OMLocation16";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Rules","View");
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Rules","Edit");

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToWorkRolesWithStaffingRules();
            configurationPage.deleteBasicStaffingRule();
            configurationPage.saveBtnIsClickable();
            configurationPage.saveBtnIsClickable();
            locationsPage.verifyOverrideStatusAtLocationLevel("Scheduling Rules","Yes");
            //reset
            locationsPage.clickActionsForTemplate("Scheduling Rules","Reset");
            locationsPage.verifyOverrideStatusAtLocationLevel("Scheduling Rules","No");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Overridden Operating Hours template in location level")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenOperatingHoursInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "OverrideLocationLevelOH";
            int moveCount = 4;
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Operating Hours", "View");
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Operating Hours", "Edit");

            locationsPage.editBtnIsClickableInBusinessHours();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            locationsPage.selectDayInWorkingHoursPopUpWin(6);
            configurationPage.saveBtnIsClickable();
            configurationPage.saveBtnIsClickable();
            locationsPage.verifyOverrideStatusAtLocationLevel("Operating Hours", "Yes");
            //reset
            locationsPage.clickActionsForTemplate("Operating Hours", "Reset");
            locationsPage.verifyOverrideStatusAtLocationLevel("Operating Hours", "No");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Overridden assignment rules template in location level")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenAssignmentRulesInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "OMLocation16";
            String workRoleName = "ForAutomation";
            int index = 0;
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            locationsPage.clickActionsForTemplate("Assignment Rules", "View");
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Assignment Rules", "Edit");
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.verifySearchWorkRole(workRoleName);
            userManagementPage.goToWorkRolesDetails(workRoleName);
            userManagementPage.overriddenAssignmentRule(index);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.saveBtnIsClickable();
            configurationPage.saveBtnIsClickable();
            locationsPage.verifyOverrideStatusAtLocationLevel("Assignment Rules","Yes");

            //reset
            locationsPage.clickActionsForTemplate("Assignment Rules", "Reset");
            locationsPage.verifyOverrideStatusAtLocationLevel("Assignment Rules","No");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Overridden Labor model template in location level")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenLaborModelInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "OMLocation16";
            String workRoleName = "ForAutomation";
            int index = 0;
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            locationsPage.clickActionsForTemplate("Labor Model", "View");
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.clickActionsForTemplate("Labor Model", "Edit");

            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.overriddenLaborModelRuleInLocationLevel(index);
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.saveBtnIsClickable();
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");

            //reset
            locationsPage.clickActionsForTemplate("Labor Model", "Reset");
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","No");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //blocked by https://legiontech.atlassian.net/browse/OPS-5625
    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "User can view the default location level external attribute")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class,enabled = false)
    public void verifyDefaultValueOfExternalAttributesInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "AutoUsingByFiona1";
            String templateName = "AutoUsingByFiona";
            String label = "External Attributes";
            String attributeName = "AutoUsingAttribute";
            String attributeValueUpdate = "23";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();

            //Compare template level external attributes in location tab and configuration tab
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName("AutoUsingByFiona", "view");
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            HashMap<String, List<String>> templateLevelAttributesInfoInTemplate = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevel();
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.canGoToLaborModelViaTemNameInLocationLevel();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            HashMap<String, List<String>> templateLevelAttributesInfoInLocation = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevel();
            for (String key : templateLevelAttributesInfoInLocation.keySet()) {
                for (String key1 : templateLevelAttributesInfoInTemplate.keySet()) {
                    if (key.equals(key1)) {
                        List<String> templateValuesInLocation = templateLevelAttributesInfoInLocation.get(key1);
                        List<String> valuesInTemplate = templateLevelAttributesInfoInTemplate.get(key);

                        if (templateValuesInLocation.get(0).equals(valuesInTemplate.get(0))) {
                            SimpleUtils.pass("The template level attribute " + key + " in location is correct.");
                            break;
                        } else {
                            SimpleUtils.fail("The template level attribute " + key + " in location is NOT correct.", false);
                        }
                    }
                }
            }

            //Compare location level default external attributes value should be same with template level.
            locationsPage.backToConfigurationTabInLocationLevel();
//            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            if (locationsPage.verifyIsOverrideStatusAtLocationLevel("Labor Model")) {
                SimpleUtils.pass("Labor model template is overridden at location level");
                locationsPage.clickActionsForTemplate("Labor Model", "Reset");
            } else {
                SimpleUtils.pass("Labor model template is NOT overridden at location level");

            }

//            locationsPage.actionsForEachTypeOfTemplate(templateInfo.get(7).get("Template Type"), "View");
            locationsPage.clickActionsForTemplate("Labor Model", "View");
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            HashMap<String, List<String>> locationLevelAttributesInfoInLocation = locationsPage.getValueAndDescriptionForEachAttributeAtLocationLevel();
            for (String key : templateLevelAttributesInfoInLocation.keySet()) {
                for (String key1 : locationLevelAttributesInfoInLocation.keySet()) {
                    if (key.equals(key1)) {
                        List<String> valuesInTemplate = templateLevelAttributesInfoInLocation.get(key1);
                        List<String> valuesInLocation = locationLevelAttributesInfoInLocation.get(key);

                        if (valuesInTemplate.get(0).equals(valuesInLocation.get(0))) {
                            SimpleUtils.pass("The location level attribute " + key + " in location is correct.");
                            break;
                        } else {
                            SimpleUtils.fail("The location level attribute " + key + " in location is NOT correct.", false);
                        }
                    }
                }
            }

            //After update template level attributes, the location level will updated accordingly.
            laborModelPage.clickOnLaborModelTab();
            laborModelPage.goToLaborModelTile();
            laborModelPage.clickOnSpecifyTemplateName(templateName, "edit");
            laborModelPage.clickOnEditButtonOnTemplateDetailsPage();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            laborModelPage.updateAttributeValueInTemplate(attributeName, attributeValueUpdate);
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("Details");
            laborModelPage.publishNowTemplate();
            laborModelPage.clickOnSpecifyTemplateName(templateName, "view");
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            HashMap<String, List<String>> templateLevelAttributesInfoInTemplate1 = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevel();

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            locationsPage.canGoToLaborModelViaTemNameInLocationLevel();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            HashMap<String, List<String>> locationLevelAttributesInfoInLocation1 = locationsPage.getValueAndDescriptionForEachAttributeAtLocationLevel();

            for (String key : templateLevelAttributesInfoInTemplate1.keySet()) {
                for (String key1 : locationLevelAttributesInfoInLocation1.keySet()) {
                    if (key.equals(key1)) {
                        List<String> valuesInGlobal = templateLevelAttributesInfoInTemplate1.get(key1);
                        List<String> valuesInTemplate = locationLevelAttributesInfoInLocation1.get(key);

                        if (valuesInGlobal.get(0).equals(valuesInTemplate.get(0))) {
                            SimpleUtils.pass("The location level attribute " + key + " in location is updated according to template correctly.");
                            break;
                        } else {
                            SimpleUtils.fail("The location level attribute " + key + " in location is NOT updated correctly.", false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "User can update location level external attributes")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUpdateExternalAttributesInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {
            String locationName = "AutoUsingByFiona1";
            String label = "External Attributes";
            String attributeName = "AutoUsingAttribute";
            Random random = new Random();
            int number = random.nextInt(90) + 10;
            String attributeValue = String.valueOf(number);
            String attributeDescription = attributeName + attributeValue;

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();

            //override location level external attributes
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfosInLocationLevel();
            locationsPage.clickActionsForTemplate("Labor Model", "Reset");
            locationsPage.clickActionsForTemplate("Labor Model", "Edit");
            locationsPage.resetLocationLevelExternalAttributesInLaborModelTemplate();
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","No");

            locationsPage.clickActionsForTemplate("Labor Model", "Edit");
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            locationsPage.updateLocationLevelExternalAttributes(attributeName, attributeValue, attributeDescription);

            List<HashMap<String, String>> templateInfo1 = locationsPage.getLocationTemplateInfosInLocationLevel();
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");


//          Check the value is updated correct or not?
            locationsPage.clickActionsForTemplate("Labor Model", "View");
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            HashMap<String, List<String>> locationLevelAttributesInfoInLocation = locationsPage.getValueAndDescriptionForEachAttributeAtLocationLevel();
            for (String key : locationLevelAttributesInfoInLocation.keySet()) {
                if (key.equals(attributeName)) {
                    List<String> valuesInLocation = locationLevelAttributesInfoInLocation.get(key);
                    if (valuesInLocation.get(0).equals(attributeValue)) {
                        SimpleUtils.pass("User can update location level external attributes successfully");
                    } else {
                        SimpleUtils.fail("User can't update location level external attributes successfully", false);
                    }
                    break;
                }
            }

            //After update location level attributes, check template level will NOT updated
            locationsPage.backToConfigurationTabInLocationLevel();
            locationsPage.canGoToLaborModelViaTemNameInLocationLevel();
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
            HashMap<String, List<String>> templateLevelAttributesInfoInLocation = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevel();
            for (String key : templateLevelAttributesInfoInLocation.keySet()) {
                if (key.equals(attributeName)) {
                    List<String> templateValuesInLocation = templateLevelAttributesInfoInLocation.get(key);
                    if (!templateValuesInLocation.get(0).equals(attributeValue)) {
                        SimpleUtils.pass("Template level external attributes is not updated after updating location level attributes");
                    } else {
                        SimpleUtils.fail("Template level external attributes is updated after updating location level attributes", false);
                    }
                    break;
                }

            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "External Attribute E2E")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyExternalAttributeE2EAsInternalAdmin (String username, String password, String browser, String location) throws Exception {

        String locationName = "AutoUsingByFiona1";
        String label = "External Attributes";
        String attributeName = "AutoUsingAttribute";
        Random random = new Random();
        int number = random.nextInt(90) + 10;
        String attributeValue = String.valueOf(number);
        String attributeDescription = attributeName + attributeValue;
        HashMap<String, Float> hoursAndWedgetInSummary = new HashMap<>();

        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();

        //override location level external attributes
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.goToLocationDetailsPage(locationName);
        locationsPage.goToConfigurationTabInLocationLevel();
        locationsPage.clickActionsForTemplate("Labor Model", "Reset");
        locationsPage.clickActionsForTemplate("Labor Model", "Edit");
        locationsPage.resetLocationLevelExternalAttributesInLaborModelTemplate();
        locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","No");

        locationsPage.clickActionsForTemplate("Labor Model", "Edit");
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        locationsPage.updateLocationLevelExternalAttributes(attributeName, attributeValue, attributeDescription);

        locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");

        locationsPage.clickModelSwitchIconInDashboardPage(ConfigurationTest.modelSwitchOperation.Console.getValue());
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
        scheduleOverviewPage.loadScheduleOverview();
        ForecastPage ForecastPage = pageFactory.createForecastPage();
        ForecastPage.clickForecast();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.navigateToNextWeek();
        ForecastPage.clickOnLabor();
        ForecastPage.verifyLaborForecastCanLoad();
        //After click on refresh, page should get refresh and back to previous page only
        ForecastPage.verifyRefreshBtnInLaborWeekView();
        hoursAndWedgetInSummary = ForecastPage.getSummaryLaborHoursAndWages();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Location Level External Attributes Description")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationLevelExternalAttributesDescriptionAsInternalAdmin (String username, String password, String browser, String location) throws Exception {

        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTime = dfs.format(new Date()).trim();
        String attributeName = "AutoCreate" + currentTime;
        Random random = new Random();
        int number = random.nextInt(90) + 10;
        String attributeValue = String.valueOf(number);
        String attributeDescription = attributeName + "Des";
        String attributeValueUpdate = String.valueOf(number + 1);
        String attributeDescriptionUpdate = "Update123@";
        String label = "External Attributes";
        HashMap<String, List<String>> attributesInfoInGlobal = new HashMap<>();
        HashMap<String, List<String>> attributesInfoInTemplate = new HashMap<>();
        String locationName = "FionaUsingLocation";
        HashMap<String, List<String>> attributesInfoInLocation = new HashMap<>();

        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

        //go to LaborStandardRepository - External Attributes
        LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
        laborModelPage.clickOnLaborModelTab();
        laborModelPage.goToLaborStandardRepositoryTile();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);

        //Create attributes
        laborModelPage.clickOnEditButton();
        laborModelPage.clickOnAddAttributeButton();
        laborModelPage.createNewAttribute(attributeName, attributeValue, attributeDescription);
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        attributesInfoInGlobal = laborModelPage.getValueAndDescriptionForEachAttributeAtGlobalLevel();
        for (String key : attributesInfoInGlobal.keySet()) {
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

        //Go to location level check the attribute description show well or not?
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.goToLocationDetailsPage(locationName);
        locationsPage.goToConfigurationTabInLocationLevel();
        List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfosInLocationLevel();
        locationsPage.clickActionsForTemplate("Labor Model", "Reset");
        locationsPage.clickActionsForTemplate("Labor Model", "Edit");
        locationsPage.resetLocationLevelExternalAttributesInLaborModelTemplate();
        locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","No");

        locationsPage.clickActionsForTemplate("Labor Model", "View");
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        attributesInfoInLocation = locationsPage.getValueAndDescriptionForEachAttributeAtLocationLevel();
        for (String key : attributesInfoInLocation.keySet()) {
            if (key.equals(attributeName)) {
                List<String> valuesInLocation = attributesInfoInLocation.get(key);
                if (valuesInLocation.get(0).equals(attributeValue) && valuesInLocation.get(1).equals(attributeDescription)) {
                    SimpleUtils.pass("The attribute " + key + " in location level is aligned with global level.");
                    break;
                } else {
                    SimpleUtils.fail("The attribute " + key + " in location level is not aligned with global level.", false);
                }
            }
        }

        //update location level attribute description
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.goToLocationDetailsPage(locationName);
        locationsPage.goToConfigurationTabInLocationLevel();
        locationsPage.clickActionsForTemplate("Labor Model", "Edit");
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        locationsPage.updateLocationLevelExternalAttributes(attributeName, attributeValueUpdate, attributeDescriptionUpdate);

        //Check the location level external attributes updated correct or not?
        List<HashMap<String, String>> templateInfo2 = locationsPage.getLocationTemplateInfosInLocationLevel();
        locationsPage.clickActionsForTemplate("Labor Model", "View");
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        attributesInfoInLocation = locationsPage.getValueAndDescriptionForEachAttributeAtLocationLevel();
        for (String key : attributesInfoInLocation.keySet()) {
            if (key.equals(attributeName)) {
                List<String> valuesInLocation = attributesInfoInLocation.get(key);
                if (valuesInLocation.get(1).equals(attributeDescriptionUpdate)) {
                    SimpleUtils.pass("User can update location level external attributes description successfully");
                } else {
                    SimpleUtils.fail("User failed to update location level external attributes description.", false);
                }
            }
            break;
        }

        //After update location level attributes, the template level will NOT update
        locationsPage.backToConfigurationTabInLocationLevel();
        locationsPage.canGoToLaborModelViaTemNameInLocationLevel();
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        attributesInfoInTemplate = laborModelPage.getValueAndDescriptionForEachAttributeAtTemplateLevelInLocations();
        for (String key : attributesInfoInTemplate.keySet()) {
            if (key.equals(attributeName)) {
                List<String> templateValuesInLocation = attributesInfoInTemplate.get(key);
                if (templateValuesInLocation.get(1).equals(attributeDescription)) {
                    SimpleUtils.pass("Template level external attributes description of " + key + " is not updated after updating location level attributes");
                } else {
                    SimpleUtils.fail("Template level external attributes description of " + key + " is updated after updating location level attributes", false);
                }
                break;
            }
        }

        //Back to global level checking the description should not be updated
        laborModelPage.clickOnLaborModelTab();
        laborModelPage.goToLaborStandardRepositoryTile();
        laborModelPage.selectLaborStandardRepositorySubTabByLabel(label);
        attributesInfoInGlobal = laborModelPage.getValueAndDescriptionForEachAttributeAtGlobalLevel();
        for (String key : attributesInfoInGlobal.keySet()) {
            if (key.equals(attributeName)) {
                List<String> valuesInGlobal = attributesInfoInGlobal.get(key);
                if (valuesInGlobal.get(1).equals(attributeDescription)) {
                    SimpleUtils.pass("The attribute description of " + key + " is not updated in global level");
                    break;
                } else {
                    SimpleUtils.fail("The attribute description of " + key + " is updated in global level", false);
                }
            }
        }

        //Reset location level labor, the description should be reset too
        locationsPage.clickOnLocationsTab();
        locationsPage.goToSubLocationsInLocationsPage();
        locationsPage.goToLocationDetailsPage(locationName);
        locationsPage.goToConfigurationTabInLocationLevel();
        locationsPage.clickActionsForTemplate("Labor Model", "Reset");
        locationsPage.clickActionsForTemplate("Labor Model", "Edit");
        locationsPage.resetLocationLevelExternalAttributesInLaborModelTemplate();
        locationsPage.clickActionsForTemplate("Labor Model", "View");
        laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel(label);
        HashMap<String, List<String>> attributesInfoInLocationAfterReset = locationsPage.getValueAndDescriptionForEachAttributeAtLocationLevel();
        for (String key : attributesInfoInLocationAfterReset.keySet()) {
            if (key.equals(attributeName)) {
                List<String> valuesInLocation = attributesInfoInLocationAfterReset.get(key);
                SimpleUtils.report(valuesInLocation.get(1));
                SimpleUtils.report(attributeDescription);
                if (valuesInLocation.get(1).equals(attributeDescription)) {
                    SimpleUtils.pass("The attribute description of " + key + " in location level is reseted successfully!");
                    break;
                } else {
                    SimpleUtils.fail("The attribute description of " + key + " in location level is not reseted.", false);
                }
            }
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "NSOLocation_Enhancements")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyNSOLocationEnhancementsCheckAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
        String currentTime = dfs.format(new Date());
        String locationName = "ENH_NSO_AutoTestLocation";
        setLocationName(locationName);
        int index = 0;
        String searchCharactor = "No touch";

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsGroupTestInOP.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //check locations item
        locationsPage.validateItemsInLocations();
        //go to sub-locations tab
        locationsPage.goToSubLocationsInLocationsPage();
        //add new NSO location
//            locationsPage.addNewNSOLocation(locationName, searchCharactor,index);
        //check the location created successfully
        if (locationsPage.searchNewLocation(getLocationName())) {
            SimpleUtils.pass("Create new NSO location successfully");
        } else
            SimpleUtils.fail("Create new location failed or can't search created location", true);
        closeCurrentWindow();
        //go to console to and navigate to NSO to verify internal admin can see the location
        switchToConsoleWindow();
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
        //verify customer admin user can see created status location
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();
        /// Login as customer admin user
        String fileName = "UsersCredentials.json";
        fileName = System.getProperty("enterprise") + fileName;
        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
        Object[][] internalCustomerAdminCredentials = userCredentials.get("InternalCustomerAdmin");
        loginToLegionAndVerifyIsLoginDone(String.valueOf(internalCustomerAdminCredentials[0][0]), String.valueOf(internalCustomerAdminCredentials[0][1])
                , String.valueOf(internalCustomerAdminCredentials[0][2]));
        String consoleWindow1 = getDriver().getWindowHandle();
        //check customer admin user can see the NSO created location
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
        //check customer admin user can see Created status NSO location in locations function.
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsGroupTestInOP.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //check locations item
        locationsPage.validateItemsInLocations();
        //go to sub-locations tab
        locationsPage.goToSubLocationsInLocationsPage();
        if (locationsPage.searchNewLocation(getLocationName())) {
            SimpleUtils.pass("Internal Customer Admin can see the NSO location successfully");
        } else
            SimpleUtils.fail("Internal Customer Admin can not see the NSO location", true);
        closeCurrentWindow();
        switchToConsoleWindow();
        //verify  DM user can not see the NSO location at navigator, but can see Created status NSO location in locations function.
        loginPage.logOut();
        Object[][] districtManagerCredentials = userCredentials.get("DistrictManager");
        loginToLegionAndVerifyIsLoginDone(String.valueOf(districtManagerCredentials[0][0]), String.valueOf(districtManagerCredentials[0][1])
                , String.valueOf(districtManagerCredentials[0][2]));
        //check DM user can see the NSO created location--as DM and TM can not naviagte to OPs, so ignore the cases
        if (!locationSelectorPage.findLocationByMagnifyGlassIcon(locationName)) {
            SimpleUtils.pass("District Manager can not see the NSO location successfully");
        } else
            SimpleUtils.fail("District Manager can see the NSO location", true);
        //check SM user can't see Created status NSO location in navigation,can't see Created status NSO location in locations function.
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        //loginToLegionAndVerifyIsLoginDone(String.valueOf(TMCredentials[0][0]), String.valueOf(TMCredentials[0][1]), String.valueOf(TMCredentials[0][2]));
        //check DM user can see the NSO created location
        if (!locationSelectorPage.findLocationByMagnifyGlassIcon(locationName)) {
            SimpleUtils.pass("SM user can not see the NSO location successfully");
        } else
            SimpleUtils.fail("SM user can see the NSO location", true);

    }


    @FindBy(css = "lg-search[fire-on-edit=\"$ctrl.fireSearchOnEdit\"] input")
    private WebElement locationSearchInput;


    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Location common features test")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationCommonFeatureAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
        String currentTime1 = dfs.format(new Date());
        String locationName1 = "AutoTestLocationCheck"+currentTime1;
        int index = 0;
        String searchCharactor = "No touch";
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsGroupTestInOP.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //check locations item
        locationsPage.validateItemsInLocations();
        //go to sub-locations tab
        locationsPage.goToSubLocationsInLocationsPage();
        //location page UI check
        locationsPage.locationPageCommonFeatureCheck();
        //create a regular location with effective day as today
        locationsPage.addNewRegularLocationWithDate(locationName1,searchCharactor, index,0);
        //check the location status
        if(locationsPage.searchLocationAndGetStatus(currentTime1).equals("ENABLED"))
            SimpleUtils.pass("New created location with today as effective day is enabled");
        else
            SimpleUtils.report("New created location with today as effective day status is incorrect");
        String currentTime2 = dfs.format(new Date());
        String locationName2 = "AutoTestLocationCheck"+currentTime2;
        //create a regular location with effective day as a future date
        locationsPage.addNewRegularLocationWithDate(locationName2,searchCharactor, index,-5);
        //check the location status
        if(locationsPage.searchLocationAndGetStatus(currentTime2).equals("CREATED"))
            SimpleUtils.pass("New created location with future effective day is created");
        else
            SimpleUtils.report("New created location with future effective day status is incorrect");

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Source Location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationSourceTypeSelectAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsGroupTestInOP.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();
            //location page UI check
            locationsPage.locationSourceTypeCheck();
            //create a regular/Mock/NSO location as been covered in other test scenarios.

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Lizzy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Add display name in location level template page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationDiaplayNameAtTempLevelAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try {
            String locationName = "Checkpoint A";
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsGroupTestInOP.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //check locations item
            locationsPage.validateItemsInLocations();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();
            //location page UI check
            locationsPage.checkEveryLocationTemplateConfig(locationName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    public int getHttpStatusCode(String[] httpResponse) {
        return Integer.parseInt(httpResponse[0]);
    }

    private String logIn() {
        //header
        HashMap<String, String> loginHeader = new HashMap<String, String>();
        //body
        String loginString = "{\"enterpriseName\":\"opauto\",\"userName\":\"fiona+58@legion.co\",\"passwordPlainText\":\"admin11.a\",\"sourceSystem\":\"legion\"}";
        //post request
        String[] postResponse = HttpUtil.httpPost(Constants.loginUrlRC, loginHeader, loginString);
        Assert.assertEquals(getHttpStatusCode(postResponse), 200, "Failed to login!");
        String sessionId = postResponse[1];
        return sessionId;
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Download translation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDownloadTranslationAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        locationsPage.goToGlobalConfigurationInLocations();

        locationsPage.verifyDownloadTransaltionsButtonisClicked();

        //String sessionId = logIn();
        String sessionId = LoginAPI.getSessionIdFromLoginAPI("estelle+51@legion.co", "admin11.a");

        String reponse[] = HttpUtil.httpGet0(Constants.downloadTransation1,sessionId,null);

        if(reponse[0].equals("200")){
            if(reponse[1].contains("locale") && reponse[1].contains("category") && reponse[1].contains("resourceKey") && reponse[1].contains("translation")){
                SimpleUtils.pass("Download translations successfully");
            }
        }else{
            SimpleUtils.fail("Download translations failed",false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Upload translation")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyUploadTranslationAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        locationsPage.goToGlobalConfigurationInLocations();

        locationsPage.verifyUploadTransaltionsButtonisClicked();

        String sessionId = logIn();
        String reponse = HttpUtil.fileUploadByHttpPost(Constants.uploadTransation,sessionId,"\\console-ui-selenium\\src\\test\\resources\\uploadFile\\Translationstrings.csv");
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Fiscal calendar configuration")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyUserCanUploadFiscalCalendarOfSM(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsGroupTestInOP.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            //go to locations tab
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.verifyUploadFiscalCalendarButtonisClicked();
            //upload valid fiscal calender file
            String sessionId = logIn();
            String reponse = HttpUtil.fileUploadByHttpPost(Constants.uploadFiscalCalendar, sessionId, "\\console-ui-selenium\\src\\test\\resources\\uploadFile\\FisaclCalendarFiles\\FiscalCalendar-2022-2.csv");
            //download the uploaded fiscal calendar file
            locationsPage.downloadFiscalCalendar("2022", "Monday");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Template localization permission")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = true)
    public void verifyTemplateLocalizationPermissionOfDM(String username, String password, String browser, String location) throws Exception {
        try {

            String locationName = "locationAutoCreateForYang";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            //Assignment Rules
            String[] action = {"View"};
            locationsPage.verifyActionsForTemplate("Assignment Rules", action);
            //Scheduling Rules
            locationsPage.verifyActionsForTemplate("Scheduling Rules", action);
            //Labor Model
            String[] actions = {"View", "Edit"};
            locationsPage.verifyActionsForTemplate("Labor Model", actions);
            //Operating Hours
            locationsPage.verifyActionsForTemplate("Operating Hours", actions);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Override Advance staffing rule in location level")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOverriddenAdvanceStaffingRuleInLocationLevelAsInternalAdmin (String browser, String username, String password, String location) throws Exception {

        try {

            String locationName = "locationAutoCreateForYang";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            locationsPage.clickActionsForTemplate("Scheduling Rules", "Edit");

            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.selectWorkRoleToEdit("AMBASSADOR");
            //Verify location level advance staffing rules should be aligned with template level
            List<String> advanceStaffRuleStatues = new ArrayList<>();
            advanceStaffRuleStatues.add("Start at 0 minutes at Opening Business Hours, end at 0 minutes at Closing Business Hours, 1 of AMBASSADOR shift should be scheduled per Sun, Mon, Tue, Wed, Thu, Fri, Sat .");
            configurationPage.verifyAdvanceStaffRuleFromLocationLevel(advanceStaffRuleStatues);
            //Verify location level advance staffing rules should be enabled by default
            advanceStaffRuleStatues = new ArrayList<>();
            advanceStaffRuleStatues.add("This rule is enabled for this location.");
            configurationPage.verifyAdvanceStaffRuleStatusFromLocationLevel(advanceStaffRuleStatues);
            //Verify user can't add location level advance staffing rules.
            //configurationPage.verifyCanNotAddAdvancedStaffingRuleFromTemplateLevel();
            //Verify user can't edit/delete location level location level advance staffing rules.
            //configurationPage.verifyCanNotEditDeleteAdvancedStaffingRuleFromTemplateLevel();
            //Verify user can disable location level location level advance staffing rules.
            configurationPage.changeAdvanceStaffRuleStatusFromLocationLevel(0);
            advanceStaffRuleStatues.clear();
            advanceStaffRuleStatues.add("This rule is disabled for this location.");
            configurationPage.verifyAdvanceStaffRuleStatusFromLocationLevel(advanceStaffRuleStatues);
            //Verify user can enable location level location level advance staffing rules.
            //Verify the template level advance staffing Rules should not be changed after disable/enable location level advance staffing Rules.
            configurationPage.changeAdvanceStaffRuleStatusFromLocationLevel(0);
            advanceStaffRuleStatues.clear();
            advanceStaffRuleStatues.add("This rule is enabled for this location.");
            configurationPage.verifyAdvanceStaffRuleStatusFromLocationLevel(advanceStaffRuleStatues);
            //Verify user can reset location level advance staffing rule successfully.
            configurationPage.changeAdvanceStaffRuleStatusFromLocationLevel(0);
            configurationPage.saveBtnIsClickable();
            configurationPage.saveBtnIsClickable();
            locationsPage.clickActionsForTemplate("Scheduling Rules", "Reset");
            //Verify schdule can be generated correctly according to location level advance staffing rule which has been overridden.

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Assignment Rules content and enable/disable rule/add Badge")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAssignmentRulesInLocationLevelAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String locationName = "locationAutoCreateForYang";
            String workRoleName = "AutoUsedForYang";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            //Validate location - configuration tab should have assignment rules template.
            //Validate user can view location level assignment rules template
            locationsPage.clickActionsForTemplate("Assignment Rules", "Edit");
            //Validate location level assignment rules template should be aligned with global level by default.
            locationsPage.searchWorkRoleInAssignmentRuleTemplate(workRoleName);
            String assignmentRule = "MGR";
            locationsPage.verifyAssignmentRulesFromLocationLevel(assignmentRule);
            //Validate user can enable location level assignment rules template.
            //Validate user can disable location level assignment rules template.
            locationsPage.changeAssignmentRuleStatusFromLocationLevel("disable");
            locationsPage.changeAssignmentRuleStatusFromLocationLevel("enable");
            //Validate user can update badges at location level assignment rules template.;
            locationsPage.addBadgeAssignmentRuleStatusFromLocationLevel("20210713152098");
            //Validate Overridden column for assignment rules template at location level.
            locationsPage.verifyOverrideStatusAtLocationLevel("Assignment Rules","Yes");
            //Validate Last Modified Date and Last Modified By column for assignment rules template at location level.
            //Validate user can reset location level assignment rules template.
            locationsPage.clickActionsForTemplate("Assignment Rules", "Reset");
            locationsPage.verifyOverrideStatusAtLocationLevel("Assignment Rules","No");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Assignment Rules")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAssignmentRulesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try {
            String workRoleName = "AutoUsedForYang";
            String locationName = "locationAutoCreateForYang";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();
            userManagementPage.verifySearchWorkRole(workRoleName);
            userManagementPage.verifyEditBtnIsClickable();
            userManagementPage.goToWorkRolesDetails(workRoleName);
            userManagementPage.addAssignmentRule("Manager", "At all Hours", "At least", 2, 1, "20210713152098");

            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            List<HashMap<String, String>> templateInfo = locationsPage.getLocationTemplateInfoInLocationLevel();
            //Validate the new added assignment rules at global level should be enabled at location level by default.
            locationsPage.clickActionsForTemplate("Assignment Rules", "Edit");
            locationsPage.searchWorkRoleInAssignmentRuleTemplate(workRoleName);
            String assignmentRuleTitle = "Manager";
            locationsPage.verifyAssignmentRulesFromLocationLevel(assignmentRuleTitle);
            //Validate the location level assignment rule's badge info should not be changed after updating global assignment rules when
            //Validate the global assignment rules should not be changed after change one location level assignment rule.
            userManagementPage.verifyAssignmentRuleBadge(assignmentRuleTitle, "20210713152098");
            //Validate user can't update priority at location level assignment rules.
            locationsPage.verifyAssignmentRulePriorityCannotBeEdit(assignmentRuleTitle);
            userManagementPage.clickOnUserManagementTab();
            userManagementPage.goToWorkRolesTile();
            userManagementPage.verifySearchWorkRole(workRoleName);
            userManagementPage.verifyEditBtnIsClickable();
            userManagementPage.goToWorkRolesDetails(workRoleName);
            userManagementPage.deleteAssignmentRule(assignmentRuleTitle);
            //Validate that the global level assignment rule can work well when location didn't have location level assignment rules.
            //Validate that the location level assignment rule can work well when have location level assignment rules.
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
         //   scheduleCommonPage.VerifyStaffListInSchedule(workRoleName);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify that different user can edit forecast data, then he can generate demand based schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = true)
    public void verifyDifferentLegionUserCanGenerateScheduleAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy MMM dd");
            String currentTime = dfs.format(new Date());
            String locationName = "yangUsingNSOLocation";
            LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName("A B");
            teamPage.isProfilePageLoaded();
            teamPage.goToTeam();
  //          teamPage.verifyTheFunctionOfAddNewTeamMemberButton();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            ForecastPage forecastPage  = pageFactory.createForecastPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.goToSpecificWeekByDateNew(currentTime);
            scheduleCommonPage.clickOnFirstWeekInWeekPicker();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            //check the location status
            if(locationsPage.searchLocationAndGetStatus(locationName).equals("ENABLED"))
                SimpleUtils.pass("New created location with today as effective day is enabled");
            else
                SimpleUtils.report("New created location with today as effective day status is incorrect");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify that different legion user can see created status NSO location by default.")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = true)
    public void verifyDifferentLegionUserCanSeeCreatedLocationAsDM(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy MMM dd");
            String currentTime = dfs.format(new Date());
            String locationName = "yangUsingNSOLocation";
            LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(locationName);
            ForecastPage forecastPage  = pageFactory.createForecastPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            forecastPage.verifyEditBtnNotVisible();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Enterprise Profile")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = true)
    public void verifyEnterpriseProfileAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.clickOnEnterpriseProfileCard();
            locationsPage.updateEnterpriseProfileDetailInfo();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Split override/reset of Work Role and Location Attribute")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void VerifySplitOverrideResetOfWorkRoleAndLocationAttributeAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            String locationName = "locationAutoCreateForYang";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(locationName);
            locationsPage.goToConfigurationTabInLocationLevel();
            //Verify Overridden sign is changed once there is modification for Work Role.
            //Verify Overridden sign is changed once there is modification for External Attribute
            locationsPage.clickActionsForTemplate("Labor Model", "Edit");
            LaborModelPage laborModelPage = pageFactory.createOpsPortalLaborModelPage();
            laborModelPage.overriddenLaborModelRuleInLocationLevel(1);
            laborModelPage.clickOnSaveButton();
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");
            locationsPage.clickActionsForTemplate("Labor Model", "Edit");
            laborModelPage.selectLaborModelTemplateDetailsPageSubTabByLabel("External Attributes");
            laborModelPage.updateAttributeValueInTemplate("LongLane", "1");
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");
            //Verify modify and reset is split for work role and external attribute
            locationsPage.clickActionsForTemplate("Labor Model", "Reset");
            //Verify reset work role will not reset external attribute
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","Yes");
            locationsPage.clickActionsForTemplate("Labor Model", "Edit");
            locationsPage.resetLocationLevelExternalAttributesInLaborModelTemplate();
            locationsPage.verifyOverrideStatusAtLocationLevel("Labor Model","No");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify can not change location relationship for location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = true)
    public void verifyCanNotChangeLocationRelationshipForLocationGroupAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            String parentLocationName = "AutoImport_LGP2P_To_SpecificDistrict";
            String childLocationName = "AutoImport_LGP2P_Child1";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(parentLocationName);
            locationsPage.verifyLocationRelationshipForLocationGroup("Parent");
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(childLocationName);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            locationsPage.verifyLocationRelationshipForLocationGroup("Child");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify the first day of week field on district/upperfield page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = true)
    public void verifyFirstDayOfWeekFieldAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();
            consoleNavigationPage.searchLocation("000forAuto");

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            dashboardPage.getWeekFromDate("Wednesday");

            consoleNavigationPage.searchLocation("000forBU");
            dashboardPage.getWeekFromDate("Thursday");

            consoleNavigationPage.searchLocation("000forRegion");
            dashboardPage.getWeekFromDate("Wednesday");

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            locationsPage.clickOnLocationsTab();

            locationsPage.goToUpperFieldsPage();
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            SimpleDateFormat bufs = new SimpleDateFormat("yyMMddHHmmss ");
            SimpleDateFormat regionfs = new SimpleDateFormat("MMddHHmmss ");
            String currentDisTime = dfs.format(new Date());
            String currentBUTime = bufs.format(new Date());
            String currentRegionTime = regionfs.format(new Date());
            String districtName = "AutoCreateDistrict" + currentDisTime;
            String buName = "AutoCreateBU" + currentBUTime;
            String regionName = "AutoCreateRegion" + currentRegionTime;
            locationsPage.addNewDistrictWithFirstDayOfWeek("District", districtName,currentDisTime,"",0);
            locationsPage.searchUpperFields(districtName);
            locationsPage.addNewDistrictWithFirstDayOfWeek("BU", buName,currentBUTime,"",1);
            locationsPage.searchUpperFields(buName);
            locationsPage.addNewDistrictWithFirstDayOfWeek("Region", regionName,currentRegionTime,"",2);
            locationsPage.searchUpperFields(regionName);
            locationsPage.goBack();

            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation("NancyTest");
            locationsPage.checkFirstDayOfWeekNotDisplay();
            locationsPage.goBack();
            locationsPage.goBack();

            locationsPage.goToUpperFieldsPage();
            locationsPage.searchUpperFields("000forAuto");
            locationsPage.checkFirstDayOfWeekDisplay();
            locationsPage.updateFirstDayOfWeek("Wednesday");

            locationsPage.searchUpperFields("000forRegion");
            locationsPage.checkFirstDayOfWeekDisplay();
            locationsPage.updateFirstDayOfWeek("Wednesday");

            locationsPage.searchUpperFields("000forBU");
            locationsPage.checkFirstDayOfWeekDisplay();
            locationsPage.updateFirstDayOfWeek("Thursday");
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Labor Budget Plan Section UI")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLaborBudgetPlanSectionUIAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.verifyUIOfLaborBudgetPlanSection();
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "update Labor Budget Plan settings")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyUpdateLaborBudgetPlanSettingsAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            boolean subPlans = true;
            boolean compressed = true;
            String computeBudgetCost ="Work Role";
            String subPlansLevel = "Region";
            boolean subPlans1 = false;
            boolean compressed1 = false;
            String computeBudgetCost1 ="Work Role";
            String subPlansLevel1 = "Region";
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.clickOnEditButtonOnGlobalConfigurationPage();
            locationsPage.updateLaborBudgetPlanSettings(subPlans,subPlansLevel,compressed,computeBudgetCost);
            locationsPage.saveTheGlobalConfiguration();
            locationsPage.clickOnEditButtonOnGlobalConfigurationPage();
            locationsPage.updateLaborBudgetPlanSettings(subPlans1,subPlansLevel1,compressed1,computeBudgetCost1);
            locationsPage.saveTheGlobalConfiguration();
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "opauto")
    @TestName(description = "Compute LRB by work role or by job title groups")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComputeLRBByWorkRoleOrByJobTitleGroupsAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String time= sdf.format(new Date());
            String planName = "AutoUsing-ComputeMethod";
            String scplan = "AutoUsing-ComputeMethod sce";

            //go to op side to get the getLaborBudgetPlanComputeSettings
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            if(locationsPage.isBudgetPlanSectionShowing()){
                SimpleUtils.pass("Budget plan section is showing when EnableLongTermBudgetPlan is on");
            }else {
                SimpleUtils.report("Budget plan section is NOT showing when EnableLongTermBudgetPlan is off");
            }
            String computeMethod = locationsPage.getLaborBudgetPlanComputeSettings();

            //go to console side to check the plan page UI
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("RegionForPlan_Auto");
            locationSelectorPage.changeDistrict("DistrcitForPlan2");
            PlanPage planPage = pageFactory.createConsolePlanPage();
            planPage.clickOnPlanConsoleMenuItem();
            String workRoleOrJobTitle = planPage.verifyScenarioDetailPageUsingWorkRoleOrJobTitle(planName, scplan);
            if (computeMethod.contains("Job Title Group")){
                if(workRoleOrJobTitle.contains("headcount")){
                    SimpleUtils.pass("When set compute labor budget by job title group hourly rate, Scenario page can show well");
                }else {
                    SimpleUtils.fail("When set compute labor budget by job title group hourly rate, Scenario page can't show well",false);
                }
            }else {
                if(!workRoleOrJobTitle.contains("headcount")){
                    SimpleUtils.pass("When set compute labor budget by work role hourly rate, Scenario page can show well.");
                }else {
                    SimpleUtils.fail("When set compute labor budget by work role hourly rate, Scenario page can't show well",false);
                }
            }

            //go to op side to update the setting
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            if(locationsPage.isBudgetPlanSectionShowing()){
                SimpleUtils.pass("Budget plan section is showing when EnableLongTermBudgetPlan is off");
            }else {
                SimpleUtils.fail("Budget plan section is NOT showing when EnableLongTermBudgetPlan is off",false);
            }
            locationsPage.clickOnEditButtonOnGlobalConfigurationPage();
            locationsPage.UpdateOptionOfComputeBudgetCost();
            locationsPage.saveTheGlobalConfiguration();
            String computeMethod1 = locationsPage.getLaborBudgetPlanComputeSettings();

            //go to console side to check the plan page UI
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.Console.getValue());
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("RegionForPlan_Auto");
            locationSelectorPage.changeDistrict("DistrcitForPlan2");
            planPage.clickOnPlanConsoleMenuItem();
            String workRoleOrJobTitle1 = planPage.verifyScenarioDetailPageUsingWorkRoleOrJobTitle(planName, scplan);
            if (computeMethod1.contains("Job Title Group")){
                if(workRoleOrJobTitle1.contains("headcount")){
                    SimpleUtils.pass("When set compute labor budget by job title group hourly rate, Scenario page can show well");
                }else {
                    SimpleUtils.fail("When set compute labor budget by job title group hourly rate, Scenario page can't show well",false);
                }
            }else {
                if(!workRoleOrJobTitle1.contains("headcount")){
                    SimpleUtils.pass("When set compute labor budget by work role hourly rate, Scenario page can show well.");
                }else {
                    SimpleUtils.fail("When set compute labor budget by work role hourly rate, Scenario page can't show well",false);
                }
            }
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify readyForForecast is added in location details page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyReadyForForecastAddedInLocationDetailsPageAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
            String currentTime =  sdf.format(new Date()).trim();
            String newLocation = "AutoTest" + currentTime;

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //go to sub-locations tab, check readyForForecast for existing location
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(location);
            SimpleUtils.assertOnFail("Field readyForForecast Failed to show up for existing location!", locationsPage.verifyReadyForForecastFieldExist(), false);

            //check readyForForecast for new location, default value should be 'No'
            locationsPage.goBack();
            locationsPage.addNewRegularLocationWithMandatoryFields(newLocation);
            locationsPage.goToLocationDetailsPage(newLocation);
            SimpleUtils.assertOnFail("Field readyForForecast Failed to show up for newly created location!", locationsPage.verifyReadyForForecastFieldExist(), false);
            SimpleUtils.assertOnFail("ReadyForForecast value should be 'No' by default!", locationsPage.getReadyForForecastSelectedOption().equalsIgnoreCase("No"), false);
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify readyForForecast option can be saved correctly")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = true)
    public void verifyReadyForForecastOptionCanBeSavedCorrectlyAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
            String currentTime =  sdf.format(new Date()).trim();
            String newLocation = "AutoTest" + currentTime;
            String optionToSelect = "";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //go to sub-locations tab
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(location);
            //Existing location, Compare readyForForecast value in UI and DB
            String readyForForecastValueInUI = locationsPage.getReadyForForecastSelectedOption();
            System.out.println("readyForForecastValueInUI: " + readyForForecastValueInUI);
            String readyForForecastInDB = DBConnection.queryDB("legionrc.Business", "readyForForecast", "name= 'SEA' and enterpriseId='" + EnterpriseId.opauto.getValue() + "'");
            System.out.println("readyForForecastInDB: " + readyForForecastInDB);
            String parseValueInDB = readyForForecastInDB.equals("1")? "Yes":"No";
            System.out.println("parseValueInDB: " + parseValueInDB);
            if (readyForForecastValueInUI.equalsIgnoreCase(parseValueInDB)){
                SimpleUtils.pass("For existing location, the readyForForecast Value in UI and DB are the same!");
            }else {
                SimpleUtils.fail("For existing locaiton, the readyForForecast Value in UI and DB are NOT the same!", false);
            }
            //Create new location, change readyForForecast from "No" to "Yes"
            locationsPage.goBack();
            locationsPage.addNewRegularLocationWithMandatoryFields(newLocation);
            locationsPage.goToLocationDetailsPage(newLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            locationsPage.chooseReadyForForecastValue("Yes");
            locationsPage.goToLocationDetailsPage(newLocation);
            //New location, Compare readyForForecast value in UI and DB
            String readyForForecastValueInUI1 = locationsPage.getReadyForForecastSelectedOption();
            System.out.println("readyForForecastValueInUI1: " + readyForForecastValueInUI1);
            String readyForForecastInDB1 = DBConnection.queryDB("legionrc.Business", "readyForForecast", "name= '"+ newLocation + "' and enterpriseId='" + EnterpriseId.opauto.getValue() + "'");
            System.out.println("readyForForecastInDB1: " + readyForForecastInDB1);
            String parseValueInDB1 = readyForForecastInDB1.equals("1")? "Yes":"No";
            System.out.println("parseValueInDB1: " + parseValueInDB1);
            if (readyForForecastValueInUI1.equalsIgnoreCase("yes") && readyForForecastValueInUI1.equalsIgnoreCase(parseValueInDB1) ){
                SimpleUtils.pass("For New location, the readyForForecast Value in UI and DB are the same!");
            }else {
                SimpleUtils.fail("For New location, the readyForForecast Value in UI and DB are NOT the same!", false);
            }

            //Change readyForForecast option for newly created location
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            optionToSelect = locationsPage.getReadyForForecastSelectedOption().equalsIgnoreCase("yes")?"No":"Yes";
            locationsPage.chooseReadyForForecastValue(optionToSelect);
            locationsPage.goToLocationDetailsPage(newLocation);

            //After change, Compare readyForForecast value in UI and DB
            String readyForForecastValueInUI2 = locationsPage.getReadyForForecastSelectedOption();
            System.out.println("readyForForecastValueInUI2: " + readyForForecastValueInUI2);
            String readyForForecastInDB2 = DBConnection.queryDB("legionrc.Business", "readyForForecast", "name= '"+ newLocation + "' and enterpriseId='" + EnterpriseId.opauto.getValue() + "'");
            System.out.println("readyForForecastInDB2: " + readyForForecastInDB2);
            String parseValueInDB2 = readyForForecastInDB2.equals("1")? "Yes":"No";
            System.out.println("parseValueInDB2: " + parseValueInDB2);
            if (readyForForecastValueInUI2.equalsIgnoreCase(optionToSelect) && readyForForecastValueInUI2.equalsIgnoreCase(parseValueInDB2) ){
                SimpleUtils.pass("After change for new location, the readyForForecast Value in UI and DB are the same!");
            }else {
                SimpleUtils.fail("After change for new location, the readyForForecast Value in UI and DB are NOT the same!", false);
            }

            //Change readyForForecast option for the existing location
            locationsPage.goBack();
            locationsPage.goToLocationDetailsPage(location);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            optionToSelect = locationsPage.getReadyForForecastSelectedOption().equalsIgnoreCase("yes")?"No":"Yes";
            locationsPage.chooseReadyForForecastValue(optionToSelect);
            locationsPage.goToLocationDetailsPage(location);

            //After change, Compare readyForForecast value in UI and DB
            String readyForForecastValueInUI3 = locationsPage.getReadyForForecastSelectedOption();
            System.out.println("readyForForecastValueInUI3: " + readyForForecastValueInUI3);
            String readyForForecastInDB3 = DBConnection.queryDB("legionrc.Business", "readyForForecast", "name= 'SEA' and enterpriseId='" + EnterpriseId.opauto.getValue() + "'");
            System.out.println("readyForForecastInDB3: " + readyForForecastInDB3);
            String parseValueInDB3 = readyForForecastInDB3.equals("1")? "Yes":"No";
            System.out.println("parseValueInDB3: " + parseValueInDB3);
            if (readyForForecastValueInUI3.equalsIgnoreCase(optionToSelect) && readyForForecastValueInUI3.equalsIgnoreCase(parseValueInDB3) ){
                SimpleUtils.pass("After change for existing location, the readyForForecast Value in UI and DB are the same!");
            }else {
                SimpleUtils.fail("After change for existing location, the readyForForecast Value in UI and DB are NOT the same!", false);
            }
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify readyForForecast when Import existing location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyReadyForForecastWhenImportExistingLocationAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
            String existingLocation = "TestImportUpdateExisting";
            String fileWithNoReadyForForecast = "src/test/resources/uploadFile/LocationTest/UpdateLocationsWithNoReadyForForecast.csv";
            String fileWithReadyForForecastNo = "src/test/resources/uploadFile/LocationTest/UpdateLocationsWithReadyForForecastNo.csv";
            String fileWithReadyForForecastYes = "src/test/resources/uploadFile/LocationTest/UpdateLocationsWithReadyForForecastYes.csv";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //Before import, existing location, get readyForForecast value in UI
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(existingLocation);
            String beforeImportValue = locationsPage.getReadyForForecastSelectedOption();
            locationsPage.goBack();
            //Update an existing location by import file
            locationsPage.importLocations(fileWithNoReadyForForecast, getSession(), "true", 200, "summary.failed",0);

            //After import, existing location, get readyForForecast value in UI
            locationsPage.goToLocationDetailsPage(existingLocation);
            String afterImportValue = locationsPage.getReadyForForecastSelectedOption();
            System.out.println("afterImportValue is: " + afterImportValue);
            SimpleUtils.assertOnFail("ReadyForForecast value should not be changed after import!", beforeImportValue.equalsIgnoreCase(afterImportValue), false);
            locationsPage.goBack();
            //Update the existing location by import file, change  to No
            locationsPage.importLocations(fileWithReadyForForecastNo, getSession(), "true", 200, "summary.failed",0);

            //After import, existing location, get readyForForecast value in UI
            locationsPage.goToLocationDetailsPage(existingLocation);
            afterImportValue = locationsPage.getReadyForForecastSelectedOption();
            System.out.println("afterImportValue is: " + afterImportValue);
            SimpleUtils.assertOnFail("ReadyForForecast value should be changed to No after import!", "No".equalsIgnoreCase(afterImportValue), false);
            locationsPage.goBack();

            //Update the existing location by import file, change  to Yes
            locationsPage.importLocations(fileWithReadyForForecastYes, getSession(), "true", 200, "summary.failed",0);

            //After import, existing location, get readyForForecast value in UI
            locationsPage.goToLocationDetailsPage(existingLocation);
            afterImportValue = locationsPage.getReadyForForecastSelectedOption();
            System.out.println("afterImportValue is: " + afterImportValue);
            SimpleUtils.assertOnFail("ReadyForForecast value should be changed to Yes after import!", "Yes".equalsIgnoreCase(afterImportValue), false);
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify import location group function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyImportLocationGroupAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            String filePath = "src/test/resources/uploadFile/LocationTest/0325Upload3.csv";
            String locationName = "0325Upload3";

            locationsPage.importLocations(filePath, getSession(), "true", 200, "summary.failed",0);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            // disable the imported location
            locationsPage.disableEnableLocation(locationName, "Disable");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Import locations common function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyImportLocationCommonFunctionAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            String filePath = "src/test/resources/uploadFile/LocationTest/0325Upload3.csv";
            locationsPage.importLocations(filePath, getSession(), "true", 200, "summary.failed",0);
            String locationName = "0325Upload3";
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            // disable the imported location
            locationsPage.disableEnableLocation(locationName, "Disable");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify enable/disable location via Location Integration")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEnableDisableLocationViaIntegrationAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            String filePath = "src/test/resources/uploadFile/LocationTest/0325Upload4.csv";
            locationsPage.importLocations(filePath, getSession(), "true", 200, "summary.failed",0);
            String locationName = "0325Upload4";
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            // Enable existing location via location import function
            if (locationsPage.searchLocationAndGetStatus(locationName).equals("ENABLED"))
                SimpleUtils.pass("New created location with today as effective day is enabled");
            else
                SimpleUtils.report("New created location with today as effective day status is incorrect");
            String disabledFilePath = "src/test/resources/uploadFile/LocationTest/0325Upload4Disable.csv";
            // Disable existing location via location import function
            locationsPage.importLocations(disabledFilePath, getSession(), "true", 200, "summary.failed",0);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            if (locationsPage.searchLocationAndGetStatus(locationName).equals("DISABLED"))
                SimpleUtils.pass("New created location with today as effective day is enabled");
            else
                SimpleUtils.report("New created location with today as effective day status is incorrect");
            List column = new ArrayList<>();
            column.add("Status");
            locationsPage.verifyColumnsInLocationSampleFile( getSession(),column);
             } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Owner(owner = "Jane")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify Location Type could not be changed with toggle EnableChangeLocationGroupSetting off")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class,enabled = false)
    public void verifyLocationTypeNotAllowedToChangeWithToggleOffAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            String regularLocation = "RegularNone-Jane";
            String nsoLocation = "NSONone-Jane";
            String parentLocation = "Parent-Jane";
            String childLocation = "Child-Jane";
            String selectedOption = "";
            ArrayList<String> locationGroupSettings = new ArrayList<>(Arrays.asList("None", "Part of a location group", "Parent location"));

            //Turn off toggle EnableChangeLocationGroupSetting
            ToggleAPI.updateToggle(Toggles.EnableChangeLocationGroupSetting.getValue(), "jane.meng+008@legion.co", "P@ssword123",false);
            refreshPage();
            
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //Enter Regular None location edit page, check if location Type can be changed.
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.goToLocationDetailsPage(regularLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail("The selected Option should be 'None'!", selectedOption.equalsIgnoreCase("None"), false);
            SimpleUtils.assertOnFail(selectedOption + " should not be able to change to other option with toggle EnableChangeLocationGroupSetting off",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption) == true, false);
            locationsPage.goBack();

            //Enter NSO location edit page, check if location Type can be changed.
            locationsPage.goToLocationDetailsPage(nsoLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail("The selected Option should be 'None'!", selectedOption.equalsIgnoreCase("None"), false);
            SimpleUtils.assertOnFail(selectedOption + " should not be able to change to other option with toggle EnableChangeLocationGroupSetting off",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption) == true, false);
            locationsPage.goBack();

            //Enter Parent location edit page, check if location Type can be changed.
            locationsPage.goToLocationDetailsPage(parentLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail("The selected Option should be 'Parent location'!", selectedOption.equalsIgnoreCase("Parent location"), false);
            SimpleUtils.assertOnFail(selectedOption + " should not be able to change to other option with toggle EnableChangeLocationGroupSetting off",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption) == true, false);
            locationsPage.goBack();

            //Enter Child location edit page, check if location Type can be changed.
            locationsPage.goToLocationDetailsPage(childLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail("The selected Option should be 'Part of a location group'!", selectedOption.equalsIgnoreCase("Part of a location group"), false);
            SimpleUtils.assertOnFail(selectedOption + " should not be able to change to other option with toggle EnableChangeLocationGroupSetting off",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption) == true, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify Location Group Settings could be changed with toggle EnableChangeLocationGroupSetting on")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationTypeCouldBeChangedWithToggleOnAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
            String currentDate = sdf.format(new Date()).trim();
            String regularLocation = "RegularNone-Jane" + currentDate;
            String p2pLocation = "P2P-Jane" + currentDate;
            String parentLocation = "Parent-Jane" + currentDate;
            String childLocation = "Child-Jane" + currentDate;
            String searchCharacter = "No touch";
            int index = 0;
            String selectedOption = "";
            String locationType = "Regular";
            ArrayList<String> locationGroupSettings = new ArrayList<>(Arrays.asList("None", "Part of a location group", "Parent location"));
            ArrayList<String> parentType = new ArrayList<>(Arrays.asList("Parent Child", "Peer to Peer"));

            //Turn on toggle EnableChangeLocationGroupSetting
//            ToggleAPI.updateToggle(Toggles.EnableChangeLocationGroupSetting.getValue(), "jane.meng+008@legion.co", "P@ssword123",true);
//            refreshPage();

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            //Add a Regular-None location
            locationsPage.addNewRegularLocationWithDate(regularLocation, searchCharacter, index, 0);
            //Add a Parent location
            locationsPage.addParentLocation(locationType, parentLocation, searchCharacter, index, locationGroupSettings.get(2), parentType.get(0));
            //Add a Child location
            locationsPage.addChildLocation(locationType, childLocation, parentLocation, searchCharacter, index, locationGroupSettings.get(1));
            //Add a P2P location
            locationsPage.addParentLocation(locationType, p2pLocation, searchCharacter, index, locationGroupSettings.get(2), parentType.get(1));
            //1.Change Child location to Regular.
            locationsPage.goToLocationDetailsPage(childLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail(selectedOption + " should be able to change other options with toggle EnableChangeLocationGroupSetting on",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption), false);
            locationsPage.changeLocationGroupSettings(selectedOption, locationGroupSettings.get(0));

            //2.Change P2P location to Parent
            locationsPage.goToLocationDetailsPage(p2pLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail(selectedOption + " should be able to change other options with toggle EnableChangeLocationGroupSetting on",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption), false);
            locationsPage.changeLocationGroupSettings(selectedOption, locationGroupSettings.get(2), parentType.get(0));

            //3.Change Parent location to P2P.
            locationsPage.goToLocationDetailsPage(parentLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail(selectedOption + " should be able to change other options with toggle EnableChangeLocationGroupSetting on",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption), false);
            locationsPage.changeLocationGroupSettings(selectedOption, locationGroupSettings.get(2), parentType.get(1));

            //4.Change Regular None to Child
            locationsPage.goToLocationDetailsPage(regularLocation);
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            selectedOption = locationsPage.getLocationGroupSettingsSelectedOption();
            SimpleUtils.assertOnFail(selectedOption + " should be able to change other options with toggle EnableChangeLocationGroupSetting on",
                    locationsPage.verifyLocationGroupSettingEnabled(selectedOption), false);
            locationsPage.changeLocationGroupSettings(selectedOption, locationGroupSettings.get(1), p2pLocation);
            locationsPage.clickOnSaveButton();

            //Turn off toggle EnableChangeLocationGroupSetting
//            ToggleAPI.updateToggle(Toggles.EnableChangeLocationGroupSetting.getValue(), "jane.meng+008@legion.co", "P@ssword123",false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify Mock location can be updated")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMockLocationCanBeUpdatedAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
            String currentTime = sdf.format(new Date()).trim();
            String locationName = "AutoTest" + currentTime;
            String mockName = locationName + "-MOCK";
            int index = 0;
            String searchCharactor = "No touch";
            String configurationType = "Default";

            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            //Add a Mock
            locationsPage.addNewRegularLocationWithAllFields(locationName, searchCharactor, index);
            locationsPage.addNewMockLocationWithAllFields(locationName, index);
            //Update above location
            String status = locationsPage.searchLocationAndGetStatus(mockName);
            System.out.println("status: " + status);
            locationsPage.goToLocationDetailsPage(mockName);
            if (!status.equalsIgnoreCase("enabled")){
                locationsPage.enableLocation(mockName);
            }
            locationsPage.editLocationBtnIsClickableInLocationDetails();
            locationsPage.updateMockLocation(mockName, configurationType);
        }catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Ability to indicate Location Group type via Location Integration")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyIndicateLocationGroupTypeViaLocationIntegrationAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            String filePath = "src/test/resources/uploadFile/LocationTest/locationGroup.csv";
            locationsPage.importLocations(filePath, getSession(), "true", 200,"summary.failed",0);
            List column = new ArrayList<>();
            column.add("LocationGroupType");
            locationsPage.verifyColumnsInLocationSampleFile( getSession(),column);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Yang")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify import function by API")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyImportFunctionByAPIAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            String expectedResult = "Location Type cannot be empty,";
            String filePath = "src/test/resources/uploadFile/LocationTest/emptyLocationType.csv";
            locationsPage.importLocations(filePath, getSession(), "false", 200, "validationResults[2]", expectedResult);
//            String filePath1 = "src/test/resources/uploadFile/LocationTest/emptyConfigType.csv";
//            String expectedResult1 = "DisplayName cannot be empty,Name cannot be empty,LocationId cannot be empty,ConfigType cannot be empty,Address line 1 cannot be empty,City cannot be empty";
//            locationsPage.importLocations(filePath1, getSession(), "false", 200,"validationResults[2]",expectedResult1);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Jane")
    @Enterprise(name = "opauto")
    @TestName(description = "Verify could filter out different status location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifyCouldFilterOutDifferentStatusLocationAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            List<String> filterNames = new ArrayList<>(Arrays.asList("ENABLED", "DISABLED", "CREATED"));
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

            //go to locations tab
            locationsPage.clickOnLocationsTab();
            //go to sub-locations tab, check readyForForecast for existing location
            locationsPage.goToSubLocationsInLocationsPage();
            //Verify could filter out enabled locations
            locationsPage.selectFilter(filterNames.subList(0, 1));
            locationsPage.verifyLocationStatusInFilterResult(filterNames.subList(0, 1));
            locationsPage.clearFilterByUnSelect();

            //Verify could filter out disabled locations
            locationsPage.selectFilter(filterNames.subList(1, 2));
            locationsPage.verifyLocationStatusInFilterResult(filterNames.subList(1, 2));
            locationsPage.clearFilterByUnSelect();

            //Verify could filter out created locations
            locationsPage.selectFilter(filterNames.subList(2, 3));
            locationsPage.verifyLocationStatusInFilterResult(filterNames.subList(2, 3));
            locationsPage.clearFilterByUnSelect();

            //Verify could filter out enabled and disabled locations
            locationsPage.selectFilter(filterNames.subList(0, 2));
            locationsPage.verifyLocationStatusInFilterResult(filterNames.subList(0, 2));

            //Clear filter
            locationsPage.clickClearFilter();
            locationsPage.verifyLocationStatusInFilterResult(filterNames);

            //Could re-filter after clear filter
            locationsPage.selectFilter(filterNames.subList(2, 3));
            locationsPage.verifyLocationStatusInFilterResult(filterNames.subList(2, 3));
            locationsPage.clearFilterByUnSelect();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
