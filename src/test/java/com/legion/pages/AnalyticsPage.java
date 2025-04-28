package com.legion.pages;

import java.util.HashMap;

public interface AnalyticsPage {
	public void gotoAnalyticsPage() throws Exception;

	public HashMap<String, Float> getForecastedHours() throws Exception;

	public HashMap<String, Float> getScheduleHours() throws Exception;

	public void navigateToNextWeek() throws Exception;

	public String getAnalyticsActiveDuration();

	public boolean loadAnalyticsTab() throws Exception;

	public void clickOnAnalyticsSubTab(String subTabLabel) throws Exception;

	public void exportKPIReportByTitle(String kpiTitle) throws Exception;

	public void clickOnAnalyticsConsoleMenu() throws Exception;

	public void selectAnalyticsCheckBoxByLabel(String label) throws Exception;

	public HashMap<String, Float> getAnalyticsKPIHoursByLabel(String label) throws Exception;

	public boolean isReportsPageLoaded() throws Exception;

	public void switchAllLocationsOrSingleLocation(boolean isAllLocations) throws Exception;

	public boolean isSpecificReportLoaded(String reportName) throws Exception;

	public void mouseHoverAndExportReportByName(String reportName) throws Exception;

	public void mouseHoverAndRefreshByName(String reportName) throws Exception;

	public boolean isSpecificReportsTabBeenSelected(String reportsTabName) throws Exception;
}
