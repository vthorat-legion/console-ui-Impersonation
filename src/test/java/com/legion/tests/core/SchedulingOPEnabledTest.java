package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

import static com.legion.utils.MyThreadLocal.*;

public class SchedulingOPEnabledTest  extends TestBase {

    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, String> schedulePolicyData = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SchedulingPoliciesData.json");
    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");


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
    @Owner(owner = "Estelle/Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality - Week View - Context Menu")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleFunctionalityWeekViewAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(!isActiveWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        //In week view, Group by All filter have 4 filters:1.Group by all  2. Group by work role  3. Group by TM 4.Group by job title
        scheduleMainPage.validateGroupBySelectorSchedulePage(false);
        //Selecting any of them, check the schedule table
        scheduleMainPage.validateScheduleTableWhenSelectAnyOfGroupByOptions(false);

        //Edit button should be clickable
        //While click on edit button,if Schedule is finalized then prompt is available and Prompt is in proper alignment and correct msg info.
        //Edit anyway and cancel button is clickable
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();

        //click on the context of any TM, 1. View profile 2. Change shift role  3.Assign TM 4.  Convert to open shift is enabled for current and future week day 5.Edit meal break time 6. Delete shift
        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.navigateToNextWeek();
        boolean isActiveWeekGenerated2 = createSchedulePage.isWeekGenerated();
        if(isActiveWeekGenerated2){
           createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        Thread.sleep(3000);
        createSchedulePage.createScheduleForNonDGFlowNewUI();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
        scheduleMainPage.saveSchedule();
        createSchedulePage.publishActiveSchedule();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        SimpleUtils.assertOnFail(" context of any TM display doesn't show well" , shiftOperatePage.verifyContextOfTMDisplay(), false);

        //"After Click on view profile,then particular TM profile is displayed :1. Personal details 2. Work Preferences 3. Availability
        shiftOperatePage.clickOnViewProfile();
        shiftOperatePage.verifyPersonalDetailsDisplayed();
        shiftOperatePage.verifyWorkPreferenceDisplayed();
        shiftOperatePage.verifyAvailabilityDisplayed();
        shiftOperatePage.closeViewProfileContainer();

        //"After Click on the Change shift role, one prompt is enabled:various work role any one of them can be selected"
        shiftOperatePage.clickOnProfileIcon();
        shiftOperatePage.clickOnChangeRole();
        shiftOperatePage.verifyChangeRoleFunctionality();
        //check the work role by click Apply button
        shiftOperatePage.changeWorkRoleInPrompt(true);
        //check the work role by click Cancel button
        shiftOperatePage.changeWorkRoleInPrompt(false);

        //After Click on Assign TM-Select TMs window is opened,Recommended and search TM tab is enabled
        shiftOperatePage.clickOnProfileIcon();
        shiftOperatePage.clickonAssignTM();
        shiftOperatePage.verifyRecommendedAndSearchTMEnabled();

        //Search and select any TM,Click on the assign: new Tm is updated on the schedule table
        //Select new TM from Search Team Member tab
        WebElement selectedShift = null;
        selectedShift = shiftOperatePage.clickOnProfileIcon();
        String selectedShiftId= selectedShift.getAttribute("id").toString();
        shiftOperatePage.clickonAssignTM();
        String firstNameOfSelectedTM = newShiftPage.selectTeamMembers();
        newShiftPage.clickOnOfferOrAssignBtn();
        SimpleUtils.assertOnFail(" New selected TM doesn't display in scheduled table" , firstNameOfSelectedTM.equals(scheduleShiftTablePage.getShiftById(selectedShiftId).findElement(By.className("week-schedule-worker-name")).getText()), false);
        //Select new TM from Recommended TMs tab
//        selectedShift = shiftOperatePage.clickOnProfileIcon();
//        String selectedShiftId2 = selectedShift.getAttribute("id").toString();
//        shiftOperatePage.clickonAssignTM();
//        shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
//        String firstNameOfSelectedTM2 = newShiftPage.selectTeamMembers();
//        newShiftPage.clickOnOfferOrAssignBtn();
//        SimpleUtils.assertOnFail(" New selected TM doesn't display in scheduled table" , firstNameOfSelectedTM2.equals(scheduleShiftTablePage.getShiftById(selectedShiftId2).findElement(By.className("week-schedule-worker-name")).getText()), false);

        //Click on the Convert to open shift, checkbox is available to offer the shift to any specific TM[optional] Cancel /yes
        //if checkbox is unselected then, shift is convert to open
        selectedShift = shiftOperatePage.clickOnProfileIcon();
        String tmFirstName = selectedShift.findElement(By.className("week-schedule-worker-name")).getText();
        shiftOperatePage.clickOnConvertToOpenShift();
        if (shiftOperatePage.verifyConvertToOpenPopUpDisplay(tmFirstName)) {
            shiftOperatePage.convertToOpenShiftDirectly();
        }
        //if checkbox is select then select team member page will display
        selectedShift = shiftOperatePage.clickOnProfileIcon();
        tmFirstName = selectedShift.findElement(By.className("week-schedule-worker-name")).getText();
        shiftOperatePage.clickOnConvertToOpenShift();
        if (shiftOperatePage.verifyConvertToOpenPopUpDisplay(tmFirstName)) {
            shiftOperatePage.convertToOpenShiftAndOfferToSpecificTMs();
        }

        //After click on Edit Shift Time, the Edit Shift window will display
        shiftOperatePage.clickOnProfileIcon();
        shiftOperatePage.clickOnEditShiftTime();
        shiftOperatePage.verifyEditShiftTimePopUpDisplay();
        shiftOperatePage.clickOnCancelEditShiftTimeButton();
        //Edit shift time and click update button
        shiftOperatePage.editAndVerifyShiftTime(true);
        //Edit shift time and click Cancel button
        shiftOperatePage.editAndVerifyShiftTime(false);

        //Verify Edit/View Meal Break
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

        //verify cancel button
        shiftOperatePage.verifyDeleteShiftCancelButton();

        //verify delete shift
        shiftOperatePage.verifyDeleteShift();
    }

//    @Automated(automated = "Automated")
//    @Owner(owner = "Mary")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify the budget hour in DM view schedule page for non dg flow")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyBudgetHourInDMViewSchedulePageForNonDGFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        try{
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//
//            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
//            if (isWeekGenerated) {
//                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//            }
//            createSchedulePage.createScheduleForNonDGFlowNewUI();
//            float budgetHoursInSchedule = Float.parseFloat(smartCardPage.getBudgetNScheduledHoursFromSmartCard().get("Budget"));
//
//            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//            locationSelectorPage.changeDistrictDirect();
//
//            ScheduleDMViewPage scheduleDMViewPage = pageFactory.createScheduleDMViewPage();
//            float budgetedHoursInDMViewSchedule = scheduleDMViewPage.getBudgetedHourOfScheduleInDMViewByLocation(location);
//            if (budgetHoursInSchedule != 0 && budgetHoursInSchedule == budgetedHoursInDMViewSchedule) {
//                SimpleUtils.pass("Verified the budget hour in DM view schedule page is consistent with the value saved in create schedule page!");
//            } else {
//                SimpleUtils.fail("Verified the budget hour in DM view schedule page is consistent with the value saved in create schedule page! The budget hour in DM view schedule page is " +
//                        budgetedHoursInDMViewSchedule + ". The value saved in create schedule page is " + budgetHoursInSchedule, false);
//            }
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//
//    }



    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Assign TM warning: TM status is already Scheduled at Home location")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAssignTMWarningForTMIsAlreadyScheduledAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
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
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            List<String> firstShiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(0);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.selectWorkRole(firstShiftInfo.get(4));
            newShiftPage.moveSliderAtCertainPoint("4pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkingDaysOnNewShiftPageByIndex(Integer.parseInt(firstShiftInfo.get(1)));
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.verifyScheduledWarningWhenAssigning(firstShiftInfo.get(0) + " " + firstShiftInfo.get(5),
                    firstShiftInfo.get(2));
            newShiftPage.clickCloseBtnForCreateShift();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    // need change case when create new shift
    @Automated(automated = "Automated")
    @Owner(owner = "haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify smart card for schedule not publish(include past weeks)")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifySmartCardForScheduleNotPublishAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab("Schedule");
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("07:00AM", "09:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            //make edits
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.selectWorkRole("");
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            //generate and save, should not display number of changes, we set it as 0.
            int changesNotPublished = 0;
            //Verify changes not publish smart card.
            SimpleUtils.assertOnFail("Changes not publish smart card is not loaded!",smartCardPage.isSpecificSmartCardLoaded("ACTION REQUIRED"),false);
            smartCardPage.verifyChangesNotPublishSmartCard(changesNotPublished);
            createSchedulePage.verifyLabelOfPublishBtn("Publish");
            String activeWeek = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.clickOnScheduleSubTab("Overview");
            List<String> resultListInOverview = scheduleOverviewPage.getOverviewData();
            for (String s : resultListInOverview){
                String a = s.substring(1,7);
                if (activeWeek.toLowerCase().contains(a.toLowerCase())){
                    if (s.contains("Unpublished Edits")){
                        SimpleUtils.pass("Warning message in overview page is correct!");
                    } else {
                        SimpleUtils.fail("Warning message is not expected: "+ s.split(",")[4],false);
                    }
                }
            }
        }
        catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify smart card for schedule not publish(include past weeks) - republish")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNumberOnSmartCardForScheduleNotPublishAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab("Schedule");
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();
            if (createSchedulePage.isWeekGenerated()){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("07:00AM", "11:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //make edits and publish
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("7am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(scheduleWorkRoles.get(getEnterprise()));
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            //make edits and save
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(scheduleWorkRoles.get(getEnterprise()));
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            //generate and save, should not display number of changes, we set it as 0.
            int changesNotPublished = 1;
            //Verify changes not publish smart card.
            SimpleUtils.assertOnFail("Changes not publish smart card is not loaded!",
                    smartCardPage.isSpecificSmartCardLoaded("UNPUBLISHED CHANGES"),false);
            smartCardPage.verifyChangesNotPublishSmartCard(changesNotPublished);
            createSchedulePage.verifyLabelOfPublishBtn("Republish");
            String activeWeek = scheduleCommonPage.getActiveWeekText();
            scheduleCommonPage.clickOnScheduleSubTab("Overview");
            List<String> resultListInOverview = scheduleOverviewPage.getOverviewData();
            for (String s : resultListInOverview){
                String a = s.substring(1,7);
                if (activeWeek.toLowerCase().contains(a.toLowerCase())){
                    if (s.contains("Unpublished Edits")){
                        SimpleUtils.pass("Warning message in overview page is correct!");
                    } else {
                        SimpleUtils.fail("Warning message is not expected: "+ s.split(",")[4],false);
                    }
                }
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    //Blocking by Refresh button bug: https://legiontech.atlassian.net/browse/SCH-3116
//    @Automated(automated = "Automated")
//    @Owner(owner = "Mary")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "verify the Unpublished Edits on dashboard and overview page")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyUnpublishedEditsTextOnDashboardAndOverviewPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        try{
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
//                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//
//            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
//            if (!isWeekGenerated){
//                createSchedulePage.createScheduleForNonDGFlowNewUI();
//            }
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            newShiftPage.addOpenShiftWithLastDay("GENERAL MANAGER");
//            scheduleMainPage.saveSchedule();
//
//            //Verify the Unpublished Edits text on overview page
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//            List<WebElement> schedulesInOverviewPage = scheduleOverviewPage.getOverviewScheduleWeeks();
//            if (schedulesInOverviewPage != null && schedulesInOverviewPage.size()>0){
//                WebElement warningTextOfCurrentScheduleWeek = schedulesInOverviewPage.get(0).findElement(By.cssSelector("div.text-small.ng-binding"));
//                if (warningTextOfCurrentScheduleWeek != null){
//                    String warningText = warningTextOfCurrentScheduleWeek.getText();
//                    if (warningText !=null && warningText.equals("Unpublished Edits")){
//                        SimpleUtils.pass("Verified the Unpublished Edits on Overview page display correctly. ");
//                    } else{
//                        SimpleUtils.fail("Verified the Unpublished Edits on Overview page display incorrectly. The actual warning text is " + warningText +".", true);
//                    }
//                }
//            } else{
//                SimpleUtils.fail("Overview Page: Schedule weeks not found!" , true);
//            }
//
//            //Verify the Unpublished Edits text on dashboard page
//            dashboardPage.navigateToDashboard();
//            dashboardPage.clickOnRefreshButton();
//            List<WebElement> dashboardScheduleWeeks = dashboardPage.getDashboardScheduleWeeks();
//            if (dashboardScheduleWeeks != null && dashboardScheduleWeeks.size()>0){
//                WebElement warningTextOfCurrentScheduleWeek = dashboardScheduleWeeks.get(1).findElement(By.cssSelector("div.text-small.ng-binding"));
//                if (warningTextOfCurrentScheduleWeek != null){
//                    String warningText = warningTextOfCurrentScheduleWeek.getText();
//                    if (warningText !=null && warningText.equals("Unpublished Edits")){
//                        SimpleUtils.pass("Verified the Unpublished Edits text on Dashboard page display correctly. ");
//                    } else{
//                        SimpleUtils.fail("Verified the Unpublished Edits text on Dashboard page display incorrectly. The actual warning text is " + warningText +".", false);
//                    }
//                }
//            } else{
//                SimpleUtils.fail("Dashboard Page: Schedule weeks not found!" , false);
//            }
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//
//    }



    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality >  Compliance smartcard")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyComplianceSmartCardFunctionalityAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.goToScheduleNewUI();
        if(smartCardPage.verifyComplianceShiftsSmartCardShowing() && smartCardPage.verifyRedFlagIsVisible()){
            smartCardPage.verifyComplianceFilterIsSelectedAftClickingViewShift();
            smartCardPage.verifyComplianceShiftsShowingInGrid();
            smartCardPage.verifyClearFilterFunction();
        }else
            SimpleUtils.report("There is no compliance and no red flag");
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality > Schedule smartcard")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleFunctionalityScheduleSmartCardAsStoreManager(String username, String password, String browser, String location)
            throws Exception {
        ArrayList<LinkedHashMap<String, Float>> scheduleOverviewAllWeekHours = new ArrayList<LinkedHashMap<String, Float>>();
        HashMap<String, Float> scheduleSmartCardHoursWages = new HashMap<>();
        HashMap<String, Float> overviewData = new HashMap<>();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.goToScheduleNewUI();
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(!isActiveWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        scheduleSmartCardHoursWages = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
        SimpleUtils.report("scheduleSmartCardHoursWages :"+scheduleSmartCardHoursWages);
        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
        scheduleOverviewPage.clickOverviewTab();
        List<WebElement> scheduleOverViewWeeks =  scheduleOverviewPage.getOverviewScheduleWeeks();
        overviewData = scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(0));
        SimpleUtils.report("overview data :"+scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(0)));
        if (Math.abs(overviewData.get("guidanceHours") - (scheduleSmartCardHoursWages.get("budgetedHours"))) <= 0.05
                && Math.abs(overviewData.get("scheduledHours") - (scheduleSmartCardHoursWages.get("scheduledHours"))) <= 0.05
                && Math.abs(overviewData.get("otherHours") - (scheduleSmartCardHoursWages.get("otherHours"))) <= 0.05) {
            SimpleUtils.pass("Schedule/Budgeted smartcard-is showing the values in Hours and wages, it is displaying the same data as overview page have for the current week .");
        }else {
            SimpleUtils.fail("Scheduled Hours and Overview Schedule Hours not same",true);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify offers generated for open shift")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOffersGeneratedForOpenShiftsAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            //delete unassigned shifts and open shifts.
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Action Required");
            //scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            //Delete all shifts are action required.
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Open");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("");
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectShiftTypeFilterByText("Compliance Review");
            shiftOperatePage.deleteAllShiftsInWeekView();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            shiftOperatePage.clickOnProfileIcon();
            shiftOperatePage.clickOnConvertToOpenShift();
            shiftOperatePage.convertToOpenShiftDirectly();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            int i = 0;
            boolean hasOffers = false;
            while (i < 20 && !hasOffers) {
                BasePage.waitForSeconds(10);
                shiftOperatePage.clickOnProfileIconOfOpenShift();
                scheduleShiftTablePage.clickViewStatusBtn();
                hasOffers = shiftOperatePage.checkIfOfferListHasOffers();
                shiftOperatePage.closeViewStatusContainer();
                i++;
            }
            SimpleUtils.assertOnFail("The offer list should not null!", hasOffers, false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Work Role in Configuration")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWorkRoleConfigurationAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        //Go to OP page
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to User Management page
        UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
        userManagementPage.clickOnUserManagementTab();
        userManagementPage.goToWorkRolesTile();

        HashMap<String,String> workRoleInfo  = userManagementPage.getAllWorkRoleStyleInfo("");
        SimpleUtils.assertOnFail("There should be style info for work role!", !workRoleInfo.isEmpty(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Work Role in FILTER")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWorkRoleInFilterAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
        // Navigate to a week
        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule if not created
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        scheduleMainPage.clickOnFilterBtn();
        ArrayList<HashMap<String,String>> workRoleInfoInFilter  = scheduleMainPage.getWorkRoleInfoFromFilter();

        //Go to OP page
        dashboardPage.navigateToDashboard();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to User Management page
        UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
        userManagementPage.clickOnUserManagementTab();
        userManagementPage.goToWorkRolesTile();

        if (!workRoleInfoInFilter.isEmpty()){
            HashMap<String,String> workRoleInfoInConfiguration  = userManagementPage.getAllWorkRoleStyleInfo(workRoleInfoInFilter.get(0).get("WorkRoleName"));
            if (workRoleInfoInFilter.get(0).get("WorkRoleStyle").replace("background-color", "background").equalsIgnoreCase(workRoleInfoInConfiguration.get(workRoleInfoInFilter.get(0).get("WorkRoleName")))){
                SimpleUtils.pass("Work Role color match!");
            }  else {
                SimpleUtils.fail("Work role color don't match!", false);
            }
        } else {
            SimpleUtils.fail("No work role displaying!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Work Role in Schedule")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWorkRoleInScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
        // Navigate to a week
        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule if not created
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ArrayList<HashMap<String,String>> workRoleInfoInGroupBySection  = scheduleShiftTablePage.getGroupByOptionsStyleInfo();

        //Go to OP page
        dashboardPage.navigateToDashboard();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to User Management page
        UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
        userManagementPage.clickOnUserManagementTab();
        userManagementPage.goToWorkRolesTile();

        if (!workRoleInfoInGroupBySection.isEmpty()){
            HashMap<String,String> workRoleInfoInConfiguration  = userManagementPage.getAllWorkRoleStyleInfo(workRoleInfoInGroupBySection.get(0).get("optionName"));
            if (workRoleInfoInGroupBySection.get(0).get("optionStyle").replace("background-color", "background").equalsIgnoreCase(workRoleInfoInConfiguration.get(workRoleInfoInGroupBySection.get(0).get("optionName")))){
                SimpleUtils.pass("Work Role color match!");
            }  else {
                SimpleUtils.fail("Work role color don't match!", false);
            }
        } else {
            SimpleUtils.fail("No work role displaying!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Work Role in STAFF")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWorkRoleInStaffAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
        // Navigate to a week
        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule if not created
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        scheduleMainPage.goToToggleSummaryView();
        ArrayList<HashMap<String,String>> workRoleInfoInStaffSection  = scheduleMainPage.getToggleSummaryStaffWorkRoleStyleInfo();

        //Go to OP page
        dashboardPage.navigateToDashboard();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to User Management page
        UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
        userManagementPage.clickOnUserManagementTab();
        userManagementPage.goToWorkRolesTile();

        if (!workRoleInfoInStaffSection.isEmpty()){
            HashMap<String,String> workRoleInfoInConfiguration  = userManagementPage.getAllWorkRoleStyleInfo(workRoleInfoInStaffSection.get(0).get("WorkRoleName"));
            if (workRoleInfoInStaffSection.get(0).get("WorkRoleStyle").replace("background-color", "background").equalsIgnoreCase(workRoleInfoInConfiguration.get(workRoleInfoInStaffSection.get(0).get("WorkRoleName")))){
                SimpleUtils.pass("Work Role color match!");
            }  else {
                SimpleUtils.fail("Work role color don't match!", false);
            }
        } else {
            SimpleUtils.fail("No work role displaying!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Work Role in Labor Guidance")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWorkRoleInLaborGuidanceAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
        // Navigate to a week
        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.navigateToNextWeek();
        // create the schedule if not created
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        AnalyzePage analyzePage = pageFactory.createAnalyzePage();
        analyzePage.clickOnAnalyzeBtn("labor guidance");
        ArrayList<HashMap<String,String>> workRoleInfoInLaborGuidanceSection  = analyzePage.getLaborGuidanceWorkRoleStyleInfo();
        analyzePage.closeAnalyzeWindow();
        //Go to OP page
        dashboardPage.navigateToDashboard();
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to User Management page
        UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
        userManagementPage.clickOnUserManagementTab();
        userManagementPage.goToWorkRolesTile();

        if (!workRoleInfoInLaborGuidanceSection.isEmpty()){
            HashMap<String,String> workRoleInfoInConfiguration  = userManagementPage.getAllWorkRoleStyleInfo(workRoleInfoInLaborGuidanceSection.get(0).get("WorkRoleName"));
            if (workRoleInfoInLaborGuidanceSection.get(0).get("WorkRoleStyle").replace("background-color", "background").equalsIgnoreCase(workRoleInfoInConfiguration.get(workRoleInfoInLaborGuidanceSection.get(0).get("WorkRoleName")))){
                SimpleUtils.pass("Work Role color match!");
            }  else {
                SimpleUtils.fail("Work role color don't match!", false);
            }
        } else {
            SimpleUtils.fail("No work role displaying!", false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality > Job Title Filter Functionality > Week View")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
    public void viewAndFilterScheduleWithGroupByJobTitleInWeekView(String username, String password, String browser, String location)
            throws Exception {

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);


        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);

        /*
         *  Navigate to Schedule Week view
         */
        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
        if(!isActiveWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        boolean isWeekView = true;
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
        scheduleMainPage.filterScheduleByJobTitle(isWeekView);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.filterScheduleByJobTitle(isWeekView);
        scheduleMainPage.clickOnCancelButtonOnEditMode();
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality > Job Title Filter Functionality > Day View")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
    public void viewAndFilterScheduleWithGroupByJobTitleInDayView(String username, String password, String browser, String location)
            throws Exception {

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();

        SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);

        /*
         *  Navigate to Schedule day view
         */
        boolean isWeekView = false;
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }
        scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
        scheduleMainPage.filterScheduleByJobTitle(isWeekView);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.filterScheduleByJobTitle(isWeekView);
        scheduleMainPage.clickOnCancelButtonOnEditMode();
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality > Job Title Filter Functionality > Combination")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void viewAndFilterScheduleWithGroupByJobTitleFilterCombinationInWeekViewAsStoreManager(String username, String password, String browser, String location)
            throws Exception {

        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        /*
         *  Navigate to Schedule week view
         */
        boolean isWeekView = true;
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated){
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }

        scheduleMainPage.selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
        scheduleMainPage.filterScheduleByWorkRoleAndJobTitle(isWeekView);
        scheduleMainPage.filterScheduleByShiftTypeAndJobTitle(isWeekView);
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.filterScheduleByJobTitle(isWeekView);
        scheduleMainPage.clickOnCancelButtonOnEditMode();
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Schedules widget")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifySchedulesWidgetsAsStoreManager(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
            // Verify Edit mode Dashboard loaded
            liquidDashboardPage.enterEditMode();
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Schedules.getValue());
            liquidDashboardPage.saveAndExitEditMode();

            // Refresh the dashboard to get the value updated
//            dashboardPage.clickOnRefreshButton();

            //verify view schedules link
            List<String> resultListOnWidget = liquidDashboardPage.getDataOnSchedulesWidget();
            liquidDashboardPage.clickOnLinkByWidgetNameAndLinkName(LiquidDashboardTest.widgetType.Schedules.getValue(), LiquidDashboardTest.linkNames.View_Schedules.getValue());
            //verify value on widget
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            List<String> resultListInOverview = scheduleOverviewPage.getOverviewData();
            if (resultListOnWidget.size()==resultListInOverview.size()){
                for (int i=0;i<resultListInOverview.size();i++){
                    boolean flag = resultListInOverview.get(i).equals(resultListOnWidget.get(i));
                    if (flag){
                        SimpleUtils.pass("Schedules widget: Values on widget are consistent with the one in overview");
                    } else {
                        SimpleUtils.fail("Schedules widget: Values on widget are not consistent with the one in overview!",false);
                    }
                }
            } else {
                SimpleUtils.fail("Schedules widget: something wrong with the number of week displayed!",false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify UI for common widget")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyCommonUIOfWidgetsAsStoreManager(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
            // Verifiy Edit mode Dashboard loaded
            liquidDashboardPage.enterEditMode();

            //verify switch off Todays_Forcast widget
            liquidDashboardPage.switchOffWidget(LiquidDashboardTest.widgetType.Todays_Forecast.getValue());
            //verify switch on Todays_Forcast widget
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Todays_Forecast.getValue());
            //verify close Todays_Forcast widget
            liquidDashboardPage.closeWidget(LiquidDashboardTest.widgetType.Todays_Forecast.getValue());
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Todays_Forecast.getValue());


            //verify switch off Timesheet_Approval_Status widget
            liquidDashboardPage.switchOffWidget(LiquidDashboardTest.widgetType.Timesheet_Approval_Status.getValue());
            //verify switch on Timesheet_Approval_Status widget
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Timesheet_Approval_Status.getValue());
            //verify close Timesheet_Approval_Status widget
            liquidDashboardPage.closeWidget(LiquidDashboardTest.widgetType.Timesheet_Approval_Status.getValue());
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Timesheet_Approval_Status.getValue());

/*
        //verify switch off Timesheet_Approval_Rate widget
        liquidDashboardPage.switchOffWidget(widgetType.Timesheet_Approval_Rate.getValue());
        //verify switch on Timesheet_Approval_Rate widget
        liquidDashboardPage.switchOnWidget(widgetType.Timesheet_Approval_Rate.getValue());
        //verify close Timesheet_Approval_Rate widget
        liquidDashboardPage.closeWidget(widgetType.Timesheet_Approval_Rate.getValue());
        liquidDashboardPage.switchOnWidget(widgetType.Timesheet_Approval_Rate.getValue());
*/

            //verify switch off Alerts widget
            liquidDashboardPage.switchOffWidget(LiquidDashboardTest.widgetType.Alerts.getValue());
            //verify switch on Alerts widget
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Alerts.getValue());
            //verify close Alerts widget
            liquidDashboardPage.closeWidget(LiquidDashboardTest.widgetType.Alerts.getValue());
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Alerts.getValue());

/*
        //verify switch off Swaps_Covers widget
        liquidDashboardPage.switchOffWidget(widgetType.Swaps_Covers.getValue());
        //verify switch on Swaps_Covers widget
        liquidDashboardPage.switchOnWidget(widgetType.Swaps_Covers.getValue());
        //verify close Swaps_Covers widget
        liquidDashboardPage.closeWidget(widgetType.Swaps_Covers.getValue());
        liquidDashboardPage.switchOnWidget(widgetType.Swaps_Covers.getValue());
*/

            //verify switch off Starting_Soon widget
            liquidDashboardPage.switchOffWidget(LiquidDashboardTest.widgetType.Starting_Soon.getValue());
            //verify switch on Starting_Soon widget
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Starting_Soon.getValue());
            //verify close Starting_Soon widget
            liquidDashboardPage.closeWidget(LiquidDashboardTest.widgetType.Starting_Soon.getValue());
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Starting_Soon.getValue());


            //verify switch off Schedules widget
            liquidDashboardPage.switchOffWidget(LiquidDashboardTest.widgetType.Schedules.getValue());
            //verify switch on Schedules widget
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Schedules.getValue());
            //verify close Schedules widget
            liquidDashboardPage.closeWidget(LiquidDashboardTest.widgetType.Schedules.getValue());
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Schedules.getValue());

/*
        //verify switch off Open_Shifts widget
        liquidDashboardPage.switchOffWidget(widgetType.Open_Shifts.getValue());
        //verify switch on Open_Shifts widget
        liquidDashboardPage.switchOnWidget(widgetType.Open_Shifts.getValue());
        //verify close Open_Shifts widget
        liquidDashboardPage.closeWidget(widgetType.Open_Shifts.getValue());
        liquidDashboardPage.switchOnWidget(widgetType.Open_Shifts.getValue());
*/

            //verify switch off compliance violation widget
            liquidDashboardPage.switchOffWidget(LiquidDashboardTest.widgetType.Compliance_Violation.getValue());
            //verify switch on compliance violation widget
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Compliance_Violation.getValue());
            //verify close compliance violation widget
            liquidDashboardPage.closeWidget(LiquidDashboardTest.widgetType.Compliance_Violation.getValue());
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Compliance_Violation.getValue());

            //verify switch off helpful links widget
            liquidDashboardPage.switchOffWidget(LiquidDashboardTest.widgetType.Helpful_Links.getValue());
            //verify switch on helpful links widget
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Helpful_Links.getValue());
            //verify close helpful links widget
            liquidDashboardPage.closeWidget(LiquidDashboardTest.widgetType.Helpful_Links.getValue());
            liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Helpful_Links.getValue());
            //verify back button to get out of manage page
            liquidDashboardPage.verifyBackBtn();
            //verify if there is update time info icon
            liquidDashboardPage.saveAndExitEditMode();
            liquidDashboardPage.verifyUpdateTimeInfoIcon(LiquidDashboardTest.widgetType.Compliance_Violation.getValue());
            //liquidDashboardPage.verifyUpdateTimeInfoIcon(widgetType.Open_Shifts.getValue());
            liquidDashboardPage.verifyUpdateTimeInfoIcon(LiquidDashboardTest.widgetType.Schedules.getValue());
            liquidDashboardPage.verifyUpdateTimeInfoIcon(LiquidDashboardTest.widgetType.Starting_Soon.getValue());
            //liquidDashboardPage.verifyUpdateTimeInfoIcon(widgetType.Swaps_Covers.getValue());
            liquidDashboardPage.verifyUpdateTimeInfoIcon(LiquidDashboardTest.widgetType.Alerts.getValue());
            //liquidDashboardPage.verifyUpdateTimeInfoIcon(widgetType.Timesheet_Approval_Rate.getValue());
            liquidDashboardPage.verifyUpdateTimeInfoIcon(LiquidDashboardTest.widgetType.Timesheet_Approval_Status.getValue());
            liquidDashboardPage.verifyUpdateTimeInfoIcon(LiquidDashboardTest.widgetType.Helpful_Links.getValue());
            liquidDashboardPage.verifyUpdateTimeInfoIcon(LiquidDashboardTest.widgetType.Todays_Forecast.getValue());
            //verify search input
            liquidDashboardPage.enterEditMode();
            liquidDashboardPage.verifySearchInput(LiquidDashboardTest.widgetType.Helpful_Links.getValue());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify auto publish settings are available")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAutoPublishSettingAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();

        //Go to OP page
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to Configuration
        cinemarkMinorPage.clickConfigurationTabInOP();
        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        String templateName = "test auto publish "+String.valueOf(System.currentTimeMillis());
        cinemarkMinorPage.newTemplate(templateName);
        String s = controlsNewUIPage.getDaysInAdvancePublishSchedulesInSchedulingPolicies();
        SimpleUtils.assertOnFail("Days publish in advance is not loaded!", !("".equals(controlsNewUIPage.getDaysInAdvancePublishSchedulesInSchedulingPolicies())), false);
        List<String> optionsForAutoPublish = new ArrayList<>();
        optionsForAutoPublish.add("Auto publish on day after time");
        optionsForAutoPublish.add("Auto publish after date");
        optionsForAutoPublish.add("Disabled");
        controlsNewUIPage.updateAndVerifyAutoPublishSettings(optionsForAutoPublish.get(0));
        controlsNewUIPage.updateAndVerifyAutoPublishSettings(optionsForAutoPublish.get(1));
        controlsNewUIPage.updateAndVerifyAutoPublishSettings(optionsForAutoPublish.get(2));

        List<String> optionsForDayOfWeek = new ArrayList<>();
        optionsForDayOfWeek.add("Mon");
        optionsForDayOfWeek.add("Tue");
        optionsForDayOfWeek.add("Wed");
        optionsForDayOfWeek.add("Thu");
        optionsForDayOfWeek.add("Fri");
        optionsForDayOfWeek.add("Sat");
        optionsForDayOfWeek.add("Sun");
        optionsForDayOfWeek.add("None");

        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(0));
        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(1));
        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(2));
        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(3));
        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(4));
        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(5));
        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(6));
        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(optionsForDayOfWeek.get(7));

        controlsNewUIPage.updateAutoPublishSchedulePublishTimeOfDay("40");

        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(0));
        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(1));
        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(2));
        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(3));
        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(4));
        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(5));
        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(6));
        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(optionsForDayOfWeek.get(7));

        controlsNewUIPage.updateAutoPublishScheduleRepublishTimeOfDay("30");

        cinemarkMinorPage.saveOrPublishTemplate(CinemarkMinorTest.templateAction.Save_As_Draft.getValue());

        //delete the template.
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        configurationPage.clickOnSpecifyTemplateName(templateName, "view");
        cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Delete.getValue());
        cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.OKWhenPublish.getValue());
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify can set and save the settings for auto publish")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAutoPublishSettingCanBeSavedAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();

        //Go to OP page
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
        //go to Configuration
        cinemarkMinorPage.clickConfigurationTabInOP();
        controlsNewUIPage.clickOnControlsSchedulingPolicies();
        String templateName = "test auto publish " + System.currentTimeMillis();
        cinemarkMinorPage.newTemplate(templateName);
        String option = "Auto publish after date";
        controlsNewUIPage.updateAndVerifyAutoPublishSettings(option);

        String dayForPublish = "Wed";

        controlsNewUIPage.updateAutoPublishSchedulePublishDayOfWeek(dayForPublish);

        controlsNewUIPage.updateAutoPublishSchedulePublishTimeOfDay("40");

        String dayForRepublish = "Fri";

        controlsNewUIPage.updateAutoPublishScheduleRepublishDayOfWeek(dayForRepublish);

        controlsNewUIPage.updateAutoPublishScheduleRepublishTimeOfDay("30");

        cinemarkMinorPage.saveOrPublishTemplate(CinemarkMinorTest.templateAction.Save_As_Draft.getValue());

        //Open the template to check if the values saved successfully.
        ConfigurationPage configurationPage = pageFactory.createOpsPortalConfigurationPage();
        configurationPage.clickOnSpecifyTemplateName(templateName, "view");
        cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
        cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.OKWhenEdit.getValue());
        SimpleUtils.assertOnFail("Auto publish option was not saved successfully!", option.equalsIgnoreCase(controlsNewUIPage.getAutoPublishSettings()), false);
        SimpleUtils.assertOnFail("Auto publish day of week was not saved successfully!", dayForPublish.equalsIgnoreCase(controlsNewUIPage.getAutoPublishSchedulePublishDayOfWeek()), false);
        SimpleUtils.assertOnFail("Auto republish day of week was not saved successfully!", dayForRepublish.equalsIgnoreCase(controlsNewUIPage.getAutoPublishScheduleRepublishDayOfWeek()), false);
        SimpleUtils.assertOnFail("Auto publish time of day was not saved successfully!", "40".equalsIgnoreCase(controlsNewUIPage.getAutoPublishSchedulePublishTimeOfDay()), false);
        SimpleUtils.assertOnFail("Auto republish time of day was not saved successfully!", "30".equalsIgnoreCase(controlsNewUIPage.getAutoPublishScheduleRepublishTimeOfDay()), false);
        cinemarkMinorPage.saveOrPublishTemplate(CinemarkMinorTest.templateAction.Save_As_Draft.getValue());

        //delete the template.
        configurationPage.clickOnSpecifyTemplateName(templateName, "view");
        cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Delete.getValue());
        cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.OKWhenPublish.getValue());
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality > Forecast")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleFunctionalityForecastAsStoreManager(String username, String password, String browser, String location) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
        LiquidDashboardPage liquidDashboardPage = pageFactory.createConsoleLiquidDashboardPage();
        // Verify Edit mode Dashboard loaded
        liquidDashboardPage.enterEditMode();
        liquidDashboardPage.switchOnWidget(LiquidDashboardTest.widgetType.Schedules.getValue());
        liquidDashboardPage.saveAndExitEditMode();

        // Refresh the dashboard to get the value updated
//            dashboardPage.clickOnRefreshButton();

        //verify view schedules link
        List<String> resultListOnWidget = liquidDashboardPage.getDataOnSchedulesWidget();
        liquidDashboardPage.clickOnLinkByWidgetNameAndLinkName(LiquidDashboardTest.widgetType.Schedules.getValue(), LiquidDashboardTest.linkNames.View_Schedules.getValue());
        //verify value on widget
        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
        List<String> resultListInOverview = scheduleOverviewPage.getOverviewData();
        if (resultListOnWidget.size() > 0 && resultListInOverview.size() > 0 && resultListOnWidget.size() < resultListInOverview.size()){
            for (int i=0;i<resultListOnWidget.size();i++){
                boolean flag = false;
                String [] widget = resultListOnWidget.get(i).split(",");
                String [] overview = resultListInOverview.get(i).split(",");
                // Due to bug: https://legiontech.atlassian.net/browse/SCH-1976, the projected hour may not be consistent
                widget[widget.length - 1] = "";
                overview[overview.length - 1] = "";
                if (widget.length == overview.length && Arrays.equals(widget, overview)) {
                    flag = true;
                }
                if (flag){
                    SimpleUtils.pass("Schedules widget: Values on widget are consistent with the one in overview");
                } else {
                    SimpleUtils.fail("Schedules widget: Values on widget are not consistent with the one in overview!",false);
                }
            }
        } else {
            SimpleUtils.fail("Schedules widget: something wrong with the number of week displayed!",false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality > Labor Forecast")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyScheduleLaborForeCastFunctionalityAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
        scheduleOverviewPage.loadScheduleOverview();
        ForecastPage ForecastPage  = pageFactory.createForecastPage();
        ForecastPage.clickForecast();
        ForecastPage.clickOnLabor();
        //past+current+future week is visible and enable to navigate to future and past week by '>' and '<' button
        ForecastPage.verifyNextPreviousBtnCorrectOrNot();
        //Work role filter is selected all roles by default, can be selected one or more
        ForecastPage.verifyWorkRoleSelection();
        //After selecting any workrole, Projected Labor bar will display according to work role
        ForecastPage.verifyBudgetedHoursInLaborSummaryWhileSelectDifferentWorkRole();
        //Weather week smartcard is displayed for a week[sun-sat]
        ForecastPage.weatherWeekSmartCardIsDisplayedForAWeek();
        //If some work role has been selected in one week then these will remain selected in every past and future week
        ForecastPage.verifyWorkRoleIsSelectedAftSwitchToPastFutureWeek();
        //After click on refresh, page should get refresh and back to previous page only
        ForecastPage.verifyRefreshBtnInLaborWeekView();

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Schedule functionality > Shopper Forecast> Weather smartcard")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
    public void validateWeatherSmartCardOnForecastPageAsInternalAdmin(String username, String password, String browser, String location)
            throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

        String WeatherCardText = "WEATHER";
        ForecastPage forecastPage = pageFactory.createForecastPage();
        forecastPage.clickForecast();
        //Validate Weather Smart card on Week View
        scheduleCommonPage.clickOnWeekView();

        Thread.sleep(5000);
        String activeWeekText = scheduleCommonPage.getActiveWeekText();

        if (smartCardPage.isSmartCardAvailableByLabel(WeatherCardText)) {
            SimpleUtils.pass("Weather Forecart Smart Card appeared for week view duration: '" + activeWeekText + "'");
            String[] splitActiveWeekText = activeWeekText.split(" ");
            String smartCardTextByLabel = smartCardPage.getsmartCardTextByLabel(WeatherCardText);
            String weatherTemperature = smartCardPage.getWeatherTemperature();

            SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain starting day('" + splitActiveWeekText[0] + "') of active week: '" + activeWeekText + "'",
                    smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[0].toLowerCase()), true);

            SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain Ending day('" + splitActiveWeekText[0] + "') of active week: '" + activeWeekText + "'",
                    smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[0].toLowerCase()), true);
            if (weatherTemperature != "")
                SimpleUtils.pass("Weather Forecart Smart Card contains Temperature value: '" + weatherTemperature + "' for the duration: '" +
                        activeWeekText + "'");
            else
                SimpleUtils.fail("Weather Forecart Smart Card not contains Temperature value for the duration: '" + activeWeekText + "'", true);
        } else {
            //SimpleUtils.fail("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'", true);
            SimpleUtils.warn("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'");
        }

        //Validate Weather Smart card on day View
        scheduleCommonPage.clickOnDayView();
        for (int index = 0; index < ScheduleTestKendraScott2.dayCount.Seven.getValue(); index++) {
            if (index != 0)
                scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.One.getValue());

            String activeDayText = scheduleCommonPage.getActiveWeekText();
            if (smartCardPage.isSmartCardAvailableByLabel(WeatherCardText)) {
                SimpleUtils.pass("Weather Forecart Smart Card appeared for week view duration: '" + activeDayText + "'");
                String[] splitActiveWeekText = activeDayText.split(" ");
                String smartCardTextByLabel = smartCardPage.getsmartCardTextByLabel(WeatherCardText);
                SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain starting day('" + splitActiveWeekText[1] + "') of active day: '" + activeDayText + "'",
                        smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[1].toLowerCase()), true);
                String weatherTemperature = smartCardPage.getWeatherTemperature();
                if (weatherTemperature != "")
                    SimpleUtils.pass("Weather Forecart Smart Card contains Temperature value: '" + weatherTemperature + "' for the duration: '" +
                            activeWeekText + "'");
                else
                    SimpleUtils.pass("Weather Forecart Smart Card not contains Temperature value for the duration: '" + activeWeekText + "'");
            } else {
                //SimpleUtils.fail("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'", true);
                SimpleUtils.warn("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'");
            }
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content of new profile page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentOfNewProfilePageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Select one team member which has the account to view profile
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            teamPage.searchAndSelectTeamMemberByName("Pena");

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            //Verify User Profile Section is loaded
            profileNewUIPage.verifyUserProfileSectionIsLoaded();
            //Verify HR Profile Information Section is loaded
            profileNewUIPage.verifyHRProfileInformationSectionIsLoaded();
            //Verify Legion Information Section is loaded
            profileNewUIPage.verifyLegionInformationSectionIsLoaded();
            //Verify Actions Section is loaded
            profileNewUIPage.verifyActionSectionIsLoaded();
            //Verify the fields in User Profile Section are display correctly
            profileNewUIPage.verifyFieldsInUserProfileSection();
            //Verify the fields in HR Profile Information Section are display correctly
            profileNewUIPage.verifyFieldsInHRProfileInformationSection();
            //Verify the fields in Legion Information Section are display correctly
            profileNewUIPage.verifyFieldsInLegionInformationSection();
            //Verify the contents in Actions Section are display correctly
            profileNewUIPage.verifyContentsInActionsSection();
            //Verify Edit and Sync TM Info buttons are display correctly
            profileNewUIPage.verifyEditUserProfileButtonIsLoaded();
            profileNewUIPage.verifySyncTMInfoButtonIsLoaded();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content of new profile page in TM View")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheContentOfNewProfilePageInTMViewAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Select one team member to view profile
            dashboardPage.clickOnSubMenuOnProfile("My Profile");

            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            //Verify User Profile Section is loaded
            profileNewUIPage.verifyUserProfileSectionIsLoaded();
            //Verify HR Profile Information Section is loaded
            profileNewUIPage.verifyHRProfileInformationSectionIsLoaded();
            //Verify Legion Information Section is loaded
            profileNewUIPage.verifyLegionInformationSectionIsLoaded();
            //Verify Actions Section is loaded
            profileNewUIPage.verifyActionSectionIsLoaded();
            //Verify the fields in User Profile Section are display correctly
            profileNewUIPage.verifyFieldsInUserProfileSection();
            //Verify the fields in HR Profile Information Section are display correctly
            profileNewUIPage.verifyFieldsInHRProfileInformationSection();
            //Verify the fields in Legion Information Section are display correctly
            profileNewUIPage.verifyFieldsInLegionInformationSection();
            //Verify the contents in Actions Section are display correctly
            profileNewUIPage.verifyContentsInActionsSectionInTMView();
            //Verify Edit button is display correctly
            profileNewUIPage.verifyEditUserProfileButtonIsLoaded();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Team functionality > Work Preferences")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyTheTeamFunctionalityInWorkPreferencesAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            dashboardPage.isDashboardPageLoaded();
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.selectATeamMemberToViewProfile();
            teamPage.isProfilePageLoaded();
            teamPage.navigateToWorkPreferencesTab();
            // Verify shift preferences- can be Edit by clicking on pencil icon Changes are being Cancel by clicking on cancel button
            List<String> previousPreferences = teamPage.getShiftPreferences();
            teamPage.clickOnEditShiftPreference();
            SimpleUtils.assertOnFail("Edit Shift Preferences layout failed to load!", teamPage.isEditShiftPreferLayoutLoaded(), true);
            teamPage.setSliderForShiftPreferences();
            teamPage.changeShiftPreferencesStatus();
            teamPage.clickCancelEditShiftPrefBtn();
            List<String> currentPreferences = teamPage.getShiftPreferences();
            if (previousPreferences.containsAll(currentPreferences) && currentPreferences.containsAll(previousPreferences)) {
                SimpleUtils.pass("Shift preferences don't change after cancelling!");
            } else {
                SimpleUtils.fail("Shift preferences are changed after cancelling!", true);
            }
            // Verify shift preferences- can be Edit by clicking on pencil icon Changes are being Saved by clicking on Save button
            teamPage.clickOnEditShiftPreference();
            SimpleUtils.assertOnFail("Edit Shift Preferences layout failed to load!", teamPage.isEditShiftPreferLayoutLoaded(), true);
            List<String> changedShiftPrefs = teamPage.setSliderForShiftPreferences();
            List<String> status = teamPage.changeShiftPreferencesStatus();
            teamPage.clickSaveShiftPrefBtn();
            currentPreferences = teamPage.getShiftPreferences();
            teamPage.verifyCurrentShiftPrefIsConsistentWithTheChanged(currentPreferences, changedShiftPrefs, status);
            // Verify Availability Graph [Edited by manager/Admin]:Weekly Availability/Unavailability is showing by green/Red color
            teamPage.editOrUnLockAvailability();
            SimpleUtils.assertOnFail("Edit Availability layout failed to load!", teamPage.areCancelAndSaveAvailabilityBtnLoaded(), true);
            teamPage.changePreferredHours();
            teamPage.changeBusyHours();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

//
//    @Automated(automated ="Automated")
//    @Owner(owner = "Nora")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify the team functionality in Roster - Sort")
//    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyTheTeamFunctionalityInRosterForSort(String browser, String username, String password, String location) throws Exception {
//        try {
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//            // Check whether the location is location group or not
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//            if(isActiveWeekGenerated){
//                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//            }
//            boolean isLocationGroup = toggleSummaryPage.isLocationGroup();
//
//            // Verify TM Count is correct from roster
//            TeamPage teamPage = pageFactory.createConsoleTeamPage();
//            teamPage.goToTeam();
//            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
//            teamPage.verifyTMCountIsCorrectOnRoster();
//            // Verify Search Team Members is working correctly
//            List<String> testStrings = new ArrayList<>(Arrays.asList("jam", "boris", "Retail", "a"));
//            teamPage.verifyTheFunctionOfSearchTMBar(testStrings);
//            // Verify the column in roster page
//            teamPage.verifyTheColumnInRosterPage(isLocationGroup);
//            // Verify NAME column can be sorted in ascending or descending order
//            teamPage.verifyTheSortFunctionInRosterByColumnName("NAME");
//            // Verify EMPLOYEE ID column can be sorted in ascending or descending order
//            teamPage.verifyTheSortFunctionInRosterByColumnName("EMPLOYEE ID");
//            // Verify JOB TITLE column can be sorted in ascending or descending order
//            teamPage.verifyTheSortFunctionInRosterByColumnName("JOB TITLE");
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }

//
//    @Automated(automated ="Automated")
//    @Owner(owner = "Estelle")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify the Schedule functionality > Overview")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyScheduleFunctionalityOverviewAsStoreManager(String username, String password, String browser, String location) throws Exception {
//        try {
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTest.SchedulePageSubTabText.Overview.getValue());
//            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//
//            //	Current +2 month calendar is visible
//            SimpleUtils.assertOnFail("Current +2 month calendar is visible", scheduleOverviewPage.isCurrent2MonthCalendarVisible(), false);
//            //"<" Button is navigate to previous month calendar, After Clicking on "<" , ">" button is enabled and navigating to future month calendar
//            scheduleOverviewPage.verifyNavigation();
//            //	Schedule week & its status is visible: if it is finalized,draft,guidance,published
//            List<HashMap<String, String>> weekDuration = scheduleOverviewPage.getOverviewPageWeeksDuration();
//            List<String> scheduleWeekStatus = scheduleOverviewPage.getScheduleWeeksStatus();
//            if (scheduleWeekStatus.contains("Published") || scheduleWeekStatus.contains("Draft")
//                    || scheduleWeekStatus.contains("Finalized") || scheduleWeekStatus.contains("Guidance")) {
//                SimpleUtils.pass("Schedule week & its status is visible");
//            } else if (weekDuration.size() > 0) {
//                SimpleUtils.pass("Schedule week is visible");
//            } else
//                SimpleUtils.fail("Schedule week & its status is not visible", true);
//            //	Current week is in dark blue color
//            SimpleUtils.assertOnFail("Current Week not Highlighted!", scheduleOverviewPage.isCurrentWeekDarkBlueColor(), false);
//            //	Current Date is in Red color
//            SimpleUtils.assertOnFail("Current Date is not in Red color", scheduleOverviewPage.isCurrentDateRed(), false);
//
//            //	Weekly Budgeted/Scheduled,other hour are showing in overview and matching with the Schedule smartcard of Schedule page
//            List<WebElement> scheduleOverViewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//            HashMap<String, Float> overviewData = scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(0));
//            //	user can click on Schedule week which will navigate to Schedule page
//            scheduleOverviewPage.clickOnCurrentWeekToOpenSchedule();
//            SimpleUtils.pass("user can click on Schedule week which will navigate to Schedule page");
//
//            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
//            if (!isWeekGenerated) {
//                createSchedulePage.createScheduleForNonDGFlowNewUI();
//            }
//            HashMap<String, Float> scheduleSmartCardHoursWages = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();
//            if ((scheduleSmartCardHoursWages.get("budgetedHours") - overviewData.get("guidanceHours") <= 0.05)
//                    & (scheduleSmartCardHoursWages.get("scheduledHours") - overviewData.get("scheduledHours") <= 0.05)
//                    & (scheduleSmartCardHoursWages.get("otherHours") - overviewData.get("otherHours") <= 0.05)) {
//                SimpleUtils.pass("Schedule/Budgeted smartcard-is showing the values in Hours and wages, it is displaying the same data as overview page have for the current week .");
//            } else {
//                SimpleUtils.fail("Scheduled Hours and Overview Schedule Hours not same", false);
//            }
//
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTest.SchedulePageSubTabText.Overview.getValue());
//            //	After Generating Schedule, status will be in draft and scheduled hour will also updated.
//            List<WebElement> overviewPageScheduledWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//            Float guidanceHoursForGuidanceSchedule = 0.0f;
//            Float scheduledHoursForGuidanceSchedule = 0.0f;
//            Float otherHoursForGuidanceSchedule = 0.0f;
//            for (int i = 0; i < overviewPageScheduledWeeks.size(); i++) {
//                if (overviewPageScheduledWeeks.get(i).getText().toLowerCase().contains(ScheduleTestKendraScott2.overviewWeeksStatus.Guidance.getValue().toLowerCase())) {
//                    HashMap<String, Float> overviewDataInGuidance = scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(i));
//                    if (!overviewDataInGuidance.get("guidanceHours").equals(guidanceHoursForGuidanceSchedule) & overviewDataInGuidance.get("scheduledHours").equals(scheduledHoursForGuidanceSchedule) & overviewDataInGuidance.get("otherHours").equals(otherHoursForGuidanceSchedule)) {
//                        SimpleUtils.pass("If any week is in Guidance status, then only Budgeted hours are showing, scheduledHours and otherHours are all zero");
//                    } else
//                        SimpleUtils.fail("this status of this week is not in Guidance", false);
//
//                    String activityInfoBeforeGenerated = null;
//                    String scheduleStatusAftGenerated = null;
//                    scheduleOverviewPage.clickOnGuidanceBtnOnOverview(i);
//                    Thread.sleep(5000);
//                    if (!createSchedulePage.isWeekGenerated()) {
//                        createSchedulePage.createScheduleForNonDGFlowNewUI();
//                        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTest.SchedulePageSubTabText.Overview.getValue());
//
//                        List<String> scheduleActivityInfo = scheduleOverviewPage.getScheduleActivityInfo();
//                        String activityInfoAfterGenerateSchedule = scheduleActivityInfo.get(i);
//                        List<String> scheduleWeekStatus2 = scheduleOverviewPage.getScheduleWeeksStatus();
//                        scheduleStatusAftGenerated = scheduleWeekStatus2.get(i);
//                        if (scheduleStatusAftGenerated.equals("Draft")) {
//                            SimpleUtils.pass("After Generating Schedule, status will be in draft");
//                        }
//                        if (activityInfoAfterGenerateSchedule.contains(username.substring(0, 6)) & !activityInfoAfterGenerateSchedule.equals(activityInfoBeforeGenerated)) {
//                            //	whoever made the changes in Schedule defined in Activity
//                            //	Profile icon of user is in round shape and his/her name is showing along with Time and date[when he/she has made the changes]
//                            SimpleUtils.pass("Profile icon of user is updated by current user");
//                        }
//                    }
//                    break;
//
//                } else {
//                    SimpleUtils.report("there is no guidance schedule");
//                }
//            }
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the content of Activities page after click on the Activities button")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOfActivityPageAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.verifyFiveActivityButtonsLoaded();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate to close Activity Feed")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyToCloseActivityFeedAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.verifyClickOnActivityCloseButton();
            if (activityPage.isActivityContainerPoppedUp()) {
                SimpleUtils.fail("Activity pop up container is not closed!", false);
            } else {
                SimpleUtils.pass("Activity pop up container is closed Successfully!");
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the navigation in each tab is normal on Activities page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyNavigationOfEachTabOnActivityAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.TimeOff.getValue(), ActivityTest.indexOfActivityType.TimeOff.name());
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.Schedule.getValue(), ActivityTest.indexOfActivityType.Schedule.name());
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated ="Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate access controls on Activities page when logon with Admin/TM or SM switch to employer view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyAccessControlsOnActivitiesPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();

            //Verify Activity Feed as admin
            if (!activityPage.isActivityBellIconLoaded())
                SimpleUtils.pass("Admin view have no access to see Activity Feed as expected");
            else SimpleUtils.fail("Admin view can see Activity Feed unexpectedly",false);
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Verify Activity Feed as Store Manager
            String fileName = "UsersCredentials.json";
            fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise")+fileName;
            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
            Object[][] teamMemberCredentials = userCredentials.get("StoreManager");
            loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
                    , String.valueOf(teamMemberCredentials[0][2]));
            dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
            if (activityPage.isActivityBellIconLoaded()) {
                SimpleUtils.pass("SM view have access to see Activity Feed successfully");
            } else {
                SimpleUtils.fail("SM view failed to see Activity Feed",true);
            }

            // Verify Activity Feed as Store Manager Employee View
            dashboardPage.clickOnProfileIconOnDashboard();
            dashboardPage.clickOnSwitchToEmployeeView();
            if (!activityPage.isActivityBellIconLoaded()) {
                SimpleUtils.pass("SM Employee view have no access to see Activity Feed successfully");
            } else {
                SimpleUtils.warn("SM Employee view still have access to see Activity Feed unexpectedly since this bug: https://legiontech.atlassian.net/browse/SF-323");
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated ="Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the content when there is no notification in every activity tab")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheContentOnActivityAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyClickOnActivityIcon();
            activityPage.verifyTheContentOnActivity();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


//    @Automated(automated ="Automated")
//    @Owner(owner = "Estelle")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Validate the activity of publish or update schedule")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
//    public void verifyActivityOfPublishUpdateScheduleAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//
//        scheduleCommonPage.navigateToNextWeek();
//        //make publish schedule activity
//        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//        if (isActiveWeekGenerated){
//            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//        }
//        createSchedulePage.createScheduleForNonDGFlowNewUI();
//        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
//        scheduleMainPage.saveSchedule();
//        createSchedulePage.publishActiveSchedule();
//        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//        String requestUserName = profileNewUIPage.getNickNameFromProfile();
//        LoginPage loginPage = pageFactory.createConsoleLoginPage();
//        loginPage.logOut();
//
//
//        // Login as Store Manager
//        String fileName = "UserCredentialsForComparableSwapShifts.json";
//        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//        fileName = "UsersCredentials.json";
//        fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise")+fileName;
//        userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//        Object[][] teamMemberCredentials = userCredentials.get("StoreManager");
//        loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                , String.valueOf(teamMemberCredentials[0][2]));
//        dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//        scheduleCommonPage.navigateToNextWeek();
//
//        // Verify Schedule publish activity are loaded
//
//        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//        activityPage.verifyActivityBellIconLoaded();
//        activityPage.verifyClickOnActivityIcon();
//        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.Schedule.getValue(), ActivityTest.indexOfActivityType.Schedule.name());
//        activityPage.verifyActivityOfPublishSchedule(requestUserName);
//        activityPage.verifyClickOnActivityCloseButton();
//
//        //make update schedule activity to add one open shift
//        //schedulePage.clickOnDayView();
//        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
//        // This method is used for the old UI
//        //schedulePage.clickNewDayViewShiftButtonLoaded();
//        newShiftPage.clickOnDayViewAddNewShiftButton();
//        newShiftPage.customizeNewShiftPage();
//        newShiftPage.moveSliderAtCertainPoint("6:00pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//        newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//        newShiftPage.selectWorkRole(scheduleWorkRoles.get("GENERAL MANAGER"));
//        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
//        newShiftPage.clickOnCreateOrNextBtn();
//        scheduleMainPage.saveSchedule();
//        createSchedulePage.publishActiveSchedule();
//
//
//        // Verify Schedule update activity are
//
//        String requestUserNameSM = profileNewUIPage.getNickNameFromProfile();
//        activityPage.verifyActivityBellIconLoaded();
//        activityPage.verifyClickOnActivityIcon();
//        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.Schedule.getValue(), ActivityTest.indexOfActivityType.Schedule.name());
//        activityPage.verifyActivityOfUpdateSchedule(requestUserNameSM);
//    }


//
//    @Automated(automated = "Automated")
//    @Owner(owner = "Estelle")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate activity for claim the open shift")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyActivityOfClaimOpenShiftAsTeamLead(String browser, String username, String password, String location) throws Exception {
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//        String teamMemberName = profileNewUIPage.getNickNameFromProfile();
//        LoginPage loginPage = pageFactory.createConsoleLoginPage();
//        loginPage.logOut();
//
//        String fileName = "UsersCredentials.json";
//        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//        fileName = SimpleUtils.getEnterprise("KendraScott2_Enterprise") + fileName;
//        userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//        Object[][] credential = userCredentials.get("InternalAdmin");
//        loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1]), String.valueOf(credential[0][2]));
//
//        // 1.Checking configuration in controls
//        String option = "Always";
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//        controlsNewUIPage.clickOnControlsConsoleMenu();
//        controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
//        boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
//        SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
//        //String selectedOption = controlsNewUIPage.getIsApprovalByManagerRequiredWhenEmployeeClaimsOpenShiftSelectedOption();
//        controlsNewUIPage.updateOpenShiftApprovedByManagerOption(option);
//        // 2.admin create one manual open shift and assign to specific TM
//
//        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//        //to generate schedule  if current week is not generated
//        scheduleCommonPage.navigateToNextWeek();
//        boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//        if(!isActiveWeekGenerated){
//            createSchedulePage.createScheduleForNonDGFlowNewUI();
//        }
//        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
//        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(teamMemberName);
//        scheduleMainPage.saveSchedule();
//
//        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//        newShiftPage.clickOnDayViewAddNewShiftButton();
//        newShiftPage.customizeNewShiftPage();
//        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//        newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//        newShiftPage.selectWorkRole(scheduleWorkRoles.get("MOD"));
//        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
//        newShiftPage.clickOnCreateOrNextBtn();
//        newShiftPage.searchTeamMemberByName(teamMemberName);
//        newShiftPage.clickOnOfferOrAssignBtn();
//        scheduleMainPage.saveSchedule();
//        createSchedulePage.publishActiveSchedule();
//        loginPage.logOut();
//
//        // 3.Login with the TM to claim the shift
//        loginToLegionAndVerifyIsLoginDone(username, password, location);
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//        dashboardPage.goToTodayForNewUI();
//        scheduleCommonPage.navigateToNextWeek();
//        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//        String cardName = "WANT MORE HOURS?";
//        SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
//        String linkName = "View Shifts";
//        smartCardPage.clickLinkOnSmartCardByName(linkName);
//        SimpleUtils.assertOnFail("Open shifts not loaed Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
//        List<String> claimShift = new ArrayList<>(Arrays.asList("Claim Shift"));
//        mySchedulePage.selectOneShiftIsClaimShift(claimShift);
//        mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
//        mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();
//
//        loginPage.logOut();
//
//        // 4.Login with SM to check activity
//        Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//        loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
//                , String.valueOf(storeManagerCredentials[0][2]));
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//        activityPage.verifyActivityBellIconLoaded();
//        activityPage.verifyClickOnActivityIcon();
//        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftOffer.getValue(), ActivityTest.indexOfActivityType.ShiftOffer.name());
//        activityPage.verifyActivityOfShiftOffer(teamMemberName);
//        activityPage.approveOrRejectShiftOfferRequestOnActivity(teamMemberName, ActivityTest.approveRejectAction.Approve.getValue());
//
//    }


//    @Automated(automated ="Automated")
//    @Owner(owner = "Julie")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate the functioning of Reject button on pending Reject for Cover the shift")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
//    public void verifyRejectCoverRequestOfShiftSwapActivityAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        // Configuration in controls :Is approval by Manager required when an employee claims a shift swap or cover request?-Always
//        // TM's next week's schedule must be published before running this test case
//        // Cover TM should be in the list of Cover Request Status window
//        // Cover TM should be not on the schedule at the same day with requested TM and is defined in "UserCredentialsForComparableSwapShifts.json"
//        try {
//            prepareTheSwapShiftsAsInternalAdmin(browser, username, password, location);
//            SimpleUtils.report("Need to set 'Is approval by Manager required when an employee claims a shift swap or cover request?' to 'Always' First!");
//            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
//            controlsPage.gotoControlsPage();
//            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            SimpleUtils.assertOnFail("Controls Page not loaded Successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
//            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
//            SimpleUtils.assertOnFail("Schedule Collaboration Page not loaded Successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
//            String option = "Always";
//            controlsNewUIPage.updateSwapAndCoverRequestIsApprovalRequired(option);
//
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//
//            List<String> swapNames = new ArrayList<>();
//            String fileName = "UserCredentialsForComparableSwapShifts.json";
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            for (Map.Entry<String, Object[][]> entry : userCredentials.entrySet()) {
//                if (!entry.getKey().equals("Cover TM")) {
//                    swapNames.add(entry.getKey());
//                    SimpleUtils.pass("Get Swap User name: " + entry.getKey());
//                }
//            }
//            Object[][] credential = null;
//            credential = userCredentials.get(swapNames.get(0));
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//                    , String.valueOf(credential[0][2]));
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String requestUserName = profileNewUIPage.getNickNameFromProfile();
//            if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//                dashboardPage.clickOnSwitchToEmployeeView();
//            }
//
//            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToNextWeek();
//
//            // For Cover Feature
//            List<String> swapCoverRequests = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
//            int index = mySchedulePage.verifyClickOnAnyShift();
//            String request = "Request to Cover Shift";
//            mySchedulePage.clickTheShiftRequestByName(request);
//            // Validate the Submit button feature
//            String title = "Submit Cover Request";
//            SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//            mySchedulePage.verifyClickOnSubmitButton();
//
//            loginPage.logOut();
//
//            credential = userCredentials.get(swapNames.get(1));
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//                    , String.valueOf(credential[0][2]));
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            String coverName = profileNewUIPage.getNickNameFromProfile();
//            if (dashboardPage.isSwitchToEmployeeViewPresent())
//                dashboardPage.clickOnSwitchToEmployeeView();
//            dashboardPage.goToTodayForNewUI();
//            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToNextWeek();
//
//            // Validate that smartcard is available to recipient team member
//            String smartCard = "WANT MORE HOURS?";
//            SimpleUtils.assertOnFail("Smart Card: " + smartCard + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(smartCard), false);
//            // Validate the availability of all cover request shifts in schedule table
//            String linkName = "View Shifts";;
//            smartCardPage.clickLinkOnSmartCardByName(linkName);
//            SimpleUtils.assertOnFail("Open shifts not loaded Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
//            // Validate the availability of Claim Shift Request popup
//            String requestName = "Claim Shift";
//            mySchedulePage.clickTheShiftRequestToClaimShift(requestName, requestUserName);
//            // Validate the clickability of I Agree button
//            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();
//
//            loginPage.logOut();
//
//            // Login as Store Manager
//            fileName = "UsersCredentials.json";
//            fileName = SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//            userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            Object[][] teamMemberCredentials = userCredentials.get("StoreManager");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                    , String.valueOf(teamMemberCredentials[0][2]));
//            dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//            // Verify Activity Icon is loaded and approve the cover shift request
//            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//            activityPage.verifyActivityBellIconLoaded();
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
//            activityPage.approveOrRejectShiftCoverRequestOnActivity(requestUserName, coverName, ActivityTest.approveRejectAction.Reject.getValue());
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(),false);
//        }
//    }

//    @Automated(automated ="Automated")
//    @Owner(owner = "Nora")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate the content of Shift Swap activity when TM request to swap the shift")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
//    public void verifyTheContentOfShiftSwapActivityAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        prepareTheSwapShiftsAsInternalAdmin(browser, username, password, location);
//        SimpleUtils.report("Need to set 'Is approval by Manager required when an employee claims a shift swap or cover request?' to 'Always' First!");
//        ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
//        controlsPage.gotoControlsPage();
//        ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//        SimpleUtils.assertOnFail("Controls Page not loaded Successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
//        controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
//        SimpleUtils.assertOnFail("Schedule Collaboration Page not loaded Successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
//        String option = "Always";
//        controlsNewUIPage.updateSwapAndCoverRequestIsApprovalRequired(option);
//
//        LoginPage loginPage = pageFactory.createConsoleLoginPage();
//        loginPage.logOut();
//
//        List<String> swapNames = new ArrayList<>();
//        String fileName = "UserCredentialsForComparableSwapShifts.json";
//        HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//        for (Map.Entry<String, Object[][]> entry : userCredentials.entrySet()) {
//            if (!entry.getKey().equals("Cover TM")) {
//                swapNames.add(entry.getKey());
//                SimpleUtils.pass("Get Swap User name: " + entry.getKey());
//            }
//        }
//        Object[][] credential = null;
//        credential = userCredentials.get(swapNames.get(0));
//        loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//                , String.valueOf(credential[0][2]));
//        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//        String requestUserName = profileNewUIPage.getNickNameFromProfile();
//        if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//            dashboardPage.clickOnSwitchToEmployeeView();
//        }
//
//        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//        scheduleCommonPage.navigateToNextWeek();
//        scheduleCommonPage.navigateToNextWeek();
//
//        // For Swap Feature
//        List<String> swapCoverRequsts = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
//        int index = mySchedulePage.verifyClickOnAnyShift();
//        String request = "Request to Swap Shift";
//        String title = "Find Shifts to Swap";
//        mySchedulePage.clickTheShiftRequestByName(request);
//        SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//        mySchedulePage.verifyComparableShiftsAreLoaded();
//        mySchedulePage.verifySelectMultipleSwapShifts();
//        // Validate the Submit button feature
//        mySchedulePage.verifyClickOnNextButtonOnSwap();
//        title = "Submit Swap Request";
//        SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//        mySchedulePage.verifyClickOnSubmitButton();
//        // Validate the disappearence of Request to Swap and Request to Cover option
//        mySchedulePage.clickOnShiftByIndex(index);
//        if (!mySchedulePage.verifyShiftRequestButtonOnPopup(swapCoverRequsts)) {
//            SimpleUtils.pass("Request to Swap and Request to Cover options are disappear");
//        }else {
//            SimpleUtils.fail("Request to Swap and Request to Cover options are still shown!", false);
//        }
//
//        loginPage.logOut();
//        credential = userCredentials.get(swapNames.get(1));
//        loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//                , String.valueOf(credential[0][2]));
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//        String respondUserName = profileNewUIPage.getNickNameFromProfile();
//        if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//            dashboardPage.clickOnSwitchToEmployeeView();
//        }
//        dashboardPage.goToTodayForNewUI();
//        scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//        scheduleCommonPage.navigateToNextWeek();
//        scheduleCommonPage.navigateToNextWeek();
//
//        // Validate that swap request smartcard is available to recipient team member
//        String smartCard = "SWAP REQUESTS";
//        smartCardPage.isSmartCardAvailableByLabel(smartCard);
//        // Validate the availability of all swap request shifts in schedule table
//        String linkName = "View All";
//        smartCardPage.clickLinkOnSmartCardByName(linkName);
//        mySchedulePage.verifySwapRequestShiftsLoaded();
//        // Validate that recipient can claim the swap request shift.
//        mySchedulePage.verifyClickAcceptSwapButton();
//
//        loginPage.logOut();
//
//        // Login as Store Manager
//        fileName = "UsersCredentials.json";
//        fileName = SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//        userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//        Object[][] teamMemberCredentials = userCredentials.get("StoreManager");
//        loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                , String.valueOf(teamMemberCredentials[0][2]));
//        dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//        // Verify Activity Icon is loaded
//        String actionLabel = "requested";
//        ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//        activityPage.verifyActivityBellIconLoaded();
//        activityPage.verifyClickOnActivityIcon();
//        activityPage.verifyNewShiftSwapCardShowsOnActivity(requestUserName, respondUserName, actionLabel, true);
//        activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
//        activityPage.verifyNewShiftSwapCardShowsOnActivity(requestUserName, respondUserName, actionLabel, false);
//        activityPage.approveOrRejectShiftSwapRequestOnActivity(requestUserName, respondUserName, ActivityTest.approveRejectAction.Approve.getValue());
//    }
//
//    @Automated(automated ="Automated")
//    @Owner(owner = "Julie")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate the content of Shift Swap activity when TM request to swap the shift")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
//    public void verifyTheContentOfShiftSwapActivityAsStoreManager(String browser, String username, String password, String location) throws Exception {
//        try {
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
//            activityPage.verifyTheContentOfShiftSwapActivity();
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(),false);
//        }
//    }
//
//    @Automated(automated ="Automated")
//    @Owner(owner = "Nora")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Validate the functioning of Approve button on pending Approval for swap the shift")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
//    public void verifyTheFunctionOfShiftSwapActivityAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//        try {
//            prepareTheSwapShiftsAsInternalAdmin(browser, username, password, location);
//            SimpleUtils.report("Need to set 'Is approval by Manager required when an employee claims a shift swap or cover request?' to 'Always' First!");
//            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
//            controlsPage.gotoControlsPage();
//            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            SimpleUtils.assertOnFail("Controls Page not loaded Successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
//            controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
//            SimpleUtils.assertOnFail("Schedule Collaboration Page not loaded Successfully!", controlsNewUIPage.isControlsScheduleCollaborationLoaded(), false);
//            String option = "Always";
//            controlsNewUIPage.updateSwapAndCoverRequestIsApprovalRequired(option);
//
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//
//            List<String> swapNames = new ArrayList<>();
//            String fileName = "UserCredentialsForComparableSwapShifts.json";
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            for (Map.Entry<String, Object[][]> entry : userCredentials.entrySet()) {
//                if (!entry.getKey().equals("Cover TM")) {
//                    swapNames.add(entry.getKey());
//                    SimpleUtils.pass("Get Swap User name: " + entry.getKey());
//                }
//            }
//            Object[][] credential = null;
//            credential = userCredentials.get(swapNames.get(0));
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//                    , String.valueOf(credential[0][2]));
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String requestUserName = profileNewUIPage.getNickNameFromProfile();
//            if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//                dashboardPage.clickOnSwitchToEmployeeView();
//            }
//
//            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToNextWeek();
//
//            // For Swap Feature
//            List<String> swapCoverRequsts = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
//            int index = mySchedulePage.verifyClickOnAnyShift();
//            String request = "Request to Swap Shift";
//            String title = "Find Shifts to Swap";
//            mySchedulePage.clickTheShiftRequestByName(request);
//            SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), true);
//            mySchedulePage.verifyComparableShiftsAreLoaded();
//            mySchedulePage.verifySelectMultipleSwapShifts();
//            // Validate the Submit button feature
//            mySchedulePage.verifyClickOnNextButtonOnSwap();
//            title = "Submit Swap Request";
//            SimpleUtils.assertOnFail(title + " page not loaded Successfully!", mySchedulePage.isPopupWindowLoaded(title), false);
//            mySchedulePage.verifyClickOnSubmitButton();
//            // Validate the disappearence of Request to Swap and Request to Cover option
//            mySchedulePage.clickOnShiftByIndex(index);
//            if (!mySchedulePage.verifyShiftRequestButtonOnPopup(swapCoverRequsts)) {
//                SimpleUtils.pass("Request to Swap and Request to Cover options are disappear");
//            } else {
//                SimpleUtils.fail("Request to Swap and Request to Cover options are still shown!", false);
//            }
//
//            loginPage.logOut();
//
//            credential = userCredentials.get(swapNames.get(1));
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(credential[0][0]), String.valueOf(credential[0][1])
//                    , String.valueOf(credential[0][2]));
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            String respondUserName = profileNewUIPage.getNickNameFromProfile();
//            if (dashboardPage.isSwitchToEmployeeViewPresent()) {
//                dashboardPage.clickOnSwitchToEmployeeView();
//            }
//            dashboardPage.goToTodayForNewUI();
//            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToNextWeek();
//
//            // Validate that swap request smartcard is available to recipient team member
//            String smartCard = "SWAP REQUESTS";
//            smartCardPage.isSmartCardAvailableByLabel(smartCard);
//            // Validate the availability of all swap request shifts in schedule table
//            String linkName = "View All";
//            smartCardPage.clickLinkOnSmartCardByName(linkName);
//            mySchedulePage.verifySwapRequestShiftsLoaded();
//            // Validate that recipient can claim the swap request shift.
//            mySchedulePage.verifyClickAcceptSwapButton();
//
//            loginPage.logOut();
//
//            // Login as Store Manager
//            fileName = "UsersCredentials.json";
//            fileName = SimpleUtils.getEnterprise("KendraScott2_Enterprise") + fileName;
//            userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            Object[][] teamMemberCredentials = userCredentials.get("StoreManager");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                    , String.valueOf(teamMemberCredentials[0][2]));
//            dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//            // Verify Activity Icon is loaded
//            String actionLabel = "requested";
//            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//            activityPage.verifyActivityBellIconLoaded();
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.verifyNewShiftSwapCardShowsOnActivity(requestUserName, respondUserName, actionLabel, true);
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ShiftSwap.getValue(), ActivityTest.indexOfActivityType.ShiftSwap.name());
//            activityPage.verifyNewShiftSwapCardShowsOnActivity(requestUserName, respondUserName, actionLabel, false);
//            List<String> swapData = activityPage.getShiftSwapDataFromActivity(requestUserName, respondUserName);
//            activityPage.approveOrRejectShiftSwapRequestOnActivity(requestUserName, respondUserName, ActivityTest.approveRejectAction.Approve.getValue());
//            activityPage.closeActivityWindow();
//
//            // Go to Schedule page to check whether the shifts are swapped
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToNextWeek();
//            mySchedulePage.verifyShiftsAreSwapped(swapData);
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }

// Blocking by https://legiontech.atlassian.net/browse/SCH-3170
//    @Owner(owner = "Haya")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify the notification when TM is requesting time off")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyTheNotificationForRequestTimeOffAsTeamMember(String browser, String username, String password, String location) {
//        try {
//            // Login as Team Member to create time off
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//
//            String fileName = "UsersCredentials.json";
//            fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise")+fileName;
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String requestUserName = profileNewUIPage.getNickNameFromProfile();
//            String myTimeOffLabel = "My Time Off";
//            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myTimeOffLabel);
//            profileNewUIPage.cancelAllTimeOff();
//            profileNewUIPage.clickOnCreateTimeOffBtn();
//            SimpleUtils.assertOnFail("New time off request window not loaded Successfully!", profileNewUIPage.isNewTimeOffWindowLoaded(), false);
//            String timeOffReasonLabel = "JURY DUTY";
//            // select time off reason
//            profileNewUIPage.selectTimeOffReason(timeOffReasonLabel);
//            profileNewUIPage.selectStartAndEndDate();
//            profileNewUIPage.clickOnSaveTimeOffRequestBtn();
//            loginPage.logOut();
//
//            // Login as Store Manager again to check message and reject
//            String RequstTimeOff = "requested";
//            Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
//                    , String.valueOf(storeManagerCredentials[0][2]));
//            String respondUserName = profileNewUIPage.getNickNameFromProfile();
//            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.TimeOff.getValue(), ActivityTest.indexOfActivityType.TimeOff.name());
//            activityPage.verifyTheNotificationForReqestTimeOff(requestUserName, getTimeOffStartTime(),getTimeOffEndTime(), RequstTimeOff);
//            activityPage.approveOrRejectTTimeOffRequestOnActivity(requestUserName,respondUserName, ActivityTest.approveRejectAction.Approve.getValue());
//            //activityPage.closeActivityWindow();
//            loginPage.logOut();
//
//            // Login as Team Member to create time off
//            loginToLegionAndVerifyIsLoginDone(username, password, location);
//            profileNewUIPage.clickOnUserProfileImage();
//            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myTimeOffLabel);
//            profileNewUIPage.cancelAllTimeOff();
//            profileNewUIPage.clickOnCreateTimeOffBtn();
//            SimpleUtils.assertOnFail("New time off request window not loaded Successfully!", profileNewUIPage.isNewTimeOffWindowLoaded(), false);
//            //select time off reason
//            profileNewUIPage.selectTimeOffReason(timeOffReasonLabel);
//            profileNewUIPage.selectStartAndEndDate();
//            profileNewUIPage.clickOnSaveTimeOffRequestBtn();
//            loginPage.logOut();
//
//            // Login as Store Manager again to check message and approve
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
//                    , String.valueOf(storeManagerCredentials[0][2]));
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.TimeOff.getValue(), ActivityTest.indexOfActivityType.TimeOff.name());
//            activityPage.verifyTheNotificationForReqestTimeOff(requestUserName, getTimeOffStartTime(),getTimeOffEndTime(), RequstTimeOff);
//            activityPage.approveOrRejectTTimeOffRequestOnActivity(requestUserName,respondUserName, ActivityTest.approveRejectAction.Reject.getValue());
//            //activityPage.closeActivityWindow();
//            loginPage.logOut();
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }
//
//    @Automated(automated ="Automated")
//    @Owner(owner = "Haya")
//    @Enterprise(name = "KendraScott2_Enterprise")
//    @TestName(description = "Verify the notification when TM cancels time off request")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyTheNotificationForCancelTimeOffAsTeamMember(String browser, String username, String password, String location) {
//        try {
//            // Login as Team member to create the time off request
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            String fileName = "UsersCredentials.json";
//            fileName = SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String requestUserName = profileNewUIPage.getNickNameFromProfile();
//            String myProfileLabel = "My Profile";
//            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
//            SimpleUtils.assertOnFail("Profile page not loaded Successfully!", profileNewUIPage.isProfilePageLoaded(), false);
//            String aboutMeLabel = "About Me";
//            profileNewUIPage.selectProfilePageSubSectionByLabel(aboutMeLabel);
//            String myTimeOffLabel = "My Time Off";
//            profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffLabel);
//            String timeOffReasonLabel = "JURY DUTY";
//            profileNewUIPage.cancelAllTimeOff();
//            profileNewUIPage.clickOnCreateTimeOffBtn();
//            SimpleUtils.assertOnFail("New time off request window not loaded Successfully!", profileNewUIPage.isNewTimeOffWindowLoaded(), false);
//            // select time off reason
//            profileNewUIPage.selectTimeOffReason(timeOffReasonLabel);
//            List<String> startNEndDates = profileNewUIPage.selectStartAndEndDate();
//            profileNewUIPage.clickOnSaveTimeOffRequestBtn();
//            profileNewUIPage.cancelAllTimeOff();
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//
//            // Login as Store Manager again to check message
//            String RequstTimeOff = "cancelled";
//            Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
//                    , String.valueOf(storeManagerCredentials[0][2]));
//            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.TimeOff.getValue(), ActivityTest.indexOfActivityType.TimeOff.name());
//            activityPage.verifyTheNotificationForReqestTimeOff(requestUserName,getTimeOffStartTime(),getTimeOffEndTime(),RequstTimeOff);
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }

//    Need update control setting
//    @Automated(automated ="Automated")
//    @Owner(owner = "Haya")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify the notification when TM updates availability for a specific week with config Not required")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyNotificationForUpdateAvailability4SpecificWeekWithConfNOAsInternalAdmin(String browser, String username, String password, String location) {
//        try {
//            // Login with Store Manager Credentials
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            // Set availability policy
//            Thread.sleep(5000);
//            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
//            controlsPage.gotoControlsPage();
//            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
//            controlsNewUIPage.clickOnControlsSchedulingPolicies();
//            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
//            controlsNewUIPage.clickOnGlobalLocationButton();
//            String isApprovalRequired = "Not required";
//            controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(isApprovalRequired);
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//
//            //Login as Team Member to change availability
//            String fileName = "UsersCredentials.json";
//            fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise")+fileName;
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            Object[][] teamMemberCredentials = userCredentials.get("TeamMember");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                    , String.valueOf(teamMemberCredentials[0][2]));
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String requestUserName = profileNewUIPage.getNickNameFromProfile();
//            loginPage.logOut();
//
//            // Login as Internal Admin again
//            loginToLegionAndVerifyIsLoginDone(username, password, location);
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//            // Go to Team Roster, search the team member
//            TeamPage teamPage = pageFactory.createConsoleTeamPage();
//            teamPage.goToTeam();
//            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
//            teamPage.searchAndSelectTeamMemberByName(requestUserName);
//            String workPreferencesLabel = "Work Preferences";
//            profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
//            profileNewUIPage.approveAllPendingAvailabilityRequest();
//            loginPage.logOut();
//
//            // Login as Team Member again
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                    , String.valueOf(teamMemberCredentials[0][2]));
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            profileNewUIPage.getNickNameFromProfile();
//            String myWorkPreferencesLabel = "My Work Preferences";
//            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
//            //Update Preferred And Busy Hours
//            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
//                profileNewUIPage.clickNextWeek();
//            }
//            String weekInfo = profileNewUIPage.getAvailabilityWeek();
//            int sliderIndex = 1;
//            double hours = -0.5;//move 1 metric 0.5h left
//            String leftOrRightDuration = "Right"; //move the right bar
//            String hoursType = "When I prefer to work";
//            String repeatChanges = "This week only";
//            profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
//                    hours, repeatChanges);
//            loginPage.logOut();
//
//            // Login as Store Manager again to check message
//            Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
//                    , String.valueOf(storeManagerCredentials[0][2]));
//            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
//            String requestAwailabilityChangeLabel = "request";
//            activityPage.verifyNotificationForUpdateAvailability(requestUserName,isApprovalRequired,requestAwailabilityChangeLabel,weekInfo,repeatChanges);
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }

// blocking by https://legiontech.atlassian.net/browse/SCH-3109
//    @Automated(automated ="Automated")
//    @Owner(owner = "Haya")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify the notification when TM requests availability from a week onwards")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyNotificationForUpdateAvailabilityRepeatForwardWithConfYesAsInternalAdmin(String browser, String username, String password, String location) {
//        try {
//            // Login with Store Manager Credentials
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            // Set availability policy
//            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
//            controlsPage.gotoControlsPage();
//            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
//
//            dashboardPage.navigateToDashboard();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            controlsPage.gotoControlsPage();
//            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
//
//            controlsNewUIPage.clickOnControlsSchedulingPolicies();
//            SimpleUtils.assertOnFail("Scheduling policy page not loaded successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
//
//            controlsNewUIPage.clickOnGlobalLocationButton();
//            String isApprovalRequired = "Required for all changes";
//            controlsNewUIPage.updateAvailabilityManagementIsApprovalRequired(isApprovalRequired);
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//
//            //Login as Team Member to change availability
//            String fileName = "UsersCredentials.json";
//            fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise")+fileName;
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            Object[][] teamMemberCredentials = userCredentials.get("TeamMember");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                    , String.valueOf(teamMemberCredentials[0][2]));
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String requestUserName = profileNewUIPage.getNickNameFromProfile();
//            loginPage.logOut();
//
//            // Login as Internal Admin again
//            loginToLegionAndVerifyIsLoginDone(username, password, location);
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//            // Go to Team Roster, search the team member
//            TeamPage teamPage = pageFactory.createConsoleTeamPage();
//            teamPage.goToTeam();
//            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
//            teamPage.searchAndSelectTeamMemberByName(requestUserName);
//            String workPreferencesLabel = "Work Preferences";
//            profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
//            profileNewUIPage.approveAllPendingAvailabilityRequest();
//            loginPage.logOut();
//
//            // Login as Team Member again
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1])
//                    , String.valueOf(teamMemberCredentials[0][2]));
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//            profileNewUIPage.getNickNameFromProfile();
//            String myWorkPreferencesLabel = "My Work Preferences";
//            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myWorkPreferencesLabel);
//            //Update Preferred And Busy Hours
//            while (profileNewUIPage.isMyAvailabilityLockedNewUI()){
//                profileNewUIPage.clickNextWeek();
//            }
//            String weekInfo = profileNewUIPage.getAvailabilityWeek();
//            int sliderIndex = 1;
//            double hours = -0.5;//move 1 metric 0.5h left
//            String leftOrRightDuration = "Right";
//            String hoursType = "When I prefer to work";
//            String repeatChanges = "repeat forward";
//            profileNewUIPage.updateMyAvailability(hoursType, sliderIndex, leftOrRightDuration,
//                    hours, repeatChanges);
//            loginPage.logOut();
//
//            // Login as Store Manager again to check message
//            Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
//                    , String.valueOf(storeManagerCredentials[0][2]));
//            String respondUserName = profileNewUIPage.getNickNameFromProfile();
//            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
//            activityPage.verifyClickOnActivityIcon();
//            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
//            String requestAwailabilityChangeLabel = "requested";
//            activityPage.verifyNotificationForUpdateAvailability(requestUserName,isApprovalRequired,requestAwailabilityChangeLabel,weekInfo,repeatChanges);
//            activityPage.approveOrRejectTTimeOffRequestOnActivity(requestUserName,respondUserName, ActivityTest.approveRejectAction.Reject.getValue());
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }


    @Automated(automated ="Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the notification when TM updates work preferences")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass= CredentialDataProviderSource.class)
    public void verifyTheNotificationOfWorkPreferencesAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String tmName = profileNewUIPage.getNickNameFromProfile();
            String myProfileLabel = "My Profile";
            profileNewUIPage.selectProfileSubPageByLabelOnProfileImage(myProfileLabel);
            SimpleUtils.assertOnFail("Profile page not loaded Successfully!", profileNewUIPage.isProfilePageLoaded(), false);
            String workPreferencesLabel = "My Work Preferences";
            profileNewUIPage.selectProfilePageSubSectionByLabel(workPreferencesLabel);
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.isWorkPreferencesPageLoaded();

            // Team Member updates the work Preferences
            teamPage.clickOnEditShiftPreference();
            SimpleUtils.assertOnFail("Edit Shift Preferences layout failed to load!", teamPage.isEditShiftPreferLayoutLoaded(), true);
            teamPage.setSliderForShiftPreferences();
            teamPage.changeShiftPreferencesStatus();
            teamPage.clickSaveShiftPrefBtn();

            // Team Member logout
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Login as Store Manager to check the activity
            loginAsDifferentRole(AccessRoles.StoreManager.getValue());
            dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Verify Activity Icon is loaded
            ActivityPage activityPage = pageFactory.createConsoleActivityPage();
            activityPage.verifyActivityBellIconLoaded();
            activityPage.verifyClickOnActivityIcon();
            activityPage.clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
            activityPage.verifyNewWorkPreferencesCardShowsOnActivity(tmName);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


//    @Automated(automated = "Automated")
//    @Owner(owner = "Julie")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify dashboard functionality when login through TM View")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyDashboardFunctionalityAsTeamMember(String browser, String username, String password, String location) throws Exception {
//        try {
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String nickName = profileNewUIPage.getNickNameFromProfile();
//
//            //T1838579 Validate the TM accessible tabs.
//            dashboardPage.validateTMAccessibleTabs();
//
//            //T1838580 Validate the presence of location.
//            dashboardPage.validateThePresenceOfLocation();
//
//            //T1838581 Validate the accessible location.
//            dashboardPage.validateTheAccessibleLocation();
//
//            //T1838582 Validate the presence of logo.
//            dashboardPage.validateThePresenceOfLogo();
//
//            //T1838584 Validate the visibility of Username.
//            dashboardPage.validateTheVisibilityOfUsername(nickName);
//
//            //T1838583 Validate the information after selecting different location.
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//
//            String fileName = "UsersCredentials.json";
//            fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise") + fileName;
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            Object[][] internalAdminCredentials = userCredentials.get("InternalAdmin");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(internalAdminCredentials[0][0]), String.valueOf(internalAdminCredentials[0][1])
//                    , String.valueOf(internalAdminCredentials[0][2]));
//
//            TeamPage teamPage = pageFactory.createConsoleTeamPage();
//            teamPage.goToTeam();
//            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
//            teamPage.searchAndSelectTeamMemberByName(nickName);
//            profileNewUIPage.selectProfilePageSubSectionByLabel("Time Off");
//            profileNewUIPage.rejectAllTimeOff();
//
//            SchedulePage schedulePageAdmin = pageFactory.createConsoleScheduleNewUIPage();
//            schedulePageAdmin.goToConsoleScheduleAndScheduleSubMenu();
//            schedulePageAdmin.navigateToNextWeek();
//            schedulePageAdmin.navigateToNextWeek();//BUG
//            boolean isWeekGenerated = schedulePageAdmin.isWeekGenerated();
//            if (!isWeekGenerated) {
//                schedulePageAdmin.createScheduleForNonDGFlowNewUI();
//            }
//            schedulePageAdmin.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            schedulePageAdmin.deleteTMShiftInWeekView(nickName);
//            schedulePageAdmin.deleteTMShiftInWeekView("Unassigned");
//            schedulePageAdmin.clickOnDayViewAddNewShiftButton();
//            schedulePageAdmin.customizeNewShiftPage();
//            schedulePageAdmin.selectWorkRole("GENERAL MANAGER");
//            schedulePageAdmin.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//            schedulePageAdmin.clickOnCreateOrNextBtn();
//            schedulePageAdmin.searchTeamMemberByName(nickName);
////		if (schedulePageAdmin.displayAlertPopUp())
////			schedulePageAdmin.displayAlertPopUpForRoleViolation();
//            schedulePageAdmin.clickOnOfferOrAssignBtn();
//            schedulePageAdmin.saveSchedule();
//            schedulePageAdmin.publishActiveSchedule();
//            List<String> scheduleListAdmin = schedulePageAdmin.getWeekScheduleShiftTimeListOfWeekView(nickName);
//            loginPage.logOut();
//
//            loginToLegionAndVerifyIsLoginDone(username, password, location);
//            dashboardPage.validateDateAndTimeAfterSelectingDifferentLocation();
//            SchedulePage schedulePageTM = pageFactory.createConsoleScheduleNewUIPage();
//            schedulePageTM.clickOnScheduleConsoleMenuItem();
//            schedulePageTM.navigateToNextWeek();
//            schedulePageTM.navigateToNextWeek();//BUG
//            List<String> scheduleListTM = new ArrayList<>();
//            if (schedulePageTM.getShiftHoursFromInfoLayout().size() > 0) {
//                for (String tmShiftTime : schedulePageTM.getShiftHoursFromInfoLayout()) {
//                    tmShiftTime = tmShiftTime.replaceAll(":00", "");
//                    scheduleListTM.add(tmShiftTime);
//                }
//            }
//            if (scheduleListTM != null && scheduleListTM.size() > 0 && scheduleListAdmin != null && scheduleListAdmin.size() > 0) {
//                if (scheduleListTM.size() == scheduleListAdmin.size() && scheduleListTM.containsAll(scheduleListAdmin)) {
//                    SimpleUtils.pass("Schedules in TM view is consistent with the Admin view of the location successfully");
//                } else
//                    SimpleUtils.fail("Schedule doesn't show of the location correctly", true);
//            } else {
//                SimpleUtils.report("Schedule may have not been generated");
//            }
//
//            //T1838585 Validate date and time.
//            dashboardPage.navigateToDashboard();
//            dashboardPage.validateDateAndTime();
//
//            //T1838586 Validate the upcoming schedules.
//            dashboardPage.validateTheUpcomingSchedules(location);
//
//            //T1838587 Validate the click ability of VIEW MY SCHEDULE button.
//            dashboardPage.validateVIEWMYSCHEDULEButtonClickable();
//            dashboardPage.navigateToDashboard();
//
//            //T1838588 Validate the visibility of profile picture.
//            dashboardPage.validateTheVisibilityOfProfilePicture();
//
//            //T1838589 Validate the click ability of Profile picture icon.
//            dashboardPage.validateProfilePictureIconClickable();
//
//            //T1838590 Validate the visibility of Profile.
//            dashboardPage.validateTheVisibilityOfProfile();
//
//            //T1838591 Validate the click ability of My profile, My Work Preferences, My Time off.
//            dashboardPage.validateProfileDropdownClickable();
//
//            //T1838592 Validate the data of My profile.
//            dashboardPage.validateTheDataOfMyProfile();
//
//            //T1838593 Validate the functionality My Work Preferences and My Availability.
//            dashboardPage.navigateToDashboard();
//            String dateFromDashboard = dashboardPage.getCurrentDateFromDashboard();
//            dashboardPage.validateTheDataOfMyWorkPreferences(dateFromDashboard);
//
//            //T1838594 Validate the presence of data on Time off page.
//            dashboardPage.validateTheDataOfMyTimeOff();
//
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(),false);
//        }
//    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Work Preference details by updating the information")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWorkPreferenceDetailsByUpdatingTheInformationAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), true);
            dashboardPage.clickOnSubMenuOnProfile("My Work Preferences");
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            //SimpleUtils.assertOnFail("Profile Page not loaded Successfully!", profileNewUIPage.isProfilePageLoaded(), false);

            //T1838598 Validate the update of shift preferences.
            profileNewUIPage.validateTheUpdateOfShiftPreferences(true, false);

            //T1838599 Validate the update of Availability.
            profileNewUIPage.validateTheUpdateOfAvailability("When I prefer to work", 1, "Right",
                    120, "This week only");
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

//    Need disable TA
//    @Automated(automated = "Automated")
//    @Owner(owner = "Julie")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verification of My Schedule Page")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verificationOfMySchedulePageAsTeamMember(String browser, String username, String password, String location) throws Exception {
//        try {
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//
//            //T1838603 Validate the availability of schedule table.
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String nickName = profileNewUIPage.getNickNameFromProfile();
//            mySchedulePage.validateTheAvailabilityOfScheduleTable(nickName);
//
//            //T1838604 Validate the disability of location selector on Schedule page.
//            mySchedulePage.validateTheDisabilityOfLocationSelectorOnSchedulePage();
//
//            //T1838605 Validate the availability of profile menu.
//            mySchedulePage.validateTheAvailabilityOfScheduleMenu();
//
//            //T1838606 Validate the focus of schedule.
//            mySchedulePage.validateTheFocusOfSchedule();
//
//            //T1838607 Validate the default filter is selected as Scheduled.
//            mySchedulePage.validateTheDefaultFilterIsSelectedAsScheduled();
//
//            //T1838608 Validate the focus of week.
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            dashboardPage.navigateToDashboard();
//            String currentDate = dashboardPage.getCurrentDateFromDashboard();
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            mySchedulePage.validateTheFocusOfWeek(currentDate);
//
//            //T1838609 Validate the selection of previous and upcoming week.
//            schedulePage.verifySelectOtherWeeks();
//        } catch (Exception e) {
//            SimpleUtils.fail(e.getMessage(),false);
//        }
//    }


    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verification of To and Fro navigation of week picker")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verificationOfToAndFroNavigationOfWeekPickerAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
            String nickName = profileNewUIPage.getNickNameFromProfile();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();

            //T1838610 Validate the click ability of forward and backward button.
            scheduleCommonPage.validateForwardAndBackwardButtonClickable();

            //T1838611 Validate the data according to the selected week.
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            mySchedulePage.validateTheDataAccordingToTheSelectedWeek();

            //T1838612 Validate the seven days - Sunday to Saturday is available in schedule table.
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            mySchedulePage.validateTheSevenDaysIsAvailableInScheduleTable();
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            ///Log in as admin to get the operation hours
            String fileName = "UsersCredentials.json";
            fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise") + fileName;
            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
            Object[][] internalAdminCredentials = userCredentials.get("InternalAdmin");
            loginToLegionAndVerifyIsLoginDone(String.valueOf(internalAdminCredentials[0][0]), String.valueOf(internalAdminCredentials[0][1])
                    , String.valueOf(internalAdminCredentials[0][2]));
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

            // Go to Scheduling Policies to get the additional Scheduled Hour
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls Page not loaded Successfully!", controlsNewUIPage.isControlsPageLoaded(), false);
            controlsNewUIPage.clickOnControlsSchedulingPolicies();
            SimpleUtils.assertOnFail("Scheduling Policies Page not loaded Successfully!", controlsNewUIPage.isControlsSchedulingPoliciesLoaded(), false);
            HashMap<String, Integer> schedulePoliciesBufferHours = controlsNewUIPage.getScheduleBufferHours();

            SchedulePage schedulePageAdmin = pageFactory.createConsoleScheduleNewUIPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.clickOnScheduleSubTab("Schedule");
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek(); //BUG

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated){
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectWorkRole("GENERAL MANAGER");
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(nickName);
            if(newShiftPage.displayAlertPopUp())
                newShiftPage.displayAlertPopUpForRoleViolation();
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();
            if (!toggleSummaryPage.isSummaryViewLoaded())
                toggleSummaryPage.toggleSummaryView();
            String theEarliestAndLatestTimeInSummaryView = toggleSummaryPage.getTheEarliestAndLatestTimeInSummaryView(schedulePoliciesBufferHours);
            SimpleUtils.report("theEarliestAndLatestOperationHoursInSummaryView is " + theEarliestAndLatestTimeInSummaryView);
            loginPage.logOut();

            ///Log in as team member again to compare the operation hours
            loginToLegionAndVerifyIsLoginDone(username, password, location);
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.navigateToNextWeek(); //BUG
            String theEarliestAndLatestTimeInScheduleTable = mySchedulePage.getTheEarliestAndLatestTimeInScheduleTable();
            SimpleUtils.report("theEarliestAndLatestOperationHoursInScheduleTable is " + theEarliestAndLatestTimeInScheduleTable);
            mySchedulePage.compareOperationHoursBetweenAdminAndTM(theEarliestAndLatestTimeInSummaryView, theEarliestAndLatestTimeInScheduleTable);

            //T1838613 Validate that hours and date is visible of shifts.
            mySchedulePage.validateThatHoursAndDateIsVisibleOfShifts();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Profile picture functionality")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyProfilePictureFunctionalityAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //T1838614 Validate the clickability of Profile picture in a shift.
            mySchedulePage.validateProfilePictureInAShiftClickable();

            //T1838615 Validate the data of profile popup in a shift.
            //schedulePage.validateTheDataOfProfilePopupInAShift();
            // todo: <Here is an incident LEG-10929>
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Info icon functionality")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyInfoIconFunctionalityAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();

            //T1838616 Validate the availability of info icon.
            mySchedulePage.validateTheAvailabilityOfInfoIcon();

            //T1838617 Validate the clickability of info icon.
            mySchedulePage.validateInfoIconClickable();

            //T1838618 Validate the availability of Meal break as per the control settings.
            //schedulePage.validateMealBreakPerControlSettings();
            // todo: <Meal break is a postpone feature>
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Julie")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verification of Open Shift Schedule Smart Card")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyOpenShiftScheduleSmartCardAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try {

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            //T1838619 Validate the availability of Open shift Smartcard.
            smartCardPage.validateTheAvailabilityOfOpenShiftSmartcard();

            //T1838620 Validate the clickability of View shifts in Open shift smartcard.
            mySchedulePage.validateViewShiftsClickable();

            //T1838621 Validate the number of open shifts in smartcard and schedule table.
            mySchedulePage.validateTheNumberOfOpenShifts();

            //T1838622 Verify the availability of claim open shift popup.
            mySchedulePage.verifyTheAvailabilityOfClaimOpenShiftPopup();
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

//    @Automated(automated = "Automated")
//    @Owner(owner = "Nora")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
//    @TestName(description = "Verify the availibility and functionality of claiming open shift popup")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyTheFunctionalityOfClaimOpenShiftAsTeamLead(String browser, String username, String password, String location)
//            throws Exception {
//        try {
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//            ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//            String tmName = profileNewUIPage.getNickNameFromProfile();
//            LoginPage loginPage = pageFactory.createConsoleLoginPage();
//            loginPage.logOut();
//
//            String fileName = "UsersCredentials.json";
//            HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            fileName = SimpleUtils.getEnterprise("CinemarkWkdy_Enterprise") + fileName;
//            userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//            Object[][] credentials = userCredentials.get("InternalAdmin");
//            loginToLegionAndVerifyIsLoginDone(String.valueOf(credentials[0][0]), String.valueOf(credentials[0][1]), String.valueOf(credentials[0][2]));
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), true);
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToNextWeek();
//
//            boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//            if (isActiveWeekGenerated) {
//                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//            }
//            createSchedulePage.createScheduleForNonDGFlowNewUI();
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(tmName);
//            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
//            scheduleMainPage.saveSchedule();
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
//            newShiftPage.clickOnDayViewAddNewShiftButton();
//            newShiftPage.customizeNewShiftPage();
//            newShiftPage.selectWorkRole("GENERAL MANAGER");
//            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
//            newShiftPage.clickOnCreateOrNextBtn();
//            newShiftPage.searchTeamMemberByName(tmName);
//            newShiftPage.clickOnOfferOrAssignBtn();
//            scheduleMainPage.saveSchedule();
//            createSchedulePage.publishActiveSchedule();
//
//            loginPage.logOut();
//
//            loginToLegionAndVerifyIsLoginDone(username, password, location);
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//            scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//            scheduleCommonPage.navigateToNextWeek();
//            scheduleCommonPage.navigateToNextWeek();
//            // Validate the clickability of claim open text in popup
//            String cardName = "WANT MORE HOURS?";
//            SimpleUtils.assertOnFail("Smart Card: " + cardName + " not loaded Successfully!", smartCardPage.isSpecificSmartCardLoaded(cardName), false);
//            String linkName = "View Shifts";
//            smartCardPage.clickLinkOnSmartCardByName(linkName);
//            SimpleUtils.assertOnFail("Open shifts not loaed Successfully!", scheduleShiftTablePage.areShiftsPresent(), false);
//            List<String> shiftHours = mySchedulePage.getShiftHoursFromInfoLayout();
//            List<String> claimShift = new ArrayList<>(Arrays.asList("Claim Shift"));
//            int index = mySchedulePage.selectOneShiftIsClaimShift(claimShift);
//            String weekDay = mySchedulePage.getSpecificShiftWeekDay(index);
//            // Validate the availability of Claim Shift Request popup
//            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
//            // Validate the availability of Cancel and I Agree buttons in popup
//            mySchedulePage.verifyClaimShiftOfferNBtnsLoaded();
//            // Validate the date and time of Claim Shift Request popup
//            mySchedulePage.verifyTheShiftHourOnPopupWithScheduleTable(shiftHours.get(index), weekDay);
//            // Validate the clickability of Cancel button
//            schedulePage.verifyClickCancelBtnOnClaimShiftOffer();
//            // Validate the clickability of I Agree button
//            mySchedulePage.clickOnShiftByIndex(index);
//            mySchedulePage.clickTheShiftRequestByName(claimShift.get(0));
//            mySchedulePage.verifyClickAgreeBtnOnClaimShiftOffer();
//            // Validate the status of Claim request
//            mySchedulePage.clickOnShiftByIndex(index);
//            List<String> claimStatus = new ArrayList<>(Arrays.asList("Claim Shift Approval Pending", "Cancel Claim Request"));
//            mySchedulePage.verifyShiftRequestButtonOnPopup(claimStatus);
//            // Validate the availability of Cancel Claim Request option.
//            mySchedulePage.verifyTheColorOfCancelClaimRequest(claimStatus.get(1));
//            // Validate that Cancel claim request is clickable and popup is displaying by clicking on it to reconfirm the cancellation
//            mySchedulePage.clickTheShiftRequestByName(claimStatus.get(1));
//            mySchedulePage.verifyReConfirmDialogPopup();
//            // Validate that Claim request remains in Pending state after clicking on No button
//            mySchedulePage.verifyClickNoButton();
//            mySchedulePage.clickOnShiftByIndex(index);
//            mySchedulePage.verifyShiftRequestButtonOnPopup(claimStatus);
//            // Validate the Cancellation of Claim request by clicking  on Yes
//            mySchedulePage.clickTheShiftRequestByName(claimStatus.get(1));
//            mySchedulePage.verifyReConfirmDialogPopup();
//            mySchedulePage.verifyClickOnYesButton();
//            mySchedulePage.clickOnShiftByIndex(index);
//            mySchedulePage.verifyShiftRequestButtonOnPopup(claimShift);
//            // Validate the functionality of clear filter in Open shift smart card
//            mySchedulePage.verifyTheFunctionalityOfClearFilter();
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }


    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the feature of filter")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void verifyTheFeatureOfFilterAsInternalAdmin(String browser, String username, String password, String location)
            throws Exception {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            MySchedulePage mySchedulePage = pageFactory.createMySchedulePage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek();//BUG
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            // Deleting the existing shifts for swap team members
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(6,6,6);
            newShiftPage.selectWorkRole("GENERAL MANAGER");
            newShiftPage.moveSliderAtCertainPoint("5pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            createSchedulePage.publishActiveSchedule();

            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            loginPage.logOut();

            // Login as Team Member
            loginAsDifferentRole(AccessRoles.TeamMember.getValue());

            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            mySchedulePage.goToSchedulePageAsTeamMember();
            scheduleCommonPage.navigateToNextWeek();
            scheduleCommonPage.navigateToNextWeek(); //BUG
            String subTitle = "Team Schedule";
            mySchedulePage.gotoScheduleSubTabByText(subTitle);
            // Validate the feature of filter
            mySchedulePage.verifyScheduledNOpenFilterLoaded();
            // Validate the filter - Schedule and Open
            scheduleMainPage.checkAndUnCheckTheFilters();
            // Validate the filter results by applying scheduled filter
            // Validate the filter results by applying Open filter
            scheduleMainPage.filterScheduleByShiftTypeAsTeamMember(true);
            // Validate the filter results by applying both filters and none of them
            scheduleMainPage.filterScheduleByBothAndNone();
            // Validate the filter value by moving to other weeks
            String selectedFilter = scheduleMainPage.selectOneFilter();
            scheduleMainPage.verifySelectedFilterPersistsWhenSelectingOtherWeeks(selectedFilter);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the left navigation menu on login using admin access")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheLeftNavigationMenuOnLoginUsingAdminAccessAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            verifyTheLeftNavigationMenuOnLoginUsingDifferentAccess("Admin");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the left navigation menu on login using SM access")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheLeftNavigationMenuOnLoginUsingSMAccessAsStoreManager(String browser, String username, String password, String location) throws Exception {
        try{
            verifyTheLeftNavigationMenuOnLoginUsingDifferentAccess("StoreManager");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the left navigation menu on login using TL access")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheLeftNavigationMenuOnLoginUsingTLAccessAsTeamLead(String browser, String username, String password, String location) throws Exception {
        try{
            verifyTheLeftNavigationMenuOnLoginUsingDifferentAccess("TeamLead");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the left navigation menu on login using TM access")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheLeftNavigationMenuOnLoginUsingTMAccessAsTeamMember(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            dashboardPage.closeNewFeatureEnhancementsPopup();
            verifyTheLeftNavigationMenuOnLoginUsingDifferentAccess("TeamMember");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the left navigation menu on login using DM access")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheLeftNavigationMenuOnLoginUsingDMAccessAsDistrictManager(String browser, String username, String password, String location) throws Exception {
        try{
            verifyTheLeftNavigationMenuOnLoginUsingDifferentAccess("DistrictManager");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the left navigation menu on login using CA (Customer Admin) access")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheLeftNavigationMenuOnLoginUsingCAAccessAsCustomerAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            verifyTheLeftNavigationMenuOnLoginUsingDifferentAccess("CustomerAdmin");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void verifyTheLeftNavigationMenuOnLoginUsingDifferentAccess(String userRole) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        //Check DashBoard console menu is display
        SimpleUtils.assertOnFail("DashBoard console menu not loaded Successfully!", dashboardPage.isDashboardConsoleMenuDisplay(), false);
        //Check dashboard page is display after click Dashboard console menu
        dashboardPage.clickOnDashboardConsoleMenu();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        dashboardPage.verifyHeaderNavigationMessage("Dashboard");

        //Check Legion logo is display or not
        if (userRole.contains("Admin") || userRole.contains("StoreManager")
                || userRole.contains("DistrictManager") || userRole.contains("CustomerAdmin")) {
            SimpleUtils.assertOnFail("Legion logo should be loaded!", dashboardPage.isLegionLogoDisplay(), false);
        } else
            SimpleUtils.assertOnFail("Legion logo should not be loaded!", !dashboardPage.isLegionLogoDisplay(), false);

        //Check District selector is display or not
        if (userRole.contains("Admin") || userRole.contains("CustomerAdmin") || userRole.contains("DistrictManager")) {
            SimpleUtils.assertOnFail("District selector should be loaded!", dashboardPage.IsThereDistrictNavigationForLegionBasic(), false);
            //Check District dropdown is display after click District selector
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            locationSelectorPage.verifyClickChangeDistrictButton();
        } else
            SimpleUtils.assertOnFail("District selector should not be loaded!", !dashboardPage.IsThereDistrictNavigationForLegionBasic(), false);

        //Check Team console menu is display or not
        if (userRole.contains("TeamMember")) {
            SimpleUtils.assertOnFail("Team console menu should not loaded!", !dashboardPage.isTeamConsoleMenuDisplay(), false);
        } else{
            SimpleUtils.assertOnFail("Team console menu not loaded Successfully!", dashboardPage.isTeamConsoleMenuDisplay(), false);
            //Check Team page is display after click Team tab
            TeamPage teamPage = pageFactory.createConsoleTeamPage();
            teamPage.goToTeam();
            teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
            dashboardPage.verifyHeaderNavigationMessage("Team");
        }

        //Check Schedule console menu is display
        SimpleUtils.assertOnFail("Schedule console menu not loaded Successfully!", dashboardPage.isScheduleConsoleMenuDisplay(), false);

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        //Check Schedule overview page is display after click Schedule tab
        if (userRole.contains("TeamLead") || userRole.contains("TeamMember")) {
            scheduleCommonPage.verifyTMSchedulePanelDisplay();
        } else {
            ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
            SimpleUtils.assertOnFail("Schedule page not loaded Successfully!", scheduleOverviewPage.loadScheduleOverview(), false);
        }
        dashboardPage.verifyHeaderNavigationMessage("Schedule");

        //Check TimeSheet console menu is display or not
        if (getDriver().getCurrentUrl().contains(propertyMap.get("Coffee_Enterprise"))) {
            if (userRole.contains("Admin") || userRole.contains("StoreManager")
                    || userRole.contains("CustomerAdmin") || userRole.contains("DistrictManager")){
                SimpleUtils.assertOnFail("Timesheet console menu not loaded Successfully!", dashboardPage.isTimesheetConsoleMenuDisplay(), false);
                TimeSheetPage timeSheetPage = pageFactory.createTimeSheetPage();
                timeSheetPage.clickOnTimeSheetConsoleMenu();
                SimpleUtils.assertOnFail("Timesheet console page not loaded Successfully!", timeSheetPage.isTimeSheetPageLoaded(), false);
            } else{
                SimpleUtils.assertOnFail("Timesheet console menu should not loaded!", !dashboardPage.isTimesheetConsoleMenuDisplay(), false);
            }
        }

        //Check Analytics console menu is display or not
        if (userRole.contains("TeamLead") || userRole.contains("TeamMember")) {
            SimpleUtils.assertOnFail("Report console menu should not be loaded Successfully!",
                    !dashboardPage.isReportConsoleMenuDisplay(), false);
        } else {
            SimpleUtils.assertOnFail("Report console menu not loaded Successfully!", dashboardPage.isReportConsoleMenuDisplay(), false);
            //Check Analytics page is display after click Analytics tab
            AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
            analyticsPage.clickOnAnalyticsConsoleMenu();
            SimpleUtils.assertOnFail("Report Page not loaded Successfully!", analyticsPage.isReportsPageLoaded(), false);
            dashboardPage.verifyHeaderNavigationMessage("Report");
        }


        //Check Inbox console menu is display
        SimpleUtils.assertOnFail("Inbox console menu not loaded Successfully!", dashboardPage.isInboxConsoleMenuDisplay(), false);
        //Check Inbox page is display after click Inbox tab
        InboxPage inboxPage = pageFactory.createConsoleInboxPage();
        inboxPage.clickOnInboxConsoleMenuItem();
        SimpleUtils.assertOnFail("Inbox console menu not loaded Successfully!", inboxPage.isAnnouncementListPanelDisplay(), false);
        dashboardPage.verifyHeaderNavigationMessage("Inbox");

        //Check Admin console menu is display
        if (userRole.equalsIgnoreCase("Admin")){
            SimpleUtils.assertOnFail("Admin console menu not loaded Successfully!", dashboardPage.isAdminConsoleMenuDisplay(), false);
            //Check Admin page is display after click Admin tab
            dashboardPage.clickOnAdminConsoleMenu();
            dashboardPage.verifyAdminPageIsLoaded();
            dashboardPage.verifyHeaderNavigationMessage("Admin");
        } else
            SimpleUtils.assertOnFail("Admin console menu should not be loaded!", !dashboardPage.isAdminConsoleMenuDisplay(), false);

        //Check Integration console menu is display
        if (userRole.equalsIgnoreCase("Admin")) {
            SimpleUtils.assertOnFail("Integration console menu not loaded Successfully!", dashboardPage.isIntegrationConsoleMenuDisplay(), false);
            //Check Integration page is display after click Integration tab
            dashboardPage.clickOnIntegrationConsoleMenu();
            dashboardPage.verifyIntegrationPageIsLoaded();
            dashboardPage.verifyHeaderNavigationMessage("Integration");
        } else
            SimpleUtils.assertOnFail("Integration console menu should not be loaded!", !dashboardPage.isIntegrationConsoleMenuDisplay(), false);

//        //Check Controls console menu is display
//        if (userRole.contains("TeamLead") || userRole.contains("TeamMember")){
//            SimpleUtils.assertOnFail("Controls console menu should not be loaded!", !dashboardPage.isControlsConsoleMenuDisplay(), false);
//        } else {
//            SimpleUtils.assertOnFail("Controls console menu not loaded Successfully!", dashboardPage.isControlsConsoleMenuDisplay(), false);
//            //Check Controls page is display after click Controls tab
//            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//            controlsNewUIPage.clickOnControlsConsoleMenu();
//            controlsNewUIPage.isControlsPageLoaded();
//            dashboardPage.verifyHeaderNavigationMessage("Controls");
//        }

        //Check Logout console menu is display
        SimpleUtils.assertOnFail("Logout console menu not loaded Successfully!", dashboardPage.isLogoutConsoleMenuDisplay(), false);
        //Check Login page is display after click Logout tab
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.logOut();
        loginPage.verifyLoginPageIsLoaded();
    }
}
