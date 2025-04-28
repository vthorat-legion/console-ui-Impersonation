package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.TimeSheetPage;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleTimeSheetPage extends BasePage implements TimeSheetPage{

	@FindBy(css = "div.console-navigation-item-label.Timesheet")
	private WebElement timeSheetConsoleMenuDiv;

	@FindBy(css = "div.console-navigation-item-label.Compliance")
	private WebElement complianceConsoleMenuDiv;

	@FindBy(css = "div.header-navigation-label")
	private WebElement timeSheetPageHeaderLabel;

	@FindBy(css = "div.analytics-new-table-group")
	private List<WebElement> timeSheetWorkersRows;

	@FindBy(css = "div.analytics-new-table")
	private WebElement timesheetTable;

	@FindBy(css = "div.lg-timesheet-table-improved__grid-row.lg-timesheet-table-improved__worker-day")
	private List<WebElement> timeSheetTableWorkersDayRows;

	@FindBy(css = "div.timesheet-details-modal")
	private WebElement timeSheetDetailModel;

	@FindBy(css = "[ng-repeat=\"clockIn in $ctrl.clockIns\"]")
	private List<WebElement> timeSheetDetailPopUpClocks;

	@FindBy(css = "lg-button[label=\"Delete\"]")
	private WebElement timeSheetDeleteClockBtn;

	@FindBy(css = "lg-button[label=\"Yes\"]")
	private WebElement confirmDeleteShiftYesBtn;

	@FindBy(className = "timesheet-details-modal__close")
	private WebElement timeSheetDetailsPopupCloseBtn;

	@FindBy(css = "div.lg-timeclocks__alert")
	private List<WebElement> timeClockAlertIcons;

	@FindBy(css="div.popover.fade.in")
	private WebElement popoverDiv;

	@FindBy(css="lg-button[label=\"Add Timeclock\"]")
	private WebElement addTimeClockBtn;

	@FindBy(css="lg-select[label=\"Location\"]")
	private WebElement addTCLocationField;

	@FindBy(css="input-field[label=\"Date\"]")
	private WebElement addTCDateField;

	@FindBy(css="lg-picker-input[label=\"Employee\"]")
	private WebElement addTCEmployeeField;

	@FindBy(css="select[aria-label=\"Work Role\"]")
	private WebElement addTCWorkRoleDropDown;

	@FindBy(css="input[aria-label=\"Shift Start\"]")
	private WebElement addTCShiftStartTimeTextField;

	@FindBy(css="input[aria-label=\"Shift End\"]")
	private WebElement addTCShiftEndTimeTextField;

	@FindBy (css="input[aria-label=\"Break Start\"]")
	private WebElement addTCBreakStartTimeTextField;

	@FindBy (css="input[aria-label=\"Break End\"]")
	private WebElement addTCBreakEndTimeTextField;

	@FindBy(css="input[placeholder='Enter Comment (Optional)']")
	private WebElement addTCAddNotesTextField;

	@FindBy(css="lg-button[label=\"Add\"]")
	private WebElement addTimeClockSaveBtn;

	@FindBy(css="div.lg-search-options__option-wrapper")
	private List<WebElement> dropdownOptions;

	@FindBy(className = "lg-single-calendar-month")
	private WebElement timeClockCalendarLabel;

	@FindBy(css="[ng-click=\"$ctrl.changeMonth(-1)\"]")
	private WebElement timeClockPreviousMonthArrow;

	@FindBy(css="[ng-click=\"$ctrl.changeMonth(1)\"]")
	private WebElement timeClockNextMonthArrow;

	@FindBy(css="div.lg-single-calendar-date")
	private List<WebElement> calenderDates;

	@FindBy(css = "div.lg-add-clock")
	private WebElement addClockPopup;

	@FindBy(xpath = "//div[@ng-show='!forbidModeChange']//span[text()='Week']")
	private WebElement weekViewButton;

	@FindBy(xpath = "//div[@ng-show='!forbidModeChange']//span[text()='PP Weekly']")
	private WebElement ppweeklyViewButton;

	@FindBy(css="div.lg-button-group-last")
	private WebElement payPeriodBtn;

	@FindBy(css="input[placeholder=\"You can search by name, location, and job title.\"]")
	private WebElement timeSheetWorkerSearchBox;

	@FindBy(css = "div.lg-button-group-first")
	private WebElement timeSheetDayViewBtn;

	@FindBy(css="[ng-repeat=\"clockIn in $ctrl.clockIns\"]")
	private List<WebElement> savedClockes;

	@FindBy(css = "div.day-week-picker-period.day-week-picker-period-active")
	private WebElement timeSheetActivePeriod;

	@FindBy(className="day-week-picker-arrow-left")
	private WebElement timeSheetNavigationPreviousDurationArrow;

	@FindBy(className="day-week-picker-arrow-right")
	private WebElement timeSheetNavigationNextDurationArrow;

	@FindBy(css = "lg-eg-status[type=\"Pending\"]")
	private List<WebElement> timeSheetPendingStatusList;

	@FindBy(css = "lg-button[ng-click=\"$ctrl.approve()\"]")
	private WebElement timeSheetPopUpApproveBtn;

	@FindBy(css = "lg-button[label=\"Edit\"]")
	private List<WebElement> timesheetEditBtns;

	@FindBy(css= "div.lg-picker-input__wrapper.lg-ng-animate")
	private WebElement locatioOrDatePickerPopup;

	@FindBy(css = "lg-single-calendar.lg-calendar-input__widget")
	private WebElement calendarInputWidget;

	@FindBy(css = "div.timesheet-details-modal")
	private WebElement TSPopupDetailsModel;

	@FindBy(css = "div.timesheet-details-modal__status-icon")
	private WebElement timeSheetDetailPopupApproveStatus;

	@FindBy(css = "div.lg-timeclock-activities")
	private WebElement timeClockHistoryDetailsSection;

	@FindBy(css = "div.lg-scheduled-shifts__expand")
	private WebElement expandHistoryBtn;

	@FindBy(css = "span.lg-scheduled-shifts__info.lg-scheduled-shifts__info--right")
	private List<WebElement> shiftLocationAndHours;

	@FindBy(css = "lg-button[label=\"Add Break\"]")
	private List<WebElement> timeClockAddBreakButtons;

	@FindBy (css = "lg-button[label=\"+ Add Break\"]")
	private WebElement addBreakLink;

	@FindBy(css = "input[type=\"time\"]")
	private List<WebElement> timeInputFields;

	@FindBy(css = "button.lg-icon-button.lg-icon-button--confirm")
	private WebElement timeClockConfirmBtn;

	@FindBy(css = "div.lg-timeclock-activities__collapse")
	private WebElement timeClockActivitiesCollapseBtn;

	@FindBy(css = "div.lg-no-timeclock__block.lg-no-timeclock__block--add")
	private WebElement addClockBtnOnDetailPopup;

	@FindBy(css= "lg-button[label=\"Export\"]")
	private WebElement exportTimesheetBtn;

	@FindBy(css= "lg-button[label=\"Export Anyway\"]")
	private WebElement exportAnywayTimesheetBtn;

	@FindBy(css = "div.card-carousel-fixed")
	private WebElement timesheetCarouselCardHoursDiv;

	@FindBy(css = "div.lg-toast")
	private WebElement successMsg;

	@FindBy (css="div.lg-no-timeclock")
	private WebElement noClockDisplayPanel;

	@FindBy (css="div[ng-click='$ctrl.onAdd()']")
	private WebElement addClock;

	@FindBy (css = "input-field.lg-new-time-input-text input")
	private WebElement addClockIn;

	@FindBy (css = "button.lg-icon-button--confirm")
	private WebElement saveClockBtn;

	@FindBy (xpath = "//div[contains(text(),'Clock out')]/parent::div//lg-button//span[contains(@class,'ng-binding')]")
	private WebElement editBtnAddClockOut;

	@FindBy (css = "lg-close.timesheet-details-modal__close")
	private WebElement closeTimeSheetDetailPopUp;

	@FindBy (xpath = "//div[contains(text(),'Clock in')]/parent::div/div[contains(@class,'lg-timeclocks__column--highlight')]")
	private WebElement shiftStartTimeInPopUp;

	@FindBy (xpath = "//div[contains(text(),'Clock out')]/parent::div/div[contains(@class,'lg-timeclocks__column--highlight')]")
	private WebElement shiftEndTimeInPopUp;

	@FindBy (xpath = "//div[contains(@class,'timesheet-details-modal__footer-span')][contains(text(),'Clocked:')]")
	private WebElement clockedHourValue;

	//added by Nishant

	@FindBy(xpath = "//div[@class='timesheet__dropdown']/lg-filter[@label='Location']/div/input-field")
	private WebElement locationFilter;
	@FindBy(xpath = "//div[@class='timesheet__dropdown']/lg-filter[@label='Location']/div/input-field//input")
	private WebElement txtLocationFilter;
	@FindBy(css = "div.lg-timesheet-table__placeholder")
	private WebElement timesheetTableForNoLocationSelected;
	@FindBy(xpath = "//lg-filter[@label='Location']//div[contains(@class,'lg-filter__category-items')]//input-field/ng-form")
	private List<WebElement> locationCheckbox;
	@FindBy(xpath = "//lg-filter[@label='Location']//div[contains(@class,'lg-filter__category-items')]//input-field/label")
	private List<WebElement> locationCheckboxLabel;
	@FindBy(xpath = "//div[contains(@class,'day-week-picker-period-active')]/preceding-sibling::div[contains(@class,'day-week-picker-period')]")
	private WebElement immediatePastToCurrentActiveWeek;
	@FindBy(css = ".lg-timesheet-table-improved__grid-row.lg-timesheet-table-improved__worker-row")
	private List<WebElement> timesheetTableRow;
	@FindBy(css = "div.day-week-picker-arrow-left")
	private WebElement previousDayArrow;
	@FindBy(css = "div.lg-timesheet-table__grid-row.lg-timesheet-table__worker-day lg-button")
	private List<WebElement> workersDayRows;
	@FindBy(css = "div.timesheet-details-modal__title-span-sub--home")
	private WebElement TSPopupDetailsLocationName;
	@FindBy(css = "div.timesheet-details-modal__title-span")
	private WebElement TSPopupDetailsWorkerNameAndShiftDay;

	//added by Nishant

	@FindBy(css = "lg-smart-card[heading='Timesheet Due'] content-box")
	private WebElement dueDateSmartCard;

	@FindBy(css = "lg-smart-card[heading='Timesheet Due'] div[ng-if='$ctrl.heading']")
	private WebElement dueDateHeader;

	@FindBy(css = "lg-smart-card[heading='Timesheet Due'] div[ng-if='$ctrl.main']")
	private WebElement dueDateValue;

	@FindBy(css = "lg-smart-card[heading='Due Date'] div[ng-if='$ctrl.note']")
	private WebElement dueDateTimesheetNote;

	@FindBy(css = "div.card-carousel-card.card-carousel-card-primary")
	private WebElement timesheetApprovalSmartCard;

	@FindBy(css = "div.card-carousel-card.analytics-card-color")
	private WebElement totalUnplannedClocksSmartCard;

	@FindBy(css = "div.card-carousel-card.card-carousel-card-card-carousel-card-yellow-top")
	private WebElement SummaryOfUnplannedClocksSmartCard;

	@FindBy(css = "div.analytics-new-table-group")
	private List<WebElement> timesheetTblRow;

	@FindBy(css = "div.card-carousel-card.card-carousel-card-analytics-card-color-yellow div")
	private List<WebElement> totalUnplannedClocksSmartCardValueNTxt;

	@FindBy(css = "div.analytics-card-color-text-1")
	private WebElement totalUnplannedClocksSmartCardValueOnDMView;

	@FindBy(css = "div.analytics-card-color-text-4")
	private WebElement totalTimesheetSmartCardValueOnDMView;

	@FindBy(css = "div[ng-repeat*='smartCardData.Timesheet.UnplannedClocks']")
	private List<WebElement> detailSummaryUnplannedClocksVal;

//	@FindBy(css = "div.analytics-new-table-group-row-open div.analytics-new-table-group-row-action")
//	private List<WebElement> goToSMViewArrow;

	@FindBy(css = "input-field[placeholder='Search']")
	private List<WebElement> searchTxt;

	@FindBy(xpath = "//lg-smart-card[@heading='Timesheet Due']/content-box | //lg-smart-card[@heading='Approval']/content-box")
	private WebElement dueDateOrApprovalSmartCard;

	@FindBy(css = "div.lg-smart-card.ng-scope.lg-smart-card--is-primary")
	private WebElement primarySmartCardTimesheet;

	@FindBy(css = "lg-smart-card[heading='Alerts'] content-box")
	private WebElement alertSmartCardTimesheet;

	@FindBy(css = "a.lg-filter__clear-active")
	private WebElement clearLocationFilter;

	@FindBy(css = ".lg-timesheet-carousel__table-row--cell")
	private List<WebElement> alertsSmartCardValue;

	@FindBy(className = "lg-timesheet-table-improved__grid-column--entries-count")
	private List<WebElement> totalTimesheetsOnSMView;

	@FindBy(xpath = "//div[contains(@class,'analytics-new-table-group-row-open')]//img[@ng-if='isLocation(el)']/following-sibling::span")
	private List<WebElement> listLocations;

	@FindBy(css = "div.analytics-new-table-group-row-open div:nth-child(2)")
	private List<WebElement> unplannedClocksTblView;

	@FindBy(css = "div.analytics-new-table-group-row-open div:nth-child(3)")
	private List<WebElement> totalTimesheetsTblView;

	@FindBy(css ="div.card-carousel-card.card-carousel-card-primary.card-carousel-card-primary-small")
	private WebElement totalViolationHoursSmartCard;

	@FindBy(css ="div.card-carousel-card.card-carousel-card-analytics-card-color-red")
	private WebElement locationWithViolationSmartCard;

	String timeSheetHeaderLabel = "Timesheet";
	String locationFilterSpecificLocations = null;
	List<String> locationName = new ArrayList<>();
	public ConsoleTimeSheetPage(){
		PageFactory.initElements(getDriver(), this);
	}

	@Override
	public void clickOnTimeSheetConsoleMenu() throws Exception {
		if(isElementLoaded(timeSheetConsoleMenuDiv, 30))
			click(timeSheetConsoleMenuDiv);
		else
			SimpleUtils.fail("Timesheet Console Menu not loaded Successfully!", false);
	}

	public void clickOnComplianceConsoleMenu() throws Exception {
		if(isElementLoaded(complianceConsoleMenuDiv))
			click(complianceConsoleMenuDiv);
		else
			SimpleUtils.fail("Compliance Console Menu not loaded Successfully!", false);
	}

	@Override
	public boolean isTimeSheetPageLoaded() throws Exception
	{
		if(isElementLoaded(timeSheetPageHeaderLabel))
			if(timeSheetPageHeaderLabel.getText().toLowerCase().contains(timeSheetHeaderLabel.toLowerCase())) {
				SimpleUtils.pass("Time Sheet Page loaded Successfully!");
				return true;
			}

		return false;
	}


	@Override
	public void openATimeSheetWithClockInAndOut() throws Exception
	{
		boolean isTimesheetSelected = false;
		if(isElementLoaded(timesheetTable))
		{
			if(timeSheetWorkersRows.size() != 0) {
				for(WebElement workerRow: timeSheetWorkersRows)
				{
					String[] workerRowColumnsText =  workerRow.getText().split("\n");
					float regularHours = Float.valueOf(workerRowColumnsText[4]);
					if(regularHours > 0)
					{
						click(workerRow);
						List<WebElement> displayedWorkersDayRows = getTimeSheetDisplayedWorkersDayRows();
						for(WebElement activeRow: displayedWorkersDayRows)
						{
							String[] activeRowColumnText = activeRow.getText().replace("--", "-1").split("\n");
							float activeRowRegularHours = Float.valueOf(activeRowColumnText[2]);
							if(activeRowRegularHours > 0)
							{
								WebElement activeRowPopUpLink = activeRow.findElement(By.cssSelector("lg-button[action-link]"));
								if(isElementLoaded(activeRowPopUpLink))
								{
									click(activeRowPopUpLink);
									SimpleUtils.pass("Timesheet Details edit view popup loaded!");
									isTimesheetSelected = true;
									break;
								}
							}
						}
						break;
					}
				}
				if(! isTimesheetSelected) {
					SimpleUtils.fail("Time Sheet Page: No timesheet with clock-in and clock-out found!", false);
				}
			}
			else {
				SimpleUtils.fail("Time Sheet Page: No Workers row found!", false);
			}
		}
		else {
			SimpleUtils.fail("Time Sheet Page: Workers Table not Loaded successfully!", false);
		}
	}

	@Override
	public List<WebElement> getTimeSheetDisplayedWorkersDayRows()
	{
		List<WebElement> displayedDayRows = new ArrayList<WebElement>();
		if(timeSheetTableWorkersDayRows.size() != 0) {
			for(WebElement workersDayRow : timeSheetTableWorkersDayRows) {
				if(! workersDayRow.getAttribute("class").contains("ng-hide"))
					displayedDayRows.add(workersDayRow);
			}
		}
		return displayedDayRows;
	}


	@Override
	public void clickOnEditTimesheetClock(int index) throws Exception
	{
		if(isElementLoaded(timeSheetDetailModel)) {
			if(timesheetEditBtns.size() > index) {
				click(timesheetEditBtns.get(index));
				SimpleUtils.pass("Timesheet clock edit button clicked.");
			}
			else {
				SimpleUtils.fail("Timesheet Popup No Clock entry found!",true);
			}
		}
		else {
			SimpleUtils.fail("Time Sheet Detail Model/Popup not displayed!", true);
		}
	}

	@Override
	public void clickOnEditTimesheetClock(WebElement webElement) throws Exception
	{
		if(isElementLoaded(timeSheetDetailModel)) {
			if(isElementLoaded(webElement, 10)) {
				click(webElement);
				SimpleUtils.pass("Timesheet clock edit button clicked.");
			}
			else {
				SimpleUtils.fail("Timesheet Popup Edit button not found!",true);
			}
		}
		else {
			SimpleUtils.fail("Time Sheet Detail Model/Popup not displayed!", true);
		}
	}


	@Override
	public void clickOnDeleteClockButton() throws Exception
	{
		if(isElementLoaded(timeSheetDeleteClockBtn)) {
			click(timeSheetDeleteClockBtn);
			SimpleUtils.pass("Clicked on delete Clock button.");
			if(isElementLoaded(confirmDeleteShiftYesBtn)) {
				click(confirmDeleteShiftYesBtn);
				SimpleUtils.pass("Clicked on 'Yes' buttom to Confirm Clock delete action.");
			}
		}
		else {
			SimpleUtils.fail("Delete Shift Clock button not loaded successfully!", true);
		}
	}


	@Override
	public void closeTimeSheetDetailPopUp() throws Exception
	{
		if(isElementLoaded(timeSheetDetailsPopupCloseBtn))
		{
			click(timeSheetDetailsPopupCloseBtn);
//			SimpleUtils.pass("Timesheet Details Edit popup Closed successfully.");
		}
		else
			SimpleUtils.fail("Timesheet Detail Popup Close Button not found!", true);
	}

	public ArrayList<String> hoverOnClockIconAndGetInfo() throws Exception
	{
		ArrayList<String> clocksInfo = new ArrayList<String>();
		if(timeClockAlertIcons.size() != 0)
		{
			for(WebElement timeClockAlertIcon: timeClockAlertIcons)
			{
				Actions builder = new Actions(MyThreadLocal.getDriver());
				builder.moveToElement(timeClockAlertIcon).build().perform();
				if(isElementLoaded(popoverDiv))
					clocksInfo.add(popoverDiv.getText());
			}
		}
		else {
			SimpleUtils.report("No Clock Info Icon found of Timesheet Details popup.");
		}
		return clocksInfo;
	}


	private static final SimpleDateFormat sdf =
			new SimpleDateFormat("yyyy-MM-dd");

	private static final SimpleDateFormat sdfNew =
			new SimpleDateFormat("MMM d, yyyy");

	public String formatDateForTimesheet(String DaysFromTodayInPast ) {
		String finalDate = null;
		try {
			int DaysFromToday = Integer.parseInt(DaysFromTodayInPast);
			String dateToCompare = searchDateForTimesheet(DaysFromToday);
			Date date = sdf.parse(dateToCompare);
			finalDate = sdfNew.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return finalDate;
	}

	@Override
	public void addNewTimeClock(String location, String employee, String workRole, String startTime, String endTime,String breakStartTime, String breakEndTime, String notes, String DaysFromTodayInPast ) throws Exception {
		if(isElementLoaded(addTimeClockBtn,5)) {
			click(addTimeClockBtn);
			if (isElementEnabled(addTimeClockSaveBtn, 10)) {
				// Select Location
				click(addTCLocationField);
				List<WebElement> locationTextButtons = addTCLocationField.findElements(By.cssSelector("input[type=\"text\"]"));
				WebElement searchButton = locationTextButtons.get(1);
				searchButton.sendKeys(location);
				Thread.sleep(2000);
				if (dropdownOptions.size() != 0) {
					click(dropdownOptions.get(0));
				}
				if (!locatioOrDatePickerPopup.getAttribute("class").contains("ng-hide"))
					click(addTCLocationField);

				// Select Date Month & Year
				click(addTCDateField);
				int DaysFromToday = Integer.parseInt(DaysFromTodayInPast);
				String dateSelected = selectDateForTimesheet(DaysFromToday);

				// Select Employee
				boolean isEmployeeFound = false;
				List<WebElement> timeCLockEmployeeTextBox = addTCEmployeeField.findElements(By.cssSelector("input[type=\"text\"]"));
				click(timeCLockEmployeeTextBox.get(0));
				timeCLockEmployeeTextBox.get(0).sendKeys(employee.split(" ")[0]);
				timeCLockEmployeeTextBox.get(0).sendKeys(Keys.TAB);
				Thread.sleep(2000);
				for (WebElement employeeOption : dropdownOptions) {
					if (employeeOption.getText().toLowerCase().contains(employee.toLowerCase())) {
						click(employeeOption);
						isEmployeeFound = true;
						break;
					}
				}
				SimpleUtils.assertOnFail("The employee '" + employee + "' not found while adding a Time Clock.", isEmployeeFound, false);

				// Select Work Role
				Select workRoleDropDown = new Select(addTCWorkRoleDropDown);
				workRoleDropDown.selectByVisibleText(workRole);

				// Shift Start Field
				addTCShiftStartTimeTextField.clear();
				addTCShiftStartTimeTextField.sendKeys(startTime);

				click(addBreakLink);

				// Break Start Field
				addTCBreakStartTimeTextField.clear();
				addTCBreakStartTimeTextField.sendKeys(breakStartTime);

				// Break End Field
				addTCBreakEndTimeTextField.clear();
				addTCBreakEndTimeTextField.sendKeys(breakEndTime);

				// Shift End Field
				addTCShiftEndTimeTextField.clear();
				addTCShiftEndTimeTextField.sendKeys(endTime);

				// Add Notes Field
				addTCAddNotesTextField.clear();
				addTCAddNotesTextField.sendKeys(notes);

				// Save
				click(addTimeClockSaveBtn);

				if(isElementLoaded(successMsg, 5)){
					SimpleUtils.pass("Successfully added timeclock of " +employee+ " for " +dateSelected+ " at location "+ location + " as workrole " + workRole + " with shift start and end time as "+startTime+"-"+endTime);
				}else{
					SimpleUtils.fail("Unable to save clock",false);
				}


//				if (isElementLoaded(addClockPopup, 1))
//					SimpleUtils.fail("Unable to Save Time Clock!", false);
//				else
//					SimpleUtils.pass("Time Clock Saved Successfully!");
			}
		}
	}

	@FindBy (css = "div.day-week-picker-period")
	private List<WebElement> datesInCurrentActiveWeek;

	@FindBy (css = "div.day-week-picker-arrow-left")
	private WebElement leftArrowDayPicker;

	@FindBy(css = "div.day-week-picker-period")
	private WebElement timeSheetWeekPeriod;

	public String getMonthValue(String monthVal){
		switch(monthVal)
		{
			case "Jan" :
				monthVal = "01";
				break;
			case "Feb" :
				monthVal = "02";
				break;
			case "Mar" :
				monthVal = "03";
				break;
			case "Apr" :
				monthVal = "04";
				break;
			case "May" :
				monthVal = "05";
				break;
			case "Jun" :
				monthVal = "06";
				break;
			case "Jul" :
				monthVal = "07";
				break;
			case "Aug" :
				monthVal = "08";
				break;
			case "Sep" :
				monthVal = "09";
				break;
			case "Oct" :
				monthVal = "10";
				break;
			case "Nov" :
				monthVal = "11";
				break;
			case "Dec" :
				monthVal = "12";
				break;
		}
		return monthVal;
	}

	public void navigateToDesiredWeek(String Date) throws Exception{
		String dayOfWeek = null;
		LocalDate timesheetdate = LocalDate.parse(Date);
		String weekStartingDay = getWeekStartingDay();
		String[] arrWeekStartingDay = weekStartingDay.split("\n");
		String monthVal = getMonthValue((arrWeekStartingDay[1].split(" "))[0]);
		LocalDate now = LocalDate.now();
		if((arrWeekStartingDay[1].split(" "))[1].length() == 1){
			dayOfWeek =  "0" + (arrWeekStartingDay[1].split(" "))[1];
		}
		String startingDayOfCurrentWeek = String.valueOf(now.getYear()) + "-" + monthVal + "-" + dayOfWeek;
		LocalDate startingDayOfCurrentWeekDate = LocalDate.parse(startingDayOfCurrentWeek);
		if(startingDayOfCurrentWeekDate.getMonthValue() - timesheetdate.getMonthValue() == 0){
			if(startingDayOfCurrentWeekDate.getDayOfMonth() - timesheetdate.getDayOfMonth() == 0 ||
					startingDayOfCurrentWeekDate.getDayOfMonth() - timesheetdate.getDayOfMonth() < 0){
				System.out.println("Same week verification continue");
			}else{
				clickPreviousDayArrow(startingDayOfCurrentWeekDate.getDayOfMonth() - timesheetdate.getDayOfMonth());
			}
		}else{
			clickPreviousDayArrow(1);
		}

	}

	public void clickOnSelectedDate(String Date){
		for(int i=0;i<datesInCurrentActiveWeek.size();i++){
			String dateToCompare[] = datesInCurrentActiveWeek.get(i).getText().split("\n");
			if(dateToCompare[1].equalsIgnoreCase(Date)){
				datesInCurrentActiveWeek.get(i).click();
				break;
			}
		}
	}

	public void clickPreviousDayArrow(int previousArrowCount) throws Exception{
		if(isElementLoaded(previousDayArrow,5)){
			for(int i=0; i<previousArrowCount;i+=7){
				click(previousDayArrow);
			}
		}
	}

	public String getWeekStartingDay() throws Exception{
		String weekStartingDay = null;
		if(isElementLoaded(timeSheetWeekPeriod,10)){
			weekStartingDay =  timeSheetWeekPeriod.getText();
		}
		return weekStartingDay;
	}

	@Override
	public void valiadteTimeClock(String location, String employee, String workRole,
								  String startTime, String endTime,String breakStartTime, String breakEndTime, String notes, String DaysFromTodayInPast ) throws Exception
	{
		String detailsOfModifiedTimesheer[] = null;
		if(startTime.startsWith("0"))
			startTime = startTime.substring(1);
		if(breakStartTime.startsWith("0"))
			breakStartTime = breakStartTime.substring(1);
		if(breakEndTime.startsWith("0"))
			breakEndTime = breakEndTime.substring(1);
		if(endTime.startsWith("0"))
			endTime = endTime.substring(1);
		boolean isShiftStartMatched = false;
		boolean isBreakStartMatched = false;
		boolean isBreakEndMatched = false;
		boolean isShiftEndMatched = false;
		clickOnTimeSheetConsoleMenu();
//		String timesheetDateToCompare = selectDateForTimesheet(Integer.parseInt(DaysFromTodayInPast));
		String timesheetDateToCompare = searchDateForTimesheet(Integer.parseInt(DaysFromTodayInPast));
		navigateToDesiredWeek(timesheetDateToCompare);
		String dateOnWhichTimesheetAdded[] = formatDateForTimesheet(DaysFromTodayInPast).split(",");
		clickOnSelectedDate(dateOnWhichTimesheetAdded[0]);
		if(seachAndSelectWorkerByName(employee))
		{
			for(WebElement WorkersDayRow : getTimeSheetDisplayedWorkersDayRows())
			{
				if(WorkersDayRow.getText().toLowerCase().contains(dateOnWhichTimesheetAdded[0].toLowerCase()))
				{
					WebElement activeRowPopUpLink = WorkersDayRow.findElement(By.cssSelector("lg-button[action-link]"));
					if(isElementLoaded(activeRowPopUpLink,5))
					{
						click(activeRowPopUpLink);
						waitForSeconds(5);
						for(WebElement clock: getAllAvailableClocksOnClockDetailsPopup())
						{
							List<WebElement> clockInAndOutDetails = clock.findElements(By.cssSelector("[ng-repeat=\"key in ['in', 'out']\"]"));
							for(WebElement clockInOut: clockInAndOutDetails)
							{
								detailsOfModifiedTimesheer = activePopUpDetails.getText().split("\n");
								String elementText = clockInOut.getText().replace("\n", " ").toLowerCase();
								if(elementText.contains("shift start"))
								{
									if(elementText.contains(location.toLowerCase()) && elementText.contains(workRole.toLowerCase()) && elementText.replaceAll(" ","").contains(startTime.toLowerCase()))
									{
										isShiftStartMatched = true;
									}
								}
								else if(elementText.contains("start") && !isBreakStartMatched)
								{
									if(elementText.replaceAll(" ","").contains(breakStartTime.toLowerCase()))
									{
										isBreakStartMatched = true;
									}
								}
								else if(elementText.contains("end") && !isBreakEndMatched)
								{
									if(elementText.replaceAll(" ","").contains(breakEndTime.toLowerCase()))
									{
										isBreakEndMatched = true;
									}
								}
								else if(elementText.contains("shift end"))
								{
									if(elementText.contains(location.toLowerCase()) && elementText.contains(location.toLowerCase())
											&& elementText.replaceAll(" ","").contains(endTime.toLowerCase()))
									{
										isShiftEndMatched = true;
									}
								}
							}
						}
					}
				}
			}
		}

		if(isShiftStartMatched == true && isBreakStartMatched==true && isBreakEndMatched==true && isShiftEndMatched == true )
		{
			SimpleUtils.pass("Successfully Verified timesheet added using Add Timeclock button for " + detailsOfModifiedTimesheer[0]);
		}else{
			SimpleUtils.fail("Verification of timesheet added using Add Timeclock button for " + detailsOfModifiedTimesheer[0] +" is not successful", false);
		}
	}

	public void clickImmediatePastToCurrentActiveWeekInDayPicker() {
		if (isElementEnabled(immediatePastToCurrentActiveWeek, 30)) {
			click(immediatePastToCurrentActiveWeek);
			if(areListElementVisible(timesheetTableRow,15,1)){
				SimpleUtils.pass("Timesheet Grid loaded Successfully");
			}
		} else {
			SimpleUtils.report("No week found");
		}
	}



	public void enterClockDetails(String clockTime){
		String[] timeClockToEnter = clockTime.split(":");
		for(int i=0;i<timeClockToEnter.length;i++) {
			addClockIn.sendKeys(timeClockToEnter[i]);
			addClockIn.sendKeys(Keys.TAB);
		}

	}

	public String convertTo24HourFormat(String Time) throws ParseException {
		DateFormat df = new SimpleDateFormat("hh:mm aa");
		DateFormat outputformat = new SimpleDateFormat("HH:mm");
		Date date = null;
		String output = null;
		try{
			date= df.parse(Time);
			output = outputformat.format(date);
		}catch(ParseException pe){
			pe.printStackTrace();
		}
		return output;
	}

	public Float calcDurationOfShiftBasedOnClockValues(String shiftStart, String shiftEnd) throws Exception{
		String shiftStartTime = convertTo24HourFormat(shiftStart);
		String splitValueForShiftStart[] = shiftStartTime.split(":");
		Float shiftStartInMinutes =((Float.parseFloat(splitValueForShiftStart[0]))*60 + (Float.parseFloat(splitValueForShiftStart[1])));
		String shiftEndTime = convertTo24HourFormat(shiftEnd);
		String splitValueForShiftEnd[] = shiftEndTime.split(":");
		Float shiftEndInMinutes = ((Float.parseFloat(splitValueForShiftEnd[0]))*60 + (Float.parseFloat(splitValueForShiftEnd[1])));
		Float calcDuration = (shiftEndInMinutes - shiftStartInMinutes)/60;
		return calcDuration;

	}

	@FindBy (css = "div.timesheet-details-modal__title-span")
	private WebElement activePopUpDetails;


	@Override
	public void updateTimeClock(String location, String employee, String startTime, String endTime, String notes, String DaysFromTodayInPast) throws Exception {
		clickOnWeekDuration();
		clickImmediatePastToCurrentActiveWeekInDayPicker();
		String timesheetDateToCompare = formatDateForTimesheet(DaysFromTodayInPast);
		if(seachAndSelectWorkerByName(employee))
		{
			for(WebElement WorkersDayRow : getTimeSheetDisplayedWorkersDayRows()) {
				WebElement activeRowPopUpLink = WorkersDayRow.findElement(By.cssSelector("lg-button[action-link]"));
				if (isElementLoaded(activeRowPopUpLink)) {
					click(activeRowPopUpLink);
					if (isElementLoaded(noClockDisplayPanel, 3)) {
						String detailsOfModifiedTimesheer[] = activePopUpDetails.getText().split("\n");
						click(addClock);
						click(addClockIn);
						enterClockDetails(startTime);
						click(saveClockBtn);
						if(isElementLoaded(editBtnAddClockOut, 10)) {
							click(editBtnAddClockOut);
							click(addClockIn);
							enterClockDetails(endTime);
							click(saveClockBtn);
							String shiftStart = shiftStartTimeInPopUp.getText();
							String shiftEnd = shiftEndTimeInPopUp.getText();
							SimpleUtils.pass("Successfully modified timesheet of " + detailsOfModifiedTimesheer[0] + " having workrole " + detailsOfModifiedTimesheer[1] + " for location " +detailsOfModifiedTimesheer[2]);
							Float calcClockedDuration = calcDurationOfShiftBasedOnClockValues(shiftStart,shiftEnd );
							String clockedHour[] = clockedHourValue.getText().split(" ");
							int clockedHourValueRetrieved = Integer.parseInt(clockedHour[1]);
							if(calcClockedDuration  == clockedHourValueRetrieved){
								SimpleUtils.pass("Clocked Hours are correctly calculated, expected clocked hour value is "+ calcClockedDuration+ " and the value getting displayed is " + clockedHourValueRetrieved);
							}else{
								SimpleUtils.fail("Clocked Hours are not calculated correctly, expected clocked hour value is "+ calcClockedDuration+ " and the value getting displayed is " + clockedHourValueRetrieved,false);
							}
							break;
						}
					}else{
						closeTimeSheetDetailPopUp();
					}
				}
			}
		}
	}

	public String createString(String originalString, String stringToBeInserted, int index){
		int lengthOfString = originalString.length();
		if(lengthOfString<7){
			originalString = "0"+originalString;
		}
		String finalString = originalString.substring(0, index + 1)
				+ stringToBeInserted
				+ originalString.substring(index + 1);
		return finalString;
	}



	@FindBy (xpath = "//span[contains(@class,'lg-scheduled-shifts__info ng-binding')]")
	private WebElement scheduleDetails;

	@FindBy (xpath = "//div[contains(@class,'timesheet-details-modal__footer-span')][contains(text(),'Scheduled:')]")
	private WebElement scheduledHourValue;

	@FindBy (xpath = "//div[contains(@class,'timesheet-details-modal__footer-span')][contains(text(),'Difference:')]")
	private WebElement differenceHourValue;

	@FindBy (css = "div.lg-schedule-changed")
	private WebElement scheduleChangeAlertPanel;

	@FindBy (xpath = "//input[@value='EmployerInitiated']")
	private WebElement scheduleChangeManagerInitiated;

	@FindBy (xpath = "//lg-icon-button[@type='confirm']")
	private WebElement saveSelectionScheduleChangeButton;

	@FindBy (css = "lg-button.lg-schedule-changed__edit-button")
	private WebElement editButtonScheduleChange;

	@FindBy (css = "div.lg-timeclock-activities__clockin-approval div.lg-timeclock-activities__clockin-comments-text")
	private List<WebElement> approverList;

	@FindBy (css = "div.timesheet-details-modal__status-icon")
	private WebElement approvedStatusIcon;


	@Override
	public void timesheetAutoApproval(String location, String employee, String startTime, String endTime, String notes) throws Exception {
		clickOnWeekDuration();
		clickImmediatePastToCurrentActiveWeekInDayPicker();
//		String timesheetDateToCompare = formatDateForTimesheet();
		String scheduledStartTime = null;
		String scheduledEndTime = null;
		String detailsOfModifiedTimesheer[] = null;
		String scheduleHourDetails[] = null;
		if(seachAndSelectWorkerByName(employee)) {
			for (WebElement WorkersDayRow : getTimeSheetDisplayedWorkersDayRows()) {
				WebElement activeRowPopUpLink = WorkersDayRow.findElement(By.cssSelector("lg-button[action-link]"));
				if (isElementLoaded(activeRowPopUpLink)) {
					click(activeRowPopUpLink);
					if (isElementLoaded(noClockDisplayPanel, 3)) {
						detailsOfModifiedTimesheer = activePopUpDetails.getText().split("\n");
						scheduleHourDetails = scheduleDetails.getText().replaceAll(" ", "").split("-");
						scheduledStartTime = createString(scheduleHourDetails[0], ":", 4);
						click(addClock);
						click(addClockIn);
						enterClockDetails(scheduledStartTime);
						click(saveClockBtn);
						if (isElementEnabled(editBtnAddClockOut, 10)) {
							scheduledEndTime = createString(scheduleHourDetails[1], ":", 4);
							click(editBtnAddClockOut);
							waitForSeconds(2);
							click(addClockIn);
							enterClockDetails(scheduledEndTime);
							click(saveClockBtn);
						}
						SimpleUtils.pass("Clocks got added successfully");
						if(isElementLoaded(scheduleChangeAlertPanel, 3) && !isElementLoaded(editButtonScheduleChange,2)){
							click(scheduleChangeManagerInitiated);
							click(saveSelectionScheduleChangeButton);
						}
						Float calcscheduledHour = calcDurationOfShiftBasedOnClockValues(createString(scheduleHourDetails[0], " ", 4), createString(scheduleHourDetails[1], " ", 4));
						String shiftStart = shiftStartTimeInPopUp.getText();

						String shiftEnd = shiftEndTimeInPopUp.getText();
						Float calcClockedHour = calcDurationOfShiftBasedOnClockValues(shiftStart,shiftEnd);
						String clockedHour[] = clockedHourValue.getText().split(" ");
						Float clockedHourValueRetrieved = Float.parseFloat(clockedHour[1]);
						String scheduledhour[] = scheduledHourValue.getText().split(" ");
						Float scheduledhourValueRetrieved = Float.parseFloat(scheduledhour[1]);
						String differenceHour[] = differenceHourValue.getText().split(" ");
						Float differencehourValueRetrieved = Float.parseFloat(differenceHour[1]);
						Float calcDifferenceHour = calcClockedHour - calcscheduledHour;
						if(calcscheduledHour == scheduledhourValueRetrieved && calcClockedHour == clockedHourValueRetrieved && calcDifferenceHour == calcDifferenceHour) {
							SimpleUtils.pass("Calculation of Clocked, Scheduled & Difference Hour is working fine");
							SimpleUtils.pass("Scheduled Hour =" + scheduleDetails.getText() + ", duration calculated = " + scheduledhourValueRetrieved);
							SimpleUtils.pass("Clocked Hour =" + shiftStart + "-"+shiftEnd + ", duration calculated = " + clockedHourValueRetrieved);
							SimpleUtils.pass("Difference in clocked & scheduled Hour calculated =" + calcDifferenceHour + ", retrived value = " + differencehourValueRetrieved);
						}
						if (calcDifferenceHour == 0 && isElementLoaded(approvedStatusIcon, 2)){
							displayTimeClockHistory();
							int approvalCountForTimesheet = approverList.size();
							if(approverList.get(approvalCountForTimesheet-1).getText().toLowerCase().contains("legion")){
								SimpleUtils.pass("Timesheet for " + detailsOfModifiedTimesheer[0] + " got auto approved successfully");
							}else{
								SimpleUtils.fail("Auto approval of Timesheet for " + detailsOfModifiedTimesheer[0] + " is not successful", false);
							}
						}
						break;
					}else{
						closeTimeSheetDetailPopUp();
					}

				}
			}
		}
	}



	@Override
	public void clickOnPayPeriodDuration() throws Exception
	{
		String activeButtonClassKeyword = "selected";
		if(isElementLoaded(payPeriodBtn))
		{
			if(! payPeriodBtn.getAttribute("class").toLowerCase().contains(activeButtonClassKeyword))
			{
				click(payPeriodBtn);
				SimpleUtils.pass("Timesheet duration type '"+payPeriodBtn.getText()+"' selected successfully.");

			}
		}
		else
			SimpleUtils.fail("Timesheet: Pay Period Button not loaded!", false);
	}

	@Override
	public void clickOnWeekDuration() throws Exception
	{
		String activeButtonClassKeyword = "selected";
		if(isElementLoaded(weekViewButton))
		{
			if(! weekViewButton.getAttribute("class").toLowerCase().contains(activeButtonClassKeyword))
			{
				click(weekViewButton);
				SimpleUtils.pass("Timesheet duration type '"+weekViewButton.getText()+"' selected successfully.");
			}
		}
		else
			SimpleUtils.fail("Timesheet: WeekView Button not loaded!", false);
	}

	//added by Nishant

	public void clickOnPPWeeklyDuration() throws Exception {
		String activeButtonClassKeyword = "selected";
		if(isElementLoaded(ppweeklyViewButton))
		{
			if(! ppweeklyViewButton.getAttribute("class").toLowerCase().contains(activeButtonClassKeyword))
			{
				click(ppweeklyViewButton);
				SimpleUtils.pass("Timesheet duration type '"+ppweeklyViewButton.getText()+"' selected successfully.");
			}
		}
		else
			SimpleUtils.fail("Timesheet: PPWeeklyView Button not loaded!", false);
	}

	@Override
	public void clickOnDayView() throws Exception {
		String activeButtonClassKeyword = "selected";
		if(isElementLoaded(timeSheetDayViewBtn))
		{
			if(! timeSheetDayViewBtn.getAttribute("class").toLowerCase().contains(activeButtonClassKeyword)) {
				click(timeSheetDayViewBtn);
				SimpleUtils.pass("Timesheet duration type '"+ timeSheetDayViewBtn.getText() +"' selected successfully.");
			}
		}
		else
			SimpleUtils.fail("Timesheet: Pay Period Button not loaded!", false);
	}

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]//div[5]//span")
	private List<WebElement> ScheduleHourGridTable;

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]//div[6]//span")
	private List<WebElement> ClockedHourGridTable;

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]//div[7]//span")
	private List<WebElement> diffHourGridTable;

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]//div[8]//span")
	private List<WebElement> RegHourGridTable;

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]//div[9]//span")
	private List<WebElement> OTHourGridTable;

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]//div[10]//span")
	private List<WebElement> DTHourGridTable;

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]//div[3]")
	private List<WebElement> numberOfTimesheetEnteriesForTM;

	@FindBy (xpath = "//div[contains(@class,\"lg-timesheet-table__worker-day\")]//div[5]//span")
	private List<WebElement> schedHourDayWise;

	@FindBy (xpath = "//div[contains(@class,\"lg-timesheet-table__worker-day\")]//div[6]//span")
	private List<WebElement> clockHourDayWise;

	@FindBy (xpath = "//div[contains(@class,\"lg-timesheet-table__worker-day\")]//div[7]//span")
	private List<WebElement> diffHourDayWise;

	@FindBy (xpath = "//div[contains(@class,\"lg-timesheet-table__worker-day\")]//div[8]//span")
	private List<WebElement> regHourDayWise;

	@FindBy (xpath = "//div[contains(@class,\"lg-timesheet-table__worker-day\")]//div[9]//span")
	private List<WebElement> OTHourDayWise;

	@FindBy (xpath = "//div[contains(@class,\"lg-timesheet-table__worker-day\")]//div[10]//span")
	private List<WebElement> DTHourDayWise;

	@FindBy (css = ".lg-paged-search__pagination div.lg-pagination__pages")
	private WebElement pagination;

	@FindBy (css = "div.lg-paged-search__pagination div.lg-pagination__arrow--right")
	private WebElement footerRightNavigation;

	@FindBy (css = "div.lg-paged-search__pagination div.lg-pagination__arrow--right.lg-pagination__arrow--disabled")
	private WebElement footerRightNavigationDisabled;

	@FindBy (xpath = "//div[contains(@class,'lg-pagination__pages')]//div[contains(@class,'select-wrapper')]//select/option")
	private List<WebElement> paginationListValue;

	@FindBy (xpath = "//div[contains(@class,'lg-smart-card__content')]//tr[2]//td[2]")
	private WebElement regScheduledHour;


	@FindBy (xpath = "//div[contains(@class,'lg-smart-card__content')]//tr[2]//td[3]")
	private WebElement OTScheduledHour;


	@FindBy (xpath = "//div[contains(@class,'lg-smart-card__content')]//td[contains(text(),'Scheduled')]//parent::tr")
	private WebElement smartcardScheduleValues;

	@FindBy (xpath = "//div[contains(@class,'lg-smart-card__content')]//td[contains(text(),'Clocked')]//parent::tr")
	private WebElement smartcardClockedValues;

	@FindBy (xpath = "//div[contains(@class,'lg-smart-card__content')]//td[contains(text(),'Difference')]//parent::tr")
	private WebElement smartcardDifferenceValues;

	@FindBy (xpath = "//div[contains(@class,'lg-timesheet-table__worker-row')]")
	private List<WebElement> workerRow;

	@FindBy (css = "div.lg-timesheet-table__grid-row.lg-timesheet-table__worker-day")
	private List<WebElement> workerDayRow;

	@FindBy (css = "div.lg-timesheet-table__name")
	private List<WebElement> tMNameFromGroupView;



	@Override
	public void timesheetSmartCard() throws Exception {
		float totalSchedHour = 0.0f;
		float totalClockedHour = 0.0f;
		float totalRegHour = 0.0f;
		float totalOTHour = 0.0f;
		float totalDTHour = 0.0f;
		float totalDiffHour=0.0f;
		clickOnPayPeriodDuration();
		clickImmediatePastToCurrentActiveWeekInDayPicker();
		String paginationValue[] = pagination.getText().split("f ");
		for(int j=0; j<Integer.parseInt(paginationValue[1]); j++) {
			for (int i = 0; i < timeSheetWorkersRows.size(); i++) {
				String workerScheduleHour = ScheduleHourGridTable.get(i).getText();
				totalSchedHour = totalSchedHour + Float.parseFloat(workerScheduleHour);
				String workerClockedHour = ClockedHourGridTable.get(i).getText();
				totalClockedHour = totalClockedHour + Float.parseFloat(workerClockedHour);
				String workerRegHour = RegHourGridTable.get(i).getText();
				totalRegHour = totalRegHour + Float.parseFloat(workerRegHour);
				String workerOTHour = OTHourGridTable.get(i).getText();
				totalOTHour = totalOTHour + Float.parseFloat(workerOTHour);
				String workerDTHour = DTHourGridTable.get(i).getText();
				totalDTHour = totalDTHour + Float.parseFloat(workerDTHour);
				String workerDiffHour = diffHourGridTable.get(i).getText();
				if(workerDiffHour.startsWith("(")){
					String diffValue1 = workerDiffHour.replace("(", "");
					String diffValue2 = diffValue1.replace(")", "");
					totalDiffHour = totalDiffHour  + (-Float.parseFloat(diffValue2));
				}else{
					totalDiffHour = totalDiffHour + Float.parseFloat(workerDiffHour);
				}
			}
//			int selectedPageValue = Integer.parseInt(paginationListValue.get(0).getText());
			if(isElementLoaded(footerRightNavigationDisabled, 2)){
				break;
			}else{
				click(footerRightNavigation);
			}
		}
		//Values from smartcard
		String scheduledHourCompRow[] = smartcardScheduleValues.getText().split(" ");
		float totalScheduleHour = Float.parseFloat(scheduledHourCompRow[1])+Float.parseFloat(scheduledHourCompRow[2])+Float.parseFloat(scheduledHourCompRow[3]);
		String clockedHourCompRow[] = smartcardClockedValues.getText().split(" ");
		float totalClockHour = Float.parseFloat(clockedHourCompRow[1])+Float.parseFloat(clockedHourCompRow[2])+Float.parseFloat(clockedHourCompRow[3]);
		float totalRegularClockedHour = Float.parseFloat(clockedHourCompRow[1]);
		float totalOTClockedHour = Float.parseFloat(clockedHourCompRow[2]);
		float totalDTClockedHour = Float.parseFloat(clockedHourCompRow[3]);
		String differenceHourCompRow[] = smartcardDifferenceValues.getText().split(" ");
		float totalDifferenceHour = Float.parseFloat(differenceHourCompRow[1])+Float.parseFloat(differenceHourCompRow[2])+Float.parseFloat(differenceHourCompRow[3]);
		if(totalSchedHour == totalScheduleHour){
			SimpleUtils.pass("Value for total of schedule hour i.e sum of Reg+OT+DT getting displayed in smartcard = "+totalScheduleHour+" is equal to sum of schedule hour for all the timesheets from gridview i.e = " +totalSchedHour);
		}else{
			SimpleUtils.fail("Value for total of schedule hour i.e sum of Reg+OT+DT getting displayed in smartcard = "+totalScheduleHour+" is not equal to sum of schedule hour for all the timesheets from gridview i.e = " +totalSchedHour,true);
		}
		if(totalClockedHour == totalClockHour){
			SimpleUtils.pass("Value for total of clocked hour i.e sum of Reg+OT+DT getting displayed in smartcard = "+totalClockHour+" is equal to sum of clocked hour for all the timesheets from gridview i.e = " +totalClockedHour);
		}else{
			SimpleUtils.fail("Value for total of clocked hour i.e sum of Reg+OT+DT getting displayed in smartcard = "+totalClockHour+" is not equal to sum of clocked hour for all the timesheets from gridview i.e = " +totalClockedHour,true);
		}
		if(totalRegHour == totalRegularClockedHour){
			SimpleUtils.pass("Value for total of clocked Reg hour getting displayed in smartcard = "+totalRegularClockedHour+" is equal to sum of clocked Reg hour for all the timesheets from gridview i.e = " +totalRegHour);
		}else{
			SimpleUtils.fail("Value for total of clocked Reg hour getting displayed in smartcard = "+totalRegularClockedHour+" is not equal to sum of clocked Reg hour for all the timesheets from gridview i.e = " +totalRegHour,true);
		}
		if(totalOTHour == totalOTClockedHour){
			SimpleUtils.pass("Value for total of clocked OT hour getting displayed in smartcard = "+totalOTClockedHour+" is equal to sum of clocked OT hour for all the timesheets from gridview i.e = " +totalOTHour);
		}else{
			SimpleUtils.fail("Value for total of clocked OT hour getting displayed in smartcard = "+totalOTClockedHour+" is not equal to sum of clocked OT hour for all the timesheets from gridview i.e = " +totalOTHour,true);
		}
		if(totalDTHour == totalDTClockedHour){
			SimpleUtils.pass("Value for total of clocked DT hour getting displayed in smartcard = "+totalDTClockedHour+" is equal to sum of clocked DT hour for all the timesheets from gridview i.e = " +totalDTHour);
		}else{
			SimpleUtils.fail("Value for total of clocked DT hour getting displayed in smartcard = "+totalDTClockedHour+" is not equal to sum of clocked DT hour for all the timesheets from gridview i.e = " +totalDTHour,true);
		}
		if(totalDiffHour == totalDifferenceHour){
			SimpleUtils.pass("Value for total of Difference hour i.e sum of Reg+OT+DT getting displayed in smartcard = "+totalDifferenceHour+" is equal to sum of Difference hour for all the timesheets from gridview i.e = " +totalDiffHour);
		}else{
			SimpleUtils.fail("Value for total of Difference hour i.e sum of Reg+OT+DT getting displayed in smartcard = "+totalDifferenceHour+" is not equal to sum of Difference hour for all the timesheets from gridview i.e = " +totalDiffHour,true);
		}
	}

	//Coverting -- to 0
	public String covertDashTo0(String textPicked){
		String updatedTextPicked = textPicked;
		if(textPicked.equalsIgnoreCase("--")){
			updatedTextPicked = "0";
		}
		return updatedTextPicked;
	}

	public String dayDifferenceHour(String workerDayDiffHour, String workerDayScheduleHour){
		if(workerDayDiffHour.equalsIgnoreCase("--")){
			workerDayDiffHour = "(" + workerDayScheduleHour + ")";
		}
		return workerDayDiffHour;
	}

	@Override
	public void verifyTMsRecordInTimesheetTab() throws Exception{
		clickOnPayPeriodDuration();
		clickImmediatePastToCurrentActiveWeekInDayPicker();
		for (int i = 0; i <5; i++) {
			double groupViewTMDiffHour;
			String[] timesheetEnteriesForTM = numberOfTimesheetEnteriesForTM.get(i).getText().split(" ");
			String workerScheduleHour = ScheduleHourGridTable.get(i).getText();
			double groupViewTMSchedHour = Double.parseDouble(workerScheduleHour);
			String workerClockedHour = ClockedHourGridTable.get(i).getText();
			double groupViewTMClockedHour = Double.parseDouble(workerClockedHour);
			String workerRegHour = RegHourGridTable.get(i).getText();
			double groupViewTMRegHour = Double.parseDouble(workerRegHour);
			String workerOTHour = OTHourGridTable.get(i).getText();
			double groupViewTMOTHour = Double.parseDouble(workerOTHour);
			String workerDTHour = DTHourGridTable.get(i).getText();
			double groupViewTMDTHour = Double.parseDouble(workerDTHour);
			String workerDiffHour = diffHourGridTable.get(i).getText();
			if (workerDiffHour.startsWith("(")) {
				String diffValue1 = workerDiffHour.replace("(", "");
				String diffValue2 = diffValue1.replace(")", "");
				groupViewTMDiffHour = (-Double.parseDouble(diffValue2));
			} else {
				groupViewTMDiffHour = Double.parseDouble(workerDiffHour);
			}
			workerRow.get(i).click();
			Double totalSchedHourForTM = 0.0;
			Double totalClockedHourForTM = 0.0;
			Double totalRegHourForTM = 0.0;
			Double totalOTHourForTM = 0.0;
			Double totalDTHourForTM = 0.0;
			Double totalDiffHourForTM = 0.0;
			int noOfTimesheetEnteriesForTM = workerDayRow.size();
			for (int j = 0; j <  workerDayRow.size(); j++) {
				String workerDayScheduleHour = schedHourDayWise.get(j).getText();
				String updatedWorkerDayScheduleHour = covertDashTo0(workerDayScheduleHour);
				totalSchedHourForTM = totalSchedHourForTM + Double.parseDouble(updatedWorkerDayScheduleHour);
				String workerDayClockedHour = clockHourDayWise.get(j).getText();
				String updatedworkerDayClockedHour = covertDashTo0(workerDayClockedHour);
				totalClockedHourForTM = totalClockedHourForTM + Double.parseDouble(updatedworkerDayClockedHour);
				String workerDayRegHour = regHourDayWise.get(j).getText();
				String updatedworkerDayRegHour = covertDashTo0(workerDayRegHour);
				totalRegHourForTM = totalRegHourForTM + Double.parseDouble(updatedworkerDayRegHour);
				String workerDayOTHour = OTHourDayWise.get(j).getText();
				String updatedworkerDayOTHour = covertDashTo0(workerDayOTHour);
				totalOTHourForTM = totalOTHourForTM + Double.parseDouble(updatedworkerDayOTHour);
				String workerDayDTHour = DTHourDayWise.get(j).getText();
				String updatedworkerDayDTHour = covertDashTo0(workerDayDTHour);
				totalDTHourForTM = totalDTHourForTM + Double.parseDouble(updatedworkerDayDTHour);
				String workerDayDiffHour = diffHourDayWise.get(j).getText();
				String updatedworkerDayDiffHour = dayDifferenceHour(workerDayDiffHour, workerDayScheduleHour);
				if (updatedworkerDayDiffHour.startsWith("(")) {
					String diffValue1 = updatedworkerDayDiffHour.replace("(", "");
					String diffValue2 = diffValue1.replace(")", "");
					totalDiffHourForTM = totalDiffHourForTM + (-Double.parseDouble(diffValue2));
				} else {
					totalDiffHourForTM = totalDiffHourForTM + Double.parseDouble(updatedworkerDayDiffHour);
				}
			}
			workerRow.get(i).click();
			//Comparision of displayed value and calculated value
			String[] TMNAme = tMNameFromGroupView.get(i).getText().split("\n");
			SimpleUtils.report("=====================================Below are the timesheet details for " + TMNAme[0] + "=======================================");
			if(Integer.parseInt(timesheetEnteriesForTM[0]) == noOfTimesheetEnteriesForTM){
				SimpleUtils.pass("Number of timesheet enteries showing up for the TM in grouped view is = " + numberOfTimesheetEnteriesForTM.get(i).getText() + " is equal to number of timesheet records = " + noOfTimesheetEnteriesForTM);
			} else {
				SimpleUtils.fail("Number of timesheet enteries showing up for the TM in grouped view is = " + numberOfTimesheetEnteriesForTM.get(i).getText() + " is not equal to number of timesheet records = " + noOfTimesheetEnteriesForTM, true);
			}
			if (groupViewTMSchedHour == totalSchedHourForTM) {
				SimpleUtils.pass("Schedule hour getting displayed at group level is  = " + groupViewTMSchedHour + " is equal to sum of schedule hour for all the timesheets records =" + totalSchedHourForTM);
			} else {
				SimpleUtils.fail("Schedule hour getting displayed at group level is  = " + groupViewTMSchedHour + " is not equal to sum of schedule hour for all the timesheets records =" + totalSchedHourForTM, true);
			}
			if (groupViewTMClockedHour == totalClockedHourForTM) {
				SimpleUtils.pass("Clocked hour getting displayed at group level is  = " + groupViewTMClockedHour + " is equal to sum of clocked hour for all the timesheets records =" + totalClockedHourForTM);
			} else {
				SimpleUtils.fail("Clocked hour getting displayed at group level is  = " + groupViewTMClockedHour + " is not equal to sum of clocked hour for all the timesheets records =" + totalClockedHourForTM, true);
			}
			if (groupViewTMRegHour == totalRegHourForTM) {
				SimpleUtils.pass("Regular hour getting displayed at group level is  = " + groupViewTMRegHour + " is equal to sum of Regular hour for all the timesheets records =" + totalRegHourForTM);
			} else {
				SimpleUtils.fail("Regular hour getting displayed at group level is  = " + groupViewTMRegHour + " is not equal to sum of Regular hour for all the timesheets records =" + totalRegHourForTM, true);
			}
			if (groupViewTMOTHour == totalOTHourForTM) {
				SimpleUtils.pass("OT hour getting displayed at group level is  = " + groupViewTMOTHour + " is equal to sum of OT hour for all the timesheets records =" + totalOTHourForTM);
			} else {
				SimpleUtils.fail("OT hour getting displayed at group level is  = " + groupViewTMOTHour + " is not equal to sum of OT hour for all the timesheets records =" + totalOTHourForTM, true);
			}
			if (groupViewTMDTHour == totalDTHourForTM) {
				SimpleUtils.pass("DT hour getting displayed at group level is  = " + groupViewTMDTHour + " is equal to sum of DT hour for all the timesheets records =" + totalDTHourForTM);
			} else {
				SimpleUtils.fail("DT hour getting displayed at group level is  = " + groupViewTMDTHour + " is not equal to sum of DT hour for all the timesheets records =" + totalDTHourForTM, true);
			}
			if (groupViewTMDiffHour == totalDiffHourForTM) {
				SimpleUtils.pass("Difference hour getting displayed at group level is  = " + groupViewTMDiffHour + " is equal to sum of Difference hour for all the timesheets records =" + totalDiffHourForTM);
			} else {
				SimpleUtils.fail("Difference hour getting displayed at group level is  = " + groupViewTMDiffHour + " is equal to sum of Difference hour for all the timesheets records =" + totalDiffHourForTM, true);
			}
		}

	}



	public ArrayList<WebElement> getAllAvailableClocksOnClockDetailsPopup()
	{
		ArrayList<WebElement> availableClocks = new ArrayList<WebElement>();
		for(WebElement clockDetails : savedClockes)
		{
			if(clockDetails.getText().length() != 0 && clockDetails.getText().trim() != "")
				availableClocks.add(clockDetails);
		}
		return availableClocks;
	}



	@Override
	public HashMap<String, Float> getTimeClockHoursByDate(String DaysFromTodayInPast, String timeClockEmployee) throws Exception {
		HashMap<String, Float> allHours = new HashMap<String, Float>();
		clickOnTimeSheetConsoleMenu();
		clickOnPayPeriodDuration();

		int DaysFromToday = Integer.parseInt(DaysFromTodayInPast);
		String selectedDate = selectDateForTimesheet(DaysFromToday);
		if(seachAndSelectWorkerByName(timeClockEmployee))
		{
			for(WebElement WorkersDayRow : getTimeSheetDisplayedWorkersDayRows()) {
				if(WorkersDayRow.getText().toLowerCase().contains(selectedDate.toLowerCase().split(",")[0])) {
					allHours = getTimesheetWorkerHoursByDay(WorkersDayRow);
				}
			}
		}
		return allHours;
	}



	@Override
	public String getActiveDayWeekOrPayPeriod() throws Exception {
		String activePeriodText = "";
		if(isElementLoaded(timeSheetActivePeriod))
		{
			activePeriodText = timeSheetActivePeriod.getText().replace("\n", " ");
		}
		else
			SimpleUtils.fail("Timesheet duration list not loaded Successfully.", false);

		return activePeriodText;
	}


	@Override
	public void navigateDayWeekOrPayPeriodToPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount)
	{
		List<WebElement> timeSheetDurationNavigatorLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
		for(int i = 0; i < weekCount; i++)
		{
			int displayedWeekCount = timeSheetDurationNavigatorLabels.size();
			for(WebElement ScheduleCalendarDayLabel: timeSheetDurationNavigatorLabels)
			{
				if(ScheduleCalendarDayLabel.getAttribute("class").toString().contains("day-week-picker-period-active"))
				{
					if(nextWeekViewOrPreviousWeekView.toLowerCase().contains("next") || nextWeekViewOrPreviousWeekView.toLowerCase().contains("future"))
					{
						try {
							int activeWeekIndex = timeSheetDurationNavigatorLabels.indexOf(ScheduleCalendarDayLabel);
							if(activeWeekIndex < (displayedWeekCount - 1))
							{
								click(timeSheetDurationNavigatorLabels.get(activeWeekIndex + 1));
							}
							else {
								click(timeSheetNavigationNextDurationArrow);
								click(timeSheetDurationNavigatorLabels.get(0));
							}
						}
						catch (Exception e) {
							SimpleUtils.report("Schedule page Calender Next Week Arrows Not Loaded/Clickable after '"+ScheduleCalendarDayLabel.getText().replace("\n", "")+ "'");
						}
					}
					else
					{
						try {
							int activeWeekIndex = timeSheetDurationNavigatorLabels.indexOf(ScheduleCalendarDayLabel);
							if(activeWeekIndex > 0)
							{
								click(timeSheetDurationNavigatorLabels.get(activeWeekIndex - 1));
							}
							else {
								click(timeSheetNavigationPreviousDurationArrow);
								click(timeSheetDurationNavigatorLabels.get(displayedWeekCount - 1));
							}
						} catch (Exception e) {
							SimpleUtils.fail("Schedule page Calender Previous Week Arrows Not Loaded/Clickable after '"+ScheduleCalendarDayLabel.getText().replace("\n", "")+ "'", true);
						}
					}
					break;
				}
			}
		}
	}


	@Override
	public void openFirstPendingTimeSheet() throws Exception
	{
		if(isElementLoaded(timesheetTable))
		{
			if(timeSheetWorkersRows.size() != 0) {
				for(WebElement workerRow: timeSheetWorkersRows)
				{
					if(workerRow.findElements(By.cssSelector("lg-eg-status[type=\"Pending\"]")).size() != 0)
					{
						click(workerRow);
						List<WebElement> timeSheetDisplayedWorkersDayRows = getTimeSheetDisplayedWorkersDayRows();
						for(WebElement activeRow: timeSheetDisplayedWorkersDayRows)
						{
							if(activeRow.findElements(By.cssSelector("lg-eg-status[type=\"Pending\"]")).size() != 0)
							{
								WebElement activeRowPopUpLink = activeRow.findElement(By.cssSelector("lg-button[action-link]"));
								if(isElementLoaded(activeRowPopUpLink))
								{
									click(activeRowPopUpLink);
									break;
								}
							}
						}
						break;
					}

				}
			}
		}

	}

	@Override
	public boolean isTimeSheetPopupApproveButtonActive() throws Exception
	{
		if(isElementLoaded(timeSheetPopUpApproveBtn))
		{
			List<WebElement> approveBtnList = timeSheetPopUpApproveBtn.findElements(By.cssSelector("button[type=\"button\"]"));
			if( approveBtnList.size() != 0)
			{
				if(approveBtnList.get(0).isEnabled())
				{
					return true;
				}
			}
		}

		return false;
	}


	@Override
	public boolean seachAndSelectWorkerByName(String workerName) throws Exception
	{
		if(isElementLoaded(timeSheetWorkerSearchBox, 10))
		{

			timeSheetWorkerSearchBox.click();
			timeSheetWorkerSearchBox.clear();
			timeSheetWorkerSearchBox.sendKeys(workerName.split(" ")[0]);
			timeSheetWorkerSearchBox.sendKeys(Keys.TAB);
			Thread.sleep(2000);
			if(timeSheetWorkersRows.size() != 0) {
				for(WebElement workerRow: timeSheetWorkersRows) {
					if(workerRow.getText().toLowerCase().contains(workerName.toLowerCase())) {
						click(workerRow);
						SimpleUtils.pass("Timesheet worker: '"+ workerName +"' selected.");
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public HashMap<String, Float> getTimesheetWorkerHoursByDay(WebElement WorkersDayRow)
	{
		HashMap<String, Float> workerAllDayRowHours = new HashMap<String, Float>();
		String[] workerRowColumnsText =  WorkersDayRow.getText().replace("--", "-1").split("\n");
		if(workerRowColumnsText.length > 10) {
			float regHours = Float.valueOf(workerRowColumnsText[2]);
			float oTHours = Float.valueOf(workerRowColumnsText[3]);
			float dTHours = Float.valueOf(workerRowColumnsText[4]);
			float holHours = Float.valueOf(workerRowColumnsText[5]);
			float totalHours = Float.valueOf(workerRowColumnsText[6]);
			float schedHours = Float.valueOf(workerRowColumnsText[7]);
			float diffHours = Float.valueOf(workerRowColumnsText[8]);
			float tipsHours = Float.valueOf(workerRowColumnsText[9]);
			float mealHours = Float.valueOf(workerRowColumnsText[10]);
			workerAllDayRowHours.put("regHours", regHours);
			workerAllDayRowHours.put("oTHours", oTHours);
			workerAllDayRowHours.put("dTHours", dTHours);
			workerAllDayRowHours.put("holHours", holHours);
			workerAllDayRowHours.put("totalHours", totalHours);
			workerAllDayRowHours.put("schedHours", schedHours);
			workerAllDayRowHours.put("diffHours", diffHours);
			workerAllDayRowHours.put("tipsHours", tipsHours);
			workerAllDayRowHours.put("mealHours", mealHours);
		}
		else {
			SimpleUtils.fail("Unable to fetch time clock hours.", true);
		}
		return workerAllDayRowHours;
	}


	@Override
	public String getWorkerTimeSheetAlert(WebElement workersDayRow) throws Exception {
		String timeSheetAlert = "";
		List<WebElement> workerDayAlertBtn = workersDayRow.findElements(By.cssSelector("lg-timesheet-alert[ng-if=\"day.alerts.length\"]"));
		if(workerDayAlertBtn.size() != 0)
		{
			mouseHover(workerDayAlertBtn.get(0));
			if(isElementLoaded(popoverDiv))
				timeSheetAlert = popoverDiv.getText().replace("\n", " ");
		}
		else
			SimpleUtils.report("Timesheet alert not available for:'"+workersDayRow.getText().split("\n")[0]+"'.");
		return timeSheetAlert;
	}



	@Override
	public void openWorkerDayTimeSheetByElement(WebElement workersDayRow) throws Exception {
		WebElement activeRowPopUpLink = workersDayRow.findElement(By.cssSelector("lg-button[action-link]"));
		if(isElementLoaded(activeRowPopUpLink)) {
			click(activeRowPopUpLink);
			SimpleUtils.pass("Timesheet Details Edit popup Opened successfully.");
		}
		else {
			SimpleUtils.fail("Active Row PopUp Link not found.", false);
		}
	}

	@Override
	public boolean isTimesheetPopupModelContainsKeyword(String keyword) throws Exception
	{
		if(isElementLoaded(TSPopupDetailsModel)) {
			if(TSPopupDetailsModel.getText().toLowerCase().contains(keyword.toLowerCase()))
				return true;
		}
		else
			SimpleUtils.fail("Timesheet details popup not loaded successfully.", false);

		return false;
	}

	@Override
	public boolean isWorkerDayRowStatusPending(WebElement workerDayRow) throws Exception
	{
		if(isElementLoaded(workerDayRow))
		{
			List<WebElement> workerRowPendingStatus = workerDayRow.findElements(By.cssSelector("lg-eg-status[type=\"Pending\"]"));
			if(workerRowPendingStatus.size() > 0)
				return true;
		}
		return false;
	}

	@Override
	public void clickOnApproveButton() throws Exception
	{
		if(isElementLoaded(timeSheetPopUpApproveBtn))
		{
			click(timeSheetPopUpApproveBtn);
			SimpleUtils.pass("Timesheet details popup 'Approve' button clicked successfully.");
		}
		else {
			SimpleUtils.fail("Timesheet details popup 'Approve' button not loaded.", false);
		}
	}


	@Override
	public boolean isTimeSheetApproved() throws Exception
	{
		if(isElementLoaded(timeSheetDetailPopupApproveStatus, 10))
		{
			SimpleUtils.pass("Timesheet approved for duration: '"+getActiveDayWeekOrPayPeriod()+"'.");
			return true;
		}

		return false;
	}


	@Override
	public String getTimeClockHistoryText() throws Exception
	{
		String timeClockHistoryText = "";
		if(isElementLoaded(timeClockHistoryDetailsSection))
			if(timeClockHistoryDetailsSection.isDisplayed())
				timeClockHistoryText = timeClockHistoryDetailsSection.getText();

		return timeClockHistoryText;
	}


	@Override
	public void displayTimeClockHistory() throws Exception
	{
		if(isElementLoaded(expandHistoryBtn, 10))
		{
			click(expandHistoryBtn);
		}
		else {
			SimpleUtils.fail("Timesheet Details popup Expand History Button not loaded.", false);
		}
	}


	@Override
	public List<WebElement> getAllTimeSheetEditBtnElements() throws Exception
	{
		return timesheetEditBtns;
	}


	@Override
	public boolean isTimeSheetWorkerRowContainsCheckbox(WebElement workerRow)
	{
		List<WebElement> workerCheckboxs = workerRow.findElements(By.cssSelector("input-field[value=\"worker.selected\"]"));
		if(workerCheckboxs.size() > 0)
			return true;

		return false;
	}

	@Override
	public List<WebElement> getTimeSheetWorkersRow() throws Exception
	{
		if(isElementLoaded(timesheetTable))
		{
			return timeSheetWorkersRows;
		}
		else {
			SimpleUtils.fail("Time Sheet Page: Workers Table not Loaded successfully!", false);
			return new ArrayList<WebElement>();
		}
	}

	@Override
	public String getWorkerNameByWorkerRowElement(WebElement workerRow) throws Exception {
		String workerName = "";
		WebElement workerNameElement = workerRow.findElement(By.cssSelector("div.lg-timesheet-table__name"));
		if(isElementLoaded(workerNameElement, 10))
			workerName = workerNameElement.getText();

		return workerName;
	}

	@Override
	public HashMap<String, Float> getWorkerAllHours(WebElement workerRow) {
		HashMap<String, Float> workerTotalTimeClockHours = new HashMap<String, Float>();
		float SchedHours = 0;
		float ClockHours = 0;
		float DiffHours = 0;
		float RegHours = 0;
		float OTHours = 0;
		float DTHours = 0;
		float PTOHours = 0;
		float ExtraPay = 0;
		int workerRowColumnRowLength = 12;
		String[] workerRowColumnText = workerRow.getText().split("\n");
		try
		{
			SchedHours = Float.valueOf(workerRowColumnText[workerRowColumnText.length-8]);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker Schedule Hours not a valid number, found: '"+ workerRowColumnText[4] +".");
		}

		try
		{
			ClockHours = Float.valueOf(workerRowColumnText[workerRowColumnText.length-7]);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker Clock Hours not a valid number, found: '"+ workerRowColumnText[5] +".");
		}

		try
		{
			String DiffHoursString = workerRowColumnText[workerRowColumnText.length-6];
			if(DiffHoursString.contains("(") || DiffHoursString.contains(")")){
				DiffHoursString = "-"+DiffHoursString.substring(1, DiffHoursString.length()-1);
			}
			DiffHours = Float.valueOf(DiffHoursString);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker Difference Hours not a valid number, found: '"+ workerRowColumnText[6] +".");
		}
		try
		{
			RegHours = Float.valueOf(workerRowColumnText[workerRowColumnText.length-5]);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker Total Regular Hours not a valid number, found: '"+ workerRowColumnText[7] +".");
		}

		try
		{
			OTHours = Float.valueOf(workerRowColumnText[workerRowColumnText.length-4]);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker Total Overtime Hours not a valid number, found: '"+ workerRowColumnText[8] +".");
		}

		try
		{
			DTHours = Float.valueOf(workerRowColumnText[workerRowColumnText.length-3]);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker Double Time Hours not a valid number, found: '"+ workerRowColumnText[9] +".");
		}

		try
		{
			PTOHours = Float.valueOf(workerRowColumnText[workerRowColumnText.length-2]);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker PTO Hours not a valid number, found: '"+ workerRowColumnText[10] +".");
		}

		try
		{
			ExtraPay = Float.valueOf(workerRowColumnText[workerRowColumnText.length-1]);
		}
		catch (NumberFormatException e)
		{
			SimpleUtils.report("TimeSheet worker Extra Pay Hours not a valid number, found: '"+ workerRowColumnText[11] +".");
		}
//		if(workerRowColumnText.length == workerRowColumnRowLength)
//		{
//
//		}

		workerTotalTimeClockHours.put("SchedHours", SchedHours);
		workerTotalTimeClockHours.put("ClockHours", ClockHours);
		workerTotalTimeClockHours.put("DiffHours", DiffHours);
		workerTotalTimeClockHours.put("RegHours", RegHours);
		workerTotalTimeClockHours.put("OTHours", OTHours);
		workerTotalTimeClockHours.put("DTHours", DTHours);
		workerTotalTimeClockHours.put("PTOHours", PTOHours);
		workerTotalTimeClockHours.put("ExtraPay", ExtraPay);

		return workerTotalTimeClockHours;
	}


	@Override
	public HashMap<String, Float> getWorkerTotalHours(WebElement workerRow) {
		HashMap<String, Float> workerTotalTimeClockHours = new HashMap<String, Float>();
		float RegHours = 0;
		float OTHours = 0;
		float DTHours = 0;
		float HolHours = 0;
		float TotalHours = 0;
		float SchedHours = 0;
		float DiffHours = 0;
		float TipsHours = 0;
		float MealHours = 0;
		int workerRowColumnRowLength = 13;
		String[] workerRowColumnText = workerRow.getText().split("\n");
		if(workerRowColumnText.length == workerRowColumnRowLength)
		{
			try
			{
				RegHours = Float.valueOf(workerRowColumnText[4]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Total Regular Hours not a valid number, found: '"+ workerRowColumnText[4] +".");
			}

			try
			{
				OTHours = Float.valueOf(workerRowColumnText[5]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Total Overtime Hours not a valid number, found: '"+ workerRowColumnText[5] +".");
			}

			try
			{
				DTHours = Float.valueOf(workerRowColumnText[6]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Double Time Hours not a valid number, found: '"+ workerRowColumnText[6] +".");
			}

			try
			{
				HolHours = Float.valueOf(workerRowColumnText[7]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Holiday Hours not a valid number, found: '"+ workerRowColumnText[7] +".");
			}

			try
			{
				TotalHours = Float.valueOf(workerRowColumnText[8]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Total Hours not a valid number, found: '"+ workerRowColumnText[8] +".");
			}

			try
			{
				SchedHours = Float.valueOf(workerRowColumnText[9]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Schedule Hours not a valid number, found: '"+ workerRowColumnText[9] +".");
			}

			try
			{
				DiffHours = Float.valueOf(workerRowColumnText[10]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Difference Hours not a valid number, found: '"+ workerRowColumnText[10] +".");
			}

			try
			{
				TipsHours = Float.valueOf(workerRowColumnText[11]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Tips Hours not a valid number, found: '"+ workerRowColumnText[7] +".");
			}

			try
			{
				MealHours = Float.valueOf(workerRowColumnText[12]);
			}
			catch (NumberFormatException e)
			{
				SimpleUtils.report("TimeSheet worker Meal Hours not a valid number, found: '"+ workerRowColumnText[12] +".");
			}

		}

		workerTotalTimeClockHours.put("RegHours", RegHours);
		workerTotalTimeClockHours.put("OTHours", OTHours);
		workerTotalTimeClockHours.put("DTHours", DTHours);
		workerTotalTimeClockHours.put("HolHours", HolHours);
		workerTotalTimeClockHours.put("TotalHours", TotalHours);
		workerTotalTimeClockHours.put("SchedHours", SchedHours);
		workerTotalTimeClockHours.put("DiffHours", DiffHours);
		workerTotalTimeClockHours.put("TipsHours", TipsHours);
		workerTotalTimeClockHours.put("MealHours", MealHours);

		return workerTotalTimeClockHours;
	}


	@Override
	public void vadidateWorkerTimesheetLocationsForAllTimeClocks(WebElement workersDayRow) throws Exception {
		String[] workerdayRowText = workersDayRow.getText().split("\n");
		String timeClockListViewLocation = workerdayRowText[1];
		openWorkerDayTimeSheetByElement(workersDayRow);
		String noScheduledShiftText = "No shifts scheduled";
		boolean isLocationCorrect = false;
		boolean isNoScheduledShiftFound = isTimesheetPopupModelContainsKeyword(noScheduledShiftText);
		if(! isNoScheduledShiftFound)
		{
			if(shiftLocationAndHours.size() != 0)
			{
				for(WebElement location : shiftLocationAndHours)
				{
					if(location.getText().toLowerCase().contains(timeClockListViewLocation.toLowerCase()))
						isLocationCorrect = true;
				}

				if(isLocationCorrect)
				{
					SimpleUtils.pass("Time Clock location matched with scheduled shift location for the day:'"+ workerdayRowText[0] +"'");
				}
				else
				{
					SimpleUtils.fail("Time Clock location not matched with scheduled shift location or Shift not scheduled for the day:'"+
							workerdayRowText[0] +"'" , true);
				}
			}
			else {
				SimpleUtils.report("Shift not contains any location, duration: '"+ workerdayRowText[0] +"'.");
			}
		}
		else {
			SimpleUtils.report("Shift is not Scheduled for the day: '"+ workerdayRowText[0] +"'.");
		}

		closeTimeSheetDetailPopUp();
	}


	@Override
	public void addBreakToOpenedTimeClock(String breakStartTime, String breakEndTime) {
		if(timeClockAddBreakButtons.size() != 0)
		{
			String[] breakStartTimeArray = breakStartTime.split(":");
			String[] breakEndTimeArray = breakEndTime.split(":");
			for(WebElement addBreakBtn : timeClockAddBreakButtons)
			{
				if(addBreakBtn.isDisplayed() && addBreakBtn.isEnabled())
				{
					click(addBreakBtn);
					if(timeInputFields.size() == 2)
					{
						// adding Break Start Time
						timeInputFields.get(0).click();
						timeInputFields.get(0).sendKeys(breakStartTimeArray[0]);
						timeInputFields.get(0).sendKeys(breakStartTimeArray[1]);

						// adding Break End Time
						timeInputFields.get(0).sendKeys(Keys.TAB);;
						timeInputFields.get(1).sendKeys(breakEndTimeArray[0]);
						timeInputFields.get(1).sendKeys(breakEndTimeArray[1]);
						click(timeClockConfirmBtn);
						SimpleUtils.pass("Time clock break added successfully with duration: '"+ breakStartTime +" - "+ breakEndTime +"'.");
					}

					break;
				}
				else
					SimpleUtils.fail("'Add Break not displayed on time clock detail popup.", true);
			}
		}
		else
			SimpleUtils.fail("'Add Break not found on time clock detail popup.", true);

	}

	@Override
	public void closeTimeClockHistoryView() throws Exception {

		if(isElementLoaded(timeClockActivitiesCollapseBtn, 10))
			click(timeClockActivitiesCollapseBtn);
	}

	@Override
	public void addTimeClockCheckInOnDetailPopupWithDefaultValue() throws Exception {
		String timeClockCheckIn = "09:00";
		if(isElementLoaded(addClockBtnOnDetailPopup, 10))
		{
			click(addClockBtnOnDetailPopup);
			timeInputFields.get(0).click();
			timeInputFields.get(0).sendKeys(timeClockCheckIn.split(":")[0]);
			timeInputFields.get(0).sendKeys(timeClockCheckIn.split(":")[1]);
			click(timeClockConfirmBtn);
			SimpleUtils.pass("Time clock default Check in added successfully with time: '"+ timeClockCheckIn +"'.");
		}

	}


	@Override
	public boolean isTimeClockApproved(WebElement workerTimeClock) throws Exception {
		WebElement timeClockStatus = workerTimeClock.findElement(By.cssSelector("lg-eg-status[type]"));
		if(isElementLoaded(timeClockStatus))
			if(timeClockStatus.getAttribute("type").contains("Approved"))
				return true;
		return false;
	}

	@FindBy(css = "div[ng-repeat=\"key in ['in', 'out']\"]")
	private List<WebElement> timeClockEntries;

	@Override
	public void removeTimeClockEntryByLabel(String label) throws Exception
	{
		for(WebElement clockEntry : timeClockEntries)
		{
			if(clockEntry.getText().toLowerCase().contains(label.toLowerCase()))
			{
				WebElement editBtn = clockEntry.findElement(By.cssSelector("lg-button[label=\"Edit\"]"));
				click(editBtn);
				clickOnDeleteClockButton();
			}
		}
	}


	@FindBy(css = "div.lg-timesheet-table")
	private WebElement timeSheetDetailsTable;

	@Override
	public boolean isTimeSheetDetailsTableLoaded() throws Exception
	{
		String activeDay = getActiveDayWeekOrPayPeriod();
		if(isElementLoaded(timeSheetDetailsTable, 20) && timeSheetDetailsTable.getText().trim().length() > 0)
		{
			SimpleUtils.pass("Timesheet loaded successfully for duration Type: '"+ getTimeSheetActiveDurationType() +"' for pay period duration " + activeDay.substring(10));
			return true;
		}
		return false;
	}


	@FindBy(css = "div[ng-repeat=\"button in $ctrl.buttons\"]")
	private List<WebElement> timeSheetDurationBtns;

	@Override
	public void clickOnWeekView()
	{
		String weekViewLabel = "week";
		if(timeSheetDurationBtns.size() > 0)
		{
			for(WebElement timeSheetDuration : timeSheetDurationBtns)
			{
				if(timeSheetDuration.getText().toLowerCase().contains(weekViewLabel.toLowerCase()))
				{
					click(timeSheetDuration);
					SimpleUtils.pass("Timesheet duration type '"+ timeSheetDuration.getText() +"' selected successfully.");
				}
			}
		}
		else
			SimpleUtils.fail("TimeSheet Duration Buttons not loaded.", false);
	}

	@Override
	public String getTimeSheetActiveDurationType()
	{
		String timeSheetActiveDurationType = "";
		if(timeSheetDurationBtns.size() > 0)
		{
			for(WebElement timeSheetDuration : timeSheetDurationBtns)
			{
				if(timeSheetDuration.getAttribute("class").contains("selected"))
					timeSheetActiveDurationType = timeSheetDuration.getText();
			}
		}
		else
			SimpleUtils.fail("TimeSheet Duration Buttons not loaded.", false);

		return timeSheetActiveDurationType;
	}



	@Override
	public void exportTimesheet() throws Exception {
		if(isElementLoaded(exportTimesheetBtn)) {
			click(exportTimesheetBtn);
			if(isElementLoaded(exportAnywayTimesheetBtn))
				click(exportAnywayTimesheetBtn);
		}
		else
			SimpleUtils.fail("Timesheet Page: Timesheet 'Export' button not loaded.", false);
	}



	@Override
	public HashMap<String, Float> getTotalTimeSheetCarouselCardsHours() throws Exception {
		HashMap<String, Float> timeSheetCarouselCardsHours = new HashMap<String, Float>();
		if(isElementLoaded(timesheetCarouselCardHoursDiv)) {
			String[] carouselCardText = timesheetCarouselCardHoursDiv.getAttribute("innerText").split("\n");
			for(String carouselCardRow : carouselCardText) {
				if(carouselCardRow.toLowerCase().contains("clocked")) {
					String[] carouselCardRowCols = carouselCardRow.split("\t");
					if(carouselCardRowCols.length > 3)
					{
						timeSheetCarouselCardsHours.put("regularHours", Float.valueOf(carouselCardRowCols[1]));
						timeSheetCarouselCardsHours.put("overtimeHours", Float.valueOf(carouselCardRowCols[2]));
						timeSheetCarouselCardsHours.put("doubleTimeHours", Float.valueOf(carouselCardRowCols[3]));
					}
				}
			}
		}
		else
			SimpleUtils.fail("TimeSheet Page: Hours Carousel Card not loaded.", false);
		return timeSheetCarouselCardsHours;
	}

	// added by Nishant

	public void validateLocationFilterIfNoLocationSelected(String locationFilterAllLocations) throws Exception{
		if(isElementLoaded(locationFilter,5) && isElementLoaded(txtLocationFilter,5)){
			if(txtLocationFilter.getAttribute("value").contains("All") || txtLocationFilter.getAttribute("value").contains("Location")){
				click(locationFilter);
				clickOnLocationFilterCheckBox(locationFilterAllLocations);
			}else{
				SimpleUtils.fail("Location Filter label "
						+txtLocationFilter.getAttribute("value")+ " not matching with " + locationFilterAllLocations,true);
			}
		}else{
			SimpleUtils.fail("Location Filter not displayed on page",false);
		}
	}

	public void validateLocationFilterIfDefaultLocationSelected(String locationFilterDefaultLocations) throws Exception{
		if(isElementLoaded(locationFilter,5) && isElementLoaded(txtLocationFilter,5)){
			if(txtLocationFilter.getAttribute("value").equalsIgnoreCase(locationFilterDefaultLocations)){
				if(areListElementVisible(timesheetTableRow,15,1)){
					SimpleUtils.pass("Timesheet data available for specified period");
				}else{
					SimpleUtils.fail("No date found in timesheet table for specific period",true);
				}
			}else{
				SimpleUtils.fail("Location Filter label "
						+txtLocationFilter.getAttribute("value")+ " not matching with " + locationFilterDefaultLocations,true);
			}
		}else{
			SimpleUtils.fail("Location Filter not displayed on page",false);
		}
	}


	public void clickOnLocationFilterCheckBox(String locationFilterAllLocations) throws Exception{
		if (areListElementVisible(locationCheckbox,5) &&
				areListElementVisible(locationCheckboxLabel,5)){
			for(WebElement locationCheckboxLabels : locationCheckboxLabel){
				if(locationCheckboxLabels.getText().equalsIgnoreCase(locationFilterAllLocations)){
					click(locationCheckboxLabels);
					click(locationFilter);
					SimpleUtils.pass("Location Filter checkbox clicked Successfully");
					break;
				}else{
					SimpleUtils.fail("Location Filter checkbox label "
							+locationCheckboxLabels.getText()+ " not matching with " + locationFilterAllLocations,true);
				}
			}
		}else{
			SimpleUtils.fail("Location Filter checkbox is not clickable",false);
		}
	}


	public void verifyTimesheetTableIfNoLocationSelected() throws  Exception{
		if(isElementLoaded(timesheetTableForNoLocationSelected,10)){
			SimpleUtils.pass("No data visible in timesheet data table as  None selected in Location filter");
		}else{
			SimpleUtils.fail("User is able to see Timesheet table data which is not correct",false);
		}
	}

	public void validateLocationFilterIfSpecificLocationSelected(String locationFilterSpecificLocations) throws Exception{
		if(isElementLoaded(locationFilter,5)){
			click(locationFilter);
			clickOnLocationFilterCheckBoxForSpecificLocation(locationFilterSpecificLocations);
			if(areListElementVisible(timesheetTableRow,15,1)){
				SimpleUtils.pass("User is able to see data in timesheet table");
			}else{
				SimpleUtils.fail("No date found in timesheet table",true);
			}
		}else{
			SimpleUtils.fail("Location Filter not displayed on page",false);
		}
	}


	public void clickOnLocationFilterCheckBoxForSpecificLocation(String locationFilterSpecificLocations) throws Exception{
		if (areListElementVisible(locationCheckbox,5) &&
				areListElementVisible(locationCheckboxLabel,5)){
			locationCheckboxLabel.get(0);
			for(int i=0; i<locationCheckboxLabel.size(); i++){
				if(locationCheckboxLabel.get(i).getText().toLowerCase().contains(locationFilterSpecificLocations.toLowerCase())){
					click(locationCheckboxLabel.get(i));
					SimpleUtils.pass(locationCheckboxLabel.get(i).getText() + " checkbox selected Successfully");
					click(locationFilter);
					break;
				}
			}
		}else{
			SimpleUtils.fail("Location Filter checkbox is not clickable",false);
		}
	}

	public void clickWorkerRow(List<WebElement> allWorkersRow, String locationFilterSpecificLocations) throws Exception{
		boolean flag= false;
		for(WebElement allWorkersRows : allWorkersRow){
			click(allWorkersRows);
			for(WebElement workersDayRow : workersDayRows){
				click(workersDayRow);
				String[] timesheetPopupDetailsWorkerNameAndShiftDay = openTimesheetModelPop();
				if(locationName.get(0).contains(locationFilterSpecificLocations)){
					flag = true;
					SimpleUtils.pass("Home Location " + locationName.get(0) + " for " +  timesheetPopupDetailsWorkerNameAndShiftDay[0] + "" +
							" for " + timesheetPopupDetailsWorkerNameAndShiftDay[1] + " matching with selected filter value " + locationFilterSpecificLocations);
					locationName.clear();
				}else{
					SimpleUtils.fail("Home Location " + locationName.get(0) + " for " +  timesheetPopupDetailsWorkerNameAndShiftDay[0] + "" +
							" for " + timesheetPopupDetailsWorkerNameAndShiftDay[1] + " doesn't" +
							" match with selected filter value " + locationFilterSpecificLocations, true);
				}
				locationName.clear();
			}
			click(allWorkersRows);
			break;
		}
	}

	public String[] openTimesheetModelPop() throws Exception{
		String[] timesheetPopupDetailsWorkerNameAndShiftDay = null;
		if(isElementEnabled(TSPopupDetailsModel,10)){
			if(isElementEnabled(TSPopupDetailsLocationName,5)){
				String textLocation = TSPopupDetailsLocationName.getText();
				locationName.add(textLocation);
				if (isElementLoaded(TSPopupDetailsWorkerNameAndShiftDay, 2)) {
					timesheetPopupDetailsWorkerNameAndShiftDay =
							(TSPopupDetailsWorkerNameAndShiftDay.getText().split("\n"))[0].split(" - ");
				}
				closeTimeSheetDetailPopUp();
			}
		}else{
			SimpleUtils.fail("Timesheet detail pop up not loaded Successfullly",false);
		}
		return timesheetPopupDetailsWorkerNameAndShiftDay;
	}

	//added by Nishant

	public String verifyTimesheetSmartCard() throws Exception {
		String valDueDate ="";
		String finalDueDate ="";
		if(isElementEnabled(dueDateSmartCard,5)){
			SimpleUtils.pass("Timesheet Due Date smart card loaded Successfully");
			verifyDueDateheader();
			if(isElementLoaded(dueDateValue,5)){
				valDueDate = dueDateValue.getText();
				String[] arrValDueDate = valDueDate.split(" ");
				finalDueDate = arrValDueDate[1];
			}
		}else{
			SimpleUtils.fail("Timesheet Due Date smart card not loaded Successfully",false);
		}
		return valDueDate;
	}

	public void verifyDueDateheader() throws Exception {
		if(isElementLoaded(dueDateHeader,5)){
			if(dueDateHeader.getText().equalsIgnoreCase("DUE DATE")){
				SimpleUtils.pass("Timesheet Due Date smart card header is " + dueDateHeader.getText() );
			}
		}else{
			SimpleUtils.fail("Timesheet Due Date smart card Header not loaded Successfully",true);
		}
	}

	public String verifyTimesheetDueHeader() throws Exception {
		String timesheetDueDate ="";
		String timesheetDueDateValue ="";
		LocalDate now = LocalDate.now();
		if(isElementEnabled(dueDateSmartCard,5)){
			if(isElementLoaded(dueDateTimesheetNote,5)){
				timesheetDueDate = dueDateTimesheetNote.getText();
				timesheetDueDateValue= timesheetDueDate.replaceAll("[^0-9]", "");
				SimpleUtils.pass("Timesheet Due Date is shown in Smart card as " + dueDateTimesheetNote);
			}
		}else{
			SimpleUtils.fail("Timesheet Due Date smart card not loaded Successfullly",false);
		}
		return timesheetDueDateValue;
	}

	public void validateLoadingOfTimeSheetSmartCard() throws Exception {
		if(isElementEnabled(timesheetApprovalSmartCard,10)){
			SimpleUtils.pass("Timesheet Approval Smart Card loaded Successfully!!");
		}else{
			SimpleUtils.fail("Timesheet Approval Smart Card not loaded Successfully!!",true);
		}
		if(isElementEnabled(totalUnplannedClocksSmartCard,10)){
			SimpleUtils.pass("Total Unplanned Smart Card loaded Successfully!!");
		}else{
			SimpleUtils.fail("Total Unplanned Smart Card not loaded Successfully!!",true);
		}
		if(isElementEnabled(SummaryOfUnplannedClocksSmartCard,10)){
			SimpleUtils.pass("Unplanned Clocks Detail Summary Smart Card loaded Successfully!!");
		}else{
			SimpleUtils.fail("Unplanned Clocks Detail Summary Smart Card not loaded Successfully!!",true);
		}
		if(areListElementVisible(timesheetTblRow,10)){
			SimpleUtils.pass("Rows of Timesheet table loaded Successfully on page!!");
		}else{
			SimpleUtils.fail("Rows of Timesheet table not loaded Successfully on page!!",false);
		}

	}

//	@FindBy(xpath = "//*[contains(@class,'day-week-picker-period-active')]/preceding-sibling::div[1]")
//	private WebElement immediatePastToCurrentActiveWeek;



	//	public void clickImmediatePastToCurrentActiveWeekInDayPicker() {
//		if (isElementEnabled(immediatePastToCurrentActiveWeek, 30)) {
//			click(immediatePastToCurrentActiveWeek);
//		} else {
//			SimpleUtils.report("This is a last week in Day Week picker");
//		}
//	}
	@FindBy(css = "div.day-week-picker-period-active")
	private WebElement daypicker;

	public void validateLoadingOfTimeSheetSmartCard(String nextWeekViewOrPreviousWeekView) throws Exception {
		String weekSelected = null;
		clickImmediatePastToCurrentActiveWeekInDayPicker();
		clickOnRefreshButton();
		SimpleUtils.assertOnFail("Timesheet page not loaded successfully", isTimeSheetPageLoaded(), false);
		weekSelected = daypicker.getText().replace("\n", " ");

		if(isElementEnabled(timesheetApprovalSmartCard,5)){
			SimpleUtils.pass("Timesheet Approval Smart Card loaded Successfully for week " + weekSelected);
		}else{
			SimpleUtils.fail("Timesheet Approval Smart Card not loaded Successfully for week " + weekSelected,true);
		}
		if(isElementEnabled(totalUnplannedClocksSmartCard,5)){
			SimpleUtils.pass("Total Unplanned Smart Card loaded Successfully for week " + weekSelected);
		}else{
			SimpleUtils.fail("Total Unplanned Smart Card not loaded Successfully for week " + weekSelected,true);
		}
		if(isElementEnabled(SummaryOfUnplannedClocksSmartCard,5)){
			SimpleUtils.pass("Unplanned Clocks Detail Summary Smart Card loaded Successfully for week " + weekSelected);
		}else{
			SimpleUtils.fail("Unplanned Clocks Detail Summary Smart Card not loaded Successfully for week " + weekSelected,true);
		}
		if(areListElementVisible(timesheetTblRow,5)){
			SimpleUtils.pass("Rows of Timesheet table loaded Successfully for week " + weekSelected + " on page!!");
		} else{
			SimpleUtils.fail("Rows of Timesheet table not loaded Successfully or week " + weekSelected + " on page!!",false);
		}

	}

	public int getUnplannedClocksValueNtext() throws Exception {
		int totalUnplannedClocksOnDMViewSmartCard = 0;
		if(areListElementVisible(totalUnplannedClocksSmartCardValueNTxt,1)){
			for(int i=0;i<totalUnplannedClocksSmartCardValueNTxt.size();i++){
//				listTotalUnplannedHrsText.add(totalUnplannedClocksSmartCardValueNTxt.get(i).getText());
				totalUnplannedClocksOnDMViewSmartCard  = totalUnplannedClocksOnDMViewSmartCard +
						Integer.parseInt((totalUnplannedClocksSmartCardValueNTxt.get(i).getText().split("\n"))[0]);
			}
		}else{
			SimpleUtils.fail("Total Unplanned Smart Card Text not loaded Successfully!!",true);
		}
		return totalUnplannedClocksOnDMViewSmartCard;
	}

	public int getUnplannedClocksDetailSummaryValue() throws Exception {
		int totalUnplannedClocksOnDMViewSmartCardDetailSummary = 0;
		if(areListElementVisible(detailSummaryUnplannedClocksVal,1)){
			for(int i=0;i<detailSummaryUnplannedClocksVal.size();i++){
				totalUnplannedClocksOnDMViewSmartCardDetailSummary  = totalUnplannedClocksOnDMViewSmartCardDetailSummary +
						Integer.parseInt((detailSummaryUnplannedClocksVal.get(i).getText().replace(",", "").split("\n"))[0]);
			}
		}else{
			SimpleUtils.fail("Detail Summary of Unplanned Smart Card Text not loaded Successfully!!",true);
		}
		return totalUnplannedClocksOnDMViewSmartCardDetailSummary;
	}

	public void goToSMView(List<String> searchLocation, String datePickerTxtDMView,
						   int locationCount, int totalUnplannedClocksOnDMView, int totalTimesheetsOnDMView) throws Exception {
		if(areListElementVisible(timesheetTblRow,2) && !searchLocation.isEmpty()) {
			for(int j=0; j<timesheetTblRow.size();j++) {
				if (j == locationCount) {
					break;
				}
				click(timesheetTblRow.get(j));
				validateLoadingOfTimeSheetSmartCardSMView();
				compareDMAndSMViewDatePickerText(datePickerTxtDMView);
				int totalUnplannedClocksOnSMView = getAlertsSmartCardValue();
				int totalTimehseetOnSMView = getAllTimesheetValOnSMView();
				compareDMAndSMViewUnplannedClocksCount(totalUnplannedClocksOnDMView, totalUnplannedClocksOnSMView);
				compareDMAndSMViewTimesheetCount(totalTimesheetsOnDMView, totalTimehseetOnSMView);
			}
		}else{
			SimpleUtils.fail("Rows of Timesheet table not loaded Successfully on page!!",false);
		}
	}

	public void validateLoadingOfTimeSheetSmartCardSMView() throws Exception {
		String activeWeek = getActiveWeekText();
		if(isElementEnabled(primarySmartCardTimesheet,5)){
			SimpleUtils.pass("Timesheet Primary Smart Card on SM View loaded Successfully for week " + activeWeek);
		}else{
			SimpleUtils.fail("Timesheet Primary Smart Card on SM View not loaded Successfully for week " + activeWeek,true);
		}
		if(isElementEnabled(dueDateOrApprovalSmartCard,5)){
			SimpleUtils.pass("Due Date OR Approval Smart Card loaded Successfully for week " + activeWeek);
		}else{
			SimpleUtils.fail("Due Date OR Approval Smart Card not loaded Successfully for week " + activeWeek,true);
		}
		if(isElementEnabled(alertSmartCardTimesheet,5)){
			SimpleUtils.pass("Alert Smart Card loaded Successfully for week " + activeWeek);
		}else{
			SimpleUtils.fail("Alert Smart Card not loaded Successfully for week " + activeWeek,true);
		}
		if(areListElementVisible(timesheetTableRow,10) && timesheetTableRow.size()>0){
			SimpleUtils.pass("Rows of Timesheet table loaded Successfully for week " + activeWeek + " on page!!");
		} else if (timesheetTable.getText().contains("There are no timesheets that match this period or filter. Please try again.")) {
            SimpleUtils.report("There are no timesheets that match this period or filter. Please try again.");
		} else {
			SimpleUtils.fail("Rows of Timesheet table not loaded Successfully or week " + activeWeek + " on page!!",false);
		}

	}

	public void clickOnLocationFilter(List<String> locationFilterAllLocations) throws Exception{
		if(isElementLoaded(locationFilter,5)){
			click(locationFilter);
			clickOnLocationFilterOnSMView(locationFilterAllLocations);
			if(areListElementVisible(timesheetTableRow,15,1)){
				SimpleUtils.pass("User is able to see data in timesheet table");
			}else{
				SimpleUtils.fail("No date found in timesheet table",true);
			}
		}else{
			SimpleUtils.fail("Location Filter not displayed on page",false);
		}
	}

	public void clickOnLocationFilterOnSMView(List<String> locationFilterSpecificLocations) throws Exception{
		if (areListElementVisible(locationCheckbox,5) &&
				areListElementVisible(locationCheckboxLabel,5) && isElementEnabled(clearLocationFilter,2)){
			click(clearLocationFilter);
			for(int i=0; i<locationCheckboxLabel.size(); i++){
				for(int j=0; j<locationFilterSpecificLocations.size();j++){
					if(locationCheckboxLabel.get(i).getText().toLowerCase().contains(locationFilterSpecificLocations.get(j).toLowerCase())){
						click(locationCheckboxLabel.get(i));
					}
				}
			}
		}else{
			SimpleUtils.fail("Location Filter checkbox is not clickable",false);
		}
		click(locationFilter);
	}

	public int getAlertsSmartCardValue() throws Exception {
		int totalUnplannedClocksOnSMView = 0;
		if(areListElementVisible(alertsSmartCardValue,2)){
			for(int i=0; i< alertsSmartCardValue.size();i++){
				totalUnplannedClocksOnSMView  = totalUnplannedClocksOnSMView + Integer.parseInt((alertsSmartCardValue.get(i).getText().split("\n"))[0]);
			}
		}else{
			SimpleUtils.fail("Alert Smart Card not displayed on page",true);
		}
		return totalUnplannedClocksOnSMView;
	}

	public int getAllTimesheetValOnSMView() throws Exception {
		int totalTimehseetOnSMView = 0;
		if(areListElementVisible(timesheetTableRow,10)
				&& areListElementVisible(totalTimesheetsOnSMView,10)){
			String paginationValue = "";
			if (pagination.getText().contains("f "))
				paginationValue = pagination.getText().split("f ")[1];
			System.out.println("paginationValue is "+paginationValue);
			if (paginationValue.isEmpty())
				paginationValue = "1";
			for(int j=0; j<Integer.parseInt(paginationValue); j++) {
				if(areListElementVisible(timesheetTableRow,10)
						&& areListElementVisible(totalTimesheetsOnSMView,10)){
					for (int i = 0; i < timesheetTableRow.size(); i++) {
						totalTimehseetOnSMView = totalTimehseetOnSMView + Integer.parseInt((totalTimesheetsOnSMView.get(i).getText().split(" "))[0]);
					}
				}
				if (isElementLoaded(footerRightNavigationDisabled, 2)) {
					break;
				} else {
					clickTheElement(footerRightNavigation);
				}
			}
		}else{
			SimpleUtils.fail("No date found in timesheet table",true);
		}
		return totalTimehseetOnSMView;
	}

	public List<String> getLocationName() throws Exception {
		List<String> listLocationName = new ArrayList<>();
		if(areListElementVisible(listLocations,15)){
			for(int i=0; i<listLocations.size();i++){
				listLocationName.add(listLocations.get(i).getText());
			}
		}else{
			SimpleUtils.report("Location not displayed on DM view of Timesheet table");
		}
		return listLocationName;
	}

	public int getUnplannedClocksOnDMView() throws Exception {
		int totalUnplannedClocksOnDMView = 0;
		if(areListElementVisible(unplannedClocksTblView,2)){
			for(int i=0; i< unplannedClocksTblView.size();i++){
				totalUnplannedClocksOnDMView  = totalUnplannedClocksOnDMView +
						Integer.parseInt((unplannedClocksTblView.get(i).getText().replace(",", "")));
			}
		}else{
			SimpleUtils.fail("Unplanned Clocks data not displayed on DM view of Timesheet table",true);
		}
		return totalUnplannedClocksOnDMView;
	}

	public int getTotalTimesheetsOnDMView() throws Exception {
		int totalTimesheetsOnDMView = 0;
		if(areListElementVisible(totalTimesheetsTblView,2)){
			for(int i=0; i< totalTimesheetsTblView.size();i++){
				totalTimesheetsOnDMView  = totalTimesheetsOnDMView +
						Integer.parseInt((totalTimesheetsTblView.get(i).getText().replace(",", "")));
			}
		}else{
			SimpleUtils.fail("Total Timesheet data not displayed on DM view of Timesheet table",true);
		}
		return totalTimesheetsOnDMView;
	}

	public void compareDMAndSMViewTimesheetCount(int totalTimesheetsOnDMView, int totalTimehseetOnSMView){
		if(totalTimesheetsOnDMView== totalTimehseetOnSMView){
			SimpleUtils.pass("Timesheet Count on DM View " + totalTimesheetsOnDMView + " " +
					" matches with Timesheet Count on SM View " + totalTimehseetOnSMView);
		}else{
			SimpleUtils.fail("Timesheet Count on DM View " + totalTimesheetsOnDMView + " " +
					" do not match with Timesheet Count on SM View " + totalTimehseetOnSMView,true);
		}
	}

	public void compareDMAndSMViewUnplannedClocksCount(int totalUnplannedClocksOnDMView, int totalUnplannedClocksOnSMView){
		if(totalUnplannedClocksOnDMView== totalUnplannedClocksOnSMView){
			SimpleUtils.pass("Unplanned Clocks Count on DM View " + totalUnplannedClocksOnDMView + " " +
					" matches with Unplanned Clocks Count on SM View " + totalUnplannedClocksOnSMView);
		}else{
			SimpleUtils.fail("Unplanned Clocks Count on DM View " + totalUnplannedClocksOnDMView + " " +
					" do not match with Unplanned Clocks Count on SM View " + totalUnplannedClocksOnSMView,true);
		}
	}

	public int getUnplannedClockSmartCardOnDMView() throws Exception {
		int totalUnplannedClockSmartCardValOnDMView = 0;
		if(isElementEnabled(totalUnplannedClocksSmartCardValueOnDMView,2)){
			totalUnplannedClockSmartCardValOnDMView = Integer.parseInt(totalUnplannedClocksSmartCardValueOnDMView.getText());
		}
		return totalUnplannedClockSmartCardValOnDMView;
	}

	public int getTotalTimesheetFromSmartCardOnDMView() throws Exception {
		int totalTimesheetOnDMViewSmartCard = 0;
		if(isElementEnabled(totalTimesheetSmartCardValueOnDMView,2)){
			totalTimesheetOnDMViewSmartCard = Integer.parseInt((totalTimesheetSmartCardValueOnDMView.getText().split(" "))[0]);
		}
		return totalTimesheetOnDMViewSmartCard;
	}

	public void goToSMView() throws Exception {
		String activeWeekOnDMView = getActiveWeekText();
		if(areListElementVisible(timesheetTblRow,2)){
			for(int j=0; j<timesheetTblRow.size();j++) {
				if (j == 1) {
					break;
				}
				click(timesheetTblRow.get(j));
				validateLoadingOfTimeSheetSmartCardSMView(activeWeekOnDMView);
			}
		}else{
			SimpleUtils.fail("Rows of Timesheet table not loaded Successfully on page!!",false);
		}
	}

	public void validateLoadingOfTimeSheetSmartCardSMView(String activeWeekOnDMView) throws Exception {
		waitForSeconds(2);
		compareDMAndSMViewDatePickerText(activeWeekOnDMView);
		if(isElementEnabled(primarySmartCardTimesheet,5)){
			SimpleUtils.pass("Timesheet Primary Smart Card on SM View loaded Successfully for week " + activeWeekOnDMView);
		}else{
			SimpleUtils.fail("Timesheet Primary Smart Card on SM View not loaded Successfully for week " + activeWeekOnDMView,true);
		}
		if(isElementEnabled(dueDateOrApprovalSmartCard,5)){
			SimpleUtils.pass("Due Date OR Approval Smart Card loaded Successfully for week " + activeWeekOnDMView);
		}else{
			SimpleUtils.fail("Due Date OR Approval Smart Card not loaded Successfully for week " + activeWeekOnDMView,true);
		}
		if(isElementEnabled(alertSmartCardTimesheet,5)){
			SimpleUtils.pass("Alert Smart Card loaded Successfully for week " + activeWeekOnDMView);
		}else{
			SimpleUtils.fail("Alert Smart Card not loaded Successfully for week " + activeWeekOnDMView,true);
		}
		if(areListElementVisible(timesheetTableRow,10) && timesheetTableRow.size()>0){
			SimpleUtils.pass("Rows of Timesheet table loaded Successfully for week " + activeWeekOnDMView + " on page!!");
		} else if (timesheetTable.getText().contains("There are no timesheets that match this period or filter. Please try again.")) {
			SimpleUtils.report("There are no timesheets that match this period or filter. Please try again.");
		}else{
			SimpleUtils.fail("Rows of Timesheet table not loaded Successfully or week " + activeWeekOnDMView + " on page!!",false);
		}

	}

	public void validateLoadingOfComplianceOnDMView(String nextWeekViewOrPreviousWeekView, boolean currentWeek) throws Exception {
		String weekSelected = null;
		if(!currentWeek){
			clickImmediatePastToCurrentActiveWeekInDayPicker();
		}
		weekSelected = daypicker.getText().replace("\n", " ");
		if(isElementEnabled(totalViolationHoursSmartCard,5)){
			SimpleUtils.pass("Compliance Violations Smart Card loaded Successfully for week " + weekSelected);
		}else{
			SimpleUtils.fail("Compliance Violations Smart Card not loaded Successfully for week " + weekSelected,true);
		}
		if(isElementEnabled(locationWithViolationSmartCard,5)){
			SimpleUtils.pass("Location with  Compliance Violation Smart Card loaded Successfully for week " + weekSelected);
		}else{
			SimpleUtils.fail("Location with  Compliance Violation Smart Card ot  Successfully for week " + weekSelected,true);
		}
		if(areListElementVisible(timesheetTblRow,5)){
			SimpleUtils.pass("Rows of Compliance table loaded Successfully for week " + weekSelected + " on page!!");
		}else{
			SimpleUtils.fail("Rows of Compliance table not loaded Successfully or week " + weekSelected + " on page!!",false);
		}

	}

	public void toNFroNavigationFromDMDashboardToDMCompliance(String CurrentWeek) throws Exception{
		String weekSelected = null;
		validateLoadingOfComplianceOnDMView("nextWeekViewOrPreviousWeekView", true);
	}

	@FindBy(css = "div[ng-click='viewCompliance()']")
	private WebElement viewViolationOnDashboard;

	public void clickOnComplianceViolationSectionOnDashboard() throws Exception {
		if(isElementLoaded(viewViolationOnDashboard,5)){
			clickTheElement(viewViolationOnDashboard);
			SimpleUtils.pass("View Violation link on Dashboard clicked Successfully!!");
		}else{
			SimpleUtils.fail("View Violation link on Dashboard not clicked Successfully!!",false);
		}
	}

	// Added by Nora
	@FindBy(css = "input[placeholder=\"Select location\"]")
	private WebElement selectLocationLabel;
	@FindBy(css = ".lg-filter__category-items [ng-repeat=\"opt in opts\"]")
	private List<WebElement> locationItemsInFilter;
	@FindBy(css = ".lg-filter__clear.lg-filter__clear-active")
	private WebElement activeClearFilterBtn;
	@FindBy(css = ".lg-filter__wrapper input[placeholder=\"Search Location\"]")
	private WebElement searchLocationInput;
	@FindBy(css = ".lg-timesheet-progress__number")
	private WebElement timesheetApprovalRate;
	@FindBy(css = ".day-week-picker-period-active")
	private WebElement activeWeek;

	@Override
	public void verifyCurrentWeekIsSelectedByDefault(String currentWeek) throws Exception {
		if (isElementLoaded(activeWeek, 5)) {
			if (activeWeek.getText().toLowerCase().contains(currentWeek.toLowerCase())) {
				SimpleUtils.pass("Current Week: " + activeWeek.getText() + " is selected by default on TimeSheet page!");
			}else {
				SimpleUtils.fail("Current Week: " + activeWeek.getText() + " is not selected by default on TimeSheet page!"
						+ " Selected week is: " + activeWeek.getText() + ", but current week on dashboard starts with: " + currentWeek, false);
			}
		}else {
			SimpleUtils.fail("TimeSheet Page: active week not loaded Successfully!", false);
		}
	}

	@Override
	public List<String> getAlertsDataFromSmartCard() throws Exception {
		List<String> alerts = new ArrayList<>();
		// wait for the data loaded on smart card
		waitForSeconds(3);
		if (areListElementVisible(alertsSmartCardValue, 5) && alertsSmartCardValue.size() == 6) {
			for (WebElement alertsCell : alertsSmartCardValue) {
				if (!alertsCell.getText().isEmpty()) {
					alerts.add(alertsCell.getText());
					SimpleUtils.report("Get the alerts Data: \"" + alertsCell.getText() + "\" Successfully!");
				}else {
					SimpleUtils.fail("Failed to get the alert data!", false);
				}
			}
		}else {
			SimpleUtils.fail("The Alerts data not loaded Successfully!", false);
		}
		return alerts;
	}

	@Override
	public int getApprovalRateFromTimesheetByLocation(String location) throws Exception {
		int rate = 0;
		filterTheLocationByName(location);
		if (isElementLoaded(timesheetApprovalRate, 5)) {
			SimpleUtils.pass("TimeSheet Page: Get the timesheet approval rate: " + timesheetApprovalRate.getText()
					+ " for location: " + location + " Successfully");
			rate = timesheetApprovalRate.getText().contains("%") ? Integer.parseInt(timesheetApprovalRate.getText().replaceAll("%", "")) : 0;
		}else {
			SimpleUtils.fail("TimeSheet Page: timesheet approval rate not loaded Successfully!", false);
		}
		return rate;
	}

	private void filterTheLocationByName(String locationName) throws Exception {
		if (isElementLoaded(selectLocationLabel, 10)) {
			clickTheElement(selectLocationLabel);
			if (areListElementVisible(locationItemsInFilter, 10)) {
				SimpleUtils.pass("Timesheet Page: Click on the Location Filter Successfully!");
				if (isElementLoaded(activeClearFilterBtn, 10)) {
					clickTheElement(activeClearFilterBtn);
					boolean isClear = true;
					for (WebElement locationItem : locationItemsInFilter) {
						WebElement checkbox = locationItem.findElement(By.cssSelector("input[type=\"checkbox\"]"));
						if (checkbox != null) {
							if (checkbox.getAttribute("class").contains("ng-not-empty")) {
								isClear = false;
								break;
							}
						}else {
							isClear = false;
							break;
						}
					}
					if (!isClear) {
						SimpleUtils.fail("TimeSheet Page: Click on Clear Filter button not Successfully!", false);
					}else {
						SimpleUtils.pass("TimeSheet Page: Click on Clear Filter button Successfully!");
					}
				}
				if (isElementLoaded(searchLocationInput, 5)) {
					searchLocationInput.sendKeys(locationName);
					waitForSeconds(2);
					if (areListElementVisible(locationItemsInFilter, 5)) {
						for (WebElement locationItem : locationItemsInFilter) {
							if (locationItem.getText().contains(locationName)) {
								WebElement checkbox = locationItem.findElement(By.cssSelector("input[type=\"checkbox\"]"));
								clickTheElement(checkbox);
								break;
							}
						}
					}else {
						SimpleUtils.fail("Failed to find the location: " + locationName, false);
					}
				}else {
					SimpleUtils.fail("TimeSheet Page: Search Location Input not loaded Successfully!", false);
				}
			}else {
				SimpleUtils.fail("TimeSheet Page: Click on location filter failed!", false);
			}
		}else {
			SimpleUtils.fail("TimeSheet Page: Search Location Filter not loaded Successfully!", false);
		}
	}

	//added by Estelle
	@FindBy(css = "lg-button[label=\"Approve\"]")
	private WebElement approveBtn;
	@Override
	public String verifyLocationList() throws Exception {
		if (areListElementVisible(timesheetTblRow,35)) {
			if (timesheetTblRow.size()>0) {
				SimpleUtils.pass("Location list load successfully");
				String[] locationNameContext =timesheetTblRow.get(0).getText().split("\n");
				String locationName = locationNameContext[0];
				click(timesheetTblRow.get(0));
				if (isElementLoaded(approveBtn,5)) {
					SimpleUtils.pass("Can enter "+ locationName+"location's timesheet page");
				}else
					SimpleUtils.fail("Enter location"+locationName+"location's timesheet page failed",false);
				return locationName;
			}
		}else
			SimpleUtils.report("There is no location for this district");
		return null;
	}

	// Added By Julie
	@FindBy (css = "[ng-attr-style*=\"smartCardData.Timesheet.totalTimesheets\"]")
	private List<WebElement> ratesOnDMViewSmartCard;

	@Override
	public List<String> getTimesheetApprovalRateOnDMViewSmartCard() throws Exception {
		List<String> rates = new ArrayList<>();
		// wait for the data loaded on smart card
		waitForSeconds(3);
		if (areListElementVisible(ratesOnDMViewSmartCard, 5) && ratesOnDMViewSmartCard.size() == 4) {
			for (WebElement rate : ratesOnDMViewSmartCard) {
				if (!rate.getText().isEmpty()) {
					rates.add(rate.getText().trim());
					SimpleUtils.report("Timesheet Page: Get the rate data: \"" + rate.getText().trim() + "\" on DM View smart card Successfully!");
				} else {
					SimpleUtils.fail("Timesheet Page: Failed to get the rate data on DM View smart card", false);
				}
			}
		} else {
			SimpleUtils.fail("Timesheet Page: The Alerts data not loaded on DM View smart card", false);
		}
		return rates;
	}

	// Added By Julie
	@FindBy(css = "[ng-click=\"$ctrl.onReload(true)\"]")
	private WebElement refreshButton;

	@FindBy(css = "[ng-if=\"$ctrl.minutes >= 0 && $ctrl.date && !$ctrl.loading\"]")
	private WebElement lastUpdatedIcon;

	@FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes === 0\"]")
	private WebElement justUpdated;

	@FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes > 0\"]")
	private WebElement lastUpdated;

	@FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes > 0\"] span")
	private WebElement lastUpdatedMinutes;

	@FindBy (css = ".console-navigation-item-label.Schedule")
	private WebElement scheduleConsoleMenu;

	@FindBy (css = ".analytics-new.ng-scope")
	private WebElement timesheetSection;

	@FindBy (className = "analytics-new-table-group-row-open")
	private List<WebElement> rowsInAnalyticsTable;

	@FindBy (css = ".console-navigation-item-label.Timesheet")
	private WebElement timesheetConsoleMenu;

	@FindBy (css = ".day-week-picker-period-week")
	private List<WebElement> currentWeeks;

	@FindBy(className = "day-week-picker-arrow-right")
	private WebElement calendarNavigationNextWeekArrow;

	@FindBy(className = "day-week-picker-arrow-left")
	private WebElement calendarNavigationPreviousWeekArrow;

	@FindBy(css = "input-field[value=\"$ctrl.allSelected\"]")
	private WebElement allSelectedCheckBox;

	@FindBy(css = "input-field[value=\"day.selected\"]")
	private List<WebElement> daySelectedCheckBox;

	@FindBy(css = "input-field[value=\"worker.selected\"]")
	private List<WebElement> workerSelectedCheckBox;

	@FindBy(css = "lg-button[label=\"Approve\"] button")
	private WebElement timesheetApproveBtn;

	@Override
	public void clickOnRefreshButton() throws Exception {
		if (isElementLoaded(refreshButton, 10)) {
			clickTheElement(refreshButton);
			if(isElementLoaded(lastUpdatedIcon, 120)){
				SimpleUtils.pass("Click on Refresh button Successfully!");
			} else
				SimpleUtils.fail("Refresh timeout! ", false);
		} else {
			SimpleUtils.fail("Refresh button not Loaded!", true);
		}
	}

	@Override
	public void validateThePresenceOfRefreshButton() throws Exception {
		if (isElementLoaded(refreshButton,10)) {
			if (refreshButton.isDisplayed() && !refreshButton.getText().isEmpty() && refreshButton.getText() != null) {
				if (getDriver().findElement(By.xpath("//body//day-week-picker/following-sibling::last-updated-countdown/div/lg-button")).equals(refreshButton)) {
					SimpleUtils.pass("Timesheet Page: Refresh button shows near week section successfully");
				} else {
					SimpleUtils.fail("Timesheet Page: Refresh button is not above welcome section", true);
				}
			} else {
				SimpleUtils.fail("Timesheet Page: Refresh button isn't present", true);
			}
		} else {
			SimpleUtils.fail("Timesheet Page: Refresh button failed to load", true);
		}
	}

	@Override
	public void validateRefreshFunction() throws Exception {
		int minutes = 0;
		if (isElementLoaded(lastUpdatedMinutes,10) ) {
			minutes = lastUpdatedMinutes.getText().contains(" ")? Integer.valueOf(lastUpdatedMinutes.getText().split(" ")[0]):Integer.valueOf(lastUpdatedMinutes.getText());
			if (minutes >= 30 ) {
				if (lastUpdatedMinutes.getAttribute("class").contains("last-updated-countdown-time-orange"))
					SimpleUtils.pass("Timesheet Page: When the Last Updated time >= 30 mins, the color changes to orange");
				else
					SimpleUtils.fail("Timesheet Page: When the Last Updated time >= 30 mins, the color failed to change to orange",false);
			}
		}
		if (isElementLoaded(refreshButton, 10)) {
			clickTheElement(refreshButton);
			SimpleUtils.pass("Timesheet Page: Click on Refresh button Successfully!");
			if (timesheetSection.getAttribute("class").contains("analytics-new-refreshing") && refreshButton.getAttribute("label").equals("Refreshing...")) {
				SimpleUtils.pass("Timesheet Page: After clicking Refresh button, the background is muted and it shows an indicator 'Refreshing...' that we are processing the info");
				if (isElementLoaded(justUpdated,60) && timesheetSection.getAttribute("class").contains("home-dashboard-loading"))
					SimpleUtils.pass("Timesheet Page: Once the data is done refreshing, the page shows 'JUST UPDATED' and page becomes brighter again");
				else
					SimpleUtils.fail("Timesheet Page: When the data is done refreshing, the page doesn't show 'JUST UPDATED' and page doesn't become brighter again",false);
				if (isElementLoaded(lastUpdated,60) && lastUpdatedMinutes.getAttribute("class").contains("last-updated-countdown-time-blue"))
					SimpleUtils.pass("Timesheet Page: The Last Updated info provides the minutes last updated in blue");
				else
					SimpleUtils.fail("Timesheet Page: The Last Updated info doesn't provide the minutes last updated in blue",false);
			} else {
				SimpleUtils.fail("Timesheet Page: After clicking Refresh button, the background isn't muted and it doesn't show 'Refreshing...'",true);
			}
		} else {
			SimpleUtils.fail("Timesheet Page: Refresh button not Loaded!", true);
		}
	}

	@Override
	public void validateRefreshPerformance() throws Exception {
		if (isElementLoaded(refreshButton, 10)) {
			clickTheElement(refreshButton);
			if (refreshButton.getAttribute("label").equals("Refreshing...")) {
				SimpleUtils.pass("Timesheet Page: After clicking Refresh button, the button becomes 'Refreshing...'");
				WebElement element = (new WebDriverWait(getDriver(), 60))
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[label=\"Refresh\"]")));
				if (element.isDisplayed()) {
					SimpleUtils.pass("Timesheet Page: Page refreshes within 1 minute successfully");
				} else {
					SimpleUtils.fail("Timesheet Page: Page doesn't refresh within 1 minute", false);
				}
			} else {
				SimpleUtils.fail("Timesheet Page: After clicking Refresh button, the background isn't muted and it doesn't show 'Refreshing...'",true);
			}
		} else {
			SimpleUtils.fail("Timesheet Page: Refresh button not Loaded!", true);
		}
	}

	@Override
	public void validateRefreshTimestamp() throws Exception {
		String timestamp = "";
		if (isElementLoaded(justUpdated, 5)) {
			SimpleUtils.pass("Timesheet Page: The page just refreshed");
		} else if (isElementLoaded(lastUpdatedMinutes, 5)) {
			timestamp = lastUpdatedMinutes.getText();
			if (timestamp.contains("HOURS") && timestamp.contains(" ")) {
				timestamp = timestamp.split(" ")[0];
				if (Integer.valueOf(timestamp) == 1)
					SimpleUtils.pass("Timesheet Page: The backstop is 1 hour so that the data is not older than 1 hour stale");
				else
					// SimpleUtils.fail("Schedule Page: The backstop is older than 1 hour stale",false);
					SimpleUtils.warn("SCH-2589: [DM View] Refresh time is older than 1 hour stale");
			} else if (timestamp.contains("MINS") && timestamp.contains(" ")) {
				timestamp = timestamp.split(" ")[0];
				if (Integer.valueOf(timestamp) < 60 && Integer.valueOf(timestamp) >= 1)
					SimpleUtils.pass("Timesheet Page: The backstop is last updated " + timestamp + " mins ago");
				else
					SimpleUtils.fail("Timesheet Page: The backup is last updated " + timestamp + " mins ago actually", false);
			} else
				SimpleUtils.fail("Timesheet Page: The backup display \'" + lastUpdated.getText() + "\'",false);
		} else
			SimpleUtils.fail("Timesheet Page: Timestamp failed to load", false);
	}

	@Override
	public void navigateToSchedule() throws Exception {
		if (isElementLoaded(scheduleConsoleMenu, 10)) {
			click(scheduleConsoleMenu);
			if (scheduleConsoleMenu.findElement(By.xpath("./..")).getAttribute("class").contains("active"))
				SimpleUtils.pass("Schedule Page: Click on Schedule console menu successfully");
			else
				SimpleUtils.fail("Schedule Page: It doesn't navigate to Schedule console menu after clicking", false);
		} else {
			SimpleUtils.fail("Schedule menu in left navigation is not loaded!",false);
		}
	}

	@Override
	public void validateRefreshWhenNavigationBack() throws Exception {
		String timestamp1 = "";
		String timestamp2 = "";
		if (isElementLoaded(lastUpdated, 5)) {
			timestamp1 = lastUpdated.getText();
		} else if (isElementLoaded(justUpdated, 5)) {
			timestamp1 = justUpdated.getText();
		} else
			SimpleUtils.fail("Timesheet Page: Timestamp failed to load", false);
		navigateToSchedule();
		clickOnTimeSheetConsoleMenu();
		if (isElementLoaded(lastUpdated, 5)) {
			timestamp2 = lastUpdated.getText();
		} else if (isElementLoaded(justUpdated, 5)) {
			timestamp2 = justUpdated.getText();
		} else
			SimpleUtils.fail("Timesheet Page: Timestamp failed to load", false);
		if (timestamp2.equals(timestamp1) && !timestamp1.equals("") && !refreshButton.getAttribute("label").equals("Refreshing...")) {
			SimpleUtils.pass("Timesheet Page: It keeps the previous Last Updated time, not refreshing every time");
		} else {
			SimpleUtils.fail("Timesheet Page: It doesn't keep the previous Last Updated time", false);
		}
	}

	@Override
	public void navigateToPreviousWeek() throws Exception {
		int currentWeekIndex = -1;
		if (areListElementVisible(currentWeeks, 10)) {
			for (int i = 0; i < currentWeeks.size(); i++) {
				String className = currentWeeks.get(i).getAttribute("class");
				if (className.contains("day-week-picker-period-active")) {
					currentWeekIndex = i;
				}
			}
			if (currentWeekIndex == 0 && isElementLoaded(calendarNavigationPreviousWeekArrow, 5)) {
				clickTheElement(calendarNavigationPreviousWeekArrow);
				if (areListElementVisible(currentWeeks, 5)) {
					clickTheElement(currentWeeks.get(currentWeeks.size()-1));
					SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeeks.size()-1).getText() + "' Successfully!");
				}
			} else {
				clickTheElement(currentWeeks.get(currentWeekIndex - 1));
				SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeekIndex - 1).getText() + "' Successfully!");
			}
		} else {
			SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
		}
	}

	@Override
	public void navigateToNextWeek() throws Exception {
		int currentWeekIndex = -1;
		if (areListElementVisible(currentWeeks, 10)) {
			for (int i = 0; i < currentWeeks.size(); i++) {
				String className = currentWeeks.get(i).getAttribute("class");
				if (className.contains("day-week-picker-period-active")) {
					currentWeekIndex = i;
				}
			}
			if (currentWeekIndex == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
				clickTheElement(calendarNavigationNextWeekArrow);
				if (areListElementVisible(currentWeeks, 5)) {
					clickTheElement(currentWeeks.get(0));
					SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(0).getText() + "' Successfully!");
				}
			} else {
				clickTheElement(currentWeeks.get(currentWeekIndex + 1));
				SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(currentWeekIndex + 1).getText() + "' Successfully!");
			}
		} else {
			SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
		}
	}

	@Override
	public void clickOnGivenLocation(String location) throws Exception {
		if (areListElementVisible(rowsInAnalyticsTable,10)) {
			for (WebElement row: rowsInAnalyticsTable) {
				if (row.findElement(By.xpath("./div[1]/span/img/following-sibling::span")).getText().equals(location)) {
					clickTheElement(row.findElement(By.xpath("./div[1]/span/img/following-sibling::span")));
					SimpleUtils.pass("Timesheet Page: Find the location " + location + " successfully");
					break;
				}
			}
		} else
			SimpleUtils.fail("Timesheet Page: There are no locations in current district or failed to load",false);
	}

	@Override
	public String getTimesheetApprovalForGivenLocationInDMView(String location) throws Exception {
		String timesheetApproval = "";
		if (areListElementVisible(rowsInAnalyticsTable,10)) {
			for (WebElement row : rowsInAnalyticsTable) {
				if (row.findElement(By.xpath("./div[1]/span/img/following-sibling::span")).getText().equals(location)) {
					timesheetApproval = row.findElement(By.xpath("./div[4]/span")).getText().trim();
					SimpleUtils.pass("Timesheet Page: Find the location " + location + " with Timesheet Approval " + timesheetApproval);
					break;
				}
			}
		} else
			SimpleUtils.fail("Timesheet Page: There are no locations in current district or failed to load",false);
		return timesheetApproval;
	}

	@Override
	public void approveAnyTimesheet() throws Exception {
		if (areListElementVisible(workerSelectedCheckBox,15)) {
			int index1 = 0;
			if (workerSelectedCheckBox.size()>1)
				index1 = (new Random()).nextInt(workerSelectedCheckBox.size() - 1);
			click(workerSelectedCheckBox.get(index1).findElement(By.xpath("./../../div[2]")));
			if (areListElementVisible(daySelectedCheckBox,10)) {
				int index2 = 0;
				if (daySelectedCheckBox.size() > 1)
					index2 = (new Random()).nextInt(daySelectedCheckBox.size() - 1);
				String id = daySelectedCheckBox.get(index2).findElement(By.xpath("./../..")).getAttribute("id");
				click(daySelectedCheckBox.get(index2));
				clickOnApproveButtonInTimesheetTable();
				WebElement status = getDriver().findElement(By.xpath("//*[@id=\"" + id + "\"]/div[4]/lg-eg-status"));
				if (status.getAttribute("type").equals("Approved"))
					SimpleUtils.pass("Timesheet Page: Approve any timesheet successfully");
				else
					SimpleUtils.fail("Timesheet Page: Failed to approve any timesheet",false);
			} else
				SimpleUtils.fail("Timesheet Page: Timesheet day rows failed to load",false);
		} else
			SimpleUtils.fail("Timesheet Page: Workers failed to load",false);
	}

	@Override
	public boolean isWorkerDisplayInTimesheetTable() throws Exception {
		boolean isWorkerDisplay = false;
		if (areListElementVisible(workerSelectedCheckBox,10) && workerSelectedCheckBox.size() > 0) {
			isWorkerDisplay = true;
		}
		return isWorkerDisplay;
	}

	@Override
	public boolean isTimeSheetApproveButtonActive() throws Exception {
		if (isElementLoaded(timesheetApproveBtn,10)) {
			if(!timesheetApproveBtn.getCssValue("cursor").contains("not-allowed"))
				return true;
		} else
			SimpleUtils.fail("Timesheet Page: Timesheet Approve button failed to load",false);
		return false;
	}

	@Override
	public void clickOnApproveButtonInTimesheetTable() throws Exception {
		if (isTimeSheetApproveButtonActive()) {
			click(timesheetApproveBtn);
			SimpleUtils.pass("Timesheet Page: Timesheet table 'Approve' button clicked successfully.");
		} else
			SimpleUtils.fail("Timesheet Page: Timesheet Approve button failed to load or is inactive", false);
	}

	@Override
	public String getApprovalRateFromTIMESHEETDUESmartCard() throws Exception {
		String rate = "";
		if (isElementLoaded(timesheetApprovalRate, 5)) {
			SimpleUtils.pass("Timesheet Page: Get the timesheet approval rate: " + timesheetApprovalRate.getText()
					+ " successfully");
			rate = timesheetApprovalRate.getText();
		} else
			SimpleUtils.fail("Timesheet Page: Timesheet approval rate not loaded Successfully!", false);
		return rate;
	}

	@FindBy(className = "lg-timeclocks-improved__action-buttons-save")
	private WebElement timeSheetDetailsSaveBtn;

	@FindBy(id = "id_back_button")
	private WebElement timeSheetDetailsBackBtn;

	@FindBy(id = "id_timesheet_clock_0_action_popup_trigger")
	private WebElement timeSheetDetailsActionBtn;

	@FindBy(id = "id_timesheet_clock_action_button_add_clock_after")
	private WebElement timeSheetDetailsAddClockAfterBtn;

	@FindBy(css = "lg-new-timeclock-editor-improved .field-container")
	private List<WebElement> timeSheetDetailsClocks;

	@FindBy(id = "id_timesheet_clock_action_button_delete_clock")
	private WebElement timeSheetDetailsDeleteClock;

	@FindBy(id = "id_timesheet_clock_action_button_add_clock_after")
	private WebElement timeSheetDetailsAddClockAfter;

	@FindBy(xpath = "//div[contains(text(), 'Add ')][contains(text(), 'lock')]")
	private WebElement timeSheetDetailsAddClock;

	@FindBy(css = "#id_timesheet_clock_0_location select")
	private WebElement timeSheetDetailsLocationSelect;

	@FindBy(className = "lgn-alert-modal")
	private WebElement timeSheetDetailsAlertModal;

	@FindBy(className = "lgn-action-button-success")
	private WebElement timeSheetDetailsSaveBtnInAlertModal;

	@FindBy(className = "dropdown-toggle")
	private WebElement dropdownToggle;

	@FindBy(className = "dropdown-menu-right")
	private WebElement reaggregateTimesheetOption;

	@FindBy(className = "lg-modal")
	private WebElement reaggregateTimesheetPopup;

	@FindBy(css = "[label=\"Confirm\"]")
	private WebElement reaggregateTimesheetPopupConfirmBtn;

	@Override
	public void reaggregateTimesheet() throws Exception {
		if (isElementLoaded(dropdownToggle,10)) {
			click(dropdownToggle);
			click(reaggregateTimesheetOption);
			if (isElementLoaded(reaggregateTimesheetPopup,10))
				click(reaggregateTimesheetPopupConfirmBtn);
			if(isElementLoaded(successMsg, 30))
				SimpleUtils.pass("Timesheet Page: Timesheet successfully re-aggregated");
			else
				SimpleUtils.fail("Timesheet Page: Timesheet failed to re-aggregate",false);
		} else
			SimpleUtils.fail("Timesheet Page: Dropdown Toggle icon failed to load",false);
	}

	@Override
	public void saveTimeSheetDetail() throws Exception {
		if(isElementLoaded(timeSheetDetailsSaveBtn,10)) {
			click(timeSheetDetailsSaveBtn);
			if (isElementLoaded(timeSheetDetailsAlertModal,2))
				click(timeSheetDetailsSaveBtnInAlertModal);
			if (isElementLoaded(successMsg, 5))
				SimpleUtils.pass("Timesheet Page: Timesheet Details Edit popup Saved successfully");
			else
				SimpleUtils.fail("Timesheet Page: Timesheet Details Edit popup failed to save clock",false);
		} else
			SimpleUtils.fail("Timesheet Page: Timesheet Detail Popup Save Button not found!", true);
	}

	@Override
	public void clickOnTimeSheetDetailBackBtn() throws Exception {
		if(isElementLoaded(timeSheetDetailsBackBtn,10)) {
			click(timeSheetDetailsBackBtn);
			if(isElementLoaded(timesheetTable,10))
				SimpleUtils.pass("Timesheet Page: Timesheet Details Edit popup goes back successfully");
			else
				SimpleUtils.fail("Timesheet Page: Timesheet Details Edit popup failed to go back",false);
		} else
			SimpleUtils.fail("Timesheet Page: Timesheet Detail Back Button not found!", true);
	}

	@Override
	public void deleteExistingClocks() throws Exception {
		if (areListElementVisible(timeSheetDetailsClocks,10)) {
			for (WebElement clock : timeSheetDetailsClocks) {
				WebElement action = clock.findElement(By.xpath("./div[contains(@id,'action_popup_trigger')]"));
				click(action);
				click(timeSheetDetailsDeleteClock);
				WebElement timeSheetDetailsReasonSelect = clock.findElement(By.xpath("./following-sibling::div[1]//input-field[1]"));
				if (isElementEnabled(timeSheetDetailsReasonSelect, 10)) {
					click(timeSheetDetailsReasonSelect);
					click(timeSheetDetailsReasonSelect.findElements(By.cssSelector("option[label]")).get(0));
				}
				WebElement timeSheetDetailsCommentBox = clock.findElement(By.xpath("./following-sibling::div[1]//input-field[@id='id_comment_box']"));
				if (isElementEnabled(timeSheetDetailsCommentBox, 10) && !timeSheetDetailsCommentBox.getAttribute("class").contains("ng-hide")) {
					timeSheetDetailsCommentBox.sendKeys("Test");
				}
			}
		} else
			SimpleUtils.fail("Timesheet Page: Cannot find existing clocks",false);
	}

	@Override
	public boolean isTimeSheetConsoleMenuTabLoaded() throws Exception {
		if(isElementLoaded(timeSheetConsoleMenuDiv))
			return true;
		else
			return false;
	}

	@Override
	public void addTimeClockCheckInOutOnDetailWithDefaultValue(String location) throws Exception {
		String timeClockCheckOut = "11:30PM";
		if (isElementLoaded(scheduleDetails,10))
			timeClockCheckOut = scheduleDetails.getText().split("-")[1].trim();
		if (!isElementLoaded(timeSheetDetailsAddClock, 10)) {
			deleteExistingClocks();
			saveTimeSheetDetail();
		}
		if (isElementLoaded(timeSheetDetailsAddClock, 10)) {
			click(timeSheetDetailsAddClock);
			selectByVisibleText(timeSheetDetailsLocationSelect,location);
			click(timeSheetDetailsActionBtn);
			click(timeSheetDetailsAddClockAfter);
			WebElement selectClockType = timeSheetDetailsClocks.get(1).findElement(By.cssSelector("select"));
			selectByVisibleText(selectClockType,"Clock out");
			WebElement clockOutInput = timeSheetDetailsClocks.get(1).findElement(By.cssSelector("input-field[type=\"text\"] input"));
			click(clockOutInput);
			clockOutInput.sendKeys(timeClockCheckOut.split(":")[0]);
			clockOutInput.sendKeys(Keys.TAB);
			clockOutInput.sendKeys(timeClockCheckOut.split(":")[1].substring(0,2));
			clockOutInput.sendKeys(Keys.TAB);
			clockOutInput.sendKeys(timeClockCheckOut.split(":")[1].substring(2));
		}
	}

	@FindBy(css = "div.analytics-new-table")
	private WebElement analyticsTableInTimesheetDMViewPage;

	@Override
	public boolean isTimesheetDMView() throws Exception {
		boolean result = false;
		if (isElementLoaded(analyticsTableInTimesheetDMViewPage, 5)) {
			result = true;
		}
		return result;
	}

	@Override
	public List<String> getDataFromTimesheetTableForGivenLocationInDMView(String location) throws Exception {
		List<String> complianceViolationsOnDMViewSmartCard = new ArrayList<>();
		boolean isLocationFound = false;
		if (areListElementVisible(rowsInAnalyticsTable,10)) {
			for (WebElement row: rowsInAnalyticsTable) {
				if (row.findElement(By.xpath("./div[1]/span/img/following-sibling::span")).getText().equals(location)) {
					isLocationFound = true;
					List<WebElement> dataElements = row.findElements(By.cssSelector(".ng-scope.col-fx-1"));
					for (WebElement dataElement: dataElements) {
						if (!dataElement.getAttribute("class").contains("ng-hide") && dataElement.getText() != null)
							complianceViolationsOnDMViewSmartCard.add(dataElement.getText());
					}
					break;
				}
			}
		} else
			SimpleUtils.fail("Timesheet Page: There are no locations in current district or failed to load",false);
		if (isLocationFound)
			SimpleUtils.pass("Timesheet Page: Find the location " + location + " successfully");
		else
			SimpleUtils.fail("Timesheet Page: Failed to find the location, try another location again",false);
		return complianceViolationsOnDMViewSmartCard;
	}

	@FindBy(className = "lg-timesheet-table-improved__grid-column--entries-count")
	private List<WebElement> timeSheetDetailsEntriesCount;

	@Override
	public int getTotalTimesheetInSMView() throws Exception {
		int totalTimesheetInSMView = 0;
		if (areListElementVisible(timeSheetDetailsEntriesCount,10)) {
			for (WebElement e: timeSheetDetailsEntriesCount) {
				totalTimesheetInSMView += Integer.parseInt((e.getText().split(" "))[0]);
			}
		}
		return totalTimesheetInSMView;
	}

	@FindBy(css = ".card-carousel-card-primary .card-carousel-card-title")
	private WebElement timesheetApprovalRateSmartCardTitle;

	@FindBy (className = "analytics-new-smart-card-timesheet-approval-legend")
	private WebElement timesheetApprovalLegend;

	@FindBy (className = "analytics-new-smart-card-timesheet-approval-graph")
	private WebElement timesheetApprovalGraph;

	@FindBy (className = "analytics-new-smart-card-timesheet-approval-due-date")
	private WebElement timesheetApprovalDueDate;

	@Override
	public void validateTheContentOnTIMESHEETAPPROVALRATESmartCard(String dueDate) throws Exception {
    /*
    It should include:
    - Title: TIMESHEET APPROVAL RATE
    - Timesheet approval chart with <24 Hrs, 48 Hrs, > 48 Hrs and Unapproved
    - Timesheet due Dec. 21, which depends on configuration settings in Time and Attendance -> Timesheet -> Approval level, if it is 72, it will be due today +3 days
    */
		if(isElementLoaded(timesheetApprovalRateSmartCardTitle,5) && timesheetApprovalRateSmartCardTitle.getText().toUpperCase().equals("TIMESHEET APPROVAL RATE"))
			SimpleUtils.pass("Timesheet Page: The title of 'TIMESHEET APPROVAL RATE' smart card displays correctly");
		else
			SimpleUtils.fail("Timesheet Page: The title of 'TIMESHEET APPROVAL RATE' smart card failed to display or displays incorrectly",false);
		List<String> fieldNamesExpected = Arrays.asList(new String[]{"< 24 Hrs", "24-48 Hrs", "48+ Hrs", "Unapproved"});
		if(isElementLoaded(timesheetApprovalLegend,5) && isElementLoaded(timesheetApprovalGraph)) {
			List<WebElement> timesheetApprovalLegendItems = timesheetApprovalLegend.findElements(By.className("analytics-new-smart-card-timesheet-approval-legend-item"));
			boolean isMatched = false;
			for (WebElement item: timesheetApprovalLegendItems) {
				if (fieldNamesExpected.contains(item.getText().replace("\n", "")))
					isMatched = true;
				else
					break;
			}
			if (isMatched)
				SimpleUtils.pass("Timesheet Page: Timesheet approval chart with <24 Hrs, 48 Hrs, > 48 Hrs and Unapproved on smart card loaded successfully");
			else
				SimpleUtils.fail("Timesheet Page: Timesheet approval chart with <24 Hrs, 48 Hrs, > 48 Hrs and Unapproved failed to load",false);
		} else
			SimpleUtils.fail("Timesheet Page: Timesheet approval chart failed to load",false);
		if (isElementLoaded(timesheetApprovalDueDate,5) && timesheetApprovalDueDate.getText().replace(".","").contains(dueDate))
			SimpleUtils.pass("Timesheet Page: '" + timesheetApprovalDueDate.getText() + "' displays correctly");
		else
			SimpleUtils.fail("Timesheet Page: Timesheet due date on smart card failed to load or displays incorrectly",false);
	}

	@Override
	public void verifyPTOOfEmployee(String employee, String PTO) throws Exception {
		WebElement pto = getDriver().findElement(By.xpath("//*[contains(text(),'" + employee + "')]/parent::div/parent::div/following-sibling::div[9]/span"));
		if (pto.getText().equals(PTO)) {
			SimpleUtils.pass("PTO of " + employee + "' displays correctly");
		} else {
			SimpleUtils.fail("PTO of " + employee + "' does not displays correctly", false);
		}
	}
}