package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.LiquidDashboardPage;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleLiquidDashboardPage extends BasePage implements LiquidDashboardPage {
    public ConsoleLiquidDashboardPage(){
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(css = "[label=\"Edit\"]")
    private WebElement editBtn;

    @FindBy (css = ".edit-dashboard-text")
    private WebElement editDasboardText;

    @FindBy (css = "[ng-repeat*=\"groupedWidgets\"]")
    private List<WebElement> widgetsInManagePage;

    @FindBy (css = "[label=\"Manage\"]")
    private WebElement manageBtn;

    @FindBy (css = ".manageWidgetsEditLabel")
    private WebElement editDashboardBtn;

    @FindBy (css = ".widget-bg")
    private  List<WebElement> widgetsInDashboardPage;

    @FindBy (css = "[label=\"Save\"]")
    private WebElement saveBtn;

    @FindBy (xpath = "//span[text()=\"Cancel\"]")
    private WebElement cancelBtn;

    @FindBy (css = "[label=\"Back\"]")
    private WebElement backBtn;

    @FindBy (css = "input[placeholder=\"Search for widgets\"]")
    private WebElement searchInput;

    @FindBy (css = "div[ng-if=\"ShowEdit\"]")
    private WebElement editLinkBtn;

    @FindBy (xpath = "//div[text()=\"Add Link\"]")
    private WebElement addLinkBtn;

    @FindBy (xpath = "//span[text()=\"CANCEL\"]")
    private WebElement cancelAddLinkBtn;

    @FindBy (xpath = "//span[text()=\"SAVE\"]")
    private WebElement saveAddLinkBtn;

    @FindBy (css = ".link-title.link-title-text")
    private List<WebElement> linkTitles;

    @FindBy (css = ".link-row.link-row-text")
    private List<WebElement> linkTexts;

    @FindBy (css = "ng-container[ng-repeat=\"link in dataLinks\"] div")
    private List<WebElement> linksOnWidget;

    @FindBy (css = ".forecast.forecast-row.row-fx.ng-scope")
    private WebElement dataOnTodayForecast;

    @FindBy (css = ".row-fx.schedule-table-row.ng-scope")
    private List<WebElement> dataOnSchedules;

    @FindBy (className = "widgetCommonText")
    private WebElement welcomeText;

    @FindBy (className = "widgetScrollText")
    private WebElement widgetScrollText;

    @Override
    public void enterEditMode() throws Exception {
        scrollToTop();
        if (isElementLoaded(editBtn,10)){
            clickTheElement(editBtn.findElement(By.cssSelector("button")));
            if (isElementLoaded(editDasboardText,5) && isElementLoaded(manageBtn, 10)){
                SimpleUtils.pass("Edit mode load successfully!");
            } else {
                SimpleUtils.fail("Edit mode fail to load!", false);
            }
        } else{
            SimpleUtils.fail("Edit button fail to load!", false);
        }
    }

    @Override
    public void switchOnWidget(String widget) throws Exception {
        String widgetName ="";
        List<WebElement> widgets = widgetsInManagePage;
        if (isElementLoaded(manageBtn,10)){
            clickTheElement(manageBtn);
            if (areListElementVisible(widgets,10)){
                for (int i=0; i<widgets.size(); i++) {
                    widgetName = widgets.get(i).findElement(By.cssSelector("div[class=\"detail-div\"] :nth-child(1)")).getText().toLowerCase().trim();
                    if (widget.toLowerCase().contains(widgetName)){
                        if (widgets.get(i).findElement(By.cssSelector("ng-form input")).getAttribute("class").contains("ng-empty")){
                            scrollToElement(widgets.get(i));
                            clickTheElement(widgets.get(i).findElement(By.cssSelector(".slider")));
                            SimpleUtils.pass(widget+" widget's switched on!");
                            waitForSeconds(4);
                        } else {
                            SimpleUtils.pass(widget+"widget's already switched on!");
                        }
                        break;
                    }
                }
                //return to edit dashboard
                if (isElementLoaded(editDashboardBtn,10)){
                    clickTheElement(editDashboardBtn);
                    SimpleUtils.assertOnFail(widget+" widget is not loaded!",verifyIfSpecificWidgetDisplayed(widget), false);
                } else {
                    SimpleUtils.fail("Edit Dashboard button fail to load!",true);
                }
            } else {
                SimpleUtils.fail("Widgets in Manage page fail to load!",true);
            }
        } else {
            SimpleUtils.fail("Manage button fail to load!",false);
        }
    }

    @Override
    public void switchOffWidget(String widget) throws Exception {
        String widgetName ="";
        boolean flag = false;
        List<WebElement> widgets = widgetsInManagePage;
        if (isElementLoaded(manageBtn,10)){
            click(manageBtn);
            if (areListElementVisible(widgets,10)){
                for (int i=0; i<widgets.size(); i++) {
                    widgetName = widgets.get(i).findElement(By.cssSelector("div[class=\"detail-div\"] :nth-child(1)")).getText().toLowerCase().trim();
                    if (widget.contains(widgetName)){
                        if (widgets.get(i).findElement(By.cssSelector("ng-form input")).getAttribute("class").contains("ng-not-empty")){
                            scrollToElement(widgets.get(i));
                            clickTheElement(widgets.get(i).findElement(By.cssSelector(".slider")));
                            SimpleUtils.pass(widget+" widget's switched off!");
                            waitForSeconds(2);
                        } else {
                            SimpleUtils.pass(widget+" widget's already switched off!");
                        }
                        break;
                    }
                }
                //return to edit dashboard
                if (isElementLoaded(editDashboardBtn,5)){
                    click(editDashboardBtn);
                    if (!verifyIfSpecificWidgetDisplayed(widget)){
                        flag = true;
                    }
                    SimpleUtils.assertOnFail(widget+"widget is loaded which is not expected!",flag, false);
                } else {
                    SimpleUtils.fail("Edit Dashboard button fail to load!",true);
                }
            } else {
                SimpleUtils.fail("Widgets in Manage page fail to load!",true);
            }
        } else {
            SimpleUtils.fail("Manage button fail to load!",true);
        }
    }

    //parameter option: helpful links and so on
    private boolean verifyIfSpecificWidgetDisplayed(String widgetTitle) {
        waitForSeconds(10);
        boolean result = false;
        if (areListElementVisible(widgetsInDashboardPage,30)){
            for (WebElement widgetTemp : widgetsInDashboardPage){
                String s = widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText();
                if (widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains(widgetsNameWrapper(widgetTitle))) {
                    if (widgetsNameWrapper(widgetTitle).equalsIgnoreCase("timesheet approval")) {
                        if (!widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("timesheet approval status")) {
                            result =  true;
                        }
                    } else {
                        result =  true;
                    }
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Widgets in Dashboard page fail to load!",false);
        }
        return result;
    }

    @Override
    public void closeWidget(String widgetTitle) throws Exception {
        boolean flag =false;
        waitForSeconds(10);
        if (areListElementVisible(widgetsInDashboardPage,10)){
            for (WebElement widgetTemp : widgetsInDashboardPage){
                if(widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains(widgetsNameWrapper(widgetTitle))){
                    if (widgetsNameWrapper(widgetTitle).equalsIgnoreCase("timesheet approval")){
                        if (!widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("timesheet approval status")){
                            scrollToElement(widgetTemp);
                            click(widgetTemp.findElement(By.cssSelector(".boxclose")));
                            if (!verifyIfSpecificWidgetDisplayed(widgetTitle)){
                                flag = true;
                            }
                            SimpleUtils.assertOnFail("widget is loaded which is not expected!",flag, true);
                            break;
                        }
                    } else {
                        scrollToElement(widgetTemp);
                        clickTheElement(widgetTemp.findElement(By.cssSelector(".boxclose")));
                        if (!verifyIfSpecificWidgetDisplayed(widgetTitle)){
                            flag = true;
                        }
                        SimpleUtils.assertOnFail("widget is loaded which is not expected!",flag, true);
                        break;
                    }
                }
            }
        } else {
            SimpleUtils.fail("Widgets in Dashboard page fail to load!",true);
        }
    }

    @Override
    public void verifyUpdateTimeInfoIcon(String widgetTitle) throws Exception {
        waitForSeconds(3);
        if (areListElementVisible(widgetsInDashboardPage,10)){
            for (WebElement widgetTemp : widgetsInDashboardPage){
                if(widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains(widgetsNameWrapper(widgetTitle))){
                    if (widgetsNameWrapper(widgetTitle).equalsIgnoreCase("timesheet approval")){
                        if (!widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("timesheet approval status")){
                            scrollToElement(widgetTemp);
                            mouseToElement(widgetTemp);
                            if (widgetTemp.findElement(By.cssSelector(".widget-timerclock"))!=null){
                                SimpleUtils.pass("update time icon load successfully!");
                            }
                            break;
                        }
                    } else {
                        scrollToElement(widgetTemp);
                        mouseToElement(widgetTemp);
                        if (widgetTemp.findElement(By.cssSelector(".widget-timerclock"))!=null){
                            SimpleUtils.pass("update time icon load successfully!");
                        }
                        break;
                    }
                }
            }
        } else {
            SimpleUtils.fail("Widgets in Dashboard page fail to load!",true);
        }
    }

    @Override
    public void saveAndExitEditMode() throws Exception{
        if (isElementLoaded(saveBtn,10)){
            clickTheElement(saveBtn);
            if (isElementLoaded(welcomeText, 15) || isElementLoaded(widgetScrollText, 15)) {
                SimpleUtils.pass("Edit Dashboard Page: Click on Save button Successfully!");
            }else {
                SimpleUtils.fail("Edit Dashboard Page: Click on Save button failed, Dashboard welcome text not loaded Successfully!", false);
            }
        } else {
            SimpleUtils.fail("save button is not loaded!",false);
        }
    }

    @Override
    public void cancelAndExitEditMode() throws Exception{
        if (isElementLoaded(cancelBtn,10)){
            click(cancelBtn);
        } else {
            SimpleUtils.fail("cancel button is not loaded!",true);
        }
    }

    @Override
    public void verifyBackBtn() throws Exception {
        if (isElementLoaded(manageBtn,5)){
            click(manageBtn);
            if (isElementLoaded(backBtn,10)){
                click(backBtn);
                SimpleUtils.pass("Back button is working fine!");
            } else {
                SimpleUtils.fail("Back button is not loaded!",false);
            }
        } else {
            SimpleUtils.fail("verifyBackBtn: Manage button fail to load!",true);
        }
    }

    @Override
    public void verifySearchInput(String widgetTitle) throws Exception {
        if (isElementLoaded(manageBtn,10)){
            click(manageBtn);
        } else {
            SimpleUtils.fail("Manage button fail to load!",true);
        }
        if (isElementLoaded(searchInput,5)){
            searchInput.sendKeys(widgetTitle);
            waitForSeconds(5);
            if (areListElementVisible(widgetsInManagePage,5)){
                String actualResult = widgetsInManagePage.get(0).findElement(By.cssSelector("div[class=\"detail-div\"] :nth-child(1)")).getText().toLowerCase();
                if (widgetsInManagePage.size()==1 && widgetTitle.toLowerCase().contains(actualResult)){
                    SimpleUtils.pass("Search result is what you want!");
                } else {
                    SimpleUtils.fail("Search result is not correct!",true);
                }
            } else {
                SimpleUtils.fail("verifySearchInput: no search widgets result!",true);
            }
        } else {
            SimpleUtils.fail("Search input is not loaded!",true);
        }
    }


    //get widget name in edit page
    private String widgetsNameWrapper(String widgetTitleInManagePage) {
        if (widgetTitleInManagePage.contains("starting soon")){
            return "starting";
        } else if (widgetTitleInManagePage.contains("timesheet approval rate")){
            return "timesheet approval";
        } else if (widgetTitleInManagePage.contains("compliance violation")){
            return "compliance violation";
        }
        return widgetTitleInManagePage;
    }


    // Added by Nora
    @FindBy(xpath = "//*[contains(@class, \"gridster-item\")]")
    private List<WebElement> widgets;
    @FindBy(css = "div.background-current-week-legend-table")
    private WebElement currentWeekOnSchedules;
    @FindBy(css = "lg-alert .slideNumberText")
    private WebElement alertsWeek;
    @FindBy(css = ".lg-timesheet-carousel__table-row--cell")
    private List<WebElement> alertsCells;
    @FindBy(css = "[label=\"'Timesheet Approval Status'\"] .dms-box-item-title-row span")
    private WebElement timesheetApprovalStatusWeek;
    @FindBy(className = "analytics-new-smart-card-timesheet-approval-legend-item")
    private List<WebElement> timesheetApprovalLegendItems;
    @FindBy(css = "[ng-if=\"smartCardData.approved24HPerc[0] > 0\"]")
    private WebElement approved24HRate;
    @FindBy(css = "[ng-if=\"smartCardData.approved48HPerc[0] > 0\"]")
    private WebElement approved48HRate;
    @FindBy(css = "[ng-if=\"smartCardData.approved72HPerc[0] > 0\"]")
    private WebElement approved72HRate;
    @FindBy(css = "ng-if=\"smartCardData.unapprovedPerc[0] > 0\"")
    private WebElement unApprovedRate;
    @FindBy(css = "[ng-if*=\"requestForswapsdata\"]")
    private WebElement swapData;
    @FindBy(css = "[ng-if*=\"requestForCoverdata\"]")
    private WebElement coverData;
    @FindBy(className = "shift-offer")
    private List<WebElement> shiftOffers;

    @Override
    public void verifyTheContentOfSwapNCoverWidget(String swapOrCover) throws Exception {
        String unClaimedColor = "rgb(169, 169, 169)";
        String claimedColor = "rgb(129, 194, 196)";
        boolean isConsistent = false;
        if (areListElementVisible(shiftOffers, 5)) {
            for (WebElement shiftOffer : shiftOffers) {
                try {
                    WebElement pieChart = shiftOffer.findElement(By.className("widgetPieChart"));
                    WebElement legendLabel = shiftOffer.findElement(By.className("ana-kpi-legend-text-label"));
                    List<WebElement> percentages = shiftOffer.findElements(By.className("ana-kpi-legend-text"));
                    if (pieChart != null && legendLabel != null && legendLabel.getText().contains(swapOrCover) && percentages != null
                    && percentages.size() == 2) {
                        WebElement path = pieChart.findElement(By.tagName("path"));
                        WebElement text = pieChart.findElement(By.tagName("text"));
                        if (path != null && text != null) {
                            String pieChartColor = path.getAttribute("style");
                            if (percentages.get(0).getText().contains("(") && percentages.get(0).getText().contains(")") &&
                                    percentages.get(1).getText().contains("(") && percentages.get(1).getText().contains(")")) {
                                int unclaimedPercentage = Integer.parseInt(percentages.get(0).getText().substring(percentages.get(0).getText().indexOf("(") + 1,
                                        percentages.get(0).getText().indexOf(")") - 1).trim());
                                int claimedPercentage = Integer.parseInt(percentages.get(1).getText().substring(percentages.get(1).getText().indexOf("(") + 1,
                                        percentages.get(1).getText().indexOf(")") -1 ).trim());
                                int count = Integer.parseInt(legendLabel.getText().substring(0, 1));
                                int countInPieChart = Integer.parseInt(text.getText());
                                if (count == countInPieChart) {
                                    SimpleUtils.pass("Swaps & Covers Widget: the count of " + swapOrCover + " is: " + count);
                                    if (unclaimedPercentage == 100 && pieChartColor.contains(unClaimedColor)) {
                                        SimpleUtils.pass("Swaps & Covers Widget: Verified the pie chart color of Unclaim is correct");
                                        isConsistent = true;
                                        break;
                                    }
                                    if (claimedPercentage == 100 && pieChartColor.contains(claimedColor)) {
                                        SimpleUtils.pass("Swaps & Covers Widget: Verified the pie chart color of claim is correct");
                                        isConsistent = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception e) {
                    continue;
                }
            }
        }
        if (!isConsistent) {
            SimpleUtils.fail("Swaps & Covers Widget: The content on this widget is incorrect!", false);
        }
    }

    @Override
    public void verifyNoContentOfSwapsNCoversWidget() throws Exception {
        String noSwap = "There are no swaps.";
        String noCover = "There are no covers.";
        if (isElementLoaded(swapData, 5) && isElementLoaded(coverData, 5)) {
            if (swapData.getText().contains(noSwap) && coverData.getText().contains(noCover)) {
                SimpleUtils.pass("Liquid Dashboard Page: Verified there is no Swaps & Covers data when schedule is not generated!");
            }else {
                SimpleUtils.fail("Liquid Dashboard Page: Verified there is data on Swaps & Covers when schedule is not generated!", false);
            }
        }else {
            SimpleUtils.fail("Liquid Dashboard Page: Swaps & Covers elements not loaded SUccessfully!", false);
        }
    }

    @Override
    public int getTimeSheetApprovalStatusFromPieChart() throws Exception {
        int approvalRate = 0;
        if (isElementLoaded(approved24HRate, 5)) {
            approvalRate += Integer.parseInt(approved24HRate.getText().trim().replaceAll("%", ""));
        }
        if (isElementLoaded(approved48HRate, 5)) {
            approvalRate += Integer.parseInt(approved48HRate.getText().trim().replaceAll("%", ""));
        }
        if (isElementLoaded(approved72HRate, 5)) {
            approvalRate += Integer.parseInt(approved72HRate.getText().trim().replaceAll("%", ""));
        }
        return approvalRate;
    }

    @Override
    public void verifyTheContentOnTimesheetApprovalStatusWidgetLoaded(String currentWeek) throws Exception {
        /*Timesheet Approval Rate  widget should show:
        a. current week, e.g. Week of Jun 20;
        b. the card is < 24 Hrs/24-48 Hrs/48+ Hrs/Unapproved with different color
        c. [View Timesheets] button*/
        List<String> legendItems = new ArrayList<>(Arrays.asList("< 24 Hrs", "24-48 Hrs", "48+ Hrs", "Unapproved"));
        if (isElementLoaded(timesheetApprovalStatusWeek, 5) && timesheetApprovalStatusWeek.getText().toLowerCase().contains(currentWeek.toLowerCase())) {
            SimpleUtils.pass("The week of \"Timesheet Approval Status\" is loaded and correct!");
        } else {
            SimpleUtils.warn("The week of \"Timesheet Approval Status\" is not loaded or incorrect!");
        }
        if (areListElementVisible(timesheetApprovalLegendItems, 5) && timesheetApprovalLegendItems.size() == 4) {
            for (WebElement item : timesheetApprovalLegendItems) {
                if (legendItems.contains(item.getText().trim())) {
                    SimpleUtils.pass("Verified Legend Item: \"" + item.getText().trim() + "\" loaded Correctly!");
                }else {
                    SimpleUtils.fail("Unexpected legend item: \"" + item.getText().trim() + "\" loaded!", false);
                }
            }
        }else {
            SimpleUtils.fail("The Legend Items of \"Timesheet Approval Status\" not loaded Successfully!", false);
        }
        if (isElementLoaded(MyThreadLocal.getDriver().findElement(By.cssSelector("lg-analytics-timesheet-approval-primary .dms-action-link")), 5)) {
            SimpleUtils.pass("\"View Timesheets\" link loaded Successfully on \"Timesheet Approval Status\"!");
        } else {
            SimpleUtils.fail("\"View Timesheets\" link not loaded Successfully on \"Timesheet Approval Status\"!", false);
        }
    }

    @Override
    public void clickOnLinkByWidgetNameAndLinkName(String widgetName, String linkName) throws Exception {
        try {
            if (areListElementVisible(widgets, 10)) {
                for (int i =0; i<widgets.size(); i++) {
                    // wait for all the widget content loaded Successfully
                    waitForSeconds(2);
                    WebElement widgetTitle = getDriver().findElements(By.xpath("//*[contains(@class, \"gridster-item\")]//*[contains(@class, \"dms-box-title\")]")).get(i);
                    System.out.println(widgetTitle.getText());
                    if (widgetTitle != null && (widgetTitle.getText().toLowerCase().trim().contains(widgetsNameWrapper(widgetName)) ||
                            widgetTitle.getText().toLowerCase().trim().contains(widgetsNameWrapper(widgetName)))) {
                        try {
                            WebElement link = widgets.get(i).findElement(By.cssSelector(".dms-action-link"));
                            if (link != null && linkName.toLowerCase().equals(link.getText().toLowerCase().trim())) {
                                clickTheElement(link);
                                SimpleUtils.pass("Click on: \"" + linkName + "\" on Widget: \"" + widgetName + "\" Successfully!");
                                break;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            } else {
                SimpleUtils.report("There are no widgets on dashboard, please turn on them!");
            }
        } catch (Exception e) {
            SimpleUtils.fail("Execute method: clickOnLinkByWidgetNameAndLinkName() failed!", false);
        }
    }

    @Override
    public void verifyEditLinkOfHelpfulLinks() throws Exception {
        if (isElementLoaded(editLinkBtn,10)){
            scrollToElement(editLinkBtn);
            click(editLinkBtn);
            SimpleUtils.pass("Helpful Links Widget: Edit link button has been clicked!");
        } else {
            SimpleUtils.fail("Helpful Links Widget: Verify Edit Link of Helpful Links. Edit link button fail to load!", false);
        }
    }

    @Override
    public void addLinkOfHelpfulLinks() throws Exception {
        if (isElementLoaded(addLinkBtn,10)){
            click(addLinkBtn);
            SimpleUtils.pass("Helpful Links Widget: Add link button has been clicked successfully!");
            editNewLink();
        } else if(areListElementVisible(linkTitles) && linkTitles.size()==5) {
            SimpleUtils.pass("Helpful Links Widget: There are already 5 links");
        } else {
            SimpleUtils.fail("Helpful Links Widget: Add Link button fail to load!",false);
        }
    }

    private void editNewLink() throws Exception{
        if (areListElementVisible(linkTitles,10) && areListElementVisible(linkTexts,10)){
            linkTitles.get(linkTitles.size()-1).findElement(By.cssSelector("input")).sendKeys("link"+linkTitles.size());
            linkTexts.get(linkTexts.size()-1).findElement(By.cssSelector("input")).sendKeys("https://www.google.com/");
        } else {
            SimpleUtils.fail("Helpful Links Widget: editNewLink method: there is no link to edit!",false);
        }
    }

    @Override
    public void deleteAllLinks() throws Exception {
        int s = linkTexts.size();
        if (areListElementVisible(linkTexts,10)){
            for(int i=0; i<s ;i++){
                moveToElementAndClick(linkTexts.get(0).findElement(By.cssSelector(".removeLink")));
                SimpleUtils.pass("Helpful Links Widget: Delete link successfully!");
            }
        } else {
            SimpleUtils.report("Helpful Links Widget: deleteAllLinks method: No links to delete!");
        }
    }

    @Override
    public void saveLinks() throws Exception {
        if (isElementLoaded(saveAddLinkBtn,10)){
            click(saveAddLinkBtn);
            SimpleUtils.pass("Helpful Links Widget: Save button has been clicked successfully!");
        } else {
            SimpleUtils.fail("Helpful Links Widget: Save button fail to load!",true);
        }
    }

    @Override
    public void cancelLinks() throws Exception {
        if (isElementLoaded(cancelAddLinkBtn,10)){
            click(cancelAddLinkBtn);
            SimpleUtils.pass("Helpful Links Widget: Cancel button has been clicked successfully!");
        } else {
            SimpleUtils.fail("Helpful Links Widget: Cancel button fail to load!",true);
        }
    }

    @Override
    public void verifyLinks() throws Exception {
        String handle = getDriver().getWindowHandle();
        if (areListElementVisible(linksOnWidget,10)){
            for (WebElement e: linksOnWidget){
                moveToElementAndClick(e);
                SimpleUtils.pass("Helpful Links Widget: New tab open: "+getDriver().getWindowHandle());
                getDriver().switchTo().window(handle);
            }
        } else {
            SimpleUtils.fail("Helpful Links Widget: verifyLinks method: there is no links to click!",true);
        }
    }

    @Override
    public void verifyNoLinksOnHelpfulLinks() throws Exception {
        if (areListElementVisible(widgetsInDashboardPage,10)){
            for (WebElement widgetTemp : widgetsInDashboardPage){
                if(widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("helpful links")){
                    if (widgetTemp.findElement(By.cssSelector("div[ng-if=\"linkText\"]")).getAttribute("class").contains("nodata")){
                        SimpleUtils.pass("Helpful Links Widget: No links, message: "+widgetTemp.findElement(By.cssSelector("div[ng-if=\"linkText\"] h1")).getText());
                    }
                }
            }
        } else {
            SimpleUtils.fail("Helpful Links Widget: Widgets in Dashboard page fail to load!",true);
        }
    }

    @Override
    public boolean isSpecificWidgetLoaded(String widgetName) throws Exception {
        boolean isLoaded = false;
        String startingTomorrow = "starting tomorrow";
        if (areListElementVisible(widgets, 10)) {
            for (WebElement widget : widgets) {
                try {
                    WebElement widgetTitle = widget.findElement(By.cssSelector(".dms-box-title"));
                    if (widgetTitle != null && (widgetTitle.getText().toLowerCase().trim().contains(widgetName.toLowerCase()))) {
                        isLoaded = true;
                        break;
                    } else if (widgetTitle != null && widgetName.toLowerCase().equals("starting soon") &&
                            widgetTitle.getText().toLowerCase().trim().contains(startingTomorrow.toLowerCase().trim())) {
                        isLoaded = true;
                        break;
                    }
                }catch (Exception e) {
                    continue;
                }
            }
        }
        return isLoaded;
    }

    @Override
    public String getTheStartOfCurrentWeekFromSchedulesWidget() throws Exception {
        String currentWeek = "";
        waitForSeconds(3);
        if (isElementLoaded(currentWeekOnSchedules, 10)) {
            if (currentWeekOnSchedules.getText().contains("—")) {
                currentWeek = currentWeekOnSchedules.getText().split("—")[0];
                if (currentWeek.endsWith("\n")) {
                    currentWeek = currentWeek.substring(0, currentWeek.length() - 1);
                    if (currentWeek.contains(" ")) {
                        String[] tempItems = currentWeek.split(" ");
                        if (tempItems.length == 2) {
                            currentWeek = tempItems[0] + " " + Integer.parseInt(tempItems[1]);
                        }
                    }
                }
            }
        }
        if (!currentWeek.isEmpty()) {
            SimpleUtils.pass("Get the start of the current week: \"" + currentWeek + "\" Successfully!");
        }else {
            SimpleUtils.fail("Failed to get the start of the current week!", false);
        }
        return currentWeek;
    }

    @Override
    public List<String> verifyTheContentOnAlertsWidgetLoaded(String currentWeek) throws Exception {
        List<String> alerts = new ArrayList<>();
        /*Alerts widget should show:
        a. current week, e.g. Week of Jun 20;
        b. the number of Early Clocks/Incomplete Clocks/No Show/Late Clocks
                /Missed Meal/Unscheduled
        c. [View Timesheets] button*/
        if (isElementLoaded(alertsWeek, 10) && alertsWeek.getText().toLowerCase().contains(currentWeek.toLowerCase())) {
            SimpleUtils.pass("The week of Alerts is loaded and correct!");
        } else {
            SimpleUtils.fail("The week of \"Alerts\" is not loaded or incorrect!", false);
        }
        if (areListElementVisible(alertsCells, 5) && alertsCells.size() == 6) {
            for (WebElement alertsCell : alertsCells) {
                if (!alertsCell.getText().isEmpty()) {
                    alerts.add(alertsCell.getText());
                    SimpleUtils.report("Get the alerts Data: \"" + alertsCell.getText() + "\" Successfully!");
                } else {
                    SimpleUtils.fail("Failed to get the alert data!", false);
                }
            }
        } else {
            SimpleUtils.fail("The Alerts data not loaded Successfully!", false);
        }
        if (isElementLoaded(MyThreadLocal.getDriver().findElement(By.cssSelector("lg-alert .dms-action-link")), 5)) {
            SimpleUtils.pass("\"View Timesheets\" link loaded Successfully!");
        } else {
            SimpleUtils.fail("\"View Timesheets\" link not loaded Successfully!", false);
        }

        return alerts;
    }

    @Override
    public void verifyIsGraphExistedOnWidget() throws Exception {
        if (areListElementVisible(widgetsInDashboardPage,10)){
            for (WebElement widgetTemp : widgetsInDashboardPage){
                waitForSeconds(3);
                if(widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("forecast")){
                    if (isElementLoaded(widgetTemp.findElement(By.cssSelector("#curvedGraphDiv")),10)){
                        SimpleUtils.pass("Today's Forecast widget: There is a graph on today's forecast widget.");
                    } else {
                        SimpleUtils.fail("Today's Forecast widget: There is no graph on today's widget.",true);
                    }
                }
            }
        } else {
            SimpleUtils.fail("Today's Forecast widget: Widgets in Dashboard page fail to load!",true);
        }
    }

    @Override
    public HashMap <String, Float> getDataOnTodayForecast() throws Exception {
        HashMap <String, Float> resultData = new HashMap<String, Float>();
        if (isElementLoaded(dataOnTodayForecast,10)){
            String tempData = dataOnTodayForecast.getText();
            String[] dataString = dataOnTodayForecast.getText().split("\n");
            if (dataString.length>4){
                resultData.put("demand forecast",Float.valueOf(dataString[0].replaceAll("Shoppers","").replaceAll(",", "").replaceAll("ITEMS","")));
                resultData.put("budget",Float.valueOf(dataString[2].replaceAll("Hrs","")));
                resultData.put("scheduled",Float.valueOf(dataString[4].replaceAll("Hrs","")));
            } else {
                SimpleUtils.fail("Today's Forecast widget: Values doesn't display as expected!", false);
            }

        } else {
            SimpleUtils.fail("Today's Forecast widget: getDataOnTodayForecast method: No data on widget!",false);
        }
        return resultData;
    }

    @Override
    public List<String> getDataOnSchedulesWidget() throws Exception {
        List<String> resultList= new ArrayList<String>();
        if (areListElementVisible(dataOnSchedules,10)){
            for (int i=0;i<dataOnSchedules.size();i++){
                String[] temp1 = dataOnSchedules.get(i).getText().split("\n");
                String[] temp2 = Arrays.copyOf(temp1,8);
                resultList.add(Arrays.toString(temp2));
            }
        } else {
            SimpleUtils.fail("Data on schedules widget fail to load!",true);
        }
        if (resultList.size()<=4){
            SimpleUtils.pass("There are 4 week info on Schedules widget!");
        } else {
            SimpleUtils.fail("There are more than 4 week on Schedule widget which is not expected!",true);
        }
        return resultList;
    }

    @Override
    public void clickFirstWeekOnSchedulesGoToSchedule() throws Exception {
        if (areListElementVisible(dataOnSchedules,10) && dataOnSchedules.size()>0){
            waitForSeconds(3);
            moveToElementAndClick(dataOnSchedules.get(0));
        } else {
            SimpleUtils.fail("clickFirstWeekOnSchedulesGoToSchedule: data on schedules widget fail to load!",true);
        }
    }

    @Override
    public void verifyWeekInfoOnWidget(String widgetTitle, String startdayOfWeek) throws Exception {
        String weekinfo = null;
        if (areListElementVisible(widgetsInDashboardPage,10)){
            for (WebElement widgetTemp : widgetsInDashboardPage){
                waitForSeconds(2);
                if(widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains(widgetsNameWrapper(widgetTitle))){
                    if (widgetsNameWrapper(widgetTitle).equalsIgnoreCase("timesheet approval")){
                        if (!widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("timesheet approval status")){
                            if (widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("week of")){
                                weekinfo = widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().split("week of ")[1];
                                if (startdayOfWeek.toLowerCase().contains(weekinfo)){
                                    SimpleUtils.pass("week info is right!");
                                } else {
                                    SimpleUtils.fail("week info is not correct!",true);
                                }
                                break;
                            }
                        }
                    } else {
                        if (widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().contains("week of")){
                            weekinfo = widgetTemp.findElement(By.cssSelector(".dms-box-title")).getText().toLowerCase().split("week of ")[1];
                            if (startdayOfWeek.toLowerCase().contains(weekinfo)){
                                SimpleUtils.pass("week info is right!");
                            } else {
                                SimpleUtils.fail("week info is not correct!",true);
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            SimpleUtils.fail("Widgets in Dashboard page fail to load!",true);
        }
    }

    @Override
    public void clickOnCarouselOnWidget(String widgetTitle, String rightOrLeft) throws Exception {
        if (areListElementVisible(widgetsInDashboardPage,10)){
            for (WebElement widgetTemp : widgetsInDashboardPage){
                WebElement title = widgetTemp.findElement(By.cssSelector(".dms-box-title"));
                if (isElementLoaded(title,10)) {
                    if (title.getText().toLowerCase().contains(widgetsNameWrapper(widgetTitle))) {
                        if (widgetsNameWrapper(widgetTitle).equalsIgnoreCase("timesheet approval")) {
                            if (!title.getText().toLowerCase().contains("timesheet approval status")) {
                                List<WebElement> buttonOnCarousel = widgetTemp.findElements(By.cssSelector(".carosel-div .cus-carousel-control"));
                                if (areListElementVisible(buttonOnCarousel, 10)) {
                                    if (rightOrLeft.toLowerCase().contains("left")) {
                                        scrollToElement(buttonOnCarousel.get(0));
                                        click(buttonOnCarousel.get(0));
                                        SimpleUtils.pass("click left on carousel");
                                    } else {
                                        scrollToElement(buttonOnCarousel.get(1));
                                        click(buttonOnCarousel.get(1));
                                        SimpleUtils.pass("click right on carousel");
                                    }
                                    break;
                                }
                            }
                        } else {
                            List<WebElement> buttonOnCarousel = widgetTemp.findElements(By.cssSelector(".carosel-div .cus-carousel-control"));
                            if (areListElementVisible(buttonOnCarousel, 10)) {
                                if (rightOrLeft.toLowerCase().contains("left")) {
                                    scrollToElement(buttonOnCarousel.get(0));
                                    click(buttonOnCarousel.get(0));
                                    SimpleUtils.pass("click left on carousel");
                                } else {
                                    scrollToElement(buttonOnCarousel.get(1));
                                    click(buttonOnCarousel.get(1));
                                    SimpleUtils.pass("click right on carousel");
                                }
                                break;
                            }
                        }
                    }
                } else {
                    SimpleUtils.fail("Widget title element in Dashboard page fail to load",false);
                }
            }
        } else {
            SimpleUtils.fail("Widgets in Dashboard page fail to load!",true);
        }
    }

    @FindBy (css = "lg-compliance[label=\"'dashboard:compliance-violations' | $t\"]")
    private WebElement dataOnComplianceWidget;
    @Override
    public List<String> getDataOnComplianceViolationWidget() throws Exception {
        //0: Past week Total Hours, 1: Past week Violations, 2: Past week Locations
        //3: Current week Total Hours, 4: Current week Violations, 5: Current week Locations
        //6: Future week Total Hours, 7: Future week Violations, 8: Future week Locations
        List<String> resultList= new ArrayList<String>();
        if (isElementLoaded(dataOnComplianceWidget,10)){
            List<WebElement> dataOnWidget = dataOnComplianceWidget.findElements(By.cssSelector("div.dms-number-x-large"));
            List<WebElement> carouselIndicators = dataOnComplianceWidget.findElements(By.cssSelector(".cus-carousel-indicators li"));
            //Go to past week
            clickTheElement(carouselIndicators.get(0));
            for (int i=0;i<3;i++){
                String numberOnComplianceWidget = dataOnWidget.get(i).getText();
                resultList.add(numberOnComplianceWidget);
            }
            //Go to current week
            clickTheElement(carouselIndicators.get(1));
            for (int i=3;i<6;i++){
                String numberOnComplianceWidget = dataOnWidget.get(i).getText();
                resultList.add(numberOnComplianceWidget);
            }
            //Go to future week
            clickTheElement(carouselIndicators.get(2));
            for (int i=6;i<9;i++){
                String numberOnComplianceWidget = dataOnWidget.get(i).getText();
                resultList.add(numberOnComplianceWidget);
            }
        } else {
            SimpleUtils.fail("data on Compliance violation widget fail to load!",false);
        }
        return resultList;
    }

    @FindBy(css = "div.console-navigation-item-label.Compliance")
    private WebElement goToCompliance;
    @Override
    public void goToCompliancePage() throws Exception {
        if (isElementLoaded(goToCompliance,5)){
            click(goToCompliance);
            SimpleUtils.pass("Comliance button clicked!");
        } else {
            SimpleUtils.fail("compliance button is not loaded, please double check!",true);
        }
    }

    @FindBy (css = "lg-search.analytics-new-table-filter input")
    private WebElement searchLocationInCompliancePage;
    @FindBy (css = ".analytics-new-table-group-row-ScheduleCompliance")
    private List <WebElement> searchResultsIncompliacePage;
    @Override
    public List<String> getDataInCompliancePage(String location) throws Exception {
        List<String> resultList= new ArrayList<String>();
        int tempViolation = 0;
        String tempTotalHrs = "";
        if (isElementLoaded(searchLocationInCompliancePage,5)){
            searchLocationInCompliancePage.sendKeys(location);
            if (areListElementVisible(searchResultsIncompliacePage,5)){
                //get values from first row
                waitForSeconds(2);
                for (WebElement element : searchResultsIncompliacePage.get(0).findElements(By.cssSelector("div[class=\"ng-scope col-fx-1\"]"))){
                    scrollToBottom();
                    if (element.getText().toLowerCase().equals("0.0") ){
                        //0.0 no violation
                    } else if (element.getText().toLowerCase().equals("yes")){
                        tempViolation++;
                    } else if (element.getText().toLowerCase().equals("no")){
                        //0.0 no violation
                    } else {
                        tempViolation++;
                    }
                }
                resultList.add(String.valueOf(tempViolation));
                tempTotalHrs = searchResultsIncompliacePage.get(0).findElement(By.cssSelector("div.analytics-new-cell-as-input")).getText();
                //e.g.: 16.0--->16, 16.5--->16.5
                if (tempTotalHrs!="" && tempTotalHrs!=null){
                    if (tempTotalHrs.contains(".0")){
                        //tempTotalHrs.replaceAll(".0","");
                        String a = tempTotalHrs.substring(0,tempTotalHrs.indexOf("."));
                        resultList.add(a);
                    }
                }
                //just has 1 location for SM view
                resultList.add("1");
            } else {
                SimpleUtils.fail("No search result!",true);
            }

        } else {
            SimpleUtils.fail("getDataInCompliancePage: search input fail to load!",true);
        }
        return resultList;
    }

    @FindBy(css = "timesheet-approval-chart text")
    private List<WebElement> textOnTARWidget;
    @Override
    public int getApprovalRateOnTARWidget() throws Exception {
        int approvalRate = 0;
        //TAR: timesheet Aproval Rate
        if (areListElementVisible(textOnTARWidget,5)){
            for (WebElement e : textOnTARWidget){
                waitForSeconds(2);
                if (e.getText().contains("%") && e.getText()!=null && e.getText()!=""){
                    if (!e.getText().toLowerCase().contains("timesheet")){
                        //get value
                        approvalRate += Integer.valueOf(e.getText().substring(0,e.getText().indexOf("%")));
                    }
                }
            }
        }
        return approvalRate;
    }

    //Added By Julie
    @FindBy(css = "lg-open-shifts .dms-box-title")
    private WebElement openShiftsTitle;
    @FindBy(css = "lg-open-shifts .slideNumberText")
    private WebElement openShiftsWeek;
    @FindBy(css = "lg-open-shifts .widgetPieChart")
    private WebElement openShiftsPieChart;
    @FindBy(css = "lg-open-shifts .ana-kpi-legend-entry")
    private List<WebElement> openShiftsLegend;
    @FindBy(css = "lg-open-shifts .emptyState-div")
    private WebElement openShiftsNoContent;
    @FindBy(css = "lg-open-shifts [src*=\"left\"]")
    private WebElement openShiftsLeftArrow;
    @FindBy(css = "lg-open-shifts [src*=\"right\"]")
    private WebElement openShiftsRightArrow;
    @FindBy(css = "lg-open-shifts .cus-carousel-indicators li")
    private List<WebElement> openShiftsCarouselIndicators;
    @FindBy(css = "lg-open-shifts .dms-action-link")
    private WebElement openShiftsBtn;
    @FindBy(css = "[ng-attr-class*=\"fx-center left-banner\"]")
    private List<WebElement> weeksOnSchedules;
    @FindBy(xpath = "//*[@label=\"'Open Shifts'\"]//pie-chart//*[name()='text']")
    private List<WebElement> openShiftsPieChartData;

    @Override
    public void verifyTheContentOfOpenShiftsWidgetLoaded(String currentWeek) throws Exception {
        /*Open Shifts widget should show:
        a. Title: Open Shifts
        b. Default Time: the first day of current week, e.g. Week of Jul 19;
        c. the number of Unclaimed/Claimed on the right and the corresponding pie chart, or EmptyState image with description "There are no open shifts" in gray
        d. Applicable Weeks: carousel-indicators show weeks which include Left arrow, Past, Current, Future week point and Right arrow
        e. [View Schedules] button*/

        if (isElementLoaded(openShiftsTitle, 10) && openShiftsTitle.getText().contains("Open Shifts")) {
            SimpleUtils.pass("Open Shifts: The title of Open Shifts is loaded and correct");
        } else {
            SimpleUtils.fail("Open Shifts: The title of \"Open Shifts\" is not loaded or incorrect!", true);
        }
        if (isElementLoaded(openShiftsWeek, 5) && openShiftsWeek.getText().toLowerCase().contains(currentWeek.toLowerCase())) {
            SimpleUtils.pass("Open Shifts: The week of Open Shifts is loaded and correct!");
        } else {
            SimpleUtils.fail("Open Shifts: The week of \"Open Shifts\" is not loaded or incorrect!", true);
        }
        if (isOpenShiftsNoContent()) {
            SimpleUtils.pass("Open Shifts: There are no open shifts");
        } else if (isOpenShiftsPresent()) {
            SimpleUtils.pass("Open Shifts: Pie chart and Legend are loaded");
        } else {
            SimpleUtils.fail("Open Shifts: Widget info is not loaded", true);
        }
        if (areListElementVisible(openShiftsCarouselIndicators, 5) && openShiftsCarouselIndicators.size() == 3
                && isElementLoaded(openShiftsLeftArrow, 5) && isElementLoaded(openShiftsRightArrow, 5)) {
            SimpleUtils.pass("Open Shifts: Carousel-indicators show weeks which include Left arrow, Past, Current, Future week point and Right arrow");
        } else {
            SimpleUtils.fail("Open Shifts: Carousel-indicators are not loaded", true);
        }
        if (isElementLoaded(openShiftsBtn, 5) && openShiftsBtn.getText().contains("View Schedules")) {
            SimpleUtils.pass("Open Shifts: \"View Schedules\" button is loaded and correct");
        } else {
            SimpleUtils.fail("Open Shifts: \"View Schedules\" button is not loaded and incorrect", true);
        }
    }

    @Override
    public boolean isOpenShiftsPresent() throws Exception {
        Boolean isOpenShiftsPresent = false;
        if (isElementLoaded(openShiftsPieChart, 5) && areListElementVisible(openShiftsLegend, 5)) {
            isOpenShiftsPresent = true;
        }
        return isOpenShiftsPresent;
    }

    @Override
    public boolean isOpenShiftsNoContent() throws Exception {
        Boolean isOpenShiftsNoContent = false;
        if (isElementLoaded(openShiftsNoContent, 5) && openShiftsNoContent.getText().contains("There are no open shifts")) {
            isOpenShiftsNoContent = true;
        }
        return isOpenShiftsNoContent;
    }

    @Override
    public void switchWeeksOnOpenShiftsWidget(String lastWeek, String currentWeek, String nextWeek) throws Exception {
        /*
        a.Can navigate to week by clicking point
        b.Can navigate to week by clicking arrow
         */
        if (areListElementVisible(openShiftsCarouselIndicators, 5) && openShiftsCarouselIndicators.size() == 3) {
            clickTheElement(openShiftsCarouselIndicators.get(0));
            waitForSeconds(2);
            if (openShiftsWeek.getText().toUpperCase().contains(lastWeek)) {
                SimpleUtils.pass("Open Shifts: The first point switches to the last week successfully");
            } else {
                SimpleUtils.fail("Open Shifts: The first point failed to switch to the last week", true);
            }
            clickTheElement(openShiftsCarouselIndicators.get(1));
            waitForSeconds(2);
            if (openShiftsWeek.getText().toUpperCase().contains(currentWeek)) {
                SimpleUtils.pass("Open Shifts: The second point switches to the current week successfully");
            } else {
                SimpleUtils.fail("Open Shifts: The second point failed to switch to the current week", true);
            }
            clickTheElement(openShiftsCarouselIndicators.get(2));
            waitForSeconds(2);
            if (openShiftsWeek.getText().toUpperCase().contains(nextWeek)) {
                SimpleUtils.pass("Open Shifts: The third point switches to the next week successfully");
            } else {
                SimpleUtils.fail("Open Shifts: The third point failed to switch to the next week", true);
            }
        } else {
            SimpleUtils.fail("Open Shifts: The carousel indicators failed to load or load incorrectly", true);
        }
        if (isElementLoaded(openShiftsLeftArrow, 5) && isElementLoaded(openShiftsRightArrow, 5)) {
            clickTheElement(openShiftsLeftArrow);
            waitForSeconds(2);
            if (openShiftsWeek.getText().toUpperCase().contains(currentWeek)) {
                SimpleUtils.pass("Open Shifts: The left arrow switches to the current week successfully");
            } else {
                SimpleUtils.fail("Open Shifts: The left arrow failed to switch to the current week", true);
            }
            clickTheElement(openShiftsLeftArrow);
            waitForSeconds(2);
            if (openShiftsWeek.getText().toUpperCase().contains(lastWeek)) {
                SimpleUtils.pass("Open Shifts: The left arrow switches to the last week successfully");
            } else {
                SimpleUtils.fail("Open Shifts: The left arrow failed to switch to the last week", true);
            }
            click(openShiftsRightArrow);
            waitForSeconds(2);
            if (openShiftsWeek.getText().toUpperCase().contains(currentWeek)) {
                SimpleUtils.pass("Open Shifts: The right arrow switches to the current week successfully");
            } else {
                SimpleUtils.fail("Open Shifts: The right arrow failed to switch to the current week", true);
            }
            click(openShiftsRightArrow);
            waitForSeconds(2);
            if (openShiftsWeek.getText().toUpperCase().contains(nextWeek)) {
                SimpleUtils.pass("Open Shifts: The right arrow switches to the next week successfully");
            } else {
                SimpleUtils.fail("Open Shifts: The right arrow failed to switch to the next week", true);
            }
        }
    }

    @Override
    public String getTheStartOfLastWeekFromSchedulesWidget() throws Exception {
        String lastWeek = "";
        if (areListElementVisible(weeksOnSchedules, 5) && weeksOnSchedules.size() == 4) {
            if (weeksOnSchedules.get(0).getText().contains("—")) {
                lastWeek = weeksOnSchedules.get(0).getText().split("—")[0];
                if (lastWeek.endsWith("\n")) {
                    lastWeek = lastWeek.substring(0, lastWeek.length() - 1);
                }
                if (lastWeek.split(" ")[1].startsWith("0")) {
                    lastWeek = lastWeek.split(" ")[0] + " " + lastWeek.split(" ")[1].substring(1,2);
                }
            }
        }
        if (!lastWeek.isEmpty()) {
            SimpleUtils.pass("Get the start of the last week: \"" + lastWeek + "\" Successfully!");
        } else {
            SimpleUtils.fail("Failed to get the start of the last week!", false);
        }
        return lastWeek;
    }

    @Override
    public String getTheStartOfNextWeekFromSchedulesWidget() throws Exception {
        String nextWeek = "";
        if (areListElementVisible(weeksOnSchedules, 5) && weeksOnSchedules.size() == 4) {
            if (weeksOnSchedules.get(2).getText().contains("—")) {
                nextWeek = weeksOnSchedules.get(2).getText().split("—")[0];
                if (nextWeek.endsWith("\n")) {
                    nextWeek = nextWeek.substring(0, nextWeek.length() - 1);
                }
                if (nextWeek.split(" ")[1].startsWith("0")) {
                    nextWeek = nextWeek.split(" ")[0] + " " + nextWeek.split(" ")[1].substring(1,2);
                }
            }
        }
        if (!nextWeek.isEmpty()) {
            SimpleUtils.pass("Get the start of the next week: \"" + nextWeek + "\" Successfully!");
        } else {
            SimpleUtils.fail("Failed to get the start of the current week!", false);
        }
        return nextWeek;
    }

    @Override
    public HashMap<String, int[]> getDataFromOpenShiftsWidget() throws Exception {
        HashMap<String, int[]> openShiftsData = new HashMap<>();
        int pieChartUnclaimed = 0;
        int pieChartClaimed = 0;
        int legendUnclaimed = 0;
        int legendClaimed = 0;
        if (areListElementVisible(openShiftsPieChartData,5)) {
            for (WebElement pieChartData: openShiftsPieChartData) {
                WebElement pieChartColor = pieChartData.findElement(By.xpath("./../*[name()='path']"));
                if (pieChartColor.getAttribute("style").contains("rgb(169, 169, 169)"))
                    pieChartUnclaimed = Integer.parseInt(pieChartData.getText());
                if (pieChartColor.getAttribute("style").contains("rgb(129, 194, 196)"))
                    pieChartClaimed = Integer.parseInt(pieChartData.getText());
            }
        } else {
            SimpleUtils.fail("Open Shifts: No data on legend", true);
        }
        if (areListElementVisible(openShiftsLegend, 5) && openShiftsLegend.size() == 2) {
            legendUnclaimed = Integer.valueOf(openShiftsLegend.get(0).getText().substring(openShiftsLegend.get(0).getText().indexOf("(") + 1, openShiftsLegend.get(0).getText().indexOf("%")));
            legendClaimed = Integer.valueOf(openShiftsLegend.get(1).getText().substring(openShiftsLegend.get(1).getText().indexOf("(") + 1, openShiftsLegend.get(1).getText().indexOf("%")));
            int[] unclaimedData = new int[]{pieChartUnclaimed, legendUnclaimed};
            int[] claimedData = new int[]{pieChartClaimed, legendClaimed};
            openShiftsData.put("Unclaimed", unclaimedData);
            openShiftsData.put("Claimed", claimedData);
        } else {
            SimpleUtils.fail("Open Shifts: No data on legend", true);
        }
        return openShiftsData;
    }

    @FindBy(css = "[label=\"'dashboard:compliance-violations' | $t\"]")
    private WebElement complianceViolationsWidgetSection;

    @FindBy(css = "[label=\"'dashboard:compliance-violations' | $t\"] [ng-click=\"viewSchedules()\"]")
    private WebElement viewSchedulesLinkOfComplianceViolationsWidget;

    @Override
    public void clickViewSchedulesLinkOfComplianceViolationsWidget() throws Exception {
        if (isElementLoaded(viewSchedulesLinkOfComplianceViolationsWidget, 10)) {
            scrollToElement(viewSchedulesLinkOfComplianceViolationsWidget);
            clickTheElement(viewSchedulesLinkOfComplianceViolationsWidget);
        } else {
            SimpleUtils.fail("View Schedules Link Of Compliance Violations Widget is not loaded!", false);
        }
    }

    @Override
    public boolean checkViewSchedulesLinkOfComplianceViolationsSection() throws Exception {
        boolean isViewSchedulesLinkDisplayed = false;
        if (isElementLoaded(complianceViolationsWidgetSection, 15)) {
            scrollToElement(complianceViolationsWidgetSection);
            isViewSchedulesLinkDisplayed = isElementLoaded(viewSchedulesLinkOfComplianceViolationsWidget, 10);
        } else {
            SimpleUtils.fail("Compliance Violations widget is not loaded!", false);
        }
        return isViewSchedulesLinkDisplayed;
    }

    @FindBy(css = "[label=\"'dashboard:compliance-violations' | $t\"] .slideNumberText")
    private WebElement startDayOfWeekOfComplianceViolationsWidget;

    @Override
    public String getActiveWeekStartDayFromComplianceViolationsWidget() throws Exception {
        String startDayOfWeek = "";
        if (checkViewSchedulesLinkOfComplianceViolationsSection()) {
            if (isElementLoaded(startDayOfWeekOfComplianceViolationsWidget, 10)) {
                startDayOfWeek = startDayOfWeekOfComplianceViolationsWidget.getText().replaceAll(" ", "").toLowerCase();
                startDayOfWeek = startDayOfWeek.replace("weekof", "");
            } else {
                SimpleUtils.fail("Start Day on Compliance Violations Widget is not loaded!", false);
            }
        }

        return startDayOfWeek;
    }
}
