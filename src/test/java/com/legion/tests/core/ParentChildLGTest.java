package com.legion.tests.core;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
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
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ParentChildLGTest extends TestBase {

    private static HashMap<String, String> parameterMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
    private HashMap<String, Object[][]> swapCoverCredentials = null;
    private List<String> swapCoverNames = null;
    private String workRoleName = "";
    private static String Location = "Location";
    private static String District = "District";
    private static String Region = "Region";
    private static String BusinessUnit = "Business Unit";

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
    @TestName(description = "Validate the generation of parent child LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheGenerationOfParentChildLGScheduleAsInternalAdmin(String username, String password, String browser, String location)
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
            //Verify manager can edit operating hours before generate schedule
            createSchedulePage.selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(null);
            List<String> toCloseDays = new ArrayList<>();
            newShiftPage.editOperatingHoursOnScheduleOldUIPage("05:00am", "11:00pm", toCloseDays);
            String operatingHrs = toggleSummaryPage.getOperatingHrsValue("Sun").get("ScheduleOperatingHrs");
            String expectOperatingHrs = "5AM-11PM";
            SimpleUtils.assertOnFail("The expected operating hrs is"+expectOperatingHrs+", but actual is:"+operatingHrs,
                    operatingHrs.contains(expectOperatingHrs), false);
            //Verify manager can search and select locations on operating hours page when generate schedule
            //Verify manager can search and select location on edit budget page when generate schedule  https://legiontech.atlassian.net/browse/SCH-4835
            //Verify manager can generate schedule successfully
            createSchedulePage.createLGScheduleWithGivingTimeRange("06:00am", "06:00am");
            scheduleMainPage.goToToggleSummaryView();
            expectOperatingHrs = "6AM-6AM";
            operatingHrs = toggleSummaryPage.getOperatingHrsValue("Sun").get("ScheduleOperatingHrs");
            SimpleUtils.assertOnFail("The expected operating hrs is Aam-11pm, but actual is:"+operatingHrs,
                    operatingHrs.contains(expectOperatingHrs), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate mananger cannot edit operating hours when disable it's Manage Working Hours Setting permission for Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateManagerCannotEditOperatingHoursWhenDisableItsManageWorkingHoursSettingPermissionForParentChildLGAsInternalAdmin(String username, String password, String browser, String location)
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
    @TestName(description = "Validate all smart cards display correctly after generate parent child schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAllSmartCardsDisplayCorrectlyAfterGenerateParentChildScheduleAsInternalAdmin (String username, String password, String browser, String location)
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
            //verifyScheduleFunctionalityScheduleSmartCard
            ArrayList<LinkedHashMap<String, Float>> scheduleOverviewAllWeekHours = new ArrayList<LinkedHashMap<String, Float>>();
            HashMap<String, Float> scheduleSmartCardHoursWages = new HashMap<>();
            HashMap<String, Float> overviewData = new HashMap<>();

            scheduleSmartCardHoursWages = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
            SimpleUtils.report("scheduleSmartCardHoursWages :"+scheduleSmartCardHoursWages);
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            scheduleOverviewPage.clickOverviewTab();
            List<WebElement> scheduleOverViewWeeks =  scheduleOverviewPage.getOverviewScheduleWeeks();
            overviewData = scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(0));
            SimpleUtils.report("overview data :"+scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(2)));
            if (
//                Math.abs(overviewData.get("guidanceHours") - (scheduleSmartCardHoursWages.get("budgetedHours"))) <= 0.05 &&
                    Math.abs(overviewData.get("scheduledHours") - (scheduleSmartCardHoursWages.get("scheduledHours"))) <= 0.05
                            && Math.abs(overviewData.get("otherHours") - (scheduleSmartCardHoursWages.get("otherHours"))) <= 0.05) {
                SimpleUtils.pass("Schedule/Budgeted smartcard-is showing the values in Hours and wages, it is displaying the same data as overview page have for the current week .");
            }else {
                SimpleUtils.fail("Scheduled Hours and Overview Schedule Hours not same, hours on smart card for budget, scheduled and other are: " +
                        scheduleSmartCardHoursWages.get("budgetedHours") + ", " + scheduleSmartCardHoursWages.get("scheduledHours") + ", " + scheduleSmartCardHoursWages.get("otherHours")
                        + ". But hours on Overview page are: " + overviewData.get("guidanceHours") + ", " + overviewData.get("scheduledHours") + ", " + overviewData.get("otherHours"),false);
            }

            goToSchedulePageScheduleTab();
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
    @TestName(description = "Validate the buttons on Parent Child LG schedule page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheButtonsOnParentChildLGSchedulePageAsInternalAdmin(String username, String password, String browser, String location)
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


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TM cannot have a shift in more than one location without buffer time for Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMCannotHaveShiftInMoreThanOneLocationWithoutBufferTimeForParentChildLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
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
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            //Go to day view, check for TM: A at child location 1, has one shift, ex: 6am - 8am
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
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
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
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
            newShiftPage.searchWithOutSelectTM(firstNameOfTM);
            String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
            SimpleUtils.assertOnFail("30 mins travel time needed violation message fail to load!",
                    !shiftWarningMessage.toLowerCase().contains("30 mins travel time needed"), false);
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM);
            if(newShiftPage.ifWarningModeDisplay()){
//                String warningMessage = newShiftPage.getWarningMessageFromWarningModal();
//                if (warningMessage.contains("0 mins travel time needed violation")){
//                    SimpleUtils.pass("30 mins travel time needed violation message displays");
//                } else {
//                    SimpleUtils.fail("There is no '30 mins travel time needed violation' warning message displaying", false);
//                }
                shiftOperatePage.clickOnAssignAnywayButton();
            }
//            else {
//                SimpleUtils.fail("There is no '30 mins travel time needed violation' warning modal displaying!",false);
//            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
            SimpleUtils.assertOnFail("'30 mins travel time needed violation' compliance message display failed",
                    !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(1)).contains("Max shift per day violation") , false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of auto open shift for Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfAutoOpenShiftForParentChildLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

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
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM, location);
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
    @TestName(description = "Validate the function of auto open shift in day view for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfAutoOpenShiftInDayViewForParentChildLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
            String lastNameOfTM = shiftInfo.get(5);
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

            //Create auto open shift.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, null, childLocation,
                    "8am", "2pm", 1, Arrays.asList(0),
                    ScheduleTestKendraScott2.staffingOption.OpenShift.getValue(),
                    null, null);
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

            //Edit meal break
//            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
//            shiftOperatePage.verifyEditMealBreakTimeFunctionalityForAShift(true, selectedShift);
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
            newShiftPage.searchTeamMemberByNameNLocation(firstNameOfTM+ " "+lastNameOfTM, location);
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

            //change work role
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            shiftOperatePage.clickOnChangeRole();
            shiftOperatePage.verifyChangeRoleFunctionality();
            //check the work role by click Apply button
            shiftOperatePage.changeWorkRoleInPrompt(true);

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
    @TestName(description = "Validate the function of manual open shift for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfManualOpenShiftForParentChildLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
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
    @TestName(description = "Validate the function of manual open shift in day view for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfManualOpenShiftInDayViewForParentChildLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
    @TestName(description = "Validate the function of Assign TM shift for Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionOfAssignTMShiftForParentChildLGAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
            newShiftPage.searchTeamMemberByName(firstNameOfTM2);
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
    @TestName(description = "Verify Assign TM when TM has time off on that day for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAssignTMWhenTMHasTimeOffThatDayForParentChildLGAsStoreManager(String browser, String username, String password, String location) throws Exception {
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
            String nickNameFromProfile = profileNewUIPage.getNickNameFromProfile();
            String myProfileLabel = "My Profile";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
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
            profileNewUIPage.getNickNameFromProfile();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
            profileNewUIPage.cancelAllTimeOff();
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Haya/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Assign TM when TM has max no. of shifts scheduled for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAssignTMWhenTMHasMaxNoOfShiftsScheduledForParentChildAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("6:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
            newShiftPage.clickOnCreateOrNextBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("11:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
            newShiftPage.clickOnCreateOrNextBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("2:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("12:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint("5:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("3:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM);
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
    @TestName(description = "Verify Assign TM when TM has overlapping violation for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAssignTMWhenTMHasOverlappingViolationForParentChildAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
//            String firstNameOfTM = "Rosendo";
//            String workRole = "Team Member Corporate-Theatre";
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
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(1));
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+" "+lastName);
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
            newShiftPage.searchWithOutSelectTM(firstNameOfTM+ " "+lastName);
            String shiftWarningMessage = shiftOperatePage.getTheMessageOfTMScheduledStatus();
            SimpleUtils.assertOnFail("Overlapping violation message fail to load! The actual message is: "+shiftWarningMessage,
                    shiftWarningMessage.toLowerCase().contains(shiftStartTime.toLowerCase()) && shiftWarningMessage.toLowerCase()
                    .contains(shiftEndTime.toLowerCase()), false);
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
    @TestName(description = "Validate the group by dropdown list for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheGroupByDropdownListForParentChildAsInternalAdmin(String username, String password, String browser, String location)
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
    @TestName(description = "Validate the filter on schedule page for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFilterOnSchedulePageForParentChildAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
    @TestName(description = "Verify the function of copy schedule for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheFunctionOfCopyScheduleForParentChildAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToPreviousWeek();
            Thread.sleep(3000);
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
                SimpleUtils.fail("Verified Compliance is inconsistent with the copied schedule!", false);
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
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.clickOnFilterBtn();
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
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Print Schedule for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validatePrintScheduleForParentChildAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();

            // Verify the LG schedule can be printed and the shift display correctly in print file in week view
            /// Go to one generated schedule
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if(!isActiveWeekGenerated){
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
            PdfReader reader = new PdfReader(downloadPath+"\\"+fileName);
            String content = PdfTextExtractor.getTextFromPage(reader, 1);

            /// Get scheduled hours and shifts count for one sub location on schedule page
            String subLocation = content.split(" ")[0].split("\n")[0];
            getDriver().switchTo().window(handle);
            scheduleMainPage.selectLocationFilterByText(subLocation);
            HashMap<String, String> hoursOnSchedule = smartCardPage.getHoursFromSchedulePage();
            int shiftsCount = scheduleShiftTablePage.getShiftsCount();

            /// Compare the data for one sub location in printed file and schedule page
            if (content.contains(hoursOnSchedule.get("Scheduled")) && content.contains(""+shiftsCount)) {
                SimpleUtils.report("The scheduled hours of " + subLocation+ " is " + hoursOnSchedule.get("Scheduled") + " Hrs");
                SimpleUtils.report("The shifts count  of " + subLocation + " is " + shiftsCount + " Shifts");
                SimpleUtils.pass("Schedule page: The content in printed file in week view displays correctly");
            } else
                SimpleUtils.fail("Schedule page: The content in printed file in week view displays incorrectly",false);

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
            reader = new PdfReader(downloadPath+"\\"+fileName);
            content = PdfTextExtractor.getTextFromPage(reader, 1);

            /// Get scheduled hours and shifts count for one sub location on schedule page
            subLocation = content.split(" ")[0];
            getDriver().switchTo().window(handle);
            scheduleMainPage.selectLocationFilterByText(subLocation);
            hoursOnSchedule = smartCardPage.getHoursFromSchedulePage();
            shiftsCount = scheduleShiftTablePage.getAvailableShiftsInDayView().size();

            /// Compare the data for one sub location in printed file and schedule page
            if (content.contains(hoursOnSchedule.get("Scheduled")) && content.contains(""+shiftsCount)) {
                SimpleUtils.report("The scheduled hours of " + subLocation+ " is " + hoursOnSchedule.get("Scheduled") + " Hrs");
                SimpleUtils.report("The shifts count  of " + subLocation + " is " + shiftsCount + " Shifts");
                SimpleUtils.pass("Schedule page: The content in printed file in day view displays correctly");
            } else
                SimpleUtils.fail("Schedule page: The content in printed file in day view displays incorrectly",false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate UI performance for large roster (500 employees) with one location as well as multiple location groups")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateUIPerformanceForLargeRosterAsInternalAdminPC (String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeDistrict("District Whistler");
            locationSelectorPage.changeLocation("Lift Ops_Parent");

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
            createSchedulePage.createLGScheduleWithGivingTimeRange("6am", "6am");

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
    @TestName(description = "Validate that operate parent child LG schedule by different user")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateOperateParentChildLGScheduleByDifferentUserAsInternalAdmin (String username, String password, String browser, String location) throws Exception {
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
            indexes.add(0);
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
    @TestName(description = "Verify the Budget Hours smart card when SM with Manage Budget permission for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateBudgetHourSmartCardWhenSMWithManageBudgetPermissionForParentChildLGAsInternalAdmin(String username, String password, String browser, String location)
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
    @TestName(description = "Verify automatically expand when clicking group by on Parent/Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAutomaticallyExpandWhenGroupByInPCLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
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
    @TestName(description = "Validate TMs Can Receive and Accept Offers for Parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEmployeeCanReceiveAndAcceptOffersForParentChildLGAsTeamMember(String browser, String username, String password, String location) throws Exception {
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
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            String lastName = tmFullName.split(" ")[1];
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
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnFilterBtn();
            List<String> childLocationNames = scheduleMainPage.getSpecificFilterNames("location");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            Thread.sleep(5000);
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            SimpleUtils.assertOnFail("Offer TMs option should be enabled!", shiftOperatePage.isOfferTMOptionEnabled(), false);
            shiftOperatePage.clickOnOfferTMOption();
            newShiftPage.searchTeamMemberByNameNLocation(firstName+ " "+lastName, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            shiftOperatePage.clickOnProfileIconOfOpenShift();
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstName, "offered");
            shiftOperatePage.closeViewStatusContainer();
            loginPage.logOut();

            // 3.Login with the TM to claim the shift
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            String cardName = "WANT MORE HOURS?";
            SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
            String linkName = "View Shifts";
            smartCardPage.clickLinkOnSmartCardByName(linkName);
            SimpleUtils.assertOnFail("Open shifts not loaed Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage(Constants.ClaimSuccessMessage);
            loginPage.logOut();

            // 4.Login with SM to check activity
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
    //        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
    //        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
    //        activityPage.verifyActivityBellIconLoaded();
    //        activityPage.verifyClickOnActivityIcon();
    //        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
    //        activityPage.verifyActivityOfShiftOffer(teamMemberName, "");
    //        activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());

            //Check the shift been scheduled
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(3000);
            int tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstName);
            SimpleUtils.assertOnFail("The expect shift count is 0, the actual open shift count is:"+tmShiftCount,
                    tmShiftCount >= 1, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Prepare the data for swap")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void prepareTheSwapShiftsAsInternalAdminPC(String browser, String username, String password, String location) throws Exception {
        try {
            swapCoverNames = new ArrayList<>();
            swapCoverCredentials = getSwapCoverUserCredentials(location);
            for (Map.Entry<String, Object[][]> entry : swapCoverCredentials.entrySet()) {
                swapCoverNames.add(entry.getKey());
            }
            workRoleName = String.valueOf(swapCoverCredentials.get(swapCoverNames.get(0))[0][3]);

            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
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
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            // Deleting the existing shifts for swap team members
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.selectGroupByFilter("Group by All");
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(swapCoverNames.get(0));
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(swapCoverNames.get(0));
            scheduleMainPage.searchShiftOnSchedulePage(swapCoverNames.get(1));
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(swapCoverNames.get(1));
            scheduleMainPage.clickOnCloseSearchBoxButton();
            if(smartCardPage.isRequiredActionSmartCardLoaded()){
                smartCardPage.clickOnViewShiftsBtnOnRequiredActionSmartCard();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.clickOnFilterBtn();
                scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
                scheduleMainPage.clickOnFilterBtn();
            }
            scheduleMainPage.saveSchedule();
            // Add the new shifts for swap team members
            Thread.sleep(5000);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.addNewShiftsByNames(swapCoverNames, workRoleName);
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate TMs Can Receive and Accept Swap request for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeesCanSwapShiftsForParentChildLGAsTeamMember(String browser, String username, String password, String location) throws Exception {
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
//        String firstName = "Aron";
//        String firstName2= "Gianni";
//        String childLocation2 = "Child002";
//        String childLocation1= "Child001";

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




    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the location filter on Demand tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheLocationFilterOnDemandTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(getCrendentialInfo("LGInfo"));
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            Thread.sleep(3000);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
            ForecastPage forecastPage = pageFactory.createForecastPage();

            //Location filter dropdown will display on the Demand tab in forecast page
            SimpleUtils.assertOnFail("Location filter dropdown fail to load on the Demand tab in forecast page! ",
                    forecastPage.checkIsLocationFilterLoaded(), false);

            //All locations been selected in locations filter and get all info in smart card
            SimpleUtils.assertOnFail("All locations should be selected in filter dropdown by default! ",
                    forecastPage.checkIfAllLocationBeenSelected(), false);
            HashMap<String, Float> forecastDataForAllLocations= forecastPage.getInsightDataInShopperWeekView();
            float peakDemand = forecastDataForAllLocations.get("peakDemand");
            float totalDemand = forecastDataForAllLocations.get("totalDemand");
            //The relevant info will display when user filters the location
            List<String> locations = forecastPage.getAllLocationsFromFilter();
            int index = (new Random()).nextInt(locations.size());
            forecastPage.checkOrUncheckLocationInFilter(false, locations.get(index));
            HashMap<String, Float> forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The peak demand for all locations should more than or equal to the value for part of locations! ",
                    peakDemand >=forecastDataForPartOfLocations.get("peakDemand"), false);
            SimpleUtils.assertOnFail("The total demand for all locations should more than or equal to the value for part of locations! ",
                    totalDemand >=forecastDataForPartOfLocations.get("totalDemand"), false);

            forecastPage.checkOrUncheckLocationInFilter(true, locations.get(index));
            forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The peak demand for all locations should equal with before! ",
                    peakDemand == forecastDataForPartOfLocations.get("peakDemand"), false);
            SimpleUtils.assertOnFail("The total demand for all locations should equal with before! ",
                    totalDemand == forecastDataForPartOfLocations.get("totalDemand"), false);

            //Verify locations filter in day view
            scheduleCommonPage.clickOnDayView();

            //Location filter dropdown will display on the Demand tab in forecast page
            SimpleUtils.assertOnFail("Location filter dropdown fail to load on the Demand tab in forecast page! ",
                    forecastPage.checkIsLocationFilterLoaded(), false);

            //All locations been selected in locations filter and get all info in smart card
            SimpleUtils.assertOnFail("All locations should be selected in filter dropdown by default! ",
                    forecastPage.checkIfAllLocationBeenSelected(), false);
            forecastDataForAllLocations= forecastPage.getInsightDataInShopperWeekView();
            peakDemand = forecastDataForAllLocations.get("peakDemand");
            totalDemand = forecastDataForAllLocations.get("totalDemand");
            //The relevant info will display when user filters the location
            locations = forecastPage.getAllLocationsFromFilter();
            index = (new Random()).nextInt(locations.size());
            forecastPage.checkOrUncheckLocationInFilter(false, locations.get(index));
            forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The peak demand for all locations should more than or equal to the value for part of locations! ",
                    peakDemand >=forecastDataForPartOfLocations.get("peakDemand"), false);
            SimpleUtils.assertOnFail("The total demand for all locations should more than or equal to the value for part of locations! ",
                    totalDemand >=forecastDataForPartOfLocations.get("totalDemand"), false);

            forecastPage.checkOrUncheckLocationInFilter(true, locations.get(index));
            forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The peak demand for all locations should equal with before! ",
                    peakDemand == forecastDataForPartOfLocations.get("peakDemand"), false);
            SimpleUtils.assertOnFail("The total demand for all locations should equal with before! ",
                    totalDemand == forecastDataForPartOfLocations.get("totalDemand"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }


    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the location filter on Labor tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheLocationFilterOnLabelTabAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.searchSpecificUpperFieldAndNavigateTo(getCrendentialInfo("LGInfo"));
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
            ForecastPage forecastPage = pageFactory.createForecastPage();
            Thread.sleep(10000);
            forecastPage.clickOnLabor();
            //Location filter dropdown will display on the Labor tab in forecast page
            SimpleUtils.assertOnFail("Location filter dropdown fail to load on the Labor tab in forecast page! ",
                    forecastPage.checkIsLocationFilterLoaded(), false);

            //All locations been selected in locations filter and get all info in smart card
            SimpleUtils.assertOnFail("All locations should be selected in filter dropdown by default! ",
                    forecastPage.checkIfAllLocationBeenSelected(), false);
            HashMap<String, Float> forecastDataForAllLocations= forecastPage.getInsightDataInShopperWeekView();
            float forecastHrs = forecastDataForAllLocations.get("ForecastHrs");
            //The relevant info will display when user filters the location
            List<String> locations = forecastPage.getAllLocationsFromFilter();
            int index = (new Random()).nextInt(locations.size());
            forecastPage.checkOrUncheckLocationInFilter(false, locations.get(index));
            HashMap<String, Float> forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The Forecast Hrs for all locations should more than or equal to the value for part of locations! ",
                    forecastHrs >=forecastDataForPartOfLocations.get("ForecastHrs"), false);

            forecastPage.checkOrUncheckLocationInFilter(true, locations.get(index));
            forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The Forecast Hrs for all locations should more than or equal to the value for part of locations! ",
                    forecastHrs == forecastDataForPartOfLocations.get("ForecastHrs"), false);

            //Verify locations filter in day view
            scheduleCommonPage.clickOnDayView();

            //Location filter dropdown will display on the Labor tab in forecast page
            SimpleUtils.assertOnFail("Location filter dropdown fail to load on the Labor tab in forecast page! ",
                    forecastPage.checkIsLocationFilterLoaded(), false);

            //All locations been selected in locations filter and get all info in smart card
            SimpleUtils.assertOnFail("All locations should be selected in filter dropdown by default! ",
                    forecastPage.checkIfAllLocationBeenSelected(), false);
            forecastDataForAllLocations= forecastPage.getInsightDataInShopperWeekView();
            forecastHrs = forecastDataForAllLocations.get("ForecastHrs");
            //The relevant info will display when user filters the location
            locations = forecastPage.getAllLocationsFromFilter();
            index = (new Random()).nextInt(locations.size());
            forecastPage.checkOrUncheckLocationInFilter(false, locations.get(index));
            forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The Forecast Hrs for all locations should more than or equal to the value for part of locations! ",
                    forecastHrs >=forecastDataForPartOfLocations.get("ForecastHrs"), false);

            forecastPage.checkOrUncheckLocationInFilter(true, locations.get(index));
            forecastDataForPartOfLocations= forecastPage.getInsightDataInShopperWeekView();
            SimpleUtils.assertOnFail("The Forecast Hrs for all locations should more than or equal to the value for part of locations! ",
                    forecastHrs ==forecastDataForPartOfLocations.get("ForecastHrs"), false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the assign TMs workflow for new create shift UI on Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheAssignTMsWorkFlowForNewCreateShiftUIOnParentChildLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
//            SimpleUtils.assertOnFail("New create shift page is not display! ",
//                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
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
                    shiftsOfOneDay.size()>=1, false);
            Thread.sleep(10000);
            scheduleMainPage.saveSchedule();
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    shiftsOfOneDay.size()>=1, false);
            createSchedulePage.publishActiveSchedule();
            locations = scheduleShiftTablePage.getGroupByOptionsStyleInfo();
            SimpleUtils.assertOnFail("It should has one location display, but actual is has :"+locations.size(),
                    locations.size() ==1, false);
            SimpleUtils.assertOnFail("It should has "+count+1+" shifts display, but actual is has :"+scheduleShiftTablePage.getShiftsCount(),
                    shiftCount == scheduleShiftTablePage.getShiftsCount() -1, false);
            shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    shiftsOfOneDay.size()>=1, false);

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
    @TestName(description = "Validate the manuel offer TMs workflow for new create shift UI on Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheManuelOfferTMsWorkFlowForNewCreateShiftUIOnParentChildLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createLGScheduleWithGivingTimeRange("6am", "12am");
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
//            SimpleUtils.assertOnFail("New create shift page is not display! ",
//                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
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
            Thread.sleep(10000);
            scheduleMainPage.saveSchedule();
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
    @TestName(description = "Validate the auto offer TMs workflow for new create shift UI on Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheAutoOfferTMsWorkFlowForNewCreateShiftUIOnParentChildLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            Thread.sleep(5000);
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
//            SimpleUtils.assertOnFail("New create shift page is not display! ",
//                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
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
    @TestName(description = "Validate open shifts will been created when drag and drop shifts to same day and same location on parent child LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyBulkDragAndDropShiftsToSameDayAndSameLocationOnParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
    @TestName(description = "Verify shifts can be moved to same day and another location on parent child LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyMoveShiftsToSameDayAndAnotherLocationOnParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content on Multiple Edit Shifts window for Parent/Child location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOnMultipleEditShiftsWindowForPCAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify shifts can be copyed to same day and another location on parent child LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCopyShiftsToSameDayAndAnotherLocationOnParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
    @TestName(description = "Validate shifts can be moved to another day and another location on parent child LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyMoveShiftsToAnotherDayAndAnotherLocationOnParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
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
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
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
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()>0, false);
            }
            //Check the shifts are not display on first location
            scheduleShiftTablePage.expandSpecificCountGroup(1);
            for (int i=0; i< selectedShiftCount;i++) {
                SimpleUtils.assertOnFail("Bulk Drag and drop: the shifts fail to be moved! ",
                        scheduleShiftTablePage.getOneDayShiftByName(2, shiftNames.get(i)).size()==0, false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate shifts can be copied to another day and another location on parent child LG schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCopyShiftsToAnotherDayAndAnotherLocationOnParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            createSchedulePage.createLGScheduleWithGivingTimeRange("08:00am", "09:00pm");
//            if (scheduleShiftTablePage.getAllShiftsOfOneTM("open").size()>0) {
//                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
//                scheduleMainPage.saveSchedule();
//            }
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
    @TestName(description = "Validate navigation, roster, schedule and dashboard of Parent Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void validateNavigationRosterScheduleAndDashboardOfParentChildLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            goToSchedulePageScheduleTab();
            //Verify the all slave locations will be generated when generate the master location for Master-Slave LG
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createLGScheduleWithGivingTimeRange("06:00am", "06:00am");
            if(smartCardPage.isRequiredActionSmartCardLoaded()) {
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
                scheduleMainPage.saveSchedule();
            }
            createSchedulePage.publishActiveSchedule();
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
            //Verify the roster can be at master or slave location level for Master-Slave LG
            SimpleUtils.assertOnFail("The parent location roster count is:"+parentRosterCount,
                    parentRosterCount > 0, false);
            String tmName = teamPage.selectATeamMemberToViewProfile();
            //Verify the SM dashboard of P2P LG, SM Dashboard will aggregate data for the widgets
            dashboardPage.clickOnDashboardConsoleMenu();
            Thread.sleep(3000);
            int upcomingShiftCountOnParentSMDashboard = dashboardPage.getUpComingShifts().size();
            SimpleUtils.assertOnFail("The parent location upcoming shift count is:"+upcomingShiftCountOnParentSMDashboard,
                    upcomingShiftCountOnParentSMDashboard == 8
                            || upcomingShiftCountOnParentSMDashboard == 0, false);
            //Verify all sub-locations of master-slave LG have their own demand forecasts/labor forecast/staffing guidance
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            forecastPage.clickOnShopper();
            forecastPage.verifyDemandForecastCanLoad();
            forecastPage.clickOnLabor();
            forecastPage.verifyLaborForecastCanLoad();

            //Verify TMs in parent location can be searched out on other locations for Master-Slave LG
            if (!tmName.equalsIgnoreCase("")) {
                goToSchedulePageScheduleTab();
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmName);
                createShiftsWithSpecificValues(workRole, null, locationNames.get(0), "9:00am", "12:00pm",
                        1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), null, tmName);
                List<WebElement> tmShifts = scheduleShiftTablePage.getAllShiftsOfOneTM(tmName.split(" ")[0]);
                SimpleUtils.assertOnFail("TM "+ tmName+"'s shift should be created! ",
                        tmShifts.size()>0,false);
                scheduleMainPage.clickOnCancelButtonOnEditMode();
            }else
                SimpleUtils.fail("The TM from parent child LG roster should not be null! ", false);

            //Verify the all slave location will be ungenerated when ungenerate the master location for Master-Slave LG
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            SimpleUtils.assertOnFail("The schedule should be ungenerate successfully! ",
                    !createSchedulePage.isWeekGenerated() && createSchedulePage.isGenerateButtonLoaded(), false);
            //Verify the location navigation of Master-Slave LG
            List<String> allLocations = locationSelectorPage.getAllUpperFieldNamesInUpperFieldDropdownList(Location);
            SimpleUtils.assertOnFail("Parent location should display in location navigation list",
                    allLocations.contains(location), false);
            for (int i=0;i< locationNames.size(); i++){
                SimpleUtils.assertOnFail("Child location "+ locationNames.get(i)+" should not display in location navigation list: "+ allLocations,
                        !allLocations.contains(locationNames.get(i)), false);
            }

            //Verify all sub-locations of master-slave LG have their own operating hours
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

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of \"Edit Operating hours\" option for location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEditOperatingHoursForLGAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();

            //Go to the schedule page
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //The P2P location has a dropdown list which includes child locations
            scheduleMainPage.goToToggleSummaryView();
            scheduleMainPage.checkOperatingHoursOnToggleSummary();
            SimpleUtils.assertOnFail("The drop down list is unavailable!", locationSelectorPage.isChangeLocationButtonLoaded(), false);

            //Compare the operating hours between Editing and Toggle Summary pages
            scheduleMainPage.goToToggleSummaryView();
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            Thread.sleep(3000);
            String childLocation = "Child002";
            createSchedulePage.selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(childLocation);
            scheduleMainPage.checkOperatingHoursOnToggleSummary();
            scheduleMainPage.clickEditBtnOnToggleSummary();
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation);
            scheduleMainPage.checkOperatingHoursOnEditDialog();

            //Go to Edit Operating Hours page and check relevant content
            scheduleMainPage.isSaveBtnLoadedOnEditOpeHoursPageForOP();
            scheduleMainPage.isCancelBtnLoadedOnEditOpeHoursPageForOP();

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Cosimo")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of SAVE button on \"Edit operating hours\" window for location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEditOperatingHoursFunctionForLGAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();

            //Go to the schedule page
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //Edit the operating day and cancel all actions.
            scheduleMainPage.goToToggleSummaryView();
            scheduleMainPage.clickEditBtnOnToggleSummary();
            String childLocation1 = "Child001";
            String childLocation2 = "Child002";
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation2);
            List<String> weekDay = new ArrayList<>(Arrays.asList("Sunday"));
            scheduleMainPage.closeTheParticularOperatingDay(weekDay);
            scheduleMainPage.openTheParticularOperatingDay(weekDay);
            scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDay, "10:00AM","10:00PM");
            scheduleMainPage.clickCancelBtnOnEditOpeHoursPageForOP();
            scheduleMainPage.checkOperatingHoursOnToggleSummary();

            //Edit the operating day and save all actions.
            scheduleMainPage.clickEditBtnOnToggleSummary();
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation1);
            List<String> weekDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
            scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "10:00AM","10:00PM");
            scheduleMainPage.clickSaveBtnOnEditOpeHoursPageForOP();
            scheduleMainPage.clickEditBtnOnToggleSummary();
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation2);
            scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "10:00AM","10:00PM");
            scheduleMainPage.clickSaveBtnOnEditOpeHoursPageForOP();

            scheduleMainPage.goToToggleSummaryView();
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(childLocation1);
            scheduleMainPage.checkOpeHrsOfParticualrDayOnToggleSummary(weekDays, "10AM-10PM");
            createSchedulePage.closeSearchBoxForLocations();
            createSchedulePage.selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(childLocation2);
            scheduleMainPage.checkOpeHrsOfParticualrDayOnToggleSummary(weekDays, "10AM-10PM");
            createSchedulePage.closeSearchBoxForLocations();

            //Check the time duration on the day view
            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdate();
            scheduleCommonPage.clickOnDayView();
            ArrayList<String> timeDurations = scheduleShiftTablePage.getScheduleDayViewGridTimeDuration();
            String timeDuration = timeDurations.get(0) + "-" + timeDurations.get(timeDurations.size()-1);
            SimpleUtils.assertOnFail("The time duration is not matched between day view and toggle summary view!", timeDuration.equalsIgnoreCase("8 AM-12 AM"), false);

            //Check the closed operating day.
            scheduleMainPage.goToToggleSummaryView();
            scheduleMainPage.clickEditBtnOnToggleSummary();
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation1);
            scheduleMainPage.closeTheParticularOperatingDay(weekDays);
            scheduleMainPage.clickSaveBtnOnEditOpeHoursPageForOP();
            Thread.sleep(3000);

            scheduleMainPage.clickEditBtnOnToggleSummary();
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation2);
            scheduleMainPage.closeTheParticularOperatingDay(weekDays);
            scheduleMainPage.clickSaveBtnOnEditOpeHoursPageForOP();
            Thread.sleep(3000);

            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(FTSERelevantTest.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.clickOnWeekView();
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(childLocation1);
            scheduleMainPage.checkClosedDayOnToggleSummary(weekDays);
            createSchedulePage.closeSearchBoxForLocations();
            createSchedulePage.selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(childLocation2);
            scheduleMainPage.checkClosedDayOnToggleSummary(weekDays);
            createSchedulePage.closeSearchBoxForLocations();

            createSchedulePage.createScheduleForNonDGFlowNewUIWithoutUpdate();
            scheduleCommonPage.clickOnWeekView();
            int shiftCountWeek = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The schedule is not empty!", shiftCountWeek == 0, false);
            scheduleCommonPage.clickOnDayView();
            int shiftCountDay = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The current day is not empty!", shiftCountDay == 0, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        } finally {
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            String childLocation1 = "Child001";
            String childLocation2 = "Child002";
            List<String> weekDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
            scheduleMainPage.goToToggleSummaryView();
            scheduleMainPage.clickEditBtnOnToggleSummary();
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation1);
            scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "08:00AM","09:00PM");
            scheduleMainPage.clickSaveBtnOnEditOpeHoursPageForOP();
            Thread.sleep(3000);

            scheduleMainPage.clickEditBtnOnToggleSummary();
            createSchedulePage.selectLocationOnEditOperatingHoursPage(childLocation2);
            scheduleMainPage.editTheOperatingHoursWithFixedValue(weekDays, "08:00AM","09:00PM");
            scheduleMainPage.clickSaveBtnOnEditOpeHoursPageForOP();
            Thread.sleep(3000);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of changing location on Parent/Child")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingLocationForPCOnMultipleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            List<String> locations = toggleSummaryPage.getChildLocationList();
            createSchedulePage.createScheduleForNonDGFlowNewUI();

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
                    SimpleUtils.fail("Child location list is incorrect! The expected is: "+locations.get(i)
                            +" The actual is: "+actualLocations.get(i), false);
                    break;
                }
            }
            // Verify can change the location without selecting the two options
            editShiftPage.selectSpecificOptionByText(actualLocations.get(1));
            editShiftPage.clickOnUpdateButton();
            mySchedulePage.verifyThePopupMessageOnTop("Success");
            // Verify the shifts are moved to the selected child location
            scheduleMainPage.selectGroupByFilter(actualLocations.get(1));
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


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify TM cannot have a shift in more than one location without buffer time for P2P LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTMCannotHaveShiftInMoreThanOneLocationWithoutBufferTimeForParentChildAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
            if(!isActiveWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
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
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectChildLocInCreateShiftWindow(childLocationNames.get(0));
            newShiftPage.moveSliderAtCertainPoint("8:00am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("6:00am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM+" "+lastName);
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

            newShiftPage.searchWithOutSelectTM(firstNameOfTM+" "+lastName);
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
                    SimpleUtils.fail("There is no "+expectedWaningMessage+" warning message displaying。 The actual is:"+warningMessage, false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else {
                SimpleUtils.fail("There is no '"+expectedWaningMessage+"' warning modal displaying!",false);
            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM);
            expectedWaningMessage = "Minimum time between shifts";
            String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(0)).toString();
            SimpleUtils.assertOnFail("'"+expectedWaningMessage+"' compliance message display failed, the actual message is:"+actualMessage,
                    actualMessage.contains(expectedWaningMessage) , false);
            actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(1)).toString();
            SimpleUtils.assertOnFail("'"+expectedWaningMessage+"' compliance message display failed, the actual message is:"+actualMessage,
                    actualMessage.contains(expectedWaningMessage) , false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate employee can acknowledge the notification for LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeeCanAcknowledgeTheNotificationForLGAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName = tmFullName.split(" ")[0];
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String smartCardName = "SCHEDULE ACKNOWLEDGEMENT";
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will not display after generate but not publish schedule
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should not display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will not display after edit but not publish schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            createShiftsWithSpecificValues(workRole, "", "", "9:00am", "12:00pm",
                    1, Arrays.asList(1), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(), "", firstName);
            scheduleMainPage.saveSchedule();
            //https://legiontech.atlassian.net/browse/SCH-8043
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should not display before publish schedule! ",
                    smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            //Verify the SCHEDULE ACKNOWLEDGEMENT smart card will display after publish schedule
            createSchedulePage.publishActiveSchedule();
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    smartCardPage.isSpecificSmartCardLoaded(smartCardName), false);
            //Get count before acknowledge
            int pendingEmployeeCountBeforeAcknowledge = smartCardPage.getCountFromSmartCardByName(smartCardName);

            //Login as employee
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage.clickOnProfileIconOnDashboard();
            dashboardPage.clickOnSwitchToEmployeeView();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();

            //check ACTION REQUIRED smart card display
            String acknowledgeNotificationMessage = "Please review and acknowledge receiving your schedule below.";
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED")
                            && smartCardPage.isSmartCardAvailableByLabel(acknowledgeNotificationMessage), false);
            smartCardPage.clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard();
            refreshPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            SimpleUtils.assertOnFail("The SCHEDULE ACKNOWLEDGEMENT smart card should display before publish schedule! ",
                    !smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"), false);

            //Login as admin
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
            int pendingEmployeeCountAfterAcknowledge = smartCardPage.getCountFromSmartCardByName(smartCardName);
            SimpleUtils.assertOnFail("The pending employee count display incorrectly, the expected is: "
                            + (pendingEmployeeCountBeforeAcknowledge-1) + ". The actual is: "+pendingEmployeeCountAfterAcknowledge,
                    pendingEmployeeCountAfterAcknowledge == (pendingEmployeeCountBeforeAcknowledge-1) , false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content on Edit Sigle Shifts window for Parent/Child location group")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOnEditSingleShiftWindowForParentChildAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
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
//            // Verify the selected days show correctly in day view
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
    @TestName(description = "Verify the functionality of changing location when selecting single shift on Parent/Child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyChangingLocationForPCOnSingleEditShiftsWindowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            EditShiftPage editShiftPage = pageFactory.createEditShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();

            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            goToSchedulePageScheduleTab();

            // Create schedule if it is not created
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            List<String> locations = toggleSummaryPage.getChildLocationList();
            createSchedulePage.createLGScheduleWithGivingTimeRange("06:00am", "06:00am");

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
                    SimpleUtils.fail("Child location list is incorrect! The expected is: "+locations.get(i)
                            +" The actual is: "+actualLocations.get(i), false);
                    break;
                }
            }
            // Verify can change the location without selecting the two options
            editShiftPage.selectSpecificOptionByText(actualLocations.get(1));
            editShiftPage.clickOnUpdateButton();
            if(mySchedulePage.checkIfThePopupMessageOnTop("Success")){
                SimpleUtils.pass("The the location been changed successfully! ");
            }else{
                editShiftPage.clickOnUpdateAnywayButton();
                SimpleUtils.assertOnFail("The the location fail to change! ",
                        mySchedulePage.checkIfThePopupMessageOnTop("Success"), false);
            }
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


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate TMs Can Receive and Accept Cover request for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyEmployeesCanClaimCoverRequestForParentChildLGAsTeamMember(String browser, String username, String password, String location) throws Exception {
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
        String childLocation1 = location;
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
                "8am", "2pm", 1, Arrays.asList(),
                ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                null, firstName+ " "+ lastName);

        scheduleMainPage.saveSchedule();
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

        loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
        goToSchedulePageScheduleTab();
        scheduleCommonPage.navigateToNextWeek();
        Thread.sleep(3000);
        int tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstName);
        SimpleUtils.assertOnFail("The expect shift count is 0, the actual open shift count is:"+tmShiftCount,
                tmShiftCount == 0, false);
        tmShiftCount = scheduleShiftTablePage.getShiftsNumberByName(firstName2);
        SimpleUtils.assertOnFail("The expect shift count is 0, the actual open shift count is:"+tmShiftCount,
                tmShiftCount == 1, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of drag and drop shift to same location for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropShiftToSameLocationForParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
//            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
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
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            //Verify shift can be copied to different day and same location by drag and drop
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShiftsOnOneDay(1, firstNameOfTM, 0);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM,1);
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Verify shift can be moved to different day and same location by drag and drop
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShiftsOnOneDay(1, firstNameOfTM, 0);
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
    @TestName(description = "Validate the function of drag and drop shift to different location for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropShiftToDifferentLocationForParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            scheduleCommonPage.navigateToNextWeek();
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
            Thread.sleep(3000);
            createShiftsWithSpecificValues(workRole2, "", childLocationNames.get(0), "8am", "11am", 1,
                    Arrays.asList(0), ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue(),
                    null, firstNameOfTM2+ " "+ lastName2);
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
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
                SimpleUtils.pass("Drag and drop successfully!");
            } else
                SimpleUtils.fail("Fail to drag and drop! ", false);
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
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
            scheduleCommonPage.navigateToNextWeek();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            selectedShifts = scheduleShiftTablePage.selectMultipleSameAssignmentShifts(1, firstNameOfTM);
            scheduleShiftTablePage.dragBulkShiftToAnotherDay(selectedShifts, 1, true);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("copy");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            scheduleShiftTablePage.verifyShiftIsCopiedToAnotherDay(0,firstNameOfTM,1);
            scheduleMainPage.saveSchedule();
            goToSchedulePageScheduleTab();
            scheduleCommonPage.navigateToNextWeek();
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
    @TestName(description = "Validate the function of drag and drop employee to same location for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropEmployeeToSameLocationForParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(0), "2pm", "6pm", 1,
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


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the function of drag and drop employee to different location for parent child LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyDragAndDropEmployeeToDifferentLocationForParentChildLGScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            createShiftsWithSpecificValues(workRole, "", childLocationNames.get(1), "2pm", "6pm", 1,
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
}
