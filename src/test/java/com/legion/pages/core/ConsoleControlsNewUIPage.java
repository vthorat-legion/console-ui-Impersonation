package com.legion.pages.core;

import java.text.SimpleDateFormat;
import java.util.*;

import com.legion.pages.CinemarkMinorPage;
import com.legion.pages.OpsPortaPageFactories.ConfigurationPage;
import com.legion.pages.ShiftOperatePage;
import com.legion.pages.core.OpsPortal.OpsPortalConfigurationPage;
import com.legion.pages.core.schedule.ConsoleShiftOperatePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.legion.pages.BasePage;
import com.legion.pages.ControlsNewUIPage;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;

import static com.legion.utils.MyThreadLocal.*;


public class ConsoleControlsNewUIPage extends BasePage implements ControlsNewUIPage {

	public ConsoleControlsNewUIPage() {
		PageFactory.initElements(getDriver(), this);
	}


	@FindBy(css = "div.console-navigation-item-label.Controls")
	private WebElement controlsConsoleMenuDiv;

	@FindBy(css = "div.header-navigation-label")
	private WebElement controlsPageHeaderLabel;

	@FindBy(css = "lg-dashboard-card[title=\"Company Profile\"]")
	private WebElement companyProfileCard;

	@FindBy(css = "input[aria-label=\"Company Name\"]")
	private WebElement locationCompanyNameField;

	@FindBy(css = "lg-dashboard-card[title=\"Scheduling Policies\"]")
	private WebElement schedulingPoliciesCard;


	@FindBy(css = "input[aria-label=\"Business Address\"]")
	private WebElement locationBusinessAddressField;

	@FindBy(css = "input[aria-label=\"City\"]")
	private WebElement locationCityField;

	@FindBy(css = "select[aria-label=\"State\"]")
	private WebElement locationStateField;

	@FindBy(css = "select[aria-label=\"Province\"]")
	private WebElement locationProvinceField;

	@FindBy(css = "select[aria-label=\"Country\"]")
	private WebElement locationCountryField;

	@FindBy(css = "input[aria-label=\"Zip Code\"]")
	private WebElement locationZipCodeField;

	@FindBy(css = "input[aria-label=\"Postal Code\"]")
	private WebElement locationPostalCodeField;

	@FindBy(css = "select[aria-label=\"Time Zone\"]")
	private WebElement locationTimeZoneField;

	@FindBy(css = "input[aria-label=\"Website\"]")
	private WebElement locationWebsiteField;


	@FindBy(css = "input[aria-label=\"First Name\"]")
	private WebElement locationFirstNameField;

	@FindBy(css = "input[aria-label=\"Last Name\"]")
	private WebElement locationLastNameField;

	@FindBy(css = "input[aria-label=\"E-mail\"]")
	private WebElement locationEmailField;

	@FindBy(css = "input[aria-label=\"Phone\"]")
	private WebElement locationPhoneField;

	@FindBy(css = "lg-button[label=\"Save\"]")
	private List<WebElement> locationProfileSaveBtn;

	@FindBy(css = "lg-dashboard-card[title=\"Working Hours\"]")
	private WebElement workingHoursCard;

	@FindBy(css = "collapsible[block-title=\"'Regular'\"]")
	private WebElement regularHoursBlock;

	@FindBy(css = "collapsible[block-title=\"'Holiday'\"]")
	private WebElement holidayHoursBlock;

	@FindBy(css = "day-working-hours[id=\"day.dayOfTheWeek\"]")
	private List<WebElement> regularHoursRows;

	@FindBy(css = "day-working-hours[id=\"day.name\"]")
	private List<WebElement> holidayHoursRows;


	@FindBy(css = "input-field[type=\"checkbox\"]")
	private List<WebElement> regularHoursDaysCheckboxs;

	@FindBy(css = "div.lgn-time-slider-post")
	private List<WebElement> editRegularHoursSliders;

	@FindBy(css = "lg-button[ng-click=\"saveWorkingHours()\"]")
	private WebElement saveWorkersHoursBtn;

	@FindBy(css = "lg-button[ng-click=\"saveWorkdayHours()\"]")
	private WebElement saveAllRegularHoursBtn;

	@FindBy(css = "form-section[form-title=\"Budget\"]")
	private WebElement budgetFormSection;

	@FindBy(css = "input-field[origin=\"schedulingWindow\"]")
	private WebElement schedulingWindowAdvanceWeekCount;

	@FindBy(css = "div.lg-advanced-box")
	private List<WebElement> controlsAdvanceButtons;

	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.openingBufferHours\"]")
	private WebElement openingBufferHours;

	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.closingBufferHours\"]")
	private WebElement closingBufferHours;


	@FindBy(css = "lg-dashboard-card[title=\"Location Profile\"]")
	private WebElement locationProfileSection;

	@FindBy(css = "lg-dashboard-card[title=\"Schedule Collaboration\"]")
	private WebElement scheduleCollaborationSection;

	@FindBy(css = "lg-dashboard-card[title=\"Compliance\"]")
	private WebElement complianceSection;

	@FindBy(css = "lg-dashboard-card[title=\"Users and Roles\"]")
	private WebElement usersAndRolesSection;

	@FindBy(css = "lg-dashboard-card[title=\"Tasks and Work Roles\"]")
	private WebElement tasksAndWorkRolesSection;

	@FindBy(css = "lg-dashboard-card[title=\"Operating Hours\"]")
	private WebElement operatingHoursSection;

	@FindBy(css = "lg-dashboard-card[title=\"Locations\"]")
	private WebElement locationsSection;

	@FindBy(css = "page-heading[page-title=\"Location Details\"]")
	private WebElement breadcrumbsLocationDetails;

	@FindBy(css = "page-heading[page-title=\"Scheduling Policies\"]")
	private WebElement breadcrumbsSchedulingPolicies;

	@FindBy(css = "page-heading[page-title=\"Schedule Collaboration\"]")
	private WebElement breadcrumbsScheduleCollaboration;

	@FindBy(css = "page-heading[page-title=\"Compliance\"]")
	private WebElement breadcrumbsCompliance;

	@FindBy(css = "page-heading[page-title=\"Users and Roles\"]")
	private WebElement breadcrumbsUsersAndRoles;

	@FindBy(css = "page-heading[page-title=\"Tasks and Work Roles\"]")
	private WebElement breadcrumbsTasksAndWorkRoles;

	@FindBy(css = "page-heading[page-title=\"Working Hours\"]")
	private WebElement breadcrumbsWorkingHours;

	@FindBy(css = "lg-dashboard-card[title=\"Time and Attendance\"]")
	private WebElement timeAndAttendanceCard;

	@FindBy(css = "div.lg-advanced-box__toggle")
	private WebElement timeSheetAdvanceBtn;

	@FindBy(css = "question-input[question-title=\"Timesheet export format\"]")
	private WebElement timeSheetExportFormatDiv;

	@FindBy(css = "question-input[question-title*=\"Timesheet approval is due for Manager\"]")
	private WebElement timeSheetApprovalDueForManagerDiv;

	@FindBy(css = "question-input[question-title*=\"Timesheet approval is due for Payroll\"]")
	private WebElement timeSheetApprovalDueForPayrollAdminDiv;

	@FindBy(css = "form-section[form-title=\"Shifts\"]")
	private WebElement schedulingPoliciesShiftFormSectionDiv;

	@FindBy(css = "form-section[form-title=\"Schedules\"]")
	private WebElement schedulingPoliciesSchedulesFormSectionDiv;

	@FindBy(css = "input-field[value=\"sp.enterprisePreference.shiftIntervalMins\"]")
	private WebElement schedulingPoliciesShiftIntervalDiv;

	@FindBy(css = "lg-icon-button[type=\"confirm\"]")
	private WebElement confirmSaveButton;
	@FindBy(css = "lg-icon-button[type=\"cancel\"]")
	private WebElement cancelSaveButton;
	@FindBy(css = "input-field[placeholder=\"Global\"]")
	private WebElement globalLocationButton;
	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.publishDayWindowWeek\"]")
	private WebElement schedulePublishWindowDiv;
	@FindBy(css = "[question-title=\"How many days in advance would you finalize schedule?\"] div.lg-question-input")
	private WebElement advanceFinalizeDaysDiv;
	@FindBy(css = "lg-select[search-hint=\"Search Location\"]")
	private WebElement SchedulingPoliciesActiveLocation;
	@FindBy(css = "input-field[origin=\"schedulingWindow\"]")
	private WebElement schedulingWindow;
	@FindBy(css = "input-field[value=\"sp.enterprisePreference.firstDayOfWeek\"]")
	private WebElement schedulingPoliciesFirstDayOfWeek;
	@FindBy(css = "input-field[value=\"sp.enterprisePreference.minBusinessMinutes\"]")
	private WebElement earliestOpeningTimeDiv;
	@FindBy(css = "input-field[value=\"sp.enterprisePreference.maxBusinessMinutes\"]")
	private WebElement latestClosingTimeDiv;
	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.minShiftLengthHour\"]")
	private WebElement minShiftLengthHourDiv;
	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.maxShiftLengthHour\"]")
	private WebElement maxShiftLengthHourDiv;
	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.maxWorkingDaysBestPractice\"]")
	private WebElement maxWorkingDaysBestPracticeInputField;
	@FindBy(css = "input-field[value=\"sp.hourlyRatePolicy.displayBreakTimeInShift\"]")
	private WebElement displayBreakTimeInShiftInputField;
	@FindBy(css = "[question-title*=\"labor budget \"] yes-no")
	private WebElement applyLaborBudgetToSchedules;
	@FindBy(css = "input-field[value=\"sp.budgetPreferences.budgetType\"]")
	private WebElement scheduleBudgetTypeElement;
	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.availabilityLockMode\"]")
	private WebElement availabilityLockModeDiv;
	@FindBy(css = "input-field[value=\"sp.hourlyRatePolicy.scheduleUnavailableHourWorkers\"]")
	private WebElement scheduleUnavailableHourWorkersDiv;
	@FindBy(css = "input-field[value=\"sp.weeklySchedulePreference.availabilityTolerance\"]")
	private WebElement availabilityToleranceField;
	@FindBy(css = "yes-no[value=\"sp.teamPreferences.canWorkerRequestTimeOff\"]")
	private WebElement canWorkerRequestTimeOffBtnGroup;
	@FindBy(css = "input-field[value=\"sp.teamPreferences.maxWorkersOnTimeOffPerDay\"]")
	private WebElement maxWorkersOnTimeOffPerDayField;
	@FindBy(css = "form-section[form-title=\"Time off\"]")
	private WebElement schedulingPoliciesTimeOffFormSectionDiv;
	@FindBy(css = "input-field[value=\"sp.teamPreferences.noticePeriodToRequestTimeOff\"]")
	private WebElement noticePeriodToRequestTimeOffField;
	@FindBy(css = "yes-no[value=\"sp.teamPreferences.showTimeOffReasons\"]")
	private WebElement showTimeOffReasonsBtnGroup;
	@FindBy(css = "lg-button[label=\"Preserve\"]")
	private WebElement preserveSettingBtn;
	@FindBy(css = "lg-button[label=\"Overwrite\"]")
	private WebElement overwriteSettingBtn;

	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.numHoursPerWeekMin\"]")
	private List<WebElement> numHoursPerWeekMinInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.numHoursPerWeek\"]")
	private List<WebElement> numHoursPerWeekMaxInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.numHoursPerWeekIdeal\"]")
	private List<WebElement> numHoursPerWeekIdealInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.numShiftsPerWeekMin\"]")
	private List<WebElement> numShiftsPerWeekMinInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.numShiftsPerWeek\"]")
	private List<WebElement> numShiftsPerWeekMaxInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.numShiftsIdeal\"]")
	private List<WebElement> numShiftsPerWeekIdealInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.shiftMinLength\"]")
	private List<WebElement> minHoursPerShiftInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.shiftMaxLength\"]")
	private List<WebElement> maxHoursPerShiftInputFields;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.shiftIdealLength\"]")
	private List<WebElement> ideaHoursPerShiftInputFields;
	@FindBy(css = "form-section[form-title=\"Scheduling Policy Groups\"]")
	private WebElement schedulePolicyGroupSection;
	@FindBy(css = "input-field[value=\"$ctrl.engagementGroup.committedHoursPeriod\"]")
	private List<WebElement> committedHoursPeriodFileds;
	@FindBy(css = "div[ng-click=\"$ctrl.select(tab)\"]")
	private List<WebElement> schedulingPolicyGroupsTabs;
	@FindBy(css = "div.lg-question-input__wrapper.bg.budget_score > input-field > ng-form > div")
	private WebElement budgetScoreInputField;
	@FindBy(css = "input-field[value=\"$ctrl.scheduleScoreWeight.coverageScoreWeightRegular\"]")
	private WebElement coverageScoreWeightRegularInputField;
	@FindBy(css = "input-field[value=\"$ctrl.scheduleScoreWeight.coverageScoreWeightPeak\"]")
	private WebElement coverageScoreWeightPeakInputField;
	@FindBy(css = "input-field[value=\"$ctrl.scheduleScoreWeight.employeeMatchScoreWeight\"]")
	private WebElement employeeMatchScoreWeightInputField;
	@FindBy(css = "input-field[value=\"$ctrl.scheduleScoreWeight.complianceScoreWeight\"]")
	private WebElement complianceScoreWeightInputField;

	//added by Nishant

	@FindBy(css = "input[placeholder*='Select']")
	private WebElement linkAllLocations;

	@FindBy(xpath = "//div[contains(text(),'All Locations')]")
	private WebElement allLocations;

	@FindBy(css = "input[placeholder='Search Location']")
	private WebElement searchLocation;

	@FindBy(css = "div.lg-search-options__thumbnail")
	private List<WebElement> imageIcon;

	@FindBy(css = "div.lg-search-options__option")
	private List<WebElement> locationName;

	//added by Nishant

	@FindBy(xpath = "//yes-no[@value='sp.weeklySchedulePreference.canOverrideAssignmentRule']")
	private WebElement btnOverrideAssignmentRule;

	@FindBy(css = "yes-no[value='sp.weeklySchedulePreference.canOverrideAssignmentRule'] div.lg-button-group-first")
	private WebElement btnOverrideAssignmentRuleYes;

	@FindBy(css = "yes-no[value='sp.weeklySchedulePreference.canOverrideAssignmentRule'] div.lg-button-group-last")
	private WebElement btnOverrideAssignmentRuleNo;

	//Added by Estelle

	@FindBy(css = "question-input[question-title=\"Is approval by Manager required when an employee claims an Open Shift?\"]")
	private WebElement isApprovedByManagerWhileClaimOpenShift;


	public enum MealBreakDuration {

		Minute5("5 minute"),
		Minute10("10 minute"),
		Minute15("15 minute"),
		Minute20("20 minute"),
		Minute25("25 minute"),
		Minute30("30 minute"),
		Minute35("35 minute"),
		Minute40("40 minute"),
		Minute45("45 minute"),
		Minute50("50 minute"),
		Minute55("55 minute"),
		Minute60("60 minute");

		private final String value;

		MealBreakDuration(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	public enum MealBreakPaidType {

		Paid("paid"),
		Unpaid("unpaid");

		private final String value;

		MealBreakPaidType(final String newValue) {
			value = newValue;
		}

		public String getValue() {
			return value;
		}
	}

	String timeSheetHeaderLabel = "Controls";

	@Override
	public void clickOnControlsConsoleMenu() throws Exception {
		if (isElementLoaded(controlsConsoleMenuDiv, 10))
			clickTheElement(controlsConsoleMenuDiv);
		else
			SimpleUtils.fail("Controls Console Menu not loaded Successfully!", false);
	}


	@Override
	public boolean isControlsConsoleMenuAvailable() throws Exception {
		if (isElementLoaded(controlsConsoleMenuDiv, 10))
			return true;
		else
			return false;
	}

	@Override
	public boolean isControlsPageLoaded() throws Exception {
		return true;
	}


	@Override
	public void clickOnGlobalLocationButton() throws Exception {
		// Do nothing
	}


	@Override
	public void clickOnControlsCompanyProfileCard() throws Exception {
		if (isElementLoaded(companyProfileCard, 10))
			click(companyProfileCard);
		else
			SimpleUtils.fail("Controls Page: Company Profile Card not Loaded!", false);
	}


	@Override
	public void updateUserLocationProfile(String companyName, String businessAddress, String city, String state,
										  String country, String zipCode, String timeZone, String website, String firstName, String lastName,
										  String email, String phone) throws Exception {

		locationCompanyNameField.clear();
		locationCompanyNameField.sendKeys(companyName);

		locationBusinessAddressField.clear();
		locationBusinessAddressField.sendKeys(businessAddress);

		locationCityField.clear();
		locationCityField.sendKeys(city);

		Select drpCountry = new Select(locationCountryField);
		drpCountry.selectByVisibleText(country);

		try {
			if (isElementEnabled(locationStateField, 5)) {
				Select drpStates = new Select(locationStateField);
				drpStates.selectByVisibleText(state);
			}
		} catch (Exception e) {
			Select drpStates = new Select(locationProvinceField);
			drpStates.selectByVisibleText(state);
		}

		try {
			locationZipCodeField.clear();
			locationZipCodeField.sendKeys(zipCode);
		} catch (Exception e) {
			locationPostalCodeField.clear();
			locationPostalCodeField.sendKeys(zipCode);
		}

		Select drpTimeZone = new Select(locationTimeZoneField);
		drpTimeZone.selectByVisibleText(timeZone);

		locationWebsiteField.clear();
		locationWebsiteField.sendKeys(website);

		locationFirstNameField.clear();
		locationFirstNameField.sendKeys(firstName);

		locationLastNameField.clear();
		locationLastNameField.sendKeys(lastName);

		locationEmailField.clear();
		locationEmailField.sendKeys(email);

		locationPhoneField.clear();
		locationPhoneField.sendKeys(phone);

		if (locationProfileSaveBtn.size() > 0)
			click(locationProfileSaveBtn.get(0));
		else
			SimpleUtils.fail("Controls Page: Location Profile Save button not loaded.", false);

	}

	@Override
	public boolean isUserLocationProfileUpdated(String companyName, String businessAddress, String city, String state,
												String country, String zipCode, String timeZone, String website, String firstName, String lastName,
												String email, String phone) throws Exception {
		boolean isProfileValueUpdated = true;

		if (!locationCompanyNameField.getAttribute("value").contains(companyName)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'Company Name' not updated.", true);
		}

		if (!locationBusinessAddressField.getAttribute("value").contains(businessAddress)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'Business Address' not updated.", true);
		}


		if (!locationCityField.getAttribute("value").contains(city)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'City' not updated.", true);
		}

		try {
			Select drpStates = new Select(locationStateField);
			if (!drpStates.getFirstSelectedOption().getText().contains(state)) {
				isProfileValueUpdated = false;
				SimpleUtils.fail("Controls Page: Updating Location Profile - 'State' not updated.", true);
			}
		} catch (Exception e) {
			Select drpStates = new Select(locationProvinceField);
			if (!drpStates.getFirstSelectedOption().getText().contains(state)) {
				isProfileValueUpdated = false;
				SimpleUtils.fail("Controls Page: Updating Location Profile - 'Province' not updated.", true);
			}
		}

		try {
			if (!locationZipCodeField.getAttribute("value").contains(zipCode)) {
				isProfileValueUpdated = false;
				SimpleUtils.fail("Controls Page: Updating Location Profile - 'Zip Code' not updated.", true);
			}
		} catch (Exception e) {
			if (!locationPostalCodeField.getAttribute("value").contains(zipCode)) {
				isProfileValueUpdated = false;
				SimpleUtils.fail("Controls Page: Updating Location Profile - 'Postal Code' not updated.", true);
			}
		}

		Select drpTimeZone = new Select(locationTimeZoneField);
		if (!drpTimeZone.getFirstSelectedOption().getText().contains(timeZone)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'Time Zone' not updated.", true);
		}

		if (!locationWebsiteField.getAttribute("value").contains(website)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'Website' not updated.", true);
		}

		if (!locationFirstNameField.getAttribute("value").contains(firstName)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'First Name' not updated.", true);
		}

		if (!locationLastNameField.getAttribute("value").contains(lastName)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'Last Name' not updated.", true);
		}

		if (!locationEmailField.getAttribute("value").contains(email)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'E-Mail' not updated.", true);
		}

		if (!locationPhoneField.getAttribute("value").contains(phone)) {
			isProfileValueUpdated = false;
			SimpleUtils.fail("Controls Page: Updating Location Profile - 'Phone' not updated.", true);
		}

		return isProfileValueUpdated;
	}

	@Override
	public void clickOnControlsWorkingHoursCard() throws Exception {
		if (isElementLoaded(workingHoursCard))
			click(workingHoursCard);
		else
			SimpleUtils.fail("Controls Page: Working Hours Card not Loaded!", false);
	}

	@FindBy(css = "div.lgn-time-slider-notch-label")
	private List<WebElement> sliderNotchLabel;

	@FindBy(css = ".each-day-selector input-field")
	private List<WebElement> applyOtherDays;
	@Override
	public void updateControlsRegularHours(String openingHours, String closingHours, String day, List<String> applyToOtherDays) throws Exception {
		openingHours = openingHours.replace(" ", "");
		closingHours = closingHours.replace(" ", "");
		WebElement collapsibleHeader = regularHoursBlock.findElement(By.cssSelector("div.collapsible.row"));
		boolean isRegularHoursSectionOpened = collapsibleHeader.getAttribute("class").contains("open");

		if (!isRegularHoursSectionOpened)
			click(regularHoursBlock);

		if (areListElementVisible(regularHoursRows, 10)) {
			ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
			for (WebElement regularHoursRow : regularHoursRows) {
				if (regularHoursRow.getText().toLowerCase().contains(day.toLowerCase())) {
					if (regularHoursRow.findElement(By.cssSelector("label[class=\"switch\"]")).getAttribute("class").contains("ng-empty")) {
						clickTheElement(regularHoursRow.findElement(By.cssSelector("label[class=\"switch\"]")));
					}
					WebElement regularHoursEditBtn = regularHoursRow.findElement(By.cssSelector("lg-button[label=\"Edit\"]"));
					if (isElementLoaded(regularHoursEditBtn)) {
						click(regularHoursEditBtn);
						// Select Opening and closing Hours
						shiftOperatePage.moveSliderAtCertainPointOnEditShiftTimePage(closingHours, "End");
						shiftOperatePage.moveSliderAtCertainPointOnEditShiftTimePage(openingHours, "Start");
						//Apply to other days
						if (applyToOtherDays.size() != 0) {
							if (areListElementVisible(applyOtherDays, 5) && applyOtherDays.size() >= 6) {
								for (WebElement otherDay: applyOtherDays) {
									if (applyToOtherDays.contains(otherDay.getText())) {
										clickTheElement(otherDay.findElement(By.tagName("input")));
									}
								}
							} else
								SimpleUtils.fail("Apply to other days fail to load! ", false);
						}

						//Save the change
						if (isElementLoaded(saveWorkersHoursBtn)) {
							click(saveWorkersHoursBtn);
							SimpleUtils.pass("Controls Working Hours Section: Regular Hours Updated for the day ('" + day + "'). ");
						} else
							SimpleUtils.fail("Controls Working Hours Section: Editing Regular Hours 'Save' Button not loaded.", true);
					} else {
						SimpleUtils.fail("Controls Working Hours Section: Regular Hours 'Edit' Button not loaded.", true);
					}
					break;
				}

			}
		} else {
			SimpleUtils.fail("Controls Working Hours Section: Regular Hours not loaded.", true);
		}
	}


	public void moveDayViewCards(WebElement webElement, int xOffSet) {
		Actions builder = new Actions(MyThreadLocal.getDriver());
		builder.moveToElement(webElement)
				.clickAndHold()
				.moveByOffset(xOffSet, 0)
				.release()
				.build()
				.perform();
	}


	@Override
	public void clickOnSaveRegularHoursBtn() throws Exception {
		if (isElementLoaded(saveAllRegularHoursBtn)) {
			click(saveAllRegularHoursBtn);
			displaySuccessMessage();
			SimpleUtils.pass("Controls Working Hours Section: Regular Hours Saved successfully. ");
		} else {
			SimpleUtils.fail("Controls Working Hours Section: Regular Hours 'Save' Button not loaded.", true);
		}
	}


	@Override
	public void clickOnControlsSchedulingPolicies() throws Exception {
		if (isElementLoaded(schedulingPoliciesCard))
			click(schedulingPoliciesCard);
		else
			SimpleUtils.fail("Controls Page: Schedule Policies Card not Loaded!", false);
	}


	@FindBy(css = "div.lg-toast")
	private WebElement successMsg;

	@Override
	public void enableDisableBudgetSmartcard(boolean enable) throws Exception {
		WebElement enableBudgetYesBtn = budgetFormSection.findElement(By.cssSelector("div.lg-button-group-first"));
		WebElement enableBudgetNoBtn = budgetFormSection.findElement(By.cssSelector("div.lg-button-group-last"));


		if (enable && isBudgetSmartcardEnabled())
			SimpleUtils.pass("Schedule Policies Budget card already enabled.");
		else if (enable && !isBudgetSmartcardEnabled()) {
			click(enableBudgetYesBtn);
			SimpleUtils.pass("Schedule Policies Budget card enabled successfully.");
			displaySuccessMessage();
		} else if (!enable && isBudgetSmartcardEnabled()) {
			scrollToElement(enableBudgetNoBtn);
			click(enableBudgetNoBtn);
			SimpleUtils.pass("Schedule Policies Budget card disabled successfully.");
			displaySuccessMessage();
		} else
			SimpleUtils.pass("Schedule Policies Budget card already disabled.");
	}

	@Override
	public boolean isBudgetSmartcardEnabled() throws Exception {
		if (isElementLoaded(budgetFormSection)) {
			WebElement enableBudgetYesBtn = budgetFormSection.findElement(By.cssSelector("div.lg-button-group-first"));
			if (isElementLoaded(enableBudgetYesBtn)) {
				if (enableBudgetYesBtn.getAttribute("class").contains("selected"))
					return true;
			} else
				SimpleUtils.fail("Controls Page: Schedule Policies Budget form section 'Yes' button not loaded!", false);
		} else
			SimpleUtils.fail("Controls Page: Schedule Policies Budget form section not loaded!", false);
		return false;
	}


	@Override
	public String getAdvanceScheduleWeekCountToCreate() throws Exception {
		String selectedWeek = "";
		if (isElementLoaded(schedulingWindowAdvanceWeekCount)) {
			WebElement advanceScheduleWeekSelectBox = schedulingWindowAdvanceWeekCount.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			Select drpScheduleWeek = new Select(advanceScheduleWeekSelectBox);
			selectedWeek = drpScheduleWeek.getFirstSelectedOption().getText();
		} else
			SimpleUtils.fail("Controls Page: Schedule Policies Advance Schedule weeks not loaded.", false);

		return selectedWeek;
	}


	@Override
	public void updateAdvanceScheduleWeekCountToCreate(String scheduleWeekCoundToCreate) throws Exception {
		if (getAdvanceScheduleWeekCountToCreate().toLowerCase().contains(scheduleWeekCoundToCreate.toLowerCase()))
			SimpleUtils.pass("Controls Page: Schedule Policies Advance Schedule weeks value '" + scheduleWeekCoundToCreate + " weeks' already Updated.");
		else {
			WebElement advanceScheduleWeekSelectBox = schedulingWindowAdvanceWeekCount.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			Select drpScheduleWeek = new Select(advanceScheduleWeekSelectBox);
			drpScheduleWeek.selectByVisibleText(scheduleWeekCoundToCreate);
			if (getAdvanceScheduleWeekCountToCreate().toLowerCase().contains(scheduleWeekCoundToCreate.toLowerCase()))
				SimpleUtils.pass("Controls Page: Schedule Policies Advance Schedule weeks value '" + scheduleWeekCoundToCreate + "' Updated successfully.");
			else
				SimpleUtils.fail("Controls Page: Unable to update Schedule Policies Advance Schedule weeks value.", false);
		}
	}

	@Override
	public HashMap<String, Integer> getScheduleBufferHours() throws Exception {
		HashMap<String, Integer> bufferHours = new HashMap<String, Integer>();
		waitForSeconds(8);
		if (controlsAdvanceButtons.size() > 0) {
			click(controlsAdvanceButtons.get(0));
			waitForSeconds(2);
			bufferHours.put("openingBufferHours", Integer.valueOf(
					openingBufferHours.findElement(By.cssSelector("input[type=\"number\"]")).getAttribute("value")));
			bufferHours.put("closingBufferHours", Integer.valueOf(
					closingBufferHours.findElement(By.cssSelector("input[type=\"number\"]")).getAttribute("value")));
		}
		return bufferHours;
	}


	@Override
	public void clickOnControlsLocationProfileSection() throws Exception {
		if (isElementLoaded(locationProfileSection))
			click(locationProfileSection);
		else
			SimpleUtils.fail("Controls Page: Location Profile Card not Loaded!", false);
	}


	@Override
	public void clickOnControlsScheduleCollaborationSection() throws Exception {
		if (isElementLoaded(scheduleCollaborationSection))
			click(scheduleCollaborationSection);
		else
			SimpleUtils.fail("Controls Page: Schedule Collaboration Card not Loaded!", false);
	}

	@Override
	public void clickOnControlsComplianceSection() throws Exception {
		if (isElementLoaded(complianceSection, 5))
			click(complianceSection);
		else
			SimpleUtils.fail("Controls Page: Compliance Card not Loaded!", false);
	}

	@FindBy(css = "[form-title=\"Split Shift\"]")
	private WebElement splitShiftSection;
	@FindBy(css = "[form-title=\"Split Shift\"] [label=\"Edit\"]")
	private WebElement splitShiftEditBtn;
	@FindBy(css = ".modal-dialog")
	private WebElement splitShiftDialog;
	@Override
	public void turnOnOrTurnOffSplitShiftToggle(boolean action) throws Exception {
		String content = getSplitShiftContent();
		if (isElementLoaded(splitShiftSection, 10)
				&&splitShiftSection.findElement(By.cssSelector(".info")).getText().equalsIgnoreCase("Split Shift")
				&&content.contains("An employee will receive a ")
				&&content.contains(" hour premium for shifts worked with more than ")
				&&content.contains(" minutes between last clock out and next clock in.")){
			if (isElementLoaded(splitShiftSection.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
				if (action && splitShiftSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
					scrollToElement(splitShiftSection.findElement(By.cssSelector(".lg-question-input__toggle")));
					clickTheElement(splitShiftSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					SimpleUtils.pass("Toggle is turned on!");
				} else if (!action){
					clickTheElement(splitShiftSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					SimpleUtils.pass("Toggle is turned off!");
					clickTheElement(splitShiftEditBtn);
					if (isElementLoaded(splitShiftDialog, 5)){
						SimpleUtils.fail("Edit button should be disabled when toggle off!", false);
					}
				} else {
					SimpleUtils.pass("Toggle status is expected!");
				}
			} else {
				SimpleUtils.fail("Toggle fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Split Shift section fail to load!", false);
		}
	}

	public String getSplitShiftContent() throws Exception{
		if (isElementLoaded(splitShiftSection, 10)){
			return splitShiftSection.findElement(By.cssSelector(".lg-question-input__text")).getAttribute("innerText");
		}
		return "";
	}

	@Override
	public void editSplitShiftPremium(String numOfPremiumHrs, String greaterThan, boolean saveOrNot) throws Exception {
		if (isElementLoaded(splitShiftEditBtn, 5)){
			String contentBefore = getSplitShiftContent();
			clickTheElement(splitShiftEditBtn);
			if (isElementLoaded(splitShiftDialog, 10)){
				//check the title.
				if (splitShiftDialog.findElement(By.cssSelector(".lg-modal__title")).getText().trim().equalsIgnoreCase("Edit Split Shift Premium")){
					SimpleUtils.pass("Dialog title is expected!");
				} else {
					SimpleUtils.fail("Dialog title is not correct", false);
				}
				//Check setting content.
				if(splitShiftDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("An employee will receive a")
						&&splitShiftDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("hour premium for shifts worked with more than")
						&&splitShiftDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("minutes between last clock out and next clock in.")){
					SimpleUtils.pass("Setting content in the dialog is expected!");
				} else {
					SimpleUtils.fail("Setting content is not expected!", false);
				}
				//edit the content, input the parameters.
				if (splitShiftDialog.findElements(By.cssSelector("input")).size() == 2){
					splitShiftDialog.findElements(By.cssSelector("input")).get(0).clear();
					splitShiftDialog.findElements(By.cssSelector("input")).get(0).sendKeys(numOfPremiumHrs);
					splitShiftDialog.findElements(By.cssSelector("input")).get(1).clear();
					splitShiftDialog.findElements(By.cssSelector("input")).get(1).sendKeys(greaterThan);
				} else {
					SimpleUtils.fail("inputs are not shown as expected!", false);
				}
				//save or cancel.
				if (isElementLoaded(splitShiftDialog.findElement(By.cssSelector("[label=\"Cancel\"]")), 5)
					&&isElementLoaded(splitShiftDialog.findElement(By.cssSelector("[label=\"Save\"]")), 5)){
					if (saveOrNot){
						clickTheElement(splitShiftDialog.findElement(By.cssSelector("[label=\"Save\"] button")));
						SimpleUtils.assertOnFail("Setting is not saved successfully!", getSplitShiftContent().contains(numOfPremiumHrs)&&getSplitShiftContent().contains(greaterThan), false);
					} else {
						clickTheElement(splitShiftDialog.findElement(By.cssSelector("[label=\"Cancel\"] button")));
						waitForSeconds(2);
						SimpleUtils.assertOnFail("Setting should the same as before!", contentBefore.equalsIgnoreCase(getSplitShiftContent()), false);
					}
				} else {
					SimpleUtils.fail("Save or Cancel button fail to load!", false);
				}
			} else {
				SimpleUtils.fail("Split shift dialog fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Edit split shift button is not loaded!", false);
		}
	}

	@Override
	public boolean isCompliancePageLoaded() throws Exception {
		if (isElementLoaded(overtimeWeeklyText, 15)) {
			SimpleUtils.pass("Compliance page loaded successfully");
			return true;
		} else {
			SimpleUtils.fail("Compliance not Loaded", false);
			return false;
		}
	}

	@Override
	public void clickOnControlsUsersAndRolesSection() throws Exception {
		if (isElementLoaded(usersAndRolesSection, 15))
			click(usersAndRolesSection);
		else
			SimpleUtils.fail("Controls Page: Users and Roles Card not Loaded!", false);
	}

	@Override
	public void clickOnControlsTasksAndWorkRolesSection() throws Exception {
		if (isElementLoaded(tasksAndWorkRolesSection))
			click(tasksAndWorkRolesSection);
		else
			SimpleUtils.fail("Controls Page: tasksAndWorkRolesSection Card not Loaded!", false);
	}


	@Override
	public void clickOnControlsOperatingHoursSection() throws Exception {
		if (isElementLoaded(operatingHoursSection))
			click(operatingHoursSection);
		else
			SimpleUtils.fail("Controls Page: Operating Hours Card not Loaded!", false);
	}


	@Override
	public boolean isControlsLocationProfileLoaded() throws Exception {
		if (isElementLoaded(breadcrumbsLocationDetails)) {
			SimpleUtils.pass("Controls Page: Location Profile Section Loaded Successfully.");
			return true;
		}
		return false;
	}


	@Override
	public boolean isControlsSchedulingPoliciesLoaded() throws Exception {
		HashMap<String, String> templateTypeAndName = MyThreadLocal.getTemplateTypeAndName();
		CinemarkMinorPage cinemarkMinorPage = new ConsoleCinemarkMinorPage();
		ConfigurationPage configurationPage = new OpsPortalConfigurationPage();
		cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Scheduling Policies"));
		configurationPage.clickOnEditButtonOnTemplateDetailsPage();
		return true;
	}

	@Override
	public boolean isControlsScheduleCollaborationLoaded() throws Exception {
		HashMap<String, String> templateTypeAndName = MyThreadLocal.getTemplateTypeAndName();
		CinemarkMinorPage cinemarkMinorPage = new ConsoleCinemarkMinorPage();
		ConfigurationPage configurationPage = new OpsPortalConfigurationPage();
		cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Schedule Collaboration"));
		configurationPage.clickOnEditButtonOnTemplateDetailsPage();
		return true;
	}


	@Override
	public boolean isControlsComplianceLoaded() throws Exception {
		HashMap<String, String> templateTypeAndName = MyThreadLocal.getTemplateTypeAndName();
		CinemarkMinorPage cinemarkMinorPage = new ConsoleCinemarkMinorPage();
		ConfigurationPage configurationPage = new OpsPortalConfigurationPage();
		cinemarkMinorPage.findDefaultTemplate(templateTypeAndName.get("Compliance"));
		configurationPage.clickOnEditButtonOnTemplateDetailsPage();
		return false;
	}


	@Override
	public boolean isControlsUsersAndRolesLoaded() throws Exception {
		if (isElementLoaded(breadcrumbsUsersAndRoles)) {
			SimpleUtils.pass("Controls Page: Users and Roles Section Loaded Successfully.");
			return true;
		}
		return false;
	}

	@Override
	public boolean isControlsTasksAndWorkRolesLoaded() throws Exception {
		if (isElementLoaded(breadcrumbsTasksAndWorkRoles)) {
			SimpleUtils.pass("Controls Page: Tasks and Work Roles Section Loaded Successfully.");
			return true;
		}
		return false;
	}

	@Override
	public boolean isControlsWorkingHoursLoaded() throws Exception {
		if (isElementLoaded(breadcrumbsWorkingHours)) {
			SimpleUtils.pass("Controls Page: Working Hours Section Loaded Successfully.");
			return true;
		}
		return false;
	}


	@Override
	public void clickOnControlsTimeAndAttendanceCard() throws Exception {
		if (isElementLoaded(timeAndAttendanceCard)) {
			click(timeAndAttendanceCard);
			SimpleUtils.pass("Controls Page: 'Time and Attendance' tab selected successfully.");
		} else
			SimpleUtils.fail("Controls Page: 'Time and Attendance' tab not loaded.", false);
	}

	@Override
	public void clickOnControlsTimeAndAttendanceAdvanceBtn() throws Exception {
		if (isElementLoaded(timeSheetAdvanceBtn) && !timeSheetAdvanceBtn.getAttribute("class").contains("--advanced"))
			click(timeSheetAdvanceBtn);
		else
			SimpleUtils.fail("Controls - Time and Attendance section: 'Advance' button not loaded.", false);
	}

	@Override
	public void selectTimeSheetExportFormatByLabel(String optionLabel) throws Exception {
		if (isElementLoaded(timeSheetExportFormatDiv)) {
			WebElement timeSheetFormatDropDown = timeSheetExportFormatDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(timeSheetFormatDropDown)) {
				Select dropdown = new Select(timeSheetFormatDropDown);
				if (getTimeSheetExportFormatSelectedOption().contains(optionLabel)) {
					SimpleUtils.pass("Time and Attendance: Timesheet export format '" + optionLabel + "' option already selected.");
				} else {
					dropdown.selectByVisibleText(optionLabel);
					Thread.sleep(1000);
					if (getTimeSheetExportFormatSelectedOption().contains(optionLabel))
						SimpleUtils.pass("Time and Attendance: Timesheet export format '" + optionLabel + "' option selected successfully.");
					else
						SimpleUtils.fail("Time and Attendance: Unable to select Timesheet export format '" + optionLabel + "' option.", false);
				}
			} else
				SimpleUtils.fail("Controls - Time and Attendance: Timesheet export format dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: TimeSheet Export Format section not loaded.", false);
	}

	public String getTimeSheetExportFormatSelectedOption() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(timeSheetExportFormatDiv)) {
			WebElement timeSheetFormatDropDown = timeSheetExportFormatDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(timeSheetFormatDropDown)) {
				Select dropdown = new Select(timeSheetFormatDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Controls - Time and Attendance: timesheet export format dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: TimeSheet Export Format section not loaded.", false);
		return selectedOptionLabel;
	}


	@Override
	public void clickOnSchedulingPoliciesShiftAdvanceBtn() throws Exception {
//		if (isElementLoaded(schedulingPoliciesShiftFormSectionDiv,15)) {
//			WebElement schedulingPoliciesShiftAdvanceBtn = schedulingPoliciesShiftFormSectionDiv.findElement(
//					By.cssSelector("div.lg-advanced-box__toggle"));
//			if (isElementLoaded(schedulingPoliciesShiftAdvanceBtn, 15) && !schedulingPoliciesShiftAdvanceBtn.getAttribute("class")
//					.contains("--advanced")) {
//				scrollToElement(schedulingPoliciesShiftAdvanceBtn);
//				moveToElementAndClick(schedulingPoliciesShiftAdvanceBtn);
//				SimpleUtils.pass("Controls Page: - Scheduling Policies 'Shift' section: 'Advance' button clicked.");
//			} else
//				SimpleUtils.fail("Controls Page: - Scheduling Policies 'Shift' section: 'Advance' button not loaded.", false);
//		} else
//			SimpleUtils.fail("Controls Page: - Scheduling Policies section: 'Shift' form section not loaded.", false);
	}


	@Override
	public void selectSchedulingPoliciesShiftIntervalByLabel(String intervalTimeLabel) throws Exception {
		if (isElementLoaded(schedulingPoliciesShiftIntervalDiv)) {
			WebElement shiftIntervalDropDown = schedulingPoliciesShiftIntervalDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(shiftIntervalDropDown)) {
				Select dropdown = new Select(shiftIntervalDropDown);
				if (getshiftIntervalDropDownSelectedOption().contains(intervalTimeLabel)) {
					SimpleUtils.pass("Scheduling Policies: Shift Interval time '" + intervalTimeLabel + "' option already selected.");
				} else {
					dropdown.selectByVisibleText(intervalTimeLabel);
					Thread.sleep(1000);
					if (getshiftIntervalDropDownSelectedOption().contains(intervalTimeLabel))
						SimpleUtils.pass("Scheduling Policies: Shift Interval time '" + intervalTimeLabel + "' option selected successfully.");
					else
						SimpleUtils.fail("Scheduling Policies: Unable to select Shift Interval time '" + intervalTimeLabel + "' option.", false);
				}
			}
		}
	}

	public String getshiftIntervalDropDownSelectedOption() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(schedulingPoliciesShiftIntervalDiv)) {
			WebElement shiftIntervalDropDown = schedulingPoliciesShiftIntervalDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(shiftIntervalDropDown)) {
				Select dropdown = new Select(shiftIntervalDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Shift Interval time dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: 'Shift' section not loaded.", false);
		return selectedOptionLabel;
	}

	@Override
	public void clickOnSchedulingPoliciesSchedulesAdvanceBtn() throws Exception {
		if (isElementLoaded(schedulingPoliciesSchedulesFormSectionDiv)) {
			WebElement schedulingPoliciesShiftAdvanceBtn = schedulingPoliciesSchedulesFormSectionDiv.findElement(
					By.cssSelector("div.lg-advanced-box__toggle"));
			if (isElementLoaded(schedulingPoliciesShiftAdvanceBtn) && !schedulingPoliciesShiftAdvanceBtn.getAttribute("class")
					.contains("--advanced")) {
				click(schedulingPoliciesShiftAdvanceBtn);
				SimpleUtils.pass("Controls Page: - Scheduling Policies 'Schedules' section: 'Advance' button clicked.");
			} else
				SimpleUtils.fail("Controls Page: - Scheduling Policies 'Schedules' section: 'Advance' button not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: - Scheduling Policies section: 'Schedules' form section not loaded.", false);
	}


	@Override
	public String getSchedulePublishWindowWeeks() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(schedulePublishWindowDiv)) {
			WebElement schedulePublishWindowDropDown = schedulePublishWindowDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(schedulePublishWindowDropDown)) {
				Select dropdown = new Select(schedulePublishWindowDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize section not loaded.", false);
		return selectedOptionLabel;
	}

	@Override
	public int getAdvanceScheduleDaysCountToBeFinalize() throws Exception {
		int finalizeScheduleDays = 0;
		if (isElementLoaded(advanceFinalizeDaysDiv)) {
			WebElement finalizeScheduleDaysField = advanceFinalizeDaysDiv.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(finalizeScheduleDaysField)) {
				finalizeScheduleDays = Integer.valueOf(finalizeScheduleDaysField.getAttribute("value"));
			} else
				SimpleUtils.fail("Scheduling Policies: Advance Schedule days to be Finalize dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Advance Schedule days to be Finalize section not loaded.", false);
		return finalizeScheduleDays;
	}


	@Override
	public String getSchedulingPoliciesActiveLocation() throws Exception {
		String activeLocation = "";
		if (isElementLoaded(SchedulingPoliciesActiveLocation))
			activeLocation = SchedulingPoliciesActiveLocation.getText();
		else
			SimpleUtils.fail("Controls Page: Scheduling Policies active location not loaded.", true);
		return activeLocation;
	}


	@Override
	public void updateSchedulePublishWindow(String publishWindowAdvanceWeeks, boolean preserveSetting, boolean overwriteSetting) throws Exception {
		if (isElementLoaded(schedulePublishWindowDiv, 5)) {
			WebElement schedulePublishWindowDropDown = schedulePublishWindowDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(schedulePublishWindowDropDown, 5)) {
				Select dropdown = new Select(schedulePublishWindowDropDown);
				List<WebElement> dropdownOptions = dropdown.getOptions();
				for (WebElement dropdownOption : dropdownOptions) {
					if (dropdownOption.getText().toLowerCase().contains(publishWindowAdvanceWeeks.toLowerCase())) {
						click(dropdownOption);
						if (preserveSetting) {
							preserveTheSetting();
						} else if (overwriteSetting) {
							overwriteTheSetting();
						} else {
							displaySuccessMessage();
						}
					}
				}
				if (getSchedulePublishWindowWeeks().toLowerCase().contains(publishWindowAdvanceWeeks.toLowerCase()))
					SimpleUtils.pass("Scheduling Policies: Schedule Publish Window selected '" + publishWindowAdvanceWeeks + "' successfully.");
				else
					SimpleUtils.fail("Scheduling Policies: Schedule Publish Window '" + publishWindowAdvanceWeeks + "' not selected.", true);

			} else
				SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize section not loaded.", false);
	}


	@Override
	public void updateSchedulePlanningWindow(String planningWindowAdvanceWeeks, boolean preserveSetting, boolean overwriteSetting) throws Exception {
		if (isElementLoaded(schedulingWindow)) {
			WebElement schedulePlanningWindowDropDown = schedulingWindow.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(schedulePlanningWindowDropDown)) {
				Select dropdown = new Select(schedulePlanningWindowDropDown);
				List<WebElement> dropdownOptions = dropdown.getOptions();
				for (WebElement dropdownOption : dropdownOptions) {
					if (dropdownOption.getText().toLowerCase().contains(planningWindowAdvanceWeeks.toLowerCase())) {
						click(dropdownOption);
						if (preserveSetting) {
							preserveTheSetting();
						} else if (overwriteSetting) {
							overwriteTheSetting();
						} else {
							displaySuccessMessage();
						}
					}
				}
				if (getSchedulePlanningWindowWeeks().toLowerCase().contains(planningWindowAdvanceWeeks.toLowerCase()))
					SimpleUtils.pass("Scheduling Policies: Schedule Planning Window selected '" + planningWindowAdvanceWeeks + "' successfully.");
				else
					SimpleUtils.fail("Scheduling Policies: Schedule Planning Window '" + planningWindowAdvanceWeeks + "' not selected.", true);

			} else
				SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Published dropdown not loaded.", false);
		}

	}

	@Override
	public String getSchedulePlanningWindowWeeks() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(schedulingWindow)) {
			WebElement schedulePlanningWindowDropDown = schedulingWindow.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(schedulePlanningWindowDropDown)) {
				Select dropdown = new Select(schedulePlanningWindowDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Advance weeks to be Schedule dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Advance weeks to be Schedule section not loaded.", false);
		return selectedOptionLabel;
	}


	@Override
	public void updateSchedulingPoliciesFirstDayOfWeek(String day) throws Exception {
		if (isElementLoaded(schedulingPoliciesFirstDayOfWeek)) {
			WebElement firstDayOfWeekInputBox = schedulingPoliciesFirstDayOfWeek.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (firstDayOfWeekInputBox.isEnabled()) {
				Select dropdown = new Select(firstDayOfWeekInputBox);
				List<WebElement> dropdownOptions = dropdown.getOptions();
				for (WebElement dropdownOption : dropdownOptions) {
					if (dropdownOption.getText().toLowerCase().contains(day.toLowerCase())) {
						click(dropdownOption);
						preserveTheSetting();
					}
				}
				if (getSchedulingPoliciesFirstDayOfWeek().toLowerCase().contains(day.toLowerCase()))
					SimpleUtils.pass("Scheduling Policies: First Day of Week '" + day + "' selected successfully.");
				else
					SimpleUtils.fail("Scheduling Policies: First Day of Week '" + day + "' not selected successfully.", true);
			} else
				SimpleUtils.report("Scheduling Policies: First Day of Week input box 'Disabled'.");
		} else
			SimpleUtils.fail("Scheduling Policies: First Day of Week Field not loaded.", false);
	}

	@Override
	public String getSchedulingPoliciesFirstDayOfWeek() throws Exception {
		String firstDayOfWeekValue = "";
		if (isElementLoaded(schedulingPoliciesFirstDayOfWeek)) {
			WebElement firstDayOfWeekInputBox = schedulingPoliciesFirstDayOfWeek.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			Select dropdown = new Select(firstDayOfWeekInputBox);
			firstDayOfWeekValue = dropdown.getFirstSelectedOption().getText();
		} else
			SimpleUtils.fail("Scheduling Policies: First Day of Week Field not loaded.", false);
		return firstDayOfWeekValue;
	}


	@Override
	public void updateEarliestOpeningTime(String openingTime) throws Exception {
		if (isElementLoaded(earliestOpeningTimeDiv)) {
			WebElement openingTimeInputBox = earliestOpeningTimeDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (openingTimeInputBox.isEnabled()) {
				Select dropdown = new Select(openingTimeInputBox);
				List<WebElement> dropdownOptions = dropdown.getOptions();
				for (WebElement dropdownOption : dropdownOptions) {
					if (dropdownOption.getText().toLowerCase().contains(openingTime.toLowerCase())) {
						click(dropdownOption);
						preserveTheSetting();
					}
				}
				if (getEarliestOpeningTime().toLowerCase().contains(openingTime.toLowerCase()))
					SimpleUtils.pass("Scheduling Policies: Earliest Opening Time '" + openingTime + "' selected successfully.");
				else
					SimpleUtils.fail("Scheduling Policies: Earliest Opening Time '" + openingTime + "' not selected successfully.", true);
			} else
				SimpleUtils.report("Scheduling Policies: Earliest Opening Time input box 'Disabled'.");
		} else
			SimpleUtils.fail("Scheduling Policies: Earliest Opening Time Field not loaded.", false);
	}

	@Override
	public String getEarliestOpeningTime() throws Exception {
		String openingTimeValue = "";
		if (isElementLoaded(earliestOpeningTimeDiv)) {
			WebElement openingTimeValueInputBox = earliestOpeningTimeDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			Select dropdown = new Select(openingTimeValueInputBox);
			openingTimeValue = dropdown.getFirstSelectedOption().getText();
		} else
			SimpleUtils.fail("Scheduling Policies: Earliest Opening Time Field not loaded.", false);
		return openingTimeValue;
	}


	@Override
	public void updateLatestClosingTime(String closingTime) throws Exception {
		if (isElementLoaded(latestClosingTimeDiv)) {
			WebElement openingTimeInputBox = latestClosingTimeDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (openingTimeInputBox.isEnabled()) {
				Select dropdown = new Select(openingTimeInputBox);
				List<WebElement> dropdownOptions = dropdown.getOptions();
				for (WebElement dropdownOption : dropdownOptions) {
					if (dropdownOption.getText().toLowerCase().contains(closingTime.toLowerCase())) {
						click(dropdownOption);
						preserveTheSetting();
					}
				}
				if (getLatestClosingTime().toLowerCase().contains(closingTime.toLowerCase()))
					SimpleUtils.pass("Scheduling Policies: Latest Closing Time '" + closingTime + "' selected successfully.");
				else
					SimpleUtils.fail("Scheduling Policies: Latest Closing Time '" + closingTime + "' not selected successfully.", true);
			} else
				SimpleUtils.report("Scheduling Policies: Latest Closing Time input box 'Disabled'.");
		} else
			SimpleUtils.fail("Scheduling Policies: Latest Closing Time Field not loaded.", false);
	}

	@Override
	public String getLatestClosingTime() throws Exception {
		String openingTimeValue = "";
		if (isElementLoaded(latestClosingTimeDiv)) {
			WebElement openingTimeValueInputBox = latestClosingTimeDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			Select dropdown = new Select(openingTimeValueInputBox);
			openingTimeValue = dropdown.getFirstSelectedOption().getText();
		} else
			SimpleUtils.fail("Scheduling Policies: Latest Closing Time Field not loaded.", false);
		return openingTimeValue;
	}

	@Override
	public void updateAdvanceScheduleDaysToBeFinalize(String advanceDays) throws Exception {
		if (isElementLoaded(advanceFinalizeDaysDiv)) {
			WebElement finalizeScheduleDaysField = advanceFinalizeDaysDiv.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(finalizeScheduleDaysField)) {
				finalizeScheduleDaysField.clear();
				finalizeScheduleDaysField.sendKeys(advanceDays);
				if (isElementLoaded(confirmSaveButton)) {
					click(confirmSaveButton);
					preserveTheSetting();
				}
				if (getAdvanceScheduleDaysCountToBeFinalize() == Integer.valueOf(advanceDays))
					SimpleUtils.pass("Sceduling Policies: Advance days to be finalize updated successfully with value: '" + advanceDays + "'");
				else
					SimpleUtils.fail("Sceduling Policies: Advance days to be finalize not updated with value: '" + advanceDays + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Advance Schedule days to be Finalize input field not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Advance Schedule days to be Finalize section not loaded.", false);
	}

	@Override
	public void updateScheduleBufferHoursBefore(String beforeBufferCount) throws Exception {
		if (isElementLoaded(openingBufferHours)) {
			WebElement beforeBufferHrsElement = openingBufferHours.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(beforeBufferHrsElement)) {
				beforeBufferHrsElement.clear();
				beforeBufferHrsElement.sendKeys(beforeBufferCount);
				if (isElementLoaded(confirmSaveButton)) {
					click(confirmSaveButton);
					preserveTheSetting();
				}
				if (getScheduleBufferHours().get("openingBufferHours") == Integer.valueOf(beforeBufferCount))
					SimpleUtils.pass("Scheduling Policies: Schedule Buffer hours BEFORE updated successfully with value :'" + beforeBufferCount + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Schedule Buffer hours BEFORE not updated with value :'" + beforeBufferCount + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule Buffer hours BEFORE element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Opening Buffer hours section not loaded.", true);
	}

	@Override
	public void updateScheduleBufferHoursAfter(String afterBufferCount) throws Exception {
		if (isElementLoaded(closingBufferHours)) {
			WebElement afterBufferHrsElement = closingBufferHours.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(afterBufferHrsElement)) {
				afterBufferHrsElement.clear();
				afterBufferHrsElement.sendKeys(afterBufferCount);
				if (isElementLoaded(confirmSaveButton)) {
					click(confirmSaveButton);
					preserveTheSetting();
				}
				if (getScheduleBufferHours().get("closingBufferHours") == Integer.valueOf(afterBufferCount))
					SimpleUtils.pass("Scheduling Policies: Schedule Buffer hours AFTER updated successfully with value :'" + afterBufferCount + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Schedule Buffer hours AFTER not updated with value :'" + afterBufferCount + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule Buffer hours AFTER element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Closing Buffer hours section not loaded.", true);
	}


	@Override
	public void updateMinimumShiftLengthHour(String shiftLengthHour) throws Exception {
		if (isElementLoaded(minShiftLengthHourDiv)) {
			WebElement minShiftLengthHourInputBox = minShiftLengthHourDiv.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(minShiftLengthHourInputBox)) {
				minShiftLengthHourInputBox.clear();
				minShiftLengthHourInputBox.sendKeys(shiftLengthHour);
				if (isElementLoaded(confirmSaveButton)) {
					click(confirmSaveButton);
					preserveTheSetting();
				}
				if (getMinimumShiftLengthHour() == Float.valueOf(shiftLengthHour))
					SimpleUtils.pass("Scheduling Policies: Schedule Minimum Shift Length Hour updated successfully with value :'" + shiftLengthHour + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Schedule Minimum Shift Length Hour not updated with value :'" + shiftLengthHour + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule Minimum Shift Length Hour element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule Minimum Shift Length Hour section not loaded.", true);
	}

	@Override
	public float getMinimumShiftLengthHour() throws Exception {
		float minimumShiftLengthHour = 0;
		if (isElementLoaded(minShiftLengthHourDiv)) {
			WebElement minShiftLengthHourInputBox = minShiftLengthHourDiv.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(minShiftLengthHourInputBox)) {
				minimumShiftLengthHour = Float.valueOf(minShiftLengthHourInputBox.getAttribute("value"));
			} else
				SimpleUtils.fail("Scheduling Policies:  Schedule Minimum Shift Length Hour element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies:  Schedule Minimum Shift Length Hour section not loaded.", true);
		return minimumShiftLengthHour;
	}


	@Override
	public void updateMaximumShiftLengthHour(String shiftLengthHour) throws Exception {
		if (isElementLoaded(maxShiftLengthHourDiv)) {
			WebElement maxShiftLengthHourInputBox = maxShiftLengthHourDiv.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(maxShiftLengthHourInputBox)) {
				maxShiftLengthHourInputBox.clear();
				maxShiftLengthHourInputBox.sendKeys(shiftLengthHour);
				if (isElementLoaded(confirmSaveButton)) {
					click(confirmSaveButton);
					preserveTheSetting();
				}
				if (getMaximumShiftLengthHour() == Float.valueOf(shiftLengthHour))
					SimpleUtils.pass("Scheduling Policies: Schedule Maximum Shift Length Hour updated successfully with value :'" + shiftLengthHour + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Schedule Maximum Shift Length Hour not updated with value :'" + shiftLengthHour + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule Maximum Shift Length Hour element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule Maximum Shift Length Hour section not loaded.", true);
	}

	@Override
	public float getMaximumShiftLengthHour() throws Exception {
		float minimumShiftLengthHour = 0;
		if (isElementLoaded(maxShiftLengthHourDiv)) {
			WebElement maxShiftLengthHourInputBox = maxShiftLengthHourDiv.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(maxShiftLengthHourInputBox)) {
				minimumShiftLengthHour = Float.valueOf(maxShiftLengthHourInputBox.getAttribute("value"));
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule Maximum Shift Length Hour element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule Maximum Shift Length Hour section not loaded.", true);
		return minimumShiftLengthHour;
	}


	@Override
	public void updateMaximumNumWeekDaysToAutoSchedule(String maxDaysLabel) throws Exception {
		if (isElementLoaded(maxWorkingDaysBestPracticeInputField)) {
			WebElement maxWeekDaysToAutoSchedule = maxWorkingDaysBestPracticeInputField.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(maxWeekDaysToAutoSchedule)) {
				Select dropdown = new Select(maxWeekDaysToAutoSchedule);
				List<WebElement> dropdownOptions = dropdown.getOptions();
				for (WebElement dropdownOption : dropdownOptions) {
					if (dropdownOption.getText().toLowerCase().contains(maxDaysLabel.toLowerCase())) {
						click(dropdownOption);
						preserveTheSetting();
					}
				}
				if (getMaximumNumWeekDaysToAutoSchedule().toLowerCase().contains(maxDaysLabel.toLowerCase()))
					SimpleUtils.pass("Scheduling Policies: Maximum Number of Week Days To Auto Schedule updated successfully with value :'"
							+ maxDaysLabel + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Maximum Number of Week Days To Auto Schedule not updated with value :'"
							+ maxDaysLabel + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Maximum Number of Week Days To Auto Schedule element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Maximum Number of Week Days To Auto Schedule section not loaded.", true);
	}

	@Override
	public String getMaximumNumWeekDaysToAutoSchedule() throws Exception {
		String minimumShiftLengthHourValue = "";
		if (isElementLoaded(maxWorkingDaysBestPracticeInputField)) {
			WebElement maximumNumWeekDaysToAutoSchedule = maxWorkingDaysBestPracticeInputField.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(maximumNumWeekDaysToAutoSchedule)) {
				Select dropdown = new Select(maximumNumWeekDaysToAutoSchedule);
				minimumShiftLengthHourValue = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Maximum Number of Week Days To Auto Schedule element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Maximum Number of Week Days To Auto Schedule section not loaded.", true);

		return minimumShiftLengthHourValue;
	}


	@Override
	public void updateDisplayShiftBreakIcons(String shiftBreakIcons) throws Exception {
		if (isElementLoaded(displayBreakTimeInShiftInputField)) {
			WebElement shiftBreakIconsElement = displayBreakTimeInShiftInputField.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(shiftBreakIconsElement)) {
				Select dropdown = new Select(shiftBreakIconsElement);
				List<WebElement> dropdownOptions = dropdown.getOptions();
				for (WebElement dropdownOption : dropdownOptions) {
					if (dropdownOption.getText().toLowerCase().contains(shiftBreakIcons.toLowerCase())) {
						click(dropdownOption);
						preserveTheSetting();
					}
				}
				if (getDisplayShiftBreakIcons().toLowerCase().contains(shiftBreakIcons.toLowerCase()))
					SimpleUtils.pass("Scheduling Policies: Display Shift Break Icons updated successfully with value :'"
							+ shiftBreakIcons + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Display Shift Break Icons not updated with value :'"
							+ shiftBreakIcons + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Display Shift Break Icons element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Display Shift Break Icons section not loaded.", true);
	}

	@Override
	public String getDisplayShiftBreakIcons() throws Exception {
		String displayShiftBreakIconsValue = "";
		if (isElementLoaded(displayBreakTimeInShiftInputField)) {
			WebElement maxWeekDaysToAutoSchedule = displayBreakTimeInShiftInputField.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(maxWeekDaysToAutoSchedule)) {
				Select dropdown = new Select(maxWeekDaysToAutoSchedule);
				displayShiftBreakIconsValue = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Display Shift Break Icons element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Display Shift Break Icons section not loaded.", true);

		return displayShiftBreakIconsValue;
	}


	@Override
	public void updateApplyLaborBudgetToSchedules(String isLaborBudgetToApply) throws Exception {
		if (isElementLoaded(applyLaborBudgetToSchedules)) {
			WebElement applyLaborBudgetBtnGroup = applyLaborBudgetToSchedules.findElement(
					By.cssSelector("div.lg-button-group"));
			if (applyLaborBudgetBtnGroup.getAttribute("class").contains("disabled")) {
				SimpleUtils.report("Scheduling Policies: Apply Labor Budget to Schedules buttons are disabled.");
			} else {
				List<WebElement> applyLaborBudgetToSchedulesBtns = applyLaborBudgetToSchedules.findElements(
						By.cssSelector("div[ng-click=\"!button.disabled && $ctrl.change(button.value)\"]"));
				waitForSeconds(5);
				if (applyLaborBudgetToSchedulesBtns.size() > 0) {
					for (WebElement applyLaborBudgetToSchedulesBtn : applyLaborBudgetToSchedulesBtns) {
						if (applyLaborBudgetToSchedulesBtn.getText().toLowerCase().contains(isLaborBudgetToApply.toLowerCase())) {
							scrollToElement(applyLaborBudgetToSchedulesBtn);
							clickTheElement(applyLaborBudgetToSchedulesBtn);
							preserveTheSetting();
						}
					}
					if (getApplyLaborBudgetToSchedulesActiveBtnLabel().toLowerCase().contains(isLaborBudgetToApply.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Apply Labor Budget to Schedules updated with value: '"
								+ isLaborBudgetToApply + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Apply Labor Budget to Schedules not updated with value: '"
								+ isLaborBudgetToApply + "'.", false);
				} else
					SimpleUtils.fail("Scheduling Policies: Apply Labor Budget to Schedules buttons not loaded.", true);
			}

		} else
			SimpleUtils.fail("Scheduling Policies: Apply Labor Budget to Schedules section not loaded.", true);
	}

	@Override
	public String getApplyLaborBudgetToSchedulesActiveBtnLabel() throws Exception {
		String laborBudgetToApplyActiveBtnLabel = "";
		if (isElementLoaded(applyLaborBudgetToSchedules)) {
			WebElement applyLaborBudgetActiveBtn = applyLaborBudgetToSchedules.findElement(
					By.cssSelector("div.lg-button-group-selected"));
			if (isElementLoaded(applyLaborBudgetActiveBtn))
				laborBudgetToApplyActiveBtnLabel = applyLaborBudgetActiveBtn.getText();
			else
				SimpleUtils.fail("Scheduling Policies: Labor Budget to Apply active Button not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Apply Labor Budget to Schedules section not loaded.", true);

		return laborBudgetToApplyActiveBtnLabel;
	}


	@Override
	public void updateScheduleBudgetType(String budgetType) throws Exception {
		if (isElementLoaded(scheduleBudgetTypeElement)) {
			WebElement budgetTypeElement = scheduleBudgetTypeElement.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (!budgetTypeElement.isEnabled()) {
				SimpleUtils.report("Scheduling Policies: Schedule budget Type Dropdown desabled.");
			} else {
				if (isElementLoaded(budgetTypeElement)) {
					Select dropdown = new Select(budgetTypeElement);

					List<WebElement> dropdownOptions = dropdown.getOptions();
					for (WebElement dropdownOption : dropdownOptions) {
						if (dropdownOption.getText().toLowerCase().contains(budgetType.toLowerCase())) {
							click(dropdownOption);
							preserveTheSetting();
						}
					}
					if (getScheduleBudgetType().toLowerCase().contains(budgetType.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Schedule budget Type updated with the value :'" + budgetType + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Schedule budget Type not updated with the value :'" + budgetType + "'.", true);
				} else
					SimpleUtils.fail("Scheduling Policies: Schedule budget Type dropdown not loaded.", true);
			}
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule budget Type section not loaded.", true);
	}

	public String getScheduleBudgetType() throws Exception {
		String BudgetTypeLabel = "";
		if (isElementLoaded(scheduleBudgetTypeElement)) {
			WebElement budgetTypeElement = scheduleBudgetTypeElement.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(budgetTypeElement)) {
				Select dropdown = new Select(budgetTypeElement);
				BudgetTypeLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule budget Type dropdown not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule budget Type section not loaded.", true);

		return BudgetTypeLabel;
	}


	@Override
	public void updateTeamAvailabilityLockMode(String availabilityLockModeLabel) throws Exception {
		if (isElementLoaded(availabilityLockModeDiv)) {
			WebElement budgetTypeElement = availabilityLockModeDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (!budgetTypeElement.isEnabled()) {
				SimpleUtils.report("Scheduling Policies: Team Availability Lock Mode desabled.");
			} else {
				if (isElementLoaded(budgetTypeElement)) {
					Select dropdown = new Select(budgetTypeElement);

					List<WebElement> dropdownOptions = dropdown.getOptions();
					for (WebElement dropdownOption : dropdownOptions) {
						if (dropdownOption.getText().toLowerCase().contains(availabilityLockModeLabel.toLowerCase())) {
							click(dropdownOption);
							preserveTheSetting();
						}
					}
					if (getTeamAvailabilityLockMode().toLowerCase().contains(availabilityLockModeLabel.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Team Availability Lock Mode updated with the value :'"
								+ availabilityLockModeLabel + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Team Availability Lock Mode not updated with the value :'"
								+ availabilityLockModeLabel + "'.", true);
				} else
					SimpleUtils.fail("Scheduling Policies: Team Availability Lock Mode dropdown not loaded.", true);
			}
		} else
			SimpleUtils.fail("Scheduling Policies: Team Availability Lock Mode section not loaded.", true);
	}


	public String getTeamAvailabilityLockMode() throws Exception {
		String availabilityLockModeLabel = "";
		if (isElementLoaded(availabilityLockModeDiv)) {
			WebElement budgetTypeElement = availabilityLockModeDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(budgetTypeElement)) {
				Select dropdown = new Select(budgetTypeElement);
				availabilityLockModeLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Team Availability Lock Mode dropdown not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Team Availability Lock Mode section not loaded.", true);

		return availabilityLockModeLabel;
	}


	@Override
	public void updateScheduleUnavailableHourOfWorkers(String unavailableWorkersHour) throws Exception {
		if (isElementLoaded(scheduleUnavailableHourWorkersDiv)) {
			WebElement scheduleUnavailableHourWorkers = scheduleUnavailableHourWorkersDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (!scheduleUnavailableHourWorkers.isEnabled()) {
				SimpleUtils.report("Scheduling Policies: Schedule Unavailable Hour of Workers field desabled.");
			} else {
				if (isElementLoaded(scheduleUnavailableHourWorkers)) {
					Select dropdown = new Select(scheduleUnavailableHourWorkers);

					List<WebElement> dropdownOptions = dropdown.getOptions();
					for (WebElement dropdownOption : dropdownOptions) {
						if (dropdownOption.getText().toLowerCase().contains(unavailableWorkersHour.toLowerCase())) {
							click(dropdownOption);
							preserveTheSetting();
						}
					}
					if (getScheduleUnavailableHourOfWorkers().toLowerCase().contains(unavailableWorkersHour.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Schedule Unavailable Hour of Workers updated with the value :'"
								+ unavailableWorkersHour + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Schedule Unavailable Hour of Workers not updated with the value :'"
								+ unavailableWorkersHour + "'.", true);
				} else
					SimpleUtils.fail("Scheduling Policies: Schedule Unavailable Hour of Workers dropdown not loaded.", true);
			}
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule Unavailable Hour of Workers section not loaded.", true);
	}


	public String getScheduleUnavailableHourOfWorkers() throws Exception {
		String unavailableHourOfWorkers = "";
		if (isElementLoaded(scheduleUnavailableHourWorkersDiv)) {
			WebElement scheduleUnavailableHourWorkers = scheduleUnavailableHourWorkersDiv.findElement(
					By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(scheduleUnavailableHourWorkers)) {
				Select dropdown = new Select(scheduleUnavailableHourWorkers);
				unavailableHourOfWorkers = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule Unavailable Hour of Workers dropdown not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule Unavailable Hour of Workers section not loaded.", true);

		return unavailableHourOfWorkers;
	}


	@Override
	public void updateAvailabilityToleranceField(String availabilityToleranceMinutes) throws Exception {
		if (isElementLoaded(availabilityToleranceField)) {
			WebElement availabilityToleranceInputBox = availabilityToleranceField.findElement(
					By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (!availabilityToleranceInputBox.isEnabled()) {
				SimpleUtils.report("Scheduling Policies: Availability Tolerance Field desabled.");
			} else {
				if (isElementLoaded(availabilityToleranceInputBox)) {
					availabilityToleranceInputBox.clear();
					availabilityToleranceInputBox.sendKeys(availabilityToleranceMinutes);
					if (isElementLoaded(confirmSaveButton))
						click(confirmSaveButton);
					preserveTheSetting();
					if (getAvailabilityToleranceMinutes() == Float.valueOf(availabilityToleranceMinutes))
						SimpleUtils.pass("Scheduling Policies: Availability Tolerance Field updated with the value :'" + availabilityToleranceMinutes + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Availability Tolerance Field not updated with the value :'" + availabilityToleranceMinutes + "'.", true);
				} else
					SimpleUtils.fail("Scheduling Policies: Availability Tolerance Field not loaded.", true);
			}
		} else
			SimpleUtils.fail("Scheduling Policies: SAvailability Tolerance Field section not loaded.", true);
	}


	public float getAvailabilityToleranceMinutes() throws Exception {
		float availabilityToleranceMinutes = 0;
		if (isElementLoaded(availabilityToleranceField)) {
			WebElement availabilityToleranceInputBox = availabilityToleranceField.findElement(
					By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(availabilityToleranceInputBox)) {
				availabilityToleranceMinutes = Float.valueOf(availabilityToleranceInputBox.getAttribute("value"));
			} else
				SimpleUtils.fail("Scheduling Policies: Availability Tolerance Field not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Availability Tolerance Field section not loaded.", true);

		return availabilityToleranceMinutes;
	}


	@Override
	public void updateCanWorkerRequestTimeOff(String canWorkerRequestTimeOffValue) throws Exception {
		scrollToBottom();
		// Wait for data loaded
		waitForSeconds(15);
		if (isElementLoaded(canWorkerRequestTimeOffBtnGroup)) {
			WebElement canWorkerRequestTimeOffBtnGroupDiv = canWorkerRequestTimeOffBtnGroup.findElement(
					By.cssSelector("div.lg-button-group"));
			if (canWorkerRequestTimeOffBtnGroupDiv.getAttribute("class").contains("disabled")) {
				SimpleUtils.report("Scheduling Policies: Can Worker Request Time Off buttons are disabled.");
			} else {
				List<WebElement> canWorkerRequestTimeOffBtns = canWorkerRequestTimeOffBtnGroup.findElements(
						By.cssSelector("span.buttonLabel"));
				if (canWorkerRequestTimeOffBtns.size() > 0) {
					for (WebElement canWorkerRequestTimeOffBtn : canWorkerRequestTimeOffBtns) {
						if (canWorkerRequestTimeOffBtn.getText().toLowerCase().contains(canWorkerRequestTimeOffValue.toLowerCase())) {
							click(canWorkerRequestTimeOffBtn);
							preserveTheSetting();
						}
					}
					if (getCanWorkerRequestTimeOffActiveBtnLabel().toLowerCase().contains(canWorkerRequestTimeOffValue.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Can Worker Request Time Off  updated with value: '"
								+ canWorkerRequestTimeOffValue + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Can Worker Request Time Off  not updated with value: '"
								+ canWorkerRequestTimeOffValue + "'.", true);
				} else
					SimpleUtils.fail("Scheduling Policies: Can Worker Request Time Off  buttons not loaded.", true);
			}

		} else
			SimpleUtils.fail("Scheduling Policies: Can Worker Request Time Off section not loaded.", true);
	}

	public String getCanWorkerRequestTimeOffActiveBtnLabel() throws Exception {
		String canWorkerRequestTimeOffActiveBtnLabel = "";
		if (isElementLoaded(canWorkerRequestTimeOffBtnGroup)) {
			WebElement canWorkerRequestTimeOffBtn = canWorkerRequestTimeOffBtnGroup.findElement(
					By.cssSelector("div.lg-button-group-selected"));
			if (isElementLoaded(canWorkerRequestTimeOffBtn))
				canWorkerRequestTimeOffActiveBtnLabel = canWorkerRequestTimeOffBtn.getText();
			else
				SimpleUtils.fail("Scheduling Policies: Can Worker Request Time Off Button not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Can Worker Request Time Off section not loaded.", true);

		return canWorkerRequestTimeOffActiveBtnLabel;
	}


	@Override
	public void updateMaxEmployeeCanRequestForTimeOffOnSameDay(String maxWorkersTimeOfPerDayCount) throws Exception {
		if (isElementLoaded(maxWorkersOnTimeOffPerDayField)) {
			WebElement maxWorkersTimeOfPerDayInputBox = maxWorkersOnTimeOffPerDayField.findElement(
					By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(maxWorkersTimeOfPerDayInputBox)) {
				maxWorkersTimeOfPerDayInputBox.clear();
				maxWorkersTimeOfPerDayInputBox.sendKeys(maxWorkersTimeOfPerDayCount);
				if (isElementLoaded(confirmSaveButton)) {
					click(confirmSaveButton);
					preserveTheSetting();
				}
				if (getMaxEmployeeCanRequestForTimeOffOnSameDay() == Float.valueOf(maxWorkersTimeOfPerDayCount))
					SimpleUtils.pass("Scheduling Policies: Max Workers TimeOf Per Day updated successfully with value :'"
							+ maxWorkersTimeOfPerDayCount + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Max Workers TimeOf Per Day not updated with value :'"
							+ maxWorkersTimeOfPerDayCount + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Max Workers TimeOf Per Day element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Max Workers TimeOf Per Day section not loaded.", true);
	}


	private float getMaxEmployeeCanRequestForTimeOffOnSameDay() throws Exception {
		float minimumShiftLengthHour = 0;
		if (isElementLoaded(maxWorkersOnTimeOffPerDayField)) {
			WebElement maxWorkersOnTimeOffPerDayInputBox = maxWorkersOnTimeOffPerDayField.findElement(
					By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(maxWorkersOnTimeOffPerDayInputBox)) {
				minimumShiftLengthHour = Float.valueOf(maxWorkersOnTimeOffPerDayInputBox.getAttribute("value"));
			} else
				SimpleUtils.fail("Scheduling Policies: Max Workers TimeOf Per Day element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Max Workers TimeOf Per Day section not loaded.", true);
		return minimumShiftLengthHour;
	}


	@Override
	public void clickOnSchedulingPoliciesTimeOffAdvanceBtn() throws Exception {
		if (isElementLoaded(schedulingPoliciesTimeOffFormSectionDiv, 10)) {
			scrollToElement(schedulingPoliciesTimeOffFormSectionDiv);
			waitForSeconds(1);
			WebElement schedulingPoliciesTimeOffAdvanceBtn = schedulingPoliciesTimeOffFormSectionDiv.findElement(
					By.cssSelector("div.lg-advanced-box__toggle"));
			if (isElementLoaded(schedulingPoliciesTimeOffAdvanceBtn, 10) && !schedulingPoliciesTimeOffAdvanceBtn.getAttribute("class")
					.contains("--advanced")) {
				click(schedulingPoliciesTimeOffAdvanceBtn);
				SimpleUtils.pass("Controls Page: - Scheduling Policies 'Time Off' section: 'Advance' button clicked.");
			} else
				SimpleUtils.fail("Controls Page: - Scheduling Policies 'Time Off' section: 'Advance' button not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: - Scheduling Policies section: 'Time Off' form section not loaded.", false);
	}

	@FindBy(css = "[question-title*=\"Number of days requests must be made in advance\"] .input-form")
	private WebElement daysInAdvanceToCreateTimeOff;
	@Override
	public int getDaysInAdvanceCreateTimeOff() throws Exception {
		waitForSeconds(3);
		if (isElementLoaded(daysInAdvanceToCreateTimeOff,15)){
			if (SimpleUtils.isNumeric(daysInAdvanceToCreateTimeOff.findElement(By.cssSelector("div.input-faked")).getAttribute("innerText").replaceAll("\n","").trim())){
				return Integer.parseInt(daysInAdvanceToCreateTimeOff.findElement(By.cssSelector("div.input-faked")).getAttribute("innerText").replaceAll("\n","").trim());
			}
		} else {
			SimpleUtils.fail("Days in advance fail to load!", false);
		}
		return 0;
	}

	@Override
	public void updateNoticePeriodToRequestTimeOff(String noticePeriodToRequestTimeOff) throws Exception {
		if (isElementLoaded(noticePeriodToRequestTimeOffField)) {
			WebElement noticePeriodToRequestTimeOffInputBox = noticePeriodToRequestTimeOffField.findElement(
					By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(noticePeriodToRequestTimeOffInputBox)) {
				noticePeriodToRequestTimeOffInputBox.clear();
				noticePeriodToRequestTimeOffInputBox.sendKeys(noticePeriodToRequestTimeOff);
				if (isElementLoaded(confirmSaveButton)) {
					click(confirmSaveButton);
					preserveTheSetting();
				}
				if (getNoticePeriodToRequestTimeOff() == Float.valueOf(noticePeriodToRequestTimeOff))
					SimpleUtils.pass("Scheduling Policies: Notice Period To Request Time Off updated successfully with value :'"
							+ noticePeriodToRequestTimeOff + "'");
				else
					SimpleUtils.fail("Scheduling Policies: Notice Period To Request Time Off not updated with value :'"
							+ noticePeriodToRequestTimeOff + "'", true);
			} else
				SimpleUtils.fail("Scheduling Policies: Notice Period To Request Time Off element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Notice Period To Request Time Off section not loaded.", true);
	}


	private float getNoticePeriodToRequestTimeOff() throws Exception {
		float noticePeriodToRequestTimeOffValue = 0;
		if (isElementLoaded(noticePeriodToRequestTimeOffField)) {
			WebElement noticePeriodToRequestTimeOffInputBox = noticePeriodToRequestTimeOffField.findElement(
					By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(noticePeriodToRequestTimeOffInputBox)) {
				noticePeriodToRequestTimeOffValue = Float.valueOf(noticePeriodToRequestTimeOffInputBox.getAttribute("value"));
			} else
				SimpleUtils.fail("Scheduling Policies: Notice Period To Request Time Off element not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Notice Period To Request Time Off section not loaded.", true);
		return noticePeriodToRequestTimeOffValue;
	}

	@Override
	public void updateShowTimeOffReasons(String isShowTimeOffReasons) throws Exception {
		if (isElementLoaded(showTimeOffReasonsBtnGroup)) {
			WebElement showTimeOffReasonsBtnGroupBtnGroupDiv = showTimeOffReasonsBtnGroup.findElement(
					By.cssSelector("div.lg-button-group"));
			if (showTimeOffReasonsBtnGroupBtnGroupDiv.getAttribute("class").contains("disabled")) {
				SimpleUtils.report("Scheduling Policies: Require time off reason in the request buttons are disabled.");
			} else {
				List<WebElement> showTimeOffReasonsBtns = showTimeOffReasonsBtnGroup.findElements(
						By.cssSelector("span.buttonLabel"));
				if (showTimeOffReasonsBtns.size() > 0) {
					for (WebElement showTimeOffReasonsBtn : showTimeOffReasonsBtns) {
						if (showTimeOffReasonsBtn.getText().toLowerCase().contains(isShowTimeOffReasons.toLowerCase())) {
							click(showTimeOffReasonsBtn);
							preserveTheSetting();
						}
					}
					if (getShowTimeOffReasons().toLowerCase().contains(isShowTimeOffReasons.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Require time off reason in the request updated with value: '"
								+ isShowTimeOffReasons + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Require time off reason in the request not updated with value: '"
								+ isShowTimeOffReasons + "'.", true);
				} else
					SimpleUtils.fail("Scheduling Policies: Require time off reason in the request buttons not loaded.", true);
			}

		} else
			SimpleUtils.fail("Scheduling Policies: Require time off reason in the request section not loaded.", true);
	}

	public String getShowTimeOffReasons() throws Exception {
		String showTimeOffReasonsValue = "";
		if (isElementLoaded(showTimeOffReasonsBtnGroup)) {
			WebElement showTimeOffReasonsActiveBtn = showTimeOffReasonsBtnGroup.findElement(
					By.cssSelector("div.lg-button-group-selected"));
			if (isElementLoaded(showTimeOffReasonsActiveBtn))
				showTimeOffReasonsValue = showTimeOffReasonsActiveBtn.getText();
			else
				SimpleUtils.fail("Scheduling Policies: Require time off reason in the request Button not loaded.", true);
		} else
			SimpleUtils.fail("Scheduling Policies: Require time off reason in the request section not loaded.", true);

		return showTimeOffReasonsValue;
	}

	public void preserveTheSetting() throws Exception {
		if (isElementLoaded(preserveSettingBtn, 5)) {
			clickTheElement(preserveSettingBtn);
			displaySuccessMessage();
		}
	}

	public void overwriteTheSetting() throws Exception {
		if (isElementLoaded(overwriteSettingBtn, 5)) {
			click(overwriteSettingBtn);
			displaySuccessMessage();
		}
	}


	@Override
	public void updateSchedulingPolicyGroupsHoursPerWeek(int minHours, int maxHours, int idealHours) throws Exception {
		for (int index = 0; index < numHoursPerWeekMinInputFields.size(); index++) {

			if (numHoursPerWeekMinInputFields.get(index).isDisplayed()) {
				WebElement minHoursPerWeekInputBox = numHoursPerWeekMinInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(minHoursPerWeekInputBox)) {
					int value = Integer.valueOf(minHoursPerWeekInputBox.getAttribute("value"));
					if (value == minHours) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Minimum Hours' already updated with value:'" + minHours + "'.");
					} else {
						minHoursPerWeekInputBox.clear();
						minHoursPerWeekInputBox.sendKeys(String.valueOf(minHours));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(minHoursPerWeekInputBox.getAttribute("value")) == minHours)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Minimum Hours' updated with value:'" + minHours + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Minimum Hours' not updated with value:'" + minHours + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Minimum Hours' Input box not loaded.", true);
				}
			}

			if (numHoursPerWeekMaxInputFields.get(index).isDisplayed()) {
				WebElement maxHoursPerWeekInputBox = numHoursPerWeekMaxInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(maxHoursPerWeekInputBox)) {
					int value = Integer.valueOf(maxHoursPerWeekInputBox.getAttribute("value"));
					if (value == maxHours) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Maximum Hours' already updated with value:'" + maxHours + "'.");
					} else {
						maxHoursPerWeekInputBox.clear();
						maxHoursPerWeekInputBox.sendKeys(String.valueOf(maxHours));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(maxHoursPerWeekInputBox.getAttribute("value")) == maxHours)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Maximum Hours' updated with value:'" + maxHours + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Maximum Hours' not updated with value:'" + maxHours + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Maximum Hours' Input box not loaded.", true);
				}
			}

			if (numHoursPerWeekIdealInputFields.get(index).isDisplayed()) {
				WebElement idealHoursPerWeekInputBox = numHoursPerWeekIdealInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(idealHoursPerWeekInputBox)) {
					int value = Integer.valueOf(idealHoursPerWeekInputBox.getAttribute("value"));
					if (value == idealHours) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Ideal Hours' already updated with value:'" + idealHours + "'.");
					} else {
						idealHoursPerWeekInputBox.clear();
						idealHoursPerWeekInputBox.sendKeys(String.valueOf(idealHours));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(idealHoursPerWeekInputBox.getAttribute("value")) == idealHours)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Ideal Hours' updated with value:'" + idealHours + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Ideal Hours' not updated with value:'" + idealHours + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Hours Per Week 'Ideal Hours' Input box not loaded.", true);
				}
			}
		}
	}


	@Override
	public void updateSchedulingPolicyGroupsShiftsPerWeek(int minShifts, int maxShifts, int idealShifts) throws Exception {
		for (int index = 0; index < numShiftsPerWeekMinInputFields.size(); index++) {

			if (numShiftsPerWeekMinInputFields.get(index).isDisplayed()) {
				WebElement minShiftsPerWeekInputBox = numShiftsPerWeekMinInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(minShiftsPerWeekInputBox)) {
					int value = Integer.valueOf(minShiftsPerWeekInputBox.getAttribute("value"));
					if (value == minShifts) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Shifts' already updated with value:'" + minShifts + "'.");
					} else {
						minShiftsPerWeekInputBox.clear();
						minShiftsPerWeekInputBox.sendKeys(String.valueOf(minShifts));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(minShiftsPerWeekInputBox.getAttribute("value")) == minShifts)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Shifts' updated with value:'" + minShifts + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Shifts' not updated with value:'" + minShifts + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Shifts' Input box not loaded.", true);
				}
			}

			if (numShiftsPerWeekMaxInputFields.get(index).isDisplayed()) {
				WebElement maxShiftsPerWeekInputBox = numShiftsPerWeekMaxInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(maxShiftsPerWeekInputBox)) {
					int value = Integer.valueOf(maxShiftsPerWeekInputBox.getAttribute("value"));
					if (value == maxShifts) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Shifts' already updated with value:'" + maxShifts + "'.");
					} else {
						maxShiftsPerWeekInputBox.clear();
						maxShiftsPerWeekInputBox.sendKeys(String.valueOf(maxShifts));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(maxShiftsPerWeekInputBox.getAttribute("value")) == maxShifts)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Shifts' updated with value:'" + maxShifts + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Shifts' not updated with value:'" + maxShifts + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Shifts' Input box not loaded.", true);
				}
			}

			if (numShiftsPerWeekIdealInputFields.get(index).isDisplayed()) {
				WebElement idealShiftsPerWeekInputBox = numShiftsPerWeekIdealInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(idealShiftsPerWeekInputBox)) {
					int value = Integer.valueOf(idealShiftsPerWeekInputBox.getAttribute("value"));
					if (value == idealShifts) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Shifts' already updated with value:'" + idealShifts + "'.");
					} else {
						idealShiftsPerWeekInputBox.clear();
						idealShiftsPerWeekInputBox.sendKeys(String.valueOf(idealShifts));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(idealShiftsPerWeekInputBox.getAttribute("value")) == idealShifts)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Shifts' updated with value:'" + idealShifts + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Shifts' not updated with value:'" + idealShifts + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Shifts' Input box not loaded.", true);
				}
			}
		}
	}


	@Override
	public void updateSchedulingPolicyGroupsHoursPerShift(int minHoursPerShift, int maxHoursPerShift, int ideaHoursPerShift) throws Exception {
		for (int index = 0; index < minHoursPerShiftInputFields.size(); index++) {

			if (minHoursPerShiftInputFields.get(index).isDisplayed()) {
				WebElement minHoursPerShiftInputBox = minHoursPerShiftInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(minHoursPerShiftInputBox)) {
					int value = Integer.valueOf(minHoursPerShiftInputBox.getAttribute("value"));
					if (value == minHoursPerShift) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Hours Per Shifts' already updated with value:'" + minHoursPerShift + "'.");
					} else {
						minHoursPerShiftInputBox.clear();
						minHoursPerShiftInputBox.sendKeys(String.valueOf(minHoursPerShift));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(minHoursPerShiftInputBox.getAttribute("value")) == minHoursPerShift)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Hours Per Shifts' updated with value:'" + minHoursPerShift + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Hours Per Shifts' not updated with value:'" + minHoursPerShift + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Minimum Hours Per Shifts' Input box not loaded.", true);
				}
			}

			if (maxHoursPerShiftInputFields.get(index).isDisplayed()) {
				WebElement maxHoursPerShiftInputBox = maxHoursPerShiftInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(maxHoursPerShiftInputBox)) {
					int value = Integer.valueOf(maxHoursPerShiftInputBox.getAttribute("value"));
					if (value == maxHoursPerShift) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Hours Per Shifts' already updated with value:'" + maxHoursPerShift + "'.");
					} else {
						maxHoursPerShiftInputBox.clear();
						maxHoursPerShiftInputBox.sendKeys(String.valueOf(maxHoursPerShift));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(maxHoursPerShiftInputBox.getAttribute("value")) == maxHoursPerShift)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Hours Per Shifts' updated with value:'" + maxHoursPerShift + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Hours Per Shifts' not updated with value:'" + maxHoursPerShift + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Maximum Hours Per Shifts' Input box not loaded.", true);
				}
			}

			if (ideaHoursPerShiftInputFields.get(index).isDisplayed()) {
				WebElement ideaHoursPerShiftInputBox = ideaHoursPerShiftInputFields.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(ideaHoursPerShiftInputBox)) {
					int value = Integer.valueOf(ideaHoursPerShiftInputBox.getAttribute("value"));
					if (value == ideaHoursPerShift) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Hours Per Shifts' already updated with value:'" + ideaHoursPerShift + "'.");
					} else {
						ideaHoursPerShiftInputBox.clear();
						ideaHoursPerShiftInputBox.sendKeys(String.valueOf(ideaHoursPerShift));
						if (isElementLoaded(confirmSaveButton, 10))
							click(confirmSaveButton);
						if (Integer.valueOf(ideaHoursPerShiftInputBox.getAttribute("value")) == ideaHoursPerShift)
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Hours Per Shifts' updated with value:'" + ideaHoursPerShift + "'.");
						else
							SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Hours Per Shifts' not updated with value:'" + ideaHoursPerShift + "'.", true);
					}
				} else {
					SimpleUtils.fail("Scheduling Policies: Scheduling Policy Groups Shifts Per Week 'Ideal Hours Per Shifts' Input box not loaded.", true);
				}
			}
		}
	}


	@Override
	public void updateEnforceNewEmployeeCommittedAvailabilityWeeks(boolean isEmployeeCommittedAvailabilityActive, int committedHoursWeeks) throws Exception {
		if (isElementLoaded(schedulePolicyGroupSection)) {
			List<WebElement> employeeCommittedAvailabilitySwitchToggles = schedulePolicyGroupSection.findElements(
					By.cssSelector("lg-switch.lg-question-input__toggle"));
			for (WebElement employeeCommittedAvailabilitySwitchToggle : employeeCommittedAvailabilitySwitchToggles) {
				if (employeeCommittedAvailabilitySwitchToggle.isDisplayed()) {

					WebElement toggleCheckBox = employeeCommittedAvailabilitySwitchToggle.findElement(By.cssSelector("input[type=\"checkbox\"]"));
					boolean isSliderChecked = toggleCheckBox.getAttribute("class").contains("ng-not-empty");
					if (isEmployeeCommittedAvailabilityActive && isSliderChecked) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Enforce New Employee Committed Availability Toggle already 'Enabled'.");
					} else if (isEmployeeCommittedAvailabilityActive && !isSliderChecked) {
						click(employeeCommittedAvailabilitySwitchToggle);
						if (toggleCheckBox.getAttribute("class").contains("ng-not-empty"))
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Enforce New Employee Committed Availability Toggle 'Enabled' successfully.");
					} else if (!isEmployeeCommittedAvailabilityActive && isSliderChecked) {
						click(employeeCommittedAvailabilitySwitchToggle);
						if (!toggleCheckBox.getAttribute("class").contains("ng-not-empty"))
							SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Enforce New Employee Committed Availability Toggle 'Disabled' successfully.");
					} else if (!isEmployeeCommittedAvailabilityActive && !isSliderChecked) {
						SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Enforce New Employee Committed Availability Toggle already 'Disabled'.");
					}
				}
			}

			if (isEmployeeCommittedAvailabilityActive) {
				for (WebElement committedHoursPeriodFiled : committedHoursPeriodFileds) {
					if (committedHoursPeriodFiled.isDisplayed()) {
						WebElement committedHoursPeriodInputBox = committedHoursPeriodFiled.findElement(
								By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
						if (isElementLoaded(committedHoursPeriodInputBox)) {
							int value = Integer.valueOf(committedHoursPeriodInputBox.getAttribute("value"));
							if (value == committedHoursWeeks)
								SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Enforce New Employee Committed "
										+ "Availability Hours Week input filed already updated with value :'" + committedHoursWeeks + "'.");
							else {
								committedHoursPeriodInputBox.clear();
								committedHoursPeriodInputBox.sendKeys(String.valueOf(committedHoursWeeks));
								if (Integer.valueOf(committedHoursPeriodInputBox.getAttribute("value")) == committedHoursWeeks)
									SimpleUtils.pass("Scheduling Policies: Scheduling Policy Groups Enforce New Employee Committed "
											+ "Availability Hours Week input filed updated with value :'" + committedHoursWeeks + "'.");
							}
						}
					}
				}
			}
		}
	}


	@Override
	public void selectSchdulingPolicyGroupsTabByLabel(String tabLabel) throws Exception {
		boolean isTabSelected = false;
		if (areListElementVisible(schedulingPolicyGroupsTabs, 10) && schedulingPolicyGroupsTabs.size() > 0) {
			for (WebElement schedulingPolicyGroupsTab : schedulingPolicyGroupsTabs) {
				if (schedulingPolicyGroupsTab.getText().replace("Non Exempt", "Nonexempt").toLowerCase().contains(tabLabel.toLowerCase())) {
					click(schedulingPolicyGroupsTab);
					isTabSelected = true;
				}
			}
			if (isTabSelected)
				SimpleUtils.pass("Scheduling Policy Groups Tab '" + tabLabel + "' selected successfully.");
			else
				SimpleUtils.fail("Scheduling Policy Groups: Unable to select Tab '" + tabLabel + "'.", false);

		} else
			SimpleUtils.fail("Scheduling Policy Groups Tabs not loaded.", false);
	}


	@FindBy(css = "form-section[form-title=\"Location information\"]")
	private WebElement locationInformationFormSection;

	/*@FindBy(css="input-field[disabled=\"!editing\"]")
	private List<WebElement> locationInformationFields;*/
	@Override
	public HashMap<String, ArrayList<String>> getLocationInformationEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		clickOnLocationProfileEditLocationBtn();
		Thread.sleep(1000);
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		if (isLocationProfileEditLocationSectionLoaded() && isElementLoaded(locationInformationFormSection)) {
			List<WebElement> locationInformationFields = locationInformationFormSection.findElements(
					By.cssSelector("input-field[ng-attr-disabled=\"!editing\"]"));
			if (locationInformationFields.size() > 0) {
				String fieldLabel = "";
				for (WebElement inputField : locationInformationFields) {
					List<WebElement> inputFieldLabelElements = inputField.findElements(By.cssSelector("label.input-label"));
					if (inputFieldLabelElements.size() > 0) {
						fieldLabel = inputFieldLabelElements.get(0).getText();
						List<WebElement> inputBoxFields = inputField.findElements(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
						boolean isFieldEditable = false;
						if (inputBoxFields.size() > 0) {
							String fieldType = "input";
							isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
							if (isFieldEditable)
								editableFields.add(fieldLabel);
							else
								nonEditableFields.add(fieldLabel);
						} else {
							inputBoxFields = inputField.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
							if (inputBoxFields.size() > 0) {
								String fieldType = "select";
								isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
								if (isFieldEditable)
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							} else {
								notLoadedFields.add(fieldLabel);
							}
						}
					}
				}
				editableOrNonEditableFields.put("editableFields", editableFields);
				editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
				editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
			} else
				SimpleUtils.fail("Location Profile: 'Location Information' fields not loaded.", false);
		}
		clickOnLocationProfileEditCancelBtn();
		return editableOrNonEditableFields;
	}

	@FindBy(css = "lg-button[label=\"Cancel\"]")
	private List<WebElement> cancelButtons;

	private void clickOnLocationProfileEditCancelBtn() {
		if (cancelButtons.size() > 0) {
			click(cancelButtons.get(0));
			SimpleUtils.pass("Location Profile: Edit location 'Cancel' button clicked successfully.");
		} else
			SimpleUtils.fail("Location Profile: Edit location 'Cancel' button not loaded.", false);
	}


	private boolean isInputFieldEditable(WebElement webElement, String fieldType) throws Exception {
		boolean isfieldEditable = false;
		if (webElement.isDisplayed() && webElement.isEnabled()) {
			if (fieldType.toLowerCase().contains("input")) {
				try {
					String originalInputBoxValue = webElement.getAttribute("value");
					String fieldValueType = webElement.getAttribute("type");
					String sampleText = "Sample Text";
					if (fieldValueType.toLowerCase().contains("checkbox")) {
						click(webElement);
						Thread.sleep(1000);
						click(webElement);
						isfieldEditable = true;
					} else {
						if (fieldValueType.toLowerCase().contains("number"))
							sampleText = "2";
						webElement.clear();
						webElement.sendKeys(sampleText);
						if (webElement.getAttribute("value").toLowerCase().contains(sampleText.toLowerCase()))
							isfieldEditable = true;
						webElement.clear();
						webElement.sendKeys(originalInputBoxValue);
					}
				} catch (Exception e) {
					isfieldEditable = false;
				}
			} else if (fieldType.toLowerCase().contains("select")) {
				Select dropdownField = new Select(webElement);
				String originalSelectBoxValue = dropdownField.getFirstSelectedOption().getText();
				for (WebElement option : dropdownField.getOptions()) {
					if (!option.getText().equalsIgnoreCase(originalSelectBoxValue)) {
						click(option);
						if (!dropdownField.getFirstSelectedOption().getText().equalsIgnoreCase(originalSelectBoxValue))
							isfieldEditable = true;
						else if (dropdownField.getOptions().size() < 2)
							isfieldEditable = true;
						break;
					}
				}
				Thread.sleep(1000);
				if (originalSelectBoxValue.trim().length() > 0)
					dropdownField.selectByVisibleText(originalSelectBoxValue);
			} else if (fieldType.toLowerCase().contains("button")) {
				List<WebElement> btns = webElement.findElements(By.cssSelector("div.lg-button-group-selected"));
				if (btns.size() > 0) {
					try {
						click(btns.get(0));
						isfieldEditable = true;
					} catch (Exception e) {
						isfieldEditable = false;
					}
				} else if (webElement.isDisplayed() && webElement.isEnabled())
					isfieldEditable = true;
			} else if (fieldType.toLowerCase().contains("div")) {
				try {
					click(webElement);
					Thread.sleep(1000);
					click(webElement);
					isfieldEditable = true;
				} catch (Exception e) {
					isfieldEditable = false;
				}
			}
		}
		return isfieldEditable;
	}

	public static boolean isClickable(WebElement webe) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 5);
			wait.until(ExpectedConditions.elementToBeClickable(webe));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@FindBy(css = "lg-button[label=\"Edit Location\"]")
	private WebElement locationProfileEditLocationBtn;

	@Override
	public void clickOnLocationProfileEditLocationBtn() throws Exception {
		if (isElementLoaded(locationProfileEditLocationBtn)) {
			if (locationProfileEditLocationBtn.isEnabled()) {
				click(locationProfileEditLocationBtn);
				SimpleUtils.pass("Controls page: Location Profile section 'Edit Location' button clicked successfully.");
			} else
				SimpleUtils.report("Controls page: Location Profile section 'Edit Location' button not Enabled.");
		} else
			SimpleUtils.fail("Controls page: Location Profile section 'Edit Location' button not loaded.", false);
	}

	@FindBy(css = "[aria-label=\"Time Zone\"]")
	private WebElement timeZoneInput;

	@Override
	public void checkTimeZoneDropdownOptions(int targetNumbersOfUTCFormat, String timeZone) throws Exception {
		int timeZoneFormatedCount = 0;
		int incorrectFormatedCount = 0;
		if (isElementLoaded(timeZoneInput, 10) && isClickable(timeZoneInput, 10)) {
			scrollToElement(timeZoneInput);
			click(timeZoneInput);
			waitForSeconds(3);
			Select select = new Select(timeZoneInput);
			List<WebElement> options = select.getOptions();
			for (WebElement option : options) {
				if (option.getText().contains(timeZone)) {
					timeZoneFormatedCount++;
				} else {
					incorrectFormatedCount++;
				}
			}
			if ((timeZoneFormatedCount != targetNumbersOfUTCFormat) && (timeZoneFormatedCount != (targetNumbersOfUTCFormat + 8))) {
				SimpleUtils.fail("The UTC time zone count is " + timeZoneFormatedCount + " doesn't match target number " + targetNumbersOfUTCFormat, false);
			}
			if (incorrectFormatedCount > 0) {
				SimpleUtils.fail("There was " + incorrectFormatedCount + " didn't format to UTC time zone!", false);
			}
		} else {
			SimpleUtils.fail("Failed for loading time zone field!", false);
		}
	}

	@FindBy(css = "div.lg-general-form__top-buttons")
	private WebElement locationProfileEditFormSaveCancelBtnsDiv;

	public boolean isLocationProfileEditLocationSectionLoaded() throws Exception {
		if (isElementLoaded(locationProfileEditFormSaveCancelBtnsDiv)
				&& locationProfileEditFormSaveCancelBtnsDiv.isDisplayed() && locationProfileEditFormSaveCancelBtnsDiv.isEnabled()) {
			return true;
		}

		return false;
	}

	@Override
	public HashMap<String, ArrayList<String>> getSchedulingPoliciesSchedulesSectionEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		if (isElementLoaded(schedulingPoliciesSchedulesFormSectionDiv)) {
			List<WebElement> schedulesSectionFields = schedulingPoliciesSchedulesFormSectionDiv.findElements(
					By.cssSelector("div.lg-question-input"));
			for (WebElement schedulesSectionField : schedulesSectionFields) {
				WebElement fieldLabelDiv = schedulesSectionField.findElement(By.cssSelector("h3.lg-question-input__text"));
				String fieldTitle = fieldLabelDiv.getText();
				List<WebElement> inputBoxFields = schedulesSectionField.findElements(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				boolean isFieldEditable = false;
				if (inputBoxFields.size() > 0) {
					String fieldType = "input";
					isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
					if (isFieldEditable)
						editableFields.add(fieldTitle);
					else
						nonEditableFields.add(fieldTitle);
				} else {
					inputBoxFields = schedulesSectionField.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					if (inputBoxFields.size() > 0) {
						String fieldType = "select";
						isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
						if (isFieldEditable)
							editableFields.add(fieldTitle);
						else
							nonEditableFields.add(fieldTitle);
					} else {
						notLoadedFields.add(fieldTitle);
					}
				}
			}
			editableOrNonEditableFields.put("editableFields", editableFields);
			editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
			editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		}
		return editableOrNonEditableFields;
	}

	@Override
	public HashMap<String, ArrayList<String>> getSchedulingPoliciesShiftsSectionEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		if (isElementLoaded(schedulingPoliciesShiftFormSectionDiv)) {
			List<WebElement> ShiftsSectionFields = schedulingPoliciesShiftFormSectionDiv.findElements(
					By.cssSelector("div.lg-question-input"));
			for (WebElement ShiftsSectionField : ShiftsSectionFields) {
				WebElement fieldLabelDiv = ShiftsSectionField.findElement(By.cssSelector("h3.lg-question-input__text"));
				String fieldTitle = fieldLabelDiv.getText();
				List<WebElement> inputBoxFields = ShiftsSectionField.findElements(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				boolean isFieldEditable = false;
				if (inputBoxFields.size() > 0) {
					String fieldType = "input";
					isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
					if (isFieldEditable)
						editableFields.add(fieldTitle);
					else
						nonEditableFields.add(fieldTitle);
				} else {
					inputBoxFields = ShiftsSectionField.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					if (inputBoxFields.size() > 0) {
						String fieldType = "select";
						isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
						if (isFieldEditable)
							editableFields.add(fieldTitle);
						else
							nonEditableFields.add(fieldTitle);
					} else
						notLoadedFields.add(fieldTitle);
				}
			}
			editableOrNonEditableFields.put("editableFields", editableFields);
			editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
			editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		}
		return editableOrNonEditableFields;
	}

	@Override
	public HashMap<String, ArrayList<String>> getSchedulingPoliciesBudgetSectionEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		if (isElementLoaded(budgetFormSection)) {
			List<WebElement> budgetSectionFields = budgetFormSection.findElements(
					By.cssSelector("div.lg-question-input"));
			for (WebElement budgetSectionField : budgetSectionFields) {
				WebElement fieldLabelDiv = budgetSectionField.findElement(By.cssSelector("h3.lg-question-input__text"));
				String fieldTitle = fieldLabelDiv.getText();
				List<WebElement> inputBoxFields = budgetSectionField.findElements(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				boolean isFieldEditable = false;
				if (inputBoxFields.size() > 0) {
					String fieldType = "input";
					isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
					if (isFieldEditable)
						editableFields.add(fieldTitle);
					else
						nonEditableFields.add(fieldTitle);
				} else {
					inputBoxFields = budgetSectionField.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					if (inputBoxFields.size() > 0) {
						String fieldType = "select";
						isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
						if (isFieldEditable)
							editableFields.add(fieldTitle);
						else
							nonEditableFields.add(fieldTitle);
					} else {
						inputBoxFields = budgetSectionField.findElements(By.cssSelector("lg-button-group[on-change=\"$ctrl.handleChange()\"]"));
						if (inputBoxFields.size() > 0) {
							String fieldType = "lg-button-group";
							isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
							if (isFieldEditable)
								editableFields.add(fieldTitle);
							else
								nonEditableFields.add(fieldTitle);
						} else
							notLoadedFields.add(fieldTitle);
					}
				}
			}
			editableOrNonEditableFields.put("editableFields", editableFields);
			editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
			editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		}
		return editableOrNonEditableFields;
	}

	@FindBy(css = "form-section[form-title=\"Team availability management\"]")
	private WebElement teamAvailabilityManagementFormSection;

	@Override
	public HashMap<String, ArrayList<String>> getSchedulingPoliciesTeamAvailabilityManagementSectionEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		if (isElementLoaded(teamAvailabilityManagementFormSection)) {
			List<WebElement> teamAvailabilityManagementSectionFields = teamAvailabilityManagementFormSection.findElements(
					By.cssSelector("div.lg-question-input"));
			String fieldTitle = "";
			for (WebElement teamAvailabilityManagementSectionField : teamAvailabilityManagementSectionFields) {
				Thread.sleep(5000);
				try {
					WebElement fieldLabelDiv = teamAvailabilityManagementSectionField.findElement(By.cssSelector("h3.lg-question-input__text"));
					fieldTitle = fieldLabelDiv.getText();
					List<WebElement> inputBoxFields = teamAvailabilityManagementSectionField.findElements(
							By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
					boolean isFieldEditable = false;
					if (inputBoxFields.size() > 0) {
						String fieldType = "input";
						isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
						if (isFieldEditable)
							editableFields.add(fieldTitle);
						else
							nonEditableFields.add(fieldTitle);
					} else {
						inputBoxFields = teamAvailabilityManagementSectionField.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
						if (inputBoxFields.size() > 0) {
							String fieldType = "select";
							isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
							if (isFieldEditable)
								editableFields.add(fieldTitle);
							else
								nonEditableFields.add(fieldTitle);
						} else
							notLoadedFields.add(fieldTitle);
					}
				} catch (StaleElementReferenceException SERException) {
					SimpleUtils.report("Scheduling Policies: 'Team Availability Management' Section Input field After '" + fieldTitle + "' not loaded.");
				}
			}
			editableOrNonEditableFields.put("editableFields", editableFields);
			editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
			editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		}
		return editableOrNonEditableFields;
	}


	@Override
	public HashMap<String, ArrayList<String>> getSchedulingPoliciesTimeOffSectionEditableOrNonEditableFields()
			throws Exception {
		//schedulingPoliciesTimeOffFormSectionDiv
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		if (isElementLoaded(schedulingPoliciesTimeOffFormSectionDiv)) {
			List<WebElement> timeOffSectionFields = schedulingPoliciesTimeOffFormSectionDiv.findElements(
					By.cssSelector("div.lg-question-input"));
			for (WebElement timeOffSectionField : timeOffSectionFields) {
				WebElement fieldLabelDiv = timeOffSectionField.findElement(By.cssSelector("h3.lg-question-input__text"));
				String fieldTitle = fieldLabelDiv.getText();
				List<WebElement> inputBoxFields = timeOffSectionField.findElements(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				boolean isFieldEditable = false;
				if (inputBoxFields.size() > 0) {
					String fieldType = "input";
					isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
					if (isFieldEditable)
						editableFields.add(fieldTitle);
					else
						nonEditableFields.add(fieldTitle);
				} else {
					inputBoxFields = timeOffSectionField.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					if (inputBoxFields.size() > 0) {
						String fieldType = "select";
						isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
						if (isFieldEditable)
							editableFields.add(fieldTitle);
						else
							nonEditableFields.add(fieldTitle);
					} else {
						inputBoxFields = timeOffSectionField.findElements(By.cssSelector("lg-button-group[on-change=\"$ctrl.handleChange()\"]"));
						if (inputBoxFields.size() > 0) {
							String fieldType = "lg-button-group";
							isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
							if (isFieldEditable)
								editableFields.add(fieldTitle);
							else
								nonEditableFields.add(fieldTitle);
						} else
							notLoadedFields.add(fieldTitle);
					}
				}
			}
			editableOrNonEditableFields.put("editableFields", editableFields);
			editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
			editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		}
		return editableOrNonEditableFields;
	}


	@Override
	public HashMap<String, ArrayList<String>> getSchedulingPoliciesSchedulingPolicyGroupsSectionEditableOrNonEditableFields()
			throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();

		// Hours per Week Fields
		String hoursPerWeekMinimumFieldTitle = "Hours per Week Minimum";
		boolean isHoursPerWeekMinimumFieldEnable = isSchedulingPolicyGroupsFieldEnable(numHoursPerWeekMinInputFields);
		if (isHoursPerWeekMinimumFieldEnable)
			editableFields.add(hoursPerWeekMinimumFieldTitle);
		else
			nonEditableFields.add(hoursPerWeekMinimumFieldTitle);

		String hoursPerWeekMaximumFieldTitle = "Hours per Week Maximum";
		boolean isHoursPerWeekMaximumFieldEnable = isSchedulingPolicyGroupsFieldEnable(numHoursPerWeekMaxInputFields);
		if (isHoursPerWeekMaximumFieldEnable)
			editableFields.add(hoursPerWeekMaximumFieldTitle);
		else
			nonEditableFields.add(hoursPerWeekMaximumFieldTitle);

		String hoursPerWeekIdealFieldTitle = "Hours per Week Ideal";
		boolean isHoursPerWeekIdealFieldEnable = isSchedulingPolicyGroupsFieldEnable(numHoursPerWeekIdealInputFields);
		if (isHoursPerWeekIdealFieldEnable)
			editableFields.add(hoursPerWeekIdealFieldTitle);
		else
			nonEditableFields.add(hoursPerWeekIdealFieldTitle);

		// Shifts per Week Fields
		String shiftsPerWeekMinimumFieldTitle = "Shifts per Week Minimum";
		boolean isShiftsPerWeekMinimumFieldEnable = isSchedulingPolicyGroupsFieldEnable(numShiftsPerWeekMinInputFields);
		if (isShiftsPerWeekMinimumFieldEnable)
			editableFields.add(shiftsPerWeekMinimumFieldTitle);
		else
			nonEditableFields.add(shiftsPerWeekMinimumFieldTitle);

		String shiftsPerWeekMaximumFieldTitle = "Shifts per Week Maximum";
		boolean isShiftsPerWeekMaximumFieldEnable = isSchedulingPolicyGroupsFieldEnable(numShiftsPerWeekMaxInputFields);
		if (isShiftsPerWeekMaximumFieldEnable)
			editableFields.add(shiftsPerWeekMaximumFieldTitle);
		else
			nonEditableFields.add(shiftsPerWeekMaximumFieldTitle);

		String shiftsPerWeekIdealFieldTitle = "Shifts per Week Ideal";
		boolean isShiftsPerWeekIdealFieldEnable = isSchedulingPolicyGroupsFieldEnable(numShiftsPerWeekIdealInputFields);
		if (isShiftsPerWeekIdealFieldEnable)
			editableFields.add(shiftsPerWeekIdealFieldTitle);
		else
			nonEditableFields.add(shiftsPerWeekIdealFieldTitle);

		// Hours per Shift Fields
		String hoursPerShiftMinimumFieldTitle = "Hours per Shift Minimum";
		boolean isHoursPerShiftMinimumFieldEnable = isSchedulingPolicyGroupsFieldEnable(minHoursPerShiftInputFields);
		if (isHoursPerShiftMinimumFieldEnable)
			editableFields.add(hoursPerShiftMinimumFieldTitle);
		else
			nonEditableFields.add(hoursPerShiftMinimumFieldTitle);

		String hoursPerShiftMaximumFieldTitle = "Hours per Shift Maximum";
		boolean isHoursPerShiftMaximumFieldEnable = isSchedulingPolicyGroupsFieldEnable(maxHoursPerShiftInputFields);
		if (isHoursPerShiftMaximumFieldEnable)
			editableFields.add(hoursPerShiftMaximumFieldTitle);
		else
			nonEditableFields.add(hoursPerShiftMaximumFieldTitle);

		String hoursPerShiftIdealFieldTitle = "Hours per Shift Ideal";
		boolean isHoursPerShiftIdealFieldEnable = isSchedulingPolicyGroupsFieldEnable(ideaHoursPerShiftInputFields);
		if (isHoursPerShiftIdealFieldEnable)
			editableFields.add(hoursPerShiftIdealFieldTitle);
		else
			nonEditableFields.add(hoursPerShiftIdealFieldTitle);

		// Scheduling Policy Groups Enforce New Employee Committed Availability Toggle button
		List<WebElement> employeeCommittedAvailabilitySwitchToggles = schedulePolicyGroupSection.findElements(
				By.cssSelector("lg-switch.lg-question-input__toggle"));
		String employeeCommittedAvailabilitySwitchToggleFieldTitle = "Enforce New Employee Committed Availability Toggle button";
		for (WebElement employeeCommittedAvailabilitySwitchToggle : employeeCommittedAvailabilitySwitchToggles) {
			if (employeeCommittedAvailabilitySwitchToggle.isDisplayed()) {
				try {
					click(employeeCommittedAvailabilitySwitchToggle);
					editableFields.add(employeeCommittedAvailabilitySwitchToggleFieldTitle);
					click(employeeCommittedAvailabilitySwitchToggle);
				} catch (Exception e) {
					nonEditableFields.add(employeeCommittedAvailabilitySwitchToggleFieldTitle);
				}
				break;
			}
		}

		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}

	public boolean isSchedulingPolicyGroupsFieldEnable(List<WebElement> inputFieldsList) throws Exception {
		for (int index = 0; index < inputFieldsList.size(); index++) {
			if (inputFieldsList.get(index).isDisplayed()) {
				WebElement inputBox = inputFieldsList.get(index).findElement(
						By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(inputBox)) {
					String fieldType = "input";
					boolean isFieldEditable = isInputFieldEditable(inputBox, fieldType);
					if (isFieldEditable)
						return true;
				}
			}
		}
		return false;
	}


	@FindBy(css = "div.lg-question-input")
	private List<WebElement> pageFields;

	@FindBy(css = "page-heading[page-title=\"Schedule Collaboration\"]")
	private WebElement scheduleCollaborationPageHeader;

	@Override
	public HashMap<String, ArrayList<String>> getScheduleCollaborationEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		if (isElementLoaded(scheduleCollaborationPageHeader)) {
			ArrayList<String> editableFields = new ArrayList<String>();
			ArrayList<String> nonEditableFields = new ArrayList<String>();
			ArrayList<String> notLoadedFields = new ArrayList<String>();

			if (pageFields.size() > 0) {
				for (WebElement field : pageFields) {
					WebElement fieldLabelDiv = field.findElement(By.cssSelector("h3.lg-question-input__text"));
					String fieldTitle = fieldLabelDiv.getText();
					List<WebElement> inputBoxFields = field.findElements(
							By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
					boolean isFieldEditable = false;
					if (inputBoxFields.size() > 0) {
						String fieldType = "input";
						isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
						if (isFieldEditable)
							editableFields.add(fieldTitle);
						else
							nonEditableFields.add(fieldTitle);
					} else {
						inputBoxFields = field.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
						if (inputBoxFields.size() > 0) {
							String fieldType = "select";
							isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
							if (isFieldEditable)
								editableFields.add(fieldTitle);
							else
								nonEditableFields.add(fieldTitle);
						} else {
							inputBoxFields = field.findElements(By.cssSelector("lg-button-group[on-change=\"$ctrl.handleChange()\"]"));
							if (inputBoxFields.size() > 0) {
								String fieldType = "lg-button-group";
								isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
								if (isFieldEditable)
									editableFields.add(fieldTitle);
								else
									nonEditableFields.add(fieldTitle);
							} else
								notLoadedFields.add(fieldTitle);
						}
					}
				}
				editableOrNonEditableFields.put("editableFields", editableFields);
				editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
				editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
			}
		}
		return editableOrNonEditableFields;
	}


	@FindBy(css = "form-section[form-title=\"Open Shifts\"]")
	private WebElement scheduleCollaborationOpenShiftFormSectionDiv;

	@Override
	public void clickOnScheduleCollaborationOpenShiftAdvanceBtn() throws Exception {
		if (isElementLoaded(scheduleCollaborationOpenShiftFormSectionDiv)) {
			WebElement schedulingPoliciesShiftAdvanceBtn = scheduleCollaborationOpenShiftFormSectionDiv.findElement(
					By.cssSelector("div.lg-advanced-box__toggle"));
			if (isElementLoaded(schedulingPoliciesShiftAdvanceBtn) && !schedulingPoliciesShiftAdvanceBtn.getAttribute("class")
					.contains("--advanced")) {
				click(schedulingPoliciesShiftAdvanceBtn);
				SimpleUtils.pass("Controls Page: - Schedule Collaboration 'Open Shift' section: 'Advance' button clicked.");
			} else
				SimpleUtils.fail("Controls Page: - Schedule Collaboration 'Open Shift' section: 'Advance' button not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: - Schedule Collaboration section: 'Open Shift' form section not loaded.", false);
	}

	@FindBy(xpath = "//*[text()=\"Allow employees claim open shift at overtime rate?\"]")
	private WebElement allowEmployeesClaimOpenShiftOption;

	@FindBy(css = "[question-title=\"Allow employees claim open shift at overtime rate?\"] .lg-button-group-first")
	private WebElement allowEmployeesClaimOpenShiftOptionYesBtn;

	@FindBy(css = "[question-title=\"Allow employees claim open shift at overtime rate?\"] .lg-button-group-last")
	private WebElement allowEmployeesClaimOpenShiftOptionNoBtn;

	@Override
	public void allowEmployeesClaimOvertimeShiftOffer() throws Exception {
		if (isElementLoaded(allowEmployeesClaimOpenShiftOption, 5)) {
			scrollToElement(allowEmployeesClaimOpenShiftOption);
			if (isElementLoaded(allowEmployeesClaimOpenShiftOptionYesBtn, 5)) {
				click(allowEmployeesClaimOpenShiftOptionYesBtn);
				waitForSeconds(3);
				if (allowEmployeesClaimOpenShiftOptionYesBtn.getAttribute("class").contains("lg-button-group-selected")) {
					SimpleUtils.pass("Claim overtime shift access is enabled!");
				} else {
					SimpleUtils.fail("Failed for enable claim overtime shift access!", false);
				}
			}
		} else {
			SimpleUtils.fail("No option found for: Allow employees claim open shift at overtime rate?", false);
		}
	}

	@Override
	public void notAllowEmployeesClaimOvertimeShiftOffer() throws Exception {
		if (isElementLoaded(allowEmployeesClaimOpenShiftOption, 5)) {
			scrollToElement(allowEmployeesClaimOpenShiftOption);
			if (isElementLoaded(allowEmployeesClaimOpenShiftOptionNoBtn, 5)) {
				click(allowEmployeesClaimOpenShiftOptionNoBtn);
				waitForSeconds(3);
				if (allowEmployeesClaimOpenShiftOptionNoBtn.getAttribute("class").contains("lg-button-group-selected")) {
					SimpleUtils.pass("Claim overtime shift access is disabled!");
				} else {
					SimpleUtils.fail("Failed for disable claim overtime shift access!", false);
				}
			}
		} else {
			SimpleUtils.fail("No option found for: Allow employees claim open shift at overtime rate?", false);
		}
	}

	@FindBy(css = "input-field[value=\"cl.minorSchedulingPolicy.scheduleStartMin\"]")
	private WebElement minorSchTimeLabelFromField;

	@FindBy(css = "input-field[value=\"cl.minorSchedulingPolicy.scheduleEndMin\"]")
	private WebElement minorSchTimeLabelToField;

	@FindBy(css = "input-field[label=\"Mobile Policy URL\"]")
	private WebElement mobilePolicyURLField;

	@Override
	public HashMap<String, ArrayList<String>> getComplianceEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		if (isControlsComplianceLoaded()) {
			ArrayList<String> editableFields = new ArrayList<String>();
			ArrayList<String> nonEditableFields = new ArrayList<String>();
			ArrayList<String> notLoadedFields = new ArrayList<String>();

			if (pageFields.size() > 0) {
				for (WebElement field : pageFields) {
					WebElement fieldLabelDiv = field.findElement(By.cssSelector("h3.lg-question-input__text"));
					String fieldTitle = fieldLabelDiv.getText();
					List<WebElement> inputBoxFields = field.findElements(
							By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
					List<WebElement> sliderFields = field.findElements(
							By.cssSelector("label.switch"));
					if (sliderFields.size() > 0) {
						try {
							WebElement sliderCheckBox = sliderFields.get(0).findElement(By.cssSelector("input[type=\"checkbox\"]"));
							String classBeforeChange = sliderCheckBox.getAttribute("class");
							click(sliderFields.get(0));
							if (!classBeforeChange.equals(sliderCheckBox.getAttribute("class"))) {
								click(sliderFields.get(0));
								editableFields.add(fieldTitle);
							} else {
								nonEditableFields.add(fieldTitle);
							}
						} catch (Exception e) {
							nonEditableFields.add(fieldTitle);
						}
					} else {
						boolean isFieldEditable = false;
						if (inputBoxFields.size() > 0) {
							String fieldType = "input";
							isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
							if (isFieldEditable)
								editableFields.add(fieldTitle);
							else
								nonEditableFields.add(fieldTitle);
						} else {
							inputBoxFields = field.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
							if (inputBoxFields.size() > 0) {
								String fieldType = "select";
								isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
								if (isFieldEditable)
									editableFields.add(fieldTitle);
								else
									nonEditableFields.add(fieldTitle);
							} else {
								inputBoxFields = field.findElements(By.cssSelector("lg-button-group[on-change=\"$ctrl.handleChange()\"]"));
								if (inputBoxFields.size() > 0) {
									String fieldType = "lg-button-group";
									isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
									if (isFieldEditable)
										editableFields.add(fieldTitle);
									else
										nonEditableFields.add(fieldTitle);
								} else
									notLoadedFields.add(fieldTitle);
							}
						}
					}
				}

				// 

				String minorScheduleTimeFromLabel = "Minor Schedule Time 'From' Input Field";
				boolean isMinorScheduleTimeFromEditable = false;
				if (isElementLoaded(minorSchTimeLabelFromField)) {
					WebElement inputFiledDropDown = minorSchTimeLabelFromField.findElement(
							By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					String fieldType = "select";
					isMinorScheduleTimeFromEditable = isInputFieldEditable(inputFiledDropDown, fieldType);
					if (isMinorScheduleTimeFromEditable)
						editableFields.add(minorScheduleTimeFromLabel);
					else
						nonEditableFields.add(minorScheduleTimeFromLabel);
				}

				String minorScheduleTimeToLabel = "Minor Schedule Time 'To' Input Field";
				boolean isMinorScheduleTimeToEditable = false;
				if (isElementLoaded(minorSchTimeLabelToField)) {
					WebElement inputFiledDropDown = minorSchTimeLabelFromField.findElement(
							By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					String fieldType = "select";
					isMinorScheduleTimeToEditable = isInputFieldEditable(inputFiledDropDown, fieldType);
					if (isMinorScheduleTimeToEditable)
						editableFields.add(minorScheduleTimeToLabel);
					else
						nonEditableFields.add(minorScheduleTimeToLabel);
				}

				// Mobile Policy URL Field
				String mobilePolicyURLFieldLabel = "Mobile Policy URL";
				boolean isMobilePolicyURLFieldEditable = false;
				if (isElementLoaded(mobilePolicyURLField)) {
					WebElement inputFiledDropDown = mobilePolicyURLField.findElement(
							By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
					String fieldType = "input";
					isMobilePolicyURLFieldEditable = isInputFieldEditable(inputFiledDropDown, fieldType);
					if (isMobilePolicyURLFieldEditable)
						editableFields.add(mobilePolicyURLFieldLabel);
					else
						nonEditableFields.add(mobilePolicyURLFieldLabel);
				}

				editableOrNonEditableFields.put("editableFields", editableFields);
				editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
				editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
			}
		}
		return editableOrNonEditableFields;
	}

	@FindBy(css = "div.lg-tabs__nav-item")
	private List<WebElement> subTabs;

	@Override
	public void selectUsersAndRolesSubTabByLabel(String label) throws Exception {
		boolean isTabFound = false;
		waitForSeconds(3);
		if (areListElementVisible(subTabs,15) && subTabs.size() > 0) {
			for (WebElement subTab : subTabs) {
				if (subTab.getText().toLowerCase().contains(label.toLowerCase())) {
					click(subTab);
					isTabFound = true;
					break;
				}
			}
			if (isTabFound)
				SimpleUtils.pass("Controls Page: Users and Roles section - '" + label + "' tab selected successfully.");
			else
				SimpleUtils.fail("Controls Page: Users and Roles section - '" + label + "' tab not found.", true);
		} else
			SimpleUtils.fail("Controls Page: Users and Roles section - sub tabs not loaded.", false);
	}

	@FindBy(css = "lg-button[label=\"Add New User\"]")
	private WebElement usersAndRolesAddNewUserButton;
	@FindBy(css = "form-section[on-action=\"editUser()\"]")
	private WebElement addNewUsereditFormSection;
	@FindBy(css = "tr[ng-repeat=\"user in $ctrl.pagedUsers\"]")
	private List<WebElement> usersAndRolesAllUsersRows;

	public void clickOnUsersAndRolesAddNewUserBtn() throws Exception {
		if (isElementLoaded(usersAndRolesAddNewUserButton)) {
			if (usersAndRolesAddNewUserButton.isDisplayed() && usersAndRolesAddNewUserButton.isEnabled()) {
				click(usersAndRolesAddNewUserButton);
				SimpleUtils.pass("Controls Page: 'Users & Roles' section 'Add New User' button clicked successfully.");
			} else
				SimpleUtils.report("Controls Page: 'Users & Roles' section 'Add New User' button not enabled.");
		} else
			SimpleUtils.report("Controls Page: 'Users & Roles' section 'Add New User' button not loaded.");
	}

	@FindBy(css = "div.lg-single-calendar-body")
	private WebElement calendarBody;

	@Override
	public HashMap<String, ArrayList<String>> getUsersAndRolesAddNewUserPageEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		Thread.sleep(1000);
		clickOnUsersAndRolesAddNewUserBtn();
		Thread.sleep(1000);
		if (isElementLoaded(addNewUsereditFormSection)) {
			List<WebElement> newUserFormFields = addNewUsereditFormSection.findElements(By.tagName("input-field"));
			if (newUserFormFields.size() > 0) {
				String fieldTitle = "";
				for (WebElement field : newUserFormFields) {
					if (field.isDisplayed()) {
						List<WebElement> fieldLabelDiv = field.findElements(By.cssSelector("label.input-label"));
						if (fieldLabelDiv.size() > 0) {
							fieldTitle = fieldLabelDiv.get(0).getText();
						}

						List<WebElement> inputBoxFields = field.findElements(
								By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
						boolean isFieldEditable = false;
						if (inputBoxFields.size() > 0) {
							if (fieldTitle.toLowerCase().contains("date hired")) {
								click(field);
								if (isElementLoaded(calendarBody)) {
									editableFields.add(fieldTitle);
									click(field);
								} else
									nonEditableFields.add(fieldTitle);
							} else {
								String fieldType = "input";
								isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
								if (isFieldEditable)
									editableFields.add(fieldTitle);
								else
									nonEditableFields.add(fieldTitle);
							}
						} else {
							inputBoxFields = field.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
							if (inputBoxFields.size() > 0) {
								String fieldType = "select";
								isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
								if (isFieldEditable)
									editableFields.add(fieldTitle);
								else
									nonEditableFields.add(fieldTitle);
							} else {
								inputBoxFields = field.findElements(By.cssSelector("lg-button-group[on-change=\"$ctrl.handleChange()\"]"));
								if (inputBoxFields.size() > 0) {
									String fieldType = "lg-button-group";
									isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
									if (isFieldEditable)
										editableFields.add(fieldTitle);
									else
										nonEditableFields.add(fieldTitle);
								} else
									notLoadedFields.add(fieldTitle);
							}
						}
					}
				}
				editableOrNonEditableFields.put("editableFields", editableFields);
				editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
				editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
			}
			clickOnUsersAndRolesNewUserEditPageCancelBtn();
		}
		return editableOrNonEditableFields;
	}

	@FindBy(css = "lg-button[label=\"Leave this page\"]")
	private WebElement leaveThisPageBtn;

	public void clickOnUsersAndRolesNewUserEditPageCancelBtn() throws Exception {
		if (cancelButtons.size() > 0) {
			click(cancelButtons.get(0));
			if (isElementLoaded(leaveThisPageBtn))
				click(leaveThisPageBtn);
			SimpleUtils.pass("Controls Page: Users and Roles Section 'New User edit page' Cancel button clicked successfully.");
		} else
			SimpleUtils.fail("Controls Page: Users and Roles Section 'New User edit page' 'Cancel' button not loaded.", false);
	}


	@FindBy(css = "lg-button[class=\"ml-20 mr-20 ng-scope ng-isolate-scope\"]")
	private WebElement userAndRolesEditUserBtn;

	@FindBy(css = "form-section[on-action=\"editUser()\"]")
	private WebElement editUserPageFormSection;

	@FindBy(css = "a[ng-click=\"$ctrl.back()\"]")
	private WebElement backNavigator;

	@Override
	public HashMap<String, ArrayList<String>> getUsersAndRolesEditUserPageEditableOrNonEditableFields(
			String userFirstName) throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();

		searchUserByFirstName(userFirstName);
		Thread.sleep(2000);
		if (usersAndRolesAllUsersRows.size() > 0) {
			List<WebElement> userDetailsLinks = usersAndRolesAllUsersRows.get(0).findElements(By.cssSelector("button[type='button']"));
			if (userDetailsLinks.size() > 0) {
				click(userDetailsLinks.get(0));
				if (isElementLoaded(userAndRolesEditUserBtn)) {
					click(userAndRolesEditUserBtn);
					if (isElementLoaded(editUserPageFormSection)) {
						List<WebElement> newUserFormFields = addNewUsereditFormSection.findElements(By.tagName("input-field"));
						if (newUserFormFields.size() > 0) {
							String fieldTitle = "";
							for (WebElement field : newUserFormFields) {
								if (field.isDisplayed()) {
									List<WebElement> fieldLabelDiv = field.findElements(By.cssSelector("label.input-label"));
									if (fieldLabelDiv.size() > 0) {
										fieldTitle = fieldLabelDiv.get(0).getText();
									}

									List<WebElement> inputBoxFields = field.findElements(
											By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
									boolean isFieldEditable = false;
									if (inputBoxFields.size() > 0) {
										if (fieldTitle.toLowerCase().contains("date hired")) {
											click(field);
											if (isElementLoaded(calendarBody)) {
												editableFields.add(fieldTitle);
												click(field);
											} else
												nonEditableFields.add(fieldTitle);
										} else {
											String fieldType = "input";
											isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
											if (isFieldEditable)
												editableFields.add(fieldTitle);
											else
												nonEditableFields.add(fieldTitle);
										}
									} else {
										inputBoxFields = field.findElements(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
										if (inputBoxFields.size() > 0) {
											String fieldType = "select";
											isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
											if (isFieldEditable)
												editableFields.add(fieldTitle);
											else
												nonEditableFields.add(fieldTitle);
										} else {
											inputBoxFields = field.findElements(By.cssSelector("lg-button-group[on-change=\"$ctrl.handleChange()\"]"));
											if (inputBoxFields.size() > 0) {
												String fieldType = "lg-button-group";
												isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
												if (isFieldEditable)
													editableFields.add(fieldTitle);
												else
													nonEditableFields.add(fieldTitle);
											} else
												notLoadedFields.add(fieldTitle);
										}
									}
								}
							}

							// Verify Add a Badges enable or not
							boolean isAddABadgesEnabled = isUsersAndRolesEditUserAddBadgesEnable();
							if (isAddABadgesEnabled)
								editableFields.add("Add a Badge");
							else
								nonEditableFields.add("Add a Badge");
						}
					}
				}
			}
			clickOnUsersAndRolesNewUserEditPageCancelBtn();
			if (isElementLoaded(backNavigator))
				click(backNavigator);
		}

		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);

		return editableOrNonEditableFields;
	}

	@FindBy(css = "input[placeholder*=\"You can search by name, job title\"]")
	private WebElement usersAndRolesUserSearchBox;
	@FindBy(css = "tr[ng-repeat=\"user in $ctrl.pagedUsers\"]")
	private List<WebElement> userRows;

	public void searchUserByFirstName(String userFirstName) throws Exception {
		if (isElementLoaded(usersAndRolesUserSearchBox)) {
			if (usersAndRolesUserSearchBox.isDisplayed() && usersAndRolesUserSearchBox.isEnabled()) {
				usersAndRolesUserSearchBox.clear();
				usersAndRolesUserSearchBox.sendKeys(userFirstName);
				waitForSeconds(30);
				if (userRows.size()>0) {
					SimpleUtils.pass("Users and Roles: '" + usersAndRolesAllUsersRows.size() + "' user(s) found with name '"
							+ userFirstName + "'");
				}else
					SimpleUtils.report("There is no user which match your search criteria");
			}
		}else
			SimpleUtils.fail("Search filed in global model in controls load failed",false);
	}

	//added by Estelle for update one user's location info
	@FindBy(css = "lg-button[label=\"Manage Locations\"] > button")
	private WebElement managerLocationBtn;
	@FindBy(css = "[modal-title=\"Manage Locations\"]")
	private WebElement managerLocationPopUpTitle;
	@FindBy(xpath = "(//lg-search//input)[1]")
	private WebElement managerLocationInputFiled;
	@FindBy(css = "tr[ng-repeat=\"item in $ctrl.filtered\"]")
	private List<WebElement> locationListRows;
	@FindBy(css = "lg-button[label=\"OK\"")
	private WebElement okayBtnInManagerLocationWin;
	@FindBy(xpath = "//lg-table-fixed-header/h2[1]/div/input-field")
	private WebElement selectAllCheckBoxInManaLocationWin;
	@FindBy(className = "lg-user-locations-new__item-name")
	private List<WebElement> defaultLocationsForOneUser;
	@FindBy(css = ".lg-select-list__thumbnail-wrapper>input-field[type=\"checkbox\"]")
	private List<WebElement> locationCheckBoxs;

	@Override
	public void verifyUpdateUserAndRolesOneUserLocationInfo(String userFirstName) throws Exception {
		searchUserByFirstName(userFirstName);
		waitForSeconds(2);
		if (usersAndRolesAllUsersRows.size() > 0) {
			List<WebElement> userDetailsLinks = usersAndRolesAllUsersRows.get(0).findElements(By.cssSelector("button[type='button']"));
			if (userDetailsLinks.size() > 0) {
				click(userDetailsLinks.get(0));
				if (isElementLoaded(userAndRolesEditUserBtn)) {
					click(userAndRolesEditUserBtn);
					String defaultLocation = getUserLocationsList();
					scrollToBottom();
					if (isElementLoaded(managerLocationBtn)) {
						scrollToElement(managerLocationBtn);
						click(managerLocationBtn);
						searchLocation("Level:Region");
						click(selectAllCheckBoxInManaLocationWin);
						if (locationCheckBoxs.size()>0) {
							SimpleUtils.pass("Can search upperfield of region");
							for (int i = 2; i <5 && i<locationCheckBoxs.size(); i++) {
								if (isElementLoaded(locationCheckBoxs.get(i)) && !locationCheckBoxs.get(i).getAttribute("class").contains("not-empty")) {
									click(locationCheckBoxs.get(i));
								}
							}
							scrollToBottom();
							click(okayBtnInManagerLocationWin);
							waitForSeconds(5);
						}else
							SimpleUtils.fail("There is no location ",true);
					}else
						SimpleUtils.fail("Manager location button load failed ",true);
					scrollToBottom();
					String  locationAfterUpdated = getUserLocationsList();
					if (!locationAfterUpdated.equals(defaultLocation)) {
						SimpleUtils.pass("User's location was updated successfully");
					}else
						SimpleUtils.fail("Manager location failed for this user",false);
					}else
					SimpleUtils.fail("User profile edit button load failed ",true);

				}
			}
		}


	@Override
	public void clickOnLocationsTabInGlobalModel() throws Exception {
		if (isElementLoaded(locationsSection,5)) {
			click(locationsSection);
		}else
			SimpleUtils.fail("LocationsSection load failed",true);
	}

	@FindBy(css = ".lg-tabs__nav >div:nth-child(2)")
	private WebElement allLocationsInLocations;
	@FindBy(css = "tr[ng-repeat=\"location in $ctrl.pagedLocations\"]")
	private List<WebElement> locationRows;
	@Override
	public List<String> getAllLocationsInGlobalModel() throws Exception {
		if (isElementLoaded(allLocationsInLocations,5)) {
			click(allLocationsInLocations);
			if (locationRows.size()>0) {
				List<String> locationList = new ArrayList<String>();
				for (WebElement location:locationRows
					 ) {
					locationList.add(location.findElement(By.cssSelector("td>lg-button > button>span >span")).getText());
				}
				return locationList;
				}else
					SimpleUtils.fail("There is no location",true);
 			}
		return null;
		}
	//added by Estelle
	@FindBy(css = "div.lg-paged-search-sortableNew")
	private List<WebElement> tableColumns;
	public void searchLocation(String userFirstName) throws Exception {
			List<String> tableHeaders =new ArrayList<>();
			for (WebElement tab:tableColumns) {
				tableHeaders.add(tab.getText());
				SimpleUtils.pass("Manager Location's table header include : " +tab.getText());
			}
			String tabHeaders = tableHeaders.toString();
			if (tabHeaders.contains("Level")) {
				SimpleUtils.pass("add level column in user management location search result list successfully");
			}
			if (isElementLoaded(managerLocationPopUpTitle,5)) {
				managerLocationInputFiled.sendKeys(userFirstName);
				managerLocationInputFiled.sendKeys(Keys.ENTER);
				waitForSeconds(10);
				if (locationListRows.size()>1) {
					SimpleUtils.pass("Manager Location: '" + locationListRows.size() + "' upperfield(s) found with name '"
							+ userFirstName + "'");
				}else
					SimpleUtils.report("There is no upperfield now, create it pls");

			}else
				SimpleUtils.fail("search input field load failed",false);
		}

		public String getUserLocationsList(){
			String resultString = "";
			List<String> userLocationListContext = new ArrayList<String>();
			for (WebElement location:defaultLocationsForOneUser) {
				userLocationListContext.add(location.getText());
				if(userLocationListContext ==null && userLocationListContext.size()<=0){
					SimpleUtils.report("userLocationListContext is null！");
				}else{
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<userLocationListContext.size();i++){
						if(i<userLocationListContext.size()-1){
							sb.append(userLocationListContext.get(i));
							sb.append(",");
						}else{
							sb.append(userLocationListContext.get(i));
						}
					}
					resultString = sb.toString();
				}
				return resultString;
			}

			return null;
		}

	@FindBy(css = "sub-content-box[box-title=\"Badges\"]")
	private WebElement editUserPageManageBadgeSection;
	@FindBy(css = "button.lgn-action-button-success")
	private WebElement addBadgeSaveBtn;
	@FindBy(css = "div[ng-click=\"callCancelCallback()\"]")
	private WebElement addBadgeCancelBtn;

	@FindBy(css = "div.lgn-tm-manage-badges")
	private WebElement badgesModelPopupWindow;

	public boolean isUsersAndRolesEditUserAddBadgesEnable() throws Exception {
		if (isElementLoaded(editUserPageManageBadgeSection)) {
			WebElement updateBadgesBtn;
			List<WebElement> addBadgeBtn = editUserPageManageBadgeSection.findElements(By.cssSelector("lg-button[label=\"Add a badge\"]"));
			if (addBadgeBtn.size() > 0)
				updateBadgesBtn = addBadgeBtn.get(0);
			else
				updateBadgesBtn = editUserPageManageBadgeSection.findElements(By.cssSelector("lg-button[label=\"Manage\"]")).get(0);
			click(updateBadgesBtn);
			if (isElementLoaded(badgesModelPopupWindow)) {
				List<WebElement> badgesRows = badgesModelPopupWindow.findElements(By.cssSelector("div.badge-row"));
				if (badgesRows.size() > 0) {
					for (WebElement badgesRow : badgesRows) {
						WebElement badgesRowCheckBox = badgesRow.findElement(By.cssSelector("div.lgnCheckBox"));
						if (isElementLoaded(badgesRowCheckBox) && !badgesRowCheckBox.getAttribute("class").contains("checked")) {
							click(badgesRowCheckBox);
							if (badgesRowCheckBox.getAttribute("class").contains("checked")) {
								if (isElementLoaded(addBadgeSaveBtn)) {
									click(addBadgeSaveBtn);
									return true;
								}
							}
						}
					}
				}
			}
		}
		if (isElementLoaded(badgesModelPopupWindow, 2)) {
			if (isElementLoaded(addBadgeCancelBtn))
				click(addBadgeCancelBtn);
		}
		return false;
	}


	@FindBy(css = "tr[ng-repeat=\"group in $ctrl.pagedGroups\"]")
	private List<WebElement> usersAndRolesEmployeeJobTitileRows;

	@Override
	public HashMap<String, ArrayList<String>> getUsersAndRolesUpdateEmployeeJobTitleEditableOrNonEditableFields(String employeeJobTitle) throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		searchEmployeeJobTitle(employeeJobTitle);
		if (usersAndRolesEmployeeJobTitileRows.size() > 0) {
			for (WebElement employeeJobTitileRow : usersAndRolesEmployeeJobTitileRows) {
				List<WebElement> newUserFormFields = employeeJobTitileRow.findElements(By.tagName("input-field"));
				if (newUserFormFields.size() > 0) {
					String fieldTitle = "";
					for (WebElement field : newUserFormFields) {
						if (field.isDisplayed()) {
							List<WebElement> fieldLabelDiv = field.findElements(By.cssSelector("label.input-label"));
							if (fieldLabelDiv.size() > 0) {
								fieldTitle = fieldLabelDiv.get(0).getText();
							}

							List<WebElement> inputBoxFields = field.findElements(
									By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
							boolean isFieldEditable = false;
							if (inputBoxFields.size() > 0) {
								String fieldType = "input";
								isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
								if (isFieldEditable)
									editableFields.add(fieldTitle);
								else
									nonEditableFields.add(fieldTitle);
							}
						}
					}
				}
				try {
					WebElement cancelButton = employeeJobTitileRow.findElement(By.cssSelector("lg-button[label=\"Cancel\"]"));
					WebElement saveButton = employeeJobTitileRow.findElement(By.cssSelector("lg-button[label=\"Save\"]"));
					if (isElementLoaded(saveButton) && saveButton.isDisplayed() && saveButton.isEnabled()) {
						editableFields.add(saveButton.getText());
					} else {
						nonEditableFields.add(saveButton.getText());
					}

					if (isElementLoaded(cancelButton) && cancelButton.isDisplayed() && cancelButton.isEnabled()) {
						editableFields.add(cancelButton.getText());
						click(cancelButton);
					} else {
						nonEditableFields.add(cancelButton.getText());
					}
				} catch (Exception e) {
					SimpleUtils.report("Users and Roles: Job Title '" + employeeJobTitle + "' not editable.");
				}

				break;
			}
		}
		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}

	@FindBy(css = "input-field[placeholder=\"You can search by employee job title.\"]")
	private WebElement usersAndRolesEmployeeJobTitleSearchBox;

	public void searchEmployeeJobTitle(String employeeJobTitle) throws Exception {
		if (isElementLoaded(usersAndRolesEmployeeJobTitleSearchBox)) {
			WebElement jobTitleTextBox = usersAndRolesEmployeeJobTitleSearchBox.findElement(By.cssSelector("input[type=\"text\"]"));
			if (isElementLoaded(jobTitleTextBox)) {
				if (jobTitleTextBox.isDisplayed() && jobTitleTextBox.isEnabled()) {
					jobTitleTextBox.clear();
					jobTitleTextBox.sendKeys(employeeJobTitle);
					Thread.sleep(2000);
					SimpleUtils.pass("Users and Roles: '" + usersAndRolesEmployeeJobTitileRows.size() + "' Employee Job Title(s) found with Title '"
							+ employeeJobTitle + "'");
				}
			}
		}
	}


	@FindBy(css = "lg-button[label=\"Add Job Title\"]")
	private WebElement usersAndRoleAddJobTitleBtn;

	public void clickOnAddJobTitleBtn() throws Exception {
		if (isElementLoaded(usersAndRoleAddJobTitleBtn)) {
			click(usersAndRoleAddJobTitleBtn);
			if (isNewJobTitleRowEditModeActive())
				SimpleUtils.pass("Users and Roles Section : 'Add New Title' button clicked successfully.");
			else
				SimpleUtils.fail("Users and Roles Section : Unable to click 'Add New Title' button.", true);
		} else
			SimpleUtils.report("Users and Roles Section : 'Add New Title' button not loaded.");
	}

	@FindBy(css = "tr[ng-if=\"$ctrl.addingTitle\"]")
	private WebElement addJobTitleEditRow;

	public boolean isNewJobTitleRowEditModeActive() throws Exception {
		if (isElementLoaded(addJobTitleEditRow))
			return true;
		return false;
	}

	@Override
	public HashMap<String, ArrayList<String>> getUsersAndRolesCreateNewEmployeeJobTitleEditableOrNonEditableFields(
			String employeeJobTitle, String newEmployeeJobTitleRole) throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		clickOnAddJobTitleBtn();
		if (isNewJobTitleRowEditModeActive()) {
			List<WebElement> newTitleFormFields = addJobTitleEditRow.findElements(By.tagName("input-field"));
			if (newTitleFormFields.size() > 0) {
				String fieldTitle = "";
				for (WebElement field : newTitleFormFields) {
					if (field.isDisplayed()) {
						List<WebElement> fieldLabelDiv = field.findElements(By.cssSelector("label.input-label"));
						if (fieldLabelDiv.size() > 0) {
							fieldTitle = fieldLabelDiv.get(0).getText();
						}

						List<WebElement> inputBoxFields = field.findElements(
								By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
						String fieldValueType = inputBoxFields.get(0).getAttribute("type");
						if (fieldValueType.equalsIgnoreCase("text")) {
							inputBoxFields.get(0).sendKeys(employeeJobTitle);
						}
						boolean isFieldEditable = false;
						if (inputBoxFields.size() > 0) {
							String fieldType = "input";
							isFieldEditable = isInputFieldEditable(inputBoxFields.get(0), fieldType);
							if (isFieldEditable)
								editableFields.add(fieldTitle);
							else
								nonEditableFields.add(fieldTitle);
						}
					}
				}
			}

			WebElement cancelButton = addJobTitleEditRow.findElement(By.cssSelector("lg-button[label=\"Cancel\"]"));
			WebElement saveButton = addJobTitleEditRow.findElement(By.cssSelector("lg-button[label=\"Save\"]"));
			if (isElementLoaded(saveButton) && saveButton.isDisplayed() && saveButton.isEnabled()) {
				editableFields.add(saveButton.getText());
			} else {
				nonEditableFields.add(saveButton.getText());
			}

			if (isElementLoaded(cancelButton) && cancelButton.isDisplayed() && cancelButton.isEnabled()) {
				editableFields.add(cancelButton.getText());
				click(cancelButton);
			} else {
				nonEditableFields.add(cancelButton.getText());
			}
		}
		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}

	@FindBy(css = "input-field[placeholder=\"You can search by name and description.\"]")
	private WebElement usersAndRolesBadgesSearchBox;

	public void searchBadgesByLabel(String badgeLabel) throws Exception {
		if (isElementLoaded(usersAndRolesBadgesSearchBox)) {
			WebElement badgesTextBox = usersAndRolesBadgesSearchBox.findElement(By.cssSelector("input[type=\"text\"]"));
			if (isElementLoaded(badgesTextBox)) {
				if (badgesTextBox.isDisplayed() && badgesTextBox.isEnabled()) {
					badgesTextBox.clear();
					badgesTextBox.sendKeys(badgeLabel);
					Thread.sleep(2000);
					SimpleUtils.pass("Users and Roles: '" + usersAndRolesEmployeeJobTitileRows.size() + "' Badges(s) found with Title '"
							+ badgeLabel + "'");
				}
			}
		}
	}

	@FindBy(css = "tr[ng-repeat=\"badge in $ctrl.pagedBadges\"]")
	private List<WebElement> usersAndRolesBadgesRows;

	@FindBy(css = "input-field[value=\"$ctrl.badgeCopy.displayName\"]")
	private WebElement badgeDisplayNameField;

	@FindBy(css = "input-field[value=\"$ctrl.badgeCopy.description\"]")
	private WebElement badgeDescriptionField;


	@FindBy(css = "lg-badge-icons[value=\"$ctrl.badgeCopy.iconName\"]")
	private WebElement badgesIconsSection;

	@FindBy(css = "lg-badge-colors[value=\"$ctrl.badgeCopy.hexColorCode\"]")
	private WebElement badgesColorsSection;

	/*
	@FindBy(tagName="lg-badge-selector-item")
	private List<WebElement> badgesIcons;
	
	@FindBy(tagName="lg-badge-selector-item")
	private List<WebElement> badgesColors;
	 */
	@Override
	public HashMap<String, ArrayList<String>> getUsersAndRolesUpdateBadgesEditableOrNonEditableFields(String badgesLabel) throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		searchBadgesByLabel(badgesLabel);
		if (usersAndRolesBadgesRows.size() > 0) {
			for (WebElement usersAndRolesBadgesRow : usersAndRolesBadgesRows) {
				try {
					WebElement openBadgeFormBtn = usersAndRolesBadgesRow.findElement(By.cssSelector("lg-button[ng-click=\"$ctrl.openBadge(badge)\"]"));
					if (isElementLoaded(openBadgeFormBtn)) {
						click(openBadgeFormBtn);
						if (isEditBadgePopupModalLoaded()) {
							List<WebElement> inputFields = editBadgePopUpModel.findElements(By.tagName("input-field"));
							for (WebElement inputField : inputFields) {
								if (inputField.isDisplayed()) {
									WebElement inputBox = inputField.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
									if (isElementLoaded(inputBox)) {
										String fieldType = "input";
										String fieldLabel = inputField.findElement(By.cssSelector("label.input-label")).getText();
										boolean isFieldEditable = isInputFieldEditable(inputBox, fieldType);
										if (isFieldEditable)
											editableFields.add(fieldLabel);
										else
											nonEditableFields.add(fieldLabel);
									}
								}
							}

							boolean isBadgeIconEditable = isEditBadgePopupBadgeIconEditable();
							String badgeIconLabel = badgesIconsSection.findElement(By.tagName("label")).getText();
							if (isBadgeIconEditable)
								editableFields.add(badgeIconLabel);
							else
								nonEditableFields.add(badgeIconLabel);

							boolean isBadgeColorEditable = isEditBadgePopupBadgeColorEditable();
							String badgeColorLabel = badgesColorsSection.findElement(By.tagName("label")).getText();
							if (isBadgeColorEditable)
								editableFields.add(badgeColorLabel);
							else
								nonEditableFields.add(badgeColorLabel);
						}
						clickOnCancelBtn();
						break;
					}
				} catch (Exception e) {
					SimpleUtils.report("Users and Roles: Badges not editable for logged user.");
				}
			}
		}

		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}

	public boolean isEditBadgePopupBadgeIconEditable() throws Exception {
		boolean isBadgeIconEditable = false;
		if (isElementLoaded(badgesIconsSection)) {
			List<WebElement> badgesIcons = badgesIconsSection.findElements(By.cssSelector("lg-badge-selector-item"));
			if (badgesIcons.size() > 0) {
				for (WebElement badgesIcon : badgesIcons) {
					if (!badgesIcon.getAttribute("class").contains("selected")) {
						click(badgesIcon);
						if (badgesIcon.getAttribute("class").contains("selected"))
							isBadgeIconEditable = true;
						break;
					}
				}
			}
		}
		return isBadgeIconEditable;
	}

	public boolean isEditBadgePopupBadgeColorEditable() throws Exception {
		boolean isBadgeColorEditable = false;
		if (isElementLoaded(badgesColorsSection)) {
			List<WebElement> badgesColors = badgesColorsSection.findElements(By.cssSelector("lg-badge-selector-item"));
			if (badgesColors.size() > 0) {
				for (WebElement badgesColor : badgesColors) {
					if (!badgesColor.getAttribute("class").contains("selected")) {
						click(badgesColor);
						if (badgesColor.getAttribute("class").contains("selected"))
							isBadgeColorEditable = true;
						break;
					}
				}
			}
		}
		return isBadgeColorEditable;
	}

	@FindBy(css = "modal[modal-title=\"Edit Badge\"]")
	private WebElement editBadgePopUpModel;

	public boolean isEditBadgePopupModalLoaded() throws Exception {
		if (isElementLoaded(editBadgePopUpModel))
			return true;
		return false;
	}


	@FindBy(css = "modal[modal-title=\"New Badge\"]")
	private WebElement newBadgePopUpModel;

	public boolean isNewBadgeEditPopupModalLoaded() throws Exception {
		if (isElementLoaded(newBadgePopUpModel))
			return true;
		return false;
	}

	@FindBy(css = "lg-button[label=\"Cancel\"]")
	private WebElement cancelBtn;

	public void clickOnCancelBtn() throws Exception {
		if (isElementLoaded(cancelBtn)) {
			scrollToElement(cancelBtn);
			clickTheElement(cancelBtn);
			SimpleUtils.pass("Cancel button clicked successfully.");
		} else
			SimpleUtils.report("Cancel Button not loaded.");
	}


	@FindBy(css = "lg-button[label=\"Add Badge\"]")
	private WebElement addBadgeBtn;

	public void clickOnAddBadgeButton() throws Exception {
		if (isElementLoaded(addBadgeBtn)) {
			click(addBadgeBtn);
			SimpleUtils.pass("Users and Roles: 'Add Badge' button clicked successfully.");
		} else
			SimpleUtils.report("Users and Roles: 'Add Badge' button not loaded.");
	}

	@Override
	public HashMap<String, ArrayList<String>> getUsersAndRolesNewBadgeEditableOrNonEditableFields() throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		clickOnAddBadgeButton();
		if (isNewBadgeEditPopupModalLoaded()) {

			List<WebElement> inputFields = newBadgePopUpModel.findElements(By.tagName("input-field"));
			for (WebElement inputField : inputFields) {
				if (inputField.isDisplayed()) {
					WebElement inputBox = inputField.findElement(By.cssSelector("input[ng-change=\"$ctrl.handleChange()\"]"));
					if (isElementLoaded(inputBox)) {
						String fieldType = "input";
						String fieldLabel = inputField.findElement(By.cssSelector("label.input-label")).getText();
						boolean isFieldEditable = isInputFieldEditable(inputBox, fieldType);
						if (isFieldEditable)
							editableFields.add(fieldLabel);
						else
							nonEditableFields.add(fieldLabel);
					}
				}
			}

			boolean isBadgeIconEditable = isEditBadgePopupBadgeIconEditable();
			String badgeIconLabel = badgesIconsSection.findElement(By.tagName("label")).getText();
			if (isBadgeIconEditable)
				editableFields.add(badgeIconLabel);
			else
				nonEditableFields.add(badgeIconLabel);

			boolean isBadgeColorEditable = isEditBadgePopupBadgeColorEditable();
			String badgeColorLabel = badgesColorsSection.findElement(By.tagName("label")).getText();
			if (isBadgeColorEditable)
				editableFields.add(badgeColorLabel);
			else
				nonEditableFields.add(badgeColorLabel);
			clickOnCancelBtn();
		}

		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}


	@Override
	public void selectTasksAndWorkRolesSubTabByLabel(String label) throws Exception {
		boolean isTabFound = false;
		if (subTabs.size() > 0) {
			for (WebElement subTab : subTabs) {
				if (subTab.getText().toLowerCase().contains(label.toLowerCase())) {
					click(subTab);
					isTabFound = true;
				}
			}
			if (isTabFound)
				SimpleUtils.pass("Controls Page: Tasks and Work Roles section - '" + label + "' tab selected successfully.");
			else
				SimpleUtils.fail("Controls Page: Tasks and Work Roles section - '" + label + "' tab not found.", true);
		} else
			SimpleUtils.fail("Controls Page: Tasks and Work Roles section - sub tabs not loaded.", false);
	}

	@FindBy(css = "tr[ng-repeat=\"workRole in $ctrl.workRoles\"]")
	private List<WebElement> workRolesRows;

	@Override
	public List<WebElement> getTasksAndWorkRolesSectionAllWorkRolesList() throws Exception {
		return workRolesRows;
	}

	@Override
	public HashMap<String, ArrayList<String>> getTasksAndWorkRolesEditWorkRolePropertiesEditableOrNonEditableFields()
			throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		List<WebElement> workRolesList = getTasksAndWorkRolesSectionAllWorkRolesList();
		if (workRolesList.size() > 0) {
			for (WebElement workRole : workRolesList) {
				List<WebElement> workRoleInfoBtns = workRole.findElements(By.tagName("lg-button"));
				if (workRoleInfoBtns.size() > 0) {
					click(workRoleInfoBtns.get(0));
					if (isRoleDetailsPageLoaded()) {
						clickOnEditWorkRolePencilIcon();
						if (isEditWorkRolePropertiesPopupLoaded()) {
							WebElement workRoleTitleInputBox = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input.setting-work-role-change-modal-title-input"));
							if (isElementLoaded(workRoleTitleInputBox)) {
								String workRoleTitleFieldType = "input";
								String workRoleTitleFieldLabel = "Work Role Title";
								boolean isworkRoleTitleEditable = isInputFieldEditable(workRoleTitleInputBox, workRoleTitleFieldType);
								if (isworkRoleTitleEditable)
									editableFields.add(workRoleTitleFieldLabel);
								else
									nonEditableFields.add(workRoleTitleFieldLabel);
							}

							WebElement toggleDemandDrivenMode = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("div[ng-click=\"toggleDemandDrivenMode()\"]"));
							if (isElementLoaded(toggleDemandDrivenMode)) {
								String fieldLabel = toggleDemandDrivenMode.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(toggleDemandDrivenMode, "div"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement toggleBudgetMode = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("div[ng-click=\"toggleBudgetMode()\"]"));
							if (isElementLoaded(toggleBudgetMode)) {
								String fieldLabel = toggleBudgetMode.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(toggleBudgetMode, "div"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement toggleShiftOfferMode = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("div[ng-click=\"toggleShiftOfferMode()\"]"));
							if (isElementLoaded(toggleShiftOfferMode)) {
								String fieldLabel = toggleShiftOfferMode.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(toggleShiftOfferMode, "div"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement toggleShiftSwapMode = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("div[ng-click=\"toggleShiftSwapMode()\"]"));
							if (isElementLoaded(toggleShiftSwapMode)) {
								String fieldLabel = toggleShiftSwapMode.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(toggleShiftSwapMode, "div"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement staffingOrder = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input[ng-model=\"staffingOrder\"]"));
							if (isElementLoaded(staffingOrder)) {
								String fieldLabel = staffingOrder.findElement(By.xpath("./../..")).getText();
								if (isInputFieldEditable(staffingOrder, "input"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement hourlyWage = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input[ng-model=\"hourlyWage\"]"));
							if (isElementLoaded(hourlyWage)) {
								String fieldLabel = hourlyWage.findElement(By.xpath("./../..")).getText();
								if (isInputFieldEditable(hourlyWage, "input"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement minShiftLength = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input[ng-model=\"shiftLength.minShiftLength\"]"));
							if (isElementLoaded(minShiftLength)) {
								String fieldLabel = "Shift Length (Minutes): From";//minShiftLength.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(minShiftLength, "input"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement maxShiftLength = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input[ng-model=\"shiftLength.maxShiftLength\"]"));
							if (isElementLoaded(maxShiftLength)) {
								String fieldLabel = "Shift Length (Minutes): To";//maxShiftLength.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(maxShiftLength, "input"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement openingOffset = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input[ng-model=\"offset.openingOffset\"]"));
							if (isElementLoaded(openingOffset)) {
								String fieldLabel = "Buffer (Minutes): Opening"; //openingOffset.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(openingOffset, "input"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement closingOffset = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input[ng-model=\"offset.closingOffset\"]"));
							if (isElementLoaded(closingOffset)) {
								String fieldLabel = "Buffer (Minutes): Closing";//closingOffset.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(closingOffset, "input"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement consistency = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("select[ng-model=\"consistency\"]"));
							if (isElementLoaded(consistency)) {
								String fieldLabel = "Consistency";//consistency.findElement(By.xpath("./..")).getText();
								if (isInputFieldEditable(consistency, "select"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							WebElement tolerance = editWorkRolePropertiesPopupBody.findElement(
									By.cssSelector("input[ng-model=\"tolerance\"]"));
							if (isElementLoaded(tolerance)) {
								String fieldLabel = tolerance.findElement(By.xpath("./../..")).getText();
								if (isInputFieldEditable(tolerance, "input"))
									editableFields.add(fieldLabel);
								else
									nonEditableFields.add(fieldLabel);
							}

							List<WebElement> workRoleColors = editWorkRolePropertiesPopupBody.findElements(
									By.cssSelector("div[ng-repeat=\"color in colors\"]"));
							String workRoleColorChooserLabel = "Work Role Color Chooser";
							if (workRoleColors.size() > 0) {
								for (WebElement workRoleColor : workRoleColors) {
									try {
										if (!workRoleColor.getAttribute("class").contains("selected")) {
											click(workRoleColor);
											if (workRoleColor.getAttribute("class").contains("selected"))
												editableFields.add(workRoleColorChooserLabel);
											break;
										}
									} catch (Exception e) {
										nonEditableFields.add(workRoleColorChooserLabel);
										break;
									}
								}
							}

							// Click on Cancel Button
							WebElement parentDiv = editWorkRolePropertiesPopupBody.findElement(By.xpath("./../.."));
							WebElement cancelBtn = parentDiv.findElement(By.cssSelector("button[ng-click=\"cancelClicked()\"]"));
							if (isElementLoaded(cancelBtn)) {
								click(cancelBtn);
								SimpleUtils.pass("Tasks and Work Roles Section: 'Edit Work Role Properties Popup' Cancel button clicked successfully.");
							} else {
								SimpleUtils.fail("Tasks and Work Roles Section: 'Edit Work Role Properties Popup' Cancel button not loaded.", true);
							}
						} else
							SimpleUtils.report("Tasks and Work Roles Section: 'Edit Work Role Properties Popup' not loaded.");
					} else
						SimpleUtils.report("Tasks and Work Roles Section: 'Work Roles Details' page not loaded.");
					break;
				}
			}
		} else {
			SimpleUtils.report("Tasks and Work Roles Section: 'Work Roles' Tab No Work Role found.");
		}
		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}

	@FindBy(css = "page-heading[page-title=\"Work Role Details\"]")
	private WebElement workRoleDetailsPageHeader;

	public boolean isRoleDetailsPageLoaded() throws Exception {
		boolean isPageLoaded = false;
		if (isElementLoaded(workRoleDetailsPageHeader)) {
			isPageLoaded = true;
			SimpleUtils.pass("Tasks and Work Roles Section: 'Work Roles Details' page loaded successfuly.");
		}
		return isPageLoaded;
	}

	@FindBy(css = "span[ng-click=\"editWorkRole()\"]")
	private WebElement editWorkRolePencilIcon;

	public void clickOnEditWorkRolePencilIcon() throws Exception {
		if (isElementLoaded(editWorkRolePencilIcon)) {
			click(editWorkRolePencilIcon);
		} else
			SimpleUtils.report("Tasks and Work Roles Section: 'Work Roles Details' page Edit Work Role icon not loaded.");
	}

	@FindBy(css = "div.setting-work-role-change-modal-body")
	private WebElement editWorkRolePropertiesPopupBody;

	public boolean isEditWorkRolePropertiesPopupLoaded() throws Exception {
		if (isElementLoaded(editWorkRolePropertiesPopupBody))
			return true;
		return false;
	}

	@FindBy(css = "div.settings-work-role-details-edit-title-container")
	private List<WebElement> workRoleDetailsEditTitleContainers;

	@Override
	public boolean isWorkRoleDetailPageSubSectionsExpandFunctionalityWorking() throws Exception {
		boolean isExpandWorking = false;
		if (isElementLoaded(workRoleDetailsPageHeader)) {
			if (workRoleDetailsEditTitleContainers.size() > 0) {
				boolean isValueTrue = true;
				for (WebElement workRoleDetailsEditTitleContainer : workRoleDetailsEditTitleContainers) {
					String subSectionClass = workRoleDetailsEditTitleContainer.getAttribute("class");
					click(workRoleDetailsEditTitleContainer);
					if (!subSectionClass.equals(workRoleDetailsEditTitleContainer.getAttribute("class")))
						isValueTrue = isValueTrue && true;
					else {
						isValueTrue = isValueTrue && false;
						SimpleUtils.report("Tasks and Work Roles Section: 'Work Roles Details' page details section '"
								+ workRoleDetailsEditTitleContainer.getText() + "' not Expending");
					}
				}
				isExpandWorking = isValueTrue;
			} else
				SimpleUtils.fail("Tasks and Work Roles Section: 'Work Roles Details' page No Details section found.", true);
		}
		return isExpandWorking;
	}

	@FindBy(css = "div.labor-calculation")
	private WebElement laborCalculationSectionDiv;

	@Override
	public boolean isLaborCalculationTabLoaded() throws Exception {
		if (isElementLoaded(laborCalculationSectionDiv)) {
			SimpleUtils.pass("Tasks and Work Roles Section: 'Labor Calculation' Tab selected.");
			return true;
		}
		return false;
	}


	@Override
	public HashMap<String, ArrayList<String>> getTasksAndWorkRolesLaborCalculatorTabEditableOrNonEditableFields()
			throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		if (isLaborCalculationTabLoaded()) {
			String setLocationDropDownLabel = "Select a location";
			WebElement laborCalculatorSetLocationDropDown = laborCalculationSectionDiv.findElement(
					By.cssSelector("lg-select[on-change=\"setLocation(value)\"]"));
			boolean isSelectLocationButtonEditable = isTaskAndWorkRolesSelectLocationButtonEditable(laborCalculatorSetLocationDropDown);
			if (isSelectLocationButtonEditable)
				editableFields.add(setLocationDropDownLabel);
			else
				nonEditableFields.add(setLocationDropDownLabel);

			String categoriesItemsLabel = "Set item totals for categories";
			Thread.sleep(1000);
			List<WebElement> itemsForCategories = laborCalculationSectionDiv.findElements(
					By.cssSelector("input[type='number']"));
			boolean isCategoriesItemsEditable = true;
			for (WebElement itemForCategories : itemsForCategories) {
				if (itemForCategories.isDisplayed()) {
					isCategoriesItemsEditable = isCategoriesItemsEditable && isInputFieldEditable(itemForCategories, "input");
				}
			}
			if (itemsForCategories.size() > 0 && isCategoriesItemsEditable)
				editableFields.add(categoriesItemsLabel);
			else
				nonEditableFields.add(categoriesItemsLabel);

			String computeLaborHrBtnText = "Compute Labor Hours";
			try {
				WebElement laborCalculatorComputeLaborHrBtn = laborCalculationSectionDiv.findElement(
						By.cssSelector("lg-button[ng-click=\"computeLabor($event)\"]"));
				click(laborCalculatorComputeLaborHrBtn);
				editableFields.add(computeLaborHrBtnText);
			} catch (Exception e) {
				nonEditableFields.add(computeLaborHrBtnText);
			}

		}

		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}

	@FindBy(css = "div[ng-repeat=\"option in $ctrl.displayOptions\"]")
	private List<WebElement> locationOptions;

	public boolean isTaskAndWorkRolesSelectLocationButtonEditable(WebElement laborCalculatorSetLocationDropDown) throws Exception {
		if (isElementLoaded(laborCalculatorSetLocationDropDown)) {
			try {
				click(laborCalculatorSetLocationDropDown);
				if (locationOptions.size() > 0) {
					for (WebElement locationOption : locationOptions) {
						if (locationOption.isEnabled() && locationOption.isDisplayed()) {
							if (!locationOption.getAttribute("class").contains("selected")) {
								click(locationOption);
								return true;
							}
						}
					}
				}

			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	@FindBy(css = "lg-button[label=\"Add Work Role\"]")
	private WebElement taskAndWorkRolesAddWorkRoleBtn;

	public void clickOnTaskAndWorkRolesAddWorkRoleBtn() throws Exception {
		if (isElementLoaded(taskAndWorkRolesAddWorkRoleBtn)) {
			click(taskAndWorkRolesAddWorkRoleBtn);
			SimpleUtils.pass("Tasks and Work Roles: 'Work Roles' section 'Add Work Role' button clicked.");
		} else
			SimpleUtils.report("Tasks and Work Roles: 'Work Roles' section 'Add Work Role' button not loaded.");
	}

	@FindBy(css = "div.setting-create-work-role-container")
	private WebElement createWorkRoleContainerDiv;

	public boolean isTaskAndWorkRolesAddWorkRolePageLoaded() throws Exception {
		if (isElementLoaded(createWorkRoleContainerDiv))
			return true;
		return false;
	}


	@FindBy(css = "div[ng-click=\"editRoleColor()\"]")
	private WebElement workRoleColorSelectorDiv;
	@FindBy(css = "span[ng-click=\"selectColor($index)\"]")
	private List<WebElement> workRoleColors;
	@FindBy(css = "input.setting-work-role-name-title-input")
	private WebElement workRoleNameInputBox;
	@FindBy(css = "div[ng-click=\"toggleDemandDrivenMode()\"]")
	private WebElement workRoleDemandDrivenCheckBox;
	@FindBy(css = "div[ng-click=\"toggleShiftOfferMode()\"]")
	private WebElement workRoleAllowShiftOfferCheckBox;
	@FindBy(css = "div[ng-click=\"toggleShiftSwapMode()\"]")
	private WebElement workRoleAllowShiftSwapCheckBox;
	@FindBy(css = "input[ng-model=\"role.minShiftLength\"]")
	private WebElement workRoleShiftLengthLimitsMin;
	@FindBy(css = "input[ng-model=\"role.maxShiftLength\"]")
	private WebElement workRoleShiftLengthLimitsMax;
	@FindBy(css = "select.setting-work-role-shift-length-input")
	private WebElement workRoleConsistencyDropDown;
	@FindBy(css = "div[ng-click=\"role.hasBudget = !role.hasBudget\"]")
	private WebElement workRoleApplyBudgetCheckBox;
	@FindBy(css = "input[ng-model=\"role.hourlyWage\"]")
	private WebElement workRoleHourlyRateField;
	@FindBy(css = "button.wrc-save-cancel-btn")
	private WebElement workRoleCancelBtn;

	@Override
	public HashMap<String, ArrayList<String>> getTasksAndWorkRolesAddWorkRolePageEditableOrNonEditableFields()
			throws Exception {
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		clickOnTaskAndWorkRolesAddWorkRoleBtn();
		if (isTaskAndWorkRolesAddWorkRolePageLoaded()) {

			// Work Role Color Selector
			String workRoleColorSelectorLabel = "SELECT A ROLE COLOR";
			try {
				click(workRoleColorSelectorDiv);
				Thread.sleep(1000);
				if (workRoleColors.size() > 0) {
					click(workRoleColors.get(0));
					editableFields.add(workRoleColorSelectorLabel);
				} else
					nonEditableFields.add(workRoleColorSelectorLabel);
			} catch (Exception e) {
				nonEditableFields.add(workRoleColorSelectorLabel);
			}

			// Work Role Name Field
			try {
				boolean isWorkRoleNameFieldEditable = isInputFieldEditable(workRoleNameInputBox, "input");
				if (isWorkRoleNameFieldEditable)
					editableFields.add(workRoleColorSelectorLabel);
				else
					nonEditableFields.add(workRoleColorSelectorLabel);
			} catch (Exception e) {
				nonEditableFields.add(workRoleColorSelectorLabel);
			}

			// Work Role Demand-driven Check Box
			String demandDrivenCheckBoxLabel = "Demand-driven";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleDemandDrivenCheckBox, "div");
				if (isFieldEditable)
					editableFields.add(demandDrivenCheckBoxLabel);
				else
					nonEditableFields.add(demandDrivenCheckBoxLabel);
			} catch (Exception e) {
				nonEditableFields.add(demandDrivenCheckBoxLabel);
			}

			// Work Role Allow Shift Offer Check Box
			String allowShiftOfferFieldLabel = "Allow Shift Offer";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleAllowShiftOfferCheckBox, "div");
				if (isFieldEditable)
					editableFields.add(allowShiftOfferFieldLabel);
				else
					nonEditableFields.add(allowShiftOfferFieldLabel);
			} catch (Exception e) {
				nonEditableFields.add(allowShiftOfferFieldLabel);
			}

			// Work Role Allow Shift Swap Check Box
			String allowShiftSwapFieldLabel = "Allow Shift Swap";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleAllowShiftSwapCheckBox, "div");
				if (isFieldEditable)
					editableFields.add(allowShiftSwapFieldLabel);
				else
					nonEditableFields.add(allowShiftSwapFieldLabel);
			} catch (Exception e) {
				nonEditableFields.add(allowShiftSwapFieldLabel);
			}

			// Work Role Specify Shift Length Limits Min
			String shiftLengthLimitsMinLabel = "Shift Length Limits Min";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleShiftLengthLimitsMin, "input");
				if (isFieldEditable)
					editableFields.add(shiftLengthLimitsMinLabel);
				else
					nonEditableFields.add(shiftLengthLimitsMinLabel);
			} catch (Exception e) {
				nonEditableFields.add(shiftLengthLimitsMinLabel);
			}

			// Work Role Specify Shift Length Limits Max
			String shiftLengthLimitsMaxLabel = "Shift Length Limits Max";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleShiftLengthLimitsMax, "input");
				if (isFieldEditable)
					editableFields.add(shiftLengthLimitsMaxLabel);
				else
					nonEditableFields.add(shiftLengthLimitsMaxLabel);
			} catch (Exception e) {
				nonEditableFields.add(shiftLengthLimitsMaxLabel);
			}

			// Work Role Consistency DropDown
			String consistencyLabel = "Consistency";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleConsistencyDropDown, "select");
				if (isFieldEditable)
					editableFields.add(consistencyLabel);
				else
					nonEditableFields.add(consistencyLabel);
			} catch (Exception e) {
				nonEditableFields.add(consistencyLabel);
			}

			// Work Role Apply Budget Checkbox
			String applyBudgetLabel = "Apply Budget";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleApplyBudgetCheckBox, "div");
				if (isFieldEditable)
					editableFields.add(applyBudgetLabel);
				else
					nonEditableFields.add(applyBudgetLabel);
			} catch (Exception e) {
				nonEditableFields.add(applyBudgetLabel);
			}

			// Work ROle Color Selector
			String hourlyRateLabel = "Hourly rate";
			try {
				boolean isFieldEditable = isInputFieldEditable(workRoleHourlyRateField, "input");
				if (isFieldEditable)
					editableFields.add(hourlyRateLabel);
				else
					nonEditableFields.add(hourlyRateLabel);
			} catch (Exception e) {
				nonEditableFields.add(hourlyRateLabel);
			}

			// Work Role Cancel Button
			try {
				click(workRoleCancelBtn);
				SimpleUtils.pass("Tasks and Work Roles 'Add Work Role' page 'Cancel' button clicked successfuly.");
			} catch (Exception e) {
				SimpleUtils.fail("Tasks and Work Roles 'Add Work Role' page 'Cancel' button not loaded.", true);
			}
		}

		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}


	@FindBy(css = "div.modal-content")
	private WebElement modalPopupDiv;

	@Override
	public HashMap<String, ArrayList<String>> verifyUpdateControlsRegularHoursPopupEditableOrNonEditableFields()
			throws Exception {
		Thread.sleep(1000);
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		WebElement collapsibleHeader = regularHoursBlock.findElement(By.cssSelector("div.collapsible.row"));
		boolean isRegularHoursSectionOpened = collapsibleHeader.getAttribute("class").contains("open");

		if (!isRegularHoursSectionOpened)
			click(regularHoursBlock);

		if (regularHoursRows.size() > 0) {
			try {
				for (WebElement regularHoursRow : regularHoursRows) {
					String storeClosedLabel = "Store Closed Toggle";
					WebElement isClosedToggle = regularHoursRow.findElement(By.cssSelector("div[ng-show=\"isEditMode\"]"));
					boolean isStoreClosedEditable = isInputFieldEditable(isClosedToggle, "div");
					if (isStoreClosedEditable)
						editableFields.add(storeClosedLabel);
					else
						nonEditableFields.add(storeClosedLabel);
					WebElement regularHoursEditBtn = regularHoursRow.findElement(By.cssSelector("lg-button[label=\"Edit\"]"));
					if (isElementLoaded(regularHoursEditBtn)) {
						Thread.sleep(1000);
						click(regularHoursEditBtn);
						// Select Opening Hours
						int openingHourOnSlider = Integer.valueOf(editRegularHoursSliders.get(0).getText().split(":")[0].trim());
						int sliderOffSet = 50;
						String openingHoursBeforeUpdate = editRegularHoursSliders.get(0).getText();
						moveDayViewCards(editRegularHoursSliders.get(0), sliderOffSet);
						String openingHoursAfterUpdate = editRegularHoursSliders.get(0).getText();
						sliderOffSet = -50;

						String openingHoursSliderLabel = "Opening Hours";
						if (!openingHoursBeforeUpdate.equals(openingHoursAfterUpdate))
							editableFields.add(openingHoursSliderLabel);
						else
							nonEditableFields.add(openingHoursSliderLabel);

						// Select Closing Hours
						String closingHoursSliderLabel = "Closing Hours";
						String closingHoursBeforeUpdate = editRegularHoursSliders.get(1).getText();
						moveDayViewCards(editRegularHoursSliders.get(1), sliderOffSet);
						String closingHoursAfterUpdate = editRegularHoursSliders.get(1).getText();
						if (!closingHoursBeforeUpdate.equals(closingHoursAfterUpdate))
							editableFields.add(closingHoursSliderLabel);
						else
							nonEditableFields.add(closingHoursSliderLabel);

						String applyHrToOtherDaysLabel = "Apply these hours to other days";
						List<WebElement> weekdaysCheckBoxs = modalPopupDiv.findElements(By.cssSelector("input-field[type=\"checkbox\"]"));
						if (weekdaysCheckBoxs.size() > 0) {
							boolean isOtherWeekDaysCheckBoxEditable = true;
							for (WebElement weekdaysCheckBox : weekdaysCheckBoxs) {
								isOtherWeekDaysCheckBoxEditable = isOtherWeekDaysCheckBoxEditable && isInputFieldEditable(weekdaysCheckBox, "div");
							}
							if (isOtherWeekDaysCheckBoxEditable)
								editableFields.add(applyHrToOtherDaysLabel);
							else
								nonEditableFields.add(applyHrToOtherDaysLabel);
						}

						// Click On Cancel Button
						try {
							WebElement cancelBtn = modalPopupDiv.findElement(By.cssSelector("lg-button[label=\"Cancel\"]"));
							click(cancelBtn);
							SimpleUtils.pass("Controls Working Hours Section: Regular Hours 'Edit' popup 'Cancel' button clicked.");
						} catch (Exception e) {
							SimpleUtils.fail("Controls Working Hours Section: Regular Hours 'Edit' popup 'Cancel' button not clicked.", true);
						}
					} else {
						SimpleUtils.fail("Controls Working Hours Section: Regular Hours 'Edit' Button not loaded.", true);
					}
					break;
				}
			} catch (Exception e) {
				SimpleUtils.report("Controls Working Hours Section: Regular Hours not editable.");
			}
		} else {
			SimpleUtils.fail("Controls Working Hours Section: Regular Hours not loaded.", true);
		}
		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}


	@Override
	public HashMap<String, ArrayList<String>> verifyUpdateControlsHolidayHoursPopupEditableOrNonEditableFields()
			throws Exception {
		Thread.sleep(1000);
		HashMap<String, ArrayList<String>> editableOrNonEditableFields = new HashMap<String, ArrayList<String>>();
		ArrayList<String> editableFields = new ArrayList<String>();
		ArrayList<String> nonEditableFields = new ArrayList<String>();
		ArrayList<String> notLoadedFields = new ArrayList<String>();
		WebElement collapsibleHeader = holidayHoursBlock.findElement(By.cssSelector("div.collapsible.row"));
		boolean isHolidayHoursSectionOpened = collapsibleHeader.getAttribute("class").contains("open");

		if (!isHolidayHoursSectionOpened)
			click(holidayHoursBlock);

		if (regularHoursRows.size() > 0) {
			try {
				for (WebElement holidayHoursRow : holidayHoursRows) {
					String storeClosedLabel = "Store Closed Toggle";
					WebElement isClosedToggle = holidayHoursRow.findElement(By.cssSelector("div[ng-show=\"isEditMode\"]"));
					boolean isStoreClosedEditable = isInputFieldEditable(isClosedToggle, "div");
					if (isStoreClosedEditable)
						editableFields.add(storeClosedLabel);
					else
						nonEditableFields.add(storeClosedLabel);
					WebElement regularHoursEditBtn = holidayHoursRow.findElement(By.cssSelector("lg-button[label=\"Edit\"]"));
					if (isElementLoaded(regularHoursEditBtn)) {
						Thread.sleep(1000);
						click(regularHoursEditBtn);
						// Select Opening Hours
						int openingHourOnSlider = Integer.valueOf(editRegularHoursSliders.get(0).getText().split(":")[0].trim());
						int sliderOffSet = 50;
						String openingHoursBeforeUpdate = editRegularHoursSliders.get(0).getText();
						moveDayViewCards(editRegularHoursSliders.get(0), sliderOffSet);
						String openingHoursAfterUpdate = editRegularHoursSliders.get(0).getText();
						sliderOffSet = -50;

						String openingHoursSliderLabel = "Opening Hours";
						if (!openingHoursBeforeUpdate.equals(openingHoursAfterUpdate))
							editableFields.add(openingHoursSliderLabel);
						else
							nonEditableFields.add(openingHoursSliderLabel);

						// Select Closing Hours
						String closingHoursSliderLabel = "Closing Hours";
						String closingHoursBeforeUpdate = editRegularHoursSliders.get(1).getText();
						moveDayViewCards(editRegularHoursSliders.get(1), sliderOffSet);
						String closingHoursAfterUpdate = editRegularHoursSliders.get(1).getText();
						if (!closingHoursBeforeUpdate.equals(closingHoursAfterUpdate))
							editableFields.add(closingHoursSliderLabel);
						else
							nonEditableFields.add(closingHoursSliderLabel);

						String applyHrToOtherDaysLabel = "Apply these hours to other days";
						List<WebElement> weekdaysCheckBoxs = modalPopupDiv.findElements(By.cssSelector("input-field[type=\"checkbox\"]"));
						if (weekdaysCheckBoxs.size() > 0) {
							boolean isOtherWeekDaysCheckBoxEditable = true;
							for (WebElement weekdaysCheckBox : weekdaysCheckBoxs) {
								isOtherWeekDaysCheckBoxEditable = isOtherWeekDaysCheckBoxEditable && isInputFieldEditable(weekdaysCheckBox, "div");
							}
							if (isOtherWeekDaysCheckBoxEditable)
								editableFields.add(applyHrToOtherDaysLabel);
							else
								nonEditableFields.add(applyHrToOtherDaysLabel);
						}

						// Click On Cancel Button
						try {
							WebElement cancelBtn = modalPopupDiv.findElement(By.cssSelector("lg-button[label=\"Cancel\"]"));
							click(cancelBtn);
							SimpleUtils.pass("Controls Working Hours Section: Holiday Hours 'Edit' popup 'Cancel' button clicked.");
						} catch (Exception e) {
							SimpleUtils.fail("Controls Working Hours Section: Holiday Hours 'Edit' popup 'Cancel' button not clicked.", true);
						}
					} else {
						SimpleUtils.fail("Controls Working Hours Section: Holiday Hours 'Edit' Button not loaded.", true);
					}
					break;
				}
			} catch (Exception e) {
				SimpleUtils.report("Controls Working Hours Section: Holiday Hours not editable.");
			}
		} else {
			SimpleUtils.fail("Controls Working Hours Section: Regular Hours not loaded.", true);
		}
		editableOrNonEditableFields.put("editableFields", editableFields);
		editableOrNonEditableFields.put("nonEditableFields", nonEditableFields);
		editableOrNonEditableFields.put("notLoadedFields", notLoadedFields);
		return editableOrNonEditableFields;
	}


	public void displaySuccessMessage() throws Exception {
		if (isElementLoaded(successMsg, 20) && successMsg.getText().contains("Success!")) {
			SimpleUtils.pass("Success message displayed successfully." + successMsg.getText());
			waitForSeconds(2);
		} else {
			SimpleUtils.report("Success pop up not displayed successfully.");
			waitForSeconds(3);
		}
	}


	public void verifyAllLocations(String txt) throws Exception {
		if (isElementEnabled(linkAllLocations, 5)) {
			if (linkAllLocations.getAttribute("value").equalsIgnoreCase(txt)) {
				SimpleUtils.pass("On click over Global in breadcrumb it should display " + linkAllLocations.getAttribute("value") + " in breadcrumb");
			} else {
				SimpleUtils.fail("On click over Global, in breadcrumb " + txt + " not matched with " + linkAllLocations.getAttribute("value"), true);
			}
		} else {
			SimpleUtils.fail("On click over Global, All Locations not displayed successfully.", true);
		}
	}


	public void verifySearchLocations(String searchText) throws Exception {
		if (isElementEnabled(allLocations, 5)) {
			click(allLocations);
			if (isElementEnabled(searchLocation, 5)) {
				SimpleUtils.pass("On click over All Locations in breadcrumb it should display flyout with search text box");
				verifyImageAndLocationExistance();
				enterTextOnSearchLocation(searchText);
			} else {
				SimpleUtils.fail("On click over All Locations in breadcrumb flyout with search text box does not get displayed", true);
			}
		} else {
			SimpleUtils.fail("All Locations link is not clickable", true);
		}
	}


	public void verifyImageAndLocationExistance() throws Exception {
		if (areListElementVisible(imageIcon, 5) && areListElementVisible(locationName, 5)) {
			for (int i = 0; i < locationName.size(); i++) {
				if (imageIcon.get(0).getAttribute("style") != "") {
					SimpleUtils.pass("Image icon and Location are visible on page for " + locationName.get(i).getText());
				}
			}
		} else {
			SimpleUtils.fail("Image icon and location not visible on page ", true);
		}
	}

	public void enterTextOnSearchLocation(String searchText) throws Exception {
		searchLocation.sendKeys(searchText);
		if (areListElementVisible(imageIcon, 5) && areListElementVisible(locationName, 5)) {
			for (int i = 0; i < locationName.size(); i++) {
				if (locationName.get(i).getText().toLowerCase().contains(searchText.toLowerCase())) {
					SimpleUtils.pass("Location name is " + locationName.get(i).getText() + " which matches with Search box text " + searchText);
					click(locationName.get(i));
					break;
				} else {
					SimpleUtils.fail("Location name " + locationName.get(i).getText() + " not mathcing with Search box text " + searchText, true);
				}
			}

		} else {
			SimpleUtils.fail("Image icon and location not visible on page ", true);
		}
	}


	public boolean isControlsCompanyProfileCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(companyProfileCard, 5)) {
			SimpleUtils.pass("Controls Page: Company Profile Card Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: Company Profile Card not Loaded!", false);
		}
		return flag;
	}

	public boolean isControlsSchedulingPoliciesCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(schedulingPoliciesCard, 5)) {
			SimpleUtils.pass("Controls Page: Scheduling Policies Card Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: Scheduling Policies Card not Loaded!", false);
		}
		return flag;
	}

	public boolean isControlsSchedulingCollaborationCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(scheduleCollaborationSection, 5)) {
			SimpleUtils.pass("Controls Page: Scheduling Collaboration Section Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: Scheduling Collaboration Card not Loaded!", false);
		}
		return flag;
	}

	public boolean isControlsComplianceCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(complianceSection, 10)) {
			SimpleUtils.pass("Controls Page: Compliance Section Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: Compliance Section not Loaded!", false);
		}
		return flag;
	}

	public boolean isControlsUsersAndRolesCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(usersAndRolesSection, 5)) {
			SimpleUtils.pass("Controls Page: User and Roles Section Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: User and Roles Section not Loaded!", false);
		}
		return flag;
	}

	public boolean isControlsTaskAndWorkRolesCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(tasksAndWorkRolesSection, 5)) {
			SimpleUtils.pass("Controls Page: Task And Work Roles Section Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: Task And Work Roles not Loaded!", false);
		}
		return flag;
	}

	public boolean isControlsWorkingHoursCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(workingHoursCard, 5)) {
			SimpleUtils.pass("Controls Page: Working Hours Section Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: Working Hours Card not Loaded!", false);
		}
		return flag;
	}

	public boolean isControlsLocationsCard() throws Exception {
		boolean flag = false;
		if (isElementLoaded(locationsSection, 5)) {
			SimpleUtils.pass("Controls Page: Location Section Loaded Successfully!");
			flag = true;
		} else {
			SimpleUtils.fail("Controls Page: Locations Card not Loaded!", false);
		}
		return flag;
	}


	@FindBy(xpath = "//h3[contains(text(),'publish schedules')]")
	private WebElement schedulePublishWindowText;

	public void verifySchedulePublishWindow(String publishWindowAdvanceWeeks, String publishWindowQuestion, String userCredential) throws Exception {
		String fieldType = "select";

		if (isElementLoaded(schedulingPoliciesSchedulesFormSectionDiv, 5)) {
			if (isElementLoaded(schedulePublishWindowText, 5)) {
				if (schedulePublishWindowText.getText().equalsIgnoreCase(publishWindowQuestion)) {
					SimpleUtils.pass("Scheduling Policies: Schedule Publish Window question matches with expected value " + publishWindowQuestion);
				} else {
					SimpleUtils.fail("Scheduling Policies: Schedule Publish Window question not matched with expected value " + schedulePublishWindowText.getText(), true);
				}
				if (isElementLoaded(schedulePublishWindowDiv, 5)) {
					WebElement schedulePublishWindowDropDown = schedulePublishWindowDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					if (isElementLoaded(schedulePublishWindowDropDown, 5)) {
						boolean isFieldEditable = isInputFieldEditable(schedulePublishWindowDropDown, fieldType);
						isFieldEditableBasedOnAccess(userCredential, isFieldEditable);
					} else
						SimpleUtils.fail("Scheduling Policies: Schedule weeks to be published dropdown not loaded.", false);
				} else
					SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize section not loaded.", false);
			} else {
				SimpleUtils.fail("Controls Page: - Scheduling Policies : Schedule publish window Question section not loaded.", false);
			}

		} else {
			SimpleUtils.fail("Controls Page: - Scheduling Policies section: 'Schedules' form section not loaded.", false);
		}

	}


	public void isFieldEditableBasedOnAccess(String access, boolean isFieldEditable) throws Exception {
		if (isFieldEditable && access.equalsIgnoreCase("InternalAdmin")) {
			SimpleUtils.pass("Scheduling Policies: Fields are editable for " + access);
		} else if (!isFieldEditable && access.equalsIgnoreCase("StoreManager")) {
			SimpleUtils.pass("Scheduling Policies: Fields are not editable for " + access);
		} else {
			SimpleUtils.fail("Scheduling Policies: Fields are not editable", true);
		}
	}


	public void getSchedulePublishWindowWeeksDropDownValues() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(schedulePublishWindowDiv, 5)) {
			WebElement schedulePublishWindowDropDown = schedulePublishWindowDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(schedulePublishWindowDropDown, 5)) {
				Select dropdown = new Select(schedulePublishWindowDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
				for (WebElement option : dropdown.getOptions()) {
					SimpleUtils.pass("It should be a dropdown field with values " + option.getText());
				}
			} else
				SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize section not loaded.", false);

	}

	@FindBy(xpath = "//lg-location-chooser//lg-select[2]/div//div/input-field/ng-form/div")
	private WebElement differentLocations;

	public List<String> getSchedulePublishWindowValueAtDifferentLocations(
			boolean schedulePublishWindowWeeks) throws Exception {
		List<String> selectionOptionLabel = new ArrayList<>();
		if (isElementEnabled(differentLocations, 5)) {
			click(differentLocations);
			if (isElementEnabled(searchLocation, 5)) {
				SimpleUtils.pass("On click over All Locations in breadcrumb it should display flyout with search text box");
				if (areListElementVisible(locationName, 5)) {
					if (locationName.size() != 1) {
						for (int i = 0; i < locationName.size() - 1; i++) {
							click(locationName.get(i + 1));
							waitForSeconds(1);
							click(differentLocations);
							if (schedulePublishWindowWeeks) {
								selectionOptionLabel.add(getSchedulePublishWindowWeeks());
							} else {
								selectionOptionLabel.add(getSchedulePlanningWindowWeeks());
							}

						}
						click(locationName.get(0));
					} else {
						if (schedulePublishWindowWeeks) {
							selectionOptionLabel.add(getSchedulePublishWindowWeeks());
						} else {
							selectionOptionLabel.add(getSchedulePlanningWindowWeeks());
						}
					}
				} else {
					SimpleUtils.fail("Locations are not Visible", true);
				}

			} else {
				SimpleUtils.fail("On click over different Locations in breadcrumb flyout with search text box does not get displayed", true);
			}
		} else {
			SimpleUtils.fail("Different Locations link is not clickable", true);
		}
		return selectionOptionLabel;
	}


	public void verifySchedulePublishWindowUpdationValues(String publishWindowAdvanceWeeks, List<String> selectionOptionLabelAfterUpdation)
			throws Exception {
		if (isElementLoaded(schedulingPoliciesSchedulesFormSectionDiv, 5)) {
			if (isElementLoaded(schedulePublishWindowDiv, 5)) {
				WebElement schedulePublishWindowDropDown = schedulePublishWindowDiv.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(schedulePublishWindowDropDown, 5)) {
					for (int i = 0; i < selectionOptionLabelAfterUpdation.size(); i++) {
						if (selectionOptionLabelAfterUpdation.get(i).equalsIgnoreCase(getSchedulePublishWindowWeeks())) {
							SimpleUtils.pass("publish window values " + selectionOptionLabelAfterUpdation.get(i) + " are matching after updation with previous values " + publishWindowAdvanceWeeks);
						} else {
							SimpleUtils.fail("publish window values " + selectionOptionLabelAfterUpdation.get(i) + " are not matching after updation with previous values " + publishWindowAdvanceWeeks, true);
						}
					}
				} else
					SimpleUtils.fail("Scheduling Policies: Schedule weeks to be published dropdown not loaded.", false);
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule weeks to be published section not loaded.", false);
		} else {
			SimpleUtils.fail("Controls Page: - Scheduling Policies section: 'Schedules' form section not loaded.", false);
		}

	}


	public void getSchedulePlanningWindowWeeksDropDownValues() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(schedulingWindow, 5)) {
			WebElement schedulePlanningWindowDropDown = schedulingWindow.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(schedulePlanningWindowDropDown, 5)) {
				Select dropdown = new Select(schedulePlanningWindowDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
				for (WebElement option : dropdown.getOptions()) {
					SimpleUtils.pass("It should be a dropdown field with values " + option.getText());
				}
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule Planning window dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Schedule Planning window dropdown section not loaded.", false);

	}


	@FindBy(xpath = "//h3[contains(text(),'schedule be created')]")
	private WebElement schedulePlanningWindowText;

	public void verifySchedulePlanningWindow(String planningWindowAdvanceWeeks, String planningWindowQuestion, String userCredential) throws Exception {
		String fieldType = "select";
		if (isElementLoaded(schedulingPoliciesSchedulesFormSectionDiv, 5)) {
			if (isElementLoaded(schedulePlanningWindowText, 5)) {
				if (schedulePlanningWindowText.getText().equalsIgnoreCase(planningWindowQuestion)) {
					SimpleUtils.pass("Scheduling Policies: Schedule Planning Window question matches with expected value " + planningWindowQuestion);
				} else {
					SimpleUtils.fail("Scheduling Policies: Schedule Planning Window question not matched with expected value " + schedulePublishWindowText.getText(), true);
				}
				if (isElementLoaded(schedulingWindow, 5)) {
					WebElement schedulePlanningWindowDropDown = schedulingWindow.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
					if (isElementLoaded(schedulePlanningWindowDropDown, 5)) {
						boolean isFieldEditable = isInputFieldEditable(schedulePlanningWindowDropDown, fieldType);
						isFieldEditableBasedOnAccess(userCredential, isFieldEditable);
					} else
						SimpleUtils.fail("Scheduling Policies: Schedule planning weeks dropdown not loaded.", false);
				} else
					SimpleUtils.fail("Scheduling Policies: Advance Schedule weeks to be Finalize section not loaded.", false);
			} else {
				SimpleUtils.fail("Controls Page: - Scheduling Policies : Schedule planning window Question section not loaded.", false);
			}

		} else {
			SimpleUtils.fail("Controls Page: - Scheduling Policies section: 'Schedules' form section not loaded.", false);
		}

	}


	public void verifySchedulePlanningWindowUpdationValues(String planningWindowAdvanceWeeks, List<String> selectionOptionLabelAfterUpdation)
			throws Exception {
		if (isElementLoaded(schedulingPoliciesSchedulesFormSectionDiv, 5)) {
			if (isElementLoaded(schedulingWindow, 5)) {
				WebElement schedulePlanningWindowDropDown = schedulingWindow.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
				if (isElementLoaded(schedulePlanningWindowDropDown, 5)) {
					for (int i = 0; i < selectionOptionLabelAfterUpdation.size(); i++) {
						if (selectionOptionLabelAfterUpdation.get(i).equalsIgnoreCase(getSchedulePlanningWindowWeeks())) {
							SimpleUtils.pass("planning window values " + selectionOptionLabelAfterUpdation.get(i) + " are matching after updation with previous values " + planningWindowAdvanceWeeks);
						} else {
							SimpleUtils.fail("planning window values " + selectionOptionLabelAfterUpdation.get(i) + " are not matching after updation with previous values " + planningWindowAdvanceWeeks, true);
						}
					}
				} else
					SimpleUtils.fail("Scheduling Policies: Schedule weeks to be published dropdown not loaded.", false);
			} else
				SimpleUtils.fail("Scheduling Policies: Schedule weeks to be published section not loaded.", false);
		} else {
			SimpleUtils.fail("Controls Page: - Scheduling Policies section: 'Schedules' form section not loaded.", false);
		}

	}

	//added by Nishant

	public String getTimeSheetApprovalSelectedOption(boolean byManager) throws Exception {
		String selectedOptionLabel = "";
		if (byManager) {
			selectedOptionLabel = getSelectionOptionValue(timeSheetApprovalDueForManagerDiv);
		} else {
			selectedOptionLabel = getSelectionOptionValue(timeSheetApprovalDueForPayrollAdminDiv);
		}
		return selectedOptionLabel;
	}

	public String getSelectionOptionValue(WebElement timesheetDueDateAccess) throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(timesheetDueDateAccess)) {
			WebElement timeSheetFormatDropDown = timesheetDueDateAccess.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(timeSheetFormatDropDown)) {
				Select dropdown = new Select(timeSheetFormatDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Controls - Time and Attendance: timesheet Approval for Manager/Payroll Admin dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: TimeSheet Approval section loaded.", false);
		return selectedOptionLabel;
	}

	//Added by Nora
	@FindBy(css = "select[aria-label=\"Time Zone\"] option[selected=\"selected\"]")
	private WebElement timeZoneSelected;
	@FindBy(css = "input-field[value*=\"teamPreference\"] option[selected=\"selected\"]")
	private WebElement onBoardOption;
	@FindBy(css = "input-field[value*=\"teamPreference\"] select")
	private WebElement inviteOnBoardSelect;
	@FindBy(css = ".collapsible-title")
	private List<WebElement> workingHoursTypes;
	@FindBy(css = "#day\\.dayOfTheWeek .pills-row")
	private List<WebElement> weekDays;
	@FindBy(css = "[question-title=\"Is approval required by Manager when an employee claims a shift swap or cover request in a home location?\"]")
	private WebElement swapApprovalRequired;
	@FindBy(css = "[ng-repeat=\"user in $ctrl.pagedUsers\"]")
	private List<WebElement> usersInUsersNRoles;

	@Override
	public void verifyUsersAreLoaded() throws Exception {
		if (areListElementVisible(usersInUsersNRoles, 60)) {
			SimpleUtils.pass("Users and Roles: Users are loaded successfully!");
		} else {
			SimpleUtils.fail("Users and Roles: Users not loaded successfully!", false);
		}
	}

	@Override
	public void updateSwapAndCoverRequestIsApprovalRequired(String option) throws Exception {
		waitForSeconds(3);
		if (isElementLoaded(swapApprovalRequired, 20)) {
			scrollToElement(swapApprovalRequired);
			waitForSeconds(1);
			List<WebElement> options = swapApprovalRequired.findElements(By.cssSelector(".lg-button-group>div"));
			for (WebElement childOption : options) {
				if (childOption.getText().equalsIgnoreCase(option)) {
					if (childOption.getAttribute("class").contains("lg-button-group-selected")) {
						// Do nothing
					} else {
						clickTheElement(childOption);
						break;
					}
				}
			}
		}else {
			SimpleUtils.fail("Swap Request Approval Required Select not loaded Successfully!", false);
		}
	}

	@Override
	public String getTimeZoneFromLocationDetailsPage() throws Exception {
		String timeZone = null;
		if (isElementLoaded(timeZoneSelected, 5)) {
			/*
			 * Wait for the data to be loaded
			 */
			waitForSeconds(3);
			timeZone = timeZoneSelected.getText();
			if (isElementLoaded(backNavigator, 5)) {
				clickTheElement(backNavigator);
			}else {
				SimpleUtils.fail("Controls: Back Navigator not loaded Successfully!", false);
			}
		} else {
			SimpleUtils.fail("Time Zone Select failed to load!", true);
		}
		return timeZone;
	}

	@FindBy(css = "lg-schedule-score-control > div > div > div.tr > lg-button > button > span > span")
	private WebElement manageBtnInScheduleScore;

	@FindBy(css = "select[class=\"ng-pristine ng-untouched ng-valid ng-not-empty ng-valid-required\"]")
	private WebElement drpCoverField;


	@FindBy(css = "input[class=\"ng-pristine ng-untouched ng-valid ng-scope ng-not-empty ng-valid-min ng-valid-max ng-valid-required ng-valid-pattern\"]")
	private List<WebElement> scoreFieldList;

	@FindBy(css = "[label=\"Save\"]")
	private WebElement saveBtnInManageSchScore;

	@Override
	public void updateScheduleScore(String budget_score, String coverage_scores_regular_hours, String coverage_scores_peak_hours, String employee_match_score, String compliance_score, String how_to_measure_coverage_relative_to_guidance_budget) throws Exception {

		/*
		 * wait for page loaded
		 */
		waitForSeconds(10);
		scrollToBottom();
		if (isElementLoaded(manageBtnInScheduleScore, 5)) {
			click(manageBtnInScheduleScore);
			try {
				Select drpCoverage = new Select(drpCoverField);
				drpCoverage.selectByVisibleText(how_to_measure_coverage_relative_to_guidance_budget);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (scoreFieldList.size() >= 0) {
				for (WebElement e : scoreFieldList
				) {
					e.click();
					e.sendKeys(Keys.chord(Keys.CONTROL, "a"));
					e.sendKeys(budget_score);
				}
				click(saveBtnInManageSchScore);

			} else
				SimpleUtils.fail("manage button load failed and can not update schedule score", false);
		}
	}

	@FindBy(css = ".lg-question-input__text.tr")
	private List<WebElement> scoreListInSchedulePolicy;


	@Override
	public boolean isScheduleScoreUpdated(String budget_score, String coverage_scores_regular_hours, String coverage_scores_peak_hours, String employee_match_score, String compliance_score, String how_to_measure_coverage_relative_to_guidance_budget) {

		boolean isScoreUpdated = false;
		String abc = scoreListInSchedulePolicy.get(0).getText().replaceAll("%", "");

		if (scoreListInSchedulePolicy.get(0).getText().replaceAll("%", "").equals(budget_score)) {
			isScoreUpdated = true;
			SimpleUtils.pass("Controls Page: Schedule policy - 'budget_score' updated successfully.");
		}
		String b = scoreListInSchedulePolicy.get(1).getText().replaceAll("%", "");
		if (scoreListInSchedulePolicy.get(1).getText().replaceAll("%", "").equals(coverage_scores_regular_hours)) {
			isScoreUpdated = true;
			SimpleUtils.pass("Controls Page: Schedule policy - 'coverage_scores_regular_hours' updated successfully.");
		}

		String c = scoreListInSchedulePolicy.get(2).getText().replaceAll("%", "");
		if (scoreListInSchedulePolicy.get(2).getText().replaceAll("%", "").equals(coverage_scores_peak_hours)) {
			isScoreUpdated = true;
			SimpleUtils.pass("Controls Page: Schedule policy - 'coverage_scores_peak_hours' updated successfully.");
		}

		String d = scoreListInSchedulePolicy.get(3).getText().replaceAll("%", "");
		if (scoreListInSchedulePolicy.get(3).getText().replaceAll("%", "").equals(employee_match_score)) {
			isScoreUpdated = true;
			SimpleUtils.pass("Controls Page: Schedule policy - 'employee_match_score' updated successfully.");
		}
		String e = scoreListInSchedulePolicy.get(4).getText().replaceAll("%", "");
		if (scoreListInSchedulePolicy.get(4).getText().replaceAll("%", "").equals(compliance_score)) {
			isScoreUpdated = true;
			SimpleUtils.pass("Controls Page: Schedule policy - 'compliance_score' updated successfully.");
		}
		String f = scoreListInSchedulePolicy.get(5).getText().replaceAll("%", "");
		if (scoreListInSchedulePolicy.get(5).getText().replaceAll("%", "").equals(how_to_measure_coverage_relative_to_guidance_budget)) {
			isScoreUpdated = true;
			SimpleUtils.pass("Controls Page: Schedule policy - 'how_to_measure_coverage_relative_to_guidance_budget' updated successfully.");
		}
		return isScoreUpdated;

	}


	@Override
	public String getOnBoardOptionFromScheduleCollaboration() throws Exception {
		String emailOrPhone = null;
		if (isElementLoaded(onBoardOption, 15)) {
			// Wait for the data to be loaded
			waitForSeconds(3);
			emailOrPhone = onBoardOption.getText();
		} else {
			SimpleUtils.fail("On board option failed to load!", true);
		}
		return emailOrPhone;
	}

	@Override
	public void setOnBoardOptionAsEmailWhileInviting() throws Exception {
		String email = "Email";
		if (isElementLoaded(inviteOnBoardSelect, 15)) {
			scrollToBottom();
			selectByVisibleText(inviteOnBoardSelect, email);
		} else {
			SimpleUtils.fail("On board option failed to load!", true);
		}
	}

	//added by Nishant

	public void enableOverRideAssignmentRuleAsYes() throws Exception {
		boolean OverrideAssignmentRule = true;
		if (isElementEnabled(btnOverrideAssignmentRule, 20)) {
			if (isElementEnabled(btnOverrideAssignmentRuleYes, 3)) {
				if (btnOverrideAssignmentRuleYes.getAttribute("class").contains("selected")) {
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button already enabled");
				} else {
					clickTheElement(btnOverrideAssignmentRuleYes);
					if (isElementLoaded(overridePopup, 10) && isElementLoaded(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")),10)){
						clickTheElement(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")));
					}
					Actions actions = new Actions(getDriver());
					actions.moveByOffset(0, 0).click().build().perform();
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button selected!");
					displaySuccessMessage();
				}
			} else {
				SimpleUtils.fail("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button not loaded!!", false);
			}
		}
	}

	@Override
	public void enableOverRideAssignmentRuleAsNo() throws Exception {
		if (isElementEnabled(btnOverrideAssignmentRule, 20)) {
			if (isElementEnabled(btnOverrideAssignmentRuleNo, 10)) {
				waitForSeconds(5);
				if (btnOverrideAssignmentRuleNo.getAttribute("class").contains("selected")) {
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'No' button already enabled");
				} else {
					scrollToElement(btnOverrideAssignmentRuleNo);
					clickTheElement(btnOverrideAssignmentRuleNo);
					if (isElementLoaded(overridePopup, 10) && isElementLoaded(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")),10)){
						clickTheElement(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")));
					}
					Actions actions = new Actions(getDriver());
					actions.moveByOffset(0, 300).click().build().perform();
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'No' button selected!");
					displaySuccessMessage();
				}
			} else {
				SimpleUtils.fail("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button not loaded!!", false);
			}
		}
	}


	@Override
	public void clickOnWorkHoursTypeByText(String title) throws Exception {
		if (areListElementVisible(workingHoursTypes, 5)) {
			for (WebElement workingHoursType : workingHoursTypes) {
				if (workingHoursType.getText().equalsIgnoreCase(title)) {
					click(workingHoursType);
					SimpleUtils.pass("Click on Type: " + title + " Successfully!");
				}
			}
		} else {
			SimpleUtils.fail("Working Hours Types not loaded Successfully!", true);
		}
	}

	@Override
	public LinkedHashMap<String, List<String>> getRegularWorkingHours() throws Exception {
		LinkedHashMap<String, List<String>> regularHours = new LinkedHashMap<>();
		List<String> startNEndTime = null;
		if (areListElementVisible(weekDays, 30)) {
			for (int i = 0; i < weekDays.size(); i++) {
				WebElement day = getDriver().findElements(By.cssSelector("#day\\.dayOfTheWeek .pills-row")).get(i).findElement(By.className("ellipsis"));
				List<WebElement> workTimes = getDriver().findElements(By.cssSelector("#day\\.dayOfTheWeek .pills-row")).get(i).findElements(By.className("work-time"));
				if (day != null && workTimes != null && workTimes.size() == 2) {
					String startTime = workTimes.get(0).getText();
					String endTime = workTimes.get(1).getText();
					startNEndTime = new ArrayList<>();
					startNEndTime.add(startTime);
					startNEndTime.add(endTime);
					regularHours.put(getDriver().findElements(By.cssSelector("#day\\.dayOfTheWeek .pills-row")).get(i).findElement(By.className("ellipsis")).getText(), startNEndTime);
					SimpleUtils.report("Get time for: " + getDriver().findElements(By.cssSelector("#day\\.dayOfTheWeek .pills-row")).get(i).findElement(By.className("ellipsis")).getText() + ", time is: " + startTime + " - " + endTime);
				}
			}
		}
		if (regularHours.size() != 7) {
			SimpleUtils.fail("Failed to find the weekday and working times!", true);
		}
		return regularHours;
	}
	//added by Estelle to get overtime configuration data

	@FindBy(xpath = "//form-section[1]/ng-transclude/content-box/ng-transclude/question-input[1]/div/div[1]/h3")
	private WebElement overtimeWeeklyText;

	@FindBy(xpath = "//form-section[1]/ng-transclude/content-box/ng-transclude/question-input[2]/div/div[1]/h3")
	private WebElement overtimeDailyText;

	@FindBy(xpath = "//form-section[1]/ng-transclude/content-box/ng-transclude/question-input[3]/div/div[1]/h3")
	private WebElement maxConsecutiveRegularDays;

	@Override
	public HashMap<String, Integer> getOvertimePayDataFromControls() {
		waitForSeconds(20); // to wait data load completed
		HashMap<String, Integer> overtimePayData = new HashMap<String, Integer>();
		String[] overtimeWeeklyData = overtimeWeeklyText.getText().split(" ");
		String[] overtimeDailyData = overtimeDailyText.getText().split(" ");
		String[] maxConsecutiveRegularDaysData = maxConsecutiveRegularDays.getText().split(" ");
		overtimePayData.put("overtimeWeeklyText", Integer.valueOf(overtimeWeeklyData[12]));
		overtimePayData.put("overtimeDailyText", Integer.valueOf(overtimeDailyData[12]));
		overtimePayData.put("maxConsecutiveRegularDays", Integer.valueOf(maxConsecutiveRegularDaysData[10].substring(0, 1)));
		return overtimePayData;
	}


	@FindBy(xpath = "//form-section[3]/ng-transclude/content-box[1]/ng-transclude/question-input[1]/div/div[1]/h3")
	private WebElement mealBreakDataInControlsCompliance;

	@Override
	public HashMap<String, Integer> getMealBreakDataFromControls() {
		HashMap<String, Integer> mealTimeBreakData = new HashMap<String, Integer>();
		String[] mealBreakDataInControls = mealBreakDataInControlsCompliance.getText().split(" ");
		mealTimeBreakData.put("unPaiedMins", Integer.valueOf(mealBreakDataInControls[6]));
		mealTimeBreakData.put("everyXHoursOfWork", Integer.valueOf(mealBreakDataInControls[13]));
		return mealTimeBreakData;
	}

	//Added by Estelle to check open shift is approved by man
	@Override
	public String getIsApprovalByManagerRequiredWhenEmployeeClaimsOpenShiftSelectedOption() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(isApprovedByManagerWhileClaimOpenShift, 10)) {
			WebElement approveByManagerDropDown = isApprovedByManagerWhileClaimOpenShift.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
			if (isElementLoaded(approveByManagerDropDown, 10)) {
				Select dropdown = new Select(approveByManagerDropDown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Controls - Schedule Collaboration: Open Shift is Approved ByManager While Claim OpenShift dropdown not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: Schedule Collaboration: Open Shift section not loaded.", false);
		return selectedOptionLabel;
	}

	@Override
	public void updateOpenShiftApprovedByManagerOption(String option) throws Exception {
		WebElement approveByManagerDropDown = isApprovedByManagerWhileClaimOpenShift.findElement(By.cssSelector("select[ng-change=\"$ctrl.handleChange()\"]"));
		if (isElementLoaded(approveByManagerDropDown, 10)) {
			Select dropdown = new Select(approveByManagerDropDown);
			dropdown.selectByVisibleText(option);
			displaySuccessMessage();
			SimpleUtils.pass("Option is selected successfully");
		} else {
			SimpleUtils.fail("Controls - Schedule Collaboration: Open Shift is Approved ByManager While Claim OpenShift dropdown not loaded.", false);
		}

	}

	@FindBy(css = "question-input[question-title=\"Is manager approval required when an employee changes availability?\"] input-field")
	private WebElement isApprovalRequiredToChangeAvailability;
	//added by Haya
	//Options: Not required;Required for all changes;Required for reducing availability
	@Override
	public void updateAvailabilityManagementIsApprovalRequired(String option) throws Exception {
		// Wait for the values loaded
		waitForSeconds(15);
		WebElement confSelect = isApprovalRequiredToChangeAvailability.findElement(By.cssSelector("select"));
		if(isElementLoaded(confSelect)) {
			//WebElement input = isApprovalRequiredToChangeAvailability.findElement(By.xpath("//input-field"));
			if(isElementLoaded(confSelect,5)) {
				scrollToElement(confSelect);
				selectByVisibleText(confSelect,option);
				displaySuccessMessage();
			} else{
				SimpleUtils.fail("Is manager approval required when an employee changes availability? input field not loaded.", false);
			}
		} else{
			SimpleUtils.fail("Is manager approval required when an employee changes availability?  section not loaded.", false);

		}

	}
	//added by Fiona
	//get current location on controls landing page
	@FindBy(css = "div.controlsNavigation lg-select[search-hint=\"Search Location\"] input-field div")
	private WebElement currentLocationInControls;
	public String getCurrentLocationInControls() throws Exception{
		if (isElementLoaded(currentLocationInControls,5)) {
			return   currentLocationInControls.getText();
		}
		return null;
	}

	//Added by Haya
	@FindBy(css = "form-section[form-title=\"Predictable Schedule\"] .lg-question-input")
	private List<WebElement> predictableScheduleSectionToggles;

	@Override
	public void turnGFEToggleOnOrOff(boolean isTurnOn) throws Exception {
		String gfe = "Manager can send Good Faith Estimate.";
		// Wait for the default settings loaded
		waitForSeconds(15);
		if (areListElementVisible(predictableScheduleSectionToggles,10) && predictableScheduleSectionToggles.size() > 0){
			if (isTurnOn){
				for (WebElement predictableSection : predictableScheduleSectionToggles) {
					try {
						WebElement toggle = predictableSection.findElement(By.className("switch"));
						WebElement text = predictableSection.findElement(By.className("lg-question-input__text"));
						if (toggle != null && text != null && text.getText().equals(gfe)) {
							if (toggle.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")) {
								scrollToElement(toggle);
								waitForSeconds(1);
								clickTheElement(toggle);
								displaySuccessMessage();
								SimpleUtils.pass("GFE toggle is turned on!");
							} else {
								SimpleUtils.report("GFE toggle is already on!");
							}
							break;
						}
					} catch (Exception e) {
						continue;
					}
				}
			} else {
				for (WebElement predictableSection : predictableScheduleSectionToggles) {
					try {
						WebElement toggle = predictableSection.findElement(By.className("switch"));
						WebElement text = predictableSection.findElement(By.className("lg-question-input__text"));
						if (toggle != null && text != null && text.getText().equals(gfe)) {
							if (toggle.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
								scrollToElement(toggle);
								waitForSeconds(1);
								click(toggle);
								displaySuccessMessage();
								SimpleUtils.pass("GFE toggle is turned off !");
							} else {
								SimpleUtils.report("GFE toggle is already off!");
							}
							break;
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		} else {
			SimpleUtils.fail("There is no predictable schedule settings!", false);
		}
	}

	@Override
	public void turnVSLToggleOnOrOff(boolean isTurnOn) throws Exception {
		// Wait for the default settings loaded
		waitForSeconds(15);
		if (areListElementVisible(predictableScheduleSectionToggles,15) && predictableScheduleSectionToggles.size()>1){
			if (isTurnOn){
				if (predictableScheduleSectionToggles.get(1).findElement(By.cssSelector("input")).getAttribute("class").contains("ng-empty")){
					click(predictableScheduleSectionToggles.get(1).findElement(By.cssSelector("span")));
					displaySuccessMessage();
					SimpleUtils.pass("GFE toggle is turned on!");
				} else {
					SimpleUtils.report("GFE toggle is already on!");
				}
			} else {
				if (predictableScheduleSectionToggles.get(1).findElement(By.cssSelector("input")).getAttribute("class").contains("ng-not-empty")){
					click(predictableScheduleSectionToggles.get(1).findElement(By.cssSelector("span")));
					displaySuccessMessage();
					SimpleUtils.pass("GFE toggle is turned off !");
				} else {
					SimpleUtils.report("GFE toggle is already off!");
				}
			}
		} else {
			SimpleUtils.fail("There is no predictable schedule settings!", false);
		}
	}

	// Added By Julie
	@FindBy(css = ".lg-user-locations-new__item-name")
	private List<WebElement> userLocation;

	@FindBy(xpath = "//ng-transclude/div[1][@class='legion-information ng-scope']//div[2]/span")
	private WebElement schedulingPolicyGroup;

	@FindBy(css = "[form-title=\"Scheduling Policy Groups\"] lg-tabs lg-tab")
	private List<WebElement> schedulingPolicyGroupsTabContent;

	@FindBy(css = "[label=\"Cancel Deactivate\"] button")
	private WebElement cancelDeactivateBtn;

	@FindBy(css = "[label=\"Deactivate\"] button")
	private WebElement deactivateBtn;

	@FindBy(css = "[label=\"Activate\"] button")
	private WebElement activateBtn;

	@FindBy(css = ".calendar-week.select-day.current-week")
	private WebElement currentWeekInDeactivateWindow;

	@FindBy(css = ".current-day")
	private WebElement currentDayInDeactivateWindow;

	@FindBy(className = "save-btn")
	private WebElement applyOrActivateOrSaveBtn;

	@FindBy(css = "span.lgn-alert-message")
	private WebElement alertMessage;

	@FindBy(className = "lgn-action-button-success")
	private WebElement confirmBtn;

	@FindBy(css = "input[placeholder=\"You can search by name, job title, and status.\"]")
	private WebElement teamMemberSearchBox;

	@FindBy(tagName = "lg-eg-status")
	private WebElement statusInUserDetails;

	@FindBy(className = "month-header")
	private WebElement monthInCalendar;

	@FindBy(className = "selected-day")
	private WebElement selectedDayInCalendar;

	@FindBy(xpath = "//span[contains(text(),\"HOME STORE\")]/../following-sibling::div[1]")
	private WebElement homeStoreLocation;

	@Override
	public HashMap<String, List<String>> getRandomUserNLocationNSchedulingPolicyGroup() throws Exception {
		HashMap<String, List<String>> userNLocationNSchedulingPolicyGroup = new HashMap<>();
		if (areListElementVisible(usersAndRolesAllUsersRows, 10)) {
			int index = (new Random()).nextInt(usersAndRolesAllUsersRows.size());
			WebElement userName = usersAndRolesAllUsersRows.get(index).findElement(By.cssSelector("lg-button[list] button span span"));
			String userNameText = userName.getText();
			click(userName);
			if (isElementLoaded(homeStoreLocation, 5) && isElementLoaded(schedulingPolicyGroup, 5)) {
				String userLocationText = homeStoreLocation.getText().trim();
				String userSchedulingPolicyGroup = schedulingPolicyGroup.getText();
				List<String> locationNSchedulingPolicyGroup = new ArrayList<>();
				locationNSchedulingPolicyGroup.add(userLocationText);
				locationNSchedulingPolicyGroup.add(userSchedulingPolicyGroup);
				userNLocationNSchedulingPolicyGroup.put(userNameText, locationNSchedulingPolicyGroup);
				SimpleUtils.report("Get TM \"" + userNameText + "\": Location ---  " + userLocationText + ",  Scheduling Policy Group --- " + userSchedulingPolicyGroup);
			}
		}
		return userNLocationNSchedulingPolicyGroup;
	}

	@Override
	public HashMap<String, List<String>> getDataFromSchedulingPolicyGroups() throws Exception {
		// wait for data loaded
		waitForSeconds(10);
		HashMap<String, List<String>> dataFromSchedulingPolicyGroups = new HashMap<>();
		WebElement currentTab = null;
		if (areListElementVisible(schedulingPolicyGroupsTabContent, 5)) {
			for (WebElement tab : schedulingPolicyGroupsTabContent) {
				WebElement tabSelectedOrNot = tab.findElement(By.tagName("ng-transclude"));
				if (!tabSelectedOrNot.getAttribute("class").contains("ng-hide")) {
					currentTab = tab;
					break;
				}
			}
			List<WebElement> questionOptions = currentTab.findElements(By.cssSelector("div div question-input"));
			for (WebElement e : questionOptions) {
				List<WebElement> inputFields = e.findElements(By.cssSelector("input-field ng-form input[ng-change=\"$ctrl.handleChange()\"]"));
				List<String> minNMaxNIdeal = new ArrayList<>();
				for (WebElement input : inputFields)
					minNMaxNIdeal.add(input.getAttribute("value"));
				dataFromSchedulingPolicyGroups.put(e.getAttribute("question-title"), minNMaxNIdeal);
				SimpleUtils.report("Get tab \"" + currentTab.getAttribute("tab-title") + "\": " + e.getAttribute("question-title") + " --- Minimum & Maximum & Ideal: " + minNMaxNIdeal.get(0) + " & " + minNMaxNIdeal.get(1) + " & " + minNMaxNIdeal.get(2));
				if (dataFromSchedulingPolicyGroups.size() == 3)
					break;
			}
		}
		return dataFromSchedulingPolicyGroups;
	}

	@Override
	public String selectAnyActiveTM() throws Exception {
		String activeUser = "";
		if (areListElementVisible(usersAndRolesAllUsersRows, 20)) {
			for (WebElement user : usersAndRolesAllUsersRows) {
				WebElement name = user.findElement(By.cssSelector("lg-button button span span"));
				WebElement status = user.findElement(By.tagName("lg-eg-status"));
				if (name != null && status != null) {
					if (status.getAttribute("type").equalsIgnoreCase("Active")) {
						activeUser = name.getText();
						click(name);
						SimpleUtils.pass("Users and Roles Page: User '" + activeUser + "' selected successfully.");
						break;
					}
				} else
					SimpleUtils.fail("Users and Roles Page: Failed to find the name and Status!", false);
			}
		} else
			SimpleUtils.fail("Users and Roles Page: Users failed to load or no users", false);
		return activeUser;
	}

	@Override
	public String deactivateActiveTM() throws Exception {
		String date = "";
		if (isElementLoaded(cancelDeactivateBtn, 5)) {
			click(cancelDeactivateBtn);
			if (alertMessage.getText().contains("should NOT be deactivated?")) {
				click(confirmBtn);
			}
		}
		if (isElementLoaded(deactivateBtn, 5)) {
			click(deactivateBtn);
			if (isElementLoaded(currentDayInDeactivateWindow, 5)) {
				click(currentDayInDeactivateWindow);
				if (monthInCalendar.getText().trim().contains(" "))
					date = monthInCalendar.getText().trim().split(" ")[0] + " " + selectedDayInCalendar.getText().trim() + " " + monthInCalendar.getText().trim().split(" ")[1];
				Date dateInCalendar = new SimpleDateFormat("MMMM d yyyy").parse(date);
				date = new SimpleDateFormat("MM/dd/yyyy").format(dateInCalendar);
				if (applyOrActivateOrSaveBtn.isEnabled()) {
					click(applyOrActivateOrSaveBtn);
					SimpleUtils.pass("User Details Page: Select today to deactivate the user successfully");
					clickTheElement(confirmBtn);
					waitForSeconds(2);
					if (statusInUserDetails.getAttribute("Type").contains("Inactive")) {
						SimpleUtils.pass("User Details Page: Select the current day to deactivate and successfully scheduled deactivation of Team Member");
					} else
						SimpleUtils.fail("User Details Page: No successful alert message when deactivating Team Member", false);
				} else
					SimpleUtils.fail("User Details Page: APPLY button is disabled to click", false);
			} else
				SimpleUtils.fail("User Details Page: Current day in Deactivate Window failed to load", false);
		} else
			SimpleUtils.fail("User Details Page: Cannot find 'Deactivate' button", false);
		return date;
	}

	@Override
	public void activateInactiveTM() throws Exception {
		if (isElementLoaded(activateBtn, 15)) {
			click(activateBtn);
			if (isElementLoaded(currentDayInDeactivateWindow, 15)) {
				click(currentDayInDeactivateWindow);
				if (applyOrActivateOrSaveBtn.isEnabled()) {
					click(applyOrActivateOrSaveBtn);
					waitForSeconds(2);
					if (statusInUserDetails.getAttribute("Type").contains("Active")) {
						SimpleUtils.pass("User Details Page: Select the current day to activate and successfully scheduled activation of Team Member");
					} else
						SimpleUtils.fail("User Details Page: No successful alert message when activating Team Member", false);
				} else
					SimpleUtils.fail("User Details Page: ACTIVATE button is disabled to click", false);
			} else
				SimpleUtils.fail("User Details Page: Current day in Deactivate Window failed to load", false);
		} else
			SimpleUtils.fail("User Details Page: Cannot find 'Activate' button", false);
	}

	@Override
	public void searchAndSelectTeamMemberByName(String username) throws Exception {
		boolean isTeamMemberFound = false;
		if (isElementLoaded(teamMemberSearchBox, 20)) {
			teamMemberSearchBox.clear();
			teamMemberSearchBox.sendKeys(username);
			teamMemberSearchBox.sendKeys(Keys.ENTER);
			waitForSeconds(8);
			if (usersAndRolesAllUsersRows.size() > 0) {
				for (WebElement user : usersAndRolesAllUsersRows) {
					WebElement name = user.findElement(By.cssSelector("lg-button button span span"));
					if (name != null && name.getText().contains(username)) {
						clickTheElement(name);
						isTeamMemberFound = true;
						SimpleUtils.pass("Users and Roles Page: User '" + username + "' selected successfully.");
						break;
					}
				}
			}
			if (!isTeamMemberFound)
				SimpleUtils.report("Users and Roles Page: Team Member '" + username + "' not found.");
		} else
			SimpleUtils.fail("Users and Roles Page: failed to load search box",false);
	}

	// Added by Marym

	@FindBy(css = "div.lg-tabs__nav-item")
	private List<WebElement> allDistrictsAndAllLocationsTabs;

	@FindBy(css = "table.lg-table")
	private List<WebElement> allDistrictsAndAllLocationsTables;

	@FindBy(css = "[ng-click=\"$ctrl.back()\"]")
	private WebElement backButtonOnLocationDetailPage;

	@Override
	public void clickOnBackButtonOnLocationDetailPage() throws Exception {
		if (isElementLoaded(backButtonOnLocationDetailPage, 5))
			click(backButtonOnLocationDetailPage);
		else
			SimpleUtils.fail("Locations Page: Back button on location detail page not loaded!", false);

		if (areListElementVisible(allDistrictsAndAllLocationsTabs, 10))
			SimpleUtils.pass("Locations Page: Back to locations page successfully");
		else
			SimpleUtils.fail("Locations Page: Locations page not loaded!", false);
	}

	@Override
	public void clickOnControlsLocationsSection() throws Exception {
		if (isElementLoaded(locationsSection, 5))
			click(locationsSection);
		else
			SimpleUtils.fail("Controls Page: Locations Card not Loaded!", false);
	}

	@Override
	public boolean isLocationsPageLoaded() throws Exception {
		if (areListElementVisible(allDistrictsAndAllLocationsTabs, 15)) {
			SimpleUtils.pass("Controls Page: Locations page loaded successfully");
			return true;
		} else {
			SimpleUtils.fail("Controls Page: Locations page not Loaded", false);
			return false;
		}
	}

	@Override
	public void clickAllDistrictsOrAllLocationsTab(boolean isClickDistrictsTab) throws Exception {
		if (areListElementVisible(allDistrictsAndAllLocationsTabs, 15) &&
				allDistrictsAndAllLocationsTabs.size() >= 2 &&
				areListElementVisible(allDistrictsAndAllLocationsTables, 15) &&
				allDistrictsAndAllLocationsTables.size() >=2){
			if (isClickDistrictsTab){
				if (allDistrictsAndAllLocationsTabs.get(0) != null){
					click(allDistrictsAndAllLocationsTabs.get(0));
					if (isElementLoaded(allDistrictsAndAllLocationsTables.get(0))) {
						SimpleUtils.pass("Locations page: All districts page loaded successfully");
					} else {
						SimpleUtils.fail("Locations page: All districts page not Loaded", false);
					}
				} else {
					SimpleUtils.fail("Locations page: All districts tab not Loaded", false);
				}
			} else {
				if (allDistrictsAndAllLocationsTabs.get(1) != null){
					click(allDistrictsAndAllLocationsTabs.get(1));
					if (isElementLoaded(allDistrictsAndAllLocationsTables.get(1))) {
						SimpleUtils.pass("Locations page: All locations page loaded successfully");
					} else {
						SimpleUtils.fail("Locations page: All locations page not Loaded", false);
					}
				} else {
					SimpleUtils.fail("Locations page: All locations tab not Loaded", false);
				}
			}
		} else{
			SimpleUtils.fail("Controls Page: Locations page not Loaded", false);
		}
	}

	@FindBy (css = ".lg-table.ng-scope")
	private List<WebElement> tablesOfDistrictsAndLocations;

	@FindBy (css = "input[aria-label=\"Location Address\"]")
	private WebElement locationAddress;

	@FindBy (css = "input[aria-label=\"City\"]")
	private WebElement city;

	@FindBy (css = "input[aria-label=\"Zip Code\"]")
	private WebElement zipCode;

	@FindBy (css = "[aria-label=\"State\"]")
	private WebElement state;

	@FindBy (css = "[aria-label=\"Province\"]")
	private WebElement province;

	@FindBy (css = ".lg-form-section-action")
	private WebElement editLocationButton;

	@Override
	public void goToSpecificLocationDetailPageByLocationName (String locationName) throws Exception {
		boolean isLocationExist = false;
		if (allDistrictsAndAllLocationsTables != null && allDistrictsAndAllLocationsTables.size()>=2){
			WebElement tableOfLocations = allDistrictsAndAllLocationsTables.get(1);
			if (tableOfLocations != null){
				List<WebElement> locations =  tableOfLocations.findElements(By.cssSelector("[class=\"ng-binding ng-scope\"]"));
				if (locations != null && locations.size() != 0){
					for (WebElement location: locations){
						if(location != null && location.getText().equals(locationName)){
							isLocationExist = true;
							click(location);
							if (isElementLoaded(locationInformationFormSection, 10)) {
								SimpleUtils.pass("Locations page: Location detail page loaded successfully");
							} else {
								SimpleUtils.fail("Locations page: Location detail page fail to load", false);
							}
							break;
						}
					}
					if (!isLocationExist) {
						SimpleUtils.fail("Locations page: The specified location was not found. The specified location name is: " + locationName, false);
					}
				}
			} else {
				SimpleUtils.fail("Locations page: All locations page not Loaded", false);
			}
		} else {
			SimpleUtils.fail("Controls Page: Locations page not Loaded", false);
		}
	}

	@Override
	public String getLocationInfoStringFromDetailPage () throws Exception {
		String locationDetailInfo = "";
		String stateStr = "";
		String cityStr = "";
		String locationAddressStr = "";

		if (isElementLoaded(locationAddress, 10) && isElementLoaded(city, 10)
				&& (isElementLoaded(state, 10) || isElementLoaded(province,10)) ) {
			cityStr = city.getAttribute("value");
			locationAddressStr = locationAddress.getAttribute("value");
		} else {
			SimpleUtils.fail("Locations page: Elements in location page not Loaded", false);
		}
		if (isElementLoaded(state, 10)) {
			stateStr = state.getAttribute("value").contains(" ")? state.getAttribute("value").split(" ")[0].substring(0,1) + state.getAttribute("value").split(" ")[1].substring(0,1): state.getAttribute("value").substring(0,1);
		}
		if (isElementLoaded(province,10)) {
			stateStr = province.getAttribute("value").contains(" ")? province.getAttribute("value").split(" ")[0].substring(0,1) + province.getAttribute("value").split(" ")[1].substring(0,1): province.getAttribute("value").substring(0,1);
		}
		locationDetailInfo = locationAddressStr + ", " + cityStr + " " + stateStr ;
		return locationDetailInfo;
	}

	@FindBy(css = "form-section[form-title=\"Clopening\"] .lg-question-input")
	private List<WebElement> clopeningSectionToggles;

	@FindBy(css = "button.lg-icon-button--confirm")
	private WebElement confirmSettingsChangeButton;

	@Override
	public void turnONClopeningToggleAndSetHours(int clopeningHours) throws Exception {

		String clopeningMessage1 = "An employee should have at least";
		String clopeningMessage2 = "hours of break between consecutive closing and opening shifts";
		waitForSeconds(10);
		if (areListElementVisible(clopeningSectionToggles,10) && clopeningSectionToggles.size() > 0){
			for (WebElement clopeningSectionToggle : clopeningSectionToggles) {
				WebElement toggle = clopeningSectionToggle.findElement(By.className("switch"));
				WebElement text = clopeningSectionToggle.findElement(By.className("lg-question-input__text"));
				if (toggle != null && text != null && text.getText().contains(clopeningMessage1) && text.getText().contains(clopeningMessage2)) {
					//turn on Clopening toggle
					if (toggle.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")) {
						scrollToElement(toggle);
						waitForSeconds(1);
						click(toggle);
						displaySuccessMessage();
						SimpleUtils.pass("Clopening toggle is turned on!");
					} else {
						SimpleUtils.report("Clopening toggle is already on!");
					}

					// set Clopening value
					WebElement clopeningHoursInput = clopeningSectionToggle.findElement(By.cssSelector("[ng-class=\"{'ng-invalid': $ctrl.invalid}\"]"));
					if (isElementLoaded(clopeningHoursInput, 5) && clopeningHoursInput != null) {
						clopeningHoursInput.clear();
						clopeningHoursInput.sendKeys(String.valueOf(clopeningHours));
						if (isElementLoaded(confirmSettingsChangeButton, 5)) {
							click(confirmSettingsChangeButton);
						} else
							SimpleUtils.fail("Confirm clopening change button load fail !", false);
						displaySuccessMessage();
						SimpleUtils.pass("Set clopening hours successfully!");
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("Clopening settings load failed!", false);
		}
	}

	@FindBy(css = "[options=\"selections.clopeningHours\"] [ng-attr-id=\"{{$ctrl.inputName}}\"]")
	private WebElement clopeningHoursSelector;

	@FindBy(css = "[options=\"selections.clopeningHours\"]")
	private WebElement clopeningHours;



	@Override
	public void selectClopeningHours(int clopeningHour) throws Exception {
		clickOnSchedulingPoliciesShiftAdvanceBtn();
		String test = clopeningHours.getAttribute("value");
		if (isElementLoaded(clopeningHoursSelector, 5)) {
			selectByVisibleText(clopeningHoursSelector, String.valueOf(clopeningHour)+ " hours");
			displaySuccessMessage();
			SimpleUtils.pass("Clopening hour been selected successfully");
		} else
			SimpleUtils.fail("Clopening Hours selector load fail", false);
	}

	@FindBy(css = "[question-title=\"Minimum number of hours of rest between consecutive closing and opening shifts (clopening).\"]")
	private WebElement clopeningHoursSectionOP;
	@FindBy(css = "[question-title=\"Minimum number of hours of rest between consecutive closing and opening shifts (clopening).\"] [ng-attr-name]")
	private WebElement clopeningHourOP;

	@Override
	public void selectClopeningHoursOP(String clopeningHour) throws Exception {
//		String test = clopeningHours.getAttribute("value");
		if (isElementLoaded(clopeningHoursSectionOP, 5)) {
			if(isElementLoaded(clopeningHourOP, 5)){
				clopeningHourOP.clear();
				clopeningHourOP.sendKeys(clopeningHour);
			}
			else{
				SimpleUtils.fail("Clopening Hours input field load fail", false);
			}
		} else
			SimpleUtils.fail("Clopening Hours section load fail", false);
	}

	@FindBy(css = "question-input[question-title=\"Can a manager add another locations' employee in schedule before the employee's home location has published the schedule?\"] input-field")
	private WebElement canManagerAddAnotherLocationsEmployeeInSchedule;

	@Override
	public void updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule(String option) throws Exception {

		//click Global location button
		clickOnGlobalLocationButton();
		// Wait for the values loaded
		waitForSeconds(10);
		WebElement confSelect = canManagerAddAnotherLocationsEmployeeInSchedule.findElement(By.cssSelector("select"));
		if(isElementLoaded(confSelect,5)) {
			selectByVisibleText(confSelect,option);
			displaySuccessMessage();
		} else{
			SimpleUtils.fail("Can a manager add another locations' employee in schedule before the employee's home location has published the schedule? input field not loaded.", false);
		}
	}

	@FindBy(css = "form-section[form-title=\"Scheduling Minors (Ages 14 & 15)\"]")
	private WebElement schedulingMinorRuleFor14N15;
	@FindBy(css = "form-section[form-title=\"Scheduling Minors (Ages 16 & 17)\"]")
	private WebElement schedulingMinorRuleFor16N17;

	/*
	 * Parameters:
	 * from, to: A Minor may be scheduled during $from AM to $to PM only.
	 * parameter1: A Minor’s schedule must not exceed 30 hours in a week.
	 * parameter2: A Minor’s schedule must not exceed 6 hours in a weekend and holiday.
	 * parameter3: A Minor’s schedule must not exceed 3 hours in a weekday.
	 * parameter4: A Minor must not be scheduled more than 6 days a week.
	 */
	@Override
	public void setSchedulingMinorRuleFor14N15(String from, String to, String parameter1, String parameter2, String parameter3, String parameter4) throws Exception {
		if (isElementLoaded(schedulingMinorRuleFor14N15,5) && schedulingMinorRuleFor14N15.findElements(By.cssSelector("div.lg-question-input")).size()==4){
			List<WebElement> parameters = schedulingMinorRuleFor14N15.findElements(By.cssSelector("div.lg-question-input"));
			if (isElementLoaded(schedulingMinorRuleFor14N15.findElement(By.cssSelector("input-field[label=\"From\"] select")),5)){
				selectByVisibleText(schedulingMinorRuleFor14N15.findElement(By.cssSelector("input-field[label=\"From\"] select")), from);
				SimpleUtils.pass("A Minor may be scheduled during X AM to X PM only. updated From with value: '" + from);
			} else {
				SimpleUtils.fail("Setting:\"A Minor may be scheduled during X AM to X PM only.\" fail to load!", false);
			}
			if (isElementLoaded(schedulingMinorRuleFor14N15.findElement(By.cssSelector("input-field[label=\"To\"] select")),5)){
				selectByVisibleText(schedulingMinorRuleFor14N15.findElement(By.cssSelector("input-field[label=\"To\"] select")), to);
				SimpleUtils.pass("A Minor may be scheduled during X AM to X PM only. updated To with value: '" + to);
			} else {
				SimpleUtils.fail("Setting:\"A Minor may be scheduled during X AM to X PM only.\" fail to load!", false);
			}
			setSchedulingMinorNumericRule(parameter1);
			setSchedulingMinorNumericRule(parameter2);
			setSchedulingMinorNumericRule(parameter3);
			setSchedulingMinorNumericRule(parameter4);

		} else {
			SimpleUtils.fail("No scheduling rule for Minors!",false);
		}
	}

	@Override
	public void setSchedulingMinorRuleFor16N17(String from, String to, String parameter1, String parameter2, String parameter3, String parameter4) throws Exception {
		if (isElementLoaded(schedulingMinorRuleFor16N17,5) && schedulingMinorRuleFor16N17.findElements(By.cssSelector("div.lg-question-input")).size()==4){
			List<WebElement> parameters = schedulingMinorRuleFor16N17.findElements(By.cssSelector("div.lg-question-input"));
			if (isElementLoaded(schedulingMinorRuleFor16N17.findElement(By.cssSelector("input-field[label=\"From\"] select")),5)){
				selectByVisibleText(schedulingMinorRuleFor16N17.findElement(By.cssSelector("input-field[label=\"From\"] select")), from);
				SimpleUtils.pass("A Minor may be scheduled during X AM to X PM only. updated From with value: '" + from);
			} else {
				SimpleUtils.fail("Setting:\"A Minor may be scheduled during X AM to X PM only.\" fail to load!", false);
			}
			if (isElementLoaded(schedulingMinorRuleFor16N17.findElement(By.cssSelector("input-field[label=\"To\"] select")),5)){
				selectByVisibleText(schedulingMinorRuleFor16N17.findElement(By.cssSelector("input-field[label=\"To\"] select")), to);
				SimpleUtils.pass("A Minor may be scheduled during X AM to X PM only. updated To with value: '" + to);
			} else {
				SimpleUtils.fail("Setting:\"A Minor may be scheduled during X AM to X PM only.\" fail to load!", false);
			}
			setSchedulingMinorNumericRule(parameter1);
			setSchedulingMinorNumericRule(parameter2);
			setSchedulingMinorNumericRule(parameter3);
			setSchedulingMinorNumericRule(parameter4);

		} else {
			SimpleUtils.fail("No scheduling rule for Minors!",false);
		}
	}

	private void setSchedulingMinorNumericRule(String parameter) throws Exception {
		List<WebElement> parameters = schedulingMinorRuleFor16N17.findElements(By.cssSelector("div.lg-question-input"));
		if (parameter.equalsIgnoreCase("close")){
			if (parameters.get(3).findElement(By.cssSelector("lg-switch input")).getAttribute("class").contains("ng-not-empty")){
				clickTheElement(parameters.get(3).findElement(By.cssSelector(".slider")));
				SimpleUtils.pass("Setting turned off: A Minor must not be scheduled more than 6 days a week.");
			}
		} else {
			if (parameters.get(3).findElement(By.cssSelector("lg-switch input")).getAttribute("class").contains("ng-empty")){
				click(parameters.get(3).findElement(By.cssSelector(".slider")));
				SimpleUtils.pass("Setting turned on: A Minor must not be scheduled more than 6 days a week.");
				parameters.get(3).findElement(By.cssSelector("input[type=\"number\"]")).clear();
				parameters.get(3).findElement(By.cssSelector("input[type=\"number\"]")).sendKeys(parameter);
				if (isElementLoaded(confirmSaveButton)) {
					clickTheElement(confirmSaveButton);
					preserveTheSetting();
				}
				SimpleUtils.pass("Setting set as "+parameter+": A Minor must not be scheduled more than 6 days a week.");
			} else {
				parameters.get(3).findElement(By.cssSelector("input[type=\"number\"]")).clear();
				parameters.get(3).findElement(By.cssSelector("input[type=\"number\"]")).sendKeys(parameter);
				if (isElementLoaded(confirmSaveButton)) {
					clickTheElement(confirmSaveButton);
					preserveTheSetting();
				}
				SimpleUtils.pass("Setting set as "+parameter+": A Minor must not be scheduled more than 6 days a week.");
			}
		}
	}

	@FindBy(css = ".group.ng-scope")
	private List<WebElement> accessSections;
	@Override
	public void verifyRolePermissionExists(String section, String permission) throws Exception {
		if (areListElementVisible(accessSections,10)){
			for (WebElement accessSection : accessSections){
				if (accessSection.findElement(By.tagName("span")).getText().equalsIgnoreCase(section)){
					if (!accessSection.getAttribute("class").contains("expand")){
						clickTheElement(accessSection);
					}
					List<WebElement> permissions = accessSection.findElements(By.cssSelector(".table-row"));
					for (WebElement permissionTemp : permissions){
						if (permissionTemp.getText().toLowerCase().contains(permission.toLowerCase())){
							SimpleUtils.pass("Found permission: "+ permission);
						}
					}
				}
			}
		} else {
			SimpleUtils.fail("No access item loaded!", false);
		}
	}

	@Override
	public void turnOnOrOffSpecificPermissionForSM(String section, String permission, String action) throws Exception {
		if (areListElementVisible(accessSections,10)){
			for (WebElement accessSection : accessSections){
				if (accessSection.findElement(By.tagName("span")).getText().equalsIgnoreCase(section)){
					if (!accessSection.findElement(By.cssSelector("div")).getAttribute("class").contains("expand")){
						clickTheElement(accessSection.findElement(By.tagName("span")));
					}
					int index = getTheIndexByAccessRolesName("Store Manager", accessSection);
					List<WebElement> permissions = accessSection.findElements(By.cssSelector(".table-row"));
					for (WebElement permissionTemp : permissions){
						String s = permissionTemp.getText();
						if (s!=null && s.toLowerCase().contains(permission.toLowerCase())){
							SimpleUtils.pass("Found permission: "+ permission);
							List<WebElement> permissionInputs = permissionTemp.findElements(By.tagName("input"));
							if (permissionInputs.size()>index) {
								if (permissionInputs.get(index).getAttribute("class").contains("ng-not-empty")) {
									if (action.equalsIgnoreCase("on")) {
										SimpleUtils.pass(permission + " already on!");
									} else {
										clickTheElement(permissionInputs.get(index));
										SimpleUtils.pass(permission + " unChecked!");
									}
								} else {
									if (action.equalsIgnoreCase("off")) {
										SimpleUtils.pass(permission + " already off!");
									} else {
										clickTheElement(permissionInputs.get(index));
										SimpleUtils.pass(permission + " Checked!");
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("No access item loaded!", false);
		}
	}

	@Override
	public void turnOnOrOffSpecificPermissionForSpecificRoles(String section, String roles, String permission, String action) throws Exception {
		if (areListElementVisible(accessSections,10)){
			for (WebElement accessSection : accessSections){
				if (accessSection.findElement(By.tagName("span")).getText().equalsIgnoreCase(section)){
					if (!accessSection.findElement(By.cssSelector("div")).getAttribute("class").contains("expand")){
						clickTheElement(accessSection.findElement(By.tagName("span")));
					}
					int index = getTheIndexByAccessRolesName(roles, accessSection);
					List<WebElement> permissions = accessSection.findElements(By.cssSelector(".table-row"));
					for (WebElement permissionTemp : permissions){
						String s = permissionTemp.getText();
						if (s!=null && s.toLowerCase().contains(permission.toLowerCase())){
							SimpleUtils.pass("Found permission: "+ permission);
							List<WebElement> permissionInputs = permissionTemp.findElements(By.tagName("input"));
							if (permissionInputs.size()>index) {
								if (permissionInputs.get(index).getAttribute("class").contains("ng-not-empty")) {
									if (action.equalsIgnoreCase("on")) {
										SimpleUtils.pass(permission + " already on!");
									} else {
										clickTheElement(permissionInputs.get(index));
										SimpleUtils.pass(permission + " unChecked!");
									}
								} else {
									if (action.equalsIgnoreCase("off")) {
										SimpleUtils.pass(permission + " already off!");
									} else {
										clickTheElement(permissionInputs.get(index));
										SimpleUtils.pass(permission + " Checked!");
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("No access item loaded!", false);
		}
	}

	private int getTheIndexByAccessRolesName(String roleName, WebElement accessSection) throws Exception {
		int index = -1;
		if (accessSection != null) {
			List<WebElement> accessRoles = accessSection.findElements(By.cssSelector(".header-row [ng-repeat*=\"headers\"]"));
			if (accessRoles != null && accessRoles.size() > 0) {
				for (int i = 0; i < accessRoles.size(); i++) {
					if (accessRoles.get(i).getText().equalsIgnoreCase(roleName)) {
						SimpleUtils.pass("Get the index of Role: " + roleName + ": " + i);
						index = i;
						break;
					}
				}
			}
		}
		if (index == -1) {
			SimpleUtils.fail("Failed to Get the index of Role: " + roleName, false);
		}
		return index;
	}

	@FindBy(css = "[form-title*=\"Schedule Copy\"]")
	private WebElement scheduleCopySection;
	@Override
	public void enableOrDisableScheduleCopyRestriction(String yesOrNo) throws Exception {
		if (isElementLoaded(scheduleCopySection,10)){
			scrollToElement(scheduleCopySection);
			if (yesOrNo.equalsIgnoreCase("yes")){
				if (isElementLoaded(scheduleCopySection.findElement(By.cssSelector(".lg-button-group-first")),10)){
					click(scheduleCopySection.findElement(By.cssSelector(".lg-button-group-first")));
					displaySuccessMessage();
					SimpleUtils.pass("Turned on Schedule Copy Restriction!");
				} else {
					SimpleUtils.fail("Yes button fail to load!", false);
				}
			} else if (yesOrNo.equalsIgnoreCase("no")){
				if (isElementLoaded(scheduleCopySection.findElement(By.cssSelector(".lg-button-group-last")),10)){
					click(scheduleCopySection.findElement(By.cssSelector(".lg-button-group-last")));
					displaySuccessMessage();
					SimpleUtils.pass("Turned off Schedule Copy Restriction!");
				} else {
					SimpleUtils.fail("No button fail to load!", false);
				}
			} else {
				SimpleUtils.warn("You have to input the right command: yes or no");
			}
		} else {
			SimpleUtils.fail("Schedule Copy Restriction section is not loaded!", false);
		}
	}

	@FindBy(css = "[form-title*=\"Schedule Copy\"] .lg-question-input")
	List<WebElement> configItems;
	@Override
	public void setCopyConfig(boolean onOrOff, String toggleName) throws Exception {
		if (isElementLoaded(scheduleCopySection,10) && areListElementVisible(configItems, 10)){
			scrollToElement(scheduleCopySection);
			for (WebElement item: configItems){
				if (item.findElement(By.tagName("h3")).getText().contains(toggleName)){
					if (onOrOff){
						if (isElementLoaded(item.findElement(By.cssSelector(".lg-button-group-first")),10)){
							scrollToElement(item.findElement(By.cssSelector(".lg-button-group-first")));
							waitForSeconds(2);
							click(item.findElement(By.cssSelector(".lg-button-group-first")));
							displaySuccessMessage();
							SimpleUtils.pass("Turned on "+ toggleName +"!");
							break;
						} else {
							SimpleUtils.fail("Yes button fail to load!", false);
						}
					} else {
						if (isElementLoaded(item.findElement(By.cssSelector(".lg-button-group-last")),10)){
							scrollToElement(item.findElement(By.cssSelector(".lg-button-group-first")));
							waitForSeconds(2);
							click(item.findElement(By.cssSelector(".lg-button-group-last")));
							displaySuccessMessage();
							SimpleUtils.pass("Turned off "+ toggleName +"!");
							break;
						} else {
							SimpleUtils.fail("No button fail to load!", false);
						}
					}
				}
			}
		} else {
			SimpleUtils.fail("Schedule Copy Restriction section is not loaded!", false);
		}
	}

	@Override
	public void setViolationLimit(String value) throws Exception {
		if (isElementLoaded(scheduleCopySection.findElement(By.cssSelector("[question-title=\"Violation limit\"]")),10)){
			waitForSeconds(3);
			scheduleCopySection.findElement(By.cssSelector("[question-title=\"Violation limit\"] [ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).clear();
			scheduleCopySection.findElement(By.cssSelector("[question-title=\"Violation limit\"] [ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).sendKeys(value);
			if (isElementLoaded(cancelSaveButton,10)) {
				clickTheElement(cancelSaveButton);
			}
			scheduleCopySection.findElement(By.cssSelector("[question-title=\"Violation limit\"] [ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).clear();
			scheduleCopySection.findElement(By.cssSelector("[question-title=\"Violation limit\"] [ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).sendKeys(value);
			if (isElementLoaded(confirmSaveButton,10)) {
				clickTheElement(confirmSaveButton);
			}
			displaySuccessMessage();
			if (scheduleCopySection.findElement(By.cssSelector("[question-title=\"Violation limit\"] .input-faked.ng-binding")).getAttribute("innerText").contains(value)){
				SimpleUtils.pass("Violation limit is set as "+value);
			} else {
				SimpleUtils.fail("Violation limit value fail to save!", false);
			}
		} else {
			SimpleUtils.fail("Violation limit fail to load!", false);
		}
	}

	@Override
	public void setBudgetOverageLimit(String value) throws Exception {
		if (isElementLoaded(scheduleCopySection.findElement(By.cssSelector("[question-title=\"Budget overage limit\"]")),10) && isElementLoaded(scheduleCopySection.findElement(By.cssSelector("[question-title=\"Budget overage limit\"] [include-percent-sign=\"true\"]")),10)){
			waitForSeconds(3);
			scheduleCopySection.findElement(By.cssSelector("[question-title=\"Budget overage limit\"] [ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).clear();
			scheduleCopySection.findElement(By.cssSelector("[question-title=\"Budget overage limit\"] [ng-class=\"{'ng-invalid': $ctrl.invalid}\"]")).sendKeys(value);
			if (isElementLoaded(confirmSaveButton,10)) {
				clickTheElement(confirmSaveButton);
			}
			displaySuccessMessage();
			if (!value.equals("0")){
				if (scheduleCopySection.findElement(By.cssSelector("[question-title=\"Budget overage limit\"] .input-faked.ng-binding")).getAttribute("innerText").contains(value)){
					SimpleUtils.pass("Budget overage limit is set as "+value);
				} else {
					SimpleUtils.fail("Violation limit value fail to save!", false);
				}
			}
		} else {
			SimpleUtils.fail("Budget overage limit fail to load!", false);
		}
	}

	private void test() throws Exception{

	}

	//added by Estelle to verify centralized schedule release
	@FindBy(css = "[question-title=\"Centralized Schedule Release\"] yes-no")
	private WebElement centralizedScheduleRelease;



	@Override
	public boolean isCentralizedScheduleReleaseValueYes() throws Exception {
		if (isElementLoaded(centralizedScheduleRelease)) {
			WebElement centralizedScheduleReleaseGroup = centralizedScheduleRelease.findElement(
					By.cssSelector("div.lg-button-group"));
			if (centralizedScheduleReleaseGroup.getAttribute("class").contains("lg-button-group-selected")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public List<WebElement> getAvailableSelector() {

			List<WebElement> filters = centralizedScheduleRelease.findElements(By.cssSelector("ng-form > lg-button-group > div >div"/*"[ng-repeat=\"opt in opts\"]"*/));

		    return filters;
	}

	@Override
	public void updateCentralizedScheduleRelease(WebElement yesItem) throws Exception {
		if (isElementLoaded(centralizedScheduleRelease)) {
			clickTheElement(yesItem);
			SimpleUtils.pass("Success! this setting has been updated to all locations");
		}else
			SimpleUtils.fail("Centralized scheduling release load failed",false);
	}

	// Added By Julie
	@FindBy(css = "[question-title=\"How many days in advance would you typically publish schedules? (this is the Schedule Publish Window).\"] input-field")
	private WebElement schedulingPoliciesDaysInAdvancePublishSchedules;

	@FindBy(className = "lg-override-popup")
	private WebElement overridePopup;

	@Override
	public String getDaysInAdvancePublishSchedulesInSchedulingPolicies() throws Exception {
		// How many days in advance would you typically publish schedules? (this is the Schedule Publish Window).
		String days = "";
		if (isElementLoaded(schedulingPoliciesDaysInAdvancePublishSchedules,10)) {
			WebElement daysInputBox = schedulingPoliciesDaysInAdvancePublishSchedules.findElement(By.cssSelector("input"));
			days = daysInputBox.getAttribute("value");
		} else
			SimpleUtils.fail("Scheduling Policies: 'How many days in advance would you typically publish schedules?' not loaded", false);
		return days;
	}

	@FindBy(css = "question-input[question-title=\"Auto Publish Legion Schedule\"]")
	private WebElement autoPublishSection;
	@FindBy(css = "question-input[question-title=\"Schedule Publish Day of Week\"]")
	private WebElement autoPublishSchedulePublishDayOfWeek;
	@FindBy(css = "question-input[question-title=\"Schedule Publish Time of Day (in minutes)\"]")
	private WebElement autoPublishSchedulePublishTimeOfDay;
	@FindBy(css = "question-input[question-title=\"Schedule Republish Day of Week\"]")
	private WebElement autoPublishScheduleRepublishDayOfWeek;
	@FindBy(css = "question-input[question-title=\"Schedule Republish Time of Day (in minutes)\"]")
	private WebElement autoPublishScheduleRepublishTimeOfDay;
	@Override
	public void updateAndVerifyAutoPublishSettings(String option) throws Exception {
		String expectedTitle = "Auto Publish Legion Schedule";
		String expectedExplainer = "'Auto publish after date' will account for late schedules that were created after 'Schedule Publish Day of Week'";
		if (isElementLoaded(autoPublishSection, 10) && isElementLoaded(autoPublishSection.findElement(By.cssSelector(".lg-question-input__text")), 10)){
			SimpleUtils.assertOnFail("The title is not expected!", autoPublishSection.findElement(By.cssSelector(".lg-question-input__text")).getText().equalsIgnoreCase(expectedTitle), false);
			SimpleUtils.assertOnFail("The explainer is not expected!", autoPublishSection.getAttribute("explainer").equalsIgnoreCase(expectedExplainer), false);
			selectByVisibleText(autoPublishSection.findElement(By.tagName("select")), option);
		} else{
			SimpleUtils.fail("Fail to find this setting!", false);
		}
	}

	@Override
	public String getAutoPublishSettings() throws Exception {
		if (isElementLoaded(autoPublishSection.findElement(By.cssSelector("option[selected=\"selected\"]")), 10)){
			return autoPublishSection.findElement(By.cssSelector("option[selected=\"selected\"]")).getText();
		}
		return null;
	}

	@Override
	public void updateAutoPublishSchedulePublishDayOfWeek(String option) throws Exception {
		String expectedTitle = "Schedule Publish Day of Week";
		if (isElementLoaded(autoPublishSchedulePublishDayOfWeek, 10) && isElementLoaded(autoPublishSchedulePublishDayOfWeek.findElement(By.cssSelector(".lg-question-input__text")), 10)){
			SimpleUtils.assertOnFail("The title is not expected!", autoPublishSchedulePublishDayOfWeek.findElement(By.cssSelector(".lg-question-input__text")).getText().equalsIgnoreCase(expectedTitle), false);
			selectByVisibleText(autoPublishSchedulePublishDayOfWeek.findElement(By.tagName("select")), option);
		} else{
			SimpleUtils.fail("Fail to find this setting: "+ expectedTitle, false);
		}
	}

	@Override
	public String getAutoPublishSchedulePublishDayOfWeek() throws Exception {
		if (isElementLoaded(autoPublishSchedulePublishDayOfWeek.findElement(By.cssSelector("option[selected=\"selected\"]")), 10)){
			return autoPublishSchedulePublishDayOfWeek.findElement(By.cssSelector("option[selected=\"selected\"]")).getText();
		}
		return null;
	}

	@Override
	public void updateAutoPublishScheduleRepublishDayOfWeek(String option) throws Exception {
		String expectedTitle = "Schedule Republish Day of Week";
		if (isElementLoaded(autoPublishScheduleRepublishDayOfWeek, 10) && isElementLoaded(autoPublishScheduleRepublishDayOfWeek.findElement(By.cssSelector(".lg-question-input__text")), 10)){
			SimpleUtils.assertOnFail("The title is not expected!", autoPublishScheduleRepublishDayOfWeek.findElement(By.cssSelector(".lg-question-input__text")).getText().equalsIgnoreCase(expectedTitle), false);
			selectByVisibleText(autoPublishScheduleRepublishDayOfWeek.findElement(By.tagName("select")), option);
		} else{
			SimpleUtils.fail("Fail to find this setting: "+ expectedTitle, false);
		}
	}

	@Override
	public String getAutoPublishScheduleRepublishDayOfWeek() throws Exception {
		if (isElementLoaded(autoPublishScheduleRepublishDayOfWeek.findElement(By.cssSelector("option[selected=\"selected\"]")), 10)){
			return autoPublishScheduleRepublishDayOfWeek.findElement(By.cssSelector("option[selected=\"selected\"]")).getText();
		}
		return null;
	}

	@Override
	public void updateAutoPublishSchedulePublishTimeOfDay(String mins) throws Exception {
		String expectedTitle = "Schedule Publish Time of Day (in minutes)";
		if (isElementLoaded(autoPublishSchedulePublishTimeOfDay, 10) && isElementLoaded(autoPublishSchedulePublishTimeOfDay.findElement(By.cssSelector(".lg-question-input__text")), 10)){
			SimpleUtils.assertOnFail("The title is not expected!", autoPublishSchedulePublishTimeOfDay.findElement(By.cssSelector(".lg-question-input__text")).getText().equalsIgnoreCase(expectedTitle), false);
			autoPublishSchedulePublishTimeOfDay.findElement(By.tagName("input")).clear();
			autoPublishSchedulePublishTimeOfDay.findElement(By.tagName("input")).sendKeys(mins);
		} else{
			SimpleUtils.fail("Fail to find this setting: "+ expectedTitle, false);
		}
	}

	@Override
	public String getAutoPublishSchedulePublishTimeOfDay() throws Exception {
		waitForSeconds(2);
		if (isElementLoaded(autoPublishSchedulePublishTimeOfDay.findElement(By.cssSelector("input")), 10)){
			return autoPublishSchedulePublishTimeOfDay.findElement(By.cssSelector("input")).getAttribute("value");
		}
		return null;
	}

	@Override
	public void updateAutoPublishScheduleRepublishTimeOfDay(String mins) throws Exception {
		String expectedTitle = "Schedule Republish Time of Day (in minutes)";
		if (isElementLoaded(autoPublishScheduleRepublishTimeOfDay, 10) && isElementLoaded(autoPublishScheduleRepublishTimeOfDay.findElement(By.cssSelector(".lg-question-input__text")), 10)){
			SimpleUtils.assertOnFail("The title is not expected!", autoPublishScheduleRepublishTimeOfDay.findElement(By.cssSelector(".lg-question-input__text")).getText().equalsIgnoreCase(expectedTitle), false);
			autoPublishScheduleRepublishTimeOfDay.findElement(By.tagName("input")).clear();
			autoPublishScheduleRepublishTimeOfDay.findElement(By.tagName("input")).sendKeys(mins);
		} else{
			SimpleUtils.fail("Fail to find this setting: "+ expectedTitle, false);
		}
	}

	@Override
	public String getAutoPublishScheduleRepublishTimeOfDay() throws Exception {
		if (isElementLoaded(autoPublishScheduleRepublishTimeOfDay.findElement(By.cssSelector("input")), 10)){
			return autoPublishScheduleRepublishTimeOfDay.findElement(By.cssSelector("input")).getAttribute("value");
		}
		return null;
	}

	@Override
	public void updateDaysInAdvancePublishSchedulesInSchedulingPolicies(String days) throws Exception {
		// How many days in advance would you typically publish schedules? (this is the Schedule Publish Window).
		if (isElementLoaded(schedulingPoliciesDaysInAdvancePublishSchedules,10)) {
			WebElement daysInputBox = schedulingPoliciesDaysInAdvancePublishSchedules.findElement(
					By.cssSelector("input"));
			if (daysInputBox.isEnabled()) {
				daysInputBox.clear();
				daysInputBox.sendKeys(days);
				if (isElementLoaded(confirmSettingsChangeButton, 5))
					click(confirmSettingsChangeButton);
				if (isElementLoaded(overridePopup,10))
					click(overridePopup.findElement(By.cssSelector("[label=\"Overwrite\"]")));
				waitForSeconds(3);
				String daysDisplayed = getDaysInAdvancePublishSchedulesInSchedulingPolicies();
				if (daysDisplayed.equals(days))
					SimpleUtils.pass("Scheduling Policies: The days in advance publish schedules '" + days + "' entered successfully");
				else
					SimpleUtils.fail("Scheduling Policies: The days in advance publish schedules '" + days + "' not entered", false);
			} else
				SimpleUtils.report("Scheduling Policies: The days in advance publish schedules 'Disabled'");
		} else
			SimpleUtils.fail("Scheduling Policies: 'How many days in advance would you typically publish schedules?' not loaded", false);
	}


	//Added by Mary to check 'Automatically convert unassigned shifts to open shifts when generating and copying a schedule?'
	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when generating and copying a schedule?\"]")
	private WebElement convertUnassignedShiftsToOpenSetting;

	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when generating and copying a schedule?\"] .lg-question-input__text")
	private WebElement convertUnassignedShiftsToOpenSettingMessage;

	@FindBy(css = "question-input[question-title=\"Automatically convert unassigned shifts to open shifts when generating and copying a schedule?\"] select[ng-change=\"$ctrl.handleChange()\"]")
	private WebElement convertUnassignedShiftsToOpenSettingDropdown;

	@Override
	public void verifyConvertUnassignedShiftsToOpenSetting() throws Exception {
		if (isElementLoaded(convertUnassignedShiftsToOpenSetting, 10)
				&& isElementLoaded(convertUnassignedShiftsToOpenSettingMessage, 10)
				&& isElementLoaded(convertUnassignedShiftsToOpenSettingDropdown, 10)) {

			//Check the message
			String message = "Automatically convert unassigned shifts to open shifts when generating and copying a schedule?";
			if (convertUnassignedShiftsToOpenSettingMessage.getText().equalsIgnoreCase(message)){
				SimpleUtils.pass("Controls - Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings message display correctly! ");
			} else
				SimpleUtils.fail("Controls - Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings message display incorrectly!  Expected message is :'"
						+ message + "'. Actual message is : '" +convertUnassignedShiftsToOpenSettingMessage.getText()+ "'", false);

			List<String> convertUnassignedShiftsToOpenSettingOptions = new ArrayList<>();
			convertUnassignedShiftsToOpenSettingOptions.add("Yes, all unassigned shifts");
			convertUnassignedShiftsToOpenSettingOptions.add("Yes, except opening/closing shifts");
			convertUnassignedShiftsToOpenSettingOptions.add("No, keep as unassigned");

			//Check the options
			Select dropdown = new Select(convertUnassignedShiftsToOpenSettingDropdown);
			List<WebElement> dropdownOptions = dropdown.getOptions();
			for (int i = 0; i< dropdownOptions.size(); i++) {
				if (dropdownOptions.get(i).getText().equalsIgnoreCase(convertUnassignedShiftsToOpenSettingOptions.get(i))) {
					SimpleUtils.pass("Controls - Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings option: '" +dropdownOptions.get(i).getText()+ "' display correctly! ");
				} else
					SimpleUtils.fail("Controls - Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings option display incorrectly, expected is : '" +convertUnassignedShiftsToOpenSettingOptions.get(i)+
							"' , the actual is : '"+ dropdownOptions.get(i).getText()+"'. ", false);
			}

		} else
			SimpleUtils.fail("Controls Page: Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings not loaded.", false);
	}


	@Override
	public String getConvertUnassignedShiftsToOpenSettingOption() throws Exception {
		String selectedOptionLabel = "";
		if (isElementLoaded(convertUnassignedShiftsToOpenSetting, 10)) {
			if (isElementLoaded(convertUnassignedShiftsToOpenSettingDropdown, 10)) {
				Select dropdown = new Select(convertUnassignedShiftsToOpenSettingDropdown);
				selectedOptionLabel = dropdown.getFirstSelectedOption().getText();
			} else
				SimpleUtils.fail("Controls - Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings dropdown list not loaded.", false);
		} else
			SimpleUtils.fail("Controls Page: Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings not loaded.", false);
		return selectedOptionLabel;
	}

	@Override
	public void updateConvertUnassignedShiftsToOpenSettingOption(String option) throws Exception {
		if (isElementLoaded(convertUnassignedShiftsToOpenSettingDropdown, 10)) {
			Select dropdown = new Select(convertUnassignedShiftsToOpenSettingDropdown);
			dropdown.selectByVisibleText(option);
			displaySuccessMessage();
			SimpleUtils.pass("Controls Page: Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings been changed successfully");
		} else {
			SimpleUtils.fail("Controls - Schedule Collaboration: Open Shift : Convert unassigned shifts to open settings dropdown list not loaded.", false);
		}

	}

	@FindBy(css ="[question-title*=\"onboarded employees\"] span")
	private List<WebElement> yesNoForAutomaticallySetOnboardedEmployeesToActive;
	@FindBy(css = "[question-title*=\"onboarded employees\"] .lg-button-group-selected span")
	private WebElement selectedValueForAutoSetOnboardingToActive;

	@Override
	public boolean getTheSettingForAutomaticallySetOnboardedEmployeesToActive() throws Exception {
		boolean isSetActive = false;
		try {
			if (isElementLoaded(selectedValueForAutoSetOnboardingToActive, 10)) {
				if (selectedValueForAutoSetOnboardingToActive.getText().equalsIgnoreCase("Yes")) {
					isSetActive = true;
				}
				SimpleUtils.report("Get the setting for \"Automatically set onboarded employees to active?\": " + selectedValueForAutoSetOnboardingToActive.getText());
			}
		} catch (Exception e) {
			isSetActive = false;
		}
		return isSetActive;
	}

	@Override
	public void setAutomaticallySetOnboardedEmployeesToActive(String yesOrNo) throws Exception {
		// wait for the value to be loaded
		waitForSeconds(5);
		if (areListElementVisible(yesNoForAutomaticallySetOnboardedEmployeesToActive,5)) {
			for (WebElement option : yesNoForAutomaticallySetOnboardedEmployeesToActive) {
				if (option.getText().equalsIgnoreCase(yesOrNo)) {
					clickTheElement(option);
					overwriteTheSetting();
					break;
				}
			}

			SimpleUtils.pass("Set Automatically set onboarded employees to active? to "+ yesOrNo + " successfully! ");
		}else
			SimpleUtils.fail("Automatically set onboarded employees to active? setting fail to load!  ",false);
	}

	@FindBy(css ="div.lg-input-wrapper__wrapper input-field")
	private List<WebElement> accessRoles;

	@FindBy(css ="lg-button[label=\"Edit\"]")
	private WebElement editButton;

	@FindBy(css ="lg-button[label=\"Save\"]")
	private WebElement saveButton;


	public void selectAccessRoles (List<String> selectAccessRoles) throws Exception {
		if (isElementLoaded(editButton, 10)) {
			click(editButton);
			if (areListElementVisible(accessRoles, 5) && selectAccessRoles.size()>0) {
				for (WebElement accessRole : accessRoles){
					String accessRoleName = accessRole.findElement(By.tagName("label")).getText();
					for (String selectAccessRole: selectAccessRoles) {
						if (accessRoleName.equalsIgnoreCase(selectAccessRole)){
							WebElement checkBox = accessRole.findElement(By.tagName("input"));
							if (checkBox.getAttribute("class").contains("ng-empty")){
								click(checkBox);
							}
							break;
						}
					}
				}
			} else
				SimpleUtils.fail("Access roles fail to load or select access roles are null! ", false);
		} else
			SimpleUtils.fail("Edit button fail to load! ", false);
		scrollToElement(saveButton);
		click(saveButton);
		displaySuccessMessage();

	}

	@FindBy(css ="input[placeholder=\"http://...\"]")
	private WebElement companyMobilePolicyURL;

	public boolean hasCompanyMobilePolicyURLOrNot () throws Exception {
		boolean hasCompanyMobilePolicyURL = false;
		waitForSeconds(10);
		if (isElementLoaded(companyMobilePolicyURL, 5)){
			String url = companyMobilePolicyURL.getAttribute("value");
			if (!url.equals("") && !url.equals("http://...")){
				hasCompanyMobilePolicyURL = true;
			} else
				SimpleUtils.report("The company mobile policy URL is empty");
		} else
			SimpleUtils.fail("The company mobile policy fail to load! ", false);
		setCompanyPolicy(hasCompanyMobilePolicyURL);
		return hasCompanyMobilePolicyURL;
	}

	@FindBy(css = ".lg-table label.switch input.ng-not-empty")
	private List<WebElement> enabledDayparts;

	@FindBy(css = "[block-title=\"'Dayparts'\"] a[ng-show=\"collapsed\"]")
	private WebElement daypartsCollapsed;

	@FindBy(css = "[block-title=\"'Regular'\"] a[ng-show=\"collapsed\"]")
	private WebElement regularCollapsed;

	@FindBy(css = "[ng-click=\"saveDayParts()\"]")
	private WebElement saveDayPartsButton;

	@FindBy(css = ".lg-table label.switch input[type=\"checkbox\"]")
	private List<WebElement> dayPartsSlider;

	@FindBy(css = ".row-name")
	private List<WebElement> dayPartNames;

	@FindBy(css = "[label=\"Add Daypart\"]")
	private WebElement addDayPartsBtn;

	@FindBy(className = "lg-modal")
	private WebElement dialogWindow;

	@FindBy(css = "input[aria-label=\"Name\"]")
	private WebElement newDayPartName;

	@FindBy(css = "input[aria-label=\"Description\"]")
	private WebElement newDayPartDescription;

	@FindBy(css = "[label=\"Create Daypart\"] button")
	private WebElement createDaypartBtn;

	@FindBy(css = "lg-badge-selector-item")
	private List<WebElement> dayPartsColors;

	@FindBy(css = "[day-type=\"'workday'\"] [label=\"Edit\"] span>span")
	private List<WebElement> editBtnsOfRegular;

	@FindBy(css = ".location-working-hours nav.lg-tabs__nav div")
	private List<WebElement> tabsWhenEditRegular;

	@FindBy(css = "[label=\"All days\"] ng-form")
	private WebElement allDayCheckBox;

	@FindBy(css = ".location-working-hours [label=\"Save\"]")
	private WebElement saveBtnWhenEditRegular;

	@Override
	public void disableAllDayparts() throws Exception {
		if (isElementLoaded(daypartsCollapsed, 20) && !daypartsCollapsed.getAttribute("class").contains("ng-hide"))
			click(daypartsCollapsed);
		if (dayPartNames.size() > 0) {
			for (int i = 0; i < dayPartNames.size(); i++) {
				WebElement slider = dayPartNames.get(i).findElement(By.xpath("./..//ng-form"));
				if (slider.findElement(By.xpath("./input")).getAttribute("class").contains("ng-not-empty")) {
					clickTheElement(slider.findElement(By.xpath("./span")));
				}
				clickTheElement(saveDayPartsButton);
			}
		}
		if (!areListElementVisible(enabledDayparts, 10))
			SimpleUtils.pass("Controls Page: All day parts have been disabled in working hours");
		else
			SimpleUtils.fail("Controls Page: All day parts have been not disabled in working hours", false);
	}


	@Override
	public void turnOnOrOffSpecificPermissionForDifferentRole(String rolePermission, String section, String permission, String action) throws Exception {
		if (areListElementVisible(accessSections,10)){
			click(editButton);
			for (WebElement accessSection : accessSections){
				if (accessSection.findElement(By.tagName("span")).getText().equalsIgnoreCase(section)){
					if (!accessSection.findElement(By.cssSelector("div")).getAttribute("class").contains("expand")){
						clickTheElement(accessSection.findElement(By.tagName("span")));
					}
					int index = getTheIndexByAccessRolesName(rolePermission, accessSection);
					List<WebElement> permissions = accessSection.findElements(By.cssSelector(".table-row"));
					for (WebElement permissionTemp : permissions){
						String s = permissionTemp.getText();
						if (s!=null && s.toLowerCase().contains(permission.toLowerCase())){
							SimpleUtils.pass("Found permission: "+ permission);
							List<WebElement> permissionInputs = permissionTemp.findElements(By.tagName("input"));
							if (permissionInputs.size()>index) {
								if (permissionInputs.get(index).getAttribute("class").contains("ng-not-empty")) {
									if (action.equalsIgnoreCase("on")) {
										SimpleUtils.pass(permission + " already on!");
									} else {
										clickTheElement(permissionInputs.get(index));
										SimpleUtils.pass(permission + " unChecked!");
									}
								} else {
									if (action.equalsIgnoreCase("off")) {
										SimpleUtils.pass(permission + " already off!");
									} else {
										clickTheElement(permissionInputs.get(index));
										SimpleUtils.pass(permission + " Checked!");
									}
								}
								click(saveButton);
								break;
							}
						}
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("No access item loaded!", false);
		}
	}

	@Override
	public void enableDaypart(String dayPart) throws Exception {
		boolean isDaypartPresent = false;
		if (isElementLoaded(daypartsCollapsed, 20) && !daypartsCollapsed.getAttribute("class").contains("ng-hide"))
			click(daypartsCollapsed);
		if (areListElementVisible(dayPartNames, 30)) {
			for (int i = 0; i < dayPartNames.size(); i++) {
				if (dayPartNames.get(i).getText().equals(dayPart)) {
					isDaypartPresent = true;
					WebElement slider1 = dayPartNames.get(i).findElement(By.xpath("./..//ng-form"));
					if (slider1.findElement(By.xpath("./input")).getAttribute("class").contains("ng-empty")) {
						clickTheElement(slider1.findElement(By.xpath("./span")));
						clickTheElement(saveDayPartsButton);
						SimpleUtils.pass("Controls Page: Enable day part '" + dayPart + "' successfully");
						continue;
					}
				}
			}
		}
		if (isElementLoaded(addDayPartsBtn,30)) {
			if (!isDaypartPresent) {
				click(addDayPartsBtn);
				if (isElementLoaded(dialogWindow, 10)) {
					newDayPartName.sendKeys(dayPart);
					newDayPartDescription.sendKeys(dayPart);
					click(dayPartsColors.get((new Random()).nextInt(dayPartsColors.size())));
					click(createDaypartBtn);
					for (int i = 0; i < dayPartNames.size(); i++) {
						if (dayPartNames.get(i).getText().equals(dayPart)) {
							WebElement slider1 = dayPartNames.get(i).findElement(By.xpath("./..//ng-form"));
							if (slider1.findElement(By.xpath("./input")).getAttribute("class").contains("ng-empty")) {
								clickTheElement(slider1.findElement(By.xpath("./span")));
								clickTheElement(saveDayPartsButton);
								SimpleUtils.pass("Controls Page: Enable day part '" + dayPart + "' successfully");
								break;
							}
						}
					}
				} else
					SimpleUtils.fail("Controls Page: Add day parts window does not appear", false);
			}
		} else
			SimpleUtils.fail("Controls Page: Day parts failed to load",false);
	}

	@Override
	public void setDaypart(String dayPart, String startTime, String endTime) throws Exception {
		if (isElementLoaded(regularCollapsed, 20) && !regularCollapsed.getAttribute("class").contains("ng-hide"))
			click(regularCollapsed);
        clickTheElement(editBtnsOfRegular.get(0));
		if (areListElementVisible(tabsWhenEditRegular,10))
			click(tabsWhenEditRegular.get(1));
        // todo: Blocked by bug https://legiontech.atlassian.net/browse/SCH-4355
		click(allDayCheckBox);
        click(saveBtnWhenEditRegular);
	}


	@FindBy(css = "[form-title=\"Spread of Hours\"]")
	private WebElement spreadOfHoursSection;
	@FindBy(css = "[form-title=\"Spread of Hours\"] [label=\"Edit\"]")
	private WebElement spreadOfHoursEditBtn;
	@FindBy(css = ".modal-dialog")
	private WebElement editDialog;
	@Override
	public void turnOnOrTurnOffSpreadOfHoursToggle(boolean action) throws Exception {
		String content = getSpreadOfHoursContent();
		if (isElementLoaded(spreadOfHoursSection, 20)
				&&spreadOfHoursSection.findElement(By.cssSelector(".info")).getText().equalsIgnoreCase("Spread Of Hours")
				&&content.contains("An employee will receive a ")
				&&content.contains(" hour premium if the total time from the beginning and end of the work day is greater than ")
				&&content.contains(" hours.")){
			if (isElementLoaded(spreadOfHoursSection.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
				if (action && spreadOfHoursSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
					scrollToElement(spreadOfHoursSection.findElement(By.cssSelector(".lg-question-input__toggle")));
					clickTheElement(spreadOfHoursSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					displaySuccessMessage();
					SimpleUtils.pass("Toggle is turned on!");
				} else if (!action && !spreadOfHoursSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
					clickTheElement(spreadOfHoursSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					displaySuccessMessage();
					SimpleUtils.pass("Toggle is turned off!");
				} else {
					SimpleUtils.pass("Toggle status is expected!");
				}
			} else {
				SimpleUtils.fail("Toggle fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Spread Of Hours section fail to load!", false);
		}
	}

	public String getSpreadOfHoursContent() throws Exception{
		if (isElementLoaded(spreadOfHoursSection, 10)){
			return spreadOfHoursSection.findElement(By.cssSelector(".lg-question-input__text")).getText();
		}
		return "";
	}


	@Override
	public void editSpreadOfHoursPremium(String numOfPremiumHrs, String greaterThan, boolean saveOrNot) throws Exception {
		if (isElementLoaded(spreadOfHoursEditBtn, 5)){
			String contentBefore = getSpreadOfHoursContent();
			clickTheElement(spreadOfHoursEditBtn);
			if (isElementLoaded(editDialog, 10)){
				//check the title.
				if (editDialog.findElement(By.cssSelector(".lg-modal__title")).getText().trim().equalsIgnoreCase("Edit Spread Shift Premium")){
					SimpleUtils.pass("Dialog title is expected!");
				} else {
					SimpleUtils.fail("Dialog title is not correct", false);
				}
				//Check setting content.
				if(editDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("An employee will receive a")
						&&editDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("hour premium if the total time from the beginning and end of the work day is greater than")
						&&editDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("hours.")){
					SimpleUtils.pass("Setting content in the dialog is expected!");
				} else {
					SimpleUtils.fail("Setting content is not expected!", false);
				}
				//edit the content, input the parameters.
				if (editDialog.findElements(By.cssSelector("input")).size() == 2){
					editDialog.findElements(By.cssSelector("input")).get(0).clear();
					editDialog.findElements(By.cssSelector("input")).get(0).sendKeys(numOfPremiumHrs);
					editDialog.findElements(By.cssSelector("input")).get(1).clear();
					editDialog.findElements(By.cssSelector("input")).get(1).sendKeys(greaterThan);
				} else {
					SimpleUtils.fail("inputs are not shown as expected!", false);
				}
				//save or cancel.
				if (isElementLoaded(editDialog.findElement(By.cssSelector("[label=\"Cancel\"]")), 5)
						&&isElementLoaded(editDialog.findElement(By.cssSelector("[label=\"Save\"]")), 5)){
					if (saveOrNot){
						clickTheElement(editDialog.findElement(By.cssSelector("[label=\"Save\"] button")));
						Thread.sleep(3000);
						SimpleUtils.assertOnFail("Setting is not saved successfully!", getSpreadOfHoursContent().contains(numOfPremiumHrs)&&getSpreadOfHoursContent().contains(greaterThan), false);
					} else {
						clickTheElement(editDialog.findElement(By.cssSelector("[label=\"Cancel\"] button")));
						SimpleUtils.assertOnFail("Setting should the same as before!", contentBefore.equalsIgnoreCase(getSpreadOfHoursContent()), false);
					}
				} else {
					SimpleUtils.fail("Save or Cancel button fail to load!", false);
				}
			} else {
				SimpleUtils.fail("Spread Of Hours dialog fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Edit Spread Of Hours button is not loaded!", false);
		}
	}


	@FindBy(css = "lg-close.dismiss")
	private WebElement closeButtonOnDialog;
	@Override
	public void verifyCloseSplitShiftPremiumDialogButton () throws Exception {
		if (isElementLoaded(spreadOfHoursEditBtn, 5)){
			String contentBefore = getSpreadOfHoursContent();
			clickTheElement(spreadOfHoursEditBtn);
			if (isElementLoaded(editDialog, 10)){

				//edit the content, input the parameters.
				if (editDialog.findElements(By.cssSelector("input")).size() == 2){
					editDialog.findElements(By.cssSelector("input")).get(0).clear();
					editDialog.findElements(By.cssSelector("input")).get(0).sendKeys("0");
					editDialog.findElements(By.cssSelector("input")).get(1).clear();
					editDialog.findElements(By.cssSelector("input")).get(1).sendKeys("0");
				} else {
					SimpleUtils.fail("inputs are not shown as expected!", false);
				}
				//click X button on the dialog
				if (isElementLoaded(closeButtonOnDialog, 5)){
					clickTheElement(closeButtonOnDialog);
					SimpleUtils.assertOnFail("Setting should the same as before!", contentBefore.equalsIgnoreCase(getSpreadOfHoursContent()), false);
				} else {
					SimpleUtils.fail("Save or Cancel button fail to load!", false);
				}
			} else {
				SimpleUtils.fail("Spread Of Hours dialog fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Edit Spread Of Hours button is not loaded!", false);
		}
	}

	@FindBy(css = "[form-title=\"Meal & Rest Breaks\"]")
	private WebElement mealRestBreaksSection;

	@FindBy(xpath = "//question-input[contains(@question-title, 'Meal Break for every')]/div")
	private WebElement mealBreakSetting;

	@FindBy(xpath = "//question-input[contains(@question-title, 'Meal Break for every')]/div/div/ng-transclude/lg-button")
	private WebElement mealBreakEditButton;

	@Override
	public void turnOnOrTurnOffMealBreakToggle(boolean action) throws Exception {
		String content = getMealBreakContent();
		if (isElementLoaded(mealRestBreaksSection, 10)
				&&mealRestBreaksSection.findElement(By.cssSelector(".info")).getText().equalsIgnoreCase("Meal & Rest Breaks")
				&&content.contains("An employee should be scheduled for")
				&&content.contains("Meal Break for every")
				&&content.contains(" of work.")){
			if (isElementLoaded(mealBreakSetting.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
				if (action && mealBreakSetting.getAttribute("class").contains("off")){
					scrollToElement(mealBreakSetting.findElement(By.cssSelector(".lg-question-input__toggle")));
					clickTheElement(mealBreakSetting.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					displaySuccessMessage();
					SimpleUtils.pass("Toggle is turned on!");
				} else if (!action && !mealBreakSetting.getAttribute("class").contains("off")){
					clickTheElement(mealBreakSetting.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					displaySuccessMessage();
					SimpleUtils.pass("Toggle is turned off!");
				} else {
					SimpleUtils.pass("Toggle status is expected!");
				}
			} else {
				SimpleUtils.fail("Toggle fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Meal and Rest Breaks section fail to load!", false);
		}
	}


	public String getMealBreakContent() throws Exception{
		if (isElementLoaded(mealBreakSetting, 10)){
			return mealBreakSetting.getText();
		}
		return "";
	}


	@FindBy(css = "[options=\"$ctrl.selections.duration\"] select")
	private WebElement mealBreakDurationSelector;

	@FindBy(css = "[options=\"$ctrl.selections.type\"] select")
	private WebElement mealBreakPaidTypeSelector;

	@Override
	public void editMealBreak(String mealBreakDuration, String paidType, String scheduleHoursLimit, boolean saveOrNot) throws Exception {
		if (isElementLoaded(mealBreakEditButton, 5)){
			String contentBefore = getMealBreakContent();
			clickTheElement(mealBreakEditButton);
			if (isElementLoaded(editDialog, 10)){
				//check the title.
				if (editDialog.findElement(By.cssSelector(".lg-modal__title")).getText().trim().equalsIgnoreCase("Edit Meal & Rest Breaks")){
					SimpleUtils.pass("Dialog title is expected!");
				} else {
					SimpleUtils.fail("Dialog title is not correct", false);
				}
				//Check setting content.
				String settingContent = editDialog.findElement(By.cssSelector(".lg-modal__body")).getText();
				if(settingContent.contains("Your state compliance laws are the default settings for this section, you can further edit the settings to fit your business.")
						&& settingContent.contains("An employee should be scheduled for")
						&& settingContent.contains("Meal Break for every")
						&&settingContent.contains("hours of work.")){
					SimpleUtils.pass("Setting content in the dialog is expected!");
				} else {
					SimpleUtils.fail("Setting content is not expected!", false);
				}
				//edit the content, input the parameters.
				if (isElementLoaded(mealBreakDurationSelector, 10)
						&& isElementLoaded(mealBreakPaidTypeSelector, 10)
						&& isElementLoaded(editDialog.findElement(By.cssSelector("input")))) {
					//set Meal break duration selector
					selectByVisibleText(mealBreakDurationSelector,mealBreakDuration);

					//set Meal break paid type selector
					selectByVisibleText(mealBreakPaidTypeSelector,paidType);

					//set schedule Hours Limit hour
					editDialog.findElement(By.cssSelector("input")).clear();
					editDialog.findElement(By.cssSelector("input")).sendKeys(scheduleHoursLimit);
				} else {
					SimpleUtils.fail("Parameters are not shown as expected!", false);
				}

				//save or cancel.
				if (isElementLoaded(editDialog.findElement(By.cssSelector("[label=\"Cancel\"]")), 5)
						&&isElementLoaded(editDialog.findElement(By.cssSelector("[ng-if=\"$ctrl.submitLabel\"]")), 5)){
					if (saveOrNot){
						clickTheElement(editDialog.findElement(By.cssSelector("[ng-if=\"$ctrl.submitLabel\"] button")));
						String mealBreakSettingContent = getMealBreakContent();
						SimpleUtils.assertOnFail("Setting is not saved successfully!",
								mealBreakSettingContent.contains(mealBreakDuration)
										&& mealBreakSettingContent.contains(paidType)
										&& mealBreakSettingContent.contains(scheduleHoursLimit), false);
					} else {
						clickTheElement(editDialog.findElement(By.cssSelector("[label=\"Cancel\"] button")));
						SimpleUtils.assertOnFail("Setting should the same as before!", contentBefore.equalsIgnoreCase(getMealBreakContent()), false);
					}
				} else {
					SimpleUtils.fail("Save or Cancel button fail to load!", false);
				}
			} else {
				SimpleUtils.fail("Meal Break dialog fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Edit Meal Break button is not loaded!", false);
		}
	}

	@FindBy(css = "[question-title=\"Lock employee availability edits…\"] h3")
	private WebElement lockEmployeeAvailabilityMessage;

	@FindBy(css = "[question-title=\"Lock employee availability edits…\"] select")
	private WebElement lockEmployeeAvailabilitySelector;

	@Override
	public void verifyLockEmployeeAvailabilityEditsIsLoaded () throws Exception {
		String message = "Lock employee availability edits…";
		String option1 = "Once schedule is created";
		String option2 = "During schedule planning window";
		if (isElementLoaded(lockEmployeeAvailabilityMessage, 10)
				&& isElementLoaded(lockEmployeeAvailabilitySelector, 10)) {
			Select selector = new Select(lockEmployeeAvailabilitySelector);
			if (lockEmployeeAvailabilityMessage.getText().equalsIgnoreCase(message)
					&& selector.getOptions().get(0).getText().equalsIgnoreCase(option1)
					&& selector.getOptions().get(1).getText().equalsIgnoreCase(option2)) {
				SimpleUtils.pass("The text for 'Lock employee availability edits…' display correctly! ");
			} else
				SimpleUtils.fail("The text for 'Lock employee availability edits…' display incorrectly! ", false);
		} else
			SimpleUtils.fail("Lock employee availability edits section fail to loaded! ", false);
	}

	@Override
	public void updateLockEmployeeAvailabilityEdits(String lockOption) throws Exception {
		if (isElementLoaded(lockEmployeeAvailabilitySelector, 10)) {
			Select selector = new Select(lockEmployeeAvailabilitySelector);
			selector.selectByVisibleText(lockOption);
			displaySuccessMessage();
			SimpleUtils.pass("Select option successfully! ");
		} else
			SimpleUtils.fail("Lock employee availability edits section fail to loaded! ", false);
	}


	@Override
	public void verifyTheSectionsOnWorkingHoursPage() throws Exception {
		if (areListElementVisible(workingHoursTypes, 5) && workingHoursTypes.size() >= 3) {
			if (workingHoursTypes.size()== 4){
				SimpleUtils.assertOnFail("The sections display incorrectly on working hours page! ",
						workingHoursTypes.get(0).getText().equalsIgnoreCase("Dayparts")
								&& workingHoursTypes.get(1).getText().equalsIgnoreCase("Regular")
								&& workingHoursTypes.get(2).getText().equalsIgnoreCase("Holiday")
								&& workingHoursTypes.get(3).getText().equalsIgnoreCase("Company Holidays"), false);

			} else {
				SimpleUtils.assertOnFail("The sections display incorrectly on working hours page! ",
						workingHoursTypes.get(0).getText().equalsIgnoreCase("Regular")
								&& workingHoursTypes.get(1).getText().equalsIgnoreCase("Holiday")
								&& workingHoursTypes.get(2).getText().equalsIgnoreCase("Company Holidays"), false);

			}
		} else {
			SimpleUtils.fail("Working Hours Types not loaded Successfully!", true);
		}
	}

	@Override
	public boolean checkIfWorkHoursTypeCollapsed(String title) throws Exception {
		boolean workingHoursTypeCollapsed = false;
		if (areListElementVisible(workingHoursTypes, 5)) {
			for (WebElement workingHoursType : workingHoursTypes) {
				if (workingHoursType.getText().equalsIgnoreCase(title)) {
					if (workingHoursType.getAttribute("class").contains("collapsible-title-open")) {
						workingHoursTypeCollapsed = true;
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("Working Hours Types not loaded Successfully!", true);
		}
		return workingHoursTypeCollapsed;
	}


	@Override
	public void turnOnOrOffSpecificHolidayHours(String holidayName, String action) throws Exception {
		if (areListElementVisible(holidayHoursRows,10)){
			for (WebElement day: holidayHoursRows) {
				if (day.findElement(By.cssSelector(".ellipsis")).getText().equalsIgnoreCase(holidayName)) {
					if (day.findElement(By.cssSelector("[type=\"checkbox\"]")).getAttribute("class").contains("ng-not-empty")) {
						if (action.equalsIgnoreCase("on")) {
							SimpleUtils.pass(holidayName + " already on!");
						} else {
							clickTheElement(day.findElement(By.cssSelector("label[class=\"switch\"]")));
							if (day.findElement(By.cssSelector("[type=\"checkbox\"]")).getAttribute("class").contains("ng-empty")) {
								SimpleUtils.pass(holidayName + " Unchecked!");
							} else
								SimpleUtils.fail("Fail to turn off the Holiday Working Hours", false);

						}
					} else {
						if (action.equalsIgnoreCase("off")) {
							SimpleUtils.pass(holidayName + " already off!");
						} else {
							clickTheElement(day.findElement(By.cssSelector("label[class=\"switch\"]")));
							if (day.findElement(By.cssSelector("[type=\"checkbox\"]")).getAttribute("class").contains("ng-not-empty")) {
								SimpleUtils.pass(holidayName + " Checked!");
							} else
								SimpleUtils.fail("Fail to turn on the Holiday Working Hours", false);
						}
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("No access item loaded!", false);
		}
	}


	@Override
	public void turnOnOrOffSpecificRegularWorkingHours(String regularDay, String action) throws Exception {
		if (areListElementVisible(regularHoursRows,10) && regularHoursRows.size() == 7){
			for (WebElement day: regularHoursRows) {
				if (day.findElement(By.cssSelector(".ellipsis")).getText().equalsIgnoreCase(regularDay)) {
					if (day.findElement(By.cssSelector("[type=\"checkbox\"]")).getAttribute("class").contains("ng-not-empty")) {
						if (action.equalsIgnoreCase("on")) {
							SimpleUtils.pass(regularDay + " already on!");
						} else {
							clickTheElement(day.findElement(By.cssSelector("label[class=\"switch\"]")));
							if (day.findElement(By.cssSelector("[type=\"checkbox\"]")).getAttribute("class").contains("ng-empty")) {
								SimpleUtils.pass(regularDay + " Unchecked!");
							} else
								SimpleUtils.fail("Fail to turn off the Regular Working Hours", false);

						}
					} else {
						if (action.equalsIgnoreCase("off")) {
							SimpleUtils.pass(regularDay + " already off!");
						} else {
							clickTheElement(day.findElement(By.cssSelector("label[class=\"switch\"]")));
							if (day.findElement(By.cssSelector("[type=\"checkbox\"]")).getAttribute("class").contains("ng-not-empty")) {
								SimpleUtils.pass(regularDay + " Checked!");
							} else
								SimpleUtils.fail("Fail to turn on the Regular Working Hours", false);
						}
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("No access item loaded!", false);
		}
	}


	@Override
	public LinkedHashMap<String, List<String>> getHolidayWorkingHours() throws Exception {
		LinkedHashMap<String, List<String>> holidayHours = new LinkedHashMap<>();
		List<String> startNEndTime = null;
		if (areListElementVisible(holidayHoursRows, 30)) {
			for (int i = 0; i < holidayHoursRows.size(); i++) {
				WebElement day = holidayHoursRows.get(i).findElement(By.className("ellipsis"));
				List<WebElement> workTimes = holidayHoursRows.get(i).findElements(By.className("work-time"));
				if (day != null) {
					startNEndTime = new ArrayList<>();
					String startTime = "";
					String endTime = "";
					if (workTimes != null && workTimes.size() == 2) {
						startTime = workTimes.get(0).getText();
						endTime = workTimes.get(1).getText();
						startNEndTime.add(startTime);
						startNEndTime.add(endTime);
					}
					holidayHours.put(holidayHoursRows.get(i).findElement(By.className("ellipsis")).getText(), startNEndTime);
					SimpleUtils.report("Get time for: " + holidayHoursRows.get(i).findElement(By.className("ellipsis")).getText() +
							", time is: " + startTime + " - " + endTime);
				}
			}
		}
		return holidayHours;
	}


	@Override
	public void updateControlsHolidayHours(String openingHours, String closingHours, String day, List<String> applyToOtherDays) throws Exception {
		openingHours = openingHours.replace(" ", "");
		closingHours = closingHours.replace(" ", "");
		WebElement collapsibleHeader = holidayHoursBlock.findElement(By.cssSelector("div.collapsible.row"));
		boolean isHolidayHoursSectionOpened = collapsibleHeader.getAttribute("class").contains("open");

		if (!isHolidayHoursSectionOpened)
			click(holidayHoursBlock);

		if (areListElementVisible(holidayHoursRows)) {
			ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
			for (WebElement holidayHoursRow : holidayHoursRows) {
				if (holidayHoursRow.getText().toLowerCase().contains(day.toLowerCase())) {
					if (holidayHoursRow.findElement(By.cssSelector("label[class=\"switch\"]")).getAttribute("class").contains("ng-empty")) {
						clickTheElement(holidayHoursRow.findElement(By.cssSelector("label[class=\"switch\"]")));
					}
					WebElement holidayHoursEditBtn = holidayHoursRow.findElement(By.cssSelector("lg-button[label=\"Edit\"]"));
					if (isElementLoaded(holidayHoursEditBtn)) {
						click(holidayHoursEditBtn);
						// Select Opening and closing Hours
						shiftOperatePage.moveSliderAtCertainPointOnEditShiftTimePage(closingHours, "End");
						shiftOperatePage.moveSliderAtCertainPointOnEditShiftTimePage(openingHours, "Start");
						//Apply to other days
						if (applyToOtherDays.size() != 0) {
							if (areListElementVisible(applyOtherDays, 5)) {
								for (WebElement otherDay: applyOtherDays) {
									if (applyToOtherDays.contains(otherDay.getText())) {
										clickTheElement(otherDay.findElement(By.tagName("input")));
									}
								}
							} else
								SimpleUtils.fail("Apply to other days fail to load! ", false);
						}

						//Save the change
						if (isElementLoaded(saveWorkersHoursBtn)) {
							click(saveWorkersHoursBtn);
							SimpleUtils.pass("Controls Working Hours Section: Holiday Hours Updated for the day ('" + day + "'). ");
						} else
							SimpleUtils.fail("Controls Working Hours Section: Editing Holiday Hours 'Save' Button not loaded.", true);
					} else {
						SimpleUtils.fail("Controls Working Hours Section: Holiday Hours 'Edit' Button not loaded.", true);
					}
					break;
				}

			}
		} else {
			SimpleUtils.fail("Controls Working Hours Section: Holiday Hours not loaded.", true);
		}
	}


	@FindBy(css = "[label=\"Save\"] button")
	private WebElement saveBtn;
	public void clickOnSaveBtn() throws Exception {
		if (isElementLoaded(saveBtn)) {
			clickTheElement(saveBtn);
			displaySuccessMessage();
		} else
			SimpleUtils.report("Save Button not loaded.");
	}


	@FindBy(css = "lg-button[label=\"Manage\"]")
	private WebElement manageBtn;
	public void clickOnManageBtn() throws Exception {
		if (isElementLoaded(manageBtn)) {
			clickTheElement(manageBtn);
			SimpleUtils.pass("Manage button clicked successfully.");
		} else
			SimpleUtils.report("Manage Button not loaded.");
	}


	@FindBy(css = "modal[modal-title=\"Manage Company Holidays\"]")
	private WebElement manageCompanyHolidaysModal;
	@FindBy(css = "input[placeholder=\"You can search by holiday name\"]")
	private WebElement holidaySearchBox;
	@FindBy(css = "div.lg-company-holidays__item")
	private List<WebElement> companyHolidays;
	@FindBy(css = ".company-holidays-list__table-wrapper tr")
	private List<WebElement> companyHolidaysInSearchResultLists;

	public List<String> getAllSelectedCompanyHolidays () {
		if (areListElementVisible(companyHolidays, 10)) {
			List<String> selectedCompanyHolidays = new ArrayList<>();
			for (WebElement holiday: companyHolidays) {
				selectedCompanyHolidays.add(holiday.getText());
			}
			SimpleUtils.pass("Get all company holidays successfully! ");
			return selectedCompanyHolidays;
		} else
			return null;
	}

	public void searchSpecificCompanyHolidays (String companyHolidays) throws Exception {
		if (isElementLoaded(holidaySearchBox, 10)) {
			holidaySearchBox.clear();
			holidaySearchBox.sendKeys(companyHolidays);
			waitForSeconds(2);
			SimpleUtils.pass("Search company holidays successfully! ");
		} else
			SimpleUtils.fail("The company holiday search box fail to loaded! ", false);
	}


	public void checkOrUncheckSpecificCompanyHolidays (Boolean isCheck, String companyHoliday) throws Exception {
		if (!isElementLoaded(manageCompanyHolidaysModal)) {
			clickOnManageBtn();
		}
		searchSpecificCompanyHolidays(companyHoliday);
		if (areListElementVisible(companyHolidaysInSearchResultLists, 10) && companyHolidaysInSearchResultLists.size()>0) {
			boolean isCompanyHolidayExist = false;
			for (WebElement holiday : companyHolidaysInSearchResultLists) {
				if (holiday.findElement(By.cssSelector(".company-holidays-list__table-wrapper--name")).getText().equalsIgnoreCase(companyHoliday)){
					isCompanyHolidayExist = true;
					if (holiday.getAttribute("class").contains("selected")){
						if (isCheck){
							SimpleUtils.pass("This company holiday already checked! ");
						} else {
							clickTheElement(holiday.findElement(By.cssSelector(".company-holidays-list__table-wrapper--enabled input")));
							if (!holiday.getAttribute("class").contains("selected")) {
								SimpleUtils.pass("Uncheck this company holiday successfully! ");
							} else
								SimpleUtils.fail("Fail to uncheck this company holiday! ", false);
						}
					} else {
						if (isCheck){
							clickTheElement(holiday.findElement(By.cssSelector(".company-holidays-list__table-wrapper--enabled input")));
							if (holiday.getAttribute("class").contains("selected")) {
								SimpleUtils.pass("Check this company holiday successfully! ");
							} else
								SimpleUtils.fail("Fail to check this company holiday! ", false);
						} else {
							SimpleUtils.pass("This company holiday already unchecked! ");
						}
					}
					break;
				}
			}
			if (!isCompanyHolidayExist) {
				SimpleUtils.fail("The specific company holidays is not exist! ", false);
			}
			clickOnSaveBtn();
		} else
			SimpleUtils.fail("The specific company holidays is not exist! ", false);

	}

	public void setFixedHoursForSpecificCompanyHolidays (String companyHoliday, String fixedHours) throws Exception {
		if (!isElementLoaded(manageCompanyHolidaysModal)) {
			clickOnManageBtn();
		}
		searchSpecificCompanyHolidays(companyHoliday);
		if (areListElementVisible(companyHolidaysInSearchResultLists, 10) && companyHolidaysInSearchResultLists.size()>0) {
			boolean isCompanyHolidayExist = false;
			for (WebElement holiday : companyHolidaysInSearchResultLists) {
				if (holiday.findElement(By.cssSelector(".company-holidays-list__table-wrapper--name")).getText().equalsIgnoreCase(companyHoliday)){
					isCompanyHolidayExist = true;
					holiday.findElement(By.cssSelector(".company-holidays-list__table-wrapper--fixed-hours input")).clear();
					holiday.findElement(By.cssSelector(".company-holidays-list__table-wrapper--fixed-hours input")).sendKeys(fixedHours);
					break;
				}
			}
			if (!isCompanyHolidayExist) {
				SimpleUtils.fail("The specific company holidays is not exist! ", false);
			}
			clickOnSaveBtn();
		} else
			SimpleUtils.fail("The specific company holidays is not exist! ", false);
	}


	@Override
	public LinkedHashMap<String, List<String>> getCompanyHolidaysInSearchResult() throws Exception {
		LinkedHashMap<String, List<String>> companyHolidays = new LinkedHashMap<>();
		List<String> holidayInfo = null;
		if (areListElementVisible(companyHolidaysInSearchResultLists, 30)) {
			for (int i = 0; i < companyHolidaysInSearchResultLists.size(); i++) {
				holidayInfo = new ArrayList<>();
				boolean checkOrNot = false;
				WebElement holidayName = companyHolidaysInSearchResultLists.get(i).findElement(By.className("company-holidays-list__table-wrapper--name"));
				String numberOfFixedHours = companyHolidaysInSearchResultLists.get(i).
						findElement(By.cssSelector(".company-holidays-list__table-wrapper--fixed-hours input")).getAttribute("value");
				//Set the value of holiday checked or not
				if (companyHolidaysInSearchResultLists.get(i).getAttribute("class").contains("selected")) {
					checkOrNot = true;
				}
				holidayInfo.add(checkOrNot? "checked":"unchecked");

				//Set the holiday name
				holidayInfo.add(holidayName.getText());

				//Set the number of fixed hours
				holidayInfo.add(numberOfFixedHours);

				companyHolidays.put(holidayName.getText(), holidayInfo);
			}
		}
		return companyHolidays;
	}

	@Override
	public void turnOnOrTurnOffDailyOTToggle(boolean action) throws Exception {
		String content = getDailyOTContent();
		if (isElementLoaded(DailyOvertimePaySection, 10)
				&&content.contains("An employee will receive Overtime Pay for hours worked in excess of ")
				&&content.contains(" in a ")){
			if (isElementLoaded(DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
				if (action && DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
					scrollToElement(DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input__toggle")));
					clickTheElement(DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					displaySuccessMessage();
					SimpleUtils.pass("Toggle is turned on!");
				} else if (!action && !DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
					clickTheElement(DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
					displaySuccessMessage();
					SimpleUtils.pass("Toggle is turned off!");
				} else {
					SimpleUtils.pass("Toggle status is expected!");
				}
			} else {
				SimpleUtils.fail("Toggle fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Daily OT section fail to load!", false);
		}
	}

	@FindBy(css = "[form-title=\"Overtime Pay\"] [question-title*=\"</b> in a <b>\"]")
	private WebElement DailyOvertimePaySection;
	@FindBy(css = "[form-title=\"Overtime Pay\"] [ng-click=\"openDailyOvertimeConfigDialog()\"]")
	private WebElement dailyOvertimePayEditBtn;
	public String getDailyOTContent() throws Exception{
		if (isElementLoaded(DailyOvertimePaySection, 10)){
			return DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input__text")).getText();
		}
		return "";
	}

	/*
	 * numOfHrs: "8 hours"; "10 hours";"12 hours".
	 * singleDayOr24Hrs: "single work day"; "24 hour period".
	 */
	@Override
	public void editDailyOT(String numOfHrs, String singleDayOr24Hrs, boolean saveOrNot) throws Exception {
		if (isElementLoaded(dailyOvertimePayEditBtn, 5)){
			String contentBefore = getDailyOTContent();
			clickTheElement(dailyOvertimePayEditBtn);
			if (isElementLoaded(splitShiftDialog, 15)){
				//check the title.
				if (splitShiftDialog.findElement(By.cssSelector(".lg-modal__title")).getText().trim().equalsIgnoreCase("Edit Daily Overtime")){
					SimpleUtils.pass("Dialog title is expected!");
				} else {
					SimpleUtils.fail("Dialog title is not correct", false);
				}
				//Check setting content.
				if(splitShiftDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("An employee will receive Overtime Pay for hours worked in excess of")
						&&splitShiftDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("in a")){
					SimpleUtils.pass("Setting content in the dialog is expected!");
				} else {
					SimpleUtils.fail("Setting content is not expected!", false);
				}
				//edit the content, input the parameters.
				if (splitShiftDialog.findElements(By.cssSelector("select")).size() == 2){
					selectByVisibleText(splitShiftDialog.findElements(By.cssSelector("select")).get(0), numOfHrs);
					selectByVisibleText(splitShiftDialog.findElements(By.cssSelector("select")).get(1), singleDayOr24Hrs);
				} else {
					SimpleUtils.fail("Selects are not shown as expected!", false);
				}
				//save or cancel.
				if (isElementLoaded(splitShiftDialog.findElement(By.cssSelector("[label=\"Cancel\"]")), 5)
						&&isElementLoaded(splitShiftDialog.findElement(By.cssSelector("[label=\"Save\"]")), 5)){
					if (saveOrNot){
						clickTheElement(splitShiftDialog.findElement(By.cssSelector("[label=\"Save\"] button")));
						waitForSeconds(2);
						SimpleUtils.assertOnFail("Setting is not saved successfully!", getDailyOTContent().contains(numOfHrs)&&getDailyOTContent().contains(singleDayOr24Hrs), false);
					} else {
						clickTheElement(splitShiftDialog.findElement(By.cssSelector("[label=\"Cancel\"] button")));
						waitForSeconds(2);
						SimpleUtils.assertOnFail("Setting should the same as before!", contentBefore.equalsIgnoreCase(getDailyOTContent()), false);
					}
				} else {
					SimpleUtils.fail("Save or Cancel button fail to load!", false);
				}
			} else {
				SimpleUtils.fail("Split shift dialog fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Edit split shift button is not loaded!", false);
		}
	}

	@Override
	public boolean checkDailyOTEnabledOrNot() throws Exception {
		boolean isEnabled = false;
		if (isElementLoaded(DailyOvertimePaySection, 10)) {
			if (isElementLoaded(DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input__toggle")), 10)) {
				if (!DailyOvertimePaySection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")) {
					isEnabled = true;
					SimpleUtils.pass("Toggle is turned on!");
				} else {
					SimpleUtils.pass("Toggle is turned off!");
				}
			} else {
				SimpleUtils.fail("Toggle fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Daily OT section fail to load!", false);
		}
		return isEnabled;
	}

	@FindBy(css = "[form-title=\"Configuration\"]")
	private WebElement configurationSection;
	public boolean checkIfTheLocationUsingControlsConfiguration() throws Exception {
		boolean isUsingControlsConfiguration = false;
		clickOnControlsConsoleMenu();
		clickOnControlsLocationProfileSection();
		if (isElementLoaded(configurationSection, 20)) {
			if (configurationSection.findElement(By.tagName("span")).
					getText().equalsIgnoreCase("Using Controls Configuration")) {
				isUsingControlsConfiguration = true;
				SimpleUtils.report("This location is using Controls Configuration! ");
			} else
				SimpleUtils.report("This location is using Control Center Configuration");
		} else
			SimpleUtils.fail("The configuration section fail to load! ", false);
		return isUsingControlsConfiguration;
	}



	@FindBy(css = "[question-title=\"Allow employees claim open shift at overtime rate?\"]")
	private WebElement allowEmployeeClaimOTOpenShift;
	public boolean checkIfEmployeeCanClaimOTOpenShift() throws Exception {
		boolean isAllowEmployeeClaimOTOpenShift = false;
		if (isElementLoaded(allowEmployeeClaimOTOpenShift, 30)) {
			if (allowEmployeeClaimOTOpenShift.findElement(By.cssSelector(".lg-button-group-first"))
					.getAttribute("class").contains("selected")) {
				isAllowEmployeeClaimOTOpenShift = true;
				SimpleUtils.report("Allow employees claim open shift at overtime rate! ");
			} else
				SimpleUtils.report("Not allow employees claim open shift at overtime rate! ");
		} else
			SimpleUtils.fail("'Allow employees claim open shift at overtime rate' setting fail to load! ", false);
		return isAllowEmployeeClaimOTOpenShift;
	}

	@FindBy(css = "form-section[form-title=\"Budget\"] [question-title*=\"Input budget by location or break down by work role or job title?\"]")
	private WebElement budgetGroupSelectionNonOP;
	@FindBy(css = "form-section[form-title=\"Budget\"] [question-title*=\"Input budget by location or break down by work role or job title?\"] [ng-required*=\"$ctrl.required\"]")
	private WebElement budgetGroupNonOP;
	@Override
	public String getBudgetGroupSettingContentNonOP() throws Exception{
		if (isElementLoaded(budgetGroupSelectionNonOP, 10)){
			return budgetGroupSelectionNonOP.findElement(By.cssSelector(".lg-question-input__text.ng-binding")).getText();
		}
		return "";
	}
	@Override
	public void selectBudgetGroupNonOP(String optionValue) throws Exception {
		String content = getBudgetGroupSettingContentNonOP();
		if (isElementLoaded(budgetGroupSelectionNonOP, 10)
				&& (content.contains("Input budget by location or break down by work role or job title?"))){
			Select selectedBudgetGroupNonOP = new Select(budgetGroupNonOP);
			selectedBudgetGroupNonOP.selectByVisibleText(optionValue);
			SimpleUtils.report("Select '" + optionValue + "' as the budget group");
			waitForSeconds(2);
		} else {
			SimpleUtils.fail("Budget group section fail to load!", false);
		}
	}

	//Add by Cosimo
	@FindBy(css = "[question-title = \"Can manager override the assignment rule?\"]")
	private WebElement btnOverrideAssignmentRuleForOP;

	@FindBy(css = "[question-title = \"Can manager override the assignment rule?\"] div.lg-button-group-first")
	private WebElement btnOverrideAssignmentRuleYesForOP;

	@FindBy(css = "[question-title = \"Can manager override the assignment rule?\"] div.lg-button-group-last")
	private WebElement btnOverrideAssignmentRuleNoForOP;

	public void enableOverRideAssignmentRuleAsYesForOP() throws Exception {
		if (isElementEnabled(btnOverrideAssignmentRuleForOP, 20)) {
			if (isElementEnabled(btnOverrideAssignmentRuleYesForOP, 3)) {
				if (btnOverrideAssignmentRuleYesForOP.getAttribute("class").contains("selected")) {
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button already enabled");
				} else {
					clickTheElement(btnOverrideAssignmentRuleYesForOP);
					if (isElementLoaded(overridePopup, 10) && isElementLoaded(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")),10)){
						clickTheElement(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")));
					}
					Actions actions = new Actions(getDriver());
					actions.moveByOffset(0, 0).click().build().perform();
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button selected!");
					displaySuccessMessage();
				}
			} else {
				SimpleUtils.fail("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button not loaded!!", false);
			}
		}
	}

	@Override
	public void enableOverRideAssignmentRuleAsNoForOP() throws Exception {
		if (isElementEnabled(btnOverrideAssignmentRuleForOP, 20)) {
			if (isElementEnabled(btnOverrideAssignmentRuleNoForOP, 10)) {
				waitForSeconds(5);
				if (btnOverrideAssignmentRuleNoForOP.getAttribute("class").contains("selected")) {
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'No' button already enabled");
				} else {
					scrollToElement(btnOverrideAssignmentRuleNoForOP);
					clickTheElement(btnOverrideAssignmentRuleNoForOP);
					if (isElementLoaded(overridePopup, 10) && isElementLoaded(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")),10)){
						clickTheElement(overridePopup.findElement(By.cssSelector("[ng-click*=\"Overwrite()\"]")));
					}
					Actions actions = new Actions(getDriver());
					actions.moveByOffset(0, 300).click().build().perform();
					SimpleUtils.pass("Controls Page: Schedule Policies Override Assignment rule section 'No' button selected!");
					displaySuccessMessage();
				}
			} else {
				SimpleUtils.fail("Controls Page: Schedule Policies Override Assignment rule section 'Yes' button not loaded!!", false);
			}
		}else{
			SimpleUtils.fail("The override toggle is not loaded correctly!", false);
		}
	}

	@FindBy(css = "question-input[question-title*=\"What type of seniority taken into account for search and recommendation?\"]")
	private WebElement senioritySection;
	@FindBy(css = "[question-title*=\"search and recommendation?\"] yes-no")
	private WebElement seniorityToggle;
	@Override
	public void isSenioritySectionLoaded() throws Exception {
		if (isElementLoaded(senioritySection,20))
			SimpleUtils.report("The seniority toggle is loaded!");
		else
			SimpleUtils.fail("The seniority toggle is not loaded!", false);
	}

	@Override
	public String getSeniorityToggleActiveBtnLabel() throws Exception {
		String seniorityToggleActiveBtnLabel = "";
		if (isElementLoaded(seniorityToggle,20)) {
			WebElement seniorityToggleActiveActiveBtn = seniorityToggle.findElement(
					By.cssSelector("div.lg-button-group-selected"));
			if (isElementLoaded(seniorityToggleActiveActiveBtn,10))
				seniorityToggleActiveBtnLabel = seniorityToggleActiveActiveBtn.getText();
			else
				SimpleUtils.fail("Scheduling Policies: Seniority toggle active Button not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Seniority toggle section not loaded.", false);

		return seniorityToggleActiveBtnLabel;
	}

	@Override
	public void updateSeniorityToggle(String isSeniorityToggleOpen) throws Exception {
		if (isElementLoaded(seniorityToggle,25)) {
			WebElement applyLaborBudgetBtnGroup = seniorityToggle.findElement(
					By.cssSelector("div.lg-button-group"));
			if (applyLaborBudgetBtnGroup.getAttribute("class").contains("disabled")) {
				SimpleUtils.fail("Seniority toggle is disabled.", false);
			}
			else{
				List<WebElement> seniorityToggleBtns = seniorityToggle.findElements(
						By.cssSelector("div[ng-click=\"!button.disabled && $ctrl.change(button.value)\"]"));
				waitForSeconds(5);
				if (seniorityToggleBtns.size() > 0) {
					for (WebElement seniorityToggleBtn : seniorityToggleBtns) {
						if (seniorityToggleBtn.getText().toLowerCase().contains(isSeniorityToggleOpen.toLowerCase())) {
							scrollToElement(seniorityToggleBtn);
							clickTheElement(seniorityToggleBtn);
						}
					}
					if (getSeniorityToggleActiveBtnLabel().toLowerCase().contains(isSeniorityToggleOpen.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Seniority toggle is updated with value: '"
								+ isSeniorityToggleOpen + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Seniority toggle is not updated with value: '"
								+ isSeniorityToggleOpen + "'.", false);
				} else
					SimpleUtils.fail("Scheduling Policies: Seniority toggle buttons are not loaded!", false);
			}

		} else
			SimpleUtils.fail("Scheduling Policies: Seniority toggle section is not loaded!", false);
	}

	@FindBy(css = "question-input[question-title*=\"What type of seniority taken into account for search and recommendation?\"] select")
	private WebElement seniorityTypeList;
	@Override
	public void selectSeniorityType(String optionValue) throws Exception {
		if (isElementLoaded(senioritySection, 3)){
			if(isElementLoaded(seniorityTypeList, 3)){
				Select selectedSenioritySort = new Select(seniorityTypeList);
				selectedSenioritySort.selectByVisibleText(optionValue);
				SimpleUtils.report("Select '" + optionValue + "' as the seniority's order");
				waitForSeconds(2);
			}else{
				SimpleUtils.fail("Seniority sort list fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Seniority sort section fail to load!", false);
		}
	}

	@FindBy(css = "question-input[question-title*=\"Sort seniority by ascending or descending?\"]")
	private WebElement senioritySortSection;
	@FindBy(css = "question-input[question-title*=\"Sort seniority by ascending or descending?\"] select")
	private WebElement senioritySortList;
	@FindBy(css = "question-input[question-title*=\"Sort seniority by ascending or descending?\"] [class=\"select-wrapper ng-scope\"]")
	private WebElement senioritySort;
	@Override
	public void isSortOfSenioritySectionLoaded() throws Exception {
		if (isElementLoaded(senioritySortSection))
			SimpleUtils.report("The sort of seniority section is loaded!");
		else
			SimpleUtils.fail("The sort of seniority section is not loaded!", false);
	}
	@Override
	public void selectSortOfSeniority(String seniorityType, String optionValue) throws Exception {
		selectSeniorityType(seniorityType);
		if (isElementLoaded(senioritySortSection, 20)){
			if(isElementLoaded(senioritySortList, 3)){
				Select selectedSenioritySort = new Select(senioritySortList);
				selectedSenioritySort.selectByVisibleText(optionValue);
				SimpleUtils.report("Select '" + optionValue + "' as the seniority's order");
				waitForSeconds(2);
			}else{
				SimpleUtils.fail("Seniority sort list fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Seniority sort section fail to load!", false);
		}
	}

	@Override
	public String getSenioritySort() throws Exception {
		String senioritySortContent = "";
		if (isElementLoaded(senioritySort))
			senioritySortContent = senioritySort.getAttribute("data-content").trim();
		else
			SimpleUtils.fail("Scheduling Policies: Seniority sort box is not loaded!", false);
		return senioritySortContent;
	}

	@FindBy(css = "question-input[question-title*=\"Can manager override the accrual limit?\"]")
	private WebElement accrualTimeOverrideSection;
	@FindBy(css = "[question-title*=\"Can manager override the accrual limit?\"] yes-no")
	private WebElement accrualTimeOverrideToggle;
	@Override
	public void isAccrualTimeOverrideLoaded() throws Exception {
		if (isElementLoaded(accrualTimeOverrideSection,10))
			SimpleUtils.report("The accrual time override section is loaded!");
		else
			SimpleUtils.fail("The accrual time override section is not loaded!", false);
	}

	@Override
	public String getAccrualTimeOverrideToggleActiveBtnLabel() throws Exception {
		String accrualTimeOverrideActiveBtnLabel = "";
		if (isElementLoaded(accrualTimeOverrideToggle,10)) {
			WebElement accrualTimeOverrideToggleActiveBtn = accrualTimeOverrideToggle.findElement(
					By.cssSelector("div.lg-button-group-selected"));
			if (isElementLoaded(accrualTimeOverrideToggleActiveBtn,10))
				accrualTimeOverrideActiveBtnLabel = accrualTimeOverrideToggleActiveBtn.getText();
			else
				SimpleUtils.fail("Scheduling Policies: Accrual time override active button not loaded.", false);
		} else
			SimpleUtils.fail("Scheduling Policies: Accrual time override toggle section not loaded.", false);

		return accrualTimeOverrideActiveBtnLabel;
	}

	@Override
	public void updateAccrualTimeOverrideToggle(String isToggleOpen) throws Exception {
		if (isElementLoaded(accrualTimeOverrideToggle,20)) {
			WebElement applyLaborBudgetBtnGroup = accrualTimeOverrideToggle.findElement(
					By.cssSelector("div.lg-button-group"));
			if (applyLaborBudgetBtnGroup.getAttribute("class").contains("disabled")) {
				SimpleUtils.fail("accrual time override toggle is disabled.", false);
			}
			else{
				List<WebElement> accrualTimeOverrideBtns = accrualTimeOverrideToggle.findElements(
						By.cssSelector("div[ng-click=\"!button.disabled && $ctrl.change(button.value)\"]"));
				waitForSeconds(5);
				if (accrualTimeOverrideBtns.size() > 0) {
					for (WebElement seniorityToggleBtn : accrualTimeOverrideBtns) {
						if (seniorityToggleBtn.getText().toLowerCase().contains(isToggleOpen.toLowerCase())) {
							scrollToElement(seniorityToggleBtn);
							clickTheElement(seniorityToggleBtn);
						}
					}
					if (getAccrualTimeOverrideToggleActiveBtnLabel().toLowerCase().contains(isToggleOpen.toLowerCase()))
						SimpleUtils.pass("Scheduling Policies: Accrual time override toggle is updated with value: '"
								+ isToggleOpen + "'.");
					else
						SimpleUtils.fail("Scheduling Policies: Accrual time override is not updated with value: '"
								+ isToggleOpen + "'.", false);
				} else
					SimpleUtils.fail("Scheduling Policies: Accrual time override buttons are not loaded!", false);
			}

		} else
			SimpleUtils.fail("Scheduling Policies: Accrual time override section is not loaded!", false);
	}

	@FindBy(css = "[ng-if*=\"ScheduleRestriction\"]")
	private WebElement scheduleRestriction;
	@FindBy(css = "[class*=\"delete-action\"]")
	private WebElement deleteTimeOffBtn;
	@FindBy(css = "[ng-if*=\"ScheduleRestriction\"] button")
	private WebElement addTimeOffBtn;
	@Override
	public boolean isScheduleRestrictionLoaded() throws Exception {
		boolean isLoaded = true;
		if (isElementLoaded(scheduleRestriction,20))
			SimpleUtils.report("The Schedule Restriction is loaded!");
		else{
			isLoaded = false;
			SimpleUtils.report("The Schedule Restriction is not loaded!");
		}return isLoaded;
	}

	@Override
	public boolean isDeleteTimeOffBtnLoaded() throws Exception {
		boolean isLoaded = true;
		if (isElementLoaded(deleteTimeOffBtn,10))
			SimpleUtils.report("Delete button is loaded!");
		else{
			SimpleUtils.report("Delete button is not loaded!");
			isLoaded = false;
		}
		return isLoaded;
	}

	@Override
	public void clickDeleteTimeOffBtn() throws Exception {
		if (isElementLoaded(deleteTimeOffBtn,15)) {
			clickTheElement(deleteTimeOffBtn);
			if(!isElementLoaded(deleteTimeOffBtn,15)){
				SimpleUtils.report("Delete button is clicked!");
			}else
				SimpleUtils.fail("Delete button is not clicked correctly!", false);
		}else
			SimpleUtils.fail("Delete button is not loaded!", false);
	}

	@Override
	public boolean isAddTimeOffBtnLoaded() throws Exception {
		boolean isAddTimeOffBtnLoaded = true;
		if (isElementLoaded(addTimeOffBtn,10))
			SimpleUtils.report("Add button is loaded!");
		else {
			SimpleUtils.report("Add button is not loaded!");
			isAddTimeOffBtnLoaded = false;
		}return isAddTimeOffBtnLoaded;
	}

	@Override
	public boolean isAddTimeOffBtnClickable() throws Exception {
		boolean isAddTimeOffBtnClickable = true;
		if (isElementLoaded(addTimeOffBtn,15)) {
			if(addTimeOffBtn.getAttribute("disabled") == null){
				SimpleUtils.report("Time off add button is clickable!");
			}else if(addTimeOffBtn.getAttribute("disabled").contains("true")) {
				SimpleUtils.report("Time off add button is not clickable!");
				isAddTimeOffBtnClickable = false;
			}else
				SimpleUtils.fail("Time off add button status is not correct!", false);
		}else
			SimpleUtils.fail("Add button is not loaded!", false);
		return isAddTimeOffBtnClickable;
	}

	@Override
	public void clickAddTimeOffBtn() throws Exception {
		if (isElementLoaded(addTimeOffBtn,15)) {
			if(addTimeOffBtn.getAttribute("disabled") == null){
				clickTheElement(addTimeOffBtn);
				SimpleUtils.report("Time off add button is clickable!");
			}else
				SimpleUtils.fail("Time off add button is not clickable!!", false);
		}else
			SimpleUtils.fail("Add button is not loaded!", false);
	}

	@FindBy(css = "input-field[placeholder=\"Select...\"]")
	private WebElement timeOffReasonInputBox;
	@FindBy(css = "div [class*=\"lg-search-options__subLabel\"]")
	private List<WebElement> timeOffReasonList;
	@FindBy(css = "[ng-if*=\"ScheduleRestriction\"] input[placeholder=\"#\"]")
	private WebElement maximumLimit;
	@Override
	public List<String> getTimeOffReasonsOnSchedulingPolicy() throws Exception {
		List<String> timeOffReason = null;
		timeOffReason = new ArrayList<>();
		if(isElementLoaded(timeOffReasonInputBox, 10)){
			clickTheElement(timeOffReasonInputBox);
			for (WebElement timeOff : timeOffReasonList) {
				timeOffReason.add(timeOff.getText().trim());
			}
		}else{
			SimpleUtils.fail("Time off input box is not loaded!", false);
		}
		return timeOffReason;
	}

	@Override
	public void addTimeOffReasonOnSchedulingPolicy(String timeOffReason, String maximum) throws Exception {
		if(isElementLoaded(addTimeOffBtn, 10))
			clickTheElement(addTimeOffBtn);
		else
			SimpleUtils.fail("Add button is not loaded!", false);
		if(isElementLoaded(timeOffReasonInputBox, 10)){
			clickTheElement(timeOffReasonInputBox);
			for (WebElement timeOff : timeOffReasonList) {
				if (timeOff.getText().trim().equalsIgnoreCase(timeOffReason)) {
					scrollToElement(timeOff);
					clickTheElement(timeOff);
					SimpleUtils.report("Time off reason is selected!");
					break;
				}
			}
		}else{
			SimpleUtils.fail("Time off input box is not loaded!", false);
		}

		if(isElementLoaded(maximumLimit,10)){
			maximumLimit.clear();
			maximumLimit.sendKeys(maximum);
		}else
			SimpleUtils.fail("Maximum input field is not loaded!", false);
	}

	@Override
	public void modifyTimeOffReasonOnSchedulingPolicy(String timeOffReason, String maximum) throws Exception {
		if(isElementLoaded(timeOffReasonInputBox, 10)){
			clickTheElement(timeOffReasonInputBox);
			for (WebElement timeOff : timeOffReasonList) {
				if (timeOff.getText().trim().equalsIgnoreCase(timeOffReason)) {
					scrollToElement(timeOff);
					clickTheElement(timeOff);
					SimpleUtils.report("Time off reason is updated!");
				}
			}
		}else{
			SimpleUtils.fail("Time off input box is not loaded!", false);
		}

		if(isElementLoaded(maximumLimit,10)){
			maximumLimit.clear();
			maximumLimit.sendKeys(maximum);
			SimpleUtils.report("Maximum is updated!");
		}else
			SimpleUtils.fail("Maximum input field is not loaded!", false);
	}


	@Override
	public boolean getStatusOfSpecificPermissionForSpecificRoles(String section, String roles, String permission) throws Exception {
		boolean status = false;
		if (areListElementVisible(accessSections,10)){
			for (WebElement accessSection : accessSections){
				if (accessSection.findElement(By.tagName("span")).getText().equalsIgnoreCase(section)){
					if (!accessSection.findElement(By.cssSelector("div")).getAttribute("class").contains("expand")){
						clickTheElement(accessSection.findElement(By.tagName("span")));
					}
					int index = getTheIndexByAccessRolesName(roles, accessSection);
					List<WebElement> permissions = accessSection.findElements(By.cssSelector(".table-row"));
					for (WebElement permissionTemp : permissions){
						String s = permissionTemp.getText();
						if (s!=null && s.toLowerCase().contains(permission.toLowerCase())){
							SimpleUtils.pass("Found permission: "+ permission);
							List<WebElement> permissionInputs = permissionTemp.findElements(By.tagName("input"));
							if (permissionInputs.size()>index) {
								if (permissionInputs.get(index).getAttribute("class").contains("ng-not-empty")) {
									status = true;
								}
								break;
							}
						}
					}
					break;
				}
			}
		} else {
			SimpleUtils.fail("No access item loaded!", false);
		}
		return status;
	}


	@FindBy(css = "question-input[question-title*=\"Automatically group consecutive shifts of the same instance in a shift pattern?\"]")
	private WebElement autoGroupSection;
	@FindBy(css = "[question-title*=\"Automatically group consecutive shifts of the same instance in a shift pattern?\"] yes-no")
	private WebElement autoGroupToggle;
	@Override
	public void isAutoGroupSectionLoaded() throws Exception {
		if (isElementLoaded(autoGroupSection,20))
			SimpleUtils.report("Auto Group Section is loaded!");
		else
			SimpleUtils.fail("Auto Group Section is not loaded!", false);
	}

	@Override
	public String getAutoGroupToggleActiveBtnLabel() throws Exception {
		String autoGroupToggleActiveBtnLabel = "";
		if (isElementLoaded(autoGroupToggle, 20)) {
			WebElement autoGroupToggleActiveActiveBtn = autoGroupToggle.findElement(
					By.cssSelector("div.lg-button-group-selected"));
			if (isElementLoaded(autoGroupToggleActiveActiveBtn, 10))
				autoGroupToggleActiveBtnLabel = autoGroupToggleActiveActiveBtn.getText();
			else
				SimpleUtils.fail("Schedule Collaboration: Auto Group active Button is not loaded.", false);
		} else
			SimpleUtils.fail("Schedule Collaboration: Auto Group active Button is not loaded.", false);

		return autoGroupToggleActiveBtnLabel;
	}

	@Override
	public void updateAutoGroupToggle(String isAutoGroupToggleOpen) throws Exception {
		if (isElementLoaded(autoGroupToggle,25)) {
			WebElement autoGroupBtnGroup = autoGroupToggle.findElement(
					By.cssSelector("div.lg-button-group"));
			if (autoGroupBtnGroup.getAttribute("class").contains("disabled")) {
				SimpleUtils.fail("Seniority toggle is disabled.", false);
			}
			else{
				List<WebElement> autoGroupToggleBtns = autoGroupToggle.findElements(
						By.cssSelector("div[ng-click=\"!button.disabled && $ctrl.change(button.value)\"]"));
				waitForSeconds(5);
				if (autoGroupToggleBtns.size() > 0) {
					for (WebElement autoGroupToggleBtn : autoGroupToggleBtns) {
						if (autoGroupToggleBtn.getText().toLowerCase().contains(isAutoGroupToggleOpen.toLowerCase())) {
							scrollToElement(autoGroupToggleBtn);
							clickTheElement(autoGroupToggleBtn);
						}
					}
					if (getAutoGroupToggleActiveBtnLabel().toLowerCase().contains(isAutoGroupToggleOpen.toLowerCase()))
						SimpleUtils.pass("Scheduling Collaboration: Auto group toggle is updated with value: '"
								+ isAutoGroupToggleOpen + "'.");
					else
						SimpleUtils.fail("Scheduling Collaboration: Auto group toggle is not updated with value: '"
								+ isAutoGroupToggleOpen + "'.", false);
				} else
					SimpleUtils.fail("Scheduling Collaboration: Auto group toggle buttons are not loaded!", false);
			}

		} else
			SimpleUtils.fail("Scheduling Collaboration: Auto group toggle section is not loaded!", false);
	}

	@FindBy(css = "question-input[question-title*=\"What criteria are used when evaluating employees for open shift offers?\"] lg-multiple-select")
	private WebElement openShiftGroupRulesBox;
	@FindBy(css = "question-input[question-title*=\"What criteria are used when evaluating employees for open shift offers?\"] lg-multiple-select div input[placeholder*=\"Select\"]")
	private WebElement openShiftGroupRulesInputField;
	@FindBy(css = "div[class=\"select-list-item ng-scope\"] input-field")
	private List<WebElement> openShiftGroupRulesList;
	@FindBy(css = "div[class=\"select-list-item ng-scope\"] input-field ng-form input")
	private List<WebElement> openShiftGroupRuleCheckbox;
	@Override
	public boolean isOpenShiftGroupRuleSectionLoaded() throws Exception {
		boolean isLoaded;
		if (isElementLoaded(openShiftGroupRulesBox,10)) {
			SimpleUtils.report("Auto Approval section is loaded!");
			isLoaded = true;
		}
		else {
			SimpleUtils.report("Auto Approval section is not loaded!");
			isLoaded = false;
		}
		return isLoaded;
	}

	@Override
	public void verifyTextInOpenShiftGroupRuleInputBox(String text) throws Exception {
		if (isElementLoaded(autoGroupSection, 10)){
			if(isElementLoaded(openShiftGroupRulesInputField, 10)){
				if(openShiftGroupRulesInputField.getAttribute("value").trim().contains(text))
					SimpleUtils.pass("The text in open shift group rules input box is expected!");
				else {
					System.out.println("===========" + openShiftGroupRulesInputField.getAttribute("value").trim() + "==============");
					SimpleUtils.fail("The text in open shift group rules input box is not expected!", false);
				}
			}else
				SimpleUtils.fail("Open shift group rules box fail to load!", false);
		} else
			SimpleUtils.fail("Open shift group rules section fail to load!", false);
	}

	@Override
	public void selectOpenShiftGroupRule(ArrayList<String> optionValues) throws Exception {
		if (isElementLoaded(autoGroupSection, 10)){
			if(isElementLoaded(openShiftGroupRulesBox, 10)){
				click(openShiftGroupRulesBox);
				for(WebElement checkBox : openShiftGroupRuleCheckbox)
					if(checkBox.getAttribute("class").contains("ng-not-empty"))
						click(checkBox);
				ArrayList<String> options = new ArrayList<>();
				options = optionValues;
				int count = 0;
				for(int num = 0; num < optionValues.size(); num++){
					for(WebElement openShiftGroupRule : openShiftGroupRulesList){
						if(openShiftGroupRule.getText().trim().contains(options.get(num))){
							WebElement checkBox = openShiftGroupRule.findElement(By.cssSelector("ng-form[class*=\"input-form\"]"));
							click(checkBox);
							count = count + 1;
						}
					}
				}
				if(count != options.size())
					SimpleUtils.fail("Open shift group rules not match!", false);
				SimpleUtils.report("Select '" + options + "' as the open shift group rule");
				waitForSeconds(2);
			}else{
				SimpleUtils.fail("Open shift group rules list fail to load!", false);
			}
		} else {
			SimpleUtils.fail("Open shift group rules section fail to load!", false);
		}
	}

	@FindBy(css = "question-input[question-title*=\"How many hours before the shift starts is the auto assignment made?\"]")
	private WebElement autoApprovalSection;
	@FindBy(css = "question-input[question-title*=\"How many hours before the shift starts is the auto assignment made?\"] input")
	private WebElement autoApprovalAdvancedHours;
	@FindBy(css = "question-input[question-title*=\"How many hours before the shift starts is the auto assignment made?\"] ng-form")
	private WebElement advancedHours;
	@FindBy(css = "question-input[question-title*=\"How many hours before the shift starts is the auto assignment made?\"] input")
	private WebElement advancedHoursText;
	@Override
	public boolean isAutoApprovalSectionLoaded() throws Exception {
		boolean isLoaded;
		if (isElementLoaded(autoApprovalSection,10)) {
			SimpleUtils.report("Auto Approval section is loaded!");
			isLoaded = true;
		}else {
			SimpleUtils.report("Auto Approval section is loaded!");
			isLoaded = false;
		}
		return isLoaded;
	}

	public void setAutoApprovalAdvancedHours(String hours) throws Exception {
		isAutoApprovalSectionLoaded();
		if (isElementLoaded(autoApprovalAdvancedHours)) {
			autoApprovalAdvancedHours.clear();
			autoApprovalAdvancedHours.sendKeys(hours);
		}
		else
			SimpleUtils.fail("Input field of auto approval advance hours is not loaded!", false);
	}

	public String getAutoApprovalAdvancedHours() throws Exception {
		isAutoApprovalSectionLoaded();
		String hours = null;
		if (isElementLoaded(advancedHours,60))
			hours = advancedHoursText.getAttribute("value").trim();
		else
			SimpleUtils.fail("Input field of auto approval advance hours is not loaded!", false);
		return hours;
	}

}
