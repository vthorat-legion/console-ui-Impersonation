package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.LoginPage;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;

import static com.legion.utils.MyThreadLocal.*;

public class ConsoleLoginPage extends BasePage implements LoginPage {
    //todo Manideep to replace it with Legion Console-UI login page he has
    
    /* Aug 03- Zorang Team- Variables declaration*/
    
    @FindBy(css="input[ng-model=\"username\"]")
    private WebElement userNameField;

    @FindBy(css = "[placeholder='Email or Username']")
	private WebElement newUserNameField;
    
    @FindBy(css="[ng-model='password']")
    private WebElement passwordField;

    @FindBy(css = "[type=password]")
	private WebElement newPasswordInput;
    
    @FindBy(className="login-button-icon")
    private WebElement loginButton;

    @FindBy(css = "[type=submit]")
	private WebElement newSignInBtn;

    @FindBy(css = ".MuiFormHelperText-root")
	private WebElement helperText;
    
    @FindBy(className="fa-sign-out")
    private WebElement logoutButton;
    
    @FindBy(css=".no-left-right-padding")
    private WebElement legionDashboardSection;
    
    @FindBy (css = "div.console-navigation-item-label.Dashboard")
    private WebElement dashboardConsoleName;

	@FindBy(css = "lg-select[search-hint='Search Location'] div.input-faked")
	private WebElement locationSelectorButton;

	public ConsoleLoginPage() {
    	PageFactory.initElements(getDriver(), this);
    }

    /* Aug 03-The below line is commented by Zorang Team and new line is added as required */
    //public void goToDashboardHome() throws Exception {
    public void goToDashboardHome(HashMap<String,String> propertyMap) throws Exception {
    	checkElementVisibility(userNameField);
    	getActiveConsoleName(loginButton);
    	userNameField.sendKeys(propertyMap.get("DEFAULT_USERNAME"));
    	passwordField.sendKeys(propertyMap.get("DEFAULT_PASSWORD"));
		click(loginButton);
    }
    
    public void loginToLegionWithCredential(String userName, String Password) throws Exception
    {
		int retryTime = 0;
		boolean isLoaded = isUserNameInputLoaded();
		while (!isLoaded) {
			getDriver().navigate().refresh();
			isLoaded = isUserNameInputLoaded();
			retryTime = retryTime + 1;
			if (retryTime == 6) {
				SimpleUtils.fail("Login page failed to load after waiting for several minutes!", false);
				break;
			}
		}
		if (isElementLoaded(newSignInBtn, 5)) {
			newUserNameField.clear();
			newUserNameField.sendKeys(userName);
			newUserNameField.sendKeys(Keys.ENTER);
			if (isElementLoaded(newPasswordInput, 3)) {
				newPasswordInput.clear();
				newPasswordInput.sendKeys(Password);
			}
			waitForSeconds(1);
			clickTheElement(newSignInBtn);
			try {
				if (System.getProperty("enterprise").equalsIgnoreCase("vailqacn") || System.getProperty("enterprise").equalsIgnoreCase("cinemark-wkdy")) {
					waitForSeconds(10);
					if (isElementLoaded(newSignInBtn, 5)) {
						if (newSignInBtn.getText().contains("Signing in") ||
							isElementLoaded(getDriver().findElement(By.cssSelector("p.Mui-error")), 5)) {
							getDriver().navigate().refresh();
							waitForSeconds(5);
							if (isElementLoaded(newSignInBtn, 5)) {
								newUserNameField.clear();
								newUserNameField.sendKeys(userName);
								newUserNameField.sendKeys(Keys.ENTER);
								if (isElementLoaded(newPasswordInput, 3)) {
									newPasswordInput.clear();
									newPasswordInput.sendKeys(Password);
								}
								waitForSeconds(1);
								clickTheElement(newSignInBtn);
								waitForSeconds(5);
							}
						}
						if (isElementLoaded(newSignInBtn, 5)) {
							clickTheElement(newSignInBtn);
						}
					}
				}
			} catch (Exception e) {
				// Do nothing
			}
		}
		verifyLegionTermsOfService();
		waitForSeconds(4);
    }

	@Override
	public void switchToOriginalWindow(String handle)  throws Exception {
		for (String chandle : getDriver().getWindowHandles()) {
			if (chandle.equals(handle)) {
				getDriver().switchTo().window(handle);
				break;
			}
		}
	}
	private boolean isUserNameInputLoaded() {
		boolean isLoaded = false;
		try {
			if (isElementLoaded(newUserNameField, 90) || isElementLoaded(userNameField, 90)) {
				isLoaded = true;
			}
		} catch (Exception e) {
			isLoaded = false;
		}
		return isLoaded;
	}
    
    public boolean isLoginDone() throws Exception
    {
    	WebDriverWait tempWait = new WebDriverWait(getDriver(), 20); 
    	try {
    	    tempWait.until(ExpectedConditions.visibilityOf(legionHeaderIcon));
    	    return true;
    	}
    	catch (TimeoutException te) {
    		return false;
    	}
    }

	@FindBy(className = "header-legion-icon")
	private WebElement legionHeaderIcon;
	@Override
	public boolean isLoginSuccess() throws Exception {
		WebDriverWait tempWait = new WebDriverWait(getDriver(), 60);
		try {
			tempWait.until(ExpectedConditions.visibilityOf(legionHeaderIcon));
			return true;
		}
		catch (TimeoutException te) {
			return false;
		}
	}
    
    public void logOut() throws Exception
    {
    	if(isElementLoaded(logoutButton, 10))
    	{
    		clickTheElement(logoutButton);
    	}
    }
    
    public void verifyLoginDone(boolean isLoginDone, String selectedLocation) throws Exception
    {
    	if(isLoginDone){
            getActiveConsoleName(dashboardConsoleName);
            setConsoleWindowHandle(getDriver().getWindowHandle());
            waitForSeconds(5);
            if (isElementLoaded(locationSelectorButton, 30) && locationSelectorButton.getText().contains(selectedLocation)) {
				SimpleUtils.pass("Login to Legion Application " + displayCurrentURL() + " Successfully with selected location: '" + selectedLocation + "'.");
			} else {
				SimpleUtils.fail("Not able to select the location: " + selectedLocation + " Successfully!",false);
			}
    	}else{
    		SimpleUtils.fail("Not able to Login to Legion Application Successfully!",false);
    	}
    	
    }
    
    //added methods just for POC
    public void goToDashboardHomePage(String username, String pwd) throws Exception {
    	checkElementVisibility(userNameField);
    	getActiveConsoleName(loginButton);
    	userNameField.sendKeys(username);
    	passwordField.sendKeys(pwd);
		click(loginButton);
    }

    public void getActiveConsoleName(WebElement element){
    	activeConsoleName = element.getText();
    	System.out.println(activeConsoleName);
    	setScreenshotConsoleName(activeConsoleName);
    }

	@FindBy(css = "button.btn.sch-publish-confirm-btn")
	private WebElement continueBtnInNewTermsOfServicePopUpWindow;

	@FindBy(css = "div.modal-dialog")
	private WebElement newTermsOfServicePopUpWindow;

	@Override
	public void verifyNewTermsOfServicePopUp() throws Exception {
		if (isElementLoaded(newTermsOfServicePopUpWindow,5)
				&& isElementLoaded(continueBtnInNewTermsOfServicePopUpWindow,5)) {
			click(continueBtnInNewTermsOfServicePopUpWindow);
		}else
			SimpleUtils.report("There is no new terms of service");
	}


	@FindBy(css = "div[class=\"auth-form\"]")
	private WebElement loginPanel;
	@Override
	public void verifyLoginPageIsLoaded() throws Exception {
		try{
			if (isElementLoaded(newUserNameField,5) && isElementLoaded(newSignInBtn, 5)) {
				SimpleUtils.pass("Login page is loaded successfully! ");
			}else
				SimpleUtils.fail("Login page not loaded successfully!", false);
		}catch(Exception e){
			SimpleUtils.fail(e.getMessage(), false);
		}

	}


	@FindBy(css = "div.create-account")
	private WebElement createAccountMessage;

	@FindBy(css = "[ng-click=\"createAccount()\"]")
	private WebElement signUpLink;

	@FindBy(css = "div[class=\"user-onboarding-content\"]")
	private WebElement createAccountPanel;

	@FindBy(css = "input[placeholder=\"Last name\"]")
	private WebElement lastNameInput;

	@FindBy(css = "input[placeholder=\"Invitation code\"]")
	private WebElement invitationCodeInput;

	@FindBy(css = "button[ng-click=\"checkCodeAndLastName()\"]")
	private WebElement verifyButton;

	@FindBy(css = "div.lg-toast")
	private WebElement errorToast;

	@FindBy(css = "[ng-click=\"confirmEnteredEmail()\"]")
	private WebElement createAccountButton;

	@FindBy(css = "[placeholder=\"Email\"]")
	private WebElement emailInput;

	@FindBy(css = "[placeholder=\"Password\"]")
	private WebElement passwordInput;

	@FindBy(css = "[placeholder=\"Confirm Password\"]")
	private WebElement confirmPasswordInput;


	@Override
	public void verifyCreateAccountMessageDisplayCorrectly() throws Exception {
		if (isElementLoaded(createAccountMessage,15)
				&& createAccountMessage.getText().equalsIgnoreCase("Don't have an account? Sign Up")) {
			SimpleUtils.pass("Create Account Message display correctly! ");
		}else
			SimpleUtils.fail("Create Account Message display incorrectly!", false);
	}

	@Override
	public void clickSignUpLink() throws Exception {
		if (isElementLoaded(signUpLink,15)) {
			clickTheElement(signUpLink);
			SimpleUtils.pass("Click Sign Up link successfully! ");
		}else
			SimpleUtils.fail("Click Sign Up link fail to loaded!", false);
	}

	@Override
	public boolean isVerifyLastNameAndInvitationCodePageLoaded() throws Exception {
		boolean isVerifyLastNameAndInvitationCodePageLoaded = false;
		if (isElementLoaded(createAccountPanel,15)) {
			isVerifyLastNameAndInvitationCodePageLoaded = true;
			SimpleUtils.report("Verify Last Name And Invitation Code Page is loaded successfully! ");
		}else
			SimpleUtils.fail("Verify Last Name And Invitation Code Page fail to loaded!", false);
		return isVerifyLastNameAndInvitationCodePageLoaded;
	}

	@Override
	public void verifyLastNameAndInvitationCode(String lastName, String invitationCode) throws Exception {

		if (isElementLoaded(lastNameInput,15)
				&& isElementLoaded(invitationCodeInput, 15)
				&& isElementLoaded(verifyButton, 15)) {
			lastNameInput.clear();
			lastNameInput.sendKeys(lastName);
			invitationCodeInput.clear();
			invitationCodeInput.sendKeys(invitationCode);
			clickTheElement(verifyButton);

			SimpleUtils.pass("Verify last name and invitation code successfully! ");
		}else
			SimpleUtils.fail("Create Account page fail to loaded!", false);
	}

	@Override
	public boolean isErrorToastLoaded() throws Exception {
		boolean isErrorToastLoaded = false;
		if (isElementLoaded(errorToast,5)) {
			if(errorToast.getText().equals("Error! Last name or Invitation code is incorrect")){
				isErrorToastLoaded = true;
				SimpleUtils.pass("Error toast is loaded successfully! ");
			} else
				SimpleUtils.fail("Error toast is loaded successfully! ", false);
		}else
			SimpleUtils.fail("Error toast failed to load!", false);
		return isErrorToastLoaded = true;
	}

	@Override
	public boolean isCreateAccountPageLoaded() throws Exception {
		boolean isCreateAccountPageLoaded = false;
		if (isElementLoaded(createAccountButton,5)
				&& isElementLoaded(emailInput, 5)
				&& isElementLoaded(passwordInput, 5)
				&& isElementLoaded(confirmPasswordInput, 5)) {
			isCreateAccountPageLoaded = true;
			SimpleUtils.report("Create Account page is loaded successfully! ");
		}else
			SimpleUtils.report("Create Account page fail to loaded!");
		return isCreateAccountPageLoaded;
	}

	@FindBy(css="div.invalid-login")
	private WebElement invalidLoginError;
	@Override
	public boolean isInvalidLoginErrorShowing() throws Exception{
		boolean flag = false;
		if(isElementLoaded(invalidLoginError,2)){
			flag = true;
		}else {
			flag = false;
		}
		return flag;
	}
	@Override
	public void refreshLoginPage() throws Exception {
		if (isElementLoaded(invalidLoginError, 5)) {
			getDriver().get(getDriver().getCurrentUrl());
			if (isElementLoaded(loginPanel, 15)
					&& isElementLoaded(userNameField, 5)
					&& isElementLoaded(passwordField, 5)
					&& isElementLoaded(loginButton, 5)) {
				SimpleUtils.pass("Refresh page successfully!");
			} else {
				SimpleUtils.fail("Can't Refresh page successfully!", false);
			}
		} else {
			SimpleUtils.report("There is no error showing on login page");
		}
	}
	@FindBy(xpath = "//div[@data-testid='tos-text']")
	private WebElement legionTermsOfService;

	@FindBy(css = "[data-testid=\"accept-btn\"]")
	private WebElement legionTermsOfServiceAgreeButton;

	@Override
	public void verifyLegionTermsOfService() throws Exception {
		if (isElementLoaded(legionTermsOfService,5)
				&& isElementLoaded(legionTermsOfServiceAgreeButton, 5)) {
			int paragraphSize = legionTermsOfService.findElements(By.xpath("./p")).size();
			getDriver().executeScript("arguments[0].scrollIntoView()", legionTermsOfService.findElement(By.xpath("./p["+paragraphSize+"]")));
			waitForSeconds(3);
			clickTheElement(legionTermsOfServiceAgreeButton);
		}else
			SimpleUtils.report("There is no Legion Terms Of Service!");
	}

	@FindBy(css = "button[data-_pendo-button-primarybutton-1]")
	private WebElement noButtonOnIntroductionMode;
	@Override
	public void closeIntroductionMode() throws Exception {
		if (isElementLoaded(noButtonOnIntroductionMode,5)) {
			clickTheElement(noButtonOnIntroductionMode);
		}else
			SimpleUtils.report("There is no Introduction mode display!");
	}
}
