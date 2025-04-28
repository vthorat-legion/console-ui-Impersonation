package com.legion.pages.core.mobile;

import static com.legion.utils.MyThreadLocal.getAndroidDriver;
import static com.legion.utils.MyThreadLocal.getDriver;
import io.appium.java_client.MobileElement;

import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.legion.pages.BasePage;
import com.legion.pages.LoginPage;
import com.legion.pages.mobile.LoginPageAndroid;
import com.legion.tests.TestBase;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.support.ui.FluentWait;

public class MobileLoginPage extends BasePage implements LoginPageAndroid {
	//Red app

//	 @FindBy(id="co.legion.client.staging:id/loginBTN")
//	 private MobileElement btnSelectLogin;
//
//	 @FindBy(id="co.legion.client.staging:id/tv_title")
//	 private MobileElement titleLogin;
//
//	 @FindBy(id="co.legion.client.staging:id/buildType")
//	 private MobileElement clickEnterprise;
//
//	 @FindBy(id="co.legion.client.staging:id/usernameET")
//	 private MobileElement userNameMobile;
//
//	 @FindBy(id="co.legion.client.staging:id/passwordEditText")
//	 private MobileElement passwordMobile;
//
//	 @FindBy(id="co.legion.client.staging:id/login")
//	 private MobileElement loginBtn;
//
//	@FindBy(xpath="//android.widget.TextView[@text='Shift Offers']")
//	private MobileElement shiftOffers;
//
//	@FindBy(id="co.legion.client.staging:id/businessImageView")
//	private MobileElement businessImageView;
//
//	@FindBy(id="co.legion.client.staging:id/nameTV")
//	private MobileElement openShiftOfferGrid;
//
////	@FindBy(id="co.legion.client.staging:id/tab_image")
////	private List<MobileElement> shiftOffers;
//
//	@FindBy(id="co.legion.client.staging:id/subDayLayout")
//	private MobileElement shiftOfferDay;
//
//	@FindBy(id="co.legion.client.staging:id/statTimeTV")
//	private MobileElement shiftOfferStartTime;
//
//	@FindBy(id="co.legion.client.staging:id/endTimeTV")
//	private MobileElement shiftOfferEndTime;
//
//	@FindBy(id="co.legion.client.staging:id/btn_claim_your_shift_offer")
//	private MobileElement btnShiftClaim;
//
//	@FindBy(id="co.legion.client.staging:id/btn_claim_your_shift_decline")
//	private MobileElement btnShiftDecline;
//
//	@FindBy(id="co.legion.client.staging:id/alertLL")
//	private MobileElement alertClaim;
//
//	@FindBy(id="co.legion.client.staging:id/agreeTV")
//	private MobileElement btnAgreeClaim;
//
//	@FindBy(id="co.legion.client.staging:id/cancelTv")
//	private MobileElement btnCancelClaim;
//
//	@FindBy(id="co.legion.client.staging:id/shiftTypeNameTV")
//	private MobileElement shiftOfferAvail;
//
//	@FindBy(id="co.legion.client.staging:id/tab_container")
//	private MobileElement shiftOffers1;
//
//	@FindBy(xpath="android.widget.TextView[@text='Schedule']")
//	private MobileElement scheduleTab;
//
//	@FindBy(id="co.legion.client.staging:id/tv_title")
//	private MobileElement scheduleTitle;
//
//	@FindBy(id="co.legion.client.staging:id/dateTv")
//	private MobileElement scheduleDate;
//
//	@FindBy(id="co.legion.client.staging:id/nextIv")
//	private MobileElement nextArrowSchedule;
//
//	@FindBy(id="co.legion.client.staging:id/prevIv")
//	private MobileElement previousArrowSchedule;
//
//	@FindBy(xpath="//android.widget.LinearLayout[@id='co.legion.client.staging:id/mainLl']")
//	private MobileElement scheduleGrid;
//
//	//Blue app
//
//	@FindBy(id="co.legion.client.staging:id/signInBT")
//	private MobileElement btnSelectLogin;

	@FindBy(xpath="//android.widget.Button[contains(@text,'Sign in')]")
	private MobileElement btnSignIn;

	@FindBy(xpath="//android.widget.TextView[contains(@text,'WELCOME TO LEGION')]")
	private MobileElement titleLogin;

	@FindBy(id="co.legion.client:id/buildType")
	private MobileElement clickEnterprise;

	@FindBy(xpath="//android.widget.EditText[@index,'1']")
	private MobileElement userNameMobile;

	@FindBy(xpath="//android.view.View[2]/android.view.View/android.view.View/android.widget.EditText")
	private MobileElement passwordMobile;

	@FindBy(xpath="//android.widget.Button[@index,'3']")
	private MobileElement loginBtn;

	@FindBy(xpath="//android.widget.Button[contains(@text,'Profile')]")
	private MobileElement createProfileBtn;

	@FindBy(xpath="//android.widget.TextView[contains(@text,'Employer')]")
	private MobileElement txtCurrentEmployerPage;

	@FindBy(xpath="//android.widget.EditText[@index,'1']")
	private MobileElement txtCompanyIdentifier;

	@FindBy(xpath="//android.widget.Button[@text,'Continue']")
	private MobileElement btnContinue;

	@FindBy(xpath="//android.widget.TextView[contains(@text,'company')]")
	private MobileElement txtCompanyIdentifierMatch;

	@FindBy(xpath="//android.widget.Button[1]")
	private MobileElement btnCont;

	@FindBy(xpath="//android.widget.TextView[contains(@text,'Profile today')]")
	private MobileElement txtLegionProfileToday;

	@FindBy(xpath="//android.view.View[2]/android.view.View[1]//android.widget.EditText")
	private List<MobileElement> txtEmail;

	@FindBy(xpath="//android.view.View[2]/android.view.View[2]//android.widget.EditText")
	private MobileElement txtPassword;

	@FindBy(xpath="//android.view.View[2]/android.view.View[3]//android.widget.EditText")
	private MobileElement txtConfirmPassword;

	@FindBy(xpath="//android.view.View[2]/android.view.View[4]//android.widget.EditText")
	private MobileElement txtFName;

	@FindBy(xpath="//android.view.View[2]/android.view.View[5]//android.widget.EditText")
	private MobileElement txtLName;

	@FindBy(xpath="//android.view.ViewGroup/android.widget.TextView")
	private MobileElement txtLegionTermsOfServices;
	@FindBy(xpath="//android.widget.Button[contains(@text,'Agree')]")
	private MobileElement btnAgree;
	@FindBy(xpath="//android.widget.TextView[@text='Shift Offers']")
	private MobileElement shiftOffers;

	@FindBy(id="co.legion.client:id/businessImageView")
	private MobileElement businessImageView;

	@FindBy(id="co.legion.client:id/swipe_layout")
	private List<MobileElement> openShiftOfferGrid;

//	@FindBy(id="co.legion.client.staging:id/tab_image")
//	private List<MobileElement> shiftOffers;

	@FindBy(id="co.legion.client:id/subDayLayout")
	private List<MobileElement> shiftOfferDay;

	@FindBy(id="co.legion.client:id/dayNameTV")
	private MobileElement shiftDayName;

	@FindBy(id="co.legion.client:id/statTimeTV")
	private List<MobileElement> shiftOfferStartTime;

	@FindBy(id="co.legion.client:id/endTimeTV")
	private List<MobileElement> shiftOfferEndTime;

	@FindBy(id="co.legion.client:id/btn_claim_your_shift_offer")
	private MobileElement btnShiftClaim;

	@FindBy(id="co.legion.client:id/btn_claim_your_shift_decline")
	private MobileElement btnShiftDecline;

	@FindBy(id="co.legion.client:id/alertLL")
	private MobileElement alertClaim;

	@FindBy(id="co.legion.client:id/agreeTV")
	private MobileElement btnAgreeClaim;

	@FindBy(id="co.legion.client:id/cancelTv")
	private MobileElement btnCancelClaim;

	@FindBy(id="co.legion.client:id/shiftTypeNameTV")
	private MobileElement shiftOfferAvail;

	@FindBy(id="co.legion.client:id/tab_container")
	private MobileElement shiftOffers1;

	@FindBy(xpath="android.widget.TextView[@text='Schedule']")
	private MobileElement scheduleTab;

	@FindBy(id="co.legion.client:id/tab_container")
	private List<MobileElement> scheduleTabContainer;

	@FindBy(id="co.legion.client:id/tv_title")
	private MobileElement scheduleTitle;

	@FindBy(id="co.legion.client:id/dateTv")
	private MobileElement scheduleDate;

	@FindBy(id="co.legion.client:id/nextIv")
	private MobileElement nextArrowSchedule;

	@FindBy(id="co.legion.client:id/prevIv")
	private MobileElement previousArrowSchedule;

	@FindBy(id="co.legion.client:id/backgroundLL")
	private List<MobileElement> scheduleGrid;

	@FindBy(id="co.legion.client:id/shiftll")
	private List<MobileElement> scheduleRowGrid;

	@FindBy(id="co.legion.client:id/btn_offer_your_shift")
	private MobileElement btnOfferShift;

	@FindBy(id="co.legion.client:id/swapLL")
	private MobileElement swapShiftRequest;

	@FindBy(id="co.legion.client:id/dropLL")
	private MobileElement coverShiftRequest;

	@FindBy(xpath="//android.widget.TextView[contains(@text,'Swap']")
	private MobileElement swapShiftRequestText;

	@FindBy(xpath="//android.widget.TextView[contains(@text,'Cover']")
	private MobileElement coverShiftRequestText;

	@FindBy(id="co.legion.client:id/tv_toolbar_title")
	private MobileElement unclaimedOfferTitle;

	@FindBy(id="co.legion.client:id/tab1")
	private MobileElement tabSwap;

	@FindBy(id="co.legion.client:id/tab2")
	private MobileElement tabOpenShift;

	@FindBy(id="co.legion.client:id/header_offers_range_date_tv")
	private MobileElement headerRangeDate;

	@FindBy(id="co.legion.client:id/header_offers_count_tv")
	private MobileElement headerOffersCount;

	@FindBy(id="co.legion.client:id/parentLayout")
	private MobileElement openshiftGridCount;

	@FindBy(id="co.legion.client:id/statTimeTV")
	private MobileElement shiftStartTime;

	@FindBy(id="co.legion.client:id/durationTV")
	private MobileElement shiftDuration;

	@FindBy(id="co.legion.client:id/endTimeTV")
	private MobileElement shiftEndTime;

	@FindBy(id="co.legion.client:id/roleTv")
	private MobileElement shiftWorkerRole;

	@FindBy(id="co.legion.client:id/roleTV")
	private MobileElement shiftCoverWorkerRole;

	@FindBy(id="co.legion.client:id/nameTV")
	private MobileElement shiftEnterprise;

	@FindBy(id="co.legion.client:id/addressTV")
	private MobileElement shiftLocation;

	@FindBy(id="co.legion.client:id/topSection")
	private MobileElement imgLegion;

	@FindBy(xpath="//android.widget.ImageButton[@content-desc='Home']")
	private MobileElement iconSideMenu;

	@FindBy(xpath="//android.widget.CheckedTextView[contains(@text,'Logout')]")
	private MobileElement btnLogout;

	@FindBy(id="co.legion.client:id/btNextTv")
	private MobileElement btnNext;

	@FindBy(id="co.legion.client:id/shiftersNameTv")
	private List<MobileElement> shiftSwapRequestorName;

	@FindBy(id="co.legion.client:id/selectedIv")
	private List<MobileElement> radioBtnShiftSwap;

	@FindBy(id="co.legion.client:id/topSelctorLL")
	private List<MobileElement> shiftSwapGrid;

	@FindBy(id="co.legion.client:id/iv_picCircleIv")
	private List<MobileElement> teamMemberWorkingForShift;

	@FindBy(id="co.legion.client:id/allShiftsTv")
	private MobileElement tabAllShift;

	@FindBy(id="co.legion.client:id/outTv")
	private MobileElement tabUnschedule;

	@FindBy(id="co.legion.client:id/profileView")
	private List<MobileElement> listProfileView;

	@FindBy(id="co.legion.client:id/tvWorkerName")
	private List<MobileElement> listWorkerName;

	@FindBy(id="co.legion.client:id/swipeActionTv")
	private MobileElement swipeActionSwap;

	@FindBy(id="co.legion.client:id/dayNameTV1")
	private MobileElement swapRequestorDayName;

	@FindBy(id="co.legion.client:id/roleTv1")
	private MobileElement swapRequestorRole;

	@FindBy(id="co.legion.client:id/addressTV1")
	private MobileElement swapRequestorLocation;

	@FindBy(id="co.legion.client:id/statTimeTV1")
	private MobileElement swapRequestorStartTime;

	@FindBy(id="co.legion.client:id/endTimeTV1")
	private MobileElement swapRequestorEndTime;

	@FindBy(id="co.legion.client:id/returnBt")
	private MobileElement btnReturn;

	@FindBy(xpath="android.widget.TextView[contains(@text,'Swap Request')]")
	private MobileElement successMsgSwapOrCoverRequest;

	@FindBy(id="co.legion.client:id/toolbarBack")
	private MobileElement btnToolBarBack;

	@FindBy(xpath="//android.widget.TextView[contains(@text,'Logout')]")
	private MobileElement btnLogoutSave;

	@FindBy(id="co.legion.client:id/statTimeTV")
	private MobileElement swapReceiverStartTime;

	@FindBy(id="co.legion.client:id/endTimeTV")
	private MobileElement swapReceiverEndTime;

	@FindBy(id="co.legion.client:id/coverShiftHeader")
	private MobileElement coverRequestHeader;

	@FindBy(id="co.legion.client:id/job_desc")
	private MobileElement successMsgOnClaim;

	@FindBy(id="co.legion.client:id/okTv")
	private MobileElement btnOk;

	@FindBy(id="co.legion.client:id/okTv")
	private MobileElement msgApprovalPending;

	@FindBy(id="co.legion.client:id/dateTV")
	private MobileElement shiftDate;


	public MobileLoginPage() {
    	PageFactory.initElements(new AppiumFieldDecorator(getAndroidDriver(), 10, TimeUnit.SECONDS), this);
    }

	@Override
	public void clickFirstLoginBtn() throws Exception {
		// TODO Auto-generated method stub

//		btnSelectLogin.click();
		if(isElementLoadedOnMobile(btnSignIn,60)){
			clickOnMobileElement(btnSignIn);
			SimpleUtils.pass("First Sign In Button clicked Successfully!");
		}else{
			MyThreadLocal.setPlatformName("mobile");
			SimpleUtils.fail("First Sign In Button not clicked Successfully!", false);
		}

	}
	
	public void verifyLoginTitle(String textLogin) throws Exception{
		if(isElementLoadedOnMobile(titleLogin,60)){
			System.out.println("Title login is " +titleLogin.getText());
			boolean bol = titleLogin.getText().equalsIgnoreCase(textLogin);
			System.out.println(bol);
			if(titleLogin.getText().equalsIgnoreCase(textLogin)){
				SimpleUtils.pass("Login Title "+ textLogin + " matches with "+titleLogin);
			}else{
				MyThreadLocal.setPlatformName("mobile");
				SimpleUtils.fail("Login Title "+ textLogin + " not matches with "+titleLogin, true);
			}
		}else{
			SimpleUtils.fail("Login Text not available on page!", true);
		}
	}
	
	public void selectEnterpriseName() throws Exception{
		waitForSeconds(2);
		if(isElementLoadedOnMobile(clickEnterprise)){
			clickOnMobileElement(clickEnterprise);
			waitForSeconds(2);
			getAndroidDriver().findElementByAndroidUIAutomator("new UiSelector().text(\"RC\")").click();
			SimpleUtils.pass("Enterprise Stage clicked Successfully!");
		}else{
			MyThreadLocal.setPlatformName("mobile");
			SimpleUtils.fail("Enterprise Stage not clicked Successfully!",false);
		}
	}
	
	public void loginToLegionWithCredentialOnMobile(String userName, String Password) throws Exception
    {
    	waitForSeconds(2);
    	if(isElementLoadedOnMobile(userNameMobile,60)){
    		userNameMobile.sendKeys(userName);
    		waitForSeconds(3);
    		SimpleUtils.pass("Username entered Successfully!");
			clickFirstLoginBtn();
    	}else{
    		MyThreadLocal.setPlatformName("mobile");
    		SimpleUtils.fail("Username not entered Successfully!",false);
    	}
    	
    	if(isElementLoadedOnMobile(passwordMobile)){
    		passwordMobile.sendKeys(Password);
    		waitForSeconds(3);
    		SimpleUtils.pass("Password entered Successfully!");
    	}else{
    		MyThreadLocal.setPlatformName("mobile");
    		SimpleUtils.fail("Password not entered Successfully!",false);
    	}
    }

	// Click on Sign in button once username and pwd both entered

	public void clickLoginBtn() throws Exception {
		// TODO Auto-generated method stub
		if(isElementLoadedOnMobile(loginBtn,60)){
			clickOnMobileElement(loginBtn);
			SimpleUtils.pass("Sign in Button clicked after entering username and pwd");
		}else{
			MyThreadLocal.setPlatformName("mobile");
			SimpleUtils.fail("Sign In Button not clicked Successfully!", false);
		}

	}



	public void clickOpenShiftOffers(String teamMember) throws Exception{
		waitForSeconds(8);
//		validateOpenShiftInfoFromConsoleAndMobile();
		if(isElementLoadedOnMobile(shiftOffers,20)){
			pressByElement(shiftOffers,2);
			SimpleUtils.pass("Clicked on Shift Offer icon Successfully!");
		}else{
			MyThreadLocal.setPlatformName("mobile");
			SimpleUtils.fail("Shift Offer icon is not clickable", false);
		}

		if(areListElementVisibleOnMobile(openShiftOfferGrid,5)){
			pressByElement(openShiftOfferGrid.get(0),2);
			SimpleUtils.pass("Clicked on Swap Grid Successfully!!");
		}else{
			SimpleUtils.fail("Not able to Click on Shift Grid Successfully!!",false);
		}
	}

	public void clickShiftOffers(String teamMember) throws Exception{
		waitForSeconds(8);
//		validateOpenShiftInfoFromConsoleAndMobile();
		if(isElementLoadedOnMobile(shiftOffers,20)){
			pressByElement(shiftOffers,2);
			SimpleUtils.pass("Clicked on Shift Offer icon Successfully!");
		}else{
			MyThreadLocal.setPlatformName("mobile");
			SimpleUtils.fail("Shift Offer icon is not clickable", false);
		}

	}


	public void validateOpenShiftInfoFromConsoleAndMobile() throws Exception{
		if(areListElementVisibleOnMobile(openShiftOfferGrid,5) &&
				areListElementVisibleOnMobile(shiftOfferDay,5) &&
				areListElementVisibleOnMobile(shiftOfferStartTime,5) &&
				areListElementVisibleOnMobile(shiftOfferEndTime,5)){
			for(int i=0;i<openShiftOfferGrid.size();i++){
				String shiftOfferDays = shiftOfferDay.get(i).getText();
				String shiftOfferStartTimes = shiftOfferStartTime.get(i).getText();
				String shiftOfferEndTimes = shiftOfferEndTime.get(i).getText();
			}
		}else{
			SimpleUtils.fail("Shift offer not available for ",false);
		}
	}


	//added by Nishant for Swap offers

	public void clickOnScheduleTab() throws Exception{
		waitForSeconds(10);
		getAndroidDriver().runAppInBackground(Duration.ofSeconds(2));
//		List<MobileElement> element = getAndroidDriver().findElements(By.id("co.legion.client:id/tab_container"));
		if(areListElementVisibleOnMobile(scheduleTabContainer,20)){
			clickOnMobileElement(scheduleTabContainer.get(1));
			SimpleUtils.pass("Clicked on Schedule tab Successfully!");
			verifyScheduleTitle();
		}else{
			SimpleUtils.fail("Not able to Click on Schedule tab Successfully!", false);
		}
	}


	public void verifyScheduleTitle() throws Exception{
		if(isElementLoadedOnMobile(scheduleTitle,2) &&
				isElementLoadedOnMobile(scheduleDate,2)){
			if(scheduleTitle.getText().toLowerCase().equalsIgnoreCase("My Schedule")){
				SimpleUtils.pass("Schedule title matches Successfully!");
			}else{
				SimpleUtils.fail("Schedule title not matches", true);
			}
		}else{
			SimpleUtils.fail("Schedule title not loaded Successfully!", true);
		}

		if(areListElementVisibleOnMobile(scheduleGrid,2)){
			if(areListElementVisibleOnMobile(scheduleRowGrid,2)){
				SimpleUtils.pass("Schedule available for Swap Or Cover Request");
				pressByElement(scheduleRowGrid.get(0),2);
			}else{
				if(isElementLoadedOnMobile(nextArrowSchedule,2)){
					click(nextArrowSchedule);
					verifyScheduleTitle();
				}
			}

		}else{
			SimpleUtils.fail("Schedule Grid not loaded Successfully!", true);
		}
	}

	public void clickTeamMemberWorkingForShift() throws Exception{
		if(areListElementVisibleOnMobile(teamMemberWorkingForShift,2)){
			clickOnMobileElement(teamMemberWorkingForShift.get(0));
			SimpleUtils.pass("Swap/Cover Offer button clicked Successfully!");
			if(isElementLoadedOnMobile(tabAllShift,2)){
				clickOnMobileElement(tabAllShift);
				clickOnProfieView("","Albert");
			}else{
				SimpleUtils.fail("Swap/Cover Offer button not loaded Successfully!", false);
			}
		}else{
			SimpleUtils.fail("Swap/Cover Offer button not loaded Successfully!", false);
		}
	}

	public void clickOnProfieView(String strRequest, String receiverName) throws Exception{
		if(strRequest!=null && strRequest.equals(strRequest)){
			swipeUp(5,2);
		}
		if(areListElementVisibleOnMobile(listProfileView,2) &&
				areListElementVisibleOnMobile(listWorkerName,2)){
			for(int i=0; i<listWorkerName.size();i++){
				if(listWorkerName.get(i).getText().contains(receiverName)){
					swipeHorizontalByElement(listWorkerName.get(i),1);
					pressByElement(swipeActionSwap,2);
				}
			}
		}else{
			SimpleUtils.fail("Profile view page not loaded Successfully!", false);
		}
	}

	public List<String> getSwapRequestorAndReceiverInfo() throws Exception {
		List<String> swapRequestReceiverInfo = new ArrayList<>();
		if(isElementLoadedOnMobile(scheduleTitle,5)){
			if(scheduleTitle.getText().toLowerCase().equalsIgnoreCase("Submit Swap Request".toLowerCase())){
				SimpleUtils.pass("Schedule Swap request title matches Successfully!");
			}else{
				SimpleUtils.fail("Schedule Swap request title not matches Successfully", true);
			}
		}else{
			SimpleUtils.fail("Schedule Swap request title not loaded Successfully!", true);
		}

		if(isElementLoadedOnMobile(swapRequestorDayName,2)){
			String[] arrSwapRequestorDayName = swapRequestorDayName.getText().split("\n");
			swapRequestReceiverInfo.add(arrSwapRequestorDayName[0] + " " + arrSwapRequestorDayName[1]);
			swapRequestReceiverInfo.add(swapRequestorRole.getText());
			swapRequestReceiverInfo.add(swapRequestorLocation.getText());
//			swapRequestReceiverInfo.add(swapRequestorStartTime.getText());
//			swapRequestReceiverInfo.add(swapRequestorEndTime.getText());
		}else{
			SimpleUtils.fail("Schedule day Name not loaded Successfully!", true);
		}

		if(isElementLoadedOnMobile(shiftDayName,2)){
			String[] arrSwapReceiverDayName = shiftDayName.getText().split("\n");
			swapRequestReceiverInfo.add(arrSwapReceiverDayName[0] + " " + arrSwapReceiverDayName[1]);
			swapRequestReceiverInfo.add(shiftWorkerRole.getText());
			swapRequestReceiverInfo.add(shiftLocation.getText());
//			swapRequestReceiverInfo.add(swapReceiverStartTime.getText());
//			swapRequestReceiverInfo.add(swapReceiverEndTime.getText());
		}else{
			SimpleUtils.fail("Schedule Offer title not loaded Successfully!", true);
		}
		return swapRequestReceiverInfo;
	}

	public void clickSubmitBtn() throws Exception{
		if(isElementLoadedOnMobile(btnNext,5)){
			clickOnMobileElement(btnNext);
			SimpleUtils.pass("Submit button Clicked Successfully!");
		}else{
			SimpleUtils.fail("Not able to click on Submit button", false);
		}
	}

    public void navigateToReturntoHomePage() throws Exception{
		if(isElementLoadedOnMobile(btnReturn,5)){
			SimpleUtils.pass("Able to see Return to My Schedule btn on page");
			clickOnMobileElement(btnReturn);
			if(isElementLoadedOnMobile(tabAllShift,5) ||
					isElementLoadedOnMobile(tabUnschedule,5)){
				SimpleUtils.pass("User is able to return to page of selection of Swap Or Cover Request");
				if(isElementLoadedOnMobile(btnToolBarBack,2)){
					clickOnMobileElement(btnToolBarBack);
					SimpleUtils.pass("Team member working for shift page loaded Successfully!");
					clickOnMobileElement(btnToolBarBack);
					SimpleUtils.pass("Schedule page loaded Successfully!");
					if(areListElementVisibleOnMobile(scheduleTabContainer,5)){
						clickOnMobileElement(scheduleTabContainer.get(0));
						SimpleUtils.pass("Home Page loaded Successfully!!");
					}else{
						SimpleUtils.pass("Home Page not loaded Successfully!!");
					}
				}else{
					SimpleUtils.fail("Team member working for shift page not loaded Successfully!",false);
				}
			}else{
				SimpleUtils.fail("User is able to see the Success message as " + successMsgSwapOrCoverRequest.getText(), true);
			}
		}else{
			SimpleUtils.fail("Return to My Schedule btn not visible on page", false);
		}
	}

	// Verification of Home Page

	public void displayHomePageLoaded() throws Exception{
		if(isElementEnabledOnMobile(iconSideMenu,60)){
			SimpleUtils.pass("Home Page loaded Successfully!!");
			clickOnMobileElement(iconSideMenu);
			SimpleUtils.pass("Clicked on side menu Successfully!!");
		}else{
			SimpleUtils.fail("Home Page does not get loaded Successfully!!", false);
		}
	}

	public void clickLogoutBtn() throws Exception{

		if(isElementEnabledOnMobile(btnLogout,20)){
			clickOnMobileElement(btnLogout);
			SimpleUtils.pass("Clicked on logout button Successfully!!");
			if(isElementEnabledOnMobile(btnLogoutSave,20)){
				clickOnMobileElement(btnLogoutSave);
				SimpleUtils.pass("Clicked on logout save button Successfully!!");
				waitForSeconds(3);
			}else{
				SimpleUtils.fail("Logout Save button does not get clicked Successfully!!", false);
			}
		}else{
			SimpleUtils.fail("Logout button does not get clicked Successfully!!", false);
		}

	}

	public void clickOnSwapLink() throws Exception{
		if(isElementLoadedOnMobile(shiftOffers,20)){
			pressByElement(shiftOffers,2);
			SimpleUtils.pass("Clicked on Swap icon Successfully!!");
			if(isElementLoadedOnMobile(tabSwap,5)){
				clickOnMobileElement(tabSwap);
//				pressByElement(tabSwap,2);
				SimpleUtils.pass("Clicked on Swap tab Successfully!!");
			}else{
				SimpleUtils.fail("Not able to Click on Swap tab!!",false);
			}
		}else{
			SimpleUtils.fail("Not able to Click on Swap icon Successfully!!",false);
		}
	}

	public void clickOnSwapShiftGrid() throws Exception{
		if(areListElementVisibleOnMobile(openShiftOfferGrid,5)){
			pressByElement(openShiftOfferGrid.get(0),2);
			SimpleUtils.pass("Clicked on Swap Grid Successfully!!");
		}else{
			SimpleUtils.fail("Not able to Click on Shift Grid Successfully!!",false);
		}
	}

	public List<String> getRequestorAndReceiverInfoOnSwapTab() throws Exception {
		List<String> requestReceiverInfo = new ArrayList<>();
		if(isElementLoadedOnMobile(shiftDayName,2)){
			String[] arrSwapReceiverDayName = shiftDayName.getText().split("\n");
			requestReceiverInfo.add(arrSwapReceiverDayName[0] + " " + arrSwapReceiverDayName[1]);
//			requestReceiverInfo.add(shiftDayName.getText());
			requestReceiverInfo.add(shiftWorkerRole.getText());
			requestReceiverInfo.add(shiftLocation.getText());
//			requestReceiverInfo.add(swapReceiverStartTime.getText());
//			requestReceiverInfo.add(swapReceiverEndTime.getText());
		}else{
			SimpleUtils.fail("Schedule Offer title not loaded Successfully!", true);
		}
		if(isElementLoadedOnMobile(swapRequestorDayName,2)){
			String[] arrSwapRequestorDayName = swapRequestorDayName.getText().split("\n");
			requestReceiverInfo.add(arrSwapRequestorDayName[0] + " " + arrSwapRequestorDayName[1]);
//			requestReceiverInfo.add(swapRequestorDayName.getText());
			requestReceiverInfo.add(swapRequestorRole.getText());
			requestReceiverInfo.add(swapRequestorLocation.getText());
//			requestReceiverInfo.add(swapRequestorStartTime.getText());
//			requestReceiverInfo.add(swapRequestorEndTime.getText());
		}else{
			SimpleUtils.fail("Schedule Offer title not loaded Successfully!", true);
		}
		return requestReceiverInfo;
	}

	public void clickOnClaimBtn() throws Exception{
		if(isElementEnabledOnMobile(btnShiftClaim,5)){
			pressByElement(btnShiftClaim,2);
			SimpleUtils.pass("Swap/Cover Offer Claimed btn clicked Successfully!");
			if(isElementLoadedOnMobile(alertClaim,5)){
				SimpleUtils.pass("Swap/Cover Claim Alert pop displayed Successfully!");
				clickOnAgreeBtn();
			}else{
				SimpleUtils.fail("Swap/Cover Claim Alert pop not displayed", false);
			}

		}else{
			SimpleUtils.fail("Swap/Cover Claimed btn not clicked Successfully!", false);
		}
	}

	public void clickOfferBtn() throws Exception{
		if(isElementLoadedOnMobile(btnOfferShift,2)){
			click(btnOfferShift);
			SimpleUtils.pass("Swap/Cover Offer button clicked Successfully!");
			clickOnSwapRequestOption();
		}else{
			SimpleUtils.fail("Swap/Cover Offer button not loaded Successfully!", false);
		}
	}

	public void clickOnSwapRequestOption() throws Exception{
		if(isElementLoadedOnMobile(scheduleTitle,5)){
			if(scheduleTitle.getText().toLowerCase().equalsIgnoreCase("Offer your Shift".toLowerCase())){
				SimpleUtils.pass("Schedule Offer title matches Successfully!");
			}else{
				SimpleUtils.fail("Schedule Offer title not matches Successfully", true);
			}
		}else{
			SimpleUtils.fail("Schedule Offer title not loaded Successfully!", true);
		}

		if(isElementLoadedOnMobile(swapShiftRequest,2)){
			pressByElement(swapShiftRequest,1);
			SimpleUtils.pass("Swap Request pressed Successfully!");
		}else{
			SimpleUtils.fail("Swap Request option  not loaded Successfully!", true);
		}
	}

	public void selectSwapRequest() throws Exception{
		if(isElementLoadedOnMobile(scheduleTitle,5)){
			if(scheduleTitle.getText().toLowerCase().equalsIgnoreCase("Find Shifts to Swap".toLowerCase())){
				SimpleUtils.pass("Schedule Offer title matches Successfully!");
			}else{
				SimpleUtils.fail("Schedule Offer title not matches Successfully", true);
			}
		}else{
			SimpleUtils.fail("Schedule Offer title not loaded Successfully!", true);
		}

		if(areListElementVisibleOnMobile(shiftSwapRequestorName,2)){
//			longPressByElement(shiftSwapGrid.get(0),shiftSwapGrid.get(1),2);
			System.out.println(shiftSwapRequestorName.get(0).getText());
			System.out.println(shiftSwapRequestorName.get(1).getText());
		}else{
			SimpleUtils.fail("Schedule Offer title not matches Successfully", true);
		}

	}

	public void clickTeamMemberWorkingForShiftCover() throws Exception{
		if(areListElementVisibleOnMobile(teamMemberWorkingForShift,2)){
			clickOnMobileElement(teamMemberWorkingForShift.get(0));
			SimpleUtils.pass("Clicked on team member image");
			if(isElementLoadedOnMobile(tabUnschedule,2)){
				SimpleUtils.pass("UNSCHEDULED tab loaded on page Successfully!");
				clickOnMobileElement(tabUnschedule);
				clickOnProfieView("Cover","Shifei");
			}else{
				SimpleUtils.fail("UNSCHEDULED tab not loaded on page", false);
			}
		}else{
			SimpleUtils.fail("Team member image is not clickable", false);
		}
	}


	public List<String> getCoverRequestorInfo() throws Exception {
		List<String> coverRequestReceiverInfo = new ArrayList<>();
		if(isElementLoadedOnMobile(scheduleTitle,5)){
			if(scheduleTitle.getText().toLowerCase().equalsIgnoreCase("Select your Shift for cover".toLowerCase())){
				SimpleUtils.pass("Schedule Cover request title " + scheduleTitle.getText() + " loaded Successfully!");
			}else{
				SimpleUtils.fail("Schedule Cover request title " + scheduleTitle.getText() + " not loaded Successfully!", true);
			}
		}else{
			SimpleUtils.fail("Schedule Cover request title not loaded Successfully!", true);
		}

		if(isElementLoadedOnMobile(shiftDayName,2)){
			String[] arrCoverRequestorDayName = shiftDayName.getText().split("\n");
			coverRequestReceiverInfo.add(arrCoverRequestorDayName[0] + " " + arrCoverRequestorDayName[1]);
			coverRequestReceiverInfo.add(shiftCoverWorkerRole.getText());
			coverRequestReceiverInfo.add(shiftLocation.getText());
			coverRequestReceiverInfo.add(shiftStartTime.getText());
			coverRequestReceiverInfo.add(shiftEndTime.getText());
		}else{
			SimpleUtils.fail("Cover Request day Name not loaded Successfully!", true);
		}
		return coverRequestReceiverInfo;
	}


	public List<String> getCoverRequestorInfoOnClaim() throws Exception {
		List<String> coverInfo = new ArrayList<>();
		if(areListElementVisibleOnMobile(openShiftOfferGrid,5)){
			for(int i=0;i<openShiftOfferGrid.size();i++){
				if(isElementLoadedOnMobile(coverRequestHeader,2)){
					if(isElementLoadedOnMobile(shiftDayName,2)){
						coverInfo.add(shiftDayName.getText() + " " + shiftDate.getText());
						coverInfo.add(shiftCoverWorkerRole.getText());
						coverInfo.add(shiftLocation.getText());
						coverInfo.add(shiftStartTime.getText());
						coverInfo.add(shiftEndTime.getText());
						pressByElement(openShiftOfferGrid.get(0),2);
					}else{
						SimpleUtils.fail("Cover Request day Name not loaded Successfully!", true);
					}
				}
			}
		}else{
			SimpleUtils.fail("Cover Request Grid not loaded Successfully!", true);
		}

		return coverInfo;
	}


	public void clickOnAgreeBtn() throws Exception{
		if(isElementLoadedOnMobile(btnAgreeClaim,5)){
			clickOnMobileElement(btnAgreeClaim);
			SimpleUtils.pass("Clicked on I Agree button Successfully!");
			if(isElementLoadedOnMobile(successMsgOnClaim,10)){
				SimpleUtils.pass("Success message is: " + successMsgOnClaim.getText());
			}else{
				SimpleUtils.fail("Success message not loaded!",true);
			}
			if(isElementLoadedOnMobile(btnOk,2)){
				clickOnMobileElement(btnOk);
				SimpleUtils.pass("Clicked on OK button Successfully!");
//				if(isElementLoadedOnMobile(msgApprovalPending,5)){
//					SimpleUtils.pass("Message after claiming is: " + msgApprovalPending.getText());
//				}else{
//					SimpleUtils.fail("Approval pending message not displayed on page",true);
//				}
			}else{
				SimpleUtils.fail("OK button is not clickable!",false);
			}
		}else{
			SimpleUtils.fail("I Agree button is not clickable!",false);
		}
	}

	public void clickOnTooBar() throws Exception{
		if(isElementLoadedOnMobile(btnToolBarBack,5)){
			clickOnMobileElement(btnToolBarBack);
			if(areListElementVisibleOnMobile(scheduleTabContainer,5)){
				clickOnMobileElement(scheduleTabContainer.get(0));
				SimpleUtils.pass("Home Page loaded Successfully!!");
			}else{
				SimpleUtils.pass("Home Page not loaded Successfully!!");
			}
		}
	}

	public void clickOnLegionProfileBtn() throws Exception{
		if(isElementEnabledOnMobile(createProfileBtn,30)){
			clickOnMobileElement(createProfileBtn);
			SimpleUtils.pass("Create Profile Page clicked Successfully!!");
		}else{
			SimpleUtils.fail("Craete Profile Page not clicked Successfully!!",false);
		}
	}

	public void verifyCurrentEmployerPageLanded(String currentEmployerPage) throws Exception{
		if(isElementEnabledOnMobile(txtCurrentEmployerPage,30)){
			if(txtCurrentEmployerPage.getText().equalsIgnoreCase(currentEmployerPage)){
				SimpleUtils.pass("Current Employer Page Title matched!!");
			}else{
				SimpleUtils.fail("Current Employer Page Title did not match!!" + txtCurrentEmployerPage.getText(), true );
			}
		}else{
			SimpleUtils.fail("Employer Page not loaded Successfully!!",false);
		}
	}

	public void enterCompanyIdentifier(String companyIdentifier) throws Exception{
		if(isElementEnabledOnMobile(txtCompanyIdentifier,30)){
			txtCompanyIdentifier.sendKeys(companyIdentifier);
		}else{
			SimpleUtils.fail("Company Identifier text is not enabled",false);
		}
	}

	public void clickOnContinueBtn() throws Exception{
		if(isElementEnabledOnMobile(btnContinue,30)){
			clickOnMobileElement(btnContinue);
			SimpleUtils.pass("Clicked on Continue button Successfully!!");
		}else{
			SimpleUtils.fail("Continue button is not enabled",false);
		}
	}

	public void verifyFoundYourCompany() throws Exception {
		if(isElementEnabledOnMobile(txtCompanyIdentifierMatch,30)){
			if(isElementEnabledOnMobile(btnCont,5)){
				clickOnMobileElement(btnCont);
			}
			else{
				SimpleUtils.fail("Continue button is not enabled",false);
			}
		}else{
			SimpleUtils.fail("Continue identifier is not displayed on the page " + txtCompanyIdentifierMatch.getText(),true);
		}
	}

	public void verifyLegionProfileTodayPageLanded(String profileTodayPage) throws Exception{
		if(isElementEnabledOnMobile(txtLegionProfileToday,30)){
			if(txtLegionProfileToday.getText().equalsIgnoreCase(profileTodayPage)){
				SimpleUtils.pass("Today Legion profile Page Title matched!!");
			}else{
				SimpleUtils.fail("Today Legion profile Page Title did not match!!" + txtLegionProfileToday.getText(), true );
			}
		}else{
			SimpleUtils.fail("Today Legion profile Page not loaded Successfully!!",true);
		}
	}

	public void verifyEmailPageLandedForOnboarding(String valEmail) throws Exception {
		if(areListElementVisibleOnMobile(txtEmail,30)){
			if(isElementEnabledOnMobile(txtEmail.get(0),10)){
				txtEmail.get(0).sendKeys(valEmail);
				SimpleUtils.pass("Emailid has entered successfully!!");
			}else{
				SimpleUtils.fail("Email text is not editable", false);
			}
		}else{
			SimpleUtils.fail("Create Profile to fill up user details not loaded!!",false);
		}

	}

	public void enterPassword(String pwd) throws Exception{
		if(isElementEnabledOnMobile(txtPassword,5)){
			txtPassword.sendKeys(pwd);
			SimpleUtils.pass("Password has entered successfully!!");
		}else{
			SimpleUtils.fail("Password field is not editable",false);
		}
	}

	public void enterConfirmPassword(String confirmPwd, String pwd) throws Exception{
		if(isElementEnabledOnMobile(txtConfirmPassword,5)){
			txtConfirmPassword.sendKeys(confirmPwd);
			SimpleUtils.pass("Confirm Password " + confirmPwd + " matches with given password " + pwd);
		}else{
			SimpleUtils.fail("ConfirmPassword field is not editable",false);
		}
	}

	public void enterFName(String fName) throws Exception{
		if(isElementEnabledOnMobile(txtFName,5)){
			txtFName.sendKeys(fName);
			SimpleUtils.pass("First Name has entered successfully!!");
		}else{
			SimpleUtils.fail("First Name field is not editable",false);
		}
	}

	public void enterLName(String lName) throws Exception{
		if(isElementEnabledOnMobile(txtLName,5)){
			txtLName.sendKeys(lName);
			SimpleUtils.pass("Last Name has entered successfully!!");
		}else{
			SimpleUtils.fail("Last Name field is not editable",false);
		}
	}

	public void verifyLegionTISPageLanded(String legionTIS) throws Exception{
		if(isElementEnabledOnMobile(txtLegionTermsOfServices,120)){
//			System.out.println(txtLegionTermsOfServices.getText());
			if(txtLegionTermsOfServices.getText().equalsIgnoreCase(legionTIS)){
				SimpleUtils.pass("Legion Terms of Service Page Title matched!!");
			}else{
				SimpleUtils.fail("Legion Terms of Service Page Title did not match!!" + txtLegionTermsOfServices.getText(), true );
			}
		}else{
			SimpleUtils.fail("Legion Terms of Service Page not loaded Successfully!!",true);
		}
	}

	public void clickOnTISAgreeBtn() throws Exception{
		waitForSeconds(20);
		swipeUpUntilElementFound(25,1,btnAgree);
		if(isElementEnabledOnMobile(btnAgree,5)){
			clickOnMobileElement(btnAgree);
			SimpleUtils.pass("Clicked on Agree button successfully!!");
		}else{
			SimpleUtils.fail("Agree button is not clickable",false);
		}
	}

}
