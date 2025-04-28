package com.legion.pages.core;

import com.legion.pages.*;
import com.legion.pages.core.schedule.*;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.ro.Si;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleScheduleDMViewPage extends BasePage implements ScheduleDMViewPage {

    @FindBy(css = ".analytics-new-table-group-row-open")
    private List<WebElement>  schedulesInDMView;

    @FindBy(css = "[jj-switch-when=\"cells.CELL_BUDGET_HOURS\"]")
    private List<WebElement>  budgetHours;

    @FindBy(css = "[jj-switch-when=\"cells.CELL_PUBLISHED_HOURS\"]")
    private List<WebElement>  publishedHours;

    public ConsoleScheduleDMViewPage() {
        PageFactory.initElements(getDriver(), this);
    }


    public float getBudgetedHourOfScheduleInDMViewByLocation(String location) throws Exception
    {
        float budgetedHours = 0;
        boolean isLocationMatched = false;
        if (areListElementVisible(schedulesInDMView, 60) && schedulesInDMView.size() != 0){
            for (WebElement schedule : schedulesInDMView){
                WebElement locationInDMView = schedule.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_UNTOUCHED\"]"));
                if (locationInDMView != null){
                    String locationNameInDMView = locationInDMView.getText();
                    if (locationNameInDMView !=null && locationNameInDMView.equals(location)){
                        isLocationMatched = true;
                        if (areListElementVisible(budgetHours, 5)){
                            budgetedHours = Float.parseFloat(schedule.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_BUDGET_HOURS\"]")).getText().replace(",", ""));
                        } else
                            budgetedHours = Float.parseFloat(schedule.findElements(By.cssSelector("[class=\"ng-binding ng-scope\"]")).get(2).getText().replace("", ""));
                        break;
                    }
                } else{
                    SimpleUtils.fail("Get budgeted hours in DM View failed, there is no location display in this schedule" , false);
                }
            }
            if(!isLocationMatched)
            {
                SimpleUtils.fail("Get budgeted hours in DM View failed, there is no matched location display in DM view" , false);
            } else{
                SimpleUtils.pass("Get budgeted hours in DM View successful, the budgeted hours is '" + budgetedHours + ".");
            }
        } else{
            SimpleUtils.fail("Get budgeted hours in DM View failed, there is no schedules display in DM view" , false);
        }
        return budgetedHours;
    }

    // Added By Julie
    @FindBy(css = ".day-week-picker-period-active>span")
    private WebElement currentWeek;

    @FindBy(css = "text[text-anchor=\"middle\"][style]")
    private List<WebElement> budgetComparison;

    @FindBy(css = "[style=\"font-size: 14px;\"]")
    private WebElement hours;

    @Override
    public String getCurrentWeekInDMView() throws Exception {
        String week = "";
        if (isElementLoaded(currentWeek,5)) {
            week = currentWeek.getText();
            SimpleUtils.pass("Schedule Page: Get current week \"" + week + "\"");
        } else {
            SimpleUtils.fail("Schedule Page: Current week failed to load",false);
        }
        return week;
    }

    @Override
    public String getBudgetComparisonInDMView() throws Exception {
        String kpi = "";
        if (areListElementVisible(budgetComparison,30) && budgetComparison.size() == 2) {
            waitForSeconds(5);
            kpi = budgetComparison.get(0).getText() + " " + budgetComparison.get(1).getText();
        } else
            SimpleUtils.fail("Schedule Page: Failed to load ",false);
        return kpi;
    }

    @FindBy(css = "[ng-click=\"$ctrl.onReload(true)\"]")
    private WebElement refreshButton;

    @FindBy(css = "[ng-if=\"$ctrl.minutes >= 0 && $ctrl.date && !$ctrl.loading\"]")
    private WebElement lastUpdatedIcon;

    @Override
    public void clickOnRefreshButton() throws Exception {
        if (isElementLoaded(refreshButton, 10)) {
            clickTheElement(refreshButton);
            if(isElementLoaded(lastUpdatedIcon, 180)){
                SimpleUtils.pass("Click on Refresh button Successfully!");
            } else
                SimpleUtils.fail("Refresh timeout! ", false);
        } else {
            SimpleUtils.fail("Refresh button not Loaded!", true);
        }
    }
    @Override
    public void validateThePresenceOfRefreshButton() throws Exception {
        if (isElementLoaded(refreshButton,10)) {
            if (refreshButton.isDisplayed() && !refreshButton.getText().isEmpty() && refreshButton.getText() != null) {
                if (getDriver().findElement(By.xpath("//body//day-week-picker/following-sibling::last-updated-countdown/div/lg-button")).equals(refreshButton)) {
                    SimpleUtils.pass("Schedule Page: Refresh button shows near week section successfully");
                } else {
                    SimpleUtils.fail("Schedule Page: Refresh button is not above welcome section", true);
                }
            } else {
                SimpleUtils.fail("Schedule Page: Refresh button isn't present", true);
            }
        } else {
            SimpleUtils.fail("Schedule Page: Refresh button failed to load", true);
        }
    }

    @FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes === 0\"]")
    private WebElement justUpdated;

    @FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes > 0\"]")
    private WebElement lastUpdated;

    @FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes > 0\"] span")
    private WebElement lastUpdatedMinutes;

    @FindBy (className = "navigation-menu-compliance-icon")
    private WebElement complianceConsoleMenu;

    @FindBy (css = ".console-navigation-item-label.Schedule")
    private WebElement scheduleConsoleMenu;

    @FindBy (css = ".analytics-new.ng-scope")
    private WebElement scheduleSection;

    @FindBy (xpath = "//span[contains(text(),'Not Started')]")
    private List<WebElement> notStartedSchedules;

    @FindBy (xpath = "//span[contains(text(),'In Progress')]")
    private List<WebElement> inProgressSchedules;

    @FindBy (className = "analytics-new-table-group-open")
    private List<WebElement> rowsInAnalyticsTable;

    @Override
    public void validateRefreshFunction() throws Exception {
        int minutes = 0;
        if (isElementLoaded(lastUpdatedMinutes,10) ) {
            minutes = lastUpdatedMinutes.getText().contains(" ")? Integer.valueOf(lastUpdatedMinutes.getText().split(" ")[0]):Integer.valueOf(lastUpdatedMinutes.getText());
            if (minutes >= 30 ) {
                if (lastUpdatedMinutes.getAttribute("class").contains("last-updated-countdown-time-orange"))
                    SimpleUtils.pass("Schedule Page: When the Last Updated time >= 30 mins, the color changes to orange");
                else
                    SimpleUtils.fail("Schedule Page: When the Last Updated time >= 30 mins, the color failed to change to orange",false);
            }
        }
        if (isElementLoaded(refreshButton, 10)) {
            clickTheElement(refreshButton);
            SimpleUtils.pass("Schedule Page: Click on Refresh button Successfully!");
            if (scheduleSection.getAttribute("class").contains("analytics-new-refreshing") && refreshButton.getAttribute("label").equals("Refreshing...")) {
                SimpleUtils.pass("Schedule Page: After clicking Refresh button, the background is muted and it shows an indicator 'Refreshing...' that we are processing the info");
                if (isElementLoaded(justUpdated,60) && !scheduleSection.getAttribute("class").contains("home-dashboard-loading"))
                    SimpleUtils.pass("Schedule Page: Once the data is done refreshing, the page shows 'JUST UPDATED' and page becomes brighter again");
                else
                    SimpleUtils.fail("Schedule Page: When the data is done refreshing, the page doesn't show 'JUST UPDATED' and page doesn't become brighter again",false);
                if (isElementLoaded(lastUpdated,60) && lastUpdatedMinutes.getAttribute("class").contains("last-updated-countdown-time-blue"))
                    SimpleUtils.pass("Schedule Page: The Last Updated info provides the minutes last updated in blue");
                else
                    SimpleUtils.fail("Schedule Page: The Last Updated info doesn't provide the minutes last updated in blue",false);
            } else {
                SimpleUtils.fail("Schedule Page: After clicking Refresh button, the background isn't muted and it doesn't show 'Refreshing...'",true);
            }
        } else {
            SimpleUtils.fail("Schedule Page: Refresh button not Loaded!", true);
        }
    }

    @Override
    public void validateRefreshPerformance() throws Exception {
        if (isElementLoaded(refreshButton, 10)) {
            clickTheElement(refreshButton);
            if (refreshButton.getAttribute("label").equals("Refreshing...")) {
                SimpleUtils.pass("Schedule Page: After clicking Refresh button, the button becomes 'Refreshing...'");
                WebElement element = (new WebDriverWait(getDriver(), 60))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[label=\"Refresh\"]")));
                if (element.isDisplayed()) {
                    SimpleUtils.pass("Schedule Page: Page refreshes within 1 minute successfully");
                } else {
                    SimpleUtils.fail("Schedule Page: Page doesn't refresh within 1 minute", false);
                }
            } else {
                SimpleUtils.fail("Schedule Page: After clicking Refresh button, the background isn't muted and it doesn't show 'Refreshing...'",true);
            }
        } else {
            SimpleUtils.fail("Schedule Page: Refresh button not Loaded!", true);
        }
    }

    @Override
    public void validateRefreshTimestamp() throws Exception {
        String timestamp = "";
        if (isElementLoaded(justUpdated, 5)) {
            SimpleUtils.pass("Schedule Page: The page just refreshed");
        } else if (isElementLoaded(lastUpdatedMinutes, 5)) {
            timestamp = lastUpdatedMinutes.getText();
            if (timestamp.contains("HOURS") && timestamp.contains(" ")) {
                timestamp = timestamp.split(" ")[0];
                if (Integer.valueOf(timestamp) == 1)
                    SimpleUtils.pass("Schedule Page: The backstop is 1 hour so that the data is not older than 1 hour stale");
                else
                    // SimpleUtils.fail("Schedule Page:  The backstop is older than 1 hour stale",false);
                    SimpleUtils.warn("SCH-2589: [DM View] Refresh time is older than 1 hour stale");
            } else if (timestamp.contains("MINS") && timestamp.contains(" ")) {
                timestamp = timestamp.split(" ")[0];
                if (Integer.valueOf(timestamp) < 60 && Integer.valueOf(timestamp) >= 1)
                    SimpleUtils.pass("Schedule Page: The backstop is last updated " + timestamp + " mins ago");
                else
                SimpleUtils.fail("Schedule Page: The backup is last updated " + timestamp + " mins ago actually", false);
            } else
                SimpleUtils.fail("Schedule Page: The backup display \'" + lastUpdated.getText() + "\'",false);
        } else
            SimpleUtils.fail("Schedule Page: Timestamp failed to load", false);
    }

    @Override
    public void navigateToSchedule() throws Exception {
        if(isElementLoaded(scheduleConsoleMenu, 10)){
            click(scheduleConsoleMenu);
        } else {
            SimpleUtils.fail("Schedule menu in left navigation is not loaded!",false);
        }
    }

    @Override
    public void validateRefreshWhenNavigationBack() throws Exception {
        String timestamp1 = "";
        String timestamp2 = "";
        if (isElementLoaded(lastUpdated, 5)) {
            timestamp1 = lastUpdated.getText();
        } else if (isElementLoaded(justUpdated, 5)) {
            timestamp1 = justUpdated.getText();
        } else
            SimpleUtils.fail("Schedule Page: Timestamp failed to load", false);
        click(complianceConsoleMenu);
        navigateToSchedule();
        if (isElementLoaded(lastUpdated, 5)) {
            timestamp2 = lastUpdated.getText();
        } else if (isElementLoaded(justUpdated, 5)) {
            timestamp2 = justUpdated.getText();
        } else
            SimpleUtils.fail("Schedule Page: Timestamp failed to load", false);
        if (timestamp2.equals(timestamp1) && !timestamp1.equals("") && !refreshButton.getAttribute("label").equals("Refreshing...")) {
            SimpleUtils.pass("Schedule Page: It keeps the previous Last Updated time, not refreshing every time");
        } else {
            SimpleUtils.fail("Schedule Page: It doesn't keep the previous Last Updated time", false);
        }
    }

    @Override
    public boolean isNotStartedScheduleDisplay() throws Exception {
        boolean isNotStartedScheduleDisplay = false;
        if (areListElementVisible(notStartedSchedules, 10) && notStartedSchedules.size() > 0) {
            isNotStartedScheduleDisplay = true;
            SimpleUtils.report("Schedule Page: Not Started Schedules loaded in the current page");
        }
        return isNotStartedScheduleDisplay;
    }

    @Override
    public List<String> getLocationsWithNotStartedSchedules() throws Exception {
        List<String> notStartedLocations = new ArrayList<>();
        WebElement location = null;
        if (isNotStartedScheduleDisplay()) {
            for (int i=0; i < notStartedSchedules.size(); i++) {
                location = notStartedSchedules.get(i).findElement(By.xpath(".//../../preceding-sibling::div[1]/span/span"));
                notStartedLocations.add(location.getText());
            }
            if (notStartedSchedules.size() == notStartedLocations.size())
                SimpleUtils.pass("Schedule Page: Get all the locations with Not Started Schedules successfully");
            else
                SimpleUtils.fail("Schedule Page: Get all the locations with Not Started Schedules incompletely",false);
        } else
            SimpleUtils.fail("Schedule Page: There are no \"Not Started\" schedules in the current page",false);
        return notStartedLocations;
    }

    @Override
    public List<String> getLocationsWithInProgressSchedules() throws Exception {
        List<String> inProgressLocations = new ArrayList<>();
        WebElement location = null;
        if (isNotStartedScheduleDisplay()) {
            for (int i=0; i < inProgressSchedules.size(); i++) {
                location = inProgressSchedules.get(i).findElement(By.xpath(".//../../preceding-sibling::div[1]/span/span"));
                inProgressLocations.add(location.getText());
            }
            if (inProgressSchedules.size() == inProgressLocations.size())
                SimpleUtils.pass("Schedule Page: Get all the locations with Not Started Schedules successfully");
            else
                SimpleUtils.fail("Schedule Page: Get all the locations with Not Started Schedules incompletely",false);
        } else
            SimpleUtils.fail("Schedule Page: There are no \"Not Started\" schedules in the current page",false);
        return inProgressLocations;
    }

    @Override
    public String getScheduleStatusForGivenLocation(String location) throws Exception {
        String scheduleStatus = "";
        if (areListElementVisible(rowsInAnalyticsTable,10)) {
            for (WebElement row : rowsInAnalyticsTable) {
                if (row.findElement(By.xpath("./div[1]/span/img/following-sibling::span")).getText().equals(location)) {
                    scheduleStatus = row.findElement(By.xpath("./div[2]/span/span")).getText().trim();
                    SimpleUtils.pass("Schedule Page: Find the location " + location + " with Schedule Status " + scheduleStatus);
                    break;
                }
            }
        } else
            SimpleUtils.fail("Schedule Page: There are no locations in current district or failed to load",false);
        return scheduleStatus;
    }

    @FindBy(xpath = "//div[contains(@class,'analytics-new-table-group-row-open')]/div[2]")
    private List<WebElement>  scheduleStatusOnScheduleDMViewPage;

    @FindBy(css = ".analytics-new-table-group-row-open [text-anchor=\"start\"]")
    private List<WebElement>  projectedOverBudgetOnScheduleDMViewPage;

    @FindBy(css = ".analytics-new-table-group-row-open [text-anchor=\"end\"]")
    private List<WebElement>  projectedUnderBudgetOnScheduleDMViewPage;

    public Map<String, Integer> getThreeWeeksScheduleStatusFromScheduleDMViewPage() throws Exception {
        Map<String, Integer> scheduleStatusFromScheduleDMViewPage = new HashMap<>();
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        for (int j=1; j<=3; j++){
            int notStartedScheduleAccount = 0;
            int inProgressScheduleAccount = 0;
            int publishedScheduleAccount = 0;

            if(areListElementVisible(scheduleStatusOnScheduleDMViewPage, 20)
                    && scheduleStatusOnScheduleDMViewPage.size()!=0) {
                for (int i = 0; i< scheduleStatusOnScheduleDMViewPage.size(); i++){
                    switch(scheduleStatusOnScheduleDMViewPage.get(i).getText()){
                        case "Not Started" :
                            notStartedScheduleAccount= notStartedScheduleAccount+1;
                            break;
                        case "Schedule is not released to managers" :
                            notStartedScheduleAccount= notStartedScheduleAccount+1;
                            break;
                        case "In Progress" :
                            inProgressScheduleAccount = inProgressScheduleAccount+1;
                            break;
                        case "Published" :
                            publishedScheduleAccount = publishedScheduleAccount+1;
                            break;
                    }
                }
                switch(j){
                    case 1:
                        scheduleStatusFromScheduleDMViewPage.put("notStartedNumberForCurrentWeek", notStartedScheduleAccount);
                        scheduleStatusFromScheduleDMViewPage.put("inProgressForCurrentWeek", inProgressScheduleAccount);
                        scheduleStatusFromScheduleDMViewPage.put("publishedForCurrentWeek", publishedScheduleAccount);
                        scheduleCommonPage.navigateToNextWeek();
                        break;
                    case 2:
                        scheduleStatusFromScheduleDMViewPage.put("notStartedNumberForNextWeek", notStartedScheduleAccount);
                        scheduleStatusFromScheduleDMViewPage.put("inProgressForNextWeek", inProgressScheduleAccount);
                        scheduleStatusFromScheduleDMViewPage.put("publishedForNextWeek", publishedScheduleAccount);
                        scheduleCommonPage.navigateToNextWeek();
                        break;
                    case 3:
                        scheduleStatusFromScheduleDMViewPage.put("notStartedNumberForTheWeekAfterNext", notStartedScheduleAccount);
                        scheduleStatusFromScheduleDMViewPage.put("inProgressForTheWeekAfterNext", inProgressScheduleAccount);
                        scheduleStatusFromScheduleDMViewPage.put("publishedForTheWeekAfterNext", publishedScheduleAccount);
                        break;
                }
             } else{
                SimpleUtils.fail("Schedule status loaded fail on Schedule DM view" , false);
            }
        }
        return scheduleStatusFromScheduleDMViewPage;
    }

    @FindBy(css = "[class=\"published-clocked-cols__svg\"] text")
    private List<WebElement>  textFromTheChartInLocationSummarySmartCard;

    public List<String> getTextFromTheChartInLocationSummarySmartCard(){
        /*
        Non-TA env:
            0: the hours on Budget bar
            1: budget bar message "Budgeted Hrs"
            2: the hours on Published bar
            3: published bar message "Published Hrs"
            4: the hours of under or cover budget
            5: the caret of under or cover budget
        TA env:
            0: the hours on Budget bar
            1: budget bar message "Budgeted Hrs"
            2: the hours on Published bar
            3: published bar message "Published Hrs"
            4: the hours on Projected bar
            5: projected bar message "Projected Hrs"
            6: the hours of under or cover budget
            7: the caret of under or cover budget
        */

        List<String> textFromChart = new ArrayList<>();
        if (areListElementVisible(textFromTheChartInLocationSummarySmartCard, 15)&&textFromTheChartInLocationSummarySmartCard.size()!=0){
            for(int i=0;i<textFromTheChartInLocationSummarySmartCard.size();i++){
                textFromChart.add(textFromTheChartInLocationSummarySmartCard.get(i)
                        .getText().replace(",", "").split(" ")[0]);
            }
            SimpleUtils.report("Get text from the chart in location summary smart card successfully! ");
        } else{
            SimpleUtils.fail("The text on the chart in location summary smart card loaded fail! ", false);
        }
        return textFromChart;
    }

    @FindBy(css = "div.published-clocked-cols-summary-title")
    private List<WebElement>  locationNumbersOnLocationSummarySmartCard;


    public List<String> getLocationNumbersFromLocationSummarySmartCard(){
        // 0: Projected within Budget location number
        // 1: Projected over Budget location number
        List<String> locationNumbers = new ArrayList<>();
        if (areListElementVisible(locationNumbersOnLocationSummarySmartCard, 5)
                && locationNumbersOnLocationSummarySmartCard.size()==2){
            for(int i=0; i< locationNumbersOnLocationSummarySmartCard.size(); i++){
                locationNumbers.add(locationNumbersOnLocationSummarySmartCard.get(i).getText());
            }
            SimpleUtils.report("Get location numbers from location summary smart card successfully! ");
        } else
            SimpleUtils.fail("Location numbers on location summary smart card loaded fail! ", false);
        return locationNumbers;
    }


    public List<Float> getTheTotalBudgetedScheduledProjectedHourOfScheduleInDMView() throws Exception {
        clickOnRefreshButton();
        List<Float> totalHours = new ArrayList<>();
        float budgetedTotalHours = 0;
        float scheduledTotalHours = 0;
        float projectedTotalHours = 0;
        if (areListElementVisible(schedulesInDMView, 10) && schedulesInDMView.size() != 0){
            for (WebElement schedule : schedulesInDMView){
                if (areListElementVisible(budgetHours, 5) && areListElementVisible(publishedHours, 5)){
                    budgetedTotalHours += Float.parseFloat(schedule.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_BUDGET_HOURS\"]")).getText().replace(",",""));
                    scheduledTotalHours += Float.parseFloat(schedule.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_PUBLISHED_HOURS\"]")).getText().replace(",",""));
                } else {
                    if (isElementLoaded(scheduleScoreSmartCard, 5)) {
                        budgetedTotalHours += Float.parseFloat(schedule.findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(2).getText().replace(",",""));
                        scheduledTotalHours += Float.parseFloat(schedule.findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(3).getText().replace(",",""));
                    } else {
                        budgetedTotalHours += Float.parseFloat(schedule.findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(1).getText().replace(",",""));
                        scheduledTotalHours += Float.parseFloat(schedule.findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(2).getText().replace(",",""));
                    }
                }
                //There is no projected hrs for non-TA https://legiontech.atlassian.net/browse/SCH-2524
//                projectedTotalHours += Float.parseFloat(schedule.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_CLOCKED_HOURS\"]")).getText().replace(",",""));
            }
            totalHours.add(budgetedTotalHours);
            totalHours.add(scheduledTotalHours);
            SimpleUtils.report("Get total budget, schedule and projected hours successfully in DM view! ");
        } else{
            SimpleUtils.fail("Get hours in DM View failed, there is no schedules display in DM view" , false);
        }
        return totalHours;
    }

    @FindBy(css = "div[class=\"card-carousel-fixed\"]")
    private WebElement  locationSummarySmartCard;

    @FindBy(css = "[class=\"card-carousel-container\"] div.card-carousel-card")
    private List<WebElement>  scheduleStatusCards;

    @FindBy(css = "div.card-carousel-card div.analytics-card-color-text-1")
    private List<WebElement>  numbersOfSpecificStatusSchedule;

    @FindBy(css = "div.card-carousel-card div.analytics-card-color-text-2")
    private List<WebElement>  specificStatusMessages;

    @FindBy(css = "div.card-carousel-card div.analytics-card-color-text-3")
    private List<WebElement>  scheduleMessages;

    @FindBy(css = "div.card-carousel-card div.analytics-card-color-text-4")
    private List<WebElement>  totalScheduleMessages;

    @FindBy(css = "[ng-repeat=\"f in header track by $index\"]")
    private List<WebElement>  schedulesTableHeaders;

    @FindBy(css = "div.card-carousel-card-primary.card-carousel-card-table")
    private WebElement  scheduleScoreSmartCard;

    @FindBy(css = "[jj-switch-when=\"extraCells\"]")
    private List<WebElement>  projectedUnderOrOverBudgetByJobTitleHours;

    @FindBy(css = "[fill=\"#919EAB\"]")
    private List<WebElement>  colsInOrgSummarySmartCard;

    @FindBy(css = ".published-clocked-cols-summary-description")
    private List<WebElement> publishedClockedColsSummaryDescription;

    public void verifyTheScheduleStatusAccountOnScheduleStatusCards() throws Exception {
        Map<String, Integer> scheduleStatusAccountFromScheduleStatusCards = getScheduleStatusAccountFromScheduleStatusCards();
        Map<String, Integer> scheduleStatusFromScheduleDMViewPage = getThreeWeeksScheduleStatusFromScheduleDMViewPage();
        if(areListElementVisible(schedulesInDMView, 5) && schedulesInDMView.size()>0
                && scheduleStatusAccountFromScheduleStatusCards.get("notStarted")
                ==scheduleStatusFromScheduleDMViewPage.get("notStartedNumberForCurrentWeek")
                && scheduleStatusAccountFromScheduleStatusCards.get("published")
                ==scheduleStatusFromScheduleDMViewPage.get("publishedForCurrentWeek")
                && scheduleStatusAccountFromScheduleStatusCards.get("inProgress")
                ==scheduleStatusFromScheduleDMViewPage.get("inProgressForCurrentWeek")){
            SimpleUtils.pass("The Specific status schedule numbers display correctly! ");
        } else
            SimpleUtils.fail("The Specific status schedule numbers display incorrectly! ", false);

        int scheduleAccount = schedulesInDMView.size();

        if (scheduleStatusAccountFromScheduleStatusCards.get("notStarted") != 0){
            if(scheduleStatusAccountFromScheduleStatusCards.get("totalNotStartedSchedules") == scheduleAccount){
                SimpleUtils.pass("The total schedule number on Not Started Schedule card display correctly! ");
            } else
                SimpleUtils.fail("The total schedule number on Not Started Schedule card display incorrectly! ", false);
        } else {
            if(scheduleStatusAccountFromScheduleStatusCards.get("totalNotStartedSchedules") == 0){
                SimpleUtils.pass("There is no Not Started Schedule card! ");
            } else
                SimpleUtils.fail("The total schedule number of Not Started Schedule card display incorrectly! ", false);
        }

        if (scheduleStatusAccountFromScheduleStatusCards.get("published") != 0){
            if(scheduleStatusAccountFromScheduleStatusCards.get("totalPublishedSchedules") == scheduleAccount){
                SimpleUtils.pass("The total schedule number on Published Schedule card display correctly! ");
            } else
                SimpleUtils.fail("The total schedule number on Published Schedule card display incorrectly! ", false);
        } else {
            if(scheduleStatusAccountFromScheduleStatusCards.get("totalPublishedSchedules") == 0){
                SimpleUtils.pass("There is no Published Schedule card! ");
            } else
                SimpleUtils.fail("The total schedule number of Published Schedule card display incorrectly! ", false);
        }

        if (scheduleStatusAccountFromScheduleStatusCards.get("inProgress") != 0){
            if(scheduleStatusAccountFromScheduleStatusCards.get("totalInProgressSchedules") == scheduleAccount){
                SimpleUtils.pass("The total schedule number on In Progress Schedule card display correctly! ");
            } else
                SimpleUtils.fail("The total schedule number on In Progress Schedule card display incorrectly! ", false);
        } else {
            if(scheduleStatusAccountFromScheduleStatusCards.get("totalInProgressSchedules") == 0){
                SimpleUtils.pass("There is no In Progress Schedule card! ");
            } else
                SimpleUtils.fail("The total schedule number of In Progress Schedule card display incorrectly! ", false);
        }
    }

    public Map<String, Integer> getScheduleStatusAccountFromScheduleStatusCards(){
        Map<String, Integer> scheduleStatusAccount = new HashMap<>();
        scheduleStatusAccount.put("notStarted",0);
        scheduleStatusAccount.put("published",0);
        scheduleStatusAccount.put("inProgress",0);
        scheduleStatusAccount.put("totalNotStartedSchedules",0);
        scheduleStatusAccount.put("totalPublishedSchedules",0);
        scheduleStatusAccount.put("totalInProgressSchedules",0);

        if(areListElementVisible(scheduleStatusCards, 15) && scheduleStatusCards.size()>0){

            for (WebElement scheduleStatusCard: scheduleStatusCards){
                String scheduleStatus = scheduleStatusCard.findElement(By.cssSelector("div.analytics-card-color-text-2")).getText();
                switch (scheduleStatus){
                    case "Not Started":
                        scheduleStatusAccount.put("notStarted",
                                Integer.parseInt(scheduleStatusCard.findElement(By.cssSelector("div.analytics-card-color-text-1")).getText()));
                        scheduleStatusAccount.put("totalNotStartedSchedules",
                                Integer.parseInt(scheduleStatusCard.findElement(By.cssSelector("div.analytics-card-color-text-4")).getText().split(" ")[0]));
                        break;
                    case "Published":
                        scheduleStatusAccount.put("published",
                                Integer.parseInt(scheduleStatusCard.findElement(By.cssSelector("div.analytics-card-color-text-1")).getText()));
                        scheduleStatusAccount.put("totalPublishedSchedules",
                                Integer.parseInt(scheduleStatusCard.findElement(By.cssSelector("div.analytics-card-color-text-4")).getText().split(" ")[0]));
                        break;
                    case "In Progress":
                        scheduleStatusAccount.put("inProgress",
                                Integer.parseInt(scheduleStatusCard.findElement(By.cssSelector("div.analytics-card-color-text-1")).getText()));
                        scheduleStatusAccount.put("totalInProgressSchedules",
                                Integer.parseInt(scheduleStatusCard.findElement(By.cssSelector("div.analytics-card-color-text-4")).getText().split(" ")[0]));
                        break;
                }
                SimpleUtils.pass("Get " +scheduleStatus+ " schedule status account successfully! ");
            }
        } else {
            SimpleUtils.fail("Schedule Status card loaded fail! ", false);
        }
        return scheduleStatusAccount;
    }

    public void verifyTheContentOnScheduleStatusCards() throws Exception {
        if(areListElementVisible(scheduleStatusCards, 5) && scheduleStatusCards.size()>0){
            if(areListElementVisible(numbersOfSpecificStatusSchedule, 5)
                    && numbersOfSpecificStatusSchedule.size() ==scheduleStatusCards.size()
                    && areListElementVisible(specificStatusMessages, 5)
                    && specificStatusMessages.size() == scheduleStatusCards.size()
                    && areListElementVisible(scheduleMessages, 5)
                    && scheduleMessages.size() == scheduleStatusCards.size()
                    && areListElementVisible(totalScheduleMessages, 5)
                    && totalScheduleMessages.size() == scheduleStatusCards.size()) {
                for (int i=0;i<scheduleStatusCards.size();i++){
                    if ((specificStatusMessages.get(i).getText().contains("Not Started")
                            ||specificStatusMessages.get(i).getText().contains("Published")
                            ||specificStatusMessages.get(i).getText().contains("In Progress"))
                            && scheduleMessages.get(i).getText().equalsIgnoreCase("Schedules")
                            && totalScheduleMessages.get(i).getText().contains("total schedules")){
                        SimpleUtils.pass("The contents on the "+ specificStatusMessages.get(i).getText() +"Schedule Status cards loaded successfully! ");
                    } else
                        SimpleUtils.fail("The contents on the "+ (i+1) +"Schedule Status cards loaded fail! ", false);
                }
            } else
                SimpleUtils.fail("The contents on Schedule Status cards loaded fail! ", false);
        } else {
            SimpleUtils.fail("Schedule Status card loaded fail! ", false);
        }
    }

    @Override
    public void verifyScheduleStatusAndHoursInScheduleList(String locationName, Boolean isTAEnv, Boolean isDGEnv, String scheduleStatus, String specificWeek) throws Exception {
        if (areListElementVisible(schedulesInDMView, 10)
                && schedulesInDMView.size()>0
                && areListElementVisible(scheduleStatusOnScheduleDMViewPage, 10)
                && scheduleStatusOnScheduleDMViewPage.size()>0
                && schedulesInDMView.size() == scheduleStatusOnScheduleDMViewPage.size()){

            //Select specific location schedule to test, because that not all schedules are available
            ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
            ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
            CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
            NewShiftPage newShiftPage = new ConsoleNewShiftPage();
            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
            ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
            Map<String,String> scheduleHoursOnScheduleDMView = new HashMap<>();
            List<String> scheduleHoursOnScheduleDetailPage = new ArrayList<>();
            String theSelectedScheduleLocationName = locationName;
            clickSpecificScheduleByLocationName(theSelectedScheduleLocationName);
            waitForSeconds(3);

            //Check the buttons on schedule page
            DecimalFormat df1 = new DecimalFormat("###.#");
            HashMap<String, String> hoursFromScheduleSMOnDGEnv = new HashMap<>();

            switch (scheduleStatus) {
                case "Not Started":
                    if(!createSchedulePage.isWeekGenerated()){
                        SimpleUtils.pass("The 'Not Started' schedule status display correctly! ");
                    } else
                        SimpleUtils.fail("The 'Not Started' schedule status display incorrectly! ", false);
                    ScheduleOverviewPage scheduleOverviewPage = new ConsoleScheduleOverviewPage();
                    scheduleOverviewPage.clickOverviewTab();
                    WebElement overviewWeek = null;
                    if (specificWeek.equals("Current Week")){
                        overviewWeek = scheduleOverviewPage.getOverviewScheduleWeeks().get(0);
                    } else if (specificWeek.equals("Next Week")) {
                        overviewWeek = scheduleOverviewPage.getOverviewScheduleWeeks().get(1);
                    } else {
                        scheduleOverviewPage.clickOnLastWeek();
                        overviewWeek = scheduleOverviewPage.getOverviewScheduleWeeks().get(0);
                    }
                    LinkedHashMap<String, Float> weekInfo = scheduleOverviewPage.getWeekHoursByWeekElement(overviewWeek);
                    String budgetHrsOnOverViewPage = df1.format(weekInfo.get("guidanceHours"));
                    scheduleHoursOnScheduleDetailPage.add(budgetHrsOnOverViewPage);
                    String scheduledHrsOnOverViewPage = df1.format(weekInfo.get("scheduledHours"));
                    scheduleHoursOnScheduleDetailPage.add(scheduledHrsOnOverViewPage);
                    hoursFromScheduleSMOnDGEnv.put("BudgetedASM","0");
                    hoursFromScheduleSMOnDGEnv.put("BudgetedLSA","0");
                    hoursFromScheduleSMOnDGEnv.put("BudgetedSA","0");
                    hoursFromScheduleSMOnDGEnv.put("BudgetedOpen","0");
                    hoursFromScheduleSMOnDGEnv.put("ScheduledASM","0");
                    hoursFromScheduleSMOnDGEnv.put("ScheduledLSA","0");
                    hoursFromScheduleSMOnDGEnv.put("ScheduledSA","0");
                    hoursFromScheduleSMOnDGEnv.put("ScheduledOpen","0");
                    break;
                case "Published":
                    if(!createSchedulePage.isPublishButtonLoadedOnSchedulePage()
                            && !createSchedulePage.isCreateScheduleBtnLoadedOnSchedulePage()){
                        SimpleUtils.pass("The 'Published' schedule status display correctly! ");
                        if (createSchedulePage.isRepublishButtonLoadedOnSchedulePage()){
//                            schedulePage.clickOnRepublishButtonLoadedOnSchedulePage();
                            createSchedulePage.publishActiveSchedule();
                        }
                    } else
                        SimpleUtils.fail("The 'Published' schedule status display incorrectly! ", false);
                    if(isDGEnv){
                        hoursFromScheduleSMOnDGEnv = getBudgetNScheduledHoursFromSmartCardOnDGEnv();
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("BudgetedTotal"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("ScheduledTotal"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("BudgetedASM"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("BudgetedLSA"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("BudgetedSA"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("BudgetedOpen"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("ScheduledASM"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("ScheduledLSA"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("ScheduledSA"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("ScheduledOpen"));
                    } else {
                        Map<String, String> hoursFromScheduleSMOnNonDGEnv = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
                        if (hoursFromScheduleSMOnNonDGEnv.get("Budget")!=null) {
                            scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnNonDGEnv.get("Budget"));
                        } else
                            scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnNonDGEnv.get("Guidance"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnNonDGEnv.get("Scheduled"));
                    }

                    break;
                case "In Progress":
                    if(createSchedulePage.isPublishButtonLoadedOnSchedulePage()
                            && !createSchedulePage.isCreateScheduleBtnLoadedOnSchedulePage()){
                        SimpleUtils.pass("The 'In Progress' schedule status display correctly! ");
                    } else
                        SimpleUtils.fail("The 'In Progress' schedule status display incorrectly! ", false);
                    if(isDGEnv){
                        hoursFromScheduleSMOnDGEnv = getBudgetNScheduledHoursFromSmartCardOnDGEnv();
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("BudgetedTotal"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnDGEnv.get("ScheduledTotal"));
                    } else {
                        Map<String, String> hoursFromScheduleSMOnNonDGEnv = smartCardPage.getBudgetNScheduledHoursFromSmartCard();
                        if (hoursFromScheduleSMOnNonDGEnv.get("Budget")!=null) {
                            scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnNonDGEnv.get("Budget"));
                        } else
                            scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnNonDGEnv.get("Guidance"));
                        scheduleHoursOnScheduleDetailPage.add(hoursFromScheduleSMOnNonDGEnv.get("Scheduled"));
                    }
                    break;
            }

            float projectionOpenShiftsFromScheduleDetailPage = 0;
            if (isDGEnv) {
                switch (specificWeek){
                    case "Current Week":
//                    projectionOpenShiftsFromScheduleDetailPage = schedulePage.getTotalProjectionOpenShiftsHoursForCurrentWeek();
                        break;
                    case "Next Week":
                        projectionOpenShiftsFromScheduleDetailPage = scheduleShiftTablePage.newCalcTotalScheduledHourForDayInWeekView();
                        break;
                }
            }
            //Only for DG env: get timesheet hours from Time sheet page
            HashMap<String, Float> timeSheetDiffHoursByJobTitle = new HashMap<>();
            float asmHours = 0;
            float lsaHours = 0;
            float saHours = 0;
            if(isDGEnv){
                TimeSheetPage timeSheetPage = new ConsoleTimeSheetPage();
                timeSheetPage.clickOnTimeSheetConsoleMenu();
                timeSheetPage.clickOnWeekDuration();
                if(specificWeek.equalsIgnoreCase("Previous Week")){
                    timeSheetPage.navigateToPreviousWeek();
                }
                List<WebElement> allTimeSheetRows = timeSheetPage.getTimeSheetWorkersRow();
                HashMap<String, Float> timeSheetHours = new HashMap<>();
                if(!specificWeek.equalsIgnoreCase("Next Week")){
                    if(allTimeSheetRows != null && allTimeSheetRows.size() != 0){
                        for (WebElement timeSheetRow: allTimeSheetRows){

                            String jobTitle = timeSheetRow.findElement(By.cssSelector(".lg-timesheet-table-improved__name span")).getAttribute("title");
                            if(jobTitle.equalsIgnoreCase("Assistant Store Manager")){
                                timeSheetHours = timeSheetPage.getWorkerAllHours(timeSheetRow);
                                asmHours = asmHours + timeSheetHours.get("DiffHours");
                            } else if (jobTitle.equalsIgnoreCase("Lead Sales Associate")){
                                timeSheetHours = timeSheetPage.getWorkerAllHours(timeSheetRow);
                                lsaHours = lsaHours + timeSheetHours.get("DiffHours");
                            } else if (jobTitle.equalsIgnoreCase("Sales Associate")){
                                timeSheetHours = timeSheetPage.getWorkerAllHours(timeSheetRow);
                                saHours = saHours + timeSheetHours.get("DiffHours");
                            }
                        }
                    } else
                        SimpleUtils.fail("Get hours from timesheet page fail, the timesheet rows are null! ", false);
                }
                timeSheetDiffHoursByJobTitle.put("asmDiffHours", asmHours);
                timeSheetDiffHoursByJobTitle.put("lsaDiffHours", lsaHours);
                timeSheetDiffHoursByJobTitle.put("saDiffHours", saHours);
            }


            LiquidDashboardPage liquidDashboardPage = new ConsoleLiquidDashboardPage();
            DashboardPage dashboardPage = new ConsoleDashboardPage();
            LocationSelectorPage locationSelectorPage = new ConsoleLocationSelectorPage();
            String projectedHoursOnSMDashboard = "";
            if(isTAEnv){
                //Get Projected hours from schedule widget on dashboard
                dashboardPage.clickOnDashboardConsoleMenu();
                dashboardPage.clickOnRefreshButtonOnSMDashboard();
//                locationSelectorPage.changeLocation(theSelectedScheduleLocationName);
                List<String> dataFromScheduleWidget = liquidDashboardPage.getDataOnSchedulesWidget();
                switch(specificWeek){
                    case "Current Week":
                        projectedHoursOnSMDashboard = df1.format(Float.parseFloat(dataFromScheduleWidget.get(1).split(",")[7].split(" ")[1]));
                        break;
                    case "Previous Week":
                        projectedHoursOnSMDashboard = df1.format(Float.parseFloat(dataFromScheduleWidget.get(0).split(",")[7].split(" ")[1]));
                        break;
                    case "Next Week":
                        projectedHoursOnSMDashboard = df1.format(Float.parseFloat(dataFromScheduleWidget.get(2).split(",")[7].split(" ")[1]));
                        break;
                }
            }

            //validate the schedule hours are consistent between Schedule DM view and schedule detail page
            locationSelectorPage.selectCurrentUpperFieldAgain("District");
            scheduleCommonPage.clickOnScheduleConsoleMenuItem();
            if(isTAEnv){
                switch(specificWeek){
                    case "Current Week":
                        break;
                    case "Previous Week":
                        scheduleCommonPage.navigateToPreviousWeek();
                        break;
                    case "Next Week":
                        scheduleCommonPage.navigateToNextWeek();
                        break;
                }
            }
            clickOnRefreshButton();
            waitForSeconds(3);
            int i = 0;
            while (isElementLoaded(lastUpdated) && i<5) {
                clickOnRefreshButton();
                i++;
            }
            scheduleHoursOnScheduleDMView = getAllScheduleInfoFromScheduleInDMViewByLocation(theSelectedScheduleLocationName);
            Map<String,String> projectedOverBudgetHours= calculateProjectedOrPublishedUnderOrOverBudgetHours(theSelectedScheduleLocationName);
            Map<String, Float> projectedOverBudgetHoursByJobTitle = new HashMap<>();
            if (isDGEnv) {
                projectedOverBudgetHoursByJobTitle = calculateProjectedUnderOrOverBudgetByJobTitleHours(hoursFromScheduleSMOnDGEnv, timeSheetDiffHoursByJobTitle);
            }
            switch (scheduleStatus) {
                case "Not Started":
                    if((isTAEnv
                            && scheduleHoursOnScheduleDMView.get("budgetedHours").equalsIgnoreCase(df1.format(Float.parseFloat(scheduleHoursOnScheduleDetailPage.get(0))))
                            && scheduleHoursOnScheduleDMView.get("publishedHours").equalsIgnoreCase(df1.format(Float.parseFloat(scheduleHoursOnScheduleDetailPage.get(1))))              //blocking by https://legiontech.atlassian.net/browse/SCH-1874
                            && scheduleHoursOnScheduleDMView.get("publishedStatus").equalsIgnoreCase("Not Started")
                            && checkIfTheSpecificHeaderDisplayInScheduleListOnScheduleDMView("Projected Hours")
                            && scheduleHoursOnScheduleDMView.get("projectedHours").equalsIgnoreCase(projectedHoursOnSMDashboard)
                            && scheduleHoursOnScheduleDMView.get("projectedOverBudgetHours").
                            equalsIgnoreCase(projectedOverBudgetHours.get("projectedOverBudgetHours").equalsIgnoreCase("")?"":
                                    (df1.format(Float.parseFloat(projectedOverBudgetHours.get("projectedOverBudgetHours")))))
                            && scheduleHoursOnScheduleDMView.get("projectedUnderBudgetHours").
                            equalsIgnoreCase(projectedOverBudgetHours.get("projectedUnderBudgetHours").equalsIgnoreCase("")?"":
                                    (df1.format(Float.parseFloat(projectedOverBudgetHours.get("projectedUnderBudgetHours")))))
                            && (!isDGEnv || (scheduleHoursOnScheduleDMView.get("asmHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("asmHours")))
                            && scheduleHoursOnScheduleDMView.get("lsaHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("lsaHours")))
                            && scheduleHoursOnScheduleDMView.get("saHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("saHours")))
                            && scheduleHoursOnScheduleDMView.get("openHours").equalsIgnoreCase(df1.format(projectionOpenShiftsFromScheduleDetailPage)))))
                            ||  (!isTAEnv
                            && scheduleHoursOnScheduleDMView.get("budgetedHours").equalsIgnoreCase(scheduleHoursOnScheduleDetailPage.get(0))
                            && scheduleHoursOnScheduleDMView.get("publishedHours").equalsIgnoreCase(scheduleHoursOnScheduleDetailPage.get(1))              //blocking by https://legiontech.atlassian.net/browse/SCH-1874
                            && scheduleHoursOnScheduleDMView.get("publishedStatus").equalsIgnoreCase("Not Started")
                            && !checkIfTheSpecificHeaderDisplayInScheduleListOnScheduleDMView("Projected Hours"))){
                        SimpleUtils.pass("The budgeted hours on Schedule DM view is consistent " +
                                "with the budgeted hour on Overview page for Not Started schedule!");
                    } else
                        SimpleUtils.fail("The budgeted hours on Schedule DM view is inconsistent " +
                                "with the budgeted hour on Overview page for Not Started schedule! ", false);
                    break;
                case "Published":
                    if((isTAEnv
                            && scheduleHoursOnScheduleDMView.get("budgetedHours").equalsIgnoreCase(df1.format(Float.parseFloat(scheduleHoursOnScheduleDetailPage.get(0))))
                            && scheduleHoursOnScheduleDMView.get("publishedHours").equalsIgnoreCase(df1.format(Float.parseFloat(scheduleHoursOnScheduleDetailPage.get(1))))              //blocking by https://legiontech.atlassian.net/browse/SCH-1874
                            && scheduleHoursOnScheduleDMView.get("publishedStatus").equalsIgnoreCase("Published")
                            && checkIfTheSpecificHeaderDisplayInScheduleListOnScheduleDMView("Projected Hours")
//                            && scheduleHoursOnScheduleDMView.get("projectedHours").equalsIgnoreCase(projectedHoursOnSMDashboard)      //comment it because https://legiontech.atlassian.net/browse/SCH-1482
                            && scheduleHoursOnScheduleDMView.get("projectedOverBudgetHours").
                            equalsIgnoreCase(projectedOverBudgetHours.get("projectedOverBudgetHours").equalsIgnoreCase("")?"":
                                    (df1.format(Float.parseFloat(projectedOverBudgetHours.get("projectedOverBudgetHours")))))
                            && scheduleHoursOnScheduleDMView.get("projectedUnderBudgetHours").
                            equalsIgnoreCase(projectedOverBudgetHours.get("projectedUnderBudgetHours").equalsIgnoreCase("")?"":
                                    (df1.format(Float.parseFloat(projectedOverBudgetHours.get("projectedUnderBudgetHours")))))
                            && (!isDGEnv || (scheduleHoursOnScheduleDMView.get("asmHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("asmHours")))
                            && scheduleHoursOnScheduleDMView.get("lsaHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("lsaHours")))
                            && scheduleHoursOnScheduleDMView.get("saHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("saHours"))))
                            && scheduleHoursOnScheduleDMView.get("openHours").equalsIgnoreCase(df1.format(projectionOpenShiftsFromScheduleDetailPage))))
                            ||  (!isTAEnv
                            && scheduleHoursOnScheduleDMView.get("budgetedHours").equalsIgnoreCase(scheduleHoursOnScheduleDetailPage.get(0))
                            && scheduleHoursOnScheduleDMView.get("publishedHours").equalsIgnoreCase(scheduleHoursOnScheduleDetailPage.get(1))
                            && scheduleHoursOnScheduleDMView.get("publishedStatus").equalsIgnoreCase("Published")
                            && !checkIfTheSpecificHeaderDisplayInScheduleListOnScheduleDMView("Projected Hours"))){
                        SimpleUtils.pass("The budgeted and projected hours on Schedule DM view is consistent " +
                                "with the budgeted and projected hour on Schedule detail page for Published schedule!");
                    } else
                        SimpleUtils.fail("The budgeted and projected hours on Schedule DM view is inconsistent " +
                                "with the budgeted and projected hour on Schedule detail page for Published schedule! ", false);
                    break;
                case "In Progress":
                    if((isTAEnv
                            && scheduleHoursOnScheduleDMView.get("budgetedHours").equalsIgnoreCase(df1.format(Float.parseFloat(scheduleHoursOnScheduleDetailPage.get(0))))
                            && scheduleHoursOnScheduleDMView.get("publishedHours").equalsIgnoreCase(df1.format(Float.parseFloat(scheduleHoursOnScheduleDetailPage.get(1))))              //blocking by https://legiontech.atlassian.net/browse/SCH-1874
                            && scheduleHoursOnScheduleDMView.get("publishedStatus").equalsIgnoreCase("In Progress")
                            && checkIfTheSpecificHeaderDisplayInScheduleListOnScheduleDMView("Projected Hours")
                            && scheduleHoursOnScheduleDMView.get("projectedHours").equalsIgnoreCase(projectedHoursOnSMDashboard)
                            && scheduleHoursOnScheduleDMView.get("projectedOverBudgetHours").
                            equalsIgnoreCase(projectedOverBudgetHours.get("projectedOverBudgetHours").equalsIgnoreCase("")?"":
                                    (df1.format(Float.parseFloat(projectedOverBudgetHours.get("projectedOverBudgetHours")))))
                            && scheduleHoursOnScheduleDMView.get("projectedUnderBudgetHours").
                            equalsIgnoreCase(projectedOverBudgetHours.get("projectedUnderBudgetHours").equalsIgnoreCase("")?"":
                                    (df1.format(Float.parseFloat(projectedOverBudgetHours.get("projectedUnderBudgetHours")))))
                            && (!isDGEnv || (scheduleHoursOnScheduleDMView.get("asmHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("asmHours")))
                            && scheduleHoursOnScheduleDMView.get("lsaHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("lsaHours")))
                            && scheduleHoursOnScheduleDMView.get("saHours").equalsIgnoreCase(df1.format(projectedOverBudgetHoursByJobTitle.get("saHours"))))
                            && scheduleHoursOnScheduleDMView.get("openHours").equalsIgnoreCase(df1.format(projectionOpenShiftsFromScheduleDetailPage))))
                            ||  (!isTAEnv
                            && scheduleHoursOnScheduleDMView.get("budgetedHours").equalsIgnoreCase(scheduleHoursOnScheduleDetailPage.get(0))
                            && scheduleHoursOnScheduleDMView.get("publishedHours").equalsIgnoreCase(scheduleHoursOnScheduleDetailPage.get(1))              //blocking by https://legiontech.atlassian.net/browse/SCH-1874
                            && scheduleHoursOnScheduleDMView.get("publishedStatus").equalsIgnoreCase("In Progress")
//                            && !checkIfTheSpecificHeaderDisplayInScheduleListOnScheduleDMView("Projected Hours") // blocking by https://legiontech.atlassian.net/browse/SCH-2524
                    )){
                        SimpleUtils.pass("The budgeted hours on Schedule DM view is consistent " +
                                "with the budgeted hour on Schedule detail page for In Progress schedule!");
                    } else
                        SimpleUtils.fail("The budgeted hours on Schedule DM view is inconsistent " +
                                "with the budgeted hour on Schedule detail page for In Progress schedule! ", false);
                    break;
            }
        } else
            SimpleUtils.fail("Schedules in schedule DM view page loaded fail! ", false);
    }

    public Map<String, Float> calculateProjectedUnderOrOverBudgetByJobTitleHours (HashMap<String, String> hoursFromScheduleSMOnDGEnv, HashMap<String, Float> timeSheetDiffHoursByJobTitle) throws Exception {
        HashMap<String, Float> projectedUnderOrOverBudgetByJobTitleHours = new HashMap();

        if(hoursFromScheduleSMOnDGEnv != null && hoursFromScheduleSMOnDGEnv.size()!=0
                && timeSheetDiffHoursByJobTitle !=null && timeSheetDiffHoursByJobTitle.size()!=0){

            // Calculation formula： Schedule hours - Budget hours + Timesheet hours = projected Under Or Over Budget By Job Title Hours
            float asmHours = Float.parseFloat(hoursFromScheduleSMOnDGEnv.get("ScheduledASM"))
                    - Float.parseFloat(hoursFromScheduleSMOnDGEnv.get("BudgetedASM"))
                    + timeSheetDiffHoursByJobTitle.get("asmDiffHours");
            float lsaHours = Float.parseFloat(hoursFromScheduleSMOnDGEnv.get("ScheduledLSA"))
                    - Float.parseFloat(hoursFromScheduleSMOnDGEnv.get("BudgetedLSA"))
                    + timeSheetDiffHoursByJobTitle.get("lsaDiffHours");
            float saHours = Float.parseFloat(hoursFromScheduleSMOnDGEnv.get("ScheduledSA"))
                    - Float.parseFloat(hoursFromScheduleSMOnDGEnv.get("BudgetedSA"))
                    + timeSheetDiffHoursByJobTitle.get("saDiffHours");

            projectedUnderOrOverBudgetByJobTitleHours.put("asmHours", asmHours);
            projectedUnderOrOverBudgetByJobTitleHours.put("lsaHours", lsaHours);
            projectedUnderOrOverBudgetByJobTitleHours.put("saHours", saHours);
        } else {
            SimpleUtils.fail("Cannot calculate the Projected Under Or Over Budget By Job Title Hours, because the hours from schedule or timesheet page are null! ", false);
        }
        return projectedUnderOrOverBudgetByJobTitleHours;
    }

    public Map<String, String> calculateProjectedOrPublishedUnderOrOverBudgetHours (String locationName) throws Exception {
        Map<String, String> projectedUnderOrOverBudgetHours = new HashMap<>();
        String projectedUnderBudgetHours = "";
        String projectedOverBudgetHours = "";
        Map<String, String> scheduleInfo = getAllScheduleInfoFromScheduleInDMViewByLocation(locationName);
        if (scheduleInfo != null){
            float budgetHours = Float.parseFloat(scheduleInfo.get("budgetedHours"));
            float projectedHours = 0;
            if (scheduleInfo.get("projectedHours")!=null) {
                projectedHours = Float.parseFloat(scheduleInfo.get("projectedHours"));
            } else if (scheduleInfo.get("publishedHours")!=null) {
                projectedHours = Float.parseFloat(scheduleInfo.get("publishedHours"));
            } else
                SimpleUtils.fail("Get location projected or published hours fail! ", false);

            if (budgetHours > projectedHours){
                projectedUnderBudgetHours = String.valueOf(budgetHours - projectedHours);

            } else if (budgetHours <= projectedHours) {
                projectedOverBudgetHours = String.valueOf(projectedHours - budgetHours);
            }
        } else
            SimpleUtils.fail("Get schedule info from schedule DM view fail! ", false);
        projectedUnderOrOverBudgetHours.put("projectedUnderBudgetHours", projectedUnderBudgetHours);
        projectedUnderOrOverBudgetHours.put("projectedOverBudgetHours", projectedOverBudgetHours);
        return projectedUnderOrOverBudgetHours;
    }

    @FindBy (css = "lg-search.analytics-new-table-filter input")
    private WebElement searchLocationInCompliancePage;
    @FindBy(css = "[jj-switch-when=\"cells.CELL_CLOCKED_HOURS\"]")
    private List<WebElement>  projectedHrs;
    @FindBy(css = "projected-over-under text")
    private List<WebElement> budgetVariances;

    public Map<String, String> getAllScheduleInfoFromScheduleInDMViewByLocation(String location) throws Exception {
        Map<String, String> allScheduleInfo = new HashMap<>();
        boolean isLocationMatched = false;
        if (areListElementVisible(schedulesInDMView, 20) && schedulesInDMView.size() != 0){
            for (int i=0; i< schedulesInDMView.size(); i++){
                WebElement locationInDMView = schedulesInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_UNTOUCHED\"]"));
                if (locationInDMView != null){
                    String locationNameInDMView = locationInDMView.getText();
                    if (locationNameInDMView !=null && locationNameInDMView.contains(location)){
                        isLocationMatched = true;
                        //add schedule Location Name
                        allScheduleInfo.put("locationName",locationNameInDMView);
                        //add Schedule Status
                        allScheduleInfo.put("publishedStatus", schedulesInDMView.get(i).findElement(By.className("analytics-new-table-published-status")).getText());
                        //add Score
//                        allScheduleInfo.add(schedulesInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_SCORE\"]")).getText());   //Need Turn off Score function on Schedule DM view
                        String budgetedHours = "";
                        if (areListElementVisible(budgetHours, 5)){
                            budgetedHours = schedulesInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_BUDGET_HOURS\"]")).getText().replace(",","");
                        } else {
                            if (isElementLoaded(scheduleScoreSmartCard, 5)) {
                                budgetedHours = schedulesInDMView.get(i).findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(3).getText().replace(",","");
                            } else
                                budgetedHours = schedulesInDMView.get(i).findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(2).getText().replace(",","");
                        }

                        //add Budgeted Hours
                        allScheduleInfo.put("budgetedHours", budgetedHours);
                        //add Scheduled Hours
                        allScheduleInfo.put("publishedHours", schedulesInDMView.get(i).
                                findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_PUBLISHED_HOURS\"]")).getText().replace(",",""));
                        if (areListElementVisible(projectedHrs, 5)){
                            //add Projected Hours
                            String projectedHours = schedulesInDMView.get(i).
                                    findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_CLOCKED_HOURS\"]")).getText().replace(",","");
                            allScheduleInfo.put("projectedHours", projectedHours);
                            //add Projected Under/Over Budget Hours
                            if(Float.parseFloat(budgetedHours) > Float.parseFloat(projectedHours)){
                                allScheduleInfo.put("projectedUnderBudgetHours", schedulesInDMView.get(i).findElement(By.cssSelector("[text-anchor=\"end\"]")).getText());
                                allScheduleInfo.put("projectedOverBudgetHours", "");
                            } else{
                                allScheduleInfo.put("projectedOverBudgetHours", schedulesInDMView.get(i).findElement(By.cssSelector("[text-anchor=\"start\"]")).getText());
                                allScheduleInfo.put("projectedUnderBudgetHours", "");
                            }
                        }

                        //add Budget Variance
                        if (areListElementVisible(budgetVariances, 5)) {
                            allScheduleInfo.put("budgetVariance", schedulesInDMView.get(i).
                                    findElement(By.cssSelector("projected-over-under text")).getText().replace(",",""));
                        }

                        //add projectedUnderOrOverBudgetByJobTitleHours on TA-DG env
                        if(areListElementVisible(projectedUnderOrOverBudgetByJobTitleHours, 5)){
                            List<WebElement> projectedUnderOrOverBudgetByJobTitleHours = schedulesInDMView.get(i).findElements(By.cssSelector("[jj-switch-when=\"extraCells\"]"));
                            if(areListElementVisible(projectedUnderOrOverBudgetByJobTitleHours, 5)
                                    && projectedUnderOrOverBudgetByJobTitleHours.size()==4){
                                allScheduleInfo.put("asmHours", projectedUnderOrOverBudgetByJobTitleHours.get(0).getText());
                                allScheduleInfo.put("lsaHours", projectedUnderOrOverBudgetByJobTitleHours.get(1).getText());
                                allScheduleInfo.put("saHours", projectedUnderOrOverBudgetByJobTitleHours.get(2).getText());
                                allScheduleInfo.put("openHours", projectedUnderOrOverBudgetByJobTitleHours.get(3).getText());
                                SimpleUtils.pass("Get Projected Under Or Over Budget By Job Title Hours successfully! ");
                            } else
                                SimpleUtils.fail("Get Projected Under Or Over Budget By Job Title Hours fail! ", false);
                        }
                        break;
                    }
                } else{
                    SimpleUtils.fail("Get schedule info in DM View failed, there is no location display in this schedule" , false);
                }
            }
            if(!isLocationMatched)
            {
                SimpleUtils.fail("Get schedule info in DM View failed, there is no matched location display in DM view" , false);
            } else{
                SimpleUtils.pass("Get schedule info in DM View successful! ");
            }
        } else{
            SimpleUtils.fail("Get schedule info in DM View failed, there is no schedules display in DM view" , false);
        }
        return allScheduleInfo;
    }

    public void verifySmartCardsAreLoadedForPastOrFutureWeek(boolean isApplyBudget, boolean isPastWeek) throws Exception {
        String[] columnNamesInOrgSummarySmartCard;
        if (
//                    isElementLoaded(scheduleScoreSmartCard, 10) &&  //Score smart card maybe turned off
                isElementLoaded(locationSummarySmartCard, 10)
                        && areListElementVisible(scheduleStatusCards, 10)) {
            SimpleUtils.pass("All smart cards on Schedule DM view page for Past week loaded successfully! ");
            if (isApplyBudget) {
                if (isPastWeek)
                    columnNamesInOrgSummarySmartCard = new String[]{"Budgeted Hrs", "Published Hrs",
                            "Clocked Hrs", "Published Within Budget", "Published Over Budget"};
                else
                    columnNamesInOrgSummarySmartCard = new String[]{"Budgeted Hrs", "Scheduled Hrs",
                            "Projected Hrs", "Scheduled Within Budget", "Scheduled Over Budget"};
            } else {
                if (isPastWeek)
                    columnNamesInOrgSummarySmartCard = new String[]{"Guidance Hrs", "Published Hrs",
                            "Clocked Hrs", "Published Within Guidance", "Published Over Guidance"};
                else
                    columnNamesInOrgSummarySmartCard = new String[]{"Guidance Hrs", "Scheduled Hrs",
                            "Projected Hrs", "Scheduled Within Guidance", "Scheduled Over Guidance"};
            }
            for(int i = 0;i < colsInOrgSummarySmartCard.size(); i++){
                if(colsInOrgSummarySmartCard.get(i).getText().equals(columnNamesInOrgSummarySmartCard[i])){
                    SimpleUtils.pass("Schedule table header: " + colsInOrgSummarySmartCard.get(i).getText()+" display correctly! ");
                } else
                    SimpleUtils.fail("Schedule table header: " + columnNamesInOrgSummarySmartCard[i] +" display incorrectly! ", false);
            }
            for(int i = colsInOrgSummarySmartCard.size(); i < colsInOrgSummarySmartCard.size() + publishedClockedColsSummaryDescription.size(); i++){
                if(publishedClockedColsSummaryDescription.get(i - colsInOrgSummarySmartCard.size()).getText().equals(columnNamesInOrgSummarySmartCard[i])){
                    SimpleUtils.pass("Schedule table header: " + publishedClockedColsSummaryDescription.get(i - colsInOrgSummarySmartCard.size()).getText()+" display correctly! ");
                } else
                    SimpleUtils.fail("Schedule table header: " + columnNamesInOrgSummarySmartCard[i] +" display incorrectly! ", false);
            }
        } else
            SimpleUtils.fail("The smart cards on Schedule upperfield view page for past week loaded fail! ", false);
    }

    public void verifySchedulesTableHeaderNames(boolean isApplyBudget, boolean isPastWeek) throws Exception {
        WebElement allOrg = MyThreadLocal.getDriver().findElement(By.xpath("//div[3]//lg-picker-input/div/input-field//div"));
        String org = allOrg.getText().contains(" ")? allOrg.getText().split(" ")[1]:allOrg.getText().replace("All ", "");
        if (org.length() > 1)
            org = org.substring(0, org.length()-1);

        if(areListElementVisible(schedulesTableHeaders, 30) && schedulesTableHeaders.size() == 8){
            String[] schedulesTableHeaderNames;
            if(isApplyBudget){
                if(!isPastWeek)
                    schedulesTableHeaderNames = new String[]{org, "Published Status", "Score",
                            "Budget Hrs", "Scheduled Hrs", "Projected Hrs", "Budget Variance", ""};
                else
                    schedulesTableHeaderNames = new String[]{org, "Published Status", "Score",
                            "Budget Hrs", "Published Hrs", "Clocked Hrs", "Budget Variance", ""};
            } else {
                if(!isPastWeek)
                    schedulesTableHeaderNames = new String[]{org, "Published Status", "Score",
                            "Guidance Hrs", "Scheduled Hrs", "Projected Hrs", "Guidance Variance", ""};
                else
                    schedulesTableHeaderNames = new String[]{org, "Published Status", "Score",
                            "Guidance Hrs", "Published Hrs", "Clocked Hrs", "Guidance Variance", ""};
            }
            for(int i= 0;i<schedulesTableHeaders.size(); i++){
                if(schedulesTableHeaders.get(i).getText().equals(schedulesTableHeaderNames[i])){
                    SimpleUtils.pass("Schedule table header: " + schedulesTableHeaders.get(i).getText()+" display correctly! ");
                } else
                    SimpleUtils.fail("Schedule table header: " + schedulesTableHeaderNames[i] +" display incorrectly! ", false);
            }
        } else
            SimpleUtils.fail("Schedules Table Headers on Schedule DM view loaded fail! ", false);
    }

    public boolean checkIfTheSpecificHeaderDisplayInScheduleListOnScheduleDMView(String headerName){
        boolean isHeaderExist = false;
        if(areListElementVisible(schedulesTableHeaders, 10) && schedulesTableHeaders.size() > 0){
            for(int i= 0;i<schedulesTableHeaders.size(); i++){
                if(schedulesTableHeaders.get(i).getText().equals(headerName)){
                    isHeaderExist = true;
                    SimpleUtils.report("Schedule table header: " + headerName +" display correctly! ");
                    break;
                }
            }
            if (!isHeaderExist){
                SimpleUtils.report("Schedule table header: " + headerName +" is not display on Schedule list! ");
            }
        } else
            SimpleUtils.fail("Schedules Table Headers on Schedule DM view loaded fail! ", false);
        return  isHeaderExist;
    }

    public void clickSpecificScheduleByLocationName(String locationName){
        if(areListElementVisible(schedulesInDMView, 10) && schedulesInDMView.size()!=0){
            boolean isLocationExist = false;
            for (WebElement scheduleInDMView:schedulesInDMView){
                String scheduleLocationName = scheduleInDMView.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_UNTOUCHED\"]")).getText();
                if(scheduleLocationName.contains(locationName)){
                    isLocationExist = true;
                    click(scheduleInDMView);
                    SimpleUtils.pass("Click the schedule of "+ locationName+ " successfully! ");
                    break;
                }
            }
            if(!isLocationExist){
                SimpleUtils.fail("Click schedule in DM View failed, the location: "+locationName+" is not display in DM view" , false);
            }
        }  else{
            SimpleUtils.fail("Click schedule in DM View failed, there is no schedules display in DM view" , false);
        }

    }

    @FindBy(css = ".analytics-new-table-group-row-open")
    private List<WebElement>  rowsInScheduleUpperfieldViewTable;

    @FindBy (css = "lg-search.analytics-new-table-filter input")
    private WebElement searchInSchedulePage;

    @FindBy (css = ".analytics-new-table-group-row-open [jj-switch-when=\"cells.CELL_UNTOUCHED\"] span")
    private List<WebElement> upperFieldNamesInScheduleTable;

    public Map<String, String> getAllUpperFieldInfoFromScheduleByUpperField(String upperFieldName) throws Exception {
        Map<String, String> allUpperFieldInfo = new HashMap<>();
        boolean isUpperFieldMatched = false;
        if (isElementLoaded(searchInSchedulePage,5)) {
            searchInSchedulePage.sendKeys(upperFieldName);
            waitForSeconds(3);
            if (areListElementVisible(rowsInScheduleUpperfieldViewTable, 10) && rowsInScheduleUpperfieldViewTable.size() != 0) {
                for (int i=0; i< rowsInScheduleUpperfieldViewTable.size(); i++){
                    WebElement locationInDMView = rowsInScheduleUpperfieldViewTable.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_UNTOUCHED\"]"));
                    if (locationInDMView != null){
                        String locationNameInDMView = locationInDMView.getText();
                        if (locationNameInDMView !=null && locationNameInDMView.equals(upperFieldName)){
                            isUpperFieldMatched = true;
                            //add schedule Location Name
                            allUpperFieldInfo.put("locationName",locationNameInDMView);
                            //add Schedule Status
                            allUpperFieldInfo.put("publishedStatus", schedulesInDMView.get(i).findElement(By.className("analytics-new-table-published-status")).getText());
                            //add Score
//                        allScheduleInfo.add(schedulesInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_SCORE\"]")).getText());   //Need Turn off Score function on Schedule DM view
                            String budgetedHours = "";
                            if (areListElementVisible(budgetHours, 5)){
                                budgetedHours = schedulesInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_BUDGET_HOURS\"]")).getText().replace(",","");
                            } else {
                                if (isElementLoaded(scheduleScoreSmartCard, 5)) {
                                    budgetedHours = schedulesInDMView.get(i).findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(3).getText().replace(",","");
                                } else
                                    budgetedHours = schedulesInDMView.get(i).findElements(By.cssSelector("[ng-switch=\"headerIndexes[$index]\"]")).get(2).getText().replace(",","");
                            }

                            //add Budgeted Hours
                            allUpperFieldInfo.put("budgetedHours", budgetedHours);
                            //add Scheduled Hours
                            allUpperFieldInfo.put("publishedHours", schedulesInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_PUBLISHED_HOURS\"]")).getText());
                            if (areListElementVisible(projectedHrs, 5)){
                                //add Projected Hours
                                String projectedHours = schedulesInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_CLOCKED_HOURS\"]")).getText().replace(",","");
                                allUpperFieldInfo.put("projectedHours", projectedHours);
                                //add Projected Under/Over Budget Hours
                                if(Float.parseFloat(budgetedHours) > Float.parseFloat(projectedHours)){
                                    allUpperFieldInfo.put("projectedUnderBudgetHours", schedulesInDMView.get(i).findElement(By.cssSelector("[text-anchor=\"end\"]")).getText());
                                    allUpperFieldInfo.put("projectedOverBudgetHours", "");
                                } else{
                                    allUpperFieldInfo.put("projectedOverBudgetHours", schedulesInDMView.get(i).findElement(By.cssSelector("[text-anchor=\"start\"]")).getText());
                                    allUpperFieldInfo.put("projectedUnderBudgetHours", "");
                                }
                            }

                            //add projectedUnderOrOverBudgetByJobTitleHours on TA-DG env
                            if(areListElementVisible(projectedUnderOrOverBudgetByJobTitleHours, 5)){
                                List<WebElement> projectedUnderOrOverBudgetByJobTitleHours = schedulesInDMView.get(i).findElements(By.cssSelector("[jj-switch-when=\"extraCells\"]"));
                                if(areListElementVisible(projectedUnderOrOverBudgetByJobTitleHours, 5)
                                        && projectedUnderOrOverBudgetByJobTitleHours.size()==4){
                                    allUpperFieldInfo.put("asmHours", projectedUnderOrOverBudgetByJobTitleHours.get(0).getText());
                                    allUpperFieldInfo.put("lsaHours", projectedUnderOrOverBudgetByJobTitleHours.get(1).getText());
                                    allUpperFieldInfo.put("saHours", projectedUnderOrOverBudgetByJobTitleHours.get(2).getText());
                                    allUpperFieldInfo.put("openHours", projectedUnderOrOverBudgetByJobTitleHours.get(3).getText());
                                    SimpleUtils.pass("Get Projected Under Or Over Budget By Job Title Hours successfully! ");
                                } else
                                    SimpleUtils.fail("Get Projected Under Or Over Budget By Job Title Hours fail! ", false);
                            }
                            break;
                        }
                    } else{
                        SimpleUtils.fail("Get schedule info in DM View failed, there is no location display in this schedule" , false);
                    }
                }
                if (!isUpperFieldMatched) {
                    SimpleUtils.fail("Get upperField info in DM View failed, there is no matched upperField display in DM view", false);
                } else {
                    SimpleUtils.pass("Get upperField info in DM View successful! ");
                }
            } else
                SimpleUtils.fail("Get upperField info in DM View failed, there is no upperField display in DM view", false);
            searchInSchedulePage.clear();
        } else {
            SimpleUtils.fail("getDataInCompliancePage: search input fail to load!", true);
        }
        return allUpperFieldInfo;
    }

    @FindBy(css = "div.card-carousel-card-title")
    private WebElement locationsSummaryTitleOnSchedule;

    @FindBy(css = "div.published-clocked-cols-summary-title")
    private List<WebElement> locationsSummarySmartCardOnSchedule;
    public List<String> getLocationSummaryDataFromSchedulePage() throws Exception{
        String locationSummaryTitleOnSchedule = null;
        List<String> ListLocationSummaryOnSchedule = new ArrayList<>();
        if(isElementLoaded(locationsSummaryTitleOnSchedule, 10)){
            locationSummaryTitleOnSchedule = locationsSummaryTitleOnSchedule.getText();
            ListLocationSummaryOnSchedule.add(locationSummaryTitleOnSchedule);
        }else{
            SimpleUtils.fail("Location Summary Title not available on Dashboard Page", true);
        }

        if(areListElementVisible(locationsSummarySmartCardOnSchedule,10) && locationsSummarySmartCardOnSchedule.size()!=0){
            for(int i =0; i< locationsSummarySmartCardOnSchedule.size();i++){
                ListLocationSummaryOnSchedule.add(locationsSummarySmartCardOnSchedule.get(i).getText());
            }
        }else{
            SimpleUtils.fail("Location Summary Smart Card not available on Dashboard Page", true);
        }

        return ListLocationSummaryOnSchedule;
    }


    @FindBy(css = "div[class=\"card-carousel-card card-carousel-card-primary \"]")
    private WebElement locationSummary;
    @Override
    public HashMap<String, Float> getValuesAndVerifyInfoForLocationSummaryInDMView(String upperFieldType, String weekType) throws Exception {
        HashMap<String, Float> result = new HashMap<String, Float>();
        if (isElementLoaded(locationSummary,10) && locationSummary.findElements(By.cssSelector("text")).size()>=6){
            String upperFieldSummaryTitle = locationSummary.findElement(By.cssSelector(".card-carousel-card-title")).getText().toLowerCase();
            if (upperFieldSummaryTitle.contains(upperFieldType.toLowerCase() + "s summary")
                    || upperFieldSummaryTitle.contains(upperFieldType.toLowerCase() + " summary")){
                SimpleUtils.pass("Location Summary smart title displays correctly!");
                String numOfLocations = locationSummary.findElement(By.cssSelector(".card-carousel-card-title")).getText().split(" ")[0];
                if (SimpleUtils.isNumeric(numOfLocations)){
                    result.put("NumOfLocations", Float.valueOf(numOfLocations));
                } else {
                    SimpleUtils.fail("Location count in title fail to load!", false);
                }
            } else {
                SimpleUtils.fail("Location Summary smart title diaplays incorrectly!", false);
            }
            if (SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(0).getText().replace(",","")) && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(2).getText().replace(",",""))){
                result.put(locationSummary.findElements(By.cssSelector("text")).get(1).getText(),
                        Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(0).getText().replace(",","")));
                result.put(locationSummary.findElements(By.cssSelector("text")).get(3).getText(),
                        Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(2).getText().replace(",","")));
            } else {
                SimpleUtils.fail("Budget hours and Published hours display incorrectly!", false);
            }
            if (locationSummary.findElements(By.cssSelector("text")).size()==6
                    && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",",""))){
                result.put(locationSummary.findElements(By.cssSelector("text")).get(5).getText(), Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",","")));
                if (locationSummary.findElements(By.cssSelector("text")).get(5).getText().contains("▼")){
                    if (locationSummary.findElements(By.cssSelector("text")).get(5).getAttribute("fill").contains("#50b83c")){
                        SimpleUtils.pass("The color of the value is correct! -> green");
                    } else {
                        SimpleUtils.fail("The color of the value is incorrect! ->not green", false);
                    }
                } else if (locationSummary.findElements(By.cssSelector("text")).get(5).getText().contains("▲")){
                    if (locationSummary.findElements(By.cssSelector("text")).get(5).getAttribute("fill").contains("#ff0000")){
                        SimpleUtils.pass("The color of the value is correct! -> red");
                    } else {
                        SimpleUtils.fail("The color of the value is incorrect! ->not red", false);
                    }
                }
            }
            if (locationSummary.findElements(By.cssSelector("text")).size()==8
                    && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",",""))
                    && SimpleUtils.isNumeric(locationSummary.findElements(By.cssSelector("text")).get(6).getText().replace(" Hrs","").replace(",",""))){
                result.put(locationSummary.findElements(By.cssSelector("text")).get(7).getText(), Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(6).getText().replace(" Hrs","").replace(",","")));
                result.put(locationSummary.findElements(By.cssSelector("text")).get(5).getText(), Float.valueOf(locationSummary.findElements(By.cssSelector("text")).get(4).getText().replace(" Hrs","").replace(",","")));

                if (locationSummary.findElements(By.cssSelector("text")).get(7).getText().contains("▼")){
                    if (locationSummary.findElements(By.cssSelector("text")).get(7).getAttribute("fill").contains("#50b83c")){
                        SimpleUtils.pass("The color of the value is correct! -> green");
                    } else {
                        SimpleUtils.fail("The color of the value is incorrect! ->not green", false);
                    }
                } else if (locationSummary.findElements(By.cssSelector("text")).get(7).getText().contains("▲")){
                    if (locationSummary.findElements(By.cssSelector("text")).get(7).getAttribute("fill").contains("#ff0000")){
                        SimpleUtils.pass("The color of the value is correct! -> red");
                    } else {
                        SimpleUtils.fail("The color of the value is incorrect! ->not red", false);
                    }
                }
            }
            if(weekType.toLowerCase().contains("current") || weekType.contains("previous")){
                if (isElementLoaded(locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")),10)
                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Scheduled Within Budget")
                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Scheduled Over Budget")
                        && getLocationSummaryDataFromSchedulePage().size() == 3){
                    String numOfProjectedWithin = getLocationSummaryDataFromSchedulePage().get(1).split(" ")[0];
                    String numOfProjectedOver = getLocationSummaryDataFromSchedulePage().get(2).split(" ")[0];
                    if (SimpleUtils.isNumeric(numOfProjectedWithin.replace(",","")) && SimpleUtils.isNumeric(numOfProjectedOver.replace(",",""))){
                        result.put("NumOfProjectedWithin", Float.valueOf(numOfProjectedWithin.replace(",","")));
                        result.put("NumOfProjectedOver", Float.valueOf(numOfProjectedOver.replace(",","")));
                    } else {
                        SimpleUtils.fail("Scheduled Location count in title fail to load!", false);
                    }
                    SimpleUtils.pass("Scheduled locations info load successfully!");
                } else {
                    SimpleUtils.fail("Scheduled locations info fail to load!", false);
                }
            } else {
                if (isElementLoaded(locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")),10)
                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Published Within Budget")
                        && locationSummary.findElement(By.cssSelector(".published-clocked-cols-summary")).getText().contains("Published Over Budget")
                        && getLocationSummaryDataFromSchedulePage().size() == 3){
                    String numOfProjectedWithin = getLocationSummaryDataFromSchedulePage().get(1).split(" ")[0];
                    String numOfProjectedOver = getLocationSummaryDataFromSchedulePage().get(2).split(" ")[0];
                    if (SimpleUtils.isNumeric(numOfProjectedWithin.replace(",","")) && SimpleUtils.isNumeric(numOfProjectedOver.replace(",",""))){
                        result.put("NumOfProjectedWithin", Float.valueOf(numOfProjectedWithin.replace(",","")));
                        result.put("NumOfProjectedOver", Float.valueOf(numOfProjectedOver.replace(",","")));
                    } else {
                        SimpleUtils.fail("Projected Location count in title fail to load!", false);
                    }
                    SimpleUtils.pass("Projected locations info load successfully!");
                } else {
                    SimpleUtils.fail("Projected locations info fail to load!", false);
                }
            }

        } else {
            SimpleUtils.fail("Location summary smart card fail to load!", false);
        }
        return result;
    }



    @FindBy(css = "div.analytics-new-table")
    private WebElement analyticsTableInScheduleDMViewPage;
    @Override
    public boolean isScheduleDMView() throws Exception {
        boolean result = false;
        if (isElementLoaded(analyticsTableInScheduleDMViewPage, 120)) {
            result = true;
        }
        return result;
    }



    @FindBy(css = "div.analytics-new-table-group-row-open")
    private List<WebElement> locationsInTheList;
    @Override
    public List<String> getLocationsInScheduleDMViewLocationsTable() throws Exception {
        waitForSeconds(3);
        List<String> locations = new ArrayList<String>();
        if (areListElementVisible(getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")),10)){
            for (int i=0; i< getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).size(); i++){
                locations.add(getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).get(i).findElement(By.cssSelector("img.analytics-new-table-location~span")).getText());
            }
        }
        return locations;
    }



    @FindBy(css = "div.analytics-new-table-header")
    private WebElement locationTableHeader;
    @Override
    public void verifySortByColForLocationsInDMView(int index) throws Exception {
        List<String> listString = new ArrayList<String>();
        List<Float> listFloat = new ArrayList<Float>();
        if (index > 0 && index <= getNumOfColInDMViewTable()){
            listString = getListByColInTimesheetDMView(index);
            if (locationTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).size()==getNumOfColInDMViewTable()){
                click(locationTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).get(index-1));
                if (locationTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).get(index-1).getAttribute("class").contains("sorter-up")){
                    if (transferStringToFloat(listString).size()==listString.size()){
                        listFloat = transferStringToFloat(listString).stream().sorted(Float::compareTo).collect(Collectors.toList());
                        if (Math.abs(transferStringToFloat(getListByColInTimesheetDMView(index)).get(listFloat.size()-1)-listFloat.get(listFloat.size()-1)) == 0){
                            SimpleUtils.pass("Sort result is correct!");
                        } else {
                            SimpleUtils.fail("Sort result is incorrect!", false);
                        }
                    } else {
                        listString = listString.stream().sorted(String::compareTo).collect(Collectors.toList());
                        if (getListByColInTimesheetDMView(index).get(0).equals(listString.get(0))){
                            SimpleUtils.pass("Sort result is correct!");
                        } else {
                            SimpleUtils.fail("Sort result is incorrect!", false);
                        }
                    }
                } else {
                    if (transferStringToFloat(listString).size()==listString.size()){
                        listFloat = transferStringToFloat(listString).stream().sorted(Float::compareTo).collect(Collectors.toList());
                        if (Math.abs(transferStringToFloat(getListByColInTimesheetDMView(index)).get(0)-listFloat.get(listFloat.size()-1)) == 0){
                            SimpleUtils.pass("Sort result is correct!");
                        } else {
                            SimpleUtils.fail("Sort result is incorrect!", false);
                        }
                    } else {
                        listString = listString.stream().sorted(String::compareTo).collect(Collectors.toList());
                        if (getListByColInTimesheetDMView(index).get(0).equals(listString.get(listString.size()-1))){
                            SimpleUtils.pass("Sort result is correct!");
                        } else {
                            SimpleUtils.fail("Sort result is incorrect!", false);
                        }
                    }
                }
            } else {
                SimpleUtils.fail("Columns are not loaded correctly!", false);
            }
        } else {
            SimpleUtils.fail("Index beyond range.", false);
        }
    }

    @Override
    public List<Float> transferStringToFloat(List<String> listString) throws Exception{
        List<Float> result = new ArrayList<Float>();
        boolean flag = true;
        for (String s : listString){
            if (!SimpleUtils.isNumeric(s)){
                flag = false;
                break;
            }
        }
        if (flag){
            for (String s : listString){
                result.add(Float.parseFloat(s));
            }
        }
        return result;
    }


    @Override
    public List<String> getListByColInTimesheetDMView(int index) throws Exception{
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).size(); i++){
            List<WebElement> columns = getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")).get(i).findElements(By.cssSelector(".ng-scope.col-fx-1"));
            if (index > 0 && index <= getNumOfColInDMViewTable() && columns.size()>=getNumOfColInDMViewTable()-1){
                if (index == 1){
                    list = getLocationsInScheduleDMViewLocationsTable();
                } else {
                    if (areListElementVisible(getDriver().findElements(By.cssSelector("div.analytics-new-table-group-row-open")),10)){
                        list.add(columns.get(index-2).getText().replace("%","").replace(",","").replace("N/A","0"));
                    }
                }
            } else {
                SimpleUtils.fail("Index beyond range.", false);
            }
        }
        return list;
    }

    private int getNumOfColInDMViewTable() throws Exception {
        int num = 0;
        if (isElementLoaded(locationTableHeader, 10)){
            num = locationTableHeader.getText().split("\n").length;
        } else {
            SimpleUtils.fail("Table header fail to load!", false);
        }
        return num;
    }

    @Override
    public void verifyClockedOrProjectedInDMViewTable(String expected) throws Exception {
        if (isElementLoaded(locationTableHeader, 10)){
            if (locationTableHeader.getText().toLowerCase().contains(expected.toLowerCase())){
                SimpleUtils.pass(expected + " displays!");
            } else {
                SimpleUtils.fail(expected + " doesn't display!", false);
            }
        } else {
            SimpleUtils.fail("Table header fail to load!", false);
        }
    }


    @Override
    public void verifySearchLocationInScheduleDMView(String location) throws Exception {
        boolean flag = true;
        waitForSeconds(5);
        if (isElementLoaded(getDriver().findElement(By.cssSelector(".analytics-new-table input[placeholder=\"Search\"]")),60)){
            getDriver().findElement(By.cssSelector(".analytics-new-table input[placeholder=\"Search\"]")).clear();
            getDriver().findElement(By.cssSelector(".analytics-new-table input[placeholder=\"Search\"]")).sendKeys(location);
            for (String s: getLocationsInScheduleDMViewLocationsTable()){
                flag = flag && s.contains(location);
            }
            if (flag){
                SimpleUtils.pass("Search result is correct!");
            } else {
                SimpleUtils.fail("Search result is incorrect!", false);
            }
        } else {
            SimpleUtils.fail("Search box is not loaded!", false);
        }
    }


    @Override
    public void clickOnLocationNameInDMView(String location) throws Exception {
        boolean flag = false;
        if (areListElementVisible(locationsInTheList, 15)) {
            for (WebElement element : locationsInTheList) {
                if (element.findElement(By.cssSelector("img.analytics-new-table-location~span")).getText().contains(location)) {
                    flag = true;
                    click(element.findElement(By.cssSelector("img.analytics-new-table-location~span")));
                    SimpleUtils.pass(location + "clicked!");
                    break;
                }
            }
            if (!flag) {
                SimpleUtils.fail("No this location: " + location, false);
            }
        } else {
            SimpleUtils.fail("No location displayed!", false);
        }
    }



    @Override
    public int getIndexOfColInDMViewTable(String colName) throws Exception {
        int index = 0;
        boolean colExist = false;
        if (isElementLoaded(locationTableHeader, 10)){
            for (String s: locationTableHeader.getText().replace("\n(Hrs)","").split("\n")){
                ++index;
                if (s.toLowerCase().contains(colName.toLowerCase())){
                    colExist = true;
                    break;
                }
            }
            if (!colExist) {
                index = 0;
            }
        } else {
            SimpleUtils.fail("Table header fail to load!", false);
        }
        return index;
    }


    @FindBy(css = "div.card-carousel-container")
    private WebElement cardContainerInDMView;
    @Override
    public HashMap<String, Integer> getValueOnUnplannedClocksSummaryCardAndVerifyInfo() throws Exception {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        if (isElementLoaded(cardContainerInDMView,20) && isElementLoaded(cardContainerInDMView.findElement(By.cssSelector("div[class*=\"card-carousel-card-analytics-card-color-\"]")),20)){
            List<String> strList = Arrays.asList(cardContainerInDMView.findElement(By.cssSelector("div[class*=\"card-carousel-card-analytics-card-color-\"]")).getText().split("\n"));
            if (strList.size()==4 && strList.get(1).toLowerCase().contains("unplanned") && strList.get(2).toLowerCase().contains("clocks") && SimpleUtils.isNumeric(strList.get(0)) && SimpleUtils.isNumeric(strList.get(3).replace(" total timesheets", ""))){
                result.put("unplanned clocks", Integer.parseInt(strList.get(0)));
                result.put("total timesheets", Integer.parseInt(strList.get(3).replace(" total timesheets", "")));
                SimpleUtils.pass("All info on Unplanned Clocks Summary Card is expected!");
            } else {
                SimpleUtils.fail("Info on Unplanned Clocks Summary Card is not expected!", false);
            }
        } else {
            SimpleUtils.fail("Unplanned clocks card fail to load!", false);
        }
        return result;
    }



    @Override
    public HashMap<String, Integer> getValueOnUnplannedClocksSmartCardAndVerifyInfo() throws Exception {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        waitForSeconds(3);
        if (isElementLoaded(cardContainerInDMView,20) && isElementLoaded(cardContainerInDMView.findElement(By.cssSelector("div.card-carousel-card-card-carousel-card-yellow-top")),20)){
            String info = cardContainerInDMView.findElement(By.cssSelector("div.card-carousel-card-card-carousel-card-yellow-top")).getText();
            List<String> strList = Arrays.asList(info.split("\n"));
            if (strList.size()==13 && strList.get(0).contains("UNPLANNED CLOCKS") && strList.get(2).contains("Early Clocks")&& strList.get(4).contains("Late Clocks")&& strList.get(6).contains("Incomplete Clocks")
                    && strList.get(8).contains("Missed Meal")&& strList.get(10).contains("No Show")&& strList.get(12).contains("Unscheduled")){
                SimpleUtils.pass("Title and info on Unplanned Clocks Smart Card are expected!");
                if (SimpleUtils.isNumeric(strList.get(1).replace(",", ""))
                        && SimpleUtils.isNumeric(strList.get(3).replace(",", ""))
                        && SimpleUtils.isNumeric(strList.get(5).replace(",", ""))
                        && SimpleUtils.isNumeric(strList.get(7).replace(",", ""))
                        && SimpleUtils.isNumeric(strList.get(9).replace(",", ""))
                        && SimpleUtils.isNumeric(strList.get(11).replace(",", ""))){
                    result.put(strList.get(2), Integer.parseInt(strList.get(1).replace(",", "")));
                    result.put(strList.get(4), Integer.parseInt(strList.get(3).replace(",", "")));
                    result.put(strList.get(6), Integer.parseInt(strList.get(5).replace(",", "")));
                    result.put(strList.get(8), Integer.parseInt(strList.get(7).replace(",", "")));
                    result.put(strList.get(10), Integer.parseInt(strList.get(9).replace(",", "")));
                    result.put(strList.get(12), Integer.parseInt(strList.get(11).replace(",", "")));
                } else {
                    SimpleUtils.fail("Datas on UNPLANNED CLOCKS smart card aren't numeric!", false);
                }
            } else {
                SimpleUtils.fail("Info on Unplanned Clocks smart Card is not expected!", false);
            }
        } else {
            SimpleUtils.fail("Unplanned clocks card fail to load!", false);
        }
        return result;
    }

    @Override
    public void clickSpecificLocationInDMViewAnalyticTable(String location) throws Exception {
        waitForSeconds(3);
        if (areListElementVisible(locationsInTheList,30)){
            for (WebElement element: locationsInTheList){
                if (location.equalsIgnoreCase(element.findElement(By.cssSelector("img.analytics-new-table-location~span")).getText())){
                    click(element);
                    SimpleUtils.pass(location + " is clicked!");
                    break;
                }
            }
        } else {
            SimpleUtils.fail("There is no location in the list!", false);
        }
    }

    @FindBy (css = ".day-week-picker-period-week")
    private List<WebElement> currentWeeks;

    @FindBy(className = "day-week-picker-arrow-right")
    private WebElement calendarNavigationNextWeekArrow;
    @Override
    public boolean hasNextWeek() throws Exception {
        int currentWeekIndex = -1;
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                String className = currentWeeks.get(i).getAttribute("class");
                if (className.contains("day-week-picker-period-active")) {
                    currentWeekIndex = i;
                }
            }
            if (currentWeekIndex == (currentWeeks.size() - 1) && !isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
                return false;
            }else {
                return true;
            }
        } else {
            return false;
        }
    }

    @FindBy (css = "tbody tr")
    private List<WebElement> smartCardRows;

    @Override
    public HashMap<String, String> getBudgetNScheduledHoursFromSmartCardOnDGEnv() throws Exception {
        HashMap<String, String> budgetNScheduledHours = new HashMap<>();
        if (areListElementVisible(smartCardRows, 5) && smartCardRows.size() != 0) {
            List<WebElement> ths = smartCardRows.get(0).findElements(By.tagName("th"));
            List<WebElement> tds = smartCardRows.get(1).findElements(By.tagName("td"));
            List<WebElement> td2s = smartCardRows.get(2).findElements(By.tagName("td"));
            if (ths != null && tds != null && ths.size() == 6 && tds.size() == 6) {
                for (int i =1 ;i< ths.size(); i++){
                    budgetNScheduledHours.put(tds.get(0).getText() + ths.get(i).getText(), tds.get(i).getText());
                    SimpleUtils.report("Smart Card: Get the hour: " + tds.get(0) + ths.get(i).getText() + " for: " + tds.get(i).getText());
                    budgetNScheduledHours.put(td2s.get(0).getText() + ths.get(i).getText(), td2s.get(i).getText());
                    SimpleUtils.report("Smart Card: Get the hour: " + td2s.get(0) + ths.get(i).getText() + " for: " + td2s.get(i).getText());
                }
            } else {
                SimpleUtils.fail("Schedule Week View Page: The format of the budget and Scheduled hours' smart card is incorrect!", false);
            }
        } else {
            SimpleUtils.fail("Schedule Week View Page: Budget and Scheduled smart card not loaded Successfully!", false);
        }
        return budgetNScheduledHours;
    }

    @FindBy(css = "div.analytics-new-table-group")
    private List<WebElement> DMtableRowCount;

    @FindBy(xpath = "//div[contains(@class,'analytics-new-table-group-row')]//span/img/following-sibling::span")
    private List<WebElement> locationName;

    @FindBy(css = ".sc-iLcRtq.eQbIAU")
    private List<WebElement> hoursOnDashboardPage;

    @FindBy(css = ".sc-cxFVwQ.hxUXAv")
    private List<WebElement> titleOnDashboardPage;

    @FindBy(xpath = "//*[name()='svg']//*[name()='text' and @text-anchor='end']")
    private List<WebElement> projectedOverBudget;

    @FindBy(css = "span.dms-box-item-unit-trend")
    private WebElement projectedWithinOrOverBudget;

    @Override
    public List<Float> validateScheduleAndBudgetedHours() throws Exception {
        HashMap<String,List<String>> budgetHours = new HashMap<>();
        HashMap<String,List<String>> publishHours = new HashMap<>();
        HashMap<String,List<String>> clockHours = new HashMap<>();
        List<Float> totalHoursFromSchTbl = new ArrayList<>();
        List<String> budgetHrs = new ArrayList<>();
        List<String> publishedHrs = new ArrayList<>();
        List<String> clockedHrs = new ArrayList<>();
        if(areListElementVisible(DMtableRowCount,10) && DMtableRowCount.size()!=0){
            for(int i=0; i<DMtableRowCount.size();i++){
                List<WebElement> divs = DMtableRowCount.get(i).findElements(By.cssSelector(".analytics-new-table-group-row-open div"));
                if(areListElementVisible(divs,10) && divs.size()!=0){
                    SimpleUtils.report("Budget Hours for " + locationName.get(i).getText() + " is : " + divs.get(3).getText());
                    SimpleUtils.report("Publish Hours for " + locationName.get(i).getText() + " is : " + divs.get(4).getText());
                    SimpleUtils.report("Clocked Hours for " + locationName.get(i).getText() + " is : " + divs.get(5).getText());
                    budgetHrs.add(divs.get(3).getText());
                    publishedHrs.add(divs.get(3).getText());
                    clockedHrs.add(divs.get(3).getText());
                    budgetHours.put("Budgeted Hours",budgetHrs);
                    publishHours.put("Published Hours",publishedHrs);
                    clockHours.put("Clocked Hours",clockedHrs);
                }
            }
            Float totalBudgetHoursFromSchTbl = calculateTotalHoursFromScheduleTable(budgetHours);
            Float totalPublishedHoursFromSchTbl = calculateTotalHoursFromScheduleTable(publishHours);
            Float totalClockedHoursFromSchTbl = calculateTotalHoursFromScheduleTable(clockHours);
            totalHoursFromSchTbl.add(totalBudgetHoursFromSchTbl);
            totalHoursFromSchTbl.add(totalPublishedHoursFromSchTbl);
            totalHoursFromSchTbl.add(totalClockedHoursFromSchTbl);

        }else{
            SimpleUtils.fail("No data available on Schedule table in DM view",false);
        }

        return totalHoursFromSchTbl;
    }

    public Float calculateTotalHoursFromScheduleTable(HashMap<String,List<String>> hoursCalulationFromSchTbl){
        Float totalActualHours = 0.0f;
        Float totalActualHoursFromSchTbl = 0.0f;
        for (Map.Entry<String, List<String>> entry : hoursCalulationFromSchTbl.entrySet()) {
            String key = entry.getKey();

            List<String> value = entry.getValue();
            for(String aString : value){
                totalActualHours = Float.parseFloat(aString.replace(",",""));
                totalActualHoursFromSchTbl = totalActualHoursFromSchTbl + totalActualHours;
            }
        }
        return totalActualHoursFromSchTbl;
    }

    @Override
    public void compareHoursFromScheduleAndDashboardPage(List<Float> totalHoursFromSchTbl) throws Exception{

        List<Float> totalHoursFromDashboardTbl = new ArrayList<>();
        if(areListElementVisible(hoursOnDashboardPage,10) && hoursOnDashboardPage.size()!=0){
            for(int i =0; i < hoursOnDashboardPage.size();i++){
                totalHoursFromDashboardTbl.add(Float.parseFloat(hoursOnDashboardPage.get(i).getText().replace(",","")));
            }
            for(int j=0; j < totalHoursFromSchTbl.size();j++){
                if(totalHoursFromSchTbl.get(j).equals(totalHoursFromDashboardTbl.get(j))){
                    SimpleUtils.pass(titleOnDashboardPage.get(j).getText() +
                            " Hours from Dashboard page " + totalHoursFromDashboardTbl.get(j)
                            + " matching with the hours present on Schedule Page " + totalHoursFromSchTbl.get(j));
                }else{
                    SimpleUtils.fail(titleOnDashboardPage.get(j).getText() +
                            " Hours from Dashboard page " + totalHoursFromDashboardTbl.get(j)
                            + " not matching with the hours present on Schedule Page " + totalHoursFromSchTbl.get(j),true);
                }
            }
        }else{
            SimpleUtils.fail("No data available for Hours on Dashboard page in DM view",false);
        }
    }

    public float getProjectedOverBudget(){
        float totalCountProjectedOverBudget = 0.0f;
        if(areListElementVisible(projectedOverBudget,10) && projectedOverBudget.size()!=0){
            for(int i=0;i<projectedOverBudget.size();i++){
                float countProjectedOverBudget = Float.parseFloat(projectedOverBudget.get(i).getText());
                totalCountProjectedOverBudget = totalCountProjectedOverBudget + countProjectedOverBudget;
            }
        }else{
            SimpleUtils.fail("No data available for Projected Over Budget section on location specific date in DM view",false);
        }
        return totalCountProjectedOverBudget;
    }

    @Override
    public void compareHoursFromScheduleSmartCardAndDashboardSmartCard(List<Float> totalHoursFromSchTbl) throws Exception{

        List<Float> totalHoursFromDashboardTbl = new ArrayList<>();
        if(areListElementVisible(hoursOnDashboardPage,10) && hoursOnDashboardPage.size()!=0){
            for(int i =0; i < hoursOnDashboardPage.size();i++){
                totalHoursFromDashboardTbl.add(Float.parseFloat(hoursOnDashboardPage.get(i).getText().replace(",","")));
            }
            for(int j=0; j < totalHoursFromSchTbl.size();j++){
                if(totalHoursFromSchTbl.get(j).equals(totalHoursFromDashboardTbl.get(j))){
                    SimpleUtils.pass(titleOnDashboardPage.get(j).getText().replace(",","") +
                            " Hours from Dashboard page " + totalHoursFromDashboardTbl.get(j)
                            + " matching with the hours present on Schedule Page " + totalHoursFromSchTbl.get(j));
                }
            }
        }else{
            SimpleUtils.fail("No data available for Hours on Dashboard page in DM view",false);
        }
    }

    @Override
    public void compareProjectedWithinBudget(float totalCountProjectedOverBudget) throws Exception{
        if(isElementLoaded(projectedWithinOrOverBudget,10)){
            String ProjectedWithinOrOverBudget = (projectedWithinOrOverBudget.getText().split(" "))[0];
            if(totalCountProjectedOverBudget == Float.parseFloat(ProjectedWithinOrOverBudget)){
                SimpleUtils.pass("Count of Projected Over/Under Budget on Dashboard page" +
                        " " + Float.parseFloat(ProjectedWithinOrOverBudget) + " is same as Schedule page " + totalCountProjectedOverBudget);
            }else{
                SimpleUtils.fail("Count of Projected Over/Under Budget on Dashboard page" +
                        " " + Float.parseFloat(ProjectedWithinOrOverBudget) + " not matching with Schedule page " + totalCountProjectedOverBudget,false);
            }
        }else{
            SimpleUtils.fail("No data available for Projected Over/Under Budget section on Dashboard in DM view",false);
        }

    }

    public void verifyP2PSchedulesTableHeaderNames(boolean isApplyBudget) throws Exception {
        if(areListElementVisible(schedulesTableHeaders, 10) && schedulesTableHeaders.size() == 8){
            String[] schedulesTableHeaderNames;
            if(isApplyBudget){
                    schedulesTableHeaderNames = new String[]{"Peer Locations", "Published Status", "Score",
                            "Budget Hrs", "Scheduled Hrs", "Projected Hrs", "Budget Variance", ""};
            } else {
                    schedulesTableHeaderNames = new String[]{"Peer Locations", "Published Status", "Score",
                            "Guidance Hrs", "Scheduled Hrs", "Projected Hrs", "Guidance Variance", ""};
            }
            for(int i= 0;i<schedulesTableHeaders.size(); i++){
                if(schedulesTableHeaders.get(i).getText().equals(schedulesTableHeaderNames[i])){
                    SimpleUtils.pass("Schedule table header: " + schedulesTableHeaders.get(i).getText()+" display correctly! ");
                } else
                    SimpleUtils.fail("Schedule table header: " + schedulesTableHeaderNames[i] +" display incorrectly! ", false);
            }
        } else
            SimpleUtils.fail("Schedules Table Headers on Schedule DM view loaded fail! ", false);
    }


    @Override
    public boolean checkIfLocationExistOnDMViewAnalyticsTable(String locationName){
        boolean isLocationExist = false;
        if(areListElementVisible(schedulesInDMView, 10) && schedulesInDMView.size()!=0){
            for (WebElement scheduleInDMView:schedulesInDMView){
                String scheduleLocationName = scheduleInDMView.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_UNTOUCHED\"]")).getText();
                if(scheduleLocationName.contains(locationName)){
                    isLocationExist = true;
                    SimpleUtils.pass("Click the schedule of "+ locationName+ " successfully! ");
                    break;
                }
            }
        }  else{
            SimpleUtils.fail("Click schedule in DM View failed, there is no schedules display in DM view" , false);
        }
        return isLocationExist;
    }
}
