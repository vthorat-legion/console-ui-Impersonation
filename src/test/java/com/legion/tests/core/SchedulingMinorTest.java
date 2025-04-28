package com.legion.tests.core;

import com.legion.pages.DashboardPage;
import com.legion.pages.LocationSelectorPage;
import com.legion.pages.*;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.legion.utils.MyThreadLocal.getDriver;

public class SchedulingMinorTest extends TestBase {

    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
    private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
    private static String workRole = scheduleWorkRoles.get("RETAIL_ASSOCIATE");

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception {
        this.createDriver((String) params[0], "69", "Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate there is no warning message and violation when minor's shift is not avoid the minor settings")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyNoWarningMessageAndViolationDisplayWhenMinorIsNotAvoidMinorSettingsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                locationSelectorPage.changeDistrict("Demo District");
                locationSelectorPage.changeLocation("Santana Row");
            }

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(3000);
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "9:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            workRole = shiftOperatePage.getRandomWorkRole();
            String firstNameOfTM1 = "Minor14";
            String firstNameOfTM2 = "Minor16";
            String lastNameOfTM = "RC";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);

            //Create new shift for TM1
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();

            //set shift time as 10:00 AM - 1:00 PM
            newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());

            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));
            SimpleUtils.assertOnFail("There should no minor warning message display when shift is not avoid the minor setting! ",
                    !shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor"), false);
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (!warningMessage.contains("Minor")){
                    SimpleUtils.pass("There is no minor warning message display when shift is not avoid the minor setting! ");
                } else
                    SimpleUtils.fail("There should no minor warning message display when shift is not avoid the minor setting! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.pass("There is no minor warning message display when shift is not avoid the minor setting! ");

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            WebElement newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1).get(0));
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("There should no minor warning message display when shift is not avoid the minor setting! ",
                        !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);
            scheduleMainPage.clickOnCloseSearchBoxButton();

            //Create new shift for TM2
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();

            //set shift time as 10:00 AM - 1:00 PM
            newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());

            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));

            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should no minor warning message display when shift is not avoid the minor setting! ",
                    !shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor"), false);

            //check the message in warning mode
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM2);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (!warningMessage.contains("Minor")){
                    SimpleUtils.pass("There is no minor warning message display when shift is not avoid the minor setting! ");
                } else
                    SimpleUtils.fail("There should no minor warning message display when shift is not avoid the minor setting! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.pass("There is no minor warning message display when shift is not avoid the minor setting! ");

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM2);
            newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM2).get(0));
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("There should no minor warning message display when shift is not avoid the minor setting! ",
                        !scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate Scheduling Minors rules (Ages 14 & 15 and Ages 16 & 17) can be edit successfully")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyEditMinorRulesAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);

            ControlsPage controlsPage = pageFactory.createConsoleControlsPage();
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            controlsPage.gotoControlsPage();
            SimpleUtils.assertOnFail("Controls page not loaded successfully!", controlsNewUIPage.isControlsPageLoaded(), false);

            controlsNewUIPage.clickOnControlsComplianceSection();
            SimpleUtils.assertOnFail("collaboration page not loaded successfully!", controlsNewUIPage.isCompliancePageLoaded(), false);
            controlsNewUIPage.setSchedulingMinorRuleFor14N15("9:30 AM", "7:30 PM", "15", "6", "3", "5");
            controlsNewUIPage.setSchedulingMinorRuleFor16N17("10:00 AM", "7:00 PM", "20", "7", "5", "6");
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the warning message and violation when minor's shift exceed the weekend or holiday hours")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWarningMessageForExceedWeekendHrsAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            AnalyzePage analyzePage = pageFactory.createAnalyzePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                locationSelectorPage.changeDistrict("Demo District");
                locationSelectorPage.changeLocation("Santana Row");
            }

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
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            String holidaySmartCard = "HOLIDAYS";
            List<String> holidays = null;
            if (smartCardPage.isSpecificSmartCardLoaded(holidaySmartCard)){
                smartCardPage.clickLinkOnSmartCardByName("View All");
                holidays = smartCardPage.getHolidaysOfCurrentWeek();
                //close popup window
                analyzePage.closeAnalyzeWindow();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "9:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            workRole = shiftOperatePage.getRandomWorkRole();
            String firstNameOfTM1 = "Minor14";
            String firstNameOfTM2 = "Minor16";
            String lastNameOfTM = "RC";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            //Create new shift for TM1
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(0,0,0);
            //set shift time as 10:00 AM - 6:00 PM
            newShiftPage.moveSliderAtCertainPoint("6", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));
            shiftOperatePage.verifyMessageIsExpected("minor daily max 6 hrs");
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("daily schedule should not exceed 6 hours")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays", false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else {
                SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays",false);
            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            WebElement newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1).get(0));
            String test = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).toString();
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("Get new added shift failed",scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor daily max 6 hrs"), false);
            } else {
                SimpleUtils.fail("Get new added shift failed", false);
            }


            //Create new shift for TM2, check create shift on holiday
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            if (holidays!=null){
                //i: 1-5 weekday
                int index =1;
                boolean flag = false;
                for (;index<=5; index++){
                    for (String s: holidays){
                        if (s.contains(scheduleShiftTablePage.getWeekDayTextByIndex(index))){
                            flag = true;
                        }
                    }
                    if (flag){
                        break;
                    }
                }
                newShiftPage.selectDaysByIndex(index,index,index);
            } else {
                newShiftPage.selectDaysByIndex(0,0,0);
            }


            //set shift time as 10:00 AM - 6:00 PM
            newShiftPage.moveSliderAtCertainPoint("6", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));
            shiftOperatePage.verifyMessageIsExpected("minor daily max 7 hrs");
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM2);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("daily schedule should not exceed 7 hours")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays", false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else {
                SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays",false);
            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM2).get(0));
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("Get new added shift failed",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor daily max 7 hrs"), false);
            } else {
                SimpleUtils.fail("Get new added shift failed", false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the warning message and violation when minor's shift exceed the weekday hours")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyWarningMessageForExceedWeekdayHrsAsInternalAdmin(String browser, String username, String password, String location) {
        try {
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
            AnalyzePage analyzePage = pageFactory.createAnalyzePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                locationSelectorPage.changeDistrict("Demo District");
                locationSelectorPage.changeLocation("Santana Row");
            }

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
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            String holidaySmartCard = "HOLIDAYS";
            List<String> holidays = null;
            if (smartCardPage.isSpecificSmartCardLoaded(holidaySmartCard)){
                smartCardPage.clickLinkOnSmartCardByName("View All");
                holidays = smartCardPage.getHolidaysOfCurrentWeek();
                //close popup window
                analyzePage.closeAnalyzeWindow();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "9:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            workRole = shiftOperatePage.getRandomWorkRole();
            String firstNameOfTM1 = "Minor14";
            String firstNameOfTM2 = "Minor16";
            String lastNameOfTM = "RC";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Create new shift for TM1
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            int index =1;
            if (holidays!=null){
                //i: 1-5 weekday
                boolean flag = false;
                for (;index<=5; index++){
                    for (String s: holidays){
                        if (s.contains(scheduleShiftTablePage.getWeekDayTextByIndex(index))){
                            flag = true;
                        }
                    }
                    if (!flag){
                        break;
                    }
                }
            }
            newShiftPage.selectDaysByIndex(index,index,index);
            //set shift time as 10:00 AM - 6:00 PM
            newShiftPage.moveSliderAtCertainPoint("4", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));
            shiftOperatePage.verifyMessageIsExpected("minor daily max 3 hrs");
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("daily schedule should not exceed 3 hours")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays", false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else {
                SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays",false);
            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            WebElement newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1).get(0));
            String test = scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).toString();
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("Get new added shift failed",scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor daily max 3 hrs"), false);
            } else {
                SimpleUtils.fail("Get new added shift failed", false);
            }


            //Create new shift for TM2
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(index,index,index);

            //set shift time as 10:00 AM - 6:00 PM
            newShiftPage.moveSliderAtCertainPoint("4", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("10", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));
            shiftOperatePage.verifyMessageIsExpected("minor daily max 5 hrs");
            shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM2);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage = scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode();
                if (warningMessage.contains("daily schedule should not exceed 5 hours")){
                    SimpleUtils.pass("Minor warning message for exceed the weekend or holiday hours displays");
                } else {
                    SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays", false);
                }
                shiftOperatePage.clickOnAssignAnywayButton();
            } else {
                SimpleUtils.fail("There is no minor warning message display when shift exceed the weekend or holiday hours displays",false);
            }
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM2).get(0));
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("Get new added shift failed",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor daily max 5 hrs"), false);
            } else {
                SimpleUtils.fail("Get new added shift failed", false);
            }
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "Vailqacn_Enterprise")
    @TestName(description = "Validate Minor info in the Profile page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorInfoOnProfilePageAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
        String firstNameOfMinor14 = "Minor14";
        String firstNameOfMinor16 = "Minor16";
        TeamPage teamPage = pageFactory.createConsoleTeamPage();
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(firstNameOfMinor14);
        Map hrProfileInfo = profileNewUIPage.getHRProfileInfo();
        SimpleUtils.assertOnFail("Minors info is incorrect!",String.valueOf(hrProfileInfo.get("MINOR")).contains("14-15"), false);
        teamPage.goToTeam();
        teamPage.verifyTeamPageLoadedProperlyWithNoLoadingIcon();
        teamPage.searchAndSelectTeamMemberByName(firstNameOfMinor16);
        hrProfileInfo = profileNewUIPage.getHRProfileInfo();
        SimpleUtils.assertOnFail("Minors info is correct!",String.valueOf(hrProfileInfo.get("MINOR")).contains("16-17"), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate minor filter in the Schedule page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorFilterAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        String firstNameOfMinor14 = "Minor14";
        String firstNameOfMinor16 = "Minor16";

        //Go to the schedule page to create shifts for minors and check the filter.

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
        scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
        SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Succerssfully!",
                scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

        scheduleCommonPage.navigateToNextWeek();
        scheduleCommonPage.navigateToNextWeek();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated){
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUI();

        String workRole = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfMinor14);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfMinor16);
        scheduleMainPage.saveSchedule();
        //create shifts for minors.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleCommonPage.clickOnDayView();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByNameNLocation(firstNameOfMinor14, location);
        newShiftPage.clickOnOfferOrAssignBtn();

        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByNameNLocation(firstNameOfMinor16, location);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 14!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 16!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        SimpleUtils.assertOnFail("There should be all shifts displaying!", 1 < scheduleShiftTablePage.getShiftsCount(), false);
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 14!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 16!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        SimpleUtils.assertOnFail("There should be all shifts displaying!", 1 < scheduleShiftTablePage.getShiftsCount(), false);

        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 14!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 16!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        SimpleUtils.assertOnFail("There should be all shifts displaying!", 1 < scheduleShiftTablePage.getShiftsCount(), false);
        scheduleCommonPage.clickOnDayView();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 14!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        SimpleUtils.assertOnFail("There should be only one shift for minor 16!", 1 == scheduleShiftTablePage.getShiftsCount(), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnClearFilterOnFilterDropdownPopup();
        SimpleUtils.assertOnFail("There should be all shifts displaying!", 1 < scheduleShiftTablePage.getShiftsCount(), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Haya")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate minor badge info")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyMinorBadgeInfoAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
        String firstNameOfMinor14 = "Minor14";
        String firstNameOfMinor16 = "Minor16";

        //Go to the schedule page to create shifts for minors and check the filter.

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
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

        String workRole = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfMinor14);
        scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfMinor14);
        scheduleMainPage.saveSchedule();
        //create shifts for minors.
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleCommonPage.clickOnDayView();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByNameNLocation(firstNameOfMinor14, location);
        newShiftPage.clickOnOfferOrAssignBtn();

        newShiftPage.clickOnDayViewAddNewShiftButton();
        newShiftPage.customizeNewShiftPage();
        newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
        newShiftPage.selectWorkRole(workRole);
        newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
        newShiftPage.clickOnCreateOrNextBtn();
        newShiftPage.searchTeamMemberByNameNLocation(firstNameOfMinor16, location);
        newShiftPage.clickOnOfferOrAssignBtn();
        scheduleMainPage.saveSchedule();

        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-14");
        Thread.sleep(5000);
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 14-15"), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-16");
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 16-17"), false);
        scheduleCommonPage.clickOnWeekView();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-14");
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 14-15"), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-16");
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 16-17"), false);

        //verify again in edit mode.


        System.out.println(scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)));
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-14");
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 14-15"), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-16");
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 16-17"), false);
        scheduleCommonPage.clickOnDayView();
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (14-15)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-14");
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 14-15"), false);
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.selectShiftTypeFilterByText("Minor (16-17)");
        scheduleShiftTablePage.verifyShiftsHasMinorsColorRing("minor-16");
        SimpleUtils.assertOnFail("There should be minor info in i icon popup!",scheduleShiftTablePage.getIIconTextInfo(scheduleShiftTablePage.getTheShiftByIndex(0)).contains("Minor 16-17"), false);
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the warning message and violation when minor's shift is not during the setting time")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheWarningMessageAndViolationWhenMinorShiftIsNotDuringTheSettingTimeAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                locationSelectorPage.changeDistrict("Demo District");
                locationSelectorPage.changeLocation("Santana Row");
            }

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "9:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            workRole = shiftOperatePage.getRandomWorkRole();
            String firstNameOfTM1 = "Minor14";
            String firstNameOfTM2 = "Minor16";
            String lastNameOfTM = "RC";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);

            //Create new shift for TM1
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();

            //set shift time as 9:00 AM - 2:00 PM
            newShiftPage.moveSliderAtCertainPoint("2", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());

            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));

            //check the message in warning mode
//        shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM1);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = "As a minor, "+firstNameOfTM1+" should be scheduled from 9:30AM - 7:30PM";
                String warningMessage2 = "Please confirm that you want to make this change.";
                if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                        && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);


            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should have minor warning message display as: Minor working hrs 9:30AM - 7:30PM! ",
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor working hrs 9:30AM - 7:30PM"), false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            WebElement newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1).get(0));
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor working hrs 9:30AM - 7:30PM"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);

            //Create new shift for TM2
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();

            //set shift time as 9:00 AM - 2:00 PM
            newShiftPage.moveSliderAtCertainPoint("2", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());

            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));

            //check the message in warning mode
//        shiftOperatePage.clickOnRadioButtonOfSearchedTeamMemberByName(firstNameOfTM2);
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = "As a minor, "+firstNameOfTM2+" should be scheduled from 10AM - 7PM";
                String warningMessage2 = "Please confirm that you want to make this change.";
                if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                        && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);

            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should have minor warning message display as: Minor working hrs 10AM - 7PM! ",
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor working hrs 10AM - 7PM"), false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM2).get(0));
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor working hrs 10AM - 7PM"), false);
            } else
                SimpleUtils.fail("Get new added shift failed", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the warning message and violation when minor's shifts exceed the weekly hours")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheWarningMessageAndViolationWhenMinorShiftsExceedTheWeeklyHoursAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                locationSelectorPage.changeDistrict("Demo District");
                locationSelectorPage.changeLocation("Santana Row");
            }

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "09:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            workRole = shiftOperatePage.getRandomWorkRole();
            String firstNameOfTM1 = "Minor14";
            String firstNameOfTM2 = "Minor16";
            String lastNameOfTM = "RC";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);

            //Create 5 shifts for TM1 and the shifts have 15 hours totally
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 2:00 PM
            newShiftPage.moveSliderAtCertainPoint("2", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(5);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create the sixth shift for TM1
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 2:00 PM
            newShiftPage.moveSliderAtCertainPoint("2", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(5,5,5);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));

            //check the message in warning mode
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = "As a minor, "+firstNameOfTM1+"'s weekly schedule should not exceed 15 hours";
                String warningMessage2 = "Please confirm that you want to make this change.";
                if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                        && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);


            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should have minor warning message display as: Minor weekly max 15 hrs! ",
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor weekly max 15 hrs"), false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            //check the violation in i icon popup of new create shift
            WebElement newAddedShift = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM1).get(0);

            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max 15 hrs"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);
            scheduleMainPage.clickOnCloseSearchBoxButton();

            //Create 4 shifts for TM2 and the shifts have 20 hours totally
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 4:00 PM
            newShiftPage.moveSliderAtCertainPoint("4", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(4);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create the fifth shift for TM2
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 4:00 PM
            newShiftPage.moveSliderAtCertainPoint("4", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(4,4,4);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));

            //check the message in warning mode
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = "As a minor, "+firstNameOfTM2+"'s weekly schedule should not exceed 20 hours";
                String warningMessage2 = "Please confirm that you want to make this change.";
                if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                        && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);


            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should have minor warning message display as: Minor weekly max 20 hrs! ",
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor weekly max 20 hrs"), false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM2);
            newAddedShift = scheduleShiftTablePage.getOneDayShiftByName(4, firstNameOfTM2).get(0);
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max 20 hrs"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the warning message and violation when minor's shift days exceed the week days in setting")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheWarningMessageAndViolationWhenMinorShiftDaysExceedTheWeekDaysInSettingAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                locationSelectorPage.changeDistrict("Demo District");
                locationSelectorPage.changeLocation("Santana Row");
            }

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "9:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            workRole = shiftOperatePage.getRandomWorkRole();
            String firstNameOfTM1 = "Minor16";
            String firstNameOfTM2 = "Minor14";
            String lastNameOfTM = "RC";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM2);

            //Create 6 shifts in 6 days for TM1
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 1:00 PM
            newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create the shift on seventh day for TM1
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 1:00 PM
            newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(6,6,6);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));

            //check the message in warning mode
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = "As a minor, "+firstNameOfTM1+"'s weekly schedule should not exceed 6 days";
                String warningMessage2 = "Please confirm that you want to make this change.";
                if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                        && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);


            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should have minor warning message display as: Minor weekly max 6 days! ",
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor weekly max 6 days"), false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            WebElement newAddedShift = scheduleShiftTablePage.getOneDayShiftByName(6, firstNameOfTM1).get(0);
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max 6 days"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);
            scheduleMainPage.clickOnCloseSearchBoxButton();

            //Create 5 shifts in 5 days for TM2
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 1:00 PM
            newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(5);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchTeamMemberByName(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));
            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //Create the shift in sixth day for TM2
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 11:00 AM - 1:00 PM
            newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(5,5,5);
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM2 + " " + lastNameOfTM.substring(0,1));

            //check the message in warning mode
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = "As a minor, "+firstNameOfTM2+"'s weekly schedule should not exceed 5 days";
                String warningMessage2 = "Please confirm that you want to make this change.";
                if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                        && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);


            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should have minor warning message display as: Minor weekly max 5 days! ",
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Minor weekly max 5 days"), false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM2);
            newAddedShift = scheduleShiftTablePage.getOneDayShiftByName(5, firstNameOfTM2).get(0);
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Minor weekly max 5 days"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Validate the warning message and violation when minor's is < 14yr old")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheWarningMessageAndViolationWhenMinorIsUnder14YearsOldAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
            NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
            if (getDriver().getCurrentUrl().contains(propertyMap.get("KendraScott2_Enterprise"))){
                locationSelectorPage.changeDistrict("Demo District");
                locationSelectorPage.changeLocation("Santana Row");
            }

            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);

            scheduleCommonPage.navigateToNextWeek();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated){
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange( "08:00AM", "09:00PM");
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            workRole = shiftOperatePage.getRandomWorkRole();
            String firstNameOfTM1 = "Minor13";
            String lastNameOfTM = "RC";
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM1);


            //Create the shift for TM1
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.customizeNewShiftPage();
            //set shift time as 0:00 AM - 1:00 PM
            newShiftPage.moveSliderAtCertainPoint("1", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("9", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            newShiftPage.searchText(firstNameOfTM1 + " " + lastNameOfTM.substring(0,1));

            //check the message in warning mode
            if(newShiftPage.ifWarningModeDisplay()){
                String warningMessage1 = firstNameOfTM1+" is < 14 years old";
                String warningMessage2 = "Please confirm that you want to make this change.";
                if (scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage1)
                        && scheduleShiftTablePage.getWarningMessageInDragShiftWarningMode().contains(warningMessage2)){
                    SimpleUtils.pass("The message in warning mode display correctly! ");
                } else
                    SimpleUtils.fail("The message in warning mode display incorrectly! ", false);
                shiftOperatePage.clickOnAssignAnywayButton();
            } else
                SimpleUtils.fail("There should have warning mode display with minor warning message! ",false);


            //check the violation message in Status column
            SimpleUtils.assertOnFail("There should have minor warning message display as: Age < 14yr old! ",
                    shiftOperatePage.getTheMessageOfTMScheduledStatus().contains("Age < 14 yr old"), false);

            newShiftPage.clickOnOfferOrAssignBtn();
            scheduleMainPage.saveSchedule();

            //check the violation in i icon popup of new create shift
            scheduleMainPage.clickOnOpenSearchBoxButton();
            scheduleMainPage.searchShiftOnSchedulePage(firstNameOfTM1);
            WebElement newAddedShift = scheduleShiftTablePage.getTheShiftByIndex(scheduleShiftTablePage.getAddedShiftIndexes(firstNameOfTM1).get(0));
            if (newAddedShift != null) {
                SimpleUtils.assertOnFail("The minor violation message display incorrectly in i icon popup! ",
                        scheduleShiftTablePage.getComplianceMessageFromInfoIconPopup(newAddedShift).contains("Age < 14 yr old"), false);
            } else
                SimpleUtils.fail("Get new added shift failed! ", false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
