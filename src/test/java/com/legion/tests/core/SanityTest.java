//package com.legion.tests.core;
//
//import com.legion.pages.*;
//import com.legion.pages.core.schedule.ConsoleScheduleCommonPage;
//import com.legion.pages.mobile.LoginPageAndroid;
//import com.legion.tests.TestBase;
//import com.legion.tests.annotations.*;
//import com.legion.tests.data.CredentialDataProviderSource;
//import com.legion.utils.CsvUtils;
//import com.legion.utils.JsonUtil;
//import com.legion.utils.SimpleUtils;
//import com.legion.utils.SpreadSheetUtils;
//import org.openqa.selenium.WebElement;
//import org.testng.ITestContext;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.io.File;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import static com.legion.utils.MyThreadLocal.*;
//
//public class SanityTest extends TestBase{
//	  private static HashMap<String, String> propertyMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
//	  private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
//	  private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
//	  private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
//	private static HashMap<String, String> mobileCredentials = JsonUtil.getPropertiesFromJsonFile("src/test/resources/MobileCredentials.json");
//	  SchedulePage schedulePage = null;
//
//	  @Override
//	  @BeforeMethod
//	  public void firstTest(Method method, Object[] params) throws Exception {
////          this.createDriver((String)params[0],"69","Window");
//	      if(propertyMap.get("RUN_ON_MOBILE").equalsIgnoreCase("No")){
//              visitPage(method);
////	      loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
//          }
//
//	    }
//
//
//	public enum weekCount{
//			Zero(0),
//			One(1),
//			Two(2),
//			Three(3),
//			Four(4),
//			Five(5);
//			private final int value;
//			weekCount(final int newValue) {
//	            value = newValue;
//	        }
//	        public int getValue() { return value; }
//		}
//
//	public enum filtersIndex{
//		Zero(0),
//		One(1),
//		Two(2),
//		Three(3),
//		Four(4),
//		Five(5);
//		private final int value;
//		filtersIndex(final int newValue) {
//            value = newValue;
//        }
//        public int getValue() { return value; }
//	}
//
//
//	public enum dayCount{
//		Seven(7);
//		private final int value;
//		dayCount(final int newValue) {
//            value = newValue;
//        }
//        public int getValue() { return value; }
//	}
//
//	public enum schedulePlanningWindow{
//		Eight(8);
//		private final int value;
//		schedulePlanningWindow(final int newValue) {
//            value = newValue;
//        }
//        public int getValue() { return value; }
//	}
//
//	public enum sliderShiftCount{
//		SliderShiftStartCount(2),
//		SliderShiftEndTimeCount(10);
//		private final int value;
//		sliderShiftCount(final int newValue) {
//            value = newValue;
//        }
//        public int getValue() { return value; }
//	}
//
//	public enum staffingOption{
//		OpenShift("Auto"),
//		ManualShift("Manual"),
//		AssignTeamMemberShift("Assign Team Member");
//		private final String value;
//		staffingOption(final String newValue) {
//            value = newValue;
//        }
//        public String getValue() { return value; }
//	}
//
//	  public enum overviewWeeksStatus{
//		  NotAvailable("Not Available"),
//		  Draft("Draft"),
//		  Guidance("Guidance"),
//		  Published("Published"),
//		  Finalized("Finalized");
//
//		  private final String value;
//		  overviewWeeksStatus(final String newValue) {
//            value = newValue;
//          }
//          public String getValue() { return value; }
//		}
//
//
//	  public enum SchedulePageSubTabText{
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
//	  public enum weekViewType{
//		  Next("Next"),
//		  Previous("Previous");
//			private final String value;
//			weekViewType(final String newValue) {
//	            value = newValue;
//	        }
//	        public String getValue() { return value; }
//		}
//
//	  public enum shiftSliderDroppable{
//		  StartPoint("Start"),
//		  EndPoint("End");
//			private final String value;
//			shiftSliderDroppable(final String newValue) {
//	            value = newValue;
//	        }
//	        public String getValue() { return value; }
//		}
//
//	  public enum scheduleHoursAndWagesData{
//		  scheduledHours("scheduledHours"),
//		  budgetedHours("budgetedHours"),
//		  otherHours("otherHours"),
//		  wagesBudgetedCount("wagesBudgetedCount"),
//		  wagesScheduledCount("wagesScheduledCount");
//			private final String value;
//			scheduleHoursAndWagesData(final String newValue) {
//	            value = newValue;
//	        }
//	        public String getValue() { return value; }
//		}
//
//	  public enum scheduleGroupByFilterOptions{
//		  groupbyAll("Group by All"),
//		  groupbyWorkRole("Group by Work Role"),
//		  groupbyTM("Group by TM"),
//		  groupbyDayParts("Group by Day Parts");
//			private final String value;
//			scheduleGroupByFilterOptions(final String newValue) {
//	            value = newValue;
//	        }
//	        public String getValue() { return value; }
//		}
//
//
//
//
//	    public void verifyScheduleLabelHours(String shiftTimeSchedule,
//	  			Float scheduledHoursBeforeEditing, Float scheduledHoursAfterEditing) throws Exception{
//	  			Float scheduledHoursExpectedValueEditing = 0.0f;
//            // If meal break is applicable
//	  		if(Float.parseFloat(shiftTimeSchedule) >= 6){
//	  			scheduledHoursExpectedValueEditing = (float) (scheduledHoursBeforeEditing + (Float.parseFloat(shiftTimeSchedule) - 0.5));
//	  		}else{
//	  			scheduledHoursExpectedValueEditing = (float)(scheduledHoursBeforeEditing + Float.parseFloat(shiftTimeSchedule));
//	  		}
//           // If meal break is not applicable
////            scheduledHoursExpectedValueEditing = (float)(scheduledHoursBeforeEditing + Float.parseFloat(shiftTimeSchedule));
//	  		if(scheduledHoursExpectedValueEditing.equals(scheduledHoursAfterEditing)){
//	  			SimpleUtils.pass("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" matches with Scheduled Hours Actual value "+scheduledHoursAfterEditing);
//	  		}else{
//	  			SimpleUtils.fail("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" does not match with Scheduled Hours Actual value "+scheduledHoursAfterEditing,false);
//	  		}
//	  	}
//
//
//	    public void verifyTeamCount(List<String> previousTeamCount, List<String> currentTeamCount) throws Exception {
//			if(previousTeamCount.size() == currentTeamCount.size()){
//				for(int i =0; i<currentTeamCount.size();i++){
//					String currentCount = currentTeamCount.get(i);
//					String previousCount = previousTeamCount.get(i);
//					if(Integer.parseInt(currentCount) == Integer.parseInt(previousCount)+1){
//						SimpleUtils.pass("Current Team Count is greater than Previous Team Count");
//					}else{
//						SimpleUtils.fail("Current Team Count is not greater than Previous Team Count",true);
//					}
//				}
//			}else{
//				SimpleUtils.fail("Size of Current Team Count should be equal to Previous Team Count",false);
//			}
//		}
//
//	    public void verifyGutterCount(int previousGutterCount, int updatedGutterCount){
//	    	if(updatedGutterCount == previousGutterCount + 1){
//	    		SimpleUtils.pass("Size of gutter is "+updatedGutterCount+" greater than previous value "+previousGutterCount);
//	    	}else{
//	    		SimpleUtils.fail("Size of gutter is "+updatedGutterCount+" greater than previous value "+previousGutterCount, false);
//	    	}
//	    }
//
//
//	    public void scheduleNavigationTest(int previousGutterCount) throws Exception{
//			ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//	    	 scheduleMainPage.clickOnEditButton();
//	    	 boolean bolDeleteShift = checkAddedShift(previousGutterCount);
//	    	 if(bolDeleteShift){
//	    		 scheduleMainPage.clickSaveBtn();
//		    	 scheduleMainPage.clickOnEditButton();
//	    	 }
//	    }
//
//	    public boolean checkAddedShift(int guttercount)throws Exception {
//            boolean bolDeleteShift = false;
//            if (guttercount > 0) {
//                schedulePage.clickOnShiftContainer(guttercount);
//                bolDeleteShift = true;
//            }
//            return bolDeleteShift;
//        }
//
//	//added by Nishant for Sanity Suite
//
//	@Automated(automated =  "Automated")
//	@SanitySuite(sanity =  "Sanity")
//	@Owner(owner = "Naval")
//	@UseAsTestRailSectionId(testRailSectionId = 110)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate Schedule ungenerate feature.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateScheduleUngenerateFeatureAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!"
//				,scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()) , true);
//
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		BasePage basePase = new BasePage();
//		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//		boolean isWeekFoundToUnGenerate = false;
//		for(WebElement overviewWeek : overviewWeeks)
//		{
//			if(! overviewWeek.getText().contains(overviewWeeksStatus.Guidance.getValue()))
//			{
//				String weekStatus = overviewWeek.getText();
//				isWeekFoundToUnGenerate = true;
//				basePase.click(overviewWeek);
//				boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//				SimpleUtils.assertOnFail("Schedule with status: '" + weekStatus + "' not Generated for week: '"+ scheduleCommonPage.getActiveWeekText() +"'"
//						, isActiveWeekGenerated, false);
//				createSchedulePage.unGenerateActiveScheduleScheduleWeek();
//				isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//				if(! isActiveWeekGenerated)
//					SimpleUtils.pass("Schedule Page: Schedule week for duration:'"+ scheduleCommonPage.getActiveWeekText() +"' UnGenerated Successfully.");
//				else
//					SimpleUtils.fail("Schedule Page: Schedule week for duration:'"+ scheduleCommonPage.getActiveWeekText() +"' not UnGenerated.", false);
//				break;
//			}
//		}
//
//		if(! isWeekFoundToUnGenerate)
//			SimpleUtils.report("No Draft/Published/Finalized week found to Ungenerate Schedule.");
//	}
//
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Naval")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate KPI report export works[Check by exporting any one report, and confirm it should not be blank].")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyKPIReportExportFunctionalityAsStoreManager(String username, String password, String browser, String location) throws Exception {
//		AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//		analyticsPage.clickOnAnalyticsConsoleMenu();
//		String analyticsReportSubTabLabel = "REPORTS";
//		analyticsPage.clickOnAnalyticsSubTab(analyticsReportSubTabLabel);
//		String scheduleKPITitle = "Forecast to Schedule Hours KPI Daily";
//		String downloadDirPath = propertyMap.get("Download_File_Default_Dir");
//		int fileCounts = SimpleUtils.getDirectoryFilesCount(downloadDirPath);
//		analyticsPage.exportKPIReportByTitle(scheduleKPITitle);
//
//		Thread.sleep(2000);
//		if(SimpleUtils.getDirectoryFilesCount(downloadDirPath) > fileCounts) {
//			File latestFile = SimpleUtils.getLatestFileFromDirectory(downloadDirPath);
//			String fileName = latestFile.getName();
//			SimpleUtils.pass("KPI Report exported successfully with file name '"+ fileName +"'.");
//			ArrayList<HashMap<String,String>> response = CsvUtils.getDataFromCSVFileWithHeader(downloadDirPath+"/"+fileName);
//			float totalDaysStaffingGuidanceHours = 0;
//			float totalDaysProjectedSalesHours = 0;
//			for(HashMap<String,String> hashMap : response)
//			{
//				totalDaysStaffingGuidanceHours +=  Float.valueOf(hashMap.get("Forecasted Staffing Hours (All)"));
//				totalDaysProjectedSalesHours +=  Float.valueOf(hashMap.get("Forecasted Items"));
//			}
//			System.out.println("totalDaysStaffingGuidanceHours: "+totalDaysStaffingGuidanceHours);
//			System.out.println("totalDaysProjectedSalesHours: "+totalDaysProjectedSalesHours);
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//			SimpleUtils.assertOnFail( "Schedule Page not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()),false);
//			StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
//			staffingGuidancePage.navigateToStaffingGuidanceTab();
//			SimpleUtils.assertOnFail( "Staffing Guidance tab not loaded successfully!", staffingGuidancePage.isStaffingGuidanceTabActive(),false);
//			List<Float> staffingGuidanceDaysHours = staffingGuidancePage.getStaffingGuidanceHoursCountForWeekView();
//			float staffingGuidanceWeekHours = 0;
//			for(Float dayHours : staffingGuidanceDaysHours)
//			{
//				staffingGuidanceWeekHours += dayHours;
//			}
//			if(totalDaysStaffingGuidanceHours == staffingGuidanceWeekHours)
//				SimpleUtils.pass("KPI Report Guidance hours matched with Staffing Guidance '"
//						+ totalDaysStaffingGuidanceHours +"/"+ staffingGuidanceWeekHours +"'");
//			else
//				SimpleUtils.fail("KPI Report Guidance hours not matched with Staffing Guidance '"
//						+ totalDaysStaffingGuidanceHours +"/"+ staffingGuidanceWeekHours +"'", true);
//
//			schedulePage.goToProjectedSales();
//			ProjectedSalesPage projectedSalesPage = pageFactory.createProjectedSalesPage();
//			projectedSalesPage.clickOnWeekView();
//			float salesGuidance = projectedSalesPage.getProjectedSalesGuidanceItems();
//
//			if(totalDaysProjectedSalesHours == salesGuidance)
//				SimpleUtils.pass("KPI Report Projected Sales matched with Sales Guidance '"
//						+ totalDaysProjectedSalesHours +"/"+ salesGuidance +"'");
//			else
//				SimpleUtils.fail("KPI Report Guidance hours not matched with Staffing Guidance '"
//						+ totalDaysProjectedSalesHours +"/"+ salesGuidance +"'", true);
//		}
//		else
//			SimpleUtils.fail("KPI Report Not Exported.", false);
//	}
//
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate loading of smart card on Schedule tab[ No Spinning icon].")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateScheduleSmartCardsAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Schedule.getValue()) , false);
//
//		String budgetSmartCardText = "Budget Hours";
//		String scheduleSmartCardText = "SCHEDULE V";
//		String holidaySmartCardText = "holiday";
//		String complianceSmartCardText = "require compliance review";
//		String unassignedSmartCardText = "unassigned";
//		String weatherSmartCardText = "WEATHER";
//
//		int weeksToValidate = 6;
//		scheduleCommonPage.clickOnWeekView();
//		// Validation Start with Past week
//		scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(weekViewType.Previous.getValue(), weekCount.One.getValue());
//		for(int index = 0; index < weeksToValidate; index++)
//		{
//			if(index != 0)
//				scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(weekViewType.Next.getValue(), weekCount.One.getValue());
//			boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//			if(!isActiveWeekGenerated)
//				createSchedulePage.generateOrUpdateAndGenerateSchedule();
//			boolean isBudgetSmartCardLoaded = smartCardPage.isSmartCardAvailableByLabel(budgetSmartCardText);
//			boolean isScheduleSmartCardLoaded = smartCardPage.isSmartCardAvailableByLabel(scheduleSmartCardText);
//			boolean isHolidaySmartCardLoaded = smartCardPage.isSmartCardAvailableByLabel(holidaySmartCardText);
//			boolean isComplianceSmartCardLoaded = smartCardPage.isSmartCardAvailableByLabel(complianceSmartCardText);
//			boolean isUnassignedSmartCardLoaded = smartCardPage.isSmartCardAvailableByLabel(unassignedSmartCardText);
//			boolean isWeatherSmartCardLoaded = smartCardPage.isSmartCardAvailableByLabel(weatherSmartCardText);
//
//			if(isBudgetSmartCardLoaded)
//				SimpleUtils.report("Schedule Page: Budget Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//
//			if(isScheduleSmartCardLoaded)
//				SimpleUtils.report("Schedule Page: Scheduled Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//
//			if(isHolidaySmartCardLoaded)
//				SimpleUtils.report("Schedule Page: Holiday Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//
//			if(isComplianceSmartCardLoaded && schedulePage.isComlianceReviewRequiredForActiveWeek())
//				SimpleUtils.report("Schedule Page: Compliance Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//			else if(! isComplianceSmartCardLoaded && schedulePage.isComlianceReviewRequiredForActiveWeek())
//				SimpleUtils.fail("Schedule Page: Compliance Smartcard not loaded even complaince review required for Active week ('"
//						+ scheduleCommonPage.getActiveWeekText() +"').", true);
//			if(isUnassignedSmartCardLoaded)
//				SimpleUtils.report("Schedule Page: Unassigned Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//
//			if(isWeatherSmartCardLoaded)
//				SimpleUtils.report("Schedule Page: Weather Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//		}
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate Schedule genation functionality works fine.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateScheduleGenerationFunctionalityAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , false);
//		scheduleCommonPage.clickOnWeekView();
//		int scheduleWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
//		for(int index = 0; index < scheduleWeekCount; index++)
//		{
//			if(index != 0)
//				scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.One.getValue());
//
//			if(createSchedulePage.isGenerateButtonLoaded())
//			{
//				SimpleUtils.pass("Guidance week found : '"+ scheduleCommonPage.getActiveWeekText() +"'");
//
//				SimpleUtils.assertOnFail("Schedule Page: 'Edit' Button Displaying on Guidance Week :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(! schedulePage.isActionButtonLoaded("Edit")) , false);
//
//				SimpleUtils.assertOnFail("Schedule Page: 'Analyze' Button Displaying on Guidance Week :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(! schedulePage.isActionButtonLoaded("Analyze")) , false);
//
//				SimpleUtils.assertOnFail("Schedule Page: 'Republish' Button Displaying on Guidance Week :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(! schedulePage.isActionButtonLoaded("Republish")) , false);
//
//				SimpleUtils.assertOnFail("Schedule Page: 'Refresh' Button Displaying on Guidance Week :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(! schedulePage.isActionButtonLoaded("Refresh")) , false);
//
//				SimpleUtils.assertOnFail("Schedule Page: 'Publish' Button Displaying on Guidance Week :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(! schedulePage.isActionButtonLoaded("Publish")) , false);
//
//				createSchedulePage.generateOrUpdateAndGenerateSchedule();
//
//				SimpleUtils.assertOnFail("Schedule Page: 'Generate' Button Displaying on after Generating Schedule on :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(! createSchedulePage.isGenerateButtonLoaded()) , false);
//
//				ArrayList<Float> versionHistory = schedulePage.getAllVesionLabels();
//				float scheduleInitialVersion = (float) 0.0;
//
//				SimpleUtils.assertOnFail("Schedule Page: Version History Displaying more then one versions after Generating Schedule on :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(1 == versionHistory.size()) , false);
//
//				SimpleUtils.assertOnFail("Schedule Page: Version History not starting with '0.0' after Generating Schedule on :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(versionHistory.get(0).equals(scheduleInitialVersion)) , false);
//
//				SimpleUtils.pass("Schedule Generation functionality working fine.");
//				break;
//
//			}
//		}
//	}
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate Change Location functionality[On changing location, information related to changed location should show up]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateChangeLocationAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ArrayList<HashMap<String, String>> spreadSheetData = SpreadSheetUtils.readExcel("src/test/resources/TestCasesLegionScheduleCollaboration.xlsx", "Schedule Collaboration");
//		for(HashMap<String, String> spreadSheetRow : spreadSheetData)
//		{
//			String defaultAction = "";
//			String module = spreadSheetRow.get("Module");
//			String scenario = spreadSheetRow.get("Scenario");
//			String summary = spreadSheetRow.get("Summary");
//			String testSteps = spreadSheetRow.get("Test Steps");
//			String expectedResult = spreadSheetRow.get("Expected Result");
////			String actualResult = spreadSheetRow.get("Actual Result");
//			String testData = spreadSheetRow.get("Test Data");
//			String preconditions = spreadSheetRow.get("Preconditions");
//			String testCaseType = spreadSheetRow.get("Test Case Type");
//			String priority = spreadSheetRow.get("Priority/Severifty");
//			String isAutomated = spreadSheetRow.get("Automated (Y/N)");
//			String result = spreadSheetRow.get("Result (Pass/Fail)");
//			String action = spreadSheetRow.get("Action");
//			int sectionID = 230;
//
//			if(action != null && action.trim().length() > 0)
//				defaultAction = action.toLowerCase();
//
//			if(summary == null || summary.trim().length() == 0)
//				summary = "Title is missing on SpreadSheet";
//
//			if(getModuleName()!=null && getModuleName().equalsIgnoreCase(module.replace("\n",""))){
//					SimpleUtils.addTestCase(module, scenario , summary, testSteps, expectedResult, testData,
//							preconditions, testCaseType, priority, isAutomated, result, action, getSectionID());
//				}else{
//					SimpleUtils.addSectionId(module);
//					SimpleUtils.addTestCase(module, scenario , summary, testSteps, expectedResult, testData,
//							preconditions, testCaseType, priority, isAutomated, result, action, getSectionID());
//			}
//
//		}
////		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Navigate to Schedule overview, open a week with Guidance status")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateGuidanceWeekAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!"
//				,scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()) , true);
//
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		BasePage basePase = new BasePage();
//		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//		boolean isWeekFoundToUnGenerate = false;
//			for(int i=0; i< overviewWeeks.size();i++)
//			{
//				if(overviewWeeks.get(i).getText().contains(overviewWeeksStatus.Guidance.getValue()))
//				{
//					String weekStatus = overviewWeeks.get(i).getText();
//					isWeekFoundToUnGenerate = true;
//					basePase.click(overviewWeeks.get(i));
//					boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//					if(isActiveWeekGenerated){
//						SimpleUtils.fail("Schedule with status: '" + weekStatus + "' Generated for Guidance week: '",false);
//					}else{
//						SimpleUtils.pass("Schedule with status: '" + weekStatus + "' not Generated for Guidance week: '");
//						scheduleOverviewPage.clickOverviewTab();
//					}
//
//				}
//			}
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate schedule publish feature[Check by publishing one weeks schedule]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateSchedulePublishAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!"
//				,scheduleCommonPage.verifyActivatedSubTab(SchedulePageSubTabText.Overview.getValue()) , true);
//
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		BasePage basePase = new BasePage();
//		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//		boolean isWeekFoundToUnGenerate = false;
//		for(int i=0; i< overviewWeeks.size();i++)
//		{
//			if(!overviewWeeks.get(i).getText().contains(overviewWeeksStatus.Guidance.getValue()))
//			{
//				basePase.click(overviewWeeks.get(i));
//				createSchedulePage.clickOnSchedulePublishButton();
//				break;
//			}
//		}
//	}
//
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the add schedule functionality [Check by adding any one type of schedule]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void editManualShiftScheduleAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		//The schedules that are already published should remain unchanged
//		scheduleCommonPage.clickOnDayView();
//		boolean isStoreClosed = false;
//		scheduleCommonPage.navigateToNextDayIfStoreClosedForActiveDay();
//		int previousGutterCount = schedulePage.getgutterSize();
//		scheduleNavigationTest(previousGutterCount);
//		HashMap<String, Float> ScheduledHours = schedulePage.getScheduleLabelHours();
//		Float scheduledHoursBeforeEditing = ScheduledHours.get("scheduledHours");
//		HashMap<List<String>,List<String>> teamCount = schedulePage.calculateTeamCount();
//		SimpleUtils.assertOnFail("User can add new shift for past week", (scheduleMainPage.isAddNewDayViewShiftButtonLoaded()) , true);
//		String textStartDay = schedulePage.clickNewDayViewShiftButtonLoaded();
//		newShiftPage.customizeNewShiftPage();
//		schedulePage.compareCustomizeStartDay(textStartDay);
//		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//		HashMap<String, String> shiftTimeSchedule = schedulePage.calculateHourDifference();
//		newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.verifySelectTeamMembersOption();
//		newShiftPage.clickOnOfferOrAssignBtn();
//		int updatedGutterCount = schedulePage.getgutterSize();
//		List<String> previousTeamCount = schedulePage.calculatePreviousTeamCount(shiftTimeSchedule,teamCount);
//		List<String> currentTeamCount = schedulePage.calculateCurrentTeamCount(shiftTimeSchedule);
//		verifyTeamCount(previousTeamCount,currentTeamCount);
//		scheduleMainPage.clickSaveBtn();
//		HashMap<String, Float> editScheduledHours = schedulePage.getScheduleLabelHours();
//		Float scheduledHoursAfterEditing = editScheduledHours.get("scheduledHours");
//		verifyScheduleLabelHours(shiftTimeSchedule.get("ScheduleHrDifference"), scheduledHoursBeforeEditing, scheduledHoursAfterEditing);
//	}
//
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Check navigation to different section in controls tab[On click it should not logout]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void updateControlsSectionLoadingAsInternalAdmin(String browser, String username, String password, String location,ITestContext context)
//			throws Exception {
//
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
////		context.setAttribute("TestRailId", getTestRailRunId());
//		controlsNewUIPage.clickOnControlsSchedulingPolicies();
//		controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
//		controlsNewUIPage.selectSchedulingPoliciesShiftIntervalByLabel(ControlsNewUITest.schedulingPoliciesShiftIntervalTime.ThirtyMinutes.getValue());
//		Thread.sleep(1000);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!"
//				,scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		BasePage basePase = new BasePage();
//		Thread.sleep(1000);
//		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//		boolean isWeekFoundToGenerate = false;
//		int minutesInAnHours = 60;
//		for(WebElement overviewWeek : overviewWeeks)
//		{
//			if(overviewWeek.getText().contains(ScheduleNewUITest.overviewWeeksStatus.Guidance.getValue()))
//			{
//				isWeekFoundToGenerate = true;
//				basePase.click(overviewWeek);
//				createSchedulePage.generateOrUpdateAndGenerateSchedule();
//				boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//				if(isActiveWeekGenerated)
//					SimpleUtils.pass("Schedule Page: Schedule week for duration:'"+ scheduleCommonPage.getActiveWeekText() +"' Generated Successfully.");
//				else
//					SimpleUtils.fail("Schedule Page: Schedule week for duration:'"+ scheduleCommonPage.getActiveWeekText() +"' not Generated.", false);
//				scheduleCommonPage.clickOnDayView();
//				int shiftIntervalCountInAnHour = schedulePage.getScheduleShiftIntervalCountInAnHour();
//				if((minutesInAnHours /shiftIntervalCountInAnHour) == Integer.valueOf(ControlsNewUITest.schedulingPoliciesShiftIntervalTime.ThirtyMinutes.getValue().split(" ")[0]))
//					SimpleUtils.pass("Schedule Page: Schedule week for duration:'"+ scheduleCommonPage.getActiveWeekText()
//							+"' Shift Interval Time matched as '"+ ControlsNewUITest.schedulingPoliciesShiftIntervalTime.ThirtyMinutes.getValue() +"'.");
//				else
//					SimpleUtils.fail("Schedule Page: Schedule week for duration:'"+ scheduleCommonPage.getActiveWeekText()
//							+"' Shift Interval Time not matched as '"+ ControlsNewUITest.schedulingPoliciesShiftIntervalTime.ThirtyMinutes.getValue() +"'.", false);
//				break;
//			}
//		}
//		if(! isWeekFoundToGenerate)
//			SimpleUtils.report("No 'Guidance' week found to Ungenerate Schedule.");
//
//	}
//
//
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate navigation and data loading in Day/Week view for Schedule Tab[No Spinning icon or Blank screen]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void scheduleSubTabNavigationStoreManager(String username, String password, String browser, String location, ITestContext context) throws Exception {
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		scheduleOverviewPage.loadScheduleOverview();
////		int testRailId = (Integer) context.getAttribute("TestRailId");
////		System.out.println("In Test1, Value stored in context is: "+testRailId);
//
//		schedulePage.navigateScheduleDayWeekView(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.One.getValue());
//	}
//
////	@SanitySuite(sanity =  "Sanity")
////	@UseAsTestRailSectionId(testRailSectionId = 96)
////	@Automated(automated = "Automated")
////	@Owner(owner = "Gunjan")
////	@Enterprise(name = "KendraScott2_Enterprise")
////	@TestName(description = "Validate navigation and data loading in Day/Week view for Projected Traffic Tab[No Spinning icon or Blank screen]")
////	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
////	public void projectedTrafficSubTabNavigationStoreManager(String username, String password, String browser, String location) throws Exception {
////		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
////		scheduleOverviewPage.loadScheduleOverview();
////		SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
////		salesForecastPage.loadSalesForecastforCurrentNFutureWeek(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Six.getValue());
////	}
//
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate navigation and data loading in Day/Week view for Staffing Guidance Tab[No Spinning icon or Blank screen]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void staffingGuidanceSubTabNavigationStoreManager(String username, String password, String browser, String location) throws Exception {
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		scheduleOverviewPage.loadScheduleOverview();
//		StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
//		staffingGuidancePage.navigateStaffingGuidance(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
//	}
//
//
//
//	@MobilePlatform(platform = "Android")
//	@SanitySuite(sanity =  "Sanity")
//	@Automated(automated ="Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "ChezAldo_Enterprise")
//	@TestName(description = "Validate the Create An Account button on Legion Application")
//	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	public void consoleAndMobileIntegrationForShiftOffers(String username, String password, String browser, String location) throws Exception {
////		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
////		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
////
////		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
////		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
////		List<String> overviewPageScheduledWeekStatus = scheduleOverviewPage.getScheduleWeeksStatus();
////		scheduleCommonPage.clickOnScheduleSubTab(LoginTest.SchedulePageSubTabText.Overview.getValue());
////		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
////		List<WebElement> overviewPageScheduledWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
////		for(int i=0; i <overviewPageScheduledWeeks.size();i++)
////		{
////			if(overviewPageScheduledWeeks.get(i).getText().toLowerCase().contains(ScheduleTestKendraScott2.overviewWeeksStatus.Guidance.getValue().toLowerCase()))
////			{
////				scheduleOverviewPage.clickOnGuidanceBtnOnOverview(i);
////				if(createSchedulePage.isGenerateButtonLoaded())
////				{
////					SimpleUtils.pass("Guidance week found : '"+ scheduleCommonPage.getActiveWeekText() +"'");
////					createSchedulePage.generateOrUpdateAndGenerateSchedule();
////					createSchedulePage.clickOnSchedulePublishButton();
////					break;
////				}
////			}
////		}
////		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
////		schedulePage.clickOnDayView();
////		int previousGutterCount = schedulePage.getgutterSize();
////		scheduleNavigationTest(previousGutterCount);
////		HashMap<String, Float> ScheduledHours = schedulePage.getScheduleLabelHours();
////		Float scheduledHoursBeforeEditing = ScheduledHours.get("scheduledHours");
////		SimpleUtils.assertOnFail("User can add new shift for past week", (scheduleMainPage.isAddNewDayViewShiftButtonLoaded()) , true);
////		String textStartDay = schedulePage.clickNewDayViewShiftButtonLoaded();
////		newShiftPage.customizeNewShiftPage();
////		schedulePage.compareCustomizeStartDay(textStartDay);
////		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
////		newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"),  ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
////		HashMap<String, String> shiftTimeSchedule = schedulePage.calculateHourDifference();
////		newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
////		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
////		newShiftPage.clickOnCreateOrNextBtn();
////		newShiftPage.customizeNewShiftPage();
////		newShiftPage.verifySelectTeamMembersOption();
////		newShiftPage.clickOnOfferOrAssignBtn();
////		int updatedGutterCount = schedulePage.getgutterSize();
////		scheduleMainPage.clickSaveBtn();
////		HashMap<String, Float> editScheduledHours = schedulePage.getScheduleLabelHours();
////		Float scheduledHoursAfterEditing = editScheduledHours.get("scheduledHours");
////		verifyScheduleLabelHours(shiftTimeSchedule.get("ScheduleHrDifference"), scheduledHoursBeforeEditing, scheduledHoursAfterEditing);
////		createSchedulePage.clickOnSchedulePublishButton();
//		//Schedule overview should show 5 week's schedule
//
//		launchMobileApp();
//		LoginPageAndroid loginPageAndroid = mobilePageFactory.createMobileLoginPage();
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.verifyLoginTitle("LOGIN");
////		loginPageAndroid.selectEnterpriseName();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile(getTeamMemberName(), propertyMap.get("MOBILE_PASSWORD"));
//		loginPageAndroid.clickShiftOffers(getTeamMemberName());
//
//
//	}
//
//
//	@MobilePlatform(platform = "Android")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-144 : Validate navigation to below tabs and loading of data[No spinning icon]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void legionAppNavigationAllTabsStoreManager(String username, String password, String browser, String location) throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		dashboardPage.verifyDashboardPageLoadedProperly();
//		TeamPage teamPage = pageFactory.createConsoleTeamPage();
//		teamPage.loadTeamTab();
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		scheduleOverviewPage.loadScheduleOverview();
//		SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
//		salesForecastPage.loadSalesForecast();
//		StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
//		staffingGuidancePage.loadStaffingGuidance();
//
//		schedulePage.loadSchedule();
//		AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//		analyticsPage.loadAnalyticsTab();
//
//	}
//
//
//	@MobilePlatform(platform = "Android")
//	@SanitySuite(sanity =  "Sanity")
//	@Automated(automated ="Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "ChezAldo_Enterprise")
//	@TestName(description = "Validate Shift offers functionality for Open, Swap and Cover")
//	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateShiftOffers(String username, String password, String browser, String location) throws Exception {
//	  	launchMobileApp();
//	  	//Swap Request
//		LoginPageAndroid loginPageAndroid = mobilePageFactory.createMobileLoginPage();
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile("robbie.h", "kcanys2019");
//		loginPageAndroid.clickOnScheduleTab();
//		loginPageAndroid.clickTeamMemberWorkingForShift();
//		List<String> swapRequestorAndReceiverInfo = loginPageAndroid.getSwapRequestorAndReceiverInfo();
//		loginPageAndroid.clickSubmitBtn();
//		loginPageAndroid.navigateToReturntoHomePage();
//		loginPageAndroid.clickLogoutBtn();
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile("albert.f", mobileCredentials.get("MOBILE_PASSWORD"));
//		loginPageAndroid.clickShiftOffers("albert.f");
//		loginPageAndroid.clickOnSwapLink();
//		List<String> requestReceiverInfo = loginPageAndroid.getRequestorAndReceiverInfoOnSwapTab();
//		validateSwapRequestInfo(requestReceiverInfo, swapRequestorAndReceiverInfo);
//		loginPageAndroid.clickOnSwapShiftGrid();
//		loginPageAndroid.clickOnClaimBtn();
//		loginPageAndroid.clickOnTooBar();
//		loginPageAndroid.clickLogoutBtn();
////	    Cover Request
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile("albert.f", mobileCredentials.get("MOBILE_PASSWORD"));
//		loginPageAndroid.clickOnScheduleTab();
//		loginPageAndroid.clickTeamMemberWorkingForShiftCover();
//		List<String> coverRequestorInfo = loginPageAndroid.getCoverRequestorInfo();
//		loginPageAndroid.clickSubmitBtn();
//		loginPageAndroid.clickSubmitBtn();
//		loginPageAndroid.navigateToReturntoHomePage();
//		loginPageAndroid.clickLogoutBtn();
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile("merritt.c", mobileCredentials.get("MOBILE_PASSWORD"));
//		loginPageAndroid.clickShiftOffers("merritt.c");
//		List<String> coverInfo = loginPageAndroid.getCoverRequestorInfoOnClaim();
//		validateSwapRequestInfo(coverInfo, coverRequestorInfo);
//		loginPageAndroid.clickOnClaimBtn();
//		loginPageAndroid.clickOnTooBar();
//		loginPageAndroid.clickLogoutBtn();
//		//Open Shift Request
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile(mobileCredentials.get("OPENSHIFT_USERNAME"), mobileCredentials.get("OPENSHIFT_PASSWORD"));
//		loginPageAndroid.clickOpenShiftOffers(mobileCredentials.get("OPENSHIFT_USERNAME"));
//		loginPageAndroid.clickOnClaimBtn();
//	}
//
//	@MobilePlatform(platform = "Android")
//	@SanitySuite(sanity =  "Sanity")
//	@Automated(automated ="Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "ChezAldo_Enterprise")
//	@TestName(description = "Validate the Create An Account button on Legion Application")
//	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateCoverRequest(String username, String password, String browser, String location) throws Exception {
//		launchMobileApp();
//		LoginPageAndroid loginPageAndroid = mobilePageFactory.createMobileLoginPage();
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile(mobileCredentials.get("MOBILE_USERNAME"), mobileCredentials.get("MOBILE_PASSWORD"));
//		loginPageAndroid.clickOnScheduleTab();
//		loginPageAndroid.clickTeamMemberWorkingForShiftCover();
//		List<String> coverRequestorInfo = loginPageAndroid.getCoverRequestorInfo();
//		loginPageAndroid.clickSubmitBtn();
//		loginPageAndroid.clickSubmitBtn();
//		loginPageAndroid.navigateToReturntoHomePage();
//		loginPageAndroid.clickLogoutBtn();
//		loginPageAndroid.clickFirstLoginBtn();
//		loginPageAndroid.loginToLegionWithCredentialOnMobile("Carley.r", mobileCredentials.get("MOBILE_PASSWORD"));
//		loginPageAndroid.clickShiftOffers("Carley.r");
//		List<String> coverInfo = loginPageAndroid.getCoverRequestorInfoOnClaim();
//		validateSwapRequestInfo(coverInfo, coverRequestorInfo);
//		loginPageAndroid.clickOnClaimBtn();
//	}
//
//
//	public void validateSwapRequestInfo(List<String> requestReceiverInfoOnClaim, List<String> requestorAndReceiverInfoOnRequest){
//		for(int i=0; i<requestReceiverInfoOnClaim.size();i++){
//			if(requestReceiverInfoOnClaim.get(i).equals(requestorAndReceiverInfoOnRequest.get(i))){
//				SimpleUtils.pass("Requested info " + requestorAndReceiverInfoOnRequest.get(i)
//						+ " matches with the data displayed for Claim offer " + requestReceiverInfoOnClaim.get(i));
//			}else{
//				SimpleUtils.fail("Requested info " + requestorAndReceiverInfoOnRequest.get(i)
//						+ " not matching with the data displayed for Claim offer " + requestReceiverInfoOnClaim.get(i),true);
//			}
//		}
//	}
//
//
//}
