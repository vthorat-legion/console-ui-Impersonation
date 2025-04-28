package com.legion.pages;

import java.util.HashMap;
import java.util.List;

public interface SmartCardPage {
    public HashMap<String, Float> getScheduleLabelHoursAndWages() throws Exception;
    public List<HashMap<String, Float>> getScheduleLabelHoursAndWagesDataForEveryDayInCurrentWeek() throws Exception;
    public HashMap<String, Float> updateScheduleHoursAndWages(HashMap<String, Float> scheduleHoursAndWages,
                                                                     String hours, String hoursAndWagesKey);
    public boolean isSmartCardAvailableByLabel(String cardLabel) throws Exception;
    public int getSwapCountFromSwapRequestsCard(String cardLabel) throws Exception;
    public String getsmartCardTextByLabel(String cardLabel);
    public String getWeatherTemperature() throws Exception;
    public HashMap<String, String> getHoursFromSchedulePage() throws Exception;
    public void weatherWeekSmartCardIsDisplayedForAWeek() throws Exception;
    boolean isSmartCardScrolledToRightActive() throws Exception;
    public void validateTheAvailabilityOfOpenShiftSmartcard() throws Exception;
    public boolean isViewShiftsBtnPresent() throws Exception;
    public boolean isRequiredActionSmartCardLoaded() throws Exception;
    public void clickOnViewShiftsBtnOnRequiredActionSmartCard() throws Exception;
    public void clickOnClearShiftsBtnOnRequiredActionSmartCard() throws Exception;
    public boolean verifyRedFlagIsVisible() throws Exception;
    public boolean verifyComplianceShiftsSmartCardShowing() throws Exception;
    public void verifyComplianceShiftsShowingInGrid() throws Exception;
    public void verifyClearFilterFunction() throws Exception;
    public boolean clickViewShift() throws Exception;
    public void verifyComplianceFilterIsSelectedAftClickingViewShift() throws Exception;
    public HashMap<String, Float> getScheduleBudgetedHoursInScheduleSmartCard() throws Exception;
    public boolean isSpecificSmartCardLoaded(String cardName) throws Exception;
    public int getCountFromSmartCardByName(String cardName) throws Exception;
    public void clickLinkOnSmartCardByName(String linkName) throws Exception;
    public HashMap<String, String> getBudgetNScheduledHoursFromSmartCard() throws Exception;
    public int getComplianceShiftCountFromSmartCard(String cardName) throws Exception;
    public void verifyChangesNotPublishSmartCard(int changesNotPublished) throws Exception;
    public String getChangesOnActionRequired() throws Exception;
    public String getTooltipOfUnpublishedDeleted() throws Exception;
    public List<String> getHolidaysOfCurrentWeek() throws Exception;
    public void navigateToTheRightestSmartCard() throws Exception;
    public boolean isScheduleNotPublishedSmartCardLoaded() throws Exception;
    public String getWholeMessageFromActionRequiredSmartCard() throws Exception;
    public HashMap<String, String> getMessageFromActionRequiredSmartCard() throws Exception;
    public void clickViewTemplateLinkOnMasterTemplateSmartCard() throws Exception;
    public String getBudgetValueFromWeeklyBudgetSmartCard(String cardName) throws Exception;
    public String getBudgetValueFromScheduleBudgetSmartCard() throws Exception;
    public boolean isBudgetHoursSmartCardIsLoad() throws Exception;
    public void clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard () throws Exception;
}
