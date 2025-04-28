package com.legion.pages.core.OpCommons;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleNavigationPage extends BasePage {
    public ConsoleNavigationPage() {
        PageFactory.initElements(getDriver(), this);
    }
    // Added by Sophia

    //LocationBar
    @FindBy(css = "div#id_upperfield-search")
    private WebElement upperFieldSearch;
    @FindBy(css = "lg-upperfield-search lg-search>input-field input")
    private WebElement searchBox;
    @FindBy(css = "lg-upperfield-search div.lg-search-icon.ng-scope")
    private WebElement searchIcon;
    @FindBy(css = "lg-search-options[search-hint='Search'] div.lg-search-options__scroller div.lg-search-options__option-wrapper")
    private WebElement searchResult;

    //Console navigation panel
    @FindBy(id = "legion_cons_Dashboard_tab")
    private WebElement dashBoard;
    @FindBy(id = "legion_cons_Team_tab")
    private WebElement team;
    @FindBy(id = "legion_cons_Schedule_tab")
    private WebElement schedule;
    @FindBy(id = "legion_cons_Timesheet_tab")
    private WebElement timeSheet;
    @FindBy(id = "legion_cons_Compliance_tab")
    private WebElement compliance;
    @FindBy(id = "legion_cons_Report_tab")
    private WebElement report;
    @FindBy(id = "legion_cons_Inbox_tab")
    private WebElement inbox;
    @FindBy(id = "legion_cons_Admin_tab")
    private WebElement admin;
    @FindBy(id = "legion_cons_Integration_tab")
    private WebElement integration;
    @FindBy(css = "div[title='News']")
    private WebElement news;
    @FindBy(id = "legion_cons_Controls_tab")
    private WebElement controls;
    @FindBy(id = "legion_cons_Logout_tab")
    private WebElement logout;
    @FindBy(id = "legion_cons_Plan_tab")
    private WebElement plan;

    public void searchLocation(String locationName) {
        upperFieldSearch.click();
        searchBox.clear();
        searchBox.sendKeys(locationName);
        searchIcon.click();
        waitForSeconds(3);
        if (isElementDisplayed(searchResult)) {
            searchResult.click();
            waitForSeconds(5);
        } else {
            System.out.println("There is no location: " + locationName);
        }
    }

    public void navigateTo(String module) {
        WebElement element = null;
        if (module.equalsIgnoreCase("Dashboard")) {
            element = dashBoard;
        } else if (module.equalsIgnoreCase("Team")) {
            element = team;
        } else if (module.equalsIgnoreCase("Schedule")) {
            element = schedule;
        } else if (module.equalsIgnoreCase("Timesheet")) {
            element = timeSheet;
        } else if (module.equalsIgnoreCase("Compliance")) {
            element = compliance;
        } else if (module.equalsIgnoreCase("Report")) {
            element = report;
        } else if (module.equalsIgnoreCase("Inbox")) {
            element = inbox;
        } else if (module.equalsIgnoreCase("Report")) {
            element = report;
        } else if (module.equalsIgnoreCase("Admin")) {
            element = admin;
        } else if (module.equalsIgnoreCase("Integration")) {
            element = integration;
        } else if (module.equalsIgnoreCase("News")) {
            element = news;
        } else if (module.equalsIgnoreCase("Controls")) {
            element = controls;
        }else if (module.equalsIgnoreCase("Plan")) {
            element = plan;
        }else if (module.equalsIgnoreCase("Logout")) {
            element = logout;
        }else {
            System.out.println("No such Module!");
        }
        element.click();
        waitForSeconds(5);
    }
    @FindBy (css = "div.no-left-right-padding.ng-scope")
    private WebElement dashBoardTab;
    @FindBy (css = "div.col-xs-12.no-left-right-padding.ng-scope")
    private WebElement reportTab;
    @FindBy (css = "lg-dashboard-card-wrapper.ng-scope")
    private WebElement consoleTab;
    @FindBy(css = "div.lg-scenario-table-improved.ng-scope")
    private WebElement planTab;
    @FindBy(css = "lg-button[label = 'Create Plan'] > button")
    private WebElement createPlanButton;
    @FindBy(css = "input[placeholder = 'Search by labor budget name']")
    private WebElement searchLaborBudget;

    public void verifyOtherTableIsNormal(){
        navigateTo("DashBoard");
        if(dashBoardTab.isDisplayed()){
            SimpleUtils.pass("DashBoard page is normal");
        }else
            SimpleUtils.fail("DashBoard page is not normal",false);

        navigateTo("Report");
        if(reportTab.isDisplayed()){
            SimpleUtils.pass("Report page is normal");
        }else
            SimpleUtils.fail("Report page is not normal",false);

        navigateTo("Inbox");
        if(reportTab.isDisplayed()){
            SimpleUtils.pass("Inbox page is normal");
        }else
            SimpleUtils.fail("Inbox page is not normal",false);

        navigateTo("Controls");
        if(consoleTab.isDisplayed()){
            SimpleUtils.pass("Controls page is normal");
        }else
            SimpleUtils.fail("Controls page is not normal",false);

        if(isExist(plan)){
            navigateTo("plan");
            if(searchLaborBudget.isDisplayed()){
                SimpleUtils.pass("Plan page is normal");
            }else
                SimpleUtils.fail("Plan page is not normal",false);
        }

        if(isExist(admin)){
            navigateTo("Admin");
            if(reportTab.isDisplayed()){
                SimpleUtils.pass("Admin page is normal");
            }else
                SimpleUtils.fail("Admin page is not normal",false);
        }

        if(isExist(integration)){
            navigateTo("Integration");
            if(reportTab.isDisplayed()){
                SimpleUtils.pass("Intergation page is normal");
            }else
                SimpleUtils.fail("Intergation page is not normal",false);
        }
    }

    @FindBy (css = "div.sov.ng-scope")
    private WebElement scheduleTab;

    public void verifySchedulePageIsNormal(){
        if(scheduleTab.isDisplayed()){
            SimpleUtils.pass("Schedule table is normal");
        }else
            SimpleUtils.fail("Schedule table is not normal",false);
    }

    @FindBy(css ="div.table-view")
    private WebElement timeSheetTab;

    public void verifytimeSheetPageIsNormal(){
        if(timeSheetTab.isDisplayed()){
            SimpleUtils.pass("TimeSheet page is normal");
        }else
            SimpleUtils.fail("TimeSheet page is not normal",false);
    }

    @FindBy(css = "div[role = 'table']")
    private WebElement teamTable;

    public void verifyTeamPageIsNormal(){
        if(teamTable.isDisplayed()){
            SimpleUtils.pass("Team page is normal");
        }else
            SimpleUtils.fail("Team page is not normal",false);
    }

    @FindBy(css = "div.analytics-new-table.ng-scope")
    private WebElement complianceTable;
    public void verifyCompliancePageIsNormal(){
        if(complianceTable.isDisplayed()){
            SimpleUtils.pass("Compliance page is normal");
        }else
            SimpleUtils.fail("Compliance page is not normal",false);
    }

    @FindBy(css = "p.nodata-title.ng-binding")
    private WebElement noData;

    public void verifyPageEmpty(){
        if(noData.getAttribute("innerText").equals("No data to show at this level")){
            SimpleUtils.pass("Page is empty");
        }else
            SimpleUtils.fail("Page is not empty",false);
    }

    @FindBy(css = "div.analytics-new-table.ng-scope")
    private WebElement scheduleTabForDis;

    public void verifySchedulePageForDisIsNormal(){
        if(scheduleTabForDis.isDisplayed()){
            SimpleUtils.pass("Schedule table for distinct is normal");
        }else
            SimpleUtils.fail("Schedule table for distinct is not normal",false);
    }

    @FindBy(css = "div.card-carousel.row-fx")
    private WebElement timeSheetTabForDis;

    public void verifytimeSheetPageForDisIsNormal(){
        if(timeSheetTabForDis.isDisplayed()){
            SimpleUtils.pass("TimeSheet page for distinct is normal");
        }else
            SimpleUtils.fail("Schedule page for distinct is not normal",false);
    }

    public void verifyOnlyTeamIsGray(){
        if(team.getAttribute("class").contains("gray-item") && !compliance.getAttribute("class").contains("gray-item")
            && !dashBoard.getAttribute("class").contains("gray-item") && !schedule.getAttribute("class").contains("gray-item")
            && !timeSheet.getAttribute("class").contains("gray-item") && !controls.getAttribute("class").contains("gray-item")
            && !report.getAttribute("class").contains("gray-item") && !inbox.getAttribute("class").contains("gray-item")){
            SimpleUtils.pass("Only team is gray out");
        }else
            SimpleUtils.fail("Team is not gray out or other is gray out",false);
    }

    public void verifyOnlyComplianceIsGray(){
        if(!team.getAttribute("class").contains("gray-item") && compliance.getAttribute("class").contains("gray-item")
                && !dashBoard.getAttribute("class").contains("gray-item") && !schedule.getAttribute("class").contains("gray-item")
                && !timeSheet.getAttribute("class").contains("gray-item") && !controls.getAttribute("class").contains("gray-item")
                && !report.getAttribute("class").contains("gray-item") && !inbox.getAttribute("class").contains("gray-item")){
            SimpleUtils.pass("Only compliance is gray out");
        }else
            SimpleUtils.fail("Compliance is not gray out or other is gray out",false);
    }

    public void verifyFourTabAreGray(){
        if(team.getAttribute("class").contains("gray-item") && compliance.getAttribute("class").contains("gray-item")
                && !dashBoard.getAttribute("class").contains("gray-item") && schedule.getAttribute("class").contains("gray-item")
                && timeSheet.getAttribute("class").contains("gray-item") && !controls.getAttribute("class").contains("gray-item")
                && !report.getAttribute("class").contains("gray-item") && !inbox.getAttribute("class").contains("gray-item")){
            SimpleUtils.pass("Only compliance is gray out");
        }else
            SimpleUtils.fail("Compliance is not gray out or other is gray out",false);
    }
}
