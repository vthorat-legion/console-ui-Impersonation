package com.legion.pages;

import org.openqa.selenium.WebElement;

import java.util.*;

public interface ProfileNewUIPage {

	public void clickOnProfileConsoleMenu() throws Exception;

	public boolean isProfilePageLoaded() throws Exception;

	public void selectProfilePageSubSectionByLabel(String profilePageSubSectionLabel) throws Exception;
	public void verifyAvailabilityWeek(String weekinfo) throws Exception;

	public void clickOnCreateTimeOffBtn() throws Exception;

	public void selectTimeOffDuration(String startDate, String endDate) throws Exception;

	public void updateTimeOffExplanation(String explanationText) throws Exception;

	public void selectTimeOffReason(String reasonLabel) throws Exception;

	public boolean isReasonLoad(String timeOffReasonLabel) throws Exception;

	public void createNewTimeOffRequest(String timeOffReasonLabel, String timeOffExplanationText) throws Exception;

	public void clickOnSaveTimeOffRequestBtn() throws Exception;

	public String getTimeOffRequestStatus(String timeOffReasonLabel, String timeOffExplanationText,
			String timeOffStartDate, String timeOffEndDate) throws Exception;

	public boolean isEditingProfileSectionLoaded() throws Exception;

	public boolean isPersonalDetailsSectionLoaded() throws Exception;

	public boolean isChangePasswordButtonLoaded() throws Exception;

	public boolean isEngagementDetrailsSectionLoaded() throws Exception;

	public boolean isProfileBadgesSectionLoaded() throws Exception;

	public String getProfilePageActiveTabLabel() throws Exception;

	public boolean isShiftPreferenceSectionLoaded() throws Exception;

	public boolean isMyAvailabilitySectionLoaded() throws Exception;

	public boolean isCreateTimeOffButtonLoaded() throws Exception;

	public boolean isTimeOffPendingBlockLoaded() throws Exception;

	public boolean isTimeOffApprovedBlockLoaded() throws Exception;

	public boolean isTimeOffRejectedBlockLoaded() throws Exception;

	public boolean isTimeOffRequestsSectionLoaded() throws Exception;

	public String getProfilePageActiveLocation() throws Exception;

	public int getAllTimeOffRequestCount();

	public TreeMap<String, String> getUserProfileEngagementDetails() throws Exception;

	public void updateUserProfile(String firstName, String lastname, String nickName, String streetAddress1,
			String streetAddress2, String city, String state, String zip, String phone, String email) throws Exception;

	public void userProfileInviteTeamMember() throws Exception;

	public void userProfileChangePassword(String oldPassword, String newPassword, String confirmPassword) throws Exception;

	public boolean isShiftPreferenceCollapsibleWindowOpen() throws Exception;

	public void clickOnShiftPreferenceCollapsibleWindowHeader() throws Exception;

	public ArrayList<String> getUserProfileBadgesDetails() throws Exception;

	public ArrayList<String> getUserBadgesDetailsFromProfilePage() throws Exception;

	public HashMap<String, String> getMyShiftPreferenceData() throws Exception;

	public void updateMyShiftPreferenceData(boolean canReceiveOfferFromOtherLocation, boolean isVolunteersForAdditional, int minHoursPerShift,
			int maxHoursPerShift, int minShiftLength, int maxShiftLength, int minShiftsPerWeek, int maxShiftsPerWeek) throws Exception;
	
	public HashMap<String, Object> getMyAvailabilityData() throws Exception;

	boolean isMyAvailabilityCollapsibleWindowOpen() throws Exception;

	void clickOnMyAvailabilityCollapsibleWindowHeader() throws Exception;

	boolean isMyAvailabilityLocked() throws Exception;

	boolean isMyAvailabilityLockedNewUI() throws Exception;

	public ArrayList<HashMap<String, ArrayList<String>>> getMyAvailabilityPreferredAndBusyHours();

	public void updateLockedAvailabilityPreferredOrBusyHoursSlider(String hoursType, int sliderIndex,
			String leftOrRightSliderArrow, int durationMinutes, String repeatChanges) throws Exception;

	public String updateMyAvailability(String hoursType, int sliderIndex,
												   String leftOrRightSliderArrow, double durationHours, String repeatChanges) throws Exception;

	public HashMap<String, Integer> getTimeOffRequestsStatusCount() throws Exception;

	public void approveOrRejectTimeOffRequestFromToDoList(String timeOffReasonLabel, String timeOffStartDuration,
			String timeOffEndDuration, String action) throws Exception;

	public String getNickNameFromProfile() throws Exception;

	public boolean isNewTimeOffWindowLoaded() throws Exception;

	public void verifyCalendarForCurrentAndNextMonthArePresent(String currentYearMonthDate) throws Exception;

	public void clickOnUserProfileImage() throws Exception;

	public void selectProfileSubPageByLabelOnProfileImage(String profilePageSubSectionLabel) throws Exception;

	public List<String> selectStartAndEndDate() throws Exception;

	public List<String> selectStartAndEndDate(int daysInadvance, int startDays, int endDays) throws Exception;

	public String selectStartAndEndDateAtSameDay() throws Exception;

	public boolean areAllDayCheckboxesLoaded() throws Exception;

	public void deSelectAllDayCheckboxes() throws Exception;

	public void verifyStartDateAndEndDateIsCorrect(String timeOffStartDate, String timeOffEndDate) throws Exception;

	public void verifyTimeIsCorrectAfterDeSelectAllDay() throws Exception;

	public void verifyAlignmentOfAMAndPMAfterDeSelectAllDay() throws Exception;

	public int getTimeOffCountByStatusLabel(String status) throws Exception;

	public HashMap<String, List<String>> selectCurrentDayAsStartNEndDate() throws Exception;

	public void validateTheEditFunctionalityOnMyProfile(String streetAddress1, String streetAddress2, String city, String state, String zip) throws Exception;

	public void validateTheFeatureOfChangePassword(String oldPassword) throws Exception;

	public String getNewPassword(String oldPassword) throws Exception;

	public void validateTheUpdateOfShiftPreferences(boolean canReceiveOfferFromOtherLocation, boolean isVolunteersForAdditional) throws Exception;

	public void validateTheUpdateOfAvailability(String hoursType, int sliderIndex, String leftOrRightDuration,
												int durationMinutes, String repeatChanges) throws Exception;

	public void reasonsOfLeaveOnNewTimOffRequest() throws Exception;

	public String selectRandomReasonOfLeaveOnNewTimeOffRequest() throws Exception;

	public void createNewTimeOffRequestAndVerify(String timeOffReasonLabel, String timeOffExplanationText) throws Exception;

	public void validateTheFunctionalityOfTimeOffCancellation() throws Exception;

	public void newApproveOrRejectTimeOffRequestFromToDoList(String timeOffReasonLabel, String timeOffStartDuration,
															 String timeOffEndDuration, String action) throws Exception;

	public void clickNextWeek() throws Exception;

	public String getAvailabilityWeek() throws Exception;

	public void cancelAllTimeOff() throws Exception;

	public void rejectAllTimeOff() throws Exception;

	public String getTimeOffRequestStatusByExplanationText(String timeOffExplanationText) throws Exception;

	public void approveAllPendingAvailabilityRequest() throws Exception;

	public void verifyTheLatestAvailabilityRequestInfo(String weekInfo, double hours, String repeatChanges ) throws Exception;

	public void clickOnEditUserProfilePencilIcon() throws Exception;

	public void clickOnSaveUserProfileBtn() throws Exception;

	public void clickOnCancelUserProfileBtn() throws Exception;

	public void verifyUserProfileSectionIsLoaded() throws Exception;

	public void verifyHRProfileInformationSectionIsLoaded() throws Exception;

	public Map<String, String> getHRProfileInfo() throws Exception;

	public void verifyLegionInformationSectionIsLoaded() throws Exception;

	public void verifyActionSectionIsLoaded() throws Exception;

	public void verifyFieldsInUserProfileSection() throws Exception;

	public void verifyFieldsInHRProfileInformationSection() throws Exception;

	public void verifyFieldsInLegionInformationSection() throws Exception;

	public void verifyContentsInActionsSection() throws Exception;

	public void verifyEditUserProfileButtonIsLoaded() throws Exception;

	public void verifySyncTMInfoButtonIsLoaded() throws Exception;

	public void verifyContentsInActionsSectionInTMView() throws Exception;

	public void isRequiredErrorShowUp(String field) throws Exception;

	public boolean isSaveBtnDisabled() throws Exception;

	public void verifyHRProfileSectionIsNotEditable() throws Exception;

	public void verifyLegionInfoSectionIsNotEditable() throws Exception;

	public void verifyTheEmailFormatInProfilePage(List<String> testEmails) throws Exception;

	public HashMap<String, String> getValuesOfFields() throws Exception;

	public void updateAllFields(HashMap<String, String> values) throws Exception;

	public void clickOnOKBtnOnAlert() throws Exception;

	public boolean verifyManageBadgeBtn() throws Exception;

	public void verifySelectBadge() throws Exception;

	public void saveBadgeBtn() throws Exception;

	public void cancelBadgeBtn() throws Exception;

	public boolean ifMatchEmailRegex(String email) throws Exception;

	public boolean isMINORDisplayed() throws Exception;

	public boolean isMINORYesOrNo() throws Exception;

	public void verifyMINORField(boolean isMinor) throws Exception;

	public void verifySMCanSelectACalendarForMinor() throws Exception;

	public void selectAGivenCalendarForMinor(String givenCalendar) throws Exception;

	public HashMap<String, String> getUserProfileName() throws Exception;

	public void clickOnShowOrHideInvitationCodeButton(boolean toShowCode) throws Exception;

	public String getInvitationCode() throws Exception;

	public boolean isInvitationCodeLoaded() throws Exception;

	public String getShowOrHideInvitationCodeButtonTooltip() throws Exception;

	public boolean isInviteToLegionButtonLoaded() throws Exception;

	public boolean isShowOrHideInvitationCodeButtonLoaded() throws Exception;

	public void createTimeOffOnSpecificDays(String timeOffReasonLabel, String timeOffExplanationText,String fromDay, int duration) throws Exception;

	public void updatePreferredOrBusyHoursToAllDay(int dayIndex, String hoursType) throws Exception;

	public void clickAvailabilityEditButton() throws Exception;

	public boolean verifyErrorMessageForEditAvailabilityShowsUpOrNot() throws Exception;

	public void saveMyAvailabilityEditMode(String availabilityChangesRepeat ) throws Exception;

	public String getStatusOnProfilePage () throws Exception;

	public HashMap<String, String> getOneUserHRProfileInfo() throws Exception;

	public List<WebElement> getChangedPreferredAvailabilities() throws Exception;

	public List<WebElement> getChangedBusyAvailabilities () throws Exception;

	public void approveOrRejectSpecificPendingAvailabilityRequest(String availabilityWeek, String action) throws Exception;

	public void deleteAllAvailabilitiesForCurrentWeek() throws Exception;

	public void clickPreviousWeek() throws Exception;

	public void cancelSpecificPendingAvailabilityRequest(String availabilityWeek) throws Exception;

	public void cancelAllPendingAvailabilityRequest() throws Exception;

	public String getCountForStatus(String status) throws Exception;

	public String getAvailableHoursForSpecificWeek() throws Exception;

	public void verifyTheApprovedOrRejectedAvailabilityRequestCannotBeOperated(String availabilityWeek) throws Exception;

	public void approveSpecificRejectedAvailabilityRequest(String availabilityWeek) throws Exception;

	public void verifyClickCancelledAvalabilityRequest() throws Exception;

	public boolean isAlertDialogLoaded() throws Exception;

	public String getMessageFromAlertDialog () throws Exception;
	public String getMinorRuleTemplateName () throws Exception;
	public String getToolTipMessageOfAvailabilityLockIcon() throws Exception;
	public String getJobTitleFromProfilePage() throws Exception;
	public List<String> getAvailableShiftsOnAvailabilityTable ();
	public List<String> getTimeOffsLengthOnAvailabilityTable ();
	public HashMap<String, String> getTimeOffBalanceHrs ();
	public boolean checkIfReviewPreferencesInnerBoxDisplay () throws Exception;
	public void updateSpecificPreferredOrBusyHoursToAllWeek(String hoursType) throws Exception;
}
