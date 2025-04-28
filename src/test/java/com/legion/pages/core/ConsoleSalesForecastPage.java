package com.legion.pages.core;

import static com.legion.tests.TestBase.refreshPage;
import static com.legion.utils.MyThreadLocal.getDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.legion.pages.BasePage;
import com.legion.pages.SalesForecastPage;
import com.legion.utils.SimpleUtils;

public class ConsoleSalesForecastPage extends BasePage implements SalesForecastPage{

	//Object[][] salesForecastCategoriesOptions = JsonUtil.getArraysFromJsonFile("src/test/resources/salesForecastCategoriesOptions.json");
	@FindBy(className="sub-navigation-view")
	private List<WebElement> scheduleSubNavigationViewTab;
	
	@FindBy (xpath = "//span[contains(text(),'Projected')]")
	private WebElement ProjectedTrafficSubMenu;
	
	@FindBy(css = "div.sub-navigation-view-link.active")
	private WebElement SchedulePageSelectedSubTab;
	
	@FindBy(css="[ng-click=\"selectDayWeekView($event, 'week')\"]")
	private WebElement salesForecastPageWeekViewButton;
	
	String salesForecastTabLabelText = "PROJECTED";
	
	@FindBy(css="[ng-click=\"selectDayWeekView($event, 'day')\"]")
	private WebElement salesForecastPageDayViewButton;
	
	@FindBy(css = "[attr-id=\"categories\"]")
	private WebElement salesForecastFilterCategoriesDropDown;
	
	@FindBy(css = "[attr-id='categories'] ul li")
	private List<WebElement> salesForecastFilterCategoriesDropDownOptions;
	
	@FindBy(css="[ng-click='gotoNextWeek($event)']")
	private WebElement salesForecastCalendarNavigationNextWeekArrow;
	
	@FindBy(css="[ng-click=\"gotoPreviousWeek($event)\"]")
	private WebElement salesForecastCalendarNavigationPreviousWeekArrow;
	
	@FindBy(css = "div.sch-calendar-day-dimension")
	private List<WebElement> salesForecastWeekViewDayMonthDateLabels;

	@FindBy(css = "div.sch-calendar-date-label")
	private List<WebElement> salesForecastWeekViewStart;
	
	@FindBy(className="kpi")
	private List<WebElement> salesForecastDataCards;

	@FindBy (css = "div[ng-if='!storeClosed()']")
	private WebElement projectedTrafficGraphWeekView;
	
	@FindBy (css = "div.console-navigation-item-label.Schedule")
	private WebElement consoleSchedulePageTabElement;

	@FindBy (css = "span.sch-control-kpi-value.blue")
	private WebElement forecastShoppersValue;

	@FindBy (xpath = "//span[contains(text(), 'Partially Solved')]")
	private WebElement forecastGraphError;
	
	@FindBy (css = "div.sch-calendar-day-dimension.active-day")
	private WebElement currentActiveDay;



	
	public ConsoleSalesForecastPage(){
		PageFactory.initElements(getDriver(), this);
	}
	
	/*
	 *  LEG-2411 Legion Scheduling (Guidance, Schedule) Test Plan / LEG-2422
	 *  (As a store manager, can view Projected Sales Forecast data for past and current week)
	 */
	
	@Override
	public void navigateToSalesForecastTab() throws Exception
	{
		WebElement salesForecastElement = SimpleUtils.getSubTabElement(scheduleSubNavigationViewTab, salesForecastTabLabelText);
		if(salesForecastElement != null)
		{
			salesForecastElement.click();
		}
	}
	
	@Override
	public Boolean isSalesForecastTabActive() throws Exception
	{
		if(isElementLoaded(SchedulePageSelectedSubTab) && SchedulePageSelectedSubTab.getText().toLowerCase().contains(salesForecastTabLabelText.toLowerCase()))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void navigateToSalesForecastTabWeekView() throws Exception
	{
		if(isSalesForecastTabActive())
		{
			if(isElementLoaded(salesForecastPageWeekViewButton))
			{
				salesForecastPageWeekViewButton.click();
			}
			else {
				SimpleUtils.fail("Projected Sales Week View button not Loaded.", false);
			}
		}
		else {
			SimpleUtils.fail("Projected Sales Tab not dispalyed.", false);
		}
	}
	
	@Override
	public Boolean isSalesForecastTabWeekViewActive() throws Exception {
		if(isElementLoaded(salesForecastPageWeekViewButton))
		{
			if(salesForecastPageWeekViewButton.getAttribute("class").toString().contains("enabled"))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void navigateToSalesForecastTabDayView() throws Exception
	{
		if(isElementLoaded(SchedulePageSelectedSubTab) && SchedulePageSelectedSubTab.getText().contains(salesForecastTabLabelText))
		{
			if(isElementLoaded(salesForecastPageDayViewButton))
			{
				salesForecastPageDayViewButton.click();
			}
			else {
				SimpleUtils.fail("Projected Sales Day View button not Loaded.", false);
			}
		}
		else {
			SimpleUtils.fail("Projected Sales Tab not dispalyed.", false);
		}
	}
	
	@Override
	public Boolean isSalesForecastTabDayViewActive() throws Exception {
		if(isElementLoaded(salesForecastPageDayViewButton))
		{
			if(salesForecastPageDayViewButton.getAttribute("class").toString().contains("enabled"))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Boolean verifyCurrentUserWithUserJobTitle(HashMap<String, String> propertyMap, String jobTitle)
	{
		String loggedUserName = propertyMap.get("DEFAULT_USERNAME");
		String currentUsersJobTitle = SimpleUtils.getCurrentUsersJobTitle(loggedUserName);
		if(currentUsersJobTitle.toLowerCase().contains(jobTitle.toLowerCase()))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean validateSalesForecastItemOptionWithUserJobTitle(String currentUsersSalesForecastItemOption) throws Exception
	{
		if(isElementLoaded(salesForecastFilterCategoriesDropDown))
		{
			click(salesForecastFilterCategoriesDropDown);
			if(isElementLoaded(SchedulePageSelectedSubTab) && SchedulePageSelectedSubTab.getText().contains(salesForecastTabLabelText))
			{
				if(salesForecastFilterCategoriesDropDownOptions.size() != 0) {
					if(salesForecastFilterCategoriesDropDownOptions.size() == currentUsersSalesForecastItemOption.split(",").length){
						for(WebElement salesForecastFilterCategoriesDropDownOption : salesForecastFilterCategoriesDropDownOptions) {
							if(! currentUsersSalesForecastItemOption.toLowerCase().contains(salesForecastFilterCategoriesDropDownOption.getText().toLowerCase())) {
								return false;
							}
						}
						SimpleUtils.pass("sales Forecast Filter Categories Matched with logged in User Role.");
						if(salesForecastFilterCategoriesDropDown.findElement(By.className("dropdown")).getAttribute("class").contains("open"))
							click(salesForecastFilterCategoriesDropDown);
						return true;						
					}
				}
			}
		}
		
		return false;
	}
	
//	@Override
//	public void navigateSalesForecastWeekViewPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount) throws Exception
//	{
//		if(isSalesForecastTabActive())
//		{
//			for(int i = 0; i < weekCount; i++)
//			{
//				if(nextWeekViewOrPreviousWeekView.toLowerCase().contains("next") || nextWeekViewOrPreviousWeekView.toLowerCase().contains("future"))
//				{
//					salesForecastCalendarNavigationNextWeekArrow.click();
//				}
//				else
//				{
//					salesForecastCalendarNavigationPreviousWeekArrow.click();
//				}
//			}
//		}
//		else {
//			SimpleUtils.fail("Projected Sales Tab not Active!", false);
//		}
//
//	}
//
	@Override
	public Boolean validateWeekViewWithDateFormat(String legionDateFormat)
	{
		String separator = " ";
		String salesForecastWeekViewDayMonthDateLabelsText = SimpleUtils.getListElementTextAsString(salesForecastWeekViewDayMonthDateLabels, separator);
		salesForecastWeekViewDayMonthDateLabelsText = salesForecastWeekViewDayMonthDateLabelsText.replace("\n", " ");
		if(salesForecastWeekViewDayMonthDateLabelsText.toLowerCase().contains(legionDateFormat.toLowerCase()))
		{
			return true;
		}

		return false;
	}
	
	@Override
	public Map<String, String> getSalesForecastForeCastData() throws Exception
	{
		String peakDemandText = "PEAK DEMAND";
		String totalDemandText = "TOTAL DEMAND";
		String peakTimeText = "PEAK TIME";
		Map<String, String> salesForecastData = new HashMap<String, String>();
		if(isSalesForecastTabActive())
		{
			for(WebElement dayViewProjectedsalesCardsElement : salesForecastDataCards)
			{
				if(dayViewProjectedsalesCardsElement.getText().contains(peakDemandText))
				{
					String peakDemandString = dayViewProjectedsalesCardsElement.getText().replace(peakDemandText, "").replace("\n", " ");
					String[] peakDemandStringSplit = peakDemandString.split("Projected");
					if(peakDemandStringSplit.length == 2)
					{
						String peakDemandProjected = peakDemandStringSplit[0];
						String peakDemandActual = peakDemandStringSplit[1].replace("Actual", "");
						salesForecastData.put("peakDemandProjected", peakDemandProjected);
						salesForecastData.put("peakDemandActual", peakDemandActual);
					}
				}
				else if(dayViewProjectedsalesCardsElement.getText().contains(totalDemandText))
				{
					String totalDemandString = dayViewProjectedsalesCardsElement.getText().replace(totalDemandText, "").replace("\n", " ");
					String[] totalDemandStringSplit = totalDemandString.split("Projected");
					if(totalDemandStringSplit.length == 2)
					{
						String totalDemandProjected = totalDemandStringSplit[0];
						String totalDemandActual = totalDemandStringSplit[1].replace("Actual", "");
						salesForecastData.put("totalDemandProjected", totalDemandProjected);
						salesForecastData.put("totalDemandActual", totalDemandActual);
					}
				}
				else if(dayViewProjectedsalesCardsElement.getText().contains(peakTimeText))
				{
					String peakTimeString = dayViewProjectedsalesCardsElement.getText().replace(peakTimeText, "").replace("\n", " ");
					String[] peakTimeStringSplit = peakTimeString.split("Projected");
					if(peakTimeStringSplit.length == 2)
					{
						String peakTimeProjected = peakTimeStringSplit[0];
						String peakTimeActual = peakTimeStringSplit[1].replace("Actual", "");
						salesForecastData.put("peakTimeProjected", peakTimeProjected);
						salesForecastData.put("peakTimeActual", peakTimeActual);
					}
				}
			}
		}
		else {
			SimpleUtils.fail("Projected Sales Tab not Active!", false);
		}
		
		return salesForecastData;
	}

	@Override
	public void navigateSalesForecastWeekViewTpPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount) throws Exception {

	}

	//added by Gunjan

	public boolean loadSalesForecastForWeekView() throws Exception {
		// TODO Auto-generated method stub
		boolean flag=false;
			if(isElementLoaded(salesForecastPageWeekViewButton,10)){
				click(salesForecastPageWeekViewButton);
				SimpleUtils.pass("Clicked on Projected Traffic Week View");
				if(isElementLoaded(projectedTrafficGraphWeekView,10)){
					flag = true;
					SimpleUtils.pass("Projected Traffic Loaded in Week View Successfully!");
				}else{
					SimpleUtils.fail("Projected Traffic Not Loaded in Week View Successfully!", true);
				}
			}else{
				SimpleUtils.fail("Week View button not found in Projected Traffic",true);
			}
		return flag;
	}

	public boolean loadSalesForecastForDayView() throws Exception {
		// TODO Auto-generated method stub
		boolean flag=false;
			if(isElementLoaded(salesForecastPageDayViewButton,10)){
				click(salesForecastPageDayViewButton);
				SimpleUtils.pass("Clicked on Projected Traffic Day View");
				if(isElementLoaded(projectedTrafficGraphWeekView,10)){
					flag = true;
					SimpleUtils.pass("Projected Traffic Loaded in DayView Successfully!");
				}else{
					SimpleUtils.fail("Projected Traffic Not Loaded in DayView Successfully!", true);
				}
			}else{
				SimpleUtils.fail("Day View button not found in Projected Traffic",true);
			}
			return flag;
	}

	@Override
	public boolean loadSalesForecast() throws Exception {
		// TODO Auto-generated method stub
		boolean flag=false;
		if(isElementLoaded(ProjectedTrafficSubMenu,10)){
			click(ProjectedTrafficSubMenu);
			SimpleUtils.pass("Clicked on Projected Traffic Sub Menu");
			if(isElementLoaded(salesForecastPageDayViewButton,10)){
				click(salesForecastPageDayViewButton);
				SimpleUtils.pass("Clicked on Projected Traffic Day View");
				if(isElementLoaded(projectedTrafficGraphWeekView,10)){
				flag = true;
				SimpleUtils.pass("Projected Traffic Loaded in DayView Successfully!");
				}else{
					SimpleUtils.fail("Projected Traffic Not Loaded in DayView ", true);
				}
			}else{
				SimpleUtils.fail("Day View button not found in Projected Traffic",true);
			}
			if(isElementLoaded(salesForecastPageWeekViewButton,10)){
				click(salesForecastPageWeekViewButton);
				SimpleUtils.pass("Clicked on Projected Traffic Week View");
				if(isElementLoaded(projectedTrafficGraphWeekView,10)){
				flag = true;
				SimpleUtils.pass("Projected Traffic Loaded in Week View Successfully!");
				}else{
					SimpleUtils.fail("Projected Traffic Not Loaded in Week View ", true);
				}
			}else{
				SimpleUtils.fail("Week View button not found in Projected Traffic",true);
			}
		}else{
			SimpleUtils.fail("Projected Traffic Sub Menu Tab Not Found", false);
		}
		return flag;
	}

	public void dayNavigationProjectedTraffic(String weekStarting)throws Exception{
		if(isElementEnabled(salesForecastPageDayViewButton)){
			click(salesForecastPageDayViewButton);
			SimpleUtils.pass("Clicked on Projected Traffic Day View of Week Staring " +weekStarting);
			for(int i=0;i<salesForecastWeekViewDayMonthDateLabels.size();i++){
				//int forecastShoppersValueFinal = Integer.parseInt(forecastShoppersValue.getText());
				click(salesForecastWeekViewDayMonthDateLabels.get(i));
				if(isElementLoaded(projectedTrafficGraphWeekView,10) && Integer.parseInt(forecastShoppersValue.getText())>0){
					SimpleUtils.pass("Projected Traffic Loaded in DayView Successfully! for Day " + currentActiveDay.getText()+ " and Value for Forecast is " +forecastShoppersValue.getText());
				}else if (isElementLoaded(projectedTrafficGraphWeekView,10) && Integer.parseInt(forecastShoppersValue.getText())==0 && !isElementLoaded(forecastGraphError,3)){
					SimpleUtils.pass("Store Closed on " + currentActiveDay.getText()+ " and Value for Forecast is " +forecastShoppersValue.getText());
				}else{
					SimpleUtils.fail("Projected Traffic Not Loaded in DayView for Day " + currentActiveDay.getText()+ " and Value for Forecast is " +forecastShoppersValue.getText(), true);
				}
			}
		}else{
			SimpleUtils.fail("Day View button not found in Projected Traffic",false);
		}
	}

	
	public void projectedTrafficDayWeekNavigation() throws Exception{
		if(isElementEnabled(salesForecastPageWeekViewButton)){
			click(salesForecastPageWeekViewButton);
			String weekStarting = salesForecastWeekViewStart.get(0).getText();
			SimpleUtils.pass("Clicked on Projected Traffic Week View of Week Starting " +weekStarting);
			int forecastShoppersValueFinal = Integer.parseInt(forecastShoppersValue.getText());
			if(isElementLoaded(projectedTrafficGraphWeekView,10) && forecastShoppersValueFinal>0){
				//flag = true;
				SimpleUtils.pass("Projected Traffic Loaded in Week View Successfully! for week starting " +weekStarting+ " Number of Shoppers is "+ forecastShoppersValue.getText());
				dayNavigationProjectedTraffic(weekStarting);
			}else{
				SimpleUtils.fail("Projected Traffic Not Loaded in Week View for week starting " +weekStarting+ " Number of Shoppers is "+ forecastShoppersValue.getText(), true);
			}
		}else{
			SimpleUtils.fail("Week View button not found in Projected Traffic",false);
		}

	}

	@Override
	public void loadSalesForecastforCurrentNFutureWeek(String nextWeekView, int weekCount) throws Exception{
		//boolean flag=false;
			if(isElementLoaded(ProjectedTrafficSubMenu,10)){
				click(ProjectedTrafficSubMenu);
				SimpleUtils.pass("Clicked on Projected Traffic Sub Menu");
				for(int i = 0; i < weekCount; i++) {
					if (nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future")) {
						projectedTrafficDayWeekNavigation();
						click(salesForecastCalendarNavigationNextWeekArrow);
					}
				}
			}else{
				SimpleUtils.fail("Projected Traffic Sub Menu Tab Not Found", false);
			}
		}

	@FindBy(css = "[ng-click=\"$ctrl.openFilter()\"]")
	private WebElement filterButton;
	@FindBy(css = "div.lg-filter__wrapper")
	private WebElement filterPopup;
	@FindBy(css = "[ng-repeat=\"(key, opts) in $ctrl.displayFilters\"]")
	private List<WebElement> FilterElements;
	@Override
	public boolean verifyChannelOrCategoryExistInForecastPage(String filterType, String filteryName) throws Exception {
		WebElement filterLabel;
		boolean isExisting = false;
		refreshPage();
		waitForSeconds(10);
		if (isElementLoaded(filterButton,20)) {
			if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
				click(filterButton);
			for (WebElement FilterElement : FilterElements) {
				if (FilterElement.findElement(By.cssSelector("div")).getText().trim().equalsIgnoreCase(filterType)){
					List<WebElement> filters = FilterElement.findElements(By.cssSelector("input-field[type=\"checkbox\"]"));
					for (WebElement filter : filters) {
						filterLabel = filter.findElement(By.cssSelector("label"));
						if (filterLabel != null && filterLabel.getText().equalsIgnoreCase(filteryName)) {
							System.out.println(filterLabel.getText() + " 2: " + filteryName);
							isExisting =  true;
							break;
						}
					}
				}
			}
		} else {
			SimpleUtils.fail("Filters button not found on forecast page!", false);
		}
		return isExisting;
	}
}
	

