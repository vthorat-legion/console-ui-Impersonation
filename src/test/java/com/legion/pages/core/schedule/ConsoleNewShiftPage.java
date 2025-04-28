package com.legion.pages.core.schedule;

import com.legion.pages.BasePage;
import com.legion.pages.NewShiftPage;
import com.legion.tests.core.ScheduleTestKendraScott2;
import com.legion.utils.Constants;
import com.legion.utils.JsonUtil;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.legion.tests.core.ScheduleTestKendraScott2.staffingOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.legion.utils.MyThreadLocal.*;
import static com.legion.utils.MyThreadLocal.setWorkerLocation;

public class ConsoleNewShiftPage extends BasePage implements NewShiftPage{
    public ConsoleNewShiftPage() {
        PageFactory.initElements(getDriver(), this);
    }

    private static HashMap<String, String> parameterMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/envCfg.json");
    private static HashMap<String, String> propertySearchTeamMember = JsonUtil.getPropertiesFromJsonFile("src/test/resources/SearchTeamMember.json");
    private static HashMap<String, String> propertyCustomizeMap = JsonUtil.getPropertiesFromJsonFile("src/test/resources/ScheduleCustomizeNewShift.json");

    @FindBy(css = ".modal-content")
    private WebElement customizeNewShift;

    @FindBy(css = "div.lgn-time-slider-notch-selector-end")
    private WebElement sliderNotchEnd;

    @FindBy(css = "div.lgn-time-slider-notch.droppable")
    private List<WebElement> sliderDroppableCount;

    @FindBy(xpath = "//div[contains(@class,'lgn-time-slider-notch-selector-end')]/following-sibling::div[1]")
    private WebElement customizeShiftEnddayLabel;

    @FindBy(css = "div.lgn-time-slider-notch-selector-start")
    private WebElement sliderNotchStart;

    @FindBy(xpath = "//div[contains(@class,'lgn-time-slider-notch-selector-start')]/following-sibling::div[1]")
    private WebElement customizeShiftStartdayLabel;

    @FindBy(css = ".tma-staffing-option-radio-button")
    private List<WebElement> radioBtnStaffingOptions;

    @FindBy(css = ".tma-staffing-option-text-title")
    private List<WebElement> radioBtnShiftTexts;

    @FindBy(css = "button.sch-save")
    private WebElement btnSave;

    @FindBy(css = "[ng-click=\"handleNext()\"]")
    private WebElement btnSaveOnNewCreateShiftPage;

    public void customizeNewShiftPage() throws Exception
    {
        Thread.sleep(10000);
        if(isElementLoaded(customizeNewShift,35))
        {
            SimpleUtils.pass("Customize New Shift Page loaded Successfully!");
        }
        else
        {
            SimpleUtils.fail("Customize New Shift Page not loaded Successfully!",false);
        }
    }

    public void moveSliderAtSomePoint(String shiftTime, int shiftStartingCount, String startingPoint) throws Exception
    {
        if(startingPoint.equalsIgnoreCase("End")){
            if(isElementLoaded(sliderNotchEnd,10) && sliderDroppableCount.size()>0){
                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
                for(int i= shiftStartingCount; i<= sliderDroppableCount.size();i++){
                    if(i == (shiftStartingCount + Integer.parseInt(shiftTime))){
                        WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child("+(i+2)+")"));
                        mouseHoverDragandDrop(sliderNotchEnd,element);
                        WebElement ele = getDriver().findElement(By.xpath("//div[contains(@class,'lgn-time-slider-notch-selector-end')]/following-sibling::div[1]"));
                        String txt = ele.getAttribute("innerHTML");
                        if(customizeShiftEnddayLabel.getAttribute("class").contains("PM")){
                            MyThreadLocal.setScheduleHoursEndTime(customizeShiftEnddayLabel.getText() + ":00PM");
                            break;
                        }else{
                            MyThreadLocal.setScheduleHoursEndTime(customizeShiftEnddayLabel.getText() + ":00AM");
                            break;
                        }
                    }
                }

            }else{
                SimpleUtils.fail("Shift timings with Sliders not loading on page Successfully", false);
            }
        }else if(startingPoint.equalsIgnoreCase("Start")){
            if(isElementLoaded(sliderNotchStart,10) && sliderDroppableCount.size()>0){
                SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for Starting point");
                for(int i= shiftStartingCount; i<= sliderDroppableCount.size();i++){
                    if(i == (shiftStartingCount + Integer.parseInt(shiftTime))){
                        WebElement element = getDriver().findElement(By.cssSelector("div.lgn-time-slider-notch.droppable:nth-child("+(i+2)+")"));
                        mouseHoverDragandDrop(sliderNotchStart,element);
                        if(customizeShiftStartdayLabel.getAttribute("class").contains("AM")){
                            MyThreadLocal.setScheduleHoursStartTime(customizeShiftStartdayLabel.getText() + ":00AM");
                            break;
                        }else{
                            MyThreadLocal.setScheduleHoursStartTime(customizeShiftStartdayLabel.getText() + ":00PM");
                            break;
                        }
                    }
                }

            }else{
                SimpleUtils.fail("Shift timings with Sliders not loading on page Successfully", false);
            }
        }
    }



//    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/form/div/div[1]/div[7]//div[contains(@class,'react-select__placeholder')]")
    @FindBy(xpath = "//div[contains(@id,\"legion_cons_Schedule_Schedule_CreateShift_Assignment_menu\")]/div/div[contains(@class,'react-select__value-container')]/div[1]")
    private WebElement assignmentDropDownOnNewCreateShiftPage;
    @FindBy(className = "react-select__option")
    private List<WebElement> assignmentOptionsInDropDownList;
    public void clickRadioBtnStaffingOption(String staffingOption) throws Exception {
        scrollToBottom();
        waitForSeconds(1);
        if (areListElementVisible(radioBtnStaffingOptions, 5)
                && areListElementVisible(radioBtnShiftTexts, 5)) {
            boolean flag = false;
            int index = -1;
            if (radioBtnStaffingOptions.size() != 0 && radioBtnShiftTexts.size() != 0 &&
                    radioBtnStaffingOptions.size() == radioBtnShiftTexts.size()) {

                for (WebElement radioBtnShiftText : radioBtnShiftTexts) {
                    index = index + 1;
                    if (radioBtnShiftText.getText().contains(staffingOption)) {
                        click(radioBtnStaffingOptions.get(index));
                        SimpleUtils.pass(radioBtnShiftText.getText() + "Radio Button clicked Successfully!");
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    SimpleUtils.fail("No Radio Button Selected!", false);
                }

            } else {
                SimpleUtils.fail("Staffing option Radio Button is not clickable!", false);
            }
        }  else if (isElementLoaded(assignmentDropDownOnNewCreateShiftPage, 5)) {
            click(assignmentDropDownOnNewCreateShiftPage);
            SimpleUtils.pass("Assignment button clicked Successfully");
            if (assignmentOptionsInDropDownList.size() > 0) {
                for (WebElement assignmentOptions : assignmentOptionsInDropDownList) {
                    String option = assignmentOptions.getText().toLowerCase();
                    if (staffingOption.toLowerCase().contains("assign")) {
                        MyThreadLocal.setAssignTMStatus(true);
                    } else
                        MyThreadLocal.setAssignTMStatus(false);
                    if (option.contains(staffingOption.toLowerCase())) {
                        click(assignmentOptions);
                        SimpleUtils.pass(option + " been selected Successfully!");
                        break;
                    } else if(!option.contains("auto") && !staffingOption.toLowerCase().contains("auto")) {
                        click(assignmentOptions);
                        SimpleUtils.pass(option + " been selected Successfully!");
                        break;
                    }else {
                        SimpleUtils.report(option + " is not selected Successfully!");
                    }
                }
            } else {
                SimpleUtils.fail("Assignment options size are empty", false);
            }
        } else
            SimpleUtils.fail("Assignment options fail to load on create shift page! ", false);
    }

    public void clickOnCreateOrNextBtn() throws Exception {
        if (isElementLoaded(btnSave, 10)) {
            clickTheElement(btnSave);
            SimpleUtils.pass("Create or Next Button clicked Successfully on Customize new Shift page!");
        }else if (isElementLoaded(btnSaveOnNewCreateShiftPage, 5)) {
            try {
                if (isElementLoaded(shiftStartInputOnNewCreateShiftPage, 3) && isElementLoaded(shiftEndInputOnNewCreateShiftPage, 3)) {
                    if (!shiftEndInputOnNewCreateShiftPage.getAttribute("value").contains("AM") && !shiftEndInputOnNewCreateShiftPage.getAttribute("value").contains("PM")) {
                        moveSliderAtCertainPoint("9am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
                    }
                    if (!shiftStartInputOnNewCreateShiftPage.getAttribute("value").contains("AM") && !shiftStartInputOnNewCreateShiftPage.getAttribute("value").contains("PM")) {
                        moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
                    }
                }
            } catch (Exception e) {
                // Do Nothing
            }
            clickTheElement(btnSaveOnNewCreateShiftPage);
            clickOnOkButtonOnErrorDialog();
            SimpleUtils.pass("Create or Next Button clicked Successfully on Customize new Shift page!");
        }else {
            SimpleUtils.fail("Create or Next Button not clicked Successfully on Customize new Shift page!", false);
        }
    }

    @FindBy(css = ".tab-set .select .tab-label-text")
    private WebElement selectRecommendedOption;

    @FindBy(css = "[ng-if=\"hasBestWorkers()\"] div.tma-scroll-table tr")
    private List<WebElement> recommendedScrollTable;

    @FindBy(css = ".tma-search-field-input-text")
    private WebElement textSearch;

    @FindBy(css = "div.tab-label")
    private List<WebElement> btnSearchteamMember;

    @FindBy(css = ".sch-search")
    private WebElement searchIcon;

    @FindBy(xpath = "//div[@class='worker-edit-availability-status']")
    private List<WebElement> scheduleStatus;

    @FindBy(xpath="//span[contains(text(),'Best')]")
    private List<WebElement> scheduleBestMatchStatus;

    @FindBy(css = "td.table-field.action-field.tr>div")
    private List<WebElement> radionBtnSelectTeamMembers;

    @FindBy(css = "div.worker-edit-search-worker-name")
    private List<WebElement> searchWorkerName;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-availability-status']")
    private List<WebElement> scheduleSearchTeamMemberStatus;

    @FindBy(css="div.tma-empty-search-results")
    private WebElement scheduleNoAvailableMatchStatus;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@ng-click='selectAction($event, worker)']")
    private List<WebElement> radionBtnSearchTeamMembers;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-search-worker-name']/following-sibling::div")
    private List<WebElement> searchWorkerRole;

    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-search-worker-name']/following-sibling::div[2]")
    private List<WebElement> searchWorkerLocation;

    @FindBy(css = "button.lgn-action-button-success")
    private WebElement btnAssignAnyway;

    @FindBy(css = "div.lgn-alert-modal")
    private WebElement popUpScheduleOverlap;

    @FindBy(css = "div.lgn-alert-message")
    private WebElement alertMessage;

    @FindBy(css="button.tma-action.sch-save")
    private WebElement btnOffer;

    @FindBy(css = "button.lgn-dropdown-button:nth-child(1)")
    private WebElement btnWorkRole;

    @FindBy(css = " [ng-click=\"selectChoice($event, choice)\"]")
    private List<WebElement> listWorkRoles;

    @FindBy(css = "[label=\"Create New Shift\"]")
    private WebElement createNewShiftWeekView;

    @FindBy(className = "week-day-multi-picker-day")
    private List<WebElement> weekDays;

    @Override
    public void emptySearchBox() throws Exception {
        if (isElementLoaded(textSearch, 10)) {
            SimpleUtils.report("Search input box displays!");
            textSearch.clear();
        }else if(isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
            SimpleUtils.report("Search input box displays!");
            textSearchOnNewCreateShiftPage.clear();
        }else {
            SimpleUtils.fail("Search input box is not loaded!",false);
        }
    }

    @Override
    public void clickSearchIcon() throws Exception {
        if (isElementLoaded(searchIcon, 10)) {
            SimpleUtils.report("Search icon displays!");
            clickTheElement(searchIcon);
        }else{
            SimpleUtils.fail("Search icon is not loaded!",false);
        }
    }

    public void verifySelectTeamMembersOption() throws Exception {
        waitForSeconds(3);
        if (isElementLoaded(selectRecommendedOption, 20)) {
            clickTheElement(selectRecommendedOption);
            waitForSeconds(3);
            if (areListElementVisible(recommendedScrollTable, 5)) {
                String[] txtRecommendedOption = selectRecommendedOption.getText().replaceAll("\\p{P}", "").split(" ");
                if (Integer.parseInt(txtRecommendedOption[2]) == 0) {
                    if (getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
                        searchText(propertySearchTeamMember.get("AssignTeamMember"));
                    } else if (getDriver().getCurrentUrl().contains(parameterMap.get("Coffee_Enterprise"))) {
                        searchText(propertySearchTeamMember.get("TeamLCMember"));
                    }
                    SimpleUtils.pass(txtRecommendedOption[0] + " Option selected By default for Select Team member option");
                } else {
                    boolean  scheduleBestMatchStatus = getScheduleBestMatchStatus();
                    if(scheduleBestMatchStatus){
                        SimpleUtils.pass(txtRecommendedOption[0] + " Option selected By default for Select Team member option");
                    }else{
                        if(areListElementVisible(btnSearchteamMember,5)){
                            click(btnSearchteamMember.get(0));
                            if (getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
                                searchText(propertySearchTeamMember.get("AssignTeamMember"));
                            } else if (getDriver().getCurrentUrl().contains(parameterMap.get("Coffee_Enterprise"))) {
                                searchText(propertySearchTeamMember.get("TeamLCMember"));
                            }
                        }

                    }

                }
            } else if (areListElementVisible(btnSearchteamMember,5)) {
                click(btnSearchteamMember.get(0));
                if (getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
                    searchText(propertySearchTeamMember.get("AssignTeamMember"));
                } else if (getDriver().getCurrentUrl().contains(parameterMap.get("Coffee_Enterprise"))) {
                    searchText(propertySearchTeamMember.get("TeamLCMember"));
                }
            }
        } else if (areListElementVisible(searchAndRecommendedTMTabs, 20)) {
            clickTheElement(searchAndRecommendedTMTabs.get(1));
            waitForSeconds(3);
            if (areListElementVisible(searchResultsOnNewCreateShiftPage, 5)) {
                List<WebElement> assignAndOfferButtons = searchResultsOnNewCreateShiftPage.get(0).findElements(By.tagName("button"));
                    if (MyThreadLocal.getAssignTMStatus()) {
                        clickTheElement(assignAndOfferButtons.get(0));
                    } else
                        clickTheElement(assignAndOfferButtons.get(1));
                    if (isElementEnabled(btnAssignAnyway, 5)) {
                        click(btnAssignAnyway);
                    }
            } else if (areListElementVisible(searchAndRecommendedTMTabs,5)) {
                click(searchAndRecommendedTMTabs.get(0));
                if (getDriver().getCurrentUrl().contains(parameterMap.get("KendraScott2_Enterprise"))) {
                    searchText(propertySearchTeamMember.get("AssignTeamMember"));
                } else if (getDriver().getCurrentUrl().contains(parameterMap.get("Coffee_Enterprise"))) {
                    searchText(propertySearchTeamMember.get("TeamLCMember"));
                } else
                    searchText("a");
            }
        } else {
            SimpleUtils.fail("Select Team member option and Recommended options are not available on page", false);
        }
    }

    public void searchText(String searchInput) throws Exception {
        String[] searchAssignTeamMember = searchInput.split(",");
        if (isElementLoaded(textSearch, 10) && isElementLoaded(searchIcon, 10)) {
            for (int i = 0; i < searchAssignTeamMember.length; i++) {
                String[] searchTM = searchAssignTeamMember[i].split("\\.");
                textSearch.sendKeys(searchTM[0]);
                click(searchIcon);
                if (getScheduleStatus()) {
                    setTeamMemberName(searchAssignTeamMember[i]);
                    break;
                } else {
                    textSearch.clear();
                }
            }

        } else if (isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
            for (int i = 0; i < searchAssignTeamMember.length; i++) {
                String[] searchTM = searchAssignTeamMember[i].split("\\.");
                textSearchOnNewCreateShiftPage.sendKeys(searchTM[0]);
                waitForSeconds(3);
                if (getScheduleStatus()) {
                    setTeamMemberName(searchAssignTeamMember[i]);
                    break;
                } else {
                    textSearchOnNewCreateShiftPage.clear();
                }
            }
        }else {
            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
        }

    }

    public boolean getScheduleBestMatchStatus()throws Exception {
        boolean ScheduleBestMatchStatus = false;
        if(areListElementVisible(scheduleStatus,5) || scheduleBestMatchStatus.size()!=0 && radionBtnSelectTeamMembers.size() == scheduleStatus.size() && searchWorkerName.size()!=0){
            for(int i=0; i<scheduleStatus.size();i++){
                if(scheduleBestMatchStatus.get(i).getText().contains("Best")
                        || scheduleStatus.get(i).getText().contains("Unknown") || scheduleStatus.get(i).getText().contains("Available")){
                    //if(searchWorkerName.get(i).getText().contains("Gordon.M") || searchWorkerName.get(i).getText().contains("Jayne.H")){
                    click(radionBtnSelectTeamMembers.get(i));
                    setTeamMemberName(searchWorkerName.get(i).getText());
                    ScheduleBestMatchStatus = true;
                    break;
                    //}
                }
            }
        }else{
            SimpleUtils.fail("Not able to found Available status in SearchResult", false);
        }

        return ScheduleBestMatchStatus;
    }


    public boolean getScheduleStatus() throws Exception {
        boolean ScheduleStatus = false;
//		waitForSeconds(5);
        if(areListElementVisible(scheduleSearchTeamMemberStatus,10) || isElementLoaded(scheduleNoAvailableMatchStatus,10)){
            for(int i=0; i<scheduleSearchTeamMemberStatus.size();i++){
                if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Available")
                        || scheduleSearchTeamMemberStatus.get(i).getText().contains("Unknown")){
                    if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Minor")){
                        click(radionBtnSearchTeamMembers.get(i));
                        ScheduleStatus = true;
                        break;
                    } else if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Role Violation")){
                        click(radionBtnSearchTeamMembers.get(i));
                        displayAlertPopUpForRoleViolation();
                        setWorkerRole(searchWorkerRole.get(i).getText());
                        setWorkerLocation(searchWorkerLocation.get(i).getText());
//                        setWorkerShiftTime(searchWorkerSchShiftTime.getText());
//                        setWorkerShiftDuration(searchWorkerSchShiftDuration.getText());
                        ScheduleStatus = true;
                        break;
                    } else if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Will trigger")) {
                        clickTheElement(radionBtnSearchTeamMembers.get(i));
                        if (isElementLoaded(btnAssignAnyway, 10) && btnAssignAnyway.getText().toUpperCase().contains("ASSIGN ANYWAY")) {
                            clickTheElement(btnAssignAnyway);
                            waitUntilElementIsInVisible(btnAssignAnyway);
                        }
                        ScheduleStatus = true;
                        break;
                    } else {
                        click(radionBtnSearchTeamMembers.get(i));
                        setWorkerRole(searchWorkerRole.get(i).getText());
                        setWorkerLocation(searchWorkerLocation.get(i).getText());
//					setWorkerShiftTime(searchWorkerSchShiftTime.getText());
//					setWorkerShiftDuration(searchWorkerSchShiftDuration.getText());
                        ScheduleStatus = true;
                        break;
                    }
                }
            }
        } else if(areListElementVisible(searchResultsOnNewCreateShiftPage,10)){
            for(int i=0; i<searchResultsOnNewCreateShiftPage.size();i++){
                List<WebElement> allStatus= getTMScheduledStatusElementsOnNewCreateShiftPage();;
                List<WebElement> tmInfo = searchResultsOnNewCreateShiftPage.get(i).findElements(By.cssSelector("p.MuiTypography-body1"));
                String tmAllStatus = "";
                for (WebElement status: allStatus) {
                    tmAllStatus = tmAllStatus + " "+status.getText();
                }
                MyThreadLocal.setMessageOfTMScheduledStatus(tmAllStatus);
                if(tmAllStatus.contains("Available")
                        || tmAllStatus.contains("Unknown")){
                    List<WebElement> assignAndOfferButtons = searchResultsOnNewCreateShiftPage.get(i).findElements(By.tagName("button"));
//                    if (MyThreadLocal.getAssignTMStatus()) {
//                        clickTheElement(assignAndOfferButtons.get(0));
//                    } else
//                        clickTheElement(assignAndOfferButtons.get(1));
//                    if (isElementEnabled(btnAssignAnyway, 5)) {
//                        click(btnAssignAnyway);
//                    }
                    if(tmAllStatus.contains("Minor")){
                        if (MyThreadLocal.getAssignTMStatus()) {
                            clickTheElement(assignAndOfferButtons.get(0));
                        } else
                            clickTheElement(assignAndOfferButtons.get(1));
                        ScheduleStatus = true;
                        break;
                    } else if(tmAllStatus.contains("Role Violation")){
                        if (MyThreadLocal.getAssignTMStatus()) {
                            clickTheElement(assignAndOfferButtons.get(0));
                        } else
                            clickTheElement(assignAndOfferButtons.get(1));
                        displayAlertPopUpForRoleViolation();
                        setWorkerRole(tmInfo.get(1).getText());
                        setWorkerLocation(tmInfo.get(2).getText());
//                        setWorkerShiftTime(searchWorkerSchShiftTime.getText());
//                        setWorkerShiftDuration(searchWorkerSchShiftDuration.getText());
                        ScheduleStatus = true;
                        break;
                    } else if(tmAllStatus.contains("Will trigger")) {
                        if (MyThreadLocal.getAssignTMStatus()) {
                            clickTheElement(assignAndOfferButtons.get(0));
                        } else
                            clickTheElement(assignAndOfferButtons.get(1));
                        if (areListElementVisible(buttonsOnWarningMode, 5)) {
                            click(buttonsOnWarningMode.get(1));
                        }
                        ScheduleStatus = true;
                        break;
                    } else {
                        if (MyThreadLocal.getAssignTMStatus()) {
                            clickTheElement(assignAndOfferButtons.get(0));
                        } else
                            clickTheElement(assignAndOfferButtons.get(1));
                        ScheduleStatus = true;
                        break;
                    }
                }
            }
        }else{
            SimpleUtils.fail("Not able to found Available status in SearchResult", false);
        }
        return ScheduleStatus;
    }

    @Override
    public void displayAlertPopUpForRoleViolation() throws Exception{
        String msgAlert = null;
        if(isElementLoaded(popUpScheduleOverlap,5)){
            if(isElementLoaded(alertMessage,5)){
                msgAlert = alertMessage.getText();
                if(isElementLoaded(btnAssignAnyway,5)){
                    SimpleUtils.pass("Role violation messsage as such as " + msgAlert);
                    click(btnAssignAnyway);
                }else{
                    SimpleUtils.fail("Assign Anyway button not displayed on the page",false);
                }
            }else{
                SimpleUtils.fail("Alert message for not displayed",false);
            }
        }else{
            SimpleUtils.fail("Role Violation pop up not displayed",false);
        }
    }

    public void clickOnOfferOrAssignBtn() throws Exception{
        if(isElementLoaded(btnOffer,5)){
            scrollToElement(btnOffer);
            waitForSeconds(3);
            clickTheElement(btnOffer);
            if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().toUpperCase().equals("ASSIGN ANYWAY")) {
                clickTheElement(btnAssignAnyway);
            }
        }else if (isElementLoaded(btnSaveOnNewCreateShiftPage, 5)) {
            scrollToElement(btnSaveOnNewCreateShiftPage);
            waitForSeconds(3);
            clickTheElement(btnSaveOnNewCreateShiftPage);
            SimpleUtils.pass("Create or Next Button clicked Successfully on Customize new Shift page!");
            if (areListElementVisible(buttonsOnWarningMode, 10)) {
                click(buttonsOnWarningMode.get(1));
            }
            clickOnOkButtonOnErrorDialog();
        }else{
            SimpleUtils.fail("Offer Or Assign Button is not clickable", false);
        }
    }



    @FindBy(xpath = "//*[contains(@id,\"WorkRole_menu\")]/div/div[2]/div")
    private WebElement workRoleOnNewShiftPage;

    @FindBy(className = "react-select__option")
    private List<WebElement> dropDownListOnNewCreateShiftPage;
    public void selectWorkRole(String workRoles) throws Exception {
        waitForSeconds(10);
        if (isElementLoaded(btnWorkRole, 15)) {
            clickTheElement(btnWorkRole);
            SimpleUtils.pass("Work Role button clicked Successfully");
            if (listWorkRoles.size() > 0) {
                for (WebElement listWorkRole : listWorkRoles) {
                    if (listWorkRole.getText().toLowerCase().contains(workRoles.toLowerCase())) {
                        click(listWorkRole);
                        SimpleUtils.pass("Work Role " + workRoles + "selected Successfully");
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Work Roles size are empty", false);
            }
        } else if (isElementLoaded(workRoleOnNewShiftPage, 25)) {
            waitForSeconds(2);
            if (!areListElementVisible(dropDownListOnNewCreateShiftPage, 5) && dropDownListOnNewCreateShiftPage.size() == 0) {
                click(workRoleOnNewShiftPage);
            }
            SimpleUtils.pass("Work Role button clicked Successfully");
            waitForSeconds(1);
            if (dropDownListOnNewCreateShiftPage.size() > 0) {
                for (WebElement listWorkRole : dropDownListOnNewCreateShiftPage) {
                    if (listWorkRole.getText().toLowerCase().trim().contains(workRoles.toLowerCase().trim())) {
                        click(listWorkRole);
                        SimpleUtils.pass("Work Role " + workRoles + "selected Successfully");
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Work Roles size are empty", false);
            }
        } else {
            SimpleUtils.fail("Work Role button is not clickable", false);
        }

    }

    @Override
    public List<String> getWorkRoleList() throws Exception {
        List<String> workRoles = new ArrayList<>();
        waitForSeconds(3);
        if (isElementLoaded(btnWorkRole, 5)) {
            clickTheElement(btnWorkRole);
            SimpleUtils.pass("Work Role button clicked Successfully");
            if (listWorkRoles.size() > 0) {
                for (WebElement listWorkRole : listWorkRoles) {
                    workRoles.add(listWorkRole.getText().toLowerCase());
                }
            } else {
                SimpleUtils.fail("Work Roles size are empty", false);
            }
        } else if (isElementLoaded(workRoleOnNewShiftPage, 25)) {
            click(workRoleOnNewShiftPage);
            SimpleUtils.pass("Work Role button clicked Successfully");
            if (dropDownListOnNewCreateShiftPage.size() > 0) {
                for (WebElement listWorkRole : dropDownListOnNewCreateShiftPage) {
                    workRoles.add(listWorkRole.getText().toLowerCase());
                }
            } else {
                SimpleUtils.fail("Work Roles size are empty", false);
            }
        } else {
            SimpleUtils.fail("Work Role button is not clickable", false);
        }
        return workRoles;
    }

    @Override
    public void selectWorkRoleCaseSensitive(String workRole) throws Exception {
        if (isElementLoaded(btnWorkRole, 5)) {
            clickTheElement(btnWorkRole);
            SimpleUtils.pass("Work Role button clicked Successfully");
            if (listWorkRoles.size() > 0) {
                for (WebElement listWorkRole : listWorkRoles) {
                    if (listWorkRole.getText().trim().contains(workRole)) {
                        click(listWorkRole);
                        SimpleUtils.pass("Work Role " + workRole + "selected Successfully");
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Work Roles size are empty", false);
            }
        } else if (isElementLoaded(workRoleOnNewShiftPage, 15)) {
            click(workRoleOnNewShiftPage);
            SimpleUtils.pass("Work Role button clicked Successfully");
            if (dropDownListOnNewCreateShiftPage.size() > 0) {
                for (WebElement listWorkRole : dropDownListOnNewCreateShiftPage) {
                    if (listWorkRole.getText().trim().contains(workRole)) {
                        click(listWorkRole);
                        SimpleUtils.pass("Work Role " + workRole + "selected Successfully");
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Work Roles size are empty", false);
            }
        } else {
            SimpleUtils.fail("Work Role button is not clickable", false);
        }

    }

    @FindBy(id = "shiftsPerDay")
    private WebElement shiftsPerDay;

    @Override
    public void setShiftsPerDay(int numberOfShiftPerDay) throws Exception {
        if (isElementLoaded(shiftsPerDay, 5)) {
            doubleClick(shiftsPerDay);
            waitForSeconds(2);
            shiftsPerDay.sendKeys(Integer.toString(numberOfShiftPerDay));
            SimpleUtils.pass("Number of shifts per day have been set!");
        } else {
            SimpleUtils.fail("Shifts Per Day is not loaded!", false);
        }
    }

    @Override
    public void addOpenShiftWithDefaultTime(String workRole) throws Exception {
        if (isElementLoaded(createNewShiftWeekView)) {
            click(createNewShiftWeekView);
            selectWorkRole(workRole);
            clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            clickOnCreateOrNextBtn();
            Thread.sleep(2000);
        } else
            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
                    getActiveWeekText() + "'", false);
    }

    @Override
    public void addOpenShiftWithDefaultTime(String workRole, String location) throws Exception {
        if (isElementLoaded(createNewShiftWeekView)) {
            click(createNewShiftWeekView);
            selectWorkRole(workRole);
            clickRadioBtnStaffingOption(staffingOption.OpenShift.getValue());
            if (isLocationLoaded())
                selectLocation(location);
            moveSliderAtCertainPoint("11am", ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
            moveSliderAtCertainPoint("8am", ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
            clickOnCreateOrNextBtn();
            if (ifWarningModeDisplay() && isElementLoaded(okBtnInWarningMode,5))
                click(okBtnInWarningMode);
            if (areListElementVisible(buttonsOnWarningMode, 5)) {
                clickTheElement(buttonsOnWarningMode.get(0));
            }
            Thread.sleep(2000);
        } else
            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
                    getActiveWeekText() + "'", false);
    }


    @FindBy(css = "[attr-id=\"location\"] button.lgn-dropdown-button")
    private WebElement btnLocation;

    @FindBy(css = "[attr-id=\"location\"] [ng-click=\"selectChoice($event, choice)\"]")
    private List<WebElement> listLocations;


    public boolean isLocationLoaded() throws Exception {
        if (isElementLoaded(btnLocation, 20))
            return true;
        else if (isElementLoaded(btnChildLocationOnNewCreateShiftPage))
            return true;
        else
            return false;
    }

    public void selectLocation(String location) throws Exception {
        if (isElementLoaded(btnLocation, 10)) {
            click(btnLocation);
            SimpleUtils.pass("Location button clicked Successfully");
            if (listLocations.size() > 1) {
                for (WebElement listWorkRole : listLocations) {
                    if (listWorkRole.getText().toLowerCase().contains(location.toLowerCase())) {
                        click(listWorkRole);
                        SimpleUtils.pass("Location " + location + "selected Successfully");
                        break;
                    } else {
                        SimpleUtils.report("Location " + location + " not selected");
                    }
                }
            } else {
                SimpleUtils.fail("Location size are empty", false);
            }
        } else if (isElementLoaded(btnChildLocationOnNewCreateShiftPage, 5)) {
            click(btnChildLocationOnNewCreateShiftPage);
            SimpleUtils.pass("Location button clicked Successfully");
            if (dropDownListOnNewCreateShiftPage.size() > 1) {
                for (WebElement childLocation : dropDownListOnNewCreateShiftPage) {
                    if (childLocation.getText().toLowerCase().contains(location.toLowerCase())) {
                        click(childLocation);
                        SimpleUtils.pass("Location " + location + "selected Successfully");
                        break;
                    } else {
                        SimpleUtils.report("Location " + location + " not selected");
                    }
                }
            } else {
                SimpleUtils.fail("Location size are empty", false);
            }
        }else {
            SimpleUtils.fail("Work Role button is not clickable", false);
        }
    }

    @FindBy(css = "div.lgn-alert-modal")
    private WebElement warningMode;

    @FindBy(css = "span.lgn-alert-message")
    private WebElement warningMessagesInWarningMode;

    @FindBy(className = "lgn-action-button-success")
    private WebElement okBtnInWarningMode;

    @FindBy(css = ".MuiDialogContent-root")
    private WebElement warningModeOnNewCreateShiftModal;

    @Override
    public boolean ifWarningModeDisplay() throws Exception {
        if(isElementLoaded(warningMode, 25)) {
            SimpleUtils.pass("Warning mode is loaded successfully");
            return true;
        } else if (isElementLoaded(warningModeOnNewCreateShiftModal, 5)) {
            SimpleUtils.pass("Warning mode is loaded successfully");
            return true;
        }else {
            SimpleUtils.report("Warning mode fail to load");
            return false;
        }
    }

    @Override
    public void addOpenShiftWithFirstDay(String workRole) throws Exception {
        if (isElementLoaded(createNewShiftWeekView, 10)) {
            click(createNewShiftWeekView);
            selectWorkRole(workRole);
            clearAllSelectedDays();
            if (areListElementVisible(weekDays, 5) && weekDays.size() > 0) {
                clickTheElement(weekDays.get(0));
            } else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5)
                    && weekDaysInNewCreateShiftPage.size() ==7) {
                clickTheElement(weekDaysInNewCreateShiftPage.get(0)
                        .findElement(By.cssSelector(".MuiButtonBase-root")).findElement(By.tagName("input")));
            }
            clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            clickOnCreateOrNextBtn();
        } else
            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
                    getActiveWeekText() + "'", false);
    }

    @FindBy(css = "[id*=\"legion_cons_Schedule_Schedule_CreateShift\"] label")
    private List<WebElement> weekDaysInNewCreateShiftPage;
    public void clearAllSelectedDays() throws Exception {
        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
            for (WebElement weekDay : weekDays) {
                if (weekDay.getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                    click(weekDay);
                }
            }
        }else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5)
                && weekDaysInNewCreateShiftPage.size() == 7) {
            for (WebElement weekDay : weekDaysInNewCreateShiftPage) {
                if (weekDay.findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("Mui-checked")) {
                    clickTheElement(weekDay.findElement(By.cssSelector(".MuiButtonBase-root")).findElement(By.tagName("input")));
                }
            }
            if (isElementLoaded(selectDaysWarningMessageOnNewShiftPage, 3)) {
                String expectedMessage = "At least one day should be selected";
                if (checkIfSelectDaysWarningMessageIsLoaded()) {
                    SimpleUtils.pass("The 'at least one day selected' warning message display correctly!");
                } else
                    SimpleUtils.fail("The warning message display incorrectly, the expected is: "+ expectedMessage
                            + " the actual is: "+ selectDaysWarningMessageOnNewShiftPage.getText(), false);
            } else
                SimpleUtils.report("The 'at least one day selected' warning message fail to load! ");
        }else{
            SimpleUtils.fail("Weeks Days failed to load!", false);
        }
    }

    @FindBy(css = "div.lgn-time-slider-notch-label")
    private List<WebElement> scheduleOperatingHrsOnEditPage;
    @FindBy(className = "tma-time-slider")
    private WebElement scheduleOperatingHrsSlider;
    @FindBy(css = "[id*=\"ShiftStart_field\"]")
    private WebElement shiftStartInputOnNewCreateShiftPage;
    @FindBy(css = "[id*=\"ShiftEnd_field\"]")
    private WebElement shiftEndInputOnNewCreateShiftPage;

    public void moveSliderAtCertainPoint(String shiftTime, String startingPoint) throws Exception {
        waitForSeconds(3);
        if (isElementLoaded(scheduleOperatingHrsSlider, 5)) {
            WebElement element = null;
            String am = "am";
            String pm = "pm";
            if (shiftTime.length() > 2 && (shiftTime.toLowerCase().contains(am) || shiftTime.toLowerCase().contains(pm))) {
                if(areListElementVisible(scheduleOperatingHrsOnEditPage, 15)
                        && scheduleOperatingHrsOnEditPage.size() >0){
                    for (WebElement scheduleOperatingHour: scheduleOperatingHrsOnEditPage){
                        if (scheduleOperatingHour.getAttribute("class").toLowerCase().contains(shiftTime.substring(shiftTime.length() - 2).toLowerCase())) {
                            if(scheduleOperatingHour.getText().equals(
                                    shiftTime.split(":")[0]
                                            .replace("am", "").replace("pm", "")
                                            .replace("AM", "").replace("PM", "").replaceFirst("^0*", ""))){
                                element = scheduleOperatingHour;
                                break;
                            }
                        }
                    }
                }
            } else {
                if(areListElementVisible(scheduleOperatingHrsOnEditPage, 15)
                        && scheduleOperatingHrsOnEditPage.size() >0){
                    for (WebElement scheduleOperatingHour: scheduleOperatingHrsOnEditPage){
                        if(scheduleOperatingHour.getText().equals(shiftTime)){
                            element = scheduleOperatingHour;
                            break;
                        }
                    }
                }
            }
            if (element == null){
                SimpleUtils.fail("Cannot found the operating hour on edit operating hour page! ", false);
            }
            if(startingPoint.equalsIgnoreCase("End")){
                if(isElementLoaded(sliderNotchEnd,10) && sliderDroppableCount.size()>0){
                    SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
                    mouseHoverDragandDrop(sliderNotchEnd,element);
                } else{
                    SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
                }
            }else if(startingPoint.equalsIgnoreCase("Start")){
                if(isElementLoaded(sliderNotchStart,10) && sliderDroppableCount.size()>0){
                    SimpleUtils.pass("Shift timings with Sliders loaded on page Successfully for End Point");
                    mouseHoverDragandDrop(sliderNotchStart,element);
                } else{
                    SimpleUtils.fail("Shift timings with Sliders not loaded on page Successfully", false);
                }
            }
        } else if (isElementLoaded(shiftStartInputOnNewCreateShiftPage, 25)
                && isElementLoaded(shiftEndInputOnNewCreateShiftPage, 25)) {
            if (shiftTime.contains("am")) {
                shiftTime = shiftTime.replace("am","")+ ":00"+"am";
            } else if (shiftTime.contains("pm")) {
                shiftTime = shiftTime.replace("pm","")+ ":00"+"pm";
            } else
                shiftTime = shiftTime+":00";
            if(startingPoint.equalsIgnoreCase("Start")){
                click(shiftEndInputOnNewCreateShiftPage);
                click(shiftStartInputOnNewCreateShiftPage);
                shiftStartInputOnNewCreateShiftPage.sendKeys(shiftTime);
                SimpleUtils.pass("Set shift start time successfully! ");
            } else {
                click(shiftStartInputOnNewCreateShiftPage);
                click(shiftEndInputOnNewCreateShiftPage);
                shiftEndInputOnNewCreateShiftPage.sendKeys(shiftTime);
                SimpleUtils.pass("Set shift end time successfully! ");
            }
        } else
            SimpleUtils.fail("Shift time slider or inputs fail to load on create shift page! ", false);
    }


    @FindBy(css = " [ng-if=\"isLocationGroup()\"] [ng-click=\"selectChoice($event, choice)\"]")
    private List<WebElement> listLocationGroup;
    @Override
    public void addNewShiftsByNames(List<String> names, String workRole) throws Exception {
        scrollToTop();
        waitForSeconds(2);
        for(int i = 0; i < 2; i++) {
            clickOnDayViewAddNewShiftButton();
            customizeNewShiftPage();
            if(areListElementVisible(listLocationGroup, 5)
                    || isElementLoaded(btnChildLocationOnNewCreateShiftPage, 5)){
                List<String> locations = getAllLocationGroupLocationsFromCreateShiftWindow();
                selectChildLocInCreateShiftWindow(locations.get((new Random()).nextInt(locations.size()-1)+1));
                moveSliderAtSomePoint("40", 0, ScheduleTestKendraScott2.shiftSliderDroppable.EndPoint.getValue());
                moveSliderAtSomePoint("20", 0, ScheduleTestKendraScott2.shiftSliderDroppable.StartPoint.getValue());
                selectWorkRole(workRole);
            } else
                selectWorkRole(workRole);
            clearAllSelectedDays();
            if (i == 0) {
                if (getDriver().getCurrentUrl().contains(propertyMap.get(Constants.OpEnterprise1)) || getDriver().getCurrentUrl().contains(propertyMap.get(Constants.OpEnterprise2))) {
                    selectDaysByIndex(2, 2, 2);
                } else {
                    selectDaysByIndex(2, 4, 6);
                }
            }else {
                if (getDriver().getCurrentUrl().contains(propertyMap.get(Constants.OpEnterprise1)) || getDriver().getCurrentUrl().contains(propertyMap.get(Constants.OpEnterprise2))) {
                    selectDaysByIndex(4, 4, 4);
                } else {
                    selectDaysByIndex(1, 3, 5);
                }
            }
            clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.AssignTeamMemberShift.getValue());
            clickOnCreateOrNextBtn();
            if(isElementLoaded(btnAssignAnyway,5))
                click(btnAssignAnyway);
            if (areListElementVisible(buttonsOnWarningMode, 5)) {
                click(buttonsOnWarningMode.get(1));
            }
            searchTeamMemberByName(names.get(i));
            if(isElementLoaded(btnAssignAnyway,5))
                click(btnAssignAnyway);
            clickOnOfferOrAssignBtn();
        }
    }


    @FindBy(css = "[ng-show=\"hasSearchResults()\"] [ng-repeat=\"worker in searchResults\"]")
    private List<WebElement> searchResults;

    @FindBy(css = ".swap-modal>div:nth-child(2) button.MuiButtonBase-root")
    private List<WebElement> searchAndRecommendedTMTabs;

    @FindBy(css = "[placeholder=\"Search by Team Member, Role, Location or any combination.\"]")
    private WebElement textSearchOnNewCreateShiftPage;

    @FindBy(xpath = "//div[contains(@class,'MuiGrid-container')]/div/div/div/button/parent::*/parent::*/parent::*/parent::*/parent::*/div[contains(@class,'MuiGrid-container')]")
    private List<WebElement> searchResultsOnNewCreateShiftPage;

    @FindBy(css = ".MuiDialogContent-root button")
    private List<WebElement> buttonsOnWarningMode;

    @FindBy(xpath = "//div[contains(@class,'MuiGrid-grid-xs-4')]/div[1]/p")
    private List<WebElement> tmScheduledStatusOnNewCreateShiftPage;

    private List<WebElement> getTMScheduledStatusElementsOnNewCreateShiftPage() {
        int index = 0;
        if (areListElementVisible(searchTableColumns, 5)) {
            for (int i = 0; i < searchTableColumns.size(); i++) {
                if (searchTableColumns.get(i).getText().trim().toLowerCase().equalsIgnoreCase("status")) {
                    index = i;
                    break;
                }
            }
        }
        return getDriver().findElements(By.cssSelector(".MuiTabs-root+div>div>div:nth-child(2)>div>div:nth-child(2) .MuiGrid-item:nth-child("
                + (index + 1) + ")"));
    }

    public void searchTeamMemberByName(String name) throws Exception {
        if(areListElementVisible(btnSearchteamMember,5)) {
            if (btnSearchteamMember.size() == 2) {
                //click(btnSearchteamMember.get(1));
                if (isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 5)) {
                    textSearch.clear();
                    textSearch.sendKeys(name);
                    click(searchIcon);
                    if (areListElementVisible(searchResults, 30)) {
                        for (WebElement searchResult : searchResults) {
                            WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-name"));
                            WebElement optionCircle = searchResult.findElement(By.className("tma-staffing-option-outer-circle"));
                            WebElement locationInfo = searchResult.findElement(By.className("tma-description-fields"));
                            if (workerName != null && optionCircle != null) {
                                if (workerName.getText().toLowerCase().trim().replaceAll("\n"," ").contains(name.split(" ")[0].trim().toLowerCase())
                                        ||locationInfo.getText().contains(name)) {
                                    WebElement status = searchResult.findElement(By.cssSelector(".worker-edit-availability-status"));
                                    MyThreadLocal.setMessageOfTMScheduledStatus(status.getText());
                                    clickTheElement(optionCircle);
                                    SimpleUtils.report("Select Team Member: " + name + " Successfully!");
                                    waitForSeconds(2);
                                    if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().toLowerCase().equalsIgnoreCase("assign anyway")) {
                                        clickTheElement(btnAssignAnyway);
                                        SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
                                    }
                                    break;
                                }
                            }else {
                                SimpleUtils.fail("Worker name or option circle not loaded Successfully!", false);
                            }
                        }
                    }else {
                        SimpleUtils.fail("Failed to find the team member!", false);
                    }
                }else {
                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
                }
            }else {
                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
            }
        } else if (areListElementVisible(searchAndRecommendedTMTabs, 10)) {
            if (searchAndRecommendedTMTabs.size() == 2) {
                //click(btnSearchteamMember.get(1));
                if (isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
                    textSearchOnNewCreateShiftPage.sendKeys(Keys.CONTROL, "a");
                    textSearchOnNewCreateShiftPage.sendKeys(Keys.DELETE);
                    textSearchOnNewCreateShiftPage.sendKeys(name);
                    waitForSeconds(3);
                    if (areListElementVisible(searchResultsOnNewCreateShiftPage, 30)) {
                        for (WebElement searchResult : searchResultsOnNewCreateShiftPage) {
                            if (areListElementVisible(getTMScheduledStatusElementsOnNewCreateShiftPage(), 5)) {
                                String statusMessage = "";
                                for (WebElement status: getTMScheduledStatusElementsOnNewCreateShiftPage()) {
                                    statusMessage = statusMessage + status.getText() + "\n";
                                }
                                MyThreadLocal.setMessageOfTMScheduledStatus(statusMessage);
                            }
                            List<WebElement> tmInfo = searchResult.findElements(By.cssSelector("p.MuiTypography-body1"));
                            String allTMInfo = "";
                            for (WebElement info : tmInfo) {
                                allTMInfo = allTMInfo+ info.getText();
                            }
                            String tmName = tmInfo.get(0).getText();
                            List<WebElement> assignAndOfferButtons = searchResult.findElements(By.tagName("button"));
                            WebElement assignButton = assignAndOfferButtons.get(0);
                            WebElement offerButton = assignAndOfferButtons.get(1);
                            if (tmName != null && assignButton != null && offerButton != null) {
                                if (tmName.toLowerCase().trim().replaceAll("\n"," ").contains(name.split(" ")[0].trim().toLowerCase())
                                        || allTMInfo.contains(name)) {
                                    if (MyThreadLocal.getAssignTMStatus()) {
                                        clickTheElement(assignButton);
                                        SimpleUtils.report("Assign Team Member: " + name + " Successfully!");
                                    } else {
                                        clickTheElement(offerButton);
                                        SimpleUtils.report("Offer Team Member: " + name + " Successfully!");
                                    }
                                    waitForSeconds(7);
                                    if (areListElementVisible(buttonsOnWarningMode, 10)) {
                                        if (buttonsOnWarningMode.size()==2) {
                                            if (buttonsOnWarningMode.get(1).getText().toLowerCase().trim().equalsIgnoreCase("assign anyway")){
                                                clickTheElement(buttonsOnWarningMode.get(1));
                                                SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
                                            } else if(buttonsOnWarningMode.get(1).getText().toLowerCase().trim().equalsIgnoreCase("offer anyway")){
                                                clickTheElement(buttonsOnWarningMode.get(1));
                                                SimpleUtils.report("Assign Team Member: Click on 'OFFER ANYWAY' button Successfully!");
                                            }else
                                                SimpleUtils.report("Assign Team Member: ASSIGN ANYWAY and OFFER ANYWAY button fail to load! The button is:"
                                                        +buttonsOnWarningMode.get(1).getText().toLowerCase().trim());
                                        }
                                    }else
                                        SimpleUtils.report("Buttons on warning mode are not loaded! ");
                                    if(!areListElementVisible(assignedShiftsOnShiftAssignedSections, 5)
                                            && !areListElementVisible(shiftOffersOnShiftAssignedSections, 5)){
                                        SimpleUtils.fail("Fail to assign or offer TM on search or recommended TM page! ", false);
                                    }
                                    break;
                                }
                            }else {
                                SimpleUtils.fail("Worker name or buttons not loaded Successfully!", false);
                            }
                        }
                    }else {
                        SimpleUtils.fail("Failed to find the team member!", false);
                    }
                }else {
                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
                }
            }else {
                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
            }
        } else
            SimpleUtils.fail("Search team member tab fail to load! ", false);
    }

    @FindBy(css = ".MuiDialogContent-root")
    private WebElement overtimeWarningPopup;

//    @FindBy(linkText = "Offer anyway")
//    private WebElement offerAnywayBtn;

    @FindBy(css = ".MuiDialogContent-root>div>div>button:nth-child(2)")
    private WebElement offerAnywayBtn;
    @Override
    public void searchTeamMemberByNameAndAssignOrOfferShift(String name, Boolean isOffering) throws Exception {
        if(areListElementVisible(btnSearchteamMember,10)) {
            if (btnSearchteamMember.size() == 2) {
                //click(btnSearchteamMember.get(1));
                if (isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 5)) {
                    textSearch.clear();
                    textSearch.sendKeys(name);
                    click(searchIcon);
                    if (areListElementVisible(searchResults, 30)) {
                        for (WebElement searchResult : searchResults) {
                            WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-name"));
                            WebElement optionCircle = searchResult.findElement(By.className("tma-staffing-option-outer-circle"));
                            if (workerName != null && optionCircle != null) {
                                if (workerName.getText().toLowerCase().trim().replaceAll("\n"," ").contains(name.split(" ")[0].trim().toLowerCase())) {
                                    clickTheElement(optionCircle);
                                    SimpleUtils.report("Select Team Member: " + name + " Successfully!");
                                    waitForSeconds(2);
                                    if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().toLowerCase().equalsIgnoreCase("assign anyway")) {
                                        clickTheElement(btnAssignAnyway);
                                        SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
                                    }
                                    break;
                                }
                            }else {
                                SimpleUtils.fail("Worker name or option circle not loaded Successfully!", false);
                            }
                        }
                    }else {
                        SimpleUtils.fail("Failed to find the team member!", false);
                    }
                }else {
                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
                }
            }else {
                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
            }
        } else if (areListElementVisible(searchAndRecommendedTMTabs, 10)) {
            if (searchAndRecommendedTMTabs.size() == 2) {
                //click(btnSearchteamMember.get(1));
                if (isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
                    textSearchOnNewCreateShiftPage.click();
                    clearTheText(textSearchOnNewCreateShiftPage);
                    textSearchOnNewCreateShiftPage.sendKeys(name);
                    waitForSeconds(3);
                    if (areListElementVisible(searchResultsOnNewCreateShiftPage, 30)) {
                        for (WebElement searchResult : searchResultsOnNewCreateShiftPage) {
                            if (areListElementVisible(tmScheduledStatusOnNewCreateShiftPage, 5)) {
                                String statusMessage = "";
                                for (WebElement status: tmScheduledStatusOnNewCreateShiftPage) {
                                    statusMessage = statusMessage + status.getText() + "\n";
                                }
                                MyThreadLocal.setMessageOfTMScheduledStatus(statusMessage);
                            }
                            List<WebElement> tmInfo = searchResult.findElements(By.cssSelector("p.MuiTypography-body1"));
                            String tmName = tmInfo.get(0).getText();
                            List<WebElement> assignAndOfferButtons = searchResult.findElements(By.tagName("button"));
                            WebElement assignButton = assignAndOfferButtons.get(0);
                            WebElement offerButton = assignAndOfferButtons.get(1);
                            if (tmName != null && assignButton != null && offerButton != null) {
                                if (tmName.toLowerCase().trim().replaceAll("\n"," ").contains(name.split(" ")[0].trim().toLowerCase())) {
                                    if (isOffering) {
                                        clickTheElement(offerButton);
                                        if (isElementLoaded(overtimeWarningPopup, 10)) {
                                            if (isElementLoaded(offerAnywayBtn, 5)) {
                                                click(offerAnywayBtn);
                                            }
                                        }
                                    } else {
                                        clickTheElement(assignButton);
                                    }
                                    SimpleUtils.report("Select Team Member: " + name + " Successfully!");
                                    waitForSeconds(2);
                                    if (areListElementVisible(buttonsOnWarningMode, 5)) {
                                        if (buttonsOnWarningMode.size()==2) {
                                            if (buttonsOnWarningMode.get(1).getText().toLowerCase().equalsIgnoreCase("assign anyway")){
                                                clickTheElement(buttonsOnWarningMode.get(1));
                                                SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
                                            }
                                        }
                                    }
                                    if (!areListElementVisible(shiftOffersOnShiftAssignedSections, 3)
                                            && !areListElementVisible(assignedShiftsOnShiftAssignedSections, 3)){
                                        SimpleUtils.fail("Fail to select employees to assign or offer! ", false);
                                    }else
                                        SimpleUtils.pass("TM been added to assign or offer section successfully! ");
                                    break;
                                }
                            }else {
                                SimpleUtils.fail("Worker name or buttons not loaded Successfully!", false);
                            }
                        }
                    }else {
                        SimpleUtils.fail("Failed to find the team member!", false);
                    }
                }else {
                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
                }
            }else {
                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
            }
        } else
            SimpleUtils.fail("Search team member tab fail to load! ", false);
    }


    @FindBy(css = "[ng-if=\"canShowNewShiftButton()\"]")
    private WebElement addNewShiftOnDayViewButton;
    @Override
    public void clickOnDayViewAddNewShiftButton() throws Exception {
        if (isElementLoaded(createNewShiftWeekView, 10)) {
            clickTheElement(createNewShiftWeekView);
            SimpleUtils.pass("Click on Create New Shift button successfully!");
        }else if (isElementLoaded(addNewShiftOnDayViewButton, 10)) {
            click(addNewShiftOnDayViewButton);
            SimpleUtils.pass("Click on Add New Shift '+' button successfully!");
        }else {
            SimpleUtils.report("Add New Shift '+' button and Create New Shift button not loaded!");
        }
        waitForSeconds(3);
        if (checkIfNewCreateShiftPageDisplay()) {
            MyThreadLocal.setNewCreateShiftUIStatus(true);
        } else
            MyThreadLocal.setNewCreateShiftUIStatus(false);
    }

    public List<String> getAllLocationGroupLocationsFromCreateShiftWindow() throws Exception{
        List<String> locationGroupLocations = new ArrayList<>();
        if (isElementLoaded(btnChildLocation, 10)) {
            click(btnChildLocation);
            SimpleUtils.pass("Child location button clicked Successfully");
            if(areListElementVisible(listLocationGroup, 10) && listLocationGroup.size()>0){
                for (WebElement location: listLocationGroup){
                    locationGroupLocations.add(location.getText());
                }
                SimpleUtils.pass("Get location group locations from create shift window successfully! ");
            }else
                SimpleUtils.fail("Location group dropdown loaded fail! ", false);
            //close the dropdown list
            click(btnChildLocation);
        } else if (isElementLoaded(btnSaveOnNewCreateShiftPage, 5)) {
            click(btnSaveOnNewCreateShiftPage);
            SimpleUtils.pass("Child location button clicked Successfully");

            if(areListElementVisible(dropDownListOnNewCreateShiftPage, 10) && dropDownListOnNewCreateShiftPage.size()>0){
                for (WebElement location: dropDownListOnNewCreateShiftPage){
                    locationGroupLocations.add(location.getText());
                }
                SimpleUtils.pass("Get location group locations from create shift window successfully! ");
            }else
                SimpleUtils.fail("Location group dropdown loaded fail! ", false);
            //close the dropdown list
            click(btnSaveOnNewCreateShiftPage);
            clickOnOkButtonOnErrorDialog();
        }else {
            SimpleUtils.fail("Child location button is not clickable", false);
        }
        return locationGroupLocations;
    }

    @FindBy(css = "lgn-drop-down.tma-locations-dropdown button.lgn-dropdown-button")
    private WebElement btnChildLocation;

    @FindBy(xpath = "//div[@id=\"businessId\"]/div/div[1]/div[1]")
    private WebElement btnChildLocationOnNewCreateShiftPage;

    public void selectChildLocInCreateShiftWindow(String location) throws Exception {
        if (isElementLoaded(btnChildLocation, 10)) {
            click(btnChildLocation);
            SimpleUtils.pass("Child location button clicked Successfully");
            if (listWorkRoles.size() > 0) {
                for (WebElement listWorkRole : listWorkRoles) {
                    if (listWorkRole.getText().toLowerCase().contains(location.toLowerCase())) {
                        click(listWorkRole);
                        SimpleUtils.pass("Child location " + location + "selected Successfully");
                        break;
                    } else {
                        SimpleUtils.report("Child location" + location + " not selected");
                    }
                }

            } else {
                SimpleUtils.fail("Child location size are empty", false);
            }
        } else if (isElementLoaded(btnChildLocationOnNewCreateShiftPage, 5)) {
            click(btnChildLocationOnNewCreateShiftPage);
            SimpleUtils.pass("Child location button clicked Successfully");
            if (dropDownListOnNewCreateShiftPage.size() > 0) {
                for (WebElement childLocation : dropDownListOnNewCreateShiftPage) {
                    if (childLocation.getText().toLowerCase().contains(location.toLowerCase())) {
                        click(childLocation);
                        SimpleUtils.pass("Child location " + location + "selected Successfully");
                        break;
                    } else {
                        SimpleUtils.report("Child location" + location + " not selected");
                    }
                }

            } else {
                SimpleUtils.fail("Child location size are empty", false);
            }
        }else {
            SimpleUtils.fail("Child location button is not clickable", false);
        }
    }


    public void selectDaysByIndex(int index1, int index2, int index3) throws Exception {
        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
            if (index1 < weekDays.size() && index2 < weekDays.size() && index3 < weekDays.size()) {
                if (!weekDays.get(index1).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                    click(weekDays.get(index1));
                    SimpleUtils.report("Select day: " + weekDays.get(index1).getText() + " Successfully!");
                }
                if (!weekDays.get(index2).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                    click(weekDays.get(index2));
                    SimpleUtils.report("Select day: " + weekDays.get(index2).getText() + " Successfully!");
                }
                if (!weekDays.get(index3).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                    click(weekDays.get(index3));
                    SimpleUtils.report("Select day: " + weekDays.get(index3).getText() + " Successfully!");
                }
            } else {
                SimpleUtils.fail("There is index that out of range: " + index1 + ", " + index2 + ", " + index3 + ", the max value is 6!", false);
            }
        }else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5)
                && weekDaysInNewCreateShiftPage.size() == 7) {
            if (index1 < weekDaysInNewCreateShiftPage.size()
                    && index2 < weekDaysInNewCreateShiftPage.size()
                    && index3 < weekDaysInNewCreateShiftPage.size()) {
                if (!weekDaysInNewCreateShiftPage.get(index1).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("Mui-checked")) {
                    clickTheElement(weekDaysInNewCreateShiftPage.get(index1).findElement(By.cssSelector(".MuiButtonBase-root")).findElement(By.tagName("input")));
                    SimpleUtils.report("Select day: " + index1 + " Successfully!");
                }
                if (!weekDaysInNewCreateShiftPage.get(index2).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("Mui-checked")) {
                    clickTheElement(weekDaysInNewCreateShiftPage.get(index2).findElement(By.cssSelector(".MuiButtonBase-root")).findElement(By.tagName("input")));
                    SimpleUtils.report("Select day: " + index2 + " Successfully!");
                }
                if (!weekDaysInNewCreateShiftPage.get(index3).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("Mui-checked")) {
                    clickTheElement(weekDaysInNewCreateShiftPage.get(index3).findElement(By.cssSelector(".MuiButtonBase-root")).findElement(By.tagName("input")));
                    SimpleUtils.report("Select day: " + index3 + " Successfully!");
                }
            } else {
                SimpleUtils.fail("There is index that out of range: " + index1 + ", " + index2 + ", " + index3 + ", the max value is 6!", false);
            }
        }else{
            SimpleUtils.fail("Weeks Days failed to load!", false);
        }

    }

    @Override
    public void selectWorkingDaysOnNewShiftPageByIndex(int index) throws Exception {
        clearAllSelectedDays();
        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
            if (index < weekDays.size()) {
                if (!weekDays.get(index).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                    click(weekDays.get(index));
                    SimpleUtils.report("Select day: " + weekDays.get(index).getText() + " Successfully!");
                }
            }else {
                SimpleUtils.fail("There is index that out of range: " + index + ", the max value is 6!", false);
            }
        }else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5) && weekDaysInNewCreateShiftPage.size() == 7) {
            if (index < weekDaysInNewCreateShiftPage.size()) {
                WebElement checkBoxOfWeekDay = weekDaysInNewCreateShiftPage.get(index).findElement(By.cssSelector(".MuiButtonBase-root"));
                if (!checkBoxOfWeekDay.getAttribute("class").contains("Mui-checked")) {
                    click(checkBoxOfWeekDay);
                    SimpleUtils.report("Select day: " + weekDaysInNewCreateShiftPage.get(index).getText() + " Successfully!");
                }
            }else {
                SimpleUtils.fail("There is index that out of range: " + index + ", the max value is 6!", false);
            }
        }else{
            SimpleUtils.fail("Weeks Days failed to load!", true);
        }
    }

    @FindBy(className = "lgn-alert-modal")
    private WebElement confirmWindow;
    @FindBy(className = "lgn-action-button-success")
    private WebElement okBtnOnConfirm;
    @FindBy(css="[ng-show=\"hasSearchResults()\"] tr.table-row.ng-scope")
    private List<WebElement> searchTMRows;
    @FindBy(css = ".MuiTabs-root+div>div>div:nth-child(2)>div>div:nth-child(1) .MuiGrid-item")
    private List<WebElement> searchTableColumns;
    public String selectAndGetTheSelectedTM() throws Exception {
        WebElement selectedTM = null;
        String selectedTMName = "";
//		waitForSeconds(5);
        if (areListElementVisible(searchResultsOnNewCreateShiftPage, 5)) {
            int index = 0;
            if (areListElementVisible(searchTableColumns, 5)) {
                for (int i = 0; i < searchTableColumns.size(); i++) {
                    if (searchTableColumns.get(i).getText().trim().toLowerCase().equalsIgnoreCase("status")) {
                        index = i;
                        break;
                    }
                }
            }
            for (WebElement searchResult: searchResultsOnNewCreateShiftPage) {
                List<WebElement> allStatus= searchResult.findElements(By.cssSelector(".MuiGrid-item:nth-child("+ (index + 1) +")"));
                StringBuilder tmAllStatus = new StringBuilder();
                for (WebElement status: allStatus) {
                    tmAllStatus.append(" ").append(status.getText());
                }
                if((tmAllStatus.toString().contains("Available") || tmAllStatus.toString().contains("Unknown")) && !tmAllStatus.toString().contains("Assigned to this shift")
                && !tmAllStatus.toString().contains("Role Violation")){
                    selectedTMName = searchResult.findElements(By.cssSelector("p.MuiTypography-body1")).get(0).getText();
                    List<WebElement> assignAndOfferButtons = searchResult.findElements(By.tagName("button"));
                    if (MyThreadLocal.getAssignTMStatus()) {
                        clickTheElement(assignAndOfferButtons.get(0));
                        SimpleUtils.report("Click Assign button successfully! ");
                    } else {
                        if (assignAndOfferButtons.size()==1) {
                            clickTheElement(assignAndOfferButtons.get(0));
                            SimpleUtils.report("Only one offer button and click Offer button successfully! ");
                        } else {
                            clickTheElement(assignAndOfferButtons.get(1));
                            SimpleUtils.report("There are both assign and offer button, click offer button successfully! ");
                        }
                    }
                    if (isElementEnabled(btnAssignAnyway, 5)) {
                        click(btnAssignAnyway);
                    }
                    break;
                }
            }
        }else if(areListElementVisible(scheduleSearchTeamMemberStatus,5)
                || isElementLoaded(scheduleNoAvailableMatchStatus,5)){
            for(int i=0; i<scheduleSearchTeamMemberStatus.size();i++){
                String statusText = scheduleSearchTeamMemberStatus.get(i).getText();
                if((statusText.contains("Available") || statusText.contains("Unknown")) && !statusText.contains("Assigned to this shift")){
                    click(radionBtnSearchTeamMembers.get(i));
                    if (isElementEnabled(confirmWindow, 5)) {
                        click(okBtnOnConfirm);
                    }
                    selectedTM = searchTMRows.get(i);
                    selectedTMName = selectedTM.findElement(By.className("worker-edit-search-worker-display-name")).getText();
                    break;
                }
            }
            if (selectedTM == null) {
                SimpleUtils.report("Not able to found Available TMs");
            }
        } else{
            SimpleUtils.report("Not able to found Available status in SearchResult");
        }
        return selectedTMName;
    }

    @FindBy(css = "tr.table-row.ng-scope:nth-child(1)")
    private WebElement firstTableRow;

    @FindBy(css = "tr.table-row.ng-scope:nth-child(1) > td > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
    private WebElement firstnameOfTM;
    public String selectTeamMembers() throws Exception {
        String newSelectedTM = null;
        waitForSeconds(5);
        if (areListElementVisible(searchAndRecommendedTMTabs, 5)) {
            if (areListElementVisible(searchResultsOnNewCreateShiftPage, 5)) {
                if (isRecommendedTabSelected()){
                    newSelectedTM = searchResultsOnNewCreateShiftPage.get(0).findElements(By.cssSelector("p.MuiTypography-body1")).get(0).getText();
                    List<WebElement> assignAndOfferButtons = searchResultsOnNewCreateShiftPage.get(0).findElements(By.tagName("button"));
                    if (MyThreadLocal.getAssignTMStatus()) {
                        clickTheElement(assignAndOfferButtons.get(0));
                        SimpleUtils.report("Click Assign button successfully! ");
                    } else {
                        if (assignAndOfferButtons.size()==1) {
                            clickTheElement(assignAndOfferButtons.get(0));
                            SimpleUtils.report("Only one offer button and click Offer button successfully! ");
                        } else {
                            clickTheElement(assignAndOfferButtons.get(1));
                            SimpleUtils.report("There are both assign and offer button, click offer button successfully! ");
                        }
                    }
                } else {
                    newSelectedTM = selectAndGetTheSelectedTM();
                }
            } else {
                clickTheElement(searchAndRecommendedTMTabs.get(0));
                newSelectedTM = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
            }
            if(areListElementVisible(buttonsOnWarningMode,5)
                    && buttonsOnWarningMode.size()==2)
                click(buttonsOnWarningMode.get(1));
        }else if (areListElementVisible(recommendedScrollTable, 10)) {
            if (isElementLoaded(selectRecommendedOption, 5)) {
                String[] txtRecommendedOption = selectRecommendedOption.getText().replaceAll("\\p{P}", "").split(" ");
                if (Integer.parseInt(txtRecommendedOption[2]) == 0) {
                    SimpleUtils.report(txtRecommendedOption[0] + " Option no recommended TMs");
                    click(btnSearchteamMember.get(0));
                    newSelectedTM = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
                } else {
                    click(firstTableRow.findElement(By.cssSelector("td.table-field.action-field")));
                    newSelectedTM = firstnameOfTM.getText();
                }
            } else {
                click(btnSearchteamMember.get(0));
                newSelectedTM = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
                SimpleUtils.report("Recommended option not available on page");
            }
        } else if (isElementLoaded(textSearch, 10)) {
            newSelectedTM = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
        } else {
            SimpleUtils.fail("Select Team member option and Recommended options are not available on page", false);
        }

        return newSelectedTM;
    }


    @FindBy(css = "tr.table-row.ng-scope")
    List<WebElement> recommendedTMs;
    public String selectTeamMembers(int numOfTM) throws Exception {
        String newSelectedTMs = "";
        waitForSeconds(5);
        if (areListElementVisible(recommendedScrollTable, 20)) {
            if (isElementLoaded(selectRecommendedOption, 5)) {
                String[] txtRecommendedOption = selectRecommendedOption.getText().replaceAll("\\p{P}", "").split(" ");
                int recommendedNum= Integer.parseInt(txtRecommendedOption[2]);
                if (recommendedNum == 0) {
                    SimpleUtils.report(txtRecommendedOption[0] + " Option no recommended TMs");
                    click(btnSearchteamMember.get(0));
                    newSelectedTMs = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
                } else if (recommendedNum >= numOfTM){
                    for (int i = 0; i < numOfTM; i++){
                        click(recommendedTMs.get(i).findElement(By.cssSelector("td.table-field.action-field")));
                        newSelectedTMs = newSelectedTMs + recommendedTMs.get(i).findElement(By.cssSelector(".worker-edit-search-worker-display-name")).getText() + " ";
                    }
                } else {
                    for (int i = 0; i < recommendedNum; i++){
                        click(recommendedTMs.get(i).findElement(By.cssSelector("td.table-field.action-field")));
                        newSelectedTMs = newSelectedTMs + recommendedTMs.get(i).findElement(By.cssSelector(".worker-edit-search-worker-display-name")).getText() + " ";
                    }
                }
            } else {
                click(btnSearchteamMember.get(0));
                newSelectedTMs = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
                SimpleUtils.report("Recommended option not available on page");
            }
        } else if (isElementLoaded(textSearch, 5)) {
            newSelectedTMs = searchAndGetTMName(propertySearchTeamMember.get("AssignTeamMember"));
        } else {
            SimpleUtils.fail("Select Team member option and Recommended options are not available on page", false);
        }
        return newSelectedTMs;
    }

    @FindBy(css = "[class*=\"MuiAvatar-img\"]")
    List<WebElement> recommendedTMsOnCreation;
    @Override
    public boolean isRecommendedTabHasTMs() throws Exception {
        boolean  recommendedTabHasTMs = true;
        if (areListElementVisible(recommendedTMs, 10) && !(recommendedTMs.get(0).getText().equalsIgnoreCase("No result found"))) {
            SimpleUtils.report("Recommended tab on Shift Assign dialog is not empty!");
        } else if(areListElementVisible(recommendedTMsOnCreation, 10) && !(recommendedTMsOnCreation.get(0).getText().equalsIgnoreCase("No result found"))){
            SimpleUtils.report("Recommended tab on Shift Creation dialog is not empty!");
        }else{
            SimpleUtils.report("Recommended tab is empty!");
            recommendedTabHasTMs = false;
        }
        return recommendedTabHasTMs;
    }


    public String searchAndGetTMName(String searchInput) throws Exception {
        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
        String[] searchAssignTeamMember = searchInput.split(",");
        String selectedTMName = null;
        if (isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 5)) {
            for (int i = 0; i < searchAssignTeamMember.length; i++) {
                String[] searchTM = searchAssignTeamMember[i].split("\\.");
                textSearch.sendKeys(searchTM[0]);
                click(searchIcon);
                waitForSeconds(5);
                selectedTMName = newShiftPage.selectAndGetTheSelectedTM();
                if (!selectedTMName.equals("")) {
                    break;
                } else {
                    textSearch.clear();
                }
            }
            if (selectedTMName == null || selectedTMName.isEmpty()) {
                SimpleUtils.fail("Not able to found Available TMs in SearchResult", false);
            }
        } else if (isElementLoaded(textSearchOnNewCreateShiftPage, 10)) {
            for (int i = 0; i < searchAssignTeamMember.length; i++) {
                String[] searchTM = searchAssignTeamMember[i].split("\\.");
                String searchText = searchTM[0];
                textSearchOnNewCreateShiftPage.sendKeys(Keys.CONTROL, "a");
                textSearchOnNewCreateShiftPage.sendKeys(Keys.DELETE);
                textSearchOnNewCreateShiftPage.sendKeys(searchText);
                waitForSeconds(3);
                selectedTMName = newShiftPage.selectAndGetTheSelectedTM();
                if (!selectedTMName.equals("")) {
                    break;
                } else {
                    clickTheElement(searchAndRecommendedTMTabs.get(1));
                    clickTheElement(searchAndRecommendedTMTabs.get(0));
                }
            }

            if (selectedTMName == null || selectedTMName.isEmpty()) {
                SimpleUtils.fail("Not able to found Available TMs in SearchResult", false);
            }

        }else {
            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
        }
        return selectedTMName;
    }


    @Override
    public void selectSpecificTMWhileCreateNewShift(String teamMemberName) throws Exception {
        if(areListElementVisible(btnSearchteamMember,5)){
            click(btnSearchteamMember.get(1));
            searchText(teamMemberName);
        }

    }


    @Override
    public void addOpenShiftWithLastDay(String workRole) throws Exception {
        if (isElementLoaded(createNewShiftWeekView,5)) {
            click(createNewShiftWeekView);
            selectWorkRole(workRole);
            clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.OpenShift.getValue());
            clearAllSelectedDays();
            if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
                if (!weekDays.get(6).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                    click(weekDays.get(6));
                }
            } else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5) && weekDaysInNewCreateShiftPage.size() == 7) {
                if (!weekDaysInNewCreateShiftPage.get(6).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("Mui-checked")) {
                    clickTheElement(weekDaysInNewCreateShiftPage.get(6).findElement(By.cssSelector(".MuiButtonBase-root")).findElement(By.tagName("input")));
                    SimpleUtils.report("Select day: " + 6 + " Successfully!");
                }
            }
            clickOnCreateOrNextBtn();
        } else
            SimpleUtils.fail("Day View Schedule edit mode, add new shift button not found for Week Day: '" +
                    getActiveWeekText() + "'", false);
    }



    @Override
    public void addManualShiftWithLastDay(String workRole, String tmName) throws Exception {
        if (isElementLoaded(createNewShiftWeekView,5)) {
            click(createNewShiftWeekView);
            selectWorkRole(workRole);
            if (weekDays.get(0).getAttribute("class").contains("week-day-multi-picker-day-selected"))
                click(weekDays.get(0));
            clickRadioBtnStaffingOption(ScheduleTestKendraScott2.staffingOption.ManualShift.getValue());
            if (weekDays.size() == 7) {
                for (int i = weekDays.size() - 1; i >= 0; i--) {
                    if (weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-disabled"))
                        continue;
                    else {
                        click(weekDays.get(i));
                        break;
                    }
                }
            }
            clickOnCreateOrNextBtn();
            searchTeamMemberByName(tmName);
            if(isElementLoaded(btnAssignAnyway,5))
                click(btnAssignAnyway);
            clickOnOfferOrAssignBtn();
        } else
            SimpleUtils.fail("Failed to load 'Create New Shift' label, maybe it is not in edit mode", false);
    }


    @FindBy(css = "[ng-repeat=\"day in summary.workingHours\"]")
    private List<WebElement> operatingHours;

    @FindBy(css = "[ng-class=\"{ 'switcher-closed': !value }\"]")
    private WebElement openOrCloseWeekDayButton;

    @FindBy(css = "[ng-click=\"$dismiss()\"] button[ng-click=\"$ctrl.onSubmit({type:'saveas',label:$ctrl.label})\"]")
    private WebElement editOperatingHourCancelButton;

    @FindBy(css = "[ng-click=\"save()\"]")
    private WebElement editOperatingHourSaveButton;

    @FindBy(css = "lg-button[label=\"Cancel\"]")
    private WebElement operatingHoursCancelBtn;

    @FindBy(css = "lg-button.edit-operating-hours-link")
    private WebElement editOperatingHoursBtn;
    @FindBy (css = ".operating-hours-day-list-item.ng-scope")
    private List<WebElement> operatingHoursDayLists;
    @Override
    public void editOperatingHoursOnScheduleOldUIPage(String startTime, String endTime, List<String> weekDaysToClose) throws Exception {
        if (areListElementVisible(operatingHours, 20) && operatingHours.size()==7 && isElementLoaded(editOperatingHoursBtn, 10)){
            clickTheElement(editOperatingHoursBtn);
            if (isElementLoaded(customizeNewShift, 10)) {
                if (areListElementVisible(operatingHoursDayLists, 15)) {
                    for (WebElement dayList : operatingHoursDayLists) {
                        WebElement weekDay = dayList.findElement(By.cssSelector(".operating-hours-day-list-item-day"));
                        if (weekDay != null) {
                            WebElement checkbox = dayList.findElement(By.cssSelector("input[type=\"checkbox\"]"));
                            if (!weekDaysToClose.contains(weekDay.getText())) {
                                if (!startTime.equals("") || !endTime.equals("")) {
                                    if (checkbox.getAttribute("class").contains("ng-empty")) {
                                        clickTheElement(checkbox);
                                    }
                                    List<WebElement> startNEndTimes = dayList.findElements(By.cssSelector("[ng-if*=\"day.isOpened\"] input"));
                                    String openTime = startNEndTimes.get(0).getAttribute("value");
                                    String closeTime = startNEndTimes.get(1).getAttribute("value");
                                    if (!openTime.equals(startTime) || !closeTime.equals(endTime)) {
                                        startNEndTimes.get(0).clear();
                                        startNEndTimes.get(1).clear();
                                        startNEndTimes.get(0).sendKeys(startTime);
                                        startNEndTimes.get(1).sendKeys(endTime);
                                    }
                                }

                            } else {
                                if (!checkbox.getAttribute("class").contains("ng-empty")) {
                                    clickTheElement(checkbox);
                                }
                            }
                        } else {
                            SimpleUtils.fail("Failed to find weekday element!", false);
                        }
                    }
                    clickTheElement(editOperatingHourSaveButton);
                    waitForSeconds(1);
                    if (isElementEnabled(editOperatingHoursBtn, 15)) {
                        SimpleUtils.pass("Create Schedule: Save the operating hours Successfully!");
                    } else {
                        SimpleUtils.fail("Create Schedule: Click on Save the operating hours button failed, Next button is not enabled!", false);
                    }
                }
            } else
                SimpleUtils.fail("Edit operating hours modal fail to load! ", false);
        }else{
            SimpleUtils.fail("Operating Hours not loaded Successfully!", false);
        }
    }


    @Override
    public void searchTeamMemberByNameNLocation(String name, String location) throws Exception {
        if (areListElementVisible(searchAndRecommendedTMTabs, 5)) {
            if (searchAndRecommendedTMTabs.size() == 2) {
                //click(btnSearchteamMember.get(1));
                if (isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
                    textSearchOnNewCreateShiftPage.clear();
                    textSearchOnNewCreateShiftPage.sendKeys(name);
                    waitForSeconds(3);
                    if (areListElementVisible(searchResultsOnNewCreateShiftPage, 30)) {
                        for (WebElement searchResult : searchResultsOnNewCreateShiftPage) {
                            List<WebElement> tmInfo = searchResult.findElements(By.cssSelector("p.MuiTypography-body1"));
                            String tmName = tmInfo.get(0).getText();
                            String locationName = tmInfo.get(2).getText();
                            List<WebElement> assignAndOfferButtons = searchResult.findElements(By.tagName("button"));
                            WebElement assignButton = assignAndOfferButtons.get(0);
                            WebElement offerButton = assignAndOfferButtons.get(1);
                            if (tmName != null && locationName!=null && assignButton != null && offerButton != null) {
                                if (tmName.toLowerCase().trim().replaceAll("\n"," ").contains(name.split(" ")[0].trim().toLowerCase())
                                        && locationName.toLowerCase().trim().replaceAll("\n"," ").contains(location.trim().toLowerCase())) {
                                    if (MyThreadLocal.getAssignTMStatus()) {
                                        clickTheElement(assignButton);
                                    } else
                                        clickTheElement(offerButton);
                                    SimpleUtils.report("Select Team Member: " + name + " Successfully!");
                                    waitForSeconds(2);
                                    if (areListElementVisible(buttonsOnWarningMode, 5) && buttonsOnWarningMode.get(1).getText().toLowerCase().equalsIgnoreCase("assign anyway")) {
                                        clickTheElement(buttonsOnWarningMode.get(1));
                                        SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
                                    }
                                    break;
                                }
                            }else {
                                SimpleUtils.fail("Worker name or buttons not loaded Successfully!", false);
                            }
                        }
                    }else {
                        SimpleUtils.fail("Failed to find the team member!", false);
                    }
                }else {
                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
                }
            }else {
                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
            }
        } else if(areListElementVisible(btnSearchteamMember,15)) {
            if (btnSearchteamMember.size() == 2) {
                //click(btnSearchteamMember.get(1));
                if (isElementLoaded(textSearch, 5) && isElementLoaded(searchIcon, 15)) {
                    textSearch.clear();
                    textSearch.sendKeys(name);
                    clickTheElement(searchIcon);
                    if (areListElementVisible(searchResults, 15)) {
                        for (WebElement searchResult : searchResults) {
                            WebElement workerName = searchResult.findElement(By.className("worker-edit-search-worker-display-name"));
                            WebElement optionCircle = searchResult.findElement(By.className("tma-staffing-option-outer-circle"));
                            WebElement locationInfo = searchResult.findElement(By.className("tma-description-fields"));
                            if (workerName != null && optionCircle != null) {
                                if (workerName.getText().toLowerCase().trim().replaceAll("\n"," ").contains(name.trim().toLowerCase()) && locationInfo.getText().toLowerCase().trim().replaceAll("\n"," ").contains(location.trim().toLowerCase())) {
                                    click(optionCircle);
                                    SimpleUtils.report("Select Team Member: " + name + " Successfully!");
                                    waitForSeconds(2);
                                    if (isElementLoaded(btnAssignAnyway, 5) && btnAssignAnyway.getText().equalsIgnoreCase("ASSIGN ANYWAY")) {
                                        click(btnAssignAnyway);
                                        SimpleUtils.report("Assign Team Member: Click on 'ASSIGN ANYWAY' button Successfully!");
                                    }
                                    break;
                                }
                            }else {
                                SimpleUtils.fail("Worker name or option circle not loaded Successfully!", false);
                            }
                        }
                    }else {
                        SimpleUtils.fail("Failed to find the team member!", false);
                    }
                }else {
                    SimpleUtils.fail("Search text not editable and icon are not clickable", false);
                }
            }else {
                SimpleUtils.fail("Search team member should have two tabs, failed to load!", false);
            }
        } else
            SimpleUtils.fail("Search team member tab fail to load! ", false);
    }



    @FindBy(xpath="//div[@class='tab-label']/span[text()='Search Team Members']")
    private WebElement btnSearchTeamMember;
    public void selectTeamMembersOptionForSchedule() throws Exception {
        NewShiftPage newShiftPage = new ConsoleNewShiftPage();
        if(isElementEnabled(btnSearchTeamMember,5)){
            click(btnSearchTeamMember);
            if(isElementLoaded(textSearch,5)) {
                newShiftPage.searchText(propertySearchTeamMember.get("AssignTeamMember"));
            }
        }else{
            SimpleUtils.fail("Select Team member option not available on page",false);
        }

    }


    public void selectTeamMembersOptionForOverlappingSchedule() throws Exception{
        if(isElementEnabled(btnSearchTeamMember,5)){
            click(btnSearchTeamMember);
            if(isElementLoaded(textSearch,5)) {
                searchWorkerName(propertySearchTeamMember.get("AssignTeamMember"));
            }
        }else{
            SimpleUtils.fail("Select Team member option not available on page",false);
        }

    }

    public void searchWorkerName(String searchInput) throws Exception {
        String[] searchAssignTeamMember = searchInput.split(",");
        if(isElementLoaded(textSearch,10) && isElementLoaded(searchIcon,10)){
            for(int i=0; i<searchAssignTeamMember.length;i++){
                textSearch.sendKeys(searchAssignTeamMember[i]);
                click(searchIcon);
                if(getScheduleOverlappingStatus()){
                    break;
                }else{
                    textSearch.clear();
                    if(i== searchAssignTeamMember.length-1){
                        SimpleUtils.fail("There is no data found for given team member. Please provide some other input", false);
                    }
                }
            }
        }else{
            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
        }

    }


    @FindBy(xpath="//div[@class='tma-search-action']/following-sibling::div[1]//div[@class='worker-edit-search-worker-name']")
    private List<WebElement> searchWorkerDisplayName;
    public boolean getScheduleOverlappingStatus()throws Exception {
        boolean ScheduleStatus = false;
        if(areListElementVisible(scheduleSearchTeamMemberStatus,5)){
            for(int i=0; i<scheduleSearchTeamMemberStatus.size();i++){
                if(scheduleSearchTeamMemberStatus.get(i).getText().contains("Scheduled")){
                    click(radionBtnSearchTeamMembers.get(i));
                    String workerDisplayFirstNameText =(searchWorkerDisplayName.get(i).getText().replace(" ","").split("\n"))[0];
                    String workerDisplayLastNameText =(searchWorkerDisplayName.get(i).getText().replace(" ","").split("\n"))[1];
                    setTeamMemberName(workerDisplayFirstNameText + workerDisplayLastNameText);
                    boolean flag = displayAlertPopUp();
                    if (flag) {
                        ScheduleStatus = true;
                        break;
                    }
                }
            }
        }

        return ScheduleStatus;
    }


    @Override
    public boolean displayAlertPopUp() throws Exception{
        boolean flag = true;
        String msgAlert = null;
        if(isElementLoaded(popUpScheduleOverlap,5)){
            if(isElementLoaded(alertMessage,5)){
                msgAlert = alertMessage.getText();
                if(isElementLoaded(btnAssignAnyway,5)){
                    if(btnAssignAnyway.getText().toLowerCase().equals("OK".toLowerCase())){
                        click(btnAssignAnyway);
                        flag = false;
                    } else if (btnAssignAnyway.getText().toUpperCase().equals("ASSIGN ANYWAY")) {
                        flag = true;
                    } else {
                        String startTime = ((msgAlert.split(": "))[1].split(" - "))[0];
                        String startTimeFinal = SimpleUtils.convertTimeIntoHHColonMM(startTime);
                        String endTime = (((msgAlert.split(": "))[1].split(" - "))[1].split(" "))[0];
                        String endTimeFinal = SimpleUtils.convertTimeIntoHHColonMM(endTime);
                        String timeDuration = startTimeFinal + "-" + endTimeFinal;
                        setScheduleHoursTimeDuration(timeDuration);
                        click(btnAssignAnyway);
                        flag= true;
                    }

                }else{
                    SimpleUtils.fail("Schedule Overlap Assign Anyway button not displayed on the page",false);
                }
            }else{
                SimpleUtils.fail("Schedule Overlap alert message not displayed",false);
            }
        }else {
            flag = false;
        }
        return flag;
    }


    @Override
    public void selectDaysFromCurrentDay(String currentDay) throws Exception {
        int index = 7;
        String[] items = currentDay.split(" ");
        if (items.length == 3) {
            currentDay = items[0].substring(0, 3) + items[1].substring(0, 3) + (items[2].length() == 1 ? "0"+items[2] : items[2]);
        }
        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
            for (int i = 0; i < weekDays.size(); i++) {
                String weekDay = weekDays.get(i).getText().replaceAll("\\s*", "");
                if (weekDay.equalsIgnoreCase(currentDay)) {
                    index = i;
                }
                if (i >= index) {
                    if (!weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                        click(weekDays.get(i));
                        SimpleUtils.pass("Select the day: " + weekDays.get(i).getText() + " successfully!");
                    }
                }
            }
        }else{
            SimpleUtils.fail("Weeks Days failed to load!", true);
        }
    }


    @FindBy(css = "div.tma-time-interval.fl-right.ng-binding")
    private WebElement timeDurationWhenCreateNewShift;

    @Override
    public String getTimeDurationWhenCreateNewShift() throws Exception {
        if (isElementLoaded(timeDurationWhenCreateNewShift,5)) {
            String timeDuration = timeDurationWhenCreateNewShift.getText();
            return timeDuration;
        }else
            SimpleUtils.fail("time duration load failed",true);
        return null;
    }


    @Override
    public void selectSpecificWorkDay(int dayCountInOneWeek) {
        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
            SimpleUtils.report("Day count is "+dayCountInOneWeek);
            for (int i = 0; i < dayCountInOneWeek; i++) {
                if (!weekDays.get(i).getAttribute("class").contains("selected")) {
                    click(weekDays.get(i));
                    SimpleUtils.pass("Click week day "+i+" successfully! ");
                }
            }
        }else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5) && weekDaysInNewCreateShiftPage.size() == 7) {
            SimpleUtils.report("Day count is "+dayCountInOneWeek);
            for (int i = 0; i < dayCountInOneWeek; i++) {
                if (!weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("checked")
                        && (weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("aria-disabled") == null ||
                        (weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("aria-disabled") != null && weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("aria-disabled").contains("false")))) {
                    click(weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")));
                    SimpleUtils.pass("Click week day "+i+" successfully! ");
                }
            }
        }else
            SimpleUtils.fail("week days load failed",false);

    }

    @Override
    public void selectMultipleOrSpecificWorkDay(int dayCountInOneWeek, Boolean isSingleDay) {
        if (isSingleDay) {
            if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
                if (!weekDays.get(dayCountInOneWeek).getAttribute("class").contains("selected")) {
                    click(weekDays.get(dayCountInOneWeek));
                }
            } else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5) && weekDaysInNewCreateShiftPage.size() == 7) {
                if (!weekDaysInNewCreateShiftPage.get(dayCountInOneWeek).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("checked")) {
                    click(weekDaysInNewCreateShiftPage.get(dayCountInOneWeek).findElement(By.cssSelector(".MuiButtonBase-root")));
                }
            } else
                SimpleUtils.fail("week days load failed",true);
        } else {
                if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
                    for (int i = 0; i < dayCountInOneWeek; i++) {
                        if (!weekDays.get(i).getAttribute("class").contains("selected")) {
                            click(weekDays.get(i));
                        }
                    }
                } else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5) && weekDaysInNewCreateShiftPage.size() == 7) {
                    for (int i = 0; i < dayCountInOneWeek; i++) {
                        if (!weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")).getAttribute("class").contains("checked")) {
                            click(weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")));
                        }
                    }
                } else
                    SimpleUtils.fail("week days load failed",true);
            }
        }

    @Override
    public List<Integer> selectDaysByCountAndCannotSelectedDate(int count, String cannotSelectedDate) throws Exception {
        List<Integer> indexes = new ArrayList<>();
        int selectedCount = 0;
        if (count > 7) {
            SimpleUtils.fail("Create New Shift: There are total 7 days, the count: " + count + " is larger than 7", false);
        }
        if (areListElementVisible(weekDays, 15) && weekDays.size() == 7) {
            for (int i = 0; i < 7; i++) {
                if (weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-disabled")) {
                    SimpleUtils.report("Day: " + weekDays.get(i).getText() + " is disabled!");
                } else {
                    if (cannotSelectedDate == null || cannotSelectedDate == "") {
                        if (!weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                            click(weekDays.get(i));
                            SimpleUtils.report("Select day: " + weekDays.get(i).getText() + " Successfully!");
                        }
                        selectedCount++;
                        indexes.add(i);
                    } else {
                        int date = Integer.parseInt(weekDays.get(i).getText().substring(weekDays.get(i).getText().length() - 2).trim());
                        int cannotDate = Integer.parseInt(cannotSelectedDate.substring(cannotSelectedDate.length() - 2).trim());
                        if (date != cannotDate) {
                            if (!weekDays.get(i).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                                click(weekDays.get(i));
                                SimpleUtils.report("Select day: " + weekDays.get(i).getText() + " Successfully!");
                            }
                            selectedCount++;
                            indexes.add(i);
                        }
                    }
                    if (selectedCount == count) {
                        SimpleUtils.pass("Create New Shift: Select " + count + " days Successfully!");
                        break;
                    }
                }
            }
            if (selectedCount != count) {
                SimpleUtils.fail("Create New Shift: Failed to select " + count + " days! Actual is: " + selectedCount + " days!", false);
            }
        }else if (areListElementVisible(weekDaysInNewCreateShiftPage, 15) && weekDaysInNewCreateShiftPage.size() == 7) {
            for (int i = 0; i < 7; i++) {
                if (weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector("span.MuiButtonBase-root"))
                        .getAttribute("class").contains("Mui-disabled")) {
                    SimpleUtils.report("Day: " + weekDaysInNewCreateShiftPage.get(i).getText() + " is disabled!");
                } else {
                    if (cannotSelectedDate == null || cannotSelectedDate.equals("")) {
                        if (!weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector("span.MuiButtonBase-root"))
                                .getAttribute("class").contains("Mui-checked")) {
                            click(weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector("span.MuiButtonBase-root")));
                            SimpleUtils.report("Select day: " + weekDaysInNewCreateShiftPage.get(i).getText() + " Successfully!");
                        }
                        selectedCount++;
                        indexes.add(i);
                    } else {
                        int date = getWeekDaysNDates().get(weekDaysInNewCreateShiftPage.get(i).getText().substring(0, 3));
                        int cannotDate = Integer.parseInt(cannotSelectedDate.substring(cannotSelectedDate.length() - 2).trim());
                        if (date != cannotDate) {
                            if (!weekDaysInNewCreateShiftPage.get(i).getAttribute("class").contains("week-day-multi-picker-day-selected")) {
                                click(weekDaysInNewCreateShiftPage.get(i));
                                SimpleUtils.report("Select day: " + weekDaysInNewCreateShiftPage.get(i).getText() + " Successfully!");
                            }
                            selectedCount++;
                            indexes.add(i);
                        }
                    }
                    if (selectedCount == count) {
                        SimpleUtils.pass("Create New Shift: Select " + count + " days Successfully!");
                        break;
                    }
                }
            }
            if (selectedCount != count) {
                SimpleUtils.fail("Create New Shift: Failed to select " + count + " days! Actual is: " + selectedCount + " days!", false);
            }
        }else{
            SimpleUtils.fail("Weeks Days failed to load!", false);
        }

        return indexes;
    }

    @Override
    public void selectWeekDaysByDayName(String dayName) throws Exception {
        boolean isDayNameExist = false;
        if (areListElementVisible(weekDays, 5) && weekDays.size() == 7) {
            for(int i=0; i< weekDays.size(); i++){
                String weekDayName = weekDays.get(i).getText().split("\n")[0];
                if (weekDayName.equalsIgnoreCase(dayName)){
                    click(weekDays.get(i));
                    SimpleUtils.report("Select day: " + weekDays.get(i).getText() + " Successfully!");
                    isDayNameExist = true;
                    break;
                }
            }
            if (!isDayNameExist) {
                SimpleUtils.fail("This is a wrong day name: "+ dayName+ "The correct day names should be: Mon, TUE, WED, THU, FRI, SAT, SUN", true);
            }
        }else if (areListElementVisible(weekDaysInNewCreateShiftPage, 5)
                && weekDaysInNewCreateShiftPage.size() == 7) {
            for(int i=0; i< weekDaysInNewCreateShiftPage.size(); i++){
                String weekDayName = weekDaysInNewCreateShiftPage.get(i).getText().split("\n")[0];
                if (weekDayName.toLowerCase().contains(dayName.toLowerCase())){
                    click(weekDaysInNewCreateShiftPage.get(i).findElement(By.cssSelector(".MuiButtonBase-root")));
                    SimpleUtils.report("Select day: " + weekDaysInNewCreateShiftPage.get(i).getText() + " Successfully!");
                    isDayNameExist = true;
                    break;
                }
            }
            if (!isDayNameExist) {
                SimpleUtils.fail("This is a wrong day name: "+ dayName+ "The correct day names should be: Mon, TUE, WED, THU, FRI, SAT, SUN", true);
            }
        }else{
            SimpleUtils.fail("Weeks Days failed to load!", true);
        }
    }

    @Override
    public List<String> getAllOperatingHrsOnCreateShiftPage() throws Exception {
        List<String> allOperatingHrs = new ArrayList<>();
        if (areListElementVisible(scheduleOperatingHrsOnEditPage, 15)) {
            for (WebElement operatingHour : scheduleOperatingHrsOnEditPage) {
                if (operatingHour.getAttribute("class").contains("am")) {
                    allOperatingHrs.add(operatingHour.getText() + "am");
                } else {
                    allOperatingHrs.add(operatingHour.getText() + "pm");
                }
            }
        } else
            SimpleUtils.fail("The operating hours on create shift page fail to load! ", false);
        return allOperatingHrs;
    }

    @FindBy(css = "div.week-day-multi-picker-day-selected")
    private List<WebElement> selectedDaysOnCreateShiftPage;
    @FindBy(css = "[id*=\"legion_cons_Schedule_Schedule_CreateShift\"] span.Mui-checked")
    private List<WebElement> selectedDaysOnNewCreateShiftPage;

    @Override
    public List<String> getSelectedDayInfoFromCreateShiftPage() throws Exception {
        List<String> selectedDates = new ArrayList<>();
        if (areListElementVisible(selectedDaysOnCreateShiftPage, 5) && selectedDaysOnCreateShiftPage.size()>0) {
            for (WebElement selectedDate: selectedDaysOnCreateShiftPage){
                selectedDates.add(selectedDate.getText());
            }
            SimpleUtils.pass("Get selected days info successfully");
        }else if (areListElementVisible(selectedDaysOnNewCreateShiftPage, 5) && selectedDaysOnNewCreateShiftPage.size()>0) {
            for (int i=0;i< selectedDaysOnNewCreateShiftPage.size();i++){
                selectedDates.add(getDriver().findElements(By.xpath("//div[contains(@class,'MuiGrid-item')]/label/span[contains(@class,'Mui-checked')]/following-sibling::span")).get(i).getText().split(",")[0].trim());
            }
            SimpleUtils.pass("Get selected days info successfully");
        }else
            SimpleUtils.fail("Select days load failed",false);
        return selectedDates;
    }

    @FindBy(css = ".tma-dismiss-button")
    private WebElement closeSelectTMWindowBtn;
    @Override
    public void closeCustomizeNewShiftWindow() throws Exception {
        if (isElementLoaded(closeSelectTMWindowBtn, 10)){
            clickTheElement(closeSelectTMWindowBtn);
            waitUntilElementIsInVisible(closeSelectTMWindowBtn);
        } else {
            SimpleUtils.fail("Customize New Shift window: Close button not loaded Successfully!", false);
        }
    }

    @FindBy(css = "[ng-click=\"prevAction()\"]")
    private WebElement backButton;
    @FindBy(css = "[ng-click=\"back()\"]")
    private WebElement backButtonOnNewCreateShiftPage;

    @Override
    public void clickOnBackButton () throws Exception {
        if (isElementLoaded(backButton, 5)) {
            clickTheElement(backButton);
            SimpleUtils.pass("Click Back button successfully! ");
        } else if (isElementLoaded(backButtonOnNewCreateShiftPage, 5)) {
            clickTheElement(backButtonOnNewCreateShiftPage);
            SimpleUtils.pass("Click Back button successfully! ");
        }else
            SimpleUtils.fail("The Back button fail to loaded! ", false);

    }

    public boolean checkIfWarningModalDisplay () throws Exception {
        boolean checkIfWarningModalDisplay = false;
        if (isElementLoaded(popUpScheduleOverlap, 10)) {
            checkIfWarningModalDisplay = true;
        }
        return checkIfWarningModalDisplay;
    }

    @FindBy(css = ".MuiDialogContent-root p")
    private List<WebElement> warningMessagesInWarningModeOnNewCreaeShiftPage;
    public String getWarningMessageFromWarningModal () throws Exception {
        String warningMesssage ="";
        if (isElementLoaded(warningMessagesInWarningMode, 5)) {
            warningMesssage= warningMessagesInWarningMode.getText();
        }else if (areListElementVisible(warningMessagesInWarningModeOnNewCreaeShiftPage, 5)) {
            for (WebElement message: warningMessagesInWarningModeOnNewCreaeShiftPage) {
                warningMesssage = warningMesssage + " " + message.getText();
            }
            warningMesssage = warningMesssage.replace(" AM", "am").replace(" PM", "pm").replace(":00", "");
        }else
            SimpleUtils.fail("The warning message fail to load! ", false);
        return warningMesssage;
    }

    public void clickOnOkButtonOnWarningModal () throws Exception {
        if (isElementLoaded(okBtnInWarningMode, 5)) {
            clickTheElement(okBtnInWarningMode);
        }else if (areListElementVisible(buttonsOnWarningMode, 5)) {
            clickTheElement(buttonsOnWarningMode.get(0));
        }else
            SimpleUtils.fail("The OK button fail to load! ", false);
    }

    @FindBy(css = "#create-new-shift-react")
    private WebElement newCreateShiftModal;

    public boolean checkIfNewCreateShiftPageDisplay() throws Exception {
        if (isElementLoaded(newCreateShiftModal, 35)) {
            SimpleUtils.pass("The new create shift modal display! ");
            return true;
        } else {
            SimpleUtils.report("The new create shift modal is not display! ");
            return false;
        }
    }

    public void searchWithOutSelectTM(String tmName) throws Exception {
        if (isElementLoaded(textSearch, 10) && isElementLoaded(searchIcon, 10)) {
            textSearch.clear();
            textSearch.sendKeys(tmName);
            click(searchIcon);
            MyThreadLocal.setMessageOfTMScheduledStatus("");
        } else if (isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
            textSearchOnNewCreateShiftPage.sendKeys(Keys.CONTROL, "a");
            textSearchOnNewCreateShiftPage.sendKeys(Keys.DELETE);
            textSearchOnNewCreateShiftPage.sendKeys(tmName);
            waitForSeconds(3);
            MyThreadLocal.setMessageOfTMScheduledStatus("");
        }else {
            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
        }
    }


    @FindBy(css = ".MuiInputAdornment-positionEnd svg")
    private WebElement nextDayIcon;
    @FindBy(css = "[role=\"tooltip\"]")
    private WebElement nextDayTooltip;
    public void checkOrUnCheckNextDayOnCreateShiftModal(boolean toCheck) throws Exception {
        if (isElementLoaded(nextDayIcon, 10)) {
            moveToElementAndClick(nextDayIcon);
            if (isElementLoaded(nextDayTooltip, 5)){
                if (toCheck) {
                    if (nextDayTooltip.findElement(By.cssSelector("span.MuiCheckbox-root")).getAttribute("class").contains("checked")){
                        SimpleUtils.pass("Next day checkbox has already been checked! ");
                    } else {
                        if(!isElementLoaded(nextDayTooltip, 5)){
                            click(nextDayIcon);
                        }
                        waitForSeconds(3);
                        clickTheElement(nextDayTooltip.findElement(By.cssSelector("span.MuiCheckbox-root input")));
                        if (nextDayTooltip.findElement(By.cssSelector("span.MuiCheckbox-root")).getAttribute("class").contains("checked")){
                            SimpleUtils.pass("Check Next day checkbox successfully! ");
                        } else
                            SimpleUtils.fail("Fail to check Next day checkbox! ", false);
                    }
                } else {
                    if (nextDayTooltip.findElement(By.cssSelector("span.MuiCheckbox-root")).getAttribute("class").contains("checked")){
                        if (!isElementLoaded(nextDayTooltip, 5)) {
                            moveToElementAndClick(nextDayIcon);
                        }
                        clickTheElement(nextDayTooltip.findElement(By.cssSelector("span.MuiCheckbox-root input")));
                        waitForSeconds(2);
                        if (!isElementLoaded(nextDayTooltip, 5)) {
                            moveToElementAndClick(nextDayIcon);
                        }
                        if (!nextDayTooltip.findElement(By.cssSelector("span.MuiCheckbox-root")).getAttribute("class").contains("checked")){
                            SimpleUtils.pass("Uncheck Next day checkbox successfully! ");
                        } else
                            SimpleUtils.fail("Fail to uncheck Next day checkbox! ", false);
                    } else {
                        SimpleUtils.pass("Next day checkbox has already been unchecked! ");
                    }
                }
            }
        }else {
            SimpleUtils.fail("Next day icon fail to load! ", false);
        }
    }


    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[2]/div/div[2]/button")
    private WebElement clearAssignmentsLink;
    public void clickClearAssignmentsLink() throws Exception {
        if (isElementLoaded(clearAssignmentsLink, 5)) {
            clickTheElement(clearAssignmentsLink);
            if (!isElementLoaded(clearAssignmentsLink, 3)) {
                SimpleUtils.pass("Click clear assignments link successfully! ");
            } else
                SimpleUtils.fail("Fail to click clear assignments link! ", false);
        } else
            SimpleUtils.report("The clear assignments link fail to load! ");
    }

    @Override
    public boolean areWorkRoleDisplayOrderCorrect(HashMap<String, Integer> workRoleNOrders) throws Exception {
        boolean isConsistent = true;
        try {
            if (isElementLoaded(workRoleOnNewShiftPage, 5)) {
                click(workRoleOnNewShiftPage);
                SimpleUtils.pass("Work Role button clicked Successfully");
                waitForSeconds(1);
                if (dropDownListOnNewCreateShiftPage.size() > 0) {
                    for (int i = 0; i < dropDownListOnNewCreateShiftPage.size() - 1; i++) {
                        int order1 = workRoleNOrders.get(dropDownListOnNewCreateShiftPage.get(i).getText().toLowerCase().replaceAll(" ", ""));
                        int order2 = workRoleNOrders.get(dropDownListOnNewCreateShiftPage.get(i + 1).getText().toLowerCase().replaceAll(" ", ""));
                        if (order1 > order2) {
                            isConsistent = false;
                            break;
                        }
                    }
                } else {
                    isConsistent = false;
                    SimpleUtils.fail("Work Roles size are empty", false);
                }
            } else {
                isConsistent = false;
                SimpleUtils.fail("Work Role button is not clickable", false);
            }
        } catch (Exception e) {
            SimpleUtils.fail("Get exception when clicking on Work role dropdown: " + e.getStackTrace(), false);
        }
        return isConsistent;
    }

    public boolean checkIfWorkRoleDropDownIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(workRoleOnNewShiftPage, 5)) {
            isLoaded = true;
            SimpleUtils.report("The work role dropdown is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The work role dropdown is not loaded on New Create Shift page! ");
        return isLoaded;
    }



    public List<String> searchWorkRoleOnNewCreateShiftPage (String workRole) throws Exception {
        List<String> searchResult = new ArrayList<>();
        if (isElementLoaded(workRoleOnNewShiftPage, 5)) {
            click(workRoleOnNewShiftPage);
            SimpleUtils.pass("Work Role button clicked Successfully");
            getDriver().findElement(By.cssSelector("[id*=\"WorkRole_menu\"] input")).sendKeys(workRole);
            if (dropDownListOnNewCreateShiftPage.size() > 0) {
                for (WebElement listWorkRole : dropDownListOnNewCreateShiftPage) {
                    searchResult.add(listWorkRole.getText());
                }
            } else {
                SimpleUtils.report("Work Roles size are empty");
            }
        } else
            SimpleUtils.report("The work role dropdown is not loaded on New Create Shift page! ");
        return searchResult;
    }

    @FindBy(css = "[id*=\"ShiftName_field\"]")
    private WebElement shiftNameOnNewCreateShiftPage;
    @FindBy(css = "[placeholder*=\"Shift Name\"]")
    private WebElement shiftNameOnOldCreateShiftPage;
    public boolean checkIfShiftNameInputIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(shiftNameOnNewCreateShiftPage, 5)) {
            isLoaded = true;
            SimpleUtils.report("The shift name input is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The shift name input is not loaded on New Create Shift page! ");
        return isLoaded;
    }


    public boolean checkIfShiftStartAndEndInputsAreLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(shiftStartInputOnNewCreateShiftPage, 5) &&
                isElementLoaded(shiftEndInputOnNewCreateShiftPage, 5)) {
            isLoaded = true;
            SimpleUtils.report("The shift start and end inputs are loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The shift start and end inputs are not loaded on New Create Shift page! ");
        return isLoaded;
    }

    public boolean checkIfNextButtonIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(btnSaveOnNewCreateShiftPage, 5) &&
                btnSaveOnNewCreateShiftPage.getText().equalsIgnoreCase("NEXT")) {
            isLoaded = true;
            SimpleUtils.report("The Next button is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Next button is not loaded on New Create Shift page! ");
        return isLoaded;
    }

    public boolean checkIfCreateButtonIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(btnSaveOnNewCreateShiftPage, 5) &&
                btnSaveOnNewCreateShiftPage.getText().equalsIgnoreCase("CREATE")) {
            isLoaded = true;
            SimpleUtils.report("The Create button is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Create button is not loaded on New Create Shift page! ");
        return isLoaded;
    }

    public boolean checkIfCancelButtonIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(backButtonOnNewCreateShiftPage, 5) &&
                backButtonOnNewCreateShiftPage.getText().equalsIgnoreCase("CANCEL")) {
            isLoaded = true;
            SimpleUtils.report("The Cancel button is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Cancel button is not loaded on New Create Shift page! ");
        return isLoaded;
    }

    public boolean checkIfBackButtonIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(backButtonOnNewCreateShiftPage, 5) &&
                backButtonOnNewCreateShiftPage.getText().equalsIgnoreCase("BACK")) {
            isLoaded = true;
            SimpleUtils.report("The Back button is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Back button is not loaded on New Create Shift page! ");
        return isLoaded;
    }

    @FindBy(css = "[id*=\"ShiftsPerDay_field\"]")
    private WebElement shiftPerDayInputOnNewCreateShiftPage;
    public boolean checkIfShiftPerDayInputIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(shiftPerDayInputOnNewCreateShiftPage, 5)) {
            isLoaded = true;
            SimpleUtils.report("The Shift Per Day input is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Shift Per Day input is not loaded on New Create Shift page! ");
        return isLoaded;
    }

    public int getShiftPerDayValue () throws Exception {
        int shiftPerDay = 0;
        if (isElementLoaded(shiftPerDayInputOnNewCreateShiftPage, 5)) {
            shiftPerDay = Integer.parseInt(shiftPerDayInputOnNewCreateShiftPage.getAttribute("value"));
            SimpleUtils.pass("Get the Shift Per Day value successfully! ");
        } else
            SimpleUtils.fail("The Shift Per Day input is not loaded on New Create Shift page! ",false);
        return shiftPerDay;
    }


    public boolean checkIfSelectDaysCheckBoxAreLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (areListElementVisible(weekDaysInNewCreateShiftPage, 5)
                && weekDaysInNewCreateShiftPage.size()==7) {
            isLoaded = true;
            SimpleUtils.report("The select days checkboxes are loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The select days checkboxes are not loaded on New Create Shift page! ");
        return isLoaded;
    }

    public boolean checkIfAssignmentDropDownListIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(assignmentDropDownOnNewCreateShiftPage, 5)) {
            isLoaded = true;
            SimpleUtils.report("The Assignment dropdown list is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Assignment dropdown list is not loaded on New Create Shift page! ");
        return isLoaded;
    }


    @FindBy(css = "[name=\"shiftNotes\"]")
    private WebElement shiftNotesOnNewCreateShiftPage;
    @FindBy(css = ".tma-staffing-note-text")
    private WebElement shiftNotesOnOldCreateShiftPage;
    public boolean checkIfShiftNotesTextAreaIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(shiftNotesOnNewCreateShiftPage, 5)) {
            isLoaded = true;
            SimpleUtils.report("The Shift Notes textarea is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Shift Notes textarea is not loaded on New Create Shift page! ");
        return isLoaded;
    }

    @FindBy(css = "[ng-click=\"close()\"]")
    private WebElement closeIconOnNewCreateShiftPage;
    public boolean checkIfCloseIconIsLoadedOnNewCreateShiftPage () throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(closeIconOnNewCreateShiftPage, 5)) {
            isLoaded = true;
            SimpleUtils.report("The Close icon is loaded on New Create Shift page! ");
        } else
            SimpleUtils.report("The Close icon is not loaded on New Create Shift page! ");
        return isLoaded;
    }

    public void closeNewCreateShiftPage () throws Exception {
        if (isElementLoaded(closeIconOnNewCreateShiftPage, 5)) {
            clickTheElement(closeIconOnNewCreateShiftPage);
            waitForSeconds(8);
            if (!checkIfNewCreateShiftPageDisplay()) {
                SimpleUtils.pass("The New Create Shift page been closed successfully! ");
            } else
                SimpleUtils.fail("The New Create Shift page fail to close! ", false);
        } else
            SimpleUtils.report("The Close icon is not loaded on New Create Shift page! ");
    }

    @FindBy(xpath = "//div[contains(@id,'WorkRole_menu')]/following-sibling::p")
    private WebElement workRoleWarningMessageOnNewShiftPage;
    public boolean checkIfWorkRoleWarningMessageIsLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(workRoleWarningMessageOnNewShiftPage, 5)
                && workRoleWarningMessageOnNewShiftPage.getText().equalsIgnoreCase("Must select the shift role")) {
            isLoaded = true;
            SimpleUtils.report("The work role warning message is loaded! ");
        } else
            SimpleUtils.report("The work role warning message is not loaded! ");
        return isLoaded;
    }

    @FindBy(xpath = "//div[@id='create-new-shift-react']/div/div/form/div/div/div[9]/div[2]/div/p")
    private WebElement assignmentWarningMessageOnNewShiftPage;
    public boolean checkIfAssignmentWarningMessageIsLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(assignmentWarningMessageOnNewShiftPage, 5)
                && assignmentWarningMessageOnNewShiftPage.getText().equalsIgnoreCase("Must select the assignment mode")) {
            isLoaded = true;
            SimpleUtils.report("The assignment warning message is loaded! ");
        } else
            SimpleUtils.report("The assignment warning message is not loaded! ");
        return isLoaded;
    }

    @FindBy(css = "[class*=\"MuiGrid-root MuiGrid-item MuiGrid-grid-xs-10\"] p")
    private WebElement selectDaysWarningMessageOnNewShiftPage;
    public boolean checkIfSelectDaysWarningMessageIsLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(selectDaysWarningMessageOnNewShiftPage, 5)
                && selectDaysWarningMessageOnNewShiftPage.getText().equalsIgnoreCase("At least one day should be selected")) {
            isLoaded = true;
            SimpleUtils.report("The select days warning message is loaded! ");
        } else
            SimpleUtils.report("The select days warning message is not loaded! ");
        return isLoaded;
    }


    @FindBy(css = "[id*=\"ShiftStart_field-helper-text\"]")
    private WebElement shiftStartWarningMessageOnNewShiftPage;
    public String getShiftStartWarningMessage() throws Exception {
        String warningMessage = "";
        if (isElementLoaded(shiftStartWarningMessageOnNewShiftPage, 5)) {
            warningMessage = shiftStartWarningMessageOnNewShiftPage.getText();
            SimpleUtils.report("Get shift start warning message successfully! ");
        } else
            SimpleUtils.report("The select days warning message is not loaded! ");
        return warningMessage;
    }

    @FindBy(css = "[id*=\"ShiftEnd_field-helper-text\"]")
    private WebElement shiftEndWarningMessageOnNewShiftPage;
    public String getShiftEndWarningMessage() throws Exception {
        String warningMessage = "";
        if (isElementLoaded(shiftEndWarningMessageOnNewShiftPage, 5)) {
            warningMessage = shiftEndWarningMessageOnNewShiftPage.getText();
            SimpleUtils.report("Get shift start warning message successfully! ");
        } else
            SimpleUtils.report("The select days warning message is not loaded! ");
        return warningMessage;
    }

    @FindBy(css = "[id*=\"ShiftsPerDay_field-helper-text\"]")
    private WebElement shiftPerDayWarningMessageOnNewShiftPage;
    public String getShiftPerDayWarningMessage() throws Exception {
        String warningMessage = "";
        if (isElementLoaded(shiftPerDayWarningMessageOnNewShiftPage, 5)) {
            warningMessage = shiftPerDayWarningMessageOnNewShiftPage.getText();
            SimpleUtils.report("Get shift per day warning message successfully! ");
        } else
            SimpleUtils.report("The select shift per day warning message is not loaded! ");
        return warningMessage;
    }

    public void setShiftPerDayOnNewCreateShiftPage (int shiftPerDay) throws Exception {
        if (isElementLoaded(shiftPerDayInputOnNewCreateShiftPage, 5)) {
//            shiftPerDayInputOnNewCreateShiftPage.clear();
            shiftPerDayInputOnNewCreateShiftPage.sendKeys(Keys.CONTROL, "a");
            shiftPerDayInputOnNewCreateShiftPage.sendKeys(Keys.DELETE);
            shiftPerDayInputOnNewCreateShiftPage.sendKeys(String.valueOf(shiftPerDay));
            SimpleUtils.report("Set Shift Per Day on New Create Shift page successfully! ");
        } else
            SimpleUtils.report("The Shift Per Day input is not loaded on New Create Shift page! ");
    }


    public void moveMouseToSpecificWeekDayOnNewCreateShiftPage (String weekDay) throws Exception {
        if (areListElementVisible(weekDaysInNewCreateShiftPage, 5)) {
            for (WebElement day: weekDaysInNewCreateShiftPage) {
                if (day.getText().contains(weekDay)){
                    scrollToElement(day);
                    moveToElementAndClick(day);
                    SimpleUtils.pass("Move to the specific day on New Create Shift page successfully! ");
                    break;
                }
            }
        } else
            SimpleUtils.fail("The select days are not loaded on New Create Shift page! ", false);
    }

    @FindBy(css = "[role=\"tooltip\"] p")
    private WebElement closedDayTooltip;
    public boolean checkClosedDayTooltipIsLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(closedDayTooltip, 5)
                && closedDayTooltip.getText().equalsIgnoreCase("Store Closed")) {
            isLoaded = true;
            SimpleUtils.report("The closed day tooltip is loaded! ");
        } else
            SimpleUtils.report("The closed day tooltip is not loaded! ");
        return isLoaded;
    }


    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[2]/div/div")
    private List<WebElement> assignAndOfferSections;
    public boolean checkIfShiftAssignAndOffersSectionsAreLoaded() throws Exception {
        boolean isLoaded = false;
        if (areListElementVisible(assignAndOfferSections, 5) && assignAndOfferSections.size()==4) {
            isLoaded = true;
            SimpleUtils.report("The shift assign and offer sections are loaded! ");
        } else
            SimpleUtils.report("The shift assign and offer sections are not loaded! ");
        return isLoaded;
    }

    public String getShiftAssignedMessage() throws Exception {
        String message = "";
        if (areListElementVisible(assignAndOfferSections, 5) && assignAndOfferSections.size()==4) {
            message = assignAndOfferSections.get(0).getText();
            SimpleUtils.report("Get shift assigned message successfully! ");
        } else
            SimpleUtils.fail("The shift assign and offer sections are not loaded! ", false);
        return message;
    }

    public String getShiftOffersMessage() throws Exception {
        String message = "";
        if (areListElementVisible(assignAndOfferSections, 5) && assignAndOfferSections.size()==4) {
            message = assignAndOfferSections.get(2).getText();
            SimpleUtils.report("Get shift offer message successfully! ");
        } else
            SimpleUtils.fail("The shift assign and offer sections are not loaded! ", false);
        return message;
    }

    @FindBy(css = "[width=\"40\"][fill=\"none\"]")
    private List<WebElement> openShiftsOnShiftAssignedSections;
    public int getOpenShiftCountOnShiftAssignedSection() {
        int openShiftCount = 0;
        if (areListElementVisible(openShiftsOnShiftAssignedSections, 5)) {
            openShiftCount = openShiftsOnShiftAssignedSections.size();
            SimpleUtils.pass("Get open shift count: "+openShiftCount + " successfully! ");
        } else
            SimpleUtils.report("There is no open shift on the shift assigned section! ");
        return openShiftCount;
    }

    @FindBy(css = "div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-6")
    private WebElement shiftCardOnSearchTMPage;
    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div/div/p")
    private WebElement noOfShiftPerDayAndDaysScheduled;
    public int getNoOfShiftPerDayOnSearchTMPage() throws Exception {
        int noOfShiftPerDay = 0;
        if (isElementLoaded(noOfShiftPerDayAndDaysScheduled, 5)) {
            String noOfShiftPerDayMessage = noOfShiftPerDayAndDaysScheduled.getText().split("Days")[0];
            noOfShiftPerDay = Integer.parseInt(noOfShiftPerDayMessage.split(":")[1].trim());
            SimpleUtils.pass("Get no of shift per day successfully! ");
        } else
            SimpleUtils.fail("The no of shift per day message fail to load! ", false);
        return noOfShiftPerDay;
    }


    public String getDaysScheduledOnSearchTMPage() throws Exception {
        String daysScheduled = "";
        if (isElementLoaded(noOfShiftPerDayAndDaysScheduled, 5)) {
            String noOfShiftPerDayMessage = noOfShiftPerDayAndDaysScheduled.getText().split(":")[noOfShiftPerDayAndDaysScheduled.getText().split(":").length-1];
            daysScheduled = noOfShiftPerDayMessage;
            SimpleUtils.pass("Get days scheduled successfully! ");
        } else
            SimpleUtils.fail("The days scheduled message fail to load! ", false);
        return daysScheduled;
    }

    public String getShiftCardInfoOnSearchTMPage() throws Exception {
        String shiftCardInfo = "";
        if (isElementLoaded(shiftCardOnSearchTMPage, 5)) {
            shiftCardInfo = shiftCardOnSearchTMPage.getText();
            SimpleUtils.pass("Get shift card info successfully! ");
        } else
            SimpleUtils.fail("The shift card fail to load! ", false);
        return shiftCardInfo;
    }

    @FindBy(css = "label.MuiFormControlLabel-labelPlacementEnd")
    private WebElement assignShiftsForEachDaySwitch;
    public boolean checkAssignShiftsForEachDaySwitchIsLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(assignShiftsForEachDaySwitch, 10)) {
            isLoaded = true;
            SimpleUtils.report("Assign shifts for each day switch is loaded successfully! ");
        } else
            SimpleUtils.report("Assign shifts for each day switch fail to load! ");
        return isLoaded;
    }

    public boolean checkAssignShiftsForEachDaySwitchIfEnabled() throws Exception {
        boolean isEnabled = false;
        if (isElementLoaded(assignShiftsForEachDaySwitch, 10)) {
            if (assignShiftsForEachDaySwitch.findElement
                    (By.cssSelector("span.MuiSwitch-switchBase")).getAttribute("class").contains("checked")){
                isEnabled = true;
                SimpleUtils.report("Assign shifts for each day switch is enabled! ");
            } else {
                SimpleUtils.report("Assign shifts for each day switch is not enabled! ");
            }
        } else
            SimpleUtils.fail("Assign shifts for each day switch fail to load! ", false);
        return isEnabled;
    }

    public void openOrCloseAssignShiftsForEachDaySwitch(boolean toOpen) throws Exception {
        if (isElementLoaded(assignShiftsForEachDaySwitch, 10)) {
            if (toOpen) {
                if (assignShiftsForEachDaySwitch.findElement
                        (By.cssSelector("span.MuiSwitch-switchBase")).getAttribute("class").contains("checked")){
                    SimpleUtils.pass("Assign shifts for each day switch already been open! ");
                } else {
                    clickTheElement(assignShiftsForEachDaySwitch.findElement(By.cssSelector("input.MuiSwitch-input")));
                    if (assignShiftsForEachDaySwitch.findElement
                            (By.cssSelector("span.MuiSwitch-switchBase")).getAttribute("class").contains("checked")) {
                        SimpleUtils.pass("Open assign shifts for each day switch successfully! ");
                    } else
                        SimpleUtils.fail("Fail to open assign shifts for each day switch! ", false);
                }
            } else {
                if (assignShiftsForEachDaySwitch.findElement
                        (By.cssSelector("span.MuiSwitch-switchBase")).getAttribute("class").contains("checked")){
                    clickTheElement(assignShiftsForEachDaySwitch.findElement(By.cssSelector("input.MuiSwitch-input")));
                    if (!assignShiftsForEachDaySwitch.findElement
                            (By.cssSelector("span.MuiSwitch-switchBase")).getAttribute("class").contains("checked")) {
                        SimpleUtils.pass("Close assign shifts for each day switch successfully! ");
                    } else
                        SimpleUtils.fail("Fail to close assign shifts for each day switch! ", false);
                } else {
                    SimpleUtils.pass("Assign shifts for each day switch already been closed! ");
                }
            }
            SimpleUtils.report("Assign shifts for each day switch is loaded successfully! ");
        } else
            SimpleUtils.report("Assign shifts for each day switch fail to load! ");
    }

    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[1]/div[1]/div")
    private List<WebElement> assignShiftsEachDays;
    public List<String> getAssignShiftsMessageOfEachDays() {
        List<String> messages = new ArrayList<>();
        if (areListElementVisible(assignShiftsEachDays, 5)) {
            for (int i= 0; i< assignShiftsEachDays.size(); i++) {
                messages.add(assignShiftsEachDays.get(i).getText());
            }
            SimpleUtils.report("Get assign shifts message for each day successfully! ");
        } else
            SimpleUtils.report("The assign shifts each day is not loaded! ");
        return messages;
    }


    public List<WebElement> getSearchAndRecommendedResult() {
        List<WebElement> result = new ArrayList<>();
        if (areListElementVisible(searchResultsOnNewCreateShiftPage, 25)) {
            result = searchResultsOnNewCreateShiftPage;
            SimpleUtils.report("Get search result successfully! ");
        } else
            SimpleUtils.report("The assign shifts each day is not loaded! ");
        return result;
    }



    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[2]/div/div[2]/div/div/div")
    private List<WebElement> assignedShiftsOnShiftAssignedSections;
    public List<String> getAssignedShiftOnShiftAssignedSection() {
        List<String> assignedShifts = new ArrayList<>();
        if (areListElementVisible(assignedShiftsOnShiftAssignedSections, 5)) {
            for (WebElement shift: assignedShiftsOnShiftAssignedSections) {
                String tmName = shift.findElement(By.cssSelector("img")).getAttribute("alt");
                assignedShifts.add(tmName);
                SimpleUtils.pass("Get assigned shift: "+tmName + " successfully! ");
            }
        } else
            SimpleUtils.report("There is no assigned shift on the shift assigned section! ");
        return assignedShifts;
    }

    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[2]/div/div[4]/div/div/div")
    private List<WebElement> shiftOffersOnShiftAssignedSections;
    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[2]/div/div[4]/button")
    private WebElement clearOffersLink;
    public List<String> getShiftOffersOnShiftAssignedSection() {
        List<String> shiftOffers = new ArrayList<>();
        if (areListElementVisible(shiftOffersOnShiftAssignedSections, 5)) {
            for (WebElement shift: shiftOffersOnShiftAssignedSections) {
                String tmName = shift.findElement(By.cssSelector("img")).getAttribute("alt");
                shiftOffers.add(tmName);
                SimpleUtils.pass("Get assigned shift: "+tmName + " successfully! ");
            }
        } else
            SimpleUtils.report("There is no assigned shift on the shift assigned section! ");
        return shiftOffers;
    }


    public void clickClearOfferLink() throws Exception {
        if (isElementLoaded(clearOffersLink, 5)) {
            clickTheElement(clearOffersLink);
            if (!isElementLoaded(clearOffersLink, 3)) {
                SimpleUtils.pass("Click clear offers link successfully! ");
            } else
                SimpleUtils.fail("Fail to click offers assignments link! ", false);
        } else
            SimpleUtils.report("The clear offers link fail to load! ");
    }


    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[2]/div/div[2]/div/div/button")
    private List<WebElement> removeIconsForAssignedShift;
    @FindBy(xpath = "//div[contains(@class,'legion-ui-react')]/div/div[2]/div/div[4]/div/div/button")
    private List<WebElement> removeIconsForOfferedShift;
    public void removeAllAssignedShiftByClickRemoveIcon() {
        if (areListElementVisible(removeIconsForAssignedShift, 5)) {
            for (WebElement removeIcon: removeIconsForAssignedShift) {
                clickTheElement(removeIcon);
                SimpleUtils.pass("Remove one assigned shift successfully! ");
            }
        } else
            SimpleUtils.report("There is no remove icon display on Shift Assigned section! ");
    }

    public void removeAllOfferedShiftByClickRemoveIcon() {
        if (areListElementVisible(removeIconsForOfferedShift, 5)) {
            for (WebElement removeIcon: removeIconsForOfferedShift) {
                clickTheElement(removeIcon);
                SimpleUtils.pass("Remove one offered shift successfully! ");
            }
        } else
            SimpleUtils.report("There is no remove icon display on Shift Offers section! ");
    }

    @FindBy(css = "[data-testid=\"confirm-console-wrapper\"]")
    private WebElement confirmPopup;
    @FindBy(css = "[data-testid=\"confirm-console-wrapper\"] button")
    private List<WebElement> buttonsOnConfirmPopup;
    @FindBy(css = "[data-testid=\"confirm-console-wrapper\"] div p")
    private WebElement titleOnConfirmPopup;
    @FindBy(xpath = "//div[contains(@data-testid,'confirm-console-wrapper')]/div/span")
    private WebElement messageOnConfirmPopup;
    public boolean checkConfirmPopupIsLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(confirmPopup, 15)) {
            isLoaded = true;
            SimpleUtils.report("The confirm popup is loaded! ");
        } else
            SimpleUtils.report("The confirm popup is not loaded! ");
        return isLoaded;
    }

    public String getTitleOfConfirmPopup() throws Exception {
        String title = "";
        if (isElementLoaded(confirmPopup, 15)) {
            if(isElementLoaded(titleOnConfirmPopup, 5)) {
                title = titleOnConfirmPopup.getText();
                SimpleUtils.report("Get the title on confirm popup successfully! ");
            } else
                SimpleUtils.fail("The title on confirm popup fail to load! ", false);
        } else
            SimpleUtils.fail("The confirm popup is not loaded! ", false);
        return title;
    }

    public String getMessageOfConfirmPopup() throws Exception {
        String message = "";
        if (isElementLoaded(confirmPopup, 15)) {
            if(isElementLoaded(messageOnConfirmPopup, 5)) {
                message = messageOnConfirmPopup.getText();
                SimpleUtils.report("Get message on confirm popup successfully! ");
            } else
                SimpleUtils.fail("The message on confirm popup fail to load! ", false);
        } else
            SimpleUtils.fail("The confirm popup is not loaded! ", false);
        return message;
    }

    public void clickOkBtnOnConfirmPopup() throws Exception {
        if (areListElementVisible(buttonsOnConfirmPopup, 5)
                && buttonsOnConfirmPopup.size()==2) {
            clickTheElement(buttonsOnConfirmPopup.get(1));
            SimpleUtils.report("Click the Ok button successfully! ");
        } else
            SimpleUtils.fail("The buttons on confirm popup is not loaded! ", false);
    }


    public void selectAssignShiftDaysByIndex(int index) {
        if (areListElementVisible(assignShiftsEachDays, 5)) {
            for (int i= 0; i< assignShiftsEachDays.size(); i++) {
                if (index == i) {
                    clickTheElement(assignShiftsEachDays.get(i));
                    SimpleUtils.pass("Click the assign shift day successfully! ");
                    break;
                }
            }
            SimpleUtils.report("Get assign shifts message for each day successfully! ");
        } else
            SimpleUtils.fail("The assign shifts each days are not loaded! ", false);
    }


    public void selectAssignShiftDaysByDayName(String dayName) {
        if (areListElementVisible(assignShiftsEachDays, 5)) {
            for (int i= 0; i< assignShiftsEachDays.size(); i++) {
                if (assignShiftsEachDays.get(i).getText().toLowerCase().contains(dayName.toLowerCase())) {
                    clickTheElement(assignShiftsEachDays.get(i));
                    SimpleUtils.pass("Click the assign shift day successfully! ");
                    break;
                }
            }
            SimpleUtils.report("Get assign shifts message for each day successfully! ");
        } else
            SimpleUtils.fail("The assign shifts each days are not loaded! ", false);
    }

    public void clickAssignShiftsForEachDaySwitch() throws Exception {
        if (isElementLoaded(assignShiftsForEachDaySwitch, 10)) {
            clickTheElement(assignShiftsForEachDaySwitch.findElement(By.cssSelector("input.MuiSwitch-input")));
            SimpleUtils.report("Click assign shifts for each day switch successfully! ");
        } else
            SimpleUtils.report("Assign shifts for each day switch fail to load! ");
    }

    public void setShiftNotesOnNewCreateShiftPage (String shiftNotes) throws Exception {
        if (isElementLoaded(shiftNotesOnNewCreateShiftPage, 5)) {
            shiftNotesOnNewCreateShiftPage.sendKeys(Keys.CONTROL, "a");
            shiftNotesOnNewCreateShiftPage.sendKeys(Keys.DELETE);
            shiftNotesOnNewCreateShiftPage.sendKeys(String.valueOf(shiftNotes));
            SimpleUtils.report("Set Shift Notes on New Create Shift page successfully! ");
        } else if (isElementLoaded(shiftNotesOnOldCreateShiftPage, 5)) {
            shiftNotesOnOldCreateShiftPage.sendKeys(Keys.CONTROL, "a");
            shiftNotesOnOldCreateShiftPage.sendKeys(Keys.DELETE);
            shiftNotesOnOldCreateShiftPage.sendKeys(String.valueOf(shiftNotes));
            SimpleUtils.report("Set Shift Notes on New Create Shift page successfully! ");
        } else
            SimpleUtils.report("The Shift Notes textarea is not loaded on New Create Shift page! ");
    }

    public void setShiftNameOnNewCreateShiftPage (String shiftName) throws Exception {
        if (isElementLoaded(shiftNameOnNewCreateShiftPage, 5)) {
            shiftNameOnNewCreateShiftPage.sendKeys(Keys.CONTROL, "a");
            shiftNameOnNewCreateShiftPage.sendKeys(Keys.DELETE);
            shiftNameOnNewCreateShiftPage.sendKeys(String.valueOf(shiftName));
            SimpleUtils.report("Set shift name on New Create Shift page successfully! ");
        } else if (isElementLoaded(shiftNameOnOldCreateShiftPage, 5)) {
            shiftNameOnOldCreateShiftPage.sendKeys(Keys.CONTROL, "a");
            shiftNameOnOldCreateShiftPage.sendKeys(Keys.DELETE);
            shiftNameOnOldCreateShiftPage.sendKeys(shiftName);
            SimpleUtils.report("Set shift name on New Create Shift page successfully! ");
        }else
            SimpleUtils.report("The shift name input is not loaded on New Create Shift page! ");
    }

    @FindBy(css = "[color=\"grey\"]")
    private WebElement greyAvailableIcon;
    @FindBy(css = "[color=\"#37cf3f\"]")
    private WebElement greenAvailableIcon;
    @FindBy(css = "[data-testid=\"ThumbDownIcon\"]")
    private WebElement redAvailableIcon;
    @Override
    public String getTMAvailableColourForAssignedShift () throws Exception {
        String availableIconColour = null;
        if (isElementLoaded(greenAvailableIcon, 5)) {
            return availableIconColour = "green";
        }else if(isElementLoaded(greyAvailableIcon, 5)){
            return availableIconColour = "grey";
        }else if(isElementLoaded(redAvailableIcon, 5)) {
            return availableIconColour = "red";
        }else
            SimpleUtils.fail("The available icon is not displayed!", false);
        return null;
    }

    private boolean isRecommendedTabSelected(){
        boolean isRecommendedTabSelected = false;
        if (areListElementVisible(searchAndRecommendedTMTabs, 5)){
            if (searchAndRecommendedTMTabs.get(0).getAttribute("aria-selected").equalsIgnoreCase("false")){
                isRecommendedTabSelected = true;
                SimpleUtils.report("The search TM tab is selected! ");
            } else
                SimpleUtils.report("The recommended tab is selected! ");
        } else
            SimpleUtils.fail("Select Team member option and Recommended options are not available on page", false);
        return isRecommendedTabSelected;
    }

    @FindBy(css = "[id*='Close_button']")
    private WebElement closeBtnForCreateShift;
    @FindBy(className = "modal-instance-header-title ng-binding")
    private WebElement createShiftTitle;
    @Override
    public void clickCloseBtnForCreateShift() throws Exception {
        waitForSeconds(2);
        if (isElementLoaded(closeBtnForCreateShift,5)) {
            clickTheElement(closeBtnForCreateShift);
            if (!isElementLoaded(createShiftTitle))
                SimpleUtils.report("The Create Shift dialog is closed!");
            else
                SimpleUtils.fail("The Create Shift dialog is not closed!", false);
        } else if (isElementLoaded(closeSelectTMWindowBtn, 5)) {
            closeCustomizeNewShiftWindow();
        } else{
            SimpleUtils.fail("The Close button is not loaded correctly!", false);
        }
    }
    
    @FindBy(css = "[id*=\"ShiftStart_field\"]")
    private WebElement startTimeInput;
    @FindBy(css = "[id*=\"ShiftEnd_field\"]")
    private WebElement endTimeInput;

    @Override
    public void setStartTimeAndEndTimeForShift(String start, String end) throws Exception {
        String availableIconColour = null;
        if (isElementLoaded(startTimeInput, 5)) {
            startTimeInput.clear();
            startTimeInput.sendKeys(start);
        }
        if (isElementLoaded(endTimeInput, 5)) {
            endTimeInput.clear();
            endTimeInput.sendKeys(end);
        }
    }
  
    @FindBy(id = "legion_cons_Schedule_Schedule_EditShifts_Exit_button")
    private WebElement closeBtnForOfferShift;
    @FindBy(css = ".tma-header-text.fl-left.ng-binding")
    private WebElement offerShiftdialogTitle;
    @Override
    public void clickCloseBtnForOfferShift() throws Exception {
        if (isElementLoaded(closeBtnForOfferShift,5)) {
            clickTheElement(closeBtnForOfferShift);
            if (!isElementLoaded(offerShiftdialogTitle))
                SimpleUtils.report("The Offer Shift dialog is closed!");
            else
                SimpleUtils.fail("The Offer Shift dialog is not closed!", false);
        }else{
            SimpleUtils.fail("The Close button is not loaded correctly!", false);
        }
    }

    public void clickOnOkButtonOnErrorDialog () throws Exception {
        try {
            if (isElementLoaded(okBtnInWarningMode, 5)) {
                clickTheElement(okBtnInWarningMode);
            } else
                SimpleUtils.report("Toggle: EnableNewSchedulingErrorHandlingSCH is disabled");
        } catch (Exception e) {
            SimpleUtils.report("Toggle: EnableNewSchedulingErrorHandlingSCH is disabled");
        }
    }

    public void inputTeamMember(String name) throws Exception {
        if (areListElementVisible(searchAndRecommendedTMTabs, 10)) {
            if (searchAndRecommendedTMTabs.size() == 2) {
                //click(btnSearchteamMember.get(1));
                if (isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
                    textSearchOnNewCreateShiftPage.sendKeys(Keys.CONTROL, "a");
                    textSearchOnNewCreateShiftPage.sendKeys(Keys.DELETE);
                    textSearchOnNewCreateShiftPage.sendKeys(name);
                    waitForSeconds(3);
                }else
                    SimpleUtils.fail("Search box in shift creation page is not loaded correctly!",false);
            }else
                SimpleUtils.fail("Search employee tab or recommended employee tab is not loaded correctly!",false);
        }else
            SimpleUtils.fail("Shift creation dialog is not loaded correctly!",false);
    }

    public void searchEmployee(String searchInput) throws Exception {
        String[] searchAssignTeamMember = searchInput.split(",");
        if (isElementLoaded(textSearch, 10) && isElementLoaded(searchIcon, 10)) {
            for (int i = 0; i < searchAssignTeamMember.length; i++) {
                String[] searchTM = searchAssignTeamMember[i].split("\\.");
                textSearch.sendKeys(searchTM[0]);
                click(searchIcon);
            }
        } else if (isElementLoaded(textSearchOnNewCreateShiftPage, 5)) {
            for (int i = 0; i < searchAssignTeamMember.length; i++) {
                String[] searchTM = searchAssignTeamMember[i].split("\\.");
                textSearchOnNewCreateShiftPage.sendKeys(searchTM[0]);
                waitForSeconds(3);
            }
        }else {
            SimpleUtils.fail("Search text not editable and icon are not clickable", false);
        }

    }

}
