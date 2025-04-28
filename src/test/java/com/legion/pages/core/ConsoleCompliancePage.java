package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.CompliancePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleCompliancePage extends BasePage implements CompliancePage {
    public ConsoleCompliancePage() {
        PageFactory.initElements(getDriver(), this);
    }
    @FindBy(css = "div.analytics-new-violations-count.ng-binding")
    private WebElement totalViolationHrs;
    String complianceHeaderLabel = "Compliance";

    public String getTheTotalViolationHrsFromSmartCard() throws Exception {
        String hrsOfTotalViolation = "";
        if (isElementLoaded(totalViolationHrs, 15)){
            hrsOfTotalViolation = totalViolationHrs.getText();
            SimpleUtils.pass("Get the total violation hrs successfully");
        } else {
            SimpleUtils.fail("Total violation hours not loaded successfully", false);
        }
        return hrsOfTotalViolation;
    }

    // Added By Julie
    @FindBy(className = "analytics-card-color-text-4")
    private WebElement totalLocations;

    @Override
    public String getTheTotalLocationsFromSmartCard() throws Exception {
        String numberOfTotalLocations = "";
        if (isElementLoaded(totalLocations, 5)) {
            numberOfTotalLocations = totalLocations.getText().trim();
            if (!numberOfTotalLocations.isEmpty()) {
                SimpleUtils.pass("Compliance Page: Get the total locations: \"" + numberOfTotalLocations + "\" on DM View smart card successfully");
            } else {
                SimpleUtils.fail("Compliance Page: Failed to get the total locations on DM View smart card", false);
            }
        } else {
            SimpleUtils.fail("Compliance Page: Total locations not loaded", false);
        }
        return numberOfTotalLocations;
    }

    @FindBy(className = "analytics-card-color-text-1")
    private WebElement totalLocationsWithViolations;

    @Override
    public String getTheTotalLocationsWithViolationsFromSmartCard() throws Exception {
        String numberOfTotalLocationsWithViolations = "";
        if (isElementLoaded(totalLocationsWithViolations, 5)) {
            numberOfTotalLocationsWithViolations = totalLocationsWithViolations.getText().trim();
            if (!numberOfTotalLocationsWithViolations.isEmpty()) {
                SimpleUtils.pass("Compliance Page: Get the total locations with violations: \"" + numberOfTotalLocationsWithViolations + "\" on DM View smart card successfully");
            } else {
                SimpleUtils.fail("Compliance Page: Failed to get the total locations with violations on DM View smart card", false);
            }
        } else {
            SimpleUtils.fail("Compliance Page: Total locations not loaded", false);
        }
        return numberOfTotalLocationsWithViolations;
    }

    @Override
    public List<String> getComplianceViolationsOnSmartCard() throws Exception {
        List<String> complianceViolationsOnDMViewSmartCard = new ArrayList<>();
        String totalHrs = getTheTotalViolationHrsFromSmartCard();
        totalHrs = totalHrs.contains(" ")? totalHrs.split(" ")[0]:totalHrs;
        complianceViolationsOnDMViewSmartCard.add(totalHrs);
        SimpleUtils.report("Compliance Page: Get the total Hrs: \"" + totalHrs + "\" on smart card successfully");
        String violations = getTheTotalLocationsWithViolationsFromSmartCard();
        complianceViolationsOnDMViewSmartCard.add(violations);
        SimpleUtils.report("Compliance Page: Get the total violations: \"" + violations + "\" on smart card successfully");
        String totalLocations = getTheTotalLocationsFromSmartCard();
        totalLocations = totalLocations.contains(" ")? totalLocations.split(" ")[0]:totalLocations;
        complianceViolationsOnDMViewSmartCard.add(totalLocations);
        SimpleUtils.report("Compliance Page: Get the total locations/districts/regions: \"" + totalLocations + "\" on smart card successfully");
        return complianceViolationsOnDMViewSmartCard;
    }

    @Override
    public boolean isRefreshButtonDisplay() throws Exception {
        if (isElementLoaded(refreshButton,60))
            return true;
        else
            return false;
    }

    @FindBy(css = "[ng-click=\"$ctrl.onReload(true)\"]")
    private WebElement refreshButton;

    @FindBy(css = "[ng-if=\"$ctrl.minutes >= 0 && $ctrl.date && !$ctrl.loading\"]")
    private WebElement lastUpdatedIcon;

    @FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes === 0\"]")
    private WebElement justUpdated;

    @FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes > 0\"]")
    private WebElement lastUpdated;

    @FindBy (css = "last-updated-countdown span[ng-if^=\"$ctrl.minutes > 0\"] span")
    private WebElement lastUpdatedMinutes;

    @FindBy (css = ".console-navigation-item-label.Compliance")
    private WebElement complianceConsoleMenu;

    @FindBy (css = ".console-navigation-item-label.Schedule")
    private WebElement scheduleConsoleMenu;

    @FindBy (css = ".analytics-new.ng-scope")
    private WebElement complianceSection;

    @FindBy(css = "div.analytics-new-table-group-row-open")
    private List<WebElement> rowsInAnalyticsTable;

    @FindBy (css = ".console-navigation-item-label.Timesheet")
    private WebElement timesheetConsoleMenu;

    @FindBy (css = ".day-week-picker-period-week")
    private List<WebElement> currentWeeks;

    @FindBy(className = "day-week-picker-arrow-right")
    private WebElement calendarNavigationNextWeekArrow;

    @FindBy(className = "day-week-picker-arrow-left")
    private WebElement calendarNavigationPreviousWeekArrow;

    @Override
    public void clickOnRefreshButton() throws Exception {
        if (isElementLoaded(refreshButton, 10)) {
            clickTheElement(refreshButton);
            if(isElementLoaded(lastUpdatedIcon, 120)){
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
                    SimpleUtils.pass("Compliance Page: Refresh button shows near week section successfully");
                } else {
                    SimpleUtils.fail("Compliance Page: Refresh button is not above welcome section", true);
                }
            } else {
                SimpleUtils.fail("Compliance Page: Refresh button isn't present", true);
            }
        } else {
            SimpleUtils.fail("Compliance Page: Refresh button failed to load", true);
        }
    }

    @Override
    public void validateRefreshFunction() throws Exception {
        int minutes = 0;
        if (isElementLoaded(lastUpdatedMinutes,10) ) {
            minutes = lastUpdatedMinutes.getText().contains(" ")? Integer.valueOf(lastUpdatedMinutes.getText().split(" ")[0]):0;
            if (minutes >= 30 ) {
                if (lastUpdatedMinutes.getAttribute("class").contains("last-updated-countdown-time-orange"))
                    SimpleUtils.pass("Compliance Page: When the Last Updated time >= 30 mins, the color changes to orange");
                else
                    SimpleUtils.fail("Compliance Page: When the Last Updated time >= 30 mins, the color failed to change to orange",false);
            }
        }
        if (isElementLoaded(refreshButton, 10)) {
            clickTheElement(refreshButton);
            SimpleUtils.pass("Compliance Page: Click on Refresh button Successfully!");
            if (complianceSection.getAttribute("class").contains("analytics-new-refreshing") && refreshButton.getAttribute("label").equals("Refreshing...")) {
                SimpleUtils.pass("Compliance Page: After clicking Refresh button, the background is muted and it shows an indicator 'Refreshing...' that we are processing the info");
                if (isElementLoaded(justUpdated,60) && complianceSection.getAttribute("class").contains("home-dashboard-loading"))
                    SimpleUtils.pass("Compliance Page: Once the data is done refreshing, the page shows 'JUST UPDATED' and page becomes brighter again");
                else
                    SimpleUtils.warn("SCH-2857: [DM View] DURATION.X-MINS displays instead of actual refresh time stamp");
                    // SimpleUtils.fail("Compliance Page: When the data is done refreshing, the page doesn't show 'JUST UPDATED' and page doesn't become brighter again",false);
                if (isElementLoaded(lastUpdated,60) && lastUpdatedMinutes.getAttribute("class").contains("last-updated-countdown-time-blue"))
                    SimpleUtils.pass("Compliance Page: The Last Updated info provides the minutes last updated in blue");
                else
                    SimpleUtils.warn("SCH-2857: [DM View] DURATION.X-MINS displays instead of actual refresh time stamp");
                    // SimpleUtils.fail("Compliance Page: The Last Updated info doesn't provide the minutes last updated in blue",false);
            } else {
                SimpleUtils.fail("Compliance Page: After clicking Refresh button, the background isn't muted and it doesn't show 'Refreshing...'",true);
            }
        } else {
            SimpleUtils.fail("Compliance Page: Refresh button not Loaded!", true);
        }
    }

    @Override
    public void validateRefreshPerformance() throws Exception {
        if (isElementLoaded(refreshButton, 10)) {
            clickTheElement(refreshButton);
            if (refreshButton.getAttribute("label").equals("Refreshing...")) {
                SimpleUtils.pass("Compliance Page: After clicking Refresh button, the button becomes 'Refreshing...'");
                WebElement element = (new WebDriverWait(getDriver(), 60))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[label=\"Refresh\"]")));
                if (element.isDisplayed()) {
                    SimpleUtils.pass("Compliance Page: Page refreshes within 1 minute successfully");
                } else {
                    SimpleUtils.fail("Compliance Page: Page doesn't refresh within 1 minute", false);
                }
            } else {
                SimpleUtils.fail("Compliance Page: After clicking Refresh button, the background isn't muted and it doesn't show 'Refreshing...'",true);
            }
        } else {
            SimpleUtils.fail("Compliance Page: Refresh button not Loaded!", true);
        }
    }

    @Override
    public void validateRefreshTimestamp() throws Exception {
        String timestamp = "";
        if (isElementLoaded(justUpdated, 5)) {
            SimpleUtils.pass("Compliance Page: The page just refreshed");
        } else if (isElementLoaded(lastUpdatedMinutes, 5)) {
            timestamp = lastUpdatedMinutes.getText();
            if (timestamp.contains("HOUR") && timestamp.contains(" ")) {
                timestamp = timestamp.split(" ")[0];
                if (Integer.valueOf(timestamp) == 1)
                    SimpleUtils.pass("Compliance Page: The backstop is 1 hour so that the data is not older than 1 hour stale");
                else
                    // SimpleUtils.fail("Schedule Page: The backstop is older than 1 hour stale",false);
                    SimpleUtils.warn("SCH-2589: [DM View] Refresh time is older than 1 hour stale");
            } else if (timestamp.contains("MINS") && timestamp.contains(" ")) {
                timestamp = timestamp.split(" ")[0];
                if (Integer.valueOf(timestamp) < 60 && Integer.valueOf(timestamp) >= 1)
                    SimpleUtils.pass("Compliance Page: The backstop is last updated " + timestamp + " mins ago");
                else
                    SimpleUtils.fail("Compliance Page: The backup is last updated " + timestamp + " mins ago actually", false);
            } else
                // SimpleUtils.fail("Compliance Page: The backup display \'" + lastUpdated.getText() + "\'",false);
                SimpleUtils.warn("SCH-2857: [DM View] DURATION.X-MINS displays instead of actual refresh time stamp");
        } else
            SimpleUtils.fail("Compliance Page: Timestamp failed to load", false);
    }

    @Override
    public void navigateToSchedule() throws Exception {
        if (isElementLoaded(scheduleConsoleMenu, 10)) {
            click(scheduleConsoleMenu);
            if (scheduleConsoleMenu.findElement(By.xpath("./..")).getAttribute("class").contains("active"))
                SimpleUtils.pass("Schedule Page: Click on Schedule console menu successfully");
            else
                SimpleUtils.fail("Schedule Page: It doesn't navigate to Schedule console menu after clicking", false);
        } else {
            SimpleUtils.fail("Schedule menu in left navigation is not loaded!",false);
        }
    }

    @Override
    public void clickOnComplianceConsoleMenu() throws Exception {
        if(isElementLoaded(complianceConsoleMenu,20)) {
            click(complianceConsoleMenu);
            if (complianceConsoleMenu.findElement(By.xpath("./..")).getAttribute("class").contains("active"))
                SimpleUtils.pass("Compliance Page: Click on Compliance console menu successfully");
            else
                SimpleUtils.fail("Compliance Page: It doesn't navigate to Compliance console menu after clicking", false);
        } else
            SimpleUtils.fail("Compliance Console Menu not loaded Successfully!", false);
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
            SimpleUtils.fail("Compliance Page: Timestamp failed to load", false);
        navigateToSchedule();
        clickOnComplianceConsoleMenu();
        if (isElementLoaded(lastUpdated, 5)) {
            timestamp2 = lastUpdated.getText();
        } else if (isElementLoaded(justUpdated, 5)) {
            timestamp2 = justUpdated.getText();
        } else
            SimpleUtils.fail("Compliance Page: Timestamp failed to load", false);
        if (timestamp2.equals(timestamp1) && !timestamp1.equals("") && !refreshButton.getAttribute("label").equals("Refreshing...")) {
            SimpleUtils.pass("Compliance Page: It keeps the previous Last Updated time, not refreshing every time");
        } else {
            SimpleUtils.fail("Compliance Page: It doesn't keep the previous Last Updated time", false);
        }
    }

    @Override
    public void navigateToPreviousWeek() throws Exception {
        int currentWeekIndex = -1;
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                String className = currentWeeks.get(i).getAttribute("class");
                if (className.contains("day-week-picker-period-active")) {
                    currentWeekIndex = i;
                }
            }
            if (currentWeekIndex == 0 && isElementLoaded(calendarNavigationPreviousWeekArrow, 5)) {
                clickTheElement(calendarNavigationPreviousWeekArrow);
                if (areListElementVisible(currentWeeks, 5)) {
                    clickTheElement(currentWeeks.get(currentWeeks.size()-1));
                    SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeeks.size()-1).getText() + "' Successfully!");
                }
            } else {
                clickTheElement(currentWeeks.get(currentWeekIndex - 1));
                SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeekIndex - 1).getText() + "' Successfully!");
            }
        } else {
            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
        }
    }

    @Override
    public void navigateToNextWeek() throws Exception {
        int currentWeekIndex = -1;
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                String className = currentWeeks.get(i).getAttribute("class");
                if (className.contains("day-week-picker-period-active")) {
                    currentWeekIndex = i;
                }
            }
            if (currentWeekIndex == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
                clickTheElement(calendarNavigationNextWeekArrow);
                if (areListElementVisible(currentWeeks, 5)) {
                    clickTheElement(currentWeeks.get(0));
                    SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(0).getText() + "' Successfully!");
                }
            } else {
                clickTheElement(currentWeeks.get(currentWeekIndex + 1));
                SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(currentWeekIndex + 1).getText() + "' Successfully!");
            }
        } else {
            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
        }
    }

    @Override
    public List<String> getDataFromComplianceTableForGivenLocationInDMView(String location) throws Exception {
        List<String> complianceViolationsOnDMViewSmartCard = new ArrayList<>();
        boolean isLocationFound = false;
        if (areListElementVisible(rowsInAnalyticsTable,50)) {
            for (WebElement row: rowsInAnalyticsTable) {
                if (row.findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_UNTOUCHED\"]")).getText().equals(location)) {
                    isLocationFound = true;
                    List<WebElement> dataElements = row.findElements(By.cssSelector(".ng-scope.col-fx-1"));
                    for (WebElement dataElement: dataElements) {
                        if (!dataElement.getAttribute("class").contains("ng-hide") && dataElement.getText() != null)
                            complianceViolationsOnDMViewSmartCard.add(dataElement.getText());
                    }
                    break;
                }
            }
        } else
            SimpleUtils.fail("Compliance Page: There are no locations in current district or failed to load",false);
        if (isLocationFound)
            SimpleUtils.pass("Compliance Page: Find the location " + location + " successfully");
        else
            SimpleUtils.fail("Compliance Page: Failed to find the location, try another location again",false);
        return complianceViolationsOnDMViewSmartCard;
    }

    @FindBy(css = "div.header-navigation-label")
    private WebElement compliancePageHeaderLabel;

    @Override
    public boolean isCompliancePageLoaded() throws Exception {
        if(isElementLoaded(compliancePageHeaderLabel,10)){
            String s = compliancePageHeaderLabel.getText();
            if(compliancePageHeaderLabel.getText().toLowerCase().contains(complianceHeaderLabel.toLowerCase())) {
                SimpleUtils.pass("Compliance Page is loaded Successfully!");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLocationInCompliancePageClickable() throws Exception {
        boolean isLocationClickable = true;
        WebElement element = (new WebDriverWait(getDriver(), 60))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[label=\"Refresh\"]")));
        if (element.isDisplayed()) {
            SimpleUtils.pass("Compliance Page: Page refreshes within 1 minute successfully");
            if (areListElementVisible(rowsInAnalyticsTable,10)) {
                for (WebElement row: rowsInAnalyticsTable) {
                    click(row);
                    if (row.getCssValue("cursor").contains("pointer"))
                        break;
                    else
                        isLocationClickable = false;
                }
            }
        } else {
            SimpleUtils.fail("Compliance Page: Page doesn't refresh within 1 minute", false);
        }
        return isLocationClickable;
    }

    @FindBy(css = "div.card-carousel-fixed")
    private WebElement totalViolationCardInDMView;
    @FindBy(css = "div.card-carousel-container")
    private WebElement cardContainerInDMView;
    @Override
    public HashMap<String, Float> getValuesAndVerifyInfoForTotalViolationSmartCardInDMView() throws Exception {
        HashMap<String, Float> result = new HashMap<String, Float>();
        if (isElementLoaded(totalViolationCardInDMView,15) &&
                isElementLoaded(totalViolationCardInDMView.findElement(By.cssSelector("div.card-carousel-card-primary-small")),15)){
             List<String> strList = Arrays.asList(totalViolationCardInDMView.findElement(By.cssSelector("div.card-carousel-card-primary-small")).getText().split("\n"));
            if (strList.size()==5 && strList.get(0).contains("TOTAL VIOLATION HRS")
                    && strList.get(strList.size()-1).contains("Last Week")
                    && SimpleUtils.isNumeric(strList.get(1).replace("Hrs","").replace("Hr","").replace(",",""))
                    && SimpleUtils.isNumeric(strList.get(2).replace("Hrs","").replace("Hr","").replace(",",""))
                    && SimpleUtils.isNumeric(strList.get(4).replace("Hrs Last Week","").replace("Hr Last Week","").replace(",",""))){
                result.put("vioHrsCurrentWeek",Float.parseFloat(strList.get(1).replace("Hrs","").replace("Hr","").replace(",","")));
                result.put("diffHrs", Float.parseFloat(strList.get(2).replace("Hrs","").replace("Hr","").replace(",","")));
                result.put("vioHrsPastWeek",Float.parseFloat(strList.get(4).replace("Hrs Last Week","").replace("Hr Last Week","").replace(",","")));
                SimpleUtils.pass("All info on total violation smart card is displayed!");
            } else {
                SimpleUtils.fail("Info on total violation smart card is not expected!", false);
            }
        } else {
            SimpleUtils.fail("Total violation smart card fail to load!", false);
        }
        return result;
    }

    @Override
    public void verifyDiffFlag(String upOrDown) throws Exception{
        if ( isElementLoaded(totalViolationCardInDMView.findElement(By.cssSelector("div.published-clocked-cols-summary-arrow")),10)){
            if (totalViolationCardInDMView.findElement(By.cssSelector("div.published-clocked-cols-summary-arrow")).getAttribute("class").contains(upOrDown)){
                SimpleUtils.pass("Diff flag displays correctly");
            } else {
                SimpleUtils.fail("Diff flag displays incorrectly", false);
            }
        } else {
            SimpleUtils.fail("Diff flag doesn't show up!", false);
        }
    }



    @FindBy(css = "div.card-carousel-card-analytics-card-color-red")
    private WebElement upperFieldWithViolationsSmartCard;
    @Override
    public HashMap<String, Integer> getValueOnLocationsWithViolationCardAndVerifyInfo(String upperFieldType) throws Exception {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        if (isElementLoaded(cardContainerInDMView,10) && isElementLoaded(upperFieldWithViolationsSmartCard,60)){
            List<String> strList = Arrays.asList(upperFieldWithViolationsSmartCard.getText().split("\n"));
            if (strList.size()==4
                    && strList.get(1).contains(upperFieldType)
                    && strList.get(2).contains("with Violations")
                    && SimpleUtils.isNumeric(strList.get(0))
                    && SimpleUtils.isNumeric(strList.get(3).split(" ")[0])){
                result.put("UpperFieldsWithViolations" , Integer.parseInt(strList.get(0)));
                result.put("TotalUpperFields", Integer.parseInt(strList.get(3).split(" ")[0]));
                SimpleUtils.pass("All info on Locations With Violation Card is expected!");
            } else {
                SimpleUtils.fail("Info on Locations With Violation Card is not expected!", false);
            }
        } else {
            result.put("UpperFieldsWithViolations" ,0);
            result.put("TotalUpperFields", 0);
            SimpleUtils.report("Locations With Violation Card fail to load!");
        }
        return result;
    }

    @Override
    public HashMap<String, Float> getViolationHrsFromTop1ViolationCardAndVerifyInfo() throws Exception {
        HashMap<String, Float> result = new HashMap<String, Float>();
        if (isElementLoaded(cardContainerInDMView,10) && isElementLoaded(cardContainerInDMView.findElement(By.cssSelector("div.card-carousel-card-card-carousel-card-yellow-top")),10)){
            String infoForEveryViolation = null;
            String keyTemp = "";
            List<WebElement> violations = cardContainerInDMView.findElements(By.cssSelector("div.analytics-new-common-violations-row div[ng-repeat]"));
            String cardTitle = cardContainerInDMView.findElement(By.cssSelector("div.card-carousel-card-card-carousel-card-yellow-top div.card-carousel-card-title")).getText();
            SimpleUtils.assertOnFail("The number of total violation is incorrect!", cardTitle.contains(String.valueOf(violations.size())), false);
            List<String> strList = new ArrayList<>();
            for (WebElement element: violations){
                keyTemp = "";
                infoForEveryViolation = element.getText();
                strList = Arrays.asList(infoForEveryViolation.split("\n"));
                for (int i = 0; i<strList.size()-1; i++){
                    keyTemp = keyTemp +" "+ strList.get(i);
                }
                String number = strList.get(strList.size()-1).endsWith("0") ? strList.get(strList.size()-1).substring(0,
                        strList.get(strList.size()-1).length() - 1) : strList.get(strList.size()-1);
                if (SimpleUtils.isNumeric(number)){
                    result.put(keyTemp.trim(), Float.valueOf(strList.get(strList.size()-1)));
                } else {
                    SimpleUtils.fail("The violation hrs count is not numeric, please check!", false);
                }
            }
        } else {
            SimpleUtils.fail("Unplanned clocks card fail to load!", false);
        }
        return result;
    }

    @Override
    public float getTopOneViolationHrsOrNumOfACol(List<Float> list) throws Exception {
        float result = 0;
        if (list.size()>0){
            for (float i : list) {
                result = result + i;
            }
        }
        return result;
    }

    @FindBy (className = "analytics-new-table-header")
    private WebElement analyticsTableHeader;

    @Override
    public void verifyFieldNamesInAnalyticsTable(String upperFieldType) throws Exception {
        /*It should include:
        - UpperField
        - Total Extra Hours
        - Overtime (Hrs)
        - Clopening (Hrs)
        - Missed Meal
        - Schedule Changed
        - Doubletime (Hrs)
        - Late Schedule?*/
        boolean isMatched = false;
        List<String> fieldNamesExpected = Arrays.asList(new String[]{upperFieldType, "Extra Hours", "Overtime", "Clopening", "Missed Meal", "Schedule Changed Premium", "Double Time", "Late Schedule"});
        if (isElementLoaded(analyticsTableHeader,10)) {
            List<WebElement> fields = analyticsTableHeader.findElements(By.xpath("./div"));
            for (WebElement field: fields) {
                if (!field.getAttribute("class").contains("ng-hide")) {
                    if (fieldNamesExpected.contains(field.getText().replace("\n"," "))) {
                        isMatched = true;
                        SimpleUtils.report("Compliance Page: Expected field name is \'" + field.getText().replace("\n"," ") + "\'");
                    } else {
                        isMatched = false;
                        SimpleUtils.report("Compliance Page: Unexpected field name is \'" + field.getText().replace("\n"," ") + "\'");
                        break;
                    }
                }
            }
            if (isMatched)
                SimpleUtils.pass("Compliance Page: The field names in analytics table are expected");
            else
                SimpleUtils.fail("Compliance Page: The field names in analytics table are unexpected",false);
        } else
            SimpleUtils.fail("Compliance Page: The header of analytics table failed to load",false);
    }

    @Override
    public void verifySortByColForLocationsInDMView(int index) throws Exception {
        List<String> listString = new ArrayList<String>();
        List<Float> listFloat = new ArrayList<Float>();
        if (index > 0 && index <= getNumOfColInDMViewTable()) {
            listString = getListByColInTimesheetDMView(index);
            if (analyticsTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).size()==getNumOfColInDMViewTable()){
                click(analyticsTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).get(index-1));
                if (analyticsTableHeader.findElements(By.cssSelector("i.analytics-new-table-header-sorter")).get(index-1).getAttribute("class").contains("sorter-up")){
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

    @FindBy (css = ".analytics-new-table-header div")
    private List<WebElement> analyticsTableHeaders;

    private int getNumOfColInDMViewTable() throws Exception {
        int num = 0;
        if (areListElementVisible(analyticsTableHeaders, 20)){
            num = analyticsTableHeaders.size();
        } else {
            SimpleUtils.fail("Table header fail to load!", false);
        }
        return num;
    }

    @Override
    public List<String> getListByColInTimesheetDMView(int index) throws Exception{
        List<String> list = new ArrayList<String>();
        if (areListElementVisible(rowsInAnalyticsTable, 10)) {
            for (WebElement element: rowsInAnalyticsTable){
                if (index > 0 && index <= getNumOfColInDMViewTable() && element.findElements(By.cssSelector(".ng-scope.col-fx-1")).size()>=getNumOfColInDMViewTable()-1){
                    if (index == 1){
                        list = getLocationsInScheduleDMViewLocationsTable();
                    } else {
                        if (areListElementVisible(rowsInAnalyticsTable,10)){
                            list.add(element.findElements(By.cssSelector(".ng-scope.col-fx-1")).get(index-2).getText().replace("%",""));
                        }
                    }
                } else {
                    SimpleUtils.fail("Index beyond range.", false);
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getLocationsInScheduleDMViewLocationsTable() throws Exception {
        waitForSeconds(3);
        List<String> locations = new ArrayList<String>();
        if (areListElementVisible(rowsInAnalyticsTable,10)){
            for (WebElement element: rowsInAnalyticsTable){
                locations.add(element.findElement(By.cssSelector("img.analytics-new-table-location~span")).getText());
            }
        }
        return locations;
    }

    @FindBy(css = "div.analytics-new-table")
    private WebElement analyticsTableInComplianceDMViewPage;

    @Override
    public boolean isComplianceUpperFieldView() throws Exception {
        boolean result = false;
        waitForSeconds(5);
        if (isElementLoaded(analyticsTableInComplianceDMViewPage, 20)) {
            result = true;
        }
        return result;
    }

    @FindBy(css = ".analytics-new-table-group-row-open [jj-switch-when=\"cells.CELL_UNTOUCHED\"] span")
    private List<WebElement> upperFieldNamesOnAnalyticsTable;

    @Override
    public List<String> getAllUpperFieldNamesOnAnalyticsTable() throws Exception {
        List<String> upperFieldNames = new ArrayList<>();
        if (areListElementVisible(upperFieldNamesOnAnalyticsTable, 20)) {
            for (WebElement upperFieldName: upperFieldNamesOnAnalyticsTable){
                upperFieldNames.add(upperFieldName.getText());
                SimpleUtils.pass("Add upper field name: "+ upperFieldName.getText() +" successfully! ");
            }
        } else
            SimpleUtils.fail("Upper field names fail to load on analytics table", false);
        return upperFieldNames;
    }

    @FindBy(css = ".analytics-new-table-group-row-open")
    private List<WebElement>  upperFieldsInDMView;

    @FindBy (css = "lg-search.analytics-new-table-filter input")
    private WebElement searchLocationInCompliancePage;

    @FindBy (css = ".analytics-new-table-group-row-open [jj-switch-when=\"cells.CELL_UNTOUCHED\"] span")
    private List<WebElement> upperFieldNames;


    /**
     * Description: To get all the upper field names (first column) in the table
     * @param
     * @return : Return the string list of the upper field names (first column) in the table
     *
     */

    @Override
    public List<String> getAllUpperFieldNames() {
        List<String> names = new ArrayList<>();
        if (areListElementVisible(upperFieldNames,10)) {
            for (WebElement upperFieldName : upperFieldNames) {
                names.add(upperFieldName.getText());
            }
        } else
            SimpleUtils.fail("The upper field names fail to load! ", false);
        return names;
    }

    public Map<String, String> getAllUpperFieldInfoFromComplianceDMViewByUpperField(String upperFieldName) throws Exception {
        Map<String, String> allUpperFieldInfo = new HashMap<>();
        boolean isUpperFieldMatched = false;
        if (isElementLoaded(searchLocationInCompliancePage,5)) {
            searchLocationInCompliancePage.sendKeys(upperFieldName);
            waitForSeconds(3);
            if (areListElementVisible(upperFieldsInDMView, 10) && upperFieldsInDMView.size() != 0) {
                for (int i = 0; i < upperFieldsInDMView.size(); i++) {
                    WebElement upperFieldInDMView = upperFieldsInDMView.get(i).findElement(By.cssSelector("[jj-switch-when=\"cells.CELL_UNTOUCHED\"] span"));
                    if (upperFieldInDMView != null) {
                        String uppeFieldNameInDMView = upperFieldInDMView.getText();
                        if (uppeFieldNameInDMView != null && uppeFieldNameInDMView.equals(upperFieldName)) {
                            isUpperFieldMatched = true;
                            //add schedule upperfield Name
                            allUpperFieldInfo.put("upperFieldName", uppeFieldNameInDMView);
                            //add Total Extra Hours
                            allUpperFieldInfo.put("totalExtraHours", upperFieldsInDMView.get(i).findElement(By.className("analytics-new-cell-as-input")).getText());
                            List<WebElement> upperFieldHeaders = upperFieldsInDMView.get(i).findElements(By.cssSelector("[class=\"ng-scope col-fx-1\"]"));
                            //add Total Overtime
                            allUpperFieldInfo.put("overtime", upperFieldHeaders.get(0).getText());
                            //add Clopening
                            allUpperFieldInfo.put("clopening", upperFieldHeaders.get(1).getText());
                            //add Missed Meal
                            allUpperFieldInfo.put("missedMeal", upperFieldHeaders.get(2).getText());
                            //add Schedule Changed
                            allUpperFieldInfo.put("scheduleChanged", upperFieldHeaders.get(3).getText());
                            //add Doubletime
                            allUpperFieldInfo.put("doubletime", upperFieldHeaders.get(4).getText());
                            //add Late Schedule
                            allUpperFieldInfo.put("lateSchedule", upperFieldHeaders.get(5).getText());
                        } else {
                            SimpleUtils.report("Get upperField info in DM View failed, there is no upperFields display in this upperFields");
                        }
                    }
                }
                if (!isUpperFieldMatched) {
                    SimpleUtils.fail("Get upperField info in DM View failed, there is no matched upperField display in DM view", false);
                } else {
                    SimpleUtils.pass("Get upperField info in DM View successful! ");
                }
            } else
                SimpleUtils.fail("Get upperField info in DM View failed, there is no upperField display in DM view", false);
            searchLocationInCompliancePage.clear();
        } else {
            SimpleUtils.fail("getDataInCompliancePage: search input fail to load!", true);
        }
        return allUpperFieldInfo;
    }

    public boolean isUpperFieldsWithViolationSmartCardDisplay () throws Exception {
        boolean isUpperFieldsWithViolationSmartCardDisplay = false;
        if (isElementLoaded(upperFieldWithViolationsSmartCard, 60)) {
            isUpperFieldsWithViolationSmartCardDisplay = true;
            SimpleUtils.report("The upperField with Violations smart card is display! ");
        } else {
            isUpperFieldsWithViolationSmartCardDisplay = false;
            SimpleUtils.report("The upperField with Violations smart card is display! ");
        }
        return isUpperFieldsWithViolationSmartCardDisplay;
    }

    @FindBy (css = "div.card-carousel-card-card-carousel-card-yellow-top")
    private WebElement top1ViolationsSmartCard;

    public boolean isTop1ViolationSmartCardDisplay () throws Exception {
        boolean isTop1ViolationSmartCardDisplay = false;
        if (isElementLoaded(top1ViolationsSmartCard, 60)) {
            isTop1ViolationSmartCardDisplay = true;
            SimpleUtils.report("The top 1 violation smart card is display! ");
        } else {
            isTop1ViolationSmartCardDisplay = false;
            SimpleUtils.report("The top 1 violation smart card is display! ");
        }
        return isTop1ViolationSmartCardDisplay;
    }

    @FindBy(css = "div.lg-toast")
    private WebElement successMsg;
    public void displaySuccessMessage() throws Exception {
        if (isElementLoaded(successMsg, 20) && successMsg.getText().contains("Success!")) {
            SimpleUtils.pass("Success message displayed successfully." + successMsg.getText());
            waitForSeconds(2);
        } else {
            SimpleUtils.report("Success pop up not displayed successfully.");
            waitForSeconds(3);
        }
    }

    @Override
    public void turnOnOrTurnOff7thConsecutiveOTToggle(boolean action) throws Exception {
        String content = getConsecutiveOTSettingContent();
        if (isElementLoaded(consecutiveOTSection, 10)
                && ((content.contains("An employee will receive Overtime Pay for hours worked on ")
                && content.contains("day within a single workweek"))
                || content.contains("In excess of how many hours an employee will receive Doubletime Pay for hours worked on 7th consecutive day within a single workweek?"))){
            if (isElementLoaded(consecutiveOTSection.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
                if (action && consecutiveOTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    scrollToElement(consecutiveOTSection.findElement(By.cssSelector(".lg-question-input__toggle")));
                    clickTheElement(consecutiveOTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned on!");
                } else if (!action && !consecutiveOTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    clickTheElement(consecutiveOTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned off!");
                } else {
                    SimpleUtils.pass("Toggle status is expected!");
                }
            } else {
                SimpleUtils.fail("Toggle fail to load!", false);
            }
        } else {
            SimpleUtils.fail("Consecutive OT section fail to load!", false);
        }
    }

    @FindBy(css = "[form-title=\"Overtime Pay\"] [question-title*=\"consecutive</b>\"]")
    private WebElement consecutiveOTSection;
    @FindBy(css = "[form-title=\"Overtime Pay\"] [ng-click*=\"openConsecutiveOvertimeDialog()\"]")
    private WebElement consecutiveOTEditBtn;
    @FindBy(css = ".modal-dialog")
    private WebElement moodalDialog;

    @Override
    public String getConsecutiveOTSettingContent() throws Exception{
        if (isElementLoaded(consecutiveOTSection, 10)){
            return consecutiveOTSection.findElement(By.cssSelector(".lg-question-input__text")).getText();
        }
        return "";
    }

    @Override
    public String getConsecutiveDTSettingContent() throws Exception{
        if (isElementLoaded(consecutiveDTSection, 10)){
            return consecutiveDTSection.findElement(By.cssSelector(".lg-question-input__text")).getText();
        }
        return "";
    }

    @Override
    public void editConsecutiveOTSetting(String daysCount, String condition, boolean saveOrNot) throws Exception {
        if (isElementLoaded(consecutiveOTEditBtn, 5)){
            String contentBefore = getConsecutiveOTSettingContent();
            clickTheElement(consecutiveOTEditBtn);
            if (isElementLoaded(moodalDialog, 20)){
                //check the title.
                if (moodalDialog.findElement(By.cssSelector(".lg-modal__title")).getText().trim().equalsIgnoreCase("Edit overtime settings")){
                    SimpleUtils.pass("Dialog title is expected!");
                } else {
                    SimpleUtils.fail("Dialog title is not correct", false);
                }
                //Check setting content.
                if(moodalDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("An employee will receive Overtime Pay for hours worked on")
                        &&moodalDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("consecutive day within a single workweek")){
                    SimpleUtils.pass("Setting content in the dialog is expected!");
                } else {
                    SimpleUtils.fail("Setting content is not expected!", false);
                }
                //edit the content, input the parameters.
                if (moodalDialog.findElements(By.cssSelector("select")).size() == 2){
                    selectByVisibleText(moodalDialog.findElements(By.cssSelector("select")).get(0), daysCount);
                    selectByVisibleText(moodalDialog.findElements(By.cssSelector("select")).get(1), condition);
                } else {
                    SimpleUtils.fail("Selects are not shown as expected!", false);
                }
                //save or cancel.
                if (isElementLoaded(moodalDialog.findElement(By.cssSelector("[label=\"Cancel\"]")), 5)
                        &&isElementLoaded(moodalDialog.findElement(By.cssSelector("[label=\"Save\"]")), 5)){
                    if (saveOrNot){
                        clickTheElement(moodalDialog.findElement(By.cssSelector("[label=\"Save\"] button")));
                        waitForSeconds(2);
                        SimpleUtils.assertOnFail("Setting is not saved successfully!", getConsecutiveOTSettingContent().contains(daysCount)&&getConsecutiveOTSettingContent().contains(condition), false);
                    } else {
                        clickTheElement(moodalDialog.findElement(By.cssSelector("[label=\"Cancel\"] button")));
                        waitForSeconds(2);
                        SimpleUtils.assertOnFail("Setting should the same as before!", contentBefore.equalsIgnoreCase(getConsecutiveOTSettingContent()), false);
                    }
                } else {
                    SimpleUtils.fail("Save or Cancel button fail to load!", false);
                }
            } else {
                SimpleUtils.fail("Dialog fail to load!", false);
            }
        } else {
            SimpleUtils.fail("Edit Consecutive OT button is not loaded!", false);
        }
    }


    @FindBy(css = "[form-title=\"Overtime Pay\"] [question-title*=\"a single workweek.\"]")
    private WebElement weeklyOTSection;
    @FindBy(css = "[form-title=\"Overtime Pay\"] [question-title*=\"In excess of how many hours an employee will receive Overtime Pay for hours worked within a single workweek?\"]")
    private WebElement weeklyOTSectionOP;
    @FindBy(css = "[form-title=\"Overtime Pay\"] [class=\"select-wrapper ng-scope\"] [ng-required=\"$ctrl.required\"]")
    private WebElement weeklyOTEditList;
    @FindBy(css = "[form-title=\"Overtime Pay\"] [question-title*=\"In excess of how many hours an employee will receive Overtime Pay for hours worked within a single workweek?\"] [ng-attr-name = \"{{$ctrl.inputName}}\"]")
    private WebElement weeklyOTEditOP;

    @Override
    public String getWeeklyOTSettingContent() throws Exception{
        if (isElementLoaded(weeklyOTSection, 10)){
            return weeklyOTSection.findElement(By.cssSelector(".lg-question-input__text")).getText();
        }else if(isElementLoaded(weeklyOTSectionOP, 10)){
            return weeklyOTSectionOP.findElement(By.cssSelector(".lg-question-input__text")).getText();
        }else{
            return "";
        }
    }

    @Override
    public void turnOnOrTurnOffWeeklyOTToggle(boolean action) throws Exception {
        String content = getWeeklyOTSettingContent();
        if (isElementLoaded(weeklyOTSection, 10)
                && ((content.contains("An employee will receive Overtime Pay for hours worked in excess of") &&content.contains("within a single workweek")))){
            if (isElementLoaded(weeklyOTSection.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
                if (action && weeklyOTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    scrollToElement(weeklyOTSection.findElement(By.cssSelector(".lg-question-input__toggle")));
                    clickTheElement(weeklyOTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned on!");
                } else if (!action && !weeklyOTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    clickTheElement(weeklyOTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned off!");
                } else {
                    SimpleUtils.pass("Toggle status is expected!");
                }
            } else {
                SimpleUtils.fail("Toggle fail to load!", false);
            }
        } else if(isElementLoaded(weeklyOTSectionOP, 10)&&(content.contains("In excess of how many hours an employee will receive Overtime Pay for hours worked within a single workweek?"))){
            if (isElementLoaded(weeklyOTSectionOP.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
                if (action && weeklyOTSectionOP.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    scrollToElement(weeklyOTSectionOP.findElement(By.cssSelector(".lg-question-input__toggle")));
                    clickTheElement(weeklyOTSectionOP.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned on!");
                } else if (!action && !weeklyOTSectionOP.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    clickTheElement(weeklyOTSectionOP.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned off!");
                } else {
                    SimpleUtils.pass("Toggle status is expected!");
                }
            } else {
                SimpleUtils.fail("Toggle fail to load!", false);
            }
        }else{
            SimpleUtils.fail("Week OT section fail to load!", false);
        }
    }

    @Override
    public void editWeeklyOTSetting(String optionVisibleText) throws Exception {
        String content = getWeeklyOTSettingContent();
        if (isElementLoaded(weeklyOTSection, 10)
                && ((content.contains("An employee will receive Overtime Pay for hours worked in excess of") &&content.contains("within a single workweek")))) {
            Select weekOTSelectElements = new Select(weeklyOTEditList);
            weekOTSelectElements.selectByVisibleText(optionVisibleText);
//            List<WebElement> weekOTSelectableOptions = weekOTSelectElements.getOptions();
//            weekOTSelectElements.selectByIndex(1);
//            for (WebElement weekOTSelectableOption : weekOTSelectableOptions) {
//                if (weekOTSelectableOption.getText().toLowerCase().contains(optionVisibleText.toLowerCase())) {
//                    weekOTSelectElements.selectByIndex(weekOTSelectableOptions.indexOf(weekOTSelectableOption));
            displaySuccessMessage();
            SimpleUtils.report("Select '" + optionVisibleText + "' as the WeekOT");
            waitForSeconds(2);
            SimpleUtils.assertOnFail("Setting is saved successfully!", getWeeklyOTSettingContent().contains(optionVisibleText), false);
//                }}
        }
        else if(isElementLoaded(weeklyOTSectionOP, 10) && (content.contains("In excess of how many hours an employee will receive Overtime Pay for hours worked within a single workweek?"))) {
            if (isElementLoaded(weeklyOTEditOP)) {
                weeklyOTEditOP.clear();
                weeklyOTEditOP.sendKeys(optionVisibleText);
            }
        }else{
            SimpleUtils.fail("Weekly OT section fail to load!", false);
        }
    }


    @FindBy(css = "[form-title=\"Overtime Pay\"] [question-title*=\"</b> in a <b>\"]")
    private WebElement dayOTSection;
    @FindBy(css = "[form-title=\"Overtime Pay\"] [ng-click*=\"openDailyOvertimeConfigDialog()\"]")
    private WebElement dayOTEditBtn;

    @Override
    public String getDayOTSettingContent() throws Exception{
        if (isElementLoaded(dayOTSection, 10)){
            return dayOTSection.findElement(By.cssSelector(".lg-question-input__text")).getText();
        }
        return "";
    }

    @Override
    public void turnOnOrTurnOffDayOTToggle(boolean action) throws Exception {
        String content = getDayOTSettingContent();
        if (isElementLoaded(dayOTSection, 10)
                && ((content.contains("An employee will receive Overtime Pay for hours worked in excess of") &&content.contains("in a")))){
            if (isElementLoaded(dayOTSection.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
                if (action && dayOTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    scrollToElement(dayOTSection.findElement(By.cssSelector(".lg-question-input__toggle")));
                    clickTheElement(dayOTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned on!");
                } else if (!action && !dayOTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    clickTheElement(dayOTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned off!");
                } else {
                    SimpleUtils.pass("Toggle status is expected!");
                }
            } else {
                SimpleUtils.fail("Toggle fail to load!", false);
            }
        } else {
            SimpleUtils.fail("Day OT section fail to load!", false);
        }
    }

    @Override
    public void editDayOTSetting(String dailyHours, String workDayType, boolean saveOrNot) throws Exception {
        if (isElementLoaded(dayOTEditBtn, 5)){
            String contentBefore = getConsecutiveOTSettingContent();
            clickTheElement(dayOTEditBtn);
            if (isElementLoaded(moodalDialog, 10)){
                //check the title.
                if (moodalDialog.findElement(By.cssSelector(".lg-modal__title")).getText().trim().equalsIgnoreCase("Edit Daily Overtime")){
                    SimpleUtils.pass("Dialog title is expected!");
                } else {
                    SimpleUtils.fail("Dialog title is not correct", false);
                }
                //Check setting content.
                if(moodalDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("An employee will receive Overtime Pay for hours worked in excess of")
                        &&moodalDialog.findElement(By.cssSelector(".lg-modal__body")).getText().contains("in a")){
                    SimpleUtils.pass("Setting content in the dialog is expected!");
                } else {
                    SimpleUtils.fail("Setting content is not expected!", false);
                }
                //edit the content, input the parameters.
                if (moodalDialog.findElements(By.cssSelector("select")).size() == 2){
                    selectByVisibleText(moodalDialog.findElements(By.cssSelector("select")).get(0), dailyHours);
                    selectByVisibleText(moodalDialog.findElements(By.cssSelector("select")).get(1), workDayType);
                } else {
                    SimpleUtils.fail("Selects are not shown as expected!", false);
                }
                //save or cancel.
                if (isElementLoaded(moodalDialog.findElement(By.cssSelector("[label=\"Cancel\"]")), 5)
                        &&isElementLoaded(moodalDialog.findElement(By.cssSelector("[label=\"Save\"]")), 5)){
                    if (saveOrNot){
                        clickTheElement(moodalDialog.findElement(By.cssSelector("[label=\"Save\"] button")));
                        waitForSeconds(2);
                        SimpleUtils.assertOnFail("Setting is not saved successfully!", getDayOTSettingContent().contains(dailyHours)&&getDayOTSettingContent().contains(workDayType), false);
                    } else {
                        clickTheElement(moodalDialog.findElement(By.cssSelector("[label=\"Cancel\"] button")));
                        waitForSeconds(2);
                        SimpleUtils.assertOnFail("Setting should the same as before!", contentBefore.equalsIgnoreCase(getDayOTSettingContent()), false);
                    }
                } else {
                    SimpleUtils.fail("Save or Cancel button fail to load!", false);
                }
            } else {
                SimpleUtils.fail("Dialog fail to load!", false);
            }
        } else {
            SimpleUtils.fail("Edit Day OT button is not loaded!", false);
        }
    }

    @FindBy(css = "[form-title=\"Doubletime Pay\"] [question-title*=\"consecutive \"]")
    private WebElement consecutiveDTSection;

    @Override
    public void turnOnOrTurnOffConsecutiveDTToggle(boolean action) throws Exception {
        String content = getConsecutiveDTSettingContent();
        if (isElementLoaded(consecutiveDTSection, 10)
                && ((content.contains("An employee will receive Doubletime Pay for hours worked in excess of") &&content.contains("consecutive day within a single workweek")
                || content.contains("In excess of how many hours an employee will receive Doubletime Pay for hours worked on 7th consecutive day within a single workweek?")))){
            if (isElementLoaded(consecutiveDTSection.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
                if (action && consecutiveDTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    scrollToElement(consecutiveDTSection.findElement(By.cssSelector(".lg-question-input__toggle")));
                    clickTheElement(consecutiveDTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned on!");
                } else if (!action && !consecutiveDTSection.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    clickTheElement(consecutiveDTSection.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned off!");
                } else {
                    SimpleUtils.pass("Toggle status is expected!");
                }
            } else {
                SimpleUtils.fail("Toggle fail to load!", false);
            }
        } else {
            SimpleUtils.fail("Consecutive DT section fail to load!", false);
        }
    }


    @FindBy(css = "[form-title=\"Doubletime Pay\"] [question-title*=\"hours</b> within a single workweek\"]")
    private WebElement weeklyDTSection;
    @FindBy(css = "[form-title=\"Doubletime Pay\"] [question-title*=\"In excess of how many hours an employee will receive Doubletime Pay for hours worked within a single workweek?\"]")
    private WebElement weeklyDTSectionOnOP;

    @Override
    public void turnOnOrTurnOffWeelyDTToggle(boolean action) throws Exception {
        String content = getWeeklyDTSettingContent();
        if ((isElementLoaded(weeklyDTSection, 10) || isElementLoaded(weeklyDTSectionOnOP, 10))
                && ((content.contains("An employee will receive Doubletime Pay for hours worked in excess of")
                &&content.contains("within a single workweek"))
                ||content.contains("In excess of how many hours an employee will receive Doubletime Pay for hours worked within a single workweek?")) ){
            WebElement weeklyDT = null;
            if (isElementLoaded(weeklyDTSection)) {
                weeklyDT = weeklyDTSection;
            } else
                weeklyDT = weeklyDTSectionOnOP;
            if (isElementLoaded(weeklyDT.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
                if (action && weeklyDT.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    scrollToElement(weeklyDT.findElement(By.cssSelector(".lg-question-input__toggle")));
                    clickTheElement(weeklyDT.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned on!");
                } else if (!action && !weeklyDT.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    clickTheElement(weeklyDT.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned off!");
                } else {
                    SimpleUtils.pass("Toggle status is expected!");
                }
            } else {
                SimpleUtils.fail("Toggle fail to load!", false);
            }
        } else {
            SimpleUtils.fail("Weekly DT section fail to load!", false);
        }
    }

    @Override
    public String getWeeklyDTSettingContent() throws Exception{
        if (isElementLoaded(weeklyDTSection, 10)){
            return weeklyDTSection.findElement(By.cssSelector(".lg-question-input__text")).getText();
        } else if (isElementLoaded(weeklyDTSectionOnOP, 10)) {
            return weeklyDTSectionOnOP.findElement(By.cssSelector(".lg-question-input__text")).getText();
        }
        return "";
    }

    @FindBy(css = "[form-title=\"Doubletime Pay\"] [question-title*=\"hours</b> within a single workday\"]")
    private WebElement dailyDTSection;
    @FindBy(css = "[form-title=\"Doubletime Pay\"] [question-title*=\"In excess of how many hours an employee will receive Doubletime Pay for hours worked within a single workday?\"]")
    private WebElement dailyDTSectionOnOp;
    @Override
    public void turnOnOrTurnOffDailyDTToggle(boolean action) throws Exception {
        String content = getDailyDTSettingContent();
        if ((isElementLoaded(dailyDTSection, 10)||isElementLoaded(dailyDTSectionOnOp, 10))
                && ((content.contains("An employee will receive Doubletime Pay for hours worked in excess of")
                &&content.contains("within a single workday"))
                || content.contains("In excess of how many hours an employee will receive Doubletime Pay for hours worked within a single workday?"))){
            WebElement dailyDT = null;
            if (isElementLoaded(dailyDTSection, 5)) {
                dailyDT = dailyDTSection;
            } else
                dailyDT = dailyDTSectionOnOp;
            if (isElementLoaded(dailyDT.findElement(By.cssSelector(".lg-question-input__toggle")),10)){
                if (action && dailyDT.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    scrollToElement(dailyDT.findElement(By.cssSelector(".lg-question-input__toggle")));
                    clickTheElement(dailyDT.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned on!");
                } else if (!action && !dailyDT.findElement(By.cssSelector(".lg-question-input")).getAttribute("class").contains("off")){
                    clickTheElement(dailyDT.findElement(By.cssSelector(".lg-question-input__toggle .slider")));
                    displaySuccessMessage();
                    SimpleUtils.pass("Toggle is turned off!");
                } else {
                    SimpleUtils.pass("Toggle status is expected!");
                }
            } else {
                SimpleUtils.fail("Toggle fail to load!", false);
            }
        } else {
            SimpleUtils.fail("Daily DT section fail to load!", false);
        }
    }

    @Override
    public String getDailyDTSettingContent() throws Exception{
        if (isElementLoaded(dailyDTSection, 10)){
            return dailyDTSection.findElement(By.cssSelector(".lg-question-input__text")).getText();
        } else if(isElementLoaded(dailyDTSectionOnOp, 10)) {
            return dailyDTSectionOnOp.findElement(By.cssSelector(".lg-question-input__text")).getText();
        }
        return "";
    }
}
