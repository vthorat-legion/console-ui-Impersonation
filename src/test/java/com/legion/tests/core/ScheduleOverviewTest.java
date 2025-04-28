package com.legion.tests.core;

import java.lang.reflect.Method;
import java.util.*;

import com.legion.pages.*;
import org.openqa.selenium.WebElement;
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

public class ScheduleOverviewTest extends TestBase{

	private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();



	@Override
	  @BeforeMethod()
	  public void firstTest(Method testMethod, Object[] params) throws Exception{
		  this.createDriver((String)params[0],"69","Window");
	      visitPage(testMethod);
	      loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
	  }
//
//    @Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//    @Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Check the 1st/current week is highlighted and DateAndDay are correct")
//    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyCurrentWeekDateAndDayTest(String username, String password, String browser, String location) throws Exception {
////    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//	    scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//        //Map<String, String> currentWeekCalendarWeekDaysAndStartDay = scheduleOverviewPage.getWeekStartDayAndCurrentWeekDates();
//	    SimpleUtils.assertOnFail("Current Week not Highlighted!",scheduleOverviewPage.isCurrentWeekHighLighted(), false);
//
//    }
//
//    @Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//    @Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Check each week until weeks are not available DateAndDay are correct on overview page")
//    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyDateAndDayForEachWeeksUntilNotAvailableTest(String username, String password, String browser, String location) throws Exception {
////    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//	    scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//	    SimpleUtils.assertOnFail("DateAndDay verification failed for each week",
//	    		scheduleOverviewPage.verifyDateAndDayForEachWeekUntilNotAvailable(), false);
//    }
//
//    @Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//    @Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Click on each week to open schedule page and ensure the DayAndDate on schedule page matches the DayAndDate on overview page")
//    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//    public void verifyDayAndDateOnSchedulePageMatchesDayAndDateOnOverviewPageTest(String username, String password, String browser, String location) throws Exception {
//    	String scheduleWeekStatusToVerify = "Not Available";
//    	Boolean isCurrentWeekSelected = false;
//    	int index = 0;
////    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//	    scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//        scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//        List<String> currentAndUpcomingActiveWeeksDatesOnCalendar = scheduleOverviewPage.getCurrentAndUpcomingActiveWeeksDaysOnCalendar();
//		List<String> overviewPageScheduledWeekStatus = scheduleOverviewPage.getScheduleWeeksStatus();
//		String overviewCalendarDays = scheduleOverviewPage.getOverviewCalenderWeekDays();
//		for(String scheduleWeekStatus : overviewPageScheduledWeekStatus)
//		{
//			if(!scheduleWeekStatus.contains(scheduleWeekStatusToVerify)) {
//				if(isCurrentWeekSelected) {
//					scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture("next", 1);
//				}
//				else {
//					scheduleOverviewPage.clickOnCurrentWeekToOpenSchedule();
//					isCurrentWeekSelected = true;
//				}
//
//		        String activeWeekTimeDurationForEachday = schedulePage.getActiveWeekDayMonthAndDateForEachDay();
//				Boolean isScheduleActiveWeekMatchedWithOverviewCalendarWeek = schedulePage.validateScheduleActiveWeekWithOverviewCalendarWeek(currentAndUpcomingActiveWeeksDatesOnCalendar.get(index),
//						overviewCalendarDays, activeWeekTimeDurationForEachday);
//		        SimpleUtils.assertOnFail("Verification for DayAndDate On SchedulePage does not Matche with DayAndDate On OverviewPage!",isScheduleActiveWeekMatchedWithOverviewCalendarWeek, false);
//			}
//			index = index + 1;
//		}
//
//    }

	@Automated(automated ="Automated")
	@Owner(owner = "Estelle")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Schedule functionality > Overview")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	public void verifyScheduleFunctionalityOverviewAsStoreManager(String username, String password, String browser, String location) throws Exception {
		try {
			SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
			CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();

			//	Current +2 month calendar is visible
			SimpleUtils.assertOnFail("Current +2 month calendar is visible", scheduleOverviewPage.isCurrent2MonthCalendarVisible(), false);
			//"<" Button is navigate to previous month calendar, After Clicking on "<" , ">" button is enabled and navigating to future month calendar
			scheduleOverviewPage.verifyNavigation();
			//	Schedule week & its status is visible: if it is finalized,draft,guidance,published
			List<HashMap<String, String>> weekDuration = scheduleOverviewPage.getOverviewPageWeeksDuration();
			List<String> scheduleWeekStatus = scheduleOverviewPage.getScheduleWeeksStatus();
			if (scheduleWeekStatus.contains("Published") || scheduleWeekStatus.contains("Draft")
					|| scheduleWeekStatus.contains("Finalized") || scheduleWeekStatus.contains("Guidance")) {
				SimpleUtils.pass("Schedule week & its status is visible");
			} else if (weekDuration.size() > 0) {
				SimpleUtils.pass("Schedule week is visible");
			} else
				SimpleUtils.fail("Schedule week & its status is not visible", true);
			//	Current week is in dark blue color
			SimpleUtils.assertOnFail("Current Week not Highlighted!", scheduleOverviewPage.isCurrentWeekDarkBlueColor(), false);
			//	Current Date is in Red color
			SimpleUtils.assertOnFail("Current Date is not in Red color", scheduleOverviewPage.isCurrentDateRed(), false);

			//	user can click on Schedule week which will navigate to Schedule page
			scheduleOverviewPage.clickOnCurrentWeekToOpenSchedule();
			SimpleUtils.pass("user can click on Schedule week which will navigate to Schedule page");

			boolean isWeekGenerated = createSchedulePage.isWeekGenerated();
			if (!isWeekGenerated) {
				createSchedulePage.createScheduleForNonDGFlowNewUI();
			}
			HashMap<String, Float> scheduleSmartCardHoursWages = smartCardPage.getScheduleBudgetedHoursInScheduleSmartCard();

			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
            //	Weekly Budgeted/Scheduled,other hour are showing in overview and matching with the Schedule smartcard of Schedule page
			List<WebElement> scheduleOverViewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
			HashMap<String, Float> overviewData = scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(0));

			if ((scheduleSmartCardHoursWages.get("budgetedHours") - overviewData.get("guidanceHours") <= 0.05)
					& (scheduleSmartCardHoursWages.get("scheduledHours") - overviewData.get("scheduledHours") <= 0.05)
					& (scheduleSmartCardHoursWages.get("otherHours") - overviewData.get("otherHours") <= 0.05)) {
				SimpleUtils.pass("Schedule/Budgeted smartcard-is showing the values in Hours and wages, it is displaying the same data as overview page have for the current week .");
			} else {
				SimpleUtils.fail("Scheduled Hours and Overview Schedule Hours not same, hours on smart card for budget, scheduled and other are: " +
						scheduleSmartCardHoursWages.get("budgetedHours") + ", " + scheduleSmartCardHoursWages.get("scheduledHours") + ", " + scheduleSmartCardHoursWages.get("otherHours")
						+ ". But hours on Overview page are: " + overviewData.get("guidanceHours") + ", " + overviewData.get("scheduledHours") + ", " + overviewData.get("otherHours"),false);
			}

			//	After Generating Schedule, status will be in draft and scheduled hour will also updated.
			List<WebElement> overviewPageScheduledWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
			Float guidanceHoursForGuidanceSchedule = 0.0f;
			Float scheduledHoursForGuidanceSchedule = 0.0f;
			Float otherHoursForGuidanceSchedule = 0.0f;
			for (int i = 0; i < overviewPageScheduledWeeks.size(); i++) {
				if (overviewPageScheduledWeeks.get(i).getText().toLowerCase().contains(ScheduleTestKendraScott2.overviewWeeksStatus.Guidance.getValue().toLowerCase())) {
					HashMap<String, Float> overviewDataInGuidance = scheduleOverviewPage.getWeekHoursByWeekElement(scheduleOverViewWeeks.get(i));
					if (overviewDataInGuidance.get("scheduledHours").equals(scheduledHoursForGuidanceSchedule) & overviewDataInGuidance.get("otherHours").equals(otherHoursForGuidanceSchedule)) {
						SimpleUtils.pass("If any week is in Guidance status, then only Budgeted hours should be consistent with schedule page, scheduledHours and otherHours are all zero");
					} else
						SimpleUtils.fail("this status of this week is not in Guidance", false);

					String activityInfoBeforeGenerated = null;
					String scheduleStatusAftGenerated = null;
					scheduleOverviewPage.clickOnGuidanceBtnOnOverview(i);
					Thread.sleep(5000);
					if (!createSchedulePage.isWeekGenerated()) {
						createSchedulePage.createScheduleForNonDGFlowNewUI();
						scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());

						List<String> scheduleActivityInfo = scheduleOverviewPage.getScheduleActivityInfo();
						String activityInfoAfterGenerateSchedule = scheduleActivityInfo.get(i);
						List<String> scheduleWeekStatus2 = scheduleOverviewPage.getScheduleWeeksStatus();
						scheduleStatusAftGenerated = scheduleWeekStatus2.get(i);
						if (scheduleStatusAftGenerated.equals("Draft")) {
							SimpleUtils.pass("After Generating Schedule, status will be in draft");
						}
						if (activityInfoAfterGenerateSchedule.contains(username.substring(0, 6)) & !activityInfoAfterGenerateSchedule.equals(activityInfoBeforeGenerated)) {
							//	whoever made the changes in Schedule defined in Activity
							//	Profile icon of user is in round shape and his/her name is showing along with Time and date[when he/she has made the changes]
							SimpleUtils.pass("Profile icon of user is updated by current user");
						}
					}
					break;

				} else {
					SimpleUtils.report("there is no guidance schedule");
				}
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}


}
