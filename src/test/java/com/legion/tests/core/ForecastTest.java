package com.legion.tests.core;

import com.legion.pages.*;
import com.legion.pages.OpsPortaPageFactories.LocationsPage;
import com.legion.pages.OpsPortaPageFactories.UserManagementPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.core.OpsPortal.LocationsTest;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

import static java.lang.Math.abs;

public class ForecastTest extends TestBase{


	public enum ForecastCalenderWeekCount{
		ZERO(0),
		One(1),
		Two(2),
		Three(3);
		private final int value;
		ForecastCalenderWeekCount(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
	}

	@Override
	  @BeforeMethod()
	  public void firstTest(Method testMethod, Object[] params) throws Exception{
		  this.createDriver((String)params[0],"69","Window");
	      visitPage(testMethod);
	      loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
	  }


//	@Automated(automated ="Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate navigation and data loading in Day/Week view for Shoppers Subtab Of Forecast Tab ")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void forecastShoppersSubTabNavigationStoreManager(String username, String password, String browser, String location) throws Exception {
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		scheduleOverviewPage.loadScheduleOverview();
//		ForecastPage ForecastPage = pageFactory.createForecastPage();
//		ForecastPage.loadShoppersForecastforPastWeek(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.One.getValue());
//		scheduleOverviewPage.loadScheduleOverview();
//		ForecastPage.loadShoppersForecastforCurrentNFutureWeek(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Two.getValue());
//	}
//
//	@Automated(automated ="Automated")
//	@Owner(owner = "Gunjan")
//	@Enterprise(name = "KendraScott2_Enterprise")
//	@TestName(description = "Validate navigation and data loading in Day/Week view for Labor Subtab Of Forecast Tab")
//	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
//	public void forecastLaborSubTabNavigationStoreManager(String username, String password, String browser, String location) throws Exception {
//		ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
//		scheduleOverviewPage.loadScheduleOverview();
//		ForecastPage ForecastPage = pageFactory.createForecastPage();
//		ForecastPage.loadLaborForecastforPastWeek(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.One.getValue());
//		scheduleOverviewPage.loadScheduleOverview();
//		ForecastPage.loadLaborForecastforCurrentNFutureWeek(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Two.getValue());
//	}



		@Automated(automated = "Automated")
		@Owner(owner = "Estelle")
		@Enterprise(name = "KendraScott2_Enterprise")
		@TestName(description = "Verify the Schedule functionality > Shopper Forecast")
		@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
		public void verifyShopperForecastFunctionalityAsInternalAdmin(String username, String password, String browser, String location)
				throws Exception {
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
			ScheduleShiftTablePage scheduleShiftTablePage = pageFactory.createScheduleShiftTablePage();
			scheduleOverviewPage.loadScheduleOverview();
			ForecastPage ForecastPage  = pageFactory.createForecastPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ForecastPage.clickForecast();
		    //In Shoppers,Smartcard are available in correct alignment
			ForecastPage.verifySmartcardAreAvailableInShoppers();
			//In Holiday smartcard,View All button is clickable and Current week's Holidays are showing
			ForecastPage.holidaySmartCardIsDisplayedForCurrentAWeek();
			//in Shopper insight smartcard, Peak Shopper and Peek day data is matching with Forecast Bar chart
			ForecastPage.verifyPeakShopperPeakDayData();
			//there is no data if store is closed
			HashMap<String, Float> insightData1 = new HashMap<String, Float>();
			Float[] peakItemsShoppers = new Float[7];
			Float[] totalItemsShoppers = new Float[7];
			Float sum = 0.0f;
			HashMap<String, Float> insightDataInWeek = new HashMap<String, Float>();
			insightDataInWeek = ForecastPage.getInsightDataInShopperWeekView();
			scheduleCommonPage.clickOnDayView();
			//click the first day of a week
			scheduleCommonPage.clickOnPreviousDaySchedule("Sun");
			for (int index = 0; index < ConsoleScheduleNewUIPage.dayCount.Seven.getValue(); index++) {
				scheduleCommonPage.clickOnNextDaySchedule(scheduleCommonPage.getActiveAndNextDay());
				if (scheduleShiftTablePage.inActiveWeekDayClosed(index)){
					SimpleUtils.report("Store is closed and there is no insight smartc");
				}else {
					insightData1 = ForecastPage.getInsightDataInShopperDayView();
					peakItemsShoppers[index] =insightData1.get("peakShoppers");
					totalItemsShoppers[index] =insightData1.get("totalShoppers");
					sum+=totalItemsShoppers[index];
					SimpleUtils.report("Store is Open for the Day/Week: '" + scheduleCommonPage.getActiveWeekText() + ":"+insightData1);
				}
			}
			if (insightDataInWeek.get("totalShoppers").equals(sum)) {
				SimpleUtils.pass("Total Shoppers data is sum of total data in day view and Peak shoppers data is correct");
			}else {
				//SimpleUtils.fail("verifyShopperForecastFunctionality: Total Shoppers data is wrong",true);
				SimpleUtils.warn("BUG existed-->SF-418:Total Shoppers data is wrong!");
			}
			scheduleCommonPage.clickOnWeekView();
			//navigate to <> buttons are working
			ForecastPage.verifyNextPreviousBtnCorrectOrNot();
			//After selecting of all display filter option, data in Projected shoppers is showing according to filters
			//Todo: Run failed by LEG-10179
//			ForecastPage.verifyFilterFunctionInForecast();
			//Navigate to 2 past week, match the Actual data of smartcard with Projected shoppers of week
			//Todo: Run failed by LEG-10237
			ForecastPage.verifyActualDataForPastWeek();
			//verify display lines color
			ForecastPage.verifyDisplayOfActualLineSelectedByDefaultInOrangeColor();
			ForecastPage.verifyRecentTrendLineIsSelectedAndColorInBrown();
			ForecastPage.verifyLastYearLineIsSelectedAndColorInPurple();
			ForecastPage.verifyForecastColourIsBlue();
			//After Click on Refresh Button,it should navigate back to page
			ForecastPage.verifyRefreshBtnInShopperWeekView();

		}


	@Automated(automated = "Automated")
	@Owner(owner = "Estelle")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify the Schedule functionality > Shopper Forecast> Weather smartcard")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateWeatherSmartCardOnForecastPageAsInternalAdmin(String username, String password, String browser, String location)
			throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();

		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		SmartCardPage smartCardPage = pageFactory.createSmartCardPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		SimpleUtils.assertOnFail("'Overview' sub tab not loaded Successfully!",
				scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);

		String WeatherCardText = "WEATHER";
		ForecastPage forecastPage = pageFactory.createForecastPage();
		forecastPage.clickForecast();
		//Validate Weather Smart card on Week View
		scheduleCommonPage.clickOnWeekView();

		Thread.sleep(5000);
		String activeWeekText = scheduleCommonPage.getActiveWeekText();

		if (smartCardPage.isSmartCardAvailableByLabel(WeatherCardText)) {
			SimpleUtils.pass("Weather Forecart Smart Card appeared for week view duration: '" + activeWeekText + "'");
			String[] splitActiveWeekText = activeWeekText.split(" ");
			String smartCardTextByLabel = smartCardPage.getsmartCardTextByLabel(WeatherCardText);
			String weatherTemperature = smartCardPage.getWeatherTemperature();

			SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain starting day('" + splitActiveWeekText[0] + "') of active week: '" + activeWeekText + "'",
					smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[0].toLowerCase()), true);

			SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain Ending day('" + splitActiveWeekText[0] + "') of active week: '" + activeWeekText + "'",
					smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[0].toLowerCase()), true);
			if (weatherTemperature != "")
				SimpleUtils.pass("Weather Forecart Smart Card contains Temperature value: '" + weatherTemperature + "' for the duration: '" +
						activeWeekText + "'");
			else
				SimpleUtils.fail("Weather Forecart Smart Card not contains Temperature value for the duration: '" + activeWeekText + "'", true);
		} else {
			//SimpleUtils.fail("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'", true);
			SimpleUtils.warn("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'");
		}

		//Validate Weather Smart card on day View
		scheduleCommonPage.clickOnDayView();
		for (int index = 0; index < ScheduleTestKendraScott2.dayCount.Seven.getValue(); index++) {
			if (index != 0)
				scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.One.getValue());

			String activeDayText = scheduleCommonPage.getActiveWeekText();
			if (smartCardPage.isSmartCardAvailableByLabel(WeatherCardText)) {
				SimpleUtils.pass("Weather Forecart Smart Card appeared for week view duration: '" + activeDayText + "'");
				String[] splitActiveWeekText = activeDayText.split(" ");
				String smartCardTextByLabel = smartCardPage.getsmartCardTextByLabel(WeatherCardText);
				SimpleUtils.assertOnFail("Weather Forecart Smart Card not contain starting day('" + splitActiveWeekText[1] + "') of active day: '" + activeDayText + "'",
						smartCardTextByLabel.toLowerCase().contains(splitActiveWeekText[1].toLowerCase()), true);
				String weatherTemperature = smartCardPage.getWeatherTemperature();
				if (weatherTemperature != "")
					SimpleUtils.pass("Weather Forecart Smart Card contains Temperature value: '" + weatherTemperature + "' for the duration: '" +
							activeWeekText + "'");
				else
					SimpleUtils.pass("Weather Forecart Smart Card not contains Temperature value for the duration: '" + activeWeekText + "'");
			} else {
				//SimpleUtils.fail("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'", true);
				SimpleUtils.warn("Weather Forecart Smart Card not appeared for week view duration: '" + activeWeekText + "'");
			}
		}
	}



		@Automated(automated = "Automated")
		@Owner(owner = "Estelle")
		@Enterprise(name = "KendraScott2_Enterprise")
		@TestName(description = "Verify the Schedule functionality > Forecast")
		@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
		public void verifyScheduleFunctionalityForecastAsInternalAdmin(String username, String password, String browser, String location)
				throws Exception {
			ScheduleOverviewPage scheduleOverviewPage = pageFactory.createScheduleOverviewPage();
			scheduleOverviewPage.loadScheduleOverview();
			ForecastPage ForecastPage  = pageFactory.createForecastPage();
			ForecastPage.clickForecast();
			boolean isWeekForecastVisibleAndOpen = ForecastPage.verifyIsWeekForecastVisibleAndOpenByDefault();
			boolean isShopperSelectedByDefaultAndLaborClickable = ForecastPage.verifyIsShopperTypeSelectedByDefaultAndLaborTabIsClickable();
			if (isWeekForecastVisibleAndOpen) {
				if (isShopperSelectedByDefaultAndLaborClickable){
					SimpleUtils.pass("Forecast Functionality show well");
				} else {
					SimpleUtils.warn("there is no shopper in this enterprise!");
				}
			}else {
				SimpleUtils.warn("forecast default functionality work error");
			}
		}

		@Automated(automated = "Automated")
		@Owner(owner = "Estelle")
		@Enterprise(name = "KendraScott2_Enterprise")
		@TestName(description = "Verify the Schedule functionality > Labor Forecast")
		@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
		public void verifyScheduleLaborForeCastFunctionalityAsInternalAdmin(String username, String password, String browser, String location)
				throws Exception {

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) ,false);
			ForecastPage ForecastPage  = pageFactory.createForecastPage();
			ForecastPage.clickForecast();
			ForecastPage.clickOnLabor();
			//past+current+future week is visible and enable to navigate to future and past week by '>' and '<' button
			ForecastPage.verifyNextPreviousBtnCorrectOrNot();
			//Work role filter is selected all roles by default, can be selected one or more
			ForecastPage.verifyWorkRoleSelection();
           //After selecting any workrole, Projected Labor bar will display according to work role
			ForecastPage.verifyBudgetedHoursInLaborSummaryWhileSelectDifferentWorkRole();
			//Weather week smartcard is displayed for a week[sun-sat]
			ForecastPage.weatherWeekSmartCardIsDisplayedForAWeek();
			//If some work role has been selected in one week then these will remain selected in every past and future week
			ForecastPage.verifyWorkRoleIsSelectedAftSwitchToPastFutureWeek();
			//After click on refresh, page should get refresh and back to previous page only
			ForecastPage.verifyRefreshBtnInLaborWeekView();

		}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Edit Forecast in Week view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditForecastInWeekViewViewAsInternalAdmin(String browser, String username, String password, String location) {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ForecastPage forecastPage  = pageFactory.createForecastPage();

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()) , false);
			//verify edit forecast button
			int index = 3;
			String value = "510";
			String weekDayInfo = forecastPage.getTickByIndex(index);
			String editedValueInfo = value+" Edited";
			forecastPage.verifyAndClickEditBtn();
			forecastPage.verifyAndClickCancelBtn();
			forecastPage.verifyAndClickEditBtn();


			//verify double click graph bar
			forecastPage.verifyDoubleClickAndUpdateForecastBarValue(String.valueOf(index), value);
			String tooltipInfo =forecastPage.getTooltipInfo(String.valueOf(index));
			boolean flag = tooltipInfo.contains("Actual")||tooltipInfo.contains("Last Year")||tooltipInfo.contains("Recent Trend");
			SimpleUtils.assertOnFail("Info on tooltip is incorrect!",tooltipInfo.contains(weekDayInfo+" Forecast")&&tooltipInfo.contains(editedValueInfo)&&tooltipInfo.contains("Comparison")&&flag,false);
			//Save forecast and check the value.
			forecastPage.verifyAndClickSaveBtn();
			tooltipInfo =forecastPage.getTooltipInfo(String.valueOf(index));
			SimpleUtils.assertOnFail("Edited value is not saved!",tooltipInfo.contains(value),false);
			forecastPage.verifyAndClickEditBtn();
			forecastPage.verifyLegionPeakShopperFromForecastGraphInWeekView();
			scheduleCommonPage.navigateToNextWeek();
			forecastPage.verifyWarningEditingForecast();
			forecastPage.verifyAndClickSaveBtn();


			//Verify graph bars are draggable.
			//forecastPage.verifyDraggingBarGraph();
			//Save forecast and check the value.;
		} catch (Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Edit Forecast in Day View")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyEditForecastInDayViewViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ForecastPage forecastPage  = pageFactory.createForecastPage();

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()) , false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()),false);
			forecastPage.clickOnDayView();
			String currentDay = forecastPage.getActiveDayText();

			// Verify the presence of Edit button on Forecast page
			forecastPage.verifyEditBtnVisible();

			// Verify Edit button is clickable
			forecastPage.verifyAndClickEditBtn();

			// Verify the content after clicking on Edit button on Forecast page
			forecastPage.verifyContentInEditMode();

			// Verify warning message pops up when clicking other day in Edit mode, and verify OK button is clickable on the warning pop up model
			forecastPage.navigateToOtherDay();
			forecastPage.verifyWarningEditingForecast();

			// Verify cancel button is clickable
			forecastPage.verifyAndClickCancelBtn();

			// Verify mouse hover on each bar graph
			forecastPage.verifyAndClickEditBtn();
			forecastPage.verifyTooltipWhenMouseEachBarGraph();

			// Verify the Peak value, Peak Time, Total Sum are correct on smart card
			forecastPage.verifyPeakShoppersPeakTimeTotalShoppersLegionDataInDayView();

			// Verify dragging the bar graph
			forecastPage.verifyDraggingBarGraph();

			// Verify double clicking the bar graph
			String index = "3";
			String legionValueInBar = forecastPage.getLegionValueFromBarTooltip(Integer.valueOf(index));
			forecastPage.verifyDoubleClickBarGraph(index);

			// Verify the content of Specify a value layout
			forecastPage.verifyContentOfSpecifyAValueLayout(index,legionValueInBar);

			// Verify Close button on pop up is clickable
			forecastPage.verifyAndClickCloseBtn();

			// Verify Cancel button on Pop up is clickable
			forecastPage.verifyDoubleClickBarGraph(index);
			forecastPage.verifyAndClickCancelBtn();

			// Verify inputting the new value when double clicking the bar graph
			String editedValue = "2";
			forecastPage.verifyDoubleClickAndUpdateForecastBarValue(index, editedValue);

			// Verify OK button on pop up is clickable
			forecastPage.verifyDoubleClickBarGraph(index);
			forecastPage.verifyAndClickOKBtn();

			// Verify the value is changed to the new value and percentage when mouse hovering
			String editedValueInBar = forecastPage.getEditedValueFromBarTooltip(Integer.valueOf(index));
			String percentageInBar = forecastPage.getPercentageFromBarTooltip(Integer.valueOf(index));
			String percentageExpected = "";
			if (legionValueInBar.equals("0"))
				percentageExpected = "↑%";
			else {
				if (Integer.valueOf(legionValueInBar) > Integer.valueOf(editedValue))
					percentageExpected = "↓" + (Integer.valueOf(legionValueInBar) - Integer.valueOf(editedValue))* 100/Integer.valueOf(legionValueInBar)  + "%";
				else
					percentageExpected = "↑" + (Integer.valueOf(editedValue) - Integer.valueOf(legionValueInBar))*100/Integer.valueOf(legionValueInBar) + "%";
			}
			if (editedValueInBar.equals(editedValue) && percentageInBar.equals(percentageExpected))
				SimpleUtils.pass("Forecast Page: The value is changed to the new value and percentage when mouse hovering");
			else
				SimpleUtils.warn("SCH-2396: Failed to update the value when double clicking the bar graph");

			// Verify the Confirm Message when saving the edits
			forecastPage.verifyAndClickSaveBtn();
			forecastPage.verifyConfirmMessageWhenSaveForecast("day", currentDay);

			// Verify the Cancel button is clickable on the confirm pop up
			forecastPage.clickOnCancelBtnOnConfirmPopup();

			// Verify Save forecast button is clickable on the confirm pop up
			forecastPage.verifyAndClickSaveBtn();
			forecastPage.clickOnSaveBtnOnConfirmPopup();

			// Verify the values are saved successfully
			String newValueInBar = forecastPage.getLegionValueFromBarTooltip(Integer.valueOf(index));
			if (newValueInBar.equals(editedValue))
				SimpleUtils.pass("Forecast Page: the values are saved successfully");
			else
				SimpleUtils.fail("Forecast Page: the values failed to save",false);

			// Verify the value on smart card is correct after saving
			forecastPage.verifyPeakShoppersPeakTimeTotalShoppersEditedDataInDayView();
		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(),false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Edit Forecast in Day View")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyFunctionalityOfRefreshAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ForecastPage forecastPage = pageFactory.createForecastPage();

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);

			// Verify forecast can be edited and saved
			int index = 3;
			String value = "510";
			forecastPage.verifyAndClickEditBtn();
			forecastPage.verifyDoubleClickAndUpdateForecastBarValue(String.valueOf(index), value);
			forecastPage.verifyAndClickSaveBtn();

			// Verify Refresh button is clickable
			forecastPage.clickOnRefreshButton();

			// Verify Warning dialog pops up
			forecastPage.verifyWarningDialogPopsUp();

			// Verify the content on Warning dialog
			forecastPage.verifyTheContentOnWarningDialog();

			// Verify the functionality of Cancel button on Warning dialog
			forecastPage.verifyTheFunctionalityOfCancelButtonOnWarningDialog(index, value);

			// Verify the functionality of Refresh anyway button on Warning dialog
			forecastPage.clickOnRefreshButton();
			forecastPage.verifyTheFunctionalityOfRefreshanywayButtonOnWarningDialog(index, value);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "KendraScott2_Enterprise")
	@TestName(description = "Verify Edit Forecast in Day View")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyFunctionalityOfRefreshAsStoreManager(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("Dashboard page not loaded successfully!", dashboardPage.isDashboardPageLoaded(), false);

			ForecastPage forecastPage = pageFactory.createForecastPage();

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			SimpleUtils.assertOnFail("Schedule page 'Overview' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), false);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);

			// Verify forecast can be edited and saved
			int index = 3;
			String value = "510";
			forecastPage.verifyAndClickEditBtn();
			forecastPage.verifyDoubleClickAndUpdateForecastBarValue(String.valueOf(index), value);
			forecastPage.verifyAndClickSaveBtn();

			// Verify Refresh button is clickable
			forecastPage.clickOnRefreshButton();

			// Verify Warning dialog pops up
			forecastPage.verifyWarningDialogPopsUp();

			// Verify the content on Warning dialog
			forecastPage.verifyTheContentOnWarningDialog();

			// Verify the functionality of Cancel button on Warning dialog
			forecastPage.verifyTheFunctionalityOfCancelButtonOnWarningDialog(index, value);

			// Verify the functionality of Refresh anyway button on Warning dialog
			forecastPage.clickOnRefreshButton();
			forecastPage.verifyTheFunctionalityOfRefreshanywayButtonOnWarningDialog(index, value);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Julie")
	@Enterprise(name = "Coffee_Enterprise")
	@TestName(description = "Verify Edit Forecast in Day View")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateP2PLaborForecastCanLoadAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		try {
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
			LocationSelectorPage locationSelectorPage = pageFactory.createLocationSelectorPage();
			locationSelectorPage.changeDistrict("Bay Area District");
			locationSelectorPage.changeLocation("LocGroup2");

			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Overview.getValue()), true);
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
			ForecastPage forecastPage = pageFactory.createForecastPage();

            // Validate P2P labor forecast can load successfully
			forecastPage.clickOnLabor();
			forecastPage.verifyLaborForecastCanLoad();

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate day parts are available in defined tab of week view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateDayPartsAvailableInDefinedTabOfWeekViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
		ForecastPage forecastPage = pageFactory.createForecastPage();

		HashMap<String, Float> insightDataBefore = forecastPage.getInsightDataInShopperWeekView();
		forecastPage.clickOnFilterButtonUnderDefinedTab();
		forecastPage.verifyThereAreDayPartsItemsInTheFilter();
		List<String> dayPartsDefined =  Arrays.asList("Retail Afternoon", "Retail Morning");
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnFilterButtonUnderDefinedTab();
		HashMap<String, Float> insightDataAfter = forecastPage.getInsightDataInShopperWeekView();
		//SimpleUtils.assertOnFail("Insight smart card has no changes!", !insightDataBefore.entrySet().containsAll(insightDataAfter.entrySet()), false);


		forecastPage.clickOnFilterButtonUnderDefinedTab();
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnFilterButtonUnderDefinedTab();

		SimpleUtils.assertOnFail("Insight smart card has no changes!", insightDataBefore.entrySet().containsAll(forecastPage.getInsightDataInShopperWeekView().entrySet()), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate day parts are available in Labor tab of week view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateDayPartsAvailableInLablorTabOfWeekViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
		ForecastPage forecastPage = pageFactory.createForecastPage();
		forecastPage.clickOnLabor();
		forecastPage.verifyLaborForecastCanLoad();

		HashMap<String, Float> insightDataBefore = forecastPage.getInsightDataInShopperWeekView();
		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();
		List<String> dayPartsDefined =  Arrays.asList("Retail Afternoon", "Retail Morning");
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();
		HashMap<String, Float> insightDataAfter = forecastPage.getInsightDataInShopperWeekView();
		SimpleUtils.assertOnFail("Insight smart card has no changes!", !insightDataBefore.entrySet().containsAll(insightDataAfter.entrySet()), false);


		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();

		SimpleUtils.assertOnFail("Insight smart card has no changes!", insightDataBefore.entrySet().containsAll(forecastPage.getInsightDataInShopperWeekView().entrySet()), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate day parts are available in defined tab of day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateDayPartsAvailableInDefinedTabOfDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
		ForecastPage forecastPage = pageFactory.createForecastPage();

		scheduleCommonPage.clickOnDayView();
		HashMap<String, Float> insightDataBefore = forecastPage.getInsightDataInShopperWeekView();
		forecastPage.clickOnFilterButtonUnderDefinedTab();
		forecastPage.verifyThereAreDayPartsItemsInTheFilter();
		List<String> dayPartsDefined =  Arrays.asList("Retail Afternoon", "Retail Morning");
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnFilterButtonUnderDefinedTab();
		HashMap<String, Float> insightDataAfter = forecastPage.getInsightDataInShopperWeekView();
		//SimpleUtils.assertOnFail("Insight smart card has no changes!", !insightDataBefore.entrySet().containsAll(insightDataAfter.entrySet()), false);


		forecastPage.clickOnFilterButtonUnderDefinedTab();
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnFilterButtonUnderDefinedTab();

		SimpleUtils.assertOnFail("Insight smart card has no changes!", insightDataBefore.entrySet().containsAll(forecastPage.getInsightDataInShopperWeekView().entrySet()), false);
	}

	@Automated(automated = "Automated")
	@Owner(owner = "Haya")
	@Enterprise(name = "Vailqacn_Enterprise")
	@TestName(description = "Validate day parts are available in Labor tab of day view")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void validateDayPartsAvailableInLablorTabOfDayViewAsInternalAdmin(String browser, String username, String password, String location) throws Exception {
		DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
		SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!", dashboardPage.isDashboardPageLoaded(), false);
		ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
		scheduleCommonPage.clickOnScheduleConsoleMenuItem();
		scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
		SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!", scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()), false);
		ForecastPage forecastPage = pageFactory.createForecastPage();
		forecastPage.clickOnLabor();
		forecastPage.verifyLaborForecastCanLoad();

		scheduleCommonPage.clickOnDayView();
		HashMap<String, Float> insightDataBefore = forecastPage.getInsightDataInShopperWeekView();
		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();
		List<String> dayPartsDefined =  Arrays.asList("Retail Afternoon", "Retail Morning");
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();
		HashMap<String, Float> insightDataAfter = forecastPage.getInsightDataInShopperWeekView();
		SimpleUtils.assertOnFail("Insight smart card has no changes!", !insightDataBefore.entrySet().containsAll(insightDataAfter.entrySet()), false);


		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();
		forecastPage.selectFilterOptionsByText(dayPartsDefined.get(0));
		forecastPage.clickOnDayPartsFilterButtonUnderLaborTab();

		SimpleUtils.assertOnFail("Insight smart card has no changes!", insightDataBefore.entrySet().containsAll(forecastPage.getInsightDataInShopperWeekView().entrySet()), false);
	}



	@Automated(automated = "Automated")
	@Owner(owner = "Mary")
	@Enterprise(name = "CinemarkWkdy_Enterprise")
	@TestName(description = "Verify the manage past demand forecast permission for different access roles")
	@Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass = CredentialDataProviderSource.class)
	public void verifyTheManagePastDemandForecastPermissionForDifferentAccessRolesAsInternalAdmin (String browser, String username, String password, String location) throws Exception {
		try {
			LoginPage loginPage = pageFactory.createConsoleLoginPage();
			ControlsNewUIPage controlsNewUIPage = pageFactory.createControlsNewUIPage();
			ScheduleCommonPage scheduleCommonPage = pageFactory.createScheduleCommonPage();
			ForecastPage forecastPage = pageFactory.createForecastPage();
			LocationsPage locationsPage = pageFactory.createOpsPortalLocationsPage();
			UserManagementPage userManagementPage = pageFactory.createOpsPortalUserManagementPage();
			CinemarkMinorPage cinemarkMinorPage = pageFactory.createConsoleCinemarkMinorPage();
			DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
			int count = (int)(Math.random()*6+1);
			String accessRole = "";
			String roleName = "";
			switch(count) {
				case 1: accessRole = AccessRoles.StoreManager.getValue(); roleName = "Store Manager";break;
				case 2: accessRole = AccessRoles.TeamLead.getValue(); roleName = "Team Lead";break;
				case 3: accessRole = AccessRoles.CustomerAdmin.getValue(); roleName = "Admin";break;
				case 4: accessRole = AccessRoles.GeneralManager.getValue(); roleName = "General Manager";break;
				case 5: accessRole = AccessRoles.AreaManager.getValue(); roleName = "Area Manager";break;
			}
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.OperationPortal.getValue());
			SimpleUtils.assertOnFail("OpsPortal Page not loaded Successfully!", locationsPage.isOpsPortalPageLoaded(), false);
			Thread.sleep(3000);
			userManagementPage.clickOnUserManagementTab();
			SimpleUtils.assertOnFail("Users and Roles card not loaded Successfully!", controlsNewUIPage.isControlsUsersAndRolesCard(), false);
			userManagementPage.goToUserAndRoles();
			userManagementPage.goToAccessRolesTab();
			cinemarkMinorPage.clickOnBtn(CinemarkMinorTest.buttonGroup.Edit.getValue());
			//Get permission status for specific roles
			String section = "Schedule";
			String permission = "Manage Past Demand Forecast";
			boolean status = controlsNewUIPage.getStatusOfSpecificPermissionForSpecificRoles(section, roleName, permission);
			locationsPage.clickModelSwitchIconInDashboardPage(LocationsTest.modelSwitchOperation.Console.getValue());
			loginPage.logOut();
			//Verify the shifts can be created by new UI by original SM access role
			Thread.sleep(5000);
			SimpleUtils.pass("Will login as: "+ accessRole);
			loginAsDifferentRole(accessRole);
			loginPage.logOut();
			Thread.sleep(5000);
			loginAsDifferentRole(accessRole);
			dashboardPage.clickOnDashboardConsoleMenu();
			scheduleCommonPage.clickOnScheduleConsoleMenuItem();
			scheduleCommonPage.clickOnScheduleSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue());
			SimpleUtils.assertOnFail("Schedule page 'Forecast' sub tab not loaded Successfully!",
					scheduleCommonPage.verifyActivatedSubTab(ScheduleTestKendraScott2.SchedulePageSubTabText.Forecast.getValue()),
					false);
			forecastPage.clickOnShopper();
			SimpleUtils.assertOnFail("Demand forecast edit button should always enabled for current week! ",
					forecastPage.isDemandForecastEditButtonEnabled(), false);
			scheduleCommonPage.navigateToPreviousWeek();
			if (status){
				SimpleUtils.assertOnFail("Demand forecast edit button should be enabled for past week! ",
						forecastPage.isDemandForecastEditButtonEnabled(), false);
			}else
				SimpleUtils.assertOnFail("Demand forecast edit button should be disabled for past week! ",
						!forecastPage.isDemandForecastEditButtonEnabled(), false);

		} catch (Exception e) {
			SimpleUtils.fail(e.getMessage(), false);
		}

	}


}
