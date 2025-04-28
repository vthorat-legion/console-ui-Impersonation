//package com.legion.tests.core;
//
//import com.legion.pages.*;
//import com.legion.pages.core.schedule.ConsoleScheduleCommonPage;
//import com.legion.test.core.mobile.LoginTest;
//import com.legion.tests.TestBase;
//import com.legion.tests.annotations.*;
//import com.legion.tests.data.CredentialDataProviderSource;
//import com.legion.utils.CsvUtils;
//import com.legion.utils.JsonUtil;
//import com.legion.utils.SimpleUtils;
//import com.legion.utils.SpreadSheetUtils;
//import java.util.Map;
//import org.openqa.selenium.WebElement;
//import org.testng.ITestContext;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.io.File;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//import static com.legion.utils.MyThreadLocal.*;
//import static com.legion.utils.MyThreadLocal.getTimeOffEndTime;
//import static com.legion.utils.MyThreadLocal.getTimeOffStartTime;
//
//public class RegressionTest extends TestBase{
//	  private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
//	  private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
//	  private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
//	  private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
//	  private static HashMap<String, String> searchDetails = JsonUtil.getPropertiesFromJsonFile("src/test/resources/searchDetails.json");
//	  SchedulePage schedulePage = null;
//	  private static HashMap<String, String> controlsLocationDetail = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ControlsPageLocationDetail.json");
//	  private static HashMap<String, String> schedulingPoliciesData = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SchedulingPoliciesData.json");
//	private static HashMap<String, String> usersAndRolesData = JsonUtil.getPropertiesFromJsonFile("src/test/resources/UserAndRoles.json");
//
//	@Override
//	@BeforeMethod
//	public void firstTest(Method method, Object[] params) throws Exception {
//		this.createDriver((String)params[0],"69","Window");
//		visitPage(method);
//		loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
//	}
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
//		    Float scheduledHoursBeforeEditing, Float scheduledHoursAfterEditing) throws Exception{
//			Float scheduledHoursExpectedValueEditing = 0.0f;
//			if(Float.parseFloat(shiftTimeSchedule) >= 6){
//				scheduledHoursExpectedValueEditing = (float)(scheduledHoursBeforeEditing +(Float.parseFloat(shiftTimeSchedule)-0.5));
//				if(scheduledHoursExpectedValueEditing.equals(scheduledHoursAfterEditing)){
//					SimpleUtils.pass("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" matches with Scheduled Hours Actual value "+scheduledHoursAfterEditing);
//				}else{
//					SimpleUtils.fail("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" does not match with Scheduled Hours Actual value "+scheduledHoursAfterEditing,false);
//				}
//			}else{
//				// If meal break is not applicable
//				scheduledHoursExpectedValueEditing = (float)(scheduledHoursBeforeEditing + Float.parseFloat(shiftTimeSchedule));
//				if(scheduledHoursExpectedValueEditing.equals(scheduledHoursAfterEditing)){
//					SimpleUtils.pass("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" matches with Scheduled Hours Actual value "+scheduledHoursAfterEditing);
//				}else{
//					SimpleUtils.fail("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" does not match with Scheduled Hours Actual value "+scheduledHoursAfterEditing,false);
//				}
//			}
//
//		}
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
//			if(!overviewWeek.getText().contains(overviewWeeksStatus.Guidance.getValue()) )
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
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
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
//				SimpleUtils.fail("Schedule Page: Compliance Smartcard not loaded even compliance review required for Active week ('"
//						+ scheduleCommonPage.getActiveWeekText() +"').", true);
//			if(isUnassignedSmartCardLoaded)
//				SimpleUtils.report("Schedule Page: Unassigned Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//
//			if(isWeatherSmartCardLoaded)
//				SimpleUtils.report("Schedule Page: Weather Smartcard loaded successfully for the week - '"+ scheduleCommonPage.getActiveWeekText() +"'.");
//		}
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate Schedule generation functionality works fine.")
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
//			if(createSchedulePage.isGenerateButtonLoaded() || createSchedulePage.isGenerateButtonLoadedForManagerView())
//			{
//				SimpleUtils.pass("Guidance/Draft week found : '"+ scheduleCommonPage.getActiveWeekText() +"'");
//
//				SimpleUtils.assertOnFail("Schedule Page: 'Edit' Button Displaying on Guidance Week :'"+scheduleCommonPage.getActiveWeekText() +"'",
//						(! schedulePage.isActionButtonLoaded("Edit")) , false);
//
//					SimpleUtils.assertOnFail("Schedule Page: 'Analyze' Button Displaying on Guidance Week :'"+scheduleCommonPage.getActiveWeekText() +"'",
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
////				ArrayList<Float> versionHistory = schedulePage.getAllVesionLabels();
////				float scheduleInitialVersion = (float) 0.0;
////
////				SimpleUtils.assertOnFail("Schedule Page: Version History Displaying more then one versions after Generating Schedule on :'"+scheduleCommonPage.getActiveWeekText() +"'",
////						(1 == versionHistory.size()) , false);
////
////				SimpleUtils.assertOnFail("Schedule Page: Version History not starting with '0.0' after Generating Schedule on :'"+scheduleCommonPage.getActiveWeekText() +"'",
////						(versionHistory.get(0).equals(scheduleInitialVersion)) , false);
//
//				SimpleUtils.pass("Schedule Generation functionality working fine.");
//				break;
//
//			}
//		}
//	}
//
//	@MobilePlatform(platform = "Android")
//	@UseAsTestRailSectionId(testRailSectionId = 379)
//	@UseAsTestCaseSectionId(testCaseSectionId = 378)
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@SanitySuite(sanity =  "Sanity")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "On changing location, information related to changed location should show up")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateChangeLocationAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ArrayList<HashMap<String, String>> spreadSheetData = SpreadSheetUtils.readExcel("src/test/resources/TestCasesLegionAnalytics.xlsx", "Analytics");
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
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate schedule publish feature")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateSchedulePublishAsStoreManager(String browser, String username, String password, String location)
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
//		for(int i=0; i< overviewWeeks.size();i++)
//		{
//			if(!overviewWeeks.get(i).getText().contains(overviewWeeksStatus.Guidance.getValue()) &&
//					(!overviewWeeks.get(i).getText().contains(overviewWeeksStatus.Finalized.getValue())) &&
//					!overviewWeeks.get(i).getText().contains(overviewWeeksStatus.Published.getValue())) {
//				basePase.click(overviewWeeks.get(i));
//				if(createSchedulePage.isGenerateButtonLoadedForManagerView()){
//					createSchedulePage.generateOrUpdateAndGenerateSchedule();
//					createSchedulePage.clickOnSchedulePublishButton();
//				}else{
//					createSchedulePage.clickOnSchedulePublishButton();
//				}
//				break;
//			}
//		}
//	}
//
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the add schedule functionality for Manual Offer")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void editManualShiftScheduleAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//		if(!isActiveWeekGenerated){
//			createSchedulePage.generateOrUpdateAndGenerateSchedule();
//		}
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
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@UseAsTestRailSectionId(testRailSectionId = 96)
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the impact of Shift Interval minutes on Schedule page")
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
//				scheduleCommonPage.navigateToNextDayIfStoreClosedForActiveDay();
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
//	@MobilePlatform(platform = "Android")
//	@SanitySuite(sanity =  "Sanity")
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate navigation and data loading in Day/Week view for Schedule Tab No Spinning icon or Blank screen")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void scheduleSubTabNavigationStoreManager(String username, String password, String browser, String location, ITestContext context) throws Exception {
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		scheduleOverviewPage.loadScheduleOverview();
//
//		schedulePage.navigateScheduleDayWeekView(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Two.getValue());
//	}
//
//	@MobilePlatform(platform = "Android")
//	@SanitySuite(sanity =  "Sanity")
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate loading of data for Schedule in week view. No spinning icon.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void legionAppNavigationAllTabsStoreManager(String username, String password, String browser, String location) throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		dashboardPage.verifyDashboardPageLoadedProperly();
//		TeamPage teamPage = pageFactory.createConsoleTeamPage();
//		teamPage.loadTeamTab();
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		scheduleOverviewPage.loadScheduleOverview();
////		SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();
////		salesForecastPage.loadSalesForecast();
////		StaffingGuidancePage staffingGuidancePage = pageFactory.createStaffingGuidancePage();
////		staffingGuidancePage.loadStaffingGuidance();
//
//		schedulePage.loadSchedule();
//		AnalyticsPage analyticsPage = pageFactory.createConsoleAnalyticsPage();
//		analyticsPage.loadAnalyticsTab();
//
//	}
//
//	@MobilePlatform(platform = "Android")
//	@SanitySuite(sanity =  "Sanity")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the breadcrumb on controls tab")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyControlsBreadcrumbAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		//navigateToControlsSchedulingPolicies(controlsNewUIPage);
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Control Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		controlsNewUIPage.verifyAllLocations(schedulingPoliciesData.get("ALL_LOCATIONS"));
//		controlsNewUIPage.verifySearchLocations(schedulingPoliciesData.get("SEARCH_LOCATION_TEXT"));
//	}
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-92: FOR-456 :- Should not keep the stickiness of filter in Guidance and Schedule when re-entering schedule app.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void verifyFiltersWhileNavigatingToGuidanceAndScheduleTabAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//		scheduleCommonPage.clickOnWeekView();
//		scheduleMainPage.selectWorkRoleFilterByText("Manager", true);
//		dashboardPage.navigateToDashboard();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		SimpleUtils.assertOnFail("'Overview' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//		ArrayList<String> selectedWorkRoles = schedulePage.getSelectedWorkRoleOnSchedule();
//		SimpleUtils.assertOnFail("Work Role filter not cleared after navigating to Dashboard and overview page.",
//				(selectedWorkRoles.size() == 0), false);
//		SimpleUtils.pass("Work Role filter cleared after navigating to Dashboard and overview page.");
//	}
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-108: Validate Store is closed for current day and if it is so then navigate to next day schedule.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void navigateToNextDayIfActiveDayHasStoreClosedAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//		scheduleCommonPage.navigateToNextDayIfStoreClosedForActiveDay();
//	}
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "Coffee_Enterprise")
//	@TestName(description = "TP-126: LegionCoffee :- Validate Guidance Hours, Scheduled and Other Hrs on Dashboard page for all the locations.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void validateGuidanceScheduleAndOtherHoursOnDashBoardForAllLocationAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		String bayAreaLocation = "Bay Area";
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//		locationSelectorPage.changeLocation(bayAreaLocation);
//		float totalGuidanceHours = 0;
//		float totalScheduledHours = 0;
//		float totalOtherHours = 0;
//		ArrayList<HashMap<String, Float>> dashBoardBayAreaForcastdata = dashboardPage.getDashboardForeCastDataForAllLocation();
//		for (HashMap<String, Float> locationHours : dashBoardBayAreaForcastdata) {
//			totalGuidanceHours = totalGuidanceHours + locationHours.get("Guidance");
//			totalScheduledHours = totalScheduledHours + locationHours.get("Scheduled");
//			totalOtherHours = totalOtherHours + locationHours.get("Other");
//		}
//
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		SimpleUtils.assertOnFail("'Overview' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//		scheduleCommonPage.clickOnDayView();
//		HashMap<String, Float> dayViewHours = smartCardPage.getScheduleLabelHoursAndWages();
//
//		float bayAreaBudgetedHours = dayViewHours.get("budgetedHours");
//		float bayScheduledHoursHours = dayViewHours.get("scheduledHours");
//		float bayOtherHoursHours = dayViewHours.get("otherHours");
//
//		SimpleUtils.assertOnFail("Sum of All Locatons Guidance Hours not matched with Bay Area Budgeted Hours ('" +
//						totalScheduledHours + "/" + bayScheduledHoursHours + "'",
//				(totalScheduledHours == bayScheduledHoursHours), false);
//
//		SimpleUtils.assertOnFail("Sum of All Locatons Guidance Hours not matched with Bay Area Budgeted Hours ('" +
//						totalGuidanceHours + "/" + bayAreaBudgetedHours + "'",
//				(totalGuidanceHours == bayAreaBudgetedHours), false);
//
//		SimpleUtils.assertOnFail("Sum of All Locatons Guidance Hours not matched with Bay Area Budgeted Hours ('" +
//						totalOtherHours + "/" + bayOtherHoursHours + "'",
//				(totalOtherHours == bayOtherHoursHours), false);
//
//		SimpleUtils.pass("Bay Area Day view Guidance, Scheduled And Other Hours matched with sum of all locations Hours.");
//
//
//	}
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-128: Validation of weather forecast page on Schedule tab.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void validateWeatherForecastOnSchedulePageAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		String bayAreaLocation = "Bay Area";
//		String austinDowntownLocation = "AUSTIN DOWNTOWN";
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//		LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
//		if (locationSelectorPage.isLocationSelected(bayAreaLocation))
//			locationSelectorPage.changeLocation(austinDowntownLocation);
//
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",
//				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()), false);
//
//		String WeatherCardText = "WEATHER";
//		//Validate Weather Smart card on Week View
//		scheduleCommonPage.clickOnWeekView();
//		Thread.sleep(5000);
//		String activeWeekText = scheduleCommonPage.getActiveWeekText();
//
//		if (smartCardPage.isSmartCardAvailableByLabel(WeatherCardText)) {
//			SimpleUtils.pass("Weather Forecart Smart Card appeared for week view duration: '" + activeWeekText + "'");
//			String[] splitActiveWeekText = activeWeekText.split(" ");
//			String smartCardTextByLabel = smartCardPage.getsmartCardTextByLabel(WeatherCardText);
//			String weatherTemperature = smartCardPage.getWeatherTemperature();
//
//			SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain starting day('" + splitActiveWeekText[0] + "') of active week: '" + activeWeekText + "'",
//					smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[0].toLowerCase()), true);
//
//			SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain Ending day('" + splitActiveWeekText[0] + "') of active week: '" + activeWeekText + "'",
//					smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[0].toLowerCase()), true);
//			if (weatherTemperature != "")
//				SimpleUtils.pass("Weather Forecart Smart Card contains Temperature value: '" + weatherTemperature + "' for the duration: '" +
//						activeWeekText + "'");
//			else
//				SimpleUtils.fail("Weather Forecart Smart Card not contains Temperature value for the duration: '" + activeWeekText + "'", true);
//		} else {
//			SimpleUtils.fail("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'", true);
//		}
//
//		//Validate Weather Smart card on day View
//		scheduleCommonPage.clickOnDayView();
//		for (int index = 0; index < ScheduleTestKendraScott2.dayCount.Seven.getValue(); index++) {
//			if (index != 0)
//				scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.One.getValue());
//
//			String activeDayText = scheduleCommonPage.getActiveWeekText();
//			if (smartCardPage.isSmartCardAvailableByLabel(WeatherCardText)) {
//				SimpleUtils.pass("Weather Forecart Smart Card appeared for week view duration: '" + activeDayText + "'");
//				String[] splitActiveWeekText = activeDayText.split(" ");
//				String smartCardTextByLabel = smartCardPage.getsmartCardTextByLabel(WeatherCardText);
//				SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain starting day('" + splitActiveWeekText[1] + "') of active day: '" + activeDayText + "'",
//						smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[1].toLowerCase()), true);
//				String weatherTemperature = smartCardPage.getWeatherTemperature();
//				if (weatherTemperature != "")
//					SimpleUtils.pass("Weather Forecart Smart Card contains Temperature value: '" + weatherTemperature + "' for the duration: '" +
//							activeWeekText + "'");
//				else
//					SimpleUtils.pass("Weather Forecart Smart Card not contains Temperature value for the duration: '" + activeWeekText + "'");
//			} else {
//				SimpleUtils.fail("Weather Forecart Smart Card not appeared for week view duration: '" + activeDayText + "'", true);
//			}
//		}
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-145 : Validate schedule publish feature[Check by publishing one weeks schedule].")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateSchedulePublishFeatureAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!"
//				,scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		BasePage basePase = new BasePage();
//		List<WebElement> overviewWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//		boolean isWeekFoundToPublish = false;
//		for(WebElement overviewWeek : overviewWeeks)
//		{
//			if(overviewWeek.getText().contains(ScheduleNewUITest.overviewWeeksStatus.Draft.getValue()))
//			{
//				isWeekFoundToPublish = true;
//				basePase.click(overviewWeek);
//				createSchedulePage.publishActiveSchedule();
//				basePase.waitForSeconds(2);
//				SimpleUtils.assertOnFail("Schedule not published for week: '"+ scheduleCommonPage.getActiveWeekText() +"'"
//						,createSchedulePage.isWeekPublished(), false);
//				break;
//			}
//			else if(overviewWeek.getText().contains(ScheduleNewUITest.overviewWeeksStatus.Guidance.getValue()))
//			{
//				isWeekFoundToPublish = true;
//				basePase.click(overviewWeek);
//				createSchedulePage.generateOrUpdateAndGenerateSchedule();
//				SimpleUtils.assertOnFail("Schedule not Generated for week: '"+ scheduleCommonPage.getActiveWeekText() +"'"
//						, createSchedulePage.isWeekGenerated(), false);
//				createSchedulePage.publishActiveSchedule();
//				basePase.waitForSeconds(2);
//				SimpleUtils.assertOnFail("Schedule not published for week: '"+ scheduleCommonPage.getActiveWeekText() +"'"
//						, createSchedulePage.isWeekPublished(), false);
//				break;
//			}
//		}
//
//		if(! isWeekFoundToPublish)
//			SimpleUtils.report("No Draft/Guidance week found.");
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate Today's Forecast should have a non-0 number[If zero, it should be for the day store is closed]")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateDashboardTodaysForcastAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		HashMap<String, Float> todaysForcastData = dashboardPage.getTodaysForcastData();
//		if(todaysForcastData.size() > 0)
//		{
//			float demandForecast = todaysForcastData.get("demandForecast");
//			float guidanceHours = todaysForcastData.get("guidanceHours");
//
//			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//			boolean isStoreClosedToday = scheduleCommonPage.isStoreClosedForActiveWeek();
//			if(! isStoreClosedToday && (demandForecast <= 0))
//				SimpleUtils.fail("Dashboard Page: Today's Forecast contains '0' Demand Forecast.", true);
//			else
//				SimpleUtils.pass("Dashboard Page: Today's Forecast contains '"+ (int) demandForecast +"' Shoppers.");
//
//			if(! isStoreClosedToday && (guidanceHours <= 0))
//				SimpleUtils.fail("Dashboard Page: Today' Forecast contains '0' Guidance Hours.", true);
//			else
//				SimpleUtils.pass("Dashboard Page: Today's Forecast contains '"+ guidanceHours +"' Guidance Hours.");
//		}
//	}
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-156 : Schedule :- Verify whether schedule timing is getting reflected once user changes timing locally.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void changeAndValidateOperatingHoursAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnWeekView();
//		if(!createSchedulePage.isWeekGenerated())
//			createSchedulePage.generateSchedule();
//
//		scheduleCommonPage.clickOnDayView();
//		System.out.println("Active Duration : "+scheduleCommonPage.getActiveWeekText());
//		toggleSummaryPage.toggleSummaryView();
//		if(toggleSummaryPage.isSummaryViewLoaded())
//		{
//			String day = scheduleCommonPage.getActiveWeekText().split(" ")[0];
//			String startTime = "10:00am";
//			String endTime = "6:00pm";
//			schedulePage.updateScheduleOperatingHours(day, startTime, endTime);
//			toggleSummaryPage.toggleSummaryView();
//			boolean isOperatingHoursUpdated = schedulePage.isScheduleOperatingHoursUpdated(startTime, endTime);
//			if(isOperatingHoursUpdated)
//				SimpleUtils.pass("Updated Operating Hours Reflecting on Schedule page.");
//			else
//				SimpleUtils.fail("Updated Operating Hours not Reflecting on Schedule page.", false);
//		}
//		else
//			SimpleUtils.fail("Unable to load Summary view on the week '"+ scheduleCommonPage.getActiveWeekText() +"'.", false);
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate User is able to convert schedule to Open in week view")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void verifyOpenScheduleViewAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
//		//            loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		//		schedulePage.beforeEdit();
//		scheduleCommonPage.clickImmediateNextToCurrentActiveWeekInDayPicker();
//		scheduleMainPage.clickOnEditButton();
//		//		schedulePage.viewProfile();
////		schedulePage.changeWorkerRole();
////		schedulePage.assignTeamMember();
////		//		schedulePage.assignTeamMemberFlyout();
////		newShiftPage.verifySelectTeamMembersOption();
////		newShiftPage.clickOnOfferOrAssignBtn();
//		schedulePage.convertToOpenShift();
//
//	}
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Schedule Overlapping")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void verifyScheduleOverlappingAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		scheduleCommonPage.clickOnDayView();
//		boolean isStoreClosed = false;
//		scheduleCommonPage.navigateToNextDayIfStoreClosedForActiveDay();
//		scheduleMainPage.clickOnEditButton();
//		SimpleUtils.assertOnFail("User can add new shift for past week",
//				(scheduleMainPage.isAddNewDayViewShiftButtonLoaded()), true);
//		String textStartDay = schedulePage.clickNewDayViewShiftButtonLoaded();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//		newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.selectTeamMembersOptionForOverlappingSchedule();
//		newShiftPage.clickOnOfferOrAssignBtn();
//		scheduleMainPage.clickSaveBtn();
//		schedulePage.verifyScheduleStatusAsOpen();
//		schedulePage.verifyScheduleStatusAsTeamMember();
//	}
//
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Schedule Overlapping")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void verifyScheduleClopeningAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		String day = null;
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		ToggleSummaryPage toggleSummaryPage = pageFactory.createToggleSummaryPage();
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		scheduleCommonPage.clickOnDayView();
//		String activeDay = scheduleCommonPage.getActiveAndNextDay();
//		toggleSummaryPage.toggleSummaryView();
//		if (toggleSummaryPage.isSummaryViewLoaded()) {
//			day = scheduleCommonPage.getActiveWeekText().split(" ")[0];
//		}else{
//			SimpleUtils.fail("Unable to load Summary view on the week '" + scheduleCommonPage.getActiveWeekText() + "'.", false);
//		}
//		HashMap<String, String> activeDayAndOperatingHrs = toggleSummaryPage.getOperatingHrsValue(day);
//		String shiftStartTime = (activeDayAndOperatingHrs.get("ScheduleOperatingHrs").split("-"))[1].replaceAll("[^0-9]","");
//		String shiftEndTime = (activeDayAndOperatingHrs.get("ScheduleOperatingHrs").split("-"))[2].replaceAll("[^0-9]","");
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		int previousGutterCount = schedulePage.getgutterSize();
//		scheduleNavigationTest(previousGutterCount);
//		String textStartDay = schedulePage.clickNewDayViewShiftButtonLoaded();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.moveSliderAtCertainPoint(shiftEndTime, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.moveSliderAtCertainPoint(propertyCustomizeMap.get("INCREASE_START_TIME_CLOPENING"), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//		validateAssignTeamMemberPageAndSaveSchedule();
//		scheduleCommonPage.clickOnNextDaySchedule(activeDay);
//		int previousGutterCountNextDay = schedulePage.getgutterSize();
//		scheduleNavigationTest(previousGutterCountNextDay);
//		schedulePage.clickNewDayViewShiftButtonLoaded();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.moveSliderAtCertainPoint(propertyCustomizeMap.get("INCREASE_END_TIME_CLOPENING"), ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.moveSliderAtCertainPoint(shiftStartTime, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//		validateAssignTeamMemberPageAndSaveSchedule();
//		schedulePage.verifyClopeningHrs();
//		scheduleCommonPage.clickOnPreviousDaySchedule(activeDay);
//		schedulePage.verifyClopeningHrs();
//
//
//	}
//
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Verify Manager and System generated view of Schedule tab")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void verifyScheduleAsManagerOrSystemGeneratedAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		String day = null;
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		schedulePage.verifyActiveScheduleType();
//
//	}
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-139: Controls :- User should be able to save data for Company Profile.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void updateUserLocationAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		controlsNewUIPage.clickOnControlsCompanyProfileCard();
//
//		String companyName = controlsLocationDetail.get("Company_Name");
//		String businessAddress = controlsLocationDetail.get("Business_Address");
//		String city = controlsLocationDetail.get("City");
//		String state = controlsLocationDetail.get("State");
//		String country = controlsLocationDetail.get("Country");
//		String zipCode = controlsLocationDetail.get("Zip_Code");
//		String timeZone = controlsLocationDetail.get("Time_Zone");
//		String website = controlsLocationDetail.get("Website");
//		String firstName = controlsLocationDetail.get("First_Name");
//		String lastName = controlsLocationDetail.get("Last_Name");
//		String email = controlsLocationDetail.get("E_mail");
//		String phone =controlsLocationDetail.get("Phone");
//
//		controlsNewUIPage.updateUserLocationProfile(companyName, businessAddress, city, state, country, zipCode, timeZone, website,
//				firstName, lastName, email, phone);
//		boolean isUserLocationProfileUpdated = controlsNewUIPage.isUserLocationProfileUpdated(companyName, businessAddress, city, state, country, zipCode,
//				timeZone, website, firstName, lastName, email, phone);
//		if(isUserLocationProfileUpdated)
//			SimpleUtils.pass("User Location Profile Updated successfully.");
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-140 : Controls - User should be able to edit Controls > Working Hours successfully.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void updateWorkingHoursAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		ArrayList<HashMap< String,String>> regularWorkingHours = JsonUtil.getArrayOfMapFromJsonFile("src/test/resources/ControlsRegularWorkingHours.json");
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		controlsNewUIPage.clickOnControlsWorkingHoursCard();
//		for(HashMap<String, String> eachRegularHours : regularWorkingHours)
//		{
//			String isStoreClosed = eachRegularHours.get("isStoreClosed");
//			String openingHours = eachRegularHours.get("Opening_Hours");
//			String closingHours = eachRegularHours.get("Closing_Hours");
//			String day = eachRegularHours.get("Day");
//			controlsNewUIPage.updateControlsRegularHours(isStoreClosed, openingHours, closingHours, day);
//		}
//		controlsNewUIPage.clickOnSaveRegularHoursBtn();
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-141 : Controls - Scheduling Policies > Budget: Enable and disable the budget and check its impact on schedule.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyBudgetSmartcardEnableOrDisableFromSchedulingPoliciesAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
//		navigateToControlsSchedulingPolicies(controlsNewUIPage);
//
//		// Enable Budget Smartcard
//		boolean enableBudgetSmartcard = true;
//		controlsNewUIPage.enableDisableBudgetSmartcard(enableBudgetSmartcard);
//
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);
//
//		String budgetSmartcardText = "WEEKLY BUDGET";
//		boolean isBudgetSmartcardAppeared = smartCardPage.isSmartCardAvailableByLabel(budgetSmartcardText);
//		SimpleUtils.assertOnFail("Budget Smartcard not loaded on 'Schedule' tab even Scheduling policies Enabled Budget Smartcard.",
//				isBudgetSmartcardAppeared , false);
//		if(isBudgetSmartcardAppeared)
//			SimpleUtils.pass("Budget Smartcard loaded on 'Schedule' tab when Scheduling policies Enabled Budget Smartcard.");
//
//
//		// Disable Budget Smartcard
//		navigateToControlsSchedulingPolicies(controlsNewUIPage);
//		enableBudgetSmartcard = false;
//		controlsNewUIPage.enableDisableBudgetSmartcard(enableBudgetSmartcard);
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);
//		isBudgetSmartcardAppeared = smartCardPage.isSmartCardAvailableByLabel(budgetSmartcardText);
//		SimpleUtils.assertOnFail("Budget Smartcard loaded on 'Schedule' tab even Scheduling policies Disabled Budget Smartcard.",
//				! isBudgetSmartcardAppeared , false);
//		if(! isBudgetSmartcardAppeared)
//			SimpleUtils.pass("Budget Smartcard not loaded on 'Schedule' tab when Scheduling policies Disabled Budget Smartcard.");
//	}
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-147: Onboarding - Check navigation to different section in controls tab[On click it should not logout].")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void updateControlsSectionLoadingAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//
//		// Validate Controls Location Profile Section
//		controlsNewUIPage.clickOnControlsLocationProfileSection();
//		boolean isLocationProfile = controlsNewUIPage.isControlsLocationProfileLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Location Profile Section not Loaded.", isLocationProfile, true);
//
//		// Validate Controls Scheduling Policies Section
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsSchedulingPolicies();
//		boolean isSchedulingPolicies = controlsNewUIPage.isControlsSchedulingPoliciesLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Scheduling Policies Section not Loaded.", isSchedulingPolicies, true);
//
//		// Validate Controls Schedule Collaboration Section
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
//		boolean isScheduleCollaboration = controlsNewUIPage.isControlsScheduleCollaborationLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Schedule Collaboration Section not Loaded.", isScheduleCollaboration, true);
//
//		// Validate Controls Compliance Section
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsComplianceSection();
//		boolean isCompliance = controlsNewUIPage.isControlsComplianceLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Compliance Section not Loaded.", isCompliance, true);
//
//		// Validate Controls Users and Roles Section
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsUsersAndRolesSection();
//		boolean isUsersAndRoles = controlsNewUIPage.isControlsUsersAndRolesLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Users and Roles Section not Loaded.", isUsersAndRoles, true);
//
//		// Validate Controls Tasks and Work Roles Section
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsTasksAndWorkRolesSection();
//		boolean isTasksAndWorkRoles = controlsNewUIPage.isControlsTasksAndWorkRolesLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Tasks and Work Roles Section not Loaded.", isTasksAndWorkRoles, true);
//
//		// Validate Working Hours Profile Section
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsWorkingHoursCard();
//		boolean isWorkingHours = controlsNewUIPage.isControlsWorkingHoursLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Working Hours Section not Loaded.", isWorkingHours, true);
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-154: Controls :- Shift Interval minutes for the enterprise should get updated successfully.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void updateAndValidateShiftIntervalTimeAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
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
//	}
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Controls - Scheduling Policies > Enable Assignment Rule on Scheduling Policies")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void enableAssignmentRuleFromSchedulingPoliciesAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		navigateToControlsSchedulingPolicies(controlsNewUIPage);
//		controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
//
//		controlsNewUIPage.enableOverRideAssignmentRuleAsYes();
////		// Enable Budget Smartcard
////		boolean enableBudgetSmartcard = true;
////		controlsNewUIPage.enableDisableBudgetSmartcard(enableBudgetSmartcard);
////
////
////		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
////		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
////		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);
////
////		String budgetSmartcardText = "WEEKLY BUDGET";
////		boolean isBudgetSmartcardAppeared = smartCardPage.isSmartCardAvailableByLabel(budgetSmartcardText);
////		SimpleUtils.assertOnFail("Budget Smartcard not loaded on 'Schedule' tab even Scheduling policies Enabled Budget Smartcard.",
////				isBudgetSmartcardAppeared , false);
////		if(isBudgetSmartcardAppeared)
////			SimpleUtils.pass("Budget Smartcard loaded on 'Schedule' tab when Scheduling policies Enabled Budget Smartcard.");
////
////
////		// Disable Budget Smartcard
////		navigateToControlsSchedulingPolicies(controlsNewUIPage);
////		enableBudgetSmartcard = false;
////		controlsNewUIPage.enableDisableBudgetSmartcard(enableBudgetSmartcard);
////		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
////		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
////		SimpleUtils.assertOnFail("Schedule page 'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);
////		isBudgetSmartcardAppeared = smartCardPage.isSmartCardAvailableByLabel(budgetSmartcardText);
////		SimpleUtils.assertOnFail("Budget Smartcard loaded on 'Schedule' tab even Scheduling policies Disabled Budget Smartcard.",
////				! isBudgetSmartcardAppeared , false);
////		if(! isBudgetSmartcardAppeared)
////			SimpleUtils.pass("Budget Smartcard not loaded on 'Schedule' tab when Scheduling policies Disabled Budget Smartcard.");
//	}
//
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-161: Automate all the areas of controls  with Admin, Customer Admin and SM to validate"
//			+ " the fields enabled or disabled with corresponding work role access.")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateControlsAllFieldsEnabledOrDisabledAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//		String fileName = "UsersCredentials.json";
//		fileName=SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//		HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//		Object[][] teamLeadCredentials = userCredentials.get("TeamLead");
//		Object[][] teamMemberCredentials = userCredentials.get("TeamMember");
//		Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//		LoginPage loginPage = pageFactory.createConsoleLoginPage();
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Internal Admin'</b>.");
//		if(controlsNewUIPage.isControlsConsoleMenuAvailable()) {
//			verifyControlsPageAllFieldsEditableOrNot();
//		}
//		else
//			SimpleUtils.report("Controls Console Menu not loaded Successfully!.");
//
//		loginPage.logOut();
//
//
//		/*
//		 * Login as Store Manager
//		 */
//		loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1]),
//				String.valueOf(storeManagerCredentials[0][2]));
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Store Manager'</b>.");
//		if(controlsNewUIPage.isControlsConsoleMenuAvailable()) {
//			verifyControlsPageAllFieldsEditableOrNot();
//		}
//		else
//			SimpleUtils.report("Controls Console Menu not loaded Successfully!.");
//		loginPage.logOut();
//
//		/*
//		 * Login as Team Lead
//		 */
//		loginToLegionAndVerifyIsLoginDone(String.valueOf(teamLeadCredentials[0][0]), String.valueOf(teamLeadCredentials[0][1]),
//				String.valueOf(teamLeadCredentials[0][2]));
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Team Lead'</b>.");
//		if(controlsNewUIPage.isControlsConsoleMenuAvailable()) {
//			verifyControlsPageAllFieldsEditableOrNot();
//		}
//		else
//			SimpleUtils.report("Controls Console Menu not loaded Successfully!.");
//		loginPage.logOut();
//
//		/*
//		 * Login as Team Member
//		 */
//		loginToLegionAndVerifyIsLoginDone(String.valueOf(teamMemberCredentials[0][0]), String.valueOf(teamMemberCredentials[0][1]),
//				String.valueOf(teamMemberCredentials[0][2]));
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Team Member'</b>.");
//		if(controlsNewUIPage.isControlsConsoleMenuAvailable()) {
//			verifyControlsPageAllFieldsEditableOrNot();
//		}
//		else
//			SimpleUtils.report("Controls Console Menu not loaded Successfully!.");
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate Team Search and Coverage in Team Tab")
//	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateTeamTabAsStoreManager(String username, String password, String browser, String location)
//			throws Exception
//	{
//		//To Do Should be separate Test from Schedule test
//		//loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
//		TeamPage teamPage = pageFactory.createConsoleTeamPage();
//		teamPage.goToTeam();
//		String key= searchDetails.get("jobTitle");
//		List<String> list = new ArrayList<String>(Arrays.asList(key.split(",")));
//		teamPage.performSearchRoster(list);
//		teamPage.coverage();
//		teamPage.coverageViewToPastOrFuture(TeamTest.weekViewType.Next.getValue(), TeamTest.weekCount.Six.getValue());
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated = "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "DGStage_Enterprise")
//	@TestName(description = "Verify whether Manager is able to approve Time Off request")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void VerifyWhetherManagerCanApproveTimeOffRequestAsTeamMember(String browser, String username, String password, String location)
//			throws Exception
//	{
//		// Login with Team Member Credentials
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
//		dashboardPage.clickOnProfileIconOnDashboard();
//		dashboardPage.clickOnTimeOffLink();
////		profileNewUIPage.clickOnProfileConsoleMenu();
////		SimpleUtils.assertOnFail("Profile Page not loaded.", profileNewUIPage.isProfilePageLoaded(), false);
//		String myTimeOffSectionLabel = "My Time Off";
//		profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffSectionLabel);
//		String expectedRequestStatus = "PENDING";
//		String timeOffReasonLabel = "VACATION";
//		String timeOffExplanationText = "Sample Explanation Text";
//		profileNewUIPage.createNewTimeOffRequest(timeOffReasonLabel, timeOffExplanationText);
//		String requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel
//				, timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime());
//		if(requestStatus.toLowerCase().contains(expectedRequestStatus.toLowerCase()))
//			SimpleUtils.pass("Profile Page: New Time Off Request status is '"+requestStatus+"'.");
//		else
//			SimpleUtils.fail("Profile Page: New Time Off Request status is  not '"+expectedRequestStatus
//					+"', status found as '"+requestStatus+"'.", false);
//		LoginPage loginPage = pageFactory.createConsoleLoginPage();
//		loginPage.logOut();
//
//		// Login as Store Manager
//		String fileName = "UsersCredentials.json";
//		fileName = SimpleUtils.getEnterprise(getEnterprise() + "_Enterprise")+ fileName;
//		HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//		Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//		loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1])
//				, String.valueOf(storeManagerCredentials[0][2]));
//		dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		TeamPage teamPage = pageFactory.createConsoleTeamPage();
//		teamPage.goToTeam();
//		teamPage.openToDoPopupWindow();
//		teamPage.approveOrRejectTimeOffRequestFromToDoList(username, getTimeOffStartTime(), getTimeOffEndTime(), TeamTestKendraScott2.timeOffRequestAction.Approve.getValue());
//		teamPage.closeToDoPopupWindow();
//		teamPage.searchAndSelectTeamMemberByName(username);
//		String TeamMemberProfileSubSectionLabel = "Time Off";
//		profileNewUIPage.selectProfilePageSubSectionByLabel(TeamMemberProfileSubSectionLabel);
//		requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel,
//				timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime());
//		if(requestStatus.toLowerCase().contains(TeamTestKendraScott2.timeOffRequestStatus.Approved.getValue().toLowerCase()))
//			SimpleUtils.pass("Team Page: Time Off request Approved By Store Manager reflected on Team Page.");
//		else
//			SimpleUtils.fail("Team Page: Time Off request Approved By Store Manager not reflected on Team Page.", false);
//
//		loginPage.logOut();
//
//		// Login as Team Member Again
//		loginToLegionAndVerifyIsLoginDone(username, password, location);
//		dashboardPage.clickOnProfileIconOnDashboard();
//		dashboardPage.clickOnTimeOffLink();
//		profileNewUIPage.selectProfilePageSubSectionByLabel(myTimeOffSectionLabel);
//		requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel
//				, timeOffExplanationText, getTimeOffStartTime(), getTimeOffEndTime()
//
//		);
//		if(requestStatus.toLowerCase().contains(TeamTestKendraScott2.timeOffRequestStatus.Approved.getValue().toLowerCase()))
//			SimpleUtils.pass("Profile Page: New Time Off Request status is '"+requestStatus
//					+"' after Store Manager Approved the request.");
//		else
//			SimpleUtils.fail("Profile Page: New Time Off Request status is '"+requestStatus
//					+"' after Store Manager Approved the request.", false);
//	}
//
//
//
//	private void verifyControlsPageAllFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		Thread.sleep(1000);
//		verifyLocationInformationEditModeFieldsEditableOrNot();
//
//		// verifying Scheduling Policies 'Schedules' section Fields
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , true);
//		controlsNewUIPage.clickOnControlsSchedulingPolicies();
//
//		verifySchedulingPoliciesAllSectionsFieldsEditableOrNot();
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		verifySchedulingPoliciesAllSectionsFieldsEditableOrNot();
//
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsScheduleCollaborationSection();
//		verifyingScheduleCollaborationFieldsEditableOrNot();
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		verifyingScheduleCollaborationFieldsEditableOrNot();
//
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsComplianceSection();
//		if(controlsNewUIPage.isControlsComplianceLoaded()) {
//			verifyingComplianceFieldsEditableOrNot();
//			controlsNewUIPage.clickOnGlobalLocationButton();
//			verifyingComplianceFieldsEditableOrNot();
//		}
//
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsUsersAndRolesSection();
//		if(controlsNewUIPage.isControlsUsersAndRolesLoaded()) {
//			controlsNewUIPage.selectUsersAndRolesSubTabByLabel(ScheduleTestKendraScott2.usersAndRolesSubTabs.AllUsers.getValue());
//			verifyingUserAndRolesAddNewUserPageFieldsEditableOrNot();
////			String userFirstName = "Fedrico";
//			verifyingUserAndRolesEditUserPageFieldsEditableOrNot(usersAndRolesData.get("USER_FIRST_NAME"));
//			controlsNewUIPage.selectUsersAndRolesSubTabByLabel(ScheduleTestKendraScott2.usersAndRolesSubTabs.AccessByJobTitles.getValue());
////			String employeeJobTitle = "Retail Manager";
//			verifyingUserAndRolesUpdateEmployeeJobTitleEditableOrNonEditableFields(usersAndRolesData.get("PREVIOUS_EMPLOYEE_JOB_TITLE"));
////			String newEmployeeJobTitle = "Sample Employee Job Title";
////			String newEmployeeJobTitleRole = "Store Manager";
//			verifyingUserAndRolesCreatNewEmployeeJobTitleEditableOrNonEditableFields(
//					usersAndRolesData.get("NEW_EMPLOYEE_JOB_TITLE"), usersAndRolesData.get("NEW_EMPLOYEE_JOB_TITLE_ROLE"));
////			String badgeLabel = "Employee From Mars";
//			controlsNewUIPage.selectUsersAndRolesSubTabByLabel(ScheduleTestKendraScott2.usersAndRolesSubTabs.Badges.getValue());
//			verifyingUserAndRolesUpdateBadgesEditableOrNonEditableFields(usersAndRolesData.get("BADGE_LABEL"));
//			verifyingUserAndRolesCreateNewBadgesEditableOrNonEditableFields();
//		}
//
//		// Tasks and Work Roles Section
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsTasksAndWorkRolesSection();
//		if(controlsNewUIPage.isControlsTasksAndWorkRolesLoaded()) {
//			verifyingTasksAndWorkRolesSectionWorkRolesTabEditableOrNonEditableFields();
//		}
//
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsTasksAndWorkRolesSection();
//		if(controlsNewUIPage.isControlsTasksAndWorkRolesLoaded()) {
//			verifyingTasksAndWorkRolesSectionLaborCalculatorTabEditableOrNonEditableFields();
//		}
//		// Global Location
//
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsTasksAndWorkRolesSection();
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		if(controlsNewUIPage.isControlsTasksAndWorkRolesLoaded()) {
//			verifyingTasksAndWorkRolesAddWorkRolePageEditableOrNonEditableFields();
//			verifyingTasksAndWorkRolesSectionWorkRolesTabEditableOrNonEditableFields();
//		}
//
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsTasksAndWorkRolesSection();
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		if(controlsNewUIPage.isControlsTasksAndWorkRolesLoaded()) {
//			verifyingTasksAndWorkRolesSectionLaborCalculatorTabEditableOrNonEditableFields();
//		}
//
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsWorkingHoursCard();
//		verifyUpdateControlsRegularHoursPopupEditableOrNonEditableFields();
//		verifyUpdateControlsHolidayHoursPopupEditableOrNonEditableFields();
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		verifyUpdateControlsRegularHoursPopupEditableOrNonEditableFields();
//		verifyUpdateControlsHolidayHoursPopupEditableOrNonEditableFields();
//	}
//
//
//	private void verifyLocationInformationEditModeFieldsEditableOrNot() throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		if(controlsNewUIPage.isControlsConsoleMenuAvailable()) {
//			// Validating Location Profile Section
//			controlsNewUIPage.clickOnControlsConsoleMenu();
//			SimpleUtils.assertOnFail("Controls Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , true);
//			controlsNewUIPage.clickOnControlsLocationProfileSection();
//			HashMap<String,ArrayList<String>> locationInfoEditableOrNonEditableFields = controlsNewUIPage
//					.getLocationInformationEditableOrNonEditableFields();
//
//			int valuesMaxCount = locationInfoEditableOrNonEditableFields.get("editableFields").size();
//			if(locationInfoEditableOrNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//				valuesMaxCount = locationInfoEditableOrNonEditableFields.get("nonEditableFields").size();
//			if(valuesMaxCount > 0) {
//
//				String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(locationInfoEditableOrNonEditableFields);
//				SimpleUtils.pass("Location Profile: Edit Location Information Input fields Editable or Non Editable details.<br>"
//						+editableOrNonEditableFieldsValueTable);
//			}
//		}
//		else
//			SimpleUtils.report("Controls menu not available for active user.");
//	}
//
//	private void verifySchedulingPoliciesAllSectionsFieldsEditableOrNot() throws Exception {
//		verifySchedulingPoliciesSchedulesSectionFieldsEditableOrNot();
//		verifySchedulingPoliciesShiftsSectionFieldsEditableOrNot();
//		verifySchedulingPoliciesBudgetSectionFieldsEditableOrNot();
//		verifySchedulingPoliciesTeamAvailabilityManagementSectionFieldsEditableOrNot();
//		verifySchedulingPoliciesTimeOffSectionFieldsEditableOrNot();
//		// Verifying Scheduling Policy Groups Fields
//		verifySchedulingPoliciesSchedulingPolicyGroupsFieldsEditableorNot(ControlsNewUITest.schedulingPolicyGroupsTabs.FullTimeSalariedExempt.getValue());
//		verifySchedulingPoliciesSchedulingPolicyGroupsFieldsEditableorNot(ControlsNewUITest.schedulingPolicyGroupsTabs.FullTimeSalariedNonExempt.getValue());
//		verifySchedulingPoliciesSchedulingPolicyGroupsFieldsEditableorNot(ControlsNewUITest.schedulingPolicyGroupsTabs.FullTimeHourlyNonExempt.getValue());
//		verifySchedulingPoliciesSchedulingPolicyGroupsFieldsEditableorNot(ControlsNewUITest.schedulingPolicyGroupsTabs.PartTimeHourlyNonExempt.getValue());
//	}
//
//	private void verifySchedulingPoliciesSchedulesSectionFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnSchedulingPoliciesSchedulesAdvanceBtn();
//		Thread.sleep(2000);
//		HashMap<String,ArrayList<String>> schedulingPoliciesSchedulesEditableNonEditableFields = controlsNewUIPage
//				.getSchedulingPoliciesSchedulesSectionEditableOrNonEditableFields();
//
//		int valuesMaxCount = schedulingPoliciesSchedulesEditableNonEditableFields.get("editableFields").size();
//		if(schedulingPoliciesSchedulesEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = schedulingPoliciesSchedulesEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(schedulingPoliciesSchedulesEditableNonEditableFields);
//			SimpleUtils.pass("Scheduling Policies: 'Schedules' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//
//	}
//
//	private void verifySchedulingPoliciesShiftsSectionFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnSchedulingPoliciesShiftAdvanceBtn();
//		Thread.sleep(2000);
//		HashMap<String,ArrayList<String>> schedulingPoliciesShiftsEditableNonEditableFields = controlsNewUIPage
//				.getSchedulingPoliciesShiftsSectionEditableOrNonEditableFields();
//
//		int valuesMaxCount = schedulingPoliciesShiftsEditableNonEditableFields.get("editableFields").size();
//		if(schedulingPoliciesShiftsEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = schedulingPoliciesShiftsEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(schedulingPoliciesShiftsEditableNonEditableFields);
//			SimpleUtils.pass("Scheduling Policies: 'Shifts' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//	}
//
//	private void verifySchedulingPoliciesBudgetSectionFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		Thread.sleep(2000);
//		HashMap<String,ArrayList<String>> schedulingPoliciesBudgetEditableNonEditableFields = controlsNewUIPage
//				.getSchedulingPoliciesBudgetSectionEditableOrNonEditableFields();
//
//		int valuesMaxCount = schedulingPoliciesBudgetEditableNonEditableFields.get("editableFields").size();
//		if(schedulingPoliciesBudgetEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = schedulingPoliciesBudgetEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(schedulingPoliciesBudgetEditableNonEditableFields);
//			SimpleUtils.pass("Scheduling Policies: 'Budget' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//	}
//
//	private void verifySchedulingPoliciesTeamAvailabilityManagementSectionFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		Thread.sleep(2000);
//		HashMap<String,ArrayList<String>> schedulingPoliciesTeamAvailabilityManagementEditableNonEditableFields = controlsNewUIPage
//				.getSchedulingPoliciesTeamAvailabilityManagementSectionEditableOrNonEditableFields();
//
//		int valuesMaxCount = schedulingPoliciesTeamAvailabilityManagementEditableNonEditableFields.get("editableFields").size();
//		if(schedulingPoliciesTeamAvailabilityManagementEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = schedulingPoliciesTeamAvailabilityManagementEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(schedulingPoliciesTeamAvailabilityManagementEditableNonEditableFields);
//			SimpleUtils.pass("Scheduling Policies: 'Team Availability Management' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//	}
//
//	private void verifySchedulingPoliciesTimeOffSectionFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		Thread.sleep(2000);
//		controlsNewUIPage.clickOnSchedulingPoliciesTimeOffAdvanceBtn();
//		HashMap<String,ArrayList<String>> schedulingPoliciesTimeOffEditableNonEditableFields = controlsNewUIPage
//				.getSchedulingPoliciesTimeOffSectionEditableOrNonEditableFields();
//
//
//
//		int valuesMaxCount = schedulingPoliciesTimeOffEditableNonEditableFields.get("editableFields").size();
//		if(schedulingPoliciesTimeOffEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = schedulingPoliciesTimeOffEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(schedulingPoliciesTimeOffEditableNonEditableFields);
//			SimpleUtils.pass("Scheduling Policies: 'Time Off' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//	}
//
//	private void verifySchedulingPoliciesSchedulingPolicyGroupsFieldsEditableorNot(String schedulingPolicyGroupsTabsLabel) throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.selectSchdulingPolicyGroupsTabByLabel(schedulingPolicyGroupsTabsLabel);
//		HashMap<String,ArrayList<String>> schedulingPoliciesSchedulingPolicyGroupsEditableNonEditableFields = controlsNewUIPage
//				.getSchedulingPoliciesSchedulingPolicyGroupsSectionEditableOrNonEditableFields();
//
//		int valuesMaxCount = schedulingPoliciesSchedulingPolicyGroupsEditableNonEditableFields.get("editableFields").size();
//		if(schedulingPoliciesSchedulingPolicyGroupsEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = schedulingPoliciesSchedulingPolicyGroupsEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(schedulingPoliciesSchedulingPolicyGroupsEditableNonEditableFields);
//			SimpleUtils.pass("Scheduling Policies: 'Scheduling Policy Groups - "+schedulingPolicyGroupsTabsLabel
//					+"' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//
//	}
//
//	private void verifyingScheduleCollaborationFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnScheduleCollaborationOpenShiftAdvanceBtn();
//		HashMap<String,ArrayList<String>> scheduleCollaborationEditableNonEditableFields = controlsNewUIPage
//				.getScheduleCollaborationEditableOrNonEditableFields();
//
//		int valuesMaxCount = scheduleCollaborationEditableNonEditableFields.get("editableFields").size();
//		if(scheduleCollaborationEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = scheduleCollaborationEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(scheduleCollaborationEditableNonEditableFields);
//			SimpleUtils.pass("Controls Page: 'Schedule Collaboration' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//	}
//
//	private void verifyingComplianceFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String,ArrayList<String>> complianceEditableNonEditableFields = controlsNewUIPage
//				.getComplianceEditableOrNonEditableFields();
//
//		int valuesMaxCount = complianceEditableNonEditableFields.get("editableFields").size();
//		if(complianceEditableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = complianceEditableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(complianceEditableNonEditableFields);
//			SimpleUtils.pass("Controls Page: 'Compliance' Section Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//	}
//
//	private void verifyingUserAndRolesAddNewUserPageFieldsEditableOrNot() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String,ArrayList<String>> complianceEditableNonEditableFields = controlsNewUIPage
//				.getUsersAndRolesAddNewUserPageEditableOrNonEditableFields();
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(complianceEditableNonEditableFields);
//		SimpleUtils.pass("Controls Page: 'User and Roles' Section 'Add New User page' Input fields Editable or Non Editable details.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//	public String getEditableNonEditableFieldsAsHTMLTable(HashMap<String,ArrayList<String>> editableNonEditableFields)
//	{
//		String result = "";
//		int valuesMaxCount = editableNonEditableFields.get("editableFields").size();
//		if(editableNonEditableFields.get("nonEditableFields").size() > valuesMaxCount)
//			valuesMaxCount = editableNonEditableFields.get("nonEditableFields").size();
//		if(valuesMaxCount > 0) {
//			result = "<table><tr><th> Editable Fields Title </th><th>Non Editable Fields Title</th></tr>";
//			for(int index = 0; index < valuesMaxCount; index++) {
//				result = result + "<tr><td>";
//				if(editableNonEditableFields.get("editableFields").size() > index) {
//					result = result + editableNonEditableFields.get("editableFields").get(index);
//				}
//				result = result + "</td><td>";
//				if(editableNonEditableFields.get("nonEditableFields").size() > index) {
//					result = result + editableNonEditableFields.get("nonEditableFields").get(index);
//				}
//				result = result + "</td></tr>";
//			}
//			result = result + "</table>";
//		}
//		return result;
//	}
//
//	private void verifyingUserAndRolesEditUserPageFieldsEditableOrNot(String userFirstName) throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String,ArrayList<String>> complianceEditableNonEditableFields = controlsNewUIPage
//				.getUsersAndRolesEditUserPageEditableOrNonEditableFields(userFirstName);
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(complianceEditableNonEditableFields);
//		SimpleUtils.pass("Controls Page: 'User and Roles' Section 'Add New User page' Input fields Editable or Non Editable details.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//	private void verifyingUserAndRolesUpdateEmployeeJobTitleEditableOrNonEditableFields(String employeeJobTitle) throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String, ArrayList<String>> employeeJobTitleEditableOrNonEditableFields = controlsNewUIPage
//				.getUsersAndRolesUpdateEmployeeJobTitleEditableOrNonEditableFields(employeeJobTitle);
//
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(employeeJobTitleEditableOrNonEditableFields);
//		SimpleUtils.pass("Controls Page: 'User and Roles' Section 'Update Employee Job Title' Input fields Editable or Non Editable details.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//	private void verifyingUserAndRolesCreatNewEmployeeJobTitleEditableOrNonEditableFields(String employeeJobTitle, String newEmployeeJobTitleRole) throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String, ArrayList<String>> employeeJobTitleEditableOrNonEditableFields = controlsNewUIPage
//				.getUsersAndRolesCreateNewEmployeeJobTitleEditableOrNonEditableFields(employeeJobTitle, newEmployeeJobTitleRole);
//
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(employeeJobTitleEditableOrNonEditableFields);
//		SimpleUtils.pass("Controls Page: 'User and Roles' Section 'New Employee Job Title' Input fields Editable or Non Editable details.<br>"
//				+editableOrNonEditableFieldsValueTable);
//
//	}
//
//	private void verifyingUserAndRolesUpdateBadgesEditableOrNonEditableFields(String badgeLabel) throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String, ArrayList<String>> updateBadgeEditableOrNonEditableFields = controlsNewUIPage
//				.getUsersAndRolesUpdateBadgesEditableOrNonEditableFields(badgeLabel);
//
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(updateBadgeEditableOrNonEditableFields);
//		SimpleUtils.pass("Controls Page: 'User and Roles' Section 'Update Badges Popup' Input fields Editable or Non Editable details.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//	private void verifyingUserAndRolesCreateNewBadgesEditableOrNonEditableFields() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String, ArrayList<String>> newBadgeEditableOrNonEditableFields = controlsNewUIPage
//				.getUsersAndRolesNewBadgeEditableOrNonEditableFields();
//
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(newBadgeEditableOrNonEditableFields);
//		SimpleUtils.pass("Controls Page: 'User and Roles' Section 'New Badge Popup' Input fields Editable or Non Editable details.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//	private void verifyingTasksAndWorkRolesSectionWorkRolesTabEditableOrNonEditableFields()throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		Thread.sleep(1000);
//		controlsNewUIPage.selectTasksAndWorkRolesSubTabByLabel(ControlsNewUITest.tasksAndWorkRolesSubTab.WorkRoles.getValue());
//		List<WebElement> workRolesList = controlsNewUIPage.getTasksAndWorkRolesSectionAllWorkRolesList();
//		SimpleUtils.pass("Tasks and Work Roles Section: '"+workRolesList.size()+"' workroles loaded.");
//		if(workRolesList.size() > 0) {
//			HashMap<String, ArrayList<String>> editWorkRolePropertiesEditableOrNonEditableFields = controlsNewUIPage
//					.getTasksAndWorkRolesEditWorkRolePropertiesEditableOrNonEditableFields();
//
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(editWorkRolePropertiesEditableOrNonEditableFields);
//			SimpleUtils.pass("Controls Page: 'Tasks and Work Roles' Section 'Edit Work Role Properties' Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//		boolean isWorkRoleDetailsSectionExapandebale = controlsNewUIPage.isWorkRoleDetailPageSubSectionsExpandFunctionalityWorking();
//		if(isWorkRoleDetailsSectionExapandebale)
//			SimpleUtils.pass("Tasks and Work Roles Section: 'Work Roles Details' Page Details Sections are Expending successfully");
//
//	}
//
//	private void verifyingTasksAndWorkRolesSectionLaborCalculatorTabEditableOrNonEditableFields() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		Thread.sleep(2000);
//		controlsNewUIPage.selectTasksAndWorkRolesSubTabByLabel(ControlsNewUITest.tasksAndWorkRolesSubTab.LaborCalculator.getValue());
//		Thread.sleep(1000);
//		if(controlsNewUIPage.isLaborCalculationTabLoaded()) {
//			HashMap<String, ArrayList<String>> editWorkRolePropertiesEditableOrNonEditableFields = controlsNewUIPage
//					.getTasksAndWorkRolesLaborCalculatorTabEditableOrNonEditableFields();
//
//			String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(editWorkRolePropertiesEditableOrNonEditableFields);
//			SimpleUtils.pass("Controls Page: 'Tasks and Work Roles' Section 'Edit Work Role Properties' Input fields Editable or Non Editable details.<br>"
//					+editableOrNonEditableFieldsValueTable);
//		}
//		else
//			SimpleUtils.fail("Tasks and Work Roles Section: 'Labor Calculation' Tab not loaded.", true);
//	}
//
//	private void verifyingTasksAndWorkRolesAddWorkRolePageEditableOrNonEditableFields() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		Thread.sleep(2000);
//		HashMap<String, ArrayList<String>> tasksAndWorkRolesAddWorkRolePageEditableOrNonEditableFields = controlsNewUIPage
//				.getTasksAndWorkRolesAddWorkRolePageEditableOrNonEditableFields();
//
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(tasksAndWorkRolesAddWorkRolePageEditableOrNonEditableFields);
//		SimpleUtils.pass("Controls Page: 'Tasks and Work Roles' Section 'Add Work Role' page Input fields Editable or Non Editable details.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//	private void verifyUpdateControlsRegularHoursPopupEditableOrNonEditableFields() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String, ArrayList<String>> regularHoursPopupEditableOrNonEditableFields = controlsNewUIPage.verifyUpdateControlsRegularHoursPopupEditableOrNonEditableFields();
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(regularHoursPopupEditableOrNonEditableFields);
//		SimpleUtils.pass("Controls Working Hours Section: Regular Hours 'Edit' popup Editable or Not Editable fields.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//	private void verifyUpdateControlsHolidayHoursPopupEditableOrNonEditableFields() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		HashMap<String, ArrayList<String>> holidayHoursPopupEditableOrNonEditableFields = controlsNewUIPage.verifyUpdateControlsHolidayHoursPopupEditableOrNonEditableFields();
//		String editableOrNonEditableFieldsValueTable = getEditableNonEditableFieldsAsHTMLTable(holidayHoursPopupEditableOrNonEditableFields);
//		SimpleUtils.pass("Controls Working Hours Section: Holiday Hours 'Edit' popup Editable or Not Editable fields.<br>"
//				+editableOrNonEditableFieldsValueTable);
//	}
//
//
//	//added by Nishant
//
//	@MobilePlatform(platform = "Android")
//	@UseAsTestRailSectionId(testRailSectionId = 361)
//	@UseAsTestCaseSectionId(testCaseSectionId = 288)
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the options available when Global selected")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void verifyOptionWhenGlobalSelectedAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		String fileName = "UsersCredentials.json";
//		fileName=SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//		HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//		Object[][] teamLeadCredentials = userCredentials.get("TeamLead");
//		Object[][] teamMemberCredentials = userCredentials.get("TeamMember");
//		Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//		LoginPage loginPage = pageFactory.createConsoleLoginPage();
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Internal Admin'</b>.");
//		SimpleUtils.assertOnFail("Control Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		// Validate Controls Company Profile Section
//		boolean isCompanyProfileSection = controlsNewUIPage.isControlsCompanyProfileCard();
//		SimpleUtils.assertOnFail("Controls Page: Company Profile Section not Loaded.", isCompanyProfileSection, true);
//		verifyControlsAllSection();
//		loginPage.logOut();
//
//		/*
//		 * Login as Store Manager
//		 */
//		loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1]),
//				String.valueOf(storeManagerCredentials[0][2]));
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Store Manager'</b>.");
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Control Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		verifyControlsAllSection();
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate functioning of Controls option available")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateSchedulingPoliciesControlOptionAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		String fileName = "UsersCredentials.json";
//		fileName=SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//		HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//		Object[][] teamLeadCredentials = userCredentials.get("TeamLead");
//		Object[][] teamMemberCredentials = userCredentials.get("TeamMember");
//		Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//		LoginPage loginPage = pageFactory.createConsoleLoginPage();
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		verifyControlBreadcrumbForSchedulingPolicies();
//		loginPage.logOut();
//
//		/*
//		 * Login as Store Manager
//		 */
//		loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1]),
//				String.valueOf(storeManagerCredentials[0][2]));
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Store Manager'</b>.");
//		verifyControlBreadcrumbForSchedulingPolicies();
//	}
//
//	@MobilePlatform(platform = "Android")
//	@UseAsTestRailSectionId(testRailSectionId = 377)
//	@UseAsTestCaseSectionId(testCaseSectionId = 295)
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the Schedule Publish Window")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateSchedulePublishWindowAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		String fileName = "UsersCredentials.json";
//		fileName=SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//		HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//		Object[][] teamLeadCredentials = userCredentials.get("TeamLead");
//		Object[][] teamMemberCredentials = userCredentials.get("TeamMember");
//		Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//		LoginPage loginPage = pageFactory.createConsoleLoginPage();
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		String publishWindowAdvanceWeeks = schedulingPoliciesData.get("Schedule_Publish_Window");
//		String publishWindowQuestion = schedulingPoliciesData.get("Schedule_Publish_Window_Question");
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Control Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnControlsSchedulingPolicies();
//		boolean isSchedulingPolicies = controlsNewUIPage.isControlsSchedulingPoliciesLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Scheduling Policies Section not Loaded.", isSchedulingPolicies, true);
//		controlsNewUIPage.verifySchedulePublishWindow(publishWindowAdvanceWeeks, publishWindowQuestion,"InternalAdmin");
//		String selectedOptionLabel = controlsNewUIPage.getSchedulePublishWindowWeeks();
//		controlsNewUIPage.getSchedulePublishWindowWeeksDropDownValues();
//		controlsNewUIPage.updateSchedulePublishWindow(publishWindowAdvanceWeeks,false,false);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		controlsNewUIPage.verifySchedulePublishWindow(publishWindowAdvanceWeeks, publishWindowQuestion,"InternalAdmin");
//		controlsNewUIPage.updateSchedulePublishWindow(publishWindowAdvanceWeeks,true,false);
//		List<String> selectionOptionLabelAfterUpdation = controlsNewUIPage.getSchedulePublishWindowValueAtDifferentLocations(true);
//		controlsNewUIPage.verifySchedulePublishWindowUpdationValues(publishWindowAdvanceWeeks, selectionOptionLabelAfterUpdation);
//		loginPage.logOut();
//
//		/*
//		 * Login as Store Manager
//		 */
//		loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1]),
//				String.valueOf(storeManagerCredentials[0][2]));
//		SimpleUtils.pass("<b>Legion Application User logged in as role 'Store Manager'</b>.");
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Control Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnControlsSchedulingPolicies();
//		boolean isSchedulingPolicies1 = controlsNewUIPage.isControlsSchedulingPoliciesLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Scheduling Policies Section not Loaded.", isSchedulingPolicies1, true);
//		controlsNewUIPage.verifySchedulePublishWindow(publishWindowAdvanceWeeks, publishWindowQuestion,"StoreManager");
//	}
//
//	@MobilePlatform(platform = "Android")
//	@UseAsTestRailSectionId(testRailSectionId = 377)
//	@UseAsTestCaseSectionId(testCaseSectionId = 296)
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the Schedule Creation Window")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void validateSchedulePlanningWindowAsInternalAdmin(String browser, String username, String password, String location)
//			throws Exception {
//
//		String fileName = "UsersCredentials.json";
//		fileName=SimpleUtils.getEnterprise("KendraScott2_Enterprise")+fileName;
//		HashMap<String, Object[][]> userCredentials = SimpleUtils.getEnvironmentBasedUserCredentialsFromJson(fileName);
//		Object[][] teamLeadCredentials = userCredentials.get("TeamLead");
//		Object[][] teamMemberCredentials = userCredentials.get("TeamMember");
//		Object[][] storeManagerCredentials = userCredentials.get("StoreManager");
//		LoginPage loginPage = pageFactory.createConsoleLoginPage();
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		String planningWindowAdvanceWeeks = schedulingPoliciesData.get("Schedule_Planning_Window");
//		String planningWindowQuestion = schedulingPoliciesData.get("Schedule_Planning_Window_Question");
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		SimpleUtils.assertOnFail("Control Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//		controlsNewUIPage.clickOnControlsSchedulingPolicies();
//		boolean isSchedulingPolicies = controlsNewUIPage.isControlsSchedulingPoliciesLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Scheduling Policies Section not Loaded.", isSchedulingPolicies, true);
//		controlsNewUIPage.verifySchedulePlanningWindow(planningWindowAdvanceWeeks, planningWindowQuestion,"InternalAdmin");
//		String selectedOptionLabel = controlsNewUIPage.getSchedulePlanningWindowWeeks();
//		controlsNewUIPage.getSchedulePlanningWindowWeeksDropDownValues();
//		controlsNewUIPage.updateSchedulePlanningWindow(planningWindowAdvanceWeeks,false,false);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		controlsNewUIPage.verifySchedulePlanningWindow(planningWindowAdvanceWeeks, planningWindowQuestion,"InternalAdmin");
//		controlsNewUIPage.updateSchedulePlanningWindow(planningWindowAdvanceWeeks,false,true);
//		List<String> selectionOptionLabelAfterUpdation = controlsNewUIPage.getSchedulePublishWindowValueAtDifferentLocations(false);
//		controlsNewUIPage.verifySchedulePlanningWindowUpdationValues(planningWindowAdvanceWeeks, selectionOptionLabelAfterUpdation);
////		loginPage.logOut();
////
////		/*
////		 * Login as Store Manager
////		 */
////		loginToLegionAndVerifyIsLoginDone(String.valueOf(storeManagerCredentials[0][0]), String.valueOf(storeManagerCredentials[0][1]),
////				String.valueOf(storeManagerCredentials[0][2]));
////		SimpleUtils.pass("<b>Legion Application User logged in as role 'Store Manager'</b>.");
////		controlsNewUIPage.clickOnControlsConsoleMenu();
////		SimpleUtils.assertOnFail("Control Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
////		controlsNewUIPage.clickOnControlsSchedulingPolicies();
////		boolean isSchedulingPolicies1 = controlsNewUIPage.isControlsSchedulingPoliciesLoaded();
////		SimpleUtils.assertOnFail("Controls Page: Scheduling Policies Section not Loaded.", isSchedulingPolicies1, true);
////		controlsNewUIPage.verifySchedulePlanningWindow(planningWindowAdvanceWeeks, planningWindowAdvanceWeeks,"StoreManager");
//	}
//
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Verify Sum of all the shifts in a week compared to Schedule Hours shown in First Smart card of Schedule and Daily Team members count")
//	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass = CredentialDataProviderSource.class)
//	public void generateScheduleAndCheckScheduleNTMSizeWeekView(String username, String password, String browser, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		List<String> overviewPageScheduledWeekStatus = scheduleOverviewPage.getScheduleWeeksStatus();
//		scheduleCommonPage.clickOnScheduleSubTab(LoginTest.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//		List<WebElement> overviewPageScheduledWeeks = scheduleOverviewPage.getOverviewScheduleWeeks();
//		for (int i = 0; i < overviewPageScheduledWeeks.size(); i++) {
//			if (overviewPageScheduledWeeks.get(i).getText().toLowerCase().contains(ScheduleTestKendraScott2.overviewWeeksStatus.Guidance.getValue().toLowerCase())) {
//				scheduleOverviewPage.clickOnGuidanceBtnOnOverview(i);
//				if (createSchedulePage.isGenerateButtonLoaded()) {
//					SimpleUtils.pass("Guidance week found : '" + scheduleCommonPage.getActiveWeekText() + "'");
//					createSchedulePage.generateOrUpdateAndGenerateSchedule();
//					scheduleShiftTablePage.verifyScheduledHourNTMCountIsCorrect();
//					break;
//				}
//			}
//		}
//
//	}
//
//	@MobilePlatform(platform = "Android")
//	@UseAsTestRailSectionId(testRailSectionId = 391)
//	@UseAsTestCaseSectionId(testCaseSectionId = 383)
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate Add shift functionality for Assign TM")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void editAssignTeamScheduleAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		scheduleCommonPage.clickOnDayView();
//		int previousGutterCount = schedulePage.getgutterSize();
//		scheduleNavigationTest(previousGutterCount);
//		HashMap<String, Float> ScheduledHours = schedulePage.getScheduleLabelHours();
//		Float scheduledHoursBeforeEditing = ScheduledHours.get("scheduledHours");
//		HashMap<List<String>, List<String>> teamCount = schedulePage.calculateTeamCount();
//		SimpleUtils.assertOnFail("User can add new shift for past week",
//				(scheduleMainPage.isAddNewDayViewShiftButtonLoaded()), true);
//		String textStartDay = schedulePage.clickNewDayViewShiftButtonLoaded();
//		newShiftPage.customizeNewShiftPage();
//		schedulePage.compareCustomizeStartDay(textStartDay);
//		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//		HashMap<String, String> shiftTimeSchedule = schedulePage.calculateHourDifference();
//		newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		newShiftPage.customizeNewShiftPage();
//		newShiftPage.verifySelectTeamMembersOption();
//		newShiftPage.clickOnOfferOrAssignBtn();
//		int updatedGutterCount = schedulePage.getgutterSize();
//		List<String> previousTeamCount = schedulePage.calculatePreviousTeamCount(shiftTimeSchedule, teamCount);
//		List<String> currentTeamCount = schedulePage.calculateCurrentTeamCount(shiftTimeSchedule);
//		verifyTeamCount(previousTeamCount, currentTeamCount);
//		scheduleMainPage.clickSaveBtn();
//		HashMap<String, Float> editScheduledHours = schedulePage.getScheduleLabelHours();
//		Float scheduledHoursAfterEditing = editScheduledHours.get("scheduledHours");
//		verifyScheduleLabelHours(shiftTimeSchedule.get("ScheduleHrDifference"), scheduledHoursBeforeEditing, scheduledHoursAfterEditing);
//
//	}
//
//	@Automated(automated = "Automated")
//	@Owner(owner = "Nishant")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "TP-18: As a store manager, should be able to review past week's schedule and generate this week or next week's schedule")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
//	public void editOpenShiftScheduleAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		//The schedules that are already published should remain unchanged
//		scheduleCommonPage.clickOnDayView();
//		int previousGutterCount = schedulePage.getgutterSize();
//		scheduleNavigationTest(previousGutterCount);
//		HashMap<String, Float> ScheduledHours = schedulePage.getScheduleLabelHours();
//		Float scheduledHoursBeforeEditing = ScheduledHours.get("scheduledHours");
//		HashMap<List<String>, List<String>> teamCount = schedulePage.calculateTeamCount();
//		SimpleUtils.assertOnFail("User can add new shift for past week", (scheduleMainPage.isAddNewDayViewShiftButtonLoaded()), true);
//		String textStartDay = schedulePage.clickNewDayViewShiftButtonLoaded();
//		newShiftPage.customizeNewShiftPage();
//		schedulePage.compareCustomizeStartDay(textStartDay);
//		newShiftPage.moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
//		newShiftPage.moveSliderAtSomePoint(propertyCustomizeMap.get("INCREASE_START_TIME"), ScheduleTestKendraScott2.sliderShiftCount.SliderShiftStartCount.getValue(), ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
//		HashMap<String, String> shiftTimeSchedule = schedulePage.calculateHourDifference();
//		newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		int updatedGutterCount = schedulePage.getgutterSize();
//		List<String> previousTeamCount = schedulePage.calculatePreviousTeamCount(shiftTimeSchedule, teamCount);
//		List<String> currentTeamCount = schedulePage.calculateCurrentTeamCount(shiftTimeSchedule);
//		verifyTeamCount(previousTeamCount, currentTeamCount);
//		scheduleMainPage.clickSaveBtn();
//		HashMap<String, Float> editScheduledHours = schedulePage.getScheduleLabelHours();
//		Float scheduledHoursAfterEditing = editScheduledHours.get("scheduledHours");
//		verifyScheduleLabelHours(shiftTimeSchedule.get("ScheduleHrDifference"), scheduledHoursBeforeEditing, scheduledHoursAfterEditing);
//
//	}
//
//	@MobilePlatform(platform = "Android")
//	@UseAsTestRailId(testRailId = 32)
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Should be able to view Day View and filter Schedule and Group By All")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void viewAndFilterScheduleWithGroupByAllDayViewAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//		//SimpleUtils.fail("Test Failed", false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);
//
//		/*
//		 *  Navigate to Schedule Day view
//		 */
//		boolean isWeekView = false;
//		scheduleCommonPage.clickOnDayView();
//		scheduleCommonPage.navigateToNextDayIfStoreClosedForActiveDay();
//		scheduleMainPage.selectGroupByFilter(ScheduleNewUITest.scheduleGroupByFilterOptions.groupbyAll.getValue());
//		scheduleMainPage.filterScheduleByWorkRoleAndShiftType(isWeekView);
//		scheduleMainPage.clickOnEditButton();
//		scheduleMainPage.filterScheduleByWorkRoleAndShiftType(isWeekView);
//		scheduleMainPage.clickOnCancelButtonOnEditMode();
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Should be able to view Day View and filter Schedule and Group By Work Role")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void viewAndFilterScheduleWithGroupByWorkRoleDayViewAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);
//
//		/*
//		 *  Navigate to Schedule Day view
//		 */
//		boolean isWeekView = false;
//		scheduleCommonPage.clickOnDayView();
//		scheduleMainPage.selectGroupByFilter(ScheduleNewUITest.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
//		scheduleMainPage.filterScheduleByWorkRoleAndShiftType(isWeekView);
//		scheduleMainPage.clickOnEditButton();
//		scheduleMainPage.filterScheduleByWorkRoleAndShiftType(isWeekView);
//		scheduleMainPage.clickOnCancelButtonOnEditMode();
//	}
//
//	@MobilePlatform(platform = "Android")
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Naval")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Should be able to view Day View and filter Schedule and Group By TMs")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void viewAndFilterScheduleWithGroupByTMsDayViewAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		SimpleUtils.assertOnFail("'Schedule' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()) , true);
//
//		/*
//		 *  Navigate to Schedule Day view
//		 */
//		boolean isWeekView = false;
//		scheduleCommonPage.clickOnWeekView();
//		scheduleMainPage.selectGroupByFilter(ScheduleNewUITest.scheduleGroupByFilterOptions.groupbyTM.getValue());
//		scheduleCommonPage.clickOnDayView();
//		scheduleMainPage.filterScheduleByWorkRoleAndShiftType(isWeekView);
//		scheduleMainPage.clickOnEditButton();
//		scheduleMainPage.filterScheduleByWorkRoleAndShiftType(isWeekView);
//		scheduleMainPage.clickOnCancelButtonOnEditMode();
//	}
//
//
//	@MobilePlatform(platform = "Android")
//	@UseAsTestRailSectionId(testRailSectionId = 381)
//	@UseAsTestCaseSectionId(testCaseSectionId = 380)
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate edit schedule functionality")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void editScheduleHoursAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//		if(!isActiveWeekGenerated){
//			createSchedulePage.generateOrUpdateAndGenerateSchedule();
//		}
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
////		List<String> previousTeamCount = schedulePage.calculatePreviousTeamCount(shiftTimeSchedule,teamCount);
////		List<String> currentTeamCount = schedulePage.calculateCurrentTeamCount(shiftTimeSchedule);
////		verifyTeamCount(previousTeamCount,currentTeamCount);
////		scheduleMainPage.clickSaveBtn();
////		HashMap<String, Float> editScheduledHours = schedulePage.getScheduleLabelHours();
////		Float scheduledHoursAfterEditing = editScheduledHours.get("scheduledHours");
////		verifyScheduleLabelHours(shiftTimeSchedule.get("ScheduleHrDifference"), scheduledHoursBeforeEditing, scheduledHoursAfterEditing);
//	}
//	//added by Nishant
//
//	@Automated(automated =  "Automated")
//	@Owner(owner = "Nishant")
//	@SanitySuite(sanity =  "Sanity")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate the drag an drop functionality for swapping shift")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void dragAndDropSwapShiftAsStoreManager(String browser, String username, String password, String location)
//			throws Exception {
//		int overviewTotalWeekCount = Integer.parseInt(propertyMap.get("scheduleWeekCount"));
////	    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"),propertyMap.get("DEFAULT_PASSWORD"));
//		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
//		CreateSchedulePage createSchedulePage = pageFactory.createCreateSchedulePage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);
//
//		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
//		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue());
//		SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , true);
//		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue());
//		boolean isActiveWeekGenerated = createSchedulePage.isWeekGenerated();
//		if(!isActiveWeekGenerated){
//			createSchedulePage.generateOrUpdateAndGenerateSchedule();
//		}
//		//The schedules that are already published should remain unchanged
//		boolean isStoreClosed = false;
//		scheduleCommonPage.navigateToNextDayIfStoreClosedForActiveDay();
//		scheduleMainPage.clickOnEditButton();
//
//	}
//
//
//	public void verifyControlBreadcrumbForSchedulingPolicies() throws Exception {
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		controlsNewUIPage.clickOnControlsConsoleMenu();
//		controlsNewUIPage.clickOnControlsSchedulingPolicies();
//		boolean isSchedulingPolicies = controlsNewUIPage.isControlsSchedulingPoliciesLoaded();
//		SimpleUtils.assertOnFail("Controls Page: Scheduling Policies Section not Loaded.", isSchedulingPolicies, true);
//		controlsNewUIPage.clickOnGlobalLocationButton();
//		controlsNewUIPage.verifyAllLocations(schedulingPoliciesData.get("ALL_LOCATIONS"));
//		controlsNewUIPage.verifySearchLocations(schedulingPoliciesData.get("SEARCH_LOCATION_TEXT"));
//	}
//
//
//
//	public void verifyControlsAllSection() throws Exception{
//		// Validate Controls Scheduling Profile Section
//		ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
//		boolean isSchedulingPoliciesSection = controlsNewUIPage.isControlsSchedulingPoliciesCard();
//		SimpleUtils.assertOnFail("Controls Page: Scheduling Profile Section not Loaded.", isSchedulingPoliciesSection, true);
//
//		// Validate Controls Scheduling Collaboration Section
//		boolean isSchedulingCollaborationSection = controlsNewUIPage.isControlsSchedulingCollaborationCard();
//		SimpleUtils.assertOnFail("Controls Page: Scheduling Collaboration Section not Loaded.", isSchedulingCollaborationSection, true);
//
//		// Validate Controls Compliance Section
//		boolean isControlsComplianceCardSection = controlsNewUIPage.isControlsComplianceCard();
//		SimpleUtils.assertOnFail("Controls Page: Compliance Section not Loaded.", isControlsComplianceCardSection, true);
//
//		// Validate Controls User And Role Section
//		boolean isUsersAndRolesSection = controlsNewUIPage.isControlsUsersAndRolesCard();
//		SimpleUtils.assertOnFail("Controls Page: User And Role Section not Loaded.", isUsersAndRolesSection, true);
//
//		// Validate Controls Task And Work Roles Section
//		boolean isTaskAndWorkRolesSection = controlsNewUIPage.isControlsTaskAndWorkRolesCard();
//		SimpleUtils.assertOnFail("Controls Page: Task And Work Roles Section not Loaded.", isTaskAndWorkRolesSection, true);
//
//		// Validate Controls Company Profile Section
//		boolean isWorkingHoursSection = controlsNewUIPage.isControlsWorkingHoursCard();
//		SimpleUtils.assertOnFail("Controls Page: Working Hours Section not Loaded.", isWorkingHoursSection, true);
//
//		// Validate Controls Location Section
//		boolean isLocationSection = controlsNewUIPage.isControlsLocationsCard();
//		SimpleUtils.assertOnFail("Controls Page: Location Section not Loaded.", isLocationSection, true);
//	}
//
//
//	public void navigateToControlsSchedulingPolicies(ControlsNewUIPage controlsNewUIPage)
//	{
//		try {
//			controlsNewUIPage.clickOnControlsConsoleMenu();
//			SimpleUtils.assertOnFail("TimeSheet Page not loaded Successfully!",controlsNewUIPage.isControlsPageLoaded() , false);
//			controlsNewUIPage.clickOnGlobalLocationButton();
//			controlsNewUIPage.clickOnControlsSchedulingPolicies();
//		} catch (Exception e) {
//			SimpleUtils.fail(e.getMessage(), false);
//		}
//	}
//
//
//	public void validateAssignTeamMemberPageAndSaveSchedule() throws Exception{
//		NewShiftPage newShiftPage = pageFactory.createNewShiftPage();
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		newShiftPage.selectWorkRole(scheduleWorkRoles.get("WorkRole"));
//		newShiftPage.clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
//		newShiftPage.clickOnCreateOrNextBtn();
//		newShiftPage.customizeNewShiftPage();
//		schedulePage.selectTeamMembersOptionForScheduleForClopening();
//		newShiftPage.clickOnOfferOrAssignBtn();
//		scheduleMainPage.clickSaveBtn();
//	}
//
//
//}
