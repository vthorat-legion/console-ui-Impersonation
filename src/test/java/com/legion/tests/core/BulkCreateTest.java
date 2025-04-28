package com.legion.tests.core;

import com.legion.api.abSwitch.ABSwitchAPI;
import com.legion.api.abSwitch.AbSwitches;
import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

import static com.legion.utils.MyThreadLocal.*;

public class BulkCreateTest extends TestBase {
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
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate all items display on the first page of Create shift modal")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAllItemsDisplayOnTheFirstPageOfCreateShiftModalAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            ABSwitchAPI.enableABSwitch(AbSwitches.NewCreateShift.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1));
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

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Verify the work role dropdown list is display on the create shift modal
            SimpleUtils.assertOnFail("The work role dropdown list is not loaded on New create shift page! ",
                    newShiftPage.checkIfWorkRoleDropDownIsLoadedOnNewCreateShiftPage(), false);
            //Search specific work role
            List<String> searchResult = newShiftPage.searchWorkRoleOnNewCreateShiftPage(workRole);
            SimpleUtils.assertOnFail("Work role search result display incorrectly, the expected is:"+ workRole
                            +" the actual is: "+ searchResult.get(0),
                    searchResult.size()==1 && searchResult.get(0).equals(workRole), false);

            //Verify the shift name input is display on the create shift modal
            SimpleUtils.assertOnFail("The shift name input is not loaded on New create shift page! ",
                    newShiftPage.checkIfShiftNameInputIsLoadedOnNewCreateShiftPage(), false);
            //Verify the shift start input and end input is display on the create shift modal
            SimpleUtils.assertOnFail("The shift start and end inputs are not loaded on New create shift page! ",
                    newShiftPage.checkIfShiftStartAndEndInputsAreLoadedOnNewCreateShiftPage(), false);
            //Verify the Next day icon is display on the create shift modal
            //https://legiontech.atlassian.net/browse/SCH-9842
            //newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(true);
            //Verify the Shifts per day input is display on the create shift modal
            SimpleUtils.assertOnFail("The shift per day input is not loaded on New create shift page! ",
                    newShiftPage.checkIfShiftPerDayInputIsLoadedOnNewCreateShiftPage(), false);
            int shiftPerDay = newShiftPage.getShiftPerDayValue();
            SimpleUtils.assertOnFail("The shift per day input value is incorrectly! The expected is: 1, the actual is: "+shiftPerDay,
                    shiftPerDay==1, false);
            //Verify the ‘Select Days’ check boxes are display on the create shift modal
            SimpleUtils.assertOnFail("The select days checkboxes are not loaded on New create shift page! ",
                    newShiftPage.checkIfSelectDaysCheckBoxAreLoadedOnNewCreateShiftPage(), false);
            //Verify the Assignment dropdown list is display on the create shift modal
            SimpleUtils.assertOnFail("The assignment dropdown list is not loaded on New create shift page! ",
                    newShiftPage.checkIfAssignmentDropDownListIsLoadedOnNewCreateShiftPage(), false);
            //Verify the Shift Notes textbox is display on the create shift modal
            SimpleUtils.assertOnFail("The shift notes textarea is not loaded on New create shift page! ",
                    newShiftPage.checkIfShiftNotesTextAreaIsLoadedOnNewCreateShiftPage(), false);
            //The Cancel and Next button will display before select Assignment
            SimpleUtils.assertOnFail("The Next button is not loaded on New create shift page! ",
                    newShiftPage.checkIfNextButtonIsLoadedOnNewCreateShiftPage(), false);
            SimpleUtils.assertOnFail("The Cancel button is not loaded on New create shift page! ",
                    newShiftPage.checkIfCancelButtonIsLoadedOnNewCreateShiftPage(), false);
            //The Cancel and Create button will display after select Open shift as Assignment
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            SimpleUtils.assertOnFail("The Create button is not loaded on New create shift page! ",
                    newShiftPage.checkIfCreateButtonIsLoadedOnNewCreateShiftPage(), false);
            SimpleUtils.assertOnFail("The Cancel button is not loaded on New create shift page! ",
                    newShiftPage.checkIfCancelButtonIsLoadedOnNewCreateShiftPage(), false);
            //The Cancel and Next button will display after select Assign or Offer as Assignment
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            SimpleUtils.assertOnFail("The Next button is not loaded on New create shift page! ",
                    newShiftPage.checkIfNextButtonIsLoadedOnNewCreateShiftPage(), false);
            SimpleUtils.assertOnFail("The Cancel button is not loaded on New create shift page! ",
                    newShiftPage.checkIfCancelButtonIsLoadedOnNewCreateShiftPage(), false);
            //The Cancel and Next button still display when click Next button with unselect options, like unselect work role
            newShiftPage.clickOnCreateOrNextBtn();
//            SimpleUtils.assertOnFail("The Next button is not loaded on New create shift page! ",
//                    newShiftPage.checkIfNextButtonIsLoadedOnNewCreateShiftPage(), false);
//            SimpleUtils.assertOnFail("The Cancel button is not loaded on New create shift page! ",
//                    newShiftPage.checkIfCancelButtonIsLoadedOnNewCreateShiftPage(), false);
            //Verify the X icon on the create shift modal
            SimpleUtils.assertOnFail("The close icon is not loaded on New create shift page! ",
                    newShiftPage.checkIfCloseIconIsLoadedOnNewCreateShiftPage(), false);
            //Verify the create shift modal will be closed after click X icon
            newShiftPage.closeNewCreateShiftPage();
            SimpleUtils.assertOnFail("New create shift page should not display! ",
                    !newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    //@Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate all items on the select TM page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAllItemsOnTheSelectTMPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            ABSwitchAPI.enableABSwitch(AbSwitches.NewCreateShift.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1));
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

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            Thread.sleep(2000);
            List<String> weekDaysToClose = new ArrayList<>(Arrays.asList("Sunday"));
            createSchedulePage.createScheduleForNonDGByWeekInfo("SUGGESTED", weekDaysToClose, null);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clickCloseBtnForCreateShift();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            newShiftPage.clickOnCreateOrNextBtn();
            //Check the work role warning message
            SimpleUtils.assertOnFail("The work role warning message is not loaded on New create shift page! ",
                    newShiftPage.checkIfWorkRoleWarningMessageIsLoaded(), false);
            //Check the work role warning message
            SimpleUtils.assertOnFail("The assignment warning message is not loaded on New create shift page! ",
                    newShiftPage.checkIfAssignmentWarningMessageIsLoaded(), false);
            //Check the select day warning message
            newShiftPage.clearAllSelectedDays();
            SimpleUtils.assertOnFail("The assignment warning message is not loaded on New create shift page! ",
                    newShiftPage.checkIfSelectDaysWarningMessageIsLoaded(), false);
            //Check the Shift start and end inputs warning message
            newShiftPage.moveSliderAtCertainPoint("2am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(true);
            newShiftPage.selectSpecificWorkDay(7);
            String expectMessage1 = "Hours on Friday: 5:00am - 12:00am. Hours on Saturday: 5:00am - 12:00am. Hours on Monday: 5:00am - 12:00am. Hours on Tuesday: 5:00am - 12:00am. Hours on Wednesday: 500am - 12:00am. Hours on Thursday: 5:00am - 12:00am";
            String expectMessage2 = "Hours on Friday: 5:00 am - 12:00 am. Hours on Saturday: 5:00 am - 12:00 am. " +
                    "Hours on Monday: 5:00 am - 12:00 am. Hours on Tuesday: 5:00 am - 12:00 am. Hours on Wednesday: 5:00 am - 12:00 am. " +
                    "Hours on Thursday: 5:00 am - 12:00 am";
            String actualMessage = newShiftPage.getShiftStartWarningMessage();
            SimpleUtils.assertOnFail("The shift start warning message display incorrectly. The expect is: "+ expectMessage1
                            + " the actual is "+ actualMessage,
                    (expectMessage1.equalsIgnoreCase(actualMessage) || expectMessage2.equalsIgnoreCase(actualMessage)), false);
            actualMessage = newShiftPage.getShiftEndWarningMessage();
            SimpleUtils.assertOnFail("The shift end warning message display incorrectly. The expect is: "+ expectMessage1
                            + " the actual is "+ actualMessage,
                    (expectMessage1.equalsIgnoreCase(actualMessage) || expectMessage2.equalsIgnoreCase(actualMessage)), false);

            newShiftPage.moveSliderAtCertainPoint("2pm", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(false);
            String expectMessage = "Start time should be before End time";
            actualMessage = newShiftPage.getShiftEndWarningMessage();
            SimpleUtils.assertOnFail("The shift end warning message display incorrectly. The expect is: "+ expectMessage
                            + " the actual is "+ actualMessage,
                    expectMessage.equalsIgnoreCase(actualMessage), false);

            //Check shift per day warning message
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(0);
            newShiftPage.clickOnCreateOrNextBtn();
            expectMessage = "Must be > 0";
            actualMessage = newShiftPage.getShiftPerDayWarningMessage();
            SimpleUtils.assertOnFail("The shift per day warning message display incorrectly. The expect is: "+ expectMessage
                            + " the actual is "+ actualMessage,
                    expectMessage.equalsIgnoreCase(actualMessage), false);

            newShiftPage.setShiftPerDayOnNewCreateShiftPage(101);
            newShiftPage.clickOnCreateOrNextBtn();
            expectMessage = "Max 100";
            actualMessage = newShiftPage.getShiftPerDayWarningMessage();
            SimpleUtils.assertOnFail("The shift per day warning message display incorrectly. The expect is: "+ expectMessage
                            + " the actual is "+ actualMessage,
                    expectMessage.equalsIgnoreCase(actualMessage), false);

            //Check closed day warning message
            Thread.sleep(5000);
            newShiftPage.moveMouseToSpecificWeekDayOnNewCreateShiftPage("Sun");
            Thread.sleep(5000);
            SimpleUtils.assertOnFail("The closed day tooltip is loaded! ",
                    newShiftPage.checkClosedDayTooltipIsLoaded(), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the warning messages of the items on the first page of create shift modal")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheWarningMessageOfTheItemsOnFirstPageOfCreateShiftModalAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            Thread.sleep(3000);
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = (int)(Math.random()*100+1);
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = (int)(Math.random()*7+1);
            newShiftPage.selectSpecificWorkDay(dayCount);
            List<String> selectedDays = newShiftPage.getSelectedDayInfoFromCreateShiftPage();
            String shiftStartTime = "11:00am";
            String shiftEndTime = "2:00pm";
            String totalHrs = "3 Hrs";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            //Check the ‘Shifts Assigned’ and 'Shift Offers' sections display on the right of the modal
            SimpleUtils.assertOnFail("The shift assign and offer sections are not loaded! ",
                    newShiftPage.checkIfShiftAssignAndOffersSectionsAreLoaded(), false);
            //Check the shifts assigned and shift offers message
            String shiftAssignedExpectMessage = "Shifts Assigned 0 of "+count;
            String shiftAssignedActualMessage = newShiftPage.getShiftAssignedMessage();
            String shiftOffersExpectMessage = "0 Shift Offers for "+count+" Open Shifts";
            if (count==1) {
                shiftOffersExpectMessage = "0 Shift Offers for "+count+" Open Shift";
            }
            String shiftOfferActualMessage = newShiftPage.getShiftOffersMessage();
            SimpleUtils.assertOnFail("The shift assigned message display incorrectly! the expect is:"+ shiftAssignedExpectMessage
                            +" the actual is: " +shiftAssignedActualMessage,
                    shiftAssignedActualMessage.equalsIgnoreCase(shiftAssignedExpectMessage), false);
            SimpleUtils.assertOnFail("The shift offer message display incorrectly! the expect is:"+ shiftOffersExpectMessage
                            +" the actual is: " +shiftOfferActualMessage,
                    shiftOfferActualMessage.equalsIgnoreCase(shiftOffersExpectMessage), false);

            //The ‘Open Shift’ avatars is show initially
            int openShiftCount = newShiftPage.getOpenShiftCountOnShiftAssignedSection();
            SimpleUtils.assertOnFail("The open shifts display incorrect, the expect is: "+count+", the actual is: "+openShiftCount,
                     openShiftCount== count, false);

            //Check the No of shift per day
            int noOfShiftPerDay = newShiftPage.getNoOfShiftPerDayOnSearchTMPage();
            SimpleUtils.assertOnFail("The no of shift per day display incorrect, the expect is: "+count+", the actual is: "+noOfShiftPerDay,
                    noOfShiftPerDay == count, false);

            //Check the days scheduled
            List<String> dayScheduled = Arrays.asList(newShiftPage.getDaysScheduledOnSearchTMPage().replace(" ","").split(","));
            SimpleUtils.assertOnFail("The day scheduled display incorrect, the expect is: "
                            +selectedDays+", the actual is: "+dayScheduled,
                    dayScheduled.equals(selectedDays), false);

            //Check work role, shift start and end time
            String shiftInfoOnShiftCard = newShiftPage.getShiftCardInfoOnSearchTMPage().toLowerCase();
            SimpleUtils.assertOnFail("The shift info on shift card display incorrect, the expect is: "
                            + workRole +" "+ shiftStartTime+"-"+ shiftEndTime +" "+ totalHrs+", the actual is: "+shiftInfoOnShiftCard,
                    shiftInfoOnShiftCard.contains(workRole.toLowerCase())
                            && shiftInfoOnShiftCard.replace(" ", "").contains(shiftStartTime+"–"+shiftEndTime)
                            && shiftInfoOnShiftCard.contains(totalHrs.toLowerCase()), false);

            //Set 1 for Shift per day input, the 'Assign shifts for each day' checkbox should not display
            newShiftPage.clickOnBackButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(1);
            newShiftPage.clickOnCreateOrNextBtn();
            SimpleUtils.assertOnFail("The assign shift for each day switch should not display",
                    !newShiftPage.checkAssignShiftsForEachDaySwitchIsLoaded(), false);
            //Set 10 for Shift per day input, the 'Assign shifts for each day' checkbox should display
            newShiftPage.clickOnBackButton();
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(7);
            newShiftPage.clickOnCreateOrNextBtn();
            SimpleUtils.assertOnFail("The assign shift for each day switch should display",
                    newShiftPage.checkAssignShiftsForEachDaySwitchIsLoaded(), false);
            // Open the switch, the each day tabs will display
            newShiftPage.openOrCloseAssignShiftsForEachDaySwitch(true);
            int assignShiftDayCount = newShiftPage.getAssignShiftsMessageOfEachDays().size();
            SimpleUtils.assertOnFail("The assign shift for each days fail to display, the expected: "+ 7
                            +" the actual is: "+ assignShiftDayCount,
                    assignShiftDayCount ==7, false);
            // Close the switch, the each day tabs will not display
            newShiftPage.openOrCloseAssignShiftsForEachDaySwitch(false);
            assignShiftDayCount = newShiftPage.getAssignShiftsMessageOfEachDays().size();
            SimpleUtils.assertOnFail("The assign shift for each days fail to display, the expected: "+ 0
                            +" the actual is: "+ assignShiftDayCount,
                    assignShiftDayCount ==0, false);
            //Verify the buttons on select TM page
            SimpleUtils.assertOnFail("The Back button is not loaded on New create shift page! ",
                    newShiftPage.checkIfBackButtonIsLoadedOnNewCreateShiftPage(), false);
            SimpleUtils.assertOnFail("The Create button is not loaded on New create shift page! ",
                    newShiftPage.checkIfCreateButtonIsLoadedOnNewCreateShiftPage(), false);

            //Verify the X icon on the create shift modal
            SimpleUtils.assertOnFail("The close icon is not loaded on New create shift page! ",
                    newShiftPage.checkIfCloseIconIsLoadedOnNewCreateShiftPage(), false);
            //Verify the create shift modal will be closed after click X icon
            Thread.sleep(3000);
            newShiftPage.closeNewCreateShiftPage();
            SimpleUtils.assertOnFail("New create shift page should not display! ",
                    !newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }




    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify assign or offer TMs in Search and Recommended tabs")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAssignOrOfferTMsInSearchAndRecommendedTabsAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            ABSwitchAPI.enableABSwitch(AbSwitches.NewCreateShift.getValue(), getUserNameNPwdForCallingAPI().get(0), getUserNameNPwdForCallingAPI().get(1));
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

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
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
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            Thread.sleep(3000);
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = (int)(Math.random()*(100-2)+2);
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 1;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();
            //Check the TMs on search TMs tabs, the TMs display with Assign and Offer buttons
            newShiftPage.searchWithOutSelectTM(firstNameOfTM);
            int resultCount = newShiftPage.getSearchAndRecommendedResult().size();
            int openShiftCount = newShiftPage.getOpenShiftCountOnShiftAssignedSection();
            //Click the Assign button of one TMs
            MyThreadLocal.setAssignTMStatus(true);
            String firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
            //Check the No. of shift per day display correctly:SCH-7055
            int shiftPerday = newShiftPage.getNoOfShiftPerDayOnSearchTMPage();
            SimpleUtils.assertOnFail("The shift per day display incorrectly, the expected is:"+count
                            +" The actual is:"+shiftPerday,
                    shiftPerday== count, false);
            //The TM will removed from the recommended list
            SimpleUtils.assertOnFail("The assinged TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount-1, false);
            //The TMs will replace the ‘Open Shift’ avatars on Shifts Assigned sections
            SimpleUtils.assertOnFail("The assinged TM should remove from search TM list! ",
                    newShiftPage.getOpenShiftCountOnShiftAssignedSection() == openShiftCount-1, false);
            List<String> assignedShifts = newShiftPage.getAssignedShiftOnShiftAssignedSection();
            SimpleUtils.assertOnFail("The assigned TM should display on the Shift Assigned section! ",
                    assignedShifts.size() == 1 && assignedShifts.get(0).contains(firstNameOfSelectedTM), false);
            newShiftPage.clickClearAssignmentsLink();
            //The TM will back to the search TM list after click clear assignment
            SimpleUtils.assertOnFail("The assinged TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount, false);
            SimpleUtils.assertOnFail("It should has no assigned TM display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().size() == 0, false);

            //Click the Offer button of one TMs
            MyThreadLocal.setAssignTMStatus(false);
            firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
            //Check the No. of shift per day display correctly:SCH-7055
//            shiftPerday = newShiftPage.getNoOfShiftPerDayOnSearchTMPage();
//            SimpleUtils.assertOnFail("The shift per day display incorrectly, the expected is:"+count
//                            +" The actual is:"+shiftPerday,
//                    shiftPerday== count, false);
            //The TM will removed from the recommended list
            SimpleUtils.assertOnFail("The offered TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount-1, false);
            //The TMs will display on Shifts Offers sections
            SimpleUtils.assertOnFail("The offered TM should display on the Shift Assigned section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM), false);
            newShiftPage.clickClearOfferLink();
            SimpleUtils.assertOnFail("The offered TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount, false);
            SimpleUtils.assertOnFail("It should has no offered TM display on the Shift Offers section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().size() == 0, false);

            //Check the TMs on Recommended TMs tabs, the TMs display with Assign and Offer buttons
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            Thread.sleep(5000);
            resultCount = newShiftPage.getSearchAndRecommendedResult().size();
            openShiftCount = newShiftPage.getOpenShiftCountOnShiftAssignedSection();
            //Click the Assign button of one TMs
            MyThreadLocal.setAssignTMStatus(true);
            firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
            //The TM will removed from the recommended list
            int actualCount = newShiftPage.getSearchAndRecommendedResult().size();
            SimpleUtils.assertOnFail("The assigned TM should remove from recommended list! The expected count is "+(resultCount-1)
                            + " the actual count is: "+actualCount,
                    actualCount== resultCount-1, false);
            //The TMs will replace the ‘Open Shift’ avatars on Shifts Assigned sections
            SimpleUtils.assertOnFail("The assigned TM should remove from recommended list! ",
                    newShiftPage.getOpenShiftCountOnShiftAssignedSection() == openShiftCount-1, false);
            assignedShifts = newShiftPage.getAssignedShiftOnShiftAssignedSection();
            SimpleUtils.assertOnFail("The assigned TM should display on the Shift Assigned section! ",
                    assignedShifts.size() == 1 && assignedShifts.get(0).contains(firstNameOfSelectedTM), false);

            //Click the Offer button of one TMs
            resultCount = newShiftPage.getSearchAndRecommendedResult().size();
            MyThreadLocal.setAssignTMStatus(false);
            String firstNameOfSelectedTM2 = newShiftPage.selectTeamMembers().split(" ")[0];
            //The TM will removed from the recommended list
            SimpleUtils.assertOnFail("The offered TM should remove from recommended list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount-1, false);
            //The TMs will display on Shifts Offers sections
            SimpleUtils.assertOnFail("The offered TM should display on the Shift Offers section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM2), false);

            resultCount = newShiftPage.getSearchAndRecommendedResult().size();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            //The selections should be keeping even if the user changes tabs
            SimpleUtils.assertOnFail("The offered TM should display on the Shift Assigned section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM2), false);
            SimpleUtils.assertOnFail("The assigned TM should display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM), false);

            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            //Verify the TMs on Recommended tab still display when back from search TM tab
            SimpleUtils.assertOnFail("The TMs on Recommended tab should display consistently with before after back from search TM tab! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount, false);

            //Verify the 'X' button for each selected employee’s avatars
            newShiftPage.removeAllOfferedShiftByClickRemoveIcon();
            newShiftPage.removeAllAssignedShiftByClickRemoveIcon();
            SimpleUtils.assertOnFail("It should has no assigned TM display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().size() == 0, false);
            SimpleUtils.assertOnFail("It should has no offered TM display on the Shift Offers section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().size() == 0, false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate Assign shifts for each day switch")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateAssignShiftsForEachDaySwitchAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
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
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            Thread.sleep(5000);
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 2;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 7;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();

            //‘Assign or offer shifts for each day’ option is off by default.
            SimpleUtils.assertOnFail("‘Assign or offer shifts for each day’ option should be disabled ff by default",
                    !newShiftPage.checkAssignShiftsForEachDaySwitchIfEnabled(), false);

            //The different day tabs for each day showing how many shifts for that day have been assigned
            newShiftPage.openOrCloseAssignShiftsForEachDaySwitch(true);
            List<String> assignShiftDaysMessage = newShiftPage.getAssignShiftsMessageOfEachDays();
            String eachDayShiftAssignedInfo = "0 of "+count;
            SimpleUtils.assertOnFail("There should has "+dayCount+" assign shift days display, but actual"+ assignShiftDaysMessage.size(),
                    assignShiftDaysMessage.size()==dayCount, false);
            for (int i=0; i<assignShiftDaysMessage.size();i++) {
                SimpleUtils.assertOnFail("The assign shift days message display should display as: "
                                +eachDayShiftAssignedInfo+", but actual"+ assignShiftDaysMessage,
                        assignShiftDaysMessage.get(i).contains(eachDayShiftAssignedInfo), false);
            }
            dayCount = (new Random()).nextInt(7);
            newShiftPage.selectAssignShiftDaysByIndex(dayCount);
            //Check the ‘Shifts Assigned’ and 'Shift Offers' sections display on the right of the modal for each days
            SimpleUtils.assertOnFail("The shift assign and offer sections are not loaded! ",
                    newShiftPage.checkIfShiftAssignAndOffersSectionsAreLoaded(), false);

            newShiftPage.searchWithOutSelectTM(firstNameOfTM);
            //The ‘Open Shift’ avatars is show initially
            int openShiftCount = newShiftPage.getOpenShiftCountOnShiftAssignedSection();
            SimpleUtils.assertOnFail("The open shifts display incorrect, the expect is: "+count+", the actual is: "+openShiftCount,
                    openShiftCount== count, false);

            int resultCount = newShiftPage.getSearchAndRecommendedResult().size();
    //                openShiftCount = newShiftPage.getOpenShiftCountOnShiftAssignedSection();
            //Click the Assign button of one TMs
            MyThreadLocal.setAssignTMStatus(true);
            String firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
            //The TM will removed from the recommended list
            SimpleUtils.assertOnFail("The assinged TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount-1, false);
            //The TMs will replace the ‘Open Shift’ avatars on Shifts Assigned sections
            SimpleUtils.assertOnFail("The assinged TM should remove from search TM list! ",
                    newShiftPage.getOpenShiftCountOnShiftAssignedSection() == openShiftCount-1, false);
            List<String> assignedShifts = newShiftPage.getAssignedShiftOnShiftAssignedSection();
            SimpleUtils.assertOnFail("The assigned TM should display on the Shift Assigned section! ",
                    assignedShifts.size() == 1 && assignedShifts.get(0).contains(firstNameOfSelectedTM), false);
            newShiftPage.clickClearAssignmentsLink();
            //The TM will back to the search TM list after click clear assignment
            SimpleUtils.assertOnFail("The assinged TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount, false);
            SimpleUtils.assertOnFail("It should has no assigned TM display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().size() == 0, false);

            //Click the Offer button of one TMs
            MyThreadLocal.setAssignTMStatus(false);
            firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
            //The TM will removed from the recommended list
            SimpleUtils.assertOnFail("The offered TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount-1, false);
            //The TMs will display on Shifts Offers sections
            SimpleUtils.assertOnFail("The offered TM should display on the Shift Assigned section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM), false);
            newShiftPage.clickClearOfferLink();
            SimpleUtils.assertOnFail("The offered TM should remove from search TM list! ",
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount, false);
            SimpleUtils.assertOnFail("It should has no offered TM display on the Shift Offers section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().size() == 0, false);

            //Check the TMs on Recommended TMs tabs, the TMs display with Assign and Offer buttons
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            resultCount = newShiftPage.getSearchAndRecommendedResult().size();
            openShiftCount = newShiftPage.getOpenShiftCountOnShiftAssignedSection();
            //Click the Assign button of one TMs
            MyThreadLocal.setAssignTMStatus(true);
            firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
            //The TM will removed from the recommended list
            int actualCount = newShiftPage.getSearchAndRecommendedResult().size();
            SimpleUtils.assertOnFail("The assigned TM should remove from recommended list! The expected count is:"
                            + (resultCount-1)+" The actual count is: "+actualCount,
                    actualCount == resultCount-1, false);
            //The TMs will replace the ‘Open Shift’ avatars on Shifts Assigned sections
            SimpleUtils.assertOnFail("The assigned TM should remove from recommended list! ",
                    newShiftPage.getOpenShiftCountOnShiftAssignedSection() == openShiftCount-1, false);
            assignedShifts = newShiftPage.getAssignedShiftOnShiftAssignedSection();
            SimpleUtils.assertOnFail("The assigned TM should display on the Shift Assigned section! ",
                    assignedShifts.size() == 1 && assignedShifts.get(0).contains(firstNameOfSelectedTM), false);

            //Click the Offer button of one TMs
            resultCount = newShiftPage.getSearchAndRecommendedResult().size();
            if (resultCount==0) {
                SimpleUtils.fail("There is no employee display on search or recommended list! ", false);
            }else
                SimpleUtils.pass("Get employee count successfully! ");
            MyThreadLocal.setAssignTMStatus(false);
            String firstNameOfSelectedTM2 = newShiftPage.selectTeamMembers().split(" ")[0];
            //The TM will removed from the recommended list
            actualCount = newShiftPage.getSearchAndRecommendedResult().size();
            SimpleUtils.assertOnFail("The assigned TM should remove from recommended list! The expected count is:"
                            + (resultCount-1)+" The actual count is: "+actualCount,
                    actualCount == resultCount-1, false);
            //The TMs will display on Shifts Offers sections
            SimpleUtils.assertOnFail("The offered TM should display on the Shift Offers section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM2), false);

            resultCount = newShiftPage.getSearchAndRecommendedResult().size();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            //The selections should be keeping even if the user changes tabs
            SimpleUtils.assertOnFail("The offered TM should display on the Shift Assigned section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM2), false);
            SimpleUtils.assertOnFail("The assigned TM should display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM), false);

            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            //Verify the TMs on Recommended tab still display when back from search TM tab
            SimpleUtils.assertOnFail("The TMs on Recommended tab should display consistently with before after back from search TM tab! The expect is: "
                            +resultCount + " the actual is: "+newShiftPage.getSearchAndRecommendedResult().size(),
                    newShiftPage.getSearchAndRecommendedResult().size() == resultCount, false);

            //Verify the 'X' button for each selected employee’s avatars
            newShiftPage.removeAllOfferedShiftByClickRemoveIcon();
            newShiftPage.removeAllAssignedShiftByClickRemoveIcon();
            SimpleUtils.assertOnFail("It should has no assigned TM display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().size() == 0, false);
            SimpleUtils.assertOnFail("It should has no offered TM display on the Shift Offers section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().size() == 0, false);
            //                shiftOperatePage.switchSearchTMAndRecommendedTMsTab();

            //The selections should be keeping even if the user changes tabs for each day
            newShiftPage.selectAssignShiftDaysByIndex(0);
            //Assign one shift
            MyThreadLocal.setAssignTMStatus(true);
            firstNameOfSelectedTM = newShiftPage.selectTeamMembers().split(" ")[0];
            //Offer one shift
            MyThreadLocal.setAssignTMStatus(false);
            firstNameOfSelectedTM2 = newShiftPage.selectTeamMembers().split(" ")[0];
            newShiftPage.selectAssignShiftDaysByIndex(1);
            newShiftPage.selectAssignShiftDaysByIndex(0);
            SimpleUtils.assertOnFail("The assigned TM should display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM), false);
            SimpleUtils.assertOnFail("The offered TM should display on the Shift Assigned section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().get(0).contains(firstNameOfSelectedTM2), false);

            //Check assign for all days warning
            String message1 = "Assign for All Days";
            String message2 = "If you switch to assigning all days at once all your current assignments and offers will be lost";
            newShiftPage.clickAssignShiftsForEachDaySwitch();
            SimpleUtils.assertOnFail("The assign for all days popup should display! ",
                    newShiftPage.checkConfirmPopupIsLoaded(), false);
            SimpleUtils.assertOnFail("The title and message on confirm popup display incorrectly, " +
                    "the expected title is: "+ message1
                    +"the actual title is"+newShiftPage.getTitleOfConfirmPopup()
                    + " the expect message is: "+message2
                    + " the actual message is: "+ newShiftPage.getMessageOfConfirmPopup(),
                    message1.equals(newShiftPage.getTitleOfConfirmPopup())
                            && message2.equals(newShiftPage.getMessageOfConfirmPopup()), false);
            newShiftPage.clickOkBtnOnConfirmPopup();
            SimpleUtils.assertOnFail("‘Assign or offer shifts for each day’ option should be disabled! ",
                    !newShiftPage.checkAssignShiftsForEachDaySwitchIfEnabled(), false);
            openShiftCount = newShiftPage.getOpenShiftCountOnShiftAssignedSection();
            SimpleUtils.assertOnFail("The open shifts display incorrect, the expect is: "+count+", the actual is: "+openShiftCount,
                    openShiftCount== count, false);
            SimpleUtils.assertOnFail("It should has no assigned TM display on the Shift Assigned section! ",
                    newShiftPage.getAssignedShiftOnShiftAssignedSection().size() == 0, false);
            SimpleUtils.assertOnFail("It should has no offered TM display on the Shift Offers section! ",
                    newShiftPage.getShiftOffersOnShiftAssignedSection().size() == 0, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify assign shift by each days")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAssignShiftByEachDaysAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            List<String> shiftInfo = new ArrayList<>();
            String firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
                Thread.sleep(3000);
            }
            String workRole = shiftInfo.get(4);
            String lastNameOfTM = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
//            scheduleMainPage.saveSchedule();
//            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            Thread.sleep(3000);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 7;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();

            //Verify the assigned all days shifts by each days
            newShiftPage.openOrCloseAssignShiftsForEachDaySwitch(true);
            for (int i =0; i< 7; i++) {
                newShiftPage.selectAssignShiftDaysByIndex(i);
                newShiftPage.searchWithOutSelectTM(firstNameOfTM+" "+lastNameOfTM);
                MyThreadLocal.setAssignTMStatus(true);
                newShiftPage.selectTeamMembers();
            }
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            for (int i =0; i< 7; i++) {
                SimpleUtils.assertOnFail("The "+firstNameOfTM+"'s shift is not exist on the " +i +" day! ",
                        scheduleShiftTablePage.getOneDayShiftByName(i, firstNameOfTM).size()==1, false);
            }

            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            firstNameOfTM = "";
            while (firstNameOfTM.equals("") || firstNameOfTM.equalsIgnoreCase("Open")
                    || firstNameOfTM.equalsIgnoreCase("Unassigned")) {
                shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getRandomIndexOfShift());
                //Search shift by TM names: first name and last name
                firstNameOfTM = shiftInfo.get(0);
            }
            workRole = shiftInfo.get(4);
            lastNameOfTM = shiftInfo.get(5);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(firstNameOfTM);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            Thread.sleep(3000);
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();

            //Verify the assigned some but not all days shifts by each days
            newShiftPage.openOrCloseAssignShiftsForEachDaySwitch(true);
            for (int i =0; i< 7; i++) {
                if (i== 0 || i ==1 || i==2) {
                    newShiftPage.selectAssignShiftDaysByIndex(i);
                    newShiftPage.searchWithOutSelectTM(firstNameOfTM+" "+lastNameOfTM);
                    MyThreadLocal.setAssignTMStatus(true);
                    newShiftPage.selectTeamMembers();
                }
            }
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();

            for (int i =0; i< 7; i++) {
                if (i== 0 || i ==1 || i==2) {
                    SimpleUtils.assertOnFail("The "+firstNameOfTM+"''s shift is not exist on the " +i +" day! ",
                            scheduleShiftTablePage.getOneDayShiftByName(i, firstNameOfTM).size()==1, false);
                } else {
                    SimpleUtils.assertOnFail("The open shift is not exist on the " +i +" day! ",
                            scheduleShiftTablePage.getOneDayShiftByName(i, "open").size()==1, false);
                }
            }


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the auto assignment workflow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheAutoAssignmentWorkFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            if (isWeekGenerated && scheduleShiftTablePage.getAllAvailableShiftsInWeekView().size()==0) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();

            String workRole = shiftOperatePage.getRandomWorkRole();

            //Verify the auto assignment workflow with one shift for one days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
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
            scheduleMainPage.saveSchedule();
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

            //Verify the auto assignment workflow with more than one shift for more than one days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            count = 5;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            dayCount = 3;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.setShiftNameOnNewCreateShiftPage(shiftName);
            newShiftPage.setShiftNotesOnNewCreateShiftPage(shiftNotes);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            for (int i=0; i< dayCount; i++) {
                List<WebElement> openShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(i, "Open");
                SimpleUtils.assertOnFail("The open shift is not exist on the "+(i+1)+" day! The expected is: "+count
                                +" The actual is:"+openShiftsOfOneDay.size(),
                        openShiftsOfOneDay.size()==count, false);

                for (int j=0;j<count;j++) {
                    String shiftId = openShiftsOfOneDay.get(j).getAttribute("id").toString();
                    shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(scheduleShiftTablePage.getShiftIndexById(shiftId));
                    shiftTime = shiftInfo.get(2);
                    workRoleOfNewShift = shiftInfo.get(4);
                    shiftHrs = shiftInfo.get(6);
                    shiftNameOfNewShift = shiftInfo.get(7);
                    shiftNotesOfNewShift = shiftInfo.get(8);
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
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the offer TMs workflow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheOfferTMsWorkFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("unassigned");
            scheduleMainPage.saveSchedule();

            String workRole = shiftOperatePage.getRandomWorkRole();

            //Verify the auto offer workflow with one shift for one days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
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
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
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


            //Verify the auto offer workflow with multiple shifts for multiple days without offer TMs
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleMainPage.saveSchedule();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            count = 5;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            dayCount = 3;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.setShiftNameOnNewCreateShiftPage(shiftName);
            newShiftPage.setShiftNotesOnNewCreateShiftPage(shiftNotes);
            newShiftPage.clickOnCreateOrNextBtn();
            //Don't select TMs to offer and click Create button
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            for (int i=0; i< dayCount; i++) {
                List<WebElement> openShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(i, "Open");
                SimpleUtils.assertOnFail("The open shift is not exist on the "+(i+1)+" day! ",
                        openShiftsOfOneDay.size()==count, false);

                for (int j=0;j<count;j++) {
                    String shiftId = openShiftsOfOneDay.get(j).getAttribute("id").toString();
                    index = scheduleShiftTablePage.getShiftIndexById(shiftId);
                    shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
                    shiftTime = shiftInfo.get(2);
                    workRoleOfNewShift = shiftInfo.get(4);
                    shiftHrs = shiftInfo.get(6);
                    shiftNameOfNewShift = shiftInfo.get(7);
                    shiftNotesOfNewShift = shiftInfo.get(8);
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
                    scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
                    scheduleShiftTablePage.clickViewStatusBtn();
                    SimpleUtils.assertOnFail("It should has no offers in the offer list! ",
                            !shiftOperatePage.checkIfOfferListHasOffers(), false);
                    shiftOperatePage.closeViewStatusContainer();
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify offer shift by each days")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateOfferShiftByEachDaysAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("06:00AM", "06:00AM");
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 7;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();

            //Verify the offer all days shifts by each days
            newShiftPage.openOrCloseAssignShiftsForEachDaySwitch(true);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            List<String> selectedTMs = new ArrayList<>();
            for (int i =0; i< 7; i++) {
                newShiftPage.selectAssignShiftDaysByIndex(i);
                selectedTMs.add(newShiftPage.selectTeamMembers());
            }
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            for (int i =0; i< 7; i++) {
                List<WebElement> openShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(i, "Open");
                SimpleUtils.assertOnFail("The open shift is not exist on the " +i +" day! ",
                        openShiftsOfOneDay.size()==1, false);
                String shiftId = openShiftsOfOneDay.get(0).getAttribute("id").toString();
                int index = scheduleShiftTablePage.getShiftIndexById(shiftId);
                scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
                scheduleShiftTablePage.clickViewStatusBtn();
                SimpleUtils.assertOnFail("The "+selectedTMs.get(i)+" should display on the offer list! ",
                        !shiftOperatePage.getOfferStatusFromOpenShiftStatusList(selectedTMs.get(i)).equals(""), false);
                shiftOperatePage.closeViewStatusContainer();
            }

            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();

            //Verify the assigned some but not all days shifts by each days
            newShiftPage.openOrCloseAssignShiftsForEachDaySwitch(true);
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            selectedTMs = new ArrayList<>();
            for (int i =0; i< 7; i++) {
                if (i== 0 || i ==1 || i==2) {
                    newShiftPage.selectAssignShiftDaysByIndex(i);
                    selectedTMs.add(newShiftPage.selectTeamMembers());
                }
            }
            newShiftPage.clickOnCreateOrNextBtn();
            Thread.sleep(5000);
            scheduleMainPage.saveSchedule();
            for (int i =0; i< 7; i++) {
                List<WebElement> openShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(i, "Open");
                SimpleUtils.assertOnFail("The open shift is not exist on the " +i +" day! ",
                        openShiftsOfOneDay.size()==1, false);
                String shiftId = openShiftsOfOneDay.get(0).getAttribute("id").toString();
                int index = scheduleShiftTablePage.getShiftIndexById(shiftId);
                scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
                scheduleShiftTablePage.clickViewStatusBtn();
                if (i== 0 || i ==1 || i==2) {
                    SimpleUtils.assertOnFail("The "+selectedTMs.get(i)+" should display on the offer list! ",
                            !shiftOperatePage.getOfferStatusFromOpenShiftStatusList(selectedTMs.get(i)).equals(""), false);
                } else {
                    SimpleUtils.assertOnFail("It should has no offers in the offer list! ",
                            !shiftOperatePage.checkIfOfferListHasOffers(), false);
                }
                shiftOperatePage.closeViewStatusContainer();
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the assign and offer workflow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheAssignAndOfferWorkFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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

            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Unassigned");
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            int count = 2;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            int dayCount = 1;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();

            //Assign TM for one shift and offer TM for another shifts
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            MyThreadLocal.setAssignTMStatus(true);
            String assignedTM = newShiftPage.selectTeamMembers();
            MyThreadLocal.setAssignTMStatus(false);
            String offeredTM = newShiftPage.selectTeamMembers();
            newShiftPage.clickOnCreateOrNextBtn();
            Thread.sleep(5000);
            scheduleMainPage.saveSchedule();
            scheduleMainPage.publishOrRepublishSchedule();
            List<WebElement> openShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, "Open");
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    openShiftsOfOneDay.size()==1, false);
            String shiftId = openShiftsOfOneDay.get(0).getAttribute("id").toString();
            int index = scheduleShiftTablePage.getShiftIndexById(shiftId);
            scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
            scheduleShiftTablePage.clickViewStatusBtn();
            SimpleUtils.assertOnFail("The "+offeredTM+" should display on the offer list! ",
                    !shiftOperatePage.getOfferStatusFromOpenShiftStatusList(offeredTM).equals(""), false);
            shiftOperatePage.closeViewStatusContainer();

            List<WebElement> assignedShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, assignedTM.split(" ")[0]);
            SimpleUtils.assertOnFail("The "+assignedTM+" shift is not exist on the first day! ",
                    assignedShiftsOfOneDay.size()>=1, false);

            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("Open");
            scheduleMainPage.saveSchedule();
            scheduleMainPage.publishOrRepublishSchedule();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            count =7;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            List<String> assignedTMs = new ArrayList<>();
            List<String> offeredTMs = new ArrayList<>();
            for (int i=0;i< count;i++) {
                if (i <5) {
                    MyThreadLocal.setAssignTMStatus(true);
                    assignedTMs.add(newShiftPage.selectTeamMembers());
                } else {
                    MyThreadLocal.setAssignTMStatus(false);
                    offeredTMs.add(newShiftPage.selectTeamMembers());
                }
            }
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            openShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, "Open");
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    openShiftsOfOneDay.size()>=2, false);
            for (int i =0; i<5; i++) {
                shiftId = openShiftsOfOneDay.get(0).getAttribute("id");
                index = scheduleShiftTablePage.getShiftIndexById(shiftId);
                scheduleShiftTablePage.clickProfileIconOfShiftByIndex(index);
                scheduleShiftTablePage.clickViewStatusBtn();
                for (int j=0;j< offeredTMs.size();j++){
                    SimpleUtils.assertOnFail("The "+offeredTMs.get(j)+" should display on the offer list! ",
                            !shiftOperatePage.getOfferStatusFromOpenShiftStatusList(offeredTMs.get(j).split(" ")[0]).equals(""), false);
                }
                shiftOperatePage.closeViewStatusContainer();
            }

            for (int i=0;i< 5;i++) {
                assignedShiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, assignedTMs.get(i).split(" ")[0]);
                SimpleUtils.assertOnFail("The "+assignedTMs.get(i)+" shift is not exist on the first day! ",
                        assignedShiftsOfOneDay.size()>=1, false);
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the assign shifts workflow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheAssignShiftsWorkFlowAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView("open");
            scheduleMainPage.saveSchedule();
            //Verify the assign workflow with one shift for one days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
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
            List<WebElement> shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
            SimpleUtils.assertOnFail("The "+selectedTM1+ "shift is not exist on the first day! ",
                    shiftsOfOneDay.size()>=1, false);
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
            SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                    shiftsOfOneDay.size()>=1, false);
            createSchedulePage.publishActiveSchedule();
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

            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            scheduleShiftTablePage.bulkDeleteTMShiftsInWeekView(selectedTM1.split(" ")[0]);
            scheduleMainPage.saveSchedule();

            //Verify the assign TMs workflow with multiple shifts for multiple days
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.selectDaysByIndex(1,1,1);
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count =5;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            dayCount = 7;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.setShiftNameOnNewCreateShiftPage(shiftName);
            newShiftPage.setShiftNotesOnNewCreateShiftPage(shiftNotes);
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            selectedTM1 = newShiftPage.selectTeamMembers();
            String selectedTM2 = newShiftPage.selectTeamMembers();
            String selectedTM3 = newShiftPage.selectTeamMembers();
            String selectedTM4 = newShiftPage.selectTeamMembers();
            String selectedTM5 = newShiftPage.selectTeamMembers();
            List<String> selectedTMs = new ArrayList<>();
            selectedTMs.add(selectedTM1);
            selectedTMs.add(selectedTM2);
            selectedTMs.add(selectedTM3);
            selectedTMs.add(selectedTM4);
            selectedTMs.add(selectedTM5);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            for (int i =0; i<dayCount; i++) {
                for (int j=0;j<selectedTMs.size();j++) {
                    shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(i, selectedTMs.get(j).split(" ")[0]);
                    SimpleUtils.assertOnFail("The "+selectedTMs.get(j)+" shift is not exist on the "+i+" day! ",
                            shiftsOfOneDay.size()>=1, false);

                    shiftId = shiftsOfOneDay.get(0).getAttribute("id").toString();
                    index = scheduleShiftTablePage.getShiftIndexById(shiftId);
                    shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
                    shiftTime = shiftInfo.get(2);
                    workRoleOfNewShift = shiftInfo.get(4);
                    shiftHrs = shiftInfo.get(8);
                    shiftNameOfNewShift = shiftInfo.get(9);
                    shiftNotesOfNewShift = shiftInfo.get(10);
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
                }
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the shifts can be created after update shift info")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheShiftsCanBeCreatedAfterUpdateShiftInfoAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            String shiftStartTime = "8:00am";
            String shiftEndTime = "11:00am";
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
            newShiftPage.selectTeamMembers();
            //Click Back button and update the shift info
            newShiftPage.clickOnBackButton();
            //Update the shift times, shift per day and select days check boxes
            shiftStartTime = "9:00am";
            shiftEndTime = "2:00pm";
            String totalHrs = "4.5 Hrs";
            double totalWeekHrs = 31.5;
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            count =5;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            dayCount = 7;
            newShiftPage.selectSpecificWorkDay(dayCount);
            newShiftPage.clickOnCreateOrNextBtn();
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            String selectedTM1 = newShiftPage.selectTeamMembers();
            String selectedTM2 = newShiftPage.selectTeamMembers();
            String selectedTM3 = newShiftPage.selectTeamMembers();
            String selectedTM4 = newShiftPage.selectTeamMembers();
            String selectedTM5 = newShiftPage.selectTeamMembers();
            List<String> selectedTMs = new ArrayList<>();
            selectedTMs.add(selectedTM1);
            selectedTMs.add(selectedTM2);
            selectedTMs.add(selectedTM3);
            selectedTMs.add(selectedTM4);
            selectedTMs.add(selectedTM5);
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
            int i = (new Random()).nextInt(dayCount);
//            for (int i =0; i<dayCount; i++) {
                for (int j=0;j<selectedTMs.size();j++) {
                    List<WebElement> shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(i, selectedTMs.get(j).split(" ")[0]);
                    SimpleUtils.assertOnFail("The "+selectedTMs.get(j)+" shift is not exist on the "+i+" day! ",
                            shiftsOfOneDay.size()>=1, false);

                    String shiftId = shiftsOfOneDay.get(0).getAttribute("id").toString();
                    int index = scheduleShiftTablePage.getShiftIndexById(shiftId);
                    List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
                    String shiftTime = shiftInfo.get(2);
                    String workRoleOfNewShift = shiftInfo.get(4);
                    String shiftHrs = shiftInfo.get(8);
                    String shiftNameOfNewShift = shiftInfo.get(9);
                    String shiftNotesOfNewShift = shiftInfo.get(10);
                    String weeklyHrs = shiftInfo.get(7);
                    SimpleUtils.assertOnFail("The new shift's shift time display incorrectly, the expected is:"+shiftStartTime+"-"+shiftEndTime
                                    + " the actual is: "+ shiftTime,
                            shiftTime.equalsIgnoreCase(shiftStartTime+"-"+shiftEndTime), false);
                    SimpleUtils.assertOnFail("The new shift's work role display incorrectly, the expected is:"+ workRole
                                    + " the actual is: "+ workRoleOfNewShift,
                            workRoleOfNewShift.equalsIgnoreCase(workRole), false);
                    SimpleUtils.assertOnFail("The new shift's hrs display incorrectly, the expected is:"+ totalHrs
                                    + " the actual is: "+ shiftHrs,
                            totalHrs.equalsIgnoreCase(shiftHrs)
                                    || "5 Hrs".equalsIgnoreCase(shiftHrs), false);
                    SimpleUtils.assertOnFail("The new shift's name display incorrectly, the expected is:"+ shiftName
                                    + " the actual is: "+ shiftNameOfNewShift,
                            shiftName.equals(shiftNameOfNewShift), false);
                    SimpleUtils.assertOnFail("The new shift's notes display incorrectly, the expected is:"+ shiftNotes
                                    + " the actual is: "+ shiftNotesOfNewShift,
                            shiftNotes.equalsIgnoreCase(shiftNotesOfNewShift), false);
                    SimpleUtils.assertOnFail("The TM's week hrs display incorrectly, the expected is:"+ totalWeekHrs
                                    + " the actual is: "+ weeklyHrs,
                            Double.parseDouble(weeklyHrs.split(" ")[0]) >= totalWeekHrs, false);
//                }
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate the Next day check box on create shift modal")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheNextDayCheckBoxOnCreateShiftModalAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
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
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00AM", "08:00AM");
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            //Click New shift button to open create shift modal
            newShiftPage.clickOnDayViewAddNewShiftButton();
            newShiftPage.clickCloseBtnForCreateShift();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            int count = 1;
            newShiftPage.setShiftPerDayOnNewCreateShiftPage(count);
            newShiftPage.clearAllSelectedDays();
            newShiftPage.selectDaysByIndex(1,1,1);
            String shiftStartTime = "8:00am";
            String shiftEndTime = "9:00am";
            String shiftHrs = "25 Hrs";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.checkOrUnCheckNextDayOnCreateShiftModal(true);
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            String shiftInfoOnShiftCard = newShiftPage.getShiftCardInfoOnSearchTMPage();
            SimpleUtils.assertOnFail("The next day shift hrs display incorrectly! the expected is: "+shiftHrs
                            +" the actual is: "+ newShiftPage.getShiftCardInfoOnSearchTMPage(),
                    shiftInfoOnShiftCard.contains(shiftHrs), false);
            String selectedTM = newShiftPage.selectTeamMembers();
            newShiftPage.clickOnCreateOrNextBtn();
            scheduleMainPage.saveSchedule();
            Thread.sleep(5000);
                List<WebElement> shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(1, selectedTM.split(" ")[0]);
                SimpleUtils.assertOnFail("The "+selectedTM+" shift is not exist on the "+1+" day! ",
                        shiftsOfOneDay.size()==1, false);

                String shiftId = shiftsOfOneDay.get(0).getAttribute("id").toString();
                int index = scheduleShiftTablePage.getShiftIndexById(shiftId);
                List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoByIndex(index);
                String shiftTime = shiftInfo.get(2);
                String workRoleOfNewShift = shiftInfo.get(4);
                String shiftHrsOfNewShift = shiftInfo.get(8);
                SimpleUtils.assertOnFail("The new shift's shift time display incorrectly, the expected is:"+shiftStartTime+"-"+shiftEndTime
                                + " the actual is: "+ shiftTime,
                        shiftTime.equalsIgnoreCase(shiftStartTime+"-"+shiftEndTime), false);
                SimpleUtils.assertOnFail("The new shift's work role display incorrectly, the expected is:"+ workRole
                                + " the actual is: "+ workRoleOfNewShift,
                        workRoleOfNewShift.equalsIgnoreCase(workRole), false);
                SimpleUtils.assertOnFail("The new shift's hrs display incorrectly, the expected is:"+ shiftHrsOfNewShift
                                + " the actual is: "+ shiftHrs,
                        shiftHrsOfNewShift.equalsIgnoreCase(shiftHrs)
                                || shiftHrsOfNewShift.equalsIgnoreCase("24.5 Hrs")
                                || shiftHrsOfNewShift.equalsIgnoreCase("26 Hrs"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }



    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Validate shifts can be created by new UI by different access roles")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateShiftsCanBeCreatedByNewUIByDifferentAccessRolesAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            LoginPage loginPage = pageFactory.createConsoleLoginPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
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
            loginPage.logOut();
            int count = (int)(Math.random()*8+1);
            String accessRole = "";
            boolean isTL = false;
            switch(count) {
                case 1: accessRole = AccessRoles.StoreManager.getValue(); break;
                case 2: accessRole = AccessRoles.StoreManager2.getValue(); break;
                case 3: accessRole = AccessRoles.TeamLead.getValue();
                        isTL = true;
                        break;
                case 4: accessRole = AccessRoles.TeamLead2.getValue();
                        isTL = true;
                        break;
                case 5: accessRole = AccessRoles.DistrictManager.getValue();break;
                case 6: accessRole = AccessRoles.DistrictManager2.getValue();break;
                case 7: accessRole = AccessRoles.CustomerAdmin.getValue();break;
                case 8: accessRole = AccessRoles.CustomerAdmin2.getValue();break;
            }
            SimpleUtils.report("Will login as: "+ accessRole);
            //Verify the shifts can be created by new UI by original SM access role
            loginAsDifferentRole(accessRole);
            createShiftsByDifferentAccessRoles(isTL);
//            loginPage.logOut();

//            //Verify the shifts can be created by new UI by custom SM access role
//            loginAsDifferentRole(AccessRoles.StoreManager2.getValue());
//            createShiftsByDifferentAccessRoles(false);
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by original TL access role
//            loginAsDifferentRole(AccessRoles.TeamLead.getValue());
//            createShiftsByDifferentAccessRoles(true);
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by custom TL access role
//            loginAsDifferentRole(AccessRoles.TeamLead2.getValue());
//            createShiftsByDifferentAccessRoles(true);
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by original DM access role
//            loginAsDifferentRole(AccessRoles.DistrictManager.getValue());
//            createShiftsByDifferentAccessRoles(false);
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by custom DM access role
//            loginAsDifferentRole(AccessRoles.DistrictManager2.getValue());
//            createShiftsByDifferentAccessRoles(false);
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by original CA access role
//            loginAsDifferentRole(AccessRoles.CustomerAdmin.getValue());
//            createShiftsByDifferentAccessRoles(false);
//            loginPage.logOut();
//
//            //Verify the shifts can be created by new UI by custom CA access role
//            loginAsDifferentRole(AccessRoles.CustomerAdmin2.getValue());
//            createShiftsByDifferentAccessRoles(false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    private void createShiftsByDifferentAccessRoles (boolean isTL) throws Exception {
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        if (scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue())) {
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
        }
        scheduleCommonPage.navigateToNextWeek();
        boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (!isWeekGenerated) {
            createSchedulePage.createScheduleForNonDGFlowNewUI();
        }

        //Verify the assign workflow with one shift for one days
        String workRole = shiftOperatePage.getRandomWorkRole();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        newShiftPage.clickOnDayViewAddNewShiftButton();
        SimpleUtils.assertOnFail("New create shift page is not display! ",
                newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
        //Fill the required option

        newShiftPage.selectWorkRole(workRole);
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
        List<WebElement> shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0,
                selectedTM1.split(" ")[0]+" "+selectedTM1.split(" ")[1].substring(0,1));
        SimpleUtils.assertOnFail("The "+selectedTM1+ "shift is not exist on the first day! ",
                shiftsOfOneDay.size()>=1, false);
        scheduleMainPage.saveSchedule();
        Thread.sleep(5000);
        shiftsOfOneDay = scheduleShiftTablePage.getOneDayShiftByName(0, selectedTM1.split(" ")[0]);
        SimpleUtils.assertOnFail("The open shift is not exist on the first day! ",
                shiftsOfOneDay.size()>=1, false);
        createSchedulePage.publishActiveSchedule();
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
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
//    @Enterprise(name = "Vailqacn_Enterprise")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Employee ID display correctly on select TM page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyTheEmployeeIDDisplayCorrectlyOnSelectTMPageAsInternalAdmin (String browser, String username, String password, String location) throws Exception{
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
            goToSchedulePageScheduleTab();
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }
            String workRole = shiftOperatePage.getRandomWorkRole();
            scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
            newShiftPage.clickOnDayViewAddNewShiftButton();
            SimpleUtils.assertOnFail("New create shift page is not display! ",
                    newShiftPage.checkIfNewCreateShiftPageDisplay(), false);
            //Fill the required option
            newShiftPage.selectWorkRole(workRole);
            String shiftStartTime = "8:00am";
            String shiftEndTime = "11:00am";
            newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            newShiftPage.clickOnCreateOrNextBtn();
            //Select 3 TMs to assign and click Create button
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            HashMap<String, String> tmAllInfo = shiftOperatePage.getTMAllInfoFromSearchOrRecommendedListOnNewCreateShiftPageByIndex(0);
            String tmName = tmAllInfo.get("tmname");
            String employeeIdOnRecommendedPage = tmAllInfo.get("employeeid");
            shiftOperatePage.switchSearchTMAndRecommendedTMsTab();
            newShiftPage.searchWithOutSelectTM(tmName);
            tmAllInfo = shiftOperatePage.getTMAllInfoFromSearchOrRecommendedListOnNewCreateShiftPageByIndex(0);
            String employeeIdOnSearchPage = tmAllInfo.get("employeeid");
            newShiftPage.clickCloseBtnForCreateShift();
            scheduleMainPage.clickOnCancelButtonOnEditMode();
            teamPage.goToTeam();
            teamPage.searchAndSelectTeamMemberByName(tmName);
            String employeeIDOnProfilePage = profileNewUIPage.getHRProfileInfo().get("EMPLOYEE ID");
            SimpleUtils.assertOnFail("The employee id on profile page is: "+employeeIDOnProfilePage
                            + ". The employee id on recommended page is: "+employeeIdOnRecommendedPage
                            + ". The employee id on search Tm page is: "+ employeeIdOnSearchPage,
                    employeeIDOnProfilePage.equalsIgnoreCase(employeeIdOnRecommendedPage)
                            && employeeIDOnProfilePage.equalsIgnoreCase(employeeIdOnSearchPage), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
