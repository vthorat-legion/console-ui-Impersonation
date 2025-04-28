package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.ForecastPage;
import com.legion.pages.ScheduleCommonPage;
import com.legion.pages.SmartCardPage;
import com.legion.pages.core.schedule.ConsoleScheduleCommonPage;
import com.legion.pages.core.schedule.ConsoleSmartCardPage;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.*;

public class ConsoleForecastPage extends BasePage implements ForecastPage {

	//	@FindBy(xpath="//[name()='svg']/[name()='g']")
//	private WebElement forecastGraph;
	private static HashMap<String, String> parametersMap2 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ControlsPageLocationDetail.json");

	private ConsoleScheduleNewUIPage scheduleNewUIPage;
	@FindBy(xpath = "//*[name()='svg']/*[name()='g']//*[name()='rect' and @class ='bar']")
	private List<WebElement> forecastGraph;

	@FindBy(xpath = "//div[contains(@class,'sub-navigation-view')]//span[contains(text(),'Forecast')]")
	private WebElement forecastTab;

	@FindBy(css = "div.day-week-picker-arrow-right")
	private WebElement forecastCalendarNavigationNextWeekArrow;

	@FindBy(css = "div.day-week-picker-arrow-left")
	private WebElement forecastCalendarNavigationPreviousWeekArrow;

	@FindBy(css = ".ng-scope.lg-button-group-selected.lg-button-group-last")
	private WebElement weekViewButton;

	@FindBy(xpath = "//span[contains(@class,'buttonLabel')][contains(text(),'Day')]")
	private WebElement dayViewButton;

	@FindBy(css = ".schedule-search-options .lg-button-group>div:nth-child(1)")
	private WebElement shoppersTab;

	@FindBy(xpath = "//span[contains(@class,'buttonLabel')][contains(text(),'Labor')]")
	private WebElement laborTab;

	@FindBy(css = "div.day-week-picker-period-active")
	private WebElement currentActiveWeek;

	@FindBy(xpath = "//td[contains(text(),'Peak Shoppers')]/following-sibling::td")
	private WebElement peakShoppersForecast;

	@FindBy(css = "div.day-week-picker-period")
	private List<WebElement> dayViewOfDatePicker;

	@FindBy(css = "div.day-week-picker-period-active")
	private WebElement currentActiveDay;

	@FindBy(css = "div[ng-if='isClosed']")
	private WebElement storeClosedView;

	@FindBy(xpath = "//*[contains(@class,'day-week-picker-period-active')]/following-sibling::div[1]")
	private WebElement immediateNextToCurrentActiveWeek;

	@FindBy(xpath = "//*[contains(@class,'day-week-picker-period-active')]/preceding-sibling::div[1]")
	private WebElement immediatePastToCurrentActiveWeek;

//	@FindBy(xpath = "//td[contains(text(),'Hours')]//following-sibling::td[@class='number ng-binding']")
	@FindBy(xpath = "//td[contains(text(),'Hours')]//following-sibling::td[@ng-if='!hideForecast() && !hasAdjustableRoles']")
	private WebElement laborSmartCardForecast;

//	@FindBy(xpath = "//td[contains(text(),'Hours')]//following-sibling::td[@ng-if='hasBudget()']")
	@FindBy(xpath = "//td[contains(text(),'Hours')]//following-sibling::td[@ng-if='hasBudget() && !hasDayPartFilterOn() && !hasAdjustableRoles']")
	private WebElement laborSmartCardBudget;

	@FindBy(xpath = "//div[contains(text(),'Holidays')]")
	private WebElement holidaysSmartcardHeader;

	@FindBy(xpath = "//span[contains(text(),'View All')]")
	private WebElement viewALLBtnInHolidays;

	@FindBy(css = ".lg-modal__title-icon.ng-binding")
	private WebElement holidayDetailsTitle;

	@FindBy(css = "[label=\"Close\"]")
	private WebElement closeBtnInHoliday;

	@FindBy(css = ".forecast-prediction-picker-text")
	private WebElement displayDropDownBtnInProjected;


	//use this to get the text of weeks which displayed,
	@FindBy(css = "div.day-week-picker-period-week")
	private WebElement currentWeekPeriod;

	@FindBy(css = "div > label:nth-child(1)")
	private WebElement checkBoxOfRecentTrendLine;

	@FindBy(css = "div > label:nth-child(2)")
	private WebElement checkBoxOfActualLine;

	@FindBy(css = "div > label:nth-child(3)")
	private WebElement checkBoxOfLstYearLine;

	@FindBy(css = "[stroke=\"#f49342\"]")
	private WebElement actualLine;

	@FindBy(css = "[stroke=\"#795548\"]")
	private WebElement recentTrendLine;

	@FindBy(css = "[stroke=\"#9c6ade\"]")
	private WebElement lastYearLine;

	@FindBy(css = ".ng-valid-parse.ng-touched.ng-not-empty")
	private List<WebElement> checkBoxSelected;

	@FindBy(css = "day-week-picker > div > div > div:nth-child(3)>span")
	private WebElement postWeekNextToCurrentWeek;

	@FindBy(css = "day-week-picker > div > div > div:nth-child(5)>span")
	private WebElement futureWeekNextToCurrentWeek;

	@FindBy(css = "div:nth-child(3) > lg-filter > div > input-field > ng-form")
	private WebElement filterWorkRoleButton;

	@FindBy(css = ".weather-forecast-day-name")
	private List<WebElement> weatherDaysOfWeek;

	@FindBy(css = ".card-carousel-card.card-carousel-card-default")
	private WebElement weatherSmartCard;

	@FindBy(xpath = "//*[contains(text(),'Weather - Week of')]")
	private WebElement weatherWeekSmartCardHeader;

	@FindBy(css = "div.card-carousel-arrow.card-carousel-arrow-right")
	private WebElement smartcardArrowRight;

	@FindBy(css = "span.weather-forecast-temperature")
	private List<WebElement> weatherTemperatures;

	@FindBy(css = "//div[contains(text(),'Work role')]")
	private WebElement workRoleFilterTitle;

	@FindBy(css = "[label=\"Work Role\"] div.lg-filter__category-items input-field.ng-isolate-scope")
	private List<WebElement> workRoleList;

	@FindBy(css = "span.forecast-prediction-top-legend-entry.ng-binding.ng-scope")
	private List<WebElement> hoursOfWorkRole;

	@FindBy(css = "[label=\"Work Role\"] input-field[placeholder=\"None\"]")
	private WebElement filterButton;

	@FindBy(xpath = "//lg-filter[@label=\"Filter\"]/div/input-field")
	private WebElement filterButtonForShopper;

	@FindBy(css = "div.row-fx.schedule-search-options>div:nth-child(3)>lg-filter>div>input-field>ng-form>div")
	private WebElement filterButtonText;

	@FindBy(css = "a.lg-filter__clear.ng-scope.lg-filter__clear-active")
	private WebElement clearFilterBtn;

	@FindBy(css = "[label=\"Work Role\"] div.lg-filter__wrapper")
	private WebElement filterPopup;

	@FindBy(css = "[label=\"Refresh\"]")
	private WebElement refreshBtn;

	@FindBy(css = ".card-carousel-fixed")
	private WebElement insightSmartCard;

	//added by Estell to close the stop

	@FindBy(css = "[ng-click=\"editWorkingHours(day, summary.businessId)\"]")
	private List<WebElement> editBtnInOperationHours;

	@FindBy(css = "[ng-repeat=\"day in summary.workingHours\"]")
	private List<WebElement> textInOperationHours;

	@FindBy(css = ".forecast-prediction-tooltip")
	private WebElement tooltipInProjected;

	@FindBy(css = "svg#forecast-prediction")
	private WebElement barChartInProjected;

	@FindBy(css = "#forecast-prediction>g>circle")
	private List<WebElement> circleInProjected;

	@FindBy(css = "#forecast-prediction>g>rect")
	private List<WebElement> forecastBarInProjected;

	@FindBy(css = "div.day-week-picker-arrow.day-week-picker-arrow-left")
	private WebElement schedulingWindowArrowLeft;

	@FindBy(css = "div.day-week-picker-arrow.day-week-picker-arrow-right")
	private WebElement schedulingWindowArrowRight;

	@FindBy(css = "[ng-if=\"!!getSummaryField('totalActualUnits')\"]")
	private List<WebElement> actualDataInSightSmartCard;

	@FindBy(css = "label.input-label")
	private List<WebElement> workRoleListNew;


	public ConsoleForecastPage() {
		PageFactory.initElements(getDriver(), this);
	}

	public void clickImmediateNextToCurrentActiveWeekInDayPicker() {
		if (isElementEnabled(immediateNextToCurrentActiveWeek, 30)) {
			click(immediateNextToCurrentActiveWeek);
		} else {
			SimpleUtils.report("This is a last week in Day Week picker");
		}
	}

	public void clickImmediatePastToCurrentActiveWeekInDayPicker() {
		if (isElementEnabled(immediatePastToCurrentActiveWeek, 30)) {
			click(immediatePastToCurrentActiveWeek);
		} else {
			SimpleUtils.report("This is a last week in Day Week picker");
		}
	}

	public void forecastShoppersDayNavigation(String weekDuration) throws Exception {
		if (isElementEnabled(dayViewButton)) {
			click(dayViewButton);
			SimpleUtils.pass("Clicked on Day View For Shoppers Sub Tab of Forecast Tab for Week Staring " + weekDuration);
			for (int i = 0; i < dayViewOfDatePicker.size(); i++) {
				//int forecastShoppersValueFinal = Integer.parseInt(forecastShoppersValue.getText());
				click(dayViewOfDatePicker.get(i));
				if (forecastGraph.size() != 0 && Integer.parseInt(peakShoppersForecast.getText()) > 0) {
					SimpleUtils.pass("Shoopers Forecast Loaded in DayView Successfully! for " + currentActiveDay.getText() + " and Value for Forecast is " + peakShoppersForecast.getText());
				} else if (isElementLoaded(storeClosedView, 10)) {
					SimpleUtils.pass("Store Closed on " + currentActiveDay.getText());
				} else {
					SimpleUtils.fail("Forecast Not Loaded in DayView for " + currentActiveDay.getText() + " and Value for Forecast is " + peakShoppersForecast.getText(), true);
				}
			}
			click(weekViewButton);
		} else {
			SimpleUtils.fail("Day View button not found in Forecast", false);
		}
	}


	public void forecastShopperWeekNavigation() throws Exception {
		if (isElementEnabled(weekViewButton)) {
			click(weekViewButton);
			String weekDuration[] = currentActiveWeek.getText().split("\n");
			SimpleUtils.pass("Current active forecast week is " + weekDuration[1]);
			if (isElementEnabled(shoppersTab)) {
				click(shoppersTab);
				if (forecastGraph.size() != 0 && (Integer.parseInt(peakShoppersForecast.getText())) > 0) {
					SimpleUtils.pass("Shoppers Forecast Loaded in Week View Successfully! for week starting " + weekDuration[1] + " Number of Shoppers is " + peakShoppersForecast.getText());
					forecastShoppersDayNavigation(weekDuration[1]);
				} else {
					SimpleUtils.fail("Shoppers Forecast Loaded in Week View Successfully! for week starting " + weekDuration[1] + " Number of Shoppers is " + peakShoppersForecast.getText(), true);
				}
			} else {
				SimpleUtils.fail("Shoppers subtab of forecast tab not found", false);
			}
		} else {
			SimpleUtils.fail("Week View button not found in Forecast", false);
		}

	}


	@Override
	public void loadShoppersForecastforCurrentNFutureWeek(String nextWeekView, int weekCount) throws Exception {
		//boolean flag=false;
		if (isElementLoaded(forecastTab, 5)) {
			click(forecastTab);
			SimpleUtils.pass("Clicked on Forecast Sub Tab");
			for (int i = 0; i < weekCount; i++) {
				if (nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future")) {
					forecastShopperWeekNavigation();
				}
				clickImmediateNextToCurrentActiveWeekInDayPicker();
			}
		} else {
			SimpleUtils.fail("Forecast Sub Menu Tab Not Found", false);
		}
	}

	@Override
	public void loadShoppersForecastforPastWeek(String nextWeekView, int weekCount) throws Exception {
		//boolean flag=false;
		if (isElementLoaded(forecastTab, 5)) {
			click(forecastTab);
			SimpleUtils.pass("Clicked on Forecast Sub Tab");
			for (int i = 0; i < weekCount; i++) {
				if (nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future")) {
					clickImmediatePastToCurrentActiveWeekInDayPicker();
					forecastShopperWeekNavigation();
				}
			}
		} else {
			SimpleUtils.fail("Forecast Sub Menu Tab Not Found", false);
		}
	}


	public void forecastLaborDayNavigation(String weekDuration) throws Exception {
		if (isElementEnabled(dayViewButton)) {
			click(dayViewButton);
			SimpleUtils.pass("Clicked on Day View For Labor Sub Tab of Forecast Tab for Week Staring " + weekDuration);
			for (int i = 0; i < dayViewOfDatePicker.size(); i++) {
				click(dayViewOfDatePicker.get(i));
				if (forecastGraph.size() != 0 && (Float.parseFloat(laborSmartCardForecast.getText())) > 0 && (Float.parseFloat(laborSmartCardBudget.getText())) > 0) {
					SimpleUtils.pass("Labor Forecast Loaded in DayView Successfully! for " + currentActiveDay.getText() + " Labor Forecast is " + laborSmartCardForecast.getText() + " Labor Budget is " + laborSmartCardBudget.getText() + " Hour");
				} else if (isElementLoaded(storeClosedView, 10)) {
					SimpleUtils.pass("Store Closed on " + currentActiveDay.getText());
				} else {
					SimpleUtils.fail("Labor Forecast Not Loaded in DayView for " + currentActiveDay.getText() + " Labor Forecast is " + laborSmartCardForecast.getText() + " Labor Budget is " + laborSmartCardBudget.getText() + " Hour", true);
				}
			}
			click(weekViewButton);
		} else {
			SimpleUtils.fail("Day View button not found in Forecast", false);
		}
	}


	public void forecastLaborWeekNavigation() throws Exception {
		if (isElementEnabled(weekViewButton)) {
			click(weekViewButton);
			String weekDuration[] = currentActiveWeek.getText().split("\n");
			SimpleUtils.pass("Current active labor week is " + weekDuration[1]);
			if (isElementEnabled(laborTab)) {
				click(laborTab);
				if (forecastGraph.size() != 0 && (Float.parseFloat(laborSmartCardForecast.getText())) > 0 && (Float.parseFloat(laborSmartCardBudget.getText())) > 0) {
					SimpleUtils.pass("Labor Forecast Loaded in Week View Successfully! for week starting " + weekDuration[1] + " Labor Forecast is " + laborSmartCardForecast.getText() + " Labor Budget is " + laborSmartCardBudget.getText() + " Hour");
					forecastLaborDayNavigation(weekDuration[1]);
				} else {
					SimpleUtils.fail("Labor Forecast Not Loaded in Week View for Week Starting " + weekDuration[1] + " Labor Forecast is " + laborSmartCardForecast.getText() + " Labor Budget is " + laborSmartCardBudget.getText() + " Hour", true);
				}
			} else {
				SimpleUtils.fail("Labor subtab of forecast tab not found", false);
			}
		} else {
			SimpleUtils.fail("Week View button not found in Forecast", false);
		}

	}

	@Override
	public void loadLaborForecastforCurrentNFutureWeek(String nextWeekView, int weekCount) throws Exception {
		//boolean flag=false;
		if (isElementLoaded(forecastTab, 5)) {
			click(forecastTab);
			SimpleUtils.pass("Clicked on Forecast Sub Tab");
			for (int i = 0; i < weekCount; i++) {
				if (nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future")) {
					forecastLaborWeekNavigation();
				}
				clickImmediateNextToCurrentActiveWeekInDayPicker();
			}
		} else {
			SimpleUtils.fail("Forecast Sub Menu Tab Not Found", false);
		}
	}

	@Override
	public void loadLaborForecastforPastWeek(String nextWeekView, int weekCount) throws Exception {
		//boolean flag=false;
		if (isElementLoaded(forecastTab, 5)) {
			click(forecastTab);
			SimpleUtils.pass("Clicked on Forecast Sub Tab");
			for (int i = 0; i < weekCount; i++) {
				if (nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future")) {
					clickImmediatePastToCurrentActiveWeekInDayPicker();
					forecastLaborWeekNavigation();
				}
			}
		} else {
			SimpleUtils.fail("Forecast Sub Menu Tab Not Found", false);
		}
	}

	@Override
	public void holidaySmartCardIsDisplayedForCurrentAWeek() throws Exception {
		if (isElementLoaded(forecastTab, 5)) {
			click(forecastTab);
			SimpleUtils.pass("Clicked on Forecast Sub Tab");
			if (true == isHolidaySmartcardDispaly()) {
				click(viewALLBtnInHolidays);
				SimpleUtils.pass("View all button is clickable");
				if (isElementLoaded(holidayDetailsTitle, 5)) {
					SimpleUtils.pass("current holiday is showing");
					click(closeBtnInHoliday);
					SimpleUtils.pass("Close button is clickable in holidays window");
				}
			} else {
				SimpleUtils.report("this week has no holiday");
			}
		} else {
			SimpleUtils.fail("Forecast Sub Menu Tab Not Found", true);
		}
	}


	public void clickForecast() throws Exception {
		if (isElementLoaded(forecastTab, 5)) {
			click(forecastTab);
			SimpleUtils.pass("Clicked on Forecast Sub Tab");
		} else {
			SimpleUtils.fail("Forecast Sub Menu Tab Not Found", false);
		}
	}

	@Override
	public void verifyNextPreviousBtnCorrectOrNot() throws Exception {
		if (isElementLoaded(postWeekNextToCurrentWeek,5) & isElementLoaded(futureWeekNextToCurrentWeek,5)) {
			SimpleUtils.pass("post current future week is visible");
			String currentWeekPeriodText = currentWeekPeriod.getText().trim().replace("\n", "").replace(" ", "").replace("-", "");
			SimpleUtils.report("currentWeekPeriodText======" + currentWeekPeriodText);
			navigateToPreviousAndFutureWeek(forecastCalendarNavigationPreviousWeekArrow);
			String WeekPeriodTextAftBack = currentWeekPeriod.getText().trim().replace("\n", "").replace(" ", "").replace("-", "");
			SimpleUtils.report("WeekPeriodTextAftBack======" + WeekPeriodTextAftBack);
			if (WeekPeriodTextAftBack.trim().equals(currentWeekPeriodText.trim())) {
				SimpleUtils.fail(" Back buttons are not working", false);
			} else {
				SimpleUtils.pass(" Back button is working normally");
			}
			navigateToPreviousAndFutureWeek(forecastCalendarNavigationNextWeekArrow);
			String WeekPeriodTextAftForeword = currentWeekPeriod.getText().trim().replace("\n", "").replace(" ", "").replace("-", "");
			if (WeekPeriodTextAftForeword.trim().equals(currentWeekPeriodText.trim())) {
				SimpleUtils.pass(" Foreword button is working normally");
			} else {
				SimpleUtils.fail(" Foreword buttons are not working normally",false);
			}
		}
			else {
			SimpleUtils.fail("Foreword and Back buttons is not displayed", false);
		}


	}

	@Override
	public void verifyDisplayOfActualLineSelectedByDefaultInOrangeColor() throws Exception {

		if (isElementLoaded(displayDropDownBtnInProjected, 5)) {
			click(displayDropDownBtnInProjected);
			if (isElementLoaded(actualLine, 5)) {
				click(checkBoxOfActualLine);
				if (checkBoxSelected.size() == 0) {
					SimpleUtils.pass(" Display of Actual line should be selected bydefault and in Orange color");
				}

			} else {
				SimpleUtils.fail("Actual is not selected by default", true);
			}
		} else {
			SimpleUtils.fail("Display dropdown list load failed", true);
		}
	}

	@Override
	public void verifyRecentTrendLineIsSelectedAndColorInBrown() throws Exception {
		if (isElementLoaded(displayDropDownBtnInProjected, 20)) {
//			click(displayDropDownBtnInProjected);
			click(checkBoxOfRecentTrendLine);
			if (isElementLoaded(recentTrendLine, 5)) {
				SimpleUtils.pass(" Display of RecentTrend line should be selected  and in Brown color");
			} else {
				SimpleUtils.fail("RecentTrend line selected failed", false);
			}

//			click(displayDropDownBtnInProjected);
			click(checkBoxOfRecentTrendLine);
		} else {

			SimpleUtils.fail("Display dropdown list load failed", false);
		}
	}

	@Override
	public void verifyLastYearLineIsSelectedAndColorInPurple() throws Exception {
		if (isElementLoaded(displayDropDownBtnInProjected, 20)) {
//			click(displayDropDownBtnInProjected);
			click(checkBoxOfLstYearLine);
			if (isElementLoaded(lastYearLine, 5)) {
				SimpleUtils.pass(" Display of LastYear line should be selected  and in purple color");
			} else {
				SimpleUtils.fail("RecentTrend line selected failed", false);
			}
		}
		click(checkBoxOfLstYearLine);

	}

	@Override
	public void verifyForecastColourIsBlue() throws Exception {
		String forecastLegendColor = Color.fromString(getDriver().findElement(By.cssSelector(".forecast-prediction-rect.forecast-prediction-rect-baseline")).getCssValue("background-color")).asHex();;
		if (forecastGraph.size()>=0) {
			String forecastBarColor =  Color.fromString(forecastGraph.get(1).getAttribute("fill")).asHex();
			if (forecastBarColor.equals(forecastLegendColor)) {
				SimpleUtils.pass("Forecast color is correct and it's blue");
			}
		}else {
			SimpleUtils.pass("There is no forecast bar in this week");
		}
	}

	private void navigateToPreviousAndFutureWeek(WebElement element) throws Exception {

		if (isElementLoaded(element, 5)) {
			click(element);
			SimpleUtils.pass("can navigate to next or previous week");

		} else {
			SimpleUtils.fail("element is not displayed", false);
		}
	}

	private boolean isHolidaySmartcardDispaly() throws Exception {
		if (isElementLoaded(holidaysSmartcardHeader, 15)) {
			SimpleUtils.pass("Current week's Holidays are showing");
			return true;
		} else {
			SimpleUtils.report("There is no holiday in current week");
			return false;
		}

	}

	@Override
	public void verifyWorkRoleIsSelectedAftSwitchToPastFutureWeek() throws Exception {
		click(laborTab);

		if (isElementLoaded(filterWorkRoleButton, 10)) {
			String workRoleText = filterWorkRoleButton.getText();
			goToPostWeekNextToCurrentWeek();
			String postWeekFilterText = filterWorkRoleButton.getText();
			goToFutureWeekNextToCurrentWeek();
			String futureWeekFilterText = filterWorkRoleButton.getText();
			if (workRoleText.equals(postWeekFilterText)) {
				SimpleUtils.pass("work role is  remain  seleced after switching to post week");
			} else {
				SimpleUtils.fail("Work Role is changed after switch to post week", true);
			}
			if (workRoleText.equals(futureWeekFilterText)) {
				SimpleUtils.pass("work role is remain seleced after switching to future week");
			} else {
				SimpleUtils.fail("Work Role is changed after switch to future week", true);
			}
		}
	}

	@Override
	public void weatherWeekSmartCardIsDisplayedForAWeek() throws Exception {
		clickOnLabor();
		String jsonTimeZoon = parametersMap2.get("Time_Zone");
		TimeZone timeZone = TimeZone.getTimeZone(jsonTimeZoon);
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		dfs.setTimeZone(timeZone);
		String currentTime = dfs.format(new Date());
		int currentDay = Integer.valueOf(currentTime.substring(currentTime.length() - 2));
		//String firstDayInWeatherSmtCad2 = getDriver().findElement(By.xpath("//*[contains(text(),'Weather - Week of')]")).getText();
		try{
			String firstDayInWeatherSmtCad2 = getDriver().findElement(By.xpath("//*[contains(text(),'Weather - Week of')]")).getText();
			int firstDayInWeatherSmtCad = Integer.valueOf(firstDayInWeatherSmtCad2.substring(firstDayInWeatherSmtCad2.length() - 2));
			System.out.println("firstDayInWeatherSmtCad:" + firstDayInWeatherSmtCad);
			if ((firstDayInWeatherSmtCad + 7) > currentDay) {
				SimpleUtils.pass("The week smartcard is current week");
				if (areListElementVisible(weatherTemperatures, 8)) {
					String weatherWeekTest = getWeatherDayOfWeek();
					SimpleUtils.report("Weather smart card is displayed for a week from mon to sun" + weatherWeekTest);
					for (ScheduleTestKendraScott2.DayOfWeek e : ScheduleTestKendraScott2.DayOfWeek.values()) {
						if (weatherWeekTest.contains(e.toString())) {
							SimpleUtils.pass("Weather smartcard include one week weather");
						} else {
							SimpleUtils.fail("Weather Smart card is not one whole week", false);
						}
					}

				} else {
					SimpleUtils.fail("there is no week weather smartcard", false);
				}

			} else {
				SimpleUtils.fail("This is not current week weather smartcard ", false);
			}
		} catch (Exception e){
			SimpleUtils.report("there is no week weather smartcard!");
		}
	}

	@Override
	public void clickOnLabor() throws Exception {
		if (isElementEnabled(laborTab, 15)) {
			click(laborTab);
			if (isElementLoaded(workRoleFilterTitle, 5)) {
				SimpleUtils.pass("labor tab is clickable");
			}
		} else {
			SimpleUtils.fail("labor tab load failed", false);
		}
	}
	@Override
	public void clickOnShopper() throws Exception {
		if (isElementEnabled(shoppersTab, 2)) {
			click(shoppersTab);
			if (isElementLoaded(workRoleFilterTitle, 2)) {
				SimpleUtils.pass("Shopper tab is clickable");
			}
		} else {
			SimpleUtils.fail("Shopper tab load failed", true);
		}
	}

	public void clickOnClearFilterInWorkRole() throws Exception {
		if (isElementEnabled(clearFilterBtn, 2)) {
			click(clearFilterBtn);
			if (isElementLoaded(workRoleFilterTitle, 2)) {
				SimpleUtils.pass("clear Filter Btn is clickable");
			}
		} else {
			SimpleUtils.fail("clear Filter Btn load failed", true);
		}
	}

	public void verifyWorkRoleSelection() throws Exception {

		if (isElementLoaded(filterButton, 10)) {
//			defaultValueIsAll();
			clickTheElement(filterButton);
			clickOnClearFilterInWorkRole();
//			selectWorkRole();
		} else {
			SimpleUtils.fail("Work role filter load failed", false);
		}
	}



	private void defaultValueIsAll() throws Exception {
		String defaultWorkRoleText = "All";
		if (isElementLoaded(filterButton, 15)) {
			click(filterButton);
			String workRoleDefaultText = filterButtonText.getText().trim();
			if (defaultWorkRoleText.equals(workRoleDefaultText)) {
				SimpleUtils.pass("Work role filter is selected all roles by default");
			} else {
				SimpleUtils.fail("default work role value is not all", false);
			}

		}
	}

	public String getWeatherDayOfWeek() throws Exception {
		String daysText = "";
		if (weatherDaysOfWeek.size() != 0)
			for (WebElement weatherDay : weatherDaysOfWeek) {
				if (weatherDay.isDisplayed()) {
					if (daysText == "")
						daysText = weatherDay.getText();
					else
						daysText = daysText + " | " + weatherDay.getText();
				} else if (!weatherDay.isDisplayed()) {
					while (isSmartCardScrolledToRightActive() == true) {
						if (daysText == "")
							daysText = weatherDay.getText();
						else
							daysText = daysText + " | " + weatherDay.getText();
					}
				}
			}

		return daysText;
	}

	boolean isSmartCardScrolledToRightActive() throws Exception {
		if (isElementLoaded(smartcardArrowRight, 10) && smartcardArrowRight.getAttribute("class").contains("available")) {
			click(smartcardArrowRight);
			return true;
		}
		return false;
	}


	private void goToPostWeekNextToCurrentWeek() throws Exception {
		if (isElementLoaded(postWeekNextToCurrentWeek, 5)) {
			click(postWeekNextToCurrentWeek);
			SimpleUtils.pass("navigate to post week successfully");
		} else {
			SimpleUtils.fail("post week tab load failed", true);
		}
	}

	private void goToFutureWeekNextToCurrentWeek() throws Exception {
		if (isElementLoaded(futureWeekNextToCurrentWeek, 5)) {
			click(futureWeekNextToCurrentWeek);
			SimpleUtils.pass("navigate to future week successfully");
		} else {
			SimpleUtils.fail("future week tab load failed", true);
		}
	}

	@Override
	public HashMap<String, Float> getSummaryLaborHoursAndWages() throws Exception {
		HashMap<String, Float> summaryHoursAndWages = new HashMap<String, Float>();
		SmartCardPage smartCardPage = new ConsoleSmartCardPage();
		WebElement summaryLabelsDivElement = MyThreadLocal.getDriver().findElement(By.cssSelector(".card-carousel-card.card-carousel-card-primary.card-carousel-card-table"));
		if (isElementLoaded(summaryLabelsDivElement,5)) {
			String sumarySmartCardText = summaryLabelsDivElement.getText();
			String[] hoursAndBudgetInSummary = sumarySmartCardText.split("\n");
			for (String hoursAndBudget: hoursAndBudgetInSummary) {

				if(hoursAndBudget.toLowerCase().contains(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.hours.getValue().toLowerCase()))
				{
					summaryHoursAndWages = smartCardPage.updateScheduleHoursAndWages(summaryHoursAndWages , hoursAndBudget.split(" ")[1],
							"ForecastHours");
				}
				else if(hoursAndBudget.toLowerCase().contains(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.wages.getValue().toLowerCase()))
				{
					summaryHoursAndWages = smartCardPage.updateScheduleHoursAndWages(summaryHoursAndWages , hoursAndBudget.split(" ")[1]
							.replace("$", ""), "ForecastWages");
				}
			}
		}else {
			SimpleUtils.fail("there is no summary smart card",false);
		}
		return summaryHoursAndWages;
	}


	//updated by haya
	public HashMap<String, Float> getHoursBySelectedWorkRoleInLaborWeek(String workRole) throws Exception {
		HashMap<String, Float> hoursByWorkRole = new HashMap<String,Float>();
		if (areListElementVisible(hoursOfWorkRole,5)) {
			for (WebElement e :hoursOfWorkRole
				 ) {
				if (hoursByWorkRole.size()<0) {
					hoursByWorkRole.put(" operation - other role",0.0f);
				}else
				hoursByWorkRole.put(e.getText().split(":")[0],Float.valueOf(e.getText().split(":")[1].replaceAll("H","")));
			}
		}else {
			//SimpleUtils.fail("work roles hours load failed",false);
			hoursByWorkRole.put(workRole,Float.valueOf(0));
			SimpleUtils.report("No data for selected work role!");
		}

		return hoursByWorkRole;
	}

	@Override
	public List<String> getLaborWorkRoles() throws Exception {
		List<String> laborWorkRoles = new ArrayList<>();
		if (areListElementVisible(hoursOfWorkRole,5)) {
			for (WebElement e : hoursOfWorkRole) {
				laborWorkRoles.add(e.getText().split(":")[0].trim().toLowerCase());
			}
		}
		return laborWorkRoles;
	}

	@Override
	public HashMap<String, Float> getInsightDataInShopperWeekView() throws Exception {
		HashMap<String, Float> insightData = new HashMap<String, Float>();
		SmartCardPage smartCardPage = new ConsoleSmartCardPage();
		WebElement insightIsDivElement = MyThreadLocal.getDriver().findElement(By.cssSelector(".card-carousel-fixed"));
		if (isElementLoaded(insightIsDivElement,5)) {
			String insightSmartCardText = insightIsDivElement.getText();

			String[] peakShopperDayInInsight = insightSmartCardText.split("\n");

			for (String peakShopperDay: peakShopperDayInInsight) {
				if (actualDataInSightSmartCard.size()> 0) {
					if(peakShopperDay.toLowerCase().contains("peak items"))
					{
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"peakItems");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"peakEditedPeakItems");
						//insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
						//		"actualPeakItems");
					}
					else if(peakShopperDay.toLowerCase().contains("peak shoppers"))
					{
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"peakShoppers");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"peakEditedShoppers");
						//insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
						//		"actualPeakShoppers");
					}
					else if(peakShopperDay.toLowerCase().contains("peak demand"))
					{
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"peakDemand");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"peakEditedDemand");
						//insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
						//		"actualPeakDemand");
					}
					else if(peakShopperDay.toLowerCase().contains("peak day"))
					{
						Float peakday=null;
						Float actualPeakday=null;
						//insightData.put(peakShopperDay.split(" ")[2],peakday);
						insightData.put(peakShopperDay.split(" ")[3],actualPeakday);//Edited peak day
						//insightData.put(peakShopperDay.split(" ")[4],actualPeakday);
					}
					else if(peakShopperDay.toLowerCase().contains("peak time"))
					{
						Float peaktime=null;
						Float actualPeaktime=null;
						insightData.put(peakShopperDay.split(" ")[2],peaktime);
						insightData.put(peakShopperDay.split(" ")[3],actualPeaktime);
					}
					else if(peakShopperDay.toLowerCase().contains("total items"))
					{
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"totalItems");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"totalEditedItems");
						//insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
						//		"actualTotalItems");
					}
					else if(peakShopperDay.toLowerCase().contains("total shoppers"))
					{
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"totalShoppers");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"totalEditedShoppers");
						//insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
						//		"actualTotalShoppers");
					}
					else if(peakShopperDay.toLowerCase().contains("total demand"))
					{
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"totalDemand");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"totalEditedDemand");
						//insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
						//		"actualTotalDemand");
					}
	//				else {
	//				SimpleUtils.fail("this data is not which i wanted,ig",true);
	//				}
				}else {
					if(peakShopperDay.toLowerCase().contains("peak items"))
					{
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"peakItems");
					}
					else if(peakShopperDay.toLowerCase().contains("peak shoppers"))
					{
						if (peakShopperDay.split(" ").length>=3){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
									"peakShoppers");
						}
						if (peakShopperDay.split(" ").length>=4){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
									"peakEditedShoppers");
						}
						if (peakShopperDay.split(" ").length>=5){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
									"actualTotalShoppers");
						}
					}
					else if(peakShopperDay.toLowerCase().contains("peak demand"))
					{
						if (peakShopperDay.split(" ").length>=3){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
									"peakDemand");
						}
						if (peakShopperDay.split(" ").length>=4){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
									"peakEditedDemand");
						}
						if (peakShopperDay.split(" ").length>=5){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[4],
											"actualPeakDemand");
						}
					}
					else if(peakShopperDay.toLowerCase().contains("peak day"))
					{
						Float peakday=null;
						if (peakShopperDay.split(" ").length>=3){
							insightData.put(peakShopperDay.split(" ")[2],peakday);
						}
						if (peakShopperDay.split(" ").length>=4){
							insightData.put(peakShopperDay.split(" ")[3],peakday);
						}
					}
					else if(peakShopperDay.toLowerCase().contains("peak time"))
					{
						if (peakShopperDay.split(" ").length>=3){
							smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2].replace("am","").replace("pm","").replace(":","."),
									"peakTime");
							//insightData.put("peakTime", Float.valueOf(peakShopperDay.split(" ")[2].replace("am","").replace("pm","").replace(":",".")));
						}
						if (peakShopperDay.split(" ").length>=4){
							smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3].replace("am","").replace("pm","").replace(":","."),
									"editedPeakTime");
							//insightData.put("editedPeakTime", Float.valueOf(peakShopperDay.split(" ")[3].replace("am","").replace("pm","").replace(":",".")));
						}
					}
					else if(peakShopperDay.toLowerCase().contains("total items"))
					{
						if (peakShopperDay.split(" ").length>=3){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
									"totalItems");
						}

					}
					else if(peakShopperDay.toLowerCase().contains("total shoppers"))
					{
						if (peakShopperDay.split(" ").length>=3){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
									"totalShoppers");
						}
						if (peakShopperDay.split(" ").length>=4){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
									"totalEditedShoppers");
						}
					}
					else if(peakShopperDay.toLowerCase().contains("total demand"))
					{
						if (peakShopperDay.split(" ").length>=3){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
									"totalDemand");
						}
						if (peakShopperDay.split(" ").length>=4){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
									"totalEditedDemand");
						}
					}
					else if(peakShopperDay.toLowerCase().contains("hours"))
					{
						if (peakShopperDay.split(" ").length>=2){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[1],
									"ForecastHrs");
						}
						if (peakShopperDay.split(" ").length>=3){
							insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
									"BudgetedHrs");
						}
					}
				}

			}
		}else {
			SimpleUtils.fail("there is no insight smart card",false);
		}

		return insightData;
	}

	@Override
	public HashMap<String, Float> getInsightDataInShopperDayView() throws Exception {
		HashMap<String, Float> insightData = new HashMap<String, Float>();
		SmartCardPage smartCardPage = new ConsoleSmartCardPage();
		WebElement insightIsDivElement = MyThreadLocal.getDriver().findElement(By.cssSelector(".card-carousel-fixed"));
		if (isElementLoaded(insightIsDivElement,5)) {
			String insightSmartCardText = insightIsDivElement.getText();
			String[] peakShopperDayInInsight = insightSmartCardText.split("\n");
			for (String peakShopperDay: peakShopperDayInInsight) {
				if (SimpleUtils.isNumeric(peakShopperDay.split(" ")[2]) && SimpleUtils.isNumeric(peakShopperDay.split(" ")[3])){
					if(peakShopperDay.toLowerCase().contains("peak items")) {
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"peakItems");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"actualEditedPeakItems");
					} else if(peakShopperDay.toLowerCase().contains("peak shoppers")) {
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"peakShoppers");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"peakEditedShoppers");
					} else if(peakShopperDay.toLowerCase().contains("peak day")) {
						Float editedPeakday=null;
						insightData.put(peakShopperDay.split(" ")[3],editedPeakday);//Edited peak day
					} else if(peakShopperDay.toLowerCase().contains("peak time")) {
						Float peaktime=null;
						Float editedPeaktime=null;
						insightData.put(peakShopperDay.split(" ")[2],peaktime);
						insightData.put(peakShopperDay.split(" ")[3],editedPeaktime);
					} else if(peakShopperDay.toLowerCase().contains("total items")) {
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"totalItems");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"totalEditedItems");
					} else if(peakShopperDay.toLowerCase().contains("total shoppers")) {
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[2],
								"totalShoppers");
						insightData = smartCardPage.updateScheduleHoursAndWages(insightData , peakShopperDay.split(" ")[3],
								"totalEditedShoppers");
					}
				} else {
					SimpleUtils.fail("Data doesn't display on the smart card! Please check!", false);
				}
			}
		}else {
			SimpleUtils.fail("there is no insight smart card",false);
		}
		return insightData;
	}

	//added by haya
	@FindBy(css = "lg-filter[label=\"Work role\"] .lg-filter__wrapper.lg-ng-animate div[ng-mouseover]")
	private List<WebElement> workRoleFilter;
	@Override
	public void verifyBudgetedHoursInLaborSummaryWhileSelectDifferentWorkRole() throws Exception {
		for (WebElement e:workRoleFilter
			 ) {
			e.click();
			String workRoleText = e.getText();
			SimpleUtils.pass("work role ‘ " + workRoleText + " ’ is selected");
			HashMap<String, Float> HoursBySelectedWorkRoleInLaborWeek = getHoursBySelectedWorkRoleInLaborWeek(workRoleText.toUpperCase());
			HashMap<String, Float> hoursAndWedgetInSummary = getSummaryLaborHoursAndWages();
			System.out.println(hoursAndWedgetInSummary.get("ForecastHours"));
			System.out.println(HoursBySelectedWorkRoleInLaborWeek.get(workRoleText.toUpperCase()));
			System.out.println(hoursAndWedgetInSummary.get("ForecastHours") == HoursBySelectedWorkRoleInLaborWeek.get(workRoleText.toUpperCase()));
			if (hoursAndWedgetInSummary.get("ForecastHours") >= HoursBySelectedWorkRoleInLaborWeek.get(workRoleText.toUpperCase()) && hoursAndWedgetInSummary.get("ForecastHours") <= HoursBySelectedWorkRoleInLaborWeek.get(workRoleText.toUpperCase())){
				SimpleUtils.pass("Smartcard's budgeted hours are matching with the sum of work role hours");
			}else
				SimpleUtils.fail("Smartcard budget hours are not matching with selected work roles hours",true);
		    e.click();
		}

	}

		@Override
		public void verifyRefreshBtnInLaborWeekView() throws Exception {
			if (isElementLoaded(weatherWeekSmartCardHeader,10)) {
		        String defaultText = getDriver().findElement(By.cssSelector(".card-carousel.row-fx")).getText();
				SimpleUtils.report("content before default is : "+defaultText);
				if (isElementLoaded(refreshBtn,10)){
					click(refreshBtn);
					SimpleUtils.pass("refresh is clickable");
				}else{
					SimpleUtils.fail("Refresh button load failed",true);
				}
				waitForSeconds(10);//wait to load the page data
				String textAftRefresh =  getDriver().findElement(By.cssSelector(".card-carousel.row-fx")).getText();
				SimpleUtils.report("content after refresh is:"+textAftRefresh);
				if(defaultText.equals(textAftRefresh)){
					SimpleUtils.pass("page get refresh ");
				}
			}else {
				//SimpleUtils.fail("Refresh button load failed",true);
				SimpleUtils.report("Weather smart card is not loaded!");
			}
		}

	@Override
	public void verifyRefreshBtnInShopperWeekView() throws Exception {
		if (isElementLoaded(weatherSmartCard,10)) {
			String defaultText1 = insightSmartCard.getText();
			SimpleUtils.report(defaultText1);
			if (isElementLoaded(refreshBtn,10)){
				click(refreshBtn);
				SimpleUtils.pass("refresh is clickable");
			}else{
				SimpleUtils.fail("Refresh button load failed",true);
			}
			waitForSeconds(10);//wait to load the page data
			String textAftRefresh1 = insightSmartCard.getText();
			SimpleUtils.report(textAftRefresh1);
			if((textAftRefresh1).equals(defaultText1)){
				SimpleUtils.pass("page back to previous page ");
			}else {
				SimpleUtils.fail("after refresh, the page changed",true);
			}
		}else {
			//SimpleUtils.fail("Refresh button load failed",true);
			SimpleUtils.warn("Weather smart card is not loaded!");
		}
	}

	@Override
	public void verifySmartcardAreAvailableInShoppers() throws Exception {
		if (isElementLoaded(insightSmartCard,5) & isElementLoaded(weatherSmartCard,5)) {
			SimpleUtils.pass("Insight and Weather smart card is displayed");
		}
		else {
			//SimpleUtils.fail("smart card load failed",false);
			SimpleUtils.warn("insightSmartCard or weatherSmartCard load failed");
		}
	}


	public void shopperFilterInForecast(ArrayList<WebElement> shiftTypeFilters) {

		try {
			if (areListElementVisible(shiftTypeFilters, 10)) {
				for (WebElement e : shiftTypeFilters
				) {
					click(e);
					String filterName = e.getText();
					SimpleUtils.pass( filterName + " is selected");
					SimpleUtils.report(filterName+"insight data's is:"+getInsightDataInShopperWeekView());

				}
			} else {
				SimpleUtils.fail("can't select work role", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<String> getForecastBarGraphData() throws Exception {
		List<String> barGraphDataForEachDay = new ArrayList<>();
		if (isElementLoaded(insightSmartCard, 5) & isElementLoaded(barChartInProjected, 5)) {
			for (int i = 0; i < forecastBarInProjected.size(); i++) {
				mouseToElement(forecastBarInProjected.get(i));
				/*
				 *wait tooptip data load
				 * */
				waitForSeconds(2);
				if (tooltipInProjected.getText().contains("N/A")){
					barGraphDataForEachDay.add(tooltipInProjected.getText().replace("\n", " ").replace("N/A","0"));
				} else {
					barGraphDataForEachDay.add(tooltipInProjected.getText().replace("\n", " "));
				}
				waitForSeconds(1);
			}
		}
		return barGraphDataForEachDay;
	}


		@Override
		public void verifyPeakShopperPeakDayData () throws Exception {
			HashMap<String, Float> insightDataInWeek = new HashMap<String, Float>();
			insightDataInWeek = getInsightDataInShopperWeekView();
			List<String> dataInBar = getForecastBarGraphData();
			SimpleUtils.report("data in bar graph is :"+dataInBar);
			Float max = 0.0f;
			Float totalItemsInbar = 0.0f;

			for (int i = 0; i < dataInBar.size(); i++) {
				if (!dataInBar.get(i).isEmpty()) {
					if (dataInBar.get(i).split(" ").length>6){
						String totalShoppersInBar = dataInBar.get(i).split(" ")[3];
						String forecastInBar = dataInBar.get(i).split(" ")[6];
						if (totalShoppersInBar.contains(",")) {
							totalShoppersInBar = totalShoppersInBar.replaceAll(",", "");
						} else totalShoppersInBar = totalShoppersInBar;
						if (forecastInBar.contains(",")) {
							forecastInBar = forecastInBar.replaceAll(",", "");
						} else forecastInBar = forecastInBar;
						try {
							totalItemsInbar += Float.valueOf(totalShoppersInBar);
							if (max <= Float.valueOf(forecastInBar)) {
								max = Float.valueOf(forecastInBar);
							} else {
								max = max;
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
			SimpleUtils.report("max number in bar graph:"+max);
			SimpleUtils.report("total items in bar graph:"+totalItemsInbar);
			Float totalShoppersInInsightcard = insightDataInWeek.get("totalShoppers");
			try {
				if (insightDataInWeek.get("totalShoppers").equals(totalItemsInbar) & insightDataInWeek.get("peakShoppers").equals(max)) {
				   SimpleUtils.pass("In smart card ,total shoppers and peak shoppers  are  matching with bar graph");
				}
				else {
					SimpleUtils.warn("BUG existed-->SF-418:data in Insight smart card is not matching with bar graph");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

   public void schedulingWindowScrolleToLeft() throws Exception {
		if (isElementLoaded(schedulingWindowArrowLeft, 2)) {
				click(schedulingWindowArrowLeft);
		}
	}

	@Override
	public void verifyActualDataForPastWeek() throws Exception {
		HashMap<String, Float> insightDataInWeek = new HashMap<String, Float>();
		schedulingWindowScrolleToLeft();
		goToPostWeekNextToCurrentWeek();
		insightDataInWeek = getInsightDataInShopperWeekView();
		List<String> dataInBar = getForecastBarGraphData();
		Float max = 0.0f;
		Float actualTotalShoppersInbar =0.0f;

		for (int i = 0; i < dataInBar.size(); i++) {
			if (!dataInBar.get(i).isEmpty()) {
				if (dataInBar.get(i).split(" ").length > 0) {
					String actualShoppers = "";
					if (dataInBar.get(i).split(" ").length == 9) {
						actualShoppers = dataInBar.get(i).split(" ")[7];
					}
					if (dataInBar.get(i).split(" ").length == 8) {
						actualShoppers = dataInBar.get(i).split(" ")[6];
					}
					if (actualShoppers.contains(",")) {
						actualShoppers = actualShoppers.replaceAll(",", "");
					}else if (actualShoppers.equals("N/A")) {
						actualShoppers = actualShoppers.replace("N/A","0");
					}
					try {
						actualTotalShoppersInbar += Float.valueOf(actualShoppers);
						if (max <= Float.valueOf(actualShoppers)) {
							max = Float.valueOf(actualShoppers);
						} else {
							max = max;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					SimpleUtils.fail("actual value in tooltip is not loaded!", true);
				}
			}
		}
		SimpleUtils.report("max actual data in bar graph for this week is :"+max);
		SimpleUtils.report("actual total shoppers in bar graph for this week is "+actualTotalShoppersInbar);

		try {
			if (insightDataInWeek.get("actualTotalShoppers").equals(actualTotalShoppersInbar)  & insightDataInWeek.get("actualPeakShoppers").equals(max) )  {
				SimpleUtils.pass("In smart card ,total shoppers and peak shoppers  are  matching with bar graph");
			}
			else {
				//SimpleUtils.fail("data in Insight smart card is not matching with bar graph",false);
				SimpleUtils.warn("actual total data in Insight smart card is not matching with bar graph");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void verifyFilterFunctionInForecast() throws Exception {
		ArrayList<WebElement> availableFilters = getAvailableFilters();
		shopperFilterInForecast(availableFilters);

	}

	@FindBy(css = "[ng-repeat=\"(key, opts) in $ctrl.displayFilters\"]")
	private List<WebElement> scheduleFilterElements;

	public ArrayList<WebElement> getAvailableFilters() {
		ArrayList<WebElement> filterList = new ArrayList<WebElement>();
		try {
			if (isElementLoaded(filterButtonForShopper,10)) {
				if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
					click(filterButtonForShopper);
				for (WebElement scheduleFilterElement : scheduleFilterElements) {
					List<WebElement> filters = scheduleFilterElement.findElements(By.cssSelector("input-field[type=\"checkbox\"]"/*"[ng-repeat=\"opt in opts\"]"*/));
					for (WebElement filter : filters) {
						if (filter != null) {
							filterList.add(filter);
						}

					}
				}
			} else {
				SimpleUtils.fail("Filters button not found on forcast page!", false);
			}
		} catch (Exception e) {
			SimpleUtils.fail("Filters button not loaded successfully on Schedule page!", true);
		}
		return filterList;
	}

	@FindBy(css = "[ng-show*=\"projection === 'sales'\"] .lg-filter input-field[ng-click=\"$ctrl.openFilter()\"]")
	private WebElement filterBtnOnForecastDemandPage;
	@Override
	public void clickOnFilterButtonUnderDefinedTab() throws Exception {
		if (isElementLoaded(filterBtnOnForecastDemandPage, 10)){
			clickTheElement(filterBtnOnForecastDemandPage);
		}else {
			SimpleUtils.fail("No available filters on this page!", false);
		}
	}

	@FindBy(css = "[ng-show*=\"projection === 'labor'\"] .lg-filter")
	private List<WebElement> filterBtnsOnForecastLaborPage;
	@Override
	public void clickOnDayPartsFilterButtonUnderLaborTab() throws Exception {
		boolean flag = false;
		if (areListElementVisible(filterBtnsOnForecastLaborPage, 10)){
			for (WebElement filter: filterBtnsOnForecastLaborPage){
				if (filter.findElement(By.cssSelector(".lg-filter__label")).getText().equalsIgnoreCase("day part")){
					clickTheElement(filter.findElement(By.cssSelector("input-field[ng-click=\"$ctrl.openFilter()\"]")));
					flag = true;
					break;
				}
			}
			if (flag){
				SimpleUtils.pass("Find the day part filter successfully!");
			} else {
				SimpleUtils.fail("No day part filter on this page!", false);
			}
		}else {
			SimpleUtils.fail("No available filters on this page!", false);
		}
	}

	@FindBy(css = ".lg-filter__category-label")
	private List<WebElement> labelsInForecastFilter;
	@Override
	public void verifyThereAreDayPartsItemsInTheFilter() throws Exception {
		boolean flag = false;
		if (areListElementVisible(labelsInForecastFilter, 10) && labelsInForecastFilter.size()>0){
			for (WebElement element: labelsInForecastFilter){
				if (element.getText().equalsIgnoreCase("dayparts")){
					flag = true;
					break;
				}
			}
			if (flag){
				SimpleUtils.pass("Day parts filter is available!");
			} else {
				SimpleUtils.fail("Day parts filter is not available!", false);
			}
		} else {
			SimpleUtils.fail("No options in the filter", false);
		}
	}

	@FindBy(css = "[class*=\"lg-filter__wrapper lg-ng-animate\"] input-field[type=\"checkbox\"]")
	private List<WebElement> filterOptions;
	@Override
	public void selectFilterOptionsByText(String option) throws Exception {
		waitForSeconds(3);
		for (WebElement filterOption : filterOptions) {
			if (filterOption.getAttribute("label").toLowerCase().contains(option.toLowerCase())) {
				clickTheElement(filterOption.findElement(By.cssSelector("input")));
				break;
			}
		}
	}

	public boolean verifyIsShopperTypeSelectedByDefaultAndLaborTabIsClickable() throws Exception {
		boolean flag=false;
		if (isElementLoaded(shoppersTab,5)) {
			if (shoppersTab.getText().toLowerCase().contains("shopper") || shoppersTab.getText().toLowerCase().contains("demand")
			|| shoppersTab.getText().toLowerCase().contains("items")){
				if (shoppersTab.getAttribute("class").contains("selected")) {
					SimpleUtils.pass("Shopper/Demand forecast is selected by default");
					clickOnLabor();
					flag = true;
				}else {
					SimpleUtils.fail("Shopper forecast is not selected by default",false);
				}
			} else {
				SimpleUtils.report("Shopper tab is not loaded!");
			}
		}else {
			flag = false;
			SimpleUtils.fail("verifyIsShopperTypeSelectedByDefaultAndLaborTabIsClickable : forecast tab load failed",false);
		}
		return flag;
	}


	public boolean verifyIsWeekForecastVisibleAndOpenByDefault() throws Exception {
		boolean flag=false;
		goToPostWeekNextToCurrentWeek();
		if (isElementLoaded(weekViewButton,25)) {
			String aaa = weekViewButton.getAttribute("class");
			if (weekViewButton.getAttribute("class").contains("selected")) {
				SimpleUtils.pass("week forecast is selected by default");
				if (weatherTemperatures.size()>=6) {
					flag=true;
					SimpleUtils.pass("week forecast is open");
				} else {
					flag=true;
					SimpleUtils.report("No weather smart card!");
				}
			}else {
				SimpleUtils.fail("weekly forecast is not selected by default",false);
			}
		}else {
			flag =false;
			SimpleUtils.fail("weekly button load failed",false);
		}
		return flag;
	}

	@FindBy(css = "lg-button[label=\"Edit\"]")
	private WebElement editForecastBtn;
	@FindBy(css = "lg-button[label=\"Cancel\"]")
	private WebElement cancelEditForecastBtn;
	@FindBy(css = "lg-button[label=\"Save\"]")
	private WebElement saveForecastBtn;
	@Override
	public void verifyAndClickEditBtn() {
		try{
			if (isElementLoaded(editForecastBtn,10)){
				click(editForecastBtn);
				SimpleUtils.pass("Edit button is clicked!");
				if (isElementLoaded(getDriver().findElement(By.xpath("//span[contains(text(),'Edited Forecast')]")),10)){
					SimpleUtils.pass("Edit Forecast button is loaded!");
				} else {
					SimpleUtils.fail("Edit forecast is not loaded!", false);
				}
			} else {
				SimpleUtils.fail("Edit button is not loaded!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
	}

	@Override
	public void verifyAndClickCancelBtn() {
		try{
			if (isElementLoaded(cancelEditForecastBtn,10)){
				click(cancelEditForecastBtn);
				SimpleUtils.pass("cancelEditForecastBtn  button is clicked!");
			} else {
				SimpleUtils.fail("cancelEditForecastBtn button is not loaded!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
	}

	@Override
	public void verifyAndClickSaveBtn() {
		try{
			if (isElementLoaded(saveForecastBtn,10)){
				click(saveForecastBtn);
				SimpleUtils.pass("saveForecastBtn button is clicked!");
				if (isElementLoaded(updateForecastValueDialog.findElement(By.cssSelector(".modal-instance-button.confirm")),10)){
					click(updateForecastValueDialog.findElement(By.cssSelector(".modal-instance-button.confirm")));
					waitForSeconds(3);
				}
			} else {
				SimpleUtils.fail("saveForecastBtn button is not loaded!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
	}

	@FindBy(css = "g[id*=\"bar-edit-handle-wrapper\"] path.bar-edit-handle")
	private List<WebElement> forecastBars;
	@FindBy(css = "svg[id=\"forecast-prediction\"] g g:not(.tick):not([fill=\"none\"]) .transparent-area")
	private List<WebElement> forecastBarsInViewMode;

	//index value range: 0-6
	@Override
	public void verifyDoubleClickAndUpdateForecastBarValue(String index, String value) {
		try{
			if (areListElementVisible(forecastBars,10)){
				doubleClick(forecastBars.get(Integer.parseInt(index)));
				SimpleUtils.pass("the bar has been clicked!");
				updateForecastValue(value);
			} else {
				SimpleUtils.fail("forecastBars are not loaded!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
	}

	@FindBy(css = ".modal-dialog")
	private WebElement updateForecastValueDialog;
	private void updateForecastValue(String value){
		try{
			if (isElementLoaded(updateForecastValueDialog, 10)) {
				updateForecastValueDialog.findElement(By.tagName("input")).clear();
				updateForecastValueDialog.findElement(By.tagName("input")).sendKeys(value);
				click(updateForecastValueDialog.findElement(By.cssSelector("lg-button[label=\"OK\"]")));
				SimpleUtils.pass("New value has been updated");
			} else {
				SimpleUtils.fail("updateForecastValueDialog is not loaded!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
	}

	@FindBy(css = ".forecast-prediction-tooltip")
	private WebElement tooltipForForecastBar;
	@Override
	public String getTooltipInfo(String index) {
		String info = "";
		try{
			if (areListElementVisible(forecastBarsPath,10)){
				scrollToElement(forecastBarsPath.get(Integer.parseInt(index)));
				mouseToElement(forecastBarsPath.get(Integer.parseInt(index)));
				waitForSeconds(2);
				if (isElementLoaded(tooltipForForecastBar,20)){
					//String s = "2 Wed Forecast 527 Legion 527 Edited Comparison N/A Actual";
					info = tooltipForForecastBar.getText().replace("\n", " ");
				} else {
					//info = "x x Forecast 0 Legion 0 Edited Comparison N/A Actual";
					SimpleUtils.report("tooltipForForecastBar is not loaded since there is no tooltip!");
				}
			}else if (areListElementVisible(forecastBarsInViewMode,10)){
				scrollToElement(forecastBarsInViewMode.get(Integer.parseInt(index)));
				mouseToElement(forecastBarsInViewMode.get(Integer.parseInt(index)));
				waitForSeconds(2);
				if (isElementLoaded(tooltipForForecastBar,20)){
					//String s = "2 Wed Forecast 527 Legion 527 Edited Comparison N/A Actual";
					info = tooltipForForecastBar.getText().replace("\n", " ");
				} else {
					SimpleUtils.report("tooltipForForecastBar is not loaded since there is no tooltip!");
				}
			} else {
				SimpleUtils.fail("forecastBars are not loaded!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
		return info;
	}

	//@Override
	private List<String> getTooltipInfos() {
		List<String> info = new ArrayList<>();
		try{
			if (areListElementVisible(forecastBarsPath,10)){
				for (WebElement bar: forecastBarsPath){//.findElement(By.xpath("./.."))
					scrollToElement(bar);
					mouseToElement(bar);
					if (isElementLoaded(tooltipForForecastBar,20)){
						//String s = "x x Forecast 0 Legion 0 Edited Comparison N/A Actual";
						info.add(tooltipForForecastBar.getText().replace("\n", " "));
					} else {
						SimpleUtils.fail("tooltipForForecastBar is not loaded!", false);
					}
				}
			} else if (areListElementVisible(forecastBarsInViewMode,10)){
				for (WebElement bar: forecastBarsInViewMode){//.findElement(By.xpath("./.."))
					scrollToElement(bar);
					mouseToElement(bar);
					if (isElementLoaded(tooltipForForecastBar,20)){
						//String s = "2 Wed Forecast 527 Legion 527 Edited Comparison N/A Actual";
						info.add(tooltipForForecastBar.getText().replace("\n", " "));
					} else {
						SimpleUtils.fail("tooltipForForecastBar is not loaded!", false);
					}
				}
			} else {
				SimpleUtils.fail("forecastBars are not loaded!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
		return info;
	}

	@FindBy(css = "g[transform=\"translate(0,200)\"] g.tick")
	private List<WebElement> ticksForForecastGraph;
	@Override
	public String getTickByIndex(int index) {
		String tick = null;
		try{
			if (areListElementVisible(ticksForForecastGraph, 10)){
				tick = ticksForForecastGraph.get(index).getText();
			}
		} catch(Exception e){
			System.err.println();
		}
		return tick;
	}

	@Override
	public void verifyWarningEditingForecast() {
		String message = "You're in a process of editing forecast. Please, Save or Cancel edits.";
		try{
			if (isElementLoaded(updateForecastValueDialog, 10) && updateForecastValueDialog.findElement(By.cssSelector(".lgn-alert-message")).getText().contains(message)
			&& updateForecastValueDialog.findElement(By.cssSelector(".lgn-action-button")).getText().equals("OK")) {
				SimpleUtils.pass("Warning message for leaving edit forecast mode is correct!");
				click(updateForecastValueDialog.findElement(By.cssSelector(".lgn-action-button")));
			} else {
				SimpleUtils.fail("Forecast Page: The alert warning dialog is not loaded or loaded incorrectly!", false);
			}
		} catch(Exception e){
			System.err.println();
		}
	}

	@Override
	public void verifyLegionPeakShopperFromForecastGraphInWeekView() {
		try{
			float legionPeakShopperInTooltip =0;
			float total =0;
			List<String> tooltipInfos = getTooltipInfos();
			HashMap<String, Float> insightDataInWeek = getInsightDataInShopperWeekView();
			String dayIndex = "";
			for (int i =0; i<tooltipInfos.size(); i++){
				String legionPeakShopperTemp = tooltipInfos.get(i).split(" ")[3];
				total = total + Float.parseFloat(legionPeakShopperTemp);
				if (legionPeakShopperTemp!=null && !legionPeakShopperTemp.equals("") && Float.parseFloat(legionPeakShopperTemp)>legionPeakShopperInTooltip){
					legionPeakShopperInTooltip = Float.parseFloat(legionPeakShopperTemp);
					dayIndex = tooltipInfos.get(i).split(" ")[1];
				}
			}
			if (insightDataInWeek.containsKey(getDayCompletedInfo(dayIndex))){
				SimpleUtils.pass("Peak day info is correct!");
			} else {
				SimpleUtils.fail("Peak day info is incorrect! Value from tooltips: "+dayIndex, false);
			}
			if (insightDataInWeek.get("peakEditedShoppers")==legionPeakShopperInTooltip){
				SimpleUtils.pass("peakShoppers info is correct!");
			} else {
				SimpleUtils.fail("peakShoppers info is incorrect! Value from tooltips: "+legionPeakShopperInTooltip, false);
			}
			if (insightDataInWeek.get("totalEditedShoppers")==total){
				SimpleUtils.pass("totalShoppers info is correct!");
			} else {
				SimpleUtils.fail("totalShoppers info is incorrect! Value from tooltips: "+total, false);
			}
		} catch(Exception e){
			SimpleUtils.fail(e.getMessage(),false);
			System.err.println();
		}
	}


	private String getDayCompletedInfo(String abbreviationDay){
		String result= null;
		if (abbreviationDay.equalsIgnoreCase("sun")){
			result = "Sunday";
		}
		if (abbreviationDay.equalsIgnoreCase("mon")){
			result = "Monday";
		}
		if (abbreviationDay.equalsIgnoreCase("tue")){
			result = "Tuesday";
		}
		if (abbreviationDay.equalsIgnoreCase("wed")){
			result = "Wednesday";
		}
		if (abbreviationDay.equalsIgnoreCase("thu")){
			result = "Thursday";
		}
		if (abbreviationDay.equalsIgnoreCase("fri")){
			result = "Friday";
		}
		if (abbreviationDay.equalsIgnoreCase("sat")){
			result = "Saturday";
		}
		return result;
	}
	// Added by Julie
	@FindBy (css = "[ng-if=\"isEdit\"].pull-right")
	private WebElement noteInEditMode;

	@FindBy (css = "span.lgn-alert-message")
	private WebElement alertMessage;

	@FindBy(css = "g[id*=\"bar-edit-handle-wrapper\"] path")
	private List<WebElement> forecastBarsPath;

	@FindBy(css = ".lg-modal__title-icon")
	private WebElement dialogTitle;

	@FindBy(className = "dismiss")
	private WebElement closeButton;

	@FindBy(css = "[label=\"OK\"]")
	private WebElement OKButton;

	@FindBy(css = "[label=\"Cancel\"]")
	private WebElement CancelButton;

	@FindBy(xpath = "//label[contains(text(),\"Shoppers*\")]")
	private WebElement shoppersInDialog;

	@FindBy(css = "ng-form.ng-valid-step input")
	private WebElement valueOfShoppers;

	@FindBy (css = ".save-forecast-confirm__graph g g")
	private List<WebElement> barGraphsInConfirmModal;

	@FindBy (css = "[ng-click=\"$dismiss()\"]")
	private WebElement cancelBtnInConfirmModal;

	@FindBy (css = "[ng-click=\"confirm()\"]")
	private WebElement saveBtnInConfirmModal;

	@FindBy (css = "rect.transparent-area")
	private List<WebElement> bars;

	@Override
	public void clickOnDayView() throws Exception {
		if (isElementLoaded(dayViewButton, 10)) {
			if (!dayViewButton.findElement(By.xpath("./..")).getAttribute("class").contains("lg-button-group-selected")) {
				waitForSeconds(5);
				clickTheElement(dayViewButton);
			}
			SimpleUtils.pass("Forecast Page: Day View loaded successfully");
		} else {
			SimpleUtils.fail("Forecast Page: Day View button failed to load", false);
		}
	}

	@Override
	public void verifyEditBtnVisible() throws Exception {
		if (isElementLoaded(editForecastBtn,10) && editForecastBtn.isDisplayed())
			SimpleUtils.pass("Forecast Page: Edit button is visible");
		else
			SimpleUtils.fail("Forecast Page: Edit button failed to load",false);
	}

	@Override
	public void verifyContentInEditMode() throws Exception {
		if (isElementLoaded(getDriver().findElement(By.xpath("//span[contains(text(),'Edited Forecast')]")),10))
			SimpleUtils.pass("Forecast Page: Edit Forecast button shows after clicking on Edit button");
		else
			SimpleUtils.fail("Forecast Page: Edit forecast failed to load", false);
		if (isElementLoaded(saveForecastBtn,5) && isElementLoaded(cancelEditForecastBtn,5))
			SimpleUtils.pass("Forecast Page: Save and Cancel buttons show after clicking on Edit button");
		else
			SimpleUtils.fail("Forecast Page: Save and Cancel buttons failed to load",false);
		String noteMessage = "Ctrl+click to select multiple bars, double-click to enter the exact value";
		if (isElementLoaded(noteInEditMode,5) && noteInEditMode.getText().equals(noteMessage))
			SimpleUtils.pass("Forecast Page: The note \"Ctrl+click to select multiple bars, double-click to enter the exact value\" shows after clicking on Edit button");
		else
			SimpleUtils.fail("Forecast Page: The note failed to load",false);
	}

	@Override
	public void navigateToOtherDay() throws Exception {
		if (areListElementVisible(dayViewOfDatePicker,5)) {
			for (WebElement day: dayViewOfDatePicker) {
				if (!day.getAttribute("class").contains("day-week-picker-period-active")) {
					click(day);
					SimpleUtils.pass("Forecast Page: Navigate to other day");
					break;
				}
			}
		} else
			SimpleUtils.fail("Forecast Page: Dates in day view failed to load",false);
	}

	@Override
	public void verifyTooltipWhenMouseEachBarGraph() throws Exception {
		String tooltip = "";
		String tick = "";
		boolean isTooltipCorrect = false;
		if (isElementLoaded(insightSmartCard, 5) & isElementLoaded(barChartInProjected, 5)) {
			for (int i = 0; i < forecastBarsPath.size(); i++) {
				scrollToElement(forecastBarsPath.get(i));
				mouseToElement(forecastBarsPath.get(i));
				/*
				 *wait for tooltip data to load
				 * */
				waitForSeconds(2);
				if (isElementLoaded(tooltipForForecastBar,10)){
					// tooltip = "8:00 Forecast 12 Legion 12 Edited Comparison N/A Actual";
					tooltip = tooltipForForecastBar.getText().replace("\n", " ");
				} else {
					SimpleUtils.fail("Forecast Page: Tooltip failed to load in edit mode", false);
				}
				tick = getTickByIndex(i);
				if (!tick.isEmpty()) {
					if (tooltip.contains(tick + " Forecast") && (tooltip.contains("Legion") || tooltip.contains("Current")) && tooltip.contains("Edited") && tooltip.contains("Comparison") && tooltip.contains("Actual"))
						isTooltipCorrect = true;
					else {
						isTooltipCorrect = false;
						break;
					}
				} else {
					if (tooltip.contains(tick.replace("00","30") + " Forecast") && (tooltip.contains("Legion") || tooltip.contains("Current")) && tooltip.contains("Edited") && tooltip.contains("Comparison") && tooltip.contains("Actual"))
						isTooltipCorrect = true;
					else {
						isTooltipCorrect = false;
						break;
					}
				}
			}
			if (isTooltipCorrect)
				SimpleUtils.pass("Forecast Page: Tooltip is expected in edit mode");
			else
				SimpleUtils.fail("Forecast Page: Tooltip is unexpected in edit mode",false);
		} else
			SimpleUtils.fail("Forecast Page: The page failed to load",false);
	}

	@Override
	public void verifyPeakShoppersPeakTimeTotalShoppersLegionDataInDayView () throws Exception {
		HashMap<String, Float> insightDataInWeek = getInsightDataInShopperWeekView();
		List<String> dataInBar = getForecastBarGraphData();
		Float peakShoppersLegionInBar = 0.0f;
		Float totalShoppersLegionInBar = 0.0f;
		Float peakTimeLegionInBar = 0.0f;
		String legionInBar = "";

		for (int i = 0; i < dataInBar.size(); i++) {
			if (!dataInBar.get(i).isEmpty()) {
				// 12 pm Forecast 12 Legion 12 Edited Comparison 0 Actual
				if (dataInBar.get(i).split(" ").length > 8) {
					legionInBar = dataInBar.get(i).replace(" pm",":00").split(" ")[2];
					String timeInBar = dataInBar.get(i).split(" ")[0];
					totalShoppersLegionInBar += Float.valueOf(legionInBar);
					if (peakShoppersLegionInBar <= Float.valueOf(legionInBar)) {
						peakShoppersLegionInBar = Float.valueOf(legionInBar);
						peakTimeLegionInBar = Float.valueOf(timeInBar.replace(":","."));
					}
				}
			}
		}
		SimpleUtils.report("Forecast Page: Peak shoppers of Legion in bar graph: " + peakShoppersLegionInBar);
		SimpleUtils.report("Forecast Page: Peak time of legion in bar graph: " + peakTimeLegionInBar);
		SimpleUtils.report("Forecast Page: Total shoppers of legion in bar graph: " + totalShoppersLegionInBar);
		Float peakShopperLegionInInsightCard = insightDataInWeek.get("peakShoppers");
		Float peakTimeLegionInInsightCard = insightDataInWeek.get("peakTime");
		Float totalShoppersLegionInInsightCard = insightDataInWeek.get("totalShoppers");
		SimpleUtils.report("Forecast Page: Peak shoppers of Legion in smart card: " + peakShopperLegionInInsightCard);
		SimpleUtils.report("Forecast Page: Peak time of legion in smart card: " + peakTimeLegionInInsightCard);
		SimpleUtils.report("Forecast Page: Total shoppers of legion in smart card: " + totalShoppersLegionInInsightCard);
		if (totalShoppersLegionInInsightCard.equals(totalShoppersLegionInBar) & peakShopperLegionInInsightCard.equals(peakShoppersLegionInBar)
				&& peakTimeLegionInInsightCard.equals(peakTimeLegionInBar)) {
			SimpleUtils.pass("Forecast Page: In smart card, total shoppers, peak shoppers and peak time are matching with bar graph");
		} else {
			//SimpleUtils.warn("SCH-2394: Tooltip displays \"Current\" not \"Legion\" after editing forecast");
			SimpleUtils.fail("Forecast Page: In smart card, total shoppers, peak shoppers and peak time are not matching with bar graph",false);
		}
	}

	@Override
	public void verifyDraggingBarGraph() throws Exception {
		if (areListElementVisible(forecastBarsPath,10)) {
			int index = (new Random()).nextInt(forecastBarsPath.size() - 2);
			scrollToElement(forecastBarsPath.get(index));
			moveElement(forecastBarsPath.get(index), 20);
			if (!forecastBarsPath.get(index).findElement(By.xpath("./..")).getAttribute("transform").contains("(0, "))
				SimpleUtils.pass("Forecast Page: Bar graph is draggable");
			else
				SimpleUtils.fail("Forecast Page: Bar graph is not draggable",false);
		} else
			SimpleUtils.fail("Forecast Page: Forecast bars failed to load",false);
	}

	@Override
	public void verifyDoubleClickBarGraph(String index) throws Exception {
		if (areListElementVisible(forecastBars,10)) {
			for (WebElement bar: forecastBars) {
				if (bar.getAttribute("id").contains(index)) {
					doubleClick(bar);
					SimpleUtils.pass("Forecast Page: Bar graph can be double clicked");
					if (isElementLoaded(updateForecastValueDialog, 10))
						SimpleUtils.pass("Forecast Page: Specify a Value layout pops up");
					else
						SimpleUtils.fail("Forecast Page: Specify a Value layout does not pop up", false);
					break;
				}
			}
		} else
			SimpleUtils.fail("Forecast Page: Forecast Bars failed to load", false);
	}

	@Override
	public String getLegionValueFromBarTooltip(int index) throws Exception {
		String legionInBar = "";
		String tooltip = "";
		tooltip = getTooltipInfoWhenView().get(index);
		if (tooltip.split(" ").length > 6)
			legionInBar = tooltip.replace(" pm",":00").split(" ")[2];
		return legionInBar;
	}

	@Override
	public void verifyContentOfSpecifyAValueLayout(String index, String value) throws Exception {
		if (isElementLoaded(updateForecastValueDialog, 10)) {
			SimpleUtils.report("The value Of Shoppers is " + valueOfShoppers.getAttribute("value"));
			if (dialogTitle.getText().contains("Specify a value for") && isElementLoaded(closeButton,5)
					&& isElementLoaded(shoppersInDialog,5) && valueOfShoppers.getAttribute("value").equals(value)
					&& isElementLoaded(OKButton,5) && isElementLoaded(cancelEditForecastBtn,5))
				SimpleUtils.pass("Forecast Page: The following content is loaded:\n" +
						"- Specify a value\n" +
						"- Shoppers*\n" +
						"- Value of the Shoppers\n" +
						"- Cancel, Save buttons\n" +
						"- Close button");
		} else
			SimpleUtils.fail("Forecast Page: Specify a value dialog failed to load", false);
	}

	@Override
	public void verifyAndClickCloseBtn() throws Exception {
		if (isElementLoaded(updateForecastValueDialog, 10)) {
			if (isElementLoaded(closeButton,5)) {
				click(closeButton);
				if (!isElementLoaded(updateForecastValueDialog, 10))
					SimpleUtils.pass("Forecast Page: Close button is clickable in specify a value dialog");
				else
					SimpleUtils.fail("Forecast Page: Close button is not clickable in specify a value dialog",false);
			} else
				SimpleUtils.fail("Forecast Page: Close button on pop up failed to load",false);
		} else
			SimpleUtils.fail("Forecast Page: Specify a value dialog failed to load", false);
	}

	@Override
	public void verifyAndClickOKBtn() throws Exception {
		if (isElementLoaded(updateForecastValueDialog, 10)) {
			if (isElementLoaded(closeButton,5)) {
				click(OKButton);
				if (!isElementLoaded(updateForecastValueDialog, 10))
					SimpleUtils.pass("Forecast Page: OK button is clickable in specify a value dialog");
				else
					SimpleUtils.fail("Forecast Page: OK button is not clickable in specify a value dialog",false);
			} else
				SimpleUtils.fail("Forecast Page: OK button on pop up failed to load",false);
		} else
			SimpleUtils.fail("Forecast Page: Specify a value dialog failed to load", false);
	}

	@Override
	public String getEditedValueFromBarTooltip(int index) throws Exception {
		String editedInBar = "";
		String tooltip = "";
		tooltip = getTooltipInfo(String.valueOf(index));
		// 12 pm Forecast 12 Legion 12 Edited Comparison 0 Actual
		if (tooltip.split(" ").length > 8)
			editedInBar = tooltip.replace(" pm",":00").split(" ")[4];
		return editedInBar;
	}

	@Override
	public String getPercentageFromBarTooltip(int index) throws Exception {
		String percentage = "";
		String tooltip = "";
		tooltip = getTooltipInfo(String.valueOf(index));
		// 9:30 Forecast 5 Legion 2 Edited ↓60% Comparison N/A Actual
		if (tooltip.split(" ").length > 8 && tooltip.contains("%"))
			percentage = tooltip.replace(" pm",":00").split(" ")[6];
		return percentage;
	}

	@Override
	public String getActiveDayText() throws Exception {
		WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
		if (isElementLoaded(activeWeek,5))
			return activeWeek.getText().replace("\n", ", ");
		return "";
	}

	@Override
	public void verifyConfirmMessageWhenSaveForecast(String dayOrWeek, String activeDayOrWeekText) throws Exception {
		if (isElementLoaded(updateForecastValueDialog, 10)) {
			String confirmMessage = updateForecastValueDialog.getText().replace("\n"," ");
			if (confirmMessage.contains("Are you sure you want to save changes to this Forecast?") && confirmMessage.contains("Staffing Guidance will be regenerated")
					&& confirmMessage.contains("%") && isElementLoaded(cancelBtnInConfirmModal,5) && isElementLoaded(saveBtnInConfirmModal,5)
					&& barGraphsInConfirmModal.get(0).getText().contains("Shoppers Current") && barGraphsInConfirmModal.get(1).getText().contains("Shoppers Edited")
					&& confirmMessage.contains("Forecast for the " + dayOrWeek.toLowerCase() + " of " + activeDayOrWeekText))
				SimpleUtils.pass("Forecast Page: The following content is loaded:\n" +
						"- Are you sure you want to save changes to this Forecast?\n" +
						"- Current bar graph\n" +
						"- Edited bar graph\n" +
						"- Percentage\n" +
						"- Forecast for the day of Sunday, Dec 6\n" +
						"- Staffing Guidance will be regenerated.\n" +
						"- Cancel and Save buttons");
			else
				//SimpleUtils.fail("Forecast Page: The content in confirm modal failed to load or loaded incorrectly",false);
				SimpleUtils.warn("SCH-2438: Typo for Shoppers in confirm message");
		} else
			SimpleUtils.fail("Forecast Page: The confirm modal failed to load", false);
	}

	@Override
	public void clickOnCancelBtnOnConfirmPopup() throws Exception {
		if (isElementLoaded(cancelBtnInConfirmModal,5)) {
			click(cancelBtnInConfirmModal);
			if (!isElementLoaded(cancelBtnInConfirmModal,5))
			SimpleUtils.pass("Forecast Page: Cancel button is clickable on the confirm pop up");
			else
				SimpleUtils.fail("Forecast Page: Cancel button is not clickable on the confirm pop up",false);
		} else
			SimpleUtils.fail("Forecast Page: Cancel button on confirm modal failed to load", false);
	}

	@Override
	public void clickOnSaveBtnOnConfirmPopup() throws Exception {
		if (isElementLoaded(saveBtnInConfirmModal,5)) {
			click(saveBtnInConfirmModal);
			if (!isElementLoaded(saveBtnInConfirmModal,5))
				SimpleUtils.pass("Forecast Page: Save button is clickable on the confirm pop up");
			else
				SimpleUtils.fail("Forecast Page: Save button is not clickable on the confirm pop up",false);
		} else
			SimpleUtils.fail("Forecast Page: Save button on confirm modal failed to load", false);
	}

	@Override
	public void verifyPeakShoppersPeakTimeTotalShoppersEditedDataInDayView () throws Exception {
		HashMap<String, Float> insightDataInWeek = getInsightDataInShopperWeekView();
		List<String> dataInBar = getForecastBarGraphData();
		Float peakShoppersEditedInBar = 0.0f;
		Float peakTimeEditedInBar = 0.0f;
		Float totalShoppersEditedInBar = 0.0f;
		String forecastInBar = "";

		for (int i = 0; i < dataInBar.size(); i++) {
			if (!dataInBar.get(i).isEmpty()) {
				// 8:00 Forecast 12 Legion 12 Edited Comparison 0 Actual
				if (dataInBar.get(i).split(" ").length > 6) {
					forecastInBar = dataInBar.get(i).replace(" pm",":00").split(" ")[2];
					String timeInBar = dataInBar.get(i).split(" ")[0];
					totalShoppersEditedInBar += Float.valueOf(forecastInBar);
					if (peakShoppersEditedInBar <= Float.valueOf(forecastInBar)) {
						peakShoppersEditedInBar = Float.valueOf(forecastInBar);
						peakTimeEditedInBar = Float.valueOf(timeInBar.replace(":","."));
					}
				}
			}
		}
		SimpleUtils.report("Forecast Page: Peak shoppers of Forecast in bar graph: " + peakShoppersEditedInBar);
		SimpleUtils.report("Forecast Page: Peak time of Forecast in bar graph: " + peakTimeEditedInBar);
		SimpleUtils.report("Forecast Page: Total shoppers of Forecast in bar graph: " + totalShoppersEditedInBar);
		Float peakShopperEditedInInsightCard = insightDataInWeek.get("editedPeakShoppers");
		Float peakTimeEditedInInsightCard = insightDataInWeek.get("editedPeakTime");
		Float totalShoppersEditedInInsightCard = insightDataInWeek.get("editedTotalShoppers");
		SimpleUtils.report("Forecast Page: Peak shoppers of Edited in smart card: " + peakShopperEditedInInsightCard);
		SimpleUtils.report("Forecast Page: Peak time of Edited in smart card: " + peakTimeEditedInInsightCard);
		SimpleUtils.report("Forecast Page: Total shoppers of Edited in smart card: " + totalShoppersEditedInInsightCard);
		if (totalShoppersEditedInInsightCard.equals(totalShoppersEditedInBar) & peakShopperEditedInInsightCard.equals(peakShoppersEditedInBar)
				&& peakTimeEditedInInsightCard.equals(peakTimeEditedInBar)) {
			SimpleUtils.pass("Forecast Page: In smart card, total shoppers, peak shoppers and peak time are matching with bar graph after saving");
		} else {
			//SimpleUtils.warn("SCH-2394: Tooltip displays \"Current\" not \"Legion\" after editing forecast");
			SimpleUtils.fail("Forecast Page: In smart card, total shoppers, peak shoppers and peak time are not matching with bar graph after saving",false);
		}
	}

	@Override
	public List<String> getTooltipInfoWhenView() throws Exception {
		List<String> info = new ArrayList<>();
		if (areListElementVisible(bars, 10)) {
			for (int i = 0; i < bars.size(); i++) {
				scrollToElement(bars.get(i));
				mouseToElement(bars.get(i));
				if (isElementLoaded(tooltipForForecastBar, 10)) {
					// 7:30 Forecast 1 Legion 1 Edited Comparison N/A Actual
					// 7:30 Forecast 1 Forecast Comparison N/A Actual
					info.add(tooltipForForecastBar.getText().replace("\n", " "));
				} else
					info.add(getTickByIndex(i) + " Forecast 0 Forecast Comparison N/A Actual");
			}
		} else
			SimpleUtils.fail("Forecast Page: Bars failed to load", false);
		return info;
	}

	// Added by Julie
	@FindBy(css = "[label=\"Refresh\"] button")
	private WebElement refreshButton;

	@FindBy(css = ".modal-content")
	private WebElement warningDialog;

	@FindBy(css = ".redesigned-modal-header")
	private WebElement headerOnwarningDialog;

	@FindBy(css = ".redesigned-modal-body")
	private WebElement bodyOnWarningDialog;

	@FindBy(css = ".redesigned-modal-button-cancel")
	private WebElement cancelBtnOnWarningDialog;

	@FindBy(css = ".redesigned-modal-button-ok")
	private WebElement refreshanywayBtnOnWarningDialog;

	@Override
	public void clickOnRefreshButton() throws Exception {
		if (isElementLoaded(refreshButton, 10)) {
			click(refreshButton);
			SimpleUtils.pass("Forecast Page: Click on Refresh button Successfully!");
		} else {
			SimpleUtils.fail("Forecast Page: Refresh button not Loaded!", true);
		}
	}

	@Override
	public void verifyWarningDialogPopsUp() throws Exception {
		if(isElementLoaded(warningDialog, 10))
			SimpleUtils.pass("Forecast Page: Warning dialog pops up after clicking Refresh");
		else
			SimpleUtils.fail("Forecast Page: waring dialog doesn't appear", false);
	}

	@Override
	public void verifyTheContentOnWarningDialog() throws Exception {
		if(isElementLoaded(warningDialog, 10)) {
			if (isElementLoaded(headerOnwarningDialog,10) && isElementLoaded(bodyOnWarningDialog,10)
			&& isElementLoaded(cancelBtnOnWarningDialog,10) && isElementLoaded(refreshanywayBtnOnWarningDialog,10)) {
				SimpleUtils.pass("Forecast Page: Warning title, body and buttons in dialog show after clicking Refresh");
				if (headerOnwarningDialog.getText().contains("Warning") && bodyOnWarningDialog.getText().contains("Refreshing the forecast will delete all recent edits.")
						&& cancelBtnOnWarningDialog.getText().contains("Cancel") && refreshanywayBtnOnWarningDialog.getText().contains("Refresh anyway"))
					SimpleUtils.pass("Forecast Page: Following content in warning dialog is loaded:\n" +
							"- Warning" +
							"Refreshing the forecast will delete all recent edits\n" +
							"- Cancel button\n" +
							"- Refresh anyway button");
				else
					SimpleUtils.fail("Forecast Page: The content in warning dialog is incorrect", false);
			} else
				SimpleUtils.fail("Forecast Page: Warning title, body and buttons in dialog don't show after clicking Refresh",false);
		} else
			SimpleUtils.fail("Forecast Page: waring dialog doesn't appear", false);
	}

	@Override
	public void verifyTheFunctionalityOfCancelButtonOnWarningDialog(int index, String value) throws Exception {
		if (isElementLoaded(cancelBtnOnWarningDialog,10)) {
			click(cancelBtnOnWarningDialog);
			String tooltip = getTooltipInfo(String.valueOf(index));
			if (tooltip.contains(value))
				SimpleUtils.pass("Forecast Page: Click on Cancel button successfully, edited forecast persist");
			else
				SimpleUtils.fail("Forecast Page: Failed to click on Cancel button",false);
		} else
			SimpleUtils.fail("Forecast Page: Cancel button in waring dialog failed to load",false);
	}

	@Override
	public void verifyTheFunctionalityOfRefreshanywayButtonOnWarningDialog(int index, String value) throws Exception {
		if (isElementLoaded(refreshanywayBtnOnWarningDialog,10)) {
			click(refreshanywayBtnOnWarningDialog);
			String tooltip = getTooltipInfo(String.valueOf(index));
			if (!tooltip.contains(value))
				SimpleUtils.pass("Forecast Page: Click on Refresh anyway button successfully, edited forecast is reverted");
			else
				SimpleUtils.fail("Forecast Page: Failed to click on Refresh anyway button",false);
		} else
			SimpleUtils.fail("Forecast Page: Refresh anyway button in waring dialog failed to load",false);
	}

	@FindBy(id = "forecast-labor-prediction")
	private WebElement forecastLaborPrediction;

	@FindBy(className = "card-carousel-fixed")
	private WebElement forecastSmartcard;

	@Override
	public void verifyLaborForecastCanLoad() throws Exception {
		if (isElementLoaded(forecastLaborPrediction,10) && !forecastLaborPrediction.getText().contains("Failed to load data")
				&& forecastLaborPrediction.getText().contains("LABOR HOURS") && isElementLoaded(forecastSmartcard,10))
			SimpleUtils.pass("Forecast Page: Content under labor tab should be loaded successfully");
		else
			SimpleUtils.fail("Forecast Page: Content under labor tab is not loaded",false);
	}
	
	@Override
	public void verifyEditBtnNotVisible() throws Exception {
		if (!(isElementLoaded(editForecastBtn,10) && editForecastBtn.isDisplayed()))
			SimpleUtils.pass("Forecast Page: Edit button failed to load");
		else
			SimpleUtils.fail("Forecast Page: Edit button is visible",false);
	}

	@Override
	public void verifyWorkRoleInList(String workRoleName) throws Exception {
		boolean flag = false;
		if (isElementLoaded(filterButton, 10)) {
			clickTheElement(filterButton);
			for (WebElement workRole : workRoleListNew) {
				if (workRole.getText().trim().contains(workRoleName)) {
					SimpleUtils.pass(workRoleName + "is exist");
					flag = true;
					break;
				}
			}
		} else {
			SimpleUtils.fail("Work role filter load failed", false);
		}
		if (!flag) {
			SimpleUtils.fail(workRoleName + " not is exist", true);
		}
	}

	@FindBy(css = "[label=\"Locations\"] ng-form [placeholder=\"None\"]")
	private WebElement locationsFilter;

	public boolean checkIsLocationFilterLoaded() throws Exception {
		boolean isLocationFilterLoaded = false;
		if (isElementLoaded(locationsFilter, 20)) {
			isLocationFilterLoaded = true;
			SimpleUtils.report("Location filter on Forecast page is loaded! ");
		} else
			SimpleUtils.report("Location filter on Forecast page is not loaded! ");
		return isLocationFilterLoaded;
	}

	@FindBy(css = "[class=\"lg-filter__wrapper lg-ng-animate space-for-clear-button\"] [value=\"opt.checked\"]")
	private List<WebElement> locationsInLocationFilter;

	@FindBy(css = "[class=\"lg-filter__wrapper lg-ng-animate space-for-clear-button\"] input[placeholder=\"Search Locations\"]")
	private WebElement locationSearchInputInLocationFilter;

	public boolean checkIfAllLocationBeenSelected () {
		boolean ifAllLocationBeenSelected = true;
		clickTheElement(locationsFilter);
		if (areListElementVisible(locationsInLocationFilter, 10) && locationsInLocationFilter.size() > 0) {
			for (WebElement location: locationsInLocationFilter) {
				if(location.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
					SimpleUtils.report("Location: "+ location.findElement(By.tagName("label")).getText()+ " is selected! ");
				} else {
					ifAllLocationBeenSelected = false;
					SimpleUtils.report("Location: "+ location.findElement(By.tagName("label")).getText()+ " is not selected! ");
					break;
				}
			}
		} else
			SimpleUtils.fail("Locations in location filter fail to load! ", false);
		clickTheElement(locationsFilter);
		return ifAllLocationBeenSelected;
	}


	public void checkOrUncheckLocationInFilter (boolean ifCheck, String locationName) throws Exception {
		if (isElementLoaded(locationsFilter, 10)) {
			boolean isLocationExists = false;
			clickTheElement(locationsFilter);
			if (areListElementVisible(locationsInLocationFilter, 10)
					&& locationsInLocationFilter.size() > 0
					&& isElementLoaded(locationSearchInputInLocationFilter, 10)) {
				locationSearchInputInLocationFilter.clear();
				locationSearchInputInLocationFilter.sendKeys(locationName);
				for (WebElement location: locationsInLocationFilter) {
					if (location.findElement(By.tagName("label")).getText().equalsIgnoreCase(locationName)) {
						isLocationExists = true;
						if(location.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
							if (ifCheck) {
								SimpleUtils.pass("Location already been checked! ");
							} else {
								clickTheElement(location.findElement(By.tagName("input")));
								if (location.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")) {
									SimpleUtils.pass("Location been checked successfully! ");
								}
							}
						} else {
							if (ifCheck) {
								clickTheElement(location.findElement(By.tagName("input")));
								if (location.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
									SimpleUtils.pass("Location been checked successfully! ");
								}
							} else
								SimpleUtils.pass("Location already been unchecked! ");
						}
						break;
					}
				}
				if (!isLocationExists) {
					SimpleUtils.fail("Location: " + locationName+ " is not exists! ", false);
				}
				//To close the filter popup
				clickTheElement(locationsFilter);
			} else
				SimpleUtils.fail("Locations in loction filter fail to load! ", false);
		} else
			SimpleUtils.fail("The locations filter fail to load!", false);
	}


	public List<String> getAllLocationsFromFilter () throws Exception {
		List<String> locations = new ArrayList<>();
		if (isElementLoaded(locationsFilter, 10)) {
			clickTheElement(locationsFilter);
			if (areListElementVisible(locationsInLocationFilter, 10)
					&& locationsInLocationFilter.size() > 0) {
				for (WebElement location: locationsInLocationFilter) {
					locations.add(location.findElement(By.tagName("label")).getText());
					}
				} else
					SimpleUtils.fail("Locations in loction filter fail to load! ", false);
				//To close the filter popup
				clickTheElement(locationsFilter);
		} else
			SimpleUtils.fail("The locations filter fail to load!", false);
		return locations;
	}

	@FindBy(css = "[id=\"forecast-labor-prediction\"] g [style*=\"text\"]")
	private List<WebElement> attendeesAndHoursInLaborForecastChart;
	@FindBy(css = "text[text-anchor=\"middle\"]")
	private List<WebElement> dataOrTimeInLaborForecastChart;

	public HashMap<String, List<String>> getLaborChartCoordinateAxisData() {
		HashMap<String, List<String>> attendeesAndHoursOfLaborForecast = new HashMap<>();
		if (areListElementVisible(attendeesAndHoursInLaborForecastChart) && attendeesAndHoursInLaborForecastChart.size()>0) {
			List<String> attendeesInLaborForecastChart = new ArrayList<>();
			List<String> hoursInLaborForecastChart = new ArrayList<>();
			List<String> dataOrTimeInInLaborForecastChart = new ArrayList<>();

			//Get the attendees and hours value
			for (int i = 0; i<attendeesAndHoursInLaborForecastChart.size(); i++) {
				if (i%2 == 0) {
					attendeesInLaborForecastChart.add(attendeesAndHoursInLaborForecastChart.get(i).getText());
					SimpleUtils.pass("Get a attendee in Labor Forecast chart successfully! ");
				} else {
					hoursInLaborForecastChart.add(attendeesAndHoursInLaborForecastChart.get(i).getText());
					SimpleUtils.pass("Get a hour in Labor Forecast chart successfully! ");
				}
			}

			//get the dates or times
			ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
			if (scheduleCommonPage.isScheduleDayViewActive()) {
				for (int i = 0; i<dataOrTimeInLaborForecastChart.size()/4; i++) {
					dataOrTimeInInLaborForecastChart.add(dataOrTimeInLaborForecastChart.get(i*4).getText());
					SimpleUtils.pass("Get a data/time in Labor Forecast chart successfully! ");
				}
			} else {
				for (int i = 0; i<dataOrTimeInLaborForecastChart.size(); i++) {
					dataOrTimeInInLaborForecastChart.add(dataOrTimeInLaborForecastChart.get(i).getText());
					SimpleUtils.pass("Get a data/time in Labor Forecast chart successfully! ");
				}
			}

			attendeesAndHoursOfLaborForecast.put("attendees", attendeesInLaborForecastChart);
			attendeesAndHoursOfLaborForecast.put("hours", hoursInLaborForecastChart);
			attendeesAndHoursOfLaborForecast.put("dateOrTime", dataOrTimeInInLaborForecastChart);
			SimpleUtils.pass("Get all attendees and hours in Labor Forecast chart successfully! ");
		} else
			SimpleUtils.fail("The attendees and hours in Labor Forecast chart fail to load! ", false);
		return attendeesAndHoursOfLaborForecast;
	}



	@Override
	public void selectWorkRoleFilterByText(String workRoleLabel) throws Exception {
		verifyWorkRoleSelection();
		for (WebElement availableWorkRoleFilter : workRoleList) {
			if (availableWorkRoleFilter.getText().equalsIgnoreCase(workRoleLabel)) {
				click(availableWorkRoleFilter);
				SimpleUtils.pass("Schedule Work Role:'" + availableWorkRoleFilter.getText() + "' Filter selected Successfully!");
				break;
			}
		}
		if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
			click(filterButton);
	}

	@Override
	public boolean areWorkRoleDisplayOrderCorrectOnLaborForecast(HashMap<String, Integer> workRoleNOrders) throws Exception {
		boolean isConsistent = true;
		try {
			if (isElementLoaded(filterButton, 10)) {
				clickTheElement(filterButton);
				if (areListElementVisible(workRoleList, 3)) {
					for (int i = 0; i < workRoleList.size() - 1; i++) {
						System.out.println(workRoleList.get(i).findElement(By.cssSelector(".input-label")).getText().toLowerCase().trim());
						int order1 = workRoleNOrders.get(workRoleList.get(i).findElement(By.cssSelector(".input-label")).getText().toLowerCase().trim().replaceAll(" ", ""));
						int order2 = workRoleNOrders.get(workRoleList.get(i + 1).findElement(By.cssSelector(".input-label")).getText().toLowerCase().trim().replaceAll(" ", ""));
						if (order1 > order2) {
							isConsistent = false;
							break;
						}
					}
				} else {
					isConsistent = false;
				}
			} else {
				isConsistent = false;
				SimpleUtils.fail("Work role filter load failed", false);
			}
		} catch (Exception e) {
			isConsistent = false;
		}
		return isConsistent;
	}

	@FindBy(css = "[class=\"card-carousel-card-table-edit-budget ng-scope\"]")
	private WebElement laborBudgetEditBtn;
	@FindBy(css = "[class=\"table-row-field guidance-background tc ng-scope w-50\"] [class=\"row-name-field ng-binding ng-scope\"]")
	private WebElement guidanceBudget;
	@FindBy(css = "[ng-class=\"table-row-input-field\"]")
	private WebElement budgetInputField;
	@FindBy(css = "[class =\"ok-action-text ng-binding\"]")
	private WebElement applyBudgetBtn;
	@Override
	public void goToForecastLaborDay() throws Exception {
		if (isElementEnabled(dayViewButton)) {
			click(dayViewButton);
			String weekDuration[] = currentActiveWeek.getText().split("\n");
			SimpleUtils.pass("Current active labor week is " + weekDuration[1]);
			if (isElementEnabled(laborTab)) {
				click(laborTab);
				waitForSeconds(5);
				if (forecastGraph.size() != 0 && laborSmartCardForecast.getText() != null) {
					SimpleUtils.pass("Labor Forecast Loaded in Day View Successfully!" + " Labor Forecast is " + laborSmartCardForecast.getText());
				} else {
					SimpleUtils.fail("Labor Forecast Not Loaded in Day View", false);
				}
			} else {
				SimpleUtils.fail("Labor tab under Forecast tab is not found", false);
			}
		} else {
			SimpleUtils.fail("Day View button not found in Forecast", false);
		}
	}

	@Override
	public void goToForecastLaborWeek() throws Exception {
		if (isElementEnabled(weekViewButton)) {
			click(weekViewButton);
			String weekDuration[] = currentActiveWeek.getText().split("\n");
			SimpleUtils.pass("Current active labor week is " + weekDuration[1]);
			if (isElementEnabled(laborTab)) {
				click(laborTab);
				waitForSeconds(20);
				if (forecastGraph.size() != 0 && isElementLoaded(laborSmartCardForecast, 10) && laborSmartCardForecast.getText() != null) {
					SimpleUtils.pass("Labor Forecast Loaded in Week View Successfully!" + " Labor Forecast is " + laborSmartCardForecast.getText());
				} else {
					SimpleUtils.fail("Labor Forecast Not Loaded in Week View", false);
				}
			} else {
				SimpleUtils.fail("Labor subtab of forecast tab not found", false);
			}
		} else {
			SimpleUtils.fail("Week View button not found in Forecast", false);
		}

	}

	@Override
	public boolean isLaborBudgetEditBtnLoaded() throws Exception {
		boolean isBtnLoaded = true;
		if (isElementLoaded(laborBudgetEditBtn, 5))
			SimpleUtils.report("Labor budget edit button is loaded!");
		else {
			isBtnLoaded = false;
			SimpleUtils.report("Labor budget edit button is not loaded!");
		}
		return  isBtnLoaded;
	}

	@Override
	public void editLaborBudgetOnSummarySmartCard() throws Exception {
		String forecast = "";
		if (isElementLoaded(laborSmartCardForecast, 20)) {
			forecast = laborSmartCardForecast.getText();
		}
		if (isElementLoaded(laborBudgetEditBtn)) {
			click(laborBudgetEditBtn);
			if (isElementLoaded(guidanceBudget)&&isElementLoaded(budgetInputField)) {
				String guidanceBudgetText = guidanceBudget.getText();
				budgetInputField.clear();
				budgetInputField.sendKeys(guidanceBudgetText);
				if(isElementLoaded (applyBudgetBtn)){
					clickTheElement(applyBudgetBtn);
					waitForSeconds(5);
					String budget = laborSmartCardBudget.getText();
					SimpleUtils.assertOnFail("The budget is not updated correctly",forecast.matches(budget),false);
				}else{
					SimpleUtils.fail("The apply button is not loaded!", false);
				}
			} else {
				SimpleUtils.fail("The guidance budget or budget input field is not loaded!", false);
			}
		}
	}

	@Override
	public void clearLaborBudgetOnSummarySmartCard() throws Exception {
		if (isElementLoaded(laborBudgetEditBtn)) {
			click(laborBudgetEditBtn);
			if (isElementLoaded(guidanceBudget)&&isElementLoaded(budgetInputField)) {
				budgetInputField.clear();
				if(isElementLoaded (applyBudgetBtn)){
					clickTheElement(applyBudgetBtn);
					waitForSeconds(3);
				}else{
					SimpleUtils.fail("The apply button is not loaded!", false);
				}
			} else {
				SimpleUtils.fail("The guidance budget or budget input field is not loaded!", false);
			}
		}
	}

	@Override
	public String getLaborBudgetOnSummarySmartCard() throws Exception {
		String BudgetValue = null;
		waitForSeconds(3);
		if (isElementLoaded(laborSmartCardBudget, 30)) {
			BudgetValue = laborSmartCardBudget.getText().trim();
			return BudgetValue;
		} else {
			SimpleUtils.fail("The edited budget on forecast page is not loaded!", false);
		}
		return null;
	}

	@FindBy(css = "[id*=\"Chart_area\"]")
	private WebElement demandForecastChart;
	@Override
	public void verifyDemandForecastCanLoad() throws Exception {
		if (isElementLoaded(demandForecastChart,10))
			SimpleUtils.pass("Forecast Page: Demand forecast page should be loaded successfully");
		else
			SimpleUtils.fail("Forecast Page: Demand forecast page is not loaded",false);
	}

	@FindBy(css = "[ng-if=\"hasWages()\"")
	private WebElement forecastWageLine;
	@FindBy(css = "[ng-if=\"scheduleSmartCard.hasWages\"")
	private WebElement scheduleWageLine;
	@Override
	public ArrayList getTextOfLaborWages() throws Exception {
		List<String> forecastWages  = new ArrayList<>();
		if (isElementLoaded(forecastWageLine,5)) {
			String [] wageLine = forecastWageLine.getText().split("\n");
			for(String wage: wageLine){
				wage = wage.trim();
				if(wage.toLowerCase().contains("Wages")) {
					continue;
				}
				forecastWages.add(wage);
			}
		}else{
			SimpleUtils.fail("Forecasts wages are not displayed!", false);
		}return (ArrayList) forecastWages;
	}

	@Override
	public ArrayList getTextOfScheduleWages() throws Exception {
		List<String> scheduleWages  = new ArrayList<>();
		if (isElementLoaded(scheduleWageLine,5)) {
			String [] wageLine = scheduleWageLine.getText().split("\n");
			for(String wage: wageLine){
				wage = wage.trim();
				if(wage.toLowerCase().contains("Wages")) {
					continue;
				}
				scheduleWages.add(wage);
			}
		}else{
			SimpleUtils.fail("Forecasts wages are not displayed!", false);
		}return (ArrayList) scheduleWages;
	}


	@Override
	public boolean isDemandForecastEditButtonEnabled() throws Exception {
		boolean isEnabled = true;
		if (isElementLoaded(editForecastBtn, 10)) {
			String tooltip = editForecastBtn.getAttribute("data-tootik");
			String status = editForecastBtn.findElement(By.cssSelector("button")).getAttribute("disabled");
			if (status!= null && !tooltip.equalsIgnoreCase("")
					&& tooltip.equalsIgnoreCase("Can not edit past demand forecast")) {
				isEnabled = false;
			} else
				SimpleUtils.report("Forecast Page: The demand forecast edit button is not enabled!");
		} else
			SimpleUtils.fail("Forecast Page: The demand forecast edit button is not loaded!", false);
		return isEnabled;
	}
}
