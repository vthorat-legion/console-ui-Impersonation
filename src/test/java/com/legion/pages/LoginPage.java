package com.legion.pages;

import java.util.HashMap;


public interface LoginPage {
    /* Aug 03-The below line is commented by Zorang Team and new line is added as required */
    //public void goToDashboardHome() throws Exception;
    public void goToDashboardHome(HashMap<String,String> propertyMap) throws Exception;
    public void loginToLegionWithCredential(String userName, String Password) throws Exception;
    public boolean isLoginDone() throws Exception;
    public void goToDashboardHomePage(String username, String password) throws Exception;
    public void logOut() throws Exception;
    public void switchToOriginalWindow(String handle) throws Exception;
    public void verifyLoginDone(boolean isLoginDone, String selectedLocation) throws Exception;
    public void verifyNewTermsOfServicePopUp() throws Exception;
    public void verifyLoginPageIsLoaded() throws Exception;
    public void verifyCreateAccountMessageDisplayCorrectly() throws Exception;
    public void clickSignUpLink() throws Exception;
    public boolean isVerifyLastNameAndInvitationCodePageLoaded() throws Exception;
    public void verifyLastNameAndInvitationCode(String lastName, String invitationCode) throws Exception;
    public boolean isErrorToastLoaded() throws Exception;
    public boolean isCreateAccountPageLoaded() throws Exception;
    public boolean isLoginSuccess() throws Exception;
    public void refreshLoginPage() throws Exception;
    public boolean isInvalidLoginErrorShowing() throws Exception;
    public void verifyLegionTermsOfService() throws Exception;
    public void closeIntroductionMode() throws Exception;
}
