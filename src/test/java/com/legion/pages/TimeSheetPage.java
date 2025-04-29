package com.legion.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;

public interface TimeSheetPage {

	public void addTimeClock(String employee, String endTime) throws Exception  ;
	public void checkTimesheetHistory() throws Exception ;

	public void clickOnTimeSheetConsoleMenu()  throws Exception;

	public boolean isTimeSheetPageLoaded() throws Exception;

	public void openATimeSheetWithClockInAndOut() throws Exception;

	public void clickOnEditTimesheetClock(int index) throws Exception;

	public void clickOnDeleteClockButton() throws Exception;

	public void closeTimeSheetDetailPopUp() throws Exception;
	
	public ArrayList<String> hoverOnClockIconAndGetInfo() throws Exception;

	public void addNewTimeClock(String location, String employee, String workRole,
			String startTime, String endTime, String breakStartTime, String breakEndTime, String notes , String DaysFromTodayInPast) throws Exception;

	public void valiadteTimeClock(String location, String employee, String workRole, String startTime,
			String endTime,String breakStartTime, String breakEndTime, String notes, String DaysFromTodayInPast) throws Exception;

	public void updateTimeClock(String location, String employee, String startTime,
								  String endTime, String notes, String DaysFromTodayInPast) throws Exception;

	public void timesheetAutoApproval(String location, String employee, String startTime,
									  String endTime, String notes)throws Exception;

	public void clickOnPayPeriodDuration() throws Exception;

	public void clickOnWeekDuration() throws Exception;

	public HashMap<String, Float> getTimeClockHoursByDate(String DaysFromTodayInPast, String timeClockEmployee) throws Exception;

	public String getActiveDayWeekOrPayPeriod() throws Exception;
	
	public void navigateDayWeekOrPayPeriodToPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount);
	
	public void clickOnDayView() throws Exception;

	public void timesheetSmartCard() throws Exception;

	public void openFirstPendingTimeSheet() throws Exception;

	public boolean isTimeSheetPopupApproveButtonActive() throws Exception;

	public boolean seachAndSelectWorkerByName(String workerName) throws Exception;

	public List<WebElement> getTimeSheetDisplayedWorkersDayRows();

	public HashMap<String, Float> getTimesheetWorkerHoursByDay(WebElement workersDayRow);

	public String getWorkerTimeSheetAlert(WebElement workersDayRow)  throws Exception;

	public void openWorkerDayTimeSheetByElement(WebElement workersDayRow) throws Exception;

	public boolean isTimesheetPopupModelContainsKeyword(String keyword) throws Exception;

	public boolean isWorkerDayRowStatusPending(WebElement workerDayRow) throws Exception;

	public void clickOnApproveButton() throws Exception;

	public boolean isTimeSheetApproved() throws Exception;

	public String getTimeClockHistoryText() throws Exception;

	public void displayTimeClockHistory() throws Exception;

	public List<WebElement> getAllTimeSheetEditBtnElements() throws Exception;

	public void clickOnEditTimesheetClock(WebElement webElement) throws Exception;

	public boolean isTimeSheetWorkerRowContainsCheckbox(WebElement workerRow);

	public List<WebElement> getTimeSheetWorkersRow()  throws Exception;

	public String getWorkerNameByWorkerRowElement(WebElement workerRow) throws Exception;

	public HashMap<String, Float> getWorkerTotalHours(WebElement workerRow);

	public void vadidateWorkerTimesheetLocationsForAllTimeClocks(WebElement workersDayRow) throws Exception;

	public void addBreakToOpenedTimeClock(String breakStartTime, String breakEndTime) throws Exception;

	public void closeTimeClockHistoryView() throws Exception;

	public void addTimeClockCheckInOnDetailPopupWithDefaultValue() throws Exception;

	public boolean isTimeClockApproved(WebElement workerTimeClock) throws Exception;

	public void removeTimeClockEntryByLabel(String label) throws Exception;

	public boolean isTimeSheetDetailsTableLoaded() throws Exception;

	public void clickOnWeekView();

	public String getTimeSheetActiveDurationType();

	public void exportTimesheet() throws Exception;

	public HashMap<String, Float> getTotalTimeSheetCarouselCardsHours() throws Exception;

	public void validateLocationFilterIfNoLocationSelected(String locationFilterAllLocations) throws Exception;

	public void verifyTimesheetTableIfNoLocationSelected() throws  Exception;

	public void clickImmediatePastToCurrentActiveWeekInDayPicker();

	public void validateLocationFilterIfDefaultLocationSelected(String locationFilterDefaultLocations) throws Exception;

	public void validateLocationFilterIfSpecificLocationSelected(String locationFilterSpecificLocations) throws Exception;

	public void clickWorkerRow(List<WebElement> allWorkersRow, String locationFilterSpecificLocations) throws Exception;

	public void verifyTMsRecordInTimesheetTab() throws Exception;

	public String getWeekStartingDay() throws Exception;

	public void clickPreviousDayArrow(int previousArrowCount) throws Exception;

	public void clickOnPPWeeklyDuration() throws Exception;
    public String verifyTimesheetSmartCard() throws Exception;
	public String verifyTimesheetDueHeader() throws Exception;
	public void validateLoadingOfTimeSheetSmartCard() throws Exception;
	public int getUnplannedClocksValueNtext() throws Exception;
	public int getUnplannedClocksDetailSummaryValue() throws Exception;
	public void goToSMView(List<String> searchLocation, String datePickerTxtDMView,
						   int locationCount, int totalUnplannedClocksOnDMView, int totalTimesheetsOnDMView) throws Exception;
	public List<String> getLocationName() throws Exception;
	public int getUnplannedClocksOnDMView() throws Exception;
	public int getTotalTimesheetsOnDMView() throws Exception;
	public int getUnplannedClockSmartCardOnDMView() throws Exception;
	public int getTotalTimesheetFromSmartCardOnDMView() throws Exception;
	public void validateLoadingOfTimeSheetSmartCard(String nextWeekViewOrPreviousWeekView) throws Exception;
	public void goToSMView() throws Exception;
	public void validateLoadingOfComplianceOnDMView(String nextWeekViewOrPreviousWeekView, boolean currentWeek) throws Exception;
	public void clickOnComplianceConsoleMenu() throws Exception;
	public void toNFroNavigationFromDMDashboardToDMCompliance(String CurrentWeek) throws Exception;
	public void clickOnComplianceViolationSectionOnDashboard() throws Exception;
	public List<String> getAlertsDataFromSmartCard() throws Exception;
	public int getApprovalRateFromTimesheetByLocation(String location) throws Exception;
	public void verifyCurrentWeekIsSelectedByDefault(String currentWeek) throws Exception;

	public String verifyLocationList() throws Exception;
	public List<String> getTimesheetApprovalRateOnDMViewSmartCard() throws Exception;
	public void validateThePresenceOfRefreshButton() throws Exception;
	public void validateRefreshTimestamp() throws Exception;
	public void validateRefreshWhenNavigationBack() throws Exception;
	public void validateRefreshFunction() throws Exception;
	public void validateRefreshPerformance() throws Exception;
	public void navigateToSchedule() throws Exception;
	public void clickOnRefreshButton() throws Exception;
	public void navigateToPreviousWeek() throws Exception;
	public void navigateToNextWeek() throws Exception;
	public void clickOnGivenLocation(String location) throws Exception;
	public String getTimesheetApprovalForGivenLocationInDMView(String location) throws Exception;
	public boolean isWorkerDisplayInTimesheetTable() throws Exception;
	public void approveAnyTimesheet() throws Exception;
	public void clickOnApproveButtonInTimesheetTable() throws Exception;
	public boolean isTimeSheetApproveButtonActive() throws Exception;
	public String getApprovalRateFromTIMESHEETDUESmartCard() throws Exception;
	public void reaggregateTimesheet() throws Exception;
	public void saveTimeSheetDetail() throws Exception;
	public void clickOnTimeSheetDetailBackBtn() throws Exception;
	public void addTimeClockCheckInOutOnDetailWithDefaultValue(String location) throws Exception;
	public void deleteExistingClocks() throws Exception;
	public boolean isTimeSheetConsoleMenuTabLoaded() throws Exception;
	public boolean isTimesheetDMView() throws Exception;
	public List<String> getDataFromTimesheetTableForGivenLocationInDMView(String location) throws Exception;
	public int getTotalTimesheetInSMView() throws Exception;
	public HashMap<String, Float> getWorkerAllHours(WebElement workerRow);
	public void validateTheContentOnTIMESHEETAPPROVALRATESmartCard(String dueDate) throws Exception;
	public void verifyPTOOfEmployee(String employee, String PTO) throws Exception;
	}
