package com.legion.pages;

import java.util.List;
import java.util.Map;

public interface ScheduleCommonPage {
    public void clickOnScheduleConsoleMenuItem();
    public void goToSchedulePage() throws Exception;
    public boolean isSchedulePage() throws Exception;
    public void clickOnWeekView() throws Exception;
    public void clickOnDayView() throws Exception;
    public void navigateDayViewWithIndex(int dayIndex);
    public void navigateDayViewWithDayName(String dayName) throws Exception;
    public void navigateWeekViewOrDayViewToPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount);
    public void navigateToNextDayIfStoreClosedForActiveDay() throws Exception;
    public boolean isStoreClosedForActiveWeek() throws Exception;
//    public void verifySelectOtherWeeks() throws Exception;
    public void clickOnNextDaySchedule(String activeDay) throws Exception;
    public void clickOnPreviousDaySchedule(String activeDay) throws Exception;
    public void validateForwardAndBackwardButtonClickable() throws Exception;
//    public void validateTheDataAccordingToTheSelectedWeek() throws Exception;
    public void clickOnScheduleSubTab(String subTabString) throws Exception;
    public Boolean verifyActivatedSubTab(String SubTabText) throws Exception;
    public Boolean isScheduleDayViewActive();
    public String getScheduleWeekStartDayMonthDate();
    public void navigateDayViewToPast(String PreviousWeekView, int dayCount);
    public void clickImmediateNextToCurrentActiveWeekInDayPicker();
    public void clickImmediatePastToCurrentActiveWeekInDayPicker();
    public String getActiveAndNextDay() throws Exception;
    public void isScheduleForCurrentDayInDayView(String dateFromDashboard) throws Exception;
    public void currentWeekIsGettingOpenByDefault(String location) throws Exception;
    public void goToScheduleNewUI() throws Exception;
    public void dayWeekPickerSectionNavigatingCorrectly() throws Exception;
    public int getTheIndexOfCurrentDayInDayView() throws Exception;
    public String getActiveWeekText() throws Exception;
    public void clickCancelButtonOnPopupWindow() throws Exception;
    void goToConsoleScheduleAndScheduleSubMenu() throws Exception;
    void navigateToNextWeek() throws Exception;
    public void navigateToPreviousWeek() throws Exception;
    public void goToSpecificWeekByDate(String date) throws Exception;
    public void verifyTMSchedulePanelDisplay() throws Exception;
    public String convertDateStringFormat(String dateString) throws Exception;
    public Map<String, String> getActiveDayInfo() throws Exception;
    public String getHeaderOnSchedule() throws Exception;
    public void verifyHeaderOnSchedule() throws Exception;

    void goToSpecificWeekByDateNew(String date) throws Exception;

    public List<String> getYearsFromCalendarMonthYearText() throws Exception;
    public void goToSchedule() throws Exception;
    public boolean isSpecifyDayEqualWithFirstDayOfActivateWeek(String date) throws Exception;
    public void clickOnFirstWeekInWeekPicker() throws Exception;
    public void VerifyStaffListInSchedule(String name) throws Exception;
    public String getActiveWeekStartDayFromSchedule() throws Exception;
    public boolean checkIfDayAndWeekViewButtonEnabled () throws Exception;
    public Map<String, String> getSelectedWeekInfo() throws Exception;
    public void clickOnMultiWeekView() throws Exception;
}
