package com.legion.api.schedule.cost;

import java.util.HashMap;
import java.util.List;

public class ScheduleCostData {
    private HashMap<String, Integer> currentDayMinutes;
    private String totalWagesPerTM;
    private List<HashMap<String, String>> wagesBreakDownList;
    private List<HashMap<String, String>> premiumsList;
    private int dayOfTheYear;

    public ScheduleCostData(HashMap<String, Integer> currentDayMinutes, String totalWagesPerTM, List<HashMap<String, String>>
            wagesBreakDownList, List<HashMap<String, String>> premiumsList, int dayOfTheYear) {
        this.currentDayMinutes = currentDayMinutes;
        this.totalWagesPerTM = totalWagesPerTM;
        this.wagesBreakDownList = wagesBreakDownList;
        this.premiumsList = premiumsList;
        this.dayOfTheYear = dayOfTheYear;
    }

    public HashMap<String, Integer> getCurrentDayMinutes() { return currentDayMinutes; }
    public void setCurrentDayMinutes(HashMap<String, Integer> currentDayMinutes) { this.currentDayMinutes = currentDayMinutes; }

    public String getTotalWagesPerTM() { return totalWagesPerTM; }
    public void setTotalWagesPerTM(String totalWagesPerTM) { this.totalWagesPerTM = totalWagesPerTM; }

    public List<HashMap<String, String>> getWagesBreakDownList() { return wagesBreakDownList; }
    public void setWagesBreakDownList(List<HashMap<String, String>> wagesBreakDownList) { this.wagesBreakDownList = wagesBreakDownList;}

    public List<HashMap<String, String>> getPremiumsList() { return premiumsList; }
    public void setPremiumsList(List<HashMap<String, String>> premiumsList) { this.premiumsList = premiumsList;}

    public int getDayOfTheYear() { return dayOfTheYear; }
    public void setDayOfTheYear(int dayOfTheYear) { this.dayOfTheYear = dayOfTheYear; }
}
