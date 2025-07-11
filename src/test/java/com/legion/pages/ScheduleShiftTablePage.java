package com.legion.pages;

import org.openqa.selenium.WebElement;

import java.util.*;

public interface ScheduleShiftTablePage {
    public void reduceOvertimeHoursOfActiveWeekShifts() throws Exception;
    public List<WebElement> getAvailableShiftsInDayView();
    public void verifyNewAddedShiftFallsInDayPart(String nameOfTheShift, String dayPart) throws Exception;
    public ArrayList<WebElement> getAllAvailableShiftsInWeekView();
    public List<WebElement> getUnAssignedShifts();
    public HashMap<String, String> getFourUpComingShifts(boolean isStartTomorrow, String currentTime) throws Exception;
    public void scheduleUpdateAccordingToSelectWeek() throws Exception;
    public void getHoursAndTeamMembersForEachDaysOfWeek();
    public boolean verifyActiveWeekDailyScheduleHoursInWeekView();
    public void verifyScheduledHourNTMCountIsCorrect() throws Exception;
    public boolean verifyActiveWeekTeamMembersCountAvailableShiftCount();
    public ArrayList<String> getScheduleDayViewGridTimeDuration();
    public ArrayList<String> getScheduleDayViewBudgetedTeamMembersCount();
    public ArrayList<String> getScheduleDayViewScheduleTeamMembersCount();
    public void verifyShiftsChangeToOpenAfterTerminating(List<Integer> indexes, String name, String currentTime) throws Exception;
    public float newCalcTotalScheduledHourForDayInWeekView() throws Exception;
    public boolean newVerifyActiveWeekDailyScheduleHoursInWeekView() throws Exception;
    public float getActiveShiftHoursInDayView();
    public ArrayList<String> getAvailableJobTitleListInWeekView();
    public ArrayList<String> getAvailableJobTitleListInDayView();
    public WebElement clickOnProfileOfUnassignedShift() throws Exception;
    public List<String> getTheShiftInfoByIndex(int index) throws Exception;
    public List<String> getTheGreyedShiftInfoByIndex(int index) throws Exception;
    public String getTotalHrsFromRightStripCellByIndex(int index) throws Exception;
    public List<String> getTheShiftInfoInDayViewByIndex(int index) throws Exception;
    public String getIIconTextInfo(WebElement shift) throws Exception;
    public int getShiftIndexById(String id) throws Exception;
    public List<WebElement> getAllShiftsOfOneTM(String name) throws Exception;
    public List<WebElement> getAllOOOHShifts() throws Exception;
    public List<String> getComplianceMessageFromInfoIconPopup(WebElement shift) throws Exception;
    public WebElement getShiftById(String id) throws Exception;
    public WebElement clickOnProfileIconOfShiftInDayView(String openOrNot) throws Exception;
    public String getTheShiftInfoByIndexInDayview(int index) throws Exception;
    public void verifySearchResult (String firstNameOfTM, String lastNameOfTM, String workRole, String jobTitle, List<WebElement> searchResults) throws Exception;
    public void verifyUpComingShiftsConsistentWithSchedule(Map<String, String> dashboardShifts, HashMap<String, String> scheduleShifts) throws Exception;
    public void verifyNewShiftsAreShownOnSchedule(String name) throws Exception;
    public List<Integer> getAddedShiftIndexes(String name) throws Exception;
    public boolean areShiftsPresent() throws Exception;
    public int getShiftsCount() throws Exception;
    public String getShiftTextByIndex(int indexOfShift);
    public boolean isProfileIconsClickable() throws Exception;
    public int getOTShiftCount();
    public void validateXButtonForEachShift() throws Exception;
    public float getShiftHoursByTMInWeekView(String teamMember);
    public void verifyWeeklyOverTimeAndFlag(String teamMemberName) throws Exception;
    public Map<String, String> getHomeLocationInfo() throws Exception;
    List<String> getWeekScheduleShiftTimeListOfWeekView(String teamMemberName) throws Exception;
    public HashMap<String, String> getTheHoursNTheCountOfTMsForEachWeekDays() throws Exception;
    public HashMap<String, List<String>> getTheContentOfShiftsForEachWeekDay() throws Exception;
    public void verifyDayHasShifts(String day) throws Exception;
    public List<String> getDayShifts(String index) throws Exception;
    public void verifyNoShiftsForSpecificWeekDay(List<String> weekDaysToClose) throws Exception;
    public void verifyStoreIsClosedForSpecificWeekDay(List<String> weekDaysToClose) throws Exception;
    public void dragOneShiftToMakeItOverTime() throws Exception;
    public void clickProfileIconOfShiftByIndex(int index) throws Exception;
    public void clickViewStatusBtn() throws Exception;
    public int getRandomIndexOfShift();
    public void dragOneAvatarToAnother(int startIndex, String firstName, int endIndex) throws Exception;
    public void verifyConfirmStoreOpenCloseHours() throws Exception;
    public boolean ifMoveAnywayDialogDisplay() throws Exception;
    public int getTheIndexOfTheDayInWeekView(String date) throws Exception;
    public HashMap<String,Integer> dragOneAvatarToAnotherSpecificAvatar(int startIndexOfTheDay, String user1, int endIndexOfTheDay, String user2) throws Exception;
    public void verifyMessageInConfirmPage(String expectedMassageInSwap, String expectedMassageInAssign) throws Exception;
    public void verifyMessageOnCopyMoveConfirmPage(String expectedMsgInCopy, String expectedMsgInMove) throws Exception;
    public void verifyConfirmBtnIsDisabledForSpecificOption(String optionName) throws Exception;
    public void selectCopyOrMoveByOptionName(String optionName) throws Exception;
    public void selectSwapOrAssignOption(String action) throws Exception;
    public void clickConfirmBtnOnDragAndDropConfirmPage() throws Exception;
    public List<String> getShiftSwapDataFromConfirmPage(String action) throws Exception;
    public int verifyDayHasShiftByName(int indexOfDay, String name) throws Exception;
    public String getWeekDayTextByIndex(int index) throws Exception;
    public boolean verifySwapAndAssignWarningMessageInConfirmPage(String expectedMessage, String action) throws Exception;
    public void clickCancelBtnOnDragAndDropConfirmPage() throws Exception;
    public List<String> getOpenShiftInfoByIndex(int index) throws Exception;
    public List<WebElement> getOneDayShiftByName(int indexOfDay, String name) throws Exception;
    public void dragOneShiftToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception;
    public String getNameOfTheFirstShiftInADay(int dayIndex) throws Exception;
    public String getWarningMessageInDragShiftWarningMode() throws Exception;
    public void clickOnOkButtonInWarningMode() throws Exception;
    public void moveAnywayWhenChangeShift() throws Exception;
    public void copyAnywayWhenChangeShift() throws Exception;
    public void verifyShiftIsMovedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception;
    public void verifyShiftIsCopiedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception;
    public WebElement getTheShiftByIndex(int index) throws Exception;
    public int getTheIndexOfEditedShift() throws Exception;
    public int getShiftsNumberByName(String name) throws Exception;
    public boolean isDragAndDropConfirmPageLoaded() throws Exception;
    public List<WebElement> getShiftsByNameOnDayView(String name) throws Exception;
    public List<String> getWeekScheduleShiftTitles() throws Exception;
    public List<String> getDayScheduleGroupLabels() throws Exception;
    public boolean isShiftInDayPartOrNotInWeekView(int shiftIndex, String dayPart) throws Exception;
    public int getTheIndexOfShift(WebElement shift) throws Exception;
    public boolean isShiftInDayPartOrNotInDayView(int shiftIndex, String dayPart) throws Exception;
    public List<String> verifyDaysHasShifts() throws Exception;
    public void verifyShiftTimeInReadMode(String index,String shiftTime) throws Exception;
    public List<String> getIndexOfDaysHaveShifts() throws Exception;
    public void verifyShiftsHasMinorsColorRing(String minorsType) throws Exception;
    public void verifyGroupCanbeCollapsedNExpanded() throws Exception;
    public List<String> verifyGroupByTitlesOrder() throws Exception;
    public void verifyGroupByTMOrderResults() throws Exception;
    public boolean inActiveWeekDayClosed(int dayIndex) throws Exception;
    public String getFullNameOfOneShiftByIndex (int index);
    public void verifyShiftsOrderByStartTime() throws Exception;
    public void expandOnlyOneGroup(String groupName) throws Exception;
    public void verifyGroupByTitlesAreExpanded() throws Exception;
    public void verifyTimeOffCardShowInCorrectDay(int dayIndex) throws Exception;
    public void clickOnShiftInDayView (WebElement shiftInDayView) throws Exception;
    public List<String> getShiftTimeInDayViewPopUp ();
    public void clickOnXButtonInDayView () throws Exception;
    public boolean checkIfShiftInDayViewBeenMarkAsDeletedByIndex (int index) throws Exception;
    public void verifyTheEditedImgDisplayForShiftInDayByIndex (int index) throws Exception;
    public void clickTheDeleteImgForSpecifyShiftByIndex (int index) throws Exception;
    public void clickOnEditedOrDeletedImgForShiftInDayViewByIndex (int index) throws Exception;
    public void moveShiftByIndexInDayView (int index, boolean moveForeward) throws Exception;
    public ArrayList<HashMap<String,String>> getGroupByOptionsStyleInfo() throws Exception;
    public void verifyComplianceForShiftByIndex(String violation, int index) throws Exception;
    public String getTheTooltipOfClockImgsByIndex (int index) throws Exception;
    public String getTheTooltipOfScheduleSummaryHoursByIndex (int index) throws Exception;
    public String getTheTooltipOfScheduleSummaryHoursInDayViewByIndex (int index) throws Exception;
    public float calcTotalScheduledHourForOneDayInWeekView(int indexOfDay) throws Exception;
    public List<String> getAllDifferenceHrsArrowImg ();
    public HashMap<String, String> getHrsOnTooltipOfScheduleSummaryHoursByIndex (int index) throws Exception;
    public HashSet<Integer> verifyCanSelectMultipleShifts(int shiftCount) throws Exception;
    public void selectSpecificShifts(HashSet<Integer> shiftIndexes) throws Exception;
    public void rightClickOnSelectedShifts(HashSet<Integer> selectedIndex) throws Exception;
    public void verifyTheContentOnBulkActionMenu(int selectedShiftCount) throws Exception;
    public void clickOnBtnOnBulkActionMenuByText(String action) throws Exception;
    public void verifySelectedShiftsAreMarkedWithX(HashSet<Integer> selectedIndexes) throws Exception;
    public LinkedHashMap<String, Integer> getWeekDayAndDate() throws Exception;
    public void bulkDeleteTMShiftsInWeekView(String teamMemberName) throws Exception;
    public void bulkDeleteAllShiftsInDayView() throws Exception;
    public List<String> getSelectedWorkDays(HashSet<Integer> set) throws Exception;
    public List<WebElement> selectMultipleDifferentAssignmentShiftsOnOneDay(int shiftCount, int dayIndex) throws Exception;
    public List<WebElement> selectMultipleSameAssignmentShifts(int shiftCount, String tmName) throws Exception;
    public List<WebElement> selectMultipleDifferentAssignmentShifts(int shiftCount) throws Exception;
    public void dragBulkShiftToAnotherDay(List<WebElement> selectedShifts, int endIndex, boolean needConfirmChangeModalDisplay) throws Exception;
    public boolean checkIfBulkDragAndDropConfirmChangeModalDisplay() throws Exception;
    public String getBulkDragAndDropConfirmChangeInfo () throws Exception;
    public void selectMoveOrCopyBulkShifts (String moveOrCopy) throws Exception;
    public void enableOrDisableAllowComplianceErrorSwitch (boolean enableOrDisable) throws Exception;
    public void enableOrDisableAllowConvertToOpenSwitch (boolean enableOrDisable) throws Exception;
    public void expandSpecificCountGroup(int count) throws Exception;
    public int getOneDayShiftCountByIndex(int index) throws Exception;
    public void unSelectAllBulkSelectedShifts();
    public boolean isInfoIconLoaded(int index) throws Exception;
    public boolean isProfileIconLoaded(int index) throws Exception;
    public boolean isShiftTotalLengthLoaded(int index) throws Exception;
    public boolean isShiftDurationInBoxLoaded(int index) throws Exception;
    public boolean isProfileNameAndWorkRoleLoaded(int index) throws Exception;
    public boolean isShiftLengthLoaded(int index) throws Exception;
    public boolean isShiftJobTitleLoaded(int index) throws Exception;
//    public boolean isShiftDurationInAvailabilityLoaded(int index) throws Exception;
    public boolean isShiftJobTitleMediumInAvailabilityLoaded(int index) throws Exception;
    public boolean isShiftJobTitleLargeInAvailabilityLoaded(int index) throws Exception;
    public boolean isShiftLocationInAvailabilityLoaded(int index) throws Exception;
    public boolean isShiftInfoIconInAvailabilityLoaded(int index) throws Exception;
    public boolean isMyScheduleShiftLocationLoaded(int index) throws Exception;
    public boolean isMyScheduleShiftWorkRoleLoaded(int index) throws Exception;
    public boolean isMyScheduleProfileNameLoaded(int index) throws Exception;
    public void verifyGroupCannotbeCollapsedNExpanded() throws Exception;
    public String getSpecificGroupByChildLocationStatus(String childLocation);
    public String clickActionIconForSpecificGroupByChildLocation(String childLocation);
    public void clickOnSpecificButtonsGroupByActionPopup(String buttonName) throws Exception;
    public List<String> getButtonNamesFromGroupByActionPopup() throws Exception;
    public String getTotalBudgetFromSTAFFSmartCard() throws Exception;
    public boolean isScheduleTableDisplay () throws Exception;
    public HashSet<Integer> getAddedShiftsIndexesByPlusIcon() throws Exception;
    public void verifyConfirmBtnIsDisabledOnDragAndDropConfirmPage() throws Exception;
    public void verifySwapBtnIsEnabledOnDragAndDropConfirmPage() throws Exception;
    public void verifyAssignBtnIsEnabledOnDragAndDropConfirmPage() throws Exception;
    public boolean isOkButtonInWarningModeLoaded() throws Exception;
    public void bulkEditTMShiftsInWeekView(String teamMemberName) throws Exception;
    public List<WebElement> selectMultipleSameAssignmentShiftsOnOneDay(int shiftCount, String tmName, int dayIndex) throws Exception;
    public HashSet<Integer> verifyCanSelectMultipleShiftsOnOneDay(int shiftCount, int dayIndex) throws Exception;
    public void rightClickOnSelectedShiftInDayView(int index) throws Exception;
}
