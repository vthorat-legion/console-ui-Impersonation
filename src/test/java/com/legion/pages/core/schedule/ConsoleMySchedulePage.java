package com.legion.pages.core.schedule;

import com.legion.pages.*;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.Color;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleMySchedulePage extends BasePage implements MySchedulePage {
    public ConsoleMySchedulePage() {
        PageFactory.initElements(getDriver(), this);
    }

    public enum monthsOfCalendar {
        Jan("January"),
        Feb("February"),
        Mar("March"),
        Apr("April"),
        May("May"),
        Jun("June"),
        Jul("July"),
        Aug("August"),
        Sep("September"),
        Oct("October"),
        Nov("November"),
        Dec("December");
        private final String value;

        monthsOfCalendar(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    @FindBy(css = "div[ng-attr-class^=\"sch-date-title\"]")
    private List<WebElement> weekScheduleShiftsDateOfMySchedule;

    @FindBy (css = "div.day-week-picker-period-week.day-week-picker-period-active")
    private WebElement currentActiveWeek;

    @FindBy(css = ".my-schedule-no-schedule")
    private WebElement myScheduleNoSchedule;
    @FindBy(className = "day-week-picker-arrow-right")
    private WebElement calendarNavigationNextWeekArrow;

    @FindBy(className = "day-week-picker-arrow-left")
    private WebElement calendarNavigationPreviousWeekArrow;

    @FindBy (css = ".day-week-picker-period-week")
    private List<WebElement> currentWeeks;

    @FindBy (className = "period-name")
    private WebElement periodName;

    @FindBy (css = ".shift-swap-modal-table-name")
    private List<WebElement> swapCoverNames;

    @FindBy (css = "[label=\"Close\"]")
    private WebElement closeButton;


    @Override
    public List<String> getCoverTMList() throws Exception {
        List<String> coverTMList = new ArrayList<>();
        if (areListElementVisible(swapCoverNames, 10)) {
            for (WebElement tmName : swapCoverNames) {
                coverTMList.add(tmName.getText());
            }
        }
        clickTheElement(closeButton);
        return coverTMList;
    }

    @Override
    public void validateTheDataAccordingToTheSelectedWeek() throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (isElementLoaded(calendarNavigationPreviousWeekArrow, 10)) {
            scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(), ScheduleTestKendraScott2.weekCount.Two.getValue());
        } else if (isElementLoaded(calendarNavigationNextWeekArrow, 10)) {
            scheduleCommonPage.navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Two.getValue());
        } else
            SimpleUtils.fail("My Schedule Page: Forward and backward button failed to load to view previous or upcoming week", true);
        verifySelectOtherWeeks();
        validateTheScheduleShiftsAccordingToTheSelectedWeek();
    }

    public void validateTheScheduleShiftsAccordingToTheSelectedWeek() throws Exception {
        waitForSeconds(5);
        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20) && weekScheduleShiftsDateOfMySchedule.size() == 7 && isElementLoaded(currentActiveWeek, 25)) {
            String activeWeek = currentActiveWeek.getText();
            String weekScheduleShiftStartDate = weekScheduleShiftsDateOfMySchedule.get(0).getText();
            String weekScheduleShiftEndDate = weekScheduleShiftsDateOfMySchedule.get(6).getText();
            if (activeWeek.contains("\n") && weekScheduleShiftStartDate.contains(",") && weekScheduleShiftStartDate.contains(" ") && weekScheduleShiftEndDate.contains(",") && weekScheduleShiftEndDate.contains(" ") && activeWeek.contains("-")) {
                try {
                    if (weekScheduleShiftStartDate.split(",")[1].trim().split(" ")[1].startsWith("0")) {
                        weekScheduleShiftStartDate = weekScheduleShiftStartDate.split(",")[1].trim().split(" ")[0] + " " + weekScheduleShiftStartDate.split(",")[1].split(" ")[2].substring(1, 2);
                    } else {
                        weekScheduleShiftStartDate = weekScheduleShiftStartDate.split(",")[1].trim();
                    }
                    if (weekScheduleShiftEndDate.split(",")[1].trim().split(" ")[1].startsWith("0")) {
                        weekScheduleShiftEndDate = weekScheduleShiftEndDate.split(",")[1].trim().split(" ")[0] + " " + weekScheduleShiftEndDate.split(",")[1].split(" ")[2].substring(1, 2);
                    } else {
                        weekScheduleShiftEndDate = weekScheduleShiftEndDate.split(",")[1].trim();
                    }
                    activeWeek = activeWeek.split("\n")[1];
                    SimpleUtils.report("weekScheduleShiftStartDate is " + weekScheduleShiftStartDate);
                    SimpleUtils.report("weekScheduleShiftEndDate is " + weekScheduleShiftEndDate);
                    SimpleUtils.report("activeWeek is " + activeWeek);
                    if (weekScheduleShiftStartDate.equalsIgnoreCase(activeWeek.split("-")[0].trim()) && weekScheduleShiftEndDate.equalsIgnoreCase(activeWeek.split("-")[1].trim())) {
                        SimpleUtils.pass("My Schedule Page: The schedule shifts show according to the selected week successfully");
                    } else
                        SimpleUtils.fail("My Schedule Page: The schedule shifts failed to show according to the selected week", true);
                } catch (Exception e) {
                    SimpleUtils.fail("My Schedule Page: The schedule shifts texts don't have enough length ", true);
                }
            }
        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
        } else {
            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
        }
    }

    @Override
    public void verifySelectOtherWeeks() throws Exception {
        String currentWeekPeriod = "";
        String weekDate = "";
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                click(currentWeeks.get(i));
                if (isElementLoaded(periodName, 5)) {
                    currentWeekPeriod = periodName.getText().length() > 12 ? periodName.getText().substring(12) : "";
                }
                if (currentWeeks.get(i).getText().contains("\n")) {
                    weekDate = currentWeeks.get(i).getText().split("\n").length >= 2 ? currentWeeks.get(i).getText().split("\n")[1] : "";
                    if (weekDate.contains("-")) {
                        String[] dates = weekDate.split("-");
                        String shortMonth1 = dates[0].trim().substring(0, 3);
                        String shortMonth2 = dates[1].trim().substring(0, 3);
                        String fullMonth1 = getFullMonthName(shortMonth1);
                        String fullMonth2 = getFullMonthName(shortMonth2);
                        weekDate = weekDate.replaceAll(shortMonth1, fullMonth1);
                        if (!shortMonth1.equalsIgnoreCase(shortMonth2)) {
                            weekDate = weekDate.replaceAll(shortMonth2, fullMonth2);
                        }
                    }
                }
                if (weekDate.trim().equalsIgnoreCase(currentWeekPeriod.trim())) {
                    SimpleUtils.pass("Selected week is: " + currentWeeks.get(i).getText() + " and current week is: " + currentWeekPeriod);
                }else {
                    SimpleUtils.fail("Selected week is: " + currentWeeks.get(i).getText() + " but current week is: " + currentWeekPeriod, false);
                }
                if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
                    click(calendarNavigationNextWeekArrow);
                    verifySelectOtherWeeks();
                }
            }
        }else {
            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
        }
    }


    public String getFullMonthName(String shortName) {
        String fullName = "";
        monthsOfCalendar[] shortNames = monthsOfCalendar.values();
        for (int i = 0; i < shortNames.length; i++) {
            if (shortNames[i].name().equalsIgnoreCase(shortName)) {
                fullName = shortNames[i].value;
                SimpleUtils.report("Get the full name of " + shortName + ", is: " + fullName);
                break;
            }
        }
        return fullName;
    }

    @FindBy(css = ".sch-day-view-shift")
    private List<WebElement> dayViewAvailableShifts;

    @FindBy(css = "h1[ng-if=\"weeklyScheduleData.hasSchedule !== 'FALSE'\"]")
    private WebElement openShiftData;
    @Override
    public void validateTheNumberOfOpenShifts() throws Exception {
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        if (smartCardPage.isViewShiftsBtnPresent()) {
            if (areListElementVisible(dayViewAvailableShifts, 10)) {
                if (dayViewAvailableShifts.size() == Integer.valueOf(openShiftData.getText().replaceAll("[^0-9]", "")))
                    SimpleUtils.pass("My Schedule Page: The total number of open shifts in smartcard matches with the open shifts available in the schedule table successfully");
                else
                    SimpleUtils.fail("My Schedule Page: The total number of open shifts in smartcard doesn't match with the open shifts available in the schedule table", true);
            } else SimpleUtils.fail("My Schedule Page: Open shifts failed to load in the schedule table", true);
        }
    }

    @FindBy(className = "sch-worker-popover")
    private WebElement popOverLayout;

    @FindBy(className = "modal-dialog")
    private WebElement claimConfirmPopup;

    @FindBy(css = "[ng-click=\"accept()\"]")
    private WebElement acceptBtn;

    @Override
    public void verifyTheAvailabilityOfClaimOpenShiftPopup() throws Exception {
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        List<String> claimShift = new ArrayList<>(Arrays.asList("View Offer"));
        if (smartCardPage.isViewShiftsBtnPresent()) {
            if (areListElementVisible(dayViewAvailableShifts, 10)) {
                int randomIndex = (new Random()).nextInt(dayViewAvailableShifts.size());
                moveToElementAndClick(dayViewAvailableShifts.get(randomIndex).findElement(By.cssSelector(".sch-day-view-shift-worker-detail")));
                if (isPopOverLayoutLoaded()) {
                    if (verifyShiftRequestButtonOnPopup(claimShift))
                        SimpleUtils.pass("My Schedule Page: A popup to claim the open shift shows successfully");
                    else SimpleUtils.fail("My Schedule Page: A popup to claim the open shift doesn't show", false);
                } else SimpleUtils.fail("My Schedule Page: No popup appears", false);
            } else SimpleUtils.fail("My Schedule Page: Open shifts failed to load in the schedule table", true);
        }
    }


    public boolean isPopOverLayoutLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(popOverLayout, 20)) {
            isLoaded = true;
            SimpleUtils.pass("Pop over layout loaded Successfully!");
        }
        return isLoaded;
    }

    @FindBy(css = "span.sch-worker-action-label")
    private List<WebElement> shiftRequests;
    @Override
    public boolean verifyShiftRequestButtonOnPopup(List<String> requests) throws Exception {
        boolean isConsistent = false;
        List<String> shiftRequestsOnUI = new ArrayList<>();
        if (areListElementVisible(shiftRequests, 5)) {
            for (WebElement shiftRequest : shiftRequests) {
                shiftRequestsOnUI.add(shiftRequest.getText());
            }
        }
        if (shiftRequestsOnUI.containsAll(requests) && requests.containsAll(shiftRequestsOnUI)) {
            isConsistent = true;
            SimpleUtils.pass("Shift Requests loaded Successfully!");
        }
        return isConsistent;
    }

    // Added by Nora: for Team Member View
    @FindBy(className = "sch-day-view-shift-worker-detail")
    private List<WebElement> tmIcons;
    @FindBy(css = "div.lg-modal")
    private WebElement popUpWindow;
    @FindBy(className = "lg-modal__title-icon")
    private WebElement popUpWindowTitle;
    @FindBy(css = "[label=\"Cancel\"]")
    private WebElement cancelButton;
    @FindBy(css = "[label=\"Submit\"]")
    private WebElement submitButton;
    @FindBy(css = "[label=\"Back\"]")
    private WebElement backButton;
    @FindBy(css = "textarea[placeholder]")
    private WebElement messageText;
    @FindBy(className = "lgn-alert-modal")
    private WebElement confirmWindow;
    @FindBy(className = "lgn-action-button-success")
    private WebElement okBtnOnConfirm;
    @FindBy(css = "[ng-repeat*=\"shift in results\"]")
    private List<WebElement> comparableShifts;
    @FindBy(css = "[label=\"Next\"] button")
    private WebElement nextButton;
    @FindBy(css = "[label=\"Cancel Cover Request\"]")
    private WebElement cancelCoverBtn;
    @FindBy(css = "[label=\"Cancel Swap Request\"]")
    private WebElement cancelSwapBtn;
    @FindBy(css = ".shift-swap-modal-table th.ng-binding")
    private WebElement resultCount;
    @FindBy(css = "td.shift-swap-modal-shift-table-select>div")
    private List<WebElement> selectBtns;
    @FindBy(css = ".modal-content .sch-day-view-shift-outer")
    private List<WebElement> swapRequestShifts;
    @FindBy(css = "[label=\"Accept\"] button")
    private List<WebElement> acceptButtons;
    @FindBy(css = "[label=\"I agree\"]")
    private WebElement agreeButton;
    @FindBy(css = "lg-close.dismiss")
    private WebElement closeDialogBtn;
    @FindBy(className = "shift-swap-offer-time")
    private WebElement shiftOfferTime;
    @FindBy(className = "shift-swap-modal-table-shift-status")
    private List<WebElement> shiftStatus;
    @FindBy (css = ".week-schedule-shift .week-schedule-shift-wrapper")
    private List<WebElement> wholeWeekShifts;
    @FindBy (className = "period-name")
    private WebElement weekPeriod;
    @FindBy (className = "card-carousel-card")
    private List<WebElement> smartCards;
    @FindBy (className = "card-carousel-link")
    private List<WebElement> cardLinks;
    @FindBy (css = "[src*=\"print.svg\"]")
    private WebElement printIcon;
    @FindBy (css = "[src*=\"No-Schedule\"]")
    private WebElement noSchedule;
    @FindBy(css = "[ng-repeat=\"opt in opts\"]")
    private List<WebElement> filters;
    @FindBy(css = ".accept-shift")
    private WebElement claimShiftWindow;
    @FindBy(css = ".redesigned-button-ok")
    private WebElement agreeClaimBtn;
    @FindBy(css = ".redesigned-button-cancel-outline")
    private WebElement declineBtn;
    @FindBy(css = ".redesigned-modal")
    private WebElement popUpModal;
    @FindBy(css = "img[src*='shift-info']")
    private List<WebElement> infoIcons;
    @FindBy(css = ".sch-shift-hover div:nth-child(3)>div.ng-binding")
    private WebElement shiftDuration;
    @FindBy(css = ".shift-hover-subheading.ng-binding:not([ng-if])")
    private WebElement shiftJobTitleAsWorkRole;
    @FindBy(className = "accept-shift-shift-info")
    private WebElement shiftDetail;
    @FindBy(css = ".lg-toast")
    private WebElement msgOnTop;
    @FindBy(css = "[label=\"Yes\"]")
    private WebElement yesButton;
    @FindBy(css = "[label=\"No\"]")
    private WebElement noButton;
    @Override
    public int verifyClickOnAnyShift() throws Exception {
        List<String> expectedRequests = new ArrayList<>(Arrays.asList("Request to Swap Shift", "Request to Cover Shift"));
        int index = 100;
        if (areListElementVisible(tmIcons, 15) && tmIcons.size() > 0) {
            for (int i = 0; i < tmIcons.size(); i++) {
                scrollToElement(tmIcons.get(i));
                waitForSeconds(1);
                clickTheElement(tmIcons.get(i));
                if (isPopOverLayoutLoaded()) {
                    if (verifyShiftRequestButtonOnPopup(expectedRequests)) {
                        index = i;
                        break;
                    }else {
                        clickTheElement(tmIcons.get(i));
                    }
                }
            }
            if (index == 100) {
                // Doesn't find any shift that can swap or cover, cancel the previous
                index = cancelSwapOrCoverRequests(expectedRequests);
            }
        }else {
            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
        }
        return index;
    }

    public int cancelSwapOrCoverRequests(List<String> expectedRequests) throws Exception {
        List<String> swapRequest = new ArrayList<>(Arrays.asList("View Swap Request Status"));
        List<String> coverRequest = new ArrayList<>(Arrays.asList("View Cover Request Status"));
        int index = 100;
        if (areListElementVisible(tmIcons, 10)) {
            for (int i = 0; i < tmIcons.size(); i++) {
                moveToElementAndClick(tmIcons.get(i));
                if (isPopOverLayoutLoaded()) {
                    if (verifyShiftRequestButtonOnPopup(swapRequest)) {
                        cancelRequestByTitle(swapRequest, swapRequest.get(0).substring(5).trim());
                    }
                    if (verifyShiftRequestButtonOnPopup(coverRequest)) {
                        cancelRequestByTitle(coverRequest, coverRequest.get(0).substring(5).trim());
                    }
                    moveToElementAndClick(tmIcons.get(i));
                    if (verifyShiftRequestButtonOnPopup(expectedRequests)) {
                        index = i;
                        break;
                    }
                }
            }
        }else {
            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
        }
        if (index == 100) {
            SimpleUtils.fail("Failed to find a shift that can swap or cover!", false);
        }
        return index;
    }

    public void cancelRequestByTitle(List<String> requests, String title) throws Exception {
        if (requests.size() > 0) {
            clickTheShiftRequestByName(requests.get(0));
            if (isPopupWindowLoaded(title)) {
                verifyClickCancelSwapOrCoverRequest();
            }
        }
    }

    @Override
    public void clickTheShiftRequestByName(String requestName) throws Exception {
        waitForSeconds(2);
        if (areListElementVisible(shiftRequests, 5)) {
            for (WebElement shiftRequest : shiftRequests) {
                if (shiftRequest.getText().trim().equalsIgnoreCase(requestName.trim())) {
                    click(shiftRequest);
                    SimpleUtils.pass("Click " + requestName + " button Successfully!");
                    break;
                }
            }
        }else {
            SimpleUtils.fail("Shift Request buttons not loaded Successfully!", true);
        }
    }

    @Override
    public boolean isPopupWindowLoaded(String title) throws Exception {
        boolean isLoaded = false;
        waitForSeconds(3);
        if (isElementLoaded(popUpWindow, 20) && isElementLoaded(popUpWindowTitle, 20)) {
            if (title.equalsIgnoreCase(popUpWindowTitle.getText())) {
                SimpleUtils.pass(title + " window loaded Successfully!");
                isLoaded = true;
            }
        }
        return isLoaded;
    }

    @Override
    public void verifyClickCancelSwapOrCoverRequest() throws Exception {
        if (isElementLoaded(cancelCoverBtn, 5)) {
            click(cancelCoverBtn);
        }
        if (isElementLoaded(cancelSwapBtn, 5)) {
            click(cancelSwapBtn);
        }
        if (isElementLoaded(okBtnOnConfirm, 5)) {
            click(okBtnOnConfirm);
        }
        if (isElementLoaded(cancelButton, 5)) {
            click(cancelButton);
        }
        else {
            SimpleUtils.fail("Confirm Button failed to load!", true);
        }
    }


    public int cancelClaimRequest(List<String> expectedRequests) throws Exception {
        List<String> claimStatus = new ArrayList<>(Arrays.asList("Claim Shift Approval Pending", "Cancel Claim Request"));
        int index = -1;
        if (areListElementVisible(tmIcons, 10)) {
            for (int i = 0; i < tmIcons.size(); i++) {
                moveToElementAndClick(tmIcons.get(i));
                if (isPopOverLayoutLoaded()) {
                    if (verifyShiftRequestButtonOnPopup(claimStatus)) {
                        clickTheShiftRequestByName(claimStatus.get(1));
                        verifyReConfirmDialogPopup();
                        verifyClickOnYesButton();
                        moveToElementAndClick(tmIcons.get(i));
                        if (verifyShiftRequestButtonOnPopup(expectedRequests)) {
                            index = i;
                            break;
                        }
                    }
                }
            }
        }else {
            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
        }
        if (index == -1) {
            SimpleUtils.fail("Failed to find a shift that can swap or cover!", false);
        }
        return index;
    }

    @Override
    public void verifyReConfirmDialogPopup() throws Exception {
        String title = "Are you sure you want to cancel your claim for this shift?";
        if (isPopupWindowLoaded(title)) {
            if (isElementLoaded(yesButton, 5) && isElementLoaded(noButton, 5)) {
                SimpleUtils.pass("Yes and No Buttons loaded Successfully!");
            }else {
                SimpleUtils.fail("Yes and No Buttons not loaded Successfully!", false);
            }
        }else {
            SimpleUtils.fail(title + " window not loaded Successfully!", false);
        }
    }


    @Override
    public void verifyClickOnYesButton() throws Exception {
        if (isElementLoaded(yesButton, 5)) {
            click(yesButton);
            String message = "Cancelled successfully";
            verifyThePopupMessageOnTop(message);
        }else {
            SimpleUtils.fail("Yes Buttons not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyThePopupMessageOnTop(String expectedMessage) throws Exception {
        if (isElementLoaded(msgOnTop, 20)) {
            String message = msgOnTop.getText();
            if (message.contains(expectedMessage)) {
                SimpleUtils.pass("Verified Message shows correctly!");
            }else {
                SimpleUtils.fail("Message on top is incorrect, expected is: " + expectedMessage + ", but actual is: " + message, false);
            }
        }else {
            SimpleUtils.fail("Message on top not loaded Successfully!", false);
        }
    }

    @Override
    public int selectOneShiftIsClaimShift(List<String> claimShift) throws Exception {
        int index = -1;
        if (areListElementVisible(tmIcons, 5)) {
            for (int i = 0; i < tmIcons.size(); i++) {
                moveToElementAndClick(tmIcons.get(i));
                if (isPopOverLayoutLoaded()) {
                    if (verifyShiftRequestButtonOnPopup(claimShift)) {
                        index = i;
                        break;
                    } else
                        moveToElementAndClick(tmIcons.get(i)); //to close the pop up
                }
            }
            if (index == -1) {
                // Doesn't find any shift that is Claim Shift, cancel the previous
                index = cancelClaimRequest(claimShift);
            }
        }else {
            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
        }
        return index;
    }


    @FindBy (css = "div.sch-day-view-shift-worker-detail")
    private List<WebElement> scheduleTableWeekViewWorkerDetail;
    @Override
    public void validateProfilePictureInAShiftClickable() throws Exception {
        Boolean isShiftDetailsShowed = false;
        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20)) {
            if (scheduleTableWeekViewWorkerDetail.size() != 0) {
                int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
                moveToElementAndClick(scheduleTableWeekViewWorkerDetail.get(randomIndex));
                SimpleUtils.pass("My Schedule Page: Profile picture is clickable successfully");
                if (isPopOverLayoutLoaded()) {
                    if (!popOverLayout.getText().isEmpty() && popOverLayout.getText() != null) {
                        isShiftDetailsShowed = true;
                    }
                    moveToElementAndClick(scheduleTableWeekViewWorkerDetail.get(randomIndex));
                }
                if (isShiftDetailsShowed == true)
                    SimpleUtils.pass("My Schedule Page: Profile picture shows details of TM successfully");
                else
                    SimpleUtils.fail("My Schedule Page: Profile picture failed to details of TM", true);
            } else if (scheduleTableWeekViewWorkerDetail.size() == 0)
                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
        } else if (isElementLoaded(myScheduleNoSchedule, 10))
            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
        else SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
    }


    @Override
    public void clickTheShiftRequestToClaimShift(String requestName, String requestUserName) throws Exception {
        int index = 0;
        if (areListElementVisible(tmIcons, 15)) {
            for (int i = 0; i < tmIcons.size(); i++) {
                moveToElementAndClick(tmIcons.get(i));
                if (isPopOverLayoutLoaded()) {
                    if (popOverLayout.getText().contains(requestName) && popOverLayout.getText().contains(requestUserName)) {
                        index = 1;
                        clickTheElement(popOverLayout.findElement(By.cssSelector("span.sch-worker-action-label")));
                        SimpleUtils.pass("Click " + requestName + " button Successfully!");
                        break;
                    }
                }
            }
            if (index == 0) {
                SimpleUtils.fail("Failed to select one shift to claim", false);
            }
        } else {
            SimpleUtils.fail("Team Members' Icons not loaded", false);
        }
    }

    @Override
    public void claimTheOfferedOpenShift(String requestName) throws Exception {
        if (isPopOverLayoutLoaded()) {
            if (popOverLayout.getText().contains(requestName)) {
                clickTheElement(popOverLayout.findElement(By.cssSelector("span.sch-worker-action-label")));
                SimpleUtils.pass("Click " + requestName + " button Successfully!");
            }
        } else {
            SimpleUtils.fail("Team Member's shift view popup is not displayed", false);
        }

        if (isElementLoaded(claimConfirmPopup, 10)) {
            if (isClickable(acceptBtn, 5)) {
                click(acceptBtn);
                waitForSeconds(5);
            } else {
                SimpleUtils.fail("Accept button is not clickable!", false);
            }
        } else {
            SimpleUtils.fail("The confirm claim shift offer popup is not displayed", false);
        }
    }

    @Override
    public void verifyTheRedirectionOfBackButton() throws Exception {
        verifyBackNSubmitBtnLoaded();
        click(backButton);
        String title = "Find Shifts to Swap";
        if (isPopupWindowLoaded(title)) {
            SimpleUtils.pass("Redirect to Find Shifts to Swap windows Successfully!");
        }else {
            SimpleUtils.fail("Failed to redirect to Find Shifts to Swap window!", false);
        }
    }

    @Override
    public void verifyBackNSubmitBtnLoaded() throws Exception {
        if (isElementLoaded(backButton, 5)) {
            SimpleUtils.pass("Back button loaded Successfully on submit swap request!");
        }else {
            SimpleUtils.fail("Back button not loaded Successfully on submit swap request!", true);
        }
        if (isElementLoaded(submitButton, 5)) {
            SimpleUtils.pass("Submit button loaded Successfully on submit swap request!");
        }else {
            SimpleUtils.fail("Submit button not loaded Successfully on submit swap request!", false);
        }
    }


    @Override
    public void verifyClickAcceptSwapButton() throws Exception {
        String title = "Confirm Shift Swap";
        String expectedMessage = "Success";
        if (areListElementVisible(acceptButtons, 5) && acceptButtons.size() > 0) {
            clickTheElement(acceptButtons.get(0));
            SimpleUtils.assertOnFail(title + " not loaded Successfully!", isPopupWindowLoaded(title), false);
            if (isElementLoaded(agreeButton, 5)) {
                click(agreeButton);
                verifyThePopupMessageOnTop(expectedMessage);
                verifySwapRequestDeclinedDialogPopUp();
                if (isElementLoaded(closeDialogBtn, 5)) {
                    clickTheElement(closeDialogBtn);
                }
            }else {
                SimpleUtils.fail("I Agree button not loaded Successfully!", false);
            }
        }else {
            SimpleUtils.fail("Accept Button not loaded Successfully!", false);
        }
    }

    @FindBy(css = ".redesigned-modal-title")
    private WebElement deleteScheduleTitle;

    @FindBy (css = ".redesigned-button-ok")
    private WebElement deleteButtonOnDeleteSchedulePopup;
    private void verifySwapRequestDeclinedDialogPopUp() throws Exception {
        try {
            // Same elements sa Delete Schedule pop up
            if (isElementLoaded(deleteScheduleTitle, 10) && deleteScheduleTitle.getText().equalsIgnoreCase("Swap Request Declined")) {
                if (isElementLoaded(deleteButtonOnDeleteSchedulePopup, 10)) {
                    clickTheElement(deleteButtonOnDeleteSchedulePopup);
                    SimpleUtils.pass("Click on 'OK' button Successfully on 'Swap Request Declined' dialog!");
                }
            }
        } catch (Exception e) {
            // Do nothing
        }
    }

//
    @Override
    public void verifyClickAgreeBtnOnClaimShiftOffer() throws Exception {
        if (isElementLoaded(agreeClaimBtn, 5)) {
            click(agreeClaimBtn);
            String expectedMessage = "Your claim request has been received and sent for approval";
            verifyThePopupMessageOnTop(expectedMessage);
        }else {
            SimpleUtils.fail("I Agree Button not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyClickAgreeBtnOnClaimShiftOfferWhenDontNeedApproval() throws Exception {
        if (isElementLoaded(agreeClaimBtn, 5)) {
            click(agreeClaimBtn);
            String expectedMessage = "Success! This shift is yours, and has been added to your schedule.";
            verifyThePopupMessageOnTop(expectedMessage);
        }else {
            SimpleUtils.fail("I Agree Button not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyClickAgreeBtnOnClaimShiftOfferWithMessage(String expectedMessage) throws Exception {
        if (isElementLoaded(agreeClaimBtn, 25)) {
            click(agreeClaimBtn);
            verifyThePopupMessageOnTop(expectedMessage);
        }else {
            SimpleUtils.fail("I Agree Button not loaded Successfully!", false);
        }
    }


    @Override
    public void verifyClickAgreeBtnForSwapWithMessage(String expectedMessage) throws Exception {
        if (areListElementVisible(acceptButtons, 5) && acceptButtons.size()>0) {
            clickTheElement(acceptButtons.get(0));
            if (isElementLoaded(agreeButton, 5)) {
                click(agreeButton);
                verifyThePopupMessageOnTop(expectedMessage);
                verifySwapRequestDeclinedDialogPopUp();
                if (isElementLoaded(closeDialogBtn, 5)) {
                    clickTheElement(closeDialogBtn);
                }
            }else
                SimpleUtils.fail("I Agree button not loaded Successfully!", false);
        }else {
            SimpleUtils.fail("Agree Button not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyTheColorOfCancelClaimRequest(String cancelClaim) throws Exception {
        String red = "#ff0000";
        String color = "";
        if (areListElementVisible(shiftRequests, 5)) {
            for (WebElement shiftRequest : shiftRequests) {
                if (shiftRequest.getText().equalsIgnoreCase(cancelClaim)) {
                    color = Color.fromString(shiftRequest.getCssValue("color")).asHex();
                }
            }
        }else {
            SimpleUtils.fail("Shift Requests not loaded Successfully!", false);
        }
        if (red.equalsIgnoreCase(color)) {
            SimpleUtils.pass("Cancel Claim Request option is in red color");
        }else {
            SimpleUtils.fail("Cancel Claim Request option should be there in red color, but the actual color is: "
                    + color + ", expected is red: " + red, false);
        }
    }
    @FindBy(className = "card-carousel-link")
    private WebElement viewShiftsBtn;

    @FindBy(css = "[ng-repeat=\"opt in opts\"] input-field")
    private List<WebElement> shiftTypes;

    @FindBy(css = "[ng-click=\"$ctrl.openFilter()\"]")
    private WebElement filterButton;
    @Override
    public void validateViewShiftsClickable() throws Exception {
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        if (smartCardPage.isViewShiftsBtnPresent()) {
            click(viewShiftsBtn);
            SimpleUtils.pass("My Schedule Page: View shift is clickable and a filter for Open shift is applied after that successfully");
            click(filterButton);
            if (shiftTypes.size() > 0) {
                for (WebElement shiftType : shiftTypes) {
                    WebElement filterCheckBox = shiftType.findElement(By.tagName("input"));
                    if (filterCheckBox.getAttribute("class").contains("ng-not-empty")) {
                        if (shiftType.getText().contains("Offered"))
                            SimpleUtils.pass("My Schedule Page: only open shifts for the selected week should show successfully");
                        else
                            SimpleUtils.fail("My Schedule Page: Not only open shifts for the selected week show", false);
                    }
                }
            }
        }
    }

    @Override
    public void verifyComponentsOnSubmitCoverRequest() throws Exception {
        if (isElementLoaded(messageText, 5)) {
            SimpleUtils.pass("Message textbox loaded Successfully!");
        }else {
            SimpleUtils.fail("Message textbox not loaded Successfully!", true);
        }
        if (isElementLoaded(cancelButton, 5)) {
            SimpleUtils.pass("Cancel button on Submit Cover Request loaded Successfully!");
        }else {
            SimpleUtils.fail("Cancel button on Submit Cover Request not loaded Successfully!", true);
        }
        if (isElementLoaded(submitButton, 5)) {
            SimpleUtils.pass("Submit button on Submit Cover Request loaded Successfully!");
        }else {
            SimpleUtils.fail("Submit button on Submit Cover Request not loaded Successfully!", true);
        }
    }


    @Override
    public void verifyClickOnSubmitButton() throws Exception {
        if (isElementLoaded(submitButton, 10)) {
            click(submitButton);
            if (isElementLoaded(confirmWindow, 20) && isElementLoaded(okBtnOnConfirm, 20)) {
                clickTheElement(okBtnOnConfirm);
                SimpleUtils.pass("Confirm window loaded Successfully!");
            }else {
                SimpleUtils.fail("Confirm window not loaded Successfully!", true);
            }
        }else {
            SimpleUtils.fail("Submit button on Submit Cover Request not loaded Successfully!", true);
        }
    }


    @FindBy(css = ".shift-container.week-schedule-shift-wrapper")
    private List<WebElement> shifts;

    @FindBy(css = ".week-schedule-shift .shift-container .rows .worker-image-optimized img")
    private List<WebElement> profileIcons;
    @Override
    public void clickOnShiftByIndex(int index) throws Exception {
        if (areListElementVisible(tmIcons, 5)) {
            if (index < tmIcons.size()) {
                moveToElementAndClick(tmIcons.get(index));
            }else {
                SimpleUtils.fail("Index: " + index + " is out of range, the total size is: " + tmIcons.size(), true);
            }
        } else if (areListElementVisible(shifts, 10)) {
            clickTheElement(profileIcons.get(index));
        } else {
            SimpleUtils.fail("Shift Requests not loaded Successfully!", true);
        }
    }

    @Override
    public void selectOneTeamMemberToSwap(String tmName) throws Exception {
        if (areListElementVisible(comparableShifts, 5) && isElementLoaded(nextButton, 5)) {
            boolean isTMExists = false;
            for (WebElement comparableShift: comparableShifts) {
                WebElement name = comparableShift.findElement(By.className("shift-swap-modal-table-name"));
                if (name.getText().equalsIgnoreCase(tmName)) {
                    WebElement selectBtn = comparableShift.findElement(By.cssSelector("td.shift-swap-modal-shift-table-select>div"));
                    clickTheElement(selectBtn);
                    SimpleUtils.pass("Select team member: " + tmName + " Successfully!");
                    isTMExists = true;
                    break;
                }
            }
            if (!isTMExists) {
                SimpleUtils.fail("The TM is not exits in the comparable shifts list!", false);
            }
            scrollToElement(nextButton);
            clickTheElement(nextButton);
            verifyClickOnSubmitButton();
        }else {
            SimpleUtils.fail("Comparable Shifts not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyComparableShiftsAreLoaded() throws Exception {
        if (areListElementVisible(comparableShifts, 60)) {
            SimpleUtils.pass("Comparable Shifts loaded Successfully!");
        }else {
            SimpleUtils.fail("Comparable Shifts not loaded Successfully!", false);
        }
    }


    @FindBy(css = "div.console-navigation-item-label.Schedule")
    private WebElement goToScheduleButton;

    @FindBy(css = "div[helper-text*='Work in progress Schedule'] span.legend-label")
    private WebElement draft;

    @FindBy(css = "div[helper-text-position='top'] span.legend-label")
    private WebElement published;

    @FindBy(css = "div[helper-text*='final per schedule changes'] span.legend-label")
    private WebElement finalized;

    @FindBy(className = "overview-view")
    private WebElement overviewSectionElement;

    @FindBy(css = "div.sub-navigation-view-link.active")
    private WebElement activatedSubTabElement;

    @FindBy(xpath = "//div[contains(@class,'sub-navigation-view')]//span[contains(text(),'Schedule')]")
    private WebElement goToScheduleTab;

    @FindBy(css = "lg-button[label=\"Analyze\"]")
    private WebElement analyze;

    @FindBy(xpath = "//*[@class=\"version-label-container\"]/div")
    private List<WebElement> scheduleHistoryListInAnalyzePopUp;

    @FindBy(css = "lg-button[label=\"Edit\"]")
    private WebElement edit;

    @FindBy(css = "lg-button[data-tootik=\"Edit Schedule\"]")
    private WebElement newEdit;

    @FindBy(xpath = "//span[contains(text(),'Projected Sales')]")
    private WebElement goToProjectedSalesTab;

    @FindBy(css = "ui-view[name='forecastControlPanel'] span.highlight-when-help-mode-is-on")
    private WebElement salesGuidance;

    @FindBy(css = "[ng-click=\"controlPanel.fns.refreshConfirmation($event)\"]")
    private WebElement refresh;

    @FindBy(css = "button.btn.sch-publish-confirm-btn")
    private WebElement confirmRefreshButton;

    @FindBy(css = "button.btn.successful-publish-message-btn-ok")
    private WebElement okRefreshButton;

    @FindBy(xpath = "//div[contains(text(),'Guidance')]")
    private WebElement guidance;

    @FindBy(xpath = "//div[contains(text(),'Schedule History')]")
    private WebElement scheduleHistoryInAnalyzePopUp;

    @FindBy(xpath = "//div[contains(text(),'Details')]")
    private WebElement versionDetailsInAnalyzePopUp;

    @FindBy(css = "[ng-repeat=\"role in guidanceRoleDetails\"]")
    private List<WebElement> guidanceRoleDetails;

    @FindBy(css = "[ng-repeat=\"role in versionedRoleDetails\"]")
    private List<WebElement> versionRoleDetails;

    @FindBy(xpath = "//span[contains(text(),'Staffing Guidance')]")
    private WebElement goToStaffingGuidanceTab;

//	@FindBy(className="sch-calendar-day-dimension")
//	private List<WebElement> ScheduleCalendarDayLabels;

    @FindBy(css = "div.sub-navigation-view-link")
    private List<WebElement> ScheduleSubTabsElement;

    @FindBy(xpath= "//day-week-picker/div/div/div[3]")
    private WebElement calendarNavigationPreviousWeek;

    @FindBy(css = "[ng-click=\"regenerateFromOverview()\"] button")
    private WebElement generateSheduleButton;

    @FindBy(css = "[ng-click*=\"regenerateFromManagerView()\"]")
    private WebElement reGenerateScheduleButton;

    @FindBy(css = "[label='Generate Schedule']")
    private WebElement generateSheduleForEnterBudgetBtn;

    @FindBy(css = "lg-button[label*=\"ublish\"]")
    private WebElement publishSheduleButton;

    @FindBy(css = "lg-button[label*=\"ublish\"] span span")
    private WebElement txtPublishSheduleButton;

    @FindBy(css = "div.edit-budget span.header-text")
    private WebElement popUpGenerateScheduleTitleTxt;

    @FindBy(css = "span.ok-action-text")
    private WebElement btnGenerateBudgetPopUP;

    @FindBy(css = "div[ng-if='canEditHours(budget)']")
    private List<WebElement> editBudgetHrs;

    @FindBy(css = "span[ng-if='canEditWages(budget)']")
    private List<WebElement> editWagesHrs;

    @FindBy(css = "div.sch-view-dropdown-summary-content-item-heading.ng-binding")
    private WebElement analyzePopupLatestVersionLabel;

    @FindBy(css = "[ng-click=\"controlPanel.fns.analyzeAction($event)\"]")
    private WebElement scheduleAnalyzeButton;

    @FindBy(className = "sch-schedule-analyze-content")
    private WebElement scheduleAnalyzePopup;

    @FindBy(className = "sch-schedule-analyze-dismiss")
    private WebElement scheduleAnalyzePopupCloseButton;

    @FindBy(css = "lg-close.dismiss")
    private WebElement scheduleAnalyzePopupCloseButtonInKS2;

    @FindBy(css = "[ng-click=\"goToSchedule()\"]")
    private WebElement checkOutTheScheduleButton;

    @FindBy(css = "lg-button[label=\"Generate schedule\"]")
    private WebElement generateScheduleBtn;

    @FindBy(className = "console-navigation-item")
    private List<WebElement> consoleNavigationMenuItems;

    @FindBy(css = "[ng-click=\"callOkCallback()\"]")
    private WebElement editAnywayPopupButton;

    @FindBy(css = "[ng-if=\"canShowNewShiftButton()\"]")
    private WebElement addNewShiftOnDayViewButton;

    @FindBy(css = "[label=\"Cancel\"]")
    private WebElement scheduleEditModeCancelButton;

    @FindBy(css = "[label=\"Save\"]")
    private WebElement scheduleEditModeSaveButton;

    @FindBy(css = "[ng-click=\"regenerateFromOverview()\"]")
    private WebElement scheduleGenerateButton;

    @FindBy(css = "#legion-app navigation div:nth-child(4)")
    private WebElement analyticsConsoleName;
    @Override
    public void goToSchedulePageAsTeamMember() throws Exception {
        if (isElementLoaded(goToScheduleButton, 5)) {
            clickTheElement(goToScheduleButton);
            if (areListElementVisible(ScheduleSubTabsElement, 5)) {
                SimpleUtils.pass("Navigate to schedule page successfully!");
            }else {
                SimpleUtils.fail("Schedule page not loaded Successfully!", true);
            }
        }else {
            SimpleUtils.fail("Go to Schedule button not loaded Successfully!", false);
        }
    }

    @Override
    public void gotoScheduleSubTabByText(String subTitle) throws Exception {
        if (areListElementVisible(ScheduleSubTabsElement, 5)) {
            for (WebElement scheduleSubTab : ScheduleSubTabsElement) {
                if (isElementEnabled(scheduleSubTab, 5) && isClickable(scheduleSubTab, 5)
                        && scheduleSubTab.getText().equalsIgnoreCase(subTitle)) {
                    click(scheduleSubTab);
                    break;
                }
            }
            if (isElementLoaded(activatedSubTabElement, 5) && activatedSubTabElement.getText().equalsIgnoreCase(subTitle)) {
                SimpleUtils.pass("Navigate to sub tab: " + subTitle + " Successfully!");
            }else {
                SimpleUtils.fail("Failed navigating to sub tab: " + subTitle, false);
            }
        }else {
            SimpleUtils.fail("Schedule sub tab elements not loaded SUccessfully!", false);
        }
    }

    @Override
    public void verifyTeamScheduleInViewMode() throws Exception {
        if (isElementLoaded(edit, 5)) {
            SimpleUtils.fail("Team Member shouldn't see the Edit button!", false);
        }else {
            SimpleUtils.pass("Verified Team Schedule is in View Mode!");
        }
    }

    @Override
    public List<String> getWholeWeekSchedule() throws Exception {
        List<String> weekShifts = new ArrayList<>();
        if (areListElementVisible(wholeWeekShifts, 5)) {
            for (WebElement shift : wholeWeekShifts) {
                WebElement name = shift.findElement(By.className("week-schedule-worker-name"));
                if (!name.getText().contains("Open")) {
                    weekShifts.add(shift.getText());
                }
            }
        }else {
            SimpleUtils.fail("Whole Week Shifts not loaded Successfully!", true);
        }
        if (weekShifts.size() == 0) {
            SimpleUtils.fail("Failed to get the whole week shifts!", false);
        }
        return weekShifts;
    }


    @Override
    public String getSelectedWeek() throws Exception {
        String selectedWeek = "";
        if (isElementLoaded(currentActiveWeek, 15)) {
            selectedWeek = currentActiveWeek.getText();
            SimpleUtils.report("Get current active week: " + selectedWeek);
        }else {
            SimpleUtils.fail("Current Active Week not loaded Successfully!", false);
        }
        return selectedWeek;
    }

    @Override
    public void verifyTheSumOfSwapShifts() throws Exception {
        int sum = 0;
        if (isElementLoaded(resultCount, 5) && areListElementVisible(comparableShifts, 5)) {
            String result = resultCount.getText();
            if (result.length() > 7) {
                sum = Integer.parseInt(result.substring(0, result.length() - 7).trim());
            }
            if (sum == comparableShifts.size()) {
                SimpleUtils.pass("Verified Sum of swap shifts is correct!");
            }else {
                SimpleUtils.fail("Sum of swap shifts is incorrect, showing sum is: " + sum + ", but actual is: " + comparableShifts.size(), false);
            }
        }else {
            SimpleUtils.fail("Sum results and comparable shifts not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyTheDataOfComparableShifts() throws Exception {
        if (areListElementVisible(comparableShifts, 5)) {
            for (WebElement comparableShift : comparableShifts) {
                WebElement name = comparableShift.findElement(By.className("shift-swap-modal-table-name"));
                WebElement dateNTime = comparableShift.findElement(By.className("shift-swap-modal-table-shift-info"));
                WebElement location = comparableShift.findElement(By.className("shift-swap-modal-table-home-location"));
                if (name != null && dateNTime != null && location != null && !name.getText().isEmpty() &&
                        !dateNTime.getText().isEmpty() && !location.getText().isEmpty()) {
                    SimpleUtils.report("Verified name: " + name.getText() + ", date and time: " + dateNTime.getText() +
                            ", location: " + location.getText() + " are loaded!");
                }else {
                    SimpleUtils.fail("The data of the comparable shift is incorrect!", false);
                    break;
                }
            }
        }else {
            SimpleUtils.fail("Comparable shifts not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyClickOnNextButtonOnSwap() throws Exception {
        verifySelectOneShiftNVerifyNextButtonEnabled();
        clickTheElement(nextButton);
    }

    @Override
    public List<String> verifySelectMultipleSwapShifts() throws Exception {
        List<String> selectedTMs = new ArrayList<>();
        String selected = "selected";
        if (areListElementVisible(selectBtns, 5) && selectBtns.size() > 0 && areListElementVisible(swapCoverNames, 5)) {
            for (WebElement selectBtn : selectBtns) {
                String className = selectBtn.getAttribute("class");
                if (className.isEmpty()) {
                    clickTheElement(selectBtn);
                    className = selectBtn.getAttribute("class");
                    if (className.contains(selected)) {
                        SimpleUtils.pass("Select one shift Successfully!");
                    }else {
                        SimpleUtils.fail("Failed to select the shift!", false);
                    }
                }
            }
            for (WebElement name : swapCoverNames) {
                selectedTMs.add(name.getText());
            }
        }else {
            SimpleUtils.fail("Select Buttons not loaded Successfully!", false);
        }
        return selectedTMs;
    }

    @Override
    public void verifySelectOneShiftNVerifyNextButtonEnabled() throws Exception {
        String selected = "selected";
        if (areListElementVisible(selectBtns, 10) && selectBtns.size() > 0) {
            if (selectBtns.get(0).getAttribute("class").isEmpty()) {
                click(selectBtns.get(0));
            }
            if (selectBtns.get(0).getAttribute("class").contains(selected)) {
                SimpleUtils.pass("Select one shift Successfully!");
                if (isElementEnabled(nextButton, 5)) {
                    SimpleUtils.pass("Next Button is enabled after selecting one shift!");
                }else {
                    SimpleUtils.fail("Next button is still disabled after selecting one shift!", false);
                }
            }else {
                SimpleUtils.fail("Failed to select the shift!", false);
            }
        }else {
            SimpleUtils.fail("Select Buttons not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyNextButtonIsLoadedAndDisabledByDefault() throws Exception {
        String notAllowed = "not-allowed";
        if (isElementLoaded(nextButton, 5)) {
            SimpleUtils.pass("Next button loaded Successfully!");
            WebElement button = nextButton.findElement(By.tagName("button"));
            String cursorAttribute = button == null ? "" : button.getCssValue("cursor");
            if (notAllowed.equalsIgnoreCase(cursorAttribute)) {
                SimpleUtils.pass("Next button is disabled by default!");
            }else {
                SimpleUtils.fail("Next button should be disabled by default, but it is enabled!", false);
            }
        }else {
            SimpleUtils.fail("Next button not loaded Successfully!", false);
        }
    }


    @Override
    public void verifySwapRequestShiftsLoaded() throws Exception {
        if (areListElementVisible(swapRequestShifts, 5)) {
            SimpleUtils.pass("Swap Request Shifts loaded Successfully!");
        }else {
            SimpleUtils.fail("Swap Request Shifts not loaded Successfully!", false);
        }
    }

    @Override
    public void verifyTheContentOfMessageOnSubmitCover() throws Exception {
        if (isElementLoaded(messageText, 5) && isElementLoaded(shiftOfferTime, 5)) {
            String expectedText = "Hey, I have a commitment " + shiftOfferTime.getText() + " that conflicts with my shift, would you be able to help cover my shift?";
            String actualText = messageText.getAttribute("value");
            if (expectedText.equalsIgnoreCase(actualText)) {
                SimpleUtils.pass("The content of Add Message text box is correct!");
                messageText.clear();
            }else {
                SimpleUtils.fail("The content of Add Message text box is incorrect! Expected is: " + expectedText
                        + " but actual is: " + actualText, false);
            }
        }else {
            SimpleUtils.fail("Message text box not loaded Successfully!", true);
        }
    }


    @Override
    public void verifyShiftRequestStatus(String expectedStatus) throws Exception {
        if (areListElementVisible(shiftStatus, 10)) {
            for (WebElement status : shiftStatus) {
                if (expectedStatus.equalsIgnoreCase(status.getText())) {
                    SimpleUtils.pass("Verified shift status: " + expectedStatus + " is correct!");
                }else {
                    SimpleUtils.fail("Shift status is incorrect, expected is: " + expectedStatus + ", but actual is: "
                            + status.getText(), false);
                }
            }
        }else {
            SimpleUtils.fail("Shift Status not loaded Successfully!", false);
        }
    }

    @FindBy(className = "week-schedule-shift-wrapper")
    private List<WebElement> shiftsWeekView;
    @FindBy(css = "div.popover div:nth-child(3)>div.ng-binding")
    private WebElement timeDuration;
    @Override
    public void verifyScheduledNOpenFilterLoaded() throws Exception {
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        String shiftTypeFilterKey = "shifttype";
        String scheduled = "Scheduled";
        String open = "Open";
        if (areListElementVisible(shiftsWeekView, 5)) {
            HashMap<String, ArrayList<WebElement>> availableFilters = scheduleMainPage.getAvailableFilters();
            if (availableFilters.size() > 0) {
                ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
                if (shiftTypeFilters.size() == 2) {
                    if (shiftTypeFilters.get(0).getText().contains(scheduled) && shiftTypeFilters.get(1).getText().contains(open)) {
                        SimpleUtils.pass("Filter is enabled and it has two filters - Scheduled and Open");
                    } else {
                        SimpleUtils.fail("Two filters are incorrect, expected are Scheduled and Open, actual are: "
                                + shiftTypeFilters.get(0).getText() + " and " + shiftTypeFilters.get(1).getText(), false);
                    }
                } else {
                    SimpleUtils.fail("The size of Shift type filters are incorrect!", false);
                }
            } else {
                SimpleUtils.fail("Filters not loaded Successfully!", false);
            }
        }else if (isElementLoaded(noSchedule, 5) && isElementLoaded(periodName, 5)) {
            SimpleUtils.report("The Schedule of this Week: " + periodName.getText() + " isn't generated!");
        }else {
            SimpleUtils.fail("Shifts week view not loaded Successfully!", false);
        }
    }


    @Override
    public void verifyClaimShiftOfferNBtnsLoaded() throws Exception {
        if (isElementLoaded(claimShiftWindow, 5)) {
            if (isElementLoaded(agreeClaimBtn, 5) && isElementLoaded(declineBtn, 5)) {
                SimpleUtils.pass("Accept and Decline Buttons loaded Successfully!");
            }else {
                SimpleUtils.fail("Accept and Decline Buttons not loaded Successfully!", false);
            }
        }else {
            SimpleUtils.fail("Pop up Window: Open Shift not loaded Successfully!", false);
        }
    }

    @Override
    public void clickOnDeclineButton() throws Exception {
        if (isElementLoaded(declineBtn, 5)) {
            clickTheElement(declineBtn);
            waitForSeconds(2);
            if (isElementLoaded(agreeClaimBtn, 10) && agreeClaimBtn.getText().equalsIgnoreCase("OK")) {
                clickTheElement(agreeClaimBtn);
            }
        } else {
            SimpleUtils.fail("Decline button failed to load!", false);
        }
    }

    @FindBy(css = ".day-view-shift-hover-info-icon")
    private List<WebElement> hoverIcons;
    @Override
    public List<String> getShiftHoursFromInfoLayout() throws Exception {
        List<String> shiftHours = new ArrayList<>();
        waitForSeconds(2);
        if (areListElementVisible(hoverIcons, 20)) {
            for (WebElement hoverIcon : hoverIcons) {
                waitForSeconds(5);
                scrollToBottom();
                clickTheElement(hoverIcon);
                if (isElementLoaded(shiftDuration, 5)) {
                    shiftHours.add(shiftDuration.getText());
                    SimpleUtils.report("Get the Shift time: " + shiftDuration.getText() + " Successfully!");
                    click(hoverIcon);
                }else {
                    SimpleUtils.fail("Shift time duration not loaded Successfully!", false);
                }
            }
            if (shiftHours.size() != hoverIcons.size()) {
                SimpleUtils.fail("Failed to get the shift hours, the count is incorrect!", false);
            }
        }else {
            SimpleUtils.fail("Info Icons not loaded Successfully!", false);
        }
        return shiftHours;
    }


    @Override
    public void verifyTheShiftHourOnPopupWithScheduleTable(String scheduleShiftTime, String weekDay) throws Exception {
        if (isElementLoaded(shiftDetail, 5)) {
            String details = shiftDetail.getText();
            if (details.toLowerCase().replaceAll("\\s*", "").contains(scheduleShiftTime.toLowerCase().replaceAll("\\s*", "")) &&
                    details.toLowerCase().replaceAll("\\s*", "").contains(weekDay.toLowerCase().replaceAll("\\s*", ""))) {
                SimpleUtils.pass("Date and time in the Popup is match with the date and time in Schedule table: " + scheduleShiftTime);
            }else {
                SimpleUtils.fail("Date and time in the Popup is incorrect: " + details + ", expected week day is: "
                        + weekDay + ", and expected schedule shift time is:" + scheduleShiftTime, false);
            }
        }else {
            SimpleUtils.fail("Shift Details not loaded Successfully!", false);
        }
    }


    @Override
    public String getSpecificShiftWeekDay(int index) throws Exception {
        String weekDay = null;
        if (areListElementVisible(tmIcons, 5) && index < tmIcons.size()) {
            WebElement clickedShift = tmIcons.get(index);
            WebElement parent = clickedShift.findElement(By.xpath("./../../../../../../../../.."));
            if (parent != null) {
                WebElement weekDayElement = parent.findElement(By.tagName("div"));
                String currentWeekDay = weekDayElement == null ? null : weekDayElement.getText();
                if (currentWeekDay != null && !currentWeekDay.isEmpty()) {
                    // WeekDay format is: SATURDAY, MAY 02, need to convert 02 to 2
                    String[] items = currentWeekDay.split(" ");
                    if (items.length == 3 && SimpleUtils.isNumeric(items[2])) {
                        items[2] = Integer.toString(Integer.parseInt(items[2]));
                        weekDay = items[0] + " " + items [1] + " " + items[2];
                        SimpleUtils.report("Get the Week day for clicked shift: " + weekDay);
                    }else {
                        SimpleUtils.fail("Split String: '" + currentWeekDay + "' failed!", false);
                    }
                }else {
                    SimpleUtils.fail("Failed to get the week day for clicked shift!", false);
                }
            }else {
                SimpleUtils.fail("Failed to find the parent Element for the clicked team Member!", false);
            }
        }else {
            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
        }
        return weekDay;
    }


    @Override
    public void verifyClickNoButton() throws Exception {
        if (isElementLoaded(noButton, 5)) {
            click(noButton);
            if (!isElementLoaded(popUpWindow, 5)) {
                SimpleUtils.pass("Click on No Button Successfully!");
            }else {
                SimpleUtils.fail("Click on No Button not Successfully!", false);
            }
        }else {
            SimpleUtils.fail("No Buttons not loaded Successfully!", false);
        }
    }


    @Override
    public void verifyTheFunctionalityOfClearFilter() throws Exception {
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        String linkName = "Clear Filter";
        String open = "Open";
        smartCardPage.clickLinkOnSmartCardByName(linkName);
        if (areListElementVisible(dayViewAvailableShifts, 5)) {
            for (WebElement shift : dayViewAvailableShifts) {
                WebElement workerName = shift.findElement(By.className("sch-day-view-shift-worker-name"));
                if (workerName != null) {
                    if (!workerName.getText().trim().equalsIgnoreCase(open)) {
                        SimpleUtils.pass("Clear Filter Successfully, no open shift found!");
                    }else {
                        SimpleUtils.fail("Clear Filter not Successfully, still found the open shift!", false);
                    }
                }else {
                    SimpleUtils.fail("Failed to find the worker name element!", false);
                }
            }
        }else {
            SimpleUtils.report("No shifts found after clearing the shift!");
        }
    }

    @FindBy(className = "sch-grid-container")
    private WebElement scheduleTable;

    @FindBy(css = "div.sch-shift-container")
    private List<WebElement> scheduleShiftsRows;

    @FindBy(css = ".sub-navigation-view-link")
    private List<WebElement> subMenusOnSchedulePage;
    @Override
    public void validateTheAvailabilityOfScheduleTable(String userName) throws Exception {
        if (isElementLoaded(scheduleTable, 10)) {
            SimpleUtils.pass("My Schedule Page: Schedule table is present");
            if (scheduleShiftsRows.size() > 0) {
                for (WebElement scheduleShift : scheduleShiftsRows) {
                    if (scheduleShift.getText().toLowerCase().contains(userName.toLowerCase())) {
                        SimpleUtils.pass("My Schedule Page: TM's Schedules show in schedule table");
                        break;
                    } else if (scheduleShift.getText() == null)
                        SimpleUtils.report("My Schedule Page: TM's Schedules are empty");
                    else
                        SimpleUtils.fail("My Schedule Page: TM's Schedules don't show in schedule table", true);
                }
            }
        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
        } else {
            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
        }
    }

    @Override
    public void validateTheAvailabilityOfScheduleMenu() throws Exception {
        if (areListElementVisible(subMenusOnSchedulePage, 10)) {
            if (subMenusOnSchedulePage.size() >=2) {
                SimpleUtils.pass("Schedule Page: It has at least two sub menus successfully");
            } else {
                SimpleUtils.fail("Schedule Page: It doesn't have two sub menus", false);
            }
            for (WebElement subMenu : subMenusOnSchedulePage) {
                if (subMenu.getText().trim().equals("My Schedule") || subMenu.getText().trim().equals("Team Schedule") || subMenu.getText().trim().equals("My Clocks") ) {
                    SimpleUtils.pass("Schedule Page: It includes " + subMenu.getText());
                } else {
                    SimpleUtils.fail("Schedule Page: " + subMenu.getText() + " isn't expected in sub menu list", true);
                }
            }
        } else {
            SimpleUtils.fail("Schedule Page: Sub menu list failed to load", true);
        }
    }



    @Override
    public void validateTheFocusOfSchedule() throws Exception {
        if (areListElementVisible(subMenusOnSchedulePage, 10) && subMenusOnSchedulePage.size() > 1) {
            if (subMenusOnSchedulePage.get(0).getAttribute("class").contains("active") && subMenusOnSchedulePage.get(0).getText().contains("My Schedule")) {
                SimpleUtils.pass("Schedule Page: My schedule is selected by default not the Team schedule successfully ");
            } else
                SimpleUtils.fail("Schedule Page: My schedule isn't selected by default", false);
        } else SimpleUtils.fail("Schedule Page: Sub menus failed to load", false);
    }

    @Override
    public void validateTheDefaultFilterIsSelectedAsScheduled() throws Exception {
        if (isElementLoaded(filterButton, 5)) {
            click(filterButton);
            if (shiftTypes.size() > 0) {
                for (WebElement shiftType : shiftTypes) {
                    WebElement filterCheckBox = shiftType.findElement(By.tagName("input"));
                    if (filterCheckBox.getAttribute("class").contains("ng-not-empty")) {
                        if (shiftType.getText().contains("Scheduled"))
                            SimpleUtils.pass("My Schedule Page: Scheduled filter is applied by default successfully");
                        else
                            SimpleUtils.fail("My Schedule Page: Scheduled filter isn't applied by default successfully", false);
                    }
                }
            } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
                SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
            } else
                SimpleUtils.fail("My Schedule Page: No schedule shift type can be applied", true);
            //Click again to close the pop up menu
            click(filterButton);
        } else
            SimpleUtils.fail("My Schedule Page: Filter button failed to load", true);
    }

    @Override
    public void validateTheFocusOfWeek(String currentDate) throws Exception {
        String date = null;
        if (isScheduleWeekViewActive()) {
            SimpleUtils.pass("My Schedule Page: It is in week view now");
            if (currentDate.contains(",") && currentDate.contains(" ")) {
                date = currentDate.split(",")[1].trim().split(" ")[1];
                SimpleUtils.report("Current date is " + date);
            }
            //activeWeekText is Mon - Sun Apr 13 - Apr 19
            String activeWeekText = getActiveWeekText();
            SimpleUtils.report("activeWeekText is: " + activeWeekText);
            String weekDefaultEnd = "";
            String weekDefaultBegin = "";
            if (activeWeekText.contains(" ") && activeWeekText.contains("-")) {
                try {
                    weekDefaultBegin = activeWeekText.split("-")[1].split(" ")[3];
                    SimpleUtils.report("weekDefaultBegin is: " + weekDefaultBegin);
                    weekDefaultEnd = activeWeekText.split("-")[2].split(" ")[2];
                    SimpleUtils.report("weekDefaultEnd is: " + weekDefaultEnd);
                } catch (Exception e) {
                    SimpleUtils.fail("My Schedule Page: Active week text doesn't have enough length", true);
                }
            }
            if ((Integer.parseInt(weekDefaultBegin) <= Integer.parseInt(date) && Integer.parseInt(date) <= Integer.parseInt(weekDefaultEnd))
                    || (Integer.parseInt(date) <= Integer.parseInt(weekDefaultEnd) && (weekDefaultBegin.length() == 2 && date.length() == 1))
                    || (Integer.parseInt(date) >= Integer.parseInt(weekDefaultBegin) && (weekDefaultBegin.length() == 2 && date.length() == 2))) {
                SimpleUtils.pass("My Schedule Page: By default focus is on current week successfully");
            } else {
                SimpleUtils.fail("My Schedule Page: Current week isn't selected by default", true);
            }
        } else
            SimpleUtils.fail("My Schedule Page: It isn't in week view", true);
    }

    public Boolean isScheduleWeekViewActive() {
        WebElement scheduleWeekViewButton = MyThreadLocal.getDriver().
                findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'week')\"]"));
        if (scheduleWeekViewButton.getAttribute("class").toString().contains("enabled")) {
            return true;
        }
        return false;
    }


    @Override
    public void validateTheSevenDaysIsAvailableInScheduleTable() throws Exception {
        waitForSeconds(5);
        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20) && weekScheduleShiftsDateOfMySchedule.size() == 7 && isElementLoaded(currentActiveWeek, 15)) {
            String activeWeek = currentActiveWeek.getText();
            String weekScheduleShiftStartDay = weekScheduleShiftsDateOfMySchedule.get(0).getText();
            String weekScheduleShiftEndDay = weekScheduleShiftsDateOfMySchedule.get(6).getText();
            if (activeWeek.contains("-") && activeWeek.contains("\n") && weekScheduleShiftStartDay.contains(",") && weekScheduleShiftEndDay.contains(",")) {
                try {
                    activeWeek = activeWeek.split("\n")[0];
                    weekScheduleShiftStartDay = weekScheduleShiftStartDay.split(",")[0].substring(0, 3);
                    weekScheduleShiftEndDay = weekScheduleShiftEndDay.split(",")[0].substring(0, 3);
                    SimpleUtils.report("weekScheduleShiftStartDay is " + weekScheduleShiftStartDay);
                    SimpleUtils.report("weekScheduleShiftEndDay is " + weekScheduleShiftEndDay);
                    SimpleUtils.report("activeWeek is " + activeWeek);
                    if (weekScheduleShiftStartDay.equalsIgnoreCase(activeWeek.split("-")[0].trim()) && weekScheduleShiftEndDay.equalsIgnoreCase(activeWeek.split("-")[1].trim())) {
                        SimpleUtils.pass("My Schedule Page: Seven days - Sunday to Saturday show in the schedule table successfully");
                        //todo according to the operation hours
                    } else
                        SimpleUtils.fail("My Schedule Page: Seven days - Sunday to Saturday failed to show in the schedule table", true);
                } catch (Exception e) {
                    SimpleUtils.fail("My Schedule Page: The schedule shifts texts don't have enough length ", true);
                }
            }
        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
        } else {
            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
        }
    }


    @FindBy(css = "div.sch-day-view-grid-header span")
    private List<WebElement> scheduleShiftTimeOnHeader;
    @Override
    public String getTheEarliestAndLatestTimeInScheduleTable() throws Exception {
        String operationStartTimeInScheduleTable = null;
        String operationEndTimeInScheduleTable = null;
        if (areListElementVisible(scheduleShiftTimeOnHeader, 30)) {
            if (scheduleShiftTimeOnHeader.size() >= 2) {
                if (scheduleShiftTimeOnHeader.get(0).getText().contains("AM")) {
                    operationStartTimeInScheduleTable = scheduleShiftTimeOnHeader.get(0).getText().replaceAll("[^0-9]", "");
                    if (operationStartTimeInScheduleTable.equals("12"))
                        operationStartTimeInScheduleTable = "0";
                } else if (scheduleShiftTimeOnHeader.get(0).getText().contains("PM")) {
                    operationStartTimeInScheduleTable = scheduleShiftTimeOnHeader.get(0).getText().replaceAll("[^0-9]", "");
                    if (operationStartTimeInScheduleTable.equals("24"))
                        operationStartTimeInScheduleTable = "12";
                } else
                    operationStartTimeInScheduleTable = scheduleShiftTimeOnHeader.get(1).getText().replaceAll("[^0-9]", "");
                if (scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().contains("AM")) {
                    operationEndTimeInScheduleTable = scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().replaceAll("[^0-9]", "");
                    if (operationEndTimeInScheduleTable.equals("12"))
                        operationEndTimeInScheduleTable = "0";
                } else if (scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().contains("PM")) {
                    operationEndTimeInScheduleTable = String.valueOf(Integer.valueOf(scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 1).getText().replaceAll("[^0-9]", "")) + 12);
                    if (operationEndTimeInScheduleTable.equals("24"))
                        operationEndTimeInScheduleTable = "12";
                } else
                    operationEndTimeInScheduleTable = String.valueOf(Integer.valueOf(scheduleShiftTimeOnHeader.get(scheduleShiftTimeOnHeader.size() - 2).getText().replaceAll("[^0-9]", "")) + 12);
            } else SimpleUtils.fail("My Schedule Page: The operation hours shows wrong", true);
        } else if (isElementLoaded(myScheduleNoSchedule, 20))
            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
        else
            SimpleUtils.fail("My Schedule Page: The operation hours failed to load", true);
        return operationStartTimeInScheduleTable + "-" + operationEndTimeInScheduleTable;
    }


    @Override
    public void compareOperationHoursBetweenAdminAndTM(String theEarliestAndLatestTimeInScheduleSummary, String theEarliestAndLatestTimeInScheduleTable) throws Exception {
        if (theEarliestAndLatestTimeInScheduleSummary.contains("-") && theEarliestAndLatestTimeInScheduleTable.contains("-")) {
            if ((Integer.valueOf(theEarliestAndLatestTimeInScheduleSummary.split("-")[0]) <= Integer.valueOf(theEarliestAndLatestTimeInScheduleTable.split("-")[0])) && (Integer.valueOf(theEarliestAndLatestTimeInScheduleSummary.split("-")[1]) >= Integer.valueOf(theEarliestAndLatestTimeInScheduleTable.split("-")[1]))) {
                SimpleUtils.pass("My Schedule Page: Seven days - Sunday to Saturday show in the schedule table according to the operating hours");
            } else
                SimpleUtils.fail("My Schedule Page: Seven days - Sunday to Saturday don't show in the schedule table according to the operating hours", false);
        } else
            SimpleUtils.fail("My Schedule Page: Operation hours display wrong, please check whether the shift is generated and published", false);
    }


    @FindBy(css = ".sch-day-view-shift-time")
    private List<WebElement> weekScheduleShiftsTimeOfMySchedule;
    @Override
    public void validateThatHoursAndDateIsVisibleOfShifts() throws Exception {
        if (areListElementVisible(weekScheduleShiftsTimeOfMySchedule, 20) && areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20) && weekScheduleShiftsDateOfMySchedule.size() == 7) {
            for (int i = 0; i < weekScheduleShiftsDateOfMySchedule.size(); i++) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd");
                try {
                    String date = weekScheduleShiftsDateOfMySchedule.get(i).getText();
                    sdf.parse(date.trim());
                    SimpleUtils.pass("My Schedule Page: Result Shifts show with shift date " + date.trim() + " successfully");
                } catch (Exception e) {
                    SimpleUtils.fail("My Schedule Page: Shifts don't show a legal DateTime type", false);
                }
            }
        } else if (weekScheduleShiftsTimeOfMySchedule.size() == 0) {
            SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
            for (int i = 0; i < weekScheduleShiftsTimeOfMySchedule.size(); i++) {
                if (weekScheduleShiftsTimeOfMySchedule.get(i).getText().contains("am") || weekScheduleShiftsTimeOfMySchedule.get(i).getText().contains("pm"))
                    SimpleUtils.pass("My Schedule Page: Result Shifts show with shift hours " + weekScheduleShiftsTimeOfMySchedule.get(i).getText() + " successfully");
                else
                    SimpleUtils.fail("My Schedule Page: Result Shifts failed to show with shift hours " + weekScheduleShiftsTimeOfMySchedule.get(i).getText(), true);
            }
        } else if (isElementLoaded(myScheduleNoSchedule, 10)) {
            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
        } else {
            SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
        }
    }


    @FindBy (css = ".lg-picker-input__wrapper .lg-search-options")
    private WebElement locationDropDown;

    @FindBy(css = "div.lg-picker-input")
    private WebElement currentLocationOnSchedulePage;
    @Override
    public void validateTheDisabilityOfLocationSelectorOnSchedulePage() throws Exception {
        if (isElementLoaded(currentLocationOnSchedulePage, 10)) {
            if (currentLocationOnSchedulePage.getCssValue("cursor").contains("not-allowed"))
                SimpleUtils.pass("My Schedule Page: Location selector is in disable mode");
            else if (getDriver().findElement(By.cssSelector("lg-upperfield-navigation div.lg-picker-input")).equals(currentLocationOnSchedulePage)) {
                click(currentLocationOnSchedulePage);
                if (isElementLoaded(locationDropDown,5))
                    SimpleUtils.pass("My Schedule Page: Location selector can be clicked since upperfield is enabled");
                else
                    SimpleUtils.fail("My Schedule Page: Location selector cannot be clicked when upperfield is enabled", true);
            } else
                SimpleUtils.fail("My Schedule Page: Location selector is still in enable mode",false);
        } else SimpleUtils.fail("My Schedule Page: Location failed to load", true);
    }



    @Override
    public void validateTheAvailabilityOfInfoIcon() throws Exception {
        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20)) {
            if (hoverIcons.size() != 0) {
                if (getDriver().findElements(By.xpath("//*[contains(@class,'right-shift-box')]/div/div[1]")).size() == hoverIcons.size())
                    SimpleUtils.pass("My Schedule Page: Info icon is present at the right side of a shift successfully");
                else
                    SimpleUtils.fail("My Schedule Page: Info icon isn't present at the right side of a shift", false);
            } else if (hoverIcons.size() == 0)
                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
        } else if (isElementLoaded(myScheduleNoSchedule, 10))
            SimpleUtils.report("My Schedule Page: Schedule has not been generated.");
        else SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
    }


    @FindBy(className = "popover-content")
    private WebElement popOverContent;
    @Override
    public void validateInfoIconClickable() throws Exception {
        if (areListElementVisible(weekScheduleShiftsDateOfMySchedule, 20)) {
            if (hoverIcons.size() != 0) {
                int randomIndex = (new Random()).nextInt(hoverIcons.size());
                clickTheElement(hoverIcons.get(randomIndex));
                if (isElementLoaded(popOverContent, 5)) {
                    SimpleUtils.pass("My Schedule Page: Info icon is clickable successfully");
                    List<WebElement> hoverSubContainers = popOverContent.findElements(By.className("hover-sub-container"));
                    if (hoverSubContainers.size() == 3) {
                        String shiftRole = hoverSubContainers.get(0).findElement(By.cssSelector(".shift-hover-subheading")).getText();
                        String timing = hoverSubContainers.get(1).getText();
                        String duration = hoverSubContainers.get(2).getText();
                        if (shiftRole != null && (timing.contains("am") || timing.contains("pm")) && duration.contains("Hrs this week")) {
                            SimpleUtils.pass("My Schedule Page: Info icon has the shift details like duration, timing and shift role");
                        } else
                            SimpleUtils.fail("My Schedule Page: Info icon has the shift details like duration, timing and shift role", true);
                    }
                    scrollToBottom();
                    click(hoverIcons.get(randomIndex));
                }
            } else if (hoverIcons.size() == 0)
                SimpleUtils.report("My Schedule Page: No shift hours in the schedule table");
        } else if (isElementLoaded(myScheduleNoSchedule, 10))
            SimpleUtils.report("My Schedule Page: Schedule has not been generated");
        else SimpleUtils.fail("My Schedule Page: Failed to load shifts", true);
    }

    @FindBy(className = "sch-calendar-day-label")
    private List<WebElement> weekDayLabels;
    @Override
    public void verifyShiftsAreSwapped(List<String> swapData) throws Exception {
        int swapRequestIndex1 = -1;
        int swapRequestIndex2 = -1;
        String[] swapData1 = swapData.get(0).split("\n");
        String[] swapData2 = swapData.get(1).split("\n");
        if (areListElementVisible(weekDayLabels, 10)) {
            for (int i = 0; i < weekDayLabels.size(); i++) {
                if (weekDayLabels.get(i).getText().equalsIgnoreCase(swapData1[3].substring(0, 3))) {
                    swapRequestIndex1 = i;
                    SimpleUtils.pass("Get the index of " + swapData1[3] + ", the index is: " + i);
                }
                if (weekDayLabels.get(i).getText().equalsIgnoreCase(swapData2[3].substring(0, 3))) {
                    swapRequestIndex2 = i;
                    SimpleUtils.pass("Get the index of " + swapData2[3] + ", the index is: " + i);
                }
            }

            List<WebElement> workerNames1 = getDriver().findElements(By.cssSelector("[data-day-index=\"" + swapRequestIndex1 + "\"] .week-schedule-shift-wrapper .week-schedule-worker-name"));
            List<WebElement> workerNames2 = getDriver().findElements(By.cssSelector("[data-day-index=\"" + swapRequestIndex2 + "\"] .week-schedule-shift-wrapper .week-schedule-worker-name"));
            for (WebElement workerName1 : workerNames1) {
                if (workerName1.getText().equals(swapData1[0])) {
                    SimpleUtils.fail("Swap failed, still can find the swap Name: " + swapData1[0] + " at: " + swapData1[3], false);
                }
                if (workerName1.getText().equals(swapData2[0])) {
                    SimpleUtils.pass("Swap Successfully, can find the swap Name: " + swapData2[0] + " at: " + swapData1[3]);
                }
            }
            for (WebElement workerName2 : workerNames2) {
                if (workerName2.getText().equals(swapData2[0])) {
                    SimpleUtils.fail("Swap failed, still can find the swap Name: " + swapData2[0] + " at: " + swapData2[3], false);
                }
                if (workerName2.getText().equals(swapData1[0])) {
                    SimpleUtils.pass("Swap Successfully, can find the swap Name: " + swapData1[0] + " at: " + swapData2[3]);
                }
            }
        }else {
            SimpleUtils.fail("Week Day Labels not loaded Successfully!", false);
        }
    }

    public void clickCloseDialogButton () throws Exception {
        if (isElementLoaded(closeDialogBtn, 5)) {
            clickTheElement(closeDialogBtn);
        } else
            SimpleUtils.report("Close button fail to load! ");
    }


    @Override
    public void selectSchedulFilter(String option) throws Exception {
        if (isElementLoaded(filterButton, 5)) {
            clickTheElement(filterButton);
            if (areListElementVisible(shiftTypes, 10) && shiftTypes.size() > 0) {
                for (WebElement shiftType : shiftTypes) {
                    if (shiftType.getText().contains(option)) {
                        WebElement filterCheckBox = shiftType.findElement(By.tagName("input"));
                        if (filterCheckBox.getAttribute("class").contains("ng-empty")) {
                            click(filterCheckBox);
                        }
                    } else {
                        WebElement filterCheckBox = shiftType.findElement(By.tagName("input"));
                        if (filterCheckBox.getAttribute("class").contains("ng-not-empty")) {
                            clickTheElement(filterCheckBox);
                        }
                    }
                }
            } else
                SimpleUtils.fail("My Schedule Page: No schedule shift type can be applied", true);
            //Click again to close the pop up menu
            clickTheElement(filterButton);
        } else
            SimpleUtils.fail("My Schedule Page: Filter button failed to load", true);
    }
    @FindBy(css = "[ng-repeat-start*=\"shift in results\"]")
    private List<WebElement> coverRequestStatus;
    public boolean checkIfTMExitsInCoverOrSwapRequestList (String tmName) {
        boolean isExists = false;
        if ((areListElementVisible(comparableShifts, 10) && comparableShifts.size()!=0 )
                || (areListElementVisible(coverRequestStatus, 10) && coverRequestStatus.size() != 0)) {
            if (areListElementVisible(comparableShifts, 3)) {
                for (WebElement shift: comparableShifts) {
                    WebElement name = shift.findElement(By.cssSelector(".shift-swap-modal-table-name"));
                    if (name.getText().equalsIgnoreCase(tmName)) {
                        isExists = true;
                        break;
                    }
                }
            } else {
                for (WebElement shift: coverRequestStatus) {
                    WebElement name = shift.findElement(By.cssSelector(".shift-swap-modal-table-name"));
                    if (name.getText().equalsIgnoreCase(tmName)) {
                        isExists = true;
                        break;
                    }
                }
            }
        } else
            SimpleUtils.report("Comparable shifts or cover request list are fail to load! ");
        return isExists;
    }

    @Override
    public int getCountOfCoverOrSwapRequestsInList() {
        int count = 0;
        if ((areListElementVisible(comparableShifts, 10) && comparableShifts.size()!=0 )
                || (areListElementVisible(coverRequestStatus, 10) && coverRequestStatus.size() != 0)) {
            if (areListElementVisible(comparableShifts)) {
                count = comparableShifts.size();
            } else {
                count = coverRequestStatus.size();
            }
        } else
            SimpleUtils.report("Comparable shifts or cover request list are fail to load! ");
        return count;
    }


    @Override
    public void clickTheShiftRequestToClaimCoverShift(String requestName) throws Exception {
        boolean isCoverRequest = false;
        if (areListElementVisible(tmIcons, 15)) {
            for (WebElement tmIcon : tmIcons) {
                moveToElementAndClick(tmIcon);
                try {
                    if (isPopOverLayoutLoaded() && isElementLoaded(popOverLayout.findElement(By.cssSelector(".sch-worker-comment-comment")), 5)
                            && popOverLayout.findElement(By.cssSelector(".sch-worker-comment-comment")).getText().contains("Hey, I have a commitment")) {
                        isCoverRequest = true;
                        clickTheElement(popOverLayout.findElement(By.cssSelector("span.sch-worker-action-label")));
                        SimpleUtils.pass("Click " + requestName + " button Successfully!");
                        break;
                    }
                } catch (Exception e) {
                    // Do nothing
                }
            }
            if (!isCoverRequest) {
                SimpleUtils.fail("Failed to find the cover request!", false);
            }
        } else {
            SimpleUtils.fail("Team Members' Icons not loaded", false);
        }
    }


    @Override
    public String getStyleOfShiftByIndex(int index) throws Exception {
        String style = "";
        if (areListElementVisible(shiftsWeekView, 5)){
            if (index> shiftsWeekView.size()-1){
                SimpleUtils.fail("The index is more than shift count! ", false);
            } else {
                style = shiftsWeekView.get(index).getAttribute("style");
                SimpleUtils.pass("Get shift style successfully! The style is:"+style);
            }
        } else
            SimpleUtils.fail("The shifts on My Schedule page fail to load! ", false);
        return style;
    }

    @Override
    public boolean checkIfThePopupMessageOnTop(String expectedMessage) throws Exception {
        boolean theMessageLoaded = false;
        if (isElementLoaded(msgOnTop, 20)) {
            String message = msgOnTop.getText();
            if (message.contains(expectedMessage)) {
                theMessageLoaded = true;
                SimpleUtils.pass("Verified Message shows correctly!");
            }else {
                SimpleUtils.report("Message on top is incorrect, expected is: " + expectedMessage + ", but actual is: " + message);
            }
        }else {
            SimpleUtils.report("Message on top not loaded Successfully!");
        }
        return theMessageLoaded;
    }

    @FindBy (css = ".sch-day-view-shift-worker-title-role.ng-binding")
    private List<WebElement> workRoles;
    @Override
    public void clickAnyShiftInOpenShiftGroup(int count, String shiftWorkRole) throws Exception {
        List<String> expectedShift = new ArrayList<>(Arrays.asList("View Group Offer" + "(" + count + ")"));
        if (areListElementVisible(tmIcons, 15) && tmIcons.size() > 0) {
            for (int i = 0; i < tmIcons.size(); i++) {
                if(workRoles.get(i).getText().trim().equalsIgnoreCase(shiftWorkRole)){
                    scrollToElement(tmIcons.get(i));
                    waitForSeconds(1);
                    clickTheElement(tmIcons.get(i));
                }else
                    continue;
                if (isPopOverLayoutLoaded())
                    verifyShiftRequestButtonOnPopup(expectedShift);
                else
                    SimpleUtils.fail("Open shift group view request is not loaded!", false);
            }
        } else
            SimpleUtils.fail("Open shift group not displayed!", false);
    }

    @Override
    public boolean isOpenShiftGroupDisplayed(int count, String shiftWorkRole) throws Exception {
        String expectedShiftViewInfo = "View Group Offer " + "(" + count + ")";
        boolean isOpenShiftGroupDisplayed = false;
        String actualShiftViewInfo;
        if (areListElementVisible(workRoles, 15) && workRoles.size() > 0) {
            for (int i = 0; i < workRoles.size(); i++) {
                if(workRoles.get(i).getText().trim().equalsIgnoreCase(shiftWorkRole)) {
                    scrollToElement(tmIcons.get(i));
                    waitForSeconds(1);
                    clickTheElement(tmIcons.get(i));
                    if (isPopOverLayoutLoaded()) {
                        actualShiftViewInfo = popOverLayout.findElement(By.cssSelector("span.sch-worker-action-label")).getText().trim();
                        if (actualShiftViewInfo.equalsIgnoreCase(expectedShiftViewInfo)) {
                            isOpenShiftGroupDisplayed = true;
                            SimpleUtils.report("Specific open shift group is loaded!");
                            break;
                        }
                    } else
                        SimpleUtils.fail("Action list not pop up after click open shift", false);
                }
            }
        } else
            SimpleUtils.fail("No available open shift/open shift group!", false);
        return isOpenShiftGroupDisplayed;
    }

    @FindBy(className = "accept-shift-shift-info")
    private List<WebElement> shiftsDetail;
    @FindBy(css = ".accept-shift-header-title.ng-binding")
    private WebElement shiftGroupTitle;
    @Override
    public void checkOpenShiftGroup(int count, String shiftDuration, String shiftWorkRole, ArrayList<String> weekDays, ArrayList<String> specificDates, String location) throws Exception {
//        List<String> expectedShifts = new ArrayList<>(Arrays.asList("View Group Offer" + "(" + count + ")"));
//        if (areListElementVisible(tmIcons, 15) && tmIcons.size() > 0) {
//            for (int i = 0; i < tmIcons.size(); i++) {
//                if(workRoles.get(i).getText().trim().equalsIgnoreCase(shiftWorkRole)){
//                    scrollToElement(tmIcons.get(i));
//                    waitForSeconds(1);
//                    clickTheElement(tmIcons.get(i));
//                }else
//                    continue;
//                if (isPopOverLayoutLoaded())
//                    verifyShiftRequestButtonOnPopup(expectedShifts);
//                else
//                    SimpleUtils.fail("Open shift group view request is not loaded!", false);
//            }
//        } else
//            SimpleUtils.fail("Open shift group not displayed!", false);
        clickAnyShiftInOpenShiftGroup(count, shiftWorkRole);
        List<String> expectedShifts = new ArrayList<>(Arrays.asList("View Group Offer" + "(" + count + ")"));
        ArrayList<String> dates = new ArrayList<String>();
        dates = specificDates;
//        if (areListElementVisible(workRoles, 15) && workRoles.size() > 0) {
//            int i = 0;
//            for (; i<workRoles.size(); i++){
//                if(workRoles.get(i).getText().trim().equalsIgnoreCase(shiftWorkRole)){
//                    scrollToElement(tmIcons.get(i));
//                    waitForSeconds(1);
//                    clickTheElement(tmIcons.get(i));
//                    break;
//                }
//            }
        if (isPopOverLayoutLoaded()) {
            verifyShiftRequestButtonOnPopup(expectedShifts);
            clickTheElement(popOverLayout.findElement(By.cssSelector("span.sch-worker-action-label")));
            if(isElementLoaded(shiftGroupTitle) && shiftGroupTitle.getText().trim().contains("Open Shift Group (" + count + ")")) {
                if (areListElementVisible(shiftsDetail, 5)) {
                    for (int dayOfWeek = 0; dayOfWeek < shiftsDetail.size(); dayOfWeek++) {
                        String detail = shiftsDetail.get(dayOfWeek).getText().toLowerCase().replaceAll("\\s*", "");
                        if (detail.contains(shiftWorkRole.toLowerCase().trim()) && detail.contains(weekDays.get(dayOfWeek).toLowerCase().trim())
                                && detail.contains(location.toLowerCase().trim()) && detail.contains(shiftDuration.trim())
                                && detail.contains(dates.get(dayOfWeek).toLowerCase().trim()))
                            SimpleUtils.pass("Shift detail is correct: " + detail);
                        else
                            SimpleUtils.fail("Shift detail is incorrect!: " + shiftWorkRole.toLowerCase().trim() + weekDays.get(dayOfWeek).toLowerCase().trim() + location.toLowerCase().trim() + shiftDuration.trim() + dates.get(dayOfWeek).toLowerCase().trim() + "The expected one is: " + detail, false);
                    }
                } else
                    SimpleUtils.fail("Shift detail dialog is not loaded!", false);
            }else{
                SimpleUtils.fail("Shift group title is not displayed correctly!", false);}
        }else
            SimpleUtils.fail("Popover layout is not loaded!",false);
    }

    public void cancelClaimOpenShiftGroupRequest(List<String> expectedRequests, String shiftWorkRole) throws Exception {
        List<String> claimStatus = new ArrayList<>(Arrays.asList("Claim Shift Approval Pending", "Cancel Claim Request"));
        String expectedMessage = "Cancelled successfully";
        if (areListElementVisible(tmIcons, 15) && tmIcons.size() > 0) {
            for (int i = 0; i < tmIcons.size(); i++) {
                if(areListElementVisible(workRoles, 15) && workRoles.size() > 0) {
                    if (workRoles.get(i).getText().trim().equalsIgnoreCase(shiftWorkRole)) {
                        scrollToElement(tmIcons.get(i));
                        waitForSeconds(1);
                        clickTheElement(tmIcons.get(i));
                    } else
                        continue;
                    if (isPopOverLayoutLoaded()){
                        verifyShiftRequestButtonOnPopup(claimStatus);
                        clickTheShiftRequestByName(claimStatus.get(1));
                        verifyReConfirmDialogPopup();
                        verifyClickOnYesButton();
                        verifyThePopupMessageOnTop(expectedMessage);
                    }else
                        SimpleUtils.fail("Open shift group view request is not loaded!", false);
                }else
                    SimpleUtils.fail("Shifts work roles are not loaded!",false);
            }
        }else
            SimpleUtils.fail("Team Members' Icons not loaded Successfully!", false);
    }

    @FindBy(css = "[id*=\"legion_cons_Schedule_Schedule_day\"]")
    private List<WebElement> datesOfWeek;
    @Override
    public ArrayList<String> getAllWeekDays() throws Exception {
        ArrayList<String> workDays = new ArrayList<String>();
        String[] workDay = null;
        String expectedDate;
        if (areListElementVisible(datesOfWeek, 15) && datesOfWeek.size() > 0) {
            for(int i = 0; i<datesOfWeek.size(); i++){
                workDay = datesOfWeek.get(i).getText().split("\n");
                workDay = workDay[1].split(" ");
                expectedDate = workDay[0] + workDay[1];
                workDays.add(expectedDate);
            }
        } else
            SimpleUtils.fail("Work days of schedule week are not loaded!", false);
        return workDays;
    }

    @Override
    public ArrayList<String> getAllWeekDaysForActivityDetailVerification() throws Exception {
        ArrayList<String> workDays = new ArrayList<String>();
        String[] workDay = null;
        String expectedDate;
        if (areListElementVisible(datesOfWeek, 15) && datesOfWeek.size() > 0) {
            for(int i = 0; i<datesOfWeek.size(); i++){
                workDay = datesOfWeek.get(i).getText().split("\n");
                expectedDate = workDay[1];
                workDays.add(expectedDate);
            }
        } else
            SimpleUtils.fail("Work days of schedule week are not loaded!", false);
        return workDays;
    }

}
