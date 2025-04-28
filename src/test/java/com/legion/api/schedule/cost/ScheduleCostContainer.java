package com.legion.api.schedule.cost;

import java.util.HashMap;
import java.util.List;

public class ScheduleCostContainer {
    private HashMap<String, String> weekData;
    private List<HashMap<String, String>> dayData;
    private List<ScheduleCostData> scheduleCostDataList;

    public ScheduleCostContainer(HashMap<String, String> weekData, List<HashMap<String, String>> dayData,
                                 List<ScheduleCostData> scheduleCostDataList) {
        this.weekData = weekData;
        this.dayData = dayData;
        this.scheduleCostDataList = scheduleCostDataList;
    }

    public HashMap<String, String> getWeekData() { return weekData; }
    public void setWeekData(HashMap<String, String> weekData) { this.weekData = weekData; }

    public List<HashMap<String, String>> getDayData() { return dayData; }
    public void setDayData(List<HashMap<String, String>> dayData) { this.dayData = dayData; }

    public List<ScheduleCostData> getScheduleCostData() { return scheduleCostDataList; }
    public void setScheduleCostData(List<ScheduleCostData> scheduleCostData) { this.scheduleCostDataList = scheduleCostData; }
}
