package com.legion.pages.core.schedule;

import com.legion.pages.BasePage;
import com.legion.pages.CreateSchedulePage;
import com.legion.pages.ScheduleCommonPage;
import com.legion.pages.ScheduleMainPage;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.an.E;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleScheduleCommonPage extends BasePage implements ScheduleCommonPage {
    public ConsoleScheduleCommonPage() {
        PageFactory.initElements(getDriver(), this);
    }

    private static HashMap<String, String> propertyTimeZoneMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/LocationTimeZone.json");


    @FindBy(className = "console-navigation-item")
    private List<WebElement> consoleNavigationMenuItems;

    @FindBy(css = "div.console-navigation-item-label.Schedule")
    private WebElement goToScheduleButton;

    @FindBy(css = "#legion-app navigation div:nth-child(4)")
    private WebElement analyticsConsoleName;

    @FindBy(css = "div[helper-text*='Work in progress Schedule'] span.legend-label")
    private WebElement draft;

    @FindBy(css = "div[helper-text-position='top'] span.legend-label")
    private WebElement published;

    @FindBy(css = "div[helper-text*='final per schedule changes'] span.legend-label")
    private WebElement finalized;

    @FindBy(className = "overview-view")
    private WebElement overviewSectionElement;

    @FindBy(css = "lg-button[data-tootik=\"Edit Schedule\"]")
    private WebElement newEdit;

    @FindBy(css = "[ng-click=\"callOkCallback()\"]")
    private WebElement editAnywayPopupButton;

    final static String consoleScheduleMenuItemText = "Schedule";


    public void clickOnScheduleConsoleMenuItem() {
        if (areListElementVisible(consoleNavigationMenuItems, 10) && consoleNavigationMenuItems.size() != 0) {
            WebElement consoleScheduleMenuElement = SimpleUtils.getSubTabElement(consoleNavigationMenuItems, consoleScheduleMenuItemText);
            clickTheElement(consoleScheduleMenuElement);
            SimpleUtils.pass("'Schedule' Console Menu Loaded Successfully!");
        } else {
            SimpleUtils.fail("'Schedule' Console Menu Items Not Loaded Successfully!", false);
        }
    }

    @Override
    public void goToSchedulePage() throws Exception {

        checkElementVisibility(goToScheduleButton);
        activeConsoleName = analyticsConsoleName.getText();
        click(goToScheduleButton);
        SimpleUtils.pass("Schedule Page Loading..!");

        if (isElementLoaded(draft)) {
            SimpleUtils.pass("Draft is Displayed on the page");
        } else {
            SimpleUtils.fail("Draft not displayed on the page", true);
        }

        if (isElementLoaded(published)) {
            SimpleUtils.pass("Published is Displayed on the page");
        } else {
            SimpleUtils.fail("Published not displayed on the page", true);
        }

        if (isElementLoaded(finalized)) {
            SimpleUtils.pass("Finalized is Displayed on the page");
        } else {
            SimpleUtils.fail("Finalized not displayed on the page", true);
        }
    }


    @Override
    public boolean isSchedulePage() throws Exception {
        if (isElementLoaded(overviewSectionElement)) {
            return true;
        } else {
            return false;
        }
    }


    @FindBy(className = "day-week-picker-arrow-right")
    private WebElement calendarNavigationNextWeekArrow;

    @FindBy(className = "day-week-picker-arrow-left")
    private WebElement calendarNavigationPreviousWeekArrow;
    @FindBy(id = "legion_cons_Schedule_Schedule_Week_btn")
    private WebElement weekSubTabBtn;
    @FindBy(id = "legion_cons_Schedule_Schedule_MultiWeek_btn")
    private WebElement multiWeekTabBtn;

    @Override
    public void clickOnWeekView() throws Exception {
		/*WebElement scheduleWeekViewButton = MyThreadLocal.getDriver().
			findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'week')\"]"));*/

        WebElement scheduleWeekViewButton = null;
        if (isElementLoaded(weekSubTabBtn, 5)) {
            scheduleWeekViewButton = weekSubTabBtn;
        } else {
            scheduleWeekViewButton = MyThreadLocal.getDriver().
                    findElement(By.cssSelector("div.lg-button-group-last"));
        }
        if (isElementLoaded(scheduleWeekViewButton,15)) {
            if (!scheduleWeekViewButton.getAttribute("class").toString().contains("selected"))//selected
            {
                clickTheElement(scheduleWeekViewButton);
            }
            SimpleUtils.pass("Schedule page week view loaded successfully!");
        } else {
            SimpleUtils.fail("Schedule Page Week View Button not Loaded Successfully!", true);
        }
    }

    @Override
    public void clickOnMultiWeekView() throws Exception {
        WebElement scheduleMultiWeekViewButton = null;
        if (isElementLoaded(multiWeekTabBtn, 5)) {
            scheduleMultiWeekViewButton = multiWeekTabBtn;
        } else {
            scheduleMultiWeekViewButton = MyThreadLocal.getDriver().
                    findElement(By.cssSelector("div.lg-button-group-last"));
        }
        if (isElementLoaded(scheduleMultiWeekViewButton,15)) {
            if (!scheduleMultiWeekViewButton.getAttribute("class").toString().contains("selected"))//selected
            {
                clickTheElement(scheduleMultiWeekViewButton);
            }
            SimpleUtils.pass("Schedule page multi week view loaded successfully!");
        } else {
            SimpleUtils.fail("Schedule Page Multi-Week View Button not Loaded Successfully!", true);
        }
    }


    @Override
    public void clickOnDayView() throws Exception {
		/*WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
			findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'day')\"]"));*/
        WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
                findElement(By.cssSelector("div.lg-button-group-first"));

        if (isElementLoaded(scheduleDayViewButton)) {
            if (!scheduleDayViewButton.getAttribute("class").toString().contains("enabled")) {
                clickTheElement(scheduleDayViewButton);
                waitForSeconds(3);
            }
            SimpleUtils.pass("Schedule Page day view loaded successfully!");
        } else {
            SimpleUtils.fail("Schedule Page Day View Button not Loaded Successfully!", true);
        }
    }

    @Override
    public void navigateDayViewWithIndex(int dayIndex) {
        if (dayIndex < 7 && dayIndex >= 0) {
            try {
                clickOnDayView();
                List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
                if (ScheduleCalendarDayLabels.size() == 7) {
                    click(ScheduleCalendarDayLabels.get(dayIndex));
                }
            } catch (Exception e) {
                SimpleUtils.fail("Unable to navigate to in Day View", false);
            }
        } else {
            SimpleUtils.fail("Invalid dayIndex value to verify Store is Closed for the day", false);
        }

    }

    @Override
    public void navigateDayViewWithDayName(String dayName) throws Exception {
        // The day name should be: Fri, Sat, Sun, Mon, Tue, Wed, Thu
        clickOnDayView();
        List<WebElement> scheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
        if (scheduleCalendarDayLabels.size() == 7) {
            boolean isDayNameExists = false;
            for (WebElement day: scheduleCalendarDayLabels ){
                if (day.getText().contains(dayName)) {
                    click(day);
                    isDayNameExists = true;
                    break;
                }
            }
            if(!isDayNameExists){
                SimpleUtils.fail("The day name is not exists", false);
            }
        } else
            SimpleUtils.fail("Week day picker display incorrectly! ", false);
    }



    @FindBy(css = "div.holiday-logo-container")
    private WebElement holidayLogoContainer;

    @Override
    public void navigateWeekViewOrDayViewToPastOrFuture(String nextWeekViewOrPreviousWeekView, int weekCount) {
        String currentWeekStartingDay = "NA";
        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
        for (int i = 0; i < weekCount; i++) {
            if (ScheduleCalendarDayLabels.size() != 0) {
                currentWeekStartingDay = ScheduleCalendarDayLabels.get(0).getText();
            }

            int displayedWeekCount = ScheduleCalendarDayLabels.size();
            for (WebElement ScheduleCalendarDayLabel : ScheduleCalendarDayLabels) {
                if (ScheduleCalendarDayLabel.getAttribute("class").toString().contains("day-week-picker-period-active")) {
                    if (nextWeekViewOrPreviousWeekView.toLowerCase().contains("next") || nextWeekViewOrPreviousWeekView.toLowerCase().contains("future")) {
                        try {
                            int activeWeekIndex = ScheduleCalendarDayLabels.indexOf(ScheduleCalendarDayLabel);
                            if (activeWeekIndex < (displayedWeekCount - 1)) {
                                click(ScheduleCalendarDayLabels.get(activeWeekIndex + 1));
                            } else {
                                click(calendarNavigationNextWeekArrow);
                                click(ScheduleCalendarDayLabels.get(0));
                            }
                        } catch (Exception e) {
                            SimpleUtils.report("Schedule page Calender Next Week Arrows Not Loaded/Clickable after '" + ScheduleCalendarDayLabel.getText().replace("\n", "") + "'");
                        }
                    } else {
                        try {
                            int activeWeekIndex = ScheduleCalendarDayLabels.indexOf(ScheduleCalendarDayLabel);
                            if (activeWeekIndex > 0) {
                                click(ScheduleCalendarDayLabels.get(activeWeekIndex - 1));
                            } else {
                                click(calendarNavigationPreviousWeekArrow);
                                click(ScheduleCalendarDayLabels.get(displayedWeekCount - 1));
                            }
                        } catch (Exception e) {
                            SimpleUtils.fail("Schedule page Calender Previous Week Arrows Not Loaded/Clickable after '" + ScheduleCalendarDayLabel.getText().replace("\n", "") + "'", true);
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void navigateToNextDayIfStoreClosedForActiveDay() throws Exception {
        String dayType = "Next";
        int dayCount = 1;
        if (isStoreClosedForActiveWeek())
            navigateWeekViewOrDayViewToPastOrFuture(dayType, dayCount);
        if (!isStoreClosedForActiveWeek())
            SimpleUtils.pass("Navigated to Next day successfully!");
    }

    @Override
    public boolean isStoreClosedForActiveWeek() throws Exception {
        if (isElementLoaded(holidayLogoContainer, 10)) {
            SimpleUtils.report("Store is Closed for the Day/Week: '" + getActiveWeekText() + "'.");
            return true;
        }
        return false;
    }

    public void clickOnPreviousDaySchedule(String activeDay) throws Exception {
        List<WebElement> activeWeek = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
        for(int i=0; i<activeWeek.size();i++){
            String currentDay = activeWeek.get(i).getText().replace("\n", " ").substring(0,3);
            if(currentDay.equalsIgnoreCase(activeDay)){
                if(i== activeWeek.size()-1){
                    navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(),
                            ScheduleTestKendraScott2.weekCount.One.getValue());
                    waitForSeconds(3);
                }else{
                    click(activeWeek.get(i));
                }
            }
        }

    }

    public void clickOnNextDaySchedule(String activeDay) throws Exception {
        List<WebElement> activeWeek = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
        for(int i=0; i<activeWeek.size();i++){
            String currentDay = activeWeek.get(i).getText().replace("\n", " ").substring(0,3);
            if(currentDay.equalsIgnoreCase(activeDay)){
                if(i== activeWeek.size()-1){
                    navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(),
                            ScheduleTestKendraScott2.weekCount.One.getValue());
                    waitForSeconds(3);
                }else{
                    click(activeWeek.get(i+1));
                }
            }
        }
    }


    @Override
    public void validateForwardAndBackwardButtonClickable() throws Exception {
        String activeWeekText = getActiveWeekText();
        if (isElementLoaded(calendarNavigationNextWeekArrow, 10)) {
            try {
                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
                if (activeWeekText.equals(getActiveWeekText()))
                    SimpleUtils.pass("My Schedule Page: Forward and backward button to view previous or upcoming week is clickable successfully");
            } catch (Exception e) {
                SimpleUtils.fail("My Schedule Page: Exception occurs when clicking forward and backward button", true);
            }
        } else if (isElementLoaded(calendarNavigationPreviousWeekArrow, 10)) {
            try {
                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Previous.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
                navigateWeekViewOrDayViewToPastOrFuture(ScheduleTestKendraScott2.weekViewType.Next.getValue(), ScheduleTestKendraScott2.weekCount.Three.getValue());
                if (activeWeekText.equals(getActiveWeekText()))
                    SimpleUtils.pass("My Schedule Page: Forward and backward button to view previous or upcoming week is clickable successfully");
            } catch (Exception e) {
                SimpleUtils.fail("My Schedule Page: Exception occurs when clicking forward and backward button", true);
            }
        } else
            SimpleUtils.fail("My Schedule Page: Forward and backward button failed to load to view previous or upcoming week", true);
    }

    @FindBy(css = "div.sub-navigation-view-link")
    private List<WebElement> ScheduleSubTabsElement;
    @FindBy(css = "[slider-settings=\"schedulingSettings\"] div.sub-navigation-view-link.active")
    private WebElement activatedSubTabElement;

    @Override
    public void clickOnScheduleSubTab(String subTabString) throws Exception {
        waitForSeconds(5);
        if (areListElementVisible(ScheduleSubTabsElement, 10) && ScheduleSubTabsElement.size() != 0 && !verifyActivatedSubTab(subTabString)) {
            for (WebElement ScheduleSubTabElement : ScheduleSubTabsElement) {
                if (ScheduleSubTabElement.getText().equalsIgnoreCase(subTabString)) {
                    waitForSeconds(5);
                    clickTheElement(ScheduleSubTabElement);
                    waitForSeconds(3);
                    break;
                }
            }

        }

        if (verifyActivatedSubTab(subTabString)) {
            SimpleUtils.pass("Schedule Page: '" + subTabString + "' tab loaded Successfully!");
        } else {
            SimpleUtils.fail("Schedule Page: '" + subTabString + "' tab not loaded Successfully!", true);
        }
    }

    @Override
    public Boolean verifyActivatedSubTab(String SubTabText) throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(activatedSubTabElement,15)) {
            if (activatedSubTabElement.getText().toUpperCase().contains(SubTabText.toUpperCase())) {
                return true;
            }
        } else {
            SimpleUtils.fail("Schedule Page not loaded successfully", true);
        }
        return false;
    }

    public Boolean isScheduleDayViewActive() {
//        WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
//                findElement(By.cssSelector("[ng-click=\"selectDayWeekView($event, 'day')\"]"));
        WebElement scheduleDayViewButton = MyThreadLocal.getDriver().
                findElement(By.cssSelector("div.lg-button-group-first"));
        if (scheduleDayViewButton.getAttribute("class").contains("selected")) {
            return true;
        }
        return false;
    }

    public String getScheduleWeekStartDayMonthDate() {
        String scheduleWeekStartDuration = "NA";
        WebElement scheduleCalendarActiveWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
        try {
            if (isElementLoaded(scheduleCalendarActiveWeek)) {
                scheduleWeekStartDuration = scheduleCalendarActiveWeek.getText().replace("\n", " ");
            }
        } catch (Exception e) {
            SimpleUtils.fail("Calender duration bar not loaded successfully", true);
        }
        return scheduleWeekStartDuration;
    }

    public void navigateDayViewToPast(String PreviousWeekView, int dayCount)
    {
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        String currentWeekStartingDay = "NA";
        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
        for(int i = 0; i < dayCount; i++)
        {

            try {
                click(ScheduleCalendarDayLabels.get(i));
                scheduleMainPage.clickOnEditButton();
                scheduleMainPage.clickOnCancelButtonOnEditMode();
            } catch (Exception e) {
                SimpleUtils.fail("Schedule page Calender Previous Week Arrows Not Loaded/Clickable", true);
            }


        }
    }


    @FindBy(xpath = "//*[contains(@class,'day-week-picker-period-active')]/following-sibling::div[1]")
    private WebElement immediateNextToCurrentActiveWeek;

    @FindBy(xpath = "//*[contains(@class,'day-week-picker-period-active')]/preceding-sibling::div[1]")
    private WebElement immediatePastToCurrentActiveWeek;

    public void clickImmediateNextToCurrentActiveWeekInDayPicker() {
        if (isElementEnabled(immediateNextToCurrentActiveWeek, 30)) {
            click(immediateNextToCurrentActiveWeek);
        } else {
            SimpleUtils.report("This is a last week in Day Week picker");
        }
    }

    public void clickImmediatePastToCurrentActiveWeekInDayPicker() {
        if (isElementEnabled(immediatePastToCurrentActiveWeek, 30)) {
            click(immediatePastToCurrentActiveWeek);
        } else {
            SimpleUtils.report("This is a last week in Day Week picker");
        }
    }

    @Override
    public String getActiveWeekText() throws Exception {
        waitForSeconds(1);
        if (isElementLoaded(MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active")),15))
            return MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active")).getText().replace("\n", " ");
        return "";
    }

    public String getActiveAndNextDay() throws Exception{
        WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
        String activeDay = "";
        if(isElementLoaded(activeWeek)){
            activeDay = activeWeek.getText().replace("\n", " ").substring(0,3);
        }
        return activeDay;
    }

    @FindBy (css = "lg-button-group[buttons*=\"custom\"] div.lg-button-group-first")
    private WebElement scheduleDayViewButton;
    @FindBy (className = "period-name")
    private WebElement periodName;
    @Override
    public void isScheduleForCurrentDayInDayView(String dateFromDashboard) throws Exception {
        String tagName = "span";
        if (isElementLoaded(scheduleDayViewButton, 5) && isElementLoaded(periodName, 5)) {
            if (scheduleDayViewButton.getAttribute("class").contains("lg-button-group-selected")){
                SimpleUtils.pass("The Schedule Day View Button is selected!");
            }else{
                SimpleUtils.fail("The Schedule Day View Button isn't selected!", true);
            }
            /*
             * @periodName format "Schedule for Wednesday, February 12"
             */
            if (periodName.getText().contains(dateFromDashboard)) {
                SimpleUtils.pass("The Schedule is for current day!");
            }else{
                SimpleUtils.fail("The Schedule isn't for current day!", true);
            }
        }else{
            SimpleUtils.fail("The Schedule Day View Button isn't loaded!",true);
        }
    }


    public void currentWeekIsGettingOpenByDefault(String location) throws Exception {
        String jsonTimeZoon = propertyTimeZoneMap.get(location);
        TimeZone timeZone = TimeZone.getTimeZone(jsonTimeZoon);
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
        dfs.setTimeZone(timeZone);
        String currentTime =  dfs.format(new Date());
        Date currentDate = dfs.parse(currentTime);
        String weekBeginEndByCurrentDate = SimpleUtils.getThisWeekTimeInterval(currentDate);
        String weekBeginEndByCurrentDate2 = weekBeginEndByCurrentDate.replace("-","").replace(",","");
        String weekBeginBYCurrentDate = weekBeginEndByCurrentDate2.substring(6,8);
        String weekEndBYCurrentDate = weekBeginEndByCurrentDate2.substring(weekBeginEndByCurrentDate2.length()-2);
        SimpleUtils.report("weekBeginBYCurrentDate is : "+ weekBeginBYCurrentDate);
        SimpleUtils.report("weekEndBYCurrentDate is : "+ weekEndBYCurrentDate);
        String activeWeekText =getActiveWeekText();
        String weekDefaultBegin = activeWeekText.substring(14,16);
        SimpleUtils.report("weekDefaultBegin is :"+weekDefaultBegin);
        String weekDefaultEnd = activeWeekText.substring(activeWeekText.length()-2);
        SimpleUtils.report("weekDefaultEnd is :"+weekDefaultEnd);
        if (SimpleUtils.isNumeric(weekBeginBYCurrentDate.trim()) && SimpleUtils.isNumeric(weekDefaultBegin.trim()) &&
                SimpleUtils.isNumeric(weekEndBYCurrentDate.trim()) && SimpleUtils.isNumeric(weekDefaultEnd.trim())) {
            if (Math.abs(Integer.parseInt(weekBeginBYCurrentDate.trim()) - Integer.parseInt(weekDefaultBegin.trim())) <= 1 &&
                    Math.abs(Integer.parseInt(weekEndBYCurrentDate.trim()) - Integer.parseInt(weekDefaultEnd.trim())) <= 1 &&
                    (Math.abs(Integer.parseInt(weekBeginBYCurrentDate.trim()) - Integer.parseInt(weekDefaultBegin.trim())) ==
                            Math.abs(Integer.parseInt(weekEndBYCurrentDate.trim()) - Integer.parseInt(weekDefaultEnd.trim())))) {
                SimpleUtils.pass("Current week is getting open by default");
            } else {
                SimpleUtils.fail("Current week is not getting open by default", false);
            }
        }else {
            SimpleUtils.fail("The date is not the numeric format!", false);
        }
    }


    @FindBy (xpath = "//span[contains(text(),'Schedule')]")
    private WebElement ScheduleSubMenu;

    @FindBy(css = "[ng-click=\"showTodos($event)\"]")
    private WebElement todoButton;

    public void goToScheduleNewUI() throws Exception {

        if (isElementLoaded(goToScheduleButton,5)) {
            click(goToScheduleButton);
            click(ScheduleSubMenu);
            if (isElementLoaded(todoButton,5)) {
                SimpleUtils.pass("Schedule New UI load successfully");
            }
        }else{
            SimpleUtils.fail("Schedule New UI load failed", true);
        }

    }


    @FindBy(css = "div.day-week-picker-period-active")
    private WebElement daypicker;

    @FindBy(xpath = "//*[text()=\"Day\"]")
    private WebElement daypButton;
    public void dayWeekPickerSectionNavigatingCorrectly()  throws Exception{
        String weekIcon = "Sun - Sat";
        String activeWeekText = getActiveWeekText();
        if(activeWeekText.contains(weekIcon)){
            SimpleUtils.pass("Week pick show correctly");
        }else {
            SimpleUtils.fail("it's not week mode", true);
        }
        click(daypButton);
        if(isElementLoaded(daypicker,3)){
            SimpleUtils.pass("Day pick show correctly");
        }else {
            SimpleUtils.fail("change to day pick failed", true);
        }

    }

    @FindBy (css = "div.day-week-picker-period")
    private List<WebElement> dayPickerAllDaysInDayView;
    @Override
    public int getTheIndexOfCurrentDayInDayView() throws Exception {
        int index = 7;
        if (areListElementVisible(dayPickerAllDaysInDayView, 10)) {
            for (int i = 0; i < dayPickerAllDaysInDayView.size(); i++) {
                if (dayPickerAllDaysInDayView.get(i).getAttribute("class").contains("day-week-picker-period-active")) {
                    index = i;
                    SimpleUtils.pass("Schedule Day view: Get the current day index: " + index);
                }
            }
        }
        if (index == 7) {
            SimpleUtils.fail("Schedule Day view: Failed to get the index of CurrentDay", false);
        }
        return index;
    }

    @FindBy(css = "div.lg-modal")
    private WebElement popUpWindow;
    @FindBy(className = "lg-modal__title-icon")
    private WebElement popUpWindowTitle;
    @FindBy(css = "[label=\"Cancel\"]")
    private WebElement cancelButton;
    @Override
    public void clickCancelButtonOnPopupWindow() throws Exception {
        if (isElementLoaded(cancelButton, 5)) {
            click(cancelButton);
            if (!isElementLoaded(popUpWindow, 5)) {
                SimpleUtils.pass("Pop up window get closed after clicking cancel button!");
            }else {
                SimpleUtils.fail("Pop up window still shows after clicking cancel button!", false);
            }
        }else {
            SimpleUtils.fail("Cancel Button not loaded Successfully on pop up window!", false);
        }
    }


    @FindBy (css = "div.console-navigation-item-label.Schedule")
    private WebElement consoleSchedulePageTabElement;
    @Override
    public void goToConsoleScheduleAndScheduleSubMenu() throws Exception {
        if (isElementLoaded(consoleSchedulePageTabElement, 5)) {
            clickTheElement(consoleSchedulePageTabElement);
            clickTheElement(ScheduleSubMenu);
            ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
            if (scheduleCommonPage.verifyActivatedSubTab("Schedule"))
                SimpleUtils.pass("Schedule Page: 'Schedule' tab loaded Successfully!");
            else
                SimpleUtils.fail("Schedule Page: 'Schedule' tab not loaded", false);
        }
    }


    // Added by Nora: for Team Member View
    @FindBy (css = ".day-week-picker-period-week")
    private List<WebElement> currentWeeks;
    @FindBy(className = "sch-day-view-shift-worker-detail")
    private List<WebElement> tmIcons;
    @FindBy(className = "sch-worker-popover")
    private WebElement popOverLayout;
    @FindBy(css = "span.sch-worker-action-label")
    private List<WebElement> shiftRequests;
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
    @FindBy(css = "[label=\"Next\"]")
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
    @FindBy(css = ".loading-icon")
    private WebElement loadingIcon;

    @Override
    public void navigateToNextWeek() throws Exception {
        int currentWeekIndex = -1;
        if (areListElementVisible(currentWeeks, 20)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                String className = currentWeeks.get(i).getAttribute("class");
                if (className.contains("day-week-picker-period-active")) {
                    currentWeekIndex = i;
                }
            }
            if (currentWeekIndex == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
                clickTheElement(calendarNavigationNextWeekArrow);
                if (areListElementVisible(currentWeeks, 5)) {
                    clickTheElement(currentWeeks.get(0));
//                    waitUntilElementIsInVisible(loadingIcon);
//                    waitForSeconds(10);
                    SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(0).getText() + "' Successfully!");
                }
            }else {
                clickTheElement(currentWeeks.get(currentWeekIndex + 1));
//                waitUntilElementIsInVisible(loadingIcon);
//                waitForSeconds(10);
                SimpleUtils.pass("Navigate to next week: '" + currentWeeks.get(currentWeekIndex + 1).getText() + "' Successfully!");
            }
        }else {
            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
        }
    }

    @Override
    public void navigateToPreviousWeek() throws Exception {
        int currentWeekIndex = -1;
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                String className = currentWeeks.get(i).getAttribute("class");
                if (className.contains("day-week-picker-period-active")) {
                    currentWeekIndex = i;
                }
            }
            if (currentWeekIndex == 0 && isElementLoaded(calendarNavigationPreviousWeekArrow, 5)) {
                clickTheElement(calendarNavigationPreviousWeekArrow);
                if (areListElementVisible(currentWeeks, 5)) {
                    clickTheElement(currentWeeks.get(currentWeeks.size()-1));
                    SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeeks.size()-1).getText() + "' Successfully!");
                }
            }else {
                clickTheElement(currentWeeks.get(currentWeekIndex - 1));
                SimpleUtils.pass("Navigate to previous week: '" + currentWeeks.get(currentWeekIndex - 1).getText() + "' Successfully!");
            }
        }else {
            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
        }
    }

    @Override
    public void goToSpecificWeekByDate(String date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
        Date switchDate = dateFormat.parse(date);
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                clickTheElement(currentWeeks.get(i));
                List<String> years = getYearsFromCalendarMonthYearText();
                String activeWeek = getActiveWeekText();
                String[] items = activeWeek.split(" ");
                String weekStartText = years.get(0) + " " + items[4] + " " + items[3];
                String weekEndText = (years.size() == 2 ? years.get(1) : years.get(0)) + " " + items[7] + " " + items[6];
                Date weekStartDate = dateFormat.parse(weekStartText);
                Date weekEndDate = dateFormat.parse(weekEndText);
                boolean isBetween = SimpleUtils.isDateInTimeDuration(switchDate, weekStartDate, weekEndDate);
                if (isBetween) {
                    SimpleUtils.report("Schedule Page: Navigate to week: " + activeWeek + ", it contains the day: " + date);
                    break;
                } else {
                    if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
                        click(calendarNavigationNextWeekArrow);
                        goToSpecificWeekByDate(date);
                    }
                }
            }
        }
    }
    @Override
    public void goToSpecificWeekByDateNew(String date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
        Date switchDate = dateFormat.parse(date);
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                clickTheElement(currentWeeks.get(i));
                List<String> years = getYearsFromCalendarMonthYearText();
                String activeWeek = getActiveWeekText();
                String[] items = activeWeek.split(" ");
                String weekStartText = years.get(0) + " " + items[4] + " " + items[3];
                String weekEndText = (years.size() == 2 ? years.get(1) : years.get(0)) + " " + items[7] + " " + items[6];
                Date weekStartDate = dateFormat.parse(weekStartText);
                Date weekEndDate = dateFormat.parse(weekEndText);
                boolean isBetween = SimpleUtils.isDateInTimeDuration(switchDate, weekStartDate, weekEndDate);
                if (isBetween) {
                    SimpleUtils.report("Schedule Page: Navigate to week: " + activeWeek + ", it contains the day: " + date);
                    break;
                } else {
                    if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
                        click(calendarNavigationNextWeekArrow);
                        goToSpecificWeekByDateNew(date);
                    }
                }
            }
        }
    }

    @FindBy (className = "day-week-picker-date")
    private WebElement calMonthYear;
    public List<String> getYearsFromCalendarMonthYearText() throws Exception {
        List<String> years = new ArrayList<>();
        if (isElementLoaded(calMonthYear, 5)) {
            if (calMonthYear.getText().contains("-")) {
                String[] monthAndYear = calMonthYear.getText().split("-");
                if (monthAndYear.length == 2) {
                    if (monthAndYear[0].trim().length() > 4)
                        years.add(monthAndYear[0].trim().substring(monthAndYear[0].trim().length() - 4));
                    if (monthAndYear[1].trim().length() > 4)
                        years.add(monthAndYear[1].trim().substring(monthAndYear[1].trim().length() - 4));
                }
            }else {
                years.add(calMonthYear.getText().trim().substring(calMonthYear.getText().trim().length() - 4));
            }
        }else {
            SimpleUtils.fail("Calendar month and year not loaded successfully!", false);
        }
        return years;
    }

    @FindBy(css = "[ng-class=\"{'schedule-view-small-padding': controlPanel.isGenerateSchedleView}\"]")
    private WebElement tmSchedulePanel;
    @Override
    public void verifyTMSchedulePanelDisplay() throws Exception {
        try{
            if(isElementLoaded(tmSchedulePanel, 5)){
                SimpleUtils.pass("TM schedule panel is loaded successfully! ");
            } else
                SimpleUtils.fail("TM schedule panel not loaded successfully! ", false);
        }catch(Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Override
    public String convertDateStringFormat(String dateString) throws Exception{
        String result = dateString;
        // dateString format: JAN 2 - JAN 9, will convert 2 to 02, 9 to 09, return JAN 02 - JAN 09
        String[] items = dateString.split(" ");
        if (items.length==5 && SimpleUtils.isNumeric(items[1]) && SimpleUtils.isNumeric(items[4])){
            if (Integer.parseInt(items[1])<10 ){
                items[1] = Integer.toString(Integer.parseInt(items[1]));
                result = items[0] + " 0" + items[1] + " " + items[2] + " " + items[3];
            } else {
                result = items[0] + " " + items[1] + " " + items[2] + " " + items[3];
            }
            if (Integer.parseInt(items[4])<10){
                items[4] = Integer.toString(Integer.parseInt(items[4]));
                result = result + " 0" + items[4];
            } else {
                result = result + " " + items[4];
            }
        } else {
            SimpleUtils.fail("week day info format is not expected! Split String: " + dateString + " failed!", false);
        }
        return result;
    }


    public Map<String, String> getActiveDayInfo() throws Exception{

        Map<String, String> dayInfo = new HashMap<>();
        WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
        String[] activeDay = activeWeek.getText().replace("\n", " ").split(" ");

        dayInfo.put("weekDay", activeDay[0].substring(0, 3));
        dayInfo.put("month", activeDay[1]);
        dayInfo.put("day", activeDay[2]);
        dayInfo.put("year", getYearsFromCalendarMonthYearText().get(0));

        return dayInfo;
    }


    @FindBy (className = "header-navigation-label")
    private WebElement headerLabel;

    @Override
    public String getHeaderOnSchedule() throws Exception {
        String header = "";
        if (isElementLoaded(headerLabel,5))
            header = headerLabel.getText();
        return header;
    }

    @Override
    public void verifyHeaderOnSchedule() throws Exception {
        String header = getHeaderOnSchedule();
        if (header.equals("Schedule"))
            SimpleUtils.pass("Schedule Page: Header is \"Schedule\" as expected");
        else
            SimpleUtils.fail("Dashboard Page: Header isn't \"Schedule\"",true);
    }


    @FindBy(xpath = "//div[contains(@class,'sub-navigation-view')]//span[contains(text(),'Schedule')]")
    private WebElement goToScheduleTab;

    @FindBy(css = "lg-button[label=\"Analyze\"]")
    private WebElement analyze;

    @FindBy(css = "lg-button[label=\"Edit\"]")
    private WebElement edit;
    @Override
    public void goToSchedule() throws Exception {

        checkElementVisibility(goToScheduleTab);
        activeConsoleName = analyticsConsoleName.getText();
        click(goToScheduleTab);
        SimpleUtils.pass("Schedule Page Loading..!");
        CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
        if (createSchedulePage.isWeekGenerated()) {
            if (isElementLoaded(analyze)) {
                SimpleUtils.pass("Analyze is Displayed on Schdule page");
            } else {
                SimpleUtils.fail("Analyze not Displayed on Schedule page", true);
            }
            if (isElementLoaded(edit)) {
                SimpleUtils.pass("Edit is Displayed on Schedule page");
            } else {
                SimpleUtils.fail("Edit not Displayed on Schedule page", true);
            }
        }
    }

    @Override
    public void clickOnFirstWeekInWeekPicker() throws Exception {
        WebElement scheduleCalendarActiveWeek = MyThreadLocal.getDriver().findElement(By.className("day-week-picker-period-active"));
        if (isElementLoaded(scheduleCalendarActiveWeek)){
            clickTheElement(scheduleCalendarActiveWeek);
        }
    }

    @Override
    public Map<String, String> getSelectedWeekInfo() throws Exception{
        Map<String, String> dayInfo = new HashMap<>();
        WebElement activeWeek = MyThreadLocal.getDriver().findElement(By.cssSelector(".day-week-picker-period-active"));
        String[] activeDay = activeWeek.getText().replace("\n", " ").split(" ");

        dayInfo.put("weekDay", activeDay[0].substring(0, 3));
        dayInfo.put("month", activeDay[3]);
        dayInfo.put("day", activeDay[4]);
        dayInfo.put("year", getYearsFromCalendarMonthYearText().get(0));
        return dayInfo;
    }

    @Override
    public boolean isSpecifyDayEqualWithFirstDayOfActivateWeek(String day) throws Exception {
        boolean flag = true;
        String date = getSelectedWeekInfo().get("day");
        if (Integer.parseInt(day) == Integer.parseInt(date)) {
            SimpleUtils.pass("Template effective day is the first day of selected week.");
        } else {
            flag = false;
            SimpleUtils.report("Template effective day is NOT the first day of selected week.");
        }
        return flag;
    }


    @FindBy(xpath = "//table[@class='generate-schedule-staffing']//tr[@class]/td[2]")
    private List<WebElement> staffNameList;

    public void VerifyStaffListInSchedule(String name) throws Exception {
        boolean flag = false;
        for (int i = 0; i < staffNameList.size(); i++) {
            if (staffNameList.get(i).getText().contains(name)) {
                SimpleUtils.pass("Staff name is showing");
                flag = true;
                break;
            }
        }
        if (!flag) {
            SimpleUtils.fail("Staff name is not showing", true);
        }
    }

    @FindBy(css = ".day-week-picker-period-active")
    private WebElement activeWeek;

    @Override
    public String getActiveWeekStartDayFromSchedule() throws Exception {
        String dayStartOfTheWeek = "";
        if (isElementLoaded(activeWeek, 10)) {
            dayStartOfTheWeek = activeWeek.getText().split("\n")[1].trim();
            dayStartOfTheWeek = dayStartOfTheWeek.replaceAll(" ", "").split("-")[0].toLowerCase();
        } else {
            SimpleUtils.fail("Active week is not loaded!", false);
        }

        return dayStartOfTheWeek;
    }

    @FindBy(css = "div.lg-button-group")
    private WebElement dayAndWeekViewButton;
    public boolean checkIfDayAndWeekViewButtonEnabled () throws Exception {
        boolean isEnabled = false;
        if (isElementLoaded(dayAndWeekViewButton, 5) && isElementLoaded(scheduleDayViewButton)) {
            if (scheduleDayViewButton.getAttribute("class").contains("disabled")
                    || dayAndWeekViewButton.getAttribute("class").contains("disabled")){
                isEnabled = true;
                SimpleUtils.report("The  day and week view button is disabled! ");
            } else
                SimpleUtils.report("The  day and week view button is enabled! ");
        }else
            SimpleUtils.fail("The day and week view button fail to load! ", false);
        return isEnabled;
    }
}
