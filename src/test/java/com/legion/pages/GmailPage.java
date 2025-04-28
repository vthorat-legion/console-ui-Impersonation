package com.legion.pages;

public interface GmailPage {
    public void loginToGmailWithCredential() throws Exception;
    public boolean isLoginPageLoaded() throws Exception;
    public void inputTheEmailIDAndNext(String emailID) throws Exception;
    public boolean isLegionOKTAPageLoaded() throws Exception;
    public void loginToLegionOKTAWithCredential(String userName, String password) throws Exception;
    public boolean isSecurityQuestionPageLoaded() throws Exception;
    public void verifyTheAnswer(String answer) throws Exception;
    public boolean isGmailContentPageLoaded() throws Exception;
    public void waitUntilInvitationEmailLoaded() throws Exception;
    public void verifyInvitationCodeIsAvailableOnEmailID() throws Exception;
}
