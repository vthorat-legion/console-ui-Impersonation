package com.legion.tests.core.scheduling.schedulecost;

import com.legion.api.schedule.cost.ScheduleCostAPI;
import com.legion.api.schedule.cost.ScheduleCostContainer;
import com.legion.pages.*;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ScheduleCostPremiumTest extends TestBase {
    private HashMap<String, String> locationBusinessID = JsonUtil.getPropertiesFromJsonFile("src/test/resources/LocationBusinessID.json");
    private String currentBusinessId;
    private final String DATE_FORMAT = "yyyy MMM d";
    private String year;
    private CreateSchedulePage createSchedulePage;
    private ScheduleCommonPage scheduleCommonPage;

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) {
        try {
            this.createDriver((String) params[0], "69", "Window");
            visitPage(testMethod);
            loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
            currentBusinessId = locationBusinessID.get(params[3]);
            createSchedulePage = pageFactory.createCreateSchedulePage();
            scheduleCommonPage = pageFactory.createScheduleCommonPage();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nora")
    @Enterprise(name = "CinemarkWkdy_Enterprise")
    @TestName(description = "test")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void validateTestAsInternalAdmin(String browser, String username, String password, String location) throws Exception{
        try {
            goToSchedulePageScheduleTab();

            boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
            if (isWeekGenerated) {
                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
            }
            createSchedulePage.createScheduleForNonDGFlowNewUI();

            int weekStartDayOfTheYear = SimpleUtils.getWeekStartDayOfTheYear(getWholeDateFromWeekView(), DATE_FORMAT);
            createSchedulePage.publishActiveSchedule();

            ScheduleCostContainer scheduleCostContainer = ScheduleCostAPI.getScheduleCost(username, password, weekStartDayOfTheYear, currentBusinessId, year);
            SimpleUtils.assertOnFail("The total wage is incorrect!",
                    String.valueOf(scheduleCostContainer.getWeekData().get("totalWage")).equalsIgnoreCase("320"), false);
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    public String getWholeDateFromWeekView() throws Exception {
        Map<String, String> activeDayInfo = scheduleCommonPage.getSelectedWeekInfo();
        year = activeDayInfo.get("year");
        return activeDayInfo.get("year") + " " + activeDayInfo.get("month") + " " + activeDayInfo.get("day");
    }
}
