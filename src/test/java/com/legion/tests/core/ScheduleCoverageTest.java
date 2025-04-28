package com.legion.tests.core;

import com.legion.api.toggle.ToggleAPI;
import com.legion.api.toggle.Toggles;
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
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleCoverageTest extends TestBase {
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
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify Guidance label will show when budget is disabled and Budgeted label will show when budget is enabled")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateGuidanceAndBudgetLabelOnScheduleHoursAndIconsAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
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

            //Create the schedule if it is not created
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

            //Mouse hover the scheduled hour, check the label in the pop up panel
            String toolTipOfClockImg = scheduleShiftTablePage.getTheTooltipOfClockImgsByIndex(1);
            String toolTipInWeekView = scheduleShiftTablePage.getTheTooltipOfScheduleSummaryHoursByIndex(0);

            //If budget enabled, budget hrs will display in tooltip, otherwise guidance hrs will display
            if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                SimpleUtils.assertOnFail("The Budget hrs should display on tooltip, but the actual is: "+ toolTipInWeekView,
                        toolTipInWeekView.contains("Budget Hrs"), false);
                SimpleUtils.assertOnFail("'Hours Scheduled vs Budget' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Budget"), false);
            } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                SimpleUtils.assertOnFail("The Guidance hrs should display on tooltip, but the actual is: "+ toolTipInWeekView,
                        toolTipInWeekView.contains("Guidance Hrs"), false);
                SimpleUtils.assertOnFail("'Hours Scheduled vs Guidance' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Guidance"), false);
            } else
                SimpleUtils.fail("The Apply Labor Budget status is incorrectly, the actual is: " + isBudgetEnabled, false);


            scheduleCommonPage.clickOnDayView();
            //Mouse hover the scheduled hour in day view, check the label in the pop up panel
            String toolTipInDayView = scheduleShiftTablePage.getTheTooltipOfScheduleSummaryHoursInDayViewByIndex(0);
            toolTipOfClockImg = scheduleShiftTablePage.getTheTooltipOfClockImgsByIndex(1);
            if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                SimpleUtils.assertOnFail("The Budget hrs should display on tooltip, but the actual is: "+ toolTipInDayView,
                        toolTipInDayView.contains("Budget Hrs"), false);
                SimpleUtils.assertOnFail("'Hours Scheduled vs Budget' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Budget"), false);
            } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                SimpleUtils.assertOnFail("The Guidance hrs should display on tooltip, but the actual is: "+ toolTipInDayView,
                        toolTipInDayView.contains("Guidance Hrs"), false);
                SimpleUtils.assertOnFail("'Hours Scheduled vs Guidance' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Guidance"), false);
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the content in the pop up when mouse hovering the clock icon")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheContentInThePopupWhenMouseHoveringTheClockIconAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            String isBudgetEnabled = "";
            //Check the budget is enabled or not
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToGlobalConfigurationInLocations();
                Thread.sleep(10000);
                isBudgetEnabled = controlsNewUIPage.getApplyLaborBudgetToSchedulesActiveBtnLabel();
                switchToConsoleWindow();
            refreshCachesAfterChangeTemplate();

            //Create the schedule if it is not created
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

            //Mouse hover the first clock icon in the left in week view
            String toolTipOfClockImg = scheduleShiftTablePage.getTheTooltipOfClockImgsByIndex(0);

            //If budget enabled, budget hrs will display in tooltip, otherwise guidance hrs will display
            if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Budget' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Budget") &&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Guidance' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Guidance")&&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            } else
                SimpleUtils.fail("The Apply Labor Budget status is incorrectly, the actual is: " + isBudgetEnabled, false);

            //Mouse hover the second clock icon in the left in week view
            toolTipOfClockImg = scheduleShiftTablePage.getTheTooltipOfClockImgsByIndex(1);

            //If budget enabled, budget hrs will display in tooltip, otherwise guidance hrs will display
            if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Budget' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Budget") &&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Guidance' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Guidance")&&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            } else
                SimpleUtils.fail("The Apply Labor Budget status is incorrectly, the actual is: " + isBudgetEnabled, false);


            scheduleCommonPage.clickOnDayView();
            //Mouse hover the first clock icon in the left in day view
            toolTipOfClockImg = scheduleShiftTablePage.getTheTooltipOfClockImgsByIndex(0);
            if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Budget' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Budget")&&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Guidance' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Guidance")&&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            }
            //Mouse hover the second clock icon in the left in day view
            toolTipOfClockImg = scheduleShiftTablePage.getTheTooltipOfClockImgsByIndex(1);
            if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Budget' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Budget")&&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                SimpleUtils.assertOnFail("'Hours Scheduled vs Guidance' should display in the tooltip of clock img, the actual is: " + toolTipOfClockImg,
                        toolTipOfClockImg.contains("Hours Scheduled vs Guidance")&&
                                toolTipOfClockImg.contains("Over/Under within 25%") &&
                                toolTipOfClockImg.contains("Over/Under more than 25%"), false);
            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of coverage insights in week view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionalityOfCoverageInsightsInWeekViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
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

            //Create the schedule if it is not created
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            ForecastPage forecastPage = pageFactory.createForecastPage();
            forecastPage.clickOnLabor();
            List<String> hoursInLaborForecastChart = new ArrayList<>();
            hoursInLaborForecastChart = (List<String>) forecastPage.getLaborChartCoordinateAxisData().get("hours");
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            //Mouse hovering the scheduled hour on each day, check the content on the pop up panel
            for (int i=0; i<7;i++) {
                String toolTipInWeekView = scheduleShiftTablePage.getTheTooltipOfScheduleSummaryHoursByIndex(i).replace("\n", " ");
                String budgetOrGuidanceHrs = hoursInLaborForecastChart.get(i);
                float scheduledHour = scheduleShiftTablePage.calcTotalScheduledHourForOneDayInWeekView(i);
                String weekDayText = scheduleShiftTablePage.getWeekDayTextByIndex(i);
                DecimalFormat fnum = new DecimalFormat("#.#");
                float differentHours =Float.parseFloat(budgetOrGuidanceHrs) - scheduledHour;

                //If budget enabled, budget hrs will display in tooltip, otherwise guidance hrs will display
                if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                    SimpleUtils.assertOnFail("The Budget hrs should display on tooltip, but the actual is: "+ toolTipInWeekView,
                            toolTipInWeekView.contains(weekDayText)
                                    && toolTipInWeekView.contains(budgetOrGuidanceHrs + " Budget Hrs")
                                    && toolTipInWeekView.contains(fnum.format(scheduledHour)+" Scheduled Hrs")
                                    && toolTipInWeekView.contains( fnum.format((differentHours>=0.0?differentHours:-differentHours)) + " Difference Hrs"), false);

                } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                    SimpleUtils.assertOnFail("The Guidance hrs should display on tooltip, but the actual is: "+ toolTipInWeekView,
                            toolTipInWeekView.contains(weekDayText)
                                    && toolTipInWeekView.contains(budgetOrGuidanceHrs + " Guidance Hrs")
                                    && toolTipInWeekView.contains(fnum.format(scheduledHour)+" Scheduled Hrs")
                                    && toolTipInWeekView.contains( fnum.format((differentHours>=0.0?differentHours:-differentHours)) + " Difference Hrs"), false);
                } else
                    SimpleUtils.fail("The Apply Labor Budget status is incorrectly, the actual is: " + isBudgetEnabled, false);

            }

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the functionality of coverage insights in day view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheFunctionalityOfCoverageInsightsInDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            String isBudgetEnabled = "";
            //Check the budget is enabled or not
                LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
                SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
                locationsPage.clickOnLocationsTab();
                locationsPage.goToGlobalConfigurationInLocations();
                Thread.sleep(10000);
                isBudgetEnabled = controlsNewUIPage.getApplyLaborBudgetToSchedulesActiveBtnLabel();
                switchToConsoleWindow();
            refreshCachesAfterChangeTemplate();

            //Create the schedule if it is not created
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            ForecastPage forecastPage = pageFactory.createForecastPage();
            forecastPage.clickOnLabor();
            forecastPage.clickOnDayView();
            scheduleCommonPage.navigateDayViewWithIndex(0);
            List<String> hoursInLaborForecastChart = new ArrayList<>();
            hoursInLaborForecastChart = (List<String>) forecastPage.getLaborChartCoordinateAxisData().get("hours");
            List<String> timesInLaborForecastChart = (List<String>) forecastPage.getLaborChartCoordinateAxisData().get("dateOrTime");
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",
                    scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (!isWeekGenerated) {
                createSchedulePage.createScheduleForNonDGFlowNewUI();
            }

            //Get the index of the first grid that has budget hrs
            ArrayList<String> timeDurations = scheduleShiftTablePage.getScheduleDayViewGridTimeDuration();
            int index = -1;
            for (int i=0; i<timeDurations.size(); i++) {
                if (timeDurations.get(i).replace(" ", "").
                        toLowerCase().equalsIgnoreCase(timesInLaborForecastChart.get(0))) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                SimpleUtils.fail("Fail to found the grid that has budget hrs in schedule table day view! ", false);
            }
            //Mouse hovering the check/arrow marks on the first hour and verify the content on the pop up panel
            String toolTipInDayView = scheduleShiftTablePage.getTheTooltipOfScheduleSummaryHoursInDayViewByIndex(index).replace("\n", " ");
            String budgetOrGuidanceHrs = hoursInLaborForecastChart.get(0);
            float scheduledHour = 0;
            for (int i=0; i< scheduleShiftTablePage.getAvailableShiftsInDayView().size(); i++) {
                String shiftTime = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(i).get(2);
                if (shiftTime.replace(":00 ", "").toLowerCase().contains(timesInLaborForecastChart.get(0).toLowerCase())) {
                    scheduledHour += 1;
                } else if (shiftTime.replace(":30 ", "").toLowerCase().contains(timesInLaborForecastChart.get(0).toLowerCase())){
                    scheduledHour += 0.5;
                }
            }

            DecimalFormat fnum = new DecimalFormat("#.#");
            float differentHours =Float.parseFloat(budgetOrGuidanceHrs) - scheduledHour;

            //If budget enabled, budget hrs will display in tooltip, otherwise guidance hrs will display
            if (isBudgetEnabled.equalsIgnoreCase("yes")) {
                SimpleUtils.assertOnFail("The Budget hrs should display on tooltip, but the actual is: "+ toolTipInDayView,
                        toolTipInDayView.contains(timeDurations.get(index))
                                && toolTipInDayView.contains(budgetOrGuidanceHrs + " Budget Hrs")
                                && toolTipInDayView.contains(fnum.format(scheduledHour)+" Scheduled Hrs")
                                && toolTipInDayView.contains( fnum.format((differentHours>=0.0?differentHours:-differentHours)) + " Difference Hrs"), false);

            } else if (isBudgetEnabled.equalsIgnoreCase("no")) {
                SimpleUtils.assertOnFail("The Guidance hrs should display on tooltip, but the actual is: "+ toolTipInDayView,
                        toolTipInDayView.contains(timeDurations.get(index))
                                && toolTipInDayView.contains(budgetOrGuidanceHrs + " Guidance Hrs")
                                && toolTipInDayView.contains(fnum.format(scheduledHour)+" Scheduled Hrs")
                                && toolTipInDayView.contains( fnum.format((differentHours>=0.0?differentHours:-differentHours)) + " Difference Hrs"), false);
            } else
                SimpleUtils.fail("The Apply Labor Budget status is incorrectly, the actual is: " + isBudgetEnabled, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the up and down arrows in week view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheUpAndDownArrowsInWeekViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();

            //Create the schedule if it is not created
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
            createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "08:00pm");
            Thread.sleep(3000);
            HashMap<String, String> scheduleHrs = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(0);
            float differenceHrs = Float.parseFloat(scheduleHrs.get("differenceHrs"));
            boolean hasArrowImgBeforeChange = differenceHrs != 0;
            int arrowImgsBeforeChange = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().size();
            SimpleUtils.pass("Get hrs of arrow img successfully");
            // Edit the schedule, increase the shift duration, let the coverage < 25%
            calculateShiftTimeAndChangeIt(scheduleHrs, "lessThan25");

            //Verify the up arrow in yellow will show in edit mode when the coverage less than 25%
            List<String> arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
            if (hasArrowImgBeforeChange) {
                SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                        "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                        arrowImgsBeforeChange == arrowImgs.size(), false);
            } else
                SimpleUtils.assertOnFail("The arrow img quantity before and after the change should not be the same! " +
                                "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                        arrowImgsBeforeChange+2 == arrowImgs.size(), false);
            SimpleUtils.assertOnFail("The arrow img should be yellow and up, actual is: "+arrowImgs.get(0),
                    arrowImgs.get(0).equalsIgnoreCase("yellow up"), false);

            //Verify the up arrow in yellow will show in save mode when the coverage less than 25%
            scheduleMainPage.saveSchedule();
            arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
            if (hasArrowImgBeforeChange) {
                SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                                "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                        arrowImgsBeforeChange == arrowImgs.size(), false);
            } else
                SimpleUtils.assertOnFail("The arrow img quantity before and after the change should not be the same! " +
                                "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                        arrowImgsBeforeChange+2 == arrowImgs.size(), false);
            SimpleUtils.assertOnFail("The arrow img should be yellow and up, actual is: "+arrowImgs.get(0),
                    arrowImgs.get(0).equalsIgnoreCase("yellow up"), false);
            arrowImgsBeforeChange = arrowImgs.size();

            scheduleHrs = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(0);
            //Edit the schedule, increase the shift duration, let the coverage == 25%
            calculateShiftTimeAndChangeIt(scheduleHrs, "equalTo25");
            //Verify the up arrow in yellow will show in edit mode when the coverage equals to 25%
            arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
                SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                                "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                        arrowImgsBeforeChange == arrowImgs.size(), false);
            SimpleUtils.assertOnFail("The arrow img should be yellow and up, actual is: "+arrowImgs.get(0),
                    arrowImgs.get(0).equalsIgnoreCase("yellow up"), false);
        arrowImgsBeforeChange = arrowImgs.size();
            //Verify the up arrow in yellow will show in save mode when the coverage equals to 25%
            scheduleMainPage.saveSchedule();
            arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
                SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                                "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                        arrowImgsBeforeChange == arrowImgs.size(), false);
            SimpleUtils.assertOnFail("The arrow img should be yellow and up, actual is: "+arrowImgs.get(0),
                    arrowImgs.get(0).equalsIgnoreCase("yellow up"), false);

        arrowImgsBeforeChange = arrowImgs.size();
        scheduleHrs = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(0);
        //Edit the schedule, increase the shift duration, let the coverage > 25%
        calculateShiftTimeAndChangeIt(scheduleHrs, "moreThan25");
        //Verify the up arrow in red will show in edit mode when the coverage more than 25%
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
            SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                            "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                    arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be red and up, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("red up"), false);
        arrowImgsBeforeChange = arrowImgs.size();
        //Verify the up arrow in red will show in save mode when the coverage more than 25%
        scheduleMainPage.saveSchedule();
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
            SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                            "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                    arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be red and up, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("red up"), false);


        isWeekGenerated = createSchedulePage.isWeekGenerated();
        if (isWeekGenerated) {
            createSchedulePage.unGenerateActiveScheduleScheduleWeek();
        }
        createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("8:00am", "8:00pm");
        Thread.sleep(3000);
        arrowImgsBeforeChange = arrowImgs.size();
        scheduleHrs = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(0);
        //Edit the schedule, decrease the shift duration, let the coverage < 25%
        calculateShiftTimeAndDecreaseIt(scheduleHrs, "lessThan25");
        //Verify the down arrow in yellow will show in edit mode when the coverage less than 25%
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
        SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                        "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be yellow and down, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("yellow down"), false);
        arrowImgsBeforeChange = arrowImgs.size();
        //Verify the down arrow in yellow will show in save mode when the coverage less than 25%
        scheduleMainPage.saveSchedule();
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
        SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                        "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be yellow and down, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("yellow down"), false);

        arrowImgsBeforeChange = arrowImgs.size();
        scheduleHrs = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(0);
        //Edit the schedule, decrease the shift duration, let the coverage = 25%
        calculateShiftTimeAndDecreaseIt(scheduleHrs, "equalTo25");
        //Verify the down arrow in yellow will show in edit mode when the coverage equals to 25%
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
        SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                        "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be yellow and down, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("yellow down"), false);
        arrowImgsBeforeChange = arrowImgs.size();
        //Verify the down arrow in yellow will show in save mode when the coverage equals to 25%
        scheduleMainPage.saveSchedule();
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
        SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                        "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be yellow and down, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("yellow down"), false);

        arrowImgsBeforeChange = arrowImgs.size();
        scheduleHrs = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(0);
        //Edit the schedule, decrease the shift duration, let the coverage > 25%
        calculateShiftTimeAndDecreaseIt(scheduleHrs, "moreThan25");
        //Verify the down arrow in red will show in edit mode when the coverage more than 25%
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
        SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                        "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be red and down, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("red down"), false);
        arrowImgsBeforeChange = arrowImgs.size();
        //Verify the down arrow in red will show in save mode when the coverage more than 25%
            Thread.sleep(3000);
            scheduleMainPage.saveSchedule();
        arrowImgs = scheduleShiftTablePage.getAllDifferenceHrsArrowImg();
        SimpleUtils.assertOnFail("The arrow img quantity before and after the change should be the same! " +
                        "Before is: "+ arrowImgsBeforeChange + " After is: " +arrowImgs.size(),
                arrowImgsBeforeChange == arrowImgs.size(), false);
        SimpleUtils.assertOnFail("The arrow img should be red and down, actual is: "+arrowImgs.get(0),
                arrowImgs.get(0).equalsIgnoreCase("red down"), false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    private void calculateShiftTimeAndChangeIt(HashMap<String, String> scheduleHrs, String coverageStatus) throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        System.out.println("The budget hrs is:"+scheduleHrs.get("budgetHrs"));
        System.out.println("The different hrs is:"+ scheduleHrs.get("differenceHrs"));
        float budgetOrGuidanceHrs = Float.parseFloat(scheduleHrs.get("budgetHrs"));
        float differenceHrs = Float.parseFloat(scheduleHrs.get("differenceHrs"));
        float coverage = budgetOrGuidanceHrs/4;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        //Get the coverage shift time
        float increaseShiftDuration = 0;
        if (coverageStatus.contains("lessThan")) {
            increaseShiftDuration = (coverage-differenceHrs) - 1;
        } else if (coverageStatus.contains("moreThan")) {
            increaseShiftDuration = (coverage-differenceHrs) + 1;
        } else
            increaseShiftDuration = (coverage-differenceHrs);

        int increaseHrs = Integer.parseInt(decimalFormat.format(increaseShiftDuration).split("\\.")[0]);
        int increaseMins = Integer.parseInt(decimalFormat.format(increaseShiftDuration).split("\\.")[1]);
        switch (increaseMins) {
            case 0:
                increaseMins = 0;
                break;
            case 25:
                increaseMins = 15;
                break;
            case 50:
                increaseMins = 30;
                break;
            case 75:
                increaseMins = 45;
                break;
        }
        String shiftTime = scheduleShiftTablePage.getTheShiftInfoByIndex(0).get(2);
        String shiftStartTime = shiftTime.split("-")[0];
        String shiftEndTime = shiftTime.split("-")[1].replace("am","").replace("pm","");
        int shiftEndTimeHour = Integer.parseInt(shiftEndTime.split(":")[0])+ increaseHrs;
        int shiftEndTimeMins = Integer.parseInt(shiftEndTime.split(":")[1])+ increaseMins;
        if (shiftEndTimeMins>=60) {
            shiftEndTimeMins = shiftEndTimeMins- 60;
            shiftEndTimeHour += 1;
        }
        boolean needChangeAMOrPM = false;
        if (shiftEndTimeHour > 12) {
            shiftEndTimeHour = shiftEndTimeHour-12;
            needChangeAMOrPM = true;
        } else if (shiftEndTimeHour == 12) {
            needChangeAMOrPM = true;
        }
        String amOrPM = "am";
        if (needChangeAMOrPM) {
            if (shiftTime.split("-")[1].contains("am")){
                amOrPM = "pm";
            }
        } else {
            if (shiftTime.split("-")[1].contains("pm")) {
                amOrPM = "pm";
            }
        }
        shiftEndTime = shiftEndTimeHour+":" + shiftEndTimeMins+ amOrPM;
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        shiftOperatePage.editTheShiftTimeForSpecificShift(scheduleShiftTablePage.getTheShiftByIndex(0), shiftStartTime, shiftEndTime);
    }


    private void calculateShiftTimeAndDecreaseIt(HashMap<String, String> scheduleHrs, String coverageStatus) throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        float budgetOrGuidanceHrs = Float.parseFloat(scheduleHrs.get("budgetHrs"));
        float differenceHrs = Float.parseFloat(scheduleHrs.get("differenceHrs"));
        float coverage = budgetOrGuidanceHrs/4;
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        //Get the coverage shift time
        float decreaseShiftDuration = 0;
        if (coverageStatus.contains("lessThan")) {
            decreaseShiftDuration = (coverage+differenceHrs) - 1;
        } else if (coverageStatus.contains("moreThan")) {
            decreaseShiftDuration = (coverage-differenceHrs) + 1;
        } else
            decreaseShiftDuration = (coverage-differenceHrs);

        int decreaseHrs = Integer.parseInt(decimalFormat.format(decreaseShiftDuration).split("\\.")[0]);
        int decreaseMins = Integer.parseInt(decimalFormat.format(decreaseShiftDuration).split("\\.")[1]);
        switch (decreaseMins) {
            case 0:
                decreaseMins = 0;
                break;
            case 25:
                decreaseMins = 15;
                break;
            case 50:
                decreaseMins = 30;
                break;
            case 75:
                decreaseMins = 45;
                break;
        }
        int index = 0;
        scheduleCommonPage.clickOnDayView();
        scheduleCommonPage.navigateDayViewWithIndex(0);
        int shiftCountOfFirstDay = scheduleShiftTablePage.getShiftsCount();
        float shiftHrs = Float.parseFloat(scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index).get(8).trim().split(" ")[0]);
        while (index < shiftCountOfFirstDay && shiftHrs <= decreaseShiftDuration) {
            index ++;
            shiftHrs = Float.parseFloat(scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index).get(8).trim().split(" ")[0]);
        }
        List<String> shiftInfo = scheduleShiftTablePage.getTheShiftInfoInDayViewByIndex(index);
        scheduleCommonPage.clickOnWeekView();
        String shiftTime = shiftInfo.get(2);
        String shiftStartTime = shiftTime.split("-")[0];
        String shiftEndTime = shiftTime.split("-")[1].replace("am","").replace("pm","").replace(" ","");
        int shiftEndTimeHour = Integer.parseInt(shiftEndTime.split(":")[0])- decreaseHrs;
        int shiftEndTimeMins = Integer.parseInt(shiftEndTime.split(":")[1])- decreaseMins;
        if (shiftEndTimeMins< 0) {
            shiftEndTimeMins = shiftEndTimeMins+ 60;
            shiftEndTimeHour = shiftEndTimeHour - 1;
        }
        boolean needChangeAMOrPM = false;
        if (shiftEndTimeHour < 0) {
            shiftEndTimeHour = shiftEndTimeHour+12;
            needChangeAMOrPM = true;
        } else if (shiftEndTimeHour == 0) {
            shiftEndTimeHour = shiftEndTimeHour+12;
        }else if (shiftEndTimeHour == 12 || (shiftEndTime.contains("12") && shiftEndTimeHour!=12)) {
            needChangeAMOrPM = true;
        }
        String amOrPM = "am";
        if (needChangeAMOrPM) {
            if (shiftTime.split("-")[1].contains("am")){
                amOrPM = "pm";
            }
        } else {
            if (shiftTime.split("-")[1].contains("pm")) {
                amOrPM = "pm";
            }
        }
        shiftEndTime = shiftEndTimeHour+":" + shiftEndTimeMins+ amOrPM;
        ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
        ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
        shiftOperatePage.editTheShiftTimeForSpecificShift(scheduleShiftTablePage.getShiftById(shiftInfo.get(9)),
                shiftStartTime, shiftEndTime);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the up/down arrows/check mark in day view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateUpAndDownArrowsCheckMarkInDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
                ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
                ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
                CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
                ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
                ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
                ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();
                NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
                //Create the schedule if it is not created
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
                createSchedulePage.createScheduleForNonDGFlowNewUIWithGivingTimeRange("08:00am", "08:00pm");
                String workRole = shiftOperatePage.getRandomWorkRole();
                //go to day view
                scheduleCommonPage.clickOnDayView();
                //Go to forecast page and get the first labor time
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
                ForecastPage forecastPage = pageFactory.createForecastPage();
                forecastPage.clickOnLabor();
                List<String> timesInLaborForecastChart = (List<String>) forecastPage.getLaborChartCoordinateAxisData().get("dateOrTime");
                String firstTime = timesInLaborForecastChart.get(0);
                scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());

                //Get the index of the first grid that has budget hrs
                ArrayList<String> timeDurations = scheduleShiftTablePage.getScheduleDayViewGridTimeDuration();
                int index = -1;
                for (int i=0; i<timeDurations.size(); i++) {
                    if (timeDurations.get(i).replace(" ", "").
                            toLowerCase().equalsIgnoreCase(firstTime)) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) {
                    SimpleUtils.fail("Fail to found the grid that has budget hrs in schedule table day view! ", false);
                }

                //Edit the schedule, let the scheduled Hrs equals to Guidance/Budget Hrs
                HashMap<String, String> toolTipInDayView = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(index);
                if (!toolTipInDayView.get("differenceArrow").equals("")) {
                    int differenceHrs = Integer.parseInt(toolTipInDayView.get("differenceHrs"));
                    if (toolTipInDayView.get("differenceArrow").contains("up")) {
                        //if schedule hrs is less than budget/guidance hrs, then create new shift
                        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                        for (int i=0;i<differenceHrs;i++) {

                            newShiftPage.clickOnDayViewAddNewShiftButton();
                            newShiftPage.customizeNewShiftPage();
                            newShiftPage.clearAllSelectedDays();
                            newShiftPage.selectDaysByIndex(2,2,2);
                            newShiftPage.selectWorkRole(workRole);
                            newShiftPage.moveSliderAtCertainPoint("8pm", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
                            newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
                            newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
                            newShiftPage.clickOnCreateOrNextBtn();
                        }
                    } else {
                        //if schedule hrs is more than budget/guidance hrs, then delete the shift
                        scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                        for (int i=0;i<differenceHrs;i++) {
                            scheduleShiftTablePage.clickOnShiftInDayView(scheduleShiftTablePage.
                                    getAvailableShiftsInDayView().get(i));
                            scheduleShiftTablePage.clickOnXButtonInDayView();
                        }
                        scheduleMainPage.saveSchedule();
                    }
                }

                //Check mark in green shows
                String img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be green, actual is: "+img,
                        img.contains("green"), false);

                //Get the 25% coverage
                int budgetOrGuidanceHrs = Integer.parseInt(toolTipInDayView.get("budgetHrs"));
                float coverage = budgetOrGuidanceHrs/4;

                String shiftId = scheduleShiftTablePage.getTheShiftByIndex(0).getAttribute("id");
                //Edit the schedule, increase the shift duration, let the coverage < 25%
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                switch (budgetOrGuidanceHrs) {
                    case 3:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:30am", "10:30am");
                        break;
                    case 2:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:15am", "10:15am");
                        break;
                    case 1:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:15am", "10:15am");
                        break;
                    default:
                        SimpleUtils.fail("Please add the coverage = "+coverage +" case! ", false);
                }
                //The down arrow in yellow shows in edit mode
                Thread.sleep(10000);
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow down, actual is: "+img,
                        img.contains("yellow down"), false);
                scheduleMainPage.saveSchedule();
                //The down arrow in yellow shows in save mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow down, actual is: "+img,
                        img.contains("yellow down"), false);

                //Edit the schedule, increase the shift duration, let the coverage = 25%
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                switch (budgetOrGuidanceHrs) {
                    case 3:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:45am", "10:45am");
                        break;
                    case 2:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:30am", "10:30am");
                        break;
                    case 1:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:15am", "10:15am");
                        break;
                    default:
                        SimpleUtils.fail("Please add the coverage = "+coverage +" case! ", false);
                }
                //The down arrow in yellow shows in edit mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow down, actual is: "+img,
                        img.contains("yellow down"), false);
                scheduleMainPage.saveSchedule();
                //The down arrow in yellow shows in save mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow down, actual is: "+img,
                        img.contains("yellow down"), false);


                //Edit the schedule, decrease the shift duration, let the coverage > 25%
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                switch (budgetOrGuidanceHrs) {
                    case 3:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"09:00am", "11:00am");
                        break;
                    case 2:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"09:00am", "11:00am");
                        break;
                    case 1:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"09:00am", "11:00am");
                        break;
                    default:
                        SimpleUtils.fail("Please add the coverage = "+coverage +" case! ", false);
                }
                //The down arrow in red shows in edit mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be red down, actual is: "+img,
                        img.contains("red down"), false);
                scheduleMainPage.saveSchedule();
                //The down arrow in red shows in save mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be red down, actual is: "+img,
                        img.contains("red down"), false);


                //Move the shift back
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                shiftOperatePage.editTheShiftTimeForSpecificShift(
                        scheduleShiftTablePage.getShiftById(shiftId),"08:00am", "10:00am");
                scheduleMainPage.saveSchedule();
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be green, actual is: "+img,
                        img.contains("green"), false);

                //Edit the schedule, decrease the shift duration, let the coverage < 25%
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                newShiftPage.clickOnDayViewAddNewShiftButton();
                newShiftPage.customizeNewShiftPage();
                newShiftPage.selectWorkRole(workRole);
                newShiftPage.moveSliderAtCertainPoint("10am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
                newShiftPage.moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
                newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
                newShiftPage.clickOnCreateOrNextBtn();
                //The up arrow in red shows in edit mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be red up, actual is: "+img,
                        img.contains("red up"), false);
                scheduleMainPage.saveSchedule();
                //The up arrow in red shows in save mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be red up, actual is: "+img,
                        img.contains("red up"), false);

                shiftId = scheduleShiftTablePage.getTheShiftByIndex(0).getAttribute("id");

                //Edit the schedule, increase the shift duration, let the coverage < 25%
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                switch (budgetOrGuidanceHrs) {
                    case 3:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:30am", "10:30am");
                        break;
                    case 2:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:45am", "10:45am");
                        break;
                    case 1:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:45am", "10:45am");
                        break;
                    default:
                        SimpleUtils.fail("Please add the coverage = "+coverage +" case! ", false);
                }
                //The up arrow in yellow shows in edit mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow up, actual is: "+img,
                        img.contains("yellow up"), false);
                scheduleMainPage.saveSchedule();
                //The up arrow in yellow shows in save mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow up, actual is: "+img,
                        img.contains("yellow up"), false);

                //Edit the schedule, increase the shift duration, let the coverage = 25%
                scheduleMainPage.clickOnEditButtonNoMaterScheduleFinalizedOrNot();
                switch (budgetOrGuidanceHrs) {
                    case 3:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:15am", "10:15am");
                        break;
                    case 2:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:30am", "10:30am");
                        break;
                    case 1:
                        shiftOperatePage.editTheShiftTimeForSpecificShift(
                                scheduleShiftTablePage.getShiftById(shiftId),"08:45am", "10:45am");
                        break;
                    default:
                        SimpleUtils.fail("Please add the coverage = "+coverage +" case! ", false);
                }
                //The up arrow in yellow shows in edit mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow up, actual is: "+img,
                        img.contains("yellow up"), false);
                scheduleMainPage.saveSchedule();
                //The up arrow in yellow shows in save mode
                img = scheduleShiftTablePage.getAllDifferenceHrsArrowImg().get(index);
                SimpleUtils.assertOnFail("The img should be yellow up, actual is: "+img,
                        img.contains("yellow up"), false);


        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Mary")
    @Enterprise(name = "Vailqacn_Enterprise")
//    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "Verify the Guidance/Budget Hrs when filtering in day view")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTheGuidanceBudgetHrsWhenFilteringInDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
            ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
            CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
            ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
            ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
            ShiftOperatePage shiftOperatePage = pageFactory.createShiftOperatePage();

            //Create the schedule if it is not created
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
            scheduleCommonPage.clickOnWeekView();
            String workRole = shiftOperatePage.getRandomWorkRole();
            //Click on Filter, select one work role
            scheduleCommonPage.clickOnDayView();
            scheduleMainPage.clickOnFilterBtn();
            scheduleMainPage.selectWorkRoleFilterByText(workRole, true);

            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
            ForecastPage forecastPage = pageFactory.createForecastPage();
            forecastPage.clickOnLabor();
            forecastPage.clickOnDayView();
            forecastPage.selectWorkRoleFilterByText(workRole);
            List<String> hoursInLaborForecastChart = (List<String>) forecastPage.getLaborChartCoordinateAxisData().get("hours");
            //Get budget hrs on labor forecast
            float budgetHrsOnLaborForecast = 0;
            for (int i=0; i< hoursInLaborForecastChart.size(); i++) {
                budgetHrsOnLaborForecast += Float.parseFloat(hoursInLaborForecastChart.get(i));
            }
            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
            //Get budget and scheduled hrs on schedule page
            float budgetHrsOnSchedulePage = 0;
            float scheduledHrsOnSchedulePage = 0;
            for (int i=0; i< scheduleShiftTablePage.getScheduleDayViewGridTimeDuration().size();i++) {
                HashMap<String, String> scheduleHrs = scheduleShiftTablePage.getHrsOnTooltipOfScheduleSummaryHoursByIndex(i);
                budgetHrsOnSchedulePage += Float.parseFloat(scheduleHrs.get("budgetHrs"));
                scheduledHrsOnSchedulePage += Float.parseFloat(scheduleHrs.get("scheduledHrs"));
                // Difference Hrs should be equal to Absolute value of [Scheduled - Guidance/Budgeted]
                SimpleUtils.assertOnFail("Difference Hrs should be equal to Absolute value of [Scheduled - Guidance/Budgeted], " +
                        "but actual different hrs is:" + scheduleHrs.get("differenceHrs")
                        + " scheduled hrs is: "+ scheduleHrs.get("scheduledHrs")
                        + " budget hrs is: " + scheduleHrs.get("budgetHrs"), Float.parseFloat(scheduleHrs.get("differenceHrs"))
                        ==(Math.abs(Float.parseFloat(scheduleHrs.get("budgetHrs")) - Float.parseFloat(scheduleHrs.get("scheduledHrs")))), false);
            }

            //Guidance/Budgeted Hrs should be consistent with labor forecast for the selected work role
            SimpleUtils.assertOnFail("The budget hrs should be consistently on labor forecast and schedule page! but actual budget hrs on labor forecast is: "
                    + budgetHrsOnLaborForecast+ " on schedule page is: "+ budgetHrsOnSchedulePage,
                    budgetHrsOnLaborForecast == budgetHrsOnSchedulePage, false);

            //Get all shift hrs on current day in day view
            float shiftHrs = scheduleShiftTablePage.getActiveShiftHoursInDayView();

            //Scheduled Hrs for the selected work role display correctly
            SimpleUtils.assertOnFail("Scheduled Hrs for the selected work role display incorrectly！ expected is: "
                            + shiftHrs + " actual is: "+ scheduledHrsOnSchedulePage,
                    shiftHrs == scheduledHrsOnSchedulePage, false);

        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(),false);
        }
    }
}
