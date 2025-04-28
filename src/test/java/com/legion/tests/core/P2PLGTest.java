package com.legion.tests.core;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.core.ConsoleCompliancePage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.pages.core.schedule.ConsoleEditShiftPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.Constants;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.it.Ma;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import static com.legion.utils.MyThreadLocal.getDriver;

public class P2PLGTest extends TestBase {
    private static HashMap<String, String> parameterMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
    private static String Location = "Location";
    private static String District = "District";
    private static String Region = "Region";
    private static String BusinessUnit = "Business Unit";
//    private static String opEnterprice = "CinemarkWkdy_Enterprise";

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) {
        try {
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the generation of P2P LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheGenerationOfP2PLGScheduleAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            //Verify manager can edit operating hours before generate schedule (P2P cannot edit operating hour on toggle summary page before generate)
//            createSchedulePage.selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(null);
//            List<String> toCloseDays = new ArrayList<>();
//            newShiftPage.editOperatingHoursOnScheduleOldUIPage("05:00am", "11:00pm", toCloseDays);
//            String operatingHrs = toggleSummaryPage.getOperatingHrsValue("Sun").get("ScheduleOperatingHrs");
//            String expectOperatingHrs = "5AM-11PM";
//            SimpleUtils.assertOnFail("The expected operating hrs is"+expectOperatingHrs+", but actual is:"+operatingHrs,
//                    operatingHrs.contains(expectOperatingHrs), false);
            //Verify manager can search and select locations on operating hours page when generate schedule
            //Verify manager can search and select location on edit budget page when generate schedule  https://legiontech.atlassian.net/browse/SCH-4835
            //Verify manager can generate schedule successfully
            createSchedulePage.createLGScheduleWithGivingTimeRange("06:00am", "06:00am");
            scheduleMainPage.goToToggleSummaryView();
            String expectOperatingHrs = "6AM-6AM";
            String operatingHrs = toggleSummaryPage.getOperatingHrsValue("Sun").get("ScheduleOperatingHrs");
            SimpleUtils.assertOnFail("The expected operating hrs is Aam-11pm, but actual is:"+operatingHrs,
                    operatingHrs.contains(expectOperatingHrs), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate mananger cannot edit operating hours when disable it's Manage Working Hours Setting permission for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateManagerCannotEditOperatingHoursWhenDisableItsManageWorkingHoursSettingPermissionForP2PLGAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            //Log in as admin, uncheck the Working Hours Setting Permission to SM.
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            userManagementPage.clickOnUserManagementTab();
            SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
            userManagementPage.goToUserAndRoles();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(ScheduleTestKendraScott2.usersAndRolesSubTabs.AccessByJobTitles.getValue());

            String permissionSection = "Controls";
            String permission = "Manage Working Hours Settings";
            String actionOff = "off";
            String actionOn = "on";
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission, actionOff);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            switchToConsoleWindow();
            Thread.sleep(5000);

            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            loginPage.logOut();

            //Log in as SM
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            int i=0;
            while (i<10 && createSchedulePage.checkIfEditOperatingHoursButtonsAreShown()){
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.StoreManager.getValue());
                goToSchedulePageScheduleTab();
                scheduleCommonPage.navigateToNextWeek();
                i++;
            }
            //Check the edit buttons on ungenerate schedule page
            SimpleUtils.assertOnFail("Edit operating hours buttons are shown on ungenerate schedule page! ",
                    !createSchedulePage.checkIfEditOperatingHoursButtonsAreShown() , false);

            //Check the edit button on create schedule page
            createSchedulePage.clickCreateScheduleBtn();
            i=0;
            while (i<10 && createSchedulePage.checkIfEditOperatingHoursButtonsAreShown()){
                createSchedulePage.clickExitBtnToExitCreateScheduleWindow();
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.StoreManager.getValue());
                goToSchedulePageScheduleTab();
                scheduleCommonPage.navigateToNextWeek();
                createSchedulePage.clickCreateScheduleBtn();
                i++;
            }
            SimpleUtils.assertOnFail("Edit operating hours buttons are shown on ungenerate schedule page! ",
                    !createSchedulePage.checkIfEditOperatingHoursButtonsAreShown() , false);
            createSchedulePage.clickExitBtnToExitCreateScheduleWindow();
            loginPage.logOut();

            //Log in as admin, grant the Working Hours Setting Permission to SM.
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            userManagementPage.clickOnUserManagementTab();
            SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
            userManagementPage.goToUserAndRoles();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(ScheduleTestKendraScott2.usersAndRolesSubTabs.AccessByJobTitles.getValue());
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission, actionOn);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
            switchToConsoleWindow();
            Thread.sleep(5000);
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            loginPage.logOut();

            // Check SM cannot edit operating hours now
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            i=0;
            while (i<10 && !createSchedulePage.checkIfEditOperatingHoursButtonsAreShown()){
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.StoreManager.getValue());
                goToSchedulePageScheduleTab();
                scheduleCommonPage.navigateToNextWeek();
                i++;
            }
            //Check the edit buttons on ungenerate schedule page
            SimpleUtils.assertOnFail("Edit operating hours buttons are shown on ungenerate schedule page! ",
                    createSchedulePage.checkIfEditOperatingHoursButtonsAreShown() , false);

            //Check the edit button on create schedule page
            createSchedulePage.clickCreateScheduleBtn();
            i=0;
            while (i<20 && createSchedulePage.checkIfEditOperatingHoursButtonsAreShown()){
                createSchedulePage.clickExitBtnToExitCreateScheduleWindow();
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.StoreManager.getValue());
                goToSchedulePageScheduleTab();
                scheduleCommonPage.navigateToNextWeek();
                createSchedulePage.clickCreateScheduleBtn();
                i++;
            }
            SimpleUtils.assertOnFail("Edit operating hours buttons are shown on ungenerate schedule page! ",
                    createSchedulePage.checkIfEditOperatingHoursButtonsAreShown() , false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate all smart cards display correctly after generate P2P schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAllSmartCardsDisplayCorrectlyAfterGenerateP2PScheduleAsInternalAdmin (String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            deleteAllUnassignedShifts();
            scheduleMainPage.clickOnFilterBtn();
            String childLocation = scheduleMainPage.selectRandomChildLocationToFilter();
            //Check Schedule not published smart card is display
            SimpleUtils.assertOnFail("Schedule not published smart card should display for new generate schedule! ",
                    smartCardPage.isScheduleNotPublishedSmartCardLoaded(),false);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocation, "8am", "9pm",
                    1, Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, null);
            scheduleMainPage.saveSchedule();
            //verify Compliance SmartCard Functionality
            if(smartCardPage.verifyComplianceShiftsSmartCardShowing() && smartCardPage.verifyRedFlagIsVisible()){
                smartCardPage.verifyComplianceFilterIsSelectedAftClickingViewShift();
                smartCardPage.verifyComplianceShiftsShowingInGrid();
                smartCardPage.verifyClearFilterFunction();
            }else
                SimpleUtils.fail("There is no compliance and no red flag", false);
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();

            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(0);
            SimpleUtils.assertOnFail("Schedule not published smart card should display for new generate schedule! ",
                    smartCardPage.isScheduleNotPublishedSmartCardLoaded(),false);
            //verify Compliance SmartCard Functionality
            if(smartCardPage.verifyComplianceShiftsSmartCardShowing() && smartCardPage.verifyRedFlagIsVisible()){
                smartCardPage.verifyComplianceFilterIsSelectedAftClickingViewShift();
                smartCardPage.verifyComplianceShiftsShowingInGrid();
                smartCardPage.verifyClearFilterFunction();
            }else
                SimpleUtils.fail("There is no compliance and no red flag", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the buttons on P2P LG schedule page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheButtonsOnP2PLGSchedulePageAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            AnalyzePage analyzePage = pageFactory.createAnalyzePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            // Edit random location's operating hours during generate schedule
            createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");

            // Check Edit button
            scheduleMainPage.checkEditButton();
            scheduleMainPage.verifyEditButtonFuntionality();

            // Check Publish button
            createSchedulePage.isPublishButtonLoadedOnSchedulePage();
            deleteAllUnassignedShifts();
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("Schedule should be published! ",
                    createSchedulePage.isCurrentScheduleWeekPublished(), false);

            // Check Republish button
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnEditShiftTime();
            shiftOperatePage.verifyEditShiftTimePopUpDisplay();
            shiftOperatePage.editShiftTime();
            shiftOperatePage.clickOnUpdateEditShiftTimeButton();
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("The Republish button should display! ", createSchedulePage.isRepublishButtonLoadedOnSchedulePage(), false);
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("Schedule should be published! ", createSchedulePage.isCurrentScheduleWeekPublished(), false);

            // Check Analyze button
            analyzePage.verifyAnalyzeBtnFunctionAndScheduleHistoryScroll();


            // Check Toggle Summary View
            scheduleMainPage.goToToggleSummaryView();
            //verify the operating hours in Toggle Summary View
            createSchedulePage.checkIfEditOperatingHoursButtonsAreShown();
            scheduleMainPage.goToToggleSummaryView();

            // Ungenerate button
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            SimpleUtils.assertOnFail("Schedule should been ungenerated", !createSchedulePage.isWeekGenerated(), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of auto open shift for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfAutoOpenShiftForP2PLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isActiveWeekGenerated) {
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnFilterBtn();
            String childLocation = scheduleMainPage.selectRandomChildLocationToFilter();
            if (scheduleShiftTablePage.getShiftsCount()>0){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Create auto open shift.
            createShiftsWithSpecificValues(workRole, null, childLocation,
                    "8am", "11am", 1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.OpenShift.getValue(),
                    null, null);
            scheduleMainPage.saveSchedule();

            //edit shift time
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            String selectedShiftId= selectedShift.getAttribute("id");
            int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            List<String> shiftInfoBefore = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            shiftOperatePage.editTheShiftTimeForSpecificShift(selectedShift, "9am", "2pm");
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            List<String> shiftInfoAfter= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            SimpleUtils.assertOnFail("Auto shift time is not updated!", !shiftInfoBefore.containsAll(shiftInfoAfter), false);

            //change work role
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.clickOnChangeRole();
            shiftOperatePage.verifyChangeRoleFunctionality();
            //check the work role by click Apply button
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.changeWorkRoleInPromptOfAShift(true, selectedShift);
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            List<String> shiftInfoAfterChangeRole= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            SimpleUtils.assertOnFail("Work role is not updated!", !shiftInfoBefore.containsAll(shiftInfoAfterChangeRole), false);


            //Edit meal break
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.verifyEditMealBreakTimeFunctionalityForAShift(true, selectedShift);

            //Offer Team members
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            shiftOperatePage.verifyRecommendedTableHasTM();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            //View status
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();

            //Assign TM
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.clickonAssignTM();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            List<String> shiftInfoAfterAssignTM= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            SimpleUtils.assertOnFail("TM is not assigned!", !shiftInfoBefore.containsAll(shiftInfoAfterAssignTM), false);

            //Delete open shift
            shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            int tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstNameOfTM);
            SimpleUtils.assertOnFail("The expect open shift count is 0, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount == 0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of auto open shift in day view for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfAutoOpenShiftInDayViewForP2PLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isActiveWeekGenerated) {
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            System.out.println("The first name of the selected TM is:"+ firstNameOfTM);
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnFilterBtn();
            String childLocation = scheduleMainPage.selectRandomChildLocationToFilter();
            if (scheduleShiftTablePage.getShiftsCount()>0){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteAllShiftsInDayView();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(0);

            //Create auto open shift.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocation,
                    "8am", "2pm", 1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.OpenShift.getValue(),
                    null, null);
            scheduleMainPage.saveSchedule();

            //edit shift time
//        String firstNameOfTM = "Dejah";
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(0);
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleMainPage.clickOnFilterBtn();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            String selectedShiftId= selectedShift.getAttribute("id");
            int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            String shiftInfoBefore = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index);
            shiftOperatePage.editTheShiftTimeForSpecificShift(selectedShift, "9am", "4pm");
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            String shiftInfoAfter= scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index);
            SimpleUtils.assertOnFail("Auto shift time is not updated!",
                    !shiftInfoBefore.equalsIgnoreCase(shiftInfoAfter), false);

            //change work role
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.clickOnChangeRole();
            shiftOperatePage.verifyChangeRoleFunctionality();
            //check the work role by click Apply button
            shiftOperatePage.changeWorkRoleInPrompt(true);

            //Edit meal break
            if (shiftOperatePage.isEditMealBreakEnabled()){
                //After click on Edit Meal Break Time, the Edit Meal Break window will display
                shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(true);
                //Verify Delete Meal Break
                shiftOperatePage.verifyDeleteMealBreakFunctionality();
                //Edit meal break time and click update button
                shiftOperatePage.verifyEditMealBreakTimeFunctionality(true);
                //Edit meal break time and click cancel button
                shiftOperatePage.verifyEditMealBreakTimeFunctionality(false);
            } else
                shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(false);

            //Offer Team members
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            //View status
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "offered");
            shiftOperatePage.closeViewStatusContainer();

            //Assign TM
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.clickonAssignTM();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.selectTeamMembers();
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            String shiftInfoAfterAssignTM= scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index);
            SimpleUtils.assertOnFail("TM is not assigned!",
                    !shiftInfoBefore.equalsIgnoreCase(shiftInfoAfterAssignTM), false);

            //Delete open shift
            shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            int tmShiftCount = scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM).size();
            SimpleUtils.assertOnFail("The expect open shift count is 0, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount == 0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of manual open shift for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfManualOpenShiftForP2PLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }

            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            //Create shift and assign to TM.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocationNames.get(0),
                    "8am", "2pm", 1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.ManualShift.getValue(),
                    null, firstNameOfTM );

            //Edit meal break
            WebElement selectedShift = null;
            selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            shiftOperatePage.verifyEditMealBreakTimeFunctionalityForAShift(true, selectedShift);


            //edit shift time
            selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            String selectedShiftId= selectedShift.getAttribute("id");
            int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            List<String> shiftInfoBefore = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            shiftOperatePage.editTheShiftTimeForSpecificShift(selectedShift, "9am", "2pm");
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            List<String> shiftInfoAfter= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            SimpleUtils.assertOnFail("Auto shift time is not updated!", !shiftInfoBefore.containsAll(shiftInfoAfter), false);

            //change work role
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            shiftOperatePage.clickOnChangeRole();
            shiftOperatePage.verifyChangeRoleFunctionality();
            //check the work role by click Apply button
            shiftOperatePage.changeWorkRoleInPrompt(true);
            scheduleMainPage.saveSchedule();
            //View status
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "");
            shiftOperatePage.closeViewStatusContainer();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            //Assign TM
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            shiftOperatePage.clickonAssignTM();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            index = scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM).get(0);
            List<String> shiftInfoAfterAssignTM= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            SimpleUtils.assertOnFail("TM is not assigned!", !shiftInfoBefore.containsAll(shiftInfoAfterAssignTM), false);

            //Delete open shift
            shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            int tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstNameOfTM);
            SimpleUtils.assertOnFail("The expect open shift count is 0, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount == 0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of manual open shift in day view for p2p LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfManualOpenShiftInDayViewFoP2PLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();

            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isActiveWeekGenerated) {
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            scheduleMainPage.clickOnFilterBtn();
            String childLocation = scheduleMainPage.selectRandomChildLocationToFilter();
            if (scheduleShiftTablePage.getShiftsCount()>0){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
                scheduleMainPage.saveSchedule();
            }
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(0);

            //Create manual open shift.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocation,
                    "8am", "2pm", 1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.ManualShift.getValue(),
                    null, firstNameOfTM);
            scheduleMainPage.saveSchedule();

            //edit shift time
//        String firstNameOfTM = "Rose";
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(0);
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleMainPage.clickOnFilterBtn();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            WebElement selectedShift = shiftOperatePage.clickOnProfileIconOfOpenShift();
            String selectedShiftId= selectedShift.getAttribute("id");
            int index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            String shiftInfoBefore = scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index);
            shiftOperatePage.editTheShiftTimeForSpecificShift(selectedShift, "9am", "4pm");
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            String shiftInfoAfter= scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index);
            SimpleUtils.assertOnFail("Auto shift time is not updated!",
                    !shiftInfoBefore.equalsIgnoreCase(shiftInfoAfter), false);

            //change work role
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.clickOnChangeRole();
            shiftOperatePage.verifyChangeRoleFunctionality();
            //check the work role by click Apply button
            shiftOperatePage.changeWorkRoleInPrompt(true);

            //Edit meal break
            if (shiftOperatePage.isEditMealBreakEnabled()){
                //After click on Edit Meal Break Time, the Edit Meal Break window will display
                shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(true);
                //Verify Delete Meal Break
                shiftOperatePage.verifyDeleteMealBreakFunctionality();
                //Edit meal break time and click update button
                shiftOperatePage.verifyEditMealBreakTimeFunctionality(true);
                //Edit meal break time and click cancel button
                shiftOperatePage.verifyEditMealBreakTimeFunctionality(false);
            } else
                shiftOperatePage.verifyMealBreakTimeDisplayAndFunctionality(false);

            //View status
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstNameOfTM, "");
            shiftOperatePage.closeViewStatusContainer();

            //Assign TM
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.clickonAssignTM();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.selectTeamMembers();
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            index = scheduleShiftTablePage.getShiftIndexById(selectedShiftId);
            String shiftInfoAfterAssignTM= scheduleShiftTablePage.getTheShiftInfoByIndexInDayview(index);
            SimpleUtils.assertOnFail("TM is not assigned!",
                    !shiftInfoBefore.equalsIgnoreCase(shiftInfoAfterAssignTM), false);

            //Delete open shift
            shiftOperatePage.deleteTMShiftsInDayView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            int tmShiftCount = scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM).size();
            SimpleUtils.assertOnFail("The expect open shift count is 0, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount == 0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of Assign TM shift for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfAssignTMShiftForP2PLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }

            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String firstNameOfTM2 = "";
            while (firstNameOfTM2.equals("") || firstNameOfTM2.equalsIgnoreCase("Open")
                    || firstNameOfTM2.equalsIgnoreCase("Unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM2 = shiftInfo.get(0);
            }
            String workRole2 = shiftInfo.get(4);
            String lastName2 = shiftInfo.get(5);
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            //Create shift and assign to TM.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocationNames.get(0),
                    "8am", "2pm", 1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM );
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int index = scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM).get(0);
            //Edit meal break
            shiftOperatePage.clickOnProfileIconByIndex(index);
            WebElement selectedShift =  scheduleShiftTablePage.getTheShiftByIndex(index);
            shiftOperatePage.verifyEditMealBreakTimeFunctionalityForAShift(true, selectedShift);

            //edit shift time
            List<String> shiftInfoBefore = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            shiftOperatePage.editTheShiftTimeForSpecificShift(selectedShift, "9am", "2pm");
            index = scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM).get(0);
            List<String> shiftInfoAfter= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            SimpleUtils.assertOnFail("Assign TM shift time is not updated!", !shiftInfoBefore.containsAll(shiftInfoAfter), false);

            //change work role
            shiftOperatePage.clickOnProfileIconByIndex(index);
            shiftOperatePage.clickOnChangeRole();
            shiftOperatePage.verifyChangeRoleFunctionality();
            //check the work role by click Apply button
            shiftOperatePage.changeWorkRoleInPrompt(true);
            scheduleMainPage.saveSchedule();
            // view profile
            shiftOperatePage.clickOnProfileIconByIndex(index);
            shiftOperatePage.clickOnViewProfile();
            shiftOperatePage.verifyPersonalDetailsDisplayed();
            shiftOperatePage.verifyWorkPreferenceDisplayed();
            shiftOperatePage.verifyAvailabilityDisplayed();
            shiftOperatePage.closeViewProfileContainer();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            //Assign TM
            shiftOperatePage.clickOnProfileIconByIndex(index);
            shiftOperatePage.clickonAssignTM();
            newShiftPage.verifySelectTeamMembersOption();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2+ " "+lastName2);
            newShiftPage.clickOnOfferOrAssignBtn();
            index = scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM2).get(0);
            List<String> shiftInfoAfterAssignTM= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            SimpleUtils.assertOnFail("TM is not assigned!", !shiftInfoBefore.containsAll(shiftInfoAfterAssignTM), false);

            //Delete open shift
            shiftOperatePage.deleteTMShiftInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();

            int tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstNameOfTM2);
            SimpleUtils.assertOnFail("The expect shift count is 0, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount == 0, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Assign TM when TM has time off on that day for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAssignTMWhenTMHasTimeOffThatDayForP2PLGAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();

            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "08:00pm");
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            //Get the date info of the week for create time off
            String activeWeek = scheduleCommonPage.getActiveWeekText();
            List<String> year = scheduleCommonPage.getYearsFromCalendarMonthYearText();
            String[] items = activeWeek.split(" ");
            String fromDate = year.get(0)+ " " + items[3] + " " + items[4];
            String endDate = year.get(0)+ " " + items[6] + " " + items[7];
//            String nickNameFromProfile = profileNewUIPage.getNickNameFromProfile();
            String myProfileLabel = "My Profile";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            String nickNameFromProfile = profileNewUIPage.getUserProfileName().get("fullName");
            String myTimeOffLabel = "My Time Off";
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            String timeOffReasonLabel = "JURY DUTY";
            String timeOffExplanationText = "Sample Explanation Text";
            String timeOffStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel, timeOffExplanationText, fromDate, endDate);
            if (!timeOffStatus.equalsIgnoreCase("approved")) {
                profileNewUIPage.cancelAllTimeOff();
                //Go to team page and create time off for tm
                profileNewUIPage.createTimeOffOnSpecificDays(timeOffReasonLabel, timeOffExplanationText, fromDate, 6);
                teamPage.approvePendingTimeOffRequest();
            }

            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String workRole = shiftOperatePage.getRandomWorkRole();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(nickNameFromProfile);
            shiftOperatePage.verifyMessageIsExpected("time off");
            shiftOperatePage.verifyWarningModelForAssignTMOnTimeOff(nickNameFromProfile);
            scheduleMainPage.clickOnCancelButtonOnEditMode();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
        finally{
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //go to cancel the time off.
            String myProfileLabel = "My Profile";
            String myTimeOffLabel = "My Time Off";
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            profileNewUIPage.cancelAllTimeOff();
        }
    }


//    @Automated(automated ="Automated")
//    @Owner(owner = "Mary")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify TM cannot have a shift in more than one location without buffer time for P2P LG")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
//    public void verifyTMCannotHaveShiftInMoreThanOneLocationWithoutBufferTimeForP2PAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        try {
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
//            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//            if(!isActiveWeekGenerated){
//                createSchedulePage.createScheduleForNonDGFlowNewUI();
//            }
//            scheduleMainPage.clickOnFilterBtn();
//            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
//            List<String> shiftInfo = new ArrayList<>();
//            String firstNameOfTM = "";
//            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
//                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
//                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
//                //Search shift by TM names: first name and last name
//                firstNameOfTM = shiftInfo.get(0);
//            }
//            String workRole = shiftInfo.get(4);
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
//            scheduleMainPage.saveSchedule();
//            //Go to day view, check for TM: A at child location 1, has one shift, ex: 6am - 8am
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            newShiftPage.clickOnDayViewAddNewShiftButton();
//            newShiftPage.clickCloseBtnForCreateShift();
//            newShiftPage.clickOnDayViewAddNewShiftButton();
//            newShiftPage.customizeNewShiftPage();
//            newShiftPage.clearAllSelectedDays();
//            newShiftPage.selectSpecificWorkDay(1);
//            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
//            newShiftPage.moveSliderAtCertainPoint("10:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//            newShiftPage.selectWorkRole(workRole);
//            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//            newShiftPage.clickOnCreateOrNextBtn();
//            newShiftPage.searchTeamMemberByName(firstNameOfTM);
//            newShiftPage.clickOnCreateOrNextBtn();
//            scheduleMainPage.saveSchedule();
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            newShiftPage.clickOnDayViewAddNewShiftButton();
//            newShiftPage.customizeNewShiftPage();
//            newShiftPage.clearAllSelectedDays();
//            newShiftPage.selectSpecificWorkDay(1);
//            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
//            newShiftPage.moveSliderAtCertainPoint("12:15pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//            newShiftPage.moveSliderAtCertainPoint("10:15am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//            newShiftPage.selectWorkRole(workRole);
//            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//            newShiftPage.clickOnCreateOrNextBtn();
//            newShiftPage.searchWithOutSelectTM(firstNameOfTM);
//            String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
//            SimpleUtils.assertOnFail("30 mins travel time needed violation message fail to load!",
//                    !shiftWarningMessage.toLowerCase().contains("30 mins travel time needed"), false);
//            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
//            if(newShiftPage.ifWarningModeDisplay()){
////                String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
////                if (warningMessage.contains("0 mins travel time needed violation")){
////                    SimpleUtils.pass("30 mins travel time needed violation message displays");
////                } else {
////                    SimpleUtils.fail("There is no '30 mins travel time needed violation' warning message displaying", false);
////                }
//                shiftOperatePage.clickOnAssignAnywayButton();
//            }
////            else {
////                SimpleUtils.fail("There is no '30 mins travel time needed violation' warning modal displaying!",false);
////            }
//            newShiftPage.clickOnOfferOrAssignBtn();
//            scheduleMainPage.saveSchedule();
//            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
//            SimpleUtils.assertOnFail("'30 mins travel time needed violation' compliance message display failed",
//                    !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(1)).contains("Max shift per day violation") , false);
//
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }


    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Assign TM when TM has max no. of shifts scheduled for p2p LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAssignTMWhenTMHasMaxNoOfShiftsScheduledForP2PAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM = shiftInfo1.get(0);
            int shiftCount1 = 0;
            while ((firstNameOfTM.equalsIgnoreCase("open")
                    || firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
                shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM  = shiftInfo1.get(0);
                shiftCount1++;
            }
            String workRole =  shiftInfo1.get(4);
            String lastName = shiftInfo1.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("10:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnCreateOrNextBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("1:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnCreateOrNextBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            Thread.sleep(2000);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("3:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("2:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("6:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("4:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            //https://legiontech.atlassian.net/browse/SCH-7963
//			newShiftPage.searchWithOutSelectTM(firstNameOfTM);
//			String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
//			SimpleUtils.assertOnFail("Max shift per day violation message fail to load!",
//					shiftWarningMessage.contains("Max shift per day violation"), false);
//			shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
//			if(newShiftPage.ifWarningModeDisplay()){
//				String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
//				if (warningMessage.contains("Max shift per day violation")){
//					SimpleUtils.pass("Max shift per day violation message displays");
//				} else {
//					SimpleUtils.fail("There is no 'Max shift per day violation' warning message displaying, the actual is:"+warningMessage, false);
//				}
//				shiftOperatePage.clickOnAssignAnywayButton();
//			} else {
//				SimpleUtils.fail("There is no 'Max shift per day violation' warning modal displaying!",false);
//			}
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
            String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(3)).toString();
            SimpleUtils.assertOnFail("'Max shift per day violation' compliance message display failed",
                    actualMessage.contains("Max shift per day violation") , false);
        }catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Assign TM when TM has overlapping violation for p2p LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAssignTMWhenTMHasOverlappingViolationForP2PAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            List<String> shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM = shiftInfo1.get(0);
            int shiftCount1 = 0;
            while ((firstNameOfTM.equalsIgnoreCase("open")
                    || firstNameOfTM.equalsIgnoreCase("unassigned")) && shiftCount1 < 100) {
                shiftInfo1 = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM  = shiftInfo1.get(0);
                shiftCount1++;
            }
            String workRole =  shiftInfo1.get(4);
            String lastNameOfTM = shiftInfo1.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(0);
            String weekDay = SimpleUtils.getFullWeekDayName(scheduleCommonPage.getActiveDayInfo().get("weekDay"));
            String month = scheduleCommonPage.getActiveDayInfo().get("month");
            String day = scheduleCommonPage.getActiveDayInfo().get("day");
            scheduleCommonPage.clickOnWeekView();
            String shiftStartTime = "8am";
            String shiftEndTime = "11am";
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            Thread.sleep(2000);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+ lastNameOfTM);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchWithOutSelectTM(firstNameOfTM + " "+lastNameOfTM);
            String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus().toLowerCase();
            SimpleUtils.assertOnFail("Overlapping violation message fail to load! The actual message is: "+shiftWarningMessage,
                    shiftWarningMessage.contains(shiftStartTime) && shiftWarningMessage.contains(shiftEndTime), false);
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
            String expectedWarningMessage = firstNameOfTM+ " is scheduled "+ shiftStartTime+ " - "+shiftEndTime+ " on "+ weekDay;
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
                if (warningMessage.toLowerCase().contains(expectedWarningMessage.toLowerCase())){
                    SimpleUtils.pass("Overlapping violation message displays");
                } else {
                    SimpleUtils.fail("There is no Overlapping warning message displaying, the actual is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else {
                SimpleUtils.fail("There is no Overlapping warning modal displaying!",false);
            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
            List<WebElement> openShiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, "open");
            SimpleUtils.assertOnFail("New shift display failed", shiftsOfFirstDay.size() == 1, false);
            SimpleUtils.assertOnFail("Covert to open shift display failed", openShiftsOfFirstDay.size() == 1, false);
        }catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the group by dropdown list for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheGroupByDropdownListForP2PAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            goToSchedulePageScheduleTab();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Check schedule defaulted with group by location
            SimpleUtils.assertOnFail("Schedule table should defaulted with group by location! ",scheduleMainPage.getActiveGroupByFilter().equals("Group by Location"), false);
            //In week view, Group by All filter have 4 filters:1.Group by all  2. Group by work role  3. Group by TM 4.Group by job title
            scheduleMainPage.validateGroupBySelectorSchedulePage(true);
            //Selecting any of them, check the schedule table
            scheduleMainPage.validateScheduleTableWhenSelectAnyOfGroupByOptions(true);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Julie/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the filter on schedule page for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFilterOnSchedulePageForP2PAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();

            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            createSchedulePage.publishActiveSchedule();

            // Verify the filter UI display correctly
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.verifyFilterDropdownList(true);

            // Verify the location filter has been moved to the left
            scheduleMainPage.verifyLocationFilterInLeft(true);

            // Verify performance target < 3 seconds to load
            scheduleMainPage.verifyAllChildLocationsShiftsLoadPerformance();
            String childLocation = scheduleMainPage.selectRandomChildLocationToFilter();
            scheduleMainPage.verifyChildLocationShiftsLoadPerformance(childLocation);

            // Verify shifts will display according to location filter
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            String childLocation1 = scheduleMainPage.selectRandomChildLocationToFilter();
            scheduleMainPage.verifyShiftsDisplayThroughLocationFilter(childLocation1);
            scheduleMainPage.clickOnFilterBtn();
            String childLocation2 = scheduleMainPage.selectRandomChildLocationToFilter();
            scheduleMainPage.verifyShiftsDisplayThroughLocationFilter(childLocation2);

            // Verify budget smart cards default view will be able to show location aggregated
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            scheduleMainPage.selectAllChildLocationsToFilter();
            HashMap<String, String> budgetNScheduledHoursForAllLocations = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
            String budgetHrsFromSmartCard = budgetNScheduledHoursForAllLocations.get("Budget").replace(",","");
            float budgetHrsForAllLocations = 0;
            if (!budgetHrsFromSmartCard.equalsIgnoreCase("N/a")){
                budgetHrsForAllLocations = Float.parseFloat(budgetHrsFromSmartCard);
            }
            float scheduledHrsForAllLocations = Float.parseFloat(budgetNScheduledHoursForAllLocations.get("Scheduled").replace(",",""));
            float budgetHrs = 0;
            float scheduledHrs = 0;
            for (String childLocationName : childLocationNames) {
                scheduleMainPage.selectLocationFilterByText(childLocationName);
                HashMap<String, String> budgetNScheduledHoursForOneChild = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
                budgetHrsFromSmartCard = budgetNScheduledHoursForOneChild.get("Budget").replace(",","");
                if (!budgetHrsFromSmartCard.equalsIgnoreCase("N/a")){
                    budgetHrs = budgetHrs+ Float.parseFloat(budgetHrsFromSmartCard);
                }
                scheduledHrs = scheduledHrs + Float.parseFloat(budgetNScheduledHoursForOneChild.get("Scheduled").replace(",",""));
            }
            //https://legiontech.atlassian.net/browse/SCH-8006
//            if (budgetHrsForAllLocations == (budgetHrs))
//                SimpleUtils.pass("Schedule Page: The budget numbers in schedule hour smart card change according to the filter ");
//            else
//                SimpleUtils.fail("Schedule Page: The budget numbers in schedule hour smart card don't change according to the filter ",false);
            if (scheduledHrsForAllLocations == (scheduledHrs))
                SimpleUtils.pass("Schedule Page: The scheduled numbers in schedule hour smart card change according to the filter ");
            else
                SimpleUtils.fail("Schedule Page: The scheduled numbers in schedule hour smart card don't change according to the filter ",false);

            // Verify compliance smart cards default view will be able to show location aggregated
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectAllChildLocationsToFilter();
            int complianceShiftCountForAllLocations = 0;
            if (smartCardPage.verifyComplianceShiftsSmartCardShowing())
                complianceShiftCountForAllLocations = smartCardPage.getComplianceShiftCountFromSmartCard("COMPLIANCE");
            String location1 = scheduleMainPage.selectRandomChildLocationToFilter();
            int  complianceShiftCountForOneChild = 0;
            if (smartCardPage.verifyComplianceShiftsSmartCardShowing())
                complianceShiftCountForOneChild = smartCardPage.getComplianceShiftCountFromSmartCard("COMPLIANCE");
            if (complianceShiftCountForAllLocations >= complianceShiftCountForOneChild)
                SimpleUtils.pass("Schedule Page: The numbers in budget smart card change according to the filter ");
            else
                SimpleUtils.fail("Schedule Page: The numbers in budget smart card don't change according to the filter ",false);

            // Verify changes not publish smart card default view will be able to show location aggregated
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            String randomWorkRole = shiftOperatePage.getRandomWorkRole();
            newShiftPage.addOpenShiftWithDefaultTime(randomWorkRole, childLocationNames.get(0));
            newShiftPage.addOpenShiftWithDefaultTime(randomWorkRole, childLocationNames.get(1));
            scheduleMainPage.saveSchedule();
            int changeCountForAllLocations = smartCardPage.getCountFromSmartCardByName("UNPUBLISHED CHANGES");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.selectLocationFilterByText(childLocationNames.get(0));
            int changeCountForChildLocation = smartCardPage.getCountFromSmartCardByName("UNPUBLISHED CHANGES");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.selectLocationFilterByText(childLocationNames.get(1));
            changeCountForChildLocation = changeCountForChildLocation
                    + smartCardPage.getCountFromSmartCardByName("UNPUBLISHED CHANGES");
            if (changeCountForAllLocations == changeCountForChildLocation)
                SimpleUtils.pass("Schedule Page: The numbers in changes not publish smart card change according to the filter");
            else
                SimpleUtils.fail("Schedule Page: The numbers in changes not publish smart card don't change according to the filter",false);

            // Verify shifts, all smart cards are display according to the other filter options except locations
            int shiftsCountForAllWorkRoles = scheduleShiftTablePage.getShiftsCount();
            List<String> workRoles = scheduleMainPage.getSpecificFilterNames("Work Role");
            int shiftCount = 0;
            for (String workRole: workRoles){
                scheduleMainPage.selectWorkRoleFilterByText(workRole, true);
                shiftCount = shiftCount+ scheduleShiftTablePage.getShiftsCount();
                scheduleMainPage.clickOnFilterBtn();
            }
            if (shiftsCountForAllWorkRoles == shiftCount)
                SimpleUtils.pass("Schedule Page: The shifts and compliance smart card display according to the filter");
            else
                SimpleUtils.fail("Schedule Page: The shifts and compliance smart card don't change according to the filter",false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the function of copy schedule for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheFunctionOfCopyScheduleForP2PAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            List<String> weekDaysToClose = new ArrayList<>();
            weekDaysToClose.add("Sunday");
            createSchedulePage.createScheduleForNonDGByWeekInfo("SUGGESTED", weekDaysToClose, null);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            scheduleMainPage.clickOnFilterBtn();
            List<String> workRoles = scheduleMainPage.getSpecificFilterNames("Work Role");
            //Create auto open shift.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            String workRole = shiftOperatePage.getRandomWorkRole();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRoles.get(0));
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            // Get the hours and the count of the tms for each day, ex: "37.5 Hrs 5TMs"
            HashMap<String, String> hoursNTMsCountFirstWeek = scheduleShiftTablePage.getTheHoursNTheCountOfTMsForEachWeekDays();
            HashMap<String, List<String>> shiftsForEachDayFirstWeek = scheduleShiftTablePage.getTheContentOfShiftsForEachWeekDay();
            HashMap<String, String> budgetNScheduledHoursFirstWeek = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
            String cardName = "COMPLIANCE";
            boolean isComplianceCardLoadedFirstWeek = smartCardPage.isSpecificSmartCardLoaded(cardName);
            int complianceShiftCountFirstWeek = 0;
            if (isComplianceCardLoadedFirstWeek) {
                complianceShiftCountFirstWeek = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
            }
            String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
            if (firstWeekInfo.length() > 11) {
                firstWeekInfo = firstWeekInfo.trim().substring(10);
//                if (firstWeekInfo.contains("-")) {
//                    String[] temp = firstWeekInfo.split("-");
//                    if (temp.length == 2 && temp[0].contains(" ") && temp[1].contains(" ")) {
//                        firstWeekInfo = temp[0].trim().split(" ")[0] + " " + (temp[0].trim().split(" ")[1].length() == 1 ? "0" + temp[0].trim().split(" ")[1] : temp[0].trim().split(" ")[1])
//                                + " - " + temp[1].trim().split(" ")[0] + " " + (temp[1].trim().split(" ")[1].length() == 1 ? "0" + temp[1].trim().split(" ")[1] : temp[1].trim().split(" ")[1]);
//                    }
//                }
            }

            scheduleCommonPage.navigateToNextWeek();

            //Full copy
            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGByWeekInfo(firstWeekInfo, weekDaysToClose, null);
            HashMap<String, String> hoursNTMsCountSecondWeek = scheduleShiftTablePage.getTheHoursNTheCountOfTMsForEachWeekDays();
            HashMap<String, List<String>> shiftsForEachDaySecondWeek = scheduleShiftTablePage.getTheContentOfShiftsForEachWeekDay();
            HashMap<String, String> budgetNScheduledHoursSecondWeek = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
            boolean isComplianceCardLoadedSecondWeek = smartCardPage.isSpecificSmartCardLoaded(cardName);
            int complianceShiftCountSecondWeek = 0;
            if (isComplianceCardLoadedFirstWeek) {
                complianceShiftCountSecondWeek = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
            }

            if (hoursNTMsCountFirstWeek.equals(hoursNTMsCountSecondWeek)) {
                SimpleUtils.pass("Verified the scheduled hour and TMs of each week day are consistent with the copied schedule!");
            } else {
                SimpleUtils.fail("Verified the scheduled hour and TMs of each week day are inconsistent with the copied schedule", true);
            }
            if (SimpleUtils.compareHashMapByEntrySet(shiftsForEachDayFirstWeek, shiftsForEachDaySecondWeek)) {
                SimpleUtils.pass("Verified the shifts of each week day are consistent with the copied schedule!");
            } else {
                SimpleUtils.fail("Verified the shifts of each week day are inconsistent with the copied schedule!", true);
            }
            if (budgetNScheduledHoursFirstWeek.get("Scheduled").equals(budgetNScheduledHoursSecondWeek.get("Scheduled"))) {
                SimpleUtils.pass("The Scheduled hour is consistent with the copied scheudle: " + budgetNScheduledHoursFirstWeek.get("Scheduled"));
            } else {
                SimpleUtils.fail("The Scheduled hour is inconsistent, the first week is: " + budgetNScheduledHoursFirstWeek.get("Scheduled")
                        + ", but second week is: " + budgetNScheduledHoursSecondWeek.get("Scheduled"), true);
            }
            if ((isComplianceCardLoadedFirstWeek == isComplianceCardLoadedSecondWeek) && (complianceShiftCountFirstWeek == complianceShiftCountSecondWeek)) {
                SimpleUtils.pass("Verified Compliance is consistent with the copied schedule");
            } else {
                SimpleUtils.fail("Verified Compliance is inconsistent with the copied schedule!", true);
            }

            //Partial copy and select all work roles
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGByWeekInfo(firstWeekInfo, weekDaysToClose, workRoles);

            hoursNTMsCountSecondWeek = scheduleShiftTablePage.getTheHoursNTheCountOfTMsForEachWeekDays();
            shiftsForEachDaySecondWeek = scheduleShiftTablePage.getTheContentOfShiftsForEachWeekDay();
            budgetNScheduledHoursSecondWeek = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
            isComplianceCardLoadedSecondWeek = smartCardPage.isSpecificSmartCardLoaded(cardName);
            if (isComplianceCardLoadedFirstWeek) {
                complianceShiftCountSecondWeek = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
            }

            if (hoursNTMsCountFirstWeek.equals(hoursNTMsCountSecondWeek)) {
                SimpleUtils.pass("Verified the scheduled hour and TMs of each week day are consistent with the copied schedule!");
            } else {
                SimpleUtils.fail("Verified the scheduled hour and TMs of each week day are inconsistent with the copied schedule", true);
            }
            if (SimpleUtils.compareHashMapByEntrySet(shiftsForEachDayFirstWeek, shiftsForEachDaySecondWeek)) {
                SimpleUtils.pass("Verified the shifts of each week day are consistent with the copied schedule!");
            } else {
                SimpleUtils.fail("Verified the shifts of each week day are inconsistent with the copied schedule!", true);
            }
            if (budgetNScheduledHoursFirstWeek.get("Scheduled").equals(budgetNScheduledHoursSecondWeek.get("Scheduled"))) {
                SimpleUtils.pass("The Scheduled hour is consistent with the copied scheudle: " + budgetNScheduledHoursFirstWeek.get("Scheduled"));
            } else {
                SimpleUtils.fail("The Scheduled hour is inconsistent, the first week is: " + budgetNScheduledHoursFirstWeek.get("Scheduled")
                        + ", but second week is: " + budgetNScheduledHoursSecondWeek.get("Scheduled"), true);
            }
            if ((isComplianceCardLoadedFirstWeek == isComplianceCardLoadedSecondWeek) && (complianceShiftCountFirstWeek == complianceShiftCountSecondWeek)) {
                SimpleUtils.pass("Verified Compliance is consistent with the copied schedule");
            } else {
                SimpleUtils.fail("Verified Compliance is inconsistent with the copied schedule!", true);
            }


            //Partial copy and select partial work roles

            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            List<String> specificShiftAssigments = new ArrayList<>();
            specificShiftAssigments.add(workRoles.get(0));
            createSchedulePage.createScheduleForNonDGByWeekInfo(firstWeekInfo, weekDaysToClose, specificShiftAssigments);

            hoursNTMsCountSecondWeek = scheduleShiftTablePage.getTheHoursNTheCountOfTMsForEachWeekDays();
            shiftsForEachDaySecondWeek = scheduleShiftTablePage.getTheContentOfShiftsForEachWeekDay();
            budgetNScheduledHoursSecondWeek = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
            isComplianceCardLoadedSecondWeek = smartCardPage.isSpecificSmartCardLoaded(cardName);
            if (isComplianceCardLoadedFirstWeek) {
                complianceShiftCountSecondWeek = smartCardPage.getComplianceShiftCountFromSmartCard(cardName);
            }

            if (hoursNTMsCountFirstWeek.equals(hoursNTMsCountSecondWeek)) {
                SimpleUtils.pass("Verified the scheduled hour and TMs of each week day are consistent with the copied schedule!");
            } else {
                SimpleUtils.fail("Verified the scheduled hour and TMs of each week day are inconsistent with the copied schedule", true);
            }
            if (SimpleUtils.compareHashMapByEntrySet(shiftsForEachDayFirstWeek, shiftsForEachDaySecondWeek)) {
                SimpleUtils.pass("Verified the shifts of each week day are consistent with the copied schedule!");
            } else {
                SimpleUtils.fail("Verified the shifts of each week day are inconsistent with the copied schedule!", true);
            }
            if (budgetNScheduledHoursFirstWeek.get("Scheduled").equals(budgetNScheduledHoursSecondWeek.get("Scheduled"))) {
                SimpleUtils.pass("The Scheduled hour is consistent with the copied scheudle: " + budgetNScheduledHoursFirstWeek.get("Scheduled"));
            } else {
                SimpleUtils.fail("The Scheduled hour is inconsistent, the first week is: " + budgetNScheduledHoursFirstWeek.get("Scheduled")
                        + ", but second week is: " + budgetNScheduledHoursSecondWeek.get("Scheduled"), true);
            }
            if ((isComplianceCardLoadedFirstWeek == isComplianceCardLoadedSecondWeek) && (complianceShiftCountFirstWeek == complianceShiftCountSecondWeek)) {
                SimpleUtils.pass("Verified Compliance is consistent with the copied schedule");
            } else {
                SimpleUtils.fail("Verified Compliance is inconsistent with the copied schedule!", true);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Validate Print Schedule for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validatePrintScheduleForP2PAsInternalAdminP2P (String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeDistrict("Bay Area District");
            locationSelectorPage.changeLocation("LocGroup2");

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            // Verify the LG schedule can be printed and the shift display correctly in print file in week view
            /// Go to one generated schedule
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isActiveWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            /// In week view, print the schedule by click Print button
            SimpleUtils.assertOnFail("Print Icon not loaded Successfully!", scheduleMainPage.isPrintIconLoaded(), false);
            String handle = getDriver().getWindowHandle();
            scheduleMainPage.verifyThePrintFunction();

            /// Get the content in print file
            String downloadPath = parameterMap.get("Download_File_Default_Dir");
            Thread.sleep(5000);
            File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadPath);
            String fileName = latestFile.getName();
            PdfReader reader = new PdfReader(downloadPath + "\\" + fileName);
            String content = PdfTextExtractor.getTextFromPage(reader, 1);

            /// Get scheduled hours and shifts count for one sub location on schedule page
            String subLocation = content.split(" ")[0];
            getDriver().switchTo().window(handle);
            scheduleMainPage.selectLocationFilterByText(subLocation);
            HashMap<String, String> hoursOnSchedule = smartCardPage.getHoursFromSchedulePage();
            int shiftsCount = scheduleShiftTablePage.getShiftsCount();

            /// Compare the data for one sub location in printed file and schedule page
            if (content.contains(hoursOnSchedule.get("Scheduled")) && content.contains("" + shiftsCount)) {
                SimpleUtils.report("The scheduled hours of " + subLocation + " is " + hoursOnSchedule.get("Scheduled") + " Hrs");
                SimpleUtils.report("The shifts count  of " + subLocation + " is " + shiftsCount + " Shifts");
                SimpleUtils.pass("Schedule page: The content in printed file in week view displays correctly");
            } else
                SimpleUtils.fail("Schedule page: The content in printed file in week view displays incorrectly", false);

            // Verify the LG schedule can be printed and the shift display correctly in print file in day view
            /// In day view, print the schedule by click Print button
            scheduleCommonPage.clickOnDayView();
            SimpleUtils.assertOnFail("Print Icon not loaded Successfully!", scheduleMainPage.isPrintIconLoaded(), false);
            scheduleMainPage.verifyThePrintFunction();

            /// Get the content in print file
            downloadPath = parameterMap.get("Download_File_Default_Dir");
            Thread.sleep(5000);
            latestFile = SimpleUtils.getLatestFileFromDirectory(downloadPath);
            fileName = latestFile.getName();
            reader = new PdfReader(downloadPath + "\\" + fileName);
            content = PdfTextExtractor.getTextFromPage(reader, 1);

            /// Get scheduled hours and shifts count for one sub location on schedule page
            subLocation = content.split(" ")[0];
            getDriver().switchTo().window(handle);
            scheduleMainPage.selectLocationFilterByText(subLocation);
            hoursOnSchedule = smartCardPage.getHoursFromSchedulePage();
            shiftsCount = scheduleShiftTablePage.getAvailableShiftsInDayView().size();

            /// Compare the data for one sub location in printed file and schedule page
            if (content.contains(hoursOnSchedule.get("Scheduled")) && content.contains("" + shiftsCount)) {
                SimpleUtils.report("The scheduled hours of " + subLocation + " is " + hoursOnSchedule.get("Scheduled") + " Hrs");
                SimpleUtils.report("The shifts count  of " + subLocation + " is " + shiftsCount + " Shifts");
                SimpleUtils.pass("Schedule page: The content in printed file in day view displays correctly");
            } else
                SimpleUtils.fail("Schedule page: The content in printed file in day view displays incorrectly",false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "Coffee_Enterprise")
    @TestName(description = "Validate UI performance for large roster (500 employees) with one location as well as multiple location groups")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateUIPerformanceForLargeRosterAsInternalAdminP2P (String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeDistrict("Bay Area District");
            locationSelectorPage.changeLocation("LocGroup2");

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            // Verify LG schedule can be generated with large TMs in 2 mins
            /// Generate one schedule with more than 500 TMs
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            // Verify LG schedule can be edited with large TMs in 2 mins
            /// Edit this schedule, save and publish it
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate that operate P2P LG schedule by different user")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateOperateP2PLGScheduleByDifferentUserAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();

            // Verify operate schedule by admin user
            /// Generate schedule
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("Location");
            /// Publish schedule
            if(createSchedulePage.isPublishButtonLoadedOnSchedulePage() || createSchedulePage.isRepublishButtonLoadedOnSchedulePage())
                createSchedulePage.publishActiveSchedule();

            /// Add shifts in schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.addOpenShiftWithDefaultTime(shiftOperatePage.getRandomWorkRole(),childLocationNames.get(0));

            /// Edit shifts(include edit shift time, assign TM, delete...)
            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(0);
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.inputStartOrEndTime("11:00 AM", false);
            editShiftPage.inputStartOrEndTime("08:00 AM", true);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();

            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnAssignmentSelect();
            String assignOrOfferOption = ConsoleEditShiftPage.assignmentOptions.AssignOrOffer.getOption();
            editShiftPage.selectSpecificOptionByText(assignOrOfferOption);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift("a", false);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            /// Republish schedule
            createSchedulePage.publishActiveSchedule();

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Verify operate schedule by SM user
            /// Login as Store Manager
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();

            /// Generate schedule
            isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            /// Publish schedule
            if(createSchedulePage.isPublishButtonLoadedOnSchedulePage() || createSchedulePage.isRepublishButtonLoadedOnSchedulePage())
                createSchedulePage.publishActiveSchedule();

            /// Add shifts in schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.addOpenShiftWithDefaultTime(shiftOperatePage.getRandomWorkRole(),childLocationNames.get(1));

            /// Edit shifts(include edit shift time, assign TM, delete...)
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.inputStartOrEndTime("11:00 AM", false);
            editShiftPage.inputStartOrEndTime("08:00 AM", true);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();

            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnAssignmentSelect();
            editShiftPage.selectSpecificOptionByText(assignOrOfferOption);
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            newShiftPage.searchTeamMemberByNameAndAssignOrOfferShift("a", false);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            /// Republish schedule
            createSchedulePage.publishActiveSchedule();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Budget Hours smart card when SM with Manage Budget permission for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateBudgetHourSmartCardWhenSMWithManageBudgetPermissionForP2PLGAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            //Log in as admin, uncheck the Working Hours Setting Permission to SM.
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
            UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();

            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            userManagementPage.clickOnUserManagementTab();
            SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
            userManagementPage.goToUserAndRoles();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(ScheduleTestKendraScott2.usersAndRolesSubTabs.AccessByJobTitles.getValue());

            String permissionSection = "Schedule";
            String permission = "Manage Budget";
            String actionOff = "off";
            String actionOn = "on";
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission, actionOff);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
            switchToConsoleWindow();
            Thread.sleep(5000);

            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("The Budget Hours smart card is not loaded! ",
                    smartCardPage.isBudgetHoursSmartCardIsLoad(), false);
            loginPage.logOut();

            //Log in as SM
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            int i=0;
            while (i<10 && smartCardPage.isBudgetHoursSmartCardIsLoad()){
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.StoreManager.getValue());
                goToSchedulePageScheduleTab();
                scheduleCommonPage.navigateToNextWeek();
                i++;
            }
            //Check the budget hours smart card on schedule page
            SimpleUtils.assertOnFail("The Budget Hours smart card is not loaded! ",
                    !smartCardPage.isBudgetHoursSmartCardIsLoad(), false);

            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            userManagementPage.clickOnUserManagementTab();
            SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
            userManagementPage.goToUserAndRoles();
            controlsNewUIPage.selectUsersAndRolesSubTabByLabel(ScheduleTestKendraScott2.usersAndRolesSubTabs.AccessByJobTitles.getValue());
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
            controlsNewUIPage.turnOnOrOffSpecificPermissionForSM(permissionSection, permission, actionOn);
            cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Save.getValue());
            switchToConsoleWindow();
            Thread.sleep(5000);

            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("The Budget Hours smart card is not loaded! ",
                    smartCardPage.isBudgetHoursSmartCardIsLoad(), false);
            loginPage.logOut();
            //Log in as SM
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            i=0;
            while (i<10 && !smartCardPage.isBudgetHoursSmartCardIsLoad()){
                Thread.sleep(60000);
                loginPage.logOut();
                loginAsDifferentRole(AccessRoles.StoreManager.getValue());
                goToSchedulePageScheduleTab();
                scheduleCommonPage.navigateToNextWeek();
                i++;
            }
            //Check the budget hours smart card on schedule page
            SimpleUtils.assertOnFail("The Budget Hours smart card is not loaded! ",
                    smartCardPage.isBudgetHoursSmartCardIsLoad(), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify automatically expand when clicking group by on P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAutomaticallyExpandWhenGroupByInP2PLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

        scheduleCommonPage.navigateToNextWeek();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated) {
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        //Group by work role and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
        //Group by job title and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
        //Group by day parts and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyDayParts.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
        //Group by locations and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyLocation.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();

        //Edit-mode
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        //Group by work role and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
        //Group by job title and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
        //Group by day parts and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyDayParts.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
        //Group by locations and check the group.
        scheduleMainPage.selectGroupByFilter(ScheduleTestKendraScott2.scheduleGroupByFilterOptions.groupbyLocation.getValue());
        scheduleShiftTablePage.verifyGroupByTitlesAreExpanded();
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the assign TMs workflow for new create shift UI on P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheAssignTMsWorkFlowForNewCreateShiftUIOnP2PLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            String locationName = scheduleMainPage.selectRandomChildLocationToFilter();
            int shiftCount = scheduleShiftTablePage.getShiftsCount();
            ArrayList<HashMap<String,String>> locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Verify the assign workflow with one shift for one days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(locationName);
            String shiftStartTime = "8:00am";
            String shiftEndTime = "11:00am";
            String totalHrs = "3 Hrs";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 1;
            newShiftPage.selectSpecificWorkDay(dayCount);
            String shiftName = "ShiftNameForBulkCreateShiftUIAuto";
            String shiftNotes = "Shift Notes For Bulk Create Shift UI Auto";
            newShiftPage.setShiftNameOnNewCreateShiftPage(shiftName);
            newShiftPage.setShiftNotesOnNewCreateShiftPage(shiftNotes);
            newShiftPage.clickOnCreateOrNextBtn();
            //Select 3 TMs to assign and click Create button
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            MyThreadLocal.setAssignTMStatus(true);
            String selectedTM1 = newShiftPage.selectTeamMembers();
            newShiftPage.clickOnCreateOrNextBtn();

            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            List<WebElement> shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
            SimpleUtils.assertOnFail("The "+selectedTM1+ "shift is not exist on the first day! ",
                    shiftsOfOneDay.size()==1, false);
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    shiftsOfOneDay.size()==1, false);
            createSchedulePage.publishActiveSchedule();
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    shiftsOfOneDay.size()==1, false);

            String shiftId = shiftsOfOneDay.get(0).getAttribute("id").toString();
            int index = scheduleShiftTablePage.getShiftIndexById(shiftId);
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            String shiftTime = shiftInfo.get(2);
            String workRoleOfNewShift = shiftInfo.get(4);
            String shiftHrs = shiftInfo.get(8);
            String shiftNameOfNewShift = shiftInfo.get(9);
            String shiftNotesOfNewShift = shiftInfo.get(10);
            SimpleUtils.assertOnFail("The new shift's shift time display incorrectly, the expected is:"+shiftStartTime+"-"+shiftEndTime
                            + " the actual is: "+ shiftTime,
                    shiftTime.equalsIgnoreCase(shiftStartTime+"-"+shiftEndTime), false);
            SimpleUtils.assertOnFail("The new shift's work role display incorrectly, the expected is:"+ workRole
                            + " the actual is: "+ workRoleOfNewShift,
                    workRoleOfNewShift.equalsIgnoreCase(workRole), false);
            SimpleUtils.assertOnFail("The new shift's hrs display incorrectly, the expected is:"+ totalHrs
                            + " the actual is: "+ shiftHrs,
                    totalHrs.equalsIgnoreCase(shiftHrs), false);
            SimpleUtils.assertOnFail("The new shift's name display incorrectly, the expected is:"+ shiftName
                            + " the actual is: "+ shiftNameOfNewShift,
                    shiftName.equals(shiftNameOfNewShift), false);
            SimpleUtils.assertOnFail("The new shift's notes display incorrectly, the expected is:"+ shiftNotes
                            + " the actual is: "+ shiftNotesOfNewShift,
                    shiftNotes.equalsIgnoreCase(shiftNotesOfNewShift), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the manuel offer TMs workflow for new create shift UI on P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheManuelOfferTMsWorkFlowForNewCreateShiftUIOnP2PLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            String locationName = scheduleMainPage.selectRandomChildLocationToFilter();
            ArrayList<HashMap<String,String>> locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            int shiftCount = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            String workRole = shiftOperatePage.getRandomWorkRole();

            //Verify the manual offer workflow with one shift for one days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(locationName);
            String shiftStartTime = "8:00am";
            String shiftEndTime = "11:00am";
            String totalHrs = "3 Hrs";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 1;
            newShiftPage.selectSpecificWorkDay(dayCount);
            String shiftName = "ShiftNameForBulkCreateShiftUIAuto";
            String shiftNotes = "Shift Notes For Bulk Create Shift UI Auto";
            newShiftPage.setShiftNameOnNewCreateShiftPage(shiftName);
            newShiftPage.setShiftNotesOnNewCreateShiftPage(shiftNotes);
            newShiftPage.clickOnCreateOrNextBtn();
            //Select 3 TMs to offer and click Create button
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            MyThreadLocal.setAssignTMStatus(false);
            String selectedTM1 = newShiftPage.selectTeamMembers();
            String selectedTM2 = newShiftPage.selectTeamMembers();
            String selectedTM3 = newShiftPage.selectTeamMembers();
            newShiftPage.clickOnCreateOrNextBtn();
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            //There are 1 open shift been created and shift offer will been sent to the multiple TMs
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, "Open").size()==1, false);
            int index = scheduleShiftTablePage.getAddedShiftIndexes("Open").get(0);
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            scheduleShiftTablePage.clickViewStatusBtn();

            SimpleUtils.assertOnFail("The "+selectedTM1+" should display on the offer list! ",
                    !shiftOperatePage.getOfferStatusFromOpenShiftStatusList(selectedTM1).equals(""), false);
            SimpleUtils.assertOnFail("The "+selectedTM2+" should display on the offer list! ",
                    !shiftOperatePage.getOfferStatusFromOpenShiftStatusList(selectedTM2).equals(""), false);
            SimpleUtils.assertOnFail("The "+selectedTM3+" should display on the offer list! ",
                    !shiftOperatePage.getOfferStatusFromOpenShiftStatusList(selectedTM3).equals(""), false);
            shiftOperatePage.closeViewStatusContainer();

            //The work role, shifts time, daily hrs and weekly hrs are display correctly
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            String shiftTime = shiftInfo.get(2);
            String workRoleOfNewShift = shiftInfo.get(4);
            String shiftHrs = shiftInfo.get(6);
            String shiftNameOfNewShift = shiftInfo.get(7);
            String shiftNotesOfNewShift = shiftInfo.get(8);
            SimpleUtils.assertOnFail("The new shift's shift time display incorrectly, the expected is:"+shiftStartTime+"-"+shiftEndTime
                            + " the actual is: "+ shiftTime,
                    shiftTime.equalsIgnoreCase(shiftStartTime+"-"+shiftEndTime), false);
            SimpleUtils.assertOnFail("The new shift's work role display incorrectly, the expected is:"+ workRole
                            + " the actual is: "+ workRoleOfNewShift,
                    workRoleOfNewShift.equalsIgnoreCase(workRole), false);
            SimpleUtils.assertOnFail("The new shift's hrs display incorrectly, the expected is:"+ totalHrs
                            + " the actual is: "+ shiftHrs,
                    totalHrs.equalsIgnoreCase(shiftHrs), false);
            SimpleUtils.assertOnFail("The new shift's name display incorrectly, the expected is:"+ shiftName
                            + " the actual is: "+ shiftNameOfNewShift,
                    shiftName.equals(shiftNameOfNewShift), false);
            SimpleUtils.assertOnFail("The new shift's notes display incorrectly, the expected is:"+ shiftNotes
                            + " the actual is: "+ shiftNotesOfNewShift,
                    shiftNotes.equalsIgnoreCase(shiftNotesOfNewShift), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the auto offer TMs workflow for new create shift UI on P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheAutoOfferTMsWorkFlowForNewCreateShiftUIOnP2PLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            String locationName = scheduleMainPage.selectRandomChildLocationToFilter();
            ArrayList<HashMap<String,String>> locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            int shiftCount = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);

            //Verify the auto offer workflow with one shift for one days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(locationName);
            String shiftStartTime = "11:00am";
            String shiftEndTime = "2:00pm";
            String totalHrs = "3 Hrs";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 1;
            newShiftPage.selectSpecificWorkDay(dayCount);
            String shiftName = "ShiftNameForBulkCreateShiftUIAuto";
            String shiftNotes = "Shift Notes For Bulk Create Shift UI Auto";
            newShiftPage.setShiftNameOnNewCreateShiftPage(shiftName);
            newShiftPage.setShiftNotesOnNewCreateShiftPage(shiftNotes);
            newShiftPage.clickOnCreateOrNextBtn();
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            Thread.sleep(3000);
            scheduleMainPage.saveSchedule();
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    scheduleShiftTablePage.getOneDayShiftByName(0, "Open").size()==1, false);
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getAddedShiftIndexes("Open").get(0));
            String shiftTime = shiftInfo.get(2);
            String workRoleOfNewShift = shiftInfo.get(4);
            String shiftHrs = shiftInfo.get(6);
            String shiftNameOfNewShift = shiftInfo.get(7);
            String shiftNotesOfNewShift = shiftInfo.get(8);
            SimpleUtils.assertOnFail("The new shift's shift time display incorrectly, the expected is:"+shiftStartTime+"-"+shiftEndTime
                            + " the actual is: "+ shiftTime,
                    shiftTime.equalsIgnoreCase(shiftStartTime+"-"+shiftEndTime), false);
            SimpleUtils.assertOnFail("The new shift's work role display incorrectly, the expected is:"+ workRole
                            + " the actual is: "+ workRoleOfNewShift,
                    workRoleOfNewShift.equalsIgnoreCase(workRole), false);
            SimpleUtils.assertOnFail("The new shift's hrs display incorrectly, the expected is:"+ totalHrs
                            + " the actual is: "+ shiftHrs,
                    totalHrs.equalsIgnoreCase(shiftHrs), false);
            SimpleUtils.assertOnFail("The new shift's name display incorrectly, the expected is:"+ shiftName
                            + " the actual is: "+ shiftNameOfNewShift,
                    shiftName.equals(shiftNameOfNewShift), false);
            SimpleUtils.assertOnFail("The new shift's notes display incorrectly, the expected is:"+ shiftNotes
                            + " the actual is: "+ shiftNotesOfNewShift,
                    shiftNotes.equalsIgnoreCase(shiftNotesOfNewShift), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate open shifts will been created when drag and drop shifts to same day and same location on P2P LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkDragAndDropShiftsToSameDayAndSameLocationOnP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectRandomChildLocationToFilter();
            ArrayList<HashMap<String,String>> locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);
            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, false);

            //Two open shifts been created
            List<WebElement> newAddedShifts = scheduleShiftTablePage.getOneDayShiftByName(1, "open");
            SimpleUtils.assertOnFail("The expected new added shifts count is "+selectedShiftCount
                            + " The actual new added shift count is:"+newAddedShifts.size(),
                    newAddedShifts.size()==0, false);

            scheduleMainPage.saveSchedule();
            newAddedShifts = scheduleShiftTablePage.getOneDayShiftByName(1, "open");
            SimpleUtils.assertOnFail("The expected new added shifts count is "+selectedShiftCount
                            + " The actual new added shift count is:"+newAddedShifts.size(),
                    newAddedShifts.size()==2, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify shifts can be moved to same day and another location on p2p LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyMoveShiftsToSameDayAndAnotherLocationOnP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.expandSpecificCountGroup(2);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);
            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }

            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);

            //Select move option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("move");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore && oneDayShiftsCountAfter == oneDayShiftsCountBefore, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore && oneDayShiftsCountAfter == oneDayShiftsCountBefore, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Verify changes can be published
            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore && oneDayShiftsCountAfter == oneDayShiftsCountBefore, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Check the shifts are not display on first location
            scheduleShiftTablePage.expandSpecificCountGroup(1);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of peer locations in the Schedule Overview page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheFunctionalityOfPeerLocationsInTheScheduleOverviewPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            boolean isLocationUsingControlsConfiguration = controlsNewUIPage.checkIfTheLocationUsingControlsConfiguration();
            String isBudgetEnabled = "";
            //Check the budget is enabled or not
            if (isLocationUsingControlsConfiguration) {
                controlsNewUIPage.clickOnControlsConsoleMenu();
                controlsNewUIPage.clickOnControlsSchedulingPolicies();
                Thread.sleep(10000);
                isBudgetEnabled = controlsNewUIPage.getApplyLaborBudgetToSchedulesActiveBtnLabel();
            } else {
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToGlobalConfigurationInLocations();
                Thread.sleep(10000);
                isBudgetEnabled = controlsNewUIPage.getApplyLaborBudgetToSchedulesActiveBtnLabel();
                switchToConsoleWindow();
            }
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            List<String> locationNames = scheduleMainPage.getSpecificFilterNames("location");
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //Check that the peer locations should be listed
            for (String name : locationNames) {
                scheduleDMViewPage.getAllScheduleInfoFromScheduleInDMViewByLocation(name);
            }

            //Corresponding peer locations should be loaded according to the search strings
            for (String name : locationNames) {
                scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(name);
            }

            //Verify the columns of the location list
            scheduleDMViewPage.verifyP2PSchedulesTableHeaderNames(isBudgetEnabled.equalsIgnoreCase("Yes"));

            //Verify the Not Started status when the peer location schedule has not been created yet
            String peerLocation = locationNames.get(new Random().nextInt(locationNames.size()));
            scheduleDMViewPage.clickOnLocationNameInDMView(peerLocation);
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            Thread.sleep(3000);
            scheduleDMViewPage.clickOnRefreshButton();
            Thread.sleep(3000);
            String publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(peerLocation)
                    .get("publishedStatus");
            SimpleUtils.assertOnFail("The schedule status should be Not Started, but actual is:"+publishStatus,
                    publishStatus.equalsIgnoreCase("Not Started"), false);

            //Verify the In Progress status when the peer location schedule is created but was never published
            Thread.sleep(3000);
            scheduleDMViewPage.clickOnLocationNameInDMView(peerLocation);
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleDMViewPage.clickOnRefreshButton();
            publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(peerLocation)
                    .get("publishedStatus");
            SimpleUtils.assertOnFail("The schedule status should be In progress, but actual is:"+publishStatus,
                    publishStatus.equalsIgnoreCase("In Progress"), false);

            //Verify the Published status when the peer location schedule has been published
            Thread.sleep(3000);
            scheduleDMViewPage.clickOnLocationNameInDMView(peerLocation);
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            if (createSchedulePage.isCurrentScheduleWeekPublished()){
                SimpleUtils.pass("The schedule has been published successfully! ");
            }else
                SimpleUtils.fail("The schedule fail to publish! ", false);
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            Thread.sleep(3000);
            scheduleDMViewPage.clickOnRefreshButton();
            Thread.sleep(3000);
            publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(peerLocation)
                    .get("publishedStatus");
            SimpleUtils.assertOnFail("The schedule status should be Published, but actual is:"+publishStatus,
                    publishStatus.equalsIgnoreCase("Published"), false);

            //Verify the Published status when the peer location schedule has been updated after the publish can be republished
            scheduleDMViewPage.clickOnLocationNameInDMView(peerLocation);
            Thread.sleep(3000);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            Thread.sleep(8000);
            newShiftPage.selectWorkRole(workRole);
            String shiftStartTime = "9:00am";
            String shiftEndTime = "2:00pm";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.selectTeamMembers();
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            Thread.sleep(3000);
            scheduleDMViewPage.clickOnRefreshButton();
            Thread.sleep(3000);
            publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(peerLocation)
                    .get("publishedStatus");
            SimpleUtils.assertOnFail("The schedule status should be Published, but actual is:"+publishStatus,
                    publishStatus.equalsIgnoreCase("Published"), false);

            //Verify the functionality of "View Group Schedule" button
            Map<String, String> weekInfoBeforeClick = scheduleCommonPage.getActiveDayInfo();
            scheduleOverviewPage.clickOnViewGroupScheduleButton();
            SimpleUtils.assertOnFail("The schedule main page should be loaded! ",
                    scheduleMainPage.isScheduleMainPageLoaded(), false);
            Map<String, String> weekInfoAfterClick = scheduleCommonPage.getActiveDayInfo();
            //Verify the correct week is shown
            SimpleUtils.assertOnFail("The week info before click is: "+weekInfoBeforeClick+
                            " The week info after click is: "+weekInfoAfterClick,
                    weekInfoAfterClick.equals(weekInfoBeforeClick), false);

            //Verify can navigate back to overview page when click the back button of the browser
            MyThreadLocal.getDriver().navigate().back();
            SimpleUtils.assertOnFail("The P2P overview page should display! ",
                    scheduleDMViewPage.isScheduleDMView(), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Ting")
    @Enterprise(name = "")
    @TestName(description = "Verify copy or move shifts to sub-locations in same day using location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCopyOrMoveShiftsToSubLocationsInSameDayUsingLocationGroupAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();

            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.expandSpecificCountGroup(2);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            //Get shift count before drag and drop
            int allShiftsCountBeforeForCopy = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountBeforeForCopy = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);

            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 1;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);
            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount; i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }

            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);

            //Select copy option
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("Copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfterForCopy = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountAfterForCopy = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: " + allShiftsCountBeforeForCopy + " and " + oneDayShiftsCountBeforeForCopy
                            + ", but the actual are: " + allShiftsCountAfterForCopy + " and " + oneDayShiftsCountAfterForCopy,
                    allShiftsCountAfterForCopy - allShiftsCountBeforeForCopy == 1 && oneDayShiftsCountAfterForCopy - oneDayShiftsCountBeforeForCopy == 1, false);
            for (int i=0; i< selectedShiftCount; i++) {
                SimpleUtils.assertOnFail("Drag and drop: the shift failed to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() > 0, false);
            }
            SimpleUtils.assertOnFail("An open shift should be created!", scheduleShiftTablePage.getAllShiftsOfOneTM("open").size() == 1, false);

            /*
            // TODO: Can't be saved due to repetitive shifts created instead of converting it to an open shift
            // Issue has been raised: https://legiontech.atlassian.net/browse/SCH-7077
            */

            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfterForCopy = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfterForCopy = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: " + allShiftsCountBeforeForCopy + " and "+ oneDayShiftsCountBeforeForCopy
                            + ", but the actual are: " + allShiftsCountAfterForCopy + " and "+ oneDayShiftsCountAfterForCopy,
                    allShiftsCountAfterForCopy - allShiftsCountBeforeForCopy == 1 && oneDayShiftsCountAfterForCopy - oneDayShiftsCountBeforeForCopy == 1, false);
            for (int i=0; i< selectedShiftCount; i++) {
                SimpleUtils.assertOnFail("Drag and drop: the shift failed to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() > 0, false);
            }
            SimpleUtils.assertOnFail("An open shift should be created!", scheduleShiftTablePage.getAllShiftsOfOneTM("open").size() == 1, false);

            // Edit schedule
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.expandSpecificCountGroup(2);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            //Get shift count before drag and drop
            int allShiftsCountBeforeForMove = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountBeforeForMove = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);

            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);

            //Select move option
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("Move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfterForMove = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountAfterForMove = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: "+ allShiftsCountBeforeForMove + " and "+ oneDayShiftsCountBeforeForMove
                            + ", but the actual are: "+allShiftsCountAfterForMove + " and "+ oneDayShiftsCountAfterForMove,
                    allShiftsCountAfterForMove == allShiftsCountBeforeForMove && oneDayShiftsCountAfterForMove == oneDayShiftsCountBeforeForMove, false);
            for (int i=0; i< selectedShiftCount; i++) {
                SimpleUtils.assertOnFail("Drag and drop: the shift failed to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() > 0, false);
            }
            SimpleUtils.assertOnFail("An open shift should be there!", scheduleShiftTablePage.getAllShiftsOfOneTM("open").size() == 1, false);

            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfterForMove = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfterForMove = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: " + allShiftsCountBeforeForMove + " and " + oneDayShiftsCountBeforeForMove
                            + ", but the actual are: "+allShiftsCountAfterForMove + " and "+ oneDayShiftsCountAfterForMove,
                    allShiftsCountAfterForMove == allShiftsCountBeforeForMove && oneDayShiftsCountAfterForMove == oneDayShiftsCountBeforeForMove, false);
            for (int i=0; i< selectedShiftCount; i++) {
                SimpleUtils.assertOnFail("Drag and drop: the shift failed to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size() > 0, false);
            }
            SimpleUtils.assertOnFail("An open shift should be there!", scheduleShiftTablePage.getAllShiftsOfOneTM("open").size() == 1, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify can create the schedule for all peer locations")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCanCreateScheduleForAllPeerLocationsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> locationNames = scheduleMainPage.getSpecificFilterNames("location");
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyAll.getValue());
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.publishOrRepublishSchedule();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //Check that the peer locations should be listed
            for (String name : locationNames) {
                scheduleDMViewPage.getAllScheduleInfoFromScheduleInDMViewByLocation(name);
                scheduleDMViewPage.clickOnRefreshButton();
//                scheduleDMViewPage.clickOnRefreshButton();
                String publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(name)
                        .get("publishedStatus");
                SimpleUtils.assertOnFail("The "+name+" schedule status should be Published, but actual is:"+publishStatus,
                        publishStatus.equalsIgnoreCase("Published"), false);
            }
            scheduleOverviewPage.clickOnViewGroupScheduleButton();
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.verifyGroupCanbeCollapsedNExpanded();

            //Verify the value on LOCATION GROUP smart card when schedule is published
            String messageOnSmartCard = smartCardPage.getsmartCardTextByLabel("LOCATION GROUP").replace("\n", " ");
            String expectedMessage1 = "0 Not Started";
            String expectedMessage2 = "0 In Progress";
            String expectedMessage3 = locationNames.size()+" Published";
            String expectedMessage4 = locationNames.size()+" Total Locations";
            SimpleUtils.assertOnFail("The expected message is: "+expectedMessage1
                            + expectedMessage2+ " "
                            + expectedMessage3+ " "
                            + expectedMessage4+ " The actual message is: "+messageOnSmartCard,
                    messageOnSmartCard.contains(expectedMessage1)
                            && messageOnSmartCard.contains(expectedMessage2)
                            && messageOnSmartCard.contains(expectedMessage3)
                            && messageOnSmartCard.contains(expectedMessage4), false);

            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            //Check that the peer locations should be listed
            for (String name : locationNames) {
                scheduleDMViewPage.getAllScheduleInfoFromScheduleInDMViewByLocation(name);
                scheduleDMViewPage.clickOnRefreshButton();
                String publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(name)
                        .get("publishedStatus");
                SimpleUtils.assertOnFail("The schedule status should be Published, but actual is:"+publishStatus,
                        publishStatus.equalsIgnoreCase("Not Started"), false);
            }
            scheduleOverviewPage.clickOnViewGroupScheduleButton();
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.verifyGroupCannotbeCollapsedNExpanded();

            //Verify the value on LOCATION GROUP smart card when schedule is published
            messageOnSmartCard = smartCardPage.getsmartCardTextByLabel("LOCATION GROUP").replace("\n", " ");
            expectedMessage1 = locationNames.size()+" Not Started";
            expectedMessage2 = "0 In Progress";
            expectedMessage3 = "0 Published";
            expectedMessage4 = locationNames.size()+" Total Locations";
            SimpleUtils.assertOnFail("The expected message is: "+expectedMessage1
                            + expectedMessage2+ " "
                            + expectedMessage3+ " "
                            + expectedMessage4+ " The actual message is: "+messageOnSmartCard,
                    messageOnSmartCard.contains(expectedMessage1)
                            && messageOnSmartCard.contains(expectedMessage2)
                            && messageOnSmartCard.contains(expectedMessage3)
                            && messageOnSmartCard.contains(expectedMessage4), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the actions for each peer locations in different status")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheActionForEachPeerLocationsInDifferentStatusAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            //Delete the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String locationName = scheduleMainPage.getSpecificFilterNames("location").get(0);
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            //The status is changed to Not Started
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            String status = scheduleShiftTablePage.getSpecificGroupByChildLocationStatus(locationName);
            SimpleUtils.assertOnFail("The expected status is Not Started, the actual status is: "+status,
                    status.equalsIgnoreCase("Not Started"), false);
            scheduleShiftTablePage.clickActionIconForSpecificGroupByChildLocation(locationName);

            //Verify "Edit Operating Hours" action when peer location is Not Started
            String editOperatingHoursButton = "Edit Operating Hours";
            String deleteButton = "Delete Schedule";
            String createScheduleButton = "Create Schedule";
            String publishScheduleButton = "Publish Schedule";
            String republishButton = "Republish Schedule";
            List<String> buttonsFromPopup = scheduleShiftTablePage.getButtonNamesFromGroupByActionPopup();
            SimpleUtils.assertOnFail("The buttons on group by location action popup display incorrectly! The expected is"
                    + createScheduleButton + " button and "+ editOperatingHoursButton + " button."
                    +" The actual is"+buttonsFromPopup,
                    buttonsFromPopup.contains(editOperatingHoursButton)
                            && buttonsFromPopup.contains(createScheduleButton), false);
            scheduleShiftTablePage.clickOnSpecificButtonsGroupByActionPopup(editOperatingHoursButton);
            newShiftPage.customizeNewShiftPage();
            newShiftPage.closeNewCreateShiftPage();

            //Verify "Create Schedule" action when peer location is Not Started
            scheduleShiftTablePage.clickActionIconForSpecificGroupByChildLocation(locationName);
            scheduleShiftTablePage.clickOnSpecificButtonsGroupByActionPopup(createScheduleButton);
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();
            Thread.sleep(3000);
            if (createSchedulePage.isCopyScheduleWindow()) {
                createSchedulePage.selectWhichWeekToCopyFrom("SUGGESTED");
                createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();
            }
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyAll.getValue());
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
                scheduleMainPage.saveSchedule();
                scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            }

            //The status is changed to In Progress
            status = scheduleShiftTablePage.getSpecificGroupByChildLocationStatus(locationName);
            SimpleUtils.assertOnFail("The expected status is In Progress, the actual status is: "+status,
                    status.equalsIgnoreCase("In Progress"), false);

            //Verify "Edit Operating Hours" action when peer location is In Progress
            scheduleShiftTablePage.clickActionIconForSpecificGroupByChildLocation(locationName);
            buttonsFromPopup = scheduleShiftTablePage.getButtonNamesFromGroupByActionPopup();
            SimpleUtils.assertOnFail("The buttons on group by location action popup display incorrectly! The expected is"
                            + editOperatingHoursButton + " button and "+ publishScheduleButton + " button and "+ deleteButton + " button."
                            +" The actual is"+buttonsFromPopup,
                    buttonsFromPopup.contains(editOperatingHoursButton)
                            && buttonsFromPopup.contains(publishScheduleButton)
                            && buttonsFromPopup.contains(deleteButton), false);
            scheduleShiftTablePage.clickOnSpecificButtonsGroupByActionPopup(editOperatingHoursButton);
            newShiftPage.customizeNewShiftPage();
            newShiftPage.closeNewCreateShiftPage();

            //Verify "Publish" action when peer location is In Progress
            scheduleShiftTablePage.clickActionIconForSpecificGroupByChildLocation(locationName);
            scheduleShiftTablePage.clickOnSpecificButtonsGroupByActionPopup(publishScheduleButton);
            scheduleShiftTablePage.clickOnOkButtonInWarningMode();
            CompliancePage compliancePage = new ConsoleCompliancePage();
            compliancePage.displaySuccessMessage();
            //The status is changed to Published
            status = scheduleShiftTablePage.getSpecificGroupByChildLocationStatus(locationName);
            SimpleUtils.assertOnFail("The expected status is Published, the actual status is: "+status,
                    status.equalsIgnoreCase("Published"), false);

            //Verify the actions when peer locations has unpublished changes
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(locationName);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.selectTeamMembers();
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            scheduleShiftTablePage.clickActionIconForSpecificGroupByChildLocation(locationName);
            buttonsFromPopup = scheduleShiftTablePage.getButtonNamesFromGroupByActionPopup();
            SimpleUtils.assertOnFail("The buttons on group by location action popup display incorrectly! The expected is"
                            + editOperatingHoursButton + " button and "+ republishButton + " button and "+ deleteButton + " button."
                            +" The actual is"+buttonsFromPopup,
                    buttonsFromPopup.contains(editOperatingHoursButton)
                            && buttonsFromPopup.contains(republishButton)
                            && buttonsFromPopup.contains(deleteButton), false);
            scheduleShiftTablePage.clickOnSpecificButtonsGroupByActionPopup(republishButton);
            scheduleShiftTablePage.clickOnOkButtonInWarningMode();
            Thread.sleep(15000);
            //The status is changed to Published
            status = scheduleShiftTablePage.getSpecificGroupByChildLocationStatus(locationName);
            SimpleUtils.assertOnFail("The expected status is Published, the actual status is: "+status,
                    status.equalsIgnoreCase("Published"), false);

            //Verify "Delete" when peer location is Published
            scheduleShiftTablePage.clickActionIconForSpecificGroupByChildLocation(locationName);
            buttonsFromPopup = scheduleShiftTablePage.getButtonNamesFromGroupByActionPopup();
            SimpleUtils.assertOnFail("The buttons on group by location action popup display incorrectly! The expected is"
                            + deleteButton + " button and "+ editOperatingHoursButton + " button."
                            +" The actual is"+buttonsFromPopup,
                    buttonsFromPopup.contains(editOperatingHoursButton)
                            && buttonsFromPopup.contains(deleteButton), false);
            scheduleShiftTablePage.clickOnSpecificButtonsGroupByActionPopup(deleteButton);
            createSchedulePage.confirmDeleteSchedule();
            Thread.sleep(15000);
            //The status is changed to Not Started
            status = scheduleShiftTablePage.getSpecificGroupByChildLocationStatus(locationName);
            SimpleUtils.assertOnFail("The expected status is Not Started, the actual status is: "+status,
                    status.equalsIgnoreCase("Not Started"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the status of P2P in DM view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheStatusOfP2PInDMViewsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            //Verify Not Started will show if all the peer locations are Not Started
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get("District");
            locationSelectorPage.changeUpperFieldDirect("District", districtName);
            scheduleDMViewPage.clickOnRefreshButton();
            String publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(location)
                    .get("publishedStatus");
            SimpleUtils.assertOnFail("The schedule status should be Not Started, but actual is:"+publishStatus,
                    publishStatus.equalsIgnoreCase("Not Started"), false);
            //Verify In Progress will show if some peer locations are Not Started, some are In Progress
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            locationSelectorPage.changeUpperFieldDirect("District", districtName);
            scheduleDMViewPage.clickOnRefreshButton();
            publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(location)
                    .get("publishedStatus");
            SimpleUtils.assertOnFail("The schedule status should be In Progress, but actual is:"+publishStatus,
                    publishStatus.equalsIgnoreCase("In Progress"), false);
            //Verify Published will show if all the peer locations are published
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            locationSelectorPage.changeUpperFieldDirect("District", districtName);
            scheduleDMViewPage.clickOnRefreshButton();
            publishStatus = scheduleDMViewPage.getAllUpperFieldInfoFromScheduleByUpperField(location)
                    .get("publishedStatus");
            SimpleUtils.assertOnFail("The schedule status should be Published, but actual is:"+publishStatus,
                    publishStatus.equalsIgnoreCase("Published"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify data match in District Summary widget on Dashboard in Region View when it includes LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDataMatchInDistrictSummaryWidgetOnDashboardInRegionViewWhenItIncludesLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            //Get Budegeted Hrs/Scheduled Hrs/Projected Hrs/Budget Variance in location summary widget from every district
            List<String> districtNames = locationSelectorPage.getAllUpperFieldNamesInUpperFieldDropdownList(District);
            float sumBudgetedHrs = 0;
            float sumScheduledHrs = 0;
            float sumProjectedHrs = 0;
            float sumBudgetVarianceHrs = 0;
            for (String districtName : districtNames) {
                locationSelectorPage.changeUpperFieldDirect(District, districtName);
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                dashboardPage.clickOnDashboardConsoleMenu();
                List<String> dataOnLocationSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
                sumBudgetedHrs += Float.parseFloat(dataOnLocationSummaryWidget.get(0));
                sumScheduledHrs += Float.parseFloat(dataOnLocationSummaryWidget.get(1));
                sumProjectedHrs += Float.parseFloat(dataOnLocationSummaryWidget.get(2));
                if (dataOnLocationSummaryWidget.get(6).equalsIgnoreCase("under")){
                    sumBudgetVarianceHrs = sumBudgetVarianceHrs - Float.parseFloat(dataOnLocationSummaryWidget.get(5));
                }else
                    sumBudgetVarianceHrs = sumBudgetVarianceHrs + Float.parseFloat(dataOnLocationSummaryWidget.get(5));
            }

            //            dashboardPage.clickOnRefreshButtonOnSMDashboard();
            //Get Budegeted Hrs/Scheduled Hrs/Projected Hrs/Budget Variance in District summary widget
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String regionName = selectedUpperFields.get(Region);
            locationSelectorPage.changeUpperFieldDirect(Region, regionName);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            Thread.sleep(5000);
            dashboardPage.clickOnDashboardConsoleMenu();
            dashboardPage.clickOnRefreshButton();
            List<String> dataOnDistrictSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            float budgetedHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(0));
            float scheduledHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(1));
            float projectedHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(2));
            float budgetVarianceHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(5));

            //Compare the value of Projected Hrs in region view with the sum of Projected Hrs in DM view
            SimpleUtils.assertOnFail("The budgeted hrs on Region View is: "+budgetedHrs
                            +" The sum of budget hrs on District View is: "+sumBudgetedHrs,
                    budgetedHrs == sumBudgetedHrs, false);

            SimpleUtils.assertOnFail("The scheduled Hrs on Region View is: "+scheduledHrs
                            +" The sum of scheduled Hrs on District View is: "+sumScheduledHrs,
                    (scheduledHrs - sumScheduledHrs)< 0.1, false);

            SimpleUtils.assertOnFail("The projected Hrs on Region View is: "+projectedHrs
                            +" The sum of projected Hrs on District View is: "+sumProjectedHrs,
                    projectedHrs == sumProjectedHrs, false);

            SimpleUtils.assertOnFail("The budget Variance Hrs on Region View is: "+budgetVarianceHrs
                            +" The sum of budget Variance Hrs on District View is: "+Math.abs(sumBudgetVarianceHrs),
                    budgetVarianceHrs == Math.abs(sumBudgetVarianceHrs), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify data match in Location Summary widget on Dashboard in DM View when it includes LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDataMatchInLocationSummaryWidgetOnDashboardInDMViewWhenItIncludesLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> subLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            dashboardPage.clickOnDashboardConsoleMenu();
            dashboardPage.clickOnRefreshButtonOnSMDashboard();
            //Get Budegeted Hrs/Scheduled Hrs/Projected Hrs/Budget Variance location count in District summary widget
            Map<String, String> selectedUpperFields = locationSelectorPage.getSelectedUpperFields();
            String districtName = selectedUpperFields.get(District);
            locationSelectorPage.changeUpperFieldDirect(District, districtName);
            List<String> dataOnDistrictSummaryWidget = dashboardPage.getTheDataOnLocationSummaryWidget();
            float budgetedHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(0));
            float scheduledHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(1));
            float projectedHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(2));
            int locationsCount = Integer.parseInt(dataOnDistrictSummaryWidget.get(3).split(" ")[0])+Integer.parseInt(dataOnDistrictSummaryWidget.get(4).split(" ")[0]);
            float budgetVarianceHrs = Float.parseFloat(dataOnDistrictSummaryWidget.get(5));

            //Click on All Locations to get the location number (excluding peer child location) to compare with the number in Location Summary widget
            List<String> locationNames = locationSelectorPage.getAllUpperFieldNamesInUpperFieldDropdownList(Location);
            SimpleUtils.assertOnFail("The location count on District Summary widget is: "+locationsCount
                            + " The actual location count on upperfield dropdown list is: "
                            +(locationNames.size()-subLocationNames.size()),
                    locationsCount == (locationNames.size()-subLocationNames.size()), false);

            //Click on View Schedules link in Location Summary widget
            dashboardPage.clickOnViewSchedulesOnOrgSummaryWidget();

            //Get Budegeted Hrs/Scheduled Hrs/Projected Hrs/Budget Variance location count in District summary smart card
            scheduleDMViewPage.clickOnRefreshButton();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            Thread.sleep(5000);
            List<String> textOnTheChartInRegionSummarySmartCard= scheduleDMViewPage.getTextFromTheChartInLocationSummarySmartCard();
            float budgetedHrsOnLocationSummarySmartCard = Float.parseFloat(textOnTheChartInRegionSummarySmartCard.get(0));
            float scheduledHrsOnLocationSummarySmartCard = Float.parseFloat(textOnTheChartInRegionSummarySmartCard.get(2));
            float projectedHrsOnLocationSummarySmartCard = Float.parseFloat(textOnTheChartInRegionSummarySmartCard.get(4));
            float budgetVarianceHrsOnLocationSummarySmartCard = Float.parseFloat(textOnTheChartInRegionSummarySmartCard.get(6));

            //Compare the value of Projected Hrs in region view with the sum of Projected Hrs in DM view
            SimpleUtils.assertOnFail("The budgeted hrs on DM dashboard View is: "+budgetedHrs
                            +" The sum of budget hrs on DM schedule View is: "+budgetedHrsOnLocationSummarySmartCard,
                    budgetedHrs == budgetedHrsOnLocationSummarySmartCard, false);

            SimpleUtils.assertOnFail("The scheduled Hrs on DM dashboard View is: "+scheduledHrs
                            +" The sum of scheduled Hrs on DM schedule View is: "+scheduledHrsOnLocationSummarySmartCard,
                    scheduledHrs == scheduledHrsOnLocationSummarySmartCard, false);

            SimpleUtils.assertOnFail("The projected Hrs on DM dashboard View is: "+projectedHrs
                            +" The sum of projected Hrs on DM schedule View is: "+projectedHrsOnLocationSummarySmartCard,
                    projectedHrs == projectedHrsOnLocationSummarySmartCard, false);

            SimpleUtils.assertOnFail("The budget Variance Hrs on DM dashboard View is: "+budgetVarianceHrs
                            +" The sum of budget Variance Hrs on DM schedule View is: "+budgetVarianceHrsOnLocationSummarySmartCard,
                    budgetVarianceHrs == budgetVarianceHrsOnLocationSummarySmartCard, false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate analytics table on Schedule in DM View when it includes LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnScheduleInDMViewWhenItIncludesLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
//            scheduleMainPage.publishOrRepublishSchedule();
            scheduleMainPage.clickOnFilterBtn();
            List<String> subLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            locationSelectorPage.selectCurrentUpperFieldAgain(District);
            scheduleDMViewPage.clickOnRefreshButton();
            locationSelectorPage.refreshTheBrowser();
            //P2P children location should not appear along with parent. so only LG’s parents would appear on the DM view.
            SimpleUtils.assertOnFail("The parent location: "+location+" should display in the analytics table on schedule DM view! ",
                    scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(location), false);

            for (String subLocation:subLocationNames) {
                SimpleUtils.assertOnFail("The sub location: "+subLocation+" should not display in the analytics table on schedule DM view! ",
                        !scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(subLocation), false);
            }

            //Get parent location data on Schedule DM view
            Map<String, String> locationInfoOnDMView =
                    scheduleDMViewPage.getAllScheduleInfoFromScheduleInDMViewByLocation(location);
            float budgetHourOnDMView = Float.parseFloat(locationInfoOnDMView.get("budgetedHours"));
            float publishedOnDMView = Float.parseFloat(locationInfoOnDMView.get("publishedHours"));

            //Analytics table should match the current week's data
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(location);
//            Thread.sleep(5000);
            HashMap<String, Float> scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            float budgetHourOnSchedulePage = scheduleHoursForFirstSchedule.get("budgetedHours");
            float publishedOnSchedulePage = scheduleHoursForFirstSchedule.get("scheduledHours");
            SimpleUtils.assertOnFail("The budget hours on DM view is: "+budgetHourOnDMView
                            + " The budget hours on DM view is: "+budgetHourOnSchedulePage,
                    budgetHourOnDMView == budgetHourOnSchedulePage,false);
            SimpleUtils.assertOnFail("The published hours on DM view is: "+publishedOnDMView
                            + " The published hours on DM view is: "+publishedOnSchedulePage,
                    publishedOnDMView == publishedOnSchedulePage,false);
            //Go to past week
            scheduleCommonPage.navigateToPreviousWeek();
            isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
//            scheduleMainPage.publishOrRepublishSchedule();
            locationSelectorPage.selectCurrentUpperFieldAgain(District);

            //P2P children location should not appear along with parent. so only LG’s parents would appear on the DM view.
            SimpleUtils.assertOnFail("The parent location: "+location+" should display in the analytics table on schedule DM view! ",
                    scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(location), false);

            for (String subLocation:subLocationNames) {
                SimpleUtils.assertOnFail("The sub location: "+subLocation+" should not display in the analytics table on schedule DM view! ",
                        !scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(subLocation), false);
            }

            //Get parent location data on Schedule DM view
            locationInfoOnDMView =
                    scheduleDMViewPage.getAllScheduleInfoFromScheduleInDMViewByLocation(location);
            budgetHourOnDMView = Float.parseFloat(locationInfoOnDMView.get("budgetedHours"));
            publishedOnDMView = Float.parseFloat(locationInfoOnDMView.get("publishedHours"));

            //Analytics table should match the current week's data
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(location);
            scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            budgetHourOnSchedulePage = scheduleHoursForFirstSchedule.get("budgetedHours");
            publishedOnSchedulePage = scheduleHoursForFirstSchedule.get("scheduledHours");
            SimpleUtils.assertOnFail("The budget hours on DM view is: "+budgetHourOnDMView
                            + " The budget hours on DM view is: "+budgetHourOnSchedulePage,
                    budgetHourOnDMView == budgetHourOnSchedulePage,false);
            SimpleUtils.assertOnFail("The published hours on DM view is: "+publishedOnDMView
                            + " The published hours on DM view is: "+publishedOnSchedulePage,
                    publishedOnDMView == publishedOnSchedulePage,false);

            //Go to future week
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
//            scheduleMainPage.publishOrRepublishSchedule();
            locationSelectorPage.selectCurrentUpperFieldAgain(District);
            //P2P children location should not appear along with parent. so only LG’s parents would appear on the DM view.
            SimpleUtils.assertOnFail("The parent location: "+location+" should display in the analytics table on schedule DM view! ",
                    scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(location), false);

            for (String subLocation:subLocationNames) {
                SimpleUtils.assertOnFail("The sub location: "+subLocation+" should not display in the analytics table on schedule DM view! ",
                        !scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(subLocation), false);
            }

            //Get parent location data on Schedule DM view
            locationInfoOnDMView =
                    scheduleDMViewPage.getAllScheduleInfoFromScheduleInDMViewByLocation(location);
            budgetHourOnDMView = Float.parseFloat(locationInfoOnDMView.get("budgetedHours"));
            publishedOnDMView = Float.parseFloat(locationInfoOnDMView.get("publishedHours"));

            //Analytics table should match the current week's data
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(location);
            scheduleHoursForFirstSchedule = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            budgetHourOnSchedulePage = scheduleHoursForFirstSchedule.get("budgetedHours");
            publishedOnSchedulePage = scheduleHoursForFirstSchedule.get("scheduledHours");
            SimpleUtils.assertOnFail("The budget hours on DM view is: "+budgetHourOnDMView
                            + " The budget hours on DM view is: "+budgetHourOnSchedulePage,
                    budgetHourOnDMView == budgetHourOnSchedulePage,false);
            SimpleUtils.assertOnFail("The published hours on DM view is: "+publishedOnDMView
                            + " The published hours on DM view is: "+publishedOnSchedulePage,
                    publishedOnDMView == publishedOnSchedulePage,false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate analytics table on Compliance in DM View when it includes LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnComplianceInDMViewWhenItIncludesLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> subLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            String districtName = locationSelectorPage.getSelectedUpperFields().get(District);
            locationSelectorPage.selectCurrentUpperFieldAgain(District);
            timeSheetPage.clickOnComplianceConsoleMenu();
            //P2P children location should not appear along with parent. so only LG’s parents would appear on the DM view.
            SimpleUtils.assertOnFail("The parent location: "+location+" should display in the analytics table on schedule DM view! ",
                    scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(location), false);

            for (String subLocation:subLocationNames) {
                SimpleUtils.assertOnFail("The sub location: "+subLocation+" should not display in the analytics table on schedule DM view! ",
                        !scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(subLocation), false);
            }

            // Validate the data of analytics table for past week.
            compliancePage.navigateToPreviousWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for past week successfully",compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInDMForPast = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String totalExtraHoursInDMView = dataInDMForPast.get(0);

            dashboardPage.navigateToDashboard();
            locationSelectorPage.changeLocation(location);
            List<String> dataOnComplianceViolationWidgetInSMDashboard = liquidDashboardPage.getDataOnComplianceViolationWidget();
            String totalHrsInSMForPast = dataOnComplianceViolationWidgetInSMDashboard.get(3);
            SimpleUtils.report("Total Extra Hours In DM View for past week is "+totalExtraHoursInDMView);
            SimpleUtils.report("Total Extra Hours In SM View for past week is "+totalHrsInSMForPast);
            if(totalHrsInSMForPast.equals(String.valueOf(Math.round(Float.parseFloat(totalExtraHoursInDMView)))))
                SimpleUtils.pass("Compliance Page: Analytics table matches the past week's data");
            else
                SimpleUtils.fail("Compliance Page: Analytics table doesn't match the past week's data",false);

            // Validate the data of analytics table for current week.
            String totalHrsInSMForCurrent = dataOnComplianceViolationWidgetInSMDashboard.get(3);
            locationSelectorPage.reSelectDistrict(districtName);
            compliancePage.clickOnComplianceConsoleMenu();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for current week successfully",compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInDMForCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String totalExtraHoursInDMViewForCurrent = dataInDMForCurrent.get(0);
            SimpleUtils.report("Total Extra Hours In DM View for current week is " + totalExtraHoursInDMViewForCurrent);
            SimpleUtils.report("Total Extra Hours In SM View for current week is " + totalHrsInSMForCurrent);
            if(totalHrsInSMForCurrent.equals(String.valueOf(Math.round(Float.parseFloat((totalExtraHoursInDMViewForCurrent))))))
                SimpleUtils.pass("Compliance Page: Analytics table matches the current week's data");
            else
                SimpleUtils.fail("Compliance Page: Analytics table doesn't match the current week's data",false);

            // Validate the data of analytics table for future week
            compliancePage.navigateToNextWeek();
            SimpleUtils.assertOnFail("Compliance page analytics table not loaded for future week successfully",compliancePage.isComplianceUpperFieldView(), false);
            List<String> dataInDMForFuture = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String totalExtraHoursInDMViewForFuture = dataInDMForFuture.get(0);
            SimpleUtils.report("Total Extra Hours In DM View for future week is " + totalExtraHoursInDMViewForFuture);
            if(totalExtraHoursInDMViewForFuture.equals("0"))
                SimpleUtils.pass("Compliance Page: Analytics table matches the future week's data");
            else
                SimpleUtils.fail("Compliance Page: Analytics table doesn't match the future week's data",false);
            compliancePage.navigateToPreviousWeek();

            // Validate Late Schedule is Yes
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page not loaded successfully", dashboardPage.isScheduleConsoleMenuDisplay(), false);
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated)
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            locationSelectorPage.reSelectDistrict(districtName);

            compliancePage.clickOnComplianceConsoleMenu();
            compliancePage.clickOnRefreshButton();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            List<String>  dataCurrent = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String lateScheduleYes = dataCurrent.get(dataCurrent.size()-1);
            if (lateScheduleYes.equals("Yes"))
                SimpleUtils.pass("Compliance Page: Late Schedule is Yes as expected");
            else
                SimpleUtils.fail("Compliance Page: Late Schedule is not Yes",false);

            // Validate Late Schedule is No
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleDMViewPage.clickOnLocationNameInDMView(location);
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated)
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (smartCardPage.isRequiredActionSmartCardLoaded()){
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            locationSelectorPage.reSelectDistrict(districtName);

            compliancePage.clickOnComplianceConsoleMenu();
            compliancePage.navigateToNextWeek();
            compliancePage.navigateToNextWeek();
            List<String>  dataNext = compliancePage.getDataFromComplianceTableForGivenLocationInDMView(location);
            String lateScheduleNo = dataNext.get(dataNext.size()-1);
            if (lateScheduleNo.equals("No"))
                SimpleUtils.pass("Compliance Page: Late Schedule is No as expected");
            else
                SimpleUtils.fail("Compliance Page: Late Schedule is not No",false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate analytics table on Timesheet in DM View when it includes LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAnalyticsTableOnTimesheetInDMViewWhenItIncludesLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CompliancePage compliancePage = pageFactory.createConsoleCompliancePage();
            LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> subLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            String districtName = locationSelectorPage.getSelectedUpperFields().get(District);
            locationSelectorPage.selectCurrentUpperFieldAgain(District);
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            //P2P children location should not appear along with parent. so only LG’s parents would appear on the DM view.
            SimpleUtils.assertOnFail("The parent location: "+location+" should display in the analytics table on schedule DM view! ",
                    scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(location), false);

            for (String subLocation:subLocationNames) {
                SimpleUtils.assertOnFail("The sub location: "+subLocation+" should not display in the analytics table on schedule DM view! ",
                        !scheduleDMViewPage.checkIfLocationExistOnDMViewAnalyticsTable(subLocation), false);
            }

            //Validate the data of analytics table for past week.
            scheduleCommonPage.navigateToPreviousWeek();
            timeSheetPage.clickOnRefreshButton();
            Thread.sleep(5000);
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(location);
            SimpleUtils.assertOnFail("This is not the Timesheet SM view page for past week!",timeSheetPage.isTimeSheetPageLoaded(), false);
            dashboardPage.clickOnDashboardConsoleMenu();
            locationSelectorPage.reSelectDistrict(districtName);
            Thread.sleep(5000);
            timeSheetPage.clickOnTimeSheetConsoleMenu();
            //Validate the data of analytics table for current week.
            scheduleDMViewPage.clickSpecificLocationInDMViewAnalyticTable(location);
            SimpleUtils.assertOnFail("This is not the Timesheet SM view page for current!",timeSheetPage.isTimeSheetPageLoaded(), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content on Multiple Edit Shifts window for P2P location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOnMultipleEditShiftsWindowForP2PAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            BasePage basePage = new BasePage();
            String activeWeek = basePage.getActiveWeekText();
            String startOfWeek = activeWeek.split(" ")[3] + " " + activeWeek.split(" ")[4];
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            HashSet<Integer> set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            List<String> selectedDays = scheduleShiftTablePage.getSelectedWorkDays(set);
            // Verify action menu will pop up when right clicking on anywhere of the selected shifts
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            // Verify Edit action is visible when right clicking the selected shifts in week view
            // Verify the functionality of Edit button in week view
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            Thread.sleep(1000);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly
            editShiftPage.verifySelectedWorkDays(selectedShiftCount, selectedDays);
            // Verify the Location Name shows correctly
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section
            editShiftPage.verifyTheContentOfOptionsSection();
            // Verify the visibility of Clear Edited Fields button
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the three columns show on Shift Details section
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section
            editShiftPage.verifyEditableTypesShowOnShiftDetail();
            // Verify the functionality of x button
            editShiftPage.clickOnXButton();
            SimpleUtils.assertOnFail("Click on X button failed!", !editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the functionality of Cancel button
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnCancelButton();

            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleCommonPage.clickOnDayView();
            String weekDay = basePage.getActiveWeekText();
            String fullWeekDay = SimpleUtils.getFullWeekDayName(weekDay.split(" ")[0].trim());
            selectedDays = new ArrayList<>();
            selectedDays.add(fullWeekDay);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            Thread.sleep(3000);
            set = scheduleShiftTablePage.verifyCanSelectMultipleShifts(selectedShiftCount);
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window in day view
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly in day view
            editShiftPage.verifySelectedWorkDays(selectedShiftCount, selectedDays);
            // Verify the Location Name shows correctly in day view
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons in day view
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section in day view
            editShiftPage.verifyTheContentOfOptionsSection();
            // Verify the visibility of Clear Edited Fields button in day view
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the three columns show on Shift Details section in day view
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section in day view
            editShiftPage.verifyEditableTypesShowOnShiftDetail();
            editShiftPage.clickOnXButton();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TM cannot have a shift in more than one location without buffer time for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMCannotHaveShiftInMoreThanOneLocationWithoutBufferTimeForP2PAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(isActiveWeekGenerated){
               createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00am", "06:00am");
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            //Go to day view, check for TM: A at child location 1, has one shift, ex: 6am - 8am
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clickCloseBtnForCreateShift();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("6:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+ " "+lastName);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
            newShiftPage.moveSliderAtCertainPoint("10:15am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8:15am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();

            newShiftPage.searchWithOutSelectTM(firstNameOfTM+ " "+ lastName);
            String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
            String expectedWaningMessage= "Minimum time between shifts";
            SimpleUtils.assertOnFail(expectedWaningMessage+ " message fail to load!",
                    shiftWarningMessage.contains(expectedWaningMessage), false);
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
            expectedWaningMessage = firstNameOfTM+ " does not have minimum time between shifts";
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = newShiftPage.getWarningMessageFromWarningModal();

                if (warningMessage.toLowerCase().contains(expectedWaningMessage.toLowerCase())){
                    SimpleUtils.pass(expectedWaningMessage+" message displays");
                } else {
                    SimpleUtils.fail("There is no "+expectedWaningMessage+" warning message displaying", false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else {
                SimpleUtils.fail("There is no '"+expectedWaningMessage+"' warning modal displaying!",false);
            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            //https://legiontech.atlassian.net/browse/SCH-7977
//            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
//            expectedWaningMessage = "Minimum time between shifts";
//            String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(0)).toString();
//            SimpleUtils.assertOnFail("'"+expectedWaningMessage+"' compliance message display failed, the actual message is:"+actualMessage,
//                    actualMessage.contains(expectedWaningMessage) , false);
//            actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(1)).toString();
//            SimpleUtils.assertOnFail("'"+expectedWaningMessage+"' compliance message display failed, the actual message is:"+actualMessage,
//                    actualMessage.contains(expectedWaningMessage) , false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify shifts can be copyed to same day and another location on p2p LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCopyShiftsToSameDayAndAnotherLocationOnP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.expandSpecificCountGroup(2);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);
            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }

            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);

            //Select move option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("copy");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore+2 + " and "+ oneDayShiftsCountBefore+2
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore+2 && oneDayShiftsCountAfter == oneDayShiftsCountBefore+2, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore+2 && oneDayShiftsCountAfter == oneDayShiftsCountBefore+2, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Verify changes can be published
            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(1);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore+2 && oneDayShiftsCountAfter == oneDayShiftsCountBefore+2, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Check the shifts are not display on first location
            scheduleShiftTablePage.expandSpecificCountGroup(1);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate shifts can be moved to another day and another location on p2p LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyMoveShiftsToAnotherDayAndAnotherLocationOnP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.expandSpecificCountGroup(2);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);
            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }

            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

            //Select move option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("move");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore && oneDayShiftsCountAfter-2 == oneDayShiftsCountBefore, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);
            }
            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore && oneDayShiftsCountAfter-2 == oneDayShiftsCountBefore, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);
            }
            //Verify changes can be published
            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore && oneDayShiftsCountAfter-2 == oneDayShiftsCountBefore, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);
            }
            //Check the shifts are not display on first location
            scheduleShiftTablePage.expandSpecificCountGroup(1);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()==0, false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate shifts can be copied to another day and another location on p2p LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCopyShiftsToAnotherDayAndAnotherLocationOnP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);

            // Navigate to next week
            scheduleCommonPage.navigateToNextWeek();
            // create the schedule.
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleShiftTablePage.expandSpecificCountGroup(2);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Get shift count before drag and drop
            int allShiftsCountBefore = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountBefore = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            // Verify can select multiple shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 2;
            List<WebElement> selectedShifts = scheduleShiftTablePage.
                    selectMultipleDifferentAssignmentShiftsOnOneDay(selectedShiftCount, 1);
            List<String> shiftNames = new ArrayList<>();
            for (int i=0; i< selectedShiftCount;i++) {
                int index = scheduleShiftTablePage.getTheIndexOfShift(selectedShifts.get(i));
                shiftNames.add(scheduleShiftTablePage.getTheShiftInfoByIndex(index).get(0));
            }

            //Drag the selected shifts to same day
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);

            //Select move option
            scheduleShiftTablePage.selectMoveOrCopyBulkShifts("copy");
            scheduleShiftTablePage.enableOrDisableAllowComplianceErrorSwitch(true);
            scheduleShiftTablePage.enableOrDisableAllowConvertToOpenSwitch(true);
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();

            //Check the shift count after drag and drop
            int allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            int oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore+2 + " and "+ oneDayShiftsCountBefore+2
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore+2 && oneDayShiftsCountAfter == oneDayShiftsCountBefore+2, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be copied! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Verify changes can be saved
            scheduleMainPage.saveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore+2 && oneDayShiftsCountAfter == oneDayShiftsCountBefore+2, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Verify changes can be published
            createSchedulePage.publishActiveSchedule();
            allShiftsCountAfter = scheduleShiftTablePage.getShiftsCount();
            oneDayShiftsCountAfter = scheduleShiftTablePage.getOneDayShiftCountByIndex(2);
            SimpleUtils.assertOnFail("The expected count are: "+allShiftsCountBefore + " and "+ oneDayShiftsCountBefore
                            + ", but the actual are: "+allShiftsCountAfter + " and "+ oneDayShiftsCountAfter,
                    allShiftsCountAfter == allShiftsCountBefore+2 && oneDayShiftsCountAfter == oneDayShiftsCountBefore+2, false);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
            //Check the shifts are not display on first location
            scheduleShiftTablePage.expandSpecificCountGroup(1);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(1, shiftNames.get(i)).size()>0, false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate navigation, roster, schedule and dashboard of P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void validateNavigationRosterScheduleAndDashboardOfP2PLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            goToSchedulePageScheduleTab();
            //Verify all the sub locations will be generated when generate parent level location for P2P LG
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String workRole = shiftOperatePage.getRandomWorkRole();
            List<String> locationNames = scheduleMainPage.getSpecificFilterNames("location");
            scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            ArrayList<HashMap<String,String>> locations  = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("The sub location count is incorrectly on schedule! ",
                    locationNames.size() == locations.size(), false);
            List<String> locationsFromGroupByOption = new ArrayList<>();
            for (HashMap<String, String> stringStringHashMap : locations) {
                locationsFromGroupByOption.add(stringStringHashMap.get("optionName"));
            }

            SimpleUtils.assertOnFail("The locations from group by option are:"+locationsFromGroupByOption
                            + " the locations from filter are:"+ locationNames,
                    locationsFromGroupByOption.toString().equalsIgnoreCase(locationNames.toString()), false);


            teamPage.goToTeam();
            int parentRosterCount = teamPage.verifyTMCountIsCorrectOnRoster();
            dashboardPage.clickOnDashboardConsoleMenu();
            int upcomingShiftCountOnParentSMDashboard = dashboardPage.getUpComingShifts().size();
            int childRosterCount = 0;
            int upcomingShiftCountOnChildSMDashboard = 0;
            String tmName = "";
            for (String childLocation: locationNames) {
                locationSelectorPage.changeUpperFieldDirect(Location, childLocation);
                dashboardPage.clickOnDashboardConsoleMenu();
                dashboardPage.clickOnRefreshButtonOnSMDashboard();
                Thread.sleep(5000);
                upcomingShiftCountOnChildSMDashboard = upcomingShiftCountOnParentSMDashboard+ dashboardPage.getUpComingShifts().size();

                //Verify all sub-locations of P2P LG have their own demand forecasts/labor forecast/staffing guidance
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
                forecastPage.clickOnShopper();
                forecastPage.verifyDemandForecastCanLoad();
                forecastPage.clickOnLabor();
                forecastPage.verifyLaborForecastCanLoad();

                //Verify TMs in one location can be searched out on other locations for P2P LG
                if (!tmName.equalsIgnoreCase("")) {
                    goToSchedulePageScheduleTab();
                    scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                    scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmName);
                    createShiftsWithSpecificValues(workRole, null, null, "9:00am", "12:00pm",
                            1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), null, tmName);
                    List<WebElement> tmShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(tmName.split(" ")[0]);
                    SimpleUtils.assertOnFail("TM "+ tmName+"'s shift should be created! ",
                            tmShifts.size()>0,false);
                    scheduleMainPage.clickOnCancelButtonOnEditMode();
                }
                teamPage.goToTeam();
                childRosterCount = childRosterCount + teamPage.verifyTMCountIsCorrectOnRoster();
                tmName = teamPage.selectATeamMemberToViewProfile();

                dashboardPage.clickOnDashboardConsoleMenu();
                dashboardPage.clickOnRefreshButtonOnSMDashboard();
                upcomingShiftCountOnParentSMDashboard = upcomingShiftCountOnParentSMDashboard
                        + dashboardPage.getUpComingShifts().size();

                //Verify the P2P LG schedule can be accessed as parent location or sub-location
                goToSchedulePageScheduleTab();
                SimpleUtils.assertOnFail("The schedule page should be load!",
                        scheduleMainPage.isScheduleMainPageLoaded(), false);
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmName.split(" ")[0]);
                scheduleMainPage.saveSchedule();
                Thread.sleep(2000);
            }
            //Verify the roster is at sub-location level for P2P LG
            SimpleUtils.assertOnFail("The parent location roster count is:"+parentRosterCount
                            + " the child locations roster count is:"+childRosterCount,
                    parentRosterCount >= childRosterCount, false);

            //Verify the SM dashboard of P2P LG, SM Dashboard will aggregate data for the widgets
            SimpleUtils.assertOnFail("The parent location upcoming shift count is:"+upcomingShiftCountOnParentSMDashboard
                            + " the child locations upcoming shift count is:"+upcomingShiftCountOnChildSMDashboard,
                    upcomingShiftCountOnParentSMDashboard == upcomingShiftCountOnChildSMDashboard, false);

            //Verify all the sub locations will be ungenerated when ungenerate parent level location for P2P LG
            locationSelectorPage.changeUpperFieldDirect(Location, location);
            goToSchedulePageScheduleTab();
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            SimpleUtils.assertOnFail("The schedule should be ungenerate successfully! ",
                    !createSchedulePage.isWeekGenerated() && createSchedulePage.isGenerateButtonLoaded(), false);
            //Verify the location navigation of P2P LG
            List<String> allLocations = locationSelectorPage.getAllUpperFieldNamesInUpperFieldDropdownList(Location);
            SimpleUtils.assertOnFail("Parent location should display in location navigation list",
                    allLocations.contains(location), false);
            for (int i=0;i< locationNames.size(); i++){
                SimpleUtils.assertOnFail("Child location "+ locationNames.get(i)+" should display in location navigation list: "+ allLocations,
                        allLocations.contains(locationNames.get(i)), false);
            }

            //Verify all the sub locations can be generated separately for P2P LG
            String peerLocation = locationNames.get(new Random().nextInt(locationNames.size()));
            locationSelectorPage.changeUpperFieldDirect(Location, peerLocation);
            goToSchedulePageScheduleTab();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            //Verify the parent location still can been generated successfully when sub-location been generated before for P2P LG
            locationSelectorPage.changeUpperFieldDirect(Location, location);
            goToSchedulePageScheduleTab();
            SimpleUtils.assertOnFail("The parent location cannot been generated! ",
                    createSchedulePage.isCreateScheduleBtnLoadedOnSchedulePage(), false);
            //Verify all sub-locations of P2P LG have their own operating hours
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.clickCreateScheduleButton();
            for (int i=0;i< locationNames.size(); i++) {
                createSchedulePage.selectLocationOnCreateScheduleEditOperatingHoursPage(locationNames.get(i));
                createSchedulePage.editOperatingHoursWithGivingPrameters("8:00AM", "8:00PM");
            }
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();
            createSchedulePage.selectWhichWeekToCopyFrom("SUGGESTED");
            createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();


        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing location on P2P")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingLocationForP2POnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            ArrayList<HashMap<String,String>> childLocations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            List<String> locations = new ArrayList<>();
            for (int i = 0; i < childLocations.size(); i++) {
                locations.add(childLocations.get(i).get("optionName"));
            }

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> shiftInfoList1 = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
            List<String> shiftInfoList2 = scheduleShiftTablePage.getTheShiftInfoByIndex(1);

            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(0);
            indexes.add(1);
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            Iterator<Integer> iterator = indexes.iterator();
            List<Integer> indexList = new ArrayList<>();
            while(iterator.hasNext()){
                indexList.add(iterator.next());
            }
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify the child locations listed in the Location selector
            editShiftPage.clickOnLocationSelect();
            List<String> actualLocations = editShiftPage.getOptionsFromSpecificSelect();
            for (int i = 0; i < locations.size(); i++) {
                if (!locations.get(i).toLowerCase().trim().equalsIgnoreCase(actualLocations.get(i).toLowerCase().trim())) {
                    SimpleUtils.fail("Child location list is incorrect!", false);
                    break;
                }
            }
            // Verify can change the location without selecting the two options
            editShiftPage.selectSpecificOptionByText(actualLocations.get(1));
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            // Verify the shifts are moved to the selected child location
            scheduleMainPage.selectLocationFilterByText(actualLocations.get(1));
            SimpleUtils.assertOnFail("Shift is not moved the child location: " + actualLocations.get(1),
                    scheduleShiftTablePage.getOneDayShiftByName(0, shiftInfoList1.get(0)).size() == 1, false);
            SimpleUtils.assertOnFail("Shift is not moved the child location: " + actualLocations.get(1),
                    scheduleShiftTablePage.getOneDayShiftByName(1, shiftInfoList2.get(0)).size() == 1, false);
            // Verify the shifts are saved successfully
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("Shift is not moved the child location: " + actualLocations.get(1),
                    scheduleShiftTablePage.getOneDayShiftByName(0, shiftInfoList1.get(0)).size() == 1, false);
            SimpleUtils.assertOnFail("Shift is not moved the child location: " + actualLocations.get(1),
                    scheduleShiftTablePage.getOneDayShiftByName(1, shiftInfoList2.get(0)).size() == 1, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    
    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Budget/Guidance hours show correctly when navigating from parent location to peer location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifyTheBudgetGuidanceColumnShowCorrectlyInChildAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ConfigurationPage configuration = pageFactory.createOpsPortalConfigurationPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.editLaborBudgetSettingContent();
            locationsPage.turnOnOrTurnOffLaborBudgetToggle(true);
            locationsPage.selectBudgetGroup("By Location");
            configuration.updateInputBudgetSettingDropdownOption("Hours");
            locationsPage.saveTheGlobalConfiguration();
            switchToConsoleWindow();
            refreshCachesAfterChangeTemplate();
            Thread.sleep(60000);

            //Select parent location's budget/guidance value
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            //Check child location's budget on schedule smart card, it won't same as parent
            boolean isActiveWeekGenerated1 = createSchedulePage.isWeekGenerated();
            if (!isActiveWeekGenerated1) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            List <String> locationNames = scheduleMainPage.getSpecificFilterNames("location");
            String childLocation1 = locationNames.get(0);
            String childLocation2 = locationNames.get(1);

            boolean isActiveWeekGenerated2 = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated2) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationNames.get(0));
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
            forecastPage.goToForecastLaborWeek();

            scheduleMainPage.checkAllWorkRolesUnderLabor();
            scheduleMainPage.clickWorkRoleFilterOfLabor();

            forecastPage.editLaborBudgetOnSummarySmartCard();
            String laborBudget1 = forecastPage.getLaborBudgetOnSummarySmartCard();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(locationNames.get(1));
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
            forecastPage.goToForecastLaborWeek();

            scheduleMainPage.checkAllWorkRolesUnderLabor();
            scheduleMainPage.clickWorkRoleFilterOfLabor();

            forecastPage.editLaborBudgetOnSummarySmartCard();
            String laborBudget2 = forecastPage.getLaborBudgetOnSummarySmartCard();

            //Generate the parent schedule, compare the budget after the page loaded fully.
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String parentBudget = smartCardPage.getBudgetValueFromScheduleBudgetSmartCard().trim();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(childLocation1);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            String budgetOfChild1 = smartCardPage.getBudgetValueFromScheduleBudgetSmartCard().trim();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(childLocation2);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            String budgetOfChild2 = smartCardPage.getBudgetValueFromScheduleBudgetSmartCard().trim();

            SimpleUtils.assertOnFail("The budget on schedule view is not consisting with the edited value on Forecast page!",laborBudget1.equals(budgetOfChild1) && laborBudget2.equals(budgetOfChild2),false);

            //Compare the budget between parent and child, they are not same
            SimpleUtils.assertOnFail("The budget between parent and child location shouldn't be same!",!(parentBudget.equals(budgetOfChild1) && parentBudget.equals(budgetOfChild2)),false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content on Edit Single Shift window for P2P location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOnEditSingleShiftWindowForP2PAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
            String workRole = shiftOperatePage.getRandomWorkRole();
            BasePage basePage = new BasePage();
            String activeWeek = basePage.getActiveWeekText();
            String startOfWeek = activeWeek.split(" ")[3] + " " + activeWeek.split(" ")[4];
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Verify can select single shifts by pressing Ctrl/Cmd(Mac)
            int selectedShiftCount = 1;
            int index = 1;
            HashSet<Integer> set = new HashSet<>();
            set.add(index);
            List<String> shiftInfo= scheduleShiftTablePage.getTheShiftInfoByIndex(index);
            List<String> selectedDays = scheduleShiftTablePage.getSelectedWorkDays(set);
            // Verify action menu will pop up when right clicking on anywhere of the selected shifts
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.verifyTheContentOnBulkActionMenu(selectedShiftCount);
            // Verify Edit action is visible when right clicking the selected shift in week view
            // Verify the functionality of Edit button in week view
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly
            editShiftPage.verifyShiftInfoCard(shiftInfo);
            // Verify the Location Name shows correctly
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section
            editShiftPage.verifyTheContentOfOptionsSectionIsNotLoaded();
            // Verify the visibility of Clear Edited Fields button
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the three columns show on Shift Details section
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section
            editShiftPage.verifyEditableTypesShowOnSingleEditShiftDetail();
            // Verify the functionality of x button
            editShiftPage.clickOnXButton();
            SimpleUtils.assertOnFail("Click on X button failed!", !editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the functionality of Cancel button
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            editShiftPage.clickOnCancelButton();

            scheduleMainPage.clickOnCancelButtonOnEditMode();
            scheduleCommonPage.clickOnDayView();
            String weekDay = basePage.getActiveWeekText();
            String fullWeekDay = SimpleUtils.getFullWeekDayName(weekDay.split(" ")[0].trim());
            selectedDays = new ArrayList<>();
            selectedDays.add(fullWeekDay);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            shiftInfo = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.rightClickOnSelectedShifts(set);
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);
            // Verify the title of Edit Shifts window in day view
            editShiftPage.verifyTheTitleOfEditShiftsWindow(selectedShiftCount, startOfWeek);
            // Verify the selected days show correctly in day view
//            editShiftPage.verifyShiftInfoCard(shiftInfo);
            // Verify the Location Name shows correctly in day view
            editShiftPage.verifyLocationNameShowsCorrectly(location);
            // Verify the visibility of buttons in day view
            editShiftPage.verifyTheVisibilityOfButtons();
            // Verify the content of options section in day view
            editShiftPage.verifyTheContentOfOptionsSectionIsNotLoaded();
            // Verify the visibility of Clear Edited Fields button in day view
            //SimpleUtils.assertOnFail("Clear Edited Fields button failed to load!", editShiftPage.isClearEditedFieldsBtnLoaded(), false);
            // Verify the three columns show on Shift Details section in day view
            editShiftPage.verifyTwoColumns();
            // Verify the editable types show on Shift Detail section in day view
            editShiftPage.verifyEditableTypesShowOnSingleEditShiftDetail();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing location when selecting single shift on P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingLocationForP2POnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            ArrayList<HashMap<String,String>> childLocations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            List<String> locations = new ArrayList<>();
            for (int i = 0; i < childLocations.size(); i++) {
                locations.add(childLocations.get(i).get("optionName"));
            }

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<String> shiftInfoList1 = scheduleShiftTablePage.getTheShiftInfoByIndex(0);

            HashSet<Integer> indexes = new HashSet<>();
            indexes.add(0);
            scheduleShiftTablePage.selectSpecificShifts(indexes);
            scheduleShiftTablePage.rightClickOnSelectedShifts(indexes);
            String action = "Edit";
            scheduleShiftTablePage.clickOnBtnOnBulkActionMenuByText(action);
            SimpleUtils.assertOnFail("Edit Shifts window failed to load!", editShiftPage.isEditShiftWindowLoaded(), false);

            // Verify the child locations listed in the Location selector
            editShiftPage.clickOnLocationSelect();
            List<String> actualLocations = editShiftPage.getOptionsFromSpecificSelect();
            for (int i = 0; i < locations.size(); i++) {
                if (!locations.get(i).toLowerCase().trim().equalsIgnoreCase(actualLocations.get(i).toLowerCase().trim())) {
                    SimpleUtils.fail("Child location list is incorrect!", false);
                    break;
                }
            }
            // Verify can change the location without selecting the two options
            editShiftPage.selectSpecificOptionByText(actualLocations.get(1));
            editShiftPage.clickOnUpdateButton();
            editShiftPage.clickOnUpdateAnywayButton();
            // Verify the shifts are moved to the selected child location
            scheduleMainPage.selectGroupByFilter(actualLocations.get(1));
            SimpleUtils.assertOnFail("Shift is not moved the child location: " + actualLocations.get(1),
                    scheduleShiftTablePage.getOneDayShiftByName(0, shiftInfoList1.get(0)).size() == 1, false);
            // Verify the shifts are saved successfully
            scheduleMainPage.saveSchedule();
            SimpleUtils.assertOnFail("Shift is not moved the child location: " + actualLocations.get(1),
                    scheduleShiftTablePage.getOneDayShiftByName(0, shiftInfoList1.get(0)).size() == 1, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate TMs Can Receive and Accept Offers for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEmployeeCanReceiveAndAcceptOffersForP2PLGAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String teamMemberName = profileNewUIPage.getNickNameFromProfile();
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            //to generate schedule if current week is not generated
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(teamMemberName, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(teamMemberName, "offered");
            shiftOperatePage.closeViewStatusContainer();
            loginPage.logOut();

            // 3.Login with the TM to claim the shift
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            String cardName = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
            String linkName = "View Shifts";
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            SimpleUtils.assertOnFail("Open shifts not loaed Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
            loginPage.logOut();

            // 4.Login with SM to check activity
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
            activityPage.verifyActivityOfShiftOffer(teamMemberName, "");
            activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());

            //Check the shift been scheduled
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();

            int tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(teamMemberName);
            SimpleUtils.assertOnFail("The expect "+teamMemberName+"'s shift count is 1, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount >= 1, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate TMs Can Receive and Accept Swap request for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeesCanSwapShiftsForP2PLGAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try{
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            String lastName = tmFullName.split(" ")[1];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName2 = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName2 = tmFullName2.split(" ")[0];
            String lastName2 = tmFullName2.split(" ")[1];
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName2);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocationNames.get(0),
                    "8am", "2pm", 1, Arrays.asList(6),
                    ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstName+ " "+ lastName);
            createShiftsWithSpecificValues(workRole, null, childLocationNames.get(1),
                    "8am", "2pm", 1, Arrays.asList(0,1,2),
                    ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstName2+ " "+lastName2);

            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();

            // For Swap Feature
            List<String> swapCoverRequsts = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
            int index = mySchedulePage.verifyClickOnAnyShift();
            String request = "Request to Swap Shift";
            String title = "Find Shifts to Swap";
            mySchedulePage.clickTheShiftRequestByName(request);
            SimpleUtils.assertOnFail(title + " page not loaded Successfully!",
                    mySchedulePage.isPopupWindowLoaded(title), true);
            mySchedulePage.verifyComparableShiftsAreLoaded();
            mySchedulePage.verifySelectMultipleSwapShifts();
            // Validate the Submit button feature
            mySchedulePage.verifyClickOnNextButtonOnSwap();
            title = "Submit Swap Request";
            SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
            mySchedulePage.verifyClickOnSubmitButton();
            // Validate the disappearence of Request to Swap and Request to Cover option
            mySchedulePage.clickOnShiftByIndex(index);
            if (!mySchedulePage.verifyShiftRequestButtonOnPopup(swapCoverRequsts)) {
                SimpleUtils.pass("Request to Swap and Request to Cover options are disappear");
            }else {
                SimpleUtils.fail("Request to Swap and Request to Cover options are still shown!", false);
            }

            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();

            // Validate that swap request smartcard is available to recipient team member
            String smartCard = "SWAP REQUESTS";
            smartCardPage.isSmartCardAvailableByLabel(smartCard);
            // Validate the availability of all swap request shifts in schedule table
            String linkName = "View All";
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            mySchedulePage.verifySwapRequestShiftsLoaded();
            // Validate that recipient can claim the swap request shift.
            mySchedulePage.verifyClickAcceptSwapButton();

            loginPage.logOut();

            // Login as Store Manager
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());

            // Verify Activity Icon is loaded
            String actionLabel = "requested";
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.verifyNewShiftSwapCardWithTwoLocationsShowsOnActivity(firstName, firstName2, actionLabel, true, childLocationNames.get(1), childLocationNames.get(0));
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
            activityPage.verifyNewShiftSwapCardWithTwoLocationsShowsOnActivity(firstName, firstName2, actionLabel, false, childLocationNames.get(1), childLocationNames.get(0));
            activityPage.approveOrRejectShiftSwapRequestOnActivity(firstName, firstName2, ActivityTest.approveRejectAction.Approve.getValue());
            //To close activities popup
            activityPage.closeActivityWindow();
            loginPage.logOut();
            // Login as Store Manager
            loginAsDifferentRole(AccessRoles.StoreManager2.getValue());
//        String firstName = "Aron";
//        String firstName2= "Gianni";
//        String childLocation2 = "Child002";
//        String childLocation1= "Child001";

            // Verify Activity Icon is loaded
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
            activityPage.verifyNewShiftSwapCardWithTwoLocationsShowsOnActivity(firstName, firstName2, actionLabel, false, childLocationNames.get(1), childLocationNames.get(0));
            activityPage.approveOrRejectShiftSwapRequestOnActivity(firstName, firstName2, ActivityTest.approveRejectAction.Approve.getValue());
            //To close activities popup
            activityPage.closeActivityWindow();

            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            int tmShiftCount = scheduleShiftTablePage.getOneDayShiftByName(0, firstName).size();
            tmShiftCount+= scheduleShiftTablePage.getOneDayShiftByName(1, firstName).size();
            tmShiftCount+= scheduleShiftTablePage.getOneDayShiftByName(2, firstName).size();
            SimpleUtils.assertOnFail("The expect shift count is 1, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount >= 1, false);
            tmShiftCount = scheduleShiftTablePage.getOneDayShiftByName(6, firstName2).size();
            SimpleUtils.assertOnFail("The expect shift count is 0, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount >= 1, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate TMs Can Receive and Accept Cover request for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeesCanClaimCoverRequestForP2PLGAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try{
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
        String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
        String firstName = tmFullName.split(" ")[0];
        String lastName = tmFullName.split(" ")[1];
        String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
//        String childLocation1 = location;
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
        profileNewUIPage.clickOnUserProfileImage();
        profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
        String tmFullName2 = profileNewUIPage.getUserProfileName().get("fullName");
        String firstName2 = tmFullName2.split(" ")[0];
        String lastName2 = tmFullName2.split(" ")[1];
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        goToSchedulePageScheduleTab();
        scheduleCommonPage.navigateToNextWeek();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName2);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
        scheduleMainPage.saveSchedule();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectJobTitleFilterByText(jobTitle);
        String workRole = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnFilterBtn();
        List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
//        if (tmShiftCountBeforeCover == 0){
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocationNames.get(0),
                    "8am", "2pm", 1, Arrays.asList(),
                    ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstName+ " "+ lastName);
            scheduleMainPage.saveSchedule();
//        }

        Thread.sleep(3000);
        createSchedulePage.publishActiveSchedule();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();

        // For Cover Feature
        List<String> swapCoverRequests = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
        mySchedulePage.verifyClickOnAnyShift();
        //String request = "Request to Cover Shift";
        mySchedulePage.clickTheShiftRequestByName(swapCoverRequests.get(1));
        // Validate the Submit button feature
        String title = "Submit Cover Request";
        SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
        mySchedulePage.verifyClickOnSubmitButton();
        String requestName = "View Cover Request Status";
        int coverRequestsCount = 0;
        int i = 0;
        while (i<10 && coverRequestsCount==0) {
            Thread.sleep(30000);
            mySchedulePage.clickTheShiftRequestToClaimShift(requestName, firstName);
            coverRequestsCount = mySchedulePage.getCountOfCoverOrSwapRequestsInList();
            mySchedulePage.clickCloseDialogButton();
            i++;
        }
        mySchedulePage.clickTheShiftRequestToClaimShift(requestName, firstName);
        SimpleUtils.assertOnFail("The TM:" + firstName2 + " should be listed! ",
                mySchedulePage.checkIfTMExitsInCoverOrSwapRequestList(firstName2), false);
        mySchedulePage.clickCloseDialogButton();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.TeamMember2.getValue());
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.navigateToNextWeek();
        // Validate that smartcard is available to recipient team member
        String smartCard = "WANT MORE HOURS?";
        SimpleUtils.assertOnFail("Smart Card: " + smartCard + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(smartCard), false);
        // Validate the availability of all cover request shifts in schedule table
        String linkName = "View Shifts";
        smartCardPage.clickLinkOnSmartCardByName(linkName);
        SimpleUtils.assertOnFail("Open shifts not loaded Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
        // Validate the availability of Claim Shift Request popup
        requestName = "View Offer";
        mySchedulePage.clickTheShiftRequestToClaimCoverShift(requestName);
        // Validate the clickability of I Agree button
        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimRequestBeenSendForApprovalMessage);
        loginPage.logOut();

        // Login as Store Manager
        loginAsDifferentRole(AccessRoles.StoreManager.getValue());

        // Verify Activity Icon is loaded
        String actionLabel = "requested";
        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
        activityPage.approveOrRejectShiftCoverRequestOnActivity(firstName, firstName2, ActivityTest.approveRejectAction.Approve.getValue(), childLocationNames.get(0));
        //To close activities popup
        activityPage.closeActivityWindow();
        loginPage.logOut();
        loginAsDifferentRole(AccessRoles.StoreManager2.getValue());

        // Verify Activity Icon is loaded
        activityPage.verifyActivityBellIconLoaded();
        activityPage.verifyClickOnActivityIcon();
        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
        activityPage.approveOrRejectShiftCoverRequestOnActivity(firstName, firstName2, ActivityTest.approveRejectAction.Approve.getValue(), childLocationNames.get(0));
        //To close activities popup
        activityPage.closeActivityWindow();
        loginPage.logOut();

        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        goToSchedulePageScheduleTab();
        scheduleCommonPage.navigateToNextWeek();
        Thread.sleep(5000);
        int tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstName);
        SimpleUtils.assertOnFail("The expect shift count is 0, the actual shift count is:"+tmShiftCount,
                tmShiftCount == 0, false);
        tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstName2);
        SimpleUtils.assertOnFail("The expect shift count is 1, the actual shift count is:"+tmShiftCount,
                tmShiftCount == 1, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of drag and drop shift to same location for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropShiftToSameLocationForP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            goToSchedulePageScheduleTab();

            //Verify the open shifts will be created when drag&drop shift in same day and same location
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated) {
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            //Get child locations
//            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            //create shifts
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(0), "8am", "11am", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM+ " "+ lastName);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.selectLocationFilterByText(childLocationNames.get(0));
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int shiftCountOnFirstDayBeforeDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM);
            int shiftCountOnSecondDayBeforeDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM);
            int shiftCountOnThirdDayBeforeDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(2, firstNameOfTM);
            List<WebElement> selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 0, false);
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==shiftCountOnFirstDayBeforeDragAndDrop
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,"Open")==1){
                SimpleUtils.pass("Drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! ", false);

            //Verify shift can be copied to different day and same location by drag and drop
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM,1);
            //Verify shift can be moved to different day and same location by drag and drop
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM,2);

            //Verify shifts display correctly after save schedule
            scheduleMainPage.saveSchedule();
            int shiftCountOnFirstDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM);
            int shiftCountOnSecondDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM);
            int shiftCountOnThirdDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(2, firstNameOfTM);
            if (shiftCountOnFirstDayAfterDragAndDrop ==shiftCountOnFirstDayBeforeDragAndDrop-1
                    && shiftCountOnSecondDayAfterDragAndDrop ==shiftCountOnSecondDayBeforeDragAndDrop+1
                    && shiftCountOnThirdDayAfterDragAndDrop == shiftCountOnThirdDayBeforeDragAndDrop+1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,"Open")==1){
                SimpleUtils.pass("Drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! The actual counts on three days are: "
                        +shiftCountOnFirstDayAfterDragAndDrop + ", "
                        +shiftCountOnSecondDayAfterDragAndDrop + ", "
                        +shiftCountOnThirdDayAfterDragAndDrop, false);

            //Verify shifts display correctly after publish schedule
            createSchedulePage.publishActiveSchedule();
            shiftCountOnFirstDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM);
            shiftCountOnSecondDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM);
            shiftCountOnThirdDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(2, firstNameOfTM);
            if (shiftCountOnFirstDayAfterDragAndDrop ==shiftCountOnFirstDayBeforeDragAndDrop-1
                    && shiftCountOnSecondDayAfterDragAndDrop ==shiftCountOnSecondDayBeforeDragAndDrop+1
                    && shiftCountOnThirdDayAfterDragAndDrop == shiftCountOnThirdDayBeforeDragAndDrop+1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,"Open")==1){
                SimpleUtils.pass("Drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! The actual counts on three days are: "
                        +shiftCountOnFirstDayAfterDragAndDrop + ", "
                        +shiftCountOnSecondDayAfterDragAndDrop + ", "
                        +shiftCountOnThirdDayAfterDragAndDrop, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of drag and drop shift to different location for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropShiftToDifferentLocationForP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            goToSchedulePageScheduleTab();

            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated) {
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            String firstNameOfTM2 = shiftInfo.get(0);
            while (firstNameOfTM2.equalsIgnoreCase("open")
                    || firstNameOfTM2.equalsIgnoreCase("unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM2  = shiftInfo.get(0);
            }
            String workRole2 = shiftInfo.get(4);
            String lastName2 = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();
            //Get child locations
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            //create shifts
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(0), "8am", "11am", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM+ " "+ lastName);
            createShiftsWithSpecificValues(workRole2, "", childLocationNames.get(0), "8am", "11am", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM2+ " "+ lastName2);
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            //Verify shift will be covered to open when copy shift to same day and different location
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM2);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 0, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.ifMoveAnywayDialogDisplay()) {
                scheduleShiftTablePage.moveAnywayWhenChangeShift();
            }
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,"Open")==0){
                SimpleUtils.pass(firstNameOfTM2+ " drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! ", false);
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            //Verify shift will be covered to open when copy shift to same day and different location
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 0, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.copyAnywayWhenChangeShift();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,"Open")==1){
                SimpleUtils.pass("Drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! ", false);
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            //Verify shift can be copied to different day and different location by drag and drop
            goToSchedulePageScheduleTab();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM,1);
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Verify shift can be moved to different day and different location by drag and drop
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 2, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM,2);
            scheduleMainPage.saveSchedule();
            //Verify shift can be copied to different day and different location by drag and drop

            //Verify shifts display correctly after save schedule
            int shiftCountOnFirstDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM);
            int shiftCountOnSecondDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM);
            int shiftCountOnThirdDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(2, firstNameOfTM);
            if (shiftCountOnFirstDayAfterDragAndDrop == 0
                    && shiftCountOnSecondDayAfterDragAndDrop ==1
                    && shiftCountOnThirdDayAfterDragAndDrop == 1){
                SimpleUtils.pass("Drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! The actual counts on three days are: "
                        +shiftCountOnFirstDayAfterDragAndDrop + ", "
                        +shiftCountOnSecondDayAfterDragAndDrop + ", "
                        +shiftCountOnThirdDayAfterDragAndDrop, false);

            //Verify shifts display correctly after publish schedule
            createSchedulePage.publishActiveSchedule();
            shiftCountOnFirstDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(0, firstNameOfTM);
            shiftCountOnSecondDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(1, firstNameOfTM);
            shiftCountOnThirdDayAfterDragAndDrop = scheduleShiftTablePage.verifyDayHasShiftByName(2, firstNameOfTM);
            if (shiftCountOnFirstDayAfterDragAndDrop == 0
                    && shiftCountOnSecondDayAfterDragAndDrop ==1
                    && shiftCountOnThirdDayAfterDragAndDrop == 1){
                SimpleUtils.pass("Drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! The actual counts on three days are: "
                        +shiftCountOnFirstDayAfterDragAndDrop + ", "
                        +shiftCountOnSecondDayAfterDragAndDrop + ", "
                        +shiftCountOnThirdDayAfterDragAndDrop, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of drag and drop employee to same location for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropEmployeeToSameLocationForP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            goToSchedulePageScheduleTab();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated) {
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            String firstNameOfTM2 = shiftInfo.get(0);
            while (firstNameOfTM2.equalsIgnoreCase("open")
                    || firstNameOfTM2.equalsIgnoreCase("unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM2  = shiftInfo.get(0);
            }
            String workRole2 = shiftInfo.get(4);
            String lastName2 = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();
            //Get child locations
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            //create shifts
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(0), "8am", "11am", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM+ " "+ lastName);
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(0), "2am", "6am", 1,
                    Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM+ " "+ lastName);
            createShiftsWithSpecificValues(workRole2, "", childLocationNames.get(0), "5pm", "9pm", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM2+ " "+ lastName2);
            scheduleMainPage.saveSchedule();


//            String firstNameOfTM = "Anastacio";
//            String firstNameOfTM2 = "Cleve";

            goToSchedulePageScheduleTab();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Verify the assignment can be swapped when drag&drop employee avatar to another one in same day and same location
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM,0,firstNameOfTM2);
            Thread.sleep(3000);
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1){
                SimpleUtils.pass("assign successfully!");
            }
            //Verify the assignment can be covered when drag&drop employee avatar to another one in same day and same location
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM2,0,firstNameOfTM);
            Thread.sleep(3000);
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==2
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==0){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==2
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==0){
                SimpleUtils.pass("assign successfully!");
            }
            //Verify the assignment can be covered when drag&drop employee avatar to another one in different day and same location
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM2,1,firstNameOfTM);
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM2)==1){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Verify the assignment can be swapped when drag&drop employee avatar to another one in different day and same location
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM,1,firstNameOfTM2);
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM2)==0){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM2)==0){
                SimpleUtils.pass("assign successfully!");
            }

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of drag and drop employee to different location for p2 LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropEmployeeToDifferentLocationForP2PLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try{
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();

            goToSchedulePageScheduleTab();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated) {
                createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
            }
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            String firstNameOfTM2 = shiftInfo.get(0);
            while (firstNameOfTM2.equalsIgnoreCase("open")
                    || firstNameOfTM2.equalsIgnoreCase("unassigned")
                    || firstNameOfTM2.equalsIgnoreCase(firstNameOfTM)) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM2  = shiftInfo.get(0);
            }
            String workRole2 = shiftInfo.get(4);
            String lastName2 = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            scheduleMainPage.saveSchedule();
            //Get child locations
            scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");

            //create shifts
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(0), "8am", "11am", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM+ " "+ lastName);
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(1), "2am", "6am", 1,
                    Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM+ " "+ lastName);
            createShiftsWithSpecificValues(workRole2, "", childLocationNames.get(1), "5pm", "9pm", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM2+ " "+ lastName2);
            scheduleMainPage.saveSchedule();


//            String firstNameOfTM = "Anastacio";
//            String firstNameOfTM2 = "Cleve";

            goToSchedulePageScheduleTab();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Verify the assignment can be swapped when drag&drop employee avatar to another one in same day and same location
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM,0,firstNameOfTM2);
            Thread.sleep(3000);
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1){
                SimpleUtils.pass("assign successfully!");
            }
            //Verify the assignment can be covered when drag&drop employee avatar to another one in same day and same location
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM2,0,firstNameOfTM);
            Thread.sleep(3000);
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==2
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==0){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==2
                    && scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM2)==0){
                SimpleUtils.pass("assign successfully!");
            }
            //Verify the assignment can be covered when drag&drop employee avatar to another one in different day and same location
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM2,1,firstNameOfTM);
            Thread.sleep(3000);
            scheduleShiftTablePage.selectSwapOrAssignOption("swap");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM2)==1){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Verify the assignment can be swapped when drag&drop employee avatar to another one in different day and same location
            scheduleShiftTablePage.dragOneAvatarToAnotherSpecificAvatar(0,firstNameOfTM,1,firstNameOfTM2);
            Thread.sleep(3000);
            scheduleShiftTablePage.selectSwapOrAssignOption("assign");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM2)==0){
                SimpleUtils.pass("assign successfully!");
            }
            scheduleMainPage.saveSchedule();
            if (scheduleShiftTablePage.verifyDayHasShiftByName(0,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM)==1
                    && scheduleShiftTablePage.verifyDayHasShiftByName(1,firstNameOfTM2)==0){
                SimpleUtils.pass("assign successfully!");
            }

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
    
    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the parent location disable the budget edit button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void VerifyParentLocationDisabledLaborBudgetEditBtnAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ForecastPage forecastPage = pageFactory.createForecastPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToGlobalConfigurationInLocations();
            locationsPage.editLaborBudgetSettingContent();
            locationsPage.turnOnOrTurnOffLaborBudgetToggle(true);
            locationsPage.selectBudgetGroup("By Location");
            locationsPage.saveTheGlobalConfiguration();
            switchToConsoleWindow();
            refreshCachesAfterChangeTemplate();
            Thread.sleep(120000);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            List <String> locationNames = scheduleMainPage.getSpecificFilterNames("location");
            String childLocation1 = locationNames.get(0);
            String childLocation2 = locationNames.get(1);

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(childLocation1);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());

            forecastPage.goToForecastLaborWeek();
            SimpleUtils.assertOnFail("Child location disabled the labor budget edit button on week view!",
                    forecastPage.isLaborBudgetEditBtnLoaded(),false);
            forecastPage.editLaborBudgetOnSummarySmartCard();
            forecastPage.goToForecastLaborDay();
            SimpleUtils.assertOnFail("Child location disabled the labor budget edit button on day view!",
                    forecastPage.isLaborBudgetEditBtnLoaded(),false);
            forecastPage.editLaborBudgetOnSummarySmartCard();

            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(childLocation2);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
            forecastPage.goToForecastLaborWeek();
            SimpleUtils.assertOnFail("Child location disabled the labor budget edit button on week view!",
                    forecastPage.isLaborBudgetEditBtnLoaded(),false);
            forecastPage.editLaborBudgetOnSummarySmartCard();
            forecastPage.goToForecastLaborDay();
            SimpleUtils.assertOnFail("Child location disabled the labor budget edit button on day view!",
                    forecastPage.isLaborBudgetEditBtnLoaded(),false);
            forecastPage.editLaborBudgetOnSummarySmartCard();

            //Go to the parent schedule, check the labor edit budget button
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(location);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
            forecastPage.goToForecastLaborWeek();
            SimpleUtils.assertOnFail("Parent location shows the labor budget edit button on week view!",
                    !(forecastPage.isLaborBudgetEditBtnLoaded()),false);
            forecastPage.goToForecastLaborDay();
            SimpleUtils.assertOnFail("Parent location shows the labor budget edit button on day view!",
                    !(forecastPage.isLaborBudgetEditBtnLoaded()),false);
                    
        }catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
