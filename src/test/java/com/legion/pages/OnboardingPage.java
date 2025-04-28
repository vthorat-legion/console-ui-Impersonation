package com.legion.pages;

public interface OnboardingPage {

    public void openOnboardingPage(String invitationCode, String firstName, boolean isRehired, String enterpriseDisplayName);
    public void verifyTheContentOfCreateAccountPage(String firstName, String invitationCode) throws Exception;
    public void verifyLastName(String lastName) throws Exception;
    public boolean isCreateAccountPageLoadedAfterVerifyingLastName() throws Exception;
    public void createAccountForNewHire(String password) throws Exception;
    public void verifyIsEmailCorrectDialogPopup() throws Exception;
    public void clickYesBtnOnIsEmailCorrectDialog() throws Exception;
    public void verifyPleaseVerifyYourEmailPageLoaded() throws Exception;
    public void verifyImportantNoticeFromYourEmployerPageLoaded() throws Exception;
    public void clickOnButtonByLabel(String label) throws Exception;
    public void validateVerifyProfilePageLoaded() throws Exception;
    public void verifySetAvailabilityPageLoaded() throws Exception;
    public void clickOnNextButtonOnSetAvailabilityPage() throws Exception;
    public void verifyThatsItPageLoaded() throws Exception;
    public void clickOnDoneOnThatsItPage() throws Exception;
    public void verifyTheContentOfLoginToYourAccountPage() throws Exception;
    public void verifyRehireLoginToPreviousCredential(String username, String password) throws Exception;
    public void verifyWorkLocationsPageLoaded() throws Exception;
    public void clickOnNextButtonOnWorkLocationsPage() throws Exception;
    public void clickOnBackButtonOnWorkLocationsPage() throws Exception;
    public void setOtherPreferredLocationsToggleOnWorkLocationsPage(String yesOrNo) throws Exception;
}
