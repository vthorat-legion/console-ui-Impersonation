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
import com.legion.pages.TrafficForecastPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.tests.testframework.ExtentTestManager;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;

public class TrafficForecastTest extends TestBase{

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
	
	public enum forecastTabText{
		  projectedSales("Projected Sales"),
		  projectedTraffic("Projected Traffic");
		  private final String value;
		  forecastTabText(final String newValue) {
			  value = newValue;
	      }
	      public String getValue() { return value; }
	}
	String projectedType = "TRAFFIC";
	
	//private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
	private static HashMap<String, String> salesForecastCategoriesOptions = JsonUtil.getPropertiesFromJsonFile("src/test/resources/salesForecastCategoriesOptions.json");
	@Override
	  @BeforeMethod()
	  public void firstTest(Method testMethod, Object[] params) throws Exception{
		  this.createDriver((String)params[0],"69","Window");
	      visitPage(testMethod);
	      loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
	  }
	// To be updated https://legiontech.atlassian.net/browse/LEG-5293 for automation we need to go 2 weeks back for actuals(i.e. the week that has complete actuals data)
	@Automated(automated ="Automated")
	@Owner(owner = "Naval")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "LEG-2422: As a store manager, can view Projected Sales Forecast data for past and current week")
    @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
    public void trafficForecastDataAsStoreManagerTest(String username, String password, String browser, String location)
            throws Exception
    {
    	//To Do Should be separate Test from Schedule test
//    	loginToLegionAndVerifyIsLoginDone(propertyMap.get("DEFAULT_USERNAME"), propertyMap.get("DEFAULT_PASSWORD"));
        DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
        dashboardPage.goToToday();
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
        SimpleUtils.assertOnFail( "Schedule Page not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Schedule.getValue()),false);
        TrafficForecastPage trafficForecastPage = pageFactory.createTrafficForecastPage(); 
        trafficForecastPage.navigateToSalesForecastTab();
        SimpleUtils.assertOnFail( "Projected Sales Tab not Active!",trafficForecastPage.isSalesForecastTabActive() ,false);
        

//         * Schedule Projected Sales as Week View

        trafficForecastPage.navigateToSalesForecastTabWeekView();
        SimpleUtils.assertOnFail( "Projected Sales Forecast Tab Week View not loaded successfully!",trafficForecastPage.isSalesForecastTabWeekViewActive() ,false);
        //pass("Shedule page Projected Sales Item Option/Categories With User Job Title matched!");
        HashMap<String, String> dayMonthDateFormatForCurrentPastAndFutureWeek = SimpleUtils.getDayMonthDateFormatForCurrentPastAndFutureWeek(SimpleUtils.getCurrentDateDayOfYear(), SimpleUtils.getCurrentISOYear());

//         * Projected Sales forecast for current week
//
		Map<String, String> currentWeekSalesForecastCardsData =  trafficForecastPage.getSalesForecastForeCastData();
        salesForecastWeeksViewForeCastData(currentWeekSalesForecastCardsData, "Current Week");
       
        

        // * Projected Sales forecast for Past week

        trafficForecastPage.navigateSalesForecastWeekViewTpPastOrFuture("Previous Week", SalesForecastForecastCalenderWeekCount.Two.getValue());
        Map<String, String> previousWeekSalesForecastCardsData =  trafficForecastPage.getSalesForecastForeCastData();
        salesForecastWeeksViewForeCastData(previousWeekSalesForecastCardsData, "Previous 2nd Week");
        
        

         //* Projected Sales forecast for Future week

        trafficForecastPage.navigateSalesForecastWeekViewTpPastOrFuture("Future Week", SalesForecastForecastCalenderWeekCount.Three.getValue());
        Map<String, String> futureWeekSalesForecastCardsData =  trafficForecastPage.getSalesForecastForeCastData();
        salesForecastWeeksViewForeCastData(futureWeekSalesForecastCardsData, "Future Week");
        
    }
	
	
	
	private void salesForecastWeeksViewForeCastData(Map<String, String> WeekSalesForecastCardsData, String weekType) {
		
    	String peakTrafficProjected = (String)WeekSalesForecastCardsData.get("peakTrafficProjected");
    	String peakTrafficActual = (String)WeekSalesForecastCardsData.get("peakTrafficActual");
    	String totalTrafficProjected = (String)WeekSalesForecastCardsData.get("totalTrafficProjected");
    	String totalTrafficActual = (String)WeekSalesForecastCardsData.get("totalTrafficActual");
    	String peakTimeProjected = (String)WeekSalesForecastCardsData.get("peakTimeProjected");
    	String peakTimeActual = (String)WeekSalesForecastCardsData.get("peakTimeActual");
    	
    	/*
         * Fail on Projected & Actual values are null
         */
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Traffic Projected is'null'!",(peakTrafficProjected != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Traffic Actual is'null'!",(peakTrafficActual != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Total Traffic Projected is'null'!",(totalTrafficProjected != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Total Traffic Actual is'null'!",(totalTrafficActual != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Time Projected is'null'!",(peakTimeProjected != null) ,true);
    	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Time Actual is'null'!",(peakTimeActual != null) ,true);
        
    	/*
         * fail on "N/A" value of Actuals on Past 2nd Week
         */
    	if(!weekType.toLowerCase().contains("future") && !weekType.toLowerCase().contains("current"))
        {
        	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Traffic Actual is 'N/A'!",(! peakTrafficActual.contains("N/A")),true);
        	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Total Traffic Actual is 'N/A'!",(! totalTrafficActual.contains("N/A")) ,true);
        	SimpleUtils.assertOnFail( weekType+" Projected Sales Cards Data Peak Time Actual is 'N/A'!",(! peakTimeActual.contains("N/A")) ,true);
        }
        
        
        /*
         *  Logging Projected Sales forecast Data card values
         */
        
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Traffic Projected - "+peakTrafficProjected );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Traffic Actual - "+peakTrafficActual );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Total Traffic Projected - "+totalTrafficProjected);
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Total Traffic Actual - "+totalTrafficActual );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Time Projected - "+peakTimeProjected );
        ExtentTestManager.getTest().log(Status.INFO, weekType+" Projected Sales Cards Data Peak Time Actual - "+peakTimeActual );
		
	}
}
