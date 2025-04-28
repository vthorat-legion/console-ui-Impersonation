package com.legion.pages.mobile;

import java.util.List;

public interface LoginPageAndroid {

	public void clickFirstLoginBtn() throws Exception;
	public void verifyLoginTitle(String textLogin) throws Exception;
	public void selectEnterpriseName() throws Exception;
	public void loginToLegionWithCredentialOnMobile(String userName, String Password) throws Exception;
	public void clickShiftOffers(String teamMember) throws Exception;
	public void clickOnScheduleTab() throws Exception;
	public void clickOfferBtn() throws Exception;
	public void selectSwapRequest() throws Exception;
	public void clickTeamMemberWorkingForShift() throws Exception;
	public List<String> getSwapRequestorAndReceiverInfo() throws Exception;
	public void clickSubmitBtn() throws Exception;
	public void navigateToReturntoHomePage() throws Exception;
	public void clickLogoutBtn() throws Exception;
	public void clickLoginBtn() throws Exception;
	public void clickOnSwapLink() throws Exception;
	public void clickOnSwapShiftGrid() throws Exception;
	public List<String> getRequestorAndReceiverInfoOnSwapTab() throws Exception;
	public void clickOnClaimBtn() throws Exception;
	public void clickTeamMemberWorkingForShiftCover() throws Exception;
	public List<String> getCoverRequestorInfo() throws Exception;
	public List<String> getCoverRequestorInfoOnClaim() throws Exception;
	public void clickOnTooBar() throws Exception;
	public void clickOpenShiftOffers(String teamMember) throws Exception;
	public void displayHomePageLoaded() throws Exception;
	public void clickOnLegionProfileBtn() throws Exception;
	public void verifyCurrentEmployerPageLanded(String currentEmployerPage) throws Exception;
	public void enterCompanyIdentifier(String companyIdentifier) throws Exception;
	public void clickOnContinueBtn() throws Exception;
	public void verifyFoundYourCompany() throws Exception;
	public void verifyLegionProfileTodayPageLanded(String profileTodayPage) throws Exception;
	public void verifyEmailPageLandedForOnboarding(String valEmail) throws Exception;
	public void enterPassword(String pwd) throws Exception;
	public void enterConfirmPassword(String confirmPwd, String pwd) throws Exception;
	public void enterFName(String fName) throws Exception;
	public void enterLName(String lName) throws Exception;
	public void verifyLegionTISPageLanded(String legionTIS) throws Exception;
	public void clickOnTISAgreeBtn() throws Exception;
}
