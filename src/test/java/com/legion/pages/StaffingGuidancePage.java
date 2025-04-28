package com.legion.pages;

import java.util.HashMap;
import java.util.List;
public interface StaffingGuidancePage {

	public void navigateToStaffingGuidanceTab() throws Exception;
	public Boolean isStaffingGuidanceTabActive() throws Exception;
	public void navigateToStaffingGuidanceTabWeekView() throws Exception;
	public Boolean isStaffingGuidanceForecastTabWeekViewActive() throws Exception;
	public void navigateToStaffingGuidanceTabDayView() throws Exception;
	public Boolean isStaffingGuidanceForecastTabDayViewActive() throws Exception;
	public List<String> getStaffingGuidanceForecastDayViewTimeDuration() throws Exception;
	public List<Integer> getStaffingGuidanceForecastDayViewItemsCount() throws Exception;
	public List<Integer> getStaffingGuidanceForecastDayViewTeamMembersCount() throws Exception;
	public List<String> getStaffingGuidanceDayDateMonthLabelsForWeekView() throws Exception;
	public List<Float> getStaffingGuidanceHoursCountForWeekView() throws Exception;
	public void clickOnStaffingGuidanceAnalyzeButton() throws Exception;
	public Boolean isStaffingGuidanceAnalyzePopupAppear() throws Exception;
	public List<HashMap<String, String>> getStaffingGuidanceVersionHistory() throws Exception;
	public List<HashMap<String, String>> getAnalyzePopupStaffingGuidanceAndLatestVersionData() throws Exception;
	public String getActiveWorkRole() throws Exception;
	public boolean loadStaffingGuidance() throws Exception;
	public void navigateStaffingGuidance(String nextWeekViewOrPreviousWeekView, int weekCount) throws Exception;
	
}
