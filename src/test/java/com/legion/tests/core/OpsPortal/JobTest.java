package com.legion.tests.core.OpsPortal;

import com.legion.pages.LoginPage;
import com.legion.pages.OpsPortaPageFactories.JobsPage;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.core.OpsPortal.OpsPortalLocationsPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.legion.utils.MyThreadLocal.*;


public class JobTest extends TestBase {

    public enum modelSwitchOperation{

        Console("Console"),
        OperationPortal("Control Cent");

        private final String value;
        modelSwitchOperation(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
    }

    @Override
    @BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{


        this.createDriver((String)params[0],"83","Window");
        visitPage(testMethod);
        loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
        LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
        locationsPage.clickModelSwitchIconInDashboardPage(modelSwitchOperation.OperationPortal.getValue());
        LoginPage loginPage = pageFactory.createConsoleLoginPage();
        loginPage.verifyNewTermsOfServicePopUp();
        SimpleUtils.assertOnFail("Control Center not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);

    }

    //commented
//    @Automated(automated = "Automated")
//    @Owner(owner = "Estelle")
//    @Enterprise(name = "Op_Enterprise")
//    @TestName(description = "Validate to enable centralized schedule release function")
//    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//    public void verifyOpenCentralizedScheduleReleaseToYesAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
//
//        try{
//            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
//            ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//
//            // Validate Controls Scheduling Policies Section
//            controlsNewUIPage.clickOnControlsConsoleMenu();
//            controlsNewUIPage.clickOnGlobalLocationButton();
//            controlsNewUIPage.clickOnControlsSchedulingPolicies();
//            boolean isSchedulingPolicies = controlsNewUIPage.isControlsSchedulingPoliciesLoaded();
//            SimpleUtils.assertOnFail("Controls Page: Scheduling Policies Section not Loaded.", isSchedulingPolicies, true);
//            controlsNewUIPage.clickOnSchedulingPoliciesSchedulesAdvanceBtn();
//            //check the centralized schedule release button is yes or no
//            List<WebElement> CentralizedScheduleReleaseSelector = controlsNewUIPage.getAvailableSelector();
//            WebElement yesItem = CentralizedScheduleReleaseSelector.get(0);
//            WebElement noItem = CentralizedScheduleReleaseSelector.get(1);
//
//            if (controlsNewUIPage.isCentralizedScheduleReleaseValueYes()) {
//                SimpleUtils.pass("Scheduling Policies: Centralized Schedule Release button is Yes");
//            }else
//                controlsNewUIPage.updateCentralizedScheduleRelease(yesItem);
//
//        } catch (Exception e){
//            SimpleUtils.fail(e.getMessage(), false);
//        }
//    }



    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate job landing page show")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyJobLandingPageAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try{
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.verifyJobLandingPageShowWell();
            jobsPage.verifyPaginationFunctionInJob();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate job search function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyJobSearchFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try{
            String searchText = "*,Adjust Forecast,Create Schedule,Adjust Budget,Release Schedule";
            String[] searchJobCha = searchText.split(",");
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            for (String search: searchJobCha
            ) {
                jobsPage.iCanSearchTheJobWhichICreated(search);
            }

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate check each type of job details page")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCheckJobDetailsFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try{

            try{
                String searchCreateSchedule = "Create Schedule";
                String searchReleaseSchedule = "Release Schedule";
                String searchAdjustBudget = "Adjust Budget";
                String searchAdjustForecast = "Adjust Forecast";
                int index = 0;

                JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
                jobsPage.iCanEnterJobsTab();
                jobsPage.iCanSearchTheJobWhichICreated(searchCreateSchedule);
                jobsPage.iCanGoToCreateScheduleJobDetailsPage(index);
                jobsPage.iCanBackToJobListPage();
                jobsPage.iCanSearchTheJobWhichICreated(searchReleaseSchedule);
                jobsPage.iCanGoToReleaseScheduleJobDetailsPage(index);
                jobsPage.iCanClickCloseBtnInJobDetailsPage();
                jobsPage.iCanSearchTheJobWhichICreated(searchAdjustBudget);
                jobsPage.iCanGoToAdjustBudgetJobDetailsPage(index);
                jobsPage.iCanClickCloseBtnInJobDetailsPage();
                jobsPage.iCanSearchTheJobWhichICreated(searchAdjustForecast);
                jobsPage.iCanGoToAdjustForecastJobDetailsPage(index);
                jobsPage.iCanClickCloseBtnInJobDetailsPage();
            } catch (Exception e){
                SimpleUtils.fail(e.getMessage(), false);
            }

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify copy stop resume and archive job function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCopyStopResumeAndArchiveJobFunctionFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try{
            int index = 0;
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime =  dfs.format(new Date()).trim();
            String jobType = "Create Schedule";
            String jobTitle = "AutoCreateJob"+currentTime;
            setJobName(jobTitle);
            String commentText = "created by automation scripts";
            String searchText = "QA";

            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            //create new job firstly
            jobsPage.iCanEnterCreateNewJobPage();
            jobsPage.selectJobType(jobType);
            jobsPage.selectWeekForJobToTakePlace();
            jobsPage.clickOkBtnInCreateNewJobPage();
            jobsPage.inputJobTitle(jobTitle);
            jobsPage.inputJobComments(commentText);
            jobsPage.addLocationBtnIsClickable();
//            jobsPage.iCanSelectLocationsByAddLocation(searchText,index);
            jobsPage.iCanSelectUpperFieldByAddLocation(searchText,index);
            jobsPage.createBtnIsClickable();
            jobsPage.iCanSearchTheJobWhichICreated(jobTitle);
            jobsPage.iCanStopJob(jobTitle);

            jobsPage.iCanSearchTheJobWhichICreated(jobTitle);
            jobsPage.iCanResumeJob(jobTitle);
            jobsPage.iCanSearchTheJobWhichICreated(jobTitle);
            jobsPage.iCanCopyJob(jobTitle);
            jobsPage.iCanSearchTheJobWhichICreated("Copy Of "+jobTitle);
            jobsPage.iCanStopJob("Copy Of "+jobTitle);
            jobsPage.iCanSearchTheJobWhichICreated("Copy Of "+jobTitle);
            jobsPage.iCanArchiveJob("Copy Of "+jobTitle);



        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate filter function by job type and job status")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFilterFunctionByJobTypeAndJobStatusAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try{
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.filterJobsByJobTypeAndStatus();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate filter function by job type")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFilterFunctionByJobType(String browser, String username, String password, String location) throws Exception {

        try{
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.filterJobsByJobType();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate filter function by job status")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyFilterFunctionByJobStatusAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try{
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.filterJobsByJobStatus();
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate create create schedule job")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateScheduleJobFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {


        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            String currentTime =  dfs.format(new Date());
            String jobType = "Create Schedule";
            String jobTitle = "AutoCreateJob"+currentTime;
            setJobName(jobTitle);
            String commentText = "created by automation scripts";
            String searchText = "OMLocation16";
            int index = 0;

//            ArrayList<HashMap<String, String>> jobInfoDetails =jobsPage.iCanGetJobInfo(jobTitle);

//            //go to schedule page to see current week schedule generated or not
//            LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
//            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
//
//            DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//            LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//            locationSelectorPage.changeDistrict("OMDistrict1");
//            locationSelectorPage.changeLocation(searchText);
//
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//
//            if (createSchedulePage.isWeekGenerated()){
//                createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//            }else {
//                SimpleUtils.pass("Current week schedule is not  Generated!");
//                locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanEnterCreateNewJobPage();
            if (jobsPage.verifyCreatNewJobPopUpWin()) {
                jobsPage.selectJobType(jobType);
                jobsPage.selectWeekForJobToTakePlace();
                jobsPage.clickOkBtnInCreateNewJobPage();
                jobsPage.inputJobTitle(jobTitle);
                jobsPage.inputJobComments(commentText);
                jobsPage.addLocationBtnIsClickable();
                jobsPage.iCanSelectLocationsByAddLocation(searchText,index);
                jobsPage.createBtnIsClickable();
                jobsPage.iCanSearchTheJobWhichICreated(jobTitle);
            }else
                SimpleUtils.fail("Create job pop up page load failed",false);
//            }

//            Thread.sleep(60000);//to wait for job completed
//            locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
//            SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//
//            locationSelectorPage.changeDistrict("OMDistrict1");
//            locationSelectorPage.changeLocation(searchText);
//
//
//            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//            SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//            scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//
//            if(!createSchedulePage.isWeekGenerated()&& schedulePage.suggestedButtonIsHighlighted()){
//                SimpleUtils.pass("Created schedule job doesn't generated the manager schedule");
//
//            }else
//                SimpleUtils.fail("It should not generated schedule in manager tab",false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate abnormal create job flow")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAbnormalCheatJobFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {

        try{
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            String currentTime =  dfs.format(new Date());
            String jobType = "Create Schedule";
            String jobTitle = "AutoCreateJob"+currentTime;
            setJobName(jobTitle);
            String commentText = "created by automation scripts";
            String searchText = "OMLocation16";
            int index = 0;

            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanEnterCreateNewJobPage();
            jobsPage.iCanCloseJobCreatePopUpWindowByCloseBtn();
            jobsPage.iCanEnterCreateNewJobPage();
            jobsPage.iCanCancelJobCreatePopUpWindowByCancelBtn();
            jobsPage.iCanEnterCreateNewJobPage();

            if (jobsPage.verifyCreatNewJobPopUpWin()) {
                jobsPage.selectJobType(jobType);
                jobsPage.selectWeekForJobToTakePlace();
                jobsPage.clickOkBtnInCreateNewJobPage();
                jobsPage.inputJobTitle(jobTitle);
                jobsPage.inputJobComments(commentText);
                jobsPage.addLocationBtnIsClickable();
                jobsPage.iCanSelectLocationsByAddLocation(searchText, index);
                jobsPage.iCanCancelJobInJobCreatPageByCancelBtn();
                ArrayList<HashMap<String, String>> jobInfoDetails =jobsPage.iCanGetJobInfo(jobTitle);
                if (jobInfoDetails.size()==0) {
                    SimpleUtils.pass("The creating job was canceled successfully after clicking cancel button");
                }else
                    SimpleUtils.fail("",false);
            }else
                SimpleUtils.fail("Create job pop up page load failed",false);
        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate release schedule job function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyCreateReleaseScheduleJobFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {


        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss ");
            String currentTime =  dfs.format(new Date());
            String jobType = "Release Schedule";
            String jobTitle = "AutoReleaseJob"+currentTime;
            setJobName(jobTitle);
            String commentText = "created by automation scripts";
            String searchText = "UpperField";
            int index = 0;
            String releaseDay = "10";
            String timeForRelease = "0";

            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanEnterCreateNewJobPage();
            if (jobsPage.verifyCreatNewJobPopUpWin()) {
                jobsPage.selectJobType(jobType);
                jobsPage.selectWeekForJobToTakePlace();
                jobsPage.clickOkBtnInCreateNewJobPage();
                jobsPage.inputJobTitle(jobTitle);
                jobsPage.inputJobComments(commentText);
                jobsPage.addLocationBtnIsClickable();
                jobsPage.iCanSelectLocationsViaDynamicGroupInAddLocation(searchText);
                jobsPage.iCanClickOnCreatAndReleaseCheckBox();
                jobsPage.iCanSetUpDaysBeforeRelease(releaseDay);
                jobsPage.iCanSetUpTimeOfRelease(timeForRelease);
                jobsPage.createBtnIsClickable();
                jobsPage.iCanSearchTheJobWhichICreated(jobTitle);
            }else
                SimpleUtils.fail("Create job pop up page load failed",false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate adjust budget job function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAdjustBudgetJobFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime =  dfs.format(new Date());
            String jobType = "Adjust Budget";
            String jobTitle = "AutoAdjustBudgetJob"+currentTime;
            setJobName(jobTitle);
            String commentText = "created by automation scripts";
            String searchText = "OMLocation16";
            String searchTaskText = "OMLocation16";
            int index = 0;
            String budgetAssignmentNum = "10";
            String workRole = "Automation Work Role";
            String taskName = "Line Verification";

            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanEnterCreateNewJobPage();
            if (jobsPage.verifyCreatNewJobPopUpWin()) {
                jobsPage.selectJobType(jobType);
                jobsPage.selectWeekForJobToTakePlace();
                jobsPage.clickOkBtnInCreateNewJobPage();
                if (jobsPage.verifyLayoutOfAdjustBudget()) {
                    jobsPage.inputJobTitle(jobTitle);
                    jobsPage.inputJobComments(commentText);
                    jobsPage.addLocationBtnIsClickable();
                    jobsPage.iCanSelectLocationsByAddLocation(searchText,index);
                    jobsPage.iCanSetUpBudgetAssignmentNum(budgetAssignmentNum);
                    //add tasks
                    jobsPage.addTaskButtonIsClickable();
                    jobsPage.iCanAddTasks(searchText,index,taskName);
                    //add work roles
                    jobsPage.addWorkRoleButtonIsClickable();
                    jobsPage.iCanAddWorkRoles(searchText,index,workRole);
                    jobsPage.createBtnIsClickableInAdjustBudgetJob();
                    jobsPage.verifyAdjustBudgetConfirmationPage(jobTitle,budgetAssignmentNum,taskName,workRole);
                    jobsPage.cancelBthInAdjustBudgetConfirmationPageIsClickable();
                    jobsPage.executeBtnIsClickable();
                    jobsPage.iCanSearchTheJobWhichICreated(jobTitle);
                }

            }else
                SimpleUtils.fail("Create job pop up page load failed",false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Validate adjust forecast job function")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyAdjustForecastJobFunctionAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime =  dfs.format(new Date());
            String jobType = "Adjust Forecast";
            String jobTitle = "AutoAdjustForecastJob"+currentTime;
            setJobName(jobTitle);
            String commentText = "created by automation scripts";
            String searchText = "OMLocation16";
            String searchTaskText = "OMLocation16";
            int index = 0;
            String adjustmentValue = "10";
            String directionChoices = "Decrease";
            String categoryType = "Enrollments";
            String adjustmentType ="Percent";

            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanEnterCreateNewJobPage();
            if (jobsPage.verifyCreatNewJobPopUpWin()) {
                jobsPage.selectJobType(jobType);
                jobsPage.selectWeekForJobToTakePlace();
                jobsPage.clickOkBtnInCreateNewJobPage();
                if (jobsPage.verifyLayoutOfAdjustForecast()) {
                    jobsPage.inputJobTitle(jobTitle);
                    jobsPage.inputJobComments(commentText);
                    jobsPage.addLocationBtnIsClickable();
                    jobsPage.iCanSelectLocationsByAddLocation(searchText,index);
                    jobsPage.selectDirectionChoices(directionChoices);
                    jobsPage.selectCategoryTypes(categoryType);
                    jobsPage.inputAdjustmentValue(adjustmentValue);
                    jobsPage.selectAdjustmentType(adjustmentType);
                    jobsPage.createBtnIsClickableInAdjustBudgetJob();
                    jobsPage.verifyAdjustForecastConfirmationPage(jobTitle,adjustmentValue,directionChoices,categoryType,searchTaskText);
                    jobsPage.cancelBthInAdjustBudgetConfirmationPageIsClickable();
                    jobsPage.executeBtnIsClickable();
                    jobsPage.iCanSearchTheJobWhichICreated(jobTitle);
                }

            }else
                SimpleUtils.fail("Create job pop up page load failed",false);

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }


    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Export Result File and Export Task Summary function In Create Schedule Job")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyExportResultFileAndExportTaskSummaryFunctionInCreateScheduleJobAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            int index =0;
            String searchCharactor = "Create Schedule";
            //go to job tab
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanSearchTheJobWhichICreated(searchCharactor);
            jobsPage.iCanGoToCreateScheduleJobDetailsPage(index);
            jobsPage.verifyExportResultFunction();
            jobsPage.verifyExportTaskSummaryFunction();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Export Result File and Export Task Summary function In Adjust Budget Job")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyExportResultFileAndExportTaskSummaryFunctionInAdjustBudgetJobAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            int index =0;
            String searchCharactor = "Adjust Budget";
            //go to job tab
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanSearchTheJobWhichICreated(searchCharactor);
            jobsPage.iCanGoToAdjustBudgetJobDetailsPage(index);
            jobsPage.verifyExportResultFunction();
            jobsPage.verifyExportTaskSummaryFunction();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Export Result File In Release Schedule Job")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyExportResultFileFunctionInReleaseScheduleJobAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            int index =0;
            String searchCharactor = "Release Schedule";
            //go to job tab
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanSearchTheJobWhichICreated(searchCharactor);
            jobsPage.iCanGoToReleaseScheduleJobDetailsPage(index);
            jobsPage.verifyExportResultFunction();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Estelle")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Export Result File In Adjust Forecast Job")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyExportResultFileFunctionInAdjustForecastJobAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            int index =0;
            String searchCharactor = "Adjust Forecast";
            //go to job tab
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();
            jobsPage.iCanSearchTheJobWhichICreated(searchCharactor);
            jobsPage.iCanGoToAdjustForecastJobDetailsPage(index);
            jobsPage.verifyExportResultFunction();

        } catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Automated(automated = "Automated")
    @Owner(owner = "Nancy")
    @Enterprise(name = "Op_Enterprise")
    @TestName(description = "Verify Dynamic Group Function>In OM Job Automated")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
    public void verifyDynamicGroupFunctionInOMJobAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
        try{
            //go to job tab
            JobsPage jobsPage = pageFactory.createOpsPortalJobsPage();
            jobsPage.iCanEnterJobsTab();

            jobsPage.addWorkforceSharingDGWithMutiplyCriteria();
            jobsPage.verifyDuplicatedDGErrorMessage();
            jobsPage.editFirstDynamicGroup();
            jobsPage.removeFirstDynamicGroup();

            jobsPage.createDynamicGroup("Create Schedule");
            
            jobsPage.verifyDynamicGroupDisplayInSpecifyJobType("Adjust Forecast");
            jobsPage.verifyDynamicGroupDisplayInSpecifyJobType("Adjust Budget");
            jobsPage.verifyDynamicGroupDisplayInSpecifyJobType("Release Schedule");
            jobsPage.verifyDynamicGroupDisplayInSpecifyJobType("Create Schedule");
        }catch (Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }
}
