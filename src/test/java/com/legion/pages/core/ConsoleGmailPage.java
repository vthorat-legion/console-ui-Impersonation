package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.GmailPage;
import com.legion.utils.JsonUtil;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleGmailPage extends BasePage implements GmailPage {

    private static HashMap<String, String> gmailCredentials = JsonUtil.getPropertiesFromJsonFile("src/test/resources/GmailUserCredential.json");
    public ConsoleGmailPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy (css = "input[type=\"email\"]")
    private WebElement emailInput;
    @FindBy (id = "identifierNext")
    private WebElement nextButton;
    @FindBy (css = "input[id=\"okta-signin-username\"]")
    private WebElement userNameInput;
    @FindBy (css = "input[id=\"okta-signin-password\"]")
    private WebElement pwdInput;
    @FindBy (id = "okta-signin-submit")
    private WebElement signInButton;
    @FindBy (css = "input[name=\"answer\"]")
    private WebElement answerInput;
    @FindBy (className = "button-primary")
    private WebElement verifyButton;
    @FindBy (css = "div.qhFLie>div[role=\"button\"]")
    private WebElement continueButton;
    @FindBy (css = "div.TN.bzz.aHS-bnt")
    private WebElement inboxTab;
    @FindBy (css = "div.asf.T-I-J3.J-J5-Ji")
    private WebElement refreshButton;
    @FindBy (css = "tr.zA.zE")
    private List<WebElement> unReadEmails;
    @FindBy (css = "div[style*=\"background-color:#ffffff\"]>span")
    private WebElement invitationCode;

    @Override
    public void loginToGmailWithCredential() throws Exception {
        openNewURLOnNewTab(gmailCredentials.get("GMAIL_URL"));
        isLoginPageLoaded();
        inputTheEmailIDAndNext(gmailCredentials.get("EMAIL_ID"));
        isLegionOKTAPageLoaded();
        loginToLegionOKTAWithCredential(gmailCredentials.get("EMAIL_ID"), gmailCredentials.get("PASSWORD"));
        isSecurityQuestionPageLoaded();
        verifyTheAnswer(gmailCredentials.get("ANSWER"));
        if (isElementLoaded(continueButton, 60)){
            click(continueButton);
        }
        isGmailContentPageLoaded();
    }

    @Override
    public boolean isLoginPageLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(emailInput, 30) && isElementLoaded(nextButton, 30)) {
            isLoaded = true;
            SimpleUtils.pass("Gmail Login page is loaded!");
        }else {
            SimpleUtils.fail("Gmail login page failed to load!", false);
        }
        return isLoaded;
    }

    @Override
    public void inputTheEmailIDAndNext(String emailID) throws Exception {
        waitForPageLoaded(getDriver());
        emailInput.sendKeys(emailID);
        click(nextButton);
    }

    @Override
    public boolean isLegionOKTAPageLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(userNameInput, 30) && isElementLoaded(pwdInput, 30) && isElementLoaded(signInButton, 30)){
            isLoaded = true;
            SimpleUtils.pass("Legion OKTA page loaded successfully!");
        }else {
            SimpleUtils.fail("Legion OKTA page failed to load!", false);
        }
        return isLoaded;
    }

    @Override
    public void loginToLegionOKTAWithCredential(String userName, String password) throws Exception {
        waitForPageLoaded(getDriver());
        userNameInput.sendKeys(userName);
        pwdInput.sendKeys(password);
        waitForSeconds(3);
        moveToElementAndClick(signInButton);
    }

    @Override
    public boolean isSecurityQuestionPageLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(answerInput, 60) && isElementLoaded(verifyButton, 60)) {
            isLoaded = true;
            SimpleUtils.pass("Security Question Page loaded successfully!");
        }else {
            SimpleUtils.fail("Security Question Page failed to load!", false);
        }
        return isLoaded;
    }

    @Override
    public void verifyTheAnswer(String answer) throws Exception {
        waitForPageLoaded(getDriver());
        answerInput.sendKeys(answer);
        moveToElementAndClick(verifyButton);
    }

    @Override
    public boolean isGmailContentPageLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(inboxTab, 60) && isElementLoaded(refreshButton, 60)) {
            isLoaded = true;
            SimpleUtils.pass("Gmail Content Page loaded successfully!");
        }else {
            SimpleUtils.fail("Gmail Content Page failed to load!", false);
        }
        return isLoaded;
    }

    @Override
    public void waitUntilInvitationEmailLoaded() throws Exception {
        String css = "tr.zA.zE div.yW span[email=\"no-reply@legion.work\"]";
        waitForElementLoaded(css, 60);
        if (areListElementVisible(unReadEmails, 30)) {
            WebElement sender = getDriver().findElement(By.cssSelector(css));
            if (isElementLoaded(sender, 10)) {
                click(sender);
                SimpleUtils.pass("Email is received!");
            }else{
                SimpleUtils.fail("Email isn't received!", false);
            }
        }else{
            SimpleUtils.fail("There is no unread emails!", false);
        }
    }

    @Override
    public void verifyInvitationCodeIsAvailableOnEmailID() throws Exception {
        String code = null;
        if (isElementLoaded(invitationCode, 30)){
            code = invitationCode.getText();
            if (code.length() == 6) {
                try{
                    Integer.valueOf(code);
                    SimpleUtils.pass("Verified Invitation code is in the email, Invitation Code: " + code);
                }catch (Exception ex) {
                    SimpleUtils.fail("Invitation Code is not consistent with digit!", false);
                }
            }else{
                SimpleUtils.fail("Invitation Code should consistent of 6 digits.", false);
            }
        }else {
            SimpleUtils.fail("Invitation Code doesn't show in the email!", false);
        }
    }
}
