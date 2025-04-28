package com.legion.pages.core;

import com.google.inject.internal.cglib.core.$ReflectUtils;
import com.legion.pages.BasePage;
import com.legion.pages.InboxPage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleInboxPage  extends BasePage implements InboxPage {

    public ConsoleInboxPage(){
        PageFactory.initElements(getDriver(), this);
    }

    //Added by Nora

    //Added by Julie
    final static String consoleInboxMenuItemText = "Inbox";

    @FindBy(css = "a[ng-click=\"createAnnouncement()\"]")
    private WebElement createAnnouncementIcon;

    @FindBy(css = ".weekdays-column")
    private List<WebElement> weekDaysInGFE;

    @FindBy(className = "console-navigation-item")
    private List<WebElement> consoleNavigationMenuItems;

    @FindBy(css = "select[ng-attr-id=\"{{$ctrl.inputName}}\"]")
    private WebElement announcementType;

    @FindBy(css = "p.estimate-faith-text-vsl")
    private WebElement VSLInfo;

    @FindBy(className = "week-summary")
    private WebElement weekSummary;

    @FindBy(id = "gfe-min-shifts")
    private WebElement gfeMinShifts;

    @FindBy(id = "gfe-average-hours")
    private WebElement gfeAverageHours;

    @FindBy(css = "div:nth-child(3) > p > label")
    private WebElement gfeLocation;

    @Override
    public void clickOnInboxConsoleMenuItem() throws Exception {
        if (consoleNavigationMenuItems.size() != 0) {
            WebElement consoleInboxMenuElement = SimpleUtils.getSubTabElement(consoleNavigationMenuItems, consoleInboxMenuItemText);
            clickTheElement(consoleInboxMenuElement);
            SimpleUtils.pass("'Inbox' Console Menu loaded successfully!");
        } else
            SimpleUtils.fail("'Inbox' Console Menu failed to load!", false);
    }

    @Override
    public void createGFEAnnouncement() throws Exception {
        if (isElementLoaded(createAnnouncementIcon,10)) {
            waitForSeconds(5);
            clickTheElement(createAnnouncementIcon);
            if (isElementLoaded(announcementType,15)) {
                selectByVisibleText(announcementType, "Good Faith Estimate");
                if (areListElementVisible(weekDaysInGFE,5))
                    SimpleUtils.pass("Inbox: A new announcement with type \"Good Faith Estimate\" is selected successfully");
                else
                    SimpleUtils.fail("Inbox: A new announcement with type \"Good Faith Estimate\" failed to select",false);
            } else
                SimpleUtils.fail("Inbox: Create New Announcement window failed to load after clicking + icon",false);
        } else
            SimpleUtils.fail("Inbox: Create Announcement + icon failed to load",false);
    }

    @Override
    public LinkedHashMap<String, List<String>> getGFEWorkingHours() throws Exception {
        // SUN,[09:00AM, 05:00PM]
        LinkedHashMap<String, List<String>> GFEHours = new LinkedHashMap<>();
        if (areListElementVisible(weekDaysInGFE, 10)) {
            for (WebElement weekDay : weekDaysInGFE) {
                WebElement day = weekDay.findElement(By.className("weekdays-column-header"));
                List<WebElement> workTimes = weekDay.findElements(By.tagName("input"));
                if (day != null && workTimes != null && workTimes.size() == 2) {
                    List<String> startNEndTime = new ArrayList<>();
                    String startTime = workTimes.get(0).getAttribute("value");
                    String endTime = workTimes.get(1).getAttribute("value");
                    startNEndTime.add(startTime);
                    startNEndTime.add(endTime);
                    GFEHours.put(day.getText(), startNEndTime);
                    SimpleUtils.report("Inbox: Get GFE time for: " + day.getText() + ", GFE time is: " + startTime + " - " + endTime);
                }
            }
        }
        if (GFEHours.size() != 7)
            SimpleUtils.fail("Inbox: Failed to find the week day and working time of GFE!", false);
        return GFEHours;
    }

    @Override
    public String getGFEFirstDayOfWeek() throws Exception {
        // SUN
        String GFETheFirstDayOfWeek = "";
        if (areListElementVisible(weekDaysInGFE, 10)) {
            WebElement day = weekDaysInGFE.get(0).findElement(By.className("weekdays-column-header"));
            GFETheFirstDayOfWeek = day.getText();
        }
        return GFETheFirstDayOfWeek;
    }

    @Override
    public boolean compareGFEWorkingHrsWithRegularWorkingHrs(LinkedHashMap<String, List<String>> GFEWorkingHours,
                                                             LinkedHashMap<String, List<String>> regularHoursFromControl) throws Exception {
        boolean isConsistent = false;
        Iterator itRegular = regularHoursFromControl.keySet().iterator();
        while (itRegular.hasNext()) {
            String regularKey = itRegular.next().toString();
            List<String> regularValue = regularHoursFromControl.get(regularKey);
            Iterator itGFE = GFEWorkingHours.keySet().iterator();
            while (itGFE.hasNext()) {
                String GFEKey = itGFE.next().toString();
                if (regularKey.contains(GFEKey) ) {
                    List<String> GFEValue = GFEWorkingHours.get(GFEKey);
                    if (GFEValue.size() == 2 && regularValue.size() == 2) {
                        if (GFEValue.get(0).contains(regularValue.get(0).replace(" ", "").toUpperCase())
                                && GFEValue.get(1).contains(regularValue.get(1).replace(" ", "").toUpperCase())) {
                            isConsistent = true;
                            SimpleUtils.report("Regular Working Hours: " + regularKey + "---" + regularValue);
                            SimpleUtils.report("GFE Working Hours: " + GFEKey + "---" + GFEValue);
                            SimpleUtils.report("Are they consistent? " + isConsistent);
                            break;
                        }
                    }
                }
            }
        }
       return isConsistent;
    }

    @Override
    public void verifyVSLInfo(boolean isVSLTurnOn) throws Exception {
        if (isVSLTurnOn) {
            if (isElementLoaded(VSLInfo, 5) &&
                    VSLInfo.getText().contains("Team members will be informed regarding opting in to the Voluntary Standby List"))
                SimpleUtils.pass("Inbox: VSL info \"Team members will be informed regarding opting in to the Voluntary Standby List.\" is loaded successfully when VSL is turned on");
             else
                SimpleUtils.fail("Inbox: VSL info failed to load", false);
        } else {
            if (!isElementLoaded(VSLInfo, 5))
                SimpleUtils.pass("Inbox: No message \"Team members will be informed regarding opting in to the Volntary Standby List.\" loaded when VSL is turned off");
            else
                SimpleUtils.fail("Inbox: VSL info still displays although VSL is turned off",false);
        }
    }

    @Override
    public HashMap<String, String> getTheContentOfWeekSummaryInGFE() throws Exception {
        // Minimum Shifts, 3
        // Average Hours, 30
        // Location, AUSTIN DOWNTOWN
        waitForSeconds(2);
        HashMap<String, String> theContentOfWeekSummary = new HashMap<>();
        if (isElementLoaded(weekSummary,5)) {
            theContentOfWeekSummary.put("Minimum Shifts", gfeMinShifts.getAttribute("value"));
            theContentOfWeekSummary.put("Average Hours", gfeAverageHours.getAttribute("value"));
            theContentOfWeekSummary.put("Location", gfeLocation.getText());
            SimpleUtils.report("GFE Minimum Shifts: " + theContentOfWeekSummary.get("Minimum Shifts") + ", Average Hours: " + theContentOfWeekSummary.get("Average Hours")
                    + ", Location: " + theContentOfWeekSummary.get("Location"));
        }
        return theContentOfWeekSummary;
    }

    public boolean compareDataInGFEWeekSummary(HashMap<String, String> theContentOfWeekSummaryInGFE,
                                                             HashMap<String, List<String>> DataFromSchedulingPolicyGroups) throws Exception {
        boolean isConsistent = false;
        String valueGFE = theContentOfWeekSummaryInGFE.get("Minimum Shifts");
        List<String> valueSPG = DataFromSchedulingPolicyGroups.get("Shifts per week");
        if (valueGFE != null && valueSPG.get(0).equals(valueGFE)) {
            isConsistent = true;
            SimpleUtils.pass("Minimum Shifts in GFE --- " + valueGFE + ", Shifts per week in Scheduling Policy Groups --- " + valueSPG.get(0) + ". Are they consistent? " + isConsistent);
            valueGFE = theContentOfWeekSummaryInGFE.get("Average Hours");
            valueSPG = DataFromSchedulingPolicyGroups.get("Hours per week");
            if (valueGFE != null && valueSPG.get(0).equals(valueGFE)) {
                isConsistent = true;
                SimpleUtils.pass("Average Hours in GFE --- " + valueGFE + ", Hours per week in Scheduling Policy Groups --- " + valueSPG.get(0) + ". Are they consistent? " + isConsistent);
            } else {
                isConsistent = false;
                SimpleUtils.fail("Average Hours in GFE --- " + valueGFE + ", Hours per week in Scheduling Policy Groups --- " + valueSPG.get(0) + ". Are they consistent? " + isConsistent,false);
            }
        } else {
            isConsistent = false;
            SimpleUtils.fail("Minimum Shifts in GFE --- " + valueGFE + ", Shifts per week in Scheduling Policy Groups --- " + valueSPG.get(0) + ". Are they consistent? " + isConsistent, false);
        }
        return isConsistent;
    }

    //Added by Marym

    @FindBy(css = ".new-announcement-modal")
    private WebElement newAnnouncementModal;

    @FindBy(xpath = "//div[contains(@ng-if,\"options.canSendGoodFaithEstimate\")]")
    private WebElement announcementTypeSection;

    @FindBy(xpath = "//div[contains(@ng-class,\"options.canSendGoodFaithEstimate\")]")
    private WebElement sendToSection;

    @FindBy(css = ".gfe-send-to-select")
    private WebElement sendToTextBox;

    @FindBy(css = "label[style=\"display: block\"]")
    private WebElement messageText;

    @FindBy(css = "[placeholder=\"Title goes here\"]")
    private WebElement titleArea;

    @FindBy(css = "[placeholder=\"Your message goes here...\"]")
    private WebElement messageArea;

    @FindBy(css = "[ng-click=\"callCancelCallback()\"]")
    private WebElement cancelButton;

    @FindBy(css = "[ng-click=\"callOkCallback()\"]")
    private WebElement saveButton;

    @FindBy(css = "textarea[placeholder=\"Write a message...\"]")
    private WebElement messageInCreateGFETemplate;

    @FindBy(css = "[ng-if=\"gfeMetaData.showVSL\"]")
    private WebElement textOfVSL;

    @FindBy(css = ".estimate-faith.ng-scope")
    private WebElement operatingHoursSection;

    @FindBy(css = "div.weekdays-column.ng-scope")
    private List<WebElement> workingDays;

    @FindBy(css = "div.week-summary>h6")
    private WebElement weekSummaryText;

    @FindBy(css = "div.week-summary-settings-column")
    private List<WebElement> weekSummaryItems;


    @Override
    public void checkCreateGFEPage() throws Exception {
        if(isElementLoaded(newAnnouncementModal,10)){
            //check Send to:
            if (isElementLoaded(sendToSection, 5)){
                WebElement sendToText = sendToSection.findElement(By.tagName("label"));
                if (sendToText !=null && sendToText.getText().equals("Send to:") && sendToTextBox != null){
                    SimpleUtils.pass("Inbox: Send to: section display successfully");
                } else{
                    SimpleUtils.fail("Inbox: Send to text or textbox failed to load",false);
                }

            } else{
                SimpleUtils.fail("Inbox: Send to: section failed to load",false);
            }

            //Check Message
            String expectedTextInMessageSection = "Hi [Name],\n" +
                    "\n" +
                    "Below is the Good Faith Estimate showing a typical week's schedule for you at [store name]. Your actual work schedule may vary and you may ask for updates to your work schedule/availability.\n" +
                    "\n" +
                    "Please acknowledge that you have received this estimate.";
            String expectedTextInOperatingHoursSection= "Select Days to be Included in Good Faith Estimate"
                    + "Click on any Day & Scheduling Window to select and deselect working days."
                    + "Note that edits in the good faith estimate will not change the Legion suggested schedule.";
            String expectedTextOfVSL = "Team members will be informed regarding opting in to the Voluntary Standby List.";

            if (messageInCreateGFETemplate != null){
                if (messageInCreateGFETemplate.getAttribute("value").equals(expectedTextInMessageSection)){
                    SimpleUtils.pass("Inbox: Message in Create DFE page display correctly");
                }
                else{
                    SimpleUtils.fail("Inbox: Message in create GFE page display incorrectly, the expected message is: " + expectedTextInMessageSection
                            + "the actual message is: "+ messageInCreateGFETemplate.getAttribute("value"),false);
                }
            }
            else{
                SimpleUtils.fail("Inbox: Message in create GFE page failed to load",false);
            }

            if (isElementLoaded(textOfVSL)){

                if (textOfVSL.getText().equals(expectedTextOfVSL)){
                    SimpleUtils.pass("Inbox: VSL text in create GFR page display correctly");
                } else{
                    SimpleUtils.fail("Inbox: VSL text in create GFR page display incorrectly. The expected text is: " +expectedTextOfVSL
                            + " the actual text is: " + textOfVSL, false);
                }
            }

            if (isElementLoaded(operatingHoursSection)){
                WebElement operatingHoursSectionTitle = operatingHoursSection.findElement(By.tagName("h2"));
                List<WebElement> operatingHoursSectionMessages = operatingHoursSection.findElements(By.tagName("p"));

                if (operatingHoursSectionMessages != null && operatingHoursSectionMessages.size() >1){

                    String textInOperatingHoursSection = operatingHoursSectionTitle.getText()
                            + operatingHoursSectionMessages.get(0).getText()
                            + operatingHoursSectionMessages.get(1).getText();

                    if (textInOperatingHoursSection.equals(expectedTextInOperatingHoursSection)){
                        SimpleUtils.pass("Inbox: Text in operating hours section display correctly");
                    } else{
                        SimpleUtils.fail("Inbox: Text in operating hours section display incorrectly, the expected message is: \n" + expectedTextInOperatingHoursSection + "\n"
                                + "the actual message is: \n"+ textInOperatingHoursSection,false);
                    }
                }

                //check working days
                if (workingDays != null && workingDays.size()==7){
                    for (int i=0 ; i <7 ;i ++){
                        WebElement workingDay = workingDays.get(i);
                        WebElement weekDayElment = workingDay.findElement(By.cssSelector(".weekdays-column-header.ng-binding"));
                        String weekDay = "";
                        if (weekDay!=null){
                            weekDay = weekDayElment.getText();
                        } else{
                            SimpleUtils.fail("Inbox: Week days display incorrectly",false);
                        }
                        if (workingDay.getAttribute("class").contains("selected")){
                            SimpleUtils.pass("Inbox: Working day:  "+ weekDay + " display correctly and selected");
                        } else {
                            SimpleUtils.fail("Inbox: Working day " + weekDay + " display incorrectly",false);
                        }
                    }

                } else{
                    SimpleUtils.fail("Inbox: Working days failed to load",false);
                }

                //check Week Summary
                if (isElementLoaded(weekSummaryText)){
                    if (weekSummaryText.getText().equals("WEEK SUMMARY")){
                        SimpleUtils.pass("Inbox:  Week Summary text display correctly.");
                    } else{
                        SimpleUtils.fail("Inbox:  Week Summary text display incorrectly. The expected text is: WEEK SUMMARY, the actual text is :  " + weekSummaryText.getText(), true);
                    }
                } else {
                    SimpleUtils.fail("Inbox: Week Summary text failed to load",false);
                }

                if (areListElementVisible(weekSummaryItems) && weekSummaryItems.size()==3){
                    WebElement minimumShifts = weekSummaryItems.get(0);
                    if (minimumShifts != null){
                        if(minimumShifts.findElement(By.tagName("label")).getText().equals("Minimum Shifts")){
                            SimpleUtils.pass("Inbox:  Week Summary -> Minimum Shifts display correctly.");
                        } else{
                            SimpleUtils.fail("Inbox:  Week Summary -> Minimum Shifts display incorrectly. The expected text is Minimum Shifts, the actual text is "
                                    + minimumShifts.findElement(By.tagName("label")).getText(), false);
                        }
                    } else {
                        SimpleUtils.fail("Inbox: Week Summary -> Minimum Shifts failed to load",false);
                    }

                    WebElement averageHours = weekSummaryItems.get(1);
                    if (averageHours != null){
                        if(averageHours.findElement(By.tagName("label")).getText().equals("Average Hours")){
                            SimpleUtils.pass("Inbox:  Week Summary -> Average Hours display correctly.");
                        } else{
                            SimpleUtils.fail("Inbox:  Week Summary -> Average Hours display incorrectly. The expected text is Average Hours, the actual text is "
                                    + averageHours.findElement(By.tagName("label")).getText(), false);
                        }
                    } else{
                        SimpleUtils.fail("Inbox: Week Summary -> Average Hours failed to load",false);
                    }
                } else {
                    SimpleUtils.fail("Inbox: Week Summary items failed to load",false);
                }
            } else{
                SimpleUtils.fail("Inbox: Operating hours section in create GFE page failed to load",false);
            }
        } else{
            SimpleUtils.fail("Inbox: Create GFE page failed to load",false);
        }
    }

    @Override
    public void checkCreateAnnouncementPageWithGFETurnOnOrTurnOff(boolean isTurnOn) throws Exception {
        if (isElementLoaded(createAnnouncementIcon,10)) {
            clickTheElement(createAnnouncementIcon);
            if(isElementLoaded(newAnnouncementModal,5)){
                // check 'Create new announcement' text
                WebElement createNewAnnouncementText = newAnnouncementModal.findElement(By.tagName("h2"));
                if (createNewAnnouncementText != null && createNewAnnouncementText.getText().equals("Create new announcement")){
                    SimpleUtils.pass("Inbox: 'Create new announcement' text display successfully");
                } else{
                    SimpleUtils.fail("Inbox: 'Create new announcement' text failed to load",true);
                }

                //check Announcement Type
                if (isTurnOn){
                    if (announcementTypeSection != null){

                        //check Announcement type text
                        WebElement announcementTypeText = announcementTypeSection.findElement(By.tagName("label"));
                        if (announcementTypeText != null && announcementTypeText.getText().equals("Announcement type:")){
                            SimpleUtils.pass("Inbox: Announcement type text display correctly");
                        } else{
                            SimpleUtils.fail("Inbox: Announcement type text display incorrectly. The expected value is: Announcement type:, "
                                    + "the actual value is: " + announcementTypeText, true);
                        }

                        //check Announcement Type selector
                        if (announcementType !=null) {
                            Select dropdown = new Select(announcementType);
                            List<WebElement> allAnnouncementTypes = dropdown.getOptions();

                            if (allAnnouncementTypes != null && allAnnouncementTypes.size() > 1) {
                                if (allAnnouncementTypes.get(0).getText().equalsIgnoreCase("Message") && allAnnouncementTypes.get(1).getText().equalsIgnoreCase("Good Faith Estimate")) {
                                    SimpleUtils.pass("Inbox: Announcement type: Message & Good Faith Estimate shows correctly, Message is the first option, Good Faith Estimate is the second option.");
                                } else {
                                    SimpleUtils.fail("Inbox: Announcement type: Message & Good Faith Estimate shows incorrectly", false);
                                }
                            } else {
                                SimpleUtils.fail("Inbox: Announcement type: Message & Good Faith Estimate failed to load", false);
                            }
                        } else {
                            SimpleUtils.fail("Inbox: Announcement types selector failed to load", false);
                        }
                    } else{
                        SimpleUtils.fail("Inbox: Announcement type failed to load",false);
                    }
                } else {
                    if(!isElementLoaded(announcementTypeSection, 5)){
                        SimpleUtils.pass("Inbox: Announcement type is not displayed, because GFE is turned off");
                    } else{
                        SimpleUtils.fail("Inbox: Announcement type should not display, because GFE is turned off",false);
                    }
                }

                //check Send to:
                if (sendToSection !=null){
                    WebElement sendToText = sendToSection.findElement(By.tagName("label"));
                    if (sendToText !=null && sendToText.getText().equals("Send to:") && sendToTextBox != null){
                        SimpleUtils.pass("Inbox: Send to: section display successfully");
                    } else{
                        SimpleUtils.fail("Inbox: Send to text or textbox failed to load",false);
                    }

                } else{
                    SimpleUtils.fail("Inbox: Send to: section failed to load",false);
                }

                //check Message section
                if (messageText !=null && messageText.getText().equals("Message:") && titleArea !=null && messageArea != null){
                    SimpleUtils.pass("Inbox: Message section display successfully");
                } else{
                    SimpleUtils.fail("Inbox: Message section failed to load",false);
                }

                //check Cancel and Send button
                if(isElementLoaded(cancelButton, 5)){
                    SimpleUtils.pass("Inbox: Cancel button display successfully");
                } else{
                    SimpleUtils.fail("Inbox: Cancel button failed to load",false);
                }
                if (isElementLoaded(saveButton, 5)){
                    SimpleUtils.pass("Inbox: Save button display successfully");
                } else{
                    SimpleUtils.fail("Inbox: Save button failed to load",false);
                }
            } else{
                SimpleUtils.fail("Inbox: Create Announcement page failed to load",false);
            }
        } else
            SimpleUtils.fail("Inbox: Create Announcement + icon failed to load",false);

    }

    //Added by Haya
    @FindBy(css = ".gfe-send-to-select [ng-model=\"search\"]")
    private WebElement sendToInput;
    @FindBy(css = ".gfe-send-to-select .selector-input")
    private WebElement sendTo;
    @FindBy(css = ".gfe-send-to-select .selector-dropdown .selector-option")
    private List<WebElement> tmOptions;
    @Override
    public void sendToTM(String nickName) throws Exception {
        if (isElementLoaded(sendToInput,10) && isElementLoaded(sendTo, 10)){
            clickTheElement(sendTo);
            sendToInput.sendKeys(nickName);
            waitForSeconds(10);
            if (areListElementVisible(tmOptions, 10)) {
                for (WebElement tmOption : tmOptions) {
                    if (tmOption.getText().contains(nickName)) {
                        clickTheElement(tmOption);
                        waitForSeconds(1);
                        SimpleUtils.pass("GFE Announcement: Select " + nickName + " Successfully!");
                        break;
                    }
                }
            } else {
                SimpleUtils.report("GFE Announcement: Cannot find " + nickName + "!, try again");
                sendToInput.clear();
                sendToInput.sendKeys(nickName);
            }
        } else {
            SimpleUtils.fail("GFE Announcement: Send to element failed to load!", false);
        }
    }

    @FindBy(css = ".estimate-faith-area")
    private WebElement efaMessageArea;
    @Override
    public void changeTheMessage(String message) throws Exception {
        if (isElementLoaded(efaMessageArea.findElement(By.cssSelector("textarea")),5)){
            efaMessageArea.findElement(By.cssSelector("textarea")).clear();
            efaMessageArea.findElement(By.cssSelector("textarea")).sendKeys(message);
        } else {
            SimpleUtils.fail("Message textarea fail to load!", false);
        }
    }

    @FindBy(css = ".announcement-message.ng-binding")
    private WebElement messageViewed;
    @Override
    public void verifyMessageIsExpected(String messageExpected) throws Exception {
        if (isElementLoaded(messageViewed,5)){
            String s= messageViewed.getText();
            if (messageExpected.equals(messageViewed.getText())){
                SimpleUtils.pass("Message is consistent!");
            } else {
                SimpleUtils.fail("Message is inconsistent!",false);
            }
        } else {
            SimpleUtils.fail("Message fail to load!",false);
        }
    }

    @FindBy(css = ".weekdays-row .weekdays-column")
    private List<WebElement> allWorkingDays;
    //Parameters: day
    //e.g.: SUN,MON,TUE,WED,THU,FRI,SAT
    @Override
    public void chooseOneDayToClose(String day) throws Exception {
        selectAllWorkingDays();
        if (areListElementVisible(allWorkingDays,5)){
            for (WebElement element: allWorkingDays){
                if (element.findElement(By.cssSelector(".weekdays-column-header")).getText().contains(day)){
                    click(element.findElement(By.cssSelector(".weekdays-column-header")));
                    verifyDayIsNotSelected(element);
                    SimpleUtils.pass(day+" is closed!");
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Working days are not loaded!", false);
        }
    }

    private void selectAllWorkingDays() throws Exception{
        if (areListElementVisible(allWorkingDays,5)){
            for (WebElement element: allWorkingDays){
                if (!element.getAttribute("class").contains("selected")){
                    click(element.findElement(By.cssSelector(".weekdays-column-header")));
                }
            }
        } else {
            SimpleUtils.fail("Working days are not loaded!", false);
        }
    }

    private void verifyDayIsNotSelected(WebElement day){
        if (!day.getAttribute("class").contains("selected")){
            SimpleUtils.pass("Operating day is unselected!");
        }
    }

    @FindBy(css = ".working-days .working-days-day")
    private List<WebElement> actualWorkingDays;
    @Override
    public void verifyDayIsClosed(String day) throws Exception {
        if (areListElementVisible(actualWorkingDays,5)){
            for (WebElement element: actualWorkingDays){
                if (element.findElement(By.cssSelector(".working-days-day-title")).getText().contains(day)){
                    if (!element.getAttribute("class").contains("working-days-day--blue")){
                        SimpleUtils.pass(day+" is closed!");
                    } else {
                        SimpleUtils.fail(day+" should be closed!", false);
                    }
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Working days are not loaded in view mode!", false);
        }
    }

    @Override
    public void changeOperatingHrsOfDay(String day, String startTime, String endTime) throws Exception{
        if (areListElementVisible(allWorkingDays,5)){
            for (WebElement element: allWorkingDays){
                if (element.findElement(By.cssSelector(".weekdays-column-header")).getText().contains(day)){
                    if (isElementLoaded(element.findElement(By.cssSelector(".weekdays-column-body")),5) && element.findElements(By.cssSelector(".weekdays-column-body input")).size()==2){
                        element.findElements(By.cssSelector(".weekdays-column-body input")).get(0).clear();
                        element.findElements(By.cssSelector(".weekdays-column-body input")).get(0).sendKeys(startTime);
                        element.findElements(By.cssSelector(".weekdays-column-body input")).get(1).clear();
                        element.findElements(By.cssSelector(".weekdays-column-body input")).get(1).sendKeys(endTime);
                        SimpleUtils.pass("start time and end time are set");
                    } else {
                        SimpleUtils.fail("Start time and end time are not loaded!", false);
                    }
                    SimpleUtils.pass(day+" is closed!");
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Working days are not loaded!", false);
        }
    }

    @FindBy (css = ".weekdays-column.selected")
    private List<WebElement> selectedDays;

    @Override
    public List<String> getSelectedOperatingHours() throws Exception {
        List<String> selectedOperatingHours = new ArrayList<>();
        if (areListElementVisible(selectedDays, 5)) {
            for (WebElement selectedDay : selectedDays) {
                WebElement weekDay = selectedDay.findElement(By.cssSelector(".weekdays-column-header"));
                List<WebElement> startNEndTime = selectedDay.findElements(By.tagName("input"));
                if (weekDay != null && startNEndTime != null && startNEndTime.size() == 2) {
                    String fullName = SimpleUtils.getFullWeekDayName(weekDay.getText());
                    SimpleDateFormat twentyFour = new SimpleDateFormat("H:mm");
                    SimpleDateFormat twelve = new SimpleDateFormat("hh:mma");
                    Date startTime = twelve.parse(startNEndTime.get(0).getAttribute("value"));
                    Date endTime = twelve.parse(startNEndTime.get(1).getAttribute("value"));
                    String oneDayTime = fullName + ": " + twentyFour.format(startTime) + "-" + twentyFour.format(endTime);
                    selectedOperatingHours.add(oneDayTime);
                    SimpleUtils.pass("Inbox - GFE: get the operating hour: " + oneDayTime + " Successfully!");
                }
            }
            if (selectedOperatingHours.size() != selectedDays.size()) {
                SimpleUtils.fail("Inbox - GFE: Failed to get the correct operating hours!", false);
            }
        } else {
            SimpleUtils.report("Inbox - GFE: There are no selected working days!");
        }
        return selectedOperatingHours;
    }

    @Override
    public void verifyOperatingHrsOfDay(String dayExpected, String startTimeExpected, String endTimeExpected) throws Exception {
        if (areListElementVisible(actualWorkingDays,5)){
            for (WebElement element: actualWorkingDays){
                if (element.findElement(By.cssSelector(".working-days-day-title")).getText().contains(dayExpected)){
                    if (element.findElement(By.cssSelector(".working-days-day-work-time")).getText().split("\n").length>1){
                        String actualStartTime = element.findElement(By.cssSelector(".working-days-day-work-time")).getText().split("\n")[0];
                        String actualEndTime = element.findElement(By.cssSelector(".working-days-day-work-time")).getText().split("\n")[1];
                        if (getTimeFormat(startTimeExpected).contains(getTimeFormat(actualStartTime)) && getTimeFormat(endTimeExpected).contains(getTimeFormat(actualEndTime))){
                            SimpleUtils.pass(dayExpected+": Operating hours is consistent with setting!");
                        }else {
                            SimpleUtils.fail(dayExpected+": Operating hours is inconsistent with setting!", false);
                        }
                        break;
                    } else {
                        SimpleUtils.fail("start time or end time fail to load!", false);
                    }
                }
            }
        } else {
            SimpleUtils.fail("Working days are not loaded!", false);
        }
    }

    //09:00AM-->9:00am
    private String getTimeFormat(String time) throws Exception{
        String result = time.substring(0,2);
        if (time.contains("AM") | time.contains("am")){
            result = result.concat(":00am");
        } else {
            result = result.concat(":00pm");
        }
        if (result.indexOf("0")==0){
            result = result.substring(1);
        }
        return result.replace("::", ":");
    }

    @FindBy(css = ".week-summary")
    private WebElement weekSummarySection;
    @Override
    public void changeWeekSummaryInfo(String minimumShifts, String averageHrs) throws Exception {
        if (isElementLoaded(weekSummarySection,5)){
            weekSummarySection.findElement(By.id("gfe-min-shifts")).clear();
            weekSummarySection.findElement(By.id("gfe-min-shifts")).sendKeys(minimumShifts);
            weekSummarySection.findElement(By.id("gfe-average-hours")).clear();
            weekSummarySection.findElement(By.id("gfe-average-hours")).sendKeys(averageHrs);
            SimpleUtils.pass("minimum shifts and average hours are set!");
        } else {
            SimpleUtils.fail("Week summary section is not loaded!", false);
        }
    }

    @FindBy(css = "[type=\"'success'\"] button")
    private WebElement sendBtn;
    @Override
    public void clickSendBtn() throws Exception {
        if (isElementLoaded(sendBtn,10)){
            scrollToBottom();
            click(sendBtn);
            SimpleUtils.pass("Send button has been clicked!");
        } else {
            SimpleUtils.fail("Send button is not loaded!", false);
        }
    }

    @FindBy(css = ".announcement-object")
    private List<WebElement> announcements;
    @Override
    public void clickFirstGFEInList() throws Exception {
        if (areListElementVisible(announcements,5)){
            click(announcements.get(0));
            SimpleUtils.pass("The first announcement in the list has been chosen!");
        } else {
            SimpleUtils.fail("There is no announcement in the lists!", false);
        }
    }

    @FindBy(css = ".acknowledge-and-vsl-btns button")
    private WebElement acknowledgeBtn;
    @Override
    public void clickAcknowledgeBtn() throws Exception {
        if (isElementLoaded(acknowledgeBtn,10)){
            click(acknowledgeBtn);
            SimpleUtils.pass("Acknowledge button has been clicked!");
        } else {
            SimpleUtils.fail("Acknowledge button fail to load!", false);
        }
    }

    @FindBy(css = ".announcement-comments-form")
    private WebElement commentForm;
    @Override
    public void addComment(String comment) throws Exception {
        if (isElementLoaded(commentForm,5)){
            commentForm.findElement(By.cssSelector("input")).clear();
            commentForm.findElement(By.cssSelector("input")).sendKeys(comment);
            scrollToElement(commentForm.findElement(By.cssSelector(".invite-icon")));
            clickTheElement(commentForm.findElement(By.cssSelector(".invite-icon")));
            SimpleUtils.pass("Comment is added!");
        } else {
            SimpleUtils.fail("Comment form is not loaded!", false);
        }
    }

    @FindBy(css = ".announcement-comments-list-comment")
    private List<WebElement> comments;
    @Override
    public void verifyComment(String comment, String name) throws Exception {
        boolean flag = false;
        if (areListElementVisible(comments,5)){
            for(WebElement element: comments){
                if (element.findElement(By.tagName("b")).getText().contains(name) && element.getText().contains(comment)){
                    SimpleUtils.pass("Comment displays correctly!");
                    flag = true;
                    break;
                }
            }
            if (!flag){
                SimpleUtils.fail("There is no comment expected!", false);
            }
        }
    }

    @FindBy(css = ".acknowledge-and-vsl-btns .acknowledge-and-vsl-btns-vsl-btn")
    private WebElement vslTooltip;
    @Override
    public void verifyVSLTooltip() throws Exception {
        String expectedTooltip = "You can get more hours by opting in to the Voluntary Standby List! This will enable the team to call you when more help is needed. To opt in please go to your Work Preferences. ";
        if (isElementLoaded(vslTooltip,5) && expectedTooltip.contains(vslTooltip.getAttribute("data-tootik"))){
            SimpleUtils.pass("There is right tooltip with VSL.");
        } else {
            SimpleUtils.fail("There is no tooltip for VSL!", false);
        }
    }


    @FindBy(css = "div[class=\"row announcements-list ng-scope\"]")
    private WebElement announcementListPanel;
    @Override
    public boolean isAnnouncementListPanelDisplay() throws Exception {
        boolean isAnnouncementListPanelDisplay = false;
        try{
            if(isElementLoaded(announcementListPanel, 5)) {
                isAnnouncementListPanelDisplay = true;
                SimpleUtils.report("Announcement List Panel is loaded Successfully!");
            } else
                SimpleUtils.report("Announcement List Panel not loaded Successfully!");
        } catch(Exception e){
            SimpleUtils.fail(e.getMessage(), false);
        }
        return isAnnouncementListPanelDisplay;
    }

}
