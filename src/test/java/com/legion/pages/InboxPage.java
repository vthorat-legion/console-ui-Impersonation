package com.legion.pages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface InboxPage {

    //Added by Nora
    public List<String> getSelectedOperatingHours() throws Exception;

    //Added by Julie
    public void createGFEAnnouncement() throws Exception;
    public void clickOnInboxConsoleMenuItem() throws Exception;
    public LinkedHashMap<String, List<String>> getGFEWorkingHours() throws Exception;
    public String getGFEFirstDayOfWeek() throws Exception;
    public boolean compareGFEWorkingHrsWithRegularWorkingHrs(LinkedHashMap<String, List<String>> GFEWorkingHours,
                                                             LinkedHashMap<String, List<String>> regularHoursFromControl) throws Exception;
    public void verifyVSLInfo(boolean isVSLTurnOn) throws Exception;
    public HashMap<String, String> getTheContentOfWeekSummaryInGFE() throws Exception;
    public boolean compareDataInGFEWeekSummary(HashMap<String, String> theContentOfWeekSummaryInGFE,
                                               HashMap<String, List<String>> DataFromSchedulingPolicyGroups) throws Exception;

    //Added by Marym
    public void checkCreateAnnouncementPageWithGFETurnOnOrTurnOff(boolean isTurnOn) throws Exception;
    public void checkCreateGFEPage() throws Exception;

    //Added by Haya
    public void sendToTM(String nickName) throws Exception;
    public void changeTheMessage(String message) throws Exception;
    public void verifyMessageIsExpected(String message) throws Exception;
    public void chooseOneDayToClose(String day) throws Exception;
    public void verifyDayIsClosed(String day) throws Exception;
    public void changeOperatingHrsOfDay(String day, String startTime, String endTime) throws Exception;
    public void verifyOperatingHrsOfDay(String dayExpected, String startTimeExpected, String endTimeExpected) throws Exception;
    public void changeWeekSummaryInfo(String minimumShifts, String averageHrs) throws Exception;
    public void clickSendBtn() throws Exception;
    public void clickFirstGFEInList() throws Exception;
    public void clickAcknowledgeBtn() throws Exception;
    public void addComment(String comment) throws Exception;
    public void verifyComment(String comment, String name) throws Exception;
    public void verifyVSLTooltip() throws Exception;
    public boolean isAnnouncementListPanelDisplay() throws Exception;

}
