package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.core.ConsoleLocationSelectorPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.apache.regexp.RE;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.legion.utils.MyThreadLocal.*;

public class LocationNavigationTest extends TestBase {



    //The location that has same BU and  different region with the default location
    private static String location2 = "5751200 - Ticket POS";
    private static String Location = "Location";
    private static String District = "District";
    private static String Region = "Region";
    private static String BusinessUnit = "Business Unit";
    private static String hQ = "HQ";

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
        try {
            this.createDriver((String) params[0], "83", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify changing location on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingLocationOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            String anotherLocation = locationSelectorPage.changeAnotherLocation();

            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            locationSelectorPage.changeLocationDirect(location);

            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            locationSelectorPage.changeLocationDirect(anotherLocation);
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify searching and selecting the location on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySearchingAndSelectingTheLocationOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields1 = locationSelectorPage.getSelectedUpperFields();

            //Get the upperfield info of another location
            locationSelectorPage.changeAnotherDistrict();
            locationSelectorPage.changeAnotherLocation();
            Map<String, String> upperFields2 = locationSelectorPage.getSelectedUpperFields();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            searchAndCheckTheUpperFields(upperFields2);

            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);

            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            searchAndCheckTheUpperFields(upperFields1);
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);

            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            searchAndCheckTheUpperFields(upperFields2);
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void searchAndCheckTheUpperFields (Map<String, String> upperFields) throws Exception {
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(upperFields.get(Location));
        Thread.sleep(2000);
        Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
        SimpleUtils.assertOnFail("The selected upperfields is incorrect",
                selectedUpperFields.get(Location).equalsIgnoreCase(upperFields.get(Location))
                        && selectedUpperFields.get(District).equalsIgnoreCase(upperFields.get(District))
                        && selectedUpperFields.get(Region).equalsIgnoreCase(upperFields.get(Region)), false);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify changing district on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingDistrictOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields1 = locationSelectorPage.getSelectedUpperFields();

            //Get the upperfield info of another location
            locationSelectorPage.changeAnotherRegionInRegionView();
            locationSelectorPage.changeAnotherDistrict();
            locationSelectorPage.changeAnotherLocation();
            Map<String, String> upperFields2 = locationSelectorPage.getSelectedUpperFields();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //Go to Schedule tab -> Overview page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            //Click on change district button to change the district
            String regionName = upperFields2.get(Region);
            String districtName = upperFields2.get(District);
            String locationName = upperFields2.get(Location);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page DM page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the locations listed
            SimpleUtils.assertOnFail("The location: " + locationName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(locationName), false);


            //Go to Schedule tab -> Schedule page
            locationSelectorPage.changeLocationDirect(locationName);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Click on change district button to change the district
            regionName = upperFields1.get(Region);
            districtName = upperFields1.get(District);
            locationName = upperFields1.get(Location);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page DM page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the locations listed
            SimpleUtils.assertOnFail("The location: " + locationName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(locationName), false);

            //Go to Schedule tab -> Forecast page
            locationSelectorPage.changeLocationDirect(locationName);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            //Click on change district button to change the district
            regionName = upperFields2.get(Region);
            districtName = upperFields2.get(District);
            locationName = upperFields2.get(Location);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page DM page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the locations listed
            SimpleUtils.assertOnFail("The location: " + locationName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(locationName), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify searching and selecting the district on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySearchingAndSelectingTheDistrictOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields1 = locationSelectorPage.getSelectedUpperFields();

            //Get the upperfield info of another location
            locationSelectorPage.changeAnotherRegionInRegionView();
            locationSelectorPage.changeAnotherDistrict();
            locationSelectorPage.changeAnotherLocation();
            Map<String, String> upperFields2 = locationSelectorPage.getSelectedUpperFields();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Go to Schedule tab -> Overview page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);

            //Click on Search button to search the district and select
            searchDistrictAndCheckTheUpperFields(upperFields2);

            //Verify the locations listed
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("The location: " + upperFields2.get(Location)+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(upperFields2.get(Location)), false);

            //Go to Schedule tab -> Forecast page
            locationSelectorPage.changeLocationDirect(upperFields2.get(Location));
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            //Click on Search button to search the district and select
            searchDistrictAndCheckTheUpperFields(upperFields1);
            SimpleUtils.assertOnFail("The location: " + upperFields1.get(Location)+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(upperFields1.get(Location)), false);

            //Go to Schedule tab -> Schedule page
            locationSelectorPage.changeLocationDirect(upperFields1.get(Location));
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Click on Search button to search the district and select
            searchDistrictAndCheckTheUpperFields(upperFields2);
            SimpleUtils.assertOnFail("The location: " + upperFields2.get(Location)+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(upperFields2.get(Location)), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void searchDistrictAndCheckTheUpperFields (Map<String, String> upperFields) throws Exception {
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(upperFields.get(District));
        Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();

        //Verify the upperfiled should be correct
        //Verify district is selected in the navigation bar
        SimpleUtils.assertOnFail("The selected upperfields is incorrect",
                selectedUpperFields.get(District).equalsIgnoreCase(upperFields.get(District))
                        && selectedUpperFields.get(Region).equalsIgnoreCase(upperFields.get(Region)), false);
        //Verify the DM page loaded

        SimpleUtils.assertOnFail("Schedule page DM page not loaded Successfully!",
                scheduleDMViewPage.isScheduleDMView(), false);
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify changing Region on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingRegionOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            //Get the upperfield info of current region
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);

            //Get the upperfield info of another region
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName2 = location2;
            String regionName2 = upperFields.get(Region);
            String districtName2 = upperFields.get(District);

            //Go back the the default location
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //Go to Schedule tab -> Overview page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);

            //Click on change region button to change the region
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page Region view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the district listed
            SimpleUtils.assertOnFail("The district: " + districtName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName), false);



            //Go to Schedule tab -> Schedule page
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            locationSelectorPage.changeLocationDirect(locationName);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Click on change region button to change region to another one
            locationSelectorPage.changeUpperFieldDirect(Region, regionName2);

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page Region view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the district listed
            SimpleUtils.assertOnFail("The district: " + districtName2+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName2), false);

            //Go to Schedule tab -> Forecast page
            locationSelectorPage.changeUpperFieldDirect(District, districtName2);
            locationSelectorPage.changeLocationDirect(locationName2);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());

            //Click on change region button to change back to the default region
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page Region view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the locations listed
            SimpleUtils.assertOnFail("The district: " + districtName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify searching and selecting the region on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySearchingAndSelectingTheRegionOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields1 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields1.put(BusinessUnit, upperFields.get(BusinessUnit));

            //Get the upperfield info of another location
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields2 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields2.put(BusinessUnit, upperFields.get(BusinessUnit));

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Go to Schedule tab -> Overview page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            //Click on change region button to change the region
            searchRegionAndCheckTheUpperFields(upperFields2, location2);

            //Go to Schedule tab -> Forecast page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            searchRegionAndCheckTheUpperFields(upperFields1, location);

            //Go to Schedule tab -> Schedule page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            searchRegionAndCheckTheUpperFields(upperFields2, location2);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void searchRegionAndCheckTheUpperFields (Map<String, String> upperFields, String locationName) throws Exception {
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(upperFields.get(Region));
        Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
        String bUName = upperFields.get(BusinessUnit);
        String regionName = upperFields.get(Region);
        String districtName = upperFields.get(District);
        //Verify the upperfield should be correct
        //Verify district is selected in the navigation bar
        SimpleUtils.assertOnFail("The selected upperfields is incorrect",
                selectedUpperFields.get(Region).equalsIgnoreCase(regionName)
                        && selectedUpperFields.get(BusinessUnit).equalsIgnoreCase(bUName), false);

        //Verify the Region page loaded

        SimpleUtils.assertOnFail("Schedule page Region view not loaded Successfully!",
                scheduleDMViewPage.isScheduleDMView(), false);

        //Verify the district listed
        SimpleUtils.assertOnFail("The district: " + districtName+ " should be listed in the table! ",
                scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName), false);

        locationSelectorPage.changeUpperFieldDirect(District, districtName);
        locationSelectorPage.changeLocationDirect(locationName);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify changing business unit on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingBusinessUnitOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields1 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields1.put(BusinessUnit, upperFields.get(BusinessUnit));

            //Get the upperfield info of another location
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields2 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields2.put(BusinessUnit, upperFields.get(BusinessUnit));

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Go to Schedule tab -> Overview page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            //Click on change region button to change the region
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields2.get(Region));
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, upperFields2.get(BusinessUnit));

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page BU view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the region listed
            SimpleUtils.assertOnFail("The region: " + upperFields2.get(Region)+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(upperFields2.get(Region)), false);

            //Go to Schedule tab -> Schedule page
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields2.get(Region));
            locationSelectorPage.changeUpperFieldDirect(District, upperFields2.get(District));
            locationSelectorPage.changeLocationDirect(location2);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Click on change BU button to change the region
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields1.get(Region));
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, upperFields1.get(BusinessUnit));

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page Region view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the Region listed
            SimpleUtils.assertOnFail("The region: " + upperFields1.get(Region)+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(upperFields1.get(Region)), false);

            //Go to Schedule tab -> Forecast page
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields1.get(Region));
            locationSelectorPage.changeUpperFieldDirect(District, upperFields1.get(District));
            locationSelectorPage.changeLocationDirect(location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            //Click on change region button to change the region
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields2.get(Region));
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, upperFields2.get(BusinessUnit));

            //Verify the page loaded
            SimpleUtils.assertOnFail("Schedule page Region view page not loaded Successfully!",
                    scheduleDMViewPage.isScheduleDMView(), false);
            //Verify the region listed
            SimpleUtils.assertOnFail("The region: " + upperFields2.get(Region)+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(upperFields2.get(Region)), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify searching and selecting the business unit on SM schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySearchingAndSelectingTheBusinessUnitOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields1 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields1.put(BusinessUnit, upperFields.get(BusinessUnit));

            //Get the upperfield info of another location
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields2 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields2.put(BusinessUnit, upperFields.get(BusinessUnit));

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Go to Schedule tab -> Overview page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
            //Click on change BU button to change the BU
            searchBusinessUnitAndCheckTheUpperFields(upperFields2, location2);

            //Go to Schedule tab -> Forecast page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            searchBusinessUnitAndCheckTheUpperFields(upperFields1, location);

            //Go to Schedule tab -> Schedule page
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            searchBusinessUnitAndCheckTheUpperFields(upperFields2, location2);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void searchBusinessUnitAndCheckTheUpperFields (Map<String, String> upperFields, String locationName) throws Exception {
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(upperFields.get(BusinessUnit));
        Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
        String bUName = upperFields.get(BusinessUnit);
        String regionName = upperFields.get(Region);
        String districtName = upperFields.get(District);

        //Verify the upperfield should be correct
        SimpleUtils.assertOnFail("The selected upperfields is incorrect",
                selectedUpperFields.get(BusinessUnit).equalsIgnoreCase(bUName), false);

        //Verify the BU page loaded
        SimpleUtils.assertOnFail("Schedule page BU view not loaded Successfully!",
                scheduleDMViewPage.isScheduleDMView(), false);

        //Verify the region listed
        SimpleUtils.assertOnFail("The region: " + regionName+ " should be listed in the table! ",
                scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(regionName), false);
        locationSelectorPage.changeUpperFieldDirect(Region, regionName);
        locationSelectorPage.changeUpperFieldDirect(District, districtName);
        locationSelectorPage.changeLocationDirect(locationName);
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting HQ on business unit schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySelectingHQOnBusinessUnitScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields1 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields1.put(BusinessUnit, upperFields.get(BusinessUnit));

            //Get the upperfield info of another location
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields2 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields2.put(BusinessUnit, upperFields.get(BusinessUnit));

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Click on change HQ button to change the HQ
            String locationName = location2;
            String buName = upperFields2.get(BusinessUnit);
            String regionName = upperFields2.get(Region);
            String districtName = upperFields2.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            locationSelectorPage.changeUpperFieldDirect(hQ, hQ);

            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

            //Go to Schedule tab
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            locationSelectorPage.changeLocationDirect(locationName);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Click on change BU button to change the BU
            buName = upperFields1.get(BusinessUnit);
            locationName = location;
            regionName = upperFields1.get(Region);
            districtName = upperFields1.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            locationSelectorPage.changeUpperFieldDirect(hQ, hQ);

            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

            //Go to Schedule tab
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            locationSelectorPage.changeLocationDirect(locationName);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //Click on change Bu button to change the BU
            buName = upperFields2.get(BusinessUnit);
            regionName = upperFields2.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            locationSelectorPage.changeUpperFieldDirect(hQ, hQ);

            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify searching HQ on different level of schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySearchingAndSelectingTheHQOnSMScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Get the upperfield info of current location
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields1 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields1.put(BusinessUnit, upperFields.get(BusinessUnit));

            //Get the upperfield info of another location
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location2);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            Map<String, String> upperFields2 = upperFields;
            locationSelectorPage.changeUpperFieldDirect(Region, upperFields.get(Region));
            upperFields = locationSelectorPage.getSelectedUpperFields();
            upperFields2.put(BusinessUnit, upperFields.get(BusinessUnit));

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //Click on change region button to change the region
            searchHQAndCheckTheUpperFields(upperFields2, location2);

            //Go to Schedule tab -> Forecast page
            searchHQAndCheckTheUpperFields(upperFields1, location);

            //Go to Schedule tab -> Schedule page
            searchHQAndCheckTheUpperFields(upperFields2, location2);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void searchHQAndCheckTheUpperFields (Map<String, String> upperFields, String locationName) throws Exception {
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(hQ);
        Thread.sleep(3000);
        Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
        String bUName = upperFields.get(BusinessUnit);
        String regionName = upperFields.get(Region);
        String districtName = upperFields.get(District);
        //Verify the upperfield should be correct
        SimpleUtils.assertOnFail("The selected upperfields is incorrect",
                selectedUpperFields.get(hQ).equalsIgnoreCase(hQ), false);

        //Verify the No data page loaded
        SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                locationSelectorPage.isNoDataToShowPageLoaded(), false);

        //Verify the district listed
        locationSelectorPage.changeUpperFieldDirect(BusinessUnit, bUName);
        locationSelectorPage.changeUpperFieldDirect(Region, regionName);
        locationSelectorPage.changeUpperFieldDirect(District, districtName);
        locationSelectorPage.changeLocationDirect(locationName);
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting different level of upperfields and locations on HQ schedule tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySelectingDifferentLevelOfUpperFieldsAndLocationsOnHQScheduleTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String,String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);

            //Click on change HQ button to change the HQ
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(hQ);
            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

            //Go to Schedule tab
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("The region: " + regionName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(regionName), false);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("The district: " + districtName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(districtName), false);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            SimpleUtils.assertOnFail("The location: " + locationName+ " should be listed in the table! ",
                    scheduleDMViewPage.getLocationsInScheduleDMViewLocationsTable().contains(locationName), false);

            locationSelectorPage.changeLocationDirect(locationName);
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);


        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting different level of upperfields and locations on HQ compliance tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySelectingDifferentLevelOfUpperFieldsAndLocationsOnHQComplianceTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            dashboardPage.isConsoleNavigationBarIsGray("Compliance");
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String,String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(hQ);

            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(regionName);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(districtName);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(locationName);
            locationSelectorPage.changeLocationDirect(locationName);
            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting different level of upperfields on SM compliance tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySelectingDifferentLevelOfUpperFieldsOnSMComplianceTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            compliancePage.clickOnComplianceConsoleMenu();

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);
            Map<String,String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(locationName);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(districtName);

            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            compliancePage.getAllUpperFieldInfoFromComplianceDMViewByUpperField(regionName);

            locationSelectorPage.changeUpperFieldDirect(hQ, hQ);
            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting different level of upperfields and locations on HQ Report tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySelectingDifferentLevelOfUpperFieldsAndLocationsOnHQReportTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String,String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            dashboardPage.isConsoleNavigationBarIsGray("Report");
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            //Check Report page is display after click Report tab
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!", analyticsPage.isReportsPageLoaded(), false);


            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(hQ);

            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

            String reportMenuTab = "Report";
            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(buName), false);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(regionName), false);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(districtName), false);

            locationSelectorPage.changeLocationDirect(locationName);
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(locationName), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting different level of upperfields on location Report tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySelectingDifferentLevelOfUpperFieldsOnLocationReportTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String,String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            String reportMenuTab = "Report";

            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(locationName), false);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(districtName), false);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(regionName), false);

            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!",
                    analyticsPage.isReportsPageLoaded(), false);
            SimpleUtils.assertOnFail("Report is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(reportMenuTab), false);
            SimpleUtils.assertOnFail("Report tab is not selected correctly !",
                    analyticsPage.isSpecificReportsTabBeenSelected(buName), false);

            locationSelectorPage.changeUpperFieldDirect(hQ, hQ);
            //Verify the No data page loaded
            SimpleUtils.assertOnFail("The No data to show page fail to load! ",
                    locationSelectorPage.isNoDataToShowPageLoaded(), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting different level of upperfields and locations on HQ News tab - Newsfeed page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySelectingDifferentLevelOfUpperFieldsAndLocationsOnHQNewsTabAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            NewsPage newsPage = pageFactory.createNewsPage();
            newsPage.clickOnNewsConsoleMenu();
            newsPage.enableViewing();
            List<String> postInfo = newsPage.createPost();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            loginAsDifferentRole("InternalAdmin");

            String newsMenuTab = "News";
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            Thread.sleep(3000);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);

            //Check Report page is display after click Report tab
            newsPage.clickOnNewsConsoleMenu();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(hQ);
            newsPage.enableViewing();
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);


            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

            locationSelectorPage.changeLocationDirect(locationName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);
            newsPage.deletePost(postInfo.get(0));

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify selecting different level of upperfields on location Newsfeed page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySelectingDifferentLevelOfUpperFieldsOnLocationsNewsfeedPageAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            NewsPage newsPage = pageFactory.createNewsPage();
            newsPage.clickOnNewsConsoleMenu();
            List<String> postInfo = newsPage.createPost();

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole("InternalAdmin");
            String newsMenuTab = "News";
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            Thread.sleep(3000);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);

            newsPage.clickOnNewsConsoleMenu();
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

            locationSelectorPage.changeUpperFieldDirect(hQ, hQ);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            SimpleUtils.assertOnFail("The location level post fail to load! ",
                    !newsPage.checkIfPostExists(postInfo.get(0)), false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationName);
            newsPage.deletePost(postInfo.get(0));

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify location navigation should not show on Moderation page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyLocationNavigationShouldNotShowOnModerationPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            NewsPage newsPage = pageFactory.createNewsPage();
            newsPage.clickOnNewsConsoleMenu();

            String newsMenuTab = "News";
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            Map<String, String> upperFields = locationSelectorPage.getSelectedUpperFields();
            String locationName = location;
            String regionName = upperFields.get(Region);
            String districtName = upperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            Thread.sleep(3000);
            upperFields = locationSelectorPage.getSelectedUpperFields();
            String buName = upperFields.get(BusinessUnit);

            newsPage.clickOnNewsConsoleMenu();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(hQ);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            newsPage.clickModerationTab();
            SimpleUtils.assertOnFail("The upperfield navigation should not display! ",
                    !locationSelectorPage.isUpperFieldNavigationLoaded() , false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);
            newsPage.clickNewsfeedTab();

            locationSelectorPage.changeUpperFieldDirect(BusinessUnit, buName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            newsPage.clickModerationTab();
            SimpleUtils.assertOnFail("The upperfield navigation should not display! ",
                    !locationSelectorPage.isUpperFieldNavigationLoaded() , false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);
            newsPage.clickNewsfeedTab();

            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            newsPage.clickModerationTab();
            SimpleUtils.assertOnFail("The upperfield navigation should not display! ",
                    !locationSelectorPage.isUpperFieldNavigationLoaded() , false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);
            newsPage.clickNewsfeedTab();

            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            newsPage.clickModerationTab();
            SimpleUtils.assertOnFail("The upperfield navigation should not display! ",
                    !locationSelectorPage.isUpperFieldNavigationLoaded() , false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);
            newsPage.clickNewsfeedTab();

            locationSelectorPage.changeLocationDirect(locationName);
            SimpleUtils.assertOnFail("The news page fail to load! ",
                    newsPage.checkIfNewsPageLoaded(), false);
            newsPage.clickModerationTab();
            SimpleUtils.assertOnFail("The upperfield navigation should not display! ",
                    !locationSelectorPage.isUpperFieldNavigationLoaded() , false);
            SimpleUtils.assertOnFail("News menu tab is not selected Successfully!",
                    dashboardPage.isConsoleNavigationBarBeenSelected(newsMenuTab), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify changing location on TM view schedule page when login as SM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangeLocationOnSchedulePageWhenLoginAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            if (dashboardPage.isSwitchToEmployeeViewPresent()) {
                dashboardPage.clickOnSwitchToEmployeeView();
            }
            //Verify changing location on My Schedule page
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
            locationSelectorPage.changeAnotherLocation();
            //Location should be changed successfully, My Schedule page is loaded
            mySchedulePage.validateTheAvailabilityOfScheduleMenu();
            mySchedulePage.validateTheFocusOfSchedule();
            //Verify changing location on Team Schedule page
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.TeamSchedule.getValue());
            locationSelectorPage.changeAnotherLocation();
            //Location should be changed successfully, My Schedule page is loaded
            mySchedulePage.validateTheAvailabilityOfScheduleMenu();
            mySchedulePage.validateTheFocusOfSchedule();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Verify changing location on TM view schedule page when login as TM")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangeLocationOnSchedulePageWhenLoginAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Verify changing location on My Schedule page
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.MySchedule.getValue());
            locationSelectorPage.changeAnotherLocation();
            //Location should be changed successfully, My Schedule page is loaded
            mySchedulePage.validateTheAvailabilityOfScheduleMenu();
            mySchedulePage.validateTheFocusOfSchedule();
            //Verify changing location on Team Schedule page
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.TeamSchedule.getValue());
            locationSelectorPage.changeAnotherLocation();
            //Location should be changed successfully, My Schedule page is loaded
            mySchedulePage.validateTheAvailabilityOfScheduleMenu();
            mySchedulePage.validateTheFocusOfSchedule();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
