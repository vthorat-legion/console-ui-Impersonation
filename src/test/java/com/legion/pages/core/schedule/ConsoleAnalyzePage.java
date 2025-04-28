package com.legion.pages.core.schedule;

import com.legion.pages.AnalyzePage;
import com.legion.pages.BasePage;
import com.legion.pages.ScheduleMainPage;
import com.legion.pages.core.schedule.ConsoleScheduleMainPage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleAnalyzePage extends BasePage implements AnalyzePage {
    public ConsoleAnalyzePage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(xpath = "//*[@class=\"version-label-container\"]/div")
    private List<WebElement> scheduleHistoryListInAnalyzePopUp;
    @FindBy(css = "lg-close.dismiss")
    private WebElement scheduleAnalyzePopupCloseButtonInKS2;
    @FindBy(className = "sch-schedule-analyze-dismiss")
    private WebElement scheduleAnalyzePopupCloseButton;
    @FindBy(xpath = "//div[contains(text(),'Details')]")
    private WebElement versionDetailsInAnalyzePopUp;
    @FindBy(className = "sch-schedule-analyze-content")
    private WebElement scheduleAnalyzePopup;

    @Override
    public void verifyAnalyzeBtnFunctionAndScheduleHistoryScroll() throws Exception {
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        scheduleMainPage.clickOnScheduleAnalyzeButton();
        for (WebElement e:scheduleHistoryListInAnalyzePopUp
        ) {
            if(verifyScrollBarWorkingInAnalyzePopUP(e)){
                SimpleUtils.report("Staffing Guidance Schedule History-Scrollbar is working correctly version x details");

            }else {
                SimpleUtils.fail("Staffing Guidance Schedule History-Scrollbar is not working correctly version x details",true);
            }
        }
        if (isElementLoaded(scheduleAnalyzePopupCloseButtonInKS2,10)){
            click(scheduleAnalyzePopupCloseButtonInKS2);
            SimpleUtils.pass("close button is working");
        } else if (isElementLoaded(scheduleAnalyzePopupCloseButton)){
            click(scheduleAnalyzePopupCloseButton);
            SimpleUtils.pass("close button is working");
        } else{
            SimpleUtils.fail("No close button on schedule analyse popup",true);
        }

    }

    public boolean verifyScrollBarWorkingInAnalyzePopUP(WebElement element) throws Exception {
        if (areListElementVisible(scheduleHistoryListInAnalyzePopUp,10)&scheduleHistoryListInAnalyzePopUp.size()>4) {
            SimpleUtils.report("versions are more enough and there is a scroll bar to check details");
            scrollToElement(element);
            click(element);
            String versionNubScrollToText = versionDetailsInAnalyzePopUp.getText().trim().split(" ")[1];
            if (versionNubScrollToText.equals(element.getText().trim().split(" ")[1])) {
                SimpleUtils.pass("scroll bar can work normally");
                return  true;
            }else {
                SimpleUtils.fail("scroll bar can not  work normally",true);
            }
        }else if(scheduleHistoryListInAnalyzePopUp.size()<=4){

            SimpleUtils.report("there are some versions,but not scroll bar");
            click(element);
            String versionNubScrollToText = versionDetailsInAnalyzePopUp.getText().trim().split(" ")[1];
            if (versionNubScrollToText.equals(element.getText().trim().split(" ")[1])) {
                SimpleUtils.pass("schedule version work well");
                return  true;
            }else {
                SimpleUtils.fail("schedule version doesn't work well",true);
            }

        }
        return  false;
    }

    public void closeStaffingGuidanceAnalyzePopup() throws Exception {
        if (isStaffingGuidanceAnalyzePopupAppear()) {
            click(scheduleAnalyzePopupCloseButton);
        }
    }

    public Boolean isStaffingGuidanceAnalyzePopupAppear() throws Exception {
        if (isElementLoaded(scheduleAnalyzePopup)) {
            return true;
        }
        return false;
    }

    @FindBy(css = "lg-button[label=\"Analyze\"]")
    private WebElement analyzeBtn;
    @FindBy(css="div[ng-click=\"selectedTab = 'history'\"]")
    private WebElement scheduleHistoryTab;
    @FindBy(css="div[ng-click=\"selectedTab = 'labor'\"]")
    private WebElement laborGuidanceTab;
    @FindBy(xpath="//tr[@class='ng-scope sch-schedule-analyze__versions-selected']")
    private WebElement version1;
    @FindBy(xpath="//tr[@class='ng-scope']")
    private WebElement version0;
    @FindBy(xpath="//lg-close[@id='legion_cons_Schedule_Schedule_Analyze_Close_button']")
    private WebElement closeSchHistory;

    @Override
    public void clickOnAnalyzeBtn(String tab) throws Exception {
        if (isElementLoaded(analyzeBtn, 15)) {
            click(analyzeBtn);
            SimpleUtils.pass("Clicked analyze button!");
            if (tab.toLowerCase().contains("history")) {
                if (isElementLoaded(scheduleHistoryTab, 15)) {
                    click(scheduleHistoryTab);
                    SimpleUtils.pass("Clicked schedule HistoryTab!");
                    // === Version 0 Check ===
                    if (isElementLoaded(version0, 10)) {
                        String version0Text = version0.getText();
                        if (version0Text.contains("0.0") && version0Text.toLowerCase().contains("impersonating")) {
                            SimpleUtils.pass("Version 0.0 shows impersonator details :- " + version0Text);
                        } else {
                            SimpleUtils.fail("Version 0.0 does not show impersonator details.", true);
                        }
                    } else {
                        SimpleUtils.fail("Version 0 element not loaded.", false);
                    }

                    // === Version 1 Check ===
                    if (isElementLoaded(version1, 10)) {
                        String version1Text = version1.getText();
                        if (version1Text.contains("1.0") && version1Text.toLowerCase().contains("impersonating")) {
                            SimpleUtils.pass("Version 1.0 shows impersonator details."+version1Text);
                        } else {
                            SimpleUtils.fail("Version 1.0 does not show impersonator details :- ", true);
                        }
                    } else {
                        SimpleUtils.fail("Version 1 element not loaded.", true);
                    }

                } else {
                    SimpleUtils.fail("There is no schedule History Tab!", true);
                }
            }
            waitForSeconds(10);
             click(closeSchHistory);
        }
    }



//    @Override
//    public void clickOnAnalyzeBtn(String tab) throws Exception {
//        if (isElementLoaded(analyzeBtn,15)){
//            click(analyzeBtn);
//            SimpleUtils.pass("Clicked analyze button!");
//            if (tab.toLowerCase().contains("history")){
//                if (isElementLoaded(scheduleHistoryTab,15)){
//                    click(scheduleHistoryTab);
//                    SimpleUtils.pass("Clicked schedule HistoryTab!");
//                } else {
//                    SimpleUtils.fail("There is no schedule History Tab!", false);
//                }
//webelement version0 - please check if shows 0.0 as version and included text "impersonating" then passed as Vesion 0.0 shows impersonator details
//                webelement version1 - please check if shows 1.0 as version and included text "impersonating" then passed as Vesion 1.0 shows impersonator details
//
////            } else if (tab.toLowerCase().contains("labor")){
////                if (isElementLoaded(laborGuidanceTab,15)){
////                    click(laborGuidanceTab);
////                    SimpleUtils.pass("Clicked labor guidance Tab!");
////                } else {
////                    SimpleUtils.fail("There is no labor guidance Tab!", false);
////                }
//            } else {
//                SimpleUtils.fail("No this tab:"+ tab, false);
//            }
//        } else {
//            SimpleUtils.fail("There is no Analyze button!", false);
//        }
//    }



    @FindBy(css = ".sch-schedule-analyze__grey tr")
    private List<WebElement> scheduleVersionInfo;

    @Override
    public void verifyScheduleVersion(String version) throws Exception {
        if (areListElementVisible(scheduleVersionInfo,15) && areListElementVisible(scheduleVersionInfo.get(scheduleVersionInfo.size()-1).findElements(By.tagName("td")),15)){
            String versionText = scheduleVersionInfo.get(scheduleVersionInfo.size()-1).findElements(By.tagName("td")).get(0).getText().split("\n")[0];
            if ("".equals(versionText)){
                versionText = scheduleVersionInfo.get(scheduleVersionInfo.size()-1).findElements(By.tagName("td")).get(1).getText().split("\n")[0];
            }
            if(version.equalsIgnoreCase(versionText)){
                SimpleUtils.pass("version info is correct!");
            }else {
                SimpleUtils.fail("Version info is incorrect!", false);
            }
        } else {
            SimpleUtils.fail("There is no schedule version info!", false);
        }
    }

    @FindBy(css = "lg-close.dismiss")
    private WebElement closeAnalyzeBtn;
    @Override
    public void closeAnalyzeWindow() throws Exception {
        if (isElementLoaded(closeAnalyzeBtn,15)){
            click(closeAnalyzeBtn);
            SimpleUtils.pass("Clicked close button!");
        } else {
            SimpleUtils.fail("There is no close button!", false);
        }
    }

    @FindBy(css = ".sch-schedule-analyze__content pie-chart")
    private WebElement pirChartUnderLaborGuidanceTab;
    @FindBy(css = ".sch-schedule-analyze__content .sch-schedule-analyze__total-hours")
    private WebElement totalHrsUnderLaborGuidanceTab;
    @Override
    public String getPieChartTotalHrsFromLaborGuidanceTab() throws Exception {
        float result = 0;
        if (isElementLoaded(pirChartUnderLaborGuidanceTab, 10)){
            SimpleUtils.pass("Pie chart load successfully!");
            List<WebElement> textsOnPieChart = pirChartUnderLaborGuidanceTab.findElements(By.cssSelector("text"));
            if (areListElementVisible(textsOnPieChart, 10)){
                for (WebElement element: textsOnPieChart){
                    if (SimpleUtils.isNumeric(element.getText().toLowerCase().replace(",", "").replace("hrs",""))){
                        result = result + Float.parseFloat(element.getText().toLowerCase().replace(",", "").replace("hrs",""));
                    } else {
                        SimpleUtils.fail("The text is not a number!", false);
                    }
                }
            } else {
                SimpleUtils.fail("There is no text on the pie chart!", false);
            }
        } else {
            SimpleUtils.fail("There is no pie chart!", false);
        }
        if (isElementLoaded(totalHrsUnderLaborGuidanceTab, 10)){
            if (totalHrsUnderLaborGuidanceTab.getText().replace(",","").contains(String.valueOf(result).replace(".0", ""))){
                SimpleUtils.pass("Total hours is consistent!");
                return totalHrsUnderLaborGuidanceTab.getText().replace(",","");
            } else {
                SimpleUtils.fail("Total hours is inconsistent!", false);
            }
        }  else {
            SimpleUtils.fail("There is no total value for the pie chart!", false);
        }
        return null;
    }

    @FindBy(css = ".sch-schedule-analyze__total-hours")
    private List<WebElement> textsForPieChartsInAnalyzeHistoryTab;
    @FindBy(css = ".sch-schedule-analyze__graph-header")
    private List<WebElement> pieChartHeadersInAnalyzeHistoryTab;

    @Override
    public String getPieChartTotalHrsFromHistoryTab(String scheduledOrGuidance) throws Exception{
        if (areListElementVisible(textsForPieChartsInAnalyzeHistoryTab, 10) && textsForPieChartsInAnalyzeHistoryTab.size()==2){
            if (scheduledOrGuidance.toLowerCase().contains("schedule")){
                return textsForPieChartsInAnalyzeHistoryTab.get(0).getText();
            }
            if (scheduledOrGuidance.toLowerCase().contains("guidance")){
                return textsForPieChartsInAnalyzeHistoryTab.get(1).getText();
            }
        }
        return null;
    }

    @Override
    public String getPieChartHeadersFromHistoryTab(String scheduledOrGuidance) throws Exception{
        waitForSeconds(3);
        if (areListElementVisible(pieChartHeadersInAnalyzeHistoryTab, 10) && pieChartHeadersInAnalyzeHistoryTab.size()==2){
            if (scheduledOrGuidance.toLowerCase().contains("schedule")){
                return pieChartHeadersInAnalyzeHistoryTab.get(0).getText();
            }
            if (scheduledOrGuidance.toLowerCase().contains("guidance")){
                return pieChartHeadersInAnalyzeHistoryTab.get(1).getText();
            }
        }
        return null;
    }

    @FindBy(css = "div.sch-schedule-analyze__hours-legend")
    private WebElement workRoleSectionFromLaborGuidance;
    @Override
    public ArrayList<HashMap<String, String>> getLaborGuidanceWorkRoleStyleInfo() throws Exception {
        ArrayList<HashMap<String,String>> workRoleInfo = new ArrayList<>();
        if (workRoleSectionFromLaborGuidance.findElements(By.cssSelector("div.sch-schedule-analyze__role-title")).size() > 0) {
            for (WebElement row : workRoleSectionFromLaborGuidance.findElements(By.cssSelector("div.sch-schedule-analyze__role-title"))) {
                HashMap<String, String> workRoleInfoInEachRow = new HashMap<>();
                workRoleInfoInEachRow.put("WorkRoleName", row.getText().substring(row.getText().indexOf("Hrs")+3).trim().toLowerCase());
                workRoleInfoInEachRow.put("WorkRoleStyle", row.findElement(By.cssSelector("span[style]")).getAttribute("style"));
                workRoleInfo.add(workRoleInfoInEachRow);
            }
        }
        return workRoleInfo;
    }
}
