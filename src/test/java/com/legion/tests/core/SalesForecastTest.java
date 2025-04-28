package com.legion.tests.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.legion.pages.ScheduleCommonPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.legion.pages.DashboardPage;
import com.legion.pages.SchedulePage;
import com.legion.pages.SalesForecastPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;

public class SalesForecastTest extends TestBase{

	public enum SalesForecastForecastCalenderWeekCount{
		ZERO(0),
		One(1),
		Two(2),
		Three(3);
		private final int value;
		SalesForecastForecastCalenderWeekCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
	}
	
	//private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
	private static HashMap<String, String> salesForecastCategoriesOptions = JsonUtil.getPropertiesFromJsonFile("src/test/resources/salesForecastCategoriesOptions.json");
	@Override
	  @BeforeMethod()
	  public void firstTest(Method testMethod, Object[] params) throws Exception{
		  this.createDriver((String)params[0],"69","Window");
	      visitPage(testMethod);
	      loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
	  }
	SalesForecastPage schedulePage = null;
	// To be updated https://legiontech.atlassian.net/browse/LEG-5293 for automation we need to go 2 weeks back for actuals(i.e. the week that has complete actuals data)
	@Automated(automated ="Automated")
	@Owner(owner = "Naval")
	@TestName(description = "LEG-2422: As a store manager, can view Projected Sales Forecast data for past and current week")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void salesForecastDataAsStoreManagerTest(String username, String password, String browser, String location)
            throws Exception
    {
    	//To Do Should be separate Test from Schedule test
//    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        dashboardPage.goToToday();
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        SimpleUtils.assertOnFail( "Schedule Page not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()),false);
        SalesForecastPage salesForecastPage = pageFactory.createSalesForecastPage();  
        salesForecastPage.navigateToSalesForecastTab();
        SimpleUtils.assertOnFail( "Projected Sales Tab not Active!",salesForecastPage.isSalesForecastTabActive() ,false);
        

//         * Schedule Projected Sales as Week View

        salesForecastPage.navigateToSalesForecastTabWeekView();
        SimpleUtils.assertOnFail( "Projected Sales Forecast Tab Week View not loaded successfully!",salesForecastPage.isSalesForecastTabWeekViewActive() ,false);
        SimpleUtils.assertOnFail( "Projected Sales Item Options/Categories With User Job Title not matched!",salesForecastPage.validateSalesForecastItemOptionWithUserJobTitle(salesForecastCategoriesOptions.get("Manager")) ,true);
/*        //pass("Shedule page Projected Sales Item Option/Categories With User Job Title matched!");
        HashMap<String, String> dayMonthDateFormatForCurrentPastAndFutureWeek = SimpleUtils.getDayMonthDateFormatForCurrentPastAndFutureWeek(SimpleUtils.getCurrentDateDayOfYear(), SimpleUtils.getCurrentISOYear());
        String currentWeekDate = (String)dayMonthDateFormatForCurrentPastAndFutureWeek.get("currentWeekDate");
        String pastWeekDate = (String)dayMonthDateFormatForCurrentPastAndFutureWeek.get("pastWeekDate");
        String futureWeekDate = (String)dayMonthDateFormatForCurrentPastAndFutureWeek.get("futureWeekDate");
        SimpleUtils.assertOnFail( "Map return currentWeekDate as 'null'!",(currentWeekDate != null) ,false);
        SimpleUtils.assertOnFail( "Map return pastWeekDate as 'null'!",(pastWeekDate != null) ,true);
        SimpleUtils.assertOnFail( "Map return futureWeekDate as 'null'!",(futureWeekDate != null) ,true);*/
        

         //* Projected Sales forecast for current week

		Map<String, String> currentWeekSalesForecastCardsData =  salesForecastPage.getSalesForecastForeCastData();
        salesForecastWeeksViewForeCastData(currentWeekSalesForecastCardsData, "Current Week");
       
        

       // Projected Sales forecast for Past 2nd Week

        salesForecastPage.navigateSalesForecastWeekViewTpPastOrFuture("Previous Week", SalesForecastForecastCalenderWeekCount.Two.getValue());
        Map<String, String> previousWeekSalesForecastCardsData =  salesForecastPage.getSalesForecastForeCastData();
        salesForecastWeeksViewForeCastData(previousWeekSalesForecastCardsData, "Previous Week");
        
        

         // Projected Sales forecast for Future week

        salesForecastPage.navigateSalesForecastWeekViewTpPastOrFuture("Future Week", SalesForecastForecastCalenderWeekCount.Three.getValue());
        Map<String, String> futureWeekSalesForecastCardsData =  salesForecastPage.getSalesForecastForeCastData();
        salesForecastWeeksViewForeCastData(futureWeekSalesForecastCardsData, "Future Week");
        
    }
	
	
	
	private void salesForecastWeeksViewForeCastData(Map<String, String> WeekSalesForecastCardsData, String weekType) {
		
    	String peakDemandProjected = (String)WeekSalesForecastCardsData.get("peakDemandProjected");
    	String peakDemandActual = (String)WeekSalesForecastCardsData.get("peakDemandActual");
    	String totalDemandProjected = (String)WeekSalesForecastCardsData.get("totalDemandProjected");
    	String totalDemandActual = (String)WeekSalesForecastCardsData.get("totalDemandActual");
    	String peakTimeProjected = (String)WeekSalesForecastCardsData.get("peakTimeProjected");
    	String peakTimeActual = (String)WeekSalesForecastCardsData.get("peakTimeActual");
    	
    	/*
         * Fail on Projected & Actual values are null
         */
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Demand Projected is'null'!",(peakDemandProjected != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Demand Actual is'null'!",(peakDemandActual != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Total Demand Projected is'null'!",(totalDemandProjected != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Total Demand Actual is'null'!",(totalDemandActual != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Time Projected is'null'!",(peakTimeProjected != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Time Actual is'null'!",(peakTimeActual != null) ,true);
        /*
         * fail on "N/A" value of Actuals on Past & Current Week
         */
        if(!weekType.toLowerCase().contains("future") && !weekType.toLowerCase().contains("current"))
        {
        	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Demand Actual is 'N/A'!",(! peakDemandActual.contains("N/A")),true);
        	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Total Demand Actual is 'N/A'!",(! totalDemandActual.contains("N/A")) ,true);
        	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Time Actual is 'N/A'!",(! peakTimeActual.contains("N/A")) ,true);
        }
        
        
        /*
         *  Logging Projected Sales forecast Data card values
         */
        
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Demand Projected - "+peakDemandProjected );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Demand Actual - "+peakDemandActual );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Total Demand Projected - "+totalDemandProjected);
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Total Demand Actual - "+totalDemandActual );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Time Projected - "+peakTimeProjected );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Time Actual - "+peakTimeActual );
		
	}
	
	
	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee2_Enterprise")
	@TestName(description = "TP-44: Coffee Cups in All Sales Item filter is not showing any data")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void shouldAllSalesItemDisplayEnabledFilter(String username, String password, String browser, String location)
            throws Exception
    {
		SimpleUtils.pass("Login as Store Manager Successfully");
		SimpleUtils.pass("Successfully opened the Schedule app");
		SimpleUtils.pass("Navigate to previous weeks in Projected Sales");
		SimpleUtils.pass("Look for the Actual's value for Coffee Cups filter");
		SimpleUtils.pass("If Actuals for the Coffee Cups has some value then assert the presence of Projected Sales graph");
    }
	
	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Tech_Enterprise")
	@TestName(description = "LEG-5192: Sales Guidance Graphs are missing for both Day view and Week view in Projected Sales in LegionTech Env (Location-Toronto)")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void salesForecastGraphMissing(String username, String password, String browser, String location)
            throws Exception
    {
		SimpleUtils.pass("Login into Legiontech application successfully");
		SimpleUtils.pass("Change location to Totonto");
		SimpleUtils.pass("Click on Schedule button");
		SimpleUtils.pass("Click on Projected Sales Tab");
		SimpleUtils.pass("assert Sales Guidance Graphs are missing for both Day view and Week view");
    }

	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "LEG-5196: Graphs are not changing in Sales Forecast in day view if user selects any locations from All locations filter")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void salesForecastGraphNotChangingForAllLocationFilter(String username, String password, String browser, String location)
            throws Exception
    {
		SimpleUtils.pass("Login into Legion Coffee application successfully");
		SimpleUtils.pass("Select Location as Bay Area");
		SimpleUtils.pass("Click on Schedule button");
		SimpleUtils.pass("Click on Projected Sales Tab");
		SimpleUtils.pass("Click on Day view and navigate for any week");
		SimpleUtils.pass("Graphs should be changing for each day");
    }

	@Automated(automated = "Manual")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "LEG-5293: Actuals showing as NA Oct17(Wed) onwards in LegionCoffee")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void smartCardActualsShowingAsNAForPastDate(String username, String password, String browser, String location)
            throws Exception
    {
		SimpleUtils.pass("Login into Legion Coffee application successfully");
		SimpleUtils.pass("Select Location as Bay Area");
		SimpleUtils.pass("Click on Schedule button");
		SimpleUtils.pass("Click on Projected Sales Tab");
		SimpleUtils.pass("Click on Day view select Oct17");
		SimpleUtils.pass("Actuals should not have NA there should be some value for past date");
    }






}
