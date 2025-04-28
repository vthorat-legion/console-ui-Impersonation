package com.legion.pages;

public interface NewShiftPatternBiddingPage {
    public void removeOrCancelAllCurrentShiftBidding() throws Exception;
    public int getCurrentShiftBiddingsCount() throws Exception;
    public void clickAddShiftBiddingButton() throws Exception;
    public void selectTheFirstScheduleStartWeek() throws Exception;
    public void clickSaveButtonOnCreateShiftBiddingPage() throws Exception;
    public void setShiftBiddingWindowStartAndEndDateAndTime() throws Exception;
}
