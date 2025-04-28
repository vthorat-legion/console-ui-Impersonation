package com.legion.pages.OpsPortaPageFactories;

import java.util.ArrayList;
import java.util.HashMap;

public interface JobsPage {

    public void iCanEnterJobsTab();

    public void verifyJobLandingPageShowWell();

    public void iCanEnterCreateNewJobPage();

    public void selectJobType(String jobType) throws Exception;

    public void selectWeekForJobToTakePlace();

    public void inputJobTitle(String jobTitle);

    public void inputJobComments(String commentText);

    public void addLocationBtnIsClickable();

    public void iCanSelectLocationsByAddLocation(String searchText, int index);

    public void createBtnIsClickable();

    public void clickOkBtnInCreateNewJobPage();

    public void iCanSearchTheJobWhichICreated(String jobTitle) throws Exception;

    public void iCanBackToJobListPage();

    public void iCanClickCloseBtnInJobDetailsPage();

    public void iCanDownloadExportResultFile();

    public void iCanDownloadExportTaskSummary();

    public ArrayList<HashMap<String, String>> iCanGetJobInfo(String jobTitle);

    public void iCanGoToCreateScheduleJobDetailsPage(int index);

    public void iCanGoToReleaseScheduleJobDetailsPage(int index);

    public void iCanGoToAdjustBudgetJobDetailsPage(int index);

    public void iCanGoToAdjustForecastJobDetailsPage(int index);

    public void iCanCopyJob(String jobTitle) throws Exception;

    public void iCanStopJob(String jobTitle) throws Exception;

    public void iCanResumeJob(String jobTitle) throws Exception;

    public void iCanArchiveJob(String jobTitle) throws Exception;

    public void iCanSelectUpperFieldByAddLocation(String searchText, int index);

    public void filterJobsByJobTypeAndStatus() throws Exception;

    public void filterJobsByJobType() throws Exception;

    public void filterJobsByJobStatus() throws Exception;

    public void filterClearFilterFunction();

    public boolean verifyCreatNewJobPopUpWin();

    public void iCanCloseJobCreatePopUpWindowByCloseBtn();

    public void iCanCancelJobCreatePopUpWindowByCancelBtn();

    public void iCanCancelJobInJobCreatPageByCancelBtn();

    public void iCanSetUpDaysBeforeRelease(String releaseDay);

    public void iCanSetUpTimeOfRelease(String timeForRelease) throws Exception;

    public void iCanClickOnCreatAndReleaseCheckBox();

    public void verifyPaginationFunctionInJob() throws Exception;

    public boolean verifyLayoutOfAdjustBudget();

    public void iCanSetUpBudgetAssignmentNum(String budgetAssignmentNum);

    public void addTaskButtonIsClickable();

    public void iCanAddTasks(String searchText, int index, String taskName);

    public void addWorkRoleButtonIsClickable();

    public void iCanAddWorkRoles(String searchText, int index, String workRole);

    public void executeBtnIsClickable();

    public void createBtnIsClickableInAdjustBudgetJob() throws Exception;

    public void verifyAdjustBudgetConfirmationPage(String jobTitle, String budgetAssignmentNum, String taskName, String workRole);

    public void cancelBthInAdjustBudgetConfirmationPageIsClickable();

    public boolean verifyLayoutOfAdjustForecast();

    public void selectDirectionChoices(String directionChoices) throws Exception;

    public void selectCategoryTypes(String categoryType) throws Exception;

    public void inputAdjustmentValue(String adjustmentValue);

    public void selectAdjustmentType(String adjustmentType) throws Exception;

    public void verifyAdjustForecastConfirmationPage(String jobTitle, String adjustmentValue, String directionChoices, String categoryType, String searchTaskText);

    public void verifyExportResultFunction();

    public void verifyExportTaskSummaryFunction();

    public void iCanSelectLocationsViaDynamicGroupInAddLocation(String searchText) throws Exception;

    public void verifyDynamicGroupName() throws Exception;

    public void verifyDynamicGroupDisplayInSpecifyJobType(String type) throws Exception;

    public void createDynamicGroup(String type) throws Exception;

    public void addWorkforceSharingDGWithMutiplyCriteria() throws Exception;

    public void verifyDuplicatedDGErrorMessage() throws Exception;

    public void editFirstDynamicGroup() throws Exception;

    public void removeFirstDynamicGroup() throws Exception;

    public void archiveOrStopSpecificJob(String jobTitle) throws Exception;

    public void selectWeeksForJobToTakePlaceByIndex(int index);

    public String createNewJob(String type);

}
