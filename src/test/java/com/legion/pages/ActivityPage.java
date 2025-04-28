package com.legion.pages;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ActivityPage{
    public void verifyActivityBellIconLoaded() throws Exception;
    public void verifyClickOnActivityIcon() throws Exception;
    public void clickActivityFilterByIndex(int index, String filterName) throws Exception;
    public void verifyNewShiftSwapCardShowsOnActivity(String requestUserName, String respondUserName, String actionLabel,
                                                      boolean isNewLabelShows, String location) throws Exception;
    public void approveOrRejectShiftSwapRequestOnActivity(String requestUserName, String respondUserName, String action) throws Exception;
    public void verifyActivityOfPublishSchedule(String requestUserName) throws Exception;
    public void verifyClickOnActivityCloseButton() throws Exception;
    public void verifyActivityOfUpdateSchedule(String requestUserName)throws Exception;
    public boolean isActivityBellIconLoaded() throws Exception;
    public void verifyTheContentOnActivity() throws Exception;
    public void verifyTheContentOfShiftSwapActivity(String location) throws Exception;
    public WebElement verifyNewShiftCoverCardShowsOnActivity(String requestUserName, String respondUserName, String location) throws Exception;
    public void approveOrRejectShiftCoverRequestOnActivity(String requestUserName, String respondUserName, String action, String location) throws Exception;
    public void verifyActivityOfShiftOffer(String requestUserName, String location) throws Exception;
    public void approveOrRejectShiftOfferRequestOnActivity(String requestUserName, String action)throws Exception;
    public void verifyApproveShiftOfferRequestAndGetErrorOnActivity(String requestUserName, String expectedMessage) throws Exception;
    public void verifyFiveActivityButtonsLoaded() throws Exception;
    public void goToProfileLinkOnActivity() throws Exception;
    public boolean isActivityContainerPoppedUp() throws Exception;
    public boolean isApproveRejectBtnsLoaded(int index) throws Exception;
    public void verifyNewWorkPreferencesCardShowsOnActivity(String userName) throws Exception;
    public void verifyNewBusinessProfileCardShowsOnActivity(String userName, boolean isNewLabelShows) throws Exception;
    public void verifyTheNotificationForReqestTimeOff(String requestUserName, String startTime, String endTime,String timeOffAction) throws Exception;
    public void approveOrRejectTTimeOffRequestOnActivity (String requestUserName, String respondUserName, String action) throws Exception;
    public void closeActivityWindow() throws Exception;
    public void verifyNoNotificationForActivateTM() throws Exception;
    public void verifyCancelledMessageOnTheBottomOfTheNotification() throws Exception;
    public void verifyNotificationForUpdateAvailability(String requestName,String isApprovalRequired,String requestOrCancelLabel,String weekInfo,String repeatChange) throws Exception;
    public List<String> getShiftSwapDataFromActivity(String requestUserName, String respondUserName) throws Exception;
    public void approveOrRejectMultipleShiftOfferRequestOnActivity(String requestUserName, String action, int count) throws Exception;
    public HashMap<String, String> getBalanceHrsFromActivity() throws Exception;
    public void clickDetailLinksInActivitiesByIndex(int index) throws Exception;
    public void verifyNewClaimOpenShiftCardShowsOnActivity(String requestUserName, String workRole, String shiftDateAndTime, String location) throws Exception;
    public void verifyNewShiftSwapCardWithTwoLocationsShowsOnActivity(String requestUserName, String respondUserName, String actionLabel,
                                                                      boolean isNewLabelShows, String location1, String location2) throws Exception;
    public void verifyNewClaimOpenShiftGroupCardShowsOnActivity(int requestNum, String requestUserName, String workRole, String shiftDateAndTime, String location) throws Exception;
    public void approveOrRejectOpenShiftGroupRequestOnActivity(int requestNum, String action) throws Exception;
    public void verifyContentOfOpenShiftGroupRequestOnActivity(String shiftGroupTitle, String shiftExpiryDate, ArrayList<String> specificDate, String shiftStartNEndTime, String workRole, String location) throws Exception;
    public boolean isOpenShiftGroupRequestFolded() throws Exception;
}
