package com.legion.pages.core.schedule;

import com.legion.pages.BasePage;
import com.legion.pages.EditShiftPage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleEditShiftPage extends BasePage implements EditShiftPage {
    public ConsoleEditShiftPage() {
        PageFactory.initElements(getDriver(), this);
    }

    public enum sectionType {
        WorkRole("Work Role"),
        ShiftName("Shift Name"),
        Location("Location"),
        StartTime("Start Time"),
        EndTime("End Time"),
        Date("Date"),
        Assignment("Assignment"),
        ShiftNotes("Shift Notes"),
        Breaks("Breaks");
        private final String type;
        sectionType(final String newType){
            type = newType;
        }
        public String getType(){
            return type;
        }
    }

    public enum twoOptions {
        AllowConflicts("allowConflicts"),
        AllowComplianceErrors("allowComplianceErrors");

        private final String option;
        twoOptions(final String specificOption) {
            option = specificOption;
        }
        public String getOption() {
            return option;
        }
    }

    public enum assignmentOptions {
        DoNotChangeAssignments("Do not change assignments"),
        OpenShift("Open Shift: Auto Offer to TMs"),
        AssignOrOffer("Assign or Offer to Specific TM's");

        private final String option;
        assignmentOptions(final String specificOption) {
            option = specificOption;
        }
        public String getOption() {
            return option;
        }
    }

    @FindBy (css = ".modal-content")
    private WebElement editShiftWindow;
    @FindBy (css = ".modal-instance-header-title")
    private WebElement windowTitle;
    @FindBy (css = "#edit-shift-react form>div:nth-child(1)>div")
    private WebElement subTitle;
    @FindBy (css = ".generate-modal-location")
    private WebElement locationInfo;
    @FindBy (css = ".tma-dismiss-button")
    private WebElement xButton;
    @FindBy (css = "[ng-click*=back]")
    private WebElement cancelButton;
    @FindBy (css = "button#legion_cons_Schedule_Schedule_EditShifts_Update_button")
    private WebElement updateButton;
    @FindBy (css = "#edit-shift-react form>div:nth-child(3)>div")
    private WebElement optionsSection;
    @FindBy (css = "[data-testid=ReplayIcon]")
    private WebElement clearEditedFieldsBtn;
    @FindBy (css = ".MuiGrid-container")
    private List<WebElement> gridContainers;
    @FindBy (css = ".react-select__option")
    private List<WebElement> dropDownListOnReact;
    @FindBy (css = ".MuiInputAdornment-positionEnd")
    private WebElement nextDayIcon;
    @FindBy (css="[role=tooltip] input")
    private WebElement nextDayPopup;
    @FindBy (name = "allowComplianceErrors")
    private WebElement allowComplianceOption;
    @FindBy (name = "allowConflicts")
    private WebElement allowConflictsOption;

    @Override
    public boolean isEditShiftWindowLoaded() throws Exception {
        waitForSeconds(10);
        if (isElementLoaded(editShiftWindow, 25) && areListElementVisible(gridContainers, 25)) {
            return true;
        }
        return false;
    }

    @Override
    public void verifyTheTitleOfEditShiftsWindow(int selectedShiftCount, String startOfWeek) throws Exception {
        String expectedTitle = "Edit " + selectedShiftCount + " Shift" + (selectedShiftCount == 0 || selectedShiftCount == 1 ? "" : "s") + ": Week of "
                + startOfWeek;
        if (isElementLoaded(windowTitle, 5) && windowTitle.getText().trim().equalsIgnoreCase(expectedTitle)) {
            SimpleUtils.pass("The title of the Edit Shift window is correct: " + expectedTitle);
        } else {
            SimpleUtils.fail("The title of the Edit Shift window is incorrect!", false);
        }
    }

    @Override
    public void verifySelectedWorkDays(int selectedShitCount, List<String> selectedDays) throws Exception {
        String selectedDaysString = "";
        if (selectedDays.size() == 2) {
            if (selectedDays.get(0) == selectedDays.get(1)) {
                selectedDaysString = selectedDays.get(0);
            } else {
               selectedDaysString = selectedDays.toString().substring(selectedDays.toString().indexOf("[") + 1, selectedDays.toString().indexOf("]"));
            }
        } else if (selectedDays.size() == 1) {
            selectedDaysString = selectedDays.get(0);
        }
        String expectedSubTitle = "SHIFTS SELECTED: " + selectedShitCount + "\n" + "Days: " + selectedDaysString;
        if (isElementLoaded(subTitle, 3) && subTitle.getText().equalsIgnoreCase(expectedSubTitle)) {
            SimpleUtils.pass("The sub title of the Edit Shift window is correct: " + expectedSubTitle);
        } else {
            SimpleUtils.fail("The sub title of the Edit Shift window is incorrect!, Actual: '" + subTitle.getText()
                    + "', Expected: '" + expectedSubTitle + "'", false);
        }
    }

    @Override
    public void verifyLocationNameShowsCorrectly(String locationName) throws Exception {
        if (isElementLoaded(locationInfo, 3) && locationInfo.getText().trim().equalsIgnoreCase(locationName)) {
            SimpleUtils.pass("Location Name shows on the Edit Shift window!");
        } else {
            SimpleUtils.fail("Location Name is incorrect on Edit Shift window!", false);
        }
    }

    @Override
    public void verifyTheVisibilityOfButtons() throws Exception {
        if (isElementLoaded(xButton, 3) && isElementLoaded(cancelButton, 3) && isElementLoaded(updateButton)) {
            SimpleUtils.pass("x, CANCEL, UPDATE buttons loaded successfully on Edit Shift Window!");
        } else {
            SimpleUtils.fail("x, CANCEL, UPDATE buttons failed to load on Edit Shift Window!", false);
        }
    }

    @Override
    public void verifyTheContentOfOptionsSection() throws Exception {
        if (isElementLoaded(optionsSection, 3) && optionsSection.getText().contains(
                "Your bulk edit may result in some shifts incurring a compliance violation or scheduling conflict.") &&
        optionsSection.getText().contains("Allow shift edits that result in scheduling conflicts. Existing shifts will convert to open.")
        && optionsSection.getText().contains("Allow shift edits that result in compliance violations such as overtime.")) {
            SimpleUtils.pass("The content on options section is correct!");
        } else {
            SimpleUtils.fail("The content on options section is incorrect!", false);
        }
    }

    @Override
    public boolean isClearEditedFieldsBtnLoaded() throws Exception {
        if (isElementLoaded(clearEditedFieldsBtn, 3)) {
            return true;
        }
        return false;
    }

    @Override
    public void verifyTwoColumns() throws Exception {
        if (areListElementVisible(gridContainers, 3) && gridContainers.size() > 0) {
            List<WebElement> columns = gridContainers.get(0).findElements(By.cssSelector(".MuiGrid-root"));
            if (columns.size() == 2) {
                SimpleUtils.pass("The columns are correct");
            } else {
                SimpleUtils.fail("The columns on Edit Shift window is incorrect!", false);
            }
        } else {
            SimpleUtils.fail("The content failed to load on Edit Shift window!", false);
        }
    }

    @Override
    public void verifyEditableTypesShowOnShiftDetail() throws Exception {
        if (areListElementVisible(gridContainers, 3)) {
            if (gridContainers.size() == 7) {
                if (gridContainers.get(0).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Work Role")
                && gridContainers.get(5).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Name")
                && gridContainers.get(1).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Start Time")
                && gridContainers.get(2).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("End Time")
                && gridContainers.get(3).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Date")
                && gridContainers.get(4).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Assignment")
                && gridContainers.get(6).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Notes")) {
                    SimpleUtils.pass("'Work Role', 'Shift Name', 'Start Time', 'End Time', 'Date', 'Assignment', 'Shift Notes' " +
                            "sections are loaded successfully!");
                } else {
                    SimpleUtils.fail("Sections on Shift Details is incorrect!", false);
                }
            } else if (gridContainers.size() == 8) {
                if (gridContainers.get(0).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Work Role")
                        && gridContainers.get(6).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Name")
                        && gridContainers.get(1).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Location")
                        && gridContainers.get(2).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Start Time")
                        && gridContainers.get(3).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("End Time")
                        && gridContainers.get(4).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Date")
                        && gridContainers.get(5).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Assignment")
                        && gridContainers.get(7).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Notes")) {
                    SimpleUtils.pass("'Work Role', 'Shift Name', 'Location', 'Start Time', 'End Time', 'Date', 'Assignment', 'Shift Notes' " +
                            "sections are loaded successfully for Location Group!");
                } else {
                    SimpleUtils.fail("Sections on Shift Details is incorrect!", false);
                }
            }
        } else {
            SimpleUtils.fail("Shift Details section failed to load!", false);
        }
    }

    @Override
    public void clickOnXButton() throws Exception {
        if (isElementLoaded(xButton, 1)) {
            clickTheElement(xButton);
        } else {
            SimpleUtils.fail("X button failed to load!", false);
        }
    }

    @Override
    public void clickOnCancelButton() throws Exception {
        if (isElementLoaded(cancelButton, 1)) {
            clickTheElement(cancelButton);
        } else {
            SimpleUtils.fail("Cancel button failed to load!", false);
        }
    }

    @Override
    public void verifyTheTextInCurrentColumn(String type, String value) throws Exception {
        WebElement gridRow = null;
        if (areListElementVisible(gridContainers, 3)) {
            if (gridContainers.size() == 7) {
                if (type.equals(sectionType.WorkRole.getType())) {
                    gridRow = gridContainers.get(1);
                } else if (type.equals(sectionType.ShiftName.getType())) {
                    gridRow = gridContainers.get(2);
                } else if (type.equals(sectionType.StartTime.getType())) {
                    gridRow = gridContainers.get(3);
                } else if (type.equals(sectionType.EndTime.getType())) {
                    gridRow = gridContainers.get(4);
                } else if (type.equals(sectionType.Date.getType())) {
                    gridRow = gridContainers.get(5);
                } else if (type.equals(sectionType.Assignment.getType())) {
                    gridRow = gridContainers.get(6);
                } else if (type.equals(sectionType.ShiftNotes.getType())) {
                    gridRow = gridContainers.get(7);
                }
            } else if (gridContainers.size() == 8) {
                if (type.equals(sectionType.WorkRole.getType())) {
                    gridRow = gridContainers.get(1);
                } else if (type.equals(sectionType.Location.getType())) {
                    gridRow = gridContainers.get(2);
                } else if (type.equals(sectionType.ShiftName.getType())) {
                    gridRow = gridContainers.get(3);
                } else if (type.equals(sectionType.StartTime.getType())) {
                    gridRow = gridContainers.get(4);
                } else if (type.equals(sectionType.EndTime.getType())) {
                    gridRow = gridContainers.get(5);
                } else if (type.equals(sectionType.Date.getType())) {
                    gridRow = gridContainers.get(6);
                } else if (type.equals(sectionType.Assignment.getType())) {
                    gridRow = gridContainers.get(7);
                } else if (type.equals(sectionType.ShiftNotes.getType())) {
                    gridRow = gridContainers.get(8);
                }
            }
            if (gridRow.findElement(By.cssSelector(".MuiGrid-item:nth-child(2)")).getText().trim().equalsIgnoreCase(value)) {
                SimpleUtils.pass("Verified for " + type + ", the value is correct!");
            } else {
                SimpleUtils.fail("Verified for " + type + ", the value is incorrect! Expected: " + value +
                        ", But actual is: " + gridRow.findElement(By.cssSelector(".MuiGrid-item:nth-child(2)")).getText().trim(), false);
            }
        } else {
            SimpleUtils.fail("Shift Details section failed to load!", false);
        }
    }

    @Override
    public List<String> getOptionsFromSpecificSelect() throws Exception {
        List<String> options = new ArrayList<>();
        if (areListElementVisible(dropDownListOnReact, 5)) {
            for (WebElement option : dropDownListOnReact) {
                options.add(option.getText().toLowerCase());
            }
        } else {
            SimpleUtils.fail("Options failed to load!", false);
        }
        return options;
    }

    @Override
    public void clickOnDateSelect() throws Exception {
        WebElement dateSection = getSpecificElementByTypeAndColumn(sectionType.Date.getType(), "Edited");
        scrollToElement(dateSection);
        waitForSeconds(1);
        if (isElementLoaded(dateSection, 5)) {
            moveToElementAndClick(dateSection.findElement(By.cssSelector(".react-select__value-container")));
            if (dropDownListOnReact.size() > 0) {
                SimpleUtils.pass("Click on Date select successfully!");
            } else {
                SimpleUtils.fail("Failed to click on date select!", false);
            }
        } else {
            SimpleUtils.fail("Date section on Edited column failed to load!", false);
        }
    }

    @Override
    public void clickOnWorkRoleSelect() throws Exception {
        WebElement workRoleSection = getSpecificElementByTypeAndColumn(sectionType.WorkRole.getType(), "Edited");
        if (isElementLoaded(workRoleSection, 5)) {
            moveToElementAndClick(workRoleSection.findElement(By.cssSelector(".react-select__indicators")));
        } else {
            SimpleUtils.fail("Work Role section on Edited column failed to load!", false);
        }
    }

    @Override
    public void clickOnLocationSelect() throws Exception {
        WebElement locationSection = getSpecificElementByTypeAndColumn(sectionType.Location.getType(), "Edited");
        if (isElementLoaded(locationSection, 5)) {
            moveToElementAndClick(locationSection.findElement(By.cssSelector(".react-select__indicators")));
        } else {
            SimpleUtils.fail("Location section on Edited column failed to load!", false);
        }
    }

    @Override
    public void clickOnAssignmentSelect() throws Exception {
        WebElement assignmentSection = getSpecificElementByTypeAndColumn(sectionType.Assignment.getType(), "Edited");
        if (isElementLoaded(assignmentSection, 5)) {
            moveToElementAndClick(assignmentSection.findElement(By.cssSelector(".react-select__indicators")));
        } else {
            SimpleUtils.fail("Assignment section on Edited column failed to load!", false);
        }
    }

    @Override
    public void selectSpecificOptionByText(String text) throws Exception {
        selectOptionByLabel(text);
    }

    @Override
    public String getSelectedWorkRole() throws Exception {
        String selectedWorkRole = "";
        WebElement editedWorkRoleSection = getSpecificElementByTypeAndColumn(sectionType.WorkRole.getType(), "Edited");
        if (editedWorkRoleSection != null) {
            selectedWorkRole = editedWorkRoleSection.getText();
        }
        return selectedWorkRole;
    }

    @Override
    public String getSelectedDate() throws Exception {
        String selectedDate = "";
        WebElement editedDateSection = getSpecificElementByTypeAndColumn(sectionType.Date.getType(), "Edited");
        if (editedDateSection != null) {
            selectedDate = editedDateSection.getText();
        }
        return selectedDate;
    }

    @Override
    public String getSelectedAssignment() throws Exception {
        String assignment = "";
        WebElement assignmentSection = getSpecificElementByTypeAndColumn(sectionType.Assignment.getType(), "Edited");
        if (assignmentSection != null) {
            assignment = assignmentSection.getText();
        }
        return assignment;
    }

    @Override
    public void clickOnClearEditedFieldsButton() throws Exception {
        clickTheElement(clearEditedFieldsBtn.findElement(By.xpath("./..")));
    }

    @Override
    public void clickOnUpdateButton() throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(updateButton, 5)) {
            clickTheElement(updateButton);
        }
    }

    @Override
    public void inputShiftName(String shiftName) throws Exception {
        WebElement shiftNameInputSection = getSpecificElementByTypeAndColumn(sectionType.ShiftName.getType(), "Edited");
        WebElement input = shiftNameInputSection.findElement(By.cssSelector("[placeholder=\"Shift Name (Optional)\"]"));
        input.clear();
        input.sendKeys(shiftName);
        if (input.getAttribute("value").equals(shiftName)) {
            SimpleUtils.pass("Input the string in Shift Name successfully!");
        } else {
            SimpleUtils.fail("Input the string in Shift Name failed!", false);
        }
    }

    @Override
    public void inputShiftNotes(String shiftNote) throws Exception {
        WebElement shiftNotesSection = getSpecificElementByTypeAndColumn(sectionType.ShiftNotes.getType(), "Edited");
        WebElement input = shiftNotesSection.findElement(By.name("notes"));
        input.clear();
        input.sendKeys(shiftNote);
        if (input.getAttribute("value").equals(shiftNote)) {
            SimpleUtils.pass("Input the string in Shift Notes successfully!");
        } else {
            SimpleUtils.fail("Input the string in Shift Notes failed!", false);
        }
    }

    @Override
    public void inputStartOrEndTime(String time, boolean isStartTime) throws Exception {
        WebElement timeSection = null;
        if (isStartTime) {
            timeSection = getSpecificElementByTypeAndColumn(sectionType.StartTime.getType(), "Edited");
        } else {
            timeSection = getSpecificElementByTypeAndColumn(sectionType.EndTime.getType(), "Edited");
        }
        WebElement input = timeSection.findElement(By.cssSelector("[placeholder*=\"Time\"]"));
        input.click();
        input.sendKeys(Keys.CONTROL, "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(time);
//        waitForSeconds(15);
        WebElement shiftNameInputSection = getSpecificElementByTypeAndColumn(sectionType.ShiftName.getType(), "Edited");
        WebElement shiftNameInput = shiftNameInputSection.findElement(By.cssSelector("[placeholder=\"Shift Name (Optional)\"]"));
        shiftNameInput.click();
        if (input.getAttribute("value").toLowerCase().contains(time.toLowerCase())) {
            SimpleUtils.pass("Input the string in Time successfully!");
        } else {
            SimpleUtils.fail("Input the string in Time failed!", false);
        }
    }

    @Override
    public void checkUseOffset(boolean isStartTimeSection, boolean check) throws Exception {
        WebElement timeSection = null;
        if (isStartTimeSection) {
            timeSection = getSpecificElementByTypeAndColumn(sectionType.StartTime.getType(), "Edited");
        } else {
            timeSection = getSpecificElementByTypeAndColumn(sectionType.EndTime.getType(), "Edited");
        }
        WebElement checkbox = timeSection.findElement(By.cssSelector("[type=checkbox]"));
        WebElement parent = checkbox.findElement(By.xpath("./.."));
        if (check) {
            if (!parent.getAttribute("class").contains("Mui-checked")) {
                clickTheElement(checkbox);
                if (areListElementVisible(timeSection.findElements(By.cssSelector(".MuiFormControl-root")), 3) &&
                        isElementLoaded(timeSection.findElement(By.cssSelector(".react-select__control")), 3)) {
                    SimpleUtils.pass("Check on Use Offset button successfully!");
                } else {
                    SimpleUtils.fail("Failed to check on Use Offset button!", false);
                }
            }
        } else {
            if (parent.getAttribute("class").contains("Mui-checked")) {
                clickTheElement(checkbox);
                if (isElementLoaded(timeSection.findElement(By.cssSelector("[placeholder*=\"Time\"]")), 3)) {
                    SimpleUtils.pass("Uncheck on Use Offset button successfully!");
                } else {
                    SimpleUtils.fail("Failed to uncheck on Use Offset button!", false);
                }
            }
        }
    }

    @Override
    public void verifyTheFunctionalityOfOffsetTime(String hours, String mins, String earlyOrLate, boolean isStartTimeSection) throws Exception {
        WebElement timeSection = null;
        if (isStartTimeSection) {
            timeSection = getSpecificElementByTypeAndColumn(sectionType.StartTime.getType(), "Edited");
        } else {
            timeSection = getSpecificElementByTypeAndColumn(sectionType.EndTime.getType(), "Edited");
        }
        WebElement hoursInput = timeSection.findElements(By.cssSelector("[type=number]")).get(0);
        WebElement minsInput = timeSection.findElements(By.cssSelector("[type=number]")).get(1);
        WebElement select = timeSection.findElement(By.cssSelector(".react-select__dropdown-indicator"));

        if (earlyOrLate != null && !earlyOrLate.isEmpty()) {
            select.click();
            selectOptionByLabel(earlyOrLate);
        }

        hoursInput.click();
        for (int i = 0; i < 4; i++) {
            hoursInput.sendKeys(Keys.BACK_SPACE);
        }
        minsInput.click();
        for (int i = 0; i < 4; i++) {
            minsInput.sendKeys(Keys.BACK_SPACE);
        }
        if (hours != null && !hours.isEmpty()) {
            hoursInput.sendKeys(hours);
            if (Integer.parseInt(hours) >= 12) {
                clickTheElement(updateButton);
                WebElement warning = timeSection.findElement(By.cssSelector(".MuiFormHelperText-root"));
                SimpleUtils.assertOnFail("Warning message 'Max 12 Hrs' failed to show!'", warning.getText()
                        .trim().equalsIgnoreCase("Max 12 Hrs"), false);
            }
        }
        if (mins != null && !mins.isEmpty()) {
            minsInput.sendKeys(mins);
            if (Integer.parseInt(mins) != 0 && Integer.parseInt(mins) != 15 && Integer.parseInt(mins) != 45 && Integer.parseInt(mins) != 60) {
                hoursInput.click();
                hoursInput.clear();
                clickTheElement(updateButton);
                WebElement warning = timeSection.findElement(By.cssSelector(".MuiFormHelperText-root"));
                SimpleUtils.assertOnFail("Warning message 'Must match slots' failed to show!'", warning.getText()
                        .trim().equalsIgnoreCase("Must match slots"), false);
            }
        }
    }

    @Override
    public void checkOrUnCheckNextDayOnBulkEditPage(boolean isCheck) throws Exception {
        if (isCheck) {
            if (isElementLoaded(nextDayIcon, 10)) {
                if (nextDayIcon.findElement(By.tagName("svg")).getAttribute("height").equals("16")) {
                    moveElement(nextDayIcon, 0);
                    moveToElementAndClick(nextDayPopup);
                    if (nextDayIcon.findElement(By.tagName("svg")).getAttribute("height").equals("8")) {
                        SimpleUtils.pass("The next day checkbox been checked successfully! ");
                    } else
                        SimpleUtils.fail("The next day checkbox been checked fail! ", false);
                } else
                    SimpleUtils.pass("The next day checkbox already checked! ");
            } else
                SimpleUtils.fail("The next day img fail to load! ", false);

        } else {
            if (isElementLoaded(nextDayIcon, 10)) {
                if (nextDayIcon.findElement(By.tagName("svg")).getAttribute("height").equals("8")) {
                    moveElement(nextDayIcon, 0);
                    clickTheElement(nextDayPopup);
                    if (nextDayIcon.findElement(By.tagName("svg")).getAttribute("height").equals("16")) {
                        SimpleUtils.pass("The next day checkbox been unchecked successfully! ");
                    } else
                        SimpleUtils.fail("The next day checkbox been unchecked fail! ", false);
                } else
                    SimpleUtils.pass("The next day checkbox already unchecked! ");
            } else
                SimpleUtils.report("The next day img is not loaded! ");
        }
    }

    @Override
    public void checkOrUncheckOptionsByName(String optionName, boolean isCheck) throws Exception {
        WebElement option = null;
        if (optionName.equals(twoOptions.AllowConflicts.getOption())) {
            option = allowConflictsOption;
        } else {
            option = allowComplianceOption;
        }
        WebElement parent = option.findElement(By.xpath("./.."));
        if (isCheck) {
            if (parent != null) {
                if (!parent.getAttribute("class").contains("Mui-checked")) {
                    clickTheElement(option);
                    if (parent.getAttribute("class").contains("Mui-checked")) {
                        SimpleUtils.pass("Check the option successfully!");
                    } else {
                        SimpleUtils.fail("Failed to check the option!", false);
                    }
                } else {
                    SimpleUtils.report("The option is already checked!");
                }
            }
        } else {
            if (parent != null) {
                if (parent.getAttribute("class").contains("Mui-checked")) {
                    clickTheElement(option);
                    if (!parent.getAttribute("class").contains("Mui-checked")) {
                        SimpleUtils.pass("UnCheck the option successfully!");
                    } else {
                        SimpleUtils.fail("Failed to uncheck the option!", false);
                    }
                } else {
                    SimpleUtils.report("The option is already unchecked!");
                }
            }
        }
    }

    private WebElement getSpecificElementByTypeAndColumn(String type, String column) throws Exception {
        WebElement element = null;
        String selector = "";
        if (column.equals("Value")) {
            selector = ".MuiGrid-item:nth-child(1)";
        } else if (column.equals("Edited")) {
            selector = ".MuiGrid-item:nth-child(2)";
        }
        int editShiftCount = Integer.parseInt(windowTitle.getText().trim().split(" ")[1]);
        if (editShiftCount==1){
            if (areListElementVisible(gridContainers, 3)) {
                if (gridContainers.size() == 8) {
                    if (type.equals(sectionType.WorkRole.getType())) {
                        element = gridContainers.get(0).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Breaks.getType())) {
                        element = gridContainers.get(1).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.StartTime.getType())) {
                        element = gridContainers.get(2).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.EndTime.getType())) {
                        element = gridContainers.get(3).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Date.getType())) {
                        element = gridContainers.get(4).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Assignment.getType())) {
                        element = gridContainers.get(5).findElement(By.cssSelector(selector));
                    } else if(type.equals(sectionType.ShiftName.getType())) {
                        element = gridContainers.get(6).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.ShiftNotes.getType())) {
                        element = gridContainers.get(7).findElement(By.cssSelector(selector));
                    }
                } else if (gridContainers.size() == 9) {
                    if (type.equals(sectionType.WorkRole.getType())) {
                        element = gridContainers.get(0).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Breaks.getType())) {
                        element = gridContainers.get(1).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Location.getType())) {
                        element = gridContainers.get(2).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.StartTime.getType())) {
                        element = gridContainers.get(3).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.EndTime.getType())) {
                        element = gridContainers.get(4).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Date.getType())) {
                        element = gridContainers.get(5).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Assignment.getType())) {
                        element = gridContainers.get(6).findElement(By.cssSelector(selector));
                    } else if(type.equals(sectionType.ShiftName.getType())) {
                        element = gridContainers.get(7).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.ShiftNotes.getType())) {
                        element = gridContainers.get(8).findElement(By.cssSelector(selector));
                    }
                } else if (gridContainers.size() == 10) {
                    if (type.equals(sectionType.WorkRole.getType())) {
                        element = gridContainers.get(0).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Breaks.getType())) {
                        element = gridContainers.get(1).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Location.getType())) {
                        element = gridContainers.get(3).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.StartTime.getType())) {
                        element = gridContainers.get(4).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.EndTime.getType())) {
                        element = gridContainers.get(5).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Date.getType())) {
                        element = gridContainers.get(6).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Assignment.getType())) {
                        element = gridContainers.get(7).findElement(By.cssSelector(selector));
                    } else if(type.equals(sectionType.ShiftName.getType())) {
                        element = gridContainers.get(8).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.ShiftNotes.getType())) {
                        element = gridContainers.get(9).findElement(By.cssSelector(selector));
                    }
                }
            } else {
                SimpleUtils.fail("Shift Details section failed to load!", false);
            }
        } else{
            if (areListElementVisible(gridContainers, 3)) {
                if (gridContainers.size() == 7) {
                    if (type.equals(sectionType.WorkRole.getType())) {
                        element = gridContainers.get(0).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.ShiftName.getType())) {
                        element = gridContainers.get(5).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.StartTime.getType())) {
                        element = gridContainers.get(1).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.EndTime.getType())) {
                        element = gridContainers.get(2).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Date.getType())) {
                        element = gridContainers.get(3).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Assignment.getType())) {
                        element = gridContainers.get(4).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.ShiftNotes.getType())) {
                        element = gridContainers.get(6).findElement(By.cssSelector(selector));
                    }
                } else if (gridContainers.size() == 8) {
                    if (type.equals(sectionType.WorkRole.getType())) {
                        element = gridContainers.get(0).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.ShiftName.getType())) {
                        element = gridContainers.get(6).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Location.getType())) {
                        element = gridContainers.get(1).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.StartTime.getType())) {
                        element = gridContainers.get(2).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.EndTime.getType())) {
                        element = gridContainers.get(3).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Date.getType())) {
                        element = gridContainers.get(4).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.Assignment.getType())) {
                        element = gridContainers.get(5).findElement(By.cssSelector(selector));
                    } else if (type.equals(sectionType.ShiftNotes.getType())) {
                        element = gridContainers.get(7).findElement(By.cssSelector(selector));
                    }
                }
            } else {
                SimpleUtils.fail("Shift Details section failed to load!", false);
            }
        }

        return element;
    }

    @FindBy (css = "#edit-shift-react form>div:nth-child(1)>div")
    private WebElement shiftInfoCard;
    @FindBy (css = "#edit-shift-react form>div:nth-child(1)>div span.MuiTypography-caption")
    private WebElement employeeInfo;
    @Override
    public void verifyShiftInfoCard(List<String> shiftInfo) throws Exception {
        if (isElementLoaded(shiftInfoCard, 3)) {
            SimpleUtils.pass("The shift info card of the Single Edit Shift window is loaded! ");
            String firstName = shiftInfo.get(0);
            String lastName = shiftInfo.get(5);
            String workRole = shiftInfo.get(4);
            String jobTitle = shiftInfo.get(3);
            String shiftTime = shiftInfo.get(6);
//            String shiftHours = shiftInfo.get(8);

            //Check the Avatar is display
            WebElement avatar = shiftInfoCard.findElement(By.cssSelector(".MuiAvatar-circular [alt=\""+firstName+" "+ lastName +"\"]"));
            if (isElementLoaded(avatar, 3)) {
                SimpleUtils.pass("The avatar on shift info card loaded successfully! ");
            } else
                SimpleUtils.fail("The avatar on shift info card fail to load! ", false);
            //Check TM name, job title, work role
            if (isElementLoaded(employeeInfo, 3)) {
                SimpleUtils.pass("The section of employee info loaded successfully! ");
                String employeeAllInfo = employeeInfo.getText();
                String employeeFirstName = employeeAllInfo.split("\\(")[0].trim().split(" ")[0].trim();
                String shiftWorkRole = employeeAllInfo.substring(employeeAllInfo.indexOf("(")+1, employeeAllInfo.indexOf(")")).trim();
                String employeeJobTitle = employeeAllInfo.split("\\n")[employeeAllInfo.split("\\n").length-1].trim();
                //https://legiontech.atlassian.net/browse/SCH-8165
//                if (employeeFirstName.equalsIgnoreCase(firstName)
//                        && shiftWorkRole.equalsIgnoreCase(workRole)
//                        && employeeJobTitle.equalsIgnoreCase(jobTitle)) {
//                    SimpleUtils.pass("The employee name, work role, job title display correctly on shift info card! ");
//                } else
//                    SimpleUtils.fail("The expected info are: "
//                            + firstName+ ", "
//                            + workRole+ ", "
//                            + jobTitle+ ". The actual are:"
//                            + employeeFirstName + ", "
//                            + shiftWorkRole + ", "
//                            + employeeJobTitle, false);
                List<WebElement> shiftTimeAndHrs = shiftInfoCard.findElements(By.tagName("p"));
                if (areListElementVisible(shiftTimeAndHrs, 3)&& shiftTimeAndHrs.size() == 2) {
                    String shiftTimeOnShiftCard = shiftTimeAndHrs.get(0).getText();
                    String shiftHrsOnShiftCard = shiftTimeAndHrs.get(1).getText();
                    //Check shift time
                    //Check shift hours
                    //https://legiontech.atlassian.net/browse/SCH-8164
//                    if (shiftTimeOnShiftCard.replace(" ", "").replace("–","").equalsIgnoreCase(shiftTime.replace(" ", "").replace("-", ""))
//                            && shiftHrsOnShiftCard.equalsIgnoreCase(shiftHours)) {
//                        SimpleUtils.pass("The shift time and shift hours display correctly on shift info card! ");
//                    } else
//                        SimpleUtils.fail("The expected info are: "
//                                + shiftTime+ " "
//                                + shiftHours+" The actual are:"
//                                + shiftTimeOnShiftCard + " "
//                                + shiftHrsOnShiftCard, false);
                }

            } else
                SimpleUtils.fail("The section of employee info fail to load! ", false);


        } else {
            SimpleUtils.fail("The shift info card of the Single Edit Shift window is fail to load! ", false);
        }
    }

    @Override
    public void verifyTheContentOfOptionsSectionIsNotLoaded () throws Exception {
        if (!isElementLoaded(optionsSection, 3)) {
            SimpleUtils.pass("The content on options section is not loaded on single edit shift page!");
        } else {
            SimpleUtils.fail("The content on options section should no loaded on single edit shift page!", false);
        }
    }


    @Override
    public void verifyEditableTypesShowOnSingleEditShiftDetail() throws Exception {
        if (areListElementVisible(gridContainers, 3)) {
            if (gridContainers.size() == 9) {
                if (gridContainers.get(0).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Work Role")
                        && gridContainers.get(7).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Name")
                        && gridContainers.get(3).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Start Time")
                        && gridContainers.get(4).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("End Time")
                        && gridContainers.get(1).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Breaks")
                        && gridContainers.get(5).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Date")
                        && gridContainers.get(6).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Assignment")
                        && gridContainers.get(8).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Notes")) {
                    SimpleUtils.pass("'Work Role', 'Shift Name', 'Start Time', 'End Time', 'Breaks', 'Date', 'Assignment', 'Shift Notes' " +
                            "sections are loaded successfully!");
                } else {
                    SimpleUtils.fail("Sections on Shift Details is incorrect!", false);
                }
            } else if (gridContainers.size() == 10) {
                if (gridContainers.get(0).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Work Role")
                        && gridContainers.get(8).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Name")
                        && gridContainers.get(3).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Location")
                        && gridContainers.get(4).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Start Time")
                        && gridContainers.get(5).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("End Time")
                        && gridContainers.get(1).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Breaks")
                        && gridContainers.get(6).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Date")
                        && gridContainers.get(7).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Assignment")
                        && gridContainers.get(9).findElement(By.cssSelector(".MuiGrid-item:nth-child(1)")).getText().equals("Shift Notes")) {
                    SimpleUtils.pass("'Work Role', 'Shift Name', 'Location', 'Start Time', 'End Time', 'Breaks', 'Date', 'Assignment', 'Shift Notes' " +
                            "sections are loaded successfully for Location Group!");
                } else {
                    SimpleUtils.fail("Sections on Shift Details is incorrect!", false);
                }
            }
        } else {
            SimpleUtils.fail("Shift Details section failed to load!", false);
        }
    }


    @Override
    public void verifyTheTextInCurrentColumnOnSingleEditShiftPage(String type, String value) throws Exception {
        WebElement gridRow = null;
        if (areListElementVisible(gridContainers, 3)) {
            if (gridContainers.size() == 10) {
                if (type.equals(sectionType.WorkRole.getType())) {
                    gridRow = gridContainers.get(1);
                } else if (type.equals(sectionType.ShiftName.getType())) {
                    gridRow = gridContainers.get(2);
                } else if (type.equals(sectionType.StartTime.getType())) {
                    gridRow = gridContainers.get(3);
                } else if (type.equals(sectionType.EndTime.getType())) {
                    gridRow = gridContainers.get(4);
                } else if (type.equals(sectionType.Breaks.getType())) {
                    gridRow = gridContainers.get(5);
                } else if (type.equals(sectionType.Date.getType())) {
                    gridRow = gridContainers.get(7);
                } else if (type.equals(sectionType.Assignment.getType())) {
                    gridRow = gridContainers.get(8);
                } else if (type.equals(sectionType.ShiftNotes.getType())) {
                    gridRow = gridContainers.get(9);
                }
            } else if (gridContainers.size() == 11) {
                if (type.equals(sectionType.WorkRole.getType())) {
                    gridRow = gridContainers.get(1);
                } else if (type.equals(sectionType.Location.getType())) {
                    gridRow = gridContainers.get(2);
                } else if (type.equals(sectionType.ShiftName.getType())) {
                    gridRow = gridContainers.get(3);
                } else if (type.equals(sectionType.StartTime.getType())) {
                    gridRow = gridContainers.get(4);
                } else if (type.equals(sectionType.EndTime.getType())) {
                    gridRow = gridContainers.get(5);
                } else if (type.equals(sectionType.Breaks.getType())) {
                    gridRow = gridContainers.get(6);
                }else if (type.equals(sectionType.Date.getType())) {
                    gridRow = gridContainers.get(8);
                } else if (type.equals(sectionType.Assignment.getType())) {
                    gridRow = gridContainers.get(9);
                } else if (type.equals(sectionType.ShiftNotes.getType())) {
                    gridRow = gridContainers.get(10);
                }
            }
            if (type.equals(sectionType.Assignment.getType())){
                String employeeNameOfAvatar = gridRow.findElement(By.tagName("img")).getAttribute("alt");
                if (employeeNameOfAvatar.equalsIgnoreCase(value)) {
                    SimpleUtils.pass("Verified for " + type + ", the value is correct!");
                } else
                    SimpleUtils.fail("Verified for " + type + ", the value is incorrect! Expected: " + value +
                            ", But actual is: "+employeeNameOfAvatar, false);
            }else if (type.equals(sectionType.Breaks.getType())) {
                if (gridRow.findElement(By.cssSelector(".MuiGrid-item:nth-child(2)")).getText().contains(value.split(" and ")[0])
                        && gridRow.findElement(By.cssSelector(".MuiGrid-item:nth-child(2)")).getText().contains(value.split(" and ")[1])) {
                    SimpleUtils.pass("Verified for " + type + ", the value is correct!");
                } else {
                    SimpleUtils.fail("Verified for " + type + ", the value is incorrect! Expected: " + value +
                            ", But actual is: " + gridRow.findElement(By.cssSelector(".MuiGrid-item:nth-child(2)")).getText().trim(), false);
                }
            }else {
                if (gridRow.findElement(By.cssSelector(".MuiGrid-item:nth-child(2)")).getText().trim().equalsIgnoreCase(value)) {
                    SimpleUtils.pass("Verified for " + type + ", the value is correct!");
                } else {
                    SimpleUtils.fail("Verified for " + type + ", the value is incorrect! Expected: " + value +
                            ", But actual is: " + gridRow.findElement(By.cssSelector(".MuiGrid-item:nth-child(2)")).getText().trim(), false);
                }
            }
        } else {
            SimpleUtils.fail("Shift Details section failed to load!", false);
        }
    }


    @FindBy (css = ".MuiGrid-spacing-xs-2 >div:nth-child(1) [data-testid=\"CancelRoundedIcon\"]")
    private List<WebElement> removeMealBreakButtons;
    @FindBy (css = ".MuiGrid-spacing-xs-2 >div:nth-child(1) [data-testid=\"AddOutlinedIcon\"]")
    private WebElement addMealBreakButton;
    @FindBy (css = ".MuiGrid-spacing-xs-2 >div:nth-child(2) [data-testid=\"CancelRoundedIcon\"]")
    private List<WebElement> removeRestBreakButtons;
    @FindBy (css = ".MuiGrid-spacing-xs-2 >div:nth-child(2) [data-testid=\"AddOutlinedIcon\"]")
    private WebElement addRestBreakButton;
    @FindBy (css = ".MuiGrid-grid-xs-true:nth-child(1)  [style=\"border-top: none;\"] div")
    private List<WebElement> mealBreakWarningMessages;
    @FindBy (css = ".MuiGrid-grid-xs-true:nth-child(2)  [style=\"border-top: none;\"] div")
    private List<WebElement> restBreakWarningMessages;


    @Override
    public void removeAllMealBreaks() {
        if (areListElementVisible(removeMealBreakButtons, 3)){
            for (WebElement button: removeMealBreakButtons) {
                scrollToElement(button);
                click(button);
                SimpleUtils.pass("Click the meal break button successfully! ");
            }
            if (areListElementVisible(removeMealBreakButtons, 3)) {
                SimpleUtils.fail("Remove all meal breaks fail, there still have "+removeMealBreakButtons.size()+" left", false);
            } else
                SimpleUtils.pass("Remove all meal breaks successfully! ");
        } else
            SimpleUtils.report("There is no meal break buttons! ");
    }


    @Override
    public void removeAllRestBreaks() throws Exception {
        checkOrUncheckAutomaticallyScheduleOptimizedBreak(false);
        if (areListElementVisible(removeRestBreakButtons, 3)){
            for (WebElement button: removeRestBreakButtons) {
                scrollToElement(button);
                click(button);
                SimpleUtils.pass("Click the rest break button successfully! ");
            }
            if (areListElementVisible(removeRestBreakButtons, 3)) {
                SimpleUtils.fail("Remove all rest breaks fail, there still have "+removeRestBreakButtons.size()+" left", false);
            } else
                SimpleUtils.pass("Remove all rest breaks successfully! ");
        } else
            SimpleUtils.report("There is no rest break buttons! ");
    }
    
//    @FindBy(css = "#shiftStart-helper-text")
//    private WebElement startTimeErrorMessage;
//    @FindBy(css = "#shiftEnd-helper-text")
//    private WebElement endTimeErrorMessage;
    @Override
    public List<String> getErrorMessageOfTime() throws Exception {
        List<String> errorMessages = new ArrayList<>();
        WebElement startTimeErrorMessage = null;
        WebElement endTimeErrorMessage = null;
        try {
            startTimeErrorMessage = getSpecificElementByTypeAndColumn(sectionType.StartTime.getType(), "Edited")
                    .findElement(By.cssSelector("#legion_cons_Schedule_Schedule_EditShifts_StartTime-helper-text"));
        } catch (Exception e) {
            // Do nothing
        }
        try {
            endTimeErrorMessage = getSpecificElementByTypeAndColumn(sectionType.EndTime.getType(), "Edited")
                    .findElement(By.cssSelector("#legion_cons_Schedule_Schedule_EditShifts_EndTime-helper-text"));
        } catch (Exception e) {
            // Do nothing
        }
        if (startTimeErrorMessage != null && endTimeErrorMessage == null) {
            waitForSeconds(1);
            errorMessages.add(startTimeErrorMessage.getText().trim());
            SimpleUtils.report("Catch the error message of Start Time!");
        } else if (startTimeErrorMessage == null && endTimeErrorMessage != null) {
            waitForSeconds(1);
            errorMessages.add(endTimeErrorMessage.getText().trim());
            SimpleUtils.report("Catch the error message of End Time!");
        } else if (startTimeErrorMessage != null && endTimeErrorMessage != null) {
            waitForSeconds(1);
            errorMessages.add(startTimeErrorMessage.getText().trim());
            errorMessages.add(endTimeErrorMessage.getText().trim());
            SimpleUtils.report("Catch the error message of Start Time & End Time!");
        }
        return errorMessages;
    }

    @FindBy (css = "[name=\"mealBreaks.0.startMin\"]")
    private List<WebElement> mealBreaksStartInputs;
    @FindBy (css = "[name=\"mealBreaks.0.endMin\"]")
    private List<WebElement> mealBreaksEndInputs;
    @FindBy (css = "[name=\"restBreaks.0.startMin\"]")
    private List<WebElement> restBreaksStartInputs;
    @FindBy (css = "[name=\"restBreaks.0.endMin\"]")
    private List<WebElement> restBreaksEndInputs;


    @Override
    public void inputMealBreakTimes(String startMealTime, String endMealTime, int index) throws Exception {
        checkOrUncheckAutomaticallyScheduleOptimizedBreak(false);
        if (areListElementVisible(mealBreaksStartInputs, 3)
                && areListElementVisible(mealBreaksEndInputs, 3)){
            mealBreaksStartInputs.get(index).clear();
            mealBreaksStartInputs.get(index).sendKeys(startMealTime);
            mealBreaksEndInputs.get(index).clear();
            mealBreaksEndInputs.get(index).sendKeys(endMealTime);
            click(locationInfo);
            SimpleUtils.pass("Set meal start and end time successfully! ");
        } else
            SimpleUtils.fail("Meal break start and end inputs fail to load! ", false);
    }


    @Override
    public void inputRestBreakTimes(String startRestTime, String endRestTime, int index) throws Exception {
        checkOrUncheckAutomaticallyScheduleOptimizedBreak(false);
        if (areListElementVisible(restBreaksEndInputs, 3)
                && areListElementVisible(restBreaksEndInputs, 3)){
            restBreaksEndInputs.get(index).clear();
            restBreaksEndInputs.get(index).sendKeys(endRestTime);
            restBreaksStartInputs.get(index).clear();
            restBreaksStartInputs.get(index).sendKeys(startRestTime);
            click(locationInfo);
            SimpleUtils.pass("Set rest start and end time successfully! ");
        } else
            SimpleUtils.fail("Rest break start and end inputs fail to load! ", false);
    }

    @Override
    public List<Map<String, String>> getMealBreakTimes() throws Exception {
        List<Map<String, String>> mealBreakTimeList = new ArrayList<>();
        if (areListElementVisible(mealBreaksStartInputs, 3)
                && areListElementVisible(mealBreaksEndInputs, 3)
                && mealBreaksStartInputs.size() == mealBreaksEndInputs.size()){
            for (int i= 0; i< mealBreaksStartInputs.size();i++) {
                Map<String, String> mealBreakTimes= new HashMap<>();
                mealBreakTimes.put("mealStartTime", mealBreaksStartInputs.get(i).getAttribute("value"));
                mealBreakTimes.put("mealEndTime", mealBreaksEndInputs.get(i).getAttribute("value"));
                mealBreakTimeList.add(mealBreakTimes);
            }

            if (mealBreakTimeList.size() > 0) {
                SimpleUtils.pass("Get meal breaks successfully! ");
            } else
                SimpleUtils.fail("Fail to get meal breaks! ", false);
        } else
            SimpleUtils.fail("Meal break start and end inputs fail to load! ", false);
        return mealBreakTimeList;
    }


    @Override
    public List<Map<String, String>> getRestBreakTimes() throws Exception {
        List<Map<String, String>> restBreakTimeList = new ArrayList<>();
        if (areListElementVisible(restBreaksStartInputs, 3)
                && areListElementVisible(restBreaksEndInputs, 3)
                && restBreaksStartInputs.size() == restBreaksEndInputs.size()){
            for (int i= 0; i< restBreaksStartInputs.size();i++) {
                Map<String, String> restBreakTimes= new HashMap<>();
                restBreakTimes.put("restStartTime", restBreaksStartInputs.get(i).getAttribute("value"));
                restBreakTimes.put("restEndTime", restBreaksEndInputs.get(i).getAttribute("value"));
                restBreakTimeList.add(restBreakTimes);
            }

            if (restBreakTimeList.size() > 0) {
                SimpleUtils.pass("Get rest breaks successfully! ");
            } else
                SimpleUtils.fail("Fail to get rest breaks! ", false);
        } else
            SimpleUtils.fail("Rest break start and end inputs fail to load! ", false);
        return restBreakTimeList;
    }


    @Override
    public void clickOnAddMealBreakButton() throws Exception {
        checkOrUncheckAutomaticallyScheduleOptimizedBreak(false);
        if (isElementLoaded(addMealBreakButton, 5)) {
            scrollToElement(addMealBreakButton);
            click(addMealBreakButton);
            SimpleUtils.pass("Click add meal break button successfully! ");
        }else
            SimpleUtils.fail("The add meal break button fail to load! ", false);
    }

    @Override
    public void clickOnAddRestBreakButton() throws Exception {
        if (isElementLoaded(addRestBreakButton, 5)) {
            scrollToElement(addRestBreakButton);
            click(addRestBreakButton);
            SimpleUtils.pass("Click add rest break button successfully! ");
        }else
            SimpleUtils.fail("The add rest break button fail to load! ", false);
    }


    @Override
    public List<String> getMealBreakWarningMessage() {
        List<String> warningMessage = new ArrayList<>();
        if (areListElementVisible(mealBreakWarningMessages, 3)){
            for (WebElement message: mealBreakWarningMessages) {
                warningMessage.add(message.getText());
                SimpleUtils.pass("Set warning message: "+message.getAttribute("value")+" successfully!");
            }
        }else
            SimpleUtils.report("The meal break warning message fail to load! ");
        return warningMessage;
    }


    @Override
    public List<String> getRestBreakWarningMessage() {
        List<String> warningMessage = new ArrayList<>();
        if (areListElementVisible(restBreakWarningMessages, 3)){
            for (WebElement message: restBreakWarningMessages) {
                warningMessage.add(message.getText());
                SimpleUtils.pass("Set warning message: "+message.getAttribute("value")+" successfully!");
            }
        }else
            SimpleUtils.report("The rest break warning message fail to load! ");
        return warningMessage;
    }


    @Override
    public int getMealBreakCount () throws Exception {
        checkOrUncheckAutomaticallyScheduleOptimizedBreak(false);
        int count = 0;
        if (mealBreaksStartInputs.size() == mealBreaksEndInputs.size()) {
            count = mealBreaksStartInputs.size();
            SimpleUtils.pass("Set meal break count successfully! ");
        }else
            SimpleUtils.fail("Meal breaks on single edit shift page display incorrectly", false);
        return count;
    }

    @Override
    public int getRestBreakCount () throws Exception {
        checkOrUncheckAutomaticallyScheduleOptimizedBreak(false);
        int count = 0;
        if (restBreaksStartInputs.size() == restBreaksEndInputs.size()) {
            count = restBreaksStartInputs.size();
            SimpleUtils.pass("Set reset break count successfully! ");
        }else
            SimpleUtils.fail("Rest breaks on single edit shift page display incorrectly", false);
        return count;
    }



    @FindBy (css = ".MuiPaper-elevation>div>div>div>button:nth-child(2)")
    private WebElement updateAnywayButton;
    @Override
    public void clickOnUpdateAnywayButton() throws Exception {
//        waitForSeconds(2);
        if (isElementLoaded(updateAnywayButton, 3)) {
            clickTheElement(updateAnywayButton);
        }
    }


    @FindBy (css = "span.MuiCheckbox-colorPrimary")
    private WebElement automaticallyScheduleOptimizedBreak;
    @Override
    public void checkOrUncheckAutomaticallyScheduleOptimizedBreak(boolean isCheck) throws Exception {
        if (isElementLoaded(automaticallyScheduleOptimizedBreak, 5)) {
            if (isCheck) {
                if (automaticallyScheduleOptimizedBreak.getAttribute("class").contains("checked")){
                    SimpleUtils.pass("The checkbox already checked");
                } else {
                    click(automaticallyScheduleOptimizedBreak);
                    if (automaticallyScheduleOptimizedBreak.getAttribute("class").contains("checked")){
                        SimpleUtils.pass("The checkbox been checked successfully! ");
                    } else
                        SimpleUtils.fail("Fail to check the check box!", false);
                }
            } else {
                if (automaticallyScheduleOptimizedBreak.getAttribute("class").contains("checked")){
                    click(automaticallyScheduleOptimizedBreak);
                    if (automaticallyScheduleOptimizedBreak.getAttribute("class").contains("checked")){
                        SimpleUtils.fail("Fail to uncheck the check box!", false);
                    } else
                        SimpleUtils.pass("The checkbox been unchecked successfully! ");

                } else {
                    SimpleUtils.pass("The checkbox already unchecked");
                }
            }
        }else
            SimpleUtils.fail("The Automatically schedule optimized break(s) fail to load! ", false);
    }
}
