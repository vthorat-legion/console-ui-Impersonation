package com.legion.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.WebElement;

public interface ControlsNewUIPage {

	void clickOnControlsConsoleMenu() throws Exception;

	public boolean isControlsPageLoaded()  throws Exception;

	public void clickOnGlobalLocationButton() throws Exception;

	public void clickOnControlsCompanyProfileCard() throws Exception;

	public void updateUserLocationProfile(String companyName, String businessAddress, String city, String state,
			String country, String zipCode, String timeZone, String website, String firstName, String lastName,
			String email, String phone) throws Exception;

	public boolean isUserLocationProfileUpdated(String companyName, String businessAddress, String city, String state,
			String country, String zipCode, String timeZone, String website, String firstName, String lastName,
			String email, String phone) throws Exception;

	public void clickOnControlsWorkingHoursCard() throws Exception;

	public void clickOnSaveRegularHoursBtn() throws Exception;

	public void clickOnControlsSchedulingPolicies() throws Exception;

	public void updateAndVerifyAutoPublishSettings(String option) throws Exception;

	public void updateAutoPublishSchedulePublishDayOfWeek(String option) throws Exception;

	public void updateAutoPublishSchedulePublishTimeOfDay(String mins) throws Exception;

	public void updateAutoPublishScheduleRepublishDayOfWeek(String option) throws Exception;

	public void updateAutoPublishScheduleRepublishTimeOfDay(String mins) throws Exception;

	public String getAutoPublishSettings() throws Exception;

	public String getAutoPublishSchedulePublishDayOfWeek() throws Exception;

	public String getAutoPublishSchedulePublishTimeOfDay() throws Exception;

	public String getAutoPublishScheduleRepublishDayOfWeek() throws Exception;

	public String getAutoPublishScheduleRepublishTimeOfDay() throws Exception;

	public void enableOrDisableScheduleCopyRestriction(String yesOrNo) throws Exception;

	public void setCopyConfig(boolean onOrOff, String toggleName) throws Exception;

	public void setViolationLimit(String value) throws Exception;

	public void setBudgetOverageLimit(String value) throws Exception;

	public boolean isBudgetSmartcardEnabled() throws Exception;

	public void enableDisableBudgetSmartcard(boolean enable) throws Exception;

	public String getAdvanceScheduleWeekCountToCreate() throws Exception;

	public void updateAdvanceScheduleWeekCountToCreate(String scheduleWeekCoundToCreate) throws Exception;

	public HashMap<String, Integer> getScheduleBufferHours() throws Exception;

	public void clickOnControlsLocationProfileSection() throws Exception;
	public void clickOnControlsScheduleCollaborationSection() throws Exception;
	public void clickOnControlsComplianceSection() throws Exception;
	public void turnOnOrTurnOffSplitShiftToggle(boolean action) throws Exception;
	public void editSplitShiftPremium(String numOfPremiumHrs, String greaterThan, boolean saveOrNot) throws Exception;
	public void clickOnControlsUsersAndRolesSection() throws Exception;
	public void clickOnControlsTasksAndWorkRolesSection() throws Exception;
	public void clickOnControlsOperatingHoursSection() throws Exception;
	
	public boolean isControlsLocationProfileLoaded() throws Exception;
	public boolean isControlsScheduleCollaborationLoaded() throws Exception;
	public boolean isControlsComplianceLoaded() throws Exception;
	public boolean isControlsUsersAndRolesLoaded() throws Exception;
	public boolean isControlsTasksAndWorkRolesLoaded() throws Exception;
	public boolean isControlsSchedulingPoliciesLoaded() throws Exception;
	public boolean isControlsWorkingHoursLoaded() throws Exception;

	public void clickOnControlsTimeAndAttendanceCard() throws Exception;

	public void clickOnControlsTimeAndAttendanceAdvanceBtn() throws Exception;

	public void selectTimeSheetExportFormatByLabel(String optionLabel) throws Exception;

	public void clickOnSchedulingPoliciesShiftAdvanceBtn() throws Exception;

	public void selectSchedulingPoliciesShiftIntervalByLabel(String intervalTimeLabel) throws Exception;

	public void clickOnSchedulingPoliciesSchedulesAdvanceBtn() throws Exception;

	public String getSchedulePublishWindowWeeks() throws Exception;

	public int getAdvanceScheduleDaysCountToBeFinalize() throws Exception;

	public String getSchedulingPoliciesActiveLocation() throws Exception;

	public void updateSchedulePublishWindow(String publishWindowAdvanceWeeks, boolean preserveSetting, boolean overwriteSetting) throws Exception;

	public void updateSchedulePlanningWindow(String planningWindowAdvanceWeeks , boolean preserveSetting, boolean overwriteSetting) throws Exception;

	public String getSchedulePlanningWindowWeeks() throws Exception;

	public void updateSchedulingPoliciesFirstDayOfWeek(String day) throws Exception;

	public String getSchedulingPoliciesFirstDayOfWeek() throws Exception;

	public void updateEarliestOpeningTime(String openingTime) throws Exception;

	public String getEarliestOpeningTime() throws Exception;

	public void updateLatestClosingTime(String closingTime) throws Exception;

	public String getLatestClosingTime() throws Exception;

	public void updateAdvanceScheduleDaysToBeFinalize(String advanceDays) throws Exception;

	public void updateScheduleBufferHoursBefore(String beforeBufferCount) throws Exception;

	public void updateScheduleBufferHoursAfter(String afterBufferCount) throws Exception;

	public void updateMinimumShiftLengthHour(String shiftLengthHour) throws Exception;

	public float getMinimumShiftLengthHour() throws Exception;

	public void updateMaximumShiftLengthHour(String shiftLengthHour) throws Exception;

	public float getMaximumShiftLengthHour() throws Exception;

	public void updateMaximumNumWeekDaysToAutoSchedule(String maxDaysLabel) throws Exception;

	public String getMaximumNumWeekDaysToAutoSchedule() throws Exception;

	public void updateDisplayShiftBreakIcons(String shiftBreakIcons) throws Exception;

	public String getDisplayShiftBreakIcons() throws Exception;

	public void updateApplyLaborBudgetToSchedules(String isLaborBudgetToApply) throws Exception;

	public void updateScheduleBudgetType(String budgetType) throws Exception;

	public void updateTeamAvailabilityLockMode(String availabilityLockModeLabel) throws Exception;

	public void updateScheduleUnavailableHourOfWorkers(String unavailableWorkersHour) throws Exception;

	public void updateAvailabilityToleranceField(String availabilityToleranceMinutes) throws Exception;

	public void updateCanWorkerRequestTimeOff(String canWorkerRequestTimeOffValue) throws Exception;

	public void clickOnSchedulingPoliciesTimeOffAdvanceBtn() throws Exception;

	public int getDaysInAdvanceCreateTimeOff() throws Exception;

	public void updateMaxEmployeeCanRequestForTimeOffOnSameDay(String maxWorkersTimeOfPerDayCount) throws Exception;

	public void updateNoticePeriodToRequestTimeOff(String noticePeriodToRequestTimeOff) throws Exception;

	public void updateShowTimeOffReasons(String isShowTimeOffReasons) throws Exception;

	public void updateSchedulingPolicyGroupsHoursPerWeek(int minHours, int maxHours, int idealHour) throws Exception;

	public void updateSchedulingPolicyGroupsShiftsPerWeek(int minShifts, int maxShifts, int idealShifts) throws Exception;

	public void updateSchedulingPolicyGroupsHoursPerShift(int minHoursPerShift, int maxHoursPerShift, int ideaHoursPerShift)
			throws Exception;

	public void updateEnforceNewEmployeeCommittedAvailabilityWeeks(boolean isEmployeeCommittedAvailabilityActive
			, int committedHoursWeeks) throws Exception;

	public void selectSchdulingPolicyGroupsTabByLabel(String tabLabel) throws Exception;

	public HashMap<String, ArrayList<String>> getLocationInformationEditableOrNonEditableFields() throws Exception;

	public boolean isControlsConsoleMenuAvailable() throws Exception;

	public void clickOnLocationProfileEditLocationBtn() throws Exception;

    public void checkTimeZoneDropdownOptions(int targetNumbersOfUTCFormat, String timeZone) throws Exception;

    public HashMap<String, ArrayList<String>> getSchedulingPoliciesSchedulesSectionEditableOrNonEditableFields() throws Exception;

	public HashMap<String, ArrayList<String>> getSchedulingPoliciesShiftsSectionEditableOrNonEditableFields() throws Exception;

	public HashMap<String, ArrayList<String>> getSchedulingPoliciesBudgetSectionEditableOrNonEditableFields() throws Exception;

	public HashMap<String, ArrayList<String>> getSchedulingPoliciesTeamAvailabilityManagementSectionEditableOrNonEditableFields()
			throws Exception;

	public HashMap<String, ArrayList<String>> getSchedulingPoliciesTimeOffSectionEditableOrNonEditableFields() throws Exception;

	public HashMap<String, ArrayList<String>> getSchedulingPoliciesSchedulingPolicyGroupsSectionEditableOrNonEditableFields()
			throws Exception;

	public HashMap<String, ArrayList<String>> getScheduleCollaborationEditableOrNonEditableFields() throws Exception;

	public void clickOnScheduleCollaborationOpenShiftAdvanceBtn() throws Exception;
	public void allowEmployeesClaimOvertimeShiftOffer() throws Exception;
	public void notAllowEmployeesClaimOvertimeShiftOffer() throws Exception;

	public HashMap<String, ArrayList<String>> getComplianceEditableOrNonEditableFields() throws Exception;

	public void selectUsersAndRolesSubTabByLabel(String label) throws Exception;

	public HashMap<String, ArrayList<String>> getUsersAndRolesAddNewUserPageEditableOrNonEditableFields() throws Exception;

	public HashMap<String, ArrayList<String>> getUsersAndRolesEditUserPageEditableOrNonEditableFields(String userFirstName) throws Exception;

	public HashMap<String, ArrayList<String>> getUsersAndRolesUpdateEmployeeJobTitleEditableOrNonEditableFields(
			String employeeJobTitle) throws Exception;

	public HashMap<String, ArrayList<String>> getUsersAndRolesCreateNewEmployeeJobTitleEditableOrNonEditableFields(
			String employeeJobTitle, String newEmployeeJobTitleRole) throws Exception;

	public HashMap<String, ArrayList<String>> getUsersAndRolesUpdateBadgesEditableOrNonEditableFields(String BadgesLabel)
			throws Exception;

	public HashMap<String, ArrayList<String>> getUsersAndRolesNewBadgeEditableOrNonEditableFields() throws Exception;

	public void selectTasksAndWorkRolesSubTabByLabel(String label) throws Exception;

	public List<WebElement> getTasksAndWorkRolesSectionAllWorkRolesList() throws Exception;

	public HashMap<String, ArrayList<String>> getTasksAndWorkRolesEditWorkRolePropertiesEditableOrNonEditableFields() throws Exception;

	public boolean isWorkRoleDetailPageSubSectionsExpandFunctionalityWorking() throws Exception;

	public boolean isLaborCalculationTabLoaded() throws Exception;

	public HashMap<String, ArrayList<String>> getTasksAndWorkRolesLaborCalculatorTabEditableOrNonEditableFields() throws Exception;

	public HashMap<String, ArrayList<String>> getTasksAndWorkRolesAddWorkRolePageEditableOrNonEditableFields()
			throws Exception;

	public HashMap<String, ArrayList<String>> verifyUpdateControlsRegularHoursPopupEditableOrNonEditableFields() throws Exception;

	public HashMap<String, ArrayList<String>> verifyUpdateControlsHolidayHoursPopupEditableOrNonEditableFields() throws Exception;

	public void verifyAllLocations(String txt) throws Exception;
    public void verifySearchLocations(String searchText) throws Exception;
	public boolean isControlsCompanyProfileCard() throws Exception;
	public boolean isControlsSchedulingPoliciesCard() throws Exception;
	public boolean isControlsSchedulingCollaborationCard() throws Exception;
	public boolean isControlsComplianceCard() throws Exception;
	public boolean isControlsUsersAndRolesCard() throws Exception;
	public boolean isControlsTaskAndWorkRolesCard() throws Exception;
	public boolean isControlsWorkingHoursCard() throws Exception;
	public boolean isControlsLocationsCard() throws Exception;
	public void verifySchedulePublishWindow(String publishWindowAdvanceWeeks, String publishWindowQuestion, String userCredential) throws Exception ;
	public void getSchedulePublishWindowWeeksDropDownValues() throws Exception;
	public List<String> getSchedulePublishWindowValueAtDifferentLocations(boolean schedulePublishWindowWeeks) throws Exception;
	public void getSchedulePlanningWindowWeeksDropDownValues() throws Exception;
	public void verifySchedulePublishWindowUpdationValues(String publishWindowAdvanceWeeks, List<String>
			selectionOptionLabelAfterUpdation) throws Exception;
	public void verifySchedulePlanningWindow(String planningWindowAdvanceWeeks,
											 String planningWindowQuestion, String userCredential) throws Exception;
	public void verifySchedulePlanningWindowUpdationValues(String planningWindowAdvanceWeeks, List<String> selectionOptionLabelAfterUpdation)
			throws Exception;
	public String getTimeSheetApprovalSelectedOption(boolean byManager) throws Exception;

	public String getTimeZoneFromLocationDetailsPage() throws Exception;

	public void updateScheduleScore(String budget_score, String coverage_scores_regular_hours, String coverage_scores_peak_hours, String employee_match_score, String compliance_score, String how_to_measure_coverage_relative_to_guidance_budget) throws Exception;

	public boolean isScheduleScoreUpdated(String budget_score, String coverage_scores_regular_hours, String coverage_scores_peak_hours, String employee_match_score, String compliance_score, String how_to_measure_coverage_relative_to_guidance_budget);

	public String getOnBoardOptionFromScheduleCollaboration() throws Exception;

	public void setOnBoardOptionAsEmailWhileInviting() throws Exception;

	public void clickOnWorkHoursTypeByText(String title) throws Exception;

	public LinkedHashMap<String, List<String>> getRegularWorkingHours() throws Exception;

	public void enableOverRideAssignmentRuleAsYes() throws Exception;

	public void enableOverRideAssignmentRuleAsNo() throws Exception;

	public HashMap<String, Integer> getOvertimePayDataFromControls();

	public HashMap<String, Integer> getMealBreakDataFromControls();

	public boolean isCompliancePageLoaded() throws Exception;

	public String getIsApprovalByManagerRequiredWhenEmployeeClaimsOpenShiftSelectedOption() throws Exception;

	public void updateOpenShiftApprovedByManagerOption(String option)throws Exception;
	public void updateAvailabilityManagementIsApprovalRequired(String option) throws Exception;
	public void updateSwapAndCoverRequestIsApprovalRequired(String option) throws Exception;
	public void turnGFEToggleOnOrOff(boolean isTurnOn) throws Exception;
	public void turnVSLToggleOnOrOff(boolean isTurnOn) throws Exception;
	public HashMap<String, List<String>> getRandomUserNLocationNSchedulingPolicyGroup() throws Exception;
	public HashMap<String, List<String>> getDataFromSchedulingPolicyGroups() throws Exception;
	public String selectAnyActiveTM() throws Exception;
	public String deactivateActiveTM() throws Exception;
	public void activateInactiveTM() throws Exception;
	public void searchAndSelectTeamMemberByName(String username) throws Exception;
	public void clickOnControlsLocationsSection() throws Exception;
	public boolean isLocationsPageLoaded() throws Exception;
	public void clickAllDistrictsOrAllLocationsTab(boolean isClickDistrictsTab) throws Exception;
	public void goToSpecificLocationDetailPageByLocationName (String locationName) throws Exception;
	public String getLocationInfoStringFromDetailPage () throws Exception;
	public void clickOnBackButtonOnLocationDetailPage() throws Exception;
	public void turnONClopeningToggleAndSetHours(int clopeningHours) throws Exception;
	public void selectClopeningHours(int clopeningHour) throws Exception;
	public void updateCanManagerAddAnotherLocationsEmployeeInScheduleBeforeTheEmployeeHomeLocationHasPublishedTheSchedule(String option) throws Exception;
	public void setSchedulingMinorRuleFor14N15(String from, String to, String parameter1, String parameter2, String parameter3, String parameter4) throws Exception;
	public void setSchedulingMinorRuleFor16N17(String from, String to, String parameter1, String parameter2, String parameter3, String parameter4) throws Exception;
	public void searchUserByFirstName(String userFirstName) throws Exception;
	public void verifyUpdateUserAndRolesOneUserLocationInfo(String userFirstName) throws Exception;
	public void clickOnLocationsTabInGlobalModel() throws Exception;
	public List<String> getAllLocationsInGlobalModel() throws Exception;
	public String getCurrentLocationInControls() throws Exception;
	public void verifyRolePermissionExists(String section, String permission) throws Exception;
	public void turnOnOrOffSpecificPermissionForSM(String section, String permission, String action) throws Exception;
	public boolean isCentralizedScheduleReleaseValueYes() throws Exception;

	public List<WebElement> getAvailableSelector();

	public void updateCentralizedScheduleRelease(WebElement yesItem) throws Exception;
	public String getDaysInAdvancePublishSchedulesInSchedulingPolicies() throws Exception;
	public void updateDaysInAdvancePublishSchedulesInSchedulingPolicies(String days) throws Exception;
	public void verifyConvertUnassignedShiftsToOpenSetting() throws Exception;
	public String getConvertUnassignedShiftsToOpenSettingOption() throws Exception;
	public void updateConvertUnassignedShiftsToOpenSettingOption(String option) throws Exception;
	public void setAutomaticallySetOnboardedEmployeesToActive(String yesOrNo) throws Exception;
	public boolean getTheSettingForAutomaticallySetOnboardedEmployeesToActive() throws Exception;
	public void selectAccessRoles (List<String> selectAccessRoles) throws Exception;
	public boolean hasCompanyMobilePolicyURLOrNot () throws Exception;
	public void verifyUsersAreLoaded() throws Exception;
	public void disableAllDayparts() throws Exception;
	public void enableDaypart(String dayPart) throws Exception;
	public void turnOnOrOffSpecificPermissionForDifferentRole(String rolePermission, String section, String permission, String action) throws Exception;
	public void setDaypart(String dayPart, String startTime, String endTime) throws Exception;
	public String getApplyLaborBudgetToSchedulesActiveBtnLabel() throws Exception;
	public void turnOnOrTurnOffSpreadOfHoursToggle(boolean action) throws Exception;
	public void editSpreadOfHoursPremium(String numOfPremiumHrs, String greaterThan, boolean saveOrNot) throws Exception;
	public void verifyCloseSplitShiftPremiumDialogButton () throws Exception;
	public void turnOnOrTurnOffMealBreakToggle(boolean action) throws Exception;
	public void editMealBreak(String mealBreakDuration, String paidType, String scheduleHoursLimit, boolean saveOrNot) throws Exception;
	public void verifyLockEmployeeAvailabilityEditsIsLoaded () throws Exception;
	public void updateLockEmployeeAvailabilityEdits(String lockOption) throws Exception;
	public void verifyTheSectionsOnWorkingHoursPage() throws Exception;
	public boolean checkIfWorkHoursTypeCollapsed(String title) throws Exception;
	public void turnOnOrOffSpecificHolidayHours(String holidayName, String action) throws Exception;
	public void turnOnOrOffSpecificRegularWorkingHours(String regularDay, String action) throws Exception;
	public void updateControlsRegularHours(String openingHours, String closingHours, String day, List<String> applyToOtherDays) throws Exception;
	public void clickOnCancelBtn() throws Exception;
	public LinkedHashMap<String, List<String>> getHolidayWorkingHours() throws Exception;
	public void updateControlsHolidayHours(String openingHours, String closingHours, String day, List<String> applyToOtherDays) throws Exception;
	public void clickOnSaveBtn() throws Exception;
	public void clickOnManageBtn() throws Exception;
	public List<String> getAllSelectedCompanyHolidays ();
	public void searchSpecificCompanyHolidays (String companyHolidays) throws Exception;
	public void checkOrUncheckSpecificCompanyHolidays (Boolean isCheck, String companyHoliday) throws Exception;
	public void setFixedHoursForSpecificCompanyHolidays (String companyHoliday, String fixedHours) throws Exception;
	public LinkedHashMap<String, List<String>> getCompanyHolidaysInSearchResult() throws Exception;
	public void turnOnOrTurnOffDailyOTToggle(boolean action) throws Exception;
	public void editDailyOT(String numOfHrs, String singleDayOr24Hrs, boolean saveOrNot) throws Exception;
	public boolean checkDailyOTEnabledOrNot() throws Exception;
	public boolean checkIfTheLocationUsingControlsConfiguration() throws Exception;
	public boolean checkIfEmployeeCanClaimOTOpenShift() throws Exception;
	public void selectClopeningHoursOP(String clopeningHour) throws Exception;
	public String getBudgetGroupSettingContentNonOP() throws Exception;
	public void selectBudgetGroupNonOP(String optionValue) throws Exception;
	public void turnOnOrOffSpecificPermissionForSpecificRoles(String section, String role, String permission, String action) throws Exception;
	public void enableOverRideAssignmentRuleAsYesForOP() throws Exception;
	public void enableOverRideAssignmentRuleAsNoForOP() throws Exception;
	public void isSenioritySectionLoaded() throws Exception;
	public String getSeniorityToggleActiveBtnLabel() throws Exception;
	public void updateSeniorityToggle(String isSeniorityToggleOpen) throws Exception;
	public void isSortOfSenioritySectionLoaded() throws Exception;
	public void selectSortOfSeniority(String seniorityType, String optionValue) throws Exception;
	public String getSenioritySort() throws Exception;
	public boolean isScheduleRestrictionLoaded() throws Exception;
	public void isAccrualTimeOverrideLoaded() throws Exception;
	public void updateAccrualTimeOverrideToggle(String isToggleOpen) throws Exception;
	public String getAccrualTimeOverrideToggleActiveBtnLabel() throws Exception;
	public boolean isDeleteTimeOffBtnLoaded() throws Exception;
	public void clickDeleteTimeOffBtn() throws Exception;
	public boolean isAddTimeOffBtnLoaded() throws Exception;
	public boolean isAddTimeOffBtnClickable() throws Exception;
	public void addTimeOffReasonOnSchedulingPolicy(String timeOffReason, String maximum) throws Exception;
	public void modifyTimeOffReasonOnSchedulingPolicy(String timeOffReason, String maximum) throws Exception;
	public List<String> getTimeOffReasonsOnSchedulingPolicy() throws Exception;
	public void clickAddTimeOffBtn() throws Exception;
	public boolean getStatusOfSpecificPermissionForSpecificRoles(String section, String roles, String permission) throws Exception;
	public void selectSeniorityType(String optionValue) throws Exception;
	public void isAutoGroupSectionLoaded() throws Exception;
	public String getAutoGroupToggleActiveBtnLabel() throws Exception;
	public void updateAutoGroupToggle(String isAutoGroupToggleOpen) throws Exception;
	public void selectOpenShiftGroupRule(ArrayList<String> optionValues) throws Exception;
	public boolean isOpenShiftGroupRuleSectionLoaded() throws Exception;
	public void verifyTextInOpenShiftGroupRuleInputBox(String text) throws Exception;
	public boolean isAutoApprovalSectionLoaded() throws Exception;
	public void setAutoApprovalAdvancedHours(String hours) throws Exception;
	public String getAutoApprovalAdvancedHours() throws Exception;
}
