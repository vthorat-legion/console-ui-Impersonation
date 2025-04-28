package com.legion.pages.core.schedule;

import com.legion.pages.*;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.tests.core.GroupByDayPartsTest;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.sl.In;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.handler.DeleteSession;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.awt.image.SinglePixelPackedSampleModel;
import java.util.*;
import java.util.stream.Collectors;

import static com.legion.utils.MyThreadLocal.getDriver;
import static com.legion.utils.MyThreadLocal.setWeekDaysNDates;

public class ConsoleScheduleShiftTablePage extends BasePage implements ScheduleShiftTablePage {
    public ConsoleScheduleShiftTablePage() {
        PageFactory.initElements(getDriver(), this);
    }

    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");

    @FindBy(css = ".sch-day-view-shift")
    private List<WebElement> dayViewAvailableShifts;

    @Override
    public void reduceOvertimeHoursOfActiveWeekShifts() throws Exception {
        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        for (WebElement activeWeekDay : ScheduleCalendarDayLabels) {
            click(activeWeekDay);
            List<WebElement> availableDayShifts = getAvailableShiftsInDayView();
            if (availableDayShifts.size() != 0) {
                scheduleMainPage.clickOnEditButton();
                for (WebElement shiftWithOT : getAvailableShiftsInDayView()) {
                    WebElement shiftRightSlider = shiftWithOT.findElement(By.cssSelector("div.sch-day-view-shift-pinch.right"));
                    String OTString = "hrs ot";
                    int xOffSet = -50;
                    while (shiftWithOT.getText().toLowerCase().contains(OTString)) {
                        moveDayViewCards(shiftRightSlider, xOffSet);
                    }
                }
                scheduleMainPage.clickSaveBtn();
                break;
            }
        }
    }


    public List<WebElement> getAvailableShiftsInDayView() {
        if (areListElementVisible(dayViewAvailableShifts, 60)) {
            return dayViewAvailableShifts;
        }
        return null;
    }

    @FindBy(css = ".drag-target-place")
    List<WebElement> shiftPlaces;
    @FindBy(css = ".week-schedule-shift-title")
    List<WebElement> dayPartTitlesOnSchedulePage;

    @Override
    public void verifyNewAddedShiftFallsInDayPart(String nameOfTheShift, String dayPart) throws Exception {
        int index = 0;
        int indexOfDayPart = 0;
        int indexOfNextDayPart = 0;
        String nextDayPart = getNextDayPart(dayPart);

        if (areListElementVisible(shiftPlaces,15) && areListElementVisible(dayPartTitlesOnSchedulePage, 15)){
            verifySpecificDayPartExists(dayPart);
            index = getIndexInAllShiftPlacesOfTheOnlyAddedOne(nameOfTheShift);
            for (int i = 0; i < shiftPlaces.size(); i++){
                if (shiftPlaces.get(i).getText().toLowerCase().contains(dayPart.toLowerCase())){
                    indexOfDayPart = i;
                    continue;
                }
                if (nextDayPart != null && shiftPlaces.get(i).getText().toLowerCase().contains(nextDayPart.toLowerCase())){
                    indexOfNextDayPart = i;
                    break;
                }
            }
            System.out.println(indexOfDayPart);
            System.out.println(index);
            System.out.println(indexOfNextDayPart);
            if (indexOfNextDayPart == 0 && index > indexOfDayPart){
                SimpleUtils.pass("successful!");
            } else if (indexOfNextDayPart != 0 && index > indexOfDayPart && index < indexOfNextDayPart){
                SimpleUtils.pass("successful!");
            } else {
                SimpleUtils.fail("fail!", false);
            }
        } else {

        }
    }

    public String getNextDayPart(String dayPart) throws Exception{
        String nextDayPart = null;
        for (int i = 0 ; i < dayPartTitlesOnSchedulePage.size(); i++){
            if (dayPartTitlesOnSchedulePage.get(i).getText().equalsIgnoreCase(dayPart)){
                if (i < dayPartTitlesOnSchedulePage.size()-1){
                    nextDayPart = dayPartTitlesOnSchedulePage.get(i+1).getText();
                }
            }
        }
        return nextDayPart;
    }

    public void verifySpecificDayPartExists(String dayPart) throws Exception{
        boolean flag = false;
        for (WebElement dayPartTemp: dayPartTitlesOnSchedulePage){
            System.out.println(dayPartTemp.getText());
            if (dayPartTemp.getText().equalsIgnoreCase(dayPart)){
                flag = true;
                break;
            }
        }
        if (flag){
            SimpleUtils.pass(dayPart + " exists!");
        } else {
            SimpleUtils.fail(dayPart + " doesn't exists!", false);
        }
    }

    public int getIndexInAllShiftPlacesOfTheOnlyAddedOne(String name) throws Exception{
        int index = 0;
        for (int i = 0; i < shiftPlaces.size(); i++){
            System.out.println(shiftPlaces.get(i).getText());
            if (shiftPlaces.get(i).getText().toLowerCase().contains(name.toLowerCase())){
                index = i;
                break;
            }
        }
        return index;
    }


    @FindBy(css = ".shift-container.week-schedule-shift-wrapper")
    private List<WebElement> shiftsOnScheduleView;
    public ArrayList<WebElement> getAllAvailableShiftsInWeekView() {
        ArrayList<WebElement> avalableShifts = new ArrayList<WebElement>();
        if (shiftsOnScheduleView.size() != 0) {
            for (WebElement shiftOnScheduleView : shiftsOnScheduleView) {
                if (shiftOnScheduleView.getText().trim().length() > 0 && shiftOnScheduleView.isDisplayed()) {
                    avalableShifts.add(shiftOnScheduleView);
                }
            }
        }
        return avalableShifts;
    }

    public List<WebElement> getUnAssignedShifts() {
        String unAssignedShiftsLabel = "unassigned";
        List<WebElement> unAssignedShiftsObj = new ArrayList<WebElement>();
        waitForSeconds(5);
        if (areListElementVisible(shiftsOnScheduleView, 10) && shiftsOnScheduleView.size() != 0) {
            for (WebElement shift : shiftsOnScheduleView) {
                if (shift.getText().toLowerCase().contains(unAssignedShiftsLabel) && shift.isDisplayed())
                    unAssignedShiftsObj.add(shift);
            }
        }
        return unAssignedShiftsObj;
    }


    @Override
    public HashMap<String, String> getFourUpComingShifts(boolean isStartTomorrow, String currentTime) throws Exception {
        HashMap<String, String> upComingShifts = new HashMap<>();
        String activeDay = null;
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (isStartTomorrow) {
            activeDay = scheduleCommonPage.getActiveAndNextDay();
            scheduleCommonPage.clickOnNextDaySchedule(activeDay);
            upComingShifts = getAvailableShiftsForDayView(upComingShifts);
        }else {
            upComingShifts = getShiftsForCurrentDayIfStartingSoon(upComingShifts, currentTime);
        }
        while (upComingShifts.size() < 4) {
            activeDay = scheduleCommonPage.getActiveAndNextDay();
            scheduleCommonPage.clickOnNextDaySchedule(activeDay);
            upComingShifts = getAvailableShiftsForDayView(upComingShifts);
        }
        if (upComingShifts.size() >= 4) {
            SimpleUtils.pass("Get four shifts successfully!");
        }else {
            SimpleUtils.fail("Failed to get at least four shifts!", false);
        }
        return upComingShifts;
    }


    public HashMap<String, String> getAvailableShiftsForDayView(HashMap<String, String> upComingShifts) throws Exception {
        String name = null;
        String role = null;
        if (areListElementVisible(dayViewAvailableShifts, 15)) {
            for (WebElement dayViewAvailableShift : dayViewAvailableShifts) {
                name = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().toLowerCase();
                if (name.contains("(")) {
                    name = name.substring(0, name.indexOf("(") - 1);
                }
                if (!name.contains("open") && !name.contains("unassigned")) {
                    role = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-title-role")).getText().toLowerCase();
                    upComingShifts.put(name, role);
                }
            }
        } else {
            SimpleUtils.fail("Day View Available shifts failed to load!", true);
        }
        return upComingShifts;
    }

    @FindBy(css = "div.popover div:nth-child(3)>div.ng-binding")
    private WebElement timeDuration;
    public HashMap<String, String> getShiftsForCurrentDayIfStartingSoon(HashMap<String, String> upComingShifts, String currentTime) throws Exception {
        String name = null;
        String role = null;
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (areListElementVisible(dayViewAvailableShifts, 15)) {
            for (WebElement dayViewAvailableShift : dayViewAvailableShifts) {
                WebElement hoverInfo = dayViewAvailableShift.findElement(By.className("day-view-shift-right-top-icons"));
                if (hoverInfo != null) {
                    clickTheElement(hoverInfo);
                    if (isElementLoaded(timeDuration, 5)) {
                        String startTime = timeDuration.getText().split("-")[0];
                        clickTheElement(hoverInfo);
                        int shiftStartMinutes = SimpleUtils.getMinutesFromTime(startTime);
                        int currentMinutes = SimpleUtils.getMinutesFromTime(currentTime);
                        if (shiftStartMinutes > currentMinutes) {
                            name = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-name")).getText().toLowerCase();
                            if (name.contains("(")) {
                                name = name.substring(0, name.indexOf("(") - 1);
                            }
                            if (!name.contains("open") && !name.contains("unassigned")) {
                                role = dayViewAvailableShift.findElement(By.className("sch-day-view-shift-worker-title-role")).getText().toLowerCase();
                                upComingShifts.put(name, role);
                            }
                        }
                    }else {
                        SimpleUtils.fail("Failed to get the time duration!", true);
                    }
                }else {
                    SimpleUtils.fail("Failed to get the hover info element!", true);
                }
            }
        }else {
            SimpleUtils.fail("Day View Available shifts failed to load!", true);
        }
        return upComingShifts;
    }


    @FindBy(xpath= "//day-week-picker/div/div/div[3]")
    private WebElement calendarNavigationPreviousWeek;
    public void scheduleUpdateAccordingToSelectWeek() throws Exception {
        if (isElementLoaded(calendarNavigationPreviousWeek,5) ) {
            String preWeekText = calendarNavigationPreviousWeek.getText().replace("\n","").replace("-","");
            String preWeekText2 = preWeekText.trim().substring(preWeekText.length()-2);
            click(calendarNavigationPreviousWeek);
            String scheCalDay = getScheduleDayRange().trim();
            if (areListElementVisible(schCalendarDateLabel,10) && scheCalDay.trim().contains(preWeekText2.trim())) {
                SimpleUtils.pass("data is getting updating on Schedule page according to corresponding week");
            }else {
                SimpleUtils.fail("schedule canlendar is not updating according to corresponding week",true);
            }
        }else {
            SimpleUtils.fail("no next week calendar",true);
        }
    }

    @FindBy(css = "div.sch-calendar-date-label>span")
    private List<WebElement> schCalendarDateLabel;
    @FindBy(className = "sch-calendar-day-label")
    private List<WebElement> schWeekDayLabels;

    public String getScheduleDayRange() throws Exception {
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        String dayRangeText = "";
        if (schCalendarDateLabel.size() != 0)
            for (WebElement scheCalDay : schCalendarDateLabel) {
                if (scheCalDay.isDisplayed()) {
                    if (dayRangeText == "")
                        dayRangeText = scheCalDay.getText();
                    else
                        dayRangeText = dayRangeText + " | " + scheCalDay.getText();
                } else if (!scheCalDay.isDisplayed()) {
                    while (smartCardPage.isSmartCardScrolledToRightActive() == true) {
                        if (dayRangeText == "")
                            dayRangeText = scheCalDay.getText();
                        else
                            dayRangeText = dayRangeText + " | " + scheCalDay.getText();
                    }
                }
            }

        return dayRangeText;
    }

    @Override
    public LinkedHashMap<String, Integer> getWeekDayAndDate() throws Exception {
        LinkedHashMap<String, Integer> weekDayNDates = new LinkedHashMap<>();
        if (areListElementVisible(schCalendarDateLabel, 10) && areListElementVisible(schWeekDayLabels, 10)) {
            for (int i = 0; i < schCalendarDateLabel.size(); i++) {
                weekDayNDates.put(schWeekDayLabels.get(i).getText().trim(), Integer.parseInt(schCalendarDateLabel.get(i).getText().trim()));
            }
            setWeekDaysNDates(weekDayNDates);
        }
        return weekDayNDates;
    }

    @Override
    public List<String> getSelectedWorkDays(HashSet<Integer> set) throws Exception {
        LinkedHashMap<String, Integer> weekDaysAndDates = getWeekDayAndDate();
        List<String> days = new ArrayList<>();
        List<String> fullWeekDays = new ArrayList<>();
        Iterator<String> iterator = weekDaysAndDates.keySet().iterator();
        while (iterator.hasNext()) {
            days.add(iterator.next());
        }
        if (areListElementVisible(weekShifts, 3)) {
            for (int i : set) {
                int index = Integer.parseInt(weekShifts.get(i).getAttribute("data-day-index").trim());
                fullWeekDays.add(SimpleUtils.getFullWeekDayName(days.get(index)));
            }
        }
        return fullWeekDays;
    }

    @FindBy(css = "div.sch-calendar-day-dimension")
    private List<WebElement> weekViewDaysAndDates;

    @FindBy(css = "div.sch-week-view-day-summary")
    private List<WebElement> weekDaySummeryHoursAndTeamMembers;
    public void getHoursAndTeamMembersForEachDaysOfWeek() {
        String weekDaysAndDatesText = "";
        String weekDaysHoursAndTMsCount = "";
        try {
            if (weekViewDaysAndDates.size() != 0) {
                for (WebElement weekViewDayAndDate : weekViewDaysAndDates) {
                    if (weekDaysAndDatesText != "")
                        weekDaysAndDatesText = weekDaysAndDatesText + " | " + weekViewDayAndDate.getText();
                    else
                        weekDaysAndDatesText = weekViewDayAndDate.getText();
                }
                SimpleUtils.report("Active Week Days And Dates: " + weekDaysAndDatesText);
            }
            if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
                for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
                    if (weekDaysHoursAndTMsCount != "")
                        weekDaysHoursAndTMsCount = weekDaysHoursAndTMsCount + " | " + weekDayHoursAndTMs.getText();
                    else
                        weekDaysHoursAndTMsCount = weekDayHoursAndTMs.getText();
                }
                SimpleUtils.report("Active Week Hours And TeamMembers: " + weekDaysHoursAndTMsCount);
            }
        } catch (Exception e) {
            SimpleUtils.fail("Unable to get Hours & Team Members for active Week!", true);
        }
    }


    public boolean verifyActiveWeekDailyScheduleHoursInWeekView() {
        Float weekDaysScheduleHours = (float) 0;
        Float activeWeekScheduleHoursOnCard = (float) 0;
        try {
            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
            activeWeekScheduleHoursOnCard = smartCardPage.getScheduleLabelHoursAndWages().get(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.scheduledHours.getValue());
            if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
                for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
                    float dayScheduleHours = Float.parseFloat(weekDayHoursAndTMs.getText().split("HRs")[0]);
                    weekDaysScheduleHours = (float) (weekDaysScheduleHours + Math.round(dayScheduleHours * 10.0) / 10.0);
                }
            }
            float totalShiftSizeForWeek = calcTotalScheduledHourForDayInWeekView();
//            System.out.println("sum" + totalShiftSizeForWeek);
            if (totalShiftSizeForWeek == activeWeekScheduleHoursOnCard) {
                SimpleUtils.pass("Sum of all the shifts in a week equal to Week Schedule Hours!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')");
                return true;
            } else {
                SimpleUtils.fail("Sum of all the shifts in an week is not equal to Week scheduled Hour!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')", false);
            }
//            if(weekDaysScheduleHours.equals(activeWeekScheduleHoursOnCard))
//            {
//                SimpleUtils.pass("Sum of Daily Schedule Hours equal to Week Schedule Hours! ('"+weekDaysScheduleHours+ "/"+activeWeekScheduleHoursOnCard+"')");
//                return true;
//            }
        } catch (Exception e) {
            SimpleUtils.fail("Unable to Verify Daily Schedule Hours with Week Schedule Hours!", true);
        }
        return false;
    }


    @FindBy(xpath = "//*[@class='shift-hover-seperator']/following-sibling::div[1]/div[1]")
    private WebElement shiftSize;

    @FindBy(css = "img[ng-if*='hasViolation']")
    private List<WebElement> infoIcon;


    public float calcTotalScheduledHourForDayInWeekView() throws Exception {
        float sumOfAllShiftsLength = 0;
        for (int i = 0; i < infoIcon.size(); i++) {
            if (isElementEnabled(infoIcon.get(i))) {
                click(infoIcon.get(i));
                String[] TMShiftSize = shiftSize.getText().split(" ");
                float shiftSizeInHour = Float.valueOf(TMShiftSize[0]);
                sumOfAllShiftsLength = sumOfAllShiftsLength + shiftSizeInHour;

            } else {
                SimpleUtils.fail("Shift not loaded successfully in week view", false);
            }
        }
        return (sumOfAllShiftsLength);

    }


    @Override
    public void verifyScheduledHourNTMCountIsCorrect() throws Exception {
        getHoursAndTeamMembersForEachDaysOfWeek();
        verifyActiveWeekDailyScheduleHoursInWeekView();
        verifyActiveWeekTeamMembersCountAvailableShiftCount();
    }

    public boolean verifyActiveWeekTeamMembersCountAvailableShiftCount() {
        int weekDaysTMsCount = 0;
        int weekDaysShiftsCount = 0;
        try {
            if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
                for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
                    String TeamMembersCount = weekDayHoursAndTMs.getText().split("HRs")[1].replace("TMs", "").trim();
                    weekDaysTMsCount = weekDaysTMsCount + Integer.parseInt(TeamMembersCount);
                }
            }


            if (shiftsOnScheduleView.size() != 0) {
                for (WebElement shiftOnScheduleView : shiftsOnScheduleView) {
                    if (shiftOnScheduleView.getText().trim().length() > 0 && shiftOnScheduleView.isDisplayed()) {
                        weekDaysShiftsCount = weekDaysShiftsCount + 1;
                    }
                }
            }

            if (weekDaysTMsCount == weekDaysShiftsCount) {
                SimpleUtils.pass("Sum of Daily Team Members Count equal to Sum of Daily Shifts Count! ('" + weekDaysTMsCount + "/" + weekDaysShiftsCount + "')");
                return true;
            } else {
                SimpleUtils.fail("Sum of Daily Team Members Count not equal to Sum of Daily Shifts Count! ('" + weekDaysTMsCount + "/" + weekDaysShiftsCount + "')", true);
            }
        } catch (Exception e) {
            SimpleUtils.fail("Unable to Verify Daily Team Members Count with Daily Shifts Count!", true);
        }
        return false;
    }


    @FindBy(css = "div.sch-day-view-grid-header.fill")
    private List<WebElement> dayViewShiftsTimeDuration;
    public ArrayList<String> getScheduleDayViewGridTimeDuration() {
        ArrayList<String> gridTimeDurations = new ArrayList<String>();
        if (dayViewShiftsTimeDuration.size() != 0) {
            for (WebElement timeDuration : dayViewShiftsTimeDuration) {
                gridTimeDurations.add(timeDuration.getText().trim());
            }
        }

        return gridTimeDurations;
    }



    @FindBy(css = "div.sch-day-view-grid-header.tm-count.guidance")
    private List<WebElement> dayViewbudgetedTMCount;
    public ArrayList<String> getScheduleDayViewBudgetedTeamMembersCount() {
        ArrayList<String> BudgetedTMsCount = new ArrayList<String>();
        if (dayViewbudgetedTMCount.size() != 0) {
            for (WebElement BudgetedTMs : dayViewbudgetedTMCount) {
                BudgetedTMsCount.add(BudgetedTMs.getText().trim());
            }
        }

        return BudgetedTMsCount;
    }


    @FindBy(xpath = "//div[contains(@class,'sch-day-view-grid-header tm-count') and not(contains(@class,'guidance'))]")
    private List<WebElement> dayViewScheduleTMsCount;
    public ArrayList<String> getScheduleDayViewScheduleTeamMembersCount() {
        ArrayList<String> ScheduledTMsCount = new ArrayList<String>();
        if (dayViewScheduleTMsCount.size() != 0) {
            for (WebElement scheduleTMs : dayViewScheduleTMsCount) {
                ScheduledTMsCount.add(scheduleTMs.getText());
            }
        }

        return ScheduledTMsCount;
    }


    @FindBy(className = "week-schedule-shift-wrapper")
    private List<WebElement> shiftsWeekView;
    @FindBy(css = "[id=\"schedule-grid-react-wrapper\"] [class=\"rows\"] .week-schedule-worker-name")
    private List<WebElement> namesWeekView;
    @FindBy(css = ".sch-day-view-shift-worker-name")
    private List<WebElement> namesDayView;
    @FindBy(css = ".right-shift-box")
    private List<WebElement> rightShiftBoxes;
    @FindBy(css = ".shift-selected-multi")
    private List<WebElement> selectedShifts;
    @FindBy(css = "._pendo-button-tertiaryButton")
    private WebElement maybeLaterBtn;

    @Override
    public void verifyShiftsChangeToOpenAfterTerminating(List<Integer> indexes, String name, String currentTime) throws Exception {
        String open = "Open";
        String unAssigned = "Unassigned";
        String shiftTime = null;
        if (indexes.size() > 0 && areListElementVisible(shiftsWeekView, 5)) {
            for (int index : indexes) {
                WebElement workerName = shiftsWeekView.get(index).findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (workerName != null) {
                    if (workerName.getText().contains(name)) {
                        shiftTime = shiftsWeekView.get(index).findElement(By.className("week-schedule-shift-time")).getText();
                        boolean isConvertToOpen = compareShiftTimeWithCurrentTime(shiftTime, currentTime);
                        SimpleUtils.report("IsConvertToOpen: " + isConvertToOpen + " index is: " + index);
                        if (!isConvertToOpen) {
                            SimpleUtils.pass("Shift isn't change to open or unassigned since the current is earlier than the shift end time!");
                        }else {
                            SimpleUtils.fail("Shift doesn't change to open or unassigned, worker name is: " + workerName.getText(), true);
                        }
                    }else if (workerName.getText().equalsIgnoreCase(open) || workerName.getText().equalsIgnoreCase(unAssigned)) {
                        SimpleUtils.report("Index is: " + index);
                        SimpleUtils.pass("Shift is changed to open or unassigned!");
                    }else {
                        SimpleUtils.fail("Shift doesn't change to open or unassigned, worker name is: " + workerName.getText(), true);
                    }
                }else {
                    SimpleUtils.fail("Failed to find the worker name element!", true);
                }
            }
        }else {
            SimpleUtils.fail("Shifts on week view failed to load!", false);
        }
    }

    public boolean compareShiftTimeWithCurrentTime(String shiftTime, String currentTime) {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        boolean isConvertToOpen = false;
        int shiftStartMinutes = 0;
        int currentMinutes = 0;
        String[] startAndEndTime = shiftTime.split("-");
        if (startAndEndTime.length == 2) {
            String startTime = startAndEndTime[0].trim();
            shiftStartMinutes = SimpleUtils.getMinutesFromTime(startTime);
            currentMinutes = SimpleUtils.getMinutesFromTime(currentTime);
            SimpleUtils.report(startTime);
            SimpleUtils.report("Convert start time to Minute: " + shiftStartMinutes);
            SimpleUtils.report(currentTime);
            SimpleUtils.report("Convert current time to Minute: " + currentMinutes);
        }
        if (currentMinutes < shiftStartMinutes) {
            isConvertToOpen = true;
        }
        return isConvertToOpen;
    }

    @FindBy (css = "div.sch-day-view-shift-worker-detail")
    private List<WebElement> scheduleTableWeekViewWorkerDetail;

    @FindBy(css = ".sch-calendar-day-summary")
    private List<WebElement> daySummaries;

    @Override
    public float newCalcTotalScheduledHourForDayInWeekView() throws Exception {
        float sumOfAllShiftsLength = 0;
        if (areListElementVisible(daySummaries,10)){
            for (int i=0; i<daySummaries.size();i++){
                String[] TMShiftSize = daySummaries.get(i).findElement(By.cssSelector("span:nth-child(1)")).getText().split(" ");
                float shiftSizeInHour = Float.valueOf(TMShiftSize[0]);
                sumOfAllShiftsLength = sumOfAllShiftsLength + shiftSizeInHour;
            }
        } else {
            SimpleUtils.fail("weekDaySummeryHoursAndTeamMembers are not loaded!", false);
        }
        return (sumOfAllShiftsLength);

    }

    public boolean newVerifyActiveWeekDailyScheduleHoursInWeekView() throws Exception {
        Float weekDaysScheduleHours = 0.0f;
        Float activeWeekScheduleHoursOnCard = 0.0f;
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        activeWeekScheduleHoursOnCard = smartCardPage.getScheduleLabelHoursAndWages().get(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.scheduledHours.getValue())
                + smartCardPage.getScheduleLabelHoursAndWages().get(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.otherHours.getValue());
        if (weekDaySummeryHoursAndTeamMembers.size() != 0) {
            for (WebElement weekDayHoursAndTMs : weekDaySummeryHoursAndTeamMembers) {
                float dayScheduleHours = Float.parseFloat(weekDayHoursAndTMs.getText().split("HRs")[0]);
                weekDaysScheduleHours = (float) (weekDaysScheduleHours + Math.round(dayScheduleHours * 10.0) / 10.0);
            }
        }
        float totalShiftSizeForWeek = newCalcTotalScheduledHourForDayInWeekView();
        if (activeWeekScheduleHoursOnCard - totalShiftSizeForWeek <= 0.06) {
            SimpleUtils.pass("Sum of all the shifts in a week equal to Week Schedule Hours!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')");
            return true;
        } else {
            SimpleUtils.fail("Sum of all the shifts in an week is not equal to Week scheduled Hour!('" + totalShiftSizeForWeek + "/" + activeWeekScheduleHoursOnCard + "')", false);
        }
        return false;
    }

    @FindBy(css = "div.day-view-shift-right-top-icons")
    private List<WebElement> scheduleInfoIconInDayView;

    @FindBy(xpath = "//div/shift-hover/div/div[5]/div[1]")
    private WebElement  workHoursInDayViewFromPopUp;
    public float getActiveShiftHoursInDayView() {
        Float totalDayWorkTime = 0.0f;
        if (areListElementVisible(scheduleTableWeekViewWorkerDetail,5) ) {
            for (int i = 0; i <scheduleTableWeekViewWorkerDetail.size() ; i++) {
                clickTheElement(scheduleInfoIconInDayView.get(i));
                String[] timeDurationForTMContext = workHoursInDayViewFromPopUp.getText().split(" ");
                float shiftSizeInHour = Float.valueOf(timeDurationForTMContext[0]);
                totalDayWorkTime = totalDayWorkTime+shiftSizeInHour;
            }
        }else
            totalDayWorkTime = 0.0f;
        return totalDayWorkTime;
    }


    @FindBy(className = "week-schedule-shift-title")
    private List<WebElement> availableJobTitleListInWeekView;

    @FindBy(className = "sch-group-label")
    private List<WebElement> availableJobTitleListInDayView;

    public ArrayList<String> getAvailableJobTitleListInWeekView(){
        ArrayList<String> availableJobTitleList = new ArrayList<>();
        for (WebElement jobTitle:availableJobTitleListInWeekView
        ) {
            availableJobTitleList.add(jobTitle.getText().toLowerCase().trim());
        }

        return availableJobTitleList;
    }

    public ArrayList<String> getAvailableJobTitleListInDayView(){
        ArrayList<String> availableJobTitleList = new ArrayList<>();
        for (WebElement jobTitle:availableJobTitleListInDayView
        ) {
            availableJobTitleList.add(jobTitle.getText().toLowerCase().trim());
        }

        return availableJobTitleList;
    }

    @FindBy(css = ".sch-day-view-shift .sch-shift-worker-img-cursor")
    private List<WebElement> profileIconsInDayView;

    @FindBy(css = ".shift-container.week-schedule-shift-wrapper")
    private List<WebElement> shifts;

    @FindBy(css = ".week-schedule-shift .shift-container .rows .worker-image-optimized img")
    private List<WebElement> profileIcons;
    public WebElement clickOnProfileIconOfShiftInDayView(String openOrNot) throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
        WebElement selectedShift = null;
        if(shiftOperatePage.isProfileIconsEnable()&& areListElementVisible(dayViewAvailableShifts, 10)) {
            if (openOrNot.toLowerCase().contains("open")){
                int randomIndex = (new Random()).nextInt(profileIconsInDayView.size());
                while (!dayViewAvailableShifts.get(randomIndex).findElement(By.cssSelector(".sch-day-view-shift-worker-name")).getText().toLowerCase().contains("open")){
                    randomIndex = (new Random()).nextInt(dayViewAvailableShifts.size());
                }
                clickTheElement(profileIconsInDayView.get(randomIndex));
                selectedShift = dayViewAvailableShifts.get(randomIndex);
            } else {
                int randomIndex = (new Random()).nextInt(profileIconsInDayView.size());
                while (dayViewAvailableShifts.get(randomIndex).findElement(By.cssSelector(".sch-day-view-shift-worker-name")).getText().toLowerCase().contains("open")){
                    randomIndex = (new Random()).nextInt(profileIconsInDayView.size());
                }
                clickTheElement(profileIconsInDayView.get(randomIndex));
                selectedShift = dayViewAvailableShifts.get(randomIndex);
            }
        } else {
            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
        }
        return selectedShift;
    }


    @Override
    public WebElement clickOnProfileOfUnassignedShift() throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
        WebElement selectedShift = null;
        if(shiftOperatePage.isProfileIconsEnable()&& areListElementVisible(shifts, 10)) {
            int randomIndex = (new Random()).nextInt(profileIcons.size());
            while (!profileIcons.get(randomIndex).getAttribute("src").contains("unassignedShiftImage")){
                randomIndex = (new Random()).nextInt(profileIcons.size());
            }
            clickTheElement(profileIcons.get(randomIndex));
            selectedShift = shifts.get(randomIndex);
        } else if (areListElementVisible(scheduleTableWeekViewWorkerDetail, 10) && areListElementVisible(dayViewAvailableShifts, 10)) {
            int randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
            while (!dayViewAvailableShifts.get(randomIndex).findElement(By.className("sch-day-view-shift-worker-name")).getText().contains("Open")){
                randomIndex = (new Random()).nextInt(scheduleTableWeekViewWorkerDetail.size());
            }
            clickTheElement(scheduleTableWeekViewWorkerDetail.get(randomIndex));
            selectedShift = dayViewAvailableShifts.get(randomIndex);
        } else {
            SimpleUtils.fail("Can't Click on Profile Icon due to unavailability ",false);
        }
        return selectedShift;
    }

    @FindBy(className = "week-schedule-shift")
    private List<WebElement> weekShifts;
    @FindBy(css = ".week-schedule-shift.week-schedule-shift-another-location")
    private List<WebElement> weekShiftsFromAnotherLocation;
    @FindBy(css = ".sch-shift-hover div:nth-child(3)>div.ng-binding")
    private WebElement shiftDuration;
    @FindBy(css = ".shift-hover-subheading.ng-binding:not([ng-if])")
    private WebElement shiftJobTitleAsWorkRole;
    @FindBy(css = ".sch-shift-hover div:nth-child(5)>div.ng-binding")
    private WebElement shiftTotalHrs;
    @FindBy(css = ".week-schedule-shift-place-pto")
    private List<WebElement> ptoPlaces;
    @FindBy (css = "[class=\"hover-sub-container\"]")
    private List<WebElement> infoContainers;
    @FindBy (css = "[ng-if-start=\"shift.name\"]")
    private WebElement shiftName;
    @FindBy (xpath = "//div[contains(@ng-if-start,'shift.notes')]/following-sibling::div/div")
    private WebElement shiftNotes;

    @Override
    public List<String> getTheShiftInfoByIndex(int index) throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
//        waitForSeconds(10);
        List<String> shiftInfo = new ArrayList<>();
        if (areListElementVisible(weekShifts, 20) && index < weekShifts.size()) {
            clickTheElement(weekShifts.get(index).findElement(By.className("week-schedule-shift-open-popover")));
            String firstName = MyThreadLocal.getDriver().findElement(By.xpath("//div[@class=\"hover-sub-container\"][1]/div[1]")).getText();
            waitForSeconds(3);
//            String firstName = MyThreadLocal.getDriver().findElement(By.xpath("//div[contains(@class,'popover-content')]/shift-hover/div/div[1]/div[1]")).getText();
            if (firstName.equals("")) {
                SimpleUtils.fail("Failed for getting user first name from shift info", false);
            }else
                SimpleUtils.pass("Get user first name successfully! The first name is: "+firstName);
            if (!firstName.equalsIgnoreCase("Open") && !firstName.equalsIgnoreCase("Unassigned")) {
                String dayIndex = weekShifts.get(index).getAttribute("data-day-index");
                SimpleUtils.pass("Get shift day index successfully! The day index is: "+ dayIndex);
                String lastName = shiftOperatePage.getTMDetailNameFromProfilePage(weekShifts.get(index)).split(" ")[1].trim();
                Thread.sleep(3000);
                SimpleUtils.pass("Get user last name successfully! The last name is: "+ lastName);
                String jobTitle = weekShifts.get(index).findElement(By.cssSelector(".rows .week-schedule-role-name")).getText();
                SimpleUtils.pass("Get user job title successfully! The job tile is: "+ jobTitle);
                String shiftTimeWeekView = weekShifts.get(index).findElement(By.className("week-schedule-shift-time")).getText();
                SimpleUtils.pass("Get shift time in shift card successfully! The shift time is: "+ shiftTimeWeekView);
                WebElement infoIcon = weekShifts.get(index).findElement(By.className("week-schedule-shift-open-popover"));
                clickTheElement(infoIcon);
                String shiftNameOnIIconPopUp = "";
                String shiftNotesOnIIconPopUp = "";
                if (isElementLoaded(shiftName, 5)){
                    shiftNameOnIIconPopUp = shiftName.getText();
                    SimpleUtils.pass("Get shift name on i icon popup successfully! The shift name is:"+shiftNameOnIIconPopUp);
                }
                String workRole = shiftJobTitleAsWorkRole.getText().split(" as ")[1].trim();
                SimpleUtils.pass("Get shift work role successfully! The work role is: " + workRole);
                    if (isElementLoaded(shiftNotes, 5)) {
                        shiftNotesOnIIconPopUp = shiftNotes.getText();
                    }
                    String shiftTime = "";
                    String totalHrs = "";
                    String shiftHrs = "";
                    if (areListElementVisible(infoContainers, 5) && infoContainers.size() == 3) {
                        shiftTime = infoContainers.get(infoContainers.size() - 2).getText().split("\n")[0].trim().replace(" ", "");
                        totalHrs = infoContainers.get(infoContainers.size() - 1).getText().split("\n")[0].split("\\|")[1].trim();
                        shiftHrs = infoContainers.get(infoContainers.size() - 1).getText().split("\n")[0].split("\\|")[0].trim();
                    }
                    shiftInfo.add(firstName); //Index 0
                    shiftInfo.add(dayIndex); //Index 1
                    shiftInfo.add(shiftTime); //Index 2
                    shiftInfo.add(jobTitle); //Index 3
                    shiftInfo.add(workRole); //Index 4
                    shiftInfo.add(lastName); //Index 5
                    shiftInfo.add(shiftTimeWeekView); //Index 6
                    shiftInfo.add(totalHrs); //Index 7
                    shiftInfo.add(shiftHrs); //Index 8
                    shiftInfo.add(shiftNameOnIIconPopUp); //Index 9
                    shiftInfo.add(shiftNotesOnIIconPopUp); //Index 10
                    //To close the info popup
//                    clickTheElement(weekShifts.get(index));
            } else {
                //SimpleUtils.report("This is an Open Shift");
                //return shiftInfo;
                //For open shift
                String dayIndex = weekShifts.get(index).getAttribute("data-day-index");
                String lastName = "";
                if (firstName.equalsIgnoreCase("Unassigned")){
                    lastName = "unassigned";
                } else
                    lastName = "open";
                String jobTitle = "";
                WebElement infoIcon = weekShifts.get(index).findElement(By.className("week-schedule-shift-open-popover"));
                clickTheElement(infoIcon);
                String workRole = shiftJobTitleAsWorkRole.getText().trim();
                String shiftTime = "";
                if (areListElementVisible(infoContainers, 10)) {
                    shiftTime = infoContainers.get(infoContainers.size() - 2).getText().split("\n")[0].trim().replace(" ", "");
                }
                String shiftNameOnIIconPopUp = "";
                String shiftNotesOnIIconPopUp = "";
                if (isElementLoaded(shiftName, 5)){
                    shiftNameOnIIconPopUp = shiftName.getText();
                }
                if (isElementLoaded(shiftNotes, 5)) {
                    shiftNotesOnIIconPopUp = shiftNotes.getText();
                }
                String shiftHours = infoContainers.get(infoContainers.size() - 1).getText();
                shiftInfo.add(firstName); //Index 0
                shiftInfo.add(dayIndex); //Index 1
                shiftInfo.add(shiftTime); //Index 2
                shiftInfo.add(jobTitle); //Index 3
                shiftInfo.add(workRole); //Index 4
                shiftInfo.add(lastName); //Index 5
                shiftInfo.add(shiftHours);//Index 6
                shiftInfo.add(shiftNameOnIIconPopUp); //Index 7
                shiftInfo.add(shiftNotesOnIIconPopUp); //Index 8
                //To close the info popup
                clickTheElement(weekShifts.get(index));
            }
        } else {
            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
        }
        if (shiftInfo.size() < 7) {
            SimpleUtils.fail("Failed to get the shift info!", false);
        }
        return shiftInfo;
    }

    @Override
    public List<String> getTheGreyedShiftInfoByIndex(int index) throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
        waitForSeconds(3);
        List<String> shiftInfo = new ArrayList<>();
        if (areListElementVisible(weekShiftsFromAnotherLocation, 20) && index < weekShiftsFromAnotherLocation.size()) {
            String firstName = weekShiftsFromAnotherLocation.get(index).findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText();
            String dayIndex = weekShiftsFromAnotherLocation.get(index).getAttribute("data-day-index");
            String lastName = shiftOperatePage.getTMDetailNameFromProfilePage(weekShiftsFromAnotherLocation.get(index)).split(" ")[1].trim();
            waitForSeconds(2);
            String jobTitle = weekShiftsFromAnotherLocation.get(index).findElement(By.cssSelector(".rows .week-schedule-role-name")).getText();
            String shiftTimeWeekView = weekShiftsFromAnotherLocation.get(index).findElement(By.className("week-schedule-shift-time")).getText();
            WebElement infoIcon = weekShiftsFromAnotherLocation.get(index).findElement(By.className("week-schedule-shift-open-popover"));
            clickTheElement(infoIcon);
            String workRole = shiftJobTitleAsWorkRole.getText().split("as")[1].trim();
            if (isElementLoaded(shiftDuration, 10) && isElementLoaded(shiftTotalHrs, 10)) {
                String shiftTime = shiftDuration.getText();
                String totalHrs = shiftTotalHrs.getText().split("\\|")[1];
                shiftInfo.add(firstName);
                shiftInfo.add(dayIndex);
                shiftInfo.add(shiftTime);
                shiftInfo.add(jobTitle);
                shiftInfo.add(workRole);
                shiftInfo.add(lastName);
                shiftInfo.add(shiftTimeWeekView);
                shiftInfo.add(totalHrs);
            }
            //To close the info popup
            clickTheElement(weekShiftsFromAnotherLocation.get(index));
        } else {
            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
        }
        if (shiftInfo.size() <= 7) {
            SimpleUtils.fail("Failed to get the shift info!", false);
        }
        return shiftInfo;
    }

    @FindBy(css = ".week-schedule-right-strip-cell div div:nth-child(1)")
    private List<WebElement> totalHrsStripCells;
    @Override
    public String getTotalHrsFromRightStripCellByIndex(int index) throws Exception {
        if (areListElementVisible(totalHrsStripCells, 10)){
            return totalHrsStripCells.get(0).getText();
        } else {
            SimpleUtils.fail("There is no total hours info on the right strip", false);
        }
        return null;
    }

    @Override
    public List<String> getTheShiftInfoInDayViewByIndex(int index) throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        List<String> shiftInfo = new ArrayList<>();
        if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
            WebElement infoIcon = dayViewAvailableShifts.get(index).findElement(By.className("day-view-shift-right-top-icons"));
            clickTheElement(infoIcon);
            String firstName = MyThreadLocal.getDriver().findElement(By.xpath("//div[@class=\"hover-sub-container\"][1]/div[1]")).getText();
//            String firstName = dayViewAvailableShifts.get(index).
//                    findElement(By.className("sch-day-view-shift-worker-name")).getText().split(" ")[0];
            if (!firstName.equalsIgnoreCase("Open") && !firstName.equalsIgnoreCase("Unassigned") ) {
                String lastName = shiftOperatePage.getTMDetailNameFromProfilePage(dayViewAvailableShifts.get(index)).split(" ")[1].trim();
                String shiftTimeWeekView = dayViewAvailableShifts.get(index).findElement(By.className("sch-day-view-shift-time")).getText();
//                WebElement infoIcon = dayViewAvailableShifts.get(index).findElement(By.className("day-view-shift-hover-info-icon"));
                clickTheElement(infoIcon);
                String workRole = shiftJobTitleAsWorkRole.getText().split("as")[1].trim();
                String jobTitle = shiftJobTitleAsWorkRole.getText().split("as")[0].trim();
                String totalHrs = infoContainers.get(infoContainers.size() - 1).getText().split("\\|")[1];
                String shiftHrs = infoContainers.get(infoContainers.size() - 1).getText().split("\\|")[0];
                if (isElementLoaded(shiftDuration, 10)) {
                    String shiftTime = shiftDuration.getText();
                    shiftInfo.add(firstName);
                    shiftInfo.add(String.valueOf(scheduleCommonPage.getTheIndexOfCurrentDayInDayView()));
                    shiftInfo.add(shiftTime);
                    shiftInfo.add(jobTitle);
                    shiftInfo.add(workRole);
                    shiftInfo.add(lastName);
                    shiftInfo.add(shiftTimeWeekView);
                    shiftInfo.add(totalHrs);
                    shiftInfo.add(shiftHrs);
                    shiftInfo.add(dayViewAvailableShifts.get(index).getAttribute("id"));
                }
                //To close the info popup
                clickTheElement(dayViewAvailableShifts.get(index));
            } else {
                //SimpleUtils.report("This is an Open Shift");
                //return shiftInfo;
                //For open shift
                //String dayIndex = weekShifts.get(index).getAttribute("data-day-index");
                String lastName = "";
                if (firstName.equalsIgnoreCase("Unassigned")){
                    lastName = "unassigned";
                } else
                    lastName = "open";
                String jobTitle = "";
                String shiftTimeWeekView = dayViewAvailableShifts.get(index).findElement(By.className("sch-day-view-shift-time")).getText();
//                WebElement infoIcon = dayViewAvailableShifts.get(index).findElement(By.className("day-view-shift-hover-info-icon"));
                String workRole = shiftJobTitleAsWorkRole.getText().trim();
                if (isElementLoaded(shiftDuration, 10)) {
                    String shiftTime = shiftDuration.getText();
                    shiftInfo.add(firstName);
                    //shiftInfo.add(dayIndex);
                    shiftInfo.add(String.valueOf(scheduleCommonPage.getTheIndexOfCurrentDayInDayView()));
                    shiftInfo.add(shiftTime);
                    shiftInfo.add(jobTitle);
                    shiftInfo.add(workRole);
                    shiftInfo.add(lastName);
                    shiftInfo.add(shiftTimeWeekView);
                }
                //To close the info popup
                clickTheElement(dayViewAvailableShifts.get(index));
            }
        } else {
            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
        }
        if (shiftInfo.size() < 7) {
            SimpleUtils.fail("Failed to get the shift info!", false);
        }
        return shiftInfo;
    }

    public void verifySearchResult (String firstNameOfTM, String lastNameOfTM, String workRole, String jobTitle, List<WebElement> searchResults) throws Exception {
        ShiftOperatePage shiftOperatePage = new ConsoleShiftOperatePage();
        if (searchResults !=null && searchResults.size()>0) {
            if (firstNameOfTM != null) {
                for (int i=0; i< searchResults.size(); i++) {
                    String[] tmDetailName = shiftOperatePage.getTMDetailNameFromProfilePage(searchResults.get(i)).split(" ");
                    if (firstNameOfTM.equalsIgnoreCase(tmDetailName[0])
                            || firstNameOfTM.equalsIgnoreCase(tmDetailName[1])
                            || tmDetailName[0].toLowerCase().contains(firstNameOfTM.toLowerCase())
                            || tmDetailName[1].toLowerCase().contains(firstNameOfTM.toLowerCase())) {
                        SimpleUtils.pass("The search result display correctly when search by TM first name");
                    } else {
                        SimpleUtils.fail("The search result incorrect when search by TM first name, the expected name is: " + firstNameOfTM+ ". The actual name is: " + tmDetailName[0] +" " +tmDetailName[1],false);
                        break;
                    }
                }
            } else if (lastNameOfTM != null) {
                for (int i=0; i< searchResults.size(); i++) {
                    String[] tmDetailName = shiftOperatePage.getTMDetailNameFromProfilePage(searchResults.get(i)).split(" ");
                    if (tmDetailName[0].toLowerCase().contains(lastNameOfTM.toLowerCase())
                            || tmDetailName[1].toLowerCase().contains(lastNameOfTM.toLowerCase())) {
                        SimpleUtils.pass("The search result display correctly when search by TM last name");
                    } else {
                        SimpleUtils.fail("The search result incorrect when search by TM last name",false);
                        break;
                    }
                }
            }
            else if (workRole != null) {
                String[] workRoleWords = workRole.split(" ");
                for (int i=0; i <searchResults.size(); i++) {
                    scrollToElement(searchResults.get(i));
                    waitForSeconds(3);
                    Map<String, String> shiftInfo= getShiftInfoFromInfoPopUp(searchResults.get(i));
                    String shiftWorkRole = shiftInfo.get("WorkRole");
                    String shiftJobTitle = shiftInfo.get("JobTitle");
                    if (shiftWorkRole.contains(workRole)|| shiftJobTitle.contains(workRole)) {
                        SimpleUtils.pass("The search result display correctly when search by Work Role");
                    } else if(workRoleWords.length>1) {
                        for (int j=0; j< workRoleWords.length; j++){
                            if (shiftWorkRole.contains(workRoleWords[j])){
                                SimpleUtils.pass("The search result display correctly when search by Work Role");
                                break;
                            }
                        }
                    } else {
                        SimpleUtils.fail("The search result incorrect when search by Work Role, expected: " + workRole
                                + ", actual is: " + getShiftInfoFromInfoPopUp(searchResults.get(i)).get("WorkRole"),false);
                        break;
                    }
                }
            } else if (jobTitle != null) {
                String[] jobTitleWords = jobTitle.split(" ");
                for (int i=0; i <searchResults.size(); i++) {
                    scrollToElement(searchResults.get(i));
                    Map<String, String> shiftInfo= getShiftInfoFromInfoPopUp(searchResults.get(i));
                    String shiftWorkRole = shiftInfo.get("WorkRole");
                    String shiftJobTitle = shiftInfo.get("JobTitle");
                    if (jobTitle.equals(shiftJobTitle)|| jobTitle.equals(shiftWorkRole)) {
                        SimpleUtils.pass("The search result display correctly when search by Job Title");
                    } else if(jobTitleWords.length>1) {
                        for (int j=0; j< jobTitleWords.length; j++){
                            if (shiftWorkRole.contains(jobTitleWords[j])){
                                SimpleUtils.pass("The search result display correctly when search by Job Title");
                                break;
                            }
                        }
                    } else {
                        SimpleUtils.fail("The search result incorrect when search by Job Title",false);
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Verify texts all are null!",false);
            }
        } else {
            SimpleUtils.fail("There is no search result!",false);
        }
    }


    @FindBy(className = "popover-content")
    private WebElement popOverContent;
    public Map<String, String> getShiftInfoFromInfoPopUp(WebElement shift) throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        Map<String, String> shiftInfo = new HashMap<String, String>();
        if (shift != null) {
            //click blank area to close the i icon opened before.
            scrollToElement(shift);
            Actions actions = new Actions(getDriver());
            actions.moveByOffset(0, 0).click().build().perform();
            if(scheduleCommonPage.isScheduleDayViewActive()){
                clickTheElement(shift.findElement(By.className("day-view-shift-right-top-icons")));
                waitForSeconds(2);
            } else
                clickTheElement(shift.findElement(By.className("week-schedule-shift-open-popover")));

        } else {
            SimpleUtils.fail("Selected shift is null!",true);
        }
        if (isElementEnabled(popOverContent, 5)) {
            String shiftName = MyThreadLocal.getDriver().findElement(By.xpath("//div[@class=\"hover-sub-container\"][1]/div[1]")).getText();
            shiftInfo.put("shiftName", shiftName);
            String[] jobTitleAndWorkRole = popOverContent.findElement(By.cssSelector(".shift-hover-subheading.ng-binding")).getText().split("as");
            if (jobTitleAndWorkRole.length==1){
                //add job title
                shiftInfo.put("JobTitle", "");
                //add work role
                shiftInfo.put("WorkRole", jobTitleAndWorkRole[0].trim());
            } else {
                //add job title
                shiftInfo.put("JobTitle", jobTitleAndWorkRole[0].trim());
                //add work role
                shiftInfo.put("WorkRole", jobTitleAndWorkRole[1].trim());
            }
            String[] shiftHrs = popOverContent.findElements(By.cssSelector(".hover-sub-container")).get(2).findElement(By.cssSelector("div")).getText().split("\\|");
            if (shiftHrs.length==1) {
                shiftInfo.put("DailyHrs", shiftHrs[0].split(" ")[0]);
                shiftInfo.put("WeeklyHrs", "");
            } else {
                shiftInfo.put("DailyHrs", shiftHrs[0].split(" ")[0]);
                shiftInfo.put("WeeklyHrs", shiftHrs[1].trim().split(" ")[0]);
            }
        }
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        //To close the popup
        scheduleMainPage.clickOnFilterBtn();
        scheduleMainPage.clickOnFilterBtn();
        return shiftInfo;
    }


    @FindBy(css = ".sch-shift-hover.visible")
    private WebElement infoTextFromInfoIcon;
    @Override
    public String getIIconTextInfo(WebElement shift) throws Exception{
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (isElementLoaded(shift, 5)){
            waitForSeconds(3);
            scrollToElement(shift);
            if(scheduleCommonPage.isScheduleDayViewActive()){
                click(shift.findElement(By.cssSelector(".day-view-shift-right-top-icons img")));
                waitForSeconds(2);
            } else
                click(shift.findElement(By.cssSelector("img.week-schedule-shift-open-popover")));
            if (isElementLoaded(infoTextFromInfoIcon, 5)){
                return infoTextFromInfoIcon.getText();
            } else
                SimpleUtils.fail("Info icon popup fail to load", false);
        } else
            SimpleUtils.fail("Shift fail to load", false);
        return null;
    }


    @Override
    public int getShiftIndexById(String id) throws Exception {
        waitForSeconds(5);
        WebElement shift = null;
        int index = 0;
        if (id != null && !id.equals("")) {
            String css = "[data-shift-id=\""+ id+"\"]";
            shift = MyThreadLocal.getDriver().findElement(By.cssSelector(css));
            if (isElementLoaded(shift, 5) && areListElementVisible(weekShifts,10)) {
                for (WebElement element: weekShifts){
                    if (element.findElement(By.cssSelector(".week-schedule-shift div.week-schedule-shift-wrapper")).getAttribute("id").equalsIgnoreCase(id)){
                        return index;
                    }
                    index++;
                }
            } else if (isElementLoaded(shift, 5) && areListElementVisible(dayViewAvailableShifts,10)) {
                for (WebElement element: dayViewAvailableShifts){
                    if (element.getText().equalsIgnoreCase(shift.getText())){
                        return index;
                    }
                    index++;
                }
            }else{
                SimpleUtils.fail("Cannot find shift by the id !",false);
            }
        } else {
            SimpleUtils.fail("The shift id is null or empty!",false);
        }
        return index;
    }


    @Override
    public List<WebElement> getAllShiftsOfOneTM(String name) throws Exception{
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        List<WebElement> allShifts = new ArrayList<>();
        if (areListElementVisible(shiftsWeekView, 20)) {
            for (WebElement shiftWeekView : shiftsWeekView) {
                WebElement workerName = null;
                scrollToElement(shiftWeekView);
                if(scheduleCommonPage.isScheduleDayViewActive()){
                    workerName = shiftWeekView.findElement(By.className("sch-day-view-shift-worker-name"));
                } else
                    workerName = shiftWeekView.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (workerName != null && workerName.getText().toLowerCase().contains(name.toLowerCase())) {
                    allShifts.add(shiftWeekView);
                }
            }
        }else
            SimpleUtils.fail("Schedule Week View: shifts load failed or there is no shift in this week", false);
        return allShifts;
    }

    @FindBy(css = "span.ot-hours-text")
    private List<WebElement> complianceMessageInInfoIconPopup;
    @Override
    public List<WebElement> getAllOOOHShifts() throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        List<WebElement> allOOOHShifts = new ArrayList<>();
        WebElement iIcon = null;
        if (areListElementVisible(shiftsWeekView, 15)) {
            for (WebElement shiftWeekView : shiftsWeekView) {
                scrollToElement(shiftWeekView);
                if(scheduleCommonPage.isScheduleDayViewActive()){
                    iIcon = shiftWeekView.findElement(By.cssSelector("div.day-view-shift-right-top-icons img"));
                    waitForSeconds(2);
                } else
                    iIcon = shiftWeekView.findElement(By.cssSelector("img.week-schedule-shift-open-popover"));
                if(iIcon.getAttribute("src").contains("danger")) {
                    click(iIcon);
                    if (isElementLoaded(popOverContent, 5)){
                        if (areListElementVisible(complianceMessageInInfoIconPopup, 5) && complianceMessageInInfoIconPopup.size()>0){
                            List<String> complianceMessages = new ArrayList<>();
                            for (WebElement message: complianceMessageInInfoIconPopup){
                                complianceMessages.add(message.getText());
                            }
                            if(complianceMessages.contains("Outside Operating hours")) {
                                allOOOHShifts.add(shiftWeekView);
                            }
                        } else
                            SimpleUtils.report("There is no compliance message in info icon popup");
                    } else
                        SimpleUtils.fail("Info icon popup fail to load", false);
                }
            }
        }else
            SimpleUtils.fail("Schedule Week View: shifts load failed or there is no shift in this week", false);
        return allOOOHShifts;
    }


    @Override
    public List<String> getComplianceMessageFromInfoIconPopup(WebElement shift) throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        List<String> complianceMessages = new ArrayList<>();
        if (isElementLoaded(shift, 5)){
            scrollToElement(shift);
            waitForSeconds(3);
            if (isElementLoaded(popOverContent, 5)) {
                //To close the i icon popup
                clickTheElement(shift);
            }
            if(scheduleCommonPage.isScheduleDayViewActive()){
                click(shift.findElement(By.cssSelector(".day-view-shift-right-top-icons img")));
                waitForSeconds(2);
            } else
                click(shift.findElement(By.cssSelector("img.week-schedule-shift-open-popover")));
            if (isElementLoaded(popOverContent, 5)){
                if (areListElementVisible(complianceMessageInInfoIconPopup, 5) && complianceMessageInInfoIconPopup.size()>0){
                    for (int i=0; i< complianceMessageInInfoIconPopup.size(); i++){
                        complianceMessages.add(complianceMessageInInfoIconPopup.get(i).getText().replace(".0", ""));
                    }
                } else
                    SimpleUtils.report("There is no compliance message in info icon popup");
            } else
                SimpleUtils.fail("Info icon popup fail to load", false);
        } else
            SimpleUtils.fail("Shift fail to load", false);
        // Actions actions = new Actions(getDriver());
        // actions.moveByOffset(0, 0).click().build().perform();
        return complianceMessages;
    }


    public WebElement getShiftById(String id) throws Exception {
        waitForSeconds(5);
        WebElement shift = null;
        if (id != null && !id.equals("")) {
            String css = "div[data-shift-id=\""+ id+"\"]";
            shift = MyThreadLocal.getDriver().findElement(By.cssSelector(css));
            if (isElementLoaded(shift, 5)) {
                SimpleUtils.pass("Get one shift by the id successfully");
            } else
                SimpleUtils.fail("Cannot find shift by the id !",false);
        } else {
            SimpleUtils.fail("The shift id is null or empty!",false);
        }
        return shift;
    }



    @Override
    public String getTheShiftInfoByIndexInDayview(int index) throws Exception {
        String shiftInfo = "";
        if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
            shiftInfo = dayViewAvailableShifts.get(index).getText();
        } else {
            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
        }
        return shiftInfo;
    }


    @Override
    public void verifyUpComingShiftsConsistentWithSchedule(Map<String, String> dashboardShifts, HashMap<String, String> scheduleShifts) throws Exception {
        if (scheduleShifts.entrySet().containsAll(dashboardShifts.entrySet())) {
            SimpleUtils.pass("Up coming shifts from dashboard is consistent with the shifts in schedule!");
        }else {
            SimpleUtils.fail("Up coming shifts from dashboard isn't consistent with the shifts in schedule!", true);
        }
    }

    @FindBy(css = "img[src*=\"added-shift\"]")
    private List<WebElement> addedShiftIcons;
    @Override
    public void verifyNewShiftsAreShownOnSchedule(String name) throws Exception {
        boolean isFound = false;
        if (areListElementVisible(addedShiftIcons, 5)) {
            for (WebElement addedShiftIcon : addedShiftIcons) {
                WebElement parent = addedShiftIcon.findElement(By.xpath("./../../../.."));
                if (parent != null) {
                    WebElement teamMemberName = parent.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                    if (teamMemberName != null && teamMemberName.getText().contains(name)) {
                        isFound = true;
                        SimpleUtils.pass("Added a New shift for: " + name + " is successful!");
                    }
                }else {
                    SimpleUtils.fail("Failed to find the parent element for adding icon!", false);
                }
            }
        }else {
            SimpleUtils.fail("Failed to find the new added shift icons!", false);
        }
        if (!isFound) {
            SimpleUtils.fail("Cannot find the new shift for team member: " + name, true);
        }
    }

    @FindBy(className = "week-schedule-shift")
    private List<WebElement> shiftsInWeekView;
    @Override
    public List<Integer> getAddedShiftIndexes(String name) throws Exception {
        // Wait for the shifts to be loaded
        waitForSeconds(5);
        List<Integer> indexes = new ArrayList<>();
        if (areListElementVisible(shiftsInWeekView, 5)) {
            for (int i = 0; i < shiftsInWeekView.size(); i++) {
                WebElement workerName = shiftsInWeekView.get(i).findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (workerName != null) {
                    if (workerName.getText().contains(name)) {
                        indexes.add(i);
                        SimpleUtils.pass("Get the index: " + i + " successfully!");
                    }
                }
            }
        }
        if (indexes.size() == 0) {
            SimpleUtils.fail("Failed to get the index of the newly added shifts!", false);
        }
        return indexes;
    }

    @Override
    public HashSet<Integer> getAddedShiftsIndexesByPlusIcon() throws Exception {
        HashSet<Integer> indexes = new HashSet<>();
        if (areListElementVisible(shiftsInWeekView, 5)) {
            for (int i = 0; i < shiftsInWeekView.size(); i++) {
                try {
                    WebElement plusIcon = shiftsInWeekView.get(i).findElement(By.cssSelector("img[src*=\"added-shift\"]"));
                    if (plusIcon != null) {
                        indexes.add(i);
                        SimpleUtils.pass("Get the index: " + i + " successfully!");
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        if (indexes.size() == 0) {
            SimpleUtils.fail("Failed to get the index of the newly added shifts!", false);
        }
        return indexes;
    }

    @Override
    public boolean areShiftsPresent() throws Exception {
        boolean arePresent = false;
        if (areListElementVisible(dayViewAvailableShifts, 5)) {
            arePresent = true;
        }
        return arePresent;
    }

    @FindBy (css = ".week-schedule-shift .week-schedule-shift-wrapper")
    private List<WebElement> wholeWeekShifts;
    @Override
    public int getShiftsCount() throws Exception {
        int count = 0;
        waitForSeconds(5);
        if (areListElementVisible(wholeWeekShifts, 10)) {
            count = wholeWeekShifts.size();
        } else if (areListElementVisible(dayViewAvailableShifts, 10)){
            count = dayViewAvailableShifts.size();
        }
        return count;
    }

    @Override
    public String getShiftTextByIndex(int indexOfShift) {
        String shiftText = "";
        waitForSeconds(5);
        if (areListElementVisible(wholeWeekShifts, 10)) {
            shiftText = wholeWeekShifts.get(indexOfShift).getText();
        } else if (areListElementVisible(dayViewAvailableShifts, 10)){
            shiftText = dayViewAvailableShifts.get(indexOfShift).getText();
        }
        return shiftText;
    }

    @Override
    public boolean isProfileIconsClickable() throws Exception {
        if(areListElementVisible(profileIcons,10)){
            int randomIndex = (new Random()).nextInt(profileIcons.size());
            try{
                click(profileIcons.get(randomIndex));
                return true;
            } catch (Exception e){
                //
            }
        } else {
            SimpleUtils.fail("Profile Icon is not present for selected Employee", false);
        }
        return false;
    }


    @FindBy(css = ".sch-day-view-shift-overtime-icon")
    private List<WebElement> oTFlag;

    @FindBy(css = ".ot-hours-text")
    private List<WebElement> oTHoursText;

    @Override
    public int getOTShiftCount() {
        int count =0;
        if (areListElementVisible(oTFlag,5) && areListElementVisible(oTHoursText,5)) {
            count = oTHoursText.size();
        }
        return count;
    }


    @FindBy(css = "div.sch-day-view-shift-delete")
    private WebElement btnDelete ;

    @FindBy(css = "div[ng-repeat=\"shift in filteredShifts\"]")
    private List<WebElement> shiftsInDayViewNew;

    @FindBy(css = ".sch-day-view-shift")
    private List<WebElement> shiftsInDayView;

    @FindBy(css = "div.sch-shift-container")
    private List<WebElement> scheduleShiftsRows;

    //update by haya
    public void validateXButtonForEachShift() throws Exception{
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        String deletedInfo = "Deleted";
        int shiftCount = 0;
        if (areListElementVisible(shiftsInDayViewNew, 5)) {
            shiftCount = shiftsInDayViewNew.size();
            for (int i = 0; i < shiftsInDayViewNew.size(); i++) {
                List<WebElement> tempShifts = getDriver().findElements(By.cssSelector("div[ng-repeat=\"shift in filteredShifts\"]"));
                click(tempShifts.get(i));
                if (isElementEnabled(btnDelete, 5)) {
                    SimpleUtils.pass(": X button is present for selected Shift");
                    clickTheElement(btnDelete);
                    // To avoid stale element issue
                    tempShifts = getDriver().findElements(By.cssSelector("div[ng-repeat=\"shift in filteredShifts\"]"));
                    String deletedShiftInfo = tempShifts.get(i).findElement(By.cssSelector("div.sch-day-view-right-gutter-text")).getText();
                    if (deletedShiftInfo.contains(deletedInfo)) {
                        SimpleUtils.pass("can delete shift by X button");
                        break;
                    } else {
                        SimpleUtils.fail("delete shift failed by X button, no deleted guter text!", true);
                    }
                    break;
                } else SimpleUtils.fail("X button is not present for ", true);
            }
        } else if (areListElementVisible(shiftsInDayView, 5)) {
            shiftCount = shiftsInDayView.size();
            for (int i = 0; i < shiftsInDayView.size(); i++) {
                List<WebElement> tempShifts = getDriver().findElements(By.cssSelector(".sch-day-view-shift"));
                moveToElementAndClick(tempShifts.get(i));
                if (isElementEnabled(btnDelete, 5)) {
                    SimpleUtils.pass(": X button is present for selected Shift");
                    clickTheElement(btnDelete);
                    // To avoid stale element issue
                    String deletedShiftInfo = getDriver().findElements(By.cssSelector("div.sch-day-view-right-gutter-text")).get(i).getText();
                    if (deletedShiftInfo.contains(deletedInfo)) {
                        SimpleUtils.pass("can delete shift by X button");
                        break;
                    } else {
                        SimpleUtils.fail("delete shift failed by X button, no deleted guter text!", true);
                    }
                    break;
                } else SimpleUtils.fail("X button is not present for ", true);
            }
        } else {
            SimpleUtils.fail("There is no shifts in day view!", false);
        }
        scheduleMainPage.saveSchedule();
        int shiftCountAftDelete =  scheduleShiftsRows.size();
        if (shiftCountAftDelete < shiftCount) {
            SimpleUtils.pass("delete shift successfully by X button");

        }else
            SimpleUtils.fail("delete shift failed by X button",false);
    }


    @FindBy(xpath = "//div/shift-hover/div/div[5]/div[1]")
    private WebElement totalHoursInWeekFromPopUp;

    @FindBy(css = "div.sch-worker-display-name")
    private List<WebElement> workerName;

    @FindBy(css = "div.week-schedule-worker-name.ng-binding")
    private List<WebElement> workerNameList;

    @FindBy(css = ".week-view-shift-info-icon")
    private List<WebElement> scheduleInfoIcon;
    @Override
    public float getShiftHoursByTMInWeekView(String teamMember) {
        Float timeDurationForTMInWeek = 0.0f;
        waitForSeconds(5);
        if (areListElementVisible(workerNameList,5) ) {
            for (int i = 0; i <workerNameList.size() ; i++) {
                if ( workerNameList.get(i).getText().trim().toLowerCase().contains(teamMember.toLowerCase())) {
                    click(scheduleInfoIcon.get(i));
                    String[] timeDurationForTMContext = totalHoursInWeekFromPopUp.getText().split(" ");
                    timeDurationForTMInWeek = Float.valueOf(timeDurationForTMContext[3].trim());
                    break;
                }
            }
        }else
            timeDurationForTMInWeek = 0.0f;
        return timeDurationForTMInWeek;
    }


    @FindBy (css = "[ng-repeat=\"message in getComplianceMessages()\"]")
    private List<WebElement> otFlagandOThoursInWeekForTM;
    @Override
    public void verifyWeeklyOverTimeAndFlag(String teamMemberName) throws Exception {
        boolean flag = false;
        if (areListElementVisible(otFlagandOThoursInWeekForTM,10)){
            for (WebElement e : otFlagandOThoursInWeekForTM){
                if (e.getText().contains("week overtime")){
                    flag = true;
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Flag is not present for team member " + propertySearchTeamMember.get("TeamMember"), false);
        }
        if (flag) {
            SimpleUtils.pass("week overtime shifts created successfully");
        } else {
            SimpleUtils.fail("weekly overtime Flag is not present for team member " + propertySearchTeamMember.get("TeamMember"), false);
        }
    }


    @FindBy(css = ".sch-worker-h-view")
    private WebElement workerInfoFromShift;
    @Override
    public Map<String, String> getHomeLocationInfo() throws Exception {
        Map<String, String> resultInfo = new HashMap<String, String>();
        if (isElementLoaded(workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-display-name")), 10)){
            resultInfo.put("worker name", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-display-name")).getText().replace("\n", ""));

        } else {
            SimpleUtils.fail("Worker name info fail to load!", false);
        }
        if (isElementLoaded(workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")), 10)){
            if (workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")).getText().split("\n").length == 2){
                resultInfo.put("job title", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")).getText().split("\n")[0]);
                resultInfo.put("PTorFT", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-role")).getText().split("\n")[1]);
            } else {
                SimpleUtils.fail("Work role and PT or FT info are not expected!", false);
            }
        } else {
            SimpleUtils.fail("Work role info fail to load!", false);
        }
        if (isElementLoaded(workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-role-name")), 10)){
            resultInfo.put("homeLocation", workerInfoFromShift.findElement(By.cssSelector(".sch-worker-h-view-role-name")).getText());
        } else {
            SimpleUtils.fail("Home location info fail to load!", false);
        }
        if (areListElementVisible(workerInfoFromShift.findElements(By.cssSelector(".one-badge")), 10)){
            resultInfo.put("badgeSum", String.valueOf(workerInfoFromShift.findElements(By.cssSelector(".one-badge")).size()));
            String badgeInfo = "";
            for (WebElement element: workerInfoFromShift.findElements(By.cssSelector(".one-badge"))){
                badgeInfo = badgeInfo + " " + element.getAttribute("data-original-title");
            }
            resultInfo.put("badgeInfo", badgeInfo);
        } else {
            resultInfo.put("badgeSum", "0");
        }
        return resultInfo;
    }



    //Added by Julie
    @FindBy(css = "div.rows")
    private List<WebElement> weekScheduleShiftsOfWeekView;

    @FindBy(css = ".sch-day-view-shift-time")
    private List<WebElement> weekScheduleShiftsTimeOfMySchedule;

    @FindBy(css = ".my-schedule-no-schedule")
    private WebElement myScheduleNoSchedule;

    @FindBy(className = "sch-navigation-container")
    private WebElement scheduleTable;

    @FindBy(css = "div.lg-picker-input")
    private WebElement currentLocationOnSchedulePage;

    @FindBy(css = ".sub-navigation-view-link")
    private List<WebElement> subMenusOnSchedulePage;

    @FindBy(css = "[ng-repeat=\"opt in opts\"] input-field")
    private List<WebElement> shiftTypes;

    @FindBy(css = "div[ng-attr-class^=\"sch-date-title\"]")
    private List<WebElement> weekScheduleShiftsDateOfMySchedule;

    @FindBy(css = "div.sch-day-view-grid-header span")
    private List<WebElement> scheduleShiftTimeOnHeader;

    @FindBy(css = ".day-view-shift-right-top-icons")
    private List<WebElement> hoverIcons;

    @FindBy(css = ".card-carousel-card-default")
    private WebElement openShiftCard;

    @FindBy(className = "card-carousel-link")
    private WebElement viewShiftsBtn;

    @FindBy(css = "h1[ng-if=\"weeklyScheduleData.hasSchedule !== 'FALSE'\"]")
    private WebElement openShiftData;

    @FindBy(css = "img[src*=\"openShift\"]")
    private List<WebElement> blueIconsOfOpenShift;

    @FindBy(css = "[ng-if=\"isGenerateOverview()\"] h1")
    private WebElement weekInfoBeforeCreateSchedule;

    @FindBy (css = ".modal-instance-header-title")
    private WebElement headerWhileCreateSchedule;

    @FindBy (css = ".generate-modal-location")
    private WebElement locationWhileCreateSchedule;

    @FindBy (css = ".text-right[ng-if=\"hasBudget\"]")
    private List<WebElement> budgetedHoursOnSTAFF;

    @FindBy (xpath = "//div[contains(text(), \"Weekly Budget\")]/following-sibling::h1[1]")
    private WebElement budgetHoursOnWeeklyBudget;

    @FindBy (css = "[x=\"25\"]")
    private List<WebElement> budgetHrsOnGraph;

    @FindBy (xpath = "//p[contains(text(),\"Target Budget: \")]/span")
    private WebElement targetBudget;

    @FindBy (css = ".generate-modal-week-container.selected text[x=\"85\"]")
    private WebElement scheduledHrsOnGraph;

    @FindBy (xpath = "//div[contains(text(), \"Action Required\")]/following-sibling::h1[1]")
    private WebElement changesOnActionRequired;

    @FindBy(css = "img[ng-if=\"unpublishedDeleted && isOneAndOnlyShiftTypeSelected('Edited')\"]")
    private WebElement tooltipIconOfUnpublishedDeleted;

    @FindBy (className = "sch-calendar-day")
    private List<WebElement> scheduleCalendarDays;

    @FindBy (className = "tma-header-text")
    private WebElement titleInSelectTeamMemberWindow;

    @FindBy (className = "worker-edit-availability-status")
    private WebElement messageInSelectTeamMemberWindow;

    @FindBy (css = "[ng-repeat=\"worker in searchResults\"] .tma-staffing-option-outer-circle")
    private WebElement optionCircle;

    @FindBy (css = "[ng-click=\"cancelAction()\"]")
    private WebElement closeButtonOnCustomize;

    List<String> weekScheduleShiftTimeListOfWeekView = new ArrayList<String>();
    List<String> weekScheduleShiftTimeListOfMySchedule = new ArrayList<String>();

    @Override
    public List<String> getWeekScheduleShiftTimeListOfWeekView(String teamMemberName) throws Exception {
        //clickOnWeekView();
        if (areListElementVisible(weekScheduleShiftsOfWeekView, 10) && weekScheduleShiftsOfWeekView.size() != 0) {
            for (int i = 0; i < weekScheduleShiftsOfWeekView.size(); i++) {
                if (weekScheduleShiftsOfWeekView.get(i).findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText().contains(teamMemberName)) {
                    weekScheduleShiftTimeListOfWeekView.add(weekScheduleShiftsOfWeekView.get(i).findElement(By.cssSelector(".week-schedule-shift-time")).getText().replace(" ", "").toLowerCase());
                }
            }
        } else if (weekScheduleShiftsOfWeekView.size() == 0) {
            SimpleUtils.report("Schedule Week View Page: No shift available");
        } else {
            SimpleUtils.fail("Schedule Week View Page: Failed to load shifts", true);
        }
        return weekScheduleShiftTimeListOfWeekView;
    }

    @FindBy (className = "sch-calendar-day-dimension")
    private List<WebElement> weekDayDimensions;
    @Override
    public HashMap<String, String> getTheHoursNTheCountOfTMsForEachWeekDays() throws Exception {
        HashMap<String, String> hoursNTeamMembersCount = new HashMap<>();
        if (areListElementVisible(weekDayDimensions, 10) && weekDayDimensions.size() == 14) {
            for (int i = 0; i < weekDayDimensions.size()-7; i++) {
                WebElement weekDay = weekDayDimensions.get(i).findElement(By.className("sch-calendar-day-label"));
                WebElement hoursNCount = weekDayDimensions.get(i).findElement(By.className("sch-calendar-day-summary"));
                List<WebElement> shiftsInSameDay = getDriver().findElements(By.cssSelector("[data-day-index=\"" + i +"\"] .week-schedule-shift-wrapper"));
                if (weekDay != null && hoursNCount != null) {
                    hoursNTeamMembersCount.put(weekDay.getText(), hoursNCount.getText().split(" ")[0] +
                            hoursNCount.getText().split(" ")[1] + " " + shiftsInSameDay.size() + "TMs");
                    SimpleUtils.report("Schedule Week View Page: Get the week day: " + weekDay.getText() + " and the count of hours" +
                            ", TMs are: " + hoursNCount.getText());
                } else {
                    SimpleUtils.fail("Schedule Week View Page: week day, hours and TMs are not loaded Successfully!", false);
                }
            }
        } else {
            SimpleUtils.fail("Schedule Week View Page: Each week day dimension not loaded Successfully!", false);
        }
        return hoursNTeamMembersCount;
    }


    @Override
    public HashMap<String, List<String>> getTheContentOfShiftsForEachWeekDay() throws Exception {
        HashMap<String, List<String>> shiftsForEachDay = new HashMap<>();
        if (areListElementVisible(weekDayDimensions, 10) && weekDayDimensions.size() == 14) {
            for (int i=0; i< weekDayDimensions.size()-7;i++) {
                WebElement weekDay = weekDayDimensions.get(i).findElement(By.className("sch-calendar-day-label"));
                List<WebElement> weekShiftWrappers = weekDayDimensions.get(i).findElements(By.className("week-schedule-shift-wrapper"));
                List<String> infos = new ArrayList<>();
                if (weekShiftWrappers != null && weekShiftWrappers.size() > 0) {
                    for (WebElement weekShiftWrapper : weekShiftWrappers) {
                        WebElement shiftTime = weekShiftWrapper.findElement(By.className("week-schedule-shift-time"));
                        WebElement workerName = weekShiftWrapper.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                        WebElement jobTitle = weekShiftWrapper.findElement(By.className("week-schedule-role-name"));
                        if (weekDay != null && shiftTime != null && workerName != null && jobTitle != null) {
                            infos.add(shiftTime.getText() + "\n" + workerName.getText() + "\n" + jobTitle.getText());
                        } else {
                            SimpleUtils.fail("Schedule Week View Page: Failed to find the week day, shift time, worker name and job title elements!", false);
                        }
                    }
                }
                shiftsForEachDay.put(weekDay.getText(), infos);
                SimpleUtils.report("Schedule Week View Page: Get the shifts for week day: " + weekDay.getText() + ", the shifts are: " + infos.toString());
            }
        } else {
            SimpleUtils.fail("Schedule Week View Page: Each week day dimension not loaded Successfully!", false);
        }
        return shiftsForEachDay;
    }


    @FindBy (css = ".sch-calendar-day-dimension")
    private List<WebElement> scheduleDays;
    @Override
    public void verifyDayHasShifts(String day) throws Exception {
        if (areListElementVisible(scheduleDays,10)){
            if (day.toLowerCase().contains("sunday")) {
                for (WebElement e : scheduleDays) {
                    if (e.getAttribute("class").contains("0")) {
                        String data = e.getAttribute("data-day");
                        if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")), 10))
                            SimpleUtils.pass("On Sunday there are shifts!");
                        else
                            SimpleUtils.fail("There are no shifts on Sunday!", false);
                        break;
                    }
                }
            } else if (day.toLowerCase().contains("monday")){
                for (WebElement e : scheduleDays){
                    if (e.getAttribute("class").contains("1")){
                        String data = e.getAttribute("data-day");
                        if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
                            SimpleUtils.pass("On Sunday there are shifts!");
                        else
                            SimpleUtils.fail("There are no shifts on Sunday!",false);
                        break;
                    }
                }
            } else if (day.toLowerCase().contains("tuesday")){
                for (WebElement e : scheduleDays){
                    if (e.getAttribute("class").contains("2")){
                        String data = e.getAttribute("data-day");
                        if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
                            SimpleUtils.pass("On Sunday there are shifts!");
                        else
                            SimpleUtils.fail("There are no shifts on Sunday!",false);
                        break;
                    }
                }
            }
        } else if (day.toLowerCase().contains("wednesday")){
            for (WebElement e : scheduleDays){
                if (e.getAttribute("class").contains("3")){
                    String data = e.getAttribute("data-day");
                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
                        SimpleUtils.pass("On Sunday there are shifts!");
                    else
                        SimpleUtils.fail("There are no shifts on Sunday!",false);
                    break;
                }
            }
        } else if (day.toLowerCase().contains("thursday")){
            for (WebElement e : scheduleDays){
                if (e.getAttribute("class").contains("4")){
                    String data = e.getAttribute("data-day");
                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
                        SimpleUtils.pass("On Sunday there are shifts!");
                    else
                        SimpleUtils.fail("There are no shifts on Sunday!",false);
                    break;
                }
            }
        } else if (day.toLowerCase().contains("friday")){
            for (WebElement e : scheduleDays){
                if (e.getAttribute("class").contains("5")){
                    String data = e.getAttribute("data-day");
                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
                        SimpleUtils.pass("On Sunday there are shifts!");
                    else
                        SimpleUtils.fail("There are no shifts on Sunday!",false);
                    break;
                }
            }
        } else if (day.toLowerCase().contains("saturday")){
            for (WebElement e : scheduleDays){
                if (e.getAttribute("class").contains("6")){
                    String data = e.getAttribute("data-day");
                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")),10))
                        SimpleUtils.pass("On Sunday there are shifts!");
                    else
                        SimpleUtils.fail("There are no shifts on Sunday!",false);
                    break;
                }
            }
        } else {
            SimpleUtils.fail("No schedule day loaded in schedule page!",false);
        }
    }


    @Override
    public List<String> getDayShifts(String index) throws Exception {
        List<String> result = new ArrayList<>();
        if (areListElementVisible(scheduleDays,10)){
            for (WebElement e : scheduleDays) {
                if (e.getAttribute("class").contains(index)) {
                    String data = e.getAttribute("data-day");
                    if (areListElementVisible(MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift")), 10)){
                        List<WebElement> shifts = MyThreadLocal.getDriver().findElements(By.cssSelector("div[data-day=\"" + data + "\"].week-schedule-shift"));
                        for (WebElement shift : shifts){
                            result.add(shift.getText());
                        }
                        SimpleUtils.pass("On Sunday there are shifts!");
                    }
                    break;
                }
            }
        } else {
            SimpleUtils.fail("No schedule day loaded in schedule page!",false);
        }
        return result;
    }


    @Override
    public void verifyNoShiftsForSpecificWeekDay(List<String> weekDaysToClose) throws Exception {
        if (areListElementVisible(weekDayDimensions, 10) && weekDayDimensions.size() == 7) {
            for (WebElement weekDayDimension : weekDayDimensions) {
                WebElement weekDay = weekDayDimension.findElement(By.className("sch-calendar-day-label"));
                // Judge if the week day is in the list, if it is, this day is closed, there should be no shifts
                if (weekDay != null && weekDaysToClose.contains(SimpleUtils.getFullWeekDayName(weekDay.getText()))) {
                    List<WebElement> weekShiftWrappers = weekDayDimension.findElements(By.className("week-schedule-shift-wrapper"));
                    if (weekShiftWrappers != null) {
                        if (weekShiftWrappers.size() == 0) {
                            SimpleUtils.pass("Verified for Week Day: " + weekDay.getText() + ", there are no shifts Loaded!");
                        } else {
                            SimpleUtils.fail("Verified for Week Day: " + weekDay.getText() + " failed, this day is closed, but there still have shifts!", false);
                        }
                    } else {
                        SimpleUtils.pass("Verified for Week Day: " + weekDay.getText() + ", there are no shifts Loaded!");
                    }
                }
            }
        } else {
            SimpleUtils.fail("Schedule Week View Page: Each week day dimension not loaded Successfully!", false);
        }
    }


    @FindBy (css = "div.day-week-picker-period")
    private List<WebElement> dayPickerAllDaysInDayView;

    @FindBy (css = "img.holiday-logo-image")
    private WebElement storeClosed;

    @FindBy (css = ".holiday-text")
    private WebElement storeClosedText;
    @Override
    public void verifyStoreIsClosedForSpecificWeekDay(List<String> weekDaysToClose) throws Exception {
        if (weekDaysToClose != null && weekDaysToClose.size() > 0) {
            for (String weekDayToClose : weekDaysToClose) {
                if (areListElementVisible(dayPickerAllDaysInDayView, 5)) {
                    for (WebElement dayPicker : dayPickerAllDaysInDayView) {
                        if (dayPicker.getText().toLowerCase().contains(weekDayToClose.substring(0, 3).toLowerCase())) {
                            clickTheElement(dayPicker);
                            if (isElementLoaded(storeClosed, 10) && isElementLoaded(storeClosedText, 10) &&
                                    storeClosedText.getText().equals("Store is closed.")) {
                                SimpleUtils.pass("Verified 'Store is closed.' for week day:" + weekDayToClose);
                            } else {
                                SimpleUtils.fail("Verified 'Store is not closed.' for week day:" + weekDayToClose, false);
                            }
                            break;
                        }
                    }
                } else {
                    SimpleUtils.fail("Schedule Day View: Day pickers not loaded Successfully!", false);
                }
            }
        }else {
            SimpleUtils.report("There are no week days that need to close!");
        }
    }


    // Added by Nora: for day view overtime
    @Override
    public void dragOneShiftToMakeItOverTime() throws Exception {
        if (areListElementVisible(scheduleShiftsRows, 10) && scheduleShiftsRows.size() > 0) {
            SimpleUtils.report("Schedule Day View: shifts loaded Successfully!");
            int index = 0;
            WebElement leftPinch = scheduleShiftsRows.get(index).findElement(By.cssSelector(".sch-day-view-shift-pinch.left"));
            WebElement rightPinch = scheduleShiftsRows.get(index).findElement(By.cssSelector(".sch-day-view-shift-pinch.right"));
            List<WebElement> gridCells = scheduleShiftsRows.get(index).findElements(By.cssSelector(".sch-day-view-grid-cell"));
            if (leftPinch != null && rightPinch != null && gridCells != null && gridCells.size() > 0) {
                WebElement firstCell = gridCells.get(0);
                WebElement lastCell = gridCells.get(gridCells.size() - 1);
                mouseHoverDragandDrop(leftPinch, firstCell);
                waitForSeconds(2);
                mouseHoverDragandDrop(rightPinch, lastCell);
                waitForSeconds(2);
                WebElement flag = scheduleShiftsRows.get(index).findElement(By.cssSelector(".sch-day-view-shift-overtime-icon"));
                if (flag != null) {
                    SimpleUtils.pass("Schedule Day View: day overtime icon shows correctly!");
                } else {
                    SimpleUtils.fail("Schedule Day View: day overtime icon failed to show!", false);
                }
            } else {
                SimpleUtils.fail("Schedule Day View: Failed to find the left pinch, right pinch and grid cells elements!", false);
            }
        } else {
            SimpleUtils.report("Schedule Day View: There is no shift for this day!");
        }
    }

    @Override
    public void clickProfileIconOfShiftByIndex(int index) throws Exception {
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (scheduleCommonPage.isScheduleDayViewActive()) {
            if(areListElementVisible(shiftsInDayView, 15) && index < shiftsInDayView.size()){
                scrollToElement(shiftsInDayView.get(index));
                waitForSeconds(1);
                clickTheElement(shiftsInDayView.get(index).findElement(By.cssSelector(".sch-day-view-shift-worker-detail")));
                SimpleUtils.pass("clicked shift icon!");
            } else {
                SimpleUtils.fail("There is no shift you want",false);
            }
        } else {
            if(areListElementVisible(weekShifts, 15) && index < weekShifts.size()){
                scrollToElement(weekShifts.get(index));
                waitForSeconds(1);
                clickTheElement(weekShifts.get(index).findElement(By.cssSelector(".worker-image-optimized img")));
                SimpleUtils.pass("clicked shift icon!");
            } else {
                SimpleUtils.fail("There is no shift you want",false);
            }
        }

    }

    @Override
    public void verifyComplianceForShiftByIndex(String violation, int index) throws Exception {
        List<WebElement> shifts = null;
        if (areListElementVisible(shiftsInWeekView, 5)) {
            shifts = shiftsInWeekView;
        } else if (areListElementVisible(shiftsInDayView, 5)) {
            shifts = shiftsInDayView;
        }
        List<String> messages = getComplianceMessageFromInfoIconPopup(shifts.get(index));
        if (messages.contains(violation)) {
            SimpleUtils.pass(violation + " shows correctly");
        } else {
            SimpleUtils.fail(violation + " doesn't show on the shift!", false);
        }
    }

    @FindBy(xpath = "//span[text()=\"View Status\"]")
    private WebElement viewStatusBtn;
    @Override
    public void clickViewStatusBtn() throws Exception {
        if(isElementLoaded(viewStatusBtn,15)){
            clickTheElement(viewStatusBtn);
            waitForSeconds(2);
            SimpleUtils.pass("clicked view status button!");
        } else {
            // Do nothing
        }
    }


    public int getRandomIndexOfShift() {
        int randomIndex = 0;
        if (areListElementVisible(weekShifts, 5) && weekShifts.size() >0 ){
            randomIndex = (new Random()).nextInt(weekShifts.size());
        } else if (areListElementVisible(shiftsInDayView, 5) && shiftsInDayView.size() >0) {
            randomIndex = (new Random()).nextInt(shiftsInDayView.size());
        } else
            SimpleUtils.fail("There is no shift display on schedule page", true);
        return randomIndex;
    }


    @Override
    public void dragOneAvatarToAnother(int startIndex, String firstName, int endIndex) throws Exception {
        boolean isDragged = false;
        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"] .week-schedule-shift-wrapper"));
        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
            for (WebElement start : startElements) {
                WebElement startName = start.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                WebElement startAvatar = start.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
                if (startName != null && startAvatar != null && startName.getText().contains(firstName)) {
                    for (WebElement end : endElements) {
                        WebElement endAvatar = end.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
                        WebElement endName = end.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                        if (endAvatar != null && endName != null && !endName.getText().contains(firstName) &&
                                !endName.getText().equalsIgnoreCase("Open")) {
                            mouseHoverDragandDrop(startAvatar, endAvatar);
                            SimpleUtils.report("Drag&Drop: Drag " + firstName + " to " + endName.getText() + " Successfully!");
                            verifyConfirmStoreOpenCloseHours();
                            isDragged = true;
                            break;
                        }
                    }
                    break;
                }
            }
            if (!isDragged) {
                SimpleUtils.fail("Failed to drag the user: " + firstName + " to another Successfully!", false);
            }
        } else {
            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
        }
    }


    @FindBy(css=".modal-dialog.modal-lgn-md")
    private WebElement moveAnywayDialog;

    @Override
    public void verifyConfirmStoreOpenCloseHours() throws Exception {
        try {
            if (ifMoveAnywayDialogDisplay()) {
                if (isElementLoaded(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")), 10)) {
                    if (moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")).getText().equals("OK")) {
                        clickTheElement(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")));
                        SimpleUtils.pass("CONFIRM button clicked!");
                    }
                }
            }
        } catch (Exception e) {
            // Do nothing
        }
    }

    @Override
    public boolean ifMoveAnywayDialogDisplay() throws Exception {
        if (isElementLoaded(moveAnywayDialog,10)){
            return true;
        }
        return false;
    }


    @Override
    public int getTheIndexOfTheDayInWeekView(String date) throws Exception {
        int index = -1;
        if (areListElementVisible(schCalendarDateLabel, 10)) {
            for (int i = 0; i < schCalendarDateLabel.size(); i++) {
                if (Integer.parseInt(schCalendarDateLabel.get(i).getText().trim()) == Integer.parseInt(date.trim())) {
                    index = i;
                    SimpleUtils.pass("Get the index of Date" + date + ", the index is: " + i);
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Schedule Week View: Week day labels are failed to load!", false);
        }
        if (index == -1) {
            SimpleUtils.fail("Failed to get the index of the day: " + date, false);
        }
        return index;
    }


    @Override
    public HashMap<String,Integer> dragOneAvatarToAnotherSpecificAvatar(int startIndexOfTheDay, String user1, int endIndexOfTheDay, String user2) throws Exception {
        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndexOfTheDay + "\"] .week-schedule-shift-wrapper"));
        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndexOfTheDay + "\"] .week-schedule-shift-wrapper"));
        HashMap<String,Integer> shiftsSwaped =  new HashMap<String, Integer>();
        WebElement startAvatar = null;
        WebElement endAvatar = null;
        int i = 0;
        int j = 0;
        waitForSeconds(5);
        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
            for (WebElement start : startElements) {
                i++;
                WebElement name1 = start.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (name1 != null && name1.getText().split(" ")[0].equalsIgnoreCase(user1)) {
                    startAvatar = start.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
                    shiftsSwaped.put(user1,i);
                }
            }
            for (WebElement end : endElements) {
                j++;
                WebElement name2 = end.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (name2 != null  && name2.getText().split(" ")[0].equalsIgnoreCase(user2)) {
                    endAvatar = end.findElement(By.cssSelector(".rows .week-view-shift-image-optimized img"));
                    shiftsSwaped.put(user2,j);

                }
            }
            if (endAvatar != null && startAvatar != null) {
                mouseHoverDragandDrop(startAvatar, endAvatar);
            }
        } else {
            SimpleUtils.fail("No shifts on the day",false);
        }
        return shiftsSwaped;
    }


    @FindBy(css = "div[ng-repeat=\"error in swapError\"]")
    private List<WebElement> errorMessagesInSwap;
    @FindBy(css = "div[ng-repeat=\"error in assignError\"]")
    private List<WebElement> errorMessagesInAssign;
    //========
    @FindBy(css = "div[ng-repeat=\"error in assignError\"]")
    private WebElement errorMessageInAssign;
    @FindBy(css = ".swap-modal-error")
    private List<WebElement> copyMoveErrorMesgs;

    @Override
    public void verifyMessageInConfirmPage(String expectedMassageInSwap, String expectedMassageInAssign) throws Exception {
        String errorMessageForSwap = null;
        String errorMessageInAssign = null;
        if (areListElementVisible(errorMessagesInSwap,15) && areListElementVisible(errorMessagesInAssign,15)){
            for (WebElement element: errorMessagesInSwap){
                errorMessageForSwap = errorMessageForSwap+element.getText();
            }
            for (WebElement element: errorMessagesInAssign){
                errorMessageInAssign = errorMessageInAssign+element.getText();
            }
            if (errorMessageForSwap.contains(expectedMassageInSwap) && errorMessageInAssign.contains(expectedMassageInAssign)){
                SimpleUtils.pass("errorMessageInSwap: "+errorMessageForSwap+"\nerrorMessageInAssign: "+errorMessageInAssign);
            }else{
                SimpleUtils.fail("warning message for overtime when drag and drop is not expected!",false);
            }

        } else {
            SimpleUtils.fail("No warning message for overtime when drag and drop",false);
        }
    }

    @Override
    public void verifyMessageOnCopyMoveConfirmPage(String expectedMsgInCopy, String expectedMsgInMove) throws Exception {
        int count = 0;
        if (areListElementVisible(copyMoveErrorMesgs,15) && copyMoveErrorMesgs.size() > 0){
            for (WebElement message : copyMoveErrorMesgs) {
                if (message.getText().replaceAll(" ", "").equalsIgnoreCase(expectedMsgInCopy.replaceAll(" ", "")) || message.getText().replaceAll(" ", "").equalsIgnoreCase(expectedMsgInMove.replaceAll(" ", ""))) {
                    count = count + 1;
                }
            }
            if (count == 2) {
                SimpleUtils.pass(expectedMsgInCopy + " shows correctly!");
            } else {
                SimpleUtils.fail("\"" + expectedMsgInCopy + "\"" + " is not show!", false);
            }
        } else {
            SimpleUtils.fail("No warning message when drag and drop",false);
        }
    }

    @Override
    public void verifyConfirmBtnIsDisabledForSpecificOption(String optionName) throws Exception {
        try {
            selectCopyOrMoveByOptionName(optionName);
            if (isElementLoaded(confirmBtnOnDragAndDropConfirmPage, 5) && confirmBtnOnDragAndDropConfirmPage.getAttribute("class").contains("disabled")) {
                SimpleUtils.pass("CONFIRM button is disabled!");
            } else {
                SimpleUtils.fail("CONFIRM button is mot loaded or is not disabled!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Override
    public void selectCopyOrMoveByOptionName(String optionName) throws Exception {
        try {
            if (areListElementVisible(swapAndAssignOptions,15)&&swapAndAssignOptions.size()==2){
                if (optionName.equalsIgnoreCase("Copy")){
                    click(swapAndAssignOptions.get(0));
                    waitForSeconds(1);
                    if (!swapAndAssignOptions.get(0).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
                        SimpleUtils.pass("Copy option selected successfully!");
                    } else {
                        SimpleUtils.fail("Copy option is not selected", false);
                    }
                }
                if (optionName.equalsIgnoreCase("Move")){
                    click(swapAndAssignOptions.get(1));
                    if (!swapAndAssignOptions.get(1).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
                        SimpleUtils.pass("Move option selected successfully!");
                    } else {
                        SimpleUtils.fail("Move option is not selected", false);
                    }
                }
            } else {
                SimpleUtils.fail("Copy and move options fail to load!",false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @FindBy(css = ".tma-staffing-option-outer-circle")
    private List<WebElement> swapAndAssignOptions;
    @Override
    public void selectSwapOrAssignOption(String action) throws Exception {
        if (areListElementVisible(swapAndAssignOptions,15)&&swapAndAssignOptions.size()==2){
            if (action.equalsIgnoreCase("swap")){
                click(swapAndAssignOptions.get(0));
                waitForSeconds(1);
                if (!swapAndAssignOptions.get(0).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
                    SimpleUtils.pass("swap option selected successfully!");
                } else {
                    SimpleUtils.fail("swap option is not selected", false);
                }
            }
            if (action.equalsIgnoreCase("assign")){
                click(swapAndAssignOptions.get(1));
                if (!swapAndAssignOptions.get(1).findElement(By.cssSelector(".tma-staffing-option-inner-circle")).getAttribute("class").contains("ng-hide")){
                    SimpleUtils.pass("assign option selected successfully!");
                } else {
                    SimpleUtils.fail("assign option is not selected", false);
                }
            }
        } else {
            SimpleUtils.fail("swap and assign options fail to load!",false);
        }
    }

    @FindBy(css = ".modal-instance-button.confirm.ng-binding")
    private WebElement confirmBtnOnDragAndDropConfirmPage;
    @Override
    public void clickConfirmBtnOnDragAndDropConfirmPage() throws Exception {
        waitForSeconds(3);
        if (isElementLoaded(confirmBtnOnDragAndDropConfirmPage,15) && !confirmBtnOnDragAndDropConfirmPage.getAttribute("class").contains("disabled")){
            click(confirmBtnOnDragAndDropConfirmPage);
            SimpleUtils.pass("confirm button is clicked successfully!");
        } else {
            SimpleUtils.fail("confirm button is disabled!",false);
        }
    }


    @FindBy(css=".swap-modal-shifts.swap-modal-shifts-swap")
    private WebElement swapSectionInfo;
    @FindBy(css=".swap-modal-shifts.swap-modal-shifts-assign")
    private WebElement assignSectionInfo;
    @Override
    public List<String> getShiftSwapDataFromConfirmPage(String action) throws Exception {
        List<String> swapData = new ArrayList<>();
        List<WebElement> swapResults = new ArrayList<>();
        if (isElementLoaded(swapSectionInfo, 5) && isElementLoaded(assignSectionInfo, 5)) {
            if (action.equalsIgnoreCase("swap")){
                swapResults = swapSectionInfo.findElements(By.cssSelector("swap-modal-shift"));
            }
            if (action.equalsIgnoreCase("assign")){
                swapResults = assignSectionInfo.findElements(By.cssSelector("swap-modal-shift"));
            }

            if (swapResults != null && swapResults.size() > 0) {
                for(WebElement swapResult : swapResults) {
                    WebElement date = swapResult.findElement(By.className("swap-modal-shift-time"));
                    WebElement nameAndTitle = swapResult.findElement(By.className("swap-modal-shift-person"));
                    if (date != null && nameAndTitle != null) {
                        List <String> temp1 = Arrays.asList(nameAndTitle.getText().split("\n"));
                        List <String> temp2 = Arrays.asList(date.getText().replace(",", "").replace("\n", "").split("-"));
                        swapData.add(temp1.get(0)+"\n"+ temp1.get(1) + "\n"+ temp2.get(0) + "\n"+ temp2.get(1)+" - "+temp2.get(1) );
                        SimpleUtils.report("Get the swap date: " + date.getText() + " and swap name title: " + nameAndTitle.getText() + " Successfully!");
                    }else {
                        SimpleUtils.fail("Failed to find the date and name elements!", false);
                    }
                }
            }else {
                SimpleUtils.fail("Failed to find the swap elements!", false);
            }
        }
        if (swapData.size() != 2) {
            SimpleUtils.fail("Failed to get the swap data!", false);
        }
        return swapData;
    }

    @Override
    public int verifyDayHasShiftByName(int indexOfDay, String name) throws Exception {
        int count = 0;
        List<WebElement> shifts = getDriver().findElements(By.cssSelector("[data-day-index=\"" + indexOfDay + "\"] .week-schedule-shift-wrapper"));
        if (shifts != null && shifts.size() > 0) {
            for (WebElement shift : shifts) {
                WebElement name1 = shift.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (name1 != null && name1.getText().contains(name)) {
                    SimpleUtils.pass("shift exists on this day!");
                    count++;
                }
            }
        } else {
            SimpleUtils.fail("No shifts on the day",false);
        }
        return count;
    }

    @FindBy(css = ".sch-calendar-day-label")
    private List<WebElement> weekDayLabels;
    @Override
    public String getWeekDayTextByIndex(int index) throws Exception {
        String weekDayText = null;
        if (index < 0 || index > 6) {
            SimpleUtils.fail("The parameter index: " + index + " is out of range!", false);
        }
        if (areListElementVisible(weekDayLabels, 10)) {
            weekDayText = weekDayLabels.get(index).getText();
        } else {
            SimpleUtils.fail("Schedule Week View: week day labels failed to load!", false);
        }
        return weekDayText;
    }


    @FindBy(css = "div[ng-repeat=\"error in swapError\"]")
    private List<WebElement> warningMessagesInSwap;
    @FindBy(css = "div[ng-repeat=\"error in assignError\"]")
    private List<WebElement> warningMessagesInAssign;

    @Override
    public boolean verifySwapAndAssignWarningMessageInConfirmPage(String expectedMessage, String action) throws Exception {
        boolean canFindTheExpectedMessage = false;
        if (action.equalsIgnoreCase("swap")) {
            if (areListElementVisible(warningMessagesInSwap, 15) && warningMessagesInSwap.size() > 0) {
                for (int i = 0; i < warningMessagesInSwap.size(); i++) {
                    SimpleUtils.pass("The warning message is : "+warningMessagesInSwap.get(i).getText().toLowerCase());
                    if (warningMessagesInSwap.get(i).getText().toLowerCase().replace("\\-", "").contains(expectedMessage.toLowerCase().replace("\\-", ""))) {
                        canFindTheExpectedMessage = true;
                        SimpleUtils.pass("The expected message can be find successfully");
                        break;
                    }
                }
            } else {
                SimpleUtils.report("There is no warning message in swap section");
            }
        } else if (action.equalsIgnoreCase("assign")) {
            if (areListElementVisible(warningMessagesInAssign, 15) && warningMessagesInAssign.size() > 0) {
                for (int i = 0; i < warningMessagesInAssign.size(); i++) {
                    if (warningMessagesInAssign.get(i).getText().contains(expectedMessage)) {
                        canFindTheExpectedMessage = true;
                        SimpleUtils.pass("The expected message can be find successfully");
                        break;
                    }
                }
            } else {
                SimpleUtils.report("There is no warning message in assign section");
            }
        } else
            SimpleUtils.fail("No this action on drag&drop confirm page", true);

        return canFindTheExpectedMessage;
    }

    @FindBy(css = "div.modal-instance-button")
    private WebElement cancelBtnOnDragAndDropConfirmPage;
    @Override
    public void clickCancelBtnOnDragAndDropConfirmPage() throws Exception {
        if (isElementLoaded(cancelBtnOnDragAndDropConfirmPage,15) ){
            click(cancelBtnOnDragAndDropConfirmPage);
            SimpleUtils.pass("cancel button is clicked successfully!");
        } else {
            SimpleUtils.fail("cancel button is disabled!",false);
        }
    }

    @Override
    public List<WebElement> getOneDayShiftByName(int indexOfDay, String name) throws Exception {
        int count = 0;
        List<WebElement> shiftsOfOneTM = new ArrayList<>();;
        List<WebElement> shifts = getDriver().findElements(By.cssSelector("[data-day-index=\"" + indexOfDay + "\"] .week-schedule-shift-wrapper"));
        if (areListElementVisible(shifts, 5) && shifts != null && shifts.size() > 0) {
            for (WebElement shift : shifts) {
//                clickTheElement(shift.findElement(By.className("week-schedule-shit-open-popover")));
//                String shiftName = MyThreadLocal.getDriver().findElement(By.xpath("//div[@class=\"hover-sub-container\"][1]/div[1]")).getText();
                String shiftName = shift.findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText().toLowerCase();
                if (!shiftName.equals("") && shiftName.toLowerCase().contains(name.toLowerCase())) {
                    shiftsOfOneTM.add(shift);
                    SimpleUtils.pass("shift exists on this day!");
                    count++;
                }
            }
            if(count==0){
                SimpleUtils.report("No shifts on the day for the TM: " + name);
            }
        } else {
            SimpleUtils.fail("No shifts on the day: "+ indexOfDay,false);
        }
        return shiftsOfOneTM;
    }


    @Override
    public List<String> getOpenShiftInfoByIndex(int index) throws Exception {
        List<String> openShiftInfo = new ArrayList<>();
        if (areListElementVisible(weekShifts, 20) && index < weekShifts.size()) {
            String shiftTimeWeekView = weekShifts.get(index).findElement(By.className("week-schedule-shift-time")).getText();
            WebElement infoIcon = weekShifts.get(index).findElement(By.className("week-schedule-shift-open-popover"));
            clickTheElement(infoIcon);
            String workRole = shiftJobTitleAsWorkRole.getText().trim();
            if (isElementLoaded(shiftDuration, 10)) {
                String shiftTime = shiftDuration.getText();
                openShiftInfo.add(shiftTime);
                openShiftInfo.add(workRole);
                openShiftInfo.add(shiftTimeWeekView);
            }
            //To close the info popup
            moveToElementAndClick(weekShifts.get(index));
        } else {
            SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
        }
        if (openShiftInfo.size() != 3) {
            SimpleUtils.fail("Failed to get open shift info!", false);
        }
        return openShiftInfo;
    }


    @Override
    public void dragOneShiftToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception {
        waitForSeconds(3);
        boolean isDragged = false;
        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"]"));
        WebElement weekDay = getDriver().findElement(By.cssSelector("[data-day-index=\""+endIndex+"\"] .sch-calendar-day-label"));
        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0 && weekDay!=null) {
            for (WebElement start : startElements) {
                scrollToElement(start);
                waitForSeconds(1);
                WebElement startName = start.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                SimpleUtils.report("Check the tm name: "+ startName.getText().split(" ")[0]);
                if (startName.getText().split(" ")[0].equalsIgnoreCase(firstName)) {
                    mouseHoverDragandDrop(startName, endElements.get(0));
                    SimpleUtils.report("Drag&Drop: Drag " + firstName + " to " + weekDay.getText() + " days Successfully!");
                    //verifyConfirmStoreOpenCloseHours();
                    isDragged = true;
                    break;
                }
            }
            if (!isDragged) {
                SimpleUtils.fail("Failed to drag the user: " + firstName + " on day: "
                        +startIndex+1+" to another day: "+ endIndex+1+" Successfully!", false);
            }
        } else {
            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
        }
    }

    @Override
    public String getNameOfTheFirstShiftInADay(int dayIndex) throws Exception {
        List<WebElement> elements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + dayIndex + "\"] .week-schedule-shift-wrapper"));
        if (areListElementVisible(elements, 10)){
            return elements.get(0).findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText();
        }
        return null;
    }

    @FindBy(css = "div.lgn-alert-modal")
    private WebElement warningMode;


    @FindBy(css = "span.lgn-alert-message")
    private List<WebElement> warningMessagesInWarningMode;

    @FindBy(css = ".lgn-action-button-success")
    private WebElement okBtnInWarningMode;

    @FindBy(css = ".lgn-action-button-success")
    private WebElement okBtnOnConfirm;
    @FindBy(css = ".MuiDialogContent-root p")
    private List<WebElement> warningMessagesInWarningModeOnNewCreaeShiftPage;
    @Override
    public String getWarningMessageInDragShiftWarningMode() throws Exception {
        String warningMessage = "";
        if(areListElementVisible(warningMessagesInWarningMode, 5) && warningMessagesInWarningMode.size()>0) {
            for (WebElement warningMessageInWarningMode: warningMessagesInWarningMode){
                warningMessage = warningMessage + warningMessageInWarningMode.getText()+"\n";
            }
        } else if (areListElementVisible(warningMessagesInWarningModeOnNewCreaeShiftPage, 5)) {
            for (WebElement message: warningMessagesInWarningModeOnNewCreaeShiftPage) {
                warningMessage = warningMessage + " " + message.getText();
            }
            warningMessage = warningMessage.replace(" AM", "am").replace(" PM", "pm")
                    .replace(":00", "").replace(" 0", " ").replace(".0", "").trim();
        }else {
            SimpleUtils.fail("Warning message fail to load", false);
        }
        return warningMessage;
    }

    @Override
    public void clickOnOkButtonInWarningMode() throws Exception {
        if(isElementLoaded(okBtnInWarningMode, 5)) {
            click(okBtnInWarningMode);
            SimpleUtils.pass("Click on Ok button on warning successfully");
        } else {
            SimpleUtils.report("Ok button fail to load");
        }
    }

    @Override
    public boolean isOkButtonInWarningModeLoaded() throws Exception {
        if(isElementLoaded(okBtnInWarningMode, 5)) {
            SimpleUtils.report("The OK button is loaded on warning mode!");
            return true;
        } else {
            SimpleUtils.report("The OK button is not loaded on warning mode!");
            return false;
        }
    }


    @Override
    public void moveAnywayWhenChangeShift() throws Exception {
        if (isElementLoaded(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")),10)){
            if (moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")).getText().trim().equals("MOVE ANYWAY")) {
                click(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")));
                SimpleUtils.pass("move anyway button clicked!");
            } else {
                SimpleUtils.fail("move anyway button fail to load!",false);
            }
        } else {
            SimpleUtils.fail("move anyway button fail to load!",false);
        }
    }


    @Override
    public void copyAnywayWhenChangeShift() throws Exception {
        if (isElementLoaded(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")),10)){
            if (moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")).getText().trim().equals("COPY ANYWAY")) {
                click(moveAnywayDialog.findElement(By.cssSelector(".lgn-action-button-success")));
                SimpleUtils.pass("copy anyway button clicked!");
            } else {
                SimpleUtils.fail("copy anyway button fail to load!",false);
            }
        } else {
            SimpleUtils.fail("copy anyway button fail to load!",false);
        }
    }

    @Override
    public void verifyShiftIsMovedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception {
        boolean isMoved = false;
        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"] .week-schedule-shift-wrapper"));
        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
            for (WebElement start : startElements) {
                WebElement startName = start.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (startName != null) {
                    if (startName.getText().equalsIgnoreCase(firstName)) {
                        SimpleUtils.fail("Still can find the TM:" + firstName + " on " + startIndex, false);
                    }
                } else {
                    SimpleUtils.fail("Failed to find the worker name elements!", false);
                }
            }
            for (WebElement end : endElements) {
                WebElement endName = end.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (endName != null) {
                    if (endName.getText().contains(firstName)) {
                        isMoved = true;
                        break;
                    }
                } else {
                    SimpleUtils.fail("Failed to find the worker name elements!", false);
                }
            }
            if (!isMoved) {
                SimpleUtils.fail(firstName + " isn't moved to the day, which index is: " + endIndex, false);
            }
        } else {
            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
        }
    }

    @Override
    public void verifyShiftIsCopiedToAnotherDay(int startIndex, String firstName, int endIndex) throws Exception {
        boolean isCopied = false;
        boolean isExisted = false;
        List<WebElement> startElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + startIndex + "\"] .week-schedule-shift-wrapper"));
        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"] .week-schedule-shift-wrapper"));
        if (startElements != null && endElements != null && startElements.size() > 0 && endElements.size() > 0) {
            for (WebElement start : startElements) {
                WebElement startName = start.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (startName != null) {
                    if (startName.getText().contains(firstName)) {
                        SimpleUtils.pass("Can find the TM:" + firstName + " on " + startIndex);
                        isCopied = true;
                        break;
                    }
                } else {
                    SimpleUtils.fail("Failed to find the worker name elements!", false);
                }
            }
            for (WebElement end : endElements) {
                WebElement endName = end.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (endName != null) {
                    if (endName.getText().contains(firstName)) {
                        isCopied = true;
                        break;
                    }
                } else {
                    SimpleUtils.fail("Failed to find the worker name elements!", false);
                }
            }
            if (!isCopied && !isExisted) {
                SimpleUtils.fail(firstName + " isn't copied to the day correctly, which index is: " + endIndex, false);
            }
        } else {
            SimpleUtils.fail("Schedule Page: Failed to find the shift elements for index: " + startIndex + " or " + endIndex, false);
        }
    }




    @Override
    public WebElement getTheShiftByIndex(int index) throws Exception {
        WebElement shift = null;
        if (areListElementVisible(weekShifts, 20) && index < weekShifts.size()) {
            shift = weekShifts.get(index);
            SimpleUtils.pass("Get the "+index+" shift in week view successfully! ");
        } else if (areListElementVisible(shiftsInDayView, 20) && index < shiftsInDayView.size()) {
            shift = shiftsInDayView.get(index);
            SimpleUtils.pass("Get the "+index+" shift in day view successfully! ");
        } else
            SimpleUtils.fail("Schedule Page: week or day shifts not loaded successfully!", false);
        return shift;
    }


    @Override
    public int getTheIndexOfEditedShift() throws Exception {
        int index = -1;
        if (areListElementVisible(weekShifts, 10)) {
            for (int i = 0; i < weekShifts.size(); i++) {
                try {
                    WebElement editedShift = weekShifts.get(i).findElement(By.cssSelector("[src*=\"edited-shift-week.png\"]"));
                    index = i;
                    SimpleUtils.pass("Schedule Week View: Get the index of the edited shift successfully: " + i);
                    break;
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    continue;
                }
            }
        } else if (areListElementVisible(shiftsInDayView,10)) {
            for (int i = 0; i < shiftsInDayView.size(); i++) {
                try {
                    WebElement editedShift = shiftsInDayView.get(i).findElement(By.xpath("./../../preceding-sibling::div[1][@ng-if=\"isShiftBeingEdited(shift)\"]"));
                    index = i;
                    SimpleUtils.pass("Schedule Day View: Get the index of the edited shift successfully: " + i);
                    break;
                } catch (NoSuchElementException e) {
                    continue;
                }
            }
        } else {
            SimpleUtils.fail("Schedule Page: There are no shifts loaded!", false);
        }
        return index;
    }


    @Override
    public int getShiftsNumberByName(String name) throws Exception {
        int result = 0;
        if (areListElementVisible(shiftsWeekView, 15)) {
            if (name == null && name.equals("")){
                result = shiftsWeekView.size();
            } else {
                for (WebElement shiftWeekView : shiftsWeekView) {
                    WebElement workerName = shiftWeekView.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                    if (workerName != null) {
                        if (workerName.getText().toLowerCase().contains(name.toLowerCase())) {
                            result++;
                        }
                    }
                }
            }
        }
        return result;
    }


    @FindBy(css = ".swap-modal.modal-instance")
    private WebElement dragAndDropConfirmPage;
    @Override
    public boolean isDragAndDropConfirmPageLoaded() throws Exception {
        boolean isConfirmPageDisplay = false;
        if (isElementLoaded(dragAndDropConfirmPage,15) ){
            isConfirmPageDisplay = true;
            SimpleUtils.report("Drag and Drop confirm page is display!");
        } else {
            SimpleUtils.report("Drag and Drop confirm page is not display!");
        }
        return isConfirmPageDisplay;
    }

    @Override
    public List<WebElement> getShiftsByNameOnDayView(String name) throws Exception {
        int count = 0;
        List<WebElement> shiftsOfOneTM = new ArrayList<>();
        if (areListElementVisible(dayViewAvailableShifts, 5) && dayViewAvailableShifts != null && dayViewAvailableShifts.size() > 0) {
            for (WebElement shift : dayViewAvailableShifts) {
                String shiftName = shift.findElement(By.cssSelector(".sch-day-view-shift-worker-name")).getText().split("\\(")[0].trim();
                if (shiftName.toLowerCase().contains(name.toLowerCase())) {
                    shiftsOfOneTM.add(shift);
                    SimpleUtils.pass("shift exists on this day!");
                    count++;
                }
            }
            if(count==0){
                SimpleUtils.report("No shifts on the day for the TM: " + name);
            }
        } else {
            SimpleUtils.fail("No shifts on the day",false);
        }
        return shiftsOfOneTM;
    }


    @FindBy (className = "week-schedule-shift-title")
    private List<WebElement> weekScheduleShiftTitles;

    @FindBy (className = "sch-group-label")
    private List<WebElement> dayScheduleGroupLabels;

    @FindBy(css = "[src=\"img/legion/edit/deleted-shift-week.png\"]")
    private List<WebElement> deleteShiftImgsInWeekView;

    @FindBy(css = "img[ng-src=\"img/legion/edit/deleted-shift-day@2x.png\"]")
    private List<WebElement> deleteShiftImgsInDayView;

    @Override
    public List<String> getWeekScheduleShiftTitles() throws Exception {
        List<String> weekShiftTitles = new ArrayList<>();
        if (areListElementVisible(weekScheduleShiftTitles, 10)) {
            for (WebElement title: weekScheduleShiftTitles) {
                weekShiftTitles.add(title.getText());
            }
        }
        return weekShiftTitles;
    }

    @Override
    public List<String> getDayScheduleGroupLabels() throws Exception {
        List<String> dayGroupLabels = new ArrayList<>();
        if (areListElementVisible(dayScheduleGroupLabels, 10)) {
            for (WebElement label: dayScheduleGroupLabels) {
                dayGroupLabels.add(label.getText());
            }
        }
        return dayGroupLabels;
    }

    @Override
    public boolean isShiftInDayPartOrNotInWeekView(int shiftIndex, String dayPart) throws Exception {
        boolean isIn = false;
        int index1 = -1;
        int index2 = -1;
        for (int i = 0; i < weekScheduleShiftTitles.size(); i++) {
            if (weekScheduleShiftTitles.get(i).getText().equals(dayPart)) {
                for (int k = 7; k < 13; k++) {
                    try {
                        WebElement nextShift = weekScheduleShiftTitles.get(i).findElement(By.xpath("./../../following-sibling::div[" + k + "]/div"));
                        if (isElementLoaded(nextShift,10)) {
                            index1 = getTheIndexOfShift(nextShift);
                            break;
                        }
                    } catch (NoSuchElementException e) {
                        continue;
                    }
                }
                if (i != weekScheduleShiftTitles.size() - 1) {
                    for (int j = 7; j < 13; j++) {
                        try {
                            WebElement nextShift = weekScheduleShiftTitles.get(i + 1).findElement(By.xpath("./../../following-sibling::div[" + j + "]/div"));
                            if (isElementLoaded(nextShift,10)) {
                                index2 = getTheIndexOfShift(nextShift);
                                break;
                            }
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                    }
                } else
                    index2 = weekShifts.size() - 1;
                if (shiftIndex >= index1 && shiftIndex <= index2) {
                    isIn = true;
                }
                break;
            }
        }
        return isIn;
    }

    @Override
    public boolean isShiftInDayPartOrNotInDayView(int shiftIndex, String dayPart) throws Exception {
        boolean isIn = false;
        int index2 = -1;
        for (int i = 0; i < dayScheduleGroupLabels.size(); i++) {
            if (dayScheduleGroupLabels.get(i).getText().equals(dayPart)) {
                int index1 = getTheIndexOfShift(dayScheduleGroupLabels.get(i).findElement(By.xpath("./../../following-sibling::div[1]//worker-shift/div")));
                if (i != dayScheduleGroupLabels.size() - 1)
                    index2 = getTheIndexOfShift(dayScheduleGroupLabels.get(i+1).findElement(By.xpath("./../../following-sibling::div[1]//worker-shift/div")));
                else
                    index2 = shiftsInDayView.size() - 1;
                if (shiftIndex >= index1 && shiftIndex <= index2) {
                    isIn = true;
                    break;
                }
            }
        }
        return isIn;
    }

    @Override
    public int getTheIndexOfShift(WebElement shift) throws Exception {
        int index = -1;
        if (areListElementVisible(weekShifts, 10)) {
            for (int i = 0; i < weekShifts.size(); i++) {
                try {
                    if (weekShifts.get(i).equals(shift)) {
                        index = i;
                        SimpleUtils.pass("Schedule Week View: Get the index of the shift successfully: " + i);
                        break;
                    }

                } catch (NoSuchElementException e) {
                    continue;
                }
            }
        } else if (areListElementVisible(shiftsInDayView, 10)) {
            for (int i = 0; i < shiftsInDayView.size(); i++) {
                try {
                    if (shiftsInDayView.get(i).equals(shift)) {
                        index = i;
                        SimpleUtils.pass("Schedule Day View: Get the index of the shift successfully: " + i);
                        break;
                    }

                } catch (NoSuchElementException e) {
                    continue;
                }
            }
        } else {
            SimpleUtils.fail("Schedule Week View: There are no shifts loaded!", false);
        }
        return index;
    }


    // added by Fiona
    public List<String> verifyDaysHasShifts() throws Exception {
        List<String> dayHasShifts = new ArrayList<String>();
        if (areListElementVisible(scheduleDays, 10)) {
            for(WebElement scheduleDay:scheduleDays){
                String dayAbbr = scheduleDay.findElement(By.cssSelector("div.sch-calendar-day-label")).getText().trim();
                String totalCalendarDaySummary = scheduleDay.findElement(By.cssSelector("div.sch-calendar-day-summary span")).getText().trim().split(" ")[0];
                if(! totalCalendarDaySummary.equals("0")){
                    dayHasShifts.add(dayAbbr);
                    SimpleUtils.pass(dayAbbr + " has shifts!");
                }
            }
        }
        return dayHasShifts;
    }

    @FindBy(css=".ReactVirtualized__Grid__innerScrollContainer")
    private WebElement shiftsTable;

    @Override
    public void verifyShiftTimeInReadMode(String index,String shiftTime) throws Exception{
        String shiftTimeInShiftTable = null;
        if (isElementEnabled(shiftsTable,5)) {
            List<WebElement> shiftsTableList = shiftsTable.findElements(By.cssSelector("div[data-day-index=\"" + index + "\"].week-schedule-shift"));
            for(WebElement shiftTable:shiftsTableList){
                shiftTimeInShiftTable = shiftTable.findElement(By.cssSelector(".week-schedule-shift-time")).getText().trim();
                if(shiftTimeInShiftTable.equals(shiftTime)){
                    SimpleUtils.pass("The shift time on data-day-index: " + index + " is aligned with advance staffing rule");
                }else {
                    SimpleUtils.fail("The shift time is NOT aligned with advance staffing rule",false);
                }
            }
        }else{
            SimpleUtils.fail("There is no shifts generated.",false);
        }
    }

    @Override
    public List<String> getIndexOfDaysHaveShifts() throws Exception {
        List<String> index = new ArrayList<String>();
        String dataDayIndex = null;
        if (areListElementVisible(scheduleDays, 10)) {
            for(WebElement scheduleDay:scheduleDays){
                String totalCalendarDaySummary = scheduleDay.findElement(By.cssSelector("div.sch-calendar-day-summary span")).getText().trim().split(" ")[0];
                if(! totalCalendarDaySummary.equals("0")){
                    dataDayIndex = scheduleDay.getAttribute("data-day-index").trim();
                    index.add(dataDayIndex);
                }
            }
        } else {
            SimpleUtils.fail("Table header fail to load!", false);
        }
        return index;
    }

    @FindBy(css = ".week-schedule-shift .shift-container .rows .worker-image-optimized")
    private List<WebElement> profileIconsRingsInWeekView;
    @FindBy(css = ".sch-day-view-shift-outer .allow-pointer-events")
    private List<WebElement> profileIconsRingsInDayView;
    @FindBy(css = ".sch-day-view-shift-outer")
    private List<WebElement> shiftOuterInDayView;

    @Override
    public void verifyShiftsHasMinorsColorRing(String minorsType) throws Exception {
        if (areListElementVisible(profileIconsRingsInDayView, 15)){
            for (WebElement element: profileIconsRingsInDayView){
                if (element.getAttribute("class").contains(minorsType)) {
                    SimpleUtils.pass("The color ring display correctly! ");
                } else
                    SimpleUtils.fail("No colered ring representing minors", false);
            }
        } else if (areListElementVisible(profileIconsRingsInWeekView, 15)){
            for (WebElement element: profileIconsRingsInWeekView){
                if (element.getAttribute("class").contains(minorsType)) {
                    SimpleUtils.pass("The color ring display correctly! ");
                } else
                    SimpleUtils.fail("No colered ring representing minors",false);
            }
        } else {
            SimpleUtils.fail("No profile icons!", false);
        }
    }


    @FindBy(css = ".week-schedule-ribbon-group-toggle")
    private List<WebElement> groupTitleList;
    @Override
    public void verifyGroupCanbeCollapsedNExpanded() throws Exception {
        if (areListElementVisible(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")),10)){
            for (int i=0; i< getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).size(); i++){
                clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("week-schedule-ribbon-group-toggle-closed")){
                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                    if (!getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("week-schedule-ribbon-group-toggle-closed")){
                        SimpleUtils.pass("Group can be expanded!!");
                    } else {
                        SimpleUtils.fail("Group is not expanded!", false);
                    }
                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                    if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("week-schedule-ribbon-group-toggle-closed")){
                        SimpleUtils.pass("Group can be collapsed!");
                    } else {
                        SimpleUtils.fail("Group is not collapsed!", false);
                    }
                }
                if (!getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("week-schedule-ribbon-group-toggle-closed")){
                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                    if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("week-schedule-ribbon-group-toggle-closed")){
                        SimpleUtils.pass("Group can be collapsed!");
                    } else {
                        SimpleUtils.fail("Group is not collapsed!", false);
                    }
                }
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
    }

    @Override
    public void expandOnlyOneGroup(String groupName) throws Exception {
        if (areListElementVisible(groupTitleList,10)){
            for (int i=0; i< groupTitleList.size(); i++){
                if (!groupTitleList.get(i).getText().equalsIgnoreCase(groupName)){
                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                    if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
                        SimpleUtils.pass("Group is collapsed!");
                    } else {
                        clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                        if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
                            SimpleUtils.pass("Group is collapsed!");
                        } else {
                            SimpleUtils.fail("Group is not able to be collapsed!", false);
                        }
                    }
                }
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
    }

    @Override
    public void verifyGroupByTitlesAreExpanded() throws Exception {
        if (areListElementVisible(groupTitleList,10)){
            for (int i=0; i< getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).size(); i++){
                if (!getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
                    SimpleUtils.pass("Group is expanded!");
                } else {
                    SimpleUtils.fail("Group should be expanded by default!", false);
                }
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
    }

    //used by Group by work role and job title.
    @Override
    public List<String> verifyGroupByTitlesOrder() throws Exception{
        List<String> results = new ArrayList<>();
        List<WebElement> groupTitles = new ArrayList<>();
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (scheduleCommonPage.isScheduleDayViewActive()) {
            groupTitles = availableJobTitleListInDayView;
        } else
            groupTitles = groupTitleList;
        if (areListElementVisible(groupTitles, 10)){
            for (WebElement element: groupTitles){
                results.add(element.getText().trim());
            }
            SimpleUtils.assertOnFail("Order results is incorrect!", results.stream().sorted().collect(Collectors.toList()).containsAll(results), false);
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
        return results;
    }

    @Override
    public ArrayList<HashMap<String,String>> getGroupByOptionsStyleInfo() throws Exception{
        ArrayList<HashMap<String,String>> results = new ArrayList<>();
        List<WebElement> groupTitles;
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if (scheduleCommonPage.isScheduleDayViewActive()) {
            groupTitles = availableJobTitleListInDayView;
        } else
            groupTitles = groupTitleList;
        if (areListElementVisible(groupTitles, 10)){
            for (WebElement element: groupTitles){
                HashMap<String, String> workRoleStyleInfo = new HashMap<>();
                workRoleStyleInfo.put("optionName",element.findElement(By.cssSelector(".week-schedule-shift-title")).getText().toLowerCase());
                try {
                    if (isElementLoaded(element.findElement(By.cssSelector(".week-schedule-shift-color")), 5)) {
                        workRoleStyleInfo.put("optionStyle", element.findElement(By.cssSelector(".week-schedule-shift-color")).getAttribute("style"));
                    } else {
                        workRoleStyleInfo.put("optionStyle","");
                    }
                } catch (Exception e) {
                    workRoleStyleInfo.put("optionStyle","");
                }
                results.add(workRoleStyleInfo);
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
        return results;
    }

    @FindBy(css = ".week-schedule-shift[data-day=\"1\"] .rows .week-schedule-worker-name")
    private List<WebElement> workerNamesOnTheShiftsOfTheFirstDay;
    @Override
    public void verifyGroupByTMOrderResults() throws Exception{
        List<String> results = new ArrayList<>();
        if (areListElementVisible(workerNamesOnTheShiftsOfTheFirstDay, 10)){
            for (WebElement element: workerNamesOnTheShiftsOfTheFirstDay){
                results.add(element.getText().trim());
            }
            SimpleUtils.assertOnFail("Order results is incorrect!", results.stream().sorted().collect(Collectors.toList()).equals(results), false);
        } else {
            SimpleUtils.fail("No shifts on the first day of the week!", false);
        }
    }

    @FindBy(css = ".week-schedule-shift[data-day=\"1\"] .week-schedule-shift-time")
    private List<WebElement> shiftTimesOnTheShiftsOfTheFirstDay;
    @Override
    public void verifyShiftsOrderByStartTime() throws Exception {
        List<String> results = new ArrayList<>();
        if (areListElementVisible(shiftTimesOnTheShiftsOfTheFirstDay, 10)){
            for (WebElement element: shiftTimesOnTheShiftsOfTheFirstDay){
                results.add(element.getText().split("-")[0].trim());
            }
            boolean flag = false;
            String first = results.get(0);
            String last = results.get(results.size()-1);
            if ((first.contains("am") && last.contains("am")) || (first.contains("pm") && last.contains("pm"))){
                if (SimpleUtils.isNumeric(first.replace("am","").replace("pm","")) && SimpleUtils.isNumeric(last.replace("pm","").replace("pm",""))){
                    if (Float.valueOf(first.replace("am","").replace("pm","")) <= Float.valueOf(first.replace("am","").replace("pm",""))){
                        flag = true;
                    }
                }
            } else {
                if (first.contains("am")){
                    flag = true;
                }
            }
            SimpleUtils.assertOnFail("Order results is incorrect!", flag, false);
        } else {
            SimpleUtils.fail("No shifts on the first day of the week!", false);
        }
    }

    @FindBy(css = "div.holiday-logo-container")
    private WebElement holidayLogoContainer;

    @FindBy(css = "tr[ng-repeat=\"day in summary.workingHours\"]")
    private List<WebElement> guidanceWeekOperatingHours;
    @Override
    public boolean inActiveWeekDayClosed(int dayIndex) throws Exception {
        CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
        if (createSchedulePage.isWeekGenerated()) {
            ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
            scheduleCommonPage.navigateDayViewWithIndex(dayIndex);
            if (isElementLoaded(holidayLogoContainer))
                return true;
        } else {
            if (guidanceWeekOperatingHours.size() != 0) {
                if (guidanceWeekOperatingHours.get(dayIndex).getText().contains("Closed"))
                    return true;
            }
        }
        return false;

    }

    @Override
    public void verifyTimeOffCardShowInCorrectDay(int dayIndex) throws Exception {
        try {
            boolean flag = false;
            if (areListElementVisible(ptoPlaces, 5) && ptoPlaces.size() > 0) {
                for (WebElement pto : ptoPlaces) {
                    if (pto.getAttribute("data-day-index").equalsIgnoreCase(String.valueOf(dayIndex))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    SimpleUtils.pass("Time Off card shows correctly on schedule when grouping by TM!");
                } else {
                    SimpleUtils.fail("Time Off card failed to show on schedule when grouping by TM!", false);
                }
            } else {
                SimpleUtils.fail("Time Off card failed to show on schedule when grouping by TM!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail("Time Off card failed to show on schedule when grouping by TM!", false);
        }
    }

    public String getFullNameOfOneShiftByIndex (int index) {
        String fullName = "";
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        List<WebElement> shifts = new ArrayList<>();
        if(!areListElementVisible(weekShifts, 10)){
            shifts = dayViewAvailableShifts;
            if (areListElementVisible(shifts, 20) && index < shifts.size()) {
                String[] nameAndWorkRole = shifts.get(index).findElement(By.className("sch-day-view-shift-worker-name"))
                        .getText().split("\\(")[0].trim().split(" ");
                fullName = nameAndWorkRole[0] + " " + nameAndWorkRole[1];
            } else {
                SimpleUtils.fail("Schedule Page: day shifts not loaded successfully!", false);
            }
        } else {
            shifts = weekShifts;
            if (areListElementVisible(shifts, 20) && index < shifts.size()) {
                fullName = shifts.get(index).findElement(By.cssSelector(".rows .week-schedule-worker-name")).getText();
            } else {
                SimpleUtils.fail("Schedule Page: week shifts not loaded successfully!", false);
            }
        }
        return fullName;
    }

    //Focus on one shift and the X button and shift time popup can display display
    public void clickOnShiftInDayView (WebElement shiftInDayView) throws Exception {
        if (isElementLoaded(shiftInDayView, 10)) {
            moveElement(shiftInDayView.findElement(By.cssSelector(".break-container")), 0);
            if (areListElementVisible(shiftTimeInDayViewPopUp, 5)) {
                SimpleUtils.pass("Click the shift successfully! ");
            } else
                SimpleUtils.fail("Fail to click the shift! ", false);
        } else
            SimpleUtils.fail("The given shift is not exist! ", false);
    }

    @FindBy(css = ".sch-day-view-shift-hour-container")
    private List<WebElement> shiftTimeInDayViewPopUp;

    public List<String> getShiftTimeInDayViewPopUp () {
        List<String> shiftTimes = new ArrayList<>();
        if (areListElementVisible(shiftTimeInDayViewPopUp, 10)) {
            shiftTimes.add(shiftTimeInDayViewPopUp.get(0).getText().replace("\n", " "));
            shiftTimes.add(shiftTimeInDayViewPopUp.get(1).getText().replace("\n", " "));
            SimpleUtils.pass("Get shift times successfully! ");
        } else
            SimpleUtils.fail("The shift times in popup in day view fail to load! ", false);
        return shiftTimes;
    }

    public void clickOnXButtonInDayView () throws Exception {
        if(isElementLoaded(btnDelete, 10)){
            clickTheElement(btnDelete);
            if (areListElementVisible(deleteShiftImgsInDayView, 10)){
                SimpleUtils.pass("Click the X button successfully! ");
            } else
                SimpleUtils.fail("Fail to click the X button successfully! ", false);
        } else
            SimpleUtils.fail("The X button is not display! ", false);
    }


    @FindBy(css = "[ng-repeat=\"shift in shiftGroup\"]")
    private List<WebElement> dayViewShiftGroups;
    public boolean checkIfShiftInDayViewBeenMarkAsDeletedByIndex (int index) throws Exception {
        boolean isShiftMarkAsDeleted = false;
        if (areListElementVisible(dayViewShiftGroups, 5) && dayViewShiftGroups.size() > index) {
            if (isElementLoaded(dayViewShiftGroups.get(index).findElement(By.cssSelector(".sch-day-view-shift")))
                    && dayViewShiftGroups.get(index).findElement(By.cssSelector(".sch-day-view-shift")).getAttribute("class").contains("deleted")) {
                isShiftMarkAsDeleted = true;
                SimpleUtils.pass("The shift with index: " + index + " been marked as deleted in day view!");
            }
        } else
            SimpleUtils.fail("The shifts in day view fail to load! ", false);
        return isShiftMarkAsDeleted;
    }


    public void verifyTheEditedImgDisplayForShiftInDayByIndex (int index) throws Exception {
        if (areListElementVisible(dayViewShiftGroups, 5) && dayViewShiftGroups.size() > index) {
            if (isElementLoaded(dayViewShiftGroups.get(index).findElement(By.cssSelector("[ng-src=\"img/legion/edit/edited-shift-day@2x.png\"]")))) {
                SimpleUtils.pass("The shift with index: " + index + " been marked as edited in day view!");
            } else
                SimpleUtils.fail("The shift with index: " + index + " is not marked as edited in day view!", false);
        } else
            SimpleUtils.fail("The shifts in day view fail to load! ", false);
    }


    public void clickTheDeleteImgForSpecifyShiftByIndex (int index) throws Exception {
        if (areListElementVisible(dayViewShiftGroups, 5) && dayViewShiftGroups.size() > index) {
            if (isElementLoaded(dayViewShiftGroups.get(index).findElement(By.cssSelector("[ng-src=\"img/legion/edit/deleted-shift-day@2x.png\"]")))) {
                clickTheElement(dayViewShiftGroups.get(index).findElement(By.cssSelector("[ng-src=\"img/legion/edit/deleted-shift-day@2x.png\"]")));
                waitForSeconds(2);
                if (dayViewShiftGroups.get(index).findElement(By.cssSelector(".sch-day-view-shift")).getAttribute("class").contains("deleted")) {
                    SimpleUtils.pass("The shift with index: " + index + " been returned back in day view successfully!");
                }
            } else
                SimpleUtils.fail("The delete img for the shift fail to load! ", false);
        }else
            SimpleUtils.fail("The shifts in day view fail to load! ", false);
    }

    public void clickOnEditedOrDeletedImgForShiftInDayViewByIndex (int index) throws Exception {
        if (areListElementVisible(dayViewShiftGroups, 5) && dayViewShiftGroups.size() > index) {
            if (isElementLoaded(dayViewShiftGroups.get(index).findElement(By.cssSelector(".sch-day-view-right-gutter img")))) {
                scrollToElement(dayViewShiftGroups.get(index).findElement(By.cssSelector(".sch-day-view-right-gutter img")));
                clickTheElement(dayViewShiftGroups.get(index).findElement(By.cssSelector(".sch-day-view-right-gutter img")));
                SimpleUtils.pass("Click the deleted or edited img for the shift successfully! ");
            } else
                SimpleUtils.fail("The deleted or edited img for the shift fail to load! ", false);
        } else
            SimpleUtils.fail("The shifts in day view fail to load! ", false);
    }


    public void moveShiftByIndexInDayView (int index, boolean moveForeward) throws Exception {
        if (areListElementVisible(dayViewShiftGroups, 5) && dayViewShiftGroups.size() > index) {
            scrollToElement(dayViewShiftGroups.get(index));
            moveElement(dayViewShiftGroups.get(index).findElement(By.cssSelector(".left-shift-box")), 0);
            if (moveForeward) {
                mouseHoverDragandDrop(dayViewShiftGroups.get(index).findElement(By.cssSelector(".left-shift-box")),
                        dayViewShiftGroups.get(index).findElements(By.cssSelector(".sch-day-view-grid-cell")).get(0));
                int xOffSet = -150;
                moveDayViewCards(dayViewShiftGroups.get(index).findElement(By.cssSelector(".left-shift-box")), xOffSet);
            } else
                mouseHoverDragandDrop(dayViewShiftGroups.get(index).findElement(By.cssSelector(".left-shift-box")),
                    dayViewShiftGroups.get(index).findElements(By.cssSelector(".sch-day-view-grid-cell")).get(dayViewShiftGroups.get(index).findElements(By.cssSelector(".sch-day-view-grid-cell")).size()-1));
            SimpleUtils.pass("Click the shift successfully! ");
        }else
            SimpleUtils.fail("The shifts in day view fail to load! ", false);
    }

    //Total have 14 schedule summary hours in schedule shift table
    @FindBy(css = "div.week-view-day-distribution-tooltip")
    private WebElement tooltipOfScheduleSummaryHours;

    @FindBy(css = "div.popover")
    private WebElement tooltipOfScheduleSummaryHoursInDayView;

    @FindBy(css = ".text img")
    private List<WebElement> daySummariesInDayView;

    @FindBy(css = "div.guidance-vs-scheduled-helper img")
    private List<WebElement> clockImgs;

    public String getTheTooltipOfClockImgsByIndex (int index) throws Exception {
        String tooltip = "";
        if (areListElementVisible(clockImgs, 10) && clockImgs.size()== 2) {
            moveToElementAndClick(clockImgs.get(index));
            if (isElementLoaded(tooltipOfScheduleSummaryHoursInDayView, 5)) {
                tooltip = tooltipOfScheduleSummaryHoursInDayView.getText();
            } else
                SimpleUtils.fail("The tooptip of clock img failed to load!", false);
        } else
            SimpleUtils.fail("Clock img failed to load on schedule shift table! ", false);
        return tooltip;
    }

    public String getTheTooltipOfScheduleSummaryHoursByIndex (int index) throws Exception {
        String tooltip = "";
        if (areListElementVisible(daySummaries, 10) && daySummaries.size()> 0) {
            clickTheElement(daySummaries.get(index));
            moveToElementAndClick(daySummaries.get(index));
            moveToElementAndClick(daySummaries.get(index));
            moveToElementAndClick(daySummaries.get(index));
            if (isElementLoaded(tooltipOfScheduleSummaryHours, 10)) {
                tooltip = tooltipOfScheduleSummaryHours.getText();
            } else
                SimpleUtils.fail("The "+index+" tooptip of schedule summary hours failed to load!", false);
        } else
            SimpleUtils.fail("Schedule hours failed to load on schedule shift table! ", false);
        return tooltip;
    }



    @FindBy(css = "div.tooltip-for-hours")
    private List<WebElement> toolTipForScheduleHours;
    @FindBy(css = "div.tooltip-for-hours img")
    private List<WebElement> arrowInToolTip;

    public HashMap<String, String> getHrsOnTooltipOfScheduleSummaryHoursByIndex (int index) throws Exception {
        scrollToBottom();
        HashMap<String, String> tooltip = new HashMap<>();
        if (areListElementVisible(daySummaries, 5)
                && daySummaries.size()> 0
                && index<daySummaries.size()) {
            scrollToElement(daySummaries.get(index));
            waitForSeconds(3);
            moveToElementAndClick(daySummaries.get(index));
            tooltip = getHrsOnTooltip();
        } else if (areListElementVisible(daySummariesInDayView, 5)
                && daySummariesInDayView.size()> 0
                && index<daySummariesInDayView.size()) {
            moveToElementAndClick(daySummariesInDayView.get(index));
            tooltip = getHrsOnTooltip();
        }else
            SimpleUtils.fail("Schedule hours failed to load on schedule shift table! ", false);
        return tooltip;
    }

    private HashMap<String, String> getHrsOnTooltip() throws Exception {
        HashMap<String, String> tooltip = new HashMap<>();
        if (areListElementVisible(toolTipForScheduleHours, 5)) {
            WebElement toolTip = toolTipForScheduleHours.get(toolTipForScheduleHours.size()-1);
            tooltip.put("date", toolTip.findElement(By.tagName("b")).getText());
            tooltip.put("budgetHrs", toolTip
                    .findElements(By.cssSelector("span.pull-right")).get(0).getText());
            tooltip.put("scheduledHrs", toolTip
                    .findElements(By.cssSelector("span.pull-right")).get(1).getText());
            tooltip.put("differenceHrs", toolTip
                    .findElements(By.cssSelector("span.pull-right")).get(2).getText());
            String arrowStatus = "";
            if (areListElementVisible(arrowInToolTip, 5)) {
                WebElement arrow = arrowInToolTip.get(arrowInToolTip.size()-1);
                if (arrow.getAttribute("src").contains("yellow")) {
                    arrowStatus= "yellow ";
                } else
                    arrowStatus = "red ";
                if (arrow.getAttribute("class").contains("rotate-90deg")) {
                    arrowStatus = arrowStatus + "up";
                } else
                    arrowStatus = arrowStatus + "down";
            }
            tooltip.put("differenceArrow", arrowStatus);
        } else
            SimpleUtils.fail("The tooptip of schedule summary hours failed to load!", false);
        return tooltip;
    }


    public String getTheTooltipOfScheduleSummaryHoursInDayViewByIndex (int index) throws Exception {
        String tooltip = "";
        if (areListElementVisible(daySummariesInDayView, 10) && daySummariesInDayView.size()>0) {
            moveToElementAndClick(daySummariesInDayView.get(index));
            if (isElementLoaded(tooltipOfScheduleSummaryHoursInDayView, 10)) {
                tooltip = tooltipOfScheduleSummaryHoursInDayView.getText();
            } else
                SimpleUtils.fail("The tooptip of schedule summary hours failed to load in day view!", false);
        } else
            SimpleUtils.fail("Schedule hours failed to load on schedule shift table in day view! ", false);
        return tooltip;
    }


    @Override
    public float calcTotalScheduledHourForOneDayInWeekView(int indexOfDay) throws Exception {
        float sumOfAllShiftsLengthOfOneDay = 0;
        List<WebElement> shifts = getDriver().findElements(By.cssSelector("[data-day-index=\"" + indexOfDay + "\"] .week-schedule-shift-wrapper"));
        if (shifts != null && shifts.size() > 0) {
            for (WebElement shift : shifts) {
                float shiftHrs = Float.parseFloat(getShiftInfoFromInfoPopUp(shift).get("DailyHrs"));
                sumOfAllShiftsLengthOfOneDay += shiftHrs;
                SimpleUtils.pass("Get one shift hrs successfully! ");
            }
        } else {
            SimpleUtils.report("No shifts on the day");
        }
        return sumOfAllShiftsLengthOfOneDay;
    }


    @FindBy (css = ".sch-calendar-day-summary img")
    private List<WebElement> differenceHrsArrowImgInWeekView;
    @FindBy (css = "div.sch-day-view-grid-header img")
    private List<WebElement> differenceHrsArrowImgInDayView;
    public List<String> getAllDifferenceHrsArrowImg () {
        List<String> arrowStatus = new ArrayList<>();
        List<WebElement> arrowImg = new ArrayList<>();
        if (areListElementVisible(differenceHrsArrowImgInWeekView, 5)) {
            arrowImg = differenceHrsArrowImgInWeekView;
        } else if (areListElementVisible(differenceHrsArrowImgInDayView, 5)){
            arrowImg = differenceHrsArrowImgInDayView;
        } else
            SimpleUtils.report("There is no difference hrs arrow img display on schedule table! ");
        if (areListElementVisible(arrowImg) && arrowImg.size() >0) {
            for (WebElement img: arrowImg) {
                String status = "";
                if (img.getAttribute("src").contains("yellow")) {
                    status= "yellow ";
                } else if (img.getAttribute("src").contains("green")) {
                    status= "green";
                } else
                    status = "red ";
                if (img.getAttribute("class").contains("rotate-90deg")
                        || img.getAttribute("style").contains("rotate(90deg)")) {
                    status = status + "up";
                } else if (img.getAttribute("class").contains("rotate-270deg")
                        || img.getAttribute("style").contains("rotate(-90deg)")) {
                    status = status + "down";
                }
                arrowStatus.add(status);
                SimpleUtils.pass("Get one arrow img status successfully! ");
            }
        } else
            SimpleUtils.report("There is no difference hrs arrow img display on schedule table! ");
        return arrowStatus;
    }

    // Added by Nora: about bulk delete
    @FindBy (className = "bulk-action-wrapper-inner")
    private WebElement bulkActionMenu;
    @FindBy (css = ".bulk-action-wrapper-element-button")
    private List<WebElement> bulkActionBtns;

    @Override
    public HashSet<Integer> verifyCanSelectMultipleShifts(int shiftCount) throws Exception {
        skipTheNewFeatureDialog();
        HashSet<Integer> set = new HashSet<>();
        List<WebElement> names = null;
        if (areListElementVisible(namesWeekView, 10)) {
            names = namesWeekView;
        } else if (areListElementVisible(shiftOuterInDayView, 10)) {
            names = shiftOuterInDayView;
        }
        if (names.size() >= shiftCount) {
            SimpleUtils.randomSet(0, names.size() - 1, shiftCount, set);
            try {
                if (isElementLoaded(getDriver().findElement(By.cssSelector("[id*='pendo-image-badge']")))) {
                    scrollToElement(getDriver().findElement(By.cssSelector("[id*='pendo-image-badge']")));
                } else if (isElementLoaded(getDriver().findElement(By.cssSelector("[ng-href=\"mailto:help@legion.co\"]")), 5)) {
                    scrollToElement(getDriver().findElement(By.cssSelector("[ng-href=\"mailto:help@legion.co\"]")));
                }
            } catch (Exception e) {
                scrollToBottom();
            }
            waitForSeconds(2);
            Actions action = new Actions(getDriver());
            action.keyDown(Keys.CONTROL).build().perform();
            for (int i : set) {
                action.moveToElement(names.get(i)).click(names.get(i));
                waitForSeconds(2);
            }
            action.keyUp(Keys.CONTROL).build().perform();
            if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftCount) {
                SimpleUtils.pass("Selected " + shiftCount + " shifts successfully");
            } else {
                SimpleUtils.fail("Expected to select " + shiftCount + " shifts, but actually selected " +
                        getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
            }
        } else {
            SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
        }
        return set;
    }

    private void skipTheNewFeatureDialog() throws Exception {
        try {
            if (isElementLoaded(maybeLaterBtn, 5)) {
                clickTheElement(maybeLaterBtn);
            }
        } catch (Exception e) {
            // Do nothing
        }
    }

    @Override
    public void selectSpecificShifts(HashSet<Integer> shiftIndexes) throws Exception {
        skipTheNewFeatureDialog();
        List<WebElement> names = null;
        List<WebElement> shifts = null;
        if (areListElementVisible(namesWeekView, 10) && areListElementVisible(shiftsWeekView, 5)) {
            names = namesWeekView;
            shifts = shiftsWeekView;
        } else if (areListElementVisible(namesDayView, 10) && areListElementVisible(dayViewAvailableShifts, 5)) {
            names = namesDayView;
            shifts = dayViewAvailableShifts;
        }
        try {
            if (isElementLoaded(getDriver().findElement(By.cssSelector(".edit-border")), 5)) {
                scrollToElement(getDriver().findElement(By.cssSelector(".edit-border")));
            } else if (isElementLoaded(getDriver().findElement(By.cssSelector("[ng-href=\"mailto:help@legion.co\"]")), 5)) {
                scrollToElement(getDriver().findElement(By.cssSelector("[ng-href=\"mailto:help@legion.co\"]")));
            }
        } catch (Exception e) {
            scrollToBottom();
        }
        waitForSeconds(2);
        if (!areListElementVisible(selectedShifts, 5)) {
            if (names.size() >= shiftIndexes.size()) {
                Actions action = new Actions(getDriver());
                action.keyDown(Keys.CONTROL).build().perform();
                for (int i : shiftIndexes) {
                    scrollToElement(names.get(i));
                    waitForSeconds(1);
                    action.moveToElement(names.get(i)).click(names.get(i));
                }
                action.keyUp(Keys.CONTROL).build().perform();
                if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftIndexes.size()) {
                    SimpleUtils.pass("Selected " + shiftIndexes.size() + " shifts successfully");
                } else {
                    SimpleUtils.fail("Expected to select " + shiftIndexes.size() + " shifts, but actually selected " +
                            getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
                }
            } else {
                SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
            }
        } else {
            if (names.size() >= shiftIndexes.size()) {
                Actions action = new Actions(getDriver());
                action.keyDown(Keys.CONTROL).build().perform();
                for (int i : shiftIndexes) {
                    scrollToElement(names.get(i));
                    waitForSeconds(1);
                    if (!shifts.get(i).getAttribute("class").contains("shift-selected-multi")) {
                        action.moveToElement(names.get(i)).click(names.get(i));
                    }
                }
                action.keyUp(Keys.CONTROL).build().perform();
                if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftIndexes.size()) {
                    SimpleUtils.pass("Selected " + shiftIndexes.size() + " shifts successfully");
                } else {
                    SimpleUtils.fail("Expected to select " + shiftIndexes.size() + " shifts, but actually selected " +
                            getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
                }
            } else {
                SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
            }
        }
    }

    @Override
    public void bulkDeleteAllShiftsInDayView() throws Exception {
        try {
            if (isElementLoaded(getDriver().findElement(By.cssSelector(".edit-border")), 5)) {
                scrollToElement(getDriver().findElement(By.cssSelector(".edit-border")));
            }
        } catch (Exception e) {
            scrollToBottom();
        }
        waitForSeconds(2);
        if (!areListElementVisible(selectedShifts, 5)) {
            if (namesDayView.size() >= dayViewAvailableShifts.size()) {
                Actions action = new Actions(getDriver());
                action.keyDown(Keys.CONTROL).build().perform();
                for (int i = 0; i < dayViewAvailableShifts.size(); i++) {
                    if (isElementLoaded(namesDayView.get(i), 3)) {
                        scrollToElement(namesDayView.get(i));
                        waitForSeconds(1);
                        action.moveToElement(namesDayView.get(i)).click(namesDayView.get(i));
                    } else if (isElementLoaded(rightShiftBoxes.get(i), 3)) {
                        scrollToElement(rightShiftBoxes.get(i));
                        waitForSeconds(1);
                        action.moveToElement(rightShiftBoxes.get(i)).click(rightShiftBoxes.get(i));
                    }
                }
                action.keyUp(Keys.CONTROL).build().perform();
                if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == dayViewAvailableShifts.size()) {
                    SimpleUtils.pass("Selected " + dayViewAvailableShifts.size() + " shifts successfully");
                } else {
                    SimpleUtils.fail("Expected to select " + dayViewAvailableShifts.size() + " shifts, but actually selected " +
                            getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
                }
            } else {
                SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
            }
        } else {
            if (namesDayView.size() >= dayViewAvailableShifts.size()) {
                Actions action = new Actions(getDriver());
                action.keyDown(Keys.CONTROL).build().perform();
                for (int i = 0; i < dayViewAvailableShifts.size(); i++) {
                    scrollToElement(namesDayView.get(i));
                    waitForSeconds(1);
                    if (!shifts.get(i).getAttribute("class").contains("shift-selected-multi")) {
                        action.moveToElement(namesDayView.get(i)).click(namesDayView.get(i));
                    }
                }
                action.keyUp(Keys.CONTROL).build().perform();
                if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == dayViewAvailableShifts.size()) {
                    SimpleUtils.pass("Selected " + dayViewAvailableShifts.size() + " shifts successfully");
                } else {
                    SimpleUtils.fail("Expected to select " + dayViewAvailableShifts.size() + " shifts, but actually selected " +
                            getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
                }
            } else {
                SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
            }
        }
        HashSet<Integer> index = new HashSet<>();
        index.add(0);
        rightClickOnSelectedShifts(index);
        clickOnBtnOnBulkActionMenuByText("Delete");
    }

    @Override
    public void rightClickOnSelectedShifts(HashSet<Integer> selectedIndex) throws Exception {
        if (selectedIndex.size() > 0) {
            List<WebElement> names = null;
            if (areListElementVisible(namesWeekView, 10)) {
                names = namesWeekView;
            } else if (areListElementVisible(shiftOuterInDayView, 10)) {
                names = shiftOuterInDayView;
            }
            Actions action = new Actions(getDriver());
            for (int i : selectedIndex) {
                scrollToElement(names.get(i));
                waitForSeconds(1);
                action.contextClick(names.get(i)).build().perform();
                if (isBulkActionMenuPopup()) {
                    SimpleUtils.pass("Right Click on the Selected Shifts successfully!");
                    break;
                }
            }
        } else {
            SimpleUtils.fail("There is no selected shifts' index!", false);
        }
    }

    @Override
    public void rightClickOnSelectedShiftInDayView(int index) throws Exception {
        if (index >= 0) {
            List<WebElement> names = null;
            if (areListElementVisible(shiftOuterInDayView, 10)) {
                names = shiftOuterInDayView;
                Actions action = new Actions(getDriver());
                scrollToElement(names.get(index));
                waitForSeconds(1);
                action.contextClick(names.get(index)).build().perform();
                if (isBulkActionMenuPopup())
                    SimpleUtils.pass("Right Click on the Selected Shifts successfully!");
                else
                    SimpleUtils.fail("Right Click on the Selected Shifts not shown correctly!",false);
            }else if (areListElementVisible(shiftsWeekView,20)) {
                names = shiftsWeekView;
                Actions action = new Actions(getDriver());
                scrollToElement(names.get(index));
                waitForSeconds(1);
                action.contextClick(names.get(index)).build().perform();
                if (isBulkActionMenuPopup())
                    SimpleUtils.pass("Right Click on the Selected Shifts successfully!");
                else
                    SimpleUtils.fail("Right Click on the Selected Shifts not shown correctly!",false);
            }else
                SimpleUtils.fail("No any shifts in week view!", false);
        } else
            SimpleUtils.fail("There is no selected shifts' index!", false);
    }

    @Override
    public void verifyTheContentOnBulkActionMenu(int selectedShiftCount) throws Exception {
        if (isElementLoaded(bulkActionMenu, 5) && bulkActionMenu.getText().contains(String.valueOf(selectedShiftCount))
        && (bulkActionMenu.getText().contains("Shifts Selected") || bulkActionMenu.getText().contains("Shift Selected") ) && bulkActionMenu.getText().contains("Delete")) {
            SimpleUtils.pass("The content on bulk action menu is correct!");
        } else {
            SimpleUtils.fail("The content on bulk action menu is incorrect!", false);
        }
    }

    @Override
    public void clickOnBtnOnBulkActionMenuByText(String action) throws Exception {
        if (areListElementVisible(bulkActionBtns, 5)) {
            for (WebElement button : bulkActionBtns) {
                if (button.getText().trim().equalsIgnoreCase(action)) {
                    clickTheElement(button);
                    SimpleUtils.pass("Click on " + action + " button successfully on Bulk Action Menu!");
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Buttons failed to load on Bulk Action Menu!", false);
        }
    }

    @Override
    public void verifySelectedShiftsAreMarkedWithX(HashSet<Integer> selectedIndexes) throws Exception {
        boolean isMarkedX = true;
        if (selectedIndexes.size() > 0) {
            List<WebElement> currentShifts = null;
            if (areListElementVisible(dayViewShiftGroups, 10)) {
                currentShifts = dayViewShiftGroups;
            } else if (areListElementVisible(shiftsWeekView, 10)) {
                currentShifts = shiftsWeekView;
            }
            for (int i : selectedIndexes) {
                try {
                    if (!isElementLoaded(currentShifts.get(i).findElement(By.cssSelector("[src*=\"deleted-shift\"]")), 5)) {
                        isMarkedX = false;
                        break;
                    }
                } catch (Exception e) {
                    isMarkedX = false;
                    break;
                }
            }
            if (!isMarkedX) {
                SimpleUtils.fail("Some shifts are not marked as X!", false);
            }
        } else {
            SimpleUtils.fail("There is no selected shifts' index!", false);
        }
    }

    private boolean isBulkActionMenuPopup() throws Exception {
        if (isElementLoaded(bulkActionMenu, 5)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void bulkDeleteTMShiftsInWeekView(String teamMemberName) throws Exception {
        unSelectAllBulkSelectedShifts();
        if (areListElementVisible(shiftsWeekView, 15)) {
            HashSet<Integer> shiftIndexes = new HashSet<>();
            //Get all index of TM's shifts
            for (int i=0; i< shiftsWeekView.size(); i++) {
                    WebElement workerName = shiftsWeekView.get(i).findElement(By.cssSelector("[class=\"rows\"] .week-schedule-worker-name"));
                    if (workerName != null) {
                        if (workerName.getText().toLowerCase().trim().contains(teamMemberName.toLowerCase().trim())) {
                            shiftIndexes.add(i);
                            SimpleUtils.pass("Bulk delete: Get shift index :"+i+" successfully! ");
                        }
                    }
            }
            if (shiftIndexes.size()>0) {
                //Select the shifts
                Actions action = new Actions(getDriver());
                for (int i : shiftIndexes) {
                    scrollToElement(shiftsWeekView.get(i));
                    waitForSeconds(1);
                    action.keyDown(Keys.CONTROL).build().perform();
                    action.click(shiftsWeekView.get(i).findElement(By.cssSelector("[class=\"rows\"] .week-schedule-worker-name")));
                    action.keyUp(Keys.CONTROL).build().perform();
                }
                int selectedShiftCount = getDriver().findElements(By.cssSelector(".shift-selected-multi")).size();
                if (selectedShiftCount == shiftIndexes.size()){
                    SimpleUtils.pass("Bulk delete:Select shift successfully! ");
                }else
                    SimpleUtils.fail("Bulk delete: Fail to select shift! the expect count is:"+shiftIndexes.size()
                            + " the actual count is: "+selectedShiftCount, false);
                //Right click the selected shifts
                waitForSeconds(2);
                rightClickOnSelectedShifts(shiftIndexes);
                // Verify the Delete button on Bulk Action Menu is clickable
                clickOnBtnOnBulkActionMenuByText("Delete");
                waitForSeconds(3);
                // Verify the shifts are marked as X after clicking on Delete button
                verifySelectedShiftsAreMarkedWithX(shiftIndexes);
            } else
                SimpleUtils.report("There is no shift for :"+teamMemberName+" !");
        }else
            SimpleUtils.report("Schedule Week View: shifts load failed or there is no shift in this week");
    }

    @Override
    public List<WebElement> selectMultipleDifferentAssignmentShiftsOnOneDay(int shiftCount, int dayIndex) throws Exception {
        List<WebElement> shiftsOnOneDay = getDriver().findElements(By.cssSelector("[data-day-index=\"" + dayIndex + "\"] .week-schedule-shift"));
        List<WebElement> selectedShifts = new ArrayList<>();
        List<String> selectedShiftTMNames = new ArrayList<>();
//        scrollToBottom();
        waitForSeconds(2);
        if (shiftsOnOneDay.size() >= shiftCount) {
            Actions action = new Actions(getDriver());
            action.keyDown(Keys.CONTROL).build().perform();
            for (WebElement element : shiftsOnOneDay) {
                WebElement shiftName = element.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (!selectedShiftTMNames.contains(shiftName.getText())) {
                    action.click(element);
                    selectedShifts.add(element);
                    selectedShiftTMNames.add(shiftName.getText());
                    SimpleUtils.pass("Bulk action: Click "+shiftName.getText()+"'s shift successfully! ");
                }
                if (selectedShifts.size() == shiftCount) {
                    break;
                }
            }
            action.keyUp(Keys.CONTROL).build().perform();
            if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftCount) {
                SimpleUtils.pass("Selected " + shiftCount + " shifts successfully");
            } else {
                SimpleUtils.fail("Expected to select " + shiftCount + " shifts, but actually selected " +
                        getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
            }
        } else {
            SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
        }
        return selectedShifts;
    }

    @Override
    public List<WebElement> selectMultipleSameAssignmentShifts(int shiftCount, String tmName) throws Exception {
        List<WebElement> selectedShifts = new ArrayList<>();
//        List<String> selectedShiftTMNames = new ArrayList<>();
        List<WebElement> names = new ArrayList<>();
        if (areListElementVisible(namesWeekView, 10)) {
            names = namesWeekView;
        } else if (areListElementVisible(namesDayView, 10)) {
            names = namesDayView;
        }
        scrollToBottom();
        waitForSeconds(2);
        if (names.size() >= shiftCount) {
            Actions action = new Actions(getDriver());
            action.keyDown(Keys.CONTROL).build().perform();
            for (WebElement name : names) {
                if (name.getText().toLowerCase().contains(tmName.toLowerCase())) {
                    action.click(name);
                    selectedShifts.add(name);
                    SimpleUtils.pass("Bulk action: Click " + tmName + "'s shift successfully! ");
                }
                if (selectedShifts.size() == shiftCount) {
                    break;
                }
            }
            action.keyUp(Keys.CONTROL).build().perform();
            if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftCount) {
                SimpleUtils.pass("Selected " + shiftCount + " shifts successfully");
            } else {
                SimpleUtils.fail("Expected to select " + shiftCount + " shifts, but actually selected " +
                        getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
            }
        } else {
            SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
        }
        return selectedShifts;
    }

    public boolean isInfoIconLoaded(int index) throws Exception {
        boolean infoIconLoaded = false;
        try {
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement infoIcon = dayViewAvailableShifts.get(index).findElement(By.className("day-view-shift-right-top-icons"));
                if (isElementLoaded(infoIcon, 5)) {
                    infoIconLoaded = true;
                    SimpleUtils.report("The info icon displays!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            }
        }catch (Exception e) {
            SimpleUtils.report("The info icon doesn't display!");
        }finally{
            return infoIconLoaded;
        }
    }


    @Override
    public List<WebElement> selectMultipleDifferentAssignmentShifts(int shiftCount) throws Exception {
        List<WebElement> selectedShifts = new ArrayList<>();
        List<String> selectedShiftTMNames = new ArrayList<>();
        List<WebElement> elements = new ArrayList<>();
        if (areListElementVisible(weekShifts, 10)) {
            elements = weekShifts;
        } else if (areListElementVisible(dayViewAvailableShifts, 10)) {
            elements = dayViewAvailableShifts;
        }
        if (elements.size() >= shiftCount) {
            Actions action = new Actions(getDriver());
            action.keyDown(Keys.CONTROL).build().perform();
            for (WebElement element : elements) {
                WebElement shiftName = element.findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (!selectedShiftTMNames.contains(shiftName.getText())) {
                    scrollToElement(element);
                    waitForSeconds(1);
                    action.click(element);
                    selectedShifts.add(element);
                    selectedShiftTMNames.add(shiftName.getText());
                    SimpleUtils.pass("Bulk action: Click " + shiftName.getText() + "'s shift successfully! ");
                }
                if (selectedShifts.size() == shiftCount) {
                    break;
                }
            }
            action.keyUp(Keys.CONTROL).build().perform();
            if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftCount) {
                SimpleUtils.pass("Selected " + shiftCount + " shifts successfully");
            } else {
                SimpleUtils.fail("Expected to select " + shiftCount + " shifts, but actually selected " +
                        getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
            }
        }
        return selectedShifts;
    }

    @Override
    public void dragBulkShiftToAnotherDay(List<WebElement> selectedShifts, int endIndex, boolean needConfirmChangeModalDisplay) throws Exception {
//        waitForSeconds(3);
        List<WebElement> endElements = getDriver().findElements(By.cssSelector("[data-day-index=\"" + endIndex + "\"] .week-schedule-shift-wrapper"));
        WebElement weekDay = getDriver().findElement(By.cssSelector("[data-day-index=\""+endIndex+"\"] .sch-calendar-day-label"));
        if (selectedShifts != null
                && endElements != null
                && selectedShifts.size() > 0
//                && endElements.size() > 0
                && weekDay!=null) {
            Actions action = new Actions(getDriver());
            action.clickAndHold(selectedShifts.get(selectedShifts.size()-1)).build().perform();
            if (endElements.size() == 0) {
                scrollToElement(daySummaries.get(endIndex+7));
                waitForSeconds(1);
                action.moveToElement(daySummaries.get(endIndex+7));
            }else {
                scrollToElement(endElements.get(endElements.size()-1));
                waitForSeconds(1);
                action.moveToElement(endElements.get(endElements.size()-1));
            }
            action.release().build().perform();

//            scrollToElement(endElements.get(endElements.size()-1));
//            waitForSeconds(1);
//            mouseHoverDragandDrop(selectedShifts.get(selectedShifts.size()-1), endElements.get(endElements.size()-1));
            if (needConfirmChangeModalDisplay) {
                if (!checkIfBulkDragAndDropConfirmChangeModalDisplay()) {
                    SimpleUtils.fail("Bulk Drag&Drop: Bulk drag and drop confirm change modal should display!", false);
                }else
                    SimpleUtils.pass("Bulk Drag&Drop: Drag multiple shifts to " + weekDay.getText() + " days Successfully!");
            }else {
                if (checkIfBulkDragAndDropConfirmChangeModalDisplay()) {
                    SimpleUtils.fail("Bulk Drag&Drop: Bulk drag and drop confirm change modal should not display!", false);
                }else
                    SimpleUtils.pass("Bulk Drag&Drop: Bulk drag and drop confirm change modal not display!");
            }
        } else {
            SimpleUtils.fail("Schedule Page: Failed to find the shifts or " + endIndex, false);
        }
    }

    @FindBy(css = ".swap-modal.modal-instance")
    private WebElement bulkDragAndDropConfirmChangeModal;
    public boolean checkIfBulkDragAndDropConfirmChangeModalDisplay() throws Exception {
        boolean ifModalDisplay = false;
        if (isElementLoaded(bulkDragAndDropConfirmChangeModal, 5)) {
            ifModalDisplay = true;
            SimpleUtils.report("Bulk action: Bulk drag and drop confirm change modal display successfully! ");
        } else
            SimpleUtils.report("Bulk action: Bulk drag and drop confirm change modal is not display! ");
        return ifModalDisplay;
    }

    @FindBy(css = "p.text-regular")
    private WebElement bulkDragAndDropConfirmChangeInfo;
    public String getBulkDragAndDropConfirmChangeInfo () throws Exception {
        String message = "";
        if (isElementLoaded(bulkDragAndDropConfirmChangeInfo, 5)) {
            message = bulkDragAndDropConfirmChangeInfo.getText();
            SimpleUtils.pass("Bulk action: Get bulk drag and drop confirm change info successfully!");
        } else
            SimpleUtils.fail("Bulk action: The bulk drag and drop confirm change info fail to load! ", false);
        return message;
    }

    @FindBy(css = "[ng-click=\"config.mode = 'move'\"] .tma-staffing-option-outer-circle")
    private WebElement moveShiftsRadioButton;
    @FindBy(css = "[ng-click=\"config.mode = 'copy'\"] .tma-staffing-option-outer-circle")
    private WebElement copyShiftsRadioButton;
    public void selectMoveOrCopyBulkShifts (String moveOrCopy) throws Exception {
        if (isElementLoaded(moveShiftsRadioButton, 5)
                && isElementLoaded(copyShiftsRadioButton, 5)) {
            if (moveOrCopy.equalsIgnoreCase("move")) {
                if (moveShiftsRadioButton.findElement(By.tagName("div")).getAttribute("class").contains("ng-hide")) {
                    click(moveShiftsRadioButton);
                    if (!moveShiftsRadioButton.findElement(By.tagName("div")).getAttribute("class").contains("ng-hide")) {
                        SimpleUtils.pass("Bulk action: Select move shifts radio button successfully!");
                    } else
                        SimpleUtils.fail("Bulk action: Fail to select move shift radio button! ", false);
                } else
                    SimpleUtils.pass("Bulk action: Move shifts radio button already been selected!");
            }else {
                if (copyShiftsRadioButton.findElement(By.tagName("div")).getAttribute("class").contains("ng-hide")) {
                    click(copyShiftsRadioButton);
                    if (!copyShiftsRadioButton.findElement(By.tagName("div")).getAttribute("class").contains("ng-hide")) {
                        SimpleUtils.pass("Bulk action: Select copy shifts radio button successfully!");
                    } else
                        SimpleUtils.fail("Bulk action: Fail to select copy shift radio button! ", false);
                } else
                    SimpleUtils.pass("Bulk action: Copy shifts radio button already been selected!");
            }
        }else
            SimpleUtils.fail("Bulk action: The move and copy shifts radio buttons fail to load! ", false);
    }

    @FindBy(css = "[value=\"config.allowComplianceErrors\"]")
    private WebElement allowComplianceErrorSwitch;
    @FindBy(css = "[value=\"config.allowConvertToOpen\"]")
    private WebElement allowConvertToOpenSwitch;
    public void enableOrDisableAllowComplianceErrorSwitch (boolean enableOrDisable) throws Exception {
        if (isElementLoaded(allowComplianceErrorSwitch, 10)) {
            if (enableOrDisable) {
                if (allowComplianceErrorSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")) {
                    click(allowComplianceErrorSwitch);
                    if (allowComplianceErrorSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
                        SimpleUtils.pass("Bulk action: Enable Allow Compliance Error switch successfully!");
                    } else
                        SimpleUtils.fail("Bulk action: Fail to Enable Allow Compliance Error switch! ", false);
                } else
                    SimpleUtils.pass("Bulk action: Allow Compliance Error switch already been enabled!");
            }else {
                if (allowComplianceErrorSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
                    click(allowComplianceErrorSwitch);
                    if (allowComplianceErrorSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")) {
                        SimpleUtils.pass("Bulk action: Disable Allow Compliance Error switch successfully!");
                    } else
                        SimpleUtils.fail("Bulk action: Fail to disable Allow Compliance Error switch! ", false);
                } else
                    SimpleUtils.pass("Bulk action: Allow Compliance Error switch already been disable!");
            }
        }else
            SimpleUtils.fail("Bulk action: The Allow Compliance Error switch fail to load! ", false);
    }


    public void enableOrDisableAllowConvertToOpenSwitch (boolean enableOrDisable) throws Exception {
        if (isElementLoaded(allowConvertToOpenSwitch, 5)) {
            if (enableOrDisable) {
                if (allowConvertToOpenSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")) {
                    click(allowConvertToOpenSwitch);
                    if (allowConvertToOpenSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
                        SimpleUtils.pass("Bulk action: Enable Allow Convert to Open switch successfully!");
                    } else
                        SimpleUtils.fail("Bulk action: Fail to Enable Allow Convert to Open switch! ", false);
                } else
                    SimpleUtils.pass("Bulk action: Enable Allow Convert to Open switch already been enabled!");
            }else {
                if (allowConvertToOpenSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-not-empty")) {
                    click(allowConvertToOpenSwitch);
                    if (allowConvertToOpenSwitch.findElement(By.tagName("input")).getAttribute("class").contains("ng-empty")) {
                        SimpleUtils.pass("Bulk action: Disable Enable Allow Convert to Open switch successfully!");
                    } else
                        SimpleUtils.fail("Bulk action: Fail to disable Enable Allow Convert to Open switch! ", false);
                } else
                    SimpleUtils.pass("Bulk action: Enable Allow Convert to Open switch already been disable!");
            }
        }else
            SimpleUtils.fail("Bulk action: The Enable Allow Convert to Open switch fail to load! ", false);
    }

    @Override
    public void expandSpecificCountGroup(int count) throws Exception {
        if (areListElementVisible(groupTitleList,10)){
            if (count> groupTitleList.size()) {
                SimpleUtils.fail("The count "+count+" more than the group by lists: "+ groupTitleList.size(), false);
            } else {
                int expendedGroup = 0;
                for (int i=0; i< groupTitleList.size(); i++){
                    if (expendedGroup != count){
                        clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                        if (!getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
                            expendedGroup +=1;
                            SimpleUtils.pass("Group is expanded!");
                        } else {
                            clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                            if (!getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
                                expendedGroup +=1;
                                SimpleUtils.pass("Group is expanded!");
                            } else {
                                SimpleUtils.fail("Group is not able to be expanded!", false);
                            }
                        }
                    }else {
                        clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                        if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
                            SimpleUtils.pass("Group is collapsed!");
                        } else {
                            clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                            if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("closed")){
                                SimpleUtils.pass("Group is collapsed!");
                            } else {
                                SimpleUtils.fail("Group is not able to be collapsed!", false);
                            }
                        }
                    }
                }
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
    }


    @Override
    public int getOneDayShiftCountByIndex(int index) throws Exception {
        int count = 0;
        if (index < 7) {
            List<WebElement> oneDayShifts = getDriver().findElements(By.cssSelector("[data-day-index=\"" + index + "\"] .week-schedule-shift-wrapper"));
            count = oneDayShifts.size();
        } else
            SimpleUtils.fail("Index cannot greater than 7, but actual it is:" + index, false);

        return count;
    }
    public boolean isProfileIconLoaded(int index) throws Exception {
        boolean profileIconLoaded = false;
        try{
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement profileIcon = dayViewAvailableShifts.get(index).findElement(By.cssSelector(".sch-day-view-shift-worker-detail.ng-scope.sch-shift-orientation-right.sch-shift-worker-img-cursor"));
                if(isElementLoaded(profileIcon)) {
                    profileIconLoaded = true;
                    SimpleUtils.report("The profile icon displays!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            }
        }catch(Exception e){
            SimpleUtils.report("The profile icon doesn't display!");
        }finally{
            return profileIconLoaded;
        }
    }

    @Override
    public boolean isShiftLengthLoaded(int index) throws Exception {
        boolean shiftLengthLoaded = false;
        try {
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement shiftLength = dayViewAvailableShifts.get(index).findElement(By.cssSelector(".sch-day-view-worker-time.ng-binding"));
                if (isElementLoaded(shiftLength)) {
                    shiftLengthLoaded = true;
                    SimpleUtils.report("The shift length displays!");
                }
            }else {
                SimpleUtils.fail("The shifts in DayView are not loaded correctly!", false);
            }
        }catch (Exception e) {
            SimpleUtils.report("The shift length doesn't display!");
        }finally {
            return shiftLengthLoaded;
        }
    }

    @Override
    public boolean isShiftDurationInBoxLoaded(int index) throws Exception {
        boolean shiftDurationInBoxLoaded = false;
        try {
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement shiftDuration = dayViewAvailableShifts.get(index).findElement(By.cssSelector("[class=\"sch-day-view-shift-time pt-5 mr-10\"]"));
                if (isElementLoaded(shiftDuration)) {
                    shiftDurationInBoxLoaded = true;
                    SimpleUtils.report("The shift duration displays!");
                }
            }else{
                SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
            }
        } catch (Exception e) {
            SimpleUtils.report("The shift duration doesn't display!");
        }finally {
            return shiftDurationInBoxLoaded;
        }
    }

    @Override
    public boolean isShiftTotalLengthLoaded(int index) throws Exception {
        boolean shiftTotalLengthLoaded = false;
        try {
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement shiftTotalLength = dayViewAvailableShifts.get(index).findElement(By.cssSelector(".sch-day-view-worker-time.ng-binding.ng-scope"));
                if (isElementLoaded(shiftTotalLength)) {
                    shiftTotalLengthLoaded = true;
                    SimpleUtils.report("The shift total length displays!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            }
        }catch(Exception e){
            SimpleUtils.report("The shift total length doesn't display!");
        }finally {
            return shiftTotalLengthLoaded;
        }
    }

    @Override
    public boolean isProfileNameAndWorkRoleLoaded(int index) throws Exception {
        boolean profileNameAndWorkRoleLoaded = false;
        try {
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement profileNameAndWorkRole = dayViewAvailableShifts.get(index).findElement(By.cssSelector(".sch-day-view-shift-worker-name.row-fx.ng-scope"));
                if (isElementLoaded(profileNameAndWorkRole)) {
                    profileNameAndWorkRoleLoaded = true;
                    SimpleUtils.report("The profile name and work role display!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            }
        }catch(Exception e) {
            SimpleUtils.report("The profile name and work role don't display!");
        }finally{
            return profileNameAndWorkRoleLoaded ;
        }
    }

    @Override
    public boolean isMyScheduleProfileNameLoaded(int index) throws Exception {
        boolean profileNameAndWorkRoleLoaded = false;
        try {
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement profileNameAndWorkRole = dayViewAvailableShifts.get(index).findElement(By.className("sch-day-view-shift-worker-name-status"));
                if (isElementLoaded(profileNameAndWorkRole)) {
                    profileNameAndWorkRoleLoaded = true;
                    SimpleUtils.report("The profile name and work role display!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            }
        }catch(Exception e) {
            SimpleUtils.report("The profile name and work role don't display!");
        }finally{
            return profileNameAndWorkRoleLoaded ;
        }
    }

    @Override
    public boolean isShiftJobTitleLoaded(int index) throws Exception {
        boolean shiftJobTitleLoaded = false;
        try{
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement shiftJobTitle = dayViewAvailableShifts.get(index).findElement(By.cssSelector(".sch-day-view-shift-worker-title-role.ng-binding"));
                if (isElementLoaded(shiftJobTitle)) {
                    shiftJobTitleLoaded = true;
                    SimpleUtils.report("The job title displays!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            } }
        catch (Exception e) {
            SimpleUtils.report("The job title doesn't display!");
        }finally {
            return shiftJobTitleLoaded;
        }
    }

    @Override
    public boolean isMyScheduleShiftLocationLoaded(int index) throws Exception {
        boolean myScheduleShiftLocationLoaded = false;
        try{
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement shiftJobTitle = dayViewAvailableShifts.get(index).findElement(By.cssSelector(".sch-day-view-shift-location.ng-binding.ng-scope"));
                if (isElementLoaded(shiftJobTitle)) {
                    myScheduleShiftLocationLoaded = true;
                    SimpleUtils.report("The location displays!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            } }
        catch (Exception e) {
            SimpleUtils.report("The location doesn't display!");
        }finally {
            return myScheduleShiftLocationLoaded;
        }
    }

    @Override
    public boolean isMyScheduleShiftWorkRoleLoaded(int index) throws Exception {
        boolean shiftJobTitleLoaded = false;
        try{
            if (areListElementVisible(dayViewAvailableShifts, 20) && index < dayViewAvailableShifts.size()) {
                WebElement shiftJobTitle = dayViewAvailableShifts.get(index).findElement(By.cssSelector("[class=\"sch-day-view-shift-worker-name-status\"] [class=\"sch-day-view-shift-worker-title-role ng-binding\"]"));
                if (isElementLoaded(shiftJobTitle)) {
                    shiftJobTitleLoaded = true;
                    SimpleUtils.report("The job title displays!");
                }else{
                    SimpleUtils.fail("The shifts in DayView are not loaded correctly!",false);
                }
            } }
        catch (Exception e) {
            SimpleUtils.report("The job title doesn't display!");
        }finally {
            return shiftJobTitleLoaded;
        }
    }

//    @Override
//    public boolean isShiftDurationInAvailabilityLoaded(int index) throws Exception {
//        boolean shiftDurationInAvailablityLoaded = false;
//        try{
//            if (areListElementVisible(viewProfileAvailableShifts, 20) && index < viewProfileAvailableShifts.size()) {
//                WebElement shiftJobTitle = viewProfileAvailableShifts.get(index).findElement(By.cssSelector("[class=\"availability-shift ng-scope Standard\"] [class=\"ng-binding ng-scope\"]"));
//                if (isElementLoaded(shiftJobTitle)) {
//                    shiftDurationInAvailablityLoaded = true;
//                    SimpleUtils.report("The shift duration in Availability displays!");
//                }else{
//                    SimpleUtils.fail("The shifts in Availability are not loaded correctly!",false);
//                }
//            } }
//        catch (Exception e) {
//            SimpleUtils.report("The shift duration in Availability doesn't display!");
//        }finally {
//            return shiftDurationInAvailablityLoaded;
//        }
//    }

    @FindBy(css = ".availability-shift.ng-scope.Standard")
    private List<WebElement> viewProfileAvailableShifts;
    @Override
    public boolean isShiftJobTitleMediumInAvailabilityLoaded(int index) throws Exception {
        boolean shiftJobTitleInAvailabilityLoaded = false;
        try{
            if (areListElementVisible(viewProfileAvailableShifts, 20) && index < viewProfileAvailableShifts.size()) {
                WebElement shiftJobTitle = viewProfileAvailableShifts.get(index).findElement(By.cssSelector(".left.shift-name.ng-binding.ng-scope"));
                if (isElementLoaded(shiftJobTitle)) {
                    shiftJobTitleInAvailabilityLoaded = true;
                    SimpleUtils.report("The shift job title in Availability displays!");
                }else{
                    SimpleUtils.fail("The shifts in Availability are not loaded correctly!",false);
                }
            } }
        catch (Exception e) {
            SimpleUtils.report("The shift job title in Availability doesn't display!");
        }finally {
            return shiftJobTitleInAvailabilityLoaded;
        }
    }

    @Override
    public boolean isShiftJobTitleLargeInAvailabilityLoaded(int index) throws Exception {
        boolean shiftJobTitleInAvailabilityLoaded = false;
        try{
            if (areListElementVisible(viewProfileAvailableShifts, 20) && index < viewProfileAvailableShifts.size()) {
                WebElement shiftJobTitle = viewProfileAvailableShifts.get(index).findElement(By.cssSelector(".left.shift-name.ng-binding.ng-scope.large"));
                if (isElementLoaded(shiftJobTitle)) {
                    shiftJobTitleInAvailabilityLoaded = true;
                    SimpleUtils.report("The shift job title in Availability displays!");
                }else{
                    SimpleUtils.fail("The shifts in Availability are not loaded correctly!",false);
                }
            } }
        catch (Exception e) {
            SimpleUtils.report("The shift job title in Availability doesn't display!");
        }finally {
            return shiftJobTitleInAvailabilityLoaded;
        }
    }

    @Override
    public boolean isShiftLocationInAvailabilityLoaded(int index) throws Exception {
        boolean shiftLocationInAvailabilityLoaded = false;
        try{
            if (areListElementVisible(viewProfileAvailableShifts, 20) && index < viewProfileAvailableShifts.size()) {
                WebElement shiftJobTitle = viewProfileAvailableShifts.get(index).findElement(By.cssSelector(".business-location.ng-binding.ng-scope"));
                if (isElementLoaded(shiftJobTitle)) {
                    shiftLocationInAvailabilityLoaded = true;
                    SimpleUtils.report("The shift location in Availability displays!");
                }else{
                    SimpleUtils.fail("The shifts in Availability are not loaded correctly!",false);
                }
            } }
        catch (Exception e) {
            SimpleUtils.report("The shift location in Availability doesn't display!");
        }finally {
            return shiftLocationInAvailabilityLoaded;
        }
    }

    @Override
    public boolean isShiftInfoIconInAvailabilityLoaded(int index) throws Exception {
        boolean shiftInfoIconInAvailabilityLoaded = false;
        try{
            if (areListElementVisible(viewProfileAvailableShifts, 20) && index < viewProfileAvailableShifts.size()) {
                WebElement shiftJobTitle = viewProfileAvailableShifts.get(index).findElement(By.cssSelector(""));
                if (isElementLoaded(shiftJobTitle)) {
                    shiftInfoIconInAvailabilityLoaded = true;
                    SimpleUtils.report("The shift info icon in Availability displays!");
                }else{
                    SimpleUtils.fail("The shifts in Availability are not loaded correctly!",false);
                }
            } }
        catch (Exception e) {
            SimpleUtils.report("The shift info icon in Availability doesn't display!");
        }finally {
            return shiftInfoIconInAvailabilityLoaded;
        }
    }


    @FindBy(css = ".shift-selected-multi")
    private List<WebElement> bulkSelectedShift;
    public void unSelectAllBulkSelectedShifts(){
        if (areListElementVisible(bulkSelectedShift, 5)) {
            Actions action = new Actions(getDriver());
            action.keyDown(Keys.CONTROL).build().perform();
            for (WebElement element : bulkSelectedShift) {
                action.click(element);
                SimpleUtils.pass("Bulk action: Unselect one shift successfully! ");
            }
            action.keyUp(Keys.CONTROL).build().perform();
        } else
            SimpleUtils.report("There is no bulk selected shifts! ");
    }

    @Override
    public void verifyGroupCannotbeCollapsedNExpanded() throws Exception {
        if (areListElementVisible(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")),10)){
            for (int i=0; i< getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).size(); i++){
                clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                if (getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("week-schedule-ribbon-group-toggle-closed")){
                    clickTheElement(getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i));
                    if (!getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle")).get(i).getAttribute("class").contains("week-schedule-ribbon-group-toggle-closed")){
                        SimpleUtils.fail("Group should not expanded!", false);
                    } else {
                        SimpleUtils.pass("Group cannot be expanded!!");
                    }
                } else
                    SimpleUtils.fail("Group should not expanded!", false);
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
    }

    public String getSpecificGroupByChildLocationStatus(String childLocation) {
        String status = "";
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
        List<WebElement> groupByLocationItems = getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle"));
        if (areListElementVisible(groupByLocationItems,10)){
            for (WebElement groupByLocationItem : groupByLocationItems) {
                String locationName = groupByLocationItem.findElement(By.cssSelector(".week-schedule-shift-title")).getText();
                if (locationName.equalsIgnoreCase(childLocation)) {
                    status = groupByLocationItem
                            .findElement(By.cssSelector(".week-schedule-ribbon-right-schedule-status")).getText();
                    SimpleUtils.pass("Get " + childLocation + "'s status: " + status + " successfully! ");
                    break;
                }
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
        return status;
    }


    public String clickActionIconForSpecificGroupByChildLocation(String childLocation) {
        String status = "";
        ScheduleMainPage scheduleMainPage = new ConsoleScheduleMainPage();
        scheduleMainPage.selectGroupByFilter(GroupByDayPartsTest.scheduleGroupByFilterOptions.groupbyLocation.getValue());
        List<WebElement> groupByLocationItems = getDriver().findElements(By.cssSelector(".week-schedule-ribbon-group-toggle"));
        if (areListElementVisible(groupByLocationItems,10)){
            for (WebElement groupByLocationItem : groupByLocationItems) {
                String locationName = groupByLocationItem.findElement(By.cssSelector(".week-schedule-shift-title")).getText();
                if (locationName.equalsIgnoreCase(childLocation)) {
                    clickTheElement(groupByLocationItem.findElement(By.cssSelector("[role=\"button\"]")));
                    SimpleUtils.pass("Click " + childLocation + "'s action icon successfully! ");
                    break;
                }
            }
        } else {
            SimpleUtils.fail("No group title show up!", false);
        }
        return status;
    }

    @FindBy(css = "[role=\"menu\"]")
    private WebElement groupByActionPopup;
    public void clickOnSpecificButtonsGroupByActionPopup(String buttonName) throws Exception {
        if (isElementLoaded(groupByActionPopup, 3)){
            List<WebElement> buttonsOnPopUp = groupByActionPopup.findElements(By.cssSelector("li.MuiListItem-button"));
            boolean isButtonExist = false;
            for (WebElement button: buttonsOnPopUp){
                if (button.getText().equalsIgnoreCase(buttonName)){
                    isButtonExist = true;
                    clickTheElement(button);
                    SimpleUtils.pass("Group by location: Click the "+buttonName+" button on action popup successfully! ");
                    break;
                }
            }
            if (!isButtonExist) {
                SimpleUtils.fail("Group by location: The button "+buttonName+ " is not exist on action popup! ", false);
            }
        } else
            SimpleUtils.fail("Group by Location: The action popup fail to load!! ", false);
    }


    public List<String> getButtonNamesFromGroupByActionPopup() throws Exception {
        List<String> buttonNames = new ArrayList<>();
        if (isElementLoaded(groupByActionPopup, 3)){
            List<WebElement> buttonsOnPopUp = groupByActionPopup.findElements(By.cssSelector("li.MuiListItem-button"));
            for (WebElement button: buttonsOnPopUp){
                buttonNames.add(button.getText());
                SimpleUtils.pass("Get button : "+ button.getText()+" successfully! ");
            }
        } else
            SimpleUtils.fail("Group by Location: The action popup fail to load!! ", false);
        return buttonNames;
    }

    @FindBy(css = "[ng-class=\"hideItem('staffing.guidance')\"]")
    private WebElement staffSmartCard;
    @Override
    public String getTotalBudgetFromSTAFFSmartCard() throws Exception {
        String totalBudgetFromSTAFFSmartCard = null;
        if (isElementLoaded(staffSmartCard,3)) {
            if (areListElementVisible(budgetedHoursOnSTAFF,3)) {
                totalBudgetFromSTAFFSmartCard = budgetedHoursOnSTAFF.get(budgetedHoursOnSTAFF.size()-1).getText();
            } else {
                SimpleUtils.fail("The budgets are not loaded correctly on the STAFF Card!", false);
            }
        } else {
            SimpleUtils.fail("The STAFF Card is not loaded correctly!", false);
        }
        return totalBudgetFromSTAFFSmartCard;
    }

    @Override
    public boolean isScheduleTableDisplay () throws Exception {
        boolean isScheduleTableDisplay = false;
        if (isElementLoaded(scheduleTable,10)) {
            isScheduleTableDisplay = true;
            SimpleUtils.report("The schedule table is display correctly! ");
        } else {
            SimpleUtils.report("The schedule table is not display! ");
        }
        return isScheduleTableDisplay;
    }

    @Override
    public void verifyConfirmBtnIsDisabledOnDragAndDropConfirmPage() throws Exception {
        waitForSeconds(3);
        if (isElementLoaded(confirmBtnOnDragAndDropConfirmPage,15)
                && confirmBtnOnDragAndDropConfirmPage.getText().equalsIgnoreCase("Confirm")
                && confirmBtnOnDragAndDropConfirmPage.getAttribute("class").contains("disabled")){
            SimpleUtils.pass("Confirm button label display correctly and disabled!");
        } else {
            SimpleUtils.fail("Confirm button display incorrectly!",false);
        }
    }

    @Override
    public void verifySwapBtnIsEnabledOnDragAndDropConfirmPage() throws Exception {
        waitForSeconds(3);
        if (isElementLoaded(confirmBtnOnDragAndDropConfirmPage,15)
                && confirmBtnOnDragAndDropConfirmPage.getText().equalsIgnoreCase("Swap")
                && !confirmBtnOnDragAndDropConfirmPage.getAttribute("class").contains("disabled")){
            SimpleUtils.pass("Swap button label display correctly and disabled!");
        } else {
            SimpleUtils.fail("Swap button display incorrectly!",false);
        }
    }


    @Override
    public void verifyAssignBtnIsEnabledOnDragAndDropConfirmPage() throws Exception {
        waitForSeconds(3);
        if (isElementLoaded(confirmBtnOnDragAndDropConfirmPage,15)
                && confirmBtnOnDragAndDropConfirmPage.getText().equalsIgnoreCase("Assign")
                && !confirmBtnOnDragAndDropConfirmPage.getAttribute("class").contains("disabled")){
            SimpleUtils.pass("Assign button label display correctly and disabled!");
        } else {
            SimpleUtils.fail("Assign button display incorrectly!",false);
        }
    }

    @Override
    public void bulkEditTMShiftsInWeekView(String teamMemberName) throws Exception {
        unSelectAllBulkSelectedShifts();
        if (areListElementVisible(shiftsWeekView, 15)) {
            HashSet<Integer> shiftIndexes = new HashSet<>();
            //Get all index of TM's shifts
            for (int i=0; i< shiftsWeekView.size(); i++) {
                WebElement workerName = shiftsWeekView.get(i).findElement(By.cssSelector(".rows .week-schedule-worker-name"));
                if (workerName != null) {
                    if (workerName.getText().toLowerCase().trim().contains(teamMemberName.toLowerCase().trim())) {
                        shiftIndexes.add(i);
                        SimpleUtils.pass("Bulk edit: Get shift index :"+i+" successfully! ");
                    }
                }
            }
            if (shiftIndexes.size()>0) {
                //Select the shifts
                Actions action = new Actions(getDriver());
                for (int i : shiftIndexes) {
                    scrollToElement(shiftsWeekView.get(i));
                    waitForSeconds(1);
                    action.keyDown(Keys.CONTROL).build().perform();
                    action.click(shiftsWeekView.get(i).findElement(By.cssSelector(".rows .week-schedule-worker-name")));
                    action.keyUp(Keys.CONTROL).build().perform();
                }
                int selectedShiftCount = getDriver().findElements(By.cssSelector(".shift-selected-multi")).size();
                if (selectedShiftCount == shiftIndexes.size()){
                    SimpleUtils.pass("Bulk edit:Select shift successfully! ");
                }else
                    SimpleUtils.fail("Bulk edit: Fail to select shift! the expect count is:"+shiftIndexes.size()
                            + " the actual count is: "+selectedShiftCount, false);
                //Right click the selected shifts
                waitForSeconds(2);
                rightClickOnSelectedShifts(shiftIndexes);
                // Verify the Delete button on Bulk Action Menu is clickable
                clickOnBtnOnBulkActionMenuByText("Edit");
                waitForSeconds(3);
                // Verify the shifts are marked as X after clicking on Delete button
                ConsoleEditShiftPage consoleEditShiftPage = new ConsoleEditShiftPage();
                consoleEditShiftPage.verifyTheContentOfOptionsSection();
            } else
                SimpleUtils.report("There is no shift for :"+teamMemberName+" !");
        }else
            SimpleUtils.report("Schedule Week View: shifts load failed or there is no shift in this week");
    }


    @Override
    public List<WebElement> selectMultipleSameAssignmentShiftsOnOneDay(int shiftCount, String tmName, int dayIndex) throws Exception {
        List<WebElement> selectedShifts = new ArrayList<>();
//        List<String> selectedShiftTMNames = new ArrayList<>();
//        List<WebElement> names = new ArrayList<>();
//        if (areListElementVisible(namesWeekView, 10)) {
//            names = namesWeekView;
//        } else if (areListElementVisible(namesDayView, 10)) {
//            names = namesDayView;
//        }
        List<WebElement> names = getDriver().findElements(By.cssSelector("[id=\"schedule-grid-react-wrapper\"] [data-day-index=\"" + dayIndex + "\"] .rows .week-schedule-worker-name"));
        scrollToBottom();
        waitForSeconds(2);
        if (names.size() >= shiftCount) {
            Actions action = new Actions(getDriver());
            action.keyDown(Keys.CONTROL).build().perform();
            for (WebElement name : names) {
                if (name.getText().toLowerCase().contains(tmName.toLowerCase())) {
                    action.click(name);
                    selectedShifts.add(name);
                    SimpleUtils.pass("Bulk action: Click " + tmName + "'s shift successfully! ");
                }
                if (selectedShifts.size() == shiftCount) {
                    break;
                }
            }
            action.keyUp(Keys.CONTROL).build().perform();
            if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftCount) {
                SimpleUtils.pass("Selected " + shiftCount + " shifts successfully");
            } else {
                SimpleUtils.fail("Expected to select " + shiftCount + " shifts, but actually selected " +
                        getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
            }
        } else {
            SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
        }
        return selectedShifts;
    }


    @Override
    public HashSet<Integer> verifyCanSelectMultipleShiftsOnOneDay(int shiftCount, int dayIndex) throws Exception {
        skipTheNewFeatureDialog();
        HashSet<Integer> set = new HashSet<>();
//        List<WebElement> names = null;
//        if (areListElementVisible(namesWeekView, 10)) {
//            names = namesWeekView;
//        } else if (areListElementVisible(shiftOuterInDayView, 10)) {
//            names = shiftOuterInDayView;
//        }
        List<WebElement> names = getDriver().findElements(By.cssSelector("[id=\"schedule-grid-react-wrapper\"] [data-day-index=\"" + dayIndex + "\"] .rows .week-schedule-worker-name"));
        if (names.size() >= shiftCount) {
            SimpleUtils.randomSet(0, names.size() - 1, shiftCount, set);
            Actions action = new Actions(getDriver());
            action.keyDown(Keys.CONTROL).build().perform();
            for (int i : set) {
                scrollToBottom();
                waitForSeconds(1);
                action.moveToElement(names.get(i)).click(names.get(i));
                waitForSeconds(1);
            }
            action.keyUp(Keys.CONTROL).build().perform();
            if (getDriver().findElements(By.cssSelector(".shift-selected-multi")).size() == shiftCount) {
                SimpleUtils.pass("Selected " + shiftCount + " shifts successfully");
            } else {
                SimpleUtils.fail("Expected to select " + shiftCount + " shifts, but actually selected " +
                        getDriver().findElements(By.cssSelector("shift-selected-multi")).size() + " shifts!", false);
            }
        } else {
            SimpleUtils.fail("Selected number is larger than the shifts' count!", false);
        }
        return set;
    }

}