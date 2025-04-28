package com.legion.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

public interface ScheduleOverviewPage {

	public List<String> getScheduleWeeksStatus() throws Exception;
	public Map<String, String> getWeekStartDayAndCurrentWeekDates() throws Exception;
	public List<HashMap<String, String>> getOverviewPageWeeksDuration() throws Exception;
	public Boolean isCurrentWeekHighLighted() throws Exception;
	public Boolean verifyDateAndDayForEachWeekUntilNotAvailable() throws Exception;
	public Boolean verifyDayAndDateOnSchedulePageMatchesDayAndDateOnOverviewPage() throws Exception;
	public List<String> getCurrentAndUpcomingActiveWeeksDaysOnCalendar() throws Exception;
	public void clickOnCurrentWeekToOpenSchedule() throws Exception;
	public String getOverviewCalenderWeekDays() throws Exception;
	public List<WebElement> getOverviewScheduleWeeks();
	public void clickScheduleDraftAndGuidanceStatus(List<String> overviewScheduleWeeksStatus);
	public ArrayList<String> getOverviewCalendarMonthsYears() throws Exception;
	public LinkedHashMap<String, Float> getWeekHoursByWeekElement(WebElement overViewWeek);
	public void clickOnGuidanceBtnOnOverview(int index);
	public boolean loadScheduleOverview() throws Exception;
	public boolean loadScheduleTableInOverview() throws Exception;
	public int getScheduleOverviewWeeksCountCanBeCreatInAdvance();
	public String getOverviewWeekDuration(WebElement webElement) throws Exception;
	public void clickOverviewTab();

    public boolean isCurrent2MonthCalendarVisible() throws Exception;
	public boolean isCurrentDateRed() throws Exception;
	public void verifyNavigation() throws Exception;
	public boolean isCurrentWeekDarkBlueColor() throws Exception;

	public List<String> getScheduleActivityInfo();
	public List<String> getOverviewData() throws Exception;
	public int getDaysBetweenFinalizeDateAndScheduleStartDate(String finalizeByDate, String scheduleStartDate) throws Exception;
	public void clickOnLastWeek() throws Exception;
	public void clickOnViewGroupScheduleButton() throws Exception;
	public String getCurrentWeekBudgetHours() throws Exception;
	public List<String> getScheduleWeeksStatusWarningMessage() throws Exception;
}
