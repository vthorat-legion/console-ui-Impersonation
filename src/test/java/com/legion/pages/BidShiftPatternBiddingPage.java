package com.legion.pages;

public interface BidShiftPatternBiddingPage {
    public boolean checkIfTheShiftBiddingWidgetLoaded() throws Exception;
    public void clickSubmitBidButton() throws Exception;
    public void clickNextButton() throws Exception;
    public void addAllShiftPatterns() throws Exception;
    public void clickSubmitButton() throws Exception;
    public void addSpecificShiftPattern(String workRole, String shiftPatternName) throws Exception;
    public void rankSelectedShiftPattern(String workRole, String shiftPatternName, int rank);
}
