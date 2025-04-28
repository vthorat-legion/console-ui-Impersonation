//package com.legion.tests.core;
//
//import java.io.File;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.legion.pages.*;
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import com.legion.tests.TestBase;
//import com.legion.tests.annotations.Automated;
//import com.legion.tests.annotations.Enterprise;
//import com.legion.tests.annotations.Owner;
//import com.legion.tests.annotations.TestName;
////import com.legion.tests.core.ScheduleTestKendraScott2.SchedulePageSubTabText;
//import com.legion.tests.data.CredentialDataProviderSource;
//import com.legion.utils.CsvUtils;
//import com.legion.utils.JsonUtil;
//import com.legion.utils.SimpleUtils;
//
//public class AnalyticsTestKendraScott2 extends TestBase{
//	private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
//
//	public enum SchedulePageSubTabText{
//		  Overview("OVERVIEW"),
//		  ProjectedSales("PROJECTED SALES"),
//		  StaffingGuidance("STAFFING GUIDANCE"),
//		  Schedule("SCHEDULE"),
//		  ProjectedTraffic("PROJECTED TRAFFIC");
//			private final String value;
//			SchedulePageSubTabText(final String newValue) {
//	            value = newValue;
//	        }
//	        public String getValue() { return value; }
//		}
//
//	 @Override
//	 @BeforeMethod()
//	 public void firstTest(Method testMethod, Object[] params) throws Exception{
//	   this.createDriver((String)params[0],"69","Window");
//       visitPage(testMethod);
//       loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
//     }
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "Kendrascott2_Enterprise")
//	@TestName(description = "Verify Analytics flow")
//	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	public void gotoAnalyticsPageTest(String username, String password, String browser, String location) throws Exception {
//		AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//		analyticsPage.gotoAnalyticsPage();
//	}
//
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "Kendrascott2_Enterprise")
//	@TestName(description = "TP-133 : Analytics :- Value of Forecast and Schedule graph needs to be verified.")
//	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateForecastAndScheduleHoursWithScheduleOverviewPageAsStoreManager(String username, String password, String browser, String location) throws Exception {
//		ArrayList<LinkedHashMap<String, Float>> scheduleOverviewAllWeekHours = new ArrayList<LinkedHashMap<String, Float>>();
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//        SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//        SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//        		scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()) , false);
//
//        scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//        SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//        		scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()) , false);
//
//        ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//        List<WebElement> scheduleOverViewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//        for(WebElement overViewWeek : scheduleOverViewWeeks)
//        {
//        	scheduleOverviewAllWeekHours.add(scheduleOverviewPage.getWeekHoursByWeekElement(overViewWeek));
//        }
//        String forecastedHoursText = "forecastedHours";
//		String publishedScheduleHoursText = "publishedScheduleHours";
//
//		String overviewGuidanceHoursText = "guidanceHours";
//		String overviewScheduleHoursText = "scheduledHours";
//
//		AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//		analyticsPage.gotoAnalyticsPage();
//		boolean isFirst = true;
//		for(LinkedHashMap<String, Float> weekHours : scheduleOverviewAllWeekHours)
//		{
//			if(! isFirst)
//			{
//				analyticsPage.navigateToNextWeek();
//			}
//
//
//			HashMap<String, Float> analyticaForecastedHours = analyticsPage.getForecastedHours();
//			HashMap<String, Float> analyticaScheduleHours = analyticsPage.getScheduleHours();
//			if(analyticaForecastedHours.get(forecastedHoursText).equals(weekHours.get(overviewGuidanceHoursText)))
//				SimpleUtils.pass("Analytics Forecated Hours and Schedule Guidance Hours found same ("+
//						analyticaForecastedHours.get(forecastedHoursText) +"/" + weekHours.get(overviewGuidanceHoursText) +") for Duration: '"+
//							analyticsPage.getAnalyticsActiveDuration()+"'.");
//			else
//				SimpleUtils.fail("Analytics Forecated Hours and Schedule Guidance Hours not same ("+
//						analyticaForecastedHours.get(forecastedHoursText) +"/" + weekHours.get(overviewGuidanceHoursText) +") for Duration: '"+
//							analyticsPage.getAnalyticsActiveDuration()+"'.", true);
//
//
//			if(analyticaScheduleHours.get(publishedScheduleHoursText).equals(weekHours.get(overviewScheduleHoursText)))
//				SimpleUtils.pass("Analytics Scheduled Hours and Overview Schedule Hours found same ("+
//						analyticaScheduleHours.get(publishedScheduleHoursText) +"/" + weekHours.get(overviewScheduleHoursText) +") for Duration: '"+
//							analyticsPage.getAnalyticsActiveDuration()+"'.");
//			else
//				SimpleUtils.fail("Analytics Scheduled Hours and Overview Schedule Hours not same ("+
//						analyticaScheduleHours.get(publishedScheduleHoursText) +"/" + weekHours.get(overviewScheduleHoursText) +") for Duration: '"+
//							analyticsPage.getAnalyticsActiveDuration()+"'.", true);
//
//			isFirst = false;
//		}
//   }
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "Coffee2_Enterprise")
//	@TestName(description = "TP-150 : Validate KPI report export works[Check by exporting any one report, and confirm it should not be blank].")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyKPIReportExportFunctionalityAsStoreManager(String username, String password, String browser, String location) throws Exception {
//	   AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//	   analyticsPage.clickOnAnalyticsConsoleMenu();
//	   String analyticsReportSubTabLabel = "REPORTS";
//	   analyticsPage.clickOnAnalyticsSubTab(analyticsReportSubTabLabel);
//	   String scheduleKPITitle = "Forecast to Schedule Hours KPI Daily";
//	   String downloadDirPath = propertyMap.get("Download_File_Default_Dir");
//	   int fileCounts = SimpleUtils.getDirectoryFilesCount(downloadDirPath);
//	   analyticsPage.exportKPIReportByTitle(scheduleKPITitle);
//
//	   Thread.sleep(2000);
//	   if(SimpleUtils.getDirectoryFilesCount(downloadDirPath) > fileCounts) {
//		   File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
//		   String fileName = latestFile.getName();
//		   SimpleUtils.pass("KPI Report exported successfully with file name '"+ fileName +"'.");
//		   ArrayList<HashMap<String,String>> response = CsvUtils.getDataFromCSVFileWithHeader(downloadDirPath+"/"+fileName);
//		   float totalDaysStaffingGuidanceHours = 0;
//		   float totalDaysProjectedSalesHours = 0;
//		   for(HashMap<String,String> hashMap : response)
//		   {
//			   totalDaysStaffingGuidanceHours +=  Float.valueOf(hashMap.get("Forecasted Staffing Hours (All)"));
//			   totalDaysProjectedSalesHours +=  Float.valueOf(hashMap.get("Forecasted Items"));
//		   }
//		   System.out.println("totalDaysStaffingGuidanceHours: "+totalDaysStaffingGuidanceHours);
//		   System.out.println("totalDaysProjectedSalesHours: "+totalDaysProjectedSalesHours);
//
//		   ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		   scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		   SimpleUtils.assertOnFail( "Schedule Page not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()),false);
//		   StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
//	       staffingGuidancePage.navigateToStaffingGuidanceTab();
//	       SimpleUtils.assertOnFail( "Staffing Guidance tab not loaded successfully!", staffingGuidancePage.isStaffingGuidanceTabActive(),false);
//	       List<Float> staffingGuidanceDaysHours = staffingGuidancePage.getStaffingGuidanceHoursCountForWeekView();
//	       float staffingGuidanceWeekHours = 0;
//	       for(Float dayHours : staffingGuidanceDaysHours)
//	       {
//	    	   staffingGuidanceWeekHours += dayHours;
//	       }
//	       if(totalDaysStaffingGuidanceHours == staffingGuidanceWeekHours)
//	    	   SimpleUtils.pass("KPI Report Guidance hours matched with Staffing Guidance '"
//	    			   + totalDaysStaffingGuidanceHours +"/"+ staffingGuidanceWeekHours +"'");
//	       else
//	    	   SimpleUtils.fail("KPI Report Guidance hours not matched with Staffing Guidance '"
//	    			   + totalDaysStaffingGuidanceHours +"/"+ staffingGuidanceWeekHours +"'", true);
//
//	       schedulePage.goToProjectedSales();
//	       ProjectedSalesPage projectedSalesPage = pageFactory.createProjectedSalesPage();
//	       projectedSalesPage.clickOnWeekView();
//	       float salesGuidance = projectedSalesPage.getProjectedSalesGuidanceItems();
//
//	       if(totalDaysProjectedSalesHours == salesGuidance)
//	    	   SimpleUtils.pass("KPI Report Projected Sales matched with Sales Guidance '"
//	    			   + totalDaysProjectedSalesHours +"/"+ salesGuidance +"'");
//	       else
//	    	   SimpleUtils.fail("KPI Report Guidance hours not matched with Staffing Guidance '"
//	    			   + totalDaysProjectedSalesHours +"/"+ salesGuidance +"'", true);
//	   }
//	   else
//		   SimpleUtils.fail("KPI Report Not Exported.", false);
//   }
//
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "Coffee2_Enterprise")
//	@TestName(description = "TP-152 : Validate KPI report values.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateKPIReportValuesAsStoreManager(String username, String password, String browser, String location) throws Exception {
//	   AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//	   analyticsPage.clickOnAnalyticsConsoleMenu();
//	   //String analyticsReportSubTabLabel = "REPORTS";
//	   //analyticsPage.clickOnAnalyticsSubTab(analyticsReportSubTabLabel);
//	   String allHoursLabel = "All Hours";
//	   String peakHoursLabel = "Peak Hours";
//	   analyticsPage.selectAnalyticsCheckBoxByLabel(allHoursLabel);
//	   analyticsPage.selectAnalyticsCheckBoxByLabel(peakHoursLabel);
//	   HashMap<String, Float> forecastedHours = analyticsPage.getAnalyticsKPIHoursByLabel("Forecasted Hours");
//	   HashMap<String, Float> initialScheduleHours = analyticsPage.getAnalyticsKPIHoursByLabel("Initial Schedule Hours");
//	   HashMap<String, Float> publishedScheduleHours = analyticsPage.getAnalyticsKPIHoursByLabel("Published Schedule Hours");
//
//	   float forecastedStaffingAllHours = 0;
//	   float forecastedStaffingPeakHours = 0;
//	   float initialScheduleAllHours = 0;
//	   float initialSchedulePeakHours = 0;
//	   float publishedScheduleAllHours = 0;
//	   float publishedSchedulePeakHours = 0;
//
//	   // Exporting KPI Report
//	   String analyticsReportSubTabLabel = "REPORTS";
//	   analyticsPage.clickOnAnalyticsSubTab(analyticsReportSubTabLabel);
//	   String scheduleKPITitle = "Forecast to Schedule Hours KPI Daily";
//	   String downloadDirPath = propertyMap.get("Download_File_Default_Dir");
//	   int fileCounts = SimpleUtils.getDirectoryFilesCount(downloadDirPath);
//	   analyticsPage.exportKPIReportByTitle(scheduleKPITitle);
//	   Thread.sleep(2000);
//	   if(SimpleUtils.getDirectoryFilesCount(downloadDirPath) > fileCounts) {
//		   File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
//		   String fileName = latestFile.getName();
//		   SimpleUtils.pass("KPI Report exported successfully with file name '"+ fileName +"'.");
//		   ArrayList<HashMap<String,String>> response = CsvUtils.getDataFromCSVFileWithHeader(downloadDirPath+"/"+fileName);
//		   if(response.size() > 0)
//		   {
//			   for(HashMap<String,String> KPIReportRow : response)
//			   {
//				   forecastedStaffingAllHours += Float.valueOf(KPIReportRow.get("Forecasted Staffing Hours (All)"));
//				   forecastedStaffingPeakHours += Float.valueOf(KPIReportRow.get("Forecasted Staffing Hours (Peak)"));
//				   initialScheduleAllHours += Float.valueOf(KPIReportRow.get("Initial Schedule Hours (All)"));
//				   initialSchedulePeakHours += Float.valueOf(KPIReportRow.get("Initial Schedule Hours (Peak)"));
//				   publishedScheduleAllHours += Float.valueOf(KPIReportRow.get("Published Schedule Hours (All)"));
//				   publishedSchedulePeakHours += Float.valueOf(KPIReportRow.get("Published Schedule Hours (Peak)"));
//			   }
//		   }
//		   else
//			   SimpleUtils.fail("KPI Report Exported with no data.", false);
//	   }
//	   else
//		   SimpleUtils.fail("KPI Report Not Exported.", false);
//
//
//
//	   SimpleUtils.pass("Analytics Page: KPI Reports Hours Validation Start..");
//	   //Validating Forecasted Hours (All)
//	   if(forecastedStaffingAllHours == forecastedHours.get("hours"))
//		   SimpleUtils.pass("KPI Report 'Forecasted Staffing Hours (All)' matched with Analytics 'Forecasted All Hours' ('"
//				   +forecastedStaffingAllHours+"/"+forecastedHours.get("hours")+"').");
//	   else
//		   SimpleUtils.fail("KPI Report 'Forecasted Staffing Hours (All)' not matched with Analytics 'Forecasted All Hours' ('"
//				   +forecastedStaffingAllHours+"/"+forecastedHours.get("hours")+"').", true);
//
//	 //Validating Forecasted Hours (Peak)
//	   if(forecastedStaffingPeakHours == forecastedHours.get("peakHours"))
//		   SimpleUtils.pass("KPI Report 'Forecasted Staffing Hours (Peak)' matched with Analytics 'Forecasted Peak Hours' ('"
//				   +forecastedStaffingPeakHours+"/"+forecastedHours.get("peakHours")+"').");
//	   else
//		   SimpleUtils.fail("KPI Report 'Forecasted Staffing Hours (Peak)' not matched with Analytics 'Forecasted Peak Hours' ('"
//				   +forecastedStaffingPeakHours+"/"+forecastedHours.get("peakHours")+"').", true);
//
//	 //Validating Initial Schedule Hours (All)
//	   if(initialScheduleAllHours == initialScheduleHours.get("hours"))
//		   SimpleUtils.pass("KPI Report 'Initial Schedule Hours (All)' matched with Analytics 'Initial Schedule All Hours' ('"
//				   +initialScheduleAllHours+"/"+initialScheduleHours.get("hours")+"').");
//	   else
//		   SimpleUtils.fail("KPI Report 'Initial Schedule Hours (All)' not matched with Analytics 'Initial Schedule All Hours' ('"
//				   +initialScheduleAllHours+"/"+initialScheduleHours.get("hours")+"').", true);
//
//	 //Validating Initial Schedule Hours (Peak)
//	   if(initialSchedulePeakHours == initialScheduleHours.get("peakHours"))
//		   SimpleUtils.pass("KPI Report 'Initial Schedule Hours (Peak)' matched with Analytics 'Initial Schedule Peak Hours' ('"
//				   +initialSchedulePeakHours+"/"+initialScheduleHours.get("peakHours")+"').");
//	   else
//		   SimpleUtils.fail("KPI Report 'Initial Schedule Hours (Peak)' not matched with Analytics 'Initial Schedule Peak Hours' ('"
//				   +initialSchedulePeakHours+"/"+initialScheduleHours.get("peakHours")+"').", true);
//
//	   //***********************************************
//	 //Validating Initial Schedule Hours (All)
//	   if(publishedScheduleAllHours == publishedScheduleHours.get("hours"))
//		   SimpleUtils.pass("KPI Report 'Published Schedule Hours (All)' matched with Analytics 'Published Schedule All Hours' ('"
//				   +publishedScheduleAllHours+"/"+publishedScheduleHours.get("hours")+"').");
//	   else
//		   SimpleUtils.fail("KPI Report 'Published Schedule Hours (All)' not matched with Analytics 'Published Schedule All Hours' ('"
//				   +publishedScheduleAllHours+"/"+publishedScheduleHours.get("hours")+"').", true);
//
//	 //Validating Initial Schedule Hours (Peak)
//	   if(publishedSchedulePeakHours == publishedScheduleHours.get("peakHours"))
//		   SimpleUtils.pass("KPI Report 'Published Schedule Hours (Peak)' matched with Analytics 'Published Schedule Peak Hours' ('"
//				   +publishedSchedulePeakHours+"/"+publishedScheduleHours.get("peakHours")+"').");
//	   else
//		   SimpleUtils.fail("KPI Report 'Published Schedule Hours (Peak)' not matched with Analytics 'Published Schedule Peak Hours' ('"
//				   +publishedSchedulePeakHours+"/"+publishedScheduleHours.get("peakHours")+"').", true);
//	}
//}
