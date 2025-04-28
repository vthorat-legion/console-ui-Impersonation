package com.legion.pages.core.schedule;

import com.legion.pages.BasePage;
import com.legion.pages.ScheduleCommonPage;
import com.legion.pages.SmartCardPage;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleSmartCardPage extends BasePage implements SmartCardPage {
    public ConsoleSmartCardPage() {
        PageFactory.initElements(getDriver(), this);
    }
    private static HashMap<String, String> parametersMap2 = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ControlsPageLocationDetail.json");

    @FindBy(css = "div.card-carousel-card")
    private List<WebElement> carouselCards;

    @FindBy(css = "div.card-carousel-arrow.card-carousel-arrow-left")
    private WebElement smartcardArrowLeft;

    @FindBy(css = "div.card-carousel-arrow.card-carousel-arrow-right")
    private WebElement smartcardArrowRight;
    @Override
    public HashMap<String, Float> getScheduleLabelHoursAndWages() throws Exception {
        HashMap<String, Float> scheduleHoursAndWages = new HashMap<String, Float>();
        WebElement budgetedScheduledLabelsDivElement = MyThreadLocal.getDriver().findElement(By.cssSelector("[ng-if*=\"isTitleBasedBudget()\"] .card-carousel-card"));
        if(isElementEnabled(budgetedScheduledLabelsDivElement,5))
        {
//			Thread.sleep(2000);
            String scheduleWagesAndHoursCardText = budgetedScheduledLabelsDivElement.getText();
            String [] tmp =  scheduleWagesAndHoursCardText.split("\n");
            String[] scheduleWagesAndHours = new String[6];
            if (tmp.length>6) {
                if(scheduleWagesAndHoursCardText.contains("Non-Budgeted")){
                    scheduleWagesAndHours[0] = tmp[0];
                    scheduleWagesAndHours[1] = tmp[1];
                    scheduleWagesAndHours[2] = tmp[2];
                    scheduleWagesAndHours[3] = tmp[3];
                    scheduleWagesAndHours[4] = tmp[4]+" "+tmp[5]+" "+tmp[6];
                    scheduleWagesAndHours[5] = tmp[7];
                } else {
                    scheduleWagesAndHours[0] = tmp[0];
                    scheduleWagesAndHours[1] = tmp[1];
                    scheduleWagesAndHours[2] = tmp[2];
                    scheduleWagesAndHours[3] = tmp[3]+" "+tmp[4]+" "+tmp[5];
                    scheduleWagesAndHours[4] = tmp[6];
                }

            }else
                scheduleWagesAndHours =tmp;
            for(int i = 0; i < scheduleWagesAndHours.length; i++)
            {
                if(scheduleWagesAndHours[i].toLowerCase().contains(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.hours.getValue().toLowerCase()))
                {
                    if (scheduleWagesAndHours[i].split(" ").length == 4) {
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[1],
                                ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.budgetedHours.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[2],
                                ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.scheduledHours.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[3],
                                ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.otherHours.getValue());
                    } else {
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i].split(" ")[1],
                                ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.budgetedHours.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 1],
                                ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.scheduledHours.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 2],
                                ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.otherHours.getValue());
                    }
                    break;
                }
                else if(scheduleWagesAndHours[i].toLowerCase().contains(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.wages.getValue().toLowerCase()))
                {
                    if (scheduleWagesAndHours[i].split(" ").length == 4) {
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[1]
                                .replace("$", ""), ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.budgetedWages.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[2]
                                .replace("$", ""), ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.scheduledWages.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages , scheduleWagesAndHours[i].split(" ")[3]
                                .replace("$", ""), ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.otherWages.getValue());
                    } else {
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i].split(" ")[1]
                                .replace("$", ""), ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.budgetedWages.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 1]
                                .replace("$", ""), ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.scheduledWages.getValue());
                        scheduleHoursAndWages = updateScheduleHoursAndWages(scheduleHoursAndWages, scheduleWagesAndHours[i + 1]
                                .replace("$", ""), ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.otherWages.getValue());
                    }
                    break;
                }
            }
        }
        return scheduleHoursAndWages;
    }

    public HashMap<String, Float> updateScheduleHoursAndWages(HashMap<String, Float> scheduleHoursAndWages,
                                                                     String hours, String hoursAndWagesKey) {
        if (!SimpleUtils.isNumeric(hours.replace(",",""))){
            hours = "0";
        }
        scheduleHoursAndWages.put(hoursAndWagesKey, Float.valueOf(hours.replaceAll(",","")));
        return scheduleHoursAndWages;
    }

    @Override
    public synchronized List<HashMap<String, Float>> getScheduleLabelHoursAndWagesDataForEveryDayInCurrentWeek() throws Exception {
        List<HashMap<String, Float>> ScheduleLabelHoursAndWagesDataForDays = new ArrayList<HashMap<String, Float>>();
        List<WebElement> ScheduleCalendarDayLabels = MyThreadLocal.getDriver().findElements(By.className("day-week-picker-period"));
        ScheduleCommonPage scheduleCommonPage = new ConsoleScheduleCommonPage();
        if(scheduleCommonPage.isScheduleDayViewActive()) {
            if(ScheduleCalendarDayLabels.size() != 0) {
                for(WebElement ScheduleCalendarDayLabel: ScheduleCalendarDayLabels)
                {
                    click(ScheduleCalendarDayLabel);
                    ScheduleLabelHoursAndWagesDataForDays.add(getScheduleLabelHoursAndWages());
                }
            }
            else {
                SimpleUtils.fail("Schedule Page Day View Calender not Loaded Successfully!", true);
            }
        }
        else {
            SimpleUtils.fail("Schedule Page Day View Button not Active!", true);
        }
        return ScheduleLabelHoursAndWagesDataForDays;
    }

    public boolean isSmartCardAvailableByLabel(String cardLabel) throws Exception {
        waitForSeconds(4);
        if (carouselCards.size() != 0) {
            for (WebElement carouselCard : carouselCards) {
                smartCardScrolleToLeft();
                if (carouselCard.isDisplayed() && carouselCard.getText().toLowerCase().contains(cardLabel.toLowerCase())
                        && isSmartcardContainText(carouselCard))
                    return true;
                else if (!carouselCard.isDisplayed()) {
                    while (isSmartCardScrolledToRightActive() == true) {
                        if (carouselCard.isDisplayed() && carouselCard.getText().toLowerCase().contains(cardLabel.toLowerCase())
                                && isSmartcardContainText(carouselCard))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getSwapCountFromSwapRequestsCard(String cardLabel) throws Exception {
        int count = 0;
        for (WebElement carouselCard : carouselCards) {
            if (carouselCard.getText().toLowerCase().contains(cardLabel.toLowerCase())
                    && isSmartcardContainText(carouselCard)) {
                count = Integer.parseInt(carouselCard.findElement(By.tagName("h1")).getText().split(" ")[0].trim());
            }
        }
        return count;
    }


    void smartCardScrolleToLeft() throws Exception {
        if (isElementLoaded(smartcardArrowLeft, 2)) {
            while (smartcardArrowLeft.getAttribute("class").contains("available")) {
                click(smartcardArrowLeft);
            }
        }

    }


    public boolean isSmartcardContainText(WebElement smartcardElement) throws Exception {
        if (smartcardElement.getText().trim().length() > 0) {
            return true;
        } else {
            SimpleUtils.fail("Schedule Page: Smartcard contains No Text or Spinning Icon on duration '" + getActiveWeekText() + "'.", true);
            return false;
        }
    }



    public boolean isSmartCardScrolledToRightActive() throws Exception {
        if (isElementLoaded(smartcardArrowRight, 10) && smartcardArrowRight.getAttribute("class").contains("available")) {
            click(smartcardArrowRight);
            return true;
        }
        return false;
    }

    @Override
    public String getsmartCardTextByLabel(String cardLabel) {
        if (carouselCards.size() != 0) {
            for (WebElement carouselCard : carouselCards) {
                if (carouselCard.isDisplayed() && carouselCard.getText().toLowerCase().contains(cardLabel.toLowerCase())){
                    scrollToElement(carouselCard);
                    return carouselCard.getText();
                }
            }
        }
        return null;
    }


    @FindBy(css = "span.weather-forecast-temperature")
    private List<WebElement> weatherTemperatures;

    @Override
    public String getWeatherTemperature() throws Exception {
        String temperatureText = "";
        if (weatherTemperatures.size() != 0)
            for (WebElement weatherTemperature : weatherTemperatures) {
                if (weatherTemperature.isDisplayed()) {
                    if (temperatureText == "")
                        temperatureText = weatherTemperature.getText();
                    else
                        temperatureText = temperatureText + " | " + weatherTemperature.getText();
                } else if (!weatherTemperature.isDisplayed()) {
                    while (isSmartCardScrolledToRightActive() == true) {
                        if (temperatureText == "")
                            temperatureText = weatherTemperature.getText();
                        else
                            temperatureText = temperatureText + " | " + weatherTemperature.getText();
                    }
                }
            }

        return temperatureText;
    }

    @FindBy(css = "table tr:nth-child(1)")
    private WebElement scheduleTableTitle;
    @FindBy(css = "table tr:nth-child(2)")
    private WebElement scheduleTableHours;
    @Override
    public HashMap<String, String> getHoursFromSchedulePage() throws Exception {
        // Wait for the hours to load
        waitForSeconds(5);
        HashMap<String, String> scheduleHours = new HashMap<>();
        if (isElementLoaded(scheduleTableTitle, 5) && isElementLoaded(scheduleTableHours, 5)) {
            List<WebElement> titles = scheduleTableTitle.findElements(By.tagName("th"));
            List<WebElement> hours = scheduleTableHours.findElements(By.tagName("td"));
            if (titles != null && hours != null) {
                if (titles.size() == 4 && hours.size() == 4) {
                    for (int i = 1; i < titles.size(); i++) {
                        scheduleHours.put(titles.get(i).getText(), hours.get(i).getText());
                        SimpleUtils.pass("Get Title: " + titles.get(i).getText() + " and related Hours: " +
                                hours.get(i).getText());
                    }
                }
            } else {
                SimpleUtils.fail("Failed to find the Schedule table elementes!", true);
            }
        }else {
            SimpleUtils.fail("Elements are not Loaded!", true);
        }
        return scheduleHours;
    }


    public void weatherWeekSmartCardIsDisplayedForAWeek() throws Exception {
        if (isElementLoaded(smartcardArrowRight,5)) {
            click(smartcardArrowRight);
            String jsonTimeZoon = parametersMap2.get("Time_Zone");
            TimeZone timeZone = TimeZone.getTimeZone(jsonTimeZoon);
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            dfs.setTimeZone(timeZone);
            String currentTime =  dfs.format(new Date());
            int currentDay = Integer.valueOf(currentTime.substring(currentTime.length()-2));
            try{
                String firstDayInWeatherSmtCad2 = getDriver().findElement(By.xpath("//*[contains(text(),'Weather - Week of')]")).getText();
                int firstDayInWeatherSmtCad = Integer.valueOf(firstDayInWeatherSmtCad2.substring(firstDayInWeatherSmtCad2.length() - 2));
                System.out.println("firstDayInWeatherSmtCad:" + firstDayInWeatherSmtCad);
                if ((firstDayInWeatherSmtCad + 7) > currentDay) {
                    SimpleUtils.pass("The week smartcard is current week");
                    if (areListElementVisible(weatherTemperatures, 8)) {
                        String weatherWeekTest = getWeatherDayOfWeek();
                        SimpleUtils.report("Weather smart card is displayed for a week from mon to sun" + weatherWeekTest);
                        for (ScheduleTestKendraScott2.DayOfWeek e : ScheduleTestKendraScott2.DayOfWeek.values()) {
                            if (weatherWeekTest.contains(e.toString())) {
                                SimpleUtils.pass("Weather smartcard include one week weather");
                            } else {
                                SimpleUtils.fail("Weather Smart card is not one whole week", false);
                            }
                        }
                    } else {
                        SimpleUtils.fail("there is no week weather smartcard", false);
                    }
                } else {
                    SimpleUtils.fail("This is not current week weather smartcard ", false);
                }
            } catch (Exception e){
                SimpleUtils.report("there is no week weather smartcard!");
            }
        }else {
            String jsonTimeZoon = parametersMap2.get("Time_Zone");
            TimeZone timeZone = TimeZone.getTimeZone(jsonTimeZoon);
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            dfs.setTimeZone(timeZone);
            String currentTime =  dfs.format(new Date());
            int currentDay = Integer.valueOf(currentTime.substring(currentTime.length()-2));
            try{
                String firstDayInWeatherSmtCad2 = getDriver().findElement(By.xpath("//*[contains(text(),'Weather - Week of')]")).getText();
                int firstDayInWeatherSmtCad = Integer.valueOf(firstDayInWeatherSmtCad2.substring(firstDayInWeatherSmtCad2.length() - 2));
                System.out.println("firstDayInWeatherSmtCad:" + firstDayInWeatherSmtCad);
                if ((firstDayInWeatherSmtCad + 7) > currentDay) {
                    SimpleUtils.pass("The week smartcard is current week");
                    if (areListElementVisible(weatherTemperatures, 8)) {
                        String weatherWeekTest = getWeatherDayOfWeek();
                        SimpleUtils.report("Weather smart card is displayed for a week from mon to sun" + weatherWeekTest);
                        for (ScheduleTestKendraScott2.DayOfWeek e : ScheduleTestKendraScott2.DayOfWeek.values()) {
                            if (weatherWeekTest.contains(e.toString())) {
                                SimpleUtils.pass("Weather smartcard include one week weather");
                            } else {
                                SimpleUtils.fail("Weather Smart card is not one whole week", false);
                            }
                        }

                    } else {
                        SimpleUtils.fail("there is no week weather smartcard", false);
                    }

                } else {
                    SimpleUtils.fail("This is not current week weather smartcard ", false);
                }
            } catch (Exception e){
                SimpleUtils.report("there is no week weather smartcard!");
            }
        }

    }

    @FindBy(css = ".weather-forecast-day-name")
    private List<WebElement> weatherDaysOfWeek;
    public String getWeatherDayOfWeek() throws Exception {
        String daysText = "";
        if (weatherDaysOfWeek.size() != 0)
            for (WebElement weatherDay : weatherDaysOfWeek) {
                if (weatherDay.isDisplayed()) {
                    if (daysText == "")
                        daysText = weatherDay.getText();
                    else
                        daysText = daysText + " | " + weatherDay.getText();
                } else if (!weatherDay.isDisplayed()) {
                    while (isSmartCardScrolledToRightActive() == true) {
                        if (daysText == "")
                            daysText = weatherDay.getText();
                        else
                            daysText = daysText + " | " + weatherDay.getText();
                    }
                }
            }

        return daysText;
    }


    @FindBy(className = "card-carousel-link")
    private WebElement viewShiftsBtn;
    @Override
    public void validateTheAvailabilityOfOpenShiftSmartcard() throws Exception {
        if (areListElementVisible(carouselCards, 10)) {
            if (isSmartCardAvailableByLabel("WANT MORE HOURS"))
                SimpleUtils.pass("My Schedule Page: Open Shift Smartcard is present on Schedule Page successfully");
            else SimpleUtils.fail("My Schedule Page: Open Shift Smartcard isn't present on Schedule Page", true);
        } else SimpleUtils.fail("My Schedule Page: Smartcards failed to load", true);
    }

    public boolean isViewShiftsBtnPresent() throws Exception {
        boolean isConsistent = false;
        if (areListElementVisible(carouselCards, 10)) {
            if (isSmartCardAvailableByLabel("WANT MORE HOURS")) {
                if (isElementLoaded(viewShiftsBtn, 5)) {
                    isConsistent = true;
                    SimpleUtils.pass("My Schedule Page: 'View Shifts' button is present on Open Shift Smartcard successfully");
                } else
                    SimpleUtils.report("My Schedule Page: 'View Shifts' button isn't present on Open Shift Smartcard since there is 0 shift offer");
            } else SimpleUtils.fail("My Schedule Page: Open Shift Smartcard isn't present on Schedule Page", true);
        } else SimpleUtils.fail("My Schedule Page: Smartcards failed to load", true);
        return isConsistent;
    }


    @FindBy (css = "[ng-if=\"(scheduleSmartCard.minorBlockingViolations || scheduleSmartCard.unassignedShifts || scheduleSmartCard.outsideOperatingHoursShifts) && hasSchedule()\"] .card-carousel-card")
    private WebElement requiredActionSmartCard;
    @FindBy (css = "[ng-click=\"smartCardShiftFilter('Requires Action')\"]")
    private WebElement viewShiftsBtnOnRequiredActionSmartCard;

    public boolean isRequiredActionSmartCardLoaded() throws Exception {
        if (isElementLoaded(requiredActionSmartCard, 5)) {
            return true;
        } else
            return false;
    }

    public void clickOnViewShiftsBtnOnRequiredActionSmartCard() throws Exception {
        if (isElementLoaded(requiredActionSmartCard, 5) && isElementLoaded(viewShiftsBtnOnRequiredActionSmartCard, 5)) {
            if (viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("View Shifts")){
                click(viewShiftsBtnOnRequiredActionSmartCard);
                SimpleUtils.pass("Click View Shifts button successfully! ");
            } else if(viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("Clear Filter")){
                SimpleUtils.report("View Shifts button alreay been clicked! ");
            } else
                SimpleUtils.fail("The button name on required action smart card display incorrectly! ", false);
        } else
            SimpleUtils.fail("The required action smard card or the view shifts button on it loaded fail! ", false);
    }

    public void clickOnClearShiftsBtnOnRequiredActionSmartCard() throws Exception {
        if (isElementLoaded(requiredActionSmartCard, 5) && isElementLoaded(viewShiftsBtnOnRequiredActionSmartCard, 5)) {
            if (viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("Clear Filter")){
                click(viewShiftsBtnOnRequiredActionSmartCard);
                SimpleUtils.pass("Click Clear Filter button successfully! ");
            } else if(viewShiftsBtnOnRequiredActionSmartCard.getText().equalsIgnoreCase("View Shifts")){
                SimpleUtils.report("Clear Filter button alreay been clicked! ");
            } else
                SimpleUtils.fail("The button name on required action smart card display incorrectly! ", false);
        } else
            SimpleUtils.fail("The required action smard card or the view shifts button on it loaded fail! ", false);
    }

    @FindBy(css = ".fa-flag.sch-red")
    private WebElement redFlagInCompliance;

    @FindBy(css = "[src=\"img/legion/schedule/shift-info-danger.png\"]")
    private List<WebElement> complianceShitShowIcon;

    @FindBy(css = "card-carousel-card[ng-if*='compliance'] span")
    private WebElement viewShift;

    @FindBy(css = "[ng-click=\"$ctrl.openFilter()\"]")
    private WebElement filterButton;
    @Override
    public boolean verifyRedFlagIsVisible() throws Exception {
        if (isElementLoaded(redFlagInCompliance, 20)) {
            SimpleUtils.report("red flag is visible ");
            return true;
        }
        return false;
    }

    @FindBy(css = "[ng-if=\"scheduleSmartCard.complianceViolations\"] div.card-carousel-card")
    private WebElement complianceSmartcardHeader;
    @Override
    public boolean verifyComplianceShiftsSmartCardShowing() throws Exception {
        if (isElementLoaded(complianceSmartcardHeader,15)) {
            SimpleUtils.pass("Compliance smartcard is visible ");
            return true;
        } else {
            SimpleUtils.report("there is no compliance smartcard this week");
            return false;
        }
    }

    @Override
    public void verifyComplianceShiftsShowingInGrid() throws Exception {
        if (isElementLoaded(complianceSmartcardHeader,15)) {
            if (complianceShitShowIcon.size() > 0) {
                SimpleUtils.pass("Compliance shift is showing in grid");
            }else {
                SimpleUtils.fail("compliance shifts display failed",false);
            }
        }else {
            SimpleUtils.report("there is no compliance smartcard in current week");
        }

    }

    @Override
    public void verifyClearFilterFunction() throws Exception {
        String clearFilterBtnTextDefault = "Clear Filter";

        if (isElementLoaded(complianceSmartcardHeader,10) ) {
            String clearFilterTxt =viewShift.getText();
            SimpleUtils.report("clear filter is" + clearFilterTxt);
            if (clearFilterBtnTextDefault.equals(clearFilterTxt)) {
                click(viewShift);
                SimpleUtils.pass("clear filter button is clickable");
                String filterText = getDriver().findElement(By.cssSelector("lg-filter > div > input-field > ng-form > div")).getText();
                if (filterText.equals("")) {
                    SimpleUtils.pass("filter 'Compliance shifts' will be unselected after clicking clear filter");
                }
            }else
                SimpleUtils.fail("clear filter  button can't clickable",true);
        }else
            SimpleUtils.report("there is no compliance shift this week");

    }


    @FindBy(css = "[ng-click=\"smartCardShiftFilter('Compliance Review')\"]")
    private WebElement viewShiftBtn;
    @Override
    public boolean clickViewShift() throws Exception {
        if (isElementLoaded(viewShiftBtn, 15)) {
            click(viewShiftBtn);
            SimpleUtils.report("View shift button is visible ");
            return true;
        }else
            SimpleUtils.report("No view shift button");
        return false;
    }

    @Override
    public void verifyComplianceFilterIsSelectedAftClickingViewShift() throws Exception {
        String filterTextDefault =" Compliance Review\n" +
                "    ";
        if (clickViewShift() == true) {
            String filterText = getDriver().findElement(By.cssSelector("lg-filter > div > input-field > ng-form > div")).getText();
            if (filterText.equals(filterTextDefault)) {
                SimpleUtils.report("Compliance filter is selected after clicking view shift button");
            }
        }else {
            SimpleUtils.report("there is no compliance");
        }
    }

    @FindBy(xpath = "//table[@class=\"ng-scope\"]")
    private WebElement scheduleSmartCard;

    @FindBy(css = "lg-button[ng-click=\"controlPanel.fns.editAction($event)\"]")
    private WebElement editScheduleButton;
    @Override
    public HashMap<String, Float> getScheduleBudgetedHoursInScheduleSmartCard() throws Exception {
            /*
            wait schedule smart card data load
            */
        waitForSeconds(10);
        if (isElementLoaded(scheduleSmartCard,20)
                && isElementLoaded(editScheduleButton, 10) ){
            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
            HashMap<String, Float> hoursWagesText = smartCardPage.getScheduleLabelHoursAndWages();
            return hoursWagesText;
        }
        return null;
    }

    @FindBy (className = "card-carousel-card")
    private List<WebElement> smartCards;
    @FindBy (className = "card-carousel-link")
    private List<WebElement> cardLinks;

    @FindBy (css = ".sch-grid-container")
    private WebElement shiftGrid;

    @Override
    public void clickLinkOnSmartCardByName(String linkName) throws Exception {
        if (isElementLoaded(shiftGrid, 20)) {
            waitForSeconds(3);
            if (areListElementVisible(cardLinks, 10)) {
                for (WebElement cardLink : cardLinks) {
                    if (cardLink.getText().equalsIgnoreCase(linkName)) {
                        clickTheElement(cardLink);
                        waitForSeconds(2);
                        SimpleUtils.pass("Click the link: " + linkName + " Successfully!");
                        break;
                    }
                }
            }else {
                SimpleUtils.report("There are no smart card links!");
            }
        } else {
            SimpleUtils.fail("Failed for loading shift grid view!", false);
        }
    }

    @Override
    public int getCountFromSmartCardByName(String cardName) throws Exception {
        int count = -1;
        if (areListElementVisible(smartCards, 5)) {
            for (WebElement smartCard : smartCards) {
                WebElement title = smartCard.findElement(By.className("card-carousel-card-title"));
                if (title != null && title.getText().trim().contains(cardName)) {
                    WebElement h1 = smartCard.findElement(By.tagName("h1"));
                    String h1Title = h1 == null ? "" : h1.getText();
                    if (h1Title.contains(" ")) {
                        String[] items = h1Title.split(" ");
                        for (String item : items) {
                            if (SimpleUtils.isNumeric(item)) {
                                count = Integer.parseInt(item);
                                SimpleUtils.report("Get " + cardName + " count is: " + count);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (count == -1) {
            SimpleUtils.fail("Failed to get the count from " + cardName + " card!", false);
        }
        return count;
    }

    @Override
    public boolean isSpecificSmartCardLoaded(String cardName) throws Exception {
        boolean isLoaded = false;
        waitForSeconds(15);
        if (areListElementVisible(smartCards, 35)) {
            for (WebElement smartCard : smartCards) {
                WebElement title = smartCard.findElement(By.className("card-carousel-card-title"));
                if (title != null && title.getText().trim().equalsIgnoreCase(cardName)) {
                    isLoaded = true;
                    break;
                }
            }
        }
        return isLoaded;
    }

    @FindBy (css = "tbody tr")
    private List<WebElement> smartCardRows;
    @Override
    public HashMap<String, String> getBudgetNScheduledHoursFromSmartCard() throws Exception {
        HashMap<String, String> budgetNScheduledHours = new HashMap<>();
        if (areListElementVisible(smartCardRows, 5) && smartCardRows.size() != 0) {
            List<WebElement> ths = smartCardRows.get(0).findElements(By.tagName("th"));
            List<WebElement> tds = smartCardRows.get(1).findElements(By.tagName("td"));
            if (ths != null && tds != null && ths.size() == 4 && tds.size() == 4) {
                budgetNScheduledHours.put(ths.get(1).getText(), tds.get(1).getText());
                budgetNScheduledHours.put(ths.get(2).getText(), tds.get(2).getText());
                SimpleUtils.report("Smart Card: Get the hour: " + tds.get(1).getText() + " for: " + ths.get(1).getText());
                SimpleUtils.report("Smart Card: Get the hour: " + tds.get(2).getText() + " for: " + ths.get(2).getText());
            } else {
                SimpleUtils.fail("Schedule Week View Page: The format of the budget and Scheduled hours' smart card is incorrect!", false);
            }
        } else {
            SimpleUtils.fail("Schedule Week View Page: Budget and Scheduled smart card not loaded Successfully!", false);
        }
        return budgetNScheduledHours;
    }


    @Override
    public int getComplianceShiftCountFromSmartCard(String cardName) throws Exception {
        int count = 0;
        if (areListElementVisible(smartCards, 5)) {
            for (WebElement smartCard : smartCards) {
                WebElement title = smartCard.findElement(By.className("card-carousel-card-title"));
                if (title != null && title.getText().trim().equalsIgnoreCase(cardName)) {
                    WebElement header = smartCard.findElement(By.tagName("h1"));
                    if (header != null && !header.getText().isEmpty()) {
                        count = Integer.parseInt(header.getText().trim().split(" ")[0]);
                        SimpleUtils.report("Compliance Card: Get: " + count + " compliance shift(s).");
                        break;
                    }
                }
            }
        }
        if (count == 0) {
            SimpleUtils.report("Compliance Card: Failed to get the count of the shift(s)!");
        }
        return count;
    }


    @Override
    public void verifyChangesNotPublishSmartCard(int changesNotPublished) throws Exception {
        boolean flag = false;
        if (areListElementVisible(smartCards,15)){
            for (WebElement e: smartCards) {
                //findElement(By.cssSelector(".card-carousel-card-title"))
                if (changesNotPublished == 0) {
                    if (e.getText().toLowerCase().contains("action required") && e.getText().toLowerCase().contains("schedule not") && e.getText().toLowerCase().contains("published")) {
                        SimpleUtils.pass("Changes not published smart card loads successfully!");
                        flag = true;
                        break;
                    }
                } else {
                    if (e.getText().toLowerCase().contains("unpublished changes") && e.getText().toLowerCase().contains(changesNotPublished + " change") && e.getText().toLowerCase().contains("not published")) {
                        SimpleUtils.pass("Changes not published smart card with number of changes loads successfully!");
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag){
                SimpleUtils.fail("There is no expected smart card",false);
            }
        } else {
            SimpleUtils.fail("No smart cards!", false);
        }
    }

    @FindBy (xpath = "//div[contains(text(), \"Action Required\")]/following-sibling::h1[1]")
    private WebElement changesOnActionRequired;
    @Override
    public String getChangesOnActionRequired() throws Exception {
        String changes = "";
        if (isElementLoaded(changesOnActionRequired, 10)) {
            changes = changesOnActionRequired.getText().replaceAll("\"","").trim();
        }
        return changes;
    }


    @FindBy(css = "img[ng-if=\"unpublishedDeleted && isOneAndOnlyShiftTypeSelected('Edited')\"]")
    private WebElement tooltipIconOfUnpublishedDeleted;
    @Override
    public String getTooltipOfUnpublishedDeleted() throws Exception {
        String tooltipOfUnpublishedDeleted = "";
        if (isElementLoaded(tooltipIconOfUnpublishedDeleted,10)) {
            mouseHover(tooltipIconOfUnpublishedDeleted);
            tooltipOfUnpublishedDeleted = changesOnActionRequired.getAttribute("data-tootik");
        }
        return tooltipOfUnpublishedDeleted;
    }


    @FindBy(css = ".modal-dialog")
    private WebElement holidaySmartCardWindow;
    @Override
    public List<String> getHolidaysOfCurrentWeek() throws Exception {
        List<String> holidays = new ArrayList<String>();
        if (isElementLoaded(holidaySmartCardWindow,5) && holidaySmartCardWindow.findElements(By.cssSelector(".event-card span")).size()>0){
            List<WebElement> holidayList = holidaySmartCardWindow.findElements(By.cssSelector(".event-card span"));
            for (WebElement element: holidayList){
                holidays.add(element.getText().replace("\n",""));
            }
        } else {
            SimpleUtils.fail("Holiday popup window fail to load!", false);
        }
        return holidays;
    }



    @FindBy(css = ".card-carousel-arrow-right.available")
    private WebElement arrowRightAvailable;

    @Override
    public void navigateToTheRightestSmartCard() throws Exception {
        while (isElementLoaded(arrowRightAvailable, 5)) {
            click(arrowRightAvailable);
        }
    }

    @FindBy(css = "[ng-if=\"controlPanel.fns.getVisibility('PUBLISH') && hasSchedule() && controlPanel.canPublish\"] .card-carousel-card-smart-card-required")
    private WebElement scheduleNotPublishedSmartCard;

    @Override
    public boolean isScheduleNotPublishedSmartCardLoaded() throws Exception {
        if (isElementLoaded(scheduleNotPublishedSmartCard,15)) {
            SimpleUtils.pass("Schedule Not Published SmartCard is show ");
            return true;
        } else {
            SimpleUtils.report("There is no Schedule Not Published SmartCard this week");
            return false;
        }

    }


    @Override
    public String getWholeMessageFromActionRequiredSmartCard() throws Exception {
        String message = "";
        if (isElementLoaded(requiredActionSmartCard, 5)) {
            message = requiredActionSmartCard.getText();
        } else
            SimpleUtils.fail("Required Action smart card fail to load! ", false);
        return message;
    }


    @FindBy (css = "[ng-if=\"requiredActionsCount >= 2\"] .col-fx-1")
    private List<WebElement> unassignedAndOOOHMessageOnActionRequiredSmartCard;

    @FindBy (css = "[ng-if=\"scheduleSmartCard.unassignedShifts && requiredActionsCount === 1\"]")
    private WebElement unassignedMessageOnActionRequiredSmartCard;

    @FindBy (css = "[ng-if=\"scheduleSmartCard.outsideOperatingHoursShifts && requiredActionsCount === 1\"]")
    private WebElement oOOHMessageOnActionRequiredSmartCard;

    @FindBy (css = "[ng-if=\"scheduleSmartCard.minorBlockingViolations && requiredActionsCount === 1\"]")
    private WebElement minorViolationMessageOnActionRequiredSmartCard;

    @Override
    public HashMap<String, String> getMessageFromActionRequiredSmartCard() throws Exception {
        HashMap<String, String> messageOnSmartCard = new HashMap<String, String>();
        if (isElementLoaded(requiredActionSmartCard, 5)) {
            if (areListElementVisible(unassignedAndOOOHMessageOnActionRequiredSmartCard, 5)) {
                boolean hasUnassignedMessage = false;
                boolean hasOOOHMessage = false;
                boolean hasMinorViolationMessage = false;
                for (WebElement message: unassignedAndOOOHMessageOnActionRequiredSmartCard) {
                    String firstMessage = message.findElement(By.tagName("div")).getText();
                    switch (firstMessage) {
                        case "Unassigned":
                            messageOnSmartCard.put("unassigned", message.getText());
                            hasUnassignedMessage = true;
                        case "Outside Operating Hours":
                            messageOnSmartCard.put("OOOH", message.getText());
                            hasOOOHMessage = true;
                        case "Minor Violation":
                            messageOnSmartCard.put("minorViolation", message.getText());
                            hasMinorViolationMessage = true;
                    }
                }
                if (!hasUnassignedMessage) {
                    messageOnSmartCard.put("unassigned", "");
                }
                if (!hasOOOHMessage) {
                    messageOnSmartCard.put("OOOH", "");
                }
                if (!hasMinorViolationMessage) {
                    messageOnSmartCard.put("minorViolation", "");
                }
            } else if (isElementLoaded(unassignedMessageOnActionRequiredSmartCard, 5)) {
                messageOnSmartCard.put("unassigned", unassignedMessageOnActionRequiredSmartCard.getText());
                messageOnSmartCard.put("OOOH", "");
                messageOnSmartCard.put("minorViolation", "");
            } else if (isElementLoaded(oOOHMessageOnActionRequiredSmartCard, 5)) {
                messageOnSmartCard.put("OOOH", oOOHMessageOnActionRequiredSmartCard.getText());
                messageOnSmartCard.put("unassigned", "");
                messageOnSmartCard.put("minorViolation", "");
            } else if (isElementLoaded(minorViolationMessageOnActionRequiredSmartCard, 5)) {
                messageOnSmartCard.put("OOOH", "");
                messageOnSmartCard.put("unassigned", "");
                messageOnSmartCard.put("minorViolation", minorViolationMessageOnActionRequiredSmartCard.getText());
            }else
                SimpleUtils.fail("No available message display on Action Required smart card! ", false);
        } else
            SimpleUtils.fail("Required Action smart card fail to load! ", false);
        return messageOnSmartCard;
    }


    @FindBy (css = "div.card-carousel-card-blue")
    private WebElement masterTemplateSmartCard;
    @FindBy (css = "[ng-click*=\"toggleTemplateView(\"]")
    private WebElement viewTemplateLinkOnMasterTemplateSmartCard;

    public void clickViewTemplateLinkOnMasterTemplateSmartCard() throws Exception {
        if (isElementLoaded(masterTemplateSmartCard, 10)) {
            if (isElementLoaded(viewTemplateLinkOnMasterTemplateSmartCard, 5)) {
                clickTheElement(viewTemplateLinkOnMasterTemplateSmartCard);
                SimpleUtils.pass("Click View Template link successfully! ");
            } else
                SimpleUtils.fail("The View Template link on Master Template smard card fail to load! ", false);
        } else
            SimpleUtils.fail("The Master Template smart card fail to load! ", false);
    }

    @FindBy (xpath = "//div[contains(text(), \"Weekly Budget\")]/following-sibling::h1[1]")
    private WebElement budgetHoursOnWeeklyBudget;
    @Override
    public String getBudgetValueFromWeeklyBudgetSmartCard(String cardName) throws Exception {
        String weeklyBudgetValue;
        if (areListElementVisible(smartCards, 15)) {
            for (WebElement smartCard : smartCards) {
                WebElement title = smartCard.findElement(By.className("card-carousel-card-title"));
                if (title != null && title.getText().trim().equalsIgnoreCase(cardName)) {
                    weeklyBudgetValue = budgetHoursOnWeeklyBudget.getText();
                    return weeklyBudgetValue;
                }
            }
        }else{
            SimpleUtils.fail("The Smart Cards on the Schedule page are not loaded correctly!", false);
        }
        return null;
    }


    @Override
    public String getBudgetValueFromScheduleBudgetSmartCard() throws Exception {
        if (isElementLoaded(scheduleSmartCard, 3)) {
            WebElement ScheduleBudgetSmartCard = scheduleSmartCard.findElement(By.cssSelector("[ng-if=\"scheduleSmartCard.guidanceSummary !== null\"]"));
            return ScheduleBudgetSmartCard.getText().trim();
        }else{
            SimpleUtils.fail("The Schedule Smart Card on the Schedule page is not loaded correctly!", false);
        }
        return null;
    }

    @Override
    public boolean isBudgetHoursSmartCardIsLoad() throws Exception {
        boolean isLoaded = false;
        if (isSpecificSmartCardLoaded("WEEKLY BUDGET")) {
            isLoaded = true;
        } else {
            if (areListElementVisible(smartCards, 15)) {
                for (WebElement smartCard : smartCards) {
                    try {
                        WebElement content = smartCard.findElement(By.tagName("h1"));
                        if (content != null && content.getText().trim().equalsIgnoreCase("Budget Hours")) {
                            isLoaded = true;
                            break;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        return isLoaded;
    }
    @FindBy (css = "span[ng-if=\"!hasBeenAcknowledged\"]")
    private WebElement acknowledgeButton;
    @Override
    public void clickOnAcknowledgeButtonOnAcknowledgeNotificationSmartCard () throws Exception {
        if (isElementLoaded(acknowledgeButton, 3)) {
            clickTheElement(acknowledgeButton);
            waitForSeconds(5);
            if(!isElementLoaded(acknowledgeButton, 3) && !isSpecificSmartCardLoaded("ACTION REQUIRED")){
                SimpleUtils.pass("Acknowledge successfully! the acknowledge notification smart card and button has already disappear! ");
            }else
                SimpleUtils.fail("Acknowledge fail! the acknowledge notification smart card and button should disappear! ", false);
        }else{
            SimpleUtils.fail("The Acknowledge Button On Acknowledge Notification Smart Card is not loaded correctly!", false);
        }
    }
}
