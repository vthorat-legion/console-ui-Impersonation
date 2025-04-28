package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.NewShiftPatternBiddingPage;
import com.legion.pages.NewsPage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Month;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;


import java.text.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ConsoleNewShiftPatternBiddingPage extends BasePage implements NewShiftPatternBiddingPage {
    public ConsoleNewShiftPatternBiddingPage() {
        PageFactory.initElements(getDriver(), this);
    }
    @FindBy(css = "[ng-repeat=\"rule in $ctrl.sortedRows\"]")
    private List<WebElement> currentShiftBiddings;
    @FindBy(xpath = "//table/tbody/tr[@class='ng-scope']/td/lg-button[2]/button/span/span")
    private List<WebElement> currentShiftBiddingCancelOrRemoveButtons;

    @Override
    public void removeOrCancelAllCurrentShiftBidding() throws Exception {
        if (areListElementVisible(currentShiftBiddings, 5)
                && areListElementVisible(currentShiftBiddingCancelOrRemoveButtons, 5)) {
            while (currentShiftBiddingCancelOrRemoveButtons.size() >0){
                scrollToElement(currentShiftBiddingCancelOrRemoveButtons.get(0));
                clickTheElement(currentShiftBiddingCancelOrRemoveButtons.get(0));
            }
        } else {
            SimpleUtils.report("There is no current shift bidding!");
        }
    }


    @Override
    public int getCurrentShiftBiddingsCount() throws Exception {
        int count = 0;
        if (areListElementVisible(currentShiftBiddings, 5)) {
            count = currentShiftBiddings.size();
            SimpleUtils.pass("Get current shift bidding count successfully, the count is:"+count);
        } else {
            SimpleUtils.report("There is no current shift bidding!");
        }
        return count;
    }

    @FindBy(css = "lg-button[label=\"Add\"] span span")
    private WebElement addShiftBiddingButton;

    @Override
    public void clickAddShiftBiddingButton() throws Exception {
        if (isElementLoaded(addShiftBiddingButton, 5)) {
            clickTheElement(addShiftBiddingButton);
            SimpleUtils.pass("Click Add shift bidding button successfully! ");
        } else {
            SimpleUtils.fail("Add shift bidding button fail to load!", false);
        }
    }

    @FindBy(css = "[class=\"lg-single-calendar-date ng-scope\"]")
    private List<WebElement> availableBiddingDates;

    @FindBy(xpath = "//lg-picker-input[contains(@placeholder,'Please select a date')]/div/input-field")
    private List<WebElement> shiftBiddingWindowDateInputs;

    @FindBy(css = ".lg-new-time-input-text input")
    private List<WebElement> shiftBiddingWindowTimeInputs;

    @FindBy(css = "[ng-click=\"$ctrl.changeMonth(1)\"]")
    private List<WebElement> goToNextMonthButtons;


    @Override
    public void setShiftBiddingWindowStartAndEndDateAndTime() throws Exception {


        if (areListElementVisible(shiftBiddingWindowDateInputs, 5)
                && areListElementVisible(shiftBiddingWindowTimeInputs, 5)){
            //Set shift bidding window end date
            click(shiftBiddingWindowDateInputs.get(1));
            if (availableBiddingDates.size()==2){
                click(goToNextMonthButtons.get(1));
                waitForSeconds(2);
                click(availableBiddingDates.get(1));
            } else{
                click(availableBiddingDates.get(availableBiddingDates.size()/2+1));
            }

            //Set shift bidding window start date
            click(shiftBiddingWindowDateInputs.get(0));
            click(availableBiddingDates.get(0));

            //Set shift bidding window start time
            shiftBiddingWindowTimeInputs.get(0).clear();
            String biddingWindowStartTime = getCurrentUTCTime();
            shiftBiddingWindowTimeInputs.get(0).sendKeys(biddingWindowStartTime);
            //Set shift bidding window end time
        } else
            SimpleUtils.fail("The shift bidding window date inputs or time inputs fail to load! ", false);
    }

    private String getCurrentUTCTime(){
        //Get UTC current time
        // 1. Get locale time
        Calendar cal = Calendar.getInstance();
        // 2. Obtain time offset
        int zoneoffset = cal.get(Calendar.ZONE_OFFSET);
        // 3. Obtain daylight saving time difference:
        int dstoffset = cal.get(Calendar.DST_OFFSET);
        // 4. By deducting these differences from local time, the UTC time can be obtained:
        cal.add(Calendar.MILLISECOND, -(zoneoffset + dstoffset));
        //Add 2 minutes
        cal.add(Calendar.MINUTE,2);
        //Obtain Month Day, Year
        String cDate=new SimpleDateFormat("MMMMdd,YYYY").format(cal.getTime());
        //Get hour:minute AM/PM
        String cTime=new SimpleDateFormat("KK:mmaa", Locale.ENGLISH).format(cal.getTime());
        return cTime;
    }

    @FindBy(css = "[class=\"calendar-week select-week\"]")
    private List<WebElement> availableToSelectScheduleStartWeeks;

    @FindBy(css = "[pendo-id=\"set_schedule_week\"] input-field")
    private WebElement selectScheduleStartWeeksInput;

    @FindBy(css = "i.next-month")
    private WebElement nextMonthIcon;

    @Override
    public void selectTheFirstScheduleStartWeek() throws Exception {
        if (isElementLoaded(selectScheduleStartWeeksInput, 5)){
            scrollToElement(selectScheduleStartWeeksInput);
            click(selectScheduleStartWeeksInput);
            if (!areListElementVisible(availableToSelectScheduleStartWeeks, 5)){
                clickTheElement(nextMonthIcon);
            }
            if (areListElementVisible(availableToSelectScheduleStartWeeks, 5)){
                clickTheElement(availableToSelectScheduleStartWeeks.get(0));
            } else
                SimpleUtils.fail("There is no available weeks can be selected", false);
        } else
            SimpleUtils.fail("The set schedule week input fail to load! ", false);
    }



    @FindBy(css = "[label=\"Save\"] button:not([disabled])")
    private WebElement saveButton;
    @Override
    public void clickSaveButtonOnCreateShiftBiddingPage() throws Exception {
        if (isElementLoaded(saveButton, 5)){
            clickTheElement(saveButton);
            SimpleUtils.pass("Click Save button successfully! ");
        } else
            SimpleUtils.fail("The save button fail to load on new shift bidding page! ", false);
    }
}
