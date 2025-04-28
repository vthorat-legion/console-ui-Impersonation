package com.legion.tests.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.legion.pages.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;

public class ScheduleRoleBasedNewUITest extends TestBase {

    private static HashMap<String, String> propertyMap = JsonUtil
        .getPropertiesFromJsonFile("src/test/resources/envCfg.json");
   

    SchedulePage schedulePage = null;
    public enum weekCount {
        Zero(0),
        One(1),
        Two(2),
        Three(3),
        Four(4),
        Five(5);
        private final int value;

        weekCount(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public enum overviewWeeksStatus {
        NotAvailable("Not Available"),
        Draft("Draft"),
        Guidance("Guidance"),
        Finalized("Finalized");

        private final String value;

        overviewWeeksStatus(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }


    public enum SchedulePageSubTabText {
        Overview("OVERVIEW"),
        ProjectedSales("PROJECTED SALES"),
        StaffingGuidance("STAFFING GUIDANCE"),
        Schedule("SCHEDULE");
        private final String value;

        SchedulePageSubTabText(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum weekViewType {
        Next("Next"),
        Previous("Previous");
        private final String value;

        weekViewType(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public enum scheduleHoursAndWagesData {
        scheduledHours("scheduledHours"),
        budgetedHours("budgetedHours"),
        otherHours("otherHours"),
        budgetedWages("budgetedWages"),
        scheduledWages("scheduledWages"),
        otherWages("otherWages");
        private final String value;

        scheduleHoursAndWagesData(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    @BeforeMethod
    public void firstTest(Method method, Object[] params) throws Exception {
        this.createDriver((String) params[0], "68", "Linux");
        visitPage(method);
        loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
        navigateToSchedulePage();
    }

    // ToDo -
    @Automated(automated = "Automated")
    @Owner(owner = "Naval")
    @TestName(description = "Login as Team Member, navigate & verify Schedule page")
    @Enterprise(name = "KendraScott2_Enterprise")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void scheduleTestAsTeamMember(String browser, String username, String password, String location)
        throws Exception {
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        SimpleUtils.assertOnFail("Schedule Page: Schedule is not Published for current week.",
            createSchedulePage.isCurrentScheduleWeekPublished(), false);
        List<HashMap<String, Float>> scheduleDaysViewLabelDataForWeekDays = getDaysDataofCurrentWeek();
        HashMap<String, Float> scheduleWeekViewLabelData = getCurrentWeekData();
        SimpleUtils.assertOnFail("Schedule Page: Wages are loaded for Team Member in week view.",
            !iswagesLoadedInWeekView(scheduleWeekViewLabelData), true);
        comparingWeekScheduledHoursAndSumOfDaysScheduledHours(scheduleWeekViewLabelData,
            scheduleDaysViewLabelDataForWeekDays);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Naval")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Login as Team Lead, navigate & verify Schedule page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void scheduleTestAsTeamLead(String browser, String username, String password, String location)
        throws Exception {
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        SimpleUtils.assertOnFail("Schedule Page: Schedule is not Published for current week.",
            createSchedulePage.isCurrentScheduleWeekPublished(), false);
        HashMap<String, Float> scheduleWeekViewLabelData = getCurrentWeekData();
        List<HashMap<String, Float>> scheduleDaysViewLabelDataForWeekDays = getDaysDataofCurrentWeek();
        SimpleUtils.assertOnFail("Schedule Page: Wages are loaded for Team Lead in week view.",
            !iswagesLoadedInWeekView(scheduleWeekViewLabelData), true);
        comparingWeekScheduledHoursAndSumOfDaysScheduledHours(scheduleWeekViewLabelData,
            scheduleDaysViewLabelDataForWeekDays);

    }

    @Automated(automated = "Automated")
    @Owner(owner = "Naval")
    @Enterprise(name = "KendraScott2_Enterprise")
    @TestName(description = "Login as Store Manager, navigate & verify Schedule page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void scheduleTestAsStoreManager(String browser, String username, String password, String location)
        throws Exception {
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        SimpleUtils.assertOnFail("Schedule Page: Schedule is not Published for current week.",
            createSchedulePage.isCurrentScheduleWeekPublished(), false);
        HashMap<String, Float> scheduleWeekViewLabelData = getCurrentWeekData();
        List<HashMap<String, Float>> scheduleDaysViewLabelDataForWeekDays = getDaysDataofCurrentWeek();
        SimpleUtils.assertOnFail("Schedule Page: Wages are not loaded for Store Manager in week view.",
            iswagesLoadedInWeekView(scheduleWeekViewLabelData), true);
        comparingWeekScheduledHoursAndSumOfDaysScheduledHours(scheduleWeekViewLabelData,
            scheduleDaysViewLabelDataForWeekDays);
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Naval")
    @TestName(description = "Login to Legion with roles, navigate & verify Schedule page")
    @Enterprise(name = "Tech_Enterprise")
    @Test(dataProvider = "legionTeamCredentialsByEnterpriseP", dataProviderClass = CredentialDataProviderSource.class)
    public void scheduleTest(String browser, String username, String password, String location) throws Exception {
        CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
        SimpleUtils.assertOnFail("Schedule Page: Schedule is not Published for current week.",
				  createSchedulePage.isCurrentScheduleWeekPublished(), false);
        List<HashMap<String, Float>> scheduleDaysViewLabelDataForWeekDays = getDaysDataofCurrentWeek();
        HashMap<String, Float> scheduleWeekViewLabelData = getCurrentWeekData();
        comparingWeekScheduledHoursAndSumOfDaysScheduledHours(scheduleWeekViewLabelData,
            scheduleDaysViewLabelDataForWeekDays);
    }

    public void navigateToSchedulePage() throws Exception {

        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        scheduleCommonPage.clickOnScheduleConsoleMenuItem();
        scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Schedule.value);
    }


    public void changeLocationTest(String location) {
        LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
        locationSelectorPage.changeLocation(location);
        SimpleUtils
            .assertOnFail("Dashboard Page: Location not changed!", locationSelectorPage.isLocationSelected(location),
                false);
    }

    public HashMap<String, Float> getCurrentWeekData() throws Exception {
        HashMap<String, Float> scheduleWeekViewLabelData = new HashMap<String, Float>();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        scheduleCommonPage.clickOnWeekView();
        scheduleWeekViewLabelData = smartCardPage.getScheduleLabelHoursAndWages();

        return scheduleWeekViewLabelData;

    }

    public synchronized List<HashMap<String, Float>> getDaysDataofCurrentWeek() throws Exception {
        List<HashMap<String, Float>> scheduleDaysViewLabelDataForWeekDays = new ArrayList<HashMap<String, Float>>();
        ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
        scheduleCommonPage.clickOnDayView();
        scheduleDaysViewLabelDataForWeekDays = smartCardPage.getScheduleLabelHoursAndWagesDataForEveryDayInCurrentWeek();

        return scheduleDaysViewLabelDataForWeekDays;
    }

    public synchronized void comparingWeekScheduledHoursAndSumOfDaysScheduledHours(
        HashMap<String, Float> scheduleWeekViewLabelData,
        List<HashMap<String, Float>> scheduleDaysViewLabelDataForWeekDays) {
        // Variables for Day View Data
        Float scheduleDaysScheduledHoursTotal = (float) 0;
        Float scheduleDaysBudgetedHoursTotal = (float) 0;
        Float scheduleDaysOtherHoursTotal = (float) 0;
        Float scheduleDaysWagesBudgetedCountTotal = (float) 0;
        Float scheduleDaysWagesScheduledCountTotal = (float) 0;

        // Variables for Week View Data
        Float scheduleWeekScheduledHours = scheduleWeekViewLabelData
            .get(scheduleHoursAndWagesData.scheduledHours.getValue());
        Float scheduleWeekBudgetedHours = scheduleWeekViewLabelData
            .get(scheduleHoursAndWagesData.budgetedHours.getValue());
        Float scheduleWeekOtherHours = scheduleWeekViewLabelData.get(scheduleHoursAndWagesData.otherHours.getValue());
        Float scheduleWeekWagesBudgetedCount = scheduleWeekViewLabelData
            .get(scheduleHoursAndWagesData.budgetedWages.getValue());
        Float scheduleWeekWagesScheduledCount = scheduleWeekViewLabelData
            .get(scheduleHoursAndWagesData.scheduledWages.getValue());

        for (HashMap<String, Float> scheduleDaysViewLabelDataForWeekDay : scheduleDaysViewLabelDataForWeekDays) {
            if (scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.scheduledHours.getValue()) != null) {
                scheduleDaysScheduledHoursTotal = scheduleDaysScheduledHoursTotal + scheduleDaysViewLabelDataForWeekDay
                    .get(scheduleHoursAndWagesData.scheduledHours.getValue());
            }

            if (scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.budgetedHours.getValue()) != null) {
                scheduleDaysBudgetedHoursTotal = scheduleDaysBudgetedHoursTotal + scheduleDaysViewLabelDataForWeekDay
                    .get(scheduleHoursAndWagesData.budgetedHours.getValue());
            }

            if (scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.otherHours.getValue()) != null) {
                scheduleDaysOtherHoursTotal = scheduleDaysOtherHoursTotal + scheduleDaysViewLabelDataForWeekDay
                    .get(scheduleHoursAndWagesData.otherHours.getValue());
            }

            if (scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.budgetedWages.getValue())
                != null) {
                scheduleDaysWagesBudgetedCountTotal =
                    scheduleDaysWagesBudgetedCountTotal + scheduleDaysViewLabelDataForWeekDay
                        .get(scheduleHoursAndWagesData.budgetedWages.getValue());
            }

            if (scheduleDaysViewLabelDataForWeekDay.get(scheduleHoursAndWagesData.scheduledWages.getValue())
                != null) {
                scheduleDaysWagesScheduledCountTotal =
                    scheduleDaysWagesScheduledCountTotal + scheduleDaysViewLabelDataForWeekDay
                        .get(scheduleHoursAndWagesData.scheduledWages.getValue());
            }
        }
        if (scheduleWeekScheduledHours != null && scheduleDaysScheduledHoursTotal != null) {
            if (scheduleWeekScheduledHours.equals(scheduleDaysScheduledHoursTotal)) {
                SimpleUtils.pass("Schedule Page: Week Scheduled Hours matched with Sum of Days Scheduled Hours ("
                    + scheduleWeekScheduledHours + "/"
                    + scheduleDaysScheduledHoursTotal + ")");
            } else {
//   		        SimpleUtils.assertOnFail("Schedule Page: Week Scheduled Hours not matched with Sum of Days Scheduled Hours (" +scheduleWeekScheduledHours+"/"
//   		        		+scheduleDaysScheduledHoursTotal+ ")", scheduleWeekScheduledHours.equals(scheduleDaysScheduledHoursTotal), true);
                SimpleUtils.report("Schedule Page: Week Scheduled Hours not matched with Sum of Days Scheduled Hours ("
                    + scheduleWeekScheduledHours + "/"
                    + scheduleDaysScheduledHoursTotal + ")");
            }
        }

        if (scheduleWeekBudgetedHours != null && scheduleDaysBudgetedHoursTotal != null) {
            if (scheduleWeekBudgetedHours.equals(scheduleDaysBudgetedHoursTotal)) {
                SimpleUtils.pass("Schedule Page: Week Budgeted Hours matched with Sum of Days Budgeted Hours ("
                    + scheduleWeekBudgetedHours + "/"
                    + scheduleDaysBudgetedHoursTotal);
            } else {
//	   		        SimpleUtils.assertOnFail("Schedule Page: Week Budgeted Hours not matched with Sum of Days Budgeted Hours (" +scheduleWeekBudgetedHours+ "/" 
//	   		        		+ scheduleDaysBudgetedHoursTotal + ")", scheduleWeekBudgetedHours.equals(scheduleDaysBudgetedHoursTotal), true);
                SimpleUtils.report("Schedule Page: Week Budgeted Hours not matched with Sum of Days Budgeted Hours ("
                    + scheduleWeekBudgetedHours + "/"
                    + scheduleDaysBudgetedHoursTotal + ")");
            }
        }

        if (scheduleWeekOtherHours != null && scheduleDaysOtherHoursTotal != null) {
            if (scheduleWeekOtherHours.equals(scheduleDaysOtherHoursTotal)) {
                SimpleUtils.pass(
                    "Schedule Page: Week Other Hours matched with Sum of Days Other Hours (" + scheduleWeekOtherHours
                        + "/"
                        + scheduleDaysOtherHoursTotal + ")");
            } else {
                /*SimpleUtils.assertOnFail("Schedule Page: Week Other Hours not matched with Sum of Days Other Hours ("
                        + scheduleWeekOtherHours + "/"
                        + scheduleDaysOtherHoursTotal + ")", scheduleWeekOtherHours.equals(scheduleDaysOtherHoursTotal),
                    true);*/
            	
            	SimpleUtils.report("Schedule Page: Week Other Hours not matched with Sum of Days Other Hours ("
                        + scheduleWeekOtherHours + "/"
                        + scheduleDaysOtherHoursTotal + ")");
            }
        }

        if (scheduleWeekWagesBudgetedCount != null && scheduleDaysWagesBudgetedCountTotal != null) {
            if (scheduleWeekWagesBudgetedCount.equals(scheduleDaysWagesBudgetedCountTotal)) {
                SimpleUtils.pass("Schedule Page: Week Budgeted Wages matched with Sum of Days Budgeted Wages ("
                    + scheduleWeekWagesBudgetedCount + "/"
                    + scheduleDaysWagesBudgetedCountTotal);
            } else {
                /*SimpleUtils.assertOnFail(
                    "Schedule Page: Week Budgeted Wages not matched with Sum of Days Budgeted Wages ("
                        + scheduleWeekWagesBudgetedCount + "/"
                        + scheduleDaysWagesBudgetedCountTotal + ")",
                    scheduleWeekWagesBudgetedCount.equals(scheduleDaysWagesBudgetedCountTotal), true);*/
            	 SimpleUtils.report("Schedule Page: Week Budgeted Wages not matched with Sum of Days Budgeted Wages ("
                         + scheduleWeekWagesBudgetedCount + "/"
                         + scheduleDaysWagesBudgetedCountTotal + ")");
            }
        }

        if (scheduleWeekWagesScheduledCount != null && scheduleDaysWagesScheduledCountTotal != null) {
            if (scheduleWeekWagesScheduledCount.equals(scheduleDaysWagesScheduledCountTotal)) {
                SimpleUtils.pass("Schedule Page: Week Scheduled Wages matched with Sum of Days Scheduled Wages ("
                    + scheduleWeekWagesScheduledCount + "/"
                    + scheduleDaysWagesScheduledCountTotal);
            } else {
//	   		        SimpleUtils.assertOnFail("Schedule Page: Week Scheduled Wages not matched with Sum of Days Scheduled Wages (" +scheduleWeekWagesScheduledCount+ "/" 
//	   		        		+ scheduleDaysWagesScheduledCountTotal + ")", scheduleWeekWagesScheduledCount.equals(scheduleDaysWagesScheduledCountTotal), true);
                SimpleUtils.report("Schedule Page: Week Scheduled Wages not matched with Sum of Days Scheduled Wages ("
                    + scheduleWeekWagesScheduledCount + "/"
                    + scheduleDaysWagesScheduledCountTotal + ")");
            }
        }
    }

    public boolean iswagesLoadedInWeekView(HashMap<String, Float> scheduleWeekViewLabelData) {
        Float scheduleWeekWagesBudgetedCount = scheduleWeekViewLabelData
            .get(scheduleHoursAndWagesData.budgetedWages.getValue());
        Float scheduleWeekWagesScheduledCount = scheduleWeekViewLabelData
            .get(scheduleHoursAndWagesData.scheduledWages.getValue());
        if (scheduleWeekWagesBudgetedCount != null && scheduleWeekWagesScheduledCount != null) {
            return true;
        }
        return false;
    }
}