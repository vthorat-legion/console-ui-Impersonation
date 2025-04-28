package com.legion.pages.core.schedule;

import com.legion.pages.*;
import com.legion.pages.core.ConsoleScheduleNewUIPage;
import com.legion.utils.FileDownloadVerify;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import com.legion.utils.JsonUtil;

import java.util.*;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleScheduleMainPage extends BasePage implements ScheduleMainPage {
    public ConsoleScheduleMainPage() {
        PageFactory.initElements(getDriver(), this);
    }
    private static HashMap<String, String> propertyOperatingHours = JsonUtil.getPropertiesFromJsonFile("src/test/resources/operatingHours.json");

    @FindBy(css = "lg-button[label=\"Analyze\"]")
    private WebElement analyze;

    @FindBy(xpath = "//div[contains(text(),'Schedule History')]")
    private WebElement scheduleHistoryInAnalyzePopUp;

    @FindBy(css = "[label=\"Cancel\"]")
    private WebElement scheduleEditModeCancelButton;

    @FindBy(css = "lg-button[label=\"Edit\"] button")
    private WebElement newEdit;

    @FindBy(css = "[ng-click=\"callOkCallback()\"]")
    private WebElement editAnywayPopupButton;

    @FindBy(css = "lg-button[data-tootik=\"Save changes\"]")
    private WebElement newScheduleSaveButton;

    @FindBy(xpath = "//div[contains(@ng-if,'PostSave')]")
    private WebElement popUpPostSave;

    @FindBy(css = "button.btn.sch-ok-single-btn")
    private WebElement btnOK;

    @FindBy(xpath = "//div[contains(@ng-if,'PreSave')]")
    private WebElement popUpPreSave;

    @FindBy(css = "button.btn.sch-save-confirm-btn")
    private WebElement scheduleVersionSaveBtn;

    @FindBy(css = "[label=\"Create New Shift\"]")
    private WebElement createNewShiftWeekView;

    @FindBy(css = "lg-button-group[buttons='scheduleTypeOptions'] div.lg-button-group-selected")
    private WebElement activScheduleType;

    @FindBy(css = "lg-button-group[buttons='scheduleTypeOptions'] div.lg-button-group-last")
    private WebElement scheduleTypeManager;

    @FindBy(css = "lg-button[label=\"Generate schedule\"]")
    private WebElement generateScheduleBtn;

    @FindBy(css = "lg-button[ng-click=\"controlPanel.fns.editAction($event)\"]")
    private WebElement editScheduleButton;

    @FindBy(css = "div.modal-content")
    private WebElement popupAlertPremiumPay;

    @FindBy(css = "._pendo-button-tertiaryButton")
    private WebElement maybeLaterBtn;

    @FindBy(css = "button.btn.lgn-action-button.lgn-action-button-success")
    private WebElement btnEditAnyway;

    @FindBy(css = "button.btn.lgn-action-button.lgn-action-button-default")
    private WebElement btnCancelOnAlertPopup;

    @FindBy(css = "lg-button[ng-click=\"controlPanel.fns.saveConfirmation($event)\"]")
    private WebElement btnSaveOnSchedulePage;

    @FindBy(css = "lg-button[ng-click=\"controlPanel.fns.cancelAction($event)\"]")
    private WebElement btnCancelOnSchedulePage;

    @FindBy(css = "[ng-click=\"controlPanel.fns.publishConfirmation($event, false)\"]")
    private WebElement publishButton;

    @FindBy(css = "[ng-click=\"controlPanel.fns.publishConfirmation($event, true)\"]")
    private WebElement republishButton;

    @FindBy(css = ".sch-publish-confirm-btn")
    private WebElement confirmPublishBtn;

    @FindBy(css = "select.ng-valid-required")
    private WebElement scheduleGroupByButton;

    public void clickOnScheduleAnalyzeButton() throws Exception {
        if (isElementLoaded(analyze)) {
            click(analyze);
            if (isElementLoaded(scheduleHistoryInAnalyzePopUp,5)) {
                SimpleUtils.pass("Analyze button is clickable and pop up page displayed");
            }
        } else {
            SimpleUtils.fail("Schedule Analyze Button not loaded successfully!", false);
        }
    }

    public void clickOnCancelButtonOnEditMode() throws Exception {
        if (isElementLoaded(scheduleEditModeCancelButton)) {
            clickTheElement(scheduleEditModeCancelButton);
            SimpleUtils.pass("Schedule edit shift page cancelled successfully!");
        }
    }


    public void clickOnEditButton() throws Exception {
        if (isElementEnabled(newEdit,2)) {
            click(newEdit);
            if (isElementLoaded(editAnywayPopupButton, 2)) {
                click(editAnywayPopupButton);
                SimpleUtils.pass("Schedule edit shift page loaded successfully!");
            } else {
                SimpleUtils.pass("Schedule edit shift page loaded successfully for Draft or Publish Status");
            }
        } else {
            SimpleUtils.pass("Schedule Edit button is not enabled Successfully!");
        }
    }

    public void clickSaveBtn() throws Exception {
        if (isElementLoaded(newScheduleSaveButton)) {
            click(newScheduleSaveButton);
            clickOnVersionSaveBtn();
            clickOnPostSaveBtn();
        } else {
            SimpleUtils.fail("Schedule Save button not clicked Successfully!", false);
        }
    }

    public void clickOnVersionSaveBtn() throws Exception {
        if (isElementLoaded(popUpPreSave) && isElementLoaded(scheduleVersionSaveBtn)) {
            click(scheduleVersionSaveBtn);
            SimpleUtils.pass("Schedule Version Save button clicked Successfully!");
            waitForSeconds(3);
        } else {
            SimpleUtils.fail("Schedule Version Save button not clicked Successfully!", false);
        }
    }

    public void clickOnPostSaveBtn() throws Exception {
        if (isElementLoaded(popUpPostSave) && isElementLoaded(btnOK)) {
            click(btnOK);
            SimpleUtils.pass("Schedule Ok button clicked Successfully!");
            waitForSeconds(3);
        } else {
            SimpleUtils.fail("Schedule Ok button not clicked Successfully!", false);
        }
    }

    public Boolean isAddNewDayViewShiftButtonLoaded() throws Exception {
        if (isElementLoaded(createNewShiftWeekView)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isScheduleTypeLoaded() {
        try {
            if (isElementEnabled(activScheduleType, 10)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void switchToManagerView() throws Exception {
        CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
        String activeWeekText = getActiveWeekText();
        if(isElementEnabled(activScheduleType,5)){
            if(activScheduleType.getText().equalsIgnoreCase("Suggested")){
                click(scheduleTypeManager);
                if(createSchedulePage.isGenerateButtonLoadedForManagerView()){
                    click(generateScheduleBtn);
                    createSchedulePage.generateScheduleFromCreateNewScheduleWindow(activeWeekText);
                }else{
                    SimpleUtils.fail("Generate button not found on page",false);
                }
            }else{
                if(createSchedulePage.isGenerateButtonLoadedForManagerView()){
                    click(generateScheduleBtn);
                    createSchedulePage.generateScheduleFromCreateNewScheduleWindow(activeWeekText);
                }else{
                    SimpleUtils.fail("Generate button not found on page",false);
                }
            }
        }else{
            SimpleUtils.fail("Schedule Type " + scheduleTypeManager.getText() + " is disabled",false);
        }
    }

    public void clickOnEditButtonNoMaterScheduleFinalizedOrNot() throws Exception {
        CreateSchedulePage createSchedulePage = new ConsoleCreateSchedulePage();
        waitForSeconds(3);
        if(checkEditButton())
        {
            // Validate what happens next to the Edit!
            // When Status is finalized, look for extra popup.
            clickTheElement(editScheduleButton);
            waitForSeconds(5);
            if(isElementLoaded(popupAlertPremiumPay,15) ) {
                SimpleUtils.pass("Edit button is clickable and Alert(premium pay pop-up) is appeared on Screen");
                waitForSeconds(4);
                // Validate CANCEL and EDIT ANYWAY Buttons are enabled.
                if(isElementEnabled(btnEditAnyway,10) && isElementEnabled(btnCancelOnAlertPopup,10)){
                    SimpleUtils.pass("CANCEL And EDIT ANYWAY Buttons are enabled on Alert Pop up");
                    SimpleUtils.report("Click on EDIT ANYWAY button and check for next save and cancel buttons");
                    clickTheElement(btnEditAnyway);
                } else {
                    SimpleUtils.fail("CANCEL And EDIT ANYWAY Buttons are not enabled on Alert Popup ",false);
                }
                waitForSeconds(5);
            }
            if (isElementLoaded(maybeLaterBtn, 20)) {
                clickTheElement(maybeLaterBtn);
            }
            if(checkSaveButton() && checkCancelButton()) {
                SimpleUtils.pass("Save and Cancel buttons are enabled ");
            } else{
                SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
            }
        }else{
            createSchedulePage.generateOrUpdateAndGenerateSchedule();
        }

/*        if(checkEditButton())
        {
            // Validate what happens next to the Edit!
            // When Status is finalized, look for extra popup.
            if(isScheduleFinalized())
            {
                click(editScheduleButton);
                String warningMessage1 = "Editing finalized schedule\n" + "Editing a finalized schedule after the ";
                String warningMessage2 = "-day advance notice period may incur a schedule change premium.";
                String editFinalizedScheduleWarning = editFinalizedScheduleWarningTitle.getText() + "\n" + editFinalizedScheduleWarningMessage.getText();
                if(isElementLoaded(popupAlertPremiumPay,5) && editFinalizedScheduleWarning.contains(warningMessage1) &&
                editFinalizedScheduleWarning.contains(warningMessage2)) {
                    SimpleUtils.pass("Edit button is clickable and Alert(premium pay pop-up) is appeared on Screen");
                    // Validate CANCEL and EDIT ANYWAY Buttons are enabled.
                    if(isElementEnabled(btnEditAnyway,5) && isElementEnabled(btnCancelOnAlertPopup,5)){
                        SimpleUtils.pass("CANCEL And EDIT ANYWAY Buttons are enabled on Alert Pop up");
                        SimpleUtils.report("Click on EDIT ANYWAY button and check for next save and cancel buttons");
                        click(btnEditAnyway);
                        if(checkSaveButton() && checkCancelButton()) {
                            SimpleUtils.pass("Save and Cancel buttons are enabled ");
                        }
                        else
                            SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
                    }
                    else
                        SimpleUtils.fail("CANCEL And EDIT ANYWAY Buttons are not enabled on Alert Popup ",false);
                }
            }
            else
            {
                clickTheElement(editScheduleButton);
                // Validate Save and cancel buttons are enabled!
                if(checkSaveButton() && checkCancelButton()) {
                    SimpleUtils.pass("Save and Cancel buttons are enabled ");
                }
                else
                    SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
            }
        }else
            generateOrUpdateAndGenerateSchedule(); */
    }

    @Override
    public boolean checkEditButton() throws Exception {
        if(isElementLoaded(editScheduleButton,15))
        {

            SimpleUtils.pass("Edit button is Editable");
            return true;
        }
        else {
            SimpleUtils.fail("Edit button is not Enable on screen", false);
            return false;
        }
    }

    @Override
    public boolean checkSaveButton() throws Exception {
        if(isElementEnabled(btnSaveOnSchedulePage,10))
        {
            SimpleUtils.pass("Save button is enabled ");
            return true;
        }
        else
        {
            SimpleUtils.fail("Save button is not enabled. ", true);
            return false;
        }
    }

    @Override
    public boolean checkCancelButton() throws Exception {
        if(isElementEnabled(btnCancelOnSchedulePage,10))
        {
            SimpleUtils.pass("Cancel button is enabled ");
            return true;
        }
        else
        {
            SimpleUtils.fail("Cancel button is not enabled. ", true);
            return false;
        }
    }

    @Override
    public void verifyEditButtonFuntionality() throws Exception {

        if(checkEditButton())
        {
            // Validate what happens next to the Edit!
            // When Status is finalized, look for extra popup.
            if(isScheduleFinalized())
            {
                click(editScheduleButton);
                if(isElementLoaded(popupAlertPremiumPay,5)) {
                    SimpleUtils.pass("Alert(premium pay pop-up) is appeared on Screen");
                    // Validate CANCEL and EDIT ANYWAY Buttons are enabled.
                    if(isElementEnabled(btnEditAnyway,5) && isElementEnabled(btnCancelOnAlertPopup,5)){
                        SimpleUtils.pass("CANCEL And EDIT ANYWAY Buttons are enabled on Alert Pop up");
                        click(btnEditAnyway);
                        if(checkSaveButton() && checkCancelButton()) {
                            SimpleUtils.pass("Save and Cancel buttons are enabled ");
                            selectCancelButton();
                        }
                        else
                            SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);

                    }
                    else
                        SimpleUtils.fail("CANCEL And EDIT ANYWAY Buttons are not enabled on Alert Popup ",false);
                }
                else
                    SimpleUtils.fail("Alert(premium pay pop-up) is not appeared on Screen",false);
            }
            else
            {
                click(editScheduleButton);
                SimpleUtils.pass("Edit button is clickable");
                // Validate Save and cancel buttons are enabled!
                if(checkSaveButton() && checkCancelButton()) {
                    SimpleUtils.pass("Save and Cancel buttons are enabled ");
                    selectCancelButton();
                }
                else
                    SimpleUtils.fail("Save and Cancel buttons are not enabled. ", false);
            }
        }
    }


    @Override
    public boolean isScheduleFinalized() throws Exception {
        if(isElementLoaded(publishButton,5))
        {
            SimpleUtils.report("Publish button is loaded on screen, Hence We don't expect Alert Popup.  ");
            return false;  }
        else {
            SimpleUtils.report("Publish button is not loaded on screen, Hence We have to expect Alert Popup.  ");
            return true;  }
    }

    @Override
    public void publishOrRepublishSchedule() throws Exception {
        if (isClickable(publishButton, 15)) {
            scrollToElement(publishButton);
            click(publishButton);
            if (isElementLoaded(confirmPublishBtn, 15)) {
                click(confirmPublishBtn);
                waitForSeconds(5);
                waitForNotExists(publishButton, 60);
            }
        }

        if (isClickable(republishButton, 15)) {
            scrollToElement(republishButton);
            click(republishButton);
            if (isElementLoaded(confirmPublishBtn, 15)) {
                click(confirmPublishBtn);
                waitForSeconds(5);
                waitForNotExists(republishButton, 60);
            }
        }
    }

    @Override
    public void selectCancelButton() throws Exception {
        if(checkCancelButton())
        {
            click(btnCancelOnSchedulePage);
            SimpleUtils.pass("Cancel button is clicked ! ");
        }
        else
        {
            SimpleUtils.fail("Cancel Button cannot be clicked! ",false);
        }
    }

    @Override
    public void selectSaveButton() throws Exception {
        if(checkCancelButton())
        {
            click(btnSaveOnSchedulePage);
            SimpleUtils.pass("Save button is clicked ! ");
        }
        else
        {
            SimpleUtils.fail("Save Button cannot be clicked! ",false);
        }

    }

    @FindBy(css = "div.lg-filter__wrapper")
    private WebElement filterPopup;

    @FindBy(css = "[ng-click=\"$ctrl.openFilter()\"]")
    private WebElement filterButton;

    @FindBy(css = "[ng-repeat=\"(key, opts) in $ctrl.displayFilters\"]")
    private List<WebElement> scheduleFilterElements;

    @FindBy(css = "[ng-repeat=\"opt in opts\"]")
    private List<WebElement> filters;

    public void selectGroupByFilter(String optionVisibleText) {
        Select groupBySelectElement = new Select(scheduleGroupByButton);
        List<WebElement> scheduleGroupByButtonOptions = groupBySelectElement.getOptions();
        groupBySelectElement.selectByIndex(1);
        for (WebElement scheduleGroupByButtonOption : scheduleGroupByButtonOptions) {
            if (scheduleGroupByButtonOption.getText().toLowerCase().contains(optionVisibleText.toLowerCase())) {
                groupBySelectElement.selectByIndex(scheduleGroupByButtonOptions.indexOf(scheduleGroupByButtonOption));
                SimpleUtils.report("Selected Group By Filter: '" + optionVisibleText + "'");
            }
        }
    }

    public void selectShiftTypeFilterByText(String filterText) throws Exception {
        if (areListElementVisible(wholeWeekShifts, 20)) {
            SimpleUtils.pass("Shifts list grid has been loaded!");
        }

        String shiftTypeFilterKey = "shifttype";
        ArrayList<WebElement> shiftTypeFilters = getAvailableFilters().get(shiftTypeFilterKey);
        unCheckFilters(shiftTypeFilters);
        for (WebElement shiftTypeOption : shiftTypeFilters) {
            if (shiftTypeOption.getText().toLowerCase().contains(filterText.toLowerCase())) {
                clickTheElement(shiftTypeOption.findElement(By.tagName("input")));
                SimpleUtils.pass("Click the shift type successfully! ");
                break;
            }
        }

        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
        waitForSeconds(5);

        if (areListElementVisible(wholeWeekShifts, 20)) {
            SimpleUtils.pass("Shifts list grid has been loaded!");
        }
    }

    public HashMap<String, ArrayList<WebElement>> getAvailableFilters() {
        HashMap<String, ArrayList<WebElement>> scheduleFilters = new HashMap<String, ArrayList<WebElement>>();
        try {
            if (isElementLoaded(filterButton,15)) {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                for (WebElement scheduleFilterElement : scheduleFilterElements) {
                    WebElement filterLabel = scheduleFilterElement.findElement(By.className("lg-filter__category-label"));
                    String filterType = filterLabel.getText().toLowerCase().replace(" ", "");
                    List<WebElement> filters = scheduleFilterElement.findElements(By.cssSelector("input-field[type=\"checkbox\"]"/*"[ng-repeat=\"opt in opts\"]"*/));
                    ArrayList<WebElement> filterList = new ArrayList<WebElement>();
                    for (WebElement filter : filters) {
                        filterList.add(filter);
                    }
                    scheduleFilters.put(filterType, filterList);
                }
            } else {
                SimpleUtils.fail("Filters button not found on Schedule page!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail("Filters button not loaded successfully on Schedule page!", true);
        }
        return scheduleFilters;
    }


    public void unCheckFilters(ArrayList<WebElement> filterElements) throws Exception {
        if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
        waitForSeconds(2);
        for (WebElement filterElement : filterElements) {
            if (isElementLoaded(filterElement, 5)) {
                WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
                String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
                if (elementClasses.contains("ng-not-empty"))
                    click(filterElement);
            }
        }
    }

    public void unCheckFilters() throws Exception {
        if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
        if (areListElementVisible(filters, 10)) {
            for (WebElement filterElement : filters) {
                waitForSeconds(2);
                WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
                String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
                if (elementClasses.contains("ng-not-empty"))
                    clickTheElement(filterCheckBox);
            }
        }
    }


    @FindBy(xpath = "//span[contains(text(),'Save')]")
    private WebElement scheduleSaveBtn;

    @FindBy(css = "button[ng-click*='confirmSaveAction']")
    private WebElement saveOnSaveConfirmationPopup;

    @FindBy(css = ".lg-toast")
    private WebElement msgOnTop;
    @FindBy(css = "div.lg-toast")
    private WebElement successMsg;
    public void saveSchedule() throws Exception {
        waitForSeconds(3);
        if (isElementEnabled(scheduleSaveBtn, 10) && isClickable(scheduleSaveBtn, 10)) {
            scrollToElement(scheduleSaveBtn);
            clickTheElement(scheduleSaveBtn);
        } else {
            SimpleUtils.fail("Schedule save button not found", false);
        }

        if (isElementLoaded(successMsg, 15) && successMsg.getText().contains("Success!")) {
            SimpleUtils.pass("Save the Schedule with no change Successfully!");
        } else if (isSaveConfirmPopupLoaded()) {
            clickTheElement(saveOnSaveConfirmationPopup);
            waitForNotExists(saveOnSaveConfirmationPopup, 30);
            waitForSeconds(5);
            try{
                if (isElementLoaded(editScheduleButton, 10)) {
                    SimpleUtils.pass("Save the Schedule Successfully!");
                } else {
                    SimpleUtils.fail("Save Schedule Failed!", false);
                }
            } catch(StaleElementReferenceException e){
                SimpleUtils.report("stale element reference: element is not attached to the page document");
            }
        } else {
            SimpleUtils.report("Schedule save button not found");
        }
    }


    @FindBy(css = "[ng-click=\"printActionInProgress() || printAction($event)\"]")
    private WebElement printButton;

    @FindBy(css = "[label=\"Print\"]")
    private WebElement printButtonInPrintLayout;
    @Override
    public void printButtonIsClickable() throws Exception {
        if (isElementLoaded(printButton,10)){
            scrollToTop();
            click(printButton);
            if(isElementLoaded(printButtonInPrintLayout, 5)) {
                click(printButtonInPrintLayout);
            }
        }else{
            SimpleUtils.fail("There is no print button",false);
        }
    }


    @FindBy(css = "lg-button[label=\"Edit\"]")
    private WebElement edit;

    @FindBy(css = "lg-button-group[buttons='scheduleTypeOptions'] div.lg-button-group-first")
    private WebElement scheduleTypeSystem;

    @FindBy (css = "[on-change=\"updateGroupBy(value)\"]")
    private WebElement groupByAllIcon;

    @Override
    public void legionButtonIsClickableAndHasNoEditButton() throws Exception {
        clickOnSuggestedButton();
        if(!isElementLoaded(edit,5)){
            SimpleUtils.pass("Legion schedule has no edit button");
        }else{
            SimpleUtils.fail("it's not in legion schedule page", true);
        }
    }

    public void clickOnSuggestedButton() throws Exception {
        if (isElementEnabled(scheduleTypeSystem, 5)) {
            clickTheElement(scheduleTypeSystem);
            SimpleUtils.pass("legion button is clickable");
        }else {
            SimpleUtils.fail("the schedule is not generated, generated schedule firstly",true);
        }
    }

    public void legionIsDisplayingTheSchedul() throws Exception {
        if(isElementLoaded(groupByAllIcon,10)){
            SimpleUtils.pass("Legion schedule is displaying");
        }else {
            SimpleUtils.fail("Legion Schedule load failed", true);
        }
    }

    public void filterScheduleByShiftTypeWeekView(ArrayList<WebElement> shiftTypeFilters) {
        //String shiftType = "";
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        for (WebElement shiftTypeFilter : shiftTypeFilters) {
            try {
                Thread.sleep(1000);
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(shiftTypeFilters);
                String shiftType = shiftTypeFilter.getText();
                SimpleUtils.report("Data for Shift Type: '" + shiftType + "'");
                click(shiftTypeFilter);
                click(filterButton);
                String cardHoursAndWagesText = "";
                SmartCardPage smartCardPage = new ConsoleSmartCardPage();
                HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
                for (Map.Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
                    if (cardHoursAndWagesText != "")
                        cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
                    else
                        cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
                }
                SimpleUtils.report("Active Week Card's Data: " + cardHoursAndWagesText);
                scheduleShiftTablePage.getHoursAndTeamMembersForEachDaysOfWeek();
                SimpleUtils.assertOnFail("Sum of Daily Schedule Hours not equal to Active Week Schedule Hours!", scheduleShiftTablePage.verifyActiveWeekDailyScheduleHoursInWeekView(), true);

                if (!getActiveGroupByFilter().toLowerCase().contains(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyTM.getValue().toLowerCase())
                        && !shiftType.toLowerCase().contains("open"))
                    scheduleShiftTablePage.verifyActiveWeekTeamMembersCountAvailableShiftCount();
            } catch (Exception e) {
                SimpleUtils.fail("Unable to get Card data for active week!", true);
            }
        }
    }

    public void filterScheduleByShiftTypeDayView(ArrayList<WebElement> shiftTypeFilters) {
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        for (WebElement shiftTypeFilter : shiftTypeFilters) {
            try {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(shiftTypeFilters);
                SimpleUtils.report("Data for Shift Type: '" + shiftTypeFilter.getText() + "'");
                click(shiftTypeFilter);
                click(filterButton);
                String cardHoursAndWagesText = "";
                SmartCardPage smartCardPage = new ConsoleSmartCardPage();
                HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
                for (Map.Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
                    if (cardHoursAndWagesText != "")
                        cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
                    else
                        cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
                }
                SimpleUtils.report("Active Week Card's Data: " + cardHoursAndWagesText);
                String timeDurationText = "";
                for (String timeDuration : scheduleShiftTablePage.getScheduleDayViewGridTimeDuration()) {
                    if (timeDurationText == "")
                        timeDurationText = timeDuration;
                    else
                        timeDurationText = timeDurationText + " | " + timeDuration;
                }
                SimpleUtils.report("Schedule Day View Shift Duration: " + timeDurationText);

                String budgetedTeamMembersCount = "";
                for (String budgetedTeamMembers : scheduleShiftTablePage.getScheduleDayViewBudgetedTeamMembersCount()) {
                    if (budgetedTeamMembersCount == "")
                        budgetedTeamMembersCount = budgetedTeamMembers;
                    else
                        budgetedTeamMembersCount = budgetedTeamMembersCount + " | " + budgetedTeamMembers;
                }
                SimpleUtils.report("Schedule Day View budgeted Team Members count: " + budgetedTeamMembersCount);

                String scheduleTeamMembersCount = "";
                for (String scheduleTeamMembers : scheduleShiftTablePage.getScheduleDayViewScheduleTeamMembersCount()) {
                    if (scheduleTeamMembersCount == "")
                        scheduleTeamMembersCount = scheduleTeamMembers;
                    else
                        scheduleTeamMembersCount = scheduleTeamMembersCount + " | " + scheduleTeamMembers;
                }
                SimpleUtils.report("Schedule Day View budgeted Team Members count: " + scheduleTeamMembersCount);
            } catch (Exception e) {
                SimpleUtils.fail("Unable to get Card data for active week!", true);
            }
        }
    }

    @Override
    public String getActiveGroupByFilter() throws Exception {
        String selectedGroupByFilter = "";
        if (isElementLoaded(scheduleGroupByButton)) {
            Select groupBySelectElement = new Select(scheduleGroupByButton);
            selectedGroupByFilter = groupBySelectElement.getFirstSelectedOption().getText();
        } else {
            SimpleUtils.fail("Group By Filter not loaded successfully for active Week/Day: '" + getActiveWeekText() + "'", false);
        }
        return selectedGroupByFilter;
    }


    public void filterScheduleByWorkRoleAndShiftType(boolean isWeekView) throws Exception {
        waitForSeconds(10);
        String shiftTypeFilterKey = "shifttype";
        String workRoleFilterKey = "workrole";
        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
        if (availableFilters.size() > 1) {
            ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
            ArrayList<WebElement> workRoleFilters = availableFilters.get(workRoleFilterKey);
            for (WebElement workRoleFilter : workRoleFilters) {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(workRoleFilters);
                click(workRoleFilter);
                SimpleUtils.report("Data for Work Role: '" + workRoleFilter.getText() + "'");
                if (isWeekView)
                    filterScheduleByShiftTypeWeekView(shiftTypeFilters);
                else
                    filterScheduleByShiftTypeDayView(shiftTypeFilters);
            }
        } else {
            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
        }
    }


    public ArrayList<HashMap<String, String>> getHoursAndShiftsCountForEachWorkRolesInWeekView() throws Exception {
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        String workRoleFilterKey = "workrole";
        ArrayList<HashMap<String, String>> eachWorkRolesData = new ArrayList<HashMap<String, String>>();
        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
        SmartCardPage smartCardPage = new ConsoleSmartCardPage();
        if (availableFilters.size() > 1) {
            ArrayList<WebElement> workRoleFilters = availableFilters.get(workRoleFilterKey);
            for (WebElement workRoleFilter : workRoleFilters) {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(workRoleFilters);
                click(workRoleFilter);

                //adding workrole name
                HashMap<String, String> workRole = new HashMap<String, String>();
                workRole.put("workRole", workRoleFilter.getText());

                //Adding Card data (Hours & Wages)
                for (Map.Entry<String, Float> e : smartCardPage.getScheduleLabelHoursAndWages().entrySet())
                    workRole.put(e.getKey(), e.getValue().toString());
                // Adding Shifts Count
                workRole.put("shiftsCount", "" + scheduleShiftTablePage.getAllAvailableShiftsInWeekView().size());

                eachWorkRolesData.add(workRole);
            }
            unCheckFilters(workRoleFilters);
            if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                click(filterButton);
        } else {
            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
        }

        return eachWorkRolesData;
    }


    public void selectWorkRoleFilterByIndex(int index, boolean isClearWorkRoleFilters) throws Exception {
        String filterType = "workrole";
        ArrayList<WebElement> availableWorkRoleFilters = getAvailableFilters().get(filterType);
        if (availableWorkRoleFilters.size() >= index) {
            if (isClearWorkRoleFilters)
                unCheckFilters(availableWorkRoleFilters);
            click(availableWorkRoleFilters.get(index));
            SimpleUtils.pass("Schedule Work Role:'" + availableWorkRoleFilters.get(index).getText() + "' Filter selected Successfully!");
        }
    }

    @Override
    public void selectWorkRoleFilterByText(String workRoleLabel, boolean isClearWorkRoleFilters) throws Exception {
        String filterType = "workrole";
        ArrayList<WebElement> availableWorkRoleFilters = getAvailableFilters().get(filterType);
        if (isClearWorkRoleFilters)
            unCheckFilters(availableWorkRoleFilters);
        for (WebElement availableWorkRoleFilter : availableWorkRoleFilters) {
            if (availableWorkRoleFilter.getText().split("\\(")[0].trim().equalsIgnoreCase(workRoleLabel.toLowerCase())) {
                click(availableWorkRoleFilter);
                SimpleUtils.pass("Schedule Work Role:'" + availableWorkRoleFilter.getText() + "' Filter selected Successfully!");
                break;
            }
        }
        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
    }

    @FindBy (css = ".week-schedule-shift .week-schedule-shift-wrapper")
    private List<WebElement> wholeWeekShifts;
    public void filterScheduleByShiftTypeWeekViewAsTeamMember(ArrayList<WebElement> shiftTypeFilters) throws Exception {
        if (shiftTypeFilters.size() > 0) {
            for (WebElement shiftTypeFilter : shiftTypeFilters) {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(shiftTypeFilters);
                String shiftType = shiftTypeFilter.getText();
                SimpleUtils.report("Data for Shift Type: '" + shiftType + "'");
                click(shiftTypeFilter);
                click(filterButton);
                if (areListElementVisible(wholeWeekShifts, 5)) {
                    for (WebElement shift : wholeWeekShifts) {
                        WebElement name = shift.findElement(By.className("week-schedule-worker-name"));
                        if (shiftType.contains("Open")) {
                            if (!name.getText().equalsIgnoreCase("Open")) {
                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
                            }
                        }else {
                            if (name.getText().contains("Open")) {
                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
                            }
                        }
                    }
                }else {
                    SimpleUtils.report("Didn't find shift for type: " + shiftType);
                }
            }
        }else {
            SimpleUtils.fail("Shift Type Filters not loaded Successfully!", false);
        }
    }

    @FindBy(css = ".sch-day-view-shift")
    private List<WebElement> dayViewAvailableShifts;
    public void filterScheduleByShiftTypeDayViewAsTeamMember(ArrayList<WebElement> shiftTypeFilters) throws Exception {
        if (shiftTypeFilters.size() > 0) {
            for (WebElement shiftTypeFilter : shiftTypeFilters) {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(shiftTypeFilters);
                String shiftType = shiftTypeFilter.getText();
                SimpleUtils.report("Data for Shift Type: '" + shiftType + "'");
                click(shiftTypeFilter);
                click(filterButton);
                if (areListElementVisible(dayViewAvailableShifts, 5)) {
                    for (WebElement shift : dayViewAvailableShifts) {
                        WebElement name = shift.findElement(By.className("sch-day-view-shift-worker-name"));
                        if (shiftType.equalsIgnoreCase("Open")) {
                            if (!name.getText().contains("Open")) {
                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
                            }
                        }else {
                            if (name.getText().contains("Open")) {
                                SimpleUtils.fail("Shift: " + name.getText() + " isn't for shift type: " + shiftType, false);
                            }
                        }
                    }
                }else {
                    SimpleUtils.report("Didn't find shift for type: " + shiftType);
                }
            }
        }else {
            SimpleUtils.fail("Shift Type Filters not loaded Successfully!", false);
        }
    }


    @Override
    public void checkAndUnCheckTheFilters() throws Exception {
        if (areListElementVisible(filters, 10)) {
            unCheckFilters();
            for (WebElement filter : filters) {
                String filterName = filter.findElement(By.className("input-label")) == null ? "" : filter.findElement(By.className("input-label")).getText();
                WebElement filterCheckBox = filter.findElement(By.cssSelector("input[type=\"checkbox\"]"));
                String elementClass = filterCheckBox.getAttribute("class").toLowerCase();
                if (elementClass.contains("ng-not-empty")) {
                    SimpleUtils.fail("Uncheck the filter: " + filterName + " not Successfully!", false);
                }else {
                    SimpleUtils.pass("Uncheck the filter: " + filterName + " Successfully!");
                }
                click(filterCheckBox);
                elementClass = filterCheckBox.getAttribute("class").toLowerCase();
                if (elementClass.contains("ng-not-empty")) {
                    SimpleUtils.pass("Check the filter: " + filterName + " Successfully!");
                }else {
                    SimpleUtils.fail("Check the filter: " + filterName + " not Successfully!", false);
                }
            }
        }else {
            SimpleUtils.fail("Filters on Schedule page not loaded Successfully!", false);
        }
    }

    @Override
    public void filterScheduleByShiftTypeAsTeamMember(boolean isWeekView) throws Exception {
        String shiftTypeFilterKey = "shifttype";
        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
        if (availableFilters.size() > 0) {
            ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
            if (isWeekView) {
                filterScheduleByShiftTypeWeekViewAsTeamMember(shiftTypeFilters);
            }else {
                filterScheduleByShiftTypeDayViewAsTeamMember(shiftTypeFilters);
            }
        }
    }


    @Override
    public String selectOneFilter() throws Exception {
        String selectedFilter = null;
        if (areListElementVisible(filters, 10)) {
            unCheckFilters();
            waitForSeconds(2);
            WebElement filterCheckBox = filters.get(1).findElement(By.cssSelector("input[type=\"checkbox\"]"));
            String elementClass = filterCheckBox.getAttribute("class").toLowerCase();
            if (elementClass.contains("ng-empty")) {
                clickTheElement(filterCheckBox);
                waitForSeconds(2);
                elementClass = filterCheckBox.getAttribute("class").toLowerCase();
            }
            selectedFilter = filters.get(1).findElement(By.className("input-label")) == null ? "" : filters.get(1).findElement(By.className("input-label")).getText();
            if (elementClass.contains("ng-not-empty")) {
                SimpleUtils.pass("Check the filter: " + selectedFilter + " Successfully!");
            }else {
                SimpleUtils.fail("Check the filter: " + selectedFilter + " not Successfully!", false);
            }
        }else {
            SimpleUtils.fail("Filters on Schedule page not loaded Successfully!", false);
        }
        return selectedFilter;
    }


    @Override
    public void filterScheduleByBothAndNone() throws Exception {
        String shiftTypeFilterKey = "shifttype";
        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
        ArrayList<WebElement> shiftTypeFilters = null;
        int bothSize = 0;
        int noneSize = 0;
        if (availableFilters.size() > 0) {
            shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
            unCheckFilters(shiftTypeFilters);
            waitForSeconds(2);
            if (areListElementVisible(wholeWeekShifts, 5)) {
                noneSize = wholeWeekShifts.size();
            }
            checkFilters(shiftTypeFilters);
            waitForSeconds(2);
            if (areListElementVisible(wholeWeekShifts, 5)) {
                bothSize = wholeWeekShifts.size();
            }
            if (noneSize != 0 && bothSize != 0 && noneSize == bothSize) {
                SimpleUtils.pass("Scheduled and open shifts are shown when applying both filters and none of them!");
            } else {
                SimpleUtils.fail("Applying both filters size is: " + bothSize + ", but applying none of them size is: " + noneSize
                        + ", they are inconsistent!", false);
            }
        } else {
            SimpleUtils.fail("Failed to get the available filters!", false);
        }
    }


    public void checkFilters(ArrayList<WebElement> filterElements) {
        if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
        waitForSeconds(2);
        for (WebElement filterElement : filterElements) {
            WebElement filterCheckBox = filterElement.findElement(By.cssSelector("input[type=\"checkbox\"]"));
            String elementClasses = filterCheckBox.getAttribute("class").toLowerCase();
            if (elementClasses.contains("ng-empty"))
                click(filterElement);
        }
    }


    public void filterScheduleByJobTitleWeekView(ArrayList<WebElement> jobTitleFilters, ArrayList<String> availableJobTitleList) throws Exception{
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        for (WebElement jobTitleFilter : jobTitleFilters) {
            Thread.sleep(1000);
            if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                click(filterButton);
            unCheckFilters(jobTitleFilters);
            String jobTitle = jobTitleFilter.getText();
            SimpleUtils.report("Data for job title: '" + jobTitle + "' as bellow");
            clickTheElement(jobTitleFilter.findElement(By.cssSelector("input[type=\"checkbox\"]")));
            click(filterButton);
            String cardHoursAndWagesText = "";
            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
            HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
            for (Map.Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
                if (cardHoursAndWagesText != "")
                    cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
                else
                    cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
            }
            SimpleUtils.report("Active Week Card's Data: " + cardHoursAndWagesText);
            if (availableJobTitleList.contains(jobTitle.toLowerCase().trim())) {
                SimpleUtils.assertOnFail("Sum of Daily Schedule Hours not equal to Active Week Schedule Hours!",
                        scheduleShiftTablePage.newVerifyActiveWeekDailyScheduleHoursInWeekView(), true);
            }else
                SimpleUtils.report("there is no data for this job title: '" + jobTitle+ "'");
        }
    }



    public void filterScheduleByJobTitleDayView(ArrayList<WebElement> jobTitleFilters,ArrayList<String> availableJobTitleList) throws Exception{
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        for (WebElement jobTitleFilter : jobTitleFilters) {
            if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                click(filterButton);
            unCheckFilters(jobTitleFilters);
            String jobTitle = jobTitleFilter.getText();
            SimpleUtils.report("Data for job title: '" + jobTitle + "' as bellow");
            click(jobTitleFilter);
            click(filterButton);
            String cardHoursAndWagesText = "";
            SmartCardPage smartCardPage = new ConsoleSmartCardPage();
            HashMap<String, Float> hoursAndWagesCardData = smartCardPage.getScheduleLabelHoursAndWages();
            for (Map.Entry<String, Float> hoursAndWages : hoursAndWagesCardData.entrySet()) {
                if (cardHoursAndWagesText != "")
                    cardHoursAndWagesText = cardHoursAndWagesText + ", " + hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
                else
                    cardHoursAndWagesText = hoursAndWages.getKey() + ": '" + hoursAndWages.getValue() + "'";
            }
            SimpleUtils.report("Active Day Card's Data: " + cardHoursAndWagesText);
            float activeDayScheduleHoursOnCard = smartCardPage.getScheduleLabelHoursAndWages().get(ConsoleScheduleNewUIPage.scheduleHoursAndWagesData.scheduledHours.getValue());
            float totalShiftsWorkTime = scheduleShiftTablePage.getActiveShiftHoursInDayView();
            SimpleUtils.report("Active Day Total Work Time Data: " + totalShiftsWorkTime);
            if (availableJobTitleList.contains(jobTitle.toLowerCase().trim())) {
                if (activeDayScheduleHoursOnCard - totalShiftsWorkTime <= 0.05) {
                    SimpleUtils.pass("Schedule Hours in smart card  equal to total Active Schedule Hours by job title filter ");
                }else
                    SimpleUtils.fail("the job tile filter hours not equal to schedule hours in schedule samrtcard",false);
            }else
                SimpleUtils.report( "there is no data for this job title: '" + jobTitle + "'");
        }
    }


    public void filterScheduleByJobTitle(boolean isWeekView) throws Exception{
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        ArrayList<String> availableJobTitleList = new ArrayList<>();
        if (isWeekView == true) {
            availableJobTitleList = scheduleShiftTablePage.getAvailableJobTitleListInWeekView();
        }else
            availableJobTitleList = scheduleShiftTablePage.getAvailableJobTitleListInDayView();

        waitForSeconds(10);
        String jobTitleFilterKey = "jobtitle";
        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
        if (availableFilters.size() > 1) {
            ArrayList<WebElement> jobTitleFilters = availableFilters.get(jobTitleFilterKey);
            if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                click(filterButton);
            unCheckFilters(jobTitleFilters);
            if (isWeekView) {
                filterScheduleByJobTitleWeekView(jobTitleFilters, availableJobTitleList);
            }
            else {
                filterScheduleByJobTitleDayView(jobTitleFilters, availableJobTitleList);
            }
        } else {
            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
        }
    }



    @Override
    public void filterScheduleByWorkRoleAndJobTitle(boolean isWeekView) throws Exception{
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        waitForSeconds(10);
        ArrayList<String> availableJobTitleList = new ArrayList<>();
        if (isWeekView == true) {
            availableJobTitleList = scheduleShiftTablePage.getAvailableJobTitleListInWeekView();
        }else
            availableJobTitleList = scheduleShiftTablePage.getAvailableJobTitleListInDayView();

        String workRoleFilterKey = "workrole";
        String jobTitleFilterKey = "jobtitle";

        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
        if (availableFilters.size() > 1) {
            ArrayList<WebElement> workRoleFilters = availableFilters.get(workRoleFilterKey);
            ArrayList<WebElement> jobTitleFilters = availableFilters.get(jobTitleFilterKey);
            for (WebElement workRoleFilter : workRoleFilters) {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(workRoleFilters);
                click(workRoleFilter.findElement(By.cssSelector("input[type=\"checkbox\"]")));
                SimpleUtils.report("Data for Work Role: '" + workRoleFilter.getText() + "'");
                if (isWeekView) {
                    filterScheduleByJobTitleWeekView(jobTitleFilters, availableJobTitleList);
                }else {
                    filterScheduleByJobTitleDayView(jobTitleFilters, availableJobTitleList);
                }
            }
        } else {
            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
        }
    }
    @Override
    public void filterScheduleByShiftTypeAndJobTitle(boolean isWeekView) throws Exception{
        ScheduleShiftTablePage scheduleShiftTablePage = new ConsoleScheduleShiftTablePage();
        waitForSeconds(10);
        ArrayList<String> availableJobTitleList = new ArrayList<>();
        if (isWeekView == true) {
            availableJobTitleList = scheduleShiftTablePage.getAvailableJobTitleListInWeekView();
        }else
            availableJobTitleList = scheduleShiftTablePage.getAvailableJobTitleListInDayView();

        String shiftTypeFilterKey = "shifttype";
        String jobTitleFilterKey = "jobtitle";

        HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
        if (availableFilters.size() > 1) {
            ArrayList<WebElement> shiftTypeFilters = availableFilters.get(shiftTypeFilterKey);
            ArrayList<WebElement> jobTitleFilters = availableFilters.get(jobTitleFilterKey);
            for (WebElement shiftTypeFilter : shiftTypeFilters) {
                if (filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
                    click(filterButton);
                unCheckFilters(shiftTypeFilters);
                click(shiftTypeFilter);
                SimpleUtils.report("Data for Work Role: '" + shiftTypeFilter.getText() + "'");
                if (isWeekView) {
                    filterScheduleByJobTitleWeekView(jobTitleFilters, availableJobTitleList);
                }else {
                    filterScheduleByJobTitleDayView(jobTitleFilters, availableJobTitleList);
                }
            }
        } else {
            SimpleUtils.fail("Filters are not appears on Schedule page!", false);
        }
    }


    @Override
    public void selectLocationFilterByText(String filterText) throws Exception {
        String locationFilterKey = "location";
        ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
        unCheckFilters(locationFilters);
        for (WebElement locationOption : locationFilters) {
            if (locationOption.getText().toLowerCase().contains(filterText.toLowerCase())) {
                click(locationOption);
                break;
            }
        }
        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
    }


    @Override
    public void selectAllChildLocationsToFilter() throws Exception {
        if (isElementLoaded(filterPopup,5)) {
            String locationFilterKey = "location";
            ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
            unCheckFilters(locationFilters);
            checkFilters(locationFilters);
        } else
            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
    }

    @FindBy(css = "div.rows")
    private List<WebElement> weekScheduleShiftsOfWeekView;
    @Override
    public void verifyAllChildLocationsShiftsLoadPerformance() throws Exception {
        if (isElementLoaded(filterPopup,5)) {
            String locationFilterKey = "location";
            ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
            unCheckFilters(locationFilters);
            for (WebElement locationOption: locationFilters) {
                click(locationOption);
                if (areListElementVisible(weekScheduleShiftsOfWeekView,3))
                    SimpleUtils.pass("Schedule Page: The performance target is < 3 seconds to load");
                else
                    SimpleUtils.fail("Schedule Page: The performance target is more than 3 seconds to load",false);
            }
        } else
            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
    }


    @FindBy(css = "[class=\"week-schedule-shift-title\"]")
    private List<WebElement> scheduleShiftTitles;
    @FindBy(css = "[class=\"sch-group-header\"]")
    private List<WebElement> dayScheduleShiftTitles;
    @FindBy(css = ".sch-group-label")
    private List<WebElement> scheduleShiftTitlesDayView;
    @Override
    public void verifyShiftsDisplayThroughLocationFilter(String childLocation) throws Exception {
        String locationFilterKey = "location";
        boolean isChildLocationPresent = false;
        ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
        unCheckFilters(locationFilters);
        for (WebElement locationOption: locationFilters) {
            if (locationOption.getText().contains(childLocation)) {
                isChildLocationPresent = true;
                click(locationOption);
                if (areListElementVisible(scheduleShiftTitles,3)) {
                    for (WebElement title: scheduleShiftTitles) {
                        if (childLocation.toUpperCase().contains(title.getText())) {
                            SimpleUtils.pass("Schedule Page: The shifts change successfully according to the filter");
                            break;
                        } else
                            SimpleUtils.fail("Schedule Page: The shifts don't change according to the filter",false);
                    }
                } else
                    SimpleUtils.fail("Schedule Page: The shifts of the child location failed to load or loaded slowly",false);
                break;
            }
        }
        if (!isChildLocationPresent)
            SimpleUtils.fail("Schedule Page: The filtered child location does not exist",false);
        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
    }


    @Override
    public void verifyChildLocationShiftsLoadPerformance(String childLocation) throws Exception {
        String locationFilterKey = "location";
        boolean isChildLocationPresent = false;
        ArrayList<WebElement> locationFilters = getAvailableFilters().get(locationFilterKey);
        unCheckFilters(locationFilters);
        for (WebElement locationOption: locationFilters) {
            if (locationOption.getText().contains(childLocation)) {
                isChildLocationPresent = true;
                click(locationOption);
                if (areListElementVisible(weekScheduleShiftsOfWeekView,3))
                    SimpleUtils.pass("Schedule Page: The performance target is < 3 seconds to load");
                else
                    SimpleUtils.fail("Schedule Page: The performance target is more than 3 seconds to load",false);
                break;
            }
        }
        if (!isChildLocationPresent)
            SimpleUtils.fail("Schedule Page: The filtered child location does not exist",false);
    }

    @Override
    public void selectChildLocationFilterByText(String location) throws Exception {
        String shiftTypeFilterKey = "location";
        ArrayList<WebElement> shiftTypeFilters = getAvailableFilters().get(shiftTypeFilterKey);
        unCheckFilters(shiftTypeFilters);
        for (WebElement shiftTypeOption : shiftTypeFilters) {
            if (shiftTypeOption.getText().toLowerCase().contains(location.toLowerCase())) {
                click(shiftTypeOption);
                break;
            }
        }
        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
    }


    @FindBy (css = "[src*=\"print.svg\"]")
    private WebElement printIcon;
    @Override
    public void verifyThePrintFunction() throws Exception {
        try {
            if (isPrintIconLoaded()) {
                clickTheElement(printIcon);
                // Wait for the schedule to be downloaded
                if (isElementLoaded(printButtonInPrintLayout, 5)) {
                    click(printButtonInPrintLayout);
                }
                waitForSeconds(10);
                String downloadPath = SimpleUtils.fileDownloadPath;
                SimpleUtils.assertOnFail("Failed to download the team schedule",
                        FileDownloadVerify.isFileDownloaded_Ext(downloadPath, "WeekView-Parent_Child-Oct 28-Nov 3"), false);
            } else {
                SimpleUtils.fail("Print icon not loaded Successfully on Schedule page!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.toString(), false);
        }
    }

    @Override
    public boolean isPrintIconLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(printIcon, 5)) {
            isLoaded = true;
            SimpleUtils.pass("Print Icon loaded Successfully!");
        }
        return isLoaded;
    }


    @FindBy(css = ".group-by-select-box select")
    private WebElement groupBySelector;

    // Options of group by selectors

    @FindBy(xpath = "//option[contains(@value,'string:All')]")
    private WebElement groupByAll;

    @FindBy(xpath = "//option[contains(@value,'string:WorkRole')]")
    private WebElement groupByWorkRole;

    @FindBy(xpath = "//option[contains(@value,'string:TM')]")
    private WebElement groupByTM;

    @FindBy(xpath = "//option[contains(@value,'string:JobTitle')]")
    private WebElement groupByJobTitle;

    @FindBy(xpath = "//option[contains(@value,'string:Location')]")
    private WebElement groupByLocation;
    @Override
    public void validateGroupBySelectorSchedulePage(boolean isLocationGroup) throws Exception {
        waitForSeconds(3);
        if(isElementLoaded(groupBySelector,10))
        {
            click( groupBySelector);
            // Validate the each button on dropdown
            if(isElementEnabled(groupByAll,3)
                    && isElementEnabled(groupByWorkRole,3)
                    && isElementEnabled(groupByTM,3)
                    && isElementLoaded(groupByJobTitle,3)
                    && (!isLocationGroup || isElementLoaded(groupByLocation, 5)))
                if(isLocationGroup){
                    SimpleUtils.pass("In Week view: 'Group by All' filter have 5 filters:1.Group by all 2. Group by work role 3. Group by TM 4.Group by job title 5 Group by location");
                } else
                    SimpleUtils.pass("In Week view: 'Group by All' filter have 4 filters:1.Group by all 2. Group by work role 3. Group by TM 4.Group by job title");
            else SimpleUtils.fail("Group by All filter does not have 4 filters:1.Group by all 2. Group by work role 3. Group by TM 4.Group by job title",true);
        }
        else SimpleUtils.fail("Group By Selector is not available on screen ", false);

    }

    @FindBy (css = ".day-week-picker-period-week")
    private List<WebElement> currentWeeks;
    @FindBy (className = "period-name")
    private WebElement weekPeriod;
    @FindBy (className = "card-carousel-card")
    private List<WebElement> smartCards;
    @FindBy (className = "card-carousel-link")
    private List<WebElement> cardLinks;
    @FindBy (css = "[src*=\"No-Schedule\"]")
    private WebElement noSchedule;
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
    private WebElement yesButton;
    @FindBy(css = "[label=\"No\"]")
    private WebElement noButton;
    @FindBy(className = "day-week-picker-arrow-right")
    private WebElement calendarNavigationNextWeekArrow;
    @Override
    public void verifySelectedFilterPersistsWhenSelectingOtherWeeks(String selectedFilter) throws Exception {
        if (areListElementVisible(currentWeeks, 10)) {
            for (int i = 0; i < currentWeeks.size(); i++) {
                click(currentWeeks.get(i));
                if (isElementLoaded(filterButton, 15)) {
                    String selectedValue = filterButton.findElement(By.cssSelector("input-field[placeholder=\"None\"] input")).getAttribute("value");
                    if (selectedFilter.contains(selectedValue)) {
                        SimpleUtils.pass("Selected Filter is persist on Week: " + currentWeeks.get(i).getText());
                    }else {
                        SimpleUtils.fail("Selected filter is changed on Week: " + currentWeeks.get(i).getText()
                                + ", expected filter is: " + selectedFilter + ", but actual selected filter is: " + selectedValue, false);
                    }
                }else {
                    SimpleUtils.fail("Filter Button not loaded Successfully!", false);
                }
                if (i == (currentWeeks.size() - 1) && isElementLoaded(calendarNavigationNextWeekArrow, 5)) {
                    click(calendarNavigationNextWeekArrow);
                    verifySelectedFilterPersistsWhenSelectingOtherWeeks(selectedFilter);
                }
            }
        }else {
            SimpleUtils.fail("Current weeks' elements not loaded Successfully!", false);
        }
    }


    //added by Haya
    @FindBy (css = "button.dropdown-toggle")
    private WebElement dropdownToggle;
    @FindBy (css = ".lg-dropdown-menu__option")
    private List<WebElement> dropdownMenuFormDropdownToggle;
    @Override
    public void goToToggleSummaryView() throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(dropdownToggle,10)){
            click(dropdownToggle);
            if (areListElementVisible(dropdownMenuFormDropdownToggle,10)&&dropdownMenuFormDropdownToggle.size()==3 ){
                waitForSeconds(3);
                click(dropdownMenuFormDropdownToggle.get(2));
                SimpleUtils.pass("Toggle Summary View has been clicked!");
            } else {
                SimpleUtils.fail("After clicking dropdown toggle button, no menu drop down", false);
            }
        } else {
            SimpleUtils.fail("There is no toggle drop down button in schedule page!", false);
        }
    }

    @Override
    public void clickToggleSummaryViewButton() throws Exception {
        if (areListElementVisible(dropdownMenuFormDropdownToggle,10)&&dropdownMenuFormDropdownToggle.size()==3 ){
            click(dropdownMenuFormDropdownToggle.get(2));
            SimpleUtils.pass("Toggle Summary View has been clicked!");
        } else {
            if (isElementLoaded(dropdownToggle,10)){
                click(dropdownToggle);
            } else {
                SimpleUtils.fail("There is no toggle drop down button in schedule page!", false);
            }
        }
    }

    @FindBy(css = "[class=\"modal-instance-header-title ng-binding\"]")
    private WebElement editOpeHoursModalTitle;
    @FindBy(css = "[class = \"modal-instance-buttons\"]")
    private WebElement BtnsOnEditOpeHoursSchedule;
    @Override
    public boolean isMoreActionsBtnClickable() throws Exception {
        Boolean isClickable = null;
        if (isElementLoaded(dropdownToggle, 10)) {
            try{
                if (dropdownToggle.getAttribute("disabled") != null && dropdownToggle.getAttribute("disabled").equalsIgnoreCase("true")) {
                    SimpleUtils.pass("The More Actions button is unclickable!");
                    isClickable = false;
                } else {
                    SimpleUtils.pass("The More Actions button is clickable!");
                    isClickable = true;
                }
            }catch (Exception e){
                isClickable = false;
            }
        }else{
            SimpleUtils.fail("The More Actions button is not loaded!",false);
        }
        return isClickable;
    }

    @FindBy(css = "lg-button[label=\"Edit\"]>button")
    private WebElement editBtnForOperatingHrs;
    @Override
    public void goToEditOperatingHoursView() throws Exception {
        String subTitle = "Edit operating hours";
        if (isElementLoaded(dropdownToggle,10)){
            click(dropdownToggle);
            if (areListElementVisible(dropdownMenuFormDropdownToggle,10)&&dropdownMenuFormDropdownToggle.size()==3 ){
                waitForSeconds(3);
                click(dropdownMenuFormDropdownToggle.get(1));
                SimpleUtils.pass("Edit operating hours has been clicked!");
                if (isElementLoaded(editOpeHoursModalTitle, 20) && isElementLoaded(BtnsOnEditOpeHoursSchedule, 20)
                        && editOpeHoursModalTitle.getText().trim().equalsIgnoreCase(subTitle)) {
                    SimpleUtils.pass("The edit operating hours page is loaded correctly!");
                }else{
                    SimpleUtils.fail("The edit operating hours page is not loaded correctly!", false);
                }
            } else {
                SimpleUtils.fail("After clicking dropdown toggle button, the expected menu is not loaded correctly!", false);
            }
        } else if (isElementLoaded(editBtnForOperatingHrs,10)){
            click(editBtnForOperatingHrs);
            SimpleUtils.pass("Edit operating hours button is clicked!");
            if (isElementLoaded(editOpeHoursModalTitle, 20) && isElementLoaded(BtnsOnEditOpeHoursSchedule, 20)
                    && editOpeHoursModalTitle.getText().trim().equalsIgnoreCase(subTitle)) {
                SimpleUtils.pass("The edit operating hours page is loaded correctly!");
            }else{
                SimpleUtils.fail("The edit operating hours page is not loaded correctly!", false);
            }
        }else {
            SimpleUtils.fail("No available entrance to the edit operating hours dialog!", false);
        }
    }

    @FindBy(css = "[ng-repeat=\"day in summary.workingHours\"]")
    private List<WebElement> operatingDays;
    @Override
    public void checkOperatingHoursOnToggleSummary() throws Exception {
        if (areListElementVisible(operatingDays, 15)) {
            for (WebElement dayList : operatingDays) {
                WebElement weekDay = dayList.findElement(By.cssSelector(".ng-binding"));
                if (weekDay != null) {
                    String[] operatingHours = null;
                    operatingHours = propertyOperatingHours.get(weekDay.getText()).split("-");
                    WebElement startNEndTimes = dayList.findElement(By.cssSelector(".text-right.ng-binding.dirty"));
                    if (operatingHours[0].contains("0") && operatingHours[1].contains("0")) {
                        operatingHours[0] = operatingHours[0].replaceAll("0","");
                        operatingHours[0] = operatingHours[0].replaceAll(":","");
                        operatingHours[1] = operatingHours[1].replaceAll("0","");
                        operatingHours[1] = operatingHours[1].replaceAll(":","");
                    }
                    String opeHrs = operatingHours[0].trim() + "-" + operatingHours[1].trim();
                    if(startNEndTimes.getText().trim().equalsIgnoreCase(opeHrs) && startNEndTimes.getText().trim().equalsIgnoreCase(opeHrs)){
                        continue;
                    }else{
                        SimpleUtils.fail("The operating hours are not match!", false);
                    }
                }else{
                    SimpleUtils.fail("The operating day is null!", false);
                }
            }
        }else{
            SimpleUtils.fail("The operating days are not loaded!", false);
        }
    }

    @Override
    public void checkOpeHrsOfParticualrDayOnToggleSummary(List<String> weekDays, String duration) throws Exception {
        if (areListElementVisible(operatingDays, 15)) {
            for (WebElement dayList : operatingDays) {
                WebElement weekDay = dayList.findElement(By.cssSelector(".ng-binding"));
                if (weekDay != null) {
                    WebElement startNEndTimes = dayList.findElement(By.cssSelector(".text-right.ng-binding"));
                    if(weekDays.contains(weekDay.getText())){
                        SimpleUtils.assertOnFail("The operating hours is not expected!",
                                startNEndTimes.getText().trim().equalsIgnoreCase(duration), false);
                    }
                }else{
                    SimpleUtils.fail("The operating day is null!", false);
                }
            }
        }else{
            SimpleUtils.fail("The operating days are not loaded!", false);
        }
    }

    @Override
    public void checkClosedDayOnToggleSummary(List<String> weekDays) throws Exception {
        if (areListElementVisible(operatingDays, 15)) {
            for (WebElement dayList : operatingDays) {
                WebElement weekDay = dayList.findElement(By.cssSelector(".ng-binding"));
                WebElement closedText = dayList.findElement(By.cssSelector(".text-right.ng-binding.dirty"));
                if (weekDay != null) {
                    if (weekDays.contains(weekDay.getText())) {
                        SimpleUtils.assertOnFail("The message of closed day is not correct!", closedText.getText().trim().equalsIgnoreCase("Closed"), false);
                    }else{
                        SimpleUtils.pass("The current day is not closed!");
                        continue;
                    }
                }else{
                    SimpleUtils.fail("The operating day is null!", false);
                }
            }
        }else{
            SimpleUtils.fail("The operating days are not loaded!", false);
        }
    }

    @FindBy(css = ".operating-hours-day-list-item.ng-scope")
    private List<WebElement> operatingHoursDayLists;
    @Override
    public void checkOperatingHoursOnEditDialog() throws Exception {
        if (areListElementVisible(operatingHoursDayLists, 15)) {
            for (WebElement dayList : operatingHoursDayLists) {
                WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
                if (weekDay != null) {
                    String[] operatingHours = null;
                    operatingHours = propertyOperatingHours.get(weekDay.getText()).split("-");
                    List<WebElement> startAndEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
                    if(operatingHours[0].trim().equalsIgnoreCase(startAndEndTimes.get(0).getAttribute("value").trim()) && operatingHours[1].trim().equalsIgnoreCase(startAndEndTimes.get(1).getAttribute("value").trim())){
                        continue;
                    }else{
                        SimpleUtils.fail("The operating hours are not match!", false);
                    }
                }else{
                    SimpleUtils.fail("The operating day is null!", false);
                }
            }
        }else{
            SimpleUtils.fail("The operating days are not loaded!", false);
        }
    }

    @Override
    public void editTheOperatingHoursWithFixedValue(List<String> weekDays, String startTime, String endTime) throws Exception {
        try {
            if (areListElementVisible(operatingHoursDayLists, 15)) {
                for (WebElement dayList : operatingHoursDayLists) {
                    WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
                    if (weekDay != null) {
                        WebElement checkbox = dayList.findElement(By.cssSelector("input[type=\"checkbox\"]"));
                        if (weekDays.contains(weekDay.getText())) {
                            if (checkbox.getAttribute("class").contains("ng-empty")) {
                                clickTheElement(checkbox);
                            }
                            String[] operatingHours = null;
                            List<WebElement> startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
                            startNEndTimes.get(0).clear();
                            startNEndTimes.get(1).clear();
                            startNEndTimes.get(0).sendKeys(startTime);
                            startNEndTimes.get(1).sendKeys(endTime);
                            SimpleUtils.assertOnFail("The operating hours is not updated successfully!",
                                    startNEndTimes.get(0).getAttribute("value").equalsIgnoreCase(startTime)
                                            && startNEndTimes.get(1).getAttribute("value").equalsIgnoreCase(endTime), false);
                        }
                    } else {
                        SimpleUtils.fail("Failed to find weekday element!", false);
                    }
                }
            } else {
                SimpleUtils.fail("Operating days are not loaded Successfully!", false);
            }
        } catch (StaleElementReferenceException e) {
            SimpleUtils.report(e.getMessage());
        }
    }

    @Override
    public void closeTheParticularOperatingDay(List<String> weekDaysToClose) throws Exception {
        try {
            if (areListElementVisible(operatingHoursDayLists, 15)) {
                for (WebElement dayList : operatingHoursDayLists) {
                    WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
                    if (weekDay != null) {
                        WebElement checkbox = dayList.findElement(By.cssSelector("input[type=\"checkbox\"]"));
                        if (weekDaysToClose.contains(weekDay.getText())) {
                            if (checkbox.getAttribute("class").contains("ng-not-empty")) {
                                clickTheElement(checkbox);
                            }
                            List<WebElement> startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
                            String closeStartTime = startNEndTimes.get(0).getAttribute("placeholder").trim();
                            String closeEndTime = startNEndTimes.get(1).getAttribute("placeholder").trim();
                            String defaultValueForCloseDay = "- - : - -";
                            SimpleUtils.assertOnFail("The operating hours is not null after close the day!",
                                    closeStartTime.equalsIgnoreCase(defaultValueForCloseDay)
                                            && closeEndTime.equalsIgnoreCase(defaultValueForCloseDay), false);
                        } else {
                            if (checkbox.getAttribute("class").contains("ng-empty")) {
                                clickTheElement(checkbox);
                            }
                        }
                    } else {
                        SimpleUtils.fail("Failed to find weekday element!", false);
                    }
                }
            } else {
                SimpleUtils.fail("Operating days are not loaded Successfully!", false);
            }
        } catch (StaleElementReferenceException e) {
            SimpleUtils.report(e.getMessage());
        }
    }

    @Override
    public void openTheParticularOperatingDay(List<String> weekDaysToOpen) throws Exception {
        try {
            if (areListElementVisible(operatingHoursDayLists, 15)) {
                for (WebElement dayList : operatingHoursDayLists) {
                    WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
                    if (weekDay != null) {
                        WebElement checkbox = dayList.findElement(By.cssSelector("input[type=\"checkbox\"]"));
                        if (weekDaysToOpen.contains(weekDay.getText())) {
                            if (checkbox.getAttribute("class").contains("ng-empty")) {
                                clickTheElement(checkbox);
                            }
                            List<WebElement> startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
                            String closeStartTime = startNEndTimes.get(0).getAttribute("value").trim();
                            String closeEndTime = startNEndTimes.get(1).getAttribute("value").trim();
                            String defaultValueForCloseDay = "- - : - -";
                            SimpleUtils.assertOnFail("The operating hours is not shown after open the day!",
                                    closeStartTime != null && !(closeStartTime.equalsIgnoreCase(defaultValueForCloseDay))
                                            && closeEndTime != null && !(closeEndTime.equalsIgnoreCase(defaultValueForCloseDay)), false);
                        }
                    } else {
                        SimpleUtils.fail("Failed to find weekday element!", false);
                    }
                }
            } else {
                SimpleUtils.fail("Operating days are not loaded Successfully!", false);
            }
        } catch (StaleElementReferenceException e) {
            SimpleUtils.report(e.getMessage());
        }
    }


    @FindBy(css = "div[ng-click=\"close()\"]")
    private WebElement cancelBtnOnOpeHrsPage;
    @FindBy(css = "[class=\"modal-instance-button ng-binding\"]")
    private WebElement cancelBtnOnOpeHrsPageForOP;
    @Override
    public void clickCancelBtnOnEditOpeHoursPage() throws Exception {
        if (isElementLoaded(cancelBtnOnOpeHrsPage, 20)) {
            SimpleUtils.pass("The Cancel button is loaded successfully!");
            clickTheElement(cancelBtnOnOpeHrsPage);
        }else{
            SimpleUtils.fail("The Cancel button is not loaded!", false);
        }
    }

    @Override
    public void clickCancelBtnOnEditOpeHoursPageForOP() throws Exception {
        if (isElementLoaded(cancelBtnOnOpeHrsPageForOP, 20)) {
            SimpleUtils.pass("The Cancel button is loaded successfully!");
            clickTheElement(cancelBtnOnOpeHrsPageForOP);
        }else{
            SimpleUtils.fail("The Cancel button is not loaded!", false);
        }
    }

    @Override
    public void isCancelBtnLoadedOnEditOpeHoursPage() throws Exception {
        if (isElementLoaded(cancelBtnOnOpeHrsPage, 20)) {
            SimpleUtils.pass("The Cancel button is loaded successfully!");
        }else{
            SimpleUtils.fail("The Cancel button is not loaded!", false);
        }
    }

    @Override
    public void isCancelBtnLoadedOnEditOpeHoursPageForOP() throws Exception {
        if (isElementLoaded(cancelBtnOnOpeHrsPageForOP, 20)) {
            SimpleUtils.pass("The Cancel button is loaded successfully!");
        }else{
            SimpleUtils.fail("The Cancel button is not loaded!", false);
        }
    }

    @FindBy(css = "[class*=\"confirm\"]")
    private WebElement saveBtnOnOpeHrsPage;
    @FindBy(css = "[class=\"modal-instance-button confirm ng-binding\"]")
    private WebElement saveBtnOnOpeHrsPageForOP;
    @Override
    public void clickSaveBtnOnEditOpeHoursPage() throws Exception {
        if (isElementLoaded(saveBtnOnOpeHrsPage, 20)) {
            clickTheElement(saveBtnOnOpeHrsPage);
            SimpleUtils.pass("The Save button is loaded successfully!");
        }else{
            SimpleUtils.fail("The Save button is not loaded!", false);
        }
    }

    @Override
    public void clickSaveBtnOnEditOpeHoursPageForOP() throws Exception {
        if (isElementLoaded(saveBtnOnOpeHrsPageForOP, 20)) {
            clickTheElement(saveBtnOnOpeHrsPageForOP);
            SimpleUtils.pass("The Save button is loaded successfully!");
        }else{
            SimpleUtils.fail("The Save button is not loaded!", false);
        }
    }

    @Override
    public void isSaveBtnLoadedOnEditOpeHoursPage() throws Exception {
        if (isElementLoaded(saveBtnOnOpeHrsPage, 20)) {
            SimpleUtils.pass("The Save button is loaded successfully!");
        }else{
            SimpleUtils.fail("The Save button is not loaded!", false);
        }
    }

    @Override
    public void isSaveBtnLoadedOnEditOpeHoursPageForOP() throws Exception {
        if (isElementLoaded(saveBtnOnOpeHrsPageForOP, 20)) {
            SimpleUtils.pass("The Save button is loaded successfully!");
        }else{
            SimpleUtils.fail("The Save button is not loaded!", false);
        }
    }

    @FindBy(css = "lg-button[label=\"Edit\"]")
    private WebElement editButtonOnToggleSummary;
    @Override
    public void clickEditBtnOnToggleSummary() throws Exception {
        if (isElementLoaded(editButtonOnToggleSummary, 20)) {
            SimpleUtils.pass("Edit button on toggle summary is loaded!");
            clickTheElement(editButtonOnToggleSummary);

        }else{
            SimpleUtils.fail("Edit button on toggle summary is not loaded!", false);
        }
    }

    @FindBy(css = "[class=\"week-schedule-shift-color\"]")
    private List<WebElement> workRoleIcons;

    @FindBy(css = ".week-schedule-right-strip")
    private WebElement tMHourAndAverageShiftLengthColumn;

    @FindBy(css = "div.week-schedule-ribbon-group-toggle")
    private List<WebElement> groupByLocationToggles;
    @Override
    public void validateScheduleTableWhenSelectAnyOfGroupByOptions(boolean isLocationGroup) throws Exception {
        if(isElementLoaded(groupBySelector,5)) {
            //validate the schedule table when group by Work Role
            selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyWorkRole.getValue());
            if (areListElementVisible(workRoleIcons, 10) && workRoleIcons.size() > 0) {
                SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by work role");
            } else {
                SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by work role ", true);
            }

            //validate the schedule table when group by TM
            selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyTM.getValue());
            if (isElementEnabled(tMHourAndAverageShiftLengthColumn, 10)) {
                SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by TM ");
            } else {
                SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by TM ", true);
            }

            //validate the schedule table when group by Job Title
            selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyJobTitle.getValue());
            if (areListElementVisible(scheduleShiftTitles, 10) && scheduleShiftTitles.size() > 0) {
                SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by job title");
            } else {
                SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by job title ", false);
            }

            //validate the schedule table when group by Location
            if(isLocationGroup){
                selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyLocation.getValue());
                if (areListElementVisible(scheduleShiftTitles, 10) && scheduleShiftTitles.size() > 0
                        && areListElementVisible(groupByLocationToggles, 10) && groupByLocationToggles.size()> 0
                        && groupByLocationToggles.size() == scheduleShiftTitles.size()) {
                    SimpleUtils.pass("In Week view: Shifts in schedule table are grouped by Location");

                    // Check the sub-location display in alphabetical order
                    List<String>  locationsNamesInSchedule = new ArrayList<>();
                    for(int i = 0; i< scheduleShiftTitles.size(); i++){
                        locationsNamesInSchedule.add(scheduleShiftTitles.get(i).getText());
                    }
                    List<String>  locationsNamesBySorted = new ArrayList<>();
                    locationsNamesBySorted.addAll(locationsNamesInSchedule);
                    locationsNamesInSchedule.sort(null);

                    if(locationsNamesBySorted.equals(locationsNamesInSchedule)){
                        SimpleUtils.pass("In Week view: Sub-location display in alphabetical order when grouped by Location");
                    } else
                        SimpleUtils.fail("In Week view: Sub-location are not display in alphabetical order when grouped by Location", false);

                    //Check the first location will be opened
                    if(groupByLocationToggles.get(0).getAttribute("class").contains("open")){
                        SimpleUtils.pass("In Week view: The first location is opened ");
                    } else
                        SimpleUtils.fail("In Week view: The first location is not opened ", false);

                    //Check all locations can be expanded and closed
                    int randomIndex = (new Random()).nextInt(groupByLocationToggles.size()-1)+1;

                    if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("close")){
                        scheduleShiftTitles.get(randomIndex).click();
                        if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("open")){
                            SimpleUtils.pass("In Week view: Location can be expanded ");
                        } else
                            SimpleUtils.fail("In Week view: Location cannot be expanded ", false);

                        scheduleShiftTitles.get(randomIndex).click();
                        if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("close")){
                            SimpleUtils.pass("In Week view: Location can be closed ");
                        } else
                            SimpleUtils.fail("In Week view: Location cannot be closed ", false);
                    } else if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("open")){
                        scheduleShiftTitles.get(randomIndex).click();
                        if(groupByLocationToggles.get(randomIndex).getAttribute("class").contains("close")){
                            SimpleUtils.pass("In Week view: Location can be closed ");
                        } else
                            SimpleUtils.fail("In Week view: Location cannot be closed ", false);
                    }
                } else {
                    SimpleUtils.fail("In Week view: Shifts in schedule table are failed group by Location ", false);
                }
            }

            //change back to Group by All
            selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyAll.getValue());
        }
        else SimpleUtils.fail("Group By Selector is not available on screen ", true);
    }

    @Override
    public void isShiftTitleExist(ArrayList<String> shiftTitle) throws Exception {
        if (isElementLoaded(groupBySelector, 5)) {
            selectGroupByFilter(ConsoleScheduleNewUIPage.scheduleGroupByFilterOptions.groupbyPattern.getValue());
            if (areListElementVisible(scheduleShiftTitles, 10) && scheduleShiftTitles.size() > 0) {
                int count = 0;
                for (int i = 0; i < shiftTitle.size(); i++) {
                    for (WebElement weekScheduleShiftTitle : scheduleShiftTitles)
                        if (!shiftTitle.get(i).trim().equalsIgnoreCase(weekScheduleShiftTitle.getText().trim())) {
                            count = count + 1;
                            continue;
                        }
                    if (count == scheduleShiftTitles.size())
                        SimpleUtils.fail("No matches shift title!", false);
                }
            } else if (areListElementVisible(dayScheduleShiftTitles, 10) && dayScheduleShiftTitles.size() > 0) {
                int count = 0;
                for (int i = 0; i < shiftTitle.size(); i++) {
                    for (WebElement dayScheduleShiftTitle : dayScheduleShiftTitles)
                        if (!shiftTitle.get(i).trim().equalsIgnoreCase(dayScheduleShiftTitle.getText().trim())) {
                            count = count + 1;
                            continue;
                        }
                    if (count == dayScheduleShiftTitles.size())
                        SimpleUtils.fail("No matches shift title!", false);
                }
            } else
                SimpleUtils.fail("In schedule view: Shift titles are not loaded correctly ", false);
        }else
            SimpleUtils.fail("Group by section is not loaded!", false);
    }


    @FindBy(css = ".save-schedule-confirm-message2")
    private WebElement saveMessage;
    @Override
    public void verifyVersionInSaveMessage(String version) throws Exception {
        if (isElementEnabled(scheduleSaveBtn)) {
            clickTheElement(scheduleSaveBtn);
        } else {
            SimpleUtils.fail("Schedule save button not found", false);
        }
        waitForSeconds(3);
        String a= saveMessage.getText();
        if (isElementLoaded(saveMessage,15) && a.contains(version)){
            SimpleUtils.pass("version info is correct!");
        } else {
            SimpleUtils.fail("There is no save message or the version is incorrect!", false);
        }
        if (isElementEnabled(saveOnSaveConfirmationPopup)) {
            clickTheElement(saveOnSaveConfirmationPopup);
            if (isElementLoaded(msgOnTop, 30) && msgOnTop.getText().contains("Success")) {
                SimpleUtils.pass("Save the Schedule Successfully!");
            } else {
                SimpleUtils.fail("Save Schedule Failed!", false);
            }
        } else {
            SimpleUtils.fail("Schedule save button not found", false);
        }
    }


    @FindBy(xpath = "//span[text()=\"Manager\"]")
    private WebElement managerTab;
    @Override
    public void clickOnManagerButton() throws Exception {
        if (isElementEnabled(managerTab, 5)) {
            click(managerTab);
            SimpleUtils.pass("Manager button is clickable");
        }else {
            SimpleUtils.fail("There is no Manager button!",true);
        }
    }


    @FindBy(css = "[ng-click=\"openSearchBox()\"]")
    private WebElement openSearchBoxButton;

    @FindBy(css = "[ng-click=\"closeSearchBox()\"]")
    private WebElement closeSearchBoxButton;

    @FindBy(css = "input[placeholder*=\"Search by Employee Name, Work Role")
    private WebElement searchBox;

    @FindBy(css = "div[ng-show=\"!forbidModeChange\"]")
    private WebElement switchDayViewAndWeeKViewButton;



    public void verifyGhostTextInSearchBox () throws Exception{
        if (isElementEnabled(searchBox, 5)) {
            String ghostText = "Search by Employee Name, Work Role or Title";
            if (searchBox.getAttribute("placeholder").equals(ghostText)) {
                SimpleUtils.pass("The ghost text in search box display correctly");
            } else
                SimpleUtils.fail("The ghost text in search box display incorrectly",true);

        } else {
            SimpleUtils.fail("Search box on schedule page load fail!",false);
        }
    }

    public void clickOnOpenSearchBoxButton() throws Exception {
        if (isElementEnabled(openSearchBoxButton, 5) && isClickable(openSearchBoxButton, 5)) {
            clickTheElement(openSearchBoxButton);
            if (isElementLoaded(searchBox, 15)) {
                SimpleUtils.pass("Search box is opened successfully");
            } else {
                SimpleUtils.fail("Search box is not opened successfully", false);
            }

        }else {
            SimpleUtils.fail("There is no Open search box button!",false);
        }
    }

    public void clickOnCloseSearchBoxButton() throws Exception {
        if (isElementEnabled(closeSearchBoxButton, 5)) {
            clickTheElement(closeSearchBoxButton);
            if (!isElementEnabled(searchBox, 5)) {
                SimpleUtils.pass("Search box is closed successfully");
            } else {
                SimpleUtils.fail("Search box is not closed successfully", false);
            }
        }else {
            SimpleUtils.report("There is no Close search box button!");
        }
    }


    @FindBy(className = "week-schedule-shift")
    private List<WebElement> weekShifts;
    public List<WebElement> searchShiftOnSchedulePage(String searchText) throws Exception {
        List<WebElement> searchResult = null;
        if (isElementEnabled(searchBox, 5)) {
            searchBox.clear();
            waitForSeconds(3);
            searchBox.sendKeys(searchText);
            waitForSeconds(5);
            if (areListElementVisible(weekShifts, 5) && weekShifts.size() >0) {
                searchResult = weekShifts;
            } else if (areListElementVisible(dayViewAvailableShifts, 5) && dayViewAvailableShifts.size() >0) {
                searchResult = dayViewAvailableShifts;
            } else
                SimpleUtils.report("Cannot search on schedule page!");
        } else {
            SimpleUtils.fail("Search box on schedule page load fail!",false);
        }
        return searchResult;
    }


    @FindBy (className = "lg-filter__category-label")
    private List<WebElement> filterLabels;

    @Override
    public void verifyFilterDropdownList(boolean isLG) throws Exception {
        if (isElementLoaded(filterPopup,5)) {
            HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
            if (!availableFilters.get("location").isEmpty()) {
                if (isLG)
                    SimpleUtils.pass("Schedule Page: 'LOCATION' is one label when current env is LG");
                else
                    SimpleUtils.fail("Schedule Page: 'LOCATION' should not be one label when current env isn't LG", false);
            } else
                SimpleUtils.report("Schedule Page: 'LOCATION' isn't one label currently");
            if (availableFilters.get("shifttype").size() >= 7 && availableFilters.get("jobtitle").size() > 1 && availableFilters.get("workrole").size() > 1)
                SimpleUtils.pass("Schedule Page: 'SHIFT TYPE'/'JOB TITLE/'WORK ROLE' display as expected");
            else
                SimpleUtils.fail("Schedule Page: 'SHIFT TYPE'/'JOB TITLE/'WORK ROLE' display unexpectedly", false);
        } else
            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
    }

    @FindBy (css = ".lg-filter__clear")
    private WebElement clearFilterOnFilterDropdownPopup;
    public void clickOnClearFilterOnFilterDropdownPopup() throws Exception {
        if(isElementLoaded(clearFilterOnFilterDropdownPopup, 15)){
            if(clearFilterOnFilterDropdownPopup.getAttribute("class").contains("active")){
                scrollToElement(clearFilterOnFilterDropdownPopup);
                clickTheElement(clearFilterOnFilterDropdownPopup);
                waitForSeconds(2);
                SimpleUtils.pass("Click Clear Filter button on Filter dropdown popup successfully! ");
            } else if (!clearFilterOnFilterDropdownPopup.getAttribute("class").contains("active")) {
                clickTheElement(filterButton);
                waitForSeconds(2);
                SimpleUtils.report("Clear filter button is disabled because there is no filters been selected! ");
            }

            if (areListElementVisible(wholeWeekShifts, 20) || areListElementVisible(dayViewAvailableShifts, 5)) {
                SimpleUtils.pass("Shifts list grid has been loaded!");
            } else {
                SimpleUtils.fail("Shifts list grid loading failed!", false);
            }
        } else
            SimpleUtils.fail("Clear Filter button loaded fail! ", false);
    }


    @FindBy(css = "lg-button[label*=\"ublish\"]")
    private WebElement publishSheduleButton;
    @Override
    public String getTooltipOfPublishButton() throws Exception {
        String tooltip = "";
        if (isElementLoaded(publishSheduleButton, 5)) {
            tooltip = publishSheduleButton.getAttribute("data-tootik");
        } else
            SimpleUtils.fail("Publish schedule button fail to load! ", false);
        return tooltip;
    }


    // Added by Nora
    @FindBy (css = "lg-button-group[buttons*=\"custom\"] div.lg-button-group-first")
    private WebElement scheduleDayViewButton;
    @FindBy (className = "period-name")
    private WebElement periodName;
    @FindBy (className = "card-carousel-container")
    private WebElement cardCarousel;
    @FindBy(css = "table tr:nth-child(1)")
    private WebElement scheduleTableTitle;
    @FindBy(css = "table tr:nth-child(2)")
    private WebElement scheduleTableHours;
    @FindBy(className = "week-day-multi-picker-day")
    private List<WebElement> weekDays;
    @FindBy(css = "[ng-show=\"hasSearchResults()\"] [ng-repeat=\"worker in searchResults\"]")
    private List<WebElement> searchResults;
    @FindBy(className = "week-schedule-shift-wrapper")
    private List<WebElement> shiftsWeekView;
    @FindBy(css = "div.popover div:nth-child(3)>div.ng-binding")
    private WebElement timeDuration;
    @FindBy(className = "sch-calendar-day-label")
    private List<WebElement> weekDayLabels;
    //    @FindBy(css = ".schedule-summary-search-dropdown [icon*=\"search.svg'\"]")
//    private WebElement searchLocationBtn;
    @FindBy(css = ".redesigned-modal-icon")
    private WebElement deleteScheduleIcon;
    @FindBy(css = ".redesigned-modal-title")
    private WebElement deleteScheduleTitle;
    @FindBy(css = ".redesigned-modal-text")
    private WebElement deleteScheduleText;
    @FindBy(css = "[label*=\"Delete Schedule\"]")
    private WebElement deleteScheduleWeek;


    @FindBy (css = "lg-button[ng-click=\"deleteSchedule()\"]")
    private WebElement deleteScheduleButton;

    @FindBy (css = "div.redesigned-modal")
    private WebElement deleteSchedulePopup;

    @FindBy (css = ".redesigned-modal input")
    private WebElement deleteScheduleCheckBox;

    @FindBy (css = ".redesigned-button-ok")
    private WebElement deleteButtonOnDeleteSchedulePopup;

    @FindBy (css = ".redesigned-button-cancel-gray")
    private WebElement cancelButtonOnDeleteSchedulePopup;

    @FindBy (className = "day-week-picker-date")
    private WebElement calMonthYear;

    @Override
    public void verifyClickOnCancelBtnOnDeleteScheduleDialog() throws Exception {
        try {
            if (isElementLoaded(cancelButtonOnDeleteSchedulePopup, 5)) {
                clickTheElement(cancelButtonOnDeleteSchedulePopup);
                waitForSeconds(2);
                if (!isElementLoaded(deleteSchedulePopup, 5)) {
                    SimpleUtils.pass("Delete Schedule Dialog: Click on Cancel button successfully!");
                } else {
                    SimpleUtils.fail("Delete Schedule Dialog: Click on Cancel button failed!", false);
                }
            } else {
                SimpleUtils.fail("Delete Schedule Dialog: Cancel button is not loaded successfully!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Override
    public void verifyDeleteBtnDisabledOnDeleteScheduleDialog() throws Exception {
        try {
            if (isElementLoaded(deleteButtonOnDeleteSchedulePopup, 5) &&
                    deleteButtonOnDeleteSchedulePopup.getAttribute("disabled").equalsIgnoreCase("true")) {
                SimpleUtils.pass("Delete Schedule Dialog: Delete button is disabled by default!");
            } else {
                SimpleUtils.fail("Delete Schedule Dialog: Delete button is not disabled by default!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Override
    public void verifyDeleteButtonEnabledWhenClickingCheckbox() throws Exception {
        try {
            if (isElementLoaded(deleteScheduleCheckBox, 5)) {
                clickTheElement(deleteScheduleCheckBox);
                if (isElementLoaded(deleteButtonOnDeleteSchedulePopup, 5) && deleteButtonOnDeleteSchedulePopup.getAttribute("disabled") == null) {
                    SimpleUtils.pass("Delete Schedule Dialog: Delete Button is enabled when clicking the checkbox!");
                } else {
                    SimpleUtils.fail("Delete Schedule Dialog: Delete Button is not enabled when clicking the checkbox!", false);
                }
            } else {
                SimpleUtils.fail("Delete Schedule Dialog: Check box is not loaded successfully!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Override
    public String getDeleteScheduleForWhichWeekText() throws Exception {
        String scheduleWeekText = "";
        String year = "";
        if (isElementLoaded(weekPeriod, 5) && isElementLoaded(calMonthYear, 5)) {
            if (calMonthYear.getText().split(" ").length == 5) {
                year = calMonthYear.getText().trim().substring(4, 8);
            } else {
                year = calMonthYear.getText().trim().substring(calMonthYear.getText().trim().length() - 4);
            }
            String [] items = weekPeriod.getText().split(" ");
            if (items.length == 7) {
                scheduleWeekText = "Delete " + items[0] + " " + items[1] + " " + items[2].substring(0, 3) + " " + items[3]
                        + " " + items[4] + " " + items[5].substring(0, 3) + " " + items[6] + ", " + year;
                SimpleUtils.report("Delete Schedule For Which Weeek Text: " + scheduleWeekText);
            }
        }
        if (scheduleWeekText == "") {
            SimpleUtils.fail("Failed to get the delete schedule for which week Text", false);
        }
        return scheduleWeekText;
    }

    @Override
    public void verifyTheContentOnDeleteScheduleDialog(String confirmMessage, String week) throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(deleteSchedulePopup, 30)) {
            if (isElementLoaded(deleteScheduleIcon, 5) && isElementLoaded(deleteScheduleTitle, 5)
                    && deleteScheduleTitle.getText().equalsIgnoreCase("Delete Schedule") && isElementLoaded(deleteScheduleTitle, 5)
                    && deleteScheduleText.getText().equalsIgnoreCase(confirmMessage) && isElementLoaded(deleteScheduleWeek, 5)
                    && deleteScheduleWeek.getText().toLowerCase().contains(week.toLowerCase()) && isElementLoaded(cancelButtonOnDeleteSchedulePopup, 5)
                    && isElementLoaded(deleteButtonOnDeleteSchedulePopup, 5) && isElementLoaded(deleteScheduleCheckBox, 5)) {
                SimpleUtils.pass("Delete Schedule Dialog: Verified the content is correct!");
            } else {
                SimpleUtils.fail("Delete Schedule Dialog: The content is unexpected!", false);
            }
        } else {
            SimpleUtils.fail("Delete Schedule Dialog failed to pop up!", false);
        }
    }

    @Override
    public void verifyClickOnDeleteScheduleButton() throws Exception {
        try {
            if (isElementLoaded(deleteScheduleButton, 10)) {
                clickTheElement(deleteScheduleButton);
                SimpleUtils.pass("Schedule: Click on Delete Schedule button Successfully!");
            } else {
                SimpleUtils.fail("Schedule: Delete Schedule button is not loaded Successfully!", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail(e.getMessage(), false);
        }
    }

    @Override
    public boolean isDeleteScheduleButtonLoaded() throws Exception {
        try {
            if (isElementLoaded(deleteScheduleButton, 5)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void verifyLocationFilterInLeft(boolean isLG) throws Exception {
        if (isElementLoaded(filterPopup,5)) {
            if (isLG) {
                if (filterLabels.size() == 4 && filterLabels.get(0).getText().equals("LOCATION"))
                    SimpleUtils.pass("Schedule Page: 'LOCATION' displays in left when current env is LG");
                else
                    SimpleUtils.fail("Schedule Page: 'LOCATION' is not in the left when current env is LG",false);
            } else {
                if (filterLabels.size() == 3 && !filterLabels.get(0).getText().equals("LOCATION"))
                    SimpleUtils.pass("Schedule Page: 'LOCATION' doesn't display when current env isn't LG");
                else
                    SimpleUtils.fail("Schedule Page: Filter displays unexpectedly when current env isn't LG", false);
            }
        } else
            SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
    }

    @Override
    public String selectRandomChildLocationToFilter() throws Exception {
        String randomLocation = "";
        int randomIndex = 0;
        if (isElementLoaded(filterPopup,5)) {
            HashMap<String, ArrayList<WebElement>> availableFilters = getAvailableFilters();
            if (!availableFilters.get("location").isEmpty()) {
                randomIndex = (new Random()).nextInt(availableFilters.get("location").size());
                randomLocation = availableFilters.get("location").get(randomIndex).getText();
                randomLocation = randomLocation.contains(" ")? randomLocation.split(" ")[0]: "";
                selectLocationFilterByText(randomLocation);
            } else
                SimpleUtils.report("Schedule Page: 'LOCATION' isn't one label currently");
        } else
            SimpleUtils.fail("Schedule Page: The drop down list does not pop up", false);
        return randomLocation;
    }


    @Override
    public void verifyShiftTypeInLeft(boolean isLG) throws Exception {
        if (isElementLoaded(filterPopup,5)) {
            if (isLG) {
                if (isElementLoaded(filterPopup,5)) {
                    if (filterLabels.size() == 4 && filterLabels.get(1).getText().equals("SHIFT TYPE"))
                        SimpleUtils.pass("Schedule Page: 'SHIFT TYPE' displays in left expect Location");
                    else
                        SimpleUtils.fail("Schedule Page: 'SHIFT TYPE' is not in the left expect Location",false);
                } else
                    SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
            } else {
                if (isElementLoaded(filterPopup,5)) {
                    if (filterLabels.size() >= 3 && filterLabels.get(0).getText().equals("SHIFT TYPE"))
                        SimpleUtils.pass("Schedule Page: 'SHIFT TYPE' displays in left");
                    else
                        SimpleUtils.fail("Schedule Page: 'SHIFT TYPE' is not in the left",false);
                } else
                    SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
            }
        } else
            SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
    }

    @Override
    public void verifyShiftTypeFilters() throws Exception {
        if (isElementLoaded(filterPopup,5)) {
            String shiftTypeFilterKey = "shifttype";
            ArrayList<WebElement> shiftTypeFilters = getAvailableFilters().get(shiftTypeFilterKey);
            if (shiftTypeFilters.size() >= 7) {
                if ((shiftTypeFilters.get(0).getText().contains("Action Required")
                        && shiftTypeFilters.get(1).getText().contains("Assigned")
                        && shiftTypeFilters.get(2).getText().contains("Compliance Review")
                        && shiftTypeFilters.get(3).getText().contains("New or Borrowed TM")
                        && shiftTypeFilters.get(4).getText().contains("Not Acknowledged")
                        && shiftTypeFilters.get(5).getText().contains("Open")
                        && shiftTypeFilters.get(6).getText().contains("Swap/Cover Requested")
                        && shiftTypeFilters.get(7).getText().contains("Unavailable")
                        && shiftTypeFilters.get(8).getText().contains("Unpublished changes"))||(
                        shiftTypeFilters.get(0).getText().contains("Action Required")
                                && shiftTypeFilters.get(1).getText().contains("Assigned")
                                && shiftTypeFilters.get(2).getText().contains("Compliance Review")
                                && shiftTypeFilters.get(3).getText().contains("Not Acknowledged")
                                && shiftTypeFilters.get(4).getText().contains("Open")
                                && shiftTypeFilters.get(5).getText().contains("Swap/Cover Requested")
                                && shiftTypeFilters.get(6).getText().contains("Unavailable")
                                && shiftTypeFilters.get(7).getText().contains("Unpublished changes")
                        ) || (shiftTypeFilters.get(0).getText().contains("Action Required")
                                && shiftTypeFilters.get(1).getText().contains("Assigned")
                                && shiftTypeFilters.get(2).getText().contains("Compliance Review")
                                && shiftTypeFilters.get(3).getText().contains("Open")
                                && shiftTypeFilters.get(4).getText().contains("Swap/Cover Requested")
                                && shiftTypeFilters.get(5).getText().contains("Unavailable")
                                && shiftTypeFilters.get(6).getText().contains("Unpublished changes")) ||
                        (shiftTypeFilters.get(0).getText().contains("Action Required")
                                && shiftTypeFilters.get(1).getText().contains("Assigned")
                                && shiftTypeFilters.get(2).getText().contains("Compliance Review")
                                && shiftTypeFilters.get(3).getText().contains("New or Borrowed TM")
                                && shiftTypeFilters.get(4).getText().contains("Open")
                                && shiftTypeFilters.get(5).getText().contains("Swap/Cover Requested")
                                && shiftTypeFilters.get(6).getText().contains("Unavailable")
                                && shiftTypeFilters.get(7).getText().contains("Unpublished changes"))){
                    SimpleUtils.pass("The shift types display correctly in Filter dropdown list! ");
                } else
                    SimpleUtils.fail("The shift types display incorrectly in Filter dropdown list! ", false);
            } else
                SimpleUtils.fail("The shift types count display incorrectly in Filter dropdown list! ", false);
        } else
            SimpleUtils.fail("Schedule Page: The drop down list does not pop up",false);
    }


    @FindBy(css = "label.input-label.ng-binding")
    private List<WebElement> allFilterText;

    public int getSpecificFiltersCount (String filterText) throws Exception {
        int count = 0;
        if (areListElementVisible(allFilterText, 10) && allFilterText.size()>0){
            for (WebElement filter: allFilterText){
                String [] fullText= filter.getText().split("\\(");
                String filterName = fullText[0].trim();
                String filterCount = fullText[1].replace(")", "");
                if (filterName.equalsIgnoreCase(filterText)) {
                    count = Integer.parseInt(filterCount);
                }
            }
        } else
            SimpleUtils.fail("Filter text in schedule filter dropdown list fail to load! ", false);
        return count;
    }


    @Override
    public boolean isGroupByDayPartsLoaded() throws Exception {
        Select groupBySelectElement = new Select(scheduleGroupByButton);
        List<WebElement> scheduleGroupByButtonOptions = groupBySelectElement.getOptions();
        for (WebElement scheduleGroupByButtonOption: scheduleGroupByButtonOptions) {
            if (scheduleGroupByButtonOption.getText().contains("Group by Day Parts"))
                return true;
        }
        return false;
    }

    @FindBy(css = ".popover-content")
    private WebElement shiftInfoPopup;

    @Override
    public void closeShiftInfoPopup() throws Exception {
        if (areListElementVisible(wholeWeekShifts,15)) {
            if (isElementLoaded(shiftInfoPopup, 5)) {
                click(shiftInfoPopup);
                waitForSeconds(3);
                if (isElementLoaded(shiftInfoPopup, 5)) {
                    SimpleUtils.fail("Shift info popup is not closed!", false);
                }
            }
        }
    }

    @Override
    public void clickOnFilterBtn() throws Exception {
        closeShiftInfoPopup(); // handle If the shift info pop covered the shift filter input box
        if (isElementLoaded(filterButton,30)) {
            clickTheElement(filterButton);
            waitForSeconds(2);
            SimpleUtils.pass("filter button is clickable");
        } else {
            SimpleUtils.fail("filter button is not Loaded Successfully!", true);
        }
    }

    @FindBy(xpath = "//div[contains(text(),'Work Role')]/parent::div")
    private WebElement workRoleFilterSection;
    @Override
    public ArrayList<HashMap<String, String>> getWorkRoleInfoFromFilter() throws Exception {
        ArrayList<HashMap<String,String>> workRoleInfo = new ArrayList<>();
        if (workRoleFilterSection.findElements(By.cssSelector(".lg-filter__category-items .input-label")).size() > 0) {
            for (WebElement row : workRoleFilterSection.findElements(By.cssSelector(".lg-filter__category-items .input-label"))) {
                HashMap<String, String> workRoleInfoInEachRow = new HashMap<>();
                workRoleInfoInEachRow.put("WorkRoleName", row.getText().substring(0, row.getText().indexOf("(")).trim().toLowerCase());
                workRoleInfoInEachRow.put("WorkRoleStyle", row.findElement(By.cssSelector("span")).getAttribute("style"));
                workRoleInfo.add(workRoleInfoInEachRow);
            }
        }
        return workRoleInfo;
    }

    @FindBy(css = "div[ng-class*=\"staffing.guidance\"]")
    private WebElement staffSectionFromToggleSummaryView;
    @Override
    public ArrayList<HashMap<String, String>> getToggleSummaryStaffWorkRoleStyleInfo() throws Exception {
        ArrayList<HashMap<String,String>> workRoleInfo = new ArrayList<>();
        if (staffSectionFromToggleSummaryView.findElements(By.cssSelector("tr[ng-repeat*=\"summary.staffingGuidance.roleHours\"]")).size() > 0) {
            for (WebElement row : staffSectionFromToggleSummaryView.findElements(By.cssSelector("tr[ng-repeat*=\"summary.staffingGuidance.roleHours\"]"))) {
                HashMap<String, String> workRoleInfoInEachRow = new HashMap<>();
                workRoleInfoInEachRow.put("WorkRoleName", row.findElement(By.cssSelector("td[class = \"ng-binding\"]")).getText().trim().toLowerCase());
                workRoleInfoInEachRow.put("WorkRoleStyle", row.findElement(By.cssSelector("td[style]")).getAttribute("style"));
                workRoleInfo.add(workRoleInfoInEachRow);
            }
        }
        return workRoleInfo;
    }

    public void selectJobTitleFilterByText(String filterText) throws Exception {
        String filterKey = "jobtitle";
        ArrayList<WebElement> jobTitleFilters = getAvailableFilters().get(filterKey);
        unCheckFilters(jobTitleFilters);
        for (WebElement jobTitleOption : jobTitleFilters) {
            if (jobTitleOption.getText().toLowerCase().contains(filterText.toLowerCase())) {
                click(jobTitleOption);
                break;
            }
        }
        if (!filterPopup.getAttribute("class").toLowerCase().contains("ng-hide"))
            click(filterButton);
    }

    @Override
    public boolean verifyDisplayOrderWhenGroupingByWorkRole(HashMap<String, Integer> workRoleNOrders) throws Exception {
        boolean isConsistent = true;
        if (areListElementVisible(scheduleShiftTitles, 3)) {
            for (int i = 0; i < scheduleShiftTitles.size() - 1; i++) {
                int order1 = workRoleNOrders.get(scheduleShiftTitles.get(i).getText().toLowerCase().replaceAll(" ", ""));
                int order2 = workRoleNOrders.get(scheduleShiftTitles.get(i + 1).getText().toLowerCase().replaceAll(" ", ""));
                if (order1 > order2) {
                    isConsistent = false;
                    break;
                }
            }
        } else if (areListElementVisible(scheduleShiftTitlesDayView, 3)) {
            for (int i = 0; i < scheduleShiftTitlesDayView.size() - 1; i++) {
                int order1 = workRoleNOrders.get(scheduleShiftTitlesDayView.get(i).getText().toLowerCase().replaceAll(" ", ""));
                int order2 = workRoleNOrders.get(scheduleShiftTitlesDayView.get(i + 1).getText().toLowerCase().replaceAll(" ", ""));
                if (order1 > order2) {
                    isConsistent = false;
                    break;
                }
            }
        } else {
            isConsistent = false;
        }
        return isConsistent;
    }

    @Override
    public boolean areDisplayOrderCorrectOnFilterPopup(HashMap<String, Integer> workRoleNOrders) throws Exception {
        boolean isConsistent = true;
        ArrayList<WebElement> availableWorkRoleFilters = getAvailableFilters().get("workrole");
        for (int i = 0; i < availableWorkRoleFilters.size() - 1; i++) {
            int order1 = workRoleNOrders.get(availableWorkRoleFilters.get(i).getText().toLowerCase().
                    substring(0, availableWorkRoleFilters.get(i).getText().toLowerCase().indexOf('(')).trim().replaceAll(" ", ""));
            int order2 = workRoleNOrders.get(availableWorkRoleFilters.get(i + 1).getText().toLowerCase().
                    substring(0, availableWorkRoleFilters.get(i + 1).getText().toLowerCase().indexOf('(')).trim().replaceAll(" ", ""));
            if (order1 > order2) {
                isConsistent = false;
                break;
            }
        }
        return isConsistent;
    }
    
    @FindBy(css = "[ng-repeat=\"r in summary.staffingGuidance.roleHours\"] [class=\"ng-binding\"]")
    private List<WebElement> staffWorkRoles;
    public List<String> getStaffWorkRoles () {
        List<String> workRoles = new ArrayList<>();
        if (areListElementVisible(staffWorkRoles, 10) && staffWorkRoles.size()>0) {
            for (int i=0;i<staffWorkRoles.size(); i++) {
                String workRole = staffWorkRoles.get(i).getText();
                workRoles.add(workRole);
                SimpleUtils.pass("Get staff work role "+workRole+" successfully!");
            }
        } else
            SimpleUtils.fail("Staff work roles fail to load! ", false);
        return workRoles;
    }

    public List<String> getSpecificFilterNames (String filterText) throws Exception {
        List<String> names = new ArrayList<>();
        ArrayList<WebElement> availableFilters = getAvailableFilters().get(filterText.toLowerCase().replace(" ", ""));
        if (availableFilters != null && availableFilters.size()>0){
            for (int i = 0; i < availableFilters.size(); i++) {
                String name = availableFilters.get(i).getText().split("\\(")[0].trim();
                names.add(name);
                SimpleUtils.pass("Get name: "+name +" from "+filterText+" list successfully! ");
            }
        } else
            SimpleUtils.report("There is no this filter: "+filterText);
        return names;
    }

    @FindBy(css = "[label=\"Create schedule\"] button:not([disabled])")
    private WebElement generateScheduleButton;
    public boolean isScheduleMainPageLoaded () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(editScheduleButton, 5)
                ||isElementLoaded(generateScheduleButton, 5)) {
            isLoaded = true;
            SimpleUtils.pass("Schedule main page is loaded successfully! ");
        } else
            SimpleUtils.report("Schedule main page is not loaded! ");
        return isLoaded;
    }

    public boolean isManagerViewSelected () throws Exception {
        boolean isManagerViewSelected = false;
        if(isElementEnabled(activScheduleType,5)){
            if(activScheduleType.getText().equalsIgnoreCase("Suggested")){
                SimpleUtils.report("The Suggested tab is selected! ");
            }else{
                isManagerViewSelected = true;
                SimpleUtils.report("The Manager tab is selected! ");
            }
        }else{
            SimpleUtils.fail("Schedule Type " + scheduleTypeManager.getText() + " is disabled",false);
        }
        return isManagerViewSelected;
    }

    @FindBy(css = ".lg-toast [ng-if=highlightText]")
    private WebElement successMsgOnTop;
    @FindBy(css = ".lg-toast [class*=lg-toast__simple-text]")
    private WebElement NoChangeMsgOnTop;
    @Override
    public void saveScheduleWithoutChange() throws Exception {
        if (isElementEnabled(scheduleSaveBtn, 10) && isClickable(scheduleSaveBtn, 10)) {
            scrollToElement(scheduleSaveBtn);
            clickTheElement(scheduleSaveBtn);
            if (isElementLoaded(successMsgOnTop, 5) && isElementLoaded(NoChangeMsgOnTop, 5)) {
                if (successMsgOnTop.getText().contains("Success") && NoChangeMsgOnTop.getText().contains("No changes to be saved"))
                    SimpleUtils.pass("Save the Schedule without modification successfully!");
                else
                    SimpleUtils.fail("Saved message is not correct!", false);
            }else
                SimpleUtils.fail("Saved successfully message is not loaded!", false);
        }else {
            SimpleUtils.fail("Schedule save button not found", false);
        }
    }

    private boolean isSaveConfirmPopupLoaded() throws Exception {
        waitForSeconds(35);
        boolean isLoaded = false;
        if (isElementLoaded(saveOnSaveConfirmationPopup, 45)) {
            isLoaded =true;
            SimpleUtils.pass("Schedule save button is loaded successfully! ");
        } else {
            SimpleUtils.report("Schedule save button not found");
        }
        return isLoaded;
    }

    @FindBy(css = "lg-filter[label=\"Work Role\"] [ng-click=\"$ctrl.openFilter()\"]")
    private WebElement workRoleFilterButton;
    @FindBy(css = "lg-filter[label=\"Work Role\"] input[type=\"checkbox\"]")
    private List<WebElement> workRoleCheckBoxes;
    @Override
    public void checkAllWorkRolesUnderLabor() throws Exception {
        if (isElementLoaded(workRoleFilterButton, 10) && isClickable(workRoleFilterButton, 10)) {
            scrollToElement(workRoleFilterButton);
            clickTheElement(workRoleFilterButton);
            if (areListElementVisible(workRoleCheckBoxes,10)) {
                for (WebElement checkBox : workRoleCheckBoxes){
                    if(checkBox.getAttribute("class").contains("ng-empty")){
                        scrollToElement(checkBox);
                        click(checkBox);
                    }
                }
            }else
                SimpleUtils.fail("Work role list is not loaded!", false);
        }else
            SimpleUtils.fail("Work role filter box is not found!", false);
    }

    @Override
    public void clickWorkRoleFilterOfLabor() throws Exception {
        if (isElementLoaded(workRoleFilterButton, 10) && isClickable(workRoleFilterButton, 10)) {
            scrollToElement(workRoleFilterButton);
            clickTheElement(workRoleFilterButton);
        }else
            SimpleUtils.fail("Work role filter box is not found!", false);
    }

}
