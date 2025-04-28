package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.PlanPage;
import com.legion.utils.SimpleUtils;
import cucumber.api.Scenario;
import cucumber.api.java.an.E;
import cucumber.api.java.ro.Si;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.DeleteSession;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.*;

public class ConsolePlanPage extends BasePage implements PlanPage {

    public ConsolePlanPage() {
        PageFactory.initElements(getDriver(), this);
    }

    //Added by Lizzy
    final static String consolePlanItemText = "Plan";


    @FindBy(css = ".console-navigation-item-label.Plan")
    private WebElement goToPlanButton;
    @FindBy(css = "lg-button[label='Create Plan']")
    private WebElement createPlanBtn;
    @FindBy(css = ".modal-dialog.modal-lgn-lg")
    private WebElement createPlanModalDialog;
    @FindBy(css = "input-field[label='Budget plan name']>ng-form>input")
    private WebElement createPlanName;
    @FindBy(css = "input-field[label=\"Budget plan name\"] lg-input-error span")
    private WebElement createPlanNameError;
    @FindBy(css = "input-field[label='Description']>ng-form>input")
    private WebElement createPlanDescription;
    @FindBy(css = "ranged-calendar__day ng-binding ng-scope real-day")
    private List<WebElement> calendarDates;
    @FindBy(css = "ranged-calendar__day ng-binding ng-scope real-day in-range is-today")
    private WebElement currentDay;
    @FindBy(css = "calendar-nav-right")
    private WebElement endDateNextMonthArrow;
    @FindBy(css = "day-selected ng-binding ng-scope")
    private List<WebElement> daysSelected;
    @FindBy(css = "lg-button[label='OK'] button")
    private WebElement planCreateOKBTN;
    @FindBy(css = "lg-button[label='Cancel'] button")
    private WebElement PlanCreateCancelBTN;
    @FindBy(css = "div.scenario-details h1.title")
    private WebElement scenarioPlanNameInDeatil;
    @FindBy(xpath = "//input[contains(@placeholder,\"Search by\")]")
    private WebElement planSearchInputField;
    @FindBy(css = "div[ng-repeat-start=\"plan in filteredPlanLists\"]")
    private List<WebElement> planSearchedResults;
    @FindBy(css = "div[ng-repeat=\"s in plan.scenaries\"]")
    private List<WebElement> scenarioPlans;
    @FindBy(css = "lg-button[label=\"Create\"]")
    private List<WebElement> createScPlanButton;
    @FindBy(css = "lg-button[label=\"Save\"]>button")
    private WebElement planSaveButton;
    @FindBy(css = "span[ng-class=\"getPlanStatusClass(scenario)\"]")
    private WebElement planStatusInDetail;
    @FindBy(css = "lg-button[label=\"Generate demand forecast\"] button")
    private WebElement generateForecastButton;
    @FindBy(css = "lg-button[label=\"Run Budget\"] button")
    private WebElement runBudgetButton;
    @FindBy(css = "modal[modal-title=\"Generate demand forecast\"]>div.lg-modal")
    private WebElement forecastRunDialog;
    @FindBy(css = "modal[modal-title=\"Re-generate demand forecast\"]>div")
    private WebElement forecastReRunDialog;
    @FindBy(css = "lg-button[label=\"Generate\"]")
    private WebElement forecastGenerateBTNOnDialog;
    @FindBy(css = "lg-button[label=\"Download CSV\"] button")
    private WebElement forecastDownloadCSVLink;
    @FindBy(css = "a[href*='BudgetSample']")
    private WebElement budgetDownloadCSVLink;
    @FindBy(css = "modal[modal-title=\"Run budget\"]>div.lg-modal")
    private WebElement budgetRunDialog;
    @FindBy(css = "lg-button[label=\"Run\"] button")
    private WebElement budgetRunBTNOnDialog;
    @FindBy(css = "p[ng-if=\"showJobResult(downloadStates,'BudgetPlan')\"] span:nth-child(4)")
    private WebElement budgetValue;
    @FindBy(css = "lg-button[label=\"Send For Review\"] button")
    private WebElement sendForReviewButton;
    @FindBy(css = "modal[modal-title='Send For Review']>div")
    private WebElement sendForReviewDialog;
    @FindBy(css = "lg-button[label=\"Send\"] button")
    private WebElement sendButtonOnDialog;
    @FindBy(css = "input-field[value=\"scenario.name\"] input")
    private WebElement scenarioPlanNameInput;
    @FindBy(css = "input-field[value=\"scenario.name\"] .input-faked")
    private WebElement scenarioPlanDefaultPlanName;
    @FindBy(css = "input-field[label=\"Description\"] textarea")
    private WebElement scenarioPlanDescriptionInput;
    @FindBy(css = "lg-button[ng-if=\"$ctrl.secondLabel\"] button")
    private WebElement scenarioPlanSave;
    @FindBy(css = "input-field[label=\"Recipients\"] input")
    private WebElement scenarioEmailInput;
    @FindBy(css = "div.lg-toast")
    private WebElement errorToast;
    @FindBy(css = ".lg-pagination__pages.ng-binding")
    private WebElement pageNumberText;
    @FindBy(css = "modal[modal-title=\"Edit Plan\"]")
    private WebElement editParentPlanDialog;
    @FindBy(css = "input-field[value=\"plan.name\"] input")
    private WebElement editParentPlanName;
    @FindBy(css = "input-field[value=\"scenario.name\"] input")
    private WebElement editScenarioPlanName;
    @FindBy(css = "p.location-display span")
    private WebElement locationInCreatePlanDialog;
    @FindBy(css = "div.ranged-calendar__day.real-day.can-not-select")
    private List<WebElement> planDurationDisabledDays;
    @FindBy(css = "div.ranged-calendar__day.real-day.is-today")
    private WebElement planDurationToday;
    @FindBy(css = "div[ng-if=\"invalidRangeMessage\"]")
    private WebElement planDurationErrorMsg;
    @FindBy(css = "b[ng-if=\"daySelected\"]")
    private List<WebElement> planDurationStartAndEnd;
    @FindBy(css = "lg-close.dismiss")
    private WebElement planDialogCloseIcon;
    @FindBy(css = "lg-button[label=\"Archive\"]")
    private WebElement planDialogArchiveBTN;
    @FindBy(css = "lg-button[label=\"Copy\"]")
    private WebElement planDialogCopyBTN;
    @FindBy(css = "modal[modal-title=\"Archive labor budget scenario\"]")
    private WebElement scenarioPlanArchivedialog;
    @FindBy(xpath = "//modal[contains(@modal-title,\"Budget Scenario\")]")
    private WebElement scenarioPlanCopydialog;
    @FindBy(css = "sub-content-box[box-title=\"Email notification on status update\"]")
    private WebElement scenarioPlanDetailEmail;
    @FindBy(css = "a[ng-click=\"$ctrl.back()\"]")
    private WebElement scenarioPlanBackLink;
    @FindBy(css = "span.status-highlight")
    private WebElement scenarioPlanStatusInDetail;
    @FindBy(css = "lg-button[label=\"Re-run\"] button")
    private List<WebElement> scenarioPlanReRunBTNs;
    @FindBy(css = "div.banner-content p.mr-30")
    private List<WebElement> scenarioPlanContents;
    @FindBy(css = "div.om-job-details div.row div.plr-0-0.job-title")
    private List<WebElement> scenarioPlanDetails;
    @FindBy(css = "lg-button[ng-click=\"downloadScenario('OperatingHours',true)\"] button")
    private WebElement OHDownloadLink;
    @FindBy(css = "lg-button[ng-click=\"downloadScenario('WageRate',true)\"] button")
    private WebElement wagesDownloadLink;
    @FindBy(css = "lg-button[label=\"Approve\"] button")
    private WebElement scenarioPlanApproveBTN;
    @FindBy(css = "lg-button[label=\"Reject\"] button")
    private WebElement scenarioPlanRejectBTN;
    @FindBy(css = "modal[modal-title=\"Approve Budget\"]>div")
    private WebElement approveBudgetDialog;
    @FindBy(css = "lg-button[label=\"Set in effect\"] button")
    private WebElement scenarioPlanSetInEffectBTN;
    @FindBy(css = "modal[modal-title=\"Reject Budget\"]>div")
    private WebElement scenarioPlanRejectDialog;
    @FindBy(css = "lg-button[label=\"Stop\"] button")
    private WebElement scenarioPlanForecastStopToRunLink;
    @FindBy(css = ".scenario-jobs-budget-details__progress-bar-inner")
    private List<WebElement> scenarioPlanForecastRunProgressBar;
    @FindBy(css = "modal[modal-title=\"Stop demand forecast\"]>div")
    private WebElement scenarioPlanStopForecastDialog;
    @FindBy(css = "div.progress-info .ml-5")
    private List<WebElement> scenarioPlanJobsStatus;
    @FindBy(css = "modal[modal-title=\"Edit Labor Budget Scenario\"]>div")
    private WebElement scenarioPlanEditDialog;

    @Override
    public boolean verifyPlanConsoleTabShowing() throws Exception{
        boolean flag = false;
        if(isElementLoaded(goToPlanButton, 10)){
            SimpleUtils.pass("User can see Plan tab");
            flag = true;
        }else {
            SimpleUtils.report("Plan tab is not shown");
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean verifyCreatePlanButtonShowing() throws Exception {
        waitForSeconds(5);
        boolean flag = false;
        if (isElementLoaded(createPlanBtn, 10)) {
            flag = true;
            SimpleUtils.pass("User can see create plan button");
        }else {
            flag = false;
            SimpleUtils.report("User can't see create plan button");
        }
        return flag;
    }


    @Override
    public void clickOnPlanConsoleMenuItem() throws Exception {
        if (isElementLoaded(goToPlanButton, 10)) {
            clickTheElement(goToPlanButton);
            waitForSeconds(3);
            SimpleUtils.pass("'Plan' Console Menu loaded successfully!");
        } else
            SimpleUtils.fail("'Plan' Console Menu failed to load!", false);
    }

    private int getSpecificDayIndex(WebElement specificDay) {
        int index = 0;
        if (areListElementVisible(calendarDates, 10)) {
            for (int i = 0; i < calendarDates.size(); i++) {
                String day = calendarDates.get(i).getText();
                if (day.equals(specificDay.getText())) {
                    index = i;
                    SimpleUtils.pass("Get current day's index successfully");
                }
            }
        } else {
            SimpleUtils.fail("Days on calendar failed to load!", true);
        }
        return index;
    }


    private void  planDurationSetting(int fromDay, int toDay) throws Exception {
        selectDate(fromDay);
        selectDate(toDay);

    }


    @Override
    public void createANewPlan(String planName) throws Exception {
        if (isElementLoaded(createPlanBtn, 10)) {
            clickTheElement(createPlanBtn);
            if (isElementLoaded(createPlanModalDialog, 10)) {
                SimpleUtils.pass("Create plan dialog loaded successfully!");
                //input plan name
                createPlanName.sendKeys(planName);
                //input plan description
                createPlanDescription.sendKeys("The description was created by automation ");
                //Select duration for the budget to take place
                planDurationSetting(7, 14);
                //click the ok
                clickTheElement(planCreateOKBTN);
                if (isElementLoaded(errorToast, 5)) {
                    if (errorToast.getText().contains("plan name already exists:")) {
                        //click the cancel
                        clickTheElement(PlanCreateCancelBTN);
                        waitForSeconds(3);
                        SimpleUtils.report("Create a plan with a duplicated name is not allowed!");
                    }
                } else if (isElementLoaded(createPlanBtn) && searchAPlan(planName))
                    SimpleUtils.pass("Create a parent plan successfully!");
            } else
                SimpleUtils.fail("Create plan dialog not loaded on page!", false);

        } else
            SimpleUtils.fail("Create Plan button not loaded on page!", false);

    }

    @Override
    public void verifyScenarioPlanAutoCreated(String planName, String scenarioName) throws Exception {
        //search the plan
        if (searchAPlan(planName)) {
            clickTheElement(planSearchedResults.get(0).findElement(By.cssSelector("div:nth-child(1)")));
            waitForSeconds(2);
            if (areListElementVisible(createScPlanButton)) {
                //click the create button
                clickTheElement(createScPlanButton.get(0));
                waitForSeconds(2);
                //input scenario plan name and description
                scenarioPlanNameInput.sendKeys(scenarioName);
                scenarioPlanDescriptionInput.sendKeys("This is the description for " + scenarioName);
                //click save
                clickTheElement(scenarioPlanCopydialog.findElement(By.cssSelector(" lg-button[label=\"Create\"]")));
                if (isElementLoaded(errorToast, 5)) {
                    if (errorToast.getText().contains("Error!")) {
                        SimpleUtils.report("Create a scenario plan failed with errors!");
                    }
                    //click the cancel
                    clickTheElement(PlanCreateCancelBTN);
                    waitForSeconds(3);
                }
                //check if auto navigated to the scenario plan detail page
                else if (isElementLoaded(scenarioPlanNameInDeatil, 5)) {
                    SimpleUtils.pass("Scenario plan detail page loaded successfully!");
                    //assert the scenario plane name
                    if (scenarioPlanNameInDeatil.getText().equals(scenarioName))
                        SimpleUtils.pass("Scenario plan created successfully with right name!");
                    //click back link to list
                    clickTheElement(scenarioPlanBackLink);
                    //check page auto back to plan list
                    if (isElementLoaded(planSearchInputField, 3))
                        SimpleUtils.pass("Page back to plan list after saved the scenario plan!");
                }
            } else
                SimpleUtils.fail("No create button show to allow scenario plan!", false);

        }
    }

    @Override
    public void verifyRunBTNInPlanDetail(String planName, String scenarioName) throws Exception {
        //search the plan
        if (searchAPlan(planName)) {
            //find the scenario plan and enter its detail
            if(goToScenarioPlanDetail(planName,scenarioName)){
                //check the run forecast button
                if(isElementLoaded(generateForecastButton,5)){
                    //click the generate forecast button to begin to run forecast
                    clickTheElement(generateForecastButton);
                    if(isElementLoaded(forecastRunDialog,5))
                        //click generate
                        clickTheElement(forecastGenerateBTNOnDialog);
                    //check the stop link and progress bar displayed.
                    if(isElementLoaded(scenarioPlanForecastStopToRunLink,5)&&areListElementVisible(scenarioPlanForecastRunProgressBar,10)) {
                        SimpleUtils.pass("The forecast begin to run and show the in progress bar and can be stopped");
                        //click the stop link to stop it
                        clickTheElement(scenarioPlanForecastStopToRunLink);
                        if(isElementLoaded(scenarioPlanStopForecastDialog)) {
                            //click stop to stop the job
                            clickTheElement(scenarioPlanStopForecastDialog.findElement(By.cssSelector("lg-button[label=\"Stop\"] button")));
                            //check the stop link changed to re-run
                            if(areListElementVisible(scenarioPlanReRunBTNs,5)){
                                SimpleUtils.pass("The Re-run link is show after stop the forecast job");
                                //click re-run link to re-run
                                clickTheElement(scenarioPlanReRunBTNs.get(0));
                                if(isElementLoaded(forecastReRunDialog,5)){
                                    SimpleUtils.pass("The Re-generate demand forecast dialog pops up successfully!");
                                    clickTheElement(forecastGenerateBTNOnDialog);
                                    //check the stop link and progress bar show again.
                                    if(isElementLoaded(scenarioPlanForecastStopToRunLink)&&areListElementVisible(scenarioPlanForecastRunProgressBar,10))
                                        SimpleUtils.pass("The forecast job is re runing again!");
                                }
                                //click back to navigate back to plan list page
                                clickTheElement(scenarioPlanBackLink);
                                waitForSeconds(3);

                            }
                        }


                    }
                }
            }
        }
    }

    private boolean checkScenarioPlanExist(String planName,String scplanName ) throws Exception{
        boolean scenarioPlanExist=false;
        if(searchAPlan(planName)) {
            if (!areListElementVisible(scenarioPlans, 5))
                //click the plan to expand
                clickTheElement(planSearchedResults.get(0).findElement(By.cssSelector("div:nth-child(1)")));
            waitForSeconds(2);
            if (areListElementVisible(scenarioPlans)) {
                SimpleUtils.pass("Plan with scenario plans loaded successfully after it was expanded!");
                for (WebElement scplan : scenarioPlans) {
                    if (scplan.findElement(By.cssSelector(" div:nth-child(1)")).getText().trim().equals(scplanName)) {
                        scenarioPlanExist = true;
                        SimpleUtils.pass("Find the scenario plan successfully!");
                        break;
                    }
                }
            }
        }
        return scenarioPlanExist;
    }


    @Override
    public void editAScenarioPlan(String planName,String scplanName) throws Exception {
        if (searchAPlan(planName)) {
            if (!areListElementVisible(scenarioPlans, 5))
                //click the plan to expand
                clickTheElement(planSearchedResults.get(0).findElement(By.cssSelector("div:nth-child(1)")));
            waitForSeconds(2);
            if (areListElementVisible(scenarioPlans)) {
                SimpleUtils.pass("Plan with scenario plans loaded successfully after it was expanded!");
                for (WebElement scplan : scenarioPlans) {
                    if (scplan.findElement(By.cssSelector(" div:nth-child(1)")).getText().trim().equals(scplanName)) {
                        SimpleUtils.pass("Find the scenario plan successfully!");
                        //get the scenario plan title in list
                        String scplanNameInList = scplan.findElement(By.cssSelector("div:nth-child(1)")).getText().trim();
                        //click the edit to edit the scenario plan
                        if (isElementLoaded(scplan.findElement(By.cssSelector(" lg-button[label=\"Edit\"]")))) {
                            clickTheElement(scplan.findElement(By.cssSelector(" lg-button[label=\"Edit\"]")));
                            if (isElementLoaded(scenarioPlanEditDialog, 5)) {
                                SimpleUtils.pass("The scenario plan edit dialog pops up successfully");
                                //input the scenario plan name
                                editScenarioPlanName.clear();
                                editScenarioPlanName.sendKeys(scplanName + "-Updated");
                                //click ok
                                planSaveButton.click();
                                waitForSeconds(3);
                                //check the updated successfully
                                boolean planupdated=checkScenarioPlanExist(planName, scplanName + "-Updated");
                                SimpleUtils.assertOnFail("The scenario plan does not updated successfully!",planupdated,false);
                                //do archive the scenario plan
                                archiveAPlan(planName, scplanName + "-Updated");
                            }
                        }
                    }
                    break;
                }

            }
        }
    }

    @Override
    public void checkCompleteForecastPlan(String planName,String scplanName) throws Exception{
        if(goToScenarioPlanDetail(planName,scplanName)){
            //check the status of the plan
            if(getScenarioPlanStatus().equals("Completed")){
                //check the forecast status is complete and progress bar is 100%
                boolean forecastStatus=areListElementVisible(scenarioPlanJobsStatus,10)&&scenarioPlanJobsStatus.get(0).getText().equals("Status:Completed");
                SimpleUtils.assertOnFail("The forecast status of complete scenario plan not loaded or status is incorrect!",forecastStatus,false);
                //continue to check the progress bar
                boolean forecastProgressBar=(areListElementVisible(scenarioPlanForecastRunProgressBar,10)&& scenarioPlanForecastRunProgressBar.get(0).getAttribute("style").contains("width: 250px"));
                SimpleUtils.assertOnFail("The forecast status progress bar for complete scenario plan is incorrect!",forecastProgressBar,false);

                }

            }
        }

    @Override
    public boolean verifyCreatePlanButtonAvail(String upperfieldName) throws Exception {
        boolean createBtn = false;
        if (isElementLoaded(createPlanBtn, 10)) {
            createBtn = true;
        }
        return createBtn;
    }

    @Override
    public boolean searchAPlan(String keywords) throws Exception {
        Boolean isExist = false;
        //click the plan navigation to make sure you are in plan list page
        if (isElementLoaded(planSearchInputField, 3)) {
            SimpleUtils.pass("Plan search input field loaded successfully!");
            waitForSeconds(5);
            planSearchInputField.clear();
            planSearchInputField.sendKeys(keywords);
            planSearchInputField.sendKeys(Keys.ENTER);
            if (areListElementVisible(planSearchedResults)) {
                isExist = true;
                SimpleUtils.pass("Searched plan with result successfully!");

            }
        }
        return isExist;
    }


    @Override
    public void verifyCreatePlanLandingPage(String planName) throws Exception {

        SimpleDateFormat dfs = new SimpleDateFormat("MMddHH");
        String currentDate =  dfs.format(new Date()).trim();
        //check the data in list
        if (areListElementVisible(planSearchedResults)) {
            SimpleUtils.report("There are data show in plan landing page!");
            //get the count
            int dataInAPage = planSearchedResults.size();
            //get the total page
            int maxPageNum = Integer.valueOf(pageNumberText.getText().trim().split("of")[1].trim());
            if (maxPageNum > 1 && dataInAPage != 10)
                SimpleUtils.pass("There are more than 1 pages of data and 1o records in a page!");
            else
                SimpleUtils.report("The total data count in the current page is:" + dataInAPage);
            //check the latest updated data will show as the first one
            if (searchAPlan(planName)) {
                //make changes to plan name and check the position
                planSearchedResults.get(0).findElement(By.cssSelector("lg-button[label=\"Edit\"]")).click();
                waitForSeconds(1);
                //check if edit dialog pops up
                if (isElementLoaded(editParentPlanDialog, 5)) {
                    SimpleUtils.pass("Edit parent plan dialog pops up successfully");
                    editParentPlanName.clear();
                    editParentPlanName.sendKeys(planName + currentDate);
                    //click ok
                    planCreateOKBTN.click();
                    waitForSeconds(3);
                    //check edit successfully
                    if (searchAPlan(planName + currentDate)) {
                        SimpleUtils.pass("Parent plan edit successfully");
                        //check the first record of data is the one edited
                        planSearchInputField.clear();
                        planSearchInputField.sendKeys(Keys.ENTER);
                        //get the title of the first data
                        String planTitle = planSearchedResults.get(0).findElement(By.cssSelector(".lg-scenario-table-improved__plan-name")).getText().trim();
                        System.out.println("planTitle:" + planTitle);
                        System.out.println("planName:" + planName);
                        if (planTitle.equals(planName + currentDate)){
                            SimpleUtils.pass("The latest updated data show as the first of record successfully!");
                        }
                        //recover the data
                        if (searchAPlan(planName + currentDate)) {
                            planSearchedResults.get(0).findElement(By.cssSelector("lg-button[label=\"Edit\"]")).click();
                            waitForSeconds(2);
                            editParentPlanName.clear();
                            editParentPlanName.sendKeys(planName + currentDate + "updated");
                            planCreateOKBTN.click();
                        }
                    }
                }
            }
            //check the latest updated data will show as the first one
            if (searchAPlan(planName)) {
                planSearchedResults.get(0).click();
                //check expanded plan and view link
                if (areListElementVisible(scenarioPlans)) {
                    SimpleUtils.pass("Plan with scenario plans loaded successfully after it was expanded!");
                    //check the view link
                    if (isElementLoaded(scenarioPlans.get(0).findElement(By.cssSelector(" lg-button[label=\"View\"]")), 3)) {
                        SimpleUtils.pass("View link is existing under a scenario plan!");
                        //get the scenario plan title in list
                        String scplanNameInList = scenarioPlans.get(0).findElement(By.cssSelector("div:nth-child(1)")).getText().trim();
                        //click the link
                        clickTheElement(scenarioPlans.get(0).findElement(By.cssSelector(" lg-button[label=\"View\"]")));
                        waitForSeconds(2);
                        //check page navigate to the detail
                        //get the title of the scenario plan
                        String planTitleInDetails = scenarioPlanNameInDeatil.getText().trim();
                        if (planTitleInDetails.equals(scplanNameInList))
                            SimpleUtils.pass("View link navigate to the plan detail successfully");
                        else
                            SimpleUtils.fail("View link navigate to the plan detail failed", false);
                    } else
                        SimpleUtils.fail("The view link is not show under a scneario plan", false);


                } else
                    SimpleUtils.report("The parent plan has no scenario plan till now!");
            }
        }
    }

    @Override
    public void takeOperationToPlan(String parentPlanName, String scenarioPlanName, String status) throws Exception {
        searchAPlan(parentPlanName);
        //click the planName to expand
        WebElement planName = planSearchedResults.get(0).findElement(By.cssSelector("div[ng-click=\"expandScenario(plan)\"]"));
        if (isElementLoaded(planName, 5)) {
            SimpleUtils.pass("plan name loaded successfully!");
            if (!areListElementVisible(scenarioPlans, 5)){
                clickTheElement(planName);
            }
            //find the scenario plan to view
            if (scenarioPlans.size() > 0) {
                SimpleUtils.pass("Scenario plan loaded successfully");
                for (int index = 0; index < scenarioPlans.size(); index++) {
                    if (scenarioPlans.get(index).findElement(By.cssSelector("div.lg-scenario-table-improved__grid-column--left.ng-binding")).getText().contains(scenarioPlanName)) {
                        WebElement viewEle = scenarioPlans.get(index).findElement(By.cssSelector("lg-button[label=\"View\"] button"));
                        if (isElementLoaded(viewEle, 5)) {
                            SimpleUtils.pass("Plan view button loaded successfully");
                            clickTheElement(viewEle);
                            waitForSeconds(2);
                        } else
                            SimpleUtils.fail("Plan view button not loaded", false);
                    }
                    //check if enter the scenario plan detail
                    if (isElementLoaded(planStatusInDetail, 5) && scenarioPlanNameInDeatil.getText().equals(scenarioPlanName)) {
                        SimpleUtils.pass("Scenario plan loaded successfully with status name!");
                        //check the status and operation
                        String currentStatus = planStatusInDetail.getText().trim();
                        if (status.equalsIgnoreCase("Not Started") && isElementLoaded(generateForecastButton)) {
                            //need to generate forecast and budget
                            clickTheElement(generateForecastButton);
                            if (isElementLoaded(forecastRunDialog, 5)) {
                                SimpleUtils.pass("Generate forecast dialog pops up successfully!");
                                //click the generate button on dialog
                                clickTheElement(forecastGenerateBTNOnDialog);
                                {
                                    //wait until the download csv for generate forecast button is displayed
                                    waitUntilElementIsInVisible(forecastDownloadCSVLink);
                                    if (scenarioPlanStatusInDetail.getText().equals("In Progress"))
                                        SimpleUtils.pass("Scenario plan changed to in progress from not started!");
                                    else
                                        SimpleUtils.fail("Scenario plan not changed to in progress status", false);
                                }
                            }
                        } else if (status.equalsIgnoreCase("In Progress") && isElementLoaded(runBudgetButton)) {
                            //click the run budget button
                            clickTheElement(runBudgetButton);
                            waitUntilElementIsInVisible(budgetDownloadCSVLink);
                            //check the status changed to completed
                            if (scenarioPlanNameInDeatil.getText().equals("Completed"))
                                SimpleUtils.pass("Scenario plan changed to completed from in progress!");
                            else
                                SimpleUtils.fail("Scenario plan not changed to Scenario plan changed to completed from in progress", false);
                            //check the send for review button is enabled
                            if (isElementEnabled(sendForReviewButton)) {
                                SimpleUtils.pass("The send for review button is enabled!");
                                clickTheElement(sendForReviewButton);
                                if (isElementLoaded(sendButtonOnDialog)) {
                                    SimpleUtils.pass("Send for review dialog pops up successfully!");
                                    clickTheElement(sendButtonOnDialog);
                                } else
                                    SimpleUtils.fail("Send for review dialog not pop up", false);


                            } else
                                SimpleUtils.fail("The send for review button is not enabled", false);
                        } else
                            SimpleUtils.fail("run budget button not loaded!", false);
                    } else
                        SimpleUtils.fail("Scenario plan detail page not loaded!", false);
                }
            } else
                SimpleUtils.fail("No scenario plan loaded!", false);

        } else
            SimpleUtils.fail("Arrow icon to expand a parent plan not loaded!", false);
    }

    @Override
    public String getCurrentLocationsForCreatePlan() throws Exception {
        String currentLoc = null;
        if (isElementLoaded(createPlanBtn)) {
            clickTheElement(createPlanBtn);
            //check the create plan dialog
            if (isElementLoaded(createPlanModalDialog)) {
                SimpleUtils.pass("Create plan dialog pops up successfully");
                if (isElementLoaded(locationInCreatePlanDialog)) {
                    currentLoc = locationInCreatePlanDialog.getText().trim();
                    //click cancel to dismiss the dialog
                    clickTheElement(PlanCreateCancelBTN);
                }
            }
        }
        return currentLoc;
    }

    public void verifyCreatePlanDialog(String planName) throws Exception {
        String startDay1 = null;
        String startDay2 = null;
        String endDay1 = null;
        String endDay2 = null;
        if (isElementLoaded(createPlanBtn)) {
            SimpleUtils.pass("Create plan button show in the plan landing page successfully");
            clickTheElement(createPlanBtn);
            //check the create plan dialog
            if (isElementLoaded(createPlanModalDialog)) {
                SimpleUtils.pass("Create plan dialog pops up successfully");
                //check the duration-the day before today can not be selected
                String today = planDurationToday.getText().trim();
                for (WebElement disableDdays : planDurationDisabledDays) {
                    if (Integer.parseInt(disableDdays.getText().trim()) < Integer.parseInt(today))
                        SimpleUtils.pass("The days that before today are disabled to select");
                    else
                        SimpleUtils.fail("The days before today are enabled!", false);
                }
                //check the max length of the duration is not more than 12 months
                planDurationSetting(1, 400);
                if (areListElementVisible(planDurationStartAndEnd)) {
                    //get the start day value
                    startDay1 = planDurationStartAndEnd.get(0).getText();
                    //get the end day value
                    endDay1 = planDurationStartAndEnd.get(1).getText();
                }
                //check the error message
//                if (isElementLoaded(planDurationErrorMsg) && planDurationErrorMsg.getText().contains("Duration can not exceed 12 months"))
//                    SimpleUtils.pass("The duration for the plan is not more than 12 months");
//                else
//                    SimpleUtils.fail("The duration for the plan can set to more than 12 months", false);
                if (isElementLoaded(planDialogCloseIcon)) {
                    SimpleUtils.pass("The close icon is show in plan create dialog");
                    //click it to dismiss the dialog
                    clickTheElement(planDialogCloseIcon);
                    waitForSeconds(2);
                }
                //click the create plan button again
                if (isElementLoaded(createPlanBtn)) {
                    SimpleUtils.pass("Page navigate to plan list");
                    clickTheElement(createPlanBtn);
                    //select the duration
                    planDurationSetting(7, 14);
                    if (areListElementVisible(planDurationStartAndEnd)) {
                        //get the start day value
                        startDay1 = planDurationStartAndEnd.get(0).getText();
                        //get the end day value
                        endDay1 = planDurationStartAndEnd.get(1).getText();
                    }
                    //assert the start and end day is not a fixed day, it can be change according to selection
                    if (!startDay1.equals(startDay2))
                        SimpleUtils.pass("The from day value can be changed according to the start day selection");
                    else
                        SimpleUtils.fail("The from day value can not be changed according to the start day selection", false);
                    if (!endDay1.equals(endDay2))
                        SimpleUtils.pass("The end day value can be changed according to the end day selection");
                    else
                        SimpleUtils.fail("The end day value can not be changed according to the end day selection", false);
                    //check the plan name is required
                    createPlanName.clear();
                    //get the ok button disabled status
                    String okStatus = planCreateOKBTN.getAttribute("disabled");
                    if (okStatus.equals("true"))
                        SimpleUtils.pass("The ok button is disabled if plan name is not filled");
                    else
                        SimpleUtils.fail("The ok button enabled when plan name is empty!", false);
                    //check the error info for empty plan name
                    createPlanName.sendKeys("name");
                    createPlanName.clear();
                    if (isElementLoaded(createPlanNameError)) {
                        //get the error message
                        String planNameRequired = createPlanNameError.getText().trim();
                        if (planNameRequired.equals("Budget plan name is required")) ;
                        SimpleUtils.pass("The plan name is not allowed as empty");
                    }
                    //fill the plan name with some special chars
                    createPlanName.clear();
                    createPlanName.sendKeys(planName + "@#$%&*");
                    planCreateOKBTN.click();
                    if (isElementLoaded(errorToast, 5)) {
                        if (errorToast.getText().contains("plan name is empty or have special character")) {
                            SimpleUtils.pass("The plan name is not allowed as empty or with special chars");
                        }
                    }
                    //create the plan as name with "-" and "_"
                    createPlanName.clear();
                    createPlanName.sendKeys(planName + "-n1_m1");
                    planCreateOKBTN.click();
                    //check plan create successfully.
                    if(isElementLoaded(errorToast, 3)){
                        if (errorToast.getText().contains("plan name already exists"))
                            SimpleUtils.pass("The plan name already exists!");
                    }else if (isElementLoaded(scenarioPlanNameInDeatil, 3)) {
                        String planTitleInDetails = scenarioPlanNameInDeatil.getText().trim();
                        if (planTitleInDetails.equals(planName + "-n1_m1 scenario 1"))
                            SimpleUtils.pass("Plan created successfully and page navigated to the scenario plan detail successfully!");
                    }else {
                        SimpleUtils.fail("Plan failed to be created!", false);
                    }
                }

            }
        }
        else
            SimpleUtils.fail("Create plan button not show in the plan landing page",false);
    }

    @Override
    public boolean archiveAPlan(String planName,String scplanName) throws Exception {
        boolean archiveAct = true;
        //go to the scenario plan detail
        goToScenarioPlanDetail(planName, scplanName);
        if (isElementLoaded(planDialogArchiveBTN)) {
            SimpleUtils.pass("Archive button is showed in scenario plan detail page successfully");
            clickTheElement(planDialogArchiveBTN);
            waitForSeconds(5);
            if (isElementLoaded(scenarioPlanArchivedialog)){
                SimpleUtils.pass("Archive dialog pops up successfully!");
                //click the archive
                scenarioPlanArchivedialog.findElement(By.cssSelector("lg-button[label=\"Archive\"] button")).click();
                if (isElementLoaded(errorToast, 5)) {
                    if (errorToast.getText().contains("cannot archive scenario while it is in progress")) {
                        archiveAct = false;
                        SimpleUtils.fail("User failed to archive a plan!", false);
                    }
                }
                else {
                    waitForSeconds(2);
                    //check the scenario plan removed from the list
                    Boolean archiveRes = goToScenarioPlanDetail(planName, scplanName);
                    if (!archiveRes)
                        SimpleUtils.pass("Archive a scenario plan successfully");
                    }
                } else
                    SimpleUtils.fail("Archive dialog not pops up!", false);
                } else
                    SimpleUtils.fail("No arhive button show in the plan detail page", false);
        return archiveAct;
        }

    private boolean goToScenarioPlanDetail(String planName,String scplanName) throws Exception{
        Boolean scplanExist=false;
        //check the latest updated data will show as the first one
        if (searchAPlan(planName)) {
            if (!areListElementVisible(scenarioPlans, 5))
                //click the plan to expand
                clickTheElement(planSearchedResults.get(0).findElement(By.cssSelector("div:nth-child(1)")));
            waitForSeconds(2);
            if (areListElementVisible(scenarioPlans)) {
                SimpleUtils.pass("Plan with scenario plans loaded successfully after it was expanded!");
                for (WebElement scplan : scenarioPlans) {
                    if (scplan.findElement(By.cssSelector(" div:nth-child(1)")).getText().trim().contains(scplanName)) {
                        SimpleUtils.pass("Find the scenario plan successfully!");
                        scplanExist = true;
                        SimpleUtils.pass("Find the tested scenario plan");
                        //get the scenario plan title in list
                        String scplanNameInList = scplan.findElement(By.cssSelector("div:nth-child(1)")).getText().trim();
                        //click the view to enter its detail
                        if (isElementLoaded(scplan.findElement(By.cssSelector(" lg-button[label=\"View\"]")))) {
                            SimpleUtils.pass("View link is supported to scenario plan");
                            //click the view to enter detail
                            scplan.findElement(By.cssSelector(" lg-button[label=\"View\"]")).click();
                            waitForSeconds(2);
                            if (isElementLoaded(scenarioPlanNameInDeatil) && isElementLoaded(scenarioPlanDetailEmail)) {
                                //get the title of the scenario plan
                                String planTitleInDetails = scenarioPlanNameInDeatil.getText().trim();
                                if (planTitleInDetails.equals(scplanNameInList)||planTitleInDetails.contains(scplanName))
                                    SimpleUtils.pass("View link navigate to the plan detail successfully");
                                else
                                    SimpleUtils.fail("View link navigate to the plan detail failed", false);
                            }
                        }
                        break;
                    }
                }
            }
                else
                SimpleUtils.fail("No scenario plan for the parent plan", false);
        }

        return scplanExist;
    }

    @Override
    public void verifyCreatePlanDetailUICheck(String planName, String scplan, String copiedScName) throws Exception{
        //check if the scenario plan existed
        if(goToScenarioPlanDetail(planName,scplan))
            archiveAPlan(planName,scplan);
        //try to create a scenario plan
        verifyScenarioPlanAutoCreated(planName,scplan);
        //go to the scenario plan detail
        goToScenarioPlanDetail(planName,scplan);
        //check the copy button
        if(isElementLoaded(planDialogCopyBTN)){
            SimpleUtils.pass("Copy button is showed in scenario plan detail page successfully");
            clickTheElement(planDialogCopyBTN);
            waitForSeconds(5);
            if(isElementLoaded(scenarioPlanCopydialog, 10)){
                SimpleUtils.pass("Copy dialog pops up successfully!");
                //get the default plan name
                String defaultCopyPlanName=scenarioPlanNameInput.getAttribute("value").trim();
                if(defaultCopyPlanName.equals("Copy Of "+scplan))
                    SimpleUtils.pass("The default scenario plan name is correct");
                else
                    SimpleUtils.report("The default scenario plan name is incorrect!");
                //input scenario name
                scenarioPlanNameInput.clear();
                scenarioPlanNameInput.sendKeys(copiedScName);
                //click the copy button
                scenarioPlanCopydialog.findElement(By.cssSelector(" lg-button[label=\"Create\"] ")).click();
                waitForSeconds(2);
                //Check the current navigated plan name is the new copied plan
                if(isElementLoaded(scenarioPlanNameInDeatil)&&isElementLoaded(scenarioPlanDetailEmail)) {
                    //get the title of the scenario plan
                    String planTitleInDetails = scenarioPlanNameInDeatil.getText().trim();
                    if (planTitleInDetails.equals(copiedScName)) {
                        SimpleUtils.pass("Copy to generated a new scenario plan successfully");
                        //input email
                        scenarioEmailInput.clear();
                        scenarioEmailInput.sendKeys("CCAutoTest1@legion.co,CCAutoTest2@legion.co");
                        //click save to back to plan list page
                        clickTheElement(planSaveButton);
                    }
                    else
                        SimpleUtils.fail("Copy to generated a new scenario plan failed", false);
                            }
            }
            else
                SimpleUtils.fail("Copy dialog not pops up!",false);
        }
        else
            SimpleUtils.fail("No Copy button show in the plan detail page",false);
        //check the archive button to archive the copied plan
        if(archiveAPlan(planName,copiedScName))
            SimpleUtils.pass("The scenario plan:"+copiedScName+"was archived successfully@");
        //check the status change--create a new scenario plan
        goToScenarioPlanDetail(planName,scplan);
        //check the back link in detail page
        clickTheElement(scenarioPlanBackLink);
        //check page will back to plan list
        if (isElementLoaded(planSearchInputField, 3))
            SimpleUtils.pass("Page back to plan list after click back lin at the scenario plan!");
        //change the plan from not started to in progress--run forecast and budget
        goToScenarioPlanDetail(planName,scplan);
        String st = getScenarioPlanStatus();
        if (st != null && st.equals("Not Started")) {
            SimpleUtils.pass("The scenario plan status is not started!");
            //check the run budget status
            if (isElementLoaded(runBudgetButton) && runBudgetButton.getAttribute("disabled").equals("true"))
                SimpleUtils.pass("The run budget button is disabled when the scenario plan is not started!");
            else
                SimpleUtils.report("The run budget button is enabled when the scenario plan is not started!");
            //click generate forecast to check the status change to in progress
            if (isElementLoaded(generateForecastButton))
                clickTheElement(generateForecastButton);
            if (isElementLoaded(forecastRunDialog) && isElementLoaded(forecastGenerateBTNOnDialog)) {
                //click the generate button to run the forecast
                clickTheElement(forecastGenerateBTNOnDialog);
                waitForSeconds(2);
                //get the status again
                if (scenarioPlanStatusInDetail.getText().equals("In Progress"))
                    SimpleUtils.pass("Begin to generate forecast will push the plan status from not started to in progress!");
                //wait some seconds till the forecast job finished
                if (areListElementVisible(scenarioPlanReRunBTNs, 500)) {
                    SimpleUtils.pass("The forecast job run to finished!");
                    //check the budget button is enabled
                    if (runBudgetButton.getAttribute("disabled") == null) {
                        SimpleUtils.pass("The run budget button is enabled after the forecast job finished");
                        //click the run budget to run budget
                        clickTheElement(runBudgetButton);
                        if (isElementLoaded(budgetRunDialog) && isElementLoaded(budgetRunBTNOnDialog)) {
                            clickTheElement(budgetRunBTNOnDialog);
                            //check the budget result
                            if (isElementLoaded(budgetValue,300) && Double.valueOf(budgetValue.getText().split("\\$")[1].trim().replace(",", "")).intValue() > 0) {
                                SimpleUtils.pass("Budget job run complete and get the result budget hours on UI!");
                                // send for review for the plan
                                sendForReviewAplan();
                            }
                        }
                    }
                }
            }
        }
        //check page back to plan landing page and continue to check reject action
        if(goToScenarioPlanDetail(planName,scplan)){
            String sts = getScenarioPlanStatus();
            if (sts != null && sts.equals("Ready For Review")) {
                SimpleUtils.pass("Plan changed to ready for review after click send for review!");
                //do reject
                rejectAScenarioPlan();
            }
        }
        //check page auto navigated to plan landing page and continue to  send for review again
        if(goToScenarioPlanDetail(planName,scplan)) {
            //check the status changed to ready for review
            String sta = getScenarioPlanStatus();
            if (sta != null && sta.equals("Reviewed-Rejected")) {
                SimpleUtils.pass("Plan changed to Reviewed-Rejected after click reject!");
                //do send for review again
                sendForReviewAplan();
            }
        }
        //check page back to plan landing page and continue to check approve action
        if(goToScenarioPlanDetail(planName,scplan)){
            String sts = getScenarioPlanStatus();
            if (sts != null && sts.equals("Ready For Review")) {
                SimpleUtils.pass("Plan changed to ready for review after click send for review again!");
                //do approve
                approveAScenarioPlan();
            }
        }
        //check page auto navigated to plan landing page and continue to set in effect
        if(goToScenarioPlanDetail(planName,scplan)) {
            //check the status changed to ready for review
            String sta = getScenarioPlanStatus();
            if (sta != null && sta.equals("Reviewed-Approved")) {
                SimpleUtils.pass("Plan changed to Reviewed-Approved after click approved!");
                //do set in effect
                if(isElementLoaded(scenarioPlanSetInEffectBTN,5)) {
                    SimpleUtils.pass("The set in effect button is enabled in plan");
                    clickTheElement(scenarioPlanSetInEffectBTN);
                    waitForSeconds(2);
                    if (isElementLoaded(setInEffectPopup)){
                        clickTheElement(setInEffectButtonOnSetInEffectPopup);
                        waitForSeconds(3);
                    }
                    //check the parent plan changed to in effect
                    if(searchAPlan(planName)){
                        //check status for prent plan
                        String ineffectSta=planSearchedResults.get(0).findElement(By.cssSelector("span.status-highlight")).getText().trim();
                        if(ineffectSta.equals("In Effect"))
                            SimpleUtils.pass("The parent plan status changed to in effect successfully!");

                    }
                }

            }
        }
        //do data archive to make sure the flow can be run every time---reject and then archive
        if(goToScenarioPlanDetail(planName,scplan)){
            rejectAScenarioPlan();
        }
        archiveAPlan(planName,scplan);

    }

    private void approveAScenarioPlan() throws Exception{
        if(isElementLoaded(scenarioPlanApproveBTN)){
            SimpleUtils.pass("The approve button displayed at scenario plan detail!");
            //click approve
            clickTheElement(scenarioPlanApproveBTN);
            if(isElementLoaded(approveBudgetDialog,5)){
                SimpleUtils.pass("Approve budget plan dialog pops up successfully!");
                clickTheElement(approveBudgetDialog.findElement(By.cssSelector("lg-button[label=\"Approve\"] button")));
                waitForSeconds(2);

            }
            else
                SimpleUtils.fail("No approve dialog not show on Page!",false);
        }
        else
            SimpleUtils.fail("No approve button showed on Page!",false);
    }


    private void rejectAScenarioPlan() throws Exception{
        if(isElementLoaded(scenarioPlanRejectBTN)){
          SimpleUtils.pass("The approve button displayed at scenario plan detail!");
        //click approve
        clickTheElement(scenarioPlanRejectBTN);
        if(isElementLoaded(scenarioPlanRejectDialog,5)){
        SimpleUtils.pass("Approve budget plan dialog pops up successfully!");
        clickTheElement(scenarioPlanRejectDialog.findElement(By.cssSelector("lg-button[label=\"Reject\"] button")));
        waitForSeconds(2);

         }
        else
            SimpleUtils.fail("No approve dialog not show on Page!",false);
        }
        else
            SimpleUtils.fail("No approve button showed on Page!",false);
        }


        private void sendForReviewAplan() throws Exception {
            //continue to check the ready review status in a complete plan
            if (isElementLoaded(sendForReviewButton, 10)) {
                SimpleUtils.pass("The send for review button show ob plan successfully!");
                clickTheElement(sendForReviewButton);
                if (isElementLoaded(sendForReviewDialog, 5)) {
                    SimpleUtils.pass("The send for review dialog pops up successfully!");
                    clickTheElement(sendButtonOnDialog);
                    waitForSeconds(2);
                } else
                    SimpleUtils.fail("Send for reivew dialog not show on Page!", false);
            } else
                SimpleUtils.fail("No Send for review button show on Page!", false);
        }



    @Override
    public String getScenarioPlanStatus() throws Exception{
        String currentStatus = null;
        //get the title, duration and updated info and assert they are with values
        if (areListElementVisible(scenarioPlanContents, 5) && scenarioPlanContents.size() == 3) {
            SimpleUtils.pass("Scenario plan contents loaded successfully!");
            //Check the status and budget value
            String status = scenarioPlanContents.get(2).getText().split(":")[1];
            if (status != null)
                currentStatus = status.trim();
        }
        return currentStatus;
    }

    @Override
    public void verifyPlanDetail(String planName,String scplan) throws Exception {
        while (!areListElementVisible(planSearchedResults)){
            waitForSeconds(2);
        }

        //select a scenario plan and check the duration
        if (goToScenarioPlanDetail(planName, scplan)) {
            SimpleUtils.pass("The test scenario plan existed");
            //get the title, duration and updated info and assert they are with values
            if (areListElementVisible(scenarioPlanContents, 5) && scenarioPlanContents.size() == 3) {
                SimpleUtils.pass("Scenario plan contents loaded successfully!");
                //Check duration
                String planDuration = scenarioPlanContents.get(0).getText().split(":")[1];
                if (planDuration != null && planDuration.contains("/20"))
                    SimpleUtils.pass("The plan duration loaded successfully at plan detail page!");
                else
                    SimpleUtils.fail("The plan duration not loaded successfully at plan detail page!", false);
                //Check the status and budget value
                String status = scenarioPlanContents.get(2).getText().replaceAll(" ","").split(":")[1].trim();
                String budgetStr=scenarioPlanContents.get(1).getText().split("\\$")[1];
                String budgetValue = budgetStr.contains(".")? budgetStr.split("\\.")[0]:budgetStr.trim();
                budgetValue = budgetValue.contains(",")?budgetValue.replaceAll(",","").trim():budgetValue.trim();
                if (status != null && status.equals("Completed") || status.equals("Ready For Review") || status.equals("Reviewed-Rejected") || status.equals("Reviewed-Approved")) {
                    SimpleUtils.report("The current plan status is:"+status);
                    //get the budget value and assert the budget value is greater than 0
                    if (budgetValue != null && Integer.parseInt(budgetValue) > 0)
                        SimpleUtils.pass("The budget dollar values has been generated for complete/ready review/approved scenario plan");
                    else
                        SimpleUtils.fail("The budget dollar values has not been generated for complete/ready review/approved scenario plan", false);
                }
                else if (budgetValue.equals("--"))
                    SimpleUtils.pass("The budget hours show as blank for not started or in progress plan ");
                else
                    SimpleUtils.fail("The plan status show as empty", false);

                //check the location count and updated info
                if (areListElementVisible(scenarioPlanDetails, 5) && scenarioPlanDetails.size() == 3) {
                    SimpleUtils.pass("The test scenario plan details loaded successfully!");
                    String planUpdatedInfo = scenarioPlanDetails.get(0).getText().split(":")[1];
                    if (planUpdatedInfo != null && planUpdatedInfo.contains("lizzy100"))
                        SimpleUtils.pass("The plan updated info loaded successfully at plan detail page!");
                    else
                        SimpleUtils.fail("The plan plan updated info not loaded successfully at plan detail page!", false);
                    //check the total location count
                    String locaCount = scenarioPlanDetails.get(1).getText().split(":")[1];
                    if (locaCount != null && Integer.parseInt(locaCount.trim()) == 3)
                        SimpleUtils.pass("The location count show correctly at plan detail page");
                    else
                        SimpleUtils.fail("The location count not show at plan detail page", false);

                }
            } else
                SimpleUtils.fail("No scenario plan detail loaded!", false);

            //check the OH and wages download status
            if(isElementLoaded(OHDownloadLink,10))
                SimpleUtils.pass("OH template is ready to be donloaded!");
            if(isElementLoaded(wagesDownloadLink,10))
                SimpleUtils.pass("Wages template is ready to be donloaded!");

        }
        //back to plan landing page
        clickTheElement(scenarioPlanBackLink);
        waitForSeconds(2);

    }

    @FindBy(css="span[ng-class=\"getPlanStatusClass(plan)\"]")
    private WebElement planStatusInList;

    @FindBy(css="span[ng-class=\"getPlanStatusClass(s)\"]")
    private List<WebElement> scenarioStatusInList;

    @Override
    public void verifyPlanStatus(String planName) throws Exception {
        if(isElementEnabled(createPlanBtn,5)&&isElementEnabled(planSearchInputField)){
            if(searchAPlan(planName)){
                List<String> scStatusList = new ArrayList<>();
                String planStatusInList=planSearchedResults.get(0).findElement(By.cssSelector("span.status-highlight")).getText().trim();
                clickTheElement(planSearchedResults.get(0).findElement(By.cssSelector("div[title]")));
                if(areListElementVisible(scenarioStatusInList,5) && scenarioStatusInList.size()!=0){
                    for(WebElement scenarioStatus:scenarioStatusInList){
                        String scStatus = scenarioStatus.getText().trim();
                        scStatusList.add(scStatus);
                    }
                }
                //check whether the Reviewed-Approved plan status is correct or not?
                if(planStatusInList.equalsIgnoreCase("Reviewed-Approved")){
                    boolean flag = false;
                    for(String scStatus:scStatusList){
                        if(scStatus.equalsIgnoreCase("Reviewed-Approved")){
                            SimpleUtils.pass("There is one scenario status is Reviewed-Approved.");
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        SimpleUtils.pass("When there is one scenario is Reviewed-Approved status, then the plan status will be Reviewed-Approved!");
                    }
                    else {
                        SimpleUtils.fail("The Plan status is not correct!",false);
                    }
                }
                //check whether the Reviewed-Rejected plan status is correct or not?
                if(planStatusInList.equalsIgnoreCase("Reviewed-Rejected")){
                    boolean flag = false;
                    for(String scStatus:scStatusList){
                        if(scStatus.equalsIgnoreCase("Reviewed-Rejected")){
                            SimpleUtils.pass("There is one scenario status is Reviewed-Rejected.");
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        SimpleUtils.pass("When there is one scenario is Reviewed-Rejected status, then the plan status will be Reviewed-Rejected!");
                    }
                    else {
                        SimpleUtils.fail("The Plan status is not correct!",false);
                    }
                }
                //check whether the In Progress plan status is correct or not?
                if(planStatusInList.equalsIgnoreCase("In Progress")){
                    List<String> status = new ArrayList<>(Arrays.asList("Not Started","Completed","In Progress"));
                    boolean flag = false;
                //remove duplicated status
                    HashSet<String> set = new HashSet<String>();
                    List<String> result = new ArrayList<String>();
                    for(String scStatus:scStatusList){
                        if(set.add(scStatus)){
                            result.add(scStatus);
                        }
                    }
                //if list has only one type of status then the status should not be "Not Started","Reviewed-Rejected","Reviewed-Approved"
                    if(result.size() == 1 && result.get(0) != "Not Started" && result.get(0) != "Reviewed-Rejected"
                            && result.get(0) != "Reviewed-Approved" && result.get(0) != "Ready For Review"){
                        SimpleUtils.pass("All Scenarios status are not Not Started|Reviewed-Rejected|Reviewed-Approved|Ready For Review");
                        flag = true;
                    }else if((result.size() > 1 && result.size() < 4) && status.containsAll(result)){
                        flag = true;
                        SimpleUtils.pass("All Scenarios are Not Started or Completed or In Progress status");
                    }else {
                        flag = false;
                    }

                    if(flag){
                        SimpleUtils.pass("All Scenarios are not Not Started and No single Scenario is Approved | Rejected | Ready For Review," +
                                " Then the plan status is In Progress");
                    }
                    else {
                        SimpleUtils.fail("The Plan status is not correct!",false);
                    }
                }

                //check whether the Not Started plan status is correct or not?
                if(planStatusInList.equalsIgnoreCase("Not Started")){
                    boolean flag = false;
                    //remove duplicated status
                    HashSet<String> set = new HashSet<String>();
                    List<String> result = new ArrayList<String>();
                    for(String scStatus:scStatusList){
                        if(set.add(scStatus)){
                            result.add(scStatus);
                        }
                    }
                    if(result.size()==1 && result.get(0).contains("Not Started")){
                        flag = true;
                    }

                    if(flag){
                        SimpleUtils.pass("When all scenarios are Not Started status, then the plan status will be Not Started!");
                    }
                    else {
                        SimpleUtils.fail("The Plan status is not correct!",false);
                    }
                }

                //check whether the Ready For Review plan status is correct or not?
                if(planStatusInList.equalsIgnoreCase("Ready For Review")){
                    boolean flag = false;
                    for(String scStatus:scStatusList){
                        if(scStatus.equalsIgnoreCase("Ready For Review")){
                            SimpleUtils.pass("There is one scenario status is Ready For Review.");
                            flag = true;
                            break;
                        }
                    }

                    if(flag){
                        SimpleUtils.pass("There is one scenario status is Ready For Review, so the plan status is Ready For Review!");
                    }
                    else {
                        SimpleUtils.fail("The Plan status is not correct!",false);
                    }
                }
            }else {
                SimpleUtils.fail("This Plan is not existing!",false);
            }
        }else {
            SimpleUtils.fail("The Plan landing page doesn't show well!",false);
        }
    }

    @FindBy(css="lg-button[label=\"Set in effect\"] button")
    private WebElement setInEffectButton;
    @FindBy(css="div.modal-dialog ")
    private WebElement setInEffectPopup;
    @FindBy(css="div.modal-dialog h1 div")
    private WebElement setInEffectPopupTitle;
    @FindBy(css="div.modal-dialog general-form p")
    private WebElement setInEffectPopupText;
    @FindBy(css="div.modal-dialog lg-button[label=\"Cancel\"] button")
    private WebElement cancelButtonOnSetInEffectPopup;
    @FindBy(css="div.modal-dialog lg-button[label=\"Set in effect\"] button")
    private WebElement setInEffectButtonOnSetInEffectPopup;
    @FindBy(css="div.modal-dialog h1 lg-close")
    private WebElement closeButtonOnSetInEffectPopup;

    @Override
    public void verifySetInEffectPopup(String planName,String scplanName) throws Exception {
        if(isElementEnabled(createPlanBtn,5)&&isElementEnabled(planSearchInputField)){
            goToScenarioPlanDetail(planName, scplanName);
            //Check whether show popup after click set in effect button
            if(isElementEnabled(setInEffectButton,2)){
                clickTheElement(setInEffectButton);
                waitForSeconds(2);
                if(isElementEnabled(setInEffectPopup,2)){
                    SimpleUtils.pass("Will show a popup after click set in effect button");
                }else {
                    SimpleUtils.fail("Will NOT show a popup after click set in effect button",false);
                }
                //Verify title and body text is correct or not?
                String title = setInEffectPopupTitle.getText().trim();
                String body = setInEffectPopupText.getText().trim();
                String expectedTitle ="Set Budget Plan in Effect";
                String expectedBody="This will copy the budget plan to the weekly schedules. " +
                        "You can further edit individual week's budget or set another budget plan in effect as needed.";
                if(title.equalsIgnoreCase(expectedTitle) && body.equalsIgnoreCase(expectedBody)){
                    SimpleUtils.pass("Set in effect popup title and body text is correct");
                }else {
                    SimpleUtils.fail("Set in effect popup title and body text is correct",false);
                }

                //Verify user can click cancel button , x button and set in effect button in popup
                if(isElementEnabled(cancelButtonOnSetInEffectPopup) && isElementEnabled(setInEffectButtonOnSetInEffectPopup)
                && isElementEnabled(closeButtonOnSetInEffectPopup)){
                    SimpleUtils.pass("There is cancel button, X button and set in effect button on popup");
                    if(isClickable(setInEffectButtonOnSetInEffectPopup,2) && isClickable(cancelButtonOnSetInEffectPopup,2)
                    && isClickable(closeButtonOnSetInEffectPopup,2)){
                        SimpleUtils.pass("set in effect button, cancel button and close button are clickable");
                    }else{
                        SimpleUtils.fail("set in effect button, cancel button and close button aren't clickable",false);
                    }
                }else {
                    SimpleUtils.fail("There is no cancel button, X button and set in effect button on popup",false);
                }
                clickTheElement(closeButtonOnSetInEffectPopup);
                waitForSeconds(2);
                if(!isElementExist("div.modal-dialog")){
                    SimpleUtils.pass("User can click x button successfully");
                }else {
                    SimpleUtils.fail("User can't click x button",false);
                }
                clickTheElement(setInEffectButton);
                waitForSeconds(2);
                if(isElementEnabled(cancelButtonOnSetInEffectPopup,2)){
                    clickTheElement(cancelButtonOnSetInEffectPopup);
                    waitForSeconds(2);
                    if(!isElementExist("div.modal-dialog")){
                        SimpleUtils.pass("User can click cancel button successfully");
                    }else {
                        SimpleUtils.fail("User can't click cancel button",false);
                    }
                }
            }else {
                SimpleUtils.fail("There is no set in effect button showing",false);
            }
        }else {
            SimpleUtils.fail("Plan landing page doesn't display.",false);
        }
    }

    @FindBy(css="div.om-job-details content-box:nth-child(2) ng-transclude>div.om-jobs-budget-details__separator+div")
    private WebElement wageRateJobTitleSection;
    @Override
    public String verifyScenarioDetailPageUsingWorkRoleOrJobTitle(String planName, String scplan) throws Exception {
        String workRoleOrJobTitle = null;
        if (goToScenarioPlanDetail(planName, scplan)) {
            SimpleUtils.pass("The test scenario plan existed");
            if(isElementEnabled(wageRateJobTitleSection,2)){
                // Wage Rate ||  Wage rate and headcount
                workRoleOrJobTitle = wageRateJobTitleSection.findElement(By.cssSelector("h2")).getText().trim();

            }
        }else {
            SimpleUtils.fail("There is no this scenario",false);
        }
        return workRoleOrJobTitle;
    }
}


