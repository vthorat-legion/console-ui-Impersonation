package com.legion.pages.core.opemployeemanagement;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class TimeOffReasonConfigurationPage extends BasePage {
    public TimeOffReasonConfigurationPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading>div>a")
    private WebElement backButton;

    //bread crumbs
    @FindBy(css = "div.title-breadcrumbs>h1.title.ng-binding")
    private WebElement templateName;
    @FindBy(css = "div.title-breadcrumbs>h1.sub-title")
    private WebElement reasonName;

    //section label
    @FindBy(css = "form-section[form-title='Request Rules']  h2")
    private WebElement requestRulesSectionLabel;
    @FindBy(css = "form-section[form-title='Eligibility Rules']  h2")
    private WebElement eligibilityRulesSectionLabel;
    @FindBy(css = "form-section[form-title='Accrual Distribution']  h2")
    private WebElement accrualDistributionSectionLabel;

    //Time off rules list
    @FindBy(css = "form-section[form-title='Request Rules'] h3.lg-question-input__text.ng-binding")
    private List<WebElement> requestRuleLabels;
    @FindBy(css = "yes-no lg-button-group div.lg-button-group-first")
    private List<WebElement> yesToRules;
    @FindBy(css = "yes-no lg-button-group div.lg-button-group-last")
    private List<WebElement> noToRules;
    @FindBy(css = "input-field input")
    private List<WebElement> inputFields;

    //request rules
    //Employee can request ?
    @FindBy(css = "[question-title='Employee can request?'] h3")
    private WebElement canRequestLabel;
    @FindBy(css = "[question-title='Employee can request?'] yes-no div.lg-button-group-first")
    private WebElement yesToCanRequest;
    @FindBy(css = "[question-title='Employee can request?'] yes-no div.lg-button-group-last")
    private WebElement noToCanRequest;
    //Employee can request partial day ?
    @FindBy(css = "[question-title='Employee can request partial day?'] h3")
    private WebElement partialDayLabel;
    @FindBy(css = "[question-title='Employee can request partial day?'] yes-no div.lg-button-group-first")
    private WebElement yesToPartialDay;
    @FindBy(css = "[question-title='Employee can request partial day?'] yes-no div.lg-button-group-last")
    private WebElement noToPartialDay;
    //Manager can submit in timesheet ?
    @FindBy(css = "[question-title='Is this considered a paid time off?'] h3")
    private WebElement canSubmitInTimesheetLabel;
    @FindBy(css = "[question-title='Is this considered a paid time off?'] yes-no div.lg-button-group-first")
    private WebElement yesToSubmitInTimesheet;
    @FindBy(css = "[question-title='Is this considered a paid time off?'] yes-no div.lg-button-group-last")
    private WebElement noToSubmitInTimesheet;
    //Weekly limits(hours)
    @FindBy(css = "[question-title='Weekly limits (hours)'] h3")
    private WebElement weeklyLimitsLabel;
    @FindBy(css = "[question-title='Weekly limits (hours)'] input")
    private WebElement weeklyLimitsInput;
    @FindBy(css = "[question-title='Weekly limits (hours)'] div.input-faked.ng-binding")
    private WebElement weeklyLimitsInputNum;
    //Days request must be made in advance
    @FindBy(css = "[question-title='Days request must be made in advance'] h3")
    private WebElement requestMadeInAdvanceLabel;
    @FindBy(css = "[question-title='Days request must be made in advance'] input")
    private WebElement requestMadeInAdvanceInput;
    @FindBy(css = "[question-title='Days request must be made in advance'] div.input-faked.ng-binding")
    private WebElement requestMadeInAdvanceInputNum;
    //Configure all day time off default
    @FindBy(css = "[question-title='Configure all day time off default'] h3")
    private WebElement allDayDefaultLabel;
    @FindBy(css = "[question-title='Configure all day time off default'] input")
    private WebElement allDayDefaultInput;
    @FindBy(css = "[question-title='Configure all day time off default'] div.input-faked.ng-binding")
    private WebElement allDayDefaultInputNum;
    //Days an employee can request at one time
    @FindBy(css = "[question-title='Days an employee can request at one time'] h3")
    private WebElement daysRequestAtOneTimeLabel;
    @FindBy(css = "[question-title='Days an employee can request at one time'] input")
    private WebElement daysRequestAtOneTimeInput;
    @FindBy(css = "[question-title='Days an employee can request at one time'] div.input-faked.ng-binding")
    private WebElement daysRequestAtOneTimeInputNum;

    //Auto reject time off which exceed accrued hours ?
    @FindBy(css = "[question-title='Auto reject time off that exceeds accrued hours ?'] h3")
    private WebElement autoRejectLabel;
    @FindBy(css = "[question-title='Auto reject time off that exceeds accrued hours ?'] yes-no div.lg-button-group-first")
    private WebElement yesToAutoReject;
    @FindBy(css = "[question-title='Auto reject time off that exceed accrued hours ?'] yes-no div.lg-button-group-last")
    private WebElement noToAutoReject;
    //Allow Paid Time Off to compute to overtime ?
    @FindBy(css = "[question-title='Allow Paid Time Off to compute to overtime ?'] h3")
    private WebElement toComputeOverTimeLabel;
    @FindBy(css = "[question-title='Allow Paid Time Off to compute to overtime ?'] yes-no div.lg-button-group-first")
    private WebElement yesToComputeOverTime;
    @FindBy(css = "[question-title='Allow Paid Time Off to compute to overtime ?'] yes-no div.lg-button-group-last")
    private WebElement noToComputeOverTime;
    //Does this time off reason track Accruals ?
    @FindBy(css = "[question-title='Does this time off reason track Accruals ?'] h3")
    private WebElement trackAccrualLabel;
    @FindBy(css = "[question-title='Does this time off reason track Accruals ?'] yes-no div.lg-button-group-first")
    private WebElement yesToTrackAccrual;
    @FindBy(css = "[question-title='Does this time off reason track Accruals ?'] yes-no div.lg-button-group-last")
    private WebElement noToTrackAccrual;
    //Max hours in advance of what you earn
    @FindBy(css = "[question-title='Max hours in advance of what you earn'] h3")
    private WebElement maxHrsInAdvanceLabel;
    @FindBy(css = "[question-title='Max hours in advance of what you earn'] input")
    private WebElement maxHrsInAdvanceInput;
    @FindBy(css = "[question-title='Max hours in advance of what you earn'] div.input-faked.ng-binding")
    private WebElement maxHrsInAdvanceInputNum;

    //Enforce Yearly Limits
    @FindBy(css = "[question-title='Enforce Yearly Limits'] h3")
    private WebElement enforceYearlyLimitsLabel;
    @FindBy(css = "[question-title='Enforce Yearly Limits'] yes-no div.lg-button-group-first")
    private WebElement yesToEnforceYearlyLimits;
    @FindBy(css = "[question-title='Enforce Yearly Limits'] yes-no div.lg-button-group-last")
    private WebElement noToEnforceYearlyLimits;
    //Probation Period
    @FindBy(css = "[question-title='Probation Period'] h3")
    private WebElement probationLabel;
    @FindBy(css = "[question-title='Probation Period'] input")
    private WebElement probationInput;
    @FindBy(css = "[question-title='Probation Period'] div.input-faked.ng-binding")
    private WebElement probationInputNum;
    @FindBy(css = "[question-title='Probation Period'] select")//Days Months
    private WebElement probationSelect;
    //Annual Use Limit
    @FindBy(css = "[question-title='Annual Use Limit'] h3")
    private WebElement annualUseLimitLabel;
    @FindBy(css = "[question-title='Annual Use Limit'] input")
    private WebElement annualUseLimitInput;
    @FindBy(css = "[question-title='Annual Use Limit'] div.input-faked.ng-binding")
    private WebElement annualUseLimitInputNum;


    //Accrual Distribution
    @FindBy(css = "question-input[question-title='Accrual Start Date'] select")
    private List<WebElement> accrualStartDate;
    @FindBy(css = "question-input[question-title='Accrual End Date'] select")
    private List<WebElement> accrualEndDate;

    @FindBy(css = "question-input[question-title='Reinstatement Months'] input")
    private WebElement reinstatementMonth;
    @FindBy(css = "question-input[question-title='Distribution Method'] select")
    private WebElement distributionMethods;

    //Allowance in days
    @FindBy(css = "question-input[question-title='Allowance in days'] input-field input")
    private WebElement allowanceInPut;
    @FindBy(css = "question-input[question-title='Allowance in days'] input-field select")
    private WebElement allowanceInSelect;

    //Distribution type
    @FindBy(css = "question-input[question-title='Distribution type'] select")
    private WebElement distributionType;

    //service lever
    @FindBy(css = "table.lg-table.service-level tr:nth-child(2) input-field input")
    private List<WebElement> firstServiceLeverInput;
    @FindBy(css = "table.lg-table.service-level tr:nth-child(3) input-field input")
    private List<WebElement> secondServiceLeverInput;
    @FindBy(css = "table.lg-table.service-level tr")
    private List<WebElement> serviceLeverNum; //size-1
    @FindBy(css = "table.lg-table.service-level td.add-button>lg-button[label='+ Add']>button")
    private WebElement addButtonForServiceLever;
    @FindBy(css = "table.lg-table.service-level td>div>span.remove")
    private WebElement removeButtonForServiceLever;

    //GM holiday part
    @FindBy(css = "div.table-wrapper.ng-scope>table.lg-table tr:nth-child(1)>th:nth-child(1)")
    private WebElement gMHolidayDate;
    @FindBy(css = "div.table-wrapper.ng-scope td.add-button>lg-button>button")
    private WebElement addButtonForDistribution;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-child(2) input-field[placeholder='Select...']")
    private WebElement dateSelect;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-child(2) input-field[placeholder='Search']>ng-form>input")
    private WebElement dateSearch;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-child(2) div.lg-search-options__scroller>div>div")
    private WebElement dateSearchResult;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-child(2)>td:nth-child(2) input")
    private WebElement firstHourInput;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-last-child(2) span.remove.ng-binding")
    private WebElement lastRemoveButton;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-child(3) input-field[placeholder='Select...']")
    private WebElement dateSelect2;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-child(3) input-field[placeholder='Search']>ng-form>input")
    private WebElement dateSearch2;
    @FindBy(css = "div.table-wrapper.ng-scope tr:nth-child(3) div.lg-search-options__scroller>div>div")
    private WebElement dateSearchResult2;
    @FindBy(css = "div.error-msg.ng-binding")
    private WebElement errorMes;
    @FindBy(css = "table.lg-table.service-level tr:nth-child(2)>td:nth-child(1) input")
    private WebElement serviceLever0;
    @FindBy(css = "div.lg-search-options__scroller>div>div")
    private List<WebElement> holidayList;

    //Fixed days
    @FindBy(css = "question-input[question-title='Distribution type'] select")
    private WebElement distributionTypeSelect;
    @FindBy(css = "div.eligibility-rule.lg-question-input")
    private WebElement eligibilityRuleText;
    @FindBy(css = "div.eligibility-rule.lg-question-input>input-field:first-of-type input")
    private WebElement eligibilityHours;
    @FindBy(css = "div.eligibility-rule.lg-question-input>input-field:last-of-type input")
    private WebElement eligibilityDays;
    @FindBy(css = "div.dt-container>div.table-wrapper.ng-scope tr:nth-child(1)>th:nth-child(1)")
    private WebElement fixedDaysLabel;
    @FindBy(css = "div.dt-container>div.table-wrapper.ng-scope tr:nth-child(2)>td:nth-child(1) input")
    private WebElement fixedHours;
    @FindBy(css = "div.dt-container>div.table-wrapper.ng-scope tr:nth-child(2)>td:nth-child(2) input")
    private WebElement accruedHours;

    //payable hours
    @FindBy(css = "question-input[question-title='Payable hour types included in calculation'] h3")
    private WebElement payableHoursTitle;
    @FindBy(css = "question-input[question-title='Payable hour types included in calculation'] lg-button[label='Configure']>button")
    private WebElement payableConfigBtn;
    @FindBy(css = "modal[modal-title='Include Hour Types']>div>h1")
    private WebElement includeHourTypesModalTitle;
    @FindBy(css = "lg-button[label='Add More']>button")
    private WebElement addMoreBtn;
    @FindBy(css = "div.payable-hour-types lg-select")
    private WebElement hoursTypeSelect;
    @FindBy(css = "div.lg-search-options>div>div")
    private List<WebElement> hoursTypeOptions;
    @FindBy(css = "div.select-list-item.ng-scope>input-field")
    private List<WebElement> timeOffOptions;
    @FindBy(css = "div.hour-type-row.ng-scope>lg-cascade-select+i")
    private List<WebElement> removeBtn;
    @FindBy(css = "div.hour-type-row")
    private List<WebElement> hourTypeRows;

    //Scheduled hours
    @FindBy(css = "lg-service-level-distribution>div>div:nth-child(3) tr>th:nth-child(2)")
    private WebElement scheduledHourUnit;
    @FindBy(css = "lg-service-level-distribution>div>div:nth-child(3) tr:nth-child(2)>td:nth-child(1) input")
    private WebElement scheduledHour;
    @FindBy(css = "lg-service-level-distribution>div>div:nth-child(3) tr:nth-child(2)>td:nth-child(2) input")
    private WebElement accrualNum;

    //submit
    @FindBy(css = "lg-button[label='Cancel']>button")
    private WebElement cancelButton;
    @FindBy(css = "lg-button[label='Save']>button")
    private WebElement saveButton;

    //back
    public void back() {
        backButton.click();
    }

    //bread crumbs
    public String getTemplateName() {
        return templateName.getText();
    }

    public String getTimeOffReasonName() {
        return reasonName.getText();
    }

    // section label
    public String getRequestRulesSectionLabel() {
        return requestRulesSectionLabel.getText();
    }

    public String getAccrualDistributionSectionLabel() {
        return accrualDistributionSectionLabel.getText();
    }

    public Boolean isAccrualDistributionLabelDisplayed() {
        try {
            scrollToElement(accrualDistributionSectionLabel);
            return accrualDistributionSectionLabel.isDisplayed();
        } catch (Exception NoSuchElementException) {
            return false;
        }
    }

    //Time off request rules method
    public ArrayList<String> getRequestRuleLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        for (WebElement e : requestRuleLabels) {
            labels.add(e.getText());
        }
        return labels;
    }

    public void setTimeOffRequestRuleAs(String ruleLabel, Boolean yesOrNo) {
        WebElement yesButton = null;
        WebElement noButton = null;
        if (canRequestLabel.getText().equals(ruleLabel)) {
            yesButton = yesToCanRequest;
            noButton = noToCanRequest;
        } else if (partialDayLabel.getText().equals(ruleLabel)) {
            yesButton = yesToPartialDay;
            noButton = noToPartialDay;
//        } else if (canSubmitInTimesheetLabel.getText().equals(ruleLabel)) {
//            yesButton = yesToSubmitInTimesheet;
//            noButton = noToSubmitInTimesheet;
        } else if (autoRejectLabel.getText().equals(ruleLabel)) {
            yesButton = yesToAutoReject;
            noButton = noToAutoReject;
        } else if (toComputeOverTimeLabel.getText().equals(ruleLabel)) {
            yesButton = yesToComputeOverTime;
            noButton = noToComputeOverTime;
        } else if (trackAccrualLabel.getText().equals(ruleLabel)) {
            yesButton = yesToTrackAccrual;
            noButton = noToTrackAccrual;
        } else if (enforceYearlyLimitsLabel.getText().equals(ruleLabel)) {
            yesButton = yesToEnforceYearlyLimits;
            noButton = noToEnforceYearlyLimits;
        } else {
            throw new IllegalStateException("Unexpected value: " + ruleLabel);
        }
        if (yesOrNo) {
            scrollToElement(yesButton);
            yesButton.click();
        } else {
            scrollToElement(noButton);
            noButton.click();
        }
    }

    public Boolean isTimeOffRuleToggleTurnOn(String ruleLabel) {
        WebElement yesButton = null;
        String toggleOn = "rgba(244, 246, 248, 1)";
        if (canRequestLabel.getText().equals(ruleLabel)) {
            yesButton = yesToCanRequest;
        } else if (partialDayLabel.getText().equals(ruleLabel)) {
            yesButton = yesToPartialDay;
        } else if (canSubmitInTimesheetLabel.getText().equals(ruleLabel)) {
            yesButton = yesToSubmitInTimesheet;
        } else if (autoRejectLabel.getText().equals(ruleLabel)) {
            yesButton = yesToAutoReject;
        } else if (toComputeOverTimeLabel.getText().equals(ruleLabel)) {
            yesButton = yesToComputeOverTime;
        } else if (trackAccrualLabel.getText().equals(ruleLabel)) {
            yesButton = yesToTrackAccrual;
        } else if (enforceYearlyLimitsLabel.getText().equals(ruleLabel)) {
            yesButton = yesToEnforceYearlyLimits;
        } else {
            throw new IllegalStateException("Unexpected value: " + ruleLabel);
        }
        scrollToElement(yesButton);
        String bgColorForYes = yesButton.getCssValue("background-color");
        return bgColorForYes.equals(toggleOn);
    }

    public void setValueForTimeOffRequestRules(String ruleLabel, String num) {
        WebElement element = null;
        if (weeklyLimitsLabel.getText().equals(ruleLabel)) {
            element = weeklyLimitsInput;
        } else if (requestMadeInAdvanceLabel.getText().equals(ruleLabel)) {
            element = requestMadeInAdvanceInput;
        } else if (allDayDefaultLabel.getText().equals(ruleLabel)) {
            element = allDayDefaultInput;
        } else if (daysRequestAtOneTimeLabel.getText().equals(ruleLabel)) {
            element = daysRequestAtOneTimeInput;
        } else if (maxHrsInAdvanceLabel.getText().equals(ruleLabel)) {
            element = maxHrsInAdvanceInput;
        } else if (probationLabel.getText().equals(ruleLabel)) {
            element = probationInput;
        } else if (annualUseLimitLabel.getText().equals(ruleLabel)) {
            element = annualUseLimitInput;
        } else {
            throw new IllegalStateException("Unexpected value: " + ruleLabel);
        }
        scrollToElement(element);
        element.clear();
        element.sendKeys(num);
    }

    public String getNumSetForTimeOffRequestRules(String ruleLabel) {
        WebElement element = null;
        if (weeklyLimitsLabel.getText().equals(ruleLabel)) {
            element = weeklyLimitsInputNum;
        } else if (requestMadeInAdvanceLabel.getText().equals(ruleLabel)) {
            element = requestMadeInAdvanceInputNum;
        } else if (allDayDefaultLabel.getText().equals(ruleLabel)) {
            element = allDayDefaultInputNum;
        } else if (daysRequestAtOneTimeLabel.getText().equals(ruleLabel)) {
            element = daysRequestAtOneTimeInputNum;
        } else if (maxHrsInAdvanceLabel.getText().equals(ruleLabel)) {
            element = maxHrsInAdvanceInputNum;
        } else if (probationLabel.getText().equals(ruleLabel)) {
            element = probationInputNum;
        } else if (annualUseLimitLabel.getText().equals(ruleLabel)) {
            element = annualUseLimitInputNum;
        } else {
            throw new IllegalStateException("Unexpected value: " + ruleLabel);
        }
        scrollToElement(element);

        if (!isElementDisplayed(element)) {
            removeHidden(element);
        }
        return element.getText();
    }

    //Set time off rules
    public void setTimeOffRules(Boolean canRequest, Boolean partialDay, Boolean submitInTimesheet, String weeklyLimits, String daysInAdvance, String allDayDefault, String daysCanRequestOneTime, Boolean autoReject, Boolean overTime, Boolean trackAccrual, String maxHorsInAdvance, Boolean enforceYearlyLimits, String probationPeriod, String probationUnit, String annualUseLimit) {
        setTimeOffRequestRuleAs("Employee can request?", canRequest);
        setTimeOffRequestRuleAs("Employee can request partial day?", partialDay);
        //setTimeOffRequestRuleAs("Is this considered a paid time off ?", submitInTimesheet);
        setValueForTimeOffRequestRules("Weekly limits (hours)", weeklyLimits);
        setValueForTimeOffRequestRules("Days request must be made in advance", daysInAdvance);
        setValueForTimeOffRequestRules("Configure all day time off default", allDayDefault);
        setValueForTimeOffRequestRules("Days an employee can request at one time", daysCanRequestOneTime);
        setTimeOffRequestRuleAs("Auto reject time off that exceeds accrued hours ?", autoReject);
        setTimeOffRequestRuleAs("Allow Paid Time Off to compute to overtime ?", overTime);
        setTimeOffRequestRuleAs("Does this time off reason track Accruals ?", trackAccrual);
        setValueForTimeOffRequestRules("Max hours in advance of what you earn", maxHorsInAdvance);
        setTimeOffRequestRuleAs("Enforce Yearly Limits", enforceYearlyLimits);
        setValueForTimeOffRequestRules("Probation Period", probationPeriod);
        setProbationUnitAs(probationUnit);
        setValueForTimeOffRequestRules("Annual Use Limit", annualUseLimit);
    }

    public ArrayList<String> getProbationPeriodUnitOptions() {
        ArrayList<String> opt = new ArrayList<String>();
        Select sel = new Select(probationSelect);
        sel.getOptions().forEach(element -> {
            opt.add(element.getText());
        });
        return opt;
    }

    public void setProbationUnitAsMonths() {
        Select sel = new Select(probationSelect);
        sel.selectByIndex(1);
    }

    public void setProbationUnitAs(String method) {//Days, Months, Hours Worked
        Select sel = new Select(probationSelect);
        if (method.equalsIgnoreCase("Days")) {
            sel.selectByVisibleText("Days");
        } else if (method.equalsIgnoreCase("Months")) {
            sel.selectByVisibleText("Months");
        } else if (method.equalsIgnoreCase("Hours Worked")) {
            sel.selectByVisibleText("Hours Worked");
        }
    }

    public boolean isTimeOffConfigurationReadOnly() {
        if (weeklyLimitsInput.isEnabled()) {
            return false;
        } else {
            return true;
        }
    }

    //Accrual engine part
    public ArrayList<String> getAccrualStartOptions() {
        return getSelectOptions(accrualStartDate.get(0));
    }

    public ArrayList<String> getAccrualEndOptions() {
        return getSelectOptions(accrualEndDate.get(0));
    }

    public void setAccrualPeriod(String startDateType, String endDateType, String sMonth, String sDate, String eMonth, String eDate) {
        scrollToBottom();
        Select startType = new Select(accrualStartDate.get(0));
        startType.selectByVisibleText(startDateType);//"Hire Date","Specified Date"
        if (startDateType.contains("Specified")) {
            Select monSelect = new Select(accrualStartDate.get(1));
            monSelect.selectByVisibleText(sMonth);
            Select dateSelect = new Select(accrualStartDate.get(2));
            dateSelect.selectByVisibleText(sDate);
        }
        Select endType = new Select(accrualEndDate.get(0));
        endType.selectByVisibleText(endDateType);//"","Hire Date","Specified Date"
        if (endDateType.contains("Specified")) {
            Select monSelect = new Select(accrualEndDate.get(1));
            monSelect.selectByVisibleText(eMonth);
            Select dateSelect = new Select(accrualEndDate.get(2));
            dateSelect.selectByVisibleText(eDate);
        }
    }

    public void setAllowanceInDays(String allowanceType, String days) {
        Select typeSelect = new Select(allowanceInSelect);
        typeSelect.selectByVisibleText(allowanceType);
        allowanceInPut.clear();
        allowanceInPut.sendKeys(days);
    }

    public void setReinstatementMonth(String reinMonth) {
        reinstatementMonth.clear();
        reinstatementMonth.sendKeys(reinMonth);
    }

    public ArrayList<String> getDistributionOptions() {
        return getSelectOptions(distributionMethods);
    }

    public void setDistributionMethod(String distributionMethod) {
        scrollToBottom();
        Select select = new Select(distributionMethods);
        select.selectByVisibleText(distributionMethod);
    }

    public void setAnnualEarn(List<WebElement> serviceLeverInput, String annualEarn) {
        serviceLeverInput.get(1).clear();
        serviceLeverInput.get(1).sendKeys(annualEarn);
    }

    public void setMaxCarryover(List<WebElement> serviceLeverInput, String maxCarryover) {
        serviceLeverInput.get(2).clear();
        serviceLeverInput.get(2).sendKeys(maxCarryover);
    }

    public void setMaxAvailableHours(List<WebElement> serviceLeverInput, String maxAvailableHours) {
        serviceLeverInput.get(3).clear();
        serviceLeverInput.get(3).sendKeys(maxAvailableHours);
    }

    @FindBy(css = "option[label = 'Hire Date']")
    private WebElement hireDate;
    @FindBy(css = "option[label = 'Specified Date']")
    private WebElement specifyDate;
    @FindBy(css = "div:nth-child(2) > lg-property-meta-field > div > div > question-input > div > div.lg-question-input__wrapper > ng-transclude > input-field")
    private WebElement endDate;

    public void addSpecifiedServiceLever(int serviceLever, String annualEarn, String maxCarryover, String maxAvailableHours) {
        scrollToElement(endDate);
        highlightElement(endDate);
        click(endDate);
        click(hireDate);
        scrollToElement(addButtonForServiceLever);
        addButtonForServiceLever.click();
        List<WebElement> serviceLeverInput = null;
        if (serviceLever == 0) {
            serviceLeverInput = firstServiceLeverInput;
        } else if (serviceLever == 2) {
            serviceLeverInput = secondServiceLeverInput;
            serviceLeverInput.get(0).clear();
            serviceLeverInput.get(0).sendKeys("2");
        } else {
            System.out.println("Need to add new serviceLeverInput locator!");
        }
        setAnnualEarn(serviceLeverInput, annualEarn);
        setMaxCarryover(serviceLeverInput, maxCarryover);
        setMaxAvailableHours(serviceLeverInput, maxAvailableHours);
    }

    public int getServiceLeverNum() {
        return serviceLeverNum.size() - 1;
    }

    public void removeServiceLever() {
        scrollToElement(removeButtonForServiceLever);
        removeButtonForServiceLever.click();
    }

    public ArrayList<String> getSelectOptions(WebElement element) {
        ArrayList<String> options = new ArrayList<String>();
        scrollToBottom();
        Select select = new Select(element);
        select.getOptions().forEach((e) -> {
            options.add(e.getText());
        });
        return options;
    }

    public void saveTimeOffConfiguration(boolean tureOrFalse) {
        scrollToBottom();
        if (tureOrFalse) {
            saveButton.click();
        } else {
            cancelButton.click();
        }
        waitForSeconds(5);
    }

    public String getDistributionType(){
        return gMHolidayDate.getText();
    }

    public void setDateDistribution(String holiday, String hour){
        dateSearch.clear();
        dateSearch.sendKeys(holiday);
        dateSearchResult.click();
        firstHourInput.clear();
        firstHourInput.sendKeys(hour);
    }

    public void removeHolidayFromTheDistribution(){
        scrollToBottom();
        waitForSeconds(3);
        lastRemoveButton.click();
    }

    public boolean verifyHolidayUsedCanNotBeSelectedAgain(String holiday){
        boolean canNotSelect=false;
        addButtonForDistribution.click();
        dateSelect2.click();
        dateSearch2.clear();
        dateSearch2.sendKeys(holiday);
        try {
            dateSearchResult2.click();
        } catch (ElementNotInteractableException exception) {
            canNotSelect=true;
        } ;
        return canNotSelect;
    }

    public String getErrorMessage(){
        return errorMes.getText();
    }

    public void showDistributionOfSpecifiedServiceLever0(){
        serviceLever0.click();
    }

    public ArrayList<String> getHolidayList() {
        dateSelect.click();
        return getWebElementsText(holidayList);
    }

    public String getEligibilityTitle() {
        scrollToElement(eligibilityRuleText);
        waitForSeconds(2);
        return eligibilityRuleText.getText();
    }

    public void setEligibilityRule(String hrs, String Days) {
        eligibilityHours.clear();
        eligibilityHours.sendKeys(hrs);
        eligibilityDays.clear();
        eligibilityDays.sendKeys(Days);
    }

    public ArrayList<String> getWorkedHoursDistributionTypeOptions(){
        Select disType = new Select(distributionType);
        List<WebElement> opt=disType.getOptions();
        return getWebElementsText(opt);
    }

    public void setDistributionType(String disTy) {//Total Hours, Rate, Fixed Days
        Select disType = new Select(distributionType);
        disType.selectByVisibleText(disTy);
    }

    public void setFixedDaysDistribution(String workedHrs, String accrualHrs) {
        fixedHours.clear();
        fixedHours.sendKeys(workedHrs);
        accruedHours.clear();
        accruedHours.sendKeys(accrualHrs);
        waitForSeconds(2);
    }
    public String getFixedDaysLabel(){
        return fixedDaysLabel.getText();
    }

    public void getPayableTitle() throws Exception {
        if (isElementLoaded(payableHoursTitle, 5)) {
            SimpleUtils.pass("Payable hour types included in calculation");
        } else {
            SimpleUtils.fail("Failed to find Payable Hour title!!!",false);
        }
    }

    public void isPayableConfigButtonDisplayed() throws Exception {
        if (isElementLoaded(payableConfigBtn, 5)) {
            SimpleUtils.pass("Configure button was displayed");
        } else {
            SimpleUtils.fail("Failed to assert the configure button was displayed!!!",false);
        }
    }

    public void configurePayableHours() {
        if (payableConfigBtn.isDisplayed()) {
            payableConfigBtn.click();
            SimpleUtils.pass("payableConfig button was displayed");
        } else {
            SimpleUtils.fail("Payable hours configure button wasn't displayed!",false);
        }
    }

    public String getPayableModalTitle() {
        return includeHourTypesModalTitle.getText();
    }

    public ArrayList<String> getHoursTypeOptions() {
        hoursTypeSelect.click();
        return getWebElementsText(hoursTypeOptions);
    }

    public void removeTheExistingHourType(){
        int num=removeBtn.size();
        for(int i=0; i<num; i++){
            removeBtn.get(0).click();
        }
    }

    public void addHourTypes(String hourType, String subType1, String subType2) {
        addMore();
        WebElement theLastHourType = hourTypeRows.get(hourTypeRows.size() - 1);
        WebElement theLastSelect = theLastHourType.findElement(By.cssSelector("lg-cascade-select>lg-select"));
        theLastSelect.click();
        for (WebElement ty : hoursTypeOptions
        ) {
            if (ty.getText().equals(hourType)) {
                ty.click();
            }
        }
        boolean isThereSubSelect = true;
        try {
            theLastHourType.findElement(By.cssSelector("lg-cascade-select>lg-cascade-select"));
        } catch (Exception NoSuchElementException) {
            isThereSubSelect = false;
        }
        if (isThereSubSelect) {
            WebElement subSelect = theLastHourType.findElement(By.cssSelector("lg-cascade-select>lg-cascade-select lg-picker-input"));
            subSelect.click();
            switch (hourType) {
                case "Time Off Type":
                    WebElement searchInput = subSelect.findElement(By.cssSelector("lg-search input"));
                    if (subType1 != null) {
                        searchInput.clear();
                        searchInput.sendKeys(subType1);
                        subSelect.findElement(By.cssSelector("div.select-list-item ng-form")).click();
                    }
                    if (subType2 != null) {
                        searchInput.clear();
                        searchInput.sendKeys(subType2);
                        subSelect.findElement(By.cssSelector("div.select-list-item ng-form")).click();
                    }
                    break; //可选
                case "Holiday":
                    if (subType1 != null && subType1.equals("Fixed Hours")) {
                        subSelect.findElement(By.cssSelector("div[title='Fixed Hours']")).click();
                    }
                    if (subType2 != null && subType2.equals("Worked Hours")) {
                        subSelect.findElement(By.cssSelector("input-field[label='Worked Hours']>ng-form")).click();
                    }
                    break; //可选
                case "Other Pay Type":
                    if (subType1 != null && subType1.equals("OtherPay1")) {
                        subSelect.findElement(By.cssSelector("input-field[label='OtherPay1']>ng-form")).click();
                    }
                    if (subType2 != null && subType2.equals("OtherPay2")) {
                        subSelect.findElement(By.cssSelector("input-field[label='OtherPay2']>ng-form")).click();
                    }
                    break; //可选
                case "Compliance":
                    if (subType1 != null && subType1.equals("Doubletime")) {
                        subSelect.findElement(By.cssSelector("input-field[label='Doubletime']>ng-form")).click();
                    }
                    if (subType2 != null && subType2.equals("Overtime")) {
                        subSelect.findElement(By.cssSelector("input-field[label='Overtime']>ng-form")).click();
                    }
                    break;
            }
        }
    }

    public ArrayList<String> getTimeOffOptions(){
        ArrayList<String> timeOffLabels=new ArrayList<>();
        for (WebElement timeOff:timeOffOptions
             ) {
            timeOffLabels.add(timeOff.getAttribute("label"));
        }
        return timeOffLabels;
    }

    public void isAddMoreButtonDisplayed() throws Exception {
        if (isElementLoaded(addMoreBtn, 5)) {
            SimpleUtils.pass("addMore button was displayed");
        } else {
            SimpleUtils.fail("addMore button was not displayed",false);
        }
    }

    public void addMore() {
        addMoreBtn.click();
    }

    public String getScheduledHourUnit() {
        String unit = null;
        if (isElementDisplayed(scheduledHourUnit)) {
            unit = scheduledHourUnit.getText();
        } else {
            System.out.println("Scheduled hours Unit didn't display!!!");
        }
        return unit;
    }

    public void setScheduledHourRule(String scheduledHrs, String rate) {
        scheduledHour.clear();
        scheduledHour.sendKeys(scheduledHrs);
        accrualNum.clear();
        accrualNum.sendKeys(rate);
    }
}
