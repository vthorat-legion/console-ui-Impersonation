package com.legion.pages;

public interface ImpersonationPage {
    public void gotoControlsPage() throws Exception ;
    public void endImpersonationSession () throws Exception ;
    public void goToLegionProfile() throws Exception ;
    public void checkAcceptedToSAndImpersonateUser(String Username) throws Exception ;
    public void createNewTemplate(String TemplateName) throws Exception ;
    public void checkTemplateHistory(String TemplateName) throws Exception ;
    public void switchToEmployeeView() throws Exception  ;
    public void switchBackToManagerView() throws Exception ;
    public void confirmSessionEnds() throws Exception ;
//	public void clickOnScheduleConsoleMenuItem();
//	public void goToSchedulePage() throws Exception;
//	public boolean isSchedulePage() throws Exception;
//	public Boolean verifyActivatedSubTab(String SubTabText) throws Exception;
//	public void goToSchedule() throws Exception;
//	public void goToProjectedSales() throws Exception;
//	public void goToStaffingGuidance() throws Exception;
//	public void clickOnWeekView() throws Exception;
//	public void clickOnDayView() throws Exception;
//	public HashMap<String, Float> getScheduleLabelHoursAndWages() throws Exception;
//	public List<HashMap<String, Float>> getScheduleLabelHoursAndWagesDataForEveryDayInCurrentWeek() throws Exception;
//	public void clickOnScheduleSubTab(String subTabString) throws Exception;
//	public void navigateWeekViewOrDayViewToPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount);
//	public Boolean isWeekGenerated() throws Exception;
//	public Boolean isWeekPublished() throws Exception;
//	public void generateSchedule() throws Exception;
//	public String getScheduleWeekStartDayMonthDate();
//	public void clickOnEditButton() throws Exception;
//	public void clickImmediateNextToCurrentActiveWeekInDayPicker() throws Exception;
//	public Boolean isAddNewDayViewShiftButtonLoaded() throws Exception;
//	public void clickOnCancelButtonOnEditMode() throws Exception;
//	public Boolean isGenerateButtonLoaded() throws Exception;
//	public String getActiveWeekDayMonthAndDateForEachDay() throws Exception;
//	public Boolean validateScheduleActiveWeekWithOverviewCalendarWeek(String overviewCalendarWeekDate, String overviewCalendarWeekDays, String scheduleActiveWeekDuration);
//	public boolean isCurrentScheduleWeekPublished();
//	public void validatingRefreshButtononPublishedSchedule() throws Exception;
//	public void isGenerateScheduleButton() throws Exception;
//	public void validatingScheduleRefreshButton() throws Exception;
//	public void clickOnSchedulePublishButton() throws Exception;
//	public void clickConfirmBtnOnPublishConfirmModal() throws Exception;
//	public boolean isComplianceWarningMsgLoad() throws Exception;
//	public void clickPublishBtn() throws Exception;
//	public String getMessageForComplianceWarningInPublishConfirmModal() throws Exception;
//	public void navigateDayViewToPast(String nextWeekViewOrPreviousWeekView, int weekCount) throws Exception;
//	public String clickNewDayViewShiftButtonLoaded() throws Exception;
//	public void customizeNewShiftPage() throws Exception;
//	public void compareCustomizeStartDay(String textStartDay) throws Exception;
//	public void moveSliderAtSomePoint(String shiftTime, int shiftStartingCount, String startingPoint) throws Exception;
//	public HashMap<String, String> calculateHourDifference() throws Exception;
//	public void selectWorkRole(String workRoles) throws Exception;
//	public void clickRadioBtnStaffingOption(String staffingOption) throws Exception;
//	public void clickOnCreateOrNextBtn() throws Exception;
//	public HashMap<List<String>,List<String>> calculateTeamCount()throws Exception;
//	public List<String> calculatePreviousTeamCount(
//			HashMap<String, String> previousTeamCount, HashMap<List<String>,List<String>>
//			gridDayHourPrevTeamCount)throws Exception;
//	public List<String> calculateCurrentTeamCount(HashMap<String, String> shiftTiming)throws Exception;
//	public void clickSaveBtn() throws Exception;
//	public void clickOnVersionSaveBtn() throws Exception;
//	public void clickOnPostSaveBtn() throws Exception;
//	public void filterScheduleByWorkRoleAndShiftType(boolean isWeekView) throws Exception;
//	public void selectGroupByFilter(String optionVisibleText);
//	public void verifyNewAddedShiftFallsInDayPart(String nameOfTheShift, String dayPart) throws Exception;
//	public String getActiveWeekText() throws Exception;
//	public ArrayList<WebElement> getAllAvailableShiftsInWeekView();
//	public ArrayList<HashMap<String, String>> getHoursAndShiftsCountForEachWorkRolesInWeekView() throws Exception;
//	public ArrayList<Float> getAllVesionLabels() throws Exception;
//	public void publishActiveSchedule()throws Exception;
//	public boolean isPublishButtonLoaded();
//	public HashMap<String, Float> getScheduleLabelHours() throws Exception;
//	public int getgutterSize();
//	public void verifySelectTeamMembersOption() throws Exception;
//	public void searchText(String searchInput) throws Exception;
//	public void getAvailableStatus() throws Exception;
//	public void clickOnOfferOrAssignBtn() throws Exception;
//	public void clickOnShiftContainer(int index) throws Exception;
//	public void deleteShift() throws Exception;
//	public void deleteAllShiftsInDayView() throws Exception;
//	public void deleteShiftGutterText();
//	public boolean getScheduleStatus() throws Exception;
//	public boolean inActiveWeekDayClosed(int dayIndex) throws Exception;
//	public void navigateDayViewWithIndex(int dayIndex);
//	public String getActiveGroupByFilter() throws Exception;
//	public boolean isActiveWeekHasOneDayClose() throws Exception;
//	public boolean isActiveWeekAssignedToCurrentUser(String userName) throws Exception;
//	public boolean isScheduleGroupByWorkRole(String workRoleOption) throws Exception;
//	public void selectWorkRoleFilterByIndex(int index, boolean isClearWorkRoleFilters) throws Exception;
//	public ArrayList<String> getSelectedWorkRoleOnSchedule() throws Exception;
//	public boolean isRequiredActionUnAssignedShiftForActiveWeek() throws Exception;
//	public void clickOnRefreshButton() throws Exception;
//	public void selectShiftTypeFilterByText(String filterText) throws Exception;
//	public List<WebElement> getAvailableShiftsInDayView();
//	public void dragShiftToRightSide(WebElement shift, int xOffSet);
//	public boolean isSmartCardAvailableByLabel(String cardLabel) throws Exception;
//	public void validateBudgetPopUpHeader(String nextWeekView, int weekCount);
//	public void noBudgetDisplayWhenBudgetNotEntered(String nextWeekView, int weekCount);
	//	public void budgetHourInScheduleNBudgetedSmartCard(String nextWeekView, int weekCount);
//	public void budgetHourByWagesInScheduleNBudgetedSmartCard(String nextWeekView,int weekCount);
//	public void budgetInScheduleNBudgetSmartCard(String nextWeekView, int weekCount, int tolerance);
//	public void disableNextWeekArrow() throws Exception;
//	public void clickScheduleDraftAndGuidanceStatus(List<String> overviewScheduleWeeksStatus);
//	public void editBudgetHours();
//	public void navigateScheduleDayWeekView(String nextWeekView, int weekCount);

//	public ArrayList<String> getActiveWeekCalendarDates() throws Exception;
//	public void refreshBrowserPage() throws Exception;
//	public void addOpenShiftWithDefaultTime(String workRole) throws Exception;
//	public void addOpenShiftWithFirstDay(String workRole) throws Exception;
//	public boolean isNextWeekAvaibale() throws Exception;
//	public boolean isSmartCardPanelDisplay() throws Exception;
//	public void convertAllUnAssignedShiftToOpenShift() throws Exception;
//	public void selectWorkRoleFilterByText(String workRoleLabel, boolean isClearWorkRoleFilters) throws Exception;
//	public void reduceOvertimeHoursOfActiveWeekShifts() throws Exception;
//	public boolean isActionButtonLoaded(String actionBtnText) throws Exception;
//	public void navigateToNextDayIfStoreClosedForActiveDay() throws Exception;
	/*public void validatingRequiredActionforUnAssignedShift() throws Exception;*/
//	public String getsmartCardTextByLabel(String cardLabel);
//	public String getWeatherTemperature() throws Exception;
//	public void validatingGenrateSchedule() throws Exception;
//	public boolean loadSchedule() throws Exception;
//	public void generateOrUpdateAndGenerateSchedule() throws Exception;
//	public void createScheduleForNonDGFlowNewUI() throws Exception;
//	public void clickOnFinishButtonOnCreateSchedulePage() throws Exception;
//	public void selectWhichWeekToCopyFrom(String weekInfo) throws Exception;
//	public void editTheOperatingHours(List<String> weekDaysToClose) throws Exception;
//	public HashMap<String, Integer> getScheduleBufferHours() throws Exception;
//	public boolean isComlianceReviewRequiredForActiveWeek() throws Exception;
//	public void unGenerateActiveScheduleScheduleWeek() throws Exception;
//	public boolean isStoreClosedForActiveWeek() throws Exception;
//	public int getScheduleShiftIntervalCountInAnHour() throws Exception;
//	public void toggleSummaryView() throws Exception;
//	public boolean isSummaryViewLoaded() throws Exception;
//	public void updateScheduleOperatingHours(String day, String startTime, String endTime) throws Exception;
//	public void dragRollerElementTillTextMatched(WebElement rollerElement, String textToMatch, boolean startHrsSlider) throws Exception;;
//	public boolean isScheduleOperatingHoursUpdated(String startTime, String endTime) throws Exception;
//	public void verifyScheduledHourNTMCountIsCorrect() throws Exception;
//	public void complianceShiftSmartCard() throws Exception;
//	public void viewProfile() throws Exception;
//	public void changeWorkerRole() throws Exception;
//	public void assignTeamMember() throws Exception;
//	public void convertToOpenShift() throws Exception;
//	public void beforeEdit();
//	public void selectTeamMembersOptionForOverlappingSchedule() throws Exception;
//	public boolean getScheduleOverlappingStatus()throws Exception;
//	public void searchWorkerName(String searchInput) throws Exception;
//	public void verifyScheduleStatusAsOpen() throws Exception;
//	public void verifyScheduleStatusAsTeamMember() throws Exception;
//	public String getActiveAndNextDay() throws Exception;
//	public HashMap<String, String> getOperatingHrsValue(String day) throws Exception;
//	public void moveSliderAtCertainPoint(String shiftTime, String startingPoint) throws Exception;
//	public void clickOnNextDaySchedule(String activeDay) throws Exception;
//	public void selectTeamMembersOptionForSchedule() throws Exception;
//	public void selectTeamMembersOptionForScheduleForClopening() throws Exception;
//	public void verifyClopeningHrs() throws Exception;
//	public void clickOnPreviousDaySchedule(String activeDay) throws Exception;
//	public void verifyActiveScheduleType() throws Exception;
//	public List<Float> validateScheduleAndBudgetedHours() throws Exception;
//	public void compareHoursFromScheduleAndDashboardPage(List<Float> totalHoursFromSchTbl) throws Exception;
//	public List<Float> getHoursOnLocationSummarySmartCard() throws Exception;
//	public void compareHoursFromScheduleSmartCardAndDashboardSmartCard(List<Float> totalHoursFromSchTbl) throws Exception;
//	public void compareProjectedWithinBudget(float totalCountProjectedOverBudget) throws Exception;
//	public float getProjectedOverBudget();
//	public String getDateFromDashboard() throws Exception;
//	public void compareDashboardAndScheduleWeekDate(String DateOnSchdeule, String DateOnDashboard) throws Exception;
//	public List<String> getLocationSummaryDataFromDashBoard() throws Exception;
//	public List<String> getLocationSummaryDataFromSchedulePage() throws Exception;
//	public void compareLocationSummaryFromDashboardAndSchedule(List<String> ListLocationSummaryOnDashboard, List<String> ListLocationSummaryOnSchedule);
//	public void openBudgetPopUpGenerateSchedule() throws Exception;
//	public void updatebudgetInScheduleNBudgetSmartCard(String nextWeekView, int weekCount);
//	public void toNFroNavigationFromDMToSMSchedule(String CurrentWeek, String locationToSelectFromDMViewSchedule, String districtName, String nextWeekViewOrPreviousWeekView) throws Exception;
//	public void toNFroNavigationFromDMDashboardToDMSchedule(String CurrentWeek) throws Exception;
//	public void clickOnViewScheduleLocationSummaryDMViewDashboard();
//	public void clickOnViewSchedulePayrollProjectionDMViewDashboard();
//	public void loadingOfDMViewSchedulePage(String SelectedWeek) throws Exception;
//	public void districtSelectionSMView(String districtName) throws Exception;
//	public void isScheduleForCurrentDayInDayView(String dateFromDashboard) throws Exception;
//	public HashMap<String, String> getHoursFromSchedulePage() throws Exception;
//	public void printButtonIsClickable() throws Exception;
//	public void todoButtonIsClickable() throws Exception;
//	public void legionButtonIsClickableAndHasNoEditButton() throws Exception;
//	public void clickOnSuggestedButton() throws Exception;
//	public void legionIsDisplayingTheSchedul() throws Exception;

//	public void currentWeekIsGettingOpenByDefault(String location) throws Exception;

//	public void goToScheduleNewUI() throws Exception;

//	public void dayWeekPickerSectionNavigatingCorrectly() throws Exception;

//	public void landscapePortraitModeShowWellInWeekView() throws Exception;

//	public void landscapeModeWorkWellInWeekView() throws Exception;

//	public void portraitModeWorkWellInWeekView() throws Exception;

//	public HashMap<String, String> getFourUpComingShifts(boolean isStartTomorrow, String currentTime) throws Exception;
//	public void landscapeModeOnlyInDayView() throws Exception;

//	public void weatherWeekSmartCardIsDisplayedForAWeek() throws Exception;

//	public void scheduleUpdateAccordingToSelectWeek() throws Exception;

//	public boolean verifyRedFlagIsVisible() throws Exception;

//	public boolean verifyComplianceShiftsSmartCardShowing() throws Exception;

//	public boolean clickViewShift() throws Exception;

//	public void verifyComplianceFilterIsSelectedAftClickingViewShift() throws Exception;

//	public void verifyComplianceShiftsShowingInGrid() throws Exception;

//	public void verifyClearFilterFunction() throws Exception;

//	public void clickOnFilterBtn() throws Exception;



//	public void  verifyShiftSwapCoverRequestedIsDisplayInTo();

//	public void verifyAnalyzeBtnFunctionAndScheduleHistoryScroll() throws Exception;

//	public HashMap<String, Float> getScheduleBudgetedHoursInScheduleSmartCard() throws Exception;


	//	public HashMap<String, String> getFourUpComingShifts(boolean isStartTomorrow) throws Exception;
//	public void verifyUpComingShiftsConsistentWithSchedule(HashMap<String, String> dashboardShifts, HashMap<String, String> scheduleShifts) throws Exception;
//	public void clickOnCreateNewShiftButton() throws Exception;
//	public void verifyTeamCount(List<String> previousTeamCount, List<String> currentTeamCount) throws Exception;
//	public void selectDaysFromCurrentDay(String currentDay) throws Exception;
//	public void searchTeamMemberByName(String name) throws Exception;
//	public void searchTeamMemberByNameNLocation(String name, String location) throws Exception;
//	public void verifyNewShiftsAreShownOnSchedule(String name) throws Exception;
//	public void verifyShiftsChangeToOpenAfterTerminating(List<Integer> indexes, String name, String currentTime) throws Exception;
//	public List<Integer> getAddedShiftIndexes(String name) throws Exception;
//	public boolean areShiftsPresent() throws Exception;
//	public int verifyClickOnAnyShift() throws Exception;
//	public void clickTheShiftRequestByName(String requestName) throws Exception;
//	public boolean isPopupWindowLoaded(String title) throws Exception;
//	public void verifyComponentsOnSubmitCoverRequest() throws Exception;
//	public void verifyClickOnSubmitButton() throws Exception;
//	public void clickOnShiftByIndex(int index) throws Exception;
//	public boolean verifyShiftRequestButtonOnPopup(List<String> requests) throws Exception;
//	public void	verifyComparableShiftsAreLoaded() throws Exception;
//	public String selectOneTeamMemberToSwap() throws Exception;
//	public void verifyClickCancelSwapOrCoverRequest() throws Exception;
//	public void goToSchedulePageAsTeamMember() throws Exception;
//	public void gotoScheduleSubTabByText(String subTitle) throws Exception;
//	public void	verifyTeamScheduleInViewMode() throws Exception;
//	public List<String> getWholeWeekSchedule() throws Exception;
//	public String getSelectedWeek() throws Exception;
//	public void verifySelectOtherWeeks() throws Exception;
//	public boolean isSpecificSmartCardLoaded(String cardName) throws Exception;
//	public int getCountFromSmartCardByName(String cardName) throws Exception;
//	public void clickLinkOnSmartCardByName(String linkName) throws Exception;
//	public int getShiftsCount() throws Exception;
//	public void filterScheduleByShiftTypeAsTeamMember(boolean isWeekView) throws Exception;
//	public boolean isPrintIconLoaded() throws Exception;
//	public void verifyThePrintFunction() throws Exception;
//	public void clickCancelButtonOnPopupWindow() throws Exception;
//	public void verifyTheDataOfComparableShifts() throws Exception;
//	public void verifyTheSumOfSwapShifts() throws Exception;
//	public void verifyNextButtonIsLoadedAndDisabledByDefault() throws Exception;
//	public void verifySelectOneShiftNVerifyNextButtonEnabled() throws Exception;
//	public void verifySelectMultipleSwapShifts() throws Exception;
//	public void verifyClickOnNextButtonOnSwap() throws Exception;
//	public void verifyBackNSubmitBtnLoaded() throws Exception;
//	public void verifyTheRedirectionOfBackButton() throws Exception;
//	public void verifySwapRequestShiftsLoaded() throws Exception;
//	public void verifyClickAcceptSwapButton() throws Exception;
//	public void verifyTheContentOfMessageOnSubmitCover() throws Exception;
//	public void verifyShiftRequestStatus(String expectedStatus) throws Exception;
//	public Boolean isGenerateButtonLoadedForManagerView() throws Exception;

//	public void validateGroupBySelectorSchedulePage(boolean isLocationGroup) throws Exception;
//	public boolean checkEditButton() 	throws Exception;
//	public void verifyEditButtonFuntionality() 		throws Exception;
//	public boolean checkCancelButton() 	throws Exception;
//	public void selectCancelButton()	throws Exception;
//	public boolean checkSaveButton()	throws Exception;
//	public void selectSaveButton()		throws Exception;
//	public boolean isScheduleFinalized() throws Exception;
//	public boolean isProfileIconsEnable() throws Exception;
//	public boolean isProfileIconsClickable() throws Exception;
//	public WebElement clickOnProfileIcon() throws Exception;
//	public boolean isViewProfileEnable() throws Exception;
//	public boolean isViewOpenShiftEnable() throws Exception;
//	public boolean isChangeRoleEnable() throws Exception;
//	public boolean isAssignTMEnable() throws Exception;
//	public boolean isConvertToOpenEnable() throws Exception;
//	public void selectNextWeekSchedule() throws Exception;
//	public void clickOnViewProfile() throws Exception;
//	public void clickOnChangeRole() throws Exception;
//	public boolean validateVariousWorkRolePrompt() throws Exception;
//	public void verifyRecommendedAndSearchTMEnabled() throws Exception;
//	public void clickonAssignTM() throws Exception;
//	public void clickOnConvertToOpenShift() throws Exception;
//	public void verifyPersonalDetailsDisplayed() throws Exception;
//	public void verifyWorkPreferenceDisplayed() throws Exception;
//	public void verifyAvailabilityDisplayed() throws Exception;
//	public void closeViewProfileContainer() throws Exception;


//	public boolean  verifyContextOfTMDisplay() throws Exception;

//	public void verifyChangeRoleFunctionality() throws Exception;

//	public void verifyMealBreakTimeDisplayAndFunctionality(boolean isEditMealBreakEnabled) throws Exception;

//	public void verifyDeleteShift() throws Exception;

//	public void clickOnEditMeaLBreakTime() throws Exception;

//	public void clickOnEditButtonNoMaterScheduleFinalizedOrNot() throws Exception;

//	public void clickOnOpenShitIcon();

//	public String getTimeDurationWhenCreateNewShift() throws Exception;

//	public void verifyConvertToOpenShiftBySelectedSpecificTM() throws Exception;

//	public int getOTShiftCount();

//	public void saveSchedule() throws Exception;

//	public void validateXButtonForEachShift() throws Exception;

//	public void selectSpecificWorkDay(int dayCountInOneWeek);

//	public float getShiftHoursByTMInWeekView(String teamMember);

//	public void selectSpecificTMWhileCreateNewShift(String teamMemberName) throws Exception;

//	public void verifyWeeklyOverTimeAndFlag(String teamMemberName) throws Exception;

//	public void deleteTMShiftInWeekView(String teamMemberName) throws Exception;

//	public Map<String, String> getHomeLocationInfo() throws Exception;

//	public WebElement clickOnProfileOfUnassignedShift() throws Exception;

//	public void clickOnCancelPublishBtn() throws Exception;

//	public void filterScheduleByJobTitle(boolean isWeekView) throws Exception;

//	public void filterScheduleByWorkRoleAndJobTitle(boolean isWeekView) throws Exception;

//	public void filterScheduleByShiftTypeAndJobTitle(boolean isWeekView) throws Exception;

//	public boolean verifyConvertToOpenPopUpDisplay(String firstNameOfTM) throws Exception;
//
//	void convertToOpenShiftDirectly();

	// Added by Nora
//	void verifyScheduledNOpenFilterLoaded() throws Exception;
//	void checkAndUnCheckTheFilters() throws Exception;
//	void filterScheduleByBothAndNone() throws Exception;
//	String selectOneFilter() throws Exception;
//	void verifySelectedFilterPersistsWhenSelectingOtherWeeks(String selectedFilter) throws Exception;
//	int selectOneShiftIsClaimShift(List<String> claimShift) throws Exception;
//	void verifyClaimShiftOfferNBtnsLoaded() throws Exception;
//	List<String> getShiftHoursFromInfoLayout() throws Exception;
//	void verifyTheShiftHourOnPopupWithScheduleTable(String scheduleShiftTime, String weekDay) throws Exception;
//	String getSpecificShiftWeekDay(int index) throws Exception;
//	void verifyClickAgreeBtnOnClaimShiftOffer() throws Exception;
//	public void verifyClickAgreeBtnOnClaimShiftOfferWhenDontNeedApproval() throws Exception;
//	void verifyClickCancelBtnOnClaimShiftOffer() throws Exception;
//	void verifyTheColorOfCancelClaimRequest(String cancelClaim) throws Exception;
//	void verifyReConfirmDialogPopup() throws Exception;
//	void verifyClickNoButton() throws Exception;
//	void verifyClickOnYesButton() throws Exception;
//	void verifyTheFunctionalityOfClearFilter() throws Exception;

//	void validateTheAvailabilityOfScheduleTable(String userName) throws Exception;
//
//	void validateTheAvailabilityOfScheduleMenu() throws Exception;
//
//	void validateTheFocusOfSchedule() throws Exception;
//
//	void validateTheDefaultFilterIsSelectedAsScheduled() throws Exception;
//
//	void validateTheFocusOfWeek(String currentDate) throws Exception;

//	void validateForwardAndBackwardButtonClickable() throws Exception;

//	void validateTheDataAccordingToTheSelectedWeek() throws Exception;

//	void validateTheSevenDaysIsAvailableInScheduleTable() throws Exception;

//	public String getTheEarliestAndLatestTimeInSummaryView(HashMap<String, Integer> schedulePoliciesBufferHours) throws Exception;

//	String getTheEarliestAndLatestTimeInScheduleTable() throws Exception;

//	void compareOperationHoursBetweenAdminAndTM(String theEarliestAndLatestTimeInScheduleSummary, String theEarliestAndLatestTimeInScheduleTable) throws Exception;

//	void validateThatHoursAndDateIsVisibleOfShifts() throws Exception;

//	void validateTheDisabilityOfLocationSelectorOnSchedulePage() throws Exception;

//	void goToConsoleScheduleAndScheduleSubMenu() throws Exception;

//	void validateProfilePictureInAShiftClickable() throws Exception;

//	void validateTheDataOfProfilePopupInAShift() throws Exception;

//	void validateTheAvailabilityOfInfoIcon() throws Exception;
//
//	void validateInfoIconClickable() throws Exception;

//	void validateTheAvailabilityOfOpenShiftSmartcard() throws Exception;

//	void validateViewShiftsClickable() throws Exception;

//	void validateTheNumberOfOpenShifts() throws Exception;

//	void verifyTheAvailabilityOfClaimOpenShiftPopup() throws Exception;

//	List<String> getWeekScheduleShiftTimeListOfMySchedule() throws Exception;

//	List<String> getWeekScheduleShiftTimeListOfWeekView(String teamMemberName) throws Exception;

//	void clickTheShiftRequestToClaimShift(String requestName, String requestUserName) throws Exception;

//	void navigateToNextWeek() throws Exception;

//	public void navigateToPreviousWeek() throws Exception;

//	void verifyShiftsAreSwapped(List<String> swapData) throws Exception;

//	void clickOnDayViewAddNewShiftButton() throws Exception;

//	void addNewShiftsByNames(List<String> names, String workRole) throws Exception;
//	boolean displayAlertPopUp() throws Exception;
//	public void displayAlertPopUpForRoleViolation() throws Exception;

//	public void unGenerateActiveScheduleFromCurrentWeekOnward(int loopCount) throws Exception;
//	public List<String> getOverviewData() throws Exception;
//	public void addOpenShiftWithLastDay(String workRole) throws Exception;
//	public void deleteLatestOpenShift() throws Exception;
//	public void addManualShiftWithLastDay(String workRole, String tmName) throws Exception;
//	public HashMap<String, String> getTheHoursNTheCountOfTMsForEachWeekDays() throws Exception;
//	public HashMap<String, List<String>> getTheContentOfShiftsForEachWeekDay() throws Exception;
//	public HashMap<String, String> getBudgetNScheduledHoursFromSmartCard() throws Exception;
//	public int getComplianceShiftCountFromSmartCard(String cardName) throws Exception;
//	public float createScheduleForNonDGByWeekInfo(String weekInfo, List<String> weekDaysToClose, List<String> copyShiftAssignments) throws Exception;
//	public void clickCreateScheduleBtn() throws Exception;
//	public boolean isPartialCopyOptionLoaded() throws Exception;
//	public void clickBackBtnAndExitCreateScheduleWindow() throws Exception;
//	public void editOperatingHoursWithGivingPrameters(String day, String startTime, String endTime) throws Exception;
//	public void createScheduleForNonDGFlowNewUIWithGivingParameters(String day, String startTime, String endTime) throws Exception;
//	public void createScheduleForNonDGFlowNewUIWithGivingTimeRange(String startTime, String endTime) throws Exception;
//	public void goToToggleSummaryView() throws Exception;
//	public void verifyOperatingHrsInToggleSummary(String day, String startTime, String endTime) throws Exception;
//	public void verifyDayHasShifts(String day) throws Exception;
//	public List<String> getDayShifts(String index) throws Exception;
//	public void verifyThePopupMessageOnTop(String expectedMessage) throws Exception;
//	public void verifyNoShiftsForSpecificWeekDay(List<String> weekDaysToClose) throws Exception;
//	public void verifyStoreIsClosedForSpecificWeekDay(List<String> weekDaysToClose) throws Exception;
//	public void verifyClosedDaysInToggleSummaryView(List<String> weekDaysToClose) throws Exception;
//	public String getWeekInfoBeforeCreateSchedule() throws Exception;
//	public void verifyTheContentOnEnterBudgetWindow(String weekInfo, String location) throws Exception;
//	public List<String> setAndGetBudgetForNonDGFlow() throws Exception;
//	public HashMap<String, String> verifyNGetBudgetNScheduleWhileCreateScheduleForNonDGFlowNewUI(String weekInfo, String location) throws Exception;
//	public List<String> getBudgetedHoursOnSTAFF() throws Exception;
//	public String getBudgetOnWeeklyBudget() throws Exception;
//	public void verifyChangesNotPublishSmartCard(int changesNotPublished) throws Exception;
//	public void dragOneShiftToMakeItOverTime() throws Exception;
//	public String getChangesOnActionRequired() throws Exception;
//	public String getTooltipOfUnpublishedDeleted() throws Exception;
//	public void verifyLabelOfPublishBtn(String labelExpected) throws  Exception;
//	public void verifyMessageIsExpected(String messageExpected) throws Exception;
//	public void verifyWarningModelForAssignTMOnTimeOff(String nickName) throws Exception;
//	public void selectAShiftToAssignTM(String username) throws Exception;
//	public void verifyInactiveMessageNWarning(String username, String date) throws Exception;
//	public List<String> getTheShiftInfoByIndex(int index) throws Exception;
//	public String getRandomWorkRole() throws Exception;
//	public List<String> getTheShiftInfoInDayViewByIndex(int index) throws Exception;
//	public void selectWorkingDaysOnNewShiftPageByIndex(int index) throws Exception;
//	public void verifyScheduledWarningWhenAssigning(String userName, String shiftTime) throws Exception;
//	public void validateScheduleTableWhenSelectAnyOfGroupByOptions(boolean isLocationGroup) throws Exception;
//	public void changeWorkRoleInPrompt(boolean isApplyChange) throws Exception;
//	public void switchSearchTMAndRecommendedTMsTab() throws Exception;
//	public String convertToOpenShiftAndOfferToSpecificTMs() throws Exception;
//	public void selectConvertToOpenShiftAndOfferToASpecificTMOption() throws Exception;
//	public void clickOnEditShiftTime() throws Exception;
//	public void verifyEditShiftTimePopUpDisplay() throws Exception;
//	public List<String> editShiftTime() throws Exception;
//	public void clickOnCancelEditShiftTimeButton() throws Exception;
//	public void clickOnUpdateEditShiftTimeButton() throws Exception;
//	public void verifyShiftTime(String shiftTime) throws Exception;
//	public String getShiftTime();
//	public void  verifyDeleteShiftCancelButton() throws Exception;
//	public void verifyDeleteMealBreakFunctionality() throws Exception;
//	public void verifyEditMealBreakTimeFunctionality(boolean isSavedChange) throws Exception;
//	public void editAndVerifyShiftTime(boolean isSaveChange) throws Exception;
//	public String selectTeamMembers() throws Exception;
//	public String selectTeamMembers(int numOfTM) throws Exception;
//	public void clickOnAnalyzeBtn() throws Exception;
//	public void verifyScheduleVersion(String version) throws Exception;
//	public void closeAnalyzeWindow() throws Exception;
//	public void verifyVersionInSaveMessage(String version) throws Exception;
//	public void clickOnManagerButton() throws Exception;
//	public void verifyAllShiftsAssigned() throws Exception;
//	public void clickProfileIconOfShiftByIndex(int index) throws Exception;
//	public void clickViewStatusBtn() throws Exception;
//	public void verifyListOfOfferNotNull() throws Exception;
//	public void clickOnOpenSearchBoxButton() throws Exception;
//	public void verifyGhostTextInSearchBox () throws Exception;
//	public List<WebElement> searchShiftOnSchedulePage(String searchText) throws Exception;
//	public void verifySearchResult (String firstNameOfTM, String lastNameOfTM, String workRole, String jobTitle, List<WebElement> searchResults) throws Exception;
//	public void clickOnCloseSearchBoxButton() throws Exception;
//	public void verifySearchBoxNotDisplayInDayView() throws Exception;
//	public int getRandomIndexOfShift();
//	public void goToSpecificWeekByDate(String date) throws Exception;
//	public void clearAllSelectedDays() throws Exception;
//	public List<Integer> selectDaysByCountAndCannotSelectedDate(int count, String cannotSelectedDate) throws Exception;
//	public void dragOneAvatarToAnother(int startIndex, String firstName, int endIndex) throws Exception;
//	public int getTheIndexOfTheDayInWeekView(String date) throws Exception;
//	public HashMap<String,Integer> dragOneAvatarToAnotherSpecificAvatar(int startIndexOfTheDay, String user1, int endIndexOfTheDay, String user2) throws Exception;
//	public void verifyMessageInConfirmPage(String expectedMassageInSwap, String expectedMassageInAssign) throws Exception;
//	public void verifyMessageOnCopyMoveConfirmPage(String expectedMsgInCopy, String expectedMsgInMove) throws Exception;
//	public void verifyConfirmBtnIsDisabledForSpecificOption(String optionName) throws Exception;
//	public void selectCopyOrMoveByOptionName(String optionName) throws Exception;
//	public void selectSwapOrAssignOption(String action) throws Exception;
//	public void clickConfirmBtnOnDragAndDropConfirmPage() throws Exception;
//	public WebElement getShiftById(String id) throws Exception;
//	public List<String> getShiftSwapDataFromConfirmPage(String action) throws Exception;
//	public int verifyDayHasShiftByName(int indexOfDay, String name) throws Exception;
//	public String getWeekDayTextByIndex(int index) throws Exception;
//	public void selectDaysByIndex(int index1, int index2, int index3) throws Exception;
//	public boolean verifySwapAndAssignWarningMessageInConfirmPage(String expectedMessage, String action) throws Exception;
//	public void clickCancelBtnOnDragAndDropConfirmPage() throws Exception;
//	public List<String> getOpenShiftInfoByIndex(int index) throws Exception;
//	public List<WebElement> getOneDayShiftByName(int indexOfDay, String name) throws Exception;
//	public List<String> getComplianceMessageFromInfoIconPopup(WebElement shift) throws Exception;
//	public void dragOneShiftToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception;
//	public String getNameOfTheFirstShiftInADay(int dayIndex) throws Exception;
//	public boolean ifWarningModeDisplay() throws Exception;
//	public String getWarningMessageInDragShiftWarningMode() throws Exception;
//	public void clickOnOkButtonInWarningMode() throws Exception;
//	public List<String> getSelectedDayInfoFromCreateShiftPage() throws Exception;
//	public void moveAnywayWhenChangeShift() throws Exception;
//	public boolean ifMoveAnywayDialogDisplay() throws Exception;
//	public void verifyShiftIsMovedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception;
//	public void verifyShiftIsCopiedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception;
//	public String getTheMessageOfTMScheduledStatus() throws Exception;
//	public void verifyWarningModelMessageAssignTMInAnotherLocWhenScheduleNotPublished() throws Exception;
//	public void verifyTMNotSelected() throws Exception;
//	public void verifyAlertMessageIsExpected(String messageExpected) throws Exception;
//	public void clickOnRadioButtonOfSearchedTeamMemberByName(String name) throws Exception;
//	public void clickOnAssignAnywayButton() throws Exception;
//	public WebElement getTheShiftByIndex(int index) throws Exception;
//	public void editShiftTimeToTheLargest() throws Exception;
//	public void closeCustomizeNewShiftWindow() throws Exception;
//	public List<String> getHolidaysOfCurrentWeek() throws Exception;
//	public String getAllTheWarningMessageOfTMWhenAssign() throws Exception;
//	public int getTheIndexOfCurrentDayInDayView() throws Exception;
//	public void selectWeekDaysByDayName(String dayName) throws Exception;
//	public void editOperatingHoursOnScheduleOldUIPage(String startTime, String endTime, List<String> weekDaysToClose) throws Exception;
//	public int getTheIndexOfEditedShift() throws Exception;
//	public void navigateToTheRightestSmartCard() throws Exception;
//	public boolean isEditMealBreakEnabled() throws Exception;
//	public void verifyTMSchedulePanelDisplay() throws Exception;
//	public  boolean suggestedButtonIsHighlighted() throws Exception;
//	public boolean verifyWFSFunction();
//	public void verifyPreviousWeekWhenCreateAndCopySchedule(String weekInfo, boolean shouldBeSelected) throws Exception;
//	public void clickNextBtnOnCreateScheduleWindow() throws Exception;
//	public float getStaffingGuidanceHrs() throws Exception;
//	public void verifyTooltipForCopyScheduleWeek(String weekInfo) throws Exception;
//	public String convertDateStringFormat(String dateString) throws Exception;
//	public void verifyDifferentOperatingHours(String weekInfo) throws Exception;
//	public boolean isScheduleDMView() throws Exception;
//	public int getShiftsNumberByName(String name) throws Exception;
//	public List<String> getLocationsInScheduleDMViewLocationsTable() throws Exception;
//	public void verifySortByColForLocationsInDMView(int index) throws Exception;
//	public void verifySearchLocationInScheduleDMView(String location) throws Exception;
//	public void clickOnLocationNameInDMView(String location) throws Exception;
//	public boolean isPublishButtonLoadedOnSchedulePage() throws Exception;
//	public boolean isRepublishButtonLoadedOnSchedulePage() throws Exception;
//	public boolean isCreateScheduleBtnLoadedOnSchedulePage() throws Exception;
//	public void clickOnRepublishButtonLoadedOnSchedulePage() throws Exception;
//	public List<Float> transferStringToFloat(List<String> listString) throws Exception;
//	public HashMap<String, Float> getValuesAndVerifyInfoForLocationSummaryInDMView(String upperFieldType, String weekType) throws Exception;
//	public void verifyClockedOrProjectedInDMViewTable(String expected) throws Exception;
//	public int getIndexOfColInDMViewTable(String colName) throws Exception;
//	public HashMap<String, Integer> getValueOnUnplannedClocksSummaryCardAndVerifyInfo() throws Exception;
//	public List<String> getListByColInTimesheetDMView(int index) throws Exception;
//	public HashMap<String, Integer> getValueOnUnplannedClocksSmartCardAndVerifyInfo() throws Exception;
//	public HashMap<String, String> getBudgetNScheduledHoursFromSmartCardOnDGEnv() throws Exception;
//	public void clickSpecificLocationInDMViewAnalyticTable(String location) throws Exception;
//	public boolean hasNextWeek() throws Exception;
//	public float getTotalProjectionOpenShiftsHoursForCurrentWeek() throws Exception;
//	public float newCalcTotalScheduledHourForDayInWeekView() throws Exception;
//	public boolean isLocationGroup();
//	public void selectRandomOrSpecificLocationOnUngenerateScheduleEditOperatingHoursPage(String locationName) throws Exception;
//	public boolean checkIfEditOperatingHoursButtonsAreShown() throws Exception;
//	public void clickExitBtnToExitCreateScheduleWindow() throws Exception;
//	public boolean isScheduleNotPublishedSmartCardLoaded() throws Exception;
//	public void clickToggleSummaryViewButton() throws Exception;
//	public boolean isHourFormat24Hour() throws Exception;
//	public boolean isLocationLoaded() throws Exception;
//	public void selectLocation(String location) throws Exception;
//	public void addOpenShiftWithDefaultTime(String workRole, String location) throws Exception;
//	public void selectLocationFilterByText(String filterText) throws Exception;
//	public void selectChildLocInCreateShiftWindow(String location) throws Exception;
//	public void selectChildLocationFilterByText(String location) throws Exception;
//	public void verifyEditMealBreakTimeFunctionalityForAShift(boolean isSavedChange, WebElement shift) throws Exception;
//	public WebElement clickOnProfileIconOfOpenShift() throws Exception;
//	public WebElement clickOnProfileIconOfShiftInDayView(String openOrNot) throws Exception;
//	public void verifyEditMealBreakTimeFunctionalityForAShiftInDayView(boolean isSavedChange, String shiftid) throws Exception;
//	public void changeWorkRoleInPromptOfAShiftInDayView(boolean isApplyChange, String shiftid) throws Exception;
//	public void chooseLocationInCreateSchedulePopupWindow(String location) throws Exception;
//	public void selectRandomLocationOnCreateScheduleEditOperatingHoursPage() throws Exception;
//	public void closeViewStatusContainer() throws Exception;
//	public void editTheOperatingHoursForLGInPopupWinodw(List<String> weekDaysToClose) throws Exception;
//	public void changeWorkRoleInPromptOfAShift(boolean isApplyChange, WebElement shift) throws Exception;
//	public int getShiftIndexById(String id) throws Exception;
//	public String getTheShiftInfoByIndexInDayview(int index) throws Exception;
//	public void verifyConfirmStoreOpenCloseHours() throws Exception;
//	public List<String> getAllLocationGroupLocationsFromCreateShiftWindow() throws Exception;
//	public void verifyFilterDropdownList(boolean isLG) throws Exception;
//	public void clickOnViewShiftsBtnOnRequiredActionSmartCard() throws Exception;
//	public void clickOnClearFilterOnFilterDropdownPopup() throws Exception;
//	public boolean isRequiredActionSmartCardLoaded() throws Exception;
//	public List<WebElement> getAllShiftsOfOneTM(String name) throws Exception;
//	public String getWholeMessageFromActionRequiredSmartCard() throws Exception;
//	public String getTooltipOfPublishButton() throws Exception;
//	public HashMap<String, String> getMessageFromActionRequiredSmartCard() throws Exception;
//	public boolean isDragAndDropConfirmPageLoaded() throws Exception;
//	public void verifyUngenerateButtonIsRemoved() throws Exception;
//	public List<String> getYearsFromCalendarMonthYearText() throws Exception;
//	public boolean isDeleteScheduleButtonLoaded() throws Exception;
//	public void verifyClickOnDeleteScheduleButton() throws Exception;
//	public void verifyTheContentOnDeleteScheduleDialog(String confirmMessage, String week) throws Exception;
//	public String getDeleteScheduleForWhichWeekText() throws Exception;
//	public void verifyDeleteBtnDisabledOnDeleteScheduleDialog() throws Exception;
//	public void verifyDeleteButtonEnabledWhenClickingCheckbox() throws Exception;
//	public void verifyClickOnCancelBtnOnDeleteScheduleDialog() throws Exception;
//	public void deleteAllOOOHShiftInWeekView() throws Exception;
//	public void deleteAllShiftsInWeekView() throws Exception;
//	public List<WebElement> getAllOOOHShifts() throws Exception;
//	public void clickOnClearShiftsBtnOnRequiredActionSmartCard() throws Exception;
//	public void convertUnAssignedShiftToOpenShift(WebElement unAssignedShift) throws Exception;
//	public void editTheShiftTimeForSpecificShift(WebElement shift, String startTime, String endTime) throws Exception;
//	public boolean isOfferTMOptionVisible() throws Exception;
//	public boolean isOfferTMOptionEnabled() throws Exception;
//	public void verifyOfferTMOptionIsAvailable() throws Exception;
//	public void clickOnOfferTMOption() throws Exception;
//	public void verifyRecommendedTableHasTM() throws Exception;
//	public void verifyTMInTheOfferList(String firstName, String expectedStatus) throws Exception;
//	public String getViewStatusShiftsInfo() throws Exception;
//	public ArrayList<String> getScheduleDayViewGridTimeDuration();
//	public List<String> getAllOperatingHrsOnCreateShiftPage() throws Exception;
//	public List<String> getStartAndEndOperatingHrsOnEditShiftPage() throws Exception;
//	public void clickOnCloseButtonOnCustomizeShiftPage() throws Exception;
//	public void navigateDayViewWithDayName(String dayName) throws Exception;
//	public Map<String, String> getActiveDayInfo() throws Exception;
//	public void verifyLocationFilterInLeft(boolean isLG) throws Exception;
//	public void verifyChildLocationShiftsLoadPerformance(String childLocation) throws Exception;
//	public String selectRandomChildLocationToFilter() throws Exception;
//	public void verifyShiftsDisplayThroughLocationFilter(String childLocation) throws Exception;
//	public void verifyAllChildLocationsShiftsLoadPerformance() throws Exception;
//	public void selectAllChildLocationsToFilter() throws Exception;
//	public int getDaysBetweenFinalizeDateAndScheduleStartDate(String finalizeByDate, String scheduleStartDate) throws Exception;
//	public void verifyShiftTypeInLeft(boolean isLG) throws Exception;
//	public void verifyShiftTypeFilters() throws Exception;
//	public float checkEnterBudgetWindowLoadedForNonDG() throws Exception;
//	public boolean isCopyScheduleWindow() throws Exception;
//	public int getSpecificFiltersCount (String filterText) throws Exception;
//	public void deleteMealBreakForOneShift(WebElement shift) throws Exception;
//	public List<WebElement> getShiftsByNameOnDayView(String name) throws Exception;
//	public boolean isGroupByDayPartsLoaded() throws Exception;
//	public List<String> getWeekScheduleShiftTitles() throws Exception;
//	public List<String> getDayScheduleGroupLabels() throws Exception;
//	public boolean isShiftInDayPartOrNotInWeekView(int shiftIndex, String dayPart) throws Exception;
//	public int getTheIndexOfShift(WebElement shift) throws Exception;
//	public boolean isShiftInDayPartOrNotInDayView(int shiftIndex, String dayPart) throws Exception;
//	public void deleteAllShiftsOfGivenDayPartInWeekView(String dayPart) throws Exception;
//	public void deleteAllShiftsOfGivenDayPartInDayView(String dayPart) throws Exception;
//	public List<String> verifyDaysHasShifts() throws Exception;
//	public void verifyShiftTimeInReadMode(String index,String shiftTime) throws Exception;
//	public List<String> getIndexOfDaysHaveShifts() throws Exception;
//	public HashMap<String, String> getMealAndRestBreaksTime() throws Exception;
//	public void verifyGroupCanbeCollapsedNExpanded() throws Exception;
//	public void verifySpecificOptionEnabledOnShiftMenu(String optionName) throws Exception;
//	public boolean isMealBreakTimeWindowDisplayWell(boolean isEditMealBreakEnabled) throws Exception;
//	public void verifyShiftsHasMinorsColorRing(String minorsType) throws Exception;
//	public String getIIconTextInfo(WebElement shift) throws Exception;
//	public String getHeaderOnSchedule() throws Exception;
//	public void verifyHeaderOnSchedule() throws Exception;
//	public void verifyShiftInfoIsCorrectOnMealBreakPopUp(List<String> expectedShiftInfo) throws Exception;
//	public void verifyMealBreakAndRestBreakArePlacedCorrectly() throws Exception;
//	public List<String> verifyEditBreaks() throws Exception;
//	public void verifySpecificShiftHaveEditIcon(int index) throws Exception;
//	public void verifyBreakTimesAreUpdated(List<String> expectedBreakTimes) throws Exception;
//	public void verifyShiftNotesContent(String shiftNotes) throws Exception;
//	public void addShiftNotesToTextarea(String notes) throws Exception;
//	public String getShiftInfoInEditShiftDialog() throws Exception;
//	public void clickOnEditShiftNotesOption() throws Exception;
}