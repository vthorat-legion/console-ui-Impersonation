package com.legion.pages.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.google.inject.internal.cglib.reflect.$FastClass;
import com.legion.utils.JsonUtil;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.legion.pages.BasePage;
import com.legion.pages.ProfileNewUIPage;
import com.legion.tests.core.TeamTestKendraScott2.timeOffRequestAction;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;

import static com.legion.utils.MyThreadLocal.*;

public class ConsoleProfileNewUIPage extends BasePage implements ProfileNewUIPage{
	
	public ConsoleProfileNewUIPage(){
		PageFactory.initElements(getDriver(), this);
	}
	
	final static String consoleProfileMenuItemText = "Profile";

	@FindBy(className="console-navigation-item")
	private List<WebElement>consoleNavigationMenuItems;
	@FindBy(css = "div.profile")
	private WebElement profilePageSection;
	@FindBy(css="div.collapsible-title-text span")
	private List<WebElement> profilePageSubSections;
	@FindBy(css="[ng-click*=\"TimeOff()\"]")
	private WebElement newTimeOffBtn;
	@FindBy(css = "div.reasons-reason")
	private List<WebElement> timeOffReasons;
	@FindBy(css = ".lg-search-options__option")
	private List<WebElement> newTimeOffReasons;
	@FindBy(css = ".lg-picker-input__wrapper")
	private WebElement reasonsWrapper;
	@FindBy(css = "input[placeholder=\"Select...\"]")
	private WebElement reasonSelect;
	@FindBy(css = "textarea[placeholder=\"Optional explanation\"]")
	private WebElement timeOffExplanationtextArea;
	@FindBy(css= "div.real-day")
	private List<WebElement> calendarDates;
	@FindBy(css="a.calendar-nav-left")
	private WebElement timeOffCalendarLeftArrow;
	@FindBy(css="a.calendar-nav-right")
	private WebElement timeOffCalendarRightArrow;
	@FindBy(css="ranged-calendar[range-start=\"rangeStart\"]")
	private List<WebElement> timeOffCalendarMonths;
	@FindBy(css="button[ng-click=\"apply($event)\"]")
	private WebElement timeOffApplyBtn;
	@FindBy(css="div[ng-if=\"!editing.profile\"]")
	private List<WebElement> editingProfileSections;
	@FindBy(css="div.quick-profile")
	private WebElement quickProfileDiv;
	@FindBy(css="lgn-action-button[ng-click=\"changePassword()\"]")
	private WebElement changePasswordBtn;
	@FindBy(css="div[ng-if=\"canViewWorkerEngagement()\"]")
	private WebElement engagementDetailsSection;
	@FindBy(css="div.badge-section")
	private WebElement badgeSection;
	@FindBy(css="div.collapsible-title.collapsible-title-open")
	private List<WebElement> collapsibleTabsTitle;
	@FindBy(css="div[ng-if=\"!isPreferenceEdit()\"]")
	private WebElement shiftPreferenceSection;
	@FindBy(css="div[ng-if=\"!isPreferenceEdit()\"]")
	private WebElement myAvailabilitySection;
	@FindBy(css="div.count-block.count-block-pending")
	private WebElement timeOffPendingBlock;
	@FindBy(css="div.count-block.count-block-approved")
	private WebElement timeOffApprovedBlock;
	@FindBy(css="div.count-block.count-block-rejected")
	private WebElement timeOffRejectedBlock;
	@FindBy(css="div.timeoff-requests")
	private WebElement timeOffRequestsSection;
	@FindBy(css="div.location-selector-location-name-text")
	private WebElement locationSelectorLocationName;
	@FindBy(css="[timeoff=\"timeoff\"] .timeoff-requests-request.row-fx")
	private List<WebElement> timeOffRequestRows;
	@FindBy(css="i[ng-click=\"editProfile()\"]")
	private WebElement profileEditPencilIcon;
	@FindBy(css="button[ng-click=\"cancelEdit()\"]")
	private WebElement userProfileCancelBtn;
	@FindBy(css="div[ng-form=\"profileEdit\"]")
	private WebElement profileEditForm;
	@FindBy(css="button[type=\"submit\"]")
	private WebElement userProfileSaveBtn;
	@FindBy(css="input[name=\"firstName\"]")
	private WebElement profileFirstNameInputBox;
	@FindBy(css="input[name=\"lastName\"]")
	private WebElement profileLastNameInputBox;
	@FindBy(css="input[aria-label=\"Nickname\"]")
	private WebElement profileNickNameInputBox;
	@FindBy(css="input[aria-label=\"Street Address 1\"]")
	private WebElement profileAddressStreetAddress1InputBox;
	@FindBy(css="input[aria-label=\"Street Address 2\"]")
	private WebElement profileAddressStreetAddress2InputBox;
	@FindBy(css="input[aria-label=\"City\"]")
	private WebElement profileAddressCityInputBox;
	@FindBy(css="select[aria-label=\"State\"]")
	private WebElement profileAddressStateInputBox;
	@FindBy(css="input[aria-label=\"Zip\"")
	private WebElement profileAddressZipInputBox;
	@FindBy(css="input[name=\"phone\"]")
	private WebElement profileContactPhoneInputBox;
	@FindBy(css="input[name=\"email\"]")
	private WebElement profileContactEmailInputBox;
	@FindBy(css="lg-button[ng-click=\"invite()\"]")
	private WebElement userProfileInviteBtn;
	@FindBy(css="section[ng-form=\"inviteTm\"]")
	private WebElement inviteTeamMemberPopUp;
	@FindBy(css="input[id=\"phone\"]")
	private WebElement inviteTeamMemberPopUpPhoneField;
	@FindBy(css="input[id=\"email\"]")
	private WebElement inviteTeamMemberPopUpEmailField;
	@FindBy(css="textarea[name=\"message\"]")
	private WebElement inviteTeamMemberPopUpMessageField;
	@FindBy(css="button[ng-click=\"$dismiss()\"]")
	private WebElement inviteTeamMemberPopUpCancelBtn;
	@FindBy(css="button[ng-click=\"send()\"]")
	private WebElement inviteTeamMemberPopUpSendBtn;
	@FindBy(xpath="//span[text()=\"Change Password\"]")
	private WebElement userProfileChangePasswordBtn;
	@FindBy(css="div[ng-form=\"changePassword\"]")
	private WebElement changePasswordPopUp;
	@FindBy(css="input[ng-model=\"oldPassword\"]")
	private WebElement changePasswordPopUpOldPasswordField;
	@FindBy(css="input[ng-model=\"password\"]")
	private WebElement changePasswordPopUpNewPasswordField;
	@FindBy(css="input[ng-model=\"confirmPassword\"")
	private WebElement changePasswordPopUpConfirmPasswordField;
	@FindBy(css="div[ng-click=\"cancelCallback()\"]")
	private WebElement changePasswordPopUpPopUpCancelBtn;
	@FindBy(css="div[ng-click=\"okCallback()\"]")
	private WebElement changePasswordPopUpPopUpSendBtn;
	@FindBy(css="collapsible[identifier=\"'workpreference'\"]")
	private WebElement collapsibleWorkPreferenceSection;
	@FindBy(css="div.committed-hours")
	private WebElement commitedHoursDiv;
	@FindBy(css="div[ng-show=\"showPreference()\"]")
	private List<WebElement> showPreferenceOptionsDiv;
	@FindBy(css="div.badge-row")
	private WebElement profileBadgesRow;
	@FindBy(css="div.no-badges")
	private WebElement noBadgesDiv;
	@FindBy(css="div[ng-repeat=\"oneBadge in badges\"]")
	private List<WebElement> badgesDiv;
	@FindBy(css="div[ng-class=\"getClassForOtherLocationCheckButton()\"]")
	private WebElement otherLocationCheckButton;
	@FindBy(css="div[ng-class=\"tmPrefs.volunteerMoreHours ? 'enabled' : 'disabled'\"]")
	private WebElement volunteerMoreHoursCheckButton;
	@FindBy(css="div.edit-tm-avail.availability-slider")
	private List<WebElement> availabilitySliders;
	@FindBy(css="button[ng-click=\"savePreferences($event)\"]")
	private WebElement savePreferencesBtn;
	@FindBy(css="div.edit-availability-container")
	private WebElement ditAvailabilityContainerDiv;
	@FindBy(css="span.tm-hours-scheduled-data")
	private WebElement sheduleHoursData;
	@FindBy(css="span[ng-click=\"getLastWeekData()\"]")
	private WebElement pastWeekArrow;
	@FindBy(css="span[ng-click=\"getNextWeekData()\"]")
	private WebElement futureWeekArrow;
	@FindBy(css="span.week-nav-icon-main")
	private WebElement activeWeek;
	@FindBy(css="span.tm-remaining-hours-label-bold")
	private WebElement remainingHours;
	@FindBy(css="span.tm-total-hours-label-green")
	private WebElement totalHours;
	@FindBy(css="div.availability-shift")
	private List<WebElement> availableShifts;
	@FindBy(css="div.availability-row.availability-row-active")
	private List<WebElement> myAvailabilityDayOfWeekRows;
	@FindBy(css="button.lgn-action-button-success")
	private WebElement myAvailabilityUnLockBtn;
	@FindBy(css="lg-button[ng-click=\"onSave()\"]")
	private WebElement myAvailabilityEditModeSaveBtn;
	@FindBy(css="input-field[label=\"This week only\"] label")
	private WebElement MyAvailabilityEditSaveThisWeekOnlyBtn;
	@FindBy(css="input-field[label=\"Repeat forward\"] label")
	private WebElement MyAvailabilityEditSaveRepeatForwordBtn;
	@FindBy(css = "[ng-click=\"save()\"]")
	private WebElement myAvailabilityConfirmSubmitBtn;
	@FindBy(css="div.hour-cell.hour-cell-ghost.cursor-resizableE")
	private List<WebElement> hourCellsResizableCursorsRight;
	@FindBy(css="div.hour-cell.hour-cell-ghost.cursor-resizableW")
	private List<WebElement> hourCellsResizableCursorsLeft;
	@FindBy(css="div[ng-if=\"isAvailabilityEdit()\"]")
	private WebElement myAvailabilityEditModeHeader;
	@FindBy(css="div.count-block.count-block-pending")
	private WebElement pendingTimeOffCountDiv;
	@FindBy(css="div.count-block.count-block-approved")
	private WebElement approvedTimeOffCountDiv;
	@FindBy(css="div.count-block.count-block-rejected")
	private WebElement rejectedTimeOffCountDiv;
	@FindBy(css="todo-card[todo-type=\"todoType\"]")
	private List<WebElement> todoCards;
	@FindBy(css="a[ng-click=\"goRight()\"]")
	private WebElement nextToDoCardArrow;
	@FindBy(css="button.lgn-action-button-success")
	private WebElement confirmTimeOffApprovalBtn;
	//timeOffRequestCancelBtn last updated by Haya
	@FindBy(css="span[ng-if=\"canCancel(timeoff)\"]")
	private WebElement timeOffRequestCancelBtn;
	@FindBy(xpath="//div[contains(text(),'Starts')]/b")
	private WebElement timeOffRequestStartDate;
	@FindBy(xpath="//div[contains(text(),'End')]/b")
	private WebElement timeOffRequestEndDate;
	@FindBy(css="div.lgnCheckBox.checked")
	private WebElement checkBoxAllDay;
	@FindBy(css="span.all-day-label")
	private WebElement txtAllDay;
	@FindBy(css="div.header-avatar > img")
	private WebElement userProfileImage;
	@FindBy(css=".header-user-switch-menu-item-main")
	private WebElement userNickName;
	@FindBy(className = "request-buttons-reject")
	private WebElement timeOffRejectBtn;
	@FindBy(css = "form-section[on-action=\"editProfile()\"]")
	private WebElement profileSection;
	
	@Override
	public void clickOnProfileConsoleMenu() throws Exception {
		if(consoleNavigationMenuItems.size() != 0)
		{
			WebElement consoleScheduleMenuElement = SimpleUtils.getSubTabElement(consoleNavigationMenuItems, consoleProfileMenuItemText);
			click(consoleScheduleMenuElement);
			SimpleUtils.pass("'Profile' Console Menu Loaded Successfully!");
		}
		else {
			SimpleUtils.fail("'Profile' Console Menu Items Not Loaded Successfully!",false);
		}
	}
	
	@Override
	public boolean isProfilePageLoaded() throws Exception
	{
		if(isElementLoaded(profileSection, 25)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void selectProfilePageSubSectionByLabel(String profilePageSubSectionLabel) throws Exception {
		boolean isSubSectionSelected = false;
		scrollToTop();
		if(areListElementVisible(profilePageSubSections,60)) {
			for(WebElement profilePageSubSection : profilePageSubSections) {
				if(profilePageSubSection.getText().toLowerCase().contains(profilePageSubSectionLabel.toLowerCase())) {
					clickTheElement(profilePageSubSection);
					isSubSectionSelected = true;
					break;
				}
			}
			if(isSubSectionSelected)
				SimpleUtils.pass("Controls Page: '"+profilePageSubSectionLabel+"' sub section selected Successfully.");
			else
				SimpleUtils.fail("Controls Page: '"+profilePageSubSectionLabel+"' sub section not loaded.",false);
		}
		else
			SimpleUtils.fail("Profile Page: Sub Section not loaded.", false);
	}

	public void verifyAvailabilityWeek(String desiredweek) throws Exception {
		//scroll to the bottom of page to view the Availability table
		scrollToBottom();
		waitForSeconds(1);
		//get the week in Availability
        String currentWeek=String.valueOf(getMyAvailabilityData().get("activeWeekText"));
        if(desiredweek.equalsIgnoreCase(currentWeek))
			SimpleUtils.pass("The current week is the TM requested availability change week or the start of the week!");
        else
			SimpleUtils.fail("The go to profile for availability change week is not the set or the start of the week ", false);
	}

	@Override
	public void clickOnCreateTimeOffBtn() throws Exception {
		if(isElementLoaded(newTimeOffBtn, 15)) {
			clickTheElement(newTimeOffBtn);
			SimpleUtils.pass("Controls Page: 'Create Time Off' button Clicked.");
		}
		else
			SimpleUtils.fail("Controls Page: 'Create Time Off' button not loaded.", false);
	}

	@Override
	public boolean isReasonLoad(String timeOffReasonLabel) throws Exception{
		boolean result = false;
		List<WebElement> reasons = null;
		if(areListElementVisible(timeOffReasons, 20)) {
			reasons = timeOffReasons;
		} else if (isElementLoaded(reasonSelect, 5)) {
			if (!isElementLoaded(reasonsWrapper, 5)) {
				clickTheElement(reasonSelect);
			}
			if (areListElementVisible(newTimeOffReasons, 5)) {
				reasons = newTimeOffReasons;
			}
		}
		for(WebElement timeOffReason : reasons) {
			if(timeOffReason.getText().toLowerCase().contains(timeOffReasonLabel.toLowerCase())) {
				result = true;
				break;
			}
		}
		return result;
	}


	@FindBy(css = ".lgn-modal-body .picker-input input")
	private WebElement timeOffReasonSelector;

	@FindBy(css = ".lgn-modal-body .lg-search-options__option")
	private List<WebElement> timeOffReasonOptions;
	@Override
	public void selectTimeOffReason(String reasonLabel) throws Exception
	{
		boolean isTimeOffReasonSelected = false;
		waitForSeconds(3);
		if(areListElementVisible(timeOffReasons, 20)) {
			for(WebElement timeOffReason : timeOffReasons) {
				if(timeOffReason.getText().toLowerCase().contains(reasonLabel.toLowerCase())) {
					click(timeOffReason);
					isTimeOffReasonSelected = true;
				}
			}
			if(isTimeOffReasonSelected)
				SimpleUtils.pass("Controls Page: Time Off Reason '"+ reasonLabel +"' selected successfully.");
			else
				SimpleUtils.fail("Controls Page: Time Off Reason '"+ reasonLabel +"' not found.", false);
		} else if (isElementLoaded(timeOffReasonSelector, 5)) {
			if (!isElementLoaded(reasonsWrapper, 5)) {
				clickTheElement(reasonSelect);
			}
			if (areListElementVisible(timeOffReasonOptions)) {
				boolean isTimeOffReasonExists = false;
				for (WebElement option: timeOffReasonOptions) {
					if (option.getText().toLowerCase().contains(reasonLabel.toLowerCase())) {
						clickTheElement(option);
						SimpleUtils.pass("Select the time off reason: "+reasonLabel +" successfully! ");
						isTimeOffReasonExists = true;
						break;
					}
				}
				if (!isTimeOffReasonExists) {
					SimpleUtils.fail("The time off reason: "+ reasonLabel+ " is not exists in the reason list! ", false);
				}
			} else
				SimpleUtils.fail("There is no reasons in the time off reason selector! ", false);
		} else
			SimpleUtils.fail("There is no time off reasons can be select! ", false);
	}
	
	
	@Override
	public void updateTimeOffExplanation(String explanationText) throws Exception
	{
		if(isElementLoaded(timeOffExplanationtextArea)) {
			timeOffExplanationtextArea.sendKeys(explanationText);
			SimpleUtils.pass("Controls Page: Time Off Optional explanation updated successfully.");
		}
		else
			SimpleUtils.fail("Controls Page: 'Optional explanation' text Area not loaded.", false);
	}
	

	@Override
	public void selectTimeOffDuration(String startDate, String endDate) throws Exception
	{		
		String disabledCalendarDayClass = "can-not-select";
		String timeOffStartDate = startDate.split(",")[0].split(" ")[1];
		String timeOffStartMonth = startDate.split(",")[0].split(" ")[0];
		int timeOffStartYear = Integer.valueOf(startDate.split(",")[1].trim());
		
		String timeOffEndDate = endDate.split(",")[0].split(" ")[1];
		String timeOffEndMonth = endDate.split(",")[0].split(" ")[0];
		int timeOffEndYear = Integer.valueOf(endDate.split(",")[1].trim());
		
		WebElement changeMonthArrow = timeOffCalendarRightArrow;
		WebElement timeOffMonthAndYear = timeOffCalendarMonths.get(0).findElement(By.cssSelector("div.ranged-calendar__month-name"));
		// Selecting Start date
		while(timeOffStartYear != Integer.valueOf(timeOffMonthAndYear.getText().split(" ")[1])
				|| ! timeOffMonthAndYear.getText().toLowerCase().contains(timeOffStartMonth.toLowerCase()))
		{
			if(timeOffMonthAndYear.getText().toLowerCase().contains(timeOffStartMonth.toLowerCase())) {
				if(timeOffStartYear > Integer.valueOf(timeOffMonthAndYear.getText().split(" ")[1]))
					changeMonthArrow = timeOffCalendarRightArrow;
				else
					changeMonthArrow = timeOffCalendarLeftArrow;
			}
			click(changeMonthArrow);
		}	
		if(calendarDates.size() > 0) {
			for(WebElement calendarDate : calendarDates) {
				if(Integer.valueOf(calendarDate.getText()) == Integer.valueOf(timeOffStartDate)) {
					if(calendarDate.getAttribute("class").contains(disabledCalendarDayClass)) {
						SimpleUtils.fail("Profile Page: Time Off requests must be made at least 15 days in advance, given Start Date '"
								+startDate+"'.", false);
					}
					else {
						click(calendarDate);
						SimpleUtils.pass("Profile Page: New Time Off Request Start Date '" +startDate+" selected successfully'.");
						break;
					}
				}
			}
		}
		else
			SimpleUtils.fail("Controls Page: 'Calendar' not loaded.", false);
		
		
		// Selecting End date
		while(timeOffEndYear != Integer.valueOf(timeOffMonthAndYear.getText().split(" ")[1])
				|| ! timeOffMonthAndYear.getText().toLowerCase().contains(timeOffEndMonth.toLowerCase()))
		{
			if(timeOffMonthAndYear.getText().toLowerCase().contains(timeOffEndMonth.toLowerCase())) {
				if(timeOffEndYear > Integer.valueOf(timeOffMonthAndYear.getText().split(" ")[1]))
					changeMonthArrow = timeOffCalendarRightArrow;
				else
					changeMonthArrow = timeOffCalendarLeftArrow;
			}
			click(changeMonthArrow);
		}	
		if(calendarDates.size() > 0) {
			for(WebElement calendarDate : calendarDates) {
				if(Integer.valueOf(calendarDate.getText()) == Integer.valueOf(timeOffEndDate)) {
					if(calendarDate.getAttribute("class").contains(disabledCalendarDayClass)) {
						SimpleUtils.fail("Profile Page: Time Off requests must be made at least 15 days in advance, given End Date '"
								+endDate+"'.", false);
					}
					else {
						click(calendarDate);
						SimpleUtils.pass("Profile Page: New Time Off Request End Date '" +endDate+" selected successfully'.");
						break;
					}
				}
			}
		}
		else
			SimpleUtils.fail("Controls Page: 'Calendar' not loaded.", false);
	}


	@FindBy(css = "[label=\"OK\"] button")
	private WebElement OKButtonOnNewTimeOffPage;
	@Override
	public void clickOnSaveTimeOffRequestBtn() throws Exception
	{
		waitForSeconds(3);
		if(isElementLoaded(timeOffApplyBtn, 5)) {
			click(timeOffApplyBtn);
		} else if (isElementLoaded(OKButtonOnNewTimeOffPage, 5)) {
			click(OKButtonOnNewTimeOffPage);
		} else
			SimpleUtils.fail("Profile Page: Unable to save New Time Off Request.", false);
			
	}
	
	
	@Override
	public void createNewTimeOffRequest(String timeOffReasonLabel, String timeOffExplanationText) throws Exception {
		final int timeOffRequestCount = timeOffRequestRows.size();
		clickOnCreateTimeOffBtn();
		Thread.sleep(1000);
		selectTimeOffReason(timeOffReasonLabel);
        updateTimeOffExplanation(timeOffExplanationText);
        selectDate(17);
		selectDate(21);
		HashMap<String, String> timeOffDate = getTimeOffDate(17, 21);
		String timeOffStartDate = timeOffDate.get("startDateTimeOff");
		String timeOffEndDate = timeOffDate.get("endDateTimeOff");
		setTimeOffStartTime(timeOffStartDate);
		setTimeOffEndTime(timeOffEndDate);
        clickOnSaveTimeOffRequestBtn();
        Thread.sleep(1000);
        if(timeOffRequestRows.size() > timeOffRequestCount) 
        	SimpleUtils.pass("Profile Page: New Time Off Save Successfully.");
        else
        	SimpleUtils.fail("Profile Page: New Time Off not Save Successfully.", false);
	}

	@Override
	public String getTimeOffRequestStatus(String timeOffReasonLabel, String timeOffExplanationText,
			String timeOffStartDuration, String timeOffEndDuration) throws Exception {
//		String timeOffStartDate = timeOffStartDuration.split(", ")[1].toUpperCase();
////		String timeOffStartMonth = timeOffStartDuration.split(",")[0].split(" ")[0];
//		String timeOffEndDate = timeOffEndDuration.split(", ")[1].toUpperCase();
////		String timeOffEndMonth = timeOffEndDuration.split(",")[0].split(" ")[0];
		String timeOffStartDate = timeOffStartDuration;
		String timeOffEndDate = timeOffEndDuration;
		if (timeOffStartDuration.length()>10) {
			timeOffStartDate = timeOffStartDuration.substring(5, 11);
		}
		if (timeOffEndDuration.length()>10){
			timeOffEndDate = timeOffEndDuration.substring(5, 11);
		}
		String requestStatusText = "";
		if(areListElementVisible(timeOffRequestRows, 10)) {
			int timeOffRequestCount = timeOffRequestRows.size();
			if (timeOffRequestCount > 0) {
				for (int i = 0; i < timeOffRequestRows.size(); i++) {
					WebElement timeOffRequest = timeOffRequestRows.get(i);
					WebElement requestType = timeOffRequest.findElement(By.cssSelector("span.request-type"));
					WebElement requestStatus = timeOffRequest.findElement(By.cssSelector("span.request-status"));
					String requestTypeText = requestType.getText();
					if (timeOffReasonLabel.toLowerCase().contains(requestTypeText.toLowerCase())) {
						WebElement requestDate = timeOffRequest.findElement(By.cssSelector("div.request-date"));
						String requestDateText = requestDate.getText().replaceAll("\n", " ");
						if (requestDateText.contains("-")) {
							if (requestDateText.split("-")[0].trim().toLowerCase().contains(timeOffStartDate.toLowerCase())
									&& requestDateText.split("-")[1].trim().toLowerCase().contains(timeOffEndDate.toLowerCase())) {
								requestStatusText = requestStatus.getText();
							}
						} else if ((requestDateText.split(" ")[2] + " " + requestDateText.split(" ")[1]).equalsIgnoreCase(timeOffStartDate)
								&& timeOffStartDate.equals(timeOffEndDate)) {
							requestStatusText = requestStatus.getText();
						}
					}
				}
			} else
				SimpleUtils.fail("Profile Page: No Time off request found.", true);
		} else
			SimpleUtils.fail("Profile Page: Time off request failed to load",true);
		return requestStatusText;
	}

	@Override
	public String getTimeOffRequestStatusByExplanationText(String timeOffExplanationText) throws Exception {
		String requestStatusText = "";
		if(areListElementVisible(timeOffRequestRows, 10) && timeOffRequestRows.size() > 0) {
			for (WebElement timeOffRequest: timeOffRequestRows) {
				WebElement requestStatus = timeOffRequest.findElement(By.cssSelector("span.request-status"));
				try {
					WebElement timeOffReason = timeOffRequest.findElement(By.cssSelector("[ng-if=\"timeoff.reason\"]"));
					if (timeOffReason.getText().contains(timeOffExplanationText)) {
						requestStatusText = requestStatus.getText();
						break;
					}
				}catch (Exception e) {
					continue;
				}
			}
		} else
			SimpleUtils.fail("Profile Page: Time off request failed to load",true);
		return requestStatusText;
	}
	

	@Override
	public boolean isEditingProfileSectionLoaded() throws Exception
	{
		if(editingProfileSections.size() > 0)
		{
			
			WebElement profileSection = editingProfileSections.get(0);
			if(profileSection.getText().length() > 0) {
				SimpleUtils.pass("Profile Page: Editing Profile Section Loaded successfully.");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPersonalDetailsSectionLoaded() throws Exception
	{
		if(isElementLoaded(quickProfileDiv))
		{
			if(quickProfileDiv.getText().length() > 0) {
				SimpleUtils.pass("Profile Page: Personal Details Section on 'About Me' tab Loaded successfully.");
				return true;
			}
		}
		return false;
	}
	

	
	@Override
	public boolean isChangePasswordButtonLoaded() throws Exception
	{
		if(isElementLoaded(changePasswordBtn) && changePasswordBtn.isDisplayed())
		{
			SimpleUtils.pass("Profile Page: Change Password Button on 'About Me' tab Loaded successfully.");
			return true;
		}
		return false;
	}
	

	@Override
	public boolean isEngagementDetrailsSectionLoaded() throws Exception
	{
		if(isElementLoaded(engagementDetailsSection))
		{
			if(engagementDetailsSection.getText().length() > 0) {
				SimpleUtils.pass("Profile Page: Engagement Detrails Section on 'About Me' tab Loaded successfully.");
				return true;
			}
		}
		return false;
	}
	

	@Override
	public boolean isProfileBadgesSectionLoaded() throws Exception
	{
		if(isElementLoaded(badgeSection))
		{
			if(badgeSection.getText().length() > 0) {
				SimpleUtils.pass("Profile Page: Badges Section on 'About Me' tab Loaded successfully.");
				return true;
			}
		}
		return false;
	}
	

	@Override
	public String getProfilePageActiveTabLabel() throws Exception
	{
		String activeTabLabel = "";
		if(collapsibleTabsTitle.size() > 0) {
			for(WebElement tabTitle: collapsibleTabsTitle) {
				if(tabTitle.isDisplayed()) {
					activeTabLabel = tabTitle.getText();
					break;
				}
			}
		}
		else
			SimpleUtils.fail("Profile Page: Tabs not loaded.", true);
		return activeTabLabel;
	}
	

	@Override
	public boolean isShiftPreferenceSectionLoaded() throws Exception
	{
		String myShiftPreferencesTabTitle = "My Shift Preferences";
		if(isElementLoaded(shiftPreferenceSection))
		{
			if(! shiftPreferenceSection.isDisplayed())
				selectProfilePageSubSectionByLabel(myShiftPreferencesTabTitle);
			if(shiftPreferenceSection.getText().length() > 0) {
				SimpleUtils.pass("Profile Page: "+myShiftPreferencesTabTitle+" Section on 'My Work Preferences' tab Loaded successfully.");
				return true;
			}
		}
		return false;
	}
	

	@Override
	public boolean isMyAvailabilitySectionLoaded() throws Exception
	{
		String myAvailabilityTabTitle = "My Availability";
		if(isElementLoaded(myAvailabilitySection))
		{
			if(! myAvailabilitySection.isDisplayed())
				selectProfilePageSubSectionByLabel(myAvailabilityTabTitle);
			if(myAvailabilitySection.getText().length() > 0) {
				SimpleUtils.pass("Profile Page: "+myAvailabilityTabTitle+" Section on 'My Work Preferences' tab Loaded successfully.");
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isCreateTimeOffButtonLoaded() throws Exception
	{
		if(isElementLoaded(newTimeOffBtn) && newTimeOffBtn.isDisplayed()) {
			SimpleUtils.pass("Profile Page: Create Time off button on 'My Time Off' tab Loaded successfully.");
				return true;
		}
		return false;
	}
	

	@Override
	public boolean isTimeOffPendingBlockLoaded() throws Exception
	{
		if(isElementLoaded(timeOffPendingBlock) && timeOffPendingBlock.isDisplayed() && timeOffPendingBlock.getText().length() > 0)
		{
				SimpleUtils.pass("Profile Page: 'Time Off Pending Block' Section on 'My Time Off' tab Loaded successfully.");
				return true;
		}
		return false;
	}
	

	@Override
	public boolean isTimeOffApprovedBlockLoaded() throws Exception
	{
		if(isElementLoaded(timeOffApprovedBlock) && timeOffApprovedBlock.isDisplayed() && timeOffApprovedBlock.getText().length() > 0)
		{
				SimpleUtils.pass("Profile Page: 'Time Off Approved Block' Section on 'My Time Off' tab Loaded successfully.");
				return true;
		}
		return false;
	}
	

	@Override
	public boolean isTimeOffRejectedBlockLoaded() throws Exception
	{
		if(isElementLoaded(timeOffRejectedBlock) && timeOffRejectedBlock.isDisplayed() && timeOffRejectedBlock.getText().length() > 0)
		{
				SimpleUtils.pass("Profile Page: 'Time Off Rejected Block' Section on 'My Time Off' tab Loaded successfully.");
				return true;
		}
		return false;
	}
	

	@Override
	public boolean isTimeOffRequestsSectionLoaded() throws Exception
	{
		if(isElementLoaded(timeOffRequestsSection) && timeOffRequestsSection.isDisplayed() && timeOffRequestsSection.getText().length() > 0)
		{
				SimpleUtils.pass("Profile Page: 'Time Off Requests Section' Section on 'My Time Off' tab Loaded successfully.");
				return true;
		}
		return false;
	}
	
	
	@Override
	public String getProfilePageActiveLocation() throws Exception
	{
		String activeLocation = "";
		if(isElementLoaded(locationSelectorLocationName) && locationSelectorLocationName.isDisplayed())
		{
			activeLocation = locationSelectorLocationName.getText();
		}
		return activeLocation;
	}
	
	@Override
	public int getAllTimeOffRequestCount() {
		int timeOffRequestCount = timeOffRequestRows.size();
		return timeOffRequestCount;
	}
	
	@Override
	public void updateUserProfile(String firstName, String lastname, String nickName, String streetAddress1,
			String streetAddress2, String city, String state, String zip, String phone, String email) throws Exception
	{
		clickOnEditUserProfilePencilIcon();
		updateUserProfileName("Jamie", "Ward", "Mr. Jamie.W");
		updateUserProfileHomeAddress("", "", "", "", "");
		updateUserProfileContacts("2025550124", "jamie.w@kendrascott2.legion.co");
		//clickOnSaveUserProfileBtn();
		clickOnCancelUserProfileBtn();
	}

	@Override
	public void clickOnCancelUserProfileBtn() throws Exception {
		if(areListElementVisible(cancelBtnsOnProfile,5))
			clickTheElement(cancelBtnsOnProfile.get(0));
		waitForSeconds(2);
		if(isElementLoaded(getDriver().findElement(By.cssSelector("[on-action=\"editProfile()\"] lg-button[label=\"Edit\"]")), 10))
			SimpleUtils.pass("Profile Page: User profile Cancel Button clicked.");
		else
			SimpleUtils.fail("Profile Page: unable to cancel edit User profile popup.", false);
	}

	@FindBy (css = "[ng-click=\"editProfile()\"]")
	private WebElement editProfileButton;
	@FindBy (className = "btn-success")
	private WebElement saveTMBtn;
	@FindBy (css = "[label=\"Cancel\"]")
	private List<WebElement> cancelBtnsOnProfile;

	@Override
	public void clickOnEditUserProfilePencilIcon() throws Exception
	{
		if(isElementLoaded(profileSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")),10))
			clickTheElement(profileSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")));
		//verify if edit profile mode load
		if(areListElementVisible(saveBtnsOfProfile,10))
			SimpleUtils.pass("Profile Page: User profile edit form loaded successfully.");
		else
			SimpleUtils.fail("Profile Page: User profile edit form not loaded.", false);
	}

	@Override
	public void clickOnSaveUserProfileBtn() throws Exception
	{
		try {
			if (areListElementVisible(saveBtnsOfProfile, 5) && saveBtnsOfProfile.size() > 0) {
				scrollToElement(saveBtnsOfProfile.get(0));
				clickTheElement(saveBtnsOfProfile.get(0));
			}
			waitForSeconds(3);
			if (isElementLoaded(profileSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")), 15)) {
				SimpleUtils.pass("Profile Page: User profile successfully saved.");
			} else {
				SimpleUtils.fail("Profile Page: unable to save User profile.", false);
			}
		} catch (Exception e) {
			SimpleUtils.fail(e.toString(), false);
		}
	}

	
	public void updateUserProfileName(String firstName, String lastname, String nickName) throws Exception
	{
		// Updating First Name
		if(isElementLoaded(profileFirstNameInputBox)) {
			if(profileFirstNameInputBox.getAttribute("value").toLowerCase().contains(firstName.toLowerCase()))
				SimpleUtils.pass("Profile Page: User Profile 'First Name' already updated with value: '"+firstName+"'.");
			else {
				profileFirstNameInputBox.clear();
				profileFirstNameInputBox.sendKeys(firstName);
				SimpleUtils.pass("Profile Page: User Profile 'First Name' updated with value: '"+firstName+"'.");
			}
		}
		
		// Updating Last Name
		if(isElementLoaded(profileLastNameInputBox)) {
			if(profileLastNameInputBox.getAttribute("value").toLowerCase().contains(lastname.toLowerCase()))
				SimpleUtils.pass("Profile Page: User Profile 'Last Name' already updated with value: '"+lastname+"'.");
			else {
				profileLastNameInputBox.clear();
				profileLastNameInputBox.sendKeys(lastname);
				SimpleUtils.pass("Profile Page: User Profile 'Last Name' updated with value: '"+lastname+"'.");
			}
		}
		
		// Updating First Name
		if(isElementLoaded(profileNickNameInputBox)) {
			if(profileNickNameInputBox.getAttribute("value").toLowerCase().contains(nickName.toLowerCase()))
				SimpleUtils.pass("Profile Page: User Profile 'Nick Name' already updated with value: '"+nickName+"'.");
			else {
				profileNickNameInputBox.clear();
				profileNickNameInputBox.sendKeys(nickName);
				SimpleUtils.pass("Profile Page: User Profile 'Nick Name' updated with value: '"+nickName+"'.");
			}
		}
	}
					
	public void updateUserProfileHomeAddress(String streetAddress1, String streetAddress2, String city, String state, String zip) throws Exception
	{
		if (isElementLoaded(profileSection, 5)) {
			// Updating Home Address Street Address 1
			if (isElementLoaded(profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")), 5)) {
				if (profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")).getAttribute("value").toLowerCase().contains(streetAddress1.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 1' already updated with value: '"
							+ streetAddress1 + "'.");
				else {
					profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")).clear();
					profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")).sendKeys(streetAddress1);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 1' updated with value: '"
							+ streetAddress1 + "'.");
				}
			}

			// Updating Home Address Street Address 2
			if (isElementLoaded(profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")), 5)) {
				if (profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")).getAttribute("value").toLowerCase().contains(streetAddress2.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 2' already updated with value: '"
							+ streetAddress2 + "'.");
				else {
					profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")).clear();
					profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")).sendKeys(streetAddress2);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 2' updated with value: '"
							+ streetAddress2 + "'.");
				}
			}

			// Updating Home Address City
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")), 5)) {
				if (profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")).getAttribute("value").toLowerCase().contains(city.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'City' already updated with value: '" + city + "'.");
				else {
					profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")).clear();
					profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")).sendKeys(city);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'City' updated with value: '" + city + "'.");
				}
			}

			// Updating Home Address State
			try {
				if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"State\"] select")), 5)) {
					selectByVisibleText(profileSection.findElement(By.cssSelector("input-field[label=\"State\"] select")), state);
				} else if (isElementLoaded(getDriver().findElement(By.cssSelector("input-field[label=\"Province\"] select")), 5)) {
					selectByVisibleText(getDriver().findElement(By.cssSelector("input-field[label=\"Province\"] select")), state);
				} else {
					SimpleUtils.fail("Profile page: State/Province select failed to load!", false);
				}
			} catch (Exception e) {
				// Do nothing
			}

			// Updating Home Address Zip
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")), 5)) {
				if (profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")).getAttribute("value").toLowerCase().contains(zip.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Zip' already updated with value: '" + zip + "'.");
				else {
					profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")).clear();
					profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")).sendKeys(zip);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Zip' updated with value: '" + zip + "'.");
				}
			}
		}else {
			// Updating Home Address Street Address 1
			if(isElementLoaded(profileAddressStreetAddress1InputBox)) {
				if(profileAddressStreetAddress1InputBox.getAttribute("value").toLowerCase().contains(streetAddress1.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 1' already updated with value: '"
							+streetAddress1+"'.");
				else {
					profileAddressStreetAddress1InputBox.clear();
					profileAddressStreetAddress1InputBox.sendKeys(streetAddress1);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 1' updated with value: '"
							+streetAddress1+"'.");
				}
			}

			// Updating Home Address Street Address 2
			if(isElementLoaded(profileAddressStreetAddress2InputBox)) {
				if(profileAddressStreetAddress2InputBox.getAttribute("value").toLowerCase().contains(streetAddress2.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 2' already updated with value: '"
							+streetAddress2+"'.");
				else {
					profileAddressStreetAddress2InputBox.clear();
					profileAddressStreetAddress2InputBox.sendKeys(streetAddress2);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 2' updated with value: '"
							+streetAddress2+"'.");
				}
			}

			// Updating Home Address City
			if(isElementLoaded(profileAddressCityInputBox)) {
				if(profileAddressCityInputBox.getAttribute("value").toLowerCase().contains(city.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'City' already updated with value: '"+city+"'.");
				else {
					profileAddressCityInputBox.clear();
					profileAddressCityInputBox.sendKeys(city);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'City' updated with value: '"+city+"'.");
				}
			}

			// Updating Home Address State
			if(isElementLoaded(profileAddressStateInputBox)) {
				boolean isStateSelected = false;
				Select statesDropdown = new Select(profileAddressStateInputBox);
				if(statesDropdown.getFirstSelectedOption().getText().toLowerCase().contains(state.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Nick Name already updated with value: '"+state+"'.");
				else {
					for(WebElement stateOption : statesDropdown.getOptions()) {
						if(stateOption.getText().toLowerCase().contains(state.toLowerCase())) {
							click(stateOption);
							isStateSelected = true;
						}
					}
					if(isStateSelected)
						SimpleUtils.pass("Profile Page: User Profile Home Address 'State' updated with value: '"+state+"'.");
					else
						SimpleUtils.fail("Profile Page: User Profile Home Address State: '"+state+"' not found.", true);
				}
			}

			// Updating Home Address Zip
			if(isElementLoaded(profileAddressZipInputBox)) {
				if(profileAddressZipInputBox.getAttribute("value").toLowerCase().contains(zip.toLowerCase()))
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Zip' already updated with value: '"+zip+"'.");
				else {
					profileAddressZipInputBox.clear();
					profileAddressZipInputBox.sendKeys(zip);
					SimpleUtils.pass("Profile Page: User Profile Home Address 'Zip' updated with value: '"+zip+"'.");
				}
			}
		}
	}
	
	
	
	public void updateUserProfileContacts(String phone, String email) throws Exception
	{
		// Updating Home Address Phone
		if(isElementLoaded(profileContactPhoneInputBox)) {
			if(profileContactPhoneInputBox.getAttribute("value").toLowerCase().contains(phone.toLowerCase()))
				SimpleUtils.pass("Profile Page: User Profile Contact 'Phone' already updated with value: '"+phone+"'.");
			else {
				profileContactPhoneInputBox.clear();
				profileContactPhoneInputBox.sendKeys(phone);
				SimpleUtils.pass("Profile Page: User Profile Contact 'Phone' updated with value: '"+phone+"'.");
			}
		}
		
		// Updating Home Address Email
		if(isElementLoaded(profileContactEmailInputBox)) {
			if(profileContactEmailInputBox.getAttribute("value").toLowerCase().contains(email.toLowerCase()))
				SimpleUtils.pass("Profile Page: User Profile Contact 'Email' already updated with value: '"+email+"'.");
			else {
				profileContactEmailInputBox.clear();
				profileContactEmailInputBox.sendKeys(email);
				SimpleUtils.pass("Profile Page: User Profile Contact 'Email' updated with value: '"+email+"'.");
			}
		}
	}

	@Override
	public TreeMap<String, String> getUserProfileEngagementDetails() throws Exception {
		TreeMap<String, String> userProfileEngagementDetails = new TreeMap<String, String>();
		if(isElementLoaded(engagementDetailsSection)) {
			
			List<WebElement> rows = engagementDetailsSection.findElements(By.cssSelector("div.row"));
			for(int index = 1; index < rows.size() ; index++) {
				if(rows.get(index).getText().toLowerCase().contains("date hired") 
						&& rows.get(index).getText().toLowerCase().contains("job title")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 1) {
						userProfileEngagementDetails.put("dateHired", rowValues[0]);
						userProfileEngagementDetails.put("jobTitle", rowValues[1]);
					}
					else {
						SimpleUtils.fail("Profile Page: Unable to get Date Hired and Job Title value from ' Engagement Details Section'", true);
					}
				}
				else if(rows.get(index).getText().toLowerCase().contains("employee id")) {
					userProfileEngagementDetails.put("employeeId", rows.get(index + 1).getText());
				}
				
				else if(rows.get(index).getText().toLowerCase().contains("home store")) {
					userProfileEngagementDetails.put("homeStore", rows.get(index + 1).getText());
				}
				else if(rows.get(index).getText().toLowerCase().contains("engagement status")) {
					userProfileEngagementDetails.put("engagementStatus", rows.get(index + 1).getText());
				}
				else if(rows.get(index).getText().toLowerCase().contains("hourly")) {
					userProfileEngagementDetails.put("hourly", rows.get(index + 1).getText());
				}
				else if(rows.get(index).getText().toLowerCase().contains("salaried")) {
					userProfileEngagementDetails.put("salaried", rows.get(index + 1).getText());
				}
				
				else if(rows.get(index).getText().toLowerCase().contains("exempt")) {
					userProfileEngagementDetails.put("exempt", rows.get(index + 1).getText());
				}
				else if(rows.get(index).getText().toLowerCase().contains("status")) {
					String[] statusValue = rows.get(index).getText().split("\n");
					if(statusValue.length == 3)
						userProfileEngagementDetails.put("status", statusValue[1] + " "+ statusValue[2]);
					else
						SimpleUtils.fail("Profile Page: Unable to get Status value from ' Engagement Details Section'", true);

				}
			}
		}
		return userProfileEngagementDetails;
	}

	
	@Override
	public void userProfileInviteTeamMember() throws Exception {
		if(isElementLoaded(userProfileInviteBtn, 5)) {
			click(userProfileInviteBtn);
			SimpleUtils.pass("Profile Page: user profile 'Invite' button clicked successfully.");
			
			if(isElementLoaded(inviteTeamMemberPopUp,5)) {
				SimpleUtils.pass("Profile Page: user profile 'Invite Team Member' popup loaded successfully.");
				
				if(isElementLoaded(inviteTeamMemberPopUpPhoneField,5)) {
					String inviteTeamMemberPopUpPhoneFieldValue = inviteTeamMemberPopUpPhoneField.getAttribute("value");
					if(inviteTeamMemberPopUpPhoneFieldValue.length() > 0)
						SimpleUtils.pass("'Invite Team Member' popup 'Phone' Input field loaded with value:'"+inviteTeamMemberPopUpPhoneFieldValue+"'.");
					else
						SimpleUtils.fail("'Invite Team Member' popup 'Phone' Input field contains Blank value.", true);
				}
				else
					SimpleUtils.report("'Invite Team Member' popup 'Phone' Input field not loaded.");
				
				if(isElementLoaded(inviteTeamMemberPopUpEmailField,5)) {
					String inviteTeamMemberPopUpEmailFieldValue = inviteTeamMemberPopUpEmailField.getAttribute("value");
					if(inviteTeamMemberPopUpEmailFieldValue.length() > 0)
						SimpleUtils.pass("'Invite Team Member' popup 'Email' Input field loaded with value:'"+inviteTeamMemberPopUpEmailFieldValue+"'.");
					else
						SimpleUtils.fail("'Invite Team Member' popup 'Email' Input field contains Blank value.", true);
				}
				else
					SimpleUtils.fail("'Invite Team Member' popup 'Email' Input field not loaded.", true);
				
				if(isElementLoaded(inviteTeamMemberPopUpMessageField,5)) {
					String inviteTeamMemberPopUpMessageFieldValue = inviteTeamMemberPopUpMessageField.getAttribute("value");
					if(inviteTeamMemberPopUpMessageFieldValue.length() > 0)
						SimpleUtils.pass("'Invite Team Member' popup 'Message' Input field loaded with value:'"+inviteTeamMemberPopUpMessageFieldValue+"'.");
					else
						SimpleUtils.fail("'Invite Team Member' popup 'Message' Input field contains Blank value.", true);
				}
				else
					SimpleUtils.fail("'Invite Team Member' popup 'Message' Input field not loaded.", true);
				
				if(isElementLoaded(inviteTeamMemberPopUpSendBtn,5) && inviteTeamMemberPopUpSendBtn.isEnabled()) {
					SimpleUtils.pass("'Invite Team Member' popup 'Send' Button not loaded successfully.");
				}
				else
					SimpleUtils.fail("'Invite Team Member' popup 'Send' Button not loaded.", true);
				
				if(isElementLoaded(inviteTeamMemberPopUpCancelBtn,5) && inviteTeamMemberPopUpCancelBtn.isEnabled()) {
					SimpleUtils.pass("'Invite Team Member' popup 'Cancel' Button not loaded successfully.");
					click(inviteTeamMemberPopUpSendBtn);
					SimpleUtils.pass("'Invite Team Member' popup 'Cancel' Button clicked successfully.");
				}
				else
					SimpleUtils.fail("'Invite Team Member' popup 'Cancel' Button not loaded.", true);	
				
			}
			else
				SimpleUtils.fail("Profile Page: user profile 'Invite Team Memeber' popup not loaded.", false);			
		}
		else
			SimpleUtils.fail("Profile Page: user profile 'Invite' button not Available.", false);
	}
			
	@Override
	public void userProfileChangePassword(String oldPassword, String newPassword, String confirmPassword) throws Exception {
		if(isElementLoaded(userProfileChangePasswordBtn, 5)) {
			click(userProfileChangePasswordBtn);
			SimpleUtils.pass("Profile Page: user profile 'Change Password' button clicked successfully.");
			
			if(isElementLoaded(changePasswordPopUp)) {
				SimpleUtils.pass("Profile Page: user profile 'Change Password' popup loaded successfully.");
				
				if(isElementLoaded(changePasswordPopUpOldPasswordField)) {
					SimpleUtils.pass("'Change Password' popup 'Old Password' Input field loaded successfully.");
					changePasswordPopUpOldPasswordField.clear();
					changePasswordPopUpOldPasswordField.sendKeys(oldPassword);
				}
				else
					SimpleUtils.fail("'Change Password' popup 'Old Password' Input field not loaded.", true);
				
				if(isElementLoaded(changePasswordPopUpNewPasswordField)) {
					SimpleUtils.pass("'Change Password' popup 'New Password' Input field loaded successfully.");
					changePasswordPopUpNewPasswordField.clear();
					changePasswordPopUpNewPasswordField.sendKeys(newPassword);
				}
				else
					SimpleUtils.fail("'Change Password' popup 'New Password' Input field not loaded.", true);
				
				if(isElementLoaded(changePasswordPopUpConfirmPasswordField)) {
					SimpleUtils.pass("'Change Password' popup 'Confirm New Password' Input field loaded successfully.");
					changePasswordPopUpConfirmPasswordField.clear();
					changePasswordPopUpConfirmPasswordField.sendKeys(confirmPassword);
				}
				else
					SimpleUtils.fail("'Change Password' popup 'Confirm New Password' Input field not loaded.", true);
				
				
				if(isElementLoaded(changePasswordPopUpPopUpSendBtn) && changePasswordPopUpPopUpSendBtn.isEnabled()) {
					SimpleUtils.pass("'Change Password' popup 'Ok' Button not loaded successfully.");
				}
				else
					SimpleUtils.fail("'Change Password' popup 'Ok' Button not loaded.", true);
				
				if(isElementLoaded(changePasswordPopUpPopUpCancelBtn) && changePasswordPopUpPopUpCancelBtn.isEnabled()) {
					SimpleUtils.pass("'Change Password' popup 'Cancel' Button not loaded successfully.");
					click(changePasswordPopUpPopUpCancelBtn);
					SimpleUtils.pass("'Change Password' popup 'Cancel' Button clicked successfully.");
				}
				else
					SimpleUtils.fail("'Change Password' popup 'Cancel' Button not loaded.", true);	
				
			}
			else
				SimpleUtils.fail("Profile Page: user profile 'Change Password' popup not loaded.", false);			
		}
		else
			SimpleUtils.report("Profile Page: user profile 'Change Password' button not loaded.");
	}
	
	
	@Override
	public boolean isShiftPreferenceCollapsibleWindowOpen() throws Exception
	{
		if(isElementLoaded(collapsibleWorkPreferenceSection)) {
			WebElement collapsibleRowDiv = collapsibleWorkPreferenceSection.findElement(By.cssSelector("div.collapsible-title"));
			if(isElementLoaded(collapsibleRowDiv)) {
				if(collapsibleRowDiv.getAttribute("class").contains("open"))
					return true;
			}
			else 
				SimpleUtils.fail("Profile Page: Collapsible Work Preference window not loaded under 'My Shift Preference' Tab.", true);
		}
		else 
			SimpleUtils.fail("Profile Page: Collapsible Work Preference Section not loaded under 'My Shift Preference' Tab.", false);
		
		return false;
	}
	
	@Override
	public void clickOnShiftPreferenceCollapsibleWindowHeader() throws Exception
	{
		if(isElementLoaded(collapsibleWorkPreferenceSection)) {
			WebElement collapsibleRowDiv = collapsibleWorkPreferenceSection.findElement(By.cssSelector("div.collapsible-title"));
			if(isElementLoaded(collapsibleRowDiv)) {
				click(collapsibleRowDiv);
				SimpleUtils.pass("Profile Page: Collapsible Work Preference window Header clicked under 'My Shift Preference' Tab.");
			}
			else 
				SimpleUtils.fail("Profile Page: Collapsible Work Preference window not loaded under 'My Shift Preference' Tab.", true);
		}
		else 
			SimpleUtils.fail("Profile Page: Collapsible Work Preference Section not loaded under 'My Shift Preference' Tab.", false);
	}
	
	
	@Override
	public HashMap<String, String> getMyShiftPreferenceData() throws Exception
	{
		HashMap<String, String> shiftPreferenceData = new HashMap<String, String>();
		if(isElementLoaded(myAvailabilitySection, 10)) {
			List<WebElement> myShiftPreferenceDataLabels = myAvailabilitySection.findElements(By.cssSelector("div.quick-schedule-preference.label"));
			List<WebElement> myShiftPreferenceDataValues = myAvailabilitySection.findElements(By.cssSelector("div.quick-schedule-preference.value"));
			if(myShiftPreferenceDataLabels.size() > 0 && myShiftPreferenceDataLabels.size() == myShiftPreferenceDataValues.size()) {
				for(int index = 0; index < myShiftPreferenceDataLabels.size(); index++) {
					if(myShiftPreferenceDataLabels.get(index).isDisplayed()) {
						if(myShiftPreferenceDataLabels.get(index).getText().toLowerCase().contains("hours/shift")) {
							shiftPreferenceData.put("hoursPerShift", myShiftPreferenceDataValues.get(index).getText());
						}
						else if(myShiftPreferenceDataLabels.get(index).getText().toLowerCase().contains("hours/wk")) {
							shiftPreferenceData.put("hoursPerWeek", myShiftPreferenceDataValues.get(index).getText());
						}
						else if(myShiftPreferenceDataLabels.get(index).getText().toLowerCase().contains("shifts/wk")) {
							shiftPreferenceData.put("shiftsPerWeek", myShiftPreferenceDataValues.get(index).getText());
						}
					}
				}
				
				// Get Enterprise preferred Hours
				if(isElementLoaded(commitedHoursDiv)) {
					String[] preferredHoursText = commitedHoursDiv.getText().split(":");
					if(preferredHoursText.length > 0)
						shiftPreferenceData.put("enterprisePreferredHours",preferredHoursText[1]);
					else
						SimpleUtils.fail("Profile Page: Enterprise Preferred Hours not loaded under ' My Shift Preferences' Section.", true);
				}
				
				//
				if(showPreferenceOptionsDiv.size() > 0) {
					for(WebElement preferenceOptions : showPreferenceOptionsDiv) {
						if(preferenceOptions.isDisplayed() && preferenceOptions.isEnabled()) {
							String[] preferenceOptionsText = preferenceOptions.getText().split("\n");
							if(preferenceOptionsText.length > 0) {
								for(String preferenceOptionsTextLine : preferenceOptionsText) {
									if(preferenceOptionsTextLine.toLowerCase().contains("volunteer for additional work") || preferenceOptionsTextLine.toLowerCase().contains("voluntary standby list")) {
										String[] volunteerOptionText = preferenceOptionsTextLine.split(":");
										if(volunteerOptionText.length > 1) {
											shiftPreferenceData.put("volunteerForAdditionalWork",volunteerOptionText[1]);
										}
										else 
											SimpleUtils.fail("Profile Page: Volunteer for additional work not loaded under ' My Shift Preferences' Section.", true);	
									}
									
									if(preferenceOptionsTextLine.toLowerCase().contains("other preferred locations")) {
										String[] otherPreferredLocationsOptionText = preferenceOptionsTextLine.split(":");
										if(otherPreferredLocationsOptionText.length > 1) {
											shiftPreferenceData.put("otherPreferredLocations",otherPreferredLocationsOptionText[1].split(" ")[1]);
										}
										else 
											SimpleUtils.fail("Profile Page: 'Other preferred locations' not loaded under ' My Shift Preferences' Section.", true);	
									}
								}
							}
						}
					}	
				}
				else 
					SimpleUtils.fail("Profile Page: 'Volunteer for additional work' and 'Other preferred locations' not loaded under ' My Shift Preferences' Section.", true);
				
				
			}
		}
		return shiftPreferenceData;
	}

	
	@Override
	public ArrayList<String> getUserProfileBadgesDetails() throws Exception {
		ArrayList<String> badgesText = new ArrayList<String>();
		if(isElementLoaded(profileBadgesRow)) {
			SimpleUtils.pass("Profile Page: Badges Section loaded under 'About Me' Tab.");
			if(badgesDiv.size() > 0) {
				for(WebElement badge : badgesDiv) {
					badgesText.add(badge.getAttribute("data-original-title"));
				}
			}
			else
				SimpleUtils.report("Profile Page: Badges Section 'No badges' found under 'About Me' Tab.");
		}
		else {
			if(isElementLoaded(noBadgesDiv))
				SimpleUtils.report("Profile Page: Badges Section 'No badges' found under 'About Me' Tab.");
			else
				SimpleUtils.fail("Profile Page: Badges Section not loaded under 'About Me' Tab.", false);
		}
		return badgesText;
	}

	@FindBy(css = ".information-section.badge-section")
	private WebElement badgeSectionInProfilePage;
	@FindBy(css = ".lg-badges-badge")
	private List<WebElement> badgeList;
	@Override
	public ArrayList<String> getUserBadgesDetailsFromProfilePage() throws Exception {
		ArrayList<String> badgesText = new ArrayList<String>();
		if(isElementLoaded(badgeSectionInProfilePage, 10)) {
			if (badgeList.size()>0){
				for (WebElement element: badgeList){
					badgesText.add(element.getAttribute("popover-title"));
				}
			}
		}
		return badgesText;
	}
	
	
	public void updateReceivesShiftOffersForOtherLocationCheckButton(boolean isOfferForOtherLocation) throws Exception 
	{
		if(isElementLoaded(otherLocationCheckButton)) {
			if(isOfferForOtherLocation && otherLocationCheckButton.getAttribute("class").contains("enable"))
				SimpleUtils.pass("Profile Page: 'Receives Shift Offers for other locations' already enabled.");
			else if(!isOfferForOtherLocation && otherLocationCheckButton.getAttribute("class").contains("disabled"))
				SimpleUtils.pass("Profile Page: 'Receives Shift Offers for other locations' already Disabled.");
			else if(! isOfferForOtherLocation && otherLocationCheckButton.getAttribute("class").contains("enable")) {
				click(otherLocationCheckButton);
				SimpleUtils.pass("Profile Page: 'Receives Shift Offers for other locations' Disabled successfully.");
			}
			else {
				click(otherLocationCheckButton);
				SimpleUtils.pass("Profile Page: 'Receives Shift Offers for other locations' Enabled successfully.");
			}
	    }
		else
			SimpleUtils.fail("Profile Page: 'Receives Shift Offers for other locations' CheckBox not loaded.", false);
	}
	
	
	
	public void updateVolunteersForAdditionalWorkCheckButton(boolean isOfferForOtherLocation) throws Exception 
	{
		if(isElementLoaded(volunteerMoreHoursCheckButton)) {
			if(isOfferForOtherLocation && volunteerMoreHoursCheckButton.getAttribute("class").contains("enable"))
				SimpleUtils.pass("Profile Page: 'Volunteers for Additional Work' CheckBox already enabled.");
			else if(!isOfferForOtherLocation && volunteerMoreHoursCheckButton.getAttribute("class").contains("disabled"))
				SimpleUtils.pass("Profile Page: 'Volunteers for Additional Work' CheckBox already Disabled.");
			else if(! isOfferForOtherLocation && volunteerMoreHoursCheckButton.getAttribute("class").contains("enable")) {
				click(volunteerMoreHoursCheckButton);
				// Verify if "Agree" button loaded
				if (isElementLoaded(OKButton, 5)) {
					clickTheElement(OKButton);
				}
				SimpleUtils.pass("Profile Page: 'Volunteers for Additional Work' CheckBox Disabled successfully.");
			}
			else {
				click(volunteerMoreHoursCheckButton);
				// Verify if "Agree" button loaded
				if (isElementLoaded(OKButton, 5)) {
					clickTheElement(OKButton);
				}
				SimpleUtils.pass("Profile Page: 'Volunteers for Additional Work' CheckBox Enabled successfully.");
			}
		}
		else
			SimpleUtils.fail("Profile Page: 'Volunteers for Additional Work' CheckBox not loaded.", false);
	}
	
	
	public void updateMyShiftPreferencesAvailabilitySliders(int minCount, int maxCount, String sliderType) throws Exception
	{
		int minPreferenceCount = 0;
		int maxPreferenceCount = 0;
		if(availabilitySliders.size() > 0) {
			for(WebElement availabilitySlider : availabilitySliders) {
				if(availabilitySlider.getText().toLowerCase().contains(sliderType.toLowerCase())) {
					WebElement minSlider = availabilitySlider.findElement(By.cssSelector("span[ng-style=\"minPointerStyle\"]"));
					WebElement maxSlider = availabilitySlider.findElement(By.cssSelector("span[ng-style=\"maxPointerStyle\"]"));
					WebElement editPrefValuesDiv = availabilitySlider.findElement(By.cssSelector("div.edit-pref-values"));
					if(isElementLoaded(editPrefValuesDiv)) {
						String[] editPrefValuesDivText = editPrefValuesDiv.getText().split("-");
						if(editPrefValuesDivText.length > 1) {
							minPreferenceCount = Integer.valueOf(editPrefValuesDivText[0].trim());
							maxPreferenceCount = Integer.valueOf(editPrefValuesDivText[1].trim());
						}
					}
					
					// Update Min Slider
					if(isElementLoaded(minSlider)) {
						int sliderOffSet = 5;
						if(minCount < minPreferenceCount)
							sliderOffSet = -5;
						while(Integer.valueOf(editPrefValuesDiv.getText().split("-")[0].trim()) != minCount)
						{
							String minMaxSlidersValue = editPrefValuesDiv.getText();
							moveElement(minSlider, sliderOffSet);
							if(minMaxSlidersValue.equals(editPrefValuesDiv.getText())){
								SimpleUtils.fail("Profile Page: 'Min Slider' for "+sliderType+" not moving.", true);
								break;
							}
						}
					}
					else
						SimpleUtils.fail("Profile Page: 'Min Slider' for "+sliderType+" not loaded.", false);
					
					// Update Max Slider
					if(isElementLoaded(maxSlider)) {
						int sliderOffSet = 5;
						if(maxCount < maxPreferenceCount)
							sliderOffSet = -5;
						while(Integer.valueOf(editPrefValuesDiv.getText().split("-")[1].trim()) != maxCount)
						{
							String maxMaxSlidersValue = editPrefValuesDiv.getText();
							moveElement(maxSlider, sliderOffSet);
							if(maxMaxSlidersValue.equals(editPrefValuesDiv.getText())){
								SimpleUtils.fail("Profile Page: 'Max Slider' for "+sliderType+" not moving.", true);
								break;
							}
						}
					}
					else
						SimpleUtils.fail("Profile Page: 'Max Slider' for "+sliderType+" not loaded.", false);
					break;
				}
				
			}
		}
		else
			SimpleUtils.fail("Profile Page: Edit My Shift Preferences - Availability Sliders not loaded.", false);
	}
	
	public void moveElement(WebElement webElement, int xOffSet)
	{
		Actions builder = new Actions(MyThreadLocal.getDriver());
		builder.moveToElement(webElement)
	         .clickAndHold()
	         .moveByOffset(xOffSet, 0)
	         .release()
	         .build()
	         .perform();
	}
			
	@Override
	public void updateMyShiftPreferenceData(boolean canReceiveOfferFromOtherLocation, boolean isVolunteersForAdditional, int minHoursPerShift,
			int maxHoursPerShift, int minShiftLength, int maxShiftLength, int minShiftsPerWeek, int maxShiftsPerWeek) throws Exception
	{
		boolean isShiftPreferenceWindowOpen = isShiftPreferenceCollapsibleWindowOpen();
	      if(! isShiftPreferenceWindowOpen)
	    	  clickOnShiftPreferenceCollapsibleWindowHeader();
	      
	      clickOnEditMyShiftPreferencePencilIcon();
	      if(isMyShiftPreferenceEditContainerLoaded()) {
	    	  SimpleUtils.pass("Profile Page: 'My Shift Preference' edit Container loaded successfully.");
	    	  updateReceivesShiftOffersForOtherLocationCheckButton(canReceiveOfferFromOtherLocation);
	    	  updateVolunteersForAdditionalWorkCheckButton(isVolunteersForAdditional);
	    	  
	    	  updateMyShiftPreferencesAvailabilitySliders(minHoursPerShift, maxHoursPerShift, "Hours per week");
	    	  updateMyShiftPreferencesAvailabilitySliders(minShiftLength, maxShiftLength, "Shift length");
	    	  updateMyShiftPreferencesAvailabilitySliders(minShiftsPerWeek, maxShiftsPerWeek, "Shifts per week");
	    	  saveMyShiftPreferencesData();
	      }
	      else 
	    	  SimpleUtils.fail("Profile Page: 'My Shift Preference' edit Container not loaded.", false);
	    	  
	}

	
	private void saveMyShiftPreferencesData() throws Exception {
		if(isElementLoaded(savePreferencesBtn)) {
			click(savePreferencesBtn);
			 if(! isMyShiftPreferenceEditContainerLoaded()) {
		    	  SimpleUtils.pass("Profile Page: 'My Shift Preference' data saved successfully.");
			 }
			 else
				 SimpleUtils.pass("Profile Page: Unable to save 'My Shift Preference' data.");
		}
		else
			SimpleUtils.fail("Profile Page: 'My Shift Preference' edit container 'Save' button not loaded.", false);
	}

	public void clickOnEditMyShiftPreferencePencilIcon()  throws Exception {
		// 
		if(isElementLoaded(collapsibleWorkPreferenceSection)) {
			//WebElement collapsibleRowDiv = collapsibleWorkPreferenceSection.findElement(By.cssSelector("div.collapsible-title"));
			WebElement workPreferenceEditButton = collapsibleWorkPreferenceSection.findElement(By.cssSelector("a[ng-click=\"callEdit($event)\"]"));
			if(isElementLoaded(workPreferenceEditButton)) {
				click(workPreferenceEditButton);
			}
			else 
				SimpleUtils.fail("Profile Page: 'Edit Pencil Icon' not loaded under 'My Shift Preference' Header.", false);
		}
		else 
			SimpleUtils.fail("Profile Page: Collapsible Work Preference Section not loaded under 'My Shift Preference' Tab.", false);
	}
	
	
	
	@FindBy(css="span[ng-if=\"canApprove(r)\"]")
	private WebElement timeOffRequestApproveBtn;
	public boolean isMyShiftPreferenceEditContainerLoaded() throws Exception
	{
		if(isElementLoaded(ditAvailabilityContainerDiv) && ditAvailabilityContainerDiv.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	
	@FindBy(css="collapsible[identifier=\"'availability'\"]")
	private WebElement collapsibleAvailabilitySection;
	@Override
	public boolean isMyAvailabilityCollapsibleWindowOpen() throws Exception
	{
		if(isElementLoaded(collapsibleAvailabilitySection)) {
			WebElement collapsibleRowDiv = collapsibleAvailabilitySection.findElement(By.cssSelector("div.collapsible-title"));
			if(isElementLoaded(collapsibleRowDiv)) {
				if(collapsibleRowDiv.getAttribute("class").contains("open"))
					return true;
			}
			else 
				SimpleUtils.fail("Profile Page: Collapsible 'availability' window not loaded under 'My Work Preference' Tab.", true);
		}
		else 
			SimpleUtils.fail("Profile Page: Collapsible 'availability' Section not loaded under 'My Work Preference' Tab.", false);
		
		return false;
	}
	
	@Override
	public void clickOnMyAvailabilityCollapsibleWindowHeader() throws Exception
	{
		if(isElementLoaded(collapsibleAvailabilitySection)) {
			WebElement collapsibleRowDiv = collapsibleAvailabilitySection.findElement(By.cssSelector("div.collapsible-title"));
			if(isElementLoaded(collapsibleRowDiv)) {
				click(collapsibleRowDiv);
				SimpleUtils.pass("Profile Page: Collapsible Availability window Header clicked under 'My Work Preference' Tab.");
			}
			else 
				SimpleUtils.fail("Profile Page: Collapsible Availability window not loaded under 'My Work Preference' Tab.", true);
		}
		else 
			SimpleUtils.fail("Profile Page: Collapsible Availability Section not loaded under 'My Work Preference' Tab.", false);
	}
 
	@Override
	public HashMap<String, Object> getMyAvailabilityData() throws Exception {
		scrollToBottom();
		HashMap<String, Object> myAvailabilityData = new HashMap<String, Object>();
	      float scheduleHoursValue = 0;
	      float remainingHoursValue = 0;
	      float totalHoursValue = 0;
	      int shiftsCount = 0;
	      String activeWeekText = "";
	      if(isElementLoaded(sheduleHoursData)) {
	    	  scheduleHoursValue = Float.valueOf(sheduleHoursData.getText().replace("(", "").replace(")", ""));
	      }
	      
	      if(isElementLoaded(activeWeek)) {
	    	  activeWeekText = activeWeek.getText();
	      }
	      
	      if(isElementLoaded(remainingHours)) {
	    	  remainingHoursValue = Float.valueOf(remainingHours.getText());
	      }
	      
	      if(isElementLoaded(totalHours)) {
	    	  totalHoursValue = Float.valueOf(totalHours.getText());
	      }
	      
	      shiftsCount = availableShifts.size();
	      
	      
	     // }
	      //else 
	    	//  SimpleUtils.fail("Profile Page: 'My Availability' edit Container not loaded.", false);
	      
	      myAvailabilityData.put("scheduleHoursValue", scheduleHoursValue);
	      myAvailabilityData.put("remainingHoursValue", remainingHoursValue);
	      myAvailabilityData.put("totalHoursValue", totalHoursValue);
		  myAvailabilityData.put("shiftsCount", shiftsCount);
		  myAvailabilityData.put("activeWeekText", activeWeekText);
		return myAvailabilityData;
	}
	
	@Override
	public boolean isMyAvailabilityLocked() throws Exception
	{
		if(isElementLoaded(collapsibleAvailabilitySection)) {
			WebElement collapsibleRowDiv = collapsibleAvailabilitySection.findElement(By.cssSelector("div.collapsible-title"));
			if(isElementLoaded(collapsibleRowDiv)) {
				WebElement myAvailabilityHeaderLockBtn = collapsibleAvailabilitySection.findElement(
						By.cssSelector("a[ng-click=\"sideIconClicked($event)\"]"));
				if(isElementLoaded(myAvailabilityHeaderLockBtn) && myAvailabilityHeaderLockBtn.isDisplayed())
					return true;
			}
			else 
				SimpleUtils.fail("Profile Page: Collapsible 'availability' window not loaded under 'My Work Preference' Tab.", true);
		}
		return false;
	}

	//added by Haya
	@FindBy(css="user-profile-section[editing-locked]")
	private WebElement myAvailability;
	@FindBy(css=".user-profile-section__lock")
	private WebElement lockIcon;
	@FindBy(css="user-profile-section[editing-locked] div[class=\"user-profile-section__header\"] lg-button[label=\"Edit\"] span.ng-binding")
	private WebElement editBtn;

	@Override
	public boolean isMyAvailabilityLockedNewUI() throws Exception
	{
		if(isElementLoaded(myAvailability,15)) {
			waitForSeconds(5);
			if (isElementLoaded(lockIcon, 5)){
				return true;
			}
		}else{
			SimpleUtils.fail("My Availability section not loaded under 'My Work Preference' Tab.", false);
		}
		return false;
	}

	//added by Haya
	//return new available hours.
	@Override
	public String updateMyAvailability(String hoursType, int sliderIndex,
									   String leftOrRightSliderArrow, double durationhours, String repeatChanges) throws Exception
	{
		String result = "";
		if (isElementLoaded(editBtn,30)){
			clickTheElement(editBtn);
			updatePreferredOrBusyHoursDurationNew(sliderIndex,durationhours,leftOrRightSliderArrow, hoursType);
			result = getAvailableHoursForSpecificWeek();
			saveMyAvailabilityEditMode(repeatChanges);
		}else{
			SimpleUtils.fail("Edit button is not loaded!", false);
		}
		return result;
	}

	@FindBy(css = ".availability-box.availability-box-ghost")
	List<WebElement> availabilityGrid;

	//Haya: the old method updatePreferredOrBusyHoursDuration has problem with xOffSet. So add copied one and update it.
	private void updatePreferredOrBusyHoursDurationNew(int rowIndex, double durationhours, String leftOrRightDuration, String hoursType) throws Exception {
		String preferredHoursTabText = "When I prefer to work";
		String busyHoursTabText = "When I prefer not to work";
		if(hoursType.toLowerCase().contains(preferredHoursTabText.toLowerCase()))
			selectMyAvaliabilityEditHoursTabByLabel(preferredHoursTabText);
		else
			selectMyAvaliabilityEditHoursTabByLabel(busyHoursTabText);

		int xOffSet = (int)(durationhours *  40);
		if(leftOrRightDuration.toLowerCase().contains("right")) {
			if(hourCellsResizableCursorsRight.size() > rowIndex) {
				scrollToElement(hourCellsResizableCursorsRight.get(rowIndex));
				moveElement(hourCellsResizableCursorsRight.get(rowIndex), xOffSet);
				SimpleUtils.pass("My Availability Edit Mode - '"+hoursType+"' Hours Row updated with index - '"+rowIndex+"'.");
			} else if (areListElementVisible(availabilityGrid, 10) && availabilityGrid.get(rowIndex).findElements(By.cssSelector(".hour-cell.hour-cell-ghost")).size()==48) {
				click(availabilityGrid.get(rowIndex).findElements(By.cssSelector(".hour-cell.hour-cell-ghost")).get(23));
			} else{
					SimpleUtils.fail("My Availability Edit Mode - '"+hoursType+"' Hours Row not loaded with index - '"+rowIndex+"'.", false);
			}
		} else {
			if(hourCellsResizableCursorsLeft.size() > rowIndex) {
				moveElement(hourCellsResizableCursorsLeft.get(rowIndex), xOffSet);
				SimpleUtils.pass("My Availability Edit Mode - '"+hoursType+"' Hours Row updated with index - '"+rowIndex+"'.");
			} else if (areListElementVisible(availabilityGrid, 10) && availabilityGrid.get(rowIndex).findElements(By.cssSelector(".hour-cell.hour-cell-ghost")).size()==48) {
				click(availabilityGrid.get(rowIndex).findElements(By.cssSelector(".hour-cell.hour-cell-ghost")).get(23));
			} else{
				SimpleUtils.fail("My Availability Edit Mode - '"+hoursType+"' Hours Row not loaded with index - '"+rowIndex+"'.", false);
			}
		}
	}

	@Override
	public ArrayList<HashMap<String, ArrayList<String>>> getMyAvailabilityPreferredAndBusyHours() {
		ArrayList<HashMap<String, ArrayList<String>>> result = new ArrayList<HashMap<String, ArrayList<String>>>();
		if(myAvailabilityDayOfWeekRows.size() > 0) {
			for(WebElement myAvailabilityDayOfWeekRow :myAvailabilityDayOfWeekRows) {
				WebElement rowDowLabelDiv = myAvailabilityDayOfWeekRow.findElement(By.cssSelector("div.dow-label")); 
				HashMap<String, ArrayList<String>> preferredAndBusyDuration = new HashMap<String, ArrayList<String>>();
				List<WebElement> preferredHoursDurations = myAvailabilityDayOfWeekRow.findElements(
						By.cssSelector("div.availability-zone.green-zone"));
				List<WebElement> busyHoursDurations = myAvailabilityDayOfWeekRow.findElements(
						By.cssSelector("div.availability-zone.red-zone"));
				
				ArrayList<String> preferredDuration = new ArrayList<String>();
				for(WebElement preferredHoursDuration :preferredHoursDurations) {
					preferredDuration.add(preferredHoursDuration.getAttribute("innerText"));
				}
				
				ArrayList<String> busyDuration = new ArrayList<String>();
				for(WebElement busyHoursDuration :busyHoursDurations) {
					busyDuration.add(busyHoursDuration.getAttribute("innerText"));
				}
				preferredAndBusyDuration.put("preferredDuration", preferredDuration);
				preferredAndBusyDuration.put("busyDuration", busyDuration);
				result.add(preferredAndBusyDuration);
			}
		}
		else
			SimpleUtils.fail("Profile Page: 'My Availability section' Day of Week Rows not loaded.", true);
		return result;
	}

	
	@Override
	public void updateLockedAvailabilityPreferredOrBusyHoursSlider(String hoursType, int sliderIndex, String leftOrRightSliderArrow,
			int durationMinutes, String repeatChanges) throws Exception {
		if(isMyAvailabilityLocked()) {
			if(isElementLoaded(collapsibleAvailabilitySection)) {
				WebElement collapsibleRowDiv = collapsibleAvailabilitySection.findElement(By.cssSelector("div.collapsible-title"));
				if(isElementLoaded(collapsibleRowDiv)) {
					WebElement myAvailabilityHeaderLockBtn = collapsibleAvailabilitySection.findElement(
							By.cssSelector("a[ng-click=\"sideIconClicked($event)\"]"));
					if(isElementLoaded(myAvailabilityHeaderLockBtn) && myAvailabilityHeaderLockBtn.isDisplayed()) {
						click(myAvailabilityHeaderLockBtn);
						if(isElementLoaded(myAvailabilityUnLockBtn)) {
							click(myAvailabilityUnLockBtn);
							if(isElementLoaded(myAvailabilityUnLockBtn)) {
								click(myAvailabilityUnLockBtn);
								updatePreferredOrBusyHoursDuration(sliderIndex,durationMinutes,leftOrRightSliderArrow, hoursType);
								saveMyAvailabilityEditMode(repeatChanges);
							}
						}
					}
				}
			}
		}
		else {
			SimpleUtils.fail("Profile Page: 'My Availability section' not locked for active week:'"
					+getMyAvailabilityData().get("activeWeek")+"'.", true);
		}
	}
	
	//updated by Haya
	public void saveMyAvailabilityEditMode(String availabilityChangesRepeat ) throws Exception {
		if(isElementLoaded(myAvailabilityEditModeSaveBtn)) {
			click(myAvailabilityEditModeSaveBtn);
			if(availabilityChangesRepeat.toLowerCase().contains("repeat forward")) {
				if(isElementLoaded(MyAvailabilityEditSaveRepeatForwordBtn, 10)){
					moveToElementAndClick(MyAvailabilityEditSaveRepeatForwordBtn);
					clickTheElement(myAvailabilityConfirmSubmitBtn);
				}
			} else {
				if(isElementLoaded(MyAvailabilityEditSaveThisWeekOnlyBtn, 10)){
					moveToElementAndClick(MyAvailabilityEditSaveThisWeekOnlyBtn);
					clickTheElement(myAvailabilityConfirmSubmitBtn);
				}
			}
			waitForSeconds(10);
			if(isElementLoaded(editBtn, 25)||isElementLoaded(lockIcon, 10))
				SimpleUtils.pass("Profile Page: 'My Availability section' edit mode Saved successfully.");
			else
				SimpleUtils.fail("Profile Page: 'My Availability section' edit mode not Saved.", false);
		} else{
			SimpleUtils.fail("Profile Page: 'My Availability section' edit mode 'save' button not loaded.", true);
		}
	}


	private void updatePreferredOrBusyHoursDuration(int rowIndex, int durationMinutes, String leftOrRightDuration, String hoursType) throws Exception {
		String preferredHoursTabText = "When I prefer to work";
		String busyHoursTabText = "When I prefer not to work";
		if(hoursType.toLowerCase().contains(preferredHoursTabText.toLowerCase()))
			selectMyAvaliabilityEditHoursTabByLabel(preferredHoursTabText);
		else
			selectMyAvaliabilityEditHoursTabByLabel(busyHoursTabText);
		
		int xOffSet = ((durationMinutes / 60) * 100) / 2;
		if(leftOrRightDuration.toLowerCase().contains("right")) {
			if(hourCellsResizableCursorsRight.size() > rowIndex) {
				scrollToElement(hourCellsResizableCursorsRight.get(rowIndex));
				moveElement(hourCellsResizableCursorsRight.get(rowIndex), xOffSet);
				SimpleUtils.pass("My Availability Edit Mode - '"+hoursType+"' Hours Row updated with index - '"+rowIndex+"'.");
			}
			else {
				SimpleUtils.fail("My Availability Edit Mode - '"+hoursType+"' Hours Row not loaded with index - '"+rowIndex+"'.", false);
			}
		}
		else {
			if(hourCellsResizableCursorsLeft.size() > rowIndex) {
				moveElement(hourCellsResizableCursorsLeft.get(rowIndex), xOffSet);
				SimpleUtils.pass("My Availability Edit Mode - '"+hoursType+"' Hours Row updated with index - '"+rowIndex+"'.");
			}
			else {
				SimpleUtils.fail("My Availability Edit Mode - '"+hoursType+"' Hours Row not loaded with index - '"+rowIndex+"'.", false);
			}
		}
	}
	
	
	public void selectMyAvaliabilityEditHoursTabByLabel(String tabLabel) throws Exception
	{
		boolean isTabFound = false;
		if(isElementLoaded(myAvailabilityEditModeHeader, 15)) {
			List<WebElement> myAvailabilityHoursTabs = myAvailabilityEditModeHeader.findElements(
					By.cssSelector("div[ng-click=\"selectTab($event, t)\"]"));
			if(myAvailabilityHoursTabs.size() > 0) {
				for(WebElement myAvailabilityHoursTab : myAvailabilityHoursTabs) {
					if(myAvailabilityHoursTab.getText().toLowerCase().contains(tabLabel.toLowerCase())) {
						isTabFound = true;
						if(myAvailabilityHoursTab.getAttribute("class").contains("select")) {
							SimpleUtils.pass("My Availability Edit Mode - Hours Tab '"+tabLabel+"' already active.");
						}
						else {
							click(myAvailabilityHoursTab);
							SimpleUtils.pass("My Availability Edit Mode - Hours Tab '"+tabLabel+"' selected successfully.");
						}
					}
				}
				if(! isTabFound)
					SimpleUtils.fail("My Availability Edit Mode Hours Tabs '"+tabLabel+"' not found.", false);
			}
			else
				SimpleUtils.fail("My Availability Edit Mode Hours Tabs not loaded.", false);
		}
		else 
			SimpleUtils.fail("My Availability Edit Mode not loaded successfully.", false);
	}
	
	@Override
	public HashMap<String, Integer> getTimeOffRequestsStatusCount() throws Exception{
		HashMap<String, Integer> timeOffRequestsStatusCount = new HashMap<String, Integer>();
		if(isElementLoaded(pendingTimeOffCountDiv)) {
			WebElement pendingBlockCounter = pendingTimeOffCountDiv.findElement(By.cssSelector("span.count-block-counter"));
			if(isElementLoaded(pendingBlockCounter))
				timeOffRequestsStatusCount.put("pendingRequestCount", Integer.valueOf(pendingBlockCounter.getText()));
		}
		
		if(isElementLoaded(approvedTimeOffCountDiv)) {
			WebElement approvedBlockCounter = approvedTimeOffCountDiv.findElement(By.cssSelector("span.count-block-counter"));
			if(isElementLoaded(approvedBlockCounter))
				timeOffRequestsStatusCount.put("approvedRequestCount", Integer.valueOf(approvedBlockCounter.getText()));
		}
		
		if(isElementLoaded(rejectedTimeOffCountDiv)) {
			WebElement rejectedBlockCounter = rejectedTimeOffCountDiv.findElement(By.cssSelector("span.count-block-counter"));
			if(isElementLoaded(rejectedBlockCounter))
				timeOffRequestsStatusCount.put("rejectedRequestCount", Integer.valueOf(rejectedBlockCounter.getText()));
		}
		return timeOffRequestsStatusCount;
	}

	// Added by Nora: Time Off
	@FindBy (className = "modal-content")
	private WebElement newTimeOffWindow;
	@FindBy (css = "[checked=\"options.fullDay\"] .lgnCheckBox")
	private List<WebElement> allDayCheckboxes;
	@FindBy (css = "button.btn-sm")
	private List<WebElement> smButtons;
	@FindBy (className = "ranged-calendar__month-name")
	private List<WebElement> calendarMonthNames;
	@FindBy (css = "div.is-today")
	private WebElement todayOnCalendar;
//	@FindBy (className = "header-user-switch-menu-item")
//	private List<WebElement> profileSubPageLabels;
    @FindBy (css = "div.header-user-switch-menu-secondary-item.ng-binding")
    private List<WebElement> profileSubPageLabels;
	@FindBy (css = ".header-user-switch-menu")
	private WebElement profileSwitchMenu;
	@FindBy (css = "div.in-range")
	private List<WebElement> selectedDates;
	@FindBy (css = "b.day-selected")
	private List<WebElement> startNEndDates;
	@FindBy (css = "[options=\"startOptions\"] [selected=\"selected\"]")
	private List<WebElement> startTimes;
	@FindBy (css = "[options*=\"end\"] [selected=\"selected\"]")
	private List<WebElement> endTimes;
	@FindBy (css = "span.text-blue")
	private List<WebElement> startNEndTimes;
	@FindBy (className = "count-block-label")
	private List<WebElement> timeOffStatus;

	@Override
	public void verifyTimeIsCorrectAfterDeSelectAllDay() throws Exception {
		String expectedStartTime = "10:00 AM";
		String expectedEndTime = "3:00 PM";
		List<String> selectedStartNEndTimes = getSelectedStartNEndTime();
		if (selectedStartNEndTimes.size() == 0) {
			SimpleUtils.fail("Failed to get the selected start and End time!", false);
		}
		String actualStartTime = selectedStartNEndTimes.get(0);
		String actualEndTime = selectedStartNEndTimes.get(1);

		if (expectedStartTime.equals(actualStartTime) && expectedEndTime.equals(actualEndTime)) {
			SimpleUtils.pass("Start and End time are correct!");
		}else {
			SimpleUtils.fail("Start and End time are incorrect!", true);
		}
	}

	public List<String> getSelectedStartNEndTime() throws Exception {
		List<String> startNEndTime = new ArrayList<>();
		String expectedStartTime = null;
		String expectedEndTime = null;
		if (areListElementVisible(startTimes, 5) && areListElementVisible(endTimes, 5) && areListElementVisible(smButtons, 5)
				&& startTimes.size() == 2 && endTimes.size() == 2 && smButtons.size() == 4) {
			expectedStartTime = startTimes.get(0).getText() + ":" + (startTimes.get(1).getText().length() == 1 ?
					startTimes.get(1).getText() + "0" : startTimes.get(1).getText());
			expectedEndTime = endTimes.get(0).getText() + ":" + (endTimes.get(1).getText().length() == 1 ?
					endTimes.get(1).getText() + "0" : endTimes.get(1).getText());
			List<String> amOrPM = new ArrayList<>();
			for (WebElement smButton : smButtons) {
				if (smButton.getAttribute("class").contains("isActive")) {
					amOrPM.add(smButton.getText());
				}
			}
			if (amOrPM.size() == 2) {
				expectedStartTime = expectedStartTime + " " + amOrPM.get(0);
				expectedEndTime = expectedEndTime + " " + amOrPM.get(1);
			}
		}else {
			SimpleUtils.fail("Selected start and end time failed to load!", true);
		}
		startNEndTime.add(expectedStartTime);
		startNEndTime.add(expectedEndTime);
		return startNEndTime;
	}

	@Override
	public boolean isNewTimeOffWindowLoaded() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(newTimeOffWindow, 5)) {
			SimpleUtils.pass("New Time Off Request Window loaded successfully!");
			isLoaded = true;
		}else {
			SimpleUtils.fail("New Time Off Request Window failed to load!", false);
		}
		return isLoaded;
	}

	@Override
	public void verifyCalendarForCurrentAndNextMonthArePresent(String currentMonthYearDate) throws Exception {
		String currentYear = null;
		String currentMonth = null;
		String currentDate = null;
		if (currentMonthYearDate.contains(" ")) {
			List<String> yearMonthDate = Arrays.asList(currentMonthYearDate.split(" "));
			if (yearMonthDate.size() == 3) {
				currentMonth = yearMonthDate.get(0);
				currentYear = yearMonthDate.get(1);
				currentDate = yearMonthDate.get(2);
			}
		}
		if (areListElementVisible(calendarMonthNames, 5) && calendarMonthNames.size() == 2) {
			String currentMonthOnCalendar = calendarMonthNames.get(0).getText();
			String nextMonthOnCalendar = calendarMonthNames.get(1).getText();
			String expectedNextMonth = SimpleUtils.getNextMonthAndYearFromCurrentMonth(currentMonthOnCalendar);
			// Verify the current Month is correct on calendar
			if (currentMonthOnCalendar.contains(currentYear) && currentMonthOnCalendar.contains(currentMonth)) {
				SimpleUtils.pass("Current Month is loaded properly!");
			}else {
				SimpleUtils.fail("Current month is incorrect!", true);
			}
			// Verify next month on calendar is correct
			if (nextMonthOnCalendar.equals(expectedNextMonth)) {
				SimpleUtils.pass("Next Month is loaded properly!");
			}else {
				SimpleUtils.fail("Next month is incorrect, expected is: " + expectedNextMonth, true);
			}
		}else {
			SimpleUtils.fail("Two calendars failed to load!", true);
		}
		// Verify the current day is loaded
		if (isElementLoaded(todayOnCalendar, 5)) {
			if (Integer.parseInt(todayOnCalendar.getText()) == Integer.parseInt(currentDate)) {
				SimpleUtils.pass("Today is correct and today is: " + currentDate);
			}else {
				SimpleUtils.fail("Today: " + todayOnCalendar.getText() + " is incorrect, expected is: " + currentDate, true);
			}
		}else {
			SimpleUtils.fail("Current Day failed to load!", true);
		}
	}

	@Override
	public List<String> selectStartAndEndDate() throws Exception {
		List<String> startNEndDates = new ArrayList<>();
		selectDate(10);
		selectDate(15);
		HashMap<String, String> timeOffDate = getTimeOffDate(10, 15);
		String timeOffStartDate = timeOffDate.get("startDateTimeOff");
		String timeOffEndDate = timeOffDate.get("endDateTimeOff");
		setTimeOffStartTime(timeOffStartDate);
		setTimeOffEndTime(timeOffEndDate);
		HashMap<String, String> timeOffDateWithYear = getTimeOffDateWithYear(10, 15);
		String timeOffStartDateWithYear = timeOffDateWithYear.get("startDateWithYearTimeOff");
		String timeOffEndDateWithYear = timeOffDateWithYear.get("endDateWithYearTimeOff");
		startNEndDates.add(timeOffStartDateWithYear);
		startNEndDates.add(timeOffEndDateWithYear);
		return startNEndDates;
	}

	public List<String> selectStartAndEndDate(int daysInadvance, int startDays, int endDays) throws Exception {
		List<String> startNEndDates = new ArrayList<>();
		selectDate(daysInadvance+startDays);
		selectDate(daysInadvance+endDays);
		HashMap<String, String> timeOffDate = getTimeOffDate(daysInadvance+startDays, daysInadvance+endDays);
		String timeOffStartDate = timeOffDate.get("startDateTimeOff");
		String timeOffEndDate = timeOffDate.get("endDateTimeOff");
		setTimeOffStartTime(timeOffStartDate);
		setTimeOffEndTime(timeOffEndDate);
		HashMap<String, String> timeOffDateWithYear = getTimeOffDateWithYear(daysInadvance+startDays, daysInadvance+endDays);
		String timeOffStartDateWithYear = timeOffDateWithYear.get("startDateWithYearTimeOff");
		String timeOffEndDateWithYear = timeOffDateWithYear.get("endDateWithYearTimeOff");
		startNEndDates.add(timeOffStartDateWithYear);
		startNEndDates.add(timeOffEndDateWithYear);
		return startNEndDates;
	}



	@Override
	public String selectStartAndEndDateAtSameDay() throws Exception {
		selectDate(10);
		selectDate(10);
		HashMap<String, String> timeOffDateWithYear = getTimeOffDateWithYear(10, 10);
		String timeOffStartDateWithYear = timeOffDateWithYear.get("startDateWithYearTimeOff");
		SimpleUtils.report("Create Time Off on: " + timeOffStartDateWithYear);
		return timeOffStartDateWithYear;
	}

	@Override
	public HashMap<String, List<String>> selectCurrentDayAsStartNEndDate() throws Exception {
		HashMap<String, List<String>> selectedDateNTime = new HashMap<>();
		List<String> startNEndTimes = new ArrayList<>();
		if (isElementLoaded(todayOnCalendar, 5)) {
			click(todayOnCalendar);
			click(todayOnCalendar);
			areAllDayCheckboxesLoaded();
			deSelectAllDayCheckboxes();
			if (areListElementVisible(startNEndDates, 5) && startNEndDates.size() == 2) {
				startNEndTimes = getSelectedStartNEndTime();
				if (startNEndDates.get(0).getText().equalsIgnoreCase(startNEndDates.get(1).getText())) {
					selectedDateNTime.put(startNEndDates.get(0).getText(), startNEndTimes);
				} else {
					SimpleUtils.fail("Start date and end date is inconsistent since choosing one day!", true);
				}
			}else {
				SimpleUtils.fail("Start and end dates not loaded Successfully!", true);
			}
		}else {
			SimpleUtils.fail("Today on Calendar not loaded Successfully!", true);
		}
		return selectedDateNTime;
	}

	@Override
	public boolean areAllDayCheckboxesLoaded() throws Exception {
		boolean areLoaded = false;
		if (areListElementVisible(allDayCheckboxes, 5)) {
			SimpleUtils.pass("All day checkboxes are loaded successfully!");
			areLoaded = true;
		}else {
			SimpleUtils.fail("All day checkboxes failed to load!", false);
		}
		return areLoaded;
	}

	@Override
	public void deSelectAllDayCheckboxes() throws Exception {
		if (areListElementVisible(allDayCheckboxes, 5)) {
			for (WebElement allDay : allDayCheckboxes) {
				if (allDay.isDisplayed() && allDay.getAttribute("class").contains("checked")) {
					click(allDay);
				}
			}
		}else {
			SimpleUtils.fail("All day checkbox failed to load!", false);
		}
	}

	@Override
	public void verifyAlignmentOfAMAndPMAfterDeSelectAllDay() throws Exception {
		String textAlign = "center";
		String verticalAlign = "middle";
		if (isAMAndPMLoaded()) {
			for (WebElement sm : smButtons) {
				if (!textAlign.equals(sm.getCssValue("text-align")) || !verticalAlign.equals(sm.getCssValue("vertical-align"))) {
					SimpleUtils.fail("Alignment for AM and PM is incorrect!", false);
				}
			}
		}else {
			SimpleUtils.fail("AM and PM button failed to load!", false);
		}
	}

	@Override
	public void verifyStartDateAndEndDateIsCorrect(String timeOffStartDate, String timeOffEndDate) throws Exception {
		boolean isCorrect = false;
		if (areListElementVisible(startNEndDates, 5) && startNEndDates.size() == 2) {
			String actualStartDate = startNEndDates.get(0).getText();
			String actualEndDate = startNEndDates.get(1).getText();
			if (timeOffStartDate.equals(actualStartDate) && timeOffEndDate.equals(actualEndDate)) {
				SimpleUtils.pass("Starts Date and Ends date are correct!");
				isCorrect = true;
			}
		}
		if (!isCorrect) {
			SimpleUtils.fail("Starts Date and Ends date are incorrect!", true);
		}
	}

	@Override
	public int getTimeOffCountByStatusLabel(String status) throws Exception {
		int count = 0;
		if (areListElementVisible(timeOffStatus, 5)) {
			for (WebElement element : timeOffStatus) {
				if (element.getText().equalsIgnoreCase(status)) {
					WebElement countElement = element.findElement(By.xpath("./preceding-sibling::span[1]"));
					count = Integer.parseInt(countElement.getText());
				}
			}
		}else {
			SimpleUtils.fail("Time Off Status elements not loaded Successfully!", true);
		}
		return count;
	}

	public boolean isAMAndPMLoaded() throws Exception {
		boolean isLoaded = false;
		if (areListElementVisible(smButtons, 5)) {
			SimpleUtils.pass("AM and PM buttons are loaded successfully!");
			isLoaded = true;
		}
		return isLoaded;
	}

	//added by Haya
	@FindBy(xpath = "//div[@class=\"timeoff-requests ng-scope\"]//timeoff-list-item")
	private List<WebElement> timeOffRequestItems;
	@FindBy(css = "[timeoff=\"timeoff\"] .request-status-Approved")
	private List<WebElement> approvedTimeOffRequests;
	@FindBy(css = "[timeoff=\"timeoff\"] .request-status-Pending")
	private List<WebElement> pendingTimeOffRequests;
	@FindBy(css = ".user-profile-section .request-status-Pending")
	private List<WebElement> pendingAvailabilityRequests;
	@FindBy(css = ".user-profile-section .request-status-Cancelled")
	private List<WebElement> cancelledAvailabilityRequests;
	@FindBy(css = ".user-profile-section .timeoff-requests-request")
	private  List<WebElement> allAvailabilityRequests;
	@FindBy(css = ".request-buttons-approve")
	private WebElement approveAvailabilityButton;
	@FindBy(css = ".request-buttons-reject")
	private WebElement rejectAvailabilityButton;
	@FindBy(css = ".user-profile-section div.count-block.count-block-pending span.count-block-counter")
	private WebElement pendingCouter;
	@FindBy(css = ".user-profile-section div.count-block.count-block-approved span.count-block-counter")
	private WebElement approvedCouter;
	@FindBy(css = ".user-profile-section div.count-block.count-block-rejected span.count-block-counter")
	private WebElement rejectedCouter;

	@Override
	public void approveAllPendingAvailabilityRequest() throws Exception {
		if (areListElementVisible(pendingAvailabilityRequests, 10)) {
			for (WebElement pendingRequest : pendingAvailabilityRequests) {
				clickTheElement(pendingRequest);
				if (isElementLoaded(approveAvailabilityButton, 10)) {
					clickTheElement(approveAvailabilityButton);
					SimpleUtils.pass("Approve the pending availability request successfully!");
					approveAllPendingAvailabilityRequest();
					break;
				}
			}
		}
	}

	@Override
	public void verifyTheLatestAvailabilityRequestInfo(String weekInfo, double hours, String repeatChanges ) throws Exception {
		String increaseOrDecrease = "";
		String hourStr = "";
		String resultInfo = "";
		String newHours = "";
		if (hours>0){
			increaseOrDecrease = "Increased";
			hourStr = String.valueOf(hours);
		} else {
			increaseOrDecrease = "Decreased";
			hourStr = String.valueOf(hours).replace("-", "");
		}
		if (areListElementVisible(allAvailabilityRequests, 10) && pendingAvailabilityRequests.size()>0 ) {
			for (WebElement element: allAvailabilityRequests){
				if (element.findElement(By.cssSelector(".request-stat")).getText().toLowerCase().contains("pending")){
					resultInfo = element.findElement(By.cssSelector(".request-date")).getText().replace("\n", "");
					SimpleUtils.assertOnFail("Week info is not correct!", resultInfo.equalsIgnoreCase(weekInfo), true);
					resultInfo = element.findElement(By.cssSelector(".request-body")).getText();
					SimpleUtils.assertOnFail("Decreased or Increased hours info is not correct!", resultInfo.contains("Availability "+increaseOrDecrease+" "+hourStr+" Hrs"), true);
					if (resultInfo.split("\n").length == 3){
						String newHoursTemp = String.valueOf(resultInfo.split("\n")[1].split(" \\| ")[0].replace("Current: ","").replace("Hrs","").trim());
						if (SimpleUtils.isNumeric(newHoursTemp)){
							newHours = String.valueOf(Double.valueOf(newHoursTemp)+hours);
							SimpleUtils.assertOnFail("Current and New hours are not correct!", resultInfo.contains(newHours+" Hrs"), true);
						} else {
							SimpleUtils.fail("Availability request info is not in expected format!", false);
						}
					} else {
						SimpleUtils.fail("Availability request info is not complete!", false);
					}
					SimpleUtils.assertOnFail("Submitted date info is not correct!", resultInfo.split("\n")[2].contains("Submitted "+SimpleUtils.getCurrentDateMonthYearWithTimeZone("GMT-5", new SimpleDateFormat("MMM d,yyyy"))), true);
					break;
				}
			}
		} else {
			SimpleUtils.report("No pending availability request in the list!");
		}
	}

	@Override
	public String getCountForStatus(String status) throws Exception {
		if (status.equalsIgnoreCase("pending")){
			if (isElementLoaded(pendingCouter, 10)){
				return pendingCouter.getText();
			}
		} else if (status.equalsIgnoreCase("approved")){
			if (isElementLoaded(approvedCouter, 10)){
				return approvedCouter.getText();
			}
		} else if (status.equalsIgnoreCase("rejected")){
			if (isElementLoaded(rejectedCouter, 10)){
				return rejectedCouter.getText();
			}
		} else {
			SimpleUtils.fail("Please input the right status!", false);
		}
		return null;
	}

	//Available hours for a week in work preference table.
	@FindBy(css = ".tm-total-hours-label-green")
	private WebElement availableHrs;
	@Override
	public String getAvailableHoursForSpecificWeek() throws Exception {
		if (isElementLoaded(availableHrs, 10)){
			return availableHrs.getText();
		}
		return null;
	}

	@Override
	public void newApproveOrRejectTimeOffRequestFromToDoList(String timeOffReasonLabel, String timeOffStartDuration,
														  String timeOffEndDuration, String action) throws Exception{
		String timeOffStartDate = timeOffStartDuration.split(", ")[1].split(" ")[1];
		String timeOffStartMonth = timeOffStartDuration.split(", ")[1].split(" ")[0];
		String timeOffEndDate = timeOffEndDuration.split(", ")[1].split(" ")[1];
		String timeOffEndMonth = timeOffEndDuration.split(", ")[1].split(" ")[0];
		if(areListElementVisible(timeOffRequestItems,10) && timeOffRequestItems.size() > 0) {
			for(WebElement timeOffRequest : timeOffRequestItems) {
				WebElement requestType = timeOffRequest.findElement(By.cssSelector("span.request-type"));
				if(requestType.getText().toLowerCase().contains(timeOffReasonLabel.toLowerCase())) {
					WebElement requestDate = timeOffRequest.findElement(By.cssSelector("div.request-date"));
					String[] requestDateText = requestDate.getText().replace("\n", "").split("-");
					if(requestDateText[0].toLowerCase().contains(timeOffStartMonth.toLowerCase())
							&& requestDateText[0].toLowerCase().contains(timeOffStartDate.toLowerCase())
							&& requestDateText[1].toLowerCase().contains(timeOffEndMonth.toLowerCase())
							&& requestDateText[1].toLowerCase().contains(timeOffEndDate.toLowerCase())) {
						click(timeOffRequest);
						if(action.toLowerCase().contains("cancel")) {
							if(isElementLoaded(timeOffRequestCancelBtn)) {
								scrollToElement(timeOffRequestCancelBtn);
								click(timeOffRequestCancelBtn);
								SimpleUtils.pass("My Time Off: Time off request cancel button clicked.");
							} else {
								SimpleUtils.fail("My Time Off: Time off request cancel button not loaded.", true);
							}
						}
						else if(action.toLowerCase().contains("approve")) {
							if(isElementLoaded(timeOffRequestApproveBtn)) {
								click(timeOffRequestApproveBtn);
								SimpleUtils.pass("My Time Off: Time off request Approve button clicked.");
							} else{
								SimpleUtils.fail("My Time Off: Time off request Approve button not loaded.", true);
							}
						}
					}
				}
			}
		}
		else
			SimpleUtils.fail("Profile Page: No Time off request found.", false);
	}

	@Override
	public void cancelAllTimeOff() throws Exception {
		if(areListElementVisible(approvedTimeOffRequests,10) && approvedTimeOffRequests.size() > 0) {
			for(WebElement timeOffRequest : approvedTimeOffRequests) {
				scrollToElement(timeOffRequest);
				waitForSeconds(3);
				clickTheElement(timeOffRequest);
				if(isElementLoaded(timeOffRequestCancelBtn,5)) {
					scrollToElement(timeOffRequestCancelBtn);
					click(timeOffRequestCancelBtn);
					SimpleUtils.pass("My Time Off: Time off request cancel button clicked.");
				}
			}
		}
		if(areListElementVisible(pendingTimeOffRequests,10) && pendingTimeOffRequests.size() > 0) {
			for(WebElement timeOffRequest : pendingTimeOffRequests) {
				scrollToElement(timeOffRequest);
				clickTheElement(timeOffRequest);
				if(isElementLoaded(timeOffRequestCancelBtn,5)) {
					scrollToElement(timeOffRequestCancelBtn);
					click(timeOffRequestCancelBtn);
					SimpleUtils.pass("My Time Off: Time off request cancel button clicked.");
				}
			}
		}
	}

	@Override
	public void rejectAllTimeOff() throws Exception {
		if(areListElementVisible(approvedTimeOffRequests,10) && approvedTimeOffRequests.size() > 0) {
			for(WebElement timeOffRequest : approvedTimeOffRequests) {
				clickTheElement(timeOffRequest);
				if(isElementLoaded(timeOffRejectBtn,5)) {
					scrollToElement(timeOffRejectBtn);
					clickTheElement(timeOffRejectBtn);
					SimpleUtils.pass("My Time Off: Time off request cancel button clicked.");
				}
			}
		}
	}

	@Override
	public void approveOrRejectTimeOffRequestFromToDoList(String timeOffReasonLabel, String timeOffStartDuration, 
			String timeOffEndDuration, String action) throws Exception{
		String timeOffStartDate = timeOffStartDuration.split(",")[0].split(" ")[1];
		String timeOffStartMonth = timeOffStartDuration.split(",")[0].split(" ")[0];
		String timeOffEndDate = timeOffEndDuration.split(",")[0].split(" ")[1];
		String timeOffEndMonth = timeOffEndDuration.split(",")[0].split(" ")[0];
		
		int timeOffRequestCount = timeOffRequestRows.size();
		if(timeOffRequestCount > 0) {
			for(WebElement timeOffRequest : timeOffRequestRows) {
				WebElement requestType = timeOffRequest.findElement(By.cssSelector("span.request-type"));
				if(requestType.getText().toLowerCase().contains(timeOffReasonLabel.toLowerCase())) {
					WebElement requestDate = timeOffRequest.findElement(By.cssSelector("div.request-date"));
					String[] requestDateText = requestDate.getText().replace("\n", "").split("-");
					if(requestDateText[0].toLowerCase().contains(timeOffStartMonth.toLowerCase()) 
							&& requestDateText[0].toLowerCase().contains(timeOffStartDate.toLowerCase())
							&& requestDateText[1].toLowerCase().contains(timeOffEndMonth.toLowerCase()) 
							&& requestDateText[1].toLowerCase().contains(timeOffEndDate.toLowerCase())) {
						click(timeOffRequest);
						if(action.toLowerCase().contains("cancel")) {
							if(isElementLoaded(timeOffRequestCancelBtn)) {
								click(timeOffRequestCancelBtn);
								SimpleUtils.pass("My Time Off: Time off request cancel button clicked.");
							}
							else
								SimpleUtils.fail("My Time Off: Time off request cancel button not loaded.", true);
						}
						else if(action.toLowerCase().contains("approve")) {
							if(isElementLoaded(timeOffRequestApproveBtn)) {
								click(timeOffRequestApproveBtn);
								SimpleUtils.pass("My Time Off: Time off request Approve button clicked.");
							}
							else
								SimpleUtils.fail("My Time Off: Time off request Approve button not loaded.", true);
						}
					}
				}
			}
		}
		else
			SimpleUtils.fail("Profile Page: No Time off request found.", false);
	}

	@Override
	public String getNickNameFromProfile() throws Exception {
		String nickName = "";
		try{
			waitForSeconds(3);
			if(isElementLoaded(userProfileImage, 5)){
				clickTheElement(userProfileImage);
				waitForSeconds(1);
				clickTheElement(getDriver().findElement(By.id("legion_Profile_MyProfile")));
				WebElement nameElement = null;
				waitForSeconds(5);
				if (areListElementVisible(getDriver().findElements(By.cssSelector(".userProfileText")), 25)) {
					nameElement = getDriver().findElements(By.cssSelector(".userProfileText")).get(0);
				} else if (areListElementVisible(getDriver().findElements(By.cssSelector(".sc-eJKagG+div>div>div:nth-child(2)")),5)) {
					nameElement = getDriver().findElement(By.cssSelector(".sc-eJKagG+div>div>div:nth-child(2)"));
				}
				nickName = nameElement.getText().trim().contains(" ") ?
						nameElement.getText().trim().split(" ")[0] : nameElement.getText().trim();
				if(nickName != null && !nickName.isEmpty()){
					SimpleUtils.pass("Get User's NickName: " + nickName + "Successfully");
				}else{
					SimpleUtils.fail("The NickName is null!", false);
				}
			}else{
				SimpleUtils.fail("User Profile Image doesn't Load Successfully!", false);
			}
		}catch (Exception e){
			SimpleUtils.fail("Get NickName of the logged in user failed", false);
		}
		return nickName;
	}

	@Override
	public void clickOnUserProfileImage() throws Exception {
		if(isElementLoaded(userProfileImage, 5)) {
			click(userProfileImage);
		}else {
			SimpleUtils.fail("User profile Image failed to load!", false);
		}
	}

	@Override
	public void selectProfileSubPageByLabelOnProfileImage(String profilePageSubSectionLabel) throws Exception {
		if (!isElementLoaded(profileSwitchMenu, 5)) {
			clickTheElement(userProfileImage);
		}
		if (areListElementVisible(profileSubPageLabels, 5)) {
			for (WebElement label : profileSubPageLabels) {
				if (label.getText().equals(profilePageSubSectionLabel)) {
					clickTheElement(label);
					break;
				}
			}
		}else {
			SimpleUtils.fail("Profile sub labels failed to load after clicking on Profile Image!", false);
		}
	}

	//Added by Julie
	@FindBy(css = ".user-readonly-details")
	private List<WebElement> profileAddressInformation;

	@FindBy(css = ".lgn-alert-message")
	private WebElement alertMessage;

	@FindBy(css = ".ng-binding[ng-if=\"noticePeriodToRequestTimeOff\"]")
	private WebElement noticePeriodToRequestTimeOff;

	@FindBy(css = ".ranged-calendar__day.is-today")
	private WebElement todayInCalendarDates;

	@FindBy(css = ".request-buttons-reject")
	private WebElement cancelButtonOfPendingRequest;

	@FindBy(css = ".lgn-alert-modal")
	private WebElement alertDialog;

	@FindBy(css = ".lgn-action-button-success")
	private WebElement OKButton;

	@FindBy(css = "lg-button[label=\"Edit\"]")
	private WebElement editBtnOfMyShiftPreferences;

	@FindBy(css = "lg-button[label=\"Save\"]")
	private WebElement saveBtnOfMyShiftPreference;

	private ArrayList<String> minMaxArray = new ArrayList<>();

	public void checkUserProfileHomeAddress(String streetAddress1, String streetAddress2, String city, String state, String zip) throws Exception {
		if (areListElementVisible(profileAddressInformation, 5)) {
			if (profileAddressInformation.get(1).getText().contains(streetAddress1) && profileAddressInformation.get(1).getText().contains(streetAddress2) && profileAddressInformation.get(1).getText().contains(city) && profileAddressInformation.get(1).getText().contains("CA") && profileAddressInformation.get(1).getText().contains(zip))
				SimpleUtils.pass("Profile Page: User Profile Address already updated with value: '" + streetAddress1 + " " + streetAddress2 + " " + city + " " + state + " " + zip + "'.");
			SimpleUtils.pass("Profile Page: User Profile changes reflects after saving successfully");
		} else {
			SimpleUtils.fail("Profile Page: User Profile Address not updated", true);
		}
	}

	@Override
	public void validateTheEditFunctionalityOnMyProfile(String streetAddress1, String streetAddress2, String city, String state, String zip) throws Exception {
		clickOnEditUserProfilePencilIcon();
		updateUserProfileHomeAddress(streetAddress1, streetAddress2, city, state, zip);
		clickOnSaveUserProfileBtn();
		scrollToTop();
		checkUserProfileHomeAddress(streetAddress1, streetAddress2, city, state, zip);
/*		if (isEngagementDetrailsSectionLoaded()) {
			if (engagementDetailsSection.findElements(By.tagName("input")).size() == 0 || engagementDetailsSection.findElements(By.tagName("i")).size() == 0) {
				SimpleUtils.pass("Profile Page: Engagement Details are not be editable as expected");
			} else {
				SimpleUtils.fail("Profile Page: Engagement Details can be editable", true);
			}
		} else {
			SimpleUtils.fail("Engagement Details not loaded", true);
		}*/
	}

	@Override
	public void validateTheFeatureOfChangePassword(String oldPassword) throws Exception {
		if (isElementLoaded(userProfileChangePasswordBtn, 5)) {
			click(userProfileChangePasswordBtn);
			SimpleUtils.pass("Profile Page: user profile 'Change Password' button clicked successfully.");

			if (isElementLoaded(changePasswordPopUp, 10)) {
				String newPassword = "";
				String confirmPassword = "";
				SimpleUtils.pass("Profile Page: user profile 'Change Password' popup loaded successfully.");

				newPassword = getNewPassword(oldPassword);
				confirmPassword = getNewPassword(oldPassword);
				changePasswordPopUpOldPasswordField.sendKeys(oldPassword);
				changePasswordPopUpNewPasswordField.sendKeys(newPassword);
				changePasswordPopUpConfirmPasswordField.sendKeys(confirmPassword);
				click(changePasswordPopUpPopUpSendBtn);
				if (isElementLoaded(alertMessage, 10) && alertMessage.getText().contains("Password changed successfully")) {
					SimpleUtils.pass("Profile Page: New password is saved successfully");
				} else {
					SimpleUtils.fail("Profile Page: New password may be not saved since there isn't alert message", true);
				}
			} else
				SimpleUtils.fail("Profile Page: user profile 'Change Password' popup not loaded.", false);
		} else {
			SimpleUtils.fail("Profile Page: user profile 'Change Password' button failed to load", true);
		}
	}

	@Override
	public String getNewPassword(String oldPassword) throws Exception {
		String newPassword = "";
		if (oldPassword.equals("legionco1")) {
			newPassword = "legionco2";
			return newPassword;
		} else if (oldPassword.equals("legionco2")) {
			newPassword = "legionco1";
			return newPassword;
		} else {
			SimpleUtils.fail("Please check the current user password", true);
			return "";
		}
	}

	@Override
	public void validateTheUpdateOfShiftPreferences(boolean canReceiveOfferFromOtherLocation, boolean isVolunteersForAdditional) throws Exception {
		updateMyShiftPreferenceData(canReceiveOfferFromOtherLocation, isVolunteersForAdditional);
		minMaxArray = updateMyShiftPreferencesAvailabilitySliders();
		saveMyShiftPreferences();
		checkMyShiftPreferenceData(canReceiveOfferFromOtherLocation, isVolunteersForAdditional, minMaxArray);
	}

	public void checkMyShiftPreferenceData(boolean canReceiveOfferFromOtherLocation, boolean isVolunteersForAdditional, ArrayList<String> minMaxArray) throws Exception {
		HashMap<String, String> shiftPreferenceData = getMyShiftPreferenceData();
		if (minMaxArray != null && minMaxArray.size() == 6 && (minMaxArray.get(0) + " - " + minMaxArray.get(1)).equals(shiftPreferenceData.get("hoursPerWeek")))
			SimpleUtils.pass("Shift Preference Data: 'Hours/wk' value('"
					+ minMaxArray.get(0) + " - " + minMaxArray.get(1) + "/" + shiftPreferenceData.get("hoursPerWeek") + "') matched.");
		else
			SimpleUtils.fail("Shift Preference Data: 'Hours/wk' value('"
					+ minMaxArray.get(0) + " - " + minMaxArray.get(1) + "/" + shiftPreferenceData.get("hoursPerShift") + "') not matched.", true);
		if (minMaxArray != null && minMaxArray.size() == 6 && (minMaxArray.get(2) + " - " + minMaxArray.get(3)).equals(shiftPreferenceData.get("hoursPerShift")))
			SimpleUtils.pass("Shift Preference Data: 'Hours/shift' value('"
					+ minMaxArray.get(2) + " - " + minMaxArray.get(3) + "/" + shiftPreferenceData.get("hoursPerShift") + "') matched.");
		else
			SimpleUtils.fail("Shift Preference Data: 'Hours/shift' value('"
					+ minMaxArray.get(2) + " - " + minMaxArray.get(3) + "/" + shiftPreferenceData.get("hoursPerShift") + "') not matched.", true);
		if (minMaxArray != null && minMaxArray.size() == 6 && (minMaxArray.get(4) + " - " + minMaxArray.get(5)).equals(shiftPreferenceData.get("shiftsPerWeek")))
			SimpleUtils.pass("Shift Preference Data: 'Shifts/wk' value('"
					+ minMaxArray.get(4) + " - " + minMaxArray.get(5) + "/" + shiftPreferenceData.get("shiftsPerWeek") + "') matched.");
		else
			SimpleUtils.fail("Shift Preference Data: 'Shifts/wk' value('"
					+ minMaxArray.get(4) + " - " + minMaxArray.get(5) + "/" + shiftPreferenceData.get("shiftsPerWeek") + "') not matched.", true);
		if (isElementLoaded(volunteerMoreHoursCheckButton, 10)) {
			if (isVolunteersForAdditional == SimpleUtils.convertYesOrNoToTrueOrFalse(shiftPreferenceData.get("volunteerForAdditionalWork")))
				SimpleUtils.pass("Shift Preference Data: ''Volunteer Standby List' value('"
						+ isVolunteersForAdditional + "/" + shiftPreferenceData.get("volunteerForAdditionalWork") + "') matched.");
			else
				SimpleUtils.fail("Shift Preference Data: 'Volunteer Standby List' value('"
						+ isVolunteersForAdditional + "/" + shiftPreferenceData.get("volunteerForAdditionalWork") + "') not matched.", true);
		} else  {
			SimpleUtils.report("Shift Preference Data: ''Volunteer Standby List' is disabled and cannot be set");
		}
		if (canReceiveOfferFromOtherLocation == SimpleUtils.convertYesOrNoToTrueOrFalse(shiftPreferenceData.get("otherPreferredLocations")))
			SimpleUtils.pass("Shift Preference Data: 'Other preferred locations' value('"
					+ canReceiveOfferFromOtherLocation + "/" + shiftPreferenceData.get("otherPreferredLocations") + "') matched.");
		else
			SimpleUtils.fail("Shift Preference Data: 'Other preferred locations' value('"
					+ canReceiveOfferFromOtherLocation + "/" + shiftPreferenceData.get("otherPreferredLocations") + "') not matched.", true);

	}

	public void updateMyShiftPreferenceData(boolean canReceiveOfferFromOtherLocation, boolean isVolunteersForAdditional) throws Exception {
		clickOnEditMyShiftPreferenceButton();
		if (isMyShiftPreferenceEditContainerLoaded()) {
			SimpleUtils.pass("Profile Page: 'My Shift Preference' edit Container loaded successfully.");
			updateReceivesShiftOffersForOtherLocationCheckButton(canReceiveOfferFromOtherLocation);
			if(isElementLoaded(volunteerMoreHoursCheckButton, 10)) {
				updateVolunteersForAdditionalWorkCheckButton(isVolunteersForAdditional);
			} else
				SimpleUtils.report("Profile Page: 'Volunteers for Additional Work' Checkbox is disabled due to admin setting");
		} else
			SimpleUtils.fail("Profile Page: 'My Shift Preference' edit Container not loaded.", true);
	}

	public ArrayList<String> updateMyShiftPreferencesAvailabilitySliders() throws Exception {
		String startValue = "";
		String endValue = "";
		String maxValue = "";
		String minValue = "";
		WebElement minSlider = null;
		WebElement maxSlider = null;
		WebElement sliderType = null;
		if (areListElementVisible(availabilitySliders, 20) && availabilitySliders.size() > 0) {
			for (WebElement availabilitySlider : availabilitySliders) {
				minSlider = availabilitySlider.findElement(By.cssSelector("span[ng-style=\"minPointerStyle\"]"));
				maxSlider = availabilitySlider.findElement(By.cssSelector("span[ng-style=\"maxPointerStyle\"]"));
				sliderType = availabilitySlider.findElement(By.cssSelector(".edit-pref-label"));
				startValue = minSlider.getAttribute("aria-valuenow");
				endValue = maxSlider.getAttribute("aria-valuenow");
				maxValue = maxSlider.getAttribute("aria-valuemax");
				minValue = minSlider.getAttribute("aria-valuemin");
				if (Integer.parseInt(endValue) > Integer.parseInt(maxValue)) {
					endValue = maxValue;
				}
				if (Integer.parseInt(startValue) < Integer.parseInt(minValue)) {
					startValue = minValue;
				}
				// Update Min/Max Slider
				if (areListElementVisible(availabilitySlider.findElements(By.tagName("li")), 5)) {
					int index = (new Random()).nextInt(availabilitySlider.findElements(By.tagName("li")).size());
					String value = availabilitySlider.findElements(By.tagName("li")).get(index).findElement(By.tagName("span")) == null ? "" : availabilitySlider.findElements(By.tagName("li")).get(index).findElement(By.tagName("span")).getText();
					if (!startValue.equals(value) && !endValue.equals(value)) {
						clickTheElement(availabilitySlider.findElements(By.tagName("li")).get(index));
						startValue = minSlider.getAttribute("aria-valuenow");
						endValue = maxSlider.getAttribute("aria-valuenow");
						if (Integer.parseInt(endValue) > Integer.parseInt(maxValue)) {
							endValue = maxValue;
						}
						if (Integer.parseInt(startValue) < Integer.parseInt(minValue)) {
							startValue = minValue;
						}
						SimpleUtils.report("Select value: " + value + " successfully!");
						SimpleUtils.report("Profile Page: 'Min Slider' " + startValue + " for " + sliderType.getText() + "  is selected.");
						SimpleUtils.report("Profile Page: 'Max Slide' " + endValue + " for " + sliderType.getText() + " is selected");
					}
				} else {
					SimpleUtils.fail("Slider elements failed to load!", true);
				}
				minMaxArray.add(startValue);
				minMaxArray.add(endValue);
			}
		} else
			SimpleUtils.fail("Profile Page: Edit My Shift Preferences - Availability Sliders not loaded.", true);
		return minMaxArray;
	}

	@Override
	public void validateTheUpdateOfAvailability(String hoursType, int sliderIndex, String leftOrRightDuration,
												int durationMinutes, String repeatChanges) throws Exception {
		boolean isMyAvailabilityLocked = isMyAvailabilityLocked();
		if (isMyAvailabilityLocked) {
			ArrayList<HashMap<String, ArrayList<String>>> myAvailabilityPreferredAndBusyHoursBeforeUpdate = getMyAvailabilityPreferredAndBusyHours();
			String availabilityPreferredAndBusyHoursHTMLBefore = "<table>";
			for (HashMap<String, ArrayList<String>> preferredAndBusyHours : myAvailabilityPreferredAndBusyHoursBeforeUpdate) {
				availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "<tr>";
				for (Map.Entry<String, ArrayList<String>> entry : preferredAndBusyHours.entrySet()) {
					if (entry.getValue().size() > 0) {
						availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "<td><b>"
								+ entry.getKey() + "</b></td>";
						for (String value : entry.getValue()) {
							availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "<td>"
									+ value + "</td>";
						}
						availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "</td>";
					}
				}
				availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "</tr>";
			}
			availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "</table>";

			if (myAvailabilityPreferredAndBusyHoursBeforeUpdate.size() > 0)
				SimpleUtils.pass("Profile page: 'My Availability Preferred & Busy Hours Duration Per Day <b>Before Updating</b> loaded as Below.<br>"
						+ availabilityPreferredAndBusyHoursHTMLBefore);
			else
				SimpleUtils.fail("Profile page: 'My Availability Preferred & Busy Hours Duration not loaded", true);

			//Update Preferred And Busy Hours
			updateLockedAvailabilityPreferredOrBusyHoursSlider(hoursType, sliderIndex, leftOrRightDuration,
					durationMinutes, repeatChanges);

			ArrayList<HashMap<String, ArrayList<String>>> myAvailabilityPreferredAndBusyHoursAfterUpdate = getMyAvailabilityPreferredAndBusyHours();
			String availabilityPreferredAndBusyHoursHTMLAfter = "<table>";
			for (HashMap<String, ArrayList<String>> preferredAndBusyHours : myAvailabilityPreferredAndBusyHoursAfterUpdate) {
				availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "<tr>";
				for (Map.Entry<String, ArrayList<String>> entry : preferredAndBusyHours.entrySet()) {
					if (entry.getValue().size() > 0) {
						availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "<td><b>"
								+ entry.getKey() + "</b></td>";
						for (String value : entry.getValue()) {
							availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "<td>"
									+ value + "</td>";
						}
						availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "</td>";
					}
				}
				availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "</tr>";
			}
			availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "</table>";

			if (myAvailabilityPreferredAndBusyHoursAfterUpdate.size() > 0)
				SimpleUtils.pass("Profile page: 'My Availability Preferred & Busy Hours Duration Per Day <b>After Updating</b> loaded as Below.<br>"
						+ availabilityPreferredAndBusyHoursHTMLAfter);
			else
				SimpleUtils.fail("Profile page: 'My Availability Preferred & Busy Hours Duration not loaded", true);
		} else
			SimpleUtils.report("Profile Page: 'My Availability Section not locked for the week '"
					+ getMyAvailabilityData().get("activeWeek") + "'");
	}

	public int verifyCannotSelectDates() throws Exception {
		int cannotSelectDates = 0;
		int periodToRequestTimeOff = getPeriodToRequestTimeOff();
		if (areListElementVisible(calendarDates, 10)) {
			for (WebElement calendarDate : calendarDates) {
				if (calendarDate.getAttribute("class").contains("can-not-select")) {
					cannotSelectDates++;
				}
			}
			if (Integer.parseInt(calendarDates.get(cannotSelectDates).getText().trim()) == Integer.parseInt(todayInCalendarDates.getText().trim()) + periodToRequestTimeOff) {
				SimpleUtils.pass("New Time Off Request: It should not be able to select a date within " + periodToRequestTimeOff + " days from today(Can be changed as per the Control settings)");
			} else {
				SimpleUtils.fail("New Time Off Request: It can be able to select a date within " + periodToRequestTimeOff + " days from today", true);
			}
		} else
			SimpleUtils.fail("New Time Off Request: It failed to load", true);
		return cannotSelectDates;
	}

	public int getPeriodToRequestTimeOff() throws Exception {
		int periodToRequestTimeOff = 0;
		if (isElementLoaded(noticePeriodToRequestTimeOff, 5)) {
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcher = pattern.matcher(noticePeriodToRequestTimeOff.getText());
			while (matcher.find()) {
				periodToRequestTimeOff = Integer.valueOf(matcher.group(0));
			}
		}
		return periodToRequestTimeOff;
	}

	public void reasonsOfLeaveOnNewTimOffRequest() throws Exception {
		if (areListElementVisible(timeOffReasons, 10) && timeOffReasons.size() > 0) {
			for (WebElement timeOffReason : timeOffReasons) {
				if (timeOffReason.isDisplayed())
					SimpleUtils.pass("New Time Off Request: " + timeOffReason.getText() + " is displayed");
				else
					SimpleUtils.fail("New Time Off Request: " + timeOffReason.getText() + " isn't displayed", true);
			}
		} else if (areListElementVisible(calendarDates, 10))
			SimpleUtils.report("New Time Off Request: No time off reason in the request required per the control settings");
		else
			SimpleUtils.fail("New Time Off Request: Reasons failed to load", true);
	}

	@Override
	public String selectRandomReasonOfLeaveOnNewTimeOffRequest() throws Exception {
		String timeoffReasonLabel = "";
		if (areListElementVisible(timeOffReasons, 10)) {
			int index = (new Random()).nextInt(timeOffReasons.size());
			timeoffReasonLabel = timeOffReasons.get(index).getText().trim();
			SimpleUtils.pass("New Time Off Request: " + timeoffReasonLabel + " is selected");
		} else
			SimpleUtils.fail("New Time Off Request: Reasons failed to load", true);
		return timeoffReasonLabel;
	}

	public HashMap<String, String> getTimeOffDate() throws Exception {
		int cannotSelectDates = verifyCannotSelectDates();
		int daysStartFromToday = 0;
		int daysEndFromToday = 0;
		int periodToRequestTimeOff = getPeriodToRequestTimeOff();
		daysStartFromToday = new Random().ints(1,periodToRequestTimeOff,calendarDates.size() - cannotSelectDates).findFirst().getAsInt();
		daysEndFromToday = daysStartFromToday;
		selectDate(daysStartFromToday);
		selectDate(daysEndFromToday);
		HashMap<String, String> timeOffDate = getTimeOffDate(daysStartFromToday, daysEndFromToday);
		return timeOffDate;
	}

	@Override
	public void createNewTimeOffRequestAndVerify(String timeOffReasonLabel, String timeOffExplanationText) throws Exception {
		selectTimeOffReason(timeOffReasonLabel);
		updateTimeOffExplanation(timeOffExplanationText);
		HashMap<String, String> timeOffDate = getTimeOffDate();
		String timeOffStartDate = timeOffDate.get("startDateTimeOff");
		String timeOffEndDate = timeOffDate.get("endDateTimeOff");
		setTimeOffStartTime(timeOffStartDate);
		setTimeOffEndTime(timeOffEndDate);
		scrollToBottom();
		clickOnSaveTimeOffRequestBtn();
		String expectedRequestStatus = "PENDING";
		String requestStatus = getTimeOffRequestStatusByExplanationText(timeOffExplanationText);
		if (requestStatus.contains(expectedRequestStatus))
			SimpleUtils.pass("Profile Page: New Time Off Request reflects in '" + requestStatus + "' successfully after saving");
		else
			SimpleUtils.fail("Profile Page: New Time Off Request status is not '" + expectedRequestStatus
					+ "', status found as '" + requestStatus + "'", true);
	}

	@Override
	public void validateTheFunctionalityOfTimeOffCancellation() throws Exception {
		Boolean pendingRequestCanBeCancelled = pendingRequestCanBeCancelled();
		if (pendingRequestCanBeCancelled) {
			SimpleUtils.pass("Profile Page: Time off request is cancelled successfully");
		} else {
			SimpleUtils.report("Profile Page: No Pending Time off request found or can be cancelled. We will create a new time off");
			clickOnCreateTimeOffBtn();
			String timeOffReasonLabel = selectRandomReasonOfLeaveOnNewTimeOffRequest();
			String timeOffExplanation = (new Random()).nextInt(100) + "random" + (new Random()).nextInt(100) + "random" + (new Random()).nextInt(100);
			createNewTimeOffRequestAndVerify(timeOffReasonLabel, timeOffExplanation);
			String requestStatus = getTimeOffRequestStatusByExplanationText(timeOffExplanation);
			if (requestStatus.toLowerCase().contains("pending")) {
				pendingRequestCanBeCancelled = pendingRequestCanBeCancelled();
				if (pendingRequestCanBeCancelled) {
					SimpleUtils.pass("Profile Page: Time off request is cancelled successfully");
				} else {
					SimpleUtils.fail("Profile Page: Failed to cancel a pending time off request ", true);
				}
			} else
				SimpleUtils.fail("Profile Page: Failed to create a new time off", true);
		}
	}

	public boolean pendingRequestCanBeCancelled() throws Exception {
		Boolean pendingRequestCanBeCancelled = false;
		for (int i = 0; i < timeOffRequestRows.size(); i++) {
			WebElement requestStatus = timeOffRequestRows.get(i).findElement(By.cssSelector("span.request-status"));
			String requestStatusText = requestStatus.getText();
			if (requestStatusText.toLowerCase().contains("pending")) {
				clickTheElement(requestStatus);
				if (isElementLoaded(cancelButtonOfPendingRequest, 5)) {
					clickTheElement(cancelButtonOfPendingRequest);
					if (timeOffRequestRows.get(i).findElement(By.cssSelector(".request-status-Cancelled")).getText().toLowerCase().contains("cancelled")) {
						SimpleUtils.pass("Profile Page: The pending time off request is cancelled successfully");
						pendingRequestCanBeCancelled = true;
						break;
					} else {
						SimpleUtils.fail("Profile Page: The pending time off request failed to cancel", true);
					}
				}
			}
		}
		return pendingRequestCanBeCancelled;
	}

	@FindBy(css = "work-preference-management")
	private WebElement workPreferenceSection;
	public void clickOnEditMyShiftPreferenceButton()  throws Exception {
		if(isElementLoaded(workPreferenceSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")), 10))
			click(workPreferenceSection.findElement(By.cssSelector("lg-button[label=\"Edit\"]")));
		else
			SimpleUtils.fail("Profile Page: 'Edit' button not loaded under 'My Shift Preference' Header.", false);
	}

	private void saveMyShiftPreferences() throws Exception {
		if(isElementLoaded(saveBtnOfMyShiftPreference, 10)) {
			click(saveBtnOfMyShiftPreference);
			if(! isMyShiftPreferenceEditContainerLoaded())
				SimpleUtils.pass("Profile Page: 'My Shift Preference' data saved successfully.");
			else
				SimpleUtils.pass("Profile Page: Unable to save 'My Shift Preference' data.");
		} else
			SimpleUtils.fail("Profile Page: 'My Shift Preference' edit container 'Save' button not loaded.", false);
	}

	@FindBy(xpath = "//div[contains(text(),\"MINOR\")]")
	private WebElement minorField;

	@FindBy(xpath = "//div[contains(text(),\"MINOR\")]/../div[2]")
	private WebElement minorValue;

	@FindBy(css = ".lg-toast__highlight-text")
	private WebElement popupMessage;


	@Override
	public boolean isMINORDisplayed() throws Exception {
		Boolean isMINORDisplayed = false;
		if(isElementLoaded(minorField,10))
			isMINORDisplayed = true;
		else
			SimpleUtils.fail("Profile Page: MINOR field failed to load",false);
		return isMINORDisplayed;
	}

	@Override
	public boolean isMINORYesOrNo() throws Exception {
		Boolean isMINORYesOrNo = false;
		if(isElementLoaded(minorValue,10)) {
			if (minorValue.getText().contains("Yes"))
				isMINORYesOrNo = true;
		} else
			SimpleUtils.fail("Profile Page: MINOR value failed to load",false);
		return isMINORYesOrNo;
	}

	@Override
	public void verifyMINORField(boolean isMinor) throws Exception {
		if (isMINORDisplayed())
			SimpleUtils.pass("Profile Page: Minor filed is displayed on TM Profile");
		else
			SimpleUtils.fail("Profile Page: Minor filed failed to display on TM Profile",false);
		if (isMINORYesOrNo()) {
			if (isMinor == true)
				SimpleUtils.pass("Profile Page: When this tm is minor, it shows \"Yes\" successfully");
			else
				SimpleUtils.fail("Profile Page: When this tm is minor, it failed to display \"Yes\"", false);
		} else {
			if (isMinor == false)
				SimpleUtils.pass("Profile Page: When this tm is minor, it shows \"No\" successfully");
			else
				SimpleUtils.fail("Profile Page: When this tm is minor, it failed to display \"No\"", false);
		}
	}

	@FindBy(css = "[ng-if=\"tm.isMinor && console.isSchoolCalendarsEnabled\"] .profile-heading")
	private WebElement schoolCalendar;

	@FindBy(css = "[options=\"schoolCalendars\"]")
	private WebElement schoolCalendarOptions;

	@FindBy(css = "[options=\"schoolCalendars\"] select option")
	private List<WebElement> schoolCalendarList;

	@FindBy(css = "[label=\"Save\"] button")
	private List<WebElement> saveBtnsOfProfile;

	@FindBy(css = "[ng-click=\"editUserProfile()\"]")
	private WebElement editBtnOfProfile;

	@FindBy(xpath = "//div[contains(text(),\"Name\")]/../span")
	private WebElement nameOfProfile;

	@Override
	public void verifySMCanSelectACalendarForMinor() throws Exception {
		if (isElementLoaded(schoolCalendar,10)) {
			SimpleUtils.pass("Profile Page: There should be \"School Calendar\" section loaded");
			if (isElementLoaded(editBtnOfProfile,15)) {
				click(editBtnOfProfile);
				if (isElementLoaded(schoolCalendarOptions,5) && schoolCalendarList.size() > 1) {
					click(schoolCalendarOptions);
					int index = (new Random()).nextInt(schoolCalendarList.size() - 1) + 1;
					if (!schoolCalendarList.get(index).getText().trim().equals("None")) {
						click(schoolCalendarList.get(index));
						SimpleUtils.pass("Profile Page: The calendars all are loaded and can be selected");
					}
					if (areListElementVisible(saveBtnsOfProfile,5)) {
						clickTheElement(saveBtnsOfProfile.get(0));
						if (isElementLoaded(popupMessage,5) && popupMessage.getText().contains("Success"))
							SimpleUtils.pass("Profile Page: The selected calendar is saved successfully");
						else
							SimpleUtils.fail("Profile Page: No success message when saving the profile",false);
					} else
						SimpleUtils.fail("Profile Page: The selected calendar failed to save",false);
				} else
					SimpleUtils.fail("Profile Page: No calendar can be selected, please create one firstly",false);
			} else
					SimpleUtils.fail("Profile Page: \"Edit\" button failed to load",false);
			} else
				SimpleUtils.fail("Profile Page: Cannot find \"School Calendar\" section for a minor",false);
	}

	@FindBy(css = "[options=\"schoolCalendars\"] select")
	private WebElement schoolCalendarSelect;
	@FindBy(css = "[ng-if*=\"SchoolCalendars\"] p")
	private WebElement assignedCalendar;

	@Override
	public void selectAGivenCalendarForMinor(String givenCalendar) throws Exception {
		if (isElementLoaded(editBtnOfProfile,15)) {
			clickTheElement(editBtnOfProfile);
			selectByVisibleText(schoolCalendarSelect, givenCalendar);
			if (areListElementVisible(saveBtnsOfProfile,5)) {
				clickTheElement(saveBtnsOfProfile.get(0));
				if (isElementLoaded(popupMessage,10) && popupMessage.getText().contains("Success"))
					SimpleUtils.pass("Profile Page: The selected calendar is saved successfully");
				else
					SimpleUtils.fail("Profile Page: No success message when saving the profile",false);
				waitForSeconds(3);
				if (isElementLoaded(assignedCalendar, 10) && assignedCalendar.getText().trim().equalsIgnoreCase(givenCalendar))
					SimpleUtils.pass("Profile Page: The given calendar is selected successfully");
				else
					SimpleUtils.fail("Profile Page: The given calendar failed to select",false);
			} else
				SimpleUtils.fail("Profile Page: The selected calendar failed to save",false);
		} else
			SimpleUtils.fail("Profile Page: \"Edit\" button failed to load",false);
	}

	@Override
	public HashMap<String, String> getUserProfileName() throws Exception {
		HashMap<String, String> userProfileNames = new HashMap<>();
		String fullName = "";
		String nickName = "";
		if (isElementLoaded(nameOfProfile, 25)) {
			waitForSeconds(3);
			String names = nameOfProfile.getText().trim();
			if (names.contains("\"")) {
				String[] allNames = nameOfProfile.getText().replaceAll("\"", "").trim().split("\n");
				fullName = allNames[0];
				if(allNames.length == 2){
					nickName = allNames[1];
				}
				userProfileNames.put("fullName", fullName);
				userProfileNames.put("nickName", nickName);
				SimpleUtils.pass("Get user profile names successfully! ");
			} else {
				userProfileNames.put("fullName", names);
				SimpleUtils.pass("Get user profile names successfully! ");
			}
		} else
			SimpleUtils.fail("Names on user profile failed to load! ", false);
		return userProfileNames;
	}

	//added by Haya
	@FindBy(css = "span[ng-click=\"getNextWeekData()\"]")
	private WebElement nextWeekBtn;
	@Override
	public void clickNextWeek() throws Exception {
		if (isElementLoaded(nextWeekBtn,10)){
			click(nextWeekBtn);
		}
	}

	@Override
	public void clickPreviousWeek() throws Exception {
		if (isElementLoaded(pastWeekArrow,10)){
			click(pastWeekArrow);
		}
	}

	//added by Haya
	@Override
	public String getAvailabilityWeek() throws Exception {
		WebElement dateSpan = myAvailability.findElement(By.cssSelector(".week-nav-icon-main.ng-binding"));
		if (isElementLoaded(dateSpan,5)){
			return dateSpan.getText();
		} else {
			SimpleUtils.fail("Fail to load date info for availability!", true);
		}
		return null;
	}

	@FindBy(css = "[box-title=\"User Profile\"]")
	private WebElement userProfileSection;

	@FindBy(css = "[box-title=\"HR Profile Information\"]")
	private WebElement hrProfileInfoSection;

	@FindBy(css = "[box-title=\"Legion Information\"]")
	private WebElement legionInfoSection;

	@FindBy(css = "[box-title=\"Actions\"]")
	private WebElement actionsSection;

	@FindBy(css = "[value=\"userProfileInfo.pictureUrl\"]")
	private WebElement primaryAvatarInUserProfileSection;

	@FindBy(css = "[value=\"userProfileInfo.businessPictureUrl\"]")
	private WebElement businessAvatarInUserProfileSection;

	@FindBy(css = "div.user-readonly-details")
	private List<WebElement> userProfileInfoInUserProfileSection;

	@FindBy(css = ".quick-engagement .label")
	private List<WebElement> fieldsInHRProfileInformationSection;

	@FindBy(css = "[box-title=\"Legion Information\"] .col-xs-6.label")
	private List<WebElement> fieldsInLegionInformationSection;

	@FindBy(css = ".information-section.badge-section div")
	private WebElement badgesSectionInLegionInformationSection;

	@FindBy(css = "lg-button[ng-click=\"invite()\"]")
	private WebElement inviteToLegionButton;

	@FindBy(css = "div.invitation-status")
	private WebElement inviteMessageInActionsSection;

	@FindBy(css = "lg-button[ng-click=\"$ctrl.conformation($ctrl.sendUsername)\"]")
	private WebElement sendUsernameInActionsSection;

	@FindBy(css = "lg-button[ng-click=\"$ctrl.conformation($ctrl.resetPassword)\"]")
	private WebElement resetPasswordInActionsSection;

	@FindBy(css = "lg-button[ng-click=\"editUserProfile()\"]")
	private WebElement editUserProfileButton;

	@FindBy(css = "button.lgn-action-button-light")
	private WebElement syncTMInfoButton;

	@FindBy(css = "lg-button[ng-click=\"changePassword()\"]")
	private WebElement changePasswordButton;

	@FindBy(css = ".console-navigation-item-label")
	private List<WebElement> navigationTabs;



	public void verifyEditUserProfileButtonIsLoaded() throws Exception {
		if(isElementLoaded(editUserProfileButton, 5)){
			SimpleUtils.pass("User Profile page: Edit user profile button loaded successfully! ");
		} else {
			SimpleUtils.fail("User Profile page: Edit user profile button fail to load!", false);
		}
	}

	public void verifySyncTMInfoButtonIsLoaded() throws Exception {
		if(isElementLoaded(syncTMInfoButton, 5)){
			SimpleUtils.pass("User Profile page: Sync TM info button loaded successfully! ");
		} else {
			SimpleUtils.report("User Profile page: Sync TM info button  not loaded, please check the integration setting!");
		}
	}

	public void verifyUserProfileSectionIsLoaded() throws Exception {
		if(isElementLoaded(userProfileSection, 10)){
			SimpleUtils.pass("User Profile page: User Profile section loaded successfully! ");
		} else {
			SimpleUtils.fail("User Profile page: User Profile section fail to load!", false);
		}
	}

	public void verifyHRProfileInformationSectionIsLoaded() throws Exception {
		if(isElementLoaded(hrProfileInfoSection, 5)){
			SimpleUtils.pass("User Profile page: HR Profile Information section loaded successfully! ");
		} else {
			SimpleUtils.fail("User Profile page: HR Profile Information section fail to load!", false);
		}
	}

	@FindBy(css = ".field-content")
	private List<WebElement> profileInfoFields;
	@Override
	public Map<String, String> getHRProfileInfo() throws Exception {
		Map<String, String> result = new HashMap<>();
		if(isElementLoaded(hrProfileInfoSection, 5) && areListElementVisible(profileInfoFields, 10)){
			for (WebElement element: profileInfoFields){
				try {
					result.put(element.findElement(By.cssSelector(".label")).getText(), element.findElement(By.cssSelector(".value")).getText());
				} catch (Exception e) {
					result.put(element.findElement(By.cssSelector(".label")).getText(), "");
				}
			}
		} else {
			SimpleUtils.fail("User Profile page: HR Profile Information section fail to load!", false);
		}
		return result;
	}

	public void verifyLegionInformationSectionIsLoaded() throws Exception {
		if(isElementLoaded(legionInfoSection, 5)){
			SimpleUtils.pass("User Profile page: Legion Information section loaded successfully! ");
		} else {
			SimpleUtils.fail("User Profile page: Legion Information section fail to load!", false);
		}
	}

	public void verifyActionSectionIsLoaded() throws Exception {
		if(isElementLoaded(actionsSection, 5)){
			SimpleUtils.pass("User Profile page: Actions section loaded successfully! ");
		} else {
			SimpleUtils.fail("User Profile page: Actions section fail to load!", false);
		}
	}

	public void verifyFieldsInUserProfileSection() throws Exception {
		if (isElementLoaded(primaryAvatarInUserProfileSection, 5) &&
				isElementLoaded(businessAvatarInUserProfileSection, 5) &&
				areListElementVisible(userProfileInfoInUserProfileSection, 5)
				&& userProfileInfoInUserProfileSection.size() ==3
				&& userProfileInfoInUserProfileSection.get(0).findElement(By.cssSelector(".userProfileHeading")).getText().equalsIgnoreCase("Name")
				&& userProfileInfoInUserProfileSection.get(1).findElement(By.cssSelector(".userProfileHeading")).getText().equalsIgnoreCase("Address")
				&& userProfileInfoInUserProfileSection.get(2).findElement(By.cssSelector(".userProfileHeading")).getText().equalsIgnoreCase("CONTACT INFORMATION")) {
			SimpleUtils.pass("User Profile page: The fields in User Profile section display correctly! ");
		} else
			SimpleUtils.fail("User Profile page: The fields in User Profile section failed to display !", false);
	}

	public void verifyFieldsInHRProfileInformationSection() throws Exception {
		if (areListElementVisible(fieldsInHRProfileInformationSection, 5)) {
			if (fieldsInHRProfileInformationSection.size() == 14) {
				if (fieldsInHRProfileInformationSection.get(0).getText().equalsIgnoreCase("Name")
						&& fieldsInHRProfileInformationSection.get(1).getText().equalsIgnoreCase("JOB TITLE")
						&& fieldsInHRProfileInformationSection.get(2).getText().equalsIgnoreCase("SENIORITY")
						&& fieldsInHRProfileInformationSection.get(3).getText().equalsIgnoreCase("MANAGER NAME")
						&& fieldsInHRProfileInformationSection.get(4).findElement(By.cssSelector("span.highlight-when-help-mode-is-on")).getText().equalsIgnoreCase("HOME STORE")
						&& fieldsInHRProfileInformationSection.get(5).findElement(By.cssSelector("span.highlight-when-help-mode-is-on")).getText().equalsIgnoreCase("EMPLOYEE ID")
						&& fieldsInHRProfileInformationSection.get(6).getText().equalsIgnoreCase("DATE HIRED")
						&& fieldsInHRProfileInformationSection.get(7).getText().equalsIgnoreCase("EMPLOYMENT TYPE")
						&& fieldsInHRProfileInformationSection.get(8).getText().equalsIgnoreCase("HOURLY RATE")
						&& fieldsInHRProfileInformationSection.get(9).getText().equalsIgnoreCase("EMPLOYMENT STATUS")
						&& fieldsInHRProfileInformationSection.get(10).getText().equalsIgnoreCase("EXEMPT")
						&& fieldsInHRProfileInformationSection.get(11).getText().equalsIgnoreCase("ADDRESS")
						&& fieldsInHRProfileInformationSection.get(12).getText().equalsIgnoreCase("MINOR")
						&& fieldsInHRProfileInformationSection.get(13).getText().equalsIgnoreCase("CONTACT INFORMATION")) {
					SimpleUtils.pass("User Profile page: The fields in HR Profile Information section display correctly! ");
				} else
					SimpleUtils.fail("User Profile page: The fields in HR Profile Information section failed to display !", false);
			} else if (fieldsInHRProfileInformationSection.size() == 13) {
				if (fieldsInHRProfileInformationSection.get(0).getText().equalsIgnoreCase("Name")
						&& fieldsInHRProfileInformationSection.get(1).getText().equalsIgnoreCase("JOB TITLE")
						&& fieldsInHRProfileInformationSection.get(2).getText().equalsIgnoreCase("MANAGER NAME")
						&& fieldsInHRProfileInformationSection.get(3).findElement(By.cssSelector("span.highlight-when-help-mode-is-on")).getText().equalsIgnoreCase("HOME STORE")
						&& fieldsInHRProfileInformationSection.get(4).findElement(By.cssSelector("span.highlight-when-help-mode-is-on")).getText().equalsIgnoreCase("EMPLOYEE ID")
						&& fieldsInHRProfileInformationSection.get(5).getText().equalsIgnoreCase("DATE HIRED")
						&& fieldsInHRProfileInformationSection.get(6).getText().equalsIgnoreCase("EMPLOYMENT TYPE")
						&& fieldsInHRProfileInformationSection.get(7).getText().equalsIgnoreCase("HOURLY RATE")
						&& fieldsInHRProfileInformationSection.get(8).getText().equalsIgnoreCase("EMPLOYMENT STATUS")
						&& fieldsInHRProfileInformationSection.get(9).getText().equalsIgnoreCase("EXEMPT")
						&& fieldsInHRProfileInformationSection.get(10).getText().equalsIgnoreCase("ADDRESS")
						&& fieldsInHRProfileInformationSection.get(11).getText().equalsIgnoreCase("MINOR")
						&& fieldsInHRProfileInformationSection.get(12).getText().equalsIgnoreCase("CONTACT INFORMATION")) {
					SimpleUtils.pass("User Profile page: The fields in HR Profile Information section display correctly! ");
				} else
					SimpleUtils.fail("User Profile page: The fields in HR Profile Information section failed to display !", false);
			}else
				SimpleUtils.fail("User Profile page: The fields in HR Profile Information section failed to display !", false);
		} else
			SimpleUtils.fail("User Profile page: The fields in HR Profile Information section failed to display !", false);
	}

	public void verifyFieldsInLegionInformationSection() throws Exception {
		boolean isTimeSheetTabLoaded = isTimeSheetLoaded();
		if (isTimeSheetTabLoaded) {
			if (areListElementVisible(fieldsInLegionInformationSection, 5)
					&& fieldsInLegionInformationSection.size() == 4
					&& fieldsInLegionInformationSection.get(0).getText().equalsIgnoreCase("STATUS")
					&& fieldsInLegionInformationSection.get(1).getText().equalsIgnoreCase("SCHEDULING POLICY GROUP")
					&& fieldsInLegionInformationSection.get(2).getText().equalsIgnoreCase("ONBOARDING STATUS")
					&& fieldsInLegionInformationSection.get(3).findElement(By.cssSelector(".highlight-when-help-mode-is-on")).getText().equalsIgnoreCase("TIMECLOCK PIN")
					&& isElementLoaded(badgesSectionInLegionInformationSection, 5)
					&& badgesSectionInLegionInformationSection.getText().equalsIgnoreCase("Badges")) {
				SimpleUtils.pass("User Profile page: The fields in Legion Information section display correctly! ");
			} else
				SimpleUtils.fail("User Profile page: The fields in Legion Information section failed to display !", false);
		} else {
			if (areListElementVisible(fieldsInLegionInformationSection, 5)
					&& fieldsInLegionInformationSection.size() >= 2
					&& fieldsInLegionInformationSection.get(0).getText().equalsIgnoreCase("STATUS")
					&& fieldsInLegionInformationSection.get(1).getText().equalsIgnoreCase("SCHEDULING POLICY GROUP")
					&& isElementLoaded(badgesSectionInLegionInformationSection, 5)
					&& badgesSectionInLegionInformationSection.getText().equalsIgnoreCase("Badges")) {
				SimpleUtils.pass("User Profile page: The fields in Legion Information section display correctly! ");
			} else
				SimpleUtils.fail("User Profile page: The fields in Legion Information section failed to display !", false);
		}
	}

	private boolean isTimeSheetLoaded() throws Exception {
		boolean isLoaded = false;
		if (areListElementVisible(navigationTabs, 5)) {
			for (WebElement navigationTab : navigationTabs) {
				if (navigationTab.getText().toLowerCase().equals("timesheet")) {
					isLoaded = true;
					SimpleUtils.report("Timesheet tab is loaded!");
					break;
				}
			}
		}
		return isLoaded;
	}

	@FindBy(css = "[label=\"Activate\"] button")
	private WebElement activateBtn;
	@FindBy(css = ".timeclock__container.value.ng-binding")
	private WebElement onboardingStatusValue;
	@FindBy(css = "[label=\"Suspend Access\"] button")
	private WebElement suspendAccessButton;
	public void verifyContentsInActionsSection() throws Exception {
		if (isElementLoaded(inviteToLegionButton, 5)){
			String inviteButtonMessage = inviteToLegionButton.getText();
			if (inviteButtonMessage.contains("ReInvite")){
				if (isElementLoaded(showOrHideInvitationCodeButtonHeader, 5)) {
					if (inviteMessageInActionsSection.getText().contains("Invited to onboard")){
						SimpleUtils.pass("User Profile page: The invite message in Actions section display correctly! ");
					} else{
						SimpleUtils.fail("User Profile page: The invite message in Action section failed to display! ", false);
					}
				}
			} else {
				if (isElementLoaded(inviteMessageInActionsSection, 10)
						&& inviteMessageInActionsSection.getText().contains("Not invited yet")){
					SimpleUtils.pass("User Profile page: The invite message in Actions section display correctly! ");
				} else{
					SimpleUtils.fail("User Profile page: The invite message in Action section failed to display! ", false);
				}
			}

		} else if (checkIfReviewPreferencesInnerBoxDisplay()) {
			if (isElementLoaded(activateBtn, 5)) {
				SimpleUtils.pass("User Profile page: The activate button in Actions section display correctly! ");
			} else{
				SimpleUtils.fail("User Profile page: The activate button in Action section failed to display! ", false);
			}
		}else if (isElementLoaded(onboardingStatusValue, 5)
				&& onboardingStatusValue.getText().equalsIgnoreCase("Ready To Onboard")){
			scrollToBottom();
			if (isElementLoaded(sendUsernameInActionsSection, 5) && isElementLoaded(resetPasswordInActionsSection, 5)){
				SimpleUtils.pass("User Profile page: The Send Username and Reset Password buttons in Actions section display correctly! ");
			} else {
				SimpleUtils.fail("User Profile page: The Send Username and Reset Password buttons in Actions section failed to display !", false);
			}
		} else{
			scrollToBottom();
			if (isElementLoaded(suspendAccessButton, 5)){
				SimpleUtils.pass("User Profile page: The Suspend Access buttons in Actions section display correctly! ");
			} else {
				SimpleUtils.fail("User Profile page: The Suspend Access buttons in Actions section failed to display !", false);
			}
		}
	}

	public void verifyContentsInActionsSectionInTMView() throws Exception {
		if (isElementLoaded(changePasswordButton, 5)){
			SimpleUtils.pass("User Profile page: The change password button in Actions section display correctly! ");
		} else{
			SimpleUtils.fail("User Profile page: The change password button in Actions section display correctly! ", false);
		}
	}

	//added by Haya
	@FindBy(css = "span[ng-if=\"$ctrl.input.$error.required\"]")
	private List<WebElement> mandatoryFieldsErrorMessage;
	@Override
	public void isRequiredErrorShowUp(String field) throws Exception {
		if (areListElementVisible(mandatoryFieldsErrorMessage,10) && mandatoryFieldsErrorMessage.size()>0){
			for (WebElement element: mandatoryFieldsErrorMessage){
				if (element.getText().contains(field) && isSaveBtnDisabled()){
					SimpleUtils.pass(field+" is a mandatory field!");
				}
			}
		} else {
			SimpleUtils.fail("No mandatory fields!", false);
		}
	}

	@Override
	public boolean isSaveBtnDisabled() throws Exception {
		if(areListElementVisible(profileSection.findElements(By.cssSelector("button[disabled=\"disabled\"]")), 5)){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void verifyHRProfileSectionIsNotEditable() throws Exception {
		if (areListElementVisible(profileSection.findElements(By.cssSelector("sub-content-box[box-title=\"HR Profile Information\"] input")),5) ||
				areListElementVisible(profileSection.findElements(By.cssSelector("sub-content-box[box-title=\"HR Profile Information\"] select")),5)){
			SimpleUtils.fail("Fields in HR profile section should not be editable!",false);
		} else {
			String s = profileSection.findElement(By.cssSelector("sub-content-box[box-title=\"HR Profile Information\"]")).getText();
			if (s.contains("NAME")&&s.contains("JOB TITLE")&&s.contains("MANAGER NAME")&&s.contains("HOME STORE")&&s.contains("EMPLOYEE ID")&&s.contains("DATE HIRED")&&s.contains("EMPLOYMENT TYPE")
					&&s.contains("HOURLY RATE")&&s.contains("EMPLOYMENT STATUS")&&s.contains("EXEMPT")&&s.contains("ADDRESS")&&s.contains("MINOR")&&s.contains("CONTACT INFORMATION")){
				SimpleUtils.pass("Fields in HR profile section are existed and not editable!");
			} else {
				SimpleUtils.fail("Some fields you want in HR profile section are not loaded!",false);
			}
		}
	}

	@Override
	public void verifyLegionInfoSectionIsNotEditable() throws Exception {
		if (areListElementVisible(profileSection.findElements(By.cssSelector("sub-content-box[box-title=\"Legion Information\"] input")),5) ||
				areListElementVisible(profileSection.findElements(By.cssSelector("sub-content-box[box-title=\"Legion Information\"] select")),5)){
			SimpleUtils.fail("Fields in Legion Information section should not be editable!",false);
		} else {
			String s =profileSection.findElement(By.cssSelector("sub-content-box[box-title=\"Legion Information\"]")).getText();
			if (s.contains("STATUS")&&s.contains("SCHEDULING POLICY GROUP")&&s.contains("TIMECLOCK PIN")){
				SimpleUtils.pass("Fields in Legion Information section are existed and not editable!");
			} else {
				SimpleUtils.fail("Some fields you want in HR profile section are not loaded!",false);
			}
		}
	}

	@Override
	public void verifyTheEmailFormatInProfilePage(List<String> testEmails) throws Exception {
		String regex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}" +
				"\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,10}))$";
		String errorMessage = "Email is invalid.";
		if (isElementEnabled(profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")), 5) && testEmails.size() > 0) {
			for (String testEmail : testEmails) {
				profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")).clear();
				profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")).sendKeys(testEmail);
				if (!testEmail.matches(regex)) {
					if(areListElementVisible(saveBtnsOfProfile, 5)){
						scrollToElement(saveBtnsOfProfile.get(0));
						clickTheElement(saveBtnsOfProfile.get(0));
						verifyEmailAddressInvalidAlertDialog();
					}
				}
			}
		}else {
			SimpleUtils.fail("Email Input failed to load!", true);
		}
	}

	@Override
	public boolean ifMatchEmailRegex(String email) throws Exception {
		String regex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}" +
				"\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,10}))$";
		if (email.matches(regex)){
			return true;
		}
		return false;
	}

	private void verifyEmailAddressInvalidAlertDialog() throws Exception{
		if (isElementLoaded(alertDialog,10) && alertDialog.findElement(By.cssSelector(".lgn-alert-message.ng-scope.warning")).getText().contains("Email address invalid")){
			clickOnOKBtnOnAlert();
			SimpleUtils.pass("Email is valid so can not save successfully!");
		} else {
			SimpleUtils.fail("No alert dialog for invalid email format!",false);
		}
	}

	@Override
	public HashMap<String, String> getValuesOfFields() throws Exception{
		waitForSeconds(3);
		HashMap<String, String> results= new HashMap<String,String>();
		if (isElementLoaded(profileSection, 5)) {
			// Home Address Street Address 1
			if (isElementLoaded(profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")), 5)) {
				SimpleUtils.pass("Home Address loaded!");
				results.put("address1",profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")).getAttribute("value"));
			} else {
				SimpleUtils.fail("No Home Address field!",false);
			}

			// Home Address Street Address 2
			if (isElementLoaded(profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")), 5)) {
				SimpleUtils.pass("Home address2 loaded!");
				results.put("address2",profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")).getAttribute("value"));
			} else {
				SimpleUtils.fail("No Home address2 field!",false);
			}

			// City
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")), 5)) {
				SimpleUtils.pass("City loaded!");
				results.put("City",profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")).getAttribute("value"));
			} else {
				SimpleUtils.fail("No City field!",false);
			}

			try {
				// State
				if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"State\"] select")), 5)) {
					SimpleUtils.pass("State loaded!");
					Select statesDropdown = new Select(profileSection.findElement(By.cssSelector("input-field[label=\"State\"] select")));
					results.put("State", statesDropdown.getFirstSelectedOption().getText());
					//selectByVisibleText(profileSection.findElement(By.cssSelector("input-field[label=\"State\"] select")), state);
				} else if (isElementLoaded(getDriver().findElement(By.cssSelector("input-field[label=\"Province\"] select")), 5)) {
					SimpleUtils.pass("Province loaded!");
					Select statesDropdown = new Select(profileSection.findElement(By.cssSelector("input-field[label=\"Province\"] select")));
					results.put("State", statesDropdown.getFirstSelectedOption().getText());
				} else {
					SimpleUtils.fail("No State field!", false);
				}
			} catch (Exception e) {
				// Do nothing
			}

			// Zip Code
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")), 5)) {
				SimpleUtils.pass("Zip Code loaded!");
				results.put("Zip Code",profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")).getAttribute("value"));
			} else {
				SimpleUtils.fail("No Zip Code field!",false);
			}

			// Country
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Country\"] select")), 5)) {
				SimpleUtils.pass("Country loaded!");
				Select countryDropdown = new Select(profileSection.findElement(By.cssSelector("input-field[label=\"Country\"] select")));
				results.put("Country",countryDropdown.getFirstSelectedOption().getText());
			} else {
				SimpleUtils.fail("No Country field!",false);
			}

			//First Name
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")),5)) {
				SimpleUtils.pass("First name field loaded!");
				results.put("First Name",profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")).getAttribute("value"));
				//verify it is a mandatory field.
				profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")).clear();
				isRequiredErrorShowUp("First Name");
				profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")).sendKeys(results.get("First Name"));
			} else {
				SimpleUtils.fail("No first name field!",false);
			}

			// Last Name
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")),5)) {
				SimpleUtils.pass("Last name field loaded!");
				results.put("Last Name",profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")).getAttribute("value"));
				//verify it is a mandatory field.
				profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")).clear();
				isRequiredErrorShowUp("Last Name");
				profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")).sendKeys(results.get("Last Name"));
			} else {
				SimpleUtils.fail("No last name field!",false);
			}

			// Nick Name
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Nickname\"] input")),5)) {
				SimpleUtils.pass("Nick name field loaded!");
				results.put("Nickname",profileSection.findElement(By.cssSelector("input-field[label=\"Nickname\"] input")).getAttribute("value"));
			} else {
				SimpleUtils.fail("No nick name field!",false);
			}

			// Phone
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Phone\"] input")),5)) {
				SimpleUtils.pass("Phone field loaded!");
				results.put("Phone",profileSection.findElement(By.cssSelector("input-field[label=\"Phone\"] input")).getAttribute("value"));
			} else {
				SimpleUtils.fail("No Phone field!",false);
			}

			// Email
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")),5)) {
				SimpleUtils.pass("Email field loaded!");
				String email = profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")).getAttribute("value");
				results.put("E-mail",profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")).getAttribute("value"));
				//verify it is a mandatory field.
				profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")).clear();
				isRequiredErrorShowUp("E-Mail");
				profileSection.findElement(By.cssSelector("input-field[label=\"E-mail\"] input")).sendKeys(email);
			} else {
				SimpleUtils.fail("No Email field!",false);
			}
		}else{
			SimpleUtils.fail("Profile section fail to load!",false);
		}
		return results;
	}

	@Override
	public void updateAllFields(HashMap<String, String> values) throws Exception {
		if (isElementLoaded(profileSection, 5)) {
			// Home Address Street Address 1
			if (isElementLoaded(profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")), 5)) {
				profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")).clear();
				profileSection.findElement(By.cssSelector("double-input input-field[label=\"Home Address\"] input")).sendKeys(values.get("address1"));
				SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 1' updated with value: '"
						+ values.get("address1") + "'.");
			} else {
				SimpleUtils.fail("No Home Address field!",false);
			}

			// Home Address Street Address 2
			if (isElementLoaded(profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")), 5)) {
				profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")).clear();
				profileSection.findElement(By.cssSelector("double-input input-field[class=\"address2 ng-scope ng-isolate-scope\"] input")).sendKeys(values.get("address2"));
				SimpleUtils.pass("Profile Page: User Profile Home Address 'Street Address 2' updated with value: '"
						+ values.get("address2") + "'.");
			} else {
				SimpleUtils.fail("No Home address2 field!",false);
			}

			// City
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")), 5)) {
				profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")).clear();
				profileSection.findElement(By.cssSelector("input-field[label=\"City\"] input")).sendKeys(values.get("City"));
				SimpleUtils.pass("Profile Page: User Profile Home Address 'City' updated with value: '" + values.get("City") + "'.");
			} else {
				SimpleUtils.fail("No City field!",false);
			}

			try {
				// State
				if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"State\"] select")), 5)) {
					selectByVisibleText(profileSection.findElement(By.cssSelector("input-field[label=\"State\"] select")), values.get("State"));
					SimpleUtils.pass("Profile Page: User Profile 'State' updated with value: '" + values.get("State") + "'.");
				} else if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Province\"] select")), 5)) {
					selectByVisibleText(profileSection.findElement(By.cssSelector("input-field[label=\"Province\"] select")), values.get("State"));
					SimpleUtils.pass("Profile Page: User Profile 'State' updated with value: '" + values.get("State") + "'.");
				} else {
					SimpleUtils.fail("No State field!", false);
				}
			} catch (Exception e) {
				// Do nothing
			}

			// Zip Code
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")), 5)) {
				profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")).clear();
				profileSection.findElement(By.cssSelector("input-field[label=\"Zip Code\"] input")).sendKeys(values.get("Zip Code"));
				SimpleUtils.pass("Profile Page: User Profile 'Zip' updated with value: '" + values.get("Zip Code") + "'.");
			} else {
				SimpleUtils.fail("No Zip Code field!",false);
			}

			// Country
			if (isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Country\"] select")), 5)) {
				if (!values.get("Country").equals("")){
					selectByVisibleText(profileSection.findElement(By.cssSelector("input-field[label=\"Country\"] select")), values.get("Country"));
					SimpleUtils.pass("Profile Page: User Profile 'Country' updated with value: '" + values.get("Country") + "'.");
				}
			} else {
				SimpleUtils.fail("No Country field!",false);
			}

			//First Name
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")),5)) {
				profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")).clear();
				if (values.get("First Name").equals("") && values.get("First Name")==null){
					profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")).sendKeys("First");
				} else {
					profileSection.findElement(By.cssSelector("input-field[label=\"First Name\"] input")).sendKeys(values.get("First Name"));
					SimpleUtils.pass("Profile Page: User Profile 'First Name' updated with value: '"+values.get("First Name")+"'.");
				}
			} else {
				SimpleUtils.fail("No first name field!",false);
			}

			// Last Name
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")),5)) {
				profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")).clear();
				if (values.get("First Name").equals("") && values.get("First Name")==null){
					profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")).sendKeys("Last");
				} else {
					profileSection.findElement(By.cssSelector("input-field[label=\"Last Name\"] input")).sendKeys(values.get("Last Name"));
					SimpleUtils.pass("Profile Page: User Profile 'Last Name' updated with value: '"+values.get("Last Name")+"'.");
				}
			} else {
				SimpleUtils.fail("No last name field!",false);
			}

			// Nick Name
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Nickname\"] input")),5)) {
				profileSection.findElement(By.cssSelector("input-field[label=\"Nickname\"] input")).clear();
				profileSection.findElement(By.cssSelector("input-field[label=\"Nickname\"] input")).sendKeys(values.get("Nickname"));
				SimpleUtils.pass("Profile Page: User Profile 'Nick Name' updated with value: '"+values.get("Nickname")+"'.");
			} else {
				SimpleUtils.fail("No nick name field!",false);
			}

			// Phone
			if(isElementLoaded(profileSection.findElement(By.cssSelector("input-field[label=\"Phone\"] input")),5)) {
				profileSection.findElement(By.cssSelector("input-field[label=\"Phone\"] input")).clear();
				profileSection.findElement(By.cssSelector("input-field[label=\"Phone\"] input")).sendKeys(values.get("Phone"));
				SimpleUtils.pass("Profile Page: User Profile Contact 'Phone' updated with value: '"+values.get("Phone")+"'.");
			} else {
				SimpleUtils.fail("No Phone field!",false);
			}
		}else{
			SimpleUtils.fail("Profile section fail to load!",false);
		}
	}

	@Override
	public void clickOnOKBtnOnAlert() throws Exception {
		if (isElementLoaded(alertDialog.findElement(By.cssSelector("button")),5)){
			click(alertDialog.findElement(By.cssSelector("button")));
			SimpleUtils.pass("OK button clicked!");
		} else {
			SimpleUtils.fail("No OK button!",false);
		}
	}

	@FindBy(css = ".lg-badges button")
	private WebElement manageBadgeBtn;
	@Override
	public boolean verifyManageBadgeBtn() throws Exception {
		if (isElementLoaded(manageBadgeBtn,10)){
			scrollToElement(manageBadgeBtn);
			click(manageBadgeBtn);
			return true;
		} else {
			return false;
		}
	}

	@FindBy(css = "div.lgnCheckBox")
	private List<WebElement> checkBoxOfBadge;
	@Override
	public void verifySelectBadge() throws Exception {
		if (areListElementVisible(checkBoxOfBadge,5)){
			for (WebElement element: checkBoxOfBadge){
				if (element.getAttribute("class").contains("checked")){
					click(element);
				}
			}
			//select the first one
			click(checkBoxOfBadge.get(0));
			SimpleUtils.pass("The first badge is selected!");
		} else {
			SimpleUtils.fail("No checkbox for badge!",false);
		}
	}

	@FindBy(css = ".lgn-action-button-success")
	private WebElement saveBtnForBadge;
	@Override
	public void saveBadgeBtn() throws Exception {
		if (isElementLoaded(saveBtnForBadge,5)){
			click(saveBtnForBadge);
			SimpleUtils.pass("Save button is clicked!");
		}else{
			SimpleUtils.fail("Save button is not loaded!", false);
		}
	}

	@FindBy(css = ".lgn-action-button-default")
	private WebElement cancelBtnForBadge;
	@Override
	public void cancelBadgeBtn() throws Exception {
		if (isElementLoaded(cancelBtnForBadge,5)){
			click(cancelBtnForBadge);
			SimpleUtils.pass("Save button is clicked!");
		}else{
			SimpleUtils.fail("Save button is not loaded!", false);
		}
	}

	@FindBy(css = "[ng-click=\"showInvitationCode.value = !showInvitationCode.value\"]")
	private WebElement showOrHideInvitationCodeButton;

	@FindBy(css = "[ng-if=\"showInvitationCode.value\"]")
	private WebElement invitationCode;

	@FindBy(css = "div.header-buttons-invite-code-wrapper")
	private WebElement showOrHideInvitationCodeButtonHeader;


	public void clickOnShowOrHideInvitationCodeButton(boolean toShowCode) throws Exception {
		if (isElementLoaded(showOrHideInvitationCodeButton,5)){
			if(toShowCode){
				if(showOrHideInvitationCodeButton.getText().equalsIgnoreCase("Show Invitation Code")){
					clickTheElement(showOrHideInvitationCodeButton);
				}
			} else{
				if(showOrHideInvitationCodeButton.getText().equalsIgnoreCase("Hide Code")){
					clickTheElement(showOrHideInvitationCodeButton);
				}
			}
			SimpleUtils.pass("Show Or Hide Invitation Code button is clicked!");
		}else{
			SimpleUtils.fail("Show Or Hide Invitation Code button is not loaded!", false);
		}
	}

	public String getInvitationCode() throws Exception {
		String invitationCodeValue = "";
		if (isElementLoaded(invitationCode,5)){
			invitationCodeValue = invitationCode.getText();
			SimpleUtils.pass("Get invitation Code successfully!");
		}else{
			SimpleUtils.fail("Invitation Code is not loaded!", false);
		}
		return invitationCodeValue;
	}

	public boolean isInvitationCodeLoaded() throws Exception {
		boolean isInvitationCodeLoaded = false;
		if (isElementLoaded(invitationCode,5)){
			isInvitationCodeLoaded = true;
			SimpleUtils.report("Invitation Code is loaded!");
		}else{
			SimpleUtils.report("Invitation Code is not loaded!");
		}
		return isInvitationCodeLoaded;
	}

	public String getShowOrHideInvitationCodeButtonTooltip() throws Exception {
		String tooltip = "";
		if(isElementLoaded(showOrHideInvitationCodeButtonHeader, 5)){
			tooltip = showOrHideInvitationCodeButtonHeader.getAttribute("data-tootik");
			SimpleUtils.pass("Get tooltip of Show Or Hide Invitation Code button successfully!");
		} else
			SimpleUtils.fail("Show Or Hide Invitation Code button is not loaded!", false);
		return tooltip;
	}

	@Override
	public boolean isInviteToLegionButtonLoaded() throws Exception {
		boolean isInviteToLegionButtonLoaded = false;
		if(isElementLoaded(userProfileInviteBtn, 10)) {
			isInviteToLegionButtonLoaded =true;
			SimpleUtils.report("Profile Page: Invite To Legion Button loaded successfully.");
		} else
			SimpleUtils.report("Profile Page: Invite To Legion Button failed to load.");

		return isInviteToLegionButtonLoaded;
	}

	@Override
	public boolean isShowOrHideInvitationCodeButtonLoaded() throws Exception {
		boolean isShowOrHideInvitationCodeButtonLoaded = false;
		if(isElementLoaded(showOrHideInvitationCodeButton, 5)) {
			isShowOrHideInvitationCodeButtonLoaded =true;
			SimpleUtils.report("Profile Page: Show Or Hide Invitation Code loaded successfully.");
		} else
			SimpleUtils.report("Profile Page: Show Or Hide Invitation Code failed to load.");

		return isShowOrHideInvitationCodeButtonLoaded;
	}

	public void createTimeOffOnSpecificDays(String timeOffReasonLabel, String timeOffExplanationText,String fromDay, int duration) throws Exception {
		final int timeOffRequestCount = timeOffRequestRows.size();
		if (!isNewTimeOffWindowLoaded()) {
			clickOnCreateTimeOffBtn();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
//		String d="2021 Apr 15";
		String d= fromDay;
//		String today=SimpleUtils.getCurrentDateMonthYearWithTimeZone("GMT+8", dateFormat);
		String today=SimpleUtils.getCurrentDateMonthYearWithTimeZone("UTC-7", dateFormat);
//		String today=SimpleUtils.getCurrentDateMonthYearWithTimeZone("GMT-4", dateFormat);
		long to = dateFormat.parse(d).getTime();
		long from = dateFormat.parse(today).getTime();
		int days = (int) ((to - from)/(1000 * 60 * 60 * 24));
		selectTimeOffReason(timeOffReasonLabel);
		updateTimeOffExplanation(timeOffExplanationText);
		selectDate(days);
		selectDate(days+ duration);
		HashMap<String, String> timeOffDate = getTimeOffDate(days, duration);
		String timeOffStartDate = timeOffDate.get("startDateTimeOff");
		String timeOffEndDate = timeOffDate.get("endDateTimeOff");
		setTimeOffStartTime(timeOffStartDate);
		setTimeOffEndTime(timeOffEndDate);
		clickOnSaveTimeOffRequestBtn();
		Thread.sleep(3000);
		if(timeOffRequestRows.size() > timeOffRequestCount)
			SimpleUtils.pass("Profile Page: New Time Off Save Successfully.");
		else
			SimpleUtils.fail("Profile Page: New Time Off not Save Successfully.", false);

	}

	@FindBy(css = "[ng-click=\"removeAvailability()\"]")
	private WebElement removeAvailabilityIcon;

	@FindBy(css = "div.cursor-empty")
	private List<WebElement> emptyAvailabilities;

	@FindBy(css = "span.tooltip-red")
	private WebElement busyAvailabilityToolTip;

	@FindBy(css = "span.tooltip-green")
	private WebElement preferredAvailabilityToolTip;


	public void updatePreferredOrBusyHoursToAllDay(int dayIndex, String hoursType) throws Exception {

		String preferredHoursTabText = "When I prefer to work";
		String busyHoursTabText = "When I prefer not to work";
		WebElement availabilityToolTip = null;
		if(hoursType.toLowerCase().contains(preferredHoursTabText.toLowerCase())) {
			selectMyAvaliabilityEditHoursTabByLabel(preferredHoursTabText);
			availabilityToolTip = preferredAvailabilityToolTip;
		} else {
			selectMyAvaliabilityEditHoursTabByLabel(busyHoursTabText);
			availabilityToolTip = busyAvailabilityToolTip;
		}

		//Delete all availabilities in the day
		WebElement dayRow = null;
		if (areListElementVisible(myAvailabilityDayOfWeekRows, 5) && myAvailabilityDayOfWeekRows.size() == 7) {
			dayRow = myAvailabilityDayOfWeekRows.get(dayIndex);
			List<WebElement> availabilitiesInTheDay = dayRow.findElements(By.cssSelector("div.cursor-resizableW"));
			for (WebElement availability: availabilitiesInTheDay) {
				moveToElementAndClick(availability);
				clickTheElement(removeAvailabilityIcon);
				SimpleUtils.report("Remove one availability successfully! ");
			}
		} else
			SimpleUtils.fail("Profile Page: 'My Availability section' Day of Week Rows not loaded.", false);

		//Click first two empty availabilities
		List<WebElement> emptyAvailabilitiesInTheDay = dayRow.findElements(By.cssSelector("div.cursor-empty"));
		for (int i =0; i< 10; i++) {
			click(emptyAvailabilitiesInTheDay.get(i));
		}

		WebElement rightCell = dayRow.findElement(By.cssSelector("div.cursor-resizableE"));
		int i=0;

		while (!availabilityToolTip.getText().toLowerCase().replace(" ", "").contains("12:00am-12:00am") && i<5){
			//Drag the availability to the end of the day
			scrollToElement(rightCell);
			mouseHoverDragandDrop(rightCell,emptyAvailabilitiesInTheDay.get(emptyAvailabilitiesInTheDay.size()-1));
			i++;
			waitForSeconds(2);
		}

		if (!availabilityToolTip.getText().toLowerCase().replace(" ", "").contains("12:00am-12:00am")) {
//			mouseHoverDragandDrop(rightCell,emptyAvailabilitiesInTheDay.get(emptyAvailabilitiesInTheDay.size()-1));
			SimpleUtils.fail("Update availabilities fail! ", false);
		} else
			SimpleUtils.report("Update availabilities successfully! ");
	}


	public void clickAvailabilityEditButton() throws Exception{
		scrollToBottom();
		if (isElementLoaded(editBtn,10)){
			moveToElementAndClick(editBtn);
			SimpleUtils.pass("Click Edit button successfully!");
		}else{
			SimpleUtils.fail("Edit button is not loaded!", false);
		}
	}

	@FindBy(css = "div.modal-dialog")
	private WebElement warningDialog;
	@Override
	public boolean verifyErrorMessageForEditAvailabilityShowsUpOrNot() throws Exception {
		String s = warningDialog.findElement(By.cssSelector(".lgn-alert-message")).getText();
		if (isElementLoaded(warningDialog, 10) && warningDialog.findElement(By.cssSelector(".lgn-alert-message")).getText().equalsIgnoreCase("This Team Member current has an open Recurring Availability Request. Please approve or deny the request before making any changes")){
			return true;
		}
		return false;
	}

	@FindBy(tagName = "lg-eg-status")
	private WebElement statusOnProfilePage;

	public String getStatusOnProfilePage () throws Exception {
		String status = "";
		if (isElementLoaded(statusOnProfilePage, 10)){
			status = statusOnProfilePage.getAttribute("type");
			SimpleUtils.pass("Get status from profile page successfully! ");
		} else
			SimpleUtils.fail("Status on profile page fail to load! ", false);
		return status;
	}

	//Added by Estelle to get home store location
	@FindBy(css = "div[ng-if=\"canViewWorkerEngagement\"]")
	private WebElement hrProfileInfoForm;
	@Override
	public HashMap<String, String> getOneUserHRProfileInfo() throws Exception {
		HashMap<String, String> userProfileEngagementDetails = new HashMap<>();
		if(isElementLoaded(hrProfileInfoForm)) {
			List<WebElement> rows = hrProfileInfoForm.findElements(By.cssSelector("div.row"));

			for(int index = 0; index < rows.size() ; index++) {
				if(rows.get(index).getText().toLowerCase().contains("name")
						&& rows.get(index).getText().toLowerCase().contains("job title")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 0) {
						userProfileEngagementDetails.put("name", rowValues[0]);
						userProfileEngagementDetails.put("jobTitle", rowValues[1]);
					} else
						SimpleUtils.fail("Profile Page: Unable to get Name and Job Title value from ' HR Profile Infomation form'", false);

				}
				else if(rows.get(index).getText().toLowerCase().contains("manager name")
						&& rows.get(index).getText().toLowerCase().contains("home store")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 0) {
//						userProfileEngagementDetails.put("manager name", rowValues[0]);//manager name are all blank
						userProfileEngagementDetails.put("home store", rowValues[0]);
					} else
						SimpleUtils.fail("Profile Page: Unable to get Manager Name and Home Store value from ' HR Profile Infomation form'", false);
				}

				else if(rows.get(index).getText().toLowerCase().contains("employee id")
						&& rows.get(index).getText().toLowerCase().contains("date hired")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 0) {
						userProfileEngagementDetails.put("employee id", rowValues[0]);
						userProfileEngagementDetails.put("date hired", rowValues[1]);
					} else
						SimpleUtils.fail("Profile Page: Unable to get EMPLOYEE ID and DATE HIRED value from ' HR Profile Infomation form'", false);
				}
				else if(rows.get(index).getText().toLowerCase().contains("employment type")
						&& rows.get(index).getText().toLowerCase().contains("hourly rate")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 0) {
						userProfileEngagementDetails.put("employment type", rowValues[0]);
						userProfileEngagementDetails.put("hourly rate", rowValues[1]);
					} else
						SimpleUtils.fail("Profile Page: Unable to get EMPLOYMENT TYPE and HOURLY RATE value from ' HR Profile Infomation form'", false);
				}
				else if(rows.get(index).getText().toLowerCase().contains("employment status")
						&& rows.get(index).getText().toLowerCase().contains("exempt")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 0) {
						userProfileEngagementDetails.put("employment status", rowValues[0]);
						userProfileEngagementDetails.put("exempt", rowValues[1]);
					} else
						SimpleUtils.fail("Profile Page: Unable to get EMPLOYMENT STATUS and EXEMPT value from ' HR Profile Infomation form'", false);
				}
				else if(rows.get(index).getText().toLowerCase().contains("address")
						&& rows.get(index).getText().toLowerCase().contains("minor")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 0) {
						userProfileEngagementDetails.put("address", rowValues[0]);
						userProfileEngagementDetails.put("minor", rowValues[1]);
					} else
						SimpleUtils.fail("Profile Page: Unable to get ADDRESS and MINOR value from ' HR Profile Infomation form'", false);
				}
				else if(rows.get(index).getText().toLowerCase().contains("contact information")) {
					String[] rowValues = rows.get(index + 1).getText().split("\n");
					if(rowValues.length > 0) {
						userProfileEngagementDetails.put("phoneNumber", rowValues[0]);
						userProfileEngagementDetails.put("email", rowValues[1]);
					} else
						SimpleUtils.fail("Profile Page: Unable to get phoneNumber and email value from ' HR Profile Infomation form'", false);
				}

			}
		}
		return userProfileEngagementDetails;
	}


	@FindBy(css = "div.availability-zone.green-zone.changed")
	private List<WebElement> changedPreferredAvailabilities;

	@FindBy(css = "div.availability-zone.red-zone.changed")
	private List<WebElement> changedBusyAvailabilities;

	@FindBy(css = "div.timeoff-requests-request.row-fx")
	private List<WebElement> allAvailabilityChangeRequests;

	public List<WebElement> getChangedPreferredAvailabilities() throws Exception{
		List<WebElement> changedAvailabilities = new ArrayList<>();
		if (areListElementVisible(changedPreferredAvailabilities, 10)){
			changedAvailabilities = changedPreferredAvailabilities;
		}
		return changedAvailabilities;
	}

	public List<WebElement> getChangedBusyAvailabilities() throws Exception{
		List<WebElement> changedAvailabilities = new ArrayList<>();
		if (areListElementVisible(changedBusyAvailabilities, 10)){
			changedAvailabilities = changedBusyAvailabilities;
		}
		return changedAvailabilities;
	}

	@Override
	public void approveOrRejectSpecificPendingAvailabilityRequest(String availabilityWeek, String action) throws Exception {
		if (areListElementVisible(allAvailabilityChangeRequests, 10)) {
			for (WebElement availabilityChangeRequest : allAvailabilityChangeRequests) {
				if (isElementLoaded(availabilityChangeRequest, 5)
						&& availabilityChangeRequest.findElement(By.cssSelector("div.request-date")).
						getText().replace("\n", "").equalsIgnoreCase(availabilityWeek)
						&& availabilityChangeRequest.findElement(By.cssSelector("span.request-status")).
						getText().equalsIgnoreCase("pending")) {
					clickTheElement(availabilityChangeRequest);
					if (action.equalsIgnoreCase("approve")) {
						if (isElementLoaded(approveAvailabilityButton, 10)) {
							clickTheElement(approveAvailabilityButton);
							SimpleUtils.pass("Approve the pending availability request successfully!");
						}
					} else {
						if (isElementLoaded(rejectAvailabilityButton, 10)) {
							clickTheElement(rejectAvailabilityButton);
							SimpleUtils.pass("Reject the pending availability request successfully!");
						}
					}
					break;
				}

			}
		}
	}


	public void deleteAllAvailabilitiesForCurrentWeek() throws Exception {
		String preferredHoursTabText = "When I prefer to work";
		String busyHoursTabText = "When I prefer not to work";
		selectMyAvaliabilityEditHoursTabByLabel(preferredHoursTabText);
		//Delete all preferred availabilities in the day
		if (areListElementVisible(myAvailabilityDayOfWeekRows, 5) && myAvailabilityDayOfWeekRows.size() == 7) {
			for (WebElement myAvailabilityDayOfWeekRow: myAvailabilityDayOfWeekRows){
				List<WebElement> availabilitiesInTheDay = myAvailabilityDayOfWeekRow.findElements(By.cssSelector("div.cursor-resizableW"));
				for (WebElement availability: availabilitiesInTheDay) {
					moveToElementAndClick(availability);
					clickTheElement(removeAvailabilityIcon);
					SimpleUtils.report("Remove one availability successfully! ");
				}
			}

		} else
			SimpleUtils.fail("Profile Page: 'My Availability section' Day of Week Rows not loaded.", false);

		selectMyAvaliabilityEditHoursTabByLabel(busyHoursTabText);
		//Delete all busy availabilities in the week
		if (areListElementVisible(myAvailabilityDayOfWeekRows, 5) && myAvailabilityDayOfWeekRows.size() == 7) {
			for (WebElement myAvailabilityDayOfWeekRow: myAvailabilityDayOfWeekRows){
				List<WebElement> availabilitiesInTheDay = myAvailabilityDayOfWeekRow.findElements(By.cssSelector("div.cursor-resizableW"));
				for (WebElement availability: availabilitiesInTheDay) {
					moveToElementAndClick(availability);
					clickTheElement(removeAvailabilityIcon);
					SimpleUtils.report("Remove one availability successfully! ");
				}
			}

		} else
			SimpleUtils.fail("Profile Page: 'My Availability section' Day of Week Rows not loaded.", false);
	}


	@Override
	public void cancelSpecificPendingAvailabilityRequest(String availabilityWeek) throws Exception {
		if (areListElementVisible(allAvailabilityChangeRequests, 10)) {
			for (WebElement availabilityChangeRequest : allAvailabilityChangeRequests) {
				if (isElementLoaded(availabilityChangeRequest, 5)
						&& availabilityChangeRequest.findElement(By.cssSelector("div.request-date")).
						getText().replace("\n", "").equalsIgnoreCase(availabilityWeek)
						&& availabilityChangeRequest.findElement(By.cssSelector("span.request-status")).
						getText().equalsIgnoreCase("pending")) {
					clickTheElement(availabilityChangeRequest);
					if (isElementLoaded(rejectAvailabilityButton, 10)) {
						clickTheElement(rejectAvailabilityButton);
						SimpleUtils.pass("Reject the pending availability request successfully!");
					}
					break;
				}

			}
		}
	}

	@Override
	public void cancelAllPendingAvailabilityRequest() throws Exception {
		if (areListElementVisible(pendingAvailabilityRequests, 10)) {
			for (WebElement availabilityChangeRequest : pendingAvailabilityRequests) {
				clickTheElement(availabilityChangeRequest);
				if (isElementLoaded(cancelButtonOfPendingRequest, 10)) {
					clickTheElement(cancelButtonOfPendingRequest);
					SimpleUtils.pass("Cancel the pending availability request successfully!");
				}
			}
		}
	}

	@Override
	public void verifyTheApprovedOrRejectedAvailabilityRequestCannotBeOperated(String availabilityWeek) throws Exception {
		if (areListElementVisible(allAvailabilityChangeRequests, 10)) {
			for (WebElement availabilityChangeRequest : allAvailabilityChangeRequests) {
				if (isElementLoaded(availabilityChangeRequest, 5)
						&& availabilityChangeRequest.findElement(By.cssSelector("div.request-date")).
						getText().replace("\n", "").equalsIgnoreCase(availabilityWeek)) {
					clickTheElement(availabilityChangeRequest);
					if (!isElementLoaded(rejectAvailabilityButton, 3)
							&& !isElementLoaded(approveAvailabilityButton, 3)) {
						SimpleUtils.pass("Approve or Reject button not loaded!");
					} else {
						SimpleUtils.fail("Approve or Reject button should not loaded!", false);
					}
					break;
				}

			}
		}
	}

	@Override
	public void approveSpecificRejectedAvailabilityRequest(String availabilityWeek) throws Exception {
		if (areListElementVisible(allAvailabilityChangeRequests, 10)) {
			for (WebElement availabilityChangeRequest : allAvailabilityChangeRequests) {
				if (isElementLoaded(availabilityChangeRequest, 5)
						&& availabilityChangeRequest.findElement(By.cssSelector("div.request-date")).
						getText().replace("\n", "").equalsIgnoreCase(availabilityWeek)
						&& availabilityChangeRequest.findElement(By.cssSelector("span.request-status")).
						getText().equalsIgnoreCase("rejected")) {
					clickTheElement(availabilityChangeRequest);
					if (isElementLoaded(approveAvailabilityButton, 10)) {
						clickTheElement(approveAvailabilityButton);
						SimpleUtils.pass("Approve the pending availability request successfully!");
					} else {
						SimpleUtils.fail("Approve button fail to load!", false);
					}
					break;
				}

			}
		}
	}

	@Override
	public void verifyClickCancelledAvalabilityRequest() throws Exception {
		if (areListElementVisible(cancelledAvailabilityRequests, 10) && cancelledAvailabilityRequests.size()>0) {
			clickTheElement(cancelledAvailabilityRequests.get(0));
			if (!isElementLoaded(cancelButtonOfPendingRequest, 10)
					&& !isElementLoaded(approveAvailabilityButton,10)
					&& !isElementLoaded(rejectAvailabilityButton,10)) {
				SimpleUtils.pass("Cancel the pending availability request successfully!");
			} else {
				SimpleUtils.fail("There shouldn't be any buttons pop up for cancelled request!", false);
			}
		}
	}


	@Override
	public boolean isAlertDialogLoaded() throws Exception{
		boolean isAlertDialogLoaded = false;
		if (isElementLoaded(alertDialog,10)){
			isAlertDialogLoaded = true;
			SimpleUtils.report("Email is valid so can not save successfully!");
		} else {
			SimpleUtils.report("No alert dialog for invalid email format!");
		}
		return isAlertDialogLoaded;
	}

	@Override
	public String getMessageFromAlertDialog () throws Exception{
		String message = "";
		if (isElementLoaded(alertDialog,10)){
			message = alertDialog.findElement(By.cssSelector("span")).getText();
			SimpleUtils.pass("Email is valid so can not save successfully!");
		} else {
			SimpleUtils.fail("No alert dialog for invalid email format!",false);
		}
		return message;
	}


	@FindBy(css = "[ng-if=\"minorRuleTemplate\"] p.contentText")
	private WebElement minorRuleTemplateName;
	@Override
	public String getMinorRuleTemplateName () throws Exception{
		String message = "";
		if (isElementLoaded(minorRuleTemplateName,10)){
			message = minorRuleTemplateName.getText();
			SimpleUtils.pass("Get minor rule template name successfully!");
		} else {
			SimpleUtils.fail("Get minor rule template name fail to load!",false);
		}
		return message;
	}

	@Override
	public String getToolTipMessageOfAvailabilityLockIcon() throws Exception {
		String message = "";
		if (isElementLoaded(lockIcon)) {
			message = lockIcon.getAttribute("data-tootik");
			SimpleUtils.pass("Get lock tooltip message successfully! ");
		} else
			SimpleUtils.fail("The Availability lock icon is not loaded! ", false);

		return message;
	}

	@Override
	public String getJobTitleFromProfilePage() throws Exception {
		String jobTitle = "";
		if (areListElementVisible(profileInfoFields, 5) && profileInfoFields.size() > 0) {
			for (WebElement field : profileInfoFields) {
				WebElement label = field.findElement(By.className("label"));
				WebElement value = field.findElement(By.className("value"));
				if (label != null && value != null) {
					if (label.getText().equalsIgnoreCase("JOB TITLE")) {
						jobTitle = value.getText();
						SimpleUtils.report("Get the job title: " + jobTitle);
						break;
					}
				}
			}
		}
		return jobTitle;
	}

	public List<String> getAvailableShiftsOnAvailabilityTable (){
		List<String> availableShiftsOnAvailabilityTable = new ArrayList<>();
		if (areListElementVisible(availableShifts, 10)
				&& availableShifts.size()>0) {
			for (WebElement shift: availableShifts) {
				availableShiftsOnAvailabilityTable.add(shift.getText());
				SimpleUtils.report("Get the available shifts info successfully! ");
			}
		} else
			SimpleUtils.report("There is no available shifts on Availability Table");
		return availableShiftsOnAvailabilityTable;
	}


	@FindBy(css = ".availability-zone.pto-zone")
	private List<WebElement> timeOffsOnAvailabilityTable;
	public List<String> getTimeOffsLengthOnAvailabilityTable (){
		List<String> timeOffs = new ArrayList<>();
		if (areListElementVisible(timeOffsOnAvailabilityTable, 10)
				&& timeOffsOnAvailabilityTable.size()>0) {
			for (WebElement timeoff: timeOffsOnAvailabilityTable) {
				String length = Double.parseDouble(timeoff.getAttribute("style").split(";")[1].split(":")[1].replace("%", ""))/2.08/2 + " Hrs";
				timeOffs.add(length);
				SimpleUtils.report("Get the available shifts info successfully! ");
			}
		} else
			SimpleUtils.report("There is no timeoff on Availability Table");
		return timeOffs;
	}


	@FindBy(css = "[ng-repeat=\"timeOffType in accruedHoursBalance\"]")
	private List<WebElement> balanceHrs;
	public HashMap<String, String> getTimeOffBalanceHrs (){
		HashMap<String, String> timeOffBalanceHrs = new HashMap<>();
		if (areListElementVisible(balanceHrs, 10)
				&& balanceHrs.size()>0) {
			for (WebElement balanceHr: balanceHrs) {
				String hour = balanceHr.findElement(By.cssSelector("span.count-block-counter-hours")).getText();
				String timeOffType = balanceHr.findElement(By.cssSelector("span.count-block-label")).getText();
				if (timeOffType.equalsIgnoreCase("Floating Holiday")) {
					timeOffType = "FH";
				}
				timeOffBalanceHrs.put(timeOffType, hour);
				SimpleUtils.report("Get the balance hrs of "+timeOffType+" successfully! ");
			}
		} else
			SimpleUtils.fail("Time off balance hrs fail to load! ", false);
		return timeOffBalanceHrs;
	}


	@FindBy(css = "div.inner")
	private WebElement reviewPreferencesInnerBox;
	public boolean checkIfReviewPreferencesInnerBoxDisplay () throws Exception {
		boolean isReviewPreferencesInnerBoxDisplay = false;
		if (isElementLoaded(reviewPreferencesInnerBox, 5)) {
			isReviewPreferencesInnerBoxDisplay = true;
			SimpleUtils.pass("User profile page: The review preferences inner box display correctly! ");
		} else
			SimpleUtils.report("User profile page: The review preferences inner box fail to load! ");
		return isReviewPreferencesInnerBoxDisplay;
	}

	@Override
	public void updateSpecificPreferredOrBusyHoursToAllWeek(String hoursType) throws Exception {
		String preferredHoursTabText = "When I prefer to work";
		String busyHoursTabText = "When I prefer not to work";
		WebElement availabilityToolTip = null;
		if (hoursType.toLowerCase().contains(preferredHoursTabText.toLowerCase())) {
			selectMyAvaliabilityEditHoursTabByLabel(preferredHoursTabText);
			availabilityToolTip = preferredAvailabilityToolTip;
		} else {
			selectMyAvaliabilityEditHoursTabByLabel(busyHoursTabText);
			availabilityToolTip = busyAvailabilityToolTip;
		}

		//Delete all availabilities in the week
		WebElement dayRow = null;
		for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
			if (areListElementVisible(myAvailabilityDayOfWeekRows, 5) && myAvailabilityDayOfWeekRows.size() == 7) {
				dayRow = myAvailabilityDayOfWeekRows.get(dayIndex);
				List<WebElement> emptyAvailabilitiesInTheDay = dayRow.findElements(By.cssSelector("div.cursor-empty"));
				for (int i = 0; i < 2; i++) {
					click(emptyAvailabilitiesInTheDay.get(i));
				}

				WebElement rightCell = dayRow.findElement(By.cssSelector("div.cursor-resizableE"));
				mouseHoverDragandDrop(rightCell, emptyAvailabilitiesInTheDay.get((emptyAvailabilitiesInTheDay.size() - 4) / 2 + 1));
				waitForSeconds(2);

				if (!availabilityToolTip.getText().toLowerCase().replace(" ", "").contains("12:00am-12:00pm")) {
					SimpleUtils.fail("Update availabilities fail! ", false);
				} else
					SimpleUtils.report("Update availabilities successfully! ");
			}else{
				SimpleUtils.fail("Profile Page: 'My Availability section' Day of Week Rows not loaded.", false);
			}
		}
	}
}
