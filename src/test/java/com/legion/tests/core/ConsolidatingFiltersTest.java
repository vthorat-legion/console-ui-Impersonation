package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsolidatingFiltersTest extends TestBase {

    private static HashMap<String, Object[][]> kendraScott2TeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("KendraScott2TeamMembers.json");
    private static HashMap<String, Object[][]> cinemarkWkdyTeamMembers = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson("CinemarkWkdyTeamMembers.json");

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        try {
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        } catch (Exception e){
            SimpleUtils.fail(e.toString(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Shift Type content in Filter without LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftTypeContentInFilterWithoutLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.verifyShiftTypeInLeft(false);
            scheduleMainPage.verifyShiftTypeFilters();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "KendraScott2_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Compliance Review in week view and day view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyComplianceReviewInWeekViewAndDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            HashMap<String, Object[][]> teamMembers = new HashMap<>();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                teamMembers = kendraScott2TeamMembers;
            } else {
                teamMembers = cinemarkWkdyTeamMembers;
            }

            String firstNameOfTM1 = teamMembers.get("TeamMember2")[0][0].toString();
            String workRoleOfTM1 = teamMembers.get("TeamMember2")[0][2].toString();
            teamPage.activeTMAndRejectOrApproveAllAvailabilityAndTimeOff(firstNameOfTM1);

            // Go to Schedule page, Schedule tab
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(5000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "09:00AM", "11:00PM");

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            // Delete all the shifts that are assigned to the team member
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            // Create new shift for TM1 on first and second day for Clopening violation
            Thread.sleep(3000);
            newShiftPage.clickOnDayViewAddNewShiftButton();
            Thread.sleep(3000);
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("6pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1, 1, 1);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("7am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create new shift for TM on third day for meal break violation
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(2, 2, 2);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("4pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            //Create new shift for TM on forth day for OT violation
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(3, 3, 3);
            newShiftPage.selectWorkRole(workRoleOfTM1);
            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1);
            newShiftPage.clickOnOfferOrAssignBtn();

            scheduleMainPage.saveSchedule();

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            List<WebElement> shiftsOfThirdDay = scheduleShiftTablePage.getOneDayShiftByName(2, firstNameOfTM1);
            shiftOperatePage.deleteMealBreakForOneShift(shiftsOfThirdDay.get(0));
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnFilterBtn();
            int allShiftsCount = scheduleShiftTablePage.getShiftsCount();
            int complianceReviewCount = scheduleMainPage.getSpecificFiltersCount("Compliance Review");
            scheduleMainPage.selectShiftTypeFilterByText("Compliance Review");
            int complianceShiftsCount = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The compliance shift count display incorrectly in schedule filter dropdown list! ",
                    complianceReviewCount == complianceShiftsCount, false);

            List<WebElement> shiftsOfFirstDay = scheduleShiftTablePage.getOneDayShiftByName(0, firstNameOfTM1);
            List<WebElement> shiftsOfSecondDay = scheduleShiftTablePage.getOneDayShiftByName(1, firstNameOfTM1);
            shiftsOfThirdDay = scheduleShiftTablePage.getOneDayShiftByName(2, firstNameOfTM1);
            List<WebElement> shiftsOfForthDay = scheduleShiftTablePage.getOneDayShiftByName(3, firstNameOfTM1);

            //Check the clopening violation shifts on the first and second day
            SimpleUtils.assertOnFail("Clopening compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(0)).contains("Clopening")
                            && scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfSecondDay.get(0)).contains("Clopening") , false);

            //Check the meal break violation shifts on the third day
            SimpleUtils.assertOnFail("Meal break compliance message display failed",
                    scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfThirdDay.get(0)).contains("Missed Meal Break"), false);

                //Check the OT violation shifts on the forth day.
            String actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfForthDay.get(0)).toString();
                SimpleUtils.assertOnFail("OT compliance message display failed, the actual is: "+actualMessage,
                        actualMessage.contains("hrs daily overtime"), false);

            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            SimpleUtils.assertOnFail("Uncheck Compliance Review filter fail! ",
                    allShiftsCount == scheduleShiftTablePage.getShiftsCount(), false);

            //Validate Compliance Review in day view
            scheduleCommonPage.clickOnDayView();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Compliance Review");
            for (int i=0; i< 4; i++) {
                scheduleCommonPage.navigateDayViewWithIndex(i);
                scheduleMainPage.clickOnFilterBtn();
                complianceReviewCount = scheduleMainPage.getSpecificFiltersCount("Compliance Review");
                complianceShiftsCount = scheduleShiftTablePage.getShiftsCount();
                SimpleUtils.assertOnFail("The compliance shift count display incorrectly in schedule filter dropdown list! ",
                        complianceReviewCount == complianceShiftsCount, false);

                //Check the clopening violation shifts on the first and second day
                if (i ==0 || i==1) {
                    shiftsOfFirstDay = scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1);
                    SimpleUtils.assertOnFail("Clopening compliance message display failed",
                            scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfFirstDay.get(0)).contains("Clopening"), false);
                }

                //Check the meal break violation shifts on the third day
                if (i==2) {
                    shiftsOfThirdDay = scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1);
                    SimpleUtils.assertOnFail("Meal break compliance message display failed",
                            scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfThirdDay.get(0)).contains("Missed Meal Break"), false);
                }

                //Check the OT violation shifts on the forth day.
                if (i==3) {
                      shiftsOfForthDay = scheduleShiftTablePage.getShiftsByNameOnDayView(firstNameOfTM1);
                      actualMessage = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(shiftsOfForthDay.get(0)).toString();
                    SimpleUtils.assertOnFail("OT compliance message display failed, the actual is:"+actualMessage,
                            actualMessage.contains(" hrs daily overtime"), false);
                }
            }

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Action Required in week view and day view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyActionRequiredInWeekViewAndDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            String option = "No, keep as unassigned";
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
                ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
                controlsPage.gotoControlsPage();
                SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
                controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
                SimpleUtils.assertOnFail("Scheduling collaboration page not loaded successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
                controlsNewUIPage.clickOnScheduleCollaborationOpenShiftAdvanceBtn();

                //Set 'Automatically convert unassigned shifts to open shifts when generating the schedule?' set as Yes, all unassigned shifts
                controlsNewUIPage.updateConvertUnassignedShiftsToOpenSettingOption(option);

            } else if (getDriver().getCurrentUrl().contains(propertyMap.get("CinemarkWkdy_Enterprise"))) {
                OpsPortalLocationsPage opsPortalLocationsPage = (OpsPortalLocationsPage) pageFactory.createOpsPortalLocationsPage();
                opsPortalLocationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
                configurationPage.goToConfigurationPage();
                configurationPage.clickOnConfigurationCrad("Schedule Collaboration");
                configurationPage.clickOnSpecifyTemplateName("Cinemark Base Template", "edit");
                configurationPage.clickOnEditButtonOnTemplateDetailsPage();
                configurationPage.updateConvertUnassignedShiftsToOpenWhenCreatingScheduleSettingOption(option);
                configurationPage.updateConvertUnassignedShiftsToOpenWhenCopyingScheduleSettingOption(option);
                configurationPage.publishNowTheTemplate();
                switchToConsoleWindow();
            }

            if(getDriver().getCurrentUrl().contains(propertyMap.get("CinemarkWkdy_Enterprise"))){
                Thread.sleep(180000);
            }


            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            Thread.sleep(3000);
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(3000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "08:00PM");

            scheduleMainPage.clickOnFilterBtn();
            int allShiftsCount = scheduleShiftTablePage.getShiftsCount();
            int unassignedAndOOOHShiftCount = scheduleMainPage.getSpecificFiltersCount("Action Required");
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            int unassignedAndOOOHShiftCountInFilter = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The Action Required shift count display incorrectly in schedule filter dropdown list! ",
                    unassignedAndOOOHShiftCount == unassignedAndOOOHShiftCountInFilter, false);

            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            SimpleUtils.assertOnFail("Uncheck Action Required filter fail! ",
                    allShiftsCount == scheduleShiftTablePage.getShiftsCount(), false);

            //Validate Action Required shifts in day view
            scheduleCommonPage.clickOnDayView();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            for (int i=0; i< 7; i++) {
                scheduleCommonPage.navigateDayViewWithIndex(i);
                scheduleMainPage.clickOnFilterBtn();
                unassignedAndOOOHShiftCount = scheduleMainPage.getSpecificFiltersCount("Action Required");
                unassignedAndOOOHShiftCountInFilter = scheduleShiftTablePage.getShiftsCount();
                SimpleUtils.assertOnFail("The Action Required shift count display incorrectly in schedule filter dropdown list! ",
                        unassignedAndOOOHShiftCount == unassignedAndOOOHShiftCountInFilter, false);
            }

            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            shiftOperatePage.convertAllUnAssignedShiftToOpenShift();
            createSchedulePage.publishActiveSchedule();


            //Get the info of this week for copy schedule
            String firstWeekInfo = scheduleCommonPage.getActiveWeekText();
            if (firstWeekInfo.length() > 11) {
                firstWeekInfo = firstWeekInfo.trim().substring(10);
            }

            scheduleCommonPage.navigateToNextWeek();

            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.clickCreateScheduleBtn();
            createSchedulePage.editOperatingHoursWithGivingPrameters("Sunday", "11:00AM", "08:00PM");
            createSchedulePage.editOperatingHoursWithGivingPrameters("Monday", "11:00AM", "08:00PM");
            createSchedulePage.editOperatingHoursWithGivingPrameters("Tuesday", "11:00AM", "08:00PM");
            createSchedulePage.editOperatingHoursWithGivingPrameters("Wednesday", "11:00AM", "08:00PM");
            createSchedulePage.editOperatingHoursWithGivingPrameters("Thursday", "11:00AM", "08:00PM");
            createSchedulePage.editOperatingHoursWithGivingPrameters("Friday", "11:00AM", "08:00PM");
            createSchedulePage.editOperatingHoursWithGivingPrameters("Saturday", "11:00AM", "08:00PM");
            createSchedulePage.clickNextBtnOnCreateScheduleWindow();
            createSchedulePage.selectWhichWeekToCopyFrom(firstWeekInfo);
            createSchedulePage.clickOnFinishButtonOnCreateSchedulePage();

            scheduleMainPage.clickOnFilterBtn();
            allShiftsCount = scheduleShiftTablePage.getShiftsCount();
            unassignedAndOOOHShiftCount = scheduleMainPage.getSpecificFiltersCount("Action Required");
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            unassignedAndOOOHShiftCountInFilter = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The Action Required shift count display incorrectly in schedule filter dropdown list! ",
                    unassignedAndOOOHShiftCount == unassignedAndOOOHShiftCountInFilter, false);

            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            SimpleUtils.assertOnFail("Uncheck Action Required filter fail! ",
                    allShiftsCount == scheduleShiftTablePage.getShiftsCount(), false);

            //Validate Action Required shifts in day view
            scheduleCommonPage.clickOnDayView();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            for (int i=0; i< 7; i++) {
                scheduleCommonPage.navigateDayViewWithIndex(i);
                scheduleMainPage.clickOnFilterBtn();
                unassignedAndOOOHShiftCount = scheduleMainPage.getSpecificFiltersCount("Action Required");
                unassignedAndOOOHShiftCountInFilter = scheduleShiftTablePage.getShiftsCount();
                SimpleUtils.assertOnFail("The Action Required shift count display incorrectly in schedule filter dropdown list! ",
                        unassignedAndOOOHShiftCount == unassignedAndOOOHShiftCountInFilter, false);
            }

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate Shift Type content in Filter with LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyShiftTypeContentInFilterWithLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeDistrict("District Whistler");
            locationSelectorPage.changeLocation("Lift Ops_Parent");
            // Go to Schedule page, Schedule tab

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.verifyShiftTypeInLeft(true);
            scheduleMainPage.verifyShiftTypeFilters();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Compliance Review with LG")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyComplianceReviewWithLGAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.changeDistrict("District Whistler");
            locationSelectorPage.changeLocation("Lift Ops_Parent");

            // Go to Schedule page, Schedule tab

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            // Edit the Schedule
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

            scheduleMainPage.clickOnFilterBtn();
            int allShiftsCount = scheduleShiftTablePage.getShiftsCount();
            int complianceReviewCount = scheduleMainPage.getSpecificFiltersCount("Compliance Review");
            scheduleMainPage.selectShiftTypeFilterByText("Compliance Review");
            int complianceShiftsCount = scheduleShiftTablePage.getShiftsCount();
            SimpleUtils.assertOnFail("The compliance shift count display incorrectly in schedule filter dropdown list! ",
                    complianceReviewCount == complianceShiftsCount, false);

            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            SimpleUtils.assertOnFail("Uncheck Compliance Review filter fail! ",
                    allShiftsCount == scheduleShiftTablePage.getShiftsCount(), false);

            //Validate Compliance Review in day view
            scheduleCommonPage.clickOnDayView();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            for (int i=0; i< 7; i++) {
                scheduleCommonPage.navigateDayViewWithIndex(i);
                scheduleMainPage.clickOnFilterBtn();
                complianceReviewCount = scheduleMainPage.getSpecificFiltersCount("Compliance Review");
                complianceShiftsCount = scheduleShiftTablePage.getShiftsCount();
                SimpleUtils.assertOnFail("The compliance shift count display incorrectly in schedule filter dropdown list! ",
                        complianceReviewCount == complianceShiftsCount, false);
            }

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the options of schedule Filter should be in alphabetical and numerical order")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void validateTheOptionsOfScheduleFilterShouldBeInAlphabeticalAndNumbericalOrderAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Schedule page, Schedule tab
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            // Create schedule if it is not created
            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            scheduleMainPage.clickOnFilterBtn();
            String filterText = "SHIFT TYPE";
            List<String> filterNamesBeforeSort = scheduleMainPage.getSpecificFilterNames(filterText);
            List<String> filterNamesAfterSort = scheduleMainPage.getSpecificFilterNames(filterText);
            Collections.sort(filterNamesAfterSort);
            SimpleUtils.assertOnFail("The filter name's order is incorrectly, the expected is:"+filterNamesAfterSort
                            +" The actual is:"+filterNamesBeforeSort,
                    filterNamesAfterSort.equals(filterNamesBeforeSort), false);
            filterText = "JOB TITLE";
            filterNamesBeforeSort = scheduleMainPage.getSpecificFilterNames(filterText);
            filterNamesAfterSort = scheduleMainPage.getSpecificFilterNames(filterText);
            Collections.sort(filterNamesAfterSort);
            SimpleUtils.assertOnFail("The filter name's order is incorrectly, the expected is:"+filterNamesAfterSort
                            +" The actual is:"+filterNamesBeforeSort,
                    filterNamesAfterSort.equals(filterNamesBeforeSort), false);
            //Work role display order is related with the CC-> User Management-> Work Roles -> work role's display order
            filterText = "WORK ROLE";
            filterNamesBeforeSort = scheduleMainPage.getSpecificFilterNames(filterText);
            filterNamesAfterSort = scheduleMainPage.getSpecificFilterNames(filterText);
            Collections.sort(filterNamesAfterSort);
            SimpleUtils.assertOnFail("The filter name's order is incorrectly, the expected is:"+filterNamesAfterSort
                            +" The actual is:"+filterNamesBeforeSort,
                    filterNamesAfterSort.equals(filterNamesBeforeSort), false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
