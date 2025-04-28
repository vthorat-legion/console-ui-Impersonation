package com.legion.pages.core.schedule;

import com.legion.pages.BasePage;
import com.legion.pages.ToggleSummaryPage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleToggleSummaryPage extends BasePage implements ToggleSummaryPage {
    public ConsoleToggleSummaryPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(css = "tr[ng-repeat='day in summary.workingHours'] td:nth-child(1)")
    private List<WebElement> operatingHoursScheduleDay;

    @FindBy(css = "tr[ng-repeat='day in summary.workingHours'] td.text-right.ng-binding")
    private List<WebElement> scheduleOperatingHrsTimeDuration;

    @FindBy(css = "tr[ng-repeat=\"day in summary.workingHours\"]")
    private List<WebElement> operatingHoursRows;
    public HashMap<String, String> getOperatingHrsValue(String day) throws Exception {
        String currentDay = null;
        String nextDay = null;
        String finalDay = null;
        HashMap<String, String>  activeDayAndOperatingHrs = new HashMap<>();

        if(areListElementVisible(operatingHoursRows,5) &&
                areListElementVisible(operatingHoursScheduleDay,5) &&
                areListElementVisible(scheduleOperatingHrsTimeDuration,5)){
            for(int i =0; i<operatingHoursRows.size();i++){
                if(i == operatingHoursRows.size()-1){
                    if(operatingHoursScheduleDay.get(i).getText().substring(0,3).equalsIgnoreCase(day)){
                        currentDay = day;
                        nextDay = operatingHoursScheduleDay.get(0).getText().substring(0,3);
                        activeDayAndOperatingHrs.put("ScheduleOperatingHrs" , currentDay + "-" + scheduleOperatingHrsTimeDuration.get(i).getText());
                        SimpleUtils.pass("Current day in Schedule " + day + " matches" +
                                " with Operation hours Table " + operatingHoursScheduleDay.get(i).getText().substring(0,3));
                        break;
                    }else{
                        SimpleUtils.fail("Current day in Schedule " + day + " does not match" +
                                " with Operation hours Table " + operatingHoursScheduleDay.get(i).getText().substring(0,3),false);
                    }
                }else if(operatingHoursScheduleDay.get(i).getText().substring(0,3).equalsIgnoreCase(day)){
                    currentDay = day;
                    nextDay = operatingHoursScheduleDay.get(i+1).getText().substring(0,3);
                    activeDayAndOperatingHrs.put("ScheduleOperatingHrs" , currentDay + "-" + scheduleOperatingHrsTimeDuration.get(i).getText());
                    SimpleUtils.pass("Current day in Schedule " + day + " matches" +
                            " with Operation hours Table " + operatingHoursScheduleDay.get(i).getText().substring(0,3));
                    break;
                }

            }
        }else{
            SimpleUtils.fail("Operating hours table not loaded Successfully",false);
        }
        return activeDayAndOperatingHrs;
    }


    @Override
    public String getTheEarliestAndLatestTimeInSummaryView(HashMap<String, Integer> schedulePoliciesBufferHours) throws Exception {
        String day = null;
        String shiftStartTime = null;
        String shiftEndTime = null;
        double shiftStartTimeDouble = 12.0;
        double shiftEndTimeDouble = 0.0;
        HashMap<String, String> activeDayAndOperatingHrs = new HashMap<>();
        if (areListElementVisible(operatingHoursRows, 5) &&
                areListElementVisible(operatingHoursScheduleDay, 5) &&
                areListElementVisible(scheduleOperatingHrsTimeDuration, 5)) {
            for (int i = 0; i < operatingHoursRows.size(); i++) {
                if (scheduleOperatingHrsTimeDuration.get(i).getText().contains("Closed"))
                    continue;
                day = operatingHoursScheduleDay.get(i).getText().substring(0, 3);
                activeDayAndOperatingHrs = getOperatingHrsValue(day);
                shiftStartTime = (activeDayAndOperatingHrs.get("ScheduleOperatingHrs").split("-"))[1];
                if (shiftStartTime.toLowerCase().endsWith("pm"))
                    continue;
                shiftEndTime = (activeDayAndOperatingHrs.get("ScheduleOperatingHrs").split("-"))[2];
                if (shiftStartTime.contains(":"))
                    shiftStartTime = shiftStartTime.replace(":", ".");
                if (shiftEndTime.contains(":"))
                    shiftEndTime = shiftEndTime.replace(":", ".");
                if (shiftStartTime.toLowerCase().contains("am") && shiftStartTime.startsWith("12"))
                    shiftStartTime = shiftStartTime.replace("12", "0").replaceAll("[a-zA-Z]", "");
                if (shiftStartTime.toLowerCase().contains("am") && !shiftStartTime.startsWith("12"))
                    shiftStartTime = shiftStartTime.replaceAll("[a-zA-Z]", "");
                if (shiftStartTime.toLowerCase().contains("pm") && shiftStartTime.startsWith("12"))
                    shiftStartTime = shiftStartTime.replaceAll("[a-zA-Z]", "");
                if (shiftStartTime.toLowerCase().contains("pm") && !shiftStartTime.startsWith("12"))
                    shiftStartTime = String.valueOf(Double.valueOf(shiftEndTime.replaceAll("[a-zA-Z]", "")) + 12);
                if (shiftEndTime.toLowerCase().contains("am"))
                    shiftEndTime = shiftEndTime.replaceAll("[a-zA-Z]", "");
                if (shiftEndTime.toLowerCase().contains("pm") && !shiftEndTime.startsWith("12"))
                    shiftEndTime = String.valueOf(Double.valueOf(shiftEndTime.replaceAll("[a-zA-Z]", "")) + 12);
                if (shiftStartTimeDouble > Double.valueOf(shiftStartTime))
                    shiftStartTimeDouble = Double.valueOf(shiftStartTime);
                if (shiftEndTimeDouble < Double.valueOf(shiftEndTime))
                    shiftEndTimeDouble = Double.valueOf(shiftEndTime);
            }
        } else {
            SimpleUtils.fail("Operating hours table not loaded Successfully", true);
        }
        return Integer.valueOf(((int) shiftStartTimeDouble) - schedulePoliciesBufferHours.get("openingBufferHours")).toString() + "-" +
                Integer.valueOf(((int) shiftEndTimeDouble) + schedulePoliciesBufferHours.get("closingBufferHours")).toString();
    }

    @FindBy(css = "lg-dropdown-menu[actions=\"moreActions\"]")
    private WebElement scheduleAdminDropDownBtn;

    @FindBy(css = "div[ng-repeat=\"action in actions\"]")
    private List<WebElement> scheduleAdminDropDownOptions;
    @Override
    public void toggleSummaryView() throws Exception {
        String toggleSummaryViewOptionText = "Toggle Summary View";
        if (isElementLoaded(scheduleAdminDropDownBtn, 10)) {
            click(scheduleAdminDropDownBtn);
            if (scheduleAdminDropDownOptions.size() > 0) {
                for (WebElement scheduleAdminDropDownOption : scheduleAdminDropDownOptions) {
                    if (scheduleAdminDropDownOption.getText().toLowerCase().contains(toggleSummaryViewOptionText.toLowerCase())) {
                        click(scheduleAdminDropDownOption);
                    }
                }
            } else
                SimpleUtils.fail("Schedule Page: Admin dropdown Options not loaded to Toggle Summary View for the Week : '"
                        + getActiveWeekText() + "'.", false);
        } else
            SimpleUtils.fail("Schedule Page: Admin dropdown not loaded to Toggle Summary View for the Week : '"
                    + getActiveWeekText() + "'.", false);
    }

    @FindBy(css = "div[ng-if=\"showSummaryView\"]")
    private WebElement summaryViewDiv;

    @Override
    public boolean isSummaryViewLoaded() throws Exception {
        if (isElementLoaded(summaryViewDiv))
            return true;
        return false;
    }

    // Added by Nora: For non dg flow create schedule
    @FindBy (className = "generate-modal-subheader-title")
    private WebElement generateModalTitle;
    @FindBy (css = "[ng-click=\"next()\"]")
    private WebElement nextButtonOnCreateSchedule;
    @FindBy (className = "generate-modal-week-container")
    private List<WebElement> availableCopyWeeks;
    @FindBy (css = "generate-modal-operating-hours-step [label=\"Edit\"]")
    private WebElement operatingHoursEditBtn;
    @FindBy (css = ".operating-hours-day-list-item.ng-scope")
    private List<WebElement> operatingHoursDayLists;
    @FindBy (css = "generate-modal-budget-step [label=\"Edit\"]")
    private WebElement editBudgetBtn;
    @FindBy (css = "generate-modal-budget-step [ng-repeat=\"r in summary.staffingGuidance.roleHours\"]")
    private List<WebElement> roleHoursRows;
    @FindBy (className = "sch-calendar-day-dimension")
    private List<WebElement> weekDayDimensions;
    @FindBy (css = "tbody tr")
    private List<WebElement> smartCardRows;
    @FindBy (css = ".generate-modal-week")
    private List<WebElement> createModalWeeks;
    @FindBy (css = ".holiday-text")
    private WebElement storeClosedText;
    @FindBy (css = "[ng-repeat*=\"summary.workingHours\"]")
    private List<WebElement> summaryWorkingHoursRows;
    @FindBy (css = "span.loading-icon.ng-scope")
    private WebElement loadingIcon;
    @FindBy (css = ".operating-hours-day-list-item.ng-scope")
    private List<WebElement> currentOperatingHours;


    @Override
    public void verifyClosedDaysInToggleSummaryView(List<String> weekDaysToClose) throws Exception {
        if (areListElementVisible(summaryWorkingHoursRows, 15) && summaryWorkingHoursRows.size() == 7) {
            for (WebElement row : summaryWorkingHoursRows) {
                List<WebElement> tds = row.findElements(By.tagName("td"));
                if (tds != null && tds.size() == 2) {
                    if (weekDaysToClose.contains(tds.get(0).getText())) {
                        if (tds.get(1).getText().equals("Closed")) {
                            SimpleUtils.pass("Verfied " + tds.get(0).getText() + " is \"Closed\"");
                        } else {
                            SimpleUtils.fail("Verified " + tds.get(0).getText() + " is not \"Closed\"", false);
                        }
                    }
                } else {
                    SimpleUtils.fail("Summary Operating Hours: Failed to find two td elements!", false);
                }
            }
        } else {
            SimpleUtils.fail("Summary Operating Hours rows not loaded Successfully!", false);
        }
    }

    @FindBy(css = "[ng-if=\"isGenerateOverview()\"] h1")
    private WebElement weekInfoBeforeCreateSchedule;
    @Override
    public String getWeekInfoBeforeCreateSchedule() throws Exception {
        String weekInfo = "";
        if (isElementLoaded(weekInfoBeforeCreateSchedule,10)){
            weekInfo = weekInfoBeforeCreateSchedule.getText().trim();
            if (weekInfo.contains("Week")) {
                weekInfo = weekInfo.substring(weekInfo.indexOf("Week"));
            }
        }
        return weekInfo;
    }

    @FindBy (css = ".text-right[ng-if=\"hasBudget\"]")
    private List<WebElement> budgetedHoursOnSTAFF;

    @FindBy (xpath = "//div[contains(text(), \"Weekly Budget\")]/following-sibling::h1[1]")
    private WebElement budgetHoursOnWeeklyBudget;
    @Override
    public List<String> getBudgetedHoursOnSTAFF() throws Exception {
        List<String> budgetedHours = new ArrayList<>();
        if (areListElementVisible(budgetedHoursOnSTAFF,10)) {
            for (WebElement e : budgetedHoursOnSTAFF) {
                budgetedHours.add(e.getText().trim());
            }
        } else
            SimpleUtils.fail("Budgeted Hours On STAFF failed to load",true);
        return budgetedHours;
    }

    @Override
    public String getBudgetOnWeeklyBudget() throws Exception {
        String budgetOnWeeklyBudget = "";
        if (budgetHoursOnWeeklyBudget.getText().contains(" ")) {
            budgetOnWeeklyBudget = budgetHoursOnWeeklyBudget.getText().split(" ")[0];
        }
        return budgetOnWeeklyBudget;
    }


    @FindBy(css = ".generate-schedule-staffing tr:not([ng-repeat]) th[class=\"text-right ng-binding\"]")
    private WebElement staffingGuidanceHrs;
    @Override
    public float getStaffingGuidanceHrs() throws Exception {
        float staffingGuidanceHours = 0;
        if (isElementLoaded(staffingGuidanceHrs,20) && SimpleUtils.isNumeric(staffingGuidanceHrs.getText().replace("\n",""))){
            staffingGuidanceHours = Float.parseFloat(staffingGuidanceHrs.getText().replace("\n",""));
        } else {
            SimpleUtils.fail("There is no Staffing guidance hours!", false);
        }
        return staffingGuidanceHours;
    }


    @FindBy(css = ".schedule-summary-search-dropdown [icon*=\"search.svg'\"]")
    private WebElement searchLocationBtn;
    @FindBy(css = "input[placeholder=\"Search Locations\"]")
    private WebElement searchLocationInput;
    @FindBy(css = ".assignment-shift-search-box .lg-search-options__subLabel")
    private List<WebElement> childLocations;

    @Override
    public boolean isLocationGroup() {
        try {
            if (isElementLoaded(searchLocationBtn, 10)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getChildLocationList() throws Exception {
        List<String> childLocationList = new ArrayList<>();
        if (isLocationGroup()) {
            clickTheElement(searchLocationBtn);
            if (isElementLoaded(searchLocationInput, 3)) {
                clickTheElement(searchLocationInput);
                if (areListElementVisible(childLocations, 3)) {
                    for (WebElement child : childLocations) {
                        childLocationList.add(child.getText());
                    }
                }
            }
        }
        return childLocationList;
    }

    //added by Haya
    @FindBy (css = "div.generate-schedule-stats")
    private WebElement scheduleSummary;
    @Override
    public void verifyOperatingHrsInToggleSummary(String day, String startTime, String endTime) throws Exception {
        if (isElementLoaded(scheduleSummary) && isElementLoaded(scheduleSummary.findElement(By.cssSelector("div[ng-class=\"hideItem('projected.sales')\"] table")))){
            List<WebElement> dayInSummary = scheduleSummary.findElements(By.cssSelector("div[ng-class=\"hideItem('projected.sales')\"] tr[ng-repeat=\"day in summary.workingHours\"]"));
            for (WebElement e : dayInSummary){
                if (e.getText().contains(day) && e.getText().contains(getTimeFormat(startTime)) && e.getText().contains(getTimeFormat(endTime))){
                    SimpleUtils.pass("Operating Hours is consistent with setting!");
                }
            }
        } else {
            SimpleUtils.fail("schedule summary fail to load!", false);
        }
    }


    //added by Haya. 09:00AM-->9am
    private String getTimeFormat(String time) throws Exception{
        String result = time.substring(0,2);
        if (time.contains("AM") | time.contains("am")){
            result = result.concat("am");
        } else {
            result = result.concat("pm");
        }
        if (result.indexOf("0")==0){
            result = result.substring(1);
        }
        return result;
    }

    @FindBy(css = "[ng-class=\"hideItem('projected.sales')\"]")
    private WebElement operatingHoursSection;
    @FindBy(css = "[ng-class=\"hideItem('staffing.guidance')\"]")
    private WebElement staffSection;
    @FindBy(css = "[ng-class=\"hideItem('roster')\"]")
    private WebElement rosterUpdatesSection;
    @FindBy(css = "lg-button.edit-operating-hours-link")
    private WebElement editOperatingHoursBtn;

    public void verifyTheContentInRosterUpdatesSection() throws Exception {
        String title = "ROSTER UPDATES";
        String noChanges = "No changes";
        WebElement titleElement =
                rosterUpdatesSection.findElement(By.tagName("h3"));
        WebElement noChangesElements =
                rosterUpdatesSection.findElement(By.cssSelector("div.text-muted"));
        if (isElementLoaded(rosterUpdatesSection, 5)){
            /*
            The content should be:
            - Roster Updates
            - No changes or the tm who is leaving
             */
            SimpleUtils.assertOnFail("The expected is:"+title
                            +" the actual is: "+titleElement.getText(),
                    titleElement.getText().equalsIgnoreCase(title), false);
            SimpleUtils.assertOnFail("The expected is:"+noChanges
                            +" the actual is: "+noChangesElements.getText(),
                    noChangesElements.getText().equalsIgnoreCase(noChanges), false);
        } else
            SimpleUtils.fail("The operating hours section fail to load！ ", false);

    }


    public void verifyTheContentInStaffSection() throws Exception {
        String title = "STAFF";
        String hoursHeader1 = "Forecast Hours Budget Hours";
        String hoursHeader2 = "Forecast Hours";
        String hoursHeader = "";
        WebElement titleElement =
                staffSection.findElement(By.tagName("h3"));
        List<WebElement> hoursHeaderElements =
                staffSection.findElements(By.className("schedule-summary-th-grey"));
        if (isElementLoaded(staffSection, 5)){
            /*
            The content should be:
            - STAFF
            - Forecast Hours
            - Budget Hours
             */
            SimpleUtils.assertOnFail("The expected is:"+title
                    +" the actual is: "+titleElement.getText(),
                    titleElement.getText().equalsIgnoreCase(title), false);
            String actualHoursHeader = "";
            if (hoursHeaderElements.size()==1){
                actualHoursHeader = (hoursHeaderElements.get(0).getText()).replace("\n"," ");
                hoursHeader = hoursHeader2;
            } else{
                actualHoursHeader = (hoursHeaderElements.get(0).getText()
                        +" "+ hoursHeaderElements.get(1).getText()).replace("\n"," ");
                hoursHeader = hoursHeader1;
            }

            SimpleUtils.assertOnFail("The expected is:"+hoursHeader
                    +" the actual is: "+actualHoursHeader,
                    actualHoursHeader.equalsIgnoreCase(hoursHeader), false);
        } else
            SimpleUtils.fail("The operating hours section fail to load！ ", false);

    }


    public void verifyTheContentInOperatingHoursSection() throws Exception {
        String title = "OPERATING HOURS";
        String workingHoursHeader = "Open - Close";
        WebElement titleElement=
                operatingHoursSection.findElement(By.tagName("h3"));
        WebElement workingHoursHeaderElement =
                operatingHoursSection.findElement(By.cssSelector("tr:not([ng-repeat]) th.text-right"));
        if (isElementLoaded(operatingHoursSection, 5)){
            /*
            The content should be:
            - OPERATING HOURS
            - Open - Close
            - Seven work days
            - Starting and End time
            - Edit buttons
             */
            SimpleUtils.assertOnFail("The expected is:"+title
                            +" the actual is: "+titleElement.getText(),
                    titleElement.getText().equalsIgnoreCase(title), false);
            SimpleUtils.assertOnFail("The expected is:"+workingHoursHeader
                            +" the actual is: "+workingHoursHeaderElement.getText(),
                    workingHoursHeaderElement.getText().equalsIgnoreCase(workingHoursHeader), false);
            SimpleUtils.assertOnFail("The expected is: 7 days"
                            +" the actual is: "+ operatingHoursScheduleDay.size()+" days",
                    operatingHoursScheduleDay.size() == 7, false);
            SimpleUtils.assertOnFail("The expected is: 7 operating hours"
                            +" the actual is: "+ scheduleOperatingHrsTimeDuration.size()+" operating hours",
                    scheduleOperatingHrsTimeDuration.size() == 7, false);
            SimpleUtils.assertOnFail("The edit operating hours button should load! ",
                    isElementLoaded(editOperatingHoursBtn, 5), false);

        } else
            SimpleUtils.fail("The operating hours section fail to load！ ", false);

    }
    @FindBy(css = "div.lgn-alert-modal")
    private WebElement warningMode;

    @FindBy(css = "span.lgn-alert-message")
    private WebElement warningMessagesInWarningMode;

    @FindBy(className = "lgn-action-button-success")
    private WebElement okBtnInWarningMode;
    @Override
    public String autoFillOpenShifts() throws Exception {
        String autoFillOpenShiftsOptionText = "Auto-fill open shifts";
        String autoFillOpenShiftsMessage = "";
        if (isElementLoaded(scheduleAdminDropDownBtn, 10)) {
            click(scheduleAdminDropDownBtn);
            if (scheduleAdminDropDownOptions.size() > 0) {
                for (WebElement scheduleAdminDropDownOption : scheduleAdminDropDownOptions) {
                    if (scheduleAdminDropDownOption.getText().toLowerCase().contains(autoFillOpenShiftsOptionText.toLowerCase())) {
                        click(scheduleAdminDropDownOption);
                        if (isElementLoaded(warningMode, 10)
                                && isElementLoaded(warningMessagesInWarningMode)
                                && isElementLoaded(okBtnInWarningMode)) {
                            autoFillOpenShiftsMessage = warningMessagesInWarningMode.getText();
                        } else
                            SimpleUtils.fail("The auto fill open shift success modal fail to load! ", false);
                        clickTheElement(okBtnInWarningMode);
                    }
                }
            } else
                SimpleUtils.fail("Schedule Page: Admin dropdown Options not loaded to Auto-fill open shifts for the Week : '"
                        + getActiveWeekText() + "'.", false);
        } else
            SimpleUtils.fail("Schedule Page: Admin dropdown not loaded to Auto-fill open shifts for the Week : '"
                    + getActiveWeekText() + "'.", false);
        return autoFillOpenShiftsMessage;
    }
}
