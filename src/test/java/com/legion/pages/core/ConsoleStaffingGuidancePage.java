package com.legion.pages.core;

import static com.legion.utils.MyThreadLocal.getDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.legion.pages.BasePage;
import com.legion.pages.StaffingGuidancePage;
import com.legion.utils.SimpleUtils;

public class ConsoleStaffingGuidancePage extends BasePage implements StaffingGuidancePage{

	@FindBy(className="sub-navigation-view")
	private List<WebElement> scheduleSubNavigationViewTab;
	
	String staffingGuidanceTabLabelText = "STAFFING GUIDANCE";
	String elementEnabledClassName = "enabled";
	
	@FindBy(css = "div.sub-navigation-view-link.active")
	private WebElement schedulePageSelectedSubTab;
	
	@FindBy (xpath = "//span[contains(text(),'Staffing Guidance')]")
	private WebElement StaffingGuidanceSubMenu;
	
	@FindBy(css="[ng-click=\"selectDayWeekView($event, 'week')\"]")
	private WebElement staffingGuidancePageWeekViewButton;
	
	@FindBy(css="[ng-click=\"selectDayWeekView($event, 'day')\"]")
	private WebElement projectedSalesPageDayViewButton;
	
	@FindBy(className="schedule-view")
	private WebElement staffingGuidanceDetailViewTable;
	
	@FindBy(css="div.dc-summary-box.first-row div[class='dc-summary-content'] div")
	private List<WebElement> staffingGuidanceDayViewTimeDurationLabels;
	
	@FindBy(css = "div.dc-summary-box.dc-summary-slot-fill div[class='dc-summary-content'] div")
	private List<WebElement> staffingGuidanceDayViewItemsCountLabels;
	
	@FindBy (xpath="//div[@class='dc-summary-box']//div[contains(@helper-text,'Projected')]/parent::div/div[@class='dc-summary-content']/div")
	private List<WebElement> staffingGuidanceDayViewTeamMemberCountLabels;

	@FindBy(className = "sch-calendar-day-dimension")
	private List<WebElement> staffingGuidanceWeekViewDayDateMonthLabels;
	
	@FindBy(className = "sch-week-view-day-summary")
	private List<WebElement> staffingGuidanceWeekViewDaysHours;
	
	@FindBy(css="[ng-click=\"controlPanel.fns.analyzeAction($event)\"]")
	private WebElement staffingGuidanceAnalyzeButton;
	
	@FindBy(className="sch-schedule-analyze-content")
	private WebElement staffingGuidanceAnalyzePopup;
	
	@FindBy(className="version-label")
	private List<WebElement> versionHistoryAllSections;
	
	@FindBy(className="version-label-mini")
	private List<WebElement> versionHistoryTotalHoursAndFeasibility;
	
	@FindBy(css="div.sch-view-dropdown-summary-content-item.first")
	private WebElement analyzeStaffingGuidanceSection;
	
	@FindBy(css="div.sch-view-dropdown-summary-content-item.version")
	private WebElement analyzeStaffingGuidanceLatestVersionSection;

	@FindBy(css="div.sch-control-kpi-label")
	private List<WebElement> staffingGuidanceKPILabelsText;
	
	@FindBy(css="div.sch-control-kpi")
	private List<WebElement> staffingGuidanceKPILabelsData;

	@FindBy(css="span.sch-control-kpi-value.green")
	private List<WebElement> staffingGuidanceKPIData;

	
	@FindBy(css="[ng-style=\"dropDownButtonStyle()\"]")
	private WebElement staffingGuidanceWorkRoleFilterButton;
	
	@FindBy(css="[ng-click=\"selectChoice($event, choice)\"]")
	private List<WebElement> staffingGuidanceWorkRoleFilterOptions;
	
	@FindBy(className="sch-schedule-analyze-dismiss")
	private WebElement staffingGuidanceAnalyzePopupCloseButton;
	
	@FindBy (css = "div.console-navigation-item-label.Schedule")
	private WebElement consoleSchedulePageTabElement;
	
	@FindBy (css = "div[ng-if*='guidance-week-chart'] div.sch-slot-detail")
	private List<WebElement> staffingGuidanceWeekView;
	
	@FindBy (css = "div.day-chart-graph-container div[aria-label*='A']")
	private WebElement staffingGuidanceDayViewGraph;

	
	@FindBy(className = "schedule-view")
	private WebElement staffingGuidancePageView;

	@FindBy(css="[ng-click='gotoNextWeek($event)']")
	private WebElement staffingGuidanceNextWeekArrow;

	@FindBy(css = "div.sch-calendar-date-label")
	private List<WebElement> staffingGuidanceWeekViewStart;

	@FindBy (css = "div.sch-calendar-day-dimension.active-day")
	private WebElement currentActiveDay;

	@FindBy(css = "div.sch-calendar-day-dimension")
	private List<WebElement> staffingGuidanceDayMonthDateLabels;

	@FindBy (css="img.holiday-logo-image")
	private WebElement storeClosedLogo;

	@FindBy (css = "div.console-navigation-item-label.Dashboard")
	private WebElement consoleDashboardTabElement;
	
	public ConsoleStaffingGuidancePage(){
		PageFactory.initElements(getDriver(), this);
	}
	
	public void navigateToStaffingGuidanceTab() throws Exception
	{
		WebElement staffingGuidanceElement = SimpleUtils.getSubTabElement(scheduleSubNavigationViewTab, staffingGuidanceTabLabelText);
		if(staffingGuidanceElement != null)
		{
			staffingGuidanceElement.click();
		}
	}

	public Boolean isStaffingGuidanceTabActive() throws Exception
	{
		if(isElementLoaded(schedulePageSelectedSubTab) && schedulePageSelectedSubTab.getText().toLowerCase().contains(staffingGuidanceTabLabelText.toLowerCase()))
		{
			return true;
		}
		return false;
	}
	
	public void navigateToStaffingGuidanceTabWeekView() throws Exception
	{
		if(isStaffingGuidanceTabActive())
		{
			if(isElementLoaded(staffingGuidancePageWeekViewButton))
			{
				staffingGuidancePageWeekViewButton.click();
			}
			else {
				SimpleUtils.fail("Staffing Guidance Week View button not Loaded.", false);
			}
		}
		else {
			SimpleUtils.fail("Staffing Guidance Tab not dispalyed.", false);
		}
	}
	
	public Boolean isStaffingGuidanceForecastTabWeekViewActive() throws Exception {
		if(isElementLoaded(staffingGuidancePageWeekViewButton))
		{
			if(staffingGuidancePageWeekViewButton.getAttribute("class").toString().contains(elementEnabledClassName))
			{
				return true;
			}
		}
		return false;
	}
	
	public void navigateToStaffingGuidanceTabDayView() throws Exception
	{
		if(isStaffingGuidanceTabActive())
		{
			if(isElementLoaded(projectedSalesPageDayViewButton))
			{
				projectedSalesPageDayViewButton.click();
			}
			else {
				SimpleUtils.fail("Staffing Guidance Week View button not Loaded.", false);
			}
		}
		else {
			SimpleUtils.fail("Staffing Guidance Tab not dispalyed.", false);
		}
	}
	
	public Boolean isStaffingGuidanceForecastTabDayViewActive() throws Exception {
		if(isElementLoaded(projectedSalesPageDayViewButton))
		{
			if(projectedSalesPageDayViewButton.getAttribute("class").toString().contains(elementEnabledClassName))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<String> getStaffingGuidanceForecastDayViewTimeDuration() throws Exception
	{
		ArrayList<String> staffingGuidanceTimeDuration = new ArrayList<String>();
		if(isStaffingGuidanceForecastTabDayViewActive())
		{
			if(staffingGuidanceDayViewTimeDurationLabels.size() != 0)
			{
				for(WebElement staffingGuidanceDayViewTimeDurationLabel : staffingGuidanceDayViewTimeDurationLabels)
				{
					staffingGuidanceTimeDuration.add(staffingGuidanceDayViewTimeDurationLabel.getText());
				}
			}
		}
		else
		{
			SimpleUtils.fail("Staffing Guidance Day View not Loaded Successfully!", true);
		}
		return staffingGuidanceTimeDuration;
	}
			
	public List<Integer> getStaffingGuidanceForecastDayViewItemsCount() throws Exception
	{
		ArrayList<Integer> staffingGuidanceItemsCount = new ArrayList<Integer>();
		if(isStaffingGuidanceForecastTabDayViewActive())
		{
			if(staffingGuidanceDayViewItemsCountLabels.size() != 0)
			{
				for(WebElement staffingGuidanceDayViewItemsCountLabel : staffingGuidanceDayViewItemsCountLabels)
				{
					staffingGuidanceItemsCount.add(Integer.valueOf(staffingGuidanceDayViewItemsCountLabel.getText()));
				}
			}
		}
		else
		{
			SimpleUtils.fail("Staffing Guidance Day View not Loaded Successfully!", true);
		}
		return staffingGuidanceItemsCount;
	}
	
	public List<Integer> getStaffingGuidanceForecastDayViewTeamMembersCount() throws Exception
	{
		ArrayList<Integer> staffingGuidanceTeamMembersCount = new ArrayList<Integer>();
		if(isStaffingGuidanceForecastTabDayViewActive())
		{
			
			if(staffingGuidanceDayViewTeamMemberCountLabels.size() != 0)
			{
				for(WebElement staffingGuidanceDayViewTeamMemberCountLabel : staffingGuidanceDayViewTeamMemberCountLabels)
				{
					staffingGuidanceTeamMembersCount.add(Integer.valueOf(staffingGuidanceDayViewTeamMemberCountLabel.getText()));
				}
			}
		}
		else
		{
			SimpleUtils.fail("Staffing Guidance Day View not Loaded Successfully!", true);
		}
		return staffingGuidanceTeamMembersCount;
	}
	
	public List<String> getStaffingGuidanceDayDateMonthLabelsForWeekView() throws Exception{
		ArrayList<String> staffingGuidanceTeamMembersCount = new ArrayList<String>();
		if(isStaffingGuidanceForecastTabWeekViewActive())
		{
			for(WebElement staffingGuidanceWeekViewDayDateMonthLabel : staffingGuidanceWeekViewDayDateMonthLabels)
			{
				staffingGuidanceTeamMembersCount.add(staffingGuidanceWeekViewDayDateMonthLabel.getText());
			}
		}
		else {
			SimpleUtils.fail("Staffing Guidance Week View not Loaded Successfully!", true);
		}
		return staffingGuidanceTeamMembersCount;
	}
	
	public List<Float> getStaffingGuidanceHoursCountForWeekView() throws Exception{
		ArrayList<Float> staffingGuidanceTeamMembersCount = new ArrayList<Float>();
		if(isStaffingGuidanceForecastTabWeekViewActive())
		{
			for(WebElement staffingGuidanceWeekViewDaysHour : staffingGuidanceWeekViewDaysHours)
			{
				staffingGuidanceTeamMembersCount.add(Float.valueOf(staffingGuidanceWeekViewDaysHour.getText().replace(" HRs", "")));
			}
		}
		else {
			SimpleUtils.fail("Staffing Guidance Week View not Loaded Successfully!", true);
		}
		return staffingGuidanceTeamMembersCount;
	}
	

	public void clickOnStaffingGuidanceAnalyzeButton() throws Exception
	{
		if(isStaffingGuidanceTabActive())
		{
			if(isElementLoaded(staffingGuidanceAnalyzeButton))
			{
				click(staffingGuidanceAnalyzeButton);
			}
			else {
				SimpleUtils.fail("Staffing Guidance Analyze Button not loaded successfully!", false);
			}
		}
		else {
			SimpleUtils.fail("Staffing Guidance tab not active!", false);
		}
	}
	
	public Boolean isStaffingGuidanceAnalyzePopupAppear() throws Exception
	{
		if(isStaffingGuidanceTabActive())
		{
			if(isElementLoaded(staffingGuidanceAnalyzePopup))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<HashMap<String, String>> getStaffingGuidanceVersionHistory() throws Exception
	{
		List<HashMap<String,String>> versionHistoryDetails = new ArrayList<HashMap<String,String>>();
		if(isStaffingGuidanceAnalyzePopupAppear())
		{
			for(WebElement versionHistorySection : versionHistoryAllSections)
			{
				int index = versionHistoryAllSections.indexOf(versionHistorySection);
				HashMap<String,String> versionHistoryData = new HashMap<String,String>();
				String versionHistoryText = versionHistorySection.getText();
				String ScheduleVersion = versionHistoryText.split("\\(")[0].trim();
				String versionOwnerCreatedDate = versionHistoryText.split("\\(")[1].trim();
				versionOwnerCreatedDate = versionOwnerCreatedDate.split("\\)")[0].trim();
				String versionHistoryTotalHoursAndFeasibilityText = versionHistoryTotalHoursAndFeasibility.get(index).getText();
				String versionHistoryTotalHours = versionHistoryTotalHoursAndFeasibilityText.split(",")[0].replace("TOTAL", "").trim();
				String versionHistoryFeasibility = versionHistoryTotalHoursAndFeasibilityText.split(",")[1].replace("FEASIBILITY", "").trim();
				versionHistoryData.put("ScheduleVersion", ScheduleVersion);
				versionHistoryData.put("versionOwnerCreatedDate", versionOwnerCreatedDate);
				versionHistoryData.put("versionHistoryTotalHours", versionHistoryTotalHours);
				versionHistoryData.put("versionHistoryFeasibility", versionHistoryFeasibility);
				versionHistoryDetails.add(versionHistoryData);
			}
		}
		else
		{
			clickOnStaffingGuidanceAnalyzeButton();
			getStaffingGuidanceVersionHistory();
		}
		return versionHistoryDetails;
	}
	
	public List<HashMap<String, String>> getAnalyzePopupStaffingGuidanceAndLatestVersionData() throws Exception
	{
		List<HashMap<String, String>> staffingGuidancePopupData = new ArrayList<HashMap<String, String>> ();
		staffingGuidancePopupData.add(getAnalyzePopupStaffingGuidanceData(analyzeStaffingGuidanceSection));
		staffingGuidancePopupData.add(getAnalyzePopupStaffingGuidanceData(analyzeStaffingGuidanceLatestVersionSection));
		closeStaffingGuidanceAnalyzePopup();
		return staffingGuidancePopupData;
	}
	
	
	public HashMap<String, String> getAnalyzePopupStaffingGuidanceData(WebElement listWebElement) throws Exception
	{
		HashMap<String, String> staffingGuidanceData = new HashMap<String, String>();
		if(isStaffingGuidanceAnalyzePopupAppear())
		{
			if(isElementLoaded(listWebElement))
			{
				Boolean isWorkRoleHours = false;
				String[] StaffingGuidanceSectionDataTexts = listWebElement.getText().split("\n");
				staffingGuidanceData.put("dataType", StaffingGuidanceSectionDataTexts[0]);
				for(String StaffingGuidanceSectionDataText: StaffingGuidanceSectionDataTexts)
				{
					int index = Arrays.asList(StaffingGuidanceSectionDataTexts).indexOf(StaffingGuidanceSectionDataText);
					if(StaffingGuidanceSectionDataText.contains("Created"))
					{
						staffingGuidanceData.put("totalHours", StaffingGuidanceSectionDataTexts[index - 1]);
						staffingGuidanceData.put("ownerAndTime", StaffingGuidanceSectionDataText);
						isWorkRoleHours = true;
					}
					if(isWorkRoleHours && StaffingGuidanceSectionDataText.contains("HRs") && (index + 1) < StaffingGuidanceSectionDataTexts.length)
					{
						staffingGuidanceData.put(StaffingGuidanceSectionDataTexts[index + 1], StaffingGuidanceSectionDataText);
					}
				}
			}
		}
		
		return staffingGuidanceData;
	}
	
	public HashMap<String, String> getStaffingGuidanceKPILabel() throws Exception
	{
		HashMap<String,String> staffingGuidanceLabelsData = new HashMap<String,String>();
		if(isStaffingGuidanceTabActive())
		{
			if(staffingGuidanceKPILabelsText.size() != 0 && staffingGuidanceKPILabelsData.size() != 0)
			{
				for(WebElement staffingGuidanceKPILabelData : staffingGuidanceKPILabelsData)
				{
					int index = staffingGuidanceKPILabelsData.indexOf(staffingGuidanceKPILabelData);
					staffingGuidanceLabelsData.put(staffingGuidanceKPILabelsText.get(index).getText(), staffingGuidanceKPILabelData.getText());
				}
				
			}
		}
		return staffingGuidanceLabelsData;
	}
	
	public List<String> getStaffingGuidanceWorkRoleFilterOptions() throws Exception
	{
		List<String> staffingGuidanceWorkRolesFilterOptions = new ArrayList<String>();
		if(isStaffingGuidanceTabActive())
		{
			if(isElementLoaded(staffingGuidanceWorkRoleFilterButton))
			{
				if(staffingGuidanceWorkRoleFilterOptions.size() != 0)
				{
					for(WebElement staffingGuidanceWorkRoleFilterOption: staffingGuidanceWorkRoleFilterOptions)
					{
						staffingGuidanceWorkRolesFilterOptions.add(staffingGuidanceWorkRoleFilterOption.getText());
					}
				}
			}
		}
		return staffingGuidanceWorkRolesFilterOptions;
	}
	
	public void filterStaffingGuidanceWorkRolesAsGivenOption(String workRoleFilterOption) throws Exception
	{
		if(isStaffingGuidanceTabActive())
		{
			if(isElementLoaded(staffingGuidanceWorkRoleFilterButton))
			{
				click(staffingGuidanceWorkRoleFilterButton);
				if(staffingGuidanceWorkRoleFilterOptions.size() != 0)
				{
					if(! staffingGuidanceWorkRoleFilterOptions.get(0).getText().contains(workRoleFilterOption))
					{
						click(staffingGuidanceWorkRoleFilterOptions.get(0));
					}
					
					for(WebElement staffingGuidanceWorkRoleFilterOption: staffingGuidanceWorkRoleFilterOptions)
					{
						if(staffingGuidanceWorkRoleFilterOption.getText().contains(workRoleFilterOption))
						{
							click(staffingGuidanceWorkRoleFilterOption);
						}
					}
				}
			}
		}
		else {
			navigateToStaffingGuidanceTabDayView();
		}
	}
	
	public Boolean isStaffingGuidanceWorkRolesAsGivenOptionActivated(String workRoleFilterOption) throws Exception
	{
		if(isStaffingGuidanceTabActive())
		{
			if(isElementLoaded(staffingGuidanceWorkRoleFilterButton))
			{
				if(staffingGuidanceWorkRoleFilterButton.getText().contains(workRoleFilterOption))
				{
					return true;
				}
			}
		}
		return true;
	}
	
	public void closeStaffingGuidanceAnalyzePopup() throws Exception
	{
		if(isStaffingGuidanceAnalyzePopupAppear())
		{
			click(staffingGuidanceAnalyzePopupCloseButton);
		}
	}

	@FindBy(css = "[attr-id=\"workerRoles\"]")
	private WebElement guidanceWorkRoleDropDown;
	@Override
	public String getActiveWorkRole() throws Exception {
		String selectedWorkRole = "";
		if(isElementLoaded(guidanceWorkRoleDropDown)) {
			selectedWorkRole = guidanceWorkRoleDropDown.getText();
		}
		SimpleUtils.report("Staffing Guidance Active Work Role filter '"+ selectedWorkRole +"'");
		return selectedWorkRole;
	}

	//added by Gunjan
	@Override
	public boolean loadStaffingGuidance() throws Exception {
		// TODO Auto-generated method stub
		boolean flag=false;
		if(isElementLoaded(StaffingGuidanceSubMenu)){
			click(StaffingGuidanceSubMenu);
			SimpleUtils.pass("Clicked on Staffing Guidance Sub Menu ");
			if(isElementLoaded(projectedSalesPageDayViewButton)){
				click(projectedSalesPageDayViewButton);
				SimpleUtils.pass("Clicked on Staffing Guidance Day View");
				if(isElementLoaded(staffingGuidanceDayViewGraph)){
				flag = true;
				SimpleUtils.pass("Staffing Guidance Loaded in DayView Successfully!");
				}else{
					SimpleUtils.fail("Staffing Guidance Not Loaded in DayView Successfully!", true);
				}
			}else{
				SimpleUtils.pass("Day View button not found in Staffing Guidance");
			}
			if(isElementLoaded(staffingGuidancePageWeekViewButton)){
				click(staffingGuidancePageWeekViewButton);
				SimpleUtils.pass("Clicked on Staffing Guidance Week View");
				if(staffingGuidanceWeekView.size()!=0){
					flag = true;
					SimpleUtils.pass("Staffing Guidance Loaded in Week View Successfully!");
				}else{
					SimpleUtils.fail("Staffing Guidance Not Loaded in Week View Successfully!", true);
				}
			}else{
				SimpleUtils.pass("Week View button not found in Staffing Guidance");
			}
		}else{
			SimpleUtils.fail("Staffing Guidance Sub Menu Tab Not Found", true);
		}
		return flag;
	}

	public void staffingGuidanceDayNavigation(String weekStarting) throws Exception{
		if(isElementEnabled(projectedSalesPageDayViewButton)){
			click(projectedSalesPageDayViewButton);
			SimpleUtils.pass("Clicked on Staffing Guidance Day View of Week Starting " +weekStarting);
			for(int i=0;i<staffingGuidanceDayMonthDateLabels.size();i++){
				click(staffingGuidanceDayMonthDateLabels.get(i));
				float guidanceHoursDayViewFinalValue = Float.parseFloat(staffingGuidanceKPIData.get(0).getText());
				if(isElementLoaded(staffingGuidanceDayViewGraph,10) && guidanceHoursDayViewFinalValue>0){
					SimpleUtils.pass("Staffing Guidance Successfully for " + currentActiveDay.getText()+ " and Guidance Value is " +staffingGuidanceKPILabelsData.get(0).getText());
				}else if (!isElementLoaded(staffingGuidanceDayViewGraph,2) && isElementLoaded(storeClosedLogo, 10) && guidanceHoursDayViewFinalValue==0){
					SimpleUtils.pass("Store Closed on " + currentActiveDay.getText()+ " and Guidance Value is " +staffingGuidanceKPILabelsData.get(0).getText());
				}else{
					SimpleUtils.fail("Staffing Guidance Not Loaded for " + currentActiveDay.getText()+ " and Guidance Value is " +staffingGuidanceKPILabelsData.get(0).getText(), true);
				}
			}
		}else{
			SimpleUtils.fail("Day View button not found in Staffing Guidance",false);
		}

	}


	public void staffingGuidanceDayWeekNavigation() throws Exception{
		if(isElementEnabled(staffingGuidancePageWeekViewButton)){
			click(staffingGuidancePageWeekViewButton);
			String weekStarting = staffingGuidanceWeekViewStart.get(0).getText();
			SimpleUtils.pass("Clicked on Staffing Guidance Week View of Week Starting " +weekStarting);
			float guidanceHoursFinalValue = Float.parseFloat(staffingGuidanceKPIData.get(0).getText());
			if(isElementLoaded(staffingGuidanceWeekView.get(0),10) && guidanceHoursFinalValue>0){
				//flag = true;
				SimpleUtils.pass("Staffing Guidance Loaded Successfully! for week starting " +weekStarting+ " and Guidance value is  "+ staffingGuidanceKPILabelsData.get(0).getText());
				staffingGuidanceDayNavigation(weekStarting);
			}else{
				SimpleUtils.fail("Staffing Guidance Not Loaded for week starting" +weekStarting+ " and Guidance value is  "+ staffingGuidanceKPILabelsData.get(0).getText(), true);
			}
		}else{
			SimpleUtils.fail("Week View button not found in Staffing Guidance",false);
		}
	}

	@Override
	public void navigateStaffingGuidance(String nextWeekView, int weekCount) throws Exception{
		if(isElementLoaded(StaffingGuidanceSubMenu)){
			click(StaffingGuidanceSubMenu);
			SimpleUtils.pass("Clicked on Staffing Guidance Sub Menu ");
			for(int i = 0; i < weekCount; i++) {
				if (nextWeekView.toLowerCase().contains("next") || nextWeekView.toLowerCase().contains("future")) {
					staffingGuidanceDayWeekNavigation();
					click(staffingGuidanceNextWeekArrow);
				}
			}
		}else{
			SimpleUtils.fail("Staffing Guidance Sub Menu Tab Not Found", false);
		}
	}
}
