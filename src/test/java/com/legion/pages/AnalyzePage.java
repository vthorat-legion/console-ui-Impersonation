package com.legion.pages;

import java.util.ArrayList;
import java.util.HashMap;

public interface AnalyzePage {
    public void verifyAnalyzeBtnFunctionAndScheduleHistoryScroll() throws Exception;
    public void closeStaffingGuidanceAnalyzePopup() throws Exception;
    public void clickOnAnalyzeBtn(String tab) throws Exception;
    public void verifyScheduleVersion(String version) throws Exception;
    public void closeAnalyzeWindow() throws Exception;
    public String getPieChartTotalHrsFromLaborGuidanceTab() throws Exception;
    public String getPieChartTotalHrsFromHistoryTab(String scheduledOrGuidance) throws Exception;
    public String getPieChartHeadersFromHistoryTab(String scheduledOrGuidance) throws Exception;
    public ArrayList<HashMap<String,String>> getLaborGuidanceWorkRoleStyleInfo() throws Exception;
}
