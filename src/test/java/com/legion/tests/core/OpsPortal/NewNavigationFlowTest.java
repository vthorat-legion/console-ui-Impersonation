package com.legion.tests.core.OpsPortal;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpCommons.ConsoleNavigationPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.apache.commons.collections.ListUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

public class NewNavigationFlowTest extends TestBase {

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
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate manager location for one user in controls")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyManagerLocationForOneUserInControlsInControlsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {


        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);

        //search one user and to see edit
        controlsNewUIPage.clickOnControlsUsersAndRolesSection();
        String userFirstName = "a";

        //Validate manager location for one user
        controlsNewUIPage.verifyUpdateUserAndRolesOneUserLocationInfo(userFirstName);
    }
    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate navigation bar for SM user")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDashboardViewAsStoreManager(String browser, String username, String password, String location) throws Exception {

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        SimpleUtils.assertOnFail("Navigation Bar - Location field not loaded successfuly!", locationSelectorPage.isChangeLocationButtonLoaded(), false);
        locationSelectorPage.isSMView();
    }


    //added by Estelle for magnifying glass icon
    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "New console global navigation location picker")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyGlobalSearchFunctionOnNavigatorAsInternalCustomerAdmin(String browser, String username, String password, String location) throws Exception {

        String[]  upperFieldList = {"HQ","SeaTac AirportSEA","District-ForAutomation","ENH_NSO_AutoTestLocation","BU3"};
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        if (locationSelectorPage.verifyMagnifyGlassIconShowOrNot()) {
            //navigate to different upperField via magnifying glass icon
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(upperFieldList[4]);
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(upperFieldList[3]);
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(upperFieldList[2]);
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(upperFieldList[1]);
            locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(upperFieldList[0]);
            //verify recently viewed (5 items)
            List<String> upperFieldNameInResentView = new ArrayList<>();
            List<String> recentViewText = locationSelectorPage.getRecentlyViewedInfo();

            for (String ss: recentViewText) {
                 upperFieldNameInResentView.add(ss.split("\n")[0]);
            }
            String[] upperFieldNameInResentView1 = upperFieldNameInResentView.toArray(new String[]{});
            if (Arrays.equals(upperFieldNameInResentView1,upperFieldList)) {
                SimpleUtils.pass("Resent view list show well");
            }else
                SimpleUtils.fail("Resent view list is not the latest one",false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Make HQ the top node of the hierarchy")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEachTabIfSelectHQAsInternalCustomerAdmin(String browser, String username, String password, String location) throws Exception {


        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("HQ");
        if (locationSelectorPage.verifyHQViewShowOrNot()) {
            locationSelectorPage.verifyGreyOutPageInHQView();
            List<String> tabsName = locationSelectorPage.getConsoleTabs();
            //check dashboard is empty or not
            if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                SimpleUtils.pass("Dashboard tab show empty page successfully");
            }else
                SimpleUtils.fail("Dashboard tab show empty page failed",false);

            //go to "Team" tab
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                SimpleUtils.pass("Team tab is grey out and show empty page successfully");
            }else
                SimpleUtils.fail("Team tab is not grey out and show empty page failed",false);


            // Go to schedule page, schedule tab
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                SimpleUtils.pass("Schedule tab is grey out and show empty page successfully");
            }else
                SimpleUtils.fail("Schedule tab is not grey out and show empty page failed",false);


            //Go to "Timesheet" option menu.
            if (tabsName.contains("TimeSheet")) {
                TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
                timeSheetPage.clickOnTimeSheetConsoleMenu();
                if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                    SimpleUtils.pass("TimeSheet tab is grey out and show empty page successfully");
                }else
                    SimpleUtils.fail("TimeSheet tab is not grey out and show empty page failed",false);
            }else
                SimpleUtils.report("TimeSheet tab is disabled");


            //Go to "Compliance"  tab
            if (tabsName.contains("Compliance")) {
                CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
                compliancePage.clickOnComplianceConsoleMenu();
                if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                    SimpleUtils.pass("Compliance tab is grey out and show empty page successfully");
                }else
                    SimpleUtils.fail("Compliance tab is not grey out and show empty page failed",false);
            }else
                SimpleUtils.report("Compliance tab is disabled");



//            Go to "Report"  tab
            ReportPage reportPage = pageFactory.createConsoleReportPage();
            reportPage.clickOnConsoleReportMenu();
            if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                SimpleUtils.pass("Report tab is grey out and show empty page successfully");
            }else
                SimpleUtils.fail("Report tab is not grey out and show empty page failed",false);


            //Go to "Insight" tab
            if (tabsName.contains("Insights")) {
                InsightPage insightPage = pageFactory.createConsoleInsightPage();
                insightPage.clickOnConsoleInsightPage();
                if (insightPage.isInsightsPageDisplay()) {
                    SimpleUtils.pass("Insight page load well ");
                }else
                    SimpleUtils.fail("Insight page load failed",false);

            }else
                SimpleUtils.report("Insight tab is disabled");

            //Go to "Inbox" tab
            if (tabsName.contains("Inbox")) {
                InboxPage inboxPage = pageFactory.createConsoleInboxPage();
                inboxPage.clickOnInboxConsoleMenuItem();
                if (inboxPage.isAnnouncementListPanelDisplay()) {
                    SimpleUtils.pass("Inbox page show well");
                }else
                    SimpleUtils.fail("Inbox page load failed",false);

            }else
                SimpleUtils.report("Inbox tab is disabled");


            //Go to "News" tab
             if (tabsName.contains("News")) {
                 NewsPage newsPage = pageFactory.createConsoleNewsPage();
                 newsPage.clickOnConsoleNewsMenu();
                 if (newsPage.isNewsTabLoadWell()) {
                     SimpleUtils.pass("News tab is grey out and show empty page successfully");
                 }else
                     SimpleUtils.fail("News tab is not grey out and show empty page failed",false);

             }else
                 SimpleUtils.report("News tab is disabled");

            //Go to "Admin" tab
            if (tabsName.contains("Admin")) {
                AdminPage adminPage = pageFactory.createConsoleAdminPage();
                adminPage.clickOnConsoleAdminMenu();
                if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                    SimpleUtils.pass("Admin tab is grey out and show empty page successfully");
                }else
                    SimpleUtils.fail("Admin tab is not grey out and show empty page failed",false);

            }else
                SimpleUtils.report("Admin tab is disabled");

            //Go to "Integration" tab
            if (tabsName.contains("Integration")) {
                IntegrationPage integrationPage = pageFactory.createConsoleIntegrationPage();
                integrationPage.clickOnConsoleIntegrationPage();
                if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                    SimpleUtils.pass("Integration tab is grey out and show empty page successfully");
                }else
                    SimpleUtils.fail("Integration tab is not grey out and show empty page failed",false);

            }else
                SimpleUtils.report("Integration tab is disabled");

            //Go to "Controls" tab
            if (tabsName.contains("Controls")) {
                ControlsPage controlsPage  = pageFactory.createConsoleControlsPage();
                controlsPage.clickOnConsoleInsightPage();
                if (locationSelectorPage.isCurrentPageEmptyInHQView()) {
                    SimpleUtils.pass("Integration tab is grey out and show empty page successfully");
                }else
                    SimpleUtils.fail("Integration tab is not grey out and show empty page failed",false);

            }else
                SimpleUtils.report("Integration tab is disabled");

        }else
                SimpleUtils.fail("It's not HQ view",false);


    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Location navigation via different role")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyNavigationFunctionByDifRoleAsInternalCustomerAdmin(String browser, String username, String password, String location) throws Exception {

        String firstLevel = "HQ";
        String secondLevel = "All Business Units";
        String bu = "BU-ForAutomation";
        String region = "Region-ForAutomation";
        String district ="District-ForAutomation";
        String locationL ="OMLocation16";
//
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon(firstLevel);
        List<String> navigateDefaultText = locationSelectorPage.getNavigatorValue();
        if (navigateDefaultText.get(0).equalsIgnoreCase(firstLevel)&& navigateDefaultText.get(1).equalsIgnoreCase(secondLevel)) {
            SimpleUtils.pass("Default navigate show well");
        }
          // Validate navigate down one or more level for internal admin
         locationSelectorPage.changeUpperFields(bu);
         locationSelectorPage.changeUpperFields(region);
         locationSelectorPage.changeUpperFields(district);
         locationSelectorPage.changeUpperFields(locationL);
         //Validate User current location selection should persist upon browser refresh
         List<String> navigateText = locationSelectorPage.getNavigatorValue();
         locationSelectorPage.refreshTheBrowser();
         List<String> navigateTextAftRefresh = locationSelectorPage.getNavigatorValue();
         String[] navigateTextToStr = navigateText.toArray(new String[]{});
         String[] navigateTextAftRefreshToStr = navigateTextAftRefresh.toArray(new String[]{});
         if (Arrays.equals(navigateTextToStr,navigateTextAftRefreshToStr)) {
             SimpleUtils.pass("current location selection persist upon browser refresh");
         }else
             SimpleUtils.fail("After refresh ,the navigator value doesn't persist upon browser refresh",false);

         //Validate navigate up one or more level for internal admin
         locationSelectorPage.changeUpperFields(district);
         locationSelectorPage.changeUpperFields(region);
         locationSelectorPage.changeUpperFields(region);
         locationSelectorPage.changeUpperFields(bu);
         locationSelectorPage.changeUpperFields(firstLevel);
         List<String> navigateTextAftNavigateUp = locationSelectorPage.getNavigatorValue();
         String[] navigateTextAftNavigateUpToStr = navigateTextAftNavigateUp.toArray(new String[]{});
         if (Arrays.equals(navigateTextToStr,navigateTextAftNavigateUpToStr)) {
             SimpleUtils.pass("Internal admin can navigate up one or more level successfully");
         }else
             SimpleUtils.fail("Navigate up one or more level failed",false);
         //logout
         LoginPage loginPage = pageFactory.createConsoleLoginPage();
         loginPage.logOut();

         //verify navigation function by DM
        String fileName="UsersCredentials.json";
        fileName= MyThreadLocal.getEnterprise()+fileName;
        HashMap<String,Object[][]>userCredentials=SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
        Object[][]teamMemberCredentials=userCredentials.get("DistrictManager");
        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield(String.valueOf(teamMemberCredentials[0][0]),String.valueOf(teamMemberCredentials[0][1])
                ,String.valueOf(teamMemberCredentials[0][2]));

        List<String> navigateDefaultTextForDM = locationSelectorPage.getNavigatorValue();
        if (navigateDefaultTextForDM.get(0).equalsIgnoreCase(district)&& navigateDefaultTextForDM.get(1).equalsIgnoreCase("All Locations")) {
            SimpleUtils.pass("Default navigate show well for DM");
        }
        // Validate navigate down one or more level for internal admin
        locationSelectorPage.changeLocation(locationL);
        List<String> navigateTextAftNavDownForDM = locationSelectorPage.getNavigatorValue();
        locationSelectorPage.refreshTheBrowser();
        List<String> navigateTextAftRefreshForDM = locationSelectorPage.getNavigatorValue();
        String[] navigateTextAftNavDownForDMToStr = navigateTextAftNavDownForDM.toArray(new String[]{});
        String[] navigateTextAftRefreshForDMToStr = navigateTextAftRefreshForDM.toArray(new String[]{});

        if (Arrays.equals(navigateTextAftNavDownForDMToStr,navigateTextAftRefreshForDMToStr)) {
            SimpleUtils.pass("current location selection persist upon browser refresh");
        }else
            SimpleUtils.fail("After refresh ,the navigator value doesn't persist upon browser refresh",false);
        //Navigate up
        locationSelectorPage.changeUpperFields(district);
        List<String> navigateTextAftNavUpForDM = locationSelectorPage.getNavigatorValue();
        String[] navigateTextAftNavUpForDMToStr = navigateTextAftNavUpForDM.toArray(new String[]{});
        String[] navigateDefaultTextForDMToStr = navigateDefaultTextForDM.toArray(new String[]{});
        if (Arrays.equals(navigateTextAftNavUpForDMToStr,navigateDefaultTextForDMToStr)) {
            SimpleUtils.pass("DM can navigate up one or more level successfully");
        }else
            SimpleUtils.fail("Navigate up one or more level failed",false);

        loginPage.logOut();

        //verify navigation function by SM
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());
        dashboardPage.clickOnSubMenuOnProfile("My Profile");
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        HashMap<String, String> userHRProfileInfo  = profileNewUIPage.getOneUserHRProfileInfo();
        String homeStore = userHRProfileInfo.get("home store");
        dashboardPage.clickOnDashboardConsoleMenu();
        List<String> navigateDefaultTextForSMTM = locationSelectorPage.getNavigatorValue();
        if (navigateDefaultTextForSMTM.get(0).equalsIgnoreCase(homeStore) ) {
            SimpleUtils.pass("SM/TM navigation show well");
        }else
            SimpleUtils.report("Default navigation for SM/TM is not home store");

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate location profile page in controls")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false )
    public void verifyLocationProfilePageInControlsAsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//        locationSelectorPage.changeLocation("OMLocation16");
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);

        // Validate Controls Location Profile Section
        controlsNewUIPage.clickOnControlsLocationProfileSection();
        boolean isLocationProfile = controlsNewUIPage.isControlsLocationProfileLoaded();
        SimpleUtils.assertOnFail("Controls Page: Location Profile Section not Loaded.", isLocationProfile, true);


    }



    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate location list in Timesheet page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyLocationListFunctionInTimesheetAsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {


        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        String currentDistrict = dashboardPage.getCurrentDistrict();
        //change district to show all locations
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("ClearDistrict");

        TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();

        // Click on "Timesheet" option menu.
        timeSheetPage.clickOnTimeSheetConsoleMenu();
        SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",timeSheetPage.isTimeSheetPageLoaded() , false);

        //check location list
        String locationName = timeSheetPage.verifyLocationList();
        dashboardPage.clickOnDashboardConsoleMenu();
        String updatedLocationName = dashboardPage.getCurrentLocation();
        if (updatedLocationName.equals(locationName)) {
            SimpleUtils.pass("Location switch in Timesheet successfully");

        }else
            SimpleUtils.fail("Location switch in Timesheet failed",false);

//        //change location to default one in order to avoid other test case
//        locationSelectorPage.changeDistrict(currentDistrict);

    }

//    @Automated(automated = "Automated")
//    @Owner(owner = "Estelle")
//    @Enterprise(name = "LegionBasic_Enterprise")
//    @TestName(description = "Validate legionbasic navigation function")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyLegionBasicNavigationFunctionAsLegionBasicUser(String browser, String username, String password, String location) throws Exception {
//
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//
//        String defaultLocationAfterLogin = dashboardPage.getCurrentLocation();
//
//        //get all locations from location navigation bar
//        List<String> locationsInDashboard = dashboardPage.getLocationListInDashboard();
//        //validate there is no district
//        boolean isShow = dashboardPage.IsThereDistrictNavigationForLegionBasic();
//        if (!isShow ) {
//            SimpleUtils.pass("There is no district show");
//        }else
//            SimpleUtils.fail("For Legionbasic ,it should show district",true);
//        //Validation the location list on navigation bar
//           // go to Controls->global location>get all locations ->compare with navigation bar show
//        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("Cupertino");
//
//        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//        controlsNewUIPage.clickOnControlsConsoleMenu();
//        SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//        controlsNewUIPage.clickOnGlobalLocationButton();
//        boolean isLocationsTabShow = controlsNewUIPage.isControlsLocationsCard();
//        if (isLocationsTabShow ) {
//            controlsNewUIPage.clickOnLocationsTabInGlobalModel();
//            List<String> locationList = controlsNewUIPage.getAllLocationsInGlobalModel();
//            if ((locationList .size()!= 0 && locationsInDashboard.size() !=0)&& (locationList.size() ==locationsInDashboard.size()))
//            {
//
//
//                String[] locationListSwitch = locationList.toArray(new String[]{});
//                String[] locationsInDashboardSwitch = locationsInDashboard.toArray(new String[]{});
//                Arrays.sort(locationListSwitch);
//                Arrays.sort(locationsInDashboardSwitch);
//                if (Arrays.equals(locationListSwitch, locationsInDashboardSwitch)) {
//                    SimpleUtils.pass("Legion basic show all locations successfully");
//                }
//            } else
//                SimpleUtils.fail("Legion basic location not show well",true);
//            }else
//            SimpleUtils.report("Locations Tab is not show ,maybe OPView switch is on");
//
//        //Validated navigation bar show after switch to other tabs and then return to dashboard page
//
//        SchedulePage schedulePage = pageFactory.createConsoleScheduleNewUIPage();
//        schedulePage.clickOnScheduleConsoleMenuItem();
//        controlsNewUIPage.clickOnControlsConsoleMenu();
//        TeamPage teamPage = pageFactory.createConsoleTeamPage();
//        teamPage.goToTeam();
//        dashboardPage.clickOnDashboardConsoleMenu();
//        String locationAfterSwitchToOtherPageAndBackToDashboard = dashboardPage.getCurrentLocation();
//        if (locationAfterSwitchToOtherPageAndBackToDashboard .equals(defaultLocationAfterLogin)) {
//            SimpleUtils.pass("After back to dashboard page the location info should not be changed.");
//        }else
//            SimpleUtils.fail("After back to dashboard page the location info was changed",true);
//
//    }
//    This case is not applicable for new navigator
//    @Automated(automated = "Automated")
//    @Owner(owner = "Fiona")
//    @Enterprise(name = "Op_Enterprise")
//    @TestName(description = "Validate navigation bar view for admin user")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyDashboardViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//        SimpleUtils.assertOnFail("Navigation Bar - Location field not loaded successfuly!", locationSelectorPage.isChangeLocationButtonLoaded(), false);
//        locationSelectorPage.isDMView();
//    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate navigation bar view for district manager")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyDashboardViewAsDistrictManager(String browser, String username, String password, String location) throws Exception {

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        SimpleUtils.assertOnFail("Navigation Bar - Location field not loaded successfuly!", locationSelectorPage.isChangeLocationButtonLoaded(), false);
        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("District-ForAutomation");
        locationSelectorPage.isDMView();
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate search district function in navigation bar")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNavigationBarSearchDistrictFunctionInternalAdmin(String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        SimpleUtils.assertOnFail("Navigation Bar - Location field not loaded successfuly!", locationSelectorPage.isChangeLocationButtonLoaded(), false);

//        get current Region name
        Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
        String regionName = upperFields.get("Region");

//        get all districts in this region
        List<String> districtsNames = locationSelectorPage.getAllUpperFieldNamesInUpperFieldDropdownList("District");

        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //go to upperField tab
        locationsPage.goToUpperFieldsPage();
        List<String> districtListInRegionDetailsPage = locationsPage.getLocationsInDistrict(regionName);
        Collections.sort(districtListInRegionDetailsPage);

        //compare above two data
        if (ListUtils.isEqualList(districtsNames, districtListInRegionDetailsPage)) {
            SimpleUtils.pass("The districts can show well in navigator");
        } else {
            SimpleUtils.fail("The district list is not correct!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate search location function in navigation bar")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNavigationBarSearchLocationFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        //change district to show all locations
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeDistrict("Fiona");

        String searchLocationText = "*";
        Thread.sleep(4000);
        String currentDistrict = dashboardPage.getCurrentDistrict();
        SimpleUtils.report(currentDistrict);

        List<String> locationsInNavigationBar = new ArrayList<>();
        List<String> locationsInDistrictPage = new ArrayList<>();

        //input * to search all locations in specify district
        locationsInNavigationBar = locationSelectorPage.searchLocation(searchLocationText);

        //go to OPS -> Locations -> District function
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to locations tab
        locationsPage.clickOnLocationsTab();
        //go to sub-district tab
        locationsPage.goToUpperFieldsPage();
        //get all locations in specify district in OPS-District function
        locationsInDistrictPage = locationsPage.getLocationsInDistrict(currentDistrict);
        //compare these two list
        if(locationsInNavigationBar.size() == locationsInDistrictPage.size()){
            for(String locationInNavigationBar:locationsInNavigationBar){
                for(String locationInDistrictPage:locationsInDistrictPage){
                    if(locationInNavigationBar.contains(locationInDistrictPage)){
                        SimpleUtils.pass(locationInNavigationBar + " is showing both in navigation bar and district function!");
                        break;
                    }else{
                        continue;
//                        SimpleUtils.pass(locationInNavigationBar + " is NOT showing both in navigation bar and district function!");
                    }
                }
            }
            SimpleUtils.pass("Locations in navigation bar are matched with which in dsitrict.");
        }else {
            SimpleUtils.pass("Locations in navigation bar are NOT matched with which in dsitrict.");
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validated navigation bar show after switch to other tabs and then return to dashboard page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false )
    public void verifyNavigationBarWhenSwitchDifferentTabsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        String districtName="Fiona";
        String locationName="FionaUsingLocation";
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        SimpleUtils.assertOnFail("Navigation Bar - Location field not loaded successfuly!", locationSelectorPage.isChangeLocationButtonLoaded(), false);

        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName);

        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        if(teamPage.loadTeamTab()){
            String teamPageDistrcit = dashboardPage.getCurrentDistrict();
            String teamPageLocation = dashboardPage.getCurrentLocationInDMView();
            if(teamPageDistrcit.equals(districtName) && teamPageLocation.equals(locationName)){
                SimpleUtils.pass("The navigation bar shows well on team page");
            }else{
                SimpleUtils.fail("The navigation bar shows incorrect on team page",true);
            }
        }

        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        if(scheduleOverviewPage.loadScheduleOverview()){
            String schedulePageDistrcit = dashboardPage.getCurrentDistrict();
            String schedulePageLocation = dashboardPage.getCurrentLocationInDMView();
            if(schedulePageDistrcit.equals(districtName) && schedulePageLocation.equals(locationName)){
                SimpleUtils.pass("The navigation bar shows well on schedule page");
            }else{
                SimpleUtils.fail("The navigation bar shows incorrect on schedule page",true);
            }
        }

        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
        controlsNewUIPage.clickOnControlsConsoleMenu();
        if(controlsNewUIPage.isControlsPageLoaded()){
            Thread.sleep(5000);
            String controlsPageLocation = controlsNewUIPage.getCurrentLocationInControls();
            if(controlsPageLocation.equals(locationName)){
                SimpleUtils.pass("The navigation bar shows well on controls page");
            }else{
                SimpleUtils.fail("The navigation bar shows incorrect on controls page",true);
                SimpleUtils.report("The locations filed on controls page is: " + controlsPageLocation);
            }
        }

        dashboardPage.navigateToDashboard();
        if(dashboardPage.isDashboardPageLoaded()){
            String dashboardPageDistrict = dashboardPage.getCurrentDistrict();
            String dashboardPageLocation = dashboardPage.getCurrentLocationInDMView();
            if(dashboardPageDistrict.equals(districtName) && dashboardPageLocation.equals(locationName)){
                SimpleUtils.pass("The navigation bar shows well after back to dashboard page");
            }else{
                SimpleUtils.fail("The navigation bar shows incorrect after back to controls page",false);
            }
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Fiona")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validated the recently views list should show correctly for user")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNavigationBarRecentlyViewListAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        List<String> searchDistrictsList = new ArrayList<String>(){{
            add("Fiona");
            add("NavigationTest");
            add("ClearDistrict");
            add("DistrictNavigation1");
            add("DistrictNavigation2");
        }};

        List<String> finalDistrictsList = new ArrayList<String>();

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        SimpleUtils.assertOnFail("Navigation Bar - Location field not loaded successfuly!", locationSelectorPage.isChangeLocationButtonLoaded(), false);

        for(String district:searchDistrictsList){
            if(district!=null) {
                locationSelectorPage.changeDistrict(district);
            }
        }

        finalDistrictsList = dashboardPage.getDistrcitListInDashboard();
        Collections.reverse(searchDistrictsList);

        if(finalDistrictsList.size()==5){
        for(int i=0;i<finalDistrictsList.size();i++){
            if(finalDistrictsList.get(i).equals(searchDistrictsList.get(i))){
                SimpleUtils.pass("The " + (i+1) + " location is " + finalDistrictsList.get(i));
            }else{
                SimpleUtils.fail("The order of districts list is NOT correct",false);
            }
        }
        }else {
            SimpleUtils.fail("The count of districts is NOT correct",false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Persist console tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class, enabled = false)
    public void verifyPersistConsoleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        SimpleUtils.assertOnFail("Navigation Bar - Location field not loaded successfuly!", locationSelectorPage.isChangeLocationButtonLoaded(), false);

        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("verifyMock");

        ConsoleNavigationPage consoleNavigationPage = new ConsoleNavigationPage();

        consoleNavigationPage.verifyOnlyComplianceIsGray();
        consoleNavigationPage.verifyOtherTableIsNormal();
        consoleNavigationPage.navigateTo("Team");
        consoleNavigationPage.verifyTeamPageIsNormal();
        consoleNavigationPage.navigateTo("Compliance");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Timesheet");
        consoleNavigationPage.verifytimeSheetPageIsNormal();
        consoleNavigationPage.navigateTo("Schedule");
        consoleNavigationPage.verifySchedulePageIsNormal();

        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("ClearDistrict");

        consoleNavigationPage.verifyOnlyTeamIsGray();
        consoleNavigationPage.verifyOtherTableIsNormal();
        consoleNavigationPage.navigateTo("Team");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Compliance");
        consoleNavigationPage.verifyCompliancePageIsNormal();
        consoleNavigationPage.navigateTo("Schedule");
        consoleNavigationPage.verifySchedulePageForDisIsNormal();
        consoleNavigationPage.navigateTo("Timesheet");
        consoleNavigationPage.verifytimeSheetPageForDisIsNormal();

        consoleNavigationPage.navigateTo("logout");

        loginToLegionAndVerifyIsLoginDoneWithoutUpdateUpperfield("nancy.nan+dm@legion.co", "admin11.a","");

        consoleNavigationPage.verifyFourTabAreGray();
        consoleNavigationPage.verifyOtherTableIsNormal();
        consoleNavigationPage.navigateTo("Team");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Compliance");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Schedule");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Timesheet");
        consoleNavigationPage.verifyPageEmpty();

        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("Region20210818053401");

        consoleNavigationPage.navigateTo("dashboard");
        consoleNavigationPage.verifyFourTabAreGray();
        consoleNavigationPage.verifyOtherTableIsNormal();
        consoleNavigationPage.navigateTo("Team");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Compliance");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Schedule");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Timesheet");
        consoleNavigationPage.verifyPageEmpty();

        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("ClearDistrict");

        consoleNavigationPage.verifyOnlyTeamIsGray();
        consoleNavigationPage.verifyOtherTableIsNormal();
        consoleNavigationPage.navigateTo("Team");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Compliance");
        consoleNavigationPage.verifyCompliancePageIsNormal();
        consoleNavigationPage.navigateTo("Schedule");
        consoleNavigationPage.verifySchedulePageForDisIsNormal();
        consoleNavigationPage.navigateTo("Timesheet");
        consoleNavigationPage.verifytimeSheetPageForDisIsNormal();


        locationSelectorPage.changeUpperFieldsByMagnifyGlassIcon("verifyMock");

        consoleNavigationPage.navigateTo("dashboard");
        consoleNavigationPage.verifyOnlyComplianceIsGray();
        consoleNavigationPage.verifyOtherTableIsNormal();
        consoleNavigationPage.navigateTo("Team");
        consoleNavigationPage.verifyTeamPageIsNormal();
        consoleNavigationPage.navigateTo("Compliance");
        consoleNavigationPage.verifyPageEmpty();
        consoleNavigationPage.navigateTo("Schedule");
        consoleNavigationPage.verifySchedulePageIsNormal();
        consoleNavigationPage.navigateTo("Timesheet");
        consoleNavigationPage.verifytimeSheetPageIsNormal();
    }
}
