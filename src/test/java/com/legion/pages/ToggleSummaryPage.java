package com.legion.pages;

import java.util.HashMap;
import java.util.List;

public interface ToggleSummaryPage {
    public HashMap<String, String> getOperatingHrsValue(String day) throws Exception;
    public String getTheEarliestAndLatestTimeInSummaryView(HashMap<String, Integer> schedulePoliciesBufferHours) throws Exception;
    public void toggleSummaryView() throws Exception;
    public boolean isSummaryViewLoaded() throws Exception;
    public void verifyClosedDaysInToggleSummaryView(List<String> weekDaysToClose) throws Exception;
    public String getWeekInfoBeforeCreateSchedule() throws Exception;
    public List<String> getBudgetedHoursOnSTAFF() throws Exception;
    public String getBudgetOnWeeklyBudget() throws Exception;
    public float getStaffingGuidanceHrs() throws Exception;
    public boolean isLocationGroup();
    public void verifyOperatingHrsInToggleSummary(String day, String startTime, String endTime) throws Exception;
    public void verifyTheContentInRosterUpdatesSection() throws Exception;
    public void verifyTheContentInStaffSection() throws Exception;
    public void verifyTheContentInOperatingHoursSection() throws Exception;
    public String autoFillOpenShifts() throws Exception;
    public List<String> getChildLocationList() throws Exception;
}
