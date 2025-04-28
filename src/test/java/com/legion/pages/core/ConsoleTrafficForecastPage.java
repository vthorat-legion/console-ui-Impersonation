package com.legion.pages.core;

import static com.legion.utils.MyThreadLocal.getDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.legion.pages.BasePage;
import com.legion.pages.SalesForecastPage;
import com.legion.pages.TrafficForecastPage;
import com.legion.utils.SimpleUtils;

public class ConsoleTrafficForecastPage extends BasePage implements TrafficForecastPage{

	//Object[][] salesForecastCategoriesOptions = JsonUtil.getArraysFromJsonFile("src/test/resources/salesForecastCategoriesOptions.json");
	@FindBy(className="sub-navigation-view")
	private List<WebElement> scheduleSubNavigationViewTab;
	
	@FindBy(css = "div.sub-navigation-view-link.active")
	private WebElement SchedulePageSelectedSubTab;
	
	@FindBy(css="[ng-click=\"selectDayWeekView($event, 'week')\"]")
	private WebElement salesForecastPageWeekViewButton;
	
	String salesForecastTabLabelText = "PROJECTED TRAFFIC";
	
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
	
	@FindBy(className = "sch-calendar-day-dimension")
	private List<WebElement>salesForecastWeekViewDayMonthDateLabels;
	
	@FindBy(className="kpi")
	private List<WebElement> salesForecastDataCards;
	
	public ConsoleTrafficForecastPage(){
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
		if(isElementLoaded(SchedulePageSelectedSubTab) && SchedulePageSelectedSubTab.getText().toLowerCase().contains(salesForecastTabLabelText.toLowerCase()))
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
		if(isElementLoaded(SchedulePageSelectedSubTab) && SchedulePageSelectedSubTab.getText().toLowerCase().contains(salesForecastTabLabelText.toLowerCase()))
		{
			if(salesForecastFilterCategoriesDropDownOptions.size() != 0) {
				if(salesForecastFilterCategoriesDropDownOptions.size() == currentUsersSalesForecastItemOption.split(",").length){
					for(WebElement salesForecastFilterCategoriesDropDownOption : salesForecastFilterCategoriesDropDownOptions) {
						if(! currentUsersSalesForecastItemOption.toLowerCase().contains(salesForecastFilterCategoriesDropDownOption.getText().toLowerCase())) {
							return false;
						}
					}
					SimpleUtils.pass("sales Forecast Filter Categories Matched with logged in User Role.");
					return true;						
				}
			}
		}
		return false;
	}
	
	@Override
	public void navigateSalesForecastWeekViewTpPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount) throws Exception
	{
		if(isSalesForecastTabActive())
		{
			for(int i = 0; i < weekCount; i++)
			{
				if(nextWeekViewOrPreviousWeekView.toLowerCase().contains("next") || nextWeekViewOrPreviousWeekView.toLowerCase().contains("future"))
				{
					salesForecastCalendarNavigationNextWeekArrow.click();
				}
				else
				{
					salesForecastCalendarNavigationPreviousWeekArrow.click();
				}
			}
		}
		else {
			SimpleUtils.fail("Projected Sales Tab not Active!", false);
		}
		
	}
	
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
		String peakTrafficText = "PEAK TRAFFIC";
		String totalTrafficText = "TOTAL TRAFFIC";
		String peakTimeText = "PEAK TIME";
		Map<String, String> salesForecastData = new HashMap<String, String>();
		if(isSalesForecastTabActive())
		{
			for(WebElement dayViewProjectedsalesCardsElement : salesForecastDataCards)
			{
				if(dayViewProjectedsalesCardsElement.getText().contains(peakTrafficText))
				{
					String peakTrafficString = dayViewProjectedsalesCardsElement.getText().replace(peakTrafficText, "").replace("\n", " ");
					String[] peakTrafficStringSplit = peakTrafficString.split("Projected");
					if(peakTrafficStringSplit.length == 2)
					{
						String peakTrafficProjected = peakTrafficStringSplit[0];
						String peakTrafficActual = peakTrafficStringSplit[1].replace("Actual", "");
						salesForecastData.put("peakTrafficProjected", peakTrafficProjected);
						salesForecastData.put("peakTrafficActual", peakTrafficActual);
					}
				}
				else if(dayViewProjectedsalesCardsElement.getText().contains(totalTrafficText))
				{
					String totalTrafficString = dayViewProjectedsalesCardsElement.getText().replace(totalTrafficText, "").replace("\n", " ");
					String[] totalTrafficStringSplit = totalTrafficString.split("Projected");
					if(totalTrafficStringSplit.length == 2)
					{
						String totalTrafficProjected = totalTrafficStringSplit[0];
						String totalTrafficActual = totalTrafficStringSplit[1].replace("Actual", "");
						salesForecastData.put("totalTrafficProjected", totalTrafficProjected);
						salesForecastData.put("totalTrafficActual", totalTrafficActual);
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

	
}
