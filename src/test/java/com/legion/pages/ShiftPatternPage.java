package com.legion.pages;

import java.util.List;

public interface ShiftPatternPage {

    public void verifyTheContentOnShiftPatternDetails(String workRole) throws Exception;
    public void verifyTheMandatoryFields() throws Exception;
    public void inputNameDescriptionNInstances(String name, String description, String instances) throws Exception;
    public String selectTheCurrentWeek() throws Exception;
    public void selectAddOnOrOffWeek(boolean isOn) throws Exception;
    public void verifyTheContentOnOffWeekSection() throws Exception;
    public void checkOrUnCheckAutoSchedule(boolean isCheck) throws Exception;
    public void checkOrUnCheckSpecificDay(boolean isCheck, String day) throws Exception;
    public void verifyTheContentOnOnWeekSection() throws Exception;
    public void verifyTheFunctionalityOfExpandWeekIcon(int weekNumber, boolean isExpanded) throws Exception;
    public void verifyTheFunctionalityOfArrowIcon(int weekNumber, boolean isExpanded) throws Exception;
    public int getWeekCount() throws Exception;
    public void deleteTheWeek(int weekNumber) throws Exception;
    public void clickOnAddShiftButton() throws Exception;
    public void verifyTheContentOnCreateNewShiftWindow() throws Exception;
    public void clickOnCancelButton() throws Exception;
    public void clickOnCreateButton() throws Exception;
    public List<String> getWarningMessages() throws Exception;
    public List<String> getBreakWarnings() throws Exception;
    public void inputShiftNameDescriptionNShiftNotes(String shiftName, String description, String shiftNotes) throws Exception;
    public void verifyWorkRoleNameShows(String workRole) throws Exception;
    public void inputStartOrEndTime(String hours, String minutes, String aOrP, boolean isStart) throws Exception;
    public List<String> selectWorkDays(List<String> daysNeedSelect) throws Exception;
    public void clickOnAddMealOrRestBreakBtn(boolean isMeal) throws Exception;
    public void deleteTheBreakByNumber(boolean isMeal, int number) throws Exception;
    public void inputShiftOffsetAndBreakDuration(int startOffset, int breakDuration, int number, boolean isMeal) throws Exception;
    public String getOnWeekContentDetails() throws Exception;
    public void clickOnPencilIcon(int weekNumber) throws Exception;
    public boolean isCreateNewShiftWindowLoaded() throws Exception;
    public boolean verifyTheEditedValuePersist(String shiftName, String description, String startTime, String endTime,
                                               List<String> selectedDays, int mealStartOffset, int mealDuration,
                                               int restStartOffset, int restDuration, String shiftNote) throws Exception;
    public String getTheNumberOfTheShifts(int weekNumber) throws Exception;
    public void clickOnDeleteBtnToDelCreatedShifts(int weekNumber) throws Exception;
    public void verifySaveTheShiftPatternRule() throws Exception;
}
