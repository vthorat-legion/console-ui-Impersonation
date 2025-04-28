package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.Constants;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OvernightTest extends TestBase {
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
    @TestName(description = "Validate the overnight shift can be drag to other day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOvernightShiftsCanBeDraggedToOtherDayAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);               ;
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Operating Hours"), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectOperatingBufferHours("ContinuousOperation");
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();
            refreshCachesAfterChangeTemplate();
            //waiting for the cache
            int j =0;
            while (j< 3) {
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                Thread.sleep(60000);
                j++;
            }
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(5000);
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("12:00AM", "12:00AM");
            int i = 0;
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (i< 50 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
                i++;
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2,2,2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1+ " "+lastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(2);
            //Verify overnight shift can be created
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int index = scheduleShiftTablePage.
                    getTheIndexOfShift(scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).get(0));
            scheduleShiftTablePage.moveShiftByIndexInDayView(index, false);
            scheduleMainPage.saveSchedule();
            //Verify the overnight shift can display on next day
            scheduleCommonPage.navigateDayViewWithIndex(3);
            SimpleUtils.assertOnFail("The overnight shift also display on the next day! ",
                    scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).size()>0, false);
            //Verify the overnight shift can be dragged to other days
            scheduleCommonPage.clickOnWeekView();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.dragOneShiftToAnotherDay(2, firstNameOfTM1, 1);
            scheduleShiftTablePage.selectCopyOrMoveByOptionName("move");
            scheduleShiftTablePage.clickConfirmBtnOnDragAndDropConfirmPage();
            Thread.sleep(3000);
            scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(2,firstNameOfTM1,1);
            scheduleMainPage.saveSchedule();
            scheduleShiftTablePage.verifyShiftIsMovedToAnotherDay(2,firstNameOfTM1,1);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the shifts when enable the Continuous Operation option")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyShiftWhenEnableContinuousOperationOptionAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);               ;
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Operating Hours"), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectOperatingBufferHours("ContinuousOperation");
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();

            //Go to one schedule page day view
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6:00AM", "6:00AM");
            int i = 0;
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (i< 50 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
                i++;
            }
            String workRole = shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2,2,2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            String shiftId = scheduleShiftTablePage.getAllShiftsOfOneTM(firstNameOfTM1).get(0).getAttribute("id");
//            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(2);
            //Verify overnight shift can be created
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int index = scheduleShiftTablePage.
                    getTheIndexOfShift(scheduleShiftTablePage.getShiftById(shiftId));
            scheduleShiftTablePage.moveShiftByIndexInDayView(index, false);
            scheduleMainPage.saveSchedule();
            //Verify the overnight shift can display on next day
            scheduleCommonPage.navigateDayViewWithIndex(3);
            Thread.sleep(3000);
            SimpleUtils.assertOnFail("The overnight shift also display on the next day! ",
                    scheduleShiftTablePage.getShiftById(shiftId)!=null, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the shifts when disable the Continuous Operation option")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyShiftWhenDisableContinuousOperationOptionAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);               ;
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Operating Hours"), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectOperatingBufferHours("None");
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();
            //waiting for the cache
            refreshCachesAfterChangeTemplate();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            int j =0;
            while (j< 3) {
                scheduleCommonPage.clickOnScheduleConsoleMenuItem();
                Thread.sleep(60000);
                j++;
            }
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
            int i = 0;
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (i< 50 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
                i++;
            }
            String workRole = shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2,2,2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(2);
            //Verify overnight shift can be created
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int index = scheduleShiftTablePage.
                    getTheIndexOfShift(scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).get(0));
            scheduleShiftTablePage.moveShiftByIndexInDayView(index, false);
            scheduleMainPage.saveSchedule();
            //Verify the overnight shift can display on next day
            scheduleCommonPage.navigateDayViewWithIndex(3);
//            i =0;
            int shiftCount = scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).size();
//            while (i<5 && shiftCount> 0){
//                Thread.sleep(5000);
//                loginPage.logOut();
//                loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
//                goToSchedulePageScheduleTab();
//                scheduleCommonPage.navigateToNextWeek();
//                scheduleCommonPage.clickOnDayView();
//                scheduleCommonPage.navigateDayViewWithIndex(3);
//                shiftCount = scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).size();
//                i++;
//            }
            SimpleUtils.assertOnFail("The "+firstNameOfTM1+"'s shift should not display on the next day! ",
                    shiftCount == 0, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the TM without badge cannot claim the shift with work role that required badge")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTMWithOutBadgeCannotClaimTheShiftWithWorkRoleThatRequiredBadgeAsTeamMember(String username, String password, String browser, String location) throws Exception {
        try {
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            profileNewUIPage.clickOnUserProfileImage();
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage("My Profile");
            String tmFullName = profileNewUIPage.getUserProfileName().get("fullName");
            String firstName1 = tmFullName.split(" ")[0];
            String jobTitle = profileNewUIPage.getJobTitleFromProfilePage();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6:00AM", "6:00AM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstName1);
            scheduleMainPage.saveSchedule();
            if (smartCardPage.isRequiredActionSmartCardLoaded()) {
                shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            }
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectJobTitleFilterByText(jobTitle);
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.selectWorkRoleFilterByText(workRole, false);
            scheduleMainPage.clickOnFilterBtn();
            createSchedulePage.publishActiveSchedule();
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes("Open").get(1));
            shiftOperatePage.clickOnOfferTMOption();
            Thread.sleep(3000);

            newShiftPage.searchTeamMemberByNameNLocation(firstName1, location);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes("Open").get(1));
            scheduleShiftTablePage.clickViewStatusBtn();
            shiftOperatePage.verifyTMInTheOfferList(firstName1, "offered");
            shiftOperatePage.closeViewStatusContainer();
            loginPage.logOut();

            loginAsDifferentRole(AccessRoles.TeamMember.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
            smartCardPage.clickLinkOnSmartCardByName("View Shifts");
            List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
            mySchedulePage.selectOneShiftIsClaimShift(claimShift);
            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOfferWithMessage("Error!We are sorry. You are not eligible to claim this shift, as the Team Member Corporate-Theatre shift can only be claimed by team member with the badge IMAX.");

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the shift that overlapping with previous week's shift cannot be created")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheShiftThatOverlappingWithPreviousWeekShiftCannotBeCreatedAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

            //Go to one schedule page day view
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
            int i = 0;
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (i< 50 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
                i++;
            }
            String workRole = shiftInfo.get(4);

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(6,6,6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            String shiftId = scheduleShiftTablePage.getAllShiftsOfOneTM(firstNameOfTM1).get(0).getAttribute("id");
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(6);
            //Verify overnight shift can be created
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int index = scheduleShiftTablePage.
                    getTheIndexOfShift(scheduleShiftTablePage.getShiftById(shiftId));
            scheduleShiftTablePage.moveShiftByIndexInDayView(index, false);
            scheduleMainPage.saveSchedule();
            String weekDay = scheduleCommonPage.getActiveDayInfo().get("weekDay");
            String month = scheduleCommonPage.getActiveDayInfo().get("month");
            String day = scheduleCommonPage.getActiveDayInfo().get("day");
            if (day.length()==1) {
                day = "0"+day;
            }
            scheduleCommonPage.clickOnWeekView();
            String shiftTime = scheduleShiftTablePage.getTheShiftInfoByIndex
                    (scheduleShiftTablePage.getShiftIndexById(shiftId)).get(2);

            scheduleCommonPage.navigateToNextWeek();

            //Create the overlapping shift
            isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleCommonPage.clickOnDayView();
            //Verify the overnight shift can display on next week
            scheduleCommonPage.navigateDayViewWithIndex(0);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("6am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM1);
            String actualStatus = shiftOperatePage.getTheMessageOfTMScheduledStatus().trim();
            String expectStatus = shiftTime;
            SimpleUtils.assertOnFail("The schedule status display incorrectly, the expected is:"
                    + expectStatus+" actual is:"+ actualStatus, expectStatus.equals(actualStatus), false);
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
            String expectedWarningMessage = firstNameOfTM1+ " is scheduled to work at "
                    +shiftTime.split("-")[0]+ " - "+shiftTime.split("-")[1]
                    + " "+ weekDay + ", " + month + " " + day + ". Please change other week's schedule.";
            String actualWarningMessage = newShiftPage.getWarningMessageFromWarningModal();

            SimpleUtils.assertOnFail("The schedule status display incorrectly, the expected is:"
                    + expectedWarningMessage+" actual is:"+ actualWarningMessage,
                    expectedWarningMessage.equalsIgnoreCase(actualWarningMessage), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify shift can be drag to previous day")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyShiftCanBeDragToPreviousDayAsInternalAdmin(String username, String password, String browser, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
            locationsPage.clickOnLocationsTab();
            locationsPage.goToSubLocationsInLocationsPage();
            locationsPage.searchLocation(location);               ;
            SimpleUtils.assertOnFail("Locations not searched out Successfully!",  locationsPage.verifyUpdateLocationResult(location), false);
            locationsPage.clickOnLocationInLocationResult(location);
            locationsPage.clickOnConfigurationTabOfLocation();
            HashMap<String, String> templateTypeAndName = locationsPage.getTemplateTypeAndNameFromLocation();
            ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
            configurationPage.goToConfigurationPage();
            configurationPage.clickOnConfigurationCrad("Operating Hours");
            configurationPage.clickOnSpecifyTemplateName(templateTypeAndName.get("Operating Hours"), "edit");
            configurationPage.clickOnEditButtonOnTemplateDetailsPage();
            configurationPage.selectOperatingBufferHours("ContinuousOperation");
            configurationPage.publishNowTheTemplate();
            Thread.sleep(3000);
            switchToConsoleWindow();
            //waiting for the cache
            refreshCachesAfterChangeTemplate();
            //waiting for the cache
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();
            loginAsDifferentRole(AccessRoles.InternalAdmin.getValue());
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(5000);
            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isActiveWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("6:00AM", "6:00AM");
            Thread.sleep(5000);
            int i = 0;
            List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
            String firstNameOfTM1 = shiftInfo.get(0);
            while (i< 50 && (firstNameOfTM1.equalsIgnoreCase("open") || firstNameOfTM1.equalsIgnoreCase("Unassigned"))) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                firstNameOfTM1  = shiftInfo.get(0);
                i++;
            }
            String workRole = shiftInfo.get(4);
            String lastName = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2,2,2);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1+ " "+ lastName);
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleCommonPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(2);
            //Verify overnight shift can be created
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            int index = scheduleShiftTablePage.
                    getTheIndexOfShift(scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).get(0));
            scheduleShiftTablePage.moveShiftByIndexInDayView(index, true);
            scheduleMainPage.saveSchedule();
            //Verify the overnight shift can display on previous day
            scheduleCommonPage.navigateDayViewWithIndex(1);
            SimpleUtils.assertOnFail("The overnight shift "+firstNameOfTM1+" also display on the previous day! ",
                    scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1).size()>0, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
