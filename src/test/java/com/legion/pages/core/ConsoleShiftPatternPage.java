package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.ShiftPatternPage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleShiftPatternPage extends BasePage implements ShiftPatternPage {

    public ConsoleShiftPatternPage() {
        PageFactory.initElements(getDriver(), this);
    }

    public enum sectionType {
        WorkRole("Work Role"),
        ShiftName("Shift Name"),
        Description("Description"),
        StartTime("Start Time"),
        EndTime("End Time"),
        Days("Select Day(s)"),
        Breaks("Breaks"),
        ShiftNotes("Shift Notes");
        private final String type;
        sectionType(final String newType){
            type = newType;
        }
        public String getType(){
            return type;
        }
    }

    @FindBy (css = "content-box:nth-child(1) .lg-sub-content-box-title")
    private WebElement shiftPatternDetailsTitle;
    @FindBy (css = "[aria-label=Role]")
    private WebElement roleInput;
    @FindBy (css = "[aria-label=Name]")
    private WebElement nameInput;
    @FindBy (css = "[aria-label=Description]")
    private WebElement descriptionInput;
    @FindBy (css = "[aria-label=\"No. of instances\"]")
    private WebElement instanceInput;
    @FindBy (css = "[label=\"Start Date\"] span")
    private WebElement selectStartDateButton;
    @FindBy (css = ".add-week-btn")
    private WebElement addWeekButton;
    @FindBy (css = "content-box:nth-child(3) .lg-sub-content-box-title")
    private WebElement badgesTitle;
    @FindBy (css = ".badges-edit-wrapper .buttonLabel")
    private List<WebElement> badgesButtons;
    @FindBy (css = ".current-day")
    private WebElement currentDay;
    @FindBy (css = ".lg-modal [type=submit]")
    private WebElement okBtnOnSelectStartDate;
    @FindBy (css = "[label=\"Start Date\"] span:nth-child(2)")
    private WebElement selectedDateLabel;
    @FindBy (css = ".week-type-dropdown-menu li")
    private List<WebElement> onOffWeekOptions;
    @FindBy (css = ".week-title")
    private List<WebElement> weekTitles;
    @FindBy (css = ".auto-schedule input")
    private WebElement autoScheduleCheckbox;
    @FindBy (css = "[label=\"Auto schedule team members during off weeks\"]>label")
    private WebElement autoScheduleLabel;
    @FindBy (css = ".auto-schedule-title")
    private WebElement autoScheduleTitle;
    @FindBy (css = ".lg-checkbox-group input")
    private List<WebElement> eachDayCheckboxes;
    @FindBy (css = ".lg-checkbox-group input-field>label")
    private List<WebElement> eachDayLabels;
    @FindBy (css =".settings-shift-pattern-week-details-edit-expanded")
    private List<WebElement> expandedIcons;
    @FindBy (css = ".settings-shift-pattern-week-details-edit-collapsed")
    private List<WebElement> collapsedIcons;
    @FindBy (css = ".fa-times")
    private List<WebElement> deleteWeekBtns;
    @FindBy (css = ".fa-chevron-down")
    private List<WebElement> chevronDownBtns;
    @FindBy (css = ".fa-chevron-up")
    private List<WebElement> chevronUpBtns;
    @FindBy (css = "[ng-click*=\"addOrEditShift\"] span")
    private List<WebElement> addShiftBtns;
    @FindBy (css = "[modal-title=\"Create New Shift\"]")
    private WebElement createNewShiftWindow;
    @FindBy (css = "h6+div>.MuiGrid-container>div:nth-child(2)")
    private List<WebElement> columnValueContainers;
    @FindBy (css = "h6+div>.MuiGrid-container>div:nth-child(1)")
    private List<WebElement> columnTitleContainers;
    @FindBy (css = "[ng-click=\"close()\"]")
    private WebElement cancelOnPopup;
    @FindBy (css = "[ng-click=\"submit()\"]")
    private WebElement createOnPopup;
    @FindBy (css = ".MuiFormHelperText-root")
    private List<WebElement> warningMsgs;
    @FindBy (css = "[style=\"border-top: none;\"] div")
    private List<WebElement> breakWarnings;
    @FindBy (css = "h6+div>.MuiGrid-container>div:nth-child(2)")
    private List<WebElement> sections;
    @FindBy (id = "legion_cons_Schedule_Schedule_CreateShift_ShiftName_field")
    private WebElement shiftNameInput;
    @FindBy (id = "shiftDescription")
    private WebElement descriptionInputOnNewShfit;
    @FindBy (id = "legion_cons_Schedule_Schedule_CreateChift_ShiftNotes_field")
    private WebElement shiftNotesInput;
    @FindBy (id = "legion_cons_Schedule_Schedule_CreateShift_ShiftStart_field")
    private WebElement startTimeInput;
    @FindBy (id = "legion_cons_Schedule_Schedule_CreateShift_ShiftEnd_field")
    private WebElement endTimeInput;
    @FindBy (css = "[name*=\"selectedDays\"]")
    private List<WebElement> dayInputs;
    @FindBy (css = ".MuiCheckbox-root+span")
    private List<WebElement> dayLabels;
    @FindBy (css = ".shift-info")
    private WebElement onWeekContent;
    @FindBy(css = "div.settings-work-rule-save-icon")
    private WebElement checkMarkButton;
    @FindBy(css = ".settings-work-rule-staffing-string-format")
    private WebElement staffingRuleContent;
    @FindBy (css = "[id*=\"legion_cons_Schedule_Schedule_CreateShift\"] .MuiCheckbox-root+span")
    private List<WebElement> dayLabelsForCreation;

    private WebElement getSpecificSectionByName(String name) throws Exception {
        if (name.equalsIgnoreCase(sectionType.WorkRole.getType())) {
            return sections.get(0);
        }
        if (name.equalsIgnoreCase(sectionType.ShiftName.getType())) {
            return sections.get(1);
        }
        if (name.equalsIgnoreCase(sectionType.Description.getType())) {
            return sections.get(2);
        }
        if (name.equalsIgnoreCase(sectionType.StartTime.getType())) {
            return sections.get(3);
        }
        if (name.equalsIgnoreCase(sectionType.EndTime.getType())) {
            return sections.get(4);
        }
        if (name.equalsIgnoreCase(sectionType.Days.getType())) {
            return sections.get(5);
        }
        if (name.equalsIgnoreCase(sectionType.Breaks.getType())) {
            return sections.get(6);
        }
        if (name.equalsIgnoreCase(sectionType.ShiftNotes.getType())) {
            return sections.get(7);
        }
        return null;
    }

    @Override
    public boolean verifyTheEditedValuePersist(String shiftName, String description, String startTime, String endTime,
                                               List<String> selectedDays, int mealStartOffset, int mealDuration,
                                               int restStartOffset, int restDuration, String shiftNote) throws Exception {
        boolean isPersist = true;
        waitForSeconds(2);
        if (isElementEnabled(shiftNameInput, 5)) {
            if (!shiftNameInput.getAttribute("value").equalsIgnoreCase(shiftName) || !descriptionInput.getAttribute("value")
                    .equalsIgnoreCase(description) || !startTimeInput.getAttribute("value").equalsIgnoreCase(startTime) ||
                    !endTimeInput.getAttribute("value").equalsIgnoreCase(endTime) || !shiftNotesInput.getAttribute("value").equalsIgnoreCase(shiftNote)) {
                isPersist = false;
            }
        } else {
            SimpleUtils.report("Create New Shift dialog is not loaded");
        }
        for (int i = 0; i < dayLabels.size(); i++) {
            if (selectedDays.contains(dayLabels.get(i).getText())) {
                if (!dayInputs.get(i).getAttribute("checked").equalsIgnoreCase("true")) {
                    isPersist = false;
                    break;
                }
            } else {
                if (dayInputs.get(i).getAttribute("checked") != null && dayInputs.get(i).getAttribute("checked").equalsIgnoreCase("true")) {
                    isPersist = false;
                    break;
                }
            }
        }
        WebElement breakSection = getSpecificSectionByName(sectionType.Breaks.getType());
        List<WebElement> mealAndRest = breakSection.findElements(By.cssSelector(".MuiGrid-grid-xs-true"));
        WebElement mealSection = mealAndRest.get(0);
        WebElement restSection = mealAndRest.get(1);
        List<WebElement> mealInputs = mealSection.findElements(By.tagName("input"));
        List<WebElement> restInputs = restSection.findElements(By.tagName("input"));
        if (!mealInputs.get(0).getAttribute("value").equalsIgnoreCase(String.valueOf(mealStartOffset)) || !mealInputs
                .get(1).getAttribute("value").equalsIgnoreCase(String.valueOf(mealDuration)) || !restInputs.get(0).
                getAttribute("value").equalsIgnoreCase(String.valueOf(restStartOffset)) || !restInputs
                .get(1).getAttribute("value").equalsIgnoreCase(String.valueOf(restDuration))) {
            isPersist = false;
        }
        return isPersist;
    }

    @Override
    public String getOnWeekContentDetails() throws Exception {
        if (isElementLoaded(onWeekContent, 3)) {
            return onWeekContent.getText();
        }
        return "";
    }

    @Override
    public void clickOnAddMealOrRestBreakBtn(boolean isMeal) throws Exception {
        WebElement breakSection = getSpecificSectionByName(sectionType.Breaks.getType());
        WebElement addButton = null;
        int index = 0;
        if (isMeal) {
            index = 1;
        } else {
            index = 2;
        }
        addButton = getDriver().findElements(By.cssSelector(".add-break-button-title")).get(index - 1);
        clickTheElement(addButton);
        if (areListElementVisible(breakSection.findElements(By.cssSelector("div>div:nth-child(" + index + ") input")),
                3) && isElementLoaded(breakSection.findElement(By.cssSelector("div>div:nth-child(" + index + ") td>svg")), 3)) {
            SimpleUtils.pass("Click on Add break button successfully!");
        } else {
            SimpleUtils.fail("Break Section: inputs and close button failed to show!", false);
        }
    }

    @Override
    public void deleteTheBreakByNumber(boolean isMeal, int number) throws Exception {
        WebElement breakSection = getSpecificSectionByName(sectionType.Breaks.getType());
        List<WebElement> mealAndRest = breakSection.findElements(By.cssSelector(".MuiGrid-grid-xs-true"));
        WebElement section = null;
        if (isMeal) {
            section = mealAndRest.get(0);
        } else {
            section = mealAndRest.get(1);
        }
        List<WebElement> deleteButtons = section.findElements(By.cssSelector("td>svg"));
        if (deleteButtons.size() >= number) {
            deleteButtons.get(number - 1).click();
        }
    }

    @Override
    public void inputShiftOffsetAndBreakDuration(int startOffset, int breakDuration, int number, boolean isMeal) throws Exception {
        WebElement breakSection = getSpecificSectionByName(sectionType.Breaks.getType());
        List<WebElement> mealAndRest = breakSection.findElements(By.cssSelector(".MuiGrid-grid-xs-true"));
        WebElement section = null;
        if (isMeal) {
            section = mealAndRest.get(0);
        } else {
            section = mealAndRest.get(1);
        }
        List<WebElement> inputs = section.findElements(By.tagName("input"));
        if (number == 1) {
            if (inputs.size() == 0) {
                clickOnAddMealOrRestBreakBtn(isMeal);
                waitForSeconds(1);
                inputs = section.findElements(By.tagName("input"));
            }
            clearTheText(inputs.get(0));
            inputs.get(0).click();
            inputs.get(0).sendKeys(String.valueOf(startOffset));
            clearTheText(inputs.get(1));
            inputs.get(1).click();
            inputs.get(1).sendKeys(String.valueOf(breakDuration));
        }
        if (number == 2) {
            if (inputs.size() == 2) {
                clickOnAddMealOrRestBreakBtn(isMeal);
            }
            clearTheText(inputs.get(2));
            inputs.get(2).click();
            inputs.get(2).sendKeys(String.valueOf(startOffset));
            clearTheText(inputs.get(3));
            inputs.get(3).click();
            inputs.get(3).sendKeys(String.valueOf(breakDuration));
        }
    }

    @Override
    public void clickOnPencilIcon(int weekNumber) throws Exception {
        WebElement weekPanel = getDriver().findElement(By.cssSelector(".lg-rule-details content-box:nth-child(" + (weekNumber + 1) + ")"));
        WebElement pencilBtn = weekPanel.findElement(By.cssSelector(".fa-pencil"));
        if (isElementLoaded(pencilBtn, 3)) {
            clickTheElement(pencilBtn);
        }
    }

    @Override
    public String getTheNumberOfTheShifts(int weekNumber) throws Exception {
        WebElement weekPanel = getDriver().findElement(By.cssSelector(".lg-rule-details content-box:nth-child(" + (weekNumber + 1) + ")"));
        return weekPanel.findElement(By.cssSelector(".week-shift-summary-info")).getText();
    }

    @Override
    public void clickOnDeleteBtnToDelCreatedShifts(int weekNumber) throws Exception {
        WebElement weekPanel = getDriver().findElement(By.cssSelector(".lg-rule-details content-box:nth-child(" + (weekNumber + 1) + ")"));
        clickTheElement(weekPanel.findElement(By.cssSelector("[icon=\"'fa-pencil'\"]+lg-button i")));
    }

    @Override
    public void verifySaveTheShiftPatternRule() throws Exception {
        if (isElementEnabled(checkMarkButton, 5)) {
            clickTheElement(checkMarkButton);

        }
    }

    @Override
    public boolean isCreateNewShiftWindowLoaded() throws Exception {
        boolean isLoaded = false;
        try {
            if (isElementLoaded(createNewShiftWindow, 5)) {
                isLoaded = true;
            }
        } catch (Exception e) {
            // Do nothing
        }
        return isLoaded;
    }

    @Override
    public List<String> selectWorkDays(List<String> daysNeedSelect) throws Exception {
        List<String> selectedDays = new ArrayList<>();
        if (daysNeedSelect != null && daysNeedSelect.size() > 0 && areListElementVisible(dayInputs, 3) && areListElementVisible(dayLabelsForCreation, 3)) {
            for (int i = 0; i < dayLabelsForCreation.size(); i++) {
                if (daysNeedSelect.contains(dayLabelsForCreation.get(i).getText())) {
                    if (dayInputs.get(i).getAttribute("checked") == null) {
                        clickTheElement(dayInputs.get(i));
                    }
                    selectedDays.add(dayLabelsForCreation.get(i).getText().substring(0, 3));
                }
            }
        }
        return selectedDays;
    }

    @Override
    public void inputStartOrEndTime(String hours, String minutes, String aOrP, boolean isStart) throws Exception {
        WebElement input = null;
        if (isStart) {
            input = startTimeInput;
        } else {
            input = endTimeInput;
        }
        clickTheElement(input);
        //clearTheText(input);
        waitForSeconds(1);
        clearTheText(input);
        input.sendKeys(hours);
        input.sendKeys(Keys.TAB);
        input.sendKeys(minutes);
        input.sendKeys(Keys.TAB);
        input.sendKeys(aOrP);
        input.sendKeys(Keys.TAB);
        selectWorkDays(new ArrayList<>(Arrays.asList("Sunday")));
        selectWorkDays(new ArrayList<>(Arrays.asList("Sunday")));
        waitForSeconds(5);
        if (!input.getAttribute("value").contains(hours) && !input.getAttribute("value").contains(minutes) &&
        !input.getAttribute("value").contains(aOrP)) {
            SimpleUtils.fail("Failed to input the time!", false);
        }
    }

    @Override
    public void verifyWorkRoleNameShows(String workRole) throws Exception {
        WebElement workRoleElement = getSpecificSectionByName(sectionType.WorkRole.getType());
        if (!workRoleElement.getText().equalsIgnoreCase(workRole)) {
            SimpleUtils.fail("The work role on Create New Shift window is incorrect!", false);
        }
    }

    @Override
    public void inputShiftNameDescriptionNShiftNotes(String shiftName, String description, String shiftNotes) throws Exception {
        if (shiftName != null && !shiftName.isEmpty()) {
            shiftNameInput.click();
            clearTheText(shiftNameInput);
            shiftNameInput.sendKeys(shiftName);
            if (!shiftNameInput.getAttribute("value").equalsIgnoreCase(shiftName)) {
                SimpleUtils.fail("Failed to input the shift name!", false);
            }
        }
        if (description != null && !description.isEmpty()) {
            descriptionInputOnNewShfit.click();
            clearTheText(descriptionInputOnNewShfit);
            descriptionInputOnNewShfit.sendKeys(description);
            if (!descriptionInputOnNewShfit.getAttribute("value").equalsIgnoreCase(description)) {
                SimpleUtils.fail("Failed to input the description!", false);
            }
        }
        if (shiftNotes != null && !shiftNotes.isEmpty()) {
            shiftNotesInput.click();
            clearTheText(shiftNotesInput);
            shiftNotesInput.sendKeys(shiftNotes);
            if (!shiftNotesInput.getAttribute("value").equalsIgnoreCase(shiftNotes)) {
                SimpleUtils.fail("Failed to input the shift Notes!", false);
            }
        }
    }

    @Override
    public void clickOnCancelButton() throws Exception {
        clickTheElement(cancelOnPopup);
    }

    @Override
    public void clickOnCreateButton() throws Exception {
        clickTheElement(createOnPopup);
    }

    @Override
    public List<String> getWarningMessages() throws Exception {
        List<String> warningMessages = new ArrayList<>();
        if (areListElementVisible(warningMsgs, 5)) {
            for (WebElement warning : warningMsgs) {
                warningMessages.add(warning.getText());
            }
        }
        return warningMessages;
    }

    @Override
    public List<String> getBreakWarnings() throws Exception {
        List<String> warningMessages = new ArrayList<>();
        if (areListElementVisible(breakWarnings, 5)) {
            for (WebElement warning : breakWarnings) {
                warningMessages.add(warning.getText());
            }
        }
        return warningMessages;
    }

    @Override
    public void verifyTheContentOnCreateNewShiftWindow() throws Exception {
        if (isElementLoaded(createNewShiftWindow, 3) && areListElementVisible(columnTitleContainers, 3)
        && areListElementVisible(columnValueContainers, 3) && columnTitleContainers.size() == 8 &&
        columnValueContainers.size() == 8 && columnTitleContainers.get(0).getText().equalsIgnoreCase(sectionType.WorkRole.getType())
        && columnTitleContainers.get(1).getText().equalsIgnoreCase(sectionType.ShiftName.getType()) &&
                columnTitleContainers.get(2).getText().equalsIgnoreCase(sectionType.Description.getType()) &&
                columnTitleContainers.get(3).getText().equalsIgnoreCase(sectionType.StartTime.getType()) &&
                columnTitleContainers.get(4).getText().equalsIgnoreCase(sectionType.EndTime.getType()) &&
                columnTitleContainers.get(5).getText().equalsIgnoreCase(sectionType.Days.getType()) &&
                columnTitleContainers.get(6).getText().equalsIgnoreCase(sectionType.Breaks.getType()) &&
                columnTitleContainers.get(7).getText().equalsIgnoreCase(sectionType.ShiftNotes.getType()) &&
        isElementLoaded(cancelOnPopup, 3) && isElementLoaded(createOnPopup, 3)) {
            SimpleUtils.pass("The content on Create New Shift window is correct!");
        } else {
            SimpleUtils.fail("The content on Create New Shift window is incorrect!", false);
        }
    }

    @Override
    public void verifyTheContentOnShiftPatternDetails(String workRole) throws Exception {
        if (isElementLoaded(shiftPatternDetailsTitle, 3) && shiftPatternDetailsTitle.getText().equalsIgnoreCase("Shift Pattern Details")
        && isElementLoaded(roleInput, 3) && roleInput.getAttribute("value").equalsIgnoreCase(workRole) &&
        isElementLoaded(nameInput, 3) && isElementLoaded(descriptionInput, 3) && isElementLoaded(instanceInput, 3)
        && isElementLoaded(selectStartDateButton, 3) && isElementLoaded(addWeekButton, 3) &&
        isElementLoaded(badgesTitle, 3) && badgesTitle.getText().equalsIgnoreCase("Badges") &&
        areListElementVisible(badgesButtons)) {
            SimpleUtils.pass("The content on Shift Pattern details loaded successfully!");
        } else {
            SimpleUtils.fail("The content on Shift Pattern details are incorect!", false);
        }
    }

    @Override
    public void verifyTheMandatoryFields() throws Exception {
        if (nameInput.getAttribute("required") != null && instanceInput.getAttribute("required") != null &&
                nameInput.getAttribute("required").equalsIgnoreCase("true") && instanceInput.getAttribute("required")
        .equalsIgnoreCase("true")) {
            SimpleUtils.pass("Name and No. of Instances are mandatory fields!");
        } else {
            SimpleUtils.fail("Name and No. of Instances should be the mandatory fields!", false);
        }
    }

    @Override
    public void inputNameDescriptionNInstances(String name, String description, String instances) throws Exception {
        if (name != null && !name.isEmpty()) {
            nameInput.sendKeys(name);
            if (nameInput.getAttribute("value").equalsIgnoreCase(name)) {
                SimpleUtils.pass("Input name successfully!");
            } else {
                SimpleUtils.fail("Failed to input the name!", false);
            }
        }
        if (description != null && !description.isEmpty()) {
            descriptionInput.sendKeys(description);
            if (descriptionInput.getAttribute("value").equalsIgnoreCase(description)) {
                SimpleUtils.pass("Input Description successfully!");
            } else {
                SimpleUtils.fail("Failed to input the Description!", false);
            }
        }
        if (instances != null && !instances.isEmpty()) {
            instanceInput.sendKeys(instances);
            if (instanceInput.getAttribute("value").equalsIgnoreCase(instances)) {
                SimpleUtils.pass("Input instances successfully!");
            } else {
                SimpleUtils.fail("Failed to input the instances!", false);
            }
        }
    }

    @Override
    public String selectTheCurrentWeek() throws Exception {
        String selectedFirstDayOfWeek = "";
        clickTheElement(selectStartDateButton);
        if (isElementLoaded(currentDay, 3) && isElementLoaded(okBtnOnSelectStartDate, 3)) {
            clickTheElement(currentDay);
            clickTheElement(okBtnOnSelectStartDate);
        }
        if (isElementLoaded(selectedDateLabel, 5)) {
            selectedFirstDayOfWeek = selectedDateLabel.getText();
            SimpleUtils.report("Selected the first day of week: " + selectedFirstDayOfWeek + " successfully!");
        }
        return selectedFirstDayOfWeek;
    }

    @Override
    public void selectAddOnOrOffWeek(boolean isOn) throws Exception {
        if (isElementLoaded(addWeekButton, 3)) {
            clickTheElement(addWeekButton);
            if (areListElementVisible(onOffWeekOptions, 3)) {
                if (isOn) {
                    clickTheElement(onOffWeekOptions.get(0));
                } else {
                    clickTheElement(onOffWeekOptions.get(1));
                }
                if (areListElementVisible(weekTitles, 3)) {
                    SimpleUtils.pass("Select the week option successfully!");
                } else {
                    SimpleUtils.fail("Failed to click on the week option!", false);
                }
            } else {
                SimpleUtils.fail("On Week/Off Week options not loaded!", false);
            }
        } else {
            SimpleUtils.fail("+ Add Week button failed to load!", false);
        }
    }

    @Override
    public void verifyTheContentOnOffWeekSection() throws Exception {
        if (isElementLoaded(autoScheduleCheckbox, 3) && isElementLoaded(autoScheduleLabel, 3) &&
        autoScheduleLabel.getText().equalsIgnoreCase("Auto schedule team members during off weeks")) {
            SimpleUtils.pass("The content on Off Week section is correct!");
        } else {
            SimpleUtils.fail("The content on Off Week section is incorrect!", false);
        }
    }

    @Override
    public void verifyTheContentOnOnWeekSection() throws Exception {
        if (areListElementVisible(expandedIcons, 3) && areListElementVisible(chevronUpBtns, 3) &&
        areListElementVisible(deleteWeekBtns, 3) && areListElementVisible(addShiftBtns, 3)) {
            SimpleUtils.pass("The content on On Week section is correct!");
        } else {
            SimpleUtils.fail("The content on On Week section is incorrect!", false);
        }
    }

    @Override
    public void checkOrUnCheckAutoSchedule(boolean isCheck) throws Exception {
        if (isElementLoaded(autoScheduleCheckbox, 3)) {
            if (isCheck) {
                if (autoScheduleCheckbox.getAttribute("class").contains("ng-empty")) {
                    clickTheElement(autoScheduleCheckbox);
                    if (autoScheduleCheckbox.getAttribute("class").contains("ng-not-empty")) {
                        SimpleUtils.pass("Check the Auto schedule option successfully!");
                    } else {
                        SimpleUtils.fail("Failed to check the auto schedule option!", false);
                    }
                }
            } else {
                if (autoScheduleCheckbox.getAttribute("class").contains("ng-not-empty")) {
                    clickTheElement(autoScheduleCheckbox);
                    if (autoScheduleCheckbox.getAttribute("class").contains("ng-empty")) {
                        SimpleUtils.pass("Uncheck the Auto schedule option successfully!");
                    } else {
                        SimpleUtils.fail("Failed to uncheck the auto schedule option!", false);
                    }
                }
            }
        }
    }

    @Override
    public void checkOrUnCheckSpecificDay(boolean isCheck, String day) throws Exception {
        if (areListElementVisible(eachDayCheckboxes, 3) && areListElementVisible(eachDayLabels, 3)) {
            for (int i = 0; i < eachDayLabels.size(); i++) {
                if (day.equalsIgnoreCase(eachDayLabels.get(i).getText())) {
                    if (isCheck) {
                        if (eachDayCheckboxes.get(i).getAttribute("class").contains("ng-empty")) {
                            clickTheElement(eachDayCheckboxes.get(i));
                            if (!day.equalsIgnoreCase("Everyday")) {
                                if (eachDayCheckboxes.get(i).getAttribute("class").contains("ng-not-empty")) {
                                    SimpleUtils.pass("Check the day: " + eachDayLabels.get(i).getText() + " successfully!");
                                } else {
                                    SimpleUtils.fail("Failed to Check the day: " + eachDayLabels.get(i).getText(), false);
                                }
                            } else {
                                for (WebElement checkbox : eachDayCheckboxes) {
                                    if (checkbox.getAttribute("class").contains("ng-empty")) {
                                        SimpleUtils.fail("Failed to check the everyday option!", false);
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        if (eachDayCheckboxes.get(i).getAttribute("class").contains("ng-not-empty")) {
                            clickTheElement(eachDayCheckboxes.get(i));
                            if (!day.equalsIgnoreCase("Everyday")) {
                                if (eachDayCheckboxes.get(i).getAttribute("class").contains("ng-empty")) {
                                    SimpleUtils.pass("Uncheck the day: " + eachDayLabels.get(i).getText() + " successfully!");
                                } else {
                                    SimpleUtils.fail("Failed to uncheck the day: " + eachDayLabels.get(i).getText(), false);
                                }
                            } else {
                                for (WebElement checkbox : eachDayCheckboxes) {
                                    if (checkbox.getAttribute("class").contains("ng-not-empty")) {
                                        SimpleUtils.fail("Failed to uncheck the everyday option!", false);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        } else {
            SimpleUtils.fail("Each day checkboxes and labels failed to load!", false);
        }
    }

    @Override
    public void verifyTheFunctionalityOfExpandWeekIcon(int weekNumber, boolean isExpanded) throws Exception {
        int index = weekNumber - 1;
        WebElement weekPanel = getDriver().findElement(By.cssSelector(".lg-rule-details content-box:nth-child(" + (weekNumber + 1) + ")"));
        if (isExpanded) {
            if (isElementLoaded(weekPanel.findElement(By.cssSelector(".settings-shift-pattern-week-details-edit-collapsed")), 3)) {
                clickTheElement(weekPanel.findElement(By.cssSelector(".settings-shift-pattern-week-details-edit-collapsed")));
                if (isElementLoaded(weekPanel.findElement(By.cssSelector(".settings-shift-pattern-week-details-edit-expanded")),3)) {
                    SimpleUtils.pass("Expand the week section for Week" + weekNumber);
                } else {
                    SimpleUtils.fail("Failed to expand the week section for Week" + weekNumber, false);
                }
            }
        } else {
            if (isElementLoaded(weekPanel.findElement(By.cssSelector(".settings-shift-pattern-week-details-edit-expanded")), 3)) {
                clickTheElement(weekPanel.findElement(By.cssSelector(".settings-shift-pattern-week-details-edit-expanded")));
                if (isElementLoaded(weekPanel.findElement(By.cssSelector(".settings-shift-pattern-week-details-edit-collapsed")), 3)) {
                    SimpleUtils.pass("Collapse the week section for Week" + weekNumber);
                } else {
                    SimpleUtils.fail("Failed to collapse the week section for Week" + weekNumber, false);
                }
            }
        }
    }

    @Override
    public void verifyTheFunctionalityOfArrowIcon(int weekNumber, boolean isExpanded) throws Exception {
        int index = weekNumber - 1;
        if (isExpanded) {
            if (isElementLoaded(chevronDownBtns.get(index), 3)) {
                clickTheElement(chevronDownBtns.get(index));
                if (isElementLoaded(chevronUpBtns.get(index))) {
                    SimpleUtils.pass("Expand the week section for Week" + weekNumber);
                } else {
                    SimpleUtils.fail("Failed to expand the week section for Week" + weekNumber, false);
                }
            }
        } else {
            if (isElementLoaded(chevronUpBtns.get(index), 3)) {
                clickTheElement(chevronUpBtns.get(index));
                if (isElementLoaded(chevronDownBtns.get(index))) {
                    SimpleUtils.pass("Collapse the week section for Week" + weekNumber);
                } else {
                    SimpleUtils.fail("Failed to collapse the week section for Week" + weekNumber, false);
                }
            }
        }
    }

    @Override
    public int getWeekCount() throws Exception {
        if (areListElementVisible(weekTitles, 3)) {
            return weekTitles.size();
        } else {
            return 0;
        }
    }

    @Override
    public void deleteTheWeek(int weekNumber) throws Exception {
        if (areListElementVisible(deleteWeekBtns, 3)) {
            clickTheElement(deleteWeekBtns.get(weekNumber - 1));
        } else {
            SimpleUtils.fail("There is no delete week buttons!", false);
        }
    }

    @Override
    public void clickOnAddShiftButton() throws Exception {
        if (areListElementVisible(addShiftBtns, 3)) {
            clickTheElement(addShiftBtns.get(0));
        } else {
            selectAddOnOrOffWeek(true);
            clickOnAddShiftButton();
        }
        if (isElementLoaded(createNewShiftWindow, 3)) {
            SimpleUtils.pass("Click on + Add Shift button successfullt!");
        } else {
            SimpleUtils.fail("Create New Shift windows failed to load!", false);
        }
    }
}
