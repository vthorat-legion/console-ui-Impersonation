package com.legion.pages;

import java.util.HashMap;
import java.util.Map;

public interface TrafficForecastPage {
	public void navigateToSalesForecastTab() throws Exception;
	public Boolean isSalesForecastTabActive() throws Exception;
	public void navigateToSalesForecastTabWeekView() throws Exception;
	public Boolean isSalesForecastTabWeekViewActive() throws Exception;
	public Boolean validateSalesForecastItemOptionWithUserJobTitle(String userJobTitle) throws Exception;
	public Boolean validateWeekViewWithDateFormat(String legionDateFormat);
	public Map<String, String> getSalesForecastForeCastData() throws Exception;
	public void navigateSalesForecastWeekViewTpPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount) throws Exception;
	public void navigateToSalesForecastTabDayView() throws Exception;
	public Boolean isSalesForecastTabDayViewActive() throws Exception;
	public Boolean verifyCurrentUserWithUserJobTitle(HashMap<String, String> propertyMap, String jobTitle);
}
