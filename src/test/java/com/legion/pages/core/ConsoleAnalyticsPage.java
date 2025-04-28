package com.legion.pages.core;

import static com.legion.utils.MyThreadLocal.getDriver;

import java.util.HashMap;
import java.util.List;

import cucumber.api.java.ro.Si;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;
import com.legion.pages.AnalyticsPage;
import com.legion.pages.BasePage;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.SimpleUtils;

public class ConsoleAnalyticsPage extends BasePage implements AnalyticsPage{
	
	 @FindBy(css="div.console-navigation-item-label.Analytics")
	 private WebElement consoleAnalyticsPageTabElement;
	 
	 @FindBy(className="analytics-dashboard-section")
	 private WebElement analyticsSectionsDivClass;
	 
	 @FindBy(className="analytics-dashboard-section")
	 private List<WebElement> analyticsDivElements;
	 
	 @FindBy(css="[ng-click=\"selectType('All')\"]")
	 private WebElement analyticsAllHoursCheckBox;
	 
	 @FindBy(css="[ng-click=\"selectType('Peak')\"]")
	 private WebElement analyticsPeakHoursCheckBox;
	 
	 @FindBy(css="[ng-if=\"hasHoursData()\"]")
	 private WebElement analyticsHoursDataSectionDiv;
	 
	 @FindBy(css=".analytics-dashboard-sub-section")
	 private WebElement analyticsSubSection;
	 
	 @FindBy(css=".analytics-dashboard-sub-section")
	 private List<WebElement> analyticsShiftOffersSubSectionElements;
	 
	 @FindBy(css=".analytics-dashboard-sub-section")
	 private List<WebElement> teamMemberSatisfactionsElements;
	 
	 @FindBy(css = "div.console-navigation-item.active")
	 private WebElement activeConsoleMenuItem;
	  
	 @FindBy(className="sub-title")
	 private WebElement sectionSubTitleDiv;
	    
	 @FindBy(className="[ng-if=\"showRoleFilter()\"]")
	 private WebElement TMSShowRoleFilterDropDownButton;
	 
	 @FindBy(css="ul.dropdown-menu.dropdown-menu-right")
	 private WebElement TMSDropdownMenuDiv;
	 
	 @FindBy(css="[ng-click=\"selectChoice($event, choice)\"]")
	 private WebElement TMSDropdownMenuOptionsList;
	 
	 @FindBy(css="[ng-click=\"selectChoice($event, choice)\"]")
	 private List<WebElement> TMSDropdownMenuOptionsListElements;
	
	 @FindBy(className="sub-navigation-view-link")
	 private WebElement analyticsSubNavigationView;
	 
	 @FindBy (css = "div.console-navigation-item-label.Analytics")
	 private WebElement analyticsConsoleName;
	 
	 @FindBy(css = "div[data=\"hours\"]")
	 private WebElement forecastedHoursElement;
		
	 @FindBy(css = "div[data=\"scheduleHours\"]")
	 private WebElement scheduleHoursElement;
	 
	 @FindBy(css = "div.sch-calendar-navigation.right")
	 private WebElement navigationArrowRight;
	 
	 @FindBy(css= "div.sch-calendar-day-dimension.sch-calendar-day")
	 private List<WebElement> analyticsCalendarDays;

	 @FindBy(css= "div.sch.ng-scope")
	 private WebElement alalyticsDivReportElement;

	 @FindBy(xpath= "//span[contains(text(),'Reports')]")
	 private WebElement analyticsReportTab;


	 @FindBy(css = "div.sub-navigation-view-link")
	 private List<WebElement> analyticsSubTabs;

	 @FindBy(css="div.sub-navigation-view-link.active")
	 private WebElement analyticsActiveSubTab;

	 @FindBy(css = "tr[ng-repeat=\"kpi in kpiReports\"]")
	 private List<WebElement> kpiReportRows;

	 @FindBy(css = "div.console-navigation-item-label.Report")
	 private WebElement consoleNavigationReport;

	 @FindBy(css = "div.lgnCheckBox")
	 private List<WebElement> forecstAndScheduleCheckBoxes;

	 @FindBy(css = "div[aria-label=\"A tabular representation of the data in the chart.\"]")
	 private List<WebElement> hidenTablesData;


	public ConsoleAnalyticsPage(){
		PageFactory.initElements(getDriver(), this);
	}
	
	@Override
	public void gotoAnalyticsPage() throws Exception {
		if(isElementLoaded(consoleAnalyticsPageTabElement))
		{
			activeConsoleName = analyticsConsoleName.getText();
			click(consoleAnalyticsPageTabElement);
			if(isElementLoaded(analyticsSectionsDivClass))
			{
				for(WebElement analyticsDivElement:analyticsDivElements)
				{
					if(analyticsDivElement.getText().contains("Staffing Forecast Accuracy"))
					{
						System.out.println("Staffing Forecast Accuracy");
						//getStaffingForecastAccuracy(analyticsDivElement);
					}
					else if(analyticsDivElement.getText().contains("Team Member Satisfaction"))
					{
						ExtentTestManager.getTest().log(Status.INFO, "\n******************** Team Member Satisfaction Logs *****************************");

						getTeamMemberSatisfaction(analyticsDivElement);
					}
					else if(analyticsDivElement.getText().contains("Schedule Compliance"))
					{
						ExtentTestManager.getTest().log(Status.INFO,"\n******************** Schedule Compliance Logs *****************************");

//						getScheduleCompliance(analyticsDivElement);
					}
					else if(analyticsDivElement.getText().contains("Shift Offers"))
					{
						ExtentTestManager.getTest().log(Status.INFO,"\n******************** Shift Offers Logs *****************************");
						getShiftOffers(analyticsDivElement);
					}
				}
			}
		}
		
		/*
		 * Change tab to report
		 */
		if(isElementLoaded(analyticsSubNavigationView))
		{
			ExtentTestManager.getTest().log(Status.INFO,"\n******************** Analytics Report Logs *****************************");
			List<WebElement> analyticsSubNavigationViewTabs = getDriver().findElements(By.className("table-responsive"));
			for(WebElement analyticsSubNavigationViewTab : analyticsSubNavigationViewTabs) {
				if(analyticsSubNavigationViewTab.getText().contains("REPORTS"))
				{
					analyticsSubNavigationViewTab.click();
					SimpleUtils.pass("Analytics Report Section report Table Loaded Successfully!");
							
				}
			}
		}
		
	}
	
	public void getStaffingForecastAccuracy(WebElement analyticsDivElement) throws Exception
	{
		String analyticsTMSHasHoursDataSectionText = "";
		if(isElementLoaded(analyticsHoursDataSectionDiv)){
			SimpleUtils.pass("Analytics Staffing Forecast Accuracy Section Loaded Successfully!");
		}else{
			SimpleUtils.fail("Analytics Staffing Forecast Accuracy Section not Loaded Successfully!",true);
		}

		String allHoursCheckboxClasses = analyticsAllHoursCheckBox.getAttribute("class");
		String peakHoursCheckboxClasses = analyticsPeakHoursCheckBox.getAttribute("class");
		if(!allHoursCheckboxClasses.contains("checked"))
		{
			click(analyticsAllHoursCheckBox);
		}
		
		if(!peakHoursCheckboxClasses.contains("checked"))
		{
			click(analyticsPeakHoursCheckBox);
		}
		analyticsTMSHasHoursDataSectionText = analyticsHoursDataSectionDiv.getText();
		
		if(analyticsTMSHasHoursDataSectionText.contains("All Hrs")){
			SimpleUtils.pass("All Hours under Analytics Staffing Forecast Accuracy Section Loaded Successfully!");
		}else{
			SimpleUtils.fail("All Hours under Analytics Staffing Forecast Accuracy Section not Loaded Successfully!",true);
		}
	
		if(analyticsTMSHasHoursDataSectionText.contains("Peak Hrs")){
			SimpleUtils.pass("Peak Hours under Analytics Staffing Forecast Accuracy Section Loaded Successfully!");
		}else{
			SimpleUtils.fail("Peak Hours under Analytics Staffing Forecast Accuracy Section not Loaded Successfully!",true);
		}
		
	}
	
	public void getTeamMemberSatisfaction(WebElement analyticsDivElement) throws Exception {
		
		if(isElementLoaded(analyticsSubSection)){
			SimpleUtils.pass("Analytics Team Member Satisfaction Section Loaded Successfully!");
		}else{
			SimpleUtils.fail("Analytics Team Member Satisfaction Section not Loaded Successfully!",false);
		}
			
		if(isElementLoaded(TMSShowRoleFilterDropDownButton))
		{
			click(TMSShowRoleFilterDropDownButton);
			if(isElementLoaded(TMSDropdownMenuDiv))
			{
								
				for(WebElement TMSDropdownMenuOptionsListElement : TMSDropdownMenuOptionsListElements)
				{
					int dropdownIndex = TMSDropdownMenuOptionsListElements.indexOf(TMSDropdownMenuOptionsListElement);
					TMSDropdownMenuOptionsListElement.click();
					if(dropdownIndex > 1)
						TMSDropdownMenuOptionsListElements.get(dropdownIndex - 1).click();
					waitForSeconds(2000);
					for(WebElement teamMemberSatisfactionsElement : teamMemberSatisfactionsElements)
					{
						if(isElementLoaded(sectionSubTitleDiv))
						{
							String sectionSubHeaderText = sectionSubTitleDiv.getText();
							ExtentTestManager.getTest().log(Status.INFO,"Team Member Satisfaction - '"+sectionSubHeaderText+"' Loaded Successfully for - '"+TMSDropdownMenuOptionsListElement.getText()+"' Filter!");
						}
					}
				}
				click(TMSShowRoleFilterDropDownButton);
			}
		}
	}
	
	
	
	public void getShiftOffers(WebElement analyticsDivElement) throws Exception {
		if(isElementLoaded(analyticsSubSection))
		{
			for(WebElement analyticsShiftOffersSubSectionElement : analyticsShiftOffersSubSectionElements)
			{
				ExtentTestManager.getTest().log(Status.INFO,"Analytics Shift Offers Section - '"+analyticsShiftOffersSubSectionElement.getText()+"' Loaded Successfully!");
			}
		}
	}
	
	@Override
	public HashMap<String, Float> getForecastedHours() throws Exception {
		HashMap<String, Float> forecastedHours = new HashMap<String, Float>();
		if(isElementLoaded(forecastedHoursElement))
		{
			forecastedHours.put("forecastedHours", Float.valueOf(forecastedHoursElement.getText().replace(",", "").split("\n")[3]));
			forecastedHours.put("forecastedActualHours", Float.valueOf(forecastedHoursElement.getText().replace(",", "").split("\n")[4]));
		}
		return forecastedHours;
	}
	
	
	@Override
	public HashMap<String, Float> getScheduleHours() throws Exception {
		HashMap<String, Float> forecastedHours = new HashMap<String, Float>();
		if(isElementLoaded(scheduleHoursElement))
		{
			forecastedHours.put("initialScheduleHours", Float.valueOf(scheduleHoursElement.getText().replace(",", "").split("\n")[3]));
			forecastedHours.put("publishedScheduleHours", Float.valueOf(scheduleHoursElement.getText().replace(",", "").split("\n")[4]));
		}
		return forecastedHours;
	}

	
	@Override
	public void navigateToNextWeek() throws Exception {
		if(isElementLoaded(navigationArrowRight))
		{
			click(navigationArrowRight);
			SimpleUtils.pass("Analytics: Navigated to next week Successfully!");
		}
		else {
			SimpleUtils.fail("Analytics: Navigated to next week arrow not loaded!", false);
		}
		
	}

	@Override
	public String getAnalyticsActiveDuration() {
		String duration = "";
		if(analyticsCalendarDays.size() != 0)
		{
			duration = analyticsCalendarDays.get(0).getText().replace("\n", " ") + " - " + analyticsCalendarDays.get(analyticsCalendarDays.size() - 1).getText().replace("\n", " ");
		}
		return duration;
	}


	//added by Gunjan

	public boolean loadAnalyticsSubTab() throws Exception{
		boolean flag = true;
		if(isElementLoaded(consoleAnalyticsPageTabElement)){
			//activeConsoleName = analyticsConsoleName.getText();
			click(consoleAnalyticsPageTabElement);
			if(analyticsDivElements.size()!=0){
				for(int i=0;i< analyticsDivElements.size();i++){
					if(isElementLoaded(analyticsDivElements.get(i))){
						SimpleUtils.pass("Analytics sub-tab element loaded successfully!");
						flag = true;
						return flag;
					}else{
						SimpleUtils.report("Analytics sub-tab element not Loaded Successfully for few !");
					}
				}

			}else{
				SimpleUtils.fail("Analytics sub-tab not Loaded Successfully!",true);
				flag = false;
				return flag;
			}
		}else{
			SimpleUtils.fail("Analytics menu Tab not Loaded Successfully!",true);
		}
		return flag;

	}

	public boolean loadReportsSubTab() throws Exception{
		boolean flag = true;
		if(isElementLoaded(analyticsReportTab)){
			//activeConsoleName = analyticsConsoleName.getText();
			click(analyticsReportTab);
			if(isElementLoaded(alalyticsDivReportElement)){
				SimpleUtils.pass("Analytics Report Section Loaded Successfully!");
				flag = true;
				return flag;
				}else{
					SimpleUtils.fail("Analytics Report Section not Loaded Successfully for few !",true);
					flag = false;
					return flag;
			}
		}else{
			SimpleUtils.fail("Reports Analytics sub-menu Tab not Loaded Successfully!",true);

		}
		return flag;

	}

	@Override
	public boolean loadAnalyticsTab() throws Exception{
		// TODO Auto-generated method stub
		boolean flag=true;
		boolean resultLoadAnalyticsSubTab=loadAnalyticsSubTab();
		boolean resultLoadReportsSubTab=loadReportsSubTab();
		if(resultLoadAnalyticsSubTab==true && resultLoadReportsSubTab==true){
			SimpleUtils.pass("Analytics tab loaded successfully");
			flag = true;
			return flag;
		}else if(resultLoadAnalyticsSubTab!=true || resultLoadReportsSubTab!=true){
			SimpleUtils.fail("Analytics tab not loaded successfully",true);
		}else {
			SimpleUtils.fail("Analytics tab not loaded successfully",true);
		}
		return flag;
	}


	@Override
	public void clickOnAnalyticsSubTab(String subTabLabel) throws Exception
	{
		if(analyticsSubTabs.size() > 0) {
			for(WebElement analyticsSubTab : analyticsSubTabs) {
				if(analyticsSubTab.getText().toLowerCase().contains(subTabLabel.toLowerCase()))
					click(analyticsSubTab);
			}

			if(isElementLoaded(analyticsActiveSubTab) && analyticsActiveSubTab.getText().toLowerCase().contains(subTabLabel.toLowerCase()))
				SimpleUtils.pass("Analytics Page: sub tab '"+ subTabLabel +"' selected successfully.");
			else
				SimpleUtils.fail("Analytics Page: sub tab '"+ subTabLabel +"' not selected.", false);
		}
		else
			SimpleUtils.fail("Analytics Page: Sub tabs not loaded.", false);
	}

	@Override
	public void exportKPIReportByTitle(String kpiTitle) throws Exception
	{
		if(kpiReportRows.size() > 0) {
			for(WebElement kpiReportRow : kpiReportRows) {
				WebElement kpiTitleDiv = kpiReportRow.findElement(By.cssSelector("div.sch-kpi-title-text"));
				if(kpiTitleDiv.getText().equalsIgnoreCase(kpiTitle.toLowerCase())) {
					WebElement kpiReportExportBtn = kpiReportRow.findElement(By.cssSelector("div[ng-click=\"exportAction(kpi)\"]"));
					if(isElementLoaded(kpiReportExportBtn)) {
						click(kpiReportExportBtn);
						break;
					}
					else
						SimpleUtils.fail("Analytics Report Sub-Tab: Export button not loaded for '"+kpiTitle+"' KPI.", false);
				}
			}
		}
		else
			SimpleUtils.fail("Analytics Page: KPI reports rows not loaded.", false);
	}

	@Override
	public void clickOnAnalyticsConsoleMenu() throws Exception {
		if(isElementLoaded(consoleNavigationReport))
			click(consoleNavigationReport);
		else
			SimpleUtils.fail("Unable to click on 'Analytics' console menu.", false);
	}


	@Override
	public void selectAnalyticsCheckBoxByLabel(String label) throws Exception
	{
		if(forecstAndScheduleCheckBoxes.size() > 0)
		{
			for(WebElement analyticsCheckBox : forecstAndScheduleCheckBoxes)
			{
				WebElement parent = analyticsCheckBox.findElement(By.xpath(".."));
				if(parent.getText().toLowerCase().contains(label.toLowerCase())
						&& ! analyticsCheckBox.getAttribute("class").contains("checked")) {
					click(analyticsCheckBox);
					SimpleUtils.pass("Analytics Page: '"+label+"' checkbox selected successfully.");
				}
				else if(analyticsCheckBox.getAttribute("class").contains("checked")) {
					SimpleUtils.pass("Analytics Page: '"+label+"' checkbox already selected.");
				}
			}
		}
		else
			SimpleUtils.fail("Analytics Page: Unable to find '"+label+"' checkbox.",false);
	}

	@Override
	public HashMap<String, Float> getAnalyticsKPIHoursByLabel(String label) throws Exception
	{
		HashMap<String, Float> analyticsKPIHours = new HashMap<String, Float>();
		for(WebElement TableData : hidenTablesData)
		{
			if(TableData.getAttribute("innerText").toLowerCase().contains(label.toLowerCase())) {
				String[] dataTableRowsTexts = TableData.getAttribute("innerText").split("\n");
				//System.out.println("innerText: \n"+TableData.getAttribute("innerText"));
				for(String tableRow : dataTableRowsTexts) {
					if(tableRow.toLowerCase().contains(label.toLowerCase())) {
						String[] tableRowColumnData = tableRow.split("\t");
						if(tableRowColumnData[0].equalsIgnoreCase(label)) {
							analyticsKPIHours.put("hours", Float.valueOf(tableRowColumnData[1]));
							if(tableRowColumnData.length == 3)
								analyticsKPIHours.put("peakHours", Float.valueOf(tableRowColumnData[2]));
							break;
						}
					}
				}
			}
		}
		return analyticsKPIHours;
	}

	// Added by Nora: For GFE reports
	@FindBy (className = "sub-navigation-view-link")
	private List<WebElement> subNavigations;
	@FindBy (css = "[ng-attr-disabled*=\"DownloadAllLocationReports\"] .lg-button-group-first")
	private WebElement firstGroup;
	@FindBy (css = "[ng-attr-disabled*=\"DownloadAllLocationReports\"] .lg-button-group-last")
	private WebElement lastGroup;
	@FindBy (css = "tbody .sch-kpi-title-text")
	private List<WebElement> reportTitleTexts;
	@FindBy (css = "[ng-repeat*=\"kpi in kpiReports\"]")
	private List<WebElement> reportRows;

	@Override
	public boolean isReportsPageLoaded() throws Exception {
		boolean isLoaded = false;
		String subTitle = "Reports";
		if (areListElementVisible(subNavigations, 10)) {
			for (WebElement subNavigation : subNavigations) {
				if (subNavigation.getText().contains(subTitle) && subNavigation.getAttribute("class").contains("active")) {
					isLoaded = true;
					SimpleUtils.pass("Analytics Page: Reports page is loaded Successfully!");
					break;
				}
			}
		}
		return isLoaded;
	}

	@Override
	public void switchAllLocationsOrSingleLocation(boolean isAllLocations) throws Exception {
		String allLocations = "All Locations";
		if (isElementLoaded(firstGroup, 5) && isElementLoaded(lastGroup, 5)) {
			if (isAllLocations) {
				if (firstGroup.getText().equals(allLocations) && !firstGroup.getAttribute("class").contains("lg-button-group-selected")) {
					click(firstGroup);
					SimpleUtils.pass("Analytics: Select " + firstGroup.getText() + " Successfully!");
				}
				if (lastGroup.getText().equals(allLocations) && !lastGroup.getAttribute("class").contains("lg-button-group-selected")) {
					click(lastGroup);
					SimpleUtils.pass("Analytics: Select " + lastGroup.getText() + " Successfully!");
				}
			} else {
				if (!firstGroup.getText().equals(allLocations) && !firstGroup.getAttribute("class").contains("lg-button-group-selected")) {
					click(firstGroup);
					SimpleUtils.pass("Analytics: Select " + firstGroup.getText() + " Successfully!");
				}
				if (!lastGroup.getText().equals(allLocations) && !lastGroup.getAttribute("class").contains("lg-button-group-selected")) {
					click(lastGroup);
					SimpleUtils.pass("Analytics: Select " + lastGroup.getText() + " Successfully!");
				}
			}
		} else {
			SimpleUtils.fail("Analytics: All Locations and Single Location not loaded Successfully!", false);
		}
	}

	@Override
	public boolean isSpecificReportLoaded(String reportName) throws Exception {
		boolean isLoaded = false;
		if (areListElementVisible(reportTitleTexts, 10)) {
			for (WebElement reportTitle : reportTitleTexts) {
				if (reportTitle.getText().equalsIgnoreCase(reportName)) {
					isLoaded = true;
					SimpleUtils.pass("Analytics: Find the report: " + reportName + " Successfully!");
					break;
				}
			}
		}
		return isLoaded;
	}

	@Override
	public void mouseHoverAndExportReportByName(String reportName) throws Exception {
		if (areListElementVisible(reportRows, 5)) {
			for (WebElement reportRow : reportRows) {
				WebElement text = reportRow.findElement(By.className("sch-kpi-title-text"));
				if (text != null && text.getText().equalsIgnoreCase(reportName)) {
					mouseToElement(text);
					WebElement exportBtn = reportRow.findElement(By.className("sch-kpi-action-text"));
					if (exportBtn != null) {
						moveToElementAndClick(exportBtn);
						break;
					} else {
						SimpleUtils.fail("Analytics: Failed to find the Export button for report: " + reportName, false);
					}
				}
			}
		} else {
			SimpleUtils.fail("Analytics: Report rows not loaded Successfully!", false);
		}
	}

	@Override
	public void mouseHoverAndRefreshByName(String reportName) throws Exception {
		if (areListElementVisible(reportRows, 5)) {
			for (WebElement reportRow : reportRows) {
				WebElement text = reportRow.findElement(By.className("sch-kpi-title-text"));
				if (text != null && text.getText().equalsIgnoreCase(reportName)) {
					mouseToElement(text);
					WebElement refreshBtn = reportRow.findElement(By.cssSelector("span.sch-control-button-label"));
					if (refreshBtn != null) {
						moveToElementAndClick(refreshBtn);
						break;
					} else {
						SimpleUtils.fail("Analytics: Failed to find the Refresh button for report: " + reportName, false);
					}
				}
			}
		} else {
			SimpleUtils.fail("Analytics: Report rows not loaded Successfully!", false);
		}
	}


	@FindBy (css = "[ng-if=\"canDownloadAllLocationReports\"] div.lg-button-group-selected")
	private WebElement selectedReportsTab;
	@Override
	public boolean isSpecificReportsTabBeenSelected(String reportsTabName) throws Exception {
		boolean isSelected = false;
		if (isElementLoaded(selectedReportsTab, 10)
				&& selectedReportsTab.getText().equalsIgnoreCase(reportsTabName)) {
			isSelected = true;
			SimpleUtils.report("The tab: "+ reportsTabName+" is selected! ");
		} else
			SimpleUtils.report("The tab: "+ reportsTabName+" is not selected! ");
		return isSelected;
	}
}
