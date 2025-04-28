package com.legion.pages.core;

import static com.legion.utils.MyThreadLocal.failedComment;
import static com.legion.utils.MyThreadLocal.getDriver;

import java.text.SimpleDateFormat;
import java.util.*;

import com.legion.utils.JsonUtil;
import cucumber.api.java.ro.Si;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.legion.pages.BasePage;
import com.legion.pages.ScheduleOverviewPage;
import com.legion.utils.SimpleUtils;

public class ConsoleScheduleOverviewPage extends BasePage implements ScheduleOverviewPage{

	private static HashMap<String, String> parametersMap2 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ControlsPageLocationDetail.json");
	@FindBy(className="schedule-status-title")
	private List<WebElement> scheduleOverviewWeeksStatus;

	@FindBy(className="activity-props")
	private List<WebElement> scheduleOverviewActivityInfo;

	@FindBy(css = "div.week.background-current-week-legend-calendar")
	private List<WebElement> currentWeeksOnCalendar;

	@FindBy(css = "div.weekday")
	private List<WebElement> overviewPageCalendarWeekdays;

	@FindBy(css = "div.fx-center.left-banner")
	private List<WebElement> overviewPageScheduleWeekDurations;

	@FindBy(css="div.week.ng-scope")
	private List<WebElement> overviewscheduledWeeks;

	@FindBy(className = "schedule-table-row")
	private List<WebElement> overviewTableRows;

	@FindBy(css = ".col-sm-3.pl-0.calendar-container.ng-scope")
	private List<WebElement> overviewCalendar;

	@FindBy(css = "div.row-fx.schedule-table-row")
	private List<WebElement> overviewScheduleWeekList;

	@FindBy(css = "div.lgn-calendar.current-month")
	private List<WebElement> overviewCalendarMonthsYears;

	@FindBy(css = "div[ng-if*='!loading']")
	private WebElement scheduleTable;

	@FindBy(className = "left-banner")
	private List<WebElement> weeklyScheduleDateElements;

	@FindBy(css = "div.calendar-week")
	private WebElement calendar;

	@FindBy(css = "div.console-navigation-item-label.Schedule")
	private WebElement consoleSchedulePageTabElement;

	@FindBy(xpath = "//span[contains(text(),'Overview')]")
	private WebElement overviewTab;

	@FindBy(css = ".col-sm-1.pl-0.arrow-left")
	private WebElement arrowLeftInCal;

	@FindBy(css = ".fa.fa-angle-right")
	private WebElement arrowRightInCal;

	@FindBy(css = ".day-number.default-font-style.ng-binding.current-day")
	private WebElement currentDateInCal;

	public ConsoleScheduleOverviewPage() {
		PageFactory.initElements(getDriver(), this);
	}

	@Override
	public List<String> getScheduleWeeksStatus() throws Exception{
		List<String> overviewScheduleWeeksStatus = new ArrayList<String>();
		if (loadScheduleTableInOverview()) {
			if (scheduleOverviewWeeksStatus.size() != 0) {
				for (WebElement overviewWeekStatus : scheduleOverviewWeeksStatus) {
					overviewScheduleWeeksStatus.add(overviewWeekStatus.getText());
				}
			}
		}
		else
			SimpleUtils.fail("The schedule view table failed to load",false);

		return overviewScheduleWeeksStatus;
	}

	@Override
	public List<String> getScheduleActivityInfo() {
		List<String> overviewScheduleActivityInfo = new ArrayList<String>();
		if (scheduleOverviewActivityInfo.size() != 0) {
			for (WebElement ActivityInfo : scheduleOverviewActivityInfo) {
				overviewScheduleActivityInfo.add(ActivityInfo.getText());

			}
		}
		return overviewScheduleActivityInfo;
	}

	@Override
	public Map<String, String> getWeekStartDayAndCurrentWeekDates() throws Exception {
		Map<String, String> calendarStartWeekAndCurrentWeekDays = new HashMap<String, String>();
		calendarStartWeekAndCurrentWeekDays.put("weekStartDay", getOverviewCalenderWeekDays());
		if (currentWeeksOnCalendar.size() != 0) {
			String currentWeeksDaysLabel = "";
			for (WebElement weekLabel : currentWeeksOnCalendar) {
				currentWeeksDaysLabel = currentWeeksDaysLabel + "" + weekLabel.getText();
			}
			String[] currentWeekDaysOnCalendar = currentWeeksDaysLabel.split("\n");
			String currentWeekDaysOnCalendarAsString = "";
			for (String weekDay : currentWeekDaysOnCalendar) {
				if (weekDay.length() == 1)
					weekDay = "0" + weekDay;
				if (currentWeekDaysOnCalendarAsString != "")
					currentWeekDaysOnCalendarAsString = currentWeekDaysOnCalendarAsString + "," + weekDay;
				else
					currentWeekDaysOnCalendarAsString = weekDay;
			}
			calendarStartWeekAndCurrentWeekDays.put("weekDates", currentWeekDaysOnCalendarAsString);
		}
		return calendarStartWeekAndCurrentWeekDays;
	}

	/*
	 *  Check the 1st week is highlighted and DateAndDay are correct
	 */

	@Override
	public Boolean isCurrentWeekHighLighted() throws Exception {
		int isoYear = SimpleUtils.getCurrentISOYear();
		int dayOfYearForToday = SimpleUtils.getCurrentDateDayOfYear();
		String weekTypeDate = "currentWeekDate";
		String currentDayMonthDate = SimpleUtils.getDayMonthDateFormatForCurrentPastAndFutureWeek(
				dayOfYearForToday, isoYear).get(weekTypeDate);
		String[] currentWeekDates = getWeekStartDayAndCurrentWeekDates().get("weekDates").split(",");
		Boolean isCurrentdateMatchedWithOverviewCalendarWeekDates = false;

		// check current date matched with highlighted week dates or not?
		for (String weekDate : currentWeekDates) {
			if (currentDayMonthDate.split(" ")[2].contains(weekDate)) {
				isCurrentdateMatchedWithOverviewCalendarWeekDates = true;
			}
		}
		if (!isCurrentdateMatchedWithOverviewCalendarWeekDates) {
			SimpleUtils.fail("Current Date not matched with highlighted week dates!", false);
			return false;
		} else {
			SimpleUtils.pass("Current Date matched with highlighted week dates!");
		}

		// Check Overview page Calendar week start & end date matches or not with Overview Week Duration
		String calendarCurrentWeekStartDate = currentWeekDates[0];
		String scheduleCurrentWeekStartDate = getOverviewPageWeeksDuration().get(0).get("durationWeekStartDay").split(" ")[1];
		String calendarCurrentWeekEndDate = currentWeekDates[currentWeekDates.length - 1];
		String scheduleCurrentWeekEndDate = getOverviewPageWeeksDuration().get(0).get("durationWeekEndDay").split(" ")[1];

		if (isScheduleDurationStartDayMatchesWithCalendarWeekStartDay(calendarCurrentWeekStartDate, scheduleCurrentWeekStartDate)) {
			SimpleUtils.pass("Current Week start date:'" + calendarCurrentWeekStartDate + "' on Overview calendar and Overview current schedule duration start date:'" + scheduleCurrentWeekStartDate + "' matched!");
			if (isScheduleDurationEndDayMatchesWithCalendarWeekEndDay(calendarCurrentWeekEndDate, scheduleCurrentWeekEndDate)) {
				SimpleUtils.pass("Current Week end date:'" + calendarCurrentWeekEndDate + "' on Overview calendar and Overview current schedule duration end Date:'" + scheduleCurrentWeekEndDate + "' matched!");
				return true;
			} else {
				SimpleUtils.fail("Current Week end date:'" + calendarCurrentWeekEndDate + "' on Overview calendar and Overview current schedule week duration end Date:'" + scheduleCurrentWeekEndDate + "' not matched!", true);
			}
		} else {
			SimpleUtils.fail("Current Week start date::'" + calendarCurrentWeekStartDate + "' on Overview calendar and Overview current schedule week duration start date:'" + scheduleCurrentWeekStartDate + "' not matched!", true);
		}

		return false;
	}

	@Override
	public boolean isCurrentWeekDarkBlueColor() throws Exception {
		String color = "#001160";
		List<HashMap<String, String>> weekList = getOverviewPageWeeksDuration();
		if (overviewPageScheduleWeekDurations.get(0).getAttribute("class").contains("current")) {
			String currentWeekColor = Color.fromString(overviewPageScheduleWeekDurations.get(0).getCssValue("background-color")).asHex();
			if (color.equals(currentWeekColor)) {
				SimpleUtils.pass("Current week is in dark blue color");
				return true;
			}
		}
		return false;
	}

	@Override
	public List<HashMap<String, String>> getOverviewPageWeeksDuration() throws Exception {
		List<HashMap<String, String>> overviewWeeksDuration = new ArrayList<HashMap<String, String>>();
		if (loadScheduleTableInOverview()) {
			if (overviewPageScheduleWeekDurations.size() != 0) {
				for (WebElement overviewPageScheduleWeekDuration : overviewPageScheduleWeekDurations) {
					HashMap<String, String> scheduleEachWeekDuration = new HashMap<String, String>();
					String[] overviewWeekDuration = overviewPageScheduleWeekDuration.getText().replace("\n", "").split("\\—", 2);
					scheduleEachWeekDuration.put("durationWeekStartDay", overviewWeekDuration[0].trim());
					scheduleEachWeekDuration.put("durationWeekEndDay", overviewWeekDuration[1].trim());
					overviewWeeksDuration.add(scheduleEachWeekDuration);
				}
			} else
				SimpleUtils.fail("The duration of weeks are not loaded! ", false);
		}
				return overviewWeeksDuration;
		}

	//Check each week until weeks are 'Not Available' DateAndDay are correct on overview page
	@Override
	public Boolean verifyDateAndDayForEachWeekUntilNotAvailable() throws Exception
	{
		/**
		 * wait for week list load
		 */
		waitForSeconds(5);
		int index = 0;
		int weekMatched = 0;
		String scheduleWeekStatusToVerify = "Not Available";
		List<String> overviewPageScheduledWeekStatus = getScheduleWeeksStatus();
		List<String> currentAndUpcomingActiveWeeksDaysOnCalendar = getCurrentAndUpcomingActiveWeeksDaysOnCalendar();
		for(String scheduleWeekStatus : overviewPageScheduledWeekStatus)
		{ 
			String[] calenderWeekDates = currentAndUpcomingActiveWeeksDaysOnCalendar.get(index).split(",");
			String calendarWeekStartDate = calenderWeekDates[0];
			String scheduleWeekStartDate = getOverviewPageWeeksDuration().get(index).get("durationWeekStartDay").split(" ")[1];
			String calendarWeekEndDate = calenderWeekDates[calenderWeekDates.length - 1];
			String scheduleWeekEndDate = getOverviewPageWeeksDuration().get(index).get("durationWeekEndDay").split(" ")[1];
			
			if(scheduleWeekStatus.contains(scheduleWeekStatusToVerify)) {
				SimpleUtils.pass("Overview Page: Week Status found as 'Not Available' for Week Duration-'" 
						+ getOverviewPageWeeksDuration().get(index).get("durationWeekStartDay") +" - "	
							+ getOverviewPageWeeksDuration().get(index).get("durationWeekEndDay") +"'");
				break;
			}
			
			if(isScheduleDurationStartDayMatchesWithCalendarWeekStartDay(calendarWeekStartDate, scheduleWeekStartDate))
			{
				SimpleUtils.pass("Current Week start date:'"+ calendarWeekStartDate +"' on Overview calendar and Overview current schedule duration start date:'" + scheduleWeekStartDate + "' matched!");
				if(isScheduleDurationEndDayMatchesWithCalendarWeekEndDay(calendarWeekEndDate, scheduleWeekEndDate)) {
					SimpleUtils.pass("Current Week end date:'"+ calendarWeekEndDate +"' on Overview calendar and Overview current schedule duration end Date:'" + scheduleWeekEndDate + "' matched!");
					weekMatched = weekMatched + 1;
				}
				else {
					SimpleUtils.fail("Current Week end date:'"+ calendarWeekEndDate +"' on Overview calendar and Overview current schedule week duration end Date:'" + scheduleWeekEndDate + "' not matched!", true);
				}
			}
			else {
				SimpleUtils.fail("Current Week start date::'"+ calendarWeekStartDate +"' on Overview calendar and Overview current schedule week duration start date:'" + scheduleWeekStartDate + "' not matched!", true);	
			}
			
			index = index + 1;
		}
		if(index != 0)
			return true;
		else
			return false;
	}
	
	//Click on each week to open schedule page and ensure the DayAndDate on schedule page matches the DayAndDate on overview page
	public Boolean verifyDayAndDateOnSchedulePageMatchesDayAndDateOnOverviewPage() throws Exception
	{
		return false;
	}
	
	@Override
	public List<String> getCurrentAndUpcomingActiveWeeksDaysOnCalendar() throws Exception
	{
		List<String> calendarWeeksOnOverviewPage = new ArrayList<String>();
		Boolean isCurrentWeekStart = false;
		int daysInWeek = 7;
		String currentWeekDaysOnCalendarAsString = "";
		if(overviewscheduledWeeks.size() != 0) {
			for(WebElement overviewscheduledWeek : overviewscheduledWeeks) {
				if(overviewscheduledWeek.getAttribute("class").toString().contains("background-current-week-legend-calendar"))
					isCurrentWeekStart = true;
				if(isCurrentWeekStart)
				{
					String[] weekDaysOnCalendar = overviewscheduledWeek.getText().split("\n");
					for(String weekDay : weekDaysOnCalendar)
					{
						weekDay = weekDay.toString().trim();
						if(weekDay.length() != 0)
						{
							if(weekDay.length() == 1)
								weekDay = "0" + weekDay;
							if(currentWeekDaysOnCalendarAsString != "")
								currentWeekDaysOnCalendarAsString = currentWeekDaysOnCalendarAsString + "," + weekDay;
							else
								currentWeekDaysOnCalendarAsString = weekDay;
						}
					}
					if(currentWeekDaysOnCalendarAsString.split(",").length == daysInWeek)
					{
						calendarWeeksOnOverviewPage.add(currentWeekDaysOnCalendarAsString);
						currentWeekDaysOnCalendarAsString = "";
					}
				}
			}
		}
		return calendarWeeksOnOverviewPage;
	}
	
	
	public Boolean isScheduleDurationStartDayMatchesWithCalendarWeekStartDay(String calendarWeekStartDate, String scheduleWeekStartDate)
	{
		if(calendarWeekStartDate.equals(scheduleWeekStartDate)) {
			return true;
		}
		return false;
	}
	
	public Boolean isScheduleDurationEndDayMatchesWithCalendarWeekEndDay(String calendarWeekEndDate, String scheduleWeekEndDate)
	{
		if(calendarWeekEndDate.equals(scheduleWeekEndDate)) {
				return true;
			}
		return false;
	}
	
	@Override
	public void clickOnCurrentWeekToOpenSchedule() throws Exception
	{
		int currentWeekIndex = 0;
		if(currentWeeksOnCalendar.size() != 0)
		{
			click(currentWeeksOnCalendar.get(currentWeekIndex));
			if(overviewTableRows.size() != 0)
			{
				click(overviewTableRows.get(currentWeekIndex));
				SimpleUtils.pass("user can click on Schedule week which will navigate to Schedule page");
			}
			else {
				SimpleUtils.fail("Overview page Schedule table not loaded successfully!", false);
			}
		}
		else {
			SimpleUtils.fail("Current Week Not loaded on Overview calendar!", false);
		}
	}
	
	public String getOverviewCalenderWeekDays() throws Exception 
	{
		String weekDays = "";
		if(overviewPageCalendarWeekdays.size() != 0) {
			for(int index = 0; index < 7; index++)
			{
				if(weekDays != "")
					weekDays = weekDays + "," + overviewPageCalendarWeekdays.get(index).getText();
				else
					weekDays = overviewPageCalendarWeekdays.get(index).getText();
			}
		}
		else {
			SimpleUtils.fail("Overview Page: Calendar week days not found!",true);
		}
		return weekDays;
	}

	@Override
	public List<WebElement> getOverviewScheduleWeeks() {
		waitForSeconds(5);
		List<WebElement> overviewWeekList = new ArrayList<>();
		if(areListElementVisible(overviewTableRows,15) && areListElementVisible(overviewScheduleWeekList)){
			overviewWeekList = overviewScheduleWeekList;
		} else {
			SimpleUtils.fail("Overview Page: Week List not loaded Successfully!", false);
		}
		return overviewWeekList;
	}

	public void clickScheduleDraftAndGuidanceStatus(List<String> overviewScheduleWeeksStatus){

		for(int i=0;i<overviewScheduleWeeksStatus.size();i++){
			if(overviewScheduleWeeksStatus.get(i).contains("Finalized") ||
					overviewScheduleWeeksStatus.get(i).contains("Published") ||
					overviewScheduleWeeksStatus.get(i).contains("Draft")&&
					overviewScheduleWeeksStatus.get(i+1).contains("Guidance")){
			}

		}
	}


	@Override
	public ArrayList<String> getOverviewCalendarMonthsYears() throws Exception
	{
		ArrayList<String> overviewCalendarMonthsYearsText = new ArrayList<String>();
		if(overviewCalendarMonthsYears.size() != 0)
		{
			for(WebElement overviewCalendarMonthYear : overviewCalendarMonthsYears)
			{
				overviewCalendarMonthsYearsText.add(overviewCalendarMonthYear.getText().replace("\n", ""));
			}
		}
		return overviewCalendarMonthsYearsText;
	}

	/*@FindBy(css = "")
	private List<WebElement> weekHoursElement;*/
	@Override
	public LinkedHashMap<String, Float> getWeekHoursByWeekElement(WebElement overViewWeek) {
		LinkedHashMap<String, Float> weekHours = new LinkedHashMap<String, Float>();
		List<WebElement> weekHoursElement = overViewWeek.findElements(By.cssSelector("span.text-hours"));
		if(weekHoursElement.size() == 4)
		{
			float guidanceHours = Float.valueOf(convertToZeroIfIsNotNumeric(
					weekHoursElement.get(0).getText().split(" ")[0].replace(",","")));
			float scheduledHours = Float.valueOf(convertToZeroIfIsNotNumeric(
					weekHoursElement.get(1).getText().split(" ")[0].replace(",","")));
			float otherHours = Float.valueOf(convertToZeroIfIsNotNumeric(
					weekHoursElement.get(2).getText().split(" ")[0].replace(",","")));
			float projectedHours = Float.valueOf(convertToZeroIfIsNotNumeric(
					weekHoursElement.get(3).getText().split(" ")[0].replace(",","")));
			weekHours.put("guidanceHours", guidanceHours);
			weekHours.put("scheduledHours", scheduledHours);
			weekHours.put("otherHours", otherHours);
			weekHours.put("projectedHours", projectedHours);

		} else if (weekHoursElement.size() == 3) {
			float guidanceHours = Float.valueOf(convertToZeroIfIsNotNumeric(
					weekHoursElement.get(0).getText().split(" ")[0].replace(",","")));
			float scheduledHours = Float.valueOf(convertToZeroIfIsNotNumeric(
					weekHoursElement.get(1).getText().split(" ")[0].replace(",","")));
			float otherHours = Float.valueOf(convertToZeroIfIsNotNumeric(
					weekHoursElement.get(2).getText().split(" ")[0].replace(",","")));
			weekHours.put("guidanceHours", guidanceHours);
			weekHours.put("scheduledHours", scheduledHours);
			weekHours.put("otherHours", otherHours);
		} else
			SimpleUtils.fail("The week hours fail to load! ", false);
		return weekHours;
	}

	private String convertToZeroIfIsNotNumeric(String value) {
		if (!SimpleUtils.isNumeric(value)) {
			return "0";
		} else {
			return value;
		}
	}
	//added by Gunjan

	@Override
	public boolean loadScheduleOverview() throws Exception {
		// TODO Auto-generated method stub
		boolean flag=false;
		if(isElementLoaded(consoleSchedulePageTabElement, 15)){
			consoleSchedulePageTabElement.click();
			if(isElementLoaded(calendar, 15)){
				flag = true;
				SimpleUtils.pass("Calendar on Schedule Overview Loaded Successfully!");
			}else{
				SimpleUtils.fail("Calendar on Schedule Overview Not Loaded Successfully!", false);
			}
			if(isElementLoaded(scheduleTable, 60)){
				flag = true;
				SimpleUtils.pass("Schedule Table on Schedule Overview Loaded Successfully!");
			}else{
				SimpleUtils.fail("Schedule Table on Schedule Overview Not Loaded Successfully!", false);
			}
		}else{
			SimpleUtils.fail("ScheduleTab left navigation menu not found", true);
		}
		return flag;
	}


	public void refreshTheBrowser() {
		try {
			getDriver().navigate().refresh();
		} catch (TimeoutException ignored) {
		}

	}

	@Override
	public boolean loadScheduleTableInOverview() throws Exception {
		// check the schedule table loaded in overview page
		boolean flag=false;
		if(isElementLoaded(consoleSchedulePageTabElement, 10)){
			consoleSchedulePageTabElement.click();
			waitForSeconds(5);
			int tryT=3;
			while (tryT>0){
				if(isElementLoaded(scheduleTable, 10)){
					flag = true;
					SimpleUtils.pass("Schedule Table on Schedule Overview Loaded Successfully!");
					break;
				}else{
					SimpleUtils.fail("Schedule Table on Schedule Overview Not Loaded Successfully!", false);
					tryT--;
					loadScheduleTableInOverview();
				}
			}
//
		}else{
			SimpleUtils.fail("ScheduleTab left navigation menu not found", true);
		}
		return flag;
	}

	//added by Nishant

	public void clickOnGuidanceBtnOnOverview(int index){
		if(areListElementVisible(weeklyScheduleDateElements, 30)
				&& weeklyScheduleDateElements.size()!=0){
			click(weeklyScheduleDateElements.get(index));
			waitForSeconds(4);
		}else{
			SimpleUtils.fail("Click on Guidance On Schedule Overview Page",false);
		}
	}


	@FindBy(css = "div.week")
	private List<WebElement> calendarWeeks;

	@Override
	public int getScheduleOverviewWeeksCountCanBeCreatInAdvance()
	{
		boolean isPastWeek = true;
		float scheduleWeekCountToBeCreated = 0;
		for(WebElement week : calendarWeeks)
		{
			float currentWeekCount = 1;
			if(week.getAttribute("class").contains("current-week"))
				isPastWeek = false;
			int weekDayCount = week.getText().split("\n").length;
			if(weekDayCount < 7)
				currentWeekCount = (float) 0.5;
			boolean isCurrentWeekLocked = false;
			if(week.getAttribute("class").contains("week-locked"))
				isCurrentWeekLocked = true;

			if(!isPastWeek && !isCurrentWeekLocked)
			{
				scheduleWeekCountToBeCreated = (scheduleWeekCountToBeCreated + currentWeekCount);
			}

		}
		return (int) (scheduleWeekCountToBeCreated - 1);
	}


	@Override
	public String getOverviewWeekDuration(WebElement webElement) throws Exception {
		String weekDurationText = "";
		if(isElementLoaded(webElement)) {
			WebElement weekdurationElement = webElement.findElement(By.cssSelector("div.left-banner"));
			if(isElementLoaded(weekdurationElement))
				weekDurationText = weekdurationElement.getText().replace("\n", " ");
			else
				SimpleUtils.fail("Overview Page: Unable to get Week Duration.", true);
		}
		else
			SimpleUtils.fail("Overview Page: Unable to get Week Duration.", true);
		return weekDurationText;
	}

	public void clickOverviewTab(){
		if(isElementEnabled(overviewTab,5)){
			click(overviewTab);
			SimpleUtils.pass("Clicked on Overview tab successfully");
		}else{
			SimpleUtils.fail("Not able to click on Overview tab successfully",false);
		}
	}

	@Override
	public boolean isCurrent2MonthCalendarVisible() throws Exception {
		boolean flag = false;
		if (areListElementVisible(overviewCalendar, 5) &overviewCalendar.size()==3) {
			ArrayList<String> monthYearInCalendar = getOverviewCalendarMonthsYears();
			ArrayList<Date> monthYearInCalendar2 = new ArrayList<>();
			SimpleDateFormat dft = new SimpleDateFormat("MM yyyy", Locale.ENGLISH);
			SimpleDateFormat dft2 = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
			Calendar cal = Calendar.getInstance();
			cal.add(cal.MONTH, 0);
			String currentMonth = dft.format(cal.getTime());
			Date currentTime = dft.parse(currentMonth);

			for (int i = 0; i < monthYearInCalendar.size(); i++) {
				monthYearInCalendar2.add(dft2.parse(monthYearInCalendar.get(i)));
			}

			if (currentTime.before(monthYearInCalendar2.get(1)) & currentTime.before(monthYearInCalendar2.get(2))) {
				SimpleUtils.pass("Current +2 month calendar is visible");
				flag = true;
			} else {
				flag = false;
				SimpleUtils.fail("month calendar is not current or future +2 ",true);
			}

		} else
			SimpleUtils.fail("calendar show error",true);
			return flag;
	}

	@Override
	public boolean isCurrentDateRed() throws Exception {
		String expectColor = "#fb7800";
		if (isElementLoaded(currentDateInCal,5)) {
			String color =  Color.fromString(currentDateInCal.getCssValue("color")).asHex();
			if (color.equals(expectColor)) {
				SimpleUtils.pass("Current Date is in Red color");
				return true;
			}
		}
		return false;
	}

	@Override
	public void verifyNavigation() throws Exception {
		ArrayList<String> monthYearInCalendar = getOverviewCalendarMonthsYears();
		if (isElementLoaded(arrowLeftInCal,10)) {
			click(arrowLeftInCal);
			ArrayList<String> monthYearInCalendarAftBack = getOverviewCalendarMonthsYears();
			if (!monthYearInCalendarAftBack.equals(monthYearInCalendar)) {
				SimpleUtils.pass("\"<\" Button can navigate to previous month calendar");
			}
			click(arrowRightInCal);
			click(arrowRightInCal);
			ArrayList<String> monthYearInCalendarAftForeword = getOverviewCalendarMonthsYears();
			if (!monthYearInCalendarAftForeword.equals(monthYearInCalendar)) {
				SimpleUtils.pass("\">\" button is enabled and navigating to future month calendar");
			}
			click(arrowLeftInCal);
		}else
			SimpleUtils.fail("arrow in calendar load faild",true);
	}


	//added by haya.  return a List has 4 week's data including last week
	@FindBy (css = ".row-fx.schedule-table-row.ng-scope")
	private List<WebElement> rowDataInOverviewPage;
	@FindBy (xpath = "//div[contains(@class,\"background-current-week-legend-calendar\")]/preceding-sibling::div[1]")
	private WebElement lastWeekNavigation;
	@FindBy (css = "i.fa-angle-left")
	private WebElement leftAngle;
	@Override
	public List<String> getOverviewData() throws Exception {
		List<String> resultList = new ArrayList<String>();
		if(isElementLoaded(leftAngle,10)){
			click(leftAngle);
			if (isElementLoaded(lastWeekNavigation,10)){
				click(lastWeekNavigation);// click on last in overview page
			}
		}
		waitForSeconds(3);
		if (areListElementVisible(rowDataInOverviewPage,10)){
			for (int i=0;i<rowDataInOverviewPage.size();i++){
				String[] temp1 = rowDataInOverviewPage.get(i).getText().split("\n");
				String[] temp2 = Arrays.copyOf(temp1,8);
				resultList.add(Arrays.toString(temp2));
			}
		} else {
			SimpleUtils.fail("data on schedules widget fail to load!",false);
		}
		return resultList;
	}

	@Override
	public int getDaysBetweenFinalizeDateAndScheduleStartDate(String finalizeByDate, String scheduleStartDate) throws Exception {
		int days = 0;
		String finalizeByMonth = "";
		String finalizeByDay = "";
		String scheduleStartMonth = "";
		String scheduleStartDay = "";
		if (finalizeByDate.contains(" ") && finalizeByDate.split(" ").length == 4) {
			finalizeByMonth = finalizeByDate.split(" ")[2];
			finalizeByDay = finalizeByDate.split(" ")[3];
		}
		if (scheduleStartDate.contains(" ") && scheduleStartDate.split(" ").length == 2) {
			scheduleStartMonth = scheduleStartDate.split(" ")[0];
			scheduleStartDay = scheduleStartDate.split(" ")[1];
		}
		if (finalizeByMonth.toUpperCase().equals(scheduleStartMonth))
			days = Integer.valueOf(scheduleStartDay) - Integer.valueOf(finalizeByDay);
		else
			days = Integer.valueOf(scheduleStartDay) + 31 - Integer.valueOf(finalizeByDay);
		return days;
	}


	public void clickOnLastWeek() throws Exception {
		if(isElementLoaded(leftAngle,10)){
			click(leftAngle);
		}
		if (isElementLoaded(lastWeekNavigation,10)){
			click(lastWeekNavigation);// click on last in overview page
		}
	}


	@FindBy (css = "[label=\"View Group Schedule\"] button")
	private WebElement viewGroupScheduleButton;
	public void clickOnViewGroupScheduleButton() throws Exception {
		if(isElementLoaded(viewGroupScheduleButton,10)){
			click(viewGroupScheduleButton);
			SimpleUtils.pass("Click on View Group Schedule Button successfully! ");
		} else
			SimpleUtils.fail("The View Group Schedule Button fail to load! ", false);
	}

	@Override
	public String getCurrentWeekBudgetHours() throws Exception {
		String budgetHours;
		WebElement currentWeek = overviewTableRows.get(0);
		if (isElementLoaded(currentWeek)) {
			budgetHours = currentWeek.findElement(By.cssSelector("[ng-if = \"hasBudget\"] [ng-if = \"row.budgetHours\"]")).getText();
			SimpleUtils.pass("Catch the budget hours successfully!");
			return budgetHours;
		}else{
			SimpleUtils.fail("The current week is not listed on the first line",false);
		}
		return null;
	}

	@FindBy(css="[ng-class=\"warningTextClass(row)\"]")
	List<WebElement> scheduleOverviewWeeksStatusWarningMessage;
	@Override
	public List<String> getScheduleWeeksStatusWarningMessage() throws Exception{
		List<String> weeksStatusWarningMessage = new ArrayList<String>();
		if (loadScheduleTableInOverview()) {
			if (scheduleOverviewWeeksStatusWarningMessage.size() != 0) {
				for (WebElement warningMessage : scheduleOverviewWeeksStatusWarningMessage) {
					weeksStatusWarningMessage.add(warningMessage.getText());
				}
			}
		}
		else
			SimpleUtils.fail("The schedule view table failed to load",false);

		return weeksStatusWarningMessage;
	}
}
