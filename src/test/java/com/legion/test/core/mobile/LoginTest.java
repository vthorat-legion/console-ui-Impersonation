package com.legion.test.core.mobile;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.legion.pages.*;
import com.legion.tests.annotations.*;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.legion.pages.mobile.LoginPageAndroid;
import com.legion.pages.pagefactories.mobile.MobilePageFactory;
import com.legion.tests.TestBase;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;

import io.appium.java_client.android.AndroidDriver;

import static com.legion.api.cache.CacheAPI.getEmailVerificationURL;
import static com.legion.pages.BasePage.waitForSeconds;
import static com.legion.utils.MyThreadLocal.*;
import static com.legion.utils.SimpleUtils.*;

public class LoginTest extends TestBase{

	SchedulePage schedulePage = null;
	private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");
	private static HashMap<String, String> scheduleWorkRoles = JsonUtil.getPropertiesFromJsonFile("src/test/resources/WorkRoleOptions.json");
	private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
	@Override
	@BeforeMethod
	public void firstTest(Method method, Object[] params) throws Exception {
		// TODO Auto-generated method stub
//		this.createDriver((String) params[0], "68", "Linux");
		System.out.println("Mobile test Started");
//	    visitPage(method);
//	    loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
	}

	public enum overviewWeeksStatus{
		NotAvailable("Not Available"),
		Draft("Draft"),
		Guidance("Guidance"),
		Published("Published"),
		Finalized("Finalized");

		private final String value;
		overviewWeeksStatus(final String newValue) {
			value = newValue;
		}
		public String getValue() { return value; }
	}

	public enum SchedulePageSubTabText{
		Overview("OVERVIEW"),
		ProjectedSales("PROJECTED SALES"),
		StaffingGuidance("STAFFING GUIDANCE"),
		Schedule("SCHEDULE"),
		ProjectedTraffic("PROJECTED TRAFFIC");
		private final String value;
		SchedulePageSubTabText(final String newValue) {
			value = newValue;
		}
		public String getValue() { return value; }
	}

	@MobilePlatform(platform = "Android")
	@SanitySuite(sanity =  "Sanity")
	@UseAsTestRailSectionId(testRailSectionId = 99)
	@Automated(automated ="Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "LegionCoffee_Enterprise")
	@TestName(description = "Validate Login functionality of Mobile App")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
	public void gotoMobileLoginPage(String username, String password, String browser, String location) throws Exception {
	   launchMobileApp();
	   LoginPageAndroid loginPageAndroid = mobilePageFactory.createMobileLoginPage();
	   loginPageAndroid.verifyLoginTitle("WELCOME TO LEGION");
	   loginPageAndroid.loginToLegionWithCredentialOnMobile("val.wu", "legionco1");
	   loginPageAndroid.clickLoginBtn();
	   loginPageAndroid.displayHomePageLoaded();
	   loginPageAndroid.clickLogoutBtn();
   }


	@MobilePlatform(platform = "Android")
	@SanitySuite(sanity =  "Sanity")
	@UseAsTestRailSectionId(testRailSectionId = 99)
	@Automated(automated ="Automated")
	@Owner(owner = "Gunjan")
	@Enterprise(name = "LegionCoffee_Enterprise")
	@TestName(description = "Validate functionality of Onboarding")
	@Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
	public void gotoCreateProfileOnboardingPage(String username, String password, String browser, String location) throws Exception {
		launchMobileApp();
		LoginPageAndroid loginPageAndroid = mobilePageFactory.createMobileLoginPage();
		loginPageAndroid.clickOnLegionProfileBtn();
		loginPageAndroid.verifyCurrentEmployerPageLanded("Find Current Employer");
		loginPageAndroid.enterCompanyIdentifier("legioncoffee");
		loginPageAndroid.clickOnContinueBtn();
		loginPageAndroid.verifyFoundYourCompany();
		loginPageAndroid.verifyLegionProfileTodayPageLanded("Create your Legion Profile today");
		loginPageAndroid.clickOnContinueBtn();
		String strUserName = getUserName();
		loginPageAndroid.verifyEmailPageLandedForOnboarding(strUserName);
		loginPageAndroid.enterPassword(getParameterMap().get("password"));
		loginPageAndroid.enterConfirmPassword(getParameterMap().get("password"),getParameterMap().get("password"));
		loginPageAndroid.enterFName("Mega");
		loginPageAndroid.enterLName("Star");
		loginPageAndroid.clickOnLegionProfileBtn();
//		loginPageAndroid.verifyLegionTISPageLanded("Legion Terms of Service");
		loginPageAndroid.clickOnTISAgreeBtn();
		String urlEmailVerification = getEmailVerificationURL(strUserName,getParameterMap().get("password"));

		launchMobileWebApp();
		getAndroidWebDriver().get(urlEmailVerification);
		getAndroidWebDriver().navigate().refresh();
		waitForSeconds(90);
		launchMobileApp();


	}


	public void verifyScheduleLabelHours(String shiftTimeSchedule,
										 Float scheduledHoursBeforeEditing, Float scheduledHoursAfterEditing) throws Exception{
		Float scheduledHoursExpectedValueEditing = 0.0f;
		// If meal break is applicable
//	  		if(Float.parseFloat(shiftTimeSchedule) >= 6){
//	  			scheduledHoursExpectedValueEditing = (float) (scheduledHoursBeforeEditing + (Float.parseFloat(shiftTimeSchedule) - 0.5));
//	  		}else{
//	  			scheduledHoursExpectedValueEditing = (float)(scheduledHoursBeforeEditing + Float.parseFloat(shiftTimeSchedule));
//	  		}
		// If meal break is not applicable
		scheduledHoursExpectedValueEditing = (float)(scheduledHoursBeforeEditing + Float.parseFloat(shiftTimeSchedule));
		if(scheduledHoursExpectedValueEditing.equals(scheduledHoursAfterEditing)){
			SimpleUtils.pass("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" matches with Scheduled Hours Actual value "+scheduledHoursAfterEditing);
		}else{
			SimpleUtils.fail("Scheduled Hours Expected value "+scheduledHoursExpectedValueEditing+" does not match with Scheduled Hours Actual value "+scheduledHoursAfterEditing,false);
		}
	}


	public void verifyTeamCount(List<String> previousTeamCount, List<String> currentTeamCount) throws Exception {
		if(previousTeamCount.size() == currentTeamCount.size()){
			for(int i =0; i<currentTeamCount.size();i++){
				String currentCount = currentTeamCount.get(i);
				String previousCount = previousTeamCount.get(i);
				if(Integer.parseInt(currentCount) == Integer.parseInt(previousCount)+1){
					SimpleUtils.pass("Current Team Count is greater than Previous Team Count");
				}else{
					SimpleUtils.fail("Current Team Count is not greater than Previous Team Count",true);
				}
			}
		}else{
			SimpleUtils.fail("Size of Current Team Count should be equal to Previous Team Count",false);
		}
	}

	public void verifyGutterCount(int previousGutterCount, int updatedGutterCount){
		if(updatedGutterCount == previousGutterCount + 1){
			SimpleUtils.pass("Size of gutter is "+updatedGutterCount+" greater than previous value "+previousGutterCount);
		}else{
			SimpleUtils.fail("Size of gutter is "+updatedGutterCount+" greater than previous value "+previousGutterCount, false);
		}
	}


//	public void scheduleNavigationTest(int previousGutterCount) throws Exception{
//		ScheduleMainPage scheduleMainPage = pageFactory.createScheduleMainPage();
//		scheduleMainPage.clickOnEditButton();
//		boolean bolDeleteShift = checkAddedShift(previousGutterCount);
//		if(bolDeleteShift){
//			scheduleMainPage.clickSaveBtn();
//			scheduleMainPage.clickOnEditButton();
//		}
//	}

//	public boolean checkAddedShift(int guttercount)throws Exception {
//		boolean bolDeleteShift = false;
//		if (guttercount > 0) {
//			schedulePage.clickOnShiftContainer(guttercount);
//			bolDeleteShift = true;
//		}
//		return bolDeleteShift;
//	}


}
